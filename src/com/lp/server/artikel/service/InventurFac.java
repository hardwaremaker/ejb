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
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;

@Remote
public interface InventurFac {

	public static final int MAX_INVENTUR_BEZEICHNUNG = 80;
	public static final int MAX_INVENTURLISTE_SERIENNRCHARGENNR = 50;

	public static final String FLR_INVENTUR_C_BEZ = "c_bez";
	public static final String FLR_INVENTUR_LAGER_I_ID = "lager_i_id";
	public static final String FLR_INVENTUR_FLRLAGER = "flrlager";
	public static final String FLR_INVENTUR_T_INVENTURDATUM = "t_inventurdatum";
	public static final String FLR_INVENTUR_B_INVENTURDURCHGEFUEHRT = "b_inventurdurchgefuehrt";
	public static final String FLR_INVENTUR_FLRPERSONALINVENTURDURCHGEFUEHRT = "flrpersonalinventurdurchgefuehrt";

	public static final String FLR_INVENTURLISTE_N_INVENTURMENGE = "n_inventurmenge";
	public static final String FLR_INVENTURLISTE_C_SERIENNRCHARGENNR = "c_seriennrchargennr";
	public static final String FLR_INVENTURLISTE_FLRARTIKEL = "flrartikel";
	public static final String FLR_INVENTURLISTE_FLRLAGER = "flrlager";
	public static final String FLR_INVENTURLISTE_FLRINVENTUR = "flrinventur";

	public static final String FLR_INVENTURPROTOKOLL_N_KORREKTURMENGE = "n_korrekturmenge";
	public static final String FLR_INVENTURPROTOKOLL_N_INVENTURPREIS = "n_inventurpreis";
	public static final String FLR_INVENTURPROTOKOLL_T_ZEITPUNKT = "t_zeitpunkt";
	public static final String FLR_INVENTURPROTOKOLL_FLRINVENTURLISTE = "flrinventurliste";
	public static final String FLR_INVENTURPROTOKOLL_FLRINVENTUR = "flrinventur";

	public static final String FLR_INVENTURSTAND_N_INVENTURPREIS = "n_inventurpreis";
	public static final String FLR_INVENTURSTAND_N_ABGEWERTETERPREIS = "n_abgewerteterpreis";
	public static final String FLR_INVENTURSTAND_N_INVENTURMENGE = "n_inventurmenge";
	public static final String FLR_INVENTURSTAND_FLRARTIKEL = "flrartikel";
	public static final String FLR_INVENTURSTAND_FLRLAGER = "flrlager";
	public static final String FLR_INVENTURSTAND_FLRINVENTUR = "flrinventur";

	public final static String REPORT_MODUL = "artikel";

	public final static String REPORT_INVENTURSTAND = "ww_inventurstand.jasper";
	public final static String REPORT_INVENTURLISTE = "ww_inventurliste.jasper";
	public final static String REPORT_INVENTURPROTOKOLL = "ww_inventurprotokoll.jasper";
	public final static String REPORT_NICHTERFASSTEARTIKEL = "ww_nichterfassteartikel.jasper";

	public final static int REPORT_INVENTURSTAND_SORTIERUNG_ARTIKELNR = 0;
	public final static int REPORT_INVENTURSTAND_SORTIERUNG_ARTIKELGRUPPE = 1;
	public final static int REPORT_INVENTURSTAND_SORTIERUNG_ARTIKELKLASSE = 2;

	public Integer createInventur(InventurDto inventurDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public void removeInventur(InventurDto inventurDto) throws RemoteException,
			EJBExceptionLP;

	public void updateInventur(InventurDto inventurDto,
			TheClientDto theClientDto);

	public InventurDto inventurFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public InventurDto findByPrimaryKeyOhneExc(Integer iId)
			throws RemoteException;

	public InventurDto findByTInventurdatumMandantCNr(Timestamp tInventurdatum,
			String mandantCNr) throws RemoteException, EJBExceptionLP;

	public InventurDto[] inventurFindDurchgefuehrteInventurenEinesZeitraums(
			Timestamp tVon, Timestamp tBis, String mandantCNr)
			throws EJBExceptionLP, RemoteException;

	public InventurDto[] inventurFindInventurenNachDatum(
			Timestamp tInventurdatum, String mandantCNr) throws EJBExceptionLP,
			RemoteException;

	public InventurDto[] inventurFindOffene(String mandantCNr)
			throws EJBExceptionLP;

	public void inventurlisteErfassenMitScanner(
			InventurlisteDto inventurlisteDto, boolean bKorrekturbuchung,
			boolean bPruefeAufZuGrosseMenge, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void inventurlisteFuerSerienChargennummerAbschliessen(
			Integer inventurIId, Integer artikelIId, Integer lagerIId,
			TheClientDto theClientDto) throws RemoteException;

	public Integer createInventurliste(InventurlisteDto inventurlisteDto,
			boolean bPruefeAufZuGrosseMenge, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP;

	public void removeInventurliste(InventurlisteDto inventurlisteDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public Integer updateInventurliste(InventurlisteDto inventurlisteDto,
			boolean bPruefeAufZuGrosseMenge, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP;

	public JasperPrintLP printInventurprotokoll(Integer inventurIId,
			Integer lagerIId, TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printInventurstand(Integer inventurIId,
			Integer lagerIId, int iSortierung, TheClientDto theClientDto)
			throws RemoteException;

	public JasperPrintLP printInventurliste(Integer inventurIId,
			Integer lagerIId, boolean bInventurpreis, TheClientDto theClientDto)
			throws RemoteException;

	public JasperPrintLP printNichterfassteartikel(Integer inventurIId,
			Integer lagerIId, boolean bNurArtikelMitLagerstand,
			TheClientDto theClientDto) throws RemoteException;

	public InventurlisteDto[] inventurlisteFindByInventurIIdArtikelIId(
			Integer inventurIId, Integer artikelIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public InventurlisteDto inventurlisteFindByInventurIIdLagerIIdArtikelIIdCSeriennrchargennr(
			Integer inventurIId, Integer artikelIId, Integer lagerIId,
			String cSeriennrchargennr, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public InventurlisteDto[] inventurlisteFindByInventurIIdLagerIIdArtikelIId(
			Integer inventurIId, Integer lagerIId, Integer artikelIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public InventurlisteDto inventurlisteFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public Integer createInventurprotokoll(
			InventurprotokollDto inventurprotokollDto, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP;

	public InventurprotokollDto inventurprotokollFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public InventurprotokollDto inventurprotokollFindyByInventurlisteIIdTZeitpunkt(
			Integer inventurlisteIId, Timestamp tZeitpunkt)
			throws RemoteException, EJBExceptionLP;

	public void inventurDurchfuehren(Integer inventurIId,
			boolean bNichtInventierteArtikelAufNullSetzen,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public BigDecimal getInventurstand(Integer artikelIId, Integer lagerIId,
			Integer inventurIId, Timestamp tInventurdatum,
			TheClientDto theClientDto) throws RemoteException;

	public void preiseAbwerten(Integer inventurIId, boolean bMitStuecklisten,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Integer createInventurstand(InventurstandDto inventurstandDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public void removeInventurstand(Integer iId) throws RemoteException,
			EJBExceptionLP;

	public void removeInventurstand(InventurstandDto inventurstandDto)
			throws RemoteException, EJBExceptionLP;

	public void updateInventurstand(InventurstandDto inventurstandDto)
			throws RemoteException, EJBExceptionLP;

	public InventurstandDto inventurstandFindByPrimaryKey(Integer iId)
			throws RemoteException, EJBExceptionLP;

	public void inventurpreiseAktualisieren(Integer inventurIId,
			boolean bAufGestpreisZumInventurdatumAktualisieren,
			TheClientDto theClientDto) throws RemoteException;

	public InventurstandDto inventurstandfindByInventurIIdArtikelIIdLagerIIdOhneExc(
			Integer inventurIId, Integer lagerIId, Integer artikelIId,
			TheClientDto theClientDto);

	public void inventurpreiseAufEkPreisSetzen(Integer inventurIId,
			TheClientDto theClientDto);

	public void inventurDurchfuehrungZuruecknehmen(Integer inventurIId,
			TheClientDto theClientDto);

	public void removeInventurprotokoll(Integer inventurprotokollIId);

	public void invturprotokollZumStichtagZuruecknehmen(Integer inventurIId,
			java.sql.Date tAbStichtag, TheClientDto theClientDto);

	public String importiereInventurliste(Integer inventurIId,
			ArrayList<InvenurlisteImportDto> alImportdaten,
			TheClientDto theClientDto);
	
	public ArrayList<String> sindSeriennumernBereitsInventiert(
			InventurlisteDto inventurlisteDto, String[] snrs,
			TheClientDto theClientDto);
	public void mehrereSeriennumernInventieren(InventurlisteDto inventurlisteDto, String[] snrs,
			TheClientDto theClientDto);
	
}
