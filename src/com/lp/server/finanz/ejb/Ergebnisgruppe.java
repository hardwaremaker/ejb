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
package com.lp.server.finanz.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.server.util.ICBez;

@NamedQueries( { @NamedQuery(name = "ErgebnisgruppeejbSelectNextReihung", query = "SELECT MAX (eg.iReihung) FROM Ergebnisgruppe AS eg WHERE eg.mandantCNr = ?1 AND eg.bBilanzgruppe = ?2"),
				 @NamedQuery(name = "ErgebnisgruppefindByMandantCNr", query = "SELECT OBJECT(o) FROM Ergebnisgruppe o WHERE o.mandantCNr = ?1 AND o.bBilanzgruppe = ?2 ORDER BY o.iReihung")})
				 
@Entity
@Table(name = "FB_ERGEBNISGRUPPE")
public class Ergebnisgruppe implements Serializable, ICBez {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_BEZ")
	private String cBez;

	@Column(name = "I_REIHUNG")
	private Integer iReihung;

	@Column(name = "B_SUMMENEGATIV")
	private Short bSummenegativ;

	@Column(name = "B_INVERTIERT")
	private Short bInvertiert;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "B_PROZENTBASIS")
	private Short bProzentbasis;

	@Column(name = "I_TYP")
	private Integer iTyp;

	@Column(name = "ERGEBNISGRUPPE_I_ID_SUMME")
	private Integer ergebnisgruppeIIdSumme;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "B_BILANZGRUPPE")
	private Short bBilanzgruppe;
	
	@Column(name = "B_JAHRESGEWINN")
	private Short bJahresgewinn;
	
	public Short getBBilanzgruppe() {
		return bBilanzgruppe;
	}

	public void setBBilanzgruppe(Short bBilanzgruppe) {
		this.bBilanzgruppe = bBilanzgruppe;
	}

	private static final long serialVersionUID = 1L;

	public Ergebnisgruppe() {
		super();
	}

	public Ergebnisgruppe(Integer id, java.lang.String mandantCNr,
			java.lang.String bez, Integer reihung, Short summeNegativ,
			Short invertiert, Integer personalIIdAnlegen,
			Integer personalIIdAendern, Short prozentbasis, Integer typ, Short bBilanzgruppe,
			Short bJahresgewinn) {
		setIId(id);
		setMandantCNr(mandantCNr);
		setCBez(bez);
		setIReihung(reihung);
		setBSummenegativ(summeNegativ);
		setBInvertiert(invertiert);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setPersonalIIdAendern(personalIIdAendern);
		// Setzen der NOT NULL felder
	    Timestamp now = new Timestamp(System.currentTimeMillis());
	    this.setTAendern(now);
	    this.setTAnlegen(now);
		setBProzentbasis(prozentbasis);
		setITyp(typ);
		setBBilanzgruppe(bBilanzgruppe);
		setBJahresgewinn(bJahresgewinn);
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

	public Integer getIReihung() {
		return this.iReihung;
	}

	public void setIReihung(Integer iReihung) {
		this.iReihung = iReihung;
	}

	public Short getBSummenegativ() {
		return this.bSummenegativ;
	}

	public void setBSummenegativ(Short bSummenegativ) {
		this.bSummenegativ = bSummenegativ;
	}

	public Short getBInvertiert() {
		return this.bInvertiert;
	}

	public void setBInvertiert(Short bInvertiert) {
		this.bInvertiert = bInvertiert;
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

	public Short getBProzentbasis() {
		return this.bProzentbasis;
	}

	public void setBProzentbasis(Short bProzentbasis) {
		this.bProzentbasis = bProzentbasis;
	}

	public Integer getITyp() {
		return this.iTyp;
	}

	public void setITyp(Integer iTyp) {
		this.iTyp = iTyp;
	}

	public Integer getErgebnisgruppeIIdSumme() {
		return this.ergebnisgruppeIIdSumme;
	}

	public void setErgebnisgruppeIIdSumme(Integer ergebnisgruppeIIdSumme) {
		this.ergebnisgruppeIIdSumme = ergebnisgruppeIIdSumme;
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

	public Short getBJahresgewinn() {
		return bJahresgewinn;
	}
	
	public void setBJahresgewinn(Short bJahresgewinn) {
		this.bJahresgewinn = bJahresgewinn;
	}

}
