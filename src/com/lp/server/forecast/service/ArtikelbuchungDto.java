package com.lp.server.forecast.service;

import java.io.Serializable;
import java.math.BigDecimal;

public class ArtikelbuchungDto implements Serializable {
	private static final long serialVersionUID = -7658247497118810992L;

	private Integer artikelIId;
	private BigDecimal menge;
	private FclieferadresseNoka kommissioniertyp;

	public ArtikelbuchungDto() {
	}

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public BigDecimal getMenge() {
		return menge;
	}

	public void setMenge(BigDecimal menge) {
		this.menge = menge;
	}

	public FclieferadresseNoka getKommissioniertyp() {
		return kommissioniertyp;
	}

	public void setKommissioniertyp(FclieferadresseNoka kommissioniertyp) {
		this.kommissioniertyp = kommissioniertyp;
	}

}
