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
package com.lp.server.fertigung.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.DatumsfilterVonBis;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;

@Remote
public interface FertigungReportFac {
	public final static String REPORT_MODUL = "fertigung";

	public final static String REPORT_THEORETISCHE_FEHLMENGEN = "fert_theoretische_fehlmengen.jasper";
	public final static String REPORT_FERTIGUNGSBEGLEITSCHEIN = "fert_fertigungsbegleitschein.jasper";
	public final static String REPORT_AUSGABELISTE = "fert_ausgabeliste.jasper";
	public final static String REPORT_AUSGABELISTE2 = "fert_ausgabeliste2.jasper";
	public final static String REPORT_ALLE = "fert_alle.jasper";
	public final static String REPORT_OFFENE = "fert_offene.jasper";
	public final static String REPORT_OFFENE_AG = "fert_offene_ag.jasper";
	public final static String REPORT_HALBFERTIGFABRIKATSINVENTUR = "fert_halbfertigfabrikatsinventur.jasper";
	public final static String REPORT_HALBFERTIGFABRIKATSINVENTUR_IDENT = "fert_halbfertigfabrikatsinventur_ident.jasper";
	public final static String REPORT_NACHKALKULATION = "fert_nachkalkulation.jasper";
	public final static String REPORT_ABLIEFERUNGSSTATISTIK = "fert_ablieferungsstatistik.jasper";
	public final static String REPORT_AUFLOESBARE_FEHLMENGEN = "fert_aufloesbare_fehlmengen.jasper";
	public final static String REPORT_FEHLMENGEN_ALLER_LOSE = "fert_fehlmengen_allerlose.jasper";
	public final static String REPORT_STUECKRUECKMELDUNG = "fert_stueckrueckmeldung.jasper";
	public final static String REPORT_LOSSTATISTIK = "fert_losstatistik.jasper";
	public final static String REPORT_FEHLERSTATISTIK = "fert_fehlerstatistik.jasper";
	public final static String REPORT_MONATSAUSWERTUNG = "fert_monatsauswertung.jasper";
	public final static String REPORT_RANKINGLISTE = "fert_rankingliste.jasper";
	public final static String REPORT_LOSETIKETT1 = "fert_losetikett1.jasper";
	public final static String REPORT_VERPACKUNGSETIKETT = "fert_verpackungsetikett.jasper";
	public final static String REPORT_LOSETIKETTA4 = "fert_losetikett_a4.jasper";
	public final static String REPORT_ZEITENTWICKLUNG = "fert_zeitentwicklung.jasper";
	public final static String REPORT_AUSLASTUNGSVORSCHAU = "fert_auslastungsvorschau.jasper";
	public final static String REPORT_AUSLASTUNGSVORSCHAU_DETAILIERT = "fert_auslastungsvorschau_detailiert.jasper";
	public final static String REPORT_AUSLIEFERLISTE = "fert_auslieferliste.jasper";
	public final static String REPORT_FEHLTEILE = "fert_fehlteile.jasper";
	public final static String REPORT_ABLIEFERETIKETT = "fert_ablieferetikett.jasper";
	public final static String REPORT_SONDERETIKETT = "fert_sonderetikett.jasper";
	public final static String REPORT_VERSANDETIKETT_ABLIEFERUNG = "fert_versandetikett_ablieferung.jasper";
	public final static String REPORT_VERSANDETIKETT_VORBEREITUNG = "fert_versandetikett_vorbereitung.jasper";
	public final static String REPORT_LOSZEITEN = "fert_loszeiten.jasper";
	public final static String REPORT_MATERIALLISTE = "fert_materialliste.jasper";
	public final static String REPORT_VERGLEICH_MIT_STUECKLISTE = "fert_vergleichmitstueckliste.jasper";
	public final static String REPORT_GESAMTALKULATION = "fert_gesamtkalkulation.jasper";
	public final static String REPORT_MASCHINEUNDMATERIAL = "fert_maschineundmaterial.jasper";
	public final static String REPORT_BEDARFSZUSAMMENSCHAU = "fert_bedarfszusammenschau.jasper";
	public final static String REPORT_TAETIGKEITAGBEGINN = "fert_taetigkeitagbeginn.jasper";
	public final static String REPORT_PRUEFPLAN = "fert_pruefplan.jasper";
	public final static String REPORT_PRUEFERGEBNIS = "fert_pruefergebnis.jasper";
	public final static String REPORT_NICHT_ERFASSTE_PRUEFERGEBNISSE = "fert_nicht_erfasste_pruefergebnisse.jasper";
	public final static String REPORT_TRACEIMPORT = "fert_traceimport.jasper";
	public final static String REPORT_LADELISTE = "fert_ladeliste.jasper";
	

	public final static String REPORT_PRODUKTIONSINFORMATION = "fert_produktionsinformation.jasper";
	public final static String REPORT_ARBEITSZEITSTATUS = "fert_arbeitszeitstatus.jasper";

	public final static String REPORT_BEDARFSUEBERNAHME_SYNCHRONISIERUNG = "fert_bedarfsuebernahme_synchronisierung.jasper";
	public final static String REPORT_BEDARFSUEBERNAHME_BUCHUNGSLISTE = "fert_bedarfsuebernahme_buchungsliste.jasper";
	public final static String REPORT_BEDARFSUEBERNAHME_OFFENE = "fert_bedarfsuebernahme_offene.jasper";

	public final static int SORTIERUNG_AUSGABELISTE_MONTAGEARTPLUSCHALE = 0;
	public final static int SORTIERUNG_AUSGABELISTE_ARTIKELKLASSE = 1;
	public final static int SORTIERUNG_AUSGABELISTE_LAGERORT = 2;
	public final static int SORTIERUNG_AUSGABELISTE_IDENT = 3;

	public final static int SORTIERUNG_FEHLERSTATISTIK_LOSNUMMER = 0;
	public final static int SORTIERUNG_FEHLERSTATISTIK_ARTIKELNUMMER = 1;
	public final static int SORTIERUNG_FEHLERSTATISTIK_FEHLER = 2;

	public final static int LOSZEITEN_OPTION_SORTIERUNG_PERSON = 0;
	public final static int LOSZEITEN_OPTION_SORTIERUNG_ARTIKEL = 1;
	public final static int LOSZEITEN_OPTION_SORTIERUNG_AG_UAG = 2;

	public final static int OFFENE_OPTION_SORTIERUNG_BEGINN = 0;
	public final static int OFFENE_OPTION_SORTIERUNG_ENDE = 1;
	public final static int OFFENE_OPTION_SORTIERUNG_KUNDE = 2;
	public final static int OFFENE_OPTION_SORTIERUNG_LOSNUMMER = 3;
	public final static int OFFENE_OPTION_SORTIERUNG_KOSTENSTELLE = 4;
	public final static int OFFENE_OPTION_SORTIERUNG_ARTIKEL = 5;
	public final static int OFFENE_OPTION_SORTIERUNG_FERTIGUNGSGRUPPE = 6;
	public final static int OFFENE_OPTION_SORTIERUNG_KUNDEUNDGEWAEHLTERTERMIN = 7;
	public final static int OFFENE_OPTION_SORTIERUNG_LIEFERTERMIN = 8;

	public final static int HF_OPTION_SORTIERUNG_LOSNR = 0;
	public final static int HF_OPTION_SORTIERUNG_ARTIKELNR = 1;
	public final static int HF_OPTION_SORTIERUNG_AUFTRAGNR = 2;

	public final static int FEHLMENGEN_ALLER_LOSE_OPTION_SORTIERUNG_BELEGNUMMER = 0;
	public final static int FEHLMENGEN_ALLER_LOSE_OPTION_SORTIERUNG_ARTIKELNUMMER = 1;
	public final static int FEHLMENGEN_ALLER_LOSE_OPTION_SORTIERUNG_BEGINNTERMIN = 2;

	public final static int FEHLMENGEN_ALLER_LOSE_OPTION_STUECKLISTEN_ALLE = 0;
	public final static int FEHLMENGEN_ALLER_LOSE_OPTION_STUECKLISTEN_NUR_EIGENGEFERTIGTE = 1;
	public final static int FEHLMENGEN_ALLER_LOSE_OPTION_STUECKLISTEN_NUR_FREMDGEFERTIGTE = 2;

	public final static Integer OFFENE_OPTION_STICHTAG_LIEFERTERMIN = 0;
	public final static Integer OFFENE_OPTION_STICHTAG_BEGINNDATUM = 1;
	public final static Integer OFFENE_OPTION_STICHTAG_ENDEDATUM = 2;

	public final static int ABLIEFERSTATISTIK_OPTION_SORTIERUNG_ABLIEFERDATUM = 0;
	public final static int ABLIEFERSTATISTIK_OPTION_SORTIERUNG_ARTIKEL = 1;
	public final static int ABLIEFERSTATISTIK_OPTION_SORTIERUNG_AUFTRAG = 2;

	public final static int ABLIEFERSTATISTIK_OPTION_ALLE_ARTIKEL = 0;
	public final static int ABLIEFERSTATISTIK_OPTION_ARTIKEL_NUR_SELEKTIERTER_ARTIKEL = 1;
	public final static int ABLIEFERSTATISTIK_OPTION_ARTIKEL_NUR_KOPFSTUECKLISTEN = 2;
	public final static int ABLIEFERSTATISTIK_OPTION_ARTIKEL_NUR_KOPFLOSE = 3;

	public final static int GESAMTKALKULATION_ARTIKELNUMMER = 0;
	public final static int GESAMTKALKULATION_ARTIKELBEZEICHNUNG = 1;
	public final static int GESAMTKALKULATION_ZUSATZBEZEICHNUNG = 2;
	public final static int GESAMTKALKULATION_EINHEIT = 3;
	public final static int GESAMTKALKULATION_LOSGROESSE = 4;
	public final static int GESAMTKALKULATION_EBENE = 5;
	public final static int GESAMTKALKULATION_SOLLMENGE = 6;
	public final static int GESAMTKALKULATION_SOLLPREIS = 7;
	public final static int GESAMTKALKULATION_ISTMENGE = 8;
	public final static int GESAMTKALKULATION_ISTPREIS = 9;
	public final static int GESAMTKALKULATION_ARBEITSZEIT_KOSTEN = 10;
	public final static int GESAMTKALKULATION_VERBRAUCHTE_MENGE = 11;
	public final static int GESAMTKALKULATION_BELEGART_ZUGANG = 12;
	public final static int GESAMTKALKULATION_BELEGNUMMER_ZUGANG = 13;
	public final static int GESAMTKALKULATION_ARBEITSZEIT = 14;
	public final static int GESAMTKALKULATION_MENGENFAKTOR = 15;
	public final static int GESAMTKALKULATION_EINSTANDSPREIS = 16;
	public final static int GESAMTKALKULATION_MASCHINE = 17;
	public final static int GESAMTKALKULATION_SNR_CHNR = 18;
	public final static int GESAMTKALKULATION_SNRBEHAFTET = 19;
	public final static int GESAMTKALKULATION_CHNRBEHAFTET = 20;
	public final static int GESAMTKALKULATION_LIEF1PREIS = 21;
	public final static int GESAMTKALKULATION_BELEGSTATUS_ZUGANG = 22;
	public final static int GESAMTKALKULATION_LOSNUMMER = 23;
	public final static int GESAMTKALKULATION_BELEGARTPOSITION_I_ID_ZUGANG = 24;
	public final static int GESAMTKALKULATION_ANZAHL_SPALTEN = 25;

	public static final int HF_ARTIKELNUMMER = 0;
	public static final int HF_LOSGROESSE = 1;
	public static final int HF_LOSNUMMER = 2;
	public static final int HF_BEZEICHNUNG = 3;
	public static final int HF_ERLEDIGT = 4;
	public static final int HF_POSITION_ARTIKELNUMMMER = 5;
	public static final int HF_POSITION_BEZEICHNUNG = 6;
	public static final int HF_POSITION_AUSGEGEBEN = 7;
	public static final int HF_POSITION_ABGELIFERT = 8;
	public static final int HF_POSITION_OFFEN = 9;
	public static final int HF_POSITION_PREIS = 10;
	public static final int HF_POSITION_EKPREIS = 11;
	public static final int HF_POSITION_ARTIKELVERWENDUNG = 12;
	public static final int HF_POSITION_AUSGEGEBEN_MASCHINE = 13;
	public static final int HF_POSITION_ABGELIFERT_MASCHINE = 14;
	public static final int HF_POSITION_OFFEN_MASCHINE = 15;
	public static final int HF_POSITION_PREIS_MASCHINE = 16;
	public static final int HF_AUFTRAGSNUMMER = 17;
	public static final int HF_FERTIGUNGSGRUPPE = 18;
	public static final int HF_AUFTRAGSSTATUS = 19;
	public static final int HF_AUFTRAGSPOSITIONSSTATUS = 20;
	public static final int HF_AUFTRAGSKUNDE = 21;
	public static final int HF_AUFTRAGSKUNDEKURZBEZEICHNUNG = 22;
	public static final int HF_SORTIERSTRING = 23;
	public static final int HF_POSITION_REFERENZNUMMER = 24;
	public static final int HF_POSITION_MASCHINENBEZEICHNUNG = 25;
	public static final int HF_POSITION_ARBEITSGANG = 26;
	public static final int HF_POSITION_NUR_MASCHINENZEIT = 27;
	public static final int HF_POSITION_MASCHINENINVENTARNUMMER = 28;
	public static final int HF_POSITION_MASCHINENIDENTIFIKATIONSNUMMER = 29;
	public static final int HF_KURZBEZEICHNUNG = 30;
	public static final int HF_REFERENZNUMMER = 31;
	public static final int HF_EINHEIT = 32;
	public static final int HF_POSITION_EINHEIT = 33;
	public static final int HF_BESTELLMENGENEINHEIT = 34;
	public static final int HF_UMRECHNUNGSFAKTOR = 35;
	public static final int HF_POSITION_BESTELLMENGENEINHEIT = 36;
	public static final int HF_POSITION_UMRECHNUNGSFAKTOR = 37;
	public static final int HF_POSITION_MASCHINENSERIENNUMMER = 38;
	public static final int HF_ANZAHL_SPALTEN = 39;

	public JasperPrintLP printTheoretischeFehlmengen(Integer losIId, Integer auftragIId, Integer projektIId, boolean sortierungWieInStklErfasst,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printZeitentwicklung(java.sql.Timestamp tVon, java.sql.Timestamp tBis,
			boolean bSortiertNachArtikelgruppe, TheClientDto theClientDto);

	public JasperPrintLP printAusgabeListe(Integer[] losIId, Integer iSortierung, boolean bVerdichtetNachIdent,
			boolean bVorrangigNachFarbcodeSortiert, Integer artikelklasseIId, String alternativerReport,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printFertigungsbegleitschein(Integer losIId, Boolean bStammtVonSchnellanlage,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printProduktionsinformation(Integer losIId, TheClientDto theClientDto);

	public JasperPrintLP printAlle(ReportJournalKriterienDto krit, boolean bNurAngelegte, Integer fertigungsgruppeIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printOffene(java.sql.Date dStichtag, int iOptionStichtag, String belegNrVon, String belegNrBis,
			Integer kundeIId, Integer kostenstelleIId, Integer fertigungsgruppeIId, int iSortierung,
			boolean bNurForecast, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printLosstatistik(java.sql.Timestamp tVon, java.sql.Timestamp tBis, Integer losIId,
			Integer stuecklisteIId, Integer auftragIId, boolean bArbeitsplanSortiertNachAG, boolean bVerdichtet,
			java.sql.Timestamp tStichtag, TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printHalbfertigfabrikatsinventur(java.sql.Timestamp tsStichtag, int iSortierung,
			boolean bVerdichtet, Integer partnerIIdFertigungsort, boolean bSortiertNachFertigungsgruppe,
			boolean bNurMaterialwerte, boolean referenznummerStattArtikelnummer, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printMonatsauswertung(java.sql.Timestamp tVon, java.sql.Timestamp tBis, boolean bVerdichtet,
			TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printMaterialliste(Integer losIId, ArrayList<Integer> selektiertePositionen, boolean bSortiertNachOrginialArtikelnummer,
			TheClientDto theClientDto);

	public JasperPrintLP printLadeliste(Integer artikelIId_Taetigkeit, Integer artikelgruppeIId, DatumsfilterVonBis vonBis,
			TheClientDto theClientDto);
	
	public JasperPrintLP printRankingliste(TheClientDto theClientDto) throws RemoteException;
	
	public JasperPrintLP printAuslieferliste(java.sql.Timestamp tStichtag, Integer kundeIId,boolean bNurNachLosEndeTerminSortiert, TheClientDto theClientDto);

	public JasperPrintLP printNachkalkulation(Integer losIId, TheClientDto theClientDto);

	public ReportLosnachkalkulationDto[] getDataNachkalkulationZeitdaten(Integer losIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ReportLosnachkalkulationDto getDataNachkalkulationMaterial(Integer losIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printAblieferungsstatistik(Date dVon, Date dBis, Integer artikelIId, int iOptionArtikel,
			String optionArtikel, int iSortierungAblieferungsstatistik, boolean bNurMaterialwerte,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public boolean istErstlos(LosDto losDto, TheClientDto theClientDto);

	public JasperPrintLP printAufloesbareFehlmengen(Integer iSortierung, Boolean bNurArtikelMitLagerstand,
			Boolean bOhneEigengefertigteArtikel, TheClientDto theClientDto);

	public JasperPrintLP printFehlmengenAllerLose(boolean bAlleOhneEigengefertigteArtikel, int iOptionStueckliste,
			boolean bOhneBestellteArtikel, ArrayList<Integer> arLosIId, int iSortierung, boolean bNurDringende,
			Integer fertigungsgruppeIId, TheClientDto theClientDto);

	public JasperPrintLP printFehlteile(Integer losIId, boolean bNurPositionenMitFehlmengen, TheClientDto theClientDto);

	public JasperPrintLP printStueckrueckmeldung(Integer losIId, int iSortierung, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printLoszeiten(Integer iIdAuftragI, int iSortierung, java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, TheClientDto theClientDto);

	public JasperPrintLP printLosEtikett(Integer losIId, BigDecimal bdMenge, String sKommentar, boolean bMitInhalten,
			Integer iExemplare, TheClientDto theCLientDto) throws RemoteException;

	public JasperPrintLP printLosVerpackungsetiketten(Integer losIId, String handKommentar,Integer iAnzahl, TheClientDto theClientDto)
			throws RemoteException;

	public JasperPrintLP printLosEtikettA4(Integer losIId, BigDecimal bdMenge, String sKommentar, boolean bMitInhalten,
			Integer iExemplare, TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printAuslastungsvorschau(java.sql.Timestamp tStichtag, boolean bSortiertNachArtikelgruppe,
			TheClientDto theClientDto);

	public JasperPrintLP printOffeneArbeitsgaenge(java.sql.Date dStichtag, int iOptionStichtag, String belegNrVon,
			String belegNrBis, Integer kundeIId, Integer kostenstelleIId, Integer fertigungsgruppeIId,
			Integer artikelgruppeIId, Integer maschineIId, boolean bSollstundenbetrachtung, TheClientDto theClientDto);

	public JasperPrintLP printAblieferEtikett(Integer losablieferungIId, Integer iExemplare, BigDecimal bdHandmenge, String snrVonScannerRAW,
			TheClientDto theClientDto);

	public JasperPrintLP printFehlerstatistik(java.sql.Timestamp tVon, java.sql.Timestamp tBis, Integer iSortierung,
			boolean bAlleAnzeigen, TheClientDto theClientDto);

	public JasperPrintLP printAuslastungsvorschauDetailliert(java.sql.Timestamp tStichtag, TheClientDto theClientDto);

	public JasperPrintLP printGesamtkalkulation(Integer losIId,int iBisEbene, TheClientDto theClientDto);

	public JasperPrintLP printMaschineUndMaterial(Integer maschineIId, Integer maschinengruppeIId,
			DatumsfilterVonBis vonBis, TheClientDto theClientDto);

	public JasperPrintLP printTaetigkeitAGBeginn(Integer artikelIId_Taetigkeit, DatumsfilterVonBis vonBis,
			TheClientDto theClientDto);

	public GesamtkalkulationDto getDatenGesamtkalkulation(GesamtkalkulationDto gkDto, Integer losIId,
			Integer losablieferungIId, int iEbene, BigDecimal mengenfaktor, int bisEbene,TheClientDto theClientDto);

	public JasperPrintLP printBedarfszusammenschau(boolean bMitHandlagerbewegungen, boolean bMitArtikelkommentar,
			TheClientDto theClientDto);

	public GesamtkalkulationDto getDatenGesamtkalkulation(Integer losIId, Integer losablieferungIId,int bisEbene,
			TheClientDto theClientDto);

	public JasperPrintLP printPruefplan(Integer losIId, TheClientDto theClientDto);

	public JasperPrintLP printPruefergebnis(java.sql.Timestamp tVon, java.sql.Timestamp tBis, Integer losIId,
			Integer stuecklisteIId, Integer auftragIId, TheClientDto theClientDto);

	public JasperPrintLP printNichtErfasstePruefergebnisse(java.sql.Timestamp tVon, java.sql.Timestamp tBis,
			TheClientDto theClientDto);

	public JasperPrintLP printVergleichMitStueckliste(Integer losIId, TheClientDto theClientDto);

	public ArrayList<FehlmengenBeiAusgabeMehrererLoseDto> getFehlmengenBeiAusgabeMehrerLoseEinesStuecklistenbaumes(
			Integer losIId, TheClientDto theClientDto);

	public JasperPrintLP printLosEtikett(Integer losIId, BigDecimal bdMenge, String sKommentar, boolean bMitInhalten,
			Integer iExemplare, String cReportnamevariante, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printVersandetikettAblieferung(Integer losablieferungIId, Integer iKopien, TheClientDto theClientDto);

	public JasperPrintLP printVersandetikettVorbereitung(Integer losIId, TheClientDto theClientDto);

	JasperPrintLP printVersandetikettVorbereitungForecast(Integer forecastpositionId, TheClientDto theClientDto);

	/**
	 * Druckt ein Ablieferetikett &uuml;ber den Server am im Arbeitslplatzparameter
	 * {@link ParameterFac#ARBEITSPLATZPARAMETER_DRUCKERNAME_MOBILES_LOSABLIEFER_ETIKETT}
	 * definierten Drucker. Ist keiner definiert, wird der Druck nicht erstellt und
	 * auch keine EJBException geworfen
	 * 
	 * @param losablieferungIId
	 * @param bdHandmenge
	 * @param theClientDto
	 * @return Druck des Ablieferetiketts oder null, wenn kein Druckername definiert
	 *         wurde
	 */
	JasperPrintLP printAblieferEtikettOnServer(Integer losablieferungIId, BigDecimal bdHandmenge,
			TheClientDto theClientDto);

	public Object[][] getDataHalbfertigfabrikatsinventur(java.sql.Timestamp tsStichtag, int iSortierung,
			boolean bVerdichtet, Integer partnerIIdFertigungsort, boolean bSortiertNachFertigungsgruppe,
			boolean bNurMaterialwerte, boolean referenznummerStattArtikelnummer, TheClientDto theClientDto)
			throws RemoteException;

	public JasperPrintLP printArbeitszeitstatus(DatumsfilterVonBis datumsfilter, TheClientDto theClientDto);

	JasperPrintLP printFertigungsbegleitscheinOnServer(Integer losId, String printerName, TheClientDto theClientDto);

	JasperPrintLP printAusgabeListeOnServer(Integer losId, Integer iSortierung, boolean bVerdichtetNachIdent,
			boolean bVorrangigNachFarbcodeSortiert, Integer artikelklasseIId, String alternativerReport,
			String printerName, TheClientDto theClientDto) throws EJBExceptionLP;

	public JasperPrintLP printBedarfsuebernahmeSynchronisierung(Integer personalIId, boolean bStatusAufOffenSetzen,
			TheClientDto theClientDto);

	public JasperPrintLP printBedarfsuebernahmeBuchungsliste(boolean bStatusAufVerbuchtUndGedrucktSetzen,
			TheClientDto theClientDto);

	public JasperPrintLP printBedarfsuebernahmeOffene(Integer personalIId, Integer losIId, boolean bNurRueckgaben,
			TheClientDto theClientDto);

	JasperPrintLP printBedarfsuebernahmeSynchronisierungOnServer(Integer personalIId, boolean bStatusAufOffenSetzen,
			TheClientDto theClientDto);

	JasperPrintLP printLosEtikettOnServer(Integer losIId, BigDecimal bdMenge, String sKommentar, boolean bMitInhalten,
			Integer iExemplare, String printerName, TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	JasperPrintLP printAblieferungsstatistik(AblieferstatistikJournalKriterienDto kritDto, TheClientDto theClientDto)
			throws EJBExceptionLP;
	
	public StklPosDtoSearchResult findStklPositionByLossollmaterial(List<StuecklistepositionDto> stklPositionen,
			StklPosDtoSearchParams params, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;
	
	public JasperPrintLP printSonderetikett(String s, TheClientDto theClientDto);
	
	public JasperPrintLP printMehrereFertigungsbegleitscheine(ArrayList<Integer> losIIds,
			TheClientDto theClientDto);

	JasperPrintLP printTraceImport(String filename, ArrayList<String[]> alDatenCSV, 
			TheClientDto theClientDto);
	public void traceImportBuchen(ArrayList<BucheSerienChnrAufLosDto> zuBuchen, TheClientDto theClientDto);
}
