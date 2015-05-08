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
package com.lp.server.system.fastlanereader.generated;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class FLREinheitKonvertierung implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Integer i_id;

	/** nullable persistent field */
	private String einheit_cnr_von;

	/** nullable persistent field */
	private String einheit_cnr_zu;

	/** persistent field */
	private BigDecimal n_faktor;

	/** nullable persistent field */
	private com.lp.server.system.fastlanereader.generated.FLREinheit flreinheitvon;

	/** nullable persistent field */
	private com.lp.server.system.fastlanereader.generated.FLREinheit flreinheitzu;

	/** full constructor */
	public FLREinheitKonvertierung(
			String einheit_cnr_von,
			String einheit_cnr_zu,
			BigDecimal n_faktor,
			com.lp.server.system.fastlanereader.generated.FLREinheit flreinheitvon,
			com.lp.server.system.fastlanereader.generated.FLREinheit flreinheitzu) {
		this.einheit_cnr_von = einheit_cnr_von;
		this.einheit_cnr_zu = einheit_cnr_zu;
		this.n_faktor = n_faktor;
		this.flreinheitvon = flreinheitvon;
		this.flreinheitzu = flreinheitzu;
	}

	/** default constructor */
	public FLREinheitKonvertierung() {
	}

	/** minimal constructor */
	public FLREinheitKonvertierung(BigDecimal n_faktor) {
		this.n_faktor = n_faktor;
	}

	public Integer getI_id() {
		return this.i_id;
	}

	public void setI_id(Integer i_id) {
		this.i_id = i_id;
	}

	public String getEinheit_cnr_von() {
		return this.einheit_cnr_von;
	}

	public void setEinheit_cnr_von(String einheit_cnr_von) {
		this.einheit_cnr_von = einheit_cnr_von;
	}

	public String getEinheit_cnr_zu() {
		return this.einheit_cnr_zu;
	}

	public void setEinheit_cnr_zu(String einheit_cnr_zu) {
		this.einheit_cnr_zu = einheit_cnr_zu;
	}

	public BigDecimal getN_faktor() {
		return this.n_faktor;
	}

	public void setN_faktor(BigDecimal n_faktor) {
		this.n_faktor = n_faktor;
	}

	public com.lp.server.system.fastlanereader.generated.FLREinheit getFlreinheitvon() {
		return this.flreinheitvon;
	}

	public void setFlreinheitvon(
			com.lp.server.system.fastlanereader.generated.FLREinheit flreinheitvon) {
		this.flreinheitvon = flreinheitvon;
	}

	public com.lp.server.system.fastlanereader.generated.FLREinheit getFlreinheitzu() {
		return this.flreinheitzu;
	}

	public void setFlreinheitzu(
			com.lp.server.system.fastlanereader.generated.FLREinheit flreinheitzu) {
		this.flreinheitzu = flreinheitzu;
	}

	public String toString() {
		return new ToStringBuilder(this).append("i_id", getI_id()).toString();
	}

}
