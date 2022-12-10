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
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.lp.server.finanz.service.FibuExportKriterienDto;
import com.lp.server.finanz.service.FibuexportDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzReportFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.FinanzServiceFac.ReversechargeArt;
import com.lp.server.finanz.service.ReversechargeartDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.ejbfac.HvCreatingCachingProvider;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.LpMailText;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * Diese Klasse kuemmert sich um ...
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 14.03.06
 * </p>
 * 
 * <p>
 * 
 * @author $Author: adi $
 *         </p>
 * 
 * @version not attributable Date $Date: 2012/06/20 12:19:50 $
 */
public class FibuExportFormatterBMD extends FibuExportFormatter {
	/*
	 * ACHTUNG: lt. WH 14.03.06 Zahlungszieltage <3 Tage = Mahnsperre mit Stufe
	 * 1-3 99 Tage = Sofort faellig Skonto: Wenn kein Skonto (=0%) dann 99%
	 * eintragen
	 */

	private final static String P_SATZART = "satzart";
	private final static String P_KONTO = "konto";
	private final static String P_BUCHDAT = "buchdat";
	private final static String P_GKTO = "gkto";
	private final static String P_BELEGNR = "belegnr";
	private final static String P_BELEGDAT = "belegdat";
	private final static String P_KOST = "kost";
	private final static String P_MWST = "mwst";
	private final static String P_STEUCOD = "steucod";
	//private final static String P_EBKENNZ = "ebkennz";
	private final static String P_BUCOD = "bucod";
	private final static String P_BETRAG = "betrag";
	private final static String P_STEUER = "steuer";
	private final static String P_SKONTO = "skonto";
	//private final static String P_OPBETRAG = "opbetrag";
	private final static String P_PERIODE = "periode";
	private final static String P_FWKURS = "fwkurs";
	private final static String P_FWFAKTOR = "fwfaktor";
	private final static String P_FWBETRAG = "fwbetrag";
	private final static String P_FWSTEUER = "fwsteuer";
	private final static String P_FWSKONTO = "fwskonto";
	//private final static String P_FWOPBETRAG = "fwopbetrag";
	//private final static String P_LANDKZ = "landkz";
	//private final static String P_LKZKURS = "lkzkurs";
	//private final static String P_LKZFAKTOR = "lkzfaktor";
	private final static String P_TEXT = "text";
	private final static String P_SYMBOL = "symbol";
	private final static String P_EXTBELEGNR = "extbelegnr";
	private final static String P_ZZIEL = "zziel";
	private final static String P_SKONTOPZ = "skontopz";
	private final static String P_SKONTOTAGE = "skontotage";
	private final static String P_SKONTOPZ2 = "skontopz2";
	private final static String P_SKONTOTAGE2 = "skontotage2";
	//private final static String P_VALUTADATUM = "valutadatum";
	//private final static String P_VERTNR = "vertnr";
	//private final static String P_PROVPZ = "provpz";
	//private final static String P_BENUTZER = "benutzer";
	//private final static String P_BUCHART = "buchart";
	//private final static String P_BUCHKZ = "buchkz";
	//private final static String P_MAHNZ = "mahnz";
	//private final static String P_GEGENBUCHKZ = "gegenbuchkz";
	private final static String P_VERBUCHKZ = "verbuchkz";
	private final static String P_PARTNER_KBZ = "partkbz";
	private final static String P_PARTNER_NAME1 = "partname1";
	
	// skontopz | skontotage | skontopz2 | skontotage2 | valutadatum">

	// Steuercodes in BMD:
	// 00 Vorsteuer oder keine Steuer
	// 01 Ist-Umsatzsteuer
	// 03 Soll-Umsatzsteuer
	// 08 Einkaufskonten fuer innergem. Erwerb, wenn kein VSt.-Abzug besteht.
	// 09 Einkaufskonten fuer innergem. Erwerb, wenn VSt.-Abzug besteht.
	private final static String STEUCOD_VORSTEUER_ODER_KEINE_STEUER = "00";
	private final static String STEUCOD_IST_UMSATZSTEUER = "01";
	private final static String STEUCOD_SOLL_UMSATZSTEUER = "03";
	private final static String STEUCOD_EINKAUF_IG_ERWERB_KEINE_VORSTEUER = "08";
	private final static String STEUCOD_EINKAUF_IG_ERWERB_VORSTEUER = "09";
	private final static String STEUCOD_EINKAUF_REVERSE_CHARGE = "19";
	
	private final static String GEGENBUCHUNGSKENNZEICHEN_EINZELGEGENBUCHUNG = "E";
	private final static String GEGENBUCHUNGSKENNZEICHEN_SAMMELGEGENBUCHUNG = "S";
	private final static String GEGENBUCHUNGSKENNZEICHEN_OHNE_GEGENBUCHUNG = "O";
	// E Einzelgegenbuchung
	// S Sammelgegenbuchung
	// O Ohne Gegenbuchung

	// Buchungscodes in BMD:
	private final static String BUCOD_SOLL = "1";
	private final static String BUCOD_HABEN = "2";

	private boolean isNTCS = false;
	private final static String STEUCOD_NTCS_VORSTEUER = "02";
	private final static String STEUCOD_NTCS_IST_UMSATZSTEUER = "01";
	private final static String STEUCOD_NTCS_SOLL_UMSATZSTEUER = "01";
	private final static String STEUCOD_NTCS_EINKAUF_IG_ERWERB_KEINE_VORSTEUER = "08";
	private final static String STEUCOD_NTCS_EINKAUF_IG_ERWERB_VORSTEUER = "09";
	private final static String STEUCOD_NTCS_EINKAUF_REVERSE_CHARGE = "19";
	
	private final static String P_WAEHRUNG = "waehrung";
	
	//SP8506
	private final static String STEUCOD_NTCS_IST_UMSATZSTEUER_DRITTLAND = "05";
	private final static String STEUCOD_NTCS_SOLL_UMSATZSTEUER_DRITTLAND = "05";
	private final static String STEUCOD_NTCS_IG_LIEFERUNG = "07";
	private final static String STEUCOD_IG_LIEFERUNG = "07";

	//PJ21967
	private final static String STEUCOD_NTCS_EINKAUF_REVERSE_CHARGE_Bauleistung_19_1a_ = "29";
	private final static String STEUCOD_NTCS_EINKAUF_REVERSE_CHARGE_19_1b_ = "23";
	private final static String STEUCOD_NTCS_EINKAUF_REVERSE_CHARGE_19_1c_ = "26";
	private final static String STEUCOD_NTCS_EINKAUF_REVERSE_CHARGE_SCHROTT_19_1d = "59";
	private final ReversechargeartCache reversechargeartCache;

	protected FibuExportFormatterBMD(FibuExportKriterienDto exportKriterienDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		super(exportKriterienDto, theClientDto);
		reversechargeartCache = new ReversechargeartCache();
	}

	public FibuExportFormatterBMD(FibuExportKriterienDto exportKriterienDto,
			TheClientDto theClientDto, boolean isNTCS) {
		super(exportKriterienDto, theClientDto);
		this.isNTCS = isNTCS;
		reversechargeartCache = new ReversechargeartCache();
	}

	protected String exportiereDaten(FibuexportDto[] fibuExportDtos)
	throws EJBExceptionLP {
		String sExportDaten = "";
		try {
			if (fibuExportDtos != null) {
				Integer finanzamtPartnerIId = pruefeFinanzamt(fibuExportDtos);
				// UST-LKZ des Partners bestimmen
				final String sPartnerUstLkz = getPartnerUstLkz(fibuExportDtos);
				// Zuerst: Richtigen UST-Prozentsatz und Finanzamt bestimmen.
				PartnerDto partnerDtoFinanzamt = null;
				// FA holen, wenn eines zugeordnet werden konnte.
				if (finanzamtPartnerIId != null) {
					partnerDtoFinanzamt = super
					.getFinanzamtPartner(finanzamtPartnerIId);
				}
				boolean bFinanzamtImPartnerLand = isFinanzamtImPartnerland(
						sPartnerUstLkz, partnerDtoFinanzamt);
				// feststellen, ob es sich um IG Erwerb / Erloes handelt
				boolean bInnerGemeinschaftlich = isInnergemeinschaftlich(
						fibuExportDtos, bFinanzamtImPartnerLand);
				// Trennung zwischen ER und AR bzw. GS
				// derzeit keine Trennung
				if (fibuExportDtos[0].getBelegart().equals(
						FibuExportManager.BELEGART_ER)) {
					sExportDaten = exportiereDaten(fibuExportDtos,
							partnerDtoFinanzamt, bInnerGemeinschaftlich,
							sPartnerUstLkz);
				} else {
					sExportDaten = exportiereDaten(fibuExportDtos,
							partnerDtoFinanzamt, bInnerGemeinschaftlich,
							sPartnerUstLkz);
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return sExportDaten;
	}

	/**
	 * exportiereDatenAR
	 * 
	 * @param fibuExportDtos
	 *            FibuexportDto[]
	 * @param partnerDtoFinanzamt
	 *            PartnerDto
	 * @param bInnerGemeinschaftlich
	 *            boolean
	 * @param sPartnerUstLkz
	 *            String
	 * @return String
	 * @throws EJBExceptionLP
	 */
	private String exportiereDaten(final FibuexportDto[] fibuExportDtos,
			final PartnerDto partnerDtoFinanzamt,
			final boolean bInnerGemeinschaftlich, final String sPartnerUstLkz)
	throws EJBExceptionLP {
		StringBuffer sb = new StringBuffer();
		// fuer BMD PR08A werden nur die Gegenbuchungen exportiert, die Sammelbauchung wird automatisch von BMD generiert 
		// siehe auch Anmerkungen unten
		int iStart;
		int iEnd;
		// bei der Gutschrift kommen die Buchungen in der umgekehrten Reihenfolge, d.h. die Sammelbuchung am Schluss PJ 16766
		if (fibuExportDtos[0].getBelegart().equals(FibuExportManager.BELEGART_GS)) {
			iStart = 0;
			iEnd = fibuExportDtos.length - 1;
		} else {
			iStart = 1;
			iEnd = fibuExportDtos.length;
		}
		for (int i = iStart; i < iEnd; i++) {
			LpMailText mt = new LpMailText();
			//--------------------------------------------------------------
			// Satzart ... ist immer 0 lt. Beschreibung
			//--------------------------------------------------------------
			mt.addParameter(P_SATZART, "0");
			//--------------------------------------------------------------
			// Konto
			//--------------------------------------------------------------
			// hier muss nun das Gegenkonto verwendet werden
			if (fibuExportDtos[i].getGegenkontoDto() != null) {
				mt.addParameter(P_KONTO, formatKontonummer(fibuExportDtos[i].getGegenkontoDto().getCNr()));
			} else {
				mt.addParameter(P_KONTO,"");
			}
			//--------------------------------------------------------------
			// Buchungsdatum = Belegdatum
			//--------------------------------------------------------------
			mt.addParameter(P_BUCHDAT, formatDatum(fibuExportDtos[i].getBelegdatum()));
			//--------------------------------------------------------------
			// Gegenkonto
			//--------------------------------------------------------------
			// hier muss nun das Konto verwendet werden!
			if (fibuExportDtos[i].getKontoDto() != null) {
				mt.addParameter(P_GKTO, formatKontonummer(fibuExportDtos[i].getKontoDto().getCNr()));
			} else {
				mt.addParameter(P_GKTO, "");
			}
			//--------------------------------------------------------------
			// Belegnummer
			//--------------------------------------------------------------
			// Belegnummer: fuer BMD ist das nur die laufende Nummer, d.h.
			// ohne Geschaeftsjahr und Trennzeichen
			String sBelegnummerOhneGJ = extractLaufendeNummerAusBelegnummer(
					fibuExportDtos[i].getBelegnummer(), theClientDto);
			if (sBelegnummerOhneGJ == null || sBelegnummerOhneGJ.equals("")) {
				// Das darf nicht passieren
				myLogger.warn(theClientDto.getIDUser(),
				"Ungueltige Belegnummer");
				mt.addParameter(P_BELEGNR, "");
			} else {
				mt.addParameter(P_BELEGNR, formatBelegnummer(sBelegnummerOhneGJ));
			}
			//--------------------------------------------------------------
			// Belegdatum
			//--------------------------------------------------------------
			mt.addParameter(P_BELEGDAT, formatDatum(fibuExportDtos[i].getBelegdatum()));
			//--------------------------------------------------------------
			// Kostenstelle
			//--------------------------------------------------------------
			if (fibuExportDtos[i].getKostenstelle() != null) {
				/**
				 * @todo MR->MR: Achtung MUSS ZAHL SEIN!!
				 */
				mt.addParameter(P_KOST, formatKostenstelle(fibuExportDtos[i].getKostenstelle()));
			} else {
				mt.addParameter(P_KOST, "");
			}
			//--------------------------------------------------------------
			// Mehrwertsteuer Prozentsatz
			//--------------------------------------------------------------
			if (fibuExportDtos[i].getMwstsatz() != null) {
				mt.addParameter(P_MWST, formatBetrag(fibuExportDtos[i].getMwstsatz().getFMwstsatz(), 3, 2));
			} else {
				mt.addParameter(P_MWST, formatBetrag(0.0, 3, 2));
			}
			//--------------------------------------------------------------
			// Steuercode
			//--------------------------------------------------------------
			String sSteuercode;
			// Eingangsrechnung
			if (fibuExportDtos[i].getBelegart().equals(FibuExportManager.BELEGART_ER)) {
				if (bInnerGemeinschaftlich) {
					if (fibuExportDtos[i].isBReverseCharge())
						if (isNTCS)
							// PJ21967
							sSteuercode = getSteuercodeReverseChargeNTCS(fibuExportDtos[i]);
						else
							sSteuercode = STEUCOD_EINKAUF_REVERSE_CHARGE;
					else
						if (isNTCS)
							sSteuercode = STEUCOD_NTCS_EINKAUF_IG_ERWERB_VORSTEUER;
						else
							sSteuercode = STEUCOD_EINKAUF_IG_ERWERB_VORSTEUER;
				} else {
					// PJ21967
					if (fibuExportDtos[i].isBReverseCharge()) {
						if (isNTCS) {
							sSteuercode = getSteuercodeReverseChargeNTCS(fibuExportDtos[i]);
						} else {
							sSteuercode = STEUCOD_EINKAUF_REVERSE_CHARGE;
						}
					} else {
						if (isNTCS)
							sSteuercode = STEUCOD_NTCS_VORSTEUER;
						else
							sSteuercode = STEUCOD_VORSTEUER_ODER_KEINE_STEUER;
					}
				}
			} else {
				// Rechnung oder Gutschrift
				if (fibuExportDtos[i].getBelegart().equals(
						FibuExportManager.BELEGART_AR) || fibuExportDtos[i].getBelegart().equals(
								FibuExportManager.BELEGART_GS)) {
					if (isNTCS)
						if (fibuExportDtos[i].getLaenderartCNr().equals(FinanzFac.LAENDERART_DRITTLAND)) {
							sSteuercode = STEUCOD_NTCS_IST_UMSATZSTEUER_DRITTLAND;
						} else if (bInnerGemeinschaftlich) {
							sSteuercode = STEUCOD_NTCS_IG_LIEFERUNG;
						} else {
							sSteuercode = STEUCOD_NTCS_IST_UMSATZSTEUER;
						}
					else
						if (bInnerGemeinschaftlich) {
							sSteuercode = STEUCOD_IG_LIEFERUNG;
						} else {
							sSteuercode = STEUCOD_IST_UMSATZSTEUER;
						}
				} else {
					if (isNTCS)
						if (fibuExportDtos[i].getLaenderartCNr().equals(FinanzFac.LAENDERART_DRITTLAND)) {
							sSteuercode = STEUCOD_NTCS_SOLL_UMSATZSTEUER_DRITTLAND;
						} else {
							sSteuercode = STEUCOD_NTCS_SOLL_UMSATZSTEUER;
						}
					else
						sSteuercode = STEUCOD_SOLL_UMSATZSTEUER;
				}
			}
			mt.addParameter(P_STEUCOD, sSteuercode);
			//--------------------------------------------------------------
			// Eroeffnungsbuchung Kennzeichen - ist immer "0"
			//--------------------------------------------------------------
			//mt.addParameter(P_EBKENNZ, "0");
			//--------------------------------------------------------------
			// Buchungscode
			//--------------------------------------------------------------
			//hier nun den Buchungscode umdrehen
			if (fibuExportDtos[i].getSollbetragBD() != null) {
				mt.addParameter(P_BUCOD, BUCOD_HABEN);
			} else {
				mt.addParameter(P_BUCOD, BUCOD_SOLL);
			}
			//--------------------------------------------------------------
			// Betrag
			//--------------------------------------------------------------
			//und das Vorzeichen umkehren
			//PJ 16331 Betrag ist Brutto
			//PJ 17318 bei IG Erwerb ist der Betrag Netto
			BigDecimal betragBrutto;
			if (fibuExportDtos[i].getSollbetragBD() != null) {
				if (bInnerGemeinschaftlich)
					betragBrutto = fibuExportDtos[i].getSollbetragBD();
				else
					betragBrutto = fibuExportDtos[i].getSollbetragBD().add(fibuExportDtos[i].getSteuerBD());
				mt.addParameter(P_BETRAG, formatBetrag(betragBrutto.negate(), 15, 2));
			} else {
				if (bInnerGemeinschaftlich)
					betragBrutto = fibuExportDtos[i].getHabenbetragBD();
				else
					betragBrutto = fibuExportDtos[i].getHabenbetragBD().add(fibuExportDtos[i].getSteuerBD());
				mt.addParameter(P_BETRAG, formatBetrag(betragBrutto, 15, 2));
			}
			//--------------------------------------------------------------
			// Steuer
			//--------------------------------------------------------------
			if (fibuExportDtos[i].getBelegart().equals(
					FibuExportManager.BELEGART_AR) || fibuExportDtos[i].getBelegart().equals(
							FibuExportManager.BELEGART_GS)) {
				mt.addParameter(P_STEUER, formatBetrag(fibuExportDtos[i].getSteuerBD().negate(), 15, 2));
			} else {
				if (bInnerGemeinschaftlich)
					// die IG Ust/Vst bucht negativ!
					mt.addParameter(P_STEUER, formatBetrag(fibuExportDtos[i].getSteuerBD().negate(), 15, 2));
				else
					mt.addParameter(P_STEUER, formatBetrag(fibuExportDtos[i].getSteuerBD(), 15, 2));
			}
			//--------------------------------------------------------------
			// Skonto
			//--------------------------------------------------------------
			mt.addParameter(P_SKONTO, formatBetrag(0.0, 15, 2));
			//--------------------------------------------------------------
			// OP Betrag
			// Dieser Wert gibt an, wieviel vom Buchungsbetrag noch "offen"
			// ist. In den meisten Faellen ist der OP-Betrag mit dem
			// Buchungsbetrag ident und kann nach den glei-chen Kriterien
			// erfaszt werden.
			//--------------------------------------------------------------
			/*
			if (fibuExportDtos[i].getSollbetragBD() != null) {
				mt.addParameter(P_OPBETRAG, formatBetrag(fibuExportDtos[i]
				                                                        .getSollbetragBD(), 15, 2));
			} else {
				mt.addParameter(P_OPBETRAG, formatBetrag(fibuExportDtos[i]
				                                                        .getHabenbetragBD(), 15, 2));
			}*/
			//--------------------------------------------------------------
			// Periode
			// Hier wird die Buchungsperiode uebergeben (01 bis 13).
			// PJ 17006 bei der ER wird jetzt der Stichtag verwendet (= Valutadatum im ExportDto)
			//--------------------------------------------------------------
			Calendar c = Calendar.getInstance();
			if (fibuExportDtos[i].getBelegart().equals(FibuExportManager.BELEGART_ER)) {
				c.setTimeInMillis(fibuExportDtos[i].getValutadatum().getTime());
			} else {
				c.setTimeInMillis(fibuExportDtos[i].getBelegdatum().getTime());
			}
			
			mt.addParameter(P_PERIODE, formatBetrag(c.get(Calendar.MONTH)+1, 2, 0));
			
			//--------------------------------------------------------------
			// Fremdwaehrung (nur bei NTCS)
			if (isNTCS)
				mt.addParameter(P_WAEHRUNG, fibuExportDtos[i].getFremdwaehrung());
			
			//--------------------------------------------------------------
			// FW Kurs
			// Der Fremdwaehrungskurs musz nur bei FW-Buchungen uebergeben
			// werden. Er dient zur ATS-Umrechnung der Betraege und kann mit
			// 8 Vor- und 5 Nachkommastellen er-faszt werden.
			//--------------------------------------------------------------
			// Da wir 6 Stellen verwenden, wird das Komma um eine Stelle
			// nach links geschoben.
			// Das wird durch den FW-Faktor dann wieder ausgeglichen.
			//
			// Achtung: laut neuer Doku kann BMD jetzt 8 Vor- und 8 Nachkommastellen
			// Es wird daher mit Faktor 1 exportiert
			
			mt.addParameter(P_FWKURS, formatBetrag(fibuExportDtos[i].getKurs(), 7, 6));
			
			//--------------------------------------------------------------
			// FW Faktor
			//--------------------------------------------------------------
			mt.addParameter(P_FWFAKTOR, "1"); // siehe FWKURS
			
			//--------------------------------------------------------------
			// FW Betrag
			//--------------------------------------------------------------
			
			//und das Vorzeichen umkehren
			//FW Betrag ist Brutto
			//bei IG Erwerb ist der FW Betrag Netto
			BigDecimal fwbetragBrutto;
			if (fibuExportDtos[i].getSollbetragFWBD() != null) {
				if (bInnerGemeinschaftlich)
					fwbetragBrutto = fibuExportDtos[i].getSollbetragFWBD();
				else
					fwbetragBrutto = fibuExportDtos[i].getSollbetragFWBD().add(fibuExportDtos[i].getSteuerFWBD());
				mt.addParameter(P_FWBETRAG, formatBetrag(fwbetragBrutto.negate(), 15, 2));
			} else {
				if (bInnerGemeinschaftlich)
					fwbetragBrutto = fibuExportDtos[i].getHabenbetragFWBD();
				else
					fwbetragBrutto = fibuExportDtos[i].getHabenbetragFWBD().add(fibuExportDtos[i].getSteuerFWBD());
				mt.addParameter(P_FWBETRAG, formatBetrag(fwbetragBrutto, 15, 2));
			}

			//--------------------------------------------------------------
			// Steuer
			//--------------------------------------------------------------
			if (fibuExportDtos[i].getBelegart().equals(
					FibuExportManager.BELEGART_AR) || fibuExportDtos[i].getBelegart().equals(
							FibuExportManager.BELEGART_GS)) {
				mt.addParameter(P_FWSTEUER, formatBetrag(fibuExportDtos[i].getSteuerFWBD().negate(), 15, 2));
			} else {
				BigDecimal fwbdsteuer = new BigDecimal(0);
				if (fibuExportDtos[i].getSteuerFWBD() != null)
					fwbdsteuer = fibuExportDtos[i].getSteuerFWBD();
				if (bInnerGemeinschaftlich)
					// die IG Ust/Vst bucht negativ!
					mt.addParameter(P_FWSTEUER, formatBetrag(fwbdsteuer.negate(), 15, 2));
				else
					mt.addParameter(P_FWSTEUER, formatBetrag(fwbdsteuer, 15, 2));
			}
			
			//--------------------------------------------------------------
			// Skonto
			//--------------------------------------------------------------
			mt.addParameter(P_FWSKONTO, formatBetrag(0.0, 15, 2));
			
			//--------------------------------------------------------------
			// OP Betrag
			// Dieser Wert gibt an, wieviel vom Buchungsbetrag noch "offen"
			// ist. In den meisten Faellen ist der OP-Betrag mit dem
			// Buchungsbetrag ident und kann nach den glei-chen Kriterien
			// erfaszt werden.
			//--------------------------------------------------------------
			/*
			if (fibuExportDtos[i].getSollbetragFWBD() != null) {
				mt.addParameter(P_FWOPBETRAG, formatBetrag(
						fibuExportDtos[i].getSollbetragFWBD(), 15, 2));
			} else {
				mt.addParameter(P_FWOPBETRAG, formatBetrag(
						fibuExportDtos[i].getHabenbetragFWBD(), 15, 2));
			}
			*/
			//--------------------------------------------------------------
			// Laenderkennzeichen
			//--------------------------------------------------------------
			//mt.addParameter(P_LANDKZ, uebersetzeUSTLand(sPartnerUstLkz));
			//--------------------------------------------------------------
			// LKZ Kurs
			//--------------------------------------------------------------
			// Da wir 6 Stellen verwenden, wird das Komma um eine Stelle
			// nach links geschoben.
			// Das wird durch den FW-Faktor dann wieder ausgeglichen.
			//mt.addParameter(P_LKZKURS, formatBetrag(fibuExportDtos[i]
			//		.getKurs(), 7, 6));
			//--------------------------------------------------------------
			// LKZ Faktor
			//--------------------------------------------------------------
			//mt.addParameter(P_LKZFAKTOR, "10"); // siehe LKZKURS
			//--------------------------------------------------------------
			// Text (18)
			// In diesem Feld wird der Buchungstext uebergeben. Im Gegensatz
			// zu numerischen Feldern sollten Sie den Buchungstext von links
			// nach rechts ausrichten und die rest-lichen Stellen mit
			// Leerzeichen ausfuellen (zB
			// AR125\uF0C8\uF0C8\uF0C8\uF0C8\uF0C8\uF0C8
			// \uF0C8\uF0C8\uF0C8\uF0C8\uF0C8\uF0C8\uF0C8 fuer AR125; \uF0C8
			// = Leerzeichen).
			//--------------------------------------------------------------
			//Text immer aus erstem Export Eintrag
			// SP4720 Wir haben variablen Satzaufbau. Helper.cutString() macht
			// einen trim(), deswegen sind die hier angegebenen Leerzeichen
			// sowieso wieder weg. Die anderen .cutString() Verwendungen in 
			// dieser Klasse werden nicht weiter modifizert.
			String t = "";
			if (fibuExportDtos[i].getText() != null) {
//				t = Helper.cutString(fibuExportDtos[i].getText() + "                    ", 18);
				t = Helper.cutString(fibuExportDtos[i].getText(), 72);
				t = t.replace(";", ",");	// alle ; ersetzen da dies das Trennzeichen
			}
			mt.addParameter(P_TEXT, t);

			//PJ 16322 zusaetzliche Felder
			// Lieferantname
			// Lieferantkurzbezeichnung
			mt.addParameter(P_PARTNER_KBZ, fibuExportDtos[i].getPartnerDto().getCKbez());
			
			//PJ 22094 zusetzliche Felder
			// Name1
			mt.addParameter(P_PARTNER_NAME1, fibuExportDtos[i].getPartnerDto().getCName1nachnamefirmazeile1());
			
			//--------------------------------------------------------------
			// Buchungs-Symbol
			// Dieser Wert gibt an, unter welchem Buchungssymbol die
			// Rechnung verbucht wurde (zB AR, ER, BK,...).
			//--------------------------------------------------------------
			mt.addParameter(P_SYMBOL, fibuExportDtos[i].getBelegart());
			//--------------------------------------------------------------
			// Externe Belegnummer
			//--------------------------------------------------------------
			mt.addParameter(P_EXTBELEGNR, Helper.cutString(
					fibuExportDtos[i].getSExterneBelegnummer()
					+ "                 ", 12));
			//--------------------------------------------------------------
			// Zahlungsziel
			// Nettozahlungsziel in Tagen; zB 000030 (dieses braucht nur im
			// Falle einer Abwei-chung vom Kontenstamm angegeben werden). Im
			// Normalfall wird auf die Zahlungs-konditionen des
			// Kontenstammes zurueckgegriffen.
			//--------------------------------------------------------------
			int iNettotage = 0;
			if (fibuExportDtos[i].getZahlungszielDto() != null
					&& fibuExportDtos[i].getZahlungszielDto().getAnzahlZieltageFuerNetto() != null) {
				iNettotage = fibuExportDtos[i].getZahlungszielDto().getAnzahlZieltageFuerNetto();
			}
			mt.addParameter(P_ZZIEL, formatBetrag(iNettotage, 6, 0));
			//--------------------------------------------------------------
			// Skontoprozentsatz
			//--------------------------------------------------------------
			BigDecimal bdSkontoProzentsatz1 = new BigDecimal(0);
			if (fibuExportDtos[i].getZahlungszielDto() != null
					&& fibuExportDtos[i].getZahlungszielDto().getSkontoProzentsatz1() != null) {
				bdSkontoProzentsatz1 = fibuExportDtos[i].getZahlungszielDto().getSkontoProzentsatz1();
			}
			if (bdSkontoProzentsatz1.intValue() == 0) {
				mt.addParameter(P_SKONTOPZ, "99");
			} else {
				mt.addParameter(P_SKONTOPZ, formatBetrag(bdSkontoProzentsatz1, 3, 2));
			}
			//--------------------------------------------------------------
			// Skontotage
			//--------------------------------------------------------------
			int iSkontotage1 = 0;
			if (fibuExportDtos[i].getZahlungszielDto() != null
					&& fibuExportDtos[i].getZahlungszielDto().getSkontoAnzahlTage1() != null) {
				iSkontotage1 = fibuExportDtos[i].getZahlungszielDto().getSkontoAnzahlTage1();
			}
			mt.addParameter(P_SKONTOTAGE, formatBetrag(iSkontotage1, 4, 0));
			//--------------------------------------------------------------
			// Skontoprozentsatz 2
			//--------------------------------------------------------------
			BigDecimal bdSkontoProzentsatz2 = new BigDecimal(0);
			if (fibuExportDtos[i].getZahlungszielDto() != null
					&& fibuExportDtos[i].getZahlungszielDto().getSkontoProzentsatz2() != null) {
				bdSkontoProzentsatz2 = fibuExportDtos[i].getZahlungszielDto().getSkontoProzentsatz2();
			}
			if (bdSkontoProzentsatz2.intValue() == 0) {
				mt.addParameter(P_SKONTOPZ2, "99");
			} else {
				mt.addParameter(P_SKONTOPZ2, formatBetrag(bdSkontoProzentsatz2, 3, 2));
			}
			//--------------------------------------------------------------
			// Skontotage 2
			//--------------------------------------------------------------
			int iSkontotage2 = 0;
			if (fibuExportDtos[i].getZahlungszielDto() != null
					&& fibuExportDtos[i].getZahlungszielDto().getSkontoAnzahlTage2() != null) {
				iSkontotage2 = fibuExportDtos[i].getZahlungszielDto().getSkontoAnzahlTage2();
			}
			mt.addParameter(P_SKONTOTAGE2, formatBetrag(iSkontotage2, 4, 0));
			//--------------------------------------------------------------
			// Vertreter - 6 stellige Vertreternummmer
			// derzeit nicht unterstuetzt
			//--------------------------------------------------------------
			//mt.addParameter(P_VERTNR, "000000");
			//--------------------------------------------------------------
			// Vertreterprovisision (Prozentsatz)
			// derzeit nicht unterstuetzt
			//--------------------------------------------------------------
			//mt.addParameter(P_VERTNR, formatBetrag(0, 3, 2));
			//--------------------------------------------------------------
			//
			//--------------------------------------------------------------
			//ParametermandantDto parameter = getParameterFac()
			//		.getMandantparameter(
			//				theClientDto.getMandant(),
			//				ParameterFac.KATEGORIE_FINANZ,
			//				ParameterFac.PARAMETER_FINANZ_EXPORT_BMD_BENUTZERNUMMER);
			//mt.addParameter(P_BENUTZER, parameter.getCWert());
			//--------------------------------------------------------------
			// Buchungsart fix auf 00
			//--------------------------------------------------------------
			//mt.addParameter(P_BUCHART, "00");
			//--------------------------------------------------------------
			// Buchungskennzeichen fix auf 00
			//--------------------------------------------------------------
			//mt.addParameter(P_BUCHKZ, "00");
			//--------------------------------------------------------------
			// Mahnzaehler: Wie oft wurde die Rechnung bereits gemahnt?
			//--------------------------------------------------------------
			//mt.addParameter(P_MAHNZ, "0000");
			//--------------------------------------------------------------
			// Gegenbuchungskennzeichen (alphanum., 1 Zeichen)
			// Dieses Kennzeichen wird nur benoetigt, wenn das
			// Verbuchungskennzeichen auf A gesetzt wurde. Es gibt an, wie
			// die Gegenbuchung erfolgen soll:
			// E Einzelgegenbuchung
			// S Sammelgegenbuchung
			// O Ohne Gegenbuchung
			//--------------------------------------------------------------
			/*
			String sKz;
			if (fibuExportDtos.length <= 1) {
				sKz = GEGENBUCHUNGSKENNZEICHEN_OHNE_GEGENBUCHUNG;
			} else if (fibuExportDtos.length == 2) {
				sKz = GEGENBUCHUNGSKENNZEICHEN_EINZELGEGENBUCHUNG;
			} else {
				if (i == 0) {
					sKz = GEGENBUCHUNGSKENNZEICHEN_EINZELGEGENBUCHUNG;
				} else {
					sKz = GEGENBUCHUNGSKENNZEICHEN_SAMMELGEGENBUCHUNG;
				}
			}
			mt.addParameter(P_GEGENBUCHKZ, sKz); */
			//--------------------------------------------------------------
			// Verbuchungskennzeichen (alphanum., 1 Zeichen)
			// Dieses Kennzeichen regelt, mit welchem Programm die
			// BUERF-Datei verbucht wird:
			// A Verbuchung mit PR08A (mit automat. Gegen- und
			// Sammelbuchung)
			// (leer) Verbuchung mit dem Programm PR08
			//--------------------------------------------------------------
			mt.addParameter(P_VERBUCHKZ, "A");
			
			//--------------------------------------------------------------
			// exportieren
			//--------------------------------------------------------------
			String sZeile = mt.transformText(FinanzReportFac.REPORT_MODUL,
					theClientDto.getMandant(), getXSLFile(), theClientDto.getLocMandant(),
					theClientDto);
			sb.append(sZeile).append(Helper.CR_LF);
		}
		return sb.toString();
	}

	private String getSteuercodeReverseChargeNTCS(FibuexportDto expDto) {
		String sSteuercode = STEUCOD_NTCS_EINKAUF_REVERSE_CHARGE;
		if(expDto.getReversechargeartId() != null) {
			ReversechargeartDto rcartDto = reversechargeartCache.getValueOfKey(expDto.getReversechargeartId());
			if (rcartDto.getCNr().equals(ReversechargeArt.SCHROTT)) {
				sSteuercode = STEUCOD_NTCS_EINKAUF_REVERSE_CHARGE_SCHROTT_19_1d;
			} else if (rcartDto.getCNr().equals(ReversechargeArt.BAULEISTUNG)) {
				sSteuercode = STEUCOD_NTCS_EINKAUF_REVERSE_CHARGE_Bauleistung_19_1a_;
			}
		}
		return sSteuercode;
	}
	
	protected String getXSLFile() {
		return XSL_FILE_BMD;
	}

	protected String exportiereUeberschrift() throws EJBExceptionLP {
		LpMailText mt = new LpMailText();
		mt.addParameter(P_SATZART, P_SATZART);
		mt.addParameter(P_KONTO, P_KONTO);
		mt.addParameter(P_BUCHDAT, P_BUCHDAT);
		mt.addParameter(P_GKTO, P_GKTO);
		mt.addParameter(P_BELEGNR, P_BELEGNR);
		mt.addParameter(P_BELEGDAT, P_BELEGDAT);
		mt.addParameter(P_KOST, P_KOST);
		mt.addParameter(P_MWST, P_MWST);
		mt.addParameter(P_STEUCOD, P_STEUCOD);
		//mt.addParameter(P_EBKENNZ, P_EBKENNZ);
		mt.addParameter(P_BUCOD, P_BUCOD);
		mt.addParameter(P_BETRAG, P_BETRAG);
		mt.addParameter(P_STEUER, P_STEUER);
		mt.addParameter(P_SKONTO, P_SKONTO);
		//mt.addParameter(P_OPBETRAG, P_OPBETRAG);
		mt.addParameter(P_PERIODE, P_PERIODE);
		if (isNTCS)
			mt.addParameter(P_WAEHRUNG, P_WAEHRUNG);
		mt.addParameter(P_FWKURS, P_FWKURS);
		mt.addParameter(P_FWFAKTOR, P_FWFAKTOR);
		mt.addParameter(P_FWBETRAG, P_FWBETRAG);
		mt.addParameter(P_FWSTEUER, P_FWSTEUER);
		mt.addParameter(P_FWSKONTO, P_FWSKONTO);
		//mt.addParameter(P_FWOPBETRAG, P_FWOPBETRAG);
		//mt.addParameter(P_LANDKZ, P_LANDKZ);
		//mt.addParameter(P_LKZKURS, P_LKZKURS);
		//mt.addParameter(P_LKZFAKTOR, P_LKZFAKTOR);
		mt.addParameter(P_TEXT, P_TEXT);
		mt.addParameter(P_PARTNER_KBZ, P_TEXT);
		mt.addParameter(P_PARTNER_NAME1, P_TEXT);
		mt.addParameter(P_SYMBOL, P_SYMBOL);
		mt.addParameter(P_EXTBELEGNR, P_EXTBELEGNR);
		mt.addParameter(P_ZZIEL, P_ZZIEL);
		mt.addParameter(P_SKONTOPZ, P_SKONTOPZ);
		mt.addParameter(P_SKONTOTAGE, P_SKONTOTAGE);
		mt.addParameter(P_SKONTOPZ2, P_SKONTOPZ2);
		mt.addParameter(P_SKONTOTAGE2, P_SKONTOTAGE2);
		//mt.addParameter(P_VALUTADATUM, P_VALUTADATUM);
		//mt.addParameter(P_VERTNR, P_VERTNR);
		//mt.addParameter(P_PROVPZ, P_PROVPZ);
		//mt.addParameter(P_BENUTZER, P_BENUTZER);
		//mt.addParameter(P_BUCHART, P_BUCHART);
		//mt.addParameter(P_BUCHKZ, P_BUCHKZ);
		//mt.addParameter(P_MAHNZ, P_MAHNZ);
		//mt.addParameter(P_GEGENBUCHKZ, P_GEGENBUCHKZ);
		mt.addParameter(P_VERBUCHKZ, P_VERBUCHKZ);
		String sZeile = mt.transformText(FinanzReportFac.REPORT_MODUL,
				theClientDto.getMandant(), getXSLFile(), theClientDto
				.getLocMandant(), theClientDto);
		return sZeile;
	}

	/**
	 * Eine Kontonummer fuer BMD formatieren. Die Kontonummer kann neunstellig
	 * erfasst werden. Fehlende Stellen k&ouml;nnen mit fuehrenden Nullen (zB
	 * 000202001) ergaenzt werden.
	 * 
	 * @param sKontonummer
	 *            Kontonummer
	 * @return String
	 */
	private static String formatKontonummer(String sKontonummer) {
		Integer iKontonummer = new Integer(sKontonummer);
		NumberFormat nf = NumberFormat.getIntegerInstance();
		nf.setMaximumIntegerDigits(9);
		nf.setMinimumIntegerDigits(9);
		nf.setGroupingUsed(false);
		return nf.format(iKontonummer.intValue());
	}

	/**
	 * Eine Belegnummer fuer BMD formatieren. Belegnummer (zB 000000135 =
	 * Belegnummer 135).
	 * 
	 * @param sBelegnummer
	 *            Belegnummer
	 * @return String
	 */
	private static String formatBelegnummer(String sBelegnummer) {
		Integer iBelegnummer = new Integer(sBelegnummer);
		NumberFormat nf = NumberFormat.getIntegerInstance();
		nf.setMaximumIntegerDigits(9);
		//nf.setMinimumIntegerDigits(9);
		nf.setGroupingUsed(false);
		return nf.format(iBelegnummer.intValue());
	}

	/**
	 * Eine Kostenstellennummer fuer BMD formatieren. Belegnummer (zB 000000135
	 * = Belegnummer 135).
	 * 
	 * @param sKSTnummer
	 *            Nummer der Kostenstelle
	 * @return String
	 */
	private static String formatKostenstelle(String sKSTnummer) {
		if (Helper.istStringNumerisch(sKSTnummer)) {
			Integer iKSTnummer = new Integer(sKSTnummer);
			NumberFormat nf = NumberFormat.getIntegerInstance();
			nf.setMaximumIntegerDigits(9);
			//nf.setMinimumIntegerDigits(9);
			nf.setGroupingUsed(false);
			return nf.format(iKSTnummer.intValue());
		} else {
			return "";
		}
	}

	/**
	 * Datum fuer BMD formatieren. Hier kann das Buchungsdatum im Format
	 * JJJJMMTT (zB 19980501) angegeben werden.
	 * 
	 * @param d
	 *            Date
	 * @return String
	 */
	private static String formatDatum(java.util.Date d) {
		if (d != null) {
			DateFormat df = new SimpleDateFormat("yyyyMMdd");
			return df.format(d);
		} else {
			return "";
		}
	}

	/**
	 * Betrag fuer BMD formatieren.
	 * 
	 * @param nBetrag
	 *            Betrag
	 * @param iVorkommastellen
	 *            int
	 * @param iNachkommastellen
	 *            int
	 * @return String
	 */
	private static String formatBetrag(Number nBetrag, int iVorkommastellen,
			int iNachkommastellen) {
		if (nBetrag != null) {
			DecimalFormat df = new DecimalFormat();
			df.setMinimumFractionDigits(iNachkommastellen);
			df.setMaximumFractionDigits(iNachkommastellen);
			//df.setMinimumIntegerDigits(iVorkommastellen);
			df.setMaximumIntegerDigits(1);
			df.setMaximumIntegerDigits(iVorkommastellen);
			df.setGroupingUsed(false);
			df.setDecimalSeparatorAlwaysShown(false);
			return df.format(nBetrag.doubleValue());
		} else {
			return "";
		}
	}

	protected String uebersetzeUSTLand(String sLKZ) {
		String sUSTLand = "";
		// der Partner muss immer definiert sein
		if (sLKZ != null) {
			if (sLKZ.equalsIgnoreCase(SystemFac.LKZ_OESTERREICH)) {
				sUSTLand = "0001";
			} else if (sLKZ.equalsIgnoreCase("D")) {
				sUSTLand = "0002";
			} else if (sLKZ.equalsIgnoreCase("B")) { // Belgien
				sUSTLand = "0015";
			} else if (sLKZ.equalsIgnoreCase("DK")) { // Daenemark
				sUSTLand = "0013";
			} else if (sLKZ.equalsIgnoreCase("F")) { // Frankreich
				sUSTLand = "0017";
			} else if (sLKZ.equalsIgnoreCase("GR")) { // Griechenland
				sUSTLand = "0032";
			} else if (sLKZ.equalsIgnoreCase("GB")) { // Grossbritannien
				sUSTLand = "0043";
			} else if (sLKZ.equalsIgnoreCase("IRL")) { // Irland
				sUSTLand = "0044";
			} else if (sLKZ.equalsIgnoreCase("I")) { // Italien
				sUSTLand = "0005";
			} else if (sLKZ.equalsIgnoreCase("FIN")) { // Finnland
				sUSTLand = "0040";
			} else if (sLKZ.equalsIgnoreCase("L")) { // Luxemburg
				sUSTLand = "0016";
			} else if (sLKZ.equalsIgnoreCase("NL")) { // Niederlande
				sUSTLand = "0014";
			} else if (sLKZ.equalsIgnoreCase("P")) { // Portugal
				sUSTLand = "0019";
			} else if (sLKZ.equalsIgnoreCase("S")) { // Schweden
				sUSTLand = "0041";
			} else if (sLKZ.equalsIgnoreCase("E")) { // Spanien
				sUSTLand = "0018";
			} else if (sLKZ.equalsIgnoreCase("H")) { // Ungarn
				sUSTLand = "0007";
			} else if (sLKZ.equalsIgnoreCase("CZ")) { // Tschechien
				sUSTLand = "0009";
			} else if (sLKZ.equalsIgnoreCase("CH")) { // Schweiz
				sUSTLand = "0003";
			} else if (sLKZ.equalsIgnoreCase("USA")) { // USA
				sUSTLand = "0302";
			}
		}
		return sUSTLand;
	}

	private class ReversechargeartCache extends HvCreatingCachingProvider<Integer, ReversechargeartDto> {
		@Override
		protected ReversechargeartDto provideValue(Integer key, Integer transformedKey) {
			try {
				return getFinanzServiceFac()
					.reversechargeartFindByPrimaryKey(key, theClientDto);
			} catch(RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
				return null;				
			}
		}		
	}

}
