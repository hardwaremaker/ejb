package com.lp.server.artikel.service;

import java.io.Serializable;
import java.sql.Timestamp;

import com.lp.server.util.IIId;

public class ArtikelTruTopsDto implements Serializable, IIId {
	private static final long serialVersionUID = 1520775481277795084L;

	private Integer iId;
	private Integer artikelIId;
	private Timestamp tExportBeginn;
	private Timestamp tExportEnde;
	private String cPfad;
	private String cExportPfad;
	private String cFehlercode;
	private String cFehlertext;
	
	public ArtikelTruTopsDto() {
	}

	public Integer getIId() {
		return iId;
	}
	public void setIId(Integer iId) {
		this.iId = iId;
	}
	
	public Integer getArtikelIId() {
		return artikelIId;
	}
	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}
	
	public Timestamp getTExportBeginn() {
		return tExportBeginn;
	}
	public void setTExportBeginn(Timestamp tExportBeginn) {
		this.tExportBeginn = tExportBeginn;
	}
	
	public Timestamp getTExportEnde() {
		return tExportEnde;
	}
	public void setTExportEnde(Timestamp tExportEnde) {
		this.tExportEnde = tExportEnde;
	}
	
	public String getCPfad() {
		return cPfad;
	}
	public void setCPfad(String cPfad) {
		this.cPfad = cPfad;
	}
	
	public String getCExportPfad() {
		return cExportPfad;
	}
	public void setCExportPfad(String cExportPfad) {
		this.cExportPfad = cExportPfad;
	}
	
	public String getCFehlercode() {
		return cFehlercode;
	}
	public void setCFehlercode(String cFehlercode) {
		this.cFehlercode = cFehlercode;
	}
	
	public String getCFehlertext() {
		return cFehlertext;
	}
	public void setCFehlertext(String cFehlertext) {
		this.cFehlertext = cFehlertext;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((iId == null) ? 0 : iId.hashCode());
		result = prime * result + ((artikelIId == null) ? 0 : artikelIId.hashCode());
		result = prime * result + ((tExportBeginn == null) ? 0 : tExportBeginn.hashCode());
		result = prime * result + ((tExportEnde == null) ? 0 : tExportEnde.hashCode());
		result = prime * result + ((cExportPfad == null) ? 0 : cExportPfad.hashCode());
		result = prime * result + ((cPfad == null) ? 0 : cPfad.hashCode());
		result = prime * result + ((cFehlercode == null) ? 0 : cFehlercode.hashCode());
		result = prime * result + ((cFehlertext == null) ? 0 : cFehlertext.hashCode());
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
		
		ArtikelTruTopsDto other = (ArtikelTruTopsDto) obj;
		if (iId == null) {
			if (other.iId != null)
				return false;
		} else if (!iId.equals(other.iId))
			return false;
		
		if (artikelIId == null) {
			if (other.artikelIId != null)
				return false;
		} else if (!artikelIId.equals(other.artikelIId))
			return false;
		
		if (tExportBeginn == null) {
			if (other.tExportBeginn != null)
				return false;
		} else if (!tExportBeginn.equals(other.tExportBeginn))
			return false;
		
		if (tExportEnde == null) {
			if (other.tExportEnde != null)
				return false;
		} else if (!tExportEnde.equals(other.tExportEnde))
			return false;
		
		if (cExportPfad == null) {
			if (other.cExportPfad != null)
				return false;
		} else if (!cExportPfad.equals(other.cExportPfad))
			return false;
		
		if (cPfad == null) {
			if (other.cPfad != null)
				return false;
		} else if (!cPfad.equals(other.cPfad))
			return false;
		
		if (cFehlercode == null) {
			if (other.cFehlercode != null)
				return false;
		} else if (!cFehlercode.equals(other.cFehlercode))
			return false;
		
		if (cFehlertext == null) {
			if (other.cFehlertext != null)
				return false;
		} else if (!cFehlertext.equals(other.cFehlertext))
			return false;
		
		return true;
	}

}
