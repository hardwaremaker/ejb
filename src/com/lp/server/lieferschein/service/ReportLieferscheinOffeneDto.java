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
package com.lp.server.lieferschein.service;

import java.io.Serializable;

public class ReportLieferscheinOffeneDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iIdLieferschein;
	private String cNrLieferschein;
	private String kundeCName1;
	private String kostenstelleCNr;

	public Integer getIIdLieferschein() {
		return this.iIdLieferschein;
	}

	public void setIIdLieferschein(Integer iIdLieferschein) {
		this.iIdLieferschein = iIdLieferschein;
	}

	public String getCNrLieferschein() {
		return cNrLieferschein;
	}

	public void setCNrLieferschein(String cNrLieferschein) {
		this.cNrLieferschein = cNrLieferschein;
	}

	public String getKundeCName1() {
		return kundeCName1;
	}

	public void setKundeCName1(String kundeCName1I) {
		this.kundeCName1 = kundeCName1I;
	}

	public String getKostenstelleCNr() {
		return kostenstelleCNr;
	}

	public void setKostenstelleCNr(String kostenstelleCNrI) {
		this.kostenstelleCNr = kostenstelleCNrI;
	}
}
