package com.lp.server.finanz.service;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SepaImportSourceData implements Serializable {
	private static final long serialVersionUID = -3298207623051941071L;

	private String filepath;
	private String xml;
	private List<Integer> stmtNumbers;
	
	public SepaImportSourceData() {
		
	}
	
	public SepaImportSourceData(String filepath, String xml) {
		setFilepath(filepath);
		setXml(xml);
	}
	
	public String getFilepath() {
		return filepath;
	}
	
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	
	public String getFilename() {
		File file = getFilepath() != null ? new File(getFilepath()) : null;
		return file != null ? file.getName() : "unknown";
	}
	
	public String getXml() {
		return xml;
	}
	
	public void setXml(String xml) {
		this.xml = xml;
	}
	
	public List<Integer> getStmtNumbers() {
		if (stmtNumbers == null) {
			stmtNumbers = new ArrayList<Integer>();
		}
		return stmtNumbers;
	}
	
	public void setStmtNumbers(List<Integer> stmtNumbers) {
		this.stmtNumbers = stmtNumbers;
	}
}
