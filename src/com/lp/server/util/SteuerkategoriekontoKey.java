package com.lp.server.util;

import java.io.Serializable;

public class SteuerkategoriekontoKey implements Serializable {
	private static final long serialVersionUID = -840236071633358923L;

	private final SteuerkategorieId steuerkategorieId;
	private final MwstsatzbezId mwstsatzbezId;
	private final IBelegDatum belegdatum;
	
	public SteuerkategoriekontoKey(
			SteuerkategorieId steuerkategorieId,
			MwstsatzbezId mwstsatzbezId) {
		this(steuerkategorieId, mwstsatzbezId, new BelegDatumNowAdapter());
	}
	
	public SteuerkategoriekontoKey(
			SteuerkategorieId steuerkategorieId,
			MwstsatzbezId mwstsatzbezId, 
			IBelegDatum belegDatum) {
		this.steuerkategorieId = steuerkategorieId;
		this.mwstsatzbezId = mwstsatzbezId;
		this.belegdatum = belegDatum;
	}
	
	public SteuerkategorieId steuerkategorieId() {
		return steuerkategorieId;
	}
	
	public MwstsatzbezId mwstsatzbezId() {
		return mwstsatzbezId;
	}
	
	public IBelegDatum belegDatum() {
		return belegdatum; 
	}
}
