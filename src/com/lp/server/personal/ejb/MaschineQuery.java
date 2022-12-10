package com.lp.server.personal.ejb;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class MaschineQuery {

	public static final String ByCIdentifikationsnr = "MaschinefindByCIdentifikationsnr";
	
	public static HvTypedQuery<Maschine> byCIdentifikationsnr(EntityManager em, String identifikationsNr) {
		HvTypedQuery<Maschine> theQuery = new HvTypedQuery<Maschine>(em.createNamedQuery(ByCIdentifikationsnr));
		theQuery.setParameter(1, identifikationsNr);
		return theQuery;
	}

	public static Maschine resultByCIdentifikationsnr(EntityManager em, String identifikationsNr) {
		return byCIdentifikationsnr(em, identifikationsNr).getSingleResultNoEx();
	}
}
