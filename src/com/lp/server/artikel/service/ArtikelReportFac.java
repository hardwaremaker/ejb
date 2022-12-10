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
import java.sql.Date;
import java.util.ArrayList;

import javax.ejb.Remote;

import com.lp.server.anfrage.service.ReportAnfragestatistikKriterienDto;
import com.lp.server.artikel.service.ArtikelReportFac.ReportKundensonderkoditionenSortierung;
import com.lp.server.partner.service.CustomerPricelistReportDto;
import com.lp.server.system.service.GeaenderteChargennummernDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.LPDatenSubreport;

@Remote
public interface ArtikelReportFac {

	public final static String REPORT_MODUL = "artikel";

	public final static String REPORT_LOSSTATUS = "ww_losstatus.jasper";
	public final static String REPORT_FREIINFERTIGUNG = "ww_freiinfertigung.jasper";
	public final static String REPORT_ARTIKELETIKETT = "ww_artikeletikett.jasper";

	public final static String REPORT_ARTIKELFEHLMENGE = "ww_artikelfehlmenge.jasper";
	public final static String REPORT_ARTIKELSTAMMBLATT = "ww_artikelstammblatt.jasper";
	public final static String REPORT_VERWENDUNGSNACHWEIS = "ww_verwendungsnachweis.jasper";
	public final static String REPORT_ARTIKEL_OHNE_STKL_VERWENDUNG = "ww_artikel_ohne_stkl_verwendung.jasper";
	public final static String REPORT_BEWEGUNGSVORSCHAU = "ww_bewegungsvorschau.jasper";
	public final static String REPORT_BEWEGUNGSVORSCHAU_ALLE = "ww_bewegungsvorschau_alle.jasper";
	public final static String REPORT_AUFGELOESTEFEHLMENGEN = "ww_aufgeloestefehlmengen.jasper";
	public final static String REPORT_VKPREISLISTE = "ww_vkpreisliste.jasper";
	public final static String REPORT_ARTIKELBESTELLT = "ww_artikelbestellt.jasper";
	public final static String REPORT_ARTIKELBESTELLTRAHMEN = "ww_artikelbestelltrahmen.jasper";
	public final static String REPORT_RAHMENRESERVIERUNG = "ww_rahmenreservierung.jasper";
	public final static String REPORT_ARTIKELSTATISTIK = "ww_artikelstatistik.jasper";
	public final static String REPORT_MONATSSTATISTIK = "ww_monatsstatistik.jasper";
	public final static String REPORT_LAGERPLATZETIKETT = "ww_lagerplatzetikett.jasper";
	public final static String REPORT_VKPREISENTWICKLUNG = "ww_vkpreisentwicklung.jasper";
	public final static String REPORT_AUFTRAGSSERIENNUMMERN = "ww_auftragsseriennummern.jasper";
	public final static String REPORT_MINDESTLAGERSTAENDE = "ww_mindestlagerstaende.jasper";
	public final static String REPORT_LIEFERANTENPREIS = "ww_lieferantenpreis.jasper";
	public final static String REPORT_LIEFERANTENPREISVERGLEICH = "ww_lieferantenpreisvergleich.jasper";
	public final static String REPORT_AENDERUNGEN = "ww_aenderungen.jasper";
	public final static String REPORT_NAECHSTE_WARTUNGEN = "ww_naechste_wartungen.jasper";
	public final static String REPORT_KUNDENSOKOS = "ww_kundensokos.jasper";
	public final static String REPORT_KUNDENSONDERKONDITIONEN = "ww_kundensonderkonditionen.jasper";
	public final static String REPORT_ALLERGENE = "ww_allergene.jasper";
	public final static String REPORT_VERSCHLEISSTEILWERKZEUG = "ww_verschleissteilwerkzeug.jasper";
	public final static String REPORT_AUFTRAGSWERTE = "ww_auftragswerte.jasper";
	public final static String REPORT_GEAENDERTE_CHARGEN = "ww_geaenderte_chargen.jasper";

	public static final String REPORT_SNRSTAMMBLATT = "ww_snrstammblatt.jasper";

	public final static String ADD_INFO_FASESSION = "ADD_INFO_FASESSION";

	public final static String REPORT_MAKE_OR_BUY = "ww_make_or_buy.jasper";
	
	public final static String REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG = "ww_lagercockpit_welager_verteilungsvorschlag.jasper";
	public final static String REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG = "ww_lagercockpit_material_verteilungsvorschlag.jasper";

	public final static Integer OPTION_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_NUR_ARTIKEL_MIT_LAGERSTAND = 0;
	public final static Integer OPTION_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_NUR_RUECKNAHMEN_AUS_FERTIGUNG = 1;

	public JasperPrintLP printLosstatus(Integer artikelIId,
			TheClientDto theClientDto) throws RemoteException;
	public static enum ReportMehrereArtikeletikettenSortierung {
		SORT_ARTIKELNUMMER, SORT_ARTIKELGRUPPE, SORT_ARTIKELKLASSE, SORT_LAGERORT, SORT_ARTIKELBEZEICHNUNG,
		SORT_ARTIKELKURZBEZEICHNUNG, SORT_SHOPGRUPPE, SORT_REFERENZNUMMER
	}

	public static enum ReportKundensonderkoditionenSortierung {
		SORT_ARTIKELNUMMER, SORT_KUNDE
	}
	
	
	
	
	public JasperPrintLP printFreiInFertigung(Integer artikelIId,
			TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printArtikelstammblatt(Integer artikelIId,
			TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printBewegungsvorschau(Integer artikelId,
			boolean bInternebestellungMiteinbeziehen,
			Integer partnerIIdStandort, boolean bMitRahmen,
			boolean bUeberAlleMandanten, TheClientDto theClientDto)
			throws RemoteException;

	public JasperPrintLP printVkPreisliste(Integer preislisteIId,
			Integer artikelgruppeIId, Integer artikelklasseIId,
			Integer shopgruppeIId, boolean bMitInaktiven, String artikelNrVon,
			String artikelNrBis, boolean bMitVersteckten,
			Date datGueltikeitsdatumI, String waehrungCNr, TheClientDto theClientDto)
			throws RemoteException;

	public JasperPrintLP printVkPreisentwicklung(Integer artikelIId,
			TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printLieferantenpreis(Integer artikelIId,
			TheClientDto theClientDto);
	public JasperPrintLP printMehrereArtikeletiketten(String artikelgruppeFilt, Integer artikelklasseIId,
			Integer artikelVonId, Integer artikelBisId, Integer lagerIId, Integer lagerplatzVon, Integer lagerplatzBis,
			Integer shopgruppeIId, ReportMehrereArtikeletikettenSortierung sortierung, boolean orderAscDesc, boolean etikettProLagerplatz, TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printArtikelstatistik(Integer artikelIId, Date dVon,
			Date dBis, Integer iOption, boolean bMonatsstatistik,
			boolean bEingeschraenkt, boolean bMitHandlagerbewegungen,
			boolean bMitBewegungsvorschau,
			boolean bMitNichtFreigegebenenAuftraegen, boolean bMitHistory,
			boolean bMitVorgaengern, TheClientDto theClientDto)
			throws RemoteException;

	public JasperPrintLP printArtikeletikett(Integer artikelIId,
			String sKommentar, BigDecimal bdMenge, Integer iExemplare,
			String[] cSnrChnr, Integer lagerIIdfuerLagerstandDerCharge,
			String lfdnr,String trennzeichenLfdNr, TheClientDto theClientDto)
			throws RemoteException;
	
	public JasperPrintLP printLagerplatzetikett(Integer lagerplatzIId,
			TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printFehlmengen(Integer artikelIId,
			TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printRahmenbestellungsliste(Integer artikelIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printVerwendungsnachweis(Integer artikelIId,
			boolean bMitVerbrauchtenMengen, java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, boolean bMitVersteckten,
			boolean bMitHierarchie, boolean bMandantenuebergreifend,boolean bVerdichtet,
			TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printKundensokos(Integer artikelIId,
			TheClientDto theClientDto);

	public JasperPrintLP printAuftragsseriennummern(Integer artikelId,
			java.sql.Date dVon, java.sql.Date dBis, TheClientDto theClientDto);

	public JasperPrintLP printAufgeloesteFehlmengen(Integer fasessionIId,
			boolean bNachdruck, TheClientDto theClientDto)
			throws RemoteException;

	public JasperPrintLP printMindestlagerstaende(TheClientDto theClientDto);

	public JasperPrintLP printArtikelbestellt(Integer artikelIId, Date dVon,
			Date dBis, TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printAenderungen(Integer artikelIId,
			TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printRahmenreservierungsliste(
			ReportAnfragestatistikKriterienDto kritDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public ReportRahmenreservierungDto[] getReportRahmenreservierung(
			ReportAnfragestatistikKriterienDto kritDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printLagercockpitMaterialVerteilungsvorschlag(
			Integer iOption, TheClientDto theClientDto);

	public JasperPrintLP printLagercockpitWELagerVerteilungsvorschlag(
			TheClientDto theClientDto);

	public JasperPrintLP printNaechsteWartungen(TheClientDto theClientDto);

	public LPDatenSubreport getSubreportAllergene(Integer artikelIId,
			TheClientDto theClientDto);

	public JasperPrintLP printAllergene(String artikelNrVon,
			String artikelNrBis, TheClientDto theClientDto);

	public JasperPrintLP printGeaenderteChargen(String artikelNrVon,
			String artikelNrBis,
			ArrayList<GeaenderteChargennummernDto> alGeaenderteChargen,
			TheClientDto theClientDto);
	
	public JasperPrintLP printArtikelOhneStklVerwendung(
			boolean bMitVersteckten, TheClientDto theClientDto);

	public JasperPrintLP printWerkzeugVerschleissteil(Integer werkzeugIId,
			Integer verschleisteilIId, TheClientDto theClientDto);

	public JasperPrintLP printSeriennummernStammblatt(Integer lagerIId,
			Integer artikelIId, String[] snrs, String snrWildcard,
			Boolean bSortNachIdent, String versionWildcard,
			boolean bNurSeriennummern, boolean nurObersteEbene,
			TheClientDto theClientDto);

	public JasperPrintLP printAuftragswerte(String artikelNrVon,
			String artikelNrBis, boolean bMitKonsignationslager,
			TheClientDto theClientDto);

	/**
	 * Druckt ein Exemplar eines Artikeletiketts &uuml;ber den Server, auf dem im
	 * Arbeitsplatzparameter DRUCKERNAME_MOBILES_ARTIKEL_ETIKETT hinterlegten Drucker.
	 * 
	 * @param artikelIId
	 * @param sKommentar
	 * @param bdMenge
	 * @param cSnrChnr
	 * @param lagerIIdfuerLagerstandDerCharge
	 * @param lfdnr
	 * @param theClientDto
	 * @return
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 */
	JasperPrintLP printArtikeletikettOnServer(Integer artikelIId, String sKommentar, BigDecimal bdMenge,
			String[] cSnrChnr, Integer lagerIIdfuerLagerstandDerCharge, String lfdnr, TheClientDto theClientDto) 
					throws RemoteException, EJBExceptionLP;

	/**
	 * Druckt das Artikeletikett &uuml;ber den Server, auf dem im
	 * Arbeitsplatzparameter DRUCKERNAME_MOBILES_ARTIKEL_ETIKETT hinterlegten Drucker.
	 * 
	 * @param artikelIId
	 * @param sKommentar
	 * @param bdMenge
	 * @param cSnrChnr
	 * @param lagerIIdfuerLagerstandDerCharge
	 * @param lfdnr
	 * @param exemplare
	 * @param theClientDto
	 * @return
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 */
	JasperPrintLP printArtikeletikettOnServer(Integer artikelIId, String sKommentar, BigDecimal bdMenge,
			String[] cSnrChnr, Integer lagerIIdfuerLagerstandDerCharge, String lfdnr, Integer exemplare,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	CustomerPricelistReportDto printVkPreislisteRaw(
			Integer preislisteIId, Integer artikelgruppeIId, 
			Integer artikelklasseIId, Integer shopgruppeIId, 
			boolean bMitInaktiven, String artikelNrVon, String artikelNrBis,
			boolean bMitVersteckten, java.sql.Date datGueltikeitsdatumI, final TheClientDto theClientDto);
	
	public JasperPrintLP printBewegungsvorschauAlle(Integer artikelgruppeIId, Integer artikelklasseIId, String artiklenrVon, String artiklenrBis,
			Integer partnerIIdStandort, boolean bMitRahmen,boolean bArtikelOhneBewegungsvorschauAusblenden, boolean bMitVersteckten, TheClientDto theClientDto);
	
	public JasperPrintLP printLieferantenpreisvergleich(Integer artikelIId, java.sql.Date dStichtag,
			TheClientDto theClientDto);
	
	public JasperPrintLP printMakeOrBuy(Integer artikelgruppeIId,Integer artikelklasseIId, String artiklenrVon, String artiklenrBis,
			java.sql.Date dStichtagEK, java.sql.Date dZeitraumVonAblieferung,java.sql.Date dZeitraumBisAblieferung, boolean bMitVersteckten,  TheClientDto theClientDto);
	
	public JasperPrintLP printKundensonderkonditionen(Integer artikelgruppeIId, Integer artikelklasseIId, String artiklenrVon,
			String artiklenrBis, boolean bMitVersteckten,ReportKundensonderkoditionenSortierung sort, TheClientDto theClientDto);
	
	
}
