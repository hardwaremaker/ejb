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
import java.util.Collection;
import java.util.Hashtable;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface ArtikelbestelltFac {

	public final static String REPORT_MODUL = "artikel";

	public static final String FLR_ARTIKELBESTELLT_FLRARTIKEL = "flrartikel";
	public static final String FLR_ARTIKELBESTELLT_I_BELEGARTPOSITIONID = "i_belegartpositionid";
	public static final String FLR_ARTIKELBESTELLT_F_MENGE = "n_menge";
	/**
	 * @todo T_LIEFERTERMIN
	 */
	public static final String FLR_ARTIKELBESTELLT_D_LIEFERTERMIN = "t_liefertermin";
	public static final String FLR_ARTIKELBESTELLT_C_BELEGARTNR = "c_belegartnr";

	public static final String KEY_RAHMENBESTELLT_ANZAHL = "RAHMENBESTELLT_ANZAHL";
	public static final String KEY_RAHMENBESTELLT_BELEGCNR = "RAHMENBESTELLT_BELEGCNR";

	public void createArtikelbestellt(ArtikelbestelltDto artikelbestelltDto)
			throws EJBExceptionLP, RemoteException;

	public void removeArtikelbestellt(String belegartCNr,
			Integer belegartpositionIId) throws EJBExceptionLP, RemoteException;

	public void updateArtikelbestellt(ArtikelbestelltDto artikelbestelltDto)
			throws EJBExceptionLP, RemoteException;

	public void updateArtikelbestelltRelativ(
			ArtikelbestelltDto artikelbestelltDto) throws EJBExceptionLP,
			RemoteException;

	public ArtikelbestelltDto artikelbestelltFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public ArtikelbestelltDto artikelbestelltFindByBelegartCNrBelegartPositionIId(
			String belegartCNr, Integer belegartpositionIId)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal getAnzahlBestellt(Integer artikelIId)
			throws EJBExceptionLP, RemoteException;

	public void aktualisiereBestelltListe(Integer bestellungIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void pruefeBestelltliste(TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public ArtikelbestelltDto artikelbestelltFindByBelegartCNrBelegartPositionIIdOhneExc(
			String belegartCNr, Integer belegartpositionIId)
			throws EJBExceptionLP, RemoteException;

	/**
	 * Von offenen Rahmenbestellungen die Anzahl Rahmenbestellt Menge und die
	 * Rahmenbestellnummern zurueckgeben.
	 * 
	 * @param artikelIId
	 *            Integer
	 * @param theClientDto
	 * @return Hashtable Keys: KEY_RAHMENBESTELLT_ANZAHL BigDecimal, die Anzahl
	 *         Rahmenbestellt Menge. KEY_RAHMENBESTELLT_BELEGCNR Collection von
	 *         String Rahmenbestellcnr.
	 * 
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	public Hashtable<?, ?> getAnzahlRahmenbestellt(Integer artikelIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	/**
	 * getArtikelbestellt gibt eine List vom FLRArtikelbestellt zurueck. Achtung
	 * casten notwendig.
	 * 
	 * @param artikelIId
	 *            Integer
	 * @param dVon
	 *            Date
	 * @param dBis
	 *            Date
	 * @return List
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	public Collection<?> getArtikelbestellt(Integer artikelIId,
			java.sql.Date dVon, java.sql.Date dBis) throws EJBExceptionLP,
			RemoteException;

}
