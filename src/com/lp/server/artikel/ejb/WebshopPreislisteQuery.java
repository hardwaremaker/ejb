package com.lp.server.artikel.ejb;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.util.PreislisteId;
import com.lp.server.util.WebshopId;

public class WebshopPreislisteQuery {
	public static final String ByShopIdPreislisteId = "WebshopPreislisteShopIdPreislisteId";
		
	public static HvTypedQuery<WebshopArtikelPreisliste> byShopIdPreislisteId(
			EntityManager em, WebshopId shopId, PreislisteId preislisteId) {
		HvTypedQuery<WebshopArtikelPreisliste> q = new HvTypedQuery<WebshopArtikelPreisliste>(
				em.createNamedQuery(ByShopIdPreislisteId));
		return q.setParameter("shopId", shopId.id())
				.setParameter("preislisteId", preislisteId.id());		
	}
	
	public static WebshopArtikelPreisliste findByShopIdPreislisteId(
			EntityManager em, WebshopId shopId, PreislisteId preislisteId) {
		return byShopIdPreislisteId(
				em, shopId, preislisteId).getSingleResultNoEx();
	}
}
