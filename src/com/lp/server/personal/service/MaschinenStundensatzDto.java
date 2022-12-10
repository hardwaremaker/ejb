package com.lp.server.personal.service;

import java.io.Serializable;
import java.math.BigDecimal;




public class MaschinenStundensatzDto implements Serializable {

	 public MaschinenStundensatzDto(BigDecimal bdStundensatz, BigDecimal bdStundensatzVK) {
		super();
		this.bdStundensatz = bdStundensatz;
		this.bdStundensatzVK = bdStundensatzVK;
	}
	private BigDecimal bdStundensatz=BigDecimal.ZERO;
	 public BigDecimal getBdStundensatz() {
		return bdStundensatz;
	}
	public void setBdStundensatz(BigDecimal bdStundensatz) {
		this.bdStundensatz = bdStundensatz;
	}
	public BigDecimal getBdStundensatzVK() {
		return bdStundensatzVK;
	}
	public void setBdStundensatzVK(BigDecimal bdStundensatzVK) {
		this.bdStundensatzVK = bdStundensatzVK;
	}
	private BigDecimal bdStundensatzVK=BigDecimal.ZERO;
	
}
