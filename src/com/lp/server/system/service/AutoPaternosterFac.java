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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import com.lp.server.artikel.ejb.Paternoster;
import com.lp.server.artikel.ejb.Paternostereigenschaft;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.PaternosterDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface AutoPaternosterFac {

	public static final String PT_EIGENSCHAFT_WAITRESPONSE = "WAITRESPONSE";
	public static final String PT_EIGENSCHAFT_DIR_CMD = "DIR_CMD";
	public static final String PT_EIGENSCHAFT_DIR_RESPONSE = "DIR_RESPONSE";

	public static final String PT_TYP_LEANLIFT = "Leanlift";

	public Collection<Paternoster> getAllPaternoster()
		throws RemoteException; 

	public PaternosterDto paternosterFindByPrimaryKey(Integer id) 
		throws RemoteException;
	
	public List<Paternostereigenschaft> getAllPaternosterEigenschaftByPaternosterId(Integer paternosterId) 
		throws RemoteException;

	public void paternosterAddArtikel(Integer paternosterIId, ArtikelDto artikelDto)
		throws RemoteException;
	
	public void paternosterAddArtikelAll(Integer paternosterIId, TheClientDto theClientDto)
		throws RemoteException;
	
	public void paternosterDelArtikel(Integer paternosterIId, ArtikelDto artikelDto)
		throws RemoteException;
	
	public HashMap<String,String> getPaternosterParameter(Integer paternosterIId) 
		throws RemoteException, EJBExceptionLP;
	
	public void setTimer(long millisTillStart) 
		throws RemoteException, EJBExceptionLP;
	
	public boolean isPaternosterVerfuegbar();
	

}
