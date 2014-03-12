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
package com.lp.util;

import java.sql.Time;
import java.util.Calendar;

public final class HelperDateTime {

	private final static long MILLISEKUNDEN_EINER_STUNDE = 3600000;
	private final static long MILLISEKUNDEN_EINER_MINUTE = 60000;
	private final static long MILLISEKUNDEN_EINER_SEKUNDE = 1000;

	public final static java.sql.Time getTimeFromTimestamp(
			java.sql.Timestamp tsZeitpunkt) {
		if (tsZeitpunkt == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(tsZeitpunkt.getTime());

		long l = -MILLISEKUNDEN_EINER_STUNDE; // Time faengt bei -36000000 an
		l += c.get(Calendar.HOUR_OF_DAY) * MILLISEKUNDEN_EINER_STUNDE;
		l += c.get(Calendar.MINUTE) * MILLISEKUNDEN_EINER_MINUTE;
		l += c.get(Calendar.SECOND) * MILLISEKUNDEN_EINER_SEKUNDE;
		l += c.get(Calendar.MILLISECOND);

		java.sql.Time t = new java.sql.Time(l);

		return t;

	}

	public final static Time erzeugeTime(int iStunden, int iMinuten) {
		long l = -MILLISEKUNDEN_EINER_STUNDE; // Time faengt bei -36000000 an
		l += iStunden * MILLISEKUNDEN_EINER_STUNDE;
		l += iMinuten * MILLISEKUNDEN_EINER_MINUTE;
		return new java.sql.Time(l);
	}
}
