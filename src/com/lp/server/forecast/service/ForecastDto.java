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
package com.lp.server.forecast.service;

import java.io.Serializable;

import javax.persistence.Column;

public class ForecastDto implements Serializable {
	private Integer iId;
	private String cNr;
	private String cProjekt;

	public String getCNr() {
		return cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public String getCProjekt() {
		return cProjekt;
	}

	public void setCProjekt(String cProjekt) {
		this.cProjekt = cProjekt;
	}

	private Integer iTageCod;
	private Integer iWochenCow;
	public Integer getITageCod() {
		return iTageCod;
	}

	public void setITageCod(Integer iTageCod) {
		this.iTageCod = iTageCod;
	}

	public Integer getIWochenCow() {
		return iWochenCow;
	}

	public void setIWochenCow(Integer iWochenCow) {
		this.iWochenCow = iWochenCow;
	}

	public Integer getIMonateFca() {
		return iMonateFca;
	}

	public void setIMonateFca(Integer iMonateFca) {
		this.iMonateFca = iMonateFca;
	}

	private Integer iTageGueltig;
	
	public Integer getITageGueltig() {
		return iTageGueltig;
	}

	public void setITageGueltig(Integer iTageGueltig) {
		this.iTageGueltig = iTageGueltig;
	}

	
	private Integer iMonateFca;
	
	public String getStatusCNr() {
		return statusCNr;
	}

	public void setStatusCNr(String statusCNr) {
		this.statusCNr = statusCNr;
	}

	public Integer getKundeIId() {
		return kundeIId;
	}

	public void setKundeIId(Integer kundeIId) {
		this.kundeIId = kundeIId;
	}

	private String statusCNr;

	

	private Integer kundeIId;

	private static final long serialVersionUID = 1L;

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	private String cPfadForecastauftrag;

	public String getCPfadForecastauftrag() {
		return cPfadForecastauftrag;
	}

	public void setCPfadForecastauftrag(String cPfadForecastauftrag) {
		this.cPfadForecastauftrag = cPfadForecastauftrag;
	}

	public String getCPfadCow() {
		return cPfadCow;
	}

	public void setCPfadCow(String cPfadCow) {
		this.cPfadCow = cPfadCow;
	}

	public String getCPfadCod() {
		return cPfadCod;
	}

	public void setCPfadCod(String cPfadCod) {
		this.cPfadCod = cPfadCod;
	}

	public String getCPfadLinienabruf() {
		return cPfadLinienabruf;
	}

	public void setCPfadLinienabruf(String cPfadLinienabruf) {
		this.cPfadLinienabruf = cPfadLinienabruf;
	}

	public String getImportdefCNrForecastauftrag() {
		return importdefCNrForecastauftrag;
	}

	public void setImportdefCNrForecastauftrag(
			String importdefCNrForecastauftrag) {
		this.importdefCNrForecastauftrag = importdefCNrForecastauftrag;
	}

	public String getImportdefCNrCow() {
		return importdefCNrCow;
	}

	public void setImportdefCNrCow(String importdefCNrCow) {
		this.importdefCNrCow = importdefCNrCow;
	}

	public String getImportdefCNrCod() {
		return importdefCNrCod;
	}

	public void setImportdefCNrCod(String importdefCNrCod) {
		this.importdefCNrCod = importdefCNrCod;
	}

	public String getImportdefCNrLinienabruf() {
		return importdefCNrLinienabruf;
	}

	public void setImportdefCNrLinienabruf(String importdefCNrLinienabruf) {
		this.importdefCNrLinienabruf = importdefCNrLinienabruf;
	}

	private String cPfadCow;
	private String cPfadCod;
	private String cPfadLinienabruf;

	private String importdefCNrForecastauftrag;
	private String importdefCNrCow;
	private String importdefCNrCod;
	private String importdefCNrLinienabruf;

}
