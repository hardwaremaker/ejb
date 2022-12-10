package com.lp.server.fertigung.service;

import java.io.Serializable;
import java.math.BigDecimal;

public class StklPosDtoSearchParams implements Serializable{
	private Integer artikelIId;
	private Integer montageartIId;
	private BigDecimal sollsatzmenge;
	private BigDecimal mengeStklPos;

	public StklPosDtoSearchParams(Integer artikelIId, Integer montageartIId, BigDecimal sollsatzmenge,
			BigDecimal mengeStklPos) {
		super();
		this.artikelIId = artikelIId;
		this.montageartIId = montageartIId;
		this.sollsatzmenge = sollsatzmenge;
		this.mengeStklPos = mengeStklPos;
	}

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public Integer getMontageartIId() {
		return montageartIId;
	}

	public BigDecimal getSollsatzmenge() {
		return sollsatzmenge;
	}

	public BigDecimal getMengeStklPos() {
		return mengeStklPos;
	}
}
