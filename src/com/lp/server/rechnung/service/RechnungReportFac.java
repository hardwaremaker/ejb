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
package com.lp.server.rechnung.service;

import java.rmi.RemoteException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;

@Remote
public interface RechnungReportFac {

	// reportregeln: 0 Definiton des Moduls
	public final static String REPORT_MODUL = "rechnung";
	// reportregeln: 1 Definiton der Reportnamen
	public final static String REPORT_GUTSCHRIFT = "rech_gutschrift.jasper";
	public final static String REPORT_PROFORMARECHNUNG = "rech_proformarechnung.jasper";
	public final static String REPORT_RECHNUNG = "rech_rechnung.jasper";
	public final static String REPORT_GUTSCHRIFTEN_ALLE = "rech_gutschriften_alle.jasper";
	public final static String REPORT_PROFORMARECHNUNGEN_ALLE = "rech_proformarechnungen_alle.jasper";
	public final static String REPORT_RECHNUNGEN_ALLE = "rech_rechnungen_alle.jasper";
	public final static String REPORT_RECHNUNGEN_ZM = "rech_zusammenfassendemeldung.jasper";

	public final static String REPORT_RECHNUNGEN_OFFENE = "rech_rechnungen_offene.jasper";
	public final static String REPORT_RECHNUNGEN_UMSATZ = "rech_rechnungen_umsatz.jasper";
	public final static String REPORT_ZAHLUNGSJOURNAL = "rech_zahlungsjournal.jasper";
	public final static String REPORT_ZEITNACHWEIS = "rech_zeitnachweis.jasper";
	public final static String REPORT_WARENAUSGANGSJOURNAL = "rech_warenausgangsjournal.jasper";
	public final static String REPORT_PROVISIONSABRECHNUNG = "rech_provisionsabrechnung.jasper";
	public final static String REPORT_RECHNUNGEN_WARENAUSGANGSJOURNAL = "rech_warenausgangsjournal.jasper";
	public final static String REPORT_RECHNUNGEN_ZAHLSCHEIN = "rech_zahlschein.jasper";
	public final static String REPORT_NICHT_ABGERECHNETE_ANZAHLUNGEN = "re_nicht_abgerechnete_anzahlungen.jasper";
	public final static String REPORT_MATERIALEINSATZ = "rech_materialeinsatz.jasper";

	public final static int UMSATZ_FELD_MONAT = 0;
	public final static int UMSATZ_FELD_WERT = 1;
	public final static int UMSATZ_FELD_WERT_UST = 2;
	public final static int UMSATZ_FELD_WERT_NICHT_ABGERECHNETE_ANZAHLUNGEN = 3;
	public final static int UMSATZ_FELD_WERT_NICHT_ABGERECHNETE_ANZAHLUNGEN_UST = 4;
	public final static int UMSATZ_FELD_WERT_VJ = 5;
	public final static int UMSATZ_FELD_WERT_VJ_UST = 6;
	public final static int UMSATZ_FELD_DB = 7;
	public final static int UMSATZ_FELD_DB_VJ = 8;

	public final static int UMSATZ_FELD_OFFEN = 9;
	public final static int UMSATZ_FELD_OFFEN_VJ = 10;
	public final static int UMSATZ_FELD_OFFEN_UST = 11;
	public final static int UMSATZ_FELD_OFFEN_UST_VJ = 12;

	public final static int UMSATZ_FELD_ANZAHL_SPALTEN = 13;

	public final static int REPORT_ZAHLUNGEN_SORT_ZAHLUNGSAUSGANG = 0;
	public final static int REPORT_ZAHLUNGEN_SORT_RECHNUNGSNUMMER = 1;
	public final static int REPORT_ZAHLUNGEN_SORT_BANK_AUSZUG = 2;

	public JasperPrintLP[] printGutschrift(Integer pKey, Locale locale, Boolean bMitLogo, Integer iAnzahlKopien,
			String drucktype, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP[] printProformarechnung(Integer pKey, Locale locale, Boolean bMitLogo, Integer iAnzahlKopien,
			String drucktype, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP[] printRechnung(Integer pKey, Locale locale, Boolean bMitLogo, Integer iAnzahlKopien,
			String drucktype, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void erstelleEinzelexport(Integer rechnungIId, String pfad, boolean bSortiertNachArtikelnummer,
			TheClientDto theClientDto);

	public JasperPrintLP printRechnungAlsMahnung(Integer rechnungIId, Locale locale, Boolean bMitLogo,
			Integer iMahnstufe, String drucktype, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printRechnungenUmsatz(TheClientDto theClientDto, Integer iGeschaeftsjahr,
			Boolean bMitGutschriften, boolean bMitDeckungsbeitrag, boolean bOhneAndereMandanten, boolean bMitOffenen)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printGutschriftenAlle(ReportRechnungJournalKriterienDto krit, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printRechnungenOffene(ReportRechnungJournalKriterienDto krit, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printZusammenfassendeMeldung(java.sql.Date dVon, java.sql.Date dBis,
			Integer partnerIIdFinanzamt, TheClientDto theClientDto);

	public JasperPrintLP printZahlungsjournal(TheClientDto theClientDto, int iSortierung, Date dVon, Date dBis,
			Integer bankverbindungIId, boolean bSortierungNachKostenstelle) throws EJBExceptionLP, RemoteException;

	public String exportWAJournal(TheClientDto theClientDto, Integer kundeIId, Date dVon, Date dBis,
			String sLineSeparator, Integer iSortierung) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printRechnungenAlle(ReportRechnungJournalKriterienDto krit, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printWarenausgangsjournal(ReportRechnungJournalKriterienDto krit, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printProformarechnungenAlle(ReportRechnungJournalKriterienDto krit, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP[] printRechnungZahlschein(Integer iRechnungIId, String sReportname, Integer iKopien,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printNichtabgerechneteAnzahlungen(TheClientDto theClientDto);

	public String getArtikelsetType(RechnungPositionDto reposDto);

	public Object[] getZeileAnhandJahrMonat(TheClientDto theClientDto, Integer iJahr, Locale locUI,
			SimpleDateFormat dateformat, String sKriterium, boolean bMitDeckungsbeitrag, boolean bOhneAndereMandanten,
			boolean bMitOffenen, int i) throws RemoteException;

	ZugferdResult createZugferdRechnung(Integer rechnungIId, Locale locale, Boolean bMitLogo, String drucktype,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	ZugferdResult createZugferdGutschrift(Integer rechnungIId, Locale locale, Boolean bMitLogo, String drucktype,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	boolean isZugferdPartner(Integer rechnungIId, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printMaterialeinsatz(Integer iRechnungIId, boolean nachkalkulation, TheClientDto theClientDto);

	public ArrayList<ZeitinfoTransferDto> printZeitnachweis(ArrayList<Integer> projektzeitenIIds, java.sql.Date dateVon, java.sql.Date dateBis, TheClientDto theClientDto);
	
}
