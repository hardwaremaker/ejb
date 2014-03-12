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
package com.lp.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.xml.sax.helpers.DefaultHandler;

import com.lp.server.artikel.ejb.Artikel;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.system.service.HvDtoLogIdCnr;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.IIId;

/**
 * 
 * <p>
 * Diese Klasse ist die Oberklasse aller Belegpositionen.
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellung: Josef Ornetsmueller; dd.09.06
 * </p>
 * 
 * <p>
 * 
 * @author $Author: Gerold $
 *         </p>
 * 
 * @version not attributable Date $Date: 2012/05/04 13:16:10 $
 */
public abstract class BelegpositionDto extends DefaultHandler implements
		Serializable, IIId {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int iCopyPasteModus = COPY_PASTE_MODUS_NORMAL;

	public final static int COPY_PASTE_MODUS_NORMAL = 0;
	public final static int COPY_PASTE_MODUS_IST_PREIS_AUS_LOS = 1;

	private Integer iId = null;
	private Integer belegIId = null;
	private Integer iSort = null;
	private String positionsartCNr = null;
	private Integer artikelIId = null;
	private String cBez = null;
	private String cZusatzbez = null;
	private Short bArtikelbezeichnunguebersteuert = null;
	private String xTextinhalt = null;
	private Integer mediastandardIId = null;
	private BigDecimal nMenge = null;
	private String einheitCNr = null;
	private Integer positioniIdArtikelset;
	
	
	protected Short bNettopreisuebersteuert;
	

	public final Short getBNettopreisuebersteuert() {
		return bNettopreisuebersteuert;
	}

	public final void setBNettopreisuebersteuert(Short bNettopreisuebersteuert) {
		this.bNettopreisuebersteuert = bNettopreisuebersteuert;
	}
	private List<SeriennrChargennrMitMengeDto> alSeriennrChargennrMitMenge = null;
	

	public List<SeriennrChargennrMitMengeDto> getSeriennrChargennrMitMenge() {
		return alSeriennrChargennrMitMenge;
	}

	public void setSeriennrChargennrMitMenge(
			List<SeriennrChargennrMitMengeDto> alSeriennrChargennrMitMenge) {
		this.alSeriennrChargennrMitMenge = alSeriennrChargennrMitMenge;
	}

	public Integer getPositioniIdArtikelset() {
		return positioniIdArtikelset;
	}

	public void setPositioniIdArtikelset(Integer positioniIdArtikelset) {
		this.positioniIdArtikelset = positioniIdArtikelset;
	}

	private BigDecimal nMaterialzuschlag;

	public BigDecimal getNMaterialzuschlag() {
		return nMaterialzuschlag;
	}

	public void setNMaterialzuschlag(BigDecimal materialzuschlag) {
		nMaterialzuschlag = materialzuschlag;
	}

	public final Integer getIId() {
		return iId;
	}

	public final void setIId(Integer iId) {
		this.iId = iId;
	}

	public final Integer getBelegIId() {
		return belegIId;
	}

	public final void setBelegIId(Integer belegIId) {
		this.belegIId = belegIId;
	}

	public final Integer getISort() {
		return iSort;
	}

	public final void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public final String getPositionsartCNr() {
		return positionsartCNr;
	}

	public final void setPositionsartCNr(String positionsartCNr) {
		this.positionsartCNr = positionsartCNr;
	}

	@HvDtoLogIdCnr(entityClass=Artikel.class)
	public final Integer getArtikelIId() {
		return artikelIId;
	}

	public final void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public final String getCBez() {
		return cBez;
	}

	public final void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public final String getCZusatzbez() {
		return cZusatzbez;
	}

	public final void setCZusatzbez(String cZusatzbez) {
		this.cZusatzbez = cZusatzbez;
	}

	public final Short getBArtikelbezeichnunguebersteuert() {
		return this.bArtikelbezeichnunguebersteuert;
	}

	public final void setBArtikelbezeichnunguebersteuert(
			Short bArtikelbezeichnunguebersteuert) {
		this.bArtikelbezeichnunguebersteuert = bArtikelbezeichnunguebersteuert;
	}

	public final String getXTextinhalt() {
		return this.xTextinhalt;
	}

	public final void setXTextinhalt(String xTextinhalt) {
		this.xTextinhalt = xTextinhalt;
	}

	public final Integer getMediastandardIId() {
		return this.mediastandardIId;
	}

	public final void setMediastandardIId(Integer mediastandardIId) {
		this.mediastandardIId = mediastandardIId;
	}

	public final BigDecimal getNMenge() {
		return nMenge;
	}

	public final void setNMenge(BigDecimal nMenge) {
		this.nMenge = nMenge;
	}

	public final String getEinheitCNr() {
		return einheitCNr;
	}

	public final void setEinheitCNr(String einheitCNr) {
		this.einheitCNr = einheitCNr;
	}

	public final int getiCopyPasteModus() {
		return iCopyPasteModus;
	}

	public final void setiCopyPasteModus(int iCopyPasteModus) {
		this.iCopyPasteModus = iCopyPasteModus;
	}

	public String toString() {

		String returnString = "";

		returnString += ", iId: " + iId;
		returnString += ", belegIId: " + belegIId;
		returnString += ", iSort: " + iSort;
		returnString += ", positionsartCNr: " + positionsartCNr;
		returnString += ", artikelIId: " + artikelIId;
		returnString += ", cBez: " + cBez;
		returnString += ", cZusatzbez: " + cZusatzbez;
		returnString += ", bArtikelbezeichnunguebersteuert: "
				+ bArtikelbezeichnunguebersteuert;
		returnString += ", xTextinhalt: " + xTextinhalt;
		returnString += ", mediastandardIId: " + mediastandardIId;
		returnString += ", nMenge: " + nMenge;
		returnString += ", einheitCNr: " + einheitCNr;

		return returnString;
	}

	/**
	 * @deprecated MB: das gehoert in die BelegkonvertierungFacBaen
	 * 
	 * @param belegpositionDtoI
	 *            BelegpositionDto
	 * @return Object
	 */
	public Object cloneAsBelegpositionDto(BelegpositionDto belegpositionDtoI) {
		// iId null
		// belegIId null
		belegpositionDtoI.iSort = this.iSort;
		belegpositionDtoI.positionsartCNr = this.positionsartCNr;

		// if (this.positionsartCNr.equals(LocaleFac.POSITIONSART_IDENT)) { //
		// nicht fuer Handartikel
		belegpositionDtoI.artikelIId = this.artikelIId;
		// }

		belegpositionDtoI.cBez = this.cBez;
		belegpositionDtoI.cZusatzbez = this.cZusatzbez;
		belegpositionDtoI.bArtikelbezeichnunguebersteuert = this.bArtikelbezeichnunguebersteuert;
		belegpositionDtoI.xTextinhalt = this.xTextinhalt;
		belegpositionDtoI.mediastandardIId = this.mediastandardIId;
		belegpositionDtoI.nMenge = this.nMenge;
		belegpositionDtoI.einheitCNr = this.einheitCNr;

		return belegpositionDtoI;
	}

	public void fillWithXML(String sKeyI, String sValueI)
			throws NumberFormatException {
		// HV eigene (Feature-)Daten.
		// iId
		if (sKeyI.equals(SystemFac.SCHEMA_HV_FEATURE_I_ID)) {
			setIId(Integer.valueOf(sValueI));
		}
		// positionsartCNr
		else if (sKeyI.equals(SystemFac.SCHEMA_HV_FEATURE_POSITIONSART_C_NR)) {
			setPositionsartCNr(sValueI);
		}
		// artikelIId
		else if (sKeyI.equals(SystemFac.SCHEMA_HV_FEATURE_ARTIKEL_I_ID)) {
			setArtikelIId(Integer.valueOf(sValueI));
		}
		// cBez
		else if (sKeyI.equals(SystemFac.SCHEMA_HV_FEATURE_C_BEZ)) {
			setCBez(sValueI);
		}
		// cZusatzbez
		else if (sKeyI.equals(SystemFac.SCHEMA_HV_FEATURE_C_ZUSATZBEZ)) {
			setCZusatzbez(sValueI);
		}
		// Artikelbezeichnunguebersteuert
		else if (sKeyI
				.equals(SystemFac.SCHEMA_HV_FEATURE_ARTIKELBEZEICHNUNGUEBERSTEUERT)) {
			setBArtikelbezeichnunguebersteuert(Short.valueOf(sValueI));
		}
		// xTextinhalt
		else if (sKeyI.equals(SystemFac.SCHEMA_HV_FEATURE_XTEXTINHALT)) {
			setXTextinhalt(sValueI);
		}
		// mediastandardIId
		else if (sKeyI.equals(SystemFac.SCHEMA_HV_FEATURE_MEDIASTANDARD_I_ID)) {
			setMediastandardIId(Integer.valueOf(sValueI));
		}

		// OF Daten
		// QuantityCustomer
		else if (sKeyI.equals(SystemFac.SCHEMA_OF_QUANTITY_CUSTOMER)) {
			setNMenge(new BigDecimal(Double.valueOf(sValueI)));
		}
		// UnitCustomer
		else if (sKeyI.equals(SystemFac.SCHEMA_OF_QUANTITY_UNIT_CUSTOMER)) {
			setEinheitCNr(sValueI);
		}
	}	
}
