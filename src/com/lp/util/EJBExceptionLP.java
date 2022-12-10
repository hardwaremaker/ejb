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
package com.lp.util;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ejb.ApplicationException;

import com.lp.server.util.logger.LPLogService;

/**
 * <p>
 * exccatch: Diese Klasse kuemmert sich um Exceptions die an den Client (auch
 * anderes Servermodul) durchgereicht werden.
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellung: Josef Ornetsmueller; dd.mm.05
 * </p>
 * 
 * <p>
 * 
 * @author $Author: adi $
 *         </p>
 * 
 * @version not attributable Date $Date: 2013/01/25 17:38:57 $
 */
// Durch diese Annotation wird die EJBExeptionLP 'ungewrapped' and den Aufrufer
// geschickt.
// HIER KEINE AENDERUNGEN DURCHFUEHREN
// DARF NICHT AUF FALSE GESETZT WERDEN, DA SONST KEIN ROLLBACK BEI FEHLERN
// ERFOGLT
@ApplicationException(rollback = true)
public class EJBExceptionLP extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static final boolean enableLog = false;

	// except: 1 hier eine Konstante definieren.
	public static final int NO_VALUE = -9999;
	public static final int FEHLER = -1;

	// 0 bis 500 Server alle Module uebergreifende Fehler
	// ***************************
	public static final int FEHLER_IM_BENUTZER = 0;
	public static final int FEHLER_IM_KENNWORT = 1;
	public static final int FEHLER_FALSCHES_KENNWORT = 2;
	public static final int FEHLER_BEIM_ANLEGEN = 3;
	public static final int FEHLER_BEIM_LOESCHEN = 4;
	public static final int FEHLER_BEIM_UPDATE = 5;
	public static final int FEHLER_BEI_FINDBYPRIMARYKEY = 6;
	public static final int FEHLER_FLR = 7;
	public static final int FEHLER_DTO_IS_NULL = 8;
	public static final int FEHLER_PKFIELD_IS_NULL = 9;
	public static final int FEHLER_FELD_IN_DTO_IS_NULL = 10;
	public static final int FEHLER_MUSS_GROESSER_0_SEIN = 11;
	public static final int FEHLER_PK_GENERATOR = 12;
	public static final int FEHLER_FELD_DARF_NICHT_NULL_SEIN = 13;
	public static final int FEHLER_DUPLICATE_UNIQUE = 14;
	public static final int FEHLER_IS_ALREADY_LOCKED = 15;
	public static final int FEHLER_HOME_IS_NULL = 16;
	public static final int FEHLER_LOCK_NOTFOUND = 17;
	public static final int FEHLER_BEIM_DRUCKEN = 18;
	public static final int FEHLER_BEIM_STORNIEREN = 19;
	public static final int FEHLER_BEI_FINDALL = 20;
	public static final int FEHLER_ZUWENIG_AUF_LAGER = 21;
	public static final int FEHLER_DUPLICATE_PRIMARY_KEY = 22;
	public static final int FEHLER_BEI_FIND = 23;
	public static final int FEHLER_MENGENREDUZIERUNG_NICHT_MOEGLICH = 24;
	public static final int FEHLER_ZAHL_ZU_KLEIN = 27;
	public static final int FEHLER_ZAHL_ZU_GROSS = 28;
	public static final int FEHLER_PARAMETER_IS_NULL = 29;
	public static final int FEHLER_STATUS = 30;
	public static final int WARNUNG_99_KUNDEN_PRO_BUCHSTABE = 31;
	public static final int FEHLER_C_NR_USER_IS_NULL = 32;
	public static final int FEHLER_KUNDE_HAT_KEINE_STANDARDPREISLISTE_HINTERLEGT = 33;
	public static final int FEHLER_DATEN_IN = 33;
	public static final int FEHLER_DATEN_INKOMPATIBEL = 34;
	public static final int FEHLER_KEIN_WECHSELKURS_HINTERLEGT = 35;
	public static final int FEHLER_MANDANTPARAMETER_NICHT_ANGELEGT = 36;
	public static final int FEHLER_DARF_MIR_NICHT_MICH_SELBST_ZUORDNEN = 37;
	// public static final int FEHLER_REPORT_KONNTE_NICHT_GELADEN_WERDEN = 38;
	public static final int FEHLER_NOT_IMPLEMENTED_YET = 39;
	public static final int FEHLER_BEI_EJBSELECT = 40;
	public static final int FEHLER_FALSCHER_MANDANT = 41;
	public static final int FEHLER_UNZUREICHENDE_RECHTE = 42;
	public static final int FEHLER_HIBERNATE = 45;
	public static final int FEHLER_BENUTZER_IST_GESPERRT = 46;
	public static final int FEHLER_BENUTZER_IST_NICHT_MEHR_GUELTIG = 47;
	public static final int FEHLER_BENUTZER_DARF_SICH_BEI_DIESEM_MANDANTEN_NICHT_ANMELDEN = 48;
	public static final int FEHLER_STAMMDATENCRUD_ZUVIELE_CONTROLS = 49;
	public static final int FEHLER_STAMMDATENCRUD_FEHLER = 50;
	public static final int FEHLER_PARAMETER_IS_NOT_NULL = 51;
	public static final int FEHLER_TRANSAKTION_NICHT_DURCHGEFUEHRT__ROLLBACK = 52;
	public static final int FEHLER_KEINE_VERBINDUNG_ZUM_JBOSS = 53;

	public static final int FEHLER_BUILDNUMMER_CLIENT_SERVER = 54;
	public static final int FEHLER_BUILDNUMMER_SERVER_DB = 55;

	public static final int FEHLER_FORMAT_NUMBER = 56;
	public static final int FEHLER_BELEG_WURDE_NICHT_MANUELL_ERLEDIGT = 57;
	public static final int FEHLER_NULLPOINTEREXCEPTION = 58;
	public static final int WARNUNG_CLIENT_UHRZEIT_UNGLEICH_SERVER_UHRZEIT = 60;
	public static final int WARNUNG_SERVER_JVM = 61;
	public static final int FEHLER_EINHEITKONVERTIERUNG_KEIN_DIVISOR_DEFINIERT = 62;
	public static final int FEHLER_ZUGEBUCHTES_MATERIAL_BEREITS_VOM_LAGER_ENTNOMMEN = 63;
	public static final int FEHLER_BENUTZER_KEIN_EINTRAG_IN_BENUTZERMANDANT = 64;
	public static final int FEHLER_UNBEKANNTER_DATENTYP = 65;
	public static final int FEHLER_WERT_NICHT_KONVERTIERBAR = 66;
	public static final int FEHLER_BELEG_DARF_NICHT_INS_NAECHSTE_GJ_DATIERT_WERDEN = 67;
	public static final int FEHLER_BELEG_DARF_NICHT_INS_VORLETZTE_GJ_DATIERT_WERDEN = 68;
	public static final int FEHLER_BELEG_DARF_NICHT_IN_EIN_ANDERES_GJ_UMDATIERT_WERDEN = 69;
	public static final int FEHLER_BUILD_CLIENT = 70;
	public static final int FEHLER_DRUCKEN_POSITIONSART = 71;
	public static final int FEHLER_UNGUELTIGE_EMAILADRESSE = 72;
	public static final int FEHLER_UNGUELTIGE_FAXNUMMER = 73;
	public static final int FEHLER_ENDSUMME_EXISTIERT = 74;
	public static final int FEHLER_ENDSUMME_NICHTVORPREISBEHAFTETERPOSITION = 75;
	public static final int FEHLER_UNGUELTIGE_ZEITEINGABE = 76;
	public static final int FEHLER_UNGUELTIGE_ZAHLENEINGABE = 77;
	public static final int FEHLER_WEB_SERVICE = 78;
	public static final int FEHLER_GET_AS_XML = 79;
	public static final int FEHLER_BENUTZER_DARF_SICH_IN_DIESER_SPRACHE_NICHT_ANMELDEN = 80;
	public static final int FEHLER_COPY_PASTE = 81;
	public static final int FEHLER_NOCLASSDEFFOUNDERROR = 82;
	public static final int FEHLER_THECLIENT_WURDE_GELOESCHT = 83;
	public static final int FEHLER_BELEG_HAT_KEINE_POSITIONEN = 84;
	public static final int FEHLER_BELEG_IST_BEREITS_AKTIVIERT = 85;
	public static final int FEHLER_SPRACHE_NICHT_AKTIV = 86;
	public static final int FEHLER_NAMING_EXCEPTION = 87;
	public static final int FEHLER_NO_UNIQUE_RESULT = 88;
	public static final int FEHLER_FLR_DRUCK_VORLAGE_UNVOLLSTAENDIG = 89;
	public static final int FEHLER_BEIM_ANLEGEN_ENTITY_EXISTS = 90;
	public static final int FEHLER_TRANSACTION_TIMEOUT = 91;
	public static final int FEHLER_ABBUCHUNG_OHNE_URSPRUNGSEINTRAG_NICHT_MOEGLICH = 92;
	public static final int FEHLER_JCR_KNOTEN_EXISTIERT_BEREITS = 93;
	public static final int FEHLER_JCR_DATEI_KONNTE_NICHT_GELESEN_WERDEN = 94;
	public static final int FEHLER_JCR_KNOTEN_NICHT_GESPEICHERT = 95;
	public static final int FEHLER_SQL_EXCEPTION_MIT_INFO = 96;
	public static final int FEHLER_UNGUELTIGE_INSTALLATION = 97;
	public static final int FEHLER_JCR_KEINE_AUFTRAEGE_ZU_KOPIEREN = 98;
	public static final int FEHLER_MAXIMALE_BENUTZERANZAHL_UEBERSCHRITTEN = 99;
	public static final int FEHLER_STEUERSATZ_INNERHALB_UNTERPOSITIONEN_UNGLEICH = 100;
	public static final int FEHLER_KUNDE_STANDARDPREISLISTE_HAT_FALSCHE_WAEHRUNG = 101;
	public static final int FEHLER_KEINE_BERECHTIGUNG_BELEG_AKTIVIEREN = 102;
	public static final int FEHLER_LIZENZ_ABGELAUFEN = 103;
	public static final int FEHLER_BEIM_KENNWORT_VERSCHLUESSELN = 104;
	public static final int FEHLER_BEIM_KENNWORT_ENTSCHLUESSELN = 105;
	public static final int FEHLER_ISORT_DUPLICATE_UNIQUE = 106;
	public static final int FEHLER_PATERNOSTERKOMMUNIKATIONSFEHLER = 107;
	public static final int FEHLER_IM_WECHSELKURS_KEINE_MANDANTENWAEHRUNG_ENTHALTEN = 108;
	public static final int FEHLER_POSITION_VERTAUSCHEN_ZWISCHENSUMME_VONBIS = 109;
	public static final int FEHLER_POSITION_VERTAUSCHEN_ZWISCHENSUMME_IN_SICH_SELBST = 110;
	public static final int FEHLER_POSITION_VERTAUSCHEN_STATUS = 111;
	public static final int FEHLER_POSITION_VERTAUSCHEN_KEINE_POS_VORHANDEN = 112;
	public static final int FEHLER_INVALID_REPORT_URL = 113;

	public static final int FEHLER_KEYSTORE = 114;
	public static final int FEHLER_KEYSTORE_MANAGMENT = 115;
	public static final int FEHLER_KEYSTORE_RECOVER = 116;
	public static final int FEHLER_KEYSTORE_ALGORITHMEN = 117;
	public static final int FEHLER_KEYSTORE_CERTIFICATE = 118;

	public static final int FEHLER_HTTP_POST_IO = 119;

	public static final int FEHLER_DOKUMENTENABLAGE_OFFLINE = 120;
	public static final int FEHLER_BEIM_AKTIVIEREN_BELEG_WURDE_GEAENDERT = 121;

	/**
	 * Eine Script-Datei wurde im Server-Verzeichnis nicht gefunden</br>
	 */
	// TODO: Vorerst hier in diesem Bereich. Sollten noch mehrere Script-Fehler
	// dazukommen, dann eigenen Bereich machen
	public static final int FEHLER_SCRIPT_NICHT_GEFUNDEN = 122;

	/**
	 * Die Zwischensummenposition ist unvollst&auml;ndig
	 */
	public static final int FEHLER_POSITION_ZWISCHENSUMME_UNVOLLSTAENDIG = 123;
	public static final int FEHLER_FLRDRUCK_SPALTE_NICHT_VORHANDEN = 124;
	public static final int FEHLER_PJ18612_BENUTZER_MUSS_AN_MANDANT_002_ANGEMELDET_SEIN = 125;

	/**
	 * Die zu ver&auml;ndernden Daten wurden zwischenzeitlich modifiziert
	 */
	public static final int FEHLER_DATEN_MODIFIZIERT_UPDATE = 126;
	public static final int FEHLER_DATEN_MODIFIZIERT_REMOVE = 127;

	/**
	 * Der Root-Node existiert nicht, bzw. es kann nicht auf ihn zugegriffen werden
	 */
	public static final int FEHLER_JCR_ROOT_EXISTIERT_NICHT = 128;

	public static final int FEHLER_SORTIER_LISTE_ENTHAELT_NULL = 129;

	public static final int FEHLER_KEINE_MODULBERECHTIGUNG = 130;

	public static final int FEHLER_EJB = 131;
	public static final int FEHLER_BENUTZER_AUTOMATISCHE_BELEGANLAGE_NICHT_DEFINIERT = 132;

	public static final int FEHLER_VERSANDWEG_FUER_BELEGART_UNBEKANNT = 133;
	public static final int FEHLER_ES_WERDEN_NUR_XLS_BIS_2007_UNTERSTUETZT = 134;
	public static final int FEHLER_SCRIPT_NICHT_AUSFUEHRBAR = 135;
	public static final int FEHLER_SCRIPT_FEHLERHAFTE_STAMMDATEN = 136;
	public static final int FEHLER_BERECHTIGUNG_ZUSATZFUNKTION = 137;

	public static final int FEHLER_SCRIPT_NICHT_UEBERSETZBAR = 138;
	public static final int FEHLER_SCRIPT_KLASSE_NICHT_GEFUNDEN = 139;
	public static final int FEHLER_SCRIPT_METHODE_NICHT_GEFUNDEN = 140;
	public static final int FEHLER_SCRIPT_PARAMETER_MEHRFACH_DEFINIERT = 141;
	public static final int FEHLER_SCRIPT_INSTANZIERUNG_NICHT_MOEGLICH = 142;
	public static final int FEHLER_SCRIPT_FALSCHE_ZUGRIFFSRECHTE = 143;
	public static final int FEHLER_SCRIPT_INSTANZIERUNG_NICHT_MOEGLICH_IO = 144;
	public static final int FEHLER_SCRIPT_METHODE_NICHT_AUFRUFBAR = 145;
	public static final int FEHLER_SCRIPT_PARAMETER_NICHT_GEFUNDEN = 146;
	public static final int FEHLER_SCRIPT_INSTANZIERUNG_NICHT_MOEGLICH_VERIFY = 147;

	public final static int FEHLER_FEHLENDES_MAPPING_TABELLENNAME_USECASE = 148;
	public final static int FEHLER_DUPLICATE_UNIQUE_EXTENDED = 149;
	public final static int FEHLER_NO_UNIQUE_RESULT_EXTENDED = 150;
	public final static int FEHLER_BETREFF_IST_LEER = 151;
	public final static int FEHLER_UNGUELTIGE_EMAILADRESSE_EXTENDED = 152;
	public final static int FEHLER_RESTAPI_BENOETIGT_SYSTEMROLLE_RESTAPI = 153;
	public final static int FEHLER_BENUTZER_SYSTEMROLLE_HVMA_NICHT_GESETZT = 154;
	public final static int FEHLER_DOKUMENT_NICHT_IN_PDF_UMWANDELBAR = 155;
	public final static int FEHLER_POSITION_ZWISCHENSUMME_NICHT_UMSCHLIESSEND = 156;
	public final static int FEHLER_MATERIALBUCHUNG_AUF_LOS_KOMMT_VON_SICH_SELBST = 157;
	public final static int FEHLER_MWSTSATZ_NICHT_GEFUNDEN = 158;
	public final static int FEHLER_MEHRERE_MWSTSAETZE_IM_DATUMSBEREICH = 159;
	public final static int FEHLER_ZWISCHENSUMME_0 = 160;

	public static final int FEHLER_ZUORDNUNG_ANSPRECHPARTNER_ZU_PARTNER = 161;

	public static final int FEHLER_UNTERSCHIEDLICHE_KEY_KLASSEN = 162;
	public static final int FEHLER_KAPAZITAET_TEXTBLOCK_UEBERSCHRITTEN = 163;
	
	// 501 bis 999 Client Fehler
	// ****************************************************
	public static final int FEHLER_DLG_DONE_DO_NOTHING = 501;
	public static final int FEHLER_FEHLER_BEIM_DRUCKEN = 502;

	public static final int FEHLER_TELEFON_WAHLVORGANG = 503;
	public static final int FEHLER_ATTACHMENTS_ZU_GROSS = 504;

	// 1000-1999: Auftrag
	// ***********************************************************
	public static final int FEHLER_KEIN_EIGENTUMSVORBEHALT_DEFINIERT = 1000;
	public static final int FEHLER_KEINE_LIEFERBEDINGUNGEN_DEFINIERT = 1001;
	public static final int FEHLER_ARTIKELAENDERUNG_BEI_RAHMENPOSUPDATE_NICHT_ERLAUBT = 1002;
	public static final int FEHLER_MENGENAENDERUNG_UNTER_ABGERUFENE_MENGE_NICHT_ERLAUBT = 1003;
	public static final int FEHLER_ABRUFAUFTRAG_KANN_NICHT_MEHR_VERAENDERT_WERDEN = 1004;
	public static final int FEHLER_RAHMENAUFTRAG_IST_IM_STATUS_ANGELEGT = 1005;
	public static final int FEHLER_ALLE_LOSE_BERUECKSICHTIGEN_UND_SAMMELLIEFERSCHIEN_MEHRERE_AUFTRAEGE = 1006;
	public static final int FEHLER_AUFTRAG_AKTIVIERT_ABER_KEIN_WERT = 1007;
	public static final int FEHLER_ES_MUSS_MINDESTENS_EIN_MEILENSTEIN_VORHANDEN_SEIN = 1008;
	public static final int FEHLER_AUFTRAG_AUS_BESTELLUNG_KUNDE_NICHT_ANGELEGT = 1009;
	public static final int FEHLER_AUFTRAG_KEINE_SNRS_AUF_POSITION = 1010;
	public static final int FEHLER_KALKULATORISCHER_ARTIKEL_MIT_PREIS_UNGLEICH_0 = 1011;
	public static final int FEHLER_AUFTRAG_AUS_BESTELLUNG_LIEFERADRESSE_NICHT_ANGELEGT = 1012;
	public static final int FEHLER_AUFTRAG_KEIN_VERRECHNUNGSMODELL = 1013;
	public static final int FEHLER_ZEITDATEN_UEBERLEITUNG_AUFTRAG_KEIN_VERRECHNUNGSMODELL = 1014;
	public static final int FEHLER_ZEITDATEN_UEBERLEITUNG_KUNDE_KEIN_VERRECHNUNGSMODELL = 1015;
	public static final int FEHLER_ZEITDATEN_UEBERLEITUNG_MASCHINE_KEIN_VERRECHNUNGSARTIKEL = 1016;
	public static final int FEHLER_ER_UEBERLEITUNG_MEHRERE_ARTIKEL_ODER_AUFSCHLAEGE = 1017;
	public static final int FEHLER_REISE_UEBERLEITUNG_MEHRERE_ARTIKEL_FUER_KILOMETER_ODER_SPESEN = 1018;
	public static final int FEHLER_ZUGEHOERIGE_BESTELLUNG_IST_ERLEDIGT = 1019;
	public static final int FEHLER_AUFTRAG_VERSANDWEG_IM_PARTNER_NICHT_DEFINIERT = 1020;
	public static final int FEHLER_AUFTRAG_VERSANDWEG_PARTNER_KUNDENNUMMER_FEHLT = 1021;
	public static final int FEHLER_AUFTRAG_SON_IMPORT = 1022;
	public static final int FEHLER_AUFTRAG_VAT_IMPORT = 1023;
	public static final int FEHLER_AUFTRAG_LIEFERANTENNUMMER_FEHLT = 1024;
	public static final int FEHLER_AUFTRAG_KUNDENKENNUNG_FEHLT = 1025;
	public static final int FEHLER_AUFTRAG_WAEHRUNG_FEHLT = 1026;
	public static final int FEHLER_AUFTRAG_BESTELLPOSREFERENZ_FEHLT = 1027;
	public static final int FEHLER_AUFTRAG_VERSANDWEG_ENDPUNKT_FEHLT = 1028;
	public static final int FEHLER_AUFTRAG_VERSANDWEG_BENUTZER_FEHLT = 1029;
	public static final int FEHLER_AUFTRAG_VERSANDWEG_KENNWORT_FEHLT = 1030;
	public static final int FEHLER_AUFTRAG_KUNDENKENNUNG_BESTELLUNG_FEHLT = 1031;
	public static final int FEHLER_AUFTRAG_WOOCOMMERCE_IMPORT = 1032;
	public static final int FEHLER_AUFTRAG_ZUSAMMENFASSUNG_POSITIONEN_OHNE_ZWS = 1033;

	// 2000-2499: Artikel + Stueckliste
	// ***********************************************************
	public static final int ARTIKEL_WECHSEL_LAGERBEWIRTSCHAFTET_NICHT_MOEGLICH = 2000;
	public static final int FEHLER_ARTIKEL_ZEICHEN_IN_ARTIKELNUMMER_NICHT_ERLAUBT = 2001;
	public static final int LAGER_SERIENNUMMER_SCHON_VORHANDEN = 2002;
	public static final int LAGER_KEIN_LAGER_NICHT_ANGELEGT = 2003;
	public static final int FEHLER_ARTIKEL_ARTIKELNUMMER_ZU_LANG = 2004;
	public static final int FEHLER_ARTIKEL_ARTIKELNUMMER_ZU_KURZ = 2005;
	public static final int FEHLER_SONDERZEICHEN_AN_FALSCHER_STELLE = 2006;
	public static final int FEHLER_ARTIKEL_IST_KEINEM_LAGER_ZUGEORDNET = 2007;
	public static final int FEHLER_MENGE_FUER_SERIENNUMMERNBUCHUNG_MUSS_EINS_SEIN = 2008;
	public static final int FEHLER_LAGERBEWEGUNGSURSPRUNG_NICHT_AUFFINDBAR = 2009;
	public static final int FEHLER_ARTIKEL_HAT_KEINEN_EINZELVERKAUFSPREIS_HINTERLEGT = 2010;
	public static final int FEHLER_LAGER_HAUPTLAGERDESMANDANTEN_NICHT_ANGELEGT = 2011;
	public static final int FEHLER_DARF_NICHT_AUF_KEIN_LAGER_BUCHEN = 2012;
	public static final int FEHLER_ARTIKEL_IST_NICHT_LAGERBEWIRTSCHAFTET = 2013;
	public static final int FEHLER_VKPF_MAXIMALZEHNPREISLISTEN = 2014;
	public static final int ARTIKEL_WECHSEL_SERIENNUMMERNTRAGEND_NICHT_MOEGLICH = 2015;
	public static final int ARTIKEL_WECHSEL_CHARGENNUMMERNTRAGEND_NICHT_MOEGLICH = 2016;
	public static final int ARTIKEL_SERIENNUMMER_WURDE_SCHON_VERWENDET = 2017;
	public static final int ARTIKEL_ISTSERIENNUMMERNBEHAFTET = 2018;
	public static final int ARTIKEL_ISTCHARGENNUMMERNBEHAFET = 2019;
	public static final int ARTIKEL_ANZAHLSERIENNUMMERNNICHTKORREKT = 2020;
	public static final int ARTIKEL_KORREKTURBUCHUNGCHARGENICHTMOEGLICH = 2021;
	public static final int ARTIKEL_KEINE_LAGERBUCHUNG_VORHANDEN = 2022;
	public static final int ARTIKEL_GESTEHUNGSPREISABFRAGE_NUR_FUER_WARENABGAENGE_MOEGLICH = 2023;
	public static final int FEHLER_ARTIKEL_SERIENNUMMER_VON_BIS_ZIFFERNTEIL_UNGLEICH = 2024;
	public static final int FEHLER_ARTIKEL_SERIENNUMMER_MENGE_UNGLEICH = 2025;
	public static final int FEHLER_ARTIKEL_SERIENNUMMER_VON_BIS_PREFIX_UNGLEICH = 2026;
	public static final int FEHLER_HAUPTLAGER_BEI_DIESEM_MANDANTEN_SCHON_VORHANDEN = 2027;
	public static final int LAGER_UPDATE_AUF_ARTIKEL_NICHT_ERLAUBT = 2028;
	public static final int LAGER_UPDATE_AUF_LAGER_NICHT_ERLAUBT = 2029;
	public static final int STUECKLISTE_DEADLOCK = 2030;
	public static final int ARTIKEL_DEADLOCK = 2031;
	public static final int FEHLER_INVENTUR_BEREITS_DURCHGEFUEHRT = 2032;
	public static final int FEHLER_ARTIKEL_SERIENNUMMER_MUSS_MIT_ZIFFERNTEIL_ENDEN = 2033;
	public static final int FEHLER_VKPF_MENGENSTAFFEL_EXISTIERT = 2034;
	public static final int ARTIKEL_ANZAHLCHARGENNUMMERNNICHTKORREKT = 2035;
	public static final int FEHLER_INVENTUR_ES_DARF_NUR_DAS_LAGER_DER_INVENTUR_VERWENDET_WERDEN = 2036;
	public static final int FEHLER_SERIENCHARGENNUMMER_ENTHAELT_NICHT_ERLAUBTE_ZEICHEN = 2037;
	public static final int FEHLER_ARTIKEL_ERSATZARTIKEL_DEADLOCK = 2038;
	public static final int FEHLER_HERSTELLERKUERZEL_OVERFLOW = 2039;
	public static final int FEHLER_ZUVIELE_LAGERORTE = 2040;
	public static final int FEHLER_LAGER_WERTGUTSCHRIFT_BEI_DIESEM_MANDANTEN_SCHON_VORHANDEN = 2041;
	public static final int FEHLER_LAGER_WERTGUTSCHRIFTDESMANDANTEN_NICHT_ANGELEGT = 2042;
	public static final int FEHLER_STUECKLISTENART_ARTIKELSET_IN_STUECKLISTENPOSITION_NICHT_MOEGLICH = 2043;
	public static final int FEHLER_STUECKLISTENART_ARTIKELSET_BZW_HILFSSTUECKLISTE_DARF_KEINE_STUECKLISTE_ENTHALTEN = 2044;
	public static final int FEHLER_ARTIKELSET_KANN_NICHT_VERSCHOBEN_WERDEN = 2045;
	public static final int FEHLER_ARTIKEL_KANN_NICHT_IN_ARTIKELSET_VERSCHOBEN_WERDEN = 2046;
	public static final int FEHLER_MEHRERE_LAGERPLAETZE_PRO_LAGER_NICHT_MOEGLICH = 2047;
	public static final int FEHLER_INVENTUR_MENGE_ZU_GROSS = 2048;
	public static final int FEHLER_CHARGENNUMMER_ZU_KURZ = 2049;
	public static final int FEHLER_KEINE_BERECHTIUNG_ZUM_BUCHEN_AUF_DIESEM_LAGER = 2050;
	public static final int FEHLER_DEFAULT_ARBEITSZEITARTIKEL_NICHT_DEFINIERT = 2051;
	public static final int FEHLER_SERIENNUMMER_ZU_KURZ = 2052;
	public static final int FEHLER_SERIENNUMMER_ZU_LANG = 2053;
	public static final int FEHLER_SERIENNUMMERNGENERATOR_UNGUELTIGE_ZEICHEN = 2054;
	public static final int FEHLER_SERIENNUMMER_MUSS_UEBER_ALLE_ARTIKEL_EINDEUTIG_SEIN = 2055;
	public static final int FEHLER_GERAETESNR_BEREITS_ZUGEBUCHT = 2056;
	public static final int FEHLER_POSITIONSMENGE_EINES_SNR_ARTIKELS_MUSS_1_SEIN_WENN_GERAETESNR = 2057;
	public static final int FEHLER_VERKAUFSEAN_BEREITS_VORHANDEN = 2058;
	public static final int FEHLER_VERPACKUNGSEAN_BEREITS_VORHANDEN = 2059;
	public static final int FEHLER_RUNDUNGSARTIKEL_NICHT_DEFINIERT = 2060;
	public static final int FEHLER_IMPORT_NX_STKL_LAENGE_UNTERSCHIEDLICH = 2061;
	public static final int FEHLER_IMPORT_NX_STKL_UNTERSCHIEDLICH = 2062;
	public static final int FEHLER_IMPORT_NORMTEIL_MUSS_VORHANDEN_SEIN = 2063;
	public static final int FEHLER_IMPORT_DATENTYP_SPALTE_MENGE_FALSCH = 2064;
	public static final int FEHLER_IMPORT_DATENTYP_SPALTE_GEWICHT_FALSCH = 2065;
	public static final int FEHLER_IMPORT_MATERIAL_NICHT_VORHANDEN = 2066;
	public static final int FEHLER_LAGER_WARENEINGANG_BEI_DIESEM_MANDANTEN_SCHON_VORHANDEN = 2067;
	public static final int FEHLER_ARTIKELLIEFERANT_PREIS_IST_NULL = 2068;
	public static final int FEHLER_INVENTUR_IMPORT_NUR_MIT_LAGER_MOEGLICH = 2069;
	public static final int FEHLER_UNGUELTIGE_WERTE_CSV_IMPORT = 2070;
	public static final int FEHLER_IMPORT_DIMENSIONEN_UND_STKLPOS_EINHEIT_FALSCH = 2071;
	public static final int FEHLER_IMPORT_STUECKLISTENIMPORT_ALLGEMEIN = 2072;
	public static final int FEHLER_EINZELPREIS_NUR_LOESCHBAR_WENN_KEINE_STAFFELN = 2073;
	public static final int FEHLER_EINHEIT_C_NR_VPE_IN_ARTIKELLIEFERANT_VORHANDEN = 2074;
	public static final int FEHLER_SERIENNUMMER_ENTHAELT_NICHT_NUMERISCHE_ZEICHEN = 2075;
	public static final int FEHLER_CHARGENNUMMERNGENERATOR_UNGUELTIGE_ZEICHEN = 2076;
	public static final int FEHLER_CHARGENNUMMER_NICHT_NUMERISCH = 2077;

	public static final int FEHLER_KEINE_STUECKLISTEN_FUER_PARTNERID = 2078;
	public static final int FEHLER_KUNDENSTUECKLISTEMANDANT_NICHT_EINDEUTIG = 2079;
	public static final int FEHLER_GEBINDEMENGE_ERFORDERLICH = 2080;
	public static final int FEHLER_SNR_ALS_GERAETESERIENNUMMER_VERWENDET = 2081;

	public static final int FEHLER_EXPORT_4VENDING_KEINE_AKTIVE_PREISLISTE_VORHANDEN = 2082;
	public static final int FEHLER_EXPORT_4VENDING_KEIN_AKTUELL_GUELTIGER_VKPREIS_VORHANDEN = 2083;
	public static final int FEHLER_EXPORT_4VENDING_KEIN_ARTIKEL_MWST_SATZ_DEFINIERT = 2084;
	public static final int FEHLER_EXPORT_4VENDING_KEIN_LIEF1PREIS_VORHANDEN = 2085;
	public static final int FEHLER_EXPORT_4VENDING_ARTIKELGRUPPE_NICHT_GEFUNDEN = 2086;
	public static final int FEHLER_PRUEFKOMBINATION_IST_GESPERRT = 2087;
	public static final int FEHLER_STKL_MANDANTENWECHSEL_KEINE_BERECHTIGUNG = 2088;
	public static final int FEHLER_KEINE_ENTSPRECHUNG_IN_PRUEFKOMBINATION = 2089;

	public static final int FEHLER_PRO_FIRST_IMPORT_TAETIGKEIT_NICHT_VORHANDEN = 2090;
	public static final int FEHLER_PRO_FIRST_IMPORT_MASCHINE_NICHT_VORHANDEN = 2091;
	public static final int FEHLER_PRO_FIRST_IMPORT_KUNDE_NICHT_VORHANDEN = 2092;
	public static final int FEHLER_PRO_FIRST_IMPORT_DATENBANKVERBIDNUNG = 2093;
	public static final int FEHLER_PRO_FIRST_PARAMETER_PRO_FIRST_FREMDFERTIGUNGSARTIKEL_NICHT_DEFINIERT = 2094;
	public static final int FEHLER_PRO_FIRST_PARAMETER_PRO_FIRST_BILD_PFAD_NICHT_DEFINIERT = 2095;
	public static final int FEHLER_CHARGENEIGENSCHAFTEN_KOPIEREN_ARTIKELGRUPPEN_UNGLEICH = 2096;
	public static final int FEHLER_CHARGENEIGENSCHAFTEN_KOPIEREN_ARTIKEL_NICHT_SNR_CHNR_BEHAFTET = 2097;
	public static final int FEHLER_DEFAULT_PRO_FIRST_ARTIKELGRUPPE_NICHT_DEFINIERT = 2098;
	public static final int FEHLER_PRO_FIRST_SCHACHTELPANIMPORT_STKL_NICHT_GEFUNDEN = 2099;
	public static final int FEHLER_PRO_FIRST_SCHACHTELPANIMPORT_KEIN_LOS_GEFUNDEN = 2100;
	public static final int FEHLER_PRO_FIRST_SCHACHTELPANIMPORT_KEIN_LOS_MIT_AUSREICHENDER_MENGE_GEFUNDEN = 2101;
	public static final int FEHLER_PRO_FIRST_SCHACHTELPANIMPORT_RESTMENGE_GROESSER_LAGERMENGE = 2102;
	public static final int FEHLER_PRO_FIRST_SCHACHTELPANIMPORT_KEIN_MATERIAL_DEFINIERT = 2103;
	public static final int FEHLER_PRO_FIRST_SCHACHTELPANIMPORT_STKL_IN_PRO_FIRST_NICHT_VORHANDEN = 2104;

	public static final int FEHLER_MINDESTBESTELLWERT_ARTIKEL_NICHT_DEFINIERT = 2105;
	public static final int FEHLER_KUNDEMATERIAL_NICHT_DEFINIERT = 2106;
	public static final int FEHLER_POSITIONSMENGE_EINES_SNR_ARTIKELS_MUSS_1_SEIN_WENN_GERAETESNR_ERSATZTYPEN_AUSLASSEN = 2107;
	public static final int FEHLER_ARTIKEL_DARF_NICHT_ZUGEBUCHT_WERDEN = 2108;

	public static final int FEHLER_GTIN13_GENERIERUNG_LAENGE_BASISNUMMER_UNGUELTIG = 2109;
	public static final int FEHLER_GTIN13_GENERIERUNG_LAUFENDE_NUMMER_ZU_GROSS = 2110;
	public static final int FEHLER_GTIN13_GENERIERUNG_PARAMETER_NICHT_DEFINIERT = 2111;
	public static final int FEHLER_GTIN13_GENERIERUNG_ALLE_LAUEFENDEN_ARTIKELNUMMERN_VERGEBEN = 2111;
	public static final int FEHLER_IMPORT_TAETIGKEIT_NICHT_VORHANDEN = 2112;
	public static final int FEHLER_RUNDUNGSARTIKEL_KEIN_MWSTSATZ_DEFINIERT = 2113;
	public static final int FEHLER_SET_ARTIKEL_KOPF_DARF_NICHT_ZUGEBUCHT_WERDEN = 2114;
	public static final int FEHLER_REELID_NICH_MOEGLICH = 2115;
	public static final int FEHLER_CHARGENNUMMER_ZU_LANG = 2116;
	public static final int FEHLER_UNBEKANNTE_SERIENCHARGENNUMMER = 2117;
	public static final int ARTIKEL_LAGERBEWIRTSCHAFTET_KANN_NUR_ABGESCHALTET_WERDEN_WENN_NICHT_CHNR_SNR_BEHAFTET = 2118;
	public static final int FEHLER_DIMENSIONEN_BESTELLEN_ARTIKELNUMMERBEREITSVORHANDEN = 2119;
	public static final int FEHLER_STUECKLISTE_UNGUELTIGE_REIHENFOLGE = 2120;
	public static final int FEHLER_STUECKLISTE_EIGENGEFERTIGTE_UNTERSTUECKLISTE_MIT_ZIELMENGE_GLEICH_NULL = 2121;

	public static final int FEHLER_WEBABFRAGE_ARTIKELLIEFERANT_KEIN_ERGEBNIS = 2122;
	public static final int FEHLER_WEBABFRAGE_ARTIKELLIEFERANT_MEHRFACHE_ERGEBNISSE = 2123;
	public static final int FEHLER_WEBABFRAGE_ARTIKELLIEFERANT_UNERWARTETE_RESPONSE = 2124;
	public static final int FEHLER_WEBABFRAGE_ARTIKELLIEFERANT_RESPONSE_FORBIDDEN = 2125;
	public static final int FEHLER_WEBABFRAGE_ARTIKELLIEFERANT_UNGUELTIGER_PARAMETER = 2126;
	public static final int FEHLER_WEBABFRAGE_ARTIKELLIEFERANT_UNBEKANNTER_WEBLIEFERANT = 2127;
	public static final int FEHLER_WEBABFRAGE_ARTIKELLIEFERANT_TYP_NICHT_IMPLEMENTIERT = 2128;
	public static final int FEHLER_WEBABFRAGE_ARTIKELLIEFERANT_EXC_WAEHREND_HTTP_REQUEST = 2129;
	public static final int FEHLER_FORMELSTUECKLISTE_ABBRUCH = 2130;
	public static final int FEHLER_ERFASSUNGSFAKTOR_MUSS_GROESSER_0_SEIN = 2131;

	public static final int FEHLER_ARTIKEL_URSPRUNGSLAND_FEHLT = 2132;
	public static final int FEHLER_ARTIKEL_FREIGABE_NUR_WENN_STUECKLISTE_FREIGEGEBEN = 2133;
	public static final int FEHLER_STK_FREIGABE_RUECKGAENGIG_NUR_WENN_ARTIKEL_NICHT_FREIGEGEBEN = 2134;
	public static final int FEHLER_BEVORZUGTER_ARTIKEL_NICHT_MOEGLICH_MEHRERE_SOKOS = 2135;
	public static final int FEHLER_GENERIERE_HIERARCHISCHE_CHARGENNUMMERN = 2136;
	public static final int FEHLER_KALKULATORISCHER_ARTIKEL_KONNTE_NICHT_VERKETTET_WERDEN = 2137;
	public static final int FEHLER_ARTIKELETIKETT_ANZAHL_SNR_UNGLEICH_LFDNR = 2138;
	public static final int FEHLER_ARTIKELNUMMER_INCL_HERSTELLERNUMMER_ZU_LANG = 2139;
	public static final int FEHLER_ARTIKELNUMMER_ZU_LANG = 2140;
	
	// 2500-2999: Personal+Zeiterfassung+Zutritt
	// ********************************************
	public static final int FEHLER_PERSONAL_FEHLER_BEI_EINTRITTSDATUM = 2500;
	public static final int FEHLER_PERSONAL_URLAUBSBERECHNUNG_ZU_DATUM_KEINE_SOLLZEIT_DEFINIERT = 2501;
	public static final int FEHLER_ZEITERFASSUNG_MEHRFACHES_KOMMT = 2502;
	public static final int FEHLER_ZEITERFASSUNG_MEHRFACHES_GEHT = 2503;
	public static final int FEHLER_ZEITERFASSUNG_GEHT_OHNE_KOMMT = 2504;
	public static final int FEHLER_ZEITERFASSUNG_GEHT_FEHLT = 2505;
	public static final int FEHLER_ZEITERFASSUNG_SONDERTAETIGKEIT_MUSS_BEENDET_WERDEN = 2506;
	public static final int FEHLER_ZEITERFASSUNG_TAETIGKEIT_VOR_KOMMT = 2507;
	public static final int FEHLER_GEHT_VOR_ENDE = 2508;
	public static final int FEHLER_RELATIVE_BUCHUNG_OHNE_KOMMT = 2509;
	public static final int FEHLER_KEIN_STOP_OHNE_MASCHINE = 2510;
	public static final int FEHLER_AUSWEISNUMMER_ZESTIFT = 2511;
	public static final int FEHLER_ZUTRITTSOBJEKT_VERWENDUNGSUEBERSCHREITUNG = 2512;
	public static final int FEHLER_PERSONAL_DUPLICATE_AUSWEIS = 2513;
	public static final int FEHLER_IN_ZEITDATEN = 2514;
	public static final int FEHLER_RELATIVES_AENDERN_MIT_SONDERTAETIGKEITEN_NICHT_MOEGLICH = 2515;
	public static final int FEHLER_RELATIVES_AENDERN_ZUWENIG_ZEIT = 2516;
	public static final int FEHLER_CUD_TAETIGKEIT_TELEFON_NICHT_ERLAUBT = 2517;
	public static final int FEHLER_ZEITERFASSUNG_FEHLER_ZEITDATEN = 2518;
	public static final int FEHLER_ZEITERFASSUNG_RELATIVE_NICHT_MOEGLICH = 2519;
	public static final int FEHLER_RELATIVE_BUCHUNG_ENDE_FEHLT = 2520;
	public static final int FEHLER_RELATIVE_BUCHUNG_GESAMTE_ZEIT_VERBUCHT = 2521;
	public static final int FEHLER_LOSGUTSCHLECHT_VORHANDEN = 2522;
	public static final int FEHLER_IN_REISEZEITEN = 2523;
	public static final int FEHLER_ZEITBUCHUNGEN_VORHANDEN = 2524;
	public static final int FEHLER_PERSONAL_ZEICHEN_IN_PERSONALNUMMER_NICHT_ERLAUBT = 2525;
	public static final int FEHLER_BUCHUNG_ZWISCHEN_VON_BIS = 2526;
	public static final int FEHLER_BUCHUNG_EINFUEGEN_ZWISCHEN_VON_BIS_NICHT_ERLAUBT = 2527;
	public static final int FEHLER_SONDERZEITENIMPORT_DOPPELTER_EINTRAG = 2528;
	public static final int FEHLER_ZEITEN_BEREITS_ABGESCHLOSSEN = 2529;
	public static final int FEHLER_BUCHUNG_BEREITS_VORHANDEN = 2530;
	public static final int FEHLER_FIRMENZEITMODELL_BEREITS_VORHANDEN = 2531;
	public static final int FEHLER_FIRMENZEITMODELL_NICHT_VORHANDEN = 2532;
	public static final int FEHLER_FIRMENZEITMODELL_NICHT_AUSREICHEND_SOLLZEIT_DEFINIERT = 2533;
	public static final int FEHLER_BELEGZEITEN_IN_UNTERBRECHUNG = 2534;
	public static final int FEHLER_ZEITMODELLTAGPAUSE_UEBERSCHNEIDEN_SICH = 2535;
	public static final int FEHLER_HVMABENUTZER_ANZAHL_UEBERSCHRITTEN = 2536;
	public static final int FEHLER_EMAIL_SONDERZEITENANTRAG_NICHT_ERMITTELBAR = 2537;
	public static final int FEHLER_EMAIL_ZEITBESTAETIGUNG_NICHT_ERMITTELBAR = 2538;
	public static final int FEHLER_VERRECHNUNGSMODELLTAG_UEBERSCHNEIDEN_SICH = 2539;
	public static final int FEHLER_MAXIMALE_PERSONENANZAHL_OHNE_PERSONALMODUL_ERREICHT = 2540;
	public static final int FEHLER_VERSAND_MONATSABRECHNUNG_ANHAENGE_PFAD = 2541;
	public static final int FEHLER_ZUSAETZLICHE_SPESEN_AUF_ENDE_NICHT_MOEGLICH = 2542;
	public static final int FEHLER_MASCHINE_DUPLICATE_IDENTIFIKATIONSNR = 2543;
	public static final int FEHLER_MASCHINE_DUPLICATE_IDENTIFIKATIONSNR_ANDERER_MANDANT = 2544;
	public static final int FEHLER_EMAIL_LIEFERSCHEINBESTAETIGUNG_NICHT_ERMITTELBAR = 2545;
	public static final int FEHLER_NEGATIVE_SOLLSTUNDEN_UMWANDELN_URLAUB_BEREITS_VORHANDEN = 2546;

	// 3000-3999: Rechnung:
	// *******************************************************
	public static final int FEHLER_RECHNUNG_DARF_LAGER_NICHT_AENDERN = 3000;
	public static final int FEHLER_RECHNUNG_BEIM_AKTIVIEREN = 3001;
	public static final int FEHLER_RECHNUNG_HAT_LIEFERSCHEINE_EINES_ANDEREN_KUNDEN = 3002;
	public static final int FEHLER_RECHNUNG_KEINE_RECHNUNGSTEXTE_EINGETRAGEN = 3003;
	public static final int FEHLER_RECHNUNG_MAHNSPERRE = 3004;
	public static final int FEHLER_RECHNUNG_NEUE_MAHNSTUFE_MUSS_GROESSER_SEIN_ALS_DIE_ALTE = 3005;
	public static final int FEHLER_RECHNUNG_WERT_DARF_NICHT_NEGATIV_SEIN = 3006;
	public static final int FEHLER_RECHNUNG_LIEFERSCHEIN_MUSS_IM_SELBEN_GESCHAEFTSJAHR_LIEGEN = 3007;
	public static final int FEHLER_RECHNUNG_HAT_KEINE_POSITIONEN = 3008;
	public static final int FEHLER_RECHNUNG_VERRECHNUNGSBEGINN_NICHT_DEFINIERT = 3009;
	public static final int FEHLER_RECHNUNG_WIEDERHOLUNGSINTERVALL_NICHT_DEFINIERT = 3010;
	public static final int FEHLER_RECHNUNG_WH_AUFTRAG_ENTHAELT_SNR_BEHAFTETE_ARTIKEL = 3011;
	public static final int FEHLER_RECHNUNG_HAT_KEINEN_WERT = 3012;
	public static final int FEHLER_RECHNUNG_ZAHLUNGSZIEL_KEINE_TAGE = 3013;
	public static final int FEHLER_RECHNUNG_HVWERT_UNGLEICH_REPORTWERT = 3014;
	public static final int FEHLER_RECHNUNG_POSITIONLS_EXISTIERT = 3015;
	public static final int FEHLER_RECHNUNG_FAELLIGKEIT_NICHT_BERECHENBAR = 3016;
	public static final int FEHLER_RECHNUNG_NOCH_NICHT_AKTIVIERT = 3017;
	public static final int FEHLER_GUTSCHRIFT_WECHSEL_WERTGUTSCHRIFT_FEHLER = 3018;
	public static final int FEHLER_LIEFERSCHEIN_IN_PROFORMARECHNUNG_DOPPELT_VERRECHNET = 3019;
	public static final int FEHLER_LIEFERSCHEINE_IN_VERSCHIEDENE_LAENDER = 3020;
	public static final int FEHLER_UNTERSCHIEDLICHE_MWST_SAETZE = 3021;
	public static final int FEHLER_RECHNUNG_GS_STATUSAENDERUNG_AUF_BEZAHLT_NICHT_ERLAUBT = 3022;
	public static final int FEHLER_RECHNUNG_BEREITS_BEZAHLT = 3023;

	public static final int FEHLER_INT_ZWISCHENSUMME_BIS_KLEINER_VON = 3024;
	public static final int FEHLER_INT_ZWISCHENSUMME_VON_KLEINER_EINS = 3025;
	public static final int FEHLER_INT_ZWISCHENSUMME_POSITIONSNUMMER = 3026;
	public static final int FEHLER_INT_ZWISCHENSUMME_VON_KLEINER_BIS = 3027;
	public static final int FEHLER_INT_ZWISCHENSUMME_MWSTSATZ_UNTERSCHIEDLICH = 3028;

	public static final int FEHLER_SCHLUSSRECHNUNG_BEREITS_VORHANDEN = 3029;
	public static final int FEHLER_KEINE_ANZAHLUNGEN_VORHANDEN = 3030;
	public static final int FEHLER_ANZAHLUNGEN_NICHT_BEZAHLT = 3031;
	public static final int FEHLER_WERBEABGABEARTIKEL_NICHT_VORHANDEN = 3032;
	public static final int FEHLER_WERBEABGABEARTIKEL_DARF_NICHT_LAGERBEWIRTSCHAFTET_SEIN = 3033;
	public static final int FEHLER_AR_ANZAHLUNGEN_REVERSE_CHARGE_ABWEICHEND = 3034;
	public static final int FEHLER_ANZAHLUNG_SCHLUSSRECHNUNG_BEREITS_VORHANDEN = 3035;
	public static final int FEHLER_STORNIEREN_ZAHLUNGEN_VORHANDEN = 3036;
	public static final int FEHLER_LIEFERSCHEINE_MIT_VERSCHIEDENEN_PROJEKTEN = 3037;
	public static final int FEHLER_LIEFERSCHEIN_MUSS_GELIEFERT_SEIN = 3038;
	public static final int FEHLER_LIEFERSCHEIN_BEREITS_IN_PROFORMARECHNUNG = 3039;
	public static final int FEHLER_RECHNUNG_GS_AUF_ANZAHLUNG_NICHT_MOEGLICH = 3040;
	public static final int FEHLER_STORNIEREN_ANZAHLUNG_SCHLUSSRECHNUNG_VORHANDEN = 3041;
	public static final int FEHLER_READRESSE_PROJEKT_BESTELLNUMMER_AENDERN_RECHNUNG_NICHT_IM_STATUS_ANGELEGT = 3042;
	public static final int FEHLER_MMZ_ARTIKEL_DARF_NICHT_LAGERBEWIRTSCHAFTET_SEIN = 3043;

	// Definition: Bei ReverseCharge muessen alle Positionen den gleichen
	// Mwstsatz haben
	public static final int FEHLER_RECHNUNG_UNTERSCHIEDLICHE_MWSTSAETZE_BEI_RC = 3044;

	public static final int FEHLER_RECHNUNG_VERSANDWEG_NICHT_UNTERSTUETZT = 3045;
	public static final int FEHLER_RECHNUNG_ELEKTRONISCH_BEREITS_DURCHGEFUEHRT = 3046;
	public static final int FEHLER_RECHNUNG_VERSANDWEG_IM_PARTNER_NICHT_DEFINIERT = 3047;
	public static final int FEHLER_MEHRERE_AUFTRAEGE_OHNE_SAMMELLIEFERSCHEIN = 3048;
	public static final int FEHLER_LIEFERSCHEIN_IN_RECHNUNG_WAEHRUNGEN_UNTERSCHIEDLICH = 3049;

	public static final int FEHLER_RECHNUNG_UNTERSCHIEDLICHE_MWSTSAETZE_BEI_ANZAHLUNG = 3050;
	public static final int FEHLER_SCHLUSSRECHNUNG_ABWEICHENDER_AUFTRAG_IN_RECHUNNGSPOSITION = 3051;
	public static final int FEHLER_SCHLUSSRECHNUNG_ABWEICHENDER_AUFTRAG_IN_LIEFERSCHEIN = 3052;
	public static final int FEHLER_SCHLUSSRECHNUNG_ABWEICHENDER_AUFTRAG_IN_LIEFERSCHEINPOSITION = 3053;

	public static final int FEHLER_LIEFERSCHEINE_IN_VERSCHIEDENE_LAENDERARTEN = 3054;
	public static final int FEHLER_ZUGFERD_LIEFERANTENNUMMER_FEHLT = 3055;
	public static final int FEHLER_ZUGFERD_ADRESSINFO_FEHLT = 3056;
	public static final int FEHLER_ZUGFERD_EINHEIT_MAPPING_NICHT_GEFUNDEN = 3057;

	public static final int FEHLER_RECHNUNG_LIEFERSCHEINE_LIEFERART_UNTERSCHIEDLICH = 3058;

	public static final int FEHLER_ABRECHUNGSVORSCHLAG_UEBERLEITEN_KEIN_KUNDE = 3059;
	public static final int FEHLER_ABRECHUNGSVORSCHLAG_UEBERLEITEN_MEHRERE_KUNDEN = 3060;
	public static final int FEHLER_ABRECHUNGSVORSCHLAG_MANUELL_ERLEDIGEN = 3061;
	public static final int FEHLER_DURCH_ABRECHNUNGSVORSCHLAG_BEREITS_VERRECHNET = 3062;
	public static final int FEHLER_EINTRAG_IN_ABRECHNUNGSVORSCHLAG_UND_KANN_NICHT_GEAENDERT_WERDEN = 3063;
	public static final int FEHLER_ABRECHNUNGSVORSCHLAG_IST_GESPERRT = 3064;
	public static final int FEHLER_ABRECHUNGSVORSCHLAG_ZU_VERRECHNENDER_ARTIKEL_KALKULATORISCH = 3065;
	public static final int FEHLER_ABRECHUNGSVORSCHLAG_ARTIKEL_IN_VERRECHNUNGSMODELL_KALKULATORISCH = 3066;
	public static final int FEHLER_SCHLUSSRECHNUNG_BEREITS_VORHANDEN_ANZAHLUNG_DARF_NICHT_MEHR_GEAENDERT_WERDEN = 3067;
	public static final int FEHLER_VERPACKUNGSKOSTENARTIKEL_NICHT_VORHANDEN = 3068;
	
	public static final int FEHLER_ANZAHLUNG_MEHRERE_AUFTRAEGE_IN_LIEFERSCHEIN = 3069;
	// Zahlungsart <> Gutschrift, aber Gutschrift-Daten vorhanden
	public static final int FEHLER_GUTSCHRIFTZAHLUNG_OHNE_GUTSCHRIFTART = 3070;
	public static final int FEHLER_VERSANDKOSTENARTIKEL_NICHT_VORHANDEN = 3071;
	
	public static final int FEHLER_ANZAHLUNGSRECHNUNG_BEREITS_VORHANDEN = 3072;
	public static final int FEHLER_INT_ZWISCHENSUMME_MWSTSATZ_UNTERSCHIEDLICH_ZWSPOS = 3073;

	// 4000-4999: Finanz:
	// *********************************************************
	public static final int FEHLER_FINANZ_BUCHUNG_NICHT_ERLAUBT_BUCHUNGSREGEL = 4000;
	public static final int FEHLER_FINANZ_BANKVERBINDUNG_KONTO_SCHON_ZUGEWIESEN = 4005;
	public static final int FEHLER_FINANZ_BUCHUNG_AUF_KONTEN_VERSCHIEDENER_MANDANTEN = 4006;
	public static final int FEHLER_FINANZ_FINANZAMT_BEREITS_ANGELEGT = 4007;
	public static final int FEHLER_FINANZ_STORNIEREN_NICHT_MOEGLICH = 4008;
	public static final int FEHLER_FINANZ_UNGUELTIGER_KONTOTYP = 4009;
	public static final int FEHLER_FINANZ_UVA_AUF_GANZES_JAHR_NICHT_ERLAUBT = 4010;
	public static final int FEHLER_FINANZ_BUCHUNGAKTIVIEREN_NICHT_MOEGLICH = 4011;
	public static final int FEHLER_FINANZ_BILANZGRUPPENDEFINITON_POS_NEG = 4012;
	public static final int FEHLER_FINANZ_BUCHUNG_AUF_MITLAUFENDES_KONTO_NICHT_ERLAUBT = 4013;
	public static final int FEHLER_FINANZ_KONTOLAENDERART_ZEIGT_AUF_SICH_SELBST = 4014;
	public static final int FEHLER_FINANZ_KEINE_KONTONUMMER_FUER_BEREICH_VERFUEGBAR = 4015;
	public static final int FEHLER_FINANZ_KONTONUMMER_AUSSERHALB_DEFINITION = 4016;
	public static final int FEHLER_FINANZ_ER_MIT_POSITIONEN_NOCH_NICHT_GEDRUCKT = 4017;
	public static final int FEHLER_FINANZ_GEGENKONTO_NICHT_DEFINIERT = 4018;
	public static final int FEHLER_FINANZ_FINANZAMT_IM_MANDANT_NITCH_DEFINIERT = 4019;
	public static final int FEHLER_FINANZ_SALDOINFO_SOLL_UNGLEICH = 4020;
	public static final int FEHLER_FINANZ_SALDOINFO_HABEN_UNGLEICH = 4021;
	public static final int FEHLER_FINANZ_SALDOINFO_MWSTSATZ_UNGLEICH = 4022;
	public static final int FEHLER_FINANZ_BILANZGRUPPENDEFINITON_NEGATIV = 4023;
	public static final int FEHLER_FINANZ_KONTOLAND_ZEIGT_AUF_SICH_SELBST = 4024;
	public static final int FEHLER_FINANZ_UNGUELTIGE_FORMULARNUMMER = 4025;

	// Fehler, die das Verbuchen von Belegen betreffen
	public static final int FEHLER_FINANZ_BELEG_BEREITS_VERBUCHT = 4100;
	public static final int FEHLER_FINANZ_KEIN_DEBITORENKONTO_DEFINIERT = 4101;
	public static final int FEHLER_FINANZ_KEIN_ERLOESKONTO_DEFINIERT = 4102;
	public static final int FEHLER_FINANZ_ZYKEL_UST = 4103;
	public static final int FEHLER_FINANZ_ZYKEL_BILANZ = 4104;
	public static final int FEHLER_FINANZ_ZYKEL_SKONTO = 4105;
	public static final int FEHLER_FINANZ_KEIN_KREDITORENKONTO_DEFINIERT = 4106;
	public static final int FEHLER_FINANZ_KEIN_WARENKONTO_DEFINIERT = 4107;
	public static final int FEHLER_FINANZ_KEINE_STEUERKATEGORIE_DEFINIERT = 4108;
	public static final int FEHLER_FINANZ_KEIN_ABSTIMMKONTO_DEFINIERT = 4109;
	public static final int FEHLER_FINANZ_KEIN_KURSVERLUSTKONTO = 4110;
	public static final int FEHLER_FINANZ_KEIN_KURSGEWINNKONTO = 4111;
	public static final int FEHLER_FINANZ_KEIN_STEUERKONTO = 4112;
	public static final int FEHLER_FINANZ_KEIN_SKONTOKONTO = 4113;
	public static final int FEHLER_FINANZ_GESCHAEFTSJAHR_GESPERRT = 4114;
	public static final int FEHLER_FINANZ_KEIN_EINFUHRUMSATZSTEUERKONTO_DEFINIERT = 4115;
	public static final int FEHLER_FINANZ_BUCHUNG_NICHT_ERLAUBT_UVAVERPROBUNG = 4116;
	public static final int FEHLER_FINANZ_KEINE_KOSTENSTELLE_DEFINIERT = 4117;
	public static final int FEHLER_FINANZ_VORHERIGES_GESCHAEFTSJAHR_NICHT_GESPERRT = 4118;
	public static final int FEHLER_FINANZ_KEIN_EROEFFNUNGSKONTO_DEFINIERT = 4119;
	public static final int FEHLER_FINANZ_UNGUELTIGES_GESCHAEFTSJAHR = 4120;
	public static final int FEHLER_FINANZ_BUCHUNGDETAIL_VERSCHIEDENE_AZK_VORHANDEN = 4121;
	public static final int FEHLER_FINANZ_KEINE_STEUERKATEGORIE_REVERSE_DEFINIERT = 4122;
	public static final int FEHLER_FINANZ_UNGUELTIGER_BETRAG_ZAHLUNG_VORAUSZAHLUNG = 4123;
	public static final int FEHLER_FINANZ_KONTO_IN_ANDERER_MWST_VERWENDET = 4124;
	public static final int FEHLER_FINANZ_UNGUELTIGE_STEUERBUCHUNG = 4125;
	public static final int FEHLER_FINANZ_KEIN_LAND_IM_KUNDEN = 4126;
	public static final int FEHLER_FINANZ_GESCHAEFTSJAHR_EXISTIERT_NICHT = 4127;
	public static final int FEHLER_FINANZ_DATUM_NICHT_LETZTER_TAG_DES_MONATS = 4128;
	public static final int FEHLER_FINANZ_ABGABEN_UND_IST_VERSTEUERER_NICHT_UNTERSTUETZT = 4129;
	public static final int FEHLER_FINANZ_STEUERKONTEN_MEHRFACH_VERWENDET = 4130;
	public static final int FEHLER_FINANZ_UNGUELTIGER_RECHNUNGTYP = 4131;
	public static final int FEHLER_FINANZ_KEINE_STEUERKATEGORIE_DEFINITION_VORHANDEN = 4132;
	public static final int FEHLER_FINANZ_KEIN_ABSTIMMKONTO_FORDERUNGEN_DEFINIERT = 4133;
	public static final int FEHLER_FINANZ_KEIN_ABSTIMMKONTO_VERBINDLICHKEITEN_DEFINIERT = 4134;
	public static final int FEHLER_FINANZ_KEINE_STEUERKATEGORIEKONTO_DEFINITION_VORHANDEN = 4135;
	public static final int FEHLER_FINANZ_KEIN_ERLOESKONTO_EK_DEFINIERT = 4136;
	public static final int FEHLER_FINANZ_STEUERSAETZE_IN_KONTEN_UNTERSCHIEDLICH = 4137;
	public static final int FEHLER_FINANZ_STEUERKONTO_MEHRFACH_IN_MWSTSATZ = 4138;
	public static final int FEHLER_FINANZ_STEUERKONTEN_MEHRFACH_VERWENDET2 = 4139;
	
	public static final int FEHLER_FINANZ_KEINE_INTRASTATREGION_DEFINIERT = 4140;
	
	// import ab 4600
	public static final int FEHLER_FINANZ_IMPORT_ANZAHL_FELDER = 4600;
	public static final int FEHLER_FINANZ_IMPORT_FELD_LEER = 4601;
	public static final int FEHLER_FINANZ_IMPORT_UNGUELTIGES_DATUMSFORMAT = 4602;
	public static final int FEHLER_FINANZ_IMPORT_KONTONUMMER_BEREITS_VORHANDEN = 4603;
	public static final int FEHLER_FINANZ_IMPORT_KONTONUMMER_REFERENZ = 4604;
	public static final int FEHLER_FINANZ_IMPORT_FINANZAMT_NICHT_VORHANDEN = 4605;
	public static final int FEHLER_FINANZ_IMPORT_UVAART_NICHT_VORHANDEN = 4606;
	public static final int FEHLER_FINANZ_IMPORT_KOSTENSTELLE_NICHT_VORHANDEN = 4607;
	public static final int FEHLER_FINANZ_IMPORT_ZYKLISCHER_VERWEIS = 4608;
	public static final int FEHLER_FINANZ_IMPORT_UNGUELTIGES_BOOLEANFORMAT = 4609;
	public static final int FEHLER_FINANZ_IMPORT_KONTOART_NICHT_VORHANDEN = 4610;
	public static final int FEHLER_FINANZ_IMPORT_STEUERART_UNBEKANNT = 4611;
	public static final int FEHLER_FINANZ_IMPORT_UNCATCHED = 4699;

	// export ab 4800
	public static final int FEHLER_FINANZ_EXPORT_DEBITORENKONTO_NICHT_DEFINIERT = 4800;
	public static final int FEHLER_FINANZ_EXPORT_KREDITORENKONTO_NICHT_DEFINIERT = 4801;
	public static final int FEHLER_FINANZ_EXPORT_UST_KONTO_NICHT_DEFINIERT = 4802;
	public static final int FEHLER_FINANZ_EXPORT_BELEG_IST_NOCH_NICHT_AKTIVIERT = 4803;
	public static final int FEHLER_FINANZ_EXPORT_EINGANGSRECHNUNG_IST_GELOCKT = 4804;
	public static final int FEHLER_FINANZ_EXPORT_RECHNUNG_IST_GELOCKT = 4805;
	public static final int FEHLER_FINANZ_EXPORT_GUTSCHRIFT_IST_GELOCKT = 4806;
	public static final int FEHLER_FINANZ_EXPORT_KOSTENSTELLE_HAT_KEIN_SACHKONTO = 4807;
	public static final int FEHLER_FINANZ_EXPORT_EINGANGSRECHNUNG_NICHT_VOLLSTAENDIG_KONTIERT = 4808;
	public static final int FEHLER_FINANZ_EXPORT_ES_DARF_NUR_DER_LETZTE_GELOESCHT_WERDEN = 4809;
	public static final int FEHLER_FINANZ_EXPORT_KONTOLAENDERART_NICHT_DEFINIERT = 4810;
	public static final int FEHLER_FINANZ_EXPORT_LAENDERART_NICHT_FESTSTELLBAR_FUER_PARTNER = 4811;
	public static final int FEHLER_FINANZ_EXPORT_AUSGANGSRECHNUNG_NICHT_VOLLSTAENDIG_KONTIERT = 4812;
	public static final int FEHLER_FINANZ_EXPORT_LIEFERANTENRECHNUNGSNUMMER_FEHLT = 4813;
	public static final int FEHLER_FINANZ_EXPORT_STICHTAG_NICHT_DEFINIERT = 4814;
	public static final int FEHLER_FINANZ_EXPORT_PERSONENKONTO_KANN_NICHT_AUTOMATISCH_ERSTELLT_WERDEN = 4815;
	public static final int FEHLER_FINANZ_EXPORT_BELEG_LIEGT_AUSSERHALB_GUELIGEM_EXPORTZEITRAUM = 4816;
	public static final int FEHLER_FINANZ_EXPORT_RECHNUNG_NICHT_VOLLSTAENDIG_KONTIERT = 4817;
	public static final int FEHLER_FINANZ_EXPORT_FINANZAMT_NICHT_VOLLSTAENDIG_DEFINIERT = 4818;
	public static final int FEHLER_FINANZ_EXPORT_KONTO_HAT_KEIN_FINANZAMT = 4819;
	public static final int FEHLER_FINANZ_EXPORT_MEHRERE_FINANZAEMTER = 4820;
	public static final int FEHLER_FINANZ_EXPORT_SALDO_UNGLEICH_NULL = 4821;
	public static final int FEHLER_FINANZ_EXPORT_PARTNER_UST_LKZ_UNGLEICH_FINANZAMT_LKZ = 4822;
	public static final int FEHLER_FINANZ_EXPORT_ARTIKEL_KEINE_ARTIKELGRUPPE = 4823;
	public static final int FEHLER_FINANZ_EXPORT_WAEHRUNG_NICHT_GEFUNDEN = 4824;
	public static final int FEHLER_FINANZ_EXPORT_KORREKTURBUCHUNG_ZUHOCH = 4825;
	public static final int FEHLER_FINANZ_EXPORT_KEINKURS_ZUDATUM = 4826;
	public static final int FEHLER_FINANZ_EXPORT_KEIN_MWSTCODE = 4827;
	public static final int FEHLER_FINANZ_EXPORT_KEIN_KONTO_FUER_ARTIKELGRUPPE = 4828;
	public static final int FEHLER_FINANZ_EXPORT_KEIN_UST_KONTO_DEFINIERT = 4829;
	public static final int FEHLER_FINANZ_EXPORT_KEIN_KONTO_IGERWERB = 4830;
	public static final int FEHLER_FINANZ_EXPORT_KEIN_KONTO_REVERSECHARGE = 4831;
	public static final int FEHLER_FINANZ_EXPORT_ANZAHLUNGSRECHNUNG_LIEFERSCHEIN_NICHT_ERLAUBT = 4832;
	public static final int FEHLER_FINANZ_EXPORT_ANZAHLUNG_KONTO_NICHT_DEFINIERT = 4833;
	public static final int FEHLER_FINANZ_EXPORT_FINANZAEMTER_UNTERSCHIEDLICH = 4834;
	public static final int FEHLER_FINANZ_EXPORT_NETTODIFFERENZ_ZUHOCH = 4835;
	public static final int FEHLER_FINANZ_EXPORT_ARTIKELGRUPPEN_DEFAULT_KONTO_AR_FEHLT = 4836;
	public static final int FEHLER_FINANZ_EXPORT_DEFAULT_RABATT_KONTO_FEHLT = 4837;
	public static final int FEHLER_FINANZ_EXPORT_UNGUELTIGE_BUCHUNG_AUF_STEUERKONTO = 4838;

	// RZL spezifisch
	public static final int FEHLER_FINANZ_EXPORT_RZL_KONTONUMMER_MAXIMAL_6_STELLEN = 4840;
	public static final int FEHLER_FINANZ_EXPORT_RZL_KONTONUMMER_MAXIMAL_9_STELLEN = 4841;
	public static final int FEHLER_FINANZ_EXPORT_RZL_KEIN_UST_LAND_CODE = 4842;
	public static final int FEHLER_FINANZ_EXPORT_RZL_KEIN_NUMERISCHER_UST_LAND_CODE = 4843;

	// ZV Export ab 4850
	public static final int FEHLER_FINANZ_ZVEXPORT_LF_HAT_KEINE_BANKVERBINDUNG = 4850;
	public static final int FEHLER_FINANZ_ZVEXPORT_BANK_HAT_KEINEN_ORT = 4851;
	public static final int FEHLER_FINANZ_ZVEXPORT_EXPORT_FEHLGESCHLAGEN = 4852;
	public static final int FEHLER_FINANZ_ZVEXPORT_BANK_AUS_NICHT_SEPA_LAND = 4853;
	public static final int FEHLER_FINANZ_ZVEXPORT_KEINE_BELEGE_ZU_EXPORTIEREN = 4854;
	public static final int FEHLER_FINANZ_ZVEXPORT_ER_MIT_ABWEICHENDER_BANKVERBINDUNG_HAT_KEINE = 4855;

	// Intrastat Export ab 4860
	public static final int FEHLER_FINANZ_INTRASTAT_KEINE_WVK_NR = 4860;
	public static final int FEHLER_FINANZ_INTRASTAT_ARTIKEL_BENOETIGT_GEWICHT = 4861;
	public static final int FEHLER_FINANZ_INTRASTAT_UID_BENOETIGT = 4862;

	// BMD spezifisch ab 4880
	public static final int FEHLER_FINANZ_EXPORT_BMD_KONTONUMMER_MAXIMAL_9_STELLEN = 4880;

	// mahnwesen ab 4900
	public static final int FEHLER_FINANZ_MAHNSTUFEN_1_2_3_DUERFEN_NICHT_GELOESCHT_WERDEN = 4900;
	public static final int FEHLER_FINANZ_KEINE_MAHNTEXTE_EINGETRAGEN = 4901;
	public static final int FEHLER_FINANZ_KEINE_MAHNSTUFEN_EINGETRAGEN = 4902;
	public static final int FEHLER_FINANZ_MAHNLAUF_WURDE_SCHON_UEBERNOMMEN = 4903;
	public static final int FEHLER_FINANZ_MAHNTEXTE_1_2_3_DUERFEN_NICHT_GELOESCHT_WERDEN = 4904;
	public static final int FEHLER_FINANZ_MAHNLAUF_LASTSCHRIFTVORSCHLAEGE_SCHON_GESPEICHERT = 4905;

	// 5000-5999: System
	// **********************************************************
	public static final int FEHLER_SYSTEM_VERSANDAUFTRAG_WURDE_BEREITS_VERSANDT = 5000;
	public static final int FEHLER_SYSTEM_VERSANDAUFTRAG_KANN_NICHT_STORNIERT_WERDEN = 5001;
	public static final int FEHLER_SYSTEM_STUECK_KANN_NICHT_GELOESCHT_WERDEN = 5002;
	public static final int FEHLER_SYSTEM_SEKUNDE_KANN_NICHT_GELOESCHT_WERDEN = 5003;
	public static final int FEHLER_SYSTEM_STUNE_KANN_NICHT_GELOESCHT_WERDEN = 5004;
	public static final int FEHLER_SYSTEM_MINUTE_KANN_NICHT_GELOESCHT_WERDEN = 5005;
	public static final int FEHLER_SYSTEM_EINHEITKONVERTIERUNG_SCHON_VORHANDEN = 5006;
	public static final int FEHLER_SYSTEM_EINHEITKONVERTIERUNG_GLEICHE_EINHEITEN = 5007;
	public static final int FEHLER_SYSTEM_MWSTSATZ_IST_NULL = 5008;
	public static final int FEHLER_SYSTEM_KOSTENSTELLE_IN_VERWENDUNG = 5009;
	public static final int FEHLER_SYSTEM_JMS = 5010;
	public static final int FEHLER_GOOGLE_APIKEY_NICHT_DEFINIERT = 5011;
	public static final int FEHLER_GOOGLE_GEODATEN_ABFRAGE_NICHT_ERFOLGREICH = 5012;

	// 6000-6999: Partner (Kunde, Sachbearbeiter, Ansprechpartner, Lieferant)
	// *****
	public static final int FEHLER_PARTNER_LIEFERART = 6000;
	public static final int FEHLER_PARTNER_VKPREISFINDUNGPREISLISTENNAME = 6001;
	public static final int FEHLER_PARTNER_SPEDITEUR = 6002;
	public static final int FEHLER_PARTNER_ZAHLUNGSZIEL = 6003;
	public static final int FEHLER_PARTNER_MWST = 6004;
	public static final int FEHLER_KEIN_PARTNER_GEWAEHLT = 6006;
	public static final int WARNUNG_KTO_BESETZT = 6007;
	public static final int WARNUNG_KUNDEN_UID_NUMMER_NICHT_HINTERLEGT = 6008;
	public static final int FEHLER_PARTNER_KOMM_AENDERN_NUR_EIGENER_MANDANT = 6009;
	public static final int FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH = 6010;
	public static final int FEHLER_PARTNER_LKZ_AENDERUNG_NICHT_MOEGLICH = 6011;
	public static final int FEHLER_LIEFERGUPPE_NICHT_ANGELEGT_IMPORT_ABGEBROCHEN = 6012;
	public static final int FEHLER_KUNDE_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_DEBITOREN = 6013;
	public static final int FEHLER_LIEFERANT_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_KREDITOREN = 6014;

	public static final int FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_MWST = 6015;
	public static final int FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_LKZ = 6016;
	public static final int FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_ABW_UST = 6017;
	public static final int FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_UID = 6018;
	public static final int FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_BANKVERBINDUNG_IN_FIBU_VORHANDEN = 6019;

	public static final int FEHLER_PARTNERART_DARF_NICHT_GELOESCHT_GEAENDERT_WERDEN = 6020;
	public static final int FEHLER_ANSPRECHPARTNER_EMAIL_NICHT_DEFINIERT = 6021;
	public static final int FEHLER_ANSPRECHPARTNER_EMAIL_NICHT_EINDEUTIG = 6022;

	public static final int FEHLER_KUNDE_GESPERRT = 6023;
	public static final int FEHLER_LIEFERANT_FREMDSYSTEMNUMMER_NICHT_NUMMERISCH = 6024;
	public static final int FEHLER_KUNDE_FREMDSYSTEMNUMMER_NICHT_NUMMERISCH = 6025;
	public static final int FEHLER_LIEFERANT_ABWEICHENDESUSTLAND_UNTERSCHIEDLICH = 6026;
	public static final int FEHLER_KUNDE_ABWEICHENDESUSTLAND_UNTERSCHIEDLICH = 6027;
	public static final int FEHLER_KUNDE_KOMBINATION_MMZ_MINBW_NICHT_MOEGLICH = 6028;
	public static final int FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_PARTNERKOMMENTAR = 6029;

	public static final int FEHLER_LAND_HAT_KEINE_VORWAHL = 6030;

	public static final int FEHLER_BEIM_ERZEUGEN_DER_KREDITOREN_DEBITORENNUMMER = 6031;
	
	// 7000-7999: Lieferschein
	// ****************************************************
	public static final int FEHLER_LIEFERSCHEIN_KEINEOFFENENLIEFERSCHEINE = 7000;
	public static final int FEHLER_LIEFERSCHEIN_KEINEANGELEGTENLIEFERSCHEINE = 7001;
	public static final int FEHLER_LIEFERSCHEIN_TEXTINKONZERNDATENSPRACHENICHTHINTERLEGT = 7002;
	public static final int FEHLER_LIEFERSCHEIN_DIEZUGEHOERIGELIEFERSCHEINPOSITIONWURDEGELOESCHT = 7003;
	public static final int FEHLER_LIEFERSCHEIN_CHN_SNR_ZU_WENIG_AUF_LAGER = 7004;
	public static final int FEHLER_LIEFERSCHEIN_AVISO_BEREITS_DURCHGEFUEHRT = 7005;
	public static final int FEHLER_LIEFERSCHEIN_VERSANDWEG_NICHT_UNTERSTUETZT = 7006;
	public static final int FEHLER_LIEFERSCHEIN_VERSANDWEG_IM_PARTNER_NICHT_DEFINIERT = 7007;
	public static final int FEHLER_LIEFERSCHEIN_VERSANDWEG_PARTNER_KUNDENNUMMER_FEHLT = 7008;
	public static final int FEHLER_LIEFERSCHEIN_ANDERN_MANDANT_NACHFUELLEN_MANDANT_KEIN_KUNDE = 7009;
	public static final int FEHLER_LIEFERSCHEIN_ENTHAELT_VERKETTETE_LIEFERSCHEINE = 7010;
	public static final int FEHLER_LIEFERSCHEIN_IST_VERKETTET = 7011;
	public static final int FEHLER_LIEFERSCHEIN_IST_BEREITS_VERKETTET = 7012;
	public static final int FEHLER_LIEFERSCHEIN_HAT_KEINEN_AUFTRAGSBEZUG = 7013;
	public static final int FEHLER_LIEFERSCHEIN_ZU_VIELE_ANGELEGTE = 7014;
	public static final int FEHLER_AUSLIEFERVORSCHLAG_IST_GESPERRT = 7015;
	public static final int FEHLER_AUSLIEFERVORSCHLAG_BSPOS_ZU_WE_BEREITS_VORHANDEN = 7016;
	public static final int FEHLER_LIEFERSCHEIN_ARTIKEL_NICHT_IN_FORECAST_AUFTRAG = 7017;
	public static final int FEHLER_UMSORTIEREN_INTELLIGENTE_ZWS = 7018;
	public static final int FEHLER_ZIELLAGER_GLEICH_ABBUCHUNGSLAGER = 7019;
	public static final int FEHLER_LIEFERSCHEIN_HAT_KEINE_PLC_LIEFERART = 7020;
	public static final int FEHLER_PLC_APIKEY_FALSCHES_FORMAT = 7021;
	public static final int FEHLER_ES_EXISTIERT_BEREITS_EINE_VERSANDNUMMER = 7022;
	public static final int FEHLER_VERSANDNUMMER_NICHT_ERZEUGBAR = 7023;
	public static final int FEHLER_PLC_PFLICHTFELD_STRASSE_FEHLT = 7024;
	public static final int FEHLER_FREIER_LIEFERSCHEIN_OHNE_AUFTRAGSBEZUG_AN_ANDEREN_MANDANTEN = 7025;
	public static final int FEHLER_LIEFERSCHEIN_LIEFERANTENNUMMER_FEHLT = 7026;
//	public static final int FEHLER_AUFTRAG_KUNDENKENNUNG_FEHLT = 1025;
//	public static final int FEHLER_AUFTRAG_WAEHRUNG_FEHLT = 1026;
//	public static final int FEHLER_AUFTRAG_BESTELLPOSREFERENZ_FEHLT = 1027;
	public static final int FEHLER_LIEFERSCHEIN_VERSANDWEG_ENDPUNKT_FEHLT = 7027;
	public static final int FEHLER_LIEFERSCHEIN_VERSANDWEG_BENUTZER_FEHLT = 7028;
	public static final int FEHLER_LIEFERSCHEIN_VERSANDWEG_KENNWORT_FEHLT = 7029;
	public static final int FEHLER_LIEFERSCHEINPOSITION_MIT_WE_ANDERERMANDANT_VERKNUEPFT = 7030;

	// 8000-8999: Drucken
	// *********************************************************
	public static final int FEHLER_DRUCKEN_REPORT_NICHT_GEFUNDEN = 8000;
	public static final int FEHLER_DRUCKEN_FALSCHE_VERSION = 8001;
	public static final int FEHLER_DRUCKEN_KEINE_DATEN_ZU_DRUCKEN = 8002;
	public static final int FEHLER_DRUCKEN_FEHLER_IM_REPORT = 8003;
	public static final int FEHLER_DRUCKEN_FILE_NOT_FOUND = 8004;
	public static final int FEHLER_DRUCKEN_KEINE_DRUCKER_INSTALLIERT = 8005;
	public static final int FEHLER_DRUCKEN_KEIN_DRUCKERNAME = 8006;
	public static final int FEHLER_DRUCKEN_FONT_NICHT_GEFUNDEN_SERVER = 8007;
	public static final int FEHLER_DRUCKEN_FONT_NICHT_GEFUNDEN_CLIENT = 8008;
	public static final int FEHLER_DRUCKEN_UNBEKANNTE_REPORTVARIANTE_ZU_REPORT = 8009;
	public static final int FEHLER_DRUCKEN_MANDANTENPARAMETER_KEIN_DRUCKERNAME = 8010;
	public static final int FEHLER_DRUCKEN_MANDANTENPARAMETER_UNBEKANNTER_DRUCKERNAME = 8011;
	public static final int FEHLER_DRUCKEN_MAILTEXTVORLAGE_NICHT_GEFUNDEN = 8012;
	public static final int FEHLER_DRUCKEN_MAILTEXTVORLAGE_HTML_NICHT_GEFUNDEN = 8013;
	public static final int FEHLER_DRUCKEN_MAILTEXTVORLAGE_MIT_SIGNATUR_NICHT_GEFUNDEN = 8014;

	// 9000-9999: Eingangsrechnung
	// ************************************************
	public static final int FEHLER_WERT_UNTER_AUFTRAGSZUORDNUNG = 9000;
	public static final int FEHLER_LIEFERANTENRECHNUNGSNUMMER_DOPPELT = 9001;
	public static final int FEHLER_KONTIERUNG_ZUGEORDNET = 9002;
	public static final int FEHLER_ZUSATZKOSTEN_FEHLER_WIEDERHOLUNGERLEDIGT = 9003;
	public static final int FEHLER_ER_ANZAHLUNGEN_REVERSE_CHARGE_ABWEICHEND = 9004;
	public static final int FEHLER_ER_RUECKNAHME_NICHT_MOEGLICH_BEREITS_IN_FIBU = 9005;
	public static final int FEHLER_ER_AUFTRAGSZUORDNUNG_KOPIEREN_ZUVIEL = 9006;
	public static final int FEHLER_ER_BEREITS_REISEZEIT_ZUGEORDNET = 9007;
	public static final int FEHLER_ER_LIEFERANT_FUER_MANDANT_FEHLT = 9008;

	// Import ab 9600
	public static final int FEHLER_ER_IMPORT_FELD_LEER = 9600;
	public static final int FEHLER_ER_IMPORT_UNMARSHAL_FEHLGESCHLAGEN = 9601;
	public static final int FEHLER_ER_IMPORT_DEBITORENNUMMER_NICHT_GEFUNDEN = 9602;
	public static final int FEHLER_ER_IMPORT_DEBITORENNUMMER_NICHT_EINDEUTIG = 9603;
	public static final int FEHLER_ER_IMPORT_WARENKONTO_NICHT_DEFINIERT = 9604;
	public static final int FEHLER_ER_IMPORT_KOSTENSTELLE_NICHT_DEFINIERT = 9605;
	public static final int FEHLER_ER_IMPORT_MWSTSATZ_NICHT_DEFINIERT = 9606;
	public static final int FEHLER_ER_IMPORT_FIELD_INSTANZIERUNG_FEHLGESCHLAGEN = 9607;
	public static final int FEHLER_ER_IMPORT_ER_ANLEGEN_FEHLGESCHLAGEN = 9608;
	public static final int FEHLER_ER_IMPORT_XML_KLASSEN_TRANSFORMATION = 9609;
	public static final int FEHLER_ER_IMPORT_FREMDSYSTEMNUMMER_NICHT_GEFUNDEN = 9610;
	public static final int FEHLER_ER_IMPORT_FREMDSYSTEMNUMMER_NICHT_EINDEUTIG = 9611;
	public static final int FEHLER_ER_IMPORT_RE_ANLEGEN_FEHLGESCHLAGEN = 9612;
	public static final int FEHLER_ER_IMPORT_UEBERSTEUERTER_MWSTSATZ_NICHT_GEFUNDEN = 9613;

	// 10000-10999: Bestellung
	// ****************************************************
	public static final int FEHLER_BESTELLVORSCHLAG_IST_GESPERRT = 10000;
	public static final int FEHLER_BESTELLUNG_MAHNSPERRE = 10001;
	public static final int FEHLER_BESTELLUNG_NEUE_MAHNSTUFE_MUSS_GROESSER_SEIN_ALS_DIE_ALTE = 10002;
	public static final int FEHLER_WARENEINGANG_DARF_LAGER_NICHT_AENDERN = 10003;
	public static final int FEHLER_BESTELLUNG_IST_BEREITS_ERLEDIGT = 10004;
	public static final int FEHLER_MAHNUNGSVERSAND_KEINE_ABSENDERADRESSE = 10005;
	public static final int FEHLER_BESTELLUNG_FALSCHER_STATUS = 10006;
	public static final int FEHLER_KEIN_DOKUMENT_BEI_DOKUMENTENPFLICHTIGEM_ARTIKEL_HINTERLEGT = 10007;
	public static final int FEHLER_BESTELLUNG_HAT_KEINE_MENGENPOSITIONEN = 10008;
	public static final int FEHLER_BESTELLUNG_ARTIKEL_DARF_NICHT_MEHR_GEAENDERT_WERDEN = 10009;
	public static final int FEHLER_BESTELLUNG_WEPOS_PREIS_NOCH_NICHT_ERFASST = 10010;
	public static final int FEHLER_BESTELLUNG_NUR_KOPFARTIKEL_ZUBUCHBAR = 10011;
	public static final int FEHLER_KEIN_DOKUMENT_BEI_DOKUMENTENPFLICHTIGEM_ARTIKEL_IM_LOS_HINTERLEGT = 100011;
	public static final int FEHLER_LIEFERADRESSE_NUR_AENDERBAR_WENN_KEINE_PREISE_ERFASST = 10012;
	public static final int FEHLER_GEBINDE_IN_RAHMEN_UND_ABRUF_NICHT_MOEGLICH = 10013;
	public static final int FEHLER_BESTELLUNG_PAUSCHALKORREKTUR_ALLGEMEINER_RABATT_MUSS_0_SEIN = 100014;
	public static final int FEHLER_BESTELLUNG_PAUSCHALKORREKTUR_NUR_BIS_WARENEINGANG_MOEGLICH = 100015;
	public static final int FEHLER_BESTELLVORSCHLAG_ANDERER_MANDANT_LIEFERANT_NICHT_ANGELEGT = 100016;
	public static final int FEHLER_BESTELLUNG_ANDERER_MANDANT_SET_KANN_NICHT_BESTELLT_WERDEN = 100017;
	public static final int FEHLER_BESTELLUNG_HAT_ARTIKEL_KEINE_LAGERZUBUCHUNG = 100018;
	public static final int FEHLER_BESTELLUNG_HAT_NEGATIVE_MENGENPOSITIONEN = 10019;
	public static final int FEHLER_BESTELLUNG_SPLITT_WEP_MENGE_ZU_GROSS = 10020;
	public static final int FEHLER_BESTELLUNG_XML_TRANSFORMATION_LIEFARTIKELNR_FEHLT = 10021;
	public static final int FEHLER_BESTELLUNG_XML_MARSHALLING_EXC = 10022;
	public static final int FEHLER_BESTELLUNG_XML_LIEFKUNDENNUMMER_FEHLT = 10023;
	public static final int FEHLER_BESTELLUNG_XML_FEHLENDES_EINHEITENMAPPING = 10024;

	// Angebot 11000-11999
	public static final int FEHLER_ANGEBOT_NETTOWERTKANNNICHTBERECHNETWERDEN = 11000;
	public static final int FEHLER_ANGEBOT_ZUSAMMENFASSUNG_POSITIONEN_OHNE_ZWS = 11001;
	public static final int FEHLER_INITIALKOSTENARTIKEL_NICHT_VORHANDEN = 11002;

	// Fertigung 12000-12999
	public static final int FEHLER_FERTIGUNG_DARF_FUER_MATERIALLISTE_NICHT_DURCHGEFUEHRT_WERDEN = 12000;
	public static final int FEHLER_FERTIGUNG_STUECKLISTE_MATERIAL_WURDE_GEAENDERT = 12001;
	public static final int FEHLER_FERTIGUNG_STUECKLISTE_ARBEITSPLAN_WURDE_GEAENDERT = 12002;
	public static final int FEHLER_FERTIGUNG_LAGERENTNAHME_DARF_NICHT_GELOESCHT_WERDEN = 12003;
	public static final int FEHLER_FERTIGUNG_MATERIAL_AUS_STUECKLISTE_DARF_NICHT_GELOESCHT_WERDEN = 12004;
	public static final int FEHLER_FERTIGUNG_POSITION_AUS_ARBEITSPLAN_DARF_NICHT_GELOESCHT_WERDEN = 12005;
	public static final int FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT = 12006;
	public static final int FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT = 12007;
	public static final int FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_AUSGEGEBEN = 12008;
	public static final int FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN = 12009;
	public static final int FEHLER_FERTIGUNG_DAS_LOS_IST_GESTOPPT = 12010;
	public static final int FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_TEILERLEDIGT = 12011;
	public static final int FEHLER_ABLIEFERN_PER_AUFTRAGSNUMMER_NICHT_MOEGLICH = 12012;
	public static final int FEHLER_ABBUCHUNG_SNRCHNR_ABGEBROCHEN = 12013;
	public static final int FEHLER_FERTIGUNG_AENDERUNG_LOGROESSE_ZUVIELEABLIEFERUNGEN = 12014;
	public static final int FEHLER_FERTIGUNG_SOLLSATZGROESSE_UNTERSCHRITTEN = 12015;
	public static final int FEHLER_FERTIGUNG_UPDATE_LOSABLIEFERUNG_FEHLER_MENGE = 12016;
	public static final int FEHLER_FERTIGUNG_INTERNE_BESTELLUNG_ZU_VIELE_UNTERSTUECKLISTEN = 12017;
	public static final int FEHLER_FERTIGUNG_ES_IST_NOCH_MATERIAL_AUSGEGEBEN = 12018;
	public static final int FEHLER_FERTIGUNG_LOS_OHNE_KUNDE = 12019;
	public static final int FEHLER_FERTIGUNG_AUSGABE_ES_WUERDEN_FEHLMENGEN_ENTSTEHEN = 12020;
	public static final int FEHLER_FERTIGUNG_LOSNUMMER_NACH_BEREICH_UEBERLAUF = 12021;
	public static final int FEHLER_FERTIGUNG_AUF_DEM_LOS_IST_MATERIAL_AUSGEGEBEN = 12022;
	public static final int FEHLER_FERTIGUNG_FERTIGUNGSGRUPPE_SOFORTVERBRAUCH_NICHT_VORHANDEN = 12023;
	public static final int FEHLER_FERTIGUNG_MATERIAL_VOLLSTAENDIG = 12024;
	public static final int FEHLER_FERTIGUNG_HILFSSTUECKLISTE_DARF_KEINE_SOLLPOSITION_SEIN = 12025;
	public static final int FEHLER_MATERIALBUCHUNG_NUR_VON_KOMISSIONIERTERMINAL_MOEGLICH = 12026;
	public static final int FEHLER_LOSAUSGABE_NUR_VON_KOMISSIONIERTERMINAL_MOEGLICH = 12027;
	public static final int FEHLER_LOSAUSGABE_HTTP_POST_VERBINDUNGSFEHLER = 12028;
	public static final int FEHLER_LOSAUSGABE_HTTP_POST_STATUS_CODE = 12029;
	public static final int FEHLER_LOSERLEDIGUNG_HTTP_POST_VERBINDUNGSFEHLER = 12030;
	public static final int FEHLER_LOSERLEDIGUNG_HTTP_POST_STATUS_CODE = 12031;
	public static final int FEHLER_INTERNEBESTELLUNG_IST_GESPERRT = 12032;
	public static final int FEHLER_LOSARBEITSPLAN_GELOESCHT = 12033;
	public static final int FEHLER_LOSAUSGABE_RUESTMENGE_NICHT_AUF_LAGER = 12034;
	public static final int FEHLER_INTERNEBESTELLUNG_LOS_BEREITS_VORHANDEN = 12035;
	public static final int FEHLER_FERTIGUNG_RESTMENGENLAGER_UNGUELTIG = 12036;
	public static final int FEHLER_FERTIGUNG_FEHLER_IN_RESTMENGE = 12037;
	public static final int FEHLER_FERTIGUNG_NEGATIVE_SOLLMENGE_ARTIKEL_SNR_CNHR_BEHAFTET = 12038;
	public static final int FEHLER_FERTIGUNG_UPDATE_STKL_SOLLZEIT_ARBEITSPLAN_ANZAHL_UNGLEICH = 12039;
	public static final int FEHLER_FERTIGUNG_UPDATE_STKL_SOLLZEIT_ARBEITSPLAN_IN_STKL_NICHT_GEFUNDEN = 12040;

	// Fertigung Import ab 12600
	public static final int FEHLER_FERTIGUNG_IMPORT_UNMARSHAL_FEHLGESCHLAGEN = 12600;
	public static final int FEHLER_FERTIGUNG_IMPORT_FIELD_INSTANZIERUNG_FEHLGESCHLAGEN = 12601;
	public static final int FEHLER_FERTIGUNG_IMPORT_BASEARTIKELID_NICHT_VORHANDEN = 12602;
	public static final int FEHLER_FERTIGUNG_IMPORT_4VENDING_ID_NICHT_VORHANDEN = 12603;
	public static final int FEHLER_FERTIGUNG_IMPORT_TOURLAGER_NICHT_VORHANDEN = 12604;
	public static final int FEHLER_FERTIGUNG_IMPORT_KEINE_FERTIGUNGSGRUPPE_VORHANDEN = 12605;
	public static final int FEHLER_FERTIGUNG_IMPORT_KEINE_MONTAGEART_VORHANDEN = 12606;
	public static final int FEHLER_FERTIGUNG_IMPORT_KEINE_KOSTENSTELLE_DEFINIERT = 12607;
	public static final int FEHLER_FERTIGUNG_IMPORT_LOS_ANLEGEN_FEHLGESCHLAGEN = 12608;
	public static final int FEHLER_FERTIGUNG_IMPORT_LOSSOLLMATERIAL_ANLEGEN_FEHLGESCHLAGEN = 12609;
	public static final int FEHLER_FERTIGUNG_IMPORT_MATERIALAUSGABE_FEHLGESCHLAGEN = 12610;
	public static final int FEHLER_FERTIGUNG_IMPORT_FEHLMENGE_KONNTE_NICHT_GEBUCHT_WERDEN = 12611;
	public static final int FEHLER_FERTIGUNG_IMPORT_LOSSTATUS = 12612;
	
	public static final int FEHLER_TOPS_LOS_NICHT_GEFUNDEN = 12700;
	public static final int FEHLER_TOPS_KEIN_KUNDE_ZU_LOS = 12701;
	public static final int FEHLER_TOPS_KEIN_DEBITORENKONTO = 12702;
	public static final int FEHLER_TOPS_KEIN_MATERIAL = 12703;
	public static final int FEHLER_TOPS_KEIN_ARBEITSGANG = 12704;
	public static final int FEHLER_TOPS_IST_LETZTER_AG = 12705;
	public static final int FEHLER_TOPS_ARTIKEL_KEIN_MATERIAL = 12706;
	public static final int FEHLER_TOPS_ARTIKEL_KEINE_LASEROBERFLAECHE = 12707;
	public static final int FEHLER_TOPS_ARTIKEL_KEINE_HOEHE = 12708;
	public static final int FEHLER_TOPS_LOS_IST_KEINE_STKL = 12709;
	public static final int FEHLER_TOPS_XMLMARSHALLING = 12710;
	public static final int FEHLER_TOPS_IO_FILEEXPORT_MATERIAL = 12711;
	public static final int FEHLER_TOPS_IO_FILEEXPORT_ARTIKEL = 12712;
	public static final int FEHLER_TOPS_UPDATE_MATERIAL = 12713;
	public static final int FEHLER_TOPS_UPDATE_ARTIKEL = 12714;
	public static final int FEHLER_TOPS_KEIN_CADFILE_IN_ARTIKEL_PFAD = 12715;
	public static final int FEHLER_TOPS_KEIN_CADFILE_IN_STKL_PFAD = 12716;
	public static final int FEHLER_TOPS_KEIN_CADFILE_IN_HILFSSTKL_PFAD = 12717;
	public static final int FEHLER_TOPS_LETZTER_EXPORT_PFAD_UNTERSCHIEDLICH = 12718;
	public static final int FEHLER_TOPS_IO_SEARCHING_CADFILE = 12719;
	public static final int FEHLER_TOPS_IO_READING_METADATEN = 12720;
	public static final int FEHLER_TOPS_CADFILE_NICHT_GEFUNDEN = 12721;
	public static final int FEHLER_TOPS_UNBEKANNT = 12722;
	public static final int FEHLER_TOPS_FALLBACK_MEHRERE_CADFILES_PRO_ARTIKEL = 12723;

	// Fertigung 13000-13999
	public static final int FEHLER_BES_SUBSCRIPTION_FEHLGESCHLAGEN = 13000;
	public static final int FEHLER_BES_SUBSCRIPTION_ABBRUCH_FEHLGESCHLAGEN = 13001;

	// Inserat 14000-14999
	public static final int FEHLER_INSERAT_EIN_KUNDE_MUSS_VORHANDEN_SEIN = 14000;
	// Projekt 15000-15999
	public static final int FEHLER_PROJEKT_DARF_NICHT_STORNIERT_WERDEN_ZEITEN_VORHANDEN = 15000;
	public static final int FEHLER_ZEITBUCHUNG_STORNIERTES_PROJEKT_NICHT_MOEGLICH = 15001;
	public static final int FEHLER_ZEITBUCHUNG_INTERN_ERLEDIGTES_PROJEKT_NICHT_MOEGLICH = 15002;
	public static final int FEHLER_ZEITBUCHUNG_ANGELEGTES_LOS_NICHT_MOEGLICH = 15003;
	public static final int FEHLER_ZEITBUCHUNG_ERLEDIGTES_LOS_NICHT_MOEGLICH = 15004;
	public static final int FEHLER_ZEITBUCHUNG_ERLEDIGTER_AUFTRAG_NICHT_MOEGLICH = 15005;
	public static final int FEHLER_ZEITBUCHUNG_STORNIERTER_AUFTRAG_NICHT_MOEGLICH = 15006;
	public static final int FEHLER_ZEITBUCHUNG_AUF_FERTIGEN_ARBEITSGANG_NICHT_MOEGLICH = 15007;
	public static final int FEHLER_ZEITBUCHUNG_GESTOPPTES_LOS_NICHT_MOEGLICH = 15008;
	public static final int FEHLER_ZEITBUCHUNG_STORNIERTES_LOS_NICHT_MOEGLICH = 15009;
	public static final int FEHLER_ZEITBUCHUNG_ERLEDIGTES_PROJEKT_NICHT_MOEGLICH = 15010;

	public static final int FEHLER_ZEITBUCHUNG_STORNIERTES_ANGEBOT_NICHT_MOEGLICH = 15011;
	public static final int FEHLER_ZEITBUCHUNG_ERLEDIGTES_ANGEBOT_NICHT_MOEGLICH = 15012;
	public static final int FEHLER_ZEITBUCHUNG_TAETIGKEIT_AUF_PROJEKT_NICHT_MOEGLICH = 15013;
	public static final int FEHLER_PROJEKTGRUPPE_DEADLOCK = 15014;
	public static final int FEHLER_PROJEKT_HISTORYART_IN_AUSWAHLLISTE_ANZEIGEN_DARF_NUR_EINMAL_VORHANDEN_SEIN = 15015;

	// Sepa-Import/Export 16000 - 16999
	public static final int FEHLER_SEPAIMPORT_LANDESRICHTLINIE_VERLETZT = 16000;
	public static final int FEHLER_SEPAIMPORT_INSTANZIERUNG_DER_NULL_OBJEKTE = 16001;
	public static final int FEHLER_SEPAIMPORT_CAMT053_VERSION_NICHT_UNTERSTUETZT = 16002;
	public static final int FEHLER_SEPAIMPORT_LAENDERKENNZAHL_PATTERN_FEHLERHAFT = 16003;
	public static final int FEHLER_SEPAIMPORT_ISO_URI_NICHT_GEFUNDEN = 16004;
	public static final int FEHLER_SEPAIMPORT_KEIN_VALIDATOR_FUER_LAND_VORHANDEN = 16005;
	public static final int FEHLER_SEPAIMPORT_TRANSFORMATION_FEHLGESCHLAGEN = 16006;
	public static final int FEHLER_SEPAIMPORT_MANDANT_HAT_KEINE_BANKVERBINDUNG = 16007;
	public static final int FEHLER_SEPAIMPORT_KTOINFO_NICHT_ALS_BANKVERBINDUNG_BEKANNT = 16008;
	public static final int FEHLER_SEPAIMPORT_KEIN_SEPA_VERZEICHNIS_VORHANDEN = 16009;
	public static final int FEHLER_SEPAIMPORT_KEINE_XML_DATEI_VORHANDEN = 16010;
	public static final int FEHLER_SEPAIMPORT_SEITENNUMMERN_INKONSISTENT = 16011;
	public static final int FEHLER_SEPAIMPORT_KTOAUSZUG_HAT_KEINE_AUSZUGSNUMMER = 16012;
	public static final int FEHLER_SEPAIMPORT_SALDEN_STIMMEN_MIT_BETRAEGEN_NICHT_UEBEREIN = 16013;
	public static final int FEHLER_SEPAIMPORT_ENDE_DES_KONTOAUSZUGS_NICHT_ERKANNT = 16014;
	public static final int FEHLER_SEPAIMPORT_MEHRERE_KONTOAUSZUEGE_GEFUNDEN = 16015;
	public static final int FEHLER_SEPAIMPORT_CAMT052_WIRD_NICHT_UNTERSTUETZT = 16016;
	public static final int FEHLER_SEPAIMPORT_UNBEKANNTE_XML_DATEI = 16017;
	public static final int FEHLER_SEPAIMPORT_CAMT052_VERSION_NICHT_UNTERSTUETZT = 16018;
	public static final int FEHLER_SEPAIMPORT_BUCHUNGSBETRAEGE_FEHLERHAFT = 16019;
	public static final int FEHLER_SEPAIMPORT_DIRECTORY_NOT_FOUND = 16020;
	public static final int FEHLER_SEPAIMPORT_KTOAUSZUG_HAT_KEINE_SALDEN = 16021;
	public static final int FEHLER_SEPAIMPORT_KEINE_FORTLAUFENDE_ELEKTRONISCHE_AUSZUGSNUMMER = 16022;
	public static final int FEHLER_SEPAIMPORT_FELD_NULL_ODER_LEER = 16023;
	public static final int FEHLER_SEPAIMPORT_AUSGEWAEHLTES_CAMT_FORMAT_STIMMT_NICHT_UEBEREIN = 16024;
	public static final int FEHLER_SEPAIMPORT_BUCHUNGEN_MIT_AUSZUGSNUMMER_EXISTIEREN = 16025;
	public static final int FEHLER_SEPAIMPORT_KEIN_OFFENER_SEPAKONTOAUSZUG = 16026;
	public static final int FEHLER_SEPAIMPORT_SALDENBETRAEGE_FEHLERHAFT = 16027;
	public static final int FEHLER_SEPAIMPORT_UNTERSCHIEDLICHE_CAMT_FORMATE = 16028;
	public static final int FEHLER_SEPAVERBUCHUNG_BUCHUNGEN_MIT_AUSZUGSNUMMER_EXISTIEREN = 16029;
	public static final int FEHLER_SEPAVERBUCHUNG_KTOAUSZUG_NICHT_IM_AKTUELLEN_GJ = 16030;
	public static final int FEHLER_SEPAVERBUCHUNG_KTOAUSZUG_FALSCHER_STATUS = 16031;
	public static final int FEHLER_SEPAVERBUCHUNG_ZAHLUNGEN_MIT_AUSZUGSNUMMER_EXISTIEREN = 16032;
	public static final int FEHLER_SEPAIMPORT_WAEHRUNG_AUFTRAGSBETRAG = 16033;
	public static final int FEHLER_SEPAIMPORT_ZUVIELE_OFFENE_AR = 16034;
	public static final int FEHLER_SEPAIMPORT_ZUVIELE_OFFENE_ER = 16035;

	public static final int FEHLER_SEPAEXPORT_EXPORT_FEHLGESCHLAGEN = 16500;
	public static final int FEHLER_SEPAEXPORT_BANK_AUS_NICHT_SEPA_LAND = 16501;
	public static final int FEHLER_SEPAEXPORT_BANK_HAT_KEINEN_ORT = 16502;
	public static final int FEHLER_SEPAEXPORT_LF_HAT_KEINE_BANKVERBINDUNG = 16503;
	public static final int FEHLER_SEPAEXPORT_KEIN_SEPA_VERZEICHNIS_VORHANDEN = 16504;
	public static final int FEHLER_SEPAEXPORT_KEINE_IBAN_VORHANDEN = 16505;
	public static final int FEHLER_SEPAEXPORT_KEINE_BIC_VORHANDEN = 16506;
	public static final int FEHLER_SEPAEXPORT_KEINE_EURO_BELEGE = 16507;
	public static final int FEHLER_SEPAEXPORT_KEINE_LASTSCHRIFTEN = 16508;
	public static final int FEHLER_SEPAEXPORT_BIC_UNGUELTIG = 16509;
	public static final int FEHLER_SEPAEXPORT_KEIN_STANDARD_IN_BANKVERBINDUNG = 16510;
	public static final int FEHLER_SEPAEXPORT_KEINE_BELEGE = 16511;
	public static final int FEHLER_SEPAEXPORT_ER_MIT_ABWEICHENDER_BANKVERBINDUNG_HAT_KEINE = 16512;

	public static final int FEHLER_SEPA_STORNIEREN_BEREITS_VERBUCHT = 16800;
	// public static final int
	// FEHLER_FERTIGUNG_KAPAZITAETSVORSCHAU_KEINE_ARTIKELGRUPPEN = 12012;
	// public static final int
	// FEHLER_FERTIGUNG_KAPAZITAETSVORSCHAU_KEINE_MASCHINENGRUPPEN = 12013;

	// Forecast 17000 - 17999
	public static final int FEHLER_FORECAST_ES_IST_EIN_ANGELEGTER_NICHT_FREIGEGEBENER_FCAUFTRAG_VORHANDEN = 17000;
	public static final int FEHLER_FREIGABE_BZW_RUECKNAHME_EINES_ERLEDIGTEN_AUFTRAGES_NICHT_MOEGLICH = 17001;
	public static final int FEHLER_RUECKNAHME_DER_FREIGABE_NICHT_MOEGLICH_DA_NICHT_FREIGEGEBENER_VORHANDEN = 17002;
	public static final int FEHLER_FORECAST_IMPORT_UNGUELTIGE_ZAHL = 17003;
	public static final int FEHLER_FORECAST_IMPORT_UNGUELTIGES_DATUM = 17004;
	public static final int FEHLER_FORECAST_IMPORT_ANZAHL_FELDER = 17005;
	public static final int FEHLER_FORECAST_IMPORT_ARTIKEL_NICHT_GEFUNDEN = 17006;
	public static final int FEHLER_FORECAST_IMPORT_BESTELLNUMMER_LEER = 17007;
	public static final int FEHLER_FORECAST_IMPORT_DATUM_LEER = 17008;
	public static final int FEHLER_FORECAST_IMPORT_MENGE_LEER = 17009;
	public static final int FEHLER_FORECAST_IMPORT_MEHR_ALS_EINEN_ARTIKEL_GEFUNDEN = 17010;
	public static final int FEHLER_FORECAST_IMPORT_DATEI_NICHT_LESBAR = 17011;
	public static final int FEHLER_FORECAST_IMPORT_AUFTRAG_MIT_STATUS_ANGELEGT = 17012;
	public static final int FEHLER_FORECAST_IMPORT_KUNDE_NICHT_GEFUNDEN = 17013;
	public static final int FEHLER_FORECAST_IMPORT_LIEFERKUNDE_NICHT_GEFUNDEN = 17014;
	public static final int FEHLER_FORECAST_IMPORT_UNBEKANNTE_FORECAST_ART = 17015;
	public static final int FEHLER_FREIGABE_AUFGRUND_FEHLENDER_LOSE_AUF_LINIENABRUFEN_NICHT_MOEGLICH = 17016;
	public static final int FEHLER_FORECAST_ES_IST_BEREITS_EIN_EIN_IMPORTPFAD_IN_EINER_LIEFERADRESSE_DEFINIERT = 17017;
	public static final int FEHLER_FORECAST_IMPORT_LINIENABRUF_MIT_KOMMISSIONIEREN_VORHANDEN = 17018;
	public static final int FEHLER_FORECAST_MEHRERE_FREIGEGEBENE_FORECASTAUFTRAEGE_ZU_EINER_LIEFERADRESSE_VORHANDEN = 17019;
	public static final int FEHLER_FORECAST_IMPORT_LMFZ_UNGUELTIGES_DATUM = 17020;
	public static final int FEHLER_FORECAST_MEHRERE_LIEFERADRESSEN_FUER_KUNDE = 17021;
	public static final int FEHLER_FORECAST_KEIN_FREIGEGEBENER_FCAUFTRAG_VORHANDEN = 17022;
	public static final int FEHLER_FORECAST_BESTELLNUMMER_FEHLT = 17023;

	public static final int FEHLER_LINIENABRUF_PRODUKTION_KEIN_ZUGEHOERIGES_LOS_GEFUNDEN = 17300;
	public static final int FEHLER_LINIENABRUF_PRODUKTION_NICHT_GESTARTET = 17301;
	public static final int FEHLER_LINIENABRUF_PRODUKTION_STUECKLISTE_NICHT_IM_MANDANT = 17302;
	public static final int FEHLER_LINIENABRUF_PRODUKTION_ABLIEFERMENGE_KLEINER_EINS = 17303;
	public static final int FEHLER_LINIENABRUF_PRODUKTION_SOLLSATZGROESSEN_NICHT_GANZZAHLIG = 17304;

	public static final int FEHLER_LINIENABRUF_IMPORT_ZUWENIG_ZEILEN = 17500;
	public static final int FEHLER_LINIENABRUF_IMPORT_ZEILENLAENGE_ZU_KLEIN = 17501;
	public static final int FEHLER_LINIENABRUF_IMPORT_MENGE_NICHT_NUMERISCH = 17502;
	public static final int FEHLER_LINIENABRUF_IMPORT_PRODUKTIONSTERMIN_NICHT_IM_FC_AUFTRAG = 17503;
	public static final int FEHLER_LINIENABRUF_IMPORT_MEHR_ALS_EINEN_ARTIKEL_GEFUNDEN = 17504;
	public static final int FEHLER_LINIENABRUF_IMPORT_ARTIKEL_NICHT_IM_FC_AUFTRAG = 17505;
	public static final int FEHLER_LINIENABRUF_IMPORT_UNGUELTIGES_DATUM = 17506;
	public static final int FEHLER_LINIENABRUF_IMPORT_UNGUELTIGE_ZAHL = 17507;
	public static final int FEHLER_LINIENABRUF_IMPORT_NUR_CALLOFF_DAILY_ERLAUBT = 17508;
	public static final int FEHLER_LINIENABRUF_IMPORT_ARTIKEL_NICHT_GEFUNDEN = 17509;
	public static final int FEHLER_LINIENABRUF_IMPORT_LIEFERTERMIN_AUSSERHALB_POSITIONSTERMINE = 17510;
	public static final int FEHLER_LINIENABRUF_IMPORT_OFFSET_NICHT_GEFUNDEN = 17511;
	public static final int FEHLER_LINIENABRUF_IMPORT_KEINE_AUFTRAEGE_VORHANDEN = 17512;
	public static final int FEHLER_LINIENABRUF_IMPORT_MEHRERE_AUFTRAEGE_MIT_STATUS_ANGELEGT = 17513;
	public static final int FEHLER_LINIENABRUF_IMPORT_ARTIKEL_IM_KOMMISSIONIERSTATUS = 17514;

	public static final int FEHLER_EDIFACT_EXPORTVERZEICHNIS_NICHT_VORHANDEN = 17600;
	public static final int FEHLER_EDIFACT_EXPORTDATEI_NICHT_ERSTELLBAR = 17601;
	public static final int FEHLER_EDIFACT_ARTIKEL_BESTELLNUMMER_MEHRFACH_VORHANDEN = 17602;

	public static final int FEHLER_VMI_IMPORT = 17701;
	public static final int FEHLER_EPSILON_IMPORT = 17702;
	public static final int FEHLER_ROLLIERENDE_PLANUNG_IMPORT = 17703;
	public static final int FEHLER_ZEISS_IMPORT = 17704;

	// Angebotstkl/Einkaufsangebot 18000-18500
	public static final int FEHLER_EKAGLIERANT_IMPORT_KREDITORENNUMMER_FALSCH = 18000;
	public static final int FEHLER_EINKAUFSANGEBOT_VERDICHTEN_TEXTE_ZU_LANG = 18001;
	public static final int FEHLER_EKAGLIERANT_IMPORT_MEHRERE_BEFUELLTE_ZEILEN_EINES_ARTIKELS = 18002;

	private int code = NO_VALUE;
	private List<Object> alInfoForTheClient = null;
	private EJBExceptionData exceptionData;

	public EJBExceptionLP() {
		this(FEHLER, new Exception("undefined"));
	}

	public EJBExceptionLP(RemoteException eI) {
		super(eI);
		doLog("RemoteException", eI);
	}

	public EJBExceptionLP(Exception eI) {
		super(eI);
		doLog("Exception", eI);
	}

	/**
	 * EJBExceptionLP
	 * 
	 * @param codeI fuer den programmierer.
	 * @param al    Exception die aufgetreten ist.
	 * @param eI    Throwable
	 */
	public EJBExceptionLP(int codeI, List<Object> al, Exception eI) {
		super(eI);
		code = codeI;
		alInfoForTheClient = al;
		doLog("Fehlercode: " + codeI, new Exception("", this));
	}

	public EJBExceptionLP(EJBExceptionLP eI) {
		super(eI);
		// code = eI.getCode();
		code = eI.getCode();
		alInfoForTheClient = eI.getAlInfoForTheClient();
		doLog("Fehlercode: " + code, eI);
	}

	public EJBExceptionLP(int iCodeI, Exception eI) {
		super(eI);

		// if (eI == null) {
		// doLog("Fehlercode: " + iCodeI, new Exception("",this));
		// }
		//
		// if (eI instanceof EJBExceptionLP) {
		// code = ((EJBExceptionLP) eI).getCode();
		// doLog("Fehlercode: " + code, eI);
		// } else {
		// code = iCodeI;
		// if (eI != null) doLog("Fehlercode: " + code, eI);
		// }

		/*
		 * Umstrukturierung, weil in den Tests die mit einer EJB Exception fehlschlagen
		 * es hilfreich ist auch den Fehlercode (DUPLICATE usw.) zu sehen und nicht nur
		 * die Entity.
		 */
		code = (eI instanceof EJBExceptionLP) ? ((EJBExceptionLP) eI).getCode() : iCodeI;
		if (eI == null) {
			eI = new Exception("", this);
		}
		doLog("Fehlercode: " + code, eI);
	}

	public EJBExceptionLP(int iCodeI, String sTextForException) {
		this(iCodeI, new Exception(sTextForException));
	}

	public EJBExceptionLP(int iCodeI, String sTextForException, Object... alInfoForTheClient) {
		this(iCodeI, new ArrayList<Object>(Arrays.asList(alInfoForTheClient)), new Exception(sTextForException));
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setAlInfoForTheClient(List<Object> alInfoForTheClient) {
		this.alInfoForTheClient = alInfoForTheClient;
	}

	public int getCode() {
		return code;
	}

	// public ArrayList<Object> getAlInfoForTheClient() {
	public List<Object> getAlInfoForTheClient() {
		return alInfoForTheClient;
	}

	private void doLog(String errorI, Exception eI) {
		if (enableLog) {
			LPLogService.getInstance().getLogger(EJBExceptionLP.class).error(errorI, eI);
		}
	}

	public EJBExceptionData getExceptionData() {
		return exceptionData;
	}

	public void setExceptionData(EJBExceptionData exceptionData) {
		this.exceptionData = exceptionData;
	}
}
