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
package com.lp.server.instandhaltung.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({ @NamedQuery(name = "StandortFindByInstandhaltungIIdPartnerIId", query = "SELECT OBJECT(o) FROM Standort o WHERE o.instandhaltungIId = ?1 AND o.partnerIId = ?2") })
@Entity
@Table(name = "IS_STANDORT")
public class Standort implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "INSTANDHALTUNG_I_ID")
	private Integer instandhaltungIId;

	@Column(name = "PARTNER_I_ID")
	private Integer partnerIId;
	
	@Column(name = "AUFTRAG_I_ID")
	private Integer auftragIId;

	public Integer getAuftragIId() {
		return auftragIId;
	}

	public void setAuftragIId(Integer auftragIId) {
		this.auftragIId = auftragIId;
	}


	@Column(name = "B_VERSTECKT")
	private Short bVersteckt;
	
	
	@Column(name = "X_BEMERKUNG")
	private String xBemerkung;
	
	
	public String getXBemerkung() {
		return xBemerkung;
	}

	public void setXBemerkung(String xBemerkung) {
		this.xBemerkung = xBemerkung;
	}
	
	
	@Column(name = "C_DOKUMENTENLINK")
	private String cDokumentenlink;
	
	
	public String getCDokumentenlink() {
		return cDokumentenlink;
	}

	public void setCDokumentenlink(String cDokumentenlink) {
		this.cDokumentenlink = cDokumentenlink;
	}


	@Column(name = "ANSPRECHPARTNER_I_ID")
	private Integer ansprechpartnerIId;

	public Integer getAnsprechpartnerIId() {
		return this.ansprechpartnerIId;
	}

	public void setAnsprechpartnerIId(Integer ansprechpartner) {
		this.ansprechpartnerIId = ansprechpartner;
	}

	public Short getBVersteckt() {
		return bVersteckt;
	}

	public void setBVersteckt(Short bVersteckt) {
		this.bVersteckt = bVersteckt;
	}

	public Integer getInstandhaltungIId() {
		return instandhaltungIId;
	}

	public void setInstandhaltungIId(Integer instandhaltungIId) {
		this.instandhaltungIId = instandhaltungIId;
	}

	public Integer getPartnerIId() {
		return partnerIId;
	}

	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId;
	}

	private static final long serialVersionUID = 1L;

	public Standort() {
		super();
	}

	public Standort(Integer id, Integer instandhaltungIId, Integer partnerIId,
			Short bVersteckt,Integer auftragIId) {
		setIId(id);
		setInstandhaltungIId(instandhaltungIId);
		setPartnerIId(partnerIId);
		setBVersteckt(bVersteckt);
		setAuftragIId(auftragIId);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

}
