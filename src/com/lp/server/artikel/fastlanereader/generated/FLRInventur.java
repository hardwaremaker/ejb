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
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.lp.server.personal.fastlanereader.generated.FLRPersonal;

/** @author Hibernate CodeGenerator */
public class FLRInventur implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Integer i_id;

	/** nullable persistent field */
	private String c_bez;

	/** nullable persistent field */
	private Date t_inventurdatum;

	/** nullable persistent field */
	private String mandant_c_nr;

	/** nullable persistent field */
	private Integer lager_i_id;

	/** nullable persistent field */
	private Short b_inventurdurchgefuehrt;

	/** nullable persistent field */
	private com.lp.server.artikel.fastlanereader.generated.FLRLager flrlager;

	/** nullable persistent field */
	private FLRPersonal flrpersonalinventurdurchgefuehrt;

	/** full constructor */
	public FLRInventur(String c_bez, Date t_inventurdatum, String mandant_c_nr,
			Integer lager_i_id, Short b_inventurdurchgefuehrt,
			com.lp.server.artikel.fastlanereader.generated.FLRLager flrlager,
			FLRPersonal flrpersonalinventurdurchgefuehrt) {
		this.c_bez = c_bez;
		this.t_inventurdatum = t_inventurdatum;
		this.mandant_c_nr = mandant_c_nr;
		this.lager_i_id = lager_i_id;
		this.b_inventurdurchgefuehrt = b_inventurdurchgefuehrt;
		this.flrlager = flrlager;
		this.flrpersonalinventurdurchgefuehrt = flrpersonalinventurdurchgefuehrt;
	}

	/** default constructor */
	public FLRInventur() {
	}

	public Integer getI_id() {
		return this.i_id;
	}

	public void setI_id(Integer i_id) {
		this.i_id = i_id;
	}

	public String getC_bez() {
		return this.c_bez;
	}

	public void setC_bez(String c_bez) {
		this.c_bez = c_bez;
	}

	public Date getT_inventurdatum() {
		return this.t_inventurdatum;
	}

	public void setT_inventurdatum(Date t_inventurdatum) {
		this.t_inventurdatum = t_inventurdatum;
	}

	public String getMandant_c_nr() {
		return this.mandant_c_nr;
	}

	public void setMandant_c_nr(String mandant_c_nr) {
		this.mandant_c_nr = mandant_c_nr;
	}

	public Integer getLager_i_id() {
		return this.lager_i_id;
	}

	public void setLager_i_id(Integer lager_i_id) {
		this.lager_i_id = lager_i_id;
	}

	public Short getB_inventurdurchgefuehrt() {
		return this.b_inventurdurchgefuehrt;
	}

	public void setB_inventurdurchgefuehrt(Short b_inventurdurchgefuehrt) {
		this.b_inventurdurchgefuehrt = b_inventurdurchgefuehrt;
	}

	public com.lp.server.artikel.fastlanereader.generated.FLRLager getFlrlager() {
		return this.flrlager;
	}

	public void setFlrlager(
			com.lp.server.artikel.fastlanereader.generated.FLRLager flrlager) {
		this.flrlager = flrlager;
	}

	public FLRPersonal getFlrpersonalinventurdurchgefuehrt() {
		return this.flrpersonalinventurdurchgefuehrt;
	}

	public void setFlrpersonalinventurdurchgefuehrt(
			FLRPersonal flrpersonalinventurdurchgefuehrt) {
		this.flrpersonalinventurdurchgefuehrt = flrpersonalinventurdurchgefuehrt;
	}

	public String toString() {
		return new ToStringBuilder(this).append("i_id", getI_id()).toString();
	}

}
