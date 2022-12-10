
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
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.util.Helper;

@NamedQueries({
		@NamedQuery(name = "PositionlieferantfindByEinkaufsangebotpositionIIdEgaklieferantIId", query = "SELECT OBJECT (o) FROM Positionlieferant o WHERE o.einkaufsangebotpositionIId=?1 AND o.egaklieferantIId=?2 "),
		@NamedQuery(name = "PositionlieferantfindByEinkaufsangebotpositionIId", query = "SELECT OBJECT (o) FROM Positionlieferant o WHERE o.einkaufsangebotpositionIId=?1 ")})
@Entity
@Table(name = "AS_POSITIONLIEFERANT")
public class Positionlieferant implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "EINKAUFSANGEBOTPOSITION_I_ID")
	private Integer einkaufsangebotpositionIId;

	@Column(name = "EKAGLIEFERANT_I_ID")
	private Integer egaklieferantIId;

	@Column(name = "C_ARTIKELNRLIEFERANT")
	private String cArtikelnrlieferant;

	@Column(name = "C_BEMERKUNG")
	private String cBemerkung;
	
	@Column(name = "C_BEMERKUNG_INTERN")
	private String cBemerkungIntern;
	
	public String getCBemerkungIntern() {
		return cBemerkungIntern;
	}

	public void setCBemerkungIntern(String cBemerkungIntern) {
		this.cBemerkungIntern = cBemerkungIntern;
	}

	public String getCBemerkungVerkauf() {
		return cBemerkungVerkauf;
	}

	public void setCBemerkungVerkauf(String cBemerkungVerkauf) {
		this.cBemerkungVerkauf = cBemerkungVerkauf;
	}

	@Column(name = "C_BEMERKUNG_VERKAUF")
	private String cBemerkungVerkauf;

	@Column(name = "I_LIEFERZEITINKW")
	private Integer iLieferzeitinkw;

	@Column(name = "N_LAGERSTAND")
	private BigDecimal nLagerstand;

	@Column(name = "N_VERPACKUNGSEINHEIT")
	private BigDecimal nVerpackungseinheit;

	@Column(name = "N_MINDESTBESTELLMENGE")
	private BigDecimal nMindestbestellmenge;

	public String getCBemerkung() {
		return cBemerkung;
	}

	public void setCBemerkung(String cBemerkung) {
		this.cBemerkung = cBemerkung;
	}

	public Integer getILieferzeitinkw() {
		return iLieferzeitinkw;
	}

	public void setILieferzeitinkw(Integer iLieferzeitinkw) {
		this.iLieferzeitinkw = iLieferzeitinkw;
	}

	public BigDecimal getNLagerstand() {
		return nLagerstand;
	}

	public void setNLagerstand(BigDecimal nLagerstand) {
		this.nLagerstand = nLagerstand;
	}

	public BigDecimal getNVerpackungseinheit() {
		return nVerpackungseinheit;
	}

	public void setNVerpackungseinheit(BigDecimal nVerpackungseinheit) {
		this.nVerpackungseinheit = nVerpackungseinheit;
	}

	public BigDecimal getNMindestbestellmenge() {
		return nMindestbestellmenge;
	}

	public void setNMindestbestellmenge(BigDecimal nMindestbestellmenge) {
		this.nMindestbestellmenge = nMindestbestellmenge;
	}

	public BigDecimal getNTransportkosten() {
		return nTransportkosten;
	}

	public void setNTransportkosten(BigDecimal nTransportkosten) {
		this.nTransportkosten = nTransportkosten;
	}

	public BigDecimal getNPreisMenge1() {
		return nPreisMenge1;
	}

	public void setNPreisMenge1(BigDecimal nPreisMenge1) {
		this.nPreisMenge1 = nPreisMenge1;
	}

	public BigDecimal getNPreisMenge2() {
		return nPreisMenge2;
	}

	public void setNPreisMenge2(BigDecimal nPreisMenge2) {
		this.nPreisMenge2 = nPreisMenge2;
	}

	public BigDecimal getNPreisMenge3() {
		return nPreisMenge3;
	}

	public void setNPreisMenge3(BigDecimal nPreisMenge3) {
		this.nPreisMenge3 = nPreisMenge3;
	}

	public BigDecimal getNPreisMenge4() {
		return nPreisMenge4;
	}

	public void setNPreisMenge4(BigDecimal nPreisMenge4) {
		this.nPreisMenge4 = nPreisMenge4;
	}

	public BigDecimal getNPreisMenge5() {
		return nPreisMenge5;
	}

	public void setNPreisMenge5(BigDecimal nPreisMenge5) {
		this.nPreisMenge5 = nPreisMenge5;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	@Column(name = "N_TRANSPORTKOSTEN")
	private BigDecimal nTransportkosten;

	@Column(name = "N_PREIS_MENGE1")
	private BigDecimal nPreisMenge1;

	@Column(name = "N_PREIS_MENGE2")
	private BigDecimal nPreisMenge2;
	@Column(name = "N_PREIS_MENGE3")
	private BigDecimal nPreisMenge3;
	@Column(name = "N_PREIS_MENGE4")
	private BigDecimal nPreisMenge4;
	@Column(name = "N_PREIS_MENGE5")
	private BigDecimal nPreisMenge5;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	public String getCArtikelnrlieferant() {
		return cArtikelnrlieferant;
	}

	public void setCArtikelnrlieferant(String cArtikelnrlieferant) {
		this.cArtikelnrlieferant = cArtikelnrlieferant;
	}

	@Column(name = "B_MENGE1_BESTELLEN")
	private Short bMenge1Bestellen;

	public Short getBMenge1Bestellen() {
		return bMenge1Bestellen;
	}

	public void setBMenge1Bestellen(Short bMenge1Bestellen) {
		this.bMenge1Bestellen = bMenge1Bestellen;
	}

	public Short getBMenge2Bestellen() {
		return bMenge2Bestellen;
	}

	public void setBMenge2Bestellen(Short bMenge2Bestellen) {
		this.bMenge2Bestellen = bMenge2Bestellen;
	}

	public Short getBMenge3Bestellen() {
		return bMenge3Bestellen;
	}

	public void setBMenge3Bestellen(Short bMenge3Bestellen) {
		this.bMenge3Bestellen = bMenge3Bestellen;
	}

	public Short getBMenge4Bestellen() {
		return bMenge4Bestellen;
	}

	public void setBMenge4Bestellen(Short bMenge4Bestellen) {
		this.bMenge4Bestellen = bMenge4Bestellen;
	}

	public Short getBMenge5Bestellen() {
		return bMenge5Bestellen;
	}

	public void setBMenge5Bestellen(Short bMenge5Bestellen) {
		this.bMenge5Bestellen = bMenge5Bestellen;
	}

	@Column(name = "B_MENGE2_BESTELLEN")
	private Short bMenge2Bestellen;

	@Column(name = "B_MENGE3_BESTELLEN")
	private Short bMenge3Bestellen;

	@Column(name = "B_MENGE4_BESTELLEN")
	private Short bMenge4Bestellen;

	@Column(name = "B_MENGE5_BESTELLEN")
	private Short bMenge5Bestellen;

	
	
	public Positionlieferant() {
		super();
	}

	public Positionlieferant(Integer id, Integer einkaufsangebotpositionIId, Integer egaklieferantIId,
			Timestamp tAendern) {
		setIId(id);
		setEinkaufsangebotpositionIId(einkaufsangebotpositionIId);
		setEgaklieferantIId(egaklieferantIId);
		setTAendern(tAendern);
		setBMenge1Bestellen(Helper.boolean2Short(false));
		setBMenge2Bestellen(Helper.boolean2Short(false));
		setBMenge3Bestellen(Helper.boolean2Short(false));
		setBMenge4Bestellen(Helper.boolean2Short(false));
		setBMenge5Bestellen(Helper.boolean2Short(false));

	}

	public Integer getEinkaufsangebotpositionIId() {
		return einkaufsangebotpositionIId;
	}

	public void setEinkaufsangebotpositionIId(Integer einkaufsangebotpositionIId) {
		this.einkaufsangebotpositionIId = einkaufsangebotpositionIId;
	}

	public Integer getEgaklieferantIId() {
		return egaklieferantIId;
	}

	public void setEgaklieferantIId(Integer egaklieferantIId) {
		this.egaklieferantIId = egaklieferantIId;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

}
