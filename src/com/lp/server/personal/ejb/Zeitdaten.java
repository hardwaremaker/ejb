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

@NamedQueries( {
		@NamedQuery(name = "ZeitdatenfindZeitdatenEinesTagesUndEinerPerson", query = "SELECT OBJECT(o) FROM Zeitdaten o WHERE o.personalIId = ?1 AND o.tZeit >= ?2 AND o.tZeit < ?3 ORDER BY o.tZeit ASC"),
		@NamedQuery(name = "ZeitdatenfindZeitdatenEinesBelegs", query = "SELECT OBJECT(o) FROM Zeitdaten o WHERE o.cBelegartnr = ?1 AND o.iBelegartid = ?2 ORDER BY o.tZeit ASC"),
		@NamedQuery(name = "ZeitdatenfindByPersonalIIdTZeit", query = "SELECT OBJECT(o) FROM Zeitdaten o WHERE o.personalIId = ?1 AND o.tZeit = ?2"),
		@NamedQuery(name = "ZeitdatenfindByPersonalIIdTaetigkeitIIdTVonTBis", query = "SELECT OBJECT(o) FROM Zeitdaten o WHERE o.personalIId = ?1 AND o.taetigkeitIId = ?2 AND o.tZeit >= ?3 AND o.tZeit < ?4"),
		@NamedQuery(name = "ZeitdatenejbSelectAllBelegeByPersonalIIdTVonTBis", query = "SELECT DISTINCT o.iBelegartid FROM Zeitdaten o WHERE o.cBelegartnr = ?1 AND o.personalIId = ?2 AND o.tZeit >= ?3 AND o.tZeit < ?4"),
		@NamedQuery(name = "ZeitdatenfindZeitdatenEinesBelegsTVonTBis", query = "SELECT OBJECT(o) FROM Zeitdaten o WHERE o.cBelegartnr = ?1 AND o.iBelegartid = ?2 AND o.tZeit >=?3 AND o.tZeit <?4  ORDER BY o.tZeit ASC"),
		@NamedQuery(name = "ZeitdatenfindZeitdatenEinesBelegsTVonTBisPersonalIId", query = "SELECT OBJECT(o) FROM Zeitdaten o WHERE o.cBelegartnr = ?1 AND o.iBelegartid = ?2 AND o.tZeit >=?3 AND o.tZeit <?4 AND o.personalIId = ?5 ORDER BY o.tZeit ASC"),
		@NamedQuery(name = "ZeitdatenfindZeitdatenEinerBelegposition", query = "SELECT OBJECT(o) FROM Zeitdaten o WHERE o.cBelegartnr = ?1 AND o.iBelegartid = ?2 AND o.iBelegartpositionid = ?3 ORDER BY o.tZeit ASC"),
		@NamedQuery(name = "ZeitdatenfindZeitdatenEinerBelegpositionTVonTBis", query = "SELECT OBJECT(o) FROM Zeitdaten o WHERE o.cBelegartnr = ?1 AND o.iBelegartid = ?2 AND o.tZeit >=?3 AND o.tZeit <?4 AND o.iBelegartpositionid =?5  ORDER BY o.tZeit ASC"),
		@NamedQuery(name = "ZeitdatenfindZeitdatenEinerBelegpositionTVonTBisPersonalIId", query = "SELECT OBJECT(o) FROM Zeitdaten o WHERE o.cBelegartnr = ?1 AND o.iBelegartid = ?2 AND o.tZeit >=?3 AND o.tZeit <?4 AND o.personalIId = ?5 AND o.iBelegartpositionid = ?6 ORDER BY o.tZeit ASC"),
		@NamedQuery(name = "ZeitdatenfindZeitdatenEinesBelegesUndEinerPerson", query = "SELECT OBJECT(o) FROM Zeitdaten o WHERE o.cBelegartnr = ?1 AND o.iBelegartid = ?2 AND o.personalIId=?3 ORDER BY o.tZeit ASC"),
		@NamedQuery(name = "ZeitdatenfindZeitdatenEinerBelegpositionUndEinerPerson", query = "SELECT OBJECT(o) FROM Zeitdaten o WHERE o.cBelegartnr = ?1 AND o.iBelegartid = ?2 AND o.iBelegartpositionid = ?3 AND o.personalIId = ?4 ORDER BY o.tZeit ASC"),
		@NamedQuery(name = "ZeitdatenfindZeitdatenEinesBelegsTBis", query = "SELECT OBJECT(o) FROM Zeitdaten o WHERE o.cBelegartnr = ?1 AND o.iBelegartid = ?2 AND o.tZeit <?3  ORDER BY o.tZeit ASC") })
@Entity
@Table(name = "PERS_ZEITDATEN")
public class Zeitdaten implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "T_ZEIT")
	private Timestamp tZeit;

	@Column(name = "B_TAETIGKEITGEAENDERT")
	private Short bTaetigkeitgeaendert;

	@Column(name = "C_BELEGARTNR")
	private String cBelegartnr;

	@Column(name = "I_BELEGARTID")
	private Integer iBelegartid;

	@Column(name = "I_BELEGARTPOSITIONID")
	private Integer iBelegartpositionid;

	@Column(name = "C_BEMERKUNGZUBELEGART")
	private String cBemerkungzubelegart;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "B_AUTOMATIKBUCHUNG")
	private Short bAutomatikbuchung;

	@Column(name = "X_KOMMENTAR")
	private String xKommentar;

	@Column(name = "C_WOWURDEGEBUCHT")
	private String cWowurdegebucht;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "PERSONAL_I_ID")
	private Integer personalIId;

	@Column(name = "TAETIGKEIT_I_ID")
	private Integer taetigkeitIId;

	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;

	private static final long serialVersionUID = 1L;

	public Zeitdaten() {
		super();
	}

	public Zeitdaten(Integer id,
			Integer personalIId,
			Timestamp zeit,
			Short taetigkeitgeaendert,
			Integer personalIIdAnlegen2, Integer personalIIdAendern2,
			 Short automatikbuchung) {
		setIId(id);
		setPersonalIId(personalIId);
		setTZeit(zeit);
		setPersonalIIdAnlegen(personalIIdAnlegen2);
		setPersonalIIdAendern(personalIIdAendern2);
		setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));
		setTAnlegen(new java.sql.Timestamp(System.currentTimeMillis()));
		setBTaetigkeitgeaendert(taetigkeitgeaendert);
		setBAutomatikbuchung(automatikbuchung);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Timestamp getTZeit() {
		return this.tZeit;
	}

	public void setTZeit(Timestamp tZeit) {
		this.tZeit = tZeit;
	}

	public Short getBTaetigkeitgeaendert() {
		return this.bTaetigkeitgeaendert;
	}

	public void setBTaetigkeitgeaendert(Short bTaetigkeitgeaendert) {
		this.bTaetigkeitgeaendert = bTaetigkeitgeaendert;
	}

	public String getCBelegartnr() {
		return this.cBelegartnr;
	}

	public void setCBelegartnr(String cBelegartnr) {
		this.cBelegartnr = cBelegartnr;
	}

	public Integer getIBelegartid() {
		return this.iBelegartid;
	}

	public void setIBelegartid(Integer iBelegartid) {
		this.iBelegartid = iBelegartid;
	}

	public Integer getIBelegartpositionid() {
		return this.iBelegartpositionid;
	}

	public void setIBelegartpositionid(Integer iBelegartpositionid) {
		this.iBelegartpositionid = iBelegartpositionid;
	}

	public String getCBemerkungzubelegart() {
		return this.cBemerkungzubelegart;
	}

	public void setCBemerkungzubelegart(String cBemerkungzubelegart) {
		this.cBemerkungzubelegart = cBemerkungzubelegart;
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

	public Short getBAutomatikbuchung() {
		return this.bAutomatikbuchung;
	}

	public void setBAutomatikbuchung(Short bAutomatikbuchung) {
		this.bAutomatikbuchung = bAutomatikbuchung;
	}

	public String getXKommentar() {
		return this.xKommentar;
	}

	public void setXKommentar(String xKommentar) {
		this.xKommentar = xKommentar;
	}

	public String getCWowurdegebucht() {
		return this.cWowurdegebucht;
	}

	public void setCWowurdegebucht(String cWowurdegebucht) {
		this.cWowurdegebucht = cWowurdegebucht;
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

	public Integer getPersonalIId() {
		return this.personalIId;
	}

	public void setPersonalIId(Integer personalIId) {
		this.personalIId = personalIId;
	}

	public Integer getTaetigkeitIId() {
		return this.taetigkeitIId;
	}

	public void setTaetigkeitIId(Integer taetigkeitIId) {
		this.taetigkeitIId = taetigkeitIId;
	}

	public Integer getArtikelIId() {
		return this.artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

}
