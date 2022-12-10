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

/**
 * 
 * @author andi
 *
 */
public class EJBSepaImportExceptionLP extends EJBExceptionLP {

	private static final long serialVersionUID = 1L;
	
	public static final int SEVERITY_INFO = 1;
	public static final int SEVERITY_WARNING = 2;
	public static final int SEVERITY_ERROR = 3;
	
	private String camt053Version;
	private Integer severity;
	private String country;

	public EJBSepaImportExceptionLP(String version, String country, Integer severity, EJBExceptionLP e) {
		super(e);
		setCamt053Version(version);
		setSeverity(severity);
		setCountry(country);
	}
	
	public EJBSepaImportExceptionLP(Integer severity, EJBExceptionLP e) {
		super(e);
		setSeverity(severity);
	}
	
	public EJBSepaImportExceptionLP(String version, String country, int iCodeI, Integer severity, EJBExceptionLP e) {
		super(iCodeI, e);
		setCamt053Version(version);
		setSeverity(severity);
	}
	
	public EJBSepaImportExceptionLP(String version, String country, int iCodeI, Integer severity, String sTextForException) {
		super(iCodeI, sTextForException);
		setCamt053Version(version);
		setSeverity(severity);
		setCountry(country);
	}

	public EJBSepaImportExceptionLP(int iCodeI, Integer severity, String sTextForException) {
		super(iCodeI, sTextForException);
		setSeverity(severity);
	}
	
	public EJBSepaImportExceptionLP(int iCodeI, Integer severity, Object... alInfoForTheClient) {
		super(iCodeI, "", alInfoForTheClient);
		setSeverity(severity);
	}

	public String getCamt053Version() {
		return camt053Version;
	}

	public void setCamt053Version(String camt053Version) {
		this.camt053Version = camt053Version;
	}

	public Integer getSeverity() {
		return severity;
	}

	public void setSeverity(Integer severity) {
		this.severity = severity;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

}
