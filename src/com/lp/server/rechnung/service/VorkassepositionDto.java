package com.lp.server.rechnung.service;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;

public class VorkassepositionDto implements Serializable {
	private Integer iId;

	@Column(name = "RECHNUNG_I_ID")
	private Integer rechnungIId;

	public Integer getRechnungIId() {
		return rechnungIId;
	}

	public void setRechnungIId(Integer rechnungIId) {
		this.rechnungIId = rechnungIId;
	}

	public Integer getAuftragspositionIId() {
		return auftragspositionIId;
	}

	public void setAuftragspositionIId(Integer auftragspositionIId) {
		this.auftragspositionIId = auftragspositionIId;
	}

	public BigDecimal getNBetrag() {
		return nBetrag;
	}

	public void setNBetrag(BigDecimal nBetrag) {
		this.nBetrag = nBetrag;
	}

	private Integer auftragspositionIId;
	private BigDecimal nBetrag;

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}
}
