package com.lp.server.finanz.service;

import java.io.Serializable;
import java.math.BigDecimal;

import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.util.KontoId;
import com.lp.util.Helper;

public class SaldoInfoDto implements Serializable {
	private static final long serialVersionUID = 2343578802218997162L;
	
	private KontoId kontoId;
	private BigDecimal saldo;
	private MwstsatzDto satzDto;
	private KontoUvaVariante uva;
	
	public SaldoInfoDto() {
		setSaldo(BigDecimal.ZERO);
	}
	
	public SaldoInfoDto(KontoId kontoId) {
		this(kontoId, new KontoUvaVariante());
	}
	
	public SaldoInfoDto(KontoId kontoId, KontoUvaVariante uvavariante) {
		setKontoId(kontoId);
		setSaldo(BigDecimal.ZERO);
		setUva(uvavariante);
	}
	
	public KontoId getKontoId() {
		return kontoId;
	}
	
	public void setKontoId(KontoId kontoId) {
		this.kontoId = kontoId;
	}
	
	public BigDecimal getSaldo() {
		return saldo;
	}
	
	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
	
	public MwstsatzDto getSatzDto() {
		return satzDto;
	}
	
	public void setSatzDto(MwstsatzDto satzDto) {
		this.satzDto = satzDto;
	}
	
	public void addSaldo(BigDecimal value) {
		setSaldo(getSaldo().add(value));
	}
	
	public void subtractSaldo(BigDecimal value) {
		setSaldo(getSaldo().subtract(value));
	}
	
	public BigDecimal getGerundetenSaldo() {
		return Helper.rundeKaufmaennisch(getSaldo(), 0);
	}
	
	public void rundeSaldo() {
		setSaldo(Helper.rundeKaufmaennisch(getSaldo(), 0));
	}
	
	public void negateSaldo() {
		setSaldo(getSaldo().negate());
	}

	public KontoUvaVariante getUva() {
		return uva;
	}

	public void setUva(KontoUvaVariante uva) {
		this.uva = uva;
	}
}
