package com.lp.server.lieferschein.service;

import java.io.Serializable;
import java.sql.Timestamp;

import com.lp.server.util.LieferscheinId;

public class BestaetigterLieferscheinDto implements Serializable {
	private static final long serialVersionUID = 3809031382181462948L;

	private LieferscheinId lieferscheinId;
	private byte[] pdf;
	private byte[] unterschrift;
	private String bemerkung;
	private String unterschreiber;
	private Integer serialNumber;
	private Timestamp zeitpunkt;
	
	public LieferscheinId getLieferscheinId() {
		return lieferscheinId;
	}
	public void setLieferscheinId(LieferscheinId lieferscheinId) {
		this.lieferscheinId = lieferscheinId;
	}
	public byte[] getPdf() {
		return pdf;
	}
	public void setPdf(byte[] pdf) {
		this.pdf = pdf;
	}
	public byte[] getUnterschrift() {
		return unterschrift;
	}
	public void setUnterschrift(byte[] unterschrift) {
		this.unterschrift = unterschrift;
	}
	public String getBemerkung() {
		return bemerkung;
	}
	public void setBemerkung(String bemerkung) {
		this.bemerkung = bemerkung;
	}
	public String getUnterschreiber() {
		return unterschreiber;
	}
	public void setUnterschreiber(String unterschreiber) {
		this.unterschreiber = unterschreiber;
	}
	public Integer getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(Integer serialNumber) {
		this.serialNumber = serialNumber;
	}
	public Timestamp getZeitpunkt() {
		return zeitpunkt;
	}
	public void setZeitpunkt(Timestamp zeitpunkt) {
		this.zeitpunkt = zeitpunkt;
	}
}
