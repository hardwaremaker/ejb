package com.lp.server.stueckliste.service;

import java.io.Serializable;
import java.math.BigDecimal;

public class AlternativmaschineDto implements Serializable {
	public Integer getStuecklistearbeitsplanIId() {
		return stuecklistearbeitsplanIId;
	}

	public void setStuecklistearbeitsplanIId(Integer stuecklistearbeitsplanIId) {
		this.stuecklistearbeitsplanIId = stuecklistearbeitsplanIId;
	}

	public Integer getMaschineIId() {
		return maschineIId;
	}

	public void setMaschineIId(Integer maschineIId) {
		this.maschineIId = maschineIId;
	}

	public BigDecimal getNKorrekturfaktor() {
		return nKorrekturfaktor;
	}

	public void setNKorrekturfaktor(BigDecimal nKorrekturfaktor) {
		this.nKorrekturfaktor = nKorrekturfaktor;
	}

	public Integer getISort() {
		return iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	private Integer iId;

	private Integer stuecklistearbeitsplanIId;

	private Integer maschineIId;

	private BigDecimal nKorrekturfaktor;

	private Integer iSort;

	private static final long serialVersionUID = 1L;

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

}
