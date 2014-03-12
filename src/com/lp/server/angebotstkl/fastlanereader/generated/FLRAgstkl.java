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
package com.lp.server.angebotstkl.fastlanereader.generated;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.lp.server.partner.fastlanereader.generated.FLRKunde;

/** @author Hibernate CodeGenerator */
public class FLRAgstkl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Integer i_id;

	/** nullable persistent field */
	private String c_nr;

	/** nullable persistent field */
	private String mandant_c_nr;

	/** nullable persistent field */
	private String belegart_c_nr;

	/** nullable persistent field */
	private String c_bez;

	/** nullable persistent field */
	private String waehrung_c_nr;

	/** nullable persistent field */
	private Date t_belegdatum;

	/** nullable persistent field */
	private FLRKunde flrkunde;

	/** full constructor */
	public FLRAgstkl(String c_nr, String mandant_c_nr, String belegart_c_nr,
			String c_bez, String waehrung_c_nr, Date t_belegdatum,
			FLRKunde flrkunde) {
		this.c_nr = c_nr;
		this.mandant_c_nr = mandant_c_nr;
		this.belegart_c_nr = belegart_c_nr;
		this.c_bez = c_bez;
		this.waehrung_c_nr = waehrung_c_nr;
		this.t_belegdatum = t_belegdatum;
		this.flrkunde = flrkunde;
	}

	/** default constructor */
	public FLRAgstkl() {
	}

	public Integer getI_id() {
		return this.i_id;
	}

	public void setI_id(Integer i_id) {
		this.i_id = i_id;
	}

	public String getC_nr() {
		return this.c_nr;
	}

	public void setC_nr(String c_nr) {
		this.c_nr = c_nr;
	}

	public String getMandant_c_nr() {
		return this.mandant_c_nr;
	}

	public void setMandant_c_nr(String mandant_c_nr) {
		this.mandant_c_nr = mandant_c_nr;
	}

	public String getBelegart_c_nr() {
		return this.belegart_c_nr;
	}

	public void setBelegart_c_nr(String belegart_c_nr) {
		this.belegart_c_nr = belegart_c_nr;
	}

	public String getC_bez() {
		return this.c_bez;
	}

	public void setC_bez(String c_bez) {
		this.c_bez = c_bez;
	}

	public String getWaehrung_c_nr() {
		return this.waehrung_c_nr;
	}

	public void setWaehrung_c_nr(String waehrung_c_nr) {
		this.waehrung_c_nr = waehrung_c_nr;
	}

	public Date getT_belegdatum() {
		return this.t_belegdatum;
	}

	public void setT_belegdatum(Date t_belegdatum) {
		this.t_belegdatum = t_belegdatum;
	}

	public FLRKunde getFlrkunde() {
		return this.flrkunde;
	}

	public void setFlrkunde(FLRKunde flrkunde) {
		this.flrkunde = flrkunde;
	}

	public String toString() {
		return new ToStringBuilder(this).append("i_id", getI_id()).toString();
	}

}
