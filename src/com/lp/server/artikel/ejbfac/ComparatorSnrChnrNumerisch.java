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
package com.lp.server.artikel.ejbfac;

import java.sql.Timestamp;
import java.util.Comparator;

import com.lp.server.artikel.service.SeriennrChargennrAufLagerDto;

/**
 * <p>
 * Diese Klasse kuemmert sich um die Sortierung von Bewegungsvorschaudaten
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 10.11.05
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2012/12/19 16:11:10 $
 */
public class ComparatorSnrChnrNumerisch implements Comparator<Object> {

	public ComparatorSnrChnrNumerisch(
			boolean bSortiereNachSeriennummerChanrgennummer) {
		this.bSortiereNachSeriennummerChanrgennummer = bSortiereNachSeriennummerChanrgennummer;
	}

	boolean bSortiereNachSeriennummerChanrgennummer = false;

	/**
	 * ------------------------------------------------------------------------
	 * ---- vergleicht 2 objekte und ordnet chronologisch die Objekte
	 * 
	 * @param a
	 *            Dto1
	 * @param b
	 *            Dto2
	 * @return int
	 *         --------------------------------------------------------------
	 *         -------------
	 */
	public int compare(Object a, Object b) {
		SeriennrChargennrAufLagerDto bewegDto1 = (SeriennrChargennrAufLagerDto) a;
		SeriennrChargennrAufLagerDto bewegDto2 = (SeriennrChargennrAufLagerDto) b;

		if (bSortiereNachSeriennummerChanrgennummer) {

			Long l1 = bewegDto1.getKeyFuerSortierung();
			Long l2 = bewegDto2.getKeyFuerSortierung();

			if (l1 > l2) {
				return 1;
			} else if (l1.equals(l2)) {

				return bewegDto2.getCSeriennrChargennr().compareTo(
						bewegDto1.getCSeriennrChargennr());
			} else {
				return -1;
			}
		} else {
			Timestamp ts1 = bewegDto1.getTBuchungszeit();
			Timestamp ts2 = bewegDto2.getTBuchungszeit();

			if (ts1 != null && ts2 != null && ts1.getTime() > ts2.getTime()) {
				return 1;
			} else {
				return -1;
			}
		}
	}
}
