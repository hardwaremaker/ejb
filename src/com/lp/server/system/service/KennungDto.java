package com.lp.server.system.service;

import java.io.Serializable;

import com.lp.server.util.IIId;

public class KennungDto implements Serializable, IIId {
	private static final long serialVersionUID = 7903921521063763445L;

	private Integer iId;
	private String cnr;
	private String bez;

	@Override
	public Integer getIId() {
		return iId;
	}

	@Override
	public void setIId(Integer newIId) {
		this.iId = newIId;
	}

	public String getCnr() {
		return cnr;
	}

	public void setCnr(String cnr) {
		this.cnr = cnr;
	}

	public String getBez() {
		return bez;
	}

	public void setBez(String bez) {
		this.bez = bez;
	}

	public String formatBez() {
		if (bez != null) {
			return cnr + " " + bez;
		} else {
			return cnr;
		}
	}

}
