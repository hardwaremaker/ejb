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
package com.lp.server.fertigung.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({ @NamedQuery(name = "BedarfsuebernahmefindByStatusCNr", query = "SELECT OBJECT(o) FROM Bedarfsuebernahme o WHERE o.statusCNr=?1") })
@Entity
@Table(name = "FERT_BEDARFSUEBERNAHME")
public class Bedarfsuebernahme implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "LOS_I_ID")
	private Integer losIId;

	@Column(name = "N_WUNSCHMENGE")
	private BigDecimal nWunschmenge;

	@Column(name = "B_ZUSAETZLICH")
	private Short bZusaetzlich;

	@Column(name = "B_ABGANG")
	private Short bAbgang;

	@Column(name = "C_LOSNUMMER")
	private String cLosnummer;

	public String getCLosnummer() {
		return cLosnummer;
	}

	public void setCLosnummer(String cLosnummer) {
		this.cLosnummer = cLosnummer;
	}

	@Column(name = "C_ARTIKELNUMMER")
	private String cArtikelnummer;

	@Column(name = "STATUS_C_NR")
	private String statusCNr;

	public String getStatusCNr() {
		return statusCNr;
	}

	public void setStatusCNr(String statusCNr) {
		this.statusCNr = statusCNr;
	}

	@Column(name = "C_ARTIKELBEZEICHNUNG")
	private String cArtikelbezeichnung;

	@Column(name = "C_KOMMENTAR")
	private String cKommentar;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;

	@Column(name = "LOSSOLLMATERIAL_I_ID")
	private Integer lossollmaterialIId;

	@Column(name = "PERSONAL_I_ID_VERBUCHT_GEDRUCKT")
	private Integer personalIIdVerbuchtGedruckt;

	@Column(name = "T_VERBUCHT_GEDRUCKT")
	private Timestamp tVerbuchtGedruckt;

	@Column(name = "O_IMAGE")
	private byte[] oMedia;

	@Column(name = "T_WUNSCHTERMIN")
	private Timestamp tWunschtermin;

	public Timestamp getTWunschtermin() {
		return tWunschtermin;
	}

	public void setTWunschtermin(Timestamp tWunschtermin) {
		this.tWunschtermin = tWunschtermin;
	}

	public Integer getLosIId() {
		return losIId;
	}

	public void setLosIId(Integer losIId) {
		this.losIId = losIId;
	}

	public BigDecimal getNWunschmenge() {
		return nWunschmenge;
	}

	public void setNWunschmenge(BigDecimal nWunschmenge) {
		this.nWunschmenge = nWunschmenge;
	}

	public Short getBZusaetzlich() {
		return bZusaetzlich;
	}

	public void setBzusaetzlich(Short bZusaetzlich) {
		this.bZusaetzlich = bZusaetzlich;
	}

	public Short getBAbgang() {
		return bAbgang;
	}

	public void setBAbgang(Short bAbgang) {
		this.bAbgang = bAbgang;
	}

	public String getCArtikelnummer() {
		return cArtikelnummer;
	}

	public void setCArtikelnummer(String cArtikelnummer) {
		this.cArtikelnummer = cArtikelnummer;
	}

	public String getCArtikelbezeichnung() {
		return cArtikelbezeichnung;
	}

	public void setCArtikelbezeichnung(String cArtikelbezeichnung) {
		this.cArtikelbezeichnung = cArtikelbezeichnung;
	}

	public String getCKommentar() {
		return cKommentar;
	}

	public void setCKommentar(String cKommentar) {
		this.cKommentar = cKommentar;
	}

	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
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

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public Integer getLossollmaterialIId() {
		return lossollmaterialIId;
	}

	public void setLossollmaterialIId(Integer lossollmaterialIId) {
		this.lossollmaterialIId = lossollmaterialIId;
	}

	public Integer getPersonalIIdVerbuchtGedruckt() {
		return personalIIdVerbuchtGedruckt;
	}

	public void setPersonalIIdVerbuchtGedruckt(
			Integer personalIIdVerbuchtGedruckt) {
		this.personalIIdVerbuchtGedruckt = personalIIdVerbuchtGedruckt;
	}

	public Timestamp getTVerbuchtGedruckt() {
		return tVerbuchtGedruckt;
	}

	public void setTVerbuchtGedruckt(Timestamp tVerbuchtGedruckt) {
		this.tVerbuchtGedruckt = tVerbuchtGedruckt;
	}

	public byte[] getOMedia() {
		return oMedia;
	}

	public void setOMedia(byte[] oMedia) {
		this.oMedia = oMedia;
	}

	private static final long serialVersionUID = 1L;

	public Bedarfsuebernahme() {
		super();
	}

	public Bedarfsuebernahme(Integer id, String cLosnummer,
			Integer personalIIdAnlegen, Timestamp tAnlegen,
			Integer personalIIdAendern, Timestamp tAendern,
			Timestamp tWunschtermin, BigDecimal nWunschmenge, Short bAbgang,
			Short bZusaetzlich, String statusCNr) {
		setIId(id);
		setCLosnummer(cLosnummer);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setPersonalIIdAendern(personalIIdAendern);
		setTAnlegen(tAnlegen);
		setTAendern(tAendern);
		setTWunschtermin(tWunschtermin);
		setNWunschmenge(nWunschmenge);
		setBAbgang(bAbgang);
		setBzusaetzlich(bZusaetzlich);
		setStatusCNr(statusCNr);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

}
