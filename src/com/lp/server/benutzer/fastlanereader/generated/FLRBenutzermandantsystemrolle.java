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

import org.apache.commons.lang.builder.ToStringBuilder;

import com.lp.server.system.fastlanereader.generated.FLRMandant;

/** @author Hibernate CodeGenerator */
public class FLRBenutzermandantsystemrolle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Integer i_id;

	/** nullable persistent field */
	private FLRMandant flrmandant;

	/** nullable persistent field */
	private com.lp.server.benutzer.fastlanereader.generated.FLRBenutzer flrbenutzer;

	/** nullable persistent field */
	private com.lp.server.benutzer.fastlanereader.generated.FLRSystemrolle flrsystemrolle;

	/** full constructor */
	public FLRBenutzermandantsystemrolle(
			FLRMandant flrmandant,
			com.lp.server.benutzer.fastlanereader.generated.FLRBenutzer flrbenutzer,
			com.lp.server.benutzer.fastlanereader.generated.FLRSystemrolle flrsystemrolle) {
		this.flrmandant = flrmandant;
		this.flrbenutzer = flrbenutzer;
		this.flrsystemrolle = flrsystemrolle;
	}

	/** default constructor */
	public FLRBenutzermandantsystemrolle() {
	}

	public Integer getI_id() {
		return this.i_id;
	}

	public void setI_id(Integer i_id) {
		this.i_id = i_id;
	}

	public FLRMandant getFlrmandant() {
		return this.flrmandant;
	}

	public void setFlrmandant(FLRMandant flrmandant) {
		this.flrmandant = flrmandant;
	}

	public com.lp.server.benutzer.fastlanereader.generated.FLRBenutzer getFlrbenutzer() {
		return this.flrbenutzer;
	}

	public void setFlrbenutzer(
			com.lp.server.benutzer.fastlanereader.generated.FLRBenutzer flrbenutzer) {
		this.flrbenutzer = flrbenutzer;
	}

	public com.lp.server.benutzer.fastlanereader.generated.FLRSystemrolle getFlrsystemrolle() {
		return this.flrsystemrolle;
	}

	public void setFlrsystemrolle(
			com.lp.server.benutzer.fastlanereader.generated.FLRSystemrolle flrsystemrolle) {
		this.flrsystemrolle = flrsystemrolle;
	}

	public String toString() {
		return new ToStringBuilder(this).append("i_id", getI_id()).toString();
	}

}
