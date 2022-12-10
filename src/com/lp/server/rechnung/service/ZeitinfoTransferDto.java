package com.lp.server.rechnung.service;

import java.io.Serializable;

import com.lp.server.util.report.JasperPrintLP;

public class ZeitinfoTransferDto implements Serializable {
	public ZeitinfoTransferDto(String cEmailEmfaenger, Integer partnerIId, JasperPrintLP print) {

		this.cEmailEmfaenger = cEmailEmfaenger;
		this.partnerIId = partnerIId;
		this.print = print;
	}

	String cEmailEmfaenger = null;
	public String getcEmailEmfaenger() {
		return cEmailEmfaenger;
	}
	public Integer getPartnerIId() {
		return partnerIId;
	}
	public JasperPrintLP getPrint() {
		return print;
	}

	Integer partnerIId = null;
	JasperPrintLP print = null;
}
