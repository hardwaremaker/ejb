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
package com.lp.server.anfrage.fastlanereader.generated;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.system.fastlanereader.generated.FLRMediastandard;

/** @author Hibernate CodeGenerator */
public class FLRAnfrageposition implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Integer i_id;

	/** nullable persistent field */
	private Integer i_sort;

	/** nullable persistent field */
	private String anfragepositionart_c_nr;

	/** nullable persistent field */
	private BigDecimal n_menge;

	/** nullable persistent field */
	private String einheit_c_nr;

	/** nullable persistent field */
	private String c_bez;

	/** nullable persistent field */
	private BigDecimal n_richtpreis;

	/** nullable persistent field */
	private String x_textinhalt;

	/** nullable persistent field */
	private com.lp.server.anfrage.fastlanereader.generated.FLRAnfrage flranfrage;

	/** nullable persistent field */
	private FLRArtikel flrartikel;

	/** nullable persistent field */
	private FLRMediastandard flrmediastandard;

	/** full constructor */
	public FLRAnfrageposition(
			Integer i_sort,
			String anfragepositionart_c_nr,
			BigDecimal n_menge,
			String einheit_c_nr,
			String c_bez,
			BigDecimal n_richtpreis,
			String x_textinhalt,
			com.lp.server.anfrage.fastlanereader.generated.FLRAnfrage flranfrage,
			FLRArtikel flrartikel, FLRMediastandard flrmediastandard) {
		this.i_sort = i_sort;
		this.anfragepositionart_c_nr = anfragepositionart_c_nr;
		this.n_menge = n_menge;
		this.einheit_c_nr = einheit_c_nr;
		this.c_bez = c_bez;
		this.n_richtpreis = n_richtpreis;
		this.x_textinhalt = x_textinhalt;
		this.flranfrage = flranfrage;
		this.flrartikel = flrartikel;
		this.flrmediastandard = flrmediastandard;
	}

	/** default constructor */
	public FLRAnfrageposition() {
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

	public String getAnfragepositionart_c_nr() {
		return this.anfragepositionart_c_nr;
	}

	public void setAnfragepositionart_c_nr(String anfragepositionart_c_nr) {
		this.anfragepositionart_c_nr = anfragepositionart_c_nr;
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

	public String getC_bez() {
		return this.c_bez;
	}

	public void setC_bez(String c_bez) {
		this.c_bez = c_bez;
	}

	public BigDecimal getN_richtpreis() {
		return this.n_richtpreis;
	}

	public void setN_richtpreis(BigDecimal n_richtpreis) {
		this.n_richtpreis = n_richtpreis;
	}

	public String getX_textinhalt() {
		return this.x_textinhalt;
	}

	public void setX_textinhalt(String x_textinhalt) {
		this.x_textinhalt = x_textinhalt;
	}

	public com.lp.server.anfrage.fastlanereader.generated.FLRAnfrage getFlranfrage() {
		return this.flranfrage;
	}

	public void setFlranfrage(
			com.lp.server.anfrage.fastlanereader.generated.FLRAnfrage flranfrage) {
		this.flranfrage = flranfrage;
	}

	public FLRArtikel getFlrartikel() {
		return this.flrartikel;
	}

	public void setFlrartikel(FLRArtikel flrartikel) {
		this.flrartikel = flrartikel;
	}

	public FLRMediastandard getFlrmediastandard() {
		return this.flrmediastandard;
	}

	public void setFlrmediastandard(FLRMediastandard flrmediastandard) {
		this.flrmediastandard = flrmediastandard;
	}

	public String toString() {
		return new ToStringBuilder(this).append("i_id", getI_id()).toString();
	}

}
