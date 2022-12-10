package com.lp.server.system.ejb;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.util.HvOptional;

public class KennungQuery {
	public static final String ByCnr = "KennungFindByCnr";
	
	public static HvOptional<Kennung> findByCnr(
			EntityManager em, String cnr) {
		return HvOptional.ofNullable(new HvTypedQuery<Kennung>(
				em.createNamedQuery(ByCnr))
				.setParameter("cnr", cnr)
				.getSingleResultNoEx());
	}
}
