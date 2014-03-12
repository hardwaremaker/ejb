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
import java.util.ArrayList;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface FehlmengeFac {

	public void aktualisiereFehlmenge(String belegartCNr,
			Integer belegpositionIId, boolean throwExceptionWhenCreate, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public BigDecimal getAnzahlderPositivenFehlmengenEinesArtikels(Integer artikelIId,
			TheClientDto theClientDto);
	
	public ArtikelfehlmengeDto artikelfehlmengeFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public ArtikelfehlmengeDto[] artikelfehlmengeFindByArtikelIId(
			Integer artikelIId) throws EJBExceptionLP, RemoteException;

	public BigDecimal getAnzahlFehlmengeEinesArtikels(Integer artikelIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public ArrayList<?> getFehlmengen(Integer artikelIId, String mandantCNr,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void createArtikelfehlmenge(ArtikelfehlmengeDto artikelfehlmengeDto)
	throws EJBExceptionLP, RemoteException;
	
	public ArtikelfehlmengeDto artikelfehlmengeFindByBelegartCNrBelegartPositionIId(
			String belegartCNr, Integer belegartpositionIId)
			throws EJBExceptionLP, RemoteException;

	public ArtikelfehlmengeDto artikelfehlmengeFindByBelegartCNrBelegartPositionIIdOhneExc(
			String belegartCNr, Integer belegartpositionIId)
			throws EJBExceptionLP, RemoteException;

	public void pruefeFehlmengen(TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

}
