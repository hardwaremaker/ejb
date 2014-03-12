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
package com.lp.server.finanz.ejb;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
	@NamedQuery(name = "KassenbuchfindByKontoIId", query = "SELECT OBJECT(o) FROM Kassenbuch o WHERE o.kontoIId=?1")})
@Entity
@Table(name = "FB_KASSENBUCH")
public class Kassenbuch implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_BEZ")
	private String cBez;

	@Column(name = "B_NEGATIVERLAUBT")
	private Short bNegativerlaubt;

	@Column(name = "B_HAUPTKASSENBUCH")
	private Short bHauptkassenbuch;

	@Column(name = "T_GUELTIGVON")
	private Date tGueltigvon;

	@Column(name = "T_GUELTIGBIS")
	private Date tGueltigbis;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "KONTO_I_ID")
	private Integer kontoIId;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	private static final long serialVersionUID = 1L;

	public Kassenbuch() {
		super();
	}

	public Kassenbuch(Integer id, java.lang.String mandantCNr,
			java.lang.String bez, Integer kontoIId, Short negativErlaubt,
			Short hauptkassenbuch, Date gueltigVon,
			Integer personalIIdAnlegen, Integer personalIIdaendern) {
		setIId(id);
		setMandantCNr(mandantCNr);
		setCBez(bez);
		setKontoIId(kontoIId);
		setBNegativerlaubt(negativErlaubt);
		setBHauptkassenbuch(hauptkassenbuch);
		setTGueltigvon(gueltigVon);
		// Setzen der NOT NULL felder
		Timestamp now = new Timestamp(System.currentTimeMillis());
		this.setTAendern(now);
		this.setTAnlegen(now);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setPersonalIIdAendern(personalIIdaendern);

	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCBez() {
		return this.cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public Short getBNegativerlaubt() {
		return this.bNegativerlaubt;
	}

	public void setBNegativerlaubt(Short bNegativerlaubt) {
		this.bNegativerlaubt = bNegativerlaubt;
	}

	public Short getBHauptkassenbuch() {
		return this.bHauptkassenbuch;
	}

	public void setBHauptkassenbuch(Short bHauptkassenbuch) {
		this.bHauptkassenbuch = bHauptkassenbuch;
	}

	public Date getTGueltigvon() {
		return this.tGueltigvon;
	}

	public void setTGueltigvon(Date tGueltigvon) {
		this.tGueltigvon = tGueltigvon;
	}

	public Date getTGueltigbis() {
		return this.tGueltigbis;
	}

	public void setTGueltigbis(Date tGueltigbis) {
		this.tGueltigbis = tGueltigbis;
	}

	public Timestamp getTAnlegen() {
		return this.tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Timestamp getTAendern() {
		return this.tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Integer getKontoIId() {
		return this.kontoIId;
	}

	public void setKontoIId(Integer kontoIId) {
		this.kontoIId = kontoIId;
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

	public Integer getPersonalIIdAnlegen() {
		return this.personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

}
