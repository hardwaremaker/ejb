package com.lp.server.fertigung.bl;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.fertigung.service.CsvVerbrauchsartikel;
import com.lp.server.fertigung.service.IVerbrauchsartikelImporterBeanServices;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.fertigung.service.VerbrauchsartikelImportResult;
import com.lp.server.fertigung.service.VerbrauchsartikelImportStats;
import com.lp.server.fertigung.service.errors.ImportException;
import com.lp.server.fertigung.service.errors.NoFertigungsgruppeException;
import com.lp.server.fertigung.service.errors.NoKostenstelleException;
import com.lp.server.fertigung.service.errors.NoLoslagerException;
import com.lp.server.fertigung.service.errors.NoMontageartException;
import com.lp.server.fertigung.service.errors.NoOrUnknownDefaultArticleException;
import com.lp.server.fertigung.service.errors.NoSetArticlePositionsException;
import com.lp.server.fertigung.service.errors.NoSuchArticleException;
import com.lp.server.fertigung.service.errors.NotEnoughInStockException;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.stueckliste.service.MontageartDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.util.Helper;

public class VerbrauchsartikelImporter {

	private CsvVerbrauchsartikelTransformer transformer;
	private List<VerbrauchsartikelExtended> importArtikel;
	private List<ImportException> importErrors;
	private IVerbrauchsartikelImporterBeanServices beanServices;
	private VerbrauchsartikelImportStats stats;
	
	private FertigungsgruppeDto fertigungsgruppe;
	private KostenstelleDto kostenstelle;
	private MontageartDto montageart;
	private LagerDto loslager;
	
	private Map<String, ImportArtikelInfo> artikelInfoMap;
	private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	
	public class ImportArtikelInfo {
		private ArtikelDto artikelDto;
		private BigDecimal menge;
		
		public ImportArtikelInfo(ArtikelDto artikelDto) {
			this.artikelDto = artikelDto;
			menge = BigDecimal.ZERO;
		}
		
		public void addMenge(BigDecimal menge) {
			this.menge = this.menge.add(menge);
		}
		
		public ArtikelDto getArtikelDto() {
			return artikelDto;
		}
		
		public BigDecimal getMenge() {
			return menge;
		}
	}
	
	public VerbrauchsartikelImporter(IVerbrauchsartikelImporterBeanServices beanServices, 
			CsvVerbrauchsartikelTransformer transformer) {
		this.beanServices = beanServices;
		this.transformer = transformer;
		stats = new VerbrauchsartikelImportStats();
		artikelInfoMap = new HashMap<String, ImportArtikelInfo>();
		importArtikel = new ArrayList<VerbrauchsartikelExtended>();
	}
	
	public VerbrauchsartikelImportResult importCsv(List<String[]> csvVerbrauchsartikel) throws RemoteException {
		VerbrauchsartikelImportResult result = checkImportCsv(csvVerbrauchsartikel);
		if (result.getImportErrors().size() > 0) return result;
		
		importVerbrauchsartikel();
		return result;
	}
	
	private void importVerbrauchsartikel() throws RemoteException {
		if (importArtikel.size() < 1) return; 
		
		LosDto losDto = createLos();
		for (VerbrauchsartikelExtended verbrauchsartikel : importArtikel) {
			LossollmaterialDto lossollmaterialDto = getLossollmaterialDto(
					artikelInfoMap.get(verbrauchsartikel.getArtikelnummer()).getArtikelDto(), losDto, verbrauchsartikel);
			beanServices.createLossollmaterial(lossollmaterialDto);
//			LosistmaterialDto losistmaterialDto = getLosistmaterialDto(losDto, verbrauchsartikel);
//			beanServices.gebeMaterialNachtraeglichAus(lossollmaterialDto, losistmaterialDto);
		}
		beanServices.gebeLosAus(losDto.getIId());
		beanServices.losErledigen(losDto.getIId());
	}

	private LosDto createLos() throws RemoteException {
		LosDto losDto = beanServices.getDefaultLos();
		Timestamp abschlussdatum = importArtikel.get(0).getAbschlussdatum();
		losDto.setCProjekt("Tagesabschluss vom " + dateTimeFormat.format(abschlussdatum));
		losDto.setKostenstelleIId(getKostenstelle().getIId());
		losDto.setFertigungsgruppeIId(getFertigungsgruppe().getIId());
		losDto.setLagerIIdZiel(getLoslager().getIId());
		losDto.setTProduktionsbeginn(Helper.extractDate(abschlussdatum));
		losDto.setTProduktionsende(Helper.extractDate(abschlussdatum));
		
		return beanServices.createLosDto(losDto);
	}

//	private LosistmaterialDto getLosistmaterialDto(LosDto losDto, VerbrauchsartikelExtended vae) {
//		LosistmaterialDto losistmaterialDto = new LosistmaterialDto();
//		losistmaterialDto.setLagerIId(losDto.getLagerIIdZiel());
//		losistmaterialDto.setBAbgang(Helper.boolean2Short(vae.getMenge().signum() == 1));
//		losistmaterialDto.setNMenge(vae.getMenge());
//		
//		return losistmaterialDto;
//	}
	
	private LossollmaterialDto getLossollmaterialDto(ArtikelDto artikelDto, LosDto losDto, VerbrauchsartikelExtended vae) throws RemoteException {
		LossollmaterialDto lossollmaterialDto = beanServices.getDefaultLossollmaterial(artikelDto, losDto.getIId(), 
				losDto.getLagerIIdZiel(), getMontageart().getIId());
		lossollmaterialDto.setBNachtraeglich(Helper.boolean2Short(false));
		if (vae.getEinzelpreis() != null && vae.getEinzelpreis().signum() < 0) {
			lossollmaterialDto.setNMenge(vae.getMenge() != null ? vae.getMenge().abs().negate() : null);
		} else {
			lossollmaterialDto.setNMenge(vae.getMenge());
		}
		lossollmaterialDto.setCKommentar(Helper.cutString(vae.getKommentar(), 80));
		
		return lossollmaterialDto;
	}

	public VerbrauchsartikelImportResult checkImportCsv(List<String[]> csvVerbrauchsartikel) {
		importErrors = new ArrayList<ImportException>();
		
		checkGrunddaten();
		
		int linenumber = 1;
		for (String[] line : csvVerbrauchsartikel) {
			try {
				linenumber++;
				if (isEmptyLine(line)) {
					continue;
				}
				
				CsvVerbrauchsartikel verbrauchsartikel = transformer.getVerbrauchsartikel(line);
				if (!shouldImportArtikel(verbrauchsartikel)) {
					continue;
				}
				
				stats.incrementPossibleImports();
				addArtikel(verbrauchsartikel);
			} catch (ImportException ex) {
				ex.setLinenumber(linenumber);
				importErrors.add(ex);
			}
		}
		
		checkLagerstaende();
		return new VerbrauchsartikelImportResult(importErrors, stats);
	}
	
	private boolean shouldImportArtikel(CsvVerbrauchsartikel verbrauchsartikel) {
		String artikelgruppe = verbrauchsartikel.getArtikelhauptgruppe();
		if (artikelgruppe != null && artikelgruppe.toLowerCase().startsWith("pfand")) {
			return false;
		}
		
		return true;
	}

	private void checkLagerstaende() {
		if (getLoslager() == null) return;
		
		for (Entry<String, ImportArtikelInfo> entry : artikelInfoMap.entrySet()) {
			ImportArtikelInfo artikelInfo = entry.getValue();
			if (!requiresLagerstand(artikelInfo.getArtikelDto()))
				continue;
			
			BigDecimal lagerstand = getLagerstand(artikelInfo.getArtikelDto().getIId(), getLoslager().getIId());
			if (lagerstand == null || lagerstand.compareTo(artikelInfo.getMenge()) < 0) {
				importErrors.add(new NotEnoughInStockException(entry.getKey(), 
						artikelInfo.getMenge(), lagerstand, getLoslager().getCNr()));
				stats.incrementErrorCounts();
			}
		}
	}
	
	private boolean requiresLagerstand(ArtikelDto artikelDto) {
		return Helper.short2boolean(artikelDto.getBLagerbewirtschaftet());
	}
	
	private void addArtikel(CsvVerbrauchsartikel verbrauchsartikel) throws ImportException {
		ArtikelDto artikelDto = checkAndFindArtikel(verbrauchsartikel);
		
		StuecklisteDto stuecklisteDto = findSetArtikel(artikelDto.getIId());
		if (stuecklisteDto == null) {
			VerbrauchsartikelExtended artikelExtended = new VerbrauchsartikelExtended(verbrauchsartikel);
			artikelExtended.setKommentar(getArtikelkommentar(artikelExtended));
			addImportArtikel(artikelExtended, artikelDto);
			return;
		}
		
		addStklPositions(verbrauchsartikel, stuecklisteDto);
	}
	
	private String getArtikelkommentar(VerbrauchsartikelExtended artikelExtended) {
		StringBuilder builder = new StringBuilder();
		builder.append(dateTimeFormat.format(artikelExtended.getRechnungsdatum()))
			.append(", G-Preis=").append(Helper.formatBetrag(artikelExtended.getGesamtpreis(), 
					beanServices.getTheClientDto().getLocUi()));
		if (Helper.isStringEmpty(artikelExtended.getArtikelnummer())) {
			builder.append(", ").append(artikelExtended.getPlu())
				.append(", ").append(artikelExtended.getArtikelbezeichnung());
		}
		
		builder.append(", ").append(artikelExtended.getZahlungsart());
		return builder.toString();
	}
	
	private void addStklPositions(CsvVerbrauchsartikel verbrauchsartikel,
			StuecklisteDto stuecklisteDto) throws ImportException {
		StuecklistepositionDto[] positions = findStuecklistepositionen(stuecklisteDto.getIId());
		
		if (positions == null) {
			throw new NoSetArticlePositionsException(stuecklisteDto.getArtikelDto().getCNr());
		}
		
		for (StuecklistepositionDto position : positions) {
			VerbrauchsartikelExtended artikelExtended = mapPosition(verbrauchsartikel, position);
			artikelExtended.setKommentar(getSetartikelkommentar(artikelExtended, stuecklisteDto.getArtikelDto().getCNr()));
			addImportArtikel(artikelExtended, position.getArtikelDto());
		}
	}
	
	private String getSetartikelkommentar(VerbrauchsartikelExtended artikelExtended, String parentArtikelCnr) {
		StringBuilder builder = new StringBuilder();
		builder.append(dateTimeFormat.format(artikelExtended.getRechnungsdatum()))
			.append(", aus '")
			.append(parentArtikelCnr)
			.append("'")
			.append(" (G-Preis=").append(Helper.formatBetrag(artikelExtended.getGesamtpreis(), 
					beanServices.getTheClientDto().getLocUi())).append(")")
			.append(", ").append(artikelExtended.getZahlungsart());
		return builder.toString();
	}

	private void addImportArtikel(VerbrauchsartikelExtended verbrauchsartikel, ArtikelDto artikelDto) {
		importArtikel.add(verbrauchsartikel);
		ImportArtikelInfo artikelInfo = artikelInfoMap.get(verbrauchsartikel.getArtikelnummer());
		
		if (artikelInfo == null) {
			artikelInfo = new ImportArtikelInfo(artikelDto);
		}
		artikelInfo.addMenge(verbrauchsartikel.getMenge());
		artikelInfoMap.put(verbrauchsartikel.getArtikelnummer(), artikelInfo);
	}
	
	private VerbrauchsartikelExtended mapPosition(CsvVerbrauchsartikel verbrauchsartikel,
			StuecklistepositionDto position) {
		VerbrauchsartikelExtended mapped = new VerbrauchsartikelExtended(verbrauchsartikel);
		mapped.setArtikelnummer(position.getArtikelDto().getCNr());
		mapped.setMenge(verbrauchsartikel.getMenge().multiply(position.getNMenge()));
		
		return mapped;
	}

	private ArtikelDto checkAndFindArtikel(CsvVerbrauchsartikel verbrauchsartikel) throws ImportException {
		if (Helper.isStringEmpty(verbrauchsartikel.getArtikelnummer())) {
			String defaultArtikelnummer = getDefaultArtikelLeereArtikelnummer();
			if (Helper.isStringEmpty(defaultArtikelnummer)) {
				stats.incrementErrorCounts();
				throw new NoOrUnknownDefaultArticleException(
						ParameterFac.PARAMETER_KASSENIMPORT_LEERE_ARTIKELNUMMER_DEFAULT_ARTIKEL);
			}
			
			ArtikelDto artikelDto = findArtikelByCnr(defaultArtikelnummer);
			if (artikelDto == null) {
				stats.incrementErrorCounts();
				throw new NoOrUnknownDefaultArticleException(defaultArtikelnummer, 
						ParameterFac.PARAMETER_KASSENIMPORT_LEERE_ARTIKELNUMMER_DEFAULT_ARTIKEL);
			}
			return artikelDto;
		}
		
		ArtikelDto artikelDto = findArtikelByCnr(verbrauchsartikel.getArtikelnummer());
		if (artikelDto == null) {
			stats.incrementErrorCounts();
			throw new NoSuchArticleException(verbrauchsartikel.getArtikelnummer());
		}
		
		return artikelDto;
	}
	
	private void checkGrunddaten() {
		if (!existsFertigungsgruppe()) {
			importErrors.add(new NoFertigungsgruppeException());
			stats.incrementErrorCounts();
		}
		
		if (!existsMontageart()) {
			importErrors.add(new NoMontageartException());
			stats.incrementErrorCounts();
		}
		
		if (!existsKostenstelle()) {
			importErrors.add(new NoKostenstelleException());
			stats.incrementErrorCounts();
		}
		
		if (!existsLoslager()) {
			importErrors.add(new NoLoslagerException());
			stats.incrementErrorCounts();
		}
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
	
	private boolean existsLoslager() {
		return getLoslager() != null ? true : false;
	}

	private StuecklistepositionDto[] findStuecklistepositionen(Integer stuecklisteIId) {
		try {
			return beanServices.stuecklistepositionFindByStuecklisteIId(stuecklisteIId);
		} catch (Throwable t) {
		}
		return null;
	}

	private StuecklisteDto findSetArtikel(Integer artikelIId) {
		try {
			return beanServices.getStueckliste(artikelIId);
		} catch (Throwable t) {
		}
		return null;
	}

	private ArtikelDto findArtikelByCnr(String artikelnummer) {
		try {
			ArtikelDto artikelDto = beanServices.artikelFindByCnr(artikelnummer);
			return artikelDto;
		} catch (Throwable t) {
		}
		return null;
	}

	protected boolean isEmptyLine(String[] line) {
		for (String cell : line) {
			if (cell.trim().length() > 0) return false;
		}
		return true;
	}
	
	private FertigungsgruppeDto getFertigungsgruppe() {
		if (fertigungsgruppe == null) {
			try {
				fertigungsgruppe = beanServices.findFertigungsgruppe();
			} catch (RemoteException e) {
			}
		}
		return fertigungsgruppe;
	}
	
	private KostenstelleDto getKostenstelle() {
		if (kostenstelle == null) {
			try {
				kostenstelle = beanServices.findKostenstelle();
			} catch (RemoteException e) {
			}
		}
		return kostenstelle;
	}
	
	private MontageartDto getMontageart() {
		if (montageart == null) {
			try {
				montageart = beanServices.findMontageart();
			} catch (RemoteException e) {
			}
		}
		return montageart;
	}
	
	private LagerDto getLoslager() {
		if (loslager == null) {
			try {
				loslager = beanServices.findLoslager();
			} catch (RemoteException e) {
			}
		}
		return loslager;
	}
	
	private BigDecimal getLagerstand(Integer artikelIId, Integer lagerIId) {
		try {
			return beanServices.getLagerstand(artikelIId, lagerIId);			
		} catch (RemoteException e) {
		}
		return null;
	}
	
	private String getDefaultArtikelLeereArtikelnummer() {
		return beanServices.getDefaultArtikelnummerLeereArtikelnummer();
	}
}
