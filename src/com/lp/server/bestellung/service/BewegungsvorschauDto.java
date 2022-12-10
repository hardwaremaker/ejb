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
import java.sql.Timestamp;

import com.lp.server.auftrag.service.TabelleDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;

public class BewegungsvorschauDto extends TabelleDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean bTemporaererEintrag = false;
	

	private String ausloeser;
	
	public String getAusloeser() {
		return ausloeser;
	}

	public void setAusloeser(String ausloeser) {
		this.ausloeser = ausloeser;
	}

	private Integer auftragIIdKopfauftrag;
	
	public Integer getAuftragIIdKopfauftrag() {
		return auftragIIdKopfauftrag;
	}

	public void setAuftragIIdKopfauftrag(Integer auftragIIdKopfauftrag) {
		this.auftragIIdKopfauftrag = auftragIIdKopfauftrag;
	}

	private Timestamp tLiefertermin;
	private BigDecimal nMenge;
	private String cBelegartCNr;
	private Integer iBelegIId;
	private String cProjekt;
	private Integer iBelegPositionIId;
	private Integer artikelIId;
	private BigDecimal bdPreis;
	private Timestamp tAbliefertermin;
	
	private Integer forecastpositionIId;
	public Integer getForecastpositionIId() {
		return forecastpositionIId;
	}

	public void setForecastpositionIId(Integer forecastpositionIId) {
		this.forecastpositionIId = forecastpositionIId;
	}

	public String getForecastBemerkung() {
		return forecastBemerkung;
	}

	public void setForecastBemerkung(String forecastBemerkung) {
		this.forecastBemerkung = forecastBemerkung;
	}

	private String forecastBemerkung;
	
	private String forecastartCNr;
	public String getForecastartCNr() {
		return forecastartCNr;
	}
	
	private BigDecimal bdFiktiverLagerstand;	

	public BigDecimal getBdFiktiverLagerstand() {
		return bdFiktiverLagerstand;
	}

	public void setBdFiktiverLagerstand(BigDecimal bdFiktiverLagerstand) {
		this.bdFiktiverLagerstand = bdFiktiverLagerstand;
	}

	public void setForecastartCNr(String forecastartCNr) {
		this.forecastartCNr = forecastartCNr;
	}
	private boolean bKommtAusInternerBstellung=false;
	
	private boolean bKommtAusBestellvorschlag=false;
	
	public boolean isBKommtAusBestellvorschlag() {
		return bKommtAusBestellvorschlag;
	}

	public void setBKommtAusBestellvorschlag(boolean bKommtAusBestellvorschlag) {
		this.bKommtAusBestellvorschlag = bKommtAusBestellvorschlag;
	}

	public boolean isbKommtAusInternerBstellung() {
		return bKommtAusInternerBstellung;
	}

	public void setbKommtAusInternerBstellung(boolean bKommtAusInternerBstellung) {
		this.bKommtAusInternerBstellung = bKommtAusInternerBstellung;
	}

	public Timestamp getTAbliefertermin() {
		return tAbliefertermin;
	}

	public void setTAbliefertermin(Timestamp tAbliefertermin) {
		this.tAbliefertermin = tAbliefertermin;
	}
	private Timestamp tAuftragsfreigabe;
	private Integer personalIIdAuftragsfreigabe;

	public Integer getPersonalIIdAuftragsfreigabe() {
		return personalIIdAuftragsfreigabe;
	}

	public void setPersonalIIdAuftragsfreigabe(Integer personalIIdAuftragsfreigabe) {
		this.personalIIdAuftragsfreigabe = personalIIdAuftragsfreigabe;
	}

	public Timestamp getTAuftragsfreigabe() {
		return tAuftragsfreigabe;
	}

	public void setTAuftragsfreigabe(Timestamp tAuftragsfreigabe) {
		this.tAuftragsfreigabe = tAuftragsfreigabe;
	}

	public BigDecimal getBdPreis() {
		return bdPreis;
	}

	public void setBdPreis(BigDecimal bdPreis) {
		this.bdPreis = bdPreis;
	}
	private Integer partnerIIdStandort;
	
	public Integer getPartnerIIdStandort() {
		return partnerIIdStandort;
	}

	public void setPartnerIIdStandort(Integer partnerIIdStandort) {
		this.partnerIIdStandort = partnerIIdStandort;
	}
	private String mandantCNr;
	
	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}
	private String xTextinhalt = null;
	public final String getXTextinhalt() {
		return this.xTextinhalt;
	}

	public final void setXTextinhalt(String xTextinhalt) {
		this.xTextinhalt = xTextinhalt;
	}
	private BigDecimal nEinkaufpreis;
	public BigDecimal getNEinkaufpreis() {
		return nEinkaufpreis;
	}

	public void setNEinkaufpreis(BigDecimal bdEinkaufpreis) {
		this.nEinkaufpreis = bdEinkaufpreis;
	}
	
	private Integer lieferantIId;
	public Integer getLieferantIId() {
		return lieferantIId;
	}

	public void setLieferantIId(Integer lieferantIId) {
		this.lieferantIId = lieferantIId;
	}
	
	private Integer projektIId;
	
	public Integer getProjektIId() {
		return projektIId;
	}

	public void setProjektIId(Integer projektIId) {
		this.projektIId = projektIId;
	}

	private java.sql.Date tABTerminBestellung;

	public java.sql.Date getTABTerminBestellung() {
		return tABTerminBestellung;
	}

	public void setTABTerminBestellung(java.sql.Date tABTerminBestellung) {
		this.tABTerminBestellung = tABTerminBestellung;
	}

	private String cABNummerBestellung;
	
	public String getCABNummerBestellung() {
		return cABNummerBestellung;
	}

	public void setCABNummerBestellung(String cABNummerBestellung) {
		this.cABNummerBestellung = cABNummerBestellung;
	}
	private KundeDto kundeDto = null;
	public KundeDto getKundeDto() {
		return kundeDto;
	}

	public void setKundeDto(KundeDto kundeDto) {
		this.kundeDto = kundeDto;
	}

	private PartnerDto partnerDto = null;
	private String cBelegnummer = null;

	public Integer getIBelegIId() {
		return iBelegIId;
	}

	public Integer getIBelegPositionIId() {
		return iBelegPositionIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setIBelegIId(Integer iBelegartId) {
		this.iBelegIId = iBelegartId;
	}

	public void setIBelegPositionIId(Integer iBelegPositionIId) {
		this.iBelegPositionIId = iBelegPositionIId;
	}

	public String getCBelegartCNr() {
		return cBelegartCNr;
	}

	public void setCBelegartCNr(String cBelegartCNr) {
		this.cBelegartCNr = cBelegartCNr;
	
	}

	public Timestamp getTLiefertermin() {
		return tLiefertermin;
	}

	public void setTLiefertermin(Timestamp tLiefertermin) {
		this.tLiefertermin = tLiefertermin;
	}

	public BigDecimal getNMenge() {
		return nMenge;
	}

	public void setNMenge(BigDecimal nMenge) {
		this.nMenge = nMenge;
	}

	public void setPartnerDto(PartnerDto partnerDto) {
		this.partnerDto = partnerDto;
	}

	public PartnerDto getPartnerDto() {
		return partnerDto;
	}

	public void setCBelegnummer(String cBelegnummer) {
		this.cBelegnummer = cBelegnummer;
	}

	public String getCBelegnummer() {
		return cBelegnummer;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof BewegungsvorschauDto)) {
			return false;
		}
		BewegungsvorschauDto that = (BewegungsvorschauDto) obj;
		if (!(that.tLiefertermin == null ? this.tLiefertermin == null
				: that.tLiefertermin.equals(this.tLiefertermin))) {
			return false;
		}
		if (!(that.nMenge == null ? this.nMenge == null : that.nMenge
				.equals(this.nMenge))) {
			return false;
		}
		if (!(that.cBelegartCNr == null ? this.cBelegartCNr == null
				: that.cBelegartCNr.equals(this.cBelegartCNr))) {
			return false;
		}
		if (!(that.iBelegIId == null ? this.iBelegIId == null : that.iBelegIId
				.equals(this.iBelegIId))) {
			return false;
		}
		if (!(that.artikelIId == null ? this.artikelIId == null
				: that.artikelIId.equals(this.artikelIId))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.tLiefertermin.hashCode();
		result = 37 * result + this.nMenge.hashCode();
		result = 37 * result + this.cBelegartCNr.hashCode();
		result = 37 * result + this.iBelegIId.hashCode();
		result = 37 * result + this.artikelIId.hashCode();

		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += ", " + tLiefertermin;
		returnString += ", " + tLiefertermin;
		returnString += ", " + nMenge;
		returnString += ", " + cBelegartCNr;
		returnString += ", " + iBelegIId;
		returnString += ", " + artikelIId;
		return returnString;
	}

	public boolean getBTemporaererEintrag() {
		return bTemporaererEintrag;
	}

	public String getCProjekt() {
		return cProjekt;
	}

	public void setBTemporaererEintrag(boolean bTemporaererEintrag) {
		this.bTemporaererEintrag = bTemporaererEintrag;
	}

	public void setCProjekt(String cProjekt) {
		this.cProjekt = cProjekt;
	}
}
