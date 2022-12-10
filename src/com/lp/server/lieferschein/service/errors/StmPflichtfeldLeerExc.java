package com.lp.server.lieferschein.service.errors;

public class StmPflichtfeldLeerExc extends StmException {
	private static final long serialVersionUID = -6222857727882826831L;
	
	private String xmlField;
	private Integer autoindex;
	private Integer serialnumber;
	
	public StmPflichtfeldLeerExc(String xmlField, Integer autoindex, Integer serialnumber) {
		super("");
		setXmlField(xmlField);
		setAutoindex(autoindex);
		setSerialnumber(serialnumber);
		setExcCode(StmExceptionCode.PROPERTY_FEHLT);
	}
	
	public String getXmlField() {
		return xmlField;
	}
	
	public void setXmlField(String xmlField) {
		this.xmlField = xmlField;
	}
	
	public void setAutoindex(Integer autoindex) {
		this.autoindex = autoindex;
	}
	
	public Integer getAutoindex() {
		return autoindex;
	}
	
	public void setSerialnumber(Integer serialnumber) {
		this.serialnumber = serialnumber;
	}
	
	public Integer getSerialnumber() {
		return serialnumber;
	}
}
