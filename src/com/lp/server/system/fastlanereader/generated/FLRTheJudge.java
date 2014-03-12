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
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class FLRTheJudge implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	/** identifier field */
	private com.lp.server.system.fastlanereader.generated.FLRTheJudgePK id_comp;

	/** persistent field */
	private String c_usernr;

	/** persistent field */
	private Date t_wann;

	/** persistent field */
	private Integer personal_i_id_locker;

	/** full constructor */
	public FLRTheJudge(
			com.lp.server.system.fastlanereader.generated.FLRTheJudgePK id_comp,
			String c_usernr, Date t_wann, Integer personal_i_id_locker) {
		this.id_comp = id_comp;
		this.c_usernr = c_usernr;
		this.t_wann = t_wann;
		this.personal_i_id_locker = personal_i_id_locker;
	}

	/** default constructor */
	public FLRTheJudge() {
	}

	public com.lp.server.system.fastlanereader.generated.FLRTheJudgePK getId_comp() {
		return this.id_comp;
	}

	public void setId_comp(
			com.lp.server.system.fastlanereader.generated.FLRTheJudgePK id_comp) {
		this.id_comp = id_comp;
	}

	public String getC_usernr() {
		return this.c_usernr;
	}

	public void setC_usernr(String c_usernr) {
		this.c_usernr = c_usernr;
	}

	public Date getT_wann() {
		return this.t_wann;
	}

	public void setT_wann(Date t_wann) {
		this.t_wann = t_wann;
	}

	public Integer getPersonal_i_id_locker() {
		return this.personal_i_id_locker;
	}

	public void setPersonal_i_id_locker(Integer personal_i_id_locker) {
		this.personal_i_id_locker = personal_i_id_locker;
	}

	public String toString() {
		return new ToStringBuilder(this).append("id_comp", getId_comp())
				.toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof FLRTheJudge))
			return false;
		FLRTheJudge castOther = (FLRTheJudge) other;
		return new EqualsBuilder().append(this.getId_comp(),
				castOther.getId_comp()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getId_comp()).toHashCode();
	}

}
