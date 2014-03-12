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
package com.lp.server.fertigung.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "FERT_INTERNEBESTELLUNG")
public class Internebestellung implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "INTERNEBESTELLUNG_I_ID_ELTERNLOS")
	private Integer internebestellungIIdElternlos;

	@Column(name = "I_BELEGIID")
	private Integer iBelegiid;

	@Column(name = "N_MENGE")
	private BigDecimal nMenge;

	@Column(name = "T_LIEFERTERMIN")
	private Date tLiefertermin;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "I_BELEGPOSITIONIID")
	private Integer iBelegpositioniid;

	@Column(name = "BELEGART_C_NR")
	private String belegartCNr;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "STUECKLISTE_I_ID")
	private Integer stuecklisteIId;

	private static final long serialVersionUID = 1L;

	public Internebestellung() {
		super();
	}

	public Internebestellung(Integer id,
			String mandantCNr,
			String belegartCNr,
			Integer belegiid,
			Integer stuecklisteIId,
			BigDecimal menge,
			Timestamp liefertermin,
			Integer personalIIdAendern) {
		setIId(id);
		setMandantCNr(mandantCNr);
		setBelegartCNr(belegartCNr);
		setIBelegiid(belegiid);
		setStuecklisteIId(stuecklisteIId);
		setNMenge(menge);
		setTLiefertermin(new Date(liefertermin.getTime()));
		setPersonalIIdAendern(personalIIdAendern);
		// Setzen der NOT NULL felder
	    Timestamp now = new Timestamp(System.currentTimeMillis());
	    setTAendern(now);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getInternebestellungIIdElternlos() {
		return this.internebestellungIIdElternlos;
	}

	public void setInternebestellungIIdElternlos(
			Integer internebestellungIIdElternlos) {
		this.internebestellungIIdElternlos = internebestellungIIdElternlos;
	}

	public Integer getIBelegiid() {
		return this.iBelegiid;
	}

	public void setIBelegiid(Integer iBelegiid) {
		this.iBelegiid = iBelegiid;
	}

	public BigDecimal getNMenge() {
		return this.nMenge;
	}

	public void setNMenge(BigDecimal nMenge) {
		this.nMenge = nMenge;
	}

	public Date getTLiefertermin() {
		return this.tLiefertermin;
	}

	public void setTLiefertermin(Date tLiefertermin) {
		this.tLiefertermin = tLiefertermin;
	}

	public Timestamp getTAendern() {
		return this.tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Integer getIBelegpositionIId() {
		return this.iBelegpositioniid;
	}

	public void setIBelegpositionIId(Integer iBelegpositioniid) {
		this.iBelegpositioniid = iBelegpositioniid;
	}

	public String getBelegartCNr() {
		return this.belegartCNr;
	}

	public void setBelegartCNr(String belegartCNr) {
		this.belegartCNr = belegartCNr;
	}

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Integer getStuecklisteIId() {
		return this.stuecklisteIId;
	}

	public void setStuecklisteIId(Integer stuecklisteIId) {
		this.stuecklisteIId = stuecklisteIId;
	}

}
