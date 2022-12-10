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
package com.lp.server.fertigung.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.lp.service.BelegpositionDto;

public class LossollarbeitsplanDto extends BelegpositionDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Integer losIId;
	private Integer artikelIIdTaetigkeit;
	private Long lRuestzeit;
	private Long lStueckzeit;
	private BigDecimal nGesamtzeit;
	private Integer iArbeitsgangnummer;
	private Integer personalIIdAendern;
	private Timestamp tAendern;
	private String xText;
	private String cKomentar;
	private Short bNachtraeglich;
	private Integer maschineIId;
	private Short bFertig;
	private Integer iUnterarbeitsgang;
	private Short bAutoendebeigeht;
	private Short bNurmaschinenzeit;

	private Timestamp  tAgbeginnBerechnet;
	
	
	public Timestamp getTAgbeginnBerechnet() {
		return tAgbeginnBerechnet;
	}

	public void setTAgbeginnBerechnet(Timestamp tAgbeginnBerechnet) {
		this.tAgbeginnBerechnet = tAgbeginnBerechnet;
	}

	
	private Integer iReihung;

	public Integer getIReihung() {
		return iReihung;
	}

	public void setIReihung(Integer iReihung) {
		this.iReihung = iReihung;
	}

	private Integer iMaschinenversatztageAusStueckliste;

	public Integer getIMaschinenversatztageAusStueckliste() {
		return iMaschinenversatztageAusStueckliste;
	}

	public void setIMaschinenversatztageAusStueckliste(Integer iMaschinenversatztageAusStueckliste) {
		this.iMaschinenversatztageAusStueckliste = iMaschinenversatztageAusStueckliste;
	}

	private Integer iMaschinenversatztage;
	private Integer personalIIdZugeordneter;

	private BigDecimal nPpm;

	public BigDecimal getNPpm() {
		return nPpm;
	}

	public void setNPpm(BigDecimal nPpm) {
		this.nPpm = nPpm;
	}

	private Integer apkommentarIId;

	public Integer getApkommentarIId() {
		return apkommentarIId;
	}

	public void setApkommentarIId(Integer apkommentarIId) {
		this.apkommentarIId = apkommentarIId;
	}

	private Double fFortschritt;

	public Double getFFortschritt() {
		return fFortschritt;
	}

	public void setFFortschritt(Double fFortschritt) {
		this.fFortschritt = fFortschritt;
	}

	private Integer lossollmaterialIId;

	public Integer getLossollmaterialIId() {
		return lossollmaterialIId;
	}

	public void setLossollmaterialIId(Integer lossollmaterialIId) {
		this.lossollmaterialIId = lossollmaterialIId;
	}

	public Integer getPersonalIIdZugeordneter() {
		return personalIIdZugeordneter;
	}

	public void setPersonalIIdZugeordneter(Integer personalIIdZugeordneter) {
		this.personalIIdZugeordneter = personalIIdZugeordneter;
	}

	public Integer getIMaschinenversatztage() {
		return iMaschinenversatztage;
	}

	public void setIMaschinenversatztage(Integer maschinenversatztage) {
		iMaschinenversatztage = maschinenversatztage;
	}

	public Short getBNurmaschinenzeit() {
		return bNurmaschinenzeit;
	}

	public void setBNurmaschinenzeit(Short nurmaschinenzeit) {
		bNurmaschinenzeit = nurmaschinenzeit;
	}

	public Integer getIUnterarbeitsgang() {
		return iUnterarbeitsgang;
	}

	public void setIUnterarbeitsgang(Integer unterarbeitsgang) {
		iUnterarbeitsgang = unterarbeitsgang;
	}

	public Short getBAutoendebeigeht() {
		return bAutoendebeigeht;
	}

	public void setBAutoendebeigeht(Short autoendebeigeht) {
		bAutoendebeigeht = autoendebeigeht;
	}

	public String getAgartCNr() {
		return agartCNr;
	}

	public void setAgartCNr(String agartCNr) {
		this.agartCNr = agartCNr;
	}

	public Integer getIAufspannung() {
		return iAufspannung;
	}

	public void setIAufspannung(Integer aufspannung) {
		iAufspannung = aufspannung;
	}

	private String agartCNr;
	private Integer iAufspannung;

	public Short getBFertig() {
		return bFertig;
	}

	public void setBFertig(Short fertig) {
		bFertig = fertig;
	}

	public Integer getLossollarbeitsplanIId() {
		return getBelegIId();
	}

	public Integer getLosIId() {
		return losIId;
	}

	public void setLosIId(Integer losIId) {
		this.losIId = losIId;
	}

	public Integer getArtikelIIdTaetigkeit() {
		return artikelIIdTaetigkeit;
	}

	public void setArtikelIIdTaetigkeit(Integer artikelIIdTaetigkeit) {
		this.artikelIIdTaetigkeit = artikelIIdTaetigkeit;
	}

	public Long getLRuestzeit() {
		return lRuestzeit;
	}

	public void setLRuestzeit(Long lRuestzeit) {
		this.lRuestzeit = lRuestzeit;
	}

	public BigDecimal getStueckzeit() {
		return new BigDecimal(getLStueckzeit()).divide(new BigDecimal(3600000), 4, BigDecimal.ROUND_HALF_EVEN);
	}

	public BigDecimal getRuestzeit() {
		return new BigDecimal(getLRuestzeit()).divide(new BigDecimal(3600000), 4, BigDecimal.ROUND_HALF_EVEN);
	}

	public Long getLStueckzeit() {
		return lStueckzeit;
	}

	public void setLStueckzeit(Long lStueckzeit) {
		this.lStueckzeit = lStueckzeit;
	}

	public BigDecimal getNGesamtzeit() {
		return nGesamtzeit;
	}

	public void setNGesamtzeit(BigDecimal nGesamtzeit) {
		this.nGesamtzeit = nGesamtzeit;
	}

	public Integer getIArbeitsgangnummer() {
		return iArbeitsgangnummer;
	}

	public void setIArbeitsgangnummer(Integer iArbeitsgangnummer) {
		this.iArbeitsgangnummer = iArbeitsgangnummer;
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

	public String getXText() {
		return xText;
	}

	public void setXText(String xText) {
		this.xText = xText;
	}

	public String getCKomentar() {
		return cKomentar;
	}

	public void setCKomentar(String cKomentar) {
		this.cKomentar = cKomentar;
	}

	public Short getBNachtraeglich() {
		return bNachtraeglich;
	}

	public Integer getMaschineIId() {
		return maschineIId;
	}

	public void setBNachtraeglich(Short bNachtraeglich) {
		this.bNachtraeglich = bNachtraeglich;
	}

	public void setMaschineIId(Integer maschineIId) {
		this.maschineIId = maschineIId;
	}

	private Integer iMaschinenversatzMs;

	public Integer getIMaschinenversatzMs() {
		return iMaschinenversatzMs;
	}

	public void setIMaschinenversatzMs(Integer iMaschinenversatzMs) {
		this.iMaschinenversatzMs = iMaschinenversatzMs;
	}

	private Timestamp tFertig;
	private Integer personalIIdFertig;

	public Timestamp getTFertig() {
		return tFertig;
	}

	public void setTFertig(Timestamp tFertig) {
		this.tFertig = tFertig;
	}

	public Integer getPersonalIIdFertig() {
		return personalIIdFertig;
	}

	public void setPersonalIIdFertig(Integer personalIIdFertig) {
		this.personalIIdFertig = personalIIdFertig;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof LossollarbeitsplanDto))
			return false;
		LossollarbeitsplanDto that = (LossollarbeitsplanDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.losIId == null ? this.losIId == null : that.losIId.equals(this.losIId)))
			return false;
		if (!(that.artikelIIdTaetigkeit == null ? this.artikelIIdTaetigkeit == null
				: that.artikelIIdTaetigkeit.equals(this.artikelIIdTaetigkeit)))
			return false;
		if (!(that.lRuestzeit == null ? this.lRuestzeit == null : that.lRuestzeit.equals(this.lRuestzeit)))
			return false;
		if (!(that.lStueckzeit == null ? this.lStueckzeit == null : that.lStueckzeit.equals(this.lStueckzeit)))
			return false;
		if (!(that.nGesamtzeit == null ? this.nGesamtzeit == null : that.nGesamtzeit.equals(this.nGesamtzeit)))
			return false;
		if (!(that.iArbeitsgangnummer == null ? this.iArbeitsgangnummer == null
				: that.iArbeitsgangnummer.equals(this.iArbeitsgangnummer)))
			return false;
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern)))
			return false;
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern.equals(this.tAendern)))
			return false;
		if (!(that.xText == null ? this.xText == null : that.xText.equals(this.xText)))
			return false;
		if (!(that.cKomentar == null ? this.cKomentar == null : that.cKomentar.equals(this.cKomentar)))
			return false;
		if (!(that.bNachtraeglich == null ? this.bNachtraeglich == null
				: that.bNachtraeglich.equals(this.bNachtraeglich)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.losIId.hashCode();
		result = 37 * result + this.artikelIIdTaetigkeit.hashCode();
		result = 37 * result + this.lRuestzeit.hashCode();
		result = 37 * result + this.lStueckzeit.hashCode();
		result = 37 * result + this.nGesamtzeit.hashCode();
		result = 37 * result + this.iArbeitsgangnummer.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.xText.hashCode();
		result = 37 * result + this.cKomentar.hashCode();
		result = 37 * result + this.bNachtraeglich.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + losIId;
		returnString += ", " + artikelIIdTaetigkeit;
		returnString += ", " + lRuestzeit;
		returnString += ", " + lStueckzeit;
		returnString += ", " + nGesamtzeit;
		returnString += ", " + iArbeitsgangnummer;
		returnString += ", " + personalIIdAendern;
		returnString += ", " + tAendern;
		returnString += ", " + xText;
		returnString += ", " + cKomentar;
		returnString += ", " + bNachtraeglich;
		return returnString;
	}
}
