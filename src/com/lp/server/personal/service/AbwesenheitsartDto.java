package com.lp.server.personal.service;

import java.io.Serializable;

public class AbwesenheitsartDto  implements Serializable {
	private Integer iId;
	private Integer iSort;
	public Integer getIiSort() {
		return iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	private String cNr;
	private AbwesenheitsartsprDto sprDto;

	public AbwesenheitsartsprDto getAbwesenheitsartsprDto() {
		return sprDto;
	}

	public void setAbwesenheitsartsprDto(AbwesenheitsartsprDto sprDto) {
		this.sprDto = sprDto;
	}

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String formatBezeichnung() {
		String bez = getCNr();
		if (getAbwesenheitsartsprDto() != null && getAbwesenheitsartsprDto().getCBez() != null) {
			bez = " " + getAbwesenheitsartsprDto().getCBez();
		}
		return bez;
	}
	
	public String getBezeichnung() {
		if (getAbwesenheitsartsprDto() != null && getAbwesenheitsartsprDto().getCBez() != null) {
			return getAbwesenheitsartsprDto().getCBez();
		} else {
			return getCNr();
		}
	}

	public String getCNr() {
		return cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}
}
