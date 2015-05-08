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
package com.lp.server.util;

import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.hibernate.HibernateException;
import org.hibernate.Session;

public class HelperWebshop {
	public static Date parseDateString(String dateString) {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date d = (Date) formatter.parse(dateString) ;
			return d ;
		} catch(ParseException e) {
		}

		return null ;
	}
	
	public static Date parseDateTimeString(String dateTimeString) {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			Date d = (Date) formatter.parse(dateTimeString) ;
			return d ;
		} catch(ParseException e) {
		}

		return null ;
	}
	
	
	public static boolean isEmptyString(String string) {
		return null == string || 0 == string.trim().length() ;
	}
	
	public static String getEmptyStringForNull(String value) {
		return value == null ? "" : value ;
	}
	
	public static Date normalizeDate(Date changedDate, Locale locale) {
		Calendar cal = Calendar.getInstance(locale) ;
		cal.setTime(changedDate) ;
		cal.set(Calendar.HOUR_OF_DAY, 0) ;
		cal.set(Calendar.MINUTE, 0) ;
		cal.set(Calendar.SECOND, 0) ;
		cal.set(Calendar.MILLISECOND, 0) ;
		return cal.getTime() ;
	}

	public static Date normalizeDateTime(Date changedDate, Locale locale) {
		Calendar cal = Calendar.getInstance(locale) ;
		cal.setTime(changedDate) ;
		cal.set(Calendar.SECOND, 0) ;
		cal.set(Calendar.MILLISECOND, 0) ;
		return cal.getTime() ;
	}
	
	public static Timestamp normalizeDateTimeAsTimestamp(Date changedDate, Locale locale) {
		return new Timestamp(normalizeDateTime(changedDate, locale).getTime()) ;
	}
	
	public static Timestamp normalizeDateAsTimestamp(Date changedDate, Locale locale) {
		return new Timestamp(normalizeDate(changedDate, locale).getTime()) ;
	}
	
	public static void closeFLRSession(Session session) {
		if(session != null) {
			try {
				session.close() ;
			} catch(HibernateException e) {
			}
		}		
	}
	
	public static String unescapeHtml(String htmlString) {
		if(htmlString == null) return "" ;

		String parsed = htmlString.replaceAll("&amp;", "&");
		parsed = parsed.replaceAll("&quot;", "\"");
		parsed = parsed.replaceAll("&lt;", "<");
		parsed = parsed.replaceAll("&gt;", ">");

		return parsed ;		
	}
	
	public static String formatAsIso8601Timestamp(Date date) {
		TimeZone ts = TimeZone.getTimeZone("UTC") ;
		DateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'") ;
		isoFormat.setTimeZone(ts) ;
		return  isoFormat.format(date) ;
	}	
	
	/**
	 * Kodiert einen ISO-8859-1 erneut
	 * <p>Bereits "falsch" codierte Strings erneut kodieren.</p>
	 * 
	 * @param value der zu de/en-codierende String
	 * @return in Plattform Encoding gewandelter String
	 */
	public static String reencode(String value) {
		return new String(Charset.forName("ISO-8859-1").encode(value).array()) ;		
	}
	
	/**
	 * Alle Zeichen > 127 und '?' entfernen
	 * <p>Wenn schon sonst nichts mehr geht :(</p>
	 * @param value der String von dem die Umlaute entfernt werden sollen
	 * @return String der keine Sonderzeichen mehr enth&auml;lt
	 */
	public static String removeUmlauts(String value) {
		if(value == null) return value ;
		
		String s = "" ;
		for(int i = 0 ; i < value.length(); i++) {
			char c = value.charAt(i) ;
			if((c <= 127) && (c != '?')) {
				s = s + c ; 
			}
		}
		
		return s ;
	}
}
