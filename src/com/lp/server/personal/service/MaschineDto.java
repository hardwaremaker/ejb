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
package com.lp.server.personal.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class MaschineDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iId;
	private String mandantCNr;
	private String cInventarnummer;
	private String cBez;
	private Short bAutoendebeigeht;
	private Timestamp tKaufdatum;

	private Integer artikelIIdVerrechnen;

	public Integer getArtikelIIdVerrechnen() {
		return artikelIIdVerrechnen;
	}

	public void setArtikelIIdVerrechnen(Integer artikelIIdVerrechnen) {
		this.artikelIIdVerrechnen = artikelIIdVerrechnen;
	}

	private Short bVersteckt;

	public Short getBVersteckt() {
		return bVersteckt;
	}

	public void setBVersteckt(Short versteckt) {
		bVersteckt = versteckt;
	}

	private Short bManuelleBedienung;

	public Short getBManuelleBedienung() {
		return bManuelleBedienung;
	}

	public void setBManuelleBedienung(Short bManuelleBedienung) {
		this.bManuelleBedienung = bManuelleBedienung;
	}

	private BigDecimal nAnschaffungskosten;

	public BigDecimal getNAnschaffungskosten() {
		return nAnschaffungskosten;
	}

	public void setNAnschaffungskosten(BigDecimal nAnschaffungskosten) {
		this.nAnschaffungskosten = nAnschaffungskosten;
	}

	public Integer getIAbschreibungInMonaten() {
		return iAbschreibungInMonaten;
	}

	public void setIAbschreibungInMonaten(Integer iAbschreibungInMonaten) {
		this.iAbschreibungInMonaten = iAbschreibungInMonaten;
	}

	public BigDecimal getNVerzinsung() {
		return nVerzinsung;
	}

	public void setNVerzinsung(BigDecimal nVerzinsung) {
		this.nVerzinsung = nVerzinsung;
	}

	public BigDecimal getNEnergiekosten() {
		return nEnergiekosten;
	}

	public void setNEnergiekosten(BigDecimal nEnergiekosten) {
		this.nEnergiekosten = nEnergiekosten;
	}

	public BigDecimal getNRaumkosten() {
		return nRaumkosten;
	}

	public void setNRaumkosten(BigDecimal nRaumkosten) {
		this.nRaumkosten = nRaumkosten;
	}

	public BigDecimal getNSonstigekosten() {
		return nSonstigekosten;
	}

	public void setNSonstigekosten(BigDecimal nSonstigekosten) {
		this.nSonstigekosten = nSonstigekosten;
	}

	public Integer getIPlanstunden() {
		return iPlanstunden;
	}

	public void setIPlanstunden(Integer iPlanstunden) {
		this.iPlanstunden = iPlanstunden;
	}

	private Integer iAbschreibungInMonaten;

	private BigDecimal nVerzinsung;

	private BigDecimal nEnergiekosten;

	private BigDecimal nRaumkosten;

	private BigDecimal nSonstigekosten;

	private Integer iPlanstunden;

	public Integer getIId() {
		return iId;
	}

	
	private String cSeriennummer;
	
	public String getCSeriennummer() {
		return cSeriennummer;
	}

	public void setCSeriennummer(String cSeriennummer) {
		this.cSeriennummer = cSeriennummer;
	}

	
	private Integer maschinengruppeIId;

	private String cIdentifikationsnr;

	public String getBezeichnung() {
		String back = "";
		if (getCIdentifikationsnr() != null) {
			back += getCIdentifikationsnr();
		}
		if (getCInventarnummer() != null) {
			back += " " + getCInventarnummer();
		}
		if (getCBez() != null) {
			back += " " + getCBez();
		}
		return back;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public String getCInventarnummer() {
		return cInventarnummer;
	}

	public void setCInventarnummer(String cInventarnummer) {
		this.cInventarnummer = cInventarnummer;
	}

	public String getCBez() {
		return cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public Short getBAutoendebeigeht() {
		return bAutoendebeigeht;
	}

	public void setBAutoendebeigeht(Short bAutoendebeigeht) {
		this.bAutoendebeigeht = bAutoendebeigeht;
	}

	public Timestamp getTKaufdatum() {
		return tKaufdatum;
	}

	public String getCIdentifikationsnr() {
		return cIdentifikationsnr;
	}

	public Integer getMaschinengruppeIId() {
		return maschinengruppeIId;
	}

	public void setTKaufdatum(Timestamp tKaufdatum) {
		this.tKaufdatum = tKaufdatum;
	}

	public void setCIdentifikationsnr(String cIdentifikationsnr) {
		this.cIdentifikationsnr = cIdentifikationsnr;
	}

	public void setMaschinengruppeIId(Integer maschinengruppeIId) {
		this.maschinengruppeIId = maschinengruppeIId;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof MaschineDto))
			return false;
		MaschineDto that = (MaschineDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.mandantCNr == null ? this.mandantCNr == null : that.mandantCNr.equals(this.mandantCNr)))
			return false;
		if (!(that.cInventarnummer == null ? this.cInventarnummer == null
				: that.cInventarnummer.equals(this.cInventarnummer)))
			return false;
		if (!(that.cBez == null ? this.cBez == null : that.cBez.equals(this.cBez)))
			return false;
		if (!(that.bAutoendebeigeht == null ? this.bAutoendebeigeht == null
				: that.bAutoendebeigeht.equals(this.bAutoendebeigeht)))
			return false;
		if (!(that.tKaufdatum == null ? this.tKaufdatum == null : that.tKaufdatum.equals(this.tKaufdatum)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.mandantCNr.hashCode();
		result = 37 * result + this.cInventarnummer.hashCode();
		result = 37 * result + this.cBez.hashCode();
		result = 37 * result + this.bAutoendebeigeht.hashCode();
		result = 37 * result + this.tKaufdatum.hashCode();
		return result;
	}

	public String toString() {
		String back = "";
		if (getCIdentifikationsnr() != null) {
			back += getCIdentifikationsnr();
		}
		if (getCInventarnummer() != null) {
			back += " " + getCInventarnummer();
		}
		if (getCBez() != null) {
			back += " " + getCBez();
		}
		return back;
	}
}
