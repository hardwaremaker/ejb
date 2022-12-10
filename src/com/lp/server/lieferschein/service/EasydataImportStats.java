package com.lp.server.lieferschein.service;

import java.io.Serializable;

public class EasydataImportStats implements Serializable {
	private static final long serialVersionUID = -6032029234104605457L;

	private Integer totalImports;
	private Integer errorCounts;
	
	public EasydataImportStats() {
		reset();
	}
	
	public void reset() {
		totalImports = 0;
		errorCounts = 0;
	}
	
	public void setTotalImports(Integer totalImports) {
		this.totalImports = totalImports;
	}
	
	public Integer getTotalImports() {
		return totalImports;
	}
	
	public void incrementTotalImports() {
		totalImports++;
	}
	
	public Integer getErrorCounts() {
		return errorCounts;
	}
	
	public void incrementErrorCounts() {
		errorCounts++;
	}
}
