package com.lp.server.bestellung.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class WareneingangQuery {
	public final static String ByLieferscheinnummer = "ByLieferscheinnummer";
	
	public static HvTypedQuery<Wareneingang> byLieferscheinnummer(EntityManager em, Integer bestellungId, String lieferscheinnummer) {
		HvTypedQuery<Wareneingang> weQuery = new HvTypedQuery<Wareneingang>(em.createNamedQuery(ByLieferscheinnummer));
		return weQuery
				.setParameter("bestellungId", bestellungId)
				.setParameter("lieferscheinnummer", lieferscheinnummer);
	}

	public static List<Wareneingang> listByLieferscheinnummer(EntityManager em, Integer bestellungId, String lieferscheinnummer) {
		return byLieferscheinnummer(em, bestellungId, lieferscheinnummer).getResultList();
	}
}
