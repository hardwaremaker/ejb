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
package com.lp.server.partner.ejbfac;

import java.util.Comparator;

import com.lp.server.partner.service.KundeLieferstatistikDto;
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
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2012/12/19 13:57:30 $
 */
public class ComparatorKD implements Comparator<Object> {

	private int iSortierungNachWas = -1;
	private boolean bNachArtikelGruppe = false;

	public ComparatorKD(int iSortierungNachWasI, boolean bNachArtikelGruppe) {
		iSortierungNachWas = iSortierungNachWasI;
		this.bNachArtikelGruppe = bNachArtikelGruppe;
	}

	/**
	 * Compares its two arguments for order.
	 * 
	 * @param o1 the first object to be compared.
	 * @param o2 the second object to be compared.
	 * @return a negative integer, zero, or a positive integer as the first argument
	 *         is less than, equal to, or greater than the second.
	 * @todo Implement this java.util.Comparator method PJ 4178
	 */
	public int compare(Object o1, Object o2) {
		if (iSortierungNachWas == Helper.SORTIERUNG_NACH_DATUM) {
			KundeLieferstatistikDto k1 = (KundeLieferstatistikDto) o1;
			KundeLieferstatistikDto k2 = (KundeLieferstatistikDto) o2;

			String str1 = "";
			String str2 = "";

			if (bNachArtikelGruppe) {
				String ag1 = "";
				if (k1.getSArtikelgruppe() != null) {
					ag1 = k1.getSArtikelgruppe();
				}
				String ag2 = "";
				if (k2.getSArtikelgruppe() != null) {
					ag2 = k2.getSArtikelgruppe();
				}
				str1 += Helper.fitString2Length(ag1, 40, ' ');
				str2 += Helper.fitString2Length(ag2, 40, ' ');
			}

		
			int i = str1.compareTo(str2);

			if (i == 0) {

				long l1 = 0;
				long l2 = 0;

				if (k1.getDWarenausgangsdatum() != null) {
					l1 = k1.getDWarenausgangsdatum().getTime();
				}
				if (k2.getDWarenausgangsdatum() != null) {
					l2 = k2.getDWarenausgangsdatum().getTime();
				}

				if (l1 == l2) {
					return 0;
				}

				if (l1 < l2) {
					return 1;
				} else {
					return -1;
				}
			}
			return i;
		} else {
			// sortierung nach ident
			KundeLieferstatistikDto k1 = (KundeLieferstatistikDto) o1;
			KundeLieferstatistikDto k2 = (KundeLieferstatistikDto) o2;
			if (k1 != null && k1.getSIdent() != null && k2 != null && k2.getSIdent() != null) {

				String str1 = "";
				String str2 = "";

				if (bNachArtikelGruppe) {
					String ag1 = "";
					if (k1.getSArtikelgruppe() != null) {
						ag1 = k1.getSArtikelgruppe();
					}
					String ag2 = "";
					if (k2.getSArtikelgruppe() != null) {
						ag2 = k2.getSArtikelgruppe();
					}
					str1 += Helper.fitString2Length(ag1, 40, ' ');
					str2 += Helper.fitString2Length(ag2, 40, ' ');
				}

				str1 += Helper.fitString2Length(k1.getSIdent(), 25, ' ');
				str2 += Helper.fitString2Length(k2.getSIdent(), 25, ' ');

				int i = str1.compareTo(str2);

				if (i == 0) {
					String s1 = "               ";
					String s2 = "               ";

					if (k1.getSRechnungsnummer() != null) {
						s1 = Helper.fitString2Length(k1.getSRechnungsnummer(), 15, ' ');
					}
					if (k2.getSRechnungsnummer() != null) {
						s2 = Helper.fitString2Length(k2.getSRechnungsnummer(), 15, ' ');
					}

					if (k1.getSLieferscheinnummer() != null) {
						s1 += Helper.fitString2Length(k1.getSLieferscheinnummer(), 15, ' ');
					}
					if (k2.getSLieferscheinnummer() != null) {
						s2 += Helper.fitString2Length(k2.getSLieferscheinnummer(), 15, ' ');
					}

					return s1.compareTo(s2);
				}

				return i;
			}
			/**
			 * @todo zuerst die ohne ident und dann die mit PJ 4179
			 */
			else {
				return 1;
			}
		}
	}

	public void setISortierungNachWas(int iSortierungNachWas) {
		this.iSortierungNachWas = iSortierungNachWas;
	}

	public int getISortierungNachWas() {
		return iSortierungNachWas;
	}
}
