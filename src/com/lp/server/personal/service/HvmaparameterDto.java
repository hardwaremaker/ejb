package com.lp.server.personal.service;

import java.io.Serializable;

import com.lp.server.util.ICNr;

public class HvmaparameterDto implements Serializable, ICNr {
	private static final long serialVersionUID = -8591366577374532018L;

	private String cnr;
	private String kategorie;
	private String datentyp;	
	private String bemerkung;	
	private String defaultWert;	
	private Short uebertragen;
	
	@Override
	public String getCNr() {
		return cnr;
	}
	@Override
	public void setCNr(String cnr) {
		this.cnr = cnr;
	}
	
	public String getKategorie() {
		return kategorie;
	}
	public void setKategorie(String kategorie) {
		this.kategorie = kategorie;
	}
	public String getDatentyp() {
		return datentyp;
	}
	public void setDatentyp(String datentyp) {
		this.datentyp = datentyp;
	}
	public String getBemerkung() {
		return bemerkung;
	}
	public void setBemerkung(String bemerkung) {
		this.bemerkung = bemerkung;
	}
	public String getDefaultWert() {
		return defaultWert;
	}
	public void setDefaultWert(String defaultWert) {
		this.defaultWert = defaultWert;
	}
	public Short getUebertragen() {
		return uebertragen;
	}
	public void setUebertragen(Short uebertragen) {
		this.uebertragen = uebertragen;
	}
	
	public boolean isUebertragen() {
		return this.uebertragen == 1;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bemerkung == null) ? 0 : bemerkung.hashCode());
		result = prime * result + ((cnr == null) ? 0 : cnr.hashCode());
		result = prime * result + ((datentyp == null) ? 0 : datentyp.hashCode());
		result = prime * result + ((defaultWert == null) ? 0 : defaultWert.hashCode());
		result = prime * result + ((kategorie == null) ? 0 : kategorie.hashCode());
		result = prime * result + ((uebertragen == null) ? 0 : uebertragen.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HvmaparameterDto other = (HvmaparameterDto) obj;
		if (bemerkung == null) {
			if (other.bemerkung != null)
				return false;
		} else if (!bemerkung.equals(other.bemerkung))
			return false;
		if (cnr == null) {
			if (other.cnr != null)
				return false;
		} else if (!cnr.equals(other.cnr))
			return false;
		if (datentyp == null) {
			if (other.datentyp != null)
				return false;
		} else if (!datentyp.equals(other.datentyp))
			return false;
		if (defaultWert == null) {
			if (other.defaultWert != null)
				return false;
		} else if (!defaultWert.equals(other.defaultWert))
			return false;
		if (kategorie == null) {
			if (other.kategorie != null)
				return false;
		} else if (!kategorie.equals(other.kategorie))
			return false;
		if (uebertragen == null) {
			if (other.uebertragen != null)
				return false;
		} else if (!uebertragen.equals(other.uebertragen))
			return false;
		return true;
	}
}
