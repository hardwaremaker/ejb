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
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WW_HANDLAGERBEWEGUNG")
public class Handlagerbewegung implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "N_MENGE")
	private BigDecimal nMenge;

	@Column(name = "C_KOMMENTAR")
	private String cKommentar;

	@Column(name = "T_BUCHUNGSZEIT")
	private Timestamp tBuchungszeit;

	@Column(name = "N_VERKAUFSPREIS")
	private BigDecimal nVerkaufspreis;

	@Column(name = "N_EINSTANDSPREIS")
	private BigDecimal nEinstandspreis;

	@Column(name = "N_GESTEHUNGSPREIS")
	private BigDecimal nGestehungspreis;

	@Column(name = "B_ABGANG")
	private Short bAbgang;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;

	@Column(name = "LAGER_I_ID")
	private Integer lagerIId;

	private static final long serialVersionUID = 1L;

	public Handlagerbewegung() {
		super();
	}

	public Handlagerbewegung(Integer id,
			Integer artikelIId2,
			Integer lagerIId2,
			BigDecimal menge, 
			String kommentar,
			Integer personal,
			Short abgang) {
		setIId(id);
		setArtikelIId(artikelIId2);
	    setLagerIId(lagerIId2);
	    setTBuchungszeit(new Timestamp(System.currentTimeMillis()));
	    setPersonalIIdAendern(personal);
	    setBAbgang(abgang);
	    setCKommentar(kommentar);
	    setNMenge(menge);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public BigDecimal getNMenge() {
		return this.nMenge;
	}

	public void setNMenge(BigDecimal nMenge) {
		this.nMenge = nMenge;
	}

	public String getCKommentar() {
		return this.cKommentar;
	}

	public void setCKommentar(String cKommentar) {
		this.cKommentar = cKommentar;
	}

	public Timestamp getTBuchungszeit() {
		return this.tBuchungszeit;
	}

	public void setTBuchungszeit(Timestamp tBuchungszeit) {
		this.tBuchungszeit = tBuchungszeit;
	}

	public BigDecimal getNVerkaufspreis() {
		return this.nVerkaufspreis;
	}

	public void setNVerkaufspreis(BigDecimal nVerkaufspreis) {
		this.nVerkaufspreis = nVerkaufspreis;
	}

	public BigDecimal getNEinstandspreis() {
		return this.nEinstandspreis;
	}

	public void setNEinstandspreis(BigDecimal nEinstandspreis) {
		this.nEinstandspreis = nEinstandspreis;
	}

	public BigDecimal getNGestehungspreis() {
		return this.nGestehungspreis;
	}

	public void setNGestehungspreis(BigDecimal nGestehungspreis) {
		this.nGestehungspreis = nGestehungspreis;
	}

	public Short getBAbgang() {
		return this.bAbgang;
	}

	public void setBAbgang(Short bAbgang) {
		this.bAbgang = bAbgang;
	}

	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Integer getArtikelIId() {
		return this.artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public Integer getLagerIId() {
		return this.lagerIId;
	}

	public void setLagerIId(Integer lagerIId) {
		this.lagerIId = lagerIId;
	}

}
