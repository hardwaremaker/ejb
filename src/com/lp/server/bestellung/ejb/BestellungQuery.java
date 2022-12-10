package com.lp.server.bestellung.ejb;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class BestellungQuery {
	public final static String ByLieferantIdBestelladresseFilter = "ByLieferantIdBestelladresseFilter";
	
	public static HvTypedQuery<Bestellung> byLieferantIdBestelladresseFilter(
			EntityManager em, String mandant, Integer lieferantId, String[] allowedStati) {
		HvTypedQuery<Bestellung> query = new HvTypedQuery<Bestellung>(
				em.createNamedQuery(ByLieferantIdBestelladresseFilter));
		return query
				.setParameter("mandant", mandant)
				.setParameter("lieferantId", lieferantId)
				.setParameter("filter", Arrays.asList(allowedStati));		
	}
	
	public static List<Bestellung> listByLieferantIdBestelladresseFilter(
			EntityManager em, String mandant, Integer lieferantId, String[] allowedStati) {
		return byLieferantIdBestelladresseFilter(em, mandant, lieferantId, allowedStati).getResultList();
	}
}
