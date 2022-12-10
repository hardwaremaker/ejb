package com.lp.server.rechnung.service;

import java.io.Serializable;

import com.lp.server.util.report.JasperPrintLP;

public class ZugferdResult implements Serializable {
	private static final long serialVersionUID = 5972015413607644873L;

	private JasperPrintLP jasperPrintLP;
	private byte[] pdfData;
	private String filename;
	private String xml;
	
	public ZugferdResult() {
	}

	public JasperPrintLP getJasperPrintLP() {
		return jasperPrintLP;
	}
	public void setJasperPrintLP(JasperPrintLP jasperPrintLP) {
		this.jasperPrintLP = jasperPrintLP;
	}
	
	public byte[] getPdfData() {
		return pdfData;
	}
	public void setPdfData(byte[] pdfData) {
		this.pdfData = pdfData;
	}
	
	public String getPdfName() {
		return filename != null ? filename + ".pdf" : null;
	}
	public String getXmlName() {
		return filename != null ? filename + ".xml" : null;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFilename() {
		return filename;
	}
	
	public String getXml() {
		return xml;
	}
	public void setXml(String xml) {
		this.xml = xml;
	}
}
