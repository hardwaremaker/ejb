package com.lp.server.util;

import java.sql.Timestamp;

import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.service.BelegDto;
import com.lp.util.Helper;

public class BelegDatumAdapter implements IBelegDatum {

	private final Object belegDto;
	private final Timestamp belegDatum;
	
	public BelegDatumAdapter(BelegDto belegDto) {
		this.belegDto = belegDto;
		this.belegDatum = belegDto.getTBelegdatum();
	}
	
	public BelegDatumAdapter(EingangsrechnungDto erDto) {
		this.belegDto = erDto;
		this.belegDatum = Helper.asTimestamp(erDto.getDBelegdatum());
	}
	
	@Override
	public Timestamp date() {
		return this.belegDatum;
	}
	
	public Object dto() {
		return belegDto;
	}
}
