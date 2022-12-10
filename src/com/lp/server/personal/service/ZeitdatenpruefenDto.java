package com.lp.server.personal.service;

import java.io.Serializable;
import java.sql.Timestamp;

import com.lp.server.util.IIId;

public class ZeitdatenpruefenDto implements Serializable, IIId {
	private static final long serialVersionUID = -8268777158131388411L;

	private Integer iId;
	private Timestamp tZeit;
	private Short bTaetigkeitgeaendert;
	private String cBelegartnr;
	private Integer iBelegartid;
	private Integer iBelegartpositionid;
	private String cBemerkungzubelegart;
	private Timestamp tAnlegen;
	private Timestamp tAendern;
	private Short bAutomatikbuchung;
	private String xKommentar;
	private String cWowurdegebucht;
	private Integer personalIIdAendern;
	private Integer personalIIdAnlegen;
	private Integer personalIId;
	private Integer taetigkeitIId;
	private Integer artikelIId;
	private Integer fehlerCode;
	private String xFehlertext;
	
	public Integer getIId() {
		return iId;
	}
	public void setIId(Integer iId) {
		this.iId = iId;
	}
	public Timestamp getTZeit() {
		return tZeit;
	}
	public void setTZeit(Timestamp tZeit) {
		this.tZeit = tZeit;
	}
	public Short getBTaetigkeitgeaendert() {
		return bTaetigkeitgeaendert;
	}
	public void setBTaetigkeitgeaendert(Short bTaetigkeitgeaendert) {
		this.bTaetigkeitgeaendert = bTaetigkeitgeaendert;
	}
	public String getCBelegartnr() {
		return cBelegartnr;
	}
	public void setCBelegartnr(String cBelegartnr) {
		this.cBelegartnr = cBelegartnr;
	}
	public Integer getIBelegartid() {
		return iBelegartid;
	}
	public void setIBelegartid(Integer iBelegartid) {
		this.iBelegartid = iBelegartid;
	}
	public Integer getIBelegartpositionid() {
		return iBelegartpositionid;
	}
	public void setIBelegartpositionid(Integer iBelegartpositionid) {
		this.iBelegartpositionid = iBelegartpositionid;
	}
	public String getCBemerkungzubelegart() {
		return cBemerkungzubelegart;
	}
	public void setCBemerkungzubelegart(String cBemerkungzubelegart) {
		this.cBemerkungzubelegart = cBemerkungzubelegart;
	}
	public Timestamp getTAnlegen() {
		return tAnlegen;
	}
	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}
	public Timestamp getTAendern() {
		return tAendern;
	}
	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}
	public Short getBAutomatikbuchung() {
		return bAutomatikbuchung;
	}
	public void setBAutomatikbuchung(Short bAutomatikbuchung) {
		this.bAutomatikbuchung = bAutomatikbuchung;
	}
	public String getXKommentar() {
		return xKommentar;
	}
	public void setXKommentar(String xKommentar) {
		this.xKommentar = xKommentar;
	}
	public String getCWowurdegebucht() {
		return cWowurdegebucht;
	}
	public void setCWowurdegebucht(String cWowurdegebucht) {
		this.cWowurdegebucht = cWowurdegebucht;
	}
	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}
	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}
	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}
	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}
	public Integer getPersonalIId() {
		return personalIId;
	}
	public void setPersonalIId(Integer personalIId) {
		this.personalIId = personalIId;
	}
	public Integer getTaetigkeitIId() {
		return taetigkeitIId;
	}
	public void setTaetigkeitIId(Integer taetigkeitIId) {
		this.taetigkeitIId = taetigkeitIId;
	}
	public Integer getArtikelIId() {
		return artikelIId;
	}
	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}
	public Integer getFehlerCode() {
		return fehlerCode;
	}
	public void setFehlerCode(Integer fehlerCode) {
		this.fehlerCode = fehlerCode;
	}
	public String getXFehlertext() {
		return xFehlertext;
	}
	public void setXFehlertext(String xFehlertext) {
		this.xFehlertext = xFehlertext;
	}
	
	public void addAttributKommentar(String kommentar) {
		if(xKommentar == null) {
			xKommentar = "";
		}
		
		if(xKommentar.length() > 0) {
			xKommentar += " ";
		}
		
		xKommentar += "[" + kommentar + "]";
	}
}
