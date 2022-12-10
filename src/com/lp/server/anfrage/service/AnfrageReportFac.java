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
package com.lp.server.anfrage.service;

import java.rmi.RemoteException;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;

@Remote
public interface AnfrageReportFac {
	// modul
	public final static String REPORT_MODUL = "anfrage";

	// reports
	public final static String REPORT_ANFRAGE = "anf_anfrage.jasper";
	public final static String REPORT_ANFRAGESTATISTIK = "anf_anfragestatistik.jasper";
	public final static String REPORT_ANFRAGE_LIEFERDATENUEBERSICHT = "anf_lieferdatenuebersicht.jasper";
	public final static String REPORT_BIETERUEBERSICHT = "anf_bieteruebersicht.jasper";

	public static int REPORT_ANFRAGE_POSITION = 0;
	public static int REPORT_ANFRAGE_IDENT = 1;
	public static int REPORT_ANFRAGE_MENGE = 2;
	public static int REPORT_ANFRAGE_EINHEIT = 3;
	public static int REPORT_ANFRAGE_RICHTPREIS = 4;
	public static int REPORT_ANFRAGE_POSITIONSART = 5;
	public static int REPORT_ANFRAGE_FREIERTEXT = 6;
	public static int REPORT_ANFRAGE_LEERZEILE = 7;
	public static int REPORT_ANFRAGE_IMAGE = 8;
	public static int REPORT_ANFRAGE_SEITENUMBRUCH = 9;
	public final static int REPORT_ANFRAGE_IDENTNUMMER = 10;
	public final static int REPORT_ANFRAGE_BEZEICHNUNG = 11;
	public final static int REPORT_ANFRAGE_KURZBEZEICHNUNG = 12;
	public static int REPORT_ANFRAGE_ARTIKELCZBEZ2 = 13;
	public final static int REPORT_ANFRAGE_REFERENZNUMMER = 14;
	public final static int REPORT_ANFRAGE_ARTIKELKOMMENTAR = 15;
	public final static int REPORT_ANFRAGE_ZUSATZBEZEICHNUNG = 16;
	public final static int REPORT_ANFRAGE_BAUFORM = 17;
	public final static int REPORT_ANFRAGE_VERPACKUNGSART = 18;
	public final static int REPORT_ANFRAGE_ARTIKEL_MATERIAL = 19;
	public final static int REPORT_ANFRAGE_ARTIKEL_BREITE = 20;
	public final static int REPORT_ANFRAGE_ARTIKEL_HOEHE = 21;
	public final static int REPORT_ANFRAGE_ARTIKEL_TIEFE = 22;
	public final static int REPORT_ANFRAGE_ARTIKELNRLIEFERANT = 23;
	public final static int REPORT_ANFRAGE_ARTIKELGRUPPE = 24;
	public final static int REPORT_ANFRAGE_ARTIKELKLASSE = 25;
	public final static int REPORT_ANFRAGE_IDENT_TEXTEINGABE = 26;
	public final static int REPORT_ANFRAGE_POSITIIONOBJEKT = 27;
	public final static int REPORT_ANFRAGE_LIEFERANT_ARTIKEL_IDENTNUMMER = 28;
	public final static int REPORT_ANFRAGE_LIEFERANT_ARTIKEL_BEZEICHNUNG = 29;
	public final static int REPORT_ANFRAGE_ARTIKEL_HERSTELLER = 30;
	public final static int REPORT_ANFRAGE_ARTIKEL_HERSTELLER_NAME = 31;
	public final static int REPORT_ANFRAGE_ARTIKEL_MATERIALGEWICHT = 32;
	public final static int REPORT_ANFRAGE_ARTIKEL_KURS_MATERIALZUSCHLAG = 33;
	public final static int REPORT_ANFRAGE_ARTIKEL_DATUM_MATERIALZUSCHLAG = 34;
	public final static int REPORT_ANFRAGE_ERSATZARTIKEL = 35;
	public final static int REPORT_ANFRAGE_ARTIKEL_HERSTELLERNUMMER = 36;
	public final static int REPORT_ANFRAGE_ARTIKEL_HERSTELLERBEZEICHNUNG = 37;
	public final static int REPORT_ANFRAGE_ARTIKEL_HERSTELLER_KURZBEZEICHNUNG = 38;
	public final static int REPORT_ANFRAGE_SOLLMATERIAL_KOMMENTAR = 39;
	public final static int REPORT_ANFRAGE_SOLLMATERIAL_LOS_STKLKBEZ = 40;
	public final static int REPORT_ANFRAGE_SOLLMATERIAL_LOS_STKLBEZ = 41;
	public final static int REPORT_ANFRAGE_SOLLMATERIAL_LOSNUMMER = 42;
	public final static int REPORT_ANFRAGE_SOLLMATERIAL_LOS_STKLNUMMER = 43;
	public final static int REPORT_ANFRAGE_SOLLMATERIAL_LOS_KOMMENTAR = 44;
	public final static int REPORT_ANFRAGE_SOLLMATERIAL_LOS_PROJEKT = 45;
	public final static int REPORT_ANFRAGE_SOLLMATERIAL_SUBREPORT_ARBEITSGAENGE = 46;
	public final static int REPORT_ANFRAGE_ARTIKEL_INDEX = 47;
	public final static int REPORT_ANFRAGE_ARTIKEL_REVISION = 48;
	public final static int REPORT_ANFRAGE_ANZAHL_SPALTEN = 49;

	public static int REPORT_ANFRAGESTATISTIK_CNR = 0;
	public static int REPORT_ANFRAGESTATISTIK_KUNDE = 1;
	public static int REPORT_ANFRAGESTATISTIK_BELEGDATUM = 2;
	public static int REPORT_ANFRAGESTATISTIK_ANGEFRAGTEMENGE = 3;
	public static int REPORT_ANFRAGESTATISTIK_ANGEBOTENEMENGE = 4;
	public static int REPORT_ANFRAGESTATISTIK_ANGEBOTENERPREIS = 5;
	public static int REPORT_ANFRAGESTATISTIK_LIEFERZEIT = 6;
	public static int REPORT_ANFRAGESTATISTIK_ARTIKELNUMMER = 7;
	public static int REPORT_ANFRAGESTATISTIK_ARTIKELBEZEICHNUNG = 8;
	public static int REPORT_ANFRAGESTATISTIK_ARTIKELZUSATZBEZEICHNUNG = 9;
	public static int REPORT_ANFRAGESTATISTIK_ERSATZARTIKEL = 10;
	public static int REPORT_ANFRAGESTATISTIK_ANZAHL_SPALTEN = 11;

	public final static int REPORT_LIEFERDATEN_ARTIKELCNR = 0;
	public final static int REPORT_LIEFERDATEN_LIEFERMENGE = 1;
	public final static int REPORT_LIEFERDATEN_EINHEITCNR = 2;
	public final static int REPORT_LIEFERDATEN_ARTIKELCBEZ = 3;
	public final static int REPORT_LIEFERDATEN_ANLIEFERPREIS = 4;
	public final static int REPORT_LIEFERDATEN_ANLIEFERZEIT = 5;
	public final static int REPORT_LIEFERDATEN_LIEFERANTNAME = 6;
	public final static int REPORT_LIEFERDATEN_ANFRAGECNR = 7;
	public final static int REPORT_LIEFERDATEN_ANFRAGECBEZ = 8;
	public final static int REPORT_LIEFERDATEN_LIEFERADRESSENAME = 9;
	public final static int REPORT_LIEFERDATEN_ERSATZARTIKEL = 10;
	public final static int REPORT_LIEFERDATEN_ARTIKELCNR_ORIGINAL = 11;
	public final static int REPORT_LIEFERDATEN_BILLIGSTER_ARTIKEL = 12;
	public final static int REPORT_LIEFERDATEN_SCHNELLSTER_ARTIKEL = 13;
	public final static int REPORT_LIEFERDATEN_LIEF1PREIS = 14;
	public final static int REPORT_LIEFERDATEN_LIEF1GUELTIGAB = 15;
	public final static int REPORT_LIEFERDATEN_LIEF1FIXKOSTEN = 16;
	public final static int REPORT_LIEFERDATEN_LIEF1LIEFERANT = 17;
	public final static int REPORT_LIEFERDATEN_ANZAHL_SPALTEN = 18;

	public final static int REPORT_BIETERUEBERSICHT_POSNR = 0;
	public final static int REPORT_BIETERUEBERSICHT_ARTIKELNUMMER = 1;
	public final static int REPORT_BIETERUEBERSICHT_BEZEICHNUNG = 2;
	public final static int REPORT_BIETERUEBERSICHT_ZUSATZBEZEICHNUNG = 3;
	public final static int REPORT_BIETERUEBERSICHT_ZUSATZBEZEICHNUNG2 = 4;
	public final static int REPORT_BIETERUEBERSICHT_EINHEIT = 5;
	public final static int REPORT_BIETERUEBERSICHT_MENGE = 6;
	public final static int REPORT_BIETERUEBERSICHT_PREIS = 7;
	public final static int REPORT_BIETERUEBERSICHT_SUBREPORT_LIEFERANTEN = 8;
	public final static int REPORT_BIETERUEBERSICHT_BILLIGSTER_PREIS = 9;
	public final static int REPORT_BIETERUEBERSICHT_ANZAHL_SPALTEN = 10;

	public JasperPrintLP[] printAnfrage(Integer iIdAnfrageI, Integer iAnzahlKopienI, Boolean bMitLogo,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printAnfragestatistik(ReportAnfragestatistikKriterienDto reportAnfragestatistikKriterienDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printLieferdatenuebersicht(ReportAnfragelieferdatenuebersichtKriterienDto kritDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printBieteruebersicht(Integer anfrageIId, TheClientDto theClientDto);
}
