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
package com.lp.server.lieferschein.service;

import java.io.Serializable;

import com.lp.service.BelegpositionVerkaufDto;

public class LieferscheinpositionDto extends BelegpositionVerkaufDto implements
		Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer auftragpositionIId;
	private Double fKupferzuschlag;
	
	
	private Integer positioniId = null;
	private String typCNr = null;

	public Integer getLieferscheinIId() {
		return getBelegIId();
	}

	public void setLieferscheinIId(Integer lieferscheinIId) {
		setBelegIId(lieferscheinIId);
	}

	public String getLieferscheinpositionartCNr() {
		return getPositionsartCNr();
	}

	public void setLieferscheinpositionartCNr(String lieferscheinpositionartCNr) {
		setPositionsartCNr(lieferscheinpositionartCNr);
	}

	public Integer getAuftragpositionIId() {
		return auftragpositionIId;
	}

	public void setAuftragpositionIId(Integer auftragpositionIId) {
		this.auftragpositionIId = auftragpositionIId;
	}

	public Double getFKupferzuschlag() {
		return fKupferzuschlag;
	}

	public void setFKupferzuschlag(Double fKupferzuschlag) {
		this.fKupferzuschlag = fKupferzuschlag;
	}
	

	
	public String getTypCNr() {
		return typCNr;
	}

	public Integer getPositioniId() {
		return positioniId;
	}

	public void setTypCNr(String typCNr) {
		this.typCNr = typCNr;
	}

	public void setPositioniId(Integer positioniId) {
		this.positioniId = positioniId;
	}

	private Short bKeinlieferrest;

	public Short getBKeinlieferrest() {
		return bKeinlieferrest;
	}

	public void setBKeinlieferrest(Short bKeinlieferrest) {
		this.bKeinlieferrest = bKeinlieferrest;
	}

	private Integer lagerIId;

	public Integer getLagerIId() {
		return lagerIId;
	}

	public void setLagerIId(Integer lagerIId) {
		this.lagerIId = lagerIId;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof LieferscheinpositionDto))
			return false;
		LieferscheinpositionDto that = (LieferscheinpositionDto) obj;
		if (!(that.getIId() == null ? this.getIId() == null : that.getIId()
				.equals(this.getIId())))
			return false;
		if (!(that.getLieferscheinIId() == null ? this.getLieferscheinIId() == null
				: that.getLieferscheinIId().equals(this.getLieferscheinIId())))
			return false;
		if (!(that.getISort() == null ? this.getISort() == null : that
				.getISort().equals(this.getISort())))
			return false;
		if (!(that.getLieferscheinpositionartCNr() == null ? this
				.getLieferscheinpositionartCNr() == null : that
				.getLieferscheinpositionartCNr().equals(
						this.getLieferscheinpositionartCNr())))
			return false;
		if (!(that.getArtikelIId() == null ? this.getArtikelIId() == null
				: that.getArtikelIId().equals(this.getArtikelIId())))
			return false;
		if (!(that.getCBez() == null ? this.getCBez() == null : that.getCBez()
				.equals(this.getCBez())))
			return false;
		if (!(that.getBArtikelbezeichnunguebersteuert() == null ? this
				.getBArtikelbezeichnunguebersteuert() == null : that
				.getBArtikelbezeichnunguebersteuert().equals(
						this.getBArtikelbezeichnunguebersteuert())))
			return false;
		if (!(that.getCZusatzbez() == null ? this.getCZusatzbez() == null
				: that.getCZusatzbez().equals(this.getCZusatzbez())))
			return false;
		if (!(that.getXTextinhalt() == null ? this.getXTextinhalt() == null
				: that.getXTextinhalt().equals(this.getXTextinhalt())))
			return false;
		if (!(that.auftragpositionIId == null ? this.auftragpositionIId == null
				: that.auftragpositionIId.equals(this.auftragpositionIId)))
			return false;
		if (!(that.getNMenge() == null ? this.getNMenge() == null : that
				.getNMenge().equals(this.getNMenge())))
			return false;
		if (!(that.getEinheitCNr() == null ? this.getEinheitCNr() == null
				: that.getEinheitCNr().equals(this.getEinheitCNr())))
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
		if (!(that.nRabattbetrag == null ? this.nRabattbetrag == null
				: that.nRabattbetrag.equals(this.nRabattbetrag)))
			return false;
		if (!(that.getNNettoeinzelpreis() == null ? this.getNNettoeinzelpreis() == null
				: that.getNNettoeinzelpreis().equals(
						this.getNNettoeinzelpreis())))
			return false;
		if (!(that.nMwstbetrag == null ? this.nMwstbetrag == null
				: that.nMwstbetrag.equals(this.nMwstbetrag)))
			return false;
		if (!(that.getNBruttoeinzelpreis() == null ? this
				.getNBruttoeinzelpreis() == null : that.getNBruttoeinzelpreis()
				.equals(this.getNBruttoeinzelpreis())))
			return false;
		if (!(that.fKupferzuschlag == null ? this.fKupferzuschlag == null
				: that.fKupferzuschlag.equals(this.fKupferzuschlag)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.getIId().hashCode();
		result = 37 * result + this.getLieferscheinIId().hashCode();
		result = 37 * result + this.getISort().hashCode();
		result = 37 * result + this.getLieferscheinpositionartCNr().hashCode();
		result = 37 * result + this.getArtikelIId().hashCode();
		result = 37 * result + this.getCBez().hashCode();
		result = 37 * result
				+ this.getBArtikelbezeichnunguebersteuert().hashCode();
		result = 37 * result + this.getXTextinhalt().hashCode();
		result = 37 * result + this.auftragpositionIId.hashCode();
		result = 37 * result + this.getNMenge().hashCode();
		result = 37 * result + this.getEinheitCNr().hashCode();
		result = 37 * result + this.getFRabattsatz().hashCode();
		result = 37 * result + this.getBRabattsatzuebersteuert().hashCode();
		result = 37 * result + this.getMwstsatzIId().hashCode();
		result = 37 * result + this.getBMwstsatzuebersteuert().hashCode();
		result = 37 * result + this.getNEinzelpreis().hashCode();
		result = 37 * result + this.nRabattbetrag.hashCode();
		result = 37 * result + this.getNNettoeinzelpreis().hashCode();
		result = 37 * result + this.nMwstbetrag.hashCode();
		result = 37 * result + this.getNBruttoeinzelpreis().hashCode();
		result = 37 * result + this.fKupferzuschlag.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += getIId();
		returnString += ", " + getLieferscheinIId();
		returnString += ", " + getISort();
		returnString += ", " + getLieferscheinpositionartCNr();
		returnString += ", " + getArtikelIId();
		returnString += ", " + getCBez();
		returnString += ", " + getBArtikelbezeichnunguebersteuert();
		returnString += ", " + getCZusatzbez();
		returnString += ", " + getXTextinhalt();
		returnString += ", " + auftragpositionIId;
		returnString += ", " + getNMenge();
		returnString += ", " + getEinheitCNr();
		returnString += ", " + getFRabattsatz();
		returnString += ", " + getBRabattsatzuebersteuert();
		returnString += ", " + getMwstsatzIId();
		returnString += ", " + getBMwstsatzuebersteuert();
		returnString += ", " + getNEinzelpreis();
		returnString += ", " + nRabattbetrag;
		returnString += ", " + getNNettoeinzelpreis();
		returnString += ", " + nMwstbetrag;
		returnString += ", " + getNBruttoeinzelpreis();
		returnString += ", " + fKupferzuschlag;
		return returnString;
	}
}
