package com.lp.server.stueckliste.ejb;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.util.ArtikelId;

public class StuecklisteQuery {
	
	public static final String ByArtikelIIdMandantCNr = "StuecklistefindByArtikelIIdMandantCNr";

	public static Stueckliste findByArtikelIIdMandantCnr(EntityManager em, ArtikelId artikelId, String mandantCnr) {
		return new HvTypedQuery<Stueckliste>(em.createNamedQuery(ByArtikelIIdMandantCNr))
				.setParameter(1, artikelId.id())
				.setParameter(2, mandantCnr)
				.getSingleResultNoEx();
	}

}
