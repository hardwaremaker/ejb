package com.lp.server.finanz.service;

import java.math.BigDecimal;

public class Iso20022BuchungdetailDto extends BuchungdetailDto {
	private static final long serialVersionUID = -1719649470934472442L;

	private BigDecimal nBetragKontowaehrung;
	
	
	public BigDecimal getNBetragKontowaehrung() {
		return nBetragKontowaehrung;
	}
	public void setNBetragKontowaehrung(BigDecimal nBetragKontowaehrung) {
		this.nBetragKontowaehrung = nBetragKontowaehrung;
	}
}
