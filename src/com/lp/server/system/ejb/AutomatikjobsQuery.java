package com.lp.server.system.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class AutomatikjobsQuery {
	public static final String ByActiveScheduler = "AutomatikjobFindByActiveScheduler";
	public static final String ByIAutomatikjobtypeIid = "AutomatikjobfindByIAutomatikjobtypeIid";
	public static final String ByIAutomatikjobtypeIidMandantCnr = "AutomatikjobfindByIAutomatikjobtypeIidMandantCnr";
	public static final String FindHighestIId = "AutomatikjobFindHighestIId";
	
	public static HvTypedQuery<Automatikjobs> byActiveScheduler(
			EntityManager em, boolean active, Integer scheduler) {
		HvTypedQuery<Automatikjobs> q = new HvTypedQuery<Automatikjobs>(
				em.createNamedQuery(ByActiveScheduler));
		return q.setParameter("active", new Integer(active ? 1 : 0))
				.setParameter("scheduler", scheduler);
	}
	
	public static List<Automatikjobs> listByActiveScheduler(
			EntityManager em, boolean active, Integer scheduler) {
		return byActiveScheduler(
				em, active, scheduler).getResultList();
	}
	
	public static List<Automatikjobs> listByAutomatikjobtypeIId(EntityManager em, Integer jobtypeIId) {
		return HvTypedQuery.<Automatikjobs>namedQuery(em, ByIAutomatikjobtypeIid, jobtypeIId).getResultList();
	}
	
	public static Automatikjobs resultByAutomatikjobtypeIIdMandantCnr(EntityManager em, Integer jobtypeIId, String mandantCnr) {
		return HvTypedQuery.<Automatikjobs>namedQuery(em, ByIAutomatikjobtypeIidMandantCnr, jobtypeIId, mandantCnr).getSingleResult();
	}
	
	public static Integer findHighestIId(EntityManager em) {
		return HvTypedQuery.<Integer>namedQuery(em, FindHighestIId).getSingleResult();
	}
	
}
