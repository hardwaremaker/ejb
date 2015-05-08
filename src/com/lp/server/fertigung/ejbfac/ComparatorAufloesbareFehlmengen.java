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
package com.lp.server.fertigung.ejbfac;

import java.util.Comparator;

import com.lp.server.fertigung.service.ReportAufloesbareFehlmengenDto;
import com.lp.util.Helper;

/**
 * <p>
 * Diese Klasse kuemmert sich um die Sortierung der aufloesbaren Fehlmengen
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 19.01.06
 * </p>
 * 
 * <p>
 * 
 * @author $Author: heidi $
 *         </p>
 * 
 * @version not attributable Date $Date: 2008/08/07 12:57:17 $
 */
public class ComparatorAufloesbareFehlmengen implements Comparator<Object> {

	private int iSortierungNachWas = -1;

	public ComparatorAufloesbareFehlmengen(int iSortierungNachWasI) {
		iSortierungNachWas = iSortierungNachWasI;
	}

	public int compare(Object a, Object b) {
		ReportAufloesbareFehlmengenDto afmDto1 = (ReportAufloesbareFehlmengenDto) a;
		ReportAufloesbareFehlmengenDto afmDto2 = (ReportAufloesbareFehlmengenDto) b;
		if (iSortierungNachWas == Helper.SORTIERUNG_NACH_IDENT) {
			if (afmDto1 != null && afmDto1.getSArtikelNummer() != null
					&& afmDto2 != null && afmDto2.getSArtikelNummer() != null) {
				int i = afmDto1.getSArtikelNummer().compareTo(
						afmDto2.getSArtikelNummer());
				if (i == 0) {
					i = afmDto1.getSBelegnummer().compareTo(
							afmDto2.getSBelegnummer());
				}
				return i;
			} else {
				return 0;
			}
		}
		// Sortierung nach Losnummer
		else {
			if (afmDto1 != null && afmDto1.getSBelegnummer() != null
					&& afmDto2 != null && afmDto2.getSBelegnummer() != null) {
				return afmDto1.getSBelegnummer().compareTo(
						afmDto2.getSBelegnummer());
			} else {
				return 0;
			}
		}
	}
}
