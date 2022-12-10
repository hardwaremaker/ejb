package com.lp.server.partner.service;

import java.io.Serializable;

public class DsgvotextDto implements Serializable {
	private Integer iId;
	private String mandantCNr;
	private Integer iSort;
	private Integer dsgvokategorieIId;
	private Short bKopftext;

	private static final long serialVersionUID = 1L;

	private String xInhalt;

	public String getXInhalt() {
		return xInhalt;
	}

	public void setXInhalt(String xInhalt) {
		this.xInhalt = xInhalt;
	}

	public Integer getISort() {
		return iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public Integer getDsgvokategorieIId() {
		return dsgvokategorieIId;
	}

	public void setDsgvokategorieIId(Integer dsgvokategorieIId) {
		this.dsgvokategorieIId = dsgvokategorieIId;
	}

	public Short getBKopftext() {
		return bKopftext;
	}

	public void setBKopftext(Short bKopftext) {
		this.bKopftext = bKopftext;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}
}
