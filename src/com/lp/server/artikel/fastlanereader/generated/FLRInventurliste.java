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

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class FLRInventurliste implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Integer i_id;

	/** nullable persistent field */
	private BigDecimal n_inventurmenge;

	/** nullable persistent field */
	private String c_seriennrchargennr;

	/** nullable persistent field */
	private com.lp.server.artikel.fastlanereader.generated.FLRInventur flrinventur;

	/** nullable persistent field */
	private com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste flrartikel;

	/** nullable persistent field */
	private com.lp.server.artikel.fastlanereader.generated.FLRLager flrlager;

	/** full constructor */
	public FLRInventurliste(
			BigDecimal n_inventurmenge,
			String c_seriennrchargennr,
			com.lp.server.artikel.fastlanereader.generated.FLRInventur flrinventur,
			com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste flrartikel,
			com.lp.server.artikel.fastlanereader.generated.FLRLager flrlager) {
		this.n_inventurmenge = n_inventurmenge;
		this.c_seriennrchargennr = c_seriennrchargennr;
		this.flrinventur = flrinventur;
		this.flrartikel = flrartikel;
		this.flrlager = flrlager;
	}

	/** default constructor */
	public FLRInventurliste() {
	}

	public Integer getI_id() {
		return this.i_id;
	}

	public void setI_id(Integer i_id) {
		this.i_id = i_id;
	}

	public BigDecimal getN_inventurmenge() {
		return this.n_inventurmenge;
	}

	public void setN_inventurmenge(BigDecimal n_inventurmenge) {
		this.n_inventurmenge = n_inventurmenge;
	}

	public String getC_seriennrchargennr() {
		return this.c_seriennrchargennr;
	}

	public void setC_seriennrchargennr(String c_seriennrchargennr) {
		this.c_seriennrchargennr = c_seriennrchargennr;
	}

	public com.lp.server.artikel.fastlanereader.generated.FLRInventur getFlrinventur() {
		return this.flrinventur;
	}

	public void setFlrinventur(
			com.lp.server.artikel.fastlanereader.generated.FLRInventur flrinventur) {
		this.flrinventur = flrinventur;
	}

	public com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste getFlrartikel() {
		return this.flrartikel;
	}

	public void setFlrartikel(
			com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste flrartikel) {
		this.flrartikel = flrartikel;
	}

	public com.lp.server.artikel.fastlanereader.generated.FLRLager getFlrlager() {
		return this.flrlager;
	}

	public void setFlrlager(
			com.lp.server.artikel.fastlanereader.generated.FLRLager flrlager) {
		this.flrlager = flrlager;
	}

	public String toString() {
		return new ToStringBuilder(this).append("i_id", getI_id()).toString();
	}

}
