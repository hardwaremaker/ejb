package com.lp.server.artikel.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class ArtikelkommentardruckQuery {

	public static final String ByArtikelIIdBelegartCNr = "ArtikelkommentardruckfindByArtikelIIdBelegartCNr";
	public static final String ByArtikelIIdArtikelkommentarIId = "ArtikelkommentardruckfindByArtikelIIdArtikelkommentarIId";
	
	public ArtikelkommentardruckQuery() {
	}

	public static HvTypedQuery<Artikelkommentardruck> byArtikelIIdBelegartCNr(EntityManager em, Integer artikelIId, String belegartCnr) {
		HvTypedQuery<Artikelkommentardruck> theQuery = new HvTypedQuery<Artikelkommentardruck>(em.createNamedQuery(ByArtikelIIdBelegartCNr));
		theQuery.setParameter(1, artikelIId);
		theQuery.setParameter(2, belegartCnr);
		
		return theQuery;
	}
	
	public static List<Artikelkommentardruck> listByArtikelIIdBelegartCNr(EntityManager em, Integer artikelIId, String belegartCnr) {
		return byArtikelIIdBelegartCNr(em, artikelIId, belegartCnr).getResultList();
	}
	
	public static HvTypedQuery<Artikelkommentardruck> byArtikelIIdArtikelkommentarIId(EntityManager em, Integer artikelIId, Integer artikelkommentarIId) {
		HvTypedQuery<Artikelkommentardruck> theQuery = new HvTypedQuery<Artikelkommentardruck>(em.createNamedQuery(ByArtikelIIdArtikelkommentarIId));
		theQuery.setParameter(1, artikelIId);
		theQuery.setParameter(2, artikelkommentarIId);
		
		return theQuery;
	}
	
	public static List<Artikelkommentardruck> listByArtikelIIdArtikelkommentarIId(EntityManager em, Integer artikelIId, Integer artikelkommentarIId) {
		return byArtikelIIdArtikelkommentarIId(em, artikelIId, artikelkommentarIId).getResultList();
	}
}
