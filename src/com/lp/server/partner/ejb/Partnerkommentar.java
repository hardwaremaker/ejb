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
package com.lp.server.partner.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "PartnerkommentarfindByPartnerIIdPartnerkommentarartIIdDatenformatCNrBKunde", query = "SELECT OBJECT (o) FROM Partnerkommentar o WHERE o.partnerIId=?1 AND o.partnerkommentarartIId=?2 AND o.datenformatCNr=?3  AND o.bKunde=?4"),
		@NamedQuery(name = "PartnerkommentarfindByPartnerIIdPartnerkommentarartIIdBKunde", query = "SELECT OBJECT (o) FROM Partnerkommentar o WHERE o.partnerIId=?1 AND o.partnerkommentarartIId=?2 AND o.bKunde=?3"),
		@NamedQuery(name = "PartnerkommentarfindByPartnerIIdBKunde", query = "SELECT OBJECT (o) FROM Partnerkommentar o WHERE o.partnerIId=?1 AND o.bKunde=?2"),
		@NamedQuery(name = "PartnerkommentarejbSelectNextReihungByBKunde", query = "SELECT MAX (o.iSort) FROM Partnerkommentar o WHERE o.partnerIId = ?1 AND o.bKunde=?2"),
		@NamedQuery(name = "PartnerkommentarfindByPartnerIIdIArtBKunde", query = "SELECT OBJECT (o) FROM Partnerkommentar o WHERE o.partnerIId=?1 AND o.iArt=?2 AND o.bKunde=?3") })
@Entity
@Table(name = "PART_PARTNERKOMMENTAR")
public class Partnerkommentar implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;
	@Column(name = "PARTNER_I_ID")
	private Integer partnerIId;

	public Integer getPartnerIId() {
		return partnerIId;
	}

	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId;
	}

	public Short getBKunde() {
		return bKunde;
	}

	public void setBKunde(Short bKunde) {
		this.bKunde = bKunde;
	}

	public Integer getPartnerkommentarartIId() {
		return partnerkommentarartIId;
	}

	public void setPartnerkommentarartIId(Integer partnerkommentarartIId) {
		this.partnerkommentarartIId = partnerkommentarartIId;
	}

	@Column(name = "B_KUNDE")
	private Short bKunde;

	@Column(name = "I_ART")
	private Integer iArt;

	@Column(name = "I_SORT")
	private Integer iSort;

	@Column(name = "DATENFORMAT_C_NR")
	private String datenformatCNr;

	@Column(name = "PARTNERKOMMENTARART_I_ID")
	private Integer partnerkommentarartIId;

	@Column(name = "X_KOMMENTAR")
	private String xKommentar;

	@Column(name = "O_MEDIA")
	private byte[] oMedia;

	@Column(name = "C_DATEINAME")
	private String cDateiname;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "T_FILEDATUM")
	private Timestamp tFiledatum;

	public Timestamp gettFiledatum() {
		return tFiledatum;
	}

	public void setTFiledatum(Timestamp tFiledatum) {
		this.tFiledatum = tFiledatum;
	}

	private static final long serialVersionUID = 1L;

	public Partnerkommentar() {
		super();
	}

	public Partnerkommentar(Integer id, Integer partnerIId,
			Integer partnerkommentarartIId, String datenformatCNr2,
			Short bKunde, Integer iArt, Integer iSort,
			Integer personalIIdAendern, Timestamp tAendern) {
		setIId(id);
		setPartnerIId(partnerIId);
		setPartnerkommentarartIId(partnerkommentarartIId);
		setBKunde(bKunde);
		setDatenformatCNr(datenformatCNr2);
		setIArt(iArt);
		setISort(iSort);
		setPersonalIIdAendern(personalIIdAendern);
		setTAendern(tAendern);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getIArt() {
		return this.iArt;
	}

	public void setIArt(Integer iArt) {
		this.iArt = iArt;
	}

	public String getDatenformatCNr() {
		return this.datenformatCNr;
	}

	public void setDatenformatCNr(String datenformatCNr) {
		this.datenformatCNr = datenformatCNr;
	}

	public Integer getISort() {
		return iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public String getXKommentar() {
		return this.xKommentar;
	}

	public void setXKommentar(String xKommentar) {
		this.xKommentar = xKommentar;
	}

	public byte[] getOMedia() {
		return this.oMedia;
	}

	public void setOMedia(byte[] oMedia) {
		this.oMedia = oMedia;
	}

	public String getCDateiname() {
		return this.cDateiname;
	}

	public void setCDateiname(String cDateiname) {
		this.cDateiname = cDateiname;
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
}
