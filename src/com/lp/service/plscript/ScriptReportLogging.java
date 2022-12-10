package com.lp.service.plscript;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class ScriptReportLogging implements Serializable {
	private static final long serialVersionUID = -7387025018498121148L;

	private List<String> debugReports;
	private List<String> infoReports;
	private List<String> warnReports;
	private List<String> errorReports;

	public ScriptReportLogging() {
		initialize();
	}
	
	private void initialize() {
		debugReports = new ArrayList<String>();
		infoReports = new ArrayList<String>();
		warnReports = new ArrayList<String>();
		errorReports = new ArrayList<String>();
	}
	
	public void debug(String message) {
		debugReports.add(message);
	}
	
	public void info(String message) {
		infoReports.add(message);
	}
	
	public void warn(String message) {
		warnReports.add(message);
	}
	
	public void error(String message) {
		errorReports.add(message);
	}
	
	public List<String> getDebugs() {
		return debugReports;
	}
	
	public List<String> getInfos() {
		return infoReports;
	}
	
	public List<String> getWarns() {
		return warnReports;
	}
	
	public List<String> getErrors() {
		return errorReports;
	}
	
	public String getDebug() {
		return StringUtils.join(debugReports.iterator(), "\r\n");
	}
	
	public String getInfo() {
		return StringUtils.join(infoReports.iterator(), "\r\n");
	}
	
	public String getWarn() {
		return StringUtils.join(warnReports.iterator(), "\r\n");
	}
	
	public String getError() {
		return StringUtils.join(errorReports.iterator(), "\r\n");
	}	
}
