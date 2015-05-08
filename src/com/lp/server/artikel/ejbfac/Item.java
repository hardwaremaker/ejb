/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
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
import java.util.Date;

public class Item {

	private Integer id ;
	private String type ;
	private String sku ;
	private String name ;
	private String description ;
	private String shortDescription ;
	private boolean status ;
	private int visibility ;
	private String deliveryTime ;
	private boolean generateMetaTags ;
	private BigDecimal inventoryMinQty ;
	private int taxClass ;
	private BigDecimal inventoryMinSaleQty ;
	private BigDecimal inventoryMaxSaleQty ;
	private String itemCategory ;
	private Integer itemCategoryId ;
	private String itemLanguage ;
	private BigDecimal price ;
	private BigDecimal inventoryQty ;
	private BigDecimal specialPrice ;
	private Date specialFromDate ;
	private Date specialToDate ;
	private String seoText ;
	
	public Item() {		
		setInventoryMinSaleQty(BigDecimal.ZERO) ;
		setInventoryMaxSaleQty(BigDecimal.ZERO) ;
		setSpecialPrice(BigDecimal.ZERO) ;
		setSeoText("") ;
		setDeliveryTime("---") ;
	}
	
	
	public Integer getId() {
		return id ;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return "Einfach" ;
	}

	public void setType(String type) {
	}

	public String getName() {
		return name ;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return "description" ;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getShortDescription() {
		return shortDescription ;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public boolean isStatus() {
		return true ;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public int getVisibility() {
		return 1 ;
	}

	public void setVisibility(int visibility) {
		this.visibility = visibility;
	}

	public String getDeliveryTime() {
		return deliveryTime ;
	}

	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public boolean isGenerateMetaTags() {
		return false ;
	}

	public void setGenerateMetaTags(boolean generateMetaTags) {
		this.generateMetaTags = generateMetaTags;
	}

	public BigDecimal getInventoryMinQty() {
		return inventoryMinQty;
	}

	public void setInventoryMinQty(BigDecimal inventoryMinQty) {
		this.inventoryMinQty = inventoryMinQty;
	}

	public int getTaxClass() {
		return taxClass;
	}

	public void setTaxClass(int taxClass) {
		this.taxClass = taxClass;
	}


	public BigDecimal getInventoryMinSaleQty() {
		return inventoryMinSaleQty;
	}


	public void setInventoryMinSaleQty(BigDecimal inventoryMinSaleQty) {
		this.inventoryMinSaleQty = inventoryMinSaleQty;
	}


	public BigDecimal getInventoryMaxSaleQty() {
		return inventoryMaxSaleQty;
	}


	public void setInventoryMaxSaleQty(BigDecimal inventoryMaxSaleQty) {
		this.inventoryMaxSaleQty = inventoryMaxSaleQty;
	}


	public String getItemGroup() {
		return itemCategory;
	}


	public void setItemGroup(String itemGroup) {
		this.itemCategory = itemGroup;
	}


	public String getItemLanguage() {
		return itemLanguage;
	}


	public void setItemLanguage(String itemLanguage) {
		this.itemLanguage = itemLanguage;
	}


	public BigDecimal getPrice() {
		return price;
	}


	public void setPrice(BigDecimal price) {
		this.price = price;
	}


	public BigDecimal getInventoryQty() {
		return inventoryQty;
	}


	public void setInventoryQty(BigDecimal inventoryQty) {
		this.inventoryQty = inventoryQty;
	}


	public String getSku() {
		return sku;
	}


	public void setSku(String sku) {
		this.sku = sku;
	}


	public BigDecimal getSpecialPrice() {
		return specialPrice;
	}


	public void setSpecialPrice(BigDecimal specialPrice) {
		this.specialPrice = specialPrice;
	}


	public Date getSpecialFromDate() {
		return specialFromDate;
	}


	public void setSpecialFromDate(Date specialFromDate) {
		this.specialFromDate = specialFromDate;
	}


	public Date getSpecialToDate() {
		return specialToDate;
	}


	public void setSpecialToDate(Date specialToDate) {
		this.specialToDate = specialToDate;
	}


	public String getSeoText() {
		return seoText;
	}


	public void setSeoText(String seoText) {
		this.seoText = seoText;
	}


	public Integer getItemCategoryId() {
		return itemCategoryId;
	}

	public void setItemCategoryId(Integer itemGroupId) {
		this.itemCategoryId = itemGroupId;
	}
}
