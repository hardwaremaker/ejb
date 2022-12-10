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
package com.lp.server.stueckliste.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.DatumsfilterVonBis;
import com.lp.server.util.report.JasperPrintLP;

@Remote
public interface StuecklisteReportFac {

	public final static String REPORT_MODUL = "stueckliste";

	public final static int REPORT_STUECKLISTE_OPTION_PREIS_EINKAUFSPREIS = 0;
	public final static int REPORT_STUECKLISTE_OPTION_PREIS_GESTEHUNGSSPREIS = 1;
	public final static int REPORT_STUECKLISTE_OPTION_PREIS_VERKAUFSSPREIS = 2;

	public final static int REPORT_STUECKLISTE_OPTION_SORTIERUNG_ARTIKELNR = 0;
	public final static int REPORT_STUECKLISTE_OPTION_SORTIERUNG_POSITION = 1;
	public final static int REPORT_STUECKLISTE_OPTION_SORTIERUNG_OHNE = 2;

	public final static String REPORT_STUECKLISTE_EIGENSCHAFTEN_INDEX = "Index";
	public final static String REPORT_STUECKLISTE_EIGENSCHAFTEN_MATERIALPLATZ = "Materialplatz";

	public final static String REPORT_STUECKLISTE_ALLGEMEIN_OHNEPREIS = "stk_stueckliste.jasper";
	public final static String REPORT_STUECKLISTE_ALLGEMEIN_MITPREIS = "stk_stuecklistemitpreis.jasper";
	public final static String REPORT_STUECKLISTE_AUSGABESTUECKLSITE = "stk_ausgabestueckliste.jasper";
	public final static String REPORT_STUECKLISTE_ARBEITSPLAN = "stk_arbeitsplan.jasper";
	public final static String REPORT_STUECKLISTE_FREIGABE = "stk_freigabe.jasper";
	public final static String REPORT_STUECKLISTE_GESAMTKALKULATION = "stk_gesamtkalkulation.jasper";
	public final static String REPORT_STUECKLISTE_REICHWEITE = "stk_reichweite.jasper";
	public final static String REPORT_STUECKLISTE_PRUEFKOMBINATION = "stk_pruefkombination.jasper";
	public final static String REPORT_STUECKLISTE_PRUEFPLAN = "stk_pruefplan.jasper";
	public final static String REPORT_STUECKLISTE_VERSCHLEISSTEILVERWENDUNG = "stk_verschleissteilverwendung.jasper";
	public final static String REPORT_STUECKLISTE_MINDERVERFUEGBARKEIT = "stk_minderverfuegbarkeit.jasper";
	public final static String REPORT_STUECKLISTE_VERGLEICH_MIT_ANDERER_STUECKLISTE = "stk_vergleich_mit_anderer_stueckliste.jasper";
	public final static String REPORT_STUECKLISTE_WAFFENREGISTER = "stk_waffenregister.jasper";

	public final static int REPORT_AUSGABESTUECKLISTE_OPTION_LAGER_SELEKTIERT = 0;
	public final static int REPORT_AUSGABESTUECKLISTE_OPTION_NUR_ABBUCHUNGSLAEGER = 1;
	public final static int REPORT_AUSGABESTUECKLISTE_OPTION_ALLE_LAEGER = 2;

	public final static String REPORT_STUECKLISTE_LOSEAKTUALISIERT = "stk_loseaktualisiert.jasper";
	public static int REPORT_STUECKLISTE_LOSEAKTUALISIERT_LOSNUMMER = 0;
	public static int REPORT_STUECKLISTE_LOSEAKTUALISIERT_ARTIKELNUMMER = 1;
	public static int REPORT_STUECKLISTE_LOSEAKTUALISIERT_BEZEICHNUNG = 2;
	public static int REPORT_STUECKLISTE_LOSEAKTUALISIERT_ZUSATZBEZEICHNUNG = 3;
	public static int REPORT_STUECKLISTE_LOSEAKTUALISIERT_EINHEIT = 4;
	public static int REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_AUSGABEMENGE = 5;
	public static int REPORT_STUECKLISTE_LOSEAKTUALISIERT_BEMERKUNG = 6;
	public static int REPORT_STUECKLISTE_LOSEAKTUALISIERT_KORREKTUR_SOLLMENGE = 7;
	public static int REPORT_STUECKLISTE_LOSEAKTUALISIERT_ANZAHL_SPALTEN = 8;

	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKEL = 0;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKELBEZEICHNUNG = 1;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_STUECKLISTENEINHEIT = 2;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_MENGE = 3;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_POSITION = 4;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_PREIS = 5;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_DIMENSION = 6;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ZIELMENGENEINHEIT = 7;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ZIELMENGE = 8;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_MONTAGEART = 9;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_HOCHSTELLEN = 10;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_POSITIONSKOMMENTAR = 11;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_IMAGE = 12;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_HERSTELLER = 13;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_KURZBEZEICHNUNG = 14;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_RASTERLIEGEND = 15;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_RASTERSTEHEND = 16;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_HOCHSETZEN = 17;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKELZUSATZBEZEICHNUNG = 18;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKELZUSATZBEZEICHNUNG2 = 19;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_KOMMENTAR = 20;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_HERSTELLER_NAME = 21;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_LFDNUMMER = 22;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_REFERENZNUMMER = 23;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_INDEX = 24;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_REVISION = 25;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_WARENVERKEHRSNUMMER = 26;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_URSPRUNGSLAND_LKZ = 27;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_URSPRUNGSLAND_NAME = 28;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_STUECKLISTEPOSITION_I_ID = 29;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ERSATZARTIKEL = 30;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ERSATZARTIKEL_BEZEICHNUNG = 31;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ERSATZARTIKEL_ZUSATZBEZEICHNUNG = 32;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ERSATZARTIKEL_ZUSATZBEZEICHNUNG2 = 33;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_I_EBENE = 34;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_GEWICHT = 35;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_I_SORT = 36;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKEL_HERSTELLERNUMMER = 37;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ERSATZARTIKEL_HERSTELLERNUMMER = 38;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKEL_HERSTELLERBEZEICHNUNG = 39;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ERSATZARTIKEL_HERSTELLERBEZEICHNUNG = 40;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_FREIGABE_ZEITPUNKT = 41;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_FREIGABE_PERSON = 42;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_KUNDENARTIKELNUMMER = 43;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_KUNDENARTIKELBEZEICHNUNG = 44;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_SUBREPORT_ARTIKELKOMMENTAR = 45;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_FORMEL = 46;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_FREMDFERTIGUNG = 47;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_DIMENSION1 = 48;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_DIMENSION2 = 49;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_DIMENSION3 = 50;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_POSITIONSEINHEIT = 51;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKEL_FREIGABE_ZEITPUNKT = 52;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKEL_FREIGABE_PERSON = 53;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ARTIKEL_MATERIAL = 54;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_INITIALKOSTEN = 55;
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ANZAHL_SPALTEN = 56;

	
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_ARTIKEL = 0;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_ARTIKELBEZEICHNUNG = 1;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_STUECKLISTENEINHEIT = 2;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_MENGE = 3;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_POSITION = 4;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_PREIS = 5;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_DIMENSION = 6;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_ZIELMENGENEINHEIT = 7;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_ZIELMENGE = 8;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_MONTAGEART = 9;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_HOCHSTELLEN = 10;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_IMAGE = 12;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_HERSTELLER = 13;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_KURZBEZEICHNUNG = 14;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_RASTERLIEGEND = 15;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_RASTERSTEHEND = 16;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_HOCHSETZEN = 17;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_ARTIKELZUSATZBEZEICHNUNG = 18;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_ARTIKELZUSATZBEZEICHNUNG2 = 19;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_KOMMENTAR = 20;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_HERSTELLER_NAME = 21;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_LFDNUMMER = 22;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_REFERENZNUMMER = 23;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_INDEX = 24;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_REVISION = 25;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_WARENVERKEHRSNUMMER = 26;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_URSPRUNGSLAND_LKZ = 27;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_URSPRUNGSLAND_NAME = 28;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_STUECKLISTEPOSITION_I_ID = 29;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_ERSATZARTIKEL = 30;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_ERSATZARTIKEL_BEZEICHNUNG = 31;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_ERSATZARTIKEL_ZUSATZBEZEICHNUNG = 32;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_ERSATZARTIKEL_ZUSATZBEZEICHNUNG2 = 33;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_I_EBENE = 34;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_GEWICHT = 35;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_I_SORT = 36;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_ARTIKEL_HERSTELLERNUMMER = 37;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_ERSATZARTIKEL_HERSTELLERNUMMER = 38;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_ARTIKEL_HERSTELLERBEZEICHNUNG = 39;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_ERSATZARTIKEL_HERSTELLERBEZEICHNUNG = 40;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_FREIGABE_ZEITPUNKT = 41;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_FREIGABE_PERSON = 42;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_KUNDENARTIKELNUMMER = 43;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_KUNDENARTIKELBEZEICHNUNG = 44;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_SUBREPORT_ARTIKELKOMMENTAR = 45;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_FORMEL = 46;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_FREMDFERTIGUNG = 47;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_DIMENSION1 = 48;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_DIMENSION2 = 49;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_DIMENSION3 = 50;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_POSITIONSEINHEIT = 51;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_ARTIKEL_FREIGABE_ZEITPUNKT = 52;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_ARTIKEL_FREIGABE_PERSON = 53;
	
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_WAFFENKALIBER = 54;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_WAFFENAUSFUEHRUNG = 55;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_WAFFENTYP = 56;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_WAFFENTYPFEIN = 57;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_WAFFENKATEGORIE = 58;
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_WAFFENZUSATZ = 59;
	
	
	public static int REPORT_STUECKLISTE_WAFFENREGISTER_ANZAHL_SPALTEN = 60;
	
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKEL = 0;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKELBEZEICHNUNG = 1;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ZIELMENGENEINHEIT = 2;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ZIELMENGE = 3;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_PREIS = 4;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_WERT = 5;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_STUECKLISTENKOPF = 6;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_SUMMEDERPOSITIONENFUERKOPF = 7;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_IMAGE = 8;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_HERSTELLER = 9;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_KURZBEZEICHNUNG = 10;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_RASTERLIEGEND = 11;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_RASTERSTEHEND = 12;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_HOCHSTELLEN = 13;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_HOCHSETZEN = 14;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKELZUSATZBEZEICHNUNG = 15;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKELZUSATZBEZEICHNUNG2 = 16;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_KOMMENTAR = 17;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_HERSTELLER_NAME = 18;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_POSITION = 19;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_MONTAGEART = 20;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_LFDNUMMER = 21;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_INDEX = 22;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_REVISION = 23;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_REFERENZNUMMER = 24;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_WARENVERKEHRSNUMMER = 25;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_URSPRUNGSLAND_LKZ = 26;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_URSPRUNGSLAND_NAME = 27;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_STUECKLISTEPOSITION_I_ID = 28;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ERSATZARTIKEL = 29;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ERSATZARTIKEL_BEZEICHNUNG = 30;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ERSATZARTIKEL_ZUSATZBEZEICHNUNG = 31;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ERSATZARTIKEL_ZUSATZBEZEICHNUNG2 = 32;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_I_EBENE = 33;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_GEWICHT = 34;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_KUNDENPREIS = 35;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_I_SORT = 36;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_AUF_BELEG_MITDRUCKEN = 37;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_KALK_PREIS = 38;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_KOMMENTAR_POSITION = 39;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_MATERIALZUSCHLAG = 40;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKEL_HERSTELLERNUMMER = 41;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ERSATZARTIKEL_HERSTELLERNUMMER = 42;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKEL_HERSTELLERBEZEICHNUNG = 43;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ERSATZARTIKEL_HERSTELLERBEZEICHNUNG = 44;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_FREIGABE_ZEITPUNKT = 45;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_FREIGABE_PERSON = 46;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_KUNDENARTIKELNUMMER = 47;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_KUNDENARTIKELBEZEICHNUNG = 48;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_SUBREPORT_ARTIKELKOMMENTAR = 49;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_FREMDFERTIGUNG = 50;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKEL_FREIGABE_ZEITPUNKT = 51;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKEL_FREIGABE_PERSON = 52;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_MELDEPFLICHTIG = 53;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_BEWILLIGUNGSPFLICHTIG = 54;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ARTIKEL_MATERIAL = 55;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_PREIS_LETZTE_WEP = 56;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_INITIALKOSTEN = 57;
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ANZAHL_SPALTEN = 58;

	public JasperPrintLP printAusgabestueckliste(Integer[] stuecklisteIId, Integer lagerIId,
			Boolean bMitStuecklistenkommentar, Boolean bUnterstuecklistenEinbinden,
			Boolean bGleichePositionenZusammenfassen, BigDecimal nLosgroesse, boolean bSortiertNachArtikelbezeichnung,
			int iOptionLager, boolean fremdfertigungAufloesen, TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printMinderverfuegbarkeit(Integer stuecklisteIId, boolean inFertigungBeruecksichtigen,
			TheClientDto theClientDto);

	public JasperPrintLP printPruefplan(Integer stuecklisteIId, TheClientDto theClientDto);

	public JasperPrintLP printPruefkombinationen(Integer artikelIIdKontakt, Integer artikelIIdLitze,
			String bezeichnungLitze, TheClientDto theClientDto);

	public JasperPrintLP printArbeitsplan(Integer stuecklisteIId, BigDecimal nLosgroesse, TheClientDto theClientDto)
			throws RemoteException;

	public JasperPrintLP printStuecklisteAllgemeinMitPreis(Integer stuecklisteIId, Integer iOptionPreis,
			Boolean bMitPositionskommentar, Boolean bMitStuecklistenkommentar, Boolean bUnterstuecklistenEinbinden,
			Boolean bGleichePositionenZusammenfassen, Integer iOptionSortierungUnterstuecklisten,
			boolean bUnterstklstrukurBelassen, TheClientDto theClientDto, Integer iOptionSortierungStuecklisteGesamt1,
			Integer iOptionSortierungStuecklisteGesamt2, Integer iOptionSortierungStuecklisteGesamt3,
			boolean fremdfertigungAufloesen) throws RemoteException;

	public JasperPrintLP printReichweite(Integer stuecklisteIId, DatumsfilterVonBis vonBis, boolean bVerdichtet,
			TheClientDto theClientDto);

	public JasperPrintLP printLoseAktualisiert(TreeMap<String, Object[]> tmAufgeloesteFehlmengen, Integer stuecklisteIId,boolean bInclAusgegebenUndInProduktion,
			TheClientDto theClientDto);

	public JasperPrintLP printStuecklisteAllgemein(Integer stuecklisteIId, Boolean bMitPositionskommentar,
			Boolean bMitStuecklistenkommentar, Boolean bUnterstuecklistenEinbinden,
			Boolean bGleichePositionenZusammenfassen, Integer iOptionSortierungUnterstuecklisten,
			boolean bUnterstklstrukurBelassen, TheClientDto theClientDto, Integer iOptionSortierungStuecklisteGesamt1,
			Integer iOptionSortierungStuecklisteGesamt2, Integer iOptionSortierungStuecklisteGesamt3,
			String[] labelSortierungen, boolean fremdfertigungAufloesen) throws RemoteException;

	public Hashtable<?, ?> getStuecklisteEigenschaften(Integer iIdArtikelIDI, String sMandantCNr,
			TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printVerschleissteilverwendung(Integer werkzeugIId, TheClientDto theClientDto);

	public JasperPrintLP printVergleichMitAndererStueckliste(Integer stuecklisteIId, Integer stuecklisteIId2,
			boolean bSortiertNachArtikelnummer, boolean bVerdichtetNachArtikelnummer, boolean bHerstellerunabhaengig, boolean bNurUnterschiede,
			TheClientDto theClientDto);

	JasperPrintLP printGesamtkalkulationKonfigurator(Integer stuecklisteIId, BigDecimal nLosgroesse,
			boolean lief1PreisInKalkpreisUebernehmen, boolean bMitPreisenDerLetzten2Jahre,
			boolean unterstuecklistenVerdichten, boolean bUeberAlleMandanten, boolean bFremdfertigungAufloesen,
			Map<String, Object> konfigurationsWerte, Integer kundeIId_Uebersteuert,boolean minBSMengeUndVPEBeruecksichtigen,Double dMaterialgemeinkostenfaktor,Double dArbeitszeitgemeinkostenfaktor,Double dFertigungsgemeinkostenfaktor, Date preisGueltig, TheClientDto theClientDto);

	JasperPrintLP printGesamtkalkulation(Integer stuecklisteIId, BigDecimal nLosgroesse,
			boolean lief1PreisInKalkpreisUebernehmen, boolean bMitPreisenDerLetzten2Jahre,
			boolean unterstuecklistenVerdichten, boolean bUeberAlleMandanten, boolean nachArtikelCnrSortieren,
			boolean bFremdfertigungAufloesen, boolean minBSMengeUndVPEBeruecksichtigen,Double dMaterialgemeinkostenfaktor,Double dArbeitszeitgemeinkostenfaktor,Double dFertigungsgemeinkostenfaktor, boolean gesamtmengeMaterialBeruecksichtigen, Date preisGueltig, TheClientDto theClientDto);

	public JasperPrintLP printFreigabe(Integer stuecklisteIId, TheClientDto theClientDto);
	
	public JasperPrintLP printWaffenregister(Integer stuecklisteIId,  TheClientDto theClientDto);
	
}
