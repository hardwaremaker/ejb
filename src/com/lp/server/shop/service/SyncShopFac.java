package com.lp.server.shop.service;

import java.sql.Timestamp;

import javax.ejb.Local;

import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.WebshopId;

@Local
public interface SyncShopFac {
	/**
	 * Kann der Shop erreicht werden?</br>
	 * <p>Die Erreichbarkeit des Shops ist dadurch gegeben, dass
	 * ein GET request auf die API des Shops durchgef&uuml;hrt 
	 * werden kann.</p>
	 * 
	 * <p>Wenn der Shop nicht erreicht werden kann, ist die Anzahl 
	 * der Fehlerm&ouml;glichkeiten sehr gro&szlig;. Diese werden 
	 * dann durch Exceptions mitgeteilt</p> 
	 */
	void ping(WebshopId shopId, TheClientDto theClientDto);
	
	/**
	 * Artikelinformation vom HELIUM V an den Shop &uuml;bermitteln
	 * @param shopId
	 * @param theClientDto
	 */
	void pushItems(WebshopId shopId, TheClientDto theClientDto);

	/**
	 * Ge&auml;nderte Artikel vom HELIUM V an den Shop &uuml;bermitteln
	 * @param shopId
	 * @param tLastChanged 
	 * @param theClientDto
	 */
	void pushChangedItems(WebshopId shopId, Timestamp tLastChanged, TheClientDto theClientDto);
	
	/**
	 * Shopgruppen an den Shop &uuml;bermitteln
	 * 
	 * @param shopId
	 * @param theClientDto
	 */
	void pushCategories(WebshopId shopId, TheClientDto theClientDto);

	/**
	 * Ge&auml;nderte Shopgruppen/Categories vom HELIUM V an den Shop &uuml;bermitteln
	 * @param shopId
	 * @param tLastChanged 
	 * @param theClientDto
	 */
	void pushChangedCategories(WebshopId shopId, Timestamp tLastChanged, TheClientDto theClientDto);
	
	void pushCustomerGroups(WebshopId shopId, TheClientDto theClientDto);

	void pushTaxClasses(WebshopId shopId, TheClientDto theClientDto);

	void fetchOrders(WebshopId shopId, TheClientDto theClientDto);	
}
