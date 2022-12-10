package com.lp.server.artikel.service;

import java.io.Serializable;

public class VendidataExportStats implements Serializable {
	
	private static final long serialVersionUID = -8803070739569384410L;

	private Integer totalExports;
	private Integer errorExports;
	private Integer goodExports;
	private Integer errorCounts;

	public VendidataExportStats() {
		reset();
	}
	
	public void reset() {
		totalExports = 0;
		errorExports = 0;
		goodExports = 0;
		errorCounts = 0;
	}

	public Integer getTotalExports() {
		return totalExports;
	}

	public void setTotalExports(Integer totalExports) {
		this.totalExports = totalExports;
	}
	
	public void incrementTotalExports() {
		totalExports++;
	}

	public Integer getErrorExports() {
		return errorExports;
	}

	public void setErrorExports(Integer errorExports) {
		this.errorExports = errorExports;
	}

	public void incrementErrorExports() {
		errorExports++;
	}

	public Integer getGoodExports() {
		return goodExports;
	}

	public void setGoodExports(Integer goodExports) {
		this.goodExports = goodExports;
	}

	public void incrementGoodExports() {
		goodExports++;
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

}
