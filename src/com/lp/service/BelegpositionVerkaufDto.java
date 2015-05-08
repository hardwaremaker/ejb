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
package com.lp.service;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.lp.server.system.service.LocaleFac;

/**
 * @todo abstract machen
 * 
 *       <p>
 *       Diese Klasse kuemmert sich um ...
 *       </p>
 * 
 *       <p>
 *       Copyright Logistik Pur Software GmbH (c) 2004-2007
 *       </p>
 * 
 *       <p>
 *       Erstellung: Vorname Nachname; dd.mm.06
 *       </p>
 * 
 *       <p>
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2012/09/06 05:38:26 $
 */
public class BelegpositionVerkaufDto extends BelegpositionDto implements
		Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Double fRabattsatz;
	private Short bRabattsatzuebersteuert;
	private Double fZusatzrabattsatz;
	private Integer mwstsatzIId;
	private Short bMwstsatzuebersteuert;
	private BigDecimal nEinzelpreis;
	private BigDecimal nEinzelpreisplusversteckteraufschlag;
	private BigDecimal nNettoeinzelpreis;
	private BigDecimal nNettoeinzelpreisplusversteckteraufschlag;
	private BigDecimal nNettoeinzelpreisplusversteckteraufschlagminusrabatte;
	private BigDecimal nBruttoeinzelpreis;
	private Integer positioniId;
	private String typCNr;

	private Integer kostentraegerIId;

	private Integer zwsVonPosition;
	private Integer zwsBisPosition;
	private BigDecimal zwsNettoSumme;
	private Short bZwsPositionspreisZeigen;

	private Timestamp tMaterialzuschlagDatum;

	public Timestamp getTMaterialzuschlagDatum() {
		return tMaterialzuschlagDatum;
	}

	public void setTMaterialzuschlagDatum(Timestamp tMaterialzuschlagDatum) {
		this.tMaterialzuschlagDatum = tMaterialzuschlagDatum;
	}

	private BigDecimal nMaterialzuschlagKurs;

	public BigDecimal getNMaterialzuschlagKurs() {
		return nMaterialzuschlagKurs;
	}

	public void setNMaterialzuschlagKurs(BigDecimal nMaterialzuschlagKurs) {
		this.nMaterialzuschlagKurs = nMaterialzuschlagKurs;
	}

	protected BigDecimal nMwstbetrag;

	public BigDecimal getNMwstbetrag() {
		return nMwstbetrag;
	}

	public void setNMwstbetrag(BigDecimal nMwstbetrag) {
		this.nMwstbetrag = nMwstbetrag;
	}

	protected BigDecimal nRabattbetrag;

	public BigDecimal getNRabattbetrag() {
		return nRabattbetrag;
	}

	public void setNRabattbetrag(BigDecimal nRabattbetrag) {
		this.nRabattbetrag = nRabattbetrag;
	}

	private Integer lieferantIId;

	public Integer getLieferantIId() {
		return lieferantIId;
	}

	public void setLieferantIId(Integer lieferantIId) {
		this.lieferantIId = lieferantIId;
	}

	public Integer getKostentraegerIId() {
		return kostentraegerIId;
	}

	public void setKostentraegerIId(Integer kostentraegerIId) {
		this.kostentraegerIId = kostentraegerIId;
	}

	private String cLvposition;

	public String getCLvposition() {
		return cLvposition;
	}

	public void setCLvposition(String cLvposition) {
		this.cLvposition = cLvposition;
	}

	public Integer getVerleihIId() {
		return verleihIId;
	}

	public void setVerleihIId(Integer verleihIId) {
		this.verleihIId = verleihIId;
	}

	private Integer verleihIId;

	private BigDecimal nReportGesamtpreis;
	private BigDecimal nReportEinzelpreis;
	private BigDecimal nReportEinzelpreisplusversteckteraufschlag;
	private BigDecimal nReportNettoeinzelpreisplusversteckteraufschlag;

	private Double dReportRabattsatz;
	private Double dReportZusatzrabattsatz;
	private Double dReportMwstsatz;
	private BigDecimal nReportMwstsatzbetrag;

	public final Double getFRabattsatz() {
		return fRabattsatz;
	}

	public final void setFRabattsatz(Double fRabattsatz) {
		this.fRabattsatz = fRabattsatz;
	}

	public final Short getBRabattsatzuebersteuert() {
		return bRabattsatzuebersteuert;
	}

	public final void setBRabattsatzuebersteuert(Short bRabattsatzuebersteuert) {
		this.bRabattsatzuebersteuert = bRabattsatzuebersteuert;
	}

	public final Double getFZusatzrabattsatz() {
		return fZusatzrabattsatz;
	}

	public final void setFZusatzrabattsatz(Double fZusatzrabattsatz) {
		this.fZusatzrabattsatz = fZusatzrabattsatz;
	}

	public final Integer getMwstsatzIId() {
		return mwstsatzIId;
	}

	public final void setMwstsatzIId(Integer mwstsatzIId) {
		this.mwstsatzIId = mwstsatzIId;
	}

	public final Short getBMwstsatzuebersteuert() {
		return bMwstsatzuebersteuert;
	}

	public final void setBMwstsatzuebersteuert(Short bMwstsatzuebersteuert) {
		this.bMwstsatzuebersteuert = bMwstsatzuebersteuert;
	}

	public final BigDecimal getNEinzelpreis() {
		return nEinzelpreis;
	}

	public final void setNEinzelpreis(BigDecimal nEinzelpreis) {
		this.nEinzelpreis = nEinzelpreis;
	}

	public final BigDecimal getNEinzelpreisplusversteckteraufschlag() {
		return nEinzelpreisplusversteckteraufschlag;
	}

	public final void setNEinzelpreisplusversteckteraufschlag(
			BigDecimal nEinzelpreisplusversteckteraufschlag) {
		this.nEinzelpreisplusversteckteraufschlag = nEinzelpreisplusversteckteraufschlag;
	}

	public final BigDecimal getNNettoeinzelpreis() {
		return nNettoeinzelpreis;
	}

	public final void setNNettoeinzelpreis(BigDecimal nNettoeinzelpreis) {
		this.nNettoeinzelpreis = nNettoeinzelpreis;
	}

	public final BigDecimal getNNettoeinzelpreisplusversteckteraufschlag() {
		return nNettoeinzelpreisplusversteckteraufschlag;
	}

	public final void setNNettoeinzelpreisplusversteckteraufschlag(
			BigDecimal nNettoeinzelpreisplusversteckteraufschlag) {
		this.nNettoeinzelpreisplusversteckteraufschlag = nNettoeinzelpreisplusversteckteraufschlag;
	}

	public final BigDecimal getNNettoeinzelpreisplusversteckteraufschlagminusrabatte() {
		return nNettoeinzelpreisplusversteckteraufschlagminusrabatte;
	}

	public final void setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(
			BigDecimal nNettoeinzelpreisplusversteckteraufschlagminusrabatte) {
		this.nNettoeinzelpreisplusversteckteraufschlagminusrabatte = nNettoeinzelpreisplusversteckteraufschlagminusrabatte;
	}

	public final BigDecimal getNBruttoeinzelpreis() {
		return nBruttoeinzelpreis;
	}

	public final void setNBruttoeinzelpreis(BigDecimal nBruttoeinzelpreis) {
		this.nBruttoeinzelpreis = nBruttoeinzelpreis;
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

	public String toString() {
		String returnString = super.toString();
		returnString += ", " + fRabattsatz;
		returnString += ", " + bRabattsatzuebersteuert;
		returnString += ", " + fZusatzrabattsatz;
		returnString += ", " + mwstsatzIId;
		returnString += ", " + bMwstsatzuebersteuert;
		returnString += ", " + nEinzelpreis;
		returnString += ", " + nEinzelpreisplusversteckteraufschlag;
		returnString += ", " + nNettoeinzelpreis;
		returnString += ", " + nNettoeinzelpreisplusversteckteraufschlag;
		returnString += ", "
				+ nNettoeinzelpreisplusversteckteraufschlagminusrabatte;
		returnString += ", " + nBruttoeinzelpreis;
		returnString += ", " + positioniId;
		returnString += ", " + typCNr;
		return returnString;
	}

	/**
	 * @deprecated MB: das gehoert in die BelegkonvertierungFacBean
	 * 
	 * @param belegpositionVerkaufDtoI
	 *            BelegpositionVerkaufDto
	 * @return Object
	 */
	public Object cloneAsBelegpositionVerkaufDto(
			BelegpositionVerkaufDto belegpositionVerkaufDtoI) {
		belegpositionVerkaufDtoI = (BelegpositionVerkaufDto) cloneAsBelegpositionDto(new BelegpositionVerkaufDto());

		belegpositionVerkaufDtoI.fRabattsatz = this.fRabattsatz;
		belegpositionVerkaufDtoI.bRabattsatzuebersteuert = this.bRabattsatzuebersteuert;
		belegpositionVerkaufDtoI.fZusatzrabattsatz = this.fZusatzrabattsatz;
		belegpositionVerkaufDtoI.mwstsatzIId = this.mwstsatzIId;
		belegpositionVerkaufDtoI.bMwstsatzuebersteuert = this.bMwstsatzuebersteuert;
		belegpositionVerkaufDtoI.bNettopreisuebersteuert = this.bNettopreisuebersteuert;
		belegpositionVerkaufDtoI.nEinzelpreis = this.nEinzelpreis;
		belegpositionVerkaufDtoI.nEinzelpreisplusversteckteraufschlag = this.nEinzelpreisplusversteckteraufschlag;
		belegpositionVerkaufDtoI.nNettoeinzelpreis = this.nNettoeinzelpreis;
		belegpositionVerkaufDtoI.nNettoeinzelpreisplusversteckteraufschlag = this.nNettoeinzelpreisplusversteckteraufschlag;
		belegpositionVerkaufDtoI.nNettoeinzelpreisplusversteckteraufschlagminusrabatte = this.nNettoeinzelpreisplusversteckteraufschlagminusrabatte;
		belegpositionVerkaufDtoI.nBruttoeinzelpreis = this.nBruttoeinzelpreis;
		belegpositionVerkaufDtoI.zwsVonPosition = this.zwsVonPosition;
		belegpositionVerkaufDtoI.zwsBisPosition = this.zwsBisPosition;
		belegpositionVerkaufDtoI.bZwsPositionspreisZeigen = this.bZwsPositionspreisZeigen;
		return belegpositionVerkaufDtoI;
	}

	public BigDecimal getNReportGesamtpreis() {
		return nReportGesamtpreis;
	}

	public void setNReportGesamtpreis(BigDecimal reportGesamtpreis) {
		nReportGesamtpreis = reportGesamtpreis;
	}

	public BigDecimal getNReportEinzelpreis() {
		return nReportEinzelpreis;
	}

	public void setNReportEinzelpreis(BigDecimal reportEinzelpreis) {
		nReportEinzelpreis = reportEinzelpreis;
	}

	public BigDecimal getNReportEinzelpreisplusversteckteraufschlag() {
		return nReportEinzelpreisplusversteckteraufschlag;
	}

	public void setNReportEinzelpreisplusversteckteraufschlag(
			BigDecimal reportEinzelpreisplusversteckteraufschlag) {
		nReportEinzelpreisplusversteckteraufschlag = reportEinzelpreisplusversteckteraufschlag;
	}

	public BigDecimal getNReportNettoeinzelpreisplusversteckteraufschlag() {
		return nReportNettoeinzelpreisplusversteckteraufschlag;
	}

	public void setNReportNettoeinzelpreisplusversteckteraufschlag(
			BigDecimal reportNettoeinzelpreisplusversteckteraufschlag) {
		nReportNettoeinzelpreisplusversteckteraufschlag = reportNettoeinzelpreisplusversteckteraufschlag;
	}

	public Double getDReportRabattsatz() {
		return dReportRabattsatz;
	}

	public void setDReportRabattsatz(Double reportRabattsatz) {
		dReportRabattsatz = reportRabattsatz;
	}

	public Double getDReportZusatzrabattsatz() {
		return dReportZusatzrabattsatz;
	}

	public void setDReportZusatzrabattsatz(Double reportZusatzrabattsatz) {
		dReportZusatzrabattsatz = reportZusatzrabattsatz;
	}

	public Double getDReportMwstsatz() {
		return dReportMwstsatz;
	}

	public void setDReportMwstsatz(Double reportMwstsatz) {
		dReportMwstsatz = reportMwstsatz;
	}

	public BigDecimal getNReportMwstsatzbetrag() {
		return nReportMwstsatzbetrag;
	}

	public void setNReportMwstsatzbetrag(BigDecimal nReportMwstsatzbetra) {
		nReportMwstsatzbetrag = nReportMwstsatzbetra;

	}

	public Integer getZwsVonPosition() {
		return zwsVonPosition;
	}

	public void setZwsVonPosition(Integer zwsVonPosition) {
		this.zwsVonPosition = zwsVonPosition;
	}

	public Integer getZwsBisPosition() {
		return zwsBisPosition;
	}

	public void setZwsBisPosition(Integer zwsBisPosition) {
		this.zwsBisPosition = zwsBisPosition;
	}

	public BigDecimal getZwsNettoSumme() {
		return zwsNettoSumme;
	}

	public void setZwsNettoSumme(BigDecimal zwsNettoSumme) {
		this.zwsNettoSumme = zwsNettoSumme;
	}

	public Short getBZwsPositionspreisZeigen() {
		return bZwsPositionspreisZeigen;
	}

	public void setBZwsPositionspreisDrucken(Short bZwsPositionspreisZeigen) {
		this.bZwsPositionspreisZeigen = bZwsPositionspreisZeigen;
	}
	
	/**
	 * Handelt es sich um eine Intelligente Zwischensummenposition?
	 * 
	 * @return true wenn es eine Positionsart "Intelligente Zwischensumme" ist
	 */
	public boolean isIntelligenteZwischensumme() {
		return LocaleFac.POSITIONSART_INTELLIGENTE_ZWISCHENSUMME.equalsIgnoreCase(getPositionsartCNr()) ;
	}
	
	/**
	 * Handelt es sich um eine Handeingabe?
	 * @return true wenn es eine Positionsart "HANDEINGABE" ist
	 */
	public boolean isHandeingabe() {
		return LocaleFac.POSITIONSART_HANDEINGABE.equalsIgnoreCase(getPositionsartCNr()) ;		
	}

	/**
	 * Handelt es sich um eine Artikelposition
	 * @return true wenn es eine Positionsart "IDENT" ist
	 */
	public boolean isIdent() {
		return LocaleFac.POSITIONSART_IDENT.equalsIgnoreCase(getPositionsartCNr()) ;		
	}

	/**
	 * Handelt es sich um eine mengenbehaftete Position?</br>
	 * <p>Mengenbehaftete Positionen sind solche, die entweder Ident- oder Handeingabe sind</p>
	 * @return true wenn es eine mengenbehaftete Position ist
	 */
	public boolean isMengenbehaftet() {
		return isIdent() || isHandeingabe() ;
	}
	
	public boolean isLieferschein() {
		return LocaleFac.POSITIONSART_LIEFERSCHEIN.equalsIgnoreCase(getPositionsartCNr()) ;
	}
}
