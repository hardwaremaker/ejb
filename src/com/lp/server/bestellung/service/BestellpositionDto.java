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
package com.lp.server.bestellung.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import com.lp.service.BelegpositionDto;

public class BestellpositionDto extends BelegpositionDto implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String bestellpositionstatusCNr;
	private BigDecimal nOffeneMenge;
	private Double dRabattsatz;
	private Short bRabattsatzUebersteuert;
	private Integer mwstsatzIId;
	private Short bMwstsatzUebersteuert;

	private BigDecimal nNettoeinzelpreis;
	private BigDecimal nRabattbetrag;
	private BigDecimal nNettogesamtpreis;
	private BigDecimal nNettogesamtPreisminusRabatte;

	
	private Integer lieferantIIdWennCopyInBestellvorschlag;
	
	public Integer getLieferantIIdWennCopyInBestellvorschlag() {
		return lieferantIIdWennCopyInBestellvorschlag;
	}

	public void setLieferantIIdWennCopyInBestellvorschlag(
			Integer lieferantIIdWennCopyInBestellvorschlag) {
		this.lieferantIIdWennCopyInBestellvorschlag = lieferantIIdWennCopyInBestellvorschlag;
	}

	private Timestamp tUebersteuerterLiefertermin;
	private Short bDrucken;
	private Integer iBestellpositionIIdRahmenposition;
	private String cABKommentar;
	private String cABNummer;
	private Date tAuftragsbestaetigungstermin;

	private BigDecimal nFixkosten;
	private BigDecimal nFixkostengeliefert;

	private Timestamp tManuellvollstaendiggeliefert;

	private Timestamp tAbterminAendern;

	private Integer personalIIdLieferterminbestaetigt;

	private Timestamp tLieferterminbestaetigt;
	private Integer lossollmaterialIId;

	
	private Double fMindestbestellmenge_NOT_IN_DB;
	public Double getfMindestbestellmenge_NOT_IN_DB() {
		return fMindestbestellmenge_NOT_IN_DB;
	}

	public void setfMindestbestellmenge_NOT_IN_DB(
			Double fMindestbestellmenge_NOT_IN_DB) {
		this.fMindestbestellmenge_NOT_IN_DB = fMindestbestellmenge_NOT_IN_DB;
	}

	public BigDecimal getnVerpackungseinheit_NOT_IN_DB() {
		return nVerpackungseinheit_NOT_IN_DB;
	}

	public void setnVerpackungseinheit_NOT_IN_DB(
			BigDecimal nVerpackungseinheit_NOT_IN_DB) {
		this.nVerpackungseinheit_NOT_IN_DB = nVerpackungseinheit_NOT_IN_DB;
	}

	private BigDecimal nVerpackungseinheit_NOT_IN_DB;
	
	public Integer getLossollmaterialIId() {
		return lossollmaterialIId;
	}

	public void setLossollmaterialIId(Integer lossollmaterialIId) {
		this.lossollmaterialIId = lossollmaterialIId;
	}

	public Integer getPersonalIIdLieferterminbestaetigt() {
		return personalIIdLieferterminbestaetigt;
	}

	public void setPersonalIIdLieferterminbestaetigt(
			Integer personalIIdLieferterminbestaetigt) {
		this.personalIIdLieferterminbestaetigt = personalIIdLieferterminbestaetigt;
	}

	public Timestamp getTLieferterminbestaetigt() {
		return tLieferterminbestaetigt;
	}

	public void setTLieferterminbestaetigt(Timestamp lieferterminbestaetigt) {
		tLieferterminbestaetigt = lieferterminbestaetigt;
	}

	private Integer kundeIId;

	public Integer getKundeIId() {
		return kundeIId;
	}

	public void setKundeIId(Integer kundeIId) {
		this.kundeIId = kundeIId;
	}

	private Integer personalIIdAbterminAendern;

	private Timestamp tAbursprungstermin;

	private String cAngebotnummer;

	public String getCAngebotnummer() {
		return cAngebotnummer;
	}

	public void setCAngebotnummer(String angebotnummer) {
		cAngebotnummer = angebotnummer;
	}

	public String getCABNummer() {
		return cABNummer;
	}

	public void setCABNummer(String cABNummer) {
		this.cABNummer = cABNummer;
	}

	public void setCABKommentar(String cABKommentar) {
		this.cABKommentar = cABKommentar;
	}

	public String getCABKommentar() {
		return cABKommentar;
	}

	public void setTAuftragsbestaetigungstermin(
			Date tAuftragsbestaetigungstermin) {
		this.tAuftragsbestaetigungstermin = tAuftragsbestaetigungstermin;
	}

	public Date getTAuftragsbestaetigungstermin() {
		return tAuftragsbestaetigungstermin;
	}

	public Integer getBestellungIId() {
		return super.getBelegIId();
	}

	public void setIBestellpositionIIdRahmenposition(
			Integer iBestellpositionIIdRahmenposition) {
		this.iBestellpositionIIdRahmenposition = iBestellpositionIIdRahmenposition;
	}

	public Integer getIBestellpositionIIdRahmenposition() {
		return iBestellpositionIIdRahmenposition;
	}

	public void setBestellungIId(Integer bestellungIId) {
		super.setBelegIId(bestellungIId);
	}

	/**
	 * @deprecated bitte {@link #getPositionsartCNr()} benutzen
	 */	
	public String getBestellpositionartCNr() {
		return super.getPositionsartCNr();
	}

	
	/**
	 * @deprecated bitte {@link #setPositionsartCNr(String)} benutzen
	 * @param bestellpositionartCNr
	 */
	public void setBestellpositionartCNr(String bestellpositionartCNr) {
		super.setPositionsartCNr(bestellpositionartCNr);
	}

	public String getBestellpositionstatusCNr() {
		return bestellpositionstatusCNr;
	}

	public void setBestellpositionstatusCNr(String bestellpositionstatusCNr) {
		this.bestellpositionstatusCNr = bestellpositionstatusCNr;
	}

	public BigDecimal getNOffeneMenge() {
		return nOffeneMenge;
	}

	public void setNOffeneMenge(BigDecimal nOffeneMenge) {
		this.nOffeneMenge = nOffeneMenge;
	}

	public Double getDRabattsatz() {
		return dRabattsatz;
	}

	public void setDRabattsatz(Double dRabattsatz) {
		this.dRabattsatz = dRabattsatz;
	}

	public Short getBRabattsatzUebersteuert() {
		return bRabattsatzUebersteuert;
	}

	public void setBRabattsatzUebersteuert(Short bRabattsatzUebersteuert) {
		this.bRabattsatzUebersteuert = bRabattsatzUebersteuert;
	}

	public Integer getMwstsatzIId() {
		return mwstsatzIId;
	}

	public void setMwstsatzIId(Integer mwstsatzIId) {
		this.mwstsatzIId = mwstsatzIId;
	}

	public Short getBMwstsatzUebersteuert() {
		return bMwstsatzUebersteuert;
	}

	public void setBMwstsatzUebersteuert(Short bMwstsatzUebersteuert) {
		this.bMwstsatzUebersteuert = bMwstsatzUebersteuert;
	}

	public BigDecimal getNNettoeinzelpreis() {
		return nNettoeinzelpreis;
	}

	public void setNNettoeinzelpreis(BigDecimal nNettoeinzelpreis) {
		this.nNettoeinzelpreis = nNettoeinzelpreis;
	}

	public BigDecimal getNRabattbetrag() {
		return nRabattbetrag;
	}

	public void setNRabattbetrag(BigDecimal nRabattbetrag) {
		this.nRabattbetrag = nRabattbetrag;
	}

	public BigDecimal getNNettogesamtpreis() {
		return nNettogesamtpreis;
	}

	public void setNNettogesamtpreis(BigDecimal nNettogesamtpreis) {
		this.nNettogesamtpreis = nNettogesamtpreis;
	}

	public Timestamp getTUebersteuerterLiefertermin() {
		return tUebersteuerterLiefertermin;
	}

	public void setTUebersteuerterLiefertermin(
			Timestamp tUebersteuerterLiefertermin) {
		this.tUebersteuerterLiefertermin = tUebersteuerterLiefertermin;
	}

	public Timestamp getTManuellvollstaendiggeliefert() {
		return tManuellvollstaendiggeliefert;
	}

	public void setTManuellvollstaendiggeliefert(
			Timestamp tManuellvollstaendiggeliefert) {
		this.tManuellvollstaendiggeliefert = tManuellvollstaendiggeliefert;
	}

	public Short getBDrucken() {
		return bDrucken;
	}

	public void setBDrucken(Short bDrucken) {
		this.bDrucken = bDrucken;
	}

	public BigDecimal getNNettogesamtPreisminusRabatte() {
		return nNettogesamtPreisminusRabatte;
	}

	public void setNNettogesamtPreisminusRabatte(
			BigDecimal nNettogesamtPreisminusRabatte) {
		this.nNettogesamtPreisminusRabatte = nNettogesamtPreisminusRabatte;
	}

	public BigDecimal getNFixkosten() {
		return nFixkosten;
	}

	public void setNFixkosten(BigDecimal nFixkosten) {
		this.nFixkosten = nFixkosten;
	}

	public BigDecimal getNFixkostengeliefert() {
		return nFixkostengeliefert;
	}

	public void setNFixkostengeliefert(BigDecimal nFixkostengeliefert) {
		this.nFixkostengeliefert = nFixkostengeliefert;
	}

	public Timestamp getTAbterminAendern() {
		return this.tAbterminAendern;
	}

	public void setTAbterminAendern(Timestamp tAbterminAendern) {
		this.tAbterminAendern = tAbterminAendern;
	}

	public Integer getPersonalIIdAbterminAendern() {
		return this.personalIIdAbterminAendern;
	}

	public void setPersonalIIdAbterminAendern(Integer personalIIdAbterminAendern) {
		this.personalIIdAbterminAendern = personalIIdAbterminAendern;
	}

	public Timestamp getTAbursprungstermin() {
		return tAbursprungstermin;
	}

	public void setTAbursprungstermin(Timestamp abursprungstermin) {
		tAbursprungstermin = abursprungstermin;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof BestellpositionDto)) {
			return false;
		}
		BestellpositionDto that = (BestellpositionDto) obj;
		if (!(that.getIId() == null ? this.getIId() == null : that.getIId()
				.equals(this.getIId()))) {
			return false;
		}
		if (!(that.getBelegIId() == null ? this.getBelegIId() == null : that
				.getBelegIId().equals(this.getBelegIId()))) {
			return false;
		}
		if (!(that.getISort() == null ? this.getISort() == null : that
				.getISort().equals(this.getISort()))) {
			return false;
		}
		if (!(that.getPositionsartCNr() == null ? this.getPositionsartCNr() == null
				: that.getPositionsartCNr().equals(this.getPositionsartCNr()))) {
			return false;
		}
		if (!(that.bestellpositionstatusCNr == null ? this.bestellpositionstatusCNr == null
				: that.bestellpositionstatusCNr
						.equals(this.bestellpositionstatusCNr))) {
			return false;
		}
		if (!(that.getArtikelIId() == null ? this.getArtikelIId() == null
				: that.getArtikelIId().equals(this.getArtikelIId()))) {
			return false;
		}
		if (!(that.getCBez() == null ? this.getCBez() == null : that.getCBez()
				.equals(this.getCBez()))) {
			return false;
		}
		if (!(that.getCZusatzbez() == null ? this.getCZusatzbez() == null
				: that.getCZusatzbez().equals(this.getCZusatzbez()))) {
			return false;
		}
		if (!(that.getBArtikelbezeichnunguebersteuert() == null ? this
				.getBArtikelbezeichnunguebersteuert() == null : that
				.getBArtikelbezeichnunguebersteuert().equals(
						this.getBArtikelbezeichnunguebersteuert()))) {
			return false;
		}
		if (!(that.getXTextinhalt() == null ? this.getXTextinhalt() == null
				: that.getXTextinhalt().equals(this.getXTextinhalt()))) {
			return false;
		}
		if (!(that.nOffeneMenge == null ? this.nOffeneMenge == null
				: that.nOffeneMenge.equals(this.nOffeneMenge))) {
			return false;
		}
		if (!(that.getNMenge() == null ? this.getNMenge() == null : that
				.getNMenge().equals(this.getNMenge()))) {
			return false;
		}
		if (!(that.getEinheitCNr() == null ? this.getEinheitCNr() == null
				: that.getEinheitCNr().equals(this.getEinheitCNr()))) {
			return false;
		}
		if (!(that.dRabattsatz == null ? this.dRabattsatz == null
				: that.dRabattsatz.equals(this.dRabattsatz))) {
			return false;
		}
		if (!(that.bRabattsatzUebersteuert == null ? this.bRabattsatzUebersteuert == null
				: that.bRabattsatzUebersteuert
						.equals(this.bRabattsatzUebersteuert))) {
			return false;
		}
		if (!(that.mwstsatzIId == null ? this.mwstsatzIId == null
				: that.mwstsatzIId.equals(this.mwstsatzIId))) {
			return false;
		}
		if (!(that.bMwstsatzUebersteuert == null ? this.bMwstsatzUebersteuert == null
				: that.bMwstsatzUebersteuert.equals(this.bMwstsatzUebersteuert))) {
			return false;
		}
		if (!(that.nNettoeinzelpreis == null ? this.nNettoeinzelpreis == null
				: that.nNettoeinzelpreis.equals(this.nNettoeinzelpreis))) {
			return false;
		}
		if (!(that.nRabattbetrag == null ? this.nRabattbetrag == null
				: that.nRabattbetrag.equals(this.nRabattbetrag))) {
			return false;
		}
		if (!(that.nNettogesamtpreis == null ? this.nNettogesamtpreis == null
				: that.nNettogesamtpreis.equals(this.nNettogesamtpreis))) {
			return false;
		}
		if (!(that.tUebersteuerterLiefertermin == null ? this.tUebersteuerterLiefertermin == null
				: that.tUebersteuerterLiefertermin
						.equals(this.tUebersteuerterLiefertermin))) {
			return false;
		}
		if (!(that.bDrucken == null ? this.bDrucken == null : that.bDrucken
				.equals(this.bDrucken))) {
			return false;
		}
		if (!(that.iBestellpositionIIdRahmenposition == null ? this.iBestellpositionIIdRahmenposition == null
				: that.iBestellpositionIIdRahmenposition
						.equals(this.iBestellpositionIIdRahmenposition))) {
			return false;
		}
		if (!(that.tAuftragsbestaetigungstermin == null ? this.tAuftragsbestaetigungstermin == null
				: that.tAuftragsbestaetigungstermin
						.equals(this.tAuftragsbestaetigungstermin))) {
			return false;
		}
		if (!(that.cABKommentar == null ? this.cABKommentar == null
				: that.cABKommentar.equals(this.cABKommentar))) {
			return false;
		}
		if (!(that.cABNummer == null ? this.cABNummer == null : that.cABNummer
				.equals(this.cABNummer))) {
			return false;
		}

		if (!(that.tAbterminAendern == null ? this.tAbterminAendern == null
				: that.tAbterminAendern.equals(this.tAbterminAendern))) {
			return false;
		}

		if (!(that.personalIIdAbterminAendern == null ? this.personalIIdAbterminAendern == null
				: that.personalIIdAbterminAendern
						.equals(this.personalIIdAbterminAendern))) {
			return false;
		}

		if (!(that.tAbursprungstermin == null ? this.tAbursprungstermin == null
				: that.tAbursprungstermin.equals(this.tAbursprungstermin))) {
			return false;
		}

		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.getIId().hashCode();
		result = 37 * result + this.getBelegIId().hashCode();
		result = 37 * result + this.getISort().hashCode();
		result = 37 * result + this.getPositionsartCNr().hashCode();
		result = 37 * result + this.bestellpositionstatusCNr.hashCode();
		result = 37 * result + this.getArtikelIId().hashCode();
		result = 37 * result + this.getCBez().hashCode();
		result = 37 * result + this.getCZusatzbez().hashCode();
		result = 37 * result
				+ this.getBArtikelbezeichnunguebersteuert().hashCode();
		result = 37 * result + this.getXTextinhalt().hashCode();
		result = 37 * result + this.nOffeneMenge.hashCode();
		result = 37 * result + this.getNMenge().hashCode();
		result = 37 * result + this.getEinheitCNr().hashCode();
		result = 37 * result + this.dRabattsatz.hashCode();
		result = 37 * result + this.bRabattsatzUebersteuert.hashCode();
		result = 37 * result + this.mwstsatzIId.hashCode();
		result = 37 * result + this.bMwstsatzUebersteuert.hashCode();
		result = 37 * result + this.nNettoeinzelpreis.hashCode();
		result = 37 * result + this.nRabattbetrag.hashCode();
		result = 37 * result + this.nNettogesamtpreis.hashCode();
		result = 37 * result + this.tUebersteuerterLiefertermin.hashCode();
		result = 37 * result + this.bDrucken.hashCode();
		result = 37 * result
				+ this.iBestellpositionIIdRahmenposition.hashCode();
		result = 37 * result + this.tAuftragsbestaetigungstermin.hashCode();
		result = 37 * result + this.cABKommentar.hashCode();
		result = 37 * result + this.cABNummer.hashCode();
		result = 37 * result + this.tAbterminAendern.hashCode();
		result = 37 * result + this.personalIIdAbterminAendern.hashCode();
		result = 37 * result + this.tAbursprungstermin.hashCode();
		return result;
	}

	public String toString() {

		String returnString = super.toString();

		returnString += ", bestellpositionstatusCNr: "
				+ bestellpositionstatusCNr;
		returnString += ", nOffeneMenge: " + nOffeneMenge;
		returnString += ", dRabattsatz: " + dRabattsatz;
		returnString += ", bRabattsatzUebersteuert: " + bRabattsatzUebersteuert;
		returnString += ", mwstsatzIId: " + mwstsatzIId;
		returnString += ", bMwstsatzUebersteuert: " + bMwstsatzUebersteuert;
		returnString += ", nNettoeinzelpreis: " + nNettoeinzelpreis;
		returnString += ", nRabattbetrag: " + nRabattbetrag;
		returnString += ", nNettogesamtpreis: " + nNettogesamtpreis;
		returnString += ", tUebersteuerterLiefertermin: "
				+ tUebersteuerterLiefertermin;
		returnString += ", bDrucken: " + bDrucken;
		returnString += ", iBestellpositionIIdRahmenposition: "
				+ iBestellpositionIIdRahmenposition;
		returnString += ", tAuftragsbestaetigungstermin: "
				+ tAuftragsbestaetigungstermin;
		returnString += ", cABKommentar: " + cABKommentar;
		returnString += ", cABNummer: " + cABNummer;
		returnString += ", tAbTerminAendern: " + tAbterminAendern;
		returnString += ", personalIIdAbTerminAendern: "
				+ personalIIdAbterminAendern;
		returnString += ", tAbursprungstermin: " + tAbursprungstermin;
		return returnString;
	}

	/**
	 * @deprecated MB: use BelegkonvertierungFacBean
	 * @return Object
	 */
	public Object clone() {
		BestellpositionDto bestellpositionDto = new BestellpositionDto();

		// iId, Bezug auf Bestellung, Rahmenposition null
		bestellpositionDto.setISort(this.getISort());
		bestellpositionDto.setPositionsartCNr(this.getPositionsartCNr());
		bestellpositionDto.setBestellpositionstatusCNr(this
				.getBestellpositionstatusCNr());

		// bestellpositionstatus wird nicht verwendet
		bestellpositionDto.setArtikelIId(this.getArtikelIId());
		bestellpositionDto.setCBez(this.getCBez());
		bestellpositionDto.setBArtikelbezeichnunguebersteuert(this
				.getBArtikelbezeichnunguebersteuert());
		bestellpositionDto.setXTextinhalt(this.getXTextinhalt());
		bestellpositionDto.setMediastandardIId(this.getMediastandardIId());
		bestellpositionDto.setCZusatzbez(this.getCZusatzbez());

		bestellpositionDto.setEinheitCNr(this.getEinheitCNr());
		bestellpositionDto.dRabattsatz = this.dRabattsatz;
		bestellpositionDto.bRabattsatzUebersteuert = this.bRabattsatzUebersteuert;
		bestellpositionDto.mwstsatzIId = this.mwstsatzIId;
		bestellpositionDto.bMwstsatzUebersteuert = this.bMwstsatzUebersteuert;
		bestellpositionDto.nNettoeinzelpreis = this.nNettoeinzelpreis;
		bestellpositionDto.setNMaterialzuschlag(this.getNMaterialzuschlag());

		// Preise, die von Konditionen abhaengen, null
		bestellpositionDto.nRabattbetrag = this.nRabattbetrag;
		bestellpositionDto.nNettogesamtpreis = this.nNettogesamtpreis;
		bestellpositionDto.tUebersteuerterLiefertermin = this.tUebersteuerterLiefertermin;
		bestellpositionDto.bDrucken = this.bDrucken;
		bestellpositionDto.bNettopreisuebersteuert = this.bNettopreisuebersteuert;

		// kein Bezug auf Rahmenposition
		bestellpositionDto.tAuftragsbestaetigungstermin = this.tAuftragsbestaetigungstermin;
		bestellpositionDto.cABKommentar = this.cABKommentar;
		bestellpositionDto.cABNummer = this.cABNummer;

		bestellpositionDto.nFixkosten = this.nFixkosten;
		bestellpositionDto.nFixkostengeliefert = this.nFixkostengeliefert;
		bestellpositionDto.tAbterminAendern = this.tAbterminAendern;
		bestellpositionDto.personalIIdAbterminAendern = this.personalIIdAbterminAendern;
		bestellpositionDto.tAbursprungstermin = this.tAbursprungstermin;

		return bestellpositionDto;
	}
}
