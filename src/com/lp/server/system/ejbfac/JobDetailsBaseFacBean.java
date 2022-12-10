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

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.system.ejb.JobDetailsEntity;
import com.lp.server.system.service.JobDetailsDto;
import com.lp.server.system.service.JobDetailsFac;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;

/**
 * Generische FacBean fuer Automatikjobs mit generischer Standardfunktionalitaet
 * von Datenbankabfragen. In der abgeleiteten FacBean des konkreten Jobs
 * muessen nur mehr die Methoden fuer der Erzeugung der konkreten Dtos und Entities
 * implementiert werden.
 * Zur Typsicherheit muessen die Dtos und Entities von den jeweiligen JobDetailsklassen
 * ableiten.
 * 
 * @author andi
 *
 * @param <T>
 * @param <E>
 */
public abstract class JobDetailsBaseFacBean<T extends JobDetailsDto, E extends JobDetailsEntity> extends Facade implements JobDetailsFac<T> {

	@PersistenceContext
	private EntityManager em;
	
	private String findQuery;
	private Class<E> entityClass;
	private String pkKeyName;

	protected JobDetailsBaseFacBean(Class<E> entityClass, String query, String pkKeyName) {
		findQuery = query;
		this.entityClass = entityClass;
		this.pkKeyName = pkKeyName;
	}
	
	protected abstract T assembleDto(E entity);

	protected abstract E setEntityFromDto(E entity, T dto);

	@Override
	public T findByMandantCNr(String mandantCNr) {
		Query query = em.createNamedQuery(findQuery);
		query.setParameter(1, mandantCNr);
		return assembleDto((E)query.getSingleResult());
	}

	@Override
	public T findByMandantCNrNoEx(String mandantCNr) {
		HvTypedQuery<E> hvQuery = new HvTypedQuery<E>(em.createNamedQuery(findQuery));
		hvQuery.setParameter(1, mandantCNr);
		E entity = hvQuery.getSingleResultNoEx();
		
		return entity != null ? assembleDto(entity) : null;
	}
	
	@Override
	public T findByPrimaryKey(Integer iId) {
		E entity = findByPrimaryKeyImpl(iId);
		
		return entity == null ? null : assembleDto(entity);
	}

	/**
	 * @param iId
	 * @return Entity
	 */
	protected E findByPrimaryKeyImpl(Integer iId) {
		E entity = (E) em.find(entityClass, iId);
		return entity;
	}

	@Override
	public Integer create(T dto) {
		Integer pk = getPKGeneratorObj().getNextPrimaryKey(pkKeyName);

		E entity;
		try {
			entity = entityClass.newInstance();
			entity.setiId(pk);
			entity.setMandantCNr(dto.getMandantCNr());
			entity = setEntityFromDto(entity, dto);

			em.persist(entity);
			em.flush();
			
			return pk;
		} catch (InstantiationException e) {
			myLogger.error("InstantiationException", e);
		} catch (IllegalAccessException e) {
			myLogger.error("IllegalAccessException", e);
		}
		
		return null;
	}

	@Override
	public void remove(Integer iId) {
		E entity = findByPrimaryKeyImpl(iId);
		if (entity == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, iId.toString());
		}
		try {
			em.remove(entity);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
	}

	@Override
	public void update(T dto) {
		E entity = findByPrimaryKeyImpl(dto.getiId());
		entity = setEntityFromDto(entity, dto);
		em.merge(entity);
		em.flush();
	}

}
