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
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class FLRBelegart implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private String c_nr;

	/** persistent field */
	private Integer i_sort;

	/** persistent field */
	private String c_kbez;

	/** persistent field */
	private Integer i_standarderledigungszeitintagen;

	/** persistent field */
	private Set<?> belegart_belegart_set;

	/** full constructor */
	public FLRBelegart(Integer i_sort, String c_kbez,
			Integer i_standarderledigungszeitintagen, Set<?> belegart_belegart_set) {
		this.i_sort = i_sort;
		this.c_kbez = c_kbez;
		this.i_standarderledigungszeitintagen = i_standarderledigungszeitintagen;
		this.belegart_belegart_set = belegart_belegart_set;
	}

	/** default constructor */
	public FLRBelegart() {
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

	public String getC_kbez() {
		return this.c_kbez;
	}

	public void setC_kbez(String c_kbez) {
		this.c_kbez = c_kbez;
	}

	public Integer getI_standarderledigungszeitintagen() {
		return this.i_standarderledigungszeitintagen;
	}

	public void setI_standarderledigungszeitintagen(
			Integer i_standarderledigungszeitintagen) {
		this.i_standarderledigungszeitintagen = i_standarderledigungszeitintagen;
	}

	public Set<?> getBelegart_belegart_set() {
		return this.belegart_belegart_set;
	}

	public void setBelegart_belegart_set(Set<?> belegart_belegart_set) {
		this.belegart_belegart_set = belegart_belegart_set;
	}

	public String toString() {
		return new ToStringBuilder(this).append("c_nr", getC_nr()).toString();
	}

}
