package com.lp.server.angebot.service;

import java.io.Serializable;

public class AngebotauftragDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Integer auftragIId;
	public Integer getiId() {
		return iId;
	}

	public void setiId(Integer iId) {
		this.iId = iId;
	}

	public Integer getAuftragIId() {
		return auftragIId;
	}

	public void setAuftragIId(Integer auftragIId) {
		this.auftragIId = auftragIId;
	}

	public Integer getAngebotIId() {
		return angebotIId;
	}

	public void setAngebotIId(Integer angebotIId) {
		this.angebotIId = angebotIId;
	}

	private Integer angebotIId;
}
