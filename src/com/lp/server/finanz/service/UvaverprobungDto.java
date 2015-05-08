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
package com.lp.server.finanz.service;

import java.io.Serializable;
import java.sql.Timestamp;

public class UvaverprobungDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer iId;
	private Integer iGeschaeftsjahr;
	private Integer iMonat;
	private String mandantCNr;
	private Integer finanzamtIId;
	private Timestamp tAnlegen;
	private Integer personalIIdAnlegen;
	private int iAbrechnungszeitraum;
	
	// OFFSET fuer Feld I_MONAT in FB_UVAVERPROBUNG
	public final static int UVAABRECHNUNGSZEITRAUM_MONAT = 0;
	public final static int UVAABRECHNUNGSZEITRAUM_QUARTAL = 100;
	public final static int UVAABRECHNUNGSZEITRAUM_JAHR = 200;
	
	public Integer getIId() {
		return iId;
	}
	public void setIId(Integer iId) {
		this.iId = iId;
	}
	public String getMandantCNr() {
		return mandantCNr;
	}
	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}
	public void setIGeschaeftsjahr(Integer iGeschaeftsjahr) {
		this.iGeschaeftsjahr = iGeschaeftsjahr;
	}
	public Integer getIGeschaeftsjahr() {
		return iGeschaeftsjahr;
	}
	public void setIMonat(Integer iMonat) {
		this.iMonat = iMonat;
	}
	public Integer getIMonat() {
		return iMonat;
	}
	public void setFinanzamtIId(Integer finanzamtIId) {
		this.finanzamtIId = finanzamtIId;
	}
	public Integer getFinanzamtIId() {
		return finanzamtIId;
	}
	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}
	public Timestamp getTAnlegen() {
		return tAnlegen;
	}
	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}
	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}
	public String toInfo() {
		if (iAbrechnungszeitraum == UVAABRECHNUNGSZEITRAUM_JAHR)
			return "" + iGeschaeftsjahr + " " + tAnlegen.toString().substring(0, tAnlegen.toString().lastIndexOf("."));
		else if (iAbrechnungszeitraum == UVAABRECHNUNGSZEITRAUM_QUARTAL)
			return "" + iGeschaeftsjahr + "/Q" + iMonat + " " + tAnlegen.toString().substring(0, tAnlegen.toString().lastIndexOf("."));
		else
			return "" + iGeschaeftsjahr + "/" + iMonat + " " + tAnlegen.toString().substring(0, tAnlegen.toString().lastIndexOf("."));
	}
	public void setIAbrechnungszeitraum(int iAbrechnungszeitraum) {
		this.iAbrechnungszeitraum = iAbrechnungszeitraum;
	}
	public int getIAbrechnungszeitraum() {
		return iAbrechnungszeitraum;
	}
	
	public static final int getAbrechnungszeitraumFromString(String abrechnungsZeitraum) {
		if (abrechnungsZeitraum.equals(FinanzFac.UVA_ABRECHNUNGSZEITRAUM_JAHR))
			return UVAABRECHNUNGSZEITRAUM_JAHR;
		else if (abrechnungsZeitraum.equals(FinanzFac.UVA_ABRECHNUNGSZEITRAUM_QUARTAL))
			return UVAABRECHNUNGSZEITRAUM_QUARTAL;
		else 
			return UVAABRECHNUNGSZEITRAUM_MONAT;
		
	}
}
