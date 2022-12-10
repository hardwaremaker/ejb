package com.lp.server.artikel.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class ArtikellieferantQuery {
	public final static String ByArtikelnrlieferantLieferantIId = "ArtikellieferantfindByCArtikelnrlieferantLieferantIId";
	public final static String DistinctArtikelIIdByLieferantIId = "ArtikellieferantDistinctArtikelIIdfindByLieferantIId";
	
	public static HvTypedQuery<Artikellieferant> byArtikelnrlieferantLieferantId(EntityManager em, String artikelnrlieferant, Integer lieferantIId) {
		HvTypedQuery<Artikellieferant> query = new HvTypedQuery<Artikellieferant>(
				em.createNamedQuery(ByArtikelnrlieferantLieferantIId));
		return query.setParameter(1, artikelnrlieferant)
					.setParameter(2, lieferantIId);
	}
	
	public static List<Artikellieferant> listByArtikelnrlieferantLieferantIId(
			EntityManager em, String artikelnrlieferant, Integer lieferantIId) {
		return byArtikelnrlieferantLieferantId(em, artikelnrlieferant, lieferantIId).getResultList();
	}
	
	public static HvTypedQuery<Integer> distinctArtikelIIdsByLieferantIId(EntityManager em, Integer lieferantIId) {
		HvTypedQuery<Integer> query = new HvTypedQuery<Integer>(
				em.createNamedQuery(DistinctArtikelIIdByLieferantIId));
		query.setParameter("lieferant", lieferantIId);
		return query;
	}
	
	public static List<Integer> listDistinctArtikelIIdsByLieferantIId(EntityManager em, Integer lieferantIId) {
		return distinctArtikelIIdsByLieferantIId(em, lieferantIId).getResultList();
	}
}
