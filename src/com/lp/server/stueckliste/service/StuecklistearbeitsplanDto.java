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
package com.lp.server.stueckliste.service;

import java.math.BigDecimal;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.personal.ejb.Maschine;
import com.lp.server.system.service.HvDtoLogClass;
import com.lp.server.system.service.HvDtoLogIdCBez;
import com.lp.server.system.service.HvDtoLogIgnore;
import com.lp.service.BelegpositionDto;

@HvDtoLogClass(name=HvDtoLogClass.STUECKLISTE_ARBEITSPLAN, filtername=HvDtoLogClass.STUECKLISTE)
public class StuecklistearbeitsplanDto  extends BelegpositionDto{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Integer stuecklisteIId;
	private Integer iArbeitsgang;
//	private Integer artikelIId;
	private Long lStueckzeit;
	private Long lRuestzeit;
	private String cKommentar;
	private String xLangtext;
	private ArtikelDto artikelDto;
	private String agartCNr;
	private Integer iUnterarbeitsgang;
	private Short bNurmaschinenzeit;
	private Integer iMaschinenversatztage;
	private Integer stuecklistepositionIId;
	
	public Integer getStuecklistepositionIId() {
		return stuecklistepositionIId;
	}

	public void setStuecklistepositionIId(Integer stuecklistepositionIId) {
		this.stuecklistepositionIId = stuecklistepositionIId;
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

	public BigDecimal nMengeWennHilfsstueckliste;
	
	
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

	private Integer iAufspannung;
	

	private Integer maschineIId;
	
	public Integer getStuecklisteIId() {
		return stuecklisteIId;
	}

	public void setStuecklisteIId(Integer stuecklisteIId) {
		this.stuecklisteIId = stuecklisteIId;
	}

	public Integer getIArbeitsgang() {
		return iArbeitsgang;
	}

	public void setIArbeitsgang(Integer iArbeitsgang) {
		this.iArbeitsgang = iArbeitsgang;
	}

	public Long getLStueckzeit() {
		return lStueckzeit;
	}

	public void setLStueckzeit(Long lStueckzeit) {
		this.lStueckzeit = lStueckzeit;
	}

	public Long getLRuestzeit() {
		return lRuestzeit;
	}

	public void setLRuestzeit(Long lRuestzeit) {
		this.lRuestzeit = lRuestzeit;
	}

	public String getCKommentar() {
		return cKommentar;
	}

	public void setCKommentar(String cKommentar) {
		this.cKommentar = cKommentar;
	}

	public String getXLangtext() {
		return xLangtext;
	}

	@HvDtoLogIgnore
	public ArtikelDto getArtikelDto() {
		return artikelDto;
	}

	@HvDtoLogIdCBez(entityClass=Maschine.class)
	public Integer getMaschineIId() {
		return maschineIId;
	}

	public void setXLangtext(String xLangtext) {
		this.xLangtext = xLangtext;
	}

	public void setArtikelDto(ArtikelDto artikelDto) {
		this.artikelDto = artikelDto;
	}

	public void setMaschineIId(Integer maschineIId) {
		this.maschineIId = maschineIId;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof StuecklistearbeitsplanDto))
			return false;
		StuecklistearbeitsplanDto that = (StuecklistearbeitsplanDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.stuecklisteIId == null ? this.stuecklisteIId == null
				: that.stuecklisteIId.equals(this.stuecklisteIId)))
			return false;
		if (!(that.iArbeitsgang == null ? this.iArbeitsgang == null
				: that.iArbeitsgang.equals(this.iArbeitsgang)))
			return false;
//		if (!(that.artikelIId == null ? this.artikelIId == null
//				: that.artikelIId.equals(this.artikelIId)))
//			return false;
		if (!(that.lStueckzeit == null ? this.lStueckzeit == null
				: that.lStueckzeit.equals(this.lStueckzeit)))
			return false;
		if (!(that.lRuestzeit == null ? this.lRuestzeit == null
				: that.lRuestzeit.equals(this.lRuestzeit)))
			return false;
		if (!(that.cKommentar == null ? this.cKommentar == null
				: that.cKommentar.equals(this.cKommentar)))
			return false;
		if (!(that.xLangtext == null ? this.xLangtext == null : that.xLangtext
				.equals(this.xLangtext)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.stuecklisteIId.hashCode();
		result = 37 * result + this.iArbeitsgang.hashCode();
//		result = 37 * result + this.artikelIId.hashCode();
		result = 37 * result + this.lStueckzeit.hashCode();
		result = 37 * result + this.lRuestzeit.hashCode();
		result = 37 * result + this.cKommentar.hashCode();
		result = 37 * result + this.xLangtext.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + stuecklisteIId;
		returnString += ", " + iArbeitsgang;
//		returnString += ", " + artikelIId;
		returnString += ", " + lStueckzeit;
		returnString += ", " + lRuestzeit;
		returnString += ", " + cKommentar;
		returnString += ", " + xLangtext;
		return returnString;
	}
}
