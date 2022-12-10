package com.lp.server.eingangsrechnung.service;

public class ZusatzkostenAnlegenWarningEntry extends ZusatzkostenAnlegenEntry {
	private static final long serialVersionUID = 2015018395969152856L;

	public enum Reason {
		SteuersatzGeaendert
	};
	
	private final Reason reason;
	
	public ZusatzkostenAnlegenWarningEntry(
			Integer eingangsrechnungId, String cnr, Reason reason) {
		super(eingangsrechnungId, cnr);
		this.reason = reason;
	}
	
	public Reason reason() {
		return this.reason;
	}
}
