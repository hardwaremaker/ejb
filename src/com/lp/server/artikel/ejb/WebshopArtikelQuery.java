package com.lp.server.artikel.ejb;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.util.ArtikelId;
import com.lp.server.util.WebshopId;

public class WebshopArtikelQuery {
	public static final String ByShopIdArtikelId = "WebshopArtikelShopIdArtikelId";
	public static final String ByShopIdExternalId = "WebshopArtikelShopIdExternalId";
		
	public static HvTypedQuery<WebshopArtikel> byShopIdArtikelId(
			EntityManager em, WebshopId shopId, ArtikelId artikelId) {
		HvTypedQuery<WebshopArtikel> q = new HvTypedQuery<WebshopArtikel>(
				em.createNamedQuery(ByShopIdArtikelId));
		return q.setParameter("shopId", shopId.id())
				.setParameter("artikelId", artikelId.id());		
	}
	
	public static HvTypedQuery<WebshopArtikel> byShopIdExternalId(
			EntityManager em, WebshopId shopId, String externalId) {
		HvTypedQuery<WebshopArtikel> q = new HvTypedQuery<WebshopArtikel>(
				em.createNamedQuery(ByShopIdExternalId));
		return q.setParameter("shopId", shopId.id())
				.setParameter("externalId", externalId);		
	}
	
	public static WebshopArtikel findByShopIdArtikelId(
			EntityManager em, WebshopId shopId, ArtikelId artikelId) {
		return byShopIdArtikelId(
				em, shopId, artikelId).getSingleResultNoEx();
	}
	
	public static WebshopArtikel findByShopIdExternalId(
			EntityManager em, WebshopId shopId, String externalId) {
		return byShopIdExternalId(
				em, shopId, externalId).getSingleResultNoEx();
	}
}
