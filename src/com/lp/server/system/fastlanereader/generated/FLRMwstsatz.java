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
package com.lp.server.system.fastlanereader.generated;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class FLRMwstsatz implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** identifier field */
	private Integer i_id;

	/** persistent field */
	private Integer mwstbez_i_id;

	/** persistent field */
	private Date d_gueltigab;

	/** persistent field */
	private Double f_mwstsatz;

	/** nullable persistent field */
	private com.lp.server.system.fastlanereader.generated.FLRMwstsatzbez flrmwstsatzbez;

	/** full constructor */
	public FLRMwstsatz(
			Integer mwstbez_i_id,
			Date d_gueltigab,
			Double f_mwstsatz,
			com.lp.server.system.fastlanereader.generated.FLRMwstsatzbez flrmwstsatzbez) {
		this.mwstbez_i_id = mwstbez_i_id;
		this.d_gueltigab = d_gueltigab;
		this.f_mwstsatz = f_mwstsatz;
		this.flrmwstsatzbez = flrmwstsatzbez;
	}

	/** default constructor */
	public FLRMwstsatz() {
	}

	/** minimal constructor */
	public FLRMwstsatz(Integer mwstbez_i_id, Date d_gueltigab, Double f_mwstsatz) {
		this.mwstbez_i_id = mwstbez_i_id;
		this.d_gueltigab = d_gueltigab;
		this.f_mwstsatz = f_mwstsatz;
	}

	public Integer getI_id() {
		return this.i_id;
	}

	public void setI_id(Integer i_id) {
		this.i_id = i_id;
	}

	public Integer getMwstbez_i_id() {
		return this.mwstbez_i_id;
	}

	public void setMwstbez_i_id(Integer mwstbez_i_id) {
		this.mwstbez_i_id = mwstbez_i_id;
	}

	public Date getD_gueltigab() {
		return this.d_gueltigab;
	}

	public void setD_gueltigab(Date d_gueltigab) {
		this.d_gueltigab = d_gueltigab;
	}

	public Double getF_mwstsatz() {
		return this.f_mwstsatz;
	}

	public void setF_mwstsatz(Double f_mwstsatz) {
		this.f_mwstsatz = f_mwstsatz;
	}

	public com.lp.server.system.fastlanereader.generated.FLRMwstsatzbez getFlrmwstsatzbez() {
		return this.flrmwstsatzbez;
	}

	public void setFlrmwstsatzbez(
			com.lp.server.system.fastlanereader.generated.FLRMwstsatzbez flrmwstsatzbez) {
		this.flrmwstsatzbez = flrmwstsatzbez;
	}

	public String toString() {
		return new ToStringBuilder(this).append("i_id", getI_id()).toString();
	}

}
