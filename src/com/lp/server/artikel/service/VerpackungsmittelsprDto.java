package com.lp.server.artikel.service;

import java.io.Serializable;

public class VerpackungsmittelsprDto  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String localeCNr;
	private Integer verpackungsmittelIId;
	public Integer getVerpackungsmittelIId() {
		return verpackungsmittelIId;
	}

	public void setVerpackungsmittelIId(Integer verpackungsmittelIId) {
		this.verpackungsmittelIId = verpackungsmittelIId;
	}

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

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}
}
