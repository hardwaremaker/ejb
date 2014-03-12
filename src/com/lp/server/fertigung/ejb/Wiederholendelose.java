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
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "WiederholendeloseejbSelectNextReihung", query = "SELECT MAX (o.iSort) FROM Wiederholendelose AS o WHERE o.mandantCNr = ?1"),
		@NamedQuery(name = "WiederholendelosefindByFertigungsortpartnerIIdMandantCNr", query = "SELECT OBJECT (O) FROM Wiederholendelose o WHERE o.partnerIIdFertigungsort=?1 AND o.mandantCNr=?2") })
@Entity
@Table(name = "FERT_WIEDERHOLENDELOSE")
public class Wiederholendelose implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_PROJEKT")
	private String cProjekt;

	@Column(name = "N_LOSGROESSE")
	private BigDecimal nLosgroesse;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "T_TERMIN")
	private Timestamp tTermin;

	@Column(name = "I_TAGEVOREILEND")
	private Integer iTagevoreilend;

	@Column(name = "B_VERSTECKT")
	private Short bVersteckt;

	@Column(name = "I_SORT")
	private Integer iSort;

	@Column(name = "AUFTRAGWIEDERHOLUNGSINTERVALL_C_NR")
	private String auftragwiederholungsintervallCNr;

	@Column(name = "KOSTENSTELLE_I_ID")
	private Integer kostenstelleIId;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "PARTNER_I_ID_FERTIGUNGSORT")
	private Integer partnerIIdFertigungsort;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "FERTIGUNGSGRUPPE_I_ID")
	private Integer fertigungsgruppe;

	@Column(name = "STUECKLISTE_I_ID")
	private Integer stuecklisteIId;

	@Column(name = "LAGER_I_ID_ZIEL")
	private Integer lagerIIdZiel;

	private static final long serialVersionUID = 1L;

	public Wiederholendelose() {
		super();
	}

	public Wiederholendelose(Integer id, String mandantCNr2,
			Integer kostenstelleIId2, BigDecimal losgroesse,
			Integer partnerIIdFertigungsort2, Integer personalIIdAendern2,
			Timestamp termin, Integer lagerIIdZiel2,
			Integer tagevoreilend, Integer fertigungsgruppeIId,
			String auftragwiederholungsintervallCNr2, Short versteckt,
			Integer sort) {
		setIId(id);
		setMandantCNr(mandantCNr2);
		setKostenstelleIId(kostenstelleIId2);
		setNLosgroesse(losgroesse);
		setPartnerIIdFertigungsort(partnerIIdFertigungsort2);
		setPersonalIIdAendern(personalIIdAendern2);
		setTAendern(new Timestamp(System.currentTimeMillis()));
		setTTermin(termin);
		setLagerIIdZiel(lagerIIdZiel2);
		setITagevoreilend(tagevoreilend);
		setFertigungsgruppeIId(fertigungsgruppeIId);
		setAuftragwiederholungsintervallCNr(auftragwiederholungsintervallCNr2);
		setBVersteckt(versteckt);
		setISort(sort);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCProjekt() {
		return this.cProjekt;
	}

	public void setCProjekt(String cProjekt) {
		this.cProjekt = cProjekt;
	}

	public BigDecimal getNLosgroesse() {
		return this.nLosgroesse;
	}

	public void setNLosgroesse(BigDecimal nLosgroesse) {
		this.nLosgroesse = nLosgroesse;
	}

	public Timestamp getTAendern() {
		return this.tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Timestamp getTTermin() {
		return this.tTermin;
	}

	public void setTTermin(Timestamp tTermin) {
		this.tTermin = tTermin;
	}

	public Integer getITagevoreilend() {
		return this.iTagevoreilend;
	}

	public void setITagevoreilend(Integer iTagevoreilend) {
		this.iTagevoreilend = iTagevoreilend;
	}

	public Short getBVersteckt() {
		return this.bVersteckt;
	}

	public void setBVersteckt(Short bVersteckt) {
		this.bVersteckt = bVersteckt;
	}

	public Integer getISort() {
		return this.iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public String getAuftragwiederholungsintervallCNr() {
		return this.auftragwiederholungsintervallCNr;
	}

	public void setAuftragwiederholungsintervallCNr(
			String auftragwiederholungsintervallCNr) {
		this.auftragwiederholungsintervallCNr = auftragwiederholungsintervallCNr;
	}

	public Integer getKostenstelleIId() {
		return this.kostenstelleIId;
	}

	public void setKostenstelleIId(Integer kostenstelleIId) {
		this.kostenstelleIId = kostenstelleIId;
	}

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public Integer getPartnerIIdFertigungsort() {
		return this.partnerIIdFertigungsort;
	}

	public void setPartnerIIdFertigungsort(Integer partnerIIdFertigungsort) {
		this.partnerIIdFertigungsort = partnerIIdFertigungsort;
	}

	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Integer getFertigungsgruppeIId() {
		return this.fertigungsgruppe;
	}

	public void setFertigungsgruppeIId(Integer fertigungsgruppe) {
		this.fertigungsgruppe = fertigungsgruppe;
	}

	public Integer getStuecklisteIId() {
		return this.stuecklisteIId;
	}

	public void setStuecklisteIId(Integer stuecklisteIId) {
		this.stuecklisteIId = stuecklisteIId;
	}

	public Integer getLagerIIdZiel() {
		return this.lagerIIdZiel;
	}

	public void setLagerIIdZiel(Integer lagerIIdZiel) {
		this.lagerIIdZiel = lagerIIdZiel;
	}

}
