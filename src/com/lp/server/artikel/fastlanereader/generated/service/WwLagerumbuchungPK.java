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
package com.lp.server.artikel.fastlanereader.generated.service;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class WwLagerumbuchungPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Integer i_lagerbewegungidzubuchung;

	/** identifier field */
	private Integer i_lagerbewegungidabbuchung;

	/** full constructor */
	public WwLagerumbuchungPK(Integer i_lagerbewegungidzubuchung,
			Integer i_lagerbewegungidabbuchung) {
		this.i_lagerbewegungidzubuchung = i_lagerbewegungidzubuchung;
		this.i_lagerbewegungidabbuchung = i_lagerbewegungidabbuchung;
	}

	/** default constructor */
	public WwLagerumbuchungPK() {
	}

	public Integer getI_lagerbewegungidzubuchung() {
		return this.i_lagerbewegungidzubuchung;
	}

	public void setI_lagerbewegungidzubuchung(Integer i_lagerbewegungidzubuchung) {
		this.i_lagerbewegungidzubuchung = i_lagerbewegungidzubuchung;
	}

	public Integer getI_lagerbewegungidabbuchung() {
		return this.i_lagerbewegungidabbuchung;
	}

	public void setI_lagerbewegungidabbuchung(Integer i_lagerbewegungidabbuchung) {
		this.i_lagerbewegungidabbuchung = i_lagerbewegungidabbuchung;
	}

	public String toString() {
		return new ToStringBuilder(this).append("i_lagerbewegungidzubuchung",
				getI_lagerbewegungidzubuchung()).append(
				"i_lagerbewegungidabbuchung", getI_lagerbewegungidabbuchung())
				.toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof WwLagerumbuchungPK))
			return false;
		WwLagerumbuchungPK castOther = (WwLagerumbuchungPK) other;
		return new EqualsBuilder().append(this.getI_lagerbewegungidzubuchung(),
				castOther.getI_lagerbewegungidzubuchung()).append(
				this.getI_lagerbewegungidabbuchung(),
				castOther.getI_lagerbewegungidabbuchung()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getI_lagerbewegungidzubuchung())
				.append(getI_lagerbewegungidabbuchung()).toHashCode();
	}

}
