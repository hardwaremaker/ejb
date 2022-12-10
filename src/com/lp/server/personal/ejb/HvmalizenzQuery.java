package com.lp.server.personal.ejb;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class HvmalizenzQuery {
	public final static String ByCnr = "HvmalizenzfindByCnr";
	
	public static HvTypedQuery<Hvmalizenz> byCnr(EntityManager em, String cnr) {
		HvTypedQuery<Hvmalizenz> q = new HvTypedQuery<Hvmalizenz>(
				em.createNamedQuery(ByCnr));
		return q.setParameter("licence", cnr);
	}
	
	public static Hvmalizenz findCnr(EntityManager em, String cnr) {
		return byCnr(em, cnr).getSingleResult();
	}
}
