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
package com.lp.server.personal.fastlanereader.generated;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class FLRPersonalgehalt implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	/** identifier field */
	private Integer i_id;

	/** nullable persistent field */
	private Integer personal_i_id;

	/** nullable persistent field */
	private Integer i_jahr;

	/** nullable persistent field */
	private Integer i_monat;

	/** nullable persistent field */
	private BigDecimal n_gehalt;

	/** nullable persistent field */
	private Double f_uestpauschale;

	/** nullable persistent field */
	private BigDecimal n_stundensatz;

	/** full constructor */
	public FLRPersonalgehalt(Integer personal_i_id, Integer i_jahr,
			Integer i_monat, BigDecimal n_gehalt, Double f_uestpauschale,
			BigDecimal n_stundensatz) {
		this.personal_i_id = personal_i_id;
		this.i_jahr = i_jahr;
		this.i_monat = i_monat;
		this.n_gehalt = n_gehalt;
		this.f_uestpauschale = f_uestpauschale;
		this.n_stundensatz = n_stundensatz;
	}

	/** default constructor */
	public FLRPersonalgehalt() {
	}

	public Integer getI_id() {
		return this.i_id;
	}

	public void setI_id(Integer i_id) {
		this.i_id = i_id;
	}

	public Integer getPersonal_i_id() {
		return this.personal_i_id;
	}

	public void setPersonal_i_id(Integer personal_i_id) {
		this.personal_i_id = personal_i_id;
	}

	public Integer getI_jahr() {
		return this.i_jahr;
	}

	public void setI_jahr(Integer i_jahr) {
		this.i_jahr = i_jahr;
	}

	public Integer getI_monat() {
		return this.i_monat;
	}

	public void setI_monat(Integer i_monat) {
		this.i_monat = i_monat;
	}

	public BigDecimal getN_gehalt() {
		return this.n_gehalt;
	}

	public void setN_gehalt(BigDecimal n_gehalt) {
		this.n_gehalt = n_gehalt;
	}

	public Double getF_uestpauschale() {
		return this.f_uestpauschale;
	}

	public void setF_uestpauschale(Double f_uestpauschale) {
		this.f_uestpauschale = f_uestpauschale;
	}

	public BigDecimal getN_stundensatz() {
		return this.n_stundensatz;
	}

	public void setN_stundensatz(BigDecimal n_stundensatz) {
		this.n_stundensatz = n_stundensatz;
	}

	public String toString() {
		return new ToStringBuilder(this).append("i_id", getI_id()).toString();
	}

}
