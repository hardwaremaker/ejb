package com.lp.server.fertigung.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class AnzahlInFertigungDto implements Serializable {
	BigDecimal bdMenge;
	Timestamp tProduktionsende;
	public BigDecimal getBdMenge() {
		return bdMenge;
	}
	public void setBdMenge(BigDecimal bdMenge) {
		this.bdMenge = bdMenge;
	}
	public Timestamp getTProduktionsende() {
		return tProduktionsende;
	}
	public void setTProduktionsende(Timestamp tProduktionsende) {
		this.tProduktionsende = tProduktionsende;
	}

}
