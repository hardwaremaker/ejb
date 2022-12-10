package com.lp.server.personal.service;

import java.io.Serializable;
import java.util.Date;

public class SonderzeitenAntragEmailDto implements Serializable {
	private static final long serialVersionUID = 5563552526587680896L;

	private boolean urlaubsantrag = false;
	private boolean krankantrag = false;
	private Date von;
	private Date bis;
	private Integer personalId;
	
	public SonderzeitenAntragEmailDto() {
	}
	
	public SonderzeitenAntragEmailDto(boolean urlaubsantrag, boolean krankantrag, Date von, Date bis, Integer personalId) {
		this.urlaubsantrag = urlaubsantrag;
		this.krankantrag = krankantrag;
		this.von = von;
		this.bis = bis;
		this.personalId = personalId;
	}
	
	public boolean isUrlaubsantrag() {
		return urlaubsantrag;
	}
	
	public boolean isKrankantrag() {
		return krankantrag;
	}
	
	public Date getVon() {
		return von;
	}
	
	public Date getBis() {
		return bis;
	}
	 
	public Integer getPersonalId() {
		return personalId;
	}
}
