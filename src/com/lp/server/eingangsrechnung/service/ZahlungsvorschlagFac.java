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
package com.lp.server.eingangsrechnung.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface ZahlungsvorschlagFac {
	public final static int FORMAT_CSV = 0;

	public Integer createZahlungsvorschlag(ZahlungsvorschlagkriterienDto krit,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	/**
	 * Einen ganzen Zahlungsvorschlaglauf loeschen.
	 * 
	 * @param zahlungsvorschlaglaufIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	public void removeZahlungsvorschlaglauf(Integer zahlungsvorschlaglaufIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public ZahlungsvorschlaglaufDto zahlungsvorschlaglaufFindByPrimaryKey(
			Integer iId) throws EJBExceptionLP, RemoteException;

	public ZahlungsvorschlagDto zahlungsvorschlagFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public ZahlungsvorschlagDto[] zahlungsvorschlagFindByZahlungsvorschlaglaufIId(
			Integer zahlungsvorschlaglaufIId) throws EJBExceptionLP,
			RemoteException;

	/**
	 * Flag "Bezahlen" auf diesem Datensatz invertieren.
	 * 
	 * @param zahlungsvorschlagIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	public void toggleZahlungsvorschlagBBezahlen(Integer zahlungsvorschlagIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public BigDecimal getGesamtwertEinesZahlungsvorschlaglaufsInMandantenwaehrung(
			Integer zahlungsvorschlagleufIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public String exportiereZahlungsvorschlaglauf(
			Integer zahlungsvorschlagleufIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ZahlungsvorschlagDto updateZahlungsvorschlag(
			ZahlungsvorschlagDto zahlungsvorschlagDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;
}
