package com.lp.server.personal.service;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;

public class AnwesenheitsbestaetigungDto implements Serializable {
	private Integer iId;

	private Integer personalIId;

	private Integer auftragIId;

	private Integer projektIId;

	private Timestamp tUnterschrift;

	private Timestamp tVersandt;

	private byte[] oUnterschrift;

	private String datenformatCNr;

	private Integer iLfdnr;
	private String cName;

	public String getCName() {
		return cName;
	}

	public void setCName(String cName) {
		this.cName = cName;
	}

	public Integer getILfdnr() {
		return iLfdnr;
	}

	public void setILfdnr(Integer iLfdnr) {
		this.iLfdnr = iLfdnr;
	}

	private byte[] oPdf;

	public byte[] getOPdf() {
		return oPdf;
	}

	public void setOPdf(byte[] oPdf) {
		this.oPdf = oPdf;
	}

	private String cBemerkung;

	public Integer getPersonalIId() {
		return personalIId;
	}

	public void setPersonalIId(Integer personalIId) {
		this.personalIId = personalIId;
	}

	public Integer getAuftragIId() {
		return auftragIId;
	}

	public void setAuftragIId(Integer auftragIId) {
		this.auftragIId = auftragIId;
	}

	public Integer getProjektIId() {
		return projektIId;
	}

	public void setProjektIId(Integer projektIId) {
		this.projektIId = projektIId;
	}

	public Timestamp getTUnterschrift() {
		return tUnterschrift;
	}

	public void setTUnterschrift(Timestamp tUnterschrift) {
		this.tUnterschrift = tUnterschrift;
	}

	public Timestamp getTVersandt() {
		return tVersandt;
	}

	public void setTVersandt(Timestamp tVersandt) {
		this.tVersandt = tVersandt;
	}

	public byte[] getOUnterschrift() {
		return oUnterschrift;
	}

	public void setOUnterschrift(byte[] oUnterschrift) {
		this.oUnterschrift = oUnterschrift;
	}

	public String getDatenformatCNr() {
		return datenformatCNr;
	}

	public void setDatenformatCNr(String datenformatCNr) {
		this.datenformatCNr = datenformatCNr;
	}

	public String getCBemerkung() {
		return cBemerkung;
	}

	public void setCBemerkung(String cBemerkung) {
		this.cBemerkung = cBemerkung;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

}
