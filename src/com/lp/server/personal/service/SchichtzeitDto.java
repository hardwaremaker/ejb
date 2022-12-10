package com.lp.server.personal.service;

import java.io.Serializable;
import java.sql.Time;


public class SchichtzeitDto implements Serializable {
	private Integer iId;
	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	private Time uBeginn;

	public Time getuBeginn() {
		return uBeginn;
	}

	public void setuBeginn(Time uBeginn) {
		this.uBeginn = uBeginn;
	}

	public Time getuEnde() {
		return uEnde;
	}

	public void setuEnde(Time uEnde) {
		this.uEnde = uEnde;
	}

	public Short getBEndedestages() {
		return bEndedestages;
	}

	public void setBEndedestages(Short bEndedestages) {
		this.bEndedestages = bEndedestages;
	}

	private Time uEnde;
	private Short bEndedestages;
	private Integer schichtIId;

	public Integer getSchichtIId() {
		return schichtIId;
	}

	public void setSchichtIId(Integer schichtIId) {
		this.schichtIId = schichtIId;
	}

	public Integer getSchichtzuschlagIId() {
		return schichtzuschlagIId;
	}

	public void setSchichtzuschlagIId(Integer schichtzuschlagIId) {
		this.schichtzuschlagIId = schichtzuschlagIId;
	}

	private Integer schichtzuschlagIId;
}
