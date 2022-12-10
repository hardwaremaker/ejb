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
package com.lp.server.bestellung.ejb;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BES_BSZAHLUNGSPLAN")
public class BSZahlungsplan implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "BESTELLUNG_I_ID")
	private Integer bestellungIId;

	public Integer getBestellungIId() {
		return bestellungIId;
	}

	public void setBestellungIId(Integer bestellungIId) {
		this.bestellungIId = bestellungIId;
	}

	@Column(name = "T_TERMIN")
	private java.sql.Timestamp tTermin;

	@Column(name = "N_BETRAG")
	private BigDecimal nBetrag;

	@Column(name = "N_BETRAG_URSPRUNG")
	private BigDecimal nBetragUrsprung;

	public java.sql.Timestamp getTErledigt() {
		return tErledigt;
	}

	public void setTErledigt(java.sql.Timestamp tErledigt) {
		this.tErledigt = tErledigt;
	}

	@Column(name = "C_KOMMENTAR")
	private String cKommentar;

	@Column(name = "X_TEXT")
	private String xText;

	@Column(name = "T_ERLEDIGT")
	private java.sql.Timestamp tErledigt;

	@Column(name = "PERSONAL_I_ID_ERLEDIGT")
	private Integer personalIIdErledigt;

	public Integer getPersonalIIdErledigt() {
		return personalIIdErledigt;
	}

	public void setPersonalIIdErledigt(Integer personalIIdErledigt) {
		this.personalIIdErledigt = personalIIdErledigt;
	}

	public String getCKommentar() {
		return cKommentar;
	}

	public void setCKommentar(String cKommentar) {
		this.cKommentar = cKommentar;
	}

	public String getXText() {
		return xText;
	}

	public void setXText(String xText) {
		this.xText = xText;
	}

	private static final long serialVersionUID = 1L;

	public BSZahlungsplan() {
		super();
	}

	public BSZahlungsplan(Integer id, Integer bestellungIId,
			java.sql.Timestamp tTermin, BigDecimal nBetrag,
			BigDecimal nBetragUrsprung) {
		setIId(id);
		setBestellungIId(bestellungIId);
		setTTermin(tTermin);
		setNBetrag(nBetrag);
		setNBetragUrsprung(nBetragUrsprung);

	}

	public java.sql.Timestamp getTTermin() {
		return tTermin;
	}

	public void setTTermin(java.sql.Timestamp tTermin) {
		this.tTermin = tTermin;
	}

	public BigDecimal getNBetrag() {
		return nBetrag;
	}

	public void setNBetrag(BigDecimal nBetrag) {
		this.nBetrag = nBetrag;
	}

	public BigDecimal getNBetragUrsprung() {
		return nBetragUrsprung;
	}

	public void setNBetragUrsprung(BigDecimal nBetragUrsprung) {
		this.nBetragUrsprung = nBetragUrsprung;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

}
