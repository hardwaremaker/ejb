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
	static public final int NO_VALUE = -9999;
	static public final int FEHLER = -1;

	// 0 bis 500 Server alle Module uebergreifende Fehler
	// ***************************
	static public final int FEHLER_IM_BENUTZER = 0;
	static public final int FEHLER_IM_KENNWORT = 1;
	static public final int FEHLER_FALSCHES_KENNWORT = 2;
	static public final int FEHLER_BEIM_ANLEGEN = 3;
	static public final int FEHLER_BEIM_LOESCHEN = 4;
	static public final int FEHLER_BEIM_UPDATE = 5;
	static public final int FEHLER_BEI_FINDBYPRIMARYKEY = 6;
	static public final int FEHLER_FLR = 7;
	static public final int FEHLER_DTO_IS_NULL = 8;
	static public final int FEHLER_PKFIELD_IS_NULL = 9;
	static public final int FEHLER_FELD_IN_DTO_IS_NULL = 10;
	static public final int FEHLER_MUSS_GROESSER_0_SEIN = 11;
	static public final int FEHLER_PK_GENERATOR = 12;
	static public final int FEHLER_FELD_DARF_NICHT_NULL_SEIN = 13;
	static public final int FEHLER_DUPLICATE_UNIQUE = 14;
	static public final int FEHLER_IS_ALREADY_LOCKED = 15;
	static public final int FEHLER_HOME_IS_NULL = 16;
	static public final int FEHLER_LOCK_NOTFOUND = 17;
	static public final int FEHLER_BEIM_DRUCKEN = 18;
	static public final int FEHLER_BEIM_STORNIEREN = 19;
	static public final int FEHLER_BEI_FINDALL = 20;
	static public final int FEHLER_ZUWENIG_AUF_LAGER = 21;
	static public final int FEHLER_DUPLICATE_PRIMARY_KEY = 22;
	static public final int FEHLER_BEI_FIND = 23;
	static public final int FEHLER_MENGENREDUZIERUNG_NICHT_MOEGLICH = 24;
	static public final int FEHLER_ZAHL_ZU_KLEIN = 27;
	static public final int FEHLER_ZAHL_ZU_GROSS = 28;
	static public final int FEHLER_PARAMETER_IS_NULL = 29;
	static public final int FEHLER_STATUS = 30;
	static public final int WARNUNG_99_KUNDEN_PRO_BUCHSTABE = 31;
	static public final int FEHLER_C_NR_USER_IS_NULL = 32;
	public final static int FEHLER_KUNDE_HAT_KEINE_STANDARDPREISLISTE_HINTERLEGT = 33;
	public final static int FEHLER_DATEN_IN = 33;
	public final static int FEHLER_DATEN_INKOMPATIBEL = 34;
	public final static int FEHLER_KEIN_WECHSELKURS_HINTERLEGT = 35;
	public final static int FEHLER_MANDANTPARAMETER_NICHT_ANGELEGT = 36;
	public final static int FEHLER_DARF_MIR_NICHT_MICH_SELBST_ZUORDNEN = 37;
	// public final static int FEHLER_REPORT_KONNTE_NICHT_GELADEN_WERDEN = 38;
	public final static int FEHLER_NOT_IMPLEMENTED_YET = 39;
	public final static int FEHLER_BEI_EJBSELECT = 40;
	public final static int FEHLER_FALSCHER_MANDANT = 41;
	public final static int FEHLER_UNZUREICHENDE_RECHTE = 42;
	public final static int FEHLER_HIBERNATE = 45;
	public final static int FEHLER_BENUTZER_IST_GESPERRT = 46;
	public final static int FEHLER_BENUTZER_IST_NICHT_MEHR_GUELTIG = 47;
	public final static int FEHLER_BENUTZER_DARF_SICH_BEI_DIESEM_MANDANTEN_NICHT_ANMELDEN = 48;
	public final static int FEHLER_STAMMDATENCRUD_ZUVIELE_CONTROLS = 49;
	public final static int FEHLER_STAMMDATENCRUD_FEHLER = 50;
	public final static int FEHLER_PARAMETER_IS_NOT_NULL = 51;
	public final static int FEHLER_TRANSAKTION_NICHT_DURCHGEFUEHRT__ROLLBACK = 52;
	public final static int FEHLER_KEINE_VERBINDUNG_ZUM_JBOSS = 53;

	public final static int FEHLER_BUILDNUMMER_CLIENT_SERVER = 54;
	public final static int FEHLER_BUILDNUMMER_SERVER_DB = 55;

	public final static int FEHLER_FORMAT_NUMBER = 56;
	public final static int FEHLER_BELEG_WURDE_NICHT_MANUELL_ERLEDIGT = 57;
	public final static int FEHLER_NULLPOINTEREXCEPTION = 58;
	static public final int WARNUNG_CLIENT_UHRZEIT_UNGLEICH_SERVER_UHRZEIT = 60;
	static public final int WARNUNG_SERVER_JVM = 61;
	static public final int FEHLER_EINHEITKONVERTIERUNG_KEIN_DIVISOR_DEFINIERT = 62;
	public final static int FEHLER_ZUGEBUCHTES_MATERIAL_BEREITS_VOM_LAGER_ENTNOMMEN = 63;
	public final static int FEHLER_BENUTZER_KEIN_EINTRAG_IN_BENUTZERMANDANT = 64;
	public final static int FEHLER_UNBEKANNTER_DATENTYP = 65;
	public final static int FEHLER_WERT_NICHT_KONVERTIERBAR = 66;
	public final static int FEHLER_BELEG_DARF_NICHT_INS_NAECHSTE_GJ_DATIERT_WERDEN = 67;
	public final static int FEHLER_BELEG_DARF_NICHT_INS_VORLETZTE_GJ_DATIERT_WERDEN = 68;
	public final static int FEHLER_BELEG_DARF_NICHT_IN_EIN_ANDERES_GJ_UMDATIERT_WERDEN = 69;
	public final static int FEHLER_BUILD_CLIENT = 70;
	public final static int FEHLER_DRUCKEN_POSITIONSART = 71;
	public final static int FEHLER_UNGUELTIGE_EMAILADRESSE = 72;
	public final static int FEHLER_UNGUELTIGE_FAXNUMMER = 73;
	public final static int FEHLER_ENDSUMME_EXISTIERT = 74;
	public final static int FEHLER_ENDSUMME_NICHTVORPREISBEHAFTETERPOSITION = 75;
	public final static int FEHLER_UNGUELTIGE_ZEITEINGABE = 76;
	public final static int FEHLER_UNGUELTIGE_ZAHLENEINGABE = 77;
	public final static int FEHLER_WEB_SERVICE = 78;
	public final static int FEHLER_GET_AS_XML = 79;
	public final static int FEHLER_BENUTZER_DARF_SICH_IN_DIESER_SPRACHE_NICHT_ANMELDEN = 80;
	public final static int FEHLER_COPY_PASTE = 81;
	public final static int FEHLER_NOCLASSDEFFOUNDERROR = 82;
	public final static int FEHLER_THECLIENT_WURDE_GELOESCHT = 83;
	public final static int FEHLER_BELEG_HAT_KEINE_POSITIONEN = 84;
	public static final int FEHLER_BELEG_IST_BEREITS_AKTIVIERT = 85;
	public final static int FEHLER_SPRACHE_NICHT_AKTIV = 86;
	public final static int FEHLER_NAMING_EXCEPTION = 87;
	public final static int FEHLER_NO_UNIQUE_RESULT = 88;
	public final static int FEHLER_FLR_DRUCK_VORLAGE_UNVOLLSTAENDIG = 89;
	public final static int FEHLER_BEIM_ANLEGEN_ENTITY_EXISTS = 90;
	public final static int FEHLER_TRANSACTION_TIMEOUT = 91;
	public final static int FEHLER_ABBUCHUNG_OHNE_URSPRUNGSEINTRAG_NICHT_MOEGLICH = 92;
	public final static int FEHLER_JCR_KNOTEN_EXISTIERT_BEREITS = 93;
	public final static int FEHLER_JCR_DATEI_KONNTE_NICHT_GELESEN_WERDEN = 94;
	public final static int FEHLER_JCR_KNOTEN_NICHT_GESPEICHERT = 95;
	public final static int FEHLER_SQL_EXCEPTION_MIT_INFO = 96;
	public final static int FEHLER_UNGUELTIGE_INSTALLATION = 97;
	public static final int FEHLER_JCR_KEINE_AUFTRAEGE_ZU_KOPIEREN = 98;
	public static final int FEHLER_MAXIMALE_BENUTZERANZAHL_UEBERSCHRITTEN = 99;
	public static final int FEHLER_STEUERSATZ_INNERHALB_UNTERPOSITIONEN_UNGLEICH = 100;
	public static final int FEHLER_KUNDE_STANDARDPREISLISTE_HAT_FALSCHE_WAEHRUNG = 101;
	public static final int FEHLER_KEINE_BERECHTIGUNG_BELEG_AKTIVIEREN = 102;
	public static final int FEHLER_LIZENZ_ABGELAUFEN = 103;
	public static final int FEHLER_BEIM_KENNWORT_VERSCHLUESSELN = 104;
	public static final int FEHLER_BEIM_KENNWORT_ENTSCHLUESSELN = 105;
	public final static int FEHLER_ISORT_DUPLICATE_UNIQUE = 106;
	public static final int FEHLER_PATERNOSTERKOMMUNIKATIONSFEHLER = 107;
	public final static int FEHLER_IM_WECHSELKURS_KEINE_MANDANTENWAEHRUNG_ENTHALTEN = 108;
	public final static int FEHLER_POSITION_VERTAUSCHEN_ZWISCHENSUMME_VONBIS = 109;
	public final static int FEHLER_POSITION_VERTAUSCHEN_ZWISCHENSUMME_IN_SICH_SELBST = 110;
	public final static int FEHLER_POSITION_VERTAUSCHEN_STATUS = 111;
	public final static int FEHLER_POSITION_VERTAUSCHEN_KEINE_POS_VORHANDEN = 112;
	public final static int FEHLER_INVALID_REPORT_URL = 113;


	public final static int FEHLER_KEYSTORE             = 114 ;
	public final static int FEHLER_KEYSTORE_MANAGMENT   = 115 ;
	public final static int FEHLER_KEYSTORE_RECOVER     = 116 ;
	public final static int FEHLER_KEYSTORE_ALGORITHMEN = 117 ;
	public final static int FEHLER_KEYSTORE_CERTIFICATE = 118 ;

	public final static int FEHLER_HTTP_POST_IO         = 119 ;

	public final static int FEHLER_DOKUMENTENABLAGE_OFFLINE = 120 ;
	public final static int FEHLER_BEIM_AKTIVIEREN_BELEG_WURDE_GEAENDERT = 121 ;

	/**
	 * Eine Script-Datei wurde im Server-Verzeichnis nicht gefunden</br>
	 */
	//TODO: Vorerst hier in diesem Bereich. Sollten noch mehrere Script-Fehler dazukommen, dann eigenen Bereich machen
	public final static int FEHLER_SCRIPT_NICHT_GEFUNDEN = 122 ;
	public final static int FEHLER_SCRIPT_NICHT_AUSFUEHRBAR = 130 ;

	/**
	 * Die Zwischensummenposition ist unvollst&auml;ndig
	 */
	public final static int FEHLER_POSITION_ZWISCHENSUMME_UNVOLLSTAENDIG = 123 ;
	public final static int FEHLER_FLRDRUCK_SPALTE_NICHT_VORHANDEN = 124 ;
	public final static int FEHLER_PJ18612_BENUTZER_MUSS_AN_MANDANT_002_ANGEMELDET_SEIN = 125;

	/**
	 * Die zu ver&auml;ndernden Daten wurden zwischenzeitlich modifiziert
	 */
	public final static int FEHLER_DATEN_MODIFIZIERT_UPDATE = 126 ;
	public final static int FEHLER_DATEN_MODIFIZIERT_REMOVE = 127 ;

	/**
	 * Der Root-Node existiert nicht, bzw. es kann nicht auf ihn zugegriffen werden
	 */
	public final static int FEHLER_JCR_ROOT_EXISTIERT_NICHT = 128 ;

	public final static int FEHLER_SORTIER_LISTE_ENTHAELT_NULL = 129;

	// 501 bis 999 Client Fehler
	// ****************************************************
	public final static int FEHLER_DLG_DONE_DO_NOTHING = 501;
	public final static int FEHLER_FEHLER_BEIM_DRUCKEN = 502;

	public final static int FEHLER_TELEFON_WAHLVORGANG = 503;

	// 1000-1999: Auftrag
	// ***********************************************************
	static public final int FEHLER_KEIN_EIGENTUMSVORBEHALT_DEFINIERT = 1000;
	static public final int FEHLER_KEINE_LIEFERBEDINGUNGEN_DEFINIERT = 1001;
	static public final int FEHLER_ARTIKELAENDERUNG_BEI_RAHMENPOSUPDATE_NICHT_ERLAUBT = 1002;
	static public final int FEHLER_MENGENAENDERUNG_UNTER_ABGERUFENE_MENGE_NICHT_ERLAUBT = 1003;
	static public final int FEHLER_ABRUFAUFTRAG_KANN_NICHT_MEHR_VERAENDERT_WERDEN = 1004;
	static public final int FEHLER_RAHMENAUFTRAG_IST_IM_STATUS_ANGELEGT = 1005;
	static public final int FEHLER_ALLE_LOSE_BERUECKSICHTIGEN_UND_SAMMELLIEFERSCHIEN_MEHRERE_AUFTRAEGE = 1006;

	// 2000-2499: Artikel + Stueckliste
	// ***********************************************************
	static public final int ARTIKEL_WECHSEL_LAGERBEWIRTSCHAFTET_NICHT_MOEGLICH = 2000;
	static public final int FEHLER_ARTIKEL_ZEICHEN_IN_ARTIKELNUMMER_NICHT_ERLAUBT = 2001;
	static public final int LAGER_SERIENNUMMER_SCHON_VORHANDEN = 2002;
	static public final int LAGER_KEIN_LAGER_NICHT_ANGELEGT = 2003;
	static public final int FEHLER_ARTIKEL_ARTIKELNUMMER_ZU_LANG = 2004;
	static public final int FEHLER_ARTIKEL_ARTIKELNUMMER_ZU_KURZ = 2005;
	static public final int FEHLER_SONDERZEICHEN_AN_FALSCHER_STELLE = 2006;
	static public final int FEHLER_ARTIKEL_IST_KEINEM_LAGER_ZUGEORDNET = 2007;
	static public final int FEHLER_MENGE_FUER_SERIENNUMMERNBUCHUNG_MUSS_EINS_SEIN = 2008;
	static public final int FEHLER_LAGERBEWEGUNGSURSPRUNG_NICHT_AUFFINDBAR = 2009;
	public final static int FEHLER_ARTIKEL_HAT_KEINEN_EINZELVERKAUFSPREIS_HINTERLEGT = 2010;
	public final static int FEHLER_LAGER_HAUPTLAGERDESMANDANTEN_NICHT_ANGELEGT = 2011;
	public final static int FEHLER_DARF_NICHT_AUF_KEIN_LAGER_BUCHEN = 2012;
	public final static int FEHLER_ARTIKEL_IST_NICHT_LAGERBEWIRTSCHAFTET = 2013;
	public final static int FEHLER_VKPF_MAXIMALZEHNPREISLISTEN = 2014;
	static public final int ARTIKEL_WECHSEL_SERIENNUMMERNTRAGEND_NICHT_MOEGLICH = 2015;
	static public final int ARTIKEL_WECHSEL_CHARGENNUMMERNTRAGEND_NICHT_MOEGLICH = 2016;
	static public final int ARTIKEL_SERIENNUMMER_WURDE_SCHON_VERWENDET = 2017;
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
	static public final int FEHLER_SERIENCHARGENNUMMER_ENTHAELT_NICHT_ERLAUBTE_ZEICHEN = 2037;
	static public final int FEHLER_ARTIKEL_ERSATZARTIKEL_DEADLOCK = 2038;
	static public final int FEHLER_HERSTELLERKUERZEL_OVERFLOW = 2039;
	static public final int FEHLER_ZUVIELE_LAGERORTE = 2040;
	public static final int FEHLER_LAGER_WERTGUTSCHRIFT_BEI_DIESEM_MANDANTEN_SCHON_VORHANDEN = 2041;
	public final static int FEHLER_LAGER_WERTGUTSCHRIFTDESMANDANTEN_NICHT_ANGELEGT = 2042;
	public final static int FEHLER_STUECKLISTENART_ARTIKELSET_IN_STUECKLISTENPOSITION_NICHT_MOEGLICH = 2043;
	public final static int FEHLER_STUECKLISTENART_ARTIKELSET_BZW_HILFSSTUECKLISTE_DARF_KEINE_STUECKLISTE_ENTHALTEN = 2044;
	public final static int FEHLER_ARTIKELSET_KANN_NICHT_VERSCHOBEN_WERDEN = 2045;
	public final static int FEHLER_ARTIKEL_KANN_NICHT_IN_ARTIKELSET_VERSCHOBEN_WERDEN = 2046;
	public final static int FEHLER_MEHRERE_LAGERPLAETZE_PRO_LAGER_NICHT_MOEGLICH = 2047;
	public final static int FEHLER_INVENTUR_MENGE_ZU_GROSS = 2048;
	public final static int FEHLER_CHARGENNUMMER_ZU_KURZ = 2049;
	public final static int FEHLER_KEINE_BERECHTIUNG_ZUM_BUCHEN_AUF_DIESEM_LAGER = 2050;
	public final static int FEHLER_DEFAULT_ARBEITSZEITARTIKEL_NICHT_DEFINIERT = 2051;
	public final static int FEHLER_SERIENNUMMER_ZU_KURZ = 2052;
	public final static int FEHLER_SERIENNUMMER_ZU_LANG = 2053;
	public final static int FEHLER_SERIENNUMMERNGENERATOR_UNGUELTIGE_ZEICHEN = 2054;
	static public final int FEHLER_SERIENNUMMER_MUSS_UEBER_ALLE_ARTIKEL_EINDEUTIG_SEIN = 2055;
	static public final int FEHLER_GERAETESNR_BEREITS_ZUGEBUCHT = 2056;
	static public final int FEHLER_POSITIONSMENGE_EINES_SNR_ARTIKELS_MUSS_1_SEIN_WENN_GERAETESNR = 2057;
	static public final int FEHLER_VERKAUFSEAN_BEREITS_VORHANDEN = 2058;
	static public final int FEHLER_VERPACKUNGSEAN_BEREITS_VORHANDEN = 2059;
	static public final int FEHLER_RUNDUNGSARTIKEL_NICHT_DEFINIERT = 2060;
	static public final int FEHLER_IMPORT_NX_STKL_LAENGE_UNTERSCHIEDLICH = 2061;
	static public final int FEHLER_IMPORT_NX_STKL_UNTERSCHIEDLICH = 2062;
	static public final int FEHLER_IMPORT_NORMTEIL_MUSS_VORHANDEN_SEIN = 2063;
	static public final int FEHLER_IMPORT_DATENTYP_SPALTE_MENGE_FALSCH = 2064;
	static public final int FEHLER_IMPORT_DATENTYP_SPALTE_GEWICHT_FALSCH = 2065;
	static public final int FEHLER_IMPORT_MATERIAL_NICHT_VORHANDEN = 2066;
	public static final int FEHLER_LAGER_WARENEINGANG_BEI_DIESEM_MANDANTEN_SCHON_VORHANDEN = 2067;
	public static final int FEHLER_ARTIKELLIEFERANT_PREIS_IST_NULL = 2068;
	public static final int FEHLER_INVENTUR_IMPORT_NUR_MIT_LAGER_MOEGLICH = 2069;
	public static final int FEHLER_UNGUELTIGE_WERTE_CSV_IMPORT = 2070;
	static public final int FEHLER_IMPORT_DIMENSIONEN_UND_STKLPOS_EINHEIT_FALSCH = 2071;
	static public final int FEHLER_IMPORT_STUECKLISTENIMPORT_ALLGEMEIN = 2072;
	static public final int FEHLER_EINZELPREIS_NUR_LOESCHBAR_WENN_KEINE_STAFFELN = 2073;
	static public final int FEHLER_EINHEIT_C_NR_VPE_IN_ARTIKELLIEFERANT_VORHANDEN = 2074;
	static public final int FEHLER_SERIENNUMMER_ENTHAELT_NICHT_NUMERISCHE_ZEICHEN = 2075;
	public final static int FEHLER_CHARGENNUMMERNGENERATOR_UNGUELTIGE_ZEICHEN = 2076;
	public final static int FEHLER_CHARGENNUMMER_NICHT_NUMERISCH = 2077;

	public final static int FEHLER_KEINE_STUECKLISTEN_FUER_PARTNERID = 2078 ;
	public final static int FEHLER_KUNDENSTUECKLISTEMANDANT_NICHT_EINDEUTIG = 2079 ;

	// 2500-2999: Personal+Zeiterfassung+Zutritt
	// ********************************************
	public final static int FEHLER_PERSONAL_FEHLER_BEI_EINTRITTSDATUM = 2500;
	public final static int FEHLER_PERSONAL_URLAUBSBERECHNUNG_ZU_DATUM_KEINE_SOLLZEIT_DEFINIERT = 2501;
	public final static int FEHLER_ZEITERFASSUNG_MEHRFACHES_KOMMT = 2502;
	public final static int FEHLER_ZEITERFASSUNG_MEHRFACHES_GEHT = 2503;
	public final static int FEHLER_ZEITERFASSUNG_GEHT_OHNE_KOMMT = 2504;
	public final static int FEHLER_ZEITERFASSUNG_GEHT_FEHLT = 2505;
	public final static int FEHLER_ZEITERFASSUNG_SONDERTAETIGKEIT_MUSS_BEENDET_WERDEN = 2506;
	public final static int FEHLER_ZEITERFASSUNG_TAETIGKEIT_VOR_KOMMT = 2507;
	public final static int FEHLER_GEHT_VOR_ENDE = 2508;
	public final static int FEHLER_RELATIVE_BUCHUNG_OHNE_KOMMT = 2509;
	public final static int FEHLER_KEIN_STOP_OHNE_MASCHINE = 2510;
	public final static int FEHLER_AUSWEISNUMMER_ZESTIFT = 2511;
	public final static int FEHLER_ZUTRITTSOBJEKT_VERWENDUNGSUEBERSCHREITUNG = 2512;
	public final static int FEHLER_PERSONAL_DUPLICATE_AUSWEIS = 2513;
	public final static int FEHLER_IN_ZEITDATEN = 2514;
	public final static int FEHLER_RELATIVES_AENDERN_MIT_SONDERTAETIGKEITEN_NICHT_MOEGLICH = 2515;
	public final static int FEHLER_RELATIVES_AENDERN_ZUWENIG_ZEIT = 2516;
	public final static int FEHLER_CUD_TAETIGKEIT_TELEFON_NICHT_ERLAUBT = 2517;
	public final static int FEHLER_ZEITERFASSUNG_FEHLER_ZEITDATEN = 2518;
	public final static int FEHLER_ZEITERFASSUNG_RELATIVE_NICHT_MOEGLICH = 2519;
	public final static int FEHLER_RELATIVE_BUCHUNG_ENDE_FEHLT = 2520;
	public final static int FEHLER_RELATIVE_BUCHUNG_GESAMTE_ZEIT_VERBUCHT = 2521;
	public final static int FEHLER_LOSGUTSCHLECHT_VORHANDEN = 2522;
	public final static int FEHLER_IN_REISEZEITEN = 2523;
	public final static int FEHLER_ZEITBUCHUNGEN_VORHANDEN = 2524;
	static public final int FEHLER_PERSONAL_ZEICHEN_IN_PERSONALNUMMER_NICHT_ERLAUBT = 2525;
	public final static int FEHLER_BUCHUNG_ZWISCHEN_VON_BIS = 2526;
	public final static int FEHLER_BUCHUNG_EINFUEGEN_ZWISCHEN_VON_BIS_NICHT_ERLAUBT = 2527;
	public final static int FEHLER_SONDERZEITENIMPORT_DOPPELTER_EINTRAG = 2528;
	public final static int FEHLER_ZEITEN_BEREITS_ABGESCHLOSSEN = 2529;

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


	// Zahlungsart <> Gutschrift, aber Gutschrift-Daten vorhanden
	public static final int FEHLER_GUTSCHRIFTZAHLUNG_OHNE_GUTSCHRIFTART = 3042 ;
	
	// Definition: Bei ReverseCharge muessen alle Positionen den gleichen Mwstsatz haben
	public static final int FEHLER_RECHNUNG_UNTERSCHIEDLICHE_MWSTSAETZE_BEI_RC = 3043 ;
	

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

	// RZL spezifisch
	public static final int FEHLER_FINANZ_EXPORT_RZL_KONTONUMMER_MAXIMAL_6_STELLEN = 4830;
	// ZV Export ab 4850
	public static final int FEHLER_FINANZ_ZVEXPORT_LF_HAT_KEINE_BANKVERBINDUNG = 4850;
	public static final int FEHLER_FINANZ_ZVEXPORT_BANK_HAT_KEINEN_ORT = 4851;
	// Intrastat Export ab 4860
	public static final int FEHLER_FINANZ_INTRASTAT_KEINE_WVK_NR = 4860;
	// BMD spezifisch ab 4880
	public static final int FEHLER_FINANZ_EXPORT_BMD_KONTONUMMER_MAXIMAL_9_STELLEN = 4880;

	// mahnwesen ab 4900
	public final static int FEHLER_FINANZ_MAHNSTUFEN_1_2_3_DUERFEN_NICHT_GELOESCHT_WERDEN = 4900;
	public static final int FEHLER_FINANZ_KEINE_MAHNTEXTE_EINGETRAGEN = 4901;
	public static final int FEHLER_FINANZ_KEINE_MAHNSTUFEN_EINGETRAGEN = 4902;
	public static final int FEHLER_FINANZ_MAHNLAUF_WURDE_SCHON_UEBERNOMMEN = 4903;
	public final static int FEHLER_FINANZ_MAHNTEXTE_1_2_3_DUERFEN_NICHT_GELOESCHT_WERDEN = 4904;

	// 5000-5999: System
	// **********************************************************
	public final static int FEHLER_SYSTEM_VERSANDAUFTRAG_WURDE_BEREITS_VERSANDT = 5000;
	public final static int FEHLER_SYSTEM_VERSANDAUFTRAG_KANN_NICHT_STORNIERT_WERDEN = 5001;
	public final static int FEHLER_SYSTEM_STUECK_KANN_NICHT_GELOESCHT_WERDEN = 5002;
	public final static int FEHLER_SYSTEM_SEKUNDE_KANN_NICHT_GELOESCHT_WERDEN = 5003;
	public final static int FEHLER_SYSTEM_STUNE_KANN_NICHT_GELOESCHT_WERDEN = 5004;
	public final static int FEHLER_SYSTEM_MINUTE_KANN_NICHT_GELOESCHT_WERDEN = 5005;
	public final static int FEHLER_SYSTEM_EINHEITKONVERTIERUNG_SCHON_VORHANDEN = 5006;
	public final static int FEHLER_SYSTEM_EINHEITKONVERTIERUNG_GLEICHE_EINHEITEN = 5007;
	public final static int FEHLER_SYSTEM_MWSTSATZ_IST_NULL = 5008;
	public final static int FEHLER_SYSTEM_KOSTENSTELLE_IN_VERWENDUNG = 5009;
	public final static int FEHLER_SYSTEM_JMS = 5010;

	// 6000-6999: Partner (Kunde, Sachbearbeiter, Ansprechpartner, Lieferant)
	// *****
	public final static int FEHLER_PARTNER_LIEFERART = 6000;
	public final static int FEHLER_PARTNER_VKPREISFINDUNGPREISLISTENNAME = 6001;
	public final static int FEHLER_PARTNER_SPEDITEUR = 6002;
	public final static int FEHLER_PARTNER_ZAHLUNGSZIEL = 6003;
	public final static int FEHLER_PARTNER_MWST = 6004;
	public final static int FEHLER_KEIN_PARTNER_GEWAEHLT = 6006;
	public final static int WARNUNG_KTO_BESETZT = 6007;
	public final static int WARNUNG_KUNDEN_UID_NUMMER_NICHT_HINTERLEGT = 6008;
	public final static int FEHLER_PARTNER_KOMM_AENDERN_NUR_EIGENER_MANDANT = 6009;
	public final static int FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH = 6010;
	public final static int FEHLER_PARTNER_LKZ_AENDERUNG_NICHT_MOEGLICH = 6011;
	public final static int FEHLER_LIEFERGUPPE_NICHT_ANGELEGT_IMPORT_ABGEBROCHEN = 6012;
	public final static int FEHLER_KUNDE_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_DEBITOREN = 6013;
	public final static int FEHLER_LIEFERANT_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_KREDITOREN = 6014;

	public final static int FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_MWST = 6015;
	public final static int FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_LKZ = 6016;
	public final static int FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_ABW_UST = 6017;
	public final static int FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_UID = 6018;
	public final static int FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_BANKVERBINDUNG_IN_FIBU_VORHANDEN = 6019;

	public final static int FEHLER_PARTNERART_DARF_NICHT_GELOESCHT_GEAENDERT_WERDEN = 6020;


	/**
	 * @deprecated use {@link #FEHLER_FALSCHER_MANDANT}
	 */
	public final static int FEHLER_KUNDE_IID_IN_MANDANT_NICHT_VORHANDEN = 6020;

	public final static int FEHLER_ANSPRECHPARTNER_EMAIL_NICHT_DEFINIERT = 6021;
	public final static int FEHLER_ANSPRECHPARTNER_EMAIL_NICHT_EINDEUTIG = 6022;


	// 7000-7999: Lieferschein
	// ****************************************************
	public final static int FEHLER_LIEFERSCHEIN_KEINEOFFENENLIEFERSCHEINE = 7000;
	public final static int FEHLER_LIEFERSCHEIN_KEINEANGELEGTENLIEFERSCHEINE = 7001;
	public final static int FEHLER_LIEFERSCHEIN_TEXTINKONZERNDATENSPRACHENICHTHINTERLEGT = 7002;
	public final static int FEHLER_LIEFERSCHEIN_DIEZUGEHOERIGELIEFERSCHEINPOSITIONWURDEGELOESCHT = 7003;
	public final static int FEHLER_LIEFERSCHEIN_CHN_SNR_ZU_WENIG_AUF_LAGER = 7004;
	public final static int FEHLER_LIEFERSCHEIN_AVISO_BEREITS_DURCHGEFUEHRT = 7005 ;
	public final static int FEHLER_LIEFERSCHEIN_VERSANDWEG_NICHT_UNTERSTUETZT = 7006 ;
	public final static int FEHLER_LIEFERSCHEIN_VERSANDWEG_IM_PARTNER_NICHT_DEFINIERT = 7007 ;
	public final static int FEHLER_LIEFERSCHEIN_VERSANDWEG_PARTNER_KUNDENNUMMER_FEHLT = 7008 ;
	public final static int FEHLER_LIEFERSCHEIN_ANDERN_MANDANT_NACHFUELLEN_MANDANT_KEIN_KUNDE = 7009 ;
	public final static int FEHLER_LIEFERSCHEIN_ENTHAELT_VERKETTETE_LIEFERSCHEINE = 7010 ;
	public final static int FEHLER_LIEFERSCHEIN_IST_VERKETTET = 7011 ;
	public final static int FEHLER_LIEFERSCHEIN_IST_BEREITS_VERKETTET = 7012 ;
	public final static int FEHLER_LIEFERSCHEIN_HAT_KEINEN_AUFTRAGSBEZUG = 7013 ;


	// 8000-8999: Drucken
	// *********************************************************
	static public final int FEHLER_DRUCKEN_REPORT_NICHT_GEFUNDEN = 8000;
	static public final int FEHLER_DRUCKEN_FALSCHE_VERSION = 8001;
	static public final int FEHLER_DRUCKEN_KEINE_DATEN_ZU_DRUCKEN = 8002;
	static public final int FEHLER_DRUCKEN_FEHLER_IM_REPORT = 8003;
	static public final int FEHLER_DRUCKEN_FILE_NOT_FOUND = 8004;
	public final static int FEHLER_DRUCKEN_KEINE_DRUCKER_INSTALLIERT = 8005;

	// 9000-9999: Eingangsrechnung
	// ************************************************
	public static final int FEHLER_WERT_UNTER_AUFTRAGSZUORDNUNG = 9000;
	public static final int FEHLER_LIEFERANTENRECHNUNGSNUMMER_DOPPELT = 9001;
	public static final int FEHLER_KONTIERUNG_ZUGEORDNET = 9002;
	public static final int FEHLER_ZUSATZKOSTEN_FEHLER_WIEDERHOLUNGERLEDIGT = 9003;
	public static final int FEHLER_ER_ANZAHLUNGEN_REVERSE_CHARGE_ABWEICHEND = 9004;

	// 10000-10999: Bestellung
	// ****************************************************
	public final static int FEHLER_BESTELLVORSCHLAG_IST_GESPERRT = 10000;
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

	// Angebot 11000-11999
	public final static int FEHLER_ANGEBOT_NETTOWERTKANNNICHTBERECHNETWERDEN = 11000;

	// Fertigung 12000-12999
	public final static int FEHLER_FERTIGUNG_DARF_FUER_MATERIALLISTE_NICHT_DURCHGEFUEHRT_WERDEN = 12000;
	public final static int FEHLER_FERTIGUNG_STUECKLISTE_MATERIAL_WURDE_GEAENDERT = 12001;
	public final static int FEHLER_FERTIGUNG_STUECKLISTE_ARBEITSPLAN_WURDE_GEAENDERT = 12002;
	public final static int FEHLER_FERTIGUNG_LAGERENTNAHME_DARF_NICHT_GELOESCHT_WERDEN = 12003;
	public final static int FEHLER_FERTIGUNG_MATERIAL_AUS_STUECKLISTE_DARF_NICHT_GELOESCHT_WERDEN = 12004;
	public final static int FEHLER_FERTIGUNG_POSITION_AUS_ARBEITSPLAN_DARF_NICHT_GELOESCHT_WERDEN = 12005;
	public final static int FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT = 12006;
	public final static int FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT = 12007;
	public final static int FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_AUSGEGEBEN = 12008;
	public final static int FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN = 12009;
	public final static int FEHLER_FERTIGUNG_DAS_LOS_IST_GESTOPPT = 12010;
	public final static int FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_TEILERLEDIGT = 12011;
	public final static int FEHLER_ABLIEFERN_PER_AUFTRAGSNUMMER_NICHT_MOEGLICH = 12012;
	public final static int FEHLER_ABBUCHUNG_SNRCHNR_ABGEBROCHEN = 12013;
	public final static int FEHLER_FERTIGUNG_AENDERUNG_LOGROESSE_ZUVIELEABLIEFERUNGEN = 12014;
	public final static int FEHLER_FERTIGUNG_SOLLSATZGROESSE_UNTERSCHRITTEN = 12015;
	public final static int FEHLER_FERTIGUNG_UPDATE_LOSABLIEFERUNG_FEHLER_MENGE = 12016;
	public final static int FEHLER_FERTIGUNG_INTERNE_BESTELLUNG_ZU_VIELE_UNTERSTUECKLISTEN = 12017;
	public final static int FEHLER_FERTIGUNG_ES_IST_NOCH_MATERIAL_AUSGEGEBEN = 12018;
	public final static int FEHLER_FERTIGUNG_LOS_OHNE_KUNDE = 12019;
	public final static int FEHLER_FERTIGUNG_AUSGABE_ES_WUERDEN_FEHLMENGEN_ENTSTEHEN = 12020;
	public final static int FEHLER_FERTIGUNG_LOSNUMMER_NACH_BEREICH_UEBERLAUF = 12021;
	public final static int FEHLER_FERTIGUNG_AUF_DEM_LOS_IST_MATERIAL_AUSGEGEBEN = 12022;
	public final static int FEHLER_FERTIGUNG_FERTIGUNGSGRUPPE_SOFORTVERBRAUCH_NICHT_VORHANDEN = 12023;
	public final static int FEHLER_FERTIGUNG_MATERIAL_VOLLSTAENDIG = 12024;
	public final static int FEHLER_FERTIGUNG_HILFSSTUECKLISTE_DARF_KEINE_SOLLPOSITION_SEIN = 12025;



	// Fertigung 13000-13999
	public final static int FEHLER_BES_SUBSCRIPTION_FEHLGESCHLAGEN = 13000;
	public final static int FEHLER_BES_SUBSCRIPTION_ABBRUCH_FEHLGESCHLAGEN = 13001;

	// Inserat 14000-14999
	public final static int FEHLER_INSERAT_EIN_KUNDE_MUSS_VORHANDEN_SEIN = 14000;
	// Projekt 15000-15999
	public final static int FEHLER_PROJEKT_DARF_NICHT_STORNIERT_WERDEN_ZEITEN_VORHANDEN = 15000;
	public final static int FEHLER_ZEITBUCHUNG_STORNIERTES_PROJEKT_NICHT_MOEGLICH = 15001;
	public final static int FEHLER_ZEITBUCHUNG_INTERN_ERLEDIGTES_PROJEKT_NICHT_MOEGLICH = 15002;
	public final static int FEHLER_ZEITBUCHUNG_ANGELEGTES_LOS_NICHT_MOEGLICH = 15003;
	public final static int FEHLER_ZEITBUCHUNG_ERLEDIGTES_LOS_NICHT_MOEGLICH = 15004;
	public final static int FEHLER_ZEITBUCHUNG_ERLEDIGTER_AUFTRAG_NICHT_MOEGLICH = 15005;
	public final static int FEHLER_ZEITBUCHUNG_STORNIERTER_AUFTRAG_NICHT_MOEGLICH = 15006;
	public final static int FEHLER_ZEITBUCHUNG_AUF_FERTIGEN_ARBEITSGANG_NICHT_MOEGLICH = 15007;
	public final static int FEHLER_ZEITBUCHUNG_GESTOPPTES_LOS_NICHT_MOEGLICH = 15008;
	public final static int FEHLER_ZEITBUCHUNG_STORNIERTES_LOS_NICHT_MOEGLICH = 15009;
	public final static int FEHLER_ZEITBUCHUNG_ERLEDIGTES_PROJEKT_NICHT_MOEGLICH = 15010;

	public final static int FEHLER_ZEITBUCHUNG_STORNIERTES_ANGEBOT_NICHT_MOEGLICH = 15011;
	public final static int FEHLER_ZEITBUCHUNG_ERLEDIGTES_ANGEBOT_NICHT_MOEGLICH = 15012;

	// public final static int
	// FEHLER_FERTIGUNG_KAPAZITAETSVORSCHAU_KEINE_ARTIKELGRUPPEN = 12012;
	// public final static int
	// FEHLER_FERTIGUNG_KAPAZITAETSVORSCHAU_KEINE_MASCHINENGRUPPEN = 12013;

	private int code = NO_VALUE;
	private ArrayList<Object> alInfoForTheClient = null;

	public EJBExceptionLP() {
		this(FEHLER, new Exception("undefined")) ;
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
	 * @param codeI
	 *            fuer den programmierer.
	 * @param alInfoForTheClientI
	 *            Exception die aufgetreten ist.
	 * @param eI
	 *            Throwable
	 */
	public EJBExceptionLP(int codeI, ArrayList<Object> alInfoForTheClientI,
			Exception eI) {
		super(eI);
		code = codeI;
		alInfoForTheClient = alInfoForTheClientI;
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
		 * Umstrukturierung, weil in den Tests die mit einer EJB Exception
		 * fehlschlagen es hilfreich ist auch den Fehlercode (DUPLICATE usw.) zu
		 * sehen und nicht nur die Entity.
		 */
		code = (eI instanceof EJBExceptionLP) ? ((EJBExceptionLP) eI).getCode()
				: iCodeI;
		if (eI == null) {
			eI = new Exception("", this);
		}
		doLog("Fehlercode: " + code, eI);
	}

	public EJBExceptionLP(int iCodeI, String sTextForException) {
		this(iCodeI, new Exception(sTextForException));
	}

	public EJBExceptionLP(int iCodeI, String sTextForException,
			Object... alInfoForTheClient) {
		this(iCodeI, new ArrayList<Object>(Arrays.asList(alInfoForTheClient)),
				new Exception(sTextForException));
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setAlInfoForTheClient(ArrayList<Object> alInfoForTheClient) {
		this.alInfoForTheClient = alInfoForTheClient;
	}

	public int getCode() {
		return code;
	}

	public ArrayList<Object> getAlInfoForTheClient() {
		return alInfoForTheClient;
	}

	private void doLog(String errorI, Exception eI) {
		if (enableLog) {
			LPLogService.getInstance().getLogger(EJBExceptionLP.class)
					.error(errorI, eI);
		}
	}
}
