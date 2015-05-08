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

import org.apache.commons.lang.builder.ToStringBuilder;

import com.lp.server.artikel.fastlanereader.generated.FLRLager;

/** @author Hibernate CodeGenerator */
public class FLRLosistmaterial implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Integer i_id;

	/** nullable persistent field */
	private Integer lossollmaterial_i_id;

	/** nullable persistent field */
	private Integer lager_i_id;

	/** nullable persistent field */
	private BigDecimal n_menge;

	/** nullable persistent field */
	private Short b_abgang;

	/** nullable persistent field */
	private com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial flrlossollmaterial;

	/** nullable persistent field */
	private FLRLager flrlager;

	/** full constructor */
	public FLRLosistmaterial(
			Integer lossollmaterial_i_id,
			Integer lager_i_id,
			BigDecimal n_menge,
			Short b_abgang,
			com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial flrlossollmaterial,
			FLRLager flrlager) {
		this.lossollmaterial_i_id = lossollmaterial_i_id;
		this.lager_i_id = lager_i_id;
		this.n_menge = n_menge;
		this.b_abgang = b_abgang;
		this.flrlossollmaterial = flrlossollmaterial;
		this.flrlager = flrlager;
	}

	/** default constructor */
	public FLRLosistmaterial() {
	}

	public Integer getI_id() {
		return this.i_id;
	}

	public void setI_id(Integer i_id) {
		this.i_id = i_id;
	}

	public Integer getLossollmaterial_i_id() {
		return this.lossollmaterial_i_id;
	}

	public void setLossollmaterial_i_id(Integer lossollmaterial_i_id) {
		this.lossollmaterial_i_id = lossollmaterial_i_id;
	}

	public Integer getLager_i_id() {
		return this.lager_i_id;
	}

	public void setLager_i_id(Integer lager_i_id) {
		this.lager_i_id = lager_i_id;
	}

	public BigDecimal getN_menge() {
		return this.n_menge;
	}

	public void setN_menge(BigDecimal n_menge) {
		this.n_menge = n_menge;
	}

	public Short getB_abgang() {
		return this.b_abgang;
	}

	public void setB_abgang(Short b_abgang) {
		this.b_abgang = b_abgang;
	}

	public com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial getFlrlossollmaterial() {
		return this.flrlossollmaterial;
	}

	public void setFlrlossollmaterial(
			com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial flrlossollmaterial) {
		this.flrlossollmaterial = flrlossollmaterial;
	}

	public FLRLager getFlrlager() {
		return this.flrlager;
	}

	public void setFlrlager(FLRLager flrlager) {
		this.flrlager = flrlager;
	}

	public String toString() {
		return new ToStringBuilder(this).append("i_id", getI_id()).toString();
	}

}
