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
package com.lp.server.artikel.fastlanereader.generated.service;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class WwLagerabgangursprungPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Integer i_lagerbewegungid;

	/** identifier field */
	private Integer i_lagerbewegungidursprung;

	/** full constructor */
	public WwLagerabgangursprungPK(Integer i_lagerbewegungid,
			Integer i_lagerbewegungidursprung) {
		this.i_lagerbewegungid = i_lagerbewegungid;
		this.i_lagerbewegungidursprung = i_lagerbewegungidursprung;
	}

	/** default constructor */
	public WwLagerabgangursprungPK() {
	}

	public Integer getI_lagerbewegungid() {
		return this.i_lagerbewegungid;
	}

	public void setI_lagerbewegungid(Integer i_lagerbewegungid) {
		this.i_lagerbewegungid = i_lagerbewegungid;
	}

	public Integer getI_lagerbewegungidursprung() {
		return this.i_lagerbewegungidursprung;
	}

	public void setI_lagerbewegungidursprung(Integer i_lagerbewegungidursprung) {
		this.i_lagerbewegungidursprung = i_lagerbewegungidursprung;
	}

	public String toString() {
		return new ToStringBuilder(this).append("i_lagerbewegungid",
				getI_lagerbewegungid()).append("i_lagerbewegungidursprung",
				getI_lagerbewegungidursprung()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof WwLagerabgangursprungPK))
			return false;
		WwLagerabgangursprungPK castOther = (WwLagerabgangursprungPK) other;
		return new EqualsBuilder().append(this.getI_lagerbewegungid(),
				castOther.getI_lagerbewegungid()).append(
				this.getI_lagerbewegungidursprung(),
				castOther.getI_lagerbewegungidursprung()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getI_lagerbewegungid()).append(
				getI_lagerbewegungidursprung()).toHashCode();
	}

}
