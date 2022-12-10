package com.lp.server.eingangsrechnung.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ZahlungsvorschlagExportResult implements Serializable {

	private static final long serialVersionUID = -5910098034962038713L;

	private String daten;
	private List<ZahlungsvorschlagExportException> messages;
	
	public ZahlungsvorschlagExportResult(String daten) {
		this(daten, new ArrayList<ZahlungsvorschlagExportException>());
	}
	
	public ZahlungsvorschlagExportResult(String daten, List<ZahlungsvorschlagExportException> errors) {
		setDaten(daten);
		setMessages(errors);
	}

	public String getDaten() {
		return daten;
	}
	
	public void setDaten(String daten) {
		this.daten = daten;
	}
	
	public List<ZahlungsvorschlagExportException> getMessages() {
		return messages;
	}
	
	public void setMessages(List<ZahlungsvorschlagExportException> messages) {
		this.messages = messages;
	}
	
	public boolean hasMessages() {
		return messages != null && !messages.isEmpty();
	}
	
	public boolean hasFailed() {
		if (!hasMessages()) return false;
		
		for (ZahlungsvorschlagExportException error : messages) {
			if (ZahlungsvorschlagExportException.SEVERITY_ERROR <= error.getSeverity())
				return true;
		}
		return false;
	}
}
