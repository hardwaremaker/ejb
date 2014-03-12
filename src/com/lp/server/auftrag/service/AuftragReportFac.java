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
package com.lp.server.auftrag.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.Hashtable;
import java.util.Map;

import javax.ejb.Remote;

import com.lp.server.partner.service.PartnerklasseDto;
import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;

@Remote
public interface AuftragReportFac {
	public final static String REPORT_MODUL = "auftrag";
	

	public final static String REPORT_AUFTRAG_ALLE = "auft_auftrag_alle.jasper";
	public final static String REPORT_AUFTRAG_ERLEDIGT = "auft_auftrag_erledigt.jasper";
	public final static String REPORT_AUFTRAG_OFFENE = "auft_auftrag_offene.jasper";
	public final static String REPORT_AUFTRAG_OFFENE_OHNE_DETAILS = "auft_auftrag_offene_ohne_details.jasper";
	public final static String REPORT_AUFTRAGBESTAETIGUNG = "auft_auftragbestaetigung2.jasper";
	public final static String REPORT_AUFTRAG_PACKLISTE = "auft_packliste2.jasper";
	public final static String REPORT_AUFTRAG_PACKLISTE3 = "auft_packliste3.jasper";
	public final static String REPORT_AUFTRAG_PACKLISTE4 = "auft_packliste4.jasper";
	public final static String REPORT_AUFTRAG_PACKLISTE_SNR = "auft_packliste_snr.jasper";
	public final static String REPORT_AUFTRAGSTATISTIK = "auft_auftragstatistik.jasper";
	public final static String REPORT_AUFTRAG_SRN_ETIKETT_G = "auft_srnet_g.jasper";
	public final static String REPORT_AUFTRAG_SRN_ETIKETT_K = "auft_srnet_k.jasper";
	public final static String REPORT_AUFTRAGSZEITEN = "auft_auftragszeiten.jasper";
	public final static String REPORT_TAETIGKEITSSTATISTIK = "auft_taetigkeitsstatistik.jasper";
	public final static String REPORT_RAHMENUEBERSICHT = "auft_rahmenuebersicht.jasper";
	public final static String REPORT_AUFTRAGSPOSITIONSETIKETT = "auft_auftragspositionsetikett.jasper";

	public final static int REPORT_AUFTRAGSTATISTIK_SORTIERUNG_AUFTRAG = 1;
	public final static int REPORT_AUFTRAGSTATISTIK_SORTIERUNG_KUNDE_AUFTRAGSADRESSE = 2;
	public final static int REPORT_AUFTRAGSTATISTIK_SORTIERUNG_KUNDE_RECHNUNGSADRESSE = 3;
	public final static int REPORT_AUFTRAGSTATISTIK_SORTIERUNG_BESTELLNUMMER = 4;
	public final static int REPORT_AUFTRAGSTATISTIK_SORTIERUNG_PROJEKT = 5;
	public final static int REPORT_AUFTRAGSTATISTIK_SORTIERUNG_FUEHRENDER_ARTIKEL = 6;

	public final static String REPORT_AUFTRAG_VORKALKULATION = "auft_vorkalkulation.jasper";
	public final static String REPORT_AUFTRAG_TEIL_LIEFERBAR = "auft_teil_lieferbar.jasper";
	public final static String REPORT_AUFTRAG_OFFENE_DETAILS = "auft_auftrag_offene_details.jasper";
	public final static String REPORT_AUFTRAG_OFFENE_POSITIONEN = "auft_auftrag_offene_positionen.jasper";

	public final static String REPORT_AUFTRAG_WIEDERBESCHAFFUNG = "auft_wiederbeschaffung.jasper";
	public final static String REPORT_AUFTRAG_ROLLIERENDEPLANUNG = "auft_rollierendeplanung.jasper";
	public final static String REPORT_AUFTRAG_VERFUEGBARKEIT = "auft_verfuegbarkeit.jasper";
	public final static String REPORT_AUFTRAG_ERFUELLUNGSGRAD = "auft_erfuellungsgrad.jasper";
	public final static String REPORT_AUFTRAG_ERFUELLUNGSJOURNAL = "auft_erfuellungsjournal.jasper";

	public final static int REPORT_AUFTRAG_OFFENE_OD_AUFTRAGSNUMMER = 0;
	public final static int REPORT_AUFTRAG_OFFENE_OD_KUNDE = 1;
	public final static int REPORT_AUFTRAG_OFFENE_OD_PROJEKT = 2;
	public final static int REPORT_AUFTRAG_OFFENE_OD_AUFTRAGBESTELLNUMMER = 3;
	public final static int REPORT_AUFTRAG_OFFENE_OD_WERT = 4;
	public final static int REPORT_AUFTRAG_OFFENE_OD_WERTOFFEN = 5;
	public final static int REPORT_AUFTRAG_OFFENE_OD_LIEFERTERMIN = 6;
	public final static int REPORT_AUFTRAG_OFFENE_OD_KOSTENSTELLENUMMER = 7;
	public final static int REPORT_AUFTRAG_OFFENE_OD_KUNDE_ORT = 8;
	public final static int REPORT_AUFTRAG_OFFENE_OD_VERTRETERCNAME1 = 9;
	public final static int REPORT_AUFTRAG_OFFENE_OD_AUFTRAGEIGENSCHAFT_FA = 10;
	public final static int REPORT_AUFTRAG_OFFENE_OD_AUFTRAGEIGENSCHAFT_CLUSTER = 11;
	public final static int REPORT_AUFTRAG_OFFENE_OD_AUFTRAGEIGENSCHAFT_EQNR = 12;
	public final static int REPORT_AUFTRAG_OFFENE_OD_INTERNERKOMMENTAR = 13;
	public final static int REPORT_AUFTRAG_OFFENE_OD_ZEIT_VERRECHENBAR = 14;
	public final static int REPORT_AUFTRAG_OFFENE_OD_PERSON_VERRECHENBAR = 15;
	public final static int REPORT_AUFTRAG_OFFENE_OD_ANZAHL_SPALTEN = 16;

	public final static int REPORT_AUFTRAG_TEILLIFERBAR_AUFTRAGSNUMMER = 0;
	public final static int REPORT_AUFTRAG_TEILLIFERBAR_KUNDE = 1;
	public final static int REPORT_AUFTRAG_TEILLIFERBAR_PROJEKT = 2;
	public final static int REPORT_AUFTRAG_TEILLIFERBAR_WERT = 3;
	public final static int REPORT_AUFTRAG_TEILLIFERBAR_WERTOFFEN = 4;
	public final static int REPORT_AUFTRAG_TEILLIFERBAR_LIEFERTERMIN = 5;
	public final static int REPORT_AUFTRAG_TEILLIFERBAR_KOSTENSTELLENUMMER = 6;
	public final static int REPORT_AUFTRAG_TEILLIFERBAR_KUNDE_ORT = 7;
	public final static int REPORT_AUFTRAG_TEILLIFERBAR_ANZAHL_SPALTEN = 8;

	public final static int REPORT_AUFTRAG_OFFENE_AUFTRAGCNR = 0;
	public final static int REPORT_AUFTRAG_OFFENE_AUFTRAGKUNDE = 1;
	public final static int REPORT_AUFTRAG_OFFENE_AUFTRAGLIEFERTERMIN = 2;
	public final static int REPORT_AUFTRAG_OFFENE_AUFTRAGFINALTERMIN = 3;
	public final static int REPORT_AUFTRAG_OFFENE_AUFTRAGZAHLUNGSZIEL = 4;
	public final static int REPORT_AUFTRAG_OFFENE_AUFTRAGPROJEKTBEZEICHNUNG = 5;
	public final static int REPORT_AUFTRAG_OFFENE_ARTIKELCNR = 6;
	public final static int REPORT_AUFTRAG_OFFENE_ARTIKELBEZEICHNUNG = 7;
	public final static int REPORT_AUFTRAG_OFFENE_ARTIKELMENGE = 8;
	public final static int REPORT_AUFTRAG_OFFENE_ARTIKELEINHEIT = 9;
	public final static int REPORT_AUFTRAG_OFFENE_ARTIKELNETTOGESAMTPREISPLUSVERSTECKTERAUFSCHLAGMINUSRABATTE = 10;
	public final static int REPORT_AUFTRAG_OFFENE_ARTIKELGESTEHUNGSPREIS = 11;
	public final static int REPORT_AUFTRAG_OFFENE_ARTIKELGELIEFERTEMENGE = 12;
	public final static int REPORT_AUFTRAG_OFFENE_ARTIKELOFFENEMENGE = 13;
	public final static int REPORT_AUFTRAG_OFFENE_ARTIKELOFFENERWERT = 14;
	public final static int REPORT_AUFTRAG_OFFENE_ARTIKELOFFENERDB = 15;
	public final static int REPORT_AUFTRAG_OFFENE_KOSTENSTELLECNR = 16;
	public final static int REPORT_AUFTRAG_OFFENE_INTERNERKOMMENTAR = 17;
	public final static int REPORT_AUFTRAG_OFFENE_ARTIKELLAGERSTAND = 18;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONSTERMIN = 19;
	public final static int REPORT_AUFTRAG_OFFENE_AUFTRAGBESTELLNUMMER = 20;
	public final static int REPORT_AUFTRAG_OFFENE_AUFTRAGVERTRETER = 21;
	public final static int REPORT_AUFTRAG_OFFENE_AUFTRAGEIGENSCHAFT_FA = 22;
	public final static int REPORT_AUFTRAG_OFFENE_AUFTRAGEIGENSCHAFT_CLUSTER = 23;
	public final static int REPORT_AUFTRAG_OFFENE_AUFTRAGEIGENSCHAFT_EQNR = 24;
	public final static int REPORT_AUFTRAG_OFFENE_EINKAUFSPREIS = 25;
	public final static int REPORT_AUFTRAG_OFFENE_EXTERNERKOMMENTAR = 26;
	public final static int REPORT_AUFTRAG_OFFENE_ZEIT_VERRECHENBAR = 27;
	public final static int REPORT_AUFTRAG_OFFENE_PERSON_VERRECHENBAR = 28;
	public final static int REPORT_AUFTRAG_OFFENE_ANZAHL_SPALTEN = 29;

	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGCNR = 0;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGKUNDE = 1;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGLIEFERTERMIN = 2;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGFINALTERMIN = 3;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGZAHLUNGSZIEL = 4;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGPROJEKTBEZEICHNUNG = 5;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELCNR = 6;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELBEZEICHNUNG = 7;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELMENGE = 8;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELEINHEIT = 9;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELNETTOGESAMTPREISPLUSVERSTECKTERAUFSCHLAGMINUSRABATTE = 10;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELGESTEHUNGSPREIS = 11;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELGELIEFERTEMENGE = 12;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELOFFENEMENGE = 13;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELOFFENERWERT = 14;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELOFFENERDB = 15;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_KOSTENSTELLECNR = 16;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_INTERNERKOMMENTAR = 17;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELLAGERSTAND = 18;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_POSITIONSTERMIN = 19;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_KUNDEAUFTRAGADRESSE = 20;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_KUNDELIEFERADRESSE = 21;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_LOSNUMMER = 22;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGBESTELLNUMMER = 23;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGKUNDE2 = 24;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGARTCNR = 25;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGEIGENSCHAFT_FA = 26;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGEIGENSCHAFT_CLUSTER = 27;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGEIGENSCHAFT_EQNR = 28;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_LOS_FERTIGUNGSGRUPPE = 29;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_STKL_FERTIGUNGSGRUPPE = 30;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKELBESTELLTMENGE = 31;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_SPEDITEUR = 32;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_EINKAUFSPREIS = 33;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_EXTERNERKOMMENTAR = 34;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_B_LIEFERTERMIN = 35;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_RAHMENAUFTRAG = 36;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_LAGERSTAND_NORMALLAGER = 37;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_LAGERSTAND_ZOLLLAGER = 38;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_LAGERSTAND_SPERRLAGER = 39;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_LAGERSTAND_PERSOENLICH = 40;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_LAGERSTAND_KUNDENLAGER = 41;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_LAGERSTAND_SCHROTT = 42;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_LAGERSTAND_LIEFERANT = 43;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_LAGERSTAND_HALBFERTIG = 44;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_IST_STUECKLISTENPOSITION = 45;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_ORIGINAL_SORTIERUNG = 46;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_SORTIERUNG_STKLPOS = 47;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_SORTIERUNG_UEBER_GANZE_LISTE = 48;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_POSITIONSTERMIN_TIMESTAMP = 49;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKEL_I_ID = 50;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_LAGERSTANDS_FEHLMENGE = 51;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_LAGERSTAND_BEREITS_ABGEZOGEN = 52;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_LAGERSTANDS_FEHLMENGE_KUMULIERT = 53;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKEL_RAHMENRESERVIERUNGEN = 54;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKEL_FERTIGUNGSSATZGROESSE = 55;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKEL_LAGERSOLL = 56;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKEL_LAGERMINDEST = 57;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_KUNDELIEFERADRESSE_LIEFERDAUER = 58;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_ZEIT_VERRECHENBAR = 59;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_PERSON_VERRECHENBAR = 60;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_ANZAHL_SPALTEN = 61;

	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGCNR = 0;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGKUNDE = 1;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGLIEFERTERMIN = 2;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGFINALTERMIN = 3;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGZAHLUNGSZIEL = 4;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGPROJEKTBEZEICHNUNG = 5;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELCNR = 6;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELBEZEICHNUNG = 7;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELMENGE = 8;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELEINHEIT = 9;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELSNR = 10;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELGESTEHUNGSPREIS = 11;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELGELIEFERTEMENGE = 12;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELOFFENEMENGE = 13;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELOFFENERWERT = 14;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELOFFENERDB = 15;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGPOENALE = 16;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_INTERNERKOMMENTAR = 17;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELLAGERSTAND = 18;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_POSITIONSTERMIN = 19;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_KUNDEAUFTRAGADRESSE = 20;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_KUNDELIEFERADRESSE = 21;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_LOSNUMMER = 22;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGBESTELLNUMMER = 23;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGKUNDE2 = 24;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGARTCNR = 25;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGEIGENSCHAFT_FA = 26;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGEIGENSCHAFT_CLUSTER = 27;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGEIGENSCHAFT_EQNR = 28;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELNETTOGESAMTPREISPLUSVERSTECKTERAUFSCHLAGMINUSRABATTE = 29;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_STKL_FERTIGUNGSGRUPPE = 30;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELBESTELLTMENGE = 31;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_FIKTIVERLAGERSTAND = 32;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGROHS = 33;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_OFFENERAHMENMENGE = 34;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_ERSTLOS = 35;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_NUR_MATERIALLISTEN = 36;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_LAGERSTAND = 37;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_POSITIONSSTATUS = 38;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_ZEIT_VERRECHENBAR = 39;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_PERSON_VERRECHENBAR = 40;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_ANZAHL_SPALTEN = 41;
	// druckdetail: 0 folgende Spalten muessen vorhanden sein
	// fuer Positionsart Ident, Handeingabe, AGStueckliste
	public final static int REPORT_AUFTRAGBESTAETIGUNG_POSITION = 0;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_POSITION_TITEL = 1; // der
	// Titel
	// der
	// Position
	// wird
	// fett
	// gedruckt
	public final static int REPORT_AUFTRAGBESTAETIGUNG_POSITION_UNTERTITEL = 2; // der
	// Untertitel
	// der
	// Position
	// ist
	// optional
	public final static int REPORT_AUFTRAGBESTAETIGUNG_POSITION_BEZEICHNUNG = 3; // optional
	public final static int REPORT_AUFTRAGBESTAETIGUNG_POSITION_ZUSATZBEZ = 4; // optional
	public final static int REPORT_AUFTRAGBESTAETIGUNG_POSITION_ZUSATZBEZ2 = 5; // optional
	public final static int REPORT_AUFTRAGBESTAETIGUNG_POSITION_KOMMENTAR_TEXT = 6; // optional
	public final static int REPORT_AUFTRAGBESTAETIGUNG_POSITION_KOMMENTAR_IMAGE = 7; // optional
	public final static int REPORT_AUFTRAGBESTAETIGUNG_MENGE = 8;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_EINHEIT = 9;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_EINZELPREIS = 10;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_RABATTSATZ = 11;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_ZUSATZRABATTSATZ = 12;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_GESAMTPREIS = 13;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_MWSTSATZ = 14;
	// fuer Positionsart Betrifft, Freier Text, Textbaustein/Text
	public final static int REPORT_AUFTRAGBESTAETIGUNG_FREIERTEXT = 15;
	// fuer Positionsart Leerzeile
	public final static int REPORT_AUFTRAGBESTAETIGUNG_LEERZEILE = 16;
	// fuer Positionsart Textbaustein/Image
	public final static int REPORT_AUFTRAGBESTAETIGUNG_IMAGE = 17;
	// fuer Positionsart Seitenumbruch
	public final static int REPORT_AUFTRAGBESTAETIGUNG_SEITENUMBRUCH = 18;
	// fuer die kuenstliche Positionsart Stueckliste
	public final static int REPORT_AUFTRAGBESTAETIGUNG_STKLMENGE = 19;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_STKLEINHEIT = 20;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_STKLARTIKELCNR = 21;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_STKLARTIKELBEZ = 22;
	// die Positionsart wird mituebergeben
	public final static int REPORT_AUFTRAGBESTAETIGUNG_POSITIONSART = 23;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_POSITIONTERMIN = 24;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_IDENTNUMMER = 25;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_BEZEICHNUNG = 26;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_KURZBEZEICHNUNG = 27;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_ARTIKELCZBEZ2 = 28;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_KUNDEARTIKELNR = 29;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_ZWANGSSERIENNUMMER = 30;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_REFERENZNUMMER = 31;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_BAUFORM = 32;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_VERPACKUNGSART = 33;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_MATERIAL = 34;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_BREITE = 35;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_HOEHE = 36;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_TIEFE = 37;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_EIGENSCHAFTEN_FA = 38;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_EIGENSCHAFTEN_CLUSTER = 39;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_EIGENSCHAFTEN_EQNR = 40;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_FIKTIVERLAGERSTAND = 41;
	// Die Termine als Timestamps
	public final static int REPORT_AUFTRAGBESTAETIGUNG_LIEFERTERMIN = 42;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_POSITION_NR = 43;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_BESTELLNUMMER = 44;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_PROJEKT = 45;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_TYP_CNR = 46;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_IDENT_TEXTEINGABE = 47; // der
	public final static int REPORT_AUFTRAGBESTAETIGUNG_POSITIONOBJEKT = 48;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_SETARTIKEL_TYP = 49;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_VERLEIHTAGE = 50;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_VERLEIHFAKTOR = 51;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_INDEX = 52;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_REVISION = 53;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_GELIEFERTE_MENGE = 54;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_LV_POSITION = 55;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_VONPOSITION = 56;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_BISPOSITION = 57;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_ZWSNETTOSUMME = 58;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_ZWSTEXT = 59;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_INTERNAL_IID = 60;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_STKLARTIKELKBEZ = 61;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_STKLARTIKEL_KDARTIKELNR = 62;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_WERBEABGABEPFLICHTIG = 63;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_STKLARTIKEL_KDPREIS = 64;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_MATERIALZUSCHLAG = 65;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_KUNDEARTIKELNR_AUFTRAGSADRESSE = 66;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_MATERIALGEWICHT = 67;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_KURS_MATERIALZUSCHLAG = 68;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_DATUM_MATERIALZUSCHLAG = 69;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_ANZAHL_SPALTEN = 70;

	public final static int REPORT_STATISTIK_BELEGART = 0;
	public final static int REPORT_STATISTIK_BELEGNUMMER = 1;
	public final static int REPORT_STATISTIK_VERKAUFSWERTARBEITSOLL = 3;
	public final static int REPORT_STATISTIK_VERKAUFSWERTMATERIALSOLL = 4;
	public final static int REPORT_STATISTIK_VERKAUFSWERTARBEITIST = 5;
	public final static int REPORT_STATISTIK_VERKAUFSWERTMATERIALIST = 6;
	public final static int REPORT_STATISTIK_GESTEHUNGSWERTARBEITSOLL = 7;
	public final static int REPORT_STATISTIK_GESTEHUNGSWERTMATERIALSOLL = 8;
	public final static int REPORT_STATISTIK_GESTEHUNGSWERTARBEITIST = 9;
	public final static int REPORT_STATISTIK_GESTEHUNGSWERTMATERIALIST = 10;
	public final static int REPORT_STATISTIK_ARBEITSZEITSOLL = 11;
	public final static int REPORT_STATISTIK_ARBEITSZEITIST = 12;
	public final static int REPORT_STATISTIK_EINGANGSRECHNUNGTEXT = 13;
	public final static int REPORT_STATISTIK_MASCHINENZEITSOLL = 14;
	public final static int REPORT_STATISTIK_MASCHINENZEITIST = 15;
	public final static int REPORT_STATISTIK_ARTIKELNUMMERARBEITSZEIT = 16;
	public final static int REPORT_STATISTIK_ARTIKELBEZEICHNUNGARBEITSZEIT = 17;
	public final static int REPORT_STATISTIK_GRUPPIERUNG = 18;
	public final static int REPORT_STATISTIK_AUFTRAGSNUMMER = 19;
	public final static int REPORT_STATISTIK_GRUPPIERUNGBEZEICHNUNG = 20;
	public final static int REPORT_STATISTIK_BELEGKUNDE_NAME1 = 21;
	public final static int REPORT_STATISTIK_BELEGKUNDE_NAME2 = 22;
	public final static int REPORT_STATISTIK_BELEGKUNDE_NAME3 = 23;
	public final static int REPORT_STATISTIK_BELEGKUNDE_LKZ = 24;
	public final static int REPORT_STATISTIK_BELEGKUNDE_PLZ = 25;
	public final static int REPORT_STATISTIK_BELEGKUNDE_ORT = 26;
	public final static int REPORT_STATISTIK_BELEGKUNDE_STRASSE = 27;
	public final static int REPORT_STATISTIK_BELEGRECHNUNGSADRESSE_NAME1 = 28;
	public final static int REPORT_STATISTIK_BELEGRECHNUNGSADRESSE_NAME2 = 29;
	public final static int REPORT_STATISTIK_BELEGRECHNUNGSADRESSE_NAME3 = 30;
	public final static int REPORT_STATISTIK_BELEGRECHNUNGSADRESSE_LKZ = 31;
	public final static int REPORT_STATISTIK_BELEGRECHNUNGSADRESSE_PLZ = 32;
	public final static int REPORT_STATISTIK_BELEGRECHNUNGSADRESSE_ORT = 33;
	public final static int REPORT_STATISTIK_BELEGRECHNUNGSADRESSE_STRASSE = 34;
	public final static int REPORT_STATISTIK_PROJEKT = 35;
	public final static int REPORT_STATISTIK_BESTELLNUMMER = 36;
	public final static int REPORT_STATISTIK_LOS_STUECKLISTE_ARTIKELNUMMER = 37;
	public final static int REPORT_STATISTIK_LOS_STUECKLISTE_BEZEICHNUNG = 38;
	public final static int REPORT_STATISTIK_LOS_STUECKLISTE_ZUSATZBEZEICHNUNG = 39;
	public final static int REPORT_STATISTIK_LOS_PROJEKT = 40;
	public final static int REPORT_STATISTIK_LOS_KOMMENTAR = 41;
	public final static int REPORT_STATISTIK_RE_BESTELLNUMMER = 42;
	public final static int REPORT_STATISTIK_ER_LIEFERANT = 43;
	public final static int REPORT_STATISTIK_RECHNUNGSNUMMER_LS_VERRECHNET = 44;
	public final static int REPORT_STATISTIK_LOS_ERLEDIGUNGSDATUM = 45;
	public final static int REPORT_STATISTIK_LOS_BEWERTUNG = 46;
	public final static int REPORT_STATISTIK_FUEHRENDER_ARTIKEL = 47;
	public final static int REPORT_STATISTIK_RECHNUNGSART = 48;

	public final static int REPORT_PACKLISTE_IDENT = 0;
	public final static int REPORT_PACKLISTE_BEZEICHNUNG = 1;
	public final static int REPORT_PACKLISTE_GESAMTMENGE = 2;
	public final static int REPORT_PACKLISTE_OFFENEMENGE = 3;
	public final static int REPORT_PACKLISTE_LAGERSTAND = 4;
	public final static int REPORT_PACKLISTE_LAGERORT = 5;
	public final static int REPORT_PACKLISTE_GEWICHT = 6;
	public final static int REPORT_PACKLISTE_RASTER_LIEGEND = 7;
	public final static int REPORT_PACKLISTE_RASTER_STEHEND = 8;
	public final static int REPORT_PACKLISTE_MATERIALGEWICHT = 9;
	public final static int REPORT_PACKLISTE_SERIENCHARGENR = 10;
	public final static int REPORT_PACKLISTE_POSITION_FREIERTEXT = 11; // optional
	public final static int REPORT_PACKLISTE_POSITION_KOMMENTAR_IMAGE = 12; // optional
	public final static int REPORT_PACKLISTE_VOLLSTAENDIGKEIT_KOMPONENTEN_IDENT = 13;
	public final static int REPORT_PACKLISTE_VOLLSTAENDIGKEIT_KOMPONENTEN_BEZ = 14;
	public final static int REPORT_PACKLISTE_VORZEICHEN = 15;
	public final static int REPORT_PACKLISTE_POSITIONSTERMIN = 16;
	public final static int REPORT_PACKLISTE_POSITIONSTERMIN_TIMESTAMP = 17;
	public final static int REPORT_PACKLISTE_FIKTIVERLAGERSTAND = 18;

	public static final int REPORT_PACKLISTE_ARTIKELKLASSE = 19;
	public static final int REPORT_PACKLISTE_ARTIKELGRUPPE = 20;
	public static final int REPORT_PACKLISTE_FARBCODE = 23;
	public static final int REPORT_PACKLISTE_MATERIAL = 24;
	public static final int REPORT_PACKLISTE_HOEHE = 25;
	public static final int REPORT_PACKLISTE_BREITE = 26;
	public static final int REPORT_PACKLISTE_TIEFE = 27;
	public static final int REPORT_PACKLISTE_BAUFORM = 28;
	public static final int REPORT_PACKLISTE_VERPACKUNGSART = 29;
	public final static int REPORT_PACKLISTE_ARTIKELKOMMENTAR = 30;
	public final static int REPORT_PACKLISTE_VERKAUFSEAN = 31;
	public final static int REPORT_PACKLISTE_VERPACKUNGSMENGE = 32;
	public final static int REPORT_PACKLISTE_VERPACKUNGSEAN = 33;
	public final static int REPORT_PACKLISTE_ARBEITSGAENGE = 34;
	public final static int REPORT_PACKLISTE_SUBREPORT_DATA = 35;
	public final static int REPORT_PACKLISTE_ANZAHL_SPALTEN = 36;

	public final static int REPORT_AUFTRAGZEITEN_PERSON = 0;
	public final static int REPORT_AUFTRAGZEITEN_ARTIKEL = 1;
	public final static int REPORT_AUFTRAGZEITEN_BEZEICHNUNG = 2;
	public final static int REPORT_AUFTRAGZEITEN_STUNDENSATZ = 3;
	public final static int REPORT_AUFTRAGZEITEN_VON = 5;
	public final static int REPORT_AUFTRAGZEITEN_BIS = 6;
	public final static int REPORT_AUFTRAGZEITEN_DAUER = 7;
	public final static int REPORT_AUFTRAGZEITEN_BEMERKUNG = 8;
	public final static int REPORT_AUFTRAGZEITEN_KOMMENTAR = 9;
	public final static int REPORT_AUFTRAGZEITEN_KOSTEN = 10;
	public final static int REPORT_AUFTRAGZEITEN_ARTIKEL_POSITION = 11;
	public final static int REPORT_AUFTRAGZEITEN_BEZEICHNUNG_POSITION = 12;
	public final static int REPORT_AUFTRAGZEITEN_ZUSATZBEZEICHNUNG_POSITION = 13;
	public final static int REPORT_AUFTRAGZEITEN_POSITIONSNUMMER = 14;
	public final static int REPORT_AUFTRAGZEITEN_ANZAHL_SPALTEN = 15;

	public final static int REPORT_SRN_ETIKETT_IDENT = 0;
	public final static int REPORT_SRN_ETIKETT_BEZEICHNUNG = 1;
	public final static int REPORT_SRN_ETIKETT_GESAMTMENGE = 2;
	public final static int REPORT_SRN_ETIKETT_OFFENEMENGE = 3;
	public final static int REPORT_SRN_ETIKETT_LAGERSTAND = 4;
	public final static int REPORT_SRN_ETIKETT_LAGERORT = 5;
	public final static int REPORT_SRN_ETIKETT_GEWICHT = 6;
	public final static int REPORT_SRN_ETIKETT_FA = 7;
	public final static int REPORT_SRN_ETIKETT_CLUSTER = 8;
	public final static int REPORT_SRN_ETIKETT_EQNR = 9;
	public final static int REPORT_SRN_ETIKETT_SRNNR = 10;
	public final static int REPORT_SRN_ETIKETT_LIEFERTERMIN = 11;
	public final static int REPORT_SRN_ETIKETT_PRUEFTERMIN = 12;
	public final static int REPORT_SRN_ETIKETT_STLK_INDEX = 13;
	public final static int REPORT_SRN_ETIKETT_LIEFERANTENNUMMER = 14;
	public final static int REPORT_SRN_ETIKETT_KUNDENNAME = 15;
	public final static int REPORT_SRN_ETIKETT_KUNDENADRESSE = 16;
	public final static int REPORT_SRN_ETIKETT_KOMMENTAR = 17;
	public final static int REPORT_SRN_ETIKETT_LIEFERADRESSE = 18;
	public final static int REPORT_SRN_ETIKETT_ANZAHL_SPALTEN = 19;

	public final static int REPORT_TAETIGKEITSSTATISTIK_AUFTRAGSNUMMER = 0;
	public final static int REPORT_TAETIGKEITSSTATISTIK_KUNDE = 1;
	public final static int REPORT_TAETIGKEITSSTATISTIK_PROJEKT = 2;
	public final static int REPORT_TAETIGKEITSSTATISTIK_ARTIKELNUMMER = 3;
	public final static int REPORT_TAETIGKEITSSTATISTIK_WEITERE_ARTIKELNUMMERN = 4;
	public final static int REPORT_TAETIGKEITSSTATISTIK_AZ_ARTIKEL = 5;
	public final static int REPORT_TAETIGKEITSSTATISTIK_AZ_ARTIKELBEZ = 6;
	public final static int REPORT_TAETIGKEITSSTATISTIK_AZ_SOLL = 7;
	public final static int REPORT_TAETIGKEITSSTATISTIK_AZ_IST = 8;
	public final static int REPORT_TAETIGKEITSSTATISTIK_AZ_KOSTEN = 9;
	public final static int REPORT_TAETIGKEITSSTATISTIK_ANZAHL_SPALTEN = 10;

	public final static int REPORT_VORKALKULATION_IDENT = 0;
	/** Die Bezeichnung kann auch die im AB uebersteuerte Bezeichnung sein */
	public final static int REPORT_VORKALKULATION_BEZEICHNUNG = 1;
	public final static int REPORT_VORKALKULATION_ZUSATZBEZEICHNUNG = 2;
	public final static int REPORT_VORKALKULATION_MENGE = 3;
	public final static int REPORT_VORKALKULATION_EINHEIT = 4;
	/**
	 * Fuer die Zwischensumme muss die Menge der uebergeordneten Position
	 * bekannt sein.
	 */
	public final static int REPORT_VORKALKULATION_MENGE_UEBERGEORDNET = 5;
	/**
	 * Fuer die Zwischensumme muss die Einheit der uebergeordneten Position
	 * bekannt sein.
	 */
	public final static int REPORT_VORKALKULATION_EINHEIT_UEBERGEORDNET = 6;
	/**
	 * Jede Ident Position hat einen manuell eingegebenen Gestehungspreis, der
	 * kursiv angedruckt wird
	 */
	public final static int REPORT_VORKALKULATION_GESTEHUNGSPREIS_MANUELL = 7;
	/**
	 * Der Gestehungspreis eines Artikels, der aus dem Hauptlager des Mandanten
	 * kommt
	 */
	public final static int REPORT_VORKALKULATION_GESTEHUNGSPREIS = 8;
	/**
	 * Jede Ident Position hat einen manuell bestimmten Gestehungswert, der
	 * kursiv angedruckt wird
	 */
	public final static int REPORT_VORKALKULATION_GESTEHUNGSWERT_MANUELL = 9;
	/**
	 * Der Gestehungswert eines Artikels, der aus dem Hauptlager des Mandanten
	 * bestimmt wird
	 */
	public final static int REPORT_VORKALKULATION_GESTEHUNGSWERT = 10;
	public final static int REPORT_VORKALKULATION_VERKAUFSPREIS = 11;
	public final static int REPORT_VORKALKULATION_VERKAUFSWERT = 12;
	public final static int REPORT_VORKALKULATION_POSITIONSART = 13;
	/** Dieser Index dient der Gruppierung zur Bildung von Zwischensummen */
	public final static int REPORT_VORKALKULATION_INDEX_GRUPPE = 14;
	public final static int REPORT_VORKALKULATION_ANZAHL_SPALTEN = 15;

	public final static int REPORT_WIEDERBESCHAFFUNG_IDENTNUMMER = 0;
	public final static int REPORT_WIEDERBESCHAFFUNG_BEZEICHNUNG = 1;
	public final static int REPORT_WIEDERBESCHAFFUNG_KURZBEZEICHNUNG = 2;
	public final static int REPORT_WIEDERBESCHAFFUNG_MENGE = 3;
	public final static int REPORT_WIEDERBESCHAFFUNG_EINHEIT = 4;
	public final static int REPORT_WIEDERBESCHAFFUNG_GESTEHUNGSPREIS = 5;
	public final static int REPORT_WIEDERBESCHAFFUNG_LAGERSTAND = 6;
	public final static int REPORT_WIEDERBESCHAFFUNG_MENGE_BESTELLT = 7;
	public final static int REPORT_WIEDERBESCHAFFUNG_MENGE_RESERVIERT = 8;
	public final static int REPORT_WIEDERBESCHAFFUNG_MENGE_FEHLMENGE = 9;
	public final static int REPORT_WIEDERBESCHAFFUNG_WIEDERBESCHAFFUNGSZEIT = 10;
	public final static int REPORT_WIEDERBESCHAFFUNG_DURCHLAUFZEIT = 11;
	public final static int REPORT_WIEDERBESCHAFFUNG_BEDARF = 12;
	public final static int REPORT_WIEDERBESCHAFFUNG_LAGERND = 13;
	public final static int REPORT_WIEDERBESCHAFFUNG_LIEFERANT = 14;
	public final static int REPORT_WIEDERBESCHAFFUNG_LIEFERANT_ARTIKELNUMMER = 15;
	public final static int REPORT_WIEDERBESCHAFFUNG_LIEFERANT_ARTIKELBEZEICHNUNG = 16;
	public final static int REPORT_WIEDERBESCHAFFUNG_SUMME_SOLLZEITEN = 17;
	public final static int REPORT_WIEDERBESCHAFFUNG_LAGERSTAND_SPERRLAEGER = 18;
	public final static int REPORT_WIEDERBESCHAFFUNG_SETARTIKEL_TYP = 19;
	public final static int REPORT_WIEDERBESCHAFFUNG_ANZAHL_SPALTEN = 20;

	public final static int REPORT_ROLLIERENDEPLANUNG_IDENTNUMMER = 0;
	public final static int REPORT_ROLLIERENDEPLANUNG_BEZEICHNUNG = 1;
	public final static int REPORT_ROLLIERENDEPLANUNG_KURZBEZEICHNUNG = 2;
	public final static int REPORT_ROLLIERENDEPLANUNG_MENGE = 3;
	public final static int REPORT_ROLLIERENDEPLANUNG_MENGE2 = 4;
	public final static int REPORT_ROLLIERENDEPLANUNG_MENGE3 = 5;
	public final static int REPORT_ROLLIERENDEPLANUNG_MENGE4 = 6;
	public final static int REPORT_ROLLIERENDEPLANUNG_MENGE5 = 7;
	public final static int REPORT_ROLLIERENDEPLANUNG_MENGE6 = 8;
	public final static int REPORT_ROLLIERENDEPLANUNG_MENGE7 = 9;
	public final static int REPORT_ROLLIERENDEPLANUNG_MENGE8 = 10;
	public final static int REPORT_ROLLIERENDEPLANUNG_MENGE9 = 11;
	public final static int REPORT_ROLLIERENDEPLANUNG_MENGE10 = 12;
	public final static int REPORT_ROLLIERENDEPLANUNG_MENGE11 = 13;
	public final static int REPORT_ROLLIERENDEPLANUNG_MENGE12 = 14;
	public final static int REPORT_ROLLIERENDEPLANUNG_MENGE13 = 15;
	public final static int REPORT_ROLLIERENDEPLANUNG_EINHEIT = 16;
	public final static int REPORT_ROLLIERENDEPLANUNG_GESTEHUNGSPREIS = 17;
	public final static int REPORT_ROLLIERENDEPLANUNG_LAGERSTAND = 18;
	public final static int REPORT_ROLLIERENDEPLANUNG_MENGE_BESTELLT = 19;
	public final static int REPORT_ROLLIERENDEPLANUNG_MENGE_RESERVIERT = 20;
	public final static int REPORT_ROLLIERENDEPLANUNG_MENGE_FEHLMENGE = 21;
	public final static int REPORT_ROLLIERENDEPLANUNG_WIEDERBESCHAFFUNGSZEIT = 22;
	public final static int REPORT_ROLLIERENDEPLANUNG_DURCHLAUFZEIT = 23;
	public final static int REPORT_ROLLIERENDEPLANUNG_LIEFERANT = 24;
	public final static int REPORT_ROLLIERENDEPLANUNG_LIEFERANT_ARTIKELNUMMER = 25;
	public final static int REPORT_ROLLIERENDEPLANUNG_LIEFERANT_ARTIKELBEZEICHNUNG = 26;
	public final static int REPORT_ROLLIERENDEPLANUNG_LAGERSTAND_SPERRLAEGER = 27;
	public final static int REPORT_ROLLIERENDEPLANUNG_ANZAHL_SPALTEN = 28;

	public final static int REPORT_ERFUELLUNGSGRAD_VERTRETER = 0;
	public final static int REPORT_ERFUELLUNGSGRAD_AUFTRAGSNUMMER = 1;
	public final static int REPORT_ERFUELLUNGSGRAD_PROJEKT = 2;
	public final static int REPORT_ERFUELLUNGSGRAD_ZUSATZBELEG = 3;
	public final static int REPORT_ERFUELLUNGSGRAD_AUFTRAGSWERT = 4;
	public final static int REPORT_ERFUELLUNGSGRAD_RECHNUNGSWERT = 5;
	public final static int REPORT_ERFUELLUNGSGRAD_ZAHLUNGSGRAD = 6;
	public final static int REPORT_ERFUELLUNGSGRAD_GEPLANTESTUNDEN = 7;
	public final static int REPORT_ERFUELLUNGSGRAD_GELEISTETESTUNDEN = 8;
	public final static int REPORT_ERFUELLUNGSGRAD_WERTGELEISTETESTUNDEN = 9;
	public final static int REPORT_ERFUELLUNGSGRAD_THEORETISCHERFUELLT = 10;
	public final static int REPORT_ERFUELLUNGSGRAD_TATSAECHLICHERFUELLT = 11;
	public final static int REPORT_ERFUELLUNGSGRAD_SALDO = 12;
	public final static int REPORT_ERFUELLUNGSGRAD_VORLEISTUNG = 13;
	public final static int REPORT_ERFUELLUNGSGRAD_SUBREPORT_PERSONALZEITEN = 14;
	public final static int REPORT_ERFUELLUNGSGRAD_AUFTRAGSADRESSE_NAME1 = 15;
	public final static int REPORT_ERFUELLUNGSGRAD_AUFTRAGSADRESSE_NAME2 = 16;
	public final static int REPORT_ERFUELLUNGSGRAD_AUFTRAGSADRESSE_STRASSE = 17;
	public final static int REPORT_ERFUELLUNGSGRAD_AUFTRAGSADRESSE_LKZ = 18;
	public final static int REPORT_ERFUELLUNGSGRAD_AUFTRAGSADRESSE_PLZ = 19;
	public final static int REPORT_ERFUELLUNGSGRAD_AUFTRAGSADRESSE_ORT = 20;
	public final static int REPORT_ERFUELLUNGSGRAD_RECHNUNGSADRESSE = 21;
	public final static int REPORT_ERFUELLUNGSGRAD_LIEFERADRESSE = 22;

	public final static int REPORT_ERFUELLUNGSJOURNAL_ARTIKELNUMMER = 0;
	public final static int REPORT_ERFUELLUNGSJOURNAL_ARTIKELBEZEICHNUNG = 1;
	public final static int REPORT_ERFUELLUNGSJOURNAL_RAHMEN_GEPLANT = 2;
	public final static int REPORT_ERFUELLUNGSJOURNAL_GELIEFERT = 3;
	public final static int REPORT_ERFUELLUNGSJOURNAL_VKPREIS = 4;
	public final static int REPORT_ERFUELLUNGSJOURNAL_EINSTANDSPREIS = 5;
	public final static int REPORT_ERFUELLUNGSJOURNAL_GESTPREIS = 6;
	public final static int REPORT_ERFUELLUNGSJOURNAL_LAGERSTAND_ZUM_BIS_ZEITPUNKT = 7;
	public final static int REPORT_ERFUELLUNGSJOURNAL_VERFUEGBAR_ZUM_BIS_ZEITPUNKT = 8;
	public final static int REPORT_ERFUELLUNGSJOURNAL_EINGEKAUFT = 9;
	public final static int REPORT_ERFUELLUNGSJOURNAL_ABGELIEFERT = 10;
	public final static int REPORT_ERFUELLUNGSJOURNAL_ANZAHL_SPALTEN = 11;

	public final static int REPORT_ALLE_CNR = 0;
	public final static int REPORT_ALLE_KUNDE = 1;
	public final static int REPORT_ALLE_KOSTENSTELLE = 2;
	public final static int REPORT_ALLE_DATUM = 3;
	public final static int REPORT_ALLE_VERTRETER = 4;
	public final static int REPORT_ALLE_STATUS = 5;
	public final static int REPORT_ALLE_WERT = 6;
	public final static int REPORT_ALLE_AUFTRAGART = 7;
	public final static int REPORT_ALLE_PROJEKT = 8;
	public final static int REPORT_ALLE_WAEHRUNG = 9;
	public final static int REPORT_ALLE_KURS = 10;
	public final static int REPORT_ALLE_BESTELLNUMMER = 11;
	public final static int REPORT_ALLE_BESTELLTERMIN = 12;
	public final static int REPORT_ALLE_LIEFERTERMIN = 13;
	public final static int REPORT_ALLE_FINALTERMIN = 14;
	public final static int REPORT_ALLE_POENALE = 15;
	public final static int REPORT_ALLE_ROHS = 16;
	public final static int REPORT_ALLE_TEILLIEFERBAR = 17;
	public final static int REPORT_ALLE_UNVERBINDLICH = 18;
	public final static int REPORT_ALLE_ADRESSE = 19;
	public final static int REPORT_ALLE_LIEFERADRESSE = 20;
	public final static int REPORT_ALLE_RECHNUNGSADRESSE = 21;
	public final static int REPORT_ALLE_ANSPRECHPARTNER = 22;
	public final static int REPORT_ALLE_LAENDERART = 23;
	public final static int REPORT_ALLE_ANZAHL_SPALTEN = 24;

	public final static int REPORT_ERLEDIGT_CNR = 0;
	public final static int REPORT_ERLEDIGT_KUNDE = 1;
	public final static int REPORT_ERLEDIGT_KOSTENSTELLE = 2;
	public final static int REPORT_ERLEDIGT_DATUM = 3;
	public final static int REPORT_ERLEDIGT_VERTRETER = 4;
	public final static int REPORT_ERLEDIGT_STATUS = 5;
	public final static int REPORT_ERLEDIGT_WERT = 6;
	public final static int REPORT_ERLEDIGT_AUFTRAGART = 7;
	public final static int REPORT_ERLEDIGT_PROJEKT = 8;
	public final static int REPORT_ERLEDIGT_WAEHRUNG = 9;
	public final static int REPORT_ERLEDIGT_KURS = 10;
	public final static int REPORT_ERLEDIGT_BESTELLNUMMER = 11;
	public final static int REPORT_ERLEDIGT_BESTELLTERMIN = 12;
	public final static int REPORT_ERLEDIGT_LIEFERTERMIN = 13;
	public final static int REPORT_ERLEDIGT_FINALTERMIN = 14;
	public final static int REPORT_ERLEDIGT_POENALE = 15;
	public final static int REPORT_ERLEDIGT_ROHS = 16;
	public final static int REPORT_ERLEDIGT_TEILLIEFERBAR = 17;
	public final static int REPORT_ERLEDIGT_UNVERBINDLICH = 18;
	public final static int REPORT_ERLEDIGT_ADRESSE = 19;
	public final static int REPORT_ERLEDIGT_LIEFERADRESSE = 20;
	public final static int REPORT_ERLEDIGT_RECHNUNGSADRESSE = 21;
	public final static int REPORT_ERLEDIGT_ANSPRECHPARTNER = 22;
	public final static int REPORT_ERLEDIGT_ERLEDIGT_AM = 23;
	public final static int REPORT_ERLEDIGT_ERLEDIGT_DURCH = 24;
	public final static int REPORT_ERLEDIGT_ANZAHL_SPALTEN = 25;

	public final static int REPORT_RAHMENUEBERSICHT_AUFRAGART = 0;
	public final static int REPORT_RAHMENUEBERSICHT_AUFRAGNR = 1;
	public final static int REPORT_RAHMENUEBERSICHT_BELEGDATUM = 2;
	public final static int REPORT_RAHMENUEBERSICHT_AUFTRAGWERT = 3;
	public final static int REPORT_RAHMENUEBERSICHT_LIEFERTERMIN = 4;
	public final static int REPORT_RAHMENUEBERSICHT_ARTIKELNUMMER = 5;
	public final static int REPORT_RAHMENUEBERSICHT_ARTIKELBEZEICHNUNG = 6;
	public final static int REPORT_RAHMENUEBERSICHT_MENGE = 7;
	public final static int REPORT_RAHMENUEBERSICHT_KUNDENBESTELLNUMMER = 8;
	public final static int REPORT_RAHMENUEBERSICHT_PREIS = 9;
	public final static int REPORT_RAHMENUEBERSICHT_LIEFERSCHEIN = 10;
	public final static int REPORT_RAHMENUEBERSICHT_RECHNUNG = 11;
	public final static int REPORT_RAHMENUEBERSICHT_ANZAHL_SPALTEN = 12;

	/** WH 22.02.06 vorerst Punkt, damit man die Ebene erkennen kann */
	public final static String REPORT_VORKALKULATION_ZEICHEN_FUER_HANDEINGABE = ".";

	public final static String AUFTRAG_EIGENSCHAFTEN_FA = "Fa";
	public final static String AUFTRAG_EIGENSCHAFTEN_CLUSTER = "Cluster";
	public final static String AUFTRAG_EIGENSCHAFTEN_EQNR = "Equipmentnr";

	public JasperPrintLP printAuftragOffene(ReportJournalKriterienDto krit,
			Date dStichtag, Boolean bSortierungNachLiefertermin,
			Boolean bInternenKommentarDrucken, Integer iArt,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printAuftraegeErledigt(
			ReportJournalKriterienDto kritDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printAuftragOffeneDetails(
			ReportJournalKriterienDto krit, Date dStichtag,
			Boolean bSortierungNachLiefertermin,
			Boolean bInternenKommentarDrucken, Integer artikelklasseIId,
			Integer artikelgruppeIId, String artikelCNrVon,
			String artikelCNrBis, String projektCBezeichnung, Integer iArt,
			boolean bLagerstandsdetail, boolean bMitAngelegten,
			TheClientDto theClientDto);

	public JasperPrintLP printAuftragOffenePositionen(
			ReportJournalKriterienDto krit, Date dStichtag,
			Boolean bSortierungNachLiefertermin, Boolean bOhnePositionen,
			Boolean bSortierungNachAbliefertermin,
			Integer[] fertigungsgruppeIId, Integer iArt,
			Boolean bSortierungNurLiefertermin, String sReportname,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP[] printAuftragbestaetigung(Integer iIdAuftragI,
			Integer iAnzahlKopienI, Boolean bMitLogo, String sReportname,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printAuftragPackliste(Integer iIdAuftragI,
			String sReportName, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printAuftragszeiten(Integer iIdAuftragI,
			boolean bSortierNachPerson, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printAuftragSrnnrnEtikett(Integer iIdAuftragI,
			Integer iIdAuftragpositionI, String cAktuellerReport,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printErfuellungsgrad(java.sql.Timestamp tStichtag,
			Integer personalIId_Vertreter, Integer kostenstelleIId,
			boolean bMitWiederholenden, TheClientDto theClientDto)
			throws RemoteException;

	public JasperPrintLP printRahmenuebersicht(Integer auftragIId,
			TheClientDto theClientDto);

	public JasperPrintLP printAuftragstatistik(java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, boolean bAuftragsdatum,
			Integer kundeIId_Auftragsadresse,
			Integer kundeIId_Rechnungsadresse, Integer auftragIId,
			String cProjekt, String cBestellnummer, int iOptionSortierung,
			boolean bArbeitszeitVerdichtet, java.sql.Timestamp tStichtag,
			Integer projektIId, TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printTaetigkeitsstatistik(java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, PartnerklasseDto partnerklasse,
			TheClientDto theClientDto);

	public JasperPrintLP printRollierendeplanung(Integer auftragIId,
			boolean bSortiertNachLieferant, TheClientDto theClientDto);

	public JasperPrintLP printAuftragVorkalkulation(Integer iIdAuftragI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printErfuellungsjournal(
			Integer auftragIIdRahmenauftrag, java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, TheClientDto theClientDto);

	public JasperPrintLP printAuftragOffeneOhneDetail(
			ReportJournalKriterienDto krit, Date dStichtag,
			Boolean bSortierungNachLiefertermin,
			Boolean bInternenKommentarDrucken, Integer iArt,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printWiederbeschaffung(Integer[] aIIdArtikelI,
			BigDecimal[] aBdMenge, String[] artikelsetType,
			Map<String, Object> mapReportParameterI,
			boolean bSortiertNachLieferant, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printVerfuegbarkeitspruefung(Integer iIdAuftragI,
			boolean bSortiertNachLieferant, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Hashtable<?, ?> getAuftragEigenschaften(Integer iIdAuftragI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printAuftragAlle(ReportJournalKriterienDto kritDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;
	
	public JasperPrintLP printAuftragspositionsetikett(
			Integer auftragpositionIId, TheClientDto theClientDto);
	
}
