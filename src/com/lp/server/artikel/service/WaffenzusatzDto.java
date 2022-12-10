package com.lp.server.artikel.service;

import java.io.Serializable;

public class WaffenzusatzDto implements Serializable {
	private Integer iId;

	private String cNr;

	public String getCNr() {
		return cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	private String cBez;

	private static final long serialVersionUID = 1L;

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCBez() {
		return this.cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}
	public String formatKennungBezeichnung() {
		StringBuffer sbBez = new StringBuffer();
		
		sbBez.append(getCNr());
		
		if (getCBez() != null) {
			sbBez.append(" " + getCBez());
		}
		return sbBez.toString();
	}
}
