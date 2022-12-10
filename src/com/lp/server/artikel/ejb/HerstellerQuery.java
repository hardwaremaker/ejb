package com.lp.server.artikel.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class HerstellerQuery {

	public static final String ByCNr = "HerstellerfindByCNr";
	public static final String ByPartnerIId = "HerstellerfindByPartnerIId";
	
	public static HvTypedQuery<Hersteller> byCNr(String cNr, EntityManager em) {
		return HvTypedQuery.<Hersteller>namedQuery(em, ByCNr, cNr);
	}

	public static Hersteller resultByCNr(String cNr, EntityManager em) {
		return byCNr(cNr, em).getSingleResultNoEx();
	}
	
	public static HvTypedQuery<Hersteller> byPartnerIId(Integer iPartnerId, EntityManager em) {
		return HvTypedQuery.namedQuery(em, ByPartnerIId, iPartnerId);
	}
	
	public static List<Hersteller> listByPartnerIId(Integer iPartnerId, EntityManager em) {
		return byPartnerIId(iPartnerId, em).getResultList();
	}
}
