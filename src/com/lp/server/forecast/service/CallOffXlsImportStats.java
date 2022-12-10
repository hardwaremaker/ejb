package com.lp.server.forecast.service;

import java.io.Serializable;

public class CallOffXlsImportStats implements Serializable {
	private static final long serialVersionUID = 5968495948273101154L;

	private Integer totalImports;
	private Integer errorImports;
	private Integer errorCounts;

	public CallOffXlsImportStats() {
		reset();
	}

	private void reset() {
		totalImports = 0;
		errorCounts = 0;
		errorImports = 0;
	}

	public Integer getTotalExports() {
		return totalImports;
	}

	public void setTotalExports(Integer totalExports) {
		this.totalImports = totalExports;
	}

	public void incrementTotalExports() {
		totalImports++;
	}

	public Integer getErrorExports() {
		return errorImports;
	}

	public void setErrorExports(Integer errorExports) {
		this.errorImports = errorExports;
	}

	public void incrementErrorExports() {
		errorImports++;
	}

	public Integer getErrorCounts() {
		return errorCounts;
	}

	public void setErrorCounts(Integer errorCounts) {
		this.errorCounts = errorCounts;
	}

	public void incrementErrorCounts() {
		errorCounts++;
	}

	public void addStats(CallOffXlsImportStats otherStats) {
		totalImports += otherStats.getTotalExports();
		errorImports += otherStats.getErrorExports();
		errorCounts += otherStats.getErrorCounts();
	}
}
