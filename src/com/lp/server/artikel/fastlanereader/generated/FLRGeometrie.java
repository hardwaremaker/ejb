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
package com.lp.server.artikel.fastlanereader.generated;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class FLRGeometrie implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Integer i_id;

	/** nullable persistent field */
	private Double f_breite;

	/** nullable persistent field */
	private Double f_hoehe;

	/** nullable persistent field */
	private Double f_tiefe;

	/** nullable persistent field */
	private String c_breitetext;

	/** full constructor */
	public FLRGeometrie(Double f_breite, Double f_hoehe, Double f_tiefe,
			String c_breitetext) {
		this.f_breite = f_breite;
		this.f_hoehe = f_hoehe;
		this.f_tiefe = f_tiefe;
		this.c_breitetext = c_breitetext;
	}

	/** default constructor */
	public FLRGeometrie() {
	}

	public Integer getI_id() {
		return this.i_id;
	}

	public void setI_id(Integer i_id) {
		this.i_id = i_id;
	}

	public Double getF_breite() {
		return this.f_breite;
	}

	public void setF_breite(Double f_breite) {
		this.f_breite = f_breite;
	}

	public Double getF_hoehe() {
		return this.f_hoehe;
	}

	public void setF_hoehe(Double f_hoehe) {
		this.f_hoehe = f_hoehe;
	}

	public Double getF_tiefe() {
		return this.f_tiefe;
	}

	public void setF_tiefe(Double f_tiefe) {
		this.f_tiefe = f_tiefe;
	}

	public String getC_breitetext() {
		return this.c_breitetext;
	}

	public void setC_breitetext(String c_breitetext) {
		this.c_breitetext = c_breitetext;
	}

	public String toString() {
		return new ToStringBuilder(this).append("i_id", getI_id()).toString();
	}

}
