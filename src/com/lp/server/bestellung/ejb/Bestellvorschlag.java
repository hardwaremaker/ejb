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
package com.lp.server.bestellung.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({ @NamedQuery(name = "BestellvorschlagfindByLieferantIIdMandantCNr", query = "SELECT OBJECT (o) FROM Bestellvorschlag o WHERE o.lieferantIId=?1 AND o.mandantCNr=?2") })
@Entity
@Table(name = "BES_BESTELLVORSCHLAG")
public class Bestellvorschlag implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "N_ZUBESTELLENDEMENGE")
	private BigDecimal nZubestellendemenge;

	@Column(name = "T_BESTELLTERMIN")
	private Timestamp tLiefertermin;

	@Column(name = "I_BELEGARTID")
	private Integer iBelegartid;

	@Column(name = "N_NETTOEINZELPREIS")
	private BigDecimal nNettoeinzelpreis;

	@Column(name = "N_RABATTBETRAG")
	private BigDecimal nRabattbetrag;

	@Column(name = "N_NETTOGESAMTPREISMINUSRABATTE")
	private BigDecimal nNettogesamtpreisminusrabatte;

	@Column(name = "N_NETTOGESAMTPREIS")
	private BigDecimal nNettogesamtpreis;

	@Column(name = "F_RABATTSATZ")
	private Double fRabattsatz;

	@Column(name = "I_BELEGARTPOSITIONID")
	private Integer iBelegartpositionid;

	@Column(name = "BELEGART_C_NR")
	private String belegartCNr;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "LIEFERANT_I_ID")
	private Integer lieferantIId;

	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;

	@Column(name = "B_NETTOPREISUEBERSTEUERT")
	private Short bNettopreisuebersteuert;

	private static final long serialVersionUID = 1L;

	public Bestellvorschlag() {
		super();
	}

	public Bestellvorschlag(Integer id, Double rabattsatz,
			BigDecimal nettoGesamtPreisMinusRabatte,
			BigDecimal nettogesamtpreis, BigDecimal rabattbetrag,
			BigDecimal nettoeinzelpreis, Integer lieferantId,
			Integer belegartId, String belegartCNr, Timestamp bestelltermin,
			BigDecimal zubestellendeMenge, Integer artikelId,
			String mandantCNr, Short bNettopreisUebersteuert) {
		setIId(id);
		setFRabattsatz(rabattsatz);
		setNNettogesamtpreisminusrabatte(nettoGesamtPreisMinusRabatte);
		setNNettogesamtpreis(nettogesamtpreis);
		setNRabattbetrag(rabattbetrag);
		setNNettoeinzelpreis(nettoeinzelpreis);
		setLieferantIId(lieferantId);
		setIBelegartid(belegartId);
		setBelegartCNr(belegartCNr);
		setTLiefertermin(bestelltermin);
		setNZubestellendemenge(zubestellendeMenge);
		setArtikelIId(artikelId);
		setMandantCNr(mandantCNr);
		setBNettopreisuebersteuert(bNettopreisUebersteuert);
	}
	@Column(name = "PROJEKT_I_ID")
	private Integer projektIId;

	public Integer getProjektIId() {
		return projektIId;
	}

	public void setProjektIId(Integer projektIId) {
		this.projektIId = projektIId;
	}
	
	
	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public BigDecimal getNZubestellendemenge() {
		return this.nZubestellendemenge;
	}

	public void setNZubestellendemenge(BigDecimal nZubestellendemenge) {
		this.nZubestellendemenge = nZubestellendemenge;
	}

	public Timestamp getTLiefertermin() {
		return this.tLiefertermin;
	}

	public void setTLiefertermin(Timestamp tLiefertermin) {
		this.tLiefertermin = tLiefertermin;
	}

	public Integer getIBelegartid() {
		return this.iBelegartid;
	}

	public void setIBelegartid(Integer iBelegartid) {
		this.iBelegartid = iBelegartid;
	}

	public BigDecimal getNNettoeinzelpreis() {
		return this.nNettoeinzelpreis;
	}

	public void setNNettoeinzelpreis(BigDecimal nNettoeinzelpreis) {
		this.nNettoeinzelpreis = nNettoeinzelpreis;
	}

	public BigDecimal getNRabattbetrag() {
		return this.nRabattbetrag;
	}

	public void setNRabattbetrag(BigDecimal nRabattbetrag) {
		this.nRabattbetrag = nRabattbetrag;
	}

	public BigDecimal getNNettogesamtpreisminusrabatte() {
		return this.nNettogesamtpreisminusrabatte;
	}

	public void setNNettogesamtpreisminusrabatte(
			BigDecimal nNettogesamtpreisminusrabatte) {
		this.nNettogesamtpreisminusrabatte = nNettogesamtpreisminusrabatte;
	}

	public BigDecimal getNNettogesamtpreis() {
		return this.nNettogesamtpreis;
	}

	public void setNNettogesamtpreis(BigDecimal nNettogesamtpreis) {
		this.nNettogesamtpreis = nNettogesamtpreis;
	}

	public Double getFRabattsatz() {
		return this.fRabattsatz;
	}

	public void setFRabattsatz(Double fRabattsatz) {
		this.fRabattsatz = fRabattsatz;
	}

	public Integer getIBelegartpositionid() {
		return this.iBelegartpositionid;
	}

	public void setIBelegartpositionid(Integer iBelegartpositionid) {
		this.iBelegartpositionid = iBelegartpositionid;
	}

	public String getBelegartCNr() {
		return this.belegartCNr;
	}

	public void setBelegartCNr(String belegartCNr) {
		this.belegartCNr = belegartCNr;
	}

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public Integer getLieferantIId() {
		return this.lieferantIId;
	}

	public void setLieferantIId(Integer lieferantIId) {
		this.lieferantIId = lieferantIId;
	}

	public Integer getArtikelIId() {
		return this.artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public Short getBNettopreisuebersteuert() {
		return this.bNettopreisuebersteuert;
	}

	public void setBNettopreisuebersteuert(Short bNettopreisuebersteuert) {
		this.bNettopreisuebersteuert = bNettopreisuebersteuert;
	}

}
