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
package com.lp.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebot.service.AngebotFac;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelReportFac;
import com.lp.server.artikel.service.LagerFac;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.projekt.service.ProjektFac;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.server.util.logger.LpLogger;

/**
 * <p>
 * <b>frame</b><br/>
 * HelperReport Funktionen und Konstanten, die client- und serverseitig
 * verwendbar sind. <br/>
 * Wenn hier etwas ge&auml;ndert wird, hat das Auswirkungen auf bestimmte
 * Reports.
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004
 * </p>
 * <p>
 * Erstellungsdatum 2007-08-21
 * </p>
 * <br/>
 * 
 * @author ck
 * @version 1.0
 */

public class HelperReport {

	/**
	 * Prueft einen String, ob er in Code39 darstellbar ist Erlaubt sind :
	 * A-Z,0-9, %, +, $, /, ., - , BLANK Blank ist nur als Fueller zwischen
	 * Artikelnummer und Herstellerkennung erlaubt
	 * 
	 * @param sString
	 *            String
	 * @throws EJBExceptionLP
	 * @return boolean
	 */
	public static boolean pruefeObCode39Konform(String sString) {
		if (sString == null) {
			return false;
		}
		for (int i = 0; i < sString.length(); i++) {

			char c = sString.charAt(i);

			// 0-9
			if (c > 47 && c < 58) {
			}
			// A-Z
			else if (c > 64 && c < 91) {
			}
			// %
			else if (c == 37) {
			}
			// +
			else if (c == 43) {
			}
			// $
			else if (c == 36) {
			}
			// /
			else if (c == 47) {
			}
			// -
			else if (c == 45) {
			}
			// BLANK
			else if (c == 32) {
			} else {
				return false;
			}

		}
		return true;
	}

	public static boolean pruefeObCode128Konform(String sString) {
		if (sString == null) {
			return false;
		}
		for (int i = 0; i < sString.length(); i++) {

			char c = sString.charAt(i);

			// !
			if (c == 33) {
			}
			// #$%'()0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz{|}~
			else if (c > 34 && c < 127) {
			}
			// BLANK
			else if (c == 32) {
			} else {
				return false;
			}

		}
		return true;
	}

	public static String wandleUmNachCode39(String input) {

		if (input != null) {
			input = ersetzeUmlaute(input);
			input = input.toUpperCase();
			StringBuffer sb = new StringBuffer(input);
			for (int i = 0; i < sb.length(); i++) {

				char c = sb.charAt(i);

				// 0-9
				if (c > 47 && c < 58) {
				}
				// A-Z
				else if (c > 64 && c < 91) {
				}
				// %
				else if (c == 37) {
				}
				// +
				else if (c == 43) {
				}
				// $
				else if (c == 36) {
				}
				// /
				else if (c == 47) {
				}
				// -
				else if (c == 45) {
				}
				// .
				else if (c == 46) {
				}
				// BLANK
				else if (c == 32) {
				} else {
					c = '-';
					sb.setCharAt(i, c);
				}

			}
			input = new String(sb);
		}
		return input;
	}

	public static Double time2Double(java.sql.Time time) {
		if (time != null) {

			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(time.getTime());
			double hour = c.get(Calendar.HOUR_OF_DAY);
			double minuten = c.get(Calendar.MINUTE);
			double sekunden = c.get(Calendar.SECOND);

			double minutendecimal = minuten / 60;
			double sekundendecimal = sekunden / 3600;
			// minutendecimal = Math.round((minutendecimal+sekundendecimal) *
			// 100) / 100f;
			double fBeginn = hour + minutendecimal + sekundendecimal;

			return new Double(fBeginn);
		} else {
			return null;
		}
	}

	public static Object fuehreSQLQueryAus(String anSQLString) {
		PooledDatasource pds = PooledDatasource.getInstance();
		java.sql.Connection sqlcon = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			ParameterFac parameterFac = (ParameterFac) new InitialContext()
					.lookup("lpserver/ParameterFacBean/remote");

			String url = parameterFac.getAnwenderparameter(
					ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.ANWENDERPARAMETER_REPORT_CONNECTION_URL)
					.getCWert();
			// "jdbc:postgresql://" + "localhost" + "/" + "LP",
			if (url.trim().length() > 0) {
				/*
				 * if (url.contains("postgresql")) {
				 * Class.forName("org.postgresql.Driver"); sqlcon =
				 * java.sql.DriverManager.getConnection(url, "hvguest",
				 * "h4gzfdavfs"); } if (url.contains("jtds")) {
				 * Class.forName("net.sourceforge.jtds.jdbc.Driver"); sqlcon =
				 * javax.sql.DriverManager.getConnection(url, "hvguest",
				 * "h4gzfdavfs"); }
				 */
				pds.setUrl(url);
				// if (!pds.isInitalized())
				// pds.initalize(url);
				sqlcon = pds.getConnection();
				statement = sqlcon.createStatement();
				statement.execute(anSQLString);
				rs = statement.getResultSet();
				ResultSetMetaData rsmd = rs.getMetaData();

				if (rs.next()) {
					if (rsmd.getColumnCount() > 0) {
						return rs.getObject(1);
					}
				} else {
					return null;
				}
			}
		} catch (Throwable ex) {
			ILPLogger myLogger = LPLogService.getInstance().getLogger(
					HelperReport.class);
			myLogger.error(ex.getMessage());
			System.out.println(ex.getMessage());
		} finally {
			// connection wieder freigeben
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					//
					System.out.println(e.getMessage());
					ILPLogger myLogger = LPLogService.getInstance().getLogger(
							HelperReport.class);
					myLogger.error("Resultset Close: " + e.getMessage());
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					//
					System.out.println(e.getMessage());
					ILPLogger myLogger = LPLogService.getInstance().getLogger(
							HelperReport.class);
					myLogger.error("Statement Close: " + e.getMessage());
				}
			}
			if (sqlcon != null) {
				try {
					sqlcon.close();
				} catch (SQLException e) {
					//
					System.out.println(e.getMessage());
					ILPLogger myLogger = LPLogService.getInstance().getLogger(
							HelperReport.class);
					myLogger.error("Connection Close: " + e.getMessage());
				}
			}
		}
		return null;

	}

	public static String ersetzeUmlaute(String input) {
		if (input != null) {
			input = input.replaceAll("\u00F6", "oe");
			input = input.replaceAll("\u00D6", "Oe");
			input = input.replaceAll("\u00E4", "ae");
			input = input.replaceAll("\u00C4", "Ae");
			input = input.replaceAll("\u00FC", "ue");
			input = input.replaceAll("\u00DC", "Ue");
			input = input.replaceAll("\u00DF", "ss");

		}
		return input;
	}

	public static String wandleUmNachCode128(String input) {

		if (input != null) {

			input = ersetzeUmlaute(input);

			StringBuffer sb = new StringBuffer(input);

			for (int i = 0; i < sb.length(); i++) {

				char c = sb.charAt(i);

				// 0-9
				if (c > 31 && c < 127) {
				} else {
					c = '-';
					sb.setCharAt(i, c);
				}

			}
			input = new String(sb);
		}
		return input;
	}

	public static String ersetzeUmlauteUndSchneideAb(String input,
			int maxStellen) {
		input = ersetzeUmlaute(input);

		if (input.length() > maxStellen) {
			input = input.substring(0, maxStellen);
		}
		return input;
	}

	public String test() {
		return "test";
	}

	public static String getWochentag(java.util.Locale locale,
			java.sql.Timestamp tDatum) {
		if (tDatum != null) {
			String[] kurzeWochentage = new DateFormatSymbols(locale)
					.getWeekdays();

			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(tDatum.getTime());
			return kurzeWochentage[c.get(Calendar.DAY_OF_WEEK)];

		} else {
			return null;
		}
	}

	public static String ganzzahligerBetragInWorten(Integer betrag) {

		String[] ziffern = new String[] { "null", "eins", "zwei", "drei",
				"vier", "f\u00FCnf", "sechs", "sieben", "acht", "neun" };

		if (betrag != null) {
			String sZahl = "";
			String s = betrag.toString();
			for (int i = 0; i < s.length(); i++) {
				int j = new Integer(s.substring(i, i + 1));

				new String();
				if (i == 0) {
					sZahl += ziffern[j];
				} else {
					sZahl += "-" + ziffern[j];
				}

			}
			return sZahl;
		}

		return null;
	}

	public static Integer getCalendarOfTimestamp(java.sql.Timestamp tTimestamp,
			Locale locale) {
		Calendar cal = Calendar.getInstance(locale);
		cal.setMinimalDaysInFirstWeek(4);
		cal.setTime(tTimestamp);
		return new Integer(cal.get(Calendar.WEEK_OF_YEAR));
	}

	/**
	 * Ermittelt den angegebenen Subreportnamen und liefert die Daten
	 * zur&uuml;ck
	 * 
	 * @param sString
	 *            der Name des Subreports
	 * @return Subreport
	 */
	public static LPDatenSubreport getSubreportAusStringMitKommaGetrennt(
			String sString) {
		String[] teile = sString.split(",");
		String[] fieldnames = new String[] { "Feld", };
		Object[][] dataSubKD = new Object[teile.length][1];
		for (int i = 0; i < teile.length; i++) {
			dataSubKD[i][0] = teile[i];
		}
		return new LPDatenSubreport(dataSubKD, fieldnames);
	}

	public static LPDatenSubreport getSubreportEnthaltenesLosIstMaterial(
			String artikelnummer, String chargennummer,
			TheClientDto theClientDto) throws Throwable {
		LagerFac lagerFac = (LagerFac) new InitialContext()
				.lookup("lpserver/LagerFacBean/remote");
		return lagerFac.getSubreportEnthaltenesLosIstMaterial(artikelnummer,
				chargennummer, theClientDto);
	}

	public static LPDatenSubreport getSubreportAllergene(String artikelnummer,
			TheClientDto theClientDto) throws Throwable {
		ArtikelFac artikelFac = (ArtikelFac) new InitialContext()
				.lookup("lpserver/ArtikelFacBean/remote");

		ArtikelDto aDto = artikelFac.artikelFindByCNrMandantCNrOhneExc(
				artikelnummer, theClientDto.getMandant());

		if (aDto != null) {
			ArtikelReportFac artikelReportFac = (ArtikelReportFac) new InitialContext()
					.lookup("lpserver/ArtikelReportFacBean/remote");

			return artikelReportFac.getSubreportAllergene(aDto.getIId(),
					theClientDto);
		} else {
			return null;
		}

	}

	public static LPDatenSubreport getSubreportAusPDFFile(String pdfFile,
			int resolution) throws Throwable {

		SystemFac systemFac = (SystemFac) new InitialContext()
				.lookup("lpserver/SystemFacBean/remote");
		byte[][] bilder = systemFac.konvertierePDFFileInEinzelneBilder(pdfFile,
				resolution);

		if (bilder != null) {

			String[] fieldnames = new String[] { "Bild", };
			Object[][] dataSubKD = new Object[bilder.length][1];
			for (int i = 0; i < bilder.length; i++) {
				dataSubKD[i][0] = Helper.byteArrayToImage(bilder[i]);
			}
			return new LPDatenSubreport(dataSubKD, fieldnames);
		} else {
			return null;
		}

	}

	public static String getMonatVonJahrUndWoche(Integer iJahr, Integer iWoche,
			Locale locale) {

		DateFormatSymbols symbols = new DateFormatSymbols(locale);
		String[] defaultMonths = symbols.getMonths();

		Calendar c = Calendar.getInstance(locale);
		c.set(Calendar.YEAR, iJahr);
		c.set(Calendar.WEEK_OF_YEAR, iWoche);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());

		return defaultMonths[c.get(Calendar.MONTH)];
	}

	public static Integer getCalendarWeekOfDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return new Integer(cal.get(Calendar.WEEK_OF_YEAR));
	}

	public static Integer getCalendarWeekOfDate(String sDate, Locale locale) {
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, locale);
		Date dateParsed = null;
		try {
			dateParsed = df.parse(sDate);
		} catch (ParseException ex) {
			return new Integer(0);
		}

		return getCalendarWeekOfDate(dateParsed);
	}

	public static BigDecimal rundeKaufmaennisch(BigDecimal bigDecimal,
			int stellen) {
		return Helper.rundeKaufmaennisch(bigDecimal, stellen);
	}

	public static Boolean pruefeEndsumme(BigDecimal bdReportValue,
			BigDecimal bdHvValue, Double dAbweichung) {
		bdReportValue = bdReportValue.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		/*
		 * if(bdReportValue.doubleValue() != bdHvValue.doubleValue()){
		 * BigDecimal abw = bdReportValue.subtract(bdHvValue); if(dAbweichung ==
		 * null) dAbweichung = new Double(0.10); if(abw.abs().doubleValue() >
		 * dAbweichung){ EJBExceptionLP ex = new
		 * EJBExceptionLP(EJBExceptionLP.FEHLER_RECHNUNG_HVWERT_UNGLEICH_REPORTWERT
		 * ,null); ArrayList<Object> al = new ArrayList<Object>();
		 * al.add(bdReportValue.doubleValue()+" != " +bdHvValue.doubleValue() );
		 * ex.setAlInfoForTheClient(al); throw ex; } }
		 */
		return new Boolean(true);
	}

	// SP2199
	public static String berechneKWJahr(java.util.Date d) {
		String s = null;
		if (d != null) {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(d.getTime());
			c.get(Calendar.WEEK_OF_YEAR);
			s = c.get(Calendar.WEEK_OF_YEAR) + "/"
					+ Helper.berechneJahrDerKW(c);
			;
		}
		return s;
	}

	public static String entferneStyleInformation(String sI) {
		return Helper.strippHTML(sI);
	}

	public static Boolean pruefeEndsumme(BigDecimal bdReportNettoValue,
			BigDecimal bdHvValue, Double dAbweichung, String listeMwstsaetze,
			Locale reportLocale) {
		bdReportNettoValue = bdReportNettoValue.setScale(2,
				BigDecimal.ROUND_HALF_EVEN);

		BigDecimal bdMwstsatz = new BigDecimal(0.00);

		if (listeMwstsaetze != null && listeMwstsaetze.length() > 0) {

			String[] mwstsaetze = listeMwstsaetze.split("\n");
			for (int i = 0; i < mwstsaetze.length; i++) {
				java.text.DecimalFormat decfrm = (java.text.DecimalFormat) NumberFormat
						.getInstance(reportLocale);
				decfrm.setParseBigDecimal(true);
				ParsePosition pos = new ParsePosition(0);
				BigDecimal bd = (BigDecimal) decfrm.parse(mwstsaetze[i], pos);
				bdMwstsatz = bdMwstsatz.add(bd);
			}
			bdReportNettoValue = bdReportNettoValue.add(bdMwstsatz);
		}

		if (bdReportNettoValue.doubleValue() != bdHvValue.doubleValue()) {
			BigDecimal abw = bdReportNettoValue.subtract(bdHvValue);
			if (dAbweichung == null)
				dAbweichung = new Double(0.10);
			if (abw.abs().doubleValue() > dAbweichung) {
				EJBExceptionLP ex = new EJBExceptionLP(
						EJBExceptionLP.FEHLER_RECHNUNG_HVWERT_UNGLEICH_REPORTWERT,
						"");
				ArrayList<Object> al = new ArrayList<Object>();
				al.add(bdReportNettoValue.doubleValue() + " != "
						+ bdHvValue.doubleValue());
				ex.setAlInfoForTheClient(al);
				throw ex;
			}
		}

		return new Boolean(true);
	}

	public Context context;

	public static Double getSummeIstZeitEinesBeleges(String belegartCNr,
			String belegnummer, TheClientDto theClientDto) {

		try {
			ZeiterfassungFac zeiterfassungFac = (ZeiterfassungFac) new InitialContext()
					.lookup("lpserver/ZeiterfassungFacBean/remote");

			Double summe = new Double(-1);

			// Suche die belegId

			Integer belegIId = null;

			belegartCNr = belegartCNr.trim();

			if (belegartCNr.equals(LocaleFac.BELEGART_ANGEBOT.trim())) {
				AngebotFac angebotFac = (AngebotFac) new InitialContext()
						.lookup("lpserver/AngebotFacBean/remote");

				if (belegnummer != null && belegnummer.startsWith("AG")) {
					belegnummer = belegnummer.substring(2);
				}

				AngebotDto agDto = angebotFac.angebotFindByCNrMandantCNrOhneEx(
						belegnummer, theClientDto.getMandant());
				if (agDto != null) {
					belegIId = agDto.getIId();
				}
			} else if (belegartCNr.equals(LocaleFac.BELEGART_AUFTRAG.trim())) {
				AuftragFac auftragFac = (AuftragFac) new InitialContext()
						.lookup("lpserver/AuftragFacBean/remote");
				AuftragDto abDto = auftragFac
						.auftragFindByMandantCNrCNrOhneExc(
								theClientDto.getMandant(), belegnummer);
				if (abDto != null) {
					belegIId = abDto.getIId();
				}
			} else if (belegartCNr.equals(LocaleFac.BELEGART_LOS.trim())) {
				FertigungFac fertigungFac = (FertigungFac) new InitialContext()
						.lookup("lpserver/FertigungFacBean/remote");
				if (belegnummer != null && belegnummer.startsWith("LO")) {
					belegnummer = belegnummer.substring(2);
				}
				LosDto losDto = fertigungFac.losFindByCNrMandantCNrOhneExc(
						belegnummer, theClientDto.getMandant());
				if (losDto != null) {
					belegIId = losDto.getIId();
				}
			} else if (belegartCNr.equals(LocaleFac.BELEGART_PROJEKT.trim())) {
				ProjektFac projektFac = (ProjektFac) new InitialContext()
						.lookup("lpserver/ProjektFacBean/remote");
				if (belegnummer != null && belegnummer.startsWith("PJ")) {
					belegnummer = belegnummer.substring(2);
				}
				ProjektDto pjDto = projektFac
						.projektFindByMandantCNrCNrOhneExc(
								theClientDto.getMandant(), belegnummer);
				if (pjDto != null) {
					belegIId = pjDto.getIId();
				}
			}

			if (belegIId != null) {
				summe = zeiterfassungFac.getSummeZeitenEinesBeleges(
						belegartCNr, belegIId, null, null, null, null,
						theClientDto);
			}
			return summe;

		} catch (Throwable t) {
			return new Double(-1);

		}
	}

	public static String getMediastandardTextHtml(String cNr,
			String mandantCNr, Locale locale) {
		if (cNr != null && mandantCNr != null && locale != null) {

			try {
				MediaFac mediaFac = (MediaFac) new InitialContext()
						.lookup("lpserver/MediaFacBean/remote");
				return mediaFac.mediastandardTextHtmlFindByCNrMandantCNrLocale(
						cNr, mandantCNr, locale);

			} catch (Throwable t) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER,
						new Exception(t));
			}

		} else {
			return null;
		}
	}

	public static String laenderartZweierLaender(String lkzKunde,
			String lkzBasis, String uidNummer, java.sql.Timestamp tsEUMitglied) {
		if (lkzKunde != null && lkzBasis != null) {
			if (lkzKunde.equals(lkzBasis)) {
				return FinanzFac.LAENDERART_INLAND;
			} else {
				try {
					SystemFac systemFac = (SystemFac) new InitialContext()
							.lookup("lpserver/SystemFacBean/remote");
					LandDto landDto = systemFac.landFindByLkz(lkzKunde);

					if (landDto.getEUMitglied() != null
							&& landDto.getEUMitglied().before(tsEUMitglied)) {
						if (uidNummer != null) {
							return FinanzFac.LAENDERART_EU_AUSLAND_MIT_UID;
						} else {
							return FinanzFac.LAENDERART_EU_AUSLAND_OHNE_UID;
						}
					} else {
						return FinanzFac.LAENDERART_DRITTLAND;
					}
				} catch (Throwable t) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER,
							new Exception(t));
				}
			}
		} else {
			return FinanzFac.LAENDERART_INLAND;
		}
	}

	public static BigDecimal getLiquiditaetsKontostand(
			Integer geschaeftsjahrIId, TheClientDto theClientDto) {
		try {
			FinanzServiceFac finanzServiceFac = (FinanzServiceFac) new InitialContext()
					.lookup("lpserver/FinanzServiceFacBean/remote");
			return finanzServiceFac.getLiquiditaetsKontostand(
					geschaeftsjahrIId, theClientDto);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

	}

	public static Object[] haengeArrayAn(Object[] array1, Object[] array2) {
		if (array1 == null && array2 == null)
			return null;
		if (array1 == null)
			return array2;
		if (array2 == null)
			return array1;

		Object[] newArray = new Object[array1.length + array2.length];
		System.arraycopy(array1, 0, newArray, 0, array1.length);
		System.arraycopy(array2, 0, newArray, array1.length, array2.length);
		return newArray;
	}

	public static int berechneModulo10(String nummer) {
		// 'nummer' darf nur Ziffern zwischen 0 und 9 enthalten!

		int[] tabelle = { 0, 9, 4, 6, 8, 2, 7, 1, 3, 5 };
		int uebertrag = 0;

		for (int i = 0; i < nummer.length(); i++)
			uebertrag = tabelle[(uebertrag + Integer.parseInt(nummer.substring(
					i, i + 1))) % 10];

		return (10 - uebertrag) % 10;
	}

	public static Time double2Time(Number zahl) {

		Time t = null;
		if (zahl != null) {
			double d = (zahl.doubleValue() * (double) 3600000) - 3600000;

			t = new Time((long) d);
		}
		return t;
	}

	public static String berechneModulo10Str(String nummer) {
		int m10 = berechneModulo10(nummer);
		return String.format("%d", m10).trim();
	}

	@SuppressWarnings("unchecked")
	public static List sortList(List list) {

		// SP3293 Exception wenn NULL Wert im Sortierfeld enthalten
		for (int i = 0; i < list.size(); i++) {
			Object[] o = (Object[]) list.get(i);
			if (o.length > 0) {
				if (o[0] == null) {
					EJBExceptionLP ex = new EJBExceptionLP(
							EJBExceptionLP.FEHLER_SORTIER_LISTE_ENTHAELT_NULL,
							"Liste NULL in Element " + i);
					throw ex;
				}
			}
		}

		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				Object[] oo1 = (Object[]) o1;
				Object[] oo2 = (Object[]) o2;
				return (((String) oo1[0]).compareTo((String) oo2[0]));
			}
		});

		return list;
	}

	@SuppressWarnings("unchecked")
	public static List sortListNoNull(List list) {

		// PJ18934 Null-Elemente entfernen und sortieren
		List listnew = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			Object[] o = (Object[]) list.get(i);
			if (o.length > 0)
				if (o[0] != null)
					listnew.add(list.get(i));
		}
		return sortList(listnew);
	}

	/**
	 * Wandelt einen BigDecimal im AT Format in einen BigDecimal um
	 * 
	 * @param bigDecimal
	 *            ist die Stringrepresentation des BigDecimal
	 * @return null wenn bigDecimal nicht umgewandelt werden konnte, ansonsten
	 *         der BigDecimal
	 */
	public static BigDecimal toBigDecimal(String bigDecimal) {
		return toBigDecimal(bigDecimal, new Locale("de", "AT"));
	}

	/**
	 * Wandelt einen BigDecimalString im angegebenen Locale in einen BigDecimal
	 * um
	 * 
	 * @param bigDecimal
	 *            die Stringrepresentation in der jeweiligen Locale
	 * @param stringLocale
	 *            die Locale
	 * @return null wenn bigDecimal nicht in einen BigDecimal umgewandelt werden
	 *         kann
	 */
	public static BigDecimal toBigDecimal(String bigDecimal, Locale stringLocale) {
		if (null == bigDecimal)
			return null;

		BigDecimal bd = null;

		if (null == stringLocale) {
			stringLocale = new Locale("de", "AT");
		}

		try {
			DecimalFormat nf = (DecimalFormat) NumberFormat
					.getInstance(stringLocale);
			nf.setParseBigDecimal(true);

			bd = (BigDecimal) nf.parse(bigDecimal);
			return bd;
		} catch (ParseException e) {
		}

		return null;
	}

	/**
	 * Wandelt einen String in einen BigInteger um
	 * 
	 * @param bigInteger
	 * @return BigInteger des Strings, oder null bei Umwandlungsfehlern
	 */
	public static BigInteger toBigInteger(String bigInteger) {
		if (null == bigInteger)
			return null;

		BigInteger value = null;

		try {
			value = new BigInteger(bigInteger);
		} catch (NumberFormatException e) {
		}

		return value;
	}

	public static BufferedImage bildUm90GradDrehenWennNoetig(
			BufferedImage inputImage) {

		int width = inputImage.getWidth();
		int height = inputImage.getHeight();

		if (width > height) {

			BufferedImage returnImage = new BufferedImage(height, width,
					BufferedImage.TYPE_INT_RGB);

			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					returnImage.setRGB(y, width - x - 1,
							inputImage.getRGB(x, y));
					// Again check the Picture for better understanding
				}
			}
			return returnImage;
		} else {
			return inputImage;
		}

	}

	public static BufferedImage bildDrehen(BufferedImage inputImage, int angle) {

		double sin = Math.abs(Math.sin(Math.toRadians(angle))), cos = Math

		.abs(Math.cos(Math.toRadians(angle)));

		int w = inputImage.getWidth(null), h = inputImage.getHeight(null);

		int neww = (int) Math.floor(w * cos + h * sin), newh = (int) Math

		.floor(h * cos + w * sin);

		BufferedImage bimg = new BufferedImage(neww, newh,

		BufferedImage.TYPE_INT_ARGB);

		Graphics2D g = bimg.createGraphics();

		g.translate((neww - w) / 2, (newh - h) / 2);

		g.rotate(Math.toRadians(angle), w / 2, h / 2);

		g.drawRenderedImage(inputImage, null);

		g.dispose();

		return bimg;

	}

	/**
	 * Wandelt einen String in einen BigInteger um unter Ber&uuml;cksichtigung
	 * des Radix
	 * 
	 * @param bigInteger
	 * @return null oder den BigInteger aus dem String
	 */
	public static BigInteger toBigInteger(String bigInteger, int radix) {
		if (null == bigInteger)
			return null;

		BigInteger value = null;

		try {
			value = new BigInteger(bigInteger, radix);
		} catch (NumberFormatException e) {
		}

		return value;
	}

	/**
	 * Wandelt einen String in einen Integer um
	 * 
	 * @param integer
	 * @return null wenn der STring ein ung&uuml;ltiges Format hat, ansonsten
	 *         den Integer
	 */
	public static Integer toInteger(String integer) {
		if (null == integer)
			return null;

		Integer value = null;

		try {
			value = new Integer(integer);
		} catch (NumberFormatException e) {
		}

		return value;
	}

	public static String seriennummerErzeugen(String vorlage, int exemplar) {
		String snr = null;

		if (vorlage != null) {
			int iStartZahl = -1;
			int iEndeZahl = -1;
			boolean bEndeFound = false;
			int i = vorlage.length() - 1;
			while (i >= 0) {

				char c = vorlage.charAt(i);
				// wenn 0-9
				if (c > 47 && c < 58) {
					iEndeZahl = i;
					iStartZahl = iEndeZahl;

					for (int j = i; j >= 0; j--) {
						char d = vorlage.charAt(j);
						if (d > 47 && d < 58) {
							iStartZahl = j;
							if (j == 0) {
								bEndeFound = true;
							}
						} else {
							bEndeFound = true;
							break;
						}
					}
				}
				i--;
				if (bEndeFound) {
					break;
				}
			}

			if (iStartZahl >= 0 && iEndeZahl >= 0) {
				String zahlenteil = vorlage
						.substring(iStartZahl, iEndeZahl + 1);

				long zahl = new Long(zahlenteil);
				zahl = zahl + exemplar;
				String zahlenteilNeu = new String(zahl + "");

				int iNeueLaenge = zahlenteilNeu.length();
				if (iNeueLaenge < zahlenteil.length()) {
					iNeueLaenge = zahlenteil.length();
				}
				zahlenteilNeu = Helper.fitString2LengthAlignRight(zahl + "",
						iNeueLaenge, '0');
				// Neue Artikelnummer zusammenbauen

				return vorlage.substring(0, iStartZahl) + zahlenteilNeu
						+ vorlage.substring(iEndeZahl + 1);

			}
		}

		return snr;
	}

	public static Object putInMap(TreeMap map, String key, Object value) {
		synchronized (map) {
			if (map == null || key == null)
				return value;

			if (value == null)
				value = new Integer(0);

			if (value instanceof Integer) {
				Integer mapValue = (Integer) map.get(key);
				if (mapValue != null) {
					if (((Integer) value).compareTo(mapValue) > 0) {
						map.put(key, value);
					}
				} else {
					map.put(key, value);
				}
			}

			return value;
		}
	}
}
