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
package com.lp.server.artikel.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.lp.server.partner.service.LieferantDto;
import com.lp.util.Helper;

public class ArtikellieferantDto implements Serializable {
	public Short getBRabattbehalten() {
		return bRabattbehalten;
	}

	public void setBRabattbehalten(Short rabattbehalten) {
		bRabattbehalten = rabattbehalten;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer artikelIId;
	private Integer lieferantIId;
	private String mandantCNr;
	private String cBezbeilieferant;
	private String cArtikelnrlieferant;
	private Short bHerstellerbez;
	private Short bWebshop;

	private BigDecimal nMaterialzuschlag;

	public BigDecimal getNMaterialzuschlag() {
		return nMaterialzuschlag;
	}

	private String cWeblink;

	public String getCWeblink() {
		return cWeblink;
	}

	public void setCWeblink(String cWeblink) {
		this.cWeblink = cWeblink;
	}

	public void setNMaterialzuschlag(BigDecimal materialzuschlag) {
		nMaterialzuschlag = materialzuschlag;
	}

	public Short getBWebshop() {
		return bWebshop;
	}

	public void setBWebshop(Short webshop) {
		bWebshop = webshop;
	}

	private BigDecimal nEinzelpreis;
	private Double fRabatt;
	private BigDecimal nNettopreis;
	private Double fStandardmenge;
	private BigDecimal nStaffelmenge;
	private Double fMindestbestelmenge;
	private BigDecimal nVerpackungseinheit;
	private BigDecimal nFixkosten;
	private String cRabattgruppe;
	private Timestamp tPreisgueltigab;
	private Integer iSort;

	private LieferantDto lieferantDto;
	private Integer iId;
	private Integer artikellieferantstaffelIId;
	private Integer iWiederbeschaffungszeit;
	private Short bRabattbehalten;

	private Integer personalIIdAendern;
	private Timestamp tAendern;

	private Integer zertifikatartIId;

	public Integer getZertifikatartIId() {
		return zertifikatartIId;
	}

	public void setZertifikatartIId(Integer zertifikatartIId) {
		this.zertifikatartIId = zertifikatartIId;
	}

	private Timestamp tPreisgueltigbis;

	public Timestamp getTPreisgueltigbis() {
		return tPreisgueltigbis;
	}

	public void setTPreisgueltigbis(Timestamp preisgueltigbis) {
		tPreisgueltigbis = preisgueltigbis;
	}

	public String getCAngebotnummer() {
		return cAngebotnummer;
	}

	public void setCAngebotnummer(String angebotnummer) {
		cAngebotnummer = angebotnummer;
	}

	private String cAngebotnummer;

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp aendern) {
		tAendern = aendern;
	}

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public Integer getLieferantIId() {
		return lieferantIId;
	}

	public void setLieferantIId(Integer lieferantIId) {
		this.lieferantIId = lieferantIId;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public String getCBezbeilieferant() {
		return cBezbeilieferant;
	}

	public void setCBezbeilieferant(String cBezbeilieferant) {
		this.cBezbeilieferant = cBezbeilieferant;
	}

	public String getCArtikelnrlieferant() {
		return cArtikelnrlieferant;
	}

	public void setCArtikelnrlieferant(String cArtikelnrlieferant) {
		this.cArtikelnrlieferant = cArtikelnrlieferant;
	}

	public Short getBHerstellerbez() {
		return bHerstellerbez;
	}

	public void setBHerstellerbez(Short bHerstellerbez) {
		this.bHerstellerbez = bHerstellerbez;
	}

	public BigDecimal getNEinzelpreis() {
		return nEinzelpreis;
	}

	public void setNEinzelpreis(BigDecimal nEinzelpreis) {
		this.nEinzelpreis = nEinzelpreis;
	}

	public Double getFRabatt() {
		return fRabatt;
	}

	public void setFRabatt(Double fRabatt) {
		this.fRabatt = fRabatt;
	}

	public BigDecimal getNNettopreis() {
		return nNettopreis;
	}

	public BigDecimal getLief1Preis() {
		BigDecimal bdLief1Preis = null;
		if (nNettopreis != null) {
			bdLief1Preis = nNettopreis;
			if (nMaterialzuschlag != null) {
				bdLief1Preis = bdLief1Preis.add(nMaterialzuschlag);
			}
		}
		return bdLief1Preis;
	}

	public void setNNettopreis(BigDecimal nNettopreis) {
		this.nNettopreis = nNettopreis;
	}

	public Double getFStandardmenge() {
		return fStandardmenge;
	}

	public void setFStandardmenge(Double fStandardmenge) {
		this.fStandardmenge = fStandardmenge;
	}

	public Double getFMindestbestelmenge() {
		return fMindestbestelmenge;
	}

	public void setFMindestbestelmenge(Double fMindestbestelmenge) {
		this.fMindestbestelmenge = fMindestbestelmenge;
	}

	public BigDecimal getNVerpackungseinheit() {
		return nVerpackungseinheit;
	}

	public void setNVerpackungseinheit(BigDecimal nVerpackungseinheit) {
		this.nVerpackungseinheit = nVerpackungseinheit;
	}

	public BigDecimal getNFixkosten() {
		return nFixkosten;
	}

	public void setNFixkosten(BigDecimal nFixkosten) {
		this.nFixkosten = nFixkosten;
	}

	public String getCRabattgruppe() {
		return cRabattgruppe;
	}

	public void setCRabattgruppe(String cRabattgruppe) {
		this.cRabattgruppe = cRabattgruppe;
	}

	public Timestamp getTPreisgueltigab() {
		return tPreisgueltigab;
	}

	public void setTPreisgueltigab(Timestamp tPreisgueltigab) {
		this.tPreisgueltigab = Helper.cutTimestamp(tPreisgueltigab);
	}

	public Integer getISort() {
		return iSort;
	}

	public LieferantDto getLieferantDto() {
		return lieferantDto;
	}

	public Integer getIId() {
		return iId;
	}

	public Integer getArtikellieferantstaffelIId() {
		return artikellieferantstaffelIId;
	}

	public Integer getIWiederbeschaffungszeit() {
		return iWiederbeschaffungszeit;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public void setLieferantDto(LieferantDto lieferantDto) {
		this.lieferantDto = lieferantDto;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public void setArtikellieferantstaffelIId(Integer artikellieferantstaffelIId) {
		this.artikellieferantstaffelIId = artikellieferantstaffelIId;
	}

	public void setIWiederbeschaffungszeit(Integer iWiederbeschaffungszeit) {
		this.iWiederbeschaffungszeit = iWiederbeschaffungszeit;
	}

	public void setNStaffelmenge(BigDecimal nStaffelmenge) {
		this.nStaffelmenge = nStaffelmenge;
	}

	public BigDecimal getNStaffelmenge() {
		return nStaffelmenge;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ArtikellieferantDto)) {
			return false;
		}
		ArtikellieferantDto that = (ArtikellieferantDto) obj;
		if (!(that.artikelIId == null ? this.artikelIId == null
				: that.artikelIId.equals(this.artikelIId))) {
			return false;
		}
		if (!(that.lieferantIId == null ? this.lieferantIId == null
				: that.lieferantIId.equals(this.lieferantIId))) {
			return false;
		}
		if (!(that.mandantCNr == null ? this.mandantCNr == null
				: that.mandantCNr.equals(this.mandantCNr))) {
			return false;
		}
		if (!(that.cBezbeilieferant == null ? this.cBezbeilieferant == null
				: that.cBezbeilieferant.equals(this.cBezbeilieferant))) {
			return false;
		}
		if (!(that.cArtikelnrlieferant == null ? this.cArtikelnrlieferant == null
				: that.cArtikelnrlieferant.equals(this.cArtikelnrlieferant))) {
			return false;
		}
		if (!(that.bHerstellerbez == null ? this.bHerstellerbez == null
				: that.bHerstellerbez.equals(this.bHerstellerbez))) {
			return false;
		}
		if (!(that.nEinzelpreis == null ? this.nEinzelpreis == null
				: that.nEinzelpreis.equals(this.nEinzelpreis))) {
			return false;
		}
		if (!(that.fRabatt == null ? this.fRabatt == null : that.fRabatt
				.equals(this.fRabatt))) {
			return false;
		}
		if (!(that.nNettopreis == null ? this.nNettopreis == null
				: that.nNettopreis.equals(this.nNettopreis))) {
			return false;
		}
		if (!(that.fStandardmenge == null ? this.fStandardmenge == null
				: that.fStandardmenge.equals(this.fStandardmenge))) {
			return false;
		}
		if (!(that.fMindestbestelmenge == null ? this.fMindestbestelmenge == null
				: that.fMindestbestelmenge.equals(this.fMindestbestelmenge))) {
			return false;
		}
		if (!(that.nVerpackungseinheit == null ? this.nVerpackungseinheit == null
				: that.nVerpackungseinheit.equals(this.nVerpackungseinheit))) {
			return false;
		}
		if (!(that.nFixkosten == null ? this.nFixkosten == null
				: that.nFixkosten.equals(this.nFixkosten))) {
			return false;
		}
		if (!(that.cRabattgruppe == null ? this.cRabattgruppe == null
				: that.cRabattgruppe.equals(this.cRabattgruppe))) {
			return false;
		}
		if (!(that.tPreisgueltigab == null ? this.tPreisgueltigab == null
				: that.tPreisgueltigab.equals(this.tPreisgueltigab))) {
			return false;
		}
		if (!(that.iSort == null ? this.iSort == null : that.iSort
				.equals(this.iSort))) {
			return false;
		}
		if (!(that.nStaffelmenge == null ? this.nStaffelmenge == null
				: that.nStaffelmenge.equals(this.nStaffelmenge))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.artikelIId.hashCode();
		result = 37 * result + this.lieferantIId.hashCode();
		result = 37 * result + this.mandantCNr.hashCode();
		result = 37 * result + this.cBezbeilieferant.hashCode();
		result = 37 * result + this.cArtikelnrlieferant.hashCode();
		result = 37 * result + this.bHerstellerbez.hashCode();
		result = 37 * result + this.nEinzelpreis.hashCode();
		result = 37 * result + this.fRabatt.hashCode();
		result = 37 * result + this.nNettopreis.hashCode();
		result = 37 * result + this.fStandardmenge.hashCode();
		result = 37 * result + this.fMindestbestelmenge.hashCode();
		result = 37 * result + this.nVerpackungseinheit.hashCode();
		result = 37 * result + this.nFixkosten.hashCode();
		result = 37 * result + this.cRabattgruppe.hashCode();
		result = 37 * result + this.tPreisgueltigab.hashCode();
		result = 37 * result + this.iSort.hashCode();
		result = 37 * result + this.nStaffelmenge.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += artikelIId;
		returnString += ", " + lieferantIId;
		returnString += ", " + mandantCNr;
		returnString += ", " + cBezbeilieferant;
		returnString += ", " + cArtikelnrlieferant;
		returnString += ", " + bHerstellerbez;
		returnString += ", " + nEinzelpreis;
		returnString += ", " + fRabatt;
		returnString += ", " + nNettopreis;
		returnString += ", " + fStandardmenge;
		returnString += ", " + fMindestbestelmenge;
		returnString += ", " + nVerpackungseinheit;
		returnString += ", " + nFixkosten;
		returnString += ", " + cRabattgruppe;
		returnString += ", " + tPreisgueltigab;
		returnString += ", " + iSort;
		returnString += ", " + nStaffelmenge;
		return returnString;
	}

}
