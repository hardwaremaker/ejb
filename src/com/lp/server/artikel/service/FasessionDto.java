package com.lp.server.artikel.service;

import java.io.Serializable;
import java.sql.Timestamp;

public class FasessionDto  implements Serializable{
	private Integer iId;
	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}
	
	private Timestamp tBeginn;
	private Timestamp tGedruckt;
	private Integer personalIId;
	public Timestamp gettBeginn() {
		return tBeginn;
	}
	public void settBeginn(Timestamp tBeginn) {
		this.tBeginn = tBeginn;
	}
	public Timestamp gettGedruckt() {
		return tGedruckt;
	}
	public void settGedruckt(Timestamp tGedruckt) {
		this.tGedruckt = tGedruckt;
	}
	public Integer getPersonalIId() {
		return personalIId;
	}
	public void setPersonalIId(Integer personalIId) {
		this.personalIId = personalIId;
	}
}
