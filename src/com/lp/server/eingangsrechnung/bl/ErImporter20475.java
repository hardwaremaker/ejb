package com.lp.server.eingangsrechnung.bl;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.eingangsrechnung.service.ErImportError20475;
import com.lp.server.eingangsrechnung.service.ErImportError20475.Severity;
import com.lp.server.eingangsrechnung.service.ErImportItem20475;
import com.lp.server.eingangsrechnung.service.ErImportItemList20475;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.ReversechargeartDto;
import com.lp.server.finanz.service.UstUebersetzungDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.rechnung.service.CoinRoundingResult;
import com.lp.server.system.ejbfac.HvCreatingCachingProvider;
import com.lp.server.system.pkgenerator.format.LpDefaultBelegnummerFormat;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.WaehrungDto;
import com.lp.server.system.service.WechselkursDto;
import com.lp.server.util.MwstsatzbezId;
import com.lp.util.Helper;

public class ErImporter20475 {
	
	private ErImporter20475BeanServices beanServices;
	private SteuercodeCache steuercodeCache;
	private WaehrungCache waehrungCache;
	private KontoCache kreditorkontoCache;
	private KontoCache sachkontoCache;
	private GeschaeftsjahrCache gjCache;
	private ReversechargeartDto reversechargeartOhneDto;
	private ErImportErrorFactory errorFactory;
	private LpDefaultBelegnummerFormat belegnummerFormat;
	
	public ErImporter20475(ErImporter20475BeanServices beanServices, LpDefaultBelegnummerFormat belegnummerFormat) throws RemoteException {
		this.beanServices = beanServices;
		this.belegnummerFormat = belegnummerFormat;
		errorFactory = new ErImportErrorFactory();
		resetCache();
	}

	private void resetCache() throws RemoteException {
		steuercodeCache = new SteuercodeCache(beanServices.getAlleEingangsrechnungSteuercodes());
		waehrungCache = new WaehrungCache(beanServices.findWaehrungen());
		kreditorkontoCache = new KontoCache(FinanzServiceFac.KONTOTYP_KREDITOR);
		sachkontoCache = new KontoCache(FinanzServiceFac.KONTOTYP_SACHKONTO);
		gjCache = new GeschaeftsjahrCache();
		reversechargeartOhneDto = beanServices.findReversechargeartOhne();
	}
	
	public ErImportItem20475 doImport(ErImportItem20475 item) throws RemoteException {
		item.getErrors().clear();
		if (!checkBelegnummer(item)) {
			return item;
		}
		
		checkErData(item);
		if (item.hasErrors(Severity.ERROR)) {
			return item;
		}
		
		EingangsrechnungDto eingangsrechnungDto = createEingangsrechnung(item);
		if (!eingangsrechnungDto.getCNr().equals(item.getCnr())) {
			item.addError(errorFactory.erstellteEingangsrechnungHatAndereCnr(item, eingangsrechnungDto));
		}
		
		item.setImported(Boolean.TRUE);
		return item;
	}

	private EingangsrechnungDto createEingangsrechnung(ErImportItem20475 item) throws RemoteException {
		LieferantDto lieferantDto = findLieferant(item);
		if (lieferantDto == null) return null;

		EingangsrechnungDto erDto = new EingangsrechnungDto();
		erDto.setEingangsrechnungartCNr(EingangsrechnungFac.EINGANGSRECHNUNGART_EINGANGSRECHNUNG);
		erDto.setMandantCNr(beanServices.getMandant());
		erDto.setLieferantIId(lieferantDto.getIId());
		erDto.setDBelegdatum(item.getDate());
		erDto.setDFreigabedatum(item.getExpirationDate());
		erDto.setWaehrungCNr(item.getCurrency());
		erDto.setZahlungszielIId(lieferantDto.getZahlungszielIId());
		erDto.setBMitpositionen(Helper.getShortFalse());
		erDto.setNBetragfw(item.getAmount());
		setLieferantenrechnungsnummerText(erDto, item);
		
		if (shouldCreateSplittbuchung(item)) {
			if (!item.getCurrency().equals(beanServices.getTheClientDto().getSMandantenwaehrung())) {
				WechselkursDto kursDto = beanServices.getKursZuDatum(item.getCurrency(), 
						beanServices.getTheClientDto().getSMandantenwaehrung(), item.getDate());
				BigDecimal bdKurs = kursDto.getNKurs().setScale(
						LocaleFac.ANZAHL_NACHKOMMASTELLEN_WECHSELKURS, BigDecimal.ROUND_HALF_EVEN);
				erDto.setNKurs(bdKurs);
				erDto.setNBetrag(Helper.rundeKaufmaennisch(
						item.getAmount().multiply(bdKurs), FinanzFac.NACHKOMMASTELLEN));
			} else {
				erDto.setNBetrag(item.getAmount());
				erDto.setNKurs(BigDecimal.ONE);
			}
			erDto.setNUstBetrag(BigDecimal.ZERO);
			erDto.setNUstBetragfw(BigDecimal.ZERO);
			erDto.setBIgErwerb(Helper.getShortFalse());
			erDto.setReversechargeartId(reversechargeartOhneDto.getIId());
		} else {
			if (!item.getCurrency().equals(beanServices.getTheClientDto().getSMandantenwaehrung())) {
				WechselkursDto kursDto = beanServices.getKursZuDatum(item.getCurrency(), 
						beanServices.getTheClientDto().getSMandantenwaehrung(), item.getDate());
				BigDecimal bdKurs = kursDto.getNKurs().setScale(
						LocaleFac.ANZAHL_NACHKOMMASTELLEN_WECHSELKURS, BigDecimal.ROUND_HALF_EVEN);
				erDto.setNKurs(bdKurs);
				erDto.setNBetrag(Helper.rundeKaufmaennisch(
						item.getAmount().multiply(bdKurs), FinanzFac.NACHKOMMASTELLEN));
				erDto.setNUstBetrag(Helper.rundeKaufmaennisch(
						item.getTaxAmount().multiply(bdKurs), FinanzFac.NACHKOMMASTELLEN));
			} else {
				erDto.setNKurs(BigDecimal.ONE);
				erDto.setNBetrag(item.getAmount());
				erDto.setNUstBetrag(item.getTaxAmount());
			}
			erDto.setNUstBetragfw(item.getTaxAmount());
			UstUebersetzungDto ustUebersetzung = findSteuercode(item);
			if (ustUebersetzung == null) return null;
			
			erDto.setBIgErwerb(Helper.boolean2Short(ustUebersetzung.getBIgErwerb()));
			erDto.setReversechargeartId(ustUebersetzung.getReversechargeartIId());
			MwstsatzDto mwstsatzDto = findMwstSatzDto(new MwstsatzbezId(
					ustUebersetzung.getMwstSatzBezIId()), item.getDate());
			if (mwstsatzDto == null) return null;
			
			erDto.setMwstsatzIId(mwstsatzDto.getIId());
			KontoDto kontoDto = findKonto(item);
			if (kontoDto == null) return null;
			
			erDto.setKontoIId(kontoDto.getIId());
			erDto.setKostenstelleIId(lieferantDto.getIIdKostenstelle());
		}
		
		erDto = beanServices.createEingangsrechnung(erDto);
		return erDto;
	}

	private void setLieferantenrechnungsnummerText(EingangsrechnungDto erDto, ErImportItem20475 item) {
		Integer cnrSupplierLength = item.getCnrSupplier().length();
		Integer maxCnrSupplierLength = beanServices.getMaximaleLaengeLieferantenrechnungsnummer();
		if (cnrSupplierLength > maxCnrSupplierLength) {
			erDto.setCLieferantenrechnungsnummer(item.getCnrSupplier().substring(cnrSupplierLength - maxCnrSupplierLength));
			String text = Helper.isStringEmpty(item.getOrderNumber()) ? "" : item.getOrderNumber() + ",";
			text += item.getCnrSupplier();
			erDto.setCText(Helper.cutString(text, EingangsrechnungFac.FieldLength.TEXT));
		} else {
			erDto.setCLieferantenrechnungsnummer(item.getCnrSupplier());
			erDto.setCText(item.getOrderNumber());
		}
	}

	private boolean shouldCreateSplittbuchung(ErImportItem20475 item) {
		return isSplittbuchung(item) || Helper.isStringEmpty(item.getTaxCode())
				|| item.hasErrors(Severity.WARNING);
	}
	
	public ErImportItemList20475 doCheck(List<ErImportItem20475> items) throws RemoteException {
		resetCache();
		
		if (items.isEmpty())
			return new ErImportItemList20475(items);
		
		if (!checkFeldinhalt(items)) {
			return new ErImportItemList20475(items);
		}
		
		checkBelegnummern(items);
		
		for (ErImportItem20475 item : items) {
			checkErData(item);
		}
		
		return new ErImportItemList20475(items);
	}
	
	private boolean checkFeldinhalt(List<ErImportItem20475> items) {
		boolean mandatorySet = true;
		Integer stellenGJ = beanServices.getBelegnummerStellenGeschaeftsjahr();
		for (ErImportItem20475 item : items) {
			if (Helper.isStringEmpty(item.getCnr())) {
				item.addError(errorFactory.feldInhaltIstLeer(item, ErTransformer20475.Fieldnames.ER_NR));
				mandatorySet = false;
			} else if (item.getCnr().length() <= stellenGJ) {
				item.addError(errorFactory.feldInhaltZuKurz(item, ErTransformer20475.Fieldnames.ER_NR));
				mandatorySet = false;
			}
			
			if (Helper.isStringEmpty(item.getCreditor())) {
				item.addError(errorFactory.feldInhaltIstLeer(item, ErTransformer20475.Fieldnames.LIEFERANT));
				mandatorySet = false;
			}
			if (Helper.isStringEmpty(item.getCreditorName())) {
				item.addError(errorFactory.feldInhaltIstLeer(item, ErTransformer20475.Fieldnames.NAME));
				mandatorySet = false;
			}
			if (item.getDate() == null) {
				item.addError(errorFactory.feldInhaltIstLeer(item, ErTransformer20475.Fieldnames.RECHNUNGSDATUM));
				mandatorySet = false;
			}
			if (item.getExpirationDate() == null) {
				item.addError(errorFactory.feldInhaltIstLeer(item, ErTransformer20475.Fieldnames.EINGANGSDATUM));
				mandatorySet = false;
			}
			if (Helper.isStringEmpty(item.getCnrSupplier())) {
				item.addError(errorFactory.feldInhaltIstLeer(item, ErTransformer20475.Fieldnames.RECHNUNGSNR));
				mandatorySet = false;
			}
			if (item.getAmount() == null) {
				item.addError(errorFactory.feldInhaltIstLeer(item, ErTransformer20475.Fieldnames.RECHNUNGSBETRAG));
				mandatorySet = false;
			}
			if (Helper.isStringEmpty(item.getAccountNumber())) {
				item.addError(errorFactory.feldInhaltIstLeer(item, ErTransformer20475.Fieldnames.KONTO));
				mandatorySet = false;
			}
			if (Helper.isStringEmpty(item.getCurrency())) {
				item.addError(errorFactory.feldInhaltIstLeer(item, ErTransformer20475.Fieldnames.WAEHRUNG));
				mandatorySet = false;
			}
		}
		return mandatorySet;
	}
	
	private boolean checkBelegnummer(ErImportItem20475 item) {
		ErBelegnummer firstBelegnummer = new ErBelegnummer(item);
		if (beanServices.isGeschaeftsjahrGesperrt(firstBelegnummer.getGeschaeftsjahr())) {
			item.addError(errorFactory.geschaeftsjahrGesperrt(firstBelegnummer.getGeschaeftsjahr()));
			return false;
		}
		
		EingangsrechnungDto erDto = beanServices.getZuletztErstellteEingangsrechnung(firstBelegnummer.getGeschaeftsjahr());
		ErBelegnummer lastBelegnummer = erDto != null ? new ErBelegnummer(erDto) : null;
		if (!firstBelegnummer.isValidNextBelegnummerTo(lastBelegnummer)) {
			item.addError(erDto != null ? 
					errorFactory.importErNummernNichtFortlaufendZuLetzterEr(erDto, item) :
						errorFactory.importErNummernBeginnenNichtBeiEins(item));
			return false;
		}

		return true;
	}

	private boolean checkBelegnummern(List<ErImportItem20475> items) {
		Integer idxGJ = beanServices.getBelegnummerStellenGeschaeftsjahr();
		String gj = items.get(0).getCnr().substring(0, idxGJ);
		for (int idx = 1; idx < items.size(); idx++) {
			ErImportItem20475 item = items.get(idx);
			if (!item.getCnr().startsWith(gj)) {
				item.addError(errorFactory.erMehrererGeschaeftsjahre(item));
				return false;
			}
		}
		
		Integer lastNumber = items.get(0).getErNumber();
		for (int idx = 1; idx < items.size(); idx++) {
			ErImportItem20475 item = items.get(idx);
			if (!item.getErNumber().equals(lastNumber + 1)) {
				item.addError(errorFactory.erNummerNichtFortlaufend(item, lastNumber));
				return false;
			}
			lastNumber = item.getErNumber();
		}
		
		return checkBelegnummer(items.get(0));
	}

	private void checkErData(ErImportItem20475 item) {
		checkLieferant(item);
		if (checkSteuercode(item)) {
			checkBetraege(item);
		}
		checkKontonummer(item);
		checkLieferantenrechnungsnummer(item);
	}
	
	private boolean checkLieferantenrechnungsnummer(ErImportItem20475 item) {
		if (item.getCnrSupplier().length() > beanServices.getMaximaleLaengeLieferantenrechnungsnummer()) {
			item.addError(errorFactory.lieferantenrechnungsnummerZuLang(item));
			return false;
		}
		return true;
	}

	private boolean checkKontonummer(ErImportItem20475 item) {
		if (isSplittbuchung(item)) {
			return true;
		}
		
		KontoDto kontoDto = findKonto(item);
		return kontoDto != null;
	}
	
	private KontoDto findKonto(ErImportItem20475 item) {
		KontoDto kontoDto = sachkontoCache.getValueOfKey(item.getAccountNumber());
		if (kontoDto == null) {
			item.addError(errorFactory.kontonummerUnbekannt(item));
			return null;
		}
		return kontoDto;
	}
	
	private boolean isSplittbuchung(ErImportItem20475 item) {
		return !Helper.isStringEmpty(item.getAccountNumber()) && item.getAccountNumber().contains("+");
	}

	private KontoDto getSachkontoByKontonummer(String kontonummer) {
		try {
			KontoDto kontoDto = beanServices.findSachkontoByKontonummer(kontonummer);
			return kontoDto;
		} catch (RemoteException e) {
		}
		return null;
	}
	
	private KontoDto getKreditorenkontoByKontonummer(String kontonummer) {
		try {
			KontoDto kontoDto = beanServices.findKreditorenkontoByKontonummer(kontonummer);
			return kontoDto;
		} catch (RemoteException e) {
		}
		return null;
	}

	private boolean checkBetraege(ErImportItem20475 item) {
		UstUebersetzungDto steuercode = steuercodeCache.getValueOfKey(item.getTaxCode());
		if (steuercode == null) {
			return true;
		}
		
		MwstsatzDto mwstsatzDto = findMwstSatzDto(new MwstsatzbezId(steuercode.getMwstSatzBezIId()), item.getDate());
		if (mwstsatzDto == null) {
			item.addError(errorFactory.keinGueltigerMwstsatzZuDatum(item, steuercode));
			return false;
		}
		
		WaehrungDto waehrungDto = waehrungCache.getValueOfKey(item.getCurrency());
		if (waehrungDto == null) {
			item.addError(errorFactory.waehrungUnbekannt(item));
			return false;
		}
		
		CoinRoundingResult calcResult = calc(item, mwstsatzDto, isNetto(steuercode));
		if (item.getTaxAmount() == null 
				|| item.getTaxAmount() != null && item.getTaxAmount().compareTo(calcResult.getTaxAmount()) != 0) {
			item.addError(errorFactory.ustBetragStimmtNichtUeberein(item, calcResult));
			return false;
		}
		
		return true;
	}

	private CoinRoundingResult calc(ErImportItem20475 item, MwstsatzDto mwstsatzDto, boolean fromNetto) {
		EingangsrechnungDto calcDto = new EingangsrechnungDto();
		calcDto.setNBetrag(item.getAmount());
		calcDto.setMwstsatzIId(mwstsatzDto.getIId());
		calcDto.setWaehrungCNr(item.getCurrency());
		
		return fromNetto ? beanServices.calcMwstBetragFromNetto(calcDto) 
				: beanServices.calcMwstBetragFromBrutto(calcDto);
	}

	private boolean isNetto(UstUebersetzungDto steuercode) {
		return Boolean.TRUE.equals(steuercode.getBIgErwerb()) 
				|| !reversechargeartOhneDto.getIId().equals(steuercode.getReversechargeartIId());
	}

	private MwstsatzDto findMwstSatzDto(MwstsatzbezId mwstSatzBezId, Date date) {
		List<MwstsatzDto> saetze = beanServices.findMwstsatzFindJuengsteZuDatum(mwstSatzBezId, new Timestamp(date.getTime()));
		return saetze.isEmpty() ? null : saetze.get(0);
	}

	private boolean checkSteuercode(ErImportItem20475 item) {
		UstUebersetzungDto ustUebersetzung = findSteuercode(item);
		return ustUebersetzung != null;
	}
	
	private UstUebersetzungDto findSteuercode(ErImportItem20475 item) {
		if (Helper.isStringEmpty(item.getTaxCode()))
			return null;
		
		UstUebersetzungDto ustUebersetzung = steuercodeCache.getValueOfKey(item.getTaxCode());
		if (ustUebersetzung == null) {
			item.addError(errorFactory.steuercodeUnbekannt(item));
			return null;
		}
		return ustUebersetzung;
	}

	private boolean checkLieferant(ErImportItem20475 item) {
		LieferantDto lieferant = findLieferant(item);
		
		return lieferant != null;
	}

	private LieferantDto findLieferant(ErImportItem20475 item) {
		KontoDto kreditorKontoDto = kreditorkontoCache.getValueOfKey(item.getCreditor());
		if (kreditorKontoDto == null) {
			item.addError(errorFactory.kreditorennummerUnbekannt(item));
			return null;
		}
		
		return findLieferantByKontoPartnerKbez(item, kreditorKontoDto);
	}
	
	private LieferantDto findLieferantByKontoPartnerKbez(ErImportItem20475 item, KontoDto kreditorenKonto) {
		LieferantDto[] lieferanten = beanServices.findLieferantByKontoIId(kreditorenKonto.getIId());
		if (lieferanten == null || lieferanten.length < 1) {
			item.addError(errorFactory.kreditorennummerKeinemLieferantenZugeordnet(item));
			return null;
		}
		
		List<LieferantDto> matches = new ArrayList<LieferantDto>();
		for (LieferantDto lieferant : lieferanten) {
			PartnerDto liefPartner = beanServices.findPartnerByIId(lieferant.getPartnerIId());
			if (liefPartner != null && liefPartner.getCKbez() != null) {
				if (liefPartner.getCKbez().toLowerCase().contains(item.getCreditorName().toLowerCase())) {
					lieferant.setPartnerDto(liefPartner);
					matches.add(lieferant);
				}
			}
		}
		
		if (matches.size() > 1) {
			item.addError(errorFactory.kreditorennummerMehrereMoeglicheLieferanten(item, matches));
			return null;
		}
		
		if (matches.isEmpty()) {
			item.addError(errorFactory.kreditorennameStimmtNichtUeberein(item));
			return null;
		}
		
		return matches.get(0);
	}
	
	private String formatBetrag(BigDecimal amount) {
		return amount == null ? "null" : Helper.formatZahl(amount, FinanzFac.NACHKOMMASTELLEN, beanServices.getTheClientDto().getLocMandant());
	}
	
	private class ErImportErrorFactory {
		public ErImportError20475 erNummerNichtFortlaufend(ErImportItem20475 item, Integer lastNumber) {
			return new ErImportError20475(Severity.ERROR, 
					"ER-Nr '" + item.getErNumber() + "' ist nicht fortlaufend (Vorg\u00e4nger ER-Nr = '" + lastNumber + "').");
		}
		
		public ErImportError20475 geschaeftsjahrGesperrt(Integer geschaeftsjahr) {
			return new ErImportError20475(Severity.ERROR, 
					"Gesch\u00e4ftsjahr " + geschaeftsjahr + " ist gesperrt.");
		}

		public ErImportError20475 erstellteEingangsrechnungHatAndereCnr(ErImportItem20475 item,
				EingangsrechnungDto eingangsrechnungDto) {
			return new ErImportError20475(Severity.ERROR, 
					"Erzeugte Eingangsrechnung '" + eingangsrechnungDto.getCNr() + "' hat andere Belegnummer als aus Import-Datei erwartet (= '" + item.getCnr() + "')");
		}

		public ErImportError20475 kreditorennameStimmtNichtUeberein(ErImportItem20475 item) {
			return new ErImportError20475(Severity.ERROR, 
					"F\u00fcr Kreditorenkonto '" + item.getCreditor() + "' wurde kein Lieferant mit Kurzbezeichnung '" 
							+ item.getCreditorName() + "' gefunden.");
		}

		public ErImportError20475 importErNummernBeginnenNichtBeiEins(ErImportItem20475 item) {
			return new ErImportError20475(Severity.ERROR, 
					"Erste ER-Nr aus Import-Datei muss mit Belegnummer = 1 (ist aber = '" + item.getCnr() + "') beginnen, "
							+ "da sich noch keine Eingangsrechnungen im HELIUM V befinden.");
		}

		public ErImportError20475 importErNummernNichtFortlaufendZuLetzterEr(EingangsrechnungDto erDto, ErImportItem20475 item) {
			return new ErImportError20475(Severity.ERROR, 
					"Eingangsrechnungen aus Import schlie\u00DFen nicht an Eingangsrechnungen aus HELIUM V an: Letzte HV-ER-Nr = '" 
							+ erDto.getCNr() + "', erste ER-Nr Import-Datei = '" + item.getCnr() + "'.");
		}

		public ErImportError20475 feldInhaltZuKurz(ErImportItem20475 item, String spaltenname) {
			return new ErImportError20475(Severity.ERROR, 
					"Feldinhalt '" + spaltenname + "' ist zu kurz.");
		}

		public ErImportError20475 erMehrererGeschaeftsjahre(ErImportItem20475 item) {
			return new ErImportError20475(Severity.ERROR, 
					"Es sind Eingangsrechnungen mehrerer Gesch\u00e4ftsjahre vorhanden.");
		}

		public ErImportError20475 feldInhaltIstLeer(ErImportItem20475 item, String spaltenname) {
			return new ErImportError20475(Severity.ERROR, 
					"Feldinhalt '" + spaltenname + "' ist leer.");
		}

		public ErImportError20475 kontonummerUnbekannt(ErImportItem20475 item) {
			return new ErImportError20475(Severity.ERROR, 
					"Kontonummer '" + item.getAccountNumber() + "' nicht gefunden.");
		}

		public ErImportError20475 ustBetragStimmtNichtUeberein(ErImportItem20475 item,
				CoinRoundingResult calcResult) {
			return new ErImportError20475(Severity.WARNING, 
					"Berechneter Ust-Betrag stimmt nicht \u00fcberein: "
						+ "ER-Betrag = " + formatBetrag(item.getAmount())
						+ ", ER-Ust = " + formatBetrag(item.getTaxAmount())
						+ ", berechnete Ust = " + formatBetrag(calcResult.getTaxAmount()) + " (\u00fcber MwstSatz=" + calcResult.getMwstsatzDto().getFMwstsatz() 
						+ ", MwstSatzIId=" + calcResult.getMwstsatzDto().getIId() + ")");
		}

		public ErImportError20475 waehrungUnbekannt(ErImportItem20475 item) {
			return new ErImportError20475(Severity.ERROR, 
					"W\u00e4hrung '" + item.getCurrency() + "' nicht gefunden.");
		}

		public ErImportError20475 keinGueltigerMwstsatzZuDatum(ErImportItem20475 item,
				UstUebersetzungDto steuercode) {
			return new ErImportError20475(Severity.ERROR, 
					"Kein gueltiger Mwst-Satz (Steuercode '" + steuercode.getCNr() + "') zu Datum '" + item.getDate() + "' gefunden.");
		}

		public ErImportError20475 steuercodeUnbekannt(ErImportItem20475 item) {
			return new ErImportError20475(Severity.ERROR, 
					"Steuercode '" + item.getTaxCode() + "' nicht definiert.");
		}

		public ErImportError20475 kreditorennummerUnbekannt(ErImportItem20475 item) {
			return new ErImportError20475(Severity.ERROR, 
					"Kreditorenkonto mit Kontonummer '" + item.getCreditor() + "' nicht gefunden.");
		}

		public ErImportError20475 kreditorennummerKeinemLieferantenZugeordnet(ErImportItem20475 item) {
			return new ErImportError20475(Severity.ERROR, 
					"Dem Kreditorenkonto '" + item.getCreditor() + "' ist kein Lieferant zugeordnet.");
		}
		
		public ErImportError20475 kreditorennummerMehrereMoeglicheLieferanten(ErImportItem20475 item, List<LieferantDto> lieferanten) {
			String liefString = "";
			for (LieferantDto lief : lieferanten) {
				liefString += lief.getPartnerDto().getCKbez() + ", ";
			}
			return new ErImportError20475(Severity.ERROR, 
					"Dem Kreditorenkonto '" + item.getCreditor() + "' sind mehrere Lieferanten zugeordnet, deren Kurzbez. '" 
							+ item.getCreditorName() + "' beinhalten: " + liefString);
		}
	
		public ErImportError20475 lieferantenrechnungsnummerZuLang(ErImportItem20475 item) {
			return new ErImportError20475(Severity.WARNING, 
					"Lieferantenrechnungsnummer '" + item.getCnrSupplier() + "' ist zu lang. Sie wird auf die letzten " 
							+ beanServices.getMaximaleLaengeLieferantenrechnungsnummer() + " Stellen begrenzt.");
		}
	}
	
	private class SteuercodeCache extends HvCreatingCachingProvider<String, UstUebersetzungDto> {
		private List<UstUebersetzungDto> hmSteuercodes;
		
		public SteuercodeCache(List<UstUebersetzungDto> hmSteuercodes) {
			this.hmSteuercodes = trimKeys(hmSteuercodes);
		}
		
		private List<UstUebersetzungDto> trimKeys(List<UstUebersetzungDto> hmSteuercodes) {
			for (UstUebersetzungDto dto : hmSteuercodes) {
				dto.setCNr(dto.getCNr() != null ? dto.getCNr().trim() : null);
			}
			return hmSteuercodes;
		}
		
		protected UstUebersetzungDto provideValue(String key, String transformedKey) {
			if (key == null) return null;
			
			for (UstUebersetzungDto dto : hmSteuercodes) {
				if (key.equals(dto.getCNr())) {
					return dto;
				}
			}
			return null;
		}
	}
	
	private class WaehrungCache extends HvCreatingCachingProvider<String, WaehrungDto> {
		private WaehrungDto[] waehrungen;
		
		public WaehrungCache(WaehrungDto[] waehrungen) {
			this.waehrungen = waehrungen;
		}
		
		@Override
		protected WaehrungDto provideValue(String key, String transformedKey) {
			for (WaehrungDto dto : waehrungen) {
				if (key.equals(dto.getCNr())) {
					return dto;
				}
			}
			return null;
		}
	}
	
	private class KontoCache extends HvCreatingCachingProvider<String, KontoDto> {
		private String kontotyp;
		
		public KontoCache(String kontotyp) {
			this.kontotyp = kontotyp;
		}
		
		@Override
		protected KontoDto provideValue(String key, String transformedKey) {
			if (FinanzServiceFac.KONTOTYP_KREDITOR.equals(kontotyp)) {
				return getKreditorenkontoByKontonummer(key);
			}
			if (FinanzServiceFac.KONTOTYP_SACHKONTO.equals(kontotyp)) {
				return getSachkontoByKontonummer(key);
			}
			
			return null;
		}
	}
	
	private class GeschaeftsjahrCache extends HvCreatingCachingProvider<Date, Integer> {
		private Map<Integer, Timestamp[]> gjDatumsbereich;
		
		public GeschaeftsjahrCache() {
			gjDatumsbereich = new HashMap<Integer, Timestamp[]>();
		}
		
		@Override
		protected Integer provideValue(Date key, Date transformedKey) {
			for (Entry<Integer, Timestamp[]> entry : gjDatumsbereich.entrySet()) {
				Timestamp[] bereich = entry.getValue();
				if (key.compareTo(bereich[0]) >= 0 && key.compareTo(bereich[1]) < 0) {
					return entry.getKey();
				}
			}
			
			try {
				Integer gj = beanServices.getGeschaeftsjahrZuDatum(key);
				gjDatumsbereich.put(gj, beanServices.getDatumbereichPeriodeGJ(gj));
				return gj;
			} catch (RemoteException e) {
			}
			return null;
		}
		
	}
	
	private class ErBelegnummer {
		private Integer geschaeftsjahr;
		private Integer belegnummer;
		
		public ErBelegnummer(ErImportItem20475 importItem) {
			setProperties(importItem.getCnr(), importItem.getDate());
		}
		
		public boolean isValidNextBelegnummerTo(ErBelegnummer lastBelegnummer) {
			if (lastBelegnummer == null 
					|| !getGeschaeftsjahr().equals(lastBelegnummer.getGeschaeftsjahr())) {
				return belegnummer.equals(new Integer(1));
			}
			
			return getBelegnummer().equals(new Integer(lastBelegnummer.getBelegnummer() + 1));
		}

		public ErBelegnummer(EingangsrechnungDto erDto) {
			setProperties(erDto.getCNr(), erDto.getDBelegdatum());
		}
		
		private void setProperties(String cnr, Date belegdatum) {
			geschaeftsjahr = gjCache.getValueOfKey(belegdatum);
			belegnummer = Integer.parseInt(cnr.substring(
					cnr.length() - belegnummerFormat.getStellenLfdNummer()));
		}
		
		public Integer getBelegnummer() {
			return belegnummer;
		}
		public Integer getGeschaeftsjahr() {
			return geschaeftsjahr;
		}
		
	}
}
