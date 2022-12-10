package com.lp.server.shop.magento2;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.WebshopId;

@Stateless
public class Magento2CustomerFacBean extends Magento2BaseFacBean implements Magento2CustomerFacLocal {

	@EJB
	private Magento2ApiFacLocal magento2Api;
	
	@Override
	public MagCustomer findAccount(WebshopId shopId, Integer accountId, TheClientDto theClientDto) {
		return magento2Api.getAccount(shopId, accountId);
	}
}
