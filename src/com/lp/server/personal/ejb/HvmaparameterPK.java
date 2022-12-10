package com.lp.server.personal.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.lp.server.util.ICNr;

@Embeddable
public class HvmaparameterPK implements Serializable, ICNr {
	private static final long serialVersionUID = -7294959678877496953L;

	@Column(name = "C_NR")
	private String cnr;

	@Column(name = "C_KATEGORIE")
	private String kategorie;

	public HvmaparameterPK() {
	}
	
	public HvmaparameterPK(String cnr, String kategorie) {
		this.cnr = cnr;
		this.kategorie = kategorie;
	}
	
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cnr == null) ? 0 : cnr.hashCode());
		result = prime * result + ((kategorie == null) ? 0 : kategorie.hashCode());
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
		HvmaparameterPK other = (HvmaparameterPK) obj;
		if (cnr == null) {
			if (other.cnr != null)
				return false;
		} else if (!cnr.equals(other.cnr))
			return false;
		if (kategorie == null) {
			if (other.kategorie != null)
				return false;
		} else if (!kategorie.equals(other.kategorie))
			return false;
		return true;
	}
}
