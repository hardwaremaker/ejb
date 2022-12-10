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
import java.sql.Time;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "SonderzeitenfindByPersonalIIdTDatum", query = "SELECT OBJECT(C) FROM Sonderzeiten c WHERE c.personalIId = ?1 AND c.tDatum = ?2"),
		@NamedQuery(name = "SonderzeitenfindByPersonalIIdTDatumBTag", query = "SELECT OBJECT(C) FROM Sonderzeiten c WHERE c.personalIId = ?1 AND c.tDatum = ?2 AND c.bTag=?3"),
		@NamedQuery(name = "SonderzeitenfindStundenweiseTaetigkeitenGeplant", query = "SELECT OBJECT(C) FROM Sonderzeiten c WHERE c.personalIId = ?1 AND c.taetigkeitIId = ?2 AND c.tDatum >= ?3 AND c.bTag = 0 AND c.bHalbtag = 0"),
		@NamedQuery(name = "SonderzeitenejbSelectGanztaegigeTaetigkeitenAlt", query = "SELECT SUM (o.bTag) FROM Sonderzeiten o WHERE o.personalIId = ?1 AND o.taetigkeitIId = ?2 AND o.tDatum >= ?3 AND o.tDatum < ?4 AND o.bTag = 1"),
		@NamedQuery(name = "SonderzeitenejbSelectGanztaegigeTaetigkeitenAktuell", query = "SELECT SUM (o.bTag) FROM Sonderzeiten o WHERE o.personalIId = ?1 AND o.taetigkeitIId = ?2 AND o.tDatum >= ?3 AND o.tDatum <= ?4 AND o.bTag = 1"),
		@NamedQuery(name = "SonderzeitenejbSelectGanztaegigeTaetigkeitenGeplant", query = "SELECT SUM (o.bTag) FROM Sonderzeiten o WHERE o.personalIId = ?1 AND o.taetigkeitIId = ?2 AND o.tDatum >= ?3 AND o.bTag = 1"),
		@NamedQuery(name = "SonderzeitenfindStundenweiseTaetigkeitenAktuell", query = "SELECT OBJECT(C) FROM Sonderzeiten c WHERE c.personalIId = ?1 AND c.taetigkeitIId = ?2 AND c.tDatum >= ?3 AND c.tDatum <= ?4 AND c.bTag = 0 AND c.bHalbtag = 0"),
		@NamedQuery(name = "SonderzeitenfindAlleAutomatikbuchungenEinesZeitraums", query = "SELECT OBJECT(C) FROM Sonderzeiten c WHERE c.personalIId = ?1 AND c.tDatum >= ?2 AND c.tDatum <= ?3 AND c.bAutomatik = 1"),
		@NamedQuery(name = "SonderzeitenfindStundenweiseTaetigkeitenAlt", query = "SELECT OBJECT(C) FROM Sonderzeiten c WHERE c.personalIId = ?1 AND c.taetigkeitIId = ?2 AND c.tDatum >= ?3 AND c.tDatum < ?4 AND c.bTag = 0 AND c.bHalbtag = 0"),
		@NamedQuery(name = "SonderzeitenfindByPersonalIIdTDatumTaetigkeitIId", query = "SELECT OBJECT(C) FROM Sonderzeiten c WHERE c.personalIId = ?1 AND c.tDatum = ?2 AND c.taetigkeitIId = ?3"),
		@NamedQuery(name = "SonderzeitenejbSelectHalbtaegigeTaetigkeitenAlt", query = "SELECT SUM (o.bHalbtag) FROM Sonderzeiten o WHERE o.personalIId = ?1 AND o.taetigkeitIId = ?2 AND o.tDatum >= ?3 AND o.tDatum < ?4 AND o.bHalbtag = 1"),
		@NamedQuery(name = "SonderzeitenejbSelectHalbtaegigeTaetigkeitenAktuell", query = "SELECT SUM (o.bHalbtag) FROM Sonderzeiten o WHERE o.personalIId = ?1 AND o.taetigkeitIId = ?2 AND o.tDatum >= ?3 AND o.tDatum <= ?4 AND o.bHalbtag = 1"),
		@NamedQuery(name = "SonderzeitenejbSelectHalbtaegigeTaetigkeitenGeplant", query = "SELECT SUM (o.bHalbtag) FROM Sonderzeiten o WHERE o.personalIId = ?1 AND o.taetigkeitIId = ?2 AND o.tDatum >= ?3 AND o.bHalbtag = 1"),
		@NamedQuery(name = "SonderzeitenfindTageweiseUndHalbtageweiseTaetigkeitenGeplant", query = "SELECT OBJECT(C) FROM Sonderzeiten c WHERE c.personalIId = ?1 AND c.taetigkeitIId = ?2 AND c.tDatum >= ?3 AND ( c.bTag = 1 OR c.bHalbtag = 1)"),
		@NamedQuery(name = "SonderzeitenfindTageweiseUndHalbtageweiseTaetigkeitenAktuell", query = "SELECT OBJECT(C) FROM Sonderzeiten c WHERE c.personalIId = ?1 AND c.taetigkeitIId = ?2 AND c.tDatum >= ?3 AND c.tDatum <= ?4 AND ( c.bTag = 1 OR c.bHalbtag = 1)"),
		@NamedQuery(name = "SonderzeitenfindTageweiseUndHalbtageweiseTaetigkeitenAlt", query = "SELECT OBJECT(C) FROM Sonderzeiten c WHERE c.personalIId = ?1 AND c.taetigkeitIId = ?2 AND c.tDatum >= ?3 AND c.tDatum < ?4 AND ( c.bTag = 1 OR c.bHalbtag = 1)"),
		@NamedQuery(name = "SonderzeitenfindByPersonalIIdTVonTBisTaetigkeitIId", query = "SELECT OBJECT(C) FROM Sonderzeiten c WHERE c.personalIId = ?1 AND c.tDatum >= ?2 AND c.tDatum <= ?3 AND c.taetigkeitIId = ?4") })
@Entity
@Table(name = "PERS_SONDERZEITEN")
public class Sonderzeiten implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "T_DATUM")
	private Timestamp tDatum;

	@Column(name = "B_TAG")
	private Short bTag;

	
	@Column(name = "B_AUTOMATIK")
	private Short bAutomatik;

	public Short getBAutomatik() {
		return bAutomatik;
	}

	public void setBAutomatik(Short bAutomatik) {
		this.bAutomatik = bAutomatik;
	}

	@Column(name = "B_HALBTAG")
	private Short bHalbtag;

	@Column(name = "U_STUNDEN")
	private Time uStunden;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID")
	private Integer personalIId;

	@Column(name = "TAETIGKEIT_I_ID")
	private Integer taetigkeitIId;

	private static final long serialVersionUID = 1L;

	public Sonderzeiten() {
		super();
	}

	public Sonderzeiten(Integer id,
			Integer personalIId2, 
			Timestamp datum,
			Integer taetigkeitIId2, 
			Short tag, 
			Short halbtag,
			Integer personalIIdAendern2, Short bAutomatik) {
		setIId(id);
		setPersonalIId(personalIId2);
		setTDatum(datum);
		setTaetigkeitIId(taetigkeitIId2);
		setBTag(tag);
		setPersonalIIdAendern(personalIIdAendern2);
		setBHalbtag(halbtag);
		setTAendern(new Timestamp(System.currentTimeMillis()));
		setBAutomatik(bAutomatik);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Timestamp getTDatum() {
		return this.tDatum;
	}

	public void setTDatum(Timestamp tDatum) {
		this.tDatum = tDatum;
	}

	public Short getBTag() {
		return this.bTag;
	}

	public void setBTag(Short bTag) {
		this.bTag = bTag;
	}

	public Short getBHalbtag() {
		return this.bHalbtag;
	}

	public void setBHalbtag(Short bHalbtag) {
		this.bHalbtag = bHalbtag;
	}

	public Time getUStunden() {
		return this.uStunden;
	}

	public void setUStunden(Time uStunden) {
		this.uStunden = uStunden;
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

}
