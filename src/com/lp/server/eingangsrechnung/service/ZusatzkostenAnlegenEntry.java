package com.lp.server.eingangsrechnung.service;

import java.io.Serializable;

public class ZusatzkostenAnlegenEntry implements Serializable {
	private static final long serialVersionUID = 7919357261633223265L;
	
	private final Integer erId;
	private final String erCnr;
	
	public ZusatzkostenAnlegenEntry(Integer eingangsrechnungId, String cnr) {
		this.erId = eingangsrechnungId;
		this.erCnr = cnr;
	}

	public Integer id() {
		return this.erId;
	}
	
	public String cnr() {
		return this.erCnr;
	}
}
