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
		@NamedQuery(name = "KurzbrieffindByPartnerIId", query = "SELECT OBJECT(C) FROM Kurzbrief c WHERE c.partnerIId = ?1"),
		@NamedQuery(name = "KurzbrieffindByAnsprechpartnerIId", query = "SELECT OBJECT(C) FROM Kurzbrief c WHERE c.ansprechpartnerIId = ?1") })
@Entity
@Table(name = "PART_KURZBRIEF")
public class Kurzbrief implements Serializable {
	private static final long serialVersionUID = 2981524321590962943L;

	public Kurzbrief() {
	}

	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_BETREFF")
	private String cBetreff;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "X_TEXT")
	private String xText;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "BELEGART_C_NR")
	private String belegartCNr;

	@Column(name = "ANSPRECHPARTNER_I_ID")
	private Integer ansprechpartnerIId;

	@Column(name = "PARTNER_I_ID")
	private Integer partnerIId;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "B_HTML")
	private Short bHtml;

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public Kurzbrief(Integer iId, Integer partnerIId,
			Integer ansprechpartnerIId, Integer personalIIdAnlegen,
			Integer personalIIdAendern, String belegartCNr, Short b_html,
			String mandantCNr) {
		setIId(iId);
		setPartnerIId(partnerIId);
		setAnsprechpartnerIId(ansprechpartnerIId);
		Timestamp t = new Timestamp(System.currentTimeMillis());
		setTAnlegen(t);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setTAendern(t);
		setPersonalIIdAendern(personalIIdAendern);
		setBelegartCNr(belegartCNr);
		setBHtml(b_html);
		setMandantCNr(mandantCNr);
	}

	public Kurzbrief(Integer iId, Integer partnerIId,
			Integer personalIIdAnlegen, Integer personalIIdAendern,
			String belegartCNr, Short b_html, String mandantCNr) {
		setIId(iId);
		setPartnerIId(partnerIId);
		Timestamp t = new Timestamp(System.currentTimeMillis());
		setTAnlegen(t);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setTAendern(t);
		setPersonalIIdAendern(personalIIdAendern);
		setBelegartCNr(belegartCNr);
		setBHtml(b_html);
		setMandantCNr(mandantCNr);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCBetreff() {
		return this.cBetreff;
	}

	public void setCBetreff(String cBetreff) {
		this.cBetreff = cBetreff;
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

	public String getXText() {
		return this.xText;
	}

	public void setXText(String xText) {
		this.xText = xText;
	}

	public String getBelegartCNr() {
		return this.belegartCNr;
	}

	public void setBelegartCNr(String belegart) {
		this.belegartCNr = belegart;
	}

	public Integer getAnsprechpartnerIId() {
		return this.ansprechpartnerIId;
	}

	public void setAnsprechpartnerIId(Integer ansprechpartner) {
		this.ansprechpartnerIId = ansprechpartner;
	}

	public Integer getPartnerIId() {
		return this.partnerIId;
	}

	public void setPartnerIId(Integer partner) {
		this.partnerIId = partner;
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

	public Short getBHtml() {
		return bHtml;
	}

	public void setBHtml(Short b_html) {
		this.bHtml = b_html;
	}
}
