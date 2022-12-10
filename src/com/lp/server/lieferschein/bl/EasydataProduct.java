package com.lp.server.lieferschein.bl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class EasydataProduct implements Serializable {
	private static final long serialVersionUID = 9004895825308235490L;

	private String barcode;
	private BigDecimal quantity;
	private Timestamp time;
	
	public EasydataProduct() {
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

}
