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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class FLRFunktionspr implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** nullable persistent field */
	private String c_bezeichnung;

	/** identifier field */
	private com.lp.server.system.fastlanereader.generated.FLRFunktion funktion;

	/** identifier field */
	private com.lp.server.system.fastlanereader.generated.FLRLocale locale;

	/** full constructor */
	public FLRFunktionspr(String c_bezeichnung,
			com.lp.server.system.fastlanereader.generated.FLRFunktion funktion,
			com.lp.server.system.fastlanereader.generated.FLRLocale locale) {
		this.c_bezeichnung = c_bezeichnung;
		this.funktion = funktion;
		this.locale = locale;
	}

	/** default constructor */
	public FLRFunktionspr() {
	}

	/** minimal constructor */
	public FLRFunktionspr(
			com.lp.server.system.fastlanereader.generated.FLRFunktion funktion,
			com.lp.server.system.fastlanereader.generated.FLRLocale locale) {
		this.funktion = funktion;
		this.locale = locale;
	}

	public String getC_bezeichnung() {
		return this.c_bezeichnung;
	}

	public void setC_bezeichnung(String c_bezeichnung) {
		this.c_bezeichnung = c_bezeichnung;
	}

	public com.lp.server.system.fastlanereader.generated.FLRFunktion getFunktion() {
		return this.funktion;
	}

	public void setFunktion(
			com.lp.server.system.fastlanereader.generated.FLRFunktion funktion) {
		this.funktion = funktion;
	}

	public com.lp.server.system.fastlanereader.generated.FLRLocale getLocale() {
		return this.locale;
	}

	public void setLocale(
			com.lp.server.system.fastlanereader.generated.FLRLocale locale) {
		this.locale = locale;
	}

	public String toString() {
		return new ToStringBuilder(this).append("funktion", getFunktion())
				.append("locale", getLocale()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof FLRFunktionspr))
			return false;
		FLRFunktionspr castOther = (FLRFunktionspr) other;
		return new EqualsBuilder().append(this.getFunktion(),
				castOther.getFunktion()).append(this.getLocale(),
				castOther.getLocale()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getFunktion()).append(getLocale())
				.toHashCode();
	}

}
