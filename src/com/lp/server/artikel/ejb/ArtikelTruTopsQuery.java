package com.lp.server.artikel.ejb;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.util.ArtikelId;
import com.lp.server.util.HvOptional;

public class ArtikelTruTopsQuery {
	
	public static final String ByArtikelIId = "ArtikelTruTopsfindByArtikelIId";

	public static HvOptional<ArtikelTruTops> findByArtikelIId(EntityManager em, ArtikelId artikelId) {
		return HvOptional.ofNullable(new HvTypedQuery<ArtikelTruTops>(em.createNamedQuery(ByArtikelIId))
				.setParameter("artikelid", artikelId.id())
				.getSingleResultNoEx());
	}

}
