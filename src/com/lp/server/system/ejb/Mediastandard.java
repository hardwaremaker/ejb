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
	@NamedQuery(name = "MediastandardfindByCNrDatenformatCNrMandantCNrLocaleCNr",
			query = "SELECT OBJECT (o) FROM Mediastandard o WHERE o.cNr=?1 AND o.datenformatCNr=?2 AND o.mandantCNr = ?3 AND o.localeCNr = ?4"),
	@NamedQuery(name = "MediastandardfindByDatenformatCNrMandantCNr", 
			query = "SELECT OBJECT (o) FROM Mediastandard o WHERE o.datenformatCNr=?1 AND o.mandantCNr = ?2"),
	@NamedQuery(name = MediastandardQuery.ByCnr,
			query = "SELECT OBJECT (o) FROM Mediastandard o WHERE o.cNr=:cnr AND o.mandantCNr=:mandant AND o.localeCNr=:locale")
	})
@Entity
@Table(name = "LP_MEDIASTANDARD")
public class Mediastandard implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_NR")
	private String cNr;

	@Column(name = "O_MEDIA")
	private byte[] oMedia;

	@Column(name = "C_DATEINAME")
	private String cDateiname;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "B_VERSTECKT")
	private Short bVersteckt;

	@Column(name = "DATENFORMAT_C_NR")
	private String datenformatCNr;

	@Column(name = "LOCALE_C_NR")
	private String localeCNr;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	private static final long serialVersionUID = 1L;

	public Mediastandard() {
		super();
	}

	public Mediastandard(Integer id, 
			String nr, 
			byte[] mediaImage,
			String datenformatCNr, 
			String cDateiname,
			Integer personalIIdAnlegen,
			Timestamp tAnlegen,
			Integer personalIIdAendern,
			Timestamp tAendern, 
			String mandantCNr, 
			String localeCNr,
			Short versteckt) {
		
		setIId(id);
		setCNr(nr);
		setOMedia(mediaImage);
		setDatenformatCNr(datenformatCNr);
		setCDateiname(cDateiname);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setTAnlegen(tAnlegen);
		setPersonalIIdAendern(personalIIdAendern);
		setTAendern(tAendern);
		setMandantCNr(mandantCNr);
		setLocaleCNr(localeCNr);
		setBVersteckt(versteckt);
		
		
		
	}

	public Mediastandard(Integer id, String nr, Byte mediaImage,
			String datenformatCNr, Integer personalIIdAnlegen,
			Integer personalIIdAendern, String mandantCNr, String localeCNr,
			Short versteckt,Timestamp tAendern, Timestamp tAnlegen,String cDateiname) {
		this(id, nr, new byte[]{mediaImage}, datenformatCNr, cDateiname,personalIIdAnlegen, tAnlegen,
				personalIIdAendern,tAendern, mandantCNr, localeCNr, versteckt);
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

	public Short getBVersteckt() {
		return this.bVersteckt;
	}

	public void setBVersteckt(Short bVersteckt) {
		this.bVersteckt = bVersteckt;
	}

	public String getDatenformatCNr() {
		return this.datenformatCNr;
	}

	public void setDatenformatCNr(String datenformat) {
		this.datenformatCNr = datenformat;
	}

	public String getLocaleCNr() {
		return this.localeCNr;
	}

	public void setLocaleCNr(String locale) {
		this.localeCNr = locale;
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
