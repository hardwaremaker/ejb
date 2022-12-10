package com.lp.server.finanz.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UstUebersetzungPK implements Serializable {
	private static final long serialVersionUID = 4724555324285891502L;

	@Column(name = "C_NR")
	private String cNr;
	
	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;
	
	public UstUebersetzungPK() {
	}
	
	public UstUebersetzungPK(String cNr, String mandantCNr) {
		setCNr(cNr);
		setMandantCNr(mandantCNr);
	}

	public String getCNr() {
		return cNr;
	}
	
	public void setCNr(String cNr) {
		this.cNr = cNr;
	}
	
	public String getMandantCNr() {
		return mandantCNr;
	}
	
	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		
		if (!(o instanceof UstUebersetzungPK)) {
			return false;
		}
		
		UstUebersetzungPK other = (UstUebersetzungPK) o;
		return this.cNr.equals(other.cNr) && this.mandantCNr.equals(other.mandantCNr);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.cNr.hashCode();
		hash = hash * prime + this.mandantCNr.hashCode();
		return hash;
	}
}
