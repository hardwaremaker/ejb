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
package com.lp.server.fertigung.service;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Locale;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface FertigungServiceFac {
	public void createLosstatus(LosstatusDto losstatusDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public void removeLosstatus(LosstatusDto losstatusDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public void updateLosstatus(LosstatusDto losstatusDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public LosstatusDto losstatusFindByPrimaryKey(String statusCNr)
			throws RemoteException, EJBExceptionLP;

	public Integer createLosklasse(LosklasseDto losklasseDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public void removeLosklasse(LosklasseDto losklasseDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public void updateLosklasse(LosklasseDto losklasseDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public LosklasseDto losklasseFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public LosklassesprDto losklassesprFindByPrimaryKey(Integer losklasseIId,
			String localeCNr) throws RemoteException, EJBExceptionLP;

	public String uebersetzeLosklasseOptimal(Integer iId, Locale locale1,
			Locale locale2) throws EJBExceptionLP, RemoteException;

	public void updateLosbereich(LosbereichDto losbereichDto,
			TheClientDto theClientDto);

	public Integer createLosbereich(LosbereichDto losbereichDto,
			TheClientDto theClientDto);

	public LosbereichDto losbereichFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto);

	public void removeLosbereich(LosbereichDto losbereichDto,
			TheClientDto theClientDto);

	public LospruefplanDto lospruefplanFindByPrimaryKey(Integer iId);

	public void removeLospruefplan(Integer iId);

	public void updateLospruefplan(LospruefplanDto dto,
			TheClientDto theClientDto);

	public Integer createLospruefplan(LospruefplanDto dto,
			TheClientDto theClientDto);

	public void updatePruefergebnisse(ArrayList<PruefergebnisDto> dtos,Integer losablieferungIId,
			TheClientDto theClientDto);

	public ArrayList<LospruefplanDto> lospruefplanFindyByLosIId(Integer losIId);
	public ArrayList<PruefergebnisDto>  pruefergebnisFindByLosablieferungIId(Integer losablieferungIId);
	public void removePruefergebnisse(Integer losablieferungIId,
			TheClientDto theClientDto);
	public void vertauscheLospruefplan(Integer iId1, Integer iId2);

}
