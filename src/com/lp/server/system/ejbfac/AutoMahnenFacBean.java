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

import com.lp.server.system.ejb.AutoMahnen;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.AutoMahnenDto;
import com.lp.server.system.service.AutoMahnenDtoAssembler;
import com.lp.server.system.service.AutoMahnenFac;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;

@Stateless
public class AutoMahnenFacBean extends Facade implements AutoMahnenFac {
	
	@PersistenceContext
	private EntityManager em;

	public AutoMahnenDto autoMahnenFindByMandantCNr(String mandantCNr)
			throws RemoteException {
		Query query = em.createNamedQuery("AutoMahnenfindByMandantCNr");
		query.setParameter(1, mandantCNr);
		return AutoMahnenDtoAssembler.createDto((AutoMahnen) query.getSingleResult());
	}

	public AutoMahnenDto autoMahnenFindByPrimaryKey(Integer id)
			throws RemoteException {
		AutoMahnen autoMahnen = em.find(AutoMahnen.class, id);
		if(autoMahnen==null){
			return null;
		}
		return AutoMahnenDtoAssembler.createDto(autoMahnen);
	}

	public void createAutoMahnen(AutoMahnenDto autoMahnenDto)
			throws RemoteException {
		AutoMahnen autoMahnen = new AutoMahnen();
		Integer pk = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_AUTOMAHNEN);
		autoMahnenDto.setIId(pk);
		autoMahnen = setAutoMahnenFromAutoMahnenDto(autoMahnen, autoMahnenDto);
		em.persist(autoMahnen);
		em.flush();
	}

	public void removeAutoMahnen(Integer id) throws RemoteException {
		AutoMahnen toRemove = em.find(AutoMahnen.class, id);
		if(toRemove==null){
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try{
			em.remove(toRemove);
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void removeAutoMahnen(AutoMahnenDto autoMahnenDto)
			throws RemoteException {
		removeAutoMahnen(autoMahnenDto.getIId());

	}

	public void updateAutoMahnen(AutoMahnenDto autoMahnenDto)
			throws RemoteException {
		AutoMahnen autoMahnen = em.find(AutoMahnen.class, autoMahnenDto.getIId());
		autoMahnen = setAutoMahnenFromAutoMahnenDto(autoMahnen, autoMahnenDto);
		em.merge(autoMahnen);
		em.flush();
	}

	public void updateAutoMahnens(AutoMahnenDto[] autoMahnenDtos)
			throws RemoteException {
		for(int i=0;i<autoMahnenDtos.length;i++){
			updateAutoMahnen(autoMahnenDtos[i]);
		}

	}
	
	private AutoMahnen setAutoMahnenFromAutoMahnenDto(AutoMahnen autoMahnen,AutoMahnenDto autoMahnenDto){
		autoMahnen.setIId(autoMahnenDto.getIId());
		autoMahnen.setMandantCNr(autoMahnenDto.getMandantCNr());
		if(autoMahnenDto.getBAbMahnen() == null){
			autoMahnen.setBAbmahnen(0);
		} else {
			if(autoMahnenDto.getBAbMahnen()){
				autoMahnen.setBAbmahnen(1);
			} else {
				autoMahnen.setBAbmahnen(0);
			}
		}
		if(autoMahnenDto.getBLieferMahnen()==null){
			autoMahnen.setBLieferMahnen(0);
		} else {
			if(autoMahnenDto.getBLieferMahnen()){
				autoMahnen.setBLieferMahnen(1);
			} else {
				autoMahnen.setBLieferMahnen(0);
			}
		}
		return autoMahnen;
	}

}
