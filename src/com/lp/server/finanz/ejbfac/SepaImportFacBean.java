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
package com.lp.server.finanz.ejbfac;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.lp.server.eingangsrechnung.assembler.EingangsrechnungDtoAssembler;
import com.lp.server.eingangsrechnung.ejb.Eingangsrechnung;
import com.lp.server.eingangsrechnung.ejb.EingangsrechnungQuery;
import com.lp.server.eingangsrechnung.ejb.Eingangsrechnungzahlung;
import com.lp.server.eingangsrechnung.ejb.EingangsrechnungzahlungQuery;
import com.lp.server.eingangsrechnung.service.EingangsrechnungAdapter;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungartDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungzahlungAdapter;
import com.lp.server.eingangsrechnung.service.EingangsrechnungzahlungDto;
import com.lp.server.eingangsrechnung.service.ZahlungsvorschlagDto;
import com.lp.server.finanz.assembler.SepakontoauszugDtoAssembler;
import com.lp.server.finanz.bl.sepa.ISepaKontoauszugVerifier;
import com.lp.server.finanz.bl.sepa.SepaImportQueryBuilder;
import com.lp.server.finanz.bl.sepa.SepaImportSearchHelper;
import com.lp.server.finanz.bl.sepa.SepaImportSearchHelperHaben;
import com.lp.server.finanz.bl.sepa.SepaImportSearchHelperSoll;
import com.lp.server.finanz.bl.sepa.SepaKontoauszugVerifierFactory;
import com.lp.server.finanz.bl.sepa.SepaXmlCamtBase;
import com.lp.server.finanz.ejb.Iso20022Bankverbindung;
import com.lp.server.finanz.ejb.Iso20022LastschriftSchema;
import com.lp.server.finanz.ejb.Iso20022Query;
import com.lp.server.finanz.ejb.Iso20022Standard;
import com.lp.server.finanz.ejb.Iso20022ZahlungsauftragSchema;
import com.lp.server.finanz.ejb.Sepakontoauszug;
import com.lp.server.finanz.ejb.SepakontoauszugQuery;
import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.finanz.service.BuchungKompakt;
import com.lp.server.finanz.service.BuchungdetailDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.ISepaImportResult;
import com.lp.server.finanz.service.Iso20022BankverbindungDto;
import com.lp.server.finanz.service.Iso20022BankverbindungDtoAssembler;
import com.lp.server.finanz.service.Iso20022NachrichtDtoAssembler;
import com.lp.server.finanz.service.Iso20022PaymentsDto;
import com.lp.server.finanz.service.Iso20022StandardDto;
import com.lp.server.finanz.service.Iso20022StandardDtoAssembler;
import com.lp.server.finanz.service.Iso20022StandardEnum;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.SepaImportFac;
import com.lp.server.finanz.service.SepaImportInfoDto;
import com.lp.server.finanz.service.SepaImportProperties;
import com.lp.server.finanz.service.SepaImportResult;
import com.lp.server.finanz.service.SepaImportTransformResult;
import com.lp.server.finanz.service.SepaKontoauszug;
import com.lp.server.finanz.service.SepaKontoauszugVersionEnum;
import com.lp.server.finanz.service.SepaZahlung;
import com.lp.server.finanz.service.SepakontoauszugDto;
import com.lp.server.partner.service.BankDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.partner.service.PartnerbankDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.rechnung.ejb.Rechnung;
import com.lp.server.rechnung.ejb.RechnungQuery;
import com.lp.server.rechnung.ejb.Rechnungzahlung;
import com.lp.server.rechnung.ejb.RechnungzahlungQuery;
import com.lp.server.rechnung.service.RechnungAdapter;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungDtoAssembler;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungartDto;
import com.lp.server.rechnung.service.RechnungzahlungAdapter;
import com.lp.server.rechnung.service.RechnungzahlungDto;
import com.lp.server.system.ejbfac.EJBExcFactory;
import com.lp.server.system.ejbfac.HvCreatingCachingProvider;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.docnode.DocNodeBase;
import com.lp.server.system.jcr.service.docnode.DocNodeFile;
import com.lp.server.system.jcr.service.docnode.DocNodeSepaImport;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.ClientRemoteFac;
import com.lp.server.system.service.ImportProgressDto;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.LandplzortDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.OrtDto;
import com.lp.server.system.service.PayloadDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.VerfuegbareHostsDto;
import com.lp.server.system.service.WechselkursDto;
import com.lp.server.system.service.ZahlungszielDto;
import com.lp.server.util.BankverbindungId;
import com.lp.server.util.BelegAdapter;
import com.lp.server.util.BelegZahlungAdapter;
import com.lp.server.util.Facade;
import com.lp.server.util.KontoId;
import com.lp.server.util.Validator;
import com.lp.util.EJBExceptionLP;
import com.lp.util.EJBSepaImportExceptionLP;
import com.lp.util.Helper;

@Stateless
public class SepaImportFacBean extends Facade implements SepaImportFac {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	@org.jboss.ejb3.annotation.TransactionTimeout(5000)
	public List<ISepaImportResult> searchForImportMatches(KontoId kontoId,
			List<SepaKontoauszug> ktoauszuege, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		myLogger.warn("Start, Suche nach Belegen fuer Sepa-Kontoauszug");
		List<ISepaImportResult> results = new ArrayList<ISepaImportResult>();
		List<String> erlaubteErStati = getEingangsrechnungFac().getErlaubteStatiFuerEingangsrechnungZahlung();
		List<String> erlaubteArStati = getRechnungFac().getErlaubteStatiFuerRechnungZahlung();

		List<String> erlaubteErArten = getErlaubteArtenEingangsrechnung();
		List<String> erlaubteArArten = getErlaubteArtenAusgangsrechnung();
		
		SepaImportSearchHelper searchHelperSoll = new SepaImportSearchHelperSoll(
				ktoauszuege, erlaubteErStati, erlaubteErArten, theClientDto);
		SepaImportSearchHelper searchHelperHaben = new SepaImportSearchHelperHaben(
				ktoauszuege, erlaubteArStati, erlaubteArArten, theClientDto);

		String waehrungCnr = getWaehrungOfKonto(kontoId, theClientDto);
		List<SepaZahlung> zahlungen = searchHelperSoll.getZahlungen();
		int i = 0;
		for (SepaZahlung zahlung : zahlungen) {
			myLogger.warn("Zahlung " + (++i) + " (von " + zahlungen.size() + "): " + zahlung.toString());
			if (zahlung.getBetrag().isSoll()) {
				results.add(searchForAusgangsrechnung(zahlung, searchHelperHaben, waehrungCnr));
			} else {
				results.add(searchForEingangsrechnung(zahlung, searchHelperSoll, waehrungCnr));
			}
		}

		myLogger.warn("Ende, Suche nach Belegen fuer Sepa-Kontoauszug");
		return results;
	}

	@Override
	public String getWaehrungOfKonto(KontoId kontoId, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		KontoDto kontoDto = getFinanzFac().kontoFindByPrimaryKey(kontoId.id());
		return getWaehrungOfKonto(kontoDto, theClientDto);
	}
	
	private String getWaehrungOfKonto(KontoDto kontoDto, TheClientDto theClientDto) {
		return kontoDto.getWaehrungCNrDruck() != null 
				? kontoDto.getWaehrungCNrDruck() 
						: theClientDto.getSMandantenwaehrung();
	}

	private ISepaImportResult searchForAusgangsrechnung(SepaZahlung zahlung,
			SepaImportSearchHelper searchHelper, String waehrungCnr) throws RemoteException, EJBExceptionLP {
		
		ISepaImportResult result = new SepaImportResult();
		result.setPayment(zahlung);
		
		List<Integer> foundArIds = searchForMatches(result, searchHelper, 0, new ArrayList<Integer>());
		List<BelegAdapter> arDtos = new ArrayList<BelegAdapter>();
		
		for (Integer arId : foundArIds) {
			RechnungDto rechnungDto = getRechnungFac().rechnungFindByPrimaryKeyOhneExc(arId);
			//nur RE in EUR werden vorgeschlagen
			if (rechnungDto != null 
					&& isWaehrung(waehrungCnr, rechnungDto.getWaehrungCNr())
					&& Helper.isOneOf(rechnungDto.getRechnungartCNr(), searchHelper.getErlaubteArten())) {
				BelegAdapter arAdapter = createRechnungAdapter(rechnungDto, 
						getOffenerBetragRechnung(rechnungDto), searchHelper.getTheClientDto());
				arDtos.add(arAdapter);
			}
		}

		arDtos = compareZahlungsbetragWithBelege(result, arDtos, searchHelper);

		if (arDtos.size() == 1) {
			result.setSelectedIndex(0);
		} 			
		
		if (arDtos.size() <= MAX_BELEGE_FUER_AUSWAHL) {
			result.setFoundItems(arDtos);
		} else {
			result.setFoundItems(new ArrayList<BelegAdapter>());
		}
			
		return result;
	}

	/**
	 * Suche nach Eingangsrechnungen, die zur Zahlung passen (koennen)
	 * Die gefundenen Eingangsrechnungen werden im ImportResult gespeichert und
	 * der noch zu bezahlende Betrag ermittelt.
	 * Kommt nur eine ER bei der Suche heraus, wird dieser eine gleich
	 * fuer die UI vorselektiert.
	 * 
	 * @param zahlung aktuell zu bearbeitende Sepa-Zahlung
	 * @param searchHelper Hilfsklasse fuer Suchparameter
	 * @param waehrungCnr Waehrung
	 * @return fertiges ImportResult
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 */
	private ISepaImportResult searchForEingangsrechnung(SepaZahlung zahlung,
			SepaImportSearchHelper searchHelper, String waehrungCnr) throws RemoteException, EJBExceptionLP {

		ISepaImportResult result = new SepaImportResult();
		result.setPayment(zahlung);

		List<Integer> foundErIds = searchForMatches(result, searchHelper, 0, new ArrayList<Integer>());
		List<BelegAdapter> erDtos = new ArrayList<BelegAdapter>();

		for (Integer erId : foundErIds) {
			EingangsrechnungDto erDto = getEingangsrechnungFac().eingangsrechnungFindByPrimaryKeyOhneExc(erId);
			// nur ER in EUR werden vorgeschlagen
			if (erDto != null 
					&& isWaehrung(waehrungCnr, erDto.getWaehrungCNr())
					&& Helper.isOneOf(erDto.getEingangsrechnungartCNr(), searchHelper.getErlaubteArten())) {
				BelegAdapter erAdapter = createEingangsrechnungAdapter(erDto, 
						getOffenerBetragEingangsrechnung(erDto), searchHelper.getTheClientDto());
				erDtos.add(erAdapter);
			}
		}
		
		if (erDtos.size() != 1 || !hasZahlungsvorschlagEqualsPayment(result)) {
			erDtos = compareZahlungsbetragWithBelege(result, erDtos, searchHelper);
		}

		if (erDtos.size() == 1) {
			result.setSelectedIndex(0);
		} 			
		
		if(erDtos.size() <= MAX_BELEGE_FUER_AUSWAHL) {
			result.setFoundItems(erDtos);
		} else {
			result.setFoundItems(new ArrayList<BelegAdapter>());
		}
				
		return result;
	}

	private boolean hasZahlungsvorschlagEqualsPayment(ISepaImportResult result) {
		if (Helper.isStringEmpty(result.getPayment().getAuftraggeberreferenz())) 
			return false;
		
		ZahlungsvorschlagDto zvDto = getZahlungsvorschlagFac().zahlungsvorschlagFindByAuftraggeberreferenzOhneExc(
				result.getPayment().getAuftraggeberreferenz());
		if (zvDto == null) 
			return false;
		
		if (zvDto.getNZahlbetrag().compareTo(result.getPayment().getBetrag().getWert()) != 0
				|| !Helper.short2boolean(zvDto.getBWaereVollstaendigBezahlt())) {
			return false;
		}		
		
		result.setTotalMatch(true);
		result.setCompletedForSelectedBeleg(true);
		return true;
	}

	/**
	 * Rekursive Methode fuer das Suchen nach Eingangsrechnungen.
	 * Dabei wird die Liste der Suchkriterien (Query-Klassen) abgearbeitet und
	 * die gefundene Menge an ER weiter eingeschraenkt bis nur mehr eine
	 * ER uebrig bleibt oder die Liste der Suchkriterien zu Ende ist. 
	 * 
	 * @param result aktuell zu bearbeitende Sepa-Zahlung
	 * @param searchHelper Hilfsklasse fuer Suchparameter
	 * @param index aktueller Index der Suchkriterien-Liste
	 * @param foundBelegIds Liste der gefundenen Eingangsrechnungen-Ids
	 * @return Liste der gefundenen Eingangsrechnungen-Ids
	 */
	private List<Integer> searchForMatches(ISepaImportResult result,
			SepaImportSearchHelper searchHelper, int index, List<Integer> foundBelegIds) {
		
		if (index >= searchHelper.getSearchCriteriaOrder().size() ||
				foundBelegIds.size() == 1) return foundBelegIds;
		
		SepaImportQueryBuilder queryBuilder = searchHelper.getSearchCriteriaOrder().get(index++);
		if (queryBuilder.setValueForCriterion(result.getPayment(), searchHelper.getBuchungOfZahlung(result.getPayment()))) {
			if (queryBuilder.isTotalMatch()) {
				foundBelegIds = queryBuilder.getResultList(em);
				if (!foundBelegIds.isEmpty()) {
					result.setTotalMatch(true);
					return foundBelegIds;
				}
			} else {
				foundBelegIds = queryBuilder.getResultList(em, foundBelegIds);
			}
		}

		return searchForMatches(result, searchHelper, index, foundBelegIds);
	}
	
	/**
	 * Berechnet den noch offenen, zu zahlenden Betrag der uebergebenen Eingangsrechnung
	 * 
	 * @param erDto Eingangsrechnung
	 * @return offener Zahlbetrag der ER
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	private BigDecimal getOffenerBetragEingangsrechnung(EingangsrechnungDto erDto) 
			throws EJBExceptionLP, RemoteException {
		BigDecimal bereitsBezahltWertBrutto = getEingangsrechnungFac().getBezahltBetragFw(erDto.getIId(), null);
		BigDecimal anzahlungenWertBrutto = BigDecimal.ZERO;
		if (EingangsrechnungFac.EINGANGSRECHNUNGART_SCHLUSSZAHLUNG.equals(erDto.getEingangsrechnungartCNr())) {
			anzahlungenWertBrutto = getEingangsrechnungFac().getAnzahlungenGestelltZuSchlussrechnungFw(erDto.getIId());
		}
		return erDto.getNBetragfw().subtract(bereitsBezahltWertBrutto.setScale(FinanzFac.NACHKOMMASTELLEN))
				.subtract(anzahlungenWertBrutto);
	}

	/**
	 * Berechnet den noch offenen, zu zahlenden Betrag der uebergebenen Ausgangsrechnung
	 * 
	 * @param rechnungDto Rechnung
	 * @return offener Zahlbetrag der Rechnung
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	private BigDecimal getOffenerBetragRechnung(RechnungDto rechnungDto) 
			throws EJBExceptionLP, RemoteException {
		BigDecimal bereitsBezahltWertBrutto = getRechnungFac().getBereitsBezahltWertVonRechnungFw(rechnungDto.getIId(), null)
				.add(getRechnungFac().getBereitsBezahltWertVonRechnungUstFw(rechnungDto.getIId(), null));
		BigDecimal anzahlungenWertBrutto = BigDecimal.ZERO;
		if (RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG.equals(rechnungDto.getRechnungartCNr())) {
			anzahlungenWertBrutto = getRechnungFac().getAnzahlungenZuSchlussrechnungBrutto(rechnungDto.getIId());
		}
		return rechnungDto.getNWertfw().add(rechnungDto.getNWertustfw())
				.subtract(bereitsBezahltWertBrutto.setScale(FinanzFac.NACHKOMMASTELLEN))
				.subtract(anzahlungenWertBrutto.setScale(FinanzFac.NACHKOMMASTELLEN));
	}

	private List<BelegAdapter> compareZahlungsbetragWithBelege(ISepaImportResult result, 
			List<BelegAdapter> belege, SepaImportSearchHelper searchHelper) 
					throws RemoteException, EJBExceptionLP {
		
		List<BelegAdapter> belegeNew = new ArrayList<BelegAdapter>();
		if (belege.isEmpty()) return belegeNew;

		//		Offene Betraege der ERs durchgehen und mit dem Zahlungsbetrag abgleichen (inkl. Skonto)
//		Sehr eng ueberpruefen (Ueberlegung: Anzahlungen werden sonst uebersehen)
		for (BelegAdapter beleg : belege) {
			if (result.getPayment().getBetrag().getWert().compareTo(beleg.getOffenerBetrag()) == 0) {
				belegeNew.add(beleg);
				continue;
			}
			
			//Skontobetraege ueberpruefen
			ZahlungszielDto zahlungszielDto = getMandantFac()
					.zahlungszielFindByPrimaryKey(beleg.getZahlungszielIId(), searchHelper.getTheClientDto());
			BigDecimal betragMitSkonto = null;
			if (zahlungszielDto.getSkontoAnzahlTage1() != null && zahlungszielDto.getSkontoAnzahlTage1() != 0 &&
					searchHelper.getBuchungOfZahlung(result.getPayment()).getBuchungsdatum().before(
					Helper.addiereTageZuDatum(beleg.getDBelegdatum(), zahlungszielDto.getSkontoAnzahlTage1()))) {
				betragMitSkonto = berechneBetragMitSkonto(beleg.getBruttoBetrag(), zahlungszielDto.getSkontoProzentsatz1());
				
			} else if (zahlungszielDto.getSkontoAnzahlTage2() != null && zahlungszielDto.getSkontoAnzahlTage2() != 0 &&
					searchHelper.getBuchungOfZahlung(result.getPayment()).getBuchungsdatum().before(
					Helper.addiereTageZuDatum(beleg.getDBelegdatum(), zahlungszielDto.getSkontoAnzahlTage2()))) {
				betragMitSkonto = berechneBetragMitSkonto(beleg.getBruttoBetrag(), zahlungszielDto.getSkontoProzentsatz2());
			}
			
			if (betragMitSkonto != null && result.getPayment().getBetrag().getWert().compareTo(betragMitSkonto) == 0) {
				belegeNew.add(beleg);
			}
		}
		
		if (belegeNew.isEmpty() || belegeNew.size() > 1) {
			result.setTotalMatch(false);
		}
		
		if (belegeNew.isEmpty()) return belege;
		
		return belegeNew;
	}

	private BigDecimal berechneBetragMitSkonto(BigDecimal wert, BigDecimal prozent) {
		if (prozent == null)
			return wert;
		prozent = BigDecimal.ONE.subtract(prozent.divide(new BigDecimal(
				"100.00"), 4, BigDecimal.ROUND_HALF_EVEN));
		return wert.multiply(prozent);
	}

	@Override
	public Integer importSepaImportResults(List<ISepaImportResult> results,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		ClientRemoteFac clientRemoteFac = new ClientRemoteFac() {
			public void publish(PayloadDto payloadDto) throws RemoteException {
			}
			public void neueNachrichtenVerfuegbar() throws RemoteException {
			}
		};
		return importSepaImportResultsImpl(results, clientRemoteFac, "", theClientDto);
	}
	
	private void updatePartnerBankverbindung(Integer partnerIId, SepaZahlung payment, 
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		if (partnerIId == null || payment == null || payment.getBeteiligter().getBic() == null) {
			// ist keine BIC vorhanden kann sowieso keine neue Bankverbindung angelegt werden
			return;
		}
		String paymentBic = payment.getBeteiligter().getBic();
		PartnerbankDto[] partnerBankDtos = getBankFac().partnerbankFindByPartnerIIdOhneExc(partnerIId, theClientDto);
		
		if (partnerBankDtos != null && partnerBankDtos.length > 0) {
			for (PartnerbankDto partnerBank : partnerBankDtos) {
				if (areStringsEqual(partnerBank.getCIban(), payment.getBeteiligter().getIban())) {
					// Iban ist bereits beim Partner vorhanden
					return;
				}
			}
		}
		
		// Bankverbindung neu anlegen
		List<BankDto> bankDtos = getBankFac().bankFindByBIC(paymentBic, theClientDto);
		Integer bankPartnerIId = null;
		
		if (bankDtos == null || bankDtos.isEmpty()) {
			PartnerDto partnerDto = new PartnerDto();
			partnerDto.setCKbez(paymentBic);
			partnerDto.setCName1nachnamefirmazeile1(paymentBic);
			partnerDto.setPartnerartCNr(PartnerFac.PARTNERART_SONSTIGES);
			partnerDto.setLocaleCNrKommunikation(theClientDto.getLocMandantAsString());
			
			LandplzortDto lpoDto = getLandplzortForBIC(paymentBic, theClientDto);
			partnerDto.setLandplzortDto(lpoDto);
			partnerDto.setLandplzortIId(lpoDto != null ? lpoDto.getIId() : null);
			partnerDto.setBVersteckt(false);
			
			BankDto bankDto = new BankDto();
			bankDto.setCBic(paymentBic);
			bankDto.setPartnerDto(partnerDto);
			bankPartnerIId = getBankFac().createBank(bankDto, theClientDto);
		} else {
			bankPartnerIId = bankDtos.get(0).getPartnerIId();
		}
		
		PartnerbankDto partnerBankDto = new PartnerbankDto();
		partnerBankDto.setBankPartnerIId(bankPartnerIId);
		partnerBankDto.setPartnerIId(partnerIId);
		partnerBankDto.setCIban(payment.getBeteiligter().getIban());
		partnerBankDto.setISort(getBankFac().getMaxISort(partnerIId) + 1);
		getBankFac().createPartnerbank(partnerBankDto, theClientDto);
	}

	private LandplzortDto getLandplzortForBIC(String bic, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		String lkz = bic.substring(4, 6);
		LandDto landDto = getSystemFac().landFindByLkz(lkz);
		Integer landIId = null;
		if (landDto == null) {
			landDto = new LandDto();
			landDto.setCLkz(lkz);
			landDto.setCName(lkz);
			landDto.setILaengeuidnummer(0);
			landDto.setBSepa(new Short((short)1));
			landIId = getSystemFac().createLand(landDto, theClientDto);
			landDto = getSystemFac().landFindByPrimaryKey(landIId);
		} else {
			landIId = landDto.getIID();
		}
		
		String plz = "-";
		OrtDto ortDto = getSystemFac().ortFindByNameOhneExc(plz);
		Integer ortIId = null;
		if (ortDto == null) {
			ortDto = new OrtDto();
			ortDto.setCName("-");
			ortIId = getSystemFac().createOrt(ortDto, theClientDto);
			ortDto = getSystemFac().ortFindByPrimaryKey(ortIId);
		} else {
			ortIId = ortDto.getIId();
		}
		
		LandplzortDto lpoDto = getSystemFac().landplzortFindByLandOrtPlzOhneExc(landDto.getIID(), ortDto.getIId(), plz);
		
		if (lpoDto != null) return lpoDto;
		
		lpoDto = new LandplzortDto();
		lpoDto.setLandDto(landDto);
		lpoDto.setIlandID(landIId);
		lpoDto.setCPlz(plz);
		lpoDto.setOrtDto(ortDto);
		lpoDto.setOrtIId(ortIId);
		
		Integer lpoIId = getSystemFac().createLandplzort(lpoDto, theClientDto);
		
		return getSystemFac().landplzortFindByPrimaryKey(lpoIId);
	}

	private boolean areStringsEqual(String iban1, String iban2) {
		if (iban1 == null || iban2 == null) return false;
		
		iban1 = iban1.trim();
		iban2 = iban2.trim();
		
		return iban1.equals(iban2);
	}

	@Override
	public SepaImportTransformResult readAndTransformSepaKontoauszug(SepaImportProperties importProperties, 
			TheClientDto theClientDto) throws EJBExceptionLP {
		Validator.notNull(importProperties, "importProperties");
		Validator.notNull(importProperties.getBankverbindungDto(), "bankverbindungDto");
		Validator.notNull(importProperties.getBankverbindungDto().getCIban(), "IBAN");

		SepaXmlCamtBase sepaBase = new SepaXmlCamtBase();
		SepaImportTransformResult result = sepaBase.createSepaKontoauszug(importProperties);
		
		if (result != null && !result.getKtoauszug().isEmpty() && !result.hasErrors()) {
			SepaKontoauszugVerifierFactory verifierFactory = new SepaKontoauszugVerifierFactory();
			ISepaKontoauszugVerifier verifier = verifierFactory.getVerifier(
					result.getKtoauszug().get(0).getTypVersion(), 
					importProperties.getBankverbindungDto().getCIban());
			result = verifier.groupAndVerify(result);
			persistSepaKontoauszuege(importProperties.getBankverbindungDto(), result, theClientDto);
			return result;
		}
		
		result.setKtoauszug(new ArrayList<SepaKontoauszug>());
		return result;
	}
	
	private boolean hasErrors(List<EJBSepaImportExceptionLP> messages) {
		for (EJBSepaImportExceptionLP msg : messages) {
			if (EJBSepaImportExceptionLP.SEVERITY_ERROR == msg.getSeverity()) return true;
		}
		return false;
	}

	private void persistSepaKontoauszuege(BankverbindungDto bvDto, SepaImportTransformResult result, TheClientDto theClientDto) {
		if (hasErrors(result.getWarnings())) return;
		
		for (SepaKontoauszug ktoauszug : result.getKtoauszug()) {
			SepakontoauszugDto dto = setupDefaultSepakontoauszug(ktoauszug);
			if (getBuchenFac().existsBuchungenMitAuszugsNr(bvDto.getKontoIId(), dto.getIAuszug(), 
					new Date(dto.getTAuszug().getTime()), theClientDto)) {
				throw EJBExcFactory.buchungenMitAuszugsnummerExistieren(dto);
			}
			dto.setBankverbindungIId(bvDto.getIId());
			createSepakontoauszug(dto, theClientDto);
			result.setImportErfolgreich(true);
		}
	}

	private SepakontoauszugDto setupDefaultSepakontoauszug(SepaKontoauszug sepaKontoauszug) {
		SepakontoauszugDto dto = new SepakontoauszugDto();
		dto.setCCamtFormat(sepaKontoauszug.getTypVersion() != null ? sepaKontoauszug.getTypVersion().toString() : null);
		if (sepaKontoauszug.getKontoInfo().getIban().startsWith("AT")) {
			dto.setIAuszug(sepaKontoauszug.getAuszugsnr().intValue());
		} else {
			dto.setIAuszug(sepaKontoauszug.getElektronischeAuszugsnr().intValue());
		}
		dto.setNAnfangssaldo(sepaKontoauszug.getStartSaldo() != null ? 
				sepaKontoauszug.getStartSaldo().getBetrag().getPlusMinusWert() : null);
		dto.setNEndsaldo(sepaKontoauszug.getEndSaldo() != null ? 
				sepaKontoauszug.getEndSaldo().getBetrag().getPlusMinusWert() : null);
		dto.setOKontoauszug(sepaKontoauszug);
		dto.setVersion(SepaKontoauszugVersionEnum.DREI);
		dto.setTAuszug(new Timestamp(sepaKontoauszug.getErstellungsDatum().getTime()));
		dto.setStatusCNr(SepaImportFac.SepakontoauszugStatus.ANGELEGT);
		
		return dto;
	}

	@Override
	public List<BelegAdapter> getAlleOffenenEingangsrechnungen(String waehrungCnr, TheClientDto theClientDto) 
			throws EJBExceptionLP, RemoteException {

		List<String> erlaubteStati = getEingangsrechnungFac().getErlaubteStatiFuerEingangsrechnungZahlung();
		List<String> erlaubtErArten = getErlaubteArtenEingangsrechnung();
		List<Eingangsrechnung> eingangsrechnungen = 
				EingangsrechnungQuery.listByMandantStatusCNr(em, theClientDto.getMandant(), erlaubteStati);
		List<BelegAdapter> erAdapterList = new ArrayList<BelegAdapter>();
		
		if (isBeleganzahlZuHoch(eingangsrechnungen.size())) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_SEPAIMPORT_ZUVIELE_OFFENE_ER, 
					"Es sind " + eingangsrechnungen.size() + " offene Eingangsrechnungen (> " + MAX_OFFENE_BELEGE + ") vorhanden.", 
					eingangsrechnungen.size(), MAX_OFFENE_BELEGE);
		}
		
		for (Eingangsrechnung er : eingangsrechnungen) {
			if (isWaehrung(waehrungCnr, er.getWaehrungCNr())
					&& Helper.isOneOf(er.getEingangsrechnungartCNr(), erlaubtErArten)) {
				EingangsrechnungDto erDto = EingangsrechnungDtoAssembler.createDto(er);
				EingangsrechnungAdapter adapter = createEingangsrechnungAdapter(erDto, 
						getOffenerBetragEingangsrechnung(erDto), theClientDto);
				erAdapterList.add(adapter);
			}
		}
		
		return erAdapterList;
	}

	@Override
	public List<BelegAdapter> getAlleOffenenAusgangsrechnungen(String waehrungCnr, TheClientDto theClientDto) 
			throws EJBExceptionLP, RemoteException {
		List<String> erlaubteStati = getRechnungFac().getErlaubteStatiFuerRechnungZahlung();
		List<String> erlaubteArArten = getErlaubteArtenAusgangsrechnung();
		List<Rechnung> rechnungen = 
				RechnungQuery.listByMandantStatusCNr(em, theClientDto.getMandant(), erlaubteStati);
		List<BelegAdapter> reAdapterList = new ArrayList<BelegAdapter>();
		
		if (isBeleganzahlZuHoch(rechnungen.size())) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_SEPAIMPORT_ZUVIELE_OFFENE_AR, 
					"Es sind " + rechnungen.size() + " offene Rechnungen (> " + MAX_OFFENE_BELEGE + ") vorhanden.", 
					rechnungen.size(), MAX_OFFENE_BELEGE);
		}
		
		for (Rechnung re : rechnungen) {
			if (isWaehrung(waehrungCnr, re.getWaehrungCNr())
					&& Helper.isOneOf(re.getRechnungartCNr(), erlaubteArArten)) {
				RechnungDto reDto = RechnungDtoAssembler.createDto(re);
				RechnungAdapter adapter = createRechnungAdapter(reDto, getOffenerBetragRechnung(reDto), theClientDto);
				reAdapterList.add(adapter);
			}
		}
		
		return reAdapterList;
	}
	
	private boolean isBeleganzahlZuHoch(Integer number) {
		return new Integer(MAX_OFFENE_BELEGE).compareTo(number) < 0;
	}

	@Override
	public Map<Integer, PartnerDto> getPartnerOfBelegeMap(List<BelegAdapter> belege, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		
		Map<Integer, PartnerDto> belegPartnerMap = new HashMap<Integer, PartnerDto>();
		
		for (BelegAdapter beleg : belege) {
			if (beleg instanceof EingangsrechnungAdapter) {
				LieferantDto lieferantDto = getLieferantFac()
						.lieferantFindByPrimaryKeyOhneExc(beleg.getPartnerIId(), theClientDto);
				if (lieferantDto == null || lieferantDto.getPartnerDto() == null) continue;
				belegPartnerMap.put(beleg.getIId(), lieferantDto.getPartnerDto());
			} else if (beleg instanceof RechnungAdapter) {
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(beleg.getPartnerIId(), theClientDto);
				if (kundeDto == null || kundeDto.getPartnerDto() == null) continue;
				belegPartnerMap.put(beleg.getIId(), kundeDto.getPartnerDto());
			}
		}
		
		return belegPartnerMap;
	}

	@Override
	public BigDecimal getSaldoVonBankverbindungByAuszug(Integer kontoIId,
			Integer auszugNr, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		
		MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
		Validator.notNull(mandantDto, "mandantDto");
		
		String ktoWaehrung = getWaehrungOfKonto(new KontoId(kontoIId), theClientDto);
		
		if (mandantDto.getWaehrungCNr() != null && mandantDto.getWaehrungCNr().startsWith(ktoWaehrung)) {
			return getBuchenFac().getSaldoVonKontoByAuszug(
					kontoIId, theClientDto.getGeschaeftsJahr(), 
							auszugNr, true, true, theClientDto);
		} else {
			return getBuchenFac().getSaldoVonKontoByAuszugInWaehrung(
					kontoIId, theClientDto.getGeschaeftsJahr(), 
					auszugNr, true, true, ktoWaehrung, theClientDto);
		}
	}
	
	@Override
	public void archiviereSepaKontoauszug(String xmlKontoauszug, String filename, 
			Integer bankverbindungIId, Integer auszugsNr, TheClientDto theClientDto) 
			throws EJBExceptionLP, RemoteException {
		Date dAnlegen = new Date(System.currentTimeMillis());
		BankverbindungDto bankverbindungDto = getFinanzFac()
				.bankverbindungFindByPrimaryKey(bankverbindungIId);
		PartnerDto partnerBankDto = getPartnerFac()
				.partnerFindByPrimaryKey(bankverbindungDto.getBankIId(), theClientDto);
		Integer geschaeftsjahr = getBuchenFac()
				.findGeschaeftsjahrFuerDatum(dAnlegen, theClientDto.getMandant());
		PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(
				theClientDto.getIDPersonal(), theClientDto);
		PartnerDto anlegerDto = getPartnerFac()
				.partnerFindByPrimaryKey(personalDto.getPartnerIId(), theClientDto);
		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
		String sDate = dateFormat.format(dAnlegen);
		
		JCRDocDto jcrDocDto = new JCRDocDto();
		DocNodeBase docNodeBase = new DocNodeSepaImport(bankverbindungDto, 
				partnerBankDto, geschaeftsjahr, dAnlegen);
		DocPath docPath = new DocPath(docNodeBase).add(new DocNodeFile(filename));

		jcrDocDto.setDocPath(docPath);
		try {
			jcrDocDto.setbData(xmlKontoauszug.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new EJBExceptionLP(e);
		}
		jcrDocDto.setbVersteckt(false);
		jcrDocDto.setlAnleger(anlegerDto.getIId());
		jcrDocDto.setlPartner(partnerBankDto.getIId());
		jcrDocDto.setlSicherheitsstufe(JCRDocFac.SECURITY_ARCHIV);
		jcrDocDto.setlZeitpunkt(dAnlegen.getTime());
		jcrDocDto.setsBelegart(JCRDocFac.DEFAULT_ARCHIV_BELEGART);
		jcrDocDto.setsGruppierung(JCRDocFac.DEFAULT_ARCHIV_GRUPPE);
		jcrDocDto.setsBelegnummer(sDate);
		jcrDocDto.setsFilename(filename);
		jcrDocDto.setsMIME(".xml");
		jcrDocDto.setsName("Import Sepa " + filename);
		jcrDocDto.setsRow(bankverbindungIId.toString());
		jcrDocDto.setsTable("");
		jcrDocDto.setsSchlagworte("Import Sepa Kontoauszug camt XML " + auszugsNr);
		
		getJCRDocFac().addNewDocumentOrNewVersionOfDocumentWithinTransaction(jcrDocDto, theClientDto);
	}
	
	private EingangsrechnungAdapter createEingangsrechnungAdapter(EingangsrechnungDto erDto, 
			BigDecimal offenerBetrag, TheClientDto theClientDto) throws RemoteException, EJBExceptionLP {
		LieferantDto lieferantDto = getLieferantFac().lieferantFindByPrimaryKeyOhneExc(erDto.getLieferantIId(), theClientDto);
		PartnerDto partnerDto = lieferantDto == null ? null :
			getPartnerFac().partnerFindByPrimaryKeyOhneExc(lieferantDto.getPartnerIId(), theClientDto);
		EingangsrechnungAdapter erAdapter = new EingangsrechnungAdapter(erDto, partnerDto == null ? "" : partnerDto.getCKbez());
		erAdapter.setOffenerBetrag(offenerBetrag);
		
		return erAdapter;
	}
	
	private RechnungAdapter createRechnungAdapter(RechnungDto rechnungDto, 
			BigDecimal offenerBetrag, TheClientDto theClientDto) throws RemoteException, EJBExceptionLP {
		KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKeyOhneExc(rechnungDto.getKundeIId(), theClientDto);
		PartnerDto partnerDto = kundeDto == null ? null :
			getPartnerFac().partnerFindByPrimaryKeyOhneExc(kundeDto.getPartnerIId(), theClientDto);
		RechnungAdapter rechnungAdapter = new RechnungAdapter(rechnungDto, partnerDto == null ? "" : partnerDto.getCKbez());
		rechnungAdapter.setOffenerBetrag(offenerBetrag);
		
		return rechnungAdapter;
	}

	private Context getInitialContextForRMI(String host, Integer port) throws NamingException {
		Hashtable<String, String> environment = new Hashtable<String, String>();

		environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.rmi.registry.RegistryContextFactory");
		environment.put(Context.PROVIDER_URL, "rmi://" + host + ":" + port);
		
		return new InitialContext(environment);
	}

	@Override
	public Integer importSepaImportResults(SepakontoauszugDto kontoauszugDto, List<ISepaImportResult> results,
			String payloadReference, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		ClientRemoteFac clientRemoteFac = new ClientRemoteFac() {
			public void publish(PayloadDto payloadDto) throws RemoteException {
			}
			public void neueNachrichtenVerfuegbar() throws RemoteException {
			}
		};
		
		try {
			VerfuegbareHostsDto host = getTheClientFacLocal().getVerfuegbarenHost(theClientDto);
			Context context = getInitialContextForRMI(host.getHostname(), host.getPort());
			clientRemoteFac = (ClientRemoteFac) context.lookup(ClientRemoteFac.REMOTE_BIND_NAME);
		} catch (Throwable t) {
			myLogger.error("Error during client remote host acquisition", t);
			myLogger.info("Starting import without remote notification");
		}
		
		Integer count = importSepaImportResultsImpl(results, clientRemoteFac, payloadReference, theClientDto);
		kontoauszugDto.setTVerbuchen(getTimestamp());
		kontoauszugDto.setPersonalIIdVerbuchen(theClientDto.getIDPersonal());
		kontoauszugDto.setStatusCNr(SepaImportFac.SepakontoauszugStatus.ERLEDIGT);
		updateSepakontoauszug(kontoauszugDto, theClientDto);
		
		return count;
	}
	
	private Integer importSepaImportResultsImpl(List<ISepaImportResult> results, ClientRemoteFac clientRemoteFac,
			String payloadReference, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		StatementAccountant accountant = new StatementAccountant(theClientDto);
		return accountant.book(results, clientRemoteFac, payloadReference);
	}

	class StatementAccountant {
		private TheClientDto theClientDto;
		private MandantDto mandantDto;
		private HvCreatingCachingProvider<Integer, KontoDto> bvKontoCache;
		
		public StatementAccountant(TheClientDto theClientDto) {
			this.theClientDto = theClientDto;
		}
		
		private MandantDto getMandantDto() throws EJBExceptionLP, RemoteException {
			if (mandantDto == null) {
				mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
			}
			return mandantDto;
		}

		private HvCreatingCachingProvider<Integer, KontoDto> getBvKontoCache() {
			if (bvKontoCache == null) {
				bvKontoCache = new HvCreatingCachingProvider<Integer, KontoDto>() {
					protected KontoDto provideValue(Integer key, Integer transformedKey) {
						try {
							BankverbindungDto bvDto = getFinanzFac().bankverbindungFindByPrimaryKey(key);
							KontoDto kontoDto = getFinanzFac().kontoFindByPrimaryKey(bvDto.getKontoIId());
							return kontoDto;
						} catch (RemoteException e) {
							throwEJBExceptionLPRespectOld(e);
						}
						return null;
					}
				};
			}
			return bvKontoCache;
		}
		
		public Integer book(List<ISepaImportResult> results, ClientRemoteFac clientRemoteFac,
			String payloadReference) throws EJBExceptionLP, RemoteException {
			int importedResultCount = 0;
			for (ISepaImportResult result : results) {
				if (!result.applyPayment()) continue;
				
				for (BelegZahlungAdapter belegZahlung : result.getManualPayments()) {

					if (belegZahlung instanceof EingangsrechnungzahlungAdapter) {
						if (!importEingangsrechnungzahlung(result, belegZahlung, theClientDto)) continue;

					} else if (belegZahlung instanceof RechnungzahlungAdapter) {
						if (!importRechnungzahlung(result, belegZahlung, theClientDto)) continue;
					}
					
				}
				
				for (BuchungKompakt buchung : result.getManualBookings()) {
					getBuchenFac().verbucheUmbuchung(buchung.getBuchungDto(), 
							buchung.getBuchungdetailList().toArray(new BuchungdetailDto[buchung.getBuchungdetailList().size()]), 
							theClientDto);
				}
				
				importedResultCount++;
				if (importedResultCount % 2 == 0) {
					publishPayload(clientRemoteFac, new PayloadDto(payloadReference, 
							new SepaImportInfoDto(new ImportProgressDto(results.size(), importedResultCount))));
//					clientRemoteFac.publish(new PayloadDto(payloadReference, 
//							new SepaImportInfoDto(new ImportProgressDto(results.size(), importedResultCount))));
				}
			}

			return importedResultCount;
		}
		
		private void publishPayload(ClientRemoteFac clientRemoteFac, PayloadDto payloadDto) {
			try {
				clientRemoteFac.publish(payloadDto);
			} catch (RemoteException re) {
				myLogger.error("RemoteException during publishing payload", re);
			}
		}
		
		private boolean importRechnungzahlung(ISepaImportResult result,
				BelegZahlungAdapter belegZahlung, TheClientDto theClientDto)
				throws RemoteException {
			RechnungDto reDto = getRechnungFac()
					.rechnungFindByPrimaryKeyOhneExc(belegZahlung.getRechnungIId());
			if (reDto == null) return false;
			
			fillRechnungzahlung(belegZahlung, result.getSelectedBeleg(), theClientDto);
			getRechnungFac().createUpdateZahlung((RechnungzahlungDto) belegZahlung.getRawBelegZahlungDto(), 
					belegZahlung.isBErledigt(), null, false, theClientDto);
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKeyOhneExc(
					reDto.getKundeIId(), theClientDto);
			Integer partnerIId = kundeDto.getPartnerIId();
			
			updatePartnerBankverbindung(partnerIId, result.getPayment(), theClientDto);
			
			return true;
		}

		private boolean importEingangsrechnungzahlung(ISepaImportResult result,
				BelegZahlungAdapter belegZahlung, TheClientDto theClientDto)
				throws RemoteException {
			EingangsrechnungDto erDto = getEingangsrechnungFac()
					.eingangsrechnungFindByPrimaryKeyOhneExc(belegZahlung.getRechnungIId());
			if (erDto == null) return false;
			
			fillEingangsrechnungzahlung(belegZahlung, result.getSelectedBeleg(), theClientDto);
			getEingangsrechnungFac().createEingangsrechnungzahlung((EingangsrechnungzahlungDto) belegZahlung.getRawBelegZahlungDto(), 
					belegZahlung.isBErledigt(), theClientDto);
			LieferantDto liefDto = getLieferantFac().lieferantFindByPrimaryKeyOhneExc(
					erDto.getLieferantIId(), theClientDto);
			Integer partnerIId = liefDto.getPartnerIId();

			updatePartnerBankverbindung(partnerIId, result.getPayment(), theClientDto);
			
			return true;
		}

		private void fillEingangsrechnungzahlung(BelegZahlungAdapter erAdapter, BelegAdapter beleg, TheClientDto theClientDto) 
				throws RemoteException {
			erAdapter.setNBetragUstfw(getEingangsrechnungFac().getWertUstAnteiligZuEingangsrechnungUst(
					erAdapter.getRechnungIId(), erAdapter.getNBetragfw()));
			
			fillBelegZahlungWithMandantWaehrung(erAdapter, beleg, theClientDto);
		}

		private void fillRechnungzahlung(BelegZahlungAdapter reAdapter, BelegAdapter beleg, TheClientDto theClientDto) 
				throws RemoteException {
			reAdapter.setNBetragUstfw(getRechnungFac().getWertUstAnteiligZuRechnungUst(
					reAdapter.getRechnungIId(), reAdapter.getNBetragfw()));
			reAdapter.setNBetragfw(reAdapter.getNBetragfw().subtract(reAdapter.getNBetragUstfw()));
			
			fillBelegZahlungWithMandantWaehrung(reAdapter, beleg, theClientDto);
		}
		
		

		private void fillBelegZahlungWithMandantWaehrung(
				BelegZahlungAdapter belegZahlung, BelegAdapter beleg,
				TheClientDto theClientDto) throws RemoteException {
			String mandantWaehrung = getMandantDto().getWaehrungCNr();
			String kontoWaehrung = getWaehrungOfKonto(getBvKontoCache().getValueOfKey(belegZahlung.getBankkontoIId()), theClientDto);
			belegZahlung.setNBetrag(getLocaleFac().rechneUmInAndereWaehrungGerundetZuDatum(
					belegZahlung.getNBetragfw(), kontoWaehrung,
							mandantWaehrung, belegZahlung.getDZahldatum(), theClientDto));
			belegZahlung.setNBetragUst(getLocaleFac().rechneUmInAndereWaehrungGerundetZuDatum(
					belegZahlung.getNBetragUstfw(), kontoWaehrung,
					mandantWaehrung, belegZahlung.getDZahldatum(), theClientDto));
			
			if (mandantWaehrung.equals(kontoWaehrung)) {
				belegZahlung.setNKurs(new BigDecimal(1));
			} else {
				WechselkursDto kursDto = getLocaleFac().getKursZuDatum(
						mandantWaehrung, kontoWaehrung, belegZahlung.getDZahldatum(), theClientDto);
				belegZahlung.setNKurs(kursDto.getNKurs());
			}
		}
	}

	@Override
	public Integer createSepakontoauszug(SepakontoauszugDto sepakontoauszugDto,	TheClientDto theClientDto) {
		validateSepakontoauszugDto(sepakontoauszugDto);
		
		LocalDateTime minTime = sepakontoauszugDto.getTAuszug().toLocalDateTime()
				.with(TemporalAdjusters.firstDayOfYear())
				.with(LocalTime.MIN);
		LocalDateTime maxTime = sepakontoauszugDto.getTAuszug().toLocalDateTime()
				.with(TemporalAdjusters.lastDayOfYear())
				.with(LocalTime.MAX);
		Collection<Sepakontoauszug> auszuege = SepakontoauszugQuery.findByBankverbindungIIdIAuszugNotStatusCNrTAuszug(
				em, sepakontoauszugDto.getBankverbindungIId(), sepakontoauszugDto.getIAuszug(),
				SepaImportFac.SepakontoauszugStatus.STORNIERT, 
				Timestamp.valueOf(minTime), Timestamp.valueOf(maxTime));
		if (!auszuege.isEmpty()) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, 
					new Exception("Es existiert bereits ein Kontoauszug mit Auszugsnummer '" 
							+ sepakontoauszugDto.getIAuszug() + "' im Kalenderjahr " + minTime.getYear()));
		}
		
		try {
			Sepakontoauszug sepakontoauszug = new Sepakontoauszug();
			SepakontoauszugDtoAssembler.setEntity(sepakontoauszug, sepakontoauszugDto);
			Timestamp timestamp = getTimestamp();
			sepakontoauszug.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
			sepakontoauszug.setTAnlegen(timestamp);
			sepakontoauszug.setPersonalIIdAendern(theClientDto.getIDPersonal());
			sepakontoauszug.setTAendern(timestamp);
			
			em.persist(sepakontoauszug);
			em.flush();
			
			return sepakontoauszug.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception(e));
		}
	}

	private void validateSepakontoauszugDto(SepakontoauszugDto sepakontoauszugDto) {
		Validator.dtoNotNull(sepakontoauszugDto, "sepakontoauszugDto");
		Validator.notNull(sepakontoauszugDto.getBankverbindungIId(), "bankverbindungIId");
		Validator.notNull(sepakontoauszugDto.getIAuszug(), "iAuszug");
		Validator.notNull(sepakontoauszugDto.getTAuszug(), "tAuszug");
		Validator.notNull(sepakontoauszugDto.getVersion(), "version");
		Validator.notNull(sepakontoauszugDto.getKontoauszug(), "kontoauszug");
		Validator.notNull(sepakontoauszugDto.getCCamtFormat(), "camtFormat");
		Validator.notNull(sepakontoauszugDto.getStatusCNr(), "statusCNr");
	}
	
	@Override
	public void updateSepakontoauszug(SepakontoauszugDto sepakontoauszugDto, TheClientDto theClientDto) {
		validateSepakontoauszugDto(sepakontoauszugDto);

		Sepakontoauszug sepakontoauszug = em.find(Sepakontoauszug.class, sepakontoauszugDto.getIId());
		if (sepakontoauszug == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateSepakontoauszug. Es gibt keinen Sepakontoauszug mit iid " + sepakontoauszugDto.getIId());
		}
		
		SepakontoauszugDtoAssembler.setEntity(sepakontoauszug, sepakontoauszugDto);
		sepakontoauszug.setPersonalIIdAendern(theClientDto.getIDPersonal());
		sepakontoauszug.setTAendern(getTimestamp());

		em.persist(sepakontoauszug);
		em.flush();
	}

	@Override
	public void removeSepakontoauszug(Integer iId) {
		Validator.pkFieldNotNull(iId, "iId");
		
		Sepakontoauszug sepakontoauszug = em.find(Sepakontoauszug.class, iId);
		if (sepakontoauszug == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateSepakontoauszug. Es gibt keinen Sepakontoauszug mit iid " + iId);
		}
		
		em.remove(sepakontoauszug);
		em.flush();
	}

	@Override
	public SepakontoauszugDto sepakontoauszugFindByPrimaryKeySmall(Integer sepakontoauszugId, TheClientDto theClientDto) {
		Sepakontoauszug sepakontoauszug = sepakontoauszugFindByPrimaryKeyImpl(sepakontoauszugId);
		return SepakontoauszugDtoAssembler.createDtoSmall(sepakontoauszug);
	}

	@Override
	public SepakontoauszugDto sepakontoauszugFindByPrimaryKey(Integer sepakontoauszugId, TheClientDto theClientDto) {
		Sepakontoauszug sepakontoauszug = sepakontoauszugFindByPrimaryKeyImpl(sepakontoauszugId);
		return SepakontoauszugDtoAssembler.createDto(sepakontoauszug);
	}

	private Sepakontoauszug sepakontoauszugFindByPrimaryKeyImpl(Integer sepakontoauszugId) {
		Validator.pkFieldNotNull(sepakontoauszugId, "sepakontoauszugId");
		
		Sepakontoauszug sepakontoauszug = em.find(Sepakontoauszug.class, sepakontoauszugId);
		if (sepakontoauszug == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateSepakontoauszug. Es gibt keinen Sepakontoauszug mit iid " + sepakontoauszugId);
		}
		return sepakontoauszug;
	}

	@Override
	public SepakontoauszugDto sepakontoauszugFindByBankverbindungIIdIAuszug(Integer bankverbindungIId, Integer iAuszug, TheClientDto theClientDto) {
		Validator.notNull(bankverbindungIId, "bankverbindungIId");
		Validator.notNull(iAuszug, "iAuszug");
		
		Sepakontoauszug entity = SepakontoauszugQuery.findByBankverbindungIIdIAuszug(
				em, bankverbindungIId, iAuszug);
		return SepakontoauszugDtoAssembler.createDto(entity);
	}

	@Override
	public void storniereSepakontoauszug(Integer sepakontoauszugIId, TheClientDto theClientDto) {
		Sepakontoauszug sepakontoauszug = sepakontoauszugFindByPrimaryKeyImpl(sepakontoauszugIId);
		
		if (sepakontoauszug.getTVerbuchen() != null 
				|| SepaImportFac.SepakontoauszugStatus.ERLEDIGT.equals(sepakontoauszug.getStatusCnr())) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_SEPA_STORNIEREN_BEREITS_VERBUCHT, 
					"FEHLER_SEPA_STORNIEREN_BEREITS_VERBUCHT");
		}
		
		sepakontoauszug.setStatusCnr(SepaImportFac.SepakontoauszugStatus.STORNIERT);
		em.persist(sepakontoauszug);
		em.flush();
	}
	
	public SepakontoauszugDto getSepakontoauszugNiedrigsteAuszugsnummer(Integer bankverbindungIId, TheClientDto theClientDto) {
		Validator.notNull(bankverbindungIId, "bankverbindungIId");
		List<String> stati = new ArrayList<String>();
		stati.add(SepaImportFac.SepakontoauszugStatus.ANGELEGT);
		try {
			Sepakontoauszug entity = SepakontoauszugQuery.findByBankverbindungIIdStatusCNrMinIAuszug(em, bankverbindungIId, stati);
			return SepakontoauszugDtoAssembler.createDto(entity);
		} catch (NoResultException ex) {
			throw EJBExcFactory.keinOffenerSepakontoauszug();
		} 
	}
	
	@Override
	public void pruefeSepakontoauszugAufVerbuchung(Integer sepakontoauszugIId, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		Sepakontoauszug entity = sepakontoauszugFindByPrimaryKeyImpl(sepakontoauszugIId);
		
		if (!SepakontoauszugStatus.ANGELEGT.equals(entity.getStatusCnr())) {
			throw EJBExcFactory.sepaVerbuchungAuszugFalscherStatus(SepakontoauszugDtoAssembler.createDtoSmall(entity), 
					SepakontoauszugStatus.ANGELEGT);
		}
		
		// Kontoauszug im aktuellen GJ?
		if (getMandantFac().hatModulFinanzbuchhaltung(theClientDto)) {
			Integer gjKontoauszug = getBuchenFac().findGeschaeftsjahrFuerDatum(
					new Date(entity.getTAuszug().getTime()), theClientDto.getMandant());
			if (gjKontoauszug == null || !gjKontoauszug.equals(theClientDto.getGeschaeftsJahr())) {
				throw EJBExcFactory.sepakontoauszugNichtImAktuellenGeschaeftsjahr(
						SepakontoauszugDtoAssembler.createDtoSmall(entity), theClientDto.getGeschaeftsJahr());
			}
			// GJ gesperrt?
			getSystemFac().pruefeGeschaeftsjahrSperre(gjKontoauszug, theClientDto.getMandant());

			// Existieren bereits Buchungen dieses Auszugs?
			BankverbindungDto bankverbindungDto = getFinanzFac().bankverbindungFindByPrimaryKey(entity.getBankverbindungIId());
			if (getBuchenFac().existsBuchungenMitAuszugsNr(bankverbindungDto.getKontoIId(), 
					entity.getIAuszug(), new Date(entity.getTAuszug().getTime()), theClientDto)) {
				throw EJBExcFactory.sepaVerbuchungBuchungenMitAuszugsnummerExistieren(SepakontoauszugDtoAssembler.createDtoSmall(entity));
			}
		} else {
			pruefeZahlungenFuerAuszug(entity, theClientDto);
		}

		
	}
	
	private void pruefeZahlungenFuerAuszug(Sepakontoauszug entity, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		List<Rechnungzahlung> reZahlungen = RechnungzahlungQuery.byIAuszug(em, entity.getIAuszug());
		List<Eingangsrechnungzahlung> erZahlungen = EingangsrechnungzahlungQuery.byIAuszug(em, entity.getIAuszug());
		
		if (reZahlungen.isEmpty() && erZahlungen.isEmpty()) {
			return;
		}
		
		List<RechnungDto> rechnungen = new ArrayList<RechnungDto>();
		List<EingangsrechnungDto> eingangsrechnungen = new ArrayList<EingangsrechnungDto>();
		if (!reZahlungen.isEmpty()) {
			for (Rechnungzahlung zahlung : reZahlungen) {
				rechnungen.add(getRechnungFac().rechnungFindByPrimaryKey(zahlung.getRechnungIId()));
			}
			
			for (Eingangsrechnungzahlung zahlung : erZahlungen) {
				eingangsrechnungen.add(getEingangsrechnungFac().eingangsrechnungFindByPrimaryKey(zahlung.getEingangsrechnungIId()));
			}
		}
		
		throw EJBExcFactory.sepaVerbuchungZahlungenMitAuszugsnummerExistieren(
				SepakontoauszugDtoAssembler.createDtoSmall(entity),
				rechnungen, eingangsrechnungen);
	}

	private List<String> getErlaubteArtenAusgangsrechnung() throws EJBExceptionLP, RemoteException {
		RechnungartDto[] rechnungarten = getRechnungServiceFac().rechnungartFindByRechnungtyp(RechnungFac.RECHNUNGTYP_RECHNUNG);
		List<String> result = new ArrayList<String>();
		for (RechnungartDto art : rechnungarten) {
			result.add(art.getCNr());
		}
		return result;
	}

	private List<String> getErlaubteArtenEingangsrechnung() throws EJBExceptionLP, RemoteException {
		EingangsrechnungartDto[] erArten = getEingangsrechnungServiceFac().eingangsrechnungartFindAll();
		List<String> result = new ArrayList<String>();
		for (EingangsrechnungartDto art : erArten) {
			result.add(art.getCNr());
		}
		return result;
	}
	
	private boolean isWaehrung(String waehrungCnr, String compared) {
		return waehrungCnr != null && compared != null
				&& waehrungCnr.trim().equals(compared.trim());
	}
	
	@Override
	public Iso20022PaymentsDto getIso20022PaymentsByStandard(Iso20022StandardEnum standard) {
		return getIso20022PaymentsImpl(standard);
	}
	
	@Override
	public Map<Iso20022StandardEnum, Iso20022PaymentsDto> getMapOfIso20022Payments() {
		List<Iso20022Standard> standards = Iso20022Query.standardListAll(em);
		Map<Iso20022StandardEnum, Iso20022PaymentsDto> payments = new HashMap<Iso20022StandardEnum, Iso20022PaymentsDto>();
		
		for (Iso20022Standard std : standards) {
			Iso20022StandardEnum stdEnum = Iso20022StandardEnum.lookup(std.getCNr());
			payments.put(stdEnum, getIso20022PaymentsImpl(stdEnum));
		}
		
		return payments;
	}
	
	private Iso20022PaymentsDto getIso20022PaymentsImpl(Iso20022StandardEnum standard) {
		Iso20022PaymentsDto payments = new Iso20022PaymentsDto();

		List<Iso20022ZahlungsauftragSchema> zaList = Iso20022Query.zahlungsauftragSchemasListByStandard(em, standard);
		for (Iso20022ZahlungsauftragSchema zaSchema : zaList) {
			payments.getZahlungsauftragDtos().add(Iso20022NachrichtDtoAssembler.createDto(zaSchema));
		}
		
		List<Iso20022LastschriftSchema> laList = Iso20022Query.lastschriftSchemasListByStandard(em, standard);
		for (Iso20022LastschriftSchema laSchema : laList) {
			payments.getLastschriftDtos().add(Iso20022NachrichtDtoAssembler.createDto(laSchema));
		}

		Iso20022Standard standardEntity = Iso20022Query.standardFindByStandardEnumNoExc(em, standard);
		payments.setStandardDto(Iso20022StandardDtoAssembler.createDto(standardEntity));
		
		return payments;
	}
	
	@Override
	public List<Iso20022StandardDto> iso20022standardsFindAll() {
		List<Iso20022Standard> stdList = Iso20022Query.standardListAll(em);
		return Iso20022StandardDtoAssembler.createDtos(stdList);
	}
	
	@Override
	public Iso20022BankverbindungDto iso20022BankverbindungFindByBankverbindungIIdNoExc(BankverbindungId bankverbindungId) {
		Iso20022Bankverbindung isoBv = Iso20022Query.bankverbindungFindByBankverbindungIIdNoEx(em, bankverbindungId);
		return isoBv != null ? Iso20022BankverbindungDtoAssembler.createDto(isoBv) : null;
	}
	
	@Override
	public Integer createIso20022Bankverbindung(Iso20022BankverbindungDto iso20022BankverbindungDto) {
		Validator.dtoNotNull(iso20022BankverbindungDto, "iso20022BankverbindungDto");
		Validator.notNull(iso20022BankverbindungDto.getBankverbindungIId(), "bankverbindungIId");
		
		Iso20022Bankverbindung isoBv = Iso20022Query.bankverbindungFindByBankverbindungIIdNoEx(em, 
				new BankverbindungId(iso20022BankverbindungDto.getBankverbindungIId()));
		if (isoBv != null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, 
					new Exception("Es existiert bereits ein Eintrag fuer BankverbindungIId = "
							+ iso20022BankverbindungDto.getBankverbindungIId() + " in Iso20022Bankverbindung."));
		}
		
		try {
			Iso20022Bankverbindung entity = new Iso20022Bankverbindung();
			Iso20022BankverbindungDtoAssembler.setEntity(entity, iso20022BankverbindungDto);
			em.persist(entity);
			em.flush();
			return entity.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception(e));
		}
	}
	
	@Override
	public void updateIso20022Bankverbindung(Iso20022BankverbindungDto iso20022BankverbindungDto) {
		Validator.dtoNotNull(iso20022BankverbindungDto, "iso20022BankverbindungDto");
		Validator.pkFieldNotNull(iso20022BankverbindungDto.getIId(), "iso20022BankverbindungDto.getIId()");
		Validator.notNull(iso20022BankverbindungDto.getBankverbindungIId(), "bankverbindungIId");
		
		Iso20022Bankverbindung entity = em.find(Iso20022Bankverbindung.class, iso20022BankverbindungDto.getIId());
		if (entity == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateIso20022Bankverbindung. Es gibt keine Iso20022Bankverbindung mit iid " 
					+ iso20022BankverbindungDto.getIId());
		}
		
		Iso20022BankverbindungDtoAssembler.setEntity(entity, iso20022BankverbindungDto);
		em.persist(entity);
		em.flush();
	}
	
	@Override
	public void removeIso20022Bankverbindung(Integer iId) {
		Validator.pkFieldNotNull(iId, "iId");
		
		Iso20022Bankverbindung entity = em.find(Iso20022Bankverbindung.class, iId);
		if (entity == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateIso20022Bankverbindung. Es gibt keine Iso20022Bankverbindung mit iid " 
					+ iId);
		}
		
		em.remove(entity);
		em.flush();
	}
}
