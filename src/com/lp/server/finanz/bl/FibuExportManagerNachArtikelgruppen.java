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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungKontierungDto;
import com.lp.server.finanz.service.FibuExportKriterienDto;
import com.lp.server.finanz.service.FibuexportDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.FinanzamtDto;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.ReportErloeskontoDto;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferschein;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferscheinposition;
import com.lp.server.lieferschein.service.LieferscheinpositionFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnungPosition;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.WaehrungDto;
import com.lp.server.system.service.ZahlungszielDto;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.service.BelegpositionVerkaufDto;
import com.lp.util.AddableHashMap;
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
 * @version not attributable Date $Date: 2013/01/30 13:28:06 $
 */
class FibuExportManagerNachArtikelgruppen extends FibuExportManager {
	/**
	 * FibuExportManagerNachKostenstellen
	 * 
	 * @param exportKriterienDto
	 *            FibuExportKriterienDto
	 * @param idUser
	 *            String
	 */
	FibuExportManagerNachArtikelgruppen(
			FibuExportKriterienDto exportKriterienDto, TheClientDto theClientDto) {
		super(exportKriterienDto, theClientDto);
	}

	public FibuexportDto[] getExportdatenEingangsrechnung(Integer eingangsrechnungIId, Date stichtag) 
			throws EJBExceptionLP {
		myLogger.logData(eingangsrechnungIId, theClientDto.getIDUser());
		FibuexportDto[] exportDtos = null;
		try {
			int iZeilen = 1; // buchung auf kreditorenkonto
			int iAnzahlGegenkonten;
			EingangsrechnungDto erDto = getEingangsrechnungFac().eingangsrechnungFindByPrimaryKey(eingangsrechnungIId);
			boolean bAnzahlung = erDto.getEingangsrechnungartCNr().equals(EingangsrechnungFac.EINGANGSRECHNUNGART_ANZAHLUNG);
			// Nur Belege mit Wert ungleich 0 exportieren
			if (erDto.getNBetrag().compareTo(new BigDecimal(0)) != 0) {
				// Pruefen und Laden der eventuell vorhandenen
				// Mehrfachkontierung
				EingangsrechnungKontierungDto[] erKontoDto = super.pruefeEingangsrechnungKontierung(erDto);
				// pruefen, ob die Mehrfachkontierungen den gleichen MWST-Satz
				// haben
				boolean bUSTSatzGleich = true;
				if (erKontoDto != null) {
					for (int i = 0; i < erKontoDto.length - 1; i++) {
						if (!erKontoDto[i].getMwstsatzIId().equals(erKontoDto[i + 1].getMwstsatzIId())) {
							bUSTSatzGleich = false;
							break;
						}
					}
				}
				// Lieferant holen und Daten pruefen
				LieferantDto lieferantDto = super.pruefeEingangsrechnungLieferant(erDto);
				// Holen des Lieferantenkontos
				KontoDto kreditorenkontoDto = getFinanzFac().kontoFindByPrimaryKey(
						lieferantDto.getKontoIIdKreditorenkonto());
				// Einfache Kontierung?
				if (erDto.getKontoIId() != null) {
					iAnzahlGegenkonten = 1;
				}
				// sonst Mehrfachkontierung
				else {
					if (bAnzahlung)
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_BUCHUNG_NICHT_ERLAUBT_BUCHUNGSREGEL, 
								"Anzahlung kann nicht mehrfachkontiert sein! ER " + erDto.getCNr());
					iAnzahlGegenkonten = erKontoDto.length;
				}
				// Anzahl der Buchungszeilen
				iZeilen = 1 + iAnzahlGegenkonten;
				// jetzt wird das Daten-Array zusammengebaut
				exportDtos = new FibuexportDto[iZeilen];
				for (int i = 0; i < exportDtos.length; i++) {
					exportDtos[i] = new FibuexportDto();
					exportDtos[i].setIGeschaeftsjahr(erDto.getIGeschaeftsjahr());
					exportDtos[i].setBReverseCharge(Helper.short2boolean(erDto.getBReversecharge()));
				}
				// die Buchung auf Kreditorenkonto ist immer die erste.
				int iZeilePersonenkonto = 0;

				// Die Buchungszeile fuers Kreditorenkonto
				exportDtos[iZeilePersonenkonto].setKonto(kreditorenkontoDto);
				Integer kontoIIdAnzahlungVerr = null;
				if (bAnzahlung) {
					// bei Anzahlungsrechnung auf Konto "Anzahlung Gegeben Verrechnet" buchen
					
					FinanzamtDto finanzamtDto = getFinanzamt(kreditorenkontoDto.getFinanzamtIId());
					kontoIIdAnzahlungVerr = Helper.short2boolean(erDto.getBReversecharge()) ?
							finanzamtDto.getKontoIIdRCAnzahlungGegebenVerr() :
							finanzamtDto.getKontoIIdAnzahlungGegebenVerr();
					if (kontoIIdAnzahlungVerr == null) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_EXPORT_ANZAHLUNG_KONTO_NICHT_DEFINIERT, 
								"Verrechnungskonto Geleistete Anzahlungen nicht definiert f\u00FCr Anzahlungsrechnung " + erDto.getCNr());
					}
				}
				// Gegenkonto / Kostenstelle / MWST-Satz
				if (iAnzahlGegenkonten > 1) {
					// wenn es mehrere Gegenkonten gibt
					exportDtos[iZeilePersonenkonto].setGkto(null);
					// wenn alle Mehrfachkontierungen den gleichen UST-Satz
					// haben,
					// dann kommt auch hier der MWST-Satz rein
					if (bUSTSatzGleich) {
						MwstsatzDto mwstDto = getMwstsatz(erKontoDto[0].getMwstsatzIId());
						exportDtos[iZeilePersonenkonto].setMwstsatz(mwstDto);
					}
				} else {
					// nur ein Gegenkonto, aber 2 Moeglichkeiten:
					// 1. Das Gegenkonto steht in den Kopfdaten
					KontoDto kontoDtoGegenkonto = null;
					if (erKontoDto == null) {
						if (bAnzahlung) {
							kontoDtoGegenkonto = getFinanzFac().kontoFindByPrimaryKey(kontoIIdAnzahlungVerr);
						} else {
							kontoDtoGegenkonto = getFinanzFac().kontoFindByPrimaryKey(erDto.getKontoIId());
						}
						exportDtos[iZeilePersonenkonto].setGkto(kontoDtoGegenkonto);
						// Kostenstelle
						Integer kstIId = erDto.getKostenstelleIId();
						KostenstelleDto kstDto = getSystemFac().kostenstelleFindByPrimaryKey(kstIId);
						exportDtos[iZeilePersonenkonto].setKost(kstDto);
						// MWST-Satz
						Integer mwstIId = erDto.getMwstsatzIId();
						MwstsatzDto mwstDto = getMwstsatz(mwstIId);
						exportDtos[iZeilePersonenkonto].setMwstsatz(mwstDto);
						// MWST-Betrag
						exportDtos[iZeilePersonenkonto].setSteuer(erDto.getNUstBetrag());
					}
					// 2. Es gibt nur einen Mehrfachkontierungseintrag mit dem
					// ganzen ER-Wert
					else {
						if (bAnzahlung) {
							kontoDtoGegenkonto = getFinanzFac().kontoFindByPrimaryKey(kontoIIdAnzahlungVerr);
						} else {
							kontoDtoGegenkonto = getFinanzFac().kontoFindByPrimaryKey(
									erKontoDto[0].getKontoIId());
						}
						exportDtos[iZeilePersonenkonto].setGkto(kontoDtoGegenkonto);
						/**
						 * @todo unterscheiden: alle mit gleicher UST oder mit
						 *       verschiedener PJ 4270
						 */
						exportDtos[iZeilePersonenkonto].setSteuer(new BigDecimal(0));
					}
				}
				exportDtos[iZeilePersonenkonto].setOPNummer(erDto.getCLieferantenrechnungsnummer());
				exportDtos[iZeilePersonenkonto].setBelegnr(erDto.getCNr());
				exportDtos[iZeilePersonenkonto].setBelegdatum(erDto.getDBelegdatum());
				
				//PJ 17006
				//exportDtos[iZeilePersonenkonto].setValutadatum(erDto.getDFreigabedatum());
				exportDtos[iZeilePersonenkonto].setValutadatum(stichtag);
				
				exportDtos[iZeilePersonenkonto].setWaehrung(theClientDto.getSMandantenwaehrung());
				// Sollbetrag ist null
				exportDtos[iZeilePersonenkonto].setSollbetrag(null);
				exportDtos[iZeilePersonenkonto].setSollbetragFW(null);
				// Bruttobetrag im Haben
				exportDtos[iZeilePersonenkonto].setHabenbetrag(erDto.getNBetrag());
				exportDtos[iZeilePersonenkonto].setHabenbetragFW(erDto.getNBetragfw());
				// Steuer ist null
				exportDtos[iZeilePersonenkonto].setSteuer(null);
				// MWST-Betrag
				exportDtos[iZeilePersonenkonto].setFremdwaehrung(erDto.getWaehrungCNr());
				exportDtos[iZeilePersonenkonto].setBelegart(BELEGART_ER);
				exportDtos[iZeilePersonenkonto].setPartnerDto(lieferantDto.getPartnerDto());
				String laenderartCNr = null;
				boolean hatFibu = getMandantFac().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_FINANZBUCHHALTUNG, theClientDto);
				if (hatFibu) {
					laenderartCNr = getLaenderartZuKonto(lieferantDto.getPartnerDto(), lieferantDto.getKontoIIdKreditorenkonto());
				} else {
					laenderartCNr = super.getLaenderartZuPartner(lieferantDto.getPartnerDto());
				}
				exportDtos[iZeilePersonenkonto].setLaenderartCNr(laenderartCNr);

				ZahlungszielDto zzDto = getZahlungsziel(erDto.getZahlungszielIId());
				exportDtos[iZeilePersonenkonto].setZahlungszielDto(zzDto);
				exportDtos[iZeilePersonenkonto].setText(erDto.getCText());
				exportDtos[iZeilePersonenkonto].setSExterneBelegnummer(erDto.getCLieferantenrechnungsnummer());
				exportDtos[iZeilePersonenkonto].setUidNummer(lieferantDto.getPartnerDto().getCUid());
				// Gegenbuchung, wenn keine Mehrfachkontierung
				if (erKontoDto == null) {
					final int iZeileGegenbuchung = 1;

					// Kst nicht pruefen, da nur das konto zaehlt
					KostenstelleDto kstDtoHW = getSystemFac().kostenstelleFindByPrimaryKey(
							erDto.getKostenstelleIId());
					KontoDto kontoDtoGegenkonto = null;
					if (bAnzahlung) {
						kontoDtoGegenkonto = getFinanzFac().kontoFindByPrimaryKey(kontoIIdAnzahlungVerr);
					} else {
						kontoDtoGegenkonto = getFinanzFac().kontoFindByPrimaryKey(erDto.getKontoIId());
					}
					// buchung aufs HW-Konto
					exportDtos[iZeileGegenbuchung].setKonto(kontoDtoGegenkonto);
					exportDtos[iZeileGegenbuchung].setGkto(kreditorenkontoDto);
					exportDtos[iZeileGegenbuchung].setOPNummer(erDto.getCLieferantenrechnungsnummer());
					exportDtos[iZeileGegenbuchung].setBelegnr(erDto.getCNr());
					exportDtos[iZeileGegenbuchung].setBelegdatum(erDto.getDBelegdatum());
					
					//PJ 17006
					//exportDtos[iZeileGegenbuchung].setValutadatum(erDto.getDFreigabedatum());
					exportDtos[iZeileGegenbuchung].setValutadatum(stichtag);
					
					exportDtos[iZeileGegenbuchung].setWaehrung(theClientDto.getSMandantenwaehrung());
					if (Helper.short2boolean(erDto.getBIgErwerb()) || Helper.short2boolean(erDto.getBReversecharge())) {
						// die Steuer ist nur theoretisch, daher Betrag ohne Steuer
						exportDtos[iZeileGegenbuchung].setSollbetrag(erDto.getNBetrag());
						exportDtos[iZeileGegenbuchung].setSollbetragFW(erDto.getNBetragfw());
					} else {
						// Sollbetrag ist brutto
						exportDtos[iZeileGegenbuchung].setSollbetrag(erDto.getNBetrag().subtract(erDto.getNUstBetrag()));
						exportDtos[iZeileGegenbuchung].setSollbetragFW(erDto.getNBetragfw().subtract(erDto.getNUstBetragfw()));
					}
					// Habenbetrag ist null
					exportDtos[iZeileGegenbuchung].setHabenbetrag(null);
					exportDtos[iZeileGegenbuchung].setHabenbetragFW(null);
					// Steuerbetrag ist hier
					exportDtos[iZeileGegenbuchung].setSteuer(erDto.getNUstBetrag());
					exportDtos[iZeileGegenbuchung].setFremdwaehrung(erDto.getWaehrungCNr());
					exportDtos[iZeileGegenbuchung].setKost(kstDtoHW);
					exportDtos[iZeileGegenbuchung].setBelegart(BELEGART_ER);
					exportDtos[iZeileGegenbuchung].setPartnerDto(lieferantDto.getPartnerDto());
					// mwst-satz
					Integer mwstIId = erDto.getMwstsatzIId();
					MwstsatzDto mwstDto = getMwstsatz(mwstIId);
					exportDtos[iZeileGegenbuchung].setMwstsatz(mwstDto);
					exportDtos[iZeileGegenbuchung].setLaenderartCNr(laenderartCNr);
					exportDtos[iZeileGegenbuchung].setZahlungszielDto(zzDto);
					exportDtos[iZeileGegenbuchung].setText(erDto.getCText());
					exportDtos[iZeileGegenbuchung].setSExterneBelegnummer(erDto.getCLieferantenrechnungsnummer());
					exportDtos[iZeileGegenbuchung].setUidNummer(lieferantDto.getPartnerDto().getCUid());
				} else {
					// Mehrfachkontierung
					// Kst nicht pruefen, da nur das konto zaehlt
					if (bAnzahlung)
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_BUCHUNG_NICHT_ERLAUBT_BUCHUNGSREGEL, 
								"Anzahlung kann nicht mehrfachkontiert sein! ER " + erDto.getCNr());

					BigDecimal summeKontierung = new BigDecimal(0);
					int maxIndex = 0;
					BigDecimal maxBetrag = new BigDecimal(0);
					for (int i = 0; i < erKontoDto.length; i++) {
						final int iAktuelleZeileGegenbuchung = i + 1; // in der ersten steht die sammelbuchung
						KostenstelleDto kstDtoHW = getSystemFac().kostenstelleFindByPrimaryKey(erKontoDto[i].getKostenstelleIId());
						KontoDto kontoDtoGegenkonto = getFinanzFac().kontoFindByPrimaryKey(erKontoDto[i].getKontoIId());
						// buchung aufs HW-Konto
						exportDtos[iAktuelleZeileGegenbuchung].setKonto(kontoDtoGegenkonto);
						exportDtos[iAktuelleZeileGegenbuchung].setGkto(kreditorenkontoDto);
						exportDtos[iAktuelleZeileGegenbuchung].setOPNummer(erDto.getCLieferantenrechnungsnummer());
						exportDtos[iAktuelleZeileGegenbuchung].setBelegnr(erDto.getCNr());
						exportDtos[iAktuelleZeileGegenbuchung].setBelegdatum(erDto.getDBelegdatum());
						
						//PJ 17006
						//exportDtos[iAktuelleZeileGegenbuchung].setValutadatum(erDto.getDFreigabedatum());
						exportDtos[iAktuelleZeileGegenbuchung].setValutadatum(stichtag);
						
						exportDtos[iAktuelleZeileGegenbuchung].setWaehrung(theClientDto.getSMandantenwaehrung());
						// Sollbetrag ist netto
						// Achtung: Kontierung ist immer in Rechnungswaehrung!
						BigDecimal bdNettobetragFW;
						BigDecimal bdNettobetrag;
						if (Helper.short2boolean(erDto.getBIgErwerb()) || Helper.short2boolean(erDto.getBReversecharge())) {
							// die Steuer ist nur theoretisch, daher Betrag ohne Steuer
							bdNettobetragFW = erKontoDto[i].getNBetrag();
							bdNettobetrag = Helper.rundeKaufmaennisch(bdNettobetragFW.multiply(erDto.getNKurs()), FinanzFac.NACHKOMMASTELLEN);
						} else {
							bdNettobetragFW = erKontoDto[i].getNBetrag().subtract(erKontoDto[i].getNBetragUst());
							bdNettobetrag = Helper.rundeKaufmaennisch(bdNettobetragFW.multiply(erDto.getNKurs()), FinanzFac.NACHKOMMASTELLEN);
						}
						if (bdNettobetrag.compareTo(maxBetrag)>0) {
							maxIndex = iAktuelleZeileGegenbuchung;
							maxBetrag = bdNettobetrag;
						}
							
						exportDtos[iAktuelleZeileGegenbuchung].setSollbetrag(bdNettobetrag);
						exportDtos[iAktuelleZeileGegenbuchung].setSollbetragFW(bdNettobetragFW);
						// Habenbetrag ist null
						exportDtos[iAktuelleZeileGegenbuchung].setHabenbetrag(null);
						exportDtos[iAktuelleZeileGegenbuchung].setHabenbetragFW(null);
						// Steuerbetrag ist der UST-Betrag
						BigDecimal bdBetragUstFW = erKontoDto[i].getNBetragUst();
						BigDecimal bdBetragUst = Helper.rundeKaufmaennisch(bdBetragUstFW.multiply(erDto.getNKurs()), FinanzFac.NACHKOMMASTELLEN);
						exportDtos[iAktuelleZeileGegenbuchung].setSteuer(bdBetragUst);
						exportDtos[iAktuelleZeileGegenbuchung].setSteuerFW(bdBetragUstFW);
						summeKontierung = summeKontierung.add(bdNettobetrag).add(bdBetragUst);
						// Fremdwaehrung
						exportDtos[iAktuelleZeileGegenbuchung].setFremdwaehrung(erDto.getWaehrungCNr());
						exportDtos[iAktuelleZeileGegenbuchung].setKost(kstDtoHW);
						exportDtos[iAktuelleZeileGegenbuchung].setBelegart(BELEGART_ER);
						exportDtos[iAktuelleZeileGegenbuchung].setPartnerDto(lieferantDto.getPartnerDto());
						// mwst-satz
						Integer mwstIId = erKontoDto[i].getMwstsatzIId();
						MwstsatzDto mwstDto = getMwstsatz(mwstIId);
						exportDtos[iAktuelleZeileGegenbuchung].setMwstsatz(mwstDto);
						exportDtos[iAktuelleZeileGegenbuchung].setLaenderartCNr(laenderartCNr);
						exportDtos[iAktuelleZeileGegenbuchung].setZahlungszielDto(zzDto);
						exportDtos[iAktuelleZeileGegenbuchung].setText(erDto.getCText());
						exportDtos[iAktuelleZeileGegenbuchung].setSExterneBelegnummer(erDto.getCLieferantenrechnungsnummer());
						exportDtos[iAktuelleZeileGegenbuchung].setUidNummer(lieferantDto.getPartnerDto().getCUid());
					}
					if (summeKontierung.compareTo(exportDtos[0].getHabenbetragBD()) != 0) {
						BigDecimal diff = summeKontierung.subtract(exportDtos[0].getHabenbetragBD());
						System.out.println("Differenz: " + diff.toString());
						if (diff.abs().compareTo(new BigDecimal("0.011"))<0)
							exportDtos[maxIndex].setSollbetrag(exportDtos[maxIndex].getSollbetragBD().subtract(diff));
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

	public FibuexportDto[] getExportdatenRechnung(Integer rechnungIId, Date dStichtag)
	throws EJBExceptionLP {
		FibuexportDto[] exportDtos = null;
		Session session = null;
		// Die Werte zu den Konten werden in HashMaps gesammelt.
		AddableHashMap<Integer, BigDecimal> hmNettoFW = new AddableHashMap<Integer, BigDecimal>();
		AddableHashMap<Integer, BigDecimal> hmBruttoFW = new AddableHashMap<Integer, BigDecimal>();
		AddableHashMap<Integer, String> hmArtikelGruppen = new AddableHashMap<Integer, String>();
		AddableHashMap<Integer, MwstsatzDto> hmMwstsatz = new AddableHashMap<Integer, MwstsatzDto>();
		WaehrungDto uebersteuerteMandantenwaherung = null;
		boolean bAllgemeinerRabattExtraBuchen = false;
		boolean bAnzahlungsrechnung = false;
		try {
			KontoDto kontoDtoAllgemeinerRabatt = null;
			ParametermandantDto parameterKontoRabatt = getParameterFac()
			.getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_FINANZ_RABATT_KONTO);
			if (parameterKontoRabatt.getCWert().trim().length()>0) {
				kontoDtoAllgemeinerRabatt = getFinanzFac().kontoFindByCnrKontotypMandantOhneExc(
						parameterKontoRabatt.getCWert().trim(),
						FinanzServiceFac.KONTOTYP_SACHKONTO, theClientDto.getMandant(), theClientDto);
				if (kontoDtoAllgemeinerRabatt == null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER,
							new Exception("Konto " + parameterKontoRabatt.getCWert() 
									+ " f\u00FCr Allgemeinen Rabatt ist nicht vorhanden"));
				}
				bAllgemeinerRabattExtraBuchen = true;
			}

			ParametermandantDto parameterKontoSonstige = getParameterFac()
			.getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_FINANZ_EXPORT_ARTIKELGRUPPEN_DEFAULT_KONTO_AR);
			KontoDto kontoDtoSonstige = getFinanzFac().kontoFindByCnrKontotypMandantOhneExc(
					parameterKontoSonstige.getCWert(),
					FinanzServiceFac.KONTOTYP_SACHKONTO, 
					theClientDto.getMandant(), theClientDto);
			if (kontoDtoSonstige == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER,
						new Exception(
						"Konto f\u00FCr Positionen ohne Artikelgruppen ist nicht definiert\nDefinieren Sie den Paramter FINANZ_EXPORT_ARTIKELGRUPPEN_DEFAULT_KONTO_AR"));
			}
			//Holen einer eventuell vorhandenen uebersteuerten Waehrung
			ParametermandantDto paramterWaehrungUebersteuert = getParameterFac().
			getMandantparameter(
					theClientDto.getMandant(), 
					ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_FINANZ_EXPORT_UEBERSTEUERTE_MANDANTENWAEHRUNG);
			String sWaehrung = paramterWaehrungUebersteuert.getCWert();
			if(sWaehrung != null && !"".equals(sWaehrung) && !sWaehrung.equals(theClientDto.getSMandantenwaehrung())){
				//Mandantenwaehrung wird uebersteuert
				try{
					uebersteuerteMandantenwaherung = getLocaleFac().waehrungFindByPrimaryKey(sWaehrung);
				} catch (Exception e){
					if(e.getCause() instanceof EJBExceptionLP){
						//Wenn Waehrung nicht vorhanden dann Fehler
						EJBExceptionLP ex = new EJBExceptionLP(
								EJBExceptionLP.FEHLER_FINANZ_EXPORT_WAEHRUNG_NICHT_GEFUNDEN,
								new Exception("\u00DCbersteuerte W\u00E4hrung nicht gefunden:"));
						ArrayList<Object> a = new ArrayList<Object>();
						a.add(sWaehrung);
						ex.setAlInfoForTheClient(a);
						throw ex;
					} else {
						throw new EJBExceptionLP(e);
					}
				}
			}
			RechnungDto reDto = getRechnungFac().rechnungFindByPrimaryKey(rechnungIId);
			bAnzahlungsrechnung = reDto.getRechnungartCNr().equals(RechnungFac.RECHNUNGART_ANZAHLUNG);
			// Kunde holen und Daten pruefen
			KundeDto kundeDto = super.pruefeRechnungKunde(reDto);
			// Laenderart des Kunden
			// PJ 15203: zwischenspeichern wegen Lieferadresse
			String sLaenderartRechnung = null;
			//PJ 17120
			Integer finanzamtIId = null;
			Integer debitorenKontoIIdUebersteuert = null;
			
			boolean hatFibu = getMandantFac().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_FINANZBUCHHALTUNG, theClientDto);
			if (hatFibu) {
				sLaenderartRechnung = getLaenderartZuKonto(kundeDto.getPartnerDto(), kundeDto.getIidDebitorenkonto(), Helper.short2boolean(reDto.getBReversecharge()));
			} else {
				sLaenderartRechnung = getLaenderartZuPartner(kundeDto.getPartnerDto());
			}
			String sLaenderart = sLaenderartRechnung;
			// Konto uebersetzen
			Integer iLand = kundeDto.getPartnerDto().getLandplzortDto().getIlandID();
			//SK: wird nur benoetigt wenn es sich um Handartikel handelt
			// 	Wird also weiter unten bearbeitet
			//kontoDtoSonstige = uebersetzeKontoNachLandBzwLaenderart(
			//		kontoDtoSonstige.getIId(), sLaenderart, reDto, iLand);
			/**
			 * @ MR->MR: Achtung kontoDtoSonstige darf nur genommen werden,
			 * Artikel keine Artikelgruppe hat?! 
			 * SK: kontoDtoSonstige darf nur
			 * f&uuml;r Handeinganben verwendet werden. Artikel ohne Gruppe sind
			 * nicht erlaubt.
			 */
			if (reDto.getNWert() != null
					&& reDto.getNWert().compareTo(new BigDecimal(0)) != 0) {
				myLogger.logData("exportiere Rechnung " + reDto.getCNr(),
						theClientDto.getIDUser());
				// Rechnungspositionen mit Hibernate holen.
				ParametermandantDto parametermandantautoDebitDto = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_KUNDEN,
								ParameterFac.PARAMETER_AUTOMATISCHE_DEBITORENNUMMER);
				boolean legeDebitorAn = (Boolean) parametermandantautoDebitDto.getCWertAsObject();
				
				SessionFactory factory = FLRSessionFactory.getFactory();
				session = factory.openSession();
				Criteria c = session.createCriteria(FLRRechnungPosition.class);
				c.add(Restrictions.eq(
						RechnungFac.FLR_RECHNUNGPOSITION_RECHNUNG_I_ID,
						rechnungIId));
				List<?> list = c.list();
				for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
					FLRRechnungPosition rePos = (FLRRechnungPosition) iter
					.next();
					// nur preisbehaftete Positionen anschaun
					// sk: alle positionen da zB Lieferscheine hier keinen Preis
					// haben
					/*
					 * if (rePos.getN_bruttoeinzelpreis() != null &&
					 * rePos.getN_bruttoeinzelpreis().compareTo(new
					 * BigDecimal(0)) != 0) {
					 */
					// Netto- und Bruttowert der Position bestimmen und
					// addieren.
					//
					// PJ 15015: falls Rabattkonto Parameter gesetzt, Allgemeinen Rabatt extra buchen
					//		= Differenz aus Rechnungsbetrag und Summe der Positionen ohne Rabatt
					/**
					 * @todo MR->MR: Achtung wenn Positionsart "Lieferschein",
					 *       dann ist Menge null. Direkt den Berechneten
					 *       Summenpreis nehmen.
					 */
					// PJ 15203: default ist aus Rechnung
					sLaenderart = sLaenderartRechnung;
					BigDecimal bdNetto = null;
					BigDecimal bdBrutto = null;
					if (RechnungFac.POSITIONSART_RECHNUNG_LIEFERSCHEIN.equals(
							rePos.getPositionsart_c_nr())) {
						if (bAnzahlungsrechnung) {
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_EXPORT_ANZAHLUNGSRECHNUNG_LIEFERSCHEIN_NICHT_ERLAUBT, 
									"Lieferscheinposition nicht erlaubt in Anzahlungsrechnung " + reDto.getCNr());
						}
						// PJ 15203: 
						// Lieferschein holen wegen Lieferadresse
						FLRLieferschein flrLieferschein = (FLRLieferschein) rePos.getFlrlieferschein();
						if (flrLieferschein.getFlrkunde().getI_id() != flrLieferschein.getFlrkunderechnungsadresse().getI_id()) {
							if (hatFibu) {
								// bei Fibu ist die Basis der Laenderart das Finanzamt des Debitorenkontos
								KundeDto lieferscheinKundeDto = getKundeFac().kundeFindByPrimaryKey(flrLieferschein.getFlrkunde().getI_id(), theClientDto);
								if (lieferscheinKundeDto.getIidDebitorenkonto() == null) {
									if(legeDebitorAn) {
											KontoDto ktoDto = getKundeFac()
													.createDebitorenkontoZuKundenAutomatisch(
															lieferscheinKundeDto.getIId(), false,
															null, theClientDto);
											lieferscheinKundeDto.setIDebitorenkontoAsIntegerNotiId(new Integer(
													ktoDto.getCNr()));
											getKundeFac().updateKunde(lieferscheinKundeDto, theClientDto);
											lieferscheinKundeDto = getKundeFac().kundeFindByPrimaryKey(flrLieferschein.getFlrkunde().getI_id(), theClientDto);
									} else {
										throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_EXPORT_DEBITORENKONTO_NICHT_DEFINIERT, 
												"Debitorenkonto f\u00FCr Lieferschein nicht definiert. ", lieferscheinKundeDto.getPartnerDto().formatName()
												+ ", Lieferschein: " + flrLieferschein.getC_nr());
									}
								}
								sLaenderart = getLaenderartZuKonto(lieferscheinKundeDto.getPartnerDto(), lieferscheinKundeDto.getIidDebitorenkonto());
								sLaenderartRechnung = sLaenderart; // SP 2013/01221 Lieferschein uebersteuert fuer folgende Positionen
								//PJ 17120
								KontoDto lieferscheinDebitorenkonto = getFinanzFac().kontoFindByPrimaryKey(lieferscheinKundeDto.getIidDebitorenkonto());
								finanzamtIId = lieferscheinDebitorenkonto.getFinanzamtIId();
								debitorenKontoIIdUebersteuert = lieferscheinKundeDto.getIidDebitorenkonto();
							} else {
								// PJ 17385 nur uebersteuern wenn Laender unterschiedlich
								if (getFinanzServiceFac().isFibuLandunterschiedlich(kundeDto.getPartnerDto().getIId(), 
										flrLieferschein.getFlrkunde().getFlrpartner().getI_id())) {
											sLaenderart = getLaenderartZuPartner(getPartnerFac()
												.partnerFindByPrimaryKeyOhneExc(flrLieferschein.getFlrkunde().getFlrpartner().getI_id(), theClientDto));
								}
							}
						}

						/**
						 * @todo MR->MR:passt das? TODO: SK: die
						 *       Lieferscheinpositionen holen
						 */
						FLRLieferscheinposition[] flrLieferscheinpos = (FLRLieferscheinposition[]) rePos
						.getFlrlieferschein()
						.getFlrlieferscheinpositionen().toArray(
								new FLRLieferscheinposition[0]);
						for (int x = 0; x < flrLieferscheinpos.length; x++) {
							if (flrLieferscheinpos[x].getSetartikel_set().size()!=0) 
								continue;	// Kopf eines Sets NICHT BUCHEN!, in die Fibu gehen nur die Teile
							if (bAllgemeinerRabattExtraBuchen) {
								if(flrLieferscheinpos[x].getN_nettogesamtpreisplusversteckteraufschlag()!=null){
									bdNetto = flrLieferscheinpos[x].
									getN_nettogesamtpreisplusversteckteraufschlag().multiply(flrLieferscheinpos[x].getN_menge());
								} else {
									bdNetto = new BigDecimal(0);
								}
							} else {
								if(flrLieferscheinpos[x].getN_nettogesamtpreisplusversteckteraufschlagminusrabatt()!=null){
									bdNetto = flrLieferscheinpos[x].
									getN_nettogesamtpreisplusversteckteraufschlagminusrabatt().multiply(flrLieferscheinpos[x].getN_menge());
								} else {
									bdNetto = new BigDecimal(0);
								}
							}							
							bdNetto = bdNetto.setScale(FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN);
							BigDecimal bdMWSTSatz;
							if(flrLieferscheinpos[x].getFlrmwstsatz()!=null){
								bdMWSTSatz = new BigDecimal(flrLieferscheinpos[x].getFlrmwstsatz().getF_mwstsatz()).divide(new BigDecimal(100),
										FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN);
							} else {
								bdMWSTSatz = new BigDecimal(0);
							}
							//bdMWSTSatz = bdMWSTSatz.setScale(2,BigDecimal.ROUND_HALF_EVEN);
							BigDecimal bdMWSTBetrag = bdNetto.multiply(bdMWSTSatz);//2 nachkommastellen gerundet
							bdBrutto=  (bdNetto.add(bdMWSTBetrag)).setScale(FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN);
							// bdNetto = rePos.getN_nettoeinzelpreis();
							// bdBrutto = rePos.getN_bruttoeinzelpreis();
							//Wenn eine Handeingabe dann Default Konto
							//							if(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_HANDEINGABE.
							//									equals(flrLieferscheinpos[x].getPositionsart_c_nr())){
							//								//Das Default Konto auch uebersetzen
							//								kontoDtoSonstige = uebersetzeKontoNachLandBzwLaenderart(
							//										kontoDtoSonstige.getIId(), sLaenderart, reDto, iLand);
							//							}
							if(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT.
									equals(flrLieferscheinpos[x].getPositionsart_c_nr())){
								if (flrLieferscheinpos[x].getFlrartikel() == null) {
									EJBExceptionLP ex = 
										new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_EXPORT_ARTIKEL_KEINE_ARTIKELGRUPPE,
												new Exception(
														"Keine Artikelgruppe f\u00FCr Artikel definiert:"
														+ flrLieferscheinpos[x]
														                     .getC_bez()));
									ArrayList<Object> al = new ArrayList<Object>();
									al.add(flrLieferscheinpos[x]
									                          .getC_bez());
									ex.setAlInfoForTheClient(al);
									throw ex;
								} 
								if (flrLieferscheinpos[x].getFlrartikel()
										.getFlrartikelgruppe() == null) {
									EJBExceptionLP ex = new EJBExceptionLP(
											EJBExceptionLP.FEHLER_FINANZ_EXPORT_ARTIKEL_KEINE_ARTIKELGRUPPE,
											new Exception(
													"Keine Artikelgruppe f\u00FCr Artikel definiert:"
													+ flrLieferscheinpos[x]
													                     .getFlrartikel()
													                     .getC_nr()));
									ArrayList<Object> a = new ArrayList<Object>();
									a.add("Rechnung: " + rePos.getFlrrechnung().getC_nr() + " \n" + 
											"Lieferschein: " + flrLieferscheinpos[x].getFlrlieferschein().getC_nr() + " \n" +
											"Artikel: " + flrLieferscheinpos[x].getFlrartikel().getC_nr());
									ex.setAlInfoForTheClient(a);
									throw ex;
								} 
								if (flrLieferscheinpos[x].getFlrartikel()
										.getFlrartikelgruppe().getFlrkonto() == null) {
									EJBExceptionLP ex = new EJBExceptionLP(
											EJBExceptionLP.FEHLER_FINANZ_EXPORT_KEIN_KONTO_FUER_ARTIKELGRUPPE,
											new Exception(
													"Kein Konto f\u00FCr Artikelgruppe definiert: \n"
													+ flrLieferscheinpos[x]
													                     .getFlrartikel()
													                     .getFlrartikelgruppe()
													                     .getC_nr()));
									ArrayList<Object> a = new ArrayList<Object>();
									a.add(flrLieferscheinpos[x]
									                         .getFlrartikel().getC_nr());
									a.add(flrLieferscheinpos[x]
									                         .getFlrartikel()
									                         .getFlrartikelgruppe()
									                         .getC_nr());
									ex.setAlInfoForTheClient(a);
									throw ex;



								} 
							}
							if((LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_HANDEINGABE.
									equals(flrLieferscheinpos[x].getPositionsart_c_nr())) ||
									(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT.
											equals(flrLieferscheinpos[x].getPositionsart_c_nr()))){
								Integer iLieferscheinPosKontoIId = null;
								if(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_HANDEINGABE.
										equals(flrLieferscheinpos[x].getPositionsart_c_nr())){
									iLieferscheinPosKontoIId = kontoDtoSonstige.getIId();
								} else {
									iLieferscheinPosKontoIId = flrLieferscheinpos[x]
									                                              .getFlrartikel().getFlrartikelgruppe()
									                                              .getFlrkonto().getI_id();
								}
								if (hmNettoFW.get(iLieferscheinPosKontoIId) != null) {
									bdNetto = bdNetto.add(hmNettoFW
											.get(iLieferscheinPosKontoIId));
								}
								hmNettoFW
								.put(iLieferscheinPosKontoIId, bdNetto);
								//Zu jedem Konto Artikelgruppenbezeichnung merken
								if(hmArtikelGruppen.get(iLieferscheinPosKontoIId)==null){
									if(flrLieferscheinpos[x].getFlrartikel()
											.getFlrartikelgruppe()==null){
										if(iLieferscheinPosKontoIId.equals(kontoDtoSonstige.getIId())){
											hmArtikelGruppen.put(iLieferscheinPosKontoIId,kontoDtoSonstige.getCBez());
										} else {
											KontoDto kontoDto = getFinanzFac().kontoFindByPrimaryKey(iLieferscheinPosKontoIId);
											hmArtikelGruppen.put(iLieferscheinPosKontoIId,kontoDto.getCBez());
										}
									} else {
										hmArtikelGruppen.put(iLieferscheinPosKontoIId, flrLieferscheinpos[x].getFlrartikel()
												.getFlrartikelgruppe().getC_nr());
									}
								}
								//MWST-Satz Merken
								MwstsatzDto mwst = getMandantFac().mwstsatzFindByPrimaryKey( flrLieferscheinpos[x].getFlrmwstsatz().getI_id(), theClientDto);
								if(mwst==null){
									EJBExceptionLP ex = new EJBExceptionLP(
											EJBExceptionLP.FEHLER_FINANZ_EXPORT_KEIN_MWSTCODE,
											new Exception(
													"Kein einzigartiger FIBU-MWSTCode definiert bei Beleg:"
													+ reDto.getCNr()));
									ArrayList<Object> a = new ArrayList<Object>();
									a.add("Rechnung: " + reDto.getCNr());
									a.add("Lieferschein: " + flrLieferscheinpos[x].getFlrlieferschein().getC_nr());
									ex.setAlInfoForTheClient(a);
									throw ex;
								}
								if(hmMwstsatz.get(iLieferscheinPosKontoIId)==null){
									//Neu einfuegen
									hmMwstsatz.put(iLieferscheinPosKontoIId,mwst);
								} else {
									if(!hmMwstsatz.get(iLieferscheinPosKontoIId).equals(mwst)){
										EJBExceptionLP ex = new EJBExceptionLP(
												EJBExceptionLP.FEHLER_FINANZ_EXPORT_KEIN_MWSTCODE,
												new Exception(
														"Kein einzigartiger FIBU-MWSTCode definiert bei Beleg:"
														+ reDto.getCNr()));
										ArrayList<Object> a = new ArrayList<Object>();
										a.add("Rechnung: " + reDto.getCNr());
										a.add("Lieferschein: " + flrLieferscheinpos[x].getFlrlieferschein().getC_nr());
										ex.setAlInfoForTheClient(a);
										throw ex;
									}
								}
								if (hmBruttoFW.get(iLieferscheinPosKontoIId) != null) {
									bdBrutto = bdBrutto.add(hmBruttoFW
											.get(iLieferscheinPosKontoIId));
								}
								hmBruttoFW.put(iLieferscheinPosKontoIId,
										bdBrutto);
							}
						}
						// bdNetto = rePos.getN_nettoeinzelpreis();
						// bdBrutto = rePos.getN_bruttoeinzelpreis();
					} else {
						if (bAllgemeinerRabattExtraBuchen) {
							if(rePos.getN_nettoeinzelpreis_plus_aufschlag()!=null){
								bdNetto = rePos
								.getN_nettoeinzelpreis_plus_aufschlag()
								.multiply(rePos.getN_menge());
							} else {
								bdNetto = new BigDecimal(0);
							}
						} else {
							if(rePos.getN_nettoeinzelpreis_plus_aufschlag_minus_rabatt()!=null){
								bdNetto = rePos
								.getN_nettoeinzelpreis_plus_aufschlag_minus_rabatt()
								.multiply(rePos.getN_menge());
							} else {
								bdNetto = new BigDecimal(0);
							}
						}
						bdNetto = bdNetto.setScale(FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN);
						BigDecimal bdMWSTSatz;
						if(rePos.getFlrmwstsatz()!=null){
							bdMWSTSatz = new BigDecimal(rePos.getFlrmwstsatz().getF_mwstsatz()).divide(new BigDecimal(100), 
									FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN);
						} else {
							bdMWSTSatz = new BigDecimal(0);
						}
						//bdMWSTSatz = bdMWSTSatz.setScale(2,BigDecimal.ROUND_HALF_EVEN);
						BigDecimal bdMWSTBetrag = bdNetto.multiply(bdMWSTSatz);
						bdBrutto=  (bdNetto.add(bdMWSTBetrag)).setScale(FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN);
						// haben die auch eine Artikelgruppe mit Konto
						//						if(RechnungFac.POSITIONSART_RECHNUNG_HANDEINGABE.equals(rePos.getPositionsart_c_nr())){
						//							//Default Artikelgruppenkonto
						//							kontoDtoSonstige = uebersetzeKontoNachLandBzwLaenderart(
						//									kontoDtoSonstige.getIId(), sLaenderart, reDto, iLand);
						//						}
						if(RechnungFac.POSITIONSART_RECHNUNG_IDENT.equals(rePos.getPositionsart_c_nr())){
							if (!bAnzahlungsrechnung) {
								// nur pruefen wenn keine Anzahlungsrechnung
								if (rePos.getFlrartikel() == null
										|| rePos.getFlrartikel().getFlrartikelgruppe() == null) {								
									EJBExceptionLP ex = new EJBExceptionLP(
											EJBExceptionLP.FEHLER_FINANZ_EXPORT_ARTIKEL_KEINE_ARTIKELGRUPPE,
											new Exception(
													"Keine Artikelgruppe f\u00FCr Artikel definiert:"
													+ rePos
													.getFlrartikel()
													.getC_nr()));
									ArrayList<Object> a = new ArrayList<Object>();
									a.add("Rechnung: " + rePos.getFlrrechnung().getC_nr() + " \n" + 
	
											"Artikel: " + rePos.getFlrartikel().getC_nr());
									ex.setAlInfoForTheClient(a);
									throw ex;
								} 
								if (rePos.getFlrartikel().getFlrartikelgruppe()
										.getFlrkonto() == null) {								
									EJBExceptionLP ex = new EJBExceptionLP(
											EJBExceptionLP.FEHLER_FINANZ_EXPORT_KEIN_KONTO_FUER_ARTIKELGRUPPE,
											new Exception(
													"Kein Konto f\u00FCr Artikelgruppe definiert: \n"
													+ rePos
													.getFlrartikel()
													.getFlrartikelgruppe()
													.getC_nr()));
									ArrayList<Object> a = new ArrayList<Object>();
									a.add(rePos
											.getFlrartikel().getC_nr());
									a.add(rePos
											.getFlrartikel()
											.getFlrartikelgruppe()
											.getC_nr());
									ex.setAlInfoForTheClient(a);
									throw ex;
								} 
							}
						}
						if((RechnungFac.POSITIONSART_RECHNUNG_IDENT.equals(rePos.getPositionsart_c_nr())) ||
								(RechnungFac.POSITIONSART_RECHNUNG_HANDEINGABE.equals(rePos.getPositionsart_c_nr()))){
							if (rePos.getSetartikel_set().size() == 0) {
								// keine Kopfposition eines Artikelsets
								Integer kontoIId = null;
								if (bAnzahlungsrechnung) {
									KontoDto debitorenkontoDto = getFinanzFac().kontoFindByPrimaryKey(kundeDto.getIidDebitorenkonto());
									FinanzamtDto finanzamtDto = getFinanzamt(debitorenkontoDto.getFinanzamtIId());
									kontoIId = Helper.short2boolean(reDto.getBReversecharge()) ?
											finanzamtDto.getKontoIIdRCAnzahlungErhaltVerr() :
											finanzamtDto.getKontoIIdAnzahlungErhaltVerr();
									if (kontoIId == null) {
										throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_EXPORT_ANZAHLUNG_KONTO_NICHT_DEFINIERT, 
												"Verrechnungskonto Erhaltene Anzahlungen nicht definiert f\u00FCr Anzahlungsrechnung " + reDto.getCNr());
									}
								} else if(RechnungFac.POSITIONSART_RECHNUNG_HANDEINGABE.equals(rePos.getPositionsart_c_nr())){
									kontoIId = kontoDtoSonstige.getIId();
								} else {
									kontoIId =	rePos.getFlrartikel()
										.getFlrartikelgruppe().getFlrkonto()
										.getI_id();
									//Zu jedem Konto Artikelgruppenbezeichnung merken
									if(hmArtikelGruppen.get(kontoIId)==null){
										hmArtikelGruppen.put(kontoIId, rePos.getFlrartikel()
													.getFlrartikelgruppe().getC_nr());
									}
								}
								if (hmNettoFW.get(kontoIId) != null) {
									bdNetto = bdNetto.add(hmNettoFW.get(kontoIId));
								}
								hmNettoFW.put(kontoIId, bdNetto);
								if (hmBruttoFW.get(kontoIId) != null) {
									bdBrutto = bdBrutto.add(hmBruttoFW.get(kontoIId));
								}
								hmBruttoFW.put(kontoIId, bdBrutto);
									
								//MWST-Satz Merken
								MwstsatzDto mwst = getMandantFac().mwstsatzFindByPrimaryKey( rePos.getFlrmwstsatz().getI_id(), theClientDto);
								if(mwst==null){
									EJBExceptionLP ex = new EJBExceptionLP(
											EJBExceptionLP.FEHLER_FINANZ_EXPORT_KEIN_MWSTCODE,
											new Exception(
													"Kein einzigartiger FIBU-MWSTCode definiert bei Beleg:"
													+ reDto.getCNr()));
									ArrayList<Object> a = new ArrayList<Object>();
									a.add("Rechnung: " + reDto.getCNr());
									ex.setAlInfoForTheClient(a);
									throw ex;
								}
								if(hmMwstsatz.get(kontoIId)==null){
									//Neu einfuegen
									hmMwstsatz.put(kontoIId,mwst);
								} else {
									if(!hmMwstsatz.get(kontoIId).equals(mwst)){
										EJBExceptionLP ex = new EJBExceptionLP(
												EJBExceptionLP.FEHLER_FINANZ_EXPORT_KEIN_MWSTCODE,
												new Exception(
														"Kein einzigartiger FIBU-MWSTCode definiert bei Beleg:"
														+ reDto.getCNr()));
										ArrayList<Object> a = new ArrayList<Object>();
										a.add("Rechnung: " + reDto.getCNr());
										//a.add("Lieferschein: " + rePos.getFlrlieferschein().getC_nr());
										ex.setAlInfoForTheClient(a);
										throw ex;
									}
								}
							}
						}
					}
				}

				// }

				// Netto- und Brutto-Summe bestimmen.
				BigDecimal bdSummeNetto = new BigDecimal(0);
				BigDecimal bdSummeBrutto = new BigDecimal(0);

				Integer iKontoIIdMaxBetrag = 0;

				BigDecimal bdWertNettoFW = reDto.getNWertfw();
				BigDecimal bdWertBruttoFW = reDto.getNWertfw().add(reDto.getNWertustfw());

				for (Iterator<Integer> iter = hmNettoFW.keySet().iterator(); iter
				.hasNext();) {
					Integer kontoIId = (Integer) iter.next();
					BigDecimal bdBetragNetto = hmNettoFW.get(kontoIId);
					BigDecimal bdBetragBrutto = hmBruttoFW.get(kontoIId);
					bdSummeNetto = bdSummeNetto.add(bdBetragNetto);
					bdSummeBrutto = bdSummeBrutto.add(bdBetragBrutto);
					BigDecimal bdBetragMax = hmNettoFW.get(iKontoIIdMaxBetrag);
					if(bdBetragMax==null){
						iKontoIIdMaxBetrag=kontoIId;
					} else {
						if(bdBetragMax.compareTo(bdBetragNetto) < 0){
							iKontoIIdMaxBetrag=kontoIId;
						}
					}
				}
				if (bAllgemeinerRabattExtraBuchen && reDto.getFAllgemeinerRabattsatz().doubleValue() != 0) {
					// PJ 15015: Allgemeinen Rabatt buchen
					if(bdSummeNetto.compareTo(bdWertNettoFW) != 0 ||
							bdSummeBrutto.compareTo(bdWertBruttoFW) != 0){
						BigDecimal bdHelper = hmNettoFW.get(kontoDtoAllgemeinerRabatt.getIId());
						if (bdHelper == null)
							bdHelper = new BigDecimal(0);
						bdHelper = bdHelper.add(bdWertNettoFW.subtract(bdSummeNetto));
						hmNettoFW.put(kontoDtoAllgemeinerRabatt.getIId(), bdHelper);
						bdHelper = hmBruttoFW.get(kontoDtoAllgemeinerRabatt.getIId());
						if (bdHelper == null)
							bdHelper = new BigDecimal(0);
						bdHelper = bdHelper.add(bdWertBruttoFW.subtract(bdSummeBrutto));
						hmBruttoFW.put(kontoDtoAllgemeinerRabatt.getIId(), bdHelper);
					}
				} else {
					if(bdSummeNetto.compareTo(bdWertNettoFW) != 0 ||
							bdSummeBrutto.compareTo(bdWertBruttoFW) != 0){
						BigDecimal bdHelper = hmNettoFW.get(iKontoIIdMaxBetrag);
						bdHelper = bdHelper.add(bdWertNettoFW.subtract(bdSummeNetto));
						hmNettoFW.put(iKontoIIdMaxBetrag,bdHelper);
						bdHelper = hmBruttoFW.get(iKontoIIdMaxBetrag);
						bdHelper = bdHelper.add(bdWertBruttoFW.subtract(bdSummeBrutto));
						hmBruttoFW.put(iKontoIIdMaxBetrag, bdHelper);
					}
				}

				//--------------------------------------------------------------
				// Daten zusammenstellen.
				//--------------------------------------------------------------
				final int iAnzahlGegenkonten = hmNettoFW.size();
				final int iZeilen = 1 + iAnzahlGegenkonten; // buchung auf
				// debitorenkonto
				// UST-Satz anhand Kopfdaten (muss kein Standard-%-Satz sein)
				// Nur Belege mit Wert ungleich 0 exportieren
				MwstsatzDto mwstSatz = new MwstsatzDto();
				BigDecimal bdMwstsatz = reDto.getNWertustfw().divide(
						reDto.getNWertfw(), BigDecimal.ROUND_HALF_EVEN).multiply(
								new BigDecimal(100));
				mwstSatz.setFMwstsatz(bdMwstsatz.doubleValue());
				mwstSatz.setIId(reDto.getMwstsatzIId());

				// Holen des Kundenkontos
				KontoDto debitorenkontoDto = getFinanzFac().kontoFindByPrimaryKey(kundeDto.getIidDebitorenkonto());
				// jetzt wird das Daten-Array zusammengebaut
				exportDtos = new FibuexportDto[iZeilen];
				for (int i = 0; i < exportDtos.length; i++) {
					exportDtos[i] = new FibuexportDto();
					//exportDtos[i].setMwstsatz(mwstSatz);
					exportDtos[i].setIGeschaeftsjahr(reDto.getIGeschaeftsjahr());
				}
				// Die Buchungszeile fuers Debitorenkonto
				exportDtos[0].setKonto(debitorenkontoDto);
				// Gegenkonto / Kostenstelle / MWST-Satz
				if (iAnzahlGegenkonten > 1) {
					// wenn es mehrere Gegenkonten gibt
					exportDtos[0].setGkto(null);
				} else {
					/**
					 * @todo MR->MR: Achtung in einem Fall ist
					 *       iAnzahlGegenkonten = 0!!
					 */
					if (hmNettoFW.keySet().iterator().hasNext()) {
						Integer kontoIId = hmNettoFW.keySet().iterator().next(); // es
						// ist
						// das
						// einzige
						// nur ein Gegenkonto, aber 2 Moeglichkeiten:
						// 1. Das Gegenkonto steht in den Kopfdaten
						KontoDto kontoDtoGegenkontoUebersetzt = null;
						if (getMandantFac().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_FINANZBUCHHALTUNG, theClientDto)) {

						} else {
							kontoDtoGegenkontoUebersetzt = uebersetzeKontoNachLandBzwLaenderart(
									kontoIId, sLaenderart, debitorenkontoDto.getFinanzamtIId(), 
									debitorenkontoDto.getMandantCNr(), reDto, iLand);
						}
						exportDtos[0].setGkto(kontoDtoGegenkontoUebersetzt);
						// MWST-Betrag
						//Wg. Drittlandsgutschrift auf 0 statt NULL geaendert					
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
				if(uebersteuerteMandantenwaherung!=null){
					exportDtos[0].setWaehrung(uebersteuerteMandantenwaherung.getCNr());
				} else {
					exportDtos[0].setWaehrung(theClientDto.getSMandantenwaehrung());
				}
				// Haben ist 0
				exportDtos[0].setHabenbetrag(null);
				exportDtos[0].setHabenbetragFW(null);
				// Bruttobetrag im Soll
				if(uebersteuerteMandantenwaherung!=null){
					BigDecimal bdSollBetrMandantenwaehrung = reDto.getNWert().add(
							reDto.getNWertust());
					/*WechselkursDto bdKurs = 
						getLocaleFac().getKursZuDatum(
								uebersteuerteMandantenwaherung.getCNr(),
								theClientDto.getSMandantenwaehrung(),
								new Date(10), 
								theClientDto);
					if(bdKurs==null){
						EJBExceptionLP ex = new EJBExceptionLP(
								EJBExceptionLP.FEHLER_FINANZ_EXPORT_KEINKURS_ZUDATUM,
								new Exception("Kein Kurs f\u00FCr Stichtag eingetragen"));
						ArrayList<Object> a = new ArrayList<Object>();
						a.add("Von: " +uebersteuerteMandantenwaherung.getCNr());
						a.add("Nach: " + theClientDto.getSMandantenwaehrung());
						a.add("Datum: "+ new Date(10));
						a.add("Rechnung: " + reDto.getCNr());
						ex.setAlInfoForTheClient(a);
						throw ex;
					}*/
					//Wenn Rechnungswaehrung gleich uebersteuerte Waehrung dann ist sollbetrag gleich Rechnungsbetrag
					//in Fremdwaehrung
					if(uebersteuerteMandantenwaherung.getCNr().equals(reDto.getWaehrungCNr())){
						exportDtos[0].setSollbetrag(reDto.getNWertfw().add(reDto.getNWertustfw()));
					} else {
						exportDtos[0].setSollbetrag(bdSollBetrMandantenwaehrung.divide(getLocaleFac().getWechselkurs2(
								uebersteuerteMandantenwaherung.getCNr(),theClientDto.getSMandantenwaehrung(), theClientDto),
								FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN));
					}

				} else {
					exportDtos[0].setSollbetrag(reDto.getNWert().add(reDto.getNWertust()));
				}
				exportDtos[0].setSollbetragFW(reDto.getNWertfw().add(reDto.getNWertustfw()));
				// MWST-Betrag
				exportDtos[0].setFremdwaehrung(reDto.getWaehrungCNr());
				exportDtos[0].setBelegart(BELEGART_AR);
				exportDtos[0].setPartnerDto(kundeDto.getPartnerDto());
				exportDtos[0].setLaenderartCNr(sLaenderart);
				if(uebersteuerteMandantenwaherung!=null){
					if(uebersteuerteMandantenwaherung.getCNr().equals(reDto.getWaehrungCNr())){
						//Wenn Beleg gleich uebersteuerter Mandant explizit auf 1 gegen Rundungsfehler
						exportDtos[0].setKurs(new BigDecimal(1));
					} else {
						exportDtos[0].setKurs(getLocaleFac().getWechselkurs2(
								uebersteuerteMandantenwaherung.getCNr(),reDto.getWaehrungCNr(), theClientDto));
					}
				} else {
					exportDtos[0].setKurs(reDto.getNKurs());
				}

				ZahlungszielDto zzDto = getZahlungsziel(reDto.getZahlungszielIId());
				exportDtos[0].setZahlungszielDto(zzDto);
				exportDtos[0].setText(kundeDto.getPartnerDto().formatFixTitelName1Name2());
				exportDtos[0].setUidNummer(kundeDto.getPartnerDto().getCUid());
				// Gegenbuchungen
				// Cent-Differenzen muessen vermieden werden
				BigDecimal bdRestNetto = exportDtos[0].getSollbetragBD();
				/**
				 * @todo MR->MR: Achtung Array IndexOutOfBounds wenn von i = 1
				 *       gestartet wird.
				 */
				int iIndexDesGrNettowerts = 0;
				int i = 0;
				for (Iterator<Integer> iter = hmNettoFW.keySet().iterator(); iter.hasNext(); i++) {
					// Pruefen der Kostenstelle
					Integer kontoIId = (Integer) iter.next();
					//PJ 17120
					/* KontoDto kontoDtoGegenkonto = uebersetzeKontoNachLandBzwLaenderart(
							kontoIId, sLaenderart, debitorenkontoDto.getFinanzamtIId(), debitorenkontoDto.getMandantCNr(), reDto, iLand); */
					if (finanzamtIId == null)
						// nicht durch Lieferschein uebersteuert
						finanzamtIId = debitorenkontoDto.getFinanzamtIId();
					
					KontoDto kontoDtoGegenkonto = uebersetzeKontoNachLandBzwLaenderart(
							kontoIId, sLaenderart, finanzamtIId, debitorenkontoDto.getMandantCNr(), reDto, iLand);
					// buchung aufs HW-Konto
					exportDtos[1 + i].setDebitorenKontoIIdUebersteuert(debitorenKontoIIdUebersteuert);
					exportDtos[1 + i].setKonto(kontoDtoGegenkonto);
					exportDtos[1 + i].setGkto(debitorenkontoDto);
					exportDtos[1 + i]
					           .setOPNummer(extractLaufendeNummerAusBelegnummer(
					        		   reDto.getCNr(), theClientDto));
					exportDtos[1 + i].setBelegnr(reDto.getCNr());
					exportDtos[1 + i].setBelegdatum(reDto.getTBelegdatum());
					exportDtos[1 + i].setValutadatum(reDto.getTBelegdatum());
					if(uebersteuerteMandantenwaherung!=null){
						exportDtos[1 + i].setWaehrung(uebersteuerteMandantenwaherung.getCNr());
					} else {
						exportDtos[1 + i].setWaehrung(theClientDto
								.getSMandantenwaehrung());
					}
					// Sollbetrag ist netto
					BigDecimal bdNettobetragFW = hmNettoFW.get(kontoIId);
					BigDecimal bdNettobetrag = null;
					if(uebersteuerteMandantenwaherung!=null){
						if(uebersteuerteMandantenwaherung.getCNr().equals(reDto.getWaehrungCNr())){
							//Kurs ist 1 also FW = Rechnungswaehrung
							bdNettobetrag = bdNettobetragFW;
						} else {
							bdNettobetrag = bdNettobetragFW.divide(
									getLocaleFac().getWechselkurs2(
											uebersteuerteMandantenwaherung.getCNr(),reDto.getWaehrungCNr(), theClientDto), 
											FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN);
						}
					} else {
						bdNettobetrag = bdNettobetragFW.divide(reDto.getNKurs(), 
								FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN);
					}

					// auf 2 Nachkommastellen runden
					bdNettobetrag = bdNettobetrag.setScale(
							FinanzFac.NACHKOMMASTELLEN,
							BigDecimal.ROUND_HALF_EVEN);
					bdNettobetragFW = bdNettobetragFW.setScale(
							FinanzFac.NACHKOMMASTELLEN,
							BigDecimal.ROUND_HALF_EVEN);
					exportDtos[1 + i].setHabenbetrag(bdNettobetrag);
					exportDtos[1 + i].setHabenbetragFW(bdNettobetragFW);
					//Merken an welcher Position der groesste Betrag ist
					if(iIndexDesGrNettowerts==0){
						iIndexDesGrNettowerts = 1+i;
					} else {
						if(bdNettobetrag.compareTo(exportDtos[iIndexDesGrNettowerts].getHabenbetragBD()) > 0){
							iIndexDesGrNettowerts= 1+i;
						}
					}

					// Habenbetrag ist null
					exportDtos[1 + i].setSollbetrag(null);
					exportDtos[1 + i].setSollbetragFW(null);
					// Steuerbetrag ist der UST-Betrag
					BigDecimal bdUstFW = hmBruttoFW.get(kontoIId).subtract(
							hmNettoFW.get(kontoIId));
					bdUstFW = bdUstFW.setScale(FinanzFac.NACHKOMMASTELLEN,
							BigDecimal.ROUND_HALF_EVEN);
					exportDtos[1 + i].setSteuerFW(bdUstFW);
					BigDecimal bdBrutto = null;
					if(uebersteuerteMandantenwaherung!=null){
						bdBrutto = hmBruttoFW.get(kontoIId).divide(getLocaleFac().getWechselkurs2(
								uebersteuerteMandantenwaherung.getCNr(),reDto.getWaehrungCNr(), theClientDto), 
								FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN);
					} else {
						bdBrutto = hmBruttoFW.get(kontoIId).divide(reDto.getNKurs(), FinanzFac.NACHKOMMASTELLEN,
								BigDecimal.ROUND_HALF_EVEN);
					}
					BigDecimal bdUST = bdBrutto.subtract(bdNettobetrag);
					bdUST = bdUST.setScale(FinanzFac.NACHKOMMASTELLEN,
							BigDecimal.ROUND_HALF_EVEN);
					exportDtos[1 + i].setSteuer(bdUST);
					exportDtos[1 + i].setMwstsatz(hmMwstsatz.get(kontoIId));


					// Fremdwaehrung
					exportDtos[1 + i].setFremdwaehrung(reDto.getWaehrungCNr());
					KostenstelleDto kstDto = null;
					if (kontoDtoGegenkonto.getKostenstelleIId() != null) {
						kstDto = getSystemFac().kostenstelleFindByPrimaryKey(
								kontoDtoGegenkonto.getKostenstelleIId());
					}
					exportDtos[1 + i].setKost(kstDto);
					exportDtos[1 + i].setBelegart(BELEGART_AR);
					exportDtos[1 + i].setPartnerDto(kundeDto.getPartnerDto());
					exportDtos[1 + i].setLaenderartCNr(sLaenderart);
					exportDtos[1 + i].setZahlungszielDto(zzDto);
					/*exportDtos[1 + i].setText(kundeDto.getPartnerDto()
							.formatFixTitelName1Name2());*/
					exportDtos[1 + i].setText(hmArtikelGruppen.get(kontoIId));
					exportDtos[1 + i].setUidNummer(kundeDto.getPartnerDto()
							.getCUid());
					if(uebersteuerteMandantenwaherung!=null){
						exportDtos[1 + i].setKurs(getLocaleFac().getWechselkurs2(
								uebersteuerteMandantenwaherung.getCNr(),reDto.getWaehrungCNr(), theClientDto));
					} else {
						exportDtos[1 + i].setKurs(reDto.getNKurs());
					}
					bdRestNetto = bdRestNetto.subtract(exportDtos[1 + i].getPositionsbetragLeitwaehrungBD());
					// Restwert
					//bdRestNetto = bdRestNetto.subtract(bdNettobetrag);
					//bdRestNettoFW = bdRestNettoFW.subtract(bdNettobetragFW);
					//bdRestUST = bdRestUST.subtract(bdUstFW);
					//if (bdNettobetrag.compareTo(bdGrNettowert) > 0) {
					//	bdGrNettowert = bdNettobetrag;
					//	iIndexDesGrNettowerts = 1 + i;
					//}
				}
				// jetzt eventuelle Cent-Differenzen bereinigen
				if(bdRestNetto.compareTo(new BigDecimal(10)) > 0){
					//Wenn Korrekturwert >= 10 dann Fehler
					EJBExceptionLP ex = new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FINANZ_EXPORT_KORREKTURBUCHUNG_ZUHOCH,
							new Exception("Korrekturwert zu hoch"));
					ArrayList<Object> a = new ArrayList<Object>();
					a.add("RE: " +reDto.getCNr());
					a.add("Korrekturbetrag: " + bdRestNetto);
					ex.setAlInfoForTheClient(a);
					throw ex;
				} else {
					exportDtos[iIndexDesGrNettowerts].setHabenbetrag(exportDtos[iIndexDesGrNettowerts].getHabenbetragBD().add(bdRestNetto));
				}
				/*if (exportDtos[iIndexDesGrNettowerts].getHabenbetragBD() != null) {
					exportDtos[iIndexDesGrNettowerts]
					           .setHabenbetrag(exportDtos[iIndexDesGrNettowerts]
					                                      .getHabenbetragBD().add(bdRestNetto));
				}
				if (exportDtos[iIndexDesGrNettowerts].getHabenbetragFWBD() != null) {
					exportDtos[iIndexDesGrNettowerts]
					           .setHabenbetragFW(exportDtos[iIndexDesGrNettowerts]
					                                        .getHabenbetragFWBD().add(bdRestNettoFW));
				}
				if (exportDtos[iIndexDesGrNettowerts].getSteuerBD() != null) {
					exportDtos[iIndexDesGrNettowerts]
					           .setSteuer(exportDtos[iIndexDesGrNettowerts]
					                                 .getSteuerBD().add(bdRestUST));
				}*/
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} finally {
			closeSession(session);
		}
		return exportDtos;
	}

	
	public FibuexportDto[] getExportdatenGutschrift(Integer rechnungIId, Date dStichtag)
	throws EJBExceptionLP {
		// wie die Ausgangsrechnung, aber mit kleinen Aenderungen:
		// 1. Reihenfolge der Buchungszeilen aendern
		// 2. Belegkreis wird GS
		// 3. Betraege negieren
		FibuexportDto[] exportAR = getExportdatenRechnung(rechnungIId,dStichtag);
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
		throw new IllegalArgumentException("should not be called!") ;
	}
}
