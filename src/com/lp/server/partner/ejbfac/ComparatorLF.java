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
package com.lp.server.partner.ejbfac;

import java.util.Comparator;

import com.lp.util.Helper;

/**
 * <p>
 * Diese Klasse kuemmert sich um ...
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellung: Vorname Nachname; dd.mm.05
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2009/12/01 12:23:13 $
 */
public class ComparatorLF implements Comparator<Object> {

	private int iSortierungNachWas = -1;

	public ComparatorLF(int iSortierungNachWasI) {
		iSortierungNachWas = iSortierungNachWasI;
	}

	/**
	 * Compares its two arguments for order.
	 * 
	 * @param o1
	 *            the first object to be compared.
	 * @param o2
	 *            the second object to be compared.
	 * @return a negative integer, zero, or a positive integer as the first
	 *         argument is less than, equal to, or greater than the second.
	 * @todo Implement this java.util.Comparator method PJ 4188
	 */
	public int compare(Object o1, Object o2) {
		if (iSortierungNachWas == Helper.SORTIERUNG_NACH_DATUM) {
			WareneingangspositionenDto k1 = (WareneingangspositionenDto) o1;
			WareneingangspositionenDto k2 = (WareneingangspositionenDto) o2;
			if (k1 != null && k1.getDBelegdatum() != null && k2 != null
					&& k2.getDBelegdatum() != null
					&& k1.getDBelegdatum().after(k2.getDBelegdatum())) {
				return -1;
			} else {
				return 1;
			}
		} else if (iSortierungNachWas == Helper.SORTIERUNG_NACH_IDENT) {
			WareneingangspositionenDto k1 = (WareneingangspositionenDto) o1;
			WareneingangspositionenDto k2 = (WareneingangspositionenDto) o2;
			if (k1 != null && k1.getSIdent() != null && k2 != null
					&& k2.getSIdent() != null) {
				return k1.getSIdent().compareTo(k2.getSIdent());
			}
			return 1;
		} else {
			WareneingangspositionenDto k1 = (WareneingangspositionenDto) o1;
			WareneingangspositionenDto k2 = (WareneingangspositionenDto) o2;
			if (k1 != null && k1.getSNr() != null && k2 != null
					&& k2.getSNr() != null) {
				return k1.getSNr().compareTo(k2.getSNr());
			}
			return 1;
		}
	}

	public void setISortierungNachWas(int iSortierungNachWas) {
		this.iSortierungNachWas = iSortierungNachWas;
	}

	public int getISortierungNachWas() {
		return iSortierungNachWas;
	}
}
