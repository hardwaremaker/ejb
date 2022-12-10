package com.lp.server.fertigung.service;

import java.io.Serializable;
import java.sql.Timestamp;

public class LospruefplanDto implements Serializable {

	private Integer iId;
	private Integer losIId;
	private Integer iSort;
	private Integer lossollmaterialIIdKontakt;
	private Integer lossollmaterialIIdLitze;

	private Integer lossollmaterialIIdLitze2;

	public Integer getLossollmaterialIIdLitze2() {
		return lossollmaterialIIdLitze2;
	}

	public void setLossollmaterialIIdLitze2(Integer lossollmaterialIIdLitze2) {
		this.lossollmaterialIIdLitze2 = lossollmaterialIIdLitze2;
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

	private Integer verschleissteilIId;

	public Integer getVerschleissteilIId() {
		return verschleissteilIId;
	}

	public void setVerschleissteilIId(Integer verschleissteilIId) {
		this.verschleissteilIId = verschleissteilIId;
	}

	public Integer getLosIId() {
		return losIId;
	}

	public void setLosIId(Integer losIId) {
		this.losIId = losIId;
	}

	public Integer getLossollmaterialIIdKontakt() {
		return lossollmaterialIIdKontakt;
	}

	public void setLossollmaterialIIdKontakt(Integer lossollmaterialIIdKontakt) {
		this.lossollmaterialIIdKontakt = lossollmaterialIIdKontakt;
	}

	public Integer getLossollmaterialIIdLitze() {
		return lossollmaterialIIdLitze;
	}

	public void setLossollmaterialIIdLitze(Integer lossollmaterialIIdLitze) {
		this.lossollmaterialIIdLitze = lossollmaterialIIdLitze;
	}

	public Integer getWerkzeugIId() {
		return werkzeugIId;
	}

	public void setWerkzeugIId(Integer werkzeugIId) {
		this.werkzeugIId = werkzeugIId;
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

	private Integer werkzeugIId;
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
