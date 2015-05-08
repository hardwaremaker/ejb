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
 *******************************************************************************/
package com.lp.server.util;

import java.io.Serializable;
import java.util.Calendar;

import com.lp.util.Helper;

/**
 * Beispiel fuer Implementierung der DautmFilter Von-Bis
 * 
 * @author Christian
 * 
 */
public class DatumsfilterVonBis implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1198872214822557272L;
	private java.sql.Timestamp tVon = null;
	private java.sql.Timestamp tBis = null;

	public DatumsfilterVonBis(java.sql.Timestamp tVon, java.sql.Timestamp tBis) {
		this.tVon = tVon;
		this.tBis = tBis;
	}

	/**
	 * Wenn tVon vorhanden, dann wird als Rueckgabewert der aktuelle Tag
	 * 00:00:00.000 zureuckgegeben
	 * 
	 * @return Timestamp des Von-Datums 00:00:00.000
	 */
	public java.sql.Timestamp getTimestampVon() {
		return Helper.cutTimestamp(tVon);
	}

	/**
	 * Wenn tBis vorhanden, dann wird als Rueckgabewert der naechste Tag
	 * 00:00:00.000 zureuckgegeben
	 * 
	 * @return null bzw. Timestamp des Bis-Tages f&uuml;r darauffolgenden Tag 00:00:00.000
	 */
	public java.sql.Timestamp getTimestampBis() {
		if (tBis != null) {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(tBis.getTime());
			c.add(Calendar.DAY_OF_MONTH, 1);
			return Helper.cutTimestamp(new java.sql.Timestamp(c
					.getTimeInMillis()));
		} else {
			return null;
		}
	}

	public java.sql.Timestamp getTimestampBisUnveraendert() {
		return tBis;
	}

}
