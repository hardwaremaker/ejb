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
package com.lp.server.system.fastlanereader.generated;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class FLRBelegartdokument implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Integer i_id;

	/** nullable persistent field */
	private Integer dokument_i_id;

	/** nullable persistent field */
	private String belegart_c_nr;

	/** nullable persistent field */
	private Integer i_belegartid;

	/** nullable persistent field */
	private Date t_anlegen;

	/** nullable persistent field */
	private Integer i_sort;

	/** nullable persistent field */
	private com.lp.server.system.fastlanereader.generated.FLRDokument flrdokument;

	/** full constructor */
	public FLRBelegartdokument(
			Integer dokument_i_id,
			String belegart_c_nr,
			Integer i_belegartid,
			Date t_anlegen,
			Integer i_sort,
			com.lp.server.system.fastlanereader.generated.FLRDokument flrdokument) {
		this.dokument_i_id = dokument_i_id;
		this.belegart_c_nr = belegart_c_nr;
		this.i_belegartid = i_belegartid;
		this.t_anlegen = t_anlegen;
		this.i_sort = i_sort;
		this.flrdokument = flrdokument;
	}

	/** default constructor */
	public FLRBelegartdokument() {
	}

	public Integer getI_id() {
		return this.i_id;
	}

	public void setI_id(Integer i_id) {
		this.i_id = i_id;
	}

	public Integer getDokument_i_id() {
		return this.dokument_i_id;
	}

	public void setDokument_i_id(Integer dokument_i_id) {
		this.dokument_i_id = dokument_i_id;
	}

	public String getBelegart_c_nr() {
		return this.belegart_c_nr;
	}

	public void setBelegart_c_nr(String belegart_c_nr) {
		this.belegart_c_nr = belegart_c_nr;
	}

	public Integer getI_belegartid() {
		return this.i_belegartid;
	}

	public void setI_belegartid(Integer i_belegartid) {
		this.i_belegartid = i_belegartid;
	}

	public Date getT_anlegen() {
		return this.t_anlegen;
	}

	public void setT_anlegen(Date t_anlegen) {
		this.t_anlegen = t_anlegen;
	}

	public Integer getI_sort() {
		return this.i_sort;
	}

	public void setI_sort(Integer i_sort) {
		this.i_sort = i_sort;
	}

	public com.lp.server.system.fastlanereader.generated.FLRDokument getFlrdokument() {
		return this.flrdokument;
	}

	public void setFlrdokument(
			com.lp.server.system.fastlanereader.generated.FLRDokument flrdokument) {
		this.flrdokument = flrdokument;
	}

	public String toString() {
		return new ToStringBuilder(this).append("i_id", getI_id()).toString();
	}

}
