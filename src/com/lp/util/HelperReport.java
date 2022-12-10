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

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
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
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.lang.StringUtils;

import com.lp.server.angebot.ejbfac.AngebotFacBean;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebot.service.AngebotFac;
import com.lp.server.artikel.ejbfac.ArtikelFacBean;
import com.lp.server.artikel.ejbfac.ArtikelReportFacBean;
import com.lp.server.artikel.ejbfac.ArtikelkommentarFacBean;
import com.lp.server.artikel.ejbfac.LagerFacBean;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelReportFac;
import com.lp.server.artikel.service.ArtikelkommentarFac;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ErsatztypenDto;
import com.lp.server.artikel.service.LagerFac;
import com.lp.server.auftrag.ejbfac.AuftragFacBean;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.benutzer.ejbfac.BenutzerServicesFacBean;
import com.lp.server.benutzer.service.BenutzerServicesFac;
import com.lp.server.bestellung.ejbfac.BestellungFacBean;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.fertigung.ejbfac.FertigungFacBean;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.finanz.ejbfac.BuchenFacBean;
import com.lp.server.finanz.ejbfac.FinanzServiceFacBean;
import com.lp.server.finanz.service.BuchenFac;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.lieferschein.ejbfac.LieferscheinFacBean;
import com.lp.server.lieferschein.ejbfac.LieferscheinpositionFacBean;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinpositionFac;
import com.lp.server.partner.ejbfac.LieferantFacBean;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.personal.ejbfac.MaschineFacBean;
import com.lp.server.personal.ejbfac.PersonalFacBean;
import com.lp.server.personal.ejbfac.ZeiterfassungFacBean;
import com.lp.server.personal.service.BetriebskalenderDto;
import com.lp.server.personal.service.MaschineDto;
import com.lp.server.personal.service.MaschineFac;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.personal.service.UrlaubsabrechnungDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.personal.service.ZeitmodelltagDto;
import com.lp.server.personal.service.ZeitverteilungDto;
import com.lp.server.projekt.ejbfac.ProjektFacBean;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.projekt.service.ProjektFac;
import com.lp.server.rechnung.ejbfac.RechnungFacBean;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.stueckliste.ejbfac.StuecklisteFacBean;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.stueckliste.service.StuecklisteMitStrukturDto;
import com.lp.server.stueckliste.service.StuecklisteReportFac;
import com.lp.server.system.ejbfac.MediaFacBean;
import com.lp.server.system.ejbfac.PanelFacBean;
import com.lp.server.system.ejbfac.ParameterFacBean;
import com.lp.server.system.ejbfac.SystemFacBean;
import com.lp.server.system.ejbfac.SystemServicesFacBean;
import com.lp.server.system.ejbfac.VersandFacBean;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.PanelbeschreibungDto;
import com.lp.server.system.service.PaneldatenDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.VersandFac;
import com.lp.server.system.service.VersandauftragDto;
import com.lp.server.util.FacLookup;
import com.lp.server.util.bean.SystemBean;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.util.barcode.VDA4092Checksum;
import com.lp.util.gs1.SSCCBuilder;
import com.lp.util.report.KalenderRechner;

/**
 * <p>
 * <b>frame</b><br/>
 * HelperReport Funktionen und Konstanten, die client- und serverseitig
 * verwendbar sind. <br/ Wenn hier etwas ge&auml;ndert wird, hat das
 * Auswirkungen auf bestimmte Reports.
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
	 * Prueft einen String, ob er in Code39 darstellbar ist Erlaubt sind : A-Z,0-9,
	 * %, +, $, /, ., - , BLANK Blank ist nur als Fueller zwischen Artikelnummer und
	 * Herstellerkennung erlaubt
	 * 
	 * @param sString String
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
//			ParameterFac parameterFac = (ParameterFac) new InitialContext()
//					.lookup("lpserver/ParameterFacBean/remote");

			ParameterFac parameterFac = FacLookup.lookup(new InitialContext(), ParameterFacBean.class,
					ParameterFac.class);

			String url = parameterFac.getAnwenderparameter(ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.ANWENDERPARAMETER_REPORT_CONNECTION_URL).getCWert();
			// "jdbc:postgresql://" + "localhost" + "/" + "LP",
			if (url.trim().length() > 0) {
				/*
				 * if (url.contains("postgresql")) { Class.forName("org.postgresql.Driver");
				 * sqlcon = java.sql.DriverManager.getConnection(url, "hvguest", "h4gzfdavfs");
				 * } if (url.contains("jtds")) {
				 * Class.forName("net.sourceforge.jtds.jdbc.Driver"); sqlcon =
				 * javax.sql.DriverManager.getConnection(url, "hvguest", "h4gzfdavfs"); }
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
			ILPLogger myLogger = LPLogService.getInstance().getLogger(HelperReport.class);
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
					ILPLogger myLogger = LPLogService.getInstance().getLogger(HelperReport.class);
					myLogger.error("Resultset Close: " + e.getMessage());
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					//
					System.out.println(e.getMessage());
					ILPLogger myLogger = LPLogService.getInstance().getLogger(HelperReport.class);
					myLogger.error("Statement Close: " + e.getMessage());
				}
			}
			if (sqlcon != null) {
				try {
					sqlcon.close();
				} catch (SQLException e) {
					//
					System.out.println(e.getMessage());
					ILPLogger myLogger = LPLogService.getInstance().getLogger(HelperReport.class);
					myLogger.error("Connection Close: " + e.getMessage());
				}
			}
		}
		return null;

	}

	public static String konvertiereZeitDezimalInHHMMSS(BigDecimal bd, int iNachkommastellen, boolean bMitSekunden) {

		if (bd != null) {
			bd = bd.setScale(iNachkommastellen, BigDecimal.ROUND_HALF_EVEN);

			long diff = (long) (bd.doubleValue() * 3600000);

			long diffSeconds = diff / 1000 % 60;
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000);

			// SP8306 Wenn Sekunden nicht angezeigt werden sollen, dann wird gerundet
			// Wenn die Sekunden > 30, dann wird auf die naechste Minute gerundet
			if (bMitSekunden == false && diffSeconds > 30) {
				diffMinutes++;

				// SP9030 Wenn von 59 auf 60 aufgerundet wurde, muessen die Minuten auf 00 und
				// die Stunde um eins aufgerundet werden
				if (diffMinutes == 60) {
					diffHours++;
					diffMinutes = 0;
				}
			}

			diffMinutes = Math.abs(diffMinutes);
			diffSeconds = Math.abs(diffSeconds);

			String minuten = diffMinutes + "";
			if (diffMinutes < 10) {
				minuten = "0" + diffMinutes;
			}

			String sekunden = diffSeconds + "";
			if (diffSeconds < 10) {
				sekunden = "0" + diffSeconds;
			}

			String restZeit = "";
			if (bMitSekunden == true) {
				restZeit = (diffHours + ":" + minuten + ":" + sekunden);
			} else {
				restZeit = (diffHours + ":" + minuten);
			}

			return restZeit;
		} else {
			return null;
		}

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

	public static String ersetzeUmlauteUndSchneideAb(String input, int maxStellen) {
		input = ersetzeUmlaute(input);

		if (input.length() > maxStellen) {
			input = input.substring(0, maxStellen);
		}
		return input;
	}

	public String test() {
		return "test";
	}

	public static String getWochentag(java.util.Locale locale, java.sql.Timestamp tDatum) {
		if (tDatum != null) {
			String[] kurzeWochentage = new DateFormatSymbols(locale).getWeekdays();

			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(tDatum.getTime());
			return kurzeWochentage[c.get(Calendar.DAY_OF_WEEK)];

		} else {
			return null;
		}
	}

	// PJ20889
	public static Integer updateLieferantBeurteilung(Integer lieferantIId, Integer iPunkte, java.util.Date tDatum,
			String klasse, TheClientDto theClientDto) throws Throwable {
		LieferantFac lieferantFac = FacLookup.lookup(new InitialContext(), LieferantFacBean.class, LieferantFac.class);
		return lieferantFac.updateLieferantbeurteilung(lieferantIId, iPunkte, tDatum, klasse, theClientDto);
	}

	public static String ganzzahligerBetragInWorten(Integer betrag) {

		String[] ziffern = new String[] { "null", "eins", "zwei", "drei", "vier", "f\u00FCnf", "sechs", "sieben",
				"acht", "neun" };

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

	public static Integer getCalendarOfTimestamp(java.sql.Timestamp tTimestamp, Locale locale) {
		Calendar cal = Calendar.getInstance(locale);
		cal.setMinimalDaysInFirstWeek(4);
		cal.setTime(tTimestamp);
		return new Integer(cal.get(Calendar.WEEK_OF_YEAR));
	}

	public static LPDatenSubreport getSubreportKalendertage(java.util.Date dVon, java.util.Date dBis,
			TheClientDto theClientDto) throws Throwable {

		dVon = Helper.cutDate(dVon);
		dBis = Helper.cutDate(dBis);

		int iTage = Helper.getDifferenzInTagen(Helper.cutDate(dVon), Helper.cutDate(dBis));
		iTage++;
		PersonalFac personalFac = FacLookup.lookup(new InitialContext(), PersonalFacBean.class, PersonalFac.class);

		ZeiterfassungFac zeiterfassungFac = FacLookup.lookup(new InitialContext(), ZeiterfassungFacBean.class,
				ZeiterfassungFac.class);

		String[] fieldnames = new String[] { "Datum", "Feiertag", "SollzeitInStunden" };

		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(dVon.getTime());

		Object[][] dataSub = new Object[iTage][3];

		for (int i = 0; i < iTage; i++) {
			dataSub[i][0] = cal.getTime();

			BetriebskalenderDto bkDto = personalFac.betriebskalenderFindByMandantCNrDDatum(
					new Timestamp(cal.getTimeInMillis()), theClientDto.getMandant(), theClientDto);

			if (bkDto != null) {
				dataSub[i][1] = bkDto.getCBez();
			}

			ZeitmodelltagDto zmTagDto = zeiterfassungFac
					.getZeitmodelltagDesFirmenzeitmodellsZuDatum(new Timestamp(cal.getTimeInMillis()), theClientDto);
			if (zmTagDto != null && zmTagDto.getUSollzeit() != null) {
				dataSub[i][2] = Helper.time2Double(zmTagDto.getUSollzeit());
			}

			cal.add(Calendar.DATE, 1);
		}
		return new LPDatenSubreport(dataSub, fieldnames);
	}

	public static LPDatenSubreport getSubreportArtikelErsatztypen(String artikelnummer, TheClientDto theClientDto)
			throws Throwable {

		ArtikelFac artikelFac = FacLookup.lookup(new InitialContext(), ArtikelFacBean.class, ArtikelFac.class);
		ArtikelDto aDto = artikelFac.artikelFindByCNrOhneExc(artikelnummer, theClientDto);
		if (aDto != null) {

			ErsatztypenDto[] etypenDtos = artikelFac.ersatztypenFindByArtikelIId(aDto.getIId());

			String[] fieldnames = new String[] { "Artikelnummer", "Bezeichnung", "Kurzbezeichnung", "Zusatzbezeichnung",
					"Zusatzbezeichnung2", "Referenznummer", "Index", "Revision", "Herstellernummer",
					"Herstellerbezeichnung" };

			Object[][] dataSub = new Object[etypenDtos.length][10];

			for (int i = 0; i < etypenDtos.length; i++) {

				ArtikelDto artikelDto = artikelFac.artikelFindByPrimaryKey(etypenDtos[i].getArtikelIIdErsatz(),
						theClientDto);

				dataSub[i][0] = artikelDto.getCNr();
				dataSub[i][1] = artikelDto.getCBezAusSpr();
				dataSub[i][2] = artikelDto.getCKBezAusSpr();
				dataSub[i][3] = artikelDto.getCZBezAusSpr();
				dataSub[i][4] = artikelDto.getCZBez2AusSpr();
				dataSub[i][5] = artikelDto.getCReferenznr();
				dataSub[i][6] = artikelDto.getCIndex();
				dataSub[i][7] = artikelDto.getCRevision();
				dataSub[i][8] = artikelDto.getCArtikelnrhersteller();
				dataSub[i][9] = artikelDto.getCArtikelbezhersteller();

			}

			return new LPDatenSubreport(dataSub, fieldnames);

		}

		return null;
	}

	public static LPDatenSubreport getSubreportOffeneZeitverteilung(Integer personalIId, TheClientDto theClientDto)
			throws Throwable {

		ZeiterfassungFac zeiterfassungFac = FacLookup.lookup(new InitialContext(), ZeiterfassungFacBean.class,
				ZeiterfassungFac.class);
		FertigungFac fertigungFac = FacLookup.lookup(new InitialContext(), FertigungFacBean.class, FertigungFac.class);

		ZeitverteilungDto[] zvDtos = zeiterfassungFac.zeitverteilungFindByPersonalIIdUndTag(personalIId,
				new Timestamp(System.currentTimeMillis()));

		if (zvDtos != null && zvDtos.length > 0) {

			String[] fieldnames = new String[] { "Buchungszeitpunkt", "Losnummer", "AG", "UAG",
					"MaschineIdentifikationsnummer", "MaschineInventarnummer", "MaschineBezeichnung" };

			Object[][] dataSub = new Object[zvDtos.length][fieldnames.length];

			for (int i = 0; i < zvDtos.length; i++) {

				LosDto losDto = fertigungFac.losFindByPrimaryKey(zvDtos[i].getLosIId());

				dataSub[i][0] = zvDtos[i].getTZeit();
				dataSub[i][1] = losDto.getCNr();

				if (zvDtos[i].getLossollarbeitsplanIId() != null) {
					LossollarbeitsplanDto saDto = fertigungFac
							.lossollarbeitsplanFindByPrimaryKey(zvDtos[i].getLossollarbeitsplanIId());
					dataSub[i][2] = saDto.getIArbeitsgangnummer();
					dataSub[i][3] = saDto.getIUnterarbeitsgang();
				}

				if (zvDtos[i].getMaschineIId() != null) {
					MaschineDto maschineDto = zeiterfassungFac.maschineFindByPrimaryKey(zvDtos[i].getMaschineIId());
					dataSub[i][4] = maschineDto.getCIdentifikationsnr();
					dataSub[i][5] = maschineDto.getCInventarnummer();
					dataSub[i][6] = maschineDto.getCBez();
				}

			}

			return new LPDatenSubreport(dataSub, fieldnames);

		}

		return null;
	}

	public static <T> Predicate<T> distinctByKey(Function<T, Object> function) {
	    Set<Object> seen = new HashSet<>();
	    return t -> seen.add(function.apply(t));
	}
		
	public static LPDatenSubreport getSubreportWEPDokumente(String belegartCNr, Integer belegartpositionIId,
			String sBelegartDokumentenablage, String sGruppierung, int pdfResolution, boolean bAlleVersionen,
			TheClientDto theClientDto) throws Throwable {
		LieferscheinpositionFac lieferscheinpositionFac = FacLookup.lookup(new InitialContext(),
				LieferscheinpositionFacBean.class, LieferscheinpositionFac.class);

		TreeMap<String, ArrayList<JCRDocDto>> alDokumenteNachArtikelGruppiert = lieferscheinpositionFac.getWEPDokumente(
				belegartCNr, belegartpositionIId, sBelegartDokumentenablage, sGruppierung, bAlleVersionen,
				theClientDto);

		String[] fieldnames = new String[] { "Name", "Schlagworte", "Belegart", "Gruppierung", "Artikelnummer",
				"Image" };

		SystemBean systemBean = new SystemBean();
		ArrayList<Object[]> alDaten = new ArrayList<Object[]>();
		for (String artikelnummer : alDokumenteNachArtikelGruppiert.keySet()) {

			ArrayList<JCRDocDto> alDokumenteEinesArtikels = alDokumenteNachArtikelGruppiert.get(artikelnummer);

//			System.out.println("--> Artikel: " + artikelnummer + ".");

			Iterator<JCRDocDto> entries = alDokumenteEinesArtikels.stream()
				.filter(distinctByKey(JCRDocDto::getsPath))
				.filter(x -> x.getbData() != null && x.getsMIME() != null)
				.iterator();
			while (entries.hasNext()) {
				JCRDocDto dto = entries.next();
				Object[] oZeile = new Object[fieldnames.length];

//				System.out.println("---> Dokument: " + dto.getsPath() + ".");

				oZeile[0] = dto.getsName();
				oZeile[1] = dto.getsSchlagworte();
				oZeile[2] = dto.getsBelegart();
				oZeile[3] = dto.getsGruppierung();
				oZeile[4] = artikelnummer;

				String sMime = dto.getsMIME().toUpperCase();
				if (".JPG".equals(sMime) || ".JPEG".equals(sMime)
						|| ".GIF".equals(sMime) || ".PNG".equals(sMime)
						|| ".TIFF".equals(sMime) || ".BMP".equals(sMime)) {
					oZeile[5] = Helper.byteArrayToImage(dto.getbData());
					alDaten.add(oZeile);
				} else if (".PDF".equals(sMime)) {
					byte[][] bilder = systemBean.get().konvertierePDFFileInEinzelneBilder(dto.getbData(),
							pdfResolution);

					if (bilder != null) {
						for (int i = 0; i < bilder.length; i++) {

							Object[] o = oZeile.clone();
							o[5] = Helper.byteArrayToImage(bilder[i]);
							alDaten.add(o);
						}
					}
				}
			}
		}

		return new LPDatenSubreport(alDaten, fieldnames);
	}

	public static LPDatenSubreport getSubreportAnzahlungenZuAuftrag(String auftragCNr, TheClientDto theClientDto)
			throws Throwable {
		RechnungFac rechnungFac = FacLookup.lookup(new InitialContext(), RechnungFacBean.class, RechnungFac.class);

		AuftragFac auftragFac = FacLookup.lookup(new InitialContext(), AuftragFacBean.class, AuftragFac.class);

		AuftragDto auftragDto = auftragFac.auftragFindByMandantCNrCNrOhneExc(theClientDto.getMandant(), auftragCNr);

		if (auftragDto != null) {
			String[] fieldnames = new String[] { "Rechnung", "Belegdatum", "Status", "RechnungswertNetto", "MwstBetrag",
					"BezahltBetrag", "LetztesBezahldatum" };

			ArrayList alSubData = new ArrayList();
			RechnungDto[] alleREsDesAuftrags = rechnungFac.rechnungFindByAuftragIId(auftragDto.getIId());
			for (int i = 0; i < alleREsDesAuftrags.length; i++) {
				if (!alleREsDesAuftrags[i].getStatusCNr().equals(RechnungFac.STATUS_STORNIERT)) {

					if (alleREsDesAuftrags[i].getRechnungartCNr().equals(RechnungFac.RECHNUNGART_ANZAHLUNG)) {

						RechnungDto rDto = alleREsDesAuftrags[i];

						Object[] oZeileSub = new Object[fieldnames.length];

						oZeileSub[0] = rDto.getCNr();
						oZeileSub[1] = rDto.getTBelegdatum();
						oZeileSub[2] = rDto.getStatusCNr();
						oZeileSub[3] = rDto.getNWert();
						oZeileSub[4] = rDto.getNWertust();
						oZeileSub[5] = rechnungFac.getBereitsBezahltWertVonRechnung(rDto.getIId(), null);
						oZeileSub[6] = rechnungFac.getDatumLetzterZahlungseingang(rDto.getIId());

						alSubData.add(oZeileSub);
					}
				}
			}
			Object[][] oSubData = new Object[alSubData.size()][fieldnames.length];
			oSubData = (Object[][]) alSubData.toArray(oSubData);

			return new LPDatenSubreport(oSubData, fieldnames);

		} else {
			return null;
		}
	}

	public static BigDecimal getSummeBezahltAnzahlungenZuAuftrag(String auftragCNr, TheClientDto theClientDto)
			throws Throwable {
		RechnungFac rechnungFac = FacLookup.lookup(new InitialContext(), RechnungFacBean.class, RechnungFac.class);

		AuftragFac auftragFac = FacLookup.lookup(new InitialContext(), AuftragFacBean.class, AuftragFac.class);

		AuftragDto auftragDto = auftragFac.auftragFindByMandantCNrCNrOhneExc(theClientDto.getMandant(), auftragCNr);

		BigDecimal bdSummeBezahlt = BigDecimal.ZERO;
		if (auftragDto != null) {

			RechnungDto[] alleREsDesAuftrags = rechnungFac.rechnungFindByAuftragIId(auftragDto.getIId());
			for (int i = 0; i < alleREsDesAuftrags.length; i++) {
				if (!alleREsDesAuftrags[i].getStatusCNr().equals(RechnungFac.STATUS_STORNIERT)) {

					if (alleREsDesAuftrags[i].getRechnungartCNr().equals(RechnungFac.RECHNUNGART_ANZAHLUNG)) {
						RechnungDto rDto = alleREsDesAuftrags[i];

						bdSummeBezahlt = bdSummeBezahlt
								.add(rechnungFac.getBereitsBezahltWertVonRechnung(rDto.getIId(), null));
					}
				}
			}
		}

		return bdSummeBezahlt;
	}

	public static LPDatenSubreport getSubreportGestarteteMaschinen(Integer personalIId, TheClientDto theClientDto)
			throws Throwable {
		ZeiterfassungFac zeiterfassungFac = FacLookup.lookup(new InitialContext(), ZeiterfassungFacBean.class,
				ZeiterfassungFac.class);
		return zeiterfassungFac.getSubreportGestarteteMaschinen(personalIId, theClientDto);
	}

	public final static int DATUMSBEREICH_TYP_TAG = 0;
	public final static int DATUMSBEREICH_TYP_WOCHE = 1;
	public final static int DATUMSBEREICH_TYP_MONAT = 2;
	public final static int DATUMSBEREICH_TYP_QUARTAL = 3;
	public final static int DATUMSBEREICH_TYP_JAHR = 4;

	public static LPDatenSubreport getSubreportDatumsbereiche(java.util.Date dVon, java.util.Date dBis, int iTyp) {

		if (dVon != null && dBis != null) {

			ArrayList<java.util.Date> alDatums = new ArrayList<java.util.Date>();

			Calendar cBeginn = Calendar.getInstance();
			cBeginn.setTime(dVon);

			switch (iTyp) {

			case DATUMSBEREICH_TYP_WOCHE: {
				if (cBeginn.get(Calendar.DAY_OF_WEEK) != cBeginn.getFirstDayOfWeek()) {
					cBeginn.set(Calendar.DAY_OF_WEEK, cBeginn.getFirstDayOfWeek());
					if (cBeginn.getTime().before(dVon)) {
						cBeginn.add(Calendar.DATE, 7);
					}
				}
			}
				break;
			case DATUMSBEREICH_TYP_MONAT: {
				if (cBeginn.get(Calendar.DATE) != 1) {
					cBeginn.set(Calendar.DATE, 1);
				}

				if (cBeginn.getTime().before(dVon)) {
					cBeginn.add(Calendar.MONTH, 1);
				}

			}
				break;
			case DATUMSBEREICH_TYP_QUARTAL: {
				cBeginn.set(Calendar.DATE, 1);

				if (cBeginn.getTime().before(dVon)) {
					cBeginn.add(Calendar.MONTH, 1);
				}

				if (cBeginn.get(Calendar.MONTH) < Calendar.APRIL) {
					cBeginn.set(Calendar.MONTH, Calendar.JANUARY);
				} else if (cBeginn.get(Calendar.MONTH) < Calendar.JULY) {
					cBeginn.set(Calendar.MONTH, Calendar.APRIL);
				} else if (cBeginn.get(Calendar.MONTH) < Calendar.OCTOBER) {
					cBeginn.set(Calendar.MONTH, Calendar.JULY);
				} else if (cBeginn.get(Calendar.MONTH) <= Calendar.DECEMBER) {
					cBeginn.set(Calendar.MONTH, Calendar.OCTOBER);
				}

			}
				break;
			case DATUMSBEREICH_TYP_JAHR: {
				cBeginn.set(Calendar.DATE, 1);

				if (cBeginn.get(Calendar.MONTH) != Calendar.JANUARY) {
					cBeginn.set(Calendar.MONTH, Calendar.JANUARY);
				}

				if (cBeginn.getTime().before(dVon)) {
					cBeginn.add(Calendar.YEAR, 1);
				}

			}
				break;
			}

			while (cBeginn.getTime().getTime() <= dBis.getTime()) {

				alDatums.add(cBeginn.getTime());

				switch (iTyp) {
				case DATUMSBEREICH_TYP_TAG: {
					cBeginn.add(Calendar.DATE, 1);
				}
					break;
				case DATUMSBEREICH_TYP_WOCHE: {
					cBeginn.add(Calendar.WEEK_OF_YEAR, 1);
				}
					break;
				case DATUMSBEREICH_TYP_MONAT: {
					cBeginn.add(Calendar.MONTH, 1);

				}
					break;
				case DATUMSBEREICH_TYP_QUARTAL: {
					cBeginn.add(Calendar.MONTH, 3);
				}
					break;
				case DATUMSBEREICH_TYP_JAHR: {
					cBeginn.add(Calendar.YEAR, 1);
				}
					break;
				default: {
					dVon = Helper.addiereTageZuDatum(dBis, 1);
				}
				}

			}

			String[] fieldnames = new String[] { "Datum", };
			Object[][] dataSubKD = new Object[alDatums.size()][1];
			for (int i = 0; i < alDatums.size(); i++) {
				dataSubKD[i][0] = alDatums.get(i);
			}
			return new LPDatenSubreport(dataSubKD, fieldnames);
		} else {
			return null;
		}

	}

	public static java.sql.Timestamp getGeschaeftsjahresbeginnZuDatum(java.util.Date tDatum, TheClientDto theClientDto)
			throws Throwable {
		BuchenFac buchenFac = FacLookup.lookup(new InitialContext(), BuchenFacBean.class, BuchenFac.class);
		Integer iGFJahr = buchenFac.findGeschaeftsjahrFuerDatum(new java.sql.Date(tDatum.getTime()),
				theClientDto.getMandant());
		java.sql.Timestamp[] tVonBis = buchenFac.getDatumVonBisGeschaeftsjahr(iGFJahr, theClientDto);
		if (tVonBis != null && tVonBis.length > 0) {
			return tVonBis[0];
		} else {
			return null;
		}

	}

	/**
	 * Ermittelt den angegebenen Subreportnamen und liefert die Daten zur&uuml;ck
	 * 
	 * @param sString der Name des Subreports
	 * @return Subreport
	 */
	public static LPDatenSubreport getSubreportAusStringMitKommaGetrennt(String sString) {
		String[] teile = sString.split(",");
		String[] fieldnames = new String[] { "Feld", };
		Object[][] dataSubKD = new Object[teile.length][1];
		for (int i = 0; i < teile.length; i++) {
			dataSubKD[i][0] = teile[i].trim();
		}
		return new LPDatenSubreport(dataSubKD, fieldnames);
	}

	public static LPDatenSubreport getSubreportEnthaltenesLosIstMaterial(String artikelnummer, String chargennummer,
			TheClientDto theClientDto) throws Throwable {
		LagerFac lagerFac = FacLookup.lookup(new InitialContext(), LagerFacBean.class, LagerFac.class);
		return lagerFac.getSubreportEnthaltenesLosIstMaterial(artikelnummer, chargennummer, theClientDto);
	}

	public static LPDatenSubreport getSubreportGeraeteseriennummernEinerLagerbewegung(String belegartCNr,
			Integer belegartpositionIId, String snr, TheClientDto theClientDto) throws Throwable {
		LagerFac lagerFac = FacLookup.lookup(new InitialContext(), LagerFacBean.class, LagerFac.class);
		return lagerFac.getSubreportGeraeteseriennummernEinerLagerbewegung(belegartCNr, belegartpositionIId, snr,
				theClientDto);
	}

	public static LPDatenSubreport getSubreportSnrChnrEinerBelegposition(String belegartCNr,
			Integer belegartpositionIId, TheClientDto theClientDto) throws Throwable {
		LagerFac lagerFac = FacLookup.lookup(new InitialContext(), LagerFacBean.class, LagerFac.class);
		return lagerFac.getSubreportSnrChnrEinerBelegposition(belegartCNr, belegartpositionIId, theClientDto);
	}

	// PJ20899
	public static String getArtikeleigenschaft(String key, String cDruckname, TheClientDto theClientDto)
			throws Throwable {

		if (key != null) {
			String keyFuerPaneldaten = null;
			// key=Artikelnummer

			ArtikelFac artikelFac = FacLookup.lookup(new InitialContext(), ArtikelFacBean.class, ArtikelFac.class);
			ArtikelDto aDto = artikelFac.artikelFindByCNrOhneExc(key, theClientDto);
			if (aDto != null) {
				keyFuerPaneldaten = aDto.getIId() + "";
			}

			if (keyFuerPaneldaten != null) {

				PanelFac panelFac = FacLookup.lookup(new InitialContext(), PanelFacBean.class, PanelFac.class);
				PanelbeschreibungDto[] pbsDtos = panelFac.panelbeschreibungFindByPanelCNrMandantCNr(
						PanelFac.PANEL_ARTIKELEIGENSCHAFTEN, theClientDto.getMandant(), aDto.getArtgruIId());

				for (int i = 0; i < pbsDtos.length; i++) {
					PanelbeschreibungDto pbsDto = pbsDtos[i];
					if (pbsDto.getCDruckname() != null && pbsDto.getCDruckname().equals(cDruckname)) {

						PaneldatenDto pdDto = panelFac.paneldatenFindByPanelCNrPanelbeschreibungIIdCKeyOhneExc(
								PanelFac.PANEL_ARTIKELEIGENSCHAFTEN, pbsDto.getIId(), keyFuerPaneldaten);
						if (pdDto != null) {
							return pdDto.getXInhalt();
						}
					}

				}

			}
		}

		return null;

	}

	// PJ21174
	public static String getArtikeltechnikeigenschaft(String key, String cDruckname, TheClientDto theClientDto)
			throws Throwable {

		if (key != null) {
			String keyFuerPaneldaten = null;
			// key=Artikelnummer

			ArtikelFac artikelFac = FacLookup.lookup(new InitialContext(), ArtikelFacBean.class, ArtikelFac.class);
			ArtikelDto aDto = artikelFac.artikelFindByCNrOhneExc(key, theClientDto);
			if (aDto != null) {
				keyFuerPaneldaten = aDto.getIId() + "";
			}

			if (keyFuerPaneldaten != null) {

				PanelFac panelFac = FacLookup.lookup(new InitialContext(), PanelFacBean.class, PanelFac.class);
				PanelbeschreibungDto[] pbsDtos = panelFac.panelbeschreibungFindByPanelCNrMandantCNr(
						PanelFac.PANEL_ARTIKELTECHNIK, theClientDto.getMandant(), aDto.getArtgruIId());

				for (int i = 0; i < pbsDtos.length; i++) {
					PanelbeschreibungDto pbsDto = pbsDtos[i];
					if (pbsDto.getCDruckname() != null && pbsDto.getCDruckname().equals(cDruckname)) {

						PaneldatenDto pdDto = panelFac.paneldatenFindByPanelCNrPanelbeschreibungIIdCKeyOhneExc(
								PanelFac.PANEL_ARTIKELTECHNIK, pbsDto.getIId(), keyFuerPaneldaten);
						if (pdDto != null) {
							return pdDto.getXInhalt();
						}
					}

				}

			}
		}

		return null;

	}

	public static BigDecimal getLief1Preis(String artikelnummer, BigDecimal fMenge, String waehrungCNr,
			TheClientDto theClientDto) throws Throwable {

		ArtikelFac artikelFac = FacLookup.lookup(new InitialContext(), ArtikelFacBean.class, ArtikelFac.class);

		ArtikelDto aDto = artikelFac.artikelFindByCNrMandantCNrOhneExc(artikelnummer, theClientDto.getMandant());

		if (aDto != null) {

			ArtikellieferantDto alDto = artikelFac.getArtikelEinkaufspreisDesBevorzugtenLieferanten(aDto.getIId(),
					fMenge, waehrungCNr, theClientDto);

			if (alDto != null) {
				return alDto.getNNettopreis();
			} else {
				return null;
			}
		} else {
			return null;
		}

	}

	public static LPDatenSubreport getSubreportAllergene(String artikelnummer, TheClientDto theClientDto)
			throws Throwable {
		ArtikelFac artikelFac = FacLookup.lookup(new InitialContext(), ArtikelFacBean.class, ArtikelFac.class);

		ArtikelDto aDto = artikelFac.artikelFindByCNrMandantCNrOhneExc(artikelnummer, theClientDto.getMandant());

		if (aDto != null) {
			ArtikelReportFac artikelReportFac = FacLookup.lookup(new InitialContext(), ArtikelReportFacBean.class,
					ArtikelReportFac.class);

			return artikelReportFac.getSubreportAllergene(aDto.getIId(), theClientDto);
		} else {
			return null;
		}

	}

	public static boolean hatRecht(String rechtCNr, TheClientDto theClientDto) throws Throwable {
		BenutzerServicesFac benutzerServicesFac = FacLookup.lookup(new InitialContext(), BenutzerServicesFacBean.class,
				BenutzerServicesFac.class);
		return benutzerServicesFac.hatRecht(rechtCNr, theClientDto);
	}

	public static String getUnterstuecklisten_FOR_IN(String artikelnummerSTKL, boolean bMitFremdgefertigtenStuecklisten,
			TheClientDto theClientDto) throws Throwable {

		ArtikelFac artikelFac = FacLookup.lookup(new InitialContext(), ArtikelFacBean.class, ArtikelFac.class);

		ArtikelDto artikelDto = artikelFac.artikelFindByCNrOhneExc(artikelnummerSTKL, theClientDto);

		String in = null;

		if (artikelDto != null) {

			StuecklisteFac stuecklisteFac = FacLookup.lookup(new InitialContext(), StuecklisteFacBean.class,
					StuecklisteFac.class);

			StuecklisteDto stklDto = stuecklisteFac.stuecklisteFindByArtikelIIdMandantCNrOhneExc(artikelDto.getIId(),
					theClientDto.getMandant());

			if (stklDto != null) {

				HashSet<Integer> unterstuecklisten = new HashSet<Integer>();

				List<?> m = stuecklisteFac.getStrukturDatenEinerStueckliste(stklDto.getIId(), theClientDto,
						StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_ARTIKELNR, 0, null, true, true,
						BigDecimal.ONE, null, true, bMitFremdgefertigtenStuecklisten);

				Iterator<?> it = m.listIterator();

				while (it.hasNext()) {
					StuecklisteMitStrukturDto struktur = (StuecklisteMitStrukturDto) it.next();

					if (struktur.getStuecklisteDto() != null) {

						unterstuecklisten.add(struktur.getStuecklisteDto().getArtikelIId());

					}

				}

				Iterator itUnterstkls = unterstuecklisten.iterator();

				if (itUnterstkls.hasNext()) {

					in = "";

					while (itUnterstkls.hasNext()) {

						Integer artikelIId = (Integer) itUnterstkls.next();

						ArtikelDto aDtoUnterstkl = artikelFac.artikelFindByPrimaryKeySmall(artikelIId, theClientDto);

						in += "'" + aDtoUnterstkl.getCNr() + "'";

						if (itUnterstkls.hasNext()) {
							in += ",";
						}
					}
				}
			}
		}
		return in;
	}

	public static int ermittleTageEinesZeitraumes(java.util.Date dVon, java.util.Date dBis) {
		int iTage = 0;

		if (dVon != null && dBis != null) {
			dVon = Helper.cutDate(dVon);

			dBis = Helper.cutDate(dBis);

			if (dVon.after(dBis)) {
				java.util.Date tH = dVon;
				dVon = dBis;
				dBis = tH;
			}

			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(dVon.getTime());
			while (dBis.after(dVon)) {
				iTage++;
				c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1);
				dVon.setTime(c.getTimeInMillis());
			}
		}

		return iTage;
	}

	public static LPDatenSubreport getSubreportPDFAusArtikelkommentar(String kommentarartCNr, String artikelCNr,
			String mandantCNr, Locale locale, int resolution) throws Throwable {
		return getSubreportPDFAusArtikelkommentar(kommentarartCNr, artikelCNr, mandantCNr, locale, false, resolution);
	}

	public static LPDatenSubreport getSubreportPDFAusArtikelkommentar(String kommentarartCNr, String artikelCNr,
			String mandantCNr, Locale locale, boolean bImmerHochformat, int resolution) throws Throwable {

		ArtikelkommentarFac artikelkommentarFac = FacLookup.lookup(new InitialContext(), ArtikelkommentarFacBean.class,
				ArtikelkommentarFac.class);

		byte[] pdf = artikelkommentarFac.getPDFArtikelkommentar(artikelCNr, kommentarartCNr,
				Helper.locale2String(locale), mandantCNr);
		if (pdf != null) {
			SystemFac systemFac = FacLookup.lookup(new InitialContext(), SystemFacBean.class, SystemFac.class);
			byte[][] bilder = systemFac.konvertierePDFFileInEinzelneBilder(pdf, resolution);

			if (bilder != null) {

				String[] fieldnames = new String[] { "Bild", };
				Object[][] dataSubKD = new Object[bilder.length][1];
				for (int i = 0; i < bilder.length; i++) {

					BufferedImage bi = Helper.byteArrayToImage(bilder[i]);

					// PJ21517 Bilder sind immer Hochformat
					int width = bi.getWidth();
					int height = bi.getHeight();

					if (bImmerHochformat && width > height) {

						BufferedImage returnImage = new BufferedImage(height, width, BufferedImage.TYPE_INT_RGB);

						for (int x = 0; x < width; x++) {
							for (int y = 0; y < height; y++) {
								returnImage.setRGB(y, width - x - 1, bi.getRGB(x, y));
							}
						}
						bi = returnImage;
					}

					dataSubKD[i][0] = bi;
				}
				return new LPDatenSubreport(dataSubKD, fieldnames);
			} else {
				return null;
			}
		} else {
			return null;
		}

	}

	public static BufferedImage getArtikelkommentarBild(String kommentarartCNr, String artikelCNr, String mandantCNr,
			Locale locale) throws Throwable {

//		ArtikelkommentarFac artikelkommentarFac = (ArtikelkommentarFac) new InitialContext()
//				.lookup("lpserver/ArtikelkommentarFacBean/remote");
		ArtikelkommentarFac artikelkommentarFac = FacLookup.lookup(new InitialContext(), ArtikelkommentarFacBean.class,
				ArtikelkommentarFac.class);

		byte[] bild = artikelkommentarFac.getArtikelkommentarBild(artikelCNr, kommentarartCNr,
				Helper.locale2String(locale), mandantCNr);
		if (bild != null) {
			return Helper.byteArrayToImage(bild);
		} else {
			return null;
		}

	}

	public static LPDatenSubreport getSubreportAusPDFFile(String pdfFile, int resolution) throws Throwable {

		SystemFac systemFac = FacLookup.lookup(new InitialContext(), SystemFacBean.class, SystemFac.class);

		byte[][] bilder = systemFac.konvertierePDFFileInEinzelneBilder(pdfFile, resolution);

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

	public static String getMonatVonJahrUndWoche(Integer iJahr, Integer iWoche, Locale locale) {

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

	public static BigDecimal rundeKaufmaennisch(BigDecimal bigDecimal, int stellen) {
		return Helper.rundeKaufmaennisch(bigDecimal, stellen);
	}

	public static Boolean pruefeEndsumme(BigDecimal bdReportValue, BigDecimal bdHvValue, Double dAbweichung) {
		bdReportValue = bdReportValue.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		/*
		 * if(bdReportValue.doubleValue() != bdHvValue.doubleValue()){ BigDecimal abw =
		 * bdReportValue.subtract(bdHvValue); if(dAbweichung == null) dAbweichung = new
		 * Double(0.10); if(abw.abs().doubleValue() > dAbweichung){ EJBExceptionLP ex =
		 * new EJBExceptionLP(EJBExceptionLP.FEHLER_RECHNUNG_HVWERT_UNGLEICH_REPORTWERT
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
			s = c.get(Calendar.WEEK_OF_YEAR) + "/" + Helper.berechneJahrDerKW(c);
			;
		}
		return s;
	}

	public static boolean emailSenden(String empfaenger, String betreff, String nachricht, TheClientDto theClientDto)
			throws Throwable {

		PersonalFac personalFac = FacLookup.lookup(new InitialContext(), PersonalFacBean.class, PersonalFac.class);

		VersandFac versandFac = FacLookup.lookup(new InitialContext(), VersandFacBean.class, VersandFac.class);

		VersandauftragDto versandauftragDto = new VersandauftragDto();
		versandauftragDto.setOInhalt(null);
		versandauftragDto.setBEmpfangsbestaetigung(new Short("0"));
		versandauftragDto.setCText(nachricht);
		PersonalDto persDto = personalFac.personalFindByPrimaryKey(theClientDto.getIDPersonal(), theClientDto);
		String sAbsender = persDto.getCEmail();

		if (sAbsender == null) {
			return false;
		}
		versandauftragDto.setCAbsenderadresse(sAbsender);
		// Empfaenger = Absender da der ZUstaendige User selbst benachrichtigt
		// wird
		versandauftragDto.setCEmpfaenger(empfaenger);

		versandauftragDto.setCBetreff(betreff);
		versandFac.createVersandauftrag(versandauftragDto, false, theClientDto);
		return true;
	}

	public static String entferneStyleInformation(String sI) {
		return Helper.strippHTML(sI);
	}

	public static Boolean pruefeEndsumme(BigDecimal bdReportNettoValue, BigDecimal bdHvValue, Double dAbweichung,
			String listeMwstsaetze, Locale reportLocale) {
		bdReportNettoValue = bdReportNettoValue.setScale(2, BigDecimal.ROUND_HALF_EVEN);

		BigDecimal bdMwstsatz = getMwstSummeAusListeMwstsaetze(listeMwstsaetze, reportLocale);

		bdReportNettoValue = bdReportNettoValue.add(bdMwstsatz);

		if (bdReportNettoValue.doubleValue() != bdHvValue.doubleValue()) {
			BigDecimal abw = bdReportNettoValue.subtract(bdHvValue);
			if (dAbweichung == null)
				dAbweichung = new Double(0.10);
			if (abw.abs().doubleValue() > dAbweichung) {
				EJBExceptionLP ex = new EJBExceptionLP(EJBExceptionLP.FEHLER_RECHNUNG_HVWERT_UNGLEICH_REPORTWERT, "");
				ArrayList<Object> al = new ArrayList<Object>();
				al.add(bdReportNettoValue.doubleValue() + " != " + bdHvValue.doubleValue());
				ex.setAlInfoForTheClient(al);
				throw ex;
			}
		}

		return new Boolean(true);
	}

	public static BigDecimal getMwstSummeAusListeMwstsaetze(String listeMwstsaetze, Locale reportLocale) {
		BigDecimal bdMwstsatz = new BigDecimal(0.00);

		if (listeMwstsaetze != null && listeMwstsaetze.length() > 0) {

			String[] mwstsaetze = listeMwstsaetze.split("\n");
			for (int i = 0; i < mwstsaetze.length; i++) {
				java.text.DecimalFormat decfrm = (java.text.DecimalFormat) NumberFormat.getInstance(reportLocale);
				decfrm.setParseBigDecimal(true);
				ParsePosition pos = new ParsePosition(0);
				BigDecimal bd = (BigDecimal) decfrm.parse(mwstsaetze[i], pos);
				bdMwstsatz = bdMwstsatz.add(bd);
			}

		}
		return bdMwstsatz;
	}

	public Context context;

	public static BigDecimal getDauerEinerTaetigkeitEinesTages(String personalnummer, String artikelnummer,
			java.util.Date dDatum, TheClientDto theClientDto) {

		try {
			ZeiterfassungFac zeiterfassungFac = FacLookup.lookup(new InitialContext(), ZeiterfassungFacBean.class,
					ZeiterfassungFac.class);
			return zeiterfassungFac.getDauerEinerTaetigkeitEinesTages(personalnummer, artikelnummer, dDatum,
					theClientDto);

		} catch (Throwable t) {
			return BigDecimal.ZERO;

		}

	}

	public static BigDecimal berechneOffenenWertEinesAuftrags(String auftragCNr, TheClientDto theClientDto) {
		try {
			AuftragFac auftragFac = FacLookup.lookup(new InitialContext(), AuftragFacBean.class, AuftragFac.class);
			return auftragFac.berechneOffenenWertEinesAuftrags(auftragCNr, theClientDto);
		} catch (Throwable t) {
			return new BigDecimal(-1);

		}
	}

	public static BigDecimal berechneOffenenWertEinerBestellung(String bestellungCNr, TheClientDto theClientDto) {
		try {
			BestellungFac bestellungFac = FacLookup.lookup(new InitialContext(), BestellungFacBean.class,
					BestellungFac.class);
			return bestellungFac.berechneOffenenWertEinerBestellung(bestellungCNr, theClientDto);
		} catch (Throwable t) {
			return new BigDecimal(-1);

		}
	}

	public static Double getSummeIstZeitEinesBeleges(String belegartCNr, String belegnummer,
			TheClientDto theClientDto) {

		try {
			ZeiterfassungFac zeiterfassungFac = FacLookup.lookup(new InitialContext(), ZeiterfassungFacBean.class,
					ZeiterfassungFac.class);

			Double summe = new Double(-1);

			// Suche die belegId

			Integer belegIId = null;

			belegartCNr = belegartCNr.trim();

			if (belegartCNr.equals(LocaleFac.BELEGART_ANGEBOT.trim())) {
				AngebotFac angebotFac = FacLookup.lookup(new InitialContext(), AngebotFacBean.class, AngebotFac.class);

				if (belegnummer != null && belegnummer.startsWith("AG")) {
					belegnummer = belegnummer.substring(2);
				}

				AngebotDto agDto = angebotFac.angebotFindByCNrMandantCNrOhneEx(belegnummer, theClientDto.getMandant());
				if (agDto != null) {
					belegIId = agDto.getIId();
				}
			} else if (belegartCNr.equals(LocaleFac.BELEGART_AUFTRAG.trim())) {
				AuftragFac auftragFac = FacLookup.lookup(new InitialContext(), AuftragFacBean.class, AuftragFac.class);
				AuftragDto abDto = auftragFac.auftragFindByMandantCNrCNrOhneExc(theClientDto.getMandant(), belegnummer);
				if (abDto != null) {
					belegIId = abDto.getIId();
				}
			} else if (belegartCNr.equals(LocaleFac.BELEGART_LOS.trim())) {
				FertigungFac fertigungFac = FacLookup.lookup(new InitialContext(), FertigungFacBean.class,
						FertigungFac.class);
				if (belegnummer != null && belegnummer.startsWith("LO")) {
					belegnummer = belegnummer.substring(2);
				}
				LosDto losDto = fertigungFac.losFindByCNrMandantCNrOhneExc(belegnummer, theClientDto.getMandant());
				if (losDto != null) {
					belegIId = losDto.getIId();
				}
			} else if (belegartCNr.equals(LocaleFac.BELEGART_PROJEKT.trim())) {
				ProjektFac projektFac = FacLookup.lookup(new InitialContext(), ProjektFacBean.class, ProjektFac.class);
				if (belegnummer != null && belegnummer.startsWith("PJ")) {
					belegnummer = belegnummer.substring(2);
				}
				ProjektDto pjDto = projektFac.projektFindByMandantCNrCNrOhneExc(theClientDto.getMandant(), belegnummer);
				if (pjDto != null) {
					belegIId = pjDto.getIId();
				}
			}

			if (belegIId != null) {
				summe = zeiterfassungFac.getSummeZeitenEinesBeleges(belegartCNr, belegIId, null, null, null, null,
						theClientDto);
			}
			return summe;

		} catch (Throwable t) {
			return new Double(-1);

		}
	}

	public static UrlaubsabrechnungDto berechneUrlaubsAnspruch(String personalnummer,
			java.util.Date dAbrechnungzeitpunkt, TheClientDto theClientDto) {

		try {
			ZeiterfassungFac zeiterfassungFac = FacLookup.lookup(new InitialContext(), ZeiterfassungFacBean.class,
					ZeiterfassungFac.class);
			PersonalFac personalFac = FacLookup.lookup(new InitialContext(), PersonalFacBean.class, PersonalFac.class);
			PersonalDto personalDto = personalFac.personalFindByCPersonalnrMandantCNrOhneExc(personalnummer,
					theClientDto.getMandant());
			if (personalDto != null) {
				return zeiterfassungFac.berechneUrlaubsAnspruch(personalDto.getIId(),
						new java.sql.Date(dAbrechnungzeitpunkt.getTime()), theClientDto);
			} else {
				return null;
			}

		} catch (Throwable t) {
			return new UrlaubsabrechnungDto();

		}
	}

	public static BigDecimal getEKWertUeberLoseAusKundenlaegern(Integer lieferscheinpositionIId, boolean bMaxSollmenge,
			TheClientDto theClientDto) {

		try {
			LieferscheinFac lieferscheinFac = FacLookup.lookup(new InitialContext(), LieferscheinFacBean.class,
					LieferscheinFac.class);
			return lieferscheinFac.getEKWertUeberLoseAusKundenlaegern(lieferscheinpositionIId, bMaxSollmenge,
					theClientDto);

		} catch (Throwable t) {
			return null;

		}
	}

	public static BigDecimal getEKWertUeberLoseAusKundenlaegern(Integer lieferscheinpositionIId,
			TheClientDto theClientDto) {

		try {
			return getEKWertUeberLoseAusKundenlaegern(lieferscheinpositionIId, false, theClientDto);

		} catch (Throwable t) {
			return null;

		}
	}

	public static java.util.Date getLetztenTagDesMonats(java.util.Date datum, int versatzMonate) {

		if (datum != null) {
			Calendar c = Calendar.getInstance();

			c.setTimeInMillis(datum.getTime());

			c.add(Calendar.MONTH, versatzMonate);

			c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));

			return c.getTime();

		} else {
			return null;
		}
	}

	public static java.sql.Date addiereTageZuDatum(java.util.Date datum, int tage) {
		return Helper.addiereTageZuDatum(datum, tage);
	}

	public static String getMediastandardTextHtml(String cNr, String mandantCNr, Locale locale) {
		if (cNr != null && mandantCNr != null && locale != null) {

			try {
				MediaFac mediaFac = FacLookup.lookup(new InitialContext(), MediaFacBean.class, MediaFac.class);
				return mediaFac.mediastandardTextHtmlFindByCNrMandantCNrLocale(cNr, mandantCNr, locale);

			} catch (Throwable t) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
			}

		} else {
			return null;
		}
	}

	public static String laenderartZweierLaender(String lkzKunde, String lkzBasis, String uidNummer,
			java.sql.Timestamp tsEUMitglied) {
		if (lkzKunde != null && lkzBasis != null) {
			if (lkzKunde.equals(lkzBasis)) {
				return FinanzFac.LAENDERART_INLAND;
			} else {
				try {
					SystemFac systemFac = FacLookup.lookup(new InitialContext(), SystemFacBean.class, SystemFac.class);
					LandDto landDto = systemFac.landFindByLkz(lkzKunde);

					if (systemFac.isEUMitglied(landDto, tsEUMitglied)) {
						if (uidNummer != null) {
							return FinanzFac.LAENDERART_EU_AUSLAND_MIT_UID;
						} else {
							return FinanzFac.LAENDERART_EU_AUSLAND_OHNE_UID;
						}
					} else {
						return FinanzFac.LAENDERART_DRITTLAND;
					}
				} catch (Throwable t) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
				}
			}
		} else {
			return FinanzFac.LAENDERART_INLAND;
		}
	}

	public static BigDecimal getVerfuegbarkeitEinerMaschineInStunden(String cIdentifikationsnummer, int iJahr, int iKW,
			TheClientDto theClientDto) {

		BigDecimal bdVerfuegbarkeit = BigDecimal.ZERO;

		try {
			MaschineFac maschineFac = FacLookup.lookup(new InitialContext(), MaschineFacBean.class, MaschineFac.class);
			ZeiterfassungFac zeiterfassungFac = FacLookup.lookup(new InitialContext(), ZeiterfassungFacBean.class,
					ZeiterfassungFac.class);

			Calendar c = Calendar.getInstance();
			c.set(Calendar.YEAR, iJahr);
			c.set(Calendar.WEEK_OF_YEAR, iKW);

			java.sql.Timestamp[] tVonBis = Helper
					.getTimestampVonBisEinerKW(new java.sql.Timestamp(c.getTimeInMillis()));

			java.sql.Date dTemp = new java.sql.Date(tVonBis[0].getTime());
			for (int i = 0; i < 7; i++) {

				bdVerfuegbarkeit = bdVerfuegbarkeit.add(maschineFac.getVerfuegbarkeitInStundenZuDatum(
						zeiterfassungFac.maschineFindByCIdentifikationsnr(cIdentifikationsnummer).getIId(), dTemp,
						theClientDto));

				dTemp = new java.sql.Date(dTemp.getTime() + 24 * 3600000);

			}

		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return bdVerfuegbarkeit;
	}

	public static BigDecimal getLiquiditaetsKontostand(Integer geschaeftsjahrIId, TheClientDto theClientDto) {
		try {
			FinanzServiceFac finanzServiceFac = FacLookup.lookup(new InitialContext(), FinanzServiceFacBean.class,
					FinanzServiceFac.class);
			return finanzServiceFac.getLiquiditaetsKontostand(geschaeftsjahrIId, theClientDto);
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
			uebertrag = tabelle[(uebertrag + Integer.parseInt(nummer.substring(i, i + 1))) % 10];

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
					EJBExceptionLP ex = new EJBExceptionLP(EJBExceptionLP.FEHLER_SORTIER_LISTE_ENTHAELT_NULL,
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

	/*
	 * Anmerkung: die Augangsliste kann auch Timestamps enthalten die innerhalb
	 * einer Minute aufeinanderfolgen und unterschiedliche Werte haben. Daher muss
	 * jedes Datum minutenweise verschoben werden bis es groesser ist als der
	 * Vorgaenger
	 */
	@SuppressWarnings("unchecked")
	public static List sortListNoNullSprung(List list, int indexDate, int indexValue) {
		List listnew = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			Object[] o = (Object[]) list.get(i);
			if (o.length > 0)
				if (o[0] != null)
					listnew.add(list.get(i));
		}
		listnew = sortList(listnew);
		List listnew2 = new ArrayList();

		BigDecimal val = null;
		Date lastdate = null;

		for (int i = 0; i < list.size(); i++) {
			if (i == 0) {
				listnew2.add(listnew.get(i));
				val = (BigDecimal) ((Object[]) listnew.get(i))[indexValue];
				lastdate = (Date) ((Object[]) listnew.get(i))[indexDate];
			} else {
				if (val instanceof BigDecimal) {
					if (((BigDecimal) ((Object[]) listnew.get(i))[indexValue]).compareTo((BigDecimal) val) != 0) {
						Object[] newrow = copyArray((Object[]) listnew.get(i));
						newrow[indexValue] = val;
						listnew2.add(newrow);
						lastdate = adjustArrayDate(lastdate, newrow, indexDate, 0);

						newrow = (Object[]) listnew.get(i);
						lastdate = adjustArrayDate(lastdate, newrow, indexDate, 1);
						listnew2.add(newrow);
						val = (BigDecimal) ((Object[]) listnew.get(i))[indexValue];
					} else {
						listnew2.add(listnew.get(i));
						lastdate = adjustArrayDate(lastdate, (Object[]) listnew2.get(listnew2.size() - 1), indexDate,
								0);
					}
				}
			}
		}
		return listnew2;
	}

	private static Date adjustArrayDate(Date lastdate, Object[] row, int indexDate, int iOffset) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(((Date) row[indexDate]).getTime());
		cal.add(Calendar.MINUTE, iOffset);
		while (lastdate.compareTo(new Date(cal.getTimeInMillis())) > 0) {
			cal.add(Calendar.MINUTE, 1);
		}
		row[indexDate] = new Date(cal.getTimeInMillis());
		return (Date) row[indexDate];
	}

	private static Object[] copyArray(Object[] data) {
		Object[] newData = new Object[data.length];
		for (int i = 0; i < data.length; i++) {
			newData[i] = data[i];
		}
		return newData;
	}

	/**
	 * Wandelt einen BigDecimal im AT Format in einen BigDecimal um
	 * 
	 * @param bigDecimal ist die Stringrepresentation des BigDecimal
	 * @return null wenn bigDecimal nicht umgewandelt werden konnte, ansonsten der
	 *         BigDecimal
	 */
	public static BigDecimal toBigDecimal(String bigDecimal) {
		return toBigDecimal(bigDecimal, new Locale("de", "AT"));
	}

	/**
	 * Wandelt einen BigDecimalString im angegebenen Locale in einen BigDecimal um
	 * 
	 * @param bigDecimal   die Stringrepresentation in der jeweiligen Locale
	 * @param stringLocale die Locale
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
			DecimalFormat nf = (DecimalFormat) NumberFormat.getInstance(stringLocale);
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

	public static BufferedImage bildUm90GradDrehenWennNoetig(BufferedImage inputImage) {

		int width = inputImage.getWidth();
		int height = inputImage.getHeight();

		if (width > height) {

			BufferedImage returnImage = new BufferedImage(height, width, BufferedImage.TYPE_INT_RGB);

			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					returnImage.setRGB(y, width - x - 1, inputImage.getRGB(x, y));
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
	 * Wandelt einen String in einen BigInteger um unter Ber&uuml;cksichtigung des
	 * Radix
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

	public static BigDecimal zinsesZins(BigDecimal betrag, BigDecimal zinsenInProzent, int iLaufzeit) {
		if (betrag != null && zinsenInProzent != null) {
			BigDecimal faktor = BigDecimal.ONE
					.add(zinsenInProzent.divide(new BigDecimal(100), 6, BigDecimal.ROUND_HALF_EVEN));
			while (iLaufzeit > 0) {
				betrag = betrag.multiply(faktor);
				iLaufzeit--;
			}
		} else {
			return betrag;
		}

		return Helper.rundeKaufmaennisch(betrag, 2);
	}

	/**
	 * Wandelt einen String in einen Integer um
	 * 
	 * @param integer
	 * @return null wenn der STring ein ung&uuml;ltiges Format hat, ansonsten den
	 *         Integer
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
				String zahlenteil = vorlage.substring(iStartZahl, iEndeZahl + 1);

				// long zahl = new Long(zahlenteil);
				// zahl = zahl + exemplar;
				// BigInteger zahl = new BigInteger(zahlenteil);
				// zahl = zahl.add(new BigInteger("" + exemplar));
				// String zahlenteilNeu = new String(zahl + "");

				String zahlenteilNeu = Helper.naechsteSeriennr(zahlenteil, exemplar);
				int iNeueLaenge = zahlenteilNeu.length();
				if (iNeueLaenge < zahlenteil.length()) {
					iNeueLaenge = zahlenteil.length();
				}
				zahlenteilNeu = Helper.fitString2LengthAlignRight(zahlenteilNeu + "", iNeueLaenge, '0');
				// Neue Artikelnummer zusammenbauen

				return vorlage.substring(0, iStartZahl) + zahlenteilNeu + vorlage.substring(iEndeZahl + 1);

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

	private static VDA4092Checksum vda4092Checksum;

	private static VDA4092Checksum getVda4092Calculator() {
		if (vda4092Checksum == null) {
			vda4092Checksum = new VDA4092Checksum();
		}
		return vda4092Checksum;
	}

	public static String getVDA4092Barcode(String barcode) {
		return getVda4092Calculator().getBarcode(barcode);
	}

	public static Character getVDA4092Checksum(String barcode) {
		return getVda4092Calculator().getChecksumCharacter(barcode);
	}

	private static SSCCBuilder ssccBuilder;

	private static SSCCBuilder getSSCCBuilder() {
		if (ssccBuilder == null) {
			ssccBuilder = new SSCCBuilder();
		}
		return ssccBuilder;
	}

	// SP9020
	public static String getSubreportPath(String modul, String subreportName, String mandantCNr, Locale locale,
			String subDirectory) throws Throwable {

		if (!subreportName.endsWith(".jasper")) {
			subreportName += ".jasper";
		}
		TheClientDto theClientDto = null;
		String sPfad = SystemServicesFacBean.getPathFromLPDir(modul, subreportName, mandantCNr, locale, subDirectory,
				theClientDto);
		if (sPfad == null) {
			sPfad = SystemServicesFacBean.getPathFromLPDir("allgemein", subreportName, mandantCNr, locale, subDirectory,
					theClientDto);
		}

//		if(sPfad!=null) {
//			File f = new File(sPfad);
//			sPfad=sPfad.substring(0, sPfad.length() - f.getName().length());
//		}
//		
		return sPfad;
	}

	public static String getSSCC(Long packstknr, String mandantCnr) throws Throwable {
		if (packstknr == null)
			return null;

		ParameterFac parameterFac = FacLookup.lookup(new InitialContext(), ParameterFacBean.class, ParameterFac.class);
		String ssccBasisnummer = parameterFac.getGS1BasisnummerSSCC(mandantCnr);
		if (Helper.isStringEmpty(ssccBasisnummer))
			return "Keine Basisnummer";

		return getSSCCBuilder().basisnummerInklErweiterungsziffer(ssccBasisnummer).bezugsnummer(packstknr).build();
	}

	public static String joinStrings(String[] strings, String separator) {
		return StringUtils.join(strings, separator);
	}

	public static KalenderRechner asKalender(Timestamp ts) {
		return KalenderRechner.createFromTimeStamp(ts);
	}

	public static KalenderRechner asKalender(Date date) {
		return KalenderRechner.createFromUtilDate(date);
	}

	public static KalenderRechner asKalender(java.sql.Date date) {
		return KalenderRechner.createFromSqlDate(date);
	}

	/**
	 * Gibt {@link Supplier#get()} vom &uuml;bergebenen supplier zur&uuml;ck. Kann
	 * im Report benutzt werden um mit Multi-Line Lambda expressions "echten" Java
	 * Code in Expressions zu verwandeln ohne komplizierte Typecasts zu
	 * ben&ouml;tigen
	 * 
	 * @param supplier Der Supplier/Lambda Expression, um einen Wert zu bekommen
	 */
	public static <T> T multiLine(Supplier<T> supplier) {
		return supplier == null ? null : supplier.get();
	}
}
