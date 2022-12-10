package com.lp.server.system.ejb;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class AutomatikjobtypeQuery {
	
	public static final String ByCJobType = "AutomatikjobtypefindByCJobType";
	
	public static Automatikjobtype resultByCJobType(EntityManager em, String jobType) {
		return HvTypedQuery.<Automatikjobtype>namedQuery(em, ByCJobType, jobType).getSingleResultNoEx();
	}
}
