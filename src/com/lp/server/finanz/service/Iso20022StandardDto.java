package com.lp.server.finanz.service;

import java.io.Serializable;

public class Iso20022StandardDto implements Serializable {
	private static final long serialVersionUID = 232982456736420064L;

	private Integer iId;
	private Iso20022StandardEnum standardEnum;
	private String cBez;

	public Iso20022StandardDto() {
	}

	public Integer getIId() {
		return iId;
	}
	public void setIId(Integer iId) {
		this.iId = iId;
	}
	
	public Iso20022StandardEnum getStandardEnum() {
		return standardEnum;
	}
	public void setStandardEnum(Iso20022StandardEnum standardEnum) {
		this.standardEnum = standardEnum;
	}
	
	public String getCBez() {
		return cBez;
	}
	public void setCBez(String cBez) {
		this.cBez = cBez;
	}
}
