package com.lp.server.finanz.service;

import java.io.Serializable;
import java.sql.Timestamp;

import com.lp.server.util.UvaFormularId;

public class UvaFormularDto implements Serializable {
	private static final long serialVersionUID = -5577204608677279514L;

	private UvaFormularId pk;
	private Timestamp tAnlegen;
	private Integer personalIIdAnlegen;
	private Timestamp tAendern;
	private Integer personalIIdAendern;

	private Integer finanzamtId;
	private String mandantCNr;
	private Integer uvaartId;
	private Integer iGruppe;
	private Integer iSort;
	private String cKennzeichen;

	public UvaFormularId getPk() {
		return pk;
	}
	
	public Integer getIId() {
		return pk == null ? null : pk.id();
	}
	public void setIId(Integer iId) {
		this.pk = new UvaFormularId(iId);
	}
	
	public Timestamp getTAnlegen() {
		return tAnlegen;
	}
	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}
	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}
	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}
	public Timestamp getTAendern() {
		return tAendern;
	}
	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}
	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}
	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}
	public Integer getFinanzamtId() {
		return finanzamtId;
	}
	public void setFinanzamtId(Integer finanzamtId) {
		this.finanzamtId = finanzamtId;
	}
	public String getMandantCNr() {
		return mandantCNr;
	}
	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}
	public Integer getUvaartId() {
		return uvaartId;
	}
	public void setUvaartId(Integer uvaartId) {
		this.uvaartId = uvaartId;
	}
	public Integer getIGruppe() {
		return iGruppe;
	}
	public void setIGruppe(Integer iGruppe) {
		this.iGruppe = iGruppe;
	}
	public Integer getISort() {
		return iSort;
	}
	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}
	public String getCKennzeichen() {
		return cKennzeichen;
	}
	public void setCKennzeichen(String cKennzeichen) {
		this.cKennzeichen = cKennzeichen;
	}
}
