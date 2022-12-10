package com.lp.server.personal.ejb;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class ZeitdatenpruefenQuery {
	public final static String CountByPersonalId = "CountByPersonalId";
	
	
	public static HvTypedQuery<Long> byPersonalId(EntityManager em, Integer personalId) {
		HvTypedQuery<Long> query = new HvTypedQuery<Long>(
				em.createNamedQuery(ZeitdatenpruefenQuery.CountByPersonalId)) ;
		return query.setParameter("personalId", personalId);		
	}
	
	public static Integer countByPersonalId(EntityManager em, Integer personalId) {
		Long l = byPersonalId(em, personalId).getSingleResult();
		return l == null ? 0 : l.intValue();
	}
}
