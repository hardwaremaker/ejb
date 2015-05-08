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

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.partner.ejb.Ansprechpartner;
import com.lp.server.stueckliste.ejb.Montageart;
import com.lp.server.system.service.HvDtoLogAlways;
import com.lp.server.system.service.HvDtoLogClass;
import com.lp.server.system.service.HvDtoLogIdCBez;
import com.lp.server.system.service.HvDtoLogIdEmail;
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
	
	private Double fLagermindeststandAusKopfartikel=null;

	public Double getfLagermindeststandAusKopfartikel() {
		return fLagermindeststandAusKopfartikel;
	}

	public void setfLagermindeststandAusKopfartikel(
			Double fLagermindeststandAusKopfartikel) {
		this.fLagermindeststandAusKopfartikel = fLagermindeststandAusKopfartikel;
	}

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

	private Timestamp tAendernAnsprechpartner ;
	private Timestamp tAnlegenAnsprechpartner ;
	private Integer ansprechpartnerIIdAnlegen ;
	private Integer ansprechpartnerIIdAendern ;
	
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

	public Timestamp getTAendernAnsprechpartner() {
		return tAendernAnsprechpartner;
	}

	public void setTAendernAnsprechpartner(Timestamp tAendernAnsprechpartner) {
		this.tAendernAnsprechpartner = tAendernAnsprechpartner;
	}

	@HvDtoLogIdEmail(entityClass=Ansprechpartner.class)
	@HvDtoLogAlways
	public Integer getAnsprechpartnerIIdAnlegen() {
		return ansprechpartnerIIdAnlegen;
	}

	public void setAnsprechpartnerIIdAnlegen(Integer ansprechpartnerIIdAnlegen) {
		this.ansprechpartnerIIdAnlegen = ansprechpartnerIIdAnlegen;
	}

	@HvDtoLogIdEmail(entityClass=Ansprechpartner.class)
	@HvDtoLogAlways
	public Integer getAnsprechpartnerIIdAendern() {
		return ansprechpartnerIIdAendern;
	}

	public void setAnsprechpartnerIIdAendern(Integer ansprechpartnerIIdAendern) {
		this.ansprechpartnerIIdAendern = ansprechpartnerIIdAendern;
	}

	public Timestamp getTAnlegenAnsprechpartner() {
		return tAnlegenAnsprechpartner;
	}

	public void setTAnlegenAnsprechpartner(Timestamp tAnlegenAnsprechpartner) {
		this.tAnlegenAnsprechpartner = tAnlegenAnsprechpartner;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		StuecklistepositionDto other = (StuecklistepositionDto) obj;
		if (ansprechpartnerIIdAendern == null) {
			if (other.ansprechpartnerIIdAendern != null)
				return false;
		} else if (!ansprechpartnerIIdAendern
				.equals(other.ansprechpartnerIIdAendern))
			return false;
		if (ansprechpartnerIIdAnlegen == null) {
			if (other.ansprechpartnerIIdAnlegen != null)
				return false;
		} else if (!ansprechpartnerIIdAnlegen
				.equals(other.ansprechpartnerIIdAnlegen))
			return false;
		if (artikelDto == null) {
			if (other.artikelDto != null)
				return false;
		} else if (!artikelDto.equals(other.artikelDto))
			return false;
		if (bMitdrucken == null) {
			if (other.bMitdrucken != null)
				return false;
		} else if (!bMitdrucken.equals(other.bMitdrucken))
			return false;
		if (cKommentar == null) {
			if (other.cKommentar != null)
				return false;
		} else if (!cKommentar.equals(other.cKommentar))
			return false;
		if (cPosition == null) {
			if (other.cPosition != null)
				return false;
		} else if (!cPosition.equals(other.cPosition))
			return false;
		if (fDimension1 == null) {
			if (other.fDimension1 != null)
				return false;
		} else if (!fDimension1.equals(other.fDimension1))
			return false;
		if (fDimension2 == null) {
			if (other.fDimension2 != null)
				return false;
		} else if (!fDimension2.equals(other.fDimension2))
			return false;
		if (fDimension3 == null) {
			if (other.fDimension3 != null)
				return false;
		} else if (!fDimension3.equals(other.fDimension3))
			return false;
		if (fLagermindeststandAusKopfartikel == null) {
			if (other.fLagermindeststandAusKopfartikel != null)
				return false;
		} else if (!fLagermindeststandAusKopfartikel
				.equals(other.fLagermindeststandAusKopfartikel))
			return false;
		if (iBeginnterminoffset == null) {
			if (other.iBeginnterminoffset != null)
				return false;
		} else if (!iBeginnterminoffset.equals(other.iBeginnterminoffset))
			return false;
		if (iLfdnummer == null) {
			if (other.iLfdnummer != null)
				return false;
		} else if (!iLfdnummer.equals(other.iLfdnummer))
			return false;
		if (montageartDto == null) {
			if (other.montageartDto != null)
				return false;
		} else if (!montageartDto.equals(other.montageartDto))
			return false;
		if (montageartIId == null) {
			if (other.montageartIId != null)
				return false;
		} else if (!montageartIId.equals(other.montageartIId))
			return false;
		if (nKalkpreis == null) {
			if (other.nKalkpreis != null)
				return false;
		} else if (!nKalkpreis.equals(other.nKalkpreis))
			return false;
		if (nZielmenge == null) {
			if (other.nZielmenge != null)
				return false;
		} else if (!nZielmenge.equals(other.nZielmenge))
			return false;
		if (personalIIdAendern == null) {
			if (other.personalIIdAendern != null)
				return false;
		} else if (!personalIIdAendern.equals(other.personalIIdAendern))
			return false;
		if (personalIIdAnlegen == null) {
			if (other.personalIIdAnlegen != null)
				return false;
		} else if (!personalIIdAnlegen.equals(other.personalIIdAnlegen))
			return false;
		if (sHandeingabe == null) {
			if (other.sHandeingabe != null)
				return false;
		} else if (!sHandeingabe.equals(other.sHandeingabe))
			return false;
		if (tAendern == null) {
			if (other.tAendern != null)
				return false;
		} else if (!tAendern.equals(other.tAendern))
			return false;
		if (tAendernAnsprechpartner == null) {
			if (other.tAendernAnsprechpartner != null)
				return false;
		} else if (!tAendernAnsprechpartner
				.equals(other.tAendernAnsprechpartner))
			return false;
		if (tAnlegen == null) {
			if (other.tAnlegen != null)
				return false;
		} else if (!tAnlegen.equals(other.tAnlegen))
			return false;
		if (tAnlegenAnsprechpartner == null) {
			if (other.tAnlegenAnsprechpartner != null)
				return false;
		} else if (!tAnlegenAnsprechpartner
				.equals(other.tAnlegenAnsprechpartner))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((ansprechpartnerIIdAendern == null) ? 0
						: ansprechpartnerIIdAendern.hashCode());
		result = prime
				* result
				+ ((ansprechpartnerIIdAnlegen == null) ? 0
						: ansprechpartnerIIdAnlegen.hashCode());
		result = prime * result
				+ ((artikelDto == null) ? 0 : artikelDto.hashCode());
		result = prime * result
				+ ((bMitdrucken == null) ? 0 : bMitdrucken.hashCode());
		result = prime * result
				+ ((cKommentar == null) ? 0 : cKommentar.hashCode());
		result = prime * result
				+ ((cPosition == null) ? 0 : cPosition.hashCode());
		result = prime * result
				+ ((fDimension1 == null) ? 0 : fDimension1.hashCode());
		result = prime * result
				+ ((fDimension2 == null) ? 0 : fDimension2.hashCode());
		result = prime * result
				+ ((fDimension3 == null) ? 0 : fDimension3.hashCode());
		result = prime
				* result
				+ ((fLagermindeststandAusKopfartikel == null) ? 0
						: fLagermindeststandAusKopfartikel.hashCode());
		result = prime
				* result
				+ ((iBeginnterminoffset == null) ? 0 : iBeginnterminoffset
						.hashCode());
		result = prime * result
				+ ((iLfdnummer == null) ? 0 : iLfdnummer.hashCode());
		result = prime * result
				+ ((montageartDto == null) ? 0 : montageartDto.hashCode());
		result = prime * result
				+ ((montageartIId == null) ? 0 : montageartIId.hashCode());
		result = prime * result
				+ ((nKalkpreis == null) ? 0 : nKalkpreis.hashCode());
		result = prime * result
				+ ((nZielmenge == null) ? 0 : nZielmenge.hashCode());
		result = prime
				* result
				+ ((personalIIdAendern == null) ? 0 : personalIIdAendern
						.hashCode());
		result = prime
				* result
				+ ((personalIIdAnlegen == null) ? 0 : personalIIdAnlegen
						.hashCode());
		result = prime * result
				+ ((sHandeingabe == null) ? 0 : sHandeingabe.hashCode());
		result = prime * result
				+ ((tAendern == null) ? 0 : tAendern.hashCode());
		result = prime
				* result
				+ ((tAendernAnsprechpartner == null) ? 0
						: tAendernAnsprechpartner.hashCode());
		result = prime * result
				+ ((tAnlegen == null) ? 0 : tAnlegen.hashCode());
		result = prime
				* result
				+ ((tAnlegenAnsprechpartner == null) ? 0
						: tAnlegenAnsprechpartner.hashCode());
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
