package com.lp.server.finanz.service;

import java.io.Serializable;

public class ReversechargeartsprDto implements Serializable {
	private static final long serialVersionUID = 2196852911519849324L;

	private Integer reversechargeartId ;
	private String localeCNr;
	private String cBez;
	
	public Integer getReversechargeartId() {
		return reversechargeartId;
	}
	public void setReversechargeartId(Integer reversechargeartId) {
		this.reversechargeartId = reversechargeartId;
	}
	public String getLocaleCNr() {
		return localeCNr;
	}
	public void setLocaleCNr(String localeCNr) {
		this.localeCNr = localeCNr;
	}
	public String getcBez() {
		return cBez;
	}
	public void setcBez(String cBez) {
		this.cBez = cBez;
	}
}
