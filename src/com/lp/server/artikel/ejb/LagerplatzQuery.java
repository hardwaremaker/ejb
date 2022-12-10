package com.lp.server.artikel.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class LagerplatzQuery {

	public final static String ByLagerplatzIIds = "LagerplatzFindByLagerplatzIIds";
	public final static String ByLagerIIdCLagerplatz = "LagerplatzfindByLagerIIdCLagerplatz";
	
	public static HvTypedQuery<Lagerplatz> byLagerplatzIIds(EntityManager em, List<Integer> lagerplatzIIds) {
		HvTypedQuery<Lagerplatz> theQuery = new HvTypedQuery<Lagerplatz>(em.createNamedQuery(ByLagerplatzIIds));
		theQuery.setParameter("ids", lagerplatzIIds);
		return theQuery;
	}

	public static List<Lagerplatz> listByLagerplatzIIds(EntityManager em, List<Integer> lagerplatzIIds) {
		return byLagerplatzIIds(em, lagerplatzIIds).getResultList();
	}
	
	public static HvTypedQuery<Lagerplatz> byLagerIIdCLagerplatz(EntityManager em, Integer lagerIId, String cLagerplatz) {
		HvTypedQuery<Lagerplatz> theQuery = new HvTypedQuery<Lagerplatz>(em.createNamedQuery(ByLagerIIdCLagerplatz));
		theQuery.setParameter(1, lagerIId);
		theQuery.setParameter(2, cLagerplatz);
		return theQuery;
	}
	
	public static Lagerplatz resultByLagerIIdCLagerplatzNoExc(EntityManager em, Integer lagerIId, String cLagerplatz) {
		return byLagerIIdCLagerplatz(em, lagerIId, cLagerplatz).getSingleResultNoEx();
	}
}
