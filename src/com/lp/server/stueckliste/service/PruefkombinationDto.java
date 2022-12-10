package com.lp.server.stueckliste.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;

public class PruefkombinationDto implements Serializable {
	private Integer iId;

	private Integer artikelIIdKontakt;
	private Integer artikelIIdLitze;

	public PruefkombinationsprDto getPruefkombinationsprDto() {
		return pruefkombinationsprDto;
	}

	public void setPruefkombinationsprDto(
			PruefkombinationsprDto pruefkombinationsprDto) {
		this.pruefkombinationsprDto = pruefkombinationsprDto;
	}

	private PruefkombinationsprDto pruefkombinationsprDto;
	
	private Integer verschleissteilIId;

	private Short bStandard;
	private BigDecimal nCrimphoeheDraht;
	private BigDecimal nCrimphoeheIsolation;
	private BigDecimal nCrimpbreitDraht;
	private Timestamp tAnlegen;
	private Timestamp tAendern;
	private Integer personalIIdAendern;
	private Integer personalIIdAnlegen;

	private BigDecimal nToleranzCrimphoeheDraht;
	private BigDecimal nToleranzCrimphoeheIsolation;
	private BigDecimal nToleranzCrimpbreitDraht;
	private BigDecimal nToleranzCrimpbreiteIsolation;
	private BigDecimal nWert;
	private BigDecimal nToleranzWert;

	public BigDecimal getNToleranzCrimphoeheDraht() {
		return nToleranzCrimphoeheDraht;
	}

	public void setNToleranzCrimphoeheDraht(BigDecimal nToleranzCrimphoeheDraht) {
		this.nToleranzCrimphoeheDraht = nToleranzCrimphoeheDraht;
	}

	public BigDecimal getNToleranzCrimphoeheIsolation() {
		return nToleranzCrimphoeheIsolation;
	}

	public void setNToleranzCrimphoeheIsolation(
			BigDecimal nToleranzCrimphoeheIsolation) {
		this.nToleranzCrimphoeheIsolation = nToleranzCrimphoeheIsolation;
	}

	public BigDecimal getNToleranzCrimpbreitDraht() {
		return nToleranzCrimpbreitDraht;
	}

	public void setNToleranzCrimpbreitDraht(BigDecimal nToleranzCrimpbreitDraht) {
		this.nToleranzCrimpbreitDraht = nToleranzCrimpbreitDraht;
	}

	public BigDecimal getNToleranzCrimpbreiteIsolation() {
		return nToleranzCrimpbreiteIsolation;
	}

	public void setNToleranzCrimpbreiteIsolation(
			BigDecimal nToleranzCrimpbreiteIsolation) {
		this.nToleranzCrimpbreiteIsolation = nToleranzCrimpbreiteIsolation;
	}

	public BigDecimal getNWert() {
		return nWert;
	}

	public void setNWert(BigDecimal nWert) {
		this.nWert = nWert;
	}

	public BigDecimal getNToleranzWert() {
		return nToleranzWert;
	}

	public void setNToleranzWert(BigDecimal nToleranzWert) {
		this.nToleranzWert = nToleranzWert;
	}

	private Integer pruefartIId;

	public Integer getPruefartIId() {
		return pruefartIId;
	}

	public void setPruefartIId(Integer pruefartIId) {
		this.pruefartIId = pruefartIId;
	}

	private BigDecimal nCrimpbreiteIsolation;

	public BigDecimal getNCrimpbreiteIsolation() {
		return nCrimpbreiteIsolation;
	}

	public void setNCrimpbreiteIsolation(BigDecimal nCrimpbreiteIsolation) {
		this.nCrimpbreiteIsolation = nCrimpbreiteIsolation;
	}

	public Integer getArtikelIIdKontakt() {
		return artikelIIdKontakt;
	}

	public void setArtikelIIdKontakt(Integer artikelIIdKontakt) {
		this.artikelIIdKontakt = artikelIIdKontakt;
	}

	public Integer getArtikelIIdLitze() {
		return artikelIIdLitze;
	}

	public void setArtikelIIdLitze(Integer artikelIIdLitze) {
		this.artikelIIdLitze = artikelIIdLitze;
	}

	private Short bDoppelanschlag;
	private BigDecimal nAbzugskraftLitze;
	private BigDecimal nAbzugskraftLitze2;
	private Integer artikelIIdLitze2;
	
	public Integer getArtikelIIdLitze2() {
		return artikelIIdLitze2;
	}

	public void setArtikelIIdLitze2(Integer artikelIIdLitze2) {
		this.artikelIIdLitze2 = artikelIIdLitze2;
	}

	public Integer getVerschleissteilIId() {
		return verschleissteilIId;
	}

	public void setVerschleissteilIId(Integer verschleissteilIId) {
		this.verschleissteilIId = verschleissteilIId;
	}

	public Short getBDoppelanschlag() {
		return bDoppelanschlag;
	}

	public void setBDoppelanschlag(Short bDoppelanschlag) {
		this.bDoppelanschlag = bDoppelanschlag;
	}

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

	public Short getBStandard() {
		return bStandard;
	}

	public void setBStandard(Short bStandard) {
		this.bStandard = bStandard;
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

	private static final long serialVersionUID = 1L;

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}
}
