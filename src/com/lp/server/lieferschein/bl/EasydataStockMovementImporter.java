package com.lp.server.lieferschein.bl;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.EinkaufseanDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.lieferschein.service.EasydataImportResult;
import com.lp.server.lieferschein.service.EasydataImportStats;
import com.lp.server.lieferschein.service.IEasydataBeanServices;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.lieferschein.service.errors.StmAnsprechpartnerNichtEingetragenExc;
import com.lp.server.lieferschein.service.errors.StmArtikelMwstSatzExc;
import com.lp.server.lieferschein.service.errors.StmException;
import com.lp.server.lieferschein.service.errors.StmMandantKeinKundeExc;
import com.lp.server.lieferschein.service.errors.StmPflichtfeldLeerExc;
import com.lp.server.lieferschein.service.errors.StmTransformXmlExc;
import com.lp.server.lieferschein.service.errors.StmUnbekannteEanExc;
import com.lp.server.lieferschein.service.errors.StmUnbekanntePersonalnummerExc;
import com.lp.server.lieferschein.service.errors.StmUnbekanntesLagerExc;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.ejbfac.HvCreatingCachingProvider;
import com.lp.server.system.service.MandantDto;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

public class EasydataStockMovementImporter {
	protected final ILPLogger log = LPLogService.getInstance().getLogger(this.getClass());

	private EasydataStockMovementTransformer transformer;
	private IEasydataBeanServices beanServices;
	private ArtikelCache artikelCache;
	private LagerCache lagerCache;
	private PersonalCache personalCache;
	private Map<Integer, AnsprechpartnerDto> mandantAnsprechpartner;
	private MandantDto mandantDto;
	private List<StmException> errors;
	private List<EasydataStockMovement> stockMovements;
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss");
	private EasydataImportStats stats;
	
	public EasydataStockMovementImporter(IEasydataBeanServices beanServices) {
		this.beanServices = beanServices;
		transformer = new EasydataStockMovementTransformer();
		stats = new EasydataImportStats();
	}

	private void resetCache() throws EJBExceptionLP, RemoteException {
		artikelCache = new ArtikelCache();
		lagerCache = new LagerCache(beanServices.lagerFindAll());
		personalCache = new PersonalCache();
		mandantAnsprechpartner = new HashMap<Integer, AnsprechpartnerDto>();
	}
	
	private List<StmException> getErrors() {
		if (errors == null) {
			errors = new ArrayList<StmException>();
		}
		return errors;
	}
	
	private void addError(StmException exc) {
		getErrors().add(exc);
		stats.incrementErrorCounts();
	}
	
	public EasydataImportResult checkXMLDaten(String xmlDaten) throws EJBExceptionLP, RemoteException {
		try {
			stockMovements = transformer.transform(xmlDaten);
			stats.setTotalImports(stockMovements.size());
		} catch (Throwable e) {
			addError(new StmTransformXmlExc(e));
			return new EasydataImportResult(getErrors(), stats);
		}
		
		collectData(stockMovements);
		checkData(stockMovements);
		
		return new EasydataImportResult(getErrors(), stats);
	}
	
	public EasydataImportResult importXMLDaten(String xmlDaten) throws EJBExceptionLP, RemoteException {
		EasydataImportResult result = checkXMLDaten(xmlDaten);
		if (result.hasErrors()) return result;
		
		result.setLieferscheine(importLieferscheine());
		
		return result;
	}

	private List<LieferscheinDto> importLieferscheine() throws RemoteException {
		List<LieferscheinDto> lieferscheine = new ArrayList<LieferscheinDto>();
		KundeDto kundeDtoMandant = getKunde(getMandant().getPartnerIId());
		
		for (EasydataStockMovement stm : stockMovements) {
			LieferscheinDto lieferscheinDto = createLieferschein(stm, kundeDtoMandant);
			
			for (EasydataProduct product : stm.getProducts()) {
				createLieferscheinposition(product, lieferscheinDto);
			}
			lieferscheine.add(lieferscheinDto);
		}
		return lieferscheine;
	}
	
	private LieferscheinpositionDto createLieferscheinposition(EasydataProduct product, LieferscheinDto lieferscheinDto) throws EJBExceptionLP, RemoteException {
		EanArtikel eanArtikel = artikelCache.getValueOfKey(product.getBarcode());
		LieferscheinpositionDto lsPosDto = beanServices.setupDefaultLieferscheinposition(
				lieferscheinDto, eanArtikel.getArtikelDto(), product.getQuantity());
		lsPosDto.setXTextinhalt(dateFormatter.format(product.getTime()));
		lsPosDto.setNMenge(product.getQuantity().multiply(eanArtikel.getEinkaufseanDto().getNMenge()));
		beanServices.createLieferscheinposition(lsPosDto);
		
		return lsPosDto;
	}

	private LieferscheinDto createLieferschein(EasydataStockMovement stm, KundeDto kundeDtoMandant)
			throws RemoteException {
		LieferscheinDto lieferscheinDto = beanServices.setupDefaultLieferschein(kundeDtoMandant);
		PersonalAnsprechpartner anspr = personalCache.getValueOfKey(stm.getPersonalNumber());
		lieferscheinDto.setAnsprechpartnerIId(anspr.getAnsprechpartnerDto().getIId());
		lieferscheinDto.setLagerIId(lagerCache.getValueOfKey(stm.getStockAsInteger()).getIId());
		lieferscheinDto.setZiellagerIId(lagerCache.getValueOfKey(stm.getTargetStockAsInteger()).getIId());
		lieferscheinDto.setKostenstelleIId(kundeDtoMandant.getKostenstelleIId());
		lieferscheinDto.setTBelegdatum(stm.getTourTime());
		lieferscheinDto.setCBezProjektbezeichnung(lagerCache.getValueOfKey(stm.getTargetStockAsInteger()).getCNr());
		lieferscheinDto = beanServices.createLieferschein(lieferscheinDto);
		
		return lieferscheinDto;
	}

	private void checkData(List<EasydataStockMovement> lieferscheine) {
		checkMandatoryFields(lieferscheine);
		checkDataLager();
		checkDataArtikel();
		checkDataPersonalAnsprechpartner();
		checkDataKunde();
	}

	private void checkMandatoryFields(List<EasydataStockMovement> lieferscheine) {
		for (EasydataStockMovement stm : lieferscheine) {
			if (stm.getPersonalNumber() == null)
				addError(new StmPflichtfeldLeerExc("Tour Personalnumber", stm.getAutoIndex(), stm.getSerialNumber()));
			if (Helper.isStringEmpty(stm.getStock()))
				addError(new StmPflichtfeldLeerExc("Activity Reference (stock)", stm.getAutoIndex(), stm.getSerialNumber()));
			if (Helper.isStringEmpty(stm.getTargetStock()))
				addError(new StmPflichtfeldLeerExc("Activity Target (target stock)", stm.getAutoIndex(), stm.getSerialNumber()));
			if (stm.getTourTime() == null)
				addError(new StmPflichtfeldLeerExc("Tour Tourdate", stm.getAutoIndex(), stm.getSerialNumber()));
			
			for (EasydataProduct product : stm.getProducts()) {
				if (Helper.isStringEmpty(product.getBarcode()))
					addError(new StmPflichtfeldLeerExc("Product Barcode", stm.getAutoIndex(), stm.getSerialNumber()));
				if (product.getQuantity() == null)
					addError(new StmPflichtfeldLeerExc("Product Quantity", stm.getAutoIndex(), stm.getSerialNumber()));
			}
		}
	}

	private void checkDataKunde() {
		KundeDto kundeDto = getKunde(getMandant().getPartnerIId());
		if (kundeDto == null) {
			addError(new StmMandantKeinKundeExc(getMandant()));
		}
	}

	private void checkDataPersonalAnsprechpartner() {
		for (Entry<Integer, PersonalAnsprechpartner> entry : personalCache.entrySet()) {
			if (entry.getValue() == null) {
				addError(new StmUnbekanntePersonalnummerExc(String.valueOf(entry.getKey())));
			} else if (entry.getValue().getAnsprechpartnerDto() == null) {
				PersonalDto personalDto = entry.getValue().getPersonalDto();
				if (personalDto.getPartnerDto() == null) {
					personalDto.setPartnerDto(getPartnerDto(personalDto.getPartnerIId()));
				}
				addError(new StmAnsprechpartnerNichtEingetragenExc(getMandant(), personalDto));
			}
		}
	}

	private void checkDataArtikel() {
		Boolean paramPositionskontierung = beanServices.getParameterPositionskontierung();
		
		for (Entry<String, EanArtikel> entry : artikelCache.entrySet()) {
			if (entry.getValue() == null) {
				addError(new StmUnbekannteEanExc(entry.getKey()));
			} else if (Boolean.TRUE.equals(paramPositionskontierung) 
					&& entry.getValue().getArtikelDto().getMwstsatzbezIId() == null) {
				addError(new StmArtikelMwstSatzExc(entry.getValue().getArtikelDto()));
			}
		}
	}

	private void checkDataLager() {
		for (Entry<Integer, LagerDto> entry : lagerCache.entrySet()) {
			if (entry.getValue() == null) {
				addError(new StmUnbekanntesLagerExc(entry.getKey()));
			}
		}
	}
	
	private KundeDto getKunde(Integer partnerIId) {
		try {
			return beanServices.kundeFindByPartnerIId(partnerIId);
		} catch (RemoteException e) {
			log.error("RemoteException", e);
		}
		return null;
	}

	private MandantDto getMandant() {
		if (mandantDto == null) {
			try {
				return beanServices.mandant();
			} catch (EJBExceptionLP e) {
				log.error("EJBExceptionLP finding MandantDto", e);
			} catch (RemoteException e) {
				log.error("RemoteException finding MandantDto", e);
			}
		}
		return mandantDto;
	}

	private PartnerDto getPartnerDto(Integer partnerIId) {
		return beanServices.partnerFindByPrimaryKey(partnerIId);
	}
	
	private void collectData(List<EasydataStockMovement> lieferscheine) throws EJBExceptionLP, RemoteException {
		resetCache();

		loadMandantAnsprechpartner();
		for (EasydataStockMovement stm : lieferscheine) {
			if (stm.getStockAsInteger() != null) lagerCache.getValueOfKey(stm.getStockAsInteger());
			if (stm.getTargetStockAsInteger() != null) lagerCache.getValueOfKey(stm.getTargetStockAsInteger());
			if (stm.getPersonalNumber() != null) personalCache.getValueOfKey(stm.getPersonalNumber());
			for (EasydataProduct product : stm.getProducts()) {
				if (product.getBarcode() != null) artikelCache.getValueOfKey(product.getBarcode());
			}
		}
	}
	
	private void loadMandantAnsprechpartner() throws EJBExceptionLP, RemoteException {
		mandantAnsprechpartner = new HashMap<Integer, AnsprechpartnerDto>();
		MandantDto mandantDto = beanServices.mandant();
		List<AnsprechpartnerDto> ansprechpartner = Arrays.asList(
				beanServices.ansprechpartnerFindByPartnerIId(mandantDto.getPartnerIId()));
		Collections.sort(ansprechpartner, new Comparator<AnsprechpartnerDto>() {
			public int compare(AnsprechpartnerDto dto1, AnsprechpartnerDto dto2) {
				return dto2.getISort().compareTo(dto1.getISort());
			}
		});
		for (AnsprechpartnerDto dto : ansprechpartner) {
			mandantAnsprechpartner.put(dto.getPartnerIIdAnsprechpartner(), dto);
		}
	}

	private class ArtikelCache extends HvCreatingCachingProvider<String, EanArtikel> {
		@Override
		protected EanArtikel provideValue(String ean, String transformedKey) {
			try {
				EinkaufseanDto einkaufseanDto = beanServices.einkaufseanFindByEan(ean);
				if (einkaufseanDto == null) return null;
				
				ArtikelDto artikelDto = beanServices.artikelFindByPrimaryKey(einkaufseanDto.getArtikelIId());
				if (artikelDto == null) return null;
				
				EanArtikel eanArtikel = new EanArtikel();
				eanArtikel.setArtikelDto(artikelDto);
				eanArtikel.setEinkaufseanDto(einkaufseanDto);
				return eanArtikel;
			} catch (RemoteException e) {
				log.error("Could not find Einkaufsean by cEan = '" + ean + "'", e);
			}
			return null;
		}
	}
	
	public class LagerCache extends HvCreatingCachingProvider<Integer, LagerDto> {
		private Map<Integer, LagerDto> hvLaeger = new HashMap<Integer, LagerDto>();
		
		public LagerCache(LagerDto[] lagerDtos) {
			super();
			fillCache(Arrays.asList(lagerDtos));
		}
		
		public LagerCache(List<LagerDto> lagerDtos) {
			super();
			fillCache(lagerDtos);
		}
		
		private void fillCache(List<LagerDto> laeger) {
			for (LagerDto lager : laeger) {
				Integer tourlagerNr = getTourlagerNr(lager);
				if (tourlagerNr != null) {
					hvLaeger.put(tourlagerNr, lager);
				}
			}
		}
		
		private Integer getTourlagerNr(LagerDto lagerDto) {
			String[] splitName = lagerDto.getCNr().split("(?=\\d*$)",2);
			try {
				if (splitName != null && splitName.length == 2) {
					if (!splitName[1].isEmpty()) {
						return Integer.parseInt(splitName[1]);
					} else {
						return Integer.parseInt(splitName[0]);
					}
				}
			} catch (NumberFormatException ex) {
			}
			
			return null;
		}

		@Override
		protected LagerDto provideValue(Integer tourNr, Integer transformedKey) {
			return hvLaeger.get(tourNr);
		}
	}
	
	private class PersonalCache extends HvCreatingCachingProvider<Integer, PersonalAnsprechpartner> {
		@Override
		protected PersonalAnsprechpartner provideValue(Integer personalnummer, Integer transformedKey) {
			PersonalAnsprechpartner persAnspr = new PersonalAnsprechpartner();
			try {
				PersonalDto personalDto = beanServices.personalFindByCPersonalnr(String.valueOf(personalnummer));
				if (personalDto == null) return null;
				
				persAnspr.setPersonalDto(personalDto);
				AnsprechpartnerDto ansprechpartnerDto = mandantAnsprechpartner.get(personalDto.getPartnerIId());
				persAnspr.setAnsprechpartnerDto(ansprechpartnerDto);
				return persAnspr;
			} catch (RemoteException e) {
				log.error("Could not find Personal by cPersonalnr = '" + String.valueOf(personalnummer) + "'", e);
				return null;
			}
		}
	}
	
	private class EanArtikel {
		private ArtikelDto artikelDto;
		private EinkaufseanDto einkaufseanDto;
		
		public ArtikelDto getArtikelDto() {
			return artikelDto;
		}
		public void setArtikelDto(ArtikelDto artikelDto) {
			this.artikelDto = artikelDto;
		}
		public EinkaufseanDto getEinkaufseanDto() {
			return einkaufseanDto;
		}
		public void setEinkaufseanDto(EinkaufseanDto einkaufseanDto) {
			this.einkaufseanDto = einkaufseanDto;
		}
	}
	
	private class PersonalAnsprechpartner {
		private PersonalDto personalDto;
		private AnsprechpartnerDto ansprechpartnerDto;
		
		public PersonalDto getPersonalDto() {
			return personalDto;
		}
		public void setPersonalDto(PersonalDto personalDto) {
			this.personalDto = personalDto;
		}
		public AnsprechpartnerDto getAnsprechpartnerDto() {
			return ansprechpartnerDto;
		}
		public void setAnsprechpartnerDto(AnsprechpartnerDto ansprechpartnerDto) {
			this.ansprechpartnerDto = ansprechpartnerDto;
		}
	}
}
