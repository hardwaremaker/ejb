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

import javax.ejb.Remote;

import com.lp.util.EJBExceptionLP;

@Remote
public interface TheJudgeFac {

	static public final int MAX_WER = 50;
	static public final int MAX_WAS = 100;

	// Flr Konstanten
	public static final String FLRSPALTE_ID_COMP_C_WER = "id_comp.c_wer";
	public static final String FLRSPALTE_ID_COMP_C_WAS = "id_comp.c_was";
	public static final String FLRSPALTE_C_USERNR = "c_usernr";
	public static final String FLRSPALTE_T_WANN = "t_wann";
	public static final String FLRSPALTE_PERSONAL_I_ID_LOCKER = "personal_i_id_locker";

	public boolean isLocked(LockMeDto lockMeI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void addLockedObject(LockMeDto lockMe, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeLockedObject(LockMeDto lockMeDto) throws EJBExceptionLP,
			RemoteException;

	public LockMeDto theJudgeFindByPrimaryKey(String sWerI, String sWasI,
			Integer iIdPersonal, String cNrUserI) throws RemoteException;

	public LockMeDto[] findByWerWas(String sWerI, String sWasI)
			throws RemoteException;

	public LockMeDto[] findByWerWasOhneExc(String sWerI, String sWasI)
			throws RemoteException;

	public boolean hatRecht(String rechtCNr, TheClientDto theClientDto);
	public Integer getSystemrolleIId(TheClientDto theClientDto);
	
}
