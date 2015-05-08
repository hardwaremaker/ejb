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

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class FLRLandkfzkennzeichen implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private String c_lkz;

	/** nullable persistent field */
	private String c_kfzkennzeichen;

	/** full constructor */
	public FLRLandkfzkennzeichen(String c_kfzkennzeichen) {
		this.c_kfzkennzeichen = c_kfzkennzeichen;
	}

	/** default constructor */
	public FLRLandkfzkennzeichen() {
	}

	public String getC_lkz() {
		return this.c_lkz;
	}

	public void setC_lkz(String c_lkz) {
		this.c_lkz = c_lkz;
	}

	public String getC_kfzkennzeichen() {
		return this.c_kfzkennzeichen;
	}

	public void setC_kfzkennzeichen(String c_kfzkennzeichen) {
		this.c_kfzkennzeichen = c_kfzkennzeichen;
	}

	public String toString() {
		return new ToStringBuilder(this).append("c_lkz", getC_lkz()).toString();
	}

}
