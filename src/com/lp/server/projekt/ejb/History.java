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
package com.lp.server.projekt.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;





//@NamedQueries( { @NamedQuery(name = "HistoryfindByProjektIid", query = "SELECT OBJECT (o) FROM History o WHERE o.projektIId=?1") })
@Entity
@Table(name = "PROJ_HISTORY")
public class History implements Serializable {
	public History() {
		// TODO Auto-generated constructor stub
	}

	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "T_BELEGDATUM")
	private Timestamp tBelegdatum;

	@Column(name = "X_TEXT")
	private String xText;

	@Column(name = "O_ATTACHMENTS")
	private byte[] oAttachments;

	@Column(name = "C_ATTACHMENTSTYPE")
	private String cAttachmentstype;

	@Column(name = "PERSONAL_I_ID")
	private Integer personalIId;

	@Column(name = "HISTORYART_I_ID")
	private Integer historyartIId;
	
	public Integer getHistoryartIId() {
		return historyartIId;
	}

	public void setHistoryartIId(Integer historyartIId) {
		this.historyartIId = historyartIId;
	}

	public String getCTitel() {
		return cTitel;
	}

	public void setCTitel(String cTitel) {
		this.cTitel = cTitel;
	}

	@Column(name = "C_TITEL")
	private String cTitel;
	
//	@Column(name = "PROJEKT_I_ID")
//	private Integer projektIId;
	
	@ManyToOne
	private Projekt PROJEKT;


	private static final long serialVersionUID = 1L;

	public History(Integer iId,
			Integer personalIId, 
			Integer projektIId,
			Timestamp tBelegdatum,
			String xText) {
		setPersonalIId(personalIId);
		setTBelegdatum(tBelegdatum);
		setXText(xText);
//		setProjektIId(projektIId);
		setIId(iId);
	}
	
	public History(Integer iId,
			Integer personalIId, 
			Timestamp tBelegdatum,
			String xText,
			Projekt projekt) {
		setPersonalIId(personalIId);
		setTBelegdatum(tBelegdatum);
		setXText(xText);
		setProjekt(projekt);
		setIId(iId);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Timestamp getTBelegdatum() {
		return this.tBelegdatum;
	}

	public void setTBelegdatum(Timestamp tBelegdatum) {
		this.tBelegdatum = tBelegdatum;
	}

	public String getXText() {
		return this.xText;
	}

	public void setXText(String xText) {
		this.xText = xText;
	}

	public byte[] getOAttachments() {
		return this.oAttachments;
	}

	public void setOAttachments(byte[] oAttachments) {
		this.oAttachments = oAttachments;
	}

	public String getCAttachmentstype() {
		return this.cAttachmentstype;
	}

	public void setCAttachmentstype(String cAttachmentstype) {
		this.cAttachmentstype = cAttachmentstype;
	}

	public Integer getPersonalIId() {
		return this.personalIId;
	}

	public void setPersonalIId(Integer personalIId) {
		this.personalIId = personalIId;
	}
/*
	public Integer getProjektIId() {
		return this.projektIId;
	}

	public void setProjektIId(Integer projektIId) {
		this.projektIId = projektIId;
	}
*/	
	public Projekt getProjekt() {
		return this.PROJEKT;
	}

	public void setProjekt(Projekt projekt) {
		this.PROJEKT = projekt;
	}


}
