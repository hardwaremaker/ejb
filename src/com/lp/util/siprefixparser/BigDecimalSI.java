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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class BigDecimalSI extends BigDecimal {
	private static final long serialVersionUID = 4332156715801177664L;
	
	public static final String YOTTA = 	"Y";
	public static final String ZETTA = 	"Z";
	public static final String EXA =	"E";
	public static final String PETA =	"P";
	public static final String TERA = 	"T";
	public static final String GIGA = 	"G";
	public static final String MEGA = 	"M";
	public static final String KILO = 	"k";
	public static final String MILLI = 	"m";
	public static final String MICRO =	"\u00B5";
	public static final String NANO = 	"n";
	public static final String PIKO =	"p";
	public static final String FEMTO =	"f";
	public static final String ATTO =	"a";
	public static final String ZEPTO =	"z";
	public static final String YOKTO =	"y";
	
	private String unit;
	
	private static final Map<Integer, String> prefixes = new HashMap<Integer, String>() {
		
		private static final long serialVersionUID = 1L;
		{
			put(24, YOTTA);
			put(21, ZETTA);
			put(18, EXA);
			put(15, PETA);
			put(12, TERA);
			put(9, GIGA);
			put(6, MEGA);
			put(3, KILO);
			put(0, "");
			put(-3, MILLI);
			put(-6, MICRO);
			put(-9, NANO);
			put(-12, PIKO);
			put(-15, FEMTO);
			put(-18, ATTO);
			put(-21, ZEPTO);
			put(-24, YOKTO);
		}
	};

	/**
	 * Gibt bei toString() einen mit SI Pr&auml;fix formatierten String zur&uuml;ck.
	 * @param bd der Wert
	 * @param unit die Einheit (nicht SI, sonder wirklich zB. 'F' f&uuml;r Farad
	 */
	public BigDecimalSI(BigDecimal bd, String unit) {
		this(bd.toString(), unit);
	}
	/**
	 * Gibt bei toString() einen mit SI Pr&auml;fix formatierten String zur&uuml;ck.
	 * @param bd der Wert
	 * @param unit die Einheit (nicht SI, sonder wirklich zB. 'F' f&uuml;r Farad
	 */
	public BigDecimalSI(String bd, String unit) {
		super(bd);
		this.unit = unit == null ? "" : unit;
	}

	/**
	 * Gibt bei toString() einen mit SI Pr&auml;fix formatierten String zur&uuml;ck.
	 * @param bd der Wert
	 */
	public BigDecimalSI(BigDecimal bd) {
		this(bd.toString());
	}
	/**
	 * Gibt bei toString() einen mit SI Pr&auml;fix formatierten String zur&uuml;ck.
	 * @param bd der Wert
	 */
	public BigDecimalSI(String bd) {
		this(bd, null);
	}
	
	public String toSIString() {
		return toSIString(true);
	}
	
	public String toSIString(boolean withUnit) {
		int scale = 0;
		BigDecimal value = new BigDecimal(toString());
		BigDecimal thousand = new BigDecimal("1000");
//		System.out.println(value.toString());
		while(value.compareTo(thousand) >= 0 || value.scale() < 0) {
			int s = value.scale();
			value = value.movePointLeft(3).setScale(s+3);
			//scale+3 da movePointLeft() den Scale auf 0 setzt wenn er negativ wird
//			System.out.println("<<3");
//			System.out.println(value.toString());
			scale += 3;
		}
		while(value.compareTo(BigDecimal.ZERO)!=0 && value.compareTo(BigDecimal.ONE) < 0 && value.scale() > 2) {
			value = value.movePointRight(3);
			
//			System.out.println(">>3");
//			System.out.println(value.toString());
			
			scale -= 3;
		}
		String prefix = prefixes.get(scale);
		if(prefix == null)
			prefix = "*10^"+scale;
		return String.format("%." + value.scale() + "f", value) + prefix + (withUnit?unit:"");
	}
	
	public String getUnit() {
		return unit;
	}
	
	@Override
	public boolean equals(Object x) {
		if(!(x instanceof BigDecimalSI)) return false;
		if(!x.toString().equals(toString())) return false;
		if(!((BigDecimalSI)x).unit.equals(unit)) return false;
		return true;
	}
}
