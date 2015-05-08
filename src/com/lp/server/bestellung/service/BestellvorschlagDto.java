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

public class BestellvorschlagDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String cMandantCNr;
	private Integer iArtikelId;
	private BigDecimal nZubestellendeMenge;
	private Timestamp tLiefertermin;
	private String cBelegartCNr;
	private Integer iBelegartId;
	private Integer iLieferantId;
	private BigDecimal nNettoeinzelpreis;
	private BigDecimal nRabattbetrag;
	private BigDecimal nNettogesamtpreis;
	private BigDecimal nNettoGesamtPreisMinusRabatte;
	private Double dRabattsatz;
	private Integer iBelegartpositionid;
	private Short bNettopreisuebersteuert;
	
	private Integer personalIIdVormerkung;
	
	private Timestamp tVormerkung;

	public Integer getPersonalIIdVormerkung() {
		return personalIIdVormerkung;
	}

	public void setPersonalIIdVormerkung(Integer personalIIdVormerkung) {
		this.personalIIdVormerkung = personalIIdVormerkung;
	}

	public Timestamp getTVormerkung() {
		return tVormerkung;
	}

	public void setTVormerkung(Timestamp tVormerkung) {
		this.tVormerkung = tVormerkung;
	}
	
	private String xTextinhalt;
	public final String getXTextinhalt() {
		return this.xTextinhalt;
	}

	public final void setXTextinhalt(String xTextinhalt) {
		this.xTextinhalt = xTextinhalt;
	}

	private Short bVormerkung;

	public Short getBVormerkung() {
		return bVormerkung;
	}

	public void setBVormerkung(Short bVormerkung) {
		this.bVormerkung = bVormerkung;
	}
	
	private Integer projektIId;

	public Integer getProjektIId() {
		return projektIId;
	}

	public void setProjektIId(Integer projektIId) {
		this.projektIId = projektIId;
	}
	
	
	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Double getDRabattsatz() {
		return dRabattsatz;
	}

	public void setDRabattsatz(Double dRabattsatz) {
		this.dRabattsatz = dRabattsatz;
	}

	public String getCMandantCNr() {
		return cMandantCNr;
	}

	public void setCMandantCNr(String cMandantCNr) {
		this.cMandantCNr = cMandantCNr;
	}

	public Integer getIArtikelId() {
		return iArtikelId;
	}

	public void setIArtikelId(Integer iArtikelId) {
		this.iArtikelId = iArtikelId;
	}

	public BigDecimal getNZubestellendeMenge() {
		return nZubestellendeMenge;
	}

	public void setNZubestellendeMenge(BigDecimal nZubestellendeMenge) {
		this.nZubestellendeMenge = nZubestellendeMenge;
	}

	public Timestamp getTLiefertermin() {
		return tLiefertermin;
	}

	public void setTLiefertermin(Timestamp tLiefertermin) {
		this.tLiefertermin = tLiefertermin;
	}

	public String getCBelegartCNr() {
		return cBelegartCNr;
	}

	public void setCBelegartCNr(String cBelegartCNr) {
		this.cBelegartCNr = cBelegartCNr;
	}

	public Integer getIBelegartId() {
		return iBelegartId;
	}

	public void setIBelegartId(Integer iBelegartId) {
		this.iBelegartId = iBelegartId;
	}

	public Integer getILieferantId() {
		return iLieferantId;
	}

	public void setILieferantId(Integer iLieferantId) {
		this.iLieferantId = iLieferantId;
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

	public void setNNettoGesamtPreisMinusRabatte(
			BigDecimal nNettoGesamtPreisMinusRabatte) {
		this.nNettoGesamtPreisMinusRabatte = nNettoGesamtPreisMinusRabatte;
	}

	public void setIBelegartpositionid(Integer iBelegartpositionid) {
		this.iBelegartpositionid = iBelegartpositionid;
	}

	public BigDecimal getNNettoGesamtPreisMinusRabatte() {
		return nNettoGesamtPreisMinusRabatte;
	}

	public Integer getIBelegartpositionid() {
		return iBelegartpositionid;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof BestellvorschlagDto)) {
			return false;
		}
		BestellvorschlagDto that = (BestellvorschlagDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId))) {
			return false;
		}
		if (!(that.cMandantCNr == null ? this.cMandantCNr == null
				: that.cMandantCNr.equals(this.cMandantCNr))) {
			return false;
		}
		if (!(that.iArtikelId == null ? this.iArtikelId == null
				: that.iArtikelId.equals(this.iArtikelId))) {
			return false;
		}
		if (!(that.nZubestellendeMenge == null ? this.nZubestellendeMenge == null
				: that.nZubestellendeMenge.equals(this.nZubestellendeMenge))) {
			return false;
		}
		if (!(that.tLiefertermin == null ? this.tLiefertermin == null
				: that.tLiefertermin.equals(this.tLiefertermin))) {
			return false;
		}
		if (!(that.cBelegartCNr == null ? this.cBelegartCNr == null
				: that.cBelegartCNr.equals(this.cBelegartCNr))) {
			return false;
		}
		if (!(that.iBelegartId == null ? this.iBelegartId == null
				: that.iBelegartId.equals(this.iBelegartId))) {
			return false;
		}
		if (!(that.iLieferantId == null ? this.iLieferantId == null
				: that.iLieferantId.equals(this.iLieferantId))) {
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
		if (!(that.nNettoGesamtPreisMinusRabatte == null ? this.nNettoGesamtPreisMinusRabatte == null
				: that.nNettoGesamtPreisMinusRabatte
						.equals(this.nNettoGesamtPreisMinusRabatte))) {
			return false;
		}
		if (!(that.dRabattsatz == null ? this.dRabattsatz == null
				: that.dRabattsatz.equals(this.dRabattsatz))) {
			return false;
		}

		return true;
	}

	public Short getBNettopreisuebersteuert() {
		return this.bNettopreisuebersteuert;
	}

	public void setBNettopreisuebersteuert(Short bNettopreisuebersteuert) {
		this.bNettopreisuebersteuert = bNettopreisuebersteuert;
	}

	
	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.cMandantCNr.hashCode();
		result = 37 * result + this.iArtikelId.hashCode();
		result = 37 * result + this.nZubestellendeMenge.hashCode();
		result = 37 * result + this.tLiefertermin.hashCode();
		result = 37 * result + this.cBelegartCNr.hashCode();
		result = 37 * result + this.iBelegartId.hashCode();
		result = 37 * result + this.iLieferantId.hashCode();
		result = 37 * result + this.nNettoeinzelpreis.hashCode();
		result = 37 * result + this.nRabattbetrag.hashCode();
		result = 37 * result + this.nNettogesamtpreis.hashCode();
		result = 37 * result + this.nNettoGesamtPreisMinusRabatte.hashCode();
		result = 37 * result + this.dRabattsatz.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + cMandantCNr;
		returnString += ", " + iArtikelId;
		returnString += ", " + nZubestellendeMenge;
		returnString += ", " + tLiefertermin;
		returnString += ", " + cBelegartCNr;
		returnString += ", " + iBelegartId;
		returnString += ", " + iLieferantId;
		returnString += ", " + nNettoeinzelpreis;
		returnString += ", " + nRabattbetrag;
		returnString += ", " + nNettogesamtpreis;
		returnString += ", " + nNettoGesamtPreisMinusRabatte;
		returnString += ", " + dRabattsatz;
		return returnString;
	}
}