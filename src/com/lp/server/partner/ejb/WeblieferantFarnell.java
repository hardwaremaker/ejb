package com.lp.server.partner.ejb;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.lp.server.angebotstkl.ejb.Webpartner;
import com.lp.server.system.service.ITablenames;

@Entity
@Table(name = ITablenames.PART_WEBLIEFERANTFARNELL)
@PrimaryKeyJoinColumn(name = "WEBPARTNER_I_ID", referencedColumnName = "I_ID")
public class WeblieferantFarnell extends Webpartner {
	private static final long serialVersionUID = -5747511607696520819L;
	
	@Column(name = "C_APIKEY")
	private String cApiKey;

	@Column(name = "C_URL")
	private String cUrl;
	
	@Column(name = "C_CUSTOMERID")
	private String cCustomerId;
	
	@Column(name = "C_CUSTOMERKEY")
	private String cCustomerKey;
	
	@Column(name = "C_STORE")
	private String cStore;
	
	public WeblieferantFarnell() {
	}

	public String getCApiKey() {
		return cApiKey;
	}
	
	public void setCApiKey(String cApiKey) {
		this.cApiKey = cApiKey;
	}
	
	public String getCUrl() {
		return cUrl;
	}
	
	public void setCUrl(String cUrl) {
		this.cUrl = cUrl;
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
