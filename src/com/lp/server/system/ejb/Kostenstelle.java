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
package com.lp.server.system.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "KostenstellefindByMandant", query = "SELECT OBJECT (o) FROM Kostenstelle o  WHERE o.mandantCNr=?1"),
		@NamedQuery(name = "KostenstellefindByNummerMandant", query = "SELECT OBJECT(k) FROM Kostenstelle k WHERE k.cNr=?1 AND k.mandantCNr=?2") })
@Entity
@Table(name = "LP_KOSTENSTELLE")
public class Kostenstelle implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_NR")
	private String cNr;

	@Column(name = "B_PROFITCENTER")
	private Short bProfitcenter;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "C_BEZ")
	private String cBez;

	@Column(name = "X_BEMERKUNG")
	private String xBemerkung;

	@Column(name = "C_SUBDIRECTORY")
	private String cSubdirectory;

	@Column(name = "B_VERSTECKT")
	private Short bVersteckt;

	@Column(name = "KONTO_I_ID")
	private Integer konto_i_id;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "LAGER_I_ID_OHNEABBUCHUNG")
	private Integer lagerIIdOhneabbuchung;

	
	
	public Integer getLagerIIdOhneabbuchung() {
		return lagerIIdOhneabbuchung;
	}

	public void setLagerIIdOhneabbuchung(Integer lagerIIdOhneabbuchung) {
		this.lagerIIdOhneabbuchung = lagerIIdOhneabbuchung;
	}

	private static final long serialVersionUID = 1L;

	public Kostenstelle() {
		super();
	}

	public Kostenstelle(Integer id, String mandantCNr, String nr,
			Short profitcenter, Short versteckt) {
		setIId(id);
		//die ts anlegen, aendern nur am server
	    setTAnlegen(new Timestamp(System.currentTimeMillis()));
	    setTAendern(new Timestamp(System.currentTimeMillis()));
		setMandantCNr(mandantCNr);
		setCNr(nr);
		setBProfitcenter(profitcenter);
		setBVersteckt(versteckt);

	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCNr() {
		return this.cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public Short getBProfitcenter() {
		return this.bProfitcenter;
	}

	public void setBProfitcenter(Short bProfitcenter) {
		this.bProfitcenter = bProfitcenter;
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

	public String getCBez() {
		return this.cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public String getXBemerkung() {
		return this.xBemerkung;
	}

	public void setXBemerkung(String xBemerkung) {
		this.xBemerkung = xBemerkung;
	}

	public String getCSubdirectory() {
		return this.cSubdirectory;
	}

	public void setCSubdirectory(String cSubdirectory) {
		this.cSubdirectory = cSubdirectory;
	}

	public Short getBVersteckt() {
		return this.bVersteckt;
	}

	public void setBVersteckt(Short bVersteckt) {
		this.bVersteckt = bVersteckt;
	}

	public Integer getKontoIId() {
		return this.konto_i_id;
	}

	public void setKontoIId(Integer konto) {
		this.konto_i_id = konto;
	}

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandant) {
		this.mandantCNr = mandant;
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
