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
package com.lp.server.util.fastlanereader.service.query;

/**
 * <p>Der FastLaneReader verwendet Filter- und Sortierkriterien.</p>
 *
 * <p>Copright Logistik Pur Software GmbH (c) 2004-2007</p>
 *
 * <p>Erstellungsdatum 2004-07-30</p>
 *
 * @author uli walch
 * @version 1.0
 */

import java.io.Serializable;
import java.math.BigDecimal;

public abstract class QueryKriterium implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Attributname, entspricht einem Tabellenattribut. */
	public String kritName;

	/** Soll dieses Kriterium in der aktuellen Abfrage verwendet werden. */
	public boolean isKrit;

	/** Enthaelt den Wert auf den verglichen wird oder die Sortierreihenfolge. */
	public String value;

	public BigDecimal bdValue;

	/** Wenn String dann ignore case (true) oder nicht (false) */
	private boolean bIgnoreCase = false;

	public QueryKriterium(String pName, boolean pKrit, String pValue,
			boolean bIgnoreCaseI) {

		this.kritName = pName;
		this.isKrit = pKrit;
		this.value = pValue;
		this.bIgnoreCase = bIgnoreCaseI;
	}

	public QueryKriterium(String pName, boolean pKrit, BigDecimal bdValue,
			boolean bIgnoreCaseI) {

		this.kritName = pName;
		this.isKrit = pKrit;
		this.bdValue = bdValue;
		this.bIgnoreCase = bIgnoreCaseI;
	}


	public String toString() {
		StringBuffer buff = new StringBuffer("Krit [");

		buff.append("Name=").append(this.kritName).append(", Flag=")
				.append(this.isKrit).append(", Wert=").append(this.value)
				.append("]");

		return buff.toString();
	}

	public void setBIgnoreCase(boolean bIgnoreCase) {
		this.bIgnoreCase = bIgnoreCase;
	}

	public boolean isBIgnoreCase() {
		return bIgnoreCase;
	}
}
