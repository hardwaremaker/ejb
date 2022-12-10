
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

import com.lp.server.system.ejb.AutoLoseerledigen;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.AutoLoseerledigenDto;
import com.lp.server.system.service.AutoLoseerledigenDtoAssembler;
import com.lp.server.system.service.AutoLoseerledigenFac;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;

@Stateless
public class AutoLoseerledigenFacBean extends Facade implements AutoLoseerledigenFac {

	@PersistenceContext
	private EntityManager em;


	public AutoLoseerledigenDto autoLoseerledigenFindByMandantCNr(
			String mandantCNr) throws RemoteException {
		Query query = em.createNamedQuery("AutoLoseerledigenfindByMandantCNr");
		query.setParameter(1, mandantCNr);
		return assembleAutoLoseerledigenDto((AutoLoseerledigen) query.getSingleResult());
	}


	public AutoLoseerledigenDto autoLoseerledigenFindByPrimaryKey(
			Integer id) throws RemoteException {
		AutoLoseerledigen autoLoseerledigen = em.find(AutoLoseerledigen.class, id);
		if(autoLoseerledigen==null){
			return null;
		}
		return assembleAutoLoseerledigenDto(autoLoseerledigen);
	}


	public void createAutoLoseerledigen(
			AutoLoseerledigenDto autoLoseerledigenDto)
	throws RemoteException ,EJBExceptionLP {
		if (autoLoseerledigenDto == null) {
			return;
		}
		if(autoLoseerledigenDto.getIId() == null){
			Integer iId = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_AUTOMATIKBESTELLVORSCHLAG);
			autoLoseerledigenDto.setIId(iId);
		}

		try {
			AutoLoseerledigen autoLoseerledigen = new AutoLoseerledigen();
			autoLoseerledigen.setIId(autoLoseerledigenDto.getIId());
			autoLoseerledigen = setAutoLoseerledigenFromAutoLoseerledigenDto(autoLoseerledigen,
					autoLoseerledigenDto);
			em.persist(autoLoseerledigen);
			em.flush();
		}
		catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE,e);
		}

	}


	public void removeAutoLoseerledigen(Integer id) throws RemoteException {
		AutoLoseerledigen toRemove = em.find(AutoLoseerledigen.class, id);
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


	public void removeAutoLoseerledigen(
			AutoLoseerledigenDto autoLoseerledigenDto)
	throws RemoteException {
		removeAutoLoseerledigen(autoLoseerledigenDto.getIId());

	}


	public void updateAutoLoseerledigen(
			AutoLoseerledigenDto autoLoseerledigenDto)
	throws RemoteException {
		AutoLoseerledigen autoLoseerledigen = em.find(AutoLoseerledigen.class, autoLoseerledigenDto.getIId());
		autoLoseerledigen = setAutoLoseerledigenFromAutoLoseerledigenDto(autoLoseerledigen,
				autoLoseerledigenDto);
		em.merge(autoLoseerledigen);
		em.flush();

	}


	public void updateAutoLoseerledigens(
			AutoLoseerledigenDto[] autoLoseerledigenDtos)
	throws RemoteException {
		
		for(int i=0;i<autoLoseerledigenDtos.length;i++){
			AutoLoseerledigen autoLoseerledigen = em.find(AutoLoseerledigen.class, autoLoseerledigenDtos[i].getIId());
			autoLoseerledigen = setAutoLoseerledigenFromAutoLoseerledigenDto(autoLoseerledigen,
					autoLoseerledigenDtos[i]);
			em.merge(autoLoseerledigen);
			em.flush();
		}

	}

	private AutoLoseerledigen setAutoLoseerledigenFromAutoLoseerledigenDto(AutoLoseerledigen
			autoLoseerledigen, AutoLoseerledigenDto autoLoseerledigenDto) {
		autoLoseerledigen.setMandantCNr(autoLoseerledigenDto.getMandantCNr());
		autoLoseerledigen.setIArbeitstage(autoLoseerledigenDto.getIArbeitstage());
		return autoLoseerledigen;
	}

	private AutoLoseerledigenDto assembleAutoLoseerledigenDto(AutoLoseerledigen
			autoLoseerledigen) {
		return AutoLoseerledigenDtoAssembler.createDto(autoLoseerledigen);
	}


}
