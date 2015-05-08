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
package com.lp.server.system.service;


import java.rmi.RemoteException;
import java.sql.Timestamp;

import javax.ejb.Remote;

@Remote
public interface AutomatikjobFac {

	public static final String FLR_AUTOMATIKJOB_I_ID = "i_id";
	public static final String FLR_AUTOMATIKJOB_MANDANT_C_NR = "mandant_c_nr";
	public static final String FLR_AUTOMATIKJOB_I_SORT = "i_sort";
	public static final String FLR_AUTOMATIKJOB_C_NAME = "c_name";
	public static final String FLR_AUTOMATIKJOB_C_BESCHREIBUNG = "c_beschreibung";
	public static final String FLR_AUTOMATIKJOB_I_INTERVALL = "i_intervall";
	public static final String FLR_AUTOMATIKJOB_B_ACTIVE = "b_active";

	public void createAutomatikjob(AutomatikjobDto automatikjobDto)
			throws RemoteException;

	public void removeAutomatikjob(Integer iId) throws RemoteException;

	public void removeAutomatikjob(AutomatikjobDto automatikjobDto)
			throws RemoteException;

	public void updateAutomatikjob(AutomatikjobDto automatikjobDto)
			throws RemoteException;

	public void updateAutomatikjobs(AutomatikjobDto[] automatikjobDtos)
			throws RemoteException;

	public AutomatikjobDto automatikjobFindByPrimaryKey(Integer iId)
			throws RemoteException;

	public AutomatikjobDto automatikjobFindByISort(Integer iSort)
			throws RemoteException;

	public AutomatikjobDto[] automatikjobFindByBActive(Integer bActive)
			throws RemoteException;

	public AutomatikjobDto[] automatikjobFindByBMonthjob(Integer bMonthjob)
			throws RemoteException;

	public AutomatikjobDto[] automatikjobFindBydNextperform(
			Timestamp dNextPerform) throws RemoteException;

	public AutomatikjobDto automatikjobFindByCName(String cName)
			throws RemoteException;

	public AutomatikjobDto automatikjobFindByCMandantCNr(String cMandantCNr)
			throws RemoteException;
}
