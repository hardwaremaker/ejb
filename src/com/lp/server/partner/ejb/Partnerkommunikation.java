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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "PartnerkommunikationfindByPartnerIIdKommunikationsartCNrCBez", query = "SELECT OBJECT(c) FROM Partnerkommunikation c WHERE c.partnerIId = ?1 AND c.kommunikationsartCNr = ?2 AND c.cBez = ?3 AND c.cInhalt = ?4"),
		@NamedQuery(name = "PartnerkommunikationfindByPartnerIId", query = "SELECT OBJECT(c) FROM Partnerkommunikation c WHERE c.partnerIId = ?1"),
		@NamedQuery(name = "PartnerkommunikationfindByPartnerIIdKommunikationsartPAiIdKommArtMandant", query = "SELECT OBJECT(c) FROM Partnerkommunikation c WHERE c.partnerIId = ?1 AND c.kommunikationsartCNr = ?2 AND c.mandantCNr = ?3"),
		@NamedQuery(name = "PartnerkommunikationfindByPartnerIIdMandantCNr", query = "SELECT OBJECT(c) FROM Partnerkommunikation c WHERE c.partnerIId = ?1 AND c.mandantCNr = ?2") })
@Entity
@Table(name = "PART_PARTNERKOMMUNIKATION")
public class Partnerkommunikation implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_BEZ")
	private String cBez;

	@Column(name = "C_INHALT")
	private String cInhalt;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "KOMMUNIKATIONSART_C_NR")
	private String kommunikationsartCNr;

	@Column(name = "PARTNER_I_ID")
	private Integer partnerIId;



	private static final long serialVersionUID = 1L;

	public Partnerkommunikation() {
		super();
	}

	public Partnerkommunikation(Integer id, Integer partnerIId,
			String kommunikationsartCNr, String bez, String inhalt) {
		setIId(id);
		setPartnerIId(partnerIId);
		setKommunikationsartCNr(kommunikationsartCNr);
		setCBez(bez);
		setCInhalt(inhalt);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCBez() {
		return this.cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public String getCInhalt() {
		return this.cInhalt;
	}

	public void setCInhalt(String cInhalt) {
		this.cInhalt = cInhalt;
	}

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public String getKommunikationsartCNr() {
		return this.kommunikationsartCNr;
	}

	public void setKommunikationsartCNr(String kommunikationsartCNr) {
		this.kommunikationsartCNr = kommunikationsartCNr;
	}

	public Integer getPartnerIId() {
		return this.partnerIId;
	}

	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId;
	}


	



}
