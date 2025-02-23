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

public class LohnartstundenfaktorDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Integer lohnartIId;
	private String lohnstundenartCNr;
	private Double fFaktor;

	private Integer taetigkeitIId;

	public Integer getTaetigkeitIId() {
		return taetigkeitIId;
	}

	public void setTaetigkeitIId(Integer taetigkeitIId) {
		this.taetigkeitIId = taetigkeitIId;
	}

	private Integer schichtzeitIId;

	public Integer getSchichtzeitIId() {
		return schichtzeitIId;
	}

	public void setSchichtzeitIId(Integer schichtzeitIId) {
		this.schichtzeitIId = schichtzeitIId;
	}

	private Integer zeitmodellIId;

	public Integer getZeitmodellIId() {
		return this.zeitmodellIId;
	}

	public void setZeitmodellIId(Integer zeitmodellIId) {
		this.zeitmodellIId = zeitmodellIId;
	}

	private Integer tagesartIId;

	public Integer getTagesartIId() {
		return tagesartIId;
	}

	public void setTagesartIId(Integer tagesartIId) {
		this.tagesartIId = tagesartIId;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getLohnartIId() {
		return lohnartIId;
	}

	public void setLohnartIId(Integer lohnartIId) {
		this.lohnartIId = lohnartIId;
	}

	public String getLohnstundenartCNr() {
		return lohnstundenartCNr;
	}

	public void setLohnstundenartCNr(String lohnstundenartCNr) {
		this.lohnstundenartCNr = lohnstundenartCNr;
	}

	public Double getFFaktor() {
		return fFaktor;
	}

	public void setFFaktor(Double fFaktor) {
		this.fFaktor = fFaktor;
	}

}
