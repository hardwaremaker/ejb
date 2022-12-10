/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.server.system.service;

import java.io.Serializable;

public class DokumentenlinkDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String belegartCNr;
	private String cBasispfad;
	private String cOrdner;

	private String rechtCNr;

	public String getRechtCNr() {
		return rechtCNr;
	}

	public void setRechtCNr(String rechtCNr) {
		this.rechtCNr = rechtCNr;
	}
	
	private Short bUrl;
	private Short bTitel;
	
	public Short getBTitel() {
		return bTitel;
	}
	
	public void setBTitel(Short bTitel) {
		this.bTitel = bTitel;
	}

	public Short getBUrl() {
		return bUrl;
	}

	public void setBUrl(Short bUrl) {
		this.bUrl = bUrl;
	}

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer id) {
		iId = id;
	}

	public String getBelegartCNr() {
		return belegartCNr;
	}

	public void setBelegartCNr(String belegartCNr) {
		this.belegartCNr = belegartCNr;
	}

	public String getCBasispfad() {
		return cBasispfad;
	}

	public void setCBasispfad(String basispfad) {
		cBasispfad = basispfad;
	}

	public String getCOrdner() {
		return cOrdner;
	}

	public void setCOrdner(String ordner) {
		cOrdner = ordner;
	}

	public String getCMenuetext() {
		return cMenuetext;
	}

	public void setCMenuetext(String menuetext) {
		cMenuetext = menuetext;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	private String cMenuetext;
	private String mandantCNr;

	private Short bPfadabsolut;

	public Short getBPfadabsolut() {
		return bPfadabsolut;
	}

	public void setBPfadabsolut(Short bPfadabsolut) {
		this.bPfadabsolut = bPfadabsolut;
	}

	private Short bPfadAusArbeitsplatzparameter;

	public Short getBPfadAusArbeitsplatzparameter() {
		return bPfadAusArbeitsplatzparameter;
	}

	public void setBPfadAusArbeitsplatzparameter(Short bPfadAusArbeitsplatzparameter) {
		this.bPfadAusArbeitsplatzparameter = bPfadAusArbeitsplatzparameter;
	}
}
