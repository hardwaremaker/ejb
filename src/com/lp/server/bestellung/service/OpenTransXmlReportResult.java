package com.lp.server.bestellung.service;

import java.io.Serializable;

import com.lp.server.util.report.JasperPrintLP;

public class OpenTransXmlReportResult implements Serializable {
	private static final long serialVersionUID = 2432829321288548882L;

	private JasperPrintLP[] prints;
	private String xmlOtOrder;
	private String filename;
	
	public OpenTransXmlReportResult(JasperPrintLP[] prints) {
		setPrints(prints);
	}
	
	public OpenTransXmlReportResult(JasperPrintLP[] prints, String xmlOrder) {
		this(prints);
		setXmlOtOrder(xmlOrder);
	}
	
	public JasperPrintLP[] getPrints() {
		return prints;
	}
	
	public void setPrints(JasperPrintLP[] prints) {
		this.prints = prints;
	}
	
	public String getXmlOtOrder() {
		return xmlOtOrder;
	}
	
	public void setXmlOtOrder(String xmlOtOrder) {
		this.xmlOtOrder = xmlOtOrder;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}
}
