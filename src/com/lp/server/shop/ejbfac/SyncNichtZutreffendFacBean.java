package com.lp.server.shop.ejbfac;

import java.sql.Timestamp;

import javax.ejb.Stateless;

import com.lp.server.shop.service.SyncShopFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.WebshopId;

@Stateless
public class SyncNichtZutreffendFacBean extends Facade implements SyncShopFac {

	private void logCall(String method, WebshopId shopId) {
		myLogger.warn(method + " - Nicht zutreffend bei " + shopId.toString());
		
	}
	@Override
	public void ping(WebshopId shopId, TheClientDto theClientDto) {
		logCall("ping", shopId);
	}
	
	@Override
	public void pushCustomerGroups(WebshopId shopId, TheClientDto theClientDto) {
		logCall("pushCustomerGroups", shopId);
	}
	
	@Override
	public void pushItems(WebshopId shopId, TheClientDto theClientDto) {
		logCall("pushItems", shopId);
	}

	@Override
	public void pushChangedItems(WebshopId shopId, Timestamp tLastChanged, TheClientDto theClientDto) {
		logCall("pushChangedItems", shopId);
	}
	
	@Override
	public void pushCategories(WebshopId shopId, TheClientDto theClientDto) {
		logCall("pushCategories", shopId);
	}
	
	@Override
	public void pushTaxClasses(WebshopId shopId, TheClientDto theClientDto) {
		logCall("pushTaxClasses", shopId);
	}
	
	@Override
	public void fetchOrders(WebshopId shopId, TheClientDto theClientDto) {
		logCall("fetchOrders", shopId);
	}
	
	@Override
	public void pushChangedCategories(WebshopId shopId, Timestamp tLastChanged, TheClientDto theClientDto) {
		logCall("pushChangedCategories", shopId);		
	}
}
