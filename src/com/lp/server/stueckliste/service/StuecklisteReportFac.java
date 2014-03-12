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
package com.lp.server.stueckliste.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.TreeMap;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;
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
	public final static String REPORT_STUECKLISTE_GESAMTKALKULATION = "stk_gesamtkalkulation.jasper";

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
	public static int REPORT_STUECKLISTE_ALLGEMEINOHNEPREIS_ANZAHL_SPALTEN = 37;

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
	public static int REPORT_STUECKLISTE_ALLGEMEINMITPREIS_ANZAHL_SPALTEN = 41;

	public JasperPrintLP printAusgabestueckliste(Integer[] stuecklisteIId,
			Integer lagerIId, Boolean bMitStuecklistenkommentar,
			Boolean bUnterstuecklistenEinbinden,
			Boolean bGleichePositionenZusammenfassen,
			Integer iOptionSortierungUnterstuecklisten, BigDecimal nLosgroesse,
			boolean bUnterstklstrukurBelassen, TheClientDto theClientDto)
			throws RemoteException;

	public JasperPrintLP printGesamtkalkulation(Integer stuecklisteIId,
			BigDecimal nLosgroesse, TheClientDto theClientDto)
			throws RemoteException;

	public JasperPrintLP printArbeitsplan(Integer stuecklisteIId,
			BigDecimal nLosgroesse, TheClientDto theClientDto)
			throws RemoteException;

	public JasperPrintLP printStuecklisteAllgemeinMitPreis(
			Integer stuecklisteIId, Integer iOptionPreis,
			Boolean bMitPositionskommentar, Boolean bMitStuecklistenkommentar,
			Boolean bUnterstuecklistenEinbinden,
			Boolean bGleichePositionenZusammenfassen,
			Integer iOptionSortierungUnterstuecklisten,
			boolean bUnterstklstrukurBelassen, TheClientDto theClientDto,
			Integer iOptionSortierungStuecklisteGesamt1,
			Integer iOptionSortierungStuecklisteGesamt2,
			Integer iOptionSortierungStuecklisteGesamt3) throws RemoteException;

	public JasperPrintLP printLoseAktualisiert(
			TreeMap<String, Object[]> tmAufgeloesteFehlmengen,
			TheClientDto theClientDto);

	public JasperPrintLP printStuecklisteAllgemein(Integer stuecklisteIId,
			Boolean bMitPositionskommentar, Boolean bMitStuecklistenkommentar,
			Boolean bUnterstuecklistenEinbinden,
			Boolean bGleichePositionenZusammenfassen,
			Integer iOptionSortierungUnterstuecklisten,
			boolean bUnterstklstrukurBelassen, TheClientDto theClientDto,
			Integer iOptionSortierungStuecklisteGesamt1,
			Integer iOptionSortierungStuecklisteGesamt2,
			Integer iOptionSortierungStuecklisteGesamt3) throws RemoteException;

	public Hashtable<?, ?> getStuecklisteEigenschaften(Integer iIdArtikelIDI,
			String sMandantCNr, TheClientDto theClientDto)
			throws RemoteException;

}
