package com.lp.server.artikel.ejb;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.util.MwstsatzbezId;
import com.lp.server.util.WebshopId;

public class WebshopMwstsatzbezQuery {
	public static final String ByShopIdMwstsatzbezId = "WebshopMwstsatzbezShopIdMwstsatzbezId";
		
	public static HvTypedQuery<WebshopMwstsatzbez> byShopIdMwstsatzbezId(
			EntityManager em, WebshopId shopId, MwstsatzbezId mwstsatzbezId) {
		HvTypedQuery<WebshopMwstsatzbez> q = new HvTypedQuery<WebshopMwstsatzbez>(
				em.createNamedQuery(ByShopIdMwstsatzbezId));
		return q.setParameter("shopId", shopId.id())
				.setParameter("mwstsatzbezId", mwstsatzbezId.id());		
	}
	
	public static WebshopMwstsatzbez findByShopIdMwstsatzbezId(
			EntityManager em, WebshopId shopId, MwstsatzbezId mwstsatzbezId) {
		return byShopIdMwstsatzbezId(
				em, shopId, mwstsatzbezId).getSingleResultNoEx();
	}
}
