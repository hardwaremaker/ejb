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
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "ReiselogfindByPartnerIId", query = "SELECT OBJECT(C) FROM Reiselog c WHERE c.partnerIId = ?1"),
		@NamedQuery(name = "ReiselogfindByAnsprechpartnerIId", query = "SELECT OBJECT(C) FROM Reiselog c WHERE c.ansprechpartnerIId = ?1") })
@Entity
@Table(name = "PERS_REISELOG")
public class Reiselog implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "REISE_I_ID")
	private Integer reiseIId;

	@Column(name = "T_ZEIT")
	private Timestamp tZeit;

	@Column(name = "B_BEGINN")
	private Short bBeginn;

	@Column(name = "I_KMBEGINN")
	private Integer iKmbeginn;

	@Column(name = "I_KMENDE")
	private Integer iKmende;

	@Column(name = "N_SPESEN")
	private BigDecimal nSpesen;

	@Column(name = "C_FAHRZEUG")
	private String cFahrzeug;

	@Column(name = "C_KOMMENTAR")
	private String cKommentar;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "C_ART")
	private String cArt;

	@Column(name = "ANSPRECHPARTNER_I_ID")
	private Integer ansprechpartnerIId;

	@Column(name = "PARTNER_I_ID")
	private Integer partnerIId;

	@Column(name = "DIAETEN_I_ID")
	private Integer diaetenIId;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID")
	private Integer personalIId;

	@Column(name = "FAHRZEUG_I_ID")
	private Integer fahrzeugIId;
	
	@Column(name = "I_BELEGARTID")
	private Integer iBelegartid;
	
	@Column(name = "BELEGART_C_NR")
	private String belegartCNr;
	
	@Column(name = "F_FAKTOR")
	private Double fFaktor;
	
	public Integer getFahrzeugIId() {
		return fahrzeugIId;
	}

	public void setFahrzeugIId(Integer fahrzeugIId) {
		this.fahrzeugIId = fahrzeugIId;
	}

	public Integer getIBelegartid() {
		return iBelegartid;
	}

	public void setIBelegartid(Integer iBelegartid) {
		this.iBelegartid = iBelegartid;
	}

	public String getBelegartCNr() {
		return belegartCNr;
	}

	public void setBelegartCNr(String belegartCNr) {
		this.belegartCNr = belegartCNr;
	}

	public Double getFFaktor() {
		return fFaktor;
	}

	public void setFFaktor(Double fFaktor) {
		this.fFaktor = fFaktor;
	}
	
	private static final long serialVersionUID = 1L;

	public Reiselog() {
		super();
	}

	public Reiselog(Integer pk, Integer id, Integer personalIId2,
			Timestamp zeit, Short beginn,
			Integer personalIIdAendern2, String art) {
		setIId(pk);
		setReiseIId(id);
		setPersonalIId(personalIId2);
		setTZeit(zeit);
		setBBeginn(beginn);
		setTAendern(new Timestamp(System.currentTimeMillis()));
		setPersonalIIdAendern(personalIIdAendern2);
		setCArt(art);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getReiseIId() {
		return this.reiseIId;
	}

	public void setReiseIId(Integer reiseIId) {
		this.reiseIId = reiseIId;
	}

	public Timestamp getTZeit() {
		return this.tZeit;
	}

	public void setTZeit(Timestamp tZeit) {
		this.tZeit = tZeit;
	}

	public Short getBBeginn() {
		return this.bBeginn;
	}

	public void setBBeginn(Short bBeginn) {
		this.bBeginn = bBeginn;
	}

	public Integer getIKmbeginn() {
		return this.iKmbeginn;
	}

	public void setIKmbeginn(Integer iKmbeginn) {
		this.iKmbeginn = iKmbeginn;
	}

	public Integer getIKmende() {
		return this.iKmende;
	}

	public void setIKmende(Integer iKmende) {
		this.iKmende = iKmende;
	}

	public BigDecimal getNSpesen() {
		return this.nSpesen;
	}

	public void setNSpesen(BigDecimal nSpesen) {
		this.nSpesen = nSpesen;
	}

	public String getCFahrzeug() {
		return this.cFahrzeug;
	}

	public void setCFahrzeug(String cFahrzeug) {
		this.cFahrzeug = cFahrzeug;
	}

	public String getCKommentar() {
		return this.cKommentar;
	}

	public void setCKommentar(String cKommentar) {
		this.cKommentar = cKommentar;
	}

	public Timestamp getTAendern() {
		return this.tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public String getCArt() {
		return this.cArt;
	}

	public void setCArt(String cArt) {
		this.cArt = cArt;
	}

	public Integer getAnsprechpartnerIId() {
		return this.ansprechpartnerIId;
	}

	public void setAnsprechpartnerIId(Integer ansprechpartnerIId) {
		this.ansprechpartnerIId = ansprechpartnerIId;
	}

	public Integer getPartnerIId() {
		return this.partnerIId;
	}

	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId;
	}

	public Integer getDiaetenIId() {
		return this.diaetenIId;
	}

	public void setDiaetenIId(Integer diaetenIId) {
		this.diaetenIId = diaetenIId;
	}

	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Integer getPersonalIId() {
		return this.personalIId;
	}

	public void setPersonalIId(Integer personalIId) {
		this.personalIId = personalIId;
	}

}
