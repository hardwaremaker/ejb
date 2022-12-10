package com.lp.server.personal.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class MaschinengruppeQuery {
	public static final String ByCBez = "MaschinengruppefindByCBez";
	public static final String ByCKbez = "MaschinengruppefindByCKbez";
	public static final String ByFertigungsgruppe = "MaschinengruppeFindByFertigungsgruppe";
	public static final String NextISort = "MaschinengruppeSelectNextISort";
	
	public static HvTypedQuery<Maschinengruppe> byKbez(EntityManager em, String mandantCnr, String cKbez) {
		HvTypedQuery<Maschinengruppe> theQuery = new HvTypedQuery<Maschinengruppe>(em.createNamedQuery(ByCKbez));
		return theQuery
			.setParameter("mandantCnr", mandantCnr)
			.setParameter("cKbez", cKbez);
	}
	
	

	public static HvTypedQuery<Maschinengruppe> byFertigungsgruppe(EntityManager em, String mandantCnr, Integer fertigungsgruppeId) {
		HvTypedQuery<Maschinengruppe> theQuery = new HvTypedQuery<Maschinengruppe>(em.createNamedQuery(ByFertigungsgruppe));
		return theQuery
			.setParameter("mandantCnr", mandantCnr)
			.setParameter("fertigungsgruppe", fertigungsgruppeId);
	}

	public static Maschinengruppe findByKbez(EntityManager em, String mandantCnr, String cKbez) {
		return byKbez(em, mandantCnr, cKbez).getSingleResultNoEx();
	}
	
	public static List<Maschinengruppe> listByFertigungsgruppe(EntityManager em, String mandantCnr, Integer fertigungsgruppeId) {
		return byFertigungsgruppe(em, mandantCnr, fertigungsgruppeId).getResultList();
	}
}
