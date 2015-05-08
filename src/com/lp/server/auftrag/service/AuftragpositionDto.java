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
package com.lp.server.auftrag.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.service.BelegpositionVerkaufDto;

public class AuftragpositionDto extends BelegpositionVerkaufDto implements
		Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String auftragpositionstatusCNr;
	private BigDecimal nOffeneMenge;
	private BigDecimal nOffeneRahmenMenge;
	

	private Timestamp tUebersteuerbarerLiefertermin;
	private Short bDrucken;
	private Integer auftragpositionIIdRahmenposition;
	private String cSeriennrchargennr;
	private String[] cSeriennrn;
	private BigDecimal nEinkaufpreis;

	public BigDecimal getNOffeneMenge() {
		return nOffeneMenge;
	}

	public void setNOffeneMenge(BigDecimal nOffeneMenge) {
		this.nOffeneMenge = nOffeneMenge;
	}

	public BigDecimal getNOffeneRahmenMenge() {
		return nOffeneRahmenMenge;
	}

	public void setNOffeneRahmenMenge(BigDecimal nOffeneRahmenMenge) {
		this.nOffeneRahmenMenge = nOffeneRahmenMenge;
	}


	public BigDecimal getBdEinkaufpreis() {
		return nEinkaufpreis;
	}

	public void setBdEinkaufpreis(BigDecimal bdEinkaufpreis) {
		this.nEinkaufpreis = bdEinkaufpreis;
	}

	public Timestamp getTUebersteuerbarerLiefertermin() {
		return this.tUebersteuerbarerLiefertermin;
	}

	public void setTUebersteuerbarerLiefertermin(Timestamp pDate) {
		this.tUebersteuerbarerLiefertermin = pDate;
	}

	public Short getBDrucken() {
		return bDrucken;
	}

	public void setBDrucken(Short bDrucken) {
		this.bDrucken = bDrucken;
	}

	public Integer getAuftragpositionIIdRahmenposition() {
		return this.auftragpositionIIdRahmenposition;
	}

	public void setAuftragpositionIIdRahmenposition(
			Integer auftragpositionIIdRahmenposition) {
		this.auftragpositionIIdRahmenposition = auftragpositionIIdRahmenposition;
	}

	public String getAuftragpositionstatusCNr() {
		return auftragpositionstatusCNr;
	}

	public String getCSeriennrchargennr() {
		return cSeriennrchargennr;
	}

	public String[] getCSeriennrn() {
		return cSeriennrn;
	}

	public void setAuftragpositionstatusCNr(String auftragpositionstatusCNr) {
		this.auftragpositionstatusCNr = auftragpositionstatusCNr;
	}

	public void setCSeriennrchargennr(String cSeriennrchargennr) {
		this.cSeriennrchargennr = cSeriennrchargennr;
	}

	public void setCSeriennrn(String[] cSeriennrn) {
		this.cSeriennrn = cSeriennrn;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof AuftragpositionDto)) {
			return false;
		}
		AuftragpositionDto that = (AuftragpositionDto) obj;
		if (!(that.getIId() == null ? this.getIId() == null : that.getIId()
				.equals(this.getIId()))) {
			return false;
		}
		if (!(that.getBelegIId() == null ? this.getBelegIId() == null : that
				.getBelegIId().equals(this.getBelegIId()))) {
			return false;
		}
		if (!(that.getISort() == null ? this.getISort() == null : that
				.getISort().equals(this.getISort()))) {
			return false;
		}
		if (!(that.getPositionsartCNr() == null ? this.getPositionsartCNr() == null
				: that.getPositionsartCNr().equals(this.getPositionsartCNr()))) {
			return false;
		}
		if (!(that.getArtikelIId() == null ? this.getArtikelIId() == null
				: that.getArtikelIId().equals(this.getArtikelIId()))) {
			return false;
		}
		if (!(that.getCBez() == null ? this.getCBez() == null : that.getCBez()
				.equals(this.getCBez()))) {
			return false;
		}
		if (!(that.getCZusatzbez() == null ? this.getCZusatzbez() == null
				: that.getCZusatzbez().equals(this.getCZusatzbez()))) {
			return false;
		}
		if (!(that.getNMenge() == null ? this.getNMenge() == null : that
				.getNMenge().equals(this.getNMenge()))) {
			return false;
		}
		if (!(that.getEinheitCNr() == null ? this.getEinheitCNr() == null
				: that.getEinheitCNr().equals(this.getEinheitCNr()))) {
			return false;
		}
		if (!(that.getFRabattsatz() == null ? this.getFRabattsatz() == null
				: that.getFRabattsatz().equals(this.getFRabattsatz()))) {
			return false;
		}
		if (!(that.getMwstsatzIId() == null ? this.getMwstsatzIId() == null
				: that.getMwstsatzIId().equals(this.getMwstsatzIId()))) {
			return false;
		}
		if (!(that.bDrucken == null ? this.bDrucken == null : that.bDrucken
				.equals(this.bDrucken))) {
			return false;
		}
		if (!(that.getBArtikelbezeichnunguebersteuert() == null ? this
				.getBArtikelbezeichnunguebersteuert() == null : that
				.getBArtikelbezeichnunguebersteuert().equals(
						this.getBArtikelbezeichnunguebersteuert()))) {
			return false;
		}
		if (!(that.getXTextinhalt() == null ? this.getXTextinhalt() == null
				: that.getXTextinhalt().equals(this.getXTextinhalt()))) {
			return false;
		}
		if (!(that.tUebersteuerbarerLiefertermin == null ? this.tUebersteuerbarerLiefertermin == null
				: that.tUebersteuerbarerLiefertermin
						.equals(this.tUebersteuerbarerLiefertermin))) {
			return false;
		}
		if (!(that.getBRabattsatzuebersteuert() == null ? this
				.getBRabattsatzuebersteuert() == null : that
				.getBRabattsatzuebersteuert().equals(
						this.getBRabattsatzuebersteuert()))) {
			return false;
		}
		if (!(that.getBMwstsatzuebersteuert() == null ? this
				.getBMwstsatzuebersteuert() == null : that
				.getBMwstsatzuebersteuert().equals(
						this.getBMwstsatzuebersteuert()))) {
			return false;
		}
		if (!(that.nOffeneMenge == null ? this.nOffeneMenge == null
				: that.nOffeneMenge.equals(this.nOffeneMenge))) {
			return false;
		}
		if (!(that.nOffeneRahmenMenge == null ? this.nOffeneRahmenMenge == null
				: that.nOffeneRahmenMenge.equals(this.nOffeneRahmenMenge))) {
			return false;
		}
		if (!(that.auftragpositionstatusCNr == null ? this.auftragpositionstatusCNr == null
				: that.auftragpositionstatusCNr
						.equals(this.auftragpositionstatusCNr))) {
			return false;
		}
		if (!(that.cSeriennrchargennr == null ? this.cSeriennrchargennr == null
				: that.cSeriennrchargennr.equals(this.cSeriennrchargennr))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.getIId().hashCode();
		result = 37 * result + this.getBelegIId().hashCode();
		result = 37 * result + this.getISort().hashCode();
		result = 37 * result + this.getPositionsartCNr().hashCode();
		result = 37 * result + this.getArtikelIId().hashCode();
		result = 37 * result + this.getCBez().hashCode();
		result = 37 * result + this.getNMenge().hashCode();
		result = 37 * result + this.getEinheitCNr().hashCode();
		result = 37 * result + this.getFRabattsatz().hashCode();
		result = 37 * result + this.getMwstsatzIId().hashCode();
		result = 37 * result + this.bDrucken.hashCode();
		result = 37 * result
				+ this.getBArtikelbezeichnunguebersteuert().hashCode();
		result = 37 * result + this.getCZusatzbez().hashCode();
		result = 37 * result + this.getXTextinhalt().hashCode();
		result = 37 * result + this.tUebersteuerbarerLiefertermin.hashCode();
		result = 37 * result + this.getBRabattsatzuebersteuert().hashCode();
		result = 37 * result + this.getBMwstsatzuebersteuert().hashCode();
		result = 37 * result + this.nOffeneMenge.hashCode();
		result = 37 * result + this.nOffeneRahmenMenge.hashCode();
		result = 37 * result + this.auftragpositionstatusCNr.hashCode();
		result = 37 * result + this.cSeriennrchargennr.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += getIId();
		returnString += ", " + getBelegIId();
		returnString += ", " + getISort();
		returnString += ", " + getPositionsartCNr();
		returnString += ", " + getArtikelIId();
		returnString += ", " + getCBez();
		returnString += ", " + getNMenge();
		returnString += ", " + getEinheitCNr();
		returnString += ", " + getFRabattsatz();
		returnString += ", " + getMwstsatzIId();
		returnString += ", " + bDrucken;
		returnString += ", " + getBArtikelbezeichnunguebersteuert();
		returnString += ", " + getCZusatzbez();
		returnString += ", " + getXTextinhalt();
		returnString += ", " + tUebersteuerbarerLiefertermin;
		returnString += ", " + getBRabattsatzuebersteuert();
		returnString += ", " + getBMwstsatzuebersteuert();
		returnString += ", " + nOffeneMenge;
		returnString += ", " + nOffeneRahmenMenge;
		returnString += ", " + auftragpositionstatusCNr;
		returnString += ", " + cSeriennrchargennr;
		return returnString;
	}

	public Object clone() {
		AuftragpositionDto auftragpositionDto = new AuftragpositionDto();

		// iId null
		// auftragIId null
		auftragpositionDto.setISort(this.getISort());
		auftragpositionDto.setPositionsartCNr(this.getPositionsartCNr());
		auftragpositionDto.auftragpositionstatusCNr = AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN;
		auftragpositionDto.setArtikelIId(this.getArtikelIId());
		auftragpositionDto.setCBez(this.getCBez());
		auftragpositionDto.setBArtikelbezeichnunguebersteuert(this
				.getBArtikelbezeichnunguebersteuert());
		auftragpositionDto.setCZusatzbez(this.getCZusatzbez());
		auftragpositionDto.setXTextinhalt(this.getXTextinhalt());
		auftragpositionDto.setMediastandardIId(this.getMediastandardIId());
		auftragpositionDto.setNMenge(this.getNMenge());
		auftragpositionDto.nOffeneMenge = this.getNMenge(); // !!!
		auftragpositionDto.setEinheitCNr(this.getEinheitCNr());
		auftragpositionDto.setFRabattsatz(this.getFRabattsatz());
		auftragpositionDto.setBRabattsatzuebersteuert(this
				.getBRabattsatzuebersteuert());
		auftragpositionDto.setFZusatzrabattsatz(this.getFZusatzrabattsatz());
		auftragpositionDto.setMwstsatzIId(this.getMwstsatzIId());
		auftragpositionDto.setBMwstsatzuebersteuert(this
				.getBMwstsatzuebersteuert());
		auftragpositionDto.setBNettopreisuebersteuert(this
				.getBNettopreisuebersteuert());
		auftragpositionDto.setNEinzelpreis(this.getNEinzelpreis());
		auftragpositionDto.setKostentraegerIId(this.getKostentraegerIId());
		auftragpositionDto.setCLvposition(this.getCLvposition());
		auftragpositionDto.setNEinzelpreisplusversteckteraufschlag(this
				.getNEinzelpreisplusversteckteraufschlag());
		auftragpositionDto.setNRabattbetrag(this.getNRabattbetrag());
		auftragpositionDto.setNNettoeinzelpreis(this.getNNettoeinzelpreis());
		auftragpositionDto.setNNettoeinzelpreisplusversteckteraufschlag(this
				.getNNettoeinzelpreisplusversteckteraufschlag());
		auftragpositionDto
				.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(this
						.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());
		auftragpositionDto.setNMwstbetrag(this.getNMwstbetrag());
		auftragpositionDto.setNBruttoeinzelpreis(this.getNBruttoeinzelpreis());
		auftragpositionDto.tUebersteuerbarerLiefertermin = this.tUebersteuerbarerLiefertermin;
		auftragpositionDto.bDrucken = this.bDrucken;
		auftragpositionDto.setTypCNr(this.getTypCNr());
		auftragpositionDto.setLieferantIId(this.getLieferantIId());
		return auftragpositionDto;
	}

	/**
	 * @deprecated MB -> muss in die BelegkonvertierungFacBean
	 * 
	 * @return LieferscheinpositionDto
	 * @throws CloneNotSupportedException
	 */
	public LieferscheinpositionDto cloneAsLieferscheinpositionDto() {
		LieferscheinpositionDto lieferscheinpositionDto = new LieferscheinpositionDto();

		// iId null
		// lieferscheinIId null
		lieferscheinpositionDto.setISort(this.getISort());
		lieferscheinpositionDto.setLieferscheinpositionartCNr(this
				.getPositionsartCNr());
		lieferscheinpositionDto.setArtikelIId(this.getArtikelIId());
		// Serien- Chargennummer aus UI
		lieferscheinpositionDto.setCBez(this.getCBez());
		lieferscheinpositionDto.setCZusatzbez(this.getCZusatzbez());
		lieferscheinpositionDto.setBArtikelbezeichnunguebersteuert(this
				.getBArtikelbezeichnunguebersteuert());
		// cZusatzbezeichnung: dieses Feld gibt es nicht auf der DB
		lieferscheinpositionDto.setXTextinhalt(this.getXTextinhalt());
		lieferscheinpositionDto.setMediastandardIId(this.getMediastandardIId());
		lieferscheinpositionDto.setNMenge(this.getNMenge());
		lieferscheinpositionDto.setEinheitCNr(this.getEinheitCNr());
		lieferscheinpositionDto.setFRabattsatz(this.getFRabattsatz());
		lieferscheinpositionDto.setBRabattsatzuebersteuert(this
				.getBRabattsatzuebersteuert());
		lieferscheinpositionDto.setFZusatzrabattsatz(this
				.getFZusatzrabattsatz());
		lieferscheinpositionDto.setMwstsatzIId(this.getMwstsatzIId());
		lieferscheinpositionDto.setBMwstsatzuebersteuert(this
				.getBMwstsatzuebersteuert());
		lieferscheinpositionDto.setBNettopreisuebersteuert(this
				.getBNettopreisuebersteuert());
		lieferscheinpositionDto.setNEinzelpreis(this.getNEinzelpreis());
		lieferscheinpositionDto.setNEinzelpreis(this
				.getNEinzelpreisplusversteckteraufschlag());
		lieferscheinpositionDto.setNRabattbetrag(this.getNRabattbetrag());
		lieferscheinpositionDto.setNNettoeinzelpreis(this
				.getNNettoeinzelpreis());
		lieferscheinpositionDto.setNMaterialzuschlag(this
				.getNMaterialzuschlag());
		lieferscheinpositionDto.setNMaterialzuschlagKurs(this.getNMaterialzuschlagKurs());
		lieferscheinpositionDto.setTMaterialzuschlagDatum(this.getTMaterialzuschlagDatum());
		lieferscheinpositionDto
				.setNNettoeinzelpreisplusversteckteraufschlag(this
						.getNNettoeinzelpreisplusversteckteraufschlag());
		lieferscheinpositionDto
				.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(this
						.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());
		lieferscheinpositionDto.setNMwstbetrag(this.getNMwstbetrag());
		lieferscheinpositionDto.setNBruttoeinzelpreis(this
				.getNBruttoeinzelpreis());
		lieferscheinpositionDto.setAuftragpositionIId(this.getIId());
		lieferscheinpositionDto.setTypCNr(this.getTypCNr());
		lieferscheinpositionDto.setVerleihIId(this.getVerleihIId());

		return lieferscheinpositionDto;
	}

	/**
	 * Handelt es sich um eine Ident-Position?
	 * @return true wenn es eine Artikelposition ist
	 */
	public boolean isIdent() {
		return AuftragServiceFac.AUFTRAGPOSITIONART_IDENT.equalsIgnoreCase(getPositionsartCNr()) ;
	}
	
	/**
	 * Handelt es sich um eine Handeingabe?
	 * @return true wenn es sich um eine Handeingabe handelt
	 */
	public boolean isHandeingabe() {
		return AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE.equalsIgnoreCase(getPositionsartCNr()) ;
	}
	
	/**
	 * Handelt es sich um eine Position?
	 * @return true wenn es eine "POSITION" ist
	 */
	public boolean isPosition() {
		return AuftragServiceFac.AUFTRAGPOSITIONART_POSITION.equalsIgnoreCase(getPositionsartCNr()) ;		
	}
	
	/**
	 * Handelt es sich um eine Intelligente Zwischensumme?
	 * @return true wenn es eine intelligente Zwischensumme ist
	 */
	public boolean isIntelligenteZwischensumme() {
		return AuftragServiceFac.AUFTRAGPOSITIONART_INTELLIGENTE_ZWISCHENSUMME
				.equalsIgnoreCase(getPositionsartCNr()) ;	
	}
}
