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
public class FLRPositionsartspr implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	/** nullable persistent field */
	private String c_bez;

	/** identifier field */
	private com.lp.server.system.fastlanereader.generated.FLRPositionsart positionsart_c_nr;

	/** identifier field */
	private com.lp.server.system.fastlanereader.generated.FLRLocale locale_c_nr;

	/** full constructor */
	public FLRPositionsartspr(
			String c_bez,
			com.lp.server.system.fastlanereader.generated.FLRPositionsart positionsart_c_nr,
			com.lp.server.system.fastlanereader.generated.FLRLocale locale_c_nr) {
		this.c_bez = c_bez;
		this.positionsart_c_nr = positionsart_c_nr;
		this.locale_c_nr = locale_c_nr;
	}

	/** default constructor */
	public FLRPositionsartspr() {
	}

	/** minimal constructor */
	public FLRPositionsartspr(
			com.lp.server.system.fastlanereader.generated.FLRPositionsart positionsart_c_nr,
			com.lp.server.system.fastlanereader.generated.FLRLocale locale_c_nr) {
		this.positionsart_c_nr = positionsart_c_nr;
		this.locale_c_nr = locale_c_nr;
	}

	public String getC_bez() {
		return this.c_bez;
	}

	public void setC_bez(String c_bez) {
		this.c_bez = c_bez;
	}

	public com.lp.server.system.fastlanereader.generated.FLRPositionsart getPositionsart_c_nr() {
		return this.positionsart_c_nr;
	}

	public void setPositionsart_c_nr(
			com.lp.server.system.fastlanereader.generated.FLRPositionsart positionsart_c_nr) {
		this.positionsart_c_nr = positionsart_c_nr;
	}

	public com.lp.server.system.fastlanereader.generated.FLRLocale getLocale_c_nr() {
		return this.locale_c_nr;
	}

	public void setLocale_c_nr(
			com.lp.server.system.fastlanereader.generated.FLRLocale locale_c_nr) {
		this.locale_c_nr = locale_c_nr;
	}

	public String toString() {
		return new ToStringBuilder(this).append("positionsart_c_nr",
				getPositionsart_c_nr()).append("locale_c_nr", getLocale_c_nr())
				.toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof FLRPositionsartspr))
			return false;
		FLRPositionsartspr castOther = (FLRPositionsartspr) other;
		return new EqualsBuilder().append(this.getPositionsart_c_nr(),
				castOther.getPositionsart_c_nr()).append(this.getLocale_c_nr(),
				castOther.getLocale_c_nr()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getPositionsart_c_nr()).append(
				getLocale_c_nr()).toHashCode();
	}

}
