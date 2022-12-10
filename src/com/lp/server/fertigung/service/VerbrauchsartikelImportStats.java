package com.lp.server.fertigung.service;

import java.io.Serializable;

public class VerbrauchsartikelImportStats implements Serializable {
	private static final long serialVersionUID = 8896458894429837663L;

	private Integer possibleImports;
	private Integer errorCounts;
	
	public VerbrauchsartikelImportStats() {
		reset();
	}

	public void reset() {
		possibleImports = 0;
		errorCounts = 0;
	}
	
	public void incrementPossibleImports() {
		possibleImports++;
	}
	
	public void incrementErrorCounts() {
		errorCounts++;
	}
	
	public Integer getErrorCounts() {
		return errorCounts;
	}
	
	public Integer getPossibleImports() {
		return possibleImports;
	}
}
