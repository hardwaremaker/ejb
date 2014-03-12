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
package com.lp.server.system.ejbfac;

import java.rmi.RemoteException;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.lp.server.system.ejb.AutoFehlmengendruck;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.AutoFehlmengendruckDto;
import com.lp.server.system.service.AutoFehlmengendruckDtoAssembler;
import com.lp.server.system.service.AutoFehlmengendruckFac;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;


@Stateless
public class AutoFehlmengendruckFacBean extends Facade implements AutoFehlmengendruckFac{


	@PersistenceContext
	private EntityManager em;



	public AutoFehlmengendruckDto autoFehlmengendruckFindByMandantCNr(
			String mandantCNr) throws RemoteException {
		Query query = em.createNamedQuery("AutoFehlmengendruckfindByMandantCNr");
		query.setParameter(1, mandantCNr);
		return assembleAutoFehlmengendruckDto((AutoFehlmengendruck)query.getSingleResult());
	}


	public AutoFehlmengendruckDto autoFehlmengendruckFindByPrimaryKey(Integer id)
	throws RemoteException {
		AutoFehlmengendruck autoFehlmengendruck = em.find(AutoFehlmengendruck.class, id);
		if(autoFehlmengendruck==null){
			return null;
		}
		return assembleAutoFehlmengendruckDto(autoFehlmengendruck);
	}


	public void createAutoFehlmengendruck(
			AutoFehlmengendruckDto autoFehlmengendruckDto)
	throws RemoteException {
		AutoFehlmengendruck autoFehlmengendruck = new AutoFehlmengendruck();
		Integer pk = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_AUTOMATIKFEHLMENGENDRUCK);
		autoFehlmengendruck.setIId(pk);
		autoFehlmengendruck = setAutoFehlmengendruckFromAutoFehlmengendruckDto(autoFehlmengendruck,
				autoFehlmengendruckDto);

		em.persist(autoFehlmengendruck);
		em.flush();

	}


	public void removeAutoFehlmengendruck(Integer id) throws RemoteException {
		AutoFehlmengendruck toRemove = em.find(AutoFehlmengendruck.class,id);
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


	public void removeAutoFehlmengendruck(
			AutoFehlmengendruckDto autoFehlmengendruckDto)
	throws RemoteException {
		removeAutoFehlmengendruck(autoFehlmengendruckDto.getIId());

	}


	public void updateAutoFehlmengendruck(
			AutoFehlmengendruckDto autoFehlmengendruckDto)
	throws RemoteException {
		AutoFehlmengendruck autoFehlmengendruck = em.find(AutoFehlmengendruck.class, autoFehlmengendruckDto.getIId());
		autoFehlmengendruck = setAutoFehlmengendruckFromAutoFehlmengendruckDto(autoFehlmengendruck,
				autoFehlmengendruckDto);
		em.merge(autoFehlmengendruck);
		em.flush();

	}


	public void updateAutoFehlmengendrucks(
			AutoFehlmengendruckDto[] autoFehlmengendruckDtos)
	throws RemoteException {
		for(int i=0;i<autoFehlmengendruckDtos.length;i++){
			AutoFehlmengendruck autoFehlmengendruck = em.find(AutoFehlmengendruck.class, autoFehlmengendruckDtos[i].getIId());
			autoFehlmengendruck = setAutoFehlmengendruckFromAutoFehlmengendruckDto(autoFehlmengendruck,
					autoFehlmengendruckDtos[i]);
			em.merge(autoFehlmengendruck);
			em.flush();
		}

	}

	private AutoFehlmengendruck setAutoFehlmengendruckFromAutoFehlmengendruckDto(AutoFehlmengendruck
			autoFehlmengendruck, AutoFehlmengendruckDto autoFehlmengendruckDto) {
		autoFehlmengendruck.setMandantCNr(autoFehlmengendruckDto.getMandantCNr());
		autoFehlmengendruck.setCDrucker(autoFehlmengendruckDto.getCDrucker());
		return autoFehlmengendruck;
	}

	private AutoFehlmengendruckDto assembleAutoFehlmengendruckDto(AutoFehlmengendruck
			autoFehlmengendruck) {
		return AutoFehlmengendruckDtoAssembler.createDto(autoFehlmengendruck);
	}

}
