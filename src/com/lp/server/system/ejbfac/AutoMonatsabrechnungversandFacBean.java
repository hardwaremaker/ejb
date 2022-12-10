
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

import com.lp.server.system.ejb.AutoMonatsabrechnungversand;
import com.lp.server.system.ejb.AutoMonatsabrechnungversandAbteilungen;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.AutoMonatsabrechnungversandDto;
import com.lp.server.system.service.AutoMonatsabrechnungversandDtoAssembler;
import com.lp.server.system.service.AutoMonatsabrechnungversandFac;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;

@Stateless
public class AutoMonatsabrechnungversandFacBean extends Facade implements AutoMonatsabrechnungversandFac {

	@PersistenceContext
	private EntityManager em;


	public AutoMonatsabrechnungversandDto autoMonatsabrechnungversandFindByMandantCNr(
			String mandantCNr) {
		Query query = em.createNamedQuery("AutoMonatsabrechnungversandfindByMandantCNr");
		query.setParameter(1, mandantCNr);
		return AutoMonatsabrechnungversandDtoAssembler.createDto( (AutoMonatsabrechnungversand)query.getSingleResult());
	}

	public AutoMonatsabrechnungversandDto autoMonatsabrechnungversandAbteilungenfindByMandantCNr(
			String mandantCNr) {
		Query query = em.createNamedQuery("AutoMonatsabrechnungversandAbteilungenfindByMandantCNr");
		query.setParameter(1, mandantCNr);
		return AutoMonatsabrechnungversandDtoAssembler.createDto( (AutoMonatsabrechnungversandAbteilungen)query.getSingleResult());
	}


	public AutoMonatsabrechnungversandDto autoMonatsabrechnungversandFindByPrimaryKey(Integer id) {
		AutoMonatsabrechnungversand bean = em.find(AutoMonatsabrechnungversand.class, id);
		if(bean==null){
			return null;
		}
		return AutoMonatsabrechnungversandDtoAssembler.createDto(bean);
	}
	
	public AutoMonatsabrechnungversandDto autoMonatsabrechnungversandAbteilungenFindByPrimaryKey(Integer id) {
		AutoMonatsabrechnungversandAbteilungen bean = em.find(AutoMonatsabrechnungversandAbteilungen.class, id);
		if(bean==null){
			return null;
		}
		return AutoMonatsabrechnungversandDtoAssembler.createDto(bean);
	}


	public void createAutoMonatsabrechnungversand(
			AutoMonatsabrechnungversandDto dto) {
		AutoMonatsabrechnungversand bean = new AutoMonatsabrechnungversand();
		Integer pk = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_AUTOMATIKMONATSABRECHNUNGVERSAND);
		bean.setIId(pk);
		bean = setAutoMonatsabrechnungversandFromAutoMonatsabrechnungversandDto(bean,
				dto);
		em.persist(bean);
		em.flush();

	}
	public void createAutoMonatsabrechnungversandAbteilungen(
			AutoMonatsabrechnungversandDto dto) {
		AutoMonatsabrechnungversandAbteilungen bean = new AutoMonatsabrechnungversandAbteilungen();
		Integer pk = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_AUTOMATIKMONATSABRECHNUNGVERSANDABTEILUNGEN);
		bean.setIId(pk);
		bean = setAutoMonatsabrechnungversandAbteilungenFromAutoMonatsabrechnungversandAbteilungenDto(bean,
				dto);
		em.persist(bean);
		em.flush();

	}

	public void removeAutoMonatsabrechnungversand(Integer id) {
		AutoMonatsabrechnungversand toRemove = em.find(AutoMonatsabrechnungversand.class, id);
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
	public void removeAutoMonatsabrechnungversandAbteilungen(Integer id) {
		AutoMonatsabrechnungversandAbteilungen toRemove = em.find(AutoMonatsabrechnungversandAbteilungen.class, id);
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

	public void removeAutoMonatsabrechnungversand(
			AutoMonatsabrechnungversandDto dto) {
		removeAutoMonatsabrechnungversand(dto.getIId());

	}


	public void updateAutoMonatsabrechnungversand(
			AutoMonatsabrechnungversandDto dto) {
		AutoMonatsabrechnungversand bean = em.find(AutoMonatsabrechnungversand.class, dto.getIId());
		bean = setAutoMonatsabrechnungversandFromAutoMonatsabrechnungversandDto(bean,
				dto);
		em.merge(bean);
		em.flush();

	}
	public void updateAutoMonatsabrechnungversandAbteilungen(
			AutoMonatsabrechnungversandDto dto) {
		AutoMonatsabrechnungversandAbteilungen bean = em.find(AutoMonatsabrechnungversandAbteilungen.class, dto.getIId());
		bean = setAutoMonatsabrechnungversandAbteilungenFromAutoMonatsabrechnungversandAbteilungenDto(bean,
				dto);
		em.merge(bean);
		em.flush();

	}

	public void updateAutoMonatsabrechnungversands(
			AutoMonatsabrechnungversandDto[] dtos) {
		for(int i=0;i<dtos.length;i++){
			AutoMonatsabrechnungversand bean = em.find(AutoMonatsabrechnungversand.class, dtos[i].getIId());
			bean = setAutoMonatsabrechnungversandFromAutoMonatsabrechnungversandDto(bean,
					dtos[i]);
			em.merge(bean);
			em.flush();
		}

	}


	private AutoMonatsabrechnungversand setAutoMonatsabrechnungversandFromAutoMonatsabrechnungversandDto(AutoMonatsabrechnungversand
			bean, AutoMonatsabrechnungversandDto dto) {
		bean.setIId(bean.getIId());
		bean.setMandantCNr(dto.getMandantCNr());
		bean.setIWochentag(dto.getIWochentag());
		bean.setBMonatlich(dto.getBMonatlich());
		
		return bean;
	}

	private AutoMonatsabrechnungversandAbteilungen setAutoMonatsabrechnungversandAbteilungenFromAutoMonatsabrechnungversandAbteilungenDto(AutoMonatsabrechnungversandAbteilungen
			bean, AutoMonatsabrechnungversandDto dto) {
		bean.setIId(bean.getIId());
		bean.setMandantCNr(dto.getMandantCNr());
		bean.setIWochentag(dto.getIWochentag());
		bean.setBMonatlich(dto.getBMonatlich());
		
		return bean;
	}
}
