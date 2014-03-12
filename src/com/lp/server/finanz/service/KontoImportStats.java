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
package com.lp.server.finanz.service;

import java.io.Serializable;

public class KontoImportStats implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer totalRows ;
	private Integer goodRowCount ;
	private Integer errorRowCount ;
	private Integer warningRowCount ;
	private Integer ignoredRowCount ;
	private Integer possibleGoodRowCount ;
	

	public KontoImportStats() {
		reset() ;
	}

	public void reset() {
		totalRows = 0 ;
		goodRowCount = 0 ;
		errorRowCount = 0 ;
		warningRowCount = 0 ;
		ignoredRowCount = 0 ;
		possibleGoodRowCount = 0 ;
	}

	public Integer getIgnoredRowCount() {
		return ignoredRowCount;
	}
	public void setIgnoredRowCount(Integer ignoredRowCount) {
		this.ignoredRowCount = ignoredRowCount;
	}
	
	public void incrementIgnoredRowCount() {
		++ignoredRowCount ;
	}
	
	public Integer getTotalRows() {
		return totalRows;
	}
	public void setTotalRows(Integer totalRows) {
		this.totalRows = totalRows;
	}
	public Integer getGoodRowCount() {
		return goodRowCount;
	}
	public void setGoodRowCount(Integer goodRowCount) {
		this.goodRowCount = goodRowCount;
	}
	public void incrementGoodRowCount() {
		++goodRowCount ;
	}

	public Integer getErrorRowCount() {
		return errorRowCount;
	}
	public void setErrorRowCount(Integer errorRowCount) {
		this.errorRowCount = errorRowCount;
	}
	public void incrementErrorRowCount() {
		++errorRowCount ;
	}

	public Integer getWarningRowCount() {
		return warningRowCount;
	}
	public void setWarningRowCount(Integer warningRowCount) {
		this.warningRowCount = warningRowCount;
	}
	public void incrementWarningRowCount() {
		++warningRowCount ;
	}
	
	public Integer getPossibleGoodRowCount() {
		return possibleGoodRowCount;
	}

	public void setPossibleGoodRowCount(Integer possibleGoodRowCount) {
		this.possibleGoodRowCount = possibleGoodRowCount;
	}
	
	public void incrementPossibleGoodRowCount() {
		++possibleGoodRowCount ;
	}
}
