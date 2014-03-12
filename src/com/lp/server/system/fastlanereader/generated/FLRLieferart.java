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
package com.lp.server.system.fastlanereader.generated;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class FLRLieferart implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Integer i_id;

	/** persistent field */
	private String mandant_c_nr;

	/** persistent field */
	private String c_nr;

	/** persistent field */
	private Short b_frachtkostenalserledigtverbuchen;

	/** persistent field */
	private String c_versandort;

	/** nullable persistent field */
	private Short b_versteckt;

	/** persistent field */
	private Set<?> lieferart_lieferartspr_set;

	/** full constructor */
	public FLRLieferart(String mandant_c_nr, String c_nr,
			Short b_frachtkostenalserledigtverbuchen, String c_versandort,
			Short b_versteckt, Set<?> lieferart_lieferartspr_set) {
		this.mandant_c_nr = mandant_c_nr;
		this.c_nr = c_nr;
		this.b_frachtkostenalserledigtverbuchen = b_frachtkostenalserledigtverbuchen;
		this.c_versandort = c_versandort;
		this.b_versteckt = b_versteckt;
		this.lieferart_lieferartspr_set = lieferart_lieferartspr_set;
	}

	/** default constructor */
	public FLRLieferart() {
	}

	/** minimal constructor */
	public FLRLieferart(String mandant_c_nr, String c_nr,
			Short b_frachtkostenalserledigtverbuchen, String c_versandort,
			Set<?> lieferart_lieferartspr_set) {
		this.mandant_c_nr = mandant_c_nr;
		this.c_nr = c_nr;
		this.b_frachtkostenalserledigtverbuchen = b_frachtkostenalserledigtverbuchen;
		this.c_versandort = c_versandort;
		this.lieferart_lieferartspr_set = lieferart_lieferartspr_set;
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

	public String getC_nr() {
		return this.c_nr;
	}

	public void setC_nr(String c_nr) {
		this.c_nr = c_nr;
	}

	public Short getB_frachtkostenalserledigtverbuchen() {
		return this.b_frachtkostenalserledigtverbuchen;
	}

	public void setB_frachtkostenalserledigtverbuchen(
			Short b_frachtkostenalserledigtverbuchen) {
		this.b_frachtkostenalserledigtverbuchen = b_frachtkostenalserledigtverbuchen;
	}

	public String getC_versandort() {
		return this.c_versandort;
	}

	public void setC_versandort(String c_versandort) {
		this.c_versandort = c_versandort;
	}

	public Short getB_versteckt() {
		return this.b_versteckt;
	}

	public void setB_versteckt(Short b_versteckt) {
		this.b_versteckt = b_versteckt;
	}

	public Set<?> getLieferart_lieferartspr_set() {
		return this.lieferart_lieferartspr_set;
	}

	public void setLieferart_lieferartspr_set(Set<?> lieferart_lieferartspr_set) {
		this.lieferart_lieferartspr_set = lieferart_lieferartspr_set;
	}

	public String toString() {
		return new ToStringBuilder(this).append("i_id", getI_id()).toString();
	}

}
