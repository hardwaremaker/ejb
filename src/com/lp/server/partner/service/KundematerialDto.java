package com.lp.server.partner.service;

import java.io.Serializable;
import java.math.BigDecimal;

public class KundematerialDto implements Serializable {
	private Integer iId;
	private Integer kundeIId;

	public Integer getMaterialIId() {
		return materialIId;
	}

	public void setMaterialIId(Integer materialIId) {
		this.materialIId = materialIId;
	}

	public Integer getMaterialIIdNotierung() {
		return materialIIdNotierung;
	}

	public void setMaterialIIdNotierung(Integer materialIIdNotierung) {
		this.materialIIdNotierung = materialIIdNotierung;
	}

	public BigDecimal getNMaterialbasis() {
		return nMaterialbasis;
	}

	public void setNMaterialbasis(BigDecimal nMaterialbasis) {
		this.nMaterialbasis = nMaterialbasis;
	}

	public BigDecimal getNAufschlagBetrag() {
		return nAufschlagBetrag;
	}

	public void setNAufschlagBetrag(BigDecimal nAufschlagBetrag) {
		this.nAufschlagBetrag = nAufschlagBetrag;
	}

	public Short getBMaterialInklusive() {
		return bMaterialInklusive;
	}

	public void setBMaterialInklusive(Short bMaterialInklusive) {
		this.bMaterialInklusive = bMaterialInklusive;
	}

	public Double getFAufschlagProzent() {
		return fAufschlagProzent;
	}

	public void setFAufschlagProzent(Double fAufschlagProzent) {
		this.fAufschlagProzent = fAufschlagProzent;
	}

	private Integer materialIId;
	private Integer materialIIdNotierung;

	private BigDecimal nMaterialbasis;
	private BigDecimal nAufschlagBetrag;

	private Short bMaterialInklusive;

	private Double fAufschlagProzent;

	private static final long serialVersionUID = 1L;

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getKundeIId() {
		return this.kundeIId;
	}

	public void setKundeIId(Integer kundeIId) {
		this.kundeIId = kundeIId;
	}
}
