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
package com.lp.server.projekt.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
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
	}

	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "T_BELEGDATUM")
	private Timestamp tBelegdatum;

	@Column(name = "X_TEXT")
	private String xText;
	
	@Column(name = "N_DAUER_GEPLANT")
	private BigDecimal nDauerGeplant;

	public BigDecimal getNDauerGeplant() {
		return nDauerGeplant;
	}

	public void setNDauerGeplant(BigDecimal nDauerGeplant) {
		this.nDauerGeplant = nDauerGeplant;
	}

	@Column(name = "O_ATTACHMENTS")
	private byte[] oAttachments;

	@Column(name = "C_ATTACHMENTSTYPE")
	private String cAttachmentstype;

	@Column(name = "PERSONAL_I_ID")
	private Integer personalIId;

	@Column(name = "HISTORYART_I_ID")
	private Integer historyartIId;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "PERSONAL_I_ID_WIRDDURCHGEFUEHRT")
	private Integer personalIIdWirddurchgefuehrt;
	
	public Integer getPersonalIIdWirddurchgefuehrt() {
		return personalIIdWirddurchgefuehrt;
	}

	public void setPersonalIIdWirddurchgefuehrt(Integer personalIIdWirddurchgefuehrt) {
		this.personalIIdWirddurchgefuehrt = personalIIdWirddurchgefuehrt;
	}

	public Double getFErledigungsgrad() {
		return fErledigungsgrad;
	}

	public void setFErledigungsgrad(Double fErledigungsgrad) {
		this.fErledigungsgrad = fErledigungsgrad;
	}

	@Column(name = "F_ERLEDIGUNGSGRAD")
	private Double fErledigungsgrad;
	
	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

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

	// @Column(name = "PROJEKT_I_ID")
	// private Integer projektIId;

	@ManyToOne
	private Projekt PROJEKT;

	@Column(name = "B_HTML")
	private Short bHtml;

	private static final long serialVersionUID = 1L;

	public History(Integer iId, Integer personalIId, Timestamp tBelegdatum,
			String xText, Projekt projekt, Short bHtml,
			Integer personalIIdAnlegen, Timestamp tAnlegen,
			Integer personalIIdAendern, Timestamp tAendern,  Double fErledigungsgrad) {
		setPersonalIId(personalIId);
		setTBelegdatum(tBelegdatum);
		setXText(xText);
		setProjekt(projekt);
		setIId(iId);
		setBHtml(bHtml);
		setPersonalIIdAendern(personalIIdAendern);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setTAendern(tAendern);
		setTAnlegen(tAnlegen);
		setFErledigungsgrad(fErledigungsgrad);
		
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
	 * public Integer getProjektIId() { return this.projektIId; }
	 * 
	 * public void setProjektIId(Integer projektIId) { this.projektIId =
	 * projektIId; }
	 */
	public Projekt getProjekt() {
		return this.PROJEKT;
	}

	public void setProjekt(Projekt projekt) {
		this.PROJEKT = projekt;
	}

	public Short getBHtml() {
		return bHtml;
	}

	public void setBHtml(Short bHtml) {
		this.bHtml = bHtml;
	}
}
