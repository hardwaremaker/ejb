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
import java.util.HashSet;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;

@Remote
public interface ReservierungFac {

	public final static String REPORT_MODUL = "artikel";

	public final static String REPORT_ARTIKELRESERVIERUNG = "ww_artikelreservierung.jasper";
	public final static String REPORT_ARTIKELRESERVIERUNG_MONATSSTATISTIK = "ww_artikelreservierung_monatsstatistik.jasper";

	public static final String FLR_ARTIKELRESERVIERUNG_FLRARTIKEL = "flrartikel";
	public static final String FLR_ARTIKELRESERVIERUNG_I_BELEGARTPOSITIONID = "i_belegartpositionid";
	public static final String FLR_ARTIKELRESERVIERUNG_F_MENGE = "n_menge";
	/**
	 * @todo T_LIEFERTERMIN
	 */
	public static final String FLR_ARTIKELRESERVIERUNG_D_LIEFERTERMIN = "t_liefertermin";
	public static final String FLR_ARTIKELRESERVIERUNG_C_BELEGARTNR = "c_belegartnr";

	public void createArtikelreservierung(
			ArtikelreservierungDto artikelreservierungDto)
			throws EJBExceptionLP, RemoteException;

	public void removeArtikelreservierung(Integer iId) throws EJBExceptionLP,
			RemoteException;

	public void removeArtikelreservierung(String belegartCNr,
			Integer belegartpositionIId) throws EJBExceptionLP, RemoteException;

	public void updateArtikelreservierung(
			ArtikelreservierungDto artikelreservierungDto)
			throws EJBExceptionLP, RemoteException;

	public ArtikelreservierungDto artikelreservierungFindByPrimaryKey(
			Integer iId) throws EJBExceptionLP, RemoteException;

	public ArtikelreservierungDto artikelreservierungFindByBelegartCNrBelegartPositionIId(
			String belegartCNr, Integer belegartpositionIId)
			throws EJBExceptionLP, RemoteException;

	public ArtikelreservierungDto artikelreservierungFindByBelegartCNrBelegartPositionIIdOhneExc(
			String belegartCNr, Integer belegartpositionIId)
			throws EJBExceptionLP, RemoteException;

	public ArtikelreservierungDto getArtikelreservierung(String belegartCNr,
			Integer iIdBelegartpositionI) throws RemoteException,
			EJBExceptionLP;

	public void pruefeReservierungenPerSQL(TheClientDto theClientDto);
	public void pruefeReservierungen(TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public JasperPrintLP printArtikelreservierungen(Integer artikelIId,
			Date dVon, Date dBis, boolean bAlleMandanten, boolean bMonatsstatistik, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public BigDecimal getAnzahlReservierungen(Integer artikelIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;
	public BigDecimal getAnzahlReservierungen(Integer artikelIId,
			java.sql.Timestamp tStichtag, String mandantCNr,
			Integer partnerIIdStandort,boolean bNurInterne, String belegartCNr);
	
	public BigDecimal getAnzahlReservierungen(Integer artikelIId, java.sql.Timestamp tStichtag, String mandantCNr,
			Integer partnerIIdStandort, boolean bNurInterne, String belegartCNr, boolean bAlle);
	

	public BigDecimal getAnzahlRahmenreservierungen(Integer artikelIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;
	public BigDecimal getAnzahlReservierungen(Integer artikelIId,
			java.sql.Timestamp tStichtag, String mandantCNr)
			throws EJBExceptionLP;
	public BigDecimal getAnzahlReservierungen(Integer artikelIId,
			java.sql.Timestamp tStichtag, String mandantCNr,
			Integer partnerIIdStandort);
	
	public HashSet getSetOfArtikelIdMitReservierungen() ;
}
