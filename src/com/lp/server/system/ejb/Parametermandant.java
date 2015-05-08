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
package com.lp.server.system.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.lp.util.Helper;

@Entity
@Table(name = "LP_PARAMETERMANDANT")
public class Parametermandant implements Serializable {
	@EmbeddedId
	private ParametermandantPK pk;

	@Column(name = "C_WERT")
	private String cWert;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "C_BEMERKUNGSMALL")
	private String cBemerkungsmall;

	@Column(name = "C_BEMERKUNGLARGE")
	private String cBemerkunglarge;

	@Column(name = "C_DATENTYP")
	private String cDatentyp;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	private static final long serialVersionUID = 1L;

	public Parametermandant() {
		super();
	}

	public Parametermandant(String nr, String mandantCMandant,
			String kategorie, String wert, Integer personal,
			String bemerkungsmall, String datentyp) {
		setPk(new ParametermandantPK(nr, mandantCMandant, kategorie));
		setPersonalIIdAendern(personal);
		setCBemerkungsmall(bemerkungsmall);
		setCDatentyp(datentyp);
		setCWert(wert);
		// die ts anlegen, aendern nur am server
		setTAendern(new Timestamp(System.currentTimeMillis()));
	}

	public ParametermandantPK getPk() {
		return this.pk;
	}

	public void setPk(ParametermandantPK pk) {
		this.pk = pk;
	}

	public String getCWert() {
		if (Helper.istKennwortParameter(pk.getCNr())) {
			return Helper.decode(this.cWert);
		} else {
			return this.cWert;
		}
	}

	public void setCWert(String cWert) {
		if (Helper.istKennwortParameter(pk.getCNr())) {
			this.cWert = Helper.encode(cWert);
		} else {
			this.cWert = cWert;
		}
	}

	public Timestamp getTAendern() {
		return this.tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public String getCBemerkungsmall() {
		return this.cBemerkungsmall;
	}

	public void setCBemerkungsmall(String cBemerkungsmall) {
		this.cBemerkungsmall = cBemerkungsmall;
	}

	public String getCBemerkunglarge() {
		return this.cBemerkunglarge;
	}

	public void setCBemerkunglarge(String cBemerkunglarge) {
		this.cBemerkunglarge = cBemerkunglarge;
	}

	public String getCDatentyp() {
		return this.cDatentyp;
	}

	public void setCDatentyp(String cDatentyp) {
		this.cDatentyp = cDatentyp;
	}

	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

}
