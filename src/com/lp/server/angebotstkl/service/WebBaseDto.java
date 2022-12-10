package com.lp.server.angebotstkl.service;

public class WebBaseDto {

	private Boolean bDtosLoaded;
	
	public WebBaseDto() {
		bDtosLoaded = false;
	}
	
	public Boolean areDtosLoaded() {
		return bDtosLoaded;
	}

	public void setDtosLoaded(Boolean value) {
		bDtosLoaded = value;
	}
}
