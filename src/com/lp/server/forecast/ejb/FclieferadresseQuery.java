package com.lp.server.forecast.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class FclieferadresseQuery {
	public static final String ByKundeIId = "FclieferadresseFindByKundeIIdLieferadresse";

	public static HvTypedQuery<Fclieferadresse> byKundeIId(Integer kundeId, EntityManager em) {
		HvTypedQuery<Fclieferadresse> theQuery = new HvTypedQuery<Fclieferadresse>(em.createNamedQuery(ByKundeIId));
		theQuery.setParameter("kundeId", kundeId);
		return theQuery;
	}

	public static List<Fclieferadresse> listByKundeIId(Integer kundeId, EntityManager em) {
		return byKundeIId(kundeId, em).getResultList();
	}
}
