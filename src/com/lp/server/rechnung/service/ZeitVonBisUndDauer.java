package com.lp.server.rechnung.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;

public class ZeitVonBisUndDauer implements Serializable {
	public Timestamp tVon = null;
	public Timestamp tBis = null;
	public BigDecimal bdStunden = null;
}
