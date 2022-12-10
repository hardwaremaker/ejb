package com.lp.server.stueckliste.service;

import java.io.Serializable;

/**
 * &Uuml;ber Formel beeinflussbare Artikeleigenschaften verarbeiten
 * 
 * @author gerold
 */
public class FormelArtikel implements Serializable {
	private static final long serialVersionUID = -7928778318047219843L;
	
	private String cnr;
	private boolean generiereCnr;
	private String cKbez;
	private String cBez;
	private String cZbez;
	private String cZbez2;
		
	
	public void setCnr(String cnr) {
		this.cnr = cnr;
	}
	
	/**
	 * Die "gew&uuml;nschte" Artikelnummer
	 * @return die gew&uuml;nschte Artikelnummer
	 */
	public String getCnr() {
		return cnr;
	}
	
	public boolean hatCnr() {
		return this.cnr != null;
	}
	
	public void setGeneriereCnr(boolean generiereCnr) {
		this.generiereCnr = generiereCnr;
	}
	
	/**
	 * Soll die vorhandene <code>cnr</code> als Schablone 
	 * f&uuml;r die Generierung einer Artikelnummer verwendet
	 * werden, oder sie genau so &uuml;bernommen werden?
	 * 
	 * @return true, wenn cnr die Schablone f&uuml;r das Generieren 
	 * einer Artikelnummer ist.
	 */
	public boolean getGeneriereCnr() {
		return this.generiereCnr;
	}

	/**
	 * 
	 * @return die gew&uuml;nschte Kurzbezeichnung des Artikels
	 */
	public String getcKbez() {
		return cKbez;
	}

	public void setcKbez(String cKbez) {
		this.cKbez = cKbez;
	}

	/**
	 * 
	 * @return die gew&uuml;nschte Bezeichnung des Artikels
	 */
	public String getcBez() {
		return cBez;
	}

	public void setcBez(String cBez) {
		this.cBez = cBez;
	}

	/**
	 * 
	 * @return die gew&uuml;nschte Zusatzbezeichnung des Artikels
	 */
	public String getcZbez() {
		return cZbez;
	}

	public void setcZbez(String cZbez) {
		this.cZbez = cZbez;
	}

	/**
	 * 
	 * @return die gew&uuml;nschte Zusatzbezeichnung2 des Artikels
	 */
	public String getcZbez2() {
		return cZbez2;
	}

	public void setcZbez2(String cZbez2) {
		this.cZbez2 = cZbez2;
	}
}
