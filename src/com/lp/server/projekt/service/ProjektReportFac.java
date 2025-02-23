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
package com.lp.server.projekt.service;

import java.rmi.RemoteException;
import java.sql.Date;

import javax.ejb.Remote;

import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.DatumsfilterVonBis;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;

@Remote
public interface ProjektReportFac {

	public final static String REPORT_MODUL = "projekt";

	public final static String REPORT_PROJEKT = "proj_projekt.jasper";
	public final static String REPORT_PROJEKT_JOURNAL_MITARBEITER = "proj_projekt_mitarbeiter.jasper";
	public final static String REPORT_PROJEKT_JOURNAL_OFFENE = "proj_projekt_journal_offene.jasper";
	public final static String REPORT_PROJEKT_JOURNAL_FORECAST = "proj_projekt_journal_forecast.jasper";
	public final static String REPORT_PROJEKT_JOURNAL_ALLE = "proj_projekt_journal_alle.jasper";
	public final static String REPORT_PROJEKT_JOURNAL_ALLE_DETAILLLIERT = "proj_projekt_journal_alle_detailliert.jasper";
	public final static String REPORT_PROJEKT_JOURNAL_ERLEDIGT = "proj_projekt_journal_erledigt.jasper";
	public final static String REPORT_PROJEKT_JOURNAL_AKTIVITAETSUEBERSICHT = "proj_aktivitaetsuebersicht.jasper";
	public final static String REPORT_PROJEKTVERLAUF = "proj_projektverlauf.jasper";
	public final static String REPORT_PROJEKTZEITDATEN = "proj_projektzeitdaten.jasper";
	public final static String REPORT_PROJEKTBAUM = "proj_projektbaum.jasper";
	public final static String REPORT_PROJEKTSTATISTIK = "proj_projektstatistik.jasper";
	public final static String REPORT_AENDERUNGEN_EIGENSCHAFTEN = "proj_aenderungen_eigenschaften.jasper";
	// PJ18949 Kein echter Report; wg. XSL-Datei fuer Mailversand
	public final static String REPORT_PROJEKT_DETAIL = "proj_projekt_detail.jasper";
	public final static String REPORT_PROJEKTE_EINES_ARTIKELS = "proj_projekte_eines_artikels.jasper";

	public final static int REPORT_PROJEKTSTATISTIK_BELEGART = 0;
	public final static int REPORT_PROJEKTSTATISTIK_BELEGNUMMER = 1;
	public final static int REPORT_PROJEKTSTATISTIK_BELEGDATUM = 2;
	public final static int REPORT_PROJEKTSTATISTIK_PARTNER = 3;
	public final static int REPORT_PROJEKTSTATISTIK_ARTIKEL = 4;
	public final static int REPORT_PROJEKTSTATISTIK_ARTIKELBEZEICHNUNG = 5;
	public final static int REPORT_PROJEKTSTATISTIK_ARTIKELZUSATZBEZEICHNUNG = 6;
	public final static int REPORT_PROJEKTSTATISTIK_ARTIKELZUSATZBEZEICHNUNG2 = 7;
	public final static int REPORT_PROJEKTSTATISTIK_SNRCHNR = 8;
	public final static int REPORT_PROJEKTSTATISTIK_MENGE = 9;
	public final static int REPORT_PROJEKTSTATISTIK_ANZAHL_SPALTEN = 10;

	public final static int REPORT_PROJEKTSTATISTIK_OPTION_ALLE = 1;
	public final static int REPORT_PROJEKTSTATISTIK_OPTION_VK = 2;
	public final static int REPORT_PROJEKTSTATISTIK_OPTION_EK = 3;
	public final static int REPORT_PROJEKTSTATISTIK_OPTION_FERTIGUNG = 4;

	public static int REPORT_AENDERUNGEN_EIGENSCHAFTEN_OPERATION = 0;
	public static int REPORT_AENDERUNGEN_EIGENSCHAFTEN_I_ID = 1;
	public static int REPORT_AENDERUNGEN_EIGENSCHAFTEN_KEY = 2;
	public static int REPORT_AENDERUNGEN_EIGENSCHAFTEN_VON = 3;
	public static int REPORT_AENDERUNGEN_EIGENSCHAFTEN_NACH = 4;
	public static int REPORT_AENDERUNGEN_EIGENSCHAFTEN_LOCALE = 5;
	public static int REPORT_AENDERUNGEN_EIGENSCHAFTEN_PERSON = 6;
	public static int REPORT_AENDERUNGEN_EIGENSCHAFTEN_ZEITPUNKT = 7;
	public static int REPORT_AENDERUNGEN_EIGENSCHAFTEN_ANZAHL_SPALTEN = 8;

	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_PROJEKTCNR = 0;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_PROJEKTKATEGORIE = 1;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_KUNDECNAME1 = 2;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_ANSPRECHPARTNERCNAME1 = 3;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_PROJEKTTITEL = 4;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_ZIELTERMIN = 5;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_BELEGDATUM = 6;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_PRIO = 7;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_STATUS = 8;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_TYP = 9;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_KONTAKTART = 10;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_TEXT = 11;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_ERZEUGER = 12;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_ZUGEWIESENER = 13;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_VERRECHENBAR = 14;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_MITARBEITER = 15;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_BELEGDATUM = 16;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_TEXT = 17;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_DAUER = 18;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_ZEIT = 19;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_ZIELWOCHE = 20;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_ERLEDIGUNGSDATUM = 21;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_B_FREIGEGEBEN = 22;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_ANSPRECHPARTNER = 23;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_DATEINAME = 24;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_ORT = 25;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_LKZ = 26;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_PLZ = 27;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_WAHRSCHEINLICHKEIT = 28;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_GEPLANTERUMSATZ = 29;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_GESAMTDAUER = 30;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_INTERNERLEDIGT_PERSON = 31;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_INTERNERLEDIGT_ZEIT = 32;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_BRANCHE = 33;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_PARTNERKLASSE = 34;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_HTML = 35;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_AENDERUNGSDATUM = 36;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_BETREIBER = 37;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_I_SORT_QUEUE = 38;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_SUBREPORT_VATERPROJEKTE = 39;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_SUBREPORT_KINDPROJEKTE = 40;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_VKFORTSCHRITT = 41;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_VKFORTSCHRITT_BEZEICHNUNG = 42;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_VKFORTSCHRITT_LEADSTATUS = 43;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_REALISIERUNGSTERMIN = 44;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_JUENGSTES_AENDERUNGSDATUM = 45;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_TITEL = 46;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_HISTORYART = 47;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_WIRD_DURCHGEFUEHRT_VON = 48;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_ERLEDIGUNGSGRAD = 49;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY_DAUER_GEPLANT = 50;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_STATUS_BILD = 51;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_HISTORY = 52;
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_ANZAHL_SPALTEN = 53;

	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_PROJEKTCNR = 0;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_PROJEKTKATEGORIE = 1;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_KUNDECNAME1 = 2;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_ANSPRECHPARTNERCNAME1 = 3;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_PROJEKTTITEL = 4;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_ZIELTERMIN = 5;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_BELEGDATUM = 6;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_PRIO = 7;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_STATUS = 8;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_TYP = 9;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_KONTAKTART = 10;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_TEXT = 11;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_ERZEUGER = 12;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_ZUGEWIESENER = 13;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_VERRECHENBAR = 14;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_HISTORY_MITARBEITER = 15;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_HISTORY_BELEGDATUM = 16;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_HISTORY_TEXT = 17;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_DAUER = 18;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_ZEIT = 19;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_ZIELWOCHE = 20;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_ERLEDIGUNGSDATUM = 21;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_B_FREIGEGEBEN = 22;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_ANSPRECHPARTNER = 23;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_DATEINAME = 24;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_ORT = 25;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_LKZ = 26;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_PLZ = 27;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_WAHRSCHEINLICHKEIT = 28;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_GEPLANTERUMSATZ = 29;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_GESAMTDAUER = 30;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_INTERNERLEDIGT_PERSON = 31;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_INTERNERLEDIGT_ZEIT = 32;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_BRANCHE = 33;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_PARTNERKLASSE = 34;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_HISTORY_HTML = 35;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_HISTORY_AENDERUNGSDATUM = 36;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_BETREIBER = 37;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_I_SORT_QUEUE = 38;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_SUBREPORT_VATERPROJEKTE = 39;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_SUBREPORT_KINDPROJEKTE = 40;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_VKFORTSCHRITT = 41;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_VKFORTSCHRITT_BEZEICHNUNG = 42;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_VKFORTSCHRITT_LEADSTATUS = 43;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_REALISIERUNG = 44;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_WERT_AUFTRAG = 45;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_WERT_RECHNUNG = 46;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_WERT_GUTSCHRIFT = 47;
	public final static int REPORT_PROJEKT_JOURNAL_FORECAST_ANZAHL_SPALTEN = 48;

	public final static int REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_PROJEKTCNR = 0;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_PROJEKTKATEGORIE = 1;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_KUNDECNAME1 = 2;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_ANSPRECHPARTNERCNAME1 = 3;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_PROJEKTTITEL = 4;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_ZIELTERMIN = 5;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_BELEGDATUM = 6;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_PRIO = 7;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_STATUS = 8;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_TYP = 9;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_KONTAKTART = 10;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_TEXT = 11;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_ERZEUGER = 12;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_ZUGEWIESENER = 13;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_B_VERRECHENBAR = 14;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_DAUER = 15;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_ZEIT = 16;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_ZIELWOCHE = 17;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_ERLEDIGUNGSDATUM = 18;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_ERLEDIGER = 19;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_WAHRSCHEINLICHKEIT = 20;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_GEPLANTERUMSATZ = 21;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_INTERNERLEDIGT_PERSON = 22;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_INTERNERLEDIGT_ZEIT = 23;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_BETREIBER = 24;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_VKFORTSCHRITT = 25;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_VKFORTSCHRITT_BEZEICHNUNG = 26;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_VKFORTSCHRITT_LEADSTATUS = 27;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_SUBREPORT_HISTORY = 28;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_SUBREPORT_ZUGEHOERIGE_BELEGE = 29;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_DETAILLIERT_ANZAHL_SPALTEN = 30;

	public final static int REPORT_PROJEKT_JOURNAL_ALLE_PROJEKTCNR = 0;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_PROJEKTKATEGORIE = 1;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_KUNDECNAME1 = 2;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_ANSPRECHPARTNERCNAME1 = 3;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_PROJEKTTITEL = 4;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_ZIELTERMIN = 5;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_BELEGDATUM = 6;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_PRIO = 7;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_STATUS = 8;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_TYP = 9;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_KONTAKTART = 10;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_TEXT = 11;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_ERZEUGER = 12;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_ZUGEWIESENER = 13;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_B_VERRECHENBAR = 14;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_HISTORY_MITARBEITER = 15;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_HISTORY_BELEGDATUM = 16;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_HISTORY_TEXT = 17;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_DAUER = 18;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_ZEIT = 19;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_ZIELWOCHE = 20;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_ERLEDIGUNGSDATUM = 21;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_ERLEDIGER = 22;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_WAHRSCHEINLICHKEIT = 22;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_GEPLANTERUMSATZ = 24;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_INTERNERLEDIGT_PERSON = 25;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_INTERNERLEDIGT_ZEIT = 26;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_HISTORY_HTML = 27;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_BETREIBER = 28;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_VKFORTSCHRITT = 29;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_VKFORTSCHRITT_BEZEICHNUNG = 30;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_VKFORTSCHRITT_LEADSTATUS = 31;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_WIRD_DURCHGEFUEHRT_VON = 32;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_ERLEDIGUNGSGRAD = 33;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_HISTORY_DAUER_GEPLANT = 34;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_ISTZEIT = 35;
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_ANZAHL_SPALTEN = 36;

	public final static int REPORT_PROJEKT_JOURNAL_ERLEDIGT_PROJEKTCNR = 0;
	public final static int REPORT_PROJEKT_JOURNAL_ERLEDIGT_PROJEKTKATEGORIE = 1;
	public final static int REPORT_PROJEKT_JOURNAL_ERLEDIGT_KUNDECNAME1 = 2;
	public final static int REPORT_PROJEKT_JOURNAL_ERLEDIGT_ANSPRECHPARTNERCNAME1 = 3;
	public final static int REPORT_PROJEKT_JOURNAL_ERLEDIGT_PROJEKTTITEL = 4;
	public final static int REPORT_PROJEKT_JOURNAL_ERLEDIGT_ZIELTERMIN = 5;
	public final static int REPORT_PROJEKT_JOURNAL_ERLEDIGT_BELEGDATUM = 6;
	public final static int REPORT_PROJEKT_JOURNAL_ERLEDIGT_PRIO = 7;
	public final static int REPORT_PROJEKT_JOURNAL_ERLEDIGT_STATUS = 8;
	public final static int REPORT_PROJEKT_JOURNAL_ERLEDIGT_TYP = 9;
	public final static int REPORT_PROJEKT_JOURNAL_ERLEDIGT_KONTAKTART = 10;
	public final static int REPORT_PROJEKT_JOURNAL_ERLEDIGT_TEXT = 11;
	public final static int REPORT_PROJEKT_JOURNAL_ERLEDIGT_ERZEUGER = 12;
	public final static int REPORT_PROJEKT_JOURNAL_ERLEDIGT_ZUGEWIESENER = 13;
	public final static int REPORT_PROJEKT_JOURNAL_ERLEDIGT_B_VERRECHENBAR = 14;
	public final static int REPORT_PROJEKT_JOURNAL_ERLEDIGT_ERLEDIGUNGSDATUM = 15;
	public final static int REPORT_PROJEKT_JOURNAL_ERLEDIGT_DAUER = 16;
	public final static int REPORT_PROJEKT_JOURNAL_ERLEDIGT_ZEIT = 17;
	public final static int REPORT_PROJEKT_JOURNAL_ERLEDIGT_ZIELWOCHE = 18;
	public final static int REPORT_PROJEKT_JOURNAL_ERLEDIGT_HISTORY_MITARBEITER = 19;
	public final static int REPORT_PROJEKT_JOURNAL_ERLEDIGT_HISTORY_BELEGDATUM = 20;
	public final static int REPORT_PROJEKT_JOURNAL_ERLEDIGT_HISTORY_TEXT = 21;
	public final static int REPORT_PROJEKT_JOURNAL_ERLEDIGT_ERLEDIGER = 22;
	public final static int REPORT_PROJEKT_JOURNAL_ERLEDIGT_FREIGEGEBEN = 23;
	public final static int REPORT_PROJEKT_JOURNAL_ERLEDIGT_GESAMTDAUER = 24;
	public final static int REPORT_PROJEKT_JOURNAL_ERLEDIGT_WAHRSCHEINLICHKEIT = 25;
	public final static int REPORT_PROJEKT_JOURNAL_ERLEDIGT_GEPLANTERUMSATZ = 26;
	public final static int REPORT_PROJEKT_JOURNAL_ERLEDIGT_INTERNERLEDIGT_PERSON = 27;
	public final static int REPORT_PROJEKT_JOURNAL_ERLEDIGT_INTERNERLEDIGT_ZEIT = 28;
	public final static int REPORT_PROJEKT_JOURNAL_ERLEDIGT_HISTORY_HTML = 29;
	public final static int REPORT_PROJEKT_JOURNAL_ERLEDIGT_BETREIBER = 30;
	public final static int REPORT_PROJEKT_JOURNAL_ERLEDIGT_ERLEDIGUNGSGRUND = 31;
	public final static int REPORT_PROJEKT_JOURNAL_ERLEDIGT_VKFORTSCHRITT = 32;
	public final static int REPORT_PROJEKT_JOURNAL_ERLEDIGT_VKFORTSCHRITT_BEZEICHNUNG = 33;
	public final static int REPORT_PROJEKT_JOURNAL_ERLEDIGT_VKFORTSCHRITT_LEADSTATUS = 34;
	public final static int REPORT_PROJEKT_JOURNAL_ERLEDIGT_HISTORY_WIRD_DURCHGEFUEHRT_VON = 35;
	public final static int REPORT_PROJEKT_JOURNAL_ERLEDIGT_HISTORY_ERLEDIGUNGSGRAD = 36;
	public final static int REPORT_PROJEKT_JOURNAL_ERLEDIGT_HISTORY_DAUER_GEPLANT = 37;
	public final static int REPORT_PROJEKT_JOURNAL_ERLEDIGT_ANZAHL_SPALTEN = 38;

	public final static int REPORT_PROJEKTBAUM_PROJEKTNUMMER = 0;
	public final static int REPORT_PROJEKTBAUM_TITEL = 1;
	public final static int REPORT_PROJEKTBAUM_STATUS = 2;
	public final static int REPORT_PROJEKTBAUM_EBENE = 3;
	public final static int REPORT_PROJEKTBAUM_KUNDE = 4;
	public final static int REPORT_PROJEKTBAUM_KATEGORIE = 5;
	public final static int REPORT_PROJEKTBAUM_TYP = 6;
	public final static int REPORT_PROJEKTBAUM_TERMIN = 7;
	public final static int REPORT_PROJEKTBAUM_PRIO = 8;
	public final static int REPORT_PROJEKTBAUM_ANZAHL_SPALTEN = 9;

	public final static int REPORT_PROJEKT_ERZEUGER = 0;
	public final static int REPORT_PROJEKT_HISTORY_BELEGDATUM = 1;
	public final static int REPORT_PROJEKT_HISTORY_TEXT = 2;
	public final static int REPORT_PROJEKT_POSITION = 3;
	public final static int REPORT_PROJEKT_ERLEDIGUNGSDATUM = 4;
	public final static int REPORT_PROJEKT_HISTORYART = 5;
	public final static int REPORT_PROJEKT_TITEL = 6;
	public final static int REPORT_PROJEKT_ROT = 7;
	public final static int REPORT_PROJEKT_BLAU = 8;
	public final static int REPORT_PROJEKT_GRUEN = 9;
	public final static int REPORT_PROJEKT_HISTORY_HTML = 10;
	public final static int REPORT_PROJEKT_T_AENDERN = 11;
	public final static int REPORT_PROJEKT_PERSON_AENDERN = 12;
	public final static int REPORT_PROJEKT_KURZZEICHEN_PERSON_AENDERN = 13;
	public final static int REPORT_PROJEKT_HISTORY_ERLEDIGUNGSGRAD = 14;
	public final static int REPORT_PROJEKT_HISTORY_DURCHGEFUEHRT_VON = 15;
	public final static int REPORT_PROJEKT_HISTORY_DAUER_GEPLANT = 16;
	public final static int REPORT_PROJEKT_HISTORY_BELEGDATUM_AS_TIMESTAMP = 17;
	public final static int REPORT_PROJEKT_ANZAHL_SPALTEN = 18;

	public final static int OPTION_SORTIERUNG_AKTIVITAETSUEBERSICHT_PARTNER = 0;
	public final static int OPTION_SORTIERUNG_AKTIVITAETSUEBERSICHT_MITARBEITER = 1;
	public final static int OPTION_SORTIERUNG_AKTIVITAETSUEBERSICHT_BELEGART_BELEGNUMMER = 2;

	public final static int REPORT_AKTIVITAETSUEBERSICHT_PERSONAL = 1;
	public final static int REPORT_AKTIVITAETSUEBERSICHT_PARTNER = 2;
	public final static int REPORT_AKTIVITAETSUEBERSICHT_BELEGART = 3;
	public final static int REPORT_AKTIVITAETSUEBERSICHT_BELEGNUMMER = 4;
	public final static int REPORT_AKTIVITAETSUEBERSICHT_BELEGTEXT = 5;
	public final static int REPORT_AKTIVITAETSUEBERSICHT_BELEG_SUBREPORT = 6;
	public final static int REPORT_AKTIVITAETSUEBERSICHT_DAUER = 7;
	public final static int REPORT_AKTIVITAETSUEBERSICHT_VON = 8;
	public final static int REPORT_AKTIVITAETSUEBERSICHT_BIS = 9;
	public final static int REPORT_AKTIVITAETSUEBERSICHT_LKZ = 10;
	public final static int REPORT_AKTIVITAETSUEBERSICHT_PLZ = 11;
	public final static int REPORT_AKTIVITAETSUEBERSICHT_ORT = 12;
	public final static int REPORT_AKTIVITAETSUEBERSICHT_PROJEKT_PERSONAL_ERZEUGER = 13;
	public final static int REPORT_AKTIVITAETSUEBERSICHT_PROJEKT_PERSONAL_MITARBEITER = 14;
	public final static int REPORT_AKTIVITAETSUEBERSICHT_PROJEKT_BEREICH = 15;
	public final static int REPORT_AKTIVITAETSUEBERSICHT_PROJEKT_INTERNERLEDIGT_PERSON = 16;
	public final static int REPORT_AKTIVITAETSUEBERSICHT_PROJEKT_INTERNERLEDIGT_ZEIT = 17;
	public final static int REPORT_AKTIVITAETSUEBERSICHT_PROJEKT_ZIELWUNSCHTERMIN = 18;
	public final static int REPORT_AKTIVITAETSUEBERSICHT_BELEGSTATUS = 19;
	public final static int REPORT_AKTIVITAETSUEBERSICHT_PROJEKT_BETREIBER = 20;
	public final static int REPORT_AKTIVITAETSUEBERSICHT_ANZAHL_SPALTEN = 21;

	public final static int REPORT_PROJEKTVERLAUF_BELEGART = 0;
	public final static int REPORT_PROJEKTVERLAUF_BELEGNUMMER = 1;
	public final static int REPORT_PROJEKTVERLAUF_AZ_DAUER = 2;
	public final static int REPORT_PROJEKTVERLAUF_AZ_KOSTEN = 3;
	public final static int REPORT_PROJEKTVERLAUF_AZWERT_LOS = 4;
	public final static int REPORT_PROJEKTVERLAUF_MATERIALWERT_LOS = 5;
	public final static int REPORT_PROJEKTVERLAUF_VKWERT_AZ = 6;
	public final static int REPORT_PROJEKTVERLAUF_GESTWERT_AZ = 7;
	public final static int REPORT_PROJEKTVERLAUF_VKWERT_MATERIAL = 8;
	public final static int REPORT_PROJEKTVERLAUF_GESTWERT_MATERIAL = 9;
	public final static int REPORT_PROJEKTVERLAUF_EBENE = 10;
	public final static int REPORT_PROJEKTVERLAUF_LOS_ABGELIEFERTE_MENGE = 12;
	public final static int REPORT_PROJEKTVERLAUF_EKWERT_AZ = 13;
	public final static int REPORT_PROJEKTVERLAUF_EKWERT_MATERIAL = 14;
	public final static int REPORT_PROJEKTVERLAUF_LIEFERANT = 15;
	public final static int REPORT_PROJEKTVERLAUF_STATUS = 16;
	public final static int REPORT_PROJEKTVERLAUF_AUFTRAG_BESTELLNUMMER = 17;
	public final static int REPORT_PROJEKTVERLAUF_LIEFERTERMIN = 18;
	public final static int REPORT_PROJEKTVERLAUF_LOS_BEGINN = 19;
	public final static int REPORT_PROJEKTVERLAUF_LOS_ENDE = 20;
	public final static int REPORT_PROJEKTVERLAUF_BELEGDATUM = 21;
	public final static int REPORT_PROJEKTVERLAUF_REISE_KOMMENTAR = 22;
	public final static int REPORT_PROJEKTVERLAUF_TELEFON_KOMMENTAR_INTERN = 23;
	public final static int REPORT_PROJEKTVERLAUF_TELEFON_KOMMENTAR_EXTERN = 24;
	public final static int REPORT_PROJEKTVERLAUF_ER_KEINE_AUFTRAGSWERTUNG = 25;
	public final static int REPORT_PROJEKTVERLAUF_LOSANTEIL_LIEFERSCHEIN_LOSNUMMER = 26;
	public final static int REPORT_PROJEKTVERLAUF_LOSANTEIL_LIEFERSCHEIN_EINSTANDSWERT_MATERIAL = 27;
	public final static int REPORT_PROJEKTVERLAUF_LOSANTEIL_LIEFERSCHEIN_EINSTANDSWERT_AZ = 28;
	public final static int REPORT_PROJEKTVERLAUF_BESTELLUNG_VERRECHNET_MIT_ER = 29;
	public final static int REPORT_PROJEKTVERLAUF_AZ_ARTIKELNUMMER = 30;
	public final static int REPORT_PROJEKTVERLAUF_AZ_ARTIKELBEZEICHNUNG = 31;
	public final static int REPORT_PROJEKTVERLAUF_AZ_ARTIKELGRUPPE = 32;
	public final static int REPORT_PROJEKTVERLAUF_AZ_VKPREIS = 33;
	public final static int REPORT_PROJEKTVERLAUF_EINSTANDSWERT_MATERIAL = 34;
	public final static int REPORT_PROJEKTVERLAUF_LIEF1WERT_MATERIAL = 35;
	public final static int REPORT_PROJEKTVERLAUF_EINSTANDSWERT_HANDEINGABEN = 36;
	public final static int REPORT_PROJEKTVERLAUF_LOS_STKL_NR = 37;
	public final static int REPORT_PROJEKTVERLAUF_LOS_STKL_BEZ = 38;
	public final static int REPORT_PROJEKTVERLAUF_LOS_STKL_ZBEZ = 39;
	public final static int REPORT_PROJEKTVERLAUF_MASCH_DAUER = 40;
	public final static int REPORT_PROJEKTVERLAUF_MASCH_KOSTEN = 41;
	public final static int REPORT_PROJEKTVERLAUF_MASCH_VKPREIS = 42;
	public final static int REPORT_PROJEKTVERLAUF_ANZAHL_SPALTEN = 43;

	public final static int REPORT_PROJEKTZEITEN_PERSON = 0;
	public final static int REPORT_PROJEKTZEITEN_BELEGART = 1;
	public final static int REPORT_PROJEKTZEITEN_BELEG = 2;
	public final static int REPORT_PROJEKTZEITEN_VON = 3;
	public final static int REPORT_PROJEKTZEITEN_BIS = 4;
	public final static int REPORT_PROJEKTZEITEN_DAUER = 5;
	public final static int REPORT_PROJEKTZEITEN_ARTIKEL = 6;
	public final static int REPORT_PROJEKTZEITEN_BEZEICHNUNG = 7;
	public final static int REPORT_PROJEKTZEITEN_BEMERKUNG = 8;
	public final static int REPORT_PROJEKTZEITEN_KOMMENTAR = 9;
	public final static int REPORT_PROJEKTZEITEN_KOSTEN = 10;
	public final static int REPORT_PROJEKTZEITEN_TELEFONZEIT = 11;
	public final static int REPORT_PROJEKTZEITEN_EBENE = 12;
	public final static int REPORT_PROJEKTZEITEN_ZEITDATEN_I_ID = 13;
	public final static int REPORT_PROJEKTZEITEN_ANZAHL_SPALTEN = 14;

	public static int REPORT_PROJEKTE_EINES_ARTIKELS_BELEGNUMMER = 0;
	public static int REPORT_PROJEKTE_EINES_ARTIKELS_BEREICH = 1;
	public static int REPORT_PROJEKTE_EINES_ARTIKELS_PARTNER = 2;
	public static int REPORT_PROJEKTE_EINES_ARTIKELS_TYP = 3;
	public static int REPORT_PROJEKTE_EINES_ARTIKELS_STATUS = 4;
	public static int REPORT_PROJEKTE_EINES_ARTIKELS_KATEGORIE = 5;
	public static int REPORT_PROJEKTE_EINES_ARTIKELS_PERSON_ZUGEORDNET = 6;
	public static int REPORT_PROJEKTE_EINES_ARTIKELS_T_ANLEGEN = 7;
	public static int REPORT_PROJEKTE_EINES_ARTIKELS_TITEL = 8;
	public static int REPORT_PROJEKTE_EINES_ARTIKELS_PROJEKT_I_ID = 9;
	public static int REPORT_PROJEKTE_EINES_ARTIKELS_ANZAHL_SPALTEN = 10;
	
	public final static int SORTIERUNG_PROJEKTZEITEN_PERSON_BELEGART_BELEG = 0;
	public final static int SORTIERUNG_PROJEKTZEITEN_BELEGART_BELEG_PERSON = 1;
	public final static int SORTIERUNG_PROJEKTZEITEN_TAETIEKGKEIT_DATUM_PERSON = 2;
	public final static int SORTIERUNG_PROJEKTZEITEN_DATUM_ABSTEIGEND_PERSON = 3;

	public JasperPrintLP printProjekt(Integer iIdProjektI, Integer iAnzahlKopienI, Boolean bMitLogo,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printProjektOffene(ReportJournalKriterienDto reportJournalKriterienDtoI, Date dStichtag,
			Integer bereichIId, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printProjektForecast(ReportJournalKriterienDto reportJournalKriterienDtoI, Date dStichtag,
			Integer bereichIId, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printProjektOffeneAuswahlListe(Integer bereichIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printAktivitaetsuebersicht(DatumsfilterVonBis datumsfilter, boolean bGesamtinfo,
			int iSortierung, TheClientDto theClientDto);

	public JasperPrintLP printProjektAlle(ReportJournalKriterienDto reportJournalKriterienDtoI, Date dStichtag,
			Integer bereichIId, boolean belegdatumStattZieltermin, boolean bMitIstzeitdaten, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printProjektAlleDetailliert(ReportJournalKriterienDto reportJournalKriterienDtoI,
			Date dStichtag, Integer bereichIId, boolean belegdatumStattZieltermin, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printProjektErledigt(ReportJournalKriterienDto reportJournalKriterienDtoI, Date dStichtag,
			Integer bereichIId, boolean interneErledigungBeruecksichtigen, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printProjektverlauf(Integer projektIId, boolean bMitAZDetails, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printProjektzeiten(Integer projektIId, int iSortierung, TheClientDto theClientDto);

	public JasperPrintLP printProjektbaum(Integer projektIId, TheClientDto theClientDto);

	public JasperPrintLP printProjektstatistik(Integer projektIId, int iOption, boolean bAktuellerStand,
			TheClientDto theClientDto);

	public JasperPrintLP printAenderungenEigenschaften(Integer projektIId, TheClientDto theClientDto);
	
	public JasperPrintLP printProjektEinesArtikels(Integer artikelIId, boolean bNurOffene, DatumsfilterVonBis vonbis,
			TheClientDto theClientDto);
	

}
