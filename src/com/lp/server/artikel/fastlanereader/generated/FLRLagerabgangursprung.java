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
package com.lp.server.artikel.fastlanereader.generated;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.lp.server.artikel.fastlanereader.generated.service.WwLagerabgangursprungPK;

/** @author Hibernate CodeGenerator */
public class FLRLagerabgangursprung implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private WwLagerabgangursprungPK compId;

	/** persistent field */
	private BigDecimal n_verbrauchtemenge;

	/** persistent field */
	private BigDecimal n_gestehungspreis;

	/** full constructor */
	public FLRLagerabgangursprung(WwLagerabgangursprungPK compId,
			BigDecimal n_verbrauchtemenge, BigDecimal n_gestehungspreis) {
		this.compId = compId;
		this.n_verbrauchtemenge = n_verbrauchtemenge;
		this.n_gestehungspreis = n_gestehungspreis;
	}

	/** default constructor */
	public FLRLagerabgangursprung() {
	}

	public WwLagerabgangursprungPK getCompId() {
		return this.compId;
	}

	public void setCompId(WwLagerabgangursprungPK compId) {
		this.compId = compId;
	}

	public BigDecimal getN_verbrauchtemenge() {
		return this.n_verbrauchtemenge;
	}

	public void setN_verbrauchtemenge(BigDecimal n_verbrauchtemenge) {
		this.n_verbrauchtemenge = n_verbrauchtemenge;
	}

	public BigDecimal getN_gestehungspreis() {
		return this.n_gestehungspreis;
	}

	public void setN_gestehungspreis(BigDecimal n_gestehungspreis) {
		this.n_gestehungspreis = n_gestehungspreis;
	}

	public String toString() {
		return new ToStringBuilder(this).append("compId", getCompId())
				.toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof FLRLagerabgangursprung))
			return false;
		FLRLagerabgangursprung castOther = (FLRLagerabgangursprung) other;
		return new EqualsBuilder().append(this.getCompId(),
				castOther.getCompId()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getCompId()).toHashCode();
	}

}
