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
package com.lp.server.partner.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "KundesokofindByKundeIIdArtikelIId", query = "SELECT OBJECT (o) FROM Kundesoko o WHERE o.kundeIId=?1 AND o.artikelIId=?2"),
		@NamedQuery(name = "KundesokofindByArtikelIId", query = "SELECT OBJECT (o) FROM Kundesoko o WHERE o.artikelIId=?1"),
		@NamedQuery(name = "KundesokofindByKundeIIdArtgruIId", query = "SELECT OBJECT (o) FROM Kundesoko o WHERE o.kundeIId=?1 AND o.artgruIId=?2"),
		@NamedQuery(name = "KundesokofindByKundeIIdArtikelIIdGueltigkeitsdatum", query = "SELECT OBJECT (o) FROM Kundesoko o WHERE o.kundeIId=?1 AND o.artikelIId=?2 AND ((o.tPreisgueltigab<=?3 AND o.tPreisgueltigbis>=?3) OR (o.tPreisgueltigab<=?3 AND o.tPreisgueltigbis IS NULL))"),
		@NamedQuery(name = "KundesokofindByKundeIIdArtgruIIdGueltigkeitsdatum", query = "SELECT OBJECT (o) FROM Kundesoko o WHERE o.kundeIId=?1 AND o.artgruIId=?2 AND ((o.tPreisgueltigab<=?3 AND o.tPreisgueltigbis>=?3) OR (o.tPreisgueltigab<=?3 AND o.tPreisgueltigbis IS NULL))"),
		@NamedQuery(name = "KundesokofindByKundeIIdArtikelIIdTPreisgueltigab", query = "SELECT OBJECT (o) FROM Kundesoko o WHERE o.kundeIId=?1 AND o.artikelIId=?2 AND o.tPreisgueltigab=?3"),
		@NamedQuery(name = "KundesokofindByKundeIIdArtgruIIdTPreisgueltigab", query = "SELECT OBJECT (o) FROM Kundesoko o WHERE o.kundeIId=?1 AND o.artikelIId=?2 AND o.tPreisgueltigab=?3"),
		@NamedQuery(name = "KundesokofindByKundeIId", query = "SELECT OBJECT (O) FROM Kundesoko o WHERE o.kundeIId=?1"),
		@NamedQuery(name = KundesokoQuery.ByKundeIIdArtikelnummer,
			query = "SELECT OBJECT(O) FROM Kundesoko o where o.kundeIId=:kundeIId AND o.cKundeartikelnummer=:artikelnummer " +
				"AND ((o.tPreisgueltigab<=:tGueltig AND o.tPreisgueltigbis>=:tGueltig) OR (o.tPreisgueltigab<=:tGueltig AND o.tPreisgueltigbis IS NULL))"),
		@NamedQuery(name = KundesokoQuery.ByArtikelIIdGueltigkeitsdatum,
			query = "SELECT OBJECT(O) FROM Kundesoko o where o.artikelIId=:artikelIId " +
				"AND ((o.tPreisgueltigab<=:tGueltig AND o.tPreisgueltigbis>=:tGueltig) OR (o.tPreisgueltigab<=:tGueltig AND o.tPreisgueltigbis IS NULL))"),
		@NamedQuery(name = KundesokoQuery.ByArtgruIIdGueltigkeitsdatum,
			query = "SELECT OBJECT(O) FROM Kundesoko o where o.artgruIId=:artgruIId " +
				"AND ((o.tPreisgueltigab<=:tGueltig AND o.tPreisgueltigbis>=:tGueltig) OR (o.tPreisgueltigab<=:tGueltig AND o.tPreisgueltigbis IS NULL))")				
		})
@Entity
@Table(name = "PART_KUNDESOKO")
public class Kundesoko implements Serializable {
	public Kundesoko() {
	}

	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "T_PREISGUELTIGAB")
	private Date tPreisgueltigab;

	@Column(name = "T_PREISGUELTIGBIS")
	private Date tPreisgueltigbis;

	@Column(name = "C_BEMERKUNG")
	private String cBemerkung;

	@Column(name = "N_STARTWERT_LIEFERMENGE")
	private BigDecimal nStartwertLiefermenge;
	
	public BigDecimal getNStartwertLiefermenge() {
		return nStartwertLiefermenge;
	}

	public void setNStartwertLiefermenge(BigDecimal nStartwertLiefermenge) {
		this.nStartwertLiefermenge = nStartwertLiefermenge;
	}

	@Column(name = "B_BEMERKUNGDRUCKEN")
	private Short bBemerkungdrucken;

	@Column(name = "B_RABATTSICHTBAR")
	private Short bRabattsichtbar;

	@Column(name = "B_DRUCKEN")
	private Short bDrucken;

	@Column(name = "B_WIRKT_NICHT_FUER_PREISFINDUNG")
	private Short bWirktNichtFuerPreisfindung;
	
	
	public Short getBWirktNichtFuerPreisfindung() {
		return bWirktNichtFuerPreisfindung;
	}

	public void setBWirktNichtFuerPreisfindung(Short bWirktNichtFuerPreisfindung) {
		this.bWirktNichtFuerPreisfindung = bWirktNichtFuerPreisfindung;
	}

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "C_KUNDEARTIKELNUMMER")
	private String cKundeartikelnummer;

	@Column(name = "C_KUNDEARTIKELBEZ")
	private String cKundeartikelbez;
	
	@Column(name = "C_KUNDEARTIKELZBEZ")
	private String cKundeartikelzbez;

	public String getCKundeartikelbez() {
		return cKundeartikelbez;
	}

	public void setCKundeartikelbez(String cKundeartikelbez) {
		this.cKundeartikelbez = cKundeartikelbez;
	}

	public String getCKundeartikelzbez() {
		return cKundeartikelzbez;
	}

	public void setCKundeartikelzbez(String cKundeartikelzbez) {
		this.cKundeartikelzbez = cKundeartikelzbez;
	}

	@Column(name = "B_KEINE_MENGENSTAFFEL")
	private Short bKeineMengenstaffel;

	public Short getBKeineMengenstaffel() {
		return bKeineMengenstaffel;
	}

	public void setBKeineMengenstaffel(Short bKeineMengenstaffel) {
		this.bKeineMengenstaffel = bKeineMengenstaffel;
	}
	
	@Column(name = "KUNDE_I_ID")
	private Integer kundeIId;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "ARTGRU_I_ID")
	private Integer artgruIId;

	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;

	private static final long serialVersionUID = 1L;

	public Kundesoko(Integer iId, Integer kundeIId, Date tPreisgueltigab,
			Integer personalIIdAendern) {
		setIId(iId);
		setKundeIId(kundeIId);
		setTPreisgueltigab(tPreisgueltigab);
		setPersonalIIdAendern(personalIIdAendern);
		setBBemerkungdrucken(new Short((short) 0));
		setBDrucken(new Short((short) 0));
		setBRabattsichtbar(new Short((short) 0));
		setBWirktNichtFuerPreisfindung(new Short((short) 0));
		setBKeineMengenstaffel(new Short((short) 0));
		setTAendern(new Timestamp(System.currentTimeMillis()));
	}

	public Kundesoko(Integer iId, Integer kundeIId, Timestamp tPreisgueltigab,
			Short bemerkungdrucken, Short rabattsichtbar, Short drucken,
			Integer personalIIdAendern,Short wirktnichtfuerpreisfindung, Short bKeineMengenstaffel) {
		setIId(iId);
		setKundeIId(kundeIId);
		setTPreisgueltigab(new Date(tPreisgueltigab.getTime()));
		setPersonalIIdAendern(personalIIdAendern);
		setBBemerkungdrucken(bemerkungdrucken);
		setBDrucken(drucken);
		setBRabattsichtbar(rabattsichtbar);
		setTAendern(new Timestamp(System.currentTimeMillis()));
		setBWirktNichtFuerPreisfindung(wirktnichtfuerpreisfindung);
		setBKeineMengenstaffel(bKeineMengenstaffel);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Date getTPreisgueltigab() {
		return this.tPreisgueltigab;
	}

	public void setTPreisgueltigab(Date tPreisgueltigab) {
		this.tPreisgueltigab = tPreisgueltigab;
	}

	public Date getTPreisgueltigbis() {
		return this.tPreisgueltigbis;
	}

	public void setTPreisgueltigbis(Date tPreisgueltigbis) {
		this.tPreisgueltigbis = tPreisgueltigbis;
	}

	public String getCBemerkung() {
		return this.cBemerkung;
	}

	public void setCBemerkung(String cBemerkung) {
		this.cBemerkung = cBemerkung;
	}

	public Short getBBemerkungdrucken() {
		return this.bBemerkungdrucken;
	}

	public void setBBemerkungdrucken(Short bBemerkungdrucken) {
		this.bBemerkungdrucken = bBemerkungdrucken;
	}

	public Short getBRabattsichtbar() {
		return this.bRabattsichtbar;
	}

	public void setBRabattsichtbar(Short bRabattsichtbar) {
		this.bRabattsichtbar = bRabattsichtbar;
	}

	public Short getBDrucken() {
		return this.bDrucken;
	}

	public void setBDrucken(Short bDrucken) {
		this.bDrucken = bDrucken;
	}

	public Timestamp getTAendern() {
		return this.tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public String getCKundeartikelnummer() {
		return this.cKundeartikelnummer;
	}

	public void setCKundeartikelnummer(String cKundeartikelnummer) {
		this.cKundeartikelnummer = cKundeartikelnummer;
	}

	public Integer getKundeIId() {
		return this.kundeIId;
	}

	public void setKundeIId(Integer kundeIId) {
		this.kundeIId = kundeIId;
	}

	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Integer getArtgruIId() {
		return this.artgruIId;
	}

	public void setArtgruIId(Integer artgruIId) {
		this.artgruIId = artgruIId;
	}

	public Integer getArtikelIId() {
		return this.artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

}
