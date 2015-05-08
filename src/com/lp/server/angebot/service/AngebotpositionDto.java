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
package com.lp.server.angebot.service;

import java.math.BigDecimal;

import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.service.BelegpositionVerkaufDto;
import com.lp.util.Helper;

public class AngebotpositionDto extends BelegpositionVerkaufDto implements
		Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigDecimal nGestehungspreis;

	private Integer agstklIId;
	private BigDecimal nGesamtwertagstklinangebotswaehrung;
	private Short bAlternative;

	public AngebotpositionDto() {
		super();
	}

	public AngebotpositionDto(BelegpositionVerkaufDto belegpositionVerkaufDto) {
		super();
		this.setFRabattsatz(belegpositionVerkaufDto.getFRabattsatz());
		this.setBRabattsatzuebersteuert(belegpositionVerkaufDto
				.getBRabattsatzuebersteuert());
		this.setFZusatzrabattsatz(belegpositionVerkaufDto
				.getFZusatzrabattsatz());
		this.setMwstsatzIId(belegpositionVerkaufDto.getMwstsatzIId());
		this.setBMwstsatzuebersteuert(belegpositionVerkaufDto
				.getBMwstsatzuebersteuert());
		this.setNEinzelpreis(belegpositionVerkaufDto.getNEinzelpreis());
		this.setNEinzelpreisplusversteckteraufschlag(belegpositionVerkaufDto
				.getNEinzelpreisplusversteckteraufschlag());
		this.setNNettoeinzelpreis(belegpositionVerkaufDto
				.getNNettoeinzelpreis());
		this.setNNettoeinzelpreisplusversteckteraufschlag(belegpositionVerkaufDto
				.getNNettoeinzelpreisplusversteckteraufschlag());
		this.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(belegpositionVerkaufDto
				.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());
		this.setNBruttoeinzelpreis(belegpositionVerkaufDto
				.getNBruttoeinzelpreis());
		this.setPositioniId(belegpositionVerkaufDto.getPositioniId());
		this.setTypCNr(belegpositionVerkaufDto.getTypCNr());
		this.setBNettopreisuebersteuert(belegpositionVerkaufDto
				.getBNettopreisuebersteuert());
		this.setIId(belegpositionVerkaufDto.getIId());
		this.setBelegIId(belegpositionVerkaufDto.getBelegIId());
		this.setISort(belegpositionVerkaufDto.getISort());
		this.setPositionsartCNr(belegpositionVerkaufDto.getPositionsartCNr());
		this.setArtikelIId(belegpositionVerkaufDto.getArtikelIId());
		this.setCBez(belegpositionVerkaufDto.getCBez());
		this.setCZusatzbez(belegpositionVerkaufDto.getCZusatzbez());
		this.setBArtikelbezeichnunguebersteuert(belegpositionVerkaufDto
				.getBArtikelbezeichnunguebersteuert());
		this.setXTextinhalt(belegpositionVerkaufDto.getXTextinhalt());
		this.setMediastandardIId(belegpositionVerkaufDto.getMediastandardIId());
		this.setNMenge(belegpositionVerkaufDto.getNMenge());
		this.setEinheitCNr(belegpositionVerkaufDto.getEinheitCNr());
		this.setVerleihIId(belegpositionVerkaufDto.getVerleihIId());
		this.setNMaterialzuschlagKurs(belegpositionVerkaufDto.getNMaterialzuschlagKurs());
		this.setTMaterialzuschlagDatum(belegpositionVerkaufDto.getTMaterialzuschlagDatum());
		
	}

	private BigDecimal nEinkaufpreis;

	public BigDecimal getNEinkaufpreis() {
		return nEinkaufpreis;
	}

	public void setNEinkaufpreis(BigDecimal bdEinkaufpreis) {
		this.nEinkaufpreis = bdEinkaufpreis;
	}

	public BigDecimal getNGestehungspreis() {
		return nGestehungspreis;
	}

	public void setNGestehungspreis(BigDecimal nGestehungspreis) {
		this.nGestehungspreis = nGestehungspreis;
	}

	public Integer getAgstklIId() {
		return agstklIId;
	}

	public void setAgstklIId(Integer agstklIIdI) {
		this.agstklIId = agstklIIdI;
	}

	public BigDecimal getNGesamtwertagstklinangebotswaehrung() {
		return this.nGesamtwertagstklinangebotswaehrung;
	}

	public void setNGesamtwertagstklinangebotswaehrung(
			BigDecimal nGesamtwertagstklinangebotswaehrungI) {
		this.nGesamtwertagstklinangebotswaehrung = nGesamtwertagstklinangebotswaehrungI;
	}

	public Short getBAlternative() {
		return bAlternative;
	}

	public void setBAlternative(Short bAlternative) {
		this.bAlternative = bAlternative;
	}

	public String toString() {
		String returnString = super.toString();
		returnString += ", " + nGestehungspreis;
		returnString += ", " + nRabattbetrag;
		returnString += ", " + nMwstbetrag;
		returnString += ", " + agstklIId;
		returnString += ", " + nGesamtwertagstklinangebotswaehrung;
		return returnString;
	}

	public Object clone() {
		// positionDto = (AngebotpositionDto)
		// super.cloneAsBelegpositionVerkaufDto(new
		// AngebotpositionDto()); UW->JO geht so alles nicht

		AngebotpositionDto positionDto = (AngebotpositionDto) super
				.cloneAsBelegpositionDto(new AngebotpositionDto());

		positionDto.setFRabattsatz(super.getFRabattsatz());
		positionDto.setKostentraegerIId(super.getKostentraegerIId());
		positionDto.setCLvposition(super.getCLvposition());
		positionDto.setBRabattsatzuebersteuert(super
				.getBRabattsatzuebersteuert());
		positionDto.setFZusatzrabattsatz(super.getFZusatzrabattsatz());
		positionDto.setMwstsatzIId(super.getMwstsatzIId());
		positionDto.setBMwstsatzuebersteuert(super.getBMwstsatzuebersteuert());
		positionDto.setBNettopreisuebersteuert(super
				.getBNettopreisuebersteuert());
		positionDto.setNEinzelpreis(super.getNEinzelpreis());
		positionDto.setNEinzelpreisplusversteckteraufschlag(super
				.getNEinzelpreisplusversteckteraufschlag());
		positionDto.setNNettoeinzelpreis(super.getNNettoeinzelpreis());
		positionDto.setNNettoeinzelpreisplusversteckteraufschlag(super
				.getNNettoeinzelpreisplusversteckteraufschlag());
		positionDto
				.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(super
						.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());
		positionDto.setNBruttoeinzelpreis(super.getNBruttoeinzelpreis());
		positionDto.nGestehungspreis = this.nGestehungspreis;
		positionDto.nRabattbetrag = this.nRabattbetrag;
		positionDto.nMwstbetrag = this.nMwstbetrag;
		positionDto.agstklIId = this.agstklIId;
		positionDto.nGesamtwertagstklinangebotswaehrung = this.nGesamtwertagstklinangebotswaehrung;
		positionDto.bAlternative = this.bAlternative;
		positionDto.setLieferantIId(this.getLieferantIId());

		return positionDto;
	}

	private static String[] positionsartMitStatusOffen = new String[] {
			AngebotServiceFac.ANGEBOTPOSITIONART_IDENT,
			AngebotServiceFac.ANGEBOTPOSITIONART_HANDEINGABE,
			AngebotServiceFac.ANGEBOTPOSITIONART_POSITION,
			AngebotServiceFac.ANGEBOTPOSITIONART_INTELLIGENTE_ZWISCHENSUMME };

	/**
	 * @deprecated MB ->BelegpositionkonvertierungFacBean
	 * 
	 * @return AuftragpositionDto
	 * @throws CloneNotSupportedException
	 */
	public AuftragpositionDto cloneAsAuftragposition() {
		AuftragpositionDto auftragpositionDto = new AuftragpositionDto();

		// iId null
		// auftragIId null
		auftragpositionDto.setISort(this.getISort());
		auftragpositionDto.setPositionsartCNr(this.getPositionsartCNr());

		if (Helper.isOneOf(getPositionsartCNr(), positionsartMitStatusOffen)) {
			auftragpositionDto
					.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN);
		}

		// String positionsartCNr = getPositionsartCNr() ;
		// if
		// (positionsartCNr.equals(AngebotServiceFac.ANGEBOTPOSITIONART_IDENT)
		// ||
		// positionsartCNr.equals(AngebotServiceFac.ANGEBOTPOSITIONART_HANDEINGABE)
		// ||
		// positionsartCNr.equals(AngebotServiceFac.ANGEBOTPOSITIONART_POSITION)
		// ||
		// positionsartCNr.equals(AngebotServiceFac.ANGEBOTPOSITIONART_INTELLIGENTE_ZWISCHENSUMME)
		// ) {
		// auftragpositionDto
		// .setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN);
		// }

		auftragpositionDto.setArtikelIId(this.getArtikelIId());
		auftragpositionDto.setCBez(this.getCBez());
		auftragpositionDto.setCZusatzbez(this.getCZusatzbez());
		if (this.getCBez() != null || this.getCZusatzbez() != null)
			auftragpositionDto.setBArtikelbezeichnunguebersteuert(new Short(
					(short) 1)); // UW 27.04.06 Das Feld gibt es im AG nicht
									// mehr
		else
			auftragpositionDto.setBArtikelbezeichnunguebersteuert(new Short(
					(short) 0));

		auftragpositionDto.setXTextinhalt(this.getXTextinhalt());
		auftragpositionDto.setMediastandardIId(this.getMediastandardIId());
		auftragpositionDto.setNMenge(this.getNMenge());
		auftragpositionDto.setNOffeneMenge(this.getNMenge()); // !!!
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
		auftragpositionDto.setNEinzelpreisplusversteckteraufschlag(this
				.getNEinzelpreisplusversteckteraufschlag());
		auftragpositionDto.setKostentraegerIId(this.getKostentraegerIId());
		auftragpositionDto.setCLvposition(this.getCLvposition());
		auftragpositionDto.setNRabattbetrag(this.getNRabattbetrag());
		auftragpositionDto.setNNettoeinzelpreis(this.getNNettoeinzelpreis());
		auftragpositionDto.setNNettoeinzelpreisplusversteckteraufschlag(this
				.getNNettoeinzelpreisplusversteckteraufschlag());
		auftragpositionDto
				.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(this
						.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());
		auftragpositionDto.setNMwstbetrag(this.getNMwstbetrag());
		auftragpositionDto.setNBruttoeinzelpreis(this.getNBruttoeinzelpreis());
		auftragpositionDto.setTypCNr(this.getTypCNr());
		auftragpositionDto.setVerleihIId(this.getVerleihIId());
		auftragpositionDto.setNMaterialzuschlag(this.getNMaterialzuschlag());
		auftragpositionDto.setLieferantIId(this.getLieferantIId());
		auftragpositionDto.setBdEinkaufpreis(this.getNEinkaufpreis());
		auftragpositionDto.setNMaterialzuschlagKurs(this.getNMaterialzuschlagKurs());
		auftragpositionDto.setTMaterialzuschlagDatum(this.getTMaterialzuschlagDatum());
		// tUebersteuerbarerLiefertermin, bDrucken null

		return auftragpositionDto;
	}
	
	/**
	 * Handelt es sich um eine Intelligente Zwischensumme?
	 * @return true wenn es eine intelligente Zwischensumme ist
	 */
	public boolean isIntelligenteZwischensumme() {
		return AngebotServiceFac.ANGEBOTPOSITIONART_INTELLIGENTE_ZWISCHENSUMME
				.equalsIgnoreCase(getPositionsartCNr()) ;	
	}	
}
