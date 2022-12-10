package com.lp.server.system.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MailPropertyPK implements Serializable {
	private static final long serialVersionUID = 6244778986286885515L;

	@Column(name = "C_NR")
	private String cNr;
	
	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;
	
	public MailPropertyPK() {
	}
	
	public MailPropertyPK(String cNr, String mandantCNr) {
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
		if (o == this)
			return true;
		
		if (!(o instanceof MailPropertyPK))
			return false;
		
		MailPropertyPK other = (MailPropertyPK) o;
		return this.cNr.equals(other.cNr)
				&& this.mandantCNr.equals(other.mandantCNr);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.cNr.hashCode();
		hash = hash * prime + this.mandantCNr.hashCode();
		return hash;
	}
	
	@Override
	public String toString() {
		return "MailProperty cNr = '" + cNr + "', mandantCNr = '" + mandantCNr + "'";
	}
	
}
