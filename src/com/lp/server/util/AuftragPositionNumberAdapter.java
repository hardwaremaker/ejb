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
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.lp.server.artikel.ejb.Artikel;
import com.lp.server.auftrag.ejb.Auftragposition;
import com.lp.server.system.ejbfac.HvCreatingCachingProvider;
import com.lp.util.Helper;

public class AuftragPositionNumberAdapter extends PositionNumberCachingAdapter
		implements Serializable {
	private static final long serialVersionUID = 1348423071950648840L;

	private EntityManager em;
	private Auftragposition auftragPos;
	private HvCreatingCachingProvider<Integer, Artikel> artikelCache;
	
	protected AuftragPositionNumberAdapter() {
	}

	public AuftragPositionNumberAdapter(EntityManager em) {
		this.em = em;
	}

	public AuftragPositionNumberAdapter(Auftragposition auftragPos) {
		this.auftragPos = auftragPos;
	}

	@Override
	public Object getAdaptee() {
		return auftragPos;
	}
	
	@Override
	public void setAdaptee(Object adaptee) {
		auftragPos = (Auftragposition) adaptee;
	}

	@Override
	public Integer getIId() {
		return auftragPos.getIId();
	}

	@Override
	public Integer getPositionIId() {
		return auftragPos.getPositionIId();
	}

	@Override
	public String getPositionartCNr() {
		return auftragPos.getAuftragpositionartCNr();
	}

	@Override
	public String getCZbez() {
		return auftragPos.getCZusatzbezeichnung();
	}

	@Override
	public String getTypCNr() {
		return auftragPos.getTypCNr();
	}

	@Override
	public Integer getHeadIIdFromPosition(Integer posIId) {
		if (null == posIId)
			return null;

		Auftragposition ap = em.find(Auftragposition.class, posIId);
		if (null == ap)
			return null;

		return ap.getAuftragIId();
	}

	@Override
	public Integer getPositionIIdArtikelset() {
		return auftragPos.getPositionIIdArtikelset();
	}

	@Override
	public Integer getAuftragPositionIId() {
		// Kein Bezug zu einer Auftragposition moeglich, weil selbst eine
		// Auftragposition
		return null;
	}

	@Override
	public Iterator<?> getPositionsIteratorForAnyPosition(Integer anyPosIId) {
		return getPositionsIteratorForHeadIId(getHeadIIdFromPosition(anyPosIId));
	}


	private Artikel findArtikel(Integer artikelId) {
		if(artikelCache == null) {
			artikelCache = new HvCreatingCachingProvider<Integer, Artikel>() {@Override
				protected Artikel provideValue(Integer key, Integer transformedKey) {
					Artikel a = em.find(Artikel.class, auftragPos.getArtikelIId());
					System.out.println("Caching item id '" + key + "' => " + a.getCNr() + " (" + artikelCache.getSize() + ").");
					return a;
				}
			};
		}
		return artikelCache.getValueOfKey(artikelId);
	}
	
	@Override
	public boolean isIdent() {
//		return true ;
//		Artikel a = em.find(Artikel.class, auftragPos.getArtikelIId());
		Artikel a = findArtikel(auftragPos.getArtikelIId());
		return !Helper.short2boolean(a.getBKalkulatorisch()) ;
	}	

	@Override
	protected Iterator<?> getPositionsIteratorForHeadIIdImpl(
			Integer headIIdNotNull) {
		return getPositionsListForHeadIIdImpl(headIIdNotNull).iterator();
//		Query query = em.createNamedQuery("AuftragpositionfindByAuftrag");
//		query.setParameter(1, headIIdNotNull);
//		return query.getResultList().iterator() ;
	}
	
	@Override
	protected List<?> getPositionsListForHeadIIdImpl(Integer headIIdNotNull) {
		Query query = em.createNamedQuery("AuftragpositionfindByAuftrag");
		query.setParameter(1, headIIdNotNull);
		return query.getResultList() ;
	}
	
	@Override
	public List<?> getPositionsListForHeadIId(Integer headIId) {
		return getPositionsListForHeadIIdImpl(headIId);
	}
}
