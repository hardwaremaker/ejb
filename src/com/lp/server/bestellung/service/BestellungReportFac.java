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
package com.lp.server.bestellung.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import javax.ejb.Remote;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;

@Remote
public interface BestellungReportFac {
	public final static String REPORT_MODUL = "bestellung";

	public final static String REPORT_BESTELLUNGEN_ALLE = "bes_bestellung_alle.jasper";
	public final static String REPORT_BESTELLUNG = "bes_bestellung.jasper";
	public final static String REPORT_BSSAMMELMAHNUNG = "bes_sammelmahnung.jasper";
	public final static String REPORT_BSMAHNUNG = "bes_mahnung.jasper";
	public final static String REPORT_WEP_ETIKETT = "bes_wep_etikett.jasper";
	public final static String REPORT_BESTELLUNG_JOURNAL_OFFENE = "bes_bestellung_offene.jasper";
	public final static String REPORT_BESTELLUNG_JOURNAL_BESTELLVORSCHLAG = "bes_bestellung_bestellvorschlag.jasper";
	public final static String REPORT_BESTELLUNG_JOURNAL_OFFENE_OHNE_DETAILS = "bes_bestellung_offene_ohne_details.jasper";
	public final static String REPORT_BESTELLUNG_JOURNAL_WARENEINGANG = "bes_bestellung_wareneingangsjournal.jasper";
	public final static String REPORT_ABHOLAUFTRAG = "bes_abholauftrag.jasper";
	public final static String REPORT_GEAENDERTE_ARTIKEL = "bes_geaenderte_artikel.jasper";
	public final static String REPORT_RAHMENUEBERSICHT = "bes_rahmenuebersicht.jasper";

	// Konstanten fuer Druck Bestellung
	public static int REPORT_BESTELLUNG_POSITION = 0;
	public static int REPORT_BESTELLUNG_IDENT = 1;
	public static int REPORT_BESTELLUNG_MENGE = 2;
	public static int REPORT_BESTELLUNG_EINHEIT = 3;
	public static int REPORT_BESTELLUNG_EINZELPREIS = 4;
	public static int REPORT_BESTELLUNG_RABATT = 5;
	public static int REPORT_BESTELLUNG_GESAMTPREIS = 6;
	public static int REPORT_BESTELLUNG_POSITIONSART = 7;
	public static int REPORT_BESTELLUNG_FREIERTEXT = 8;
	public static int REPORT_BESTELLUNG_LEERZEILE = 9;
	public static int REPORT_BESTELLUNG_IMAGE = 10;
	public static int REPORT_BESTELLUNG_GEWICHT = 11;
	public static int REPORT_BESTELLUNG_SEITENUMBRUCH = 12;
	public static int REPORT_BESTELLUNG_GEWICHTEINHEIT = 13;
	public static int REPORT_BESTELLUNG_POSITIONTERMIN = 14;
	public final static int REPORT_BESTELLUNG_IDENTNUMMER = 15;
	public final static int REPORT_BESTELLUNG_BEZEICHNUNG = 16;
	public final static int REPORT_BESTELLUNG_KURZBEZEICHNUNG = 17;
	public static int REPORT_BESTELLUNG_PREISPEREINHEIT = 18;
	public static int REPORT_BESTELLUNG_ARTIKELCZBEZ2 = 19;
	public static int REPORT_BESTELLUNG_LIEFERANT_ARTIKEL_IDENTNUMMER = 20;
	public static int REPORT_BESTELLUNG_LIEFERANT_ARTIKEL_BEZEICHNUNG = 21;
	public static int REPORT_BESTELLUNG_ARTIKEL_HERSTELLER = 22;
	public final static int REPORT_BESTELLUNG_REFERENZNUMMER = 23;
	public final static int REPORT_BESTELLUNG_ARTIKELKOMMENTAR = 24;
	public final static int REPORT_BESTELLUNG_ZUSATZBEZEICHNUNG = 25;
	public final static int REPORT_BESTELLUNG_BAUFORM = 26;
	public final static int REPORT_BESTELLUNG_VERPACKUNGSART = 27;
	public final static int REPORT_BESTELLUNG_ARTIKEL_MATERIAL = 28;
	public final static int REPORT_BESTELLUNG_ARTIKEL_BREITE = 29;
	public final static int REPORT_BESTELLUNG_ARTIKEL_HOEHE = 30;
	public final static int REPORT_BESTELLUNG_ARTIKEL_TIEFE = 31;
	public static int REPORT_BESTELLUNG_ARTIKEL_HERSTELLER_NAME = 32;
	public final static int REPORT_BESTELLUNG_POSITIONOBKJEKT = 33;
	public final static int REPORT_BESTELLUNG_OFFENERAHMENMENGE = 34;
	public final static int REPORT_BESTELLUNG_ANGEBOTSNUMMER = 35;
	public final static int REPORT_BESTELLUNG_MATERIALZUSCHLAG = 36;
	public final static int REPORT_BESTELLUNG_SOLLMATERIAL_KOMMENTAR = 37;
	public final static int REPORT_BESTELLUNG_SOLLMATERIAL_LOS_STKLKBEZ = 38;
	public final static int REPORT_BESTELLUNG_SOLLMATERIAL_LOS_STKLBEZ = 39;
	public final static int REPORT_BESTELLUNG_SOLLMATERIAL_LOSNUMMER = 40;
	public final static int REPORT_BESTELLUNG_SOLLMATERIAL_LOS_STKLNUMMER = 41;
	public final static int REPORT_BESTELLUNG_SOLLMATERIAL_LOS_KOMMENTAR = 42;
	public final static int REPORT_BESTELLUNG_SOLLMATERIAL_LOS_PROJEKT = 43;
	public final static int REPORT_BESTELLUNG_SOLLMATERIAL_SUBREPORT_ARBEITSGAENGE = 44;
	public final static int REPORT_BESTELLUNG_SETARTIKEL_TYP = 45;
	public final static int REPORT_BESTELLUNG_INSERAT_DTO = 46;
	public final static int REPORT_BESTELLUNG_ARTIKEL_INDEX = 47;
	public final static int REPORT_BESTELLUNG_ARTIKEL_REVISION = 48;
	public final static int REPORT_BESTELLUNG_INSERAT_KUNDE = 49;
	public static int REPORT_BESTELLUNG_LIEFERANT_VERPACKUNGSEINHEIT = 50;
	public final static int REPORT_BESTELLUNG_ARTIKEL_MATERIALGEWICHT = 51;
	public final static int REPORT_BESTELLUNG_ARTIKEL_KURS_MATERIALZUSCHLAG = 52;
	public final static int REPORT_BESTELLUNG_ARTIKEL_DATUM_MATERIALZUSCHLAG = 53;
	public final static int REPORT_BESTELLUNG_RAHMENMENGE = 54;
	public final static int REPORT_BESTELLUNG_ABGERUFENE_MENGE = 55;
	public final static int REPORT_BESTELLUNG_LETZTER_ABRUF = 56;
	public static int REPORT_BESTELLUNG_ANZAHL_SPALTEN = 57;

	public static int REPORT_BESTELLUNG_OFFENE_OD_BESTELLUNGCNR = 0;
	public static int REPORT_BESTELLUNG_OFFENE_OD_BESTELLUNGLIEFERANT = 1;
	public static int REPORT_BESTELLUNG_OFFENE_OD_BESTELLUNGLIEFERTERMIN = 2;
	public static int REPORT_BESTELLUNG_OFFENE_OD_BESTELLUNGPROJEKTBEZEICHNUNG = 3;
	public static int REPORT_BESTELLUNG_OFFENE_OD_KOSTENSTELLECNR = 4;
	public static int REPORT_BESTELLUNG_OFFENE_OD_WERT_OFFEN = 5;
	public static int REPORT_BESTELLUNG_OFFENE_OD_WERT = 6;
	public static int REPORT_BESTELLUNG_OFFENE_OD_WERT_NETTO = 7;
	public static int REPORT_BESTELLUNG_OFFENE_OD_WERT_OFFEN_NETTO = 8;
	public static int REPORT_BESTELLUNG_OFFENE_OD_BESTELLUNGDATUM = 9;
	public static int REPORT_BESTELLUNG_OFFENE_OD_ANZAHL_SPALTEN = 10;

	public static int REPORT_BESTELLUNG_OFFENE_BESTELLUNGCNR = 0;
	public static int REPORT_BESTELLUNG_OFFENE_BESTELLUNGLIEFERANT = 1;
	public static int REPORT_BESTELLUNG_OFFENE_BESTELLUNGLIEFERTERMIN = 2;
	public static int REPORT_BESTELLUNG_OFFENE_KOSTENSTELLECNR = 3;
	public static int REPORT_BESTELLUNG_OFFENE_WERT_OFFEN = 4;
	public static int REPORT_BESTELLUNG_OFFENE_WERT = 5;
	public static int REPORT_BESTELLUNG_OFFENE_ARTIKELBEZ = 6;
	public static int REPORT_BESTELLUNG_OFFENE_ARTIKELCNR = 7;
	public static int REPORT_BESTELLUNG_OFFENE_ARTIKELEINHEIT = 8;
	public static int REPORT_BESTELLUNG_OFFENE_ARTIKELPREIS = 9;
	public static int REPORT_BESTELLUNG_OFFENE_ARTIKELMENGE = 10;
	public static int REPORT_BESTELLUNG_OFFENE_BESTELLUNGDATUM = 11;
	public static int REPORT_BESTELLUNG_OFFENE_ARTIKELOFFENEMENGE = 12;
	public static int REPORT_BESTELLUNG_OFFENE_ARTIKELOFFENEWERT = 13;
	public static int REPORT_BESTELLUNG_OFFENE_ARTIKELLAGERSTAND = 14;
	public static int REPORT_BESTELLUNG_OFFENE_PROJEKT = 15;
	public static int REPORT_BESTELLUNG_OFFENE_ABTERMIN = 16;
	public static int REPORT_BESTELLUNG_OFFENE_BESTELLUNGARTCNR = 17;
	public static int REPORT_BESTELLUNG_OFFENE_ARTIKELGELIFERTEMENGE = 18;
	public static int REPORT_BESTELLUNG_OFFENE_OFFENELIEFERUNGEN = 19;
	public static int REPORT_BESTELLUNG_OFFENE_SORTIERKRITERIUM = 20;
	public static int REPORT_BESTELLUNG_OFFENE_ABNUMMER = 21;
	public static int REPORT_BESTELLUNG_OFFENE_ABKOMMENTAR = 22;
	public static int REPORT_BESTELLUNG_OFFENE_SETARTIKEL_TYP = 23;
	public static int REPORT_BESTELLUNG_OFFENE_ABURSPRUNGSTERMIN = 24;
	public static int REPORT_BESTELLUNG_OFFENE_ANZAHL_SPALTEN = 25;

	public static int REPORT_BESTELLVORSCHLAG_BELEGART = 0;
	public static int REPORT_BESTELLVORSCHLAG_BESTELLUNGLIEFERANT = 1;
	public static int REPORT_BESTELLVORSCHLAG_BESTELLTERMIN = 2;
	public static int REPORT_BESTELLVORSCHLAG_BELEGFIRMACNR = 3;
	public static int REPORT_BESTELLVORSCHLAG_WERT_OFFEN = 4;
	public static int REPORT_BESTELLVORSCHLAG_WERT = 5;
	public static int REPORT_BESTELLVORSCHLAG_ARTIKELBEZ = 6;
	public static int REPORT_BESTELLVORSCHLAG_ARTIKELCNR = 7;
	public static int REPORT_BESTELLVORSCHLAG_ARTIKELEINHEIT = 8;
	public static int REPORT_BESTELLVORSCHLAG_ARTIKELPREIS = 9;
	public static int REPORT_BESTELLVORSCHLAG_ARTIKELMENGE = 10;
	public static int REPORT_BESTELLVORSCHLAG_BELEGCNR = 11;
	public static int REPORT_BESTELLVORSCHLAG_ARTIKELLAGERMINDESTSTAND = 12;
	public static int REPORT_BESTELLVORSCHLAG_ARTIKELRESERVIERUNG = 13;
	public static int REPORT_BESTELLVORSCHLAG_ARTIKELLAGERSOLL = 14;
	public static int REPORT_BESTELLVORSCHLAG_ARTIKELLAGERSTAND = 15;
	public static int REPORT_BESTELLVORSCHLAG_ARTIKELFEHLMENGE = 16;
	public static int REPORT_BESTELLVORSCHLAG_LIEFERANT_ARTIKEL_IDENTNUMMER = 17;
	public static int REPORT_BESTELLVORSCHLAG_LIEFERANT_ARTIKEL_BEZEICHNUNG = 18;
	public static int REPORT_BESTELLVORSCHLAG_ARTIKEL_MENGE_OFFEN = 19;
	public static int REPORT_BESTELLVORSCHLAG_ARTIKEL_RAHMENMENGE_OFFEN = 20;
	public static int REPORT_BESTELLVORSCHLAG_ARTIKEL_RAHMENDETAILBEDARF = 21;
	public static int REPORT_BESTELLVORSCHLAG_ARTIKEL_RAHMENBESTELLNR = 22;
	public static int REPORT_BESTELLVORSCHLAG_ARTIKEL_OFFENE_BESTELLNR = 23;
	public static int REPORT_BESTELLVORSCHLAG_ARTIKELKBEZ = 24;
	public static int REPORT_BESTELLVORSCHLAG_LIEFERANT_STANDARDMENGE = 25;
	public static int REPORT_BESTELLVORSCHLAG_SPERREN = 26;
	public static int REPORT_BESTELLVORSCHLAG_LIEFERANT_MATERIALZUSCHLAG = 27;
	public static int REPORT_BESTELLVORSCHLAG_ARTIKEL_LAGERBEWIRTSCHAFTET = 28;
	public static int REPORT_BESTELLVORSCHLAG_SUBREPORT_VERWENDUNGSNACHWEIS = 29;
	public static int REPORT_BESTELLVORSCHLAG_ARTIKELZBEZ = 30;
	public static int REPORT_BESTELLVORSCHLAG_ARTIKELZBEZ2 = 31;
	public static int REPORT_BESTELLVORSCHLAG_PROJEKT = 32;
	public static int REPORT_BESTELLVORSCHLAG_MANDANT = 33;
	public static int REPORT_BESTELLVORSCHLAG_ANZAHL_SPALTEN = 34;

	public static int REPORT_WEP_ETIKETT_ANLIEFERMENGE = 0;
	public static int REPORT_WEP_ETIKETT_BESTELLNUMMER = 1;
	public static int REPORT_WEP_ETIKETT_CHARGENNUMMER = 2;
	public static int REPORT_WEP_ETIKETT_SERIENNUMMER = 3;
	public static int REPORT_WEP_ETIKETT_KOMMENTAR = 4;
	public static int REPORT_WEP_ETIKETT_VERPACKUNGSEINHEIT = 5;
	public static int REPORT_WEP_ETIKETT_WARENVERKEHRSNUMMER = 6;
	public static int REPORT_WEP_ETIKETT_GEWICHT = 7;
	public static int REPORT_WEP_ETIKETT_IDENT = 8;
	public static int REPORT_WEP_ETIKETT_BEZ = 9;
	public static int REPORT_WEP_ETIKETT_ZBEZ = 10;
	public static int REPORT_WEP_ETIKETT_ZBEZ2 = 11;
	public static int REPORT_WEP_ETIKETT_LIEFERANTENARTIKELNUMMER = 12;
	public static int REPORT_WEP_ETIKETT_LIEFERANTENARTIKELBEZ = 13;
	public static int REPORT_WEP_ETIKETT_LAGER = 14;
	public static int REPORT_WEP_ETIKETT_LAGERORT = 15;
	public static int REPORT_WEP_ETIKETT_URSPRUNGSLAND = 16;
	public static int REPORT_WEP_ETIKETT_WE_DATUM = 17;
	public static int REPORT_WEP_ETIKETT_PROJEKTBEZ = 18;
	public static int REPORT_WEP_ETIKETT_EINHEIT = 19;
	public static int REPORT_WEP_ETIKETT_LIEFERANTNAME = 20;
	public static int REPORT_WEP_ETIKETT_HERSTELLER = 21;
	public static int REPORT_WEP_ETIKETT_HERSTELLERNAME = 22;
	public static int REPORT_WEP_ETIKETT_HANDMENGE = 23;
	public static int REPORT_WEP_ETIKETT_WE_REFERENZ = 24;
	public static int REPORT_WEP_ETIKETT_SUBREPORT_SNRCHNR = 25;
	public static int REPORT_WEP_ETIKETT_ANZAHL_SPALTEN = 26;

	public static int REPORT_GEAENDERTEARTIKEL_ARTIKELNUMMER_AKTUELL = 0;
	public static int REPORT_GEAENDERTEARTIKEL_ARTIKELBEZEICHNUNG_AKTUELL = 1;
	public static int REPORT_GEAENDERTEARTIKEL_ARTIKELZUSATZBEZEICHNUNG_AKTUELL = 2;
	public static int REPORT_GEAENDERTEARTIKEL_ARTIKELZUSATZBEZEICHNUNG2_AKTUELL = 3;
	public static int REPORT_GEAENDERTEARTIKEL_ARTIKELNUMMER_BESTELLUNG = 4;
	public static int REPORT_GEAENDERTEARTIKEL_ARTIKELBEZEICHNUNG_BESTELLUNG = 5;
	public static int REPORT_GEAENDERTEARTIKEL_ARTIKELZUSATZBEZEICHNUNG_BESTELLUNG = 6;
	public static int REPORT_GEAENDERTEARTIKEL_ARTIKELZUSATZBEZEICHNUNG2_BESTELLUNG = 7;
	public static int REPORT_GEAENDERTEARTIKEL_BESTELLNUMMER = 8;
	public static int REPORT_GEAENDERTEARTIKEL_LIEFERANT = 9;
	public static int REPORT_GEAENDERTEARTIKEL_BESTELLDATUM = 10;
	public static int REPORT_GEAENDERTEARTIKEL_POSITIONSTERMIN = 11;
	public static int REPORT_GEAENDERTEARTIKEL_ABTERMIN = 12;
	public static int REPORT_GEAENDERTEARTIKEL_ABNUMMER = 13;
	public static int REPORT_GEAENDERTEARTIKEL_MENGE = 14;
	public static int REPORT_GEAENDERTEARTIKEL_PREIS = 15;
	public static int REPORT_GEAENDERTEARTIKEL_ANZAHL_SPALTEN = 16;

	public final static String REPORT_VORKALKULATION_ZEICHEN_FUER_HANDEINGABE = ".";

	public JasperPrintLP printBestellungenAlle(ReportJournalKriterienDto krit,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printBSMahnungAusMahnlauf(Integer bsmahnungIId,
			boolean bMitLogo, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public JasperPrintLP printBSMahnungFuerBestellung(
			Integer bestellpositionIId, Integer bestellungIId,
			Integer bsmahnstufeIId, Date dMahndatum, boolean bMitLogo,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP[] printBSSammelMahnung(Integer bsmahnlaufIId,
			boolean bMitLogo, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public JasperPrintLP printWepEtikett(Integer iIdWepI,
			Integer iIdBestellpositionI, Integer iIdLagerI, Integer iExemplare,
			Integer iVerpackungseinheit, Double dGewicht,
			String sWarenverkehrsnummer, String sLagerort,
			String sUrsprungsland, String sKommentar, BigDecimal bdHandmenge,
			Integer wePosIId, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public ArrayList<JasperPrintLP> printBSSammelMahnung(Integer bsmahnlaufIId,
			Integer lieferantIId, TheClientDto theClientDto,
			boolean bNurNichtGemahnte, boolean bMitLogo) throws EJBExceptionLP,
			RemoteException;

	public Object[] getGeaenderteArtikelDaten(TheClientDto theClientDto);

	public JasperPrintLP printBestellungOffene(ReportJournalKriterienDto krit,
			Date dStichtag, Boolean bSortierungNachLiefertermin,
			Integer artikelklasseIId, Integer artikelgruppeIId,
			String artikelCNrVon, String artikelCNrBis,
			String projektCBezeichnung, Integer auftragIId, Integer iArt,
			boolean bNurAngelegte, boolean bNurOffeneMengenAnfuehren, Integer[] projekte,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printBestellVorschlag(ReportJournalKriterienDto krit,
			Boolean bSortierungNachLiefertermin, boolean bAnfragevorschlag, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP[] printBestellung(Integer iIdBestellungI,
			Integer iAnzahlKopienI, Boolean bMitLogo, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printGeaenderteArtikel(TheClientDto theClientDto);

	public JasperPrintLP printBestellungWareneingangsJournal(
			ReportJournalKriterienDto krit, Integer artikelklasseIId,
			Integer artikelgruppeIId, String artikelCNrVon,
			String artikelCNrBis, String projektCBezeichnung,
			Integer auftragIId, boolean bMitWarenverbrauch,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printAbholauftrag(Integer iIdBestellungI,
			TheClientDto theClientDto);

	public ArrayList<JasperPrintLP> getMahnungenFuerAlleLieferanten(
			Integer bsmahnlaufIId, TheClientDto theClientDto,
			boolean bNurNichtGemahnte, boolean bMitLogo) throws EJBExceptionLP,
			RemoteException;

	public void sendMahnlauf(String cKommuniaktionsart,
			BSMahnlaufDto bsMahnlaufDto, Locale absenderLocale,
			TheClientDto theClientDto) throws EJBExceptionLP, Throwable;
	
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printRahmenuebersicht(Integer bestellungIId,
			TheClientDto theClientDto);
	
}
