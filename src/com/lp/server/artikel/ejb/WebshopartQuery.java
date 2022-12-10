package com.lp.server.artikel.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class WebshopartQuery {
	public static final String ByAll = "WebshopartFindAll";

	public static HvTypedQuery<Webshopart> byAll(EntityManager em) {
		HvTypedQuery<Webshopart> q = new HvTypedQuery<Webshopart>(
				em.createNamedQuery(ByAll));
		return q;	
	}
	
	public static List<Webshopart> listAll(EntityManager em) {
		return byAll(em).getResultList();
	}
}
