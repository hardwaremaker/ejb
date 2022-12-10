package com.lp.server.artikel.service;

import java.io.Serializable;
import java.math.BigDecimal;

public class VerpackungsmittelDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String cNr;
	private String mandantCNr;

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public String getBezeichnung() {
		if (getVerpackungsmittelsprDto() != null) {
			if (getVerpackungsmittelsprDto().getCBez() != null) {
				return getVerpackungsmittelsprDto().getCBez();
			} else {
				return getCNr().trim();
			}
		} else {
			return getCNr().trim();
		}
	}

	private VerpackungsmittelsprDto verpackungsmittelsprDto;

	public VerpackungsmittelsprDto getVerpackungsmittelsprDto() {
		return verpackungsmittelsprDto;
	}

	public void setVkfortschrittsprDto(
			VerpackungsmittelsprDto verpackungsmittelsprDto) {
		this.verpackungsmittelsprDto = verpackungsmittelsprDto;
	}

	public String getCNr() {
		return cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	private BigDecimal nGewichtInKG;
	public BigDecimal getNGewichtInKG() {
		return nGewichtInKG;
	}

	public void setNGewichtInKG(BigDecimal nGewichtInKG) {
		this.nGewichtInKG = nGewichtInKG;
	}
}
