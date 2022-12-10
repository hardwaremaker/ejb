package com.lp.server.forecast.bl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

public class XlsForecastOffset implements Serializable {
	private static final long serialVersionUID = -6583828588241113074L;

	private Date datum;
	private BigDecimal menge;
	private int offset;
	
	public XlsForecastOffset() {
	}

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

	public BigDecimal getMenge() {
		return menge;
	}

	public void setMenge(BigDecimal menge) {
		this.menge = menge;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[").append(this.getClass().getName()).append(": ")
			.append("datum=").append(getDatum())
			.append(", menge=").append(getMenge())
			.append(", offset=").append(getOffset())
			.append("]");
		return builder.toString();
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}
}
