package com.lp.server.personal.service;

import java.io.Serializable;

public class ZeitdatenpruefenAttributDto implements Serializable {
	private static final long serialVersionUID = 678852023707926215L;

	public enum Attribut {
		Personal,
		Artikel,
		Taetigkeit,
		Auftrag,
		Auftragposition,
		Projekt
	}

	private Integer originalValue;
	private Integer newValue;
	private Attribut attribut;
	
	public ZeitdatenpruefenAttributDto() {
	}
	
	public ZeitdatenpruefenAttributDto(Attribut attribut, Integer originalValue, Integer newValue) {
		setAttribut(attribut);
		setOriginalValue(originalValue);
		setNewValue(newValue);
	}
	
	public Integer getOriginalValue() {
		return originalValue;
	}

	public void setOriginalValue(Integer originalValue) {
		this.originalValue = originalValue;
	}

	public Integer getNewValue() {
		return newValue;
	}

	public void setNewValue(Integer newValue) {
		this.newValue = newValue;
	}

	public Attribut getAttribut() {
		return attribut;
	}

	public void setAttribut(Attribut attribut) {
		this.attribut = attribut;
	}
	
	public String toKommentar() {
		return attribut.toString() + ": " 
				+ (getOriginalValue() == null ? "null" : getOriginalValue())
				+ " -> " 
				+ (getNewValue() == null ? "null" : getNewValue());
	}
}
