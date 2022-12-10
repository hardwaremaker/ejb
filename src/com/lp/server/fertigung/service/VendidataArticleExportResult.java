package com.lp.server.fertigung.service;

import java.io.Serializable;
import java.util.List;

import com.lp.server.artikel.service.VendidataExportStats;
import com.lp.util.EJBVendidataArticleExceptionLP;

public class VendidataArticleExportResult implements Serializable {

	private static final long serialVersionUID = 385343247332447817L;

	private String xmlContent;
	private List<EJBVendidataArticleExceptionLP> exportErrors;
	private VendidataExportStats stats;
	
	// Kompatibilitaet mit JaxB. Bitte nicht benutzen
	protected VendidataArticleExportResult() {
	}

	public VendidataArticleExportResult(String xmlContent, 
			List<EJBVendidataArticleExceptionLP> exportErrors, VendidataExportStats stats) {
		this.xmlContent = xmlContent;
		this.exportErrors = exportErrors;
		this.stats = stats;
	}

	public String getXmlContent() {
		return xmlContent;
	}

	public void setXmlContent(String xmlContent) {
		this.xmlContent = xmlContent;
	}

	public List<EJBVendidataArticleExceptionLP> getExportErrors() {
		return exportErrors;
	}

	public void setExportErrors(List<EJBVendidataArticleExceptionLP> exportErrors) {
		this.exportErrors = exportErrors;
	}

	public VendidataExportStats getStats() {
		return stats;
	}

	public void setStats(VendidataExportStats stats) {
		this.stats = stats;
	}

}
