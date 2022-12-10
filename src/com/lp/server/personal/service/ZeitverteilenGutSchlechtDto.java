package com.lp.server.personal.service;

import java.io.Serializable;
import java.math.BigDecimal;

public class ZeitverteilenGutSchlechtDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer lossollarbeitsplanIId=null;
	private BigDecimal gutStueck=null;
	public ZeitverteilenGutSchlechtDto(Integer lossollarbeitsplanIId, BigDecimal gutStueck, BigDecimal schlechtStueck) {
		
		this.lossollarbeitsplanIId = lossollarbeitsplanIId;
		this.gutStueck = gutStueck;
		this.schlechtStueck = schlechtStueck;
	}
	public Integer getLossollarbeitsplanIId() {
		return lossollarbeitsplanIId;
	}
	public BigDecimal getGutStueck() {
		return gutStueck;
	}
	public BigDecimal getSchlechtStueck() {
		return schlechtStueck;
	}
	private BigDecimal schlechtStueck=null;

}
