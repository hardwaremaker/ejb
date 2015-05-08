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
package com.lp.server.fertigung.fastlanereader.generated;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.lp.server.stueckliste.fastlanereader.generated.FLRStueckliste;

/** @author Hibernate CodeGenerator */
public class FLRInternebestellung implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Integer i_id;

	/** nullable persistent field */
	private String mandant_c_nr;

	/** nullable persistent field */
	private String belegart_c_nr;

	/** nullable persistent field */
	private Integer i_belegiid;

	/** nullable persistent field */
	private Integer i_belegpositioniid;

	/** nullable persistent field */
	private Integer stueckliste_i_id;

	/** nullable persistent field */
	private BigDecimal n_menge;

	/** nullable persistent field */
	private Date t_liefertermin;

	/** nullable persistent field */
	private FLRStueckliste flrstueckliste;

	/** full constructor */
	public FLRInternebestellung(String mandant_c_nr, String belegart_c_nr,
			Integer i_belegiid, Integer i_belegpositioniid,
			Integer stueckliste_i_id, BigDecimal n_menge, Date t_liefertermin,
			FLRStueckliste flrstueckliste) {
		this.mandant_c_nr = mandant_c_nr;
		this.belegart_c_nr = belegart_c_nr;
		this.i_belegiid = i_belegiid;
		this.i_belegpositioniid = i_belegpositioniid;
		this.stueckliste_i_id = stueckliste_i_id;
		this.n_menge = n_menge;
		this.t_liefertermin = t_liefertermin;
		this.flrstueckliste = flrstueckliste;
	}

	/** default constructor */
	public FLRInternebestellung() {
	}

	public Integer getI_id() {
		return this.i_id;
	}

	public void setI_id(Integer i_id) {
		this.i_id = i_id;
	}

	public String getMandant_c_nr() {
		return this.mandant_c_nr;
	}

	public void setMandant_c_nr(String mandant_c_nr) {
		this.mandant_c_nr = mandant_c_nr;
	}

	public String getBelegart_c_nr() {
		return this.belegart_c_nr;
	}

	public void setBelegart_c_nr(String belegart_c_nr) {
		this.belegart_c_nr = belegart_c_nr;
	}

	public Integer getI_belegiid() {
		return this.i_belegiid;
	}

	public void setI_belegiid(Integer i_belegiid) {
		this.i_belegiid = i_belegiid;
	}

	public Integer getI_belegpositioniid() {
		return this.i_belegpositioniid;
	}

	public void setI_belegpositioniid(Integer i_belegpositioniid) {
		this.i_belegpositioniid = i_belegpositioniid;
	}

	public Integer getStueckliste_i_id() {
		return this.stueckliste_i_id;
	}

	public void setStueckliste_i_id(Integer stueckliste_i_id) {
		this.stueckliste_i_id = stueckliste_i_id;
	}

	public BigDecimal getN_menge() {
		return this.n_menge;
	}

	public void setN_menge(BigDecimal n_menge) {
		this.n_menge = n_menge;
	}

	public Date getT_liefertermin() {
		return this.t_liefertermin;
	}

	public void setT_liefertermin(Date t_liefertermin) {
		this.t_liefertermin = t_liefertermin;
	}

	public FLRStueckliste getFlrstueckliste() {
		return this.flrstueckliste;
	}

	public void setFlrstueckliste(FLRStueckliste flrstueckliste) {
		this.flrstueckliste = flrstueckliste;
	}

	public String toString() {
		return new ToStringBuilder(this).append("i_id", getI_id()).toString();
	}

}
