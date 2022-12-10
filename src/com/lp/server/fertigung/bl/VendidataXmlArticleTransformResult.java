package com.lp.server.fertigung.bl;

import java.io.Serializable;
import java.util.List;

import com.lp.server.artikel.service.VendidataExportStats;
import com.lp.server.schema.vendidata.articles.XMLArticles;
import com.lp.util.EJBVendidataArticleExceptionLP;

public class VendidataXmlArticleTransformResult implements Serializable {
	
	private static final long serialVersionUID = -2563183672019577141L;

	private XMLArticles xmlArticles;
	private List<EJBVendidataArticleExceptionLP> exportErrors;
	private VendidataExportStats stats;
	
	public VendidataXmlArticleTransformResult(XMLArticles xmlArticles, 
			List<EJBVendidataArticleExceptionLP> exportErrors, VendidataExportStats stats) {
		setXmlArticles(xmlArticles);
		setExportErrors(exportErrors);
		setStats(stats);
	}

	public XMLArticles getXmlArticles() {
		return xmlArticles;
	}

	public void setXmlArticles(XMLArticles xmlArticles) {
		this.xmlArticles = xmlArticles;
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
