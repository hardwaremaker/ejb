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
package com.lp.server.finanz.fastlanereader.generated;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.lp.server.system.fastlanereader.generated.FLRLand;

/** @author Hibernate CodeGenerator */
public class FLRKontoLand implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
//	private KontolandPK id_comp;

	/** nullable persistent field */
	private FLRLand flrland;

	/** nullable persistent field */
	private com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto flrkonto;

	/** nullable persistent field */
	private com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto flrkonto_uebersetzt;

	private Timestamp gueltigAb;
	
	private Integer i_id;
	
	public FLRKontoLand(Integer i_id) {
		this.i_id = i_id;
	}
	
	/** full constructor */
	public FLRKontoLand(
			Integer i_id,
			FLRLand flrland,
			com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto flrkonto,
			com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto flrkonto_uebersetzt) {
		this.i_id = i_id;
		this.flrland = flrland;
		this.flrkonto = flrkonto;
		this.flrkonto_uebersetzt = flrkonto_uebersetzt;
	}

	/** default constructor */
	public FLRKontoLand() {
	}

	public FLRLand getFlrland() {
		return this.flrland;
	}

	public void setFlrland(FLRLand flrland) {
		this.flrland = flrland;
	}

	public com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto getFlrkonto() {
		return this.flrkonto;
	}

	public void setFlrkonto(
			com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto flrkonto) {
		this.flrkonto = flrkonto;
	}

	public com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto getFlrkonto_uebersetzt() {
		return this.flrkonto_uebersetzt;
	}

	public void setFlrkonto_uebersetzt(
			com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto flrkonto_uebersetzt) {
		this.flrkonto_uebersetzt = flrkonto_uebersetzt;
	}
	
	public Timestamp getGueltigAb() {
		return gueltigAb;
	}

	public void setGueltigAb(Timestamp gueltigAb) {
		this.gueltigAb = gueltigAb;
	}

	public Integer getI_id() {
		return i_id;
	}

	public void setI_id(Integer i_id) {
		this.i_id = i_id;
	}

	public String toString() {
		return new ToStringBuilder(this).append("i_id", getI_id())
				.toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof FLRKontoLand))
			return false;
		FLRKontoLand castOther = (FLRKontoLand) other;
		return new EqualsBuilder().append(this.getI_id(),
				castOther.getI_id()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getI_id()).toHashCode();
	}
}
