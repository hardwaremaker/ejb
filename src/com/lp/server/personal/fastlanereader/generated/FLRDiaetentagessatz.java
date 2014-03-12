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
package com.lp.server.personal.fastlanereader.generated;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class FLRDiaetentagessatz implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	/** identifier field */
	private Integer i_id;

	/** nullable persistent field */
	private Date t_gueltigab;

	/** nullable persistent field */
	private BigDecimal n_mindestsatz;

	/** nullable persistent field */
	private BigDecimal n_tagessatz;

	/** nullable persistent field */
	private BigDecimal n_stundensatz;

	/** nullable persistent field */
	private Integer i_abstunden;

	/** nullable persistent field */
	private Short b_stundenweise;

	/** nullable persistent field */
	private com.lp.server.personal.fastlanereader.generated.FLRDiaeten flrdiaeten;

	/** full constructor */
	public FLRDiaetentagessatz(
			Date t_gueltigab,
			BigDecimal n_mindestsatz,
			BigDecimal n_tagessatz,
			BigDecimal n_stundensatz,
			Integer i_abstunden,
			Short b_stundenweise,
			com.lp.server.personal.fastlanereader.generated.FLRDiaeten flrdiaeten) {
		this.t_gueltigab = t_gueltigab;
		this.n_mindestsatz = n_mindestsatz;
		this.n_tagessatz = n_tagessatz;
		this.n_stundensatz = n_stundensatz;
		this.i_abstunden = i_abstunden;
		this.b_stundenweise = b_stundenweise;
		this.flrdiaeten = flrdiaeten;
	}

	/** default constructor */
	public FLRDiaetentagessatz() {
	}

	public Integer getI_id() {
		return this.i_id;
	}

	public void setI_id(Integer i_id) {
		this.i_id = i_id;
	}

	public Date getT_gueltigab() {
		return this.t_gueltigab;
	}

	public void setT_gueltigab(Date t_gueltigab) {
		this.t_gueltigab = t_gueltigab;
	}

	public BigDecimal getN_mindestsatz() {
		return this.n_mindestsatz;
	}

	public void setN_mindestsatz(BigDecimal n_mindestsatz) {
		this.n_mindestsatz = n_mindestsatz;
	}

	public BigDecimal getN_tagessatz() {
		return this.n_tagessatz;
	}

	public void setN_tagessatz(BigDecimal n_tagessatz) {
		this.n_tagessatz = n_tagessatz;
	}

	public BigDecimal getN_stundensatz() {
		return this.n_stundensatz;
	}

	public void setN_stundensatz(BigDecimal n_stundensatz) {
		this.n_stundensatz = n_stundensatz;
	}

	public Integer getI_abstunden() {
		return this.i_abstunden;
	}

	public void setI_abstunden(Integer i_abstunden) {
		this.i_abstunden = i_abstunden;
	}

	public Short getB_stundenweise() {
		return this.b_stundenweise;
	}

	public void setB_stundenweise(Short b_stundenweise) {
		this.b_stundenweise = b_stundenweise;
	}

	public com.lp.server.personal.fastlanereader.generated.FLRDiaeten getFlrdiaeten() {
		return this.flrdiaeten;
	}

	public void setFlrdiaeten(
			com.lp.server.personal.fastlanereader.generated.FLRDiaeten flrdiaeten) {
		this.flrdiaeten = flrdiaeten;
	}

	public String toString() {
		return new ToStringBuilder(this).append("i_id", getI_id()).toString();
	}

}
