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
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.auftrag.service.AuftragzeitenDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.LPDatenSubreport;

public interface ZeiterfassungFacAll {

	public static int REPORT_LOHNDATENEXPORT_PERSONALNUMMER = 0;
	public static int REPORT_LOHNDATENEXPORT_VORNAME = 1;
	public static int REPORT_LOHNDATENEXPORT_NACHNAME = 2;
	public static int REPORT_LOHNDATENEXPORT_LOHNART = 3;
	public static int REPORT_LOHNDATENEXPORT_LOHNART_NR = 4;
	public static int REPORT_LOHNDATENEXPORT_LOHNSTUNDENART = 5;
	public static int REPORT_LOHNDATENEXPORT_JAHR = 6;
	public static int REPORT_LOHNDATENEXPORT_MONAT = 7;
	public static int REPORT_LOHNDATENEXPORT_STUNDEN = 8;
	public static int REPORT_LOHNDATENEXPORT_SUBREPORT_SONDERZEITEN_VON_BIS = 9;
	public static int REPORT_LOHNDATENEXPORT_ARRAYLIST_VON_BIS = 10;
	public static int REPORT_LOHNDATENEXPORT_ZEITMODELL = 11;
	public static int REPORT_LOHNDATENEXPORT_AUSFALLSPRINZIP_WOCHEN = 12;
	public static int REPORT_LOHNDATENEXPORT_AUSFALLSPRINZIP_UESTD = 13;
	public static int REPORT_LOHNDATENEXPORT_SCHICHTZEIT = 14;
	public static int REPORT_LOHNDATENEXPORT_TAETIGKEIT = 15;
	public static int REPORT_LOHNDATENEXPORT_ANZAHL_SPALTEN = 16;

	public static final String FLR_ZEITMODELL_B_VERSTECKT = "b_versteckt";

	public static final String FLR_ZEITMODELL_ZEITMODELLSPRSET = "zeitmodellsprset";

	public static final String FLR_ZEITMODELLTAG_U_SOLLZEIT = "u_sollzeit";
	public static final String FLR_ZEITMODELLTAG_U_ERLAUBTEANWESENHEITSZEIT = "u_erlaubteanwesenheitszeit";
	public static final String FLR_ZEITMODELLTAG_U_MINDESTPAUSE = "u_mindestpause";
	public static final String FLR_ZEITMODELLTAG_U_MINDESTPAUSE2 = "u_mindestpause2";
	public static final String FLR_ZEITMODELLTAG_U_AUTOPAUSEAB = "u_autopauseab";
	public static final String FLR_ZEITMODELLTAG_U_AUTOPAUSEAB2 = "u_autopauseab2";
	public static final String FLR_ZEITMODELLTAG_FLRTAGESART = "flrtagesart";
	public static final String FLR_ZEITMODELLTAG_FLRZEITMODELL = "flrzeitmodell";

	public static final String FLR_ZEITMODELLTAGPAUSE_U_BEGINN = "u_beginn";
	public static final String FLR_ZEITMODELLTAGPAUSE_U_ENDE = "u_ende";
	public static final String FLR_ZEITMODELLTAGPAUSE_FLRZEITMODELLTAG = "flrzeitmodelltag";

	public static final String FLR_TAETIGKEIT_TAETIGKEITART_C_NR = "taetigkeitart_c_nr";
	public static final String FLR_TAETIGKEIT_B_TAGBUCHBAR = "b_tagbuchbar";
	public static final String FLR_TAETIGKEIT_B_DARF_SELBER_BUCHEN = "b_darf_selber_buchen";
	public static final String FLR_TAETIGKEIT_B_BDEBUCHBAR = "b_bdebuchbar";
	public static final String FLR_TAETIGKEIT_FLRARTIKEL = "flrartikel";
	public static final String FLR_TAETIGKEIT_TAETIGKEITSPRSET = "taetigkeitsprset";

	public static final String FLR_TELEFONZEITEN_PERSONAL_I_ID = "personal_i_id";
	public static final String FLR_TELEFONZEITEN_PARTNER_I_ID = "partner_i_id";
	public static final String FLR_TELEFONZEITEN_T_VON = "t_von";
	public static final String FLR_TELEFONZEITEN_T_BIS = "t_bis";
	public static final String FLR_TELEFONZEITEN_X_KOMMENTAREXT = "x_kommentarext";
	public static final String FLR_TELEFONZEITEN_X_KOMMENTARINT = "x_kommentarint";
	public static final String FLR_TELEFONZEITEN_FLRPARTNER = "flrpartner";
	public static final String FLR_TELEFONZEITEN_FLRANSPRECHPARTNER = "flransprechpartner";
	public static final String FLR_TELEFONZEITEN_FLRPERSONAL = "flrpersonal";

	public static final String FLR_ZEITDATEN_T_ZEIT = "t_zeit";
	public static final String FLR_ZEITDATEN_FLRPERSONAL = "flrpersonal";
	public static final String FLR_ZEITDATEN_C_BELEGARTNR = "c_belegartnr";
	public static final String FLR_ZEITDATEN_I_BELEGARTID = "i_belegartid";
	public static final String FLR_ZEITDATEN_B_AUTOMATIKBUCHUNG = "b_automatikbuchung";
	public static final String FLR_ZEITDATEN_PERSONAL_I_ID = "personal_i_id";
	public static final String FLR_ZEITDATEN_ARTIKEL_I_ID = "artikel_i_id";
	// public static final String FLR_ZEITDATEN_MASCHINE_I_ID = "maschine_i_id";
	public static final String FLR_ZEITDATEN_TAETIGKEIT_I_ID = "taetigkeit_i_id";
	public static final String FLR_ZEITDATEN_C_BEMERKUNGZUBELEGART = "c_bemerkungzubelegart";
	public static final String FLR_ZEITDATEN_C_WOWURDEGEBUCHT = "c_wowurdegebucht";
	public static final String FLR_ZEITDATEN_X_KOMMENTAR = "x_kommentar";

	public static final String FLR_ZEITDATENPRUEFEN_T_DATUM = "t_datum";
	public static final String FLR_ZEITDATENPRUEFEN_I_FEHLERCODE = "i_fehlercode";
	public static final String FLR_ZEITDATENPRUEFEN_X_FEHLERTEXT = "x_fehlertext";

	public static final String FLR_MASCHINENZEITDATEN_T_VON = "t_von";
	public static final String FLR_MASCHINENZEITDATEN_T_BIS = "t_bis";
	public static final String FLR_MASCHINENZEITDATEN_FLRMASCHINE = "flrmaschine";
	public static final String FLR_MASCHINENZEITDATEN_FLRLOSSOLLARBEITSPLAN = "flrlossollarbeitsplan";
	public static final String FLR_MASCHINENZEITDATEN_C_BEMERKUNG = "c_bemerkung";

	public static final String FLR_ZEITDATEN_I_BELEGARTPOSITIONID = "i_belegartpositionid";
	public static final String FLR_ZEITDATEN_FLRMASCHINE = "flrmaschine";
	public static final String FLR_ZEITDATEN_FLRARTIKEL = "flrartikel";

	public static final String FLR_ZEITDATENLOS_FLRSOLLARBEITSPLAN = "flrlossollarbeitsplan";

	public static final String FLR_ZEITDATEN_T_AENDERN = "t_aendern";
	public static final String FLR_ZEITDATEN_FLRTAETIGKEIT = "flrtaetigkeit";

	public static final String FLR_SONDERZEITEN_PERSONAL_I_ID = "personal_i_id";
	public static final String FLR_SONDERZEITEN_D_DATUM = "t_datum";
	public static final String FLR_SONDERZEITEN_B_TAG = "b_tag";
	public static final String FLR_SONDERZEITEN_B_HALBTAG = "b_halbtag";
	public static final String FLR_SONDERZEITEN_U_STUNDEN = "u_stunden";
	public static final String FLR_SONDERZEITEN_FLRTAETIGKEIT = "flrtaetigkeit";

	public static final String FLR_MASCHINE_C_INVENTARNUMMER = "c_inventarnummer";
	public static final String FLR_MASCHINE_C_IDENTIFIKATIONSNR = "c_identifikationsnr";
	public static final String FLR_MASCHINE_T_KAUFDATUM = "t_kaufdatum";

	public static final String FLR_MASCHINE_MASCHINENGRUPPE_I_ID = "maschinengruppe_i_id";
	public static final String FLR_MASCHINE_B_VERSTECKT = "b_versteckt";
	public static final String FLR_MASCHINE_FLR_MASCHINENGRUPPE = "flrmaschinengruppe";

	public static final String FLR_MASCHINENKOSTEN_T_GUELTIGAB = "t_gueltigab";
	public static final String FLR_MASCHINENKOSTEN_N_STUNDENSATZ = "n_stundensatz";
	public static final String FLR_MASCHINENKOSTEN_N_VKSTUNDENSATZ = "n_vkstundensatz";
	public static final String FLR_MASCHINENKOSTEN_FLRMASCHINE = "flrmaschine";

	public static final String FLR_ZEITSTIFT_B_MEHRFACHSTIFT = "b_mehrfachstift";
	public static final String FLR_ZEITSTIFT_B_PERSONENZUORDNUNG = "b_personenzuordnung";
	public static final String FLR_ZEITSTIFT_C_TYP = "c_typ";
	public static final String FLR_ZEITSTIFT_FLRPERSONAL = "flrpersonal";

	public static final String FLR_REISE_PERSONAL_I_ID = "personal_i_id";
	public static final String FLR_REISE_FAHRZEUG_I_ID = "fahrzeug_i_id";
	public static final String FLR_REISE_BELEGART_C_NR = "belegart_c_nr";
	public static final String FLR_REISE_I_BELEGARTID = "i_belegartid";
	public static final String FLR_REISE_T_ZEIT = "t_zeit";
	public static final String FLR_REISE_B_BEGINN = "b_beginn";
	public static final String FLR_REISE_C_KOMMENTAR = "c_kommentar";
	public static final String FLR_REISE_N_SPESEN = "n_spesen";
	public static final String FLR_REISE_I_KMBEGINN = "i_kmbeginn";
	public static final String FLR_REISE_I_KMENDE = "i_kmende";
	public static final String FLR_REISE_FLRDIAETEN = "flrdiaeten";
	public static final String FLR_REISE_FLRPERSONAL = "flrpersonal";
	public static final String FLR_REISE_FLRPARTNER = "flrpartner";

	public static final String FLR_DIAETEN_FLRLAND = "flrland";

	public static final String FLR_DIAETENTAGESSATZ_FLRDIAETEN = "flrdiaeten";
	public static final String FLR_DIAETENTAGESSATZ_T_GUELTIGAB = "t_gueltigab";
	public static final String FLR_DIAETENTAGESSATZ_N_STUNDENSATZ = "n_stundensatz";
	public static final String FLR_DIAETENTAGESSATZ_N_TAGESSATZ = "n_tagessatz";
	public static final String FLR_DIAETENTAGESSATZ_N_MINDESTSATZ = "n_mindestsatz";
	public static final String FLR_DIAETENTAGESSATZ_B_STUNDENWEISE = "b_stundenweise";
	public static final String FLR_DIAETENTAGESSATZ_I_ABSTUNDEN = "i_abstunden";

	public final static int SORTIERUNG_ZEITDATEN_ARTIKEL = 0;
	public final static int SORTIERUNG_ZEITDATEN_PERSONAL = 1;
	public final static int SORTIERUNG_ZEITDATEN_ZEITPUNKT_PERSONAL = 2;

	public static final String TAGESART_MONTAG = "Montag";
	public static final String TAGESART_DIENSTAG = "Dienstag";
	public static final String TAGESART_MITTWOCH = "Mittwoch";
	public static final String TAGESART_DONNERSTAG = "Donnerstag";
	public static final String TAGESART_FREITAG = "Freitag";
	public static final String TAGESART_SAMSTAG = "Samstag";
	public static final String TAGESART_SONNTAG = "Sonntag";
	public static final String TAGESART_FEIERTAG = "Feiertag";
	public static final String TAGESART_HALBTAG = "Halbtag";
	public static final String TAGESART_BETRIEBSURLAUB = "BU";

	public static final String TAETIGKEITART_SONDERTAETIGKEIT = "Sonder         ";
	public static final String TAETIGKEITART_ARBEITSZEIT = "Arbeitszeit    ";
	public static final String TAETIGKEITART_MASCHINENZEIT = "Maschinenzeit  ";

	public static final String TAETIGKEIT_KOMMT = "KOMMT          ";
	public static final String TAETIGKEIT_GEHT = "GEHT           ";
	public static final String TAETIGKEIT_UNTER = "UNTER          ";
	public static final String TAETIGKEIT_URLAUB = "URLAUB         ";
	public static final String TAETIGKEIT_ENDE = "ENDE           ";
	public static final String TAETIGKEIT_ZEITAUSGLEICH = "ZEITAUSGLEICH  ";
	public static final String TAETIGKEIT_BEHOERDE = "BEHOERDE       ";
	public static final String TAETIGKEIT_ARZT = "ARZT           ";
	public static final String TAETIGKEIT_KRANK = "KRANK          ";
	public static final String TAETIGKEIT_KINDKRANK = "KINDKRANK      ";
	public static final String TAETIGKEIT_TELEFON = "TELEFON        ";
	public static final String TAETIGKEIT_URLAUBSANTRAG = "URLAUBSANTRAG  ";
	public static final String TAETIGKEIT_ZAANTRAG = "ZAANTRAG       ";
	public static final String TAETIGKEIT_KRANKANTRAG = "KRANKANTRAG    ";
	// Speziell fuer MECSTERMINAL
	public static final String TAETIGKEIT_REISE = "REISE          ";

	public final static String REPORT_MODUL = "personal";

	public final static String REPORT_MONATSABRECHNUNG = "pers_monatsabrechnung.jasper";
	public final static String REPORT_ANWESENHEITSLISTE = "pers_anwesenheitsliste.jasper";
	public final static String REPORT_SONDERZEITENLISTE = "pers_sonderzeitenliste.jasper";
	public final static String REPORT_PRODUKTIVITAETSSTATISTIK = "pers_produktivitaetsstatistik.jasper";
	public final static String REPORT_SONDERTAETIKGEITEN = "pers_sondertaetigkeiten.jasper";
	public final static String REPORT_WOCHENABRECHNUNG = "pers_wochenabrechnung.jasper";
	public final static String REPORT_WOCHENJOURNAL = "pers_wochenjournal.jasper";
	public final static String REPORT_ZEITSALDO = "pers_zeitsaldo.jasper";
	public final static String REPORT_LOHNDATENEXPORT = "pers_lohndatenexport.jasper";
	public final static String REPORT_WOCHENABSCHLUSS = "pers_wochenabschluss.jasper";

	public final static int REPORT_SONDERZEITENLISTE_OPTION_SELEKTIERTE_PERSON = 0;
	public final static int REPORT_SONDERZEITENLISTE_OPTION_ALLE_PERSONEN = 1;
	public final static int REPORT_SONDERZEITENLISTE_OPTION_ALLE_ARBEITER = 2;
	public final static int REPORT_SONDERZEITENLISTE_OPTION_ALLE_ANGESTELLTE = 3;
	public final static int REPORT_SONDERZEITENLISTE_OPTION_EINE_ABTEILUNG = 4;

	public final static int REPORT_SONDERZEITENLISTE_SORTIERUNG_PERSONALNUMMER = 0;
	public final static int REPORT_SONDERZEITENLISTE_SORTIERUNG_NACHNAME_VORNAME = 1;
	public final static int REPORT_SONDERZEITENLISTE_SORTIERUNG_SONDERTAETIGKEIT_VORNAME_NACHNAME = 2;

	public final static int REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_PERSONALNUMMER = 0;
	public final static int REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_NAME_VORNAME = 1;
	public final static int REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_ABTEILUNG_NAME_VORNAME = 2;
	public final static int REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_KOSTENSTELLE_NAME_VORNAME = 3;
	public final static int REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_ABTEILUNG_KOSTENSTELLE_NAME_VORNAME = 4;

	public final static String REPORT_ZEITERFASSUNG_ZEITDATEN_P_S_PERSON = "S_PERSON";

	public final static String REPORT_ZEITERFASSUNG_ZEITDATEN_T_ZEIT = "T_ZEIT";
	public final static String REPORT_ZEITERFASSUNG_ZEITDATEN_PERSONAL_I_ID = "PERSONAL_I_ID";

	public final static String REPORT_ZEITERFASSUNG_ZEITDATEN_P_S_VON = "S_VON";
	public final static String REPORT_ZEITERFASSUNG_ZEITDATEN_P_S_BIS = "S_BIS";

	// Feldlaengen
	public static final int MAX_ZEITMODELL_KENNUNG = 15;
	public static final int MAX_ZEITMODELL_BEZEICHNUNG = 40;

	public static final int MAX_TAETIGKEIT_KENNUNG = 15;
	public static final int MAX_TAETIGKEIT_BEZEICHNUNG = 40;

	public static final int MAX_ZEITDATEN_BEMERKUNG = 80;

	public static final int MAX_REISEZEITEN_KOMMENTAR = 80;

	public static final String REISELOG_ART_CREATE = "CREATE";
	public static final String REISELOG_ART_UPDATE = "UPDATE";
	public static final String REISELOG_ART_DELETE = "DELETE";

	public static final String ZEITSTIFT_TYP_F630 = "F630";
	public static final String ZEITSTIFT_TYP_KDC100 = "KDC100";

	public final static int BELEGZEITEN_ALLE = 0;
	public final static int BELEGZEITEN_NUR_PERSONALZEITEN = 1;
	public final static int BELEGZEITEN_NUR_MASCHINENZEITEN = 2;

	public final static int UESTAUSZAHLEN_KEINE = 0;
	public final static int UESTAUSZAHLEN_FOLGEMONAT = 1;
	public final static int UESTAUSZAHLEN_ABRECHNUNGSMONAT = 2;

	public final static String TELEFONZEITEN_TODO_ANSPRECHPARTNER = "TELEFONZEITEN_TODO_ANSPRECHPARTNER";
	public final static String TELEFONZEITEN_TODO_TEXT = "TELEFONZEITEN_TODO_TEXT";

	public final static String TELEFONZEITEN_TODO_OPTION_OFFENE_WIEDERVORLAGEN = "0";
	public final static String TELEFONZEITEN_TODO_OPTION_ALLE_WIEDERVORLAGEN = "1";
	public final static String TELEFONZEITEN_TODO_OPTION_ALLE_TELEFONZEITEN = "2";

	public static final String MONATSABRECHNUNG_INFO_EMAIL_VERSAND = "INFO_EMAIL_VERSAND";

	public Double berechneTagesArbeitszeit(Integer personalIId, Date d_datum, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Double berechneTagesArbeitszeit(Integer personalIId, Date d_datum, boolean bAnwesenheitszeit,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public SollverfuegbarkeitDto[] getVerfuegbareSollzeit(Timestamp tVon, Timestamp tBis, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Double berechneArbeitszeitImZeitraum(Integer personalIId, Date dDatumVon, Date dDatumBis,
			boolean bAbzueglichTelefonzeiten, TheClientDto theClientDto);

	public double berechneDauerPaarweiserSondertaetigkeitenEinerPersonUndEinesZeitraumes(Integer personalIId,
			Timestamp tVon, Timestamp tBis, Integer iTaetigkeit) throws RemoteException;

	public JasperPrintLP printWochenabrechnung(Integer personalIId, Timestamp tVon, Timestamp tBis,
			TheClientDto theClientDto, Integer iOption, Integer kostenstelleIIdAbteilung, Boolean bPlusVersteckte)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printWochenjournal(Integer personalIId, Timestamp tVon, Timestamp tBis,
			TheClientDto theClientDto, Integer iOption, Integer kostenstelleIIdAbteilung, Boolean bPlusVersteckte,
			boolean bNurAnwesende);

	public double berechnePaarweiserSondertaetigkeiten(ZeitdatenDto[] zeitdaten, Integer iTaetigkeit)
			throws Exception, RemoteException;

	public Integer createZeitmodell(ZeitmodellDto zeitmodellDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeZeitmodell(ZeitmodellDto zeitmodellDto) throws EJBExceptionLP, RemoteException;

	public void updateZeitmodell(ZeitmodellDto zeitmodellDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void uebersteuereZeitmodellFuerEinenTag(Integer personalIId, Integer zeitmodellIId, Date dDatum,
			TheClientDto theClientDto) throws RemoteException;

	public ZeitmodellDto zeitmodellFindByCNr(String cNr, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ZeitmodellDto zeitmodellFindByPrimaryKey(Integer iId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Integer createZeitmodelltag(ZeitmodelltagDto zeitmodelltagDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeZeitmodelltag(ZeitmodelltagDto zeitmodelltagDto) throws EJBExceptionLP, RemoteException;

	public void updateZeitmodelltag(ZeitmodelltagDto zeitmodelltagDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ZeitmodelltagDto zeitmodelltagFindByPrimaryKey(Integer iId) throws EJBExceptionLP, RemoteException;

	public ZeitmodelltagDto[] zeitmodelltagFindByZeitmodellIId(Integer iId) throws EJBExceptionLP, RemoteException;

	public Map<?, ?> getAllSprTagesarten(String cNrSpracheI) throws EJBExceptionLP, RemoteException;

	public Map<?, ?> getAllSprTagesartenEinesZeitmodells(Integer zeitmodellIId, String cNrSpracheI)
			throws EJBExceptionLP, RemoteException;

	public Map<?, ?> getAllSprTaetigkeitarten(String cNrSpracheI) throws EJBExceptionLP, RemoteException;

	public Map<Integer, String> getAllSprSondertaetigkeiten(String cNrSpracheI) throws RemoteException;

	public Map<Integer, String> getAllSprSondertaetigkeitenOhneVersteckt(String cNrSpracheI) throws RemoteException;

	public Map<Integer, String> getAllSprSondertaetigkeitenNurBDEBuchbar(String cNrSpracheI) throws RemoteException;

	public Map<Integer, String> getAllSprSondertaetigkeitenNurBDEBuchbarOhneVersteckt(String cNrSpracheI)
			throws RemoteException;

	public Double getSummeZeitenEinesBeleges(String belegartCNr, Integer belegartIId, Integer belegartpositionIId,
			Integer personalIId, Timestamp tVon, Timestamp tBis, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printProduktivitaetsstatistik(Integer personalIId, Timestamp tVon, Timestamp tBis,
			Integer iOption, Integer kostenstelleIIdAbteilung, Boolean bPlusVersteckte, boolean bNurAnwesende,
			boolean bVerdichtet, boolean bMonatsbetrachtung, boolean projekteZusammengefasst, TheClientDto theClientDto)
			throws RemoteException;

	public PersonalDto[] getPersonenDieZeitmodellVerwenden(Integer zeitmodellIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ArrayList<MaschinenzeitdatenDto> getZeitdatenEinerMaschine(Integer maschineIId, Timestamp tZeitenVon,
			Timestamp tZeitenBis, TheClientDto theClientDto) throws RemoteException;

	public AuftragzeitenDto[] getAllMaschinenzeitenEinesBeleges(Integer losIId, Integer lossollarbeitsplanIId,
			java.sql.Timestamp tVon, java.sql.Timestamp tZeitenBis, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Double getSummeMaschinenZeitenEinesBeleges(Integer losIId, Integer lossollarbeitsplanIId,
			java.sql.Timestamp tZeitenBis, TheClientDto theClientDto);

	public AuftragzeitenDto[] getAllZeitenEinesBeleges(String belegartCNr, Integer belegartIId,
			Integer belegartpositionIId, Integer personalIId, Timestamp tZeitenVon, Timestamp tZeitenBis,
			int iSortierung, TheClientDto theClientDto);

	public Map<?, ?> getAllSprTagesartenOhneMontagBisSonntag(String cNrSpracheI) throws EJBExceptionLP, RemoteException;

	public Integer createTagesart(TagesartDto tagesartDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Double getSummeSollzeitWochentags(Integer zeitmodellIId) throws EJBExceptionLP, RemoteException;

	public Double getSummeSollzeitMontagBisSonntag(Integer zeitmodellIId);

	public Double getSummeSollzeitSonnUndFeiertags(Integer zeitmodellIId) throws EJBExceptionLP, RemoteException;

	public void removeTagesart(TagesartDto tagesartDto) throws EJBExceptionLP, RemoteException;

	public void updateTagesart(TagesartDto tagesartDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public TagesartDto tagesartFindByPrimaryKey(Integer iId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public TagesartDto tagesartFindByCNr(String cNr, TheClientDto theClientDto) throws EJBExceptionLP;

	public TagesartDto[] tagesartFindAll() throws EJBExceptionLP, RemoteException;

	public BigDecimal getStundenAllerBezahltenSondertaetigkeitenImZeitraum(Integer personalIId,
			Integer tagesartIId_Feiertag, Integer tagesartIId_Halbtag, java.sql.Timestamp tVon, java.sql.Timestamp tBis,
			TheClientDto theClientDto);

	public void automatikbuchungenAufrollen(Date tVon, Date tBis, Integer personalIId, TheClientDto theClientDto,
			boolean bLoeschen) throws EJBExceptionLP, RemoteException;

	public void pruefeUndErstelleAutomatischePausen(Timestamp tZeitpunkt, Integer personalIId,
			TheClientDto theClientDto);

	public Time getRelativeZeitFuerRelativesAendernAmClient(Integer personalIId, Timestamp tBelegbuchung)
			throws RemoteException;

	public void aendereZeitRelativ(ZeitdatenDto zeitdatenDto, Time tZeitRelativ, TheClientDto theClientDto)
			throws RemoteException;

	public Integer bucheZeitRelativ(ZeitdatenDto zeitdatenDto, Timestamp tsAbDieserZeit, Boolean bAuchWennZuWenigZeit,
			boolean bIgnoriereFehler, TheClientDto theClientDto) throws RemoteException;

	public void speichereZeidatenVonZEStift(ZeitdatenDto[] zeitdatenDtos, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal getMengeGutSchlechtEinesLosSollarbeitsplanes(Integer lossollarbeitsplanIId,
			TheClientDto theClientDto, Boolean bGut) throws RemoteException;

	public Integer createZeitdaten(ZeitdatenDto zeitdatenDto, boolean bBucheAutoPausen,
			boolean bBucheMitternachtssprung, boolean bZeitverteilen, boolean bLospruefungAufFertig,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Integer createZeitdaten(ZeitdatenDto zeitdatenDto, boolean bBucheAutoPausen,
			boolean bBucheMitternachtssprung, boolean bZeitverteilen, boolean bLospruefungAufFertig,
			TheClientDto theClientDto, ArrayList<ZeitverteilenGutSchlechtDto> alGutSchlecht)
			throws EJBExceptionLP, RemoteException;

	public void removeZeitdaten(ZeitdatenDto zeitdatenDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateZeitdaten(ZeitdatenDto zeitdatenDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ZeitdatenDto zeitdatenFindByPrimaryKey(Integer iId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	ZeitdatenDto zeitdatenFindByPrimaryKeyOhneExc(Integer iId) throws RemoteException;

	public ZeitdatenDto zeitdatenFindByPersonalIIdTZeit(Integer personalIId, Timestamp tZeit)
			throws EJBExceptionLP, RemoteException;

	public ZeitdatenDto[] zeitdatenFindZeitdatenEinesTagesUndEinerPerson(Integer personalIId, Timestamp tVon,
			Timestamp tBis) throws EJBExceptionLP, RemoteException;

	public ZeitdatenDto[] zeitdatenFindZeitdatenEinesTagesUndEinerPersonOnheBelegzeiten(Integer personalIId,
			Timestamp tVon, Timestamp tBis) throws EJBExceptionLP, RemoteException;

	public Integer createTaetigkeit(TaetigkeitDto taetigkeitDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeTaetigkeit(TaetigkeitDto taetigkeitDto) throws EJBExceptionLP, RemoteException;

	public void updateTaetigkeit(TaetigkeitDto taetigkeitDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public TaetigkeitDto taetigkeitFindByCNr(String cNr, TheClientDto theClientDto);

	public TaetigkeitDto taetigkeitFindByCNrSmallOhneExc(String cNr);

	public TaetigkeitDto taetigkeitFindByPrimaryKey(Integer iId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void createTaetigkeitart(TaetigkeitartDto taetigkeitartDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeTaetigkeitart(TaetigkeitartDto taetigkeitartDto) throws EJBExceptionLP, RemoteException;

	public void updateTaetigkeitart(TaetigkeitartDto taetigkeitartDto) throws EJBExceptionLP, RemoteException;

	public TaetigkeitartDto taetigkeitartFindByPrimaryKey(String cNr) throws EJBExceptionLP, RemoteException;

	public boolean istUrlaubstagZuDatumNoetig(Integer personalIId, Timestamp d_datum, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Integer istFeiertagOderSamstagSonntag(java.sql.Timestamp d_datum, Integer partner_i_id,
			TheClientDto theClientDto) throws EJBExceptionLP;

	public ZeitmodelltagDto getZeitmodelltagZuDatum(Integer personalIId, Timestamp d_datum,
			Integer tagesartIId_Feiertag, Integer tagesartIId_Halbtag, boolean bOriginaltagWennHalberFeiertag,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

//	public BigDecimal berechneDiaeten(Integer diaetenIId, Timestamp tVon,
//			Timestamp tBis, TheClientDto theClientDto) throws RemoteException;

	public UrlaubsabrechnungDto berechneUrlaubsAnspruch(Integer personalIId, Date dAbrechnungzeitpunkt,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printSondertaetigkeiten(TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printAnwesenheitsliste(TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printAnwesenheitsliste(TheClientDto theClientDto, Integer kostenstelleIId)
			throws RemoteException;

	public JasperPrintLP printLohndatenexport(Integer personalIId, Integer iJahr, Integer iMonat, boolean bisMonatsende,
			java.sql.Date d_datum_bis, TheClientDto theClientDto, Integer iOption, Integer kostenstelleIIdAbteilung,
			Integer iOptionSortierung, Boolean bPlusVersteckte, boolean bNachLohnartVerdichten);

	public DauerSondertaetigkeitenDto getGesamtDauerEinerSondertaetigkeitImZeitraum(Integer personalIId, Timestamp tVon,
			Timestamp tBis, Integer taetigkeitIId, TheClientDto theClientDto, Integer tagesartIId_Feiertag,
			Integer tagesartIId_Halbtag) throws RemoteException;

	public JasperPrintLP printSondertaetigkeitsliste(Integer personalIId, Integer taetigkeitIId, Timestamp tVon,
			Timestamp tBis, Integer iOption, Integer kostenstelleIIdAbteilung, Boolean bPlusVersteckte,
			boolean bNurAnwesende, int iOptionSortierung,boolean bMitDetails,  TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printMonatsAbrechnung(Integer personalIId, Integer iJahr, Integer iMonat,
			boolean bisMonatsende, Date d_datum_bis, TheClientDto theClientDto, Integer iOption,
			Integer kostenstelleIIdAbteilung, Integer iOptionSortierung, Double dNurWennTagesistGroesser,
			Boolean bPlusVersteckte);

	public JasperPrintLP printZeitsaldo(Integer personalIId, Integer iJahrVon, Integer iMonatVon, Integer iJahrBis,
			Integer iMonatBis, boolean bisMonatsende, java.sql.Date d_datum_bis, TheClientDto theClientDto,
			Integer iOption, Integer kostenstelleIIdAbteilung, Integer iOptionSortierung, Boolean bPlusVersteckte);

	public void konvertiereAngebotszeitenNachAuftragzeiten(Integer angebotIId, Integer auftragIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public double getBlockzeitenEinesTages(PersonalDto personalDto, ZeitdatenDto[] einblock,
			Integer taetigkeitIId_Kommt, Integer taetigkeitIId_Geht) throws RemoteException;

	public MonatsabrechnungDto erstelleMonatsAbrechnung(Integer personalIId, Integer iJahr, Integer iMonat,
			boolean bisMonatsende, Date d_datum_bis, TheClientDto theClientDto, boolean bSaldozurueckschreiben,
			Integer iOptionSortierung, Double dNurWennTagesistGroesser) throws EJBExceptionLP, RemoteException;

	public Integer createSonderzeitenVonBis(SonderzeitenDto sonderzeitenDto, Timestamp tVon, Timestamp tBis,
			java.sql.Timestamp[] auslassen, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Integer createSonderzeitenVonBis(SonderzeitenDto sonderzeitenDto, java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, java.sql.Timestamp[] auslassen, boolean bSonderzeitImmerBuchen,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public java.sql.Timestamp[] sindIstZeitenVorhandenWennUrlaubGebuchtWird(SonderzeitenDto sonderzeitenDto,
			java.sql.Timestamp tVon, java.sql.Timestamp tBis, TheClientDto theClientDto) throws RemoteException;

	public Integer createSonderzeiten(SonderzeitenDto sonderzeitenDto, TheClientDto theClientDto);

	public void removeSonderzeiten(SonderzeitenDto sonderzeitenDto) throws EJBExceptionLP, RemoteException;

	public void updateSonderzeiten(SonderzeitenDto sonderzeitenDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public SonderzeitenDto sonderzeitenFindByPrimaryKey(Integer iId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public SonderzeitenDto[] sonderzeitenFindByPersonalIIdDDatum(Integer personalIId, Timestamp dDatum)
			throws EJBExceptionLP, RemoteException;

	public Integer createZeitmodelltagpause(ZeitmodelltagpauseDto zeitmodelltagpauseDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeZeitmodelltagpause(ZeitmodelltagpauseDto zeitmodelltagpauseDto)
			throws EJBExceptionLP, RemoteException;

	public void updateZeitmodelltagpause(ZeitmodelltagpauseDto zeitmodelltagpauseDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ZeitmodelltagpauseDto zeitmodelltagpauseFindByPrimaryKey(Integer iId) throws EJBExceptionLP, RemoteException;

	public Integer createMaschine(MaschineDto maschineDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeMaschine(MaschineDto maschineDto) throws EJBExceptionLP, RemoteException;

	public void updateMaschine(MaschineDto maschineDto) throws EJBExceptionLP, RemoteException;

	public MaschineDto maschineFindByPrimaryKey(Integer iId);

	public MaschineDto maschineFindByMandantCNrCInventarnummer(String cInventarnummer, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void importiereSonderzeiten(java.sql.Date tLoescheVorhandenevon, java.sql.Date tLoescheVorhandenebis,
			HashMap<Integer, ArrayList<SonderzeitenImportDto>> daten, TheClientDto theClientDto);

	public MaschineDto[] maschineFindByMandantCNr(String mandantCNr) throws EJBExceptionLP, RemoteException;

	public MaschineDto maschineFindByCIdentifikationsnr(String cIdentifikationsnr)
			throws EJBExceptionLP, RemoteException;

	public Integer createZeitstift(ZeitstiftDto zeitstiftDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public String istBelegGeradeInBearbeitung(String belegartCNr, Integer belegartIId, TheClientDto theClientDto);

	public void removeZeitstift(ZeitstiftDto zeitstiftDto) throws EJBExceptionLP, RemoteException;

	public void updateZeitstift(ZeitstiftDto zeitstiftDto) throws EJBExceptionLP, RemoteException;

	public ZeitstiftDto zeitstiftFindByPrimaryKey(Integer iId) throws EJBExceptionLP, RemoteException;

	public ZeitverteilungDto[] zeitverteilungFindByPersonalIIdUndTag(Integer personalIId, java.sql.Timestamp tTag)
			throws RemoteException;

	public Integer createZeitverteilung(ZeitverteilungDto zeitverteilungDto, TheClientDto theClientDto)
			throws RemoteException;

	public ZeitstiftDto[] zeitstiftFindByPersonalIId(Integer personalIId) throws EJBExceptionLP, RemoteException;

	public ZeitstiftDto[] zeitstiftFindByMandantCNr(TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Integer createMaschinenkosten(MaschinenkostenDto maschinenkostenDto) throws EJBExceptionLP, RemoteException;

	public void removeMaschinenkosten(MaschinenkostenDto maschinenkostenDto) throws EJBExceptionLP, RemoteException;

	public void updateMaschinenkosten(MaschinenkostenDto maschinenkostenDto) throws EJBExceptionLP, RemoteException;

	public MaschinenkostenDto maschinenkostenFindByPrimaryKey(Integer iId) throws EJBExceptionLP, RemoteException;

	public void automatikbuchungenLoeschen(java.sql.Date tVon, Integer personalIId, TheClientDto theClientDto,
			java.util.Date dBis);

	public void pruefeUndErstelleAutomatischesEndeBeiGeht(java.sql.Timestamp tZeitpunktGeht, Integer personalIId,
			TheClientDto theClientDto);

	public MaschinenStundensatzDto getMaschinenKostenZumZeitpunkt(Integer maschineIId, Timestamp tDatum);

	public MaschinenStundensatzDto getMaschinenKostenZumZeitpunkt(Integer maschineIId, java.sql.Timestamp tDatum,
			String belegart, Integer belegartpositionIId);
	

	public Timestamp pruefeObAmLetztenBuchungstagKommtUndGehtGebuchtWurde(Integer personalIId,
			TheClientDto theClientDto) throws RemoteException;

	public MaschinenkostenDto maschinenkostenFindByMaschineIIdTGueltigab(Integer maschineIId, Timestamp tGueltigab)
			throws EJBExceptionLP, RemoteException;

	public Integer createMaschinengruppe(MaschinengruppeDto maschinengruppeDto, TheClientDto theClientDto)
			throws RemoteException;

	public void removeMaschinengruppe(MaschinengruppeDto maschinengruppeDto) throws EJBExceptionLP, RemoteException;

	public void updateMaschinengruppe(MaschinengruppeDto maschinengruppeDto, TheClientDto theClientDto)
			throws RemoteException;

	public MaschinengruppeDto maschinengruppeFindByPrimaryKey(Integer iId) throws EJBExceptionLP, RemoteException;

	public Integer createReise(ReiseDto reiseDto, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeReise(ReiseDto reiseDto, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateReise(ReiseDto reiseDto, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateReiselog(ReiselogDto reiselogDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ReiseDto reiseFindByPrimaryKey(Integer iId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ReiseDto[] reiseFindByPartnerIId(Integer iPartnerId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ReiseDto[] reiseFindByPartnerIIdOhneExc(Integer iPartnerId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ReiselogDto[] reiselogFindByPartnerIId(Integer iPartnerId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ReiselogDto[] reiselogFindByPartnerIIdOhneExc(Integer iPartnerId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Integer createTelefonzeiten(TelefonzeitenDto telefonzeitenDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeTelefonzeiten(TelefonzeitenDto telefonzeitenDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateTelefonzeiten(TelefonzeitenDto telefonzeitenDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public TelefonzeitenDto telefonzeitenFindByPrimaryKey(Integer iId) throws EJBExceptionLP, RemoteException;

	public TelefonzeitenDto[] telefonzeitenFindByPartnerIId(Integer iPartnerIId) throws EJBExceptionLP, RemoteException;

	public TelefonzeitenDto[] telefonzeitenFindByPartnerIIdOhneExc(Integer iPartnerIId)
			throws EJBExceptionLP, RemoteException;

	public Integer createDiaeten(DiaetenDto diaetenDto) throws EJBExceptionLP, RemoteException;

	public void removeDiaeten(DiaetenDto diaetenDto) throws EJBExceptionLP, RemoteException;

	public void updateDiaeten(DiaetenDto diaetenDto) throws EJBExceptionLP, RemoteException;

	public DiaetenDto[] diaetenFindByLandIId(Integer landIId) throws EJBExceptionLP, RemoteException;

	public DiaetenDto diaetenFindByPrimaryKey(Integer iId) throws EJBExceptionLP, RemoteException;

	public Integer createDiaetentagessatz(DiaetentagessatzDto diaetentagessatzDto)
			throws EJBExceptionLP, RemoteException;

	public void removeDiaetentagessatz(DiaetentagessatzDto diaetentagessatzDto) throws EJBExceptionLP, RemoteException;

	public void updateDiaetentagessatz(DiaetentagessatzDto diaetentagessatzDto) throws EJBExceptionLP, RemoteException;

	public DiaetentagessatzDto diaetentagessatzFindByPrimaryKey(Integer iId) throws EJBExceptionLP, RemoteException;

	public DiaetentagessatzDto[] diaetentagessatzFindGueltigenTagessatzZuDatum(Integer diaetenIId, Timestamp tVon)
			throws RemoteException;

	public void updateReise(ReiseDto reiseDto) throws EJBExceptionLP, RemoteException;

	public void updateReises(ReiseDto[] reiseDtos) throws EJBExceptionLP, RemoteException;

	public ReiseDto reiseFindByPrimaryKey(Integer iId) throws EJBExceptionLP, RemoteException;

	ReiseDto reiseFindByPersonalIIdTZeit(Integer personalIId, Timestamp tZeit);

	ReiseDto reiseFindByPersonalIIdTZeitOhneExc(Integer personalIId, Timestamp tZeit);

	public ReiseDto[] reiseFindByPartnerIId(Integer partnerIIId) throws EJBExceptionLP, RemoteException;

	public ReiseDto[] reiseFindByAnsprechpartnerIId(Integer IansprechpartnerIId) throws EJBExceptionLP, RemoteException;

	public void createReiselog(ReiselogDto reiselogDto) throws EJBExceptionLP, RemoteException;

	public void removeReiselog(Integer iId) throws EJBExceptionLP, RemoteException;

	public void removeReiselog(ReiselogDto reiselogDto) throws EJBExceptionLP, RemoteException;

	public void updateReiselog(ReiselogDto reiselogDto) throws EJBExceptionLP, RemoteException;

	public void updateReiselogs(ReiselogDto[] reiselogDtos) throws EJBExceptionLP, RemoteException;

	public ReiselogDto reiselogFindByPrimaryKey(Integer iId) throws EJBExceptionLP, RemoteException;

	public ReiselogDto[] reiselogFindByPartnerIId(Integer partnerIId) throws EJBExceptionLP, RemoteException;

	public ReiselogDto[] reiselogFindByAnsprechpartnerIId(Integer iAnsprechpartnerIId)
			throws EJBExceptionLP, RemoteException;

	public void updateTelefonzeiten(TelefonzeitenDto telefonzeitenDto) throws RemoteException;

	public void updateTelefonzeitens(TelefonzeitenDto[] telefonzeitenDtos) throws RemoteException;

	public TelefonzeitenDto telefonzeitenFindByPersonalIIdTVon(Integer personalIId, Timestamp tVon)
			throws RemoteException;

	public TelefonzeitenDto[] telefonzeitenFindByAnsprechpartnerIId(Integer iAnsprechpartnerIId) throws RemoteException;

	public boolean zeitAufLoseVerteilen(Integer personalIId, Timestamp tZeitBis, TheClientDto theClientDto)
			throws RemoteException;

	public void removeZeitverteilungByPersonalIIdUndTag(Integer personalIId, java.sql.Timestamp tTag)
			throws RemoteException;

	public String erstelleMonatsAbrechnungFuerBDE(Integer personalIId, Integer iJahr, Integer iMonat,
			boolean bisMonatsende, java.sql.Date d_datum_bis, TheClientDto theClientDto, boolean bSaldozurueckschreiben,
			boolean returnVariableAlsCSV) throws RemoteException;

	public boolean sindBelegzeitenVorhanden(String cBelegartnr, Integer belegartIId);

	public AuftragzeitenDto[] getAllZeitenEinesBeleges(String belegartCNr, Integer belegartIId,
			Integer belegartpositionIId, Integer personalIId, java.sql.Timestamp tZeitenVon,
			java.sql.Timestamp tZeitenBis, int iSortierung, boolean bBeruecksichtigeLeistungsfaktor,
			TheClientDto theClientDto);

	public AuftragzeitenDto[] getAllZeitenEinesBeleges(String belegartCNr, Integer belegartIId,
			Integer belegartpositionIId, Integer personalIId, java.sql.Timestamp tZeitenVon,
			java.sql.Timestamp tZeitenBis, int iSortierung, boolean bBeruecksichtigeLeistungsfaktor,
			Integer zeitdatenIId, Integer maschineIId, int iArtBelegzeiten, TheClientDto theClientDto);

	public String erstelleAutomatischeMindestpause(java.sql.Timestamp tGeht, Integer personalIId,
			TheClientDto theClientDto);

	public void pruefeObSollzeitenUeberschritten(Integer losIId, Integer arikelIId, Integer personalIId,
			boolean bKommtVonTerminal, TheClientDto theClientDto);

	public void removeMaschinenzeitdaten(MaschinenzeitdatenDto maschinenzeitdatenDto, TheClientDto theClientDto);

	public Integer createMaschinenzeitdaten(MaschinenzeitdatenDto maschinenzeitdatenDto, TheClientDto theClientDto);

	public MaschinenzeitdatenDto maschinenzeitdatenFindByPrimaryKey(Integer iId);

	public void updateMaschinenzeitdaten(MaschinenzeitdatenDto maschinenzeitdatenDto, TheClientDto theClientDto);

	public boolean sindZuvieleZeitdatenEinesBelegesVorhanden(String belegartCNr, Integer belegartIId,
			TheClientDto theClientDto);

	public BigDecimal getSollzeitEinerPersonUndEinesTages(PersonalDto personalDto, Integer tagesartIId_Feiertag,
			Integer tagesartIId_Halbtag, Timestamp tDatum, TheClientDto theClientDto);

	public Map<String, String> getBebuchbareBelegarten(TheClientDto theClientDto);

	public void maschineStop(Integer maschineIId, Integer lossollarbeitsplanIId, java.sql.Timestamp tStop,
			TheClientDto theClientDto);

	public String getParameterSortierungZeitauswertungen(Integer iOptionSortierung, TheClientDto theClientDto);

	public HashMap<?, ?> taetigkeitenMitImportkennzeichen();

	public ZeitdatenDto[] assembleZeitdatenDtosOhneBelegzeiten(Collection<?> zeitdatens);

	public void loszeitenVerschieben(Integer losIId_Quelle, Integer losIId_Ziel, TheClientDto theClientDto);

	public Timestamp getLetztesGehtEinesTages(Integer personalIId, java.sql.Timestamp tDatum,
			TheClientDto theClientDto);

	public Timestamp getErstesKommtEinesTages(Integer personalIId, java.sql.Timestamp tDatum,
			TheClientDto theClientDto);

	public boolean sindReisezeitenZueinemTagVorhanden(Integer personalIId, java.sql.Timestamp tDatum,
			TheClientDto theClientDto);

	public void removeBereitschaft(BereitschaftDto dto);

	public void updateBereitschaft(BereitschaftDto dto);

	public BereitschaftDto bereitschaftFindByPrimaryKey(Integer iId);

	public Integer createBereitschaft(BereitschaftDto dto);

	public void projektzeitenVerschieben(Integer projektIId_Quelle, Integer projektIId_Ziel, TheClientDto theClientDto);

	public ArrayList<ReiseKomplettDto> holeReisenKomplett(String belegartCNr, Integer belegartIId,
			java.sql.Timestamp tVon, java.sql.Timestamp tBis, TheClientDto theClientDto);

	public BigDecimal getKmKostenEinerReise(ReiseKomplettDto rkDto, TheClientDto theClientDto);

	public ArrayList<ReiseKomplettDto> holeReisenKomplett(Integer fahrzeugIId, java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, TheClientDto theClientDto);

	public ArrayList<ReiseKomplettDto> holeReisenKomplettFuerAbrechnungsvorschlag(java.sql.Timestamp tBis,
			TheClientDto theClientDto);

	public BigDecimal berechneKalkJahresIstStunden(Integer personalIId, Integer iMonat, Integer iJahr,
			TheClientDto theClientDto);

	public ZeitdatenDto getZugehoerigeEndeBuchung(ZeitdatenDto zeitdatenDto, TheClientDto theClientDto);

	public void wandleUrlaubsantragInUrlaubUm(Integer[] sonderzeitenIIds, boolean bGehehmigt,
			boolean bBestehendeLoeschen, TheClientDto theClientDto);

	public void pflegeUmstellungAufVonBisErfassung(TheClientDto theClientDto);

	public VonBisErfassungTagesdatenDto berechneTagesArbeitszeitVonBisZeiterfassungOhneKommtGeht(Integer personalIId,
			java.sql.Date d_datum, TheClientDto theClientDto);

	public Timestamp getLetzteGebuchteZeit(Integer personalIId, java.sql.Timestamp tDatum, TheClientDto theClientDto);

	public Map getAllMaschinen(TheClientDto theClientDto);

	public ArrayList<AuftragzeitenDto> getAllTelefonzeitenEinesProjekts(Integer projektIId, Integer personalIId,
			java.sql.Timestamp tVon, java.sql.Timestamp tZeitenBis, TheClientDto theClientDto);

	public ArtikelDto getDefaultArbeitszeitartikel(TheClientDto theClientDto);

	public BigDecimal getPersonalKostenProStunde(TheClientDto theClientDto, HashMap hmGestpreise, int iOption,
			Integer artikelIId, Integer personalIId, Timestamp tZeitpunkt);

	public WochenabschlussReportDto printWochenabschluss(Integer personalIId, java.sql.Timestamp tKW,
			TheClientDto theClientDto);

	public void zeitenAbschliessen(Integer personalIId, java.sql.Timestamp tKW, TheClientDto theClientDto);

	public java.sql.Timestamp gibtEsBereitseinenZeitabschlussBisZurKW(Integer personalIId, java.sql.Timestamp tKW,
			TheClientDto theClientDto);

	public void bringeFehlerWennZeitabschlussvorhanden(Integer personalIId,
			java.sql.Timestamp tZeitWelcheGeaendertWerdenSoll, TheClientDto theClientDto);

	public PersonalDto[] entferneNichtAnwesendePersonen(java.sql.Timestamp tVon, java.sql.Timestamp tBis,
			PersonalDto[] personalDtos, TheClientDto theClientDto);

	public PersonalDto[] entferneNichtAnwesendePersonen(Integer iJahrVon, Integer iMonatVon, Integer iJahrBis,
			Integer iMonatBis, PersonalDto[] personalDtos, TheClientDto theClientDto);

	public DiaetentagessatzDto[] getDiaetenTagesSatzDtos(Integer diaetenIId, java.sql.Timestamp tVon);

	public BigDecimal berechneDiaetenAusScript(Integer diaetenIId, Timestamp tVon, Timestamp tBis,
			TheClientDto theClientDto, String personalart) throws RemoteException;

	public TreeMap<Timestamp, BigDecimal> getVerfuegbareSollzeitAusPersonalgehalt(java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, TheClientDto theClientDto);

	public MaschinenkostenDto getMaschinenKostenDtoZumZeitpunkt(Integer maschineIId, java.sql.Timestamp tDatum);

	public ZeitmodelltagDto getZeitmodelltagFirmenzeitmodellZuDatum(Integer zeitmodellIId, java.sql.Timestamp d_datum,
			TheClientDto theClientDto);

	public void schichtzeitmodelleAufrollen(java.sql.Date tVon, java.sql.Date tBis, Integer personalIId,
			TheClientDto theClientDto);

	public void schichtzeitmodellEintragen(ZeitdatenDto zeitdatenDto, TheClientDto theClientDto);

	public void updateZeitgutschrift(ZeitgutschriftDto dto, TheClientDto theClientDto);

	public ZeitgutschriftDto zeitgutschriftFindByPrimaryKey(Integer iId, TheClientDto theClientDto);

	public void removeZeitgutschrift(ZeitgutschriftDto dto);

	public Integer createZeitgutschrift(ZeitgutschriftDto dto, TheClientDto theClientDto);

	public BigDecimal zeitgutschriftEinesDatums(Integer personalIId, Timestamp tDatum, boolean bKommt,
			TheClientDto theClientDto);

	ZeitsaldoDto erstelleMonatsabrechnungZeitsaldo(Integer personalIId, Integer iJahr, Integer iMonat,
			boolean bisMonatsende, java.sql.Date d_datum_bis, TheClientDto theClientDto, boolean bSaldozurueckschreiben,
			Integer iOptionSortierung) throws EJBExceptionLP;

	List<ZeitdatenDto> zeitdatenFindByBelegartnrBelegidOrderedByTZeitAsc(String belegartNr, Integer belegId);

	Integer createZeitdatenpruefen(ZeitdatenpruefenDto dto, TheClientDto theClientDto);

	Integer createZeitdatenpruefen(ZeitdatenDto dto, Integer fehlerCode, String fehlerText, TheClientDto theClientDto);

	ZeitdatenpruefenDto zeitdatenpruefenFindByPrimaryKey(Integer iId, TheClientDto theClientDto) throws EJBExceptionLP;

	void removeZeitdatenpruefen(ZeitdatenpruefenDto zeitdatenpruefenDto, TheClientDto theClientDto)
			throws EJBExceptionLP;

	Integer zeitdatenpruefenInZeitdatenUeberleiten(Integer zeitdatenpruefenId, TheClientDto theClientDto);

	ZeitdatenpruefenDto updateZeitdatenpruefen(ZeitdatenpruefenDto zdpDto, TheClientDto theClientDto);

	boolean hasZeitdatenpruefen(Integer personalId);

	public java.sql.Timestamp letzterZeitabschlussVorKW(Integer personalIId, java.sql.Timestamp tKW,
			TheClientDto theClientDto);

	public BigDecimal getDauerEinerTaetigkeitEinesTages(String personalnummer, String artikelnr, java.util.Date dDatum,
			TheClientDto theClientDto);

	public void vertauscheMaschinengruppe(Integer iId1I, Integer iId2I);

	public Integer kopiereZeitmodelltag(Integer zeitmodelltagIId, TheClientDto theClientDto);

	public void updateUebertragBVADto(UebertragBVADto dto, TheClientDto theClientDto);

	public UebertragBVADto uebertragBVADtoFindByPrimaryKey(Integer iId);

	public void removeUebertragBVA(UebertragBVADto dto);

	public Integer schreibeUebertragBVADto(UebertragBVADto uebertragBVADto, TheClientDto theClientDto);

	public Integer createUebertragBVA(UebertragBVADto uebertragBVADto, TheClientDto theClientDto);

	public UebertragBVADto getUebertragBVAVormonat(Integer personalIId, Integer iJahr, Integer iMonat);

	public Integer createAuszahlungBVA(AuszahlungBVADto dto, TheClientDto theClientDto);

	public void removeAuszahlungBVA(AuszahlungBVADto dto);

	public AuszahlungBVADto auszahlungBVADtoFindByPrimaryKey(Integer iId);

	public void updateAuszahlungBVADto(AuszahlungBVADto dto, TheClientDto theClientDto);

	public AuszahlungBVADto[] auszahlungBVAFindByPersonalIIdIJahrIMonat(Integer personalIId, Integer iJahr,
			Integer iMonat);

	public ArrayList<Integer> getMaschinenzeitenEinesTagesEinesAGsUndEinerPerson(Integer personalIId,
			Integer lossollarbeitsplanIId, java.sql.Date tDatum, TheClientDto theClientDto);

	public Double getSummeMaschinenZeitenEinesBeleges(Integer losIId, Integer lossollarbeitsplanIId,
			java.sql.Timestamp tZeitenVon, java.sql.Timestamp tZeitenBis, TheClientDto theClientDto);

	public ArrayList<LossollarbeitsplanDto> getZugehoerigeSollarbeitplanDtos(Integer zeitdatenIId);

	MaschineDto maschineFindByCIdentifikationsnrOhneExc(String cIdentifkationsnr);

	MaschineDto maschineFindByPrimaryKeyOhneExc(Integer maschineIId);

	public UrlaubsabrechnungDto berechneUrlaubsanspruchAliquotMitKarenzenZumAbrechnungszeitpunkt(Integer personalIId,
			java.sql.Date dAbrechnungzeitpunkt, TheClientDto theClientDto);

	void createSonderzeitenEmail(SonderzeitenAntragEmailDto emailDto, TheClientDto theClientDto) throws RemoteException;

	public void sindEintraegeBereitsVerrechnet(Integer zeitdatenIId, Integer maschinenzeitdatenIId,
			Integer telefonzeitenIId, Integer auftragszuordnungIId, Integer reiseIId);

	public Integer getEntfernungInKmEinerReise(Integer reiseIId);

	public ReiseDto getReiseDtoVorgaenger(Timestamp tZeitpunkt, Integer personalIId);

	public java.sql.Timestamp sindZeitenBereitsUnterschrieben(Integer personalIId, java.sql.Timestamp tZeitBis,
			TheClientDto theClientDto);

	public ZeitdatenDto pruefeObMaschineBelegt(Integer personalIId, Integer maschineIId, java.sql.Timestamp tZeitpunkt,
			TheClientDto theClientDto);

	public ZeitmodelltagDto getZeitmodelltagDesFirmenzeitmodellsZuDatum(java.sql.Timestamp d_datum,
			TheClientDto theClientDto);

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void versendeMonatsAbrechnung(ArrayList<MonatsabrechnungEmailVersand> infos, String absender, Integer iJahr,
			Integer iMonat, boolean bisMonatsende, java.sql.Date d_datum_bis, TheClientDto theClientDto);

	public Integer createReisespesen(ReisespesenDto reisespesenDto, TheClientDto theClientDto);

	public void updateReisespesen(ReisespesenDto reisespesenDto, TheClientDto theClientDto);

	public void removeReisespesen(ReisespesenDto reisespesenDto, TheClientDto theClientDto);

	public ReisespesenDto reisespesenFindByPrimaryKey(Integer iId);

	public BigDecimal getZusaetzlicheReisespesenInMandantenwaehrung(Integer reiseIId);

	public void negativstundenInUrlaubUmwandeln(Integer iJahr, Integer iMonat, Integer personalIId,
			TheClientDto theClientDto);

	public void automatischeSonderzeitenEntfernen(Integer personalIId, Timestamp tVon, Timestamp tBis);

	public ZeitstiftDto[] zeitstiftFindByPersonalIIdCTyp(Integer personalIId, String cTyp);

	public MaschineDto maschinefindByCIdentifikationsnrMandantCNrOhneExc(String cIdentifkationsnr, String mandantCNr);

	public void erstelleAutomatischePauseBeiUnproduktiverZeit(java.sql.Timestamp tGeht, Integer personalIId,
			TheClientDto theClientDto);

	public void toggleTelefonzeitenWiedervorlageErledigt(Integer telefonzeitenIId, TheClientDto theClientDto);

	public ProjektzeitenDto projektzeitenFindByPrimaryKey(Integer iId);

	public void testMSSQLTreiber(TheClientDto theClientDto);

	public LPDatenSubreport getSubreportGestarteteMaschinen(Integer personalIId, TheClientDto theClientDto);

	public BigDecimal berechneBelegzeitenEinesTages(Integer personalIId, java.sql.Date d_datum,
			TheClientDto theClientDto);

	public boolean esGibtBereitsPersonalOderMaschinenzeitdaten(Integer maschineIId, TheClientDto theClientDto);

	public boolean isMaschinenZeitbuchungNachZeitpunktVorhanden(Integer maschineIId, java.sql.Timestamp ts);

}
