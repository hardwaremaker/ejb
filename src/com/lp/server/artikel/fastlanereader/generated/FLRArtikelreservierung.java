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
import java.sql.Timestamp;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class FLRArtikelreservierung implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Integer i_id;

	/** nullable persistent field */
	private Integer i_belegartpositionid;

	/** nullable persistent field */
	private BigDecimal n_menge;

	/** nullable persistent field */
	private Timestamp t_liefertermin;

	/** nullable persistent field */
	private String c_belegartnr;

	/** nullable persistent field */
	private com.lp.server.artikel.fastlanereader.generated.FLRArtikel flrartikel;

	/** full constructor */
	public FLRArtikelreservierung(Integer i_belegartpositionid,
			BigDecimal n_menge, Timestamp t_liefertermin, String c_belegartnr,
			com.lp.server.artikel.fastlanereader.generated.FLRArtikel flrartikel) {
		this.i_belegartpositionid = i_belegartpositionid;
		this.n_menge = n_menge;
		this.t_liefertermin = t_liefertermin;
		this.c_belegartnr = c_belegartnr;
		this.flrartikel = flrartikel;
	}

	/** default constructor */
	public FLRArtikelreservierung() {
	}

	public Integer getI_id() {
		return this.i_id;
	}

	public void setI_id(Integer i_id) {
		this.i_id = i_id;
	}

	public Integer getI_belegartpositionid() {
		return this.i_belegartpositionid;
	}

	public void setI_belegartpositionid(Integer i_belegartpositionid) {
		this.i_belegartpositionid = i_belegartpositionid;
	}

	public BigDecimal getN_menge() {
		return this.n_menge;
	}

	public void setN_menge(BigDecimal n_menge) {
		this.n_menge = n_menge;
	}

	public Timestamp getT_liefertermin() {
		return this.t_liefertermin;
	}

	public void setT_liefertermin(Timestamp t_liefertermin) {
		this.t_liefertermin = t_liefertermin;
	}

	public String getC_belegartnr() {
		return this.c_belegartnr;
	}

	public void setC_belegartnr(String c_belegartnr) {
		this.c_belegartnr = c_belegartnr;
	}

	public com.lp.server.artikel.fastlanereader.generated.FLRArtikel getFlrartikel() {
		return this.flrartikel;
	}

	public void setFlrartikel(
			com.lp.server.artikel.fastlanereader.generated.FLRArtikel flrartikel) {
		this.flrartikel = flrartikel;
	}

	public String toString() {
		return new ToStringBuilder(this).append("i_id", getI_id()).toString();
	}

}
