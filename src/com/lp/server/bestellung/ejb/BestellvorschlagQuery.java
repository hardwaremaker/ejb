package com.lp.server.bestellung.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.util.ArtikelId;

public class BestellvorschlagQuery {

	public static final String byArtikelIIdVormerkungMandantCnr = "BestellvorschlagfindByArtikelIIdVormerkungMandantCnr";

	public static HvTypedQuery<Bestellvorschlag> byArtikelIIdMitVormerkungMandantCnr(EntityManager em, ArtikelId artikelId, String mandantCnr) {
		return HvTypedQuery.<Bestellvorschlag>namedQuery(em, byArtikelIIdVormerkungMandantCnr, artikelId.id(), mandantCnr);
	}
	
	public static List<Bestellvorschlag> listByArtikelIIdVormerkungMandantCnr(EntityManager em, ArtikelId artikelId, String mandantCnr) {
		return byArtikelIIdMitVormerkungMandantCnr(em, artikelId, mandantCnr).getResultList();
	}
	
}
