package com.lp.server.partner.service;

import java.io.Serializable;
import java.sql.Timestamp;



public class BeauskunftungDto implements Serializable {

	
	private Integer iId;

	
	private Timestamp tAnlegen;


	private Short bKostenpflichtig;


	private Integer partnerIId;

	
	private Integer identifikationIId;
	public Integer getIdentifikationIId() {
		return identifikationIId;
	}

	public void setIdentifikationIId(Integer identifikationIId) {
		this.identifikationIId = identifikationIId;
	}


	private Integer personalIIdAnlegen;

	private static final long serialVersionUID = 1L;

	

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Timestamp getTAnlegen() {
		return this.tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Short getBKostenpflichtig() {
		return this.bKostenpflichtig;
	}

	public void setBKostenpflichtig(Short bKostenpflichtig) {
		this.bKostenpflichtig = bKostenpflichtig;
	}

	public Integer getPartnerIId() {
		return this.partnerIId;
	}

	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId;
	}

	public Integer getPersonalIIdAnlegen() {
		return this.personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

}
