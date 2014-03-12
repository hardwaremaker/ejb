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
package com.lp.server.artikel.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.TreeMap;

import javax.ejb.Remote;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.lp.server.anfrage.service.ReportAnfragestatistikKriterienDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;

@Remote
public interface ArtikelReportFac {

	public final static String REPORT_MODUL = "artikel";

	public final static String REPORT_LOSSTATUS = "ww_losstatus.jasper";
	public final static String REPORT_FREIINFERTIGUNG = "ww_freiinfertigung.jasper";
	public final static String REPORT_ARTIKELETIKETT = "ww_artikeletikett.jasper";

	public final static String REPORT_ARTIKELFEHLMENGE = "ww_artikelfehlmenge.jasper";
	public final static String REPORT_ARTIKELSTAMMBLATT = "ww_artikelstammblatt.jasper";
	public final static String REPORT_VERWENDUNGSNACHWEIS = "ww_verwendungsnachweis.jasper";
	public final static String REPORT_BEWEGUNGSVORSCHAU = "ww_bewegungsvorschau.jasper";
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
	public final static String REPORT_AENDERUNGEN = "ww_aenderungen.jasper";
	public final static String REPORT_NAECHSTE_WARTUNGEN = "ww_naechste_wartungen.jasper";
	public final static String REPORT_KUNDENSOKOS = "ww_kundensokos.jasper";

	public final static String REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG = "ww_lagercockpit_welager_verteilungsvorschlag.jasper";
	public final static String REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG = "ww_lagercockpit_material_verteilungsvorschlag.jasper";

	public JasperPrintLP printLosstatus(Integer artikelIId,
			TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printFreiInFertigung(Integer artikelIId,
			TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printArtikelstammblatt(Integer artikelIId,
			TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printBewegungsvorschau(Integer artikelId,
			boolean bInternebestellungMiteinbeziehen, TheClientDto theClientDto)
			throws RemoteException;

	public JasperPrintLP printVkPreisliste(Integer preislisteIId,
			Integer artikelgruppeIId, Integer artikelklasseIId,
			Integer shopgruppeIId, boolean bMitInaktiven, String artikelNrVon,
			String artikelNrBis, boolean bMitVersteckten,
			Date datGueltikeitsdatumI, TheClientDto theClientDto)
			throws RemoteException;

	public JasperPrintLP printVkPreisentwicklung(Integer artikelIId,
			TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printLieferantenpreis(Integer artikelIId,
			TheClientDto theClientDto);

	public JasperPrintLP printArtikelstatistik(Integer artikelIId, Date dVon,
			Date dBis, Integer iOption, boolean bMonatsstatistik,
			boolean bEingeschraenkt, boolean bMitHandlagerbewegungen,
			boolean bMitBewegungsvorschau, boolean bMitHistory,
			TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printArtikeletikett(Integer artikelIId,
			String sKommentar, BigDecimal bdMenge, Integer iExemplare,
			String[] cSnrChnr, TheClientDto theClientDto)
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
			TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printKundensokos(Integer artikelIId,
			TheClientDto theClientDto);

	public JasperPrintLP printAuftragsseriennummern(Integer artikelId,
			java.sql.Date dVon, java.sql.Date dBis, TheClientDto theClientDto);

	public JasperPrintLP printAufgeloesteFehlmengen(
			TreeMap<?, ?> tmAufgeloesteFehlmengen, TheClientDto theClientDto)
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
			boolean bNurArtikelMitLagerstand, TheClientDto theClientDto);

	public JasperPrintLP printLagercockpitWELagerVerteilungsvorschlag(
			TheClientDto theClientDto);

	public JasperPrintLP printNaechsteWartungen(TheClientDto theClientDto);
}
