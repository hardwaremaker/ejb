package com.lp.server.util;

import java.sql.Timestamp;

import com.lp.util.Helper;

public class BelegDatumNowAdapter implements IBelegDatum {
	private final Timestamp timestamp;
	
	public BelegDatumNowAdapter() {
		timestamp = Helper.cutTimestamp(new Timestamp(System.currentTimeMillis()));
	}
	
	@Override
	public Timestamp date() {
		return timestamp;
	}
}
