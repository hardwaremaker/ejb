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
package com.lp.server.eingangsrechnung.ejb;

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
		@NamedQuery(name = "EingangsrechnungKontierungfindByEingangsrechnungIIdOrderByNBetrag", query = "SELECT OBJECT(o) FROM EingangsrechnungKontierung o WHERE o.eingangsrechnungIId=?1 ORDER BY o.nBetrag ASC"),
		@NamedQuery(name = "EingangsrechnungKontierungfindByEingangsrechnungIId", query = "SELECT OBJECT(o) FROM EingangsrechnungKontierung o WHERE o.eingangsrechnungIId=?1"),
		@NamedQuery(name = "EingangsrechnungKontierungfindAll", query = "SELECT OBJECT(o) FROM EingangsrechnungKontierung o") })
@Entity
@Table(name = "ER_KONTIERUNG")
public class EingangsrechnungKontierung implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "N_BETRAG")
	private BigDecimal nBetrag;

	@Column(name = "N_BETRAGUST")
	private BigDecimal nBetragust;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "EINGANGSRECHNUNG_I_ID")
	private Integer eingangsrechnungIId;

	@Column(name = "KONTO_I_ID")
	private Integer kontoIId;

	@Column(name = "KOSTENSTELLE_I_ID")
	private Integer kostenstelleIId;

	@Column(name = "MWSTSATZ_I_ID")
	private Integer mwstsatzIId;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	private static final long serialVersionUID = 1L;

	public EingangsrechnungKontierung() {
		super();
	}

	public EingangsrechnungKontierung(Integer id, Integer eingangsrechnungIId,
			BigDecimal betrag, BigDecimal betragUst, Integer mwstsatzIId,
			Integer kostenstelleIId, Integer kontoIId,
			Integer personalIIdAnlegen2, Integer personalIIdAendern2) {
		setIId(id);
		setEingangsrechnungIId(eingangsrechnungIId);
		setNBetrag(betrag);
		setNBetragust(betragUst);
		setMwstsatzIId(mwstsatzIId);
		setKostenstelleIId(kostenstelleIId);
		setKontoIId(kontoIId);
		setPersonalIIdAnlegen(personalIIdAnlegen2);
		setPersonalIIdAendern(personalIIdAendern2);
		// Setzen der NOT NULL Felder
		java.sql.Timestamp timestamp = new java.sql.Timestamp(System
				.currentTimeMillis());
		this.setTAendern(timestamp);
		this.setTAnlegen(timestamp);
	}

	public Integer getIId() {
		return this.iId;
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

	public BigDecimal getNBetragust() {
		return this.nBetragust;
	}

	public void setNBetragust(BigDecimal nBetragust) {
		this.nBetragust = nBetragust;
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

	public Integer getEingangsrechnungIId() {
		return this.eingangsrechnungIId;
	}

	public void setEingangsrechnungIId(Integer eingangsrechnungIId) {
		this.eingangsrechnungIId = eingangsrechnungIId;
	}

	public Integer getKontoIId() {
		return this.kontoIId;
	}

	public void setKontoIId(Integer kontoIId) {
		this.kontoIId = kontoIId;
	}

	public Integer getKostenstelleIId() {
		return this.kostenstelleIId;
	}

	public void setKostenstelleIId(Integer kostenstelleIId) {
		this.kostenstelleIId = kostenstelleIId;
	}

	public Integer getMwstsatzIId() {
		return this.mwstsatzIId;
	}

	public void setMwstsatzIId(Integer mwstsatzIId) {
		this.mwstsatzIId = mwstsatzIId;
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

}
