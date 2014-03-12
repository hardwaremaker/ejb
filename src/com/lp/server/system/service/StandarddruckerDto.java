/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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
import java.util.Locale;

/**
 * 
 * <p>
 * Diese Klasse kuemmert sich um ...
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 25.03.06
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2010/02/26 09:37:24 $
 */
public class StandarddruckerDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String cPc;
	private String cReportname;
	private String cDrucker;
	private String mandantCNr;
	private Integer reportvarianteIId;
	private Short bStandard;
	
	public Short getBStandard() {
		return bStandard;
	}

	public void setBStandard(Short standard) {
		bStandard = standard;
	}

	public Integer getReportvarianteIId() {
		return reportvarianteIId;
	}

	public void setReportvarianteIId(Integer reportvarianteIId) {
		this.reportvarianteIId = reportvarianteIId;
	}

	// folgende Felder sind nicht in der DB enthalten:
	private Integer kostenstelleIId_notInDB;
	private String sModul_notInDB;
	private String sFilename_notInDB;
	private Locale locale_notInDB;
	

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}
	
	

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public String getCPc() {
		return cPc;
	}

	public void setCPc(String cPc) {
		this.cPc = cPc;
	}

	public String getCReportname() {
		return cReportname;
	}

	public void setCReportname(String cReportname) {
		this.cReportname = cReportname;
	}

	public String getCDrucker() {
		return cDrucker;
	}

	public void setCDrucker(String cDrucker) {
		this.cDrucker = cDrucker;
	}

	public Integer getKostenstelleIId_notInDB() {
		return kostenstelleIId_notInDB;
	}

	public void setKostenstelleIId_notInDB(Integer kostenstelleIId) {
		this.kostenstelleIId_notInDB = kostenstelleIId;
	}

	public String getSModul_notInDB() {
		return sModul_notInDB;
	}

	public void setSModul_notInDB(String sModul_notInDB) {
		this.sModul_notInDB = sModul_notInDB;
	}

	public String getSFilename_notInDB() {
		return sFilename_notInDB;
	}

	public void setSFilename_notInDB(String sFilename_notInDB) {
		this.sFilename_notInDB = sFilename_notInDB;
	}

	public Locale getLocale_notInDB() {
		return locale_notInDB;
	}

	public void setLocale_notInDB(Locale localeCNr_notInDB) {
		this.locale_notInDB = localeCNr_notInDB;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof StandarddruckerDto))
			return false;
		StandarddruckerDto that = (StandarddruckerDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.cPc == null ? this.cPc == null : that.cPc.equals(this.cPc)))
			return false;
		if (!(that.cReportname == null ? this.cReportname == null
				: that.cReportname.equals(this.cReportname)))
			return false;
		if (!(that.cDrucker == null ? this.cDrucker == null : that.cDrucker
				.equals(this.cDrucker)))
			return false;
		if (!(that.mandantCNr == null ? this.mandantCNr == null : that.mandantCNr
				.equals(this.mandantCNr)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.cPc.hashCode();
		result = 37 * result + this.cReportname.hashCode();
		result = 37 * result + this.cDrucker.hashCode();
		result = 37 * result + this.mandantCNr.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", " + cPc;
		returnString += ", " + cReportname;
		returnString += ", " + cDrucker;
		returnString += ", " + mandantCNr;
		return returnString;
	}
}
