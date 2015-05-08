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

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SiPrefixParserAlternatives extends SiPrefixParser {

	protected Map<String, Integer> siAlternatives = new HashMap<String, Integer>();
	
	public SiPrefixParserAlternatives() {
		
	}

	@Override
	public List<BigDecimalSI> parse(String toParse) {
		Map<String, Integer> allPrefixes = new HashMap<String, Integer>();
		allPrefixes.putAll(siAlternatives);
		allPrefixes.putAll(siPrefixes);
		return new SiPrefixParserCore(allPrefixes, units, isForceZeroUnit()).parse(toParse);
	}

	protected void checkNotAlreadyTakenAlternative(String prefix) {
		if(siAlternatives.containsKey(prefix))
			throw new IllegalArgumentException("'" + prefix + "' is already used as SI alternative. Use setForceZeroUnit(true) for enabeling Units contained in SI alternatives.");
	}
	
	protected void checkNotAlreadyTakenAlternatives(List<String> prefixes) {
		for(String prefix : prefixes) {
			checkNotAlreadyTakenAlternative(prefix);
		}
	}
	
	@Override
	public void setUnits(List<String> units) {
		if(units != null && !isForceZeroUnit()) 
			checkNotAlreadyTakenAlternatives(units);
		super.setUnits(units);
	}
	
	/**
	 * Bei Probleme mit der Eingabe von '&micro;' (Fehlen auf der Tastatur, etc.) wird
	 * haufig ein 'u' bzw 'my' verwendet, was man mit alternative setzen kann. Hier kann eine beliebige, nicht im
	 * @param alternatives
	 */
	public void setMicroAlternative(String... alternatives) {
		setSiAlternative(MICRO, alternatives);
	}

	/**
	 * Setzt Alternative Prefixes zu einem normierten SI-Pr&auml;fix.
	 * So kann eine nicht implementierte / eigener Pr&auml;fix wie der SI Pr&auml;fix verwendet werden
	 * @param scale die Hochzahl der Zehnerpotenz
	 * @param alternative
	 */
	public void setSiAlternative(Integer scale, String alternative) {
		checkNoIllegalChars(alternative);
		checkNotAlreadyTakenSI(alternative);
		checkNotAlreadyTakenUnits(alternative);
		
		siAlternatives.put(alternative, scale);
	}

	/**
	 * Setzt Alternative Prefixes zu einem normierten SI-Pr&auml;fix.
	 * So kann eine nicht implementierte / eigener Pr&auml;fix wie der SI Pr&auml;fix verwendet werden
	 * @param siPrefix der gleichwertige SI Prefix
	 * @param alternatives
	 */
	public void setSiAlternative(String siPrefix, String... alternatives) {
		if(siPrefix == null) throw new NullPointerException("siPrefix = null");
		if(alternatives == null) throw new NullPointerException("alternatives = null");
		
		Integer scale = siPrefixes.get(siPrefix);
		if(scale == null) throw new IllegalArgumentException("'" + siPrefix + "' is not a valid SI prefix");
		
		for(String prefix : alternatives) {
			setSiAlternative(scale, prefix);
		}
	}

	/**
	 * Entfernt die SI Alternative
	 * @param alternative
	 */
	public void removeSiAlternative(String alternative) {
		siAlternatives.remove(alternative);
	}

	/**
	 * Entfernt alle via setSiAlternative(...) gesetzten alternativen SI Pr&auml;fixes, auch Micro Alternative!
	 */
	public void clearSiAlternatives() {
		siAlternatives.clear();
	}
}
