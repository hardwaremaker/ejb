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
package com.lp.server.angebot.service;

import java.rmi.RemoteException;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;

@Remote
public interface AngebotReportFac {
	public final static String REPORT_MODUL = "angebot";

	public final static String REPORT_ANGEBOT = "angb_angebot.jasper";

	public final static String REPORT_VORKALKULATION = "angb_angebot_vorkalkulation.jasper";
	public final static String REPORT_ANGEBOT_JOURNAL = "angb_angebot_journal.jasper";
	public final static String REPORT_ANGEBOT_JOURNAL_ALLE = "angb_angebot_journal_alle.jasper";
	public final static String REPORT_ANGEBOT_JOURNAL_OFFENE = "angb_angebot_journal_offene.jasper";
	public final static String REPORT_ANGEBOT_JOURNAL_ABGELEHNTE = "angb_angebot_journal_abgelehnte.jasper";
	public final static String REPORT_ANGEBOT_ADRESSETIKETT = "angb_adressetikett.jasper";
	public final static String REPORT_ANGEBOTSSTATISTIK = "angb_angebotsstatistik.jasper";
	public final static String REPORT_ANGEBOTSPOTENTIAL = "angb_angebotspotential.jasper";

	// reportflr: 3 Die Indizes der Spalten in der Ergebnisliste
	public final static int REPORT_ANGEBOT_JOURNAL_ANGEBOTIID = 0;
	public final static int REPORT_ANGEBOT_JOURNAL_ANGEBOTCNR = 1;
	public final static int REPORT_ANGEBOT_JOURNAL_KUNDECNAME1 = 2;
	public final static int REPORT_ANGEBOT_JOURNAL_KOSTENSTELLECNR = 3;
	public final static int REPORT_ANGEBOT_JOURNAL_VERTRETERCNAME1 = 4;
	public final static int REPORT_ANGEBOT_JOURNAL_PROJEKTBEZ = 5;
	public final static int REPORT_ANGEBOT_JOURNAL_REALISIERUNGSTERMIN = 6;
	public final static int REPORT_ANGEBOT_JOURNAL_INTERNERKOMMENTAR = 7;
	public final static int REPORT_ANGEBOT_JOURNAL_ARTIKELCNR = 8;
	public final static int REPORT_ANGEBOT_JOURNAL_ARTIKELBEZ = 9;
	public final static int REPORT_ANGEBOT_JOURNAL_ARTIKELMENGE = 10;
	public final static int REPORT_ANGEBOT_JOURNAL_ARTIKELEINHEIT = 11;
	public final static int REPORT_ANGEBOT_JOURNAL_ARTIKELPREIS = 12;
	public final static int REPORT_ANGEBOT_JOURNAL_AUFTRAGWAHRSCHEINLICHKEIT = 13;
	public final static int REPORT_ANGEBOT_JOURNAL_ANGEBOTWERT = 14;
	public final static int REPORT_ANGEBOT_JOURNAL_ANGEBOTERLEDIGUNGSGRUND = 15;
	public final static int REPORT_ANGEBOT_JOURNAL_EXTERNERKOMMENTAR = 16;
	public final static int REPORT_ANGEBOT_JOURNAL_ERLEDIGUNGSGRUND_AB_NR = 17;
	public final static int REPORT_ANGEBOT_JOURNAL_KUNDECNAME2 = 18;
	public final static int REPORT_ANGEBOT_JOURNAL_KUNDECNAME3 = 19;
	public final static int REPORT_ANGEBOT_JOURNAL_KUNDESTRASSE = 20;
	public final static int REPORT_ANGEBOT_JOURNAL_KUNDEPLZ = 21;
	public final static int REPORT_ANGEBOT_JOURNAL_KUNDEORT = 22;
	public final static int REPORT_ANGEBOT_JOURNAL_KUNDELKZ = 23;
	public final static int REPORT_ANGEBOT_JOURNAL_KUNDETELEFON = 24;
	public final static int REPORT_ANGEBOT_JOURNAL_KUNDEFAX = 25;
	public final static int REPORT_ANGEBOT_JOURNAL_KUNDEEMAIL = 26;
	public final static int REPORT_ANGEBOT_JOURNAL_KUNDEHOMEPAGE = 27;

	public final static int REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_VORNAME = 28;
	public final static int REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_NACHNAME = 29;
	public final static int REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_TITEL = 30;
	public final static int REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_ANREDE = 31;
	public final static int REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_TELEFON = 32;
	public final static int REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_TELEFONDW = 33;
	public final static int REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_FAX = 34;
	public final static int REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_FAXDW = 35;
	public final static int REPORT_ANGEBOT_JOURNAL_ANSPRECHPARTNER_EMAIL = 36;

	
	
	

	// reportflr: 3 Die Indizes der Spalten in der Ergebnisliste
	public final static int REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_ANGEBOTIID = 0;
	public final static int REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_ANGEBOTCNR = 1;
	public final static int REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_KUNDECNAME1 = 2;
	public final static int REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_KOSTENSTELLECNR = 3;
	public final static int REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_VERTRETERCNAME1 = 4;
	public final static int REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_PROJEKTBEZ = 5;
	public final static int REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_REALISIERUNGSTERMIN = 6;
	public final static int REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_INTERNERKOMMENTAR = 7;
	public final static int REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_GESAMTANGEBOTSWERT = 8;
	public final static int REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_ANGEBOTERLEDIGUNGSGRUND = 9;
	public final static int REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_EXTERNERKOMMENTAR = 10;
	public final static int REPORT_ANGEBOT_JOURNAL_ABGELEHNTE_ERLEDIGUNGSGRUND_AB_NR = 11;
	
	public final static int REPORT_ANGEBOT_OFFENE_ANGEBOTNUMMER = 0;
	public final static int REPORT_ANGEBOT_OFFENE_KUNDE = 1;
	public final static int REPORT_ANGEBOT_OFFENE_KOSTENSTELLE = 2;

	public final static int REPORT_ANGEBOT_POSITION = 0;
	public final static int REPORT_ANGEBOT_IDENT = 1;
	public final static int REPORT_ANGEBOT_MENGE = 2;
	public final static int REPORT_ANGEBOT_EINHEIT = 3;
	public final static int REPORT_ANGEBOT_EINZELPREIS = 4;
	public final static int REPORT_ANGEBOT_RABATT = 5;
	public final static int REPORT_ANGEBOT_ZUSATZRABATTSATZ = 6;
	public final static int REPORT_ANGEBOT_GESAMTPREIS = 7;
	public final static int REPORT_ANGEBOT_MWSTSATZ = 8;
	public final static int REPORT_ANGEBOT_MWSTBETRAG = 9;
	public final static int REPORT_ANGEBOT_POSITIONSART = 10;
	public final static int REPORT_ANGEBOT_FREIERTEXT = 11;
	public final static int REPORT_ANGEBOT_LEERZEILE = 12;
	public final static int REPORT_ANGEBOT_IMAGE = 13;
	public final static int REPORT_ANGEBOT_SEITENUMBRUCH = 14;
	// unterstkl: 2 REPORT_ANGEBOT_STKLPOSITION wird durch 4 neue Konstanten
	// abgeloest
	public final static int REPORT_ANGEBOT_STKLMENGE = 15;
	public final static int REPORT_ANGEBOT_STKLEINHEIT = 16;
	public final static int REPORT_ANGEBOT_STKLARTIKELCNR = 17;
	public final static int REPORT_ANGEBOT_STKLARTIKELBEZ = 18;
	public final static int REPORT_ANGEBOT_B_ALTERNATIVE = 19;
	// endsumme: 2 Eine Textposition kann im Anschluss an die Endsumme
	// angedruckt werden
	public final static int REPORT_ANGEBOT_TEXTNACHENDSUMME = 20;
	public final static int REPORT_ANGEBOT_IDENTNUMMER = 21;
	public final static int REPORT_ANGEBOT_BEZEICHNUNG = 22;
	public final static int REPORT_ANGEBOT_KURZBEZEICHNUNG = 23;
	public final static int REPORT_ANGEBOT_ARTIKELCZBEZ2 = 24;
	public final static int REPORT_ANGEBOT_KUNDEARTIKELNR = 25;
	public final static int REPORT_ANGEBOT_REFERENZNUMMER = 26;
	public final static int REPORT_ANGEBOT_ARTIKELKOMMENTAR = 27;
	public final static int REPORT_ANGEBOT_ZUSATZBEZEICHNUNG = 28;
	public final static int REPORT_ANGEBOT_BAUFORM = 29;
	public final static int REPORT_ANGEBOT_VERPACKUNGSART = 30;
	public final static int REPORT_ANGEBOT_ARTIKEL_MATERIAL = 31;
	public final static int REPORT_ANGEBOT_ARTIKEL_BREITE = 32;
	public final static int REPORT_ANGEBOT_ARTIKEL_HOEHE = 33;
	public final static int REPORT_ANGEBOT_ARTIKEL_TIEFE = 34;
	public final static int REPORT_ANGEBOT_POSITION_NR = 35;
	public final static int REPORT_ANGEBOT_TYP_CNR = 36;
	public final static int REPORT_ANGEBOT_IDENT_TEXTEINGABE = 37;
	public final static int REPORT_ANGEBOT_POSITIONOBJEKT = 38;
	public final static int REPORT_ANGEBOT_ZERTIFIKATART = 39;
	public final static int REPORT_ANGEBOT_SETARTIKEL_TYP = 40;
	public final static int REPORT_ANGEBOT_VERLEIHTAGE = 41;
	public final static int REPORT_ANGEBOT_VERLEIHFAKTOR = 42;
	public final static int REPORT_ANGEBOT_ARTIKEL_INDEX = 43;
	public final static int REPORT_ANGEBOT_ARTIKEL_REVISION = 44;
	public final static int REPORT_ANGEBOT_LVPOSITION = 45;
	public final static int REPORT_ANGEBOT_VONPOSITION = 46 ;
	public final static int REPORT_ANGEBOT_BISPOSITION = 47 ;
	public final static int REPORT_ANGEBOT_ZWSNETTOSUMME = 48 ;
	public final static int REPORT_ANGEBOT_ZWSTEXT = 49 ;
	public final static int REPORT_ANGEBOT_INTERNAL_IID = 50 ;
	public final static int REPORT_ANGEBOT_STKLARTIKELKBEZ = 51;
	public final static int REPORT_ANGEBOT_STKLARTIKEL_KDARTIKELNR = 52;
	public final static int REPORT_ANGEBOT_STKLARTIKEL_KDPREIS = 53;
	public final static int REPORT_ANGEBOT_ARTIKEL_WERBEABGABEPFLICHTIG  = 54;
	public final static int REPORT_ANGEBOT_MATERIALZUSCHLAG = 55;
	public final static int REPORT_ANGEBOT_ARTIKEL_MATERIALGEWICHT = 56;
	public final static int REPORT_ANGEBOT_ARTIKEL_KURS_MATERIALZUSCHLAG = 57;
	public final static int REPORT_ANGEBOT_ARTIKEL_DATUM_MATERIALZUSCHLAG = 58;
	public final static int REPORT_ANGEBOT_ANZAHL_SPALTEN = 59 ;

	public final static int REPORT_VORKALKULATION_IDENT = 0;
	/** Die Bezeichnung kann auch die im AG uebersteuerte Bezeichnung sein */
	public final static int REPORT_VORKALKULATION_BEZEICHNUNG = 1;
	public final static int REPORT_VORKALKULATION_ZUSATZBEZEICHNUNG = 2;
	public final static int REPORT_VORKALKULATION_MENGE = 3;
	public final static int REPORT_VORKALKULATION_EINHEIT = 4;
	/**
	 * Fuer die Zwischensumme muss die Menge der uebergeordneten Position
	 * bekannt sein.
	 */
	public final static int REPORT_VORKALKULATION_MENGE_UEBERGEORDNET = 5;
	/**
	 * Fuer die Zwischensumme muss die Einheit der uebergeordneten Position
	 * bekannt sein.
	 */
	public final static int REPORT_VORKALKULATION_EINHEIT_UEBERGEORDNET = 6;
	/**
	 * Jede Ident Position hat einen manuell eingegebenen Gestehungspreis, der
	 * kursiv angedruckt wird
	 */
	public final static int REPORT_VORKALKULATION_GESTEHUNGSPREIS_MANUELL = 7;
	/**
	 * Der Gestehungspreis eines Artikels, der aus dem Hauptlager des Mandanten
	 * kommt
	 */
	public final static int REPORT_VORKALKULATION_GESTEHUNGSPREIS = 8;
	/**
	 * Jede Ident Position hat einen manuell bestimmten Gestehungswert, der
	 * kursiv angedruckt wird
	 */
	public final static int REPORT_VORKALKULATION_GESTEHUNGSWERT_MANUELL = 9;
	/**
	 * Der Gestehungswert eines Artikels, der aus dem Hauptlager des Mandanten
	 * bestimmt wird
	 */
	public final static int REPORT_VORKALKULATION_GESTEHUNGSWERT = 10;
	public final static int REPORT_VORKALKULATION_VERKAUFSPREIS = 11;
	public final static int REPORT_VORKALKULATION_VERKAUFSWERT = 12;
	public final static int REPORT_VORKALKULATION_POSITIONSART = 13;
	/** Dieser Index dient der Gruppierung zur Bildung von Zwischensummen */
	public final static int REPORT_VORKALKULATION_INDEX_GRUPPE = 14;
	public final static int REPORT_VORKALKULATION_FIKTIVER_LAGERSTAND = 15;
	public final static int REPORT_VORKALKULATION_LIEF1PREIS = 16;
	public final static int REPORT_VORKALKULATION_LIEF1PREISGUELTIGBIS = 17;
	public final static int REPORT_VORKALKULATION_LIEF1PREISSTAFFEL = 18;
	public final static int REPORT_VORKALKULATION_LIEFERANT = 19;
	public final static int REPORT_VORKALKULATION_TPKOSTENLIEFERUNG = 20;
	public final static int REPORT_VORKALKULATION_POSITIONSNUMMER = 21;
	public final static int REPORT_VORKALKULATION_MATERIALZUSCHLAG = 22;
	public final static int REPORT_VORKALKULATION_MATERIAL = 23;
	public final static int REPORT_VORKALKULATION_SETARTIKEL_TYP = 24;
	public final static int REPORT_VORKALKULATION_ANZAHL_SPALTEN = 25;
	/** WH 22.02.06 vorerst Punkt, damit man die Ebene erkennen kann */
	public final static String REPORT_VORKALKULATION_ZEICHEN_FUER_HANDEINGABE = ".";

	public final static int REPORT_ADRESSETIKETT_NAME = 0;
	public final static int REPORT_ADRESSETIKETT_ZHD_NAME = 1;
	public final static int REPORT_ADRESSETIKETT_STRASSE = 2;
	public final static int REPORT_ADRESSETIKETT_PLZORT = 3;
	public final static int REPORT_ADRESSETIKETT_LAND = 4;
	public final static int REPORT_ADRESSETIKETT_BILD = 5;
	public final static int REPORT_ADRESSETIKETT_ABSENDER_NAME = 6;
	public final static int REPORT_ADRESSETIKETT_ABSENDER_STRASSE = 7;
	public final static int REPORT_ADRESSETIKETT_ABSENDER_PLZORT = 8;

	public final static int REPORT_ANGEBOTSSTATISTIK_ANGEBOTENEMENGE = 0;
	public final static int REPORT_ANGEBOTSSTATISTIK_ANGEBOTENERPREIS = 1;
	public final static int REPORT_ANGEBOTSSTATISTIK_BELEGDATUM = 2;
	public final static int REPORT_ANGEBOTSSTATISTIK_CNR = 3;
	public final static int REPORT_ANGEBOTSSTATISTIK_KUNDE = 4;
	public final static int REPORT_ANGEBOTSSTATISTIK_MATERIALZUSCHLAG = 5;

	public final static int REPORT_ANGEBOTSPOTENTIAL_KUNDE = 0;
	public final static int REPORT_ANGEBOTSPOTENTIAL_KUNDE_KURZBEZEICHNUNG = 1;
	public final static int REPORT_ANGEBOTSPOTENTIAL_BELEGNUMMER = 2;
	public final static int REPORT_ANGEBOTSPOTENTIAL_PROJEKT = 3;
	public final static int REPORT_ANGEBOTSPOTENTIAL_ARTIKELNUMMER = 4;
	public final static int REPORT_ANGEBOTSPOTENTIAL_ARTIKELBEZEICHNUNG = 5;
	public final static int REPORT_ANGEBOTSPOTENTIAL_MENGE = 6;
	public final static int REPORT_ANGEBOTSPOTENTIAL_LIEFERANT = 7;
	public final static int REPORT_ANGEBOTSPOTENTIAL_BELEGDATUM = 8;
	public final static int REPORT_ANGEBOTSPOTENTIAL_GUELTIGBIS = 9;
	public final static int REPORT_ANGEBOTSPOTENTIAL_EKPREIS = 10;
	
	public JasperPrintLP[] printAngebot(Integer iIdAngebotI,
			Integer iAnzahlKopienI, Boolean bMitLogo, String sReportname, String sDrucktype,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP[] printAngebot(Integer iIdAngebotI,
			Integer iAnzahlKopienI, Boolean bMitLogo, String sReportname,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printAngebotAlle(
			ReportAngebotJournalKriterienDto kritDtoI,
			String erledigungsgrundCNr, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public JasperPrintLP printAngebotOffene(
			ReportAngebotJournalKriterienDto kritDtoI, Boolean bKommentare,Boolean bDetails,
			Boolean bKundenstammdaten ,TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printAngebotAbgelehnte(
			ReportAngebotJournalKriterienDto kritDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printAngebotVorkalkulation(Integer iIdAngebotI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printAdressetikett(Integer partnerIId,
			Integer ansprechpartnerIId, TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printAngebotsstatistik(
			ReportAngebotsstatistikKriterienDto reportAngebotsstatistikKriterienDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;
	
	public JasperPrintLP printAngebotspotential(TheClientDto theClientDto);
	

}
