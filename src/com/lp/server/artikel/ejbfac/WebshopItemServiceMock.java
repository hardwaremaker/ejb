/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.server.artikel.ejbfac;

import java.math.BigDecimal;

import javax.ws.rs.WebApplicationException;

import org.apache.http.HttpStatus;

import com.lp.server.system.service.WebshopAuthHeader;


public class WebshopItemServiceMock implements WebshopItemServiceInterface {

	@Override
	public WebshopItemResult getItemFindByCnr(WebshopAuthHeader header, String id) {
		if(null == id || id.trim().isEmpty()) {
			throw new WebApplicationException(HttpStatus.SC_BAD_REQUEST) ;
		}
	
		if("4711".equals(id)) return createItemResult(4711, "4711_cnr", new BigDecimal("4.7")) ;
//		if("4712".equals(id)) return createItem(4712, "4712_cnr", new BigDecimal("47.12"), new byte[]{1, 2, 3, 4, 5, 6}) ;
		
		throw new WebApplicationException(HttpStatus.SC_NOT_FOUND) ;
	}

	
//	@Override
//	public List<WebshopItem> getItems() {
//		WebshopItem[] items = {
//				createItem(815, "0815_cnr", new BigDecimal("8.15")),
//				createItem(4711, "4711_cnr", new BigDecimal("47.11"))
//		} ;
//		
//		return Arrays.asList(items) ;
//	}

	@Override
	public WebshopItemResult getItemFindById(WebshopAuthHeader header,
			Integer id) {
		// TODO Auto-generated method stub
		throw new WebApplicationException(HttpStatus.SC_NOT_FOUND) ;
	}


	private WebshopItemResult createItemResult (Integer id, String cnr, BigDecimal price) {
		WebshopItemResult result = new WebshopItemResult() ;
		result.setItem(createItem(id, cnr, price)) ;
		result.setOkay() ;
		return result ;
	}

	private WebshopItem createItem (Integer id, String cnr, BigDecimal price) {
		WebshopItem item = new WebshopItem() ;
		item.setId(id) ;
		item.setCnr(cnr) ;
		item.setPrice(price) ;
		
		return item ;
	}
	
	@Override
	public ShopgroupsResult getShopGroupsFindAll(WebshopAuthHeader header) {
		return null;
	}

	@Override
	public ShopgroupsResult getShopGroupsFindAllChanged(WebshopAuthHeader header, String changedDate) {
		return null;
	}

	@Override
	public ShopgroupResult getShopGroupFindByCnr(WebshopAuthHeader header, String cnr) {
		return null;
	}

	@Override
	public WebshopItemImageResult getItemImage(WebshopAuthHeader header,
			String itemImageName) {
		return null;
	}

	@Override
	public ShopgroupResult getShopGroupFindById(WebshopAuthHeader header, Integer id) {
		return null;
	}
	
	@Override
	public ShopgroupsResult getShopGroupsFindByCnrChanged(
			WebshopAuthHeader header, String rootShopgruppe, String changedDate) {
		return null;
	}

	@Override
	public ShopgroupsResult getShopGroupsFindByIdChanged(
			WebshopAuthHeader header, Integer rootShopgruppeIId, String changedDate) {
		return null;
	}


	@Override
	public WebshopItemsResult getItems(WebshopAuthHeader header) {
		return null;
	}


	@Override
	public WebshopItemsResult getItemsChanged(WebshopAuthHeader header,
			String changedDate) {
		return null;
	}


	@Override
	public ShopgroupsFlatResult getShopGroupsFlatFindAll(
			WebshopAuthHeader header) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ShopgroupsFlatResult getShopGroupsFlatFindAllChanged(
			WebshopAuthHeader header, String changedDate) {
		// TODO Auto-generated method stub
		return null;
	}
}
