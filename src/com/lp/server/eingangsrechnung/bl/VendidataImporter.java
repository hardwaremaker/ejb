/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.server.eingangsrechnung.bl;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungKontierungDto;
import com.lp.server.eingangsrechnung.service.IVendidataImporterBeanServices;
import com.lp.server.eingangsrechnung.service.VendidataDiscountPayment;
import com.lp.server.eingangsrechnung.service.VendidataImportStats;
import com.lp.server.eingangsrechnung.service.VendidataImporterResult;
import com.lp.server.eingangsrechnung.service.VendidataPaymentInfo;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.util.EJBExceptionLP;
import com.lp.util.EJBVendidataImportExceptionLP;
import com.lp.util.Helper;


public class VendidataImporter {
	protected final ILPLogger log = LPLogService.getInstance().getLogger(this.getClass());

	private IVendidataImporterBeanServices beanServices;
	private VendidataTransformer transformer;
	private List<VendidataDiscountPayment> importAusgangsgutschriften;
	private List<VendidataDiscountPayment> importRechnungen;
	private VendidataImportStats stats;
	private Timestamp reBelegdatum;
	private IZahlbetragCalculator betragCalculator;
	private Integer reNachkommastellen;
	
	public VendidataImporter(IVendidataImporterBeanServices beanServices) {
		this(beanServices, new VendidataTransformer());
	}

	public VendidataImporter(IVendidataImporterBeanServices beanServices, VendidataTransformer transformer) {
		this.beanServices = beanServices;
		this.transformer = transformer;
		importAusgangsgutschriften = new ArrayList<VendidataDiscountPayment>();
		importRechnungen = new ArrayList<VendidataDiscountPayment>();
		stats = new VendidataImportStats();
	}
	
	private void resetData() {
		importAusgangsgutschriften.clear();
		importRechnungen.clear();
		stats.reset();
	}
	
	/**
	 * Prueft die XML-Daten und sammelt Error-Messages bzgl. fehlender Datensaetze.
	 * 
	 * @param xmlDaten
	 * @return ein Resultat der Pruefung mit Error-Messages und Statistik
	 */
	public VendidataImporterResult checkImportXMLDaten(String xmlDaten) {
		List<EJBVendidataImportExceptionLP> importErrors = new ArrayList<EJBVendidataImportExceptionLP>();
		
		try {
			List<VendidataDiscountPayment> payments = transformer.transform(xmlDaten);
			resetData();
			
			for (VendidataDiscountPayment discountPayment : payments) {
				List<EJBVendidataImportExceptionLP> checkErrors = new ArrayList<EJBVendidataImportExceptionLP>();
				if (discountPayment.isAusgangsgutschrift()) {
					checkErrors = checkAusgangsgutschrift(discountPayment);
					if (checkErrors.isEmpty()) {
						importAusgangsgutschriften.add(discountPayment);
						stats.incrementTotalImportsAusgangsgutschriften();
					}
				} else if (discountPayment.isRechnung()) {
					checkErrors = checkRechnung(discountPayment);
					if (checkErrors.isEmpty()) {
						importRechnungen.add(discountPayment);
						stats.incrementTotalImportsRechnungen();
					}
				}

				importErrors.addAll(checkErrors);
			}
		} catch (EJBVendidataImportExceptionLP ex) {
			stats.incrementErrorCounts();
			importErrors.add(ex);
		} catch (Exception ex) {
			log.error("Exception", ex);
			stats.incrementErrorCounts();
			importErrors.add(new EJBVendidataImportExceptionLP(EJBVendidataImportExceptionLP.SEVERITY_ERROR, 
					EJBExceptionLP.FEHLER_ER_IMPORT_XML_KLASSEN_TRANSFORMATION, ex));
		}
		
		return new VendidataImporterResult(importErrors, stats);
	}
	
	/**
	 * Entfernt alle Paymentinfos mit Nettobetrag = 0.0
	 * 
	 * @param ausgangsgutschrift
	 * @return bereinigte Ausgangsgutschrift
	 */
	private VendidataDiscountPayment removeRedundantPaymentInfos(VendidataDiscountPayment ausgangsgutschrift) {
		List<VendidataPaymentInfo> deletes = new ArrayList<VendidataPaymentInfo>();
		for (VendidataPaymentInfo kontierung : ausgangsgutschrift.getPositionen()) {
			if (kontierung.isEmpty()) deletes.add(kontierung);
		}
		
		ausgangsgutschrift.getPositionen().removeAll(deletes);
		
		return ausgangsgutschrift;
	}

	/**
	 * Importiert die XML-Daten und erstellt Ausgangsgutschriften.
	 * 
	 * @param xmlDaten
	 * @return ein Resultat der Import-Pruefung mit Error-Messages und Statistik
	 */
	public VendidataImporterResult importXMLDaten(String xmlDaten) {
		VendidataImporterResult result = checkImportXMLDaten(xmlDaten);
		setBetragCalculator();
		result.getImportErrors().addAll(importAusgangsgutschriften());
		result.getImportErrors().addAll(importRechnungen());
		
		return result;
	}
	
	private void setBetragCalculator() {
		Boolean bruttoStattNetto = beanServices.get4VendingErArImportBruttoStattNetto();
		if (Boolean.TRUE.equals(bruttoStattNetto))
			betragCalculator = new CalcBruttoAsBase();
		else
			betragCalculator = new CalcNettoAsBase();
	}

	private List<EJBVendidataImportExceptionLP> importRechnungen() {
		List<EJBVendidataImportExceptionLP> importErrors = new ArrayList<EJBVendidataImportExceptionLP>();
		reBelegdatum = null;
		try {
			reNachkommastellen = beanServices.getUINachkommastellenVK();
		} catch (RemoteException ex) {
			log.error("RemoteException", ex);
			reNachkommastellen = FinanzFac.NACHKOMMASTELLEN;
		}
		
		for (VendidataDiscountPayment rechnung : importRechnungen) {
			try {
				createRechnung(rechnung, betragCalculator);
				stats.incrementGoodImportsRechnungen();
			} catch (Exception ex) {
				log.error("Exception", ex);
				stats.incrementErrorImportsRechnungen();
				stats.incrementErrorCounts();
				importErrors.add(new EJBVendidataImportExceptionLP(EJBVendidataImportExceptionLP.SEVERITY_ERROR, 
						EJBExceptionLP.FEHLER_ER_IMPORT_RE_ANLEGEN_FEHLGESCHLAGEN, ex));
			}
		}
		return importErrors;
	}

	private List<EJBVendidataImportExceptionLP> importAusgangsgutschriften() {
		List<EJBVendidataImportExceptionLP> importErrors = new ArrayList<EJBVendidataImportExceptionLP>();
		
		for (VendidataDiscountPayment ausgangsgutschrift : importAusgangsgutschriften) {
			try {
				createEingangsrechnung(ausgangsgutschrift, betragCalculator);
				stats.incrementGoodImportsAusgangsgutschriften();
			} catch (RemoteException ex) {
				log.error("RemoteException", ex);
				stats.incrementErrorImportsAusgangsgutschriften();
				importErrors.add(new EJBVendidataImportExceptionLP(EJBVendidataImportExceptionLP.SEVERITY_ERROR, 
						EJBExceptionLP.FEHLER_ER_IMPORT_ER_ANLEGEN_FEHLGESCHLAGEN, ex));
			} catch (EJBExceptionLP ex) {
				log.error("EJBExceptionLP", ex);
				stats.incrementErrorImportsAusgangsgutschriften();
				stats.incrementErrorCounts();
				importErrors.add(new EJBVendidataImportExceptionLP(EJBVendidataImportExceptionLP.SEVERITY_ERROR, 
						EJBExceptionLP.FEHLER_ER_IMPORT_ER_ANLEGEN_FEHLGESCHLAGEN, ex));
			}
		}
		return importErrors;
	}

	/**
	 * @param discountPayment
	 * @throws RemoteException
	 */
	private void createEingangsrechnung(VendidataDiscountPayment discountPayment, IZahlbetragCalculator betragCalc)
			throws RemoteException {
		EingangsrechnungDto erDto = new EingangsrechnungDto();
		erDto.setEingangsrechnungartCNr(EingangsrechnungFac.EINGANGSRECHNUNGART_EINGANGSRECHNUNG);
		LieferantDto liefDto = getLieferantByCustomerId(discountPayment.getKundennummer());
		erDto.setMandantCNr(beanServices.getTheClientDto().getMandant());
		erDto.setLieferantIId(liefDto.getIId());
		erDto.setDBelegdatum(discountPayment.getBelegdatum());
		erDto.setDFreigabedatum(discountPayment.getBelegdatum());
		erDto.setCLieferantenrechnungsnummer("0");
		erDto.setNBetragfw(BigDecimal.ZERO);
		erDto.setNBetrag(BigDecimal.ZERO);
		erDto.setNUstBetrag(BigDecimal.ZERO);
		erDto.setNUstBetragfw(BigDecimal.ZERO);
		erDto.setNKurs(BigDecimal.ONE.setScale(LocaleFac.ANZAHL_NACHKOMMASTELLEN_WECHSELKURS,
				BigDecimal.ROUND_HALF_EVEN));
		erDto.setWaehrungCNr(beanServices.getTheClientDto().getSMandantenwaehrung());
		erDto.setZahlungszielIId(liefDto.getZahlungszielIId());
		erDto.setBMitpositionen(Helper.boolean2Short(true));
		erDto.setCText(discountPayment.getTextPeriodenInfo());
		erDto.setBReversecharge(Helper.boolean2Short(false));
		erDto.setBIgErwerb(Helper.boolean2Short(false));
		erDto = beanServices.createEingangsrechnung(erDto);
		
		for (VendidataPaymentInfo kontierung : discountPayment.getPositionen()) {
			EingangsrechnungKontierungDto kontierungDto = new EingangsrechnungKontierungDto();
			kontierungDto.setEingangsrechnungIId(erDto.getIId());
			kontierungDto.setArtikelIId(null);
			kontierungDto.setPositionsartCNr(LocaleFac.POSITIONSART_HANDEINGABE);
			setHandeingabeBezeichnung(kontierung, kontierungDto);
			kontierungDto.setKontoIId(liefDto.getKontoIIdWarenkonto());
			kontierungDto.setKostenstelleIId(liefDto.getIIdKostenstelle());
			MwstsatzDto mwstSatzDto = getMwstSatz(kontierung, liefDto.getMwstsatzbezIId(), new Timestamp(erDto.getDBelegdatum().getTime()));
			kontierungDto.setMwstsatzIId(mwstSatzDto.getIId());
//			BigDecimal mwstBetrag = Helper.getMehrwertsteuerBetrag(kontierung.getBetrag(), new BigDecimal(mwstSatzDto.getFMwstsatz()));
			Zahlbetrag zahlbetrag = betragCalc.calc(kontierung.getBetrag(), mwstSatzDto, FinanzFac.NACHKOMMASTELLEN);
			System.out.println(zahlbetrag.asString());
			kontierungDto.setNBetrag(zahlbetrag.getBrutto());
			kontierungDto.setNBetragUst(zahlbetrag.getMwst());
			kontierungDto.setCKommentar(kontierung.getKommentar());
			beanServices.createEingangsrechnungKontierung(kontierungDto);
		}
	}
	
	private void createRechnung(VendidataDiscountPayment discountPayment, IZahlbetragCalculator betragCalc) throws RemoteException {
		KundeDto kundeDto = getKundeByCustomerId(discountPayment.getKundennummer());
		RechnungDto rechnungDto = beanServices.setupDefaultRechnung(kundeDto);
		rechnungDto.setTBelegdatum(getRechnungBelegdatum(discountPayment));
		rechnungDto.setCKopftextuebersteuert(discountPayment.getTextPeriodenInfo());
		rechnungDto.setNKurs(BigDecimal.ONE);
		rechnungDto = beanServices.createRechnung(rechnungDto);
		
		for (VendidataPaymentInfo position : discountPayment.getPositionen()) {
			RechnungPositionDto positionDto = new RechnungPositionDto();
			positionDto.setPositionsartCNr(RechnungFac.POSITIONSART_RECHNUNG_HANDEINGABE);
			positionDto.setRechnungIId(rechnungDto.getIId());
			positionDto.setBDrucken(Helper.getShortTrue());
			positionDto.setBRabattsatzuebersteuert(Helper.getShortFalse());
			positionDto.setBMwstsatzuebersteuert(Helper.getShortFalse());

			MwstsatzDto mwstSatzDto = getMwstSatz(
					position, kundeDto.getMwstsatzbezIId(), rechnungDto.getTBelegdatum());
			positionDto.setMwstsatzIId(mwstSatzDto.getIId());
			
			setHandeingabeBezeichnung(position, positionDto);
			positionDto.setXTextinhalt(position.getKommentar());
			positionDto.setNMenge(BigDecimal.ONE);
			positionDto.setEinheitCNr(SystemFac.EINHEIT_STUECK);
//			BigDecimal mwstBetrag = Helper.getMehrwertsteuerBetrag(position.getBetrag(), new BigDecimal(mwstSatzDto.getFMwstsatz()));
			Zahlbetrag zahlbetrag = betragCalc.calc(position.getBetrag(), mwstSatzDto, reNachkommastellen);
			positionDto.setNNettoeinzelpreis(zahlbetrag.getNetto());
			positionDto.setNBruttoeinzelpreis(zahlbetrag.getBrutto());
			positionDto.setNEinzelpreis(positionDto.getNNettoeinzelpreis());
			positionDto.setFRabattsatz(new Double(0));
			positionDto.setFZusatzrabattsatz(new Double(0));
			positionDto.setBNettopreisuebersteuert(Helper.getShortFalse());
			
			beanServices.createRechnungposition(positionDto, kundeDto.getLagerIIdAbbuchungslager());
		}
	}
	
	private MwstsatzDto getMwstSatz(VendidataPaymentInfo position, Integer kundeMwstsatzBezIId, Timestamp belegdatum) throws RemoteException {
		if (position.hasUebersteuertenSteuersatz()) {
			MwstsatzDto mwstSatzDto = getMwStSatz(position.getSteuersatz(), belegdatum);
			if (mwstSatzDto != null)
				return mwstSatzDto;
		} 
		MwstsatzDto mwstSatzDto = getMwStSatz(kundeMwstsatzBezIId, belegdatum);
		return mwstSatzDto;
	}
	
	private Timestamp getRechnungBelegdatum(VendidataDiscountPayment discountPayment) {
		if (reBelegdatum == null) {
			Timestamp currentTime = new Timestamp(System.currentTimeMillis());
			Integer gjCurrent = beanServices.findGeschaeftsjahrFuerDatum(new Date(currentTime.getTime()));
			Integer gjDiscounts = beanServices.findGeschaeftsjahrFuerDatum(discountPayment.getBelegdatum());
			reBelegdatum = gjCurrent.compareTo(gjDiscounts) > 0 ? new Timestamp(discountPayment.getBelegdatum().getTime()) : currentTime;
		}
		return reBelegdatum;
	}

	private String[] getBezTeile(VendidataPaymentInfo paymentInfo) {
		if (paymentInfo.hasUebersteuerteBezeichnung()) {
			return Helper.intelligenteWorttrennung(new int[]{40,  40}, paymentInfo.getUebersteuerteBezeichnung());
		}
		
		String bezeichnung = paymentInfo.isBezeichnungToken() 
				? beanServices.getTextRespectUISpr(paymentInfo.getBezeichnung(), beanServices.getTheClientDto().getMandant(), 
					beanServices.getTheClientDto().getLocUi())
				: paymentInfo.getBezeichnung();
		String[] bezTeile = Helper.intelligenteWorttrennung(new int[]{40,  40}, bezeichnung);
		return bezTeile;
	}
	
	private void setHandeingabeBezeichnung(VendidataPaymentInfo paymentInfo, RechnungPositionDto positionDto) {
		String[] bezTeile = getBezTeile(paymentInfo);
		if (bezTeile == null) {
			positionDto.setCBez(".");
			return;
		}
		if (bezTeile.length > 0) positionDto.setCBez(bezTeile[0]);
		if (bezTeile.length > 1) positionDto.setCZusatzbez(bezTeile[1]);
	}

	private void setHandeingabeBezeichnung(VendidataPaymentInfo paymentInfo, EingangsrechnungKontierungDto kontierungDto) {
		String[] bezTeile = getBezTeile(paymentInfo);
		if (bezTeile == null) return;
		if (bezTeile.length > 0) kontierungDto.setSHandeingabeBez(bezTeile[0]);
		if (bezTeile.length > 1) kontierungDto.setSHandeingabeZbez(bezTeile[1]);
	}

	private List<EJBVendidataImportExceptionLP> checkAusgangsgutschrift(VendidataDiscountPayment ausgangsgutschrift) {
		List<EJBVendidataImportExceptionLP> errors = new ArrayList<EJBVendidataImportExceptionLP>();
		// check CustomerId / Fremdsystemnummer
		checkKundendaten(ausgangsgutschrift, errors);
		
		// check Sachkonto
		if (!existsWarenkonto(ausgangsgutschrift.getKundennummer())) {
			stats.incrementErrorCounts();
			errors.add(new EJBVendidataImportExceptionLP(ausgangsgutschrift.getKundennummer(), 
					ausgangsgutschrift.getBezeichnungKunde(), EJBVendidataImportExceptionLP.SEVERITY_ERROR, 
					EJBExceptionLP.FEHLER_ER_IMPORT_WARENKONTO_NICHT_DEFINIERT));
		}
		// check Kostenstelle
		if (!existsKostenstelle(ausgangsgutschrift.getKundennummer())) {
			stats.incrementErrorCounts();
			errors.add(new EJBVendidataImportExceptionLP(ausgangsgutschrift.getKundennummer(), 
					ausgangsgutschrift.getBezeichnungKunde(), EJBVendidataImportExceptionLP.SEVERITY_ERROR, 
					EJBExceptionLP.FEHLER_ER_IMPORT_KOSTENSTELLE_NICHT_DEFINIERT));
		}
		// check MwSt-Satz
		if (!existsMwStSatz(ausgangsgutschrift.getKundennummer())) {
			stats.incrementErrorCounts();
			errors.add(new EJBVendidataImportExceptionLP(ausgangsgutschrift.getKundennummer(), 
					ausgangsgutschrift.getBezeichnungKunde(), EJBVendidataImportExceptionLP.SEVERITY_ERROR,
					EJBExceptionLP.FEHLER_ER_IMPORT_MWSTSATZ_NICHT_DEFINIERT));
		}
		
		checkUebersteuertenMwstSatz(ausgangsgutschrift, new Timestamp(ausgangsgutschrift.getBelegdatum().getTime()), errors);
		
		return errors;
	}
	
	private List<EJBVendidataImportExceptionLP> checkRechnung(VendidataDiscountPayment rechnung) {
		List<EJBVendidataImportExceptionLP> errors = new ArrayList<EJBVendidataImportExceptionLP>();
		checkKundendaten(rechnung, errors);
		checkUebersteuertenMwstSatz(rechnung, getRechnungBelegdatum(rechnung), errors);

		return errors;
	}

	private void checkUebersteuertenMwstSatz(VendidataDiscountPayment discountPayment, Timestamp belegdatum, List<EJBVendidataImportExceptionLP> errors) {
		for (VendidataPaymentInfo position : discountPayment.getPositionen()) {
			if (position.hasUebersteuertenSteuersatz()
					&& !existsMwStSatz(position.getSteuersatz(), belegdatum)) {
				stats.incrementErrorCounts();
				EJBVendidataImportExceptionLP exc = new EJBVendidataImportExceptionLP(discountPayment.getKundennummer(), 
						discountPayment.getBezeichnungKunde(), EJBVendidataImportExceptionLP.SEVERITY_ERROR, 
						EJBExceptionLP.FEHLER_ER_IMPORT_UEBERSTEUERTER_MWSTSATZ_NICHT_GEFUNDEN);
				exc.setUebersteuertenSteuersatz(position.getSteuersatz());
				errors.add(exc);
			}
		}
	}
	
	private void checkKundendaten(VendidataDiscountPayment discountPayment, List<EJBVendidataImportExceptionLP> errors) {
		Integer anzahlKunden = getAnzahlKundenByCustomerId(discountPayment.getKundennummer());
		if (anzahlKunden.equals(0)) {
			stats.incrementErrorCounts();
			errors.add(new EJBVendidataImportExceptionLP(discountPayment.getKundennummer(), 
					discountPayment.getBezeichnungKunde(), EJBVendidataImportExceptionLP.SEVERITY_ERROR, 
					EJBExceptionLP.FEHLER_ER_IMPORT_FREMDSYSTEMNUMMER_NICHT_GEFUNDEN));
		} else if (anzahlKunden.compareTo(1) > 0) {
			stats.incrementErrorCounts();
			errors.add(new EJBVendidataImportExceptionLP(discountPayment.getKundennummer(), 
					discountPayment.getBezeichnungKunde(), EJBVendidataImportExceptionLP.SEVERITY_ERROR, 
					EJBExceptionLP.FEHLER_ER_IMPORT_FREMDSYSTEMNUMMER_NICHT_EINDEUTIG));
		}
	}

	private boolean existsMwStSatz(String customerId) {
		LieferantDto lieferantDto = getLieferantByCustomerId(customerId);
		return lieferantDto == null || lieferantDto.getMwstsatzbezIId() == null ? false : true;
	}
	
	private boolean existsMwStSatz(Double steuersatz, Timestamp belegdatum) {
		MwstsatzDto mwstSatzDto = getMwStSatz(steuersatz, belegdatum);
		return mwstSatzDto != null;
	}

	private boolean existsKostenstelle(String customerId) {
		LieferantDto lieferantDto = getLieferantByCustomerId(customerId);
		return lieferantDto == null || lieferantDto.getIIdKostenstelle() == null ? false : true;
	}

	private boolean existsWarenkonto(String customerId) {
		LieferantDto lieferantDto = getLieferantByCustomerId(customerId);
		return lieferantDto == null || lieferantDto.getKontoIIdWarenkonto() == null ? false : true;
	}

	private Integer getAnzahlKundenByCustomerId(String customerId) {
		return getKundenByCustomerId(customerId).size();
	}
	
	private List<KundeDto> getKundenByCustomerId(String customerId) {
		try {
			return beanServices.kundeFindByFremdsystemnummer(customerId);
		} catch (Throwable t) {
		}
		
		return null;
	}
	
	private KundeDto getKundeByCustomerId(String customerId) {
		List<KundeDto> kunden = getKundenByCustomerId(customerId);
		if (kunden.size() != 1) return null;
		
		return kunden.get(0);
	}
	
	private LieferantDto getLieferantByCustomerId(String customerId) {
		List<KundeDto> kunden = getKundenByCustomerId(customerId);
		if (kunden.size() != 1) return null;
		
		KundeDto kundeDto = kunden.get(0);
		LieferantDto lieferantDto = getLieferantByPartnerIId(kundeDto.getPartnerIId());
		if (lieferantDto == null) {
			lieferantDto = createVerstecktenLieferantenAusKunden(kundeDto.getIId());
		}
		return lieferantDto;
	}
	
	private LieferantDto createVerstecktenLieferantenAusKunden(Integer kundeIId) {
		try {
			return beanServices.createVerstecktenLieferantenAusKunden(kundeIId);
		} catch (Throwable t) {
			log.error("Throwable", t);
		}
		return null;
	}
	
	private LieferantDto getLieferantByPartnerIId(Integer iId) {
		try {
			LieferantDto lieferantDto = beanServices.lieferantFindByPartnerIId(iId);
			return lieferantDto;
		} catch (RemoteException e) {
		}
		
		return null;
	}
	
	private MwstsatzDto getMwStSatz(Integer mwstSatzIId,
			Timestamp belegdatum) throws RemoteException {
		return beanServices
				.mwstsatzFindByMwstsatzbezIIdAktuellster(mwstSatzIId, belegdatum);
	}
	
	private MwstsatzDto getMwStSatz(Double steuersatz, Timestamp belegdatum) {
		MwstsatzDto[] mwstSaetze = beanServices.mwstsatzfindAll(belegdatum);
		for (MwstsatzDto mwst : mwstSaetze) {
			if (mwst.getFMwstsatz().equals(steuersatz)) return mwst;
		}
		return null;
	}
	
	private class Zahlbetrag {
		private BigDecimal netto;
		private BigDecimal brutto;
		private BigDecimal mwst;
		
		public Zahlbetrag() {
		}
		public void setNetto(BigDecimal netto) {
			this.netto = netto;
		}
		public BigDecimal getNetto() {
			return netto;
		}
		public void setBrutto(BigDecimal brutto) {
			this.brutto = brutto;
		}
		public BigDecimal getBrutto() {
			return brutto;
		}
		public void setMwst(BigDecimal mwst) {
			this.mwst = mwst;
		}
		public BigDecimal getMwst() {
			return mwst;
		}
		public String asString() {
			return "Zahlbetrag: Brutto=" + getBrutto() + ", Netto=" + getNetto() + ", Mwst=" + getMwst();
		}
	}
	
	private interface IZahlbetragCalculator {
		Zahlbetrag calc(BigDecimal betrag, MwstsatzDto mwstsatzDto, Integer nachkommastellen);
	}
	
	private class CalcBruttoAsBase implements IZahlbetragCalculator {
		public Zahlbetrag calc(BigDecimal brutto, MwstsatzDto mwstsatzDto, Integer nachkommastellen) {
			Zahlbetrag zahlbetrag = new Zahlbetrag();
			zahlbetrag.setBrutto(Helper.rundeKaufmaennisch(brutto, nachkommastellen));
			BigDecimal mwstSatz = new BigDecimal(mwstsatzDto.getFMwstsatz()).movePointLeft(2);
			BigDecimal netto = brutto.divide(BigDecimal.ONE.add(mwstSatz), nachkommastellen, BigDecimal.ROUND_HALF_EVEN);
			zahlbetrag.setMwst(Helper.rundeKaufmaennisch(brutto.subtract(netto), nachkommastellen));
			zahlbetrag.setNetto(netto);
			return zahlbetrag;
		}
	}
	
	private class CalcNettoAsBase implements IZahlbetragCalculator {
		public Zahlbetrag calc(BigDecimal netto, MwstsatzDto mwstsatzDto, Integer nachkommastellen) {
			Zahlbetrag zahlbetrag = new Zahlbetrag();
			zahlbetrag.setNetto(Helper.rundeKaufmaennisch(netto, nachkommastellen));
			BigDecimal mwst = Helper.getProzentWert(netto, new BigDecimal(mwstsatzDto.getFMwstsatz()), nachkommastellen);
			zahlbetrag.setMwst(mwst);
			zahlbetrag.setBrutto(zahlbetrag.getNetto().add(zahlbetrag.getMwst()));
			return zahlbetrag;
		}
	}
}
