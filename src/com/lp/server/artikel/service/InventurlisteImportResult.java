package com.lp.server.artikel.service;

import java.io.Serializable;

import com.lp.util.EJBExceptionLP;

public class InventurlisteImportResult implements Serializable {
	private static final long serialVersionUID = -7874768523292480553L;

	private String log;
	private EJBExceptionLP ejbExceptionLP;
	
	public InventurlisteImportResult(String log) {
		setLog(log);
	}
	
	public InventurlisteImportResult(String log, EJBExceptionLP ejbExceptionLP) {
		this(log);
		setEjbExceptionLP(ejbExceptionLP);
	}
	
	public String getLog() {
		return log;
	}
	
	public void setLog(String log) {
		this.log = log;
	}
	
	public EJBExceptionLP getEjbExceptionLP() {
		return ejbExceptionLP;
	}
	
	public void setEjbExceptionLP(EJBExceptionLP ejbExceptionLP) {
		this.ejbExceptionLP = ejbExceptionLP;
	}
	
	public boolean hasEjbExceptionLP() {
		return getEjbExceptionLP() != null;
	}
}
