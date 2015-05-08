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
package com.lp.server.artikel.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.DatumsfilterVonBis;
import com.lp.server.util.report.JasperPrintLP;

@Remote
public interface LagerReportFac {

	public final static String REPORT_MODUL = "artikel";

	public static final String REPORT_WARENBEWEGUNGSJOURNAL = "ww_warenbewegungsjournal.jasper";
	public static final String REPORT_KUNDEUMSATZSTATISTIK = "ww_kundeumsatzstatistik.jasper";
	public static final String REPORT_LIEFERANTUMSATZSTATISTIK = "ww_lieferantumsatzstatistik.jasper";
	public static final String REPORT_KUNDEDBSTATISTIK = "ww_kundedbstatistik.jasper";
	public static final String REPORT_LADENHUETER = "ww_ladenhueter.jasper";
	public static final String REPORT_HITLISTE = "ww_hitliste.jasper";
	public static final String REPORT_UMBUCHUNGSBELEG = "ww_umbuchungsbeleg.jasper";
	public static final String REPORT_LAGERBUCHUNGSBELEG = "ww_lagerbuchungsbeleg.jasper";
	public static final String REPORT_MINDESTHALTBARKEIT = "ww_mindesthaltbarkeit.jasper";
	public static final String REPORT_GESTPREISUEBERMINVK = "ww_gestpreisueberminvk.jasper";
	public static final String REPORT_WARENENTNAHMESTATISTIK = "ww_warenentnahmestatistik.jasper";
	public static final String REPORT_ARTIKLEGRUPPEN = "ww_artikelgruppen.jasper";
	public static final String REPORT_SHOPGRUPPEN = "ww_shopgruppen.jasper";
	public static final String REPORT_INDIREKTE_WARENEINSATZ_STATISTIK = "ww_indirektewareneinsatzstatistik.jasper";
	

	public final static int REPORT_INDIREKTE_WARENEINSATZ_SORTIERUNG_KUNDE_ARTIKEL = 0;
	public final static int REPORT_INDIREKTE_WARENEINSATZ_SORTIERUNG_KUNDE_BELEG = 1;
	
	
	public final static int REPORT_WARENENTNAHMESTATISTIK_SORTIERUNG_ARTIKELNR = 0;
	public final static int REPORT_WARENENTNAHMESTATISTIK_SORTIERUNG_ARTIKELGRUPPE = 1;
	public final static int REPORT_WARENENTNAHMESTATISTIK_SORTIERUNG_ARTIKELKLASSE = 2;
	
	public final static int REPORT_LADENHUETER_SORTIERUNG_ARTIKELNR = 0;
	public final static int REPORT_LADENHUETER_SORTIERUNG_ARTIKELGRUPPE = 1;
	public final static int REPORT_LADENHUETER_SORTIERUNG_ARTIKELKLASSE = 2;
	public final static int REPORT_LADENHUETER_SORTIERUNG_LAGERWERT = 3;
	public final static int REPORT_LADENHUETER_SORTIERUNG_GESTPREIS = 4;
	public final static int REPORT_LADENHUETER_SORTIERUNG_EINSTANDSPREIS = 5;

	public final static int REPORT_HITLISTE_SORTIERUNG_ARTIKELNR = 0;
	public final static int REPORT_HITLISTE_SORTIERUNG_ARTIKELGRUPPE = 1;
	public final static int REPORT_HITLISTE_SORTIERUNG_ARTIKELKLASSE = 2;
	public final static int REPORT_HITLISTE_SORTIERUNG_VKWERT = 3;
	public final static int REPORT_HITLISTE_SORTIERUNG_DBWERT = 4;
	public final static int REPORT_HITLISTE_SORTIERUNG_SHOPGRUPPE = 5;

	public final static String REPORT_LAGERSTANDLISTE = "ww_lagerstandliste.jasper";
	public final static int REPORT_LAGERSTANDSLISTE_SORTIERUNG_ARTIKELNUMMER = 1;
	public final static int REPORT_LAGERSTANDSLISTE_SORTIERUNG_ARTIKELGRUPPE = 2;
	public final static int REPORT_LAGERSTANDSLISTE_SORTIERUNG_ARTIKELKLASSE = 3;
	public final static int REPORT_LAGERSTANDSLISTE_SORTIERUNG_LAGERWERT = 4;
	public final static int REPORT_LAGERSTANDSLISTE_SORTIERUNG_GESTEHUNGSPREIS = 5;
	public final static int REPORT_LAGERSTANDSLISTE_SORTIERUNG_LAGERORT = 6;
	public final static int REPORT_LAGERSTANDSLISTE_SORTIERUNG_ARTIKELBEZEICHNUNG = 7;
	public final static int REPORT_LAGERSTANDSLISTE_SORTIERUNG_ARTIKELKURZBEZEICHNUNG = 8;
	public final static int REPORT_LAGERSTANDSLISTE_SORTIERUNG_SHOPGRUPPE = 9;
	public final static int REPORT_LAGERSTANDSLISTE_SORTIERUNG_REFERENZNUMMER = 10;

	public final static int REPORT_LAGERSTANDSLISTE_ARTIKEL_ALLE = 0;
	public final static int REPORT_LAGERSTANDSLISTE_ARTIKEL_NURSTKL = 1;
	public final static int REPORT_LAGERSTANDSLISTE_ARTIKEL_OHNESTKL = 2;

	public JasperPrintLP printWarenbewegungsjournal(Integer artikelIId, Integer lagerIId,
			Timestamp dVon, Timestamp dBis, TheClientDto theClientDto)
			throws RemoteException;

	public final static int REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_ARTIKELKLASSE = 0;
	public final static int REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_ARTIKELGRUPPE = 1;
	public final static int REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_FERTIGUNGSGRUPPE = 2;
	public final static int REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_JAHR = 3;
	public final static int REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_MONAT = 4;

	public final static int REPORT_KUNDEUMSATZSTATISTIK_OPTION_KUNDENGRUPPIERUNG_KEINE = 0;
	public final static int REPORT_KUNDEUMSATZSTATISTIK_OPTION_KUNDENGRUPPIERUNG_BRANCHE = 1;
	public final static int REPORT_KUNDEUMSATZSTATISTIK_OPTION_KUNDENGRUPPIERUNG_PARTNERKLASSE = 2;
	public final static int REPORT_KUNDEUMSATZSTATISTIK_OPTION_SORTIERUNG_FIRMANNAME = 0;
	public final static int REPORT_KUNDEUMSATZSTATISTIK_OPTION_SORTIERUNG_UMSATZ = 1;
	public final static int REPORT_KUNDEUMSATZSTATISTIK_OPTION_SORTIERUNG_LKZ = 2;

	
	public final static int REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_JAHRE_SORTIERBASIS_UMSATZ_GESAMT = 0;
	public final static int REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_JAHRE_SORTIERBASIS_UMSATZ_AKTUELLESJAHR = 1;
	public final static int REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_JAHRE_SORTIERBASIS_UMSATZ_VORJAHR = 2;

	
	public final static int REPORT_LIEFERANTUMSATZSTATISTIK_OPTION_GRUPPIERUNG_ARTIKELKLASSE = 0;
	public final static int REPORT_LIEFERANTUMSATZSTATISTIK_OPTION_GRUPPIERUNG_ARTIKELGRUPPE = 1;
	public final static int REPORT_LIEFERANTUMSATZSTATISTIK_OPTION_GRUPPIERUNG_JAHR = 2;
	public final static int REPORT_LIEFERANTUMSATZSTATISTIK_OPTION_GRUPPIERUNG_MONAT = 3;

	public final static int REPORT_LIEFERANTUMSATZSTATISTIK_OPTION_KUNDENGRUPPIERUNG_KEINE = 0;
	public final static int REPORT_LIEFERANTUMSATZSTATISTIK_OPTION_KUNDENGRUPPIERUNG_BRANCHE = 1;
	public final static int REPORT_LIEFERANTUMSATZSTATISTIK_OPTION_KUNDENGRUPPIERUNG_PARTNERKLASSE = 2;
	public final static int REPORT_LIEFERANTUMSATZSTATISTIK_OPTION_SORTIERUNG_FIRMANNAME = 0;
	public final static int REPORT_LIEFERANTUMSATZSTATISTIK_OPTION_SORTIERUNG_UMSATZ = 1;

	
	
	public JasperPrintLP printKundeumsatzstatistik(Timestamp tVon,
			Timestamp tBis, Integer iOptionKundengruppierung, boolean bUmsatz,
			Integer iOptionGruppierung, Integer iOptionSortierung,Integer iSortierbasisJahre,boolean bVerwendeStatistikadresse,boolean bMitNichtLagerbewertetenArtikeln,boolean ohneDBBetrachtung, 
			TheClientDto theClientDto)

	throws RemoteException;

	public JasperPrintLP printLieferantumsatzstatistik(Timestamp tVon,
			Timestamp tBis, boolean bWareneingangspositionen,
			Integer iOptionKundengruppierung, Integer iOptionGruppierung,
			Integer iOptionSortierung, TheClientDto theClientDto)
			throws RemoteException;

	public JasperPrintLP printMindesthaltbarkeit(String artikelnrVon,
			String artikelnrBis, Boolean bSortiertNachChargennr,
			Integer mhAlter, TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printLagerstandliste(Integer lagerIId,
			java.sql.Timestamp tStichtag, Boolean bMitAZArtikel,
			Boolean bNurLagerbewertete, String artikelNrVon,
			String artikelNrBis, Integer artikelgruppeIId,
			Integer artikelklasseIId, Integer vkPreislisteIId, int sortierung,
			int iArtikelarten, boolean bMitAbgewertetemGestpreis,
			boolean bMitArtikelOhneLagerstand, Integer lagerplatzIId,boolean bMitVersteckten, Integer shopgruppeIId, boolean bMitNichtLagerbewirtschaftetenArtikeln,
			TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printHitliste(Timestamp dVon, Timestamp dBis,
			Integer lagerIId, Integer iSortierung, String artikelNrVon,
			String artikelNrBis, boolean bMitHandlagerbewegungen,
			boolean bMitFertigung, boolean bMitVersteckten,TheClientDto theClientDto)
			throws RemoteException;

	public JasperPrintLP printGestpreisUeberMinVK(Integer lagerIId,
			Integer vkPreislisteIId, boolean bMitVersteckten, boolean bVergleichMitMinVKPReis,boolean bMitStuecklisten, TheClientDto theClientDto)
			throws RemoteException;

	public JasperPrintLP printLadenhueter(Timestamp dVon, Timestamp dBis,
			Integer iSortierung, String filterVon, String filterBis,
			Integer lagerIId, boolean bMitHandlagerbewegungen,
			boolean bMitFertigung,boolean bMitVersteckten, Integer artikelgruppeIId, boolean bZugaengeBeruecksichtigen, TheClientDto theClientDto)
			throws RemoteException;

	public JasperPrintLP printUmbuchungsbeleg(
			Integer lagerbewegungIIdBuchungZubuchung,
			Integer lagerbewegungIIdBuchungAbbuchung, TheClientDto theClientDto)
			throws RemoteException;

	public JasperPrintLP printLagerbuchungsbeleg(Integer iIdBuchung,
			TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printArtikelgruppen(Timestamp dVon, Timestamp dBis,
			 boolean bMitHandlagerbewegungen,
			boolean bMitFertigung, boolean bMitVersteckten,
			TheClientDto theClientDto);
	
	public JasperPrintLP printShopgruppen(Timestamp dVon, Timestamp dBis,
			boolean bMitHandlagerbewegungen, boolean bMitFertigung,
			boolean bMitVersteckten, TheClientDto theClientDto);
	
	public JasperPrintLP printWarenentnahmestatistik(Timestamp dVon,
			Timestamp dBis, Integer lagerIId,boolean bMitVersteckten, String artikelNrVon,
			String artikelNrBis,Integer artikelgruppeIId,Integer iSortierung, boolean bMitNichtLagerbewirtschaftetenArtikeln,  TheClientDto theClientDto)
			throws RemoteException;
	public JasperPrintLP printZaehlliste(Integer lagerIId,
			BigDecimal abLageragerwert, BigDecimal abGestpreis,
			Boolean bNurLagerbewirtschafteteArtikel,
			Boolean bNurArtikelMitLagerstand, Boolean bSortiereNachLagerort,
			boolean bMitVersteckten, Integer artikelgruppeIId,
			Integer artikelklasseIId, Integer lagerplatzIId,int iArtikelarten,
			TheClientDto theClientDto) throws RemoteException;
	public BigDecimal recalcGestehungspreisKomplett(Integer artikelIId, boolean debugFile);
	public JasperPrintLP printIndirekterWareneinsatz(
			DatumsfilterVonBis datumsfilter, Integer kundeIId, int iSortierung,
			TheClientDto theClientDto);
	
	
}
