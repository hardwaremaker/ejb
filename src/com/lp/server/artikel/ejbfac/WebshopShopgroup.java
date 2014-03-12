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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Eine Webshop-Gruppe enthaelt folgende Informationen
 * - die id, cnr, bez der Shopgruppe
 * - die (eventuell vorhandene) Kind-Shopgruppe
 * - die (eventuell) vorhandenen Artikel-Schluesselfelder
 * - den (eventuell) vorhandenen Referenzartikel der Shopgruppe
 * <br>
 * <p>Im aktuellen Stand ist es nicht "verboten", dass eine
 * Shopgruppe eine Kind-Shopgruppe hat und trotzdem Artikel auflistet.
 * Es ist allerdings so, dass HeliumV derzeit nur entweder eine Kindershopgruppe,
 * oder eine Artikelliste liefern wird.
 * </p>
 * @author Gerold
 */
@XmlRootElement(name = "WebshopShopgroup") 
public class WebshopShopgroup implements Serializable {
	private static final long serialVersionUID = -7325613231904903216L;
	
	private   Integer id ;
	private String cnr ;
	private String bez ;

	private WebshopItemReferenceInfo referenceItemInfo ;
	
	private Integer sortValue ;
	
	@XmlElement(nillable = false)
	private List<WebshopShopgroupOnly> childShopgroups ;
	
//	@XmlElement(nillable = false)
	private List<WebshopItemReference> items ;

//	@XmlElement(nillable = false)
	private WebshopItemReference referenceItem ;
	
	private WebshopSeoInfo seoInfo ;
	
	public WebshopShopgroup() {
		items = new ArrayList<WebshopItemReference>() ;
		childShopgroups = new ArrayList<WebshopShopgroupOnly>() ;
	}
	
	
	/**
	 * Etwaig vorhandenes Kind der Shopgruppe liefern
	 * @return null wenn die aktuelle Shopgruppe keine weiteren Kinder hat, 
	 * ansonsten die Kind-Shopgruppe. Ist die aktuelle Shopgruppe null, dann 
	 * kann die Liste der Artikel ({@link #getItems()} benutzt werden 
	 */
	public List<WebshopShopgroupOnly> getChildShopgroups() {
		return childShopgroups ;
	}

	public void setChildShopgroup(List<WebshopShopgroupOnly> shopgroups) {
		this.childShopgroups = shopgroups ;
	}
	
	/**
	 * Liste der dieser Shopgruppe zugehoerigen Artikel
	 * @return eine (leere) Liste aller Artikel, die der Shopgruppe angehoeren
	 */
	public List<WebshopItemReference> getItems() {
		return items;
	}
	
	public void setItems(List<WebshopItemReference> items) {
		this.items = items;
	}

	/**
	 * Der Referenzartikel dieser Shopgruppe, sofern vorhanden
	 * @return null wenn es keinen Referenzartikel gibt, ansonsten der Referenzartikel
	 */
	public WebshopItemReference getReferenceItem() {
		return referenceItem;
	}

	public void setReferenceItem(WebshopItemReference referenceItem) {
		this.referenceItem = referenceItem;
	}

//	public String getReferenceShortDescription() {
//		return referenceShortDescription;
//	}
//
//	public void setReferenceShortDescription(String shortDescription) {
//		this.referenceShortDescription = shortDescription;
//	}
//
//	public String getReferenceLongDescription() {
//		return referenceLongDescription;
//	}
//
//	public void setReferenceLongDescription(String longDescription) {
//		this.referenceLongDescription = longDescription;
//	}
//
//	public byte[] getReferenceImage() {
//		return referenceImage;
//	}
//
//	public void setReferenceImage(byte[] image) {
//		this.referenceImage = image;
//	}	
//
	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getCnr() {
		return cnr;
	}


	public void setCnr(String cnr) {
		this.cnr = cnr;
	}


	public String getName() {
		return bez;
	}


	public void setName(String name) {
		this.bez = name;
	}


	public Integer getSortValue() {
		return sortValue;
	}


	public void setSortValue(Integer sortValue) {
		this.sortValue = sortValue;
	}


	public WebshopItemReferenceInfo getReferenceItemInfo() {
		return referenceItemInfo;
	}


	public void setReferenceItemInfo(WebshopItemReferenceInfo referenceItemInfo) {
		this.referenceItemInfo = referenceItemInfo;
	}


	public WebshopSeoInfo getSeoInfo() {
		return seoInfo;
	}


	public void setSeoInfo(WebshopSeoInfo seoInfo) {
		this.seoInfo = seoInfo;
	}	
}
