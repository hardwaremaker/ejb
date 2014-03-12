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
package com.lp.server.partner.service;

import java.rmi.RemoteException;
import java.sql.Date;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.LPDatenSubreport;

@Remote
public interface KundeReportFac {

	public final static String REPORT_KUNDE_LIEFERSTATISTIK_KRIT_KUNDE_I_ID = "krit_kunde_i_id";
	public final static String REPORT_KUNDE_LIEFERSTATISTIK_KRIT_D_VON = "krit_d_von";
	public final static String REPORT_KUNDE_LIEFERSTATISTIK_KRIT_D_BIS = "krit_d_bis";

	// usecases
	public final static int UC_REPORT_KUNDE_LIEFERSTATISTIK = 0;
	public final static int UC_REPORT_KUNDE_UMSATZSTATISTIK = 1;
	public final static int UC_REPORT_KUNDE_STATISTIK = 2;
	public final static int UC_REPORT_KUNDE_STAMMBLATT = 3;
	public final static int UC_REPORT_KUNDE_KUNDENLISTE = 4;
	public final static int UC_REPORT_KUNDE_WARTUNGSAUSWERTUNG = 5;
	public final static int UC_REPORT_KUNDE_MONATSSTATISTIK = 6;
	public final static int UC_REPORT_KUNDE_KUNDENPREISLISTE = 7;

	public final static int REPORT_KUNDENLISTE_OPTION_PROJEKTEALLE = 1;
	public final static int REPORT_KUNDENLISTE_OPTION_PROJEKTEOFFENE = 2;
	
	public final static int REPORT_LIEFERSTATISTIK_OPTION_LIEFERADRESSE = 0;
	public final static int REPORT_LIEFERSTATISTIK_OPTION_RECHNUNGSADRESSE = 1;
	public final static int REPORT_LIEFERSTATISTIK_OPTION_STATISTIKADRESSE = 2;
	
	public final static int REPORT_STATISTIK_RE_OR_GS = 0;
	public final static int REPORT_STATISTIK_RECHNUNGSNUMMER = 1;
	public final static int REPORT_STATISTIK_LIEFERSCHEINNUMMER = 2;
	public final static int REPORT_STATISTIK_DATUM = 3;
	public final static int REPORT_STATISTIK_IDENT = 4;
	public final static int REPORT_STATISTIK_BEZEICHNUNG = 5;
	public final static int REPORT_STATISTIK_MENGE = 6;
	public final static int REPORT_STATISTIK_PREIS = 7;
	public final static int REPORT_STATISTIK_SERIENNUMMER = 8;
	public final static int REPORT_STATISTIK_MWSTSATZ = 9;
	public final static int REPORT_STATISTIK_ARTIKELGRUPPE = 10;
	public final static int REPORT_STATISTIK_ARTIKELKLASSE = 11;
	public final static int REPORT_STATISTIK_KONTO = 12;
	public final static int REPORT_STATISTIK_LAND = 13;
	public final static int REPORT_STATISTIK_PLZ = 14;
	public final static int REPORT_STATISTIK_PROVISIONSEMPFAENGER = 15;
	public final static int REPORT_STATISTIK_VERTRETER = 16;
	public final static int REPORT_STATISTIK_STATISTIKADRESSE = 17;
	public final static int REPORT_STATISTIK_ORT = 18;
	public final static int REPORT_STATISTIK_PLZ_STATISTIKADRESSE = 19;
	public final static int REPORT_STATISTIK_LAND_STATISTIKADRESSE = 20;
	public final static int REPORT_STATISTIK_ORT_STATISTIKADRESSE = 21;
	public final static int REPORT_STATISTIK_KUNDENNAME = 22;
	public final static int REPORT_STATISTIK_VERLEIHTAGE = 23;
	public final static int REPORT_STATISTIK_VERLEIHFAKTOR = 24;
	public final static int REPORT_STATISTIK_SETARTIKEL_TYP = 25;
	public final static int REPORT_STATISTIK_MATERIALZUSCHLAG = 26;
	public final static int REPORT_STATISTIK_ANZAHL_FELDER = 27;
	
	public final static int REPORT_MONATSSTATISTIK_MONAT = 0;
	public final static int REPORT_MONATSSTATISTIK_JAHR = 1;
	public final static int REPORT_MONATSSTATISTIK_MENGE = 2;
	public final static int REPORT_MONATSSTATISTIK_WERT = 3;
	public final static int REPORT_MONATSSTATISTIK_ANZAHL_FELDER = 25;
	
	public final static String REPORT_MODUL = "partner";

	public final static String REPORT_KUNDENSTAMMBLATT = "part_kundenstammblatt.jasper";
	public final static String REPORT_KUNDENLISTE = "part_kundenliste.jasper";
	public final static String REPORT_WARTUNGSAUSWERTUNG = "part_wartungsauswertung.jasper";
	public final static String REPORT_WARTUNGSAUSWERTUNG_ARTIKELLIEFERANT = "part_wartungsauswertung_artikellieferant.jasper";

	public final static String REPORT_KUNDENPREISLISTE = "part_kundenpreisliste.jasper";
	
	
	public JasperPrintLP printLieferStatistik(TheClientDto theClientDto,
			Integer iIdkundeI, Integer artikelIId, Integer artikelgruppeIId, Date dVonI, Date dBisI, Integer iSortierungI,
			boolean bMitTexteingaben, boolean bVerdichtetNachArtikel, boolean bEingeschraenkt,boolean bMonatsstatistik, int iOptionAdresse,boolean bRechnungsdatum);

	public JasperPrintLP printKundenstammblatt(Integer kundeIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;
	public JasperPrintLP printKundenpreisliste(Integer kundeIId, Integer artikelgruppeIId, Integer artikelklasseIId,
			boolean bMitInaktiven, String artikelNrVon, String artikelNrBis,
			boolean bMitVersteckten, java.sql.Date datGueltikeitsdatumI,boolean nurSonderkonditionen, boolean bMitArtikelbezeichnungenInMandantensprache, 
			TheClientDto theClientDto);
	public JasperPrintLP printKundenstatistik(
			StatistikParamDto statistikParamDtoI) throws RemoteException;

	public Object[][] getDataLieferstatistik(TheClientDto theClientDto,
			Integer kundeIId, Integer artikelIId ,Integer artikelgruppeIId, Date dVon, Date dBis, Integer iSortierung,
			String mandantCNr, boolean bMitTexteingaben,
			boolean bVerdichtetNachArtikel,boolean bEingeschraenkt, int iOptionAdresse, boolean bRechnungsdatum);
	
	public JasperPrintLP printKundenliste(TheClientDto theClientDto,
			boolean bUmsatzNachStatistikadresse, boolean bMitVersteckten,
			boolean bMitInteressenten, boolean bMitAnsprechpartner, Integer kundeIIdSelektiert, int iProjektemitdrucken, String cPlz, Integer landIId, Integer brancheIId, Integer partnerklasseIId) throws RemoteException;
	public LPDatenSubreport getSubreportProjekte(Integer partnerIId,
			boolean bNurOffene, TheClientDto theClientDto);
	
	public JasperPrintLP printWartungsauswertung(java.sql.Timestamp tStichtag,boolean bVerdichtet, boolean bSortiertNachArtikellieferant, TheClientDto theClientDto)throws RemoteException;

	public CustomerPricelistReportDto printKundenpreislisteRaw(Integer kundeIId,
			Integer artikelgruppeIId, Integer artikelklasseIId,
			boolean bMitInaktiven, String artikelNrVon, String artikelNrBis,
			boolean bMitVersteckten, java.sql.Date datGueltikeitsdatumI,
			boolean nurSonderkonditionen,
			boolean bMitArtikelbezeichnungenInMandantensprache,
			boolean nurWebshopartikel,
			TheClientDto theClientDto) ;	
}
