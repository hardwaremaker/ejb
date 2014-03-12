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
package com.lp.server.system.service;

import java.rmi.RemoteException;
import java.sql.Timestamp;

import javax.ejb.Remote;

import com.lp.util.EJBExceptionLP;

@Remote
public interface TheClientFac {

	public static final String FLRSPALTE_C_BENUTZERNAME = "c_benutzername";
	public static final String FLRSPALTE_C_UILOCALE = "c_uisprache";
	public static final String FLRSPALTE_C_KONZERNLOCALE = "c_konzernsprache";
	public static final String FLRSPALTE_T_LOGGEDIN = "t_loggedin";
	public static final String FLRSPALTE_T_LOGGEDOUT = "t_loggedout";
	public static final String FLRSPALTE_I_PERSONAL = "i_personal";

	public static final int PK_INSTALLER = 1;

	public void createTheClient(TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeTheClientTVonTBis(Timestamp tVon, Timestamp tBis)
			throws EJBExceptionLP, RemoteException;

	public void removeTheClient(String cNr) throws EJBExceptionLP,
			RemoteException;

	public void removeTheClient(TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateTheClient(TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateTheClients(TheClientDto[] theClientDtos)
			throws EJBExceptionLP, RemoteException;

	public TheClientDto theClientFindByPrimaryKey(String cNrUserI)
			throws EJBExceptionLP, RemoteException;

	public TheClientDto theClientFindByUserLoggedIn(String cNr)
			throws EJBExceptionLP, RemoteException;

	public TheClientDto theClientFindByCBenutzernameLoggedIn(String cBenutzername)
	  		throws RemoteException;

	public void putInstaller(InstallerDto installerDto) throws RemoteException;

	public void removeInstaller() throws RemoteException;

	public boolean istNeuerClientVerfuegbar(Integer iBuildnummerclient)
			throws RemoteException;

	public InstallerDto getInstaller() throws RemoteException;

	public InstallerDto getInstallerWithoutClientFile() throws RemoteException;
	
	  public InstallerDto getInstallerPart(Integer iPart) throws EJBExceptionLP;

}
