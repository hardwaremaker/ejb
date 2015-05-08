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
package com.lp.server.artikel.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "InventurprotokollfindyByInventurlisteIIdTZeitpunkt", query = "SELECT OBJECT (o) FROM Inventurprotokoll o WHERE o.inventurlisteIId=?1 AND o.tZeitpunkt=?2"),
		@NamedQuery(name = "InventurprotokollfindByInventurlisteIId", query = "SELECT OBJECT (o) FROM Inventurprotokoll o WHERE o.inventurlisteIId=?1") })
@Entity
@Table(name = "WW_INVENTURPROTOKOLL")
public class Inventurprotokoll implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "T_ZEITPUNKT")
	private Timestamp tZeitpunkt;

	@Column(name = "N_KORREKTURMENGE")
	private BigDecimal nKorrekturmenge;

	@Column(name = "N_INVENTURPREIS")
	private BigDecimal nInventurpreis;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "INVENTUR_I_ID")
	private Integer inventurIId;

	@Column(name = "INVENTURLISTE_I_ID")
	private Integer inventurlisteIId;

	private static final long serialVersionUID = 1L;

	public Inventurprotokoll() {
		super();
	}

	public Inventurprotokoll(Integer id,
			Integer inventurIId2,
			Integer inventurlisteIId2,
			Timestamp zeitpunkt,
			BigDecimal korrekturmenge,
			BigDecimal inventurpreis,
			Integer personalIIdAendern2) {
		setIId(id);
		setInventurlisteIId(inventurlisteIId2);
		setTZeitpunkt(zeitpunkt);
		setNKorrekturmenge(korrekturmenge);
		setPersonalIIdAendern(personalIIdAendern2);
		setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));
		setInventurIId(inventurIId2);
		setNInventurpreis(inventurpreis);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Timestamp getTZeitpunkt() {
		return this.tZeitpunkt;
	}

	public void setTZeitpunkt(Timestamp tZeitpunkt) {
		this.tZeitpunkt = tZeitpunkt;
	}

	public BigDecimal getNKorrekturmenge() {
		return this.nKorrekturmenge;
	}

	public void setNKorrekturmenge(BigDecimal nKorrekturmenge) {
		this.nKorrekturmenge = nKorrekturmenge;
	}

	public BigDecimal getNInventurpreis() {
		return this.nInventurpreis;
	}

	public void setNInventurpreis(BigDecimal nInventurpreis) {
		this.nInventurpreis = nInventurpreis;
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

	public Integer getInventurIId() {
		return this.inventurIId;
	}

	public void setInventurIId(Integer inventurIId) {
		this.inventurIId = inventurIId;
	}

	public Integer getInventurlisteIId() {
		return this.inventurlisteIId;
	}

	public void setInventurlisteIId(Integer inventurlisteIId) {
		this.inventurlisteIId = inventurlisteIId;
	}

}
