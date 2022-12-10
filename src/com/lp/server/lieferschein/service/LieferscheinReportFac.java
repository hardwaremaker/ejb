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
package com.lp.server.lieferschein.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Locale;

import javax.ejb.Remote;
import javax.naming.NamingException;

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
	public final static String REPORT_LIEFERSCHEIN_OFFENE_RUECKGABEN = "ls_lieferschein_offene_rueckgaben.jasper";
	public final static String REPORT_LIEFERSCHEIN_ANGELEGTE = "ls_lieferschein_angelegte.jasper";
	public final static String REPORT_LIEFERSCHEIN_WA_ETIKETT = "ls_lieferschein_wa_etikett.jasper";
	public final static String REPORT_LIEFERSCHEIN_ALLE = "ls_lieferschein_alle.jasper";
	public final static String REPORT_LIEFERSCHEIN_VERSANDETIKETTEN = "ls_versandetiketten.jasper";
	public final static String REPORT_LIEFERSCHEIN_PACKSTUECKE = "ls_packstuecke.jasper";
	public final static String REPORT_LIEFERSCHEIN_PLC_VERSANDETIKETTEN = "ls_plc_versandetiketten.jasper";
	public final static String REPORT_LIEFERSCHEIN_EMAIL = "ls_lieferschein2_email";
	public final static String REPORT_LIEFERSCHEIN_UNTERSCHRIFT = "ls_lieferschein2_unterschrift.jasper";
	public final static String REPORT_LIEFERSCHEIN_SIGNATURE = "ls_lieferschein2_unterschrift.png";

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
	public final static int REPORT_LIEFERSCHEIN_VERSION = 77 ;
	public final static int REPORT_LIEFERSCHEIN_ZWSPOSPREISDRUCKEN = 78 ;
	public final static int REPORT_LIEFERSCHEIN_LIEFERSCHEIN_NUMMER = 79;
	public final static int REPORT_LIEFERSCHEIN_LIEFERSCHEIN_BELEGDATUM = 80;
	public final static int REPORT_LIEFERSCHEIN_LIEFERSCHEIN_PROJEKT = 81;
	public final static int REPORT_LIEFERSCHEIN_LIEFERSCHEIN_BESTELLNUMMER = 82;
	public final static int REPORT_LIEFERSCHEIN_LIEFERSCHEIN_KOMMISSION = 83;
	public final static int REPORT_LIEFERSCHEIN_FORECAST_NR = 84;
	public final static int REPORT_LIEFERSCHEIN_FORECAST_PROJEKT = 85;
	public final static int REPORT_LIEFERSCHEIN_FORECAST_BEMERKUNG = 86;
	public final static int REPORT_LIEFERSCHEIN_FORECAST_BESTELLNUMMER = 87;
	public final static int REPORT_LIEFERSCHEIN_VERPACKUNGSMITTEL_KENNUNG = 88;
	public final static int REPORT_LIEFERSCHEIN_VERPACKUNGSMITTEL_BEZEICHNUNG = 89;
	public final static int REPORT_LIEFERSCHEIN_VERPACKUNGSMITTEL_GEWICHT_IN_KG = 90;
	public final static int REPORT_LIEFERSCHEIN_VERPACKUNGSMITTELMENGE = 91;
	public final static int REPORT_LIEFERSCHEIN_ARTIKEL_MATERIAL_AUS_KUNDEMATERIAL = 92;
	public final static int REPORT_LIEFERSCHEIN_ARTIKEL_MATERIALBASIS_AUS_KUNDEMATERIAL = 93;
	public final static int REPORT_LIEFERSCHEIN_ARTIKEL_AUFSCHLAG_BETRAG = 94;
	public final static int REPORT_LIEFERSCHEIN_ARTIKEL_AUFSCHLAG_PROZENT = 95;
	public final static int REPORT_LIEFERSCHEIN_UEBERLIEFERT = 96;
	public final static int REPORT_LIEFERSCHEIN_ARTIKEL_ECCN = 97;
	public final static int REPORT_LIEFERSCHEIN_ZWSNETTOSUMMEN = 98;
	public final static int REPORT_LIEFERSCHEIN_ZWSTEXTE = 99;
	public final static int REPORT_LIEFERSCHEIN_AUFTRAG_AENDERUNGSAUFTRAG_VERSION = 100;
	public final static int REPORT_LIEFERSCHEIN_ARTIKEL_PRAEFERENZBEGUENSTIGT = 101;
	public final static int REPORT_LIEFERSCHEIN_AUFTRAG_KOMMISSION = 102;
	public final static int REPORT_LIEFERSCHEIN_KUNDEARTIKELBEZEICHNUNG = 103;
	public final static int REPORT_LIEFERSCHEIN_KUNDEARTIKELBEZEICHNUNG_LIEFERADRESSE = 104;
	public final static int REPORT_LIEFERSCHEIN_ANZAHL_ZEILEN = 105;

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
	public final static int REPORT_LIEFERSCHEIN_OFFENE_LIEFERTERMIN_POSITION = 25;
	public final static int REPORT_LIEFERSCHEIN_OFFENE_RUECKGABETERMIN = 26;
	public final static int REPORT_LIEFERSCHEIN_OFFENE_STATUS = 27;
	public final static int REPORT_LIEFERSCHEIN_OFFENE_LIEFERSCHEINART = 28;
	public final static int REPORT_LIEFERSCHEIN_OFFENE_KOMMISSION = 29;
	public final static int REPORT_LIEFERSCHEIN_OFFENE_SETARTIKEL_TYP = 30;
	public final static int REPORT_LIEFERSCHEIN_OFFENE_ANZAHL_ZEILEN = 31;

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
	public final static int REPORT_LIEFERSCHEIN_WA_ETIKETT_ARTIKEL_SUBREPORT_SNRCHNR = 18;
	public final static int REPORT_LIEFERSCHEIN_WA_ETIKETT_VERPACKUNGSMITTEL_KENNUNG = 19;
	public final static int REPORT_LIEFERSCHEIN_WA_ETIKETT_VERPACKUNGSMITTEL_BEZEICHNUNG = 20;
	public final static int REPORT_LIEFERSCHEIN_WA_ETIKETT_VERPACKUNGSMITTEL_GEWICHT_IN_KG = 21;
	public final static int REPORT_LIEFERSCHEIN_WA_ETIKETT_VERPACKUNGSMITTELMENGE = 22;
	public final static int REPORT_LIEFERSCHEIN_WA_ETIKETT_FORECAST_NR = 23;
	public final static int REPORT_LIEFERSCHEIN_WA_ETIKETT_FORECAST_PROJEKT = 24;
	public final static int REPORT_LIEFERSCHEIN_WA_ETIKETT_FORECAST_BEMERKUNG = 25;
	public final static int REPORT_LIEFERSCHEIN_WA_ETIKETT_FORECAST_BESTELLNUMMER = 26;
	public final static int REPORT_LIEFERSCHEIN_WA_ETIKETT_FORECAST_FORECASTPOSITION_I_ID = 27;
	public final static int REPORT_LIEFERSCHEIN_WA_ETIKETT_AUFTRAG_BESTELLNUMMER = 28;
	public final static int REPORT_LIEFERSCHEIN_WA_ETIKETT_AUFTRAG_NUMMER = 29;
	
	public final static int REPORT_LIEFERSCHEIN_WA_ETIKETT_ARTIKEL_BEZ_KUNDE_SPR = 30;
	public final static int REPORT_LIEFERSCHEIN_WA_ETIKETT_ARTIKEL_ZBEZ_KUNDE_SPR = 31;
	public final static int REPORT_LIEFERSCHEIN_WA_ETIKETT_ARTIKEL_ZBEZ2_KUNDE_SPR = 32;
	public final static int REPORT_LIEFERSCHEIN_WA_ETIKETT_ARTIKEL_KBEZ_KUNDE_SPR = 33;
	public final static int REPORT_LIEFERSCHEIN_WA_ETIKETT_ARTIKEL_KUNDENARTIKELBEZEICHNUNG = 34;
	public final static int REPORT_LIEFERSCHEIN_WA_ETIKETT_ANZAHL_SPALTEN = 35;

	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_ANLIEFERMENGE = 0;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_IDENT = 1;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_ARTIKEL_BESTELLNUMMER = 2;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_ARTIKEL_CNR = 3;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_ARTIKEL_BEZ = 4;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_ARTIKEL_ZBEZ = 5;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_ARTIKEL_ZBEZ2 = 6;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_EINHEIT = 7;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_ARTIKEL_GEWICHT = 8;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_ARTIKEL_REFERENZNUMMER = 9;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_ARTIKEL_KUNDENARTIKELNUMMER = 10;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_ARTIKEL_TEXTEINGABE = 11;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_ARTIKEL_WE_REFERENZ = 12;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_ARTIKEL_KBEZ = 13;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_ARTIKEL_INDEX = 14;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_ARTIKEL_REVISION = 15;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_PAKETMENGE = 16;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_AKTUELLESPAKET = 17;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_VERPACKUNGSMENGE = 18;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_POSITION = 19;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_VERPACKUNGSMITTEL_KENNUNG = 20;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_VERPACKUNGSMITTEL_BEZEICHNUNG = 21;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_VERPACKUNGSMITTEL_GEWICHT_IN_KG = 22;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_VERPACKUNGSMITTELMENGE = 23;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_BESTELLDATUM = 24;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_PACKSTUECKNUMMER = 25;
	
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_FORECAST_NR = 26;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_FORECAST_PROJEKT = 27;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_FORECAST_BEMERKUNG = 28;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_FORECAST_BESTELLNUMMER = 29;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_FORECAST_FORECASTPOSITION_I_ID = 30;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_WE_REFERENZ_BELEGART = 31;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_WE_REFERENZ_BELEGNUMMER = 32;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_WE_REFERENZ_BELEGDATUM = 33;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_ARTIKEL_KUNDENARTIKELBEZEICHNUNG = 34;
	public final static int REPORT_LIEFERSCHEIN_VERSANDETIKETT_ANZAHL_SPALTEN = 35;
	
	
	public final static int REPORT_PACKSTUECKE_NUMMER = 0;
	public final static int REPORT_PACKSTUECKE_LIEFERSCHEIN = 1;
	public final static int REPORT_PACKSTUECKE_LOS = 2;
	public final static int REPORT_PACKSTUECKE_ARTIKEL = 3;
	public final static int REPORT_PACKSTUECKE_BEZEICHNUNG = 4;
	public final static int REPORT_PACKSTUECKE_ANZAHL_SPALTEN = 5;
	

	public JasperPrintLP[] printLieferschein(Integer iIdLieferscheinI,
			Integer iAnzahlKopienI, Boolean bMitLogo, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Object[][] getLieferscheinReportData(Integer lieferscheinIId,
			Locale locDruckUebersteuert,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printLieferscheinEtikett(Integer iIdLieferscheinI,
			Integer iPaketnummer, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public int getGesamtzahlPakete(Integer iIdLieferscheinI,
			TheClientDto theClientDto);

	public JasperPrintLP printLieferscheinOffene(
			ReportJournalKriterienDto reportJournalKriterienDtoI, Integer iArt,
			boolean bMitDetails,boolean bNurRueckgaben, TheClientDto theClientDto)
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
			BigDecimal bdHandmenge, Integer iExemplare,TheClientDto theClientDto, Double gewichtUebersteuert,
			java.sql.Timestamp tsLieferterminUebersteuert,
			Integer iAnzahlPaketeUebersteuert,
			String versandnummerUebersteuert, String versandnummer2Uebersteuert, boolean versandinfosSpeichern)
			throws EJBExceptionLP, RemoteException;
	
	JasperPrintLP[] printLieferscheinOnServer(
			Integer lieferscheinId, TheClientDto theClientDto) throws RemoteException ;
	
	HelperSubreportLieferscheinData createSubreportOffeneABPositionen(
			Integer auftragId, Integer statusTeillieferung, LieferscheinDto lieferscheinDto, TheClientDto theClientDto) throws NamingException, RemoteException ;
	
	public JasperPrintLP printPackstuecke(String filter, TheClientDto theClientDto);
	
	public HashSet<Integer> getGesamtAnzahlAnAuftraegen(
			LieferscheinDto lieferscheinDto);

	JasperPrintLP printVersandetikettOnServer(Integer lieferscheinIId,
			TheClientDto theClientDto) throws RemoteException;

	JasperPrintLP printLieferscheinWAEtikettOnServer(Integer lieferscheinIId,
			TheClientDto theClientDto) throws RemoteException;
	String getArtikelsetType(LieferscheinpositionDto lsposDto);

	JasperPrintLP printPostVersandetikett(Integer iIdLieferscheinI, byte[] pdf, TheClientDto theClientDto);

	JasperPrintLP[] printLieferschein(
			PrintLieferscheinAttribute attributs, TheClientDto theClientDto);
}

