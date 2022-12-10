package com.lp.server.fertigung.service;

import java.io.Serializable;
import java.util.List;

import com.lp.server.fertigung.service.errors.ImportException;

public class VerbrauchsartikelImportResult implements Serializable {

	private static final long serialVersionUID = 5337279866145742076L;

	private List<ImportException> errors;
	private VerbrauchsartikelImportStats stats;
	
	public VerbrauchsartikelImportResult(List<ImportException> errors) {
		this(errors, new VerbrauchsartikelImportStats());
	}
	public VerbrauchsartikelImportResult(List<ImportException> errors, VerbrauchsartikelImportStats stats) {
		this.errors = errors;
		this.stats = stats;
	}
	
	public List<ImportException> getImportErrors() {
		return errors;
	}
	
	public VerbrauchsartikelImportStats getStats() {
		return stats;
	}
	
}
