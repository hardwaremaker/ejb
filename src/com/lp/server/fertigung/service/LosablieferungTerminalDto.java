package com.lp.server.fertigung.service;

import java.math.BigDecimal;

public class LosablieferungTerminalDto extends LosablieferungDto {
	private static final long serialVersionUID = -5272732096443210756L;

	private BigDecimal mengeSchrott;
	private Boolean bAendereLosgroesseUmSchrottmenge;
	
	public LosablieferungTerminalDto() {
	}

	public Boolean getBAendereLosgroesseUmSchrottmenge() {
		return bAendereLosgroesseUmSchrottmenge;
	}

	public void setBAendereLosgroesseUmSchrottmenge(
			Boolean bAendereLosgroesseUmSchrottmenge) {
		this.bAendereLosgroesseUmSchrottmenge = bAendereLosgroesseUmSchrottmenge;
	}

	public BigDecimal getMengeSchrott() {
		return mengeSchrott;
	}

	public void setMengeSchrott(BigDecimal mengeSchrott) {
		this.mengeSchrott = mengeSchrott;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(super.toString());
		builder.append(", mengeSchrott=").append(getMengeSchrott())
			.append(", bAendereLosgroesseUmSchrottmenge=").append(getBAendereLosgroesseUmSchrottmenge());
		return builder.toString();
	}
	
	public BigDecimal getGesamtmenge() {
		BigDecimal menge = getNMenge() != null ? getNMenge() : BigDecimal.ZERO;
		BigDecimal mengeSchrott = getMengeSchrott() != null ? getMengeSchrott() : BigDecimal.ZERO;
		
		return menge.add(mengeSchrott);
	}
}
