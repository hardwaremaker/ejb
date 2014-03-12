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
package com.lp.server.system.fastlanereader.generated.service;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class FLRWechselkursPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private String waehrung_c_nr_von;

	/** identifier field */
	private String waehrung_c_nr_zu;

	/** identifier field */
	private Date t_datum;

	/** full constructor */
	public FLRWechselkursPK(String waehrung_c_nr_von, String waehrung_c_nr_zu,
			Date t_datum) {
		this.waehrung_c_nr_von = waehrung_c_nr_von;
		this.waehrung_c_nr_zu = waehrung_c_nr_zu;
		this.t_datum = t_datum;
	}

	/** default constructor */
	public FLRWechselkursPK() {
	}

	public String getWaehrung_c_nr_von() {
		return this.waehrung_c_nr_von;
	}

	public void setWaehrung_c_nr_von(String waehrung_c_nr_von) {
		this.waehrung_c_nr_von = waehrung_c_nr_von;
	}

	public String getWaehrung_c_nr_zu() {
		return this.waehrung_c_nr_zu;
	}

	public void setWaehrung_c_nr_zu(String waehrung_c_nr_zu) {
		this.waehrung_c_nr_zu = waehrung_c_nr_zu;
	}

	public Date getT_datum() {
		return this.t_datum;
	}

	public void setT_datum(Date t_datum) {
		this.t_datum = t_datum;
	}

	public String toString() {
		return new ToStringBuilder(this).append("waehrung_c_nr_von",
				getWaehrung_c_nr_von()).append("waehrung_c_nr_zu",
				getWaehrung_c_nr_zu()).append("t_datum", getT_datum())
				.toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof FLRWechselkursPK))
			return false;
		FLRWechselkursPK castOther = (FLRWechselkursPK) other;
		return new EqualsBuilder().append(this.getWaehrung_c_nr_von(),
				castOther.getWaehrung_c_nr_von()).append(
				this.getWaehrung_c_nr_zu(), castOther.getWaehrung_c_nr_zu())
				.append(this.getT_datum(), castOther.getT_datum()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getWaehrung_c_nr_von()).append(
				getWaehrung_c_nr_zu()).append(getT_datum()).toHashCode();
	}

}
