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
package com.lp.server.partner.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class SerienbriefDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String cBez;
	private String cPlz;
	private String mandantCNr;
	private Timestamp tAnlegen;
	private Integer personalIIdAnlegen;
	private Timestamp tAendern;
	private Integer personalIIdAendern;
	private Short bGehtAnKunden;
	private Short bGehtAnInteressenten;
	private Short bVersteckteDabei;
	private Integer ansprechpartnerfunktionIId;
	private Short bAnsprechpartnerfunktionAuchOhne;
	private String sBetreff = null;
	private String sXText = null;
	private Integer landIId = null;
	private Short bGehtanmoeglichelieferanten;
	private Short bGehtanlieferanten;
	private Short bGehtanpartner;
	private Integer partnerklasseIId;
	private Integer brancheIId;
	private String xMailtext;

	private String localeCNr;

	public String getLocaleCNr() {
		return localeCNr;
	}

	public void setLocaleCNr(String localeCNr) {
		this.localeCNr = localeCNr;
	}

	private Short bWennkeinanspmitfktDannersteransp;

	private Short bSelektionenLogischesOder;
	private Short bHtml;

	public Short getBWennkeinanspmitfktDannersteransp() {
		return bWennkeinanspmitfktDannersteransp;
	}

	public void setBWennkeinanspmitfktDannersteransp(
			Short bWennkeinanspmitfktDannersteransp) {
		this.bWennkeinanspmitfktDannersteransp = bWennkeinanspmitfktDannersteransp;
	}

	public Short getBSelektionenLogischesOder() {
		return bSelektionenLogischesOder;
	}

	public void setBSelektionenLogischesOder(Short bSelektionenLogischesOder) {
		this.bSelektionenLogischesOder = bSelektionenLogischesOder;
	}

	private boolean newsletter;

	public boolean isNewsletter() {
		return newsletter;
	}

	public void setNewsletter(boolean newsletter) {
		this.newsletter = newsletter;
	}

	public String getXMailtext() {
		return this.xMailtext;
	}

	public void setXMailtext(String xMailtext) {
		this.xMailtext = xMailtext;
	}

	public Integer getPartnerklasseIId() {
		return partnerklasseIId;
	}

	public void setPartnerklasseIId(Integer partnerklasseIId) {
		this.partnerklasseIId = partnerklasseIId;
	}

	public Integer getBrancheIId() {
		return brancheIId;
	}

	public void setBrancheIId(Integer brancheIId) {
		this.brancheIId = brancheIId;
	}

	private Short bMitzugeordnetenfirmen;

	public Short getBMitzugeordnetenfirmen() {
		return bMitzugeordnetenfirmen;
	}

	public void setBMitzugeordnetenfirmen(Short bMitzugeordnetenfirmen) {
		this.bMitzugeordnetenfirmen = bMitzugeordnetenfirmen;
	}

	private BigDecimal nAbumsatz;

	public BigDecimal getNAbumsatz() {
		return nAbumsatz;
	}

	public void setNAbumsatz(BigDecimal abumsatz) {
		nAbumsatz = abumsatz;
	}

	public BigDecimal getNBisumsatz() {
		return nBisumsatz;
	}

	public void setNBisumsatz(BigDecimal bisumsatz) {
		nBisumsatz = bisumsatz;
	}

	public Timestamp getTUmsatzab() {
		return tUmsatzab;
	}

	public void setTUmsatzab(Timestamp umsatzab) {
		tUmsatzab = umsatzab;
	}

	public Timestamp getTUmsatzbis() {
		return tUmsatzbis;
	}

	public void setTUmsatzbis(Timestamp umsatzbis) {
		tUmsatzbis = umsatzbis;
	}

	private BigDecimal nBisumsatz;
	private Timestamp tUmsatzab;
	private Timestamp tUmsatzbis;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCBez() {
		return cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public String getCPlz() {
		return cPlz;
	}

	public void setCPlz(String cPlz) {
		this.cPlz = cPlz;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Short getBGehtAnKunden() {
		return bGehtAnKunden;
	}

	public void setBGehtAnKunden(Short bGehtAnKunden) {
		this.bGehtAnKunden = bGehtAnKunden;
	}

	public Short getBGehtAnInteressenten() {
		return bGehtAnInteressenten;
	}

	public void setBGehtAnInteressenten(Short bGehtAnInteressenten) {
		this.bGehtAnInteressenten = bGehtAnInteressenten;
	}

	public Short getBVersteckteDabei() {
		return bVersteckteDabei;
	}

	public void setBVersteckteDabei(Short bVersteckteDabei) {
		this.bVersteckteDabei = bVersteckteDabei;
	}

	public Integer getAnsprechpartnerfunktionIId() {
		return ansprechpartnerfunktionIId;
	}

	public void setAnsprechpartnerfunktionIId(Integer ansprechpartnerfunktionIId) {
		this.ansprechpartnerfunktionIId = ansprechpartnerfunktionIId;
	}

	public Short getBAnsprechpartnerfunktionAuchOhne() {
		return bAnsprechpartnerfunktionAuchOhne;
	}

	public String getSBetreff() {
		return sBetreff;
	}

	public String getSXText() {
		return sXText;
	}

	public Integer getLandIId() {
		return landIId;
	}

	public Short getBGehtanmoeglichelieferanten() {
		return bGehtanmoeglichelieferanten;
	}

	public Short getBGehtanlieferanten() {
		return bGehtanlieferanten;
	}

	public Short getBGehtanpartner() {
		return bGehtanpartner;
	}

	public void setBAnsprechpartnerfunktionAuchOhne(
			Short bAnsprechpartnerfunktionAuchOhne) {
		this.bAnsprechpartnerfunktionAuchOhne = bAnsprechpartnerfunktionAuchOhne;
	}

	public void setSBetreff(String sBetreff) {
		this.sBetreff = sBetreff;
	}

	public void setSXText(String sXText) {
		this.sXText = sXText;
	}

	public void setLandIId(Integer landIId) {
		this.landIId = landIId;
	}

	public void setBGehtanmoeglichelieferanten(Short bGehtanmoeglichelieferanten) {
		this.bGehtanmoeglichelieferanten = bGehtanmoeglichelieferanten;
	}

	public void setBGehtanlieferanten(Short bGehtanlieferanten) {
		this.bGehtanlieferanten = bGehtanlieferanten;
	}

	public void setBGehtanpartner(Short bGehtanpartner) {
		this.bGehtanpartner = bGehtanpartner;
	}

	public Short getBHtml() {
		return bHtml;
	}

	public void setBHtml(Short bHtml) {
		this.bHtml = bHtml;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof SerienbriefDto)) {
			return false;
		}
		SerienbriefDto that = (SerienbriefDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId))) {
			return false;
		}
		if (!(that.cBez == null ? this.cBez == null : that.cBez
				.equals(this.cBez))) {
			return false;
		}
		if (!(that.cPlz == null ? this.cPlz == null : that.cPlz
				.equals(this.cPlz))) {
			return false;
		}
		if (!(that.mandantCNr == null ? this.mandantCNr == null
				: that.mandantCNr.equals(this.mandantCNr))) {
			return false;
		}
		if (!(that.tAnlegen == null ? this.tAnlegen == null : that.tAnlegen
				.equals(this.tAnlegen))) {
			return false;
		}
		if (!(that.personalIIdAnlegen == null ? this.personalIIdAnlegen == null
				: that.personalIIdAnlegen.equals(this.personalIIdAnlegen))) {
			return false;
		}
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern
				.equals(this.tAendern))) {
			return false;
		}
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern))) {
			return false;
		}
		if (!(that.bGehtAnKunden == null ? this.bGehtAnKunden == null
				: that.bGehtAnKunden.equals(this.bGehtAnKunden))) {
			return false;
		}
		if (!(that.bGehtAnInteressenten == null ? this.bGehtAnInteressenten == null
				: that.bGehtAnInteressenten.equals(this.bGehtAnInteressenten))) {
			return false;
		}
		if (!(that.bVersteckteDabei == null ? this.bVersteckteDabei == null
				: that.bVersteckteDabei.equals(this.bVersteckteDabei))) {
			return false;
		}
		if (!(that.ansprechpartnerfunktionIId == null ? this.ansprechpartnerfunktionIId == null
				: that.ansprechpartnerfunktionIId
						.equals(this.ansprechpartnerfunktionIId))) {
			return false;
		}
		if (!(that.bAnsprechpartnerfunktionAuchOhne == null ? this.bAnsprechpartnerfunktionAuchOhne == null
				: that.bAnsprechpartnerfunktionAuchOhne
						.equals(this.bAnsprechpartnerfunktionAuchOhne))) {
			return false;
		}
		if (that.newsletter != this.newsletter) {
			return false;
		}
		if (!(that.bHtml == null ? this.bHtml == null : that.bHtml
				.equals(this.bHtml))) {
			return false;
		}
		return true;
	}

	public boolean isHtml() {
		return bHtml == null ? false : bHtml > 0;
	}

	public boolean isPlain() {
		return bHtml == null || bHtml == 0;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.cBez.hashCode();
		result = 37 * result + this.cPlz.hashCode();
		result = 37 * result + this.mandantCNr.hashCode();
		result = 37 * result + this.tAnlegen.hashCode();
		result = 37 * result + this.personalIIdAnlegen.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.bGehtAnKunden.hashCode();
		result = 37 * result + this.bGehtAnInteressenten.hashCode();
		result = 37 * result + this.bVersteckteDabei.hashCode();
		result = 37 * result + this.ansprechpartnerfunktionIId.hashCode();
		result = 37 * result + this.bAnsprechpartnerfunktionAuchOhne.hashCode();
		result = 37 * result + this.bHtml.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + cBez;
		returnString += ", " + cPlz;
		returnString += ", " + mandantCNr;
		returnString += ", " + tAnlegen;
		returnString += ", " + personalIIdAnlegen;
		returnString += ", " + tAendern;
		returnString += ", " + personalIIdAendern;
		returnString += ", " + bGehtAnKunden;
		returnString += ", " + bGehtAnInteressenten;
		returnString += ", " + bVersteckteDabei;
		returnString += ", " + ansprechpartnerfunktionIId;
		returnString += ", " + bAnsprechpartnerfunktionAuchOhne;
		returnString += ", " + newsletter;
		return returnString;
	}
}
