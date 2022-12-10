package com.lp.server.system.mail.service;

import java.io.Serializable;
import java.sql.Timestamp;

import com.lp.server.util.ICNr;

public class MailPropertyDto implements ICNr, Serializable {
	private static final long serialVersionUID = 2919777333160202592L;

	private String cNr;
	private String cWert;
	private String cDefaultWert;
	private String mandantCNr;
	private Integer personalIIdAendern;
	private Timestamp tAendern;
	
	public MailPropertyDto() {
	}
	
	public MailPropertyDto(String cNr, String cWert) {
		setCNr(cNr);
		setCWert(cWert);
	}
	
	public String getCNr() {
		return cNr;
	}
	public void setCNr(String cNr) {
		this.cNr = cNr;
	}
	
	public String getCWert() {
		return cWert;
	}
	public void setCWert(String cWert) {
		this.cWert = cWert;
	}
	
	public String getCDefaultWert() {
		return cDefaultWert;
	}
	public void setCDefaultWert(String cDefaultWert) {
		this.cDefaultWert = cDefaultWert;
	}
	
	public String getMandantCNr() {
		return mandantCNr;
	}
	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}
	
	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}
	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}
	
	public Timestamp getTAendern() {
		return tAendern;
	}
	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) 
			return true;
		
		if (!(obj instanceof MailPropertyDto))
			return false;
		
		MailPropertyDto that = (MailPropertyDto)obj;
		if (! (that.cNr == null ? getCNr() == null : that.cNr.equals(getCNr())))
			return false;
		
		if (! (that.cWert == null ? getCWert() == null : that.cWert.equals(getCWert())))
			return false;

		if (! (that.mandantCNr == null ? getMandantCNr() == null : that.mandantCNr.equals(getMandantCNr())))
			return false;

		if (! (that.cDefaultWert == null ? getCDefaultWert() == null : that.cDefaultWert.equals(getCDefaultWert())))
			return false;

		if (! (that.tAendern == null ? getTAendern() == null : that.tAendern.equals(getTAendern())))
			return false;

		if (! (that.personalIIdAendern == null ? getPersonalIIdAendern() == null : that.personalIIdAendern.equals(getPersonalIIdAendern())))
			return false;

		return true;
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + getCNr().hashCode();
		result = 37 * result + getCWert().hashCode();
		result = 37 * result + getCDefaultWert().hashCode();
		result = 37 * result + getMandantCNr().hashCode();
		result = 37 * result + getTAendern().hashCode();
		result = 37 * result + getPersonalIIdAendern().hashCode();
		
		return result;
	}
}
