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
package com.lp.server.stueckliste.ejb;

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
		@NamedQuery(name = "StuecklistefindByArtikelIIdMandantCNr", query = "SELECT OBJECT(o) FROM Stueckliste o WHERE o.artikelIId=?1 AND o.mandantCNr = ?2"),
		@NamedQuery(name = "StuecklistefindByPartnerIIdMandantCNr", query = "SELECT OBJECT(O) FROM Stueckliste o WHERE o.partnerIId=?1 AND o.mandantCNr = ?2") })
@Entity
@Table(name = "STK_STUECKLISTE")
public class Stueckliste implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "B_FREMDFERTIGUNG")
	private Short bFremdfertigung;

	@Column(name = "B_UEBERLIEFERBAR")
	private Short bUeberlieferbar;
	
	@Column(name = "B_DRUCKEINLAGERSTANDSDETAIL")
	private Short bDruckeinlagerstandsdetail;

	public Short getBDruckeinlagerstandsdetail() {
		return bDruckeinlagerstandsdetail;
	}

	public void setBDruckeinlagerstandsdetail(Short bDruckeinlagerstandsdetail) {
		this.bDruckeinlagerstandsdetail = bDruckeinlagerstandsdetail;
	}

	public Short getBUeberlieferbar() {
		return bUeberlieferbar;
	}

	public void setBUeberlieferbar(Short bUeberlieferbar) {
		this.bUeberlieferbar = bUeberlieferbar;
	}

	@Column(name = "N_LOSGROESSE")
	private BigDecimal nLosgroesse;

	@Column(name = "N_DEFAULTDURCHLAUFZEIT")
	private BigDecimal nDefaultdurchlaufzeit;

	@Column(name = "T_AENDERNPOSITION")
	private Timestamp tAendernposition;

	@Column(name = "T_AENDERNARBEITSPLAN")
	private Timestamp tAendernarbeitsplan;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "X_KOMMENTAR")
	private String xKommentar;

	@Column(name = "B_MATERIALBUCHUNGBEIABLIEFERUNG")
	private Short bMaterialbuchungbeiablieferung;

	@Column(name = "B_AUSGABEUNTERSTUECKLISTE")
	private Short bAusgabeunterstueckliste;

	@Column(name = "AUFTRAG_I_ID_LEITAUFTRAG")
	private Integer auftragIIdLeitauftrag;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "PARTNER_I_ID")
	private Integer partnerIId;

	@Column(name = "PERSONAL_I_ID_AENDERNPOSITION")
	private Integer personalIIdAendernposition;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "STUECKLISTE_I_ID_EK")
	private Integer stuecklisteIIdEk;

	
	public Integer getStuecklisteIIdEk() {
		return stuecklisteIIdEk;
	}

	public void setStuecklisteIIdEk(Integer stuecklisteIIdEk) {
		this.stuecklisteIIdEk = stuecklisteIIdEk;
	}

	@Column(name = "LAGER_I_ID_ZIELLAGER")
	private Integer lagerIIdZiellager;

	public Integer getLagerIIdZiellager() {
		return lagerIIdZiellager;
	}

	public void setLagerIIdZiellager(Integer lagerIIdZiellager) {
		this.lagerIIdZiellager = lagerIIdZiellager;
	}

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_AENDERNARBEITSPLAN")
	private Integer personalIIdAendernarbeitsplan;

	@Column(name = "FERTIGUNGSGRUPPE_I_ID")
	private Integer fertigungsgruppeIId;

	@Column(name = "STUECKLISTEART_C_NR")
	private String stuecklisteartCNr;

	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;

	@Column(name = "I_ERFASSUNGSFAKTOR")
	private Integer iErfassungsfaktor;

	public Integer getIErfassungsfaktor() {
		return iErfassungsfaktor;
	}

	public void setIErfassungsfaktor(Integer erfassungsfaktor) {
		iErfassungsfaktor = erfassungsfaktor;
	}

	private static final long serialVersionUID = 1L;

	public Stueckliste() {
		super();
	}

	public Stueckliste(Integer id,
			Integer artikelIId,
			String mandantCNr,
			Short fremdfertigung,
			BigDecimal losgroesse,
			Timestamp aendernposition, 
			Integer personalIIdAendernposition2,
			Timestamp aendernarbeitsplan,
			Integer personalIIdAendernarbeitsplan2,
			Integer personalIIdAnlegen2,
			Integer personalIIdAendern2, 
			Integer fertigungsgruppeIId,			
			String stuecklisteartCNr,
			Short materialbuchungbeiablieferung, 
			Short ausgabeunterstueckliste, Short ueberlieferbar,
			Integer erfassungsfaktor,Integer lagerIIdZiellager,Short bDruckeinlagerstandsdetail) {
		setIId(id);
		setArtikelIId(artikelIId);
		setBFremdfertigung(fremdfertigung);
		setNLosgroesse(losgroesse);
		setTAendernarbeitsplan(aendernarbeitsplan);
		setPersonalIIdAendernarbeitsplan(personalIIdAendernarbeitsplan2);
		Timestamp t = new Timestamp(System.currentTimeMillis());
		setTAendern(t);
		setPersonalIIdAendern(personalIIdAendern2);
		setStuecklisteartCNr(stuecklisteartCNr);
		setFertigungsgruppeIId(fertigungsgruppeIId);
		setTAnlegen(t);
		setPersonalIIdAnlegen(personalIIdAnlegen2);
		setPersonalIIdAendernposition(personalIIdAendernposition2);
		setTAendernposition(aendernposition);
		setMandantCNr(mandantCNr);
		setBMaterialbuchungbeiablieferung(materialbuchungbeiablieferung);
		setBUeberlieferbar(ueberlieferbar);
		setBAusgabeunterstueckliste(ausgabeunterstueckliste);
		setIErfassungsfaktor(erfassungsfaktor);
		setLagerIIdZiellager(lagerIIdZiellager);
		setBDruckeinlagerstandsdetail(bDruckeinlagerstandsdetail);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Short getBFremdfertigung() {
		return this.bFremdfertigung;
	}

	public void setBFremdfertigung(Short bFremdfertigung) {
		this.bFremdfertigung = bFremdfertigung;
	}

	public BigDecimal getNLosgroesse() {
		return this.nLosgroesse;
	}

	public void setNLosgroesse(BigDecimal nLosgroesse) {
		this.nLosgroesse = nLosgroesse;
	}

	public BigDecimal getNDefaultdurchlaufzeit() {
		return this.nDefaultdurchlaufzeit;
	}

	public void setNDefaultdurchlaufzeit(BigDecimal nDefaultdurchlaufzeit) {
		this.nDefaultdurchlaufzeit = nDefaultdurchlaufzeit;
	}

	public Timestamp getTAendernposition() {
		return this.tAendernposition;
	}

	public void setTAendernposition(Timestamp tAendernposition) {
		this.tAendernposition = tAendernposition;
	}

	public Timestamp getTAendernarbeitsplan() {
		return this.tAendernarbeitsplan;
	}

	public void setTAendernarbeitsplan(Timestamp tAendernarbeitsplan) {
		this.tAendernarbeitsplan = tAendernarbeitsplan;
	}

	public Timestamp getTAnlegen() {
		return this.tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Timestamp getTAendern() {
		return this.tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public String getXKommentar() {
		return this.xKommentar;
	}

	public void setXKommentar(String xKommentar) {
		this.xKommentar = xKommentar;
	}

	public Short getBMaterialbuchungbeiablieferung() {
		return this.bMaterialbuchungbeiablieferung;
	}

	public void setBMaterialbuchungbeiablieferung(
			Short bMaterialbuchungbeiablieferung) {
		this.bMaterialbuchungbeiablieferung = bMaterialbuchungbeiablieferung;
	}

	public Short getBAusgabeunterstueckliste() {
		return this.bAusgabeunterstueckliste;
	}

	public void setBAusgabeunterstueckliste(Short bAusgabeunterstueckliste) {
		this.bAusgabeunterstueckliste = bAusgabeunterstueckliste;
	}

	public Integer getAuftragIIdLeitauftrag() {
		return this.auftragIIdLeitauftrag;
	}

	public void setAuftragIIdLeitauftrag(Integer auftragIIdLeitauftrag) {
		this.auftragIIdLeitauftrag = auftragIIdLeitauftrag;
	}

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public Integer getPartnerIId() {
		return this.partnerIId;
	}

	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId;
	}

	public Integer getPersonalIIdAendernposition() {
		return this.personalIIdAendernposition;
	}

	public void setPersonalIIdAendernposition(Integer personalIIdAendernposition) {
		this.personalIIdAendernposition = personalIIdAendernposition;
	}

	public Integer getPersonalIIdAnlegen() {
		return this.personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Integer getPersonalIIdAendernarbeitsplan() {
		return this.personalIIdAendernarbeitsplan;
	}

	public void setPersonalIIdAendernarbeitsplan(
			Integer personalIIdAendernarbeitsplan) {
		this.personalIIdAendernarbeitsplan = personalIIdAendernarbeitsplan;
	}

	public Integer getFertigungsgruppeIId() {
		return this.fertigungsgruppeIId;
	}

	public void setFertigungsgruppeIId(Integer fertigungsgruppeIId) {
		this.fertigungsgruppeIId = fertigungsgruppeIId;
	}

	public String getStuecklisteartCNr() {
		return this.stuecklisteartCNr;
	}

	public void setStuecklisteartCNr(String stuecklisteartCNr) {
		this.stuecklisteartCNr = stuecklisteartCNr;
	}

	public Integer getArtikelIId() {
		return this.artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

}
