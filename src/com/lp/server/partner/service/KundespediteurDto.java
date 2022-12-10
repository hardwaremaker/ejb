package com.lp.server.partner.service;

import java.io.Serializable;
import java.math.BigDecimal;



public class KundespediteurDto implements Serializable {

	private Integer iId;
	private Integer kundeIId;
	private Integer spediteurIId;

	public Integer getSpediteurIId() {
		return spediteurIId;
	}

	public void setSpediteurIId(Integer spediteurIId) {
		this.spediteurIId = spediteurIId;
	}

	public BigDecimal getNGewichtinkg() {
		return nGewichtinkg;
	}

	public void setNGewichtinkg(BigDecimal nGewichtinkg) {
		this.nGewichtinkg = nGewichtinkg;
	}

	public String getCAccounting() {
		return cAccounting;
	}

	public void setCAccounting(String cAccounting) {
		this.cAccounting = cAccounting;
	}

	private BigDecimal nGewichtinkg;
	private String cAccounting;

	private static final long serialVersionUID = 1L;

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getKundeIId() {
		return this.kundeIId;
	}

	public void setKundeIId(Integer kundeIId) {
		this.kundeIId = kundeIId;
	}

}
