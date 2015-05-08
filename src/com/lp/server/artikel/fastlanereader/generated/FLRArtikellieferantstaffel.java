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
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class FLRArtikellieferantstaffel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Integer i_id;

	/** nullable persistent field */
	private Integer artikellieferant_i_id;

	/** nullable persistent field */
	private BigDecimal n_menge;

	/** nullable persistent field */
	private Double f_rabatt;

	/** nullable persistent field */
	private BigDecimal n_nettopreis;

	/** nullable persistent field */
	private Date t_preisgueltigab;

	/** nullable persistent field */
	private Date t_preisgueltigbis;

	/** nullable persistent field */
	private com.lp.server.artikel.fastlanereader.generated.FLRArtikellieferant flrartikellieferant;

	/** full constructor */
	public FLRArtikellieferantstaffel(
			Integer artikellieferant_i_id,
			BigDecimal n_menge,
			Double f_rabatt,
			BigDecimal n_nettopreis,
			Date t_preisgueltigab,
			Date t_preisgueltigbis,
			com.lp.server.artikel.fastlanereader.generated.FLRArtikellieferant flrartikellieferant) {
		this.artikellieferant_i_id = artikellieferant_i_id;
		this.n_menge = n_menge;
		this.f_rabatt = f_rabatt;
		this.n_nettopreis = n_nettopreis;
		this.t_preisgueltigab = t_preisgueltigab;
		this.t_preisgueltigbis = t_preisgueltigbis;
		this.flrartikellieferant = flrartikellieferant;
	}

	/** default constructor */
	public FLRArtikellieferantstaffel() {
	}

	public Integer getI_id() {
		return this.i_id;
	}

	public void setI_id(Integer i_id) {
		this.i_id = i_id;
	}

	public Integer getArtikellieferant_i_id() {
		return this.artikellieferant_i_id;
	}

	public void setArtikellieferant_i_id(Integer artikellieferant_i_id) {
		this.artikellieferant_i_id = artikellieferant_i_id;
	}

	public BigDecimal getN_menge() {
		return this.n_menge;
	}

	public void setN_menge(BigDecimal n_menge) {
		this.n_menge = n_menge;
	}

	public Double getF_rabatt() {
		return this.f_rabatt;
	}

	public void setF_rabatt(Double f_rabatt) {
		this.f_rabatt = f_rabatt;
	}

	public BigDecimal getN_nettopreis() {
		return this.n_nettopreis;
	}

	public void setN_nettopreis(BigDecimal n_nettopreis) {
		this.n_nettopreis = n_nettopreis;
	}

	public Date getT_preisgueltigab() {
		return this.t_preisgueltigab;
	}

	public void setT_preisgueltigab(Date t_preisgueltigab) {
		this.t_preisgueltigab = t_preisgueltigab;
	}

	public Date getT_preisgueltigbis() {
		return this.t_preisgueltigbis;
	}

	public void setT_preisgueltigbis(Date t_preisgueltigbis) {
		this.t_preisgueltigbis = t_preisgueltigbis;
	}

	public com.lp.server.artikel.fastlanereader.generated.FLRArtikellieferant getFlrartikellieferant() {
		return this.flrartikellieferant;
	}

	public void setFlrartikellieferant(
			com.lp.server.artikel.fastlanereader.generated.FLRArtikellieferant flrartikellieferant) {
		this.flrartikellieferant = flrartikellieferant;
	}

	public String toString() {
		return new ToStringBuilder(this).append("i_id", getI_id()).toString();
	}

}
