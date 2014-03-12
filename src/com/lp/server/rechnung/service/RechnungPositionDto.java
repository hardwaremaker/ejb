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
package com.lp.server.rechnung.service;

import java.io.Serializable;

import com.lp.service.BelegpositionVerkaufDto;

public class RechnungPositionDto extends BelegpositionVerkaufDto implements
		Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer rechnungIIdGutschrift;
	private Integer lieferscheinIId;
	private Integer rechnungpositionIId;
	private Integer auftragpositionIId;
	private Short bDrucken;
	private Double fKupferzuschlag;

	public RechnungPositionDto(){
		super();
	}
	public RechnungPositionDto(BelegpositionVerkaufDto belegpositionVerkaufDto){
		super();
		this.setFRabattsatz(belegpositionVerkaufDto.getFRabattsatz());
		this.setBRabattsatzuebersteuert(belegpositionVerkaufDto.getBRabattsatzuebersteuert());
		this.setFZusatzrabattsatz(belegpositionVerkaufDto.getFZusatzrabattsatz());
		this.setMwstsatzIId(belegpositionVerkaufDto.getMwstsatzIId());
		this.setBMwstsatzuebersteuert(belegpositionVerkaufDto.getBMwstsatzuebersteuert());
		this.setNEinzelpreis(belegpositionVerkaufDto.getNEinzelpreis());
		this.setNEinzelpreisplusversteckteraufschlag(belegpositionVerkaufDto.getNEinzelpreisplusversteckteraufschlag());
		this.setNNettoeinzelpreis(belegpositionVerkaufDto.getNNettoeinzelpreis());
		this.setNNettoeinzelpreisplusversteckteraufschlag(belegpositionVerkaufDto.getNNettoeinzelpreisplusversteckteraufschlag());
		this.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(belegpositionVerkaufDto.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());
		this.setNBruttoeinzelpreis(belegpositionVerkaufDto.getNBruttoeinzelpreis());
		this.setPositioniId(belegpositionVerkaufDto.getPositioniId());
		this.setTypCNr(belegpositionVerkaufDto.getTypCNr());
		this.setBNettopreisuebersteuert(belegpositionVerkaufDto.getBNettopreisuebersteuert());
		this.setIId(belegpositionVerkaufDto.getIId());
		this.setBelegIId(belegpositionVerkaufDto.getBelegIId());
		this.setISort(belegpositionVerkaufDto.getISort());
		this.setPositionsartCNr(belegpositionVerkaufDto.getPositionsartCNr());
		this.setArtikelIId(belegpositionVerkaufDto.getArtikelIId());
		this.setCBez(belegpositionVerkaufDto.getCBez());
		this.setCZusatzbez(belegpositionVerkaufDto.getCZusatzbez());
		this.setBArtikelbezeichnunguebersteuert(belegpositionVerkaufDto.getBArtikelbezeichnunguebersteuert());
		this.setXTextinhalt(belegpositionVerkaufDto.getXTextinhalt());
		this.setMediastandardIId(belegpositionVerkaufDto.getMediastandardIId());
		this.setNMenge(belegpositionVerkaufDto.getNMenge());
		this.setEinheitCNr(belegpositionVerkaufDto.getEinheitCNr());
		this.setZwsVonPosition(belegpositionVerkaufDto.getZwsVonPosition()) ;
		this.setZwsBisPosition(belegpositionVerkaufDto.getZwsBisPosition()) ;
	}
	
	public Integer getRechnungIId() {
		return super.getBelegIId();
	}

	public void setRechnungIId(Integer rechnungIId) {
		super.setBelegIId(rechnungIId);
	}

	public String getRechnungpositionartCNr() {
		return super.getPositionsartCNr();
	}

	public void setRechnungpositionartCNr(String rechnungpositionartCNr) {
		super.setPositionsartCNr(rechnungpositionartCNr);
	}

	public Integer getRechnungIIdGutschrift() {
		return rechnungIIdGutschrift;
	}

	public void setRechnungIIdGutschrift(Integer rechnungIIdGutschrift) {
		this.rechnungIIdGutschrift = rechnungIIdGutschrift;
	}

	public Integer getLieferscheinIId() {
		return lieferscheinIId;
	}

	public void setLieferscheinIId(Integer lieferscheinIId) {
		this.lieferscheinIId = lieferscheinIId;
	}

	public Integer getRechnungpositionIId() {
		return rechnungpositionIId;
	}

	public void setRechnungpositionIId(Integer rechnungpositionIId) {
		this.rechnungpositionIId = rechnungpositionIId;
	}

	public Integer getAuftragpositionIId() {
		return auftragpositionIId;
	}

	public void setAuftragpositionIId(Integer auftragpositionIId) {
		this.auftragpositionIId = auftragpositionIId;
	}

	public Short getBDrucken() {
		return bDrucken;
	}

	public void setBDrucken(Short bDrucken) {
		this.bDrucken = bDrucken;
	}

	public Double getFKupferzuschlag() {
		return fKupferzuschlag;
	}

	public void setFKupferzuschlag(Double fKupferzuschlag) {
		this.fKupferzuschlag = fKupferzuschlag;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof RechnungPositionDto))
			return false;
		RechnungPositionDto that = (RechnungPositionDto) obj;
		if (!(that.getIId() == null ? this.getIId() == null : that.getIId()
				.equals(this.getIId())))
			return false;
		if (!(that.getBelegIId() == null ? this.getBelegIId() == null : that
				.getBelegIId().equals(this.getBelegIId())))
			return false;
		if (!(that.getPositionsartCNr() == null ? this.getPositionsartCNr() == null
				: that.getPositionsartCNr().equals(this.getPositionsartCNr())))
			return false;
		if (!(that.getISort() == null ? this.getISort() == null : that
				.getISort().equals(this.getISort())))
			return false;
		if (!(that.rechnungIIdGutschrift == null ? this.rechnungIIdGutschrift == null
				: that.rechnungIIdGutschrift.equals(this.rechnungIIdGutschrift)))
			return false;
		if (!(that.lieferscheinIId == null ? this.lieferscheinIId == null
				: that.lieferscheinIId.equals(this.lieferscheinIId)))
			return false;
		if (!(that.rechnungpositionIId == null ? this.rechnungpositionIId == null
				: that.rechnungpositionIId.equals(this.rechnungpositionIId)))
			return false;
		if (!(that.auftragpositionIId == null ? this.auftragpositionIId == null
				: that.auftragpositionIId.equals(this.auftragpositionIId)))
			return false;
		if (!(that.getCBez() == null ? this.getCBez() == null : that.getCBez()
				.equals(this.getCBez())))
			return false;
		if (!(that.getCZusatzbez() == null ? this.getCZusatzbez() == null
				: that.getCZusatzbez().equals(this.getCZusatzbez())))
			return false;
		if (!(that.getXTextinhalt() == null ? this.getXTextinhalt() == null
				: that.getXTextinhalt().equals(this.getXTextinhalt())))
			return false;
		if (!(that.getMediastandardIId() == null ? this.getMediastandardIId() == null
				: that.getMediastandardIId().equals(this.getMediastandardIId())))
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
		if (!(that.bDrucken == null ? this.bDrucken == null : that.bDrucken
				.equals(this.bDrucken)))
			return false;
		if (!(that.fKupferzuschlag == null ? this.fKupferzuschlag == null
				: that.fKupferzuschlag.equals(this.fKupferzuschlag)))
			return false;
		if (!(that.getFRabattsatz() == null ? this.getFRabattsatz() == null
				: that.getFRabattsatz().equals(this.getFRabattsatz())))
			return false;
		if (!(that.getBRabattsatzuebersteuert() == null ? this
				.getBRabattsatzuebersteuert() == null : that
				.getBRabattsatzuebersteuert().equals(
						this.getBRabattsatzuebersteuert())))
			return false;
		if (!(that.getMwstsatzIId() == null ? this.getMwstsatzIId() == null
				: that.getMwstsatzIId().equals(this.getMwstsatzIId())))
			return false;
		if (!(that.getBMwstsatzuebersteuert() == null ? this
				.getBMwstsatzuebersteuert() == null : that
				.getBMwstsatzuebersteuert().equals(
						this.getBMwstsatzuebersteuert())))
			return false;
		if (!(that.getNEinzelpreis() == null ? this.getNEinzelpreis() == null
				: that.getNEinzelpreis().equals(this.getNEinzelpreis())))
			return false;
		if (!(that.getNEinzelpreisplusversteckteraufschlag() == null ? this
				.getNEinzelpreisplusversteckteraufschlag() == null : that
				.getNEinzelpreisplusversteckteraufschlag().equals(
						this.getNEinzelpreisplusversteckteraufschlag())))
			return false;
		if (!(that.getNNettoeinzelpreis() == null ? this.getNNettoeinzelpreis() == null
				: that.getNNettoeinzelpreis().equals(
						this.getNNettoeinzelpreis())))
			return false;
		if (!(that.getNNettoeinzelpreisplusversteckteraufschlag() == null ? this
				.getNNettoeinzelpreisplusversteckteraufschlag() == null
				: that.getNNettoeinzelpreisplusversteckteraufschlag().equals(
						this.getNNettoeinzelpreisplusversteckteraufschlag())))
			return false;
		if (!(that.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte() == null ? this
				.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte() == null
				: that
						.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte()
						.equals(
								this
										.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte())))
			return false;
		if (!(that.getNBruttoeinzelpreis() == null ? this
				.getNBruttoeinzelpreis() == null : that.getNBruttoeinzelpreis()
				.equals(this.getNBruttoeinzelpreis())))
			return false;
		if (!(that.getFZusatzrabattsatz() == null ? this.getFZusatzrabattsatz() == null
				: that.getFZusatzrabattsatz().equals(
						this.getFZusatzrabattsatz())))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.getIId().hashCode();
		result = 37 * result + this.getBelegIId().hashCode();
		result = 37 * result + this.getPositionsartCNr().hashCode();
		result = 37 * result + this.getISort().hashCode();
		result = 37 * result + this.rechnungIIdGutschrift.hashCode();
		result = 37 * result + this.lieferscheinIId.hashCode();
		result = 37 * result + this.rechnungpositionIId.hashCode();
		result = 37 * result + this.auftragpositionIId.hashCode();
		result = 37 * result + this.getCBez().hashCode();
		result = 37 * result + this.getCZusatzbez().hashCode();
		result = 37 * result + this.getXTextinhalt().hashCode();
		result = 37 * result + this.getMediastandardIId().hashCode();
		result = 37 * result + this.getArtikelIId().hashCode();
		result = 37 * result + this.getNMenge().hashCode();
		result = 37 * result + this.getEinheitCNr().hashCode();
		result = 37 * result + this.bDrucken.hashCode();
		result = 37 * result + this.fKupferzuschlag.hashCode();
		result = 37 * result + this.getFRabattsatz().hashCode();
		result = 37 * result + this.getBRabattsatzuebersteuert().hashCode();
		result = 37 * result + this.getMwstsatzIId().hashCode();
		result = 37 * result + this.getBMwstsatzuebersteuert().hashCode();
		result = 37 * result + this.getNEinzelpreis().hashCode();
		result = 37 * result
				+ this.getNEinzelpreisplusversteckteraufschlag().hashCode();
		result = 37 * result + this.getNNettoeinzelpreis().hashCode();
		result = 37
				* result
				+ this.getNNettoeinzelpreisplusversteckteraufschlag()
						.hashCode();
		result = 37
				* result
				+ this
						.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte()
						.hashCode();
		result = 37 * result + this.getNBruttoeinzelpreis().hashCode();
		result = 37 * result + this.getFZusatzrabattsatz().hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += getIId();
		returnString += ", " + getBelegIId();
		returnString += ", " + getPositionsartCNr();
		returnString += ", " + getISort();
		returnString += ", " + rechnungIIdGutschrift;
		returnString += ", " + lieferscheinIId;
		returnString += ", " + rechnungpositionIId;
		returnString += ", " + auftragpositionIId;
		returnString += ", " + getCBez();
		returnString += ", " + getCZusatzbez();
		returnString += ", " + getXTextinhalt();
		returnString += ", " + getMediastandardIId();
		returnString += ", " + getArtikelIId();
		returnString += ", " + getNMenge();
		returnString += ", " + getEinheitCNr();
		returnString += ", " + bDrucken;
		returnString += ", " + fKupferzuschlag;
		returnString += ", " + getFRabattsatz();
		returnString += ", " + getBRabattsatzuebersteuert();
		returnString += ", " + getMwstsatzIId();
		returnString += ", " + getBMwstsatzuebersteuert();
		returnString += ", " + getNEinzelpreis();
		returnString += ", " + getNEinzelpreisplusversteckteraufschlag();
		returnString += ", " + getNNettoeinzelpreis();
		returnString += ", " + getNNettoeinzelpreisplusversteckteraufschlag();
		returnString += ", "
				+ getNNettoeinzelpreisplusversteckteraufschlagminusrabatte();
		returnString += ", " + getNBruttoeinzelpreis();
		returnString += ", " + getFZusatzrabattsatz();
		return returnString;
	}
}
