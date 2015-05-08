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
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "TaetigkeitfindByCNr", query = "SELECT OBJECT(o) FROM Taetigkeit o WHERE o.cNr = ?1"),
		@NamedQuery(name = "TaetigkeitfindByCImportkennzeichen", query = "SELECT OBJECT(o) FROM Taetigkeit o WHERE o.cImportkennzeichen = ?1"),
		@NamedQuery(name = "TaetigkeitfindByCImportkennzeichenNotNull", query = "SELECT OBJECT(o) FROM Taetigkeit o WHERE o.cImportkennzeichen is not null"),
		@NamedQuery(name = "TaetigkeitfindAll", query = "SELECT OBJECT(o) FROM Taetigkeit o ORDER BY o.iSort ASC"),
		@NamedQuery(name = "TaetigkeitfindByTaetigkeitartCNr", query = "SELECT OBJECT(o) FROM Taetigkeit o WHERE o.taetigkeitartCNr = ?1"),
		@NamedQuery(name = "TaetigkeitfindByBTagbuchbar", query = "SELECT OBJECT(o) FROM Taetigkeit o WHERE o.bTagbuchbar = ?1") })
@Entity
@Table(name = "PERS_TAETIGKEIT")
public class Taetigkeit implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_NR")
	private String cNr;

	@Column(name = "B_BDEBUCHBAR")
	private Short bBdebuchbar;

	@Column(name = "B_FEIERTAG")
	private Short bFeiertag;

	@Column(name = "B_TAGBUCHBAR")
	private Short bTagbuchbar;

	@Column(name = "B_UNTERBRICHTWARNMELDUNG")
	private Short bUnterbrichtwarnmeldung;
	
	@Column(name = "B_VERSTECKT")
	private Short bVersteckt;

	public Short getBUnterbrichtwarnmeldung() {
		return bUnterbrichtwarnmeldung;
	}

	public void setBUnterbrichtwarnmeldung(Short bUnterbrichtwarnmeldung) {
		this.bUnterbrichtwarnmeldung = bUnterbrichtwarnmeldung;
	}

	@Column(name = "I_SORT")
	private Integer iSort;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "TAETIGKEITART_C_NR")
	private String taetigkeitartCNr;

	@Column(name = "F_BEZAHLT")
	private Double fbezahlt;

	@Column(name = "C_IMPORTKENNZEICHEN")
	private String cImportkennzeichen;

	public String getCImportkennzeichen() {
		return cImportkennzeichen;
	}

	public void setCImportkennzeichen(String cImportkennzeichen) {
		this.cImportkennzeichen = cImportkennzeichen;
	}

	@Column(name = "I_WARNMELDUNGINKALENDERTAGEN")
	private Integer iWarnmeldunginkalendertagen;

	public Integer getIWarnmeldunginkalendertagen() {
		return iWarnmeldunginkalendertagen;
	}

	public void setIWarnmeldunginkalendertagen(
			Integer iWarnmeldunginkalendertagen) {
		this.iWarnmeldunginkalendertagen = iWarnmeldunginkalendertagen;
	}

	private static final long serialVersionUID = 1L;

	public Taetigkeit() {
		super();
	}

	public Double getFBezahlt() {
		return fbezahlt;
	}

	public void setFBezahlt(Double fbezahlt) {
		this.fbezahlt = fbezahlt;
	}

	public Taetigkeit(Integer id, String nr, String taetigkeitartCNr2,
			Integer sort, Integer personalIIdAendern2) {
		setIId(id);
		setCNr(nr);
		setTaetigkeitartCNr(taetigkeitartCNr2);
		setBBdebuchbar(new Short((short) 0));
		setFBezahlt((double) 0);
		setBFeiertag(new Short((short) 0));
		setBTagbuchbar(new Short((short) 0));
		setBTagbuchbar(new Short((short) 0));
		setbVersteckt(new Short((short) 0));
		setBUnterbrichtwarnmeldung(new Short((short) 1));
		setISort(sort);
		setTAendern(new Timestamp(System.currentTimeMillis()));
		setPersonalIIdAendern(personalIIdAendern2);
	}

	public Taetigkeit(Integer id, String nr, String taetigkeitartCNr2,
			Short bdebuchbar, Short feiertag, Double fBezahlt,
			Short tagbuchbar, Integer sort, Integer personalIIdAendern2,
			Short bUnterbrichtwarnmeldungen) {
		setIId(id);
		setCNr(nr);
		setTaetigkeitartCNr(taetigkeitartCNr2);
		setBBdebuchbar(bdebuchbar);
		setFBezahlt(fBezahlt);
		setBFeiertag(feiertag);
		setBTagbuchbar(tagbuchbar);
		setISort(sort);
		setTAendern(new Timestamp(System.currentTimeMillis()));
		setPersonalIIdAendern(personalIIdAendern2);
		setBUnterbrichtwarnmeldung(bUnterbrichtwarnmeldungen);
		setbVersteckt(new Short((short) 0));
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

	public Short getBBdebuchbar() {
		return this.bBdebuchbar;
	}

	public void setBBdebuchbar(Short bBdebuchbar) {
		this.bBdebuchbar = bBdebuchbar;
	}

	public Short getBFeiertag() {
		return this.bFeiertag;
	}

	public void setBFeiertag(Short bFeiertag) {
		this.bFeiertag = bFeiertag;
	}

	public Short getBTagbuchbar() {
		return this.bTagbuchbar;
	}

	public void setBTagbuchbar(Short bTagbuchbar) {
		this.bTagbuchbar = bTagbuchbar;
	}

	public Integer getISort() {
		return this.iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public Timestamp getTAendern() {
		return this.tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public String getTaetigkeitartCNr() {
		return this.taetigkeitartCNr;
	}

	public void setTaetigkeitartCNr(String taetigkeitartCNr) {
		this.taetigkeitartCNr = taetigkeitartCNr;
	}
	
	public void setbVersteckt(Short bVersteckt) {
		this.bVersteckt = bVersteckt;
	}
	
	public Short getbVersteckt() {
		return bVersteckt;
	}

}
