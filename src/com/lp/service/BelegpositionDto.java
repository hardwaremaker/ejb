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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((alSeriennrChargennrMitMenge == null) ? 0
						: alSeriennrChargennrMitMenge.hashCode());
		result = prime * result
				+ ((artikelIId == null) ? 0 : artikelIId.hashCode());
		result = prime
				* result
				+ ((bArtikelbezeichnunguebersteuert == null) ? 0
						: bArtikelbezeichnunguebersteuert.hashCode());
		result = prime
				* result
				+ ((bNettopreisuebersteuert == null) ? 0
						: bNettopreisuebersteuert.hashCode());
		result = prime * result
				+ ((belegIId == null) ? 0 : belegIId.hashCode());
		result = prime * result + ((cBez == null) ? 0 : cBez.hashCode());
		result = prime * result
				+ ((cZusatzbez == null) ? 0 : cZusatzbez.hashCode());
		result = prime * result
				+ ((einheitCNr == null) ? 0 : einheitCNr.hashCode());
		result = prime * result + iCopyPasteModus;
		result = prime * result + ((iId == null) ? 0 : iId.hashCode());
		result = prime * result + ((iSort == null) ? 0 : iSort.hashCode());
		result = prime
				* result
				+ ((mediastandardIId == null) ? 0 : mediastandardIId.hashCode());
		result = prime
				* result
				+ ((nMaterialzuschlag == null) ? 0 : nMaterialzuschlag
						.hashCode());
		result = prime * result + ((nMenge == null) ? 0 : nMenge.hashCode());
		result = prime
				* result
				+ ((positioniIdArtikelset == null) ? 0 : positioniIdArtikelset
						.hashCode());
		result = prime * result
				+ ((positionsartCNr == null) ? 0 : positionsartCNr.hashCode());
		result = prime * result
				+ ((xTextinhalt == null) ? 0 : xTextinhalt.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BelegpositionDto other = (BelegpositionDto) obj;
		if (alSeriennrChargennrMitMenge == null) {
			if (other.alSeriennrChargennrMitMenge != null)
				return false;
		} else if (!alSeriennrChargennrMitMenge
				.equals(other.alSeriennrChargennrMitMenge))
			return false;
		if (artikelIId == null) {
			if (other.artikelIId != null)
				return false;
		} else if (!artikelIId.equals(other.artikelIId))
			return false;
		if (bArtikelbezeichnunguebersteuert == null) {
			if (other.bArtikelbezeichnunguebersteuert != null)
				return false;
		} else if (!bArtikelbezeichnunguebersteuert
				.equals(other.bArtikelbezeichnunguebersteuert))
			return false;
		if (bNettopreisuebersteuert == null) {
			if (other.bNettopreisuebersteuert != null)
				return false;
		} else if (!bNettopreisuebersteuert
				.equals(other.bNettopreisuebersteuert))
			return false;
		if (belegIId == null) {
			if (other.belegIId != null)
				return false;
		} else if (!belegIId.equals(other.belegIId))
			return false;
		if (cBez == null) {
			if (other.cBez != null)
				return false;
		} else if (!cBez.equals(other.cBez))
			return false;
		if (cZusatzbez == null) {
			if (other.cZusatzbez != null)
				return false;
		} else if (!cZusatzbez.equals(other.cZusatzbez))
			return false;
		if (einheitCNr == null) {
			if (other.einheitCNr != null)
				return false;
		} else if (!einheitCNr.equals(other.einheitCNr))
			return false;
		if (iCopyPasteModus != other.iCopyPasteModus)
			return false;
		if (iId == null) {
			if (other.iId != null)
				return false;
		} else if (!iId.equals(other.iId))
			return false;
		if (iSort == null) {
			if (other.iSort != null)
				return false;
		} else if (!iSort.equals(other.iSort))
			return false;
		if (mediastandardIId == null) {
			if (other.mediastandardIId != null)
				return false;
		} else if (!mediastandardIId.equals(other.mediastandardIId))
			return false;
		if (nMaterialzuschlag == null) {
			if (other.nMaterialzuschlag != null)
				return false;
		} else if (!nMaterialzuschlag.equals(other.nMaterialzuschlag))
			return false;
		if (nMenge == null) {
			if (other.nMenge != null)
				return false;
		} else if (!nMenge.equals(other.nMenge))
			return false;
		if (positioniIdArtikelset == null) {
			if (other.positioniIdArtikelset != null)
				return false;
		} else if (!positioniIdArtikelset.equals(other.positioniIdArtikelset))
			return false;
		if (positionsartCNr == null) {
			if (other.positionsartCNr != null)
				return false;
		} else if (!positionsartCNr.equals(other.positionsartCNr))
			return false;
		if (xTextinhalt == null) {
			if (other.xTextinhalt != null)
				return false;
		} else if (!xTextinhalt.equals(other.xTextinhalt))
			return false;
		return true;
	}
	
	
}
