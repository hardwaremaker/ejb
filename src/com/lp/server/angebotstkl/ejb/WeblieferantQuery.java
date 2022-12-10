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
package com.lp.server.angebotstkl.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class WeblieferantQuery {
	
	public final static String ByWebpartnerIId = "WeblieferantFindByWebpartnerIId";
	public final static String SelectMaxISort = "WeblieferantSelectMaxISort";
	public final static String ByWebabfrageTypMandantCNr = "WeblieferantFindByWebabfrageTypMandantCNr";
	public final static String ByMandantCNr = "WeblieferantFindAll";
	
	public static HvTypedQuery<Weblieferant> byWebpartnerIId(EntityManager em, Integer webpartnerIId) {
		HvTypedQuery<Weblieferant> query = new HvTypedQuery<Weblieferant>(em.createNamedQuery(ByWebpartnerIId));
		query.setParameter("webpartnerIId", webpartnerIId);
		
		return query;
	}
	
	public static Weblieferant findByWebpartnerIId(EntityManager em, Integer webpartnerIId) {
		return byWebpartnerIId(em, webpartnerIId).getSingleResult();
	}
	
	public static HvTypedQuery<Integer> selectMaxISort(EntityManager em) {
		HvTypedQuery<Integer> query = new HvTypedQuery<Integer>(em.createNamedQuery(SelectMaxISort));
		
		return query;
	}
	
	public static Integer maxISort(EntityManager em) {
		return selectMaxISort(em).getSingleResult();
	}
	
	public static HvTypedQuery<Weblieferant> byWebabfrageTypMandantCNr(EntityManager em, Integer webabfrageTyp, String mandantCNr) {
		HvTypedQuery<Weblieferant> query = new HvTypedQuery<Weblieferant>(em.createNamedQuery(ByWebabfrageTypMandantCNr));
		query.setParameter("webabfrageTyp", webabfrageTyp);
		query.setParameter("mandantCNr", mandantCNr);
		
		return query;
	}
	
	public static List<Weblieferant> listByWebabfrageTypIIdMandantCNr(EntityManager em, Integer webabfrageTyp, String mandantCNr) {
		return byWebabfrageTypMandantCNr(em, webabfrageTyp, mandantCNr).getResultList();
	}

	public static HvTypedQuery<Weblieferant> byMandantCNr(EntityManager em, String mandantCNr) {
		HvTypedQuery<Weblieferant> query = new HvTypedQuery<Weblieferant>(em.createNamedQuery(ByMandantCNr));
		query.setParameter("mandantCNr", mandantCNr);
		
		return query;
	}
	
	public static List<Weblieferant> listByMandantCNr(EntityManager em, String mandantCNr) {
		return byMandantCNr(em, mandantCNr).getResultList();
	}
}
