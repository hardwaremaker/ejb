package com.lp.server.system.service;

import java.io.Serializable;

public class BelegartmediaDto implements Serializable {
	private Integer iId;
	private Integer usecaseId;
	private Integer iKey;
	private Integer iSort;
	private String cBez;
	private String datenformatCNr;
	private String xText;
	private Integer iAusrichtung;
	private byte[] oMedia;

	private static final long serialVersionUID = 1L;

	public Integer getUsecaseId() {
		return usecaseId;
	}

	public void setUsecaseId(Integer usecaseId) {
		this.usecaseId = usecaseId;
	}

	public Integer getIKey() {
		return iKey;
	}

	public void setIKey(Integer iKey) {
		this.iKey = iKey;
	}

	public String getCBez() {
		return cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public String getXText() {
		return xText;
	}

	public void setXText(String xText) {
		this.xText = xText;
	}

	public Integer getIAusrichtung() {
		return iAusrichtung;
	}

	public void setIAusrichtung(Integer iAusrichtung) {
		this.iAusrichtung = iAusrichtung;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getDatenformatCNr() {
		return this.datenformatCNr;
	}

	public void setDatenformatCNr(String datenformatCNr) {
		this.datenformatCNr = datenformatCNr;
	}

	public Integer getISort() {
		return iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public byte[] getOMedia() {
		return this.oMedia;
	}

	public void setOMedia(byte[] oMedia) {
		this.oMedia = oMedia;
	}
	
	private String cDateiname;

	public String getCDateiname() {
		return cDateiname;
	}

	public void setCDateiname(String cDateiname) {
		this.cDateiname = cDateiname;
	}

	
}
