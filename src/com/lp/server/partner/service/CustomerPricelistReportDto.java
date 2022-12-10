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
/**
 * Enth&auml;lt Informationen f&uuml;r die Kundenpreisliste<br>
 * @author gerold
 */
public class CustomerPricelistReportDto implements Serializable {
	private static final long serialVersionUID = 8982294155002016054L;

	private List<CustomerPricelistItemDto> items ;
	private IdValueDto kunde ;
	private IdValueDto artikelgruppe ;
	private IdValueDto artikelklasse ;
	private String artikelnrvon ;
	private String artikelnrbis ;
	private Boolean mitVersteckten ;
	private Boolean mitGesperrten ;
	private Boolean nurSoko ;
	private Boolean bVKMengenstaffelAnstattSokoMengestaffel ;
	private IdValueDto shopgruppe ;
	
	private Boolean mitMandantensprache ;
//	private Date gueltigkeitsdatum ;
	private Boolean onlyWebshopItems ;
	private Long gueltigkeitsMs ;
	
	public CustomerPricelistReportDto() {
		items = new ArrayList<CustomerPricelistItemDto>() ;
	}
	
	/**
	 * Die (leere) Liste aller Artikel (mit Preisinformation) dieser Preisliste
	 * @return die (leere) Liste aller Artikel (mit Preisinformation) 
	 */
	public List<CustomerPricelistItemDto> getItems() {
		return items;
	}
	public void setItems(List<CustomerPricelistItemDto> items) {
		this.items = items;
	}
	
	/**
	 * Die Kundeninformation<br>
	 * <p>Enth&auml;lt die Id und den Namen des Kunden</p>
	 * 
	 * @return die Kundeninformation
	 */
	public IdValueDto getCustomer() {
		return kunde;
	}
	public void setCustomer(IdValueDto kunde) {
		this.kunde = kunde;
	}
	
	/** 
	 * Die Information &uuml;ber die Artikelgruppe<br>
	 * <p>Die Information wird nur &uuml;bermittelt, wenn die Artikelgruppe
	 * explizit eingeschr&auml;nkt worden ist</p>
	 * @return die (optionale) Information &uuml;ber die Artikelgruppe.
	 */
	public IdValueDto getItemgroup() {
		return artikelgruppe;
	}
	public void setItemgroup(IdValueDto artikelgruppe) {
		this.artikelgruppe = artikelgruppe;
	}
	/** 
	 * Die Information &uuml;ber die Shopgruppe<br>
	 * <p>Die Information wird nur &uuml;bermittelt, wenn die Shopgruppe
	 * explizit eingeschr&auml;nkt worden ist</p>
	 * @return die (optionale) Information &uuml;ber die Shopgruppe.
	 */
	public IdValueDto getShopgroup() {
		return shopgruppe;
	}
	public void setShoproup(IdValueDto shopgruppe) {
		this.shopgruppe = shopgruppe;
	}
	
	/** 
	 * Die Information &uuml;ber die Artikelklasse<br>
	 * <p>Die Information wird nur &uuml;bermittelt, wenn die Artikelklasse
	 * explizit eingeschr&auml;nkt worden ist</p>
	 * @return die (optionale) Information &uuml;ber die Artikelklasse.
	 */
	public IdValueDto getItemclass() {
		return artikelklasse;
	}
	public void setItemclass(IdValueDto artikelklasse) {
		this.artikelklasse = artikelklasse;
	}
	
	/**
	 * Die Vongrenze der Einschr&auml;nkung der Artikelnummern
	 * @return die Vongrenze der Artikelnummer
	 */
	public String getItemRangeFrom() {
		return artikelnrvon;
	}
	public void setItemRangeFrom(String artikelnrvon) {
		this.artikelnrvon = artikelnrvon;
	}
	
	/**
	 * Die Bisgrenze der Einschr&auml;nkung der Artikelnummer
	 * @return die Bisgrenze der Artikelnummer
	 */
	public String getItemRangeTo() {
		return artikelnrbis;
	}
	public void setItemRangeTo(String artikelnrbis) {
		this.artikelnrbis = artikelnrbis;
	}
	
	/**
	 * Sollen auch versteckte Artikel ausgegeben werden?
	 * @return true wenn auch versteckte Artikel ausgegeben werden
	 */
	public Boolean getWithHidden() {
		return mitVersteckten;
	}
	public void setWithHidden(Boolean mitVersteckten) {
		this.mitVersteckten = mitVersteckten;
	}
	
	public Boolean getWithBlocked() {
		return mitGesperrten;
	}
	public void setWithBlocked(Boolean mitGesperrten) {
		this.mitGesperrten = mitGesperrten;
	}
	
	/**
	 * Bei den Artikel nur die Soko (Sonderkonditionen) ausgeben? 
	 * @return true wenn nur die Sonderkonditionspreise ausgegeben werden
	 */
	public Boolean getOnlySpecialCondition() {
		return nurSoko;
	}
	public void setOnlySpecialCondition(Boolean nurSoko) {
		this.nurSoko = nurSoko;
	}
	
	/**
	 * Werden Bezeichnungen zus&auml;tzlich zur Kundensprache auch in der
	 * Mandantensprache ausgegeben?
	 * @return true wenn auch die Mandantensprache ausgegeben wird
	 */
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
	
	/**
	 * Das Datum (in ms seit 1.1.1970) ab dem die Preisliste g&uuml;ltig ist
	 * @return das Datum der Preisg&uuml;ltigkeit in ms
	 */
	public Long getPriceValidityMs() {
		return gueltigkeitsMs;
	}

	public void setPriceValidityMs(Long gueltigkeitsMs) {
		this.gueltigkeitsMs = gueltigkeitsMs;
	}

	/**
	 * Wurden alle Artikel oder nur jene Artikel die im Webshop verf&uuml;gbar sein sollen
	 * angefordert
	 * @return true wenn nur Artikel enthalten sind, die f&uuml;r den Webshop zug&auml;nglich sind
	 */
	public Boolean getOnlyWebshopItems() {
		return onlyWebshopItems;
	}

	public void setOnlyWebshopItems(Boolean onlyWebshopItems) {
		this.onlyWebshopItems = onlyWebshopItems;
	}
	
	/**
	 * Wurde die Mengenstaffel, oder die SonderkonditionenMengenstaffel angewandt?<br>
	 * <p>Ob die Mengenstaffel verwendet wird, oder die Sonderkonditionenmengenstaffel
	 * wird mittels Parameter direkt im ERP beeinflusst</p>
	 * 
	 * @return true wenn die Mengenstaffel angewandt wurde, ansonsten false.
	 */
	public Boolean getAppliedQuantityScale() {
		return bVKMengenstaffelAnstattSokoMengestaffel;
	}

	public void setAppliedQuantityScale(
			Boolean bVKMengenstaffelAnstattSokoMengestaffel) {
		this.bVKMengenstaffelAnstattSokoMengestaffel = bVKMengenstaffelAnstattSokoMengestaffel;
	}
}
