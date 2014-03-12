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
package com.lp.server.util.fastlanereader.service.query;

/**
 * <p><I>[Hier die Beschreibung der Klasse eingf&uuml;gen]</I> </p>
 * <p>Copright Logistik Pur Software GmbH (c) 2004-2007</p>
 * <p>Erstellungsdatum <I>[Hier das Erstellungsdatum einf&uuml;gen]</I></p>
 * <p> </p>
 * @author unbekannt
 * @version 1.0
 */

import java.io.Serializable;

import com.lp.server.util.Facade;

public class FilterKriteriumDirekt extends FilterKriterium implements
		Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int PROZENT_NONE = 0;
	public static final int PROZENT_LEADING = 1;
	public static final int PROZENT_TRAILING = 2;
	public static final int PROZENT_BOTH = 3;
	public static final int EXTENDED_SEARCH = 4;

	public String uiName = null;
	public int iWrapWithProzent = PROZENT_NONE;
	public boolean wrapWithSingleQuotes = false;
	public int iEingabebreite = Facade.MAX_UNBESCHRAENKT;

	public static final int TYP_STRING = 1;
	public static final int TYP_DECIMAL = 2;
	public int iTyp = TYP_STRING;

	public FilterKriteriumDirekt(String pNameI, String pValueI,
			String pOperatorI, String uiNameI, int iWrapWithProzentI,
			boolean bWrapWithSingleQuotesI, boolean bIgnoreCaseI,
			int iEingabebreiteI) {

		this(pNameI, pValueI, pOperatorI, uiNameI, iWrapWithProzentI,
				bWrapWithSingleQuotesI, bIgnoreCaseI, iEingabebreiteI,
				TYP_STRING);
	}

	public FilterKriteriumDirekt(String pNameI, String pValueI,
			String pOperatorI, String uiNameI, int iWrapWithProzentI,
			boolean bWrapWithSingleQuotesI, boolean bIgnoreCaseI,
			int iEingabebreiteI, int iTyp) {

		super(pNameI, true, pValueI, pOperatorI, bIgnoreCaseI);
		this.iTyp=iTyp;
		uiName = uiNameI;
		iWrapWithProzent = iWrapWithProzentI;
		wrapWithSingleQuotes = bWrapWithSingleQuotesI;
		iEingabebreite = iEingabebreiteI;
	}

	public void wrapWithProzent() {
		if (iWrapWithProzent == PROZENT_LEADING) {
			value = "%" + value;
		} else if (iWrapWithProzent == PROZENT_TRAILING) {
			value = value + "%";
		} else if (iWrapWithProzent == PROZENT_BOTH) {
			value = "%" + value + "%";
		}
	}

	public void wrapWithSingleQuotes() {
		if (wrapWithSingleQuotes) {
			StringBuffer sbWrappedValue = new StringBuffer("'").append(value)
					.append("'");

			value = sbWrappedValue.toString();
		}
	}
}
