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
package com.lp.server.artikel.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({ @NamedQuery(name = "SperrenfindByCBezMandantCNr", query = "SELECT OBJECT(c) FROM Sperren c WHERE c.cBez = ?1 AND c.mandantCNr = ?2"),
	 @NamedQuery(name = "SperrenfindBDurchfertigung", query = "SELECT OBJECT(c) FROM Sperren c WHERE c.bDurchfertigung = 1 AND c.mandantCNr = ?1"),
	 @NamedQuery(name = "SperrenfindByOBildMandantCNrNotNull", query = "SELECT OBJECT(o) FROM Sperren o WHERE o.oBild IS NOT NULL AND o.mandantCNr = ?1")})
@Entity
@Table(name = "WW_SPERREN")
public class Sperren implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_BEZ")
	private String cBez;

	@Column(name = "B_GESPERRT")
	private Short bGesperrt;

	@Column(name = "B_GESPERRTEINKAUF")
	private Short bGesperrteinkauf;

	@Column(name = "B_GESPERRTVERKAUF")
	private Short bGesperrtverkauf;

	@Column(name = "B_GESPERRTLOS")
	private Short bGesperrtlos;

	@Column(name = "B_GESPERRTSTUECKLISTE")
	private Short bGesperrtstueckliste;
	@Column(name = "B_DURCHFERTIGUNG")
	private Short bDurchfertigung;

	public Short getBDurchfertigung() {
		return bDurchfertigung;
	}

	
	@Column(name = "O_BILD")
	private byte[] oBild;
	
	public byte[] getOBild() {
		return this.oBild;
	}

	public void setOBild(byte[] oBild) {
		this.oBild = oBild;
	}
	
	public void setBDurchfertigung(Short bDurchfertigung) {
		this.bDurchfertigung = bDurchfertigung;
	}

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	private static final long serialVersionUID = 1L;

	public Sperren() {
		super();
	}

	public Sperren(Integer id, String bez, String mandantCNr, Short gesperrt,
			Short gesperrteinkauf, Short gesperrtverkauf, Short gesperrtlos,
			Short gesperrtstueckliste, Short durchfertigung) {
		setIId(id);
		setCBez(bez);
		setMandantCNr(mandantCNr);
		setBGesperrt(gesperrt);
		setBGesperrteinkauf(gesperrteinkauf);
		setBGesperrtverkauf(gesperrtverkauf);
		setBGesperrtlos(gesperrtlos);
		setBGesperrtstueckliste(gesperrtstueckliste);
		setBDurchfertigung(durchfertigung);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCBez() {
		return this.cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public Short getBGesperrt() {
		return this.bGesperrt;
	}

	public void setBGesperrt(Short bGesperrt) {
		this.bGesperrt = bGesperrt;
	}

	public Short getBGesperrteinkauf() {
		return this.bGesperrteinkauf;
	}

	public void setBGesperrteinkauf(Short bGesperrteinkauf) {
		this.bGesperrteinkauf = bGesperrteinkauf;
	}

	public Short getBGesperrtverkauf() {
		return this.bGesperrtverkauf;
	}

	public void setBGesperrtverkauf(Short bGesperrtverkauf) {
		this.bGesperrtverkauf = bGesperrtverkauf;
	}

	public Short getBGesperrtlos() {
		return this.bGesperrtlos;
	}

	public void setBGesperrtlos(Short bGesperrtlos) {
		this.bGesperrtlos = bGesperrtlos;
	}

	public Short getBGesperrtstueckliste() {
		return this.bGesperrtstueckliste;
	}

	public void setBGesperrtstueckliste(Short bGesperrtstueckliste) {
		this.bGesperrtstueckliste = bGesperrtstueckliste;
	}

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

}
