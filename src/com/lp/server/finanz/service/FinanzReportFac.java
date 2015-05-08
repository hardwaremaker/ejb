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
package com.lp.server.finanz.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

import javax.ejb.Remote;

import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;

@Remote
public interface FinanzReportFac {

	public final static String REPORT_MODUL = "finanz";
	public final static String REPORT_SALDENLISTE = "finanz_saldenliste.jasper";
	public final static String REPORT_ALLE_KONTEN = "finanz_alle_konten.jasper";
	public final static String REPORT_BUCHUNGEN_AUF_KONTO = "finanz_buchungen_auf_konto.jasper";
	public final static String REPORT_BUCHUNGEN_IN_BUCHUNGSJOURNAL = "finanz_buchungen_in_buchungsjournal.jasper";
	public final static String REPORT_BUCHUNGSJOURNAL = "finanz_buchungsjournal.jasper";
	public final static String REPORT_MAHNLAUF = "finanz_mahnlauf.jasper";
	public final static String REPORT_RA_SCHREIBEN = "finanz_ra_schreiben.jasper";
	public final static String REPORT_MAHNUNG = "finanz_mahnung.jasper";
	public final static String REPORT_MAHNUNG_AUSFUEHRLICH = "finanz_mahnung_ausfuehrlich.jasper";
	public final static String REPORT_SAMMELMAHNUNG = "finanz_sammelmahnung.jasper";
	public final static String REPORT_EXPORTLAUF = "finanz_exportlauf.jasper";
	public final static String REPORT_INTRASTAT = "finanz_intrastat.jasper";
	public final static String REPORT_KONTOBLATT = "finanz_kontoblatt.jasper";
	public final static String REPORT_KASSABUCH = "finanz_kassabuch.jasper";
	public final static String REPORT_UVA = "finanz_uva.jasper";
	public final static String REPORT_OFFENEPOSTEN = "finanz_offene_posten.jasper";
	public final static String REPORT_LIQUIDITAETSVORSCHAU = "finanz_liquiditaetsvorschau.jasper";
	public final static String REPORT_ERFOLGSRECHNUNG = "finanz_ergebnisuebersicht.jasper";
	public final static String REPORT_STEUERKATEGORIEN = "finanz_steuerkategorien.jasper";
	public final static String REPORT_BUCHUNGSBELEG = "finanz_buchungsbeleg.jasper";
	public final static String REPORT_BILANZ = "finanz_bilanz.jasper";
	public final static String REPORT_USTVERPROBUNG = "finanz_ustverprobung.jasper";
	public final static String REPORT_AENDERUNGEN_KONTO = "finanz_aenderungen_konto.jasper";
	
	public final static String INTRASTAT_VERFAHREN_WARENEINGANG = "40000";
	public final static String INTRASTAT_VERFAHREN_VERSAND = "10000";
	public final static int INTRASTAT_VERKEHRSZWEIG = 3;
	public final static int INTRASTAT_NACHKOMMASTELLEN_MENGE = 3;
	public final static int INTRASTAT_NACHKOMMASTELLEN_PREISE = 2;
	public final static int INTRASTAT_NACHKOMMASTELLEN_GEWICHT = 3;

	public JasperPrintLP printAlleKonten(TheClientDto theClientDto, String kontotypCNr, boolean bMitVersteckten)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printBuchungenAufKonto(TheClientDto theClientDto, Integer kontoIId)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printBuchungenInBuchungsjournal(TheClientDto theClientDto,
			Integer buchungsjournalIId) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printBuchungsjournal(TheClientDto theClientDto,
			BuchungsjournalReportParameter params) throws EJBExceptionLP, RemoteException;

//	public JasperPrintLP printBuchungsjournal(TheClientDto theClientDto,
//			Integer buchungsjournalIId, Date dVon, Date dBis, boolean storniert, boolean bDatumsfilterIstBuchungsdatum, String text, String belegnummer, BigDecimal betrag, String kontonummer) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printRASchreiben(TheClientDto theClientDto, Integer mahnungIId)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printRASchreibenFuerRechnung(TheClientDto theClientDto,
			Integer rechnungIId) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printMahnungAusMahnlauf(TheClientDto theClientDto,
			Integer mahnungIId,Boolean bMitLogo) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printMahnungFuerRechnung(TheClientDto theClientDto,
			Integer rechnungIId, Integer mahnstufeIId,Boolean bMitLogo) throws EJBExceptionLP,
			RemoteException;
	public JasperPrintLP printSteuerkategorien(TheClientDto theClientDto);
	public JasperPrintLP printLiquiditaetsvorschau(BigDecimal kontostand, BigDecimal kreditlimit,
			Integer kalenderwochen,boolean bTerminNachZahlungsmoral, boolean bMitPlankosten,ArrayList<LiquititaetsvorschauImportDto> alPlankosten,boolean bMitOffenenAngeboten,boolean bMitOffenenBestellungen,boolean bMitOffenenAuftraegen, TheClientDto theClientDto);
	
	public JasperPrintLP[] printSammelmahnung(Integer mahnlaufIId, Boolean bMitLogo, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printSaldenliste(String mandantCNr, ReportSaldenlisteKriterienDto krit, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printKontoblaetter(PrintKontoblaetterModel kbModel, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException ;
	
	public JasperPrintLP printBuchungsbeleg(Integer buchungIId, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException ;
	
	public JasperPrintLP printKassabuch(PrintKontoblaetterModel kbModel, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException ;

	public JasperPrintLP printMahnlauf(
			ReportJournalKriterienDto reportJournalKriterienDtoI,
			Integer mahnlaufIId, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public JasperPrintLP printExportlauf(Integer exportlaufIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printIntrastatVorschau(String sVerfahren,
			java.sql.Date dVon, java.sql.Date dBis,
			BigDecimal bdTransportkosten, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;
	
	public Map<String, Object> getMahnungsParameterFuerRechnung(TheClientDto theClientDto,
			Integer rechnungIId, Integer mahnstufeIId) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printUva(String mandant, ReportUvaKriterienDto krit, TheClientDto theClient)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printOffenePosten(String kontotypCNr, Integer geschaeftsjahr,
			Integer kontoIId, java.sql.Timestamp tStichtag, TheClientDto theClientDto, boolean sortAlphabethisch);

	public JasperPrintLP printErfolgsrechnung(String mandant,
			ReportErfolgsrechnungKriterienDto kriterien,boolean bBilanz, boolean bDetail, TheClientDto theClient)
			throws EJBExceptionLP, RemoteException;
	
	public JasperPrintLP printUstVerprobung(ReportUvaKriterienDto krit, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printAenderungenKonto(Integer kontoIId,
			TheClientDto theClientDto);
	
}
