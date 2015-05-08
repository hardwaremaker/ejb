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
package com.lp.server.personal.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "TelefonzeitenfindByPersonalIIdTVon", query = "SELECT OBJECT(C) FROM Telefonzeiten c WHERE c.personalIId = ?1 AND c.tVon = ?2"),
		@NamedQuery(name = "TelefonzeitenfindByPartnerIId", query = "SELECT OBJECT(C) FROM Telefonzeiten c WHERE c.partnerIId = ?1"),
		@NamedQuery(name = "TelefonzeitenfindByAnsprechpartnerIId", query = "SELECT OBJECT(C) FROM Telefonzeiten c WHERE c.ansprechpartnerIId = ?1") })
@Entity
@Table(name = "PERS_TELEFONZEITEN")
public class Telefonzeiten implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "T_VON")
	private Timestamp tVon;

	@Column(name = "T_BIS")
	private Timestamp tBis;

	@Column(name = "X_KOMMENTAREXT")
	private String xKommentarext;

	@Column(name = "X_KOMMENTARINT")
	private String xKommentarint;

	@Column(name = "ANSPRECHPARTNER_I_ID")
	private Integer ansprechpartnerIId;

	@Column(name = "PARTNER_I_ID")
	private Integer partnerIId;

	@Column(name = "PERSONAL_I_ID")
	private Integer personalIId;
	
	@Column(name = "PROJEKT_I_ID")
	private Integer projektIId;
	
	public Integer getProjektIId() {
		return projektIId;
	}

	public void setProjektIId(Integer projektIId) {
		this.projektIId = projektIId;
	}
	private static final long serialVersionUID = 1L;

	public Telefonzeiten() {
		super();
	}

	public Telefonzeiten(Integer id, Integer personalIId, Timestamp von,
			String kommentarext) {
		setIId(id);
		setPersonalIId(personalIId);
		setTVon(von);
		setXKommentarext(kommentarext);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Timestamp getTVon() {
		return this.tVon;
	}

	public void setTVon(Timestamp tVon) {
		this.tVon = tVon;
	}

	public Timestamp getTBis() {
		return this.tBis;
	}

	public void setTBis(Timestamp tBis) {
		this.tBis = tBis;
	}

	public String getXKommentarext() {
		return this.xKommentarext;
	}

	public void setXKommentarext(String xKommentarext) {
		this.xKommentarext = xKommentarext;
	}

	public String getXKommentarint() {
		return this.xKommentarint;
	}

	public void setXKommentarint(String xKommentarint) {
		this.xKommentarint = xKommentarint;
	}

	public Integer getAnsprechpartnerIId() {
		return this.ansprechpartnerIId;
	}

	public void setAnsprechpartnerIId(Integer ansprechpartnerIId) {
		this.ansprechpartnerIId = ansprechpartnerIId;
	}

	public Integer getPartnerIId() {
		return this.partnerIId;
	}

	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId;
	}

	public Integer getPersonalIId() {
		return this.personalIId;
	}

	public void setPersonalIId(Integer personalIId) {
		this.personalIId = personalIId;
	}

}
