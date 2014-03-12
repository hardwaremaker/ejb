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

import com.lp.server.system.ejb.AutoBestellvorschlag;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.AutoBestellvorschlagDto;
import com.lp.server.system.service.AutoBestellvorschlagDtoAssembler;
import com.lp.server.system.service.AutoBestellvorschlagFac;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;

@Stateless
public class AutoBestellvorschlagFacBean extends Facade implements AutoBestellvorschlagFac {

	@PersistenceContext
	private EntityManager em;


	public AutoBestellvorschlagDto autoBestellvorschlagFindByMandantCNr(
			String mandantCNr) throws RemoteException {
		Query query = em.createNamedQuery("AutoBestellvorschlagfindByMandantCNr");
		query.setParameter(1, mandantCNr);
		return assembleAutoBestellvorschlagDto((AutoBestellvorschlag) query.getSingleResult());
	}


	public AutoBestellvorschlagDto autoBestellvorschlagFindByPrimaryKey(
			Integer id) throws RemoteException {
		AutoBestellvorschlag autoBestellvorschlag = em.find(AutoBestellvorschlag.class, id);
		if(autoBestellvorschlag==null){
			return null;
		}
		return assembleAutoBestellvorschlagDto(autoBestellvorschlag);
	}


	public void createAutoBestellvorschlag(
			AutoBestellvorschlagDto autoBestellvorschlagDto)
	throws RemoteException ,EJBExceptionLP {
		if (autoBestellvorschlagDto == null) {
			return;
		}
		if(autoBestellvorschlagDto.getIId() == null){
			Integer iId = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_AUTOMATIKBESTELLVORSCHLAG);
			autoBestellvorschlagDto.setIId(iId);
		}

		try {
			AutoBestellvorschlag autoBestellvorschlag = new AutoBestellvorschlag();
			autoBestellvorschlag.setIId(autoBestellvorschlagDto.getIId());
			autoBestellvorschlag = setAutoBestellvorschlagFromAutoBestellvorschlagDto(autoBestellvorschlag,
					autoBestellvorschlagDto);
			em.persist(autoBestellvorschlag);
			em.flush();
		}
		catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE,e);
		}

	}


	public void removeAutoBestellvorschlag(Integer id) throws RemoteException {
		AutoBestellvorschlag toRemove = em.find(AutoBestellvorschlag.class, id);
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


	public void removeAutoBestellvorschlag(
			AutoBestellvorschlagDto autoBestellvorschlagDto)
	throws RemoteException {
		removeAutoBestellvorschlag(autoBestellvorschlagDto.getIId());

	}


	public void updateAutoBestellvorschlag(
			AutoBestellvorschlagDto autoBestellvorschlagDto)
	throws RemoteException {
		AutoBestellvorschlag autoBestellvorschlag = em.find(AutoBestellvorschlag.class, autoBestellvorschlagDto.getIId());
		autoBestellvorschlag = setAutoBestellvorschlagFromAutoBestellvorschlagDto(autoBestellvorschlag,
				autoBestellvorschlagDto);
		em.merge(autoBestellvorschlag);
		em.flush();

	}


	public void updateAutoBestellvorschlags(
			AutoBestellvorschlagDto[] autoBestellvorschlagDtos)
	throws RemoteException {
		
		for(int i=0;i<autoBestellvorschlagDtos.length;i++){
			AutoBestellvorschlag autoBestellvorschlag = em.find(AutoBestellvorschlag.class, autoBestellvorschlagDtos[i].getIId());
			autoBestellvorschlag = setAutoBestellvorschlagFromAutoBestellvorschlagDto(autoBestellvorschlag,
					autoBestellvorschlagDtos[i]);
			em.merge(autoBestellvorschlag);
			em.flush();
		}

	}

	private AutoBestellvorschlag setAutoBestellvorschlagFromAutoBestellvorschlagDto(AutoBestellvorschlag
			autoBestellvorschlag, AutoBestellvorschlagDto autoBestellvorschlagDto) {
		autoBestellvorschlag.setMandantCNr(autoBestellvorschlagDto.getMandantCNr());
		autoBestellvorschlag.setCDrucker(autoBestellvorschlagDto.getCDrucker());
		return autoBestellvorschlag;
	}

	private AutoBestellvorschlagDto assembleAutoBestellvorschlagDto(AutoBestellvorschlag
			autoBestellvorschlag) {
		return AutoBestellvorschlagDtoAssembler.createDto(autoBestellvorschlag);
	}


}
