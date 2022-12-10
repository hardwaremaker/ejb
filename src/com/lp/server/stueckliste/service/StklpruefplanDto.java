package com.lp.server.stueckliste.service;

import java.io.Serializable;
import java.sql.Timestamp;

public class StklpruefplanDto implements Serializable {

	private Integer iId;
	private Integer stuecklisteId;
	private Integer stuecklistepositionIIdKontakt;
	private Integer stuecklistepositionIIdLitze;

	private Integer pruefkombinationId;
	
	public Integer getPruefkombinationId() {
		return pruefkombinationId;
	}

	public void setPruefkombinationId(Integer pruefkombinationId) {
		this.pruefkombinationId = pruefkombinationId;
	}

	
	private Integer pruefartIId;

	public Integer getPruefartIId() {
		return pruefartIId;
	}

	public void setPruefartIId(Integer pruefartIId) {
		this.pruefartIId = pruefartIId;
	}

	private Integer stuecklistepositionIIdLitze2;
	private Integer iSort;

	public Integer getStuecklistepositionIIdLitze2() {
		return stuecklistepositionIIdLitze2;
	}

	public void setStuecklistepositionIIdLitze2(
			Integer stuecklistepositionIIdLitze2) {
		this.stuecklistepositionIIdLitze2 = stuecklistepositionIIdLitze2;
	}

	public Integer getISort() {
		return iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	private Short bDoppelanschlag;

	public Short getBDoppelanschlag() {
		return bDoppelanschlag;
	}

	public void setBDoppelanschlag(Short bDoppelanschlag) {
		this.bDoppelanschlag = bDoppelanschlag;
	}

	private Integer verschleissteilIId;

	public Integer getVerschleissteilIId() {
		return verschleissteilIId;
	}

	public void setVerschleissteilIId(Integer verschleissteilIId) {
		this.verschleissteilIId = verschleissteilIId;
	}

	public Integer getStuecklisteId() {
		return stuecklisteId;
	}

	public void setStuecklisteId(Integer stuecklisteId) {
		this.stuecklisteId = stuecklisteId;
	}

	public Integer getStuecklistepositionIIdKontakt() {
		return stuecklistepositionIIdKontakt;
	}

	public void setStuecklistepositionIIdKontakt(
			Integer stuecklistepositionIIdKontakt) {
		this.stuecklistepositionIIdKontakt = stuecklistepositionIIdKontakt;
	}

	public Integer getStuecklistepositionIIdLitze() {
		return stuecklistepositionIIdLitze;
	}

	public void setStuecklistepositionIIdLitze(
			Integer stuecklistepositionIIdLitze) {
		this.stuecklistepositionIIdLitze = stuecklistepositionIIdLitze;
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

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

}
