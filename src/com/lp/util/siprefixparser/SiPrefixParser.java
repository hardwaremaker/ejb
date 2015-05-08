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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SiPrefixParser implements ISiPrefixParser {

	
	/**
	 * SI-Praefix und dazugehoeriger Zehnerpotenz-Exponent
	 */
	protected Map<String, Integer> siPrefixes = new HashMap<String, Integer>() {
		private static final long serialVersionUID = 4086303168801617007L;

		{
			put(YOTTA, 	24);
			put(ZETTA, 	21);
			put(EXA, 	18);
			put(PETA, 	15);
			put(TERA, 	12);
			put(GIGA, 	 9);
			put(MEGA,  	 6);
			put(KILO,  	 3);
			put(MILLI, 	-3);
			put(MICRO, 	-6);
			put(NANO, 	-9);
			put(PIKO,  -12);
			put(FEMTO, -15);
			put(ATTO,  -18);
			put(ZEPTO, -21);
			put(YOKTO, -24);
		}
	};
	
	protected List<String> units = new ArrayList<String>();
	private boolean forceZeroUnit = false;
	
	public SiPrefixParser() {
		
	}

	@Override
	public void setForceZeroUnit(boolean forceZeroUnit) {
		this.forceZeroUnit = forceZeroUnit;
		//Ueber einen setter und nicht automatisch, damit das bewusst gemacht wird.
	}
	
	@Override
	public boolean isForceZeroUnit() {
		return forceZeroUnit;
	}
	
	@Override
	public List<BigDecimalSI> parse(String toParse) {
		return new SiPrefixParserCore(siPrefixes, units, forceZeroUnit).parse(toParse);
	}
	
	protected void checkNoIllegalChars(String prefix) {
		//TODO: Zeichen welche nicht vorkommen duerfen erweitern
		Matcher m = Pattern.compile("[\\s\\d\\r\\n\\t,\\.]+").matcher(prefix);
		if(m.find())
			throw new IllegalArgumentException("'" + prefix + "' contains illegal charaters.\n" +
					"Must not contain a number, space, tab, linebreak, dot or comma");
	}

	protected void checkNoIllegalChars(List<String> prefixes) {
		for(String prefix : prefixes)
			checkNoIllegalChars(prefix);
	}
	
	protected void checkNotAlreadyTakenSI(String prefix) {
		if(siPrefixes.containsKey(prefix) && !forceZeroUnit)
			throw new IllegalArgumentException("'" + prefix + "' is SI prefix. Use setForceZeroUnit(true) for enabeling Units contained in SI prefixes.");
	}
	
	protected void checkNotAlreadyTakenUnits(String prefix) {
		if(units.contains(prefix))
			throw new IllegalArgumentException("'" + prefix + "' is already used as unit");
	}
	
	protected void checkNotAlreadyTakenSI(List<String> prefixes) {
		for(String prefix : prefixes) {
			checkNotAlreadyTakenSI(prefix);
		}
	}
	
	@Override
	public void setUnits(List<String> units) {
		this.units = new ArrayList<String>();
		if(units == null) throw new NullPointerException("units == null");
		checkNoIllegalChars(units);
		checkNotAlreadyTakenSI(units);
		
		this.units = new ArrayList<String>(units);
	}

	@Override
	public void setUnits(String... units) {
		setUnits(Arrays.asList(units));
	}
	
	@Override
	public void clearUnits() {
		units.clear();
	}

	@Override
	public BigDecimalSI parseFirst(String toParse) {
		List<BigDecimalSI> list = parse(toParse);
		if(list.size() == 0) return null;
		return list.get(0);
	}
}
