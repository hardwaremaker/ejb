package com.lp.server.finanz.service;

import java.io.Serializable;

public class ReversechargeartDto implements Serializable {
	private static final long serialVersionUID = -383105253411965271L;

	private String cNr ;
	private Integer iId ;
	private Integer iSort ;
	private String mandantCNr ;
	private ReversechargeartsprDto sprDto ;
	private Boolean bVersteckt;
	
	public String getCNr() {
		return this.cNr;
	}
	
	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public Integer getISort() {
		return this.iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getIId() {
		return iId;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public ReversechargeartsprDto getSprDto() {
		return sprDto;
	}

	public void setSprDto(ReversechargeartsprDto sprDto) {
		this.sprDto = sprDto;
	}
	
	public void setBVersteckt(Boolean bVersteckt) {
		this.bVersteckt = bVersteckt;
	}
	
	public Boolean getBVersteckt() {
		return bVersteckt;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cNr == null) ? 0 : cNr.hashCode());
		result = prime * result + ((iId == null) ? 0 : iId.hashCode());
		result = prime * result + ((iSort == null) ? 0 : iSort.hashCode());
		result = prime * result
				+ ((mandantCNr == null) ? 0 : mandantCNr.hashCode());
		result = prime * result + ((bVersteckt == null) ? 0 : bVersteckt.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReversechargeartDto other = (ReversechargeartDto) obj;
		if (cNr == null) {
			if (other.cNr != null)
				return false;
		} else if (!cNr.equals(other.cNr))
			return false;
		if (iId == null) {
			if (other.iId != null)
				return false;
		} else if (!iId.equals(other.iId))
			return false;
		if (iSort == null) {
			if (other.iSort != null)
				return false;
		} else if (!iSort.equals(other.iSort))
			return false;
		if (mandantCNr == null) {
			if (other.mandantCNr != null)
				return false;
		} else if (!mandantCNr.equals(other.mandantCNr))
			return false;
		if (bVersteckt == null) {
			if (other.bVersteckt != null)
				return false;
		} else if (!bVersteckt.equals(other.bVersteckt))
			return false;
		return true;
	}	
}
