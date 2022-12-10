package com.lp.server.system.service;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class HvBelegnummernformat implements IHvBelegnummernformat, Serializable {
	private static final long serialVersionUID = 7369324777725478866L;

	private Integer laengeGj;
	private Integer laengeBelegnr;
	private String trennzeichen;
	private String mandantkennung;
	
	public HvBelegnummernformat(Integer laengeGj, Integer laengeBelegnr, String trennzeichen, String mandantkennung) {
		this.laengeGj = laengeGj;
		this.laengeBelegnr = laengeBelegnr;
		this.trennzeichen = trennzeichen;
		this.mandantkennung = mandantkennung;
	}
	protected HvBelegnummernformat() {
		
	}

	public Integer getLaengeBelegnr() {
		return laengeBelegnr;
	}
	public void setLaengeBelegnr(Integer laengeBelegnr) {
		this.laengeBelegnr = laengeBelegnr;
	}
	
	public Integer getLaengeGj() {
		return laengeGj;
	}
	public void setLaengeGj(Integer laengeGj) {
		this.laengeGj = laengeGj;
	}
	
	public String getTrennzeichen() {
		return trennzeichen;
	}
	public void setTrennzeichen(String trennzeichen) {
		this.trennzeichen = trennzeichen;
	}
	
	public String getMandantkennung() {
		return mandantkennung;
	}
	public void setMandantkennung(String mandantkennung) {
		this.mandantkennung = mandantkennung;
	}
	
	public Integer getLaengeGesamt() {
		return laengeGj + laengeBelegnr + trennzeichen.length() + (mandantkennung != null ? mandantkennung.length() : 0);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!IHvBelegnummernformat.class.isInstance(obj))
			return false;
			
		IHvBelegnummernformat other = (IHvBelegnummernformat)obj;
		if (getLaengeBelegnr() == null) {
			if (other.getLaengeBelegnr() != null) return false;
		} else if (!getLaengeBelegnr().equals(other.getLaengeBelegnr())) {
			return false;
		}
		if (getLaengeGj() == null) {
			if (other.getLaengeGj() != null) return false;
		} else if (!getLaengeGj().equals(other.getLaengeGj())) {
			return false;
		}
		if (getTrennzeichen() == null) {
			if (other.getTrennzeichen() != null) return false;
		} else if (!getTrennzeichen().equals(other.getTrennzeichen())) {
			return false;
		}
		if (StringUtils.isEmpty(getMandantkennung())) {
			if (!StringUtils.isEmpty(getMandantkennung())) return false;
		} else if (!getMandantkennung().equals(other.getMandantkennung())) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getLaengeBelegnr() == null) ? 0 : getLaengeBelegnr().hashCode());
		result = prime * result + ((getLaengeGj() == null) ? 0 : getLaengeGj().hashCode());
		result = prime * result + ((getTrennzeichen() == null) ? 0 : getTrennzeichen().hashCode());
		result = prime * result + ((getMandantkennung() == null) ? 0 : getMandantkennung().hashCode());
		return result;
	}
}
