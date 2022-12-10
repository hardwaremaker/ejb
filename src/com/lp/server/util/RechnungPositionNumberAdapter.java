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
package com.lp.server.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.lp.server.rechnung.ejb.Rechnungposition;

public class RechnungPositionNumberAdapter extends PositionNumberAdapter implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 251789133930202615L;

	@PersistenceContext
	private EntityManager em;

	private Rechnungposition rechnungPos ;
	
	protected RechnungPositionNumberAdapter() {}
	
	public RechnungPositionNumberAdapter(EntityManager em) {
		this.em = em ;
	}
	
	public RechnungPositionNumberAdapter(Rechnungposition rechnungPos) {
		this.rechnungPos = rechnungPos ;
	}
	
	public void setAdaptee(Object adaptee) {
		rechnungPos = (Rechnungposition) adaptee ;
	}
	
	@Override
	public Object getAdaptee() {
		return rechnungPos ;
	}
	
	@Override
	public Integer getPositionIId() {
		return rechnungPos.getPositionIId() ;
	}

	@Override
	public String getPositionartCNr() {
		return rechnungPos.getPositionsartCNr() ;
	}

	@Override
	public String getCZbez() {
		return rechnungPos.getCZbez() ;
	}

	@Override
	public String getTypCNr() {
		return rechnungPos.getTypCNr() ;
	}

	@Override
	public Integer getAuftragPositionIId() {
		// Eine Rechnungsposition hat keinen Bezug zu einer Auftragsposition
		return null ;
	}
	
	@Override
	public Integer getHeadIIdFromPosition(Integer posIId) {
		if(null == posIId) return null ;
		
		Rechnungposition ap = em.find(Rechnungposition.class, posIId);
		if(null == ap) return null ;
		
		return ap.getRechnungIId() ;
	}

	@Override
	public Integer getPositionIIdArtikelset() {
		return rechnungPos.getPositionIIdArtikelset() ;
	}

	@Override
	public Integer getIId() {
		return rechnungPos.getIId() ;
	}

	@Override
	public Iterator<?> getPositionsIteratorForAnyPosition(Integer anyPosIId) {
		return getPositionsIteratorForHeadIId(getHeadIIdFromPosition(anyPosIId)) ; 
	}

	@Override
	public Iterator<?> getPositionsIteratorForHeadIId(Integer rechnungIId) {
		if(null == rechnungIId) return new ArrayList().iterator();
		return getPositionsListForHeadIId(rechnungIId).iterator();
//		
//		try {
//			Query query = em.createNamedQuery("RechnungPositionfindByRechnungIId");
//			query.setParameter(1, rechnungIId);
//			return query.getResultList().iterator(); 
//		} catch(NoResultException e) {	
//		}
//		
//		return new ArrayList().iterator() ;
	}

	@Override
	public List<?> getPositionsListForHeadIId(Integer headIId) {
		if(null == headIId) return new ArrayList() ;
		Query query = em.createNamedQuery("RechnungPositionfindByRechnungIId");
		query.setParameter(1, headIId);
		return query.getResultList(); 
	}	
}
