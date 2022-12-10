package com.lp.server.finanz.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.lp.server.rechnung.service.sepa.errors.SepaException;

public class SepaXmlExportResult implements Serializable {
	private static final long serialVersionUID = -7419333665830039981L;

	private String xml;
	private List<SepaException> sepaErrors;
	private String exportPath;
	
	public SepaXmlExportResult() {
	}

	public SepaXmlExportResult(String xml) {
		setXml(xml);
	}

	public SepaXmlExportResult(List<SepaException> sepaErrors) {
		setSepaErrors(sepaErrors);
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public List<SepaException> getSepaErrors() {
		if (sepaErrors == null) {
			sepaErrors = new ArrayList<SepaException>();
		}
		return sepaErrors;
	}

	public void setSepaErrors(List<SepaException> sepaErrors) {
		this.sepaErrors = sepaErrors;
	}
	
	public boolean hasErrors() {
		return !getSepaErrors().isEmpty();
	}

	public String getExportPath() {
		return exportPath;
	}

	public void setExportPath(String exportPath) {
		this.exportPath = exportPath;
	}

}
