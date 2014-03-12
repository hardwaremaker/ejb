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
package com.lp.server.partner.service;

import java.rmi.RemoteException;
import java.sql.Date;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface KundesachbearbeiterFac {

	// FLR Spaltennamen aus Hibernate Mapping.
	public static final String FLR_KUNDE_I_ID = "kunde_i_id";
	public static final String FLR_PERSONAL = "flrpersonal";
	public static final String FLR_GUELTIG_AB = "t_gueltigab";

	public Integer createKundesachbearbeiter(
			KundesachbearbeiterDto kundesachbearbeiterDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeKundesachbearbeiter(Integer iId) throws EJBExceptionLP,
			RemoteException;

	public void removeKundesachbearbeiter(
			KundesachbearbeiterDto kundesachbearbeiterDto)
			throws EJBExceptionLP, RemoteException;

	public void updateKundesachbearbeiter(
			KundesachbearbeiterDto kundesachbearbeiterDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public KundesachbearbeiterDto kundesachbearbeiterFindByPrimaryKey(
			Integer iId) throws EJBExceptionLP, RemoteException;

	public KundesachbearbeiterDto[] kundesachbearbeiterFindByKundeIId(
			Integer iIdKundeI) throws EJBExceptionLP, RemoteException;

	public KundesachbearbeiterDto[] kundesachbearbeiterFindByKundeIIdOhneExc(
			Integer iIdKundeI) throws RemoteException;

	public KundesachbearbeiterDto kundesachbearbeiterFindByKundeIIdPartnerIIdGueltigAb(
			Integer iIdKundeI, Integer iIdPersonalI, java.sql.Date dGueltigAb)
			throws EJBExceptionLP, RemoteException;

	public KundesachbearbeiterDto kundesachbearbeiterFindByKundeIIdPartnerIIdGueltigAbOhneExc(
			Integer iIdKundeI, Integer iIdPersonalI, Date dGueltigAb)
			throws RemoteException;

}
