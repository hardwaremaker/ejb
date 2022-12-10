package com.lp.server.personal.ejb;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class HvmabenutzerQuery {
	public final static String ByBenutzerIIdHvmalizenzIId = "HvmabenutzerfindByBenutzerIIdHvmalizenzIId";
	
	public static HvTypedQuery<Hvmabenutzer> byBenutzerIdLizenzId(EntityManager em, Integer benutzerId, Integer lizenzId) {
		HvTypedQuery<Hvmabenutzer> q = new HvTypedQuery<Hvmabenutzer>(
				em.createNamedQuery(ByBenutzerIIdHvmalizenzIId));
		return q
				.setParameter(1, benutzerId)
				.setParameter(2, lizenzId);
	}
	
	public static Hvmabenutzer findBenutzerIdLizenzId(
			EntityManager em, Integer benutzerId, Integer lizenzId) {
		 return byBenutzerIdLizenzId(em, benutzerId, lizenzId).getSingleResult();
	}
}
