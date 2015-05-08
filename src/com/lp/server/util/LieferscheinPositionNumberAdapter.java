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
import java.util.Iterator;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.lp.server.lieferschein.ejb.Lieferscheinposition;

public class LieferscheinPositionNumberAdapter extends PositionNumberAdapter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -165178433144854538L;

	@PersistenceContext
	private EntityManager em;

	private Lieferscheinposition lieferscheinPos ;

	public LieferscheinPositionNumberAdapter(EntityManager em) {
		this.em = em ;
	}
	
	@Override
	public void setAdaptee(Object adaptee) {
		lieferscheinPos = (Lieferscheinposition) adaptee ;
	}

	@Override
	public Integer getIId() {
		return lieferscheinPos.getIId() ;
	}

	@Override
	public Integer getPositionIId() {
		return lieferscheinPos.getPositionIId() ;
	}

	@Override
	public String getPositionartCNr() {
		return lieferscheinPos.getLieferscheinpositionartCNr() ;
	}

	@Override
	public String getCZbez() {
		return lieferscheinPos.getCZbez();
	}

	@Override
	public String getTypCNr() {
		return lieferscheinPos.getTypCNr() ;
	}

	@Override
	public Integer getHeadIIdFromPosition(Integer posIId) {
		if(posIId == null) return null ;
		
		Lieferscheinposition ap = em.find(Lieferscheinposition.class, posIId) ;
		if(ap == null) return null ;
		return ap.getLieferscheinIId() ;
	}

	@Override
	public Integer getPositionIIdArtikelset() {
		return lieferscheinPos.getPositionIIdArtikelset() ;
	}

	@Override
	public Integer getAuftragPositionIId() {
		return lieferscheinPos.getAuftragpositionIId() ;
	}

	@Override
	public Iterator<?> getPositionsIteratorForAnyPosition(Integer anyPosIId) {
		return getPositionsIteratorForHeadIId(getHeadIIdFromPosition(anyPosIId)) ; 
	}

	@Override
	public Iterator<?> getPositionsIteratorForHeadIId(Integer headIId) {
		if(headIId == null) return null ;
		
		try {
			Query query = em.createNamedQuery("LieferscheinpositionfindByLieferschein");
			query.setParameter(1, headIId);
			return query.getResultList().iterator(); 
		} catch(NoResultException e) {	
		}
		
		return null ;
	}

}
