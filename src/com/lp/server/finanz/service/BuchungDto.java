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
package com.lp.server.finanz.service;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class BuchungDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String buchungsartCNr;
	private Date dBuchungsdatum;
	private String cText;
	private Integer kostenstelleIId;
	private String cBelegnummer;
	private Timestamp tAnlegen;
	private Integer personalIIdAnlegen;
	private Timestamp tAendern;
	private Integer personalIIdAendern;
	private Integer iGeschaeftsjahr;
	private Timestamp tStorniert;
	private Integer personalIIdStorniert;
	private String belegartCNr;
	private Short  bAutomatischeBuchung ;
	private Short  bAutomatischeBuchungEB ;
	
	public BuchungDto(){
	}
	
	public BuchungDto(String buchungsartCNr, Date dBuchungsdatum, String cText, Integer iGeschaeftsjahr, String belegartCNr, boolean bAutomatischeBuchung, boolean bAutomatischeBuchungEB) {
		this.buchungsartCNr = buchungsartCNr;
		this.dBuchungsdatum = dBuchungsdatum;
		this.cText = cText;
		this.iGeschaeftsjahr = iGeschaeftsjahr;
		this.belegartCNr = belegartCNr;
		this.setAutomatischeBuchung(bAutomatischeBuchung);
		this.setAutomatischeBuchungEB(bAutomatischeBuchungEB) ;
	}
	
	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getBuchungsartCNr() {
		return buchungsartCNr;
	}

	public void setBuchungsartCNr(String buchungsartCNr) {
		this.buchungsartCNr = buchungsartCNr;
	}

	public Date getDBuchungsdatum() {
		return dBuchungsdatum;
	}

	public void setDBuchungsdatum(Date dBuchungsdatum) {
		this.dBuchungsdatum = dBuchungsdatum;
	}

	public String getCText() {
		return cText;
	}

	public void setCText(String cText) {
		this.cText = cText;
	}

	public Integer getKostenstelleIId() {
		return kostenstelleIId;
	}

	public void setKostenstelleIId(Integer kostenstelleIId) {
		this.kostenstelleIId = kostenstelleIId;
	}

	public String getCBelegnummer() {
		return cBelegnummer;
	}

	public void setCBelegnummer(String cBelegnummer) {
		this.cBelegnummer = cBelegnummer;
	}

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
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

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Integer getIGeschaeftsjahr() {
		return iGeschaeftsjahr;
	}

	public void setIGeschaeftsjahr(Integer iGeschaeftsjahr) {
		this.iGeschaeftsjahr = iGeschaeftsjahr;
	}

	public void setTStorniert(Timestamp tStorniert) {
		this.tStorniert = tStorniert;
	}

	public Timestamp getTStorniert() {
		return tStorniert;
	}

	public void setPersonalIIdStorniert(Integer personalIIdStorniert) {
		this.personalIIdStorniert = personalIIdStorniert;
	}

	public Integer getPersonalIIdStorniert() {
		return personalIIdStorniert;
	}

	public void setBelegartCNr(String belegartCNr) {
		this.belegartCNr = belegartCNr;
	}

	public String getBelegartCNr() {
		return belegartCNr;
	}
	
	public Short getbAutomatischeBuchung() {
		return bAutomatischeBuchung;
	}

	public void setbAutomatischeBuchung(Short bAutomatischeBuchung) {
		this.bAutomatischeBuchung = bAutomatischeBuchung;
	}

	public boolean isAutomatischeBuchung() {
		if(null == bAutomatischeBuchung) return false ;
		return 0 != bAutomatischeBuchung ;
	}

	public void setAutomatischeBuchung(boolean automatischeBuchung) {
		bAutomatischeBuchung = new Short(automatischeBuchung ? (short)1 : (short) 0);
	}

	public Short getbAutomatischeBuchungEB() {
		return bAutomatischeBuchungEB;
	}

	public void setbAutomatischeBuchungEB(Short bAutomatischeBuchungEB) {
		this.bAutomatischeBuchungEB = bAutomatischeBuchungEB;
	}
	
	public void setAutomatischeBuchungEB(boolean automatischeBuchung) {
		bAutomatischeBuchungEB = new Short(automatischeBuchung ? (short)1 : (short) 0);		
	}

	public boolean isAutomatischeBuchungEB() {
		if(null == bAutomatischeBuchungEB) return false ;
		return 0 != bAutomatischeBuchungEB ;
	}
	
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof BuchungDto)) {
			return false;
		}
		BuchungDto that = (BuchungDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId))) {
			return false;
		}
		if (!(that.buchungsartCNr == null ? this.buchungsartCNr == null
				: that.buchungsartCNr.equals(this.buchungsartCNr))) {
			return false;
		}
		if (!(that.dBuchungsdatum == null ? this.dBuchungsdatum == null
				: that.dBuchungsdatum.equals(this.dBuchungsdatum))) {
			return false;
		}
		if (!(that.cText == null ? this.cText == null : that.cText
				.equals(this.cText))) {
			return false;
		}
		if (!(that.kostenstelleIId == null ? this.kostenstelleIId == null
				: that.kostenstelleIId.equals(this.kostenstelleIId))) {
			return false;
		}
		if (!(that.cBelegnummer == null ? this.cBelegnummer == null
				: that.cBelegnummer.equals(this.cBelegnummer))) {
			return false;
		}
		if (!(that.tAnlegen == null ? this.tAnlegen == null : that.tAnlegen
				.equals(this.tAnlegen))) {
			return false;
		}
		if (!(that.personalIIdAnlegen == null ? this.personalIIdAnlegen == null
				: that.personalIIdAnlegen.equals(this.personalIIdAnlegen))) {
			return false;
		}
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern
				.equals(this.tAendern))) {
			return false;
		}
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern))) {
			return false;
		}
		if (!(that.iGeschaeftsjahr == null ? this.iGeschaeftsjahr == null
				: that.iGeschaeftsjahr.equals(this.iGeschaeftsjahr))) {
			return false;
		}
		if (!(that.belegartCNr == null ? this.belegartCNr == null
				: that.belegartCNr.equals(this.belegartCNr))) {
			return false;
				}
		if (!(that.bAutomatischeBuchung == null ? this.bAutomatischeBuchung == null
				: that.bAutomatischeBuchung.equals(this.bAutomatischeBuchung))) {
			return false;
		}
		if (!(that.bAutomatischeBuchungEB == null ? this.bAutomatischeBuchungEB == null
				: that.bAutomatischeBuchungEB.equals(this.bAutomatischeBuchungEB))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + iId.hashCode();
		result = 37 * result + buchungsartCNr.hashCode();
		result = 37 * result + dBuchungsdatum.hashCode();
		result = 37 * result + cText.hashCode();
		result = 37 * result + kostenstelleIId.hashCode();
		result = 37 * result + cBelegnummer.hashCode();
		result = 37 * result + tAnlegen.hashCode();
		result = 37 * result + personalIIdAnlegen.hashCode();
		result = 37 * result + tAendern.hashCode();
		result = 37 * result + personalIIdAendern.hashCode();
		result = 37 * result + iGeschaeftsjahr.hashCode();
		result = 37 * result + belegartCNr.hashCode();
		result = 37 * result + bAutomatischeBuchung.hashCode() ;
		result = 37 * result + bAutomatischeBuchungEB.hashCode() ;
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + buchungsartCNr;
		returnString += ", " + dBuchungsdatum;
		returnString += ", " + cText;
		returnString += ", " + kostenstelleIId;
		returnString += ", " + cBelegnummer;
		returnString += ", " + tAnlegen;
		returnString += ", " + personalIIdAnlegen;
		returnString += ", " + tAendern;
		returnString += ", " + personalIIdAendern;
		returnString += ", " + iGeschaeftsjahr;
		returnString += ", " + belegartCNr;
		returnString += ", " + bAutomatischeBuchung ;
		returnString += ", " + bAutomatischeBuchungEB ;
		return returnString;
	}

}
