package com.lp.server.shop.magento2;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.Local;

import com.lp.server.artikel.service.ShopgruppeDto;
import com.lp.server.artikel.service.WebshopShopgruppeDto;
import com.lp.server.shop.ejbfac.IsChanged;
import com.lp.server.shop.ejbfac.ShopgruppeHierarchyDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.WebshopId;

@Local
public interface Magento2CategoryFacLocal {
	void traverseHierarchy(WebshopId shopId,
			List<ShopgruppeHierarchyDto> hierarchyDtos,
			TheClientDto theClientDto) throws RemoteException;
	void traverseHierarchy(Integer magParentId, List<Integer> paths,
			WebshopId shopId, ShopgruppeHierarchyDto hierarchyDto, 
			TheClientDto theClientDto) throws RemoteException;
	
	void pushChangedCategories(WebshopId shopId, List<ShopgruppeHierarchyDto> hierarchyDtos,
			IsChanged<ShopgruppeDto> evaluateChanged, TheClientDto theClientDto) throws RemoteException;
	void pushChangedCategories(Integer magParentId, List<Integer> paths, WebshopId shopId,
			ShopgruppeHierarchyDto hierarchyDto, IsChanged<ShopgruppeDto> evaluateChanged, TheClientDto theClientDto) throws RemoteException;
	void pushOrphanedCategory(WebshopId shopId, WebshopShopgruppeDto wssgDto, TheClientDto theClientDto);
	void removeOrphanedCategories(WebshopId shopId, WebshopShopgruppeDto wssgDto, TheClientDto theClientDto);
}
