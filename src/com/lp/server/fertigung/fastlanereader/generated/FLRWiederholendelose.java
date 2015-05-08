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
package com.lp.server.fertigung.fastlanereader.generated;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.lp.server.stueckliste.fastlanereader.generated.FLRStueckliste;

/** @author Hibernate CodeGenerator */
public class FLRWiederholendelose implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Integer i_id;

	/** nullable persistent field */
	private String mandant_c_nr;

	/** nullable persistent field */
	private String c_projekt;

	/** nullable persistent field */
	private String auftragwiederholungsintervall_c_nr;

	/** nullable persistent field */
	private Integer stueckliste_i_id;

	/** nullable persistent field */
	private Date t_termin;

	/** nullable persistent field */
	private BigDecimal n_losgroesse;

	/** nullable persistent field */
	private Short b_versteckt;

	/** nullable persistent field */
	private Integer i_sort;

	/** nullable persistent field */
	private Integer i_tagevoreilend;

	/** nullable persistent field */
	private FLRStueckliste flrstueckliste;

	/** full constructor */
	public FLRWiederholendelose(String mandant_c_nr, String c_projekt,
			String auftragwiederholungsintervall_c_nr,
			Integer stueckliste_i_id, Date t_termin, BigDecimal n_losgroesse,
			Short b_versteckt, Integer i_sort, Integer i_tagevoreilend,
			FLRStueckliste flrstueckliste) {
		this.mandant_c_nr = mandant_c_nr;
		this.c_projekt = c_projekt;
		this.auftragwiederholungsintervall_c_nr = auftragwiederholungsintervall_c_nr;
		this.stueckliste_i_id = stueckliste_i_id;
		this.t_termin = t_termin;
		this.n_losgroesse = n_losgroesse;
		this.b_versteckt = b_versteckt;
		this.i_sort = i_sort;
		this.i_tagevoreilend = i_tagevoreilend;
		this.flrstueckliste = flrstueckliste;
	}

	/** default constructor */
	public FLRWiederholendelose() {
	}

	public Integer getI_id() {
		return this.i_id;
	}

	public void setI_id(Integer i_id) {
		this.i_id = i_id;
	}

	public String getMandant_c_nr() {
		return this.mandant_c_nr;
	}

	public void setMandant_c_nr(String mandant_c_nr) {
		this.mandant_c_nr = mandant_c_nr;
	}

	public String getC_projekt() {
		return this.c_projekt;
	}

	public void setC_projekt(String c_projekt) {
		this.c_projekt = c_projekt;
	}

	public String getAuftragwiederholungsintervall_c_nr() {
		return this.auftragwiederholungsintervall_c_nr;
	}

	public void setAuftragwiederholungsintervall_c_nr(
			String auftragwiederholungsintervall_c_nr) {
		this.auftragwiederholungsintervall_c_nr = auftragwiederholungsintervall_c_nr;
	}

	public Integer getStueckliste_i_id() {
		return this.stueckliste_i_id;
	}

	public void setStueckliste_i_id(Integer stueckliste_i_id) {
		this.stueckliste_i_id = stueckliste_i_id;
	}

	public Date getT_termin() {
		return this.t_termin;
	}

	public void setT_termin(Date t_termin) {
		this.t_termin = t_termin;
	}

	public BigDecimal getN_losgroesse() {
		return this.n_losgroesse;
	}

	public void setN_losgroesse(BigDecimal n_losgroesse) {
		this.n_losgroesse = n_losgroesse;
	}

	public Short getB_versteckt() {
		return this.b_versteckt;
	}

	public void setB_versteckt(Short b_versteckt) {
		this.b_versteckt = b_versteckt;
	}

	public Integer getI_sort() {
		return this.i_sort;
	}

	public void setI_sort(Integer i_sort) {
		this.i_sort = i_sort;
	}

	public Integer getI_tagevoreilend() {
		return this.i_tagevoreilend;
	}

	public void setI_tagevoreilend(Integer i_tagevoreilend) {
		this.i_tagevoreilend = i_tagevoreilend;
	}

	public FLRStueckliste getFlrstueckliste() {
		return this.flrstueckliste;
	}

	public void setFlrstueckliste(FLRStueckliste flrstueckliste) {
		this.flrstueckliste = flrstueckliste;
	}

	public String toString() {
		return new ToStringBuilder(this).append("i_id", getI_id()).toString();
	}

}
