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
package com.lp.server.finanz.ejbfac;

import java.util.Comparator;

import com.lp.server.finanz.service.IntrastatDto;

/**
 * <p>
 * Diese Klasse kuemmert sich um die Sortierung der Intrastat-Eintraege.
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur GmbH (c) 2004 - 2007
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 29.08.07
 * </p>
 * 
 * <p>
 * 
 * @author $Author: sebastian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2008/10/14 14:07:18 $
 */
public class ComparatorIntrastat implements Comparator<IntrastatDto> {

	public int compare(IntrastatDto a, IntrastatDto b) {
		// Die Reihenfolge der Sortierung ist nicht wichtig.
		// es muessen nur Eintraege mit gleicher WVK-Nummer, gleicher UID und
		// gleichem Partner

		int diff = a.getWarenverkehrsnummerDto().getCNr().compareTo(
				b.getWarenverkehrsnummerDto().getCNr());

		if (diff == 0) {
			String sUid1 = a.getUid();
			String sUid2 = b.getUid();
/*
 * Das reicht nicht mehr ab Berichtsjahr 2022, da die EmpfaengerUID 
 * explizit ausgegeben werden muss und damit unterschiedliche Empfaenger
 * aufscheinen muessen

			String sUid1 = Helper.getAllStartCharacters(a.getUid());
			String sUid2 = Helper.getAllStartCharacters(b.getUid());
 * 			
 */
			if (sUid1 == null && sUid2 == null) {
				diff = 0;
			} else if (sUid1 == null) {
				diff = -1;
			} else if (sUid2 == null) {
				diff = 1;
			} else {
				diff = sUid1.compareTo(sUid2);
			}
		}

		return diff;
	}
}
