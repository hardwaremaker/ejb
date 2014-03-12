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
package com.lp.server.projekt.service;

import java.rmi.RemoteException;
import java.sql.Date;

import javax.ejb.Remote;

import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;

@Remote
public interface ProjektReportFac {

	public final static String REPORT_MODUL = "projekt";

	public final static String REPORT_PROJEKT = "proj_projekt.jasper";
	public final static String REPORT_PROJEKT_JOURNAL_MITARBEITER = "proj_projekt_mitarbeiter.jasper";
	public final static String REPORT_PROJEKT_JOURNAL_OFFENE = "proj_projekt_journal_offene.jasper";
	public final static String REPORT_PROJEKT_JOURNAL_ALLE = "proj_projekt_journal_alle.jasper";
	public final static String REPORT_PROJEKT_JOURNAL_ERLEDIGT = "proj_projekt_journal_erledigt.jasper";
	public final static String REPORT_PROJEKT_JOURNAL_AKTIVITAETSUEBERSICHT = "proj_aktivitaetsuebersicht.jasper";
	public final static String REPORT_PROJEKTVERLAUF = "proj_projektverlauf.jasper";

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
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_B_VERRECHENBAR = 14;
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
	public final static int REPORT_PROJEKT_JOURNAL_OFFENE_ANZAHL_SPALTEN = 33;

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
	public final static int REPORT_PROJEKT_JOURNAL_ALLE_ANZAHL_SPALTEN = 27;

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
	public final static int REPORT_PROJEKT_JOURNAL_ERLEDIGT_ANZAHL_SPALTEN = 29;

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
	public final static int REPORT_PROJEKT_ANZAHL_SPALTEN = 10;

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
	public final static int REPORT_AKTIVITAETSUEBERSICHT_ANZAHL_SPALTEN = 18;

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
	public final static int REPORT_PROJEKTVERLAUF_ANZAHL_SPALTEN = 25;

	public JasperPrintLP printProjekt(Integer iIdProjektI,
			Integer iAnzahlKopienI, Boolean bMitLogo, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printProjektOffene(
			ReportJournalKriterienDto reportJournalKriterienDtoI,
			Date dStichtag, Integer bereichIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printProjektOffeneAuswahlListe(
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printAktivitaetsuebersicht(java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, boolean bGesamtinfo,
			TheClientDto theClientDto);

	public JasperPrintLP printProjektAlle(
			ReportJournalKriterienDto reportJournalKriterienDtoI,
			Date dStichtag, Integer bereichIId,
			boolean belegdatumStattZieltermin, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printProjektErledigt(
			ReportJournalKriterienDto reportJournalKriterienDtoI,
			Date dStichtag, Integer bereichIId,
			boolean interneErledigungBeruecksichtigen, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printProjektverlauf(Integer projektIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

}
