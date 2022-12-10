package com.lp.server.artikel.ejb;

import java.sql.Date;
import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class ArtikellieferantstaffelQuery {
	public final static String ByArtikellieferantIIdGueltigZuDatum = "ArtikellieferantstaffelfindByArtikellieferantIIdGueltigZuDatum";
	
	public static HvTypedQuery<Artikellieferantstaffel> byArtikellieferantIIdGueltigZuDatum(EntityManager em, Integer artikellieferantId, Date datum) {
		HvTypedQuery<Artikellieferantstaffel> theQuery = new HvTypedQuery<Artikellieferantstaffel>(em.createNamedQuery(ByArtikellieferantIIdGueltigZuDatum));
		theQuery.setParameter("artliefid", artikellieferantId);
		theQuery.setParameter("date", datum);
		return theQuery;
	}
	
	public static List<Artikellieferantstaffel> listByArtikellieferantIIdGueltigZuDatum(EntityManager em, Integer artikellieferantId, Date datum) {
		return byArtikellieferantIIdGueltigZuDatum(em, artikellieferantId, datum).getResultList();
	}
}
