package com.lp.server.system.service;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

public class ForecastImportLinienabruf implements
		IForecastImportFile<List<String>>, Serializable {
	private static final long serialVersionUID = 5144144775071090060L;
	private List<String> content;
	private Date calloffDate;
	private String filename;
	
	public ForecastImportLinienabruf(String filename, List<String> content, Date calloffDate) {
		this.filename = filename;
		this.content = content;
		setCalloffDate(calloffDate);
	}
	
	@Override
	public ForecastImportFileType getFiletype() {
		return ForecastImportFileType.Linienabruf;
	}

	@Override
	public List<String> getContent() {
		return content;
	}

	@Override
	public String getFilename() {
		return filename;
	}
	
	public Date getCalloffDate() {
		return calloffDate;
	}

	public void setCalloffDate(Date calloffDate) {
		this.calloffDate = calloffDate;
	}
}
