package com.lp.server.artikel.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.util.ArtikelId;
import com.lp.server.util.ArtikelTruTopsId;

public class ArtikelTruTopsMetadatenQuery {

	public static final String ByArtikelTruTopsIId = "ArtikelTruTopsMetadatenfindByArtikelTruTopsIId";
	public static final String ByArtikelIId = "ArtikelTruTopsMetadatenfindByArtikelIId";

	public static List<ArtikelTruTopsMetadaten> findByArtikelTruTopsIId(EntityManager em, ArtikelTruTopsId artikelTruTopsId) {
		return new HvTypedQuery<ArtikelTruTopsMetadaten>(em.createNamedQuery(ByArtikelTruTopsIId))
				.setParameter("artikelTruTopsId", artikelTruTopsId.id())
				.getResultList();
	}

	public static List<ArtikelTruTopsMetadaten> findByArtikelIId(EntityManager em, ArtikelId artikelId) {
		return new HvTypedQuery<ArtikelTruTopsMetadaten>(em.createNamedQuery(ByArtikelIId))
				.setParameter("artikelid", artikelId.id())
				.getResultList();
	}
}
