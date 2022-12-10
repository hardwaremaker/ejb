package com.lp.server.partner.service;

import com.lp.server.angebotstkl.service.WebpartnerDto;

public class WeblieferantFarnellDto extends WebpartnerDto {
	private static final long serialVersionUID = -5488956170476509779L;

	private String cUrl;
	private String cApiKey;
	private String cCustomerId;
	private String cCustomerKey;
	private String cStore;
	
	public WeblieferantFarnellDto() {
	}

	public String getCUrl() {
		return cUrl;
	}
	
	public void setCUrl(String cUrl) {
		this.cUrl = cUrl;
	}

	public String getCApiKey() {
		return cApiKey;
	}
	
	public void setCApiKey(String cApiKey) {
		this.cApiKey = cApiKey;
	}
	
	public String getCCustomerId() {
		return cCustomerId;
	}
	
	public void setCCustomerId(String cCustomerId) {
		this.cCustomerId = cCustomerId;
	}
	
	public String getCCustomerKey() {
		return cCustomerKey;
	}
	
	public void setCCustomerKey(String cCustomerKey) {
		this.cCustomerKey = cCustomerKey;
	}
	
	public String getCStore() {
		return cStore;
	}
	
	public void setCStore(String cStore) {
		this.cStore = cStore;
	}
}
