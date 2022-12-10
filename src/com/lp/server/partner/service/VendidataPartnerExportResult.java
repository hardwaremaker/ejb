package com.lp.server.partner.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.lp.server.artikel.service.VendidataExportStats;
import com.lp.util.EJBExceptionLP;

public class VendidataPartnerExportResult implements Serializable {

	private static final long serialVersionUID = 7323233368938689291L;

	private String xmlContent;
	private List<EJBExceptionLP> exportErrors;
	private VendidataExportStats stats;
	
	public VendidataPartnerExportResult(String xmlContent, List<EJBExceptionLP> exportErrors, VendidataExportStats stats) {
		setXmlContent(xmlContent);
		setExportErrors(exportErrors);
		setStats(stats);
	}

	public VendidataPartnerExportResult(List<EJBExceptionLP> exportErrors, VendidataExportStats stats) {
		this("", exportErrors, stats);
	}

	public String getXmlContent() {
		return xmlContent;
	}

	public void setXmlContent(String xmlContent) {
		this.xmlContent = xmlContent;
	}

	public List<EJBExceptionLP> getExportErrors() {
		if (exportErrors == null) {
			exportErrors = new ArrayList<EJBExceptionLP>();
		}
		return exportErrors;
	}

	public void setExportErrors(List<EJBExceptionLP> exportErrors) {
		this.exportErrors = exportErrors;
	}

	public VendidataExportStats getStats() {
		return stats;
	}

	public void setStats(VendidataExportStats stats) {
		this.stats = stats;
	}

}
