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
package com.lp.server.auftrag.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import javax.ejb.Remote;

import com.lp.server.partner.service.PartnerklasseDto;
import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.DatumsfilterVonBis;
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
	public final static String REPORT_ZEITBESTAETIGUNG = "auft_zeitbestaetigung.jasper";
	public final static String REPORT_ZEITBESTAETIGUNG_EMAIL = "auft_zeitbestaetigung_email";
	public final static String REPORT_ZEITBESTAETIGUNG_UNTERSCHRIFT = "auft_zeitbestaetigung_unterschrift.jasper";
	public final static String REPORT_TAETIGKEITSSTATISTIK = "auft_taetigkeitsstatistik.jasper";
	public final static String REPORT_RAHMENUEBERSICHT = "auft_rahmenuebersicht.jasper";
	public final static String REPORT_AUFTRAGSPOSITIONSETIKETT = "auft_auftragspositionsetikett.jasper";
	public final static String REPORT_AUFTRAGSUEBERSICHT = "auft_auftragsuebersicht.jasper";
	public final static String REPORT_ERFOLGSSTATUS = "auft_erfolgsstatus.jasper";
	public final static String REPORT_AUFTRAGSETIKETT = "auft_auftragsetikett.jasper";
	public final static String REPORT_LIEFERPLAN = "auft_lieferplan.jasper";
	public final static String REPORT_AUSZULIEFERNDE_POSITIONEN = "auft_auszuliefernde_positionen.jasper";
	public final static String REPORT_KOMMISSIONIERUNG = "auft_kommissionierung.jasper";
	public final static String REPORT_TEILNEHMER = "auft_teilnehmer.jasper";
	public final static String REPORT_AUFTRAGUMSATZSTATISTIK = "auft_umsatzstatistik.jasper";

	public final static int REPORT_AUFTRAGSTATISTIK_OPTION_DATUM_BELEGDATUM = 1;
	public final static int REPORT_AUFTRAGSTATISTIK_OPTION_DATUM_ERLEDIGUNGSDATUM = 2;
	public final static int REPORT_AUFTRAGSTATISTIK_OPTION_DATUM_LIEFERTERMIN_BIS_ZUM = 3;
	public final static int REPORT_AUFTRAGSTATISTIK_OPTION_DATUM_STICHTAG = 4;

	public final static int REPORT_AUFTRAGSTATISTIK_SORTIERUNG_AUFTRAG = 1;
	public final static int REPORT_AUFTRAGSTATISTIK_SORTIERUNG_KUNDE_AUFTRAGSADRESSE = 2;
	public final static int REPORT_AUFTRAGSTATISTIK_SORTIERUNG_KUNDE_RECHNUNGSADRESSE = 3;
	public final static int REPORT_AUFTRAGSTATISTIK_SORTIERUNG_BESTELLNUMMER = 4;
	public final static int REPORT_AUFTRAGSTATISTIK_SORTIERUNG_PROJEKT = 5;
	public final static int REPORT_AUFTRAGSTATISTIK_SORTIERUNG_FUEHRENDER_ARTIKEL = 6;
	public final static int REPORT_AUFTRAGSTATISTIK_SORTIERUNG_PROJEKTLEITER = 7;

	public final static String REPORT_AUFTRAG_VORKALKULATION = "auft_vorkalkulation.jasper";
	public final static String REPORT_AUFTRAG_TEIL_LIEFERBAR = "auft_teil_lieferbar.jasper";
	public final static String REPORT_AUFTRAG_OFFENE_DETAILS = "auft_auftrag_offene_details.jasper";
	public final static String REPORT_AUFTRAG_OFFENE_POSITIONEN = "auft_auftrag_offene_positionen.jasper";

	public final static String REPORT_AUFTRAG_WIEDERBESCHAFFUNG = "auft_wiederbeschaffung.jasper";
	public final static String REPORT_AUFTRAG_ROLLIERENDEPLANUNG = "auft_rollierendeplanung.jasper";
	public final static String REPORT_AUFTRAG_VERFUEGBARKEIT = "auft_verfuegbarkeit.jasper";
	public final static String REPORT_AUFTRAG_ERFUELLUNGSGRAD = "auft_erfuellungsgrad.jasper";
	public final static String REPORT_AUFTRAG_ERFUELLUNGSJOURNAL = "auft_erfuellungsjournal.jasper";
	public final static String REPORT_AUFTRAG_PROJEKTBLATT = "auft_projektblatt.jasper";
	public final static String REPORT_AUFTRAG_MEILENSTEINE = "auft_meilensteine.jasper";
	public final static String REPORT_AUFTRAG_PLANSTUNDEN = "auft_planstunden.jasper";
	public final static String REPORT_AUFTRAG_MATERIALBEDARFE = "auft_materialbedarfe.jasper";

	public final static int REPORT_AUFTRAG_OFFENE_ARTUNVERBINDLICH_ALLE = 1;
	public final static int REPORT_AUFTRAG_OFFENE_ARTUNVERBINDLICH_NUR_UNVERBINDLICHE = 2;
	public final static int REPORT_AUFTRAG_OFFENE_ARTUNVERBINDLICH_OHNE_UNVERBINDLICHE = 3;

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
	public final static int REPORT_AUFTRAG_OFFENE_OD_AUFTRAGSART = 16;
	public final static int REPORT_AUFTRAG_OFFENE_OD_RAHMENWERTOFFEN = 17;
	public final static int REPORT_AUFTRAG_OFFENE_OD_RAHMENAUFTRAG = 18;
	public final static int REPORT_AUFTRAG_OFFENE_OD_LIEFERADRESSE = 19;
	public final static int REPORT_AUFTRAG_OFFENE_OD_LIEFERADRESSE_ADRESSE = 20;
	public final static int REPORT_AUFTRAG_OFFENE_OD_LIEFERADRESSE_KURZBEZEICHNUNG = 21;
	public final static int REPORT_AUFTRAG_OFFENE_OD_AUFTRAGSFREIGABE_ZEITPUNKT = 22;
	public final static int REPORT_AUFTRAG_OFFENE_OD_AUFTRAGSFREIGABE_PERSON = 23;
	public final static int REPORT_AUFTRAG_OFFENE_OD_BESTELLDATUM = 24;
	public final static int REPORT_AUFTRAG_OFFENE_OD_VERRECHENBAR = 25;
	public final static int REPORT_AUFTRAG_OFFENE_OD_WUNSCHTERMIN = 26;
	public final static int REPORT_AUFTRAG_OFFENE_OD_WIEDERHOLUNGSINTERVALL = 27;
	public final static int REPORT_AUFTRAG_OFFENE_OD_LAUFTERMIN_VON = 28;
	public final static int REPORT_AUFTRAG_OFFENE_OD_LAUFTERMIN_BIS = 29;
	public final static int REPORT_AUFTRAG_OFFENE_OD_UNTERKOSTENSTELLE = 30;
	public final static int REPORT_AUFTRAG_OFFENE_OD_KOSTENSTELLEBEZEICHNUNG = 31;
	public final static int REPORT_AUFTRAG_OFFENE_OD_FINALTERMIN = 32;
	public final static int REPORT_AUFTRAG_OFFENE_OD_STATUS = 33;
	public final static int REPORT_AUFTRAG_OFFENE_OD_ANZAHL_SPALTEN = 34;

	public final static int REPORT_LIEFERPLAN_VERTRETER = 0;
	public final static int REPORT_LIEFERPLAN_KUNDE_NAME = 1;
	public final static int REPORT_LIEFERPLAN_KUNDE_KBEZ = 2;
	public final static int REPORT_LIEFERPLAN_KUNDE_LKZ = 3;
	public final static int REPORT_LIEFERPLAN_KUNDE_PLZ = 4;
	public final static int REPORT_LIEFERPLAN_KUNDE_ORT = 5;
	public final static int REPORT_LIEFERPLAN_AUFTRAG = 6;
	public final static int REPORT_LIEFERPLAN_AUFTRAGART = 7;
	public final static int REPORT_LIEFERPLAN_ARTIKELNUMMER = 8;
	public final static int REPORT_LIEFERPLAN_BEZEICHNUNG = 9;
	public final static int REPORT_LIEFERPLAN_ZUSATZBEZEICHNUNG = 10;
	public final static int REPORT_LIEFERPLAN_ZUSATZBEZEICHNUNG2 = 11;
	public final static int REPORT_LIEFERPLAN_KURZBEZEICHNUNG = 12;
	public final static int REPORT_LIEFERPLAN_OFFENE_MENGE = 13;
	public final static int REPORT_LIEFERPLAN_VKPREIS = 14;
	public final static int REPORT_LIEFERPLAN_INFERTIGUNG = 15;
	public final static int REPORT_LIEFERPLAN_BESTELLT = 16;
	public final static int REPORT_LIEFERPLAN_RAHMENBESTELLT = 17;
	public final static int REPORT_LIEFERPLAN_FEHLMENGE = 18;
	public final static int REPORT_LIEFERPLAN_RESERVIERT = 19;
	public final static int REPORT_LIEFERPLAN_LAGERSTAND = 20;
	public final static int REPORT_LIEFERPLAN_EINHEIT = 21;
	public final static int REPORT_LIEFERPLAN_SUBREPORT_TERMINE = 22;
	public final static int REPORT_LIEFERPLAN_RUECKSTAND = 23;
	public final static int REPORT_LIEFERPLAN_DANACH = 24;
	public final static int REPORT_LIEFERPLAN_BESTELLNUMMER = 25;
	public final static int REPORT_LIEFERPLAN_ANZAHL_SPALTEN = 26;

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
	public final static int REPORT_AUFTRAG_OFFENE_AUFTRAGSART = 29;
	public final static int REPORT_AUFTRAG_OFFENE_AUFTRAGLIEFERADRESSE = 30;
	public final static int REPORT_AUFTRAG_OFFENE_AUFTRAGLIEFERADRESSE_ADRESSE = 31;
	public final static int REPORT_AUFTRAG_OFFENE_SETARTIKEL_TYP = 32;
	public final static int REPORT_AUFTRAG_OFFENE_AUFTRAGLIEFERADRESSE_KURZBEZEICHNUNG = 33;
	public final static int REPORT_AUFTRAG_OFFENE_AUFTRAGSFREIGABE_ZEITPUNKT = 34;
	public final static int REPORT_AUFTRAG_OFFENE_AUFTRAGSFREIGABE_PERSON = 35;
	public final static int REPORT_AUFTRAG_OFFENE_BESTELLDATUM = 36;
	public final static int REPORT_AUFTRAG_OFFENE_VERRECHENBAR = 37;
	public final static int REPORT_AUFTRAG_OFFENE_AUFTRAGWUNSCHTERMIN = 38;
	public final static int REPORT_AUFTRAG_OFFENE_WIEDERHOLUNGSINTERVALL = 39;
	public final static int REPORT_AUFTRAG_OFFENE_LAUFTERMIN_VON = 40;
	public final static int REPORT_AUFTRAG_OFFENE_LAUFTERMIN_BIS = 41;
	public final static int REPORT_AUFTRAG_OFFENE_STATUS = 42;
	public final static int REPORT_AUFTRAG_OFFENE_ARTIKELREFERENZNUMMER = 43;
	public final static int REPORT_AUFTRAG_OFFENE_ARTIKELKURZBEZEICHNUNG = 44;
	public final static int REPORT_AUFTRAG_OFFENE_ARTIKEL_LAGERBEWIRTSCHAFTET = 45;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONSSTATUS = 46;
	public final static int REPORT_AUFTRAG_OFFENE_ANZAHL_SPALTEN = 47;

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
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_POSITIONSTERMIN_OHNE_LIEFERDAUER = 61;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_SETARTIKEL_TYP = 62;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_KUNDELIEFERADRESSE_KURZBEZEICHNUNG = 63;

	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKEL_REFERENZNUMMER = 64;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKEL_REVISION = 65;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKEL_INDEX = 66;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKEL_ZUSATZBEZEICHNUNG = 67;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKEL_ZUSATZBEZEICHNUNG2 = 68;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKEL_KURZBEZEICHNUNG = 69;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGSFREIGABE_ZEITPUNKT = 70;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGSFREIGABE_PERSON = 71;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_VERRECHENBAR = 72;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_AUFTRAGWUNSCHTERMIN = 73;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKEL_LIEFERGRUPPE = 74;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_FUER_DIE_STKL_GIBT_ES_BEREITS_EIN_LOS = 75;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_WIEDERHOLUNGSINTERVALL = 76;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_LAUFTERMIN_VON = 77;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_LAUFTERMIN_BIS = 78;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_STATUS = 79;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_ARTIKEL_LAGERBEWIRTSCHAFTET = 80;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_STKL_EBENE = 81;
	public final static int REPORT_AUFTRAG_OFFENE_DETAILS_ANZAHL_SPALTEN = 82;

	public final static int REPORT_AUSZULIEFERNDE_POSITIONEN_BELEGART = 0;
	public final static int REPORT_AUSZULIEFERNDE_POSITIONEN_BELEGNUMMER = 1;
	public final static int REPORT_AUSZULIEFERNDE_POSITIONEN_KUNDE = 2;
	public final static int REPORT_AUSZULIEFERNDE_POSITIONEN_LIEFERADRESSE = 3;
	public final static int REPORT_AUSZULIEFERNDE_POSITIONEN_SORTIERUNG = 4;
	public final static int REPORT_AUSZULIEFERNDE_POSITIONEN_ARTIKELNUMMER = 5;
	public final static int REPORT_AUSZULIEFERNDE_POSITIONEN_ARTIKELBEZEICHNUNG = 6;
	public final static int REPORT_AUSZULIEFERNDE_POSITIONEN_ARTIKELZUSATZBEZEICHNUNG = 7;
	public final static int REPORT_AUSZULIEFERNDE_POSITIONEN_ARTIKELZUSATZBEZEICHNUNG2 = 8;
	public final static int REPORT_AUSZULIEFERNDE_POSITIONEN_ARTIKELEINHEIT = 9;
	public final static int REPORT_AUSZULIEFERNDE_POSITIONEN_BESTELLNUMMER = 10;
	public final static int REPORT_AUSZULIEFERNDE_POSITIONEN_PROJEKT = 11;
	public final static int REPORT_AUSZULIEFERNDE_POSITIONEN_AUSLIEFERTERMIN = 12;
	public final static int REPORT_AUSZULIEFERNDE_POSITIONEN_FIKTIVER_LAGERSTAND_ZUM_AUSLIEFERTERMIN = 13;
	public final static int REPORT_AUSZULIEFERNDE_POSITIONEN_LAGERSTAND = 14;
	public final static int REPORT_AUSZULIEFERNDE_POSITIONEN_AUFTRAGSFREIGABE_ZEITPUNKT = 15;
	public final static int REPORT_AUSZULIEFERNDE_POSITIONEN_AUFTRAGSFREIGABE_PERSON = 16;
	public final static int REPORT_AUSZULIEFERNDE_POSITIONEN_MENGE = 17;
	public final static int REPORT_AUSZULIEFERNDE_POSITIONEN_OFFEN = 18;
	public final static int REPORT_AUSZULIEFERNDE_POSITIONEN_POSITIONSTERMIN = 19;
	public final static int REPORT_AUSZULIEFERNDE_POSITIONEN_SUBREPORT_OFFENE_BESTELLUNGEN = 20;
	public final static int REPORT_AUSZULIEFERNDE_POSITIONEN_ARTIKELREFERENZNUMMER = 21;
	public final static int REPORT_AUSZULIEFERNDE_POSITIONEN_LAGERPLATZ = 22;
	public final static int REPORT_AUSZULIEFERNDE_POSITIONEN_ANZAHL_SPALTEN = 23;

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
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_KUNDELIEFERADRESSE_ADRESSE = 41;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_KUNDELIEFERADRESSE_KURZBEZEICHNUNG = 42;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_BESTELLNUMMER_RAHMENAUFTRAG = 43;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_BESTELLUNMMERN = 44;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGSFREIGABE_ZEITPUNKT = 45;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGSFREIGABE_PERSON = 46;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_POSITIONSNUMMER = 47;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_VERRECHENBAR = 48;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGWUNSCHTERMIN = 49;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_WIEDERHOLUNGSINTERVALL = 50;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_LAUFTERMIN_VON = 51;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_LAUFTERMIN_BIS = 52;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_SETARTIKEL_TYP = 53;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_AUFTRAGPOSITION_I_ID = 54;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELREFERENZNUMMER = 55;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_SUBREPORT_ANZAHLUNGSRECHNUNGEN = 56;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKEL_LAGERBEWIRTSCHAFTET = 57;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_KOSTENSTELLECNR = 58;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_STKL_ART = 59;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKELART = 60;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_ARTIKEL_KALKULATORISCH = 61;
	public final static int REPORT_AUFTRAG_OFFENE_POSITIONEN_ANZAHL_SPALTEN = 62;
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
	public final static int REPORT_AUFTRAGBESTAETIGUNG_ZWSPOSPREISDRUCKEN = 70;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_RAHMENMENGE = 71;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_ABGERUFENE_MENGE = 72;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_LETZTER_ABRUF = 73;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_GEWICHT = 74;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_URSPRUNGSLAND = 75;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_WARENVERKEHRSNUMMER = 76;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_MATERIAL_AUS_KUNDEMATERIAL = 77;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_MATERIALBASIS_AUS_KUNDEMATERIAL = 78;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_AUFSCHLAG_BETRAG = 79;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_AUFSCHLAG_PROZENT = 80;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_ZWSNETTOSUMMEN = 81;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_ZWSTEXTE = 82;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_SUBREPORT_BEWEGUNGSVORSCHAU = 83;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_ARTIKEL_PRAEFERENZBEGUENSTIGT = 84;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_KUNDEARTIKELBEZEICHNUNG = 85;
	public final static int REPORT_AUFTRAGBESTAETIGUNG_ANZAHL_SPALTEN = 86;

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
	public final static int REPORT_STATISTIK_REISE_PARTNER = 48;
	public final static int REPORT_STATISTIK_REISE_KOMMENTAR = 49;
	public final static int REPORT_STATISTIK_REISE_MITARBEITER = 50;
	public final static int REPORT_STATISTIK_REISE_VON = 51;
	public final static int REPORT_STATISTIK_REISE_BIS = 52;
	public final static int REPORT_STATISTIK_ER_EINGANGSRECHNUNGSART = 53;
	public final static int REPORT_STATISTIK_ER_AUFTRAGSZUORDNUNG_KEINE_AUFTRAGSWERTUNG = 54;
	public final static int REPORT_STATISTIK_RECHNUNGSART = 55;
	public final static int REPORT_STATISTIK_LOSNUMMER_LI_QUELLE = 56;
	public final static int REPORT_STATISTIK_ZAHLBETRAG = 57;
	public final static int REPORT_STATISTIK_BELEGSTATUS = 58;
	public final static int REPORT_STATISTIK_ER_SCHLUSSRECHNUNG_NR = 59;
	// public final static int REPORT_STATISTIK_DIAETENAUSSCRIPT = 60;
	// public final static int REPORT_REISEZEITEN_SCRIPTNAME_INTERNAL = 61;
	public final static int REPORT_STATISTIK_PROJEKTLEITER = 60;
	public final static int REPORT_STATISTIK_LOS_BEGINN = 61;
	public final static int REPORT_STATISTIK_LOS_ENDE = 62;
	public final static int REPORT_STATISTIK_LOS_FERTIGUNGSGRUPPE = 63;
	public final static int REPORT_STATISTIK_EINSTANDSWERTMATERIALIST = 64;
	public final static int REPORT_STATISTIK_KOSTENSTELLE = 65;
	public final static int REPORT_STATISTIK_KOSTENSTELLE_BEZEICHNUNG = 66;
	public final static int REPORT_STATISTIK_LOS_KOSTENSTELLE = 67;
	public final static int REPORT_STATISTIK_EINSTANDSWERT_GESAMT = 68;
	public final static int REPORT_STATISTIK_EINSTANDSWERT_HANDEINGABEN = 69;
	public final static int REPORT_STATISTIK_BESTELLWERT_GESAMT = 70;
	public final static int REPORT_STATISTIK_LOS_ABLIEFERMENGE = 71;
	public final static int REPORT_STATISTIK_ANZAHL_SPALTEN = 72;

	public final static int REPORT_ERFOLGSSTATUS_AUFTRAG = 0;
	public final static int REPORT_ERFOLGSSTATUS_PROJEKT = 1;
	public final static int REPORT_ERFOLGSSTATUS_KUNDE = 2;
	public final static int REPORT_ERFOLGSSTATUS_PROJEKTLEITER = 3;
	public final static int REPORT_ERFOLGSSTATUS_BELEGDATUM = 4;
	public final static int REPORT_ERFOLGSSTATUS_SUMME_FREIE_RECHNUNGEN = 5;
	public final static int REPORT_ERFOLGSSTATUS_SUMME_FREIE_RECHNUNGEN_OFFEN = 6;
	public final static int REPORT_ERFOLGSSTATUS_SUMME_FREIE_ANZAHLUNGEN = 7;
	public final static int REPORT_ERFOLGSSTATUS_DATUM_SCHLUSSRECHNUNG = 8;
	public final static int REPORT_ERFOLGSSTATUS_SUMME_SCHLUSSRECHNUNG = 9;
	public final static int REPORT_ERFOLGSSTATUS_SUMME_SCHLUSSRECHNUNG_OFFEN = 10;
	public final static int REPORT_ERFOLGSSTATUS_SUMME_FREIE_EINGANGSRECHNUNGEN_MIT_AUFTRAGSBEZUG = 11;
	public final static int REPORT_ERFOLGSSTATUS_SUMME_FREIE_EINGANGSRECHNUNGEN_MIT_AUFTRAGSBEZUG_OFFEN = 12;
	public final static int REPORT_ERFOLGSSTATUS_SUMME_ANZAHLUNGSEINGANGSRECHNUNGEN_NICHT_IN_SCHLUSSRECHNUNG = 13;
	public final static int REPORT_ERFOLGSSTATUS_SUMME_SCHLUSS_EINGANGSRECHNUNGEN = 14;
	public final static int REPORT_ERFOLGSSTATUS_SUMME_SCHLUSS_EINGANGSRECHNUNGEN_OFFEN = 15;
	public final static int REPORT_ERFOLGSSTATUS_SUMME_MATERIALEINSATZKOSTEN = 16;
	public final static int REPORT_ERFOLGSSTATUS_SUMME_AZEINSATZKOSTEN = 17;
	public final static int REPORT_ERFOLGSSTATUS_SUMME_MASCHINENEINSATZKOSTEN = 18;
	public final static int REPORT_ERFOLGSSTATUS_SUMME_ARBEITSTUNDEN = 19;
	public final static int REPORT_ERFOLGSSTATUS_SUMME_MASCHINENSTUNDEN = 20;
	public final static int REPORT_ERFOLGSSTATUS_SUMME_REISEKOSTEN = 21;
	public final static int REPORT_ERFOLGSSTATUS_SUMME_HANDELSWARE = 22;
	public final static int REPORT_ERFOLGSSTATUS_AUFTRAGSWERT = 23;
	public final static int REPORT_ERFOLGSSTATUS_AUFTRAGSART = 24;
	public final static int REPORT_ERFOLGSSTATUS_ANZAHL_SPALTEN = 25;

	public final static int REPORT_ERFOLGSSTATUS_SORTIERUNG_AUFTRAG = 1;
	public final static int REPORT_ERFOLGSSTATUS_SORTIERUNG_KUNDE = 2;
	public final static int REPORT_ERFOLGSSTATUS_SORTIERUNG_PROJEKTLEITER = 3;

	public final static int REPORT_AUSZULIEFERNDE_POSITIONEN_SORTIERUNG_KUNDE_AUSLIEFERTEMRIN_ARTIKEL = 1;
	public final static int REPORT_AUSZULIEFERNDE_POSITIONEN_SORTIERUNG_KUNDE_ARTIKEL_AUSLIEFERTERMIN = 2;
	public final static int REPORT_AUSZULIEFERNDE_POSITIONEN_SORTIERUNG_ARTIKEL_AUSLIEFERTERMIN = 3;
	public final static int REPORT_AUSZULIEFERNDE_POSITIONEN_SORTIERUNG_KUNDE_AUSLIEFERTERMIN_AUFTRAG_ARTIKEL = 4;

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
	public final static int REPORT_PACKLISTE_MENGENTEILER = 35;
	public final static int REPORT_PACKLISTE_POSITIONSSTATUS = 36;
	public final static int REPORT_PACKLISTE_BEZEICHNUNG_EINZELN = 37;
	public final static int REPORT_PACKLISTE_ZUSATZBEZEICHNUNG_EINZELN = 38;
	public final static int REPORT_PACKLISTE_SETARTIKEL_TYP = 39;
	public final static int REPORT_PACKLISTE_LAGERSTAND_ALLER_LAEGER = 40;
	public final static int REPORT_PACKLISTE_POSITIONSNUMMER = 41;
	public final static int REPORT_PACKLISTE_AUFTRAGPOSITION_I_ID = 42;
	public final static int REPORT_PACKLISTE_KURZBEZEICHNUNG = 43;
	public final static int REPORT_PACKLISTE_REFERENZNUMMER = 44;
	public final static int REPORT_PACKLISTE_GESEHEN = 45;
	// Subreportdata muss das letzte Feld bleiben, sonst stimmt die Reihenfolge
	// im Subreport nicht!
	public final static int REPORT_PACKLISTE_SUBREPORT_DATA = 46;

	public final static int REPORT_PACKLISTE_ANZAHL_SPALTEN = 47;

	public final static int REPORT_KOMMISSIONIERUNG_IDENT = 0;
	public final static int REPORT_KOMMISSIONIERUNG_BEZEICHNUNG = 1;
	public final static int REPORT_KOMMISSIONIERUNG_GESAMTMENGE = 2;
	public final static int REPORT_KOMMISSIONIERUNG_OFFENEMENGE = 3;
	public final static int REPORT_KOMMISSIONIERUNG_LAGERSTAND = 4;
	public final static int REPORT_KOMMISSIONIERUNG_LAGERORT = 5;
	public final static int REPORT_KOMMISSIONIERUNG_GEWICHT = 6;
	public final static int REPORT_KOMMISSIONIERUNG_RASTER_LIEGEND = 7;
	public final static int REPORT_KOMMISSIONIERUNG_RASTER_STEHEND = 8;
	public final static int REPORT_KOMMISSIONIERUNG_MATERIALGEWICHT = 9;
	public final static int REPORT_KOMMISSIONIERUNG_SERIENCHARGENR = 10;
	public final static int REPORT_KOMMISSIONIERUNG_POSITION_FREIERTEXT = 11; // optional
	public final static int REPORT_KOMMISSIONIERUNG_POSITION_KOMMENTAR_IMAGE = 12; // optional
	public final static int REPORT_KOMMISSIONIERUNG_VOLLSTAENDIGKEIT_KOMPONENTEN_IDENT = 13;
	public final static int REPORT_KOMMISSIONIERUNG_VOLLSTAENDIGKEIT_KOMPONENTEN_BEZ = 14;
	public final static int REPORT_KOMMISSIONIERUNG_VORZEICHEN = 15;
	public final static int REPORT_KOMMISSIONIERUNG_POSITIONSTERMIN = 16;
	public final static int REPORT_KOMMISSIONIERUNG_POSITIONSTERMIN_TIMESTAMP = 17;
	public final static int REPORT_KOMMISSIONIERUNG_FIKTIVERLAGERSTAND = 18;

	public static final int REPORT_KOMMISSIONIERUNG_ARTIKELKLASSE = 19;
	public static final int REPORT_KOMMISSIONIERUNG_ARTIKELGRUPPE = 20;
	public static final int REPORT_KOMMISSIONIERUNG_FARBCODE = 23;
	public static final int REPORT_KOMMISSIONIERUNG_MATERIAL = 24;
	public static final int REPORT_KOMMISSIONIERUNG_HOEHE = 25;
	public static final int REPORT_KOMMISSIONIERUNG_BREITE = 26;
	public static final int REPORT_KOMMISSIONIERUNG_TIEFE = 27;
	public static final int REPORT_KOMMISSIONIERUNG_BAUFORM = 28;
	public static final int REPORT_KOMMISSIONIERUNG_VERPACKUNGSART = 29;
	public final static int REPORT_KOMMISSIONIERUNG_ARTIKELKOMMENTAR = 30;
	public final static int REPORT_KOMMISSIONIERUNG_VERKAUFSEAN = 31;
	public final static int REPORT_KOMMISSIONIERUNG_VERPACKUNGSMENGE = 32;
	public final static int REPORT_KOMMISSIONIERUNG_VERPACKUNGSEAN = 33;
	public final static int REPORT_KOMMISSIONIERUNG_ARBEITSGAENGE = 34;
	public final static int REPORT_KOMMISSIONIERUNG_MENGENTEILER = 35;
	public final static int REPORT_KOMMISSIONIERUNG_POSITIONSSTATUS = 36;
	public final static int REPORT_KOMMISSIONIERUNG_SETARTIKEL_TYP = 37;
	public final static int REPORT_KOMMISSIONIERUNG_POSITIONSNUMMER = 38;
	public final static int REPORT_KOMMISSIONIERUNG_AUFTRAGPOSITION_I_ID = 39;
	public final static int REPORT_KOMMISSIONIERUNG_KURZBEZEICHNUNG = 40;
	public final static int REPORT_KOMMISSIONIERUNG_REFERENZNUMMER = 41;
	public final static int REPORT_KOMMISSIONIERUNG_GESEHEN = 42;
	// Subreportdata muss das letzte Feld bleiben, sonst stimmt die Reihenfolge
	// im Subreport nicht!
	public final static int REPORT_KOMMISSIONIERUNG_SUBREPORT_DATA = 43;

	public final static int REPORT_KOMMISSIONIERUNG_ANZAHL_SPALTEN = 44;

	public final static int REPORT_ZEITBESTAETIGUNG_ARTIKEL = 0;
	public final static int REPORT_ZEITBESTAETIGUNG_BEZEICHNUNG = 1;
	public final static int REPORT_ZEITBESTAETIGUNG_STUNDENSATZ = 2;
	public final static int REPORT_ZEITBESTAETIGUNG_VON = 3;
	public final static int REPORT_ZEITBESTAETIGUNG_BIS = 4;
	public final static int REPORT_ZEITBESTAETIGUNG_DAUER = 5;
	public final static int REPORT_ZEITBESTAETIGUNG_BEMERKUNG = 6;
	public final static int REPORT_ZEITBESTAETIGUNG_KOMMENTAR = 7;
	public final static int REPORT_ZEITBESTAETIGUNG_KOSTEN = 8;
	public final static int REPORT_ZEITBESTAETIGUNG_ARTIKEL_POSITION = 9;
	public final static int REPORT_ZEITBESTAETIGUNG_BEZEICHNUNG_POSITION = 10;
	public final static int REPORT_ZEITBESTAETIGUNG_ZUSATZBEZEICHNUNG_POSITION = 11;
	public final static int REPORT_ZEITBESTAETIGUNG_POSITIONSNUMMER = 12;

	public final static int REPORT_ZEITBESTAETIGUNG_QUELLE = 13;
	public final static int REPORT_ZEITBESTAETIGUNG_VERRECHENBAR_IN_PROZENT = 14;
	public final static int REPORT_ZEITBESTAETIGUNG_UNTERSCHRIEBEN_AM = 15;
	public final static int REPORT_ZEITBESTAETIGUNG_FEHLER_IN_ZEITDATEN = 16;
	public final static int REPORT_ZEITBESTAETIGUNG_ZEITDATEN_I_ID = 17;
	public final static int REPORT_ZEITBESTAETIGUNG_LETZTE_AENDERUNG_ZEITDATEN = 18;
	public final static int REPORT_ZEITBESTAETIGUNG_PERSON = 19;
	public final static int REPORT_ZEITBESTAETIGUNG_UNTERSCHRIEBEN_NAME = 20;
	public final static int REPORT_ZEITBESTAETIGUNG_UNTERSCHRIEBEN_LFDNR = 21;
	public final static int REPORT_ZEITBESTAETIGUNG_ANZAHL_SPALTEN = 22;

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
	public final static int REPORT_AUFTRAGZEITEN_ZEITDATEN_I_ID = 15;
	public final static int REPORT_AUFTRAGZEITEN_POSITIONOBJEKT = 16;
	public final static int REPORT_AUFTRAGZEITEN_POSITIONOBJEKT_ZUGEHOERIGE_POSITION = 17;
	public final static int REPORT_AUFTRAGZEITEN_AUFTRAGPOSITION_I_ID = 18;
	public final static int REPORT_AUFTRAGZEITEN_AUFTRAGPOSITION_I_ID_ZUGEHOERIGE_POSITION = 19;
	public final static int REPORT_AUFTRAGZEITEN_ANZAHL_SPALTEN = 20;

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

	public final static int REPORT_MEILENSTEINE_AUFTRAGSNUMMER = 0;
	public final static int REPORT_MEILENSTEINE_STATUS = 1;
	public final static int REPORT_MEILENSTEINE_PROJEKT = 2;
	public final static int REPORT_MEILENSTEINE_LIEFERTERMIN = 3;
	public final static int REPORT_MEILENSTEINE_FINALTERMIN = 4;
	public final static int REPORT_MEILENSTEINE_BESTELLNUMMER = 5;
	public final static int REPORT_MEILENSTEINE_KUNDE = 6;
	public final static int REPORT_MEILENSTEINE_LKZ = 7;
	public final static int REPORT_MEILENSTEINE_PLZ = 8;
	public final static int REPORT_MEILENSTEINE_ORT = 9;
	public final static int REPORT_MEILENSTEINE_TERMIN = 10;
	public final static int REPORT_MEILENSTEINE_BETRAG = 11;
	public final static int REPORT_MEILENSTEINE_BETRAG_URSPRUNG = 12;
	public final static int REPORT_MEILENSTEINE_ALLE_VORRAUSSETZUNGEN_ERLEDIGT = 13;
	public final static int REPORT_MEILENSTEINE_MEILENSTEIN = 14;
	public final static int REPORT_MEILENSTEINE_KOMMENTAR = 15;
	public final static int REPORT_MEILENSTEINE_KOMMENTAR_LANG = 16;
	public final static int REPORT_MEILENSTEINE_PERSON_ERLEDIGT = 17;
	public final static int REPORT_MEILENSTEINE_KURZZEICHEN_ERLEDIGT = 18;
	public final static int REPORT_MEILENSTEINE_ERLEDIGUNGSZEITPUNKT = 19;
	public final static int REPORT_MEILENSTEINE_PROJEKTLEITER = 20;
	public final static int REPORT_MEILENSTEINE_PROJEKTLEITER_KURZZEICHEN = 21;
	public final static int REPORT_MEILENSTEINE_ANZAHL_SPALTEN = 22;

	public final static int REPORT_PLANSTUNDEN_AUFTRAGSNUMMER = 0;
	public final static int REPORT_PLANSTUNDEN_STATUS = 1;
	public final static int REPORT_PLANSTUNDEN_PROJEKT = 2;
	public final static int REPORT_PLANSTUNDEN_LIEFERTERMIN = 3;
	public final static int REPORT_PLANSTUNDEN_FINALTERMIN = 4;
	public final static int REPORT_PLANSTUNDEN_BESTELLNUMMER = 5;
	public final static int REPORT_PLANSTUNDEN_KUNDE = 6;
	public final static int REPORT_PLANSTUNDEN_VERFUEGBARKEIT = 7;
	public final static int REPORT_PLANSTUNDEN_DAUER = 8;
	public final static int REPORT_PLANSTUNDEN_DAUER_URSPRUNG = 9;
	public final static int REPORT_PLANSTUNDEN_KOMMENTAR = 10;
	public final static int REPORT_PLANSTUNDEN_KOMMENTAR_LANG = 11;
	public final static int REPORT_PLANSTUNDEN_PERSON_ERLEDIGT = 12;
	public final static int REPORT_PLANSTUNDEN_KURZZEICHEN_ERLEDIGT = 13;
	public final static int REPORT_PLANSTUNDEN_ERLEDIGUNGSZEITPUNKT = 14;
	public final static int REPORT_PLANSTUNDEN_DATUM = 15;
	public final static int REPORT_PLANSTUNDEN_TERMIN = 16;
	public final static int REPORT_PLANSTUNDEN_BETRIEBSKALENDER = 17;
	public final static int REPORT_PLANSTUNDEN_PROJEKTLEITER = 18;
	public final static int REPORT_PLANSTUNDEN_PROJEKTLEITER_KURZZEICHEN = 19;
	public final static int REPORT_PLANSTUNDEN_ANZAHL_SPALTEN = 20;

	public final static int REPORT_MATERIALBEDARFE_AUFTRAGSNUMMER = 0;
	public final static int REPORT_MATERIALBEDARFE_STATUS = 1;
	public final static int REPORT_MATERIALBEDARFE_PROJEKT = 2;
	public final static int REPORT_MATERIALBEDARFE_LIEFERTERMIN = 3;
	public final static int REPORT_MATERIALBEDARFE_FINALTERMIN = 4;
	public final static int REPORT_MATERIALBEDARFE_BESTELLNUMMER = 5;
	public final static int REPORT_MATERIALBEDARFE_KUNDE = 6;
	public final static int REPORT_MATERIALBEDARFE_BETRAG_MATERIAL = 7;
	public final static int REPORT_MATERIALBEDARFE_URSPRUNGSBETRAG_MATERIAL = 8;
	public final static int REPORT_MATERIALBEDARFE_KOMMENTAR = 9;
	public final static int REPORT_MATERIALBEDARFE_KOMMENTAR_LANG = 10;
	public final static int REPORT_MATERIALBEDARFE_PERSON_ERLEDIGT = 11;
	public final static int REPORT_MATERIALBEDARFE_KURZZEICHEN_ERLEDIGT = 12;
	public final static int REPORT_MATERIALBEDARFE_ERLEDIGUNGSZEITPUNKT = 13;
	public final static int REPORT_MATERIALBEDARFE_TERMIN = 14;
	public final static int REPORT_MATERIALBEDARFE_PROJEKTLEITER = 15;
	public final static int REPORT_MATERIALBEDARFE_PROJEKTLEITER_KURZZEICHEN = 16;
	public final static int REPORT_MATERIALBEDARFE_ANZAHL_SPALTEN = 17;

	/**
	 * Fuer die Zwischensumme muss die Menge der uebergeordneten Position bekannt
	 * sein.
	 */
	public final static int REPORT_VORKALKULATION_MENGE_UEBERGEORDNET = 5;
	/**
	 * Fuer die Zwischensumme muss die Einheit der uebergeordneten Position bekannt
	 * sein.
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
	 * Jede Ident Position hat einen manuell bestimmten Gestehungswert, der kursiv
	 * angedruckt wird
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
	public final static int REPORT_WIEDERBESCHAFFUNG_LIEFERANT = 13;
	public final static int REPORT_WIEDERBESCHAFFUNG_LIEFERANT_ARTIKELNUMMER = 14;
	public final static int REPORT_WIEDERBESCHAFFUNG_LIEFERANT_ARTIKELBEZEICHNUNG = 15;
	public final static int REPORT_WIEDERBESCHAFFUNG_SUMME_SOLLZEITEN = 16;
	public final static int REPORT_WIEDERBESCHAFFUNG_LAGERSTAND_SPERRLAEGER = 17;
	public final static int REPORT_WIEDERBESCHAFFUNG_SETARTIKEL_TYP = 18;
	public final static int REPORT_WIEDERBESCHAFFUNG_STKL_EBENE = 19;
	public final static int REPORT_WIEDERBESCHAFFUNG_NACHFOLGENDE_PRODUKTIONSDAUER = 20;
	public final static int REPORT_WIEDERBESCHAFFUNG_MANDANT = 21;
	public final static int REPORT_WIEDERBESCHAFFUNG_SUBREPORT_VERFUEGBARKEITEN_ALLER_MANDANTEN = 22;
	public final static int REPORT_WIEDERBESCHAFFUNG_MAXIMALE_WBZ_DER_EBENE = 23;
	public final static int REPORT_WIEDERBESCHAFFUNG_DAUER_BIS_FERTIGSTELLUNG_DER_EBENE = 24;
	public final static int REPORT_WIEDERBESCHAFFUNG_STUECKLISTENART = 25;
	public final static int REPORT_WIEDERBESCHAFFUNG_MANDANT_DES_LIEFERANTEN = 26;
	public final static int REPORT_WIEDERBESCHAFFUNG_MENGE_IN_FERTIGUNG = 27;
	public final static int REPORT_WIEDERBESCHAFFUNG_FRUEHESTER_LIEFERTERMIN = 28;
	public final static int REPORT_WIEDERBESCHAFFUNG_MENGE_BESTELLT_INTERN = 29;
	public final static int REPORT_WIEDERBESCHAFFUNG_MENGE_RESERVIERT_INTERN = 30;
	public final static int REPORT_WIEDERBESCHAFFUNG_MENGE_ZU_MIR_UNTERWEGS = 31;
	public final static int REPORT_WIEDERBESCHAFFUNG_ARTIKEL_LAGERBEWIRTSCHAFTET = 32;
	public final static int REPORT_WIEDERBESCHAFFUNG_REFERENZNUMMER = 33;
	public final static int REPORT_WIEDERBESCHAFFUNG_ARTIKEL_MELDEPFLICHTIG = 34;
	public final static int REPORT_WIEDERBESCHAFFUNG_ARTIKEL_BEWILLIGUNGSPFLCHTIG = 35;
	public final static int REPORT_WIEDERBESCHAFFUNG_ANZAHL_SPALTEN = 36;

	public final static int SORT_REPORT_WIEDERBESCHAFFUNG_AUFTRAGSPOSITION = 0;
	public final static int SORT_REPORT_WIEDERBESCHAFFUNG_LIEFERANT = 1;
	public final static int SORT_REPORT_WIEDERBESCHAFFUNG_GESAMT_WBZ = 2;

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
	public final static int REPORT_ALLE_LIEFERADRESSE_ADRESSE = 21;
	public final static int REPORT_ALLE_RECHNUNGSADRESSE = 22;
	public final static int REPORT_ALLE_ANSPRECHPARTNER = 23;
	public final static int REPORT_ALLE_LAENDERART = 24;
	public final static int REPORT_ALLE_LIEFERADRESSE_KURZBEZEICHNUNG = 25;
	public final static int REPORT_ALLE_AUFTRAGSFREIGABE_ZEITPUNKT = 26;
	public final static int REPORT_ALLE_AUFTRAGSFREIGABE_PERSON = 27;
	public final static int REPORT_ALLE_WIEDERHOLUNGSINTERVALL = 28;
	public final static int REPORT_ALLE_LAUFTERMIN_VON = 29;
	public final static int REPORT_ALLE_LAUFTERMIN_BIS = 30;
	public final static int REPORT_ALLE_I_VERSION = 31;
	public final static int REPORT_ALLE_PROVISIONSEMPFAENGER_KURZZEICHEN = 32;
	public final static int REPORT_ALLE_PROVISIONSEMPFAENGER_NAME = 33;
	public final static int REPORT_ALLE_ANZAHL_SPALTEN = 34;

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
	public final static int REPORT_ERLEDIGT_AUFTRAGSFREIGABE_ZEITPUNKT = 25;
	public final static int REPORT_ERLEDIGT_AUFTRAGSFREIGABE_PERSON = 26;
	public final static int REPORT_ERLEDIGT_WUNSCHTERMIN = 27;
	public final static int REPORT_ERLEDIGT_WIEDERHOLUNGSINTERVALL = 28;
	public final static int REPORT_ERLEDIGT_LAUFTERMIN_VON = 29;
	public final static int REPORT_ERLEDIGT_LAUFTERMIN_BIS = 30;
	public final static int REPORT_ERLEDIGT_ANZAHL_SPALTEN = 31;

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
	public final static int REPORT_RAHMENUEBERSICHT_STORNIERT = 12;
	public final static int REPORT_RAHMENUEBERSICHT_KEIN_RAHMENBEZUG = 13;
	public final static int REPORT_RAHMENUEBERSICHT_ARTIKELKURZBEZEICHNUNG = 14;
	public final static int REPORT_RAHMENUEBERSICHT_ARTIKELREFERENZNUMMER = 15;
	public final static int REPORT_RAHMENUEBERSICHT_ANZAHL_SPALTEN = 16;

	/** WH 22.02.06 vorerst Punkt, damit man die Ebene erkennen kann */
	public final static String REPORT_VORKALKULATION_ZEICHEN_FUER_HANDEINGABE = ".";

	public final static String AUFTRAG_EIGENSCHAFTEN_FA = "Fa";
	public final static String AUFTRAG_EIGENSCHAFTEN_CLUSTER = "Cluster";
	public final static String AUFTRAG_EIGENSCHAFTEN_EQNR = "Equipmentnr";

	public final static int REPORT_AUFTRAGSUEBERSICHT_LIEFERSCHEIN = 0;
	public final static int REPORT_AUFTRAGSUEBERSICHT_RECHNUNG = 1;
	public final static int REPORT_AUFTRAGSUEBERSICHT_ARTIKELNUMMER = 2;
	public final static int REPORT_AUFTRAGSUEBERSICHT_BEZEICHNUNG = 3;
	public final static int REPORT_AUFTRAGSUEBERSICHT_ZUSATZBEZEICHNUNG = 4;
	public final static int REPORT_AUFTRAGSUEBERSICHT_SOLLMENGE = 5;
	public final static int REPORT_AUFTRAGSUEBERSICHT_ISTMENGE = 6;
	public final static int REPORT_AUFTRAGSUEBERSICHT_EINHEIT = 7;
	public final static int REPORT_AUFTRAGSUEBERSICHT_NETTOPREIS = 8;
	public final static int REPORT_AUFTRAGSUEBERSICHT_NICHT_ZUGEORDNET = 9;
	public final static int REPORT_AUFTRAGSUEBERSICHT_ARBEITSZEIT = 10;
	public final static int REPORT_AUFTRAGSUEBERSICHT_AZ_DAUER = 11;
	public final static int REPORT_AUFTRAGSUEBERSICHT_AZ_VON = 12;
	public final static int REPORT_AUFTRAGSUEBERSICHT_AZ_BIS = 13;
	public final static int REPORT_AUFTRAGSUEBERSICHT_AZ_PERSON = 14;
	public final static int REPORT_AUFTRAGSUEBERSICHT_POS_NR = 15;
	public final static int REPORT_AUFTRAGSUEBERSICHT_AZ_PERSON_KURZZEICHEN = 16;
	public final static int REPORT_AUFTRAGSUEBERSICHT_LIEFERTERMIN = 17;
	public final static int REPORT_AUFTRAGSUEBERSICHT_BELEGDATM = 18;
	public final static int REPORT_AUFTRAGSUEBERSICHT_SOLLMENGE_BEREITS_VERWENDET = 19;
	public final static int REPORT_AUFTRAGSUEBERSICHT_SUBREPORT_SNR_CHNR = 20;
	public final static int REPORT_AUFTRAGSUEBERSICHT_SETARTIKEL_TYP = 21;
	public final static int REPORT_AUFTRAGSUEBERSICHT_LIEFERSCHEIN_VERRECHNET_MIT = 22;
	public final static int REPORT_AUFTRAGSUEBERSICHT_LIEFERSCHEIN_MANUELL_ERLEDIGT = 23;
	public final static int REPORT_AUFTRAGSUEBERSICHT_REFERENZNUMMER = 24;
	public final static int REPORT_AUFTRAGSUEBERSICHT_KURZBEZEICHNUNG = 25;
	public final static int REPORT_AUFTRAGSUEBERSICHT_GELIEFERT_ARTIKELNUMMER = 26;
	public final static int REPORT_AUFTRAGSUEBERSICHT_GELIEFERT_BEZEICHNUNG = 27;
	public final static int REPORT_AUFTRAGSUEBERSICHT_GELIEFERT_ZUSATZBEZEICHNUNG = 28;
	public final static int REPORT_AUFTRAGSUEBERSICHT_GELIEFERT_KURZBEZEICHNUNG = 29;
	public final static int REPORT_AUFTRAGSUEBERSICHT_POSITIONSSTATUS = 30;
	public final static int REPORT_AUFTRAGSUEBERSICHT_ANZAHL_SPALTEN = 31;

	public final static int REPORT_TEILNEHMER_AUFTRAG_NUMMER = 0;
	public final static int REPORT_TEILNEHMER_KUNDE_NAME1 = 1;
	public final static int REPORT_TEILNEHMER_KUNDE_NAME2 = 2;
	public final static int REPORT_TEILNEHMER_KUNDE_NAME3 = 3;
	public final static int REPORT_TEILNEHMER_KUNDE_STRASSE = 4;
	public final static int REPORT_TEILNEHMER_KUNDE_LKZ = 5;
	public final static int REPORT_TEILNEHMER_KUNDE_PLZ = 6;
	public final static int REPORT_TEILNEHMER_KUNDE_ORT = 7;
	public final static int REPORT_TEILNEHMER_AUFTRAG_BELEGDATUM = 8;
	public final static int REPORT_TEILNEHMER_AUFTRAG_LIEFERTERMIN = 9;
	public final static int REPORT_TEILNEHMER_AUFTRAG_BESTELLNUMMER = 10;
	public final static int REPORT_TEILNEHMER_AUFTRAG_PROJEKT = 11;
	public final static int REPORT_TEILNEHMER_AUFTRAG_KOSTENSTELLE = 12;
	public final static int REPORT_TEILNEHMER_TEILNEHMER_VORNAME = 13;
	public final static int REPORT_TEILNEHMER_TEILNEHMER_NACHNAME = 14;
	public final static int REPORT_TEILNEHMER_TEILNEHMER_KURZZEICHEN = 15;
	public final static int REPORT_TEILNEHMER_TEILNEHMER_PERSONALNUMMER = 16;
	public final static int REPORT_TEILNEHMER_TEILNEHMER_FUNKTION = 17;
	public final static int REPORT_TEILNEHMER_TEILNEHMER_KOSTENSTELLE = 18;
	public final static int REPORT_TEILNEHMER_AUFTRAG_STATUS = 19;
	public final static int REPORT_TEILNEHMER_ANZAHL_SPALTEN = 20;

	public final static int SORT_REPORT_TEILNEHMER_AUFTRAG = 0;
	public final static int SORT_REPORT_TEILNEHMER_KUNDE = 1;
	public final static int SORT_REPORT_TEILNEHMER_TEILNEHMER = 2;

	public static int REPORT_AUFTRAGUMSATZSTATISTIK_KUNDENGRUPPIERUNG = 0;
	public static int REPORT_AUFTRAGUMSATZSTATISTIK_KUNDE = 1;
	public static int REPORT_AUFTRAGUMSATZSTATISTIK_UMSATZ = 2;
	public static int REPORT_AUFTRAGUMSATZSTATISTIK_PLZ = 3;
	public static int REPORT_AUFTRAGUMSATZSTATISTIK_LKZ = 4;
	public static int REPORT_AUFTRAGUMSATZSTATISTIK_ABCKLASSE = 5;
	public static int REPORT_AUFTRAGUMSATZSTATISTIK_SPALTE1 = 6;
	public static int REPORT_AUFTRAGUMSATZSTATISTIK_SPALTE2 = 7;
	public static int REPORT_AUFTRAGUMSATZSTATISTIK_SPALTE3 = 8;
	public static int REPORT_AUFTRAGUMSATZSTATISTIK_SPALTE4 = 9;
	public static int REPORT_AUFTRAGUMSATZSTATISTIK_SPALTE5 = 10;
	public static int REPORT_AUFTRAGUMSATZSTATISTIK_SPALTE6 = 11;
	public static int REPORT_AUFTRAGUMSATZSTATISTIK_SPALTE7 = 12;
	public static int REPORT_AUFTRAGUMSATZSTATISTIK_SPALTE8 = 13;
	public static int REPORT_AUFTRAGUMSATZSTATISTIK_ANZAHL_SPALTEN = 14;

	public JasperPrintLP printAuftragUmsatzstatistik(Date dOffenerAuftragsstandZum, Integer iArt,
			Integer iArtUnverbindlich, Integer iOptionGruppierung, boolean bNurHauptgruppeKlasse,
			Integer iOptionKundengruppierung, Integer iOptionSortierung, boolean bMitAngelegten, TheClientDto theClientDto);

	public JasperPrintLP printAuftragOffene(ReportJournalKriterienDto krit, Date dStichtag,
			Boolean bSortierungNachLiefertermin, Boolean bInternenKommentarDrucken, Integer iArt,
			Integer iArtUnverbindlich, boolean bMitAngelegten, boolean bStichtagGreiftBeiLiefertermin,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printAuftraegeErledigt(ReportJournalKriterienDto kritDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printAuftragOffeneDetails(ReportJournalKriterienDto krit,
			boolean bSortierungNachFertigungsgruppe, Date dStichtag, Boolean bSortierungNachLiefertermin,
			Boolean bInternenKommentarDrucken, Integer artikelklasseIId, Integer artikelgruppeIId, String artikelCNrVon,
			String artikelCNrBis, String projektCBezeichnung, Integer iArt, Integer iArtUnverbindlich,
			boolean bLagerstandsdetail, boolean bMitAngelegten, Integer fertigungsgruppeIId, TheClientDto theClientDto);

	public JasperPrintLP printAuftragOffenePositionen(ReportJournalKriterienDto krit, Date dVon, Date dStichtagBzwBis,
			Boolean bSortierungNachLiefertermin, Boolean bOhnePositionen, Boolean bSortierungNachAbliefertermin,
			Integer[] fertigungsgruppeIId, Integer iArt, Integer iArtUnverbindlich, Boolean bSortierungNurLiefertermin,
			String sReportname, Boolean bSortierungNachArtikel, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP[] printAuftragbestaetigung(Integer iIdAuftragI, Integer iAnzahlKopienI, Boolean bMitLogo,
			String sReportname, boolean bMitBewegungsvorschau, TheClientDto theClientDto) throws EJBExceptionLP ;
	
	public JasperPrintLP[] printAuftragbestaetigung(Integer iIdAuftragI, Integer iAnzahlKopienI, Boolean bMitLogo,
			String sReportname, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printAuftragPackliste(Integer iIdAuftragI, String sReportName, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printKommissionierung(Integer iIdAuftragI, TheClientDto theClientDto);

	public JasperPrintLP printAuftragszeiten(Integer iIdAuftragI, boolean bSortierNachPerson, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printAuftragSrnnrnEtikett(Integer iIdAuftragI, Integer iIdAuftragpositionI,
			String cAktuellerReport, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printErfuellungsgrad(java.sql.Timestamp tStichtag, Integer personalIId_Vertreter,
			Integer kostenstelleIId, boolean bMitWiederholenden, TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printRahmenuebersicht(Integer auftragIId, Integer auftragpositionIId, TheClientDto theClientDto);

	public JasperPrintLP printAuftragstatistik(java.sql.Timestamp tVon, java.sql.Timestamp tBis, int iOptionDatum,
			Integer kundeIId_Auftragsadresse, Integer kundeIId_Rechnungsadresse, Integer auftragIId, String cProjekt,
			String cBestellnummer, int iOptionSortierung, boolean bArbeitszeitVerdichtet, java.sql.Timestamp tStichtag,
			Integer projektIId, boolean bAlleBetroffenenLoseNachkalkulieren, TheClientDto theClientDto)
			throws RemoteException;

	public JasperPrintLP printTaetigkeitsstatistik(java.sql.Timestamp tVon, java.sql.Timestamp tBis,
			PartnerklasseDto partnerklasse, TheClientDto theClientDto);

	public JasperPrintLP printRollierendeplanung(Integer auftragIId, boolean bSortiertNachLieferant,
			TheClientDto theClientDto);

	public JasperPrintLP printErfuellungsjournal(Integer auftragIIdRahmenauftrag, java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, TheClientDto theClientDto);

	public JasperPrintLP printAuftragOffeneOhneDetail(ReportJournalKriterienDto krit, Date dStichtag,
			Boolean bSortierungNachLiefertermin, Boolean bInternenKommentarDrucken, Integer iArt,
			Integer iArtUnverbindlich, boolean bMitAngelegten, boolean bStichtagGreiftBeiLiefertermin,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printWiederbeschaffung(Integer[] aIIdArtikelI, BigDecimal[] aBdMenge, String[] artikelsetType,
			Map<String, Object> mapReportParameterI, int iSortierung, Double dWBZWennNichtDefiniert,
			Integer kundenlieferdauer, boolean bUeberAlleMandanten, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printVerfuegbarkeitspruefung(Integer iIdAuftragI, int iSortierung,
			Double dWBZWennNichtDefiniert, boolean bUeberAlleMandanten, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Hashtable<?, ?> getAuftragEigenschaften(Integer iIdAuftragI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printAuftragAlle(ReportJournalKriterienDto kritDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printAuftragspositionsetikett(Integer auftragpositionIId, Integer iExemplare,
			TheClientDto theClientDto);

	public JasperPrintLP printAuftragsuebersicht(Integer iIdAuftragI, TheClientDto theClientDto);

	public Object[][] getDataAuftragsuebersicht(Integer iIdAuftragI, TheClientDto theClientDto);

	public Set getAlleLieferscheineEinesAuftrags(Integer iIdAuftragI);

	public Set getAlleRechnungenEinesAuftrags(Integer iIdAuftragI);

	public JasperPrintLP printProjektblatt(Integer auftragIId, TheClientDto theClientDto);

	public JasperPrintLP printMeilensteine(Timestamp tFaelligBis, TheClientDto theClientDto);

	public JasperPrintLP printPlanstunden(Timestamp tFaelligBis, TheClientDto theClientDto);

	public JasperPrintLP printMaterialbedarfe(Timestamp tFaelligBis, TheClientDto theClientDto);

	public JasperPrintLP printErfolgsstatus(DatumsfilterVonBis vonBis, int iOptionDatum, Integer kundeIId,
			Integer projektIId, int iOptionSortierung, ArrayList<Integer> auftragIIds, TheClientDto theClientDto);

	public JasperPrintLP printAuftragsetikett(Integer iIdAuftragI, Integer iExemplare, TheClientDto theClientDto);

	public JasperPrintLP printLieferplan(TheClientDto theClientDto);

	public JasperPrintLP printAuszulieferndePositionen(Date dAuszuliefernBis, Integer kundeIId, int iSort,
			TheClientDto theClientDto);

	public String getArtikelsetType(AuftragpositionDto auftragpositionDto);

	public JasperPrintLP printZeitbestaetigung(Integer personalIId, Integer auftragIId, DatumsfilterVonBis datumsfilter,
			boolean inclUnterschriebeneZeiten, TheClientDto theClientDto) throws EJBExceptionLP;

	public JasperPrintLP printZeitbestaetigung(Integer personalIId, Integer auftragIId, DatumsfilterVonBis datumsfilter,
			boolean inclUnterschriebeneZeiten, Integer lfdNr, TheClientDto theClientDto);

	public JasperPrintLP printAuftragteilnehmer(int iSortierung, boolean bNurOffene, TheClientDto theClientDto);

}
