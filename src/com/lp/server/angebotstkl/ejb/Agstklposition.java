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
package com.lp.server.angebotstkl.ejb;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "AgstklpositionfindByAgstklIId", query = "SELECT OBJECT (o) FROM Agstklposition o WHERE o.agstklIId=?1 ORDER BY o.iSort"),
		@NamedQuery(name = "AgstklpositionejbSelectMaxISort", query = "SELECT MAX (o.iSort) FROM Agstklposition o WHERE o.agstklIId=?1"),
		@NamedQuery(name = "AgstklpositionfindByAgstklIIdMengeNotNull", query = "SELECT OBJECT (o) FROM Agstklposition o WHERE o.agstklIId=?1 AND o.nMenge IS NOT NULL ORDER BY o.iSort"),
		@NamedQuery(name = "AgstklpositionfindByAgstklIIdBDrucken", query = "SELECT OBJECT (o) FROM Agstklposition o WHERE o.agstklIId=?1 AND o.bDrucken=?2 ORDER BY o.iSort") })
@Entity
@Table(name = "AS_AGSTKLPOSITION")
public class Agstklposition implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "I_SORT")
	private Integer iSort;

	@Column(name = "C_BEZ")
	private String cBez;

	@Column(name = "C_ZBEZ")
	private String cZbez;

	@Column(name = "B_ARTIKELBEZEICHNUNGUEBERSTEUERT")
	private Short bArtikelbezeichnunguebersteuert;

	@Column(name = "B_AUFSCHLAGGESAMT_FIXIERT")
	private Short bAufschlaggesamtFixiert;

	@Column(name = "N_MENGE")
	private BigDecimal nMenge;

	@Column(name = "N_AUFSCHLAG")
	private BigDecimal nAufschlag;

	public Short getBAufschlaggesamtFixiert() {
		return bAufschlaggesamtFixiert;
	}

	public void setBAufschlaggesamtFixiert(Short bAufschlaggesamtFixiert) {
		this.bAufschlaggesamtFixiert = bAufschlaggesamtFixiert;
	}

	public BigDecimal getNAufschlag() {
		return nAufschlag;
	}

	public void setNAufschlag(BigDecimal nAufschlag) {
		this.nAufschlag = nAufschlag;
	}

	public BigDecimal getNNettogesamtmitaufschlag() {
		return nNettogesamtmitaufschlag;
	}

	public void setNNettogesamtmitaufschlag(BigDecimal nNettogesamtmitaufschlag) {
		this.nNettogesamtmitaufschlag = nNettogesamtmitaufschlag;
	}

	public Double getFAufschlag() {
		return fAufschlag;
	}

	public void setFAufschlag(Double fAufschlag) {
		this.fAufschlag = fAufschlag;
	}

	@Column(name = "N_NETTOGESAMTMITAUFSCHLAG")
	private BigDecimal nNettogesamtmitaufschlag;
	@Column(name = "F_AUFSCHLAG")
	private Double fAufschlag;

	@Column(name = "N_MATERIALZUSCHLAG")
	private BigDecimal nMaterialzuschlag;

	public BigDecimal getNMaterialzuschlag() {
		return nMaterialzuschlag;
	}

	public void setNMaterialzuschlag(BigDecimal materialzuschlag) {
		nMaterialzuschlag = materialzuschlag;
	}

	@Column(name = "F_RABATTSATZ")
	private Double fRabattsatz;

	@Column(name = "B_RABATTSATZUEBERSTEUERT")
	private Short bRabattsatzuebersteuert;

	@Column(name = "F_ZUSATZRABATTSATZ")
	private Double fZusatzrabattsatz;

	@Column(name = "N_NETTOEINZELPREIS")
	private BigDecimal nNettoeinzelpreis;

	@Column(name = "N_NETTOGESAMTPREIS")
	private BigDecimal nNettogesamtpreis;

	@Column(name = "N_GESTEHUNGSPREIS")
	private BigDecimal nGestehungspreis;

	@Column(name = "B_DRUCKEN")
	private Short bDrucken;

	@Column(name = "AGSTKL_I_ID")
	private Integer agstklIId;

	@Column(name = "AGSTKLPOSITIONSART_C_NR")
	private String agstklpositionsartCNr;

	@Column(name = "EINHEIT_C_NR")
	private String einheitCNr;

	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;

	private static final long serialVersionUID = 1L;

	public Agstklposition() {
		super();
	}

	public Agstklposition(Integer id, Integer agstklIId,
			String agstklpositionsartCNr, Short artikelbezeichnunguebersteuert,
			Short rabattsatzuebersteuert, BigDecimal gestehungspreis,
			Short drucken) {
		setIId(id);
		setAgstklIId(agstklIId);
		setAgstklpositionsartCNr(agstklpositionsartCNr);
		setBArtikelbezeichnunguebersteuert(artikelbezeichnunguebersteuert);
		setBRabattsatzuebersteuert(rabattsatzuebersteuert);
		setBDrucken(drucken);

		// CMP alle NOT NULL und default felder hier setzen
		setBArtikelbezeichnunguebersteuert(new Short((short) 0));
		setBRabattsatzuebersteuert(new Short((short) 0));
		setBDrucken(new Short((short) 0));
		setNGestehungspreis(gestehungspreis);
		setBAufschlaggesamtFixiert(new Short((short) 0));
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getISort() {
		return this.iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public String getCBez() {
		return this.cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public String getCZbez() {
		return this.cZbez;
	}

	public void setCZbez(String cZbez) {
		this.cZbez = cZbez;
	}

	public Short getBArtikelbezeichnunguebersteuert() {
		return this.bArtikelbezeichnunguebersteuert;
	}

	public void setBArtikelbezeichnunguebersteuert(
			Short bArtikelbezeichnunguebersteuert) {
		this.bArtikelbezeichnunguebersteuert = bArtikelbezeichnunguebersteuert;
	}

	public BigDecimal getNMenge() {
		return this.nMenge;
	}

	public void setNMenge(BigDecimal nMenge) {
		this.nMenge = nMenge;
	}

	public Double getFRabattsatz() {
		return this.fRabattsatz;
	}

	public void setFRabattsatz(Double fRabattsatz) {
		this.fRabattsatz = fRabattsatz;
	}

	public Short getBRabattsatzuebersteuert() {
		return this.bRabattsatzuebersteuert;
	}

	public void setBRabattsatzuebersteuert(Short bRabattsatzuebersteuert) {
		this.bRabattsatzuebersteuert = bRabattsatzuebersteuert;
	}

	public Double getFZusatzrabattsatz() {
		return this.fZusatzrabattsatz;
	}

	public void setFZusatzrabattsatz(Double fZusatzrabattsatz) {
		this.fZusatzrabattsatz = fZusatzrabattsatz;
	}

	public BigDecimal getNNettoeinzelpreis() {
		return this.nNettoeinzelpreis;
	}

	public void setNNettoeinzelpreis(BigDecimal nNettoeinzelpreis) {
		this.nNettoeinzelpreis = nNettoeinzelpreis;
	}

	public BigDecimal getNNettogesamtpreis() {
		return this.nNettogesamtpreis;
	}

	public void setNNettogesamtpreis(BigDecimal nNettogesamtpreis) {
		this.nNettogesamtpreis = nNettogesamtpreis;
	}

	public BigDecimal getNGestehungspreis() {
		return this.nGestehungspreis;
	}

	public void setNGestehungspreis(BigDecimal nGestehungspreis) {
		this.nGestehungspreis = nGestehungspreis;
	}

	public Short getBDrucken() {
		return this.bDrucken;
	}

	public void setBDrucken(Short bDrucken) {
		this.bDrucken = bDrucken;
	}

	public Integer getAgstklIId() {
		return this.agstklIId;
	}

	public void setAgstklIId(Integer agstklIId) {
		this.agstklIId = agstklIId;
	}

	public String getAgstklpositionsartCNr() {
		return this.agstklpositionsartCNr;
	}

	public void setAgstklpositionsartCNr(String agstklpositionsartCNr) {
		this.agstklpositionsartCNr = agstklpositionsartCNr;
	}

	public String getEinheitCNr() {
		return this.einheitCNr;
	}

	public void setEinheitCNr(String einheitCNr) {
		this.einheitCNr = einheitCNr;
	}

	public Integer getArtikelIId() {
		return this.artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

}
