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
package com.lp.server.bestellung.fastlanereader.generated;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;

/** @author Hibernate CodeGenerator */
public class FLRBestellpositionSichtRahmen implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Integer i_id;

	/** nullable persistent field */
	private Integer i_sort;

	/** nullable persistent field */
	private BigDecimal n_menge;

	/** nullable persistent field */
	private String bestellpositionstatus_c_nr;

	/** nullable persistent field */
	private String einheit_c_nr;

	/** nullable persistent field */
	private BigDecimal n_offenemenge;

	/** nullable persistent field */
	private String bestellpositionart_c_nr;

	/** nullable persistent field */
	private FLRArtikel flrartikel;

	/** nullable persistent field */
	private com.lp.server.bestellung.fastlanereader.generated.FLRBestellung flrbestellung;

	/** full constructor */
	public FLRBestellpositionSichtRahmen(
			Integer i_sort,
			BigDecimal n_menge,
			String bestellpositionstatus_c_nr,
			String einheit_c_nr,
			BigDecimal n_offenemenge,
			String bestellpositionart_c_nr,
			FLRArtikel flrartikel,
			com.lp.server.bestellung.fastlanereader.generated.FLRBestellung flrbestellung) {
		this.i_sort = i_sort;
		this.n_menge = n_menge;
		this.bestellpositionstatus_c_nr = bestellpositionstatus_c_nr;
		this.einheit_c_nr = einheit_c_nr;
		this.n_offenemenge = n_offenemenge;
		this.bestellpositionart_c_nr = bestellpositionart_c_nr;
		this.flrartikel = flrartikel;
		this.flrbestellung = flrbestellung;
	}

	/** default constructor */
	public FLRBestellpositionSichtRahmen() {
	}

	public Integer getI_id() {
		return this.i_id;
	}

	public void setI_id(Integer i_id) {
		this.i_id = i_id;
	}

	public Integer getI_sort() {
		return this.i_sort;
	}

	public void setI_sort(Integer i_sort) {
		this.i_sort = i_sort;
	}

	public BigDecimal getN_menge() {
		return this.n_menge;
	}

	public void setN_menge(BigDecimal n_menge) {
		this.n_menge = n_menge;
	}

	public String getBestellpositionstatus_c_nr() {
		return this.bestellpositionstatus_c_nr;
	}

	public void setBestellpositionstatus_c_nr(String bestellpositionstatus_c_nr) {
		this.bestellpositionstatus_c_nr = bestellpositionstatus_c_nr;
	}

	public String getEinheit_c_nr() {
		return this.einheit_c_nr;
	}

	public void setEinheit_c_nr(String einheit_c_nr) {
		this.einheit_c_nr = einheit_c_nr;
	}

	public BigDecimal getN_offenemenge() {
		return this.n_offenemenge;
	}

	public void setN_offenemenge(BigDecimal n_offenemenge) {
		this.n_offenemenge = n_offenemenge;
	}

	public String getBestellpositionart_c_nr() {
		return this.bestellpositionart_c_nr;
	}

	public void setBestellpositionart_c_nr(String bestellpositionart_c_nr) {
		this.bestellpositionart_c_nr = bestellpositionart_c_nr;
	}

	public FLRArtikel getFlrartikel() {
		return this.flrartikel;
	}

	public void setFlrartikel(FLRArtikel flrartikel) {
		this.flrartikel = flrartikel;
	}

	public com.lp.server.bestellung.fastlanereader.generated.FLRBestellung getFlrbestellung() {
		return this.flrbestellung;
	}

	public void setFlrbestellung(
			com.lp.server.bestellung.fastlanereader.generated.FLRBestellung flrbestellung) {
		this.flrbestellung = flrbestellung;
	}

	public String toString() {
		return new ToStringBuilder(this).append("i_id", getI_id()).toString();
	}

}
