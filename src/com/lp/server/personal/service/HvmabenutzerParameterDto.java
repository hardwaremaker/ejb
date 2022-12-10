package com.lp.server.personal.service;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Transient;

import com.lp.server.util.ICNr;
import com.lp.server.util.IIId;
import com.lp.server.util.IMultipleKeyfields;
import com.lp.util.EJBExceptionLP;

public class HvmabenutzerParameterDto implements Serializable, IIId, ICNr, IMultipleKeyfields {
	private static final long serialVersionUID = -6638573822054791991L;

	private Integer iid;
	private String cnr;
	private String kategorie;
//	private String datentyp;	
//	private String bemerkung;	
	private String wert;	
//	private Short uebertragen;
	private Timestamp tAendern;
	private Integer personalIIdAendern;
	
	private Integer hvmabenutzerId;
	
	public Integer getHvmabenutzerId() {
		return hvmabenutzerId;
	}

	public void setHvmabenutzerId(Integer hvmabenutzerId) {
		this.hvmabenutzerId = hvmabenutzerId;
	}

	@Override
	public Integer getIId() {
		return iid;
	}

	@Override
	public void setIId(Integer iid) {
		this.iid = iid;
	}

	@Override
	@Transient
	public String[] getMKValue() {
		return new String[] {getCNr(), getKategorie()};
	}
	
	@Override
	public String getCNr() {
		return this.cnr;
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

//	public String getDatentyp() {
//		return datentyp;
//	}
//
//	public void setDatentyp(String datentyp) {
//		this.datentyp = datentyp;
//	}

//	public String getBemerkung() {
//		return bemerkung;
//	}
//
//	public void setBemerkung(String bemerkung) {
//		this.bemerkung = bemerkung;
//	}
//
	public String getWert() {
		return wert;
	}

	public void setWert(String wert) {
		this.wert = wert;
	}

	public void setWert(Integer wert) {
		this.wert = wert.toString();
	}
	
	public void setWert(Boolean wert) {
		this.wert = Boolean.TRUE.equals(wert) ? "1" : "0";
	}
	
//	public Short getUebertragen() {
//		return uebertragen;
//	}
//
//	public void setUebertragen(Short uebertragen) {
//		this.uebertragen = uebertragen;
//	}

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
//		result = prime * result + ((bemerkung == null) ? 0 : bemerkung.hashCode());
		result = prime * result + ((cnr == null) ? 0 : cnr.hashCode());
//		result = prime * result + ((datentyp == null) ? 0 : datentyp.hashCode());
		result = prime * result + ((kategorie == null) ? 0 : kategorie.hashCode());
		result = prime * result + ((personalIIdAendern == null) ? 0 : personalIIdAendern.hashCode());
		result = prime * result + ((tAendern == null) ? 0 : tAendern.hashCode());
//		result = prime * result + ((uebertragen == null) ? 0 : uebertragen.hashCode());
		result = prime * result + ((wert == null) ? 0 : wert.hashCode());
		return result;
	}

	public Integer asInteger() {
		try {
			return Integer.parseInt(getWert());
		} catch(NumberFormatException e) {
			
		}
		throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DATEN_INKOMPATIBEL,
				new Exception());
	}
	
	public Boolean asBoolean() {
		try {
			return Integer.parseInt(getWert()) == 1;
		} catch(NumberFormatException e) {		
		}

		throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DATEN_INKOMPATIBEL,
				new Exception());
	}

	public String asString() {
		String value = getWert().trim();
		if(".".equals(value)) return null;
		
		return value;
	}
	
	public List<String> asStrings() {
		String[] strings = getWert().split(",");
		return Arrays.asList(strings);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HvmabenutzerParameterDto other = (HvmabenutzerParameterDto) obj;
//		if (bemerkung == null) {
//			if (other.bemerkung != null)
//				return false;
//		} else if (!bemerkung.equals(other.bemerkung))
//			return false;
		if (cnr == null) {
			if (other.cnr != null)
				return false;
		} else if (!cnr.equals(other.cnr))
			return false;
//		if (datentyp == null) {
//			if (other.datentyp != null)
//				return false;
//		} else if (!datentyp.equals(other.datentyp))
//			return false;
		if (kategorie == null) {
			if (other.kategorie != null)
				return false;
		} else if (!kategorie.equals(other.kategorie))
			return false;
		if (personalIIdAendern == null) {
			if (other.personalIIdAendern != null)
				return false;
		} else if (!personalIIdAendern.equals(other.personalIIdAendern))
			return false;
		if (tAendern == null) {
			if (other.tAendern != null)
				return false;
		} else if (!tAendern.equals(other.tAendern))
			return false;
//		if (uebertragen == null) {
//			if (other.uebertragen != null)
//				return false;
//		} else if (!uebertragen.equals(other.uebertragen))
//			return false;
		if (wert == null) {
			if (other.wert != null)
				return false;
		} else if (!wert.equals(other.wert))
			return false;
		return true;
	}
}
