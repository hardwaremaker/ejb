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
		@NamedQuery(name = "MahnungfindByMahnlaufIId", query = "SELECT OBJECT(o) FROM Mahnung o WHERE o.mahnlaufIId=?1"),
		@NamedQuery(name = "MahnungfindByRechnungIId", query = "SELECT OBJECT(o) FROM Mahnung o WHERE o.rechnungIId=?1"),
		@NamedQuery(name = "MahnungfindByRechnungMahnstufe", query = "SELECT OBJECT(o) FROM Mahnung o WHERE o.rechnungIId=?1 AND  o.mahnstufeIId=?2") })
@Entity
@Table(name = "FB_MAHNUNG")
public class Mahnung implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "T_MAHNDATUM")
	private Date tMahndatum;
	
	@Column(name = "T_LETZTESMAHNDATUM")
	private Date tLetztesmahndatum;

	@Column(name = "T_GEDRUCKT")
	private Timestamp tGedruckt;

	

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "MAHNLAUF_I_ID")
	private Integer mahnlaufIId;

	@Column(name = "MAHNSTUFE_I_ID_LETZTEMAHNSTUFE")
	private Integer mahnstufeIIdLetztmahnstufe;

	@Column(name = "MAHNSTUFE_I_ID")
	private Integer mahnstufeIId;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "PERSONAL_I_ID_GEDRUCKT")
	private Integer personalIIdGedruckt;

	@Column(name = "RECHNUNG_I_ID")
	private Integer rechnungIId;

	private static final long serialVersionUID = 1L;

	public Mahnung() {
		super();
	}

	public Mahnung(Integer id,
			String mandantCNr,
			Integer mahnlaufIId,
			Integer mahnstufeIId,
			Integer rechnungIId, 
			Date mahndatum,
			Integer personalIIdAnlegen,
			Integer personalIIdAendern
			) {
		setIId(id);
		setMahnlaufIId(mahnlaufIId);
		setMahnstufeIId(mahnstufeIId);
		setRechnungIId(rechnungIId);
		setTMahndatum(mahndatum);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setPersonalIIdAendern(personalIIdAendern);
		// Setzen der NOT NULL Felder
		Timestamp now = new Timestamp(System.currentTimeMillis());
		this.setTAnlegen(now);
		this.setTAendern(now);
		setMandantCNr(mandantCNr);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Date getTMahndatum() {
		return this.tMahndatum;
	}

	public void setTMahndatum(Date tMahndatum) {
		this.tMahndatum = tMahndatum;
	}

	public Integer getMahnstufeIId() {
		return this.mahnstufeIId;
	}

	public void setMahnstufeIId(Integer mahnstufeIId) {
		this.mahnstufeIId = mahnstufeIId;
	}

	public Date getTLetztesmahndatum() {
		return this.tLetztesmahndatum;
	}

	public void setTLetztesmahndatum(Date tLetztesmahndatum) {
		this.tLetztesmahndatum = tLetztesmahndatum;
	}

	public Integer getMahnstufeIIdLetztemahnstufe() {
		return this.mahnstufeIIdLetztmahnstufe;
	}

	public void setMahnstufeIIdLetztemahnstufe(
			Integer mahnstufeIIdLetztmahnstufe) {
		this.mahnstufeIIdLetztmahnstufe = mahnstufeIIdLetztmahnstufe;
	}

	public Timestamp getTGedruckt() {
		return this.tGedruckt;
	}

	public void setTGedruckt(Timestamp tGedruckt) {
		this.tGedruckt = tGedruckt;
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

	public Integer getMahnlaufIId() {
		return this.mahnlaufIId;
	}

	public void setMahnlaufIId(Integer mahnlaufIId) {
		this.mahnlaufIId = mahnlaufIId;
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

	public Integer getPersonalIIdGedruckt() {
		return this.personalIIdGedruckt;
	}

	public void setPersonalIIdGedruckt(Integer personalIIdGedruckt) {
		this.personalIIdGedruckt = personalIIdGedruckt;
	}

	public Integer getRechnungIId() {
		return this.rechnungIId;
	}

	public void setRechnungIId(Integer rechnungIId) {
		this.rechnungIId = rechnungIId;
	}

}
