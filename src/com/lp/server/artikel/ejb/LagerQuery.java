package com.lp.server.artikel.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class LagerQuery {

	public static final String ByMandantCNrLagerartCNr = "LagerfindByMandantCNrLagerartCNr";
	public static final String IIdsByMandantCNr = "LagerIIdsfindByMandantCNr";
	
	public static HvTypedQuery<Lager> byMandantCNrLagerartCNr(EntityManager em, String mandantCnr, String lagerartCnr) {
		HvTypedQuery<Lager> query = new HvTypedQuery<Lager>(em.createNamedQuery(ByMandantCNrLagerartCNr));
		query.setParameter(1, mandantCnr);
		query.setParameter(2, lagerartCnr);
		return query;
	}
	
	public static List<Lager> listByMandantCNrLagerartCNr(EntityManager em, String mandantCnr, String lagerartCnr) {
		return byMandantCNrLagerartCNr(em, mandantCnr, lagerartCnr).getResultList();
	}
	
	public static List<Integer> listIIdsByMandantCNr(EntityManager em, String mandantCnr) {
		return HvTypedQuery.<Integer>namedQuery(em, IIdsByMandantCNr, mandantCnr).getResultList();
	}
}
