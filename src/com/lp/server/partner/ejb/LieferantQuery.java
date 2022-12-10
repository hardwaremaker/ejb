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
package com.lp.server.partner.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;

public class LieferantQuery {
	
	public static final String ByMandantCnr = "LieferantFindByMandantCnr";
	public static final String MaxFremdsystemnr = "LieferantMaxFremdsystemnr";
	public static final String ByMandantCnrFremdsystemnr = "LieferantFindByMandantCnrFremdsystemnr";
	public static final String IIdsByPartnerIIds = "LieferantIIdsByPartnerIIds";
	
	public static HvTypedQuery<Lieferant> byMandantCnr(EntityManager em, String mandantCnr) {
		HvTypedQuery<Lieferant> theQuery = new HvTypedQuery<Lieferant>(em.createNamedQuery(ByMandantCnr));
		theQuery.setParameter("mandant", mandantCnr);
		
		return theQuery;
	}
	
	public static List<Lieferant> listByMandantCnr(EntityManager em, String mandantCnr) {
		return byMandantCnr(em, mandantCnr).getResultList();
	}

	public static HvTypedQuery<Integer> maxFremdsystemnr(EntityManager em) {
		HvTypedQuery<Integer> theQuery = new HvTypedQuery<Integer>(em.createNamedQuery(LieferantQuery.MaxFremdsystemnr));
		
		return theQuery;
	}
	
	public static Integer resultMaxFremdsystemnr(EntityManager em) {
		return maxFremdsystemnr(em).getSingleResultNoEx();
	}

	public static HvTypedQuery<Lieferant> byMandantCNrFremdsystemnr(EntityManager em, String mandant, String fremdsystemnr) {
		HvTypedQuery<Lieferant> theQuery = new HvTypedQuery<Lieferant>(em.createNamedQuery(LieferantQuery.ByMandantCnrFremdsystemnr));
		return theQuery.setParameter("mandant", mandant)
					.setParameter("fremdsystemnr", fremdsystemnr);
	}
	
	public static Lieferant resultByMandantCNrFremdsystemnr(EntityManager em, String mandant, String fremdsystemnr) {
		return byMandantCNrFremdsystemnr(em, mandant, fremdsystemnr).getSingleResultNoEx();
	}
	
	public static HvTypedQuery<Integer> iIdsByPartnerIIds(EntityManager em, Collection<Integer> partnerIds) {
		HvTypedQuery<Integer> theQuery = new HvTypedQuery<Integer>(em.createNamedQuery(IIdsByPartnerIIds));
		theQuery.setParameter("partnerIds", partnerIds);
		return theQuery;
	}
	
	public static List<Integer> listIIdsByIban(EntityManager em, String iban) {
		List<Integer> partnerIds = PartnerbankQuery.listPartnerIIdsByIban(em, iban);
		if (partnerIds.isEmpty()) return new ArrayList<Integer>();
		
		return iIdsByPartnerIIds(em, partnerIds).getResultList();
	}
}
