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
package com.lp.server.stueckliste.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.lp.server.artikel.ejb.Artikel;
import com.lp.server.artikel.ejb.Lager;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.auftrag.ejb.Auftrag;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.partner.ejb.Partner;
import com.lp.server.stueckliste.ejb.Fertigungsgruppe;
import com.lp.server.system.service.HvDtoLogClass;
import com.lp.server.system.service.HvDtoLogIdCBez;
import com.lp.server.system.service.HvDtoLogIdCnr;
import com.lp.server.system.service.HvDtoLogIgnore;
import com.lp.server.util.IIId;

@HvDtoLogClass(name = HvDtoLogClass.STUECKLISTE)
public class StuecklisteDto implements Serializable, IIId {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Integer artikelIId;
	private String xKommentar;
	private String mandantCNr;
	private Short bFremdfertigung;
	private Integer auftragIIdLeitauftrag;
	private Integer fertigungsgruppeIId;
	private BigDecimal nLosgroesse;
	private Timestamp tAendernposition;
	private Timestamp tAendernarbeitsplan;
	private Integer personalIIdAendernposition;
	private Integer personalIIdAendernarbeitsplan;
	private Timestamp tAnlegen;
	private Timestamp tAendern;
	private Integer personalIIdAendern;
	private BigDecimal nDefaultdurchlaufzeit;
	private Integer partnerIId;
	private Integer personalIIdAnlegen;
	private Integer iErfassungsfaktor;

	private Short bUeberlieferbar;

	public Short getBUeberlieferbar() {
		return bUeberlieferbar;
	}

	public void setBUeberlieferbar(Short bUeberlieferbar) {
		this.bUeberlieferbar = bUeberlieferbar;
	}

	private Short bDruckeinlagerstandsdetail;

	public Short getBDruckeinlagerstandsdetail() {
		return bDruckeinlagerstandsdetail;
	}

	public void setBDruckeinlagerstandsdetail(Short bDruckeinlagerstandsdetail) {
		this.bDruckeinlagerstandsdetail = bDruckeinlagerstandsdetail;
	}

	private Integer stuecklisteIIdEk;

	public Integer getStuecklisteIIdEk() {
		return stuecklisteIIdEk;
	}

	public void setStuecklisteIIdEk(Integer stuecklisteIIdEk) {
		this.stuecklisteIIdEk = stuecklisteIIdEk;
	}

	private Integer lagerIIdZiellager;

	@HvDtoLogIdCnr(entityClass = Lager.class)
	public Integer getLagerIIdZiellager() {
		return lagerIIdZiellager;
	}

	public void setLagerIIdZiellager(Integer lagerIIdZiellager) {
		this.lagerIIdZiellager = lagerIIdZiellager;
	}

	private Short bKeineAutomatischeMaterialbuchung;

	public Short getBKeineAutomatischeMaterialbuchung() {
		return bKeineAutomatischeMaterialbuchung;
	}

	public void setBKeineAutomatischeMaterialbuchung(
			Short bKeineAutomatischeMaterialbuchung) {
		this.bKeineAutomatischeMaterialbuchung = bKeineAutomatischeMaterialbuchung;
	}

	public Integer getIErfassungsfaktor() {
		return iErfassungsfaktor;
	}

	public void setIErfassungsfaktor(Integer erfassungsfaktor) {
		iErfassungsfaktor = erfassungsfaktor;
	}

	private Integer personalIIdFreigabe;
	private Timestamp tFreigabe;

	public Integer getPersonalIIdFreigabe() {
		return personalIIdFreigabe;
	}

	public void setPersonalIIdFreigabe(Integer personalIIdFreigabe) {
		this.personalIIdFreigabe = personalIIdFreigabe;
	}

	public Timestamp getTFreigabe() {
		return tFreigabe;
	}

	public void setTFreigabe(Timestamp tFreigabe) {
		this.tFreigabe = tFreigabe;
	}

	public Integer getIId() {
		return iId;
	}

	private AuftragDto auftragDto;

	private ArtikelDto artikelDto;
	private String stuecklisteartCNr;
	private Short bMaterialbuchungbeiablieferung;
	private Short bAusgabeunterstueckliste;

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	@HvDtoLogIdCnr(entityClass = Artikel.class)
	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public Short getBFremdfertigung() {
		return bFremdfertigung;
	}

	public void setBFremdfertigung(Short bFremdfertigung) {
		this.bFremdfertigung = bFremdfertigung;
	}

	@HvDtoLogIdCnr(entityClass = Auftrag.class)
	public Integer getAuftragIIdLeitauftrag() {
		return auftragIIdLeitauftrag;
	}

	public void setAuftragIIdLeitauftrag(Integer auftragIIdLeitauftrag) {
		this.auftragIIdLeitauftrag = auftragIIdLeitauftrag;
	}

	@HvDtoLogIdCBez(entityClass = Fertigungsgruppe.class)
	public Integer getFertigungsgruppeIId() {
		return fertigungsgruppeIId;
	}

	public void setFertigungsgruppeIId(Integer fertigungsgruppeIId) {
		this.fertigungsgruppeIId = fertigungsgruppeIId;
	}

	public BigDecimal getNLosgroesse() {
		return nLosgroesse;
	}

	public void setNLosgroesse(BigDecimal nLosgroesse) {
		this.nLosgroesse = nLosgroesse;
	}

	public Timestamp getTAendernarbeitsplan() {
		return tAendernarbeitsplan;
	}

	public void setTAendernarbeitsplan(Timestamp tAendernarbeitsplan) {
		this.tAendernarbeitsplan = tAendernarbeitsplan;
	}

	public Integer getPersonalIIdAendernarbeitsplan() {
		return personalIIdAendernarbeitsplan;
	}

	public void setPersonalIIdAendernarbeitsplan(
			Integer personalIIdAendernarbeitsplan) {
		this.personalIIdAendernarbeitsplan = personalIIdAendernarbeitsplan;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	@HvDtoLogIgnore
	public ArtikelDto getArtikelDto() {
		return artikelDto;
	}

	@HvDtoLogIgnore
	public AuftragDto getAuftragDto() {
		return auftragDto;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	@HvDtoLogIdCBez(entityClass = Partner.class)
	public Integer getPartnerIId() {
		return partnerIId;
	}

	public String getXKommentar() {
		return xKommentar;
	}

	public Integer getPersonalIIdAendernposition() {
		return personalIIdAendernposition;
	}

	public Timestamp getTAendernposition() {
		return tAendernposition;
	}

	@HvDtoLogIgnore
	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	@HvDtoLogIgnore
	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public BigDecimal getNDefaultdurchlaufzeit() {
		return nDefaultdurchlaufzeit;
	}

	public String getStuecklisteartCNr() {
		return stuecklisteartCNr;
	}

	public Short getBMaterialbuchungbeiablieferung() {
		return bMaterialbuchungbeiablieferung;
	}

	public Short getBAusgabeunterstueckliste() {
		return bAusgabeunterstueckliste;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public void setArtikelDto(ArtikelDto artikelDto) {
		this.artikelDto = artikelDto;
	}

	public void setAuftragDto(AuftragDto auftragDto) {
		this.auftragDto = auftragDto;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId;
	}

	public void setXKommentar(String xKommentar) {
		this.xKommentar = xKommentar;
	}

	public void setPersonalIIdAendernposition(Integer personalIIdAendernposition) {
		this.personalIIdAendernposition = personalIIdAendernposition;
	}

	public void setTAendernposition(Timestamp tAendernposition) {
		this.tAendernposition = tAendernposition;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public void setNDefaultdurchlaufzeit(BigDecimal nDefaultdurchlaufzeit) {
		this.nDefaultdurchlaufzeit = nDefaultdurchlaufzeit;
	}

	public void setStuecklisteartCNr(String stuecklisteartCNr) {
		this.stuecklisteartCNr = stuecklisteartCNr;
	}

	public void setBMaterialbuchungbeiablieferung(
			Short bMaterialbuchungbeiablieferung) {
		this.bMaterialbuchungbeiablieferung = bMaterialbuchungbeiablieferung;
	}

	public void setBAusgabeunterstueckliste(Short bAusgabeunterstueckliste) {
		this.bAusgabeunterstueckliste = bAusgabeunterstueckliste;
	}

	public boolean isMaterialbuchungbeiablieferung() {
		return bMaterialbuchungbeiablieferung == null ? false
				: ((short) 1 == bMaterialbuchungbeiablieferung);
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof StuecklisteDto))
			return false;
		StuecklisteDto that = (StuecklisteDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.artikelIId == null ? this.artikelIId == null
				: that.artikelIId.equals(this.artikelIId)))
			return false;
		if (!(that.bFremdfertigung == null ? this.bFremdfertigung == null
				: that.bFremdfertigung.equals(this.bFremdfertigung)))
			return false;
		if (!(that.auftragIIdLeitauftrag == null ? this.auftragIIdLeitauftrag == null
				: that.auftragIIdLeitauftrag.equals(this.auftragIIdLeitauftrag)))
			return false;
		if (!(that.fertigungsgruppeIId == null ? this.fertigungsgruppeIId == null
				: that.fertigungsgruppeIId.equals(this.fertigungsgruppeIId)))
			return false;
		if (!(that.nLosgroesse == null ? this.nLosgroesse == null
				: that.nLosgroesse.equals(this.nLosgroesse)))
			return false;
		if (!(that.tAendernarbeitsplan == null ? this.tAendernarbeitsplan == null
				: that.tAendernarbeitsplan.equals(this.tAendernarbeitsplan)))
			return false;
		if (!(that.personalIIdAendernarbeitsplan == null ? this.personalIIdAendernarbeitsplan == null
				: that.personalIIdAendernarbeitsplan
						.equals(this.personalIIdAendernarbeitsplan)))
			return false;
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern
				.equals(this.tAendern)))
			return false;
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.artikelIId.hashCode();
		result = 37 * result + this.bFremdfertigung.hashCode();
		result = 37 * result + this.auftragIIdLeitauftrag.hashCode();
		result = 37 * result + this.fertigungsgruppeIId.hashCode();
		result = 37 * result + this.nLosgroesse.hashCode();
		result = 37 * result + this.tAendernarbeitsplan.hashCode();
		result = 37 * result + this.personalIIdAendernarbeitsplan.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + artikelIId;
		returnString += ", " + bFremdfertigung;
		returnString += ", " + auftragIIdLeitauftrag;
		returnString += ", " + fertigungsgruppeIId;
		returnString += ", " + nLosgroesse;
		returnString += ", " + tAendernarbeitsplan;
		returnString += ", " + personalIIdAendernarbeitsplan;
		returnString += ", " + tAendern;
		returnString += ", " + personalIIdAendern;
		return returnString;
	}
}
