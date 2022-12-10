package com.lp.server.angebotstkl.service;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;

public class AgstklmengenstaffelSchnellerfassungDto implements Serializable{
	private Integer iId;
	private Integer agstklIId;
	private BigDecimal nMenge;
	private BigDecimal nAufschlagAz;

	
	private BigDecimal nWertMaterial;
	public BigDecimal getNWertMaterial() {
		return nWertMaterial;
	}

	public void setNWertMaterial(BigDecimal nWertMaterial) {
		this.nWertMaterial = nWertMaterial;
	}

	public BigDecimal getNWertAz() {
		return nWertAz;
	}

	public void setNWertAz(BigDecimal nWertAz) {
		this.nWertAz = nWertAz;
	}

	public BigDecimal getNPreisEinheit() {
		return nPreisEinheit;
	}

	public void setNPreisEinheit(BigDecimal nPreisEinheit) {
		this.nPreisEinheit = nPreisEinheit;
	}

	private BigDecimal nWertAz;
	private BigDecimal nPreisEinheit;
	
	public BigDecimal getNAufschlagAz() {
		return nAufschlagAz;
	}

	public void setNAufschlagAz(BigDecimal nAufschlagAz) {
		this.nAufschlagAz = nAufschlagAz;
	}

	public BigDecimal getNAufschlagMaterial() {
		return nAufschlagMaterial;
	}

	public void setNAufschlagMaterial(BigDecimal nAufschlagMaterial) {
		this.nAufschlagMaterial = nAufschlagMaterial;
	}

	private BigDecimal nAufschlagMaterial;

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	private static final long serialVersionUID = 1L;


	public Integer getAgstklIId() {
		return agstklIId;
	}

	public void setAgstklIId(Integer agstklIId) {
		this.agstklIId = agstklIId;
	}

	public BigDecimal getNMenge() {
		return this.nMenge;
	}

	public void setNMenge(BigDecimal nMenge) {
		this.nMenge = nMenge;
	}

}
