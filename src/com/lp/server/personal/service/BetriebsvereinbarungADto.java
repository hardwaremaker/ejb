package com.lp.server.personal.service;

import java.io.Serializable;
import java.math.BigDecimal;

public class BetriebsvereinbarungADto implements Serializable {

	public BetriebsvereinbarungADto(BigDecimal bdGleitzeit,
			BigDecimal bd50ProzentWennGleitzeitUeberschritten,
			BigDecimal bd50Prozent, BigDecimal bd100Prozent,
			BigDecimal bdFaktor50, BigDecimal bdFaktor100) {
		this.bdGleitzeit = bdGleitzeit;
		this.bd50ProzentWennGleitzeitUeberschritten = bd50ProzentWennGleitzeitUeberschritten;
		this.bd50Prozent = bd50Prozent;
		this.bd100Prozent = bd100Prozent;
		this.bdFaktor50 = bdFaktor50;
		this.bdFaktor100 = bdFaktor100;
	}

	public BetriebsvereinbarungADto(BigDecimal bdGleitzeit,
			BigDecimal bd50ProzentWennGleitzeitUeberschritten,
			BigDecimal bd50Prozent, BigDecimal bd100Prozent) {
		this.bdGleitzeit = bdGleitzeit;
		this.bd50ProzentWennGleitzeitUeberschritten = bd50ProzentWennGleitzeitUeberschritten;
		this.bd50Prozent = bd50Prozent;
		this.bd100Prozent = bd100Prozent;

	}

	BigDecimal bdGleitzeit = BigDecimal.ZERO;
	BigDecimal bd50ProzentWennGleitzeitUeberschritten = BigDecimal.ZERO;
	BigDecimal bd50Prozent = BigDecimal.ZERO;
	BigDecimal bd100Prozent = BigDecimal.ZERO;

	BigDecimal bdFaktor50 = BigDecimal.ONE;
	BigDecimal bdFaktor100 = BigDecimal.ONE;


	public BigDecimal getBd50ProzentWennGleitzeitUeberschrittenZuschlag() {
		return bd50ProzentWennGleitzeitUeberschritten
				.multiply(bdFaktor50);
	}

	public BigDecimal getBd50ProzentZuschlag() {
		return bd50Prozent.multiply(bdFaktor50);
	}

	public BigDecimal getBd100ProzentZuschlag() {
		return bd100Prozent.multiply(bdFaktor100);
	}

	public BigDecimal getBdGleitzeit() {
		return bdGleitzeit;
	}

	public void setBdGleitzeit(BigDecimal bdGleitzeit) {
		this.bdGleitzeit = bdGleitzeit;
	}

	public BigDecimal getBd50ProzentWennGleitzeitUeberschritten() {
		return bd50ProzentWennGleitzeitUeberschritten;
	}

	public void setBd50ProzentWennGleitzeitUeberschritten(
			BigDecimal bd50ProzentWennGleitzeitUeberschritten) {
		this.bd50ProzentWennGleitzeitUeberschritten = bd50ProzentWennGleitzeitUeberschritten;
	}

	public BigDecimal getBd50Prozent() {
		return bd50Prozent;
	}

	public void setBd50Prozent(BigDecimal bd50Prozent) {
		this.bd50Prozent = bd50Prozent;
	}

	public BigDecimal getBd100Prozent() {
		return bd100Prozent;
	}

	public void setBd100Prozent(BigDecimal bd100Prozent) {
		this.bd100Prozent = bd100Prozent;
	}
}
