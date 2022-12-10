package com.lp.server.partner.ejb;

import javax.persistence.EntityManager;

public class GeodatenQuery {

	public static final String ByPartnerIId = "GeodatenFindByPartnerIId";
	
	public static HvTypedQuery<Geodaten> byPartnerIId(Integer partnerIId, EntityManager em) {
		HvTypedQuery<Geodaten> theQuery = new HvTypedQuery<Geodaten>(em.createNamedQuery(ByPartnerIId));
		theQuery.setParameter("partnerIId", partnerIId);
		return theQuery;
	}

	public static Geodaten resultByPartnerIIdNoEx(Integer partnerIId, EntityManager em) {
		return byPartnerIId(partnerIId, em).getSingleResultNoEx();
	}
}
