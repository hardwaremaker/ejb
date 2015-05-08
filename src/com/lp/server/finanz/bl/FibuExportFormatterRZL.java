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
import java.util.ArrayList;
import java.util.HashMap;

import com.lp.server.finanz.service.FibuExportKriterienDto;
import com.lp.server.finanz.service.FibuexportDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzReportFac;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.LpMailText;
import com.lp.util.AddableHashMap;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * Diese Klasse kuemmert sich um ...
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 09.02.06
 * </p>
 * 
 * <p>
 * 
 * @author $Author: adi $
 *         </p>
 * 
 *         Offene Fragen: ist LAENDERART_EU_AUSLAND_OHNE_UID immer wie Inland zu
 *         behandeln?
 * 
 * @version not attributable Date $Date: 2012/06/26 15:42:00 $
 */
public class FibuExportFormatterRZL extends FibuExportFormatter {

	private final static String P_KONTONUMMER = "Kontonummer";
	private final static String P_GEGENKONTO = "Gegenkonto";
	private final static String P_OP_NUMMER = "OP-Nummer";
	private final static String P_BELEGDATUM = "Beleg-Datum";
	private final static String P_VALUTADATUM = "Valuta-Datum";
	private final static String P_WAEHRUNG = "Waehrung";
	private final static String P_SOLLBETRAG = "Sollbetrag";
	private final static String P_HABENBETRAG = "Habenbetrag";
	private final static String P_STEUERBETRAG = "Steuerbetrag";
	private final static String P_FREMDWAEHRUNG = "Fremdwaehrung";
	private final static String P_FREMDWAEHRUNG_SOLLBETRAG = "Fremdwaehrung-Sollbetrag";
	private final static String P_FREMDWAEHRUNG_HABENBETRAG = "Fremdwaehrung-Habenbetrag";
	private final static String P_KOSTENSTELLE = "Kostenstelle";
	private final static String P_BELEGKREIS = "Belegkreis";
	private final static String P_BELEGNUMMER = "Belegnummer";
	private final static String P_UST_LAND = "Ust-Land";
	private final static String P_UST_PROZENTSATZ = "Ust-Prozentsatz";
	private final static String P_UST_CODE = "Ust-Code";
	private final static String P_SONDERCODE = "Sondercode";
	private final static String P_BUCHUNGSART = "Buchungsart";
	private final static String P_ABWEICHENDE_ZAHLUNGSFRIST = "AbweichendeZahlungsfrist";
	private final static String P_ABWEICHENDE_SKONTOFRIST = "AbweichendeSkontofrist";
	private final static String P_ABWEICHENDER_SKONTO_PROZENTSATZ = "AbweichenderSkontoprozentsatz";
	private final static String P_BUCHUNGSTEXT = "Buchungstext";
	private final static String P_BUCHUNGSTEXT_ZEILE_2 = "Buchungstext2";
	private final static String P_UID_NUMMER = "UID-Nummer";

	private final static String UST_CODE_KEINE_STEUER = "0";
	private final static String UST_CODE_VORSTEUER = "1";
	private final static String UST_CODE_MWST = "2";
	private final static String UST_CODE_ERWERBSSTEUER = "3";
	private final static String UST_CODE_EINFUHRUMSATZSTEUER = "11";

	private final static String SONDERCODE_KEIN_SONDERFALL_HINSICHTLICH_UST = "00";
	private final static String SONDERCODE_IG_LOHNVEREDELUNG = "01";
	private final static String SONDERCODE_IG_LIEFERUNG_NEUER_FAHRZEUGE_AN_ABNEHMER_OHNE_UID = "02";
	private final static String SONDERCODE_DREIECKSGESCHAEFT = "03";
	private final static String SONDERCODE_IG_ERWERB_OHNE_VORSTEUERABZUG = "04";
	private final static String SONDERCODE_UEBERNOMMENE_UST_GEM_PARAGRAPH_19_MIT_VORSTEUERABZUG = "05";
	private final static String SONDERCODE_UEBERNOMMENE_UST_GEM_PARAGRAPH_19_OHNE_VORSTEUERABZUG = "06";
	private final static String SONDERCODE_UEBERNOMMENE_UST_GEM_ART_25_ABS_5_MIT_VORSTEUERABZUG = "07";
	private final static String SONDERCODE_UEBERNOMMENE_UST_GEM_ART_25_ABS_5_OHNE_VORSTEUERABZUG = "08";
	private final static String SONDERCODE_ANZAHLUNGSRECHNUNG = "09";
	private final static String SONDERCODE_ERHALTENE_ANZAHLUNG = "10";

	private final static String UST_PROZENTSATZ_EXPORT_IN_NICHT_EU_LAENDER = "01";
	private final static String UST_PROZENTSATZ_IG_LIEFERUNG_IN_EIN_EU_LAND = "02";
	private final static String UST_PROZENTSATZ_IG_LEISTUNG_IN_EIN_EU_LAND = "03";

	private final static String BUCHUNGSART_OHNE_GEGENBUCHUNG = "0";
	private final static String BUCHUNGSART_MIT_GEGENBUCHUNG = "1";
	private final static String BUCHUNGSART_MIT_SAMMELBUCHUNG = "2";
	private final static String BUCHUNGSART_SPLITBUCHUNG = "3";
	private final static String BUCHUNGSART_SPLITSAMMELBUCHUNG = "4";
	private final static String BUCHUNGSART_SAMMELBUCHUNG = "5";

	private final static int KEY_SOLL = 1;
	private final static int KEY_HABEN = 2;
	private final static int KEY_UST = 3;

	private HashMap<String, String> ustCodeCache = new HashMap<String, String>();

	protected FibuExportFormatterRZL(FibuExportKriterienDto exportKriterienDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		super(exportKriterienDto, theClientDto);
	}

	/**
	 * 
	 * 
	 * @param fibuExportDtos
	 *            FibuexportDto[]
	 * @return String
	 * @throws EJBExceptionLP
	 */
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
				final boolean bFinanzamtImPartnerLand = isFinanzamtImPartnerland(
						sPartnerUstLkz, partnerDtoFinanzamt);
				// feststellen, ob es sich um IG Erwerb / Erloes handelt
				final boolean bInnerGemeinschaftlich = isInnergemeinschaftlich(
						fibuExportDtos, bFinanzamtImPartnerLand);
				// Trennung zwischen ER und AR bzw. GS
				if (fibuExportDtos[0].getBelegart().equals(
						FibuExportManager.BELEGART_ER)) {
					sExportDaten = exportiereDatenER(fibuExportDtos,
							partnerDtoFinanzamt, bInnerGemeinschaftlich,
							sPartnerUstLkz);
				} else {
					sExportDaten = exportiereDatenAR(fibuExportDtos,
							partnerDtoFinanzamt, bInnerGemeinschaftlich,
							sPartnerUstLkz);
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return sExportDaten;
	}

	private String exportiereDatenER(final FibuexportDto[] fibuExportDtos,
			final PartnerDto partnerDtoFinanzamt,
			final boolean bInnerGemeinschaftlich, final String sPartnerUstLkz)
			throws EJBExceptionLP {
		StringBuffer sb = null;
		AddableHashMap<Integer, BigDecimal> hmSalden = new AddableHashMap<Integer, BigDecimal>();
		sb = new StringBuffer();
		// wegen dem xsl-file muss ich das zeilenweise machen - als fuer jeden
		// datensatz
		for (int i = 0; i < fibuExportDtos.length; i++) {
			LpMailText mt = new LpMailText();
			exportiereSpalten_1_bis_6(fibuExportDtos[i], mt);
			//------------------------------------------------------------------
			// --
			// 29.08.07 fuer Kunde: bei ER an EU-Lief. mit nicht gleichem
			// Finanzamt-LKZ
			// zieht der theoretische Inlands-Mwst-Satz. Der muss jetzt schon
			// aus
			// den spaeter folgenden Buchungszeilen ermittelt werden, damit es
			// keine Saldendifferenzen gibt.
			//------------------------------------------------------------------
			// --
			if (i > 0) { // Wirkt nur auf die erste Buchungszeile
				if (bInnerGemeinschaftlich || fibuExportDtos[i].isBReverseCharge()) {
					BigDecimal bdSteuerbetragTheoretisch = fibuExportDtos[i].getSteuerBD().abs() ;// new BigDecimal(0);
					// for (int j = 0; j < fibuExportDtos.length; j++) {
					// BigDecimal bdUSTProzentsatz =
					// getMehrwertsteuersatzDesMandanten();
					// // Komma 2 Stellen nach links verschieben
					// bdUSTProzentsatz = bdUSTProzentsatz.movePointLeft(2);
					// if (fibuExportDtos[j].getSollbetragBD() != null) {
					// // Steuerbetrag berechnen und runden.
					// bdSteuerbetragTheoretisch =
					// bdSteuerbetragTheoretisch.add(Helper.
					// rundeKaufmaennisch(fibuExportDtos[j].getSollbetragBD().
					// multiply(
					// bdUSTProzentsatz), FinanzFac.NACHKOMMASTELLEN));
					// }
					// }
					// lt. Hrn Trauner (RZL) 31.08.07 muessen bei IG Erwerb Soll
					// und Haben brutto sein.
					if (fibuExportDtos[i].getHabenbetragBD() != null) {
						fibuExportDtos[i].setHabenbetrag(fibuExportDtos[i]
								.getHabenbetragBD().add(
										bdSteuerbetragTheoretisch));
					}
					if (fibuExportDtos[i].getSollbetragBD() != null) {
						fibuExportDtos[i].setSollbetrag(fibuExportDtos[i]
								.getSollbetragBD().add(
										bdSteuerbetragTheoretisch));
					}
				}
			}

			//------------------------------------------------------------------
			// --
			// Sollbetrag
			//------------------------------------------------------------------
			// --
			mt.addParameter(P_SOLLBETRAG, fibuExportDtos[i].getSollbetrag());
			hmSalden.add(KEY_SOLL, fibuExportDtos[i].getSollbetragBD());
			myLogger.logData(" Soll: " + fibuExportDtos[i].getSollbetragBD());
			//------------------------------------------------------------------
			// --
			// Habenbetrag
			//------------------------------------------------------------------
			// --
			mt.addParameter(P_HABENBETRAG, fibuExportDtos[i].getHabenbetrag());
			hmSalden.add(KEY_HABEN, fibuExportDtos[i].getHabenbetragBD());
			myLogger.logData("Haben: " + fibuExportDtos[i].getHabenbetragBD());
			//------------------------------------------------------------------
			// --
			// Steuerbetrag
			//------------------------------------------------------------------
			// --
			BigDecimal bdSteuerbetrag;
			// Sondergschichte fuer die EU - ER.
			if (i>0 && (bInnerGemeinschaftlich || fibuExportDtos[i].isBReverseCharge())) {
				// nicht fuer erste Buchungszeile
//				BigDecimal bdUSTProzentsatz = getMehrwertsteuersatzDesMandanten();
				// Komma 2 Stellen nach links verschieben
				BigDecimal bdUSTProzentsatz = new BigDecimal(fibuExportDtos[i].getMwstsatz().getFMwstsatz());
				bdUSTProzentsatz = bdUSTProzentsatz.movePointLeft(2);
				if (fibuExportDtos[i].getSollbetragBD() != null) {
					// Steuerbetrag berechnen und runden.
					bdSteuerbetrag = Helper.rundeKaufmaennisch(
							fibuExportDtos[i].getSollbetragBD().multiply(
									bdUSTProzentsatz),
							FinanzFac.NACHKOMMASTELLEN);
					// Damit meine Saldenpruefung stimmt, muss ich den
					// rausrechnen
					hmSalden.add(KEY_SOLL, bdSteuerbetrag.negate());
				} else {
					bdSteuerbetrag = null;
				}
			}
			// Fuer AR/GS direkt aus dem Beleg.
			else {
				bdSteuerbetrag = fibuExportDtos[i].getSteuerBD();
			}
			mt.addParameter(P_STEUERBETRAG, formatNumber(bdSteuerbetrag));
			hmSalden.add(KEY_UST, bdSteuerbetrag);
			myLogger.logData("  UST: " + bdSteuerbetrag);
			//------------------------------------------------------------------
			// --
			// Spalten Fremdwaehrung bis UST-Land
			//------------------------------------------------------------------
			// --
			exportiereSpaltenFremdwaehrungBisUSTLand(fibuExportDtos[i],
					partnerDtoFinanzamt, sPartnerUstLkz, mt);
			//------------------------------------------------------------------
			// --
			// UST Prozentsatz
			//------------------------------------------------------------------
			// --
			String sUSTProzentsatz="";
			// siehe Exportbeschreibung Anhang II
			// fuer Drittland Erloes/Erwerb ist das der Code 01
			if (fibuExportDtos[i].getLaenderartCNr().equals(
					FinanzFac.LAENDERART_DRITTLAND)) {
				sUSTProzentsatz = UST_PROZENTSATZ_EXPORT_IN_NICHT_EU_LAENDER;
			}
/*			// fuer IG Erwerb ist das der Code 02
			else if (bInnerGemeinschaftlich || fibuExportDtos[i].isBReverseCharge()) {
				// da wirds der UST-Satz des Mandanten
				// ist notwendig, damit die Vorsteuerkennzeichnung erhalten
				// bleibt
				BigDecimal bdUSTProzentsatz = getMehrwertsteuersatzDesMandanten();
				sUSTProzentsatz = Helper.formatZahl(bdUSTProzentsatz, 0,
						theClientDto.getLocUi());
			} */
			// Inland: der UST-Satz ohne Nachkommastellen.
			else {
				sUSTProzentsatz = formatMwstsatz(fibuExportDtos[i]
						.getMwstsatz());
			}
			mt.addParameter(P_UST_PROZENTSATZ, sUSTProzentsatz);
			//------------------------------------------------------------------
			// --
			// UST-Code
			//------------------------------------------------------------------
			// --
			String ustCode;
			if (fibuExportDtos[i].getLaenderartCNr().equals(
					FinanzFac.LAENDERART_DRITTLAND)) {
				ustCode = UST_CODE_KEINE_STEUER;
			}
			// fuer IG Erwerb
			else if (bInnerGemeinschaftlich || fibuExportDtos[i].isBReverseCharge()) {
				ustCode = UST_CODE_ERWERBSSTEUER;
			}
			// Sonst Inland
			else {
				ustCode = UST_CODE_VORSTEUER;
			}
			mt.addParameter(P_UST_CODE, ustCode);
			//------------------------------------------------------------------
			// --
			// die restlichen Spalten sind wieder unabhaengig von der Belegart
			//------------------------------------------------------------------
			// --
			exportiereSpaltenAbSondercode(fibuExportDtos, i, mt, false);
			//------------------------------------------------------------------
			// --
			// exportieren
			//------------------------------------------------------------------
			// --
			String sZeile = mt.transformText(FinanzReportFac.REPORT_MODUL,
					theClientDto.getMandant(), getXSLFile(), theClientDto
							.getLocMandant(), theClientDto);
			sb.append(sZeile).append(Helper.CR_LF);
		}
		// Salden pruefen.
		BigDecimal bdSaldo = hmSalden.get(KEY_HABEN).subtract(
				hmSalden.get(KEY_SOLL)).subtract(hmSalden.get(KEY_UST));
		if (bdSaldo.compareTo(new BigDecimal(0)) != 0) {
			myLogger.logData("----------------------------------------");
			myLogger.logData("Haben: " + hmSalden.get(KEY_HABEN));
			myLogger.logData(" Soll: " + hmSalden.get(KEY_SOLL));
			myLogger.logData("  UST: " + hmSalden.get(KEY_UST));
			EJBExceptionLP ex = new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FINANZ_EXPORT_SALDO_UNGLEICH_NULL,
					new Exception());
			ArrayList<Object> aList = new ArrayList<Object>();
			aList.add(fibuExportDtos[0].getBelegnummer());
			ex.setAlInfoForTheClient(aList);
			throw ex;
		}
		return sb.toString();
	}

	private BigDecimal getMehrwertsteuersatzDesMandanten()
			throws EJBExceptionLP {
		BigDecimal bdUSTProzentsatz = null;
		;
		try {
			if (mandantDto.getMwstsatzbezIIdStandardinlandmwstsatz() != null) {
				MwstsatzDto mwst = getMandantFac()
						.mwstsatzFindByMwstsatzbezIIdAktuellster(
								mandantDto
										.getMwstsatzbezIIdStandardinlandmwstsatz(),
								theClientDto);
				bdUSTProzentsatz = new BigDecimal(mwst.getFMwstsatz());
			}
			// falls dort nicht definiert, werdens 20%
			else {
				bdUSTProzentsatz = new BigDecimal(20.0);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return bdUSTProzentsatz;
	}

	private String exportiereDatenAR(final FibuexportDto[] fibuExportDtos,
			final PartnerDto partnerDtoFinanzamt,
			final boolean bInnerGemeinschaftlich, final String sPartnerUstLkz)
			throws EJBExceptionLP {
		AddableHashMap<Integer, BigDecimal> hmSalden = new AddableHashMap<Integer, BigDecimal>();

		/*
		 * Wenn alle gegenbuchungen den gleichen Steuersatz haben erh&auml;lt die
		 * Sammelbuchung den gleichen Steuersatz wie die Splittbuchungen. Sind
		 * die S&auml;tze unterschiedlich wird der Buchungscode der Sammelbuchung auf
		 * 0 gesetzt. (Siehe Buchungs-Beispiele in RZL Bescheibung)
		 */

		boolean gleicherUstSatz = true;
		boolean istGutschrift = false;
		if (fibuExportDtos[0].getBelegart() == FibuExportManager.BELEGART_GS) {
			/*
			 * PJ 15304 bei GS ist die Reihung der Buchungszeilen umgekehrt
			 */
			istGutschrift = true;
			MwstsatzDto satz = fibuExportDtos[0].getMwstsatz();
			for (int i = 0; i < fibuExportDtos.length - 1; i++) {
				if (!satz.equals(fibuExportDtos[i].getMwstsatz())) {
					gleicherUstSatz = false;
					break;
				}
			}
			if (gleicherUstSatz) {
				if (fibuExportDtos[fibuExportDtos.length - 1].getMwstsatz() == null) {
					fibuExportDtos[fibuExportDtos.length - 1].setMwstsatz(satz);
				}
			} else {
				fibuExportDtos[fibuExportDtos.length - 1].setSteucod("0");
			}
		} else {
			MwstsatzDto satz = fibuExportDtos[1].getMwstsatz();
			for (int i = 1; i < fibuExportDtos.length; i++) {
				if (!satz.equals(fibuExportDtos[i].getMwstsatz())) {
					gleicherUstSatz = false;
					break;
				}
			}
			if (gleicherUstSatz) {
				if (fibuExportDtos[0].getMwstsatz() == null) {
					fibuExportDtos[0].setMwstsatz(satz);
				}
			} else {
				fibuExportDtos[0].setSteucod("0");
			}
		}

		StringBuffer sb = new StringBuffer();
		// wegen dem xsl-file muss ich das zeilenweise machen - als fuer jeden
		// datensatz
		for (int i = 0; i < fibuExportDtos.length; i++) {
			// Zuerst: Richtigen UST-Prozentsatz und Finanzamt bestimmen.
			LpMailText mt = new LpMailText();
			exportiereSpalten_1_bis_6(fibuExportDtos[i], mt);
			//------------------------------------------------------------------
			// --
			// Sollbetrag
			//------------------------------------------------------------------
			// --
			mt.addParameter(P_SOLLBETRAG, fibuExportDtos[i].getSollbetrag());
			hmSalden.add(KEY_SOLL, fibuExportDtos[i].getSollbetragBD());
			//------------------------------------------------------------------
			// --
			// Habenbetrag
			//------------------------------------------------------------------
			// --
			mt.addParameter(P_HABENBETRAG, fibuExportDtos[i].getHabenbetrag());
			hmSalden.add(KEY_HABEN, fibuExportDtos[i].getHabenbetragBD());
			//------------------------------------------------------------------
			// --
			// Steuerbetrag
			//------------------------------------------------------------------
			// --
			mt
					.addParameter(P_STEUERBETRAG, fibuExportDtos[i]
							.getSteuerbetrag());
			hmSalden.add(KEY_UST, fibuExportDtos[i].getSteuerBD());
			//------------------------------------------------------------------
			// --
			// Spalten Fremdwaehrung bis UST-Land
			//------------------------------------------------------------------
			// --
			exportiereSpaltenFremdwaehrungBisUSTLand(fibuExportDtos[i],
					partnerDtoFinanzamt, sPartnerUstLkz, mt);
			//------------------------------------------------------------------
			// --
			// UST Prozentsatz
			//------------------------------------------------------------------
			// --
			String sUSTProzentsatz;
			// siehe Exportbeschreibung Anhang II
			// fuer Drittland Erloes/Erwerb ist das der Code 01
			if (fibuExportDtos[i].getLaenderartCNr().equals(
					FinanzFac.LAENDERART_DRITTLAND)) {
				sUSTProzentsatz = UST_PROZENTSATZ_EXPORT_IN_NICHT_EU_LAENDER;
			}
			// fuer IG Erloes ist das der Code 02
			else if (bInnerGemeinschaftlich) {
				// Sonderfall: der Partner hat keine UID-Nummer
				// dann ist das wie Inland zu behandeln -> Mwst-Satz
				if (fibuExportDtos[i].getLaenderartCNr().equals(
						FinanzFac.LAENDERART_EU_AUSLAND_OHNE_UID)) {
					sUSTProzentsatz = formatMwstsatz(fibuExportDtos[i]
							.getMwstsatz());
				}
				// Normalfall: IG Lieferung/Erwerb -> spezieller Code 02 dafuer
				else {
					sUSTProzentsatz = UST_PROZENTSATZ_IG_LIEFERUNG_IN_EIN_EU_LAND;
				}
			}
			// Inland: der UST-Satz ohne Nachkommastellen.
			else {
				sUSTProzentsatz = formatMwstsatz(fibuExportDtos[i]
						.getMwstsatz());
			}
			mt.addParameter(P_UST_PROZENTSATZ, sUSTProzentsatz);
			//------------------------------------------------------------------
			// --
			// UST-Code
			//------------------------------------------------------------------
			// --
			mt.addParameter(P_UST_CODE, UST_CODE_MWST);
			//------------------------------------------------------------------
			// --
			// die restlichen Spalten sind wieder unabhaengig von der Belegart
			//------------------------------------------------------------------
			// --
			exportiereSpaltenAbSondercode(fibuExportDtos, i, mt, istGutschrift);
			//------------------------------------------------------------------
			// --
			// exportieren
			//------------------------------------------------------------------
			// --
			String sZeile = mt.transformText(FinanzReportFac.REPORT_MODUL,
					theClientDto.getMandant(), getXSLFile(), theClientDto
							.getLocMandant(), theClientDto);
			sb.append(sZeile).append(Helper.CR_LF);
		}
		// Salden pruefen.
		BigDecimal bdSaldo;
		if (fibuExportDtos[0].getBelegart().equals(
				FibuExportManager.BELEGART_ER)) {
			bdSaldo = hmSalden.get(KEY_HABEN).subtract(hmSalden.get(KEY_SOLL))
					.subtract(hmSalden.get(KEY_UST));
		} else {
			bdSaldo = hmSalden.get(KEY_SOLL).subtract(hmSalden.get(KEY_HABEN))
					.subtract(hmSalden.get(KEY_UST));
		}
		if (bdSaldo.compareTo(new BigDecimal(0)) != 0) {
			EJBExceptionLP ex = new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FINANZ_EXPORT_SALDO_UNGLEICH_NULL,
					new Exception());
			ArrayList<Object> aList = new ArrayList<Object>();
			aList.add(fibuExportDtos[0].getBelegnummer());
			ex.setAlInfoForTheClient(aList);
			throw ex;
		}
		return sb.toString();
	}

	private void exportiereSpaltenFremdwaehrungBisUSTLand(
			FibuexportDto fibuExportDto, PartnerDto partnerDtoFinanzamt,
			String sPartnerUstLkz, LpMailText mt) throws EJBExceptionLP {
		// --------------------------------------------------------------------
		// Fremdwaehrungsdaten nur fuer belege in fremdwaehrung
		// --------------------------------------------------------------------
		if (!fibuExportDto.getFremdwaehrung().equals(
				theClientDto.getSMandantenwaehrung())) {
			mt.addParameter(P_FREMDWAEHRUNG, fibuExportDto.getFremdwaehrung());
			mt.addParameter(P_FREMDWAEHRUNG_SOLLBETRAG, fibuExportDto
					.getSollbetragFW());
			mt.addParameter(P_FREMDWAEHRUNG_HABENBETRAG, fibuExportDto
					.getHabenbetragFW());
		} else {
			mt.addParameter(P_FREMDWAEHRUNG, "");
			mt.addParameter(P_FREMDWAEHRUNG_SOLLBETRAG, "");
			mt.addParameter(P_FREMDWAEHRUNG_HABENBETRAG, "");
		}
		// --------------------------------------------------------------------
		// Kostenstelle
		// --------------------------------------------------------------------
		mt.addParameter(P_KOSTENSTELLE, fibuExportDto.getKostenstelle());
		// --------------------------------------------------------------------
		// Belegkreis
		// --------------------------------------------------------------------
		mt.addParameter(P_BELEGKREIS, fibuExportDto.getBelegart());
		// --------------------------------------------------------------------
		// Belegnummer
		// --------------------------------------------------------------------
		// Die Belegnummer ohne Geschaeftsjahr und Trennzeichen
		String sBelegnummerOhneGJ = extractLaufendeNummerAusBelegnummer(
				fibuExportDto.getBelegnummer(), theClientDto);
		if (sBelegnummerOhneGJ == null) {
			// Das darf nicht passieren
			myLogger.warn(theClientDto.getIDUser(), "Ungueltige Belegnummer");
		}
		mt.addParameter(P_BELEGNUMMER, sBelegnummerOhneGJ);
		// --------------------------------------------------------------------
		// UST Land
		// --------------------------------------------------------------------
		// wenn nun ein FA gefunden wurde, zieht dessen LKZ
		if (partnerDtoFinanzamt != null) {
			mt.addParameter(P_UST_LAND, uebersetzeUSTLand(partnerDtoFinanzamt
					.getLandplzortDto().getLandDto().getCLkz()));
		} else {
			mt.addParameter(P_UST_LAND, uebersetzeUSTLand(sPartnerUstLkz));
		}
	}

	private void exportiereSpalten_1_bis_6(FibuexportDto fibuExportDto,
			LpMailText mt) {
		mt.addParameter(P_KONTONUMMER, fibuExportDto.getKontonummer());
		mt.addParameter(P_GEGENKONTO, fibuExportDto.getGegenkonto());
		// --------------------------------------------------------------------
		// OP Nummer
		// --------------------------------------------------------------------
		String sOPNummer;
		if (fibuExportDto.getOPNummer() != null) {
			sOPNummer = fibuExportDto.getOPNummer();
		} else {
			sOPNummer = "";
		}

		// CK/AD: Wenn keine Kontierung, dann ist die OP-Nummer '0' undd dann
		// darf die
		while (sOPNummer.length() > 0 && sOPNummer.charAt(0) == '0') {
			sOPNummer = sOPNummer.substring(1);
		}

		mt.addParameter(P_OP_NUMMER, sOPNummer);
		// --------------------------------------------------------------------
		// Belegdatum
		// --------------------------------------------------------------------
		mt.addParameter(P_BELEGDATUM,
				formatDatum(fibuExportDto.getBelegdatum()));
		// --------------------------------------------------------------------
		// Valutadatum
		// --------------------------------------------------------------------
		mt.addParameter(P_VALUTADATUM, formatDatum(fibuExportDto
				.getValutadatum()));
		// --------------------------------------------------------------------
		// Waehrung
		// --------------------------------------------------------------------
		mt.addParameter(P_WAEHRUNG, fibuExportDto.getWaehrung());
	}

	private void exportiereSpaltenAbSondercode(FibuexportDto[] fibuExportDtos,
			int i, LpMailText mt, boolean istGutschrift) {
		// --------------------------------------------------------------------
		// Sondercode
		// 19.06.07 lt. WH: IG Erwerb _ohne_ vorsteuerabzug.
		// nach Ruecksprache mit RZL lassen wir den immer auf "00", sonderfaelle
		// muessen extra bestellt werden.
		// --------------------------------------------------------------------
		if (fibuExportDtos[i].isBReverseCharge())
			mt.addParameter(P_SONDERCODE,
					SONDERCODE_UEBERNOMMENE_UST_GEM_PARAGRAPH_19_MIT_VORSTEUERABZUG);
		else
			mt.addParameter(P_SONDERCODE,
				SONDERCODE_KEIN_SONDERFALL_HINSICHTLICH_UST);
		// --------------------------------------------------------------------
		// Buchungsart
		// --------------------------------------------------------------------
		// Die kann ganz einfach anhand der Anzahl der Buchungszeilen bestimmt
		// werden.
		String sBuchungsart;
		if (fibuExportDtos.length == 2) {
			// 2 Zeilen
			sBuchungsart = BUCHUNGSART_MIT_GEGENBUCHUNG;
		} else {
			if (istGutschrift) {
				if (i == fibuExportDtos.length - 1) {
					// die letzte Zeile ist die Split-Sammelbuchung
					sBuchungsart = BUCHUNGSART_SPLITSAMMELBUCHUNG;
				} else {
					// allen weiteren Zeilen sind Splitbuchungen
					sBuchungsart = BUCHUNGSART_SPLITBUCHUNG;
				}
			} else {
				if (i == 0) {
					// die erste Zeile ist die Split-Sammelbuchung
					sBuchungsart = BUCHUNGSART_SPLITSAMMELBUCHUNG;
				} else {
					// allen weiteren Zeilen sind Splitbuchungen
					sBuchungsart = BUCHUNGSART_SPLITBUCHUNG;
				}
			}
		}
		mt.addParameter(P_BUCHUNGSART, sBuchungsart);
		// --------------------------------------------------------------------
		// Zahlungsfrist in Tagen
		// --------------------------------------------------------------------
		String sZahlungsfristInTagen = "";
		if (fibuExportDtos[i].getZahlungszielDto() != null
				&& fibuExportDtos[i].getZahlungszielDto()
						.getAnzahlZieltageFuerNetto() != null) {
			sZahlungsfristInTagen = fibuExportDtos[i].getZahlungszielDto()
					.getAnzahlZieltageFuerNetto().toString();
		}
		mt.addParameter(P_ABWEICHENDE_ZAHLUNGSFRIST, sZahlungsfristInTagen);
		// --------------------------------------------------------------------
		// Skontotage
		// --------------------------------------------------------------------
		String sSkontotage = "";
		if (fibuExportDtos[i].getZahlungszielDto() != null
				&& fibuExportDtos[i].getZahlungszielDto()
						.getSkontoAnzahlTage1() != null) {
			sSkontotage = fibuExportDtos[i].getZahlungszielDto()
					.getSkontoAnzahlTage1().toString();
		}
		mt.addParameter(P_ABWEICHENDE_SKONTOFRIST, sSkontotage);
		// --------------------------------------------------------------------
		// Skontoprozentsatz
		// --------------------------------------------------------------------
		String sSkontoProzentsatz = "";
		if (fibuExportDtos[i].getZahlungszielDto() != null
				&& fibuExportDtos[i].getZahlungszielDto()
						.getSkontoProzentsatz1() != null) {
			sSkontoProzentsatz = fibuExportDtos[i].getZahlungszielDto()
					.getSkontoProzentsatz1().toString();
		}
		mt.addParameter(P_ABWEICHENDER_SKONTO_PROZENTSATZ, sSkontoProzentsatz);
		// --------------------------------------------------------------------
		// Text auf 2 Teile mit je 20 zeichen teilen und alle Strichpunkte
		// rausfiltern
		// --------------------------------------------------------------------
		String sText = "";
		// Text immer aus 1. Buchung
		if (fibuExportDtos[0].getBelegart() == FibuExportManager.BELEGART_GS) {
			/*
			 * PJ 15304 bei GS umgekehrte Folge
			 */
			if (fibuExportDtos[fibuExportDtos.length - 1].getText() != null) {
				sText = fibuExportDtos[fibuExportDtos.length - 1].getText()
						.replaceAll(";", "");
			}
		} else {
			if (fibuExportDtos[0].getText() != null) {
				sText = fibuExportDtos[0].getText().replaceAll(";", "");
			}
		}
		String sText1 = "";
		String sText2 = "";
		if (sText != null) {
			if (sText.length() <= 20) {
				sText1 = sText;
			} else {
				sText1 = sText.substring(0, 20);
				if (sText.length() <= 40) {
					sText2 = sText.substring(20, sText.length());
				} else {
					sText2 = sText.substring(20, 40);
				}
			}
		}
		// fuer ER kommt in das 2. Textfeld der Lieferantenname
		if (fibuExportDtos[i].getBelegart().equals(
				FibuExportManager.BELEGART_ER)) {
			String sName = fibuExportDtos[i].getPartnerDto()
					.formatFixTitelName1Name2();
			if (sName.length() <= 20) {
				sText2 = sName;
			} else {
				sText2 = sName.substring(0, 20);
			}
		}
		mt.addParameter(P_BUCHUNGSTEXT, sText1);
		mt.addParameter(P_BUCHUNGSTEXT_ZEILE_2, sText2);
		// --------------------------------------------------------------------
		// UID - Nummer
		// --------------------------------------------------------------------
		String sUID;
		if (fibuExportDtos[i].getUidNummer() != null) {
			// Upper Case
			sUID = fibuExportDtos[i].getUidNummer().toUpperCase();
			// Leerzeichen entfernen
			sUID = sUID.replaceAll(" ", "");
		} else {
			// wenn nicht angegeben, bleibts leer
			sUID = "";
		}
		mt.addParameter(P_UID_NUMMER, sUID);
	}

	protected String getXSLFile() {
		return XSL_FILE_RZL;
	}

	protected String exportiereUeberschrift() throws EJBExceptionLP {
		// wegen dem xsl-file muss ich das zeilenweise machen - als fuer jeden
		// datensatz
		LpMailText mt = new LpMailText();
		mt.addParameter(P_KONTONUMMER, "Kontonummer");
		mt.addParameter(P_GEGENKONTO, "Gegenkonto");
		mt.addParameter(P_OP_NUMMER, "OP-Nummer");
		mt.addParameter(P_BELEGDATUM, "Belegdatum");
		mt.addParameter(P_VALUTADATUM, "Valutadatum");
		mt.addParameter(P_WAEHRUNG, "Waehrung");
		mt.addParameter(P_SOLLBETRAG, "Sollbetrag");
		mt.addParameter(P_HABENBETRAG, "Habenbetrag");
		mt.addParameter(P_STEUERBETRAG, "Steuerbetrag");
		mt.addParameter(P_FREMDWAEHRUNG, "Fremdw\u00E4hrung");
		mt.addParameter(P_FREMDWAEHRUNG_SOLLBETRAG, "Sollbetrag FW");
		mt.addParameter(P_FREMDWAEHRUNG_HABENBETRAG, "Habenbetrag FW");
		mt.addParameter(P_KOSTENSTELLE, "Kostenstelle");
		mt.addParameter(P_BELEGKREIS, "Belegkreis");
		mt.addParameter(P_BELEGNUMMER, "Belegnummer");
		mt.addParameter(P_UST_LAND, "UST Land");
		mt.addParameter(P_UST_PROZENTSATZ, "UST Prozentsatz");
		mt.addParameter(P_UST_CODE, "UST-Code");
		mt.addParameter(P_SONDERCODE, "Sondercode");
		mt.addParameter(P_BUCHUNGSART, "Buchungsart");
		mt.addParameter(P_ABWEICHENDE_ZAHLUNGSFRIST,
				"Abweichende Zahlungsfrist");
		mt.addParameter(P_ABWEICHENDE_SKONTOFRIST, "Abweichende Skontofrist");
		mt.addParameter(P_ABWEICHENDER_SKONTO_PROZENTSATZ,
				"Abweichender Skontoprozentsatz");
		mt.addParameter(P_BUCHUNGSTEXT, "Buchungstext");
		mt.addParameter(P_BUCHUNGSTEXT_ZEILE_2, "Buchungstext Zeile 2");
		mt.addParameter(P_UID_NUMMER, "UID-Nummer");
		String sZeile = mt.transformText(FinanzReportFac.REPORT_MODUL,
				theClientDto.getMandant(), getXSLFile(), theClientDto
						.getLocMandant(), theClientDto);
		return sZeile;
	}

	protected String uebersetzeUSTLand(String sLKZ) {
		String sUSTLand = "";
		// der Partner muss immer definiert sein
		if (sLKZ != null) {
			if (ustCodeCache.get(sLKZ) == null) {
				LandDto landDto = null;
				try {
					landDto = getSystemFac().landFindByLkz(sLKZ);
				} catch (EJBExceptionLP e) {
					// kein Land
				} catch (RemoteException e) {
					//
				}
				if (landDto != null) {
					ustCodeCache.put(sLKZ, landDto.getCUstcode());
					return landDto.getCUstcode();
				}
			} else {
				return ustCodeCache.get(sLKZ);
			}
		}
		return sUSTLand;
	}

	private String formatDatum(java.util.Date d) {
		if (d != null) {
			DateFormat df = new SimpleDateFormat("ddMMyyyy");
			return df.format(d);
		} else {
			return "";
		}
	}

	private String formatNumberOhneNachkommastellen(Number n) {
		if (n != null) {
			NumberFormat nf = new DecimalFormat("#########0");
			return nf.format(n.doubleValue());
		} else {
			return "";
		}
	}

	public String formatMwstsatz(MwstsatzDto mwstsatz) {
		if (mwstsatz != null) {
			// RZL ohne Nachkommastellen
			return formatNumberOhneNachkommastellen(mwstsatz.getFMwstsatz());
		} else {
			return "";
		}
	}

	public static String formatNumber(Number n) {
		if (n != null) {
			NumberFormat nf = new DecimalFormat("#########0.00");
			return nf.format(n.doubleValue());
		} else {
			return "";
		}
	}
}
