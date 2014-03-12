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
package com.lp.server.eingangsrechnung.ejbfac;

import java.util.Comparator;

import com.lp.server.eingangsrechnung.service.ReportEingangsrechnungKontierungsjournalDto;
import com.lp.util.Helper;

/**
 * <p>
 * Diese Klasse kuemmert sich um die Sortierung der Lieferstatistikdaten
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 22.09.05
 * </p>
 * 
 * <p>
 * 
 * @author $Author: heidi $
 *         </p>
 * 
 * @version not attributable Date $Date: 2008/08/07 12:57:16 $
 */
public class ComparatorKontierungsjournal implements Comparator<Object> {

	private int iSortierungNachWas = -1;

	public ComparatorKontierungsjournal(int iSortierungNachWasI) {
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
	 * @todo Implement this java.util.Comparator method PJ 4180
	 */
	public int compare(Object o1, Object o2) {
		if (iSortierungNachWas == Helper.SORTIERUNG_NACH_KOSTENSTELLE_UND_KONTO) {
			ReportEingangsrechnungKontierungsjournalDto k1 = (ReportEingangsrechnungKontierungsjournalDto) o1;
			ReportEingangsrechnungKontierungsjournalDto k2 = (ReportEingangsrechnungKontierungsjournalDto) o2;
			if (k1 != null
					&& k1.getSKostenstellenummer() != null
					&& k2 != null
					&& k2.getSKostenstellenummer() != null
					&& k1.getSKostenstellenummer().compareTo(
							k2.getSKostenstellenummer()) != 0) {
				return k1.getSKostenstellenummer().compareTo(
						k2.getSKostenstellenummer());
			} else if (k1 != null
					&& k1.getSKontonummer() != null
					&& k2 != null
					&& k2.getSKontonummer() != null) {
				int i =  k1.getSKontonummer().compareTo(k2.getSKontonummer());
				if(i==0){
					return k1.getSEingangsrechnungsnummer().compareTo(k2.getSEingangsrechnungsnummer());
				} else {
					return k1.getSKontonummer().compareTo(k2.getSKontonummer());
				}
			}
		}
		return 0;
	}

	public void setISortierungNachWas(int iSortierungNachWas) {
		this.iSortierungNachWas = iSortierungNachWas;
	}

	public int getISortierungNachWas() {
		return iSortierungNachWas;
	}
}
