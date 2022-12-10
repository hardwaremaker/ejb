package com.lp.server.personal.service;

import java.io.Serializable;
import java.math.BigDecimal;

public class ZeitsaldoDto implements Serializable {
	
	private static final long serialVersionUID = -3927486132401580245L;

	private BigDecimal nVerfuegbarerurlaub;
	private BigDecimal nSaldo;
	private String einheitVerfuegbarerUrlaub;
	
	public ZeitsaldoDto() {
	}

	public BigDecimal getNVerfuegbarerurlaub() {
		return nVerfuegbarerurlaub;
	}

	public void setNVerfuegbarerurlaub(BigDecimal nVerfuegbarerurlaub) {
		this.nVerfuegbarerurlaub = nVerfuegbarerurlaub;
	}

	public BigDecimal getNSaldo() {
		return nSaldo;
	}

	public void setNSaldo(BigDecimal nSaldo) {
		this.nSaldo = nSaldo;
	}

	public String getEinheitVerfuegbarerUrlaub() {
		return einheitVerfuegbarerUrlaub;
	}

	public void setEinheitVerfuegbarerUrlaub(String einheitVerfuegbarerUrlaub) {
		this.einheitVerfuegbarerUrlaub = einheitVerfuegbarerUrlaub;
	}

}
