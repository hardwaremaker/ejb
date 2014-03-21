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
package com.lp.server.stueckliste.fastlanereader.generated;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;

/** @author Hibernate CodeGenerator */
public class FLRStuecklistepositionFuerDruck implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Integer i_id;

	/** nullable persistent field */
	private Integer i_sort;

	/** nullable persistent field */
	private BigDecimal n_menge;

	/** nullable persistent field */
	private String einheit_c_nr;

	/** nullable persistent field */
	private Integer stueckliste_i_id;

	/** nullable persistent field */
	private Short b_mitdrucken;

	/** nullable persistent field */
	private Integer artikel_i_id;

	/** nullable persistent field */
	private FLRArtikel flrartikel;

	/** full constructor */
	public FLRStuecklistepositionFuerDruck(Integer i_sort, BigDecimal n_menge,
			String einheit_c_nr, Integer stueckliste_i_id, Short b_mitdrucken,
			Integer artikel_i_id, FLRArtikel flrartikel) {
		this.i_sort = i_sort;
		this.n_menge = n_menge;
		this.einheit_c_nr = einheit_c_nr;
		this.stueckliste_i_id = stueckliste_i_id;
		this.b_mitdrucken = b_mitdrucken;
		this.artikel_i_id = artikel_i_id;
		this.flrartikel = flrartikel;
	}

	/** default constructor */
	public FLRStuecklistepositionFuerDruck() {
	}

	public Integer getI_id() {
		return this.i_id;
	}

	public void setI_id(Integer i_id) {
		this.i_id = i_id;
	}

	public Integer getI_sort() {
		return this.i_sort;
	}

	public void setI_sort(Integer i_sort) {
		this.i_sort = i_sort;
	}

	public BigDecimal getN_menge() {
		return this.n_menge;
	}

	public void setN_menge(BigDecimal n_menge) {
		this.n_menge = n_menge;
	}

	public String getEinheit_c_nr() {
		return this.einheit_c_nr;
	}

	public void setEinheit_c_nr(String einheit_c_nr) {
		this.einheit_c_nr = einheit_c_nr;
	}

	public Integer getStueckliste_i_id() {
		return this.stueckliste_i_id;
	}

	public void setStueckliste_i_id(Integer stueckliste_i_id) {
		this.stueckliste_i_id = stueckliste_i_id;
	}

	public Short getB_mitdrucken() {
		return this.b_mitdrucken;
	}

	public void setB_mitdrucken(Short b_mitdrucken) {
		this.b_mitdrucken = b_mitdrucken;
	}

	public Integer getArtikel_i_id() {
		return this.artikel_i_id;
	}

	public void setArtikel_i_id(Integer artikel_i_id) {
		this.artikel_i_id = artikel_i_id;
	}

	public FLRArtikel getFlrartikel() {
		return this.flrartikel;
	}

	public void setFlrartikel(FLRArtikel flrartikel) {
		this.flrartikel = flrartikel;
	}

	public String toString() {
		return new ToStringBuilder(this).append("i_id", getI_id()).toString();
	}

}
