package com.lp.server.finanz.service;

import java.io.Serializable;
import java.math.BigDecimal;

import com.lp.server.util.fastlanereader.service.query.IQueryResultData;

public class BuchungDetailSummary implements IQueryResultData, Serializable {

	private static final long serialVersionUID = -6619016918242487991L;

	private boolean azkSummeFehlerhaft;
	private BigDecimal saldo;

	public BuchungDetailSummary() {
		saldo = BigDecimal.ZERO.setScale(4) ;
		azkSummeFehlerhaft = false ;
	}

	public BuchungDetailSummary(boolean azkSummeFehlerhaft, BigDecimal saldo) {
		setAzkFehlerhafteSumme(azkSummeFehlerhaft);
		setSaldo(saldo);
	}

	public boolean isAzkFehlerhafteSumme() {
		return azkSummeFehlerhaft;
	}

	public void setAzkFehlerhafteSumme(boolean azkSummeFehlerhaft) {
		this.azkSummeFehlerhaft = azkSummeFehlerhaft;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
}
