package com.lp.server.artikel.service;

import java.io.Serializable;

public class VerschleissteilDto implements Serializable{
	/**
	 * 
	 */
	private Integer iId;
	private String cNr;
	
	private String cBez;
	


	public String getCBez2() {
		return cBez2;
	}

	public void setCBez2(String cBez2) {
		this.cBez2 = cBez2;
	}

	private String cBez2;

	private static final long serialVersionUID = 1L;

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getBezeichnung() {
		StringBuffer sbBez = new StringBuffer();
		
		sbBez.append(getCNr());
		
		if (getCBez() != null) {
			if (getCBez() != null
					&& getCBez().length() > 0) {
				sbBez.append(" "+ getCBez());
			} 
		} 
		return sbBez.toString();
	}
	
	public String getCBez() {
		return this.cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public String getCNr() {
		return cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	
}
