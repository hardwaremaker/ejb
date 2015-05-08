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
package com.lp.util;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

public class LPReportParameter implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String, Object> parameterI;
	private String modul;
	private String reportname;
	private String mandantCNr;
	private Locale sprache;
	private String idUser;
	private boolean bMitLogo;
	private Integer kostenstelleIId;

	public LPReportParameter(Map<String, Object> parameterI, String modul,
			String reportname, String mandantCNr, Locale sprache,
			String idUser, boolean bMitLogo, Integer kostenstelleIId) {
		this.parameterI = parameterI;
		this.modul = modul;
		this.reportname = reportname;
		this.mandantCNr = mandantCNr;
		this.sprache = sprache;
		this.idUser = idUser;
		this.bMitLogo = bMitLogo;
		this.kostenstelleIId = kostenstelleIId;
	}

	public boolean isBMitLogo() {
		return bMitLogo;
	}

	public String getIdUser() {
		return idUser;
	}

	public Integer getKostenstelleIId() {
		return kostenstelleIId;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public String getModul() {
		return modul;
	}

	public Map<String, Object> getParameterI() {
		return parameterI;
	}

	public String getReportname() {
		return reportname;
	}

	public Locale getSprache() {
		return sprache;
	}

	public void setBMitLogo(boolean BMitLogo) {
		this.bMitLogo = BMitLogo;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public void setKostenstelleIId(Integer kostenstelleIId) {
		this.kostenstelleIId = kostenstelleIId;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public void setModul(String modul) {
		this.modul = modul;
	}

	public void setParameterI(Map<String, Object> parameterI) {
		this.parameterI = parameterI;
	}

	public void setReportname(String reportname) {
		this.reportname = reportname;
	}

	public void setSprache(Locale sprache) {
		this.sprache = sprache;
	}
}
