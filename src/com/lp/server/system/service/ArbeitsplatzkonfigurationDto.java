package com.lp.server.system.service;

import java.io.Serializable;

public class ArbeitsplatzkonfigurationDto implements Serializable {
	private static final long serialVersionUID = 327804675137830738L;

	private Integer arbeitsplatzIId ;	
	private String cSystem ;
	private String cUser ;
	
	public Integer getArbeitsplatzIId() {
		return arbeitsplatzIId;
	}
	public void setArbeitsplatzIId(Integer arbeitsplatzIId) {
		this.arbeitsplatzIId = arbeitsplatzIId;
	}
	public String getCSystem() {
		return cSystem ;
	}
	
	public void setCSystem(String cSystem) {
		this.cSystem = cSystem ;		
	}
	
	public String getCUser() {
		return cUser;
	}

	public void setCUser(String cUser) {
		this.cUser = cUser;
	}
}
