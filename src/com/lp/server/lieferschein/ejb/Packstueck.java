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
package com.lp.server.lieferschein.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "selectgNextPackstuecknummer", query = "SELECT MAX (o.lNummer) FROM Packstueck o"),
		@NamedQuery(name = "PackstueckFindFyLosIId", query = "SELECT o FROM Packstueck o WHERE o.losIId=?1") })
@Entity
@Table(name = "LS_PACKSTUECK")
public class Packstueck implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "L_NUMMER")
	private Long lNummer;

	@Column(name = "LOS_I_ID")
	private Integer losIId;
	@Column(name = "LOSABLIEFERUNG_I_ID")
	private Integer losablieferungIId;
	@Column(name = "LIEFERSCHEIN_I_ID")
	private Integer lieferscheinIId;
	@Column(name = "LIEFERSCHEINPOSITION_I_ID")
	private Integer lieferscheinpositionIId;
	@Column(name = "FORECASTPOSITION_I_ID")
	private Integer forecastpositionIId;

	public Long getLNummer() {
		return lNummer;
	}

	public void setLNummer(Long lNummer) {
		this.lNummer = lNummer;
	}

	public Integer getLosIId() {
		return losIId;
	}

	public void setLosIId(Integer losIId) {
		this.losIId = losIId;
	}

	public Integer getLosablieferungIId() {
		return losablieferungIId;
	}

	public void setLosablieferungIId(Integer losablieferungIId) {
		this.losablieferungIId = losablieferungIId;
	}

	public Integer getLieferscheinIId() {
		return lieferscheinIId;
	}

	public void setLieferscheinIId(Integer lieferscheinIId) {
		this.lieferscheinIId = lieferscheinIId;
	}

	public Integer getLieferscheinpositionIId() {
		return lieferscheinpositionIId;
	}

	public void setLieferscheinpositionIId(Integer lieferscheinpositionIId) {
		this.lieferscheinpositionIId = lieferscheinpositionIId;
	}

	public Timestamp gettAendern() {
		return tAendern;
	}

	public void settAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getForecastpositionIId() {
		return forecastpositionIId;
	}

	public void setForecastpositionIId(Integer forecastpositionIId) {
		this.forecastpositionIId = forecastpositionIId;
	}

	private static final long serialVersionUID = 1L;

	public Packstueck() {
		super();
	}

	public Packstueck(Integer iId, Long lNummer, Integer personalIIdAendern,
			Timestamp tAendern) {
		setIId(iId);
		setLNummer(lNummer);
		setPersonalIIdAendern(personalIIdAendern);
		settAendern(tAendern);
	}
}
