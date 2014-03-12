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
package com.lp.server.angebotstkl.fastlanereader.generated;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.lp.server.partner.fastlanereader.generated.FLRKunde;

/** @author Hibernate CodeGenerator */
public class FLREinkaufsangebot implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Integer i_id;

	/** nullable persistent field */
	private String c_nr;

	/** nullable persistent field */
	private String mandant_c_nr;

	/** nullable persistent field */
	private String c_projekt;

	/** nullable persistent field */
	private Date t_belegdatum;

	/** nullable persistent field */
	private BigDecimal n_menge1;

	/** nullable persistent field */
	private BigDecimal n_menge2;

	/** nullable persistent field */
	private BigDecimal n_menge3;

	/** nullable persistent field */
	private BigDecimal n_menge4;

	/** nullable persistent field */
	private BigDecimal n_menge5;

	/** nullable persistent field */
	private FLRKunde flrkunde;

	/** full constructor */
	public FLREinkaufsangebot(String c_nr, String mandant_c_nr,
			String c_projekt, Date t_belegdatum, BigDecimal n_menge1,
			BigDecimal n_menge2, BigDecimal n_menge3, BigDecimal n_menge4,
			BigDecimal n_menge5, FLRKunde flrkunde) {
		this.c_nr = c_nr;
		this.mandant_c_nr = mandant_c_nr;
		this.c_projekt = c_projekt;
		this.t_belegdatum = t_belegdatum;
		this.n_menge1 = n_menge1;
		this.n_menge2 = n_menge2;
		this.n_menge3 = n_menge3;
		this.n_menge4 = n_menge4;
		this.n_menge5 = n_menge5;
		this.flrkunde = flrkunde;
	}

	/** default constructor */
	public FLREinkaufsangebot() {
	}

	public Integer getI_id() {
		return this.i_id;
	}

	public void setI_id(Integer i_id) {
		this.i_id = i_id;
	}

	public String getC_nr() {
		return this.c_nr;
	}

	public void setC_nr(String c_nr) {
		this.c_nr = c_nr;
	}

	public String getMandant_c_nr() {
		return this.mandant_c_nr;
	}

	public void setMandant_c_nr(String mandant_c_nr) {
		this.mandant_c_nr = mandant_c_nr;
	}

	public String getC_projekt() {
		return this.c_projekt;
	}

	public void setC_projekt(String c_projekt) {
		this.c_projekt = c_projekt;
	}

	public Date getT_belegdatum() {
		return this.t_belegdatum;
	}

	public void setT_belegdatum(Date t_belegdatum) {
		this.t_belegdatum = t_belegdatum;
	}

	public BigDecimal getN_menge1() {
		return this.n_menge1;
	}

	public void setN_menge1(BigDecimal n_menge1) {
		this.n_menge1 = n_menge1;
	}

	public BigDecimal getN_menge2() {
		return this.n_menge2;
	}

	public void setN_menge2(BigDecimal n_menge2) {
		this.n_menge2 = n_menge2;
	}

	public BigDecimal getN_menge3() {
		return this.n_menge3;
	}

	public void setN_menge3(BigDecimal n_menge3) {
		this.n_menge3 = n_menge3;
	}

	public BigDecimal getN_menge4() {
		return this.n_menge4;
	}

	public void setN_menge4(BigDecimal n_menge4) {
		this.n_menge4 = n_menge4;
	}

	public BigDecimal getN_menge5() {
		return this.n_menge5;
	}

	public void setN_menge5(BigDecimal n_menge5) {
		this.n_menge5 = n_menge5;
	}

	public FLRKunde getFlrkunde() {
		return this.flrkunde;
	}

	public void setFlrkunde(FLRKunde flrkunde) {
		this.flrkunde = flrkunde;
	}

	public String toString() {
		return new ToStringBuilder(this).append("i_id", getI_id()).toString();
	}

}
