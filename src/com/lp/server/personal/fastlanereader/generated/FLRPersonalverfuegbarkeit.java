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
package com.lp.server.personal.fastlanereader.generated;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;

/** @author Hibernate CodeGenerator */
public class FLRPersonalverfuegbarkeit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	/** identifier field */
	private Integer i_id;

	/** nullable persistent field */
	private Integer personal_i_id;

	/** nullable persistent field */
	private Integer artikel_i_id;

	/** nullable persistent field */
	private Double f_anteilprozent;

	/** nullable persistent field */
	private FLRArtikel flrartikel;

	/** nullable persistent field */
	private com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal;

	/** full constructor */
	public FLRPersonalverfuegbarkeit(
			Integer personal_i_id,
			Integer artikel_i_id,
			Double f_anteilprozent,
			FLRArtikel flrartikel,
			com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal) {
		this.personal_i_id = personal_i_id;
		this.artikel_i_id = artikel_i_id;
		this.f_anteilprozent = f_anteilprozent;
		this.flrartikel = flrartikel;
		this.flrpersonal = flrpersonal;
	}

	/** default constructor */
	public FLRPersonalverfuegbarkeit() {
	}

	public Integer getI_id() {
		return this.i_id;
	}

	public void setI_id(Integer i_id) {
		this.i_id = i_id;
	}

	public Integer getPersonal_i_id() {
		return this.personal_i_id;
	}

	public void setPersonal_i_id(Integer personal_i_id) {
		this.personal_i_id = personal_i_id;
	}

	public Integer getArtikel_i_id() {
		return this.artikel_i_id;
	}

	public void setArtikel_i_id(Integer artikel_i_id) {
		this.artikel_i_id = artikel_i_id;
	}

	public Double getF_anteilprozent() {
		return this.f_anteilprozent;
	}

	public void setF_anteilprozent(Double f_anteilprozent) {
		this.f_anteilprozent = f_anteilprozent;
	}

	public FLRArtikel getFlrartikel() {
		return this.flrartikel;
	}

	public void setFlrartikel(FLRArtikel flrartikel) {
		this.flrartikel = flrartikel;
	}

	public com.lp.server.personal.fastlanereader.generated.FLRPersonal getFlrpersonal() {
		return this.flrpersonal;
	}

	public void setFlrpersonal(
			com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal) {
		this.flrpersonal = flrpersonal;
	}

	public String toString() {
		return new ToStringBuilder(this).append("i_id", getI_id()).toString();
	}

}
