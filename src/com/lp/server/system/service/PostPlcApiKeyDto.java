package com.lp.server.system.service;

import java.io.Serializable;

public class PostPlcApiKeyDto implements Serializable {
	private static final long serialVersionUID = -645584014509127205L;

	private Integer clientID;
	private Integer orgUnitID;
	private String orgUnitGUID;
	private String url;
	private boolean valid;
	private String parameter;
	
	public PostPlcApiKeyDto(String parameterString) {
		this.parameter = parameterString;
		parse();
	}
	
	public boolean isValid() {
		return valid;
	}
	
	public Integer getClientID() {
		return this.clientID;
	}
	
	public Integer getOrgUnitID() {
		return this.orgUnitID;
	}
	
	public String getOrgUnitGUID() {
		return this.orgUnitGUID;
	}
	
	public String getParameter() {
		return this.parameter;
	}
	
	private boolean parse() {
		if(this.parameter == null) return false;
		
		String[] ids = parameter.split(";");
		if(ids.length != 4) return false;
		
		try {
			this.clientID = Integer.parseInt(ids[0]);
			this.orgUnitID = Integer.parseInt(ids[1]);
			this.orgUnitGUID = ids[2];
			this.url = ids[3];
			this.valid = true;
			return this.valid;
		} catch(NumberFormatException e) {
			return false;
		}		
	}

	public String getUri() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
