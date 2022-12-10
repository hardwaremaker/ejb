package com.lp.server.forecast.bl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class XlsForecastPosition implements Serializable {
	private static final long serialVersionUID = -4011270936061137502L;

	private String bestellnummer;
	private String artikelnummer;
	private List<XlsForecastOffset> offsets;
	private Integer artikelIId;
	private Date  cumulativeDate;
	private BigDecimal cumulativeQuantity;
	private String cumulativeReference;
	
	public XlsForecastPosition() {
		setOffsets(new ArrayList<XlsForecastOffset>());
	}

	public String getBestellnummer() {
		return bestellnummer;
	}

	public void setBestellnummer(String bestellnummer) {
		this.bestellnummer = bestellnummer;
	}

	public String getArtikelnummer() {
		return artikelnummer;
	}

	public void setArtikelnummer(String artikelnummer) {
		this.artikelnummer = artikelnummer;
	}

	public List<XlsForecastOffset> getOffsets() {
		return offsets;
	}

	public void setOffsets(List<XlsForecastOffset> offsets) {
		this.offsets = offsets;
	}

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public void addOffset(XlsForecastOffset offset) {
		offsets.add(offset);
	}
	
	public Date getCumulativeDate() {
		return cumulativeDate;
	}

	public void setCumulativeDate(Date cumulativeDate) {
		this.cumulativeDate = cumulativeDate;
	}

	public BigDecimal getCumulativeQuantity() {
		return cumulativeQuantity;
	}

	public void setCumulativeQuantity(BigDecimal cumulativeQuantity) {
		this.cumulativeQuantity = cumulativeQuantity;
	}

	public String getCumulativeReference() {
		return cumulativeReference;
	}

	public void setCumulativeReference(String cumulativeReference) {
		this.cumulativeReference = cumulativeReference;
	}

	public boolean hasCumulativeDate() {
		return cumulativeDate != null;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[").append(this.getClass().getName()).append(": ")
			.append("artikelnummer=").append(getArtikelnummer())
			.append(", bestellnummer=").append(getBestellnummer())
			.append(", artikelIId=").append(getArtikelIId())
			.append(", offsets=").append(getOffsets())
			.append("]");
		return builder.toString();
	}
}
