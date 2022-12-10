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
package com.lp.server.auftrag.fastlanereader.generated;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.lp.server.system.fastlanereader.generated.FLRPositionsartspr;

/** @author Hibernate CodeGenerator */
public class FLRAuftragpositionart implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private String positionsart_c_nr;

	/** nullable persistent field */
	private Integer i_sort;

	/** persistent field */
	private Set<FLRPositionsartspr> auftragpositionsart_positionsart_set;

	/** full constructor */
	public FLRAuftragpositionart(String positionsart_c_nr, Integer i_sort,
			Set<FLRPositionsartspr> auftragpositionsart_positionsart_set) {
		this.positionsart_c_nr = positionsart_c_nr;
		this.i_sort = i_sort;
		this.auftragpositionsart_positionsart_set = auftragpositionsart_positionsart_set;
	}

	/** default constructor */
	public FLRAuftragpositionart() {
	}

	/** minimal constructor */
	public FLRAuftragpositionart(String positionsart_c_nr,
			Set<FLRPositionsartspr> auftragpositionsart_positionsart_set) {
		this.positionsart_c_nr = positionsart_c_nr;
		this.auftragpositionsart_positionsart_set = auftragpositionsart_positionsart_set;
	}

	public String getPositionsart_c_nr() {
		return this.positionsart_c_nr;
	}

	public void setPositionsart_c_nr(String positionsart_c_nr) {
		this.positionsart_c_nr = positionsart_c_nr;
	}

	public Integer getI_sort() {
		return this.i_sort;
	}

	public void setI_sort(Integer i_sort) {
		this.i_sort = i_sort;
	}

	public Set<FLRPositionsartspr> getAuftragpositionsart_positionsart_set() {
		return this.auftragpositionsart_positionsart_set;
	}

	public void setAuftragpositionsart_positionsart_set(
			Set<FLRPositionsartspr> auftragpositionsart_positionsart_set) {
		this.auftragpositionsart_positionsart_set = auftragpositionsart_positionsart_set;
	}

	public String toString() {
		return new ToStringBuilder(this).append("positionsart_c_nr",
				getPositionsart_c_nr()).toString();
	}

}
