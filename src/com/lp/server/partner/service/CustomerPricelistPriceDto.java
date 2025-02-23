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
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CustomerPricelistPriceDto implements Serializable {
	private static final long serialVersionUID = -1906865937470114761L;

	public final static String PREISTYP_VKSTAFFELPREIS    = "VK-Staffelpreis" ;
	public final static String PREISTYP_VKPREISBASIS      = "VK-Preisbasis" ;
	public final static String PREISTYP_SOKOARTIKEL       = "Soko-Artikel" ;
	public final static String PREISTYP_SOKOARTIKELGRUPPE = "Soko-Artikelgruppe" ;
	public final static String PREISTYP_SOKOARTIKELGRUPPEVKSTAFFEL = "Soko-Artgru/VK-Staffel" ;
	public final static String PREISTYP_SOKOARTIKELVKSTAFFEL = "Soko-Artikel/VK-Staffel" ;
	
	private BigDecimal menge ;               // zeile 0 Menge
	private String preistypKey ;             // zeile 1 Basis
	private BigDecimal basispreis ;          // zeile 2 BasisPreis
	private BigDecimal fixpreis ;            // zeile 3 FixPreis
	private Double     rabattsatz ;          // zeile 4 Rabattsatz
	private BigDecimal berechneterpreis ;    // Zeile 5 BerechneterPreis
	private String     waehrung ;            // Zeile 6 Waehrung
	private Boolean    soko ;                // Zeile 7 Soko?

	@Transient
	private transient Integer scale ;
	
	public CustomerPricelistPriceDto() {
		soko = false ;
	}
	
	public CustomerPricelistPriceDto(String preistypKey) {
		this.preistypKey = preistypKey ;
		soko = false ;
	}
	
	public CustomerPricelistPriceDto(String preistypKey, int nachkommastellen) {
		this.preistypKey = preistypKey ;
		soko = false ;
		scale = nachkommastellen ;
	}
	
	private BigDecimal scaledPrice(BigDecimal price) {
		if(scale == null) return price ;
		return price == null ? null : price.setScale(scale, RoundingMode.HALF_EVEN);
	}
	
	/**
	 * Die Menge, ab der (einschlie&szlig;lich) der Preis gilt
	 * @return die Menge, aber der - einschlie&szlig;lich der Preis gilt
	 */
	public BigDecimal getAmount() { 
		return menge;
	}
	public void setAmount(BigDecimal menge) {
		this.menge = menge;
	}
	
	/**
	 * Der Preistyp.<br>
	 * <p><ul>
	 * <li><b>"VK-Preisbasis"</b> wenn es der Basispreis ist
	 * <li><b>"VK-Staffelpreis"</b> wenn es sich um einen Staffelpreis handelt
	 * <li><b>"Soko-Artikel"</b> wenn es ein Preis aus einer Sonderkondition dieses Artikels ist
	 * <li><b>"Soko-Artikelgruppe"</b> wenn es ein Preis aus der Sonderkondition der Artikelgruppe ist
	 * <li><b>"Soko-Artgru/VK-Staffel"</b> wenn es ein Preis aus der Sonderkondition der Artikelgruppe mit Mengenstaffel ist 
	 * <li><b>"Soko-Artikel/VK-Staffel"</b> wenn es ein Preis aus der Sonderkondition des Artikels mit Mengenstaffel ist 
	 * </ul></p>
	 * @return der Preistyp
	 */
	public String getPricetypKey() {
		return preistypKey;
	}
	public void setPricetypKey(String preistypKey) {
		this.preistypKey = preistypKey;
	}
	
	/**
	 * Die Preisbasis
	 * @return die Preisbasis des Artikels
	 */
	public BigDecimal getBasePrice() {
		return basispreis;
	}
	public void setBasePrice(BigDecimal basispreis) {
		this.basispreis = scaledPrice(basispreis) ;
	}
	
	/**
	 * Der Fixpreis des Artikels
	 * @return der (optionale) Fixpreis des Artikels 
	 */
	public BigDecimal getFixPrice() {
		return fixpreis;
	}
	public void setFixPrice(BigDecimal fixpreis) {
		this.fixpreis = scaledPrice(fixpreis) ;
	}
	public Double getDiscountRate() {
		return rabattsatz;
	}
	
	/** 
	 * Rabattsatz
	 * @param rabattsatz sind die (auch negative) Rabattprozent f&uuml;r diesen Preis
	 */
	public void setDiscountRate(Double rabattsatz) {
		this.rabattsatz = rabattsatz;
	}
	
	/**
	 * Der berechnete Preis<br>
	 * @return der berechnete Preis (inklusiver aller Rabatte)
	 */
	public BigDecimal getCalculatedPrice() {
		return berechneterpreis;
	}
	public void setCalculatedPrice(BigDecimal berechneterpreis) {
		this.berechneterpreis = scaledPrice(berechneterpreis);
	}
	
	/**
	 * Die W&auml;hrung des Preises
	 * @return die W&auml;hrung 
	 */
	public String getCurrency() {
		return waehrung;
	}
	public void setCurrency(String waehrung) {
		this.waehrung = waehrung;
	}
	
	/**
	 * Handelt es sich um einen Preis aus einer Sonderkondition?
	 * @return true wenn es ein Sonderkonditionenpreis ist
	 */
	public Boolean getSpecialCondition() {
		return soko;
	}
	public void setSpecialCondition(Boolean soko) {
		this.soko = soko;
	}	
}
