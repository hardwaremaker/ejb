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
		@NamedQuery(name = "SpediteurfindAllByMandant", query = "SELECT OBJECT (o) FROM Spediteur o WHERE o.mandantCNr = ?1"),
		@NamedQuery(name = "SpediteurfindByMandantSpediteurname", query = "SELECT OBJECT (o) FROM Spediteur o WHERE o.mandantCNr = ?1 AND o.cNamedesspediteurs=?2") })
@Entity
@Table(name = "LP_SPEDITEUR")
public class Spediteur implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_NAMEDESSPEDITEURS")
	private String cNamedesspediteurs;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "B_VERSTECKT")
	private Short bVersteckt;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "C_EMAIL")
	private String cEmail;
	
	@Column(name = "ANSPRECHPARTNER_I_ID")
	private Integer ansprechpartnerIId;
	
	@Column(name = "PARTNER_I_ID")
	private Integer partnerIId;

	@Column(name = "C_VERKEHRSZWEIG")
	private String cVerkehrszweig;
	

	public Integer getAnsprechpartnerIId() {
		return ansprechpartnerIId;
	}

	public void setAnsprechpartnerIId(Integer ansprechpartnerIId) {
		this.ansprechpartnerIId = ansprechpartnerIId;
	}

	public Integer getPartnerIId() {
		return partnerIId;
	}

	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId;
	}

	public String getCEmail() {
		return cEmail;
	}

	public void setCEmail(String cEmail) {
		this.cEmail = cEmail;
	}

	private static final long serialVersionUID = 1L;

	public Spediteur() {
		super();
	}

	public Spediteur(Integer idSpediteurO, String mandantCNr,
			String namedesspediteurs, Integer personal, Short versteckt) {
		setIId(idSpediteurO);
		setMandantCNr(mandantCNr);
		setCNamedesspediteurs(namedesspediteurs);
		setPersonalIIdAendern(personal);
		setBVersteckt(versteckt);
		// DB NOT NULL Felder mit Default Werten
		setTAendern(new Timestamp(System.currentTimeMillis()));
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCNamedesspediteurs() {
		return this.cNamedesspediteurs;
	}

	public void setCNamedesspediteurs(String cNamedesspediteurs) {
		this.cNamedesspediteurs = cNamedesspediteurs;
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
	
	public void setCVerkehrszweig(String cIntrastatVerkehrszweig) {
		this.cVerkehrszweig = cIntrastatVerkehrszweig;
	}
	
	public String getCVerkehrszweig() {
		return this.cVerkehrszweig;
	}
}
