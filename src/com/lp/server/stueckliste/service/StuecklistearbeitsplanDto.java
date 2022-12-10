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
package com.lp.server.stueckliste.service;

import java.math.BigDecimal;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.personal.ejb.Maschine;
import com.lp.server.system.service.HvDtoLogClass;
import com.lp.server.system.service.HvDtoLogIdCBez;
import com.lp.server.system.service.HvDtoLogIgnore;
import com.lp.service.BelegpositionDto;

@HvDtoLogClass(name = HvDtoLogClass.STUECKLISTE_ARBEITSPLAN, filtername = HvDtoLogClass.STUECKLISTE)
public class StuecklistearbeitsplanDto extends BelegpositionDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Integer stuecklisteIId;
	private Integer iArbeitsgang;
	// private Integer artikelIId;
	private Long lStueckzeit;
	private Long lRuestzeit;
	private String cKommentar;
	private String xLangtext;
	private ArtikelDto artikelDto;
	private String agartCNr;
	private Integer iUnterarbeitsgang;
	private Short bNurmaschinenzeit;
	private Integer iMaschinenversatztage;

	private Integer iMitarbeitergleichzeitig;

	public Integer getIMitarbeitergleichzeitig() {
		return iMitarbeitergleichzeitig;
	}

	public void setIMitarbeitergleichzeitig(Integer iMitarbeitergleichzeitig) {
		this.iMitarbeitergleichzeitig = iMitarbeitergleichzeitig;
	}

	private String xFormel;

	public String getXFormel() {
		return xFormel;
	}

	public void setXFormel(String formel) {
		this.xFormel = formel;
	}

	private Integer apkommentarIId;

	public Integer getApkommentarIId() {
		return apkommentarIId;
	}

	public void setApkommentarIId(Integer apkommentarIId) {
		this.apkommentarIId = apkommentarIId;
	}

	private BigDecimal nPpm;

	public BigDecimal getNPpm() {
		return nPpm;
	}

	public void setNPpm(BigDecimal nPpm) {
		this.nPpm = nPpm;
	}

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

	
	public Long getLStueckzeitDurchMitarbeitergleichzeitig(){
		if(getIMitarbeitergleichzeitig()!=null && getIMitarbeitergleichzeitig()>0){
			return lStueckzeit/getIMitarbeitergleichzeitig();
		} else {
			return lStueckzeit;
		}
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

	@HvDtoLogIdCBez(entityClass = Maschine.class)
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

	public boolean istGleich(StuecklistearbeitsplanDto obj) {
		StuecklistearbeitsplanDto other = (StuecklistearbeitsplanDto) obj;

		if (super.getArtikelIId() == null) {
			if (other.getArtikelIId() != null)
				return false;
		} else if (!super.getArtikelIId().equals(other.getArtikelIId()))
			return false;

		if (cKommentar == null) {
			if (other.cKommentar != null)
				return false;
		} else if (!cKommentar.equals(other.cKommentar))
			return false;

		if (xLangtext == null) {
			if (other.xLangtext != null)
				return false;
		} else if (!xLangtext.equals(other.xLangtext))
			return false;

		if (lRuestzeit == null) {
			if (other.lRuestzeit != null)
				return false;
		} else if (!lRuestzeit.equals(other.lRuestzeit))
			return false;

		if (lStueckzeit == null) {
			if (other.lStueckzeit != null)
				return false;
		} else if (!lStueckzeit.equals(other.lStueckzeit))
			return false;

		if (maschineIId == null) {
			if (other.maschineIId != null)
				return false;
		} else if (!maschineIId.equals(other.maschineIId))
			return false;

		if (agartCNr == null) {
			if (other.agartCNr != null)
				return false;
		} else if (!agartCNr.equals(other.agartCNr))
			return false;

		if (iAufspannung == null) {
			if (other.iAufspannung != null)
				return false;
		} else if (!iAufspannung.equals(other.iAufspannung))
			return false;

		if (iArbeitsgang == null) {
			if (other.iArbeitsgang != null)
				return false;
		} else if (!iArbeitsgang.equals(other.iArbeitsgang))
			return false;

		if (iUnterarbeitsgang == null) {
			if (other.iUnterarbeitsgang != null)
				return false;
		} else if (!iUnterarbeitsgang.equals(other.iUnterarbeitsgang))
			return false;

		if (bNurmaschinenzeit == null) {
			if (other.bNurmaschinenzeit != null)
				return false;
		} else if (!bNurmaschinenzeit.equals(other.bNurmaschinenzeit))
			return false;

		if (iMaschinenversatztage == null) {
			if (other.iMaschinenversatztage != null)
				return false;
		} else if (!iMaschinenversatztage.equals(other.iMaschinenversatztage))
			return false;

		if (apkommentarIId == null) {
			if (other.apkommentarIId != null)
				return false;
		} else if (!apkommentarIId.equals(other.apkommentarIId))
			return false;

		if (nPpm == null) {
			if (other.nPpm != null)
				return false;
		} else if (!nPpm.equals(other.nPpm))
			return false;

		if (xFormel == null) {
			if (other.xFormel != null)
				return false;
		} else if (!xFormel.equals(other.xFormel))
			return false;
		return true;
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
		// if (!(that.artikelIId == null ? this.artikelIId == null
		// : that.artikelIId.equals(this.artikelIId)))
		// return false;
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
		// result = 37 * result + this.artikelIId.hashCode();
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
		// returnString += ", " + artikelIId;
		returnString += ", " + lStueckzeit;
		returnString += ", " + lRuestzeit;
		returnString += ", " + cKommentar;
		returnString += ", " + xLangtext;
		return returnString;
	}
}
