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
package com.lp.server.artikel.fastlanereader.generated;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class FLRInventurprotokoll implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Integer i_id;

	/** nullable persistent field */
	private BigDecimal n_korrekturmenge;

	/** nullable persistent field */
	private Date t_zeitpunkt;

	/** nullable persistent field */
	private BigDecimal n_inventurpreis;

	/** nullable persistent field */
	private com.lp.server.artikel.fastlanereader.generated.FLRInventurliste flrinventurliste;

	/** nullable persistent field */
	private com.lp.server.artikel.fastlanereader.generated.FLRInventur flrinventur;

	/** full constructor */
	public FLRInventurprotokoll(
			BigDecimal n_korrekturmenge,
			Date t_zeitpunkt,
			BigDecimal n_inventurpreis,
			com.lp.server.artikel.fastlanereader.generated.FLRInventurliste flrinventurliste,
			com.lp.server.artikel.fastlanereader.generated.FLRInventur flrinventur) {
		this.n_korrekturmenge = n_korrekturmenge;
		this.t_zeitpunkt = t_zeitpunkt;
		this.n_inventurpreis = n_inventurpreis;
		this.flrinventurliste = flrinventurliste;
		this.flrinventur = flrinventur;
	}

	/** default constructor */
	public FLRInventurprotokoll() {
	}

	public Integer getI_id() {
		return this.i_id;
	}

	public void setI_id(Integer i_id) {
		this.i_id = i_id;
	}

	public BigDecimal getN_korrekturmenge() {
		return this.n_korrekturmenge;
	}

	public void setN_korrekturmenge(BigDecimal n_korrekturmenge) {
		this.n_korrekturmenge = n_korrekturmenge;
	}

	public Date getT_zeitpunkt() {
		return this.t_zeitpunkt;
	}

	public void setT_zeitpunkt(Date t_zeitpunkt) {
		this.t_zeitpunkt = t_zeitpunkt;
	}

	public BigDecimal getN_inventurpreis() {
		return this.n_inventurpreis;
	}

	public void setN_inventurpreis(BigDecimal n_inventurpreis) {
		this.n_inventurpreis = n_inventurpreis;
	}

	public com.lp.server.artikel.fastlanereader.generated.FLRInventurliste getFlrinventurliste() {
		return this.flrinventurliste;
	}

	public void setFlrinventurliste(
			com.lp.server.artikel.fastlanereader.generated.FLRInventurliste flrinventurliste) {
		this.flrinventurliste = flrinventurliste;
	}

	public com.lp.server.artikel.fastlanereader.generated.FLRInventur getFlrinventur() {
		return this.flrinventur;
	}

	public void setFlrinventur(
			com.lp.server.artikel.fastlanereader.generated.FLRInventur flrinventur) {
		this.flrinventur = flrinventur;
	}

	public String toString() {
		return new ToStringBuilder(this).append("i_id", getI_id()).toString();
	}

}
