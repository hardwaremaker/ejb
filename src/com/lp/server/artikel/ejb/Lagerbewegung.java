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
package com.lp.server.artikel.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

	
@NamedQueries( {
		@NamedQuery(name = "LagerbewegungfindIIdBuchungByIId", query = "SELECT OBJECT(C) FROM Lagerbewegung c WHERE c.iId = ?1"),
		@NamedQuery(name = "LagerbewegungfindLetzteintrag", query = "SELECT OBJECT(C) FROM Lagerbewegung c WHERE c.cBelegartnr = ?1 AND c.iBelegartpositionid = ?2 AND( c.cSeriennrchargennr = ?3 OR c.cSeriennrchargennr is null ) AND c.bHistorie = 0"),
		@NamedQuery(name = "LagerbewegungfindByIIdBuchung", query = "SELECT OBJECT(C) FROM Lagerbewegung c WHERE c.iIdBuchung = ?1 ORDER BY c.tBuchungszeit DESC"),
		@NamedQuery(name = "LagerbewegungfindByBelegartCNrBelegartPositionIIdCSeriennrchargennr", query = "SELECT OBJECT(C) FROM Lagerbewegung c WHERE c.cBelegartnr = ?1 AND c.iBelegartpositionid = ?2 AND( c.cSeriennrchargennr = ?3 OR c.cSeriennrchargennr is null )"),
		@NamedQuery(name = "LagerbewegungfindByArtikelIIdLagerIIdBAbgangCSeriennrchargennr", query = "SELECT OBJECT(C) FROM Lagerbewegung c WHERE c.artikelIId = ?1 AND c.lagerIId = ?2 AND c.bAbgang = ?3 AND ( c.cSeriennrchargennr = ?4 OR c.cSeriennrchargennr is null )"),
		@NamedQuery(name = "LagerbewegungfindByArtikelIIdBAbgang", query = "SELECT OBJECT(C) FROM Lagerbewegung c WHERE c.artikelIId = ?1 AND c.bAbgang = ?2 AND c.bVollstaendigverbraucht < 1"),
		@NamedQuery(name = "LagerbewegungfindByArtikelIIdSeriennrchargennr", query = "SELECT OBJECT(C) FROM Lagerbewegung c WHERE c.artikelIId = ?1 AND c.cSeriennrchargennr = ?2 AND c.bAbgang = ?3"),
		@NamedQuery(name = "LagerbewegungfindByArtikelIIdLagerIIdBAbgangCSeriennrchargennrBVollstaendigverbraucht", query = "SELECT OBJECT(C) FROM Lagerbewegung c WHERE c.artikelIId = ?1 AND c.lagerIId = ?2 AND c.bAbgang = ?3 AND ( c.cSeriennrchargennr = ?4 OR c.cSeriennrchargennr is null ) AND c.bVollstaendigverbraucht=?5 AND c.bHistorie=0"),
		@NamedQuery(name = "LagerbewegungejbSelectAllSeriennrchargennr", query = "SELECT  OBJECT(C) FROM Lagerbewegung AS c WHERE c.cBelegartnr = ?1 AND c.iBelegartpositionid = ?2 AND c.bHistorie = 0 AND c.nMenge>0 ORDER BY c.cSeriennrchargennr ASC"),
		@NamedQuery(name = "LagerbewegungfindByBelegartCNrBelegartPositionIId", query = "SELECT OBJECT(C) FROM Lagerbewegung c WHERE c.cBelegartnr = ?1 AND c.iBelegartpositionid = ?2 AND c.bHistorie = 0"),
		@NamedQuery(name = "LagerbewegungfindArtikelIId", query = "SELECT OBJECT(C) FROM Lagerbewegung c WHERE c.artikelIId = ?1"),
		@NamedQuery(name = "LagerbewegungfindAllSnrChnrEinesArtikels", query = "SELECT OBJECT(C) FROM Lagerbewegung c WHERE c.artikelIId = ?1 AND c.cSeriennrchargennr = ?2 "),
		@NamedQuery(name = "LagerbewegungfindSnr", query = "SELECT OBJECT(C) FROM Lagerbewegung c WHERE c.cSeriennrchargennr = ?1 AND c.bAbgang=0 AND c.bHistorie=0 AND c.nMenge>0 AND c.bVollstaendigverbraucht=0"),
		@NamedQuery(name = Lagerbewegung.QueryFindSnrAbgang, query = "SELECT OBJECT(C) FROM Lagerbewegung c WHERE c.cSeriennrchargennr = ?1 AND c.bAbgang=1 AND c.bHistorie=0 AND c.nMenge>0"),
		@NamedQuery(name = "LagerbewegungfindByBelegartCNrIBelegartid", query = "SELECT OBJECT(C) FROM Lagerbewegung c WHERE c.cBelegartnr = ?1 AND c.iBelegartid = ?2"),
		@NamedQuery(name = LagerbewegungQuery.ByLagerbewegungfindByArtikelIIdBAbgangBelegdatumCount, 
			query = "SELECT COUNT(C) FROM Lagerbewegung c WHERE c.artikelIId = :artikelId AND c.bAbgang=1 AND c.bVollstaendigverbraucht < 1 AND c.tBelegdatum > :belegDatum"),
		@NamedQuery(name = LagerbewegungQuery.ByBelegartCNrArtikelIIdCSeriennrchargennr, 
			query = "SELECT OBJECT(C) FROM Lagerbewegung c WHERE c.bHistorie=0 AND c.nMenge>0 AND c.cBelegartnr=:belegartnr AND c.artikelIId=:artikelId AND c.cSeriennrchargennr=:serienChargennr ORDER BY c.nMenge ASC"),
		@NamedQuery(name = LagerbewegungQuery.ByBelegartCNrArtikelIIdCSeriennrchargennrIBelegartIds, 
			query = "SELECT OBJECT(C) FROM Lagerbewegung c WHERE c.bHistorie=0 AND c.nMenge>0 AND c.cBelegartnr=:belegartnr AND c.artikelIId=:artikelId AND c.cSeriennrchargennr=:serienChargennr AND c.iBelegartid IN (:belegartids) ORDER BY c.nMenge ASC"),
		@NamedQuery(name = LagerbewegungQuery.ByBelegartCNrArtikelIIdCSeriennrchargennrIBelegartIdsNurAbgaenge, 
		query = "SELECT OBJECT(C) FROM Lagerbewegung c WHERE c.bHistorie=0 AND c.nMenge>0 AND c.cBelegartnr=:belegartnr AND c.artikelIId=:artikelId AND c.cSeriennrchargennr=:serienChargennr AND c.iBelegartid IN (:belegartids) AND c.bAbgang=1 ORDER BY c.nMenge ASC")
		})
@Entity
@Table(name = "WW_LAGERBEWEGUNG")
public class Lagerbewegung implements Serializable {
	
	public static final String QueryFindSnrAbgang = "LagerbewegungfindSnrAbgang" ;
	
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "I_BELEGARTID")
	private Integer iBelegartid;

	@Column(name = "I_BELEGARTPOSITIONID")
	private Integer iBelegartpositionid;

	@Column(name = "I_ID_BUCHUNG")
	private Integer iIdBuchung;

	@Column(name = "N_MENGE")
	private BigDecimal nMenge;

	@Column(name = "T_BUCHUNGSZEIT")
	private Timestamp tBuchungszeit;

	@Column(name = "C_SERIENNRCHARGENNR")
	private String cSeriennrchargennr;

	@Column(name = "C_VERSION")
	private String cVersion;

	public String getCVersion() {
		return cVersion;
	}

	public void setCVersion(String cVersion) {
		this.cVersion = cVersion;
	}

	@Column(name = "N_VERKAUFSPREIS")
	private BigDecimal nVerkaufspreis;

	@Column(name = "N_EINSTANDSPREIS")
	private BigDecimal nEinstandspreis;

	@Column(name = "N_GESTEHUNGSPREIS")
	private BigDecimal nGestehungspreis;

	@Column(name = "B_ABGANG")
	private Short bAbgang;
	
	@Column(name = "B_HISTORIE")
	private Short bHistorie;
	
	public Integer getHerstellerIId() {
		return herstellerIId;
	}

	public void setHerstellerIId(Integer herstellerIId) {
		this.herstellerIId = herstellerIId;
	}

	public Integer getLandIId() {
		return landIId;
	}

	public void setLandIId(Integer landIId) {
		this.landIId = landIId;
	}

	@Column(name = "HERSTELLER_I_ID")
	private Integer herstellerIId;

	@Column(name = "LAND_I_ID")
	private Integer landIId;
	
	
	public Short getBHistorie() {
		return bHistorie;
	}

	public void setBHistorie(Short historie) {
		bHistorie = historie;
	}

	@Column(name = "B_VOLLSTAENDIGVERBRAUCHT")
	private Short bVollstaendigverbraucht;

	@Column(name = "T_BELEGDATUM")
	private Timestamp tBelegdatum;

	@Column(name = "C_BELEGARTNR")
	private String cBelegartnr;

	@Column(name = "PERSONAL_I_ID_EINSTANDSPREISGEAENDERT")
	private Integer personalIIdEinstandspreisgeaendert;

	@Column(name = "PERSONAL_I_ID_VERKAUFSPREISGEAENDERT")
	private Integer personalIIdVerkaufspreisgeaendert;

	@Column(name = "PERSONAL_I_ID_MENGEGEAENDERT")
	private Integer personalIIdMengegeaendert;

	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;

	@Column(name = "LAGER_I_ID")
	private Integer lagerIId;

	@Column(name = "GEBINDE_I_ID")
	private Integer gebindeIId;

	public Integer getGebindeIId() {
		return gebindeIId;
	}

	public void setGebindeIId(Integer gebindeIId) {
		this.gebindeIId = gebindeIId;
	}
	
	@Column(name = "N_GEBINDEMENGE")
	private BigDecimal nGebindemenge;
	
	
	public BigDecimal getNGebindemenge() {
		return nGebindemenge;
	}

	public void setNGebindemenge(BigDecimal nGebindemenge) {
		this.nGebindemenge = nGebindemenge;
	}

	private static final long serialVersionUID = 1L;

	public Lagerbewegung() {
		super();
	}

	public Lagerbewegung(Integer id,
			String belegartnr,
			Integer belegartid,
			Integer belegartpositionid, 
			Integer idBuchung,
			Integer lagerIId2,
			Integer artikelIId2,
			BigDecimal menge,
			Integer personalIIdMengegeaendert2, 
			Short abgang,
			Short vollstaendigverbraucht, 	
			Timestamp belegdatum) {
		setIId(id);
		setCBelegartnr(belegartnr);
		setIBelegartpositionid(belegartpositionid);
		setIIdBuchung(idBuchung);
		setLagerIId(lagerIId2);
		setArtikelIId(artikelIId2);
		setNMenge(menge);
		setPersonalIIdMengegeaendert(personalIIdMengegeaendert2);
		setBAbgang(abgang);
		setBVollstaendigverbraucht(vollstaendigverbraucht);
		setIBelegartid(belegartid);
		setTBelegdatum(belegdatum);
		setTBuchungszeit(new Timestamp(System.currentTimeMillis()));
		setBHistorie(new Short((short)0));
		setNGestehungspreis(new BigDecimal(0));
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getIBelegartid() {
		return this.iBelegartid;
	}

	public void setIBelegartid(Integer iBelegartid) {
		this.iBelegartid = iBelegartid;
	}

	public Integer getIBelegartpositionid() {
		return this.iBelegartpositionid;
	}

	public void setIBelegartpositionid(Integer iBelegartpositionid) {
		this.iBelegartpositionid = iBelegartpositionid;
	}

	public Integer getIIdBuchung() {
		return this.iIdBuchung;
	}

	public void setIIdBuchung(Integer iIdBuchung) {
		this.iIdBuchung = iIdBuchung;
	}

	public BigDecimal getNMenge() {
		return this.nMenge;
	}

	public void setNMenge(BigDecimal nMenge) {
		this.nMenge = nMenge;
	}

	public Timestamp getTBuchungszeit() {
		return this.tBuchungszeit;
	}

	public void setTBuchungszeit(Timestamp tBuchungszeit) {
		this.tBuchungszeit = tBuchungszeit;
	}

	public String getCSeriennrchargennr() {
		return this.cSeriennrchargennr;
	}

	public void setCSeriennrchargennr(String cSeriennrchargennr) {
		this.cSeriennrchargennr = cSeriennrchargennr;
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

	public Short getBVollstaendigverbraucht() {
		return this.bVollstaendigverbraucht;
	}

	public void setBVollstaendigverbraucht(Short bVollstaendigverbraucht) {
		this.bVollstaendigverbraucht = bVollstaendigverbraucht;
	}

	public Timestamp getTBelegdatum() {
		return this.tBelegdatum;
	}

	public void setTBelegdatum(Timestamp tBelegdatum) {
		this.tBelegdatum = tBelegdatum;
	}

	public String getCBelegartnr() {
		return this.cBelegartnr;
	}

	public void setCBelegartnr(String cBelegartnr) {
		this.cBelegartnr = cBelegartnr;
	}

	public Integer getPersonalIIdEinstandspreisgeaendert() {
		return this.personalIIdEinstandspreisgeaendert;
	}

	public void setPersonalIIdEinstandspreisgeaendert(
			Integer personalIIdEinstandspreisgeaendert) {
		this.personalIIdEinstandspreisgeaendert = personalIIdEinstandspreisgeaendert;
	}

	public Integer getPersonalIIdVerkaufspreisgeaendert() {
		return this.personalIIdVerkaufspreisgeaendert;
	}

	public void setPersonalIIdVerkaufspreisgeaendert(
			Integer personalIIdVerkaufspreisgeaendert) {
		this.personalIIdVerkaufspreisgeaendert = personalIIdVerkaufspreisgeaendert;
	}

	public Integer getPersonalIIdMengegeaendert() {
		return this.personalIIdMengegeaendert;
	}

	public void setPersonalIIdMengegeaendert(Integer personalIIdMengegeaendert) {
		this.personalIIdMengegeaendert = personalIIdMengegeaendert;
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
