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

import com.lp.server.system.ejb.AutoMahnungsversand;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.AutoMahnungsversandDto;
import com.lp.server.system.service.AutoMahnungsversandDtoAssembler;
import com.lp.server.system.service.AutoMahnungsversandFac;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;

@Stateless
public class AutoMahnungsversandFacBean extends Facade implements AutoMahnungsversandFac {

	@PersistenceContext
	private EntityManager em;


	public AutoMahnungsversandDto autoMahnungsversandFindByMandantCNr(
			String mandantCNr) throws RemoteException {
		Query query = em.createNamedQuery("AutoMahnungsversandfindByMandantCNr");
		query.setParameter(1, mandantCNr);
		return assembleAutoMahnungsversandDto( (AutoMahnungsversand)query.getSingleResult());
	}


	public AutoMahnungsversandDto autoMahnungsversandFindByPrimaryKey(Integer id)
	throws RemoteException {
		AutoMahnungsversand autoMahnungsversand = em.find(AutoMahnungsversand.class, id);
		if(autoMahnungsversand==null){
			return null;
		}
		return assembleAutoMahnungsversandDto(autoMahnungsversand);
	}


	public void createAutoMahnungsversand(
			AutoMahnungsversandDto autoMahnungsversandDto)
	throws RemoteException {
		AutoMahnungsversand autoMahnungsversand = new AutoMahnungsversand();
		Integer pk = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_AUTOMATIKMAHNUNGSVERSAND);
		autoMahnungsversand.setIId(pk);
		autoMahnungsversand = setAutoMahnungsversandFromAutoMahnungsversandDto(autoMahnungsversand,
				autoMahnungsversandDto);
		em.persist(autoMahnungsversand);
		em.flush();

	}


	public void removeAutoMahnungsversand(Integer id) throws RemoteException {
		AutoMahnungsversand toRemove = em.find(AutoMahnungsversand.class, id);
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


	public void removeAutoMahnungsversand(
			AutoMahnungsversandDto autoMahnungsversandDto)
	throws RemoteException {
		removeAutoMahnungsversand(autoMahnungsversandDto.getIId());

	}


	public void updateAutoMahnungsversand(
			AutoMahnungsversandDto autoMahnungsversandDto)
	throws RemoteException {
		AutoMahnungsversand autoMahnungsversand = em.find(AutoMahnungsversand.class, autoMahnungsversandDto.getIId());
		autoMahnungsversand = setAutoMahnungsversandFromAutoMahnungsversandDto(autoMahnungsversand,
				autoMahnungsversandDto);
		em.merge(autoMahnungsversand);
		em.flush();

	}


	public void updateAutoMahnungsversands(
			AutoMahnungsversandDto[] autoMahnungsversandDtos)
	throws RemoteException {
		for(int i=0;i<autoMahnungsversandDtos.length;i++){
			AutoMahnungsversand autoMahnungsversand = em.find(AutoMahnungsversand.class, autoMahnungsversandDtos[i].getIId());
			autoMahnungsversand = setAutoMahnungsversandFromAutoMahnungsversandDto(autoMahnungsversand,
					autoMahnungsversandDtos[i]);
			em.merge(autoMahnungsversand);
			em.flush();
		}

	}

	private AutoMahnungsversandDto assembleAutoMahnungsversandDto(AutoMahnungsversand
			autoMahnungsversand) {
		return AutoMahnungsversandDtoAssembler.createDto(autoMahnungsversand);
	}

	private AutoMahnungsversand setAutoMahnungsversandFromAutoMahnungsversandDto(AutoMahnungsversand
			autoMahnungsversand, AutoMahnungsversandDto autoMahnungsversandDto) {
		autoMahnungsversand.setIId(autoMahnungsversand.getIId());
		autoMahnungsversand.setMandantCNr(autoMahnungsversandDto.getMandantCNr());
		autoMahnungsversand.setMahnlaufIId(autoMahnungsversandDto.getMahnlaufIId());
		autoMahnungsversand.setCDrucker(autoMahnungsversandDto.getCDrucker());
		autoMahnungsversand.setCVersandart(autoMahnungsversandDto.getCVersandart());
		return autoMahnungsversand;
	}


}
