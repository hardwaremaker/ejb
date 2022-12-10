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

public class LohnartDto implements Serializable {
	private Integer iId;

	private Integer iLohnart;
	private String cBez;
	private String cKommentar;
	private String cFormel;
	private String cTyp;
	private String personalartCNr;
	private Integer taetigkeitIIdNl;

	public Integer getILohnart() {
		return iLohnart;
	}

	public void setILohnart(Integer iLohnart) {
		this.iLohnart = iLohnart;
	}

	public String getCBez() {
		return cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public String getcKommentar() {
		return cKommentar;
	}

	public void setCKommentar(String cKommentar) {
		this.cKommentar = cKommentar;
	}

	public String getCFormel() {
		return cFormel;
	}

	public void setCFormel(String cFormel) {
		this.cFormel = cFormel;
	}

	public String getCTyp() {
		return cTyp;
	}

	public void setCTyp(String cTyp) {
		this.cTyp = cTyp;
	}

	public String getPersonalartCNr() {
		return personalartCNr;
	}

	public void setPersonalartCNr(String personalartCNr) {
		this.personalartCNr = personalartCNr;
	}

	public Integer getTaetigkeitIIdNl() {
		return taetigkeitIIdNl;
	}

	public void setTaetigkeitIIdNl(Integer taetigkeitIIdNl) {
		this.taetigkeitIIdNl = taetigkeitIIdNl;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	private Integer iAusfallWochen;

	public Integer getIAusfallWochen() {
		return iAusfallWochen;
	}

	public void setIAusfallWochen(Integer iAusfallWochen) {
		this.iAusfallWochen = iAusfallWochen;
	}

	private Double fMindestuestd;
	public Double getFMindestuestd() {
		return fMindestuestd;
	}

	public void setFMindestuestd(Double fMindestuestd) {
		this.fMindestuestd = fMindestuestd;
	}

}
