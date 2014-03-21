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
package com.lp.server.personal.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PERS_ZUTRITTSLOG")
public class Zutrittslog implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_AUSWEIS")
	private String cAusweis;

	@Column(name = "C_PERSON")
	private String cPerson;

	@Column(name = "C_ZUTRITTSCONTROLLER")
	private String cZutrittscontroller;

	@Column(name = "C_ZUTRITTSOBJEKT")
	private String cZutrittsobjekt;

	@Column(name = "T_ZEITPUNKT")
	private Timestamp tZeitpunkt;

	@Column(name = "B_ERLAUBT")
	private Short bErlaubt;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "MANDANT_C_NR_OBJEKT")
	private String mandantCNrObjekt;

	private static final long serialVersionUID = 1L;

	public Zutrittslog() {
		super();
	}

	public Zutrittslog(Integer id, String zutrittscontroller,
			String zutrittsobjekt, Timestamp zeitpunkt, String mandantCNr,
			Short erlaubt, String mandantCNrObjekt2) {
		setIId(id);
		setCZutrittscontroller(zutrittscontroller);
		setCZutrittsobjekt(zutrittsobjekt);
		setTZeitpunkt(zeitpunkt);
		setMandantCNr(mandantCNr);
		setBErlaubt(erlaubt);
		setMandantCNrObjekt(mandantCNrObjekt2);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCAusweis() {
		return this.cAusweis;
	}

	public void setCAusweis(String cAusweis) {
		this.cAusweis = cAusweis;
	}

	public String getCPerson() {
		return this.cPerson;
	}

	public void setCPerson(String cPerson) {
		this.cPerson = cPerson;
	}

	public String getCZutrittscontroller() {
		return this.cZutrittscontroller;
	}

	public void setCZutrittscontroller(String cZutrittscontroller) {
		this.cZutrittscontroller = cZutrittscontroller;
	}

	public String getCZutrittsobjekt() {
		return this.cZutrittsobjekt;
	}

	public void setCZutrittsobjekt(String cZutrittsobjekt) {
		this.cZutrittsobjekt = cZutrittsobjekt;
	}

	public Timestamp getTZeitpunkt() {
		return this.tZeitpunkt;
	}

	public void setTZeitpunkt(Timestamp tZeitpunkt) {
		this.tZeitpunkt = tZeitpunkt;
	}

	public Short getBErlaubt() {
		return this.bErlaubt;
	}

	public void setBErlaubt(Short bErlaubt) {
		this.bErlaubt = bErlaubt;
	}

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public String getMandantCNrObjekt() {
		return this.mandantCNrObjekt;
	}

	public void setMandantCNrObjekt(String mandantCNrObjekt) {
		this.mandantCNrObjekt = mandantCNrObjekt;
	}

}
