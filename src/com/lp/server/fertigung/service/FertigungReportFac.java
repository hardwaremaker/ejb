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

import javax.ejb.Remote;

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
	public final static String REPORT_LOSETIKETTA4 = "fert_losetikett_a4.jasper";
	public final static String REPORT_ZEITENTWICKLUNG = "fert_zeitentwicklung.jasper";
	public final static String REPORT_AUSLASTUNGSVORSCHAU = "fert_auslastungsvorschau.jasper";
	public final static String REPORT_AUSLASTUNGSVORSCHAU_DETAILIERT = "fert_auslastungsvorschau_detailiert.jasper";
	public final static String REPORT_AUSLIEFERLISTE = "fert_auslieferliste.jasper";
	public final static String REPORT_FEHLTEILE = "fert_fehlteile.jasper";
	public final static String REPORT_ABLIEFERETIKETT = "fert_ablieferetikett.jasper";
	public final static String REPORT_LOSZEITEN = "fert_loszeiten.jasper";
	public final static String REPORT_MATERIALLISTE = "fert_materialliste.jasper";
	public final static String REPORT_GESAMTALKULATION = "fert_gesamtkalkulation.jasper";
	public final static String REPORT_MASCHINEUNDMATERIAL = "fert_maschineundmaterial.jasper";

	public final static String REPORT_PRODUKTIONSINFORMATION = "fert_produktionsinformation.jasper";

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

	public final static Integer OFFENE_OPTION_STICHTAG_LIEFERTERMIN = 0;
	public final static Integer OFFENE_OPTION_STICHTAG_BEGINNDATUM = 1;
	public final static Integer OFFENE_OPTION_STICHTAG_ENDEDATUM = 2;
	
	public final static int ABLIEFERSTATISTIK_OPTION_SORTIERUNG_ABLIEFERDATUM = 0;
	public final static int ABLIEFERSTATISTIK_OPTION_SORTIERUNG_ARTIKEL = 1;
	public final static int ABLIEFERSTATISTIK_OPTION_SORTIERUNG_AUFTRAG = 2;
	

	public JasperPrintLP printTheoretischeFehlmengen(Integer losIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printZeitentwicklung(java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, boolean bSortiertNachArtikelgruppe,
			TheClientDto theClientDto);

	public JasperPrintLP printAusgabeListe(Integer[] losIId,
			Integer iSortierung, boolean bVerdichtetNachIdent,
			boolean bVorrangigNachFarbcodeSortiert, Integer artikelklasseIId,
			String alternativerReport, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printFertigungsbegleitschein(Integer losIId,
			Boolean bStammtVonSchnellanlage, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printProduktionsinformation(Integer losIId,
			TheClientDto theClientDto);

	public JasperPrintLP printAlle(ReportJournalKriterienDto krit,
			boolean bNurAngelegte, Integer fertigungsgruppeIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printOffene(java.sql.Date dStichtag,
			int iOptionStichtag, String belegNrVon, String belegNrBis,
			Integer kundeIId, Integer kostenstelleIId,
			Integer fertigungsgruppeIId, int iSortierung,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printLosstatistik(java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, Integer losIId, Integer stuecklisteIId,
			Integer auftragIId, boolean bArbeitsplanSortiertNachAG,
			boolean bVerdichtet, java.sql.Timestamp tStichtag,
			TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printHalbfertigfabrikatsinventur(
			java.sql.Timestamp tsStichtag, int iSortierung,
			boolean bVerdichtet, Integer partnerIIdFertigungsort,
			boolean bSortiertNachFertigungsgruppe, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printMonatsauswertung(java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, boolean bVerdichtet,
			TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printMaterialliste(Integer losIId,
			TheClientDto theClientDto);

	public JasperPrintLP printRankingliste(TheClientDto theClientDto)
			throws RemoteException;

	public JasperPrintLP printAuslieferliste(java.sql.Timestamp tStichtag,
			TheClientDto theClientDto);

	public JasperPrintLP printNachkalkulation(Integer losIId,
			TheClientDto theClientDto);

	public ReportLosnachkalkulationDto[] getDataNachkalkulationZeitdaten(
			Integer losIId, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public ReportLosnachkalkulationDto getDataNachkalkulationMaterial(
			Integer losIId, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public JasperPrintLP printAblieferungsstatistik(Date dVon, Date dBis,
			Integer artikelIId, int iSortierungAblieferungsstatistik,
			boolean bVerdichtetNachArtikel, boolean bNurKopfloseanhandStueckliste , TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public boolean istErstlos(LosDto losDto, TheClientDto theClientDto);

	public JasperPrintLP printAufloesbareFehlmengen(Integer iSortierung,
			Boolean bNurArtikelMitLagerstand,
			Boolean bOhneEigengefertigteArtikel, TheClientDto theClientDto);

	public JasperPrintLP printFehlmengenAllerLose(
			boolean bNurEigengefertigteArtikel,
			boolean bAlleOhneEigengefertigteArtikel, TheClientDto theClientDto);

	public JasperPrintLP printFehlteile(Integer losIId,
			boolean bNurPositionenMitFehlmengen, TheClientDto theClientDto);

	public JasperPrintLP printStueckrueckmeldung(Integer losIId,
			int iSortierung, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public JasperPrintLP printLoszeiten(Integer iIdAuftragI, int iSortierung,
			java.sql.Timestamp tVon, java.sql.Timestamp tBis,
			TheClientDto theClientDto);

	public JasperPrintLP printLosEtikett(Integer losIId, BigDecimal bdMenge,
			String sKommentar, boolean bMitInhalten, Integer iExemplare,
			TheClientDto theCLientDto) throws RemoteException;

	public JasperPrintLP printLosEtikettA4(Integer losIId, BigDecimal bdMenge,
			String sKommentar, boolean bMitInhalten, Integer iExemplare,
			TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printAuslastungsvorschau(java.sql.Timestamp tStichtag,
			boolean bSortiertNachArtikelgruppe, TheClientDto theClientDto);

	public JasperPrintLP printOffeneArbeitsgaenge(java.sql.Date dStichtag,
			int iOptionStichtag, String belegNrVon, String belegNrBis,
			Integer kundeIId, Integer kostenstelleIId,
			Integer fertigungsgruppeIId, Integer artikelgruppeIId,
			Integer maschineIId, boolean bSollstundenbetrachtung,
			TheClientDto theClientDto);

	public JasperPrintLP printAblieferEtikett(Integer losablieferungIId,
			Integer iExemplare, BigDecimal bdHandmenge,
			TheClientDto theClientDto);

	public JasperPrintLP printFehlerstatistik(java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, Integer iSortierung,
			boolean bAlleAnzeigen, TheClientDto theClientDto);

	public JasperPrintLP printAuslastungsvorschauDetailliert(
			java.sql.Timestamp tStichtag, TheClientDto theClientDto);

	public JasperPrintLP printGesamtkalkulation(Integer losIId,
			TheClientDto theClientDto);

	public JasperPrintLP printMaschineUndMaterial(Integer maschineIId,
			DatumsfilterVonBis vonBis, TheClientDto theClientDto);
}
