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
package com.lp.server.artikel.fastlanereader.generated;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class FLRMaterialzuschlag implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Integer i_id;

	/** nullable persistent field */
	private String material_i_id;

	/** nullable persistent field */
	private String mandant_c_nr;

	/** nullable persistent field */
	private Date t_gueltigab;

	/** nullable persistent field */
	private BigDecimal n_zuschlag;

	/** full constructor */
	public FLRMaterialzuschlag(String material_i_id, String mandant_c_nr,
			Date t_gueltigab, BigDecimal n_zuschlag) {
		this.material_i_id = material_i_id;
		this.mandant_c_nr = mandant_c_nr;
		this.t_gueltigab = t_gueltigab;
		this.n_zuschlag = n_zuschlag;
	}

	/** default constructor */
	public FLRMaterialzuschlag() {
	}

	public Integer getI_id() {
		return this.i_id;
	}

	public void setI_id(Integer i_id) {
		this.i_id = i_id;
	}

	public String getMaterial_i_id() {
		return this.material_i_id;
	}

	public void setMaterial_i_id(String material_i_id) {
		this.material_i_id = material_i_id;
	}

	public String getMandant_c_nr() {
		return this.mandant_c_nr;
	}

	public void setMandant_c_nr(String mandant_c_nr) {
		this.mandant_c_nr = mandant_c_nr;
	}

	public Date getT_gueltigab() {
		return this.t_gueltigab;
	}

	public void setT_gueltigab(Date t_gueltigab) {
		this.t_gueltigab = t_gueltigab;
	}

	public BigDecimal getN_zuschlag() {
		return this.n_zuschlag;
	}

	public void setN_zuschlag(BigDecimal n_zuschlag) {
		this.n_zuschlag = n_zuschlag;
	}

	public String toString() {
		return new ToStringBuilder(this).append("i_id", getI_id()).toString();
	}

}
