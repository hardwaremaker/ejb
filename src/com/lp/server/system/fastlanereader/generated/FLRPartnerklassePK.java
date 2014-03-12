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
public class FLRPartnerklassePK implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** identifier field */
	private Integer partnerklasse_i_id;

	/** identifier field */
	private String sprache_c_nr;

	/** full constructor */
	public FLRPartnerklassePK(Integer partnerklasse_i_id, String sprache_c_nr) {
		this.partnerklasse_i_id = partnerklasse_i_id;
		this.sprache_c_nr = sprache_c_nr;
	}

	/** default constructor */
	public FLRPartnerklassePK() {
	}

	public Integer getPartnerklasse_i_id() {
		return this.partnerklasse_i_id;
	}

	public void setPartnerklasse_i_id(Integer partnerklasse_i_id) {
		this.partnerklasse_i_id = partnerklasse_i_id;
	}

	public String getSprache_c_nr() {
		return this.sprache_c_nr;
	}

	public void setSprache_c_nr(String sprache_c_nr) {
		this.sprache_c_nr = sprache_c_nr;
	}

	public String toString() {
		return new ToStringBuilder(this).append("partnerklasse_i_id",
				getPartnerklasse_i_id()).append("sprache_c_nr",
				getSprache_c_nr()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof FLRPartnerklassePK))
			return false;
		FLRPartnerklassePK castOther = (FLRPartnerklassePK) other;
		return new EqualsBuilder().append(this.getPartnerklasse_i_id(),
				castOther.getPartnerklasse_i_id()).append(
				this.getSprache_c_nr(), castOther.getSprache_c_nr()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getPartnerklasse_i_id()).append(
				getSprache_c_nr()).toHashCode();
	}

}
