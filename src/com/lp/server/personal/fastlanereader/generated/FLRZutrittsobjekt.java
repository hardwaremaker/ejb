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
package com.lp.server.personal.fastlanereader.generated;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.lp.server.system.fastlanereader.generated.FLRMandant;

/** @author Hibernate CodeGenerator */
public class FLRZutrittsobjekt implements Serializable {

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
	private String c_bez;

	/** nullable persistent field */
	private String c_adresse;

	/** nullable persistent field */
	private String zutrittsleser_c_nr;

	/** nullable persistent field */
	private String c_relais;

	/** nullable persistent field */
	private Double f_oeffnungszeit;

	/** nullable persistent field */
	private Integer zutrittscontroller_i_id;

	/** nullable persistent field */
	private FLRMandant flrmandant;

	/** nullable persistent field */
	private com.lp.server.personal.fastlanereader.generated.FLRZutrittscontroller flrzutrittscontroller;

	/** full constructor */
	public FLRZutrittsobjekt(
			String c_nr,
			String mandant_c_nr,
			String c_bez,
			String c_adresse,
			String zutrittsleser_c_nr,
			String c_relais,
			Double f_oeffnungszeit,
			Integer zutrittscontroller_i_id,
			FLRMandant flrmandant,
			com.lp.server.personal.fastlanereader.generated.FLRZutrittscontroller flrzutrittscontroller) {
		this.c_nr = c_nr;
		this.mandant_c_nr = mandant_c_nr;
		this.c_bez = c_bez;
		this.c_adresse = c_adresse;
		this.zutrittsleser_c_nr = zutrittsleser_c_nr;
		this.c_relais = c_relais;
		this.f_oeffnungszeit = f_oeffnungszeit;
		this.zutrittscontroller_i_id = zutrittscontroller_i_id;
		this.flrmandant = flrmandant;
		this.flrzutrittscontroller = flrzutrittscontroller;
	}

	/** default constructor */
	public FLRZutrittsobjekt() {
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

	public String getC_bez() {
		return this.c_bez;
	}

	public void setC_bez(String c_bez) {
		this.c_bez = c_bez;
	}

	public String getC_adresse() {
		return this.c_adresse;
	}

	public void setC_adresse(String c_adresse) {
		this.c_adresse = c_adresse;
	}

	public String getZutrittsleser_c_nr() {
		return this.zutrittsleser_c_nr;
	}

	public void setZutrittsleser_c_nr(String zutrittsleser_c_nr) {
		this.zutrittsleser_c_nr = zutrittsleser_c_nr;
	}

	public String getC_relais() {
		return this.c_relais;
	}

	public void setC_relais(String c_relais) {
		this.c_relais = c_relais;
	}

	public Double getF_oeffnungszeit() {
		return this.f_oeffnungszeit;
	}

	public void setF_oeffnungszeit(Double f_oeffnungszeit) {
		this.f_oeffnungszeit = f_oeffnungszeit;
	}

	public Integer getZutrittscontroller_i_id() {
		return this.zutrittscontroller_i_id;
	}

	public void setZutrittscontroller_i_id(Integer zutrittscontroller_i_id) {
		this.zutrittscontroller_i_id = zutrittscontroller_i_id;
	}

	public FLRMandant getFlrmandant() {
		return this.flrmandant;
	}

	public void setFlrmandant(FLRMandant flrmandant) {
		this.flrmandant = flrmandant;
	}

	public com.lp.server.personal.fastlanereader.generated.FLRZutrittscontroller getFlrzutrittscontroller() {
		return this.flrzutrittscontroller;
	}

	public void setFlrzutrittscontroller(
			com.lp.server.personal.fastlanereader.generated.FLRZutrittscontroller flrzutrittscontroller) {
		this.flrzutrittscontroller = flrzutrittscontroller;
	}

	public String toString() {
		return new ToStringBuilder(this).append("i_id", getI_id()).toString();
	}

}
