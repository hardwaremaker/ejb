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
package com.lp.server.personal.ejb;

import java.sql.Timestamp;


public class ReisekostenDiaetenScript {

	private Timestamp beginn;
	private Timestamp ende;
	private String lkz;
	private String personalart;
	private int beginnYear;
	private int endYear;


	public ReisekostenDiaetenScript (Timestamp beginn, Timestamp ende, String lkz,
			String personalart, int beginnYear, int endYear) {

		this.beginn = beginn;
		this.ende = ende;
		this.lkz = lkz;
		this.personalart = personalart;
		this.beginnYear = beginnYear;
		this.endYear = endYear;

	}

	public Timestamp getBeginn() {
		return beginn;
	}

	public Timestamp getEnde() {
		return ende;
	}

	public String getPersonalArt() {
		return personalart;
	}

	public String getLkz() {
		return lkz;
	}

	public long getBeginnMilliSeconds() {
		return getBeginn().getTime();
	}

	public long getEndeMilliSeconds() {
		return getEnde().getTime();
	}

	public int getBeginnYear() {
		return beginnYear;
	}

	public int getEndYear() {
		return endYear;
	}

	public long getLastDayOfBeginnYearMilliSeconds(int beginnYear) {
//		Calendar cal = Calendar.getInstance();
//		cal.set(Calendar.YEAR, beginnYear);
//		cal.set(Calendar.MONTH, 11);
//		cal.set(Calendar.DAY_OF_MONTH, 31);
//		cal.set(Calendar.HOUR_OF_DAY, 24);
//		return cal.getTimeInMillis();
		Timestamp ts = Timestamp.valueOf(beginnYear + "-12-31 24:0:0.0");
		return ts.getTime();
	}

	public long getFirstDayOfEndYearMilliSeconds(int endYear) {
//		Calendar cal = Calendar.getInstance();
//		cal.set(Calendar.YEAR, endYear);
//		cal.set(Calendar.DAY_OF_YEAR, 1);
//		return cal.getTimeInMillis();
		Timestamp ts = Timestamp.valueOf(endYear + "-01-01 0:0:0.0");
		return ts.getTime();
	}

	public int getBeginnHour() {
		return getBeginn().getHours();
	}

	public int getEndeHour() {
		return getEnde().getHours();
	}

	public int getBeginnMin() {
		return getBeginn().getMinutes();
	}

	public int getEndeMin() {
		return getEnde().getMinutes();
	}

	public int getBeginnSec() {
		return getBeginn().getSeconds();
	}

	public int getEndeSec() {
		return getEnde().getSeconds();
	}
}
