/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import com.lp.server.finanz.service.FibuExportKriterienDto;
import com.lp.server.finanz.service.FibuexportDto;
import com.lp.server.finanz.service.FinanzReportFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
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
 * Copyright Logistik Pur GmbH (c) 2004, 2005, 2006
 * </p>
 * 
 * <p>
 * Erstellung: Vorname Nachname; dd.mm.06
 * </p>
 * 
 * <p>
 * 
 * @author $Author: heidi $
 *         </p>
 * 
 * @version unbekannt Date $Date: 2009/01/13 12:33:19 $
 */
public class FibuExportFormatterAbacus extends FibuExportFormatter {
	// TA005
	public final static String P_TA005_UEBERTRAGUNGSNUMMER = "TA005_Uebertragungsnummer";
	public final static String P_TA005_ZEILENNUMMER = "TA005_Zeilennummer";
	public final static String P_TA005_TRANSAKTIONSART = "TA005_Transaktionsart";
	public final static String P_TA005_INITSEQUENZ = "TA005_Initsequenz";
	public final static String P_TA005_VERSION = "TA005_Version";
	public final static String P_TA005_RESERVE1 = "TA005_Reserve1";
	public final static String P_TA005_RESERVE2 = "TA005_Reserve2";
	public final static String P_TA005_SEQUENTIELLE_NUMMER = "TA005_Sequentielle-Nummer";
	public final static String P_TA005_ERSTELLUNGSDATUM = "TA005_Erstellungsdatum";
	public final static String P_TA005_TEILNEHMER_NUMMER = "TA005_Teilnehmer-Nummer";
	public final static String P_TA005_TEILNEHMER_FILE = "TA005_Teilnehmer-File";
	public final static String P_TA005_RESERVE3 = "TA005_Reserve3";
	public final static String P_TA005_RESERVE4 = "TA005_Reserve4";
	public final static String P_TA005_RESERVE5 = "TA005_Reserve5";
	public final static String P_TA005_ENDSEQUENZ = "TA005_Endsequenz";
	// TA100:
	public final static String P_TA100_UEBERTRAGUNGSNUMMER = "TA100_Uebertragungsnummer";
	public final static String P_TA100_ZEILENNUMMER = "TA100_Zeilennummer";
	public final static String P_TA100_TRANSAKTIONSART = "TA100_Transaktionsart";
	public final static String P_TA100_BELEGNUMMER = "TA100_Belegnummer";
	public final static String P_TA100_KUNDENNUMMER = "TA100_Kundennummer";
	public final static String P_TA100_TA_VERSION = "TA100_TA-Version";
	public final static String P_TA100_ISO_CODE_BELEGWAEHRUNG = "TA100_ISO-Code-Belegwaehrung";
	public final static String P_TA100_ANZAHL_POSITIONEN = "TA100_Anzahl-Positionen";
	public final static String P_TA100_FIBU_BELEG_DATUM = "TA100_FIBU-Beleg-Datum";
	public final static String P_TA100_BELEG_TOTAL_LEITWAEHRUNG = "TA100_Beleg-Total-Leitwaehrung";
	public final static String P_TA100_BELEG_TOTAL_BELEGWAEHRUNG = "TA100_Beleg-Total-Belegwaehrung";
	public final static String P_TA100_FREMDWAEHRUNGSKURS = "TA100_Fremdwaehrungskurs";
	public final static String P_TA100_ZAHLUNGSKONDITIONEN = "TA100_Zahlungskonditionen";
	public final static String P_TA100_FRIST = "TA100_Frist";
	public final static String P_TA100_SKONTO_STUFE_1_TAGE = "TA100_Skonto-Stufe-1-Tage";
	public final static String P_TA100_SKONTO_STUFE_1_PROZENT = "TA100_Skonto-Stufe-1-Prozent";
	public final static String P_TA100_SKONTO_STUFE_2_TAGE = "TA100_Skonto-Stufe-2-Tage";
	public final static String P_TA100_SKONTO_STUFE_2_PROZENT = "TA100_Skonto-Stufe-2-Prozent";
	public final static String P_TA100_SKONTO_STUFE_3_TAGE = "TA100_Skonto-Stufe-3-Tage";
	public final static String P_TA100_SKONTO_STUFE_3_PROZENT = "TA100_Skonto-Stufe-3-Prozent";
	public final static String P_TA100_RESERVE1 = "TA100_Reserve1";
	public final static String P_TA100_IDENTIFIKATIONSNUMMER = "TA100_Identifikationsnummer";
	public final static String P_TA100_RESERVE2 = "TA100_Reserve2";
	public final static String P_TA100_KUNDE_EROEFFNEN = "TA100_Kunde-eroeffnen";
	public final static String P_TA100_KUNDEN_ADRESS_NUMMER = "TA100_Kunden-Adress-Nummer";
	public final static String P_TA100_KURZNAME = "TA100_Kurzname";
	public final static String P_TA100_VORSCHLAEGE_AUS_KUNDENNUMMER = "TA100_Vorschlaege-aus-Kundennummer";
	public final static String P_TA100_DEBI_BELEG_DATUM = "TA100_DEBI-Beleg-Datum";
	public final static String P_TA100_RESERVE3 = "TA100_Reserve3";
	public final static String P_TA100_FAELLIGKEITSDATUM = "TA100_Faelligkeitsdatum";
	public final static String P_TA100_BELEGART = "TA100_Belegart";
	public final static String P_TA100_GESCHAEFTSBEREICH = "TA100_Geschaeftsbereich";
	public final static String P_TA100_GESCHAEFTSFALL = "TA100_Geschaeftsfall";
	public final static String P_TA100_KOSTENGRUPPE = "TA100_Kostengruppe";
	public final static String P_TA100_VERANTWORTLICHER = "TA100_Verantwortlicher";
	public final static String P_TA100_BELASTUNGSWEG = "TA100_Belastungsweg";
	public final static String P_TA100_BELEGGRUPPEN_NUMMER_1 = "TA100_Beleggruppen-Nummer-1";
	public final static String P_TA100_BELEGGRUPPEN_NUMMER_2 = "TA100_Beleggruppen-Nummer-2";
	public final static String P_TA100_BELEGGRUPPEN_NUMMER_3 = "TA100_Beleggruppen-Nummer-3";
	public final static String P_TA100_BELEGCODE1 = "TA100_Belegcode1";
	public final static String P_TA100_BELEGCODE2 = "TA100_Belegcode2";
	public final static String P_TA100_BELEGCODE3 = "TA100_Belegcode3";
	public final static String P_TA100_BELEGREFERENZ = "TA100_Belegreferenz";
	public final static String P_TA100_VESR_VERFAHREN = "TA100_VESR-Verfahren";
	public final static String P_TA100_EXTERNE_BELEGNUMMER = "TA100_Externe-Belegnummer";
	public final static String P_TA100_ENDSEQUENZ = "TA100_Endsequenz";

	public final static String P_TA100_ZAHLUNGSKONDITION_NETTO = "N";
	public final static String P_TA100_ZAHLUNGSKONDITION_SKONTO = "S";
	public final static String P_TA100_ZAHLUNGSKONDITION_DATEI = "D";
	public final static String P_TA100_ZAHLUNGSKONDITION_KUNDE = "K";

	public final static String P_TA100_BELEGART_RECHNUNG = "R";
	// TA101
	public final static String P_TA101_UEBERTRAGUNGSNUMMER = "TA101_Uebertragungsnummer";
	public final static String P_TA101_ZEILENNUMMER = "TA101_Zeilennummer";
	public final static String P_TA101_TRANSAKTIONSART = "TA101_Transaktionsart";
	public final static String P_TA101_BELEGNUMMER = "TA101_Belegnummer";
	public final static String P_TA101_ESR_NUMMER = "TA101_ESR-Nummer";
	public final static String P_TA101_BEMERKUNG = "TA101_Bemerkung";
	public final static String P_TA101_ADRESSNUMMER = "TA101_Adressnummer";
	public final static String P_TA101_KURZNAME = "TA101_Kurzname";
	public final static String P_TA101_PLZ = "TA101_PLZ";
	public final static String P_TA101_ZUSATZ_KUNDENART = "TA101_Zusatz-Kundenart";
	public final static String P_TA101_ZUSATZ_KUNDENNUMMER = "TA101_Zusatz-Kundennummer";
	public final static String P_TA101_RESERVE1 = "TA101_Reserve1";
	public final static String P_TA101_RESERVE2 = "TA101_Reserve2";
	public final static String P_TA101_ISO_EINHEIT_BELEGWAEHRUNG = "TA101_ISO-Einheit-Belegwaehrung";
	public final static String P_TA101_BELEG_SAMMELKONTO = "TA101_Beleg-Sammelkonto";
	public final static String P_TA101_RESERVE3 = "TA101_Reserve3";
	public final static String P_TA101_TA_VERSION = "TA101_TA-Version";
	public final static String P_TA101_BELEG_SAMMEL_KST_1 = "TA101_Beleg-Sammel-KST-1";
	public final static String P_TA101_BELEG_SAMMEL_KST_2 = "TA101_Beleg-Sammel-KST-2";
	public final static String P_TA101_MAHNDATUM = "TA101_Mahndatum";
	public final static String P_TA101_MAHNBEREICH = "TA101_Mahnbereich";
	public final static String P_TA101_MAHNVERFAHREN = "TA101_Mahnverfahren";
	public final static String P_TA101_MAHN_ADRESSNUMMER = "TA101_Mahn-Adressnummer";
	public final static String P_TA101_MAHN_ADRESSKONTAKTPERSON = "TA101_Mahn-Adresskontaktperson";
	public final static String P_TA101_KUNDE_KURZNAME = "TA101_Kunde-Kurzname";
	public final static String P_TA101_KUNDE_NAME = "TA101_Kunde-Name";
	public final static String P_TA101_KUNDE_VORNAME = "TA101_Kunde-Vorname";
	public final static String P_TA101_KUNDE_ZUSATZ = "TA101_Kunde-Zusatz";
	public final static String P_TA101_KUNDE_ADRESSE = "TA101_Kunde-Adresse";
	public final static String P_TA101_KUNDE_LAND = "TA101_Kunde-Land";
	public final static String P_TA101_KUNDE_PLZ = "TA101_Kunde-PLZ";
	public final static String P_TA101_KUNDE_ORT = "TA101_Kunde-Ort";
	public final static String P_TA101_KUNDE_SPRACHCODE = "TA101_Kunde-Sprachcode";
	public final static String P_TA101_KUNDE_ADRESSZEILE2 = "TA101_Kunde-Adresszeile2";
	public final static String P_TA101_ENDSEQUENZ = "TA101_Endsequenz";
	// TA102
	public final static String P_TA102_UEBERTRAGUNGSNUMMER = "TA102_Uebertragungsnummer";
	public final static String P_TA102_ZEILENNUMMER = "TA102_Zeilennummer";
	public final static String P_TA102_TRANSAKTIONSART = "TA102_Transaktionsart";
	public final static String P_TA102_BELEGNUMMER = "TA102_Belegnummer";
	public final static String P_TA102_HABEN_KONTO = "TA102_Haben-Konto";
	public final static String P_TA102_HABEN_KST1 = "TA102_Haben-KST1";
	public final static String P_TA102_MWST_CODE = "TA102_MWST-Code";
	public final static String P_TA102_MWST_SATZ = "TA102_MWST-Satz";
	public final static String P_TA102_POSITIONSBETRAG_LEITWAEHRUNG = "TA102_Positionsbetrag-Leitwaehrung";
	public final static String P_TA102_POSITIONSBETRAG_BELEGWAEHRUNG = "TA102_Positionsbetrag-Belegwaehrung";
	public final static String P_TA102_MWST_BETRAG_LEITWAEHRUNG = "TA102_MWST-Betrag-Leitwaehrung";
	public final static String P_TA102_MWST_BETRAG_BELEGWAEHRUNG = "TA102_MWST-Betrag-Belegwaehrung";
	public final static String P_TA102_POSITIONSTEXT = "TA102_Positionstext";
	public final static String P_TA102_RESERVE1 = "TA102_Reserve1";
	public final static String P_TA102_RESERVE2 = "TA102_Reserve2";
	public final static String P_TA102_RESERVE3 = "TA102_Reserve3";
	public final static String P_TA102_RESERVE4 = "TA102_Reserve4";
	public final static String P_TA102_RESERVE5 = "TA102_Reserve5";
	public final static String P_TA102_RESERVE6 = "TA102_Reserve6";
	public final static String P_TA102_RESERVE7 = "TA102_Reserve7";
	public final static String P_TA102_RESERVE8 = "TA102_Reserve8";
	public final static String P_TA102_EXTERNE_REFERENZ = "TA102_Externe-Referenz";
	public final static String P_TA102_MENGEN_CODE = "TA102_Mengen-Code";
	public final static String P_TA102_RESERVE9 = "TA102_Reserve9";
	public final static String P_TA102_MENGE = "TA102_Menge";
	public final static String P_TA102_HABEN_KST2 = "TA102_Haben-KST2";
	public final static String P_TA102_TA_VERSION = "TA102_TA-Version";
	public final static String P_TA102_FIBU_CODE = "TA102_Fibu-Code";
	public final static String P_TA102_RESERVIERT_FUER_ANLAGENART = "TA102_reserviert-fuer-Anlagenart";
	public final static String P_TA102_RESERVIERT_FUER_ANLAGENNUMMER = "TA102_reserviert-fuer-Anlagennummer";
	public final static String P_TA102_LIEFERDATUM = "TA102_Lieferdatum";
	public final static String P_TA102_PROVISION_ANWEISUNGS_FLAG = "TA102_Provision-Anweisungs-Flag";
	public final static String P_TA102_SATZFINDUNGSFLAG1 = "TA102_Satzfindungsflag1";
	public final static String P_TA102_PROVISIONISTENFINDUNGSFLAG1 = "TA102_Provisionistenfindungsflag1";
	public final static String P_TA102_FELDINHALTSFLAG1 = "TA102_Feldinhaltsflag1";
	public final static String P_TA102_PROVISIONIERUNGSFELD1 = "TA102_Provisionierungsfeld1";
	public final static String P_TA102_SATZFINDUNGSFLAG2 = "TA102_Satzfindungsflag2";
	public final static String P_TA102_PROVISIONISTENFINDUNGSFLAG2 = "TA102_Provisionistenfindungsflag2";
	public final static String P_TA102_FELDINHALTSFLAG2 = "TA102_Feldinhaltsflag2";
	public final static String P_TA102_PROVISIONIERUNGSFELD2 = "TA102_Provisionierungsfeld2";
	public final static String P_TA102_POSITIONSNUMMER = "TA102_Positionsnummer";
	public final static String P_TA102_GESCHAEFTSBEREICHAUFPOSITION = "TA102_GeschaeftsbereichaufPosition";
	public final static String P_TA102_ENDSEQUENZ = "TA102_Endsequenz";
	// TA920
	public final static String P_TA920_UEBERTRAGUNGSNUMMER = "TA920_Uebertragungsnummer";
	public final static String P_TA920_ZEILENNUMMER = "TA920_Zeilennummer";
	public final static String P_TA920_TRANSAKTIONSART = "TA920_Transaktionsart";
	public final static String P_TA920_KUNDENNUMMER = "TA920_Kundennummer";
	public final static String P_TA920_NAME = "TA920_Name";
	public final static String P_TA920_VORNAME = "TA920_Vorname";
	public final static String P_TA920_KURZNAME = "TA920_Kurzname";
	public final static String P_TA920_KURZNAME_AENDERBAR = "TA920_Kurzname-aenderbar";
	public final static String P_TA920_ZUSATZ = "TA920_Zusatz";
	public final static String P_TA920_ADRESSE1 = "TA920_Adresse1";
	public final static String P_TA920_ADRESSE2 = "TA920_Adresse2";
	public final static String P_TA920_LAND = "TA920_Land";
	public final static String P_TA920_PLZ = "TA920_PLZ";
	public final static String P_TA920_ORT = "TA920_Ort";
	public final static String P_TA920_SPRACHCODE = "TA920_Sprachcode";
	public final static String P_TA920_IDENTIFIKATIONSNUMMER = "TA920_Identifikationsnummer";
	public final static String P_TA920_RESERVE = "TA920_Reserve";
	public final static String P_TA920_ADRESSNUMMER = "TA920_Adressnummer";
	public final static String P_TA920_ENDSEQUENZ = "TA920_Endsequenz";
	// TA922
	public final static String P_TA922_UEBERTRAGUNGSNUMMER = "TA922_Uebertragungsnummer";
	public final static String P_TA922_ZEILENNUMMER = "TA922_Zeilennummer";
	public final static String P_TA922_TRANSAKTIONSART = "TA922_Transaktionsart";
	public final static String P_TA922_KUNDENNUMMER = "TA922_Kundennummer";
	public final static String P_TA922_RESERVE1 = "TA922_Reserve1";
	public final static String P_TA922_VORSCHLAGS_ISO_CODE = "TA922_Vorschlags-ISO-Code";
	public final static String P_TA922_RESERVE2 = "TA922_Reserve2";
	public final static String P_TA922_FIBU_KONTO = "TA922_FIBU-Konto";
	public final static String P_TA922_VORAUSZAHLUNGSKONTO = "TA922_Vorauszahlungskonto";
	public final static String P_TA922_SKONTO_KONTO = "TA922_Skonto-Konto";
	public final static String P_TA922_KURSGEWINN_KONTO = "TA922_Kursgewinn-Konto";
	public final static String P_TA922_KURSVERLUST_KONTO = "TA922_Kursverlust-Konto";
	public final static String P_TA922_ERTRAGS_KONTO = "TA922_Ertrags-Konto";
	public final static String P_TA922_RESERVE3 = "TA922_Reserve3";
	public final static String P_TA922_SKONTO_KST = "TA922_Skonto-KST";
	public final static String P_TA922_ZAHLUNGSKONDITIONEN = "TA922_Zahlungskonditionen";
	public final static String P_TA922_MAHNCODE = "TA922_Mahncode";
	public final static String P_TA922_RESERVE4 = "TA922_Reserve4";
	public final static String P_TA922_GRUPPENNUMMER = "TA922_Gruppennummer";
	public final static String P_TA922_RESERVE5 = "TA922_Reserve5";
	public final static String P_TA922_RESERVE6 = "TA922_Reserve6";
	public final static String P_TA922_KREDITLIMITE = "TA922_Kreditlimite";
	public final static String P_TA922_BEMERKUNG = "TA922_Bemerkung";
	public final static String P_TA922_RESERVE7 = "TA922_Reserve7";
	public final static String P_TA922_AENDERUNGEN = "TA922_aenderungen";
	public final static String P_TA922_DEBI_VORSCHLAEGE = "TA922_DEBI-Vorschlaege";
	public final static String P_TA922_KURSGEWINN_KST = "TA922_Kursgewinn-KST";
	public final static String P_TA922_KURSVERLUST_KST = "TA922_Kursverlust-KST";
	public final static String P_TA922_ERTRAGS_KST = "TA922_Ertrags-KST";
	public final static String P_TA922_TA_VERSION = "TA922_TA-Version";
	public final static String P_TA922_PROVISIONISTVORSCHLAGFELD1 = "TA922_Provisionistvorschlagfeld1";
	public final static String P_TA922_PROVISIONISTVORSCHLAGFELD2 = "TA922_Provisionistvorschlagfeld2";
	public final static String P_TA922_RESERVE8 = "TA922_Reserve8";
	public final static String P_TA922_RESERVE9 = "TA922_Reserve9";
	public final static String P_TA922_VORSCHLAG_MWST_CODE = "TA922_Vorschlag-MWST-Code";
	public final static String P_TA922_STEUERNUMMER_CH = "TA922_Steuernummer-CH";
	public final static String P_TA922_STEUERNUMMER_EU = "TA922_Steuernummer-EU";
	public final static String P_TA922_KONTIERUNGSVORSCHLAGNUMMER = "TA922_Kontierungsvorschlagnummer";
	public final static String P_TA922_UMSATZDEBITORNUMMER = "TA922_Umsatzdebitornummer";
	public final static String P_TA922_VORSCHLAG_VERANTWORTLICHER = "TA922_Vorschlag-Verantwortlicher";
	public final static String P_TA922_CHECK_KREDITLIMITE_UMSATZDEBITOR = "TA922_Check-Kreditlimite-Umsatzdebitor";
	public final static String P_TA922_KENNZEICHNUNG_SAMMELDEBITOR = "TA922_Kennzeichnung-Sammeldebitor";
	public final static String P_TA922_VORSCHLAG_GESCHAEFTSBEREICH = "TA922_Vorschlag-Geschaeftsbereich";
	public final static String P_TA922_VORSCHLAG_INTERCOMPANY = "TA922_Vorschlag-Intercompany";
	public final static String P_TA922_VORSCHLAG_KST_GRUPPE = "TA922_Vorschlag-KST-Gruppe";
	public final static String P_TA922_WAEHRUNGSRISIKO_CHECK = "TA922_Waehrungsrisiko-Check";
	public final static String P_TA922_WAEHRUNGSRISIKO_BETRAG = "TA922_Waehrungsrisiko-Betrag";
	public final static String P_TA922_FREIE_KUNDENIDENTIFIKATION = "TA922_freie-Kundenidentifikation";
	public final static String P_TA922_MEHRERE_WAEHRUNGEN_PRO_KUNDE = "TA922_mehrere-Waehrungen-pro-Kunde";
	public final static String P_TA922_ENDSEQUENZ = "TA922_Endsequenz";
	// TA995
	public final static String P_TA995_UEBERTRAGUNGSNUMMER = "TA995_Uebertragungsnummer";
	public final static String P_TA995_ZEILENNUMMER = "TA995_Zeilennummer";
	public final static String P_TA995_TRANSAKTIONSART = "TA995_Transaktionsart";
	public final static String P_TA995_TRANSAKTIONSSUMME = "TA995_Transaktionssumme";
	public final static String P_TA995_ENDSEQUENZ = "TA995_Endsequenz";

	private int iRowCount;
	private static int iUebertragungsnummer = 0;

	private boolean bFirst = true;

	private Vector<Integer> vKundeBereitsExportiert;

	private Integer iCheckSumm;

	/**
	 * exportiereDaten
	 * 
	 * @param fibuExportDtos
	 *            FibuexportDto[]
	 * @return String
	 * @throws EJBExceptionLP
	 * @todo Diese com.lp.server.finanz.bl.FibuExportFormatter-Methode
	 *       implementieren
	 */
	protected String exportiereDaten(FibuexportDto[] fibuExportDtos)
			throws EJBExceptionLP {
		String sExportDaten = "";
		try {
			if (fibuExportDtos != null) {
				if (bFirst) {
					sExportDaten = sExportDaten + exportiereTA005();
					iCheckSumm = iCheckSumm + 5;
					iRowCount++;
					bFirst = false;
				}
				if (!vKundeBereitsExportiert.contains(fibuExportDtos[0]
						.getPartnerDto().getIId())) {
					vKundeBereitsExportiert.add(fibuExportDtos[0]
							.getPartnerDto().getIId());
					sExportDaten = sExportDaten
							+ exportiereTA920(fibuExportDtos[0]);
					iRowCount++;
					iCheckSumm = iCheckSumm + 920;
					sExportDaten = sExportDaten
							+ exportiereTA922(fibuExportDtos[0]);
					iCheckSumm = iCheckSumm + 922;
					iRowCount++;
				}
				sExportDaten = sExportDaten + exportiereTA100(fibuExportDtos);
				iRowCount++;
				iCheckSumm = iCheckSumm + 100;
				sExportDaten = sExportDaten
						+ exportiereTA101(fibuExportDtos[0]);
				iRowCount++;
				iCheckSumm = iCheckSumm + 101;
				for (int i = 1; i < fibuExportDtos.length; i++) {
					sExportDaten = sExportDaten
							+ exportiereTA102(fibuExportDtos[i]);
					iRowCount++;
					iCheckSumm = iCheckSumm + 102;
				}
			}
			sExportDaten = sExportDaten.replace(",", ".");
			sExportDaten = sExportDaten.replace(";null;", ";;");
			sExportDaten = sExportDaten.replace(";", ",");
			sExportDaten = sExportDaten.replace(",null,", ",,");

		} catch (RemoteException ex) {
		}
		return sExportDaten;
	}

	private String exportiereTA922(FibuexportDto fibuExportDto) {
		LpMailText mt = new LpMailText();
		mt.addParameter(P_TA922_UEBERTRAGUNGSNUMMER, iUebertragungsnummer + "");
		mt.addParameter(P_TA922_ZEILENNUMMER, iRowCount + "");
		mt.addParameter(P_TA922_TRANSAKTIONSART, "\"922\"");
		mt.addParameter(P_TA922_KUNDENNUMMER, fibuExportDto.getKontonummer());
		mt.addParameter(P_TA922_RESERVE1, "\"\"");
		try {
			KundeDto kundeDto = getKundeFac()
					.kundeFindByiIdPartnercNrMandantOhneExc(
							fibuExportDto.getPartnerDto().getIId(),
							theClientDto.getMandant(), theClientDto);
			if (kundeDto != null) {
				mt.addParameter(P_TA922_VORSCHLAGS_ISO_CODE, "\""
						+ kundeDto.getCWaehrung() + "\"");
			} else {
				mt.addParameter(P_TA922_VORSCHLAGS_ISO_CODE, "");
			}
		} catch (RemoteException e) {
		} catch (EJBExceptionLP e) {
		}
		mt.addParameter(P_TA922_RESERVE2, "\"\"");
		// Woher kommen FIBU kontos??
		mt.addParameter(P_TA922_FIBU_KONTO, "");
		mt.addParameter(P_TA922_VORAUSZAHLUNGSKONTO, "-1");
		mt.addParameter(P_TA922_SKONTO_KONTO, "-1");
		mt.addParameter(P_TA922_KURSGEWINN_KONTO, "-1");
		mt.addParameter(P_TA922_KURSVERLUST_KONTO, "-1");
		mt.addParameter(P_TA922_ERTRAGS_KONTO, "-1");
		mt.addParameter(P_TA922_RESERVE3, "\"\"");
		mt.addParameter(P_TA922_SKONTO_KST, "");
		mt.addParameter(P_TA922_ZAHLUNGSKONDITIONEN, "-1");
		mt.addParameter(P_TA922_MAHNCODE, "\"F\"");
		mt.addParameter(P_TA922_RESERVE4, "\"\"");
		mt.addParameter(P_TA922_GRUPPENNUMMER, "");
		mt.addParameter(P_TA922_RESERVE5, "\"\"");
		mt.addParameter(P_TA922_RESERVE6, "\"\"");
		mt.addParameter(P_TA922_KREDITLIMITE, "");
		mt.addParameter(P_TA922_BEMERKUNG, "\"\"");
		mt.addParameter(P_TA922_RESERVE7, "\"\"");
		mt.addParameter(P_TA922_AENDERUNGEN, "\"C\"");
		mt.addParameter(P_TA922_DEBI_VORSCHLAEGE, "");
		mt.addParameter(P_TA922_KURSGEWINN_KST, "");
		mt.addParameter(P_TA922_KURSVERLUST_KST, "");
		mt.addParameter(P_TA922_ERTRAGS_KST, "");
		mt.addParameter(P_TA922_TA_VERSION, "3");
		mt.addParameter(P_TA922_PROVISIONISTVORSCHLAGFELD1, "");
		mt.addParameter(P_TA922_PROVISIONISTVORSCHLAGFELD2, "");
		mt.addParameter(P_TA922_RESERVE8, "");
		mt.addParameter(P_TA922_RESERVE9, "");
		mt.addParameter(P_TA922_VORSCHLAG_MWST_CODE, "");
		mt.addParameter(P_TA922_STEUERNUMMER_CH, "");
		mt.addParameter(P_TA922_STEUERNUMMER_EU, "");
		mt.addParameter(P_TA922_KONTIERUNGSVORSCHLAGNUMMER, "-1");
		mt.addParameter(P_TA922_UMSATZDEBITORNUMMER, "");
		mt.addParameter(P_TA922_VORSCHLAG_VERANTWORTLICHER, "");
		mt.addParameter(P_TA922_CHECK_KREDITLIMITE_UMSATZDEBITOR, "");
		mt.addParameter(P_TA922_KENNZEICHNUNG_SAMMELDEBITOR, "");
		mt.addParameter(P_TA922_VORSCHLAG_GESCHAEFTSBEREICH, "");
		mt.addParameter(P_TA922_VORSCHLAG_INTERCOMPANY, "");
		mt.addParameter(P_TA922_VORSCHLAG_KST_GRUPPE, "");
		mt.addParameter(P_TA922_WAEHRUNGSRISIKO_CHECK, "");
		mt.addParameter(P_TA922_WAEHRUNGSRISIKO_BETRAG, "");
		mt.addParameter(P_TA922_FREIE_KUNDENIDENTIFIKATION, "");
		mt.addParameter(P_TA922_MEHRERE_WAEHRUNGEN_PRO_KUNDE, "1");
		mt.addParameter(P_TA922_ENDSEQUENZ, "\"EN$\"");
		return mt.transformText(FinanzReportFac.REPORT_MODUL, theClientDto
				.getMandant(), getXSLFile() + XSL_FILE_ABACUS_TA922,
				theClientDto.getLocMandant(), theClientDto)
				+ "\n";
	}

	private String exportiereTA005() throws EJBExceptionLP, RemoteException {
		LpMailText mt = new LpMailText();
		mt.addParameter(P_TA005_UEBERTRAGUNGSNUMMER, iUebertragungsnummer + "");
		mt.addParameter(P_TA005_ZEILENNUMMER, iRowCount + "");
		mt.addParameter(P_TA005_TRANSAKTIONSART, "\"005\"");
		mt.addParameter(P_TA005_INITSEQUENZ, "\"IN$SE\"");
		mt.addParameter(P_TA005_VERSION, "\"1.3\"");
		mt.addParameter(P_TA005_RESERVE1, "");
		mt.addParameter(P_TA005_RESERVE2, "");
		mt.addParameter(P_TA005_SEQUENTIELLE_NUMMER, iUebertragungsnummer + "");
		Calendar cal = Calendar.getInstance();
		mt.addParameter(P_TA005_ERSTELLUNGSDATUM, formatDatum(cal.getTime())
				+ "");
		mt.addParameter(P_TA005_TEILNEHMER_NUMMER, "");
		mt.addParameter(P_TA005_TEILNEHMER_FILE, "\"\"");
		mt.addParameter(P_TA005_RESERVE3, "\"\"");
		mt.addParameter(P_TA005_RESERVE4, "\"\"");
		mt.addParameter(P_TA005_RESERVE5, "\"\"");
		mt.addParameter(P_TA005_ENDSEQUENZ, "\"EN$\"");
		return mt.transformText(FinanzReportFac.REPORT_MODUL, theClientDto
				.getMandant(), getXSLFile() + XSL_FILE_ABACUS_TA005,
				theClientDto.getLocMandant(), theClientDto)
				+ "\n";
	}

	private String exportiereTA920(FibuexportDto fibuExportDto) {
		LpMailText mt = new LpMailText();
		mt.addParameter(P_TA920_UEBERTRAGUNGSNUMMER, iUebertragungsnummer + "");
		mt.addParameter(P_TA920_ZEILENNUMMER, iRowCount + "");
		mt.addParameter(P_TA920_TRANSAKTIONSART, "\"920\"");
		mt.addParameter(P_TA920_KUNDENNUMMER, fibuExportDto.getKontonummer());
		mt.addParameter(P_TA920_NAME, "\""
				+ fibuExportDto.getPartnerDto().formatName().replace("\"", "'")
						.replace(",", ".") + "\"");
		mt.addParameter(P_TA920_VORNAME, "\"\"");

		mt.addParameter(P_TA920_KURZNAME, "\""
				+ Helper.cutString(fibuExportDto.getPartnerDto().getCKbez()
						.replace("\"", "'").replace(",", "."), 50) + "\"");
		mt.addParameter(P_TA920_KURZNAME_AENDERBAR, "0");
		mt.addParameter(P_TA920_ZUSATZ, "\"\"");
		if (fibuExportDto.getPartnerDto() != null) {
			if (fibuExportDto.getPartnerDto().getCStrasse() != null) {
				mt.addParameter(P_TA920_ADRESSE1, "\""
						+ fibuExportDto.getPartnerDto().getCStrasse().replace(
								"\"", "'").replace(",", ".") + "\"");
			} else {
				mt.addParameter(P_TA920_ADRESSE1, "\"" + "\"");
			}
		} else {
			mt.addParameter(P_TA920_ADRESSE1, "\"" + "\"");
		}
		mt.addParameter(P_TA920_ADRESSE2, "\"\"");
		mt.addParameter(P_TA920_LAND, "\""
				+ fibuExportDto.getPartnerDto().getLandplzortDto().getLandDto()
						.getCLkz().replace("\"", "'").replace(",", ".") + "\"");
		mt.addParameter(P_TA920_PLZ, "\""
				+ fibuExportDto.getPartnerDto().getLandplzortDto().getCPlz()
						.replace("\"", "'").replace(",", ".") + "\"");
		mt
				.addParameter(P_TA920_ORT, "\""
						+ fibuExportDto.getPartnerDto().getLandplzortDto()
								.getOrtDto().getCName().replace("\"", "'")
								.replace(",", ".") + "\"");
		mt.addParameter(P_TA920_SPRACHCODE, "\"D\"");
		ParametermandantDto parameter = null;
		try {
			parameter = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_FINANZ_EXPORT_BMD_BENUTZERNUMMER);
		} catch (EJBExceptionLP ex) {
		} catch (RemoteException ex) {
		}
		mt.addParameter(P_TA920_IDENTIFIKATIONSNUMMER, "\""
				+ parameter.getCWert() + "\"");
		mt.addParameter(P_TA920_RESERVE, "\"\"");
		mt.addParameter(P_TA920_ADRESSNUMMER, "");
		mt.addParameter(P_TA920_ENDSEQUENZ, "\"EN$\"");
		return mt.transformText(FinanzReportFac.REPORT_MODUL, theClientDto
				.getMandant(), getXSLFile() + XSL_FILE_ABACUS_TA920,
				theClientDto.getLocMandant(), theClientDto)
				+ "\n";

	}

	private String exportiereTA100(FibuexportDto[] fibuExportDto) {
		LpMailText mt = new LpMailText();
		mt.addParameter(P_TA100_UEBERTRAGUNGSNUMMER, iUebertragungsnummer + "");
		mt.addParameter(P_TA100_ZEILENNUMMER, iRowCount + "");
		mt.addParameter(P_TA100_TRANSAKTIONSART, "\"100\"");
		mt.addParameter(P_TA100_BELEGNUMMER, fibuExportDto[0].getBelegnummer()
				.substring(3)
				+ "");
		mt
				.addParameter(P_TA100_KUNDENNUMMER, fibuExportDto[0]
						.getKontonummer());
		mt.addParameter(P_TA100_TA_VERSION, "\"4\"");
		mt.addParameter(P_TA100_ISO_CODE_BELEGWAEHRUNG, "\""
				+ fibuExportDto[0].getFremdwaehrung() + "\"");
		mt.addParameter(P_TA100_ANZAHL_POSITIONEN, fibuExportDto.length - 1
				+ "");
		mt.addParameter(P_TA100_FIBU_BELEG_DATUM, formatDatum(fibuExportDto[0]
				.getBelegdatum()));
		mt.addParameter(P_TA100_BELEG_TOTAL_LEITWAEHRUNG, fibuExportDto[0]
				.getSollbetrag()
				+ "");
		mt.addParameter(P_TA100_BELEG_TOTAL_BELEGWAEHRUNG, fibuExportDto[0]
				.getSollbetragFW()
				+ "");
		// SK Laut Hr. Fuhrer mit 0 uebergeben
		mt.addParameter(P_TA100_FREMDWAEHRUNGSKURS, /*
													 * fibuExportDto[0].getKurs()
													 * .setScale(4,
													 * BigDecimal.ROUND_HALF_UP)
													 * + ""
													 */"0");
		if (fibuExportDto[0].getZahlungszielDto() != null) {
			if (fibuExportDto[0].getZahlungszielDto()
					.getAnzahlZieltageFuerNetto() != null) {
				mt.addParameter(P_TA100_ZAHLUNGSKONDITIONEN,
						P_TA100_ZAHLUNGSKONDITION_NETTO);
			}
			if (fibuExportDto[0].getZahlungszielDto().getSkontoAnzahlTage1() != null
					|| fibuExportDto[0].getZahlungszielDto()
							.getSkontoAnzahlTage2() != null) {
				mt.addParameter(P_TA100_ZAHLUNGSKONDITIONEN,
						P_TA100_ZAHLUNGSKONDITION_SKONTO);
			}
		} else {
			mt.addParameter(P_TA100_ZAHLUNGSKONDITIONEN, "");
		}

		mt.addParameter(P_TA100_FRIST, fibuExportDto[0].getZziel());
		mt.addParameter(P_TA100_SKONTO_STUFE_1_TAGE, fibuExportDto[0]
				.getSkontotage()
				+ "");
		mt.addParameter(P_TA100_SKONTO_STUFE_1_PROZENT, fibuExportDto[0]
				.getSkontopz()
				+ "");
		mt.addParameter(P_TA100_SKONTO_STUFE_2_TAGE, fibuExportDto[0]
				.getSkontotage2()
				+ "");
		mt.addParameter(P_TA100_SKONTO_STUFE_2_PROZENT, fibuExportDto[0]
				.getSkontopz2()
				+ "");
		mt.addParameter(P_TA100_SKONTO_STUFE_3_TAGE, "");
		mt.addParameter(P_TA100_SKONTO_STUFE_3_PROZENT, "");
		mt.addParameter(P_TA100_RESERVE1, "");
		ParametermandantDto parameter = null;
		try {
			parameter = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_FINANZ_EXPORT_BMD_BENUTZERNUMMER);
		} catch (EJBExceptionLP ex) {
		} catch (RemoteException ex) {
		}
		mt.addParameter(P_TA100_IDENTIFIKATIONSNUMMER, parameter.getCWert());
		mt.addParameter(P_TA100_RESERVE2, "");
		mt.addParameter(P_TA100_KUNDE_EROEFFNEN, "\"N\"");
		mt.addParameter(P_TA100_KUNDEN_ADRESS_NUMMER, "");
		mt.addParameter(P_TA100_KURZNAME, /*
										 * fibuExportDto[0].getPartnerDto().getCKbez
										 * ().replace("\"", "'").replace(",",
										 * ".")
										 */"\"\"");
		mt.addParameter(P_TA100_VORSCHLAEGE_AUS_KUNDENNUMMER, "");
		Calendar cal = Calendar.getInstance();
		mt.addParameter(P_TA100_DEBI_BELEG_DATUM, formatDatum(cal.getTime())
				+ "");
		mt.addParameter(P_TA100_RESERVE3, "\"\"");
		mt.addParameter(P_TA100_FAELLIGKEITSDATUM, formatDatum(fibuExportDto[0]
				.getValutadatum())
				+ "");
		if (fibuExportDto[0].getBelegart()
				.equals(FibuExportManager.BELEGART_AR)) {
			mt.addParameter(P_TA100_BELEGART, "\"" + P_TA100_BELEGART_RECHNUNG
					+ "\"");
		} else {
			mt.addParameter(P_TA100_BELEGART, "\"\"");
		}
		mt.addParameter(P_TA100_GESCHAEFTSBEREICH, "");
		mt.addParameter(P_TA100_GESCHAEFTSFALL, "");
		mt.addParameter(P_TA100_KOSTENGRUPPE, "");
		mt.addParameter(P_TA100_VERANTWORTLICHER, "");
		mt.addParameter(P_TA100_BELASTUNGSWEG, "");
		mt.addParameter(P_TA100_BELEGGRUPPEN_NUMMER_1, "");
		mt.addParameter(P_TA100_BELEGGRUPPEN_NUMMER_2, "");
		mt.addParameter(P_TA100_BELEGGRUPPEN_NUMMER_3, "");
		mt.addParameter(P_TA100_BELEGCODE1, "");
		mt.addParameter(P_TA100_BELEGCODE2, "");
		mt.addParameter(P_TA100_BELEGCODE3, "");
		mt.addParameter(P_TA100_BELEGREFERENZ, "");
		mt.addParameter(P_TA100_VESR_VERFAHREN, "");
		mt.addParameter(P_TA100_EXTERNE_BELEGNUMMER, "");
		mt.addParameter(P_TA100_ENDSEQUENZ, "\"EN$\"");
		return mt.transformText(FinanzReportFac.REPORT_MODUL, theClientDto
				.getMandant(), getXSLFile() + XSL_FILE_ABACUS_TA100,
				theClientDto.getLocMandant(), theClientDto)
				+ "\n";

	}

	private String exportiereTA101(FibuexportDto fibuExportDto) {
		LpMailText mt = new LpMailText();
		mt.addParameter(P_TA101_UEBERTRAGUNGSNUMMER, iUebertragungsnummer + "");
		mt.addParameter(P_TA101_ZEILENNUMMER, iRowCount + "");
		mt.addParameter(P_TA101_TRANSAKTIONSART, "\"101\"");
		mt.addParameter(P_TA101_BELEGNUMMER, fibuExportDto.getBelegnummer()
				.substring(3));
		mt.addParameter(P_TA101_ESR_NUMMER, "");
		mt.addParameter(P_TA101_BEMERKUNG, "");
		mt.addParameter(P_TA101_ADRESSNUMMER, "");
		mt.addParameter(P_TA101_KURZNAME, /*
										 * fibuExportDto.getPartnerDto()
										 * .getCKbez().replace("\"",
										 * "'").replace(",", ".")
										 */"");
		mt.addParameter(P_TA101_PLZ,/*
									 * fibuExportDto.getPartnerDto()
									 * .formatLKZPLZOrt()
									 */"");
		mt.addParameter(P_TA101_ZUSATZ_KUNDENART, "");
		mt.addParameter(P_TA101_ZUSATZ_KUNDENNUMMER, /*
													 * fibuExportDto
													 * .getKontonummer()
													 */"");
		mt.addParameter(P_TA101_RESERVE1, "");
		mt.addParameter(P_TA101_RESERVE2, "");
		mt.addParameter(P_TA101_ISO_EINHEIT_BELEGWAEHRUNG, "\"" + /*
																 * fibuExportDto
																 * .
																 * getFremdwaehrung
																 * () +
																 */"\"");
		mt.addParameter(P_TA101_BELEG_SAMMELKONTO, "");
		mt.addParameter(P_TA101_RESERVE3, "");
		mt.addParameter(P_TA101_TA_VERSION, "3");
		mt.addParameter(P_TA101_BELEG_SAMMEL_KST_1, "");
		mt.addParameter(P_TA101_BELEG_SAMMEL_KST_2, "");
		mt.addParameter(P_TA101_MAHNDATUM, "");
		mt.addParameter(P_TA101_MAHNBEREICH, "");
		mt.addParameter(P_TA101_MAHNVERFAHREN, "");
		mt.addParameter(P_TA101_MAHN_ADRESSNUMMER, "");
		mt.addParameter(P_TA101_MAHN_ADRESSKONTAKTPERSON, "");
		mt.addParameter(P_TA101_KUNDE_KURZNAME, "");
		mt.addParameter(P_TA101_KUNDE_NAME, "");
		mt.addParameter(P_TA101_KUNDE_VORNAME, "");
		mt.addParameter(P_TA101_KUNDE_ZUSATZ, "");
		mt.addParameter(P_TA101_KUNDE_ADRESSE, "");
		mt.addParameter(P_TA101_KUNDE_LAND, "");
		mt.addParameter(P_TA101_KUNDE_PLZ, "");
		mt.addParameter(P_TA101_KUNDE_ORT, "");
		mt.addParameter(P_TA101_KUNDE_SPRACHCODE, "");
		mt.addParameter(P_TA101_KUNDE_ADRESSZEILE2, "");
		mt.addParameter(P_TA101_ENDSEQUENZ, "\"EN$\"");
		return mt.transformText(FinanzReportFac.REPORT_MODUL, theClientDto
				.getMandant(), getXSLFile() + XSL_FILE_ABACUS_TA101,
				theClientDto.getLocMandant(), theClientDto)
				+ "\n";
	}

	private String exportiereTA102(FibuexportDto fibuExportDto) {
		LpMailText mt = new LpMailText();
		mt.addParameter(P_TA102_UEBERTRAGUNGSNUMMER, iUebertragungsnummer + "");
		mt.addParameter(P_TA102_ZEILENNUMMER, iRowCount + "");
		mt.addParameter(P_TA102_TRANSAKTIONSART, "\"102\"");
		mt.addParameter(P_TA102_BELEGNUMMER, fibuExportDto.getBelegnummer()
				.substring(3));
		mt.addParameter(P_TA102_HABEN_KONTO, fibuExportDto.getKontonummer());
		mt.addParameter(P_TA102_HABEN_KST1, "");
		if (fibuExportDto.getMwstsatz() != null
				&& fibuExportDto.getMwstsatz().getIId() != null) {
			try {
				Integer iFibuMwstCode = fibuExportDto.getMwstsatz()
						.getIFibumwstcode();
				if (iFibuMwstCode != null) {
					mt.addParameter(P_TA102_MWST_CODE, "\"" + iFibuMwstCode
							+ "\"");
				} else {
					mt.addParameter(P_TA102_MWST_CODE, "\"\"");
				}
			} catch (EJBExceptionLP e) {
				mt.addParameter(P_TA102_MWST_CODE, "\"\"");
			}
		} else {
			mt.addParameter(P_TA102_MWST_CODE, "\"\"");
		}
		mt.addParameter(P_TA102_MWST_SATZ, "-1");
		mt.addParameter(P_TA102_POSITIONSBETRAG_LEITWAEHRUNG, fibuExportDto
				.getPositionsbetragLeitwaehrung());
		mt.addParameter(P_TA102_POSITIONSBETRAG_BELEGWAEHRUNG, fibuExportDto
				.getPositionsbetragBelegwaehrung());
		mt.addParameter(P_TA102_MWST_BETRAG_LEITWAEHRUNG, fibuExportDto
				.getSteuerbetrag());
		mt.addParameter(P_TA102_MWST_BETRAG_BELEGWAEHRUNG, fibuExportDto
				.getSteuerbetragFW());
		if (fibuExportDto.getText() != null) {
			mt.addParameter(P_TA102_POSITIONSTEXT, "\""
					+ fibuExportDto.getText() + "\"");
		} else {
			mt.addParameter(P_TA102_POSITIONSTEXT, "\"\"");
		}
		mt.addParameter(P_TA102_RESERVE1, "");
		mt.addParameter(P_TA102_RESERVE2, "");
		mt.addParameter(P_TA102_RESERVE3, "");
		mt.addParameter(P_TA102_RESERVE4, "");
		mt.addParameter(P_TA102_RESERVE5, "");
		mt.addParameter(P_TA102_RESERVE6, "");
		mt.addParameter(P_TA102_RESERVE7, "");
		mt.addParameter(P_TA102_RESERVE8, "");
		mt.addParameter(P_TA102_EXTERNE_REFERENZ, "");
		mt.addParameter(P_TA102_MENGEN_CODE, "");
		mt.addParameter(P_TA102_RESERVE9, "");
		mt.addParameter(P_TA102_MENGE, "");
		mt.addParameter(P_TA102_HABEN_KST2, "");
		mt.addParameter(P_TA102_TA_VERSION, "4");
		mt.addParameter(P_TA102_FIBU_CODE, "");
		mt.addParameter(P_TA102_RESERVIERT_FUER_ANLAGENART, "");
		mt.addParameter(P_TA102_RESERVIERT_FUER_ANLAGENNUMMER, "");
		mt.addParameter(P_TA102_LIEFERDATUM, "");
		mt.addParameter(P_TA102_PROVISION_ANWEISUNGS_FLAG, "");
		mt.addParameter(P_TA102_SATZFINDUNGSFLAG1, "");
		mt.addParameter(P_TA102_PROVISIONISTENFINDUNGSFLAG1, "");
		mt.addParameter(P_TA102_FELDINHALTSFLAG1, "");
		mt.addParameter(P_TA102_PROVISIONIERUNGSFELD1, "");
		mt.addParameter(P_TA102_SATZFINDUNGSFLAG2, "");
		mt.addParameter(P_TA102_PROVISIONISTENFINDUNGSFLAG2, "");
		mt.addParameter(P_TA102_FELDINHALTSFLAG2, "");
		mt.addParameter(P_TA102_PROVISIONIERUNGSFELD2, "");
		mt.addParameter(P_TA102_POSITIONSNUMMER, "");
		mt.addParameter(P_TA102_GESCHAEFTSBEREICHAUFPOSITION, "");
		mt.addParameter(P_TA102_ENDSEQUENZ, "\"EN$\"");
		return mt.transformText(FinanzReportFac.REPORT_MODUL, theClientDto
				.getMandant(), getXSLFile() + XSL_FILE_ABACUS_TA102,
				theClientDto.getLocMandant(), theClientDto)
				+ "\n";

	}

	public String exportiereTA995() {
		LpMailText mt = new LpMailText();
		iCheckSumm = iCheckSumm + 995;
		mt.addParameter(P_TA995_UEBERTRAGUNGSNUMMER, iUebertragungsnummer + "");
		mt.addParameter(P_TA995_ZEILENNUMMER, iRowCount + "");
		mt.addParameter(P_TA995_TRANSAKTIONSART, "\"995\"");
		mt.addParameter(P_TA995_TRANSAKTIONSSUMME, iCheckSumm + "");
		mt.addParameter(P_TA995_ENDSEQUENZ, "\"EN$\"");
		String helper = mt.transformText(FinanzReportFac.REPORT_MODUL,
				theClientDto.getMandant(),
				getXSLFile() + XSL_FILE_ABACUS_TA995, theClientDto
						.getLocMandant(), theClientDto)
				+ "\n";
		helper = helper.replace(",", ".");
		helper = helper.replace(";null;", ";;");
		helper = helper.replace(";", ",");
		helper = helper.replace(",null,", ",,");
		return helper;
	}

	/**
	 * exportiereUeberschrift
	 * 
	 * @return String
	 * @throws EJBExceptionLP
	 * @todo Diese com.lp.server.finanz.bl.FibuExportFormatter-Methode
	 *       implementieren
	 */
	protected String exportiereUeberschrift() throws EJBExceptionLP {
		LpMailText mt = new LpMailText();
		// TA005:
		mt.addParameter(P_TA005_UEBERTRAGUNGSNUMMER,
				P_TA005_UEBERTRAGUNGSNUMMER);
		mt.addParameter(P_TA005_ZEILENNUMMER, P_TA005_ZEILENNUMMER);
		mt.addParameter(P_TA005_TRANSAKTIONSART, P_TA005_TRANSAKTIONSART);
		mt.addParameter(P_TA005_INITSEQUENZ, P_TA005_INITSEQUENZ);
		mt.addParameter(P_TA005_VERSION, P_TA005_VERSION);
		mt.addParameter(P_TA005_RESERVE1, P_TA005_RESERVE1);
		mt.addParameter(P_TA005_RESERVE2, P_TA005_RESERVE2);
		mt.addParameter(P_TA005_SEQUENTIELLE_NUMMER,
				P_TA005_SEQUENTIELLE_NUMMER);
		mt.addParameter(P_TA005_ERSTELLUNGSDATUM, P_TA005_ERSTELLUNGSDATUM);
		mt.addParameter(P_TA005_TEILNEHMER_NUMMER, P_TA005_TEILNEHMER_NUMMER);
		mt.addParameter(P_TA005_TEILNEHMER_FILE, P_TA005_TEILNEHMER_FILE);
		mt.addParameter(P_TA005_RESERVE3, P_TA005_RESERVE3);
		mt.addParameter(P_TA005_RESERVE4, P_TA005_RESERVE4);
		mt.addParameter(P_TA005_RESERVE5, P_TA005_RESERVE5);
		mt.addParameter(P_TA005_ENDSEQUENZ, P_TA005_ENDSEQUENZ + "\n");
		// TA920:
		mt.addParameter(P_TA920_UEBERTRAGUNGSNUMMER,
				"TA920_Uebertragungsnummer");
		mt.addParameter(P_TA920_ZEILENNUMMER, "TA920_Zeilennummer");
		mt.addParameter(P_TA920_TRANSAKTIONSART, "TA920_Transaktionsart");
		mt.addParameter(P_TA920_KUNDENNUMMER, "TA920_Kundennummer");
		mt.addParameter(P_TA920_NAME, "TA920_Name");
		mt.addParameter(P_TA920_VORNAME, "TA920_Vorname");
		mt.addParameter(P_TA920_KURZNAME, "TA920_Kurzname");
		mt.addParameter(P_TA920_KURZNAME_AENDERBAR, "TA920_Kurzname-aenderbar");
		mt.addParameter(P_TA920_ZUSATZ, "TA920_Zusatz");
		mt.addParameter(P_TA920_ADRESSE1, "TA920_Adresse1");
		mt.addParameter(P_TA920_ADRESSE2, "TA920_Adresse2");
		mt.addParameter(P_TA920_LAND, "TA920_Land");
		mt.addParameter(P_TA920_PLZ, "TA920_PLZ");
		mt.addParameter(P_TA920_ORT, "TA920_Ort");
		mt.addParameter(P_TA920_SPRACHCODE, "TA920_Sprachcode");
		mt.addParameter(P_TA920_IDENTIFIKATIONSNUMMER,
				"TA920_Identifikationsnummer");
		mt.addParameter(P_TA920_RESERVE, "TA920_Reserve");
		mt.addParameter(P_TA920_ADRESSNUMMER, "TA920_Adressnummer");
		mt.addParameter(P_TA920_ENDSEQUENZ, "TA920_Endsequenz\n");

		// TA100:
		mt.addParameter(P_TA100_UEBERTRAGUNGSNUMMER,
				P_TA100_UEBERTRAGUNGSNUMMER);
		mt.addParameter(P_TA100_ZEILENNUMMER, P_TA100_ZEILENNUMMER);
		mt.addParameter(P_TA100_TRANSAKTIONSART, P_TA100_TRANSAKTIONSART);
		mt.addParameter(P_TA100_BELEGNUMMER, P_TA100_BELEGNUMMER);
		mt.addParameter(P_TA100_KUNDENNUMMER, P_TA100_KUNDENNUMMER);
		mt.addParameter(P_TA100_TA_VERSION, P_TA100_TA_VERSION);
		mt.addParameter(P_TA100_ISO_CODE_BELEGWAEHRUNG,
				P_TA100_ISO_CODE_BELEGWAEHRUNG);
		mt.addParameter(P_TA100_ANZAHL_POSITIONEN, P_TA100_ANZAHL_POSITIONEN);
		mt.addParameter(P_TA100_FIBU_BELEG_DATUM, P_TA100_FIBU_BELEG_DATUM);
		mt.addParameter(P_TA100_BELEG_TOTAL_LEITWAEHRUNG,
				P_TA100_BELEG_TOTAL_LEITWAEHRUNG);
		mt.addParameter(P_TA100_BELEG_TOTAL_BELEGWAEHRUNG,
				P_TA100_BELEG_TOTAL_BELEGWAEHRUNG);
		mt.addParameter(P_TA100_FREMDWAEHRUNGSKURS, P_TA100_FREMDWAEHRUNGSKURS);
		mt.addParameter(P_TA100_ZAHLUNGSKONDITIONEN,
				P_TA100_ZAHLUNGSKONDITIONEN);
		mt.addParameter(P_TA100_FRIST, P_TA100_FRIST);
		mt.addParameter(P_TA100_SKONTO_STUFE_1_TAGE,
				P_TA100_SKONTO_STUFE_1_TAGE);
		mt.addParameter(P_TA100_SKONTO_STUFE_1_PROZENT,
				P_TA100_SKONTO_STUFE_1_PROZENT);
		mt.addParameter(P_TA100_SKONTO_STUFE_2_TAGE,
				P_TA100_SKONTO_STUFE_2_TAGE);
		mt.addParameter(P_TA100_SKONTO_STUFE_2_PROZENT,
				P_TA100_SKONTO_STUFE_2_PROZENT);
		mt.addParameter(P_TA100_SKONTO_STUFE_3_TAGE,
				P_TA100_SKONTO_STUFE_3_TAGE);
		mt.addParameter(P_TA100_SKONTO_STUFE_3_PROZENT,
				P_TA100_SKONTO_STUFE_3_PROZENT);
		mt.addParameter(P_TA100_RESERVE1, P_TA100_RESERVE1);
		mt.addParameter(P_TA100_IDENTIFIKATIONSNUMMER,
				P_TA100_IDENTIFIKATIONSNUMMER);
		mt.addParameter(P_TA100_RESERVE2, P_TA100_RESERVE2);
		mt.addParameter(P_TA100_KUNDE_EROEFFNEN, P_TA100_KUNDE_EROEFFNEN);
		mt.addParameter(P_TA100_KUNDEN_ADRESS_NUMMER,
				P_TA100_KUNDEN_ADRESS_NUMMER);
		mt.addParameter(P_TA100_KURZNAME, P_TA100_KURZNAME);
		mt.addParameter(P_TA100_VORSCHLAEGE_AUS_KUNDENNUMMER,
				P_TA100_VORSCHLAEGE_AUS_KUNDENNUMMER);
		mt.addParameter(P_TA100_DEBI_BELEG_DATUM, P_TA100_DEBI_BELEG_DATUM);
		mt.addParameter(P_TA100_RESERVE3, P_TA100_RESERVE3);
		mt.addParameter(P_TA100_FAELLIGKEITSDATUM, P_TA100_FAELLIGKEITSDATUM);
		mt.addParameter(P_TA100_BELEGART, P_TA100_BELEGART);
		mt.addParameter(P_TA100_GESCHAEFTSBEREICH, P_TA100_GESCHAEFTSBEREICH);
		mt.addParameter(P_TA100_GESCHAEFTSFALL, P_TA100_GESCHAEFTSFALL);
		mt.addParameter(P_TA100_KOSTENGRUPPE, P_TA100_KOSTENGRUPPE);
		mt.addParameter(P_TA100_VERANTWORTLICHER, P_TA100_VERANTWORTLICHER);
		mt.addParameter(P_TA100_BELASTUNGSWEG, P_TA100_BELASTUNGSWEG);
		mt.addParameter(P_TA100_BELEGGRUPPEN_NUMMER_1,
				P_TA100_BELEGGRUPPEN_NUMMER_1);
		mt.addParameter(P_TA100_BELEGGRUPPEN_NUMMER_2,
				P_TA100_BELEGGRUPPEN_NUMMER_2);
		mt.addParameter(P_TA100_BELEGGRUPPEN_NUMMER_3,
				P_TA100_BELEGGRUPPEN_NUMMER_3);
		mt.addParameter(P_TA100_BELEGCODE1, P_TA100_BELEGCODE1);
		mt.addParameter(P_TA100_BELEGCODE2, P_TA100_BELEGCODE2);
		mt.addParameter(P_TA100_BELEGCODE3, P_TA100_BELEGCODE3);
		mt.addParameter(P_TA100_BELEGREFERENZ, P_TA100_BELEGREFERENZ);
		mt.addParameter(P_TA100_VESR_VERFAHREN, P_TA100_VESR_VERFAHREN);
		mt.addParameter(P_TA100_EXTERNE_BELEGNUMMER,
				P_TA100_EXTERNE_BELEGNUMMER);
		mt.addParameter(P_TA100_ENDSEQUENZ, P_TA100_ENDSEQUENZ + "\n");
		// TA101
		mt.addParameter(P_TA101_UEBERTRAGUNGSNUMMER,
				P_TA101_UEBERTRAGUNGSNUMMER);
		mt.addParameter(P_TA101_ZEILENNUMMER, P_TA101_ZEILENNUMMER);
		mt.addParameter(P_TA101_TRANSAKTIONSART, P_TA101_TRANSAKTIONSART);
		mt.addParameter(P_TA101_BELEGNUMMER, P_TA101_BELEGNUMMER);
		mt.addParameter(P_TA101_ESR_NUMMER, P_TA101_ESR_NUMMER);
		mt.addParameter(P_TA101_BEMERKUNG, P_TA101_BEMERKUNG);
		mt.addParameter(P_TA101_ADRESSNUMMER, P_TA101_ADRESSNUMMER);
		mt.addParameter(P_TA101_KURZNAME, P_TA101_KURZNAME);
		mt.addParameter(P_TA101_PLZ, P_TA101_PLZ);
		mt.addParameter(P_TA101_ZUSATZ_KUNDENART, P_TA101_ZUSATZ_KUNDENART);
		mt.addParameter(P_TA101_ZUSATZ_KUNDENNUMMER,
				P_TA101_ZUSATZ_KUNDENNUMMER);
		mt.addParameter(P_TA101_RESERVE1, P_TA101_RESERVE1);
		mt.addParameter(P_TA101_RESERVE2, P_TA101_RESERVE2);
		mt.addParameter(P_TA101_ISO_EINHEIT_BELEGWAEHRUNG,
				P_TA101_ISO_EINHEIT_BELEGWAEHRUNG);
		mt.addParameter(P_TA101_BELEG_SAMMELKONTO, P_TA101_BELEG_SAMMELKONTO);
		mt.addParameter(P_TA101_RESERVE3, P_TA101_RESERVE3);
		mt.addParameter(P_TA101_TA_VERSION, P_TA101_TA_VERSION);
		mt.addParameter(P_TA101_BELEG_SAMMEL_KST_1, P_TA101_BELEG_SAMMEL_KST_1);
		mt.addParameter(P_TA101_BELEG_SAMMEL_KST_2, P_TA101_BELEG_SAMMEL_KST_2);
		mt.addParameter(P_TA101_MAHNDATUM, P_TA101_MAHNDATUM);
		mt.addParameter(P_TA101_MAHNBEREICH, P_TA101_MAHNBEREICH);
		mt.addParameter(P_TA101_MAHNVERFAHREN, P_TA101_MAHNVERFAHREN);
		mt.addParameter(P_TA101_MAHN_ADRESSNUMMER, P_TA101_MAHN_ADRESSNUMMER);
		mt.addParameter(P_TA101_MAHN_ADRESSKONTAKTPERSON,
				P_TA101_MAHN_ADRESSKONTAKTPERSON);
		mt.addParameter(P_TA101_KUNDE_KURZNAME, P_TA101_KUNDE_KURZNAME);
		mt.addParameter(P_TA101_KUNDE_NAME, P_TA101_KUNDE_NAME);
		mt.addParameter(P_TA101_KUNDE_VORNAME, P_TA101_KUNDE_VORNAME);
		mt.addParameter(P_TA101_KUNDE_ZUSATZ, P_TA101_KUNDE_ZUSATZ);
		mt.addParameter(P_TA101_KUNDE_ADRESSE, P_TA101_KUNDE_ADRESSE);
		mt.addParameter(P_TA101_KUNDE_LAND, P_TA101_KUNDE_LAND);
		mt.addParameter(P_TA101_KUNDE_PLZ, P_TA101_KUNDE_PLZ);
		mt.addParameter(P_TA101_KUNDE_ORT, P_TA101_KUNDE_ORT);
		mt.addParameter(P_TA101_KUNDE_SPRACHCODE, P_TA101_KUNDE_SPRACHCODE);
		mt.addParameter(P_TA101_KUNDE_ADRESSZEILE2, P_TA101_KUNDE_ADRESSZEILE2);
		mt.addParameter(P_TA101_ENDSEQUENZ, P_TA101_ENDSEQUENZ + "\n");
		// TA102
		mt.addParameter(P_TA102_UEBERTRAGUNGSNUMMER,
				P_TA102_UEBERTRAGUNGSNUMMER);
		mt.addParameter(P_TA102_ZEILENNUMMER, P_TA102_ZEILENNUMMER);
		mt.addParameter(P_TA102_TRANSAKTIONSART, P_TA102_TRANSAKTIONSART);
		mt.addParameter(P_TA102_BELEGNUMMER, P_TA102_BELEGNUMMER);
		mt.addParameter(P_TA102_HABEN_KONTO, P_TA102_HABEN_KONTO);
		mt.addParameter(P_TA102_HABEN_KST1, P_TA102_HABEN_KST1);
		mt.addParameter(P_TA102_MWST_CODE, P_TA102_MWST_CODE);
		mt.addParameter(P_TA102_MWST_SATZ, P_TA102_MWST_SATZ);
		mt.addParameter(P_TA102_POSITIONSBETRAG_LEITWAEHRUNG,
				P_TA102_POSITIONSBETRAG_LEITWAEHRUNG);
		mt.addParameter(P_TA102_POSITIONSBETRAG_BELEGWAEHRUNG,
				P_TA102_POSITIONSBETRAG_BELEGWAEHRUNG);
		mt.addParameter(P_TA102_MWST_BETRAG_LEITWAEHRUNG,
				P_TA102_MWST_BETRAG_LEITWAEHRUNG);
		mt.addParameter(P_TA102_MWST_BETRAG_BELEGWAEHRUNG,
				P_TA102_MWST_BETRAG_BELEGWAEHRUNG);
		mt.addParameter(P_TA102_POSITIONSTEXT, P_TA102_POSITIONSTEXT);
		mt.addParameter(P_TA102_RESERVE1, P_TA102_RESERVE1);
		mt.addParameter(P_TA102_RESERVE2, P_TA102_RESERVE2);
		mt.addParameter(P_TA102_RESERVE3, P_TA102_RESERVE3);
		mt.addParameter(P_TA102_RESERVE4, P_TA102_RESERVE4);
		mt.addParameter(P_TA102_RESERVE5, P_TA102_RESERVE5);
		mt.addParameter(P_TA102_RESERVE6, P_TA102_RESERVE6);
		mt.addParameter(P_TA102_RESERVE7, P_TA102_RESERVE7);
		mt.addParameter(P_TA102_RESERVE8, P_TA102_RESERVE8);
		mt.addParameter(P_TA102_EXTERNE_REFERENZ, P_TA102_EXTERNE_REFERENZ);
		mt.addParameter(P_TA102_MENGEN_CODE, P_TA102_MENGEN_CODE);
		mt.addParameter(P_TA102_RESERVE9, P_TA102_RESERVE9);
		mt.addParameter(P_TA102_MENGE, P_TA102_MENGE);
		mt.addParameter(P_TA102_HABEN_KST2, P_TA102_HABEN_KST2);
		mt.addParameter(P_TA102_TA_VERSION, P_TA102_TA_VERSION);
		mt.addParameter(P_TA102_FIBU_CODE, P_TA102_FIBU_CODE);
		mt.addParameter(P_TA102_RESERVIERT_FUER_ANLAGENART,
				P_TA102_RESERVIERT_FUER_ANLAGENART);
		mt.addParameter(P_TA102_RESERVIERT_FUER_ANLAGENNUMMER,
				P_TA102_RESERVIERT_FUER_ANLAGENNUMMER);
		mt.addParameter(P_TA102_LIEFERDATUM, P_TA102_LIEFERDATUM);
		mt.addParameter(P_TA102_PROVISION_ANWEISUNGS_FLAG,
				P_TA102_PROVISION_ANWEISUNGS_FLAG);
		mt.addParameter(P_TA102_SATZFINDUNGSFLAG1, P_TA102_SATZFINDUNGSFLAG1);
		mt.addParameter(P_TA102_PROVISIONISTENFINDUNGSFLAG1,
				P_TA102_PROVISIONISTENFINDUNGSFLAG1);
		mt.addParameter(P_TA102_FELDINHALTSFLAG1, P_TA102_FELDINHALTSFLAG1);
		mt.addParameter(P_TA102_PROVISIONIERUNGSFELD1,
				P_TA102_PROVISIONIERUNGSFELD1);
		mt.addParameter(P_TA102_SATZFINDUNGSFLAG2, P_TA102_SATZFINDUNGSFLAG2);
		mt.addParameter(P_TA102_PROVISIONISTENFINDUNGSFLAG2,
				P_TA102_PROVISIONISTENFINDUNGSFLAG2);
		mt.addParameter(P_TA102_FELDINHALTSFLAG2, P_TA102_FELDINHALTSFLAG2);
		mt.addParameter(P_TA102_PROVISIONIERUNGSFELD2,
				P_TA102_PROVISIONIERUNGSFELD2);
		mt.addParameter(P_TA102_POSITIONSNUMMER, P_TA102_POSITIONSNUMMER);
		mt.addParameter(P_TA102_GESCHAEFTSBEREICHAUFPOSITION,
				P_TA102_GESCHAEFTSBEREICHAUFPOSITION);
		mt.addParameter(P_TA102_ENDSEQUENZ, P_TA102_ENDSEQUENZ + "\n");
		// TA922
		mt.addParameter(P_TA922_UEBERTRAGUNGSNUMMER,
				P_TA922_UEBERTRAGUNGSNUMMER);
		mt.addParameter(P_TA922_ZEILENNUMMER, P_TA922_ZEILENNUMMER);
		mt.addParameter(P_TA922_TRANSAKTIONSART, P_TA922_TRANSAKTIONSART);
		mt.addParameter(P_TA922_KUNDENNUMMER, P_TA922_KUNDENNUMMER);
		mt.addParameter(P_TA922_RESERVE1, P_TA922_RESERVE1);
		mt.addParameter(P_TA922_VORSCHLAGS_ISO_CODE,
				P_TA922_VORSCHLAGS_ISO_CODE);
		mt.addParameter(P_TA922_RESERVE2, P_TA922_RESERVE2);
		mt.addParameter(P_TA922_FIBU_KONTO, P_TA922_FIBU_KONTO);
		mt.addParameter(P_TA922_VORAUSZAHLUNGSKONTO,
				P_TA922_VORAUSZAHLUNGSKONTO);
		mt.addParameter(P_TA922_SKONTO_KONTO, P_TA922_SKONTO_KONTO);
		mt.addParameter(P_TA922_KURSGEWINN_KONTO, P_TA922_KURSGEWINN_KONTO);
		mt.addParameter(P_TA922_KURSVERLUST_KONTO, P_TA922_KURSVERLUST_KONTO);
		mt.addParameter(P_TA922_ERTRAGS_KONTO, P_TA922_ERTRAGS_KONTO);
		mt.addParameter(P_TA922_RESERVE3, P_TA922_RESERVE3);
		mt.addParameter(P_TA922_SKONTO_KST, P_TA922_SKONTO_KST);
		mt.addParameter(P_TA922_ZAHLUNGSKONDITIONEN,
				P_TA922_ZAHLUNGSKONDITIONEN);
		mt.addParameter(P_TA922_MAHNCODE, P_TA922_MAHNCODE);
		mt.addParameter(P_TA922_RESERVE4, P_TA922_RESERVE4);
		mt.addParameter(P_TA922_GRUPPENNUMMER, P_TA922_GRUPPENNUMMER);
		mt.addParameter(P_TA922_RESERVE5, P_TA922_RESERVE5);
		mt.addParameter(P_TA922_RESERVE6, P_TA922_RESERVE6);
		mt.addParameter(P_TA922_KREDITLIMITE, P_TA922_KREDITLIMITE);
		mt.addParameter(P_TA922_BEMERKUNG, P_TA922_BEMERKUNG);
		mt.addParameter(P_TA922_RESERVE7, P_TA922_RESERVE7);
		mt.addParameter(P_TA922_AENDERUNGEN, P_TA922_AENDERUNGEN);
		mt.addParameter(P_TA922_DEBI_VORSCHLAEGE, P_TA922_DEBI_VORSCHLAEGE);
		mt.addParameter(P_TA922_KURSGEWINN_KST, P_TA922_KURSGEWINN_KST);
		mt.addParameter(P_TA922_KURSVERLUST_KST, P_TA922_KURSVERLUST_KST);
		mt.addParameter(P_TA922_ERTRAGS_KST, P_TA922_ERTRAGS_KST);
		mt.addParameter(P_TA922_TA_VERSION, P_TA922_TA_VERSION);
		mt.addParameter(P_TA922_PROVISIONISTVORSCHLAGFELD1,
				P_TA922_PROVISIONISTVORSCHLAGFELD1);
		mt.addParameter(P_TA922_PROVISIONISTVORSCHLAGFELD2,
				P_TA922_PROVISIONISTVORSCHLAGFELD2);
		mt.addParameter(P_TA922_RESERVE8, P_TA922_RESERVE8);
		mt.addParameter(P_TA922_RESERVE9, P_TA922_RESERVE9);
		mt.addParameter(P_TA922_VORSCHLAG_MWST_CODE,
				P_TA922_VORSCHLAG_MWST_CODE);
		mt.addParameter(P_TA922_STEUERNUMMER_CH, P_TA922_STEUERNUMMER_CH);
		mt.addParameter(P_TA922_STEUERNUMMER_EU, P_TA922_STEUERNUMMER_EU);
		mt.addParameter(P_TA922_KONTIERUNGSVORSCHLAGNUMMER,
				P_TA922_KONTIERUNGSVORSCHLAGNUMMER);
		mt.addParameter(P_TA922_UMSATZDEBITORNUMMER,
				P_TA922_UMSATZDEBITORNUMMER);
		mt.addParameter(P_TA922_VORSCHLAG_VERANTWORTLICHER,
				P_TA922_VORSCHLAG_VERANTWORTLICHER);
		mt.addParameter(P_TA922_CHECK_KREDITLIMITE_UMSATZDEBITOR,
				P_TA922_CHECK_KREDITLIMITE_UMSATZDEBITOR);
		mt.addParameter(P_TA922_KENNZEICHNUNG_SAMMELDEBITOR,
				P_TA922_KENNZEICHNUNG_SAMMELDEBITOR);
		mt.addParameter(P_TA922_VORSCHLAG_GESCHAEFTSBEREICH,
				P_TA922_VORSCHLAG_GESCHAEFTSBEREICH);
		mt.addParameter(P_TA922_VORSCHLAG_INTERCOMPANY,
				P_TA922_VORSCHLAG_INTERCOMPANY);
		mt.addParameter(P_TA922_VORSCHLAG_KST_GRUPPE,
				P_TA922_VORSCHLAG_KST_GRUPPE);
		mt.addParameter(P_TA922_WAEHRUNGSRISIKO_CHECK,
				P_TA922_WAEHRUNGSRISIKO_CHECK);
		mt.addParameter(P_TA922_WAEHRUNGSRISIKO_BETRAG,
				P_TA922_WAEHRUNGSRISIKO_BETRAG);
		mt.addParameter(P_TA922_FREIE_KUNDENIDENTIFIKATION,
				P_TA922_FREIE_KUNDENIDENTIFIKATION);
		mt.addParameter(P_TA922_MEHRERE_WAEHRUNGEN_PRO_KUNDE,
				P_TA922_MEHRERE_WAEHRUNGEN_PRO_KUNDE);
		mt.addParameter(P_TA922_ENDSEQUENZ, P_TA922_ENDSEQUENZ + "\n");
		// TA995
		mt.addParameter(P_TA995_UEBERTRAGUNGSNUMMER,
				P_TA995_UEBERTRAGUNGSNUMMER);
		mt.addParameter(P_TA995_ZEILENNUMMER, P_TA995_ZEILENNUMMER);
		mt.addParameter(P_TA995_TRANSAKTIONSART, P_TA995_TRANSAKTIONSART);
		mt.addParameter(P_TA995_TRANSAKTIONSSUMME, P_TA995_TRANSAKTIONSSUMME);
		mt.addParameter(P_TA995_ENDSEQUENZ, P_TA995_ENDSEQUENZ);

		String sZeile = mt.transformText(FinanzReportFac.REPORT_MODUL,
				theClientDto.getMandant(), getXSLFile(), theClientDto
						.getLocMandant(), theClientDto);
		sZeile = sZeile.replace(";", ",");
		sZeile = sZeile.replace("\n,", "\n");
		return sZeile;
	}

	/**
	 * getXSLFile
	 * 
	 * @return String
	 * @todo Diese com.lp.server.finanz.bl.FibuExportFormatter-Methode
	 *       implementieren
	 */
	protected String getXSLFile() {
		return XSL_FILE_ABACUS;
	}

	/**
	 * uebersetzeUSTLand
	 * 
	 * @param sLKZ
	 *            String
	 * @return String
	 * @todo Diese com.lp.server.finanz.bl.FibuExportFormatter-Methode
	 *       implementieren
	 */
	protected String uebersetzeUSTLand(String sLKZ) {
		return "";
	}

	private String formatDatum(java.util.Date d) {
		if (d != null) {
			DateFormat df = new SimpleDateFormat("dd/MM/yy");
			return df.format(d);
		} else {
			return "";
		}
	}

	FibuExportFormatterAbacus(FibuExportKriterienDto exportKriterienDto,
			TheClientDto theClientDto) {
		super(exportKriterienDto, theClientDto);
		iRowCount = 1;
		iCheckSumm = 0;
		vKundeBereitsExportiert = new Vector<Integer>();
	}
}
