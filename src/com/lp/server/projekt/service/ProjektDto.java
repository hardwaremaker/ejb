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
package com.lp.server.projekt.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;

import org.jfree.chart.block.BlockContainer;

import com.lp.server.util.EditorContentIId;

public class ProjektDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String cNr;
	private String kategorieCNr;
	private String cTitel;
	private Integer personalIIdErzeuger;
	private Integer personalIIdZugewiesener;
	private Integer personalIIdErlediger;
	private String projekttypCNr;
	private String mandantCNr;
	private Integer iPrio;
	private String statusCNr;
	private byte[] oAttachments;
	private String cAttachmentsType;
	private String xFreetext;
	private Timestamp tZielwunschdatum;
	private Integer partnerIId;
	private Integer ansprechpartnerIId;
	private Integer iVerrechenbar;
	private Integer personalIIdAnlegen;
	private Timestamp tAnlegen;
	private Integer personalIIdAendern;
	private Timestamp tAendern;
	private Timestamp tZeit;
	private String cDateiname;
	private Timestamp tErledigt;
	private Double dDauer;
	private Short bFreigegeben;
	private Integer iSort;
	private Integer iWahrscheinlichkeit;
	private BigDecimal nUmsatzgeplant;
	private Integer bereichIId;
	private String deployNumber;
	private String buildNumber;

	private Timestamp tRealisierung;
	private Integer artikelIId;
	private Integer vkfortschrittIId;
	private Integer ansprechpartnerIIdBetreiber;
	private Integer projekterledigungsgrundIId;
	private Integer partnerIIdBetreiber;
	private Integer personalIIdInternerledigt;
	private Timestamp tInternerledigt;

	private EditorContentIId contentId;
	
	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public Timestamp getTRealisierung() {
		return tRealisierung;
	}

	public void setTRealisierung(Timestamp tRealisierung) {
		this.tRealisierung = tRealisierung;
	}

	public Integer getVkfortschrittIId() {
		return vkfortschrittIId;
	}

	public void setVkfortschrittIId(Integer vkfortschrittIId) {
		this.vkfortschrittIId = vkfortschrittIId;
	}

	public Integer getAnsprechpartnerIIdBetreiber() {
		return ansprechpartnerIIdBetreiber;
	}

	public void setAnsprechpartnerIIdBetreiber(Integer ansprechpartnerIIdBetreiber) {
		this.ansprechpartnerIIdBetreiber = ansprechpartnerIIdBetreiber;
	}

	public Integer getBereichIId() {
		return bereichIId;
	}

	public void setBereichIId(Integer bereichIId) {
		this.bereichIId = bereichIId;
	}


	public Integer getProjekterledigungsgrundIId() {
		return projekterledigungsgrundIId;
	}

	public void setProjekterledigungsgrundIId(Integer projekterledigungsgrundIId) {
		this.projekterledigungsgrundIId = projekterledigungsgrundIId;
	}

	public Integer getIWahrscheinlichkeit() {
		return iWahrscheinlichkeit;
	}

	public void setIWahrscheinlichkeit(Integer wahrscheinlichkeit) {
		iWahrscheinlichkeit = wahrscheinlichkeit;
	}

	public BigDecimal getNUmsatzgeplant() {
		return nUmsatzgeplant;
	}

	public void setNUmsatzgeplant(BigDecimal umsatzgeplant) {
		nUmsatzgeplant = umsatzgeplant;
	}

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}


	public Integer getPartnerIIdBetreiber() {
		return partnerIIdBetreiber;
	}

	public void setPartnerIIdBetreiber(Integer partnerIIdBetreiber) {
		this.partnerIIdBetreiber = partnerIIdBetreiber;
	}

	public Integer getISort() {
		return iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public String getKategorieCNr() {
		return kategorieCNr;
	}

	public String getCNr() {
		return cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public void setKategorieCNr(String cKategorie) {
		this.kategorieCNr = cKategorie;
	}

	public String getCTitel() {
		return cTitel;
	}

	public void setCTitel(String cTitel) {
		this.cTitel = cTitel;
	}

	public Integer getPersonalIIdErzeuger() {
		return personalIIdErzeuger;
	}

	public void setPersonalIIdErzeuger(Integer personalIIdErzeuger) {
		this.personalIIdErzeuger = personalIIdErzeuger;
	}

	public Integer getPersonalIIdZugewiesener() {
		return personalIIdZugewiesener;
	}

	public void setPersonalIIdZugewiesener(Integer personalIIdZugewiesener) {
		this.personalIIdZugewiesener = personalIIdZugewiesener;
	}

	public Integer getIPrio() {
		return iPrio;
	}

	public void setIPrio(Integer iPrio) {
		this.iPrio = iPrio;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public String getStatusCNr() {
		return statusCNr;
	}

	public void setStatusCNr(String cStatus) {
		this.statusCNr = cStatus;
	}

	public byte[] getOAttachments() {
		return oAttachments;
	}

	public void setOAttachments(byte[] oAttachments) {
		this.oAttachments = oAttachments;
	}


	public Integer getPersonalIIdInternerledigt() {
		return personalIIdInternerledigt;
	}

	public void setPersonalIIdInternerledigt(Integer personalIIdInternerledigt) {
		this.personalIIdInternerledigt = personalIIdInternerledigt;
	}


	public Timestamp getTInternerledigt() {
		return tInternerledigt;
	}

	public void setTInternerledigt(Timestamp internerledigt) {
		tInternerledigt = internerledigt;
	}

	public String getCAttachmentsType() {
		return cAttachmentsType;
	}

	public void setCAttachmentsType(String cAttachmentsType) {
		this.cAttachmentsType = cAttachmentsType;
	}

	public String getXFreetext() {
		return xFreetext;
	}

	public void setXFreetext(String xFreetext) {
		this.xFreetext = xFreetext;
	}

	public Timestamp getTZielwunschdatum() {
		return tZielwunschdatum;
	}

	public void setTZielwunschdatum(Timestamp tZielwunschdatum) {
		this.tZielwunschdatum = tZielwunschdatum;
	}

	public Integer getPartnerIId() {
		return partnerIId;
	}

	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId;
	}

	public Integer getAnsprechpartnerIId() {
		return ansprechpartnerIId;
	}

	public void setAnsprechpartnerIId(Integer ansprechpartnerIId) {
		this.ansprechpartnerIId = ansprechpartnerIId;
	}

	public Integer getIVerrechenbar() {
		return this.iVerrechenbar;
	}

	public void setIVerrechenbar(Integer iVerrechenbar) {
		this.iVerrechenbar = iVerrechenbar;
	}
	private Integer projektIIdNachfolger;

	public Integer getProjektIIdNachfolger() {
		return projektIIdNachfolger;
	}

	public void setProjektIIdNachfolger(Integer projektIIdNachfolger) {
		this.projektIIdNachfolger = projektIIdNachfolger;
	}

	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Timestamp getTZeit() {
		return tZeit;
	}

	public String getCDateiname() {
		return cDateiname;
	}

	public String getProjekttypCNr() {
		return projekttypCNr;
	}

	public Timestamp getTErledigt() {
		return tErledigt;
	}

	public Integer getPersonalIIdErlediger() {
		return personalIIdErlediger;
	}

	public Double getDDauer() {
		return dDauer;
	}

	public Short getBFreigegeben() {
		return bFreigegeben;
	}

	public void setTZeit(Timestamp tZeit) {
		this.tZeit = tZeit;
	}

	public void setCDateiname(String cDateiname) {
		this.cDateiname = cDateiname;
	}

	public void setProjekttypCNr(String projekttypCNr) {
		this.projekttypCNr = projekttypCNr;
	}

	public void setTErledigt(Timestamp tErledigt) {
		this.tErledigt = tErledigt;
	}

	public void setPersonalIIdErlediger(Integer personalIIdErlediger) {
		this.personalIIdErlediger = personalIIdErlediger;
	}

	public void setDDauer(Double dDauer) {
		this.dDauer = dDauer;
	}

	public void setBFreigegeben(Short bFreigegeben) {
		this.bFreigegeben = bFreigegeben;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ProjektDto)) {
			return false;
		}
		ProjektDto that = (ProjektDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId))) {
			return false;
		}
		if (!(that.cNr == null ? this.cNr == null : that.cNr.equals(this.cNr))) {
			return false;
		}
		if (!(that.kategorieCNr == null ? this.kategorieCNr == null : that.kategorieCNr.equals(this.kategorieCNr))) {
			return false;
		}
		if (!(that.cTitel == null ? this.cTitel == null : that.cTitel.equals(this.cTitel))) {
			return false;
		}
		if (!(that.personalIIdErzeuger == null ? this.personalIIdErzeuger == null
				: that.personalIIdErzeuger.equals(this.personalIIdErzeuger))) {
			return false;
		}
		if (!(that.personalIIdZugewiesener == null ? this.personalIIdZugewiesener == null
				: that.personalIIdZugewiesener.equals(this.personalIIdZugewiesener))) {
			return false;
		}
		if (!(that.projekttypCNr == null ? this.projekttypCNr == null
				: that.projekttypCNr.equals(this.projekttypCNr))) {
			return false;
		}
		if (!(that.iPrio == null ? this.iPrio == null : that.iPrio.equals(this.iPrio))) {
			return false;
		}
		if (!(that.statusCNr == null ? this.statusCNr == null : that.statusCNr.equals(this.statusCNr))) {
			return false;
		}
		if (!(that.oAttachments == null ? this.oAttachments == null
				: Arrays.equals(that.oAttachments, this.oAttachments))) {
			return false;
		}
		if (!(that.cAttachmentsType == null ? this.cAttachmentsType == null
				: that.cAttachmentsType.equals(this.cAttachmentsType))) {
			return false;
		}
		if (!(that.xFreetext == null ? this.xFreetext == null : that.xFreetext.equals(this.xFreetext))) {
			return false;
		}
		if (!(that.tZielwunschdatum == null ? this.tZielwunschdatum == null
				: that.tZielwunschdatum.equals(this.tZielwunschdatum))) {
			return false;
		}
		if (!(that.partnerIId == null ? this.partnerIId == null : that.partnerIId.equals(this.partnerIId))) {
			return false;
		}
		if (!(that.ansprechpartnerIId == null ? this.ansprechpartnerIId == null
				: that.ansprechpartnerIId.equals(this.ansprechpartnerIId))) {
			return false;
		}
		if (!(that.iVerrechenbar == null ? this.iVerrechenbar == null
				: that.iVerrechenbar.equals(this.iVerrechenbar))) {
			return false;
		}
		if (!(that.bFreigegeben == null ? this.bFreigegeben == null : that.bFreigegeben.equals(this.bFreigegeben))) {
			return false;
		}
		if (!(that.personalIIdAnlegen == null ? this.personalIIdAnlegen == null
				: that.personalIIdAnlegen.equals(this.personalIIdAnlegen))) {
			return false;
		}
		if (!(that.tAnlegen == null ? this.tAnlegen == null : that.tAnlegen.equals(this.tAnlegen))) {
			return false;
		}
		if (!(that.dDauer == null ? this.dDauer == null : that.dDauer.equals(this.dDauer))) {
			return false;
		}
		if (!(that.tZeit == null ? this.tZeit == null : that.tZeit.equals(this.tZeit))) {
			return false;
		}
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern))) {
			return false;
		}
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern.equals(this.tAendern))) {
			return false;
		}
		if (!(that.mandantCNr == null ? this.mandantCNr == null : that.mandantCNr.equals(this.mandantCNr))) {
			return false;
		}
		if (!(that.cDateiname == null ? this.cDateiname == null : that.cDateiname.equals(this.cDateiname))) {
			return false;
		}
		if (!(that.tErledigt == null ? this.tErledigt == null : that.tErledigt.equals(this.tErledigt))) {
			return false;
		}
		if (!(that.personalIIdErlediger == null ? this.personalIIdErlediger == null
				: that.personalIIdErlediger.equals(this.personalIIdErlediger))) {
			return false;
		}
		if (!(that.iSort == null ? this.iSort == null : that.iSort.equals(this.iSort))) {
			return false;
		}

		if (!(that.deployNumber == null ? this.deployNumber == null : that.deployNumber.equals(this.deployNumber))) {
			return false;
		}
		if (!(that.buildNumber == null ? this.buildNumber == null : that.buildNumber.equals(this.buildNumber))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.cNr.hashCode();
		result = 37 * result + this.kategorieCNr.hashCode();
		result = 37 * result + this.cTitel.hashCode();
		result = 37 * result + this.personalIIdErzeuger.hashCode();
		result = 37 * result + this.personalIIdZugewiesener.hashCode();
		result = 37 * result + this.projekttypCNr.hashCode();
		result = 37 * result + this.iPrio.hashCode();
		result = 37 * result + this.statusCNr.hashCode();
		result = 37 * result + Arrays.hashCode(this.oAttachments);
		result = 37 * result + this.cAttachmentsType.hashCode();
		result = 37 * result + this.xFreetext.hashCode();
		result = 37 * result + this.tZielwunschdatum.hashCode();
		result = 37 * result + this.partnerIId.hashCode();
		result = 37 * result + this.ansprechpartnerIId.hashCode();
		result = 37 * result + this.mandantCNr.hashCode();
		result = 37 * result + this.iVerrechenbar.hashCode();
		result = 37 * result + this.bFreigegeben.hashCode();
		result = 37 * result + this.personalIIdAnlegen.hashCode();
		result = 37 * result + this.tAnlegen.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.dDauer.hashCode();
		result = 37 * result + this.tZeit.hashCode();
		result = 37 * result + this.cDateiname.hashCode();
		result = 37 * result + this.tErledigt.hashCode();
		result = 37 * result + this.personalIIdErlediger.hashCode();
		result = 37 * result + this.iSort.hashCode();
		result = 37 * result + deployNumber.hashCode();
		result = 37 * result + buildNumber.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + cNr;
		returnString += ", " + kategorieCNr;
		returnString += ", " + cTitel;
		returnString += ", " + personalIIdErzeuger;
		returnString += ", " + personalIIdZugewiesener;
		returnString += ", " + projekttypCNr;
		returnString += ", " + iPrio;
		returnString += ", " + statusCNr;
		returnString += ", " + mandantCNr;
		returnString += ", " + oAttachments;
		returnString += ", " + cDateiname;
		returnString += ", " + cAttachmentsType;
		returnString += ", " + xFreetext;
		returnString += ", " + tZielwunschdatum;
		returnString += ", " + partnerIId;
		returnString += ", " + ansprechpartnerIId;
		returnString += ", " + iVerrechenbar;
		returnString += ", " + bFreigegeben;
		returnString += ", " + personalIIdAnlegen;
		returnString += ", " + tAnlegen;
		returnString += ", " + personalIIdAendern;
		returnString += ", " + tAendern;
		returnString += ", " + dDauer;
		returnString += ", " + tZeit;
		returnString += ", " + tErledigt;
		returnString += ", " + personalIIdErlediger;
		returnString += ", " + iSort;
		return returnString;
	}

	public String getDeployNumber() {
		return deployNumber;
	}

	public void setDeployNumber(String deployNumber) {
		this.deployNumber = deployNumber;
	}

	public String getBuildNumber() {
		return buildNumber;
	}

	public void setBuildNumber(String buildNumber) {
		this.buildNumber = buildNumber;
	}

	public EditorContentIId getContentId() {
		return contentId;
	}

	public void setContentId(EditorContentIId contentId) {
		this.contentId = contentId;
	}
	
	public boolean hasContentId() {
		return this.getContentId() != null &&
				this.getContentId().isValid();
	}
}
