package com.lp.server.finanz.service;

import java.io.Serializable;

public class UstWarnungDto implements Serializable {
	private static final long serialVersionUID = 4836122946765687497L;

	enum UstWarnung {
		Ok,
		SteuerbetragErwartet,
		SteuerbetragUnerwartet
	}

	private Integer kontoId;
	private Integer kontoUebersetztId;
	private UstWarnung warnung;
	private boolean steuersatzKorrekt;
	
	public UstWarnungDto() {
		setWarnung(UstWarnung.Ok);
		beSteuersatzKorrekt();
	}
	
	public Integer getKontoId() {
		return kontoId;
	}

	public void setKontoId(Integer kontoId) {
		this.kontoId = kontoId;
	}

	public void beSteuerbetragObwohlNichtErwartet() {
		warnung = UstWarnung.SteuerbetragUnerwartet;
	}
	
	public boolean hasSteuerbetragObwohlNichtErwartet() {
		return warnung.equals(UstWarnung.SteuerbetragUnerwartet);
	}	
	
	public void beKeineSteuerObwohlErwartet() {
		warnung = UstWarnung.SteuerbetragErwartet;
	}
	public boolean hasKeineSteuerObwohlErwartet() {
		return warnung.equals(UstWarnung.SteuerbetragErwartet);
	}
	
	public boolean hasWarnung() {
		return !warnung.equals(UstWarnung.Ok);
	}
	
	public UstWarnung getWarnung() {
		return warnung;
	}

	public void setWarnung(UstWarnung warnung) {
		this.warnung = warnung;
	}

	public Integer getKontoUebersetztId() {
		return kontoUebersetztId;
	}

	public void setKontoUebersetztId(Integer kontoUebersetztId) {
		this.kontoUebersetztId = kontoUebersetztId;
	}
	
	public boolean isSteuersatzKorrekt() {
		return steuersatzKorrekt;
	}
	
	public void beSteuersatzKorrekt() {
		steuersatzKorrekt = true;
	}
	
	public void beSteuersatzFalsch() {
		steuersatzKorrekt = false;
	}
}
