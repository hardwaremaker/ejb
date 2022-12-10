package com.lp.server.eingangsrechnung.service;

import java.util.ArrayList;
import java.util.List;

import com.lp.server.eingangsrechnung.service.ErImportError20475.Severity;

public class ErImportItem20475 extends ErImportItem {
	private static final long serialVersionUID = -5696879503813303270L;

	private Integer linenumber;
	private List<ErImportError20475> errors;
	private Integer erNumber;
	private Boolean imported;
	
	public ErImportItem20475() {
	}

	public Integer getLineumber() {
		return linenumber;
	}
	
	public void setLinenumber(Integer linenumber) {
		this.linenumber = linenumber;
	}
	
	public List<ErImportError20475> getErrors() {
		if (errors == null) {
			errors = new ArrayList<ErImportError20475>();
		}
		return errors;
	}
	
	public void addError(ErImportError20475 message) {
		getErrors().add(message);
	}
	
	public boolean hasErrors() {
		return !getErrors().isEmpty();
	}
	
	public boolean hasErrors(Severity severity) {
		if (!hasErrors()) return false;
		
		for (ErImportError20475 error : getErrors()) {
			if (severity.compareTo(error.getSeverity()) == 0) {
				return true;
			}
		}
		return false;
	}

	public void setErNumber(int erNumber) {
		this.erNumber = erNumber;
	}
	
	public Integer getErNumber() {
		return erNumber;
	}
	
	public boolean isImported() {
		return Boolean.TRUE.equals(imported);
	}
	
	public void setImported(Boolean imported) {
		this.imported = imported;
	}
	
	public String asString() {
		StringBuilder builder = new StringBuilder();
		builder.append("(").append(getLineumber()).append(") ").append("ER-Nr = ").append(getErNumber())
			.append(", Lieferant = ").append(getCreditor()).append(" (").append(getCreditorName()).append(")")
			.append(", Lief.RE-Nr = ").append(getCnrSupplier())
			.append(", ER-Betrag = ").append(String.valueOf(getAmount())).append(" ").append(getCurrency())
			.append(isImported() ? " >> IMPORTIERT" : "");
		return builder.toString();
	}
	
	public String asWholeInfo() {
		StringBuilder builder = new StringBuilder();
		builder.append(asString());
		for (ErImportError20475 message : getErrors()) {
			builder.append("\n\t[").append(message.getSeverity().name()).append("] ")
				.append(message.getMessage());
		}
		return builder.toString();
	}
}
