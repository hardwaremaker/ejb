package com.lp.server.bestellung.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class BestellpositionQuery {
	public final static String ByPositionIIdArtikelset = "BestellpositionfindByPositionIIdArtikelset";
	
	public static HvTypedQuery<Bestellposition> byPositionIIdArtikelSet(EntityManager em, Integer positionIIdArtikelset) {
		HvTypedQuery<Bestellposition> q = new HvTypedQuery<Bestellposition>(em.createNamedQuery(ByPositionIIdArtikelset));
		return q.setParameter(1, positionIIdArtikelset);
	} 
	
	public static List<Bestellposition> listByPositionIIdArtikelset(EntityManager em, Integer positionIIdArtikelset) {
		return byPositionIIdArtikelSet(em, positionIIdArtikelset).getResultList();
	}
}
