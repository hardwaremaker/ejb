package com.lp.server.artikel.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class ArtklaQuery {

	public final static String AllTopsByMandantCNr = "ArtklafindAllTopsByMandantCNr";
	
	public static List<Artkla> listAllTopsByMandantCNr(EntityManager em, String mandantCnr) {
		return HvTypedQuery.<Artkla>namedQuery(em, AllTopsByMandantCNr)
				.setParameter("mandant", mandantCnr)
				.getResultList();
	}
}
