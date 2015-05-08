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
package com.lp.util.siprefixparser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SiPrefixParserCore {

	public static final int PRECISION_DEFAULT = 3;
	public static final int PRECISION_DYNAMIC = -1;
	
	private static final String FIRST_NUMBER_COMA = "([\\d]+[,|.][\\d]+)";
	private static final String FIRST_NUMBER_NO_COMA = "([\\d]+)";
	private static final String LAST_NUMBER = "([\\d]{0,"+PRECISION_DEFAULT+"})";
	private static final String LAST_NUMBER_DYNAMIC = "([\\d]*)";
		
	private Map<String, Integer> prefixScales;
	private String regex;
	private int precision;
	private boolean forceZeroUnit;

	/**
	 * 
	 * @param prefixScales
	 * @param zeroScales
	 * @param precision
	 * @param forceZeroUnit true, wenn die Nicht-SI-Einheit (zB. m f&uuml;r Meter) immer enthalten sein muss.
	 */
	public SiPrefixParserCore(Map<String, Integer> prefixScales, List<String> zeroScales, int precision, boolean forceZeroUnit) {
		this.prefixScales = prefixScales;
		Set<String> units = prefixScales.keySet();
		this.precision = precision;
		this.forceZeroUnit = forceZeroUnit;
		regex = createRegexPattern(units, zeroScales);
	}
	

	/**
	 * @param prefixScales 
	 * @param zeroScales 
	 * @param forceZeroUnit true, wenn die Nicht-SI-Einheit (zB. m f&uuml;r Meter) immer enthalten sein muss.
	 */
	public SiPrefixParserCore(Map<String, Integer> prefixScales, List<String> zeroScales, boolean forceZeroUnit) {
		this(prefixScales, zeroScales, PRECISION_DYNAMIC, forceZeroUnit);
	}

	public List<BigDecimalSI> parse(String s) {
		List<BigDecimalSI> list = new ArrayList<BigDecimalSI>();
		if(s == null) return list;
		for(String sub : s.split("[\\s/]|(?<!\\d),|,(?!\\d)|(,\\z)")) {
			BigDecimalSI n = parseSubstring(sub);
			if(n != null) {
				list.add(n);
			}
		}
		return list;
	}
	
	protected BigDecimalSI parseSubstring(String ss) {
		Matcher m = Pattern.compile(regex).matcher(ss);
		if(!m.find()) return null;
		
		BigDecimal firstNo;
		Integer commaLength = null;
		if(m.group(2) != null //2. Gruppe gefuellt->Zahl mit komma
				&& (m.group(7) == null || m.group(7).isEmpty())) { //dann darf keine Zahl nach SI-Praefix vorkommen
			String s = m.group(2).replaceAll(",","\\.");
			firstNo = new BigDecimal(s);
			
			Matcher m1 = Pattern.compile("[^\\d]([\\d]*)").matcher(s);
			if(m1.find())
				commaLength = m1.group(1).length();
			
		} else if(m.group(3) != null) {	//3. Gruppe gefuellt->Zahl ohne komma
			firstNo = new BigDecimal(m.group(3));
			commaLength = 0;
		} else {
			return null; // ungueltig, zB (3,3K8)
		}
		Integer scale = null;
		if(m.group(5) != null) { // SI-Praefix (10^n)
			scale = prefixScales.get(m.group(5));
		} else if(m.group(6) != null){ // ist in Gruppe 6 (10^0)
			scale = 0;
		} else return null;
		
		BigDecimal lastNo = null;
		if(!m.group(7).isEmpty()) {
			if(m.group(5) != null && m.group(6) != null)
				return null;
			try {
				int decimalPlaces = m.group(7).length();
				commaLength = decimalPlaces;
				//commaScale += (int)Math.ceil(decimalPlaces/3f)*3; //die naechst groessere 3er Stufe
				String s = String.format("%-" + commaLength + "s", m.group(7)); // aus '5' wird '5  '
				s = s.replace(' ', '0'); //aus '5  ' wird '500' 
				lastNo = new BigDecimal(s);
			} catch (NumberFormatException e) {
				return null; //zur Sicherheit, regex verhindert das eigentlich eh.
			}
		}

		commaLength = precision == PRECISION_DYNAMIC ? commaLength : precision;
		BigDecimal result = firstNo.movePointRight(scale).setScale(commaLength-scale, BigDecimal.ROUND_HALF_EVEN);
		if(lastNo != null) result = result.add(lastNo.movePointRight(scale-commaLength).setScale(commaLength-scale, BigDecimal.ROUND_HALF_EVEN));

//		return new BigDecimalSI(result.setScale(commaLength - scale, BigDecimal.ROUND_HALF_EVEN), m.group(6));
		return new BigDecimalSI(result, m.group(6));
	}
	
	protected String prefixToRegex(Collection<String> c) {
		if(c.size() == 0) return "(^.)"; // Gruppe immer leer
		StringBuffer sb = new StringBuffer();
		boolean first = true;
		sb.append("(");
		for (String prefix : c) {
			sb.append(first?"":"|").append(prefix);
			first = false;
		}
		sb.append(")");
		return sb.toString();
	}
	
	protected String createRegexPattern(Set<String> siPrefix, List<String> zeroPrefix) {
		String PREFIXES = prefixToRegex(siPrefix);
		String ZEROS = prefixToRegex(zeroPrefix);
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("^("+FIRST_NUMBER_COMA+"|"+FIRST_NUMBER_NO_COMA+")");
		sb.append("(").append(PREFIXES).append("?")
			.append(ZEROS).append(forceZeroUnit ? "+" : "?")
			.append(")");
		sb.append(precision == PRECISION_DYNAMIC ? LAST_NUMBER_DYNAMIC : LAST_NUMBER);
		//TODO LAST_NUMBER nimmt immer den Default von 3 her (siehe Instanzierung),
		// das sollte eigentlich der uebergebene Wert sein
		sb.append("$");
		
		return sb.toString();
	}
}
