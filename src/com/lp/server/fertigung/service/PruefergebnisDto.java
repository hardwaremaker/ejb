package com.lp.server.fertigung.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class PruefergebnisDto implements Serializable {

	private Integer iId;
	private Integer losablieferungIId;
	private Integer lospruefplanIId;
	private BigDecimal nCrimphoeheDraht;
	private BigDecimal nCrimphoeheIsolation;
	private BigDecimal nCrimpbreitDraht;

	private BigDecimal nWert;
	private Short bWert;
	private String cWert;
	
	public BigDecimal getNWert() {
		return nWert;
	}

	public void setNWert(BigDecimal nWert) {
		this.nWert = nWert;
	}

	public Short getBWert() {
		return bWert;
	}

	public void setBWert(Short bWert) {
		this.bWert = bWert;
	}
	
	private BigDecimal nAbzugskraftLitze;
	
	private BigDecimal nAbzugskraftLitze2;
	
	public BigDecimal getNAbzugskraftLitze() {
		return nAbzugskraftLitze;
	}

	public void setNAbzugskraftLitze(BigDecimal nAbzugskraftLitze) {
		this.nAbzugskraftLitze = nAbzugskraftLitze;
	}

	public BigDecimal getNAbzugskraftLitze2() {
		return nAbzugskraftLitze2;
	}

	public void setNAbzugskraftLitze2(BigDecimal nAbzugskraftLitze2) {
		this.nAbzugskraftLitze2 = nAbzugskraftLitze2;
	}
	
	private BigDecimal nCrimpbreiteIsolation;

	public BigDecimal getNCrimpbreiteIsolation() {
		return nCrimpbreiteIsolation;
	}

	public void setNCrimpbreiteIsolation(BigDecimal nCrimpbreiteIsolation) {
		this.nCrimpbreiteIsolation = nCrimpbreiteIsolation;
	}

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	private Timestamp tAnlegen;
	private Timestamp tAendern;
	private Integer personalIIdAendern;
	private Integer personalIIdAnlegen;

	private static final long serialVersionUID = 1L;

	public Integer getLosablieferungIId() {
		return losablieferungIId;
	}

	public void setLosablieferungIId(Integer losablieferungIId) {
		this.losablieferungIId = losablieferungIId;
	}

	public Integer getLospruefplanIId() {
		return lospruefplanIId;
	}

	public void setLospruefplanIId(Integer lospruefplanIId) {
		this.lospruefplanIId = lospruefplanIId;
	}

	public BigDecimal getNCrimphoeheDraht() {
		return nCrimphoeheDraht;
	}

	public void setNCrimphoeheDraht(BigDecimal nCrimphoeheDraht) {
		this.nCrimphoeheDraht = nCrimphoeheDraht;
	}

	public BigDecimal getNCrimphoeheIsolation() {
		return nCrimphoeheIsolation;
	}

	public void setNCrimphoeheIsolation(BigDecimal nCrimphoeheIsolation) {
		this.nCrimphoeheIsolation = nCrimphoeheIsolation;
	}

	public BigDecimal getNCrimpbreitDraht() {
		return nCrimpbreitDraht;
	}

	public void setNCrimpbreitDraht(BigDecimal nCrimpbreitDraht) {
		this.nCrimpbreitDraht = nCrimpbreitDraht;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCWert() {
		return cWert;
	}
	
	public void setCWert(String cWert) {
		this.cWert = cWert;
	}
}
