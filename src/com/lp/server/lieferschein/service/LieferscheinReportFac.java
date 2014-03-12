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
package com.lp.server.lieferschein.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;

import javax.ejb.Remote;

import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;

@Remote
public interface LieferscheinReportFac {
	public final static String REPORT_MODUL = "lieferschein";

	public final static String REPORT_LIEFERSCHEIN = "ls_lieferschein2.jasper";
	public final static String REPORT_LIEFERSCHEIN_ETIKETT = "ls_lieferschein_etikett.jasper";
	public final static String REPORT_LIEFERSCHEIN_OFFENE = "ls_lieferschein_offene.jasper";
	public final static String REPORT_LIEFERSCHEIN_ANGELEGTE = "ls_lieferschein_angelegte.jasper";
	public final static String REPORT_LIEFERSCHEIN_WA_ETIKETT = "ls_lieferschein_wa_etikett.jasper";
	public final static String REPORT_LIEFERSCHEIN_ALLE = "ls_lieferschein_alle.jasper";
	public final static String REPORT_LIEFERSCHEIN_VERSANDETIKETTEN = "ls_versandetiketten.jasper";

	// Spalten der Datenmatrix
	public final static int REPORT_LIEFERSCHEIN_ADRESSETIKETT = 0;

	public final static int REPORT_LIEFERSCHEIN_POSITION = 0;
	public final static int REPORT_LIEFERSCHEIN_IDENT = 1;
	public final static int REPORT_LIEFERSCHEIN_MENGE = 2;
	public final static int REPORT_LIEFERSCHEIN_EINHEIT = 3;
	public final static int REPORT_LIEFERSCHEIN_BESTELLT = 4;
	public final static int REPORT_LIEFERSCHEIN_LIEFERREST = 5;
	public final static int REPORT_LIEFERSCHEIN_POSITIONSART = 6;
	public final static int REPORT_LIEFERSCHEIN_FREIERTEXT = 7;
	public final static int REPORT_LIEFERSCHEIN_LEERZEILE = 8;
	public final static int REPORT_LIEFERSCHEIN_IMAGE = 9;
	public final static int REPORT_LIEFERSCHEIN_SEITENUMBRUCH = 10;
	public final static int REPORT_LIEFERSCHEIN_STKLMENGE = 11;
	public final static int REPORT_LIEFERSCHEIN_STKLEINHEIT = 12;
	public final static int REPORT_LIEFERSCHEIN_STKLARTIKELCNR = 13;
	public final static int REPORT_LIEFERSCHEIN_STKLARTIKELBEZ = 14;
	public final static int REPORT_LIEFERSCHEIN_IDENTNUMMER = 15;
	public final static int REPORT_LIEFERSCHEIN_BEZEICHNUNG = 16;
	public final static int REPORT_LIEFERSCHEIN_KURZBEZEICHNUNG = 17;
	public final static int REPORT_LIEFERSCHEIN_AUFTRAG_NUMMER = 18;
	public final static int REPORT_LIEFERSCHEIN_AUFTRAG_PROJEKT = 19;
	public final static int REPORT_LIEFERSCHEIN_AUFTRAG_BESTELLNUMMER = 20;
	public final static int REPORT_LIEFERSCHEIN_AUFTRAG_BESTELLDATUM = 21;
	public final static int REPORT_LIEFERSCHEIN_WARENVERKEHRSNUMMER = 22;
	public final static int REPORT_LIEFERSCHEIN_SERIENCHARGENR = 23;
	public final static int REPORT_LIEFERSCHEIN_SERIENCHARGENR_MENGE = 24;
	public final static int REPORT_LIEFERSCHEIN_MINDESTHALTBARKEIT = 25;
	public final static int REPORT_LIEFERSCHEIN_KUNDEARTIKELNR = 26;
	public final static int REPORT_LIEFERSCHEIN_ARTIKEL_VERPACKUNGSMENGE = 27;
	public final static int REPORT_LIEFERSCHEIN_ARTIKEL_VERPACKUNGSEANNR = 28;
	public final static int REPORT_LIEFERSCHEIN_ARTIKEL_VERKAUFSEANNR = 29;
	public final static int REPORT_LIEFERSCHEIN_ARTIKEL_GEWICHT = 30;
	public final static int REPORT_LIEFERSCHEIN_ARTIKEL_VERPACKUNGSART = 31;
	public final static int REPORT_LIEFERSCHEIN_REFERENZNUMMER = 32;
	public final static int REPORT_LIEFERSCHEIN_ARTIKELCZBEZ2 = 33;
	public final static int REPORT_LIEFERSCHEIN_ARTIKELKOMMENTAR = 34;
	public final static int REPORT_LIEFERSCHEIN_ZUSATZBEZEICHNUNG = 35;
	public final static int REPORT_LIEFERSCHEIN_BAUFORM = 36;
	public final static int REPORT_LIEFERSCHEIN_VERPACKUNGSART = 37;
	public final static int REPORT_LIEFERSCHEIN_ARTIKEL_MATERIAL = 38;
	public final static int REPORT_LIEFERSCHEIN_ARTIKEL_BREITE = 39;
	public final static int REPORT_LIEFERSCHEIN_ARTIKEL_HOEHE = 40;
	public final static int REPORT_LIEFERSCHEIN_ARTIKEL_TIEFE = 41;
	public final static int REPORT_LIEFERSCHEIN_ARTIKEL_URSPRUNGSLAND = 42;
	public final static int REPORT_LIEFERSCHEIN_ARTIKEL_ARTIKELGRUPPE = 43;
	public final static int REPORT_LIEFERSCHEIN_POSITION_NR = 44;
	public final static int REPORT_LIEFERSCHEIN_TYP_CNR = 45;
	public final static int REPORT_LIEFERSCHEIN_IDENT_TEXTEINGABE = 46;
	public final static int REPORT_LIEFERSCHEIN_SETARTIKEL_TYP = 47;
	public final static int REPORT_LIEFERSCHEIN_WE_REFERENZ = 48;
	public final static int REPORT_LIEFERSCHEIN_FIBU_MWST_CODE = 49;
	public final static int REPORT_LIEFERSCHEIN_DOKUMENTENPFLICHTIG = 50;
	public final static int REPORT_LIEFERSCHEIN_ARTIKEL_INDEX = 51;
	public final static int REPORT_LIEFERSCHEIN_ARTIKEL_REVISION = 52;
	public final static int REPORT_LIEFERSCHEIN_VERLEIHTAGE = 53;
	public final static int REPORT_LIEFERSCHEIN_VERLEIHFAKTOR = 54;
	public final static int REPORT_LIEFERSCHEIN_POSITIONSOBJEKT = 55;
	public final static int REPORT_LIEFERSCHEIN_KEIN_LIEFERREST = 56;
	public final static int REPORT_LIEFERSCHEIN_LS_POSITION_NR = 57;
	public final static int REPORT_LIEFERSCHEIN_VONPOSITION = 58;
	public final static int REPORT_LIEFERSCHEIN_BISPOSITION = 59;
	public final static int REPORT_LIEFERSCHEIN_ZWSNETTOSUMME = 60;
	public final static int REPORT_LIEFERSCHEIN_ZWSTEXT = 61;
	public final static int REPORT_LIEFERSCHEIN_RABATT = 62;
	public final static int REPORT_LIEFERSCHEIN_INTERNAL_IID = 63;
	public final static int REPORT_LIEFERSCHEIN_LV_POSITION = 64;
	public final static int REPORT_LIEFERSCHEIN_STKLARTIKELKBEZ = 65;
	public final static int REPORT_LIEFERSCHEIN_STKLARTIKEL_KDARTIKELNR = 66;
	public final static int REPORT_LIEFERSCHEIN_ARTIKEL_WERBEABGABEPFLICHTIG = 67;
	public final static int REPORT_LIEFERSCHEIN_SERIENNUMMERN_DER_SETPOSITIONEN = 68;
	public final static int REPORT_LIEFERSCHEIN_STKLARTIKEL_KDPREIS = 69;
	public final static int REPORT_LIEFERSCHEIN_KUNDEARTIKELNR_LIEFERADRESSE = 70;
	public final static int REPORT_LIEFERSCHEIN_ARTIKEL_MATERIALGEWICHT = 71;
	public final static int REPORT_LIEFERSCHEIN_ARTIKEL_KURS_MATERIALZUSCHLAG = 72;
	public final static int REPORT_LIEFERSCHEIN_ARTIKEL_DATUM_MATERIALZUSCHLAG = 73;
	public final static int REPORT_LIEFERSCHEIN_AUFTRAG_BESTAETIGUNG_TERMIN = 74 ;
	public final static int REPORT_LIEFERSCHEIN_AUFTRAG_VERSANDWEG = 75 ;
	public final static int REPORT_LIEFERSCHEIN_LAGER_UEBERSTEUERT = 76 ;
	public final static int REPORT_LIEFERSCHEIN_ANZAHL_ZEILEN = 77 ;

	public final static int REPORT_LIEFERSCHEIN_OFFENE_LIEFERSCHEINCNR = 0;
	public final static int REPORT_LIEFERSCHEIN_OFFENE_LIEFERSCHEINKUNDE = 1;
	public final static int REPORT_LIEFERSCHEIN_OFFENE_KOSTENSTELLECNR = 2;
	public final static int REPORT_LIEFERSCHEIN_OFFENE_ANLAGEDATUM = 3;
	public final static int REPORT_LIEFERSCHEIN_OFFENE_LIEFERTERMIN = 4;
	public final static int REPORT_LIEFERSCHEIN_OFFENE_ZAHLUNGSZIEL = 5;
	public final static int REPORT_LIEFERSCHEIN_OFFENE_PROJEKTBEZEICHNUNG = 6;
	public final static int REPORT_LIEFERSCHEIN_OFFENE_ARTIKELCNR = 7;
	public final static int REPORT_LIEFERSCHEIN_OFFENE_ARTIKELBEZEICHNUNG = 8;
	public final static int REPORT_LIEFERSCHEIN_OFFENE_ARTIKELMENGE = 9;
	public final static int REPORT_LIEFERSCHEIN_OFFENE_ARTIKELEINHEIT = 10;
	public final static int REPORT_LIEFERSCHEIN_OFFENE_ARTIKELNETTOGESAMTPREISPLUSVERSTECKTERAUFSCHLAGMINUSRABATT = 11;
	public final static int REPORT_LIEFERSCHEIN_OFFENE_ARTIKELGESTEHUNGSPREIS = 12;
	public final static int REPORT_LIEFERSCHEIN_OFFENE_ARTIKELOFFENERWERT = 13;
	public final static int REPORT_LIEFERSCHEIN_OFFENE_ARTIKELOFFENERDB = 14;
	public final static int REPORT_LIEFERSCHEIN_OFFENE_AUFTRAGSNUMMER = 15;
	public final static int REPORT_LIEFERSCHEIN_OFFENE_AUFTRAG_PROJEKT = 16;
	public final static int REPORT_LIEFERSCHEIN_OFFENE_BESTELLNUMMER = 17;
	public final static int REPORT_LIEFERSCHEIN_OFFENE_LIEFERART = 18;
	public final static int REPORT_LIEFERSCHEIN_OFFENE_LIEFERARTORT = 18;
	public final static int REPORT_LIEFERSCHEIN_OFFENE_ZIELLAGER = 19;
	public final static int REPORT_LIEFERSCHEIN_OFFENE_ABLAGER = 20;
	public final static int REPORT_LIEFERSCHEIN_OFFENE_RECHNUNGSADRESSE = 21;
	public final static int REPORT_LIEFERSCHEIN_OFFENE_VERRECHENBAR = 22;
	public final static int REPORT_LIEFERSCHEIN_OFFENE_SPEDITEUR = 23;
	public static final int REPORT_LIEFERSCHEIN_OFFENE_TEXTEINGABE = 24;
	public final static int REPORT_LIEFERSCHEIN_OFFENE_ANZAHL_ZEILEN = 25;

	public final static int REPORT_LIEFERSCHEIN_ANGELEGTE_NUMMER = 0;
	public final static int REPORT_LIEFERSCHEIN_ANGELEGTE_KUNDE = 1;
	public final static int REPORT_LIEFERSCHEIN_ANGELEGTE_ANLAGEDATUM = 2;
	public final static int REPORT_LIEFERSCHEIN_ANGELEGTE_ANGELEGTVON = 3;
	public final static int REPORT_LIEFERSCHEIN_ANGELEGTE_AUFTRAGSNUMMER = 4;

	public final static int REPORT_LIEFERSCHEIN_WA_ETIKETT_POSITION = 0;
	public final static int REPORT_LIEFERSCHEIN_WA_ETIKETT_MENGE = 1;
	public final static int REPORT_LIEFERSCHEIN_WA_ETIKETT_IDENT = 2;
	public final static int REPORT_LIEFERSCHEIN_WA_ETIKETT_SERIENCHARGENR = 3;
	public final static int REPORT_LIEFERSCHEIN_WA_ETIKETT_ARTIKEL_BESTELLNUMMER = 4;
	public final static int REPORT_LIEFERSCHEIN_WA_ETIKETT_ARTIKEL_CNR = 5;
	public final static int REPORT_LIEFERSCHEIN_WA_ETIKETT_ARTIKEL_BEZ = 6;
	public final static int REPORT_LIEFERSCHEIN_WA_ETIKETT_ARTIKEL_ZBEZ = 7;
	public final static int REPORT_LIEFERSCHEIN_WA_ETIKETT_ARTIKEL_ZBEZ2 = 8;
	public final static int REPORT_LIEFERSCHEIN_WA_ETIKETT_EINHEIT = 9;
	public final static int REPORT_LIEFERSCHEIN_WA_ETIKETT_ARTIKEL_GEWICHT = 10;
	public final static int REPORT_LIEFERSCHEIN_WA_ETIKETT_ARTIKEL_REFERENZNUMMER = 11;
	public final static int REPORT_LIEFERSCHEIN_WA_ETIKETT_ARTIKEL_KUNDENARTIKELNUMMER = 12;
	public final static int REPORT_LIEFERSCHEIN_WA_ETIKETT_ARTIKEL_TEXTEINGABE = 13;
	public final static int REPORT_LIEFERSCHEIN_WA_ETIKETT_ARTIKEL_WE_REFERENZ = 14;
	public final static int REPORT_LIEFERSCHEIN_WA_ETIKETT_ARTIKEL_KBEZ = 15;
	public final static int REPORT_LIEFERSCHEIN_WA_ETIKETT_ARTIKEL_INDEX = 16;
	public final static int REPORT_LIEFERSCHEIN_WA_ETIKETT_ARTIKEL_REVISION = 17;
	public final static int REPORT_LIEFERSCHEIN_WA_ETIKETT_ANZAHL_SPALTEN = 18;

	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_ANLIEFERMENGE = 0;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_IDENT = 1;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_SERIENCHARGENR = 2;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_ARTIKEL_BESTELLNUMMER = 3;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_ARTIKEL_CNR = 4;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_ARTIKEL_BEZ = 5;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_ARTIKEL_ZBEZ = 6;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_ARTIKEL_ZBEZ2 = 7;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_EINHEIT = 8;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_ARTIKEL_GEWICHT = 9;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_ARTIKEL_REFERENZNUMMER = 10;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_ARTIKEL_KUNDENARTIKELNUMMER = 11;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_ARTIKEL_TEXTEINGABE = 12;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_ARTIKEL_WE_REFERENZ = 13;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_ARTIKEL_KBEZ = 14;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_ARTIKEL_INDEX = 15;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_ARTIKEL_REVISION = 16;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_PAKETMENGE = 17;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_AKTUELLESPAKET = 18;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_VERPACKUNGSMENGE = 19;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_POSITION = 20;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_ANZAHL_SPALTEN = 21;

	public JasperPrintLP[] printLieferschein(Integer iIdLieferscheinI,
			Integer iAnzahlKopienI, Boolean bMitLogo, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Object[][] getLieferscheinReportData(Integer lieferscheinIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printLieferscheinEtikett(Integer iIdLieferscheinI,
			Integer iPaketnummer, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public int getGesamtzahlPakete(Integer iIdLieferscheinI,
			TheClientDto theClientDto);

	public JasperPrintLP printLieferscheinOffene(
			ReportJournalKriterienDto reportJournalKriterienDtoI, Integer iArt,
			boolean bMitDetails, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printLieferscheinAlle(
			ReportLieferscheinJournalKriterienDto krit,
			TheClientDto theClientDto);

	public JasperPrintLP printVersandetikett(Integer iIdLieferscheinI,
			TheClientDto theClientDto);

	public JasperPrintLP printLieferscheinAngelegte(
			ReportJournalKriterienDto reportJournalKriterienDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printLieferscheinWAEtikett(Integer iIdLieferscheinI,
			Integer iPaketnummer, Integer iIdLieferscheinPositionI,
			BigDecimal bdHandmenge, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;
}
