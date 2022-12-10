package com.lp.server.forecast.ejb;

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
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@NamedQueries({ 
	@NamedQuery(name = "LinienabrufFindByForecastpositionIIdCLinie", query = "SELECT OBJECT(o) FROM Linienabruf o WHERE o.forecastpositionIId = ?1 AND o.cLinie = ?2"),
	@NamedQuery(name = LinienabrufQuery.ByForecastpositionIId, query = "SELECT OBJECT(o) FROM Linienabruf o WHERE o.forecastpositionIId = :forecastpositionid"),
	@NamedQuery(name = LinienabrufQuery.CountForecastpositionIId, query = "SELECT COUNT(o) FROM Linienabruf o WHERE o.forecastpositionIId = :forecastpositionid")})
@Entity
@Table(name = "FC_LINIENABRUF")
public class Linienabruf implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "N_MENGE")
	private BigDecimal nMenge;

	
	@Column(name = "N_MENGE_ERFASST")
	private BigDecimal nMengeErfasst;

	public BigDecimal getNMengeErfasst() {
		return nMengeErfasst;
	}

	public void setNMengeErfasst(BigDecimal nMengeErfasst) {
		this.nMengeErfasst = nMengeErfasst;
	}

	
	@Column(name = "FORECASTPOSITION_I_ID")
	private Integer forecastpositionIId;

	@Column(name = "C_LINIE")
	private String cLinie;

	public Integer getForecastpositionIId() {
		return forecastpositionIId;
	}

	public void setForecastpositionIId(Integer forecastpositionIId) {
		this.forecastpositionIId = forecastpositionIId;
	}

	public String getCLinie() {
		return cLinie;
	}

	public void setCLinie(String cLinie) {
		this.cLinie = cLinie;
	}

	public String getCBereichNr() {
		return cBereichNr;
	}

	public void setCBereichNr(String cBereichNr) {
		this.cBereichNr = cBereichNr;
	}

	public String getCBereichBez() {
		return cBereichBez;
	}

	public void setCBereichBez(String cBereichBez) {
		this.cBereichBez = cBereichBez;
	}

	public String getCBestellnummer() {
		return cBestellnummer;
	}

	public void setCBestellnummer(String cBestellnummer) {
		this.cBestellnummer = cBestellnummer;
	}

	public java.sql.Timestamp getTProduktionstermin() {
		return tProduktionstermin;
	}

	public void setTProduktionstermin(java.sql.Timestamp tProduktionstermin) {
		this.tProduktionstermin = tProduktionstermin;
	}

	@Column(name = "C_BEREICH_NR")
	private String cBereichNr;
	@Column(name = "C_BEREICH_BEZ")
	private String cBereichBez;
	@Column(name = "C_BESTELLNUMMER")
	private String cBestellnummer;

	@Column(name = "T_PRODUKTIONSTERMIN")
	private java.sql.Timestamp tProduktionstermin;

	public BigDecimal getNMenge() {
		return nMenge;
	}

	public void setNMenge(BigDecimal nMenge) {
		this.nMenge = nMenge;
	}

	private static final long serialVersionUID = 1L;

	public Linienabruf() {
		super();
	}

	public Linienabruf(Integer id, Integer forecastpositionIId, String cLinie,String cBereichNr,String cBereichBez,String cBestellnummer,Timestamp tProduktionstermin,
			BigDecimal nMenge, BigDecimal nMengeErfasst) {
		setIId(id);
		setForecastpositionIId(forecastpositionIId);
		setCLinie(cLinie);
		setCBereichNr(cBereichNr);
		setCBereichBez(cBereichBez);
		setCBestellnummer(cBestellnummer);
		setTProduktionstermin(tProduktionstermin);
		setNMenge(nMenge);
		setNMengeErfasst(nMengeErfasst);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

}
