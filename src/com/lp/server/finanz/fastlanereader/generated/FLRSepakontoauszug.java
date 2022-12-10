/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2016 HELIUM V IT-Solutions GmbH
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
package com.lp.server.finanz.fastlanereader.generated;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

public class FLRSepakontoauszug implements Serializable {

	private static final long serialVersionUID = -7774138093402911042L;

	private Integer i_id;
	
	private Integer bankverbindung_i_id;
	
	private Integer i_auszug;
	
	private Date t_auszug;
	
	private BigDecimal n_anfangssaldo;
	
	private BigDecimal n_endsaldo;
	
	private String c_camt_format;
	
	private Date t_anlegen;
	
    private Integer personal_i_id_anlegen;

    private Date t_verbuchen;
	
    private Integer personal_i_id_verbuchen;
    
    private String status_c_nr;

	public FLRSepakontoauszug(Integer i_id, Integer bankverbindung_i_id, Integer i_auszug, Date t_auszug, BigDecimal n_anfangssaldo, BigDecimal n_endsaldo,
			String c_camt_format, Date t_verbuchen, Integer personal_i_id_verbuchen, Date t_anlegen, Integer personal_i_id_anlegen, String status_c_nr) {
		this.i_id = i_id;
		this.bankverbindung_i_id = bankverbindung_i_id;
		this.i_auszug = i_auszug;
		this.n_anfangssaldo = n_anfangssaldo;
		this.n_endsaldo = n_endsaldo;
		this.c_camt_format = c_camt_format;
		this.t_verbuchen = t_verbuchen;
		this.personal_i_id_verbuchen = personal_i_id_verbuchen;
		this.t_anlegen = t_anlegen;
		this.personal_i_id_anlegen = personal_i_id_anlegen;
		this.t_auszug = t_auszug;
		this.status_c_nr = status_c_nr;
	}

	public FLRSepakontoauszug() {
	}

	public Integer getI_id() {
		return i_id;
	}

	public void setI_id(Integer i_id) {
		this.i_id = i_id;
	}

	public Integer getI_auszug() {
		return i_auszug;
	}

	public void setI_auszug(Integer i_auszug) {
		this.i_auszug = i_auszug;
	}

	public BigDecimal getN_anfangssaldo() {
		return n_anfangssaldo;
	}

	public void setN_anfangssaldo(BigDecimal n_anfangssaldo) {
		this.n_anfangssaldo = n_anfangssaldo;
	}

	public BigDecimal getN_endsaldo() {
		return n_endsaldo;
	}

	public void setN_endsaldo(BigDecimal n_endsaldo) {
		this.n_endsaldo = n_endsaldo;
	}

	public String getC_camt_format() {
		return c_camt_format;
	}

	public void setC_camt_format(String c_camt_format) {
		this.c_camt_format = c_camt_format;
	}

	public String toString() {
		return new ToStringBuilder(this)
			.append("i_id=", getI_id())
			.append(", i_auszug=", getI_auszug())
			.append(", n_anfangssaldo=", getN_anfangssaldo())
			.append(", n_endsaldo=", getN_endsaldo())
			.append(", c_camt_format=", getC_camt_format())
			.append(", t_verbuchen=", getT_verbuchen())
			.append(", personal_i_id_verbuchen=", getPersonal_i_id_verbuchen())
			.toString();
	}

	public Date getT_verbuchen() {
		return t_verbuchen;
	}

	public void setT_verbuchen(Date t_verbuchen) {
		this.t_verbuchen = t_verbuchen;
	}

	public Integer getPersonal_i_id_verbuchen() {
		return personal_i_id_verbuchen;
	}

	public void setPersonal_i_id_verbuchen(Integer personal_i_id_verbuchen) {
		this.personal_i_id_verbuchen = personal_i_id_verbuchen;
	}

	public Date getT_anlegen() {
		return t_anlegen;
	}

	public void setT_anlegen(Date t_anlegen) {
		this.t_anlegen = t_anlegen;
	}

	public Integer getPersonal_i_id_anlegen() {
		return personal_i_id_anlegen;
	}

	public void setPersonal_i_id_anlegen(Integer personal_i_id_anlegen) {
		this.personal_i_id_anlegen = personal_i_id_anlegen;
	}

	public Date getT_auszug() {
		return t_auszug;
	}

	public void setT_auszug(Date t_auszug) {
		this.t_auszug = t_auszug;
	}

	public String getStatus_c_nr() {
		return status_c_nr;
	}

	public void setStatus_c_nr(String status_c_nr) {
		this.status_c_nr = status_c_nr;
	}

	public Integer getBankverbindung_i_id() {
		return bankverbindung_i_id;
	}

	public void setBankverbindung_i_id(Integer bankverbindung_i_id) {
		this.bankverbindung_i_id = bankverbindung_i_id;
	}
	
}
