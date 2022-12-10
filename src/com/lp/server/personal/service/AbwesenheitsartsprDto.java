package com.lp.server.personal.service;

import java.io.Serializable;

public class AbwesenheitsartsprDto  implements Serializable  {
	private static final long serialVersionUID = 1L;
	private String localeCNr;
	private Integer abwesenheitsartIId;
	private String cBez;

	public String getLocaleCNr() {
		return localeCNr;
	}

	public void setLocaleCNr(String localeCNr) {
		this.localeCNr = localeCNr;
	}

	public String getCBez() {
		return cBez;
	}

	public Integer getAbwesenheitsartIId() {
		return abwesenheitsartIId;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public void setAbwesenheitsartIId(Integer abwesenheitsartIId) {
		this.abwesenheitsartIId = abwesenheitsartIId;
	}
}
