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
package com.lp.server.system.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class VersandwegPartnerQuery {
	public final static String ByVersandwegIIdPartnerIId = "VersandwegPartnerByVersandwegIIdPartnerIId" ;
	public final static String ByPartnerIId = "VersandwegPartnerByPartnerIId" ;
	public final static String ByPartnerIIdVersandwegCnr = "VersandwegPartnerByPartnerIIdVersandwegCnr" ;
	
	public static HvTypedQuery<VersandwegPartner> byVersandwegIIdPartnerIId(EntityManager em, Integer versandwegId, Integer partnerId, String mandantCnr) {
		HvTypedQuery<VersandwegPartner> query = new HvTypedQuery<VersandwegPartner>(em.createNamedQuery(ByVersandwegIIdPartnerIId)) ;
		query.setParameter("versandwegId", versandwegId) ;
		query.setParameter("partnerId", partnerId) ;
		query.setParameter("mandantCnr", mandantCnr);
		return query ;
	}

	public static HvTypedQuery<VersandwegPartner> byPartnerIId(EntityManager em, Integer partnerId, String mandantCnr) {
		HvTypedQuery<VersandwegPartner> query = new HvTypedQuery<VersandwegPartner>(em.createNamedQuery(ByPartnerIId)) ;
		query.setParameter("partnerId", partnerId) ;
		query.setParameter("mandantCnr", mandantCnr);
		return query ;
	}

	public static HvTypedQuery<VersandwegPartner> byPartnerIIdVersandwegCnr(EntityManager em, Integer partnerId, String versandwegCnr, String mandantCnr) {
		HvTypedQuery<VersandwegPartner> query = new HvTypedQuery<VersandwegPartner>(em.createNamedQuery(ByPartnerIIdVersandwegCnr)) ;
		query.setParameter("partnerId", partnerId) ;
		query.setParameter("versandwegCnr", versandwegCnr);
		query.setParameter("mandantCnr", mandantCnr);
		return query ;
	}
	
	public static VersandwegPartner findByVersandwegIIdPartnerIId(EntityManager em, Integer versandwegId, Integer partnerId, String mandantCnr) {
		return byVersandwegIIdPartnerIId(em, versandwegId, partnerId, mandantCnr).getSingleResult() ;
	}
	
	public static List<VersandwegPartner> listByPartnerIId(EntityManager em, Integer partnerId, String mandantCnr) {
		return byPartnerIId(em, partnerId, mandantCnr).getResultList();
	}
	
	public static VersandwegPartner findByPartnerIIdVersandwegCnr(
			EntityManager em, Integer partnerId, String versandwegCnr, String mandantCnr) {
		List<VersandwegPartner> partners = byPartnerIIdVersandwegCnr(em,
				partnerId, versandwegCnr, mandantCnr).getResultList();
		return partners.size() == 1 ? partners.get(0) : null;
	}
}
