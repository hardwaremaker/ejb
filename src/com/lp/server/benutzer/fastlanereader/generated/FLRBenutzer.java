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
package com.lp.server.benutzer.fastlanereader.generated;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.lp.server.system.fastlanereader.generated.FLRMandant;

/** @author Hibernate CodeGenerator */
public class FLRBenutzer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Integer i_id;

	/** nullable persistent field */
	private String c_benutzerkennung;

	/** nullable persistent field */
	private Date t_gueltigbis;

	/** nullable persistent field */
	private FLRMandant flrmandantdefault;

	/** full constructor */
	public FLRBenutzer(String c_benutzerkennung, Date t_gueltigbis,
			FLRMandant flrmandantdefault) {
		this.c_benutzerkennung = c_benutzerkennung;
		this.t_gueltigbis = t_gueltigbis;
		this.flrmandantdefault = flrmandantdefault;
	}

	/** default constructor */
	public FLRBenutzer() {
	}

	public Integer getI_id() {
		return this.i_id;
	}

	public void setI_id(Integer i_id) {
		this.i_id = i_id;
	}

	public String getC_benutzerkennung() {
		return this.c_benutzerkennung;
	}

	public void setC_benutzerkennung(String c_benutzerkennung) {
		this.c_benutzerkennung = c_benutzerkennung;
	}

	public Date getT_gueltigbis() {
		return this.t_gueltigbis;
	}

	public void setT_gueltigbis(Date t_gueltigbis) {
		this.t_gueltigbis = t_gueltigbis;
	}

	public FLRMandant getFlrmandantdefault() {
		return this.flrmandantdefault;
	}

	public void setFlrmandantdefault(FLRMandant mandant_c_nr_default) {
		this.flrmandantdefault = mandant_c_nr_default;
	}

	public String toString() {
		return new ToStringBuilder(this).append("i_id", getI_id()).toString();
	}

}
