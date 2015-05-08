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
import java.sql.Timestamp;

public class PartnerkommentarDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Integer partnerIId;
	private Integer partnerkommentarartIId;

	private String xKommentar;

	private Integer personalIIdAendern;
	private Timestamp tAendern;
	private Timestamp tFiledatum;

	public Timestamp getTAendern() {
		return this.tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Timestamp getTFiledatum() {
		return tFiledatum;
	}

	public void setTFiledatum(Timestamp tFiledatum) {
		this.tFiledatum = tFiledatum;
	}

	private byte[] oMedia;
	private String cDateiname;

	public String getXKommentar() {
		return xKommentar;
	}

	public byte[] getOMedia() {
		return oMedia;
	}

	public String getCDateiname() {
		return cDateiname;
	}

	public void setXKommentar(String xKommentar) {
		this.xKommentar = xKommentar;
	}

	public void setOMedia(byte[] oMedia) {
		this.oMedia = oMedia;
	}

	public void setCDateiname(String cDateiname) {
		this.cDateiname = cDateiname;
	}

	private PartnerkommentardruckDto[] partnerkommentardruckDto;
	private Integer iSort;

	public Integer getIId() {
		return iId;
	}

	private String datenformatCNr;
	private Short bKunde;
	private Integer iArt;

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getPartnerIId() {
		return partnerIId;
	}

	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId;
	}

	public Integer getPartnerkommentarartIId() {
		return partnerkommentarartIId;
	}

	public PartnerkommentardruckDto[] getPartnerkommentardruckDto() {
		return partnerkommentardruckDto;
	}

	public String getDatenformatCNr() {
		return datenformatCNr;
	}

	public Short getBKunde() {
		return bKunde;
	}

	public Integer getIArt() {
		return this.iArt;
	}

	public void setIArt(Integer iArt) {
		this.iArt = iArt;
	}

	public void setPartnerkommentarartIId(Integer partnerkommentarartIId) {
		this.partnerkommentarartIId = partnerkommentarartIId;
	}

	public void setPartnerkommentardruckDto(
			PartnerkommentardruckDto[] partnerkommentardruckDto) {
		this.partnerkommentardruckDto = partnerkommentardruckDto;
	}

	public void setDatenformatCNr(String datenformatCNr) {
		this.datenformatCNr = datenformatCNr;
	}

	public void setBKunde(Short bKunde) {
		this.bKunde = bKunde;
	}

	public Integer getISort() {
		return iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

}
