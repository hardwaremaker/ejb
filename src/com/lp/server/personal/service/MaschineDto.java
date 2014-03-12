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
package com.lp.server.personal.service;

import java.io.Serializable;
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
	private Double fVerfuegbarkeitinprozent;
	private Short bAutoendebeigeht;
	private Timestamp tKaufdatum;
	private Short bVersteckt;
	public Short getBVersteckt() {
		return bVersteckt;
	}

	public void setBVersteckt(Short versteckt) {
		bVersteckt = versteckt;
	}
	public Integer getIId() {
		return iId;
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

	public Double getFVerfuegbarkeitinprozent() {
		return fVerfuegbarkeitinprozent;
	}

	public void setFVerfuegbarkeitinprozent(Double fVerfuegbarkeitinprozent) {
		this.fVerfuegbarkeitinprozent = fVerfuegbarkeitinprozent;
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
		if (!(that.mandantCNr == null ? this.mandantCNr == null
				: that.mandantCNr.equals(this.mandantCNr)))
			return false;
		if (!(that.cInventarnummer == null ? this.cInventarnummer == null
				: that.cInventarnummer.equals(this.cInventarnummer)))
			return false;
		if (!(that.cBez == null ? this.cBez == null : that.cBez
				.equals(this.cBez)))
			return false;
		if (!(that.fVerfuegbarkeitinprozent == null ? this.fVerfuegbarkeitinprozent == null
				: that.fVerfuegbarkeitinprozent
						.equals(this.fVerfuegbarkeitinprozent)))
			return false;
		if (!(that.bAutoendebeigeht == null ? this.bAutoendebeigeht == null
				: that.bAutoendebeigeht.equals(this.bAutoendebeigeht)))
			return false;
		if (!(that.tKaufdatum == null ? this.tKaufdatum == null
				: that.tKaufdatum.equals(this.tKaufdatum)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.mandantCNr.hashCode();
		result = 37 * result + this.cInventarnummer.hashCode();
		result = 37 * result + this.cBez.hashCode();
		result = 37 * result + this.fVerfuegbarkeitinprozent.hashCode();
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
