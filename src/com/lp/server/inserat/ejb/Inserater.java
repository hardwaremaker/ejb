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
package com.lp.server.inserat.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "InseraterfindInseratIIdEingansrechnungIId", query = "SELECT OBJECT (o) FROM Inserater o WHERE o.inseratIId=?1"),
		@NamedQuery(name = "InseraterfindByInseratIIdEingansrechnungIId", query = "SELECT OBJECT (o) FROM Inserater o WHERE o.inseratIId=?1 AND o.eingangsrechnungIId=?2"),
		@NamedQuery(name = "InseraterfindByInseratIId", query = "SELECT OBJECT (o) FROM Inserater o WHERE o.inseratIId=?1"),
		@NamedQuery(name = "InseraterfindByEingangsrechnungIId", query = "SELECT OBJECT (o) FROM Inserater o WHERE o.eingangsrechnungIId=?1") })
@Entity
@Table(name = "IV_INSERATER")
public class Inserater implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "INSERAT_I_ID")
	private Integer inseratIId;

	@Column(name = "EINGANGSRECHNUNG_I_ID")
	private Integer eingangsrechnungIId;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "N_BETRAG")
	private BigDecimal nBetrag;

	@Column(name = "C_TEXT")
	private String cText;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	public Integer getInseratIId() {
		return inseratIId;
	}

	public void setInseratIId(Integer inseratIId) {
		this.inseratIId = inseratIId;
	}

	private static final long serialVersionUID = 1L;

	public Inserater(Integer id, Integer inseratIId,
			Integer eingangsrechnungIId, BigDecimal betrag,
			Integer personalIIdAnlegen2, Integer personalIIdAendern2) {
		setIId(id);
		setInseratIId(inseratIId);
		setEingangsrechnungIId(eingangsrechnungIId);
		setNBetrag(betrag);
		setPersonalIIdAnlegen(personalIIdAnlegen2);
		setPersonalIIdAendern(personalIIdAendern2);

		java.sql.Timestamp timestamp = new java.sql.Timestamp(
				System.currentTimeMillis());
		this.setTAendern(timestamp);
		this.setTAnlegen(timestamp);
	}

	public Integer getEingangsrechnungIId() {
		return eingangsrechnungIId;
	}

	public void setEingangsrechnungIId(Integer eingangsrechnungIId) {
		this.eingangsrechnungIId = eingangsrechnungIId;
	}

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public BigDecimal getNBetrag() {
		return this.nBetrag;
	}

	public void setNBetrag(BigDecimal nBetrag) {
		this.nBetrag = nBetrag;
	}

	public String getCText() {
		return this.cText;
	}

	public void setCText(String cText) {
		this.cText = cText;
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

	public Inserater() {

	}

	public Integer getPersonalIIdAnlegen() {
		return this.personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}
}
