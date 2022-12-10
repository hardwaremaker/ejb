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
package com.lp.server.system.pkgenerator.format;

import java.text.DecimalFormat;

/**
 * <p>
 * Logistik Pur Default Belegnummernformat
 * </p>
 * <p>
 * Damit koennen Belegnummern im Stil von 04/0000123 realisiert werden
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum <I>25. 10. 2004</I>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version 1.0
 */
public class LpDefaultBelegnummerFormat extends LpBelegnummerFormat {
	protected char trennzeichen;
	protected java.text.DecimalFormat dfGeschaeftsjahr;
	private int stellenGeschaeftsjahr;
	protected int stellenZufall;

	public LpDefaultBelegnummerFormat(int stellenGeschaeftsjahr,
			char trennzeichen, int stellenLfdNummer, int stellenZufall) {
		super(stellenLfdNummer);
		dfGeschaeftsjahr = new DecimalFormat(
				getDecimalFormatPattern(stellenGeschaeftsjahr));

		// damit noetigenfalls auch wirklich vorn abgeschnitten wird
		dfGeschaeftsjahr.setMaximumIntegerDigits(stellenGeschaeftsjahr);
		this.stellenGeschaeftsjahr = stellenGeschaeftsjahr;
		this.trennzeichen = trennzeichen;
		this.stellenZufall = stellenZufall;
	}

	public String format(LpBelegnummer lpBelegnummer) {
		String result = dfGeschaeftsjahr.format(lpBelegnummer
				.getGeschaeftsJahr());
		result = result + trennzeichen;
		result = result + dfBelegNummer.format(lpBelegnummer.getBelegNummer());
		return result;
	}

	public String formatMitStellenZufall(LpBelegnummer lpBelegnummer) {
		String result = dfGeschaeftsjahr.format(lpBelegnummer
				.getGeschaeftsJahr());
		result = result + trennzeichen;

		// PJ20114
		Integer belegnummer = lpBelegnummer.getBelegNummer();
		if (stellenZufall > 0) {
			belegnummer = belegnummer * (int) Math.pow(10, stellenZufall);
			belegnummer += (int) (Math.random() * (int) Math.pow(10,
					stellenZufall));
		}
		result = result + dfBelegNummer.format(belegnummer);
		return result;
	}

	public int getStellenGeschaeftsjahr() {
		return stellenGeschaeftsjahr;
	}

	public char getTrennzeichen() {
		return trennzeichen;
	}

}
