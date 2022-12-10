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
package com.lp.server.personal.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;

import com.lp.server.fertigung.service.LossollarbeitsplanDto;

public class ZeitverteilungDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iId;
	private Integer personalIId;
	private Timestamp tZeit;
	private Integer artikelIId;
	private Integer losIId;

	public static ZeitverteilungDto clone(ZeitverteilungDto orig) {
		ZeitverteilungDto klon = new ZeitverteilungDto();
		klon.setArtikelIId(orig.getArtikelIId());
		klon.setBdSummeGutSchlecht_NOT_IN_DB(orig.getBdSummeGutSchlecht_NOT_IN_DB());
		klon.setBRuesten(orig.isbRuesten());
		klon.setBVerteilt(orig.getBVerteilt());
		klon.setIIdBlock(orig.getIIdBlock());
		klon.setLosIId(orig.getLosIId());
		klon.setLossollarbeitsplanIId(orig.getLossollarbeitsplanIId());
		klon.setlZeitproLos_notInDB(orig.getlZeitproLos_notInDB());
		klon.setMaschineIId(orig.getMaschineIId());
		klon.setPersonalIId(orig.getPersonalIId());

		klon.setTZeit(orig.getTZeit());
		
		return klon;
	}

	
	private long lZeitproLos_notInDB = 0;

	public long getlZeitproLos_notInDB() {
		return lZeitproLos_notInDB;
	}
	
	LossollarbeitsplanDto[] zusaetzlicheAGs_NOT_IN_DB=null;
	
	public boolean bRuesten=false;

	public boolean isbRuesten() {
		return bRuesten;
	}

	public void setBRuesten(boolean bRuesten) {
		this.bRuesten = bRuesten;
	}

	public BigDecimal bdSummeGutSchlecht_NOT_IN_DB=null;
	
	public BigDecimal getBdSummeGutSchlecht_NOT_IN_DB() {
		return bdSummeGutSchlecht_NOT_IN_DB;
	}

	public void setBdSummeGutSchlecht_NOT_IN_DB(BigDecimal bdSummeGutSchlecht_NOT_IN_DB) {
		this.bdSummeGutSchlecht_NOT_IN_DB = bdSummeGutSchlecht_NOT_IN_DB;
	}

	public LossollarbeitsplanDto[] getZusaetzlicheAGs_NOT_IN_DB() {
		return zusaetzlicheAGs_NOT_IN_DB;
	}

	public void setZusaetzlicheAGs_NOT_IN_DB(LossollarbeitsplanDto[] zusaetzlicheAGs_NOT_IN_DB) {
		this.zusaetzlicheAGs_NOT_IN_DB = zusaetzlicheAGs_NOT_IN_DB;
	}

	

	public void setlZeitproLos_notInDB(long lZeitproLos_notInDB) {
		this.lZeitproLos_notInDB = lZeitproLos_notInDB;
	}

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getPersonalIId() {
		return personalIId;
	}

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public void setPersonalIId(Integer personalIId) {
		this.personalIId = personalIId;
	}

	public Timestamp getTZeit() {
		return tZeit;
	}

	public void setTZeit(Timestamp tZeit) {
		this.tZeit = tZeit;
	}

	public Integer getLosIId() {
		return losIId;
	}

	public void setLosIId(Integer losIId) {
		this.losIId = losIId;
	}

	private Integer maschineIId;

	public Integer getMaschineIId() {
		return this.maschineIId;
	}

	public void setMaschineIId(Integer maschineIId) {
		this.maschineIId = maschineIId;
	}

	private Integer lossollarbeitsplanIId;

	public Integer getLossollarbeitsplanIId() {
		return lossollarbeitsplanIId;
	}

	public void setLossollarbeitsplanIId(Integer lossollarbeitsplanIId) {
		this.lossollarbeitsplanIId = lossollarbeitsplanIId;
	}

	private Short bVerteilt;

	public Short getBVerteilt() {
		return bVerteilt;
	}

	public void setBVerteilt(Short bVerteilt) {
		this.bVerteilt = bVerteilt;
	}

	private Integer iIdBlock;

	public Integer getIIdBlock() {
		return iIdBlock;
	}

	public void setIIdBlock(Integer iIdBlock) {
		this.iIdBlock = iIdBlock;
	}

	private Integer zeitdatenIIdUmgewandelt;

	public Integer getZeitdatenIIdUmgewandelt() {
		return zeitdatenIIdUmgewandelt;
	}

	public void setZeitdatenIIdUmgewandelt(Integer zeitdatenIIdUmgewandelt) {
		this.zeitdatenIIdUmgewandelt = zeitdatenIIdUmgewandelt;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ZeitverteilungDto))
			return false;
		ZeitverteilungDto that = (ZeitverteilungDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.personalIId == null ? this.personalIId == null
				: that.personalIId.equals(this.personalIId)))
			return false;
		if (!(that.tZeit == null ? this.tZeit == null : that.tZeit
				.equals(this.tZeit)))
			return false;
		if (!(that.losIId == null ? this.losIId == null : that.losIId
				.equals(this.losIId)))
			return false;

		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.personalIId.hashCode();
		result = 37 * result + this.tZeit.hashCode();
		result = 37 * result + this.losIId.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + personalIId;
		returnString += ", " + losIId;
		returnString += ", " + artikelIId;
		returnString += ", " + tZeit;
		return returnString;
	}
}
