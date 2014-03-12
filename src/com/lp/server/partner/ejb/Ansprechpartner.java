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
package com.lp.server.partner.ejb;

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
		@NamedQuery(name = "AnsprechpartnerfindByPartnerFunktionGueltigAb", query = "SELECT OBJECT(c) FROM Ansprechpartner c WHERE c.partnerIId = ?1 AND c.partnerIIdAnsprechpartner = ?2 AND c.ansprechpartnerfunktionIId= ?3 AND c.tGueltigab= ?4"),
		@NamedQuery(name = "AnsprechpartnerfindByPartnerIIdAnsprechpartner", query = "SELECT OBJECT(c) FROM Ansprechpartner c WHERE c.partnerIIdAnsprechpartner = ?1"),
		@NamedQuery(name = "AnsprechpartnerfindByPartnerIId", query = "SELECT OBJECT(c) FROM Ansprechpartner c WHERE c.partnerIId = ?1 ORDER BY c.iSort"),
		@NamedQuery(name = "AnsprechpartnerejbSelectMaxISort", query = "SELECT MAX (o.iSort) FROM Ansprechpartner o WHERE o.partnerIId = ?1"),
		@NamedQuery(name = "AnsprechpartnerfindByPartnerIIdAndPartnerIIdAnsprechpartner", query = "SELECT OBJECT(c) FROM Ansprechpartner c WHERE c.partnerIId = ?1 AND c.partnerIIdAnsprechpartner = ?2"),
		@NamedQuery(name = "AnsprechpartnerfindByPartnerIIdOrPartnerIIdAnsprechpartner", query = "SELECT OBJECT(C) FROM Ansprechpartner c WHERE c.partnerIId = ?1 OR c.partnerIIdAnsprechpartner = ?1") })
@Entity
@Table(name = "PART_ANSPRECHPARTNER")
public class Ansprechpartner implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "T_GUELTIGAB")
	private Date tGueltigab;

	@Column(name = "I_SORT")
	private Integer iSort;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "X_BEMERKUNG")
	private String xBemerkung;

	@Column(name = "B_VERSTECKT")
	private Short bVersteckt;

	@Column(name = "ANSPRECHPARTNERFUNKTION_I_ID")
	private Integer ansprechpartnerfunktionIId;

	@Column(name = "PARTNER_I_ID_ANSPRECHPARTNER")
	private Integer partnerIIdAnsprechpartner;

	@Column(name = "PARTNER_I_ID")
	private Integer partnerIId;
	
	@Column(name = "B_NEWSLETTER_EMPFAENGER")
	private Short bNewsletterEmpfaenger;


	public String getCFremdsystemnr() {
		return cFremdsystemnr;
	}

	public void setCFremdsystemnr(String fremdsystemnr) {
		cFremdsystemnr = fremdsystemnr;
	}

	@Column(name = "C_FREMDSYSTEMNR")
	private String cFremdsystemnr;
	


	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	
	@Column(name = "C_FAX")
	private String cFax;
	
	@Column(name = "C_TELEFON")
	private String cTelefon;
	
	@Column(name = "C_HANDY")
	private String cHandy;
	
	@Column(name = "C_DIREKTFAX")
	private String cDirektfax;
	
	@Column(name = "C_EMAIL")
	private String cEmail;
	
	
	public String getCFax() {
		return cFax;
	}

	public void setCFax(String cFax) {
		this.cFax = cFax;
	}

	public String getCTelefon() {
		return cTelefon;
	}

	public void setCTelefon(String cTelefon) {
		this.cTelefon = cTelefon;
	}

	public String getCDirektfax() {
		return cDirektfax;
	}

	public void setCDirektfax(String cDirektfax) {
		this.cDirektfax = cDirektfax;
	}


	public String getCEmail() {
		return cEmail;
	}

	public void setCEmail(String cEmail) {
		this.cEmail = cEmail;
	}

	public String getCHandy() {
		return cHandy;
	}

	public void setCHandy(String cHandy) {
		this.cHandy = cHandy;
	}
	
	private static final long serialVersionUID = 1L;

	public Ansprechpartner() {
		super();
	}

	public Ansprechpartner(Integer iId, Integer partnerIId,
			Integer partnerIIdAnsprechpartner,
			Integer ansprechpartnerfunktionIId, Date tGueltigab,
			Integer iSort, Integer personalIIdAendern, Short bVersteckt, Short bNewsletterEmpfaenger) {
		setIId(iId);
	    setPartnerIIdAnsprechpartner(partnerIIdAnsprechpartner);
	    setISort(iSort);
	    setPersonalIIdAendern(personalIIdAendern);

	    //die ts anlegen, aendern nur am server
	    setTAendern(new Timestamp(System.currentTimeMillis()));
	    setAnsprechpartnerfunktionIId(ansprechpartnerfunktionIId);
	    setPartnerIId(partnerIId);
	    setTGueltigab(tGueltigab);
	    setBVersteckt(bVersteckt);
	    setbNewsletterEmpfaenger(bNewsletterEmpfaenger);
	}

	public void setbNewsletterEmpfaenger(Short bNewsletterEmpfaenger) {
		this.bNewsletterEmpfaenger = bNewsletterEmpfaenger;
	}
	
	public Short getbNewsletterEmpfaenger() {
		return bNewsletterEmpfaenger;
	}
	
	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Date getTGueltigab() {
		return this.tGueltigab;
	}

	public void setTGueltigab(Date tGueltigab) {
		this.tGueltigab = tGueltigab;
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

	public String getXBemerkung() {
		return this.xBemerkung;
	}

	public void setXBemerkung(String xBemerkung) {
		this.xBemerkung = xBemerkung;
	}

	public Short getBVersteckt() {
		return this.bVersteckt;
	}

	public void setBVersteckt(Short bVersteckt) {
		this.bVersteckt = bVersteckt;
	}

	public Integer getAnsprechpartnerfunktionIId() {
		return this.ansprechpartnerfunktionIId;
	}

	public void setAnsprechpartnerfunktionIId(Integer ansprechpartnerfunktion) {
		this.ansprechpartnerfunktionIId = ansprechpartnerfunktion;
	}

	public Integer getPartnerIIdAnsprechpartner() {
		return this.partnerIIdAnsprechpartner;
	}

	public void setPartnerIIdAnsprechpartner(Integer partnerIIdAnsprechpartner) {
		this.partnerIIdAnsprechpartner = partnerIIdAnsprechpartner;
	}

	public Integer getPartnerIId() {
		return this.partnerIId;
	}

	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId;
	}


	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

}
