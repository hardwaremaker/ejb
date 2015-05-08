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

@NamedQueries({ @NamedQuery(name = "ZeitmodellfindByCNrMandantCNr", query = "SELECT OBJECT(C) FROM Zeitmodell c WHERE c.cNr = ?1 AND  c.mandantCNr = ?2") })
@Entity
@Table(name = "PERS_ZEITMODELL")
public class Zeitmodell implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_NR")
	private String cNr;

	@Column(name = "B_TEILZEIT")
	private Short bTeilzeit;

	@Column(name = "B_DYNAMISCH")
	private Short bDynamisch;

	@Column(name = "B_FEIERTAGSSOLL_ADDIEREN")
	private Short bFeiertagssollAddieren;

	@Column(name = "N_MAXIMALES_WOCHENIST")
	private BigDecimal nMaximalesWochenist;

	public BigDecimal getNMaximalesWochenist() {
		return nMaximalesWochenist;
	}

	public void setNMaximalesWochenist(BigDecimal nMaximalesWochenist) {
		this.nMaximalesWochenist = nMaximalesWochenist;
	}

	public Short getBFeiertagssollAddieren() {
		return bFeiertagssollAddieren;
	}

	public void setBFeiertagssollAddieren(Short bFeiertagssollAddieren) {
		this.bFeiertagssollAddieren = bFeiertagssollAddieren;
	}

	public Short getBDynamisch() {
		return bDynamisch;
	}

	public void setBDynamisch(Short bDynamisch) {
		this.bDynamisch = bDynamisch;
	}

	@Column(name = "B_FIXEPAUSE_TROTZKOMMTGEHT")
	private Short bFixepauseTrotzkommtgeht;

	public Short getBFixepauseTrotzkommtgeht() {
		return bFixepauseTrotzkommtgeht;
	}

	public void setBFixepauseTrotzkommtgeht(Short bFixepauseTrotzkommtgeht) {
		this.bFixepauseTrotzkommtgeht = bFixepauseTrotzkommtgeht;
	}

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "B_VERSTECKT")
	private Short bVersteckt;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "F_URLAUBSTAGEPROWOCHE")
	private Double fUrlaubstageprowoche;

	@Column(name = "N_SOLLSTUNDENFIX")
	private BigDecimal nSollstundenfix;

	@Column(name = "I_MINUTENABZUG")
	private Integer iMinutenabzug;

	public Integer getIMinutenabzug() {
		return iMinutenabzug;
	}

	public void setIMinutenabzug(Integer iMinutenabzug) {
		this.iMinutenabzug = iMinutenabzug;
	}

	public BigDecimal getNSollstundenfix() {
		return nSollstundenfix;
	}

	public void setNSollstundenfix(BigDecimal nSollstundenfix) {
		this.nSollstundenfix = nSollstundenfix;
	}

	public Double getFUrlaubstageprowoche() {
		return fUrlaubstageprowoche;
	}

	public void setFUrlaubstageprowoche(Double fUrlaubstageprowoche) {
		this.fUrlaubstageprowoche = fUrlaubstageprowoche;
	}

	private static final long serialVersionUID = 1L;

	public Zeitmodell() {
		super();
	}

	public Zeitmodell(Integer id, String mandant2, String nr,
			Integer personalIIdAendern2, Double fUrlaubstageprowoche,
			Short bDynamisch, Integer iMinutenabzug,
			Short bFeiertagssollAddieren, Short bFixepauseTrotzkommtgeht) {
		setIId(id);
		setMandantCNr(mandant2);
		setCNr(nr);
		setPersonalIIdAendern(personalIIdAendern2);
		setTAendern(new Timestamp(System.currentTimeMillis()));
		setBTeilzeit(new Short((short) 0));
		setBVersteckt(new Short((short) 0));
		setFUrlaubstageprowoche(fUrlaubstageprowoche);
		setBDynamisch(bDynamisch);
		setIMinutenabzug(iMinutenabzug);
		setBFeiertagssollAddieren(bFeiertagssollAddieren);
		setBFixepauseTrotzkommtgeht(bFixepauseTrotzkommtgeht);
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

	public Short getBTeilzeit() {
		return this.bTeilzeit;
	}

	public void setBTeilzeit(Short bTeilzeit) {
		this.bTeilzeit = bTeilzeit;
	}

	public Timestamp getTAendern() {
		return this.tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Short getBVersteckt() {
		return this.bVersteckt;
	}

	public void setBVersteckt(Short bVersteckt) {
		this.bVersteckt = bVersteckt;
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

}
