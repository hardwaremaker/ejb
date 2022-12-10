package com.lp.server.artikel.service;

import java.io.Serializable;
import java.math.BigDecimal;

import com.lp.server.util.ArtikelId;

public class LagerstandInfoEntryDto implements Serializable {
	private static final long serialVersionUID = -2877188519752968963L;

	private ArtikelId artikelId;
	private BigDecimal amount;
	private BigDecimal stockMinimum;
	private BigDecimal stockNominal;
	
	public LagerstandInfoEntryDto(ArtikelId artikelId, BigDecimal amount) {
		this.artikelId = artikelId;
		this.amount = amount;
	}
	
	public ArtikelId getArtikelId() {
		return artikelId;
	}
	public void setArtikelId(ArtikelId artikelId) {
		this.artikelId = artikelId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getStockMinimum() {
		return stockMinimum;
	}

	public void setStockMinimum(BigDecimal stockMinimum) {
		this.stockMinimum = stockMinimum;
	}

	public BigDecimal getStockNominal() {
		return stockNominal;
	}

	public void setStockNominal(BigDecimal stockNominal) {
		this.stockNominal = stockNominal;
	}
}
