package com.lp.server.shop.magento2;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.List;

import javax.ejb.Local;

import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.system.service.MwstsatzbezDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.ArtikelId;
import com.lp.server.util.WebshopId;

@Local
public interface Magento2ProductFacLocal {
	void pushItem(WebshopId shopId, List<VkpfartikelpreislisteDto> pricelists, ArtikelId itemId,
			TheClientDto theClientDto) throws RemoteException;
	void pushItems(WebshopId shopId, TheClientDto theClientDto);
	void pushChangedItems(WebshopId shopId, Timestamp tLastChanged, TheClientDto theClientDto);
	void pushCustomerGroup(WebshopId shopId, VkpfartikelpreislisteDto preislisteDto, TheClientDto theClientDto);
	void pushCustomerGroups(WebshopId shopId, TheClientDto theClientDto);
	void pushTaxClass(WebshopId shopId, MwstsatzbezDto satzbezDto, TheClientDto theClientDto);
	void pushTaxClasses(WebshopId shopId, TheClientDto theClientDto);
}
