package com.lp.server.shop.magento2;

import java.rmi.RemoteException;

import javax.ejb.Local;

import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.WebshopId;

@Local
public interface Magento2OrderFacLocal {
	void fetchOrder(WebshopId shopId, Integer orderId, TheClientDto theClientDto) throws RemoteException;
	void fetchOrders(WebshopId shopId, TheClientDto theClientDto);
}
