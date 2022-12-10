package com.lp.server.personal.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class ZeitdatenQuery {
	
	public static final String ByZeitdatenEinesBelegs = "ZeitdatenfindZeitdatenEinesBelegs";

	public static HvTypedQuery<Zeitdaten> byZeitdatenEinesBelegsOrderedByTZeitAsc(EntityManager em, String belegart, Integer belegId) {
		HvTypedQuery<Zeitdaten> theQuery = new HvTypedQuery<Zeitdaten>(em.createNamedQuery(ByZeitdatenEinesBelegs));
		theQuery.setParameter(1, belegart);
		theQuery.setParameter(2, belegId);
		return theQuery;
	}
	
	public static List<Zeitdaten> listByZeitdatenEinesBelegsOrderedByTZeitAsc(EntityManager em, String belegart, Integer belegId) {
		return byZeitdatenEinesBelegsOrderedByTZeitAsc(em, belegart, belegId).getResultList();
	}

}
