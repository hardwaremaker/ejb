package com.lp.server.system.service;

import java.io.Serializable;

public class VerfuegbareHostsDto implements Serializable {
	private static final long serialVersionUID = -4032350346089997428L;
	
	private String hostname;
	private String mandantCNr;
	private Integer port;
	private String benutzerCNr;
	
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public String getMandantCNr() {
		return mandantCNr;
	}
	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getBenutzerCNr() {
		return benutzerCNr;
	}
	public void setBenutzerCNr(String benutzerCNr) {
		this.benutzerCNr = benutzerCNr;
	}
}
