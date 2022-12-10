package com.lp.server.finanz.service;

import java.io.Serializable;
import java.math.BigDecimal;

public class SepaSollBetrag implements SepaBetrag, Serializable {
	
	private static final long serialVersionUID = 3648841507822375229L;

	private BigDecimal wert;

	public SepaSollBetrag(BigDecimal initWert) {
		wert = initWert;
	}

	@Override
	public Boolean isHaben() {
		return false;
	}

	@Override
	public Boolean isSoll() {
		return true;
	}

	@Override
	public BigDecimal getWert() {
		return wert;
	}

	@Override
	public BigDecimal getPlusMinusWert() {
		return wert;
	}

	@Override
	public SepaBetrag add(SepaBetrag betrag) {
		BigDecimal ergebnis = 
				betrag.isHaben() ? wert.add(betrag.getWert().negate()) : wert.add(betrag.getWert());
	
		if (ergebnis.compareTo(BigDecimal.ZERO) == 0) {
			return new SepaSollBetrag(BigDecimal.ZERO);
		} else if (ergebnis.compareTo(BigDecimal.ZERO) == 1) {
			return new SepaSollBetrag(ergebnis);
		}
			
		return new SepaHabenBetrag(ergebnis.negate());
	}

	public SepaBetrag subtract(SepaBetrag betrag) {
		BigDecimal ergebnis = 
				betrag.isHaben() ? wert.subtract(betrag.getWert().negate()) : wert.subtract(betrag.getWert());
	
		if (ergebnis.compareTo(BigDecimal.ZERO) == 0) {
			return new SepaSollBetrag(BigDecimal.ZERO);
		} else if (ergebnis.compareTo(BigDecimal.ZERO) == 1) {
			return new SepaSollBetrag(ergebnis);
		}
					
		return new SepaHabenBetrag(ergebnis.negate());
	}

	@Override
	public String toString() {
		return "SepaSollBetrag [istHaben()=" + isHaben() + ", istSoll()="
				+ isSoll() + ", getWert()=" + getWert() + "]";
	}

	@Override
	public Boolean equals(SepaBetrag betrag) {
		return getPlusMinusWert() != null && betrag != null ? getPlusMinusWert().compareTo(betrag.getPlusMinusWert()) == 0 : false;
	}
}
