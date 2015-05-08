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
package com.lp.server.system.service;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Arrays;

public class AnwenderDto extends CryptDto implements Serializable, ICryptDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String mandantCNrHauptmandant = null;
	private Integer iId = null;
	private Timestamp tAendern = null;
	private Integer personalIIdAendern = null;

	private Integer iBuildnummerClienVon = null;
	private Integer iBuildnummerClientBis = null;

	private String cVersionServer = null;
	private Integer iBuildnummerServer = null;
	private Integer iBuildnummerServerVon = null;
	private Integer iBuildnummerServerBis = null;

	private String cVersionDB = null;
	private Integer iBuildnummerDB = null;

	private Timestamp tAblauf = null;
	
	transient private byte[] oCode = null;
	transient private byte[] oHash = null;
	private Timestamp tSubscription;
	
	public String getMandantCNrHauptmandant() {
		return mandantCNrHauptmandant;
	}

	public Integer getIId() {
		return iId;
	}

	public void setMandantCNrHauptmandant(String mandantCNrHauptmandant) {
		this.mandantCNrHauptmandant = mandantCNrHauptmandant;
	}

	public void setIId(Integer iIdI) {
		this.iId = iIdI;
	}

	public Integer getIBuildnummerClientBis() {
		return iBuildnummerClientBis;
	}

	public void setIBuildnummerClientBis(Integer iBuildnummerClientBis) {
		this.iBuildnummerClientBis = iBuildnummerClientBis;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Integer getIBuildnummerClienVon() {
		return iBuildnummerClienVon;
	}

	public void setIBuildnummerClienVon(Integer iBuildnummerClienVon) {
		this.iBuildnummerClienVon = iBuildnummerClienVon;
	}

	public Integer getIBuildnummerServerBis() {
		return iBuildnummerServerBis;
	}

	public void setIBuildnummerServerBis(Integer iBuildnummerServerBis) {
		this.iBuildnummerServerBis = iBuildnummerServerBis;
	}

	public Integer getIBuildnummerServerVon() {
		return iBuildnummerServerVon;
	}

	public void setIBuildnummerServerVon(Integer iBuildnummerServerVon) {
		this.iBuildnummerServerVon = iBuildnummerServerVon;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public Integer getIBuildnummerDB() {
		return iBuildnummerDB;
	}

	public String getCVersionServer() {
		return cVersionServer;
	}

	public String getCVersionDB() {
		return cVersionDB;
	}

	public Integer getIBuildnummerServer() {
		return iBuildnummerServer;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public void setIBuildnummerDB(Integer iVersionDB) {
		this.iBuildnummerDB = iVersionDB;
	}

	public void setCVersionServer(String cVersionServer) {
		this.cVersionServer = cVersionServer;
	}

	public void setCVersionDB(String cVersionDB) {
		this.cVersionDB = cVersionDB;
	}

	public void setIBuildnummerServer(Integer iBuildnummerServer) {
		this.iBuildnummerServer = iBuildnummerServer;
	}

	public void setTAblauf(Timestamp tAblauf) {
		this.tAblauf = tAblauf;
	}
	
	public Timestamp getTAblauf() {
		return tAblauf;
	}

	public void setTSubscription(Timestamp tSubscription) {
		this.tSubscription = tSubscription;
	}
	
	public Timestamp getTSubscription() {
		return tSubscription;
	}

	public void setOCode(byte[] oCode) {
		this.oCode = oCode;
	}

	public void setOHash(byte[] oHash) {
		this.oHash = oHash;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof AnwenderDto)) {
			return false;
		}
		AnwenderDto that = (AnwenderDto) obj;
		if (!(that.mandantCNrHauptmandant == null ? this.mandantCNrHauptmandant == null
				: that.mandantCNrHauptmandant
						.equals(this.mandantCNrHauptmandant))) {
			return false;
		}
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId))) {
			return false;
		}
		if (!(that.iBuildnummerClientBis == null ? this.iBuildnummerClientBis == null
				: that.iBuildnummerClientBis.equals(this.iBuildnummerClientBis))) {
			return false;
		}
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern
				.equals(this.tAendern))) {
			return false;
		}
		if (!(that.iBuildnummerClienVon == null ? this.iBuildnummerClienVon == null
				: that.iBuildnummerClienVon.equals(this.iBuildnummerClienVon))) {
			return false;
		}
		if (!(that.iBuildnummerServerBis == null ? this.iBuildnummerServerBis == null
				: that.iBuildnummerServerBis.equals(this.iBuildnummerServerBis))) {
			return false;
		}
		if (!(that.iBuildnummerServerVon == null ? this.iBuildnummerServerVon == null
				: that.iBuildnummerServerVon.equals(this.iBuildnummerServerVon))) {
			return false;
		}
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern))) {
			return false;
		}
		if (!(that.tAblauf == null ? this.tAblauf == null
				: that.tAblauf.equals(this.tAblauf))) {
			return false;
		}
		if (!(that.oCode == null ? this.oCode == null
				: Arrays.equals(that.oCode, this.oCode))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.mandantCNrHauptmandant.hashCode();
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.iBuildnummerClientBis.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.iBuildnummerClienVon.hashCode();
		result = 37 * result + this.iBuildnummerServerBis.hashCode();
		result = 37 * result + this.iBuildnummerServerVon.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.tAblauf.hashCode();
		result = 37 * result + Arrays.hashCode(this.oCode);
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += mandantCNrHauptmandant;
		returnString += ", " + iId;
		returnString += ", " + iBuildnummerClientBis;
		returnString += ", " + tAendern;
		returnString += ", " + iBuildnummerClienVon;
		returnString += ", " + iBuildnummerServerBis;
		returnString += ", " + iBuildnummerServerVon;
		returnString += ", " + personalIIdAendern;
		returnString += ", " + tAblauf;
		return returnString;
	}

	@Override
	public String toValidateString() {
		String returnString = "";
		returnString += mandantCNrHauptmandant;
		returnString += iId;
		returnString += tAblauf;
		returnString += Arrays.hashCode(oCode);
		return returnString;
	}
	
	public byte[] toValidateBytes() {
		byte[] ba = null;
		try {
			ba = toValidateString().getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return ba;
	}

	@Override
	public boolean validate() {
		if (this.oHash == null) return false;
		byte[] baDecode = super.decodeRSA(this.oHash, this.oCode);
		if (baDecode == null) return false;
		String sDecode;
		try {
			sDecode = new String(baDecode, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			sDecode = new String(baDecode);
		}
		if (sDecode.compareTo(this.toValidateString())==0) return true;
		return false;
	}


}
