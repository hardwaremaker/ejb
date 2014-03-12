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
package com.lp.server.util.fastlanereader.service.query;

import java.io.Serializable;
import java.util.ArrayList;

import com.lp.server.system.service.LocaleFac;

/**
 * This class contains all information that is needed to perform a query on the
 * FastLaneReader.
 * 
 * @author werner
 */
public class QueryParameters implements Serializable {

	// ==========================================================================
	// =
	// Anforderungsspec an QueryParameters:
	// --------------------------------------------------------------------------
	// -
	// - Filterkriterien:
	//
	// Alle Tabellenattribute sind enthalten, ueber ein flag
	// kann man einstellen, ob das Attribut tatsaechlich in den Kriterien
	// uebergeben wird.
	//
	// Format eines Filterkriteriums : Attributename OPERATOR_XX Value
	//
	// Es gilt: Es gibt Bloecke von Filterkriterien. Innerhalb eines
	// Blocks gibt einen Boolschen Operator (AND oder OR). Momentan gibt es
	// einen Block.
	// --------------------------------------------------------------------------
	// -
	// - Sortierkritium:
	//
	// Alle Tabellenattribute sind enthalten, ueber ein flag
	// kann man einstellen, ob das Attribut tatsaechlich in den Kriterien
	// uebergeben wird.
	//
	// Format eines Sortierkritiums : Attributname SORT_XX
	// ==========================================================================
	// =

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * the use case id for the use case Auftrag
	 */

	// Personal: 0-499
	public static final int UC_ID_PERSONAL = 1;
	public static final int UC_ID_KOLLEKTIV = 2;
	public static final int UC_ID_BERUF = 3;
	public static final int UC_ID_PENDLERPAUSCHALE = 4;
	public static final int UC_ID_LOHNGRUPPE = 5;
	public static final int UC_ID_RELIGION = 6;
	public static final int UC_ID_PERSONALANGEHOERIGE = 7;
	public static final int UC_ID_EINTRITTAUSTRITT = 8;
	public static final int UC_ID_PERSONALZEITEN = 9;
	public static final int UC_ID_ZEITMODELL = 10;
	public static final int UC_ID_ZEITMODELLTAG = 11;
	public static final int UC_ID_PERSONALZEITMODELL = 12;
	public static final int UC_ID_BETRIEBSKALENDER = 13;
	public static final int UC_ID_SONDERTAETIGKEIT = 14;
	public static final int UC_ID_ZEITDATEN = 15;
	public static final int UC_ID_URLAUBSANSPRUCH = 16;
	public static final int UC_ID_SONDERZEITEN = 17;
	public static final int UC_ID_STUNDENABRECHNUNG = 18;
	public static final int UC_ID_GLEITZEITSALDO = 19;
	public static final int UC_ID_PERSONALGEHALT = 20;
	public static final int UC_ID_TAGESART = 21;
	public static final int UC_ID_ZEITMODELLTAGPAUSE = 22;
	public static final int UC_ID_ZULAGE = 23;
	public static final int UC_ID_ARTIKELZULAGE = 24;
	public static final int UC_ID_MASCHINE = 25;
	public static final int UC_ID_ZEITSTIFT = 26;
	public static final int UC_ID_MASCHINENKOSTEN = 27;
	public static final int UC_ID_ZUTRITTSKLASSE = 28;
	public static final int UC_ID_ZUTRITTSCONTROLLER = 29;
	public static final int UC_ID_ZUTRITTSMODELL = 30;
	public static final int UC_ID_ZUTRITTSMODELLTAG = 31;
	public static final int UC_ID_ZUTRITTSMODELLTAGDETAIL = 32;
	public static final int UC_ID_PERSONALZUTRITTSKLASSE = 33;
	public static final int UC_ID_ZUTRITTSOBJEKT = 34;
	public static final int UC_ID_ZUTRITTSKLASSEOBJEKT = 35;
	public static final int UC_ID_ZUTRITTONLINECHECK = 36;
	public static final int UC_ID_ZUTRITTSOBJEKTVERWENDUNG = 37;
	public static final int UC_ID_ZUTRITTSLOG = 38;
	public static final int UC_ID_ZUTRITTDAUEROFFEN = 39;
	public static final int UC_ID_MASCHINENGRUPPE = 40;
	public static final int UC_ID_PERSONALVERFUEGBARKEIT = 41;
	public static final int UC_ID_REISE = 42;
	public static final int UC_ID_PERSONALFINGER = 43;
	public static final int UC_ID_FINGERART = 44;
	public static final int UC_ID_KOLLEKTIVUESTD = 45;
	public static final int UC_ID_KOLLEKTIVUESTD50 = 46;
	public static final int UC_ID_TELEFONZEITEN = 47;
	public static final int UC_ID_DIAETEN = 48;
	public static final int UC_ID_DIAETENTAGESSATZ = 49;
	public static final int UC_ID_ZEITDATENGUTSCHLECHT = 50;
	public static final int UC_ID_PERSONALGRUPPE = 51;
	public static final int UC_ID_PERSONALGRUPPEKOSTEN = 52;
	public static final int UC_ID_MASCHINENZEITDATEN = 53;
	public static final int UC_ID_MASCHINENZEITDATENGUTSCHLECHT = 54;
	public static final int UC_ID_PERSONAL_ZEITERFASSUNG = 55;
	public static final int UC_ID_LOHNART = 56;
	public static final int UC_ID_LOHNSTUNDENART = 57;
	public static final int UC_ID_LOHNARTSTUNDENFAKTOR = 58;
	public static final int UC_ID_SCHICHTZEITMODELL = 59;
	public static final int UC_ID_BEREITSCHAFTART = 60;
	public static final int UC_ID_BEREITSCHAFTTAG = 61;
	public static final int UC_ID_BEREITSCHAFT = 62;
	public static final int UC_ID_FEIERTAG = 63;
	public static final int UC_ID_FAHRZEUG = 64;
	public static final int UC_ID_FAHRZEUGKOSTEN = 65;
	public static final int UC_ID_PROJEKTTELEFONZEITEN = 66;


	// Benutzer 500-999
	public static final int UC_ID_BENUZTER = 500;
	public static final int UC_ID_SYSTEMROLLE = 501;
	public static final int UC_ID_BENUTZERMANDANTSYSTEMROLLE = 502;
	public static final int UC_ID_ROLLERECHT = 503;
	public static final int UC_ID_RECHT = 504;
	public static final int UC_ID_NACHRICHTART = 505;
	public static final int UC_ID_THEMA = 506;
	public static final int UC_ID_THEMAROLLE = 507;
	public static final int UC_ID_NACHRICHTARCHIV = 508;
	public static final int UC_ID_LAGERROLLE = 509;
	public static final int UC_ID_FERTIGUNGSGRUPPEROLLE = 510;

	// Auftrag: 1000-1999
	public static final int UC_ID_AUFTRAG = 1000;
	public static final int UC_ID_AUFTRAGPOSITION = 1001;
	public static final int UC_ID_AUFTRAGPOSITION_ZEITERFASSUNG = 1002;
	public static final int UC_ID_AUFTRAGTEILNEHMER = 1003;
	public static final int UC_ID_SICHTLIEFERSTATUS = 1004;
	public static final int UC_ID_AUFTRAGUEBERSICHT = 1005;
	public static final int UC_ID_AUFTRAGTEXT = 1006;
	public static final int UC_ID_AUFTRAGZEITEN = 1007;
	public static final int UC_ID_AUFTRAGART = 1008;
	public static final int UC_ID_AUFTRAGPOSITIONART = 1009;
	public static final int UC_ID_AUFTRAGPOSITIONSICHTRAHMEN = 1010;
	public static final int UC_ID_PARTNERSERIENBRIEF = 1011;
	public static final int UC_ID_AUFTRAGSERIENNRN = 1012;
	public static final int UC_ID_AUFTRAGDOKUMENT = 1013;
	public static final int UC_ID_AUFTRAGBEGRUENDUNG = 1014;
	public static final int UC_ID_AUFTRAGSICHTLSRE = 1015;
	public static final int UC_ID_AUFTRAGEINGANGSRECHNUNGEN = 1016;

	// Artikel: 2000-2999
	public static final int UC_ID_ARTIKEL = 2001;
	public static final int UC_ID_ARTIKELGRUPPE = 2002;
	public static final int UC_ID_ARTIKELKLASSE = 2003;
	public static final int UC_ID_ARTIKELHERSTELLER = 2004;
	public static final int UC_ID_ARTIKELLIEFERANT = 2005;
	public static final int UC_ID_MATERIAL = 2006;
	public static final int UC_ID_LAGER = 2007;
	public static final int UC_ID_KATALOG = 2008;
	public static final int UC_ID_ARTIKELLAGER = 2009;
	public static final int UC_ID_MATERIALZUSCHLAG = 2010;
	public static final int UC_ID_ARTIKELRESERVIERUNG = 2011;
	public static final int UC_ID_HANDLAGERBEWEGUNG = 2013;
	public static final int UC_ID_PREISLISTENNAME = 2014;
	public static final int UC_ID_ARTIKELLISTE = 2015;
	public static final int UC_ID_SERIENNUMMERNCHARGENNUMMERNAUFLAGER = 2016;
	public static final int UC_ID_ARTIKELLIEFERANTSTAFFEL = 2017;
	public static final int UC_ID_LAGERPLATZ = 2018;
	public static final int UC_ID_ARTIKELLAGERPLAETZE = 2019;
	public static final int UC_ID_CHARGENAUFLAGER = 2020;
	public static final int UC_ID_ARTIKELBESTELLT = 2021;
	public static final int UC_ID_INVENTUR = 2022;
	public static final int UC_ID_INVENTURLISTE = 2023;
	public static final int UC_ID_INVENTURPROTOKOLL = 2024;
	public static final int UC_ID_FEHLMENGE = 2025;
	public static final int UC_ID_ARTIKELKOMMENTARART = 2026;
	public static final int UC_ID_ARTIKELKOMMENTAR = 2027;
	public static final int UC_ID_INVENTURSTAND = 2028;
	public static final int UC_ID_VKPFSTAFFELMENGE = 2029;
	public static final int UC_ID_FARBCODE = 2030;
	public static final int UC_ID_SPERREN = 2031;
	public static final int UC_ID_ARTIKELSPERREN = 2032;
	public static final int UC_ID_ZUGEHOERIGE = 2033;
	public static final int UC_ID_KUNDENIDENTNUMMER = 2034;
	public static final int UC_ID_EINKAUFSEAN = 2035;
	public static final int UC_ID_FEHLMENGEAUFLOESEN = 2036;
	public static final int UC_ID_PATERNOSTER = 2037;
	public static final int UC_ID_LAGER_ALLE = 2038;
	public static final int UC_ID_VERLEIH = 2039;
	public static final int UC_ID_VORSCHLAGSTEXT = 2040;
	public static final int UC_ID_SNRCHNRFUERREKLAMATION = 2041;
	public static final int UC_ID_ALLESNRCHNR = 2042;
	public static final int UC_ID_WEBSHOP = 2043;
	public static final int UC_ID_SHOPGRUPPE = 2044;
	public static final int UC_ID_SHOPGRUPPEWEBSHOP = 2045;
	public static final int UC_ID_LAGERCOCKPIT_ARTIKEL = 2046;
	public static final int UC_ID_LAGERCOCKPIT_FEHLMENGE = 2047;
	public static final int UC_ID_LAGERCOCKPIT_LOSSOLLMATERIAL = 2048;
	public static final int UC_ID_ARTIEKLSHOPGRUPPE = 2049;
	public static final int UC_ID_LAGERCOCKPIT_NICHTLAGERBEWIRTSCHAFTETE_ARTIKEL = 2050;

	// Rechnung: 3000-3999
	public static final int UC_ID_RECHNUNG = 3000;
	public static final int UC_ID_GUTSCHRIFT = 3001;
	public static final int UC_ID_RECHNUNGPOSITION = 3002;
	public static final int UC_ID_GUTSCHRIFTPOSITION = 3003;
	public static final int UC_ID_PROFORMARECHNUNG = 3004;
	public static final int UC_ID_PROFORMARECHNUNGPOSITION = 3005;
	public static final int UC_ID_ZAHLUNG = 3006;
	public static final int UC_ID_RECHNUNG_UMSATZ = 3007;
	public static final int UC_ID_GUTSCHRIFT_UMSATZ = 3008;
	public static final int UC_ID_RECHNUNGSTATUS = 3009;
	public static final int UC_ID_RECHNUNGART = 3010;
	public static final int UC_ID_RECHNUNGPOSITIONSART = 3011;
	public static final int UC_ID_GUTSCHRIFTPOSITIONSART = 3012;
	public static final int UC_ID_PROFORMARECHNUNGPOSITIONSART = 3013;
	public static final int UC_ID_RECHNUNGTYP = 3014;
	public static final int UC_ID_ZAHLUNGSART = 3015;
	public static final int UC_ID_RECHNUNGTEXT = 3016;
	public static final int UC_ID_RECHNUNGKONTIERUNG = 3017;
	public static final int UC_ID_AUFTRAEGE_EINER_RECHNUNG = 3018;
	public static final int UC_ID_RECHNUNGPOSITIONSICHTAUFTRAG = 3019;
	public static final int UC_ID_GUTSCHRIFTTEXT = 3020;
	public static final int UC_ID_GUTSCHRIFTGRUND = 3021;

	// Finanz: 4000-4999
	public static final int UC_ID_FINANZKONTEN = 4000;
	public static final int UC_ID_FINANZKONTEN_SACHKONTEN = 4001;
	public static final int UC_ID_FINANZKONTEN_DEBITOREN = 4002;
	public static final int UC_ID_FINANZKONTEN_KREDITOREN = 4003;

	public static final int UC_ID_BUCHUNG = 4101;
	public static final int UC_ID_BUCHUNGDETAIL = 4102;
	public static final int UC_ID_BUCHUNGDETAILKASSENBUCH = 4103;
	public static final int UC_ID_BUCHUNGDETAILBUCHUNGSJOURNAL = 4104;
	public static final int UC_ID_BUCHUNGDETAILLIERT = 4105;

	public static final int UC_ID_FINANZAMT = 4200;
	public static final int UC_ID_KASSENBUCH = 4201;
	public static final int UC_ID_BANKKONTO = 4202;
	public static final int UC_ID_ERGEBNISGRUPPE = 4205;
	public static final int UC_ID_KONTOLAENDERART = 4206;
	public static final int UC_ID_WARENVERKEHRSNUMMER = 4207;
	public static final int UC_ID_KONTOLAND = 4208;

	public static final int UC_ID_KONTOART = 4300;
	public static final int UC_ID_UVAART = 4301;
	public static final int UC_ID_LAENDERART = 4302;

	public static final int UC_ID_MAHNLAUF = 4400;
	public static final int UC_ID_MAHNUNG = 4401;
	public static final int UC_ID_MAHNTEXT = 4402;
	public static final int UC_ID_MAHNSTUFE = 4403;
	public static final int UC_ID_MAHNSPESEN = 4404;
	public static final int UC_ID_MAHNSPERRE = 4405;

	public static final int UC_ID_EXPORTLAUF = 4500;
	public static final int UC_ID_EXPORTDATEN = 4501;

	public static final int UC_ID_STEUERKATEGORIE = 4502;
	public static final int UC_ID_STEUERKATEGORIEKONTO = 4503;

	// System 5000-5999
	public static final int UC_ID_THECLIENT = 5000;
	public static final int UC_ID_THEJUDGE = 5001;
	public static final int UC_ID_LANDPLZORT = 5002;
	public static final int UC_ID_KOSTENSTELLE = 5003;
	public static final int UC_ID_BRANCHE = 5004;
	public static final int UC_ID_FUNKTION = 5005;
	public static final int UC_ID_ZAHLUNGSZIEL = 5006;
	public static final int UC_ID_LAND = 5007;
	public static final int UC_ID_ORT = 5008;
	public static final int UC_ID_MANDANT = 5009;
	public static final int UC_ID_LIEFERART = 5010;
	public static final int UC_ID_SPEDITEUR = 5011;
	public static final int UC_ID_BELEGART = 5012;
	public static final int UC_ID_VERSANDAUFTRAG = 5013;
	public static final int UC_ID_EINHEIT = 5014;
	public static final int UC_ID_STATUS = 5015;
	public static final int UC_ID_MEDIASTANDARD = 5016;
	public static final int UC_ID_POSITIONSART = 5017;
	public static final int UC_ID_MEDIAART = 5018;
	public static final int UC_ID_MWSTSATZ = 5019;
	public static final int UC_ID_WAEHRUNG = 5020;
	public static final int UC_ID_PARAMETERANWENDER = 5021;
	public static final int UC_ID_PARAMETERMANDANT = 5022;
	public static final int UC_ID_WECHSELKURS = 5023;
	public static final int UC_ID_EINHEITENKONVERTIERUNG = 5024;
	public static final int UC_ID_PANEL = 5025;
	public static final int UC_ID_PANELBESCHREIBUNG = 5026;
	public static final int UC_ID_MWSTSATZBEZ = 5027;
	public static final int UC_ID_BELEGARTDOKUMENT = 5028;
	public static final int UC_ID_LANDKFZKENNZEICHEN = 5029;
	public static final int UC_ID_EXTRALISTE = 5030;
	public static final int UC_ID_PARAMETER = 5031;
	public static final int UC_ID_ARBEITSPLATZ = 5032;
	public static final int UC_ID_ARBEITSPLATZPARAMETER = 5033;
	public static final int UC_ID_AUTOMATIK = 5034;
	public static final int UC_ID_DOKUMENTBELEGART = 5035;
	public static final int UC_ID_DOKUMENTGRUPPE = 5036;
	public static final int UC_ID_DOKUMENTENLINK = 5037;
	public static final int UC_ID_KOSTENTRAEGER = 5038;

	// Partner (Kunde, Sachbearbeiter, Ansprechpartner, Lieferant) 6000-6999
	public static final int UC_ID_PARTNER = 6000;
	public static final int UC_ID_ANSPRECHPARTNER = 6001;
	public static final int UC_ID_KUNDE2 = 6003;
	public static final int UC_ID_SACHBERABEITER = 6004;
	public static final int UC_ID_LIEFERANTEN = 6006;
	public static final int UC_ID_BANK = 6007;
	public static final int UC_ID_PARTNERKLASSE = 6008;
	public static final int UC_ID_PARTNERKOMMUNIKATION = 6009;
	public static final int UC_ID_ANSPRECHPARTNERFUNKTION = 6010;
	public static final int UC_ID_PARTNERART = 6011;
	public static final int UC_ID_PARTNERBANK = 6012;
	public static final int UC_ID_KUNDE_UMSATZSTATISTIK = 6013;
	public static final int UC_ID_ANREDE = 6015;
	public static final int UC_ID_KOMMUNIKATIONSART = 6016;
	public static final int UC_ID_UMSATZSTATISTIK_LIEFERANT = 6017;
	public static final int UC_ID_MONATSSTATISTIK_LIEFERANT = 6018;
	public static final int UC_ID_LIEFERGRUPPEN = 6019;
	public static final int UC_ID_LFLIEFERGRUPPEN = 6020;
	public static final int UC_ID_LFLIEFERGRUPPENONELF = 6021;
	public static final int UC_ID_SELEKTION = 6022;
	public static final int UC_ID_PARTNERSELEKTION = 6023;
	public static final int UC_ID_PARTNERKURZBRIEF = 6024;
	public static final int UC_ID_SERIENBRIEFSELEKTION = 6025;
	public static final int UC_ID_KUNDESOKO = 6026;
	public static final int UC_ID_KUNDESOKOMENGENSTAFFEL = 6027;
	public static final int UC_ID_LIEFERANTBEURTEILUNG = 6028;
	public static final int UC_ID_KONTAKT = 6029;
	public static final int UC_ID_WIEDERVORLAGE = 6030;
	public static final int UC_ID_ANSPRECHPARTNERPARTNER = 6031;
	public static final int UC_ID_REFERENZ_ZU = 6032;

	// Lieferschein 7000-7499 MB 01.09.07 keine falschen verwendungen
	public static final int UC_ID_LIEFERSCHEIN = 7000;
	public static final int UC_ID_LIEFERSCHEINPOSITION = 7001;
	public static final int UC_ID_LIEFERSCHEINPOSITIONSICHTAUFTRAG = 7002;
	/**
	 * @todo MB: wieso wird der nirgends verwendet?
	 */
	public static final int UC_ID_LIEFERSCHEINUEBERSICHT = 7004;
	public static final int UC_ID_LIEFERSCHEINUMSATZ = 7005;
	public static final int UC_ID_LIEFERSCHEINTEXT = 7006;
	public static final int UC_ID_AUFTRAGPOSITIONINLIEFERSCHEIN = 7007;
	public static final int UC_ID_LIEFERSCHEINART = 7008;
	public static final int UC_ID_LIEFERSCHEINPOSITIONART = 7009;
	public static final int UC_ID_AUFTRAEGE_EINES_LIEFERSCHEINS = 7010;
	public static final int UC_ID_BEGRUENDUNG = 7011;

	// Angebotsstuecklisten 7500-7999 MB 01.09.07 keine falschen verwendungen
	public static final int UC_ID_AGSTKL = 7500;
	public static final int UC_ID_AGSTKLPOSITION = 7501;
	public static final int UC_ID_EINKAUFSANGEBOT = 7502;
	public static final int UC_ID_EINKAUFSANGEBOTPOSITIONEN = 7503;
	public static final int UC_ID_AUFSCHLAG = 7504;

	// Bestellung 8000-8499 MB 01.09.07 keine falschen verwendungen (ausser die
	// depr.)
	public static final int UC_ID_BESTELLUNG = 8000;
	public static final int UC_ID_BESTELLPOSITION = 8001;
	public static final int UC_ID_BESTELLUNGWARENEINGANG = 8002;
	public static final int UC_ID_BESTELLUNGWAREINEINGANGSPOSITIONEN = 8003;
	public static final int UC_ID_BESTELLUNGTEXT = 8004;
	public static final int UC_ID_BESTELLUNGART = 8005;
	public static final int UC_ID_BESTELLUNGSTATUS = 8006;
	public static final int UC_ID_BESTELLVORSCHLAG = 8007;
	public static final int UC_ID_BESTELLPOSITIONSICHTRAHMEN = 8008;
	public static final int UC_ID_BESTELLVORSCHLAGALLELIEFERANTEN = 8009;
	public static final int UC_ID_BESTELLVORSCHLAGALLETERMINE = 8010;
	public static final int UC_ID_SICHTLIEFERTERMINE = 8012;
	public static final int UC_ID_BESTELLPOSITIONART = 8013;
	public static final int UC_ID_BESMAHNTEXT = 8014;
	public static final int UC_ID_BESMAHNSTUFE = 8015;
	public static final int UC_ID_BESMAHNLAUF = 8016;
	public static final int UC_ID_BESMAHNUNG = 8017;
	public static final int UC_ID_OFFENEWEPOS = 8018;
	public static final int UC_ID_MAHNGRUPPE = 8019;
	public static final int UC_ID_LIEFERANTENOPTIMIEREN = 8020;

	// Angebot 8500-8999 MB 01.09.07 keine falschen verwendungen
	public static final int UC_ID_ANGEBOT = 8500;
	public static final int UC_ID_ANGEBOTPOSITION = 8501;
	public static final int UC_ID_ANGEBOTTEXT = 8502;
	public static final int UC_ID_ANGEBOTART = 8503;
	public static final int UC_ID_ANGEBOTERLEDIGUNGSGRUND = 8504;
	public static final int UC_ID_ANGEBOTPOSITIONART = 8505;
	public static final int UC_ID_ANGEBOTUEBERSICHT = 8506;
	public static final int UC_ID_ANGEBOTZEITEN = 8507;

	// Eingangsrechnung 9000-9499 MB 01.09.07 keine falschen verwendungen
	public static final int UC_ID_EINGANGSRECHNUNG = 9000;
	public static final int UC_ID_EINGANGSRECHNUNG_AUFTRAGSZUORDNUNG = 9001;
	public static final int UC_ID_EINGANGSRECHNUNG_UMSATZ = 9002;
	public static final int UC_ID_EINGANGSRECHNUNG_KONTIERUNG = 9003;
	public static final int UC_ID_EINGANGSRECHNUNG_ZAHLUNG = 9004;
	public static final int UC_ID_EINGANGSRECHNUNGART = 9005;
	public static final int UC_ID_EINGANGSRECHNUNGSSTATUS = 9006;
	public static final int UC_ID_ZAHLUNGSVORSCHLAGLAUF = 9007;
	public static final int UC_ID_ZAHLUNGSVORSCHLAG = 9008;
	public static final int UC_ID_EINGANGSRECHNUNG_WARENEINGANG = 9009;
	public static final int UC_ID_ZUSATZKOSTEN = 9010;

	// Anfrage 9500-9999 MB 01.09.07 keine falschen verwendungen
	public static final int UC_ID_ANFRAGE = 9500;
	public static final int UC_ID_ANFRAGETEXT = 9501;
	public static final int UC_ID_ANFRAGEPOSITION = 9502;
	public static final int UC_ID_ANFRAGEPOSITIONLIEFERDATEN = 9503;
	public static final int UC_ID_ANFRAGEPOSITIONART = 9504;
	public static final int UC_ID_ANFRAGEART = 9505;
	public static final int UC_ID_ZERTIFIKATART = 9506;

	// Fertigung 10000-10499 MB 01.09.07 keine falschen verwendungen
	public static final int UC_ID_LOS = 10000;
	public static final int UC_ID_LOSSTATUS = 10001;
	public static final int UC_ID_LOSKLASSE = 10002;
	public static final int UC_ID_LOSSOLLMATERIAL = 10003;
	public static final int UC_ID_LOSSOLLARBEITSPLAN = 10004;
	public static final int UC_ID_LOSLAGERENTNAHME = 10005;
	public static final int UC_ID_LOSLOSKLASSE = 10006;
	public static final int UC_ID_LOSABLIEFERUNG = 10007;
	public static final int UC_ID_LOSNACHKALKULATION = 10008;
	public static final int UC_ID_LOSZEITEN = 10009;
	public static final int UC_ID_INTERNEBESTELLUNG = 10010;
	public static final int UC_ID_BEWEGUNGSVORSCHAU2 = 10011;
	public static final int UC_ID_WIEDERHOLENDELOSE = 10012;
	public static final int UC_ID_ZUSATZSTATUS = 10013;
	public static final int UC_ID_LOSZUSATZSTATUS = 10014;
	public static final int UC_ID_SCHWERE = 10015;
	public static final int UC_ID_LOSISTMATERIAL = 10016;
	public static final int UC_ID_LOSGUTSCHLECHT = 10017;
	public static final int UC_ID_LOSTECHNIKER = 10018;
	public static final int UC_ID_LOSBEREICH = 10019;

	// Kueche 10500-10999 MB 01.09.07 keine falschen verwendungen
	public static final int UC_ID_SPEISEPLAN = 10500;
	public static final int UC_ID_TAGESLOS = 10501;
	public static final int UC_ID_KASSAARTIKEL = 10502;
	public static final int UC_ID_KDC100LOG = 10503;
	public static final int UC_ID_KUECHEUMRECHNUNG = 10504;
	public static final int UC_ID_BEDIENERLAGER = 10505;

	// Stueckliste 11000-11999 MB 01.09.07 keine falschen verwendungen
	public static final int UC_ID_STUECKLISTE = 11000;
	public static final int UC_ID_STUECKLISTEPOSITION = 11001;
	public static final int UC_ID_STUECKLISTEARBEITSPLAN = 11002;
	public static final int UC_ID_STUECKLISTEMONTAGEART = 11003;
	public static final int UC_ID_STUECKLISTEEIGENSCHAFT = 11004;
	public static final int UC_ID_STUECKLISTEEIGENSCHAFTART = 11005;
	public static final int UC_ID_FERTIGUNGSGRUPPE = 11006;
	public static final int UC_ID_POSERSATZ = 11007;
	public static final int UC_ID_KOMMENTARIMPORT = 11008;
	public static final int UC_ID_FERTIGUNGSGRUPPE_EINGESCHRAENKT = 11009;

	// Projekt 12000-12999 MB 01.09.07 keine falschen verwendungen
	public static final int UC_ID_PROJEKT = 12000;
	public static final int UC_ID_KATEGORIE = 12001;
	public static final int UC_ID_TYP = 12002;
	public static final int UC_ID_HISTORY = 12003;
	public static final int UC_ID_KONTAKTART = 12004;
	public static final int UC_ID_PROJEKTSTATUS = 12005;
	public static final int UC_ID_PROJEKTZEITEN = 12006;
	public static final int UC_ID_PROJEKT_QUEUE = 12007;
	public static final int UC_ID_HISTORYART = 12008;
	public static final int UC_ID_PROJEKTERLEDIGUNGSGRUND = 12009;
	public static final int UC_ID_BEREICH = 12010;
	public static final int UC_ID_PROJEKTVERLAUF = 12011;

	// Reklamation 12000-12999 MB 01.09.07 keine falschen verwendungen
	public static final int UC_ID_REKLAMATION = 13000;
	public static final int UC_ID_FEHLER = 13001;
	public static final int UC_ID_FEHLERANGABE = 13002;
	public static final int UC_ID_MASSNAHME = 13003;
	public static final int UC_ID_AUFNAHMEART = 13004;
	public static final int UC_ID_BEHANDLUNG = 13005;
	public static final int UC_ID_TERMINTREUE = 13006;
	public static final int UC_ID_WIRKSAMKEIT = 13007;
	public static final int UC_ID_REKLAMATIONBILD = 13008;

	// Kueche 10500-10599 MB 01.09.07 keine falschen verwendungen
	public static final int UC_ID_INSTANDHALTUNG = 14000;
	public static final int UC_ID_HALLE = 14001;
	public static final int UC_ID_STANDORT = 14002;
	public static final int UC_ID_GERAETETYP = 14003;
	public static final int UC_ID_ISMASCHINE = 14004;
	public static final int UC_ID_ANLAGE = 14005;
	public static final int UC_ID_GERAET = 14006;
	public static final int UC_ID_ISKATEGORIE = 14007;
	public static final int UC_ID_WARTUNGSLISTE = 14008;
	public static final int UC_ID_WARTUNGSSCHRITTE = 14009;
	public static final int UC_ID_GERAETEHISTORIE = 14010;
	public static final int UC_ID_GERAETEHISTORIE_MIT_GERAET = 14011;
	public static final int UC_ID_GEWERK = 14012;
	public static final int UC_ID_STANDORTTECHNIKER = 14013;

	// Inserat 16000-16999
	public static final int UC_ID_INSERAT = 16000;
	public static final int UC_ID_INSERATRECHNUNG = 16001;
	public static final int UC_ID_INSERATER = 16002;
	public static final int UC_ID_INSERATE_OHNE_ER = 16003;
	public static final int UC_ID_INSERATARTIKEL = 16004;

	public static final int UC_ID_EINGANGSRECHNUNGEN_EINES_INSERATES = 16005;

	// Alle FLR-Breitendefinitionen; die zentrale Stelle!
	// flrspalte: 0 fixe Spaltenbreite fuer die Anzeige einer FLR Liste am
	// Client
	public static final int FLR_BREITE_SHARE_WITH_REST = -1;

	public static final int FLR_BREITE_XXS = 2; // fuer 1 Zeichen
	public static final int FLR_BREITE_S = 3;
	public static final int FLR_BREITE_XS = 5;
	public static final int FLR_BREITE_M = 12;
	public static final int FLR_BREITE_XM = 17;
	public static final int FLR_BREITE_L = 22;
	public static final int FLR_BREITE_XL = 42;
	public static final int FLR_BREITE_XXL = 82;
	public static final int FLR_BREITE_MINIMUM = 2;

	public static final int FLR_BREITE_IDENT = FLR_BREITE_M;
	// fuer alle Ident in Positionsmodulen

	public static final int FLR_BREITE_PREIS = 14; // fuer 012.345.678,56
	public static final int FLR_BREITE_PREIS_NACHKOMMASTELLEN = 2; // Breite der
	// Nachkommastellen
	// alleine
	// z.B 2
	// fuer 0,56

	public static final int FLR_BREITE_MENGE = 12; // fuer 123.456,123
	public static final int FLR_BREITE_MENGE_NACHKOMMASTELLEN = 3; // Breite der
	// Nachkommastellen
	// alleine
	// z.B 3
	// fuer
	// 0,123

	public static final int FLR_BREITE_WAEHRUNG = 4;

	// Menge und Preis Konstanten, zum Vergleich ob FLR Breite Preis oder Menge
	// verwendet wird.
	public static final String MENGE = "Menge";
	public static final String PREIS = "Preis";

	public static final String FEATURE_PREFIX = "FEATURE:" ;
	
	public static String getModulForUseCase(int iUseCaseId) {
		if (iUseCaseId < 500) {
			return LocaleFac.BELEGART_PERSONAL;
		} else if (iUseCaseId < 1000) {
			return LocaleFac.BELEGART_BENUTZER;
		} else if (iUseCaseId < 2000) {
			return LocaleFac.BELEGART_AUFTRAG;
		} else if (iUseCaseId < 3000) {
			return LocaleFac.BELEGART_ARTIKEL;
		} else if (iUseCaseId < 4000) {
			return LocaleFac.BELEGART_RECHNUNG;
		} else if (iUseCaseId < 5000) {
			return LocaleFac.BELEGART_FINANZBUCHHALTUNG;
		} else if (iUseCaseId < 6000) {
			return LocaleFac.BELEGART_SYSTEM;
		} else if (iUseCaseId < 7000) {
			return LocaleFac.BELEGART_PARTNER;
		} else if (iUseCaseId < 7500) {
			return LocaleFac.BELEGART_LIEFERSCHEIN;
		} else if (iUseCaseId < 8000) {
			return LocaleFac.BELEGART_AGSTUECKLISTE;
		} else if (iUseCaseId < 8500) {
			return LocaleFac.BELEGART_BESTELLUNG;
		} else if (iUseCaseId < 9000) {
			return LocaleFac.BELEGART_ANGEBOT;
		} else if (iUseCaseId < 9500) {
			return LocaleFac.BELEGART_EINGANGSRECHNUNG;
		} else if (iUseCaseId < 10000) {
			return LocaleFac.BELEGART_ANFRAGE;
		} else if (iUseCaseId < 11000) {
			return LocaleFac.BELEGART_LOS;
		} else if (iUseCaseId < 12000) {
			return LocaleFac.BELEGART_STUECKLISTE;
		} else if (iUseCaseId < 13000) {
			return LocaleFac.BELEGART_PROJEKT;
		} else
			return null;
	}

	/**
	 * the use case this query was build for.
	 */
	private Integer useCaseId = null;

	/**
	 * the sorting criterias (ORDER BY clause) for the query.
	 */
	private SortierKriterium[] sortKrit = null;

	/**
	 * the filter criterias (WHERE claus).
	 */
	private FilterBlock filterBlock = null;

	/**
	 * the id of the data row that is currently selected in the table (this is
	 * not the row number but the data base id!)
	 */
	private Object keyOfSelectedRow = null;

	// flrextradata: 3 Extra Daten, die man im FLR auswerten kann!
	private ArrayList<?> listOfExtraData = null;

	private Integer limit;

	/**
	 * creates a new QueryParameters instance.
	 * 
	 * @param useCaseId
	 *            identifies the use case this query is intended for.
	 * @param pSortKrit
	 *            the sort criterias used to sort the result.
	 * @param pFilter
	 *            the filter criterias that narrow the result.
	 * @param keyOfSelectedRow
	 *            the (database) id of the row that is currently selected in the
	 *            table.
	 * @param listOfExtraDataI
	 *            ArrayList
	 */
	public QueryParameters(Integer useCaseId, SortierKriterium[] pSortKrit,
			FilterBlock pFilter, Object keyOfSelectedRow,
			ArrayList<?> listOfExtraDataI) {
		this.useCaseId = useCaseId;
		this.sortKrit = pSortKrit;
		this.filterBlock = pFilter;
		this.keyOfSelectedRow = keyOfSelectedRow;
		this.listOfExtraData = listOfExtraDataI;
	}

	/**
	 * @return the String representation of this query.
	 */
	public String toString() {
		StringBuffer buff = new StringBuffer("");

		buff.append("Use Case: " + this.useCaseId + ", ");
		buff.append(SortierKriterium.arrayToString(sortKrit));
		if (filterBlock != null) {
			buff.append(filterBlock.toString());
		}

		return buff.toString();
	}

	/**
	 * @return Returns the filterBlock.
	 */
	public FilterBlock getFilterBlock() {
		return filterBlock;
	}

	public int iMaxAnzahlZeilen = -1;

	/**
	 * @param filterBlock
	 *            The filterBlock to set.
	 */
	public void setFilterBlock(FilterBlock filterBlock) {
		this.filterBlock = filterBlock;
	}

	/**
	 * @return Returns the sortKrit.
	 */
	public SortierKriterium[] getSortKrit() {
		return sortKrit;
	}

	/**
	 * @param sortKrit
	 *            The sortKrit to set.
	 */
	public void setSortKrit(SortierKriterium[] sortKrit) {
		this.sortKrit = sortKrit;
	}

	/**
	 * @return Returns the useCaseId.
	 */
	public Integer getUseCaseId() {
		return useCaseId;
	}

	/**
	 * @param useCaseId
	 *            The useCaseId to set.
	 */
	public void setUseCaseId(Integer useCaseId) {
		this.useCaseId = useCaseId;
	}

	/**
	 * @return Returns the idOfSelectedRow.
	 */
	/*
	 * public Integer getIdOfSelectedRow() { return idOfSelectedRow; }
	 */

	/**
	 * @param idOfSelectedRow
	 *            The idOfSelectedRow to set.
	 */
	/*
	 * public void setIdOfSelectedRow(Integer idOfSelectedRow) {
	 * this.idOfSelectedRow = idOfSelectedRow; }
	 */

	/**
	 * @return Returns the idOfSelectedRow.
	 */
	public Object getKeyOfSelectedRow() {
		return keyOfSelectedRow;
	}

	public ArrayList<?> getListOfExtraData() {
		return listOfExtraData;
	}

	/**
	 * 
	 * @param keyOfSelectedRow
	 *            The idOfSelectedRow to set.
	 */
	public void setKeyOfSelectedRow(Object keyOfSelectedRow) {
		this.keyOfSelectedRow = keyOfSelectedRow;
	}

	public void setListOfExtraData(ArrayList<?> listOfExtraData) {
		this.listOfExtraData = listOfExtraData;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}
}
