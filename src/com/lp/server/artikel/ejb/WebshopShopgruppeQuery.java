package com.lp.server.artikel.ejb;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.util.ShopgruppeId;
import com.lp.server.util.WebshopId;

public class WebshopShopgruppeQuery {
	public static final String ByShopIdShopgruppeId = "WebshopShopgruppeByShopIdShopgruppeId";
	public static final String ByShopIdExternalId = "WebshopShopgruppeByShopIdExternalId";
	public static final String ByShopIdWithDate = "WebshopShopgruppeByShopIdWithDate";
	
	public static HvTypedQuery<WebshopShopgruppe> byShopIdShopgruppeId(
			EntityManager em, WebshopId shopId, ShopgruppeId shopgruppeId) {
		HvTypedQuery<WebshopShopgruppe> q = new HvTypedQuery<WebshopShopgruppe>(
				em.createNamedQuery(ByShopIdShopgruppeId));
		return q.setParameter("shopId", shopId.id())
				.setParameter("shopgruppeId", shopgruppeId.id());		
	}
	
	public static HvTypedQuery<WebshopShopgruppe> byShopIdExternalId(
			EntityManager em, WebshopId shopId, String externalId) {
		HvTypedQuery<WebshopShopgruppe> q = new HvTypedQuery<WebshopShopgruppe>(
				em.createNamedQuery(ByShopIdExternalId));
		return q.setParameter("shopId", shopId.id())
				.setParameter("externalId", externalId);
		
	}

	public static HvTypedQuery<WebshopShopgruppe> byShopIdWithDate(
			EntityManager em, WebshopId shopId, Timestamp lastChanged) {
		HvTypedQuery<WebshopShopgruppe> q = new HvTypedQuery<WebshopShopgruppe>(
				em.createNamedQuery(ByShopIdWithDate));
		return q.setParameter("shopId", shopId.id())
				.setParameter("tChanged", lastChanged);		
	}

	public static WebshopShopgruppe findByShopIdShopgruppeId(
			EntityManager em, WebshopId shopId, ShopgruppeId shopgruppeId) {
		return byShopIdShopgruppeId(
				em, shopId, shopgruppeId).getSingleResultNoEx();
	}
	
	public static WebshopShopgruppe findByShopIdExternalId(
			EntityManager em, WebshopId shopId, String externalId) {
		return byShopIdExternalId(
				em, shopId, externalId).getSingleResultNoEx();
	}
	
	public static List<WebshopShopgruppe> listByShopIdChangedDate(
			EntityManager em, WebshopId shopId, Timestamp lastChanged) {
		return byShopIdWithDate(em, shopId, lastChanged).getResultList();		
	}
}
