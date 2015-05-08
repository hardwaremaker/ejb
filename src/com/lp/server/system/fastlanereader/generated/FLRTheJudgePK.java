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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class FLRTheJudgePK implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	/** identifier field */
	private String c_wer;

	/** identifier field */
	private String c_was;

	/** full constructor */
	public FLRTheJudgePK(String c_wer, String c_was) {
		this.c_wer = c_wer;
		this.c_was = c_was;
	}

	/** default constructor */
	public FLRTheJudgePK() {
	}

	public String getC_wer() {
		return this.c_wer;
	}

	public void setC_wer(String c_wer) {
		this.c_wer = c_wer;
	}

	public String getC_was() {
		return this.c_was;
	}

	public void setC_was(String c_was) {
		this.c_was = c_was;
	}

	public String toString() {
		return new ToStringBuilder(this).append("c_wer", getC_wer()).append(
				"c_was", getC_was()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof FLRTheJudgePK))
			return false;
		FLRTheJudgePK castOther = (FLRTheJudgePK) other;
		return new EqualsBuilder()
				.append(this.getC_wer(), castOther.getC_wer()).append(
						this.getC_was(), castOther.getC_was()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getC_wer()).append(getC_was())
				.toHashCode();
	}

}
