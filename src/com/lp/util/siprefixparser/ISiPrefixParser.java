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
package com.lp.util.siprefixparser;

import java.util.List;

public interface ISiPrefixParser {
	
	public static final String YOTTA = BigDecimalSI.YOTTA;
	public static final String ZETTA = BigDecimalSI.ZETTA;
	public static final String EXA = BigDecimalSI.EXA;
	public static final String PETA = BigDecimalSI.PETA;
	public static final String TERA = BigDecimalSI.TERA;
	public static final String GIGA = BigDecimalSI.GIGA;
	public static final String MEGA = BigDecimalSI.MEGA;
	public static final String KILO = BigDecimalSI.KILO;
	public static final String MILLI = BigDecimalSI.MILLI;
	public static final String MICRO = BigDecimalSI.MICRO;
	public static final String NANO = BigDecimalSI.NANO;
	public static final String PIKO = BigDecimalSI.PIKO;
	public static final String FEMTO = BigDecimalSI.FEMTO;
	public static final String ATTO = BigDecimalSI.ATTO;
	public static final String ZEPTO = BigDecimalSI.ZEPTO;
	public static final String YOKTO = BigDecimalSI.YOKTO;

	/**
	 * Entfernt alle ZersoScalePrefixes
	 */
	public void clearUnits();
	
	
	/**
	 * Setzt Einheiten die keine Mutliplikation mit einer Zehnerpotenz darstellen.
	 * Diese koennen optional am Ende zu parsenden SI-Zahl stehen, wenn der SI-Prefix an letzter Stelle steht
	 * Ist kein SI-Praefix in der Zahl vorhanden, also zum Beispiel kein
	 *  'k' oder 'M' <b>muss</b> eine der units vorkommen, sonst wird die
	 * Zahl als nicht gueltig gewertet.<br>
	 * Vorhandene Einheiten werden &uuml;berschrieben!<br>
	 * Beispiele:<br>
	 * 1R5 = 1,5 Ohm (R ist unit)<br>
	 * 3k3R (R)<br>
	 * 2&micro;8F (F)<br>
	 * 1F (F)<br>
	 * @param units nicht Mikro-Alternativen, nicht 
	 */
	public void setUnits(List<String> units);

	/**
	 * Einheiten die keine Mutliplikation mit einer Zehnerpotenz darstellen.
	 * Diese koennen optional am Ende zu parsenden SI-Zahl stehen.
	 * Ist kein SI-Praefix in der Zahl vorhanden, also zum Beispiel kein
	 *  'k' oder 'M' <b>muss</b> eine der units vorkommen, sonst wird die
	 * Zahl als nicht gueltig gewertet.<br>
	 * Vorhandene Einheiten werden &uuml;berschrieben!<br>
	 * Beispiele:<br>
	 * 1R5 = 1,5 Ohm (R ist unit)<br>
	 * 3k3R (R)<br>
	 * 2&micro;8F (F)<br>
	 * 1F (F)<br>
	 * @param units
	 */
	public void setUnits(String... units);
	
	/**
	 * Findet alle, durch Leerzeichen getrennte, SI-Pr&auml;fix behaftete
	 * Teilstrings und liefert diese als BigDecimal zurueck.
	 * Nicht parsebare Teilstrings werden ignoriert. Wird kein
	 * einziger gueltiger Teilstring gefunden, ist die Liste leer, nicht null.<br>
	 * <b>Die SI Einheiten sind immer caseSensitive! (M = mega, m = mili)</b>
	 * @param toParse
	 * @return Liste der SI-Werte 
	 */
	public List<BigDecimalSI> parse(String toParse);

	/**
	 * Findet den ersten SI-Pr&auml;fix behafteten
	 * Teilstring und liefert diesen als BigDecimal zurueck.
	 * Nicht parsebare Teilstrings werden ignoriert. Wird kein
	 * gueltiger Teilstring gefunden, ist der R&uuml;ckgabewert null.<br>
	 * <b>Die SI Einheiten sind immer caseSensitive! (M = mega, m = mili)</b>
	 * @param toParse
	 * @return der erste ermittelte SI-Wert
	 */
	public BigDecimalSI parseFirst(String toParse);
}
