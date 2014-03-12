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
package com.lp.server.partner.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CustomerPricelistReportDto implements Serializable {
	private static final long serialVersionUID = 8982294155002016054L;

	private List<CustomerPricelistItemDto> items ;
	private IdValueDto kunde ;
	private IdValueDto artikelgruppe ;
	private IdValueDto artikelklasse ;
	private String artikelnrvon ;
	private String artikelnrbis ;
	private Boolean mitVersteckten ;
	private Boolean nurSoko ;
	private Boolean mitMandantensprache ;
//	private Date gueltigkeitsdatum ;
	private Boolean onlyWebshopItems ;
	private Long gueltigkeitsMs ;
	
	public CustomerPricelistReportDto() {
		items = new ArrayList<CustomerPricelistItemDto>() ;
	}
	
	public List<CustomerPricelistItemDto> getItems() {
		return items;
	}
	public void setItems(List<CustomerPricelistItemDto> items) {
		this.items = items;
	}
	public IdValueDto getCustomer() {
		return kunde;
	}
	public void setCustomer(IdValueDto kunde) {
		this.kunde = kunde;
	}
	public IdValueDto getItemgroup() {
		return artikelgruppe;
	}
	public void setItemgroup(IdValueDto artikelgruppe) {
		this.artikelgruppe = artikelgruppe;
	}
	public IdValueDto getItemclass() {
		return artikelklasse;
	}
	public void setItemclass(IdValueDto artikelklasse) {
		this.artikelklasse = artikelklasse;
	}
	public String getItemRangeFrom() {
		return artikelnrvon;
	}
	public void setItemRangeFrom(String artikelnrvon) {
		this.artikelnrvon = artikelnrvon;
	}
	public String getItemRangeTo() {
		return artikelnrbis;
	}
	public void setItemRangeTo(String artikelnrbis) {
		this.artikelnrbis = artikelnrbis;
	}
	public Boolean getWithHidden() {
		return mitVersteckten;
	}
	public void setWithHidden(Boolean mitVersteckten) {
		this.mitVersteckten = mitVersteckten;
	}
	public Boolean getOnlySpecialCondition() {
		return nurSoko;
	}
	public void setOnlySpecialCondition(Boolean nurSoko) {
		this.nurSoko = nurSoko;
	}
	public Boolean getWithClientLanguage() {
		return mitMandantensprache;
	}
	public void setWithClientLanguage(Boolean mitMandantensprache) {
		this.mitMandantensprache = mitMandantensprache;
	}
//	public Date getPriceValidityDate() {
//		return gueltigkeitsdatum;
//	}
//	public void setPriceValidityDate(Date gueltigkeitsdatum) {
//		this.gueltigkeitsdatum = gueltigkeitsdatum;
//	}
	
	public Long getPriceValidityMs() {
		return gueltigkeitsMs;
	}

	public void setPriceValidityMs(Long gueltigkeitsMs) {
		this.gueltigkeitsMs = gueltigkeitsMs;
	}

	public Boolean getOnlyWebshopItems() {
		return onlyWebshopItems;
	}

	public void setOnlyWebshopItems(Boolean onlyWebshopItems) {
		this.onlyWebshopItems = onlyWebshopItems;
	}	
}
