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
package com.lp.server.stueckliste.service;

import java.io.Serializable;
import java.math.BigDecimal;

public class StuecklisteMitStrukturDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private StuecklistepositionDto stuecklistepositionDto;
	private int iEbene;
	private boolean bStueckliste = false;
	private boolean bIstFremdfertigung = false; // nicht auf DB; fuer AG

	private boolean bMaschinenzeit = false;
	private boolean bArbeitszeit = false;
	private BigDecimal durchlaufzeit = null;
	private StuecklisteDto stuecklisteDto = null;
	private java.util.Date tFreigabe = null;
	private String cKurzzeichenPersonFreigabe = null;

	private String reportDebug;
	private String reportInfo;
	private String reportWarn;
	private String reportError;
	
	public StuecklisteMitStrukturDto clone() {
		StuecklisteMitStrukturDto klon = new StuecklisteMitStrukturDto(
				this.getIEbene(), this.getStuecklistepositionDto());

		klon.setBArbeitszeit(this.isBArbeitszeit());
		klon.setBIstFremdfertigung(this.isBFremdfertigung());
		klon.setBMaschinenzeit(this.isBMaschinenzeit());
		klon.setBStueckliste(this.isBStueckliste());
		klon.setCKurzzeichenPersonFreigabe(this.getCKurzzeichenPersonFreigabe());
		klon.setDurchlaufzeit(this.getDurchlaufzeit());
		klon.setIAnzahlArbeitsschritte(this.getiAnzahlArbeitsschritte());
		klon.setIEbene(this.getIEbene());
		klon.setMandantCNr(this.getMandantCNr());
		klon.setMandantCKbez(this.getMandantCKbez());
		klon.setStuecklistearbeitsplanDto(this.getStuecklistearbeitsplanDto());
		klon.setStuecklisteDto(this.getStuecklisteDto());

		klon.setStuecklistepositionDto(this.getStuecklistepositionDto());
		klon.setTFreigabe(this.getTFreigabe());
		klon.setReportDebug(this.getReportDebug());
		klon.setReportInfo(this.getReportInfo());
		klon.setReportWarn(this.getReportWarn());
		klon.setReportError(this.getReportError());
		
		return klon;
	}

	private String mandantCKbez = null;
	
	public String getMandantCKbez() {
		return mandantCKbez;
	}

	public void setMandantCKbez(String mandantCKbez) {
		this.mandantCKbez = mandantCKbez;
	}

	private String mandantCNr = null;

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	private Integer iAnzahlArbeitsschritte = null;

	public Integer getiAnzahlArbeitsschritte() {
		return iAnzahlArbeitsschritte;
	}

	public void setIAnzahlArbeitsschritte(Integer iAnzahlArbeitsschritte) {
		this.iAnzahlArbeitsschritte = iAnzahlArbeitsschritte;
	}

	public String getCKurzzeichenPersonFreigabe() {
		return cKurzzeichenPersonFreigabe;
	}

	public void setCKurzzeichenPersonFreigabe(String cKurzzeichenPersonFreigabe) {
		this.cKurzzeichenPersonFreigabe = cKurzzeichenPersonFreigabe;
	}

	public java.util.Date getTFreigabe() {
		return tFreigabe;
	}

	public void setTFreigabe(java.util.Date tFreigabe) {
		this.tFreigabe = tFreigabe;
	}

	public StuecklisteDto getStuecklisteDto() {
		return stuecklisteDto;
	}

	public void setStuecklisteDto(StuecklisteDto stuecklisteDto) {
		this.stuecklisteDto = stuecklisteDto;
	}

	public StuecklisteMitStrukturDto(int iEbene,
			StuecklistepositionDto stuecklistepositionDto) {
		this.iEbene = iEbene;
		this.stuecklistepositionDto = stuecklistepositionDto;
	}

	private StuecklistearbeitsplanDto stuecklistearbeitsplanDto;

	public StuecklistepositionDto getStuecklistepositionDto() {
		return stuecklistepositionDto;
	}

	public int getIEbene() {
		return iEbene;
	}

	public boolean isBStueckliste() {
		return bStueckliste;
	}

	public boolean isBArbeitszeit() {
		return bArbeitszeit;
	}

	public StuecklistearbeitsplanDto getStuecklistearbeitsplanDto() {
		return stuecklistearbeitsplanDto;
	}

	public boolean isBMaschinenzeit() {
		return bMaschinenzeit;
	}

	public BigDecimal getDurchlaufzeit() {
		return durchlaufzeit;
	}

	public boolean isBFremdfertigung() {
		return this.bIstFremdfertigung;
	}

	public void setStuecklistepositionDto(
			StuecklistepositionDto stuecklistepositionDto) {
		this.stuecklistepositionDto = stuecklistepositionDto;
	}

	public void setIEbene(int iEbene) {
		this.iEbene = iEbene;
	}

	public void setBStueckliste(boolean bStueckliste) {
		this.bStueckliste = bStueckliste;
	}

	public void setBIstFremdfertigung(boolean bFremdIstfertiungI) {
		this.bIstFremdfertigung = bFremdIstfertiungI;
	}

	public void setBArbeitszeit(boolean bArbeitszeit) {
		this.bArbeitszeit = bArbeitszeit;
	}

	public void setStuecklistearbeitsplanDto(
			StuecklistearbeitsplanDto stuecklistearbeitsplanDto) {
		this.stuecklistearbeitsplanDto = stuecklistearbeitsplanDto;
	}

	public void setBMaschinenzeit(boolean bMaschinenzeit) {
		this.bMaschinenzeit = bMaschinenzeit;
	}

	public void setDurchlaufzeit(BigDecimal durchlaufzeit) {
		this.durchlaufzeit = durchlaufzeit;
	}

	public String getReportDebug() {
		return reportDebug;
	}

	public void setReportDebug(String reportDebug) {
		this.reportDebug = reportDebug;
	}

	public String getReportInfo() {
		return reportInfo;
	}

	public void setReportInfo(String reportInfo) {
		this.reportInfo = reportInfo;
	}

	public String getReportWarn() {
		return reportWarn;
	}

	public void setReportWarn(String reportWarn) {
		this.reportWarn = reportWarn;
	}

	public String getReportError() {
		return reportError;
	}

	public void setReportError(String reportError) {
		this.reportError = reportError;
	}
}
