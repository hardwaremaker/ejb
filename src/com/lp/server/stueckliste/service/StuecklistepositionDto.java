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

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.stueckliste.ejb.Montageart;
import com.lp.server.system.service.HvDtoLogClass;
import com.lp.server.system.service.HvDtoLogIdCBez;
import com.lp.server.system.service.HvDtoLogIgnore;
import com.lp.service.BelegpositionDto;

@HvDtoLogClass(name=HvDtoLogClass.STUECKLISTE_POSITION, filtername=HvDtoLogClass.STUECKLISTE)
public class StuecklistepositionDto extends BelegpositionDto implements
		Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArtikelDto artikelDto;
	private MontageartDto montageartDto;
	private Float fDimension1;
	private Float fDimension2;
	private Float fDimension3;
	private String cPosition;
	private BigDecimal nKalkpreis;

	// nicht in DB
	private BigDecimal nZielmenge;

	private String sHandeingabe;
	private String cKommentar;
	private Integer montageartIId;
	private Integer iLfdnummer;

	
	private Integer iBeginnterminoffset;

	public Integer getIBeginnterminoffset() {
		return iBeginnterminoffset;
	}

	public void setIBeginnterminoffset(Integer iBeginnterminoffset) {
		this.iBeginnterminoffset = iBeginnterminoffset;
	}
	
	private Short bMitdrucken;
	private Integer personalIIdAnlegen;
	private Integer personalIIdAendern;
	private Timestamp tAendern;
	private Timestamp tAnlegen;

	public Integer getStuecklisteIId() {
		return super.getBelegIId();
	}

	public void setStuecklisteIId(Integer stuecklisteIId) {
		super.setBelegIId(stuecklisteIId);
	}

	public Float getFDimension1() {
		return fDimension1;
	}

	public void setFDimension1(Float fDimension1) {
		this.fDimension1 = fDimension1;
	}

	public Float getFDimension2() {
		return fDimension2;
	}

	public void setFDimension2(Float fDimension2) {
		this.fDimension2 = fDimension2;
	}

	public Float getFDimension3() {
		return fDimension3;
	}

	public void setFDimension3(Float fDimension3) {
		this.fDimension3 = fDimension3;
	}

	public String getCPosition() {
		return cPosition;
	}

	public void setCPosition(String cPosition) {
		this.cPosition = cPosition;
	}

	public String getCKommentar() {
		return cKommentar;
	}

	public void setCKommentar(String cKommentar) {
		this.cKommentar = cKommentar;
	}

	@HvDtoLogIdCBez(entityClass = Montageart.class)
	public Integer getMontageartIId() {
		return montageartIId;
	}

	public void setMontageartIId(Integer montageartIId) {
		this.montageartIId = montageartIId;
	}

	public Integer getILfdnummer() {
		return iLfdnummer;
	}

	public void setILfdnummer(Integer iLfdnummer) {
		this.iLfdnummer = iLfdnummer;
	}

	@HvDtoLogIgnore
	public ArtikelDto getArtikelDto() {
		return artikelDto;
	}

	@HvDtoLogIgnore
	public MontageartDto getMontageartDto() {
		return montageartDto;
	}

	// public String getPositionsartCNr() {
	// return super.getPositionsartCNr();
	// }

	public String getSHandeingabe() {
		return sHandeingabe;
	}

	public Short getBMitdrucken() {
		return bMitdrucken;
	}

	public BigDecimal getNZielmenge() {
		return nZielmenge;
	}

	public BigDecimal getNKalkpreis() {
		return nKalkpreis;
	}

	@HvDtoLogIgnore
	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	@HvDtoLogIgnore
	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setArtikelDto(ArtikelDto artikelDto) {
		this.artikelDto = artikelDto;
	}

	public void setMontageartDto(MontageartDto montageartDto) {
		this.montageartDto = montageartDto;
	}

	// public void setPositionsartCNr(String positionsartCNr) {
	// super.setPositionsartCNr(positionsartCNr);
	// }

	public void setSHandeingabe(String sHandeingabe) {
		this.sHandeingabe = sHandeingabe;
	}

	public void setBMitdrucken(Short bMitdrucken) {
		this.bMitdrucken = bMitdrucken;
	}

	public void setNZielmenge(BigDecimal nZielmenge) {
		this.nZielmenge = nZielmenge;
	}

	public void setNKalkpreis(BigDecimal nKalkpreis) {
		this.nKalkpreis = nKalkpreis;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof StuecklistepositionDto))
			return false;
		StuecklistepositionDto that = (StuecklistepositionDto) obj;
		if (!(that.getIId() == null ? this.getIId() == null : that.getIId()
				.equals(this.getIId())))
			return false;
		if (!(that.getBelegIId() == null ? this.getBelegIId() == null : that
				.getBelegIId().equals(this.getBelegIId())))
			return false;
		if (!(that.getArtikelIId() == null ? this.getArtikelIId() == null
				: that.getArtikelIId().equals(this.getArtikelIId())))
			return false;
		if (!(that.getNMenge() == null ? this.getNMenge() == null : that
				.getNMenge().equals(this.getNMenge())))
			return false;
		if (!(that.getEinheitCNr() == null ? this.getEinheitCNr() == null
				: that.getEinheitCNr().equals(this.getEinheitCNr())))
			return false;
		if (!(that.fDimension1 == null ? this.fDimension1 == null
				: that.fDimension1.equals(this.fDimension1)))
			return false;
		if (!(that.fDimension2 == null ? this.fDimension2 == null
				: that.fDimension2.equals(this.fDimension2)))
			return false;
		if (!(that.fDimension3 == null ? this.fDimension3 == null
				: that.fDimension3.equals(this.fDimension3)))
			return false;
		if (!(that.cPosition == null ? this.cPosition == null : that.cPosition
				.equals(this.cPosition)))
			return false;
		if (!(that.cKommentar == null ? this.cKommentar == null
				: that.cKommentar.equals(this.cKommentar)))
			return false;
		if (!(that.montageartIId == null ? this.montageartIId == null
				: that.montageartIId.equals(this.montageartIId)))
			return false;
		if (!(that.iLfdnummer == null ? this.iLfdnummer == null
				: that.iLfdnummer.equals(this.iLfdnummer)))
			return false;
		if (!(that.getISort() == null ? this.getISort() == null : that
				.getISort().equals(this.getISort())))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.getIId().hashCode();
		result = 37 * result + this.getBelegIId().hashCode();
		result = 37 * result + this.getArtikelIId().hashCode();
		result = 37 * result + this.getNMenge().hashCode();
		result = 37 * result + this.getEinheitCNr().hashCode();
		result = 37 * result + this.fDimension1.hashCode();
		result = 37 * result + this.fDimension2.hashCode();
		result = 37 * result + this.fDimension3.hashCode();
		result = 37 * result + this.cPosition.hashCode();
		result = 37 * result + this.cKommentar.hashCode();
		result = 37 * result + this.montageartIId.hashCode();
		result = 37 * result + this.iLfdnummer.hashCode();
		result = 37 * result + this.getISort().hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += getIId();
		returnString += ", " + getStuecklisteIId();
		returnString += ", " + getArtikelIId();
		returnString += ", " + getNMenge();
		returnString += ", " + getEinheitCNr();
		returnString += ", " + fDimension1;
		returnString += ", " + fDimension2;
		returnString += ", " + fDimension3;
		returnString += ", " + cPosition;
		returnString += ", " + cKommentar;
		returnString += ", " + montageartIId;
		returnString += ", " + iLfdnummer;
		returnString += ", " + getISort();
		return returnString;
	}
}
