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
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.lp.util.Helper;

public class StundenabrechnungDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iId;
	private Integer personalIId;
	private Timestamp tDatum;
	private BigDecimal nMehrstunden;
	private BigDecimal nUestfrei50;
	private BigDecimal nUestpflichtig50;
	private BigDecimal nUestfrei100;
	private BigDecimal nUestpflichtig100;
	private BigDecimal nGutstunden;
	private BigDecimal nQualifikationspraemie;
	private Timestamp tAendern;
	private Integer personalIIdAendern;
	private BigDecimal nUest200;
	
	
	public BigDecimal getNUest200() {
		return nUest200;
	}

	public void setNUest200(BigDecimal uest200) {
		nUest200 = uest200;
	}

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getPersonalIId() {
		return personalIId;
	}

	public void setPersonalIId(Integer personalIId) {
		this.personalIId = personalIId;
	}

	public Timestamp getTDatum() {
		return tDatum;
	}

	public void setTDatum(Timestamp tDatum) {
		this.tDatum = Helper.cutTimestamp(tDatum);
	}

	public BigDecimal getNMehrstunden() {
		return nMehrstunden;
	}

	public void setNMehrstunden(BigDecimal nMehrstunden) {
		this.nMehrstunden = nMehrstunden;
	}

	public BigDecimal getNUestfrei50() {
		return nUestfrei50;
	}

	public void setNUestfrei50(BigDecimal nUestfrei50) {
		this.nUestfrei50 = nUestfrei50;
	}

	public BigDecimal getNUestpflichtig50() {
		return nUestpflichtig50;
	}

	public void setNUestpflichtig50(BigDecimal nUestpflichtig50) {
		this.nUestpflichtig50 = nUestpflichtig50;
	}

	public BigDecimal getNUestfrei100() {
		return nUestfrei100;
	}

	public void setNUestfrei100(BigDecimal nUestfrei100) {
		this.nUestfrei100 = nUestfrei100;
	}

	public BigDecimal getNUestpflichtig100() {
		return nUestpflichtig100;
	}

	public void setNUestpflichtig100(BigDecimal nUestpflichtig100) {
		this.nUestpflichtig100 = nUestpflichtig100;
	}

	public BigDecimal getNGutstunden() {
		return nGutstunden;
	}

	public void setNGutstunden(BigDecimal nGutstunden) {
		this.nGutstunden = nGutstunden;
	}

	private String cKommentar;
	private BigDecimal nNormalstunden;

	public BigDecimal getNQualifikationspraemie() {
		return nQualifikationspraemie;
	}

	public void setNQualifikationspraemie(BigDecimal nQualifikationspraemie) {
		this.nQualifikationspraemie = nQualifikationspraemie;
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

	public String getCKommentar() {
		return cKommentar;
	}

	public BigDecimal getNNormalstunden() {
		return nNormalstunden;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public void setCKommentar(String cKommentar) {
		this.cKommentar = cKommentar;
	}

	public void setNNormalstunden(BigDecimal nNormalstunden) {
		this.nNormalstunden = nNormalstunden;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof StundenabrechnungDto))
			return false;
		StundenabrechnungDto that = (StundenabrechnungDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.personalIId == null ? this.personalIId == null
				: that.personalIId.equals(this.personalIId)))
			return false;
		if (!(that.tDatum == null ? this.tDatum == null : that.tDatum
				.equals(this.tDatum)))
			return false;
		if (!(that.nMehrstunden == null ? this.nMehrstunden == null
				: that.nMehrstunden.equals(this.nMehrstunden)))
			return false;
		if (!(that.nUestfrei50 == null ? this.nUestfrei50 == null
				: that.nUestfrei50.equals(this.nUestfrei50)))
			return false;
		if (!(that.nUestpflichtig50 == null ? this.nUestpflichtig50 == null
				: that.nUestpflichtig50.equals(this.nUestpflichtig50)))
			return false;
		if (!(that.nUestfrei100 == null ? this.nUestfrei100 == null
				: that.nUestfrei100.equals(this.nUestfrei100)))
			return false;
		if (!(that.nUestpflichtig100 == null ? this.nUestpflichtig100 == null
				: that.nUestpflichtig100.equals(this.nUestpflichtig100)))
			return false;
		if (!(that.nGutstunden == null ? this.nGutstunden == null
				: that.nGutstunden.equals(this.nGutstunden)))
			return false;
		if (!(that.nQualifikationspraemie == null ? this.nQualifikationspraemie == null
				: that.nQualifikationspraemie
						.equals(this.nQualifikationspraemie)))
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
		result = 37 * result + this.personalIId.hashCode();
		result = 37 * result + this.tDatum.hashCode();
		result = 37 * result + this.nMehrstunden.hashCode();
		result = 37 * result + this.nUestfrei50.hashCode();
		result = 37 * result + this.nUestpflichtig50.hashCode();
		result = 37 * result + this.nUestfrei100.hashCode();
		result = 37 * result + this.nUestpflichtig100.hashCode();
		result = 37 * result + this.nGutstunden.hashCode();
		result = 37 * result + this.nQualifikationspraemie.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + personalIId;
		returnString += ", " + tDatum;
		returnString += ", " + nMehrstunden;
		returnString += ", " + nUestfrei50;
		returnString += ", " + nUestpflichtig50;
		returnString += ", " + nUestfrei100;
		returnString += ", " + nUestpflichtig100;
		returnString += ", " + nGutstunden;
		returnString += ", " + nQualifikationspraemie;
		returnString += ", " + tAendern;
		returnString += ", " + personalIIdAendern;
		return returnString;
	}
}
