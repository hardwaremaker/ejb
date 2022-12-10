package com.lp.server.eingangsrechnung.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.lp.server.eingangsrechnung.service.ZusatzkostenAnlegenWarningEntry.Reason;
import com.lp.server.util.HvOptional;


public class ZusatzkostenAnlegenResult implements Serializable {
	private static final long serialVersionUID = -2968263033555670552L;

	/**
	 * Insgesamt neu angelegte ER;
	 */
	private int total;
	
	/**
	 * Liste aller erfolgreich angelegten ERs
	 */
	private final List<ZusatzkostenAnlegenEntry> success;
	
	/**
	 * Liste aller ERs die angelegt wurde, aber nachbearbeitet werden muessen
	 */
	private final List<ZusatzkostenAnlegenWarningEntry> warning;
	
	public ZusatzkostenAnlegenResult() {
		success = new ArrayList<ZusatzkostenAnlegenEntry>();
		warning = new ArrayList<ZusatzkostenAnlegenWarningEntry>();
	}
	
	/**
	 * Die Anzahl aller verarbeiteten Eingangsrechnungen
	 * @return die Anzahl aller Eingangsrechnungen
	 */
	public int total() {
		return this.total;
	}
	
	/**
	 * Die neuen, erfolgreich angelegten Eingangsrechnungen
	 * @return eine (leere) Liste aller erfolgreichen Eingangsrechnungen
	 */
	public List<ZusatzkostenAnlegenEntry> success() {
		return this.success;
	}
	
	/**
	 * Die neuen Eingangsrechnungen mit Warnungen
	 * 
	 * @return eine (leere) Liste aller ERs mit Warnungen 
	 */
	public List<ZusatzkostenAnlegenWarningEntry> warnings() {
		return this.warning;
	}
	
	public void addSuccess(EingangsrechnungDto er) {
		this.success.add(
				new ZusatzkostenAnlegenEntry(er.getIId(), er.getCNr()));
		++total;
	}
	
	public void addWarning(EingangsrechnungDto er, Reason reason) {
		this.warning.add(
				new ZusatzkostenAnlegenWarningEntry(
						er.getIId(), er.getCNr(), reason));
		++total;
	}
	
	public void add(EingangsrechnungDto er, HvOptional<Reason> reason) {
		if(reason.isPresent()) {
			addWarning(er, reason.get());
		} else {
			addSuccess(er);
		}
	}
	
	public boolean hasWarnings() {
		return this.warning.size() != 0;
	}
}
