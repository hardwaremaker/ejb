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
package com.lp.server.eingangsrechnung.service;

import java.io.Serializable;

public class VendidataImportStats implements Serializable {

	private static final long serialVersionUID = 2890546720504916325L;
	
	private Integer totalImportsAusgangsgutschriften;
	private Integer errorImportsAusgangsgutschriften;
	private Integer errorImportsRechnungen;
	private Integer goodImportsAusgangsgutschriften;
	private Integer goodImportsRechnungen;
	private Integer errorCounts;
	private Integer warningCounts;
	private Integer totalImportsRechnungen;

	public VendidataImportStats() {
		reset();
	}

	public void reset() {
		totalImportsAusgangsgutschriften = 0;
		totalImportsRechnungen = 0;
		errorImportsAusgangsgutschriften = 0;
		errorImportsRechnungen = 0;
		goodImportsAusgangsgutschriften = 0;
		goodImportsRechnungen = 0;
		errorCounts = 0;
		setWarningCounts(0);
	}

	public Integer getTotalImportsAusgangsgutschriften() {
		return totalImportsAusgangsgutschriften;
	}

	public void setTotalImportsAusgangsgutschriften(Integer totalImports) {
		this.totalImportsAusgangsgutschriften = totalImports;
	}
	
	public void incrementTotalImportsAusgangsgutschriften() {
		totalImportsAusgangsgutschriften++;
	}

	public Integer getErrorImportsAusgangsgutschriften() {
		return errorImportsAusgangsgutschriften;
	}

	public void setErrorImportsAusgangsgutschriften(Integer errorImports) {
		this.errorImportsAusgangsgutschriften = errorImports;
	}
	
	public void incrementErrorImportsAusgangsgutschriften() {
		errorImportsAusgangsgutschriften++;
	}
	
	public Integer getErrorImportsRechnungen() {
		return errorImportsRechnungen;
	}
	
	public void setErrorImportsRechnungen(Integer errorImportsRechnungen) {
		this.errorImportsRechnungen = errorImportsRechnungen;
	}
	
	public void incrementErrorImportsRechnungen() {
		errorImportsRechnungen++;
	}
	
	public Integer getGoodImportsAusgangsgutschriften() {
		return goodImportsAusgangsgutschriften;
	}

	public void setGoodImportsAusgangsgutschriften(Integer goodImports) {
		this.goodImportsAusgangsgutschriften = goodImports;
	}
	
	public void incrementGoodImportsAusgangsgutschriften() {
		goodImportsAusgangsgutschriften++;
	}

	public Integer getErrorCounts() {
		return errorCounts;
	}

	public void setErrorCounts(Integer errorCounts) {
		this.errorCounts = errorCounts;
	}

	public void incrementErrorCounts() {
		errorCounts++;
	}

	public Integer getWarningCounts() {
		return warningCounts;
	}

	public void setWarningCounts(Integer warningCounts) {
		this.warningCounts = warningCounts;
	}
	
	public void incrementWarningCounts() {
		warningCounts++;
	}

	public Integer getTotalImportsRechnungen() {
		return totalImportsRechnungen;
	}
	
	public void setTotalImportsRechnungen(Integer totalImportsRechnungen) {
		this.totalImportsRechnungen = totalImportsRechnungen;
	}
	
	public void incrementTotalImportsRechnungen() {
		totalImportsRechnungen++;
	}

	public void incrementGoodImportsRechnungen() {
		goodImportsRechnungen++;
	}
	
	public Integer getGoodImportsRechnungen() {
		return goodImportsRechnungen;
	}
	
	public void setGoodImportsRechnungen(Integer goodImportsRechnungen) {
		this.goodImportsRechnungen = goodImportsRechnungen;
	}
}
