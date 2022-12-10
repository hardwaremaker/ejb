package com.lp.server.shop.magento2;

import javax.ejb.Local;

import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.WebshopId;

@Local
public interface Magento2CustomerFacLocal {
	MagCustomer findAccount(WebshopId shopId, Integer acountId, TheClientDto theClientDto);
}
