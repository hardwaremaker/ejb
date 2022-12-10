package com.lp.server.fertigung.bl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.eingangsrechnung.service.VendidataImportStats;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.IVendidataArticleImporterBeanServices;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LosistmaterialDto;
import com.lp.server.fertigung.service.LoslagerentnahmeDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.fertigung.service.VendidataArticleConsumption;
import com.lp.server.fertigung.service.VendidataArticleConsumptionImportResult;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.stueckliste.service.MontageartDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.util.EJBExceptionLP;
import com.lp.util.EJBVendidataArticleExceptionLP;
import com.lp.util.Helper;

public class VendidataArticleConsumptionImporter implements Serializable {
	private static final long serialVersionUID = 93509585665580767L;

	protected final ILPLogger myLogger = LPLogService.getInstance().getLogger(this.getClass());
	
	private IVendidataArticleImporterBeanServices beanServices;
	private List<VendidataArticleConsumption> importVerbrauch;
	private VendidataImportStats stats;
	
	//Grunddaten
	private FertigungsgruppeDto fertigungsgruppeDto;
	private KostenstelleDto kostenstelleDto;
	private MontageartDto montageartDto;

	
	public VendidataArticleConsumptionImporter(IVendidataArticleImporterBeanServices beanServices) {
		this.beanServices = beanServices;
		importVerbrauch = new ArrayList<VendidataArticleConsumption>();
		stats = new VendidataImportStats();
	}

	public VendidataArticleConsumptionImportResult checkImportXMLDaten(List<VendidataArticleConsumption> verbrauch) {
		List<EJBVendidataArticleExceptionLP> importErrors = new ArrayList<EJBVendidataArticleExceptionLP>();
		
		try {
			importVerbrauch.clear();
			stats.reset();
			List<EJBVendidataArticleExceptionLP> checkErrorsGrunddaten = checkGrunddaten();
			importErrors.addAll(checkErrorsGrunddaten);
			for (VendidataArticleConsumption vac : verbrauch) {
				List<EJBVendidataArticleExceptionLP> checkErrors = checkArtikelVerbrauch(vac);
				if (checkErrors.isEmpty() && checkErrorsGrunddaten.isEmpty()) {
					importVerbrauch.add(vac);
					stats.incrementGoodImportsAusgangsgutschriften();
				} else {
					importErrors.addAll(checkErrors);
				}
				
			}
		} catch (Exception ex) {
			stats.incrementErrorCounts();
			importErrors.add(new EJBVendidataArticleExceptionLP(EJBVendidataArticleExceptionLP.SEVERITY_ERROR, 
					EJBExceptionLP.FEHLER_FERTIGUNG_IMPORT_FIELD_INSTANZIERUNG_FEHLGESCHLAGEN, ex));
		}
		
		return new VendidataArticleConsumptionImportResult(importErrors, stats);
	}

	public VendidataArticleConsumptionImportResult checkLosstatus(List<VendidataArticleConsumption> verbrauch) {
		List<EJBVendidataArticleExceptionLP> importErrors = new ArrayList<EJBVendidataArticleExceptionLP>();
		stats.reset();
		LosDto losDto = null;
		Map<Integer, LosDto> lose = new HashMap<Integer, LosDto>();
		for (VendidataArticleConsumption vac : verbrauch) {
			if (losDto == null || !losMatchesDatumTourNr(vac, losDto)) {
				try {
					losDto = getLosDto(vac, false);
				} catch (RemoteException e) {
				}
				if (losDto != null) lose.put(losDto.getIId(), losDto);
			}
		}
		
		importErrors.addAll(checkLose(lose));
		
		return new VendidataArticleConsumptionImportResult(importErrors, stats);
	}
	
	/**
	 * @param lose
	 */
	private List<EJBVendidataArticleExceptionLP> checkLose(Map<Integer, LosDto> lose) {
		List<EJBVendidataArticleExceptionLP> errors = new ArrayList<EJBVendidataArticleExceptionLP>();
		for (Entry<Integer, LosDto> entry : lose.entrySet()) {
			if (losExistsStatusAngelegt(entry.getValue())) {
				stats.incrementErrorCounts();
				errors.add(new EJBVendidataArticleExceptionLP(EJBVendidataArticleExceptionLP.SEVERITY_ERROR, 
						EJBExceptionLP.FEHLER_FERTIGUNG_IMPORT_LOSSTATUS, entry.getValue().getCNr(), entry.getValue().getStatusCNr().trim()));
			}
		}
		
		return errors;
	}
	
//	public VendidataArticleConsumptionImportResult importXMLDaten(String xmlDaten) {
//		VendidataArticleConsumptionImportResult result = checkImportXMLDaten(xmlDaten);
//		importProduktVerbraeuche(result, importVerbrauch, new GebeMaterialNachtraglichAus());
//		
//		return result;
//	}
	
	public VendidataArticleConsumptionImportResult importXMLDaten(List<VendidataArticleConsumption> list) {
		VendidataArticleConsumptionImportResult result = 
				new VendidataArticleConsumptionImportResult(new ArrayList<EJBVendidataArticleExceptionLP>(), stats);
		importProduktVerbraeuche(result, list, new GebeMaterialNachtraglichAus());
		
		return result;
	}
	
	public VendidataArticleConsumptionImportResult importFehlmenge(VendidataArticleConsumption vac) {
		VendidataArticleConsumptionImportResult result = 
				new VendidataArticleConsumptionImportResult(new ArrayList<EJBVendidataArticleExceptionLP>(), stats);
		List<VendidataArticleConsumption> list = new ArrayList<VendidataArticleConsumption>();
		list.add(vac);
		importProduktVerbraeuche(result, list, new BucheMaterialAufLos());
		
		return result;
	}

	private List<EJBVendidataArticleExceptionLP> checkArtikelVerbrauch(VendidataArticleConsumption vac) {
		List<EJBVendidataArticleExceptionLP> errors = new ArrayList<EJBVendidataArticleExceptionLP>();
		//mandatory fields set?
		
		//Tour-Lager da?
		if (!existsTourLager(vac.getTourNr())) {
			stats.incrementErrorCounts();
			errors.add(new EJBVendidataArticleExceptionLP(vac.getFourVendingIId(), vac.getArtikelName(), 
					vac.getTourNr(), EJBVendidataArticleExceptionLP.SEVERITY_ERROR, 
					EJBExceptionLP.FEHLER_FERTIGUNG_IMPORT_TOURLAGER_NICHT_VORHANDEN));
		}
		
		//4vending-Id nicht bekannt
		if (!exists4VendingId(vac.getFourVendingIId())) {
			stats.incrementErrorCounts();
			errors.add(new EJBVendidataArticleExceptionLP(vac.getFourVendingIId(), vac.getArtikelName(), 
					vac.getTourNr(), EJBVendidataArticleExceptionLP.SEVERITY_ERROR, 
					EJBExceptionLP.FEHLER_FERTIGUNG_IMPORT_4VENDING_ID_NICHT_VORHANDEN));
		}
		
		return errors;
	}

	private List<EJBVendidataArticleExceptionLP> checkGrunddaten() {
		List<EJBVendidataArticleExceptionLP> errors = new ArrayList<EJBVendidataArticleExceptionLP>();
		if (!existsFertigungsgruppe()) {
			stats.incrementErrorCounts();
			errors.add(new EJBVendidataArticleExceptionLP(EJBVendidataArticleExceptionLP.SEVERITY_ERROR, 
					EJBExceptionLP.FEHLER_FERTIGUNG_IMPORT_KEINE_FERTIGUNGSGRUPPE_VORHANDEN));
		}
		
		if (!existsMontageart()) {
			stats.incrementErrorCounts();
			errors.add(new EJBVendidataArticleExceptionLP(EJBVendidataArticleExceptionLP.SEVERITY_ERROR, 
					EJBExceptionLP.FEHLER_FERTIGUNG_IMPORT_KEINE_MONTAGEART_VORHANDEN));
		}
		
		if (!existsKostenstelle()) {
			stats.incrementErrorCounts();
			errors.add(new EJBVendidataArticleExceptionLP(EJBVendidataArticleExceptionLP.SEVERITY_ERROR, 
					EJBExceptionLP.FEHLER_FERTIGUNG_IMPORT_KEINE_KOSTENSTELLE_DEFINIERT));
		}
		return errors;
	}
	
	private boolean existsKostenstelle() {
		return getKostenstelle() != null ? true : false;
	}

	private boolean existsMontageart() {
		return getMontageart() != null ? true : false;
	}

	private boolean existsFertigungsgruppe() {
		return getFertigungsgruppe() != null ? true : false;
	}

	private boolean exists4VendingId(Integer fourVendingId) {
		return getArtikelBy4VendingId(fourVendingId) != null ? true : false;
	}
	
	private boolean existsTourLager(Integer tourNr) {
		return getLagerByTourNr(tourNr) != null ? true : false;
	}

	private boolean losExistsStatusAngelegt(LosDto losDto) {
		if (losDto == null) return false;
		return FertigungFac.STATUS_STORNIERT.equals(losDto.getStatusCNr()) || FertigungFac.STATUS_ANGELEGT.equals(losDto.getStatusCNr()) 
				|| FertigungFac.STATUS_ERLEDIGT.equals(losDto.getStatusCNr()) ? true : false;
	}

	private ArtikelDto getArtikelBy4VendingId(Integer fourVendingId) {
		try {
			ArtikelDto artikelDto = beanServices.artikelFindBy4VendingId(fourVendingId);
			return artikelDto;
		} catch (RemoteException e) {
		}
		
		return null;
	}
	
	private LagerDto getLagerByTourNr(Integer tourNr) {
		try {
			LagerDto[] laeger = beanServices.lagerFindAll();
			
			for (LagerDto lager : laeger) {
				if (tourNrMatchesLager(tourNr, lager)) {
					return lager;
				}
			}
		} catch (RemoteException e) {
		}
		return null;
	}
	
	private boolean tourNrMatchesLager(Integer tourNr, LagerDto lagerDto) {
		if (tourNr == null || lagerDto == null) return false;
		
		String[] splitName = lagerDto.getCNr().split("(?=\\d*$)",2);
		try {
			if (splitName != null && splitName.length == 2) {
				if (!splitName[1].isEmpty()) {
					if (tourNr == Integer.parseInt(splitName[1])) {
						return true;
					}
				} else if (tourNr == Integer.parseInt(splitName[0])){
					return true;
				}
			}
		} catch (NumberFormatException ex) {
		}
		
		return false;
	}
	
	private FertigungsgruppeDto getFertigungsgruppe() {
		if (fertigungsgruppeDto == null) {
			try {
				fertigungsgruppeDto = beanServices.findFertigungsgruppe();
			} catch (RemoteException e) {
			}
		}
		
		return fertigungsgruppeDto;
	}

	private MontageartDto getMontageart() {
		if (montageartDto == null) {
			try {
				montageartDto = beanServices.findMontageart();
			} catch (RemoteException e) {
			}
		}
		
		return montageartDto;
	}
	
	private KostenstelleDto getKostenstelle() {
		if (kostenstelleDto == null) {
			try {
				KostenstelleDto[] kostenstellen = beanServices.findKostenstelle();
				kostenstelleDto = kostenstellen != null ? kostenstellen[kostenstellen.length-1] : null;
			} catch (RemoteException e) {
			}
		}
		return kostenstelleDto;
	}

	private VendidataArticleConsumptionImportResult importProduktVerbraeuche(VendidataArticleConsumptionImportResult result, 
			List<VendidataArticleConsumption> importVerbrauch, IMaterialBuchungsCall materialBuchungsCall) {
		
		try {
			LosDto losDto = null;
			for (VendidataArticleConsumption vac : importVerbrauch) {
				try {
					losDto = losDto != null && losMatchesDatumTourNr(vac, losDto) ? losDto : getLosDto(vac, true);
				} catch (RemoteException ex) {
					stats.incrementErrorCounts();
					myLogger.error("RemoteException during Los acquisition", ex);
					result.getImportErrors().add(new EJBVendidataArticleExceptionLP(vac.getFourVendingIId(), vac.getArtikelName(), 
							vac.getTourNr(), EJBVendidataArticleExceptionLP.SEVERITY_ERROR, 
							EJBExceptionLP.FEHLER_FERTIGUNG_IMPORT_LOS_ANLEGEN_FEHLGESCHLAGEN, ex));
					break;
				} catch (Exception e) {
					myLogger.error("Error occured during Los acquisition", e);
					result.getImportErrors().add(new EJBVendidataArticleExceptionLP(vac.getFourVendingIId(), vac.getArtikelName(), 
							vac.getTourNr(), EJBVendidataArticleExceptionLP.SEVERITY_ERROR, 
							EJBExceptionLP.FEHLER_FERTIGUNG_IMPORT_LOS_ANLEGEN_FEHLGESCHLAGEN, e));
					break;
				}
				ArtikelDto artikelDto = getArtikelBy4VendingId(vac.getFourVendingIId());
				
				LossollmaterialDto lossollmaterialDto;
				try {
					lossollmaterialDto = getLossollmaterialDto(artikelDto, losDto, vac);
				} catch (RemoteException ex) {
					stats.incrementErrorCounts();
					result.getImportErrors().add(new EJBVendidataArticleExceptionLP(vac.getFourVendingIId(), vac.getArtikelName(), 
							vac.getTourNr(), EJBVendidataArticleExceptionLP.SEVERITY_ERROR, 
							EJBExceptionLP.FEHLER_FERTIGUNG_IMPORT_LOSSOLLMATERIAL_ANLEGEN_FEHLGESCHLAGEN, ex));
					break;
				}

				try {
					LosistmaterialDto losistmaterialDto = getLosistmaterialDto(losDto, vac);
					materialBuchungsCall.bucheMaterial(losDto, lossollmaterialDto, losistmaterialDto);
					stats.incrementGoodImportsAusgangsgutschriften();
					result.addIncludedTour(vac.getTourNr());
					result.addUsedLosDto(losDto);
				} catch (RemoteException ex) {
					stats.incrementErrorImportsAusgangsgutschriften();
					result.getImportErrors().add(new EJBVendidataArticleExceptionLP(vac.getFourVendingIId(), vac.getArtikelName(), 
							vac.getTourNr(), EJBVendidataArticleExceptionLP.SEVERITY_ERROR, 
							EJBExceptionLP.FEHLER_FERTIGUNG_IMPORT_MATERIALAUSGABE_FEHLGESCHLAGEN, ex));
					break;
				}
			}
		} catch (EJBExceptionLP ejbEx) {
			throw ejbEx;
		}
		return result;
	}

	/**
	 * @param losDto
	 * @param vac
	 * @return LosIstMaterial passend zum angeforderten Los
	 */
	private LosistmaterialDto getLosistmaterialDto(LosDto losDto,
			VendidataArticleConsumption vac) {
		LosistmaterialDto losistmaterialDto = new LosistmaterialDto();
		losistmaterialDto.setLagerIId(losDto.getLagerIIdZiel());
		losistmaterialDto.setBAbgang(Helper.boolean2Short(vac.getAnzahlArtikel().signum() == 1));
		losistmaterialDto.setNMenge(vac.getAnzahlArtikel().compareTo(BigDecimal.ZERO) < 0 ? vac.getAnzahlArtikel().negate() : vac.getAnzahlArtikel());
		
		return losistmaterialDto;
	}
	
	private LossollmaterialDto getLossollmaterialDto(ArtikelDto artikelDto, LosDto losDto, VendidataArticleConsumption vac) throws RemoteException {
		LossollmaterialDto lossollmaterialDto = beanServices.getDefaultLossollmaterial(artikelDto, losDto.getIId(), 
				losDto.getLagerIIdZiel(), getMontageart().getIId());
		lossollmaterialDto.setBNachtraeglich(Helper.boolean2Short(false));
		lossollmaterialDto.setNMenge(vac.getAnzahlArtikel());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss");
		lossollmaterialDto.setCKommentar(dateFormat.format(vac.getBuchungsdatum()));
		
		return lossollmaterialDto;
	}

	private LosDto getLosDto(VendidataArticleConsumption vac, boolean bCreateIfNotExists) throws RemoteException {
		MandantDto mandantDto = beanServices.mandantFindByPrimaryKey(beanServices.getTheClientDto().getMandant());
		LosDto[] lose = beanServices.losFindByFertigungsortPartnerIIdMandantCNrOhneExc(mandantDto.getPartnerIId(), mandantDto.getCNr());
		
		for (LosDto losDto : lose) {
			if (losMatchesDatumTourNr(vac, losDto)) {
				return losDto;
			}
		}
		
		if (bCreateIfNotExists) {
			return createDefaultLosDto(vac);
		}

		return null;
	}

	private boolean losMatchesDatumTourNr(VendidataArticleConsumption vac, LosDto losDto) {
		if (!tourNrMatchesLager(vac.getTourNr(), beanServices.lagerFindByPrimaryKey(losDto.getLagerIIdZiel()))) {
			return false;
		}
		
		GregorianCalendar gcVac = new GregorianCalendar();
		gcVac.setTime(vac.getBuchungsdatum());
		GregorianCalendar gcLos = new GregorianCalendar();
		gcLos.setTime(losDto.getTProduktionsbeginn());
		
		if (gcVac.get(Calendar.MONTH) != gcLos.get(Calendar.MONTH) 
				|| gcVac.get(Calendar.YEAR) != gcLos.get(Calendar.YEAR)) {
			return false;
		}
		
		return true;
	}

	private LosDto createDefaultLosDto(VendidataArticleConsumption vac) throws RemoteException {
		LosDto defaultLosDto = beanServices.getDefaultLos();
		LagerDto tourLager = getLagerByTourNr(vac.getTourNr());
		//Projekt setzen
		defaultLosDto.setCProjekt(tourLager.getCNr());
		//richtige Kostenstelle
		defaultLosDto.setKostenstelleIId(getKostenstelle().getIId());
		//Tourlager setzen als Ziel und Quelllager
		defaultLosDto.setLagerIIdZiel(tourLager.getIId());
		GregorianCalendar gcVac = new GregorianCalendar();
		gcVac.setTime(vac.getBuchungsdatum());
		gcVac.set(Calendar.DAY_OF_MONTH, 1);
		defaultLosDto.setTProduktionsbeginn(new Date(gcVac.getTime().getTime()));
		defaultLosDto.setTProduktionsende(new Date(gcVac.getTime().getTime()));
		
		defaultLosDto = beanServices.createLosDto(defaultLosDto);
		LoslagerentnahmeDto loslagerDto = new LoslagerentnahmeDto();
		loslagerDto.setLosIId(defaultLosDto.getIId());
		loslagerDto.setLagerIId(tourLager.getIId());
		loslagerDto = beanServices.createLoslagerentnahme(loslagerDto);
		LoslagerentnahmeDto[] loslaeger = beanServices.loslagerentnahmeFindByLosIId(defaultLosDto.getIId());
		for (LoslagerentnahmeDto loslager : loslaeger) {
			if (!loslager.getIId().equals(loslagerDto.getIId())) {
				beanServices.removeLoslagerentnahme(loslager);
			}
		}
		
		//Los ausgeben
		beanServices.gebeLosAus(defaultLosDto.getIId());
		
		return defaultLosDto;
	}
	
	protected interface IMaterialBuchungsCall {
		void bucheMaterial(LosDto losDto, LossollmaterialDto lossollmaterialDto, 
				LosistmaterialDto losistmaterialDto) throws RemoteException;
	}
	
	protected class GebeMaterialNachtraglichAus implements IMaterialBuchungsCall {
		public void bucheMaterial(LosDto losDto, LossollmaterialDto lossollmaterialDto,	
				LosistmaterialDto losistmaterialDto) throws RemoteException {
			beanServices.gebeMaterialNachtraeglichAus(lossollmaterialDto, losistmaterialDto);
		}
		
	}
	
	protected class BucheMaterialAufLos implements IMaterialBuchungsCall {
		public void bucheMaterial(LosDto losDto, LossollmaterialDto lossollmaterialDto,	
				LosistmaterialDto losistmaterialDto) throws RemoteException {
			beanServices.bucheMaterialAufLos(lossollmaterialDto, losDto);
		}
		
	}
}
