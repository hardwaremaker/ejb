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
package com.lp.server.finanz.bl;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungKontierungDto;
import com.lp.server.finanz.service.FibuExportKriterienDto;
import com.lp.server.finanz.service.FibuexportDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.ReportErloeskontoDto;
import com.lp.server.finanz.service.ReversechargeartDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungkontierungDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.ZahlungszielDto;
import com.lp.server.util.Validator;
import com.lp.service.BelegpositionVerkaufDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * Diese Klasse kuemmert sich um den FibuExport nach Kostenstellen
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 26.01.05
 * </p>
 * 
 * <p>
 * 
 * @author $Author: adi $
 *         </p>
 * 
 * @version not attributable Date $Date: 2012/06/27 11:27:52 $
 */
class FibuExportManagerNachKostenstellen extends FibuExportManager {
	/**
	 * FibuExportManagerNachKostenstellen
	 * 
	 * @param exportKriterienDto
	 *            FibuExportKriterienDto
	 * @param idUser
	 *            String
	 */
	FibuExportManagerNachKostenstellen(
			FibuExportKriterienDto exportKriterienDto, TheClientDto theClientDto) {
		super(exportKriterienDto, theClientDto);
	}

	public FibuexportDto[] getExportdatenEingangsrechnung(
			Integer eingangsrechnungIId, Date dStichtag) throws EJBExceptionLP {
		myLogger.logData(eingangsrechnungIId, theClientDto.getIDUser());
		FibuexportDto[] exportDtos = null;
		try {
			int iZeilen = 1; // buchung auf kreditorenkonto
			int iAnzahlGegenkonten;
			EingangsrechnungDto erDto = getEingangsrechnungFac()
					.eingangsrechnungFindByPrimaryKey(eingangsrechnungIId);

			// PJ19169
			if (Helper.short2boolean(erDto.getBMitpositionen()) == true
					&& erDto.getTGedruckt() == null) {

				List<Object> al = new ArrayList<Object>();
				al.add(erDto.getCNr());

				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_ER_MIT_POSITIONEN_NOCH_NICHT_GEDRUCKT,
						al, new Exception(
								"ER mit Positionen noch nicht aktiviert/gedruckt. "
										+ erDto.getCNr()));
			}

			// Nur Belege mit Wert ungleich 0 exportieren
			if (erDto.getNBetrag().compareTo(new BigDecimal(0)) != 0) {
				// Pruefen und Laden der eventuell vorhandenen
				// Mehrfachkontierung
				EingangsrechnungKontierungDto[] erKontoDto = 
						pruefeEingangsrechnungKontierung(erDto);
				// pruefen, ob die Mehrfachkontierungen den gleichen MWST-Satz
				// haben
				boolean bUSTSatzGleich = true;
				if (erKontoDto != null) {
					for (int i = 0; i < erKontoDto.length - 1; i++) {
						if (!erKontoDto[i].getMwstsatzIId().equals(
								erKontoDto[i + 1].getMwstsatzIId())) {
							bUSTSatzGleich = false;
							break;
						}
					}
				}
				// Lieferant holen und Daten pruefen
				LieferantDto lieferantDto = super
						.pruefeEingangsrechnungLieferant(erDto);
				// Holen des Lieferantenkontos
				KontoDto kreditorenkontoDto = getFinanzFac()
						.kontoFindByPrimaryKey(
								lieferantDto.getKontoIIdKreditorenkonto());
				// Einfache Kontierung?
				if (erDto.getKostenstelleIId() != null) {
					iAnzahlGegenkonten = 1;
				}
				// sonst Mehrfachkontierung
				else {
					iAnzahlGegenkonten = erKontoDto.length;
				}
				// Anzahl der Buchungszeilen
				iZeilen = 1 + iAnzahlGegenkonten;
				// jetzt wird das Daten-Array zusammengebaut
				boolean isReversecharge = 
						isEingangsrechnungDtoReversecharge(erDto);
				exportDtos = new FibuexportDto[iZeilen];
				for (int i = 0; i < exportDtos.length; i++) {
					exportDtos[i] = new FibuexportDto();
					exportDtos[i]
							.setIGeschaeftsjahr(erDto.getIGeschaeftsjahr());
					if(1 == iAnzahlGegenkonten) {
						exportDtos[i]
								.setReversechargeartId(erDto.getReversechargeartId());
						exportDtos[i]
									.setBReverseCharge(isReversecharge);						
					}
				}
				// die Buchung auf Kreditorenkonto ist immer die erste.
				int iZeilePersonenkonto = 0;

				// Die Buchungszeile fuers Kreditorenkonto
				exportDtos[iZeilePersonenkonto].setKonto(kreditorenkontoDto);
				// Gegenkonto / Kostenstelle / MWST-Satz
				if (iAnzahlGegenkonten > 1) {
					// wenn es mehrere Gegenkonten gibt
					exportDtos[iZeilePersonenkonto].setGkto(null);
					// wenn alle Mehrfachkontierungen den gleichen UST-Satz
					// haben,
					// dann kommt auch hier der MWST-Satz rein
					if (bUSTSatzGleich) {
						MwstsatzDto mwstDto = getMwstsatz(erKontoDto[0]
								.getMwstsatzIId());
						exportDtos[iZeilePersonenkonto].setMwstsatz(mwstDto);
					}
				} else {
					// nur ein Gegenkonto, aber 2 Moeglichkeiten:
					// 1. Das Gegenkonto steht in den Kopfdaten
					if (erKontoDto == null) {
						KontoDto kontoDtoGegenkonto = getFinanzFac()
								.kontoFindByPrimaryKey(erDto.getKontoIId());
						exportDtos[iZeilePersonenkonto]
								.setGkto(kontoDtoGegenkonto);
						// Kostenstelle
						Integer kstIId = erDto.getKostenstelleIId();
						KostenstelleDto kstDto = getSystemFac()
								.kostenstelleFindByPrimaryKey(kstIId);
						exportDtos[iZeilePersonenkonto].setKost(kstDto);
						// MWST-Satz
						Integer mwstIId = erDto.getMwstsatzIId();
						MwstsatzDto mwstDto = getMwstsatz(mwstIId);
						exportDtos[iZeilePersonenkonto].setMwstsatz(mwstDto);
						// MWST-Betrag
						exportDtos[iZeilePersonenkonto].setSteuer(erDto
								.getNUstBetrag());
						exportDtos[iZeilePersonenkonto].setSteuerFW(erDto
								.getNUstBetragfw());
					}
					// 2. Es gibt nur einen Mehrfachkontierungseintrag mit dem
					// ganzen ER-Wert
					else {
						KontoDto kontoDtoGegenkonto = getFinanzFac()
								.kontoFindByPrimaryKey(
										erKontoDto[0].getKontoIId());
						exportDtos[iZeilePersonenkonto]
								.setGkto(kontoDtoGegenkonto);
						/**
						 * @todo unterscheiden: alle mit gleicher UST oder mit
						 *       verschiedener PJ 4270
						 */
						exportDtos[iZeilePersonenkonto]
								.setSteuer(new BigDecimal(0));
					}
				}
				exportDtos[iZeilePersonenkonto].setOPNummer(erDto
						.getCLieferantenrechnungsnummer());
				exportDtos[iZeilePersonenkonto].setBelegnr(erDto.getCNr());
				exportDtos[iZeilePersonenkonto].setBelegdatum(erDto
						.getDBelegdatum());
				// PJ 17006
				// exportDtos[iZeilePersonenkonto].setValutadatum(erDto.getDFreigabedatum());
				exportDtos[iZeilePersonenkonto].setValutadatum(dStichtag);

				exportDtos[iZeilePersonenkonto].setWaehrung(theClientDto
						.getSMandantenwaehrung());
				// Sollbetrag ist null
				exportDtos[iZeilePersonenkonto].setSollbetrag(null);
				exportDtos[iZeilePersonenkonto].setSollbetragFW(null);
				// Bruttobetrag im Haben
				exportDtos[iZeilePersonenkonto].setHabenbetrag(erDto
						.getNBetrag());
				exportDtos[iZeilePersonenkonto].setHabenbetragFW(erDto
						.getNBetragfw());
				// Steuer ist null
				exportDtos[iZeilePersonenkonto].setSteuer(null);
				// MWST-Betrag
				exportDtos[iZeilePersonenkonto].setFremdwaehrung(erDto
						.getWaehrungCNr());
				exportDtos[iZeilePersonenkonto].setBelegart(BELEGART_ER);
				exportDtos[iZeilePersonenkonto].setPartnerDto(lieferantDto
						.getPartnerDto());
				String laenderartCNr = super
						.getLaenderartZuPartner(
								lieferantDto.getPartnerDto(),
								Helper.asTimestamp(erDto.getDBelegdatum()));
				exportDtos[iZeilePersonenkonto].setLaenderartCNr(laenderartCNr);

				ZahlungszielDto zzDto = getZahlungsziel(erDto
						.getZahlungszielIId());
				exportDtos[iZeilePersonenkonto].setZahlungszielDto(zzDto);
				exportDtos[iZeilePersonenkonto].setText(erDto.getCText());
				exportDtos[iZeilePersonenkonto].setSExterneBelegnummer(erDto
						.getCLieferantenrechnungsnummer());
				exportDtos[iZeilePersonenkonto].setUidNummer(lieferantDto
						.getPartnerDto().getCUid());
				Date tFaelligkeit = Helper.addiereTageZuDatum(erDto.getDBelegdatum(), zzDto.getAnzahlZieltageFuerNetto());
				exportDtos[iZeilePersonenkonto].setFaelligkeitsdatum(tFaelligkeit);
				// Gegenbuchung, wenn keine Mehrfachkontierung
				if (erKontoDto == null) {
					final int iZeileGegenbuchung = 1;

					// Kst nicht pruefen, da nur das konto zaehlt
					KostenstelleDto kstDtoHW = getSystemFac()
							.kostenstelleFindByPrimaryKey(
									erDto.getKostenstelleIId());
					KontoDto kontoDtoGegenkonto = getFinanzFac()
							.kontoFindByPrimaryKey(erDto.getKontoIId());
					// buchung aufs HW-Konto
					exportDtos[iZeileGegenbuchung].setKonto(kontoDtoGegenkonto);
					exportDtos[iZeileGegenbuchung].setGkto(kreditorenkontoDto);
					exportDtos[iZeileGegenbuchung].setOPNummer(erDto
							.getCLieferantenrechnungsnummer());
					exportDtos[iZeileGegenbuchung].setBelegnr(erDto.getCNr());
					exportDtos[iZeileGegenbuchung].setBelegdatum(erDto
							.getDBelegdatum());

					// PJ 17006
					// exportDtos[iZeileGegenbuchung].setValutadatum(erDto.getDFreigabedatum());
					exportDtos[iZeileGegenbuchung].setValutadatum(dStichtag);

					exportDtos[iZeileGegenbuchung].setWaehrung(theClientDto
							.getSMandantenwaehrung());
					// Sollbetrag ist brutto
					exportDtos[iZeileGegenbuchung].setSollbetrag(erDto
							.getNBetrag().subtract(erDto.getNUstBetrag()));
					exportDtos[iZeileGegenbuchung].setSollbetragFW(erDto
							.getNBetragfw().subtract(erDto.getNUstBetragfw()));
					// Habenbetrag ist null
					exportDtos[iZeileGegenbuchung].setHabenbetrag(null);
					exportDtos[iZeileGegenbuchung].setHabenbetragFW(null);
					// Steuerbetrag ist hier
					exportDtos[iZeileGegenbuchung].setSteuer(erDto
							.getNUstBetrag());
					exportDtos[iZeileGegenbuchung].setFremdwaehrung(erDto
							.getWaehrungCNr());
					exportDtos[iZeileGegenbuchung].setKost(kstDtoHW);
					exportDtos[iZeileGegenbuchung].setBelegart(BELEGART_ER);
					exportDtos[iZeileGegenbuchung].setPartnerDto(lieferantDto
							.getPartnerDto());
					// mwst-satz
					Integer mwstIId = erDto.getMwstsatzIId();
					MwstsatzDto mwstDto = getMwstsatz(mwstIId);
					exportDtos[iZeileGegenbuchung].setMwstsatz(mwstDto);
					exportDtos[iZeileGegenbuchung]
							.setLaenderartCNr(laenderartCNr);
					exportDtos[iZeileGegenbuchung].setZahlungszielDto(zzDto);
					exportDtos[iZeileGegenbuchung].setText(erDto.getCText());
					exportDtos[iZeileGegenbuchung].setSExterneBelegnummer(erDto
							.getCLieferantenrechnungsnummer());
					exportDtos[iZeileGegenbuchung].setUidNummer(lieferantDto
							.getPartnerDto().getCUid());
					exportDtos[iZeileGegenbuchung].setFaelligkeitsdatum(tFaelligkeit);
				} else {
					ReversechargeartDto rcartOhneDto = getFinanzServiceFac()
							.reversechargeartFindOhne(theClientDto);
					// Mehrfachkontierung
					// Kst nicht pruefen, da nur das konto zaehlt
					for (int i = 0; i < erKontoDto.length; i++) {
						final int iAktuelleZeileGegenbuchung = i + 1; // in der
						// ersten
						// steht
						// die
						// sammelbuchung
						KostenstelleDto kstDtoHW = getSystemFac()
								.kostenstelleFindByPrimaryKey(
										erKontoDto[i].getKostenstelleIId());
						KontoDto kontoDtoGegenkonto = getFinanzFac()
								.kontoFindByPrimaryKey(
										erKontoDto[i].getKontoIId());

						exportDtos[iAktuelleZeileGegenbuchung]
								.setCKommentar(erKontoDto[i].getCKommentar());

						// buchung aufs HW-Konto
						exportDtos[iAktuelleZeileGegenbuchung]
								.setKonto(kontoDtoGegenkonto);
						exportDtos[iAktuelleZeileGegenbuchung]
								.setGkto(kreditorenkontoDto);
						exportDtos[iAktuelleZeileGegenbuchung]
								.setOPNummer(erDto
										.getCLieferantenrechnungsnummer());
						exportDtos[iAktuelleZeileGegenbuchung].setBelegnr(erDto
								.getCNr());
						exportDtos[iAktuelleZeileGegenbuchung]
								.setBelegdatum(erDto.getDBelegdatum());

						// PJ 17006
						// exportDtos[iAktuelleZeileGegenbuchung].setValutadatum(erDto.getDFreigabedatum());
						exportDtos[iAktuelleZeileGegenbuchung]
								.setValutadatum(dStichtag);

						exportDtos[iAktuelleZeileGegenbuchung]
								.setWaehrung(theClientDto
										.getSMandantenwaehrung());
						// Sollbetrag ist netto
						BigDecimal bdNettobetrag = erKontoDto[i].getNBetrag()
								.subtract(erKontoDto[i].getNBetragUst());
						BigDecimal bdNettobetragFW = bdNettobetrag.divide(
								erDto.getNKurs(), FinanzFac.NACHKOMMASTELLEN,
								BigDecimal.ROUND_HALF_EVEN);
						exportDtos[iAktuelleZeileGegenbuchung]
								.setSollbetrag(bdNettobetrag);
						exportDtos[iAktuelleZeileGegenbuchung]
								.setSollbetragFW(bdNettobetragFW);
						// Habenbetrag ist null
						exportDtos[iAktuelleZeileGegenbuchung]
								.setHabenbetrag(null);
						exportDtos[iAktuelleZeileGegenbuchung]
								.setHabenbetragFW(null);
						// Steuerbetrag ist der UST-Betrag
						exportDtos[iAktuelleZeileGegenbuchung]
								.setSteuer(erKontoDto[i].getNBetragUst());
						// Fremdwaehrung
						exportDtos[iAktuelleZeileGegenbuchung]
								.setFremdwaehrung(erDto.getWaehrungCNr());
						exportDtos[iAktuelleZeileGegenbuchung]
								.setKost(kstDtoHW);
						exportDtos[iAktuelleZeileGegenbuchung]
								.setBelegart(BELEGART_ER);
						exportDtos[iAktuelleZeileGegenbuchung]
								.setPartnerDto(lieferantDto.getPartnerDto());
						// mwst-satz
						Integer mwstIId = erKontoDto[i].getMwstsatzIId();
						MwstsatzDto mwstDto = getMwstsatz(mwstIId);
						exportDtos[iAktuelleZeileGegenbuchung]
								.setMwstsatz(mwstDto);
						exportDtos[iAktuelleZeileGegenbuchung]
								.setLaenderartCNr(laenderartCNr);
						exportDtos[iAktuelleZeileGegenbuchung]
								.setZahlungszielDto(zzDto);
						exportDtos[iAktuelleZeileGegenbuchung].setText(erDto
								.getCText());
						exportDtos[iAktuelleZeileGegenbuchung]
								.setSExterneBelegnummer(erDto
										.getCLieferantenrechnungsnummer());
						exportDtos[iAktuelleZeileGegenbuchung]
								.setUidNummer(lieferantDto.getPartnerDto()
										.getCUid());
						exportDtos[iAktuelleZeileGegenbuchung]
								.setReversechargeartId(erKontoDto[i].getReversechargeartId());
						exportDtos[iAktuelleZeileGegenbuchung]
								.setBReverseCharge(!rcartOhneDto.getIId()
										.equals(erKontoDto[i].getReversechargeartId()));
						exportDtos[iAktuelleZeileGegenbuchung].setFaelligkeitsdatum(tFaelligkeit);
					}
				}
			} else {
				// Beleg mit Wert 0
				exportDtos = new FibuexportDto[0];
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return exportDtos;
	}

	private MwstsatzDto findMwstSatzZuProzentsatz(BigDecimal mwstsatz, Timestamp belegdatum) {
		MwstsatzDto[] allMwst = getMandantFac().mwstsatzfindAllByMandant(
				theClientDto.getMandant(), belegdatum, false);
		Double dMwstsatz = mwstsatz.doubleValue() ;
		for (MwstsatzDto mwstsatzDto : allMwst) {
			if(dMwstsatz.equals(mwstsatzDto.getFMwstsatz())) {
				return mwstsatzDto ;
			}
		}
		
		return null ;
	}
	
	public FibuexportDto[] getExportdatenRechnung(Integer rechnungIId,
			Date dStichtag) throws EJBExceptionLP {
		myLogger.logData(rechnungIId, theClientDto.getIDUser());
		FibuexportDto[] exportDtos = null;
		try {
			int iZeilen = 1; // buchung auf debitorenkonto
			int iAnzahlGegenkonten;
			RechnungDto reDto = getRechnungFac().rechnungFindByPrimaryKey(
					rechnungIId);
			myLogger.logData("exportiere Rechnung " + reDto.getCNr(),
					theClientDto.getIDUser());
			// UST-Satz anhand Kopfdaten (muss kein Standard-%-Satz sein)
			// Nur Belege mit Wert ungleich 0 exportieren
			if (reDto.getNWert() != null
					&& reDto.getNWert().compareTo(new BigDecimal(0)) != 0) {
				MwstsatzDto mwstSatz = new MwstsatzDto();
				BigDecimal bdMwstsatz = reDto.getNWertustfw()
						.divide(reDto.getNWertfw(), BigDecimal.ROUND_HALF_EVEN)
						.multiply(new BigDecimal(100));
//				mwstSatz.setFMwstsatz(bdMwstsatz.doubleValue());
				mwstSatz = findMwstSatzZuProzentsatz(bdMwstsatz, reDto.getTBelegdatum()) ;
				
				// Pruefen und Laden der eventuell vorhandenen
				// Mehrfachkontierung
				RechnungkontierungDto[] reKontierungDto = super
						.pruefeRechnungKontierung(reDto);
				// Lieferant holen und Daten pruefen
				KundeDto kundeDto = super.pruefeRechnungKunde(reDto);
				// Laenderart des Kunden
				String sLaenderart = getLaenderartZuPartner(kundeDto
							.getPartnerDto(), reDto.getTBelegdatum());					
				Integer iLand = kundeDto.getPartnerDto().getLandplzortDto()
						.getIlandID();
				// Holen des Kundenkontos
				KontoDto debitorenkontoDto = getFinanzFac()
						.kontoFindByPrimaryKey(kundeDto.getIidDebitorenkonto());
				// Einfache Kontierung?
				if (reKontierungDto == null || reKontierungDto.length == 0) {
					iAnzahlGegenkonten = 1;
				}
				// sonst Mehrfachkontierung
				else {
					iAnzahlGegenkonten = reKontierungDto.length;
				}
				// Anzahl der Buchungszeilen
				iZeilen = 1 + iAnzahlGegenkonten;

				boolean isArReversecharge = isRechnungDtoReversecharge(reDto);
				// jetzt wird das Daten-Array zusammengebaut
				exportDtos = new FibuexportDto[iZeilen];
				for (int i = 0; i < exportDtos.length; i++) {
					exportDtos[i] = new FibuexportDto();
					exportDtos[i].setMwstsatz(mwstSatz);
					exportDtos[i]
							.setIGeschaeftsjahr(reDto.getIGeschaeftsjahr());
					exportDtos[i]
							.setReversechargeartId(reDto.getReversechargeartId());
					exportDtos[i]
							.setBReverseCharge(isArReversecharge);
				}
				// Die Buchungszeile fuers Debitorenkonto
				exportDtos[0].setKonto(debitorenkontoDto);
				// Gegenkonto / Kostenstelle / MWST-Satz
				if (iAnzahlGegenkonten > 1) {
					// wenn es mehrere Gegenkonten gibt
					exportDtos[0].setGkto(null);
				} else {
					// nur ein Gegenkonto, aber 2 Moeglichkeiten:
					// 1. Das Gegenkonto steht in den Kopfdaten
					if (reKontierungDto == null) {
						// Kostenstelle
						Integer kstIId = reDto.getKostenstelleIId();
						// Die Kostenstelle muss auch ein Sachkonto haben
						KostenstelleDto kstDto = pruefeKostenstelle(
								kstIId, reDto);
						exportDtos[0].setKost(kstDto);
						KontoDto kontoDtoGegenkontoUebersetzt = uebersetzeKontoNachLandBzwLaenderart(
								kstDto.getKontoIId(), sLaenderart,
								debitorenkontoDto.getFinanzamtIId(),
								debitorenkontoDto.getMandantCNr(), reDto, 
								iLand, reDto.getReversechargeartId(),
								reDto.getTBelegdatum());

						exportDtos[0].setGkto(kontoDtoGegenkontoUebersetzt);
						// MWST-Betrag
						exportDtos[0].setSteuer(null);
					}
					// 2. Es gibt nur einen Mehrfachkontierungseintrag mit dem
					// ganzen Beleg-Wert
					else {
						// Dann zieht das Sachkonto aus der Kostenstelle
						KostenstelleDto kstDto = pruefeKostenstelle(
								reKontierungDto[0].getKostenstelleIId(), reDto);
						KontoDto kontoDtoGegenkonto = uebersetzeKontoNachLandBzwLaenderart(
								kstDto.getKontoIId(), sLaenderart,
								debitorenkontoDto.getFinanzamtIId(),
								debitorenkontoDto.getMandantCNr(), reDto,
								iLand, reDto.getReversechargeartId(),
								reDto.getTBelegdatum());
						exportDtos[0].setGkto(kontoDtoGegenkonto);
						exportDtos[0].setSteuer(new BigDecimal(0));
					}
				}
				// die OP-Nummer ist die Belegnummer, aber ohne GJ und
				// Trennzeichen
				exportDtos[0].setOPNummer(extractLaufendeNummerAusBelegnummer(
						reDto.getCNr(), theClientDto));
				exportDtos[0].setBelegnr(reDto.getCNr());
				exportDtos[0].setBelegdatum(reDto.getTBelegdatum());
				exportDtos[0].setValutadatum(reDto.getTBelegdatum());
				exportDtos[0].setWaehrung(theClientDto.getSMandantenwaehrung());
				// Haben ist 0
				exportDtos[0].setHabenbetrag(null);
				exportDtos[0].setHabenbetragFW(null);
				// Bruttobetrag im Soll
				exportDtos[0].setSollbetrag(reDto.getNWert().add(
						reDto.getNWertust()));
				exportDtos[0].setSollbetragFW(reDto.getNWertfw().add(
						reDto.getNWertustfw()));
				// MWST-Betrag
				exportDtos[0].setFremdwaehrung(reDto.getWaehrungCNr());
				exportDtos[0].setBelegart(BELEGART_AR);
				exportDtos[0].setPartnerDto(kundeDto.getPartnerDto());
				exportDtos[0].setLaenderartCNr(sLaenderart);

				ZahlungszielDto zzDto = getZahlungsziel(reDto
						.getZahlungszielIId());
				exportDtos[0].setZahlungszielDto(zzDto);
				exportDtos[0].setText(kundeDto.getPartnerDto()
						.formatFixTitelName1Name2());
				exportDtos[0].setUidNummer(kundeDto.getPartnerDto().getCUid());
				Date tFaelligkeit = Helper.addiereTageZuDatum(reDto.getTBelegdatum(), 
						zzDto.getAnzahlZieltageFuerNetto());
				exportDtos[0].setFaelligkeitsdatum(tFaelligkeit);
				// Gegenbuchung, wenn keine Mehrfachkontierung
				if (reKontierungDto == null) {
					// Kostenstelle pruefen
					KostenstelleDto kstDtoHW = pruefeKostenstelle(
							reDto.getKostenstelleIId(), reDto);
					KontoDto kontoDtoGegenkontoUebersetzt = uebersetzeKontoNachLandBzwLaenderart(
							kstDtoHW.getKontoIId(), sLaenderart,
							debitorenkontoDto.getFinanzamtIId(),
							debitorenkontoDto.getMandantCNr(), reDto, 
							iLand, reDto.getReversechargeartId(),
							reDto.getTBelegdatum());
					// buchung aufs HW-Konto
					exportDtos[1].setKonto(kontoDtoGegenkontoUebersetzt);
					exportDtos[1].setGkto(debitorenkontoDto);
					exportDtos[1].setOPNummer("0"); // wie am RZL-Beispiel
					exportDtos[1].setBelegnr(reDto.getCNr());
					exportDtos[1].setBelegdatum(reDto.getTBelegdatum());
					exportDtos[1].setValutadatum(reDto.getTBelegdatum());
					exportDtos[1].setWaehrung(theClientDto
							.getSMandantenwaehrung());
					// Sollbetrag ist null
					exportDtos[1].setSollbetrag(null);
					exportDtos[1].setSollbetragFW(null);
					// Habenbetrag ist brutto
					exportDtos[1].setHabenbetrag(reDto.getNWert());
					exportDtos[1].setHabenbetragFW(reDto.getNWertfw());
					// Steuerbetrag ist null
					exportDtos[1].setSteuer(reDto.getNWertust());
					exportDtos[1].setSteuerFW(reDto.getNWertustfw());
					exportDtos[1].setFremdwaehrung(reDto.getWaehrungCNr());
					exportDtos[1].setKost(kstDtoHW);
					exportDtos[1].setBelegart(BELEGART_AR);
					exportDtos[1].setPartnerDto(kundeDto.getPartnerDto());
					// // mwst-satz
					// Integer mwstIId = reDto.getMwstsatzIId();
					// MwstsatzDto mwstDto = getMwstsatz(mwstIId);
					// exportDtos[1].setMwstsatz(mwstDto);
					exportDtos[1].setLaenderartCNr(sLaenderart);
					exportDtos[1].setZahlungszielDto(zzDto);
					exportDtos[1].setText(kundeDto.getPartnerDto()
							.formatFixTitelName1Name2());
					exportDtos[1].setUidNummer(kundeDto.getPartnerDto()
							.getCUid());
					exportDtos[1].setFaelligkeitsdatum(tFaelligkeit);
				} else {
					// Cent-Differenzen muessen vermieden werden
					BigDecimal bdRestNetto = reDto.getNWert();
					BigDecimal bdRestNettoFW = reDto.getNWertfw();
					BigDecimal bdRestUST = reDto.getNWertust();
					BigDecimal bdGrNettowert = new BigDecimal(0);
					int iIndexDesGrNettowerts = 1;

					// Mehrfachkontierung
					for (int i = 0; i < reKontierungDto.length; i++) {
						// Pruefen der Kostenstelle
						KostenstelleDto kstDtoHW = pruefeKostenstelle(
								reKontierungDto[i].getKostenstelleIId(), reDto);
						KontoDto kontoDtoGegenkonto = uebersetzeKontoNachLandBzwLaenderart(
								kstDtoHW.getKontoIId(), sLaenderart,
								debitorenkontoDto.getFinanzamtIId(),
								debitorenkontoDto.getMandantCNr(), reDto, iLand,
								reDto.getReversechargeartId(), reDto.getTBelegdatum());
						// buchung aufs HW-Konto
						exportDtos[1 + i].setKonto(kontoDtoGegenkonto);
						exportDtos[1 + i].setGkto(debitorenkontoDto);
						exportDtos[1 + i]
								.setOPNummer(extractLaufendeNummerAusBelegnummer(
										reDto.getCNr(), theClientDto));
						exportDtos[1 + i].setBelegnr(reDto.getCNr());
						exportDtos[1 + i].setBelegdatum(reDto.getTBelegdatum());
						exportDtos[1 + i]
								.setValutadatum(reDto.getTBelegdatum());
						exportDtos[1 + i].setWaehrung(theClientDto
								.getSMandantenwaehrung());
						// Sollbetrag ist netto
						BigDecimal bdNettobetrag = reDto.getNWert()
								.multiply(reKontierungDto[i].getNProzentsatz())
								.movePointLeft(2);
						BigDecimal bdNettobetragFW = bdNettobetrag.divide(
								reDto.getNKurs(), FinanzFac.NACHKOMMASTELLEN,
								BigDecimal.ROUND_HALF_EVEN);
						// auf 2 Nachkommastellen runden
						bdNettobetrag = bdNettobetrag.setScale(
								FinanzFac.NACHKOMMASTELLEN,
								BigDecimal.ROUND_HALF_EVEN);
						bdNettobetragFW = bdNettobetragFW.setScale(
								FinanzFac.NACHKOMMASTELLEN,
								BigDecimal.ROUND_HALF_EVEN);
						exportDtos[1 + i].setHabenbetrag(bdNettobetrag);
						exportDtos[1 + i].setHabenbetragFW(bdNettobetragFW);
						// Habenbetrag ist null
						exportDtos[1 + i].setSollbetrag(null);
						exportDtos[1 + i].setSollbetragFW(null);
						// Steuerbetrag ist der UST-Betrag
						BigDecimal bdUst = reDto.getNWertust()
								.multiply(reKontierungDto[i].getNProzentsatz())
								.movePointLeft(2);
						bdUst = bdUst.setScale(FinanzFac.NACHKOMMASTELLEN,
								BigDecimal.ROUND_HALF_EVEN);
						exportDtos[1 + i].setSteuer(bdUst);
						// Fremdwaehrung
						exportDtos[1 + i].setFremdwaehrung(reDto
								.getWaehrungCNr());
						exportDtos[1 + i].setKost(kstDtoHW);
						exportDtos[1 + i].setBelegart(BELEGART_AR);
						exportDtos[1 + i].setPartnerDto(kundeDto
								.getPartnerDto());
						exportDtos[1 + i].setLaenderartCNr(sLaenderart);
						exportDtos[1 + i].setZahlungszielDto(zzDto);
						exportDtos[1 + i].setText(kundeDto.getPartnerDto()
								.formatFixTitelName1Name2());
						exportDtos[1 + i].setUidNummer(kundeDto.getPartnerDto()
								.getCUid());
						exportDtos[1 + i].setFaelligkeitsdatum(tFaelligkeit);
						// Restwert
						bdRestNetto = bdRestNetto.subtract(bdNettobetrag);
						bdRestNettoFW = bdRestNettoFW.subtract(bdNettobetragFW);
						bdRestUST = bdRestUST.subtract(bdUst);
						if (bdNettobetrag.compareTo(bdGrNettowert) > 0) {
							bdGrNettowert = bdNettobetrag;
							iIndexDesGrNettowerts = 1 + i;
						}
					}
					// jetzt eventuelle Cent-Differenzen bereinigen
					exportDtos[iIndexDesGrNettowerts]
							.setHabenbetrag(exportDtos[iIndexDesGrNettowerts]
									.getHabenbetragBD().add(bdRestNetto));
					exportDtos[iIndexDesGrNettowerts]
							.setHabenbetragFW(exportDtos[iIndexDesGrNettowerts]
									.getHabenbetragFWBD().add(bdRestNettoFW));
					exportDtos[iIndexDesGrNettowerts]
							.setSteuer(exportDtos[iIndexDesGrNettowerts]
									.getSteuerBD().add(bdRestUST));
					// // eventuelle Centdifferenz zwischen Brutto und Netto+UST
					// bereinigen
					// BigDecimal bdDifferenz = exportDtos[0].getSollbetragBD();
					// for (int i = 0; i < exportDtos.length; i++) {
					// if (exportDtos[i].getHabenbetragBD() != null) {
					// bdDifferenz =
					// bdDifferenz.subtract(exportDtos[i].getHabenbetragBD());
					// }
					// if (exportDtos[i].getSteuerBD() != null) {
					// bdDifferenz =
					// bdDifferenz.subtract(exportDtos[i].getSteuerBD());
					// }
					// }
					// // die Differenz wird vom UST-Wert des groessten
					// Nettobetrags subtrahiert
					// exportDtos[iIndexDesGrNettowerts].setSteuer(exportDtos[
					// iIndexDesGrNettowerts].getSteuerBD().add(bdDifferenz));
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return exportDtos;
	}

	public FibuexportDto[] getExportdatenGutschrift(Integer rechnungIId,
			Date dStichtag) throws EJBExceptionLP {
		// wie die Ausgangsrechnung, aber mit kleinen Aenderungen:
		// 1. Reihenfolge der Buchungszeilen aendern
		// 2. Belegkreis wird GS
		// 3. Betraege negieren
		FibuexportDto[] exportAR = getExportdatenRechnung(rechnungIId,
				dStichtag);
		if (exportAR == null || exportAR.length == 0)
			return null;
		FibuexportDto[] exportGS = new FibuexportDto[exportAR.length];
		for (int i = 0; i < exportAR.length; i++) {
			// Reihenfolge
			exportGS[i] = exportAR[exportAR.length - 1 - i];
			// Belegkreis
			exportGS[i].setBelegart(BELEGART_GS);
			// Werte negieren
			if (exportGS[i].getSollbetragBD() != null) {
				exportGS[i].setSollbetrag(exportGS[i].getSollbetragBD()
						.negate());
			}
			if (exportGS[i].getSollbetragFWBD() != null) {
				exportGS[i].setSollbetragFW(exportGS[i].getSollbetragFWBD()
						.negate());
			}
			if (exportGS[i].getHabenbetragBD() != null) {
				exportGS[i].setHabenbetrag(exportGS[i].getHabenbetragBD()
						.negate());
			}
			if (exportGS[i].getHabenbetragFWBD() != null) {
				exportGS[i].setHabenbetragFW(exportGS[i].getHabenbetragFWBD()
						.negate());
			}
			if (exportGS[i].getSteuerBD() != null) {
				exportGS[i].setSteuer(exportGS[i].getSteuerBD().negate());
			}
		}
		return exportGS;
	}

	@Override
	public ReportErloeskontoDto getErloeskonto(Integer rechnungId,
			BelegpositionVerkaufDto positionDto) throws EJBExceptionLP {
		throw new IllegalArgumentException("should not be called!");
	}
	
	@Override
	public KontoDto getUebersetzeKontoNachLandBzwLaenderart(Integer kontoIId,
			Integer belegId) throws EJBExceptionLP {
		Validator.notNull(kontoIId, "kontoIId");
		Validator.notNull(belegId, "belegId");
		throw new IllegalArgumentException("should not be called!");
	}	
}
/*
 * 2.3. Musterbeispiele 2.3.1. Allgemeines Neben der genauen Einhaltung des
 * Satzaufbaues ist auch notwendig, dass die Datens&auml;tze buchhalterisch
 * richtig sind. Es ist genau zu beachten, welcher Betrag im Soll bzw. im Haben
 * zu stehen hat und ob dies der Netto- oder der Bruttobetrag ist. Aus den
 * Fakturen m&uuml;ssen Buchungss&auml;tze gebildet werden und nur diese
 * k&ouml;nnen mit dieser Schnittstelle &uuml;bernommen werden. Die folgenden
 * Beispiele sollen als Hilfe bei der Erstellung der Datens&auml;tze dienen, sie
 * k&ouml;nnen jedoch Buchhaltungskenntnisse nicht ersetzen. 2.3.2.
 * Ausgangsrechnungen mit Gegenbuchung Eine Ausgangsrechnung ist dann in dieser
 * Buchungsart zu verbuchen, wenn nur ein Gegenkonto vorliegt. Eine solche
 * Rechnung ist in zwei Buchungss&auml;tze aufzuspalten: Buchung am Kundenkonto
 * und Gegenbuchung am Erl&ouml;skonto. Am Kundenkonto wird der Bruttobetrag,
 * auf den Erl&ouml;skonten der Nettobetrag und der Steuerbetrag verbucht.
 * Nettobetrag + Steuer muss den Bruttobetrag ergeben. 2.3.2.1. Ausgangsrechnung
 * in EUR Ausgangsrechnung &uuml;ber EUR 10.000,-- (netto) + 2.000,-- USt EUR
 * 12.000,-- brutto. Rechnungsdatum 15. 10. 2002, Rechnungsnummer 100,
 * Kundenkonto 20100, Erl&ouml;skonto 4120. Buchung auf das Kundenkonto
 * 20100;4120;100;15102002;;EUR;12000,00;0,00;0,00;;0,00;0,00;0;AR;100;1;20;2;
 * 0;1;;;;Ausgangsrechnung mit;Gegenbuchung; Gegenbuchung am Erl&ouml;skonto
 * 4120;20100;0;15102002;;EUR;0,00;10000,00;2000,00;;0,00;0,00;0;AR;100;1;20;2;
 * 0;1;;;;Ausgangsrechnung mit;Gegenbuchung; A Datenimport 2-7 2.3.2.2.
 * Ausgangsrechnung in einer Drittw&auml;hrung Bei Rechnungen in
 * Drittw&auml;hrungen ist im Feld ???Fremdw&auml;hrung??? der ISO-Code der
 * Drittw&auml;hrung und in den Feldern ???Fremdw&auml;hrung-Sollbetrag??? bzw.
 * ???Fremdw&auml;hrung-Habenbetrag??? der Drittw&auml;hrungsbetrag einzugeben.
 * Die Fremdw&auml;hrungsbetr&auml;ge sind in die Grundw&auml;hrung der
 * Buchhaltung umzurechnen und im Feld ???W&auml;hrung??? der ISO-Code der
 * Grundw&auml;hrung sowie unter ???Sollbetrag??? bzw. ???Habenbetrag??? die
 * umgerechneten Betr&auml;ge einzugeben. Ausgangsrechnung &uuml;ber USD 900,--
 * (netto) + 180,-- USt USD 1.080,-- brutto. Rechnungsdatum 20. 7. 2002,
 * Rechnungsnummer 100, Kundenkonto 20100, Erl&ouml;skonto 4120. Die
 * umgerechnete Betr&auml;ge sind EUR 1000,-- + 200,-- 1200,--
 * 20100;4120;100;20072002;;EUR;1200,00;0,00;0,00;USD;1080,00;0,00;0;AR;100;1;
 * 20;2;0;1;;;;Ausgangsrechnung mit;Gegenbuchung;
 * 4120;20100;0;20072002;;EUR;0,00
 * ;1200,00;200,00;USD;0,00;900,00;0;AR;100;1;20;2; 0;1;;;;Ausgangsrechnung
 * mit;Gegenbuchung; A 2-8 Datenimport ?? RZL Software GmbH. 2.3.3.
 * Ausgangsrechnung-Gutschrift mit Gegenbuchung Bei einer Gutschrift erfolgt
 * zuerst die Buchung am Erl&ouml;skonto und dann die Gegenbuchung am
 * Debitorenkonto. Gutschrift &uuml;ber EUR 1.000,-- (netto) + 200,-- USt EUR
 * 1.200,-- brutto. Rechnungsdatum 17. 1. 2002, Rechnungsnummer 101, Kundenkonto
 * 20100, Erl&ouml;skonto 4120.
 * 4120;20100;0;17012002;;EUR;1000,00;0,00;-200,00;;0,00;0,00;0;AR;101;1;20;2;
 * 0;1;;;;Gutschrift mit;Gegenbuchung;
 * 20100;4120;101;17012002;;EUR;0,00;1200,00;0,00;;0,00;0,00;0;AR;101;1;20;2;0
 * ;1;;;;Gutschrift mit;Gegenbuchung; 2.3.4. Ausgangsrechnung Splitbuchung Eine
 * Splitbuchung ist dann vorzunehmen, wenn bei einer Ausgangsrechnung mehr als 1
 * Erl&ouml;skonto vorliegt. Es erfolgt zuerst die Split-Sammelbuchung am
 * Debitor und dann die Gegenbuchungen auf den einzelnen Erl&ouml;skonten
 * 2.3.4.1. Splitbuchung mit gleichen USt-S&auml;tzen Beispiel: Ausgangsrechnung
 * &uuml;ber brutto EUR 60.000,--, am Konto 4120 sind netto 20.000,-- + 20 % USt
 * (4.000,---) , am Konto 4122 netto 30.000,-- + 20 % USt (6.000,--) zu
 * verbuchen. Split-Sammelbuchung:
 * 20200;0;103;15012002;;EUR;60000,00;0,00;0,00;;0,00;0,00;0;AR;103;1;20;2;
 * 0;4;;;;Splitbuchung mit;gleichen USt-S&auml;tzen; Split-Gegenbuchungen:
 * 4120;20200;0;15012002;;EUR;0,00;20000,00;4000,00;;0,00;0,00;0;AR;103;1;20;2
 * ;0;3;;;;Splitbuchung mit;gleichen USt-S&auml;tzen;
 * 4122;20200;0;15012002;;EUR;0,00;30000,00;6000,00;;0,00;0,00;0;AR;103;1;20;2
 * ;0;3;;;;Splitbuchung mit;gleichen USt-S&auml;tzen; 2.3.4.2. Splitbuchung mit
 * verschiedenen USt-S&auml;tzen Der Unterschied zur Splitbuchung mit gleichen
 * USt-S&auml;tzen liegt im Feld ???USt-Prozentsatz??? und ???USt-Code??? . In
 * der Split-Sammelbuchung sind diese Felder bei verschiedenen USt- S&auml;tzen
 * Null. Beispiel: Ausgangsrechnung &uuml;ber brutto EUR 25.000,--, am Konto
 * 4120 sind netto 10.000,-- + 20 % USt, am Konto 4110 netto 11818.18 + 10 % USt
 * zu verbuchen. Split-Sammelbuchung:
 * 20400;0;104;23012002;;EUR;25000,00;0,00;0,00;;0,00;0,00;0;AR;104;1;0;0;
 * 0;4;;;;Splitbuchung mit;unterschiedl. USt; Split-Gegenbuchungen:
 * 4120;20400;0;23012002;;EUR;0,00;10000,00;2000,00;;0,00;0,00;0;AR;104;1;
 * 20;2;0;3;;;;Splitbuchung mit;unterschiedl. USt;
 * 4110;20400;0;23012002;;EUR;0,00;11818,18;1181,82;;0,00;0,00;0;AR;104;1;10;2
 * ;0;3;;;;Splitbuchung mit;unterschiedl. USt; A Datenimport 2-9 2.3.5.
 * Innergemeinschaftliche Lieferung Bei einer innergemeinschaftlichen Lieferung
 * ist die UID-Nummer verpflichtend. Beispiel: Ausgangsrechnung &uuml;ber EUR
 * 100.000,-- UID-Nummer DE123456788
 * 20500;4155;105;27012002;;EUR;100000,00;0,00;0,00;;0,00;0,00;0;AR;105;1;
 * 2;2;0;1;;;;Innergem. Lieferung;;DE123456788
 * 4155;20500;0;27012002;;EUR;0,00;100000,00;0,00;;0,00;0,00;0;AR;105;1;
 * 2;2;0;1;;;;Innergem. Lieferung;;DE123456788 2.3.6. Lieferung in
 * Drittl&auml;nder (Exporterl&ouml;s) Beispiel: Ausgangsrechnung &uuml;ber USD
 * 900,-- (= EUR 1.000,--)
 * 20600;4101;105;27012002;;EUR;1000,00;0,00;0,00;USD;900,00;0,00;0;AR;105;1;
 * 1;2;0;1;;;;Export;;
 * 4101;20600;0;27012002;;EUR;0,00;1000,00;0,00;USD;0,00;900,00;0;AR;105;1;
 * 1;2;0;1;;;;EXport;; 2.3.7. Ausgangsrechnung Bauleistung (ab. 1. 10. 2002)
 * Ausgangsrechnung &uuml;ber EUR 10.000,--. Von der Buchungssystematik wie
 * Exporterl&ouml;se, jedoch ist als Sondercode 14 zu setzten.
 * 20600;4106;105;27102002;;EUR;10000,00;0,00;0,00;;0,00;0,00;0;AR;105;1;
 * 1;2;14;1;;;;Bauleistung;;ATU12345675
 * 4106;20600;0;27102002;;EUR;0,00;10000,00;0,00;;0,00;0,00;0;AR;105;1;
 * 1;2;14;1;;;;Bauleistung;;ATU12345675 2.3.8. Eingangsrechnungen mit
 * Gegenbuchung Von der Systematik her sind diese &auml;hnlich den
 * Ausgangsrechnungen. Der Unterschied besteht darin, dass auf den
 * Aufwandskonten der Nettobetrag im Soll steht und am Kreditorenkonto der
 * Bruttobetrag im Haben. Im Feld ???USt-Code??? ist 1 f&uuml;r Vorsteuer
 * einzugeben. Eingangsrechnung &uuml;ber EUR 10.000,-- (netto) + 2.000,-- USt
 * EUR 12.000,-- brutto. Rechnungsdatum 17. 1. 2002, Rechnungsnummer 100,
 * Lieferantenkonto 30100, Einkaufskonto 5100.
 * 5100;30100;0;17012002;;EUR;10000,00;0,00;2000,00;;0,00;0,00;0;ER;100;1;
 * 20;1;0;1;;;;Eingangsrechnung;;
 * 30100;5100;101;17012002;;EUR;0,00;12000,00;0,00;;0,00;0,00;0;ER;100;1;
 * 20;1;0;1;;;;Eingangsrechnung;; 2.3.9. Eingangsrechnung-Gutschrift mit
 * Gegenbuchung Gutschrift &uuml;ber EUR 1.000,-- (netto) + 200,-- USt EUR
 * 1.200,-- brutto. Rechnungsdatum 17. 1. 2002, Rechnungsnummer 101,
 * Lieferantenkonto 20100, Aufwandskonto 5100.
 * 30100;5100;0;17012002;;EUR;1200,00;0,00;0,00;;0,00;0,00;0;ER;101;1;
 * 20;1;0;1;;;;ER-Gutschrift;;
 * 5100;30100;101;17012002;;EUR;0,00;1000,00;-200,00;;0,00;0,00;0;ER;101;1;
 * 20;1;0;1;;;;ER-Gutschrift;; A 2-10 Datenimport ?? RZL Software GmbH. 2.3.10.
 * Eingangsrechnung Splitbuchung Eine Splitbuchung ist dann vorzunehmen, wenn
 * bei einer Eingangsrechnung mehr als 1 Aufwandskonto vorliegt. Es erfolgt
 * zuerst die Split-Sammelbuchung am Kreditor und dann die Gegenbuchungen auf
 * den einzelnen Aufwandskonten. 2.3.10.1. ER-Splitbuchung mit gleichen
 * USt-S&auml;tzen Beispiel: Eingangsrechnung &uuml;ber brutto EUR 60.000,--, am
 * Konto 5100 sind netto 20.000,-- + 20 % USt (4.000,---) , am Konto 5200 netto
 * 30.000,-- + 20 % USt (6.000,--) zu verbuchen. Split-Sammelbuchung:
 * 30100;0;103;15012002;;EUR;0,00;60000,00;0,00;;0,00;0,00;0;ER;103;1;
 * 20;1;0;4;;;;Splitbuchung;; Split-Gegenbuchungen:
 * 5100;30100;0;15012002;;EUR;20000,00;0,00;4000,00;;0,00;0,00;0;ER;103;1;
 * 20;1;0;3;;;;Split-ER mit;gleichen USt-S&auml;tzen;
 * 5200;30100;0;15012002;;EUR;30000,00;0,00;6000,00;;0,00;0,00;0;ER;103;1;
 * 20;1;0;3;;;;Split-ER mit;gleichen USt-S&auml;tzen; 2.3.10.2. ER-Splitbuchung
 * mit verschiedenen USt-S&auml;tzen Der Unterschied zur Splitbuchung mit
 * gleichen USt-S&auml;tzen liegt im Feld ???USt-Prozentsatz??? und
 * ???USt-Code??? . In der Split-Sammelbuchung sind diese Felder bei
 * verschiedenen USt- S&auml;tzen Null. Beispiel: Eingangsrechnung &uuml;ber
 * brutto EUR 25.000,--, am Konto 5100 sind netto 10.000,-- + 20 % USt, am Konto
 * 5300 netto 11818.18 + 10 % USt zu verbuchen. Split-Sammelbuchung:
 * 30400;0;104;23012002;;EUR;0,00;25000,00;0,00;;0,00;0,00;0;ER;104;1;
 * 0;0;0;4;;;;Split_ER mit;unterschiedl. USt; Split-Gegenbuchungen:
 * 5100;30400;0;23012002;;EUR;10000,00;0,00;2000,00;;0,00;0,00;0;ER;104;1;
 * 20;1;0;3;;;;Split-ER mit;unterschiedl. USt;
 * 5300;30400;0;23012002;;EUR;11818,18;0,00;1181,82;;0,00;0,00;0;ER;104;1;
 * 10;1;0;3;;;;Split-ER mit;unterschiedl. USt; 2.3.11. Eingangsrechnungen
 * Bauleistungen (ab 1. 10. 2002) Gem&auml;&szlig; ?? 19 1a USt geht die
 * Steuerschuld auf den Leistungsempf&auml;nger &uuml;ber. Von der
 * Buchungssystematik &auml;hnlich wie eine Eingangsrechnung mit Gegenbuchung,
 * jedoch ist der Sollbetrag und der Habenbetrag identisch und als Sondercode
 * ist 15 zu setzen. Eingangsrechnung &uuml;ber EUR 10.000,-- (brutto=netto).
 * Rechnungsdatum 17. 10. 2002, Rechnungsnummer 100, Lieferantenkonto 30100,
 * Einkaufskonto 5110. Der Rechnungsempf&auml;nger ist zum Vorsteuerabzug
 * berechtigt.
 * 5110;30100;0;17102002;;EUR;10000,00;0,00;2000,00;;0,00;0,00;0;ER;100;1;
 * 20;1;15;1;;;;Eingangsrechnung;;
 * 30100;5110;101;17012002;;EUR;0,00;10000,00;0,00;;0,00;0,00;0;ER;100;1;
 * 20;1;15;1;;;;Eingangsrechnung;;
 */

