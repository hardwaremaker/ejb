package com.lp.server.personal.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class MaschineleistungsfaktorDto implements Serializable {
	private Integer iId;

	private Integer maschineIId;

	public Integer getMaschineIId() {
		return maschineIId;
	}

	public void setMaschineIId(Integer maschineIId) {
		this.maschineIId = maschineIId;
	}

	private Integer materialIId;

	private Timestamp tGueltigab;
	private BigDecimal nFaktorInProzent;

	public BigDecimal getNFaktorInProzent() {
		return nFaktorInProzent;
	}

	public void setNFaktorInProzent(BigDecimal nFaktorInProzent) {
		this.nFaktorInProzent = nFaktorInProzent;
	}

	private static final long serialVersionUID = 1L;

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Timestamp getTGueltigab() {
		return this.tGueltigab;
	}

	public void setTGueltigab(Timestamp tGueltigab) {
		this.tGueltigab = tGueltigab;
	}

	public Integer getMaterialIId() {
		return this.materialIId;
	}

	public void setMaterialIId(Integer materialIId) {
		this.materialIId = materialIId;
	}
}
