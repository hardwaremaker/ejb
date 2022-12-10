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
package com.lp.server.lieferschein.fastlanereader.generated;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class FLRLieferscheinart implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private String c_nr;

	/** nullable persistent field */
	private Integer i_sort;

	/** persistent field */
	private Set<FLRLieferscheinartspr> lieferscheinart_lieferscheinart_set;

	/** full constructor */
	public FLRLieferscheinart(Integer i_sort,
			Set<FLRLieferscheinartspr> lieferscheinart_lieferscheinart_set) {
		this.i_sort = i_sort;
		this.lieferscheinart_lieferscheinart_set = lieferscheinart_lieferscheinart_set;
	}

	/** default constructor */
	public FLRLieferscheinart() {
	}

	/** minimal constructor */
	public FLRLieferscheinart(Set<FLRLieferscheinartspr> lieferscheinart_lieferscheinart_set) {
		this.lieferscheinart_lieferscheinart_set = lieferscheinart_lieferscheinart_set;
	}

	public String getC_nr() {
		return this.c_nr;
	}

	public void setC_nr(String c_nr) {
		this.c_nr = c_nr;
	}

	public Integer getI_sort() {
		return this.i_sort;
	}

	public void setI_sort(Integer i_sort) {
		this.i_sort = i_sort;
	}

	public Set<FLRLieferscheinartspr> getLieferscheinart_lieferscheinart_set() {
		return this.lieferscheinart_lieferscheinart_set;
	}

	public void setLieferscheinart_lieferscheinart_set(
			Set<FLRLieferscheinartspr> lieferscheinart_lieferscheinart_set) {
		this.lieferscheinart_lieferscheinart_set = lieferscheinart_lieferscheinart_set;
	}

	public String toString() {
		return new ToStringBuilder(this).append("c_nr", getC_nr()).toString();
	}

}
