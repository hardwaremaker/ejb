package com.lp.server.system.service;

import java.io.Serializable;

public class VersandwegPartnerEdi4AllDto extends VersandwegPartnerDto implements Serializable {
	private static final long serialVersionUID = 8299958607064841606L;

	private String cExportPfad;
	private String c00E;
	private String c094;
	
	public String getCExportPfad() {
		return cExportPfad;
	}

	public void setCExportPfad(String cExportPfad) {
		this.cExportPfad = cExportPfad;
	}
	public String getC00E() {
		return c00E;
	}
	
	public void setC00E(String c00E) {
		this.c00E = c00E;
	}

	public String getC094() {
		return c094;
	}

	public void setC094(String c094) {
		this.c094 = c094;
	}
}
