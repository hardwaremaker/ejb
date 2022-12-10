package com.lp.server.angebotstkl.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;

import com.lp.util.Helper;

public class PositionlieferantDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iId;

	private Integer einkaufsangebotpositionIId;

	private Integer egaklieferantIId;

	private String cArtikelnrlieferant;

	public String getCArtikelnrlieferant() {
		return cArtikelnrlieferant;
	}

	public void setCArtikelnrlieferant(String cArtikelnrlieferant) {
		this.cArtikelnrlieferant = cArtikelnrlieferant;
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

	private String cBemerkung;

	private Integer iLieferzeitinkw;

	private BigDecimal nLagerstand;

	private BigDecimal nVerpackungseinheit;

	private BigDecimal nMindestbestellmenge;

	public String getCBemerkung() {
		return cBemerkung;
	}

	public void setCBemerkung(String cBemerkung) {
		this.cBemerkung = cBemerkung;
	}

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

	private String cBemerkungVerkauf;

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

	private BigDecimal nTransportkosten;
	private BigDecimal nPreisMenge1;
	private BigDecimal nPreisMenge2;
	private BigDecimal nPreisMenge3;
	private BigDecimal nPreisMenge4;
	private BigDecimal nPreisMenge5;
	private Timestamp tAendern;

	private Short bMenge1Bestellen = Helper.boolean2Short(false);

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

	private Short bMenge2Bestellen = Helper.boolean2Short(false);
	private Short bMenge3Bestellen = Helper.boolean2Short(false);
	private Short bMenge4Bestellen = Helper.boolean2Short(false);
	private Short bMenge5Bestellen = Helper.boolean2Short(false);

}
