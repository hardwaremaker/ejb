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
package com.lp.server.personal.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.DatumsfilterVonBis;
import com.lp.server.util.report.JasperPrintLP;

@Remote
public interface ZeiterfassungReportFac {
	public final static String REPORT_MODUL = "personal";
	public final static String REPORT_ZESTIFTE = "pers_zestifte.jasper";
	public final static String REPORT_MASCHINENPRODUKTIVITAET = "pers_maschinenproduktivitaet.jasper";
	public final static String REPORT_MASCHINENLISTE = "pers_maschinenliste.jasper";
	public final static String REPORT_MASCHINENZEITDATEN = "pers_maschinenzeitdaten.jasper";
	public final static String REPORT_ZEITDATEN = "pers_zeitdaten.jasper";
	public final static String REPORT_MASCHINENBELEGUNG = "pers_maschinenbelegung.jasper";
	public final static String REPORT_MITARBEITEREINTEILUNG = "pers_mitarbeitereinteilung.jasper";
	public final static String REPORT_MASCHINENERFOLG = "pers_maschinenerfolg.jasper";
	public final static String REPORT_ABGESCHLOSSENE_ZEITBUCHUNGEN = "pers_abgeschlossene_zeitbuchungen.jasper";
	
	
	
	public final static String REPORT_REISEZEITEN = "pers_reisezeiten.jasper";
	public final static String REPORT_FAHRTENBUCH = "pers_fahrtenbuch.jasper";

	public final static String REPORT_ARBEITSZEITSTATISTIK = "pers_arbeitszeitstatistik.jasper";
	public final static String REPORT_AUFTRAGSZEITSTATISTIK = "pers_auftragszeitstatistik.jasper";
	
	public final static String REPORT_ZEITERFASSUNG_AENDERUNGEN = "pers_zeiterfassung_aenderungen.jasper";

	public final static int REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_AUFTRAG = 0;
	public final static int REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_ADRESSE = 1;
	public final static int REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_PERSONAL = 2;
	public final static int REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_ARTIKEL = 3;
	public final static int REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_ARTIKELGRUPPE = 4;
	public final static int REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_ARTIKELKLASSE = 5;
	public final static int REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_KOSTENSTELLE = 6;
	public final static int REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_KUNDE_BELEG_PERSONAL = 7;

	public final static String REPORT_ARBEITSZEITSTATISTIKVERDICHTET = "pers_arbeitszeitstatistikverdichtet.jasper";

	public final static String REPORT_MITARBEITERUEBERSICHT = "pers_mitarbeiteruebersicht.jasper";
	public final static String REPORT_PRODUKTIVITAETSTAGESSTATISTIK = "pers_produktivitaetstagesstatistik.jasper";

	public final static String REPORT_TELEFONZEITEN = "pers_telefonzeiten.jasper";
	public final static String REPORT_FAHRZEUGE = "pers_fahrzeuge.jasper";
	public final static String REPORT_URLAUBSANTRAG = "pers_urlaubsantrag.jasper";

	
	
	public static int REPORT_ZEITDATEN_PERSONALNR = 0;
	public static int REPORT_ZEITDATEN_NAME = 1;
	public static int REPORT_ZEITDATEN_ZEIT = 2;
	public static int REPORT_ZEITDATEN_TAETIGKEIT = 3;
	public static int REPORT_ZEITDATEN_AUFTRAG = 4;
	public static int REPORT_ZEITDATEN_PROJEKTBEZEICHNUNG = 5;
	public static int REPORT_ZEITDATEN_TAGESSUMME = 6;
	public static int REPORT_ZEITDATEN_MASCHINE = 7;
	public static int REPORT_ZEITDATEN_POSITION = 8;
	public static int REPORT_ZEITDATEN_BUCHUNGSART = 9;
	public static int REPORT_ZEITDATEN_BEMERKUNG = 10;
	public static int REPORT_ZEITDATEN_GUTSTK = 11;
	public static int REPORT_ZEITDATEN_SCHLECHTSTK = 12;
	public static int REPORT_ZEITDATEN_KUNDE = 13;
	public static int REPORT_ZEITDATEN_DAUER = 14;
	public static int REPORT_ZEITDATEN_KOMMENTAR = 15;
	public static int REPORT_ZEITDATEN_QUELLE = 16;
	public static int REPORT_ZEITDATEN_TAETIGKEIT_SONDERZEIT = 17;
	public static int REPORT_ZEITDATEN_DAUER_SONDERZEIT = 18;
	public static int REPORT_ZEITDATEN_DATUM_SONDERZEIT = 19;
	public static int REPORT_ZEITDATEN_ARTIKELBEZEICHNUNG = 20;
	public static int REPORT_ZEITDATEN_SOLLZEIT = 21;
	public static int REPORT_ZEITDATEN_ZEIT_BIS = 22;
	public static int REPORT_ZEITDATEN_ZUSATZ = 23;
	public static int REPORT_ZEITDATEN_ANZAHL_SPALTEN = 24;

	
	public JasperPrintLP printZestiftliste(TheClientDto theClientDto)
			throws RemoteException;

	public JasperPrintLP printAuftragszeitstatistik(java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, TheClientDto theClientDto);

	public JasperPrintLP printTelefonzeiten(Integer personalIId,
			Integer partnerIId, java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, boolean bSortiertNachPersonal,
			TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printReisezeiten(Integer personalIId, Timestamp tVon,
			Timestamp tBis, Integer iOption, boolean bPlusVersteckte,boolean bNurAnwesende,
			TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printArbeitszeitstatistik(java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, int iOptionSortierung, String belegartCNr,
			Integer belegartIId, Integer personalIId, Integer artikelIId,
			Integer partnerIId, Integer artikelgruppeIId,
			Integer artikelklasseIId, boolean bVerdichtet,
			TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printMitarbeiteruebersicht(Integer personalIId,
			Integer iJahrVon, Integer iMonatVon, Integer iJahrBis,
			Integer iMonatBis, Integer iOption, Integer iOptionSortierung,
			boolean bPlusVersteckte, boolean bNurAnwesende, TheClientDto theClientDto)
			throws RemoteException;

	public JasperPrintLP printFahrtenbuch(Integer personalIId, Timestamp tVon,
			Timestamp tBis, Integer iOption, boolean bPlusVersteckte,boolean bNurAnwesende,
			TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printMaschinenproduktivitaet(Integer maschineIId,
			Timestamp tVon, Timestamp tBis, TheClientDto theClientDto)
			throws RemoteException;

	public JasperPrintLP printMaschinenliste(Timestamp tStichtag,
			boolean bMitVersteckten, TheClientDto theClientDto)
			throws RemoteException;

	public MaschinenerfolgReportDto printMaschinenerfolg(Integer personalIId,
			Integer personalgruppeIId, java.sql.Timestamp tVon, java.sql.Timestamp tBis,
			Integer iOptionSortierung, String sortierung, boolean bMonatsbetrachtung,
			TheClientDto theClientDto);
	
	public JasperPrintLP printMaschinenzeitdaten(Integer maschineIId,
			Timestamp tVon, Timestamp tBis, TheClientDto theClientDto)
			throws RemoteException;

	
	public JasperPrintLP printZeitdatenjournal(Integer personalIId,
			Timestamp tVon, Timestamp tBis, TheClientDto theClientDto)
			throws RemoteException;

	public JasperPrintLP printMaschinenbelegung(Integer maschineIId,
			java.sql.Timestamp tStichtag, boolean bMitErstemUagDesNaechstenAg,
			TheClientDto theClientDto);

	public JasperPrintLP printMitarbeitereinteilung(Integer personalIId,
			Integer personalgruppeIId, java.sql.Timestamp tStichtag,
			Integer iOptionSortierung, String sortierung,
			TheClientDto theClientDto);
	
	public JasperPrintLP printProduktivitaetstagesstatistik(Integer personalIId,
			java.sql.Timestamp tVon, java.sql.Timestamp tBis,Integer iOption, boolean bMitVersteckten, boolean bNurAnwesende, boolean bMonatsbetrachtung, Integer personalgruppeIId,
			TheClientDto theClientDto);
	
	public JasperPrintLP printFahrzeuge(Integer fahrzeugIId,
			java.sql.Timestamp tVon, java.sql.Timestamp tBis, TheClientDto theClientDto);
	public Object[][] erstelleZeitdatenjournal(Integer personalIId,
			java.sql.Timestamp tVon, java.sql.Timestamp tBis,
			TheClientDto theClientDto);
	public JasperPrintLP printUrlaubsantrag(Integer personalIId,
			Integer[] integerIIds, boolean bGenehmigt, String cVorraussetzung,
			TheClientDto theClientDto);
	public JasperPrintLP printAbgeschlosseneZeitbuchungen(
			boolean bMitVersteckten, TheClientDto theClientDto);
	public JasperPrintLP printAenderungen(Integer personalIId,
			DatumsfilterVonBis datumsfilter,
			boolean mitInserts, boolean mitUpdates, boolean mitDeletes,
			TheClientDto theClientDto);
	
	
}
