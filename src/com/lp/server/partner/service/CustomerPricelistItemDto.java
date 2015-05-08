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
package com.lp.server.partner.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CustomerPricelistItemDto implements Serializable {
	private static final long serialVersionUID = -4700070618173130204L;

	private IdValueDto artikel ;
	private CustomerPricelistItemDescriptionDto descriptionDto ;
	private CustomerPricelistItemDescriptionDto clientDescriptionDto ;
	
	private String einheit ;
	private String gruppe ;
	private String klasse ;
	private Boolean versteckt ;
	private Boolean soko ;
	private Integer mwstsatzId ;
//	private Integer artikelgruppeId ;
	private IdValueDto artikelgruppe ;
	private List<CustomerPricelistPriceDto> preise ;
	private CustomerPricelistShopgroupDto shopgroupDto ;
	
	public CustomerPricelistItemDto() {
		preise = new ArrayList<CustomerPricelistPriceDto>() ;
		artikel = new IdValueDto() ;
		descriptionDto = new CustomerPricelistItemDescriptionDto() ;
		clientDescriptionDto = new CustomerPricelistItemDescriptionDto() ;
	}
	
	public CustomerPricelistItemDto(Integer id, String artikelnummer) {
		preise = new ArrayList<CustomerPricelistPriceDto>() ;
		artikel = new IdValueDto(id, artikelnummer) ;
		descriptionDto = new CustomerPricelistItemDescriptionDto() ;
		clientDescriptionDto = new CustomerPricelistItemDescriptionDto() ;
	}
	
	public IdValueDto getItem() {
		return artikel;
	}
	public void setItem(IdValueDto artikel) {
		this.artikel = artikel;
	}
	

	public CustomerPricelistItemDescriptionDto getDescriptionDto() {
		return descriptionDto;
	}

	public void setDescriptionDto(
			CustomerPricelistItemDescriptionDto bezeichnungDto) {
		this.descriptionDto = bezeichnungDto;
	}

	public CustomerPricelistItemDescriptionDto getClientDescriptionDto() {
		return clientDescriptionDto;
	}

	public void setClientDescriptionDto(
			CustomerPricelistItemDescriptionDto mandantenbezeichnungDto) {
		this.clientDescriptionDto = mandantenbezeichnungDto;
	}

	public String getName() {
		return descriptionDto.getName() ;
	}
	public void setName(String bezeichnung) {
		descriptionDto.setName(bezeichnung) ;
	}	
	public String getShortName() {
		return descriptionDto.getShortName() ;
	}
	public void setShortName(String kurzbezeichnung) {
		descriptionDto.setShortName(kurzbezeichnung);
	}

	public String getAdditionalName() {
		return descriptionDto.getAdditionalName() ;
	}
	public void setAdditionalName(String zusatzbezeichnung) {
		descriptionDto.setAdditionalName(zusatzbezeichnung);
	}
	
	public String getAdditionalName2() {
		return descriptionDto.getAdditionalName2() ;
	}
	public void setAdditionalName2(String zusatzbezeichnung2) {
		descriptionDto.setAdditionalName2(zusatzbezeichnung2);
	}

	public String getUnit() {
		return einheit;
	}
	public void setUnit(String einheit) {
		this.einheit = einheit;
	}
	public String getItemGroup() {
		return gruppe;
	}
	public void setItemGroup(String gruppe) {
		this.gruppe = gruppe;
	}
	public String getItemClass() {
		return klasse;
	}
	public void setItemClass(String klasse) {
		this.klasse = klasse;
	}
	public Boolean getHidden() {
		return versteckt;
	}
	public void setHidden(Boolean versteckt) {
		this.versteckt = versteckt;
	}
	public void setHidden(Short versteckt) {
		this.versteckt = versteckt == null ? false : (versteckt == 1) ;
	}
	public Boolean getSpecialCondition() {
		return soko;
	}
	public void setSpecialCondition(Boolean soko) {
		this.soko = soko;
	}	
	public Integer getVATId() {
		return mwstsatzId;
	}
	public void setVATId(Integer mwstsatzId) {
		this.mwstsatzId = mwstsatzId;
	}
//	public Integer getArtikelgruppeId() {
//		return artikelgruppeId;
//	}
//	public void setArtikelgruppeId(Integer artikelgruppeId) {
//		this.artikelgruppeId = artikelgruppeId;
//	}
//
	
	public IdValueDto getItemGroupDto() {
		return artikelgruppe;
	}

	public void setItemGroupDto(IdValueDto artikelgruppe) {
		this.artikelgruppe = artikelgruppe;
	}

	public List<CustomerPricelistPriceDto> getPrices() {
		return preise;
	}
	public void setPrices(List<CustomerPricelistPriceDto> preise) {
		this.preise = preise;
	}

	public CustomerPricelistShopgroupDto getShopgroupDto() {
		return shopgroupDto;
	}

	public void setShopgroupDto(CustomerPricelistShopgroupDto shopgroupDto) {
		this.shopgroupDto = shopgroupDto;
	}	
}
