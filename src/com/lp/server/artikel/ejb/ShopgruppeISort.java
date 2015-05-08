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
package com.lp.server.artikel.ejb;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.util.TauscheISort;

public class ShopgruppeISort extends TauscheISort<Shopgruppe> {
	private String mandantCnr = null ;
	
	public ShopgruppeISort(EntityManager em) {
		super(em, Shopgruppe.class) ;
	}

	public ShopgruppeISort(EntityManager em, String mandantCnr) {
		super(em, Shopgruppe.class) ;
		this.mandantCnr = mandantCnr ;
	}

	@Override
	protected Shopgruppe findNextEntityISort(Shopgruppe startEntity) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	@Override
	protected List<Shopgruppe> findAllEntitiesISort() {
		HvTypedQuery<Shopgruppe> theQuery = new HvTypedQuery<Shopgruppe>(
				getEm().createNamedQuery(Shopgruppe.QueryFindByMandantCNr)) ;
		theQuery.setParameter("mandant", mandantCnr) ;
		List<Shopgruppe> groups = theQuery.getResultList() ;
		return groups ;
	}

	@Override
	protected Shopgruppe findPreviousEntityISort(Shopgruppe startEntity) {
		HvTypedQuery<Shopgruppe> theQuery = new HvTypedQuery<Shopgruppe>(
				getEm().createNamedQuery(Shopgruppe.QueryFindPreviousISortByMandantCNr)) ;
		theQuery.setParameter("mandant", mandantCnr) ;
		theQuery.setParameter("isort", startEntity.getISort()) ;
		theQuery.setMaxResults(1) ;
		List<Shopgruppe> groups = theQuery.getResultList() ;
		return groups.size() > 0 ? groups.get(0) : null ;	
	}

	private Integer findMaxISort(String mandantCnr) {
		HvTypedQuery<Integer> theQuery = new HvTypedQuery<Integer>(getEm().createNamedQuery(Shopgruppe.QueryMaxISortByMandantCNr)) ;
		theQuery.setParameter("mandant", mandantCnr) ;
		Integer maxISort = null ;
		try {
			maxISort = theQuery.getSingleResult() ;
		} catch(NoResultException e) {
		}
		return maxISort ;
	}
	
	@Override
	protected Shopgruppe findLastEntityISort() {
		Integer maxISort = findMaxISort(mandantCnr) ;
		if(maxISort == null) return null ;
		
		HvTypedQuery<Shopgruppe> theQuery = new HvTypedQuery<Shopgruppe>(
				getEm().createNamedQuery(Shopgruppe.QueryFindByMandantCNrISort)) ;
		theQuery.setParameter("mandant", mandantCnr) ;
		theQuery.setParameter("isort", maxISort) ;
		
		theQuery.setMaxResults(1) ;
		List<Shopgruppe> groups = theQuery.getResultList() ;
		return groups.size() > 0 ? groups.get(0) : null ;
	}
}
