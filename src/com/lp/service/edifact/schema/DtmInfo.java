package com.lp.service.edifact.schema;

import java.util.Date;

public class DtmInfo {
	private String functionCode;
	private String dateString;
	private String formatCode;
	private Date date;
	
	public String getFunctionCode() {
		return functionCode;
	}
	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}
	public String getDateString() {
		return dateString;
	}
	public void setDateString(String dateString) {
		this.dateString = dateString;
	}
	public String getFormatCode() {
		return formatCode;
	}
	public void setFormatCode(String formatCode) {
		this.formatCode = formatCode;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
}
