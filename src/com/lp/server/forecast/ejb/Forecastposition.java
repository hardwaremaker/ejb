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
		@NamedQuery(name = ForecastpositionQuery.ByForecastauftragIIdArtikelIId, query = "SELECT OBJECT(o) FROM Forecastposition o WHERE o.forecastauftragIId = ?1 AND o.artikelIId = ?2 ORDER BY o.tTermin ASC,o.iId ASC"),
		@NamedQuery(name = ForecastpositionQuery.ByForecastauftragIId, query = "SELECT OBJECT(o) FROM Forecastposition o WHERE o.forecastauftragIId = ?1 ORDER BY o.tTermin ASC, o.iId ASC") })
@Entity
@Table(name = "FC_FORECASTPOSITION")
public class Forecastposition implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "FORECASTAUFTRAG_I_ID")
	private Integer forecastauftragIId;
	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;

	@Column(name = "X_KOMMENTAR")
	private String xKommentar;

	public String getXKommentar() {
		return xKommentar;
	}

	public void setXKommentar(String xKommentar) {
		this.xKommentar = xKommentar;
	}

	@Column(name = "STATUS_C_NR")
	private String statusCNr;

	@Column(name = "FORECASTART_C_NR")
	private String forecastartCNr;

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

	@Column(name = "T_TERMIN")
	private java.sql.Timestamp tTermin;

	@Column(name = "C_BESTELLNUMMER")
	private String cBestellnummer;
	
	@Column(name = "PERSONAL_I_ID_PRODUKTION")
	private Integer personalIIdProduktion;
	
	@Column(name = "T_PRODUKTION")
	private Timestamp tProduktion;

	public String getCBestellnummer() {
		return this.cBestellnummer;
	}

	public void setCBestellnummer(String cBestellnummer) {
		this.cBestellnummer = cBestellnummer;
	}

	public Integer getForecastauftragIId() {
		return forecastauftragIId;
	}

	public void setForecastauftragIId(Integer forecastauftragIId) {
		this.forecastauftragIId = forecastauftragIId;
	}

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public String getStatusCNr() {
		return statusCNr;
	}

	public void setStatusCNr(String statusCNr) {
		this.statusCNr = statusCNr;
	}

	public String getForecastartCNr() {
		return forecastartCNr;
	}

	public void setForecastartCNr(String forecastartCNr) {
		this.forecastartCNr = forecastartCNr;
	}

	public BigDecimal getNMenge() {
		return nMenge;
	}

	public void setNMenge(BigDecimal nMenge) {
		this.nMenge = nMenge;
	}

	public java.sql.Timestamp getTTermin() {
		return tTermin;
	}

	public void setTTermin(java.sql.Timestamp tTermin) {
		this.tTermin = tTermin;
	}

	private static final long serialVersionUID = 1L;

	public Forecastposition() {
		super();
	}

	public Forecastposition(Integer id, Integer forecastauftragIId,
			Integer artikelIId, java.sql.Timestamp tTermin, BigDecimal nMenge,
			String statusCNr,BigDecimal nMengeErfasst) {
		setIId(id);
		setForecastauftragIId(forecastauftragIId);
		setArtikelIId(artikelIId);
		setTTermin(tTermin);
		setNMenge(nMenge);
		setStatusCNr(statusCNr);
		setNMengeErfasst(nMengeErfasst);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Timestamp getTProduktion() {
		return tProduktion;
	}
	
	public void setTProduktion(Timestamp tProduktion) {
		this.tProduktion = tProduktion;
	}
	
	public Integer getPersonalIIdProduktion() {
		return personalIIdProduktion;
	}
	
	public void setPersonalIIdProduktion(Integer personalIIdProduktion) {
		this.personalIIdProduktion = personalIIdProduktion;
	}
}
