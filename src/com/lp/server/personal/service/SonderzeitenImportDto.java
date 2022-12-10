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
import java.sql.Timestamp;

public class SonderzeitenImportDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer personalIId;
	private Integer taetigkeitIId;
	private Timestamp tDatum;

	private int row ;
	private int col ;
	private String source ;
	
	public SonderzeitenImportDto() {
	}

	public SonderzeitenImportDto(Integer personalId, Integer taetigkeitId, Timestamp timestamp) {
		this.personalIId = personalId ;
		this.taetigkeitIId = taetigkeitId ;
		this.tDatum = timestamp ;
	}
	
	public Integer getPersonalIId() {
		return personalIId;
	}
	public void setPersonalIId(Integer personalIId) {
		this.personalIId = personalIId;
	}
	public Integer getTaetigkeitIId() {
		return taetigkeitIId;
	}
	public void setTaetigkeitIId(Integer taetigkeitIId) {
		this.taetigkeitIId = taetigkeitIId;
	}
	public Timestamp gettDatum() {
		return tDatum;
	}
	public void settDatum(Timestamp tDatum) {
		this.tDatum = tDatum;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
}
