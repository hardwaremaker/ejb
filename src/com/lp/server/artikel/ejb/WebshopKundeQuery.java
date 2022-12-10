package com.lp.server.artikel.ejb;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.util.KundeId;
import com.lp.server.util.WebshopId;

public class WebshopKundeQuery {
	public static final String ByShopIdKundeId = "WebshopKundeShopIdKundeId";
	public static final String ByShopIdExternalId = "WebshopKundeShopIdExternalId";
	
	public static HvTypedQuery<WebshopKunde> byShopIdKundeId(
			EntityManager em, WebshopId shopId, KundeId kundeId) {
		HvTypedQuery<WebshopKunde> q = new HvTypedQuery<WebshopKunde>(
				em.createNamedQuery(ByShopIdKundeId));
		return q.setParameter("shopId", shopId.id())
				.setParameter("kundeId", kundeId.id());		
	}
	
	public static HvTypedQuery<WebshopKunde> byShopIdExternalId(
			EntityManager em, WebshopId shopId, String externalId) {
		HvTypedQuery<WebshopKunde> q = new HvTypedQuery<WebshopKunde>(
				em.createNamedQuery(ByShopIdExternalId));
		return q.setParameter("shopId", shopId.id())
				.setParameter("externalId", externalId);		
	}
	
	public static WebshopKunde findByShopIdKundeId(
			EntityManager em, WebshopId shopId, KundeId kundeId) {
		return byShopIdKundeId(
				em, shopId, kundeId).getSingleResultNoEx();
	}
	
	public static WebshopKunde findByShopIdExternalId(
			EntityManager em, WebshopId shopId, String externalId) {
		return byShopIdExternalId(
				em, shopId, externalId).getSingleResultNoEx();
	}
}
