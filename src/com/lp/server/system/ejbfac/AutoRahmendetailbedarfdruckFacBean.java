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
package com.lp.server.system.ejbfac;

import java.rmi.RemoteException;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.lp.server.system.ejb.AutoRahmendetailbedarfdruck;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.AutoRahmendetailbedarfdruckDto;
import com.lp.server.system.service.AutoRahmendetailbedarfdruckFac;
import com.lp.server.system.service.AutoRahmendetaildruckDtoAssembler;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;

@Stateless
public class AutoRahmendetailbedarfdruckFacBean extends Facade implements
		AutoRahmendetailbedarfdruckFac {
	
	@PersistenceContext
	private EntityManager em;

	public AutoRahmendetailbedarfdruckDto autoAutoRahmendetailbedarfdruckFindByMandantCNr(
			String mandantCNr) throws RemoteException {
		Query query = em.createNamedQuery("AutoRahmendetailbedarfdruckfindByMandantCNr");
		query.setParameter(1, mandantCNr);
		return AutoRahmendetaildruckDtoAssembler.createDto((AutoRahmendetailbedarfdruck) query.getSingleResult());
	}

	public AutoRahmendetailbedarfdruckDto autoAutoRahmendetailbedarfdruckFindByPrimaryKey(
			Integer id) throws RemoteException {
		AutoRahmendetailbedarfdruck autoRahmendetailbedarfdruck = em.find(AutoRahmendetailbedarfdruck.class, id);
		if(autoRahmendetailbedarfdruck==null){
			return null;
		}
		return AutoRahmendetaildruckDtoAssembler.createDto(autoRahmendetailbedarfdruck);
	}

	public void createAutoRahmendetailbedarfdruck(
			AutoRahmendetailbedarfdruckDto autoRahmendetailbedarfdruckDto)
			throws RemoteException {
		if (autoRahmendetailbedarfdruckDto == null) {
			return;
		}
		if(autoRahmendetailbedarfdruckDto.getIId() == null){
			Integer iId = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_AUTOMATIKRAHMENDETAILBEDARFDRUCK);
			autoRahmendetailbedarfdruckDto.setIId(iId);
		}

		try {
			AutoRahmendetailbedarfdruck autoRahmendetailbedarfdruck = new AutoRahmendetailbedarfdruck();
			autoRahmendetailbedarfdruck.setIId(autoRahmendetailbedarfdruckDto.getIId());
			autoRahmendetailbedarfdruck = setAutoRahmendetailbedarfdruckFromAutoRahmendetailbedarfdruckDto(
					autoRahmendetailbedarfdruck, autoRahmendetailbedarfdruckDto);
			em.persist(autoRahmendetailbedarfdruck);
			em.flush();
		}
		catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE,e);
		}

	}

	public void removeAutoRahmendetailbedarfdruck(Integer id)
			throws RemoteException {
		AutoRahmendetailbedarfdruck toRemove = em.find(AutoRahmendetailbedarfdruck.class, id);
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void removeAutoRahmendetailbedarfdruck(
			AutoRahmendetailbedarfdruckDto autoRahmendetailbedarfdruckDto)
			throws RemoteException {
		removeAutoRahmendetailbedarfdruck(autoRahmendetailbedarfdruckDto.getIId());

	}

	public void updateAutoRahmendetailbedarfdruck(
			AutoRahmendetailbedarfdruckDto autoRahmendetailbedarfdruckDto)
			throws RemoteException {
		AutoRahmendetailbedarfdruck autoRahmendetailbedarfdruck = em.find(AutoRahmendetailbedarfdruck.class, autoRahmendetailbedarfdruckDto.getIId());
		autoRahmendetailbedarfdruck = setAutoRahmendetailbedarfdruckFromAutoRahmendetailbedarfdruckDto(autoRahmendetailbedarfdruck, autoRahmendetailbedarfdruckDto);
		em.merge(autoRahmendetailbedarfdruck);
		em.flush();
	}

	public void updateAutoRahmendetailbedarfdrucks(
			AutoRahmendetailbedarfdruckDto[] autoRahmendetailbedarfdruckDtos)
			throws RemoteException {
		for(int i=0;i<autoRahmendetailbedarfdruckDtos.length;i++){
			updateAutoRahmendetailbedarfdruck(autoRahmendetailbedarfdruckDtos[i]);
		}

	}
	
	private AutoRahmendetailbedarfdruck setAutoRahmendetailbedarfdruckFromAutoRahmendetailbedarfdruckDto(AutoRahmendetailbedarfdruck
			autoRahmendetailbedarfdruck, AutoRahmendetailbedarfdruckDto autoRahmendetailbedarfdruckDto) {
		autoRahmendetailbedarfdruck.setMandantCNr(autoRahmendetailbedarfdruckDto.getMandantCNr());
		autoRahmendetailbedarfdruck.setCDrucker(autoRahmendetailbedarfdruckDto.getCDrucker());
		if(autoRahmendetailbedarfdruckDto.getBSortiertnachArtikel()==null){
			autoRahmendetailbedarfdruckDto.setBSortiertnachArtikel(true);
		}
		if(autoRahmendetailbedarfdruckDto.getBSortiertnachArtikel()){
			autoRahmendetailbedarfdruck.setBSortiertnachArtikel(1);
		} else {
			autoRahmendetailbedarfdruck.setBSortiertnachArtikel(0);
		}
		return autoRahmendetailbedarfdruck;
	}

}
