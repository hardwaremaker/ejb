package com.lp.server.shop.magento2;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.List;

import javax.ejb.Local;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lp.server.artikel.service.WebshopArtikelDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.WebshopId;

@Local
public interface Magento2ApiFacLocal {
	public class OrderStatus {
		public final static String PENDING = "pending";
		public final static String ERP_PROCESSING = "erp_processing";
	}
	
	boolean removeCategoryId(WebshopId shopId, Integer categoryId);

	MagCategory getCategory(WebshopId shopId, Integer categoryId) throws JsonProcessingException;

	MagCategory putCategory(WebshopId shopId, MagCategory category)
			throws JsonProcessingException, UnsupportedEncodingException, URISyntaxException;

	MagCategory postCategory(WebshopId shopId, MagCategory category)
			throws UnsupportedEncodingException, JsonProcessingException, URISyntaxException;

	List<MagCategory> searchCategoriesByName(WebshopId shopId, String categoryName)
			throws JsonProcessingException, URISyntaxException;

	MagTaxClass postTaxClass(WebshopId shopId, MagTaxClass taxClass)
			throws JsonProcessingException, UnsupportedEncodingException, URISyntaxException;

	MagCustomerGroup postCustomerGroup(WebshopId shopId, MagCustomerGroup customerGroup)
			throws JsonProcessingException, UnsupportedEncodingException, URISyntaxException;

	MagSearchProductResult searchProductByExternalId(WebshopId shopId, WebshopArtikelDto waDto) throws JsonProcessingException, URISyntaxException;

	MagProduct postProduct(WebshopId shopId, MagProduct product)
			throws JsonProcessingException, UnsupportedEncodingException, URISyntaxException;

	MagProduct getProduct(WebshopId shopId, String sku, TheClientDto theClientDto)
			throws JsonProcessingException, UnsupportedEncodingException, URISyntaxException;

	MagProduct putProduct(WebshopId shopId, MagProduct product)
			throws JsonProcessingException, UnsupportedEncodingException, URISyntaxException;

	MagCustomer getAccount(WebshopId shopId, Integer accountId);

	MagSearchOrdersResult searchPendingOrders(WebshopId shopId) throws JsonProcessingException, URISyntaxException;

	MagOrder getOrder(WebshopId shopId, Integer orderId) throws JsonProcessingException, URISyntaxException;

	MagOrder putOrderStatus(WebshopId shopId, MagUpdateOrderStatus status)
			throws JsonProcessingException, URISyntaxException, UnsupportedEncodingException;
}


