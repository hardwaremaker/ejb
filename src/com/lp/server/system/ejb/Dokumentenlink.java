/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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
package com.lp.server.system.ejb;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "DokumentenlinkfindByBelegartCNrMandantCNr", query = "SELECT OBJECT (o) FROM Dokumentenlink o WHERE o.belegartCNr=?1 AND o.mandantCNr=?2"),
		@NamedQuery(name = "DokumentenlinkfindByBelegartCNrMandantCNrBPfadabsolut", query = "SELECT OBJECT (o) FROM Dokumentenlink o WHERE o.belegartCNr=?1 AND o.mandantCNr=?2 AND o.bPfadabsolut=?3")})
@Entity
@Table(name = "LP_DOKUMENTENLINK")
public class Dokumentenlink implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "BELEGART_C_NR")
	private String belegartCNr;

	@Column(name = "C_BASISPFAD")
	private String cBasispfad;

	@Column(name = "C_ORDNER")
	private String cOrdner;
	@Column(name = "C_MENUETEXT")
	private String cMenuetext;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;
	public String getBelegartCNr() {
		return belegartCNr;
	}

	@Column(name = "B_PFADABSOLUT")
	private Short bPfadabsolut;

	@Column(name = "B_URL")
	private Short bUrl;

	
	public Short getBPfadabsolut() {
		return bPfadabsolut;
	}

	public void setBPfadabsolut(Short bPfadabsolut) {
		this.bPfadabsolut = bPfadabsolut;
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


	private static final long serialVersionUID = 1L;

	public Dokumentenlink() {
		super();
	}

	public Dokumentenlink(Integer id,String belegartCNr, String basispfad,String mandantCNr, String menuetext, Short bPfadabsolut, Short bUrl) {
		setIId(id);
		setCBasispfad(basispfad);
		setCMenuetext(menuetext);
		setBelegartCNr(belegartCNr);
		setMandantCNr(mandantCNr);
		setBPfadabsolut(bPfadabsolut);
		setBUrl(bUrl);
		
	}

	public Short getBUrl() {
		return bUrl;
	}

	public void setBUrl(Short bUrl) {
		this.bUrl = bUrl;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}


	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandant) {
		this.mandantCNr = mandant;
	}



}
