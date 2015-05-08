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
package com.lp.server.eingangsrechnung.fastlanereader.generated;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto;
import com.lp.server.system.fastlanereader.generated.FLRKostenstelle;

/** @author Hibernate CodeGenerator */
public class FLREingangsrechnungKontierung implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Integer i_id;

	/** nullable persistent field */
	private Integer eingangsrechnung_i_id;

	/** nullable persistent field */
	private BigDecimal n_betrag;

	/** nullable persistent field */
	private BigDecimal n_betrag_ust;

	/** nullable persistent field */
	private FLRKostenstelle flrkostenstelle;

	/** nullable persistent field */
	private FLRFinanzKonto flrkonto;

	/** nullable persistent field */
	private com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnung flreingangsrechnung;

	/** full constructor */
	public FLREingangsrechnungKontierung(
			Integer eingangsrechnung_i_id,
			BigDecimal n_betrag,
			BigDecimal n_betrag_ust,
			FLRKostenstelle flrkostenstelle,
			FLRFinanzKonto flrkonto,
			com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnung flreingangsrechnung) {
		this.eingangsrechnung_i_id = eingangsrechnung_i_id;
		this.n_betrag = n_betrag;
		this.n_betrag_ust = n_betrag_ust;
		this.flrkostenstelle = flrkostenstelle;
		this.flrkonto = flrkonto;
		this.flreingangsrechnung = flreingangsrechnung;
	}

	/** default constructor */
	public FLREingangsrechnungKontierung() {
	}

	public Integer getI_id() {
		return this.i_id;
	}

	public void setI_id(Integer i_id) {
		this.i_id = i_id;
	}

	public Integer getEingangsrechnung_i_id() {
		return this.eingangsrechnung_i_id;
	}

	public void setEingangsrechnung_i_id(Integer eingangsrechnung_i_id) {
		this.eingangsrechnung_i_id = eingangsrechnung_i_id;
	}

	public BigDecimal getN_betrag() {
		return this.n_betrag;
	}

	public void setN_betrag(BigDecimal n_betrag) {
		this.n_betrag = n_betrag;
	}

	public BigDecimal getN_betrag_ust() {
		return this.n_betrag_ust;
	}

	public void setN_betrag_ust(BigDecimal n_betrag_ust) {
		this.n_betrag_ust = n_betrag_ust;
	}

	public FLRKostenstelle getFlrkostenstelle() {
		return this.flrkostenstelle;
	}

	public void setFlrkostenstelle(FLRKostenstelle flrkostenstelle) {
		this.flrkostenstelle = flrkostenstelle;
	}

	public FLRFinanzKonto getFlrkonto() {
		return this.flrkonto;
	}

	public void setFlrkonto(FLRFinanzKonto flrkonto) {
		this.flrkonto = flrkonto;
	}

	public com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnung getFlreingangsrechnung() {
		return this.flreingangsrechnung;
	}

	public void setFlreingangsrechnung(
			com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnung flreingangsrechnung) {
		this.flreingangsrechnung = flreingangsrechnung;
	}

	public String toString() {
		return new ToStringBuilder(this).append("i_id", getI_id()).toString();
	}

}
