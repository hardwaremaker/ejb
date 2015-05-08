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
import java.sql.Date;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

public class AutomatikjobDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private String cName;
	private Integer bActive;
	private Date dLastperformed;
	private Date dNextperform;
	private Integer iIntervall;
	private Integer bMonthjob;
	private Integer iSort;
	private Integer iAutomatikjobtypeIid;
	private String cBeschreibung;
	private Integer bPerformOnNonWOrkingDays;
	private String cMandantCNr;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCName() {
		return cName;
	}

	public void setCName(String cName) {
		this.cName = cName;
	}

	public Integer getBActive() {
		return bActive;
	}

	public void setBActive(Integer bActive) {
		this.bActive = bActive;
	}

	public Date getDLastperformed() {
		return dLastperformed;
	}

	public void setDLastperformed(Date dLastperformed) {
		this.dLastperformed = dLastperformed;
	}

	public Date getDNextperform() {
		return dNextperform;
	}

	public void setDNextperform(Date dNextperform) {
		this.dNextperform = dNextperform;
	}

	public Integer getIIntervall() {
		return iIntervall;
	}

	public void setIIntervall(Integer iIntervall) {
		this.iIntervall = iIntervall;
	}

	public Integer getBMonthjob() {
		return bMonthjob;
	}

	public void setBMonthjob(Integer bMonthjob) {
		this.bMonthjob = bMonthjob;
	}

	public Integer getISort() {
		return iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public Integer getIAutomatikjobtypeIid() {
		return iAutomatikjobtypeIid;
	}

	public void setIAutomatikjobtypeIid(Integer iAutomatikjobtypeIid) {
		this.iAutomatikjobtypeIid = iAutomatikjobtypeIid;
	}

	public String getCBeschreibung() {
		return cBeschreibung;
	}

	public void setCBeschreibung(String cBeschreibung) {
		this.cBeschreibung = cBeschreibung;
	}

	public Integer getBPerformOnNonWOrkingDays() {
		return bPerformOnNonWOrkingDays;
	}

	public void setBPerformOnNonWOrkingDays(Integer bPerformOnNonWOrkingDays) {
		this.bPerformOnNonWOrkingDays = bPerformOnNonWOrkingDays;
	}

	public PrintService getServerDefaultPrintService() {
		return PrintServiceLookup.lookupDefaultPrintService();
	}

	public PrintService[] getServerInstalledPrintServices() {
		return PrintServiceLookup.lookupPrintServices(null, null);
	}

	public String getCMandantCNr() {
		return cMandantCNr;
	}

	public void setCMandantCNr(String cMandantCNr) {
		this.cMandantCNr = cMandantCNr;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof AutomatikjobDto))
			return false;
		AutomatikjobDto that = (AutomatikjobDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId)))
			return false;
		if (!(that.cName == null ? this.cName == null : that.cName
				.equals(this.cName)))
			return false;
		if (!(that.cBeschreibung == null ? this.cBeschreibung == null
				: that.cBeschreibung.equals(this.cBeschreibung)))
			return false;
		if (!(that.bActive == null ? this.bActive == null : that.bActive
				.equals(this.bActive)))
			return false;
		if (!(that.dLastperformed == null ? this.dLastperformed == null
				: that.dLastperformed.equals(this.dLastperformed)))
			return false;
		if (!(that.dNextperform == null ? this.dNextperform == null
				: that.dNextperform.equals(this.dNextperform)))
			return false;
		if (!(that.iIntervall == null ? this.iIntervall == null
				: that.iIntervall.equals(this.iIntervall)))
			return false;
		if (!(that.bMonthjob == null ? this.bMonthjob == null : that.bMonthjob
				.equals(this.bMonthjob)))
			return false;
		if (!(that.iSort == null ? this.iSort == null : that.iSort
				.equals(this.iSort)))
			return false;
		if (!(that.iAutomatikjobtypeIid == null ? this.iAutomatikjobtypeIid == null
				: that.iAutomatikjobtypeIid.equals(this.iAutomatikjobtypeIid)))
			return false;
		if (!(that.bPerformOnNonWOrkingDays == null ? this.bPerformOnNonWOrkingDays == null
				: that.bPerformOnNonWOrkingDays
						.equals(this.bPerformOnNonWOrkingDays)))
			return false;
		if (!(that.cMandantCNr == null ? this.cMandantCNr == null
				: that.cMandantCNr.equals(this.cMandantCNr)))
			return false;
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.cName.hashCode();
		result = 37 * result + this.cBeschreibung.hashCode();
		result = 37 * result + this.bActive.hashCode();
		result = 37 * result + this.dLastperformed.hashCode();
		result = 37 * result + this.dNextperform.hashCode();
		result = 37 * result + this.iIntervall.hashCode();
		result = 37 * result + this.bMonthjob.hashCode();
		result = 37 * result + this.iSort.hashCode();
		result = 37 * result + this.iAutomatikjobtypeIid.hashCode();
		result = 37 * result + this.bPerformOnNonWOrkingDays.hashCode();
		result = 37 * result + this.cMandantCNr.hashCode();
		return result;
	}

	public String toString() {
		StringBuffer returnStringBuffer = new StringBuffer(384);
		returnStringBuffer.append("[");
		returnStringBuffer.append("iId:").append(iId);
		returnStringBuffer.append("cName:").append(cName);
		returnStringBuffer.append("cBeschreibung:").append(cBeschreibung);
		returnStringBuffer.append("bActive:").append(bActive);
		returnStringBuffer.append("dLastperformed:").append(dLastperformed);
		returnStringBuffer.append("dNextperform:").append(dNextperform);
		returnStringBuffer.append("iIntervall:").append(iIntervall);
		returnStringBuffer.append("bMonthjob:").append(bMonthjob);
		returnStringBuffer.append("iSort:").append(iSort);
		returnStringBuffer.append("iAutomatikjobtypeIid:").append(
				iAutomatikjobtypeIid);
		returnStringBuffer.append("bPerformOnNonWOrkingDays:").append(
				bPerformOnNonWOrkingDays);
		returnStringBuffer.append("cMandantCNr:").append(cMandantCNr);
		returnStringBuffer.append("]");
		return returnStringBuffer.toString();
	}
}
