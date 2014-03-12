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
package com.lp.server.util;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.util.EJBExceptionLP;

public abstract class TauscheISort<T> {
	private EntityManager em ;
	private Class<T> clazz ;
	
	public TauscheISort(EntityManager em, Class<T> theClass) {
		this.em = em ;
		clazz = theClass ;
	}
	
	/**
	 * Die ISorts der beiden angegebenen iids werden wechselseitig getauscht.
	 * 
	 * @param iId1 die IId des einen Datensatzes
	 * @param iId2 die IId des anderen Datensatzs
	 */
	public void tausche(Integer iId1, Integer iId2) throws EJBExceptionLP {
		Validator.pkFieldNotNull(iId1, "iId1") ;
		Validator.pkFieldNotNull(iId2, "iId2") ;
		
		IISort sort1 = getISortEntity(iId1) ;
		IISort sort2 = getISortEntity(iId2) ;
		
		Integer fromId = sort1.getISort() ;
		Integer otherId = sort2.getISort() ;

		sort1.setISort(-1) ;
		sort2.setISort(fromId) ;
		sort1.setISort(otherId) ;

		em.merge(sort1) ;
		em.merge(sort2) ;
		em.flush() ;
	}
	
	public Integer getISortBetweenIds(Integer startIId1, Integer stopIId2) throws EJBExceptionLP {
		Integer minSort = 0 ;
		Integer maxSort = Integer.MAX_VALUE ;
		
		if(startIId1 != null) {
			minSort = getISortEntity(startIId1).getISort() ;
		}
		if(stopIId2 != null) {			
			maxSort = getISortEntity(stopIId2).getISort() ;
		}
		
		if(minSort.equals(maxSort)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ISORT_DUPLICATE_UNIQUE, 
					"ISort bei " + clazz.getSimpleName() + " f\u00FCr iId1 " + startIId1 + " und iId2 " + stopIId2 + " identisch") ;
		}

		if(minSort > maxSort) {
			Integer i = minSort ;
			minSort = maxSort ;
			maxSort = i ;
		}
		
		int step = (maxSort - minSort) / 2 ;
		return step == 0 ? null : (minSort + step) ;
	}
	
	public Integer getNextISort(Integer iId) throws EJBExceptionLP {
		Validator.pkFieldNotNull(iId, "iId") ;	
		T myEntity = getEntity(iId) ;
		T nextEntity = findNextEntityISort(myEntity) ;
		return getISortBetweenIds(iId, nextEntity == null ? null : ((IISort)nextEntity).getISort()) ;
	}

	public Integer getNextISort() throws EJBExceptionLP {
		T lastEntity = findLastEntityISort() ;
		return getISortBetweenIds(lastEntity == null ? null : ((IIId) lastEntity).getIId(), null) ;
	}
	
	
	public Integer getPreviousISort(Integer iId) throws EJBExceptionLP {
		Validator.pkFieldNotNull(iId, "iId") ;	
		T myEntity = getEntity(iId) ;
		T previousEntity = findPreviousEntityISort(myEntity) ;
		return getISortBetweenIds(previousEntity == null ? null : ((IIId)previousEntity).getIId(), iId) ;		
	}
	
	
	public void renumber() {
		List<T> allEntities = findAllEntitiesISort() ;
		Integer newISort = 0 ; 
		for (T t : allEntities) {
			IISort entry = (IISort) t ;
			newISort += 1000 ;
			entry.setISort(newISort) ;
			
			getEm().merge(entry) ;
		}
		
		getEm().flush() ;
	}
	
	
	protected EntityManager getEm() {
		return em ;
	}
	
	/**
	 * Finde jene Entity, die unmittelbar nach der angegebene Entity basierend auf ihrem ISort Value liegt.
	 * 
	 * @param startEntity ist der Vorg&auml;nger der gesuchten Entity
	 * @return null wenn es keinen Nachfolger gibt, ansonsten den Nachfolger
	 */
	protected abstract T findNextEntityISort(T startEntity) ;
	
	protected abstract T findPreviousEntityISort(T startEntity) ;
	
	protected abstract List<T> findAllEntitiesISort() ;
	
	/**
	 * Finde die letzte existierende Entity, basierend auf der Sortierung nach ISort.
	 * 
	 * @return null wenn es noch keine Eintr&auml;ge gibt, ansonsten die Entity mit dem h&ouml;chsten ISort Value
	 */
	protected abstract T findLastEntityISort() ;
	
	private IISort getISortEntity(Integer id) throws EJBExceptionLP {
		IISort entity = (IISort) em.find(clazz, id) ;
		if(entity == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, 
					"Kein " + clazz.getSimpleName() + " mit iid '" + id + "' gefunden") ;
		}
		
		return entity ;
	}
	
	private T getEntity(Integer id) throws EJBExceptionLP {
		T entity = (T) em.find(clazz, id) ;
		if(entity == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, 
					"Kein " + clazz.getSimpleName() + " mit iid '" + id + "' gefunden") ;
		}
		
		return entity ;		
	}
}
