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

public class FibuFehlerDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String belegartCNr;
	private Integer belegIId;
	private String belegCNr;
	private int fehlercode;
	private String fehlerText;
	
	public static final int FEHLER_STATUS = 1;
	public static final int FEHLER_NICHT_IN_FIBU = 2;
	public static final int FEHLER_NICHT_VOLLSTAENDIG_KONTIERT = 3;
	public static final int FEHLER_KURS = 4;
	
	public FibuFehlerDto(String belegartCNr, Integer belegIId, String belegCNr, int fehlercode) {
		this.belegartCNr = belegartCNr;
		this.belegIId = belegIId;
		this.belegCNr = belegCNr;
		this.fehlercode = fehlercode;
		switch (fehlercode) {
		case FEHLER_STATUS:
			this.fehlerText = "Beleg hat falschen Status";
		break ;
		
		case FEHLER_NICHT_IN_FIBU:
			this.fehlerText = "Beleg ist nicht in Fibu \u00FCbernommen";
		break ;
		
		case FEHLER_NICHT_VOLLSTAENDIG_KONTIERT:
			this.fehlerText = "Beleg ist nicht vollst\u00E4ndig kontiert";
		break ;
		
		case FEHLER_KURS:
			this.fehlerText = "Kurs falsch";
		break ;
		}
	}
	
	public String getBelegartCNr() {
		return belegartCNr;
	}
	public void setBelegartCNr(String belegartCNr) {
		this.belegartCNr = belegartCNr;
	}
	public Integer getBelegIId() {
		return belegIId;
	}
	public void setBelegIId(Integer belegIId) {
		this.belegIId = belegIId;
	}
	public String getBelegCNr() {
		return belegCNr;
	}
	public void setBelegCNr(String belegCNr) {
		this.belegCNr = belegCNr;
	}
	public int getFehlercode() {
		return fehlercode;
	}
	public void setFehlercode(int fehlercode) {
		this.fehlercode = fehlercode;
	}
	public String getFehlerText() {
		return fehlerText;
	}
	public void setFehlerText(String fehlerText) {
		this.fehlerText = fehlerText;
	}

}
