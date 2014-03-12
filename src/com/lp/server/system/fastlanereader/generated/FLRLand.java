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

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class FLRLand implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Integer i_id;

	/** nullable persistent field */
	private String c_lkz;

	/** nullable persistent field */
	private String c_name;

	/** nullable persistent field */
	private String c_telvorwahl;

	/** nullable persistent field */
	private String waehrung_c_nr;

	/** full constructor */
	public FLRLand(String c_lkz, String c_name, String c_telvorwahl,
			String waehrung_c_nr) {
		this.c_lkz = c_lkz;
		this.c_name = c_name;
		this.c_telvorwahl = c_telvorwahl;
		this.waehrung_c_nr = waehrung_c_nr;
	}

	/** default constructor */
	public FLRLand() {
	}

	public Integer getI_id() {
		return this.i_id;
	}

	public void setI_id(Integer i_id) {
		this.i_id = i_id;
	}

	public String getC_lkz() {
		return this.c_lkz;
	}

	public void setC_lkz(String c_lkz) {
		this.c_lkz = c_lkz;
	}

	public String getC_name() {
		return this.c_name;
	}

	public void setC_name(String c_name) {
		this.c_name = c_name;
	}

	public String getC_telvorwahl() {
		return this.c_telvorwahl;
	}

	public void setC_telvorwahl(String c_telvorwahl) {
		this.c_telvorwahl = c_telvorwahl;
	}

	public String getWaehrung_c_nr() {
		return this.waehrung_c_nr;
	}

	public void setWaehrung_c_nr(String waehrung_c_nr) {
		this.waehrung_c_nr = waehrung_c_nr;
	}

	public String toString() {
		return new ToStringBuilder(this).append("i_id", getI_id()).toString();
	}

}
