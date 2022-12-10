package com.lp.server.finanz.service;

import java.io.Serializable;
import java.math.BigDecimal;

public class SepaHabenBetrag implements SepaBetrag, Serializable {

	private static final long serialVersionUID = 158357721792030197L;

	private BigDecimal wert;
	
	public SepaHabenBetrag(BigDecimal initWert) {
		wert = initWert;
	}

	@Override
	public Boolean isHaben() {
		return true;
	}

	@Override
	public Boolean isSoll() {
		return false;
	}

	@Override
	public BigDecimal getWert() {
		return wert;
	}

	@Override
	public BigDecimal getPlusMinusWert() {
		return wert.negate();
	}

	@Override
	public SepaBetrag add(SepaBetrag betrag) {
		BigDecimal ergebnis = 
				betrag.isHaben() ? wert.add(betrag.getWert()) : wert.add(betrag.getWert().negate());
	
		if (ergebnis.compareTo(BigDecimal.ZERO) == 0) {
			return new SepaHabenBetrag(BigDecimal.ZERO);
		} else if (ergebnis.compareTo(BigDecimal.ZERO) == 1) {
			return new SepaHabenBetrag(ergebnis);
		}
							
		return new SepaSollBetrag(ergebnis.negate());
	}

	public SepaBetrag subtract(SepaBetrag betrag) {
		BigDecimal ergebnis = 
				betrag.isHaben() ? wert.subtract(betrag.getWert()) : wert.subtract(betrag.getWert().negate());
	
		if (ergebnis.compareTo(BigDecimal.ZERO) == 0) {
			return new SepaHabenBetrag(BigDecimal.ZERO);
		} else if (ergebnis.compareTo(BigDecimal.ZERO) == 1) {
			return new SepaHabenBetrag(ergebnis);
		}
							
		return new SepaSollBetrag(ergebnis.negate());
	}

	@Override
	public String toString() {
		return "SepaHabenBetrag [istHaben()=" + isHaben() + ", istSoll()="
				+ isSoll() + ", getWert()=" + getWert() + "]";
	}

	@Override
	public Boolean equals(SepaBetrag betrag) {
		return getPlusMinusWert() != null && betrag != null ? getPlusMinusWert().compareTo(betrag.getPlusMinusWert()) == 0 : false;
	}

}
