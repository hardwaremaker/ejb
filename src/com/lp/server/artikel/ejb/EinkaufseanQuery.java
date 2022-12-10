package com.lp.server.artikel.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class EinkaufseanQuery {

	public final static String ByArtikelIId = "EinkaufseanFindByArtikelIId";
	
	public static HvTypedQuery<Einkaufsean> byArtikelIId(EntityManager em, Integer artikelIId) {
		HvTypedQuery<Einkaufsean> theQuery = new HvTypedQuery<Einkaufsean>(em.createNamedQuery(ByArtikelIId));
		theQuery.setParameter("artikelIId", artikelIId);
		
		return theQuery;
	}
	
	public static List<Einkaufsean> listByArtikelIId(EntityManager em, Integer artikelIId) {
		return byArtikelIId(em, artikelIId).getResultList();
	}
}
