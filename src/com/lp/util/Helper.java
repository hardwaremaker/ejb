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

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperPrint;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.benutzer.service.LogonFac;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.service.LandplzortDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.ServerConfiguration;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.report.JasperPrintLP;
import com.sun.media.jai.codec.ByteArraySeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.ImageEncoder;
import com.sun.media.jai.codec.TIFFEncodeParam;

/**
 * <p>
 * <b>frame</b><br/>
 * Helper Methoden und Konstanten, die client- und serverseitig verwendbar sind.
 * <br/>
 * Es sind hier ausschliesslich statische Methoden zulaessig. Diese Methoden
 * sind LP unabhaengig.
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004
 * </p>
 * <p>
 * Erstellungsdatum 2004-08-31
 * </p>
 * <br/>
 * 
 * @author uli walch
 * @version 1.0
 */

public final class Helper {
	// Sortierung
	public final static int SORTIERUNG_AUFSTEIGEND = 1;
	public final static int SORTIERUNG_ABSTEIGEND = 2;

	public final static int SORTIERUNG_NACH_DATUM = 1;
	public final static int SORTIERUNG_NACH_IDENT = 2;
	public final static int SORTIERUNG_NACH_KOSTENSTELLE_UND_KONTO = 3;
	public final static int SORTIERUNG_NACH_LOSNUMMER = 4;
	public final static int SORTIERUNG_NACH_LAGER_UND_LAGERORT = 5;
	public final static int SORTIERUNG_NACH_LAGERORT_UND_LAGER = 6;
	public final static int SORTIERUNG_NACH_MONTAGEART_UND_SCHALE = 7;
	public final static int SORTIERUNG_NACH_ARTIKELKLASSE = 8;
	public final static int SORTIERUNG_NACH_ARTIKELKLASSE_UND_MATERIAL = 9;
	public final static int SORTIERUNG_NACH_ARBEITSGANG = 10;
	public final static int SORTIERUNG_NACH_BELEGNR = 11;
	public final static int SORTIERUNG_NACH_ARTIKELBEZEICHNUNG = 12;

	public final static String CR_LF = "" + (char) 0x0d + (char) 0x0a;
	public final static String LINE_SEPARATOR = "\n";

	public final static Object NO_VALUE_AND_THATS_OK = new Object();

	public final static BigDecimal DAY_IN_MS = new BigDecimal(3600000l);

	/**
	 * MB: Instantiierung dieser Klasse verhindern.
	 */
	private Helper() {
		// nothing here
	}

	/**
	 * ersetzt Strich durch Punkt in einem String
	 * 
	 * @param stringBuffer
	 *            String
	 * @return String
	 */
	public static String replaceStrichDurchPunktInStringBuffer(
			StringBuffer stringBuffer) {

		for (int i = 0; i < stringBuffer.length(); i++) {
			if (stringBuffer.indexOf("-") != -1) {
				stringBuffer.replace(stringBuffer.indexOf("-"),
						stringBuffer.indexOf("-") + 1, ".");
			}
		}

		return stringBuffer.toString();
	}

	public static String[] intelligenteWorttrennung(int[] laengen, String text) {
		// Trennen bei Space/Komma/Bindestrich
		if (text != null) {
			String[] teile = new String[laengen.length];

			for (int i = 0; i < laengen.length; i++) {
				if (text.length() <= laengen[i]) {
					teile[i] = text;
					return teile;
				} else {

					boolean bTrennzeichenGefunden = false;
					for (int j = laengen[i]; j > 0; j--) {

						char c = text.charAt(j - 1);

						if (c == ' ' || c == ',' || c == '-') {
							bTrennzeichenGefunden = true;
							teile[i] = text.substring(0, j);
							text = text.substring(j);
							break;
						}

					}
					if (bTrennzeichenGefunden == false) {
						teile[i] = text.substring(0, laengen[i]);
						text = text.substring(laengen[i]);
					}

				}
			}
			return teile;

		} else {
			return null;
		}

	}

	public static String formatWarenzugangsreferenz(
			ArrayList<com.lp.server.artikel.service.WarenzugangsreferenzDto> list) {

		byte[] cRLFAscii = { 13, 10 };

		String weReferenz = "";

		for (int i = 0; i < list.size(); i++) {

			weReferenz += list.get(i).getBelegart() + "|"
					+ list.get(i).getBelegnummer() + "|";

			if (list.get(i).getZusatz() != null) {
				weReferenz += list.get(i).getZusatz() + "|";
			} else {
				weReferenz += "|";
			}
			if (list.get(i).getTBelegdatum() != null) {
				weReferenz += Helper.formatDatum(list.get(i).getTBelegdatum(),
						Locale.GERMAN) + "|";
			} else {
				weReferenz += "|";
			}

			if (list.get(i).getPosition1() != null) {
				weReferenz += list.get(i).getPosition1() + "|";
			} else {
				weReferenz += "|";
			}

			if (list.get(i).getPosition2() != null) {
				weReferenz += list.get(i).getPosition2() + "|";
			} else {
				weReferenz += "|";
			}

			weReferenz += new String(cRLFAscii);
		}

		return weReferenz;
	}

	public static String erzeugeMD5Hash(String password) {
		String plainText = password;
		MessageDigest mdAlgorithm;
		StringBuffer hexString = new StringBuffer();

		try {
			mdAlgorithm = MessageDigest.getInstance("MD5");
			mdAlgorithm.update(plainText.getBytes());
			byte[] digest = mdAlgorithm.digest();

			for (int i = 0; i < digest.length; i++) {
				plainText = Integer.toHexString(0xFF & digest[i]);

				if (plainText.length() < 2) {
					plainText = "0" + plainText;
				}

				hexString.append(plainText);
			}
		} catch (NoSuchAlgorithmException ex) {
			return null;
		}

		return hexString.toString();

	}

	public static String fitString2Length(String string2Fit, int newLength,
			char fillUpChar) {
		if (string2Fit == null || string2Fit.length() == newLength) {
			return string2Fit;
		}

		String retVal = null;
		if (string2Fit.length() > newLength) {
			retVal = string2Fit.substring(0, newLength);
		} else {
			StringBuffer sb = new StringBuffer(string2Fit);
			while (sb.length() < newLength) {
				sb.append(fillUpChar);
			}

			retVal = sb.toString();
		}
		return retVal;
	}

	public static String fitString2LengthHTMLBefuelltMitLeerzeichen(
			String string2Fit, int newLength) {
		return fitString2LengthHTMLBefuelltMitLeerzeichen(string2Fit,
				newLength, false);
	}

	public static String fitString2LengthHTMLBefuelltMitLeerzeichen(
			String string2Fit, int newLength, boolean rechtsbuendig) {

		if (string2Fit == null) {
			string2Fit = "";
		}

		string2Fit = string2Fit.trim();

		if (string2Fit.length() == newLength) {
			return string2Fit;
		}

		String retVal = null;
		if (string2Fit.length() > newLength) {
			retVal = string2Fit.substring(0, newLength);
		} else {
			StringBuffer sb = new StringBuffer(string2Fit);

			int iDiff = newLength - sb.length();
			for (int i = 0; i < iDiff; i++) {
				if (rechtsbuendig) {
					sb.insert(0, "&nbsp;");
				} else {
					sb.append("&nbsp;");
				}

			}

			retVal = sb.toString();
		}
		return retVal;
	}

	/**
	 * Ermittelt die Bezeichnung eines Tages (Montag, Dienstag...)
	 * 
	 * @param tag
	 *            IntegerWert
	 * @return Bezeichnung des Tages
	 * @exception RemoteException
	 * @exception Exception
	 */
	public static String holeTagbezeichnungLang(int tag) {
		String tagbezeichnung = null;
		switch (tag) {
		case Calendar.MONDAY: {
			tagbezeichnung = ZeiterfassungFac.TAGESART_MONTAG;
			break;
		}
		case Calendar.TUESDAY: {
			tagbezeichnung = ZeiterfassungFac.TAGESART_DIENSTAG;
			break;
		}
		case Calendar.WEDNESDAY: {
			tagbezeichnung = ZeiterfassungFac.TAGESART_MITTWOCH;
			break;
		}
		case Calendar.THURSDAY: {
			tagbezeichnung = ZeiterfassungFac.TAGESART_DONNERSTAG;
			break;
		}
		case Calendar.FRIDAY: {
			tagbezeichnung = ZeiterfassungFac.TAGESART_FREITAG;
			break;
		}
		case Calendar.SATURDAY: {
			tagbezeichnung = ZeiterfassungFac.TAGESART_SAMSTAG;
			break;
		}
		case Calendar.SUNDAY: {
			tagbezeichnung = ZeiterfassungFac.TAGESART_SONNTAG;
			break;
		}
		}
		return tagbezeichnung;
	}

	/**
	 * Holt die Dateiendung aus dem Dateinamen
	 * 
	 * @param filename
	 *            zB.: "Beispiel.txt"
	 * @return den Mimetype, zB.: ".txt"
	 */
	public static String getMime(String filename) {
		int dot = filename.lastIndexOf(".");
		return dot == -1 ? "" : filename.substring(dot);
	}

	/**
	 * Holt den Namen der Datei ohne Dateiendung
	 * 
	 * @param filename
	 *            zB.: "Beispiel.txt"
	 * @return zB.: "Beispiel"
	 */
	public static String getName(String filename) {
		int dot = filename.lastIndexOf(".");
		return dot == -1 ? filename : filename.substring(0, dot);
	}

	public static String konvertiereDatum2StelligAuf4Stellig(String jahr2stellig) {
		if (jahr2stellig == null) {
			return null;
		}
		if (jahr2stellig.length() < 2) {
			return jahr2stellig;
		}

		try {
			int iJahr = Integer.parseInt(jahr2stellig);

			String jahr4Stellig = null;

			if (iJahr >= 0 && iJahr <= 9) {
				jahr4Stellig = "20" + jahr2stellig;
			} else if (iJahr > 9 && iJahr < 40) {
				jahr4Stellig = "20" + jahr2stellig;
			} else {
				jahr4Stellig = "19" + jahr2stellig;
			}
			return jahr4Stellig;
		} catch (NumberFormatException ex) {
			return jahr2stellig;
		}
	}

	public static String fitString2LengthAlignRight(String string2Fit,
			int newLength, char fillUpChar) {
		if (string2Fit == null || string2Fit.length() == newLength) {
			return string2Fit;
		}

		String retVal = null;
		if (string2Fit.length() > newLength) {
			retVal = string2Fit.substring(0, newLength);
		} else {
			StringBuffer sb = new StringBuffer(string2Fit);
			while (sb.length() < newLength) {
				sb.insert(0, fillUpChar);
			}
			retVal = sb.toString();
		}
		return retVal;
	}

	/**
	 * rechnet Tage (Integer) in Millisekunden zur&uuml;ck und gibt eine long
	 * wert zurueck
	 * 
	 * @param value
	 *            Integer
	 * @return long
	 */
	public static long calculateDaysBackIntoMilliseconds(Integer value) {
		return (long) value.intValue() * 24 * 3600 * 1000;
	}

	/**
	 * berechnet das prozentuelle verhaeltnis von 2 Zahlen
	 * 
	 * @param numberA
	 *            BigDecimal
	 * @param numberB
	 *            BigDecimal
	 * @return BigDecimal
	 */
	public static BigDecimal calculateRatioInDecimal(BigDecimal numberA,
			BigDecimal numberB) {
		BigDecimal result = null;
		if (numberA.compareTo(numberB) < 0) {
			result = new BigDecimal(1).subtract(numberA.divide(numberB,
					BigDecimal.ROUND_HALF_EVEN));
		} else if (numberA.compareTo(numberB) == 0) {
			result = new BigDecimal(0);
		} else {
			result = new BigDecimal(1).subtract(numberB.divide(numberA,
					BigDecimal.ROUND_HALF_EVEN));
		}
		return result.multiply(new BigDecimal(100.0));
	}

	public static Map sortByValue(Map map) {
		List list = new LinkedList(map.entrySet());
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o1)).getValue())
						.compareTo(((Map.Entry) (o2)).getValue());
			}
		});
		// logger.info(list);
		Map result = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	/**
	 * key auffuellen
	 * 
	 * @param sI
	 *            String
	 * @param iFillToI
	 *            int
	 * @return String
	 */
	public static String fillStringWithBlankRight(String sI, int iFillToI) {
		int iHelp;

		if (sI.length() < iFillToI) {
			StringBuffer buff = new StringBuffer();
			iHelp = sI.length();
			iHelp = iFillToI - iHelp;
			buff.append(sI);

			for (int j = 0; j < iHelp; j++) {
				buff.append(" ");
			}
			sI = buff.toString();
		}
		return sI;
	}

	public static byte[] konvertierePDFFileInMultipageTiff(byte[] bai,
			int resolution) {
		byte[] out = null;
		try {

			InputStream is = new ByteArrayInputStream(bai);
			PDDocument document = null;
			BufferedImage image[] = null;
			try {
				document = PDDocument.load(is);

				int numPages = document.getNumberOfPages();

				image = new BufferedImage[numPages];

				PDFRenderer renderer = new PDFRenderer(document);
				for (int i = 0; i < numPages; i++) {

					image[i] = renderer.renderImageWithDPI(i, resolution); // Windows
					// native
					// DPI

				}

				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				TIFFEncodeParam param = new TIFFEncodeParam();
				param.setCompression(TIFFEncodeParam.COMPRESSION_PACKBITS);
				param.setLittleEndian(true);
				param.setWriteTiled(false);

				ImageEncoder encoder = com.sun.media.jai.codec.ImageCodec
						.createImageEncoder("tiff", baos, param);
				Vector vector = new Vector();
				for (int i = 1; i < image.length; i++) {
					vector.add(image[i]);
				}
				param.setExtraImages(vector.iterator());
				encoder.encode(image[0]);
				baos.flush();
				baos.close();

				out = baos.toByteArray();

			} catch (IOException e) {
				e.printStackTrace();
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e.getMessage());

			} finally {
				if (document != null) {

					try {
						document.close();
					} catch (IOException e) {
						e.printStackTrace();
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER,
								e.getMessage());

					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return out;
	}

	/**
	 * Java boolean in Java Short konvertieren.
	 * 
	 * @param pBool
	 *            boolean
	 * @return Short
	 */
	public static final Short boolean2Short(boolean pBool) {
		return new Short(pBool ? (short) 1 : (short) 0);
		//
		// Short myShort = null;
		//
		// if (pBool) {
		// myShort = new Short((short) 1);
		// } else {
		// myShort = new Short((short) 0);
		//
		// }
		// return myShort;
	}

	public static final Short getShortTrue() {
		return new Short((short) 1);
	}

	public static final Short getShortFalse() {
		return new Short((short) 0);
	}

	/**
	 * Java Short in Java boolean konvertieren.
	 * 
	 * @param pShort
	 *            Short
	 * @return boolean
	 */
	public static final boolean short2boolean(Short pShort) {
		if (null == pShort)
			return false;
		return pShort.intValue() == 1;
	}

	/**
	 * Java Short in Java boolean konvertieren.
	 * 
	 * @param pShort
	 *            Short
	 * @return boolean
	 */
	public static final boolean short2boolean(short pShort) {
		return short2boolean(new Short(pShort));
	}

	/**
	 * Java Short in Java boolean konvertieren.
	 * 
	 * @param pShort
	 *            Short
	 * @return boolean
	 */
	public static final Boolean short2Boolean(Short pShort) {
		if (null == pShort)
			return false;
		return short2Boolean(pShort.shortValue());
	}

	/**
	 * Java Short in Java boolean konvertieren.
	 * 
	 * @param pShort
	 *            Short
	 * @return boolean
	 */
	public static final Boolean short2Boolean(short pShort) {
		// return short2Boolean(new Short(pShort));
		return pShort == 1;
	}

	public static final BigDecimal rundeGeldbetrag(BigDecimal bigDecimal) {
		return rundeKaufmaennisch(bigDecimal, FinanzFac.NACHKOMMASTELLEN_I);
	}

	/**
	 * kaufmaennisch runden.
	 * 
	 * @param bigDecimal
	 *            BigDecimal
	 * @param stellen
	 *            int
	 * @return BigDecimal
	 */
	public static final BigDecimal rundeKaufmaennisch(BigDecimal bigDecimal,
			int stellen) {
		// return rundeKaufmaennisch(bigDecimal, new Integer(stellen));
		return bigDecimal == null ? null : bigDecimal.setScale(stellen,
				BigDecimal.ROUND_HALF_EVEN);
	}

	/**
	 * kaufmaennisch runden.
	 * 
	 * @param bigDecimal
	 *            BigDecimal
	 * @param stellen
	 *            int
	 * @return BigDecimal
	 */
	public static final BigDecimal rundeKaufmaennisch(BigDecimal bigDecimal,
			Integer stellen) {
		return rundeKaufmaennisch(bigDecimal, stellen.intValue());

		/*
		 * Wir haben meist den rundeKaufmaennisch(.., int). Der wird auf einen
		 * new Integer(int) aufgeblasen, um dann hier wieder auf einen int
		 * zurueckgewandelt zu werden? Das ist doch Bloedsinn.
		 */
		// if (bigDecimal != null) {
		// return bigDecimal.setScale(stellen.intValue(),
		// BigDecimal.ROUND_HALF_EVEN);
		// } else {
		// return null;
		// }
	}

	public static final BigDecimal getBigDecimalNull() {
		return Helper.rundeKaufmaennisch(new BigDecimal(0), 4);
	}

	/**
	 * Parst eine mit Komma oder Punkt als Dezimaltrennzeichen formatierte Zahl
	 * zu einem BigDecimal
	 * 
	 * @param commaFormatted
	 * @return den geparsten BigDecimal Wert
	 */
	public static BigDecimal toBigDecimal(String commaFormatted) {
		return new BigDecimal(commaFormatted
				.replaceAll("\\.(?=[\\d\\.]*,)", "").replaceAll(",", "."));
	}

	/**
	 * Prueft ob ein Datum gleich oder spaeter als eine bestimmte Untergrenze
	 * liegt. Das Datum darf nicht unter der Untergrenze liegen.
	 * 
	 * @param pDatum
	 *            Date
	 * @param pUntergrenze
	 *            Date
	 * @return boolean
	 */
	public static boolean datumGueltigInbezugAufUntergrenze(
			java.sql.Date pDatum, java.sql.Date pUntergrenze) {
		boolean gueltig = false;

		if (pDatum.equals(pUntergrenze) || (pDatum.after(pUntergrenze))) {
			gueltig = true;
		}

		return gueltig;
	}

	/**
	 * Prueft ob ein Datum gleich oder vor einer bestimmten Obergrenze liegt.
	 * Das Datum darf nicht ueber der Obergrenze liegen.
	 * 
	 * @param pDatum
	 *            Date
	 * @param pObergrenze
	 *            Date
	 * @return boolean
	 */
	public static boolean datumGueltigInbezugAufObergrenze(
			java.sql.Date pDatum, java.sql.Date pObergrenze) {
		boolean gueltig = false;

		if (pDatum.before(pObergrenze) || pDatum.equals(pObergrenze)) {
			gueltig = true;
		}

		return gueltig;
	}

	/**
	 * Wandelt einen String in ein Locale um
	 * 
	 * @param s
	 *            String
	 * @throws Exception
	 * @return Locale
	 */
	public static java.util.Locale string2Locale(String s) {
		if (s == null) {
			throw new IllegalArgumentException("s == null");
		} else if (s.length() != 10) {
			throw new IllegalArgumentException("s.length() != 10");
		} else {
			return new Locale(s.substring(0, 2), s.substring(2, 4), s
					.substring(4, 10).trim());
		}
	}

	/**
	 * Wandelt einen Locale in einen String um
	 * 
	 * @param l
	 *            Locale
	 * @throws Exception
	 * @return String
	 */
	public static String locale2String(Locale l) {
		if (l == null) {
			return null;
		}
		String s = l.getLanguage() + l.getCountry() + l.getVariant()
				+ "          ";
		return s.substring(0, 10);
	}

	/**
	 * extractDate
	 * 
	 * @param ts
	 *            Timestamp
	 * @throws EJBExceptionLP
	 * @return Date
	 */
	public static java.sql.Date extractDate(java.sql.Timestamp ts)
			throws EJBExceptionLP {
		if (ts == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("ts == null"));
		}
		return new java.sql.Date(ts.getTime());
	}

	/**
	 * Setzt Stunde/Minute/Millisekunde eines Timestamps auf 0
	 * 
	 * @param ts
	 *            Timestamp
	 * @return Timestamp
	 */
	public static java.sql.Timestamp cutTimestamp(java.sql.Timestamp ts) {
		if (ts != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(ts.getTime());
			cal.set(Calendar.MILLISECOND, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			return new java.sql.Timestamp(cal.getTimeInMillis());
		} else {
			return null;
		}
	}

	public static String cutString(String s, int length) {
		if (s == null)
			return null;
		s = s.trim();
		return s.trim().length() > length ? s.substring(0, length) : s;
	}

	public static String getAllStartCharacters(String str) {
		if (str == null) {
			return null;
		}
		StringBuffer strBuff = new StringBuffer();
		char c;
		for (int i = 0; i < str.length(); i++) {
			c = str.charAt(i);

			if (Character.isLetter(c)) {
				strBuff.append(c);
			} else {
				break;
			}
		}
		return strBuff.toString().trim();
	}

	public static java.util.Date cutDate(java.util.Date dDate) {
		if (dDate != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(dDate.getTime());
			cal.set(Calendar.MILLISECOND, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			return new java.sql.Date(cal.getTimeInMillis());
		} else {
			return null;
		}
	}

	/**
	 * Setzt Stunde/Minute/Millisekunde eines Dates auf 0
	 * 
	 * @param dDate
	 *            Date
	 * @return Date
	 */
	public static java.sql.Date cutDate(java.sql.Date dDate) {
		if (dDate != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(dDate.getTime());
			cal.set(Calendar.MILLISECOND, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			return new java.sql.Date(cal.getTimeInMillis());
		} else {
			return null;
		}
	}

	public static String decode(String s) {
		if (s != null) {
			return new String(xorWithKey(base64Decode(s), "HELIUMV".getBytes()));
		} else {
			return null;
		}
	}

	public static String encode(String s) {
		if (s != null) {
			return base64Encode(xorWithKey(s.getBytes(), "HELIUMV".getBytes()));
		} else {
			return null;
		}
	}

	private static byte[] xorWithKey(byte[] a, byte[] key) {
		byte[] out = new byte[a.length];
		for (int i = 0; i < a.length; i++) {
			out[i] = (byte) (a[i] ^ key[i % key.length]);
		}
		return out;
	}

	private static String base64Encode(byte[] bytes) {
		BASE64Encoder enc = new BASE64Encoder();
		return enc.encode(bytes).replaceAll("\\s", "");

	}

	private static byte[] base64Decode(String s) {
		try {
			BASE64Decoder d = new BASE64Decoder();
			return d.decodeBuffer(s);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static VerkaufspreisDto getVkpreisBerechnet(
			VkpreisfindungDto vkpreisfindungDtoI) {
		VerkaufspreisDto verkaufspreisDto = null;

		if (vkpreisfindungDtoI.getVkpreisberechnetStufe() != null) {

			if (vkpreisfindungDtoI.getVkpreisberechnetStufe().equals(
					VkpreisfindungDto.VKPFPREISBASIS)) {
				verkaufspreisDto = vkpreisfindungDtoI.getVkpPreisbasis();
			} else if (vkpreisfindungDtoI.getVkpreisberechnetStufe().equals(
					VkpreisfindungDto.VKPFSTUFE1)) {
				verkaufspreisDto = vkpreisfindungDtoI.getVkpStufe1();
			} else if (vkpreisfindungDtoI.getVkpreisberechnetStufe().equals(
					VkpreisfindungDto.VKPFSTUFE2)) {
				verkaufspreisDto = vkpreisfindungDtoI.getVkpStufe2();
			} else if (vkpreisfindungDtoI.getVkpreisberechnetStufe().equals(
					VkpreisfindungDto.VKPFSTUFE3)) {
				verkaufspreisDto = vkpreisfindungDtoI.getVkpStufe3();
			}
		}

		if (verkaufspreisDto != null) {
			verkaufspreisDto.bdMaterialzuschlag = vkpreisfindungDtoI
					.getNMaterialzuschlag();
		}

		return verkaufspreisDto;
	}

	/**
	 * Setzt Stunde/Minute/Sekunde/Millisekunde eines Calendar auf 00:00:00 000.
	 * 
	 * @param calI
	 *            das Datum
	 * @return Calendar
	 */
	public static Calendar cutCalendar(Calendar calI) {
		Calendar calO = null;

		if (calI != null) {
			calO = (Calendar) calI.clone();

			calO.set(Calendar.HOUR_OF_DAY, 0);
			calO.set(Calendar.MINUTE, 0);
			calO.set(Calendar.SECOND, 0);
			calO.set(Calendar.MILLISECOND, 0);
		}

		return calO;
	}

	/**
	 * Setzt Stunde/Minute/Sekunde/Millisekunde eines Calendar auf 23:59:59 999.
	 * 
	 * @param calI
	 *            das Datum
	 * @return Calendar
	 */
	public static Calendar fillCalendar(Calendar calI) {
		Calendar calO = null;

		if (calI != null) {
			calO = (Calendar) calI.clone();

			calO.set(Calendar.HOUR_OF_DAY, 23);
			calO.set(Calendar.MINUTE, 59);
			calO.set(Calendar.SECOND, 59);
			calO.set(Calendar.MILLISECOND, 999);
		}

		return calO;
	}

	/**
	 * Formatier Datum mit "/" getrennt
	 * 
	 * @param date
	 *            java.sql.Date
	 * @return String
	 */
	public static String formatDateWithSlashes(java.sql.Date date) {
		if (date != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(date.getTime());

			String s = "";
			int iMonat = cal.get(Calendar.MONTH) + 1;
			s = cal.get(Calendar.DAY_OF_MONTH) + "/" + iMonat + "/"
					+ cal.get(Calendar.YEAR);

			return s;
		} else {
			return null;
		}
	}

	public static String formatTimestampWithSlashes(
			java.sql.Timestamp tsZeitpunkt) {
		if (tsZeitpunkt != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(tsZeitpunkt.getTime());

			String s = "";
			int iMonat = cal.get(Calendar.MONTH) + 1;
			s = cal.get(Calendar.DAY_OF_MONTH)
					+ "/"
					+ iMonat
					+ "/"
					+ cal.get(Calendar.YEAR)
					+ " "
					+ cal.get(Calendar.HOUR_OF_DAY)
					+ ":"
					+ cal.get(Calendar.MINUTE)
					+ ":"
					+ cal.get(Calendar.SECOND)
					+ "."
					+ fitString2LengthAlignRight(cal.get(Calendar.MILLISECOND)
							+ "", 3, '0');

			return s;
		} else {
			return null;
		}
	}

	/**
	 * Hole das zugehoerige Datepattern zu locale.<BR>
	 * 
	 * @param locale
	 *            Locale
	 * @return The datepattern value
	 */
	public static String getDatepattern(java.util.Locale locale) {

		if (locale == null) {
			throw new NullPointerException("locale == null");
		}
		java.util.Locale.setDefault(locale);
		SimpleDateFormat sDF = new SimpleDateFormat();
		String sDFP = sDF.toPattern();
		StringBuffer sbDFP = new StringBuffer(sDFP);
		int xEnd = sDFP.indexOf(" ");
		String sRet = sbDFP.substring(0, xEnd);
		// Umwandeln in 4 stellige Jahreszahl
		int iFind = sRet.toLowerCase().indexOf("yyyy");
		if (iFind < 0) {
			iFind = sRet.toLowerCase().indexOf("yy");
			if (iFind > -1) {
				String sTemp1 = sRet.substring(0, iFind);
				String sTemp2 = sRet.substring(iFind);
				sRet = sTemp1 + "yy" + sTemp2;
			}
		}
		return sRet;
	}

	/**
	 * Den Wert eines Prozentsatzes einer Zahl berechnen, kaufmaennisch auf n
	 * Nachkommastellen gerundet.
	 * 
	 * @param zahl
	 *            BigDecimal
	 * @param prozentsatz
	 *            Double
	 * @param nachkommastellen
	 *            int
	 * @return BigDecimal
	 */
	public static BigDecimal getProzentWert(BigDecimal zahl,
			BigDecimal prozentsatz, int nachkommastellen) {
		// Prozentsatz gleich durch 100 dividieren.
		BigDecimal prozentsatz2 = prozentsatz.movePointLeft(2);
		BigDecimal result = zahl.multiply(prozentsatz2);
		return rundeKaufmaennisch(result, nachkommastellen);
	}

	public static BigDecimal getWertPlusProzent(BigDecimal zahl,
			BigDecimal prozentsatz, int nachkommastellen) {
		BigDecimal result = zahl.subtract(getProzentWert(zahl, prozentsatz,
				nachkommastellen));
		return rundeKaufmaennisch(result, nachkommastellen);
	}

	/**
	 * Prozentsatz aus 2 BigDecimals berechnen.
	 * 
	 * @param b1
	 *            Gesamtwert
	 * @param b2
	 *            Wert (Prozent vom gesamtwert)
	 * @param nachkommastellen
	 *            int
	 * @return Double Prozentsatz
	 * 
	 *         rechnet nicht BigDecimal genau
	 */
	@Deprecated
	public static Double getProzentsatz(BigDecimal b1, BigDecimal b2,
			int nachkommastellen) {
		if (b1.doubleValue() != 0.0) {
			return new Double(b2.divide(b1, nachkommastellen,
					BigDecimal.ROUND_HALF_EVEN).doubleValue() * 100.0);
		} else {
			return new Double(0);
		}
	}

	/**
	 * Prozentsatz aus 2 BigDecimals berechnen.
	 * 
	 * @param b1
	 *            Gesamtwert
	 * @param b2
	 *            Wert (Prozent vom gesamtwert)
	 * @param nachkommastellen
	 *            int
	 * @return BigDecimal Prozentsatz
	 */
	public static BigDecimal getProzentsatzBD(BigDecimal b1, BigDecimal b2,
			int nachkommastellen) {
		return b1.signum() != 0 ? b2.divide(b1, nachkommastellen + 2,
				BigDecimal.ROUND_HALF_EVEN).movePointRight(2) : BigDecimal.ZERO
				.setScale(nachkommastellen);
	}

	/**
	 * Prozentsatz aus 2 BigDecimals berechnen. (b1 - b2)/b1 * 100
	 * 
	 * @param b1
	 *            Gesamtwert
	 * @param b2
	 *            Wert (Prozent vom gesamtwert)
	 * @param nachkommastellen
	 *            int
	 * @return BigDecimal Prozentsatz
	 */
	public static BigDecimal getReduktionProzent(BigDecimal b1, BigDecimal b2,
			int nachkommastellen) {
		if (b1.doubleValue() != 0.0) {
			return b1.subtract(b2)
					.divide(b1, nachkommastellen, BigDecimal.ROUND_HALF_EVEN)
					.movePointLeft(2);
		} else {
			return new BigDecimal(0);
		}
	}

	/**
	 * Prozentsatz aus 2 BigDecimals berechnen. (b1 - b2)/b2 * 100
	 * 
	 * @param b1
	 *            Gesamtwert
	 * @param b2
	 *            Wert (Prozent vom gesamtwert)
	 * @param nachkommastellen
	 *            int
	 * @return BigDecimal Prozentsatz
	 */
	public static BigDecimal getAufschlagProzent(BigDecimal b1, BigDecimal b2,
			int nachkommastellen) {
		if (b1.doubleValue() != 0.0) {
			return b1.subtract(b2)
					.divide(b2, nachkommastellen, BigDecimal.ROUND_HALF_EVEN)
					.movePointLeft(2);
		} else {
			return new BigDecimal(0);
		}
	}

	/**
	 * Ein BigDecimal anhand eines Prozentsatzes aufteilen, kaufmaennisch auf n
	 * Nachkommastellen genau gerundet.
	 * 
	 * @param zahl
	 *            BigDecimal
	 * @param prozentsatz
	 *            Double
	 * @param nachkommastellen
	 *            int
	 * @return BigDecimal[] [0]=zahl * (1-prozentsatz), [1]=zahl * prozentsatz
	 */
	public static BigDecimal[] teileBigDecimal(BigDecimal zahl,
			Double prozentsatz, int nachkommastellen) {
		BigDecimal[] result = new BigDecimal[2];
		BigDecimal prozentsatz2 = new BigDecimal(prozentsatz.doubleValue());
		prozentsatz2 = prozentsatz2.movePointLeft(2);
		result[1] = zahl.multiply(prozentsatz2);
		result[0] = zahl.subtract(result[1]);
		rundeKaufmaennisch(result[0], nachkommastellen);
		rundeKaufmaennisch(result[1], nachkommastellen);
		return result;
	}

	/**
	 * Fuer Druck die Kurzzeichenkombination zum Andrucken zusammenbauen.
	 * 
	 * @param benAngelegt
	 *            dieser Benutzer hat den Datensatz angelegt
	 * @param benDrucken
	 *            dieser Benutzer druckt den Beleg
	 * @return String
	 */
	public static String getKurzzeichenkombi(String benAngelegt,
			String benDrucken) {
		StringBuffer buff = new StringBuffer("");

		if (benAngelegt != null) {
			buff.append(benAngelegt);
		} else {
			buff.append("???");
		}

		buff.append("/");

		if (benDrucken != null) {
			buff.append(benDrucken);
		} else {
			buff.append("???");
		}

		return buff.toString();
	}

	/**
	 * Die Differenz in Tagen zwischen 2 Datum's berechnen. liefert positive
	 * werte, wenn das Vergleichsdatum nach dem Bezugsdaum liegt.
	 * 
	 * @param daBezugsdatum
	 *            Date
	 * @param daVergleichsdatum
	 *            Date
	 * @return int
	 */
	public static int getDifferenzInTagen(java.util.Date daBezugsdatum,
			java.util.Date daVergleichsdatum) {
		if (daBezugsdatum == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("daBezugsdatum == null"));
		}
		if (daVergleichsdatum == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("daVergleichsdatum == null"));
		}

		// Neue Berechnungsmethode, wg. Sommer/Winterzeit
		Calendar cal_1 = new GregorianCalendar();
		Calendar cal_2 = new GregorianCalendar();
		cal_1.setTime(daBezugsdatum); // erster Zeitpunkt
		cal_2.setTime(daVergleichsdatum); // zweiter Zeitpunkt
		long time = cal_2.getTime().getTime() - cal_1.getTime().getTime(); // Differenz
																			// in
																			// ms
		long days = Math.round((double) time / (24. * 60. * 60. * 1000.)); // Differenz
																			// in
																			// Tagen

		return (int) days;
	}

	/**
	 * Eine Anzahl von Tagen zu einem Datum addieren.
	 * 
	 * @param datum
	 *            Date
	 * @param tage
	 *            int
	 * @return Date
	 */
	public static java.sql.Date addiereTageZuDatum(java.util.Date datum,
			int tage) {
		if (datum == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("datum == null"));
		}
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(datum);
		gc.add(Calendar.DATE, tage);
		return new java.sql.Date(gc.getTime().getTime());
	}

	public static java.sql.Timestamp addiereTageZuTimestamp(
			java.sql.Timestamp datum, int tage) {
		if (datum == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("datum == null"));
		}
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(datum);
		gc.set(Calendar.DATE, gc.get(Calendar.DATE) + tage);
		return new java.sql.Timestamp(gc.getTime().getTime());
	}

	/**
	 * Einen Mehrwertsteuerbetrag ausgehend vom Bruttobetrag ( 100+x % des
	 * nettobetrags) und dem Mehrwertsteuerprozentsatz (x) berechnen.
	 * 
	 * @param bdBrutto
	 *            BigDecimal
	 * @param dMwstsatz
	 *            double
	 * @return BigDecimal
	 */
	public static BigDecimal getMehrwertsteuerBetrag(BigDecimal bdBrutto,
			double dMwstsatz) {
		BigDecimal bdMwstSatz = new BigDecimal(dMwstsatz).movePointLeft(2);
		if (bdBrutto != null) {
			// BigDecimal bdBetragBasis = bdBrutto.divide(new BigDecimal(1)
			// .add(bdMwstSatz), FinanzFac.NACHKOMMASTELLEN,
			// BigDecimal.ROUND_HALF_EVEN);
			// return
			// Helper.rundeKaufmaennisch(bdBetragBasis.multiply(bdMwstSatz),
			// FinanzFac.NACHKOMMASTELLEN);
			BigDecimal bdBetragBasis = bdBrutto.divide(
					new BigDecimal(1).add(bdMwstSatz),
					FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN);
			return Helper.rundeKaufmaennisch(bdBrutto.subtract(bdBetragBasis),
					FinanzFac.NACHKOMMASTELLEN);
		} else {
			return null;
		}
	}

	/**
	 * Die durchschnittliche Breite eines Textes in Pixel anhand der Anzahl
	 * seiner Buchstaben ermitteln. Vorsicht: das ist nur ein Durchschnittswert.
	 * 
	 * @param iAnzahlBuchstaben
	 *            int
	 * @return int
	 */
	public static int getBreiteInPixel(int iAnzahlBuchstaben) {
		return iAnzahlBuchstaben * 6 + 6;
	}

	/**
	 * Helper Methode fuer das Drucken von Positionen in verschiedenen
	 * Belegarten.
	 * 
	 * @param iNummerI
	 *            die Nummer wird als String mit drei Stellen zurueckgegeben,
	 *            vorne wird mit Nullen aufgefuellt
	 * @return String die Nummer als dreistelliger String
	 */
	public static String formatPositionsnummer(int iNummerI) {
		String sNummerO = null;

		// dargestellt werden soll dreistellig
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumIntegerDigits(3);
		nf.setMinimumIntegerDigits(3);
		nf.setMaximumFractionDigits(0);
		nf.setMinimumFractionDigits(0);

		sNummerO = nf.format(iNummerI);

		return sNummerO;
	}

	/**
	 * Einen Betrag localeabhaengig formatieren.
	 * 
	 * @param bdBetrag
	 *            BigDecimal
	 * @param locale
	 *            Locale
	 * @return String
	 */
	public static String formatBetrag(BigDecimal bdBetrag, Locale locale) {
		return NumberFormat.getCurrencyInstance(locale).format(
				bdBetrag.doubleValue());
	}

	/**
	 * Eine Zahl localeabhaengig formatieren. geht fuer alle Wrapperklassen der
	 * basisdatentypen zb Double, ... sowie alle weiteren Subklassen von Number
	 * (BigDecimal, BigInteger, ...).
	 * 
	 * ACHTUNG: Numbers vom Typ Integer werden ignoriert MB 21.3.05
	 * 
	 * @param nZahl
	 *            Number
	 * @param locale
	 *            Locale
	 * @return String
	 */
	public static String formatZahl(Number nZahl, Locale locale) {
		if (nZahl instanceof Integer) {
			return nZahl.toString();
		}
		return NumberFormat.getNumberInstance(locale).format(
				nZahl.doubleValue());
	}

	/**
	 * Eine Zahl localeabhaengig formatieren. geht fuer alle Wrapperklassen der
	 * basisdatentypen zb Double, ... sowie alle weiteren Subklassen von Number
	 * (BigDecimal, BigInteger, ...).
	 * 
	 * ACHTUNG: Numbers vom Typ Integer werden ignoriert MB 21.3.05
	 * 
	 * @param nZahl
	 *            Number
	 * @param iNachkommastellen
	 *            int
	 * @param locale
	 *            Locale
	 * @return String
	 */
	public static String formatZahl(Number nZahl, int iNachkommastellen,
			Locale locale) {
		if (nZahl instanceof Integer) {
			return nZahl.toString();
		}
		NumberFormat nf = NumberFormat.getNumberInstance(locale);
		nf.setMaximumFractionDigits(iNachkommastellen);
		nf.setMinimumFractionDigits(iNachkommastellen);
		return nf.format(nZahl.doubleValue());
	}

	public static String formatZahlWennUngleichNull(Number nZahl,
			int iNachkommastellen, Locale locale) {
		if (nZahl != null && nZahl.doubleValue() == 0) {
			return "";
		}

		if (nZahl instanceof Integer) {
			return nZahl.toString();
		}
		NumberFormat nf = NumberFormat.getNumberInstance(locale);
		nf.setMaximumFractionDigits(iNachkommastellen);
		nf.setMinimumFractionDigits(iNachkommastellen);
		return nf.format(nZahl.doubleValue());
	}

	// public static String formatAndRoundCurrency(BigDecimal amount, int
	// iNachkommastellen, Locale locale) {
	// NumberFormat nf = NumberFormat.getNumberInstance(locale) ;
	// nf.setMaximumFractionDigits(iNachkommastellen) ;
	// nf.setMinimumFractionDigits(iNachkommastellen) ;
	// return nf.format(rundeKaufmaennisch(amount, iNachkommastellen)) ;
	// }

	public static String formatAndRoundCurrency(BigDecimal amount, Locale locale) {
		NumberFormat nf = NumberFormat.getNumberInstance(locale);
		nf.setMaximumFractionDigits(FinanzFac.NACHKOMMASTELLEN);
		nf.setMinimumFractionDigits(FinanzFac.NACHKOMMASTELLEN);
		return nf.format(rundeGeldbetrag(amount));
	}

	/**
	 * Ein Datum localeabhaengig formatieren.
	 * 
	 * @param date
	 *            Date
	 * @param locale
	 *            Locale
	 * @return String
	 */
	public static String formatDatum(Date date, Locale locale) {
		if (date != null) {
			return DateFormat.getDateInstance(DateFormat.MEDIUM, locale)
					.format(date);
		} else {
			return "";
		}
	}

	/**
	 * Ein Datum/Zeit localeabhaengig formatieren.
	 * 
	 * @param date
	 *            Date
	 * @param locale
	 *            Locale
	 * @return String
	 */
	public static String formatDatumZeit(Date date, Locale locale) {
		if (date != null) {
			return DateFormat.getDateInstance(DateFormat.MEDIUM, locale)
					.format(date)
					+ " "
					+ DateFormat.getTimeInstance(DateFormat.MEDIUM, locale)
							.format(date);
		} else {
			return "";
		}
	}

	public static String formatJJJJMMTT(long time) {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		return dateFormat.format(new Date(time));
	}

	public static String formatMMTT(long time) {
		DateFormat dateFormat = new SimpleDateFormat("MMdd");
		return dateFormat.format(new Date(time));
	}

	public static String formatTTMM(long time) {
		DateFormat dateFormat = new SimpleDateFormat("ddMM");
		return dateFormat.format(new Date(time));
	}

	/**
	 * Einen Timestamp localeabhaengig formatieren.
	 * 
	 * @param ts
	 *            Timestamp
	 * @param locale
	 *            Locale
	 * @return String
	 */
	public static String formatTimestamp(Timestamp ts, Locale locale) {
		return formatTimestamp(ts, DateFormat.MEDIUM, DateFormat.SHORT, locale);
	}

	/**
	 * Einen Timestamp localeabhaengig formatieren.
	 * 
	 * @param ts
	 *            Timestamp
	 * @param dateStyle
	 *            Konstante aus Klasse DateFormat
	 * @param timeStyle
	 *            Konstante aus Klasse DateFormat
	 * @param locale
	 *            Locale
	 * @return String
	 */
	public static String formatTimestamp(Timestamp ts, int dateStyle,
			int timeStyle, Locale locale) {
		return DateFormat.getDateTimeInstance(dateStyle, timeStyle, locale)
				.format(ts);
	}

	/**
	 * Aus einem Timestampt die Zeit Locale abhaengig herausholen.
	 * 
	 * @param ts
	 *            Timestamp
	 * @param locale
	 *            Locale
	 * @return String
	 */
	public static String formatTime(Timestamp ts, Locale locale) {
		return DateFormat.getTimeInstance(DateFormat.SHORT, locale).format(ts);
	}

	/**
	 * Einen String in einem bestimmten Format nach einem bestimmten Schema in
	 * ein Array von Strings umwandeln. Wird verwendet fuer das Lesen einer SNR
	 * oder Chargennummereingabe.
	 * 
	 * @param sStringI
	 *            ein String im Format "aa, ab, ac"
	 * @return String[]
	 * @throws EJBExceptionLP
	 */
	public static String[] erzeugeStringArrayAusString(String sStringI)
			throws EJBExceptionLP {
		if (sStringI == null || sStringI.trim().length() == 0) {
			// leerer String
			return new String[0];
		}
		int iAnzahl = 1;

		for (int i = 0; i < sStringI.length(); i++) {
			char c = sStringI.charAt(i);

			// ','
			if (c == 44) {
				iAnzahl++;
			}
		}

		if (sStringI.charAt(0) == 44
				|| sStringI.charAt(sStringI.length() - 1) == 44) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_SONDERZEICHEN_AN_FALSCHER_STELLE,
					new Exception("FEHLER_SONDERZEICHEN_AN_FALSCHER_STELLE"));

		}

		String[] sSeriennummernZerlegt = new String[iAnzahl];
		int iLetztePosition = 0;
		int sNRs = 0;

		for (int i = 0; i < sStringI.length(); i++) {
			char c = sStringI.charAt(i);

			// ','
			if (c == 44) {
				sSeriennummernZerlegt[sNRs] = sStringI.substring(
						iLetztePosition, i);
				sNRs++;
				iLetztePosition = i + 1;
			}

			if (i == sStringI.length() - 1) {
				sSeriennummernZerlegt[iAnzahl - 1] = sStringI.substring(
						iLetztePosition, sStringI.length());
			}
		}

		return sSeriennummernZerlegt;
	}

	/**
	 * Erzeugt aus einem String einzelne Serienummer und pr&uuml;ft die
	 * erzeugten Strings mit der Menge gegen, wenn angegeben
	 * 
	 * @param eingabe
	 *            String
	 * @param menge
	 *            BigDecimal
	 * @param bPruefeMenge
	 *            boolean
	 * @return String[]
	 */
	public static String[] erzeugeSeriennummernArray(String eingabe,
			java.math.BigDecimal menge, boolean bPruefeMenge) {
		if (eingabe == null) {
			return null;
		}
		eingabe = eingabe.replaceAll(" ", "");
		ArrayList<String> snrs = new ArrayList<String>();

		String[] getrennt = erzeugeStringArrayAusString(eingabe);

		for (int i = 0; i < getrennt.length; i++) {
			int minus = getrennt[i].indexOf(45);
			if (minus > 0) {
				String von = getrennt[i].substring(0, minus);
				String bis = getrennt[i].substring(minus + 1);
				int ziffernteil_laenge_von = 0;
				for (int j = von.length(); j > 0; j--) {

					char c = von.charAt(j - 1);

					if (c == '0' || c == '1' || c == '2' || c == '3'
							|| c == '4' || c == '5' || c == '6' || c == '7'
							|| c == '8' || c == '9') {
						ziffernteil_laenge_von++;
					} else {
						break;
					}
				}
				int ziffernteil_laenge_bis = 0;
				for (int j = bis.length(); j > 0; j--) {

					char c = bis.charAt(j - 1);

					if (c == '0' || c == '1' || c == '2' || c == '3'
							|| c == '4' || c == '5' || c == '6' || c == '7'
							|| c == '8' || c == '9') {
						ziffernteil_laenge_bis++;
					} else {
						break;
					}

				}
				if ((ziffernteil_laenge_von == ziffernteil_laenge_bis)) {

					if (ziffernteil_laenge_von == 0
							|| ziffernteil_laenge_bis == 0) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_ARTIKEL_SERIENNUMMER_MUSS_MIT_ZIFFERNTEIL_ENDEN,
								new Exception(
										"ziffernteil_laenge_von == 0 || ziffernteil_laenge_bis == 0"));

					}

					int iVon = new Integer(von.substring(von.length()
							- ziffernteil_laenge_von)).intValue();
					int iBis = new Integer(bis.substring(bis.length()
							- ziffernteil_laenge_bis)).intValue();
					int iPrefixLaenge = von.length() - ziffernteil_laenge_von;

					String preFixVon = von.substring(0, von.length()
							- ziffernteil_laenge_von);
					String preFixBis = bis.substring(0, bis.length()
							- ziffernteil_laenge_bis);
					if (!preFixVon.equals(preFixBis)) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_ARTIKEL_SERIENNUMMER_VON_BIS_PREFIX_UNGLEICH,
								new Exception("!preFixVon.equals(preFixBis)"));
					}

					if (iVon < iBis) {
						for (int z = iVon; z <= iBis; z++) {
							String nummer = z + "";

							StringBuffer s = new StringBuffer(von.substring(0,
									iPrefixLaenge));
							int iMitNullenFuellen = ziffernteil_laenge_von
									- nummer.length();
							for (int n = 0; n < iMitNullenFuellen; n++) {
								s.append("0");
							}

							s.append(z);
							snrs.add(s.toString());
						}
					} else {
						for (int z = iBis; z <= iVon; z++) {
							String nummer = z + "";

							StringBuffer s = new StringBuffer(von.substring(0,
									iPrefixLaenge));
							int iMitNullenFuellen = ziffernteil_laenge_von
									- nummer.length();
							for (int n = 0; n < iMitNullenFuellen; n++) {
								s.append("0");
							}

							s.append(z);
							snrs.add(s.toString());
						}

					}
				} else {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_ARTIKEL_SERIENNUMMER_VON_BIS_ZIFFERNTEIL_UNGLEICH,
							new Exception(
									"ziffernteil_laenge_von != ziffernteil_laenge_bis"));
				}

			} else {
				snrs.add(getrennt[i]);
			}

		}
		String[] ergebnis = new String[snrs.size()];
		for (int i = 0; i < snrs.size(); i++) {
			ergebnis[i] = (String) snrs.get(i);
		}

		if (bPruefeMenge == true) {
			if (new Integer(snrs.size()).doubleValue() != menge.doubleValue()) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_ARTIKEL_SERIENNUMMER_MENGE_UNGLEICH,
						new Exception(
								"new Integer(snrs.size()).doubleValue() != menge.doubleValue()"));

			}
		}
		return ergebnis;
	}

	/**
	 * Aus einem String[] einen String im Format 'aa,ab,ac' erzeugen
	 * 
	 * @param aStringI
	 *            das Array
	 * @return String der String
	 */
	public static String erzeugeStringAusStringArray(String[] aStringI) {
		String s = "";

		if (aStringI != null && aStringI.length > 0) {
			for (int i = 0; i < aStringI.length; i++) {
				s += aStringI[i];

				if (!(i == aStringI.length - 1)) {
					s += ",";
				}
			}
		}

		return s;
	}

	/**
	 * Einen bestimmten String aus einem String Array entfernen.
	 * 
	 * @param aString
	 *            das String Array
	 * @param sString
	 *            der zu entfernende String
	 * @return String[]
	 */
	public static String[] entferneStringAusStringArray(String[] aString,
			String sString) {
		ArrayList<String> alCopy = new ArrayList<String>();

		for (int i = 0; i < aString.length; i++) {
			if (!aString[i].equals(sString)) {
				alCopy.add(aString[i]);
			}
		}

		String[] aCopy = new String[alCopy.size()];

		for (int i = 0; i < alCopy.size(); i++) {
			aCopy[i] = (String) alCopy.get(i);
		}

		return aCopy;
	}

	public static String[] hinzufuegenStringZuStringArray(String[] aString,
			String sString) {
		String[] aCopy = new String[aString.length + 1];

		aCopy[aString.length + 1] = sString;

		return aCopy;
	}

	public static boolean enthaeltStringArrayString(String[] aString,
			String sString) {
		boolean bEnthalten = false;

		if (aString != null && aString.length > 0) {
			for (int i = 0; i < aString.length; i++) {
				if (aString[i].equals(sString)) {
					bEnthalten = true;
				}
			}
		}

		return bEnthalten;
	}

	public static int ermittleTageEinesZeitraumes(java.sql.Date tVon,
			java.sql.Date tBis) {
		int iTage = 0;

		tVon = cutDate(tVon);
		tBis = cutDate(tBis);

		if (tVon != null && tBis != null) {
			if (tVon.after(tBis)) {
				java.sql.Date tH = tVon;
				tVon = tBis;
				tBis = tH;
			}

			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(tVon.getTime());
			while (tBis.after(tVon)) {
				iTage++;
				c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1);
				tVon.setTime(c.getTimeInMillis());
			}
		}

		return iTage;
	}

	public static int ermittleTageEinesZeitraumes(java.sql.Timestamp tVon,
			java.sql.Date tBis) {
		int iTage = 0;

		if (tVon != null && tBis != null) {
			tVon = cutTimestamp(tVon);
			java.sql.Date dVon = new java.sql.Date(tVon.getTime());
			tBis = cutDate(tBis);

			if (tVon.after(tBis)) {
				java.sql.Date tH = dVon;
				dVon = tBis;
				tBis = tH;
			}

			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(tVon.getTime());
			while (tBis.after(tVon)) {
				iTage++;
				c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1);
				tVon.setTime(c.getTimeInMillis());
			}
		}

		return iTage;
	}

	/**
	 * Berechnet die Anzahl der Tage eines Monats
	 * 
	 * @param iJahr
	 *            Jahr
	 * @param iMonat
	 *            Monat
	 * @return Anzahl der Tage eines Monats, zB 31 f&uuml;r Oktober
	 */
	@SuppressWarnings("static-access")
	public static int ermittleAnzahlTageEinesMonats(Integer iJahr,
			Integer iMonat) {

		GregorianCalendar cal = new GregorianCalendar();

		cal.set(cal.DAY_OF_MONTH, 1);
		cal.set(cal.MONTH, iMonat.intValue());
		cal.set(cal.YEAR, iJahr.intValue());

		return cal.getActualMaximum(cal.DAY_OF_MONTH);
	}

	/**
	 * Rechnet eine Zeit (08:30) dezimal um: 8,5
	 * 
	 * @param time
	 *            Time
	 * @return Double
	 */
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

	/**
	 * Ein Image aus einem byte-Array (BLOB) erzeugen
	 * 
	 * @param imagebytes
	 *            byte[]
	 * @return BufferedImage
	 */
	public static BufferedImage byteArrayToImage(byte[] imagebytes) {
		try {
			if (imagebytes != null && (imagebytes.length > 0)) {
				BufferedImage im = ImageIO.read(new ByteArrayInputStream(
						imagebytes));
				return im;
			}
			return null;
		} catch (IOException e) {
			throw new IllegalArgumentException(e.toString());
		}
	}

	public static BufferedImage[] tiffToImageArray(byte[] tiffbytes) {
		BufferedImage[] images = null;
		ImageDecoder decoder = null;
		int numPages = 0;
		try {
			ByteArraySeekableStream ss = new ByteArraySeekableStream(tiffbytes);

			decoder = ImageCodec.createImageDecoder("tiff", ss, null);
			numPages = decoder.getNumPages();
			images = new BufferedImage[numPages];

			RenderedImage[] image = new RenderedImage[numPages];

			for (int i = 0; i < numPages; i++) {

				image[i] = decoder.decodeAsRenderedImage(i);
				WritableRaster wr = null;
				wr = image[i].copyData(wr);
				ColorModel colorModel = image[i].getColorModel();
				BufferedImage bi = new BufferedImage(colorModel, wr,
						colorModel.isAlphaPremultiplied(), null);
				images[i] = bi;

			}
		} catch (IOException ex) {
			throw new IllegalArgumentException(ex.toString());
		}

		return images;
	}

	public static boolean isAbbot(TheClientDto theClientDto) {
		boolean isLPAdmin = false;
		if (theClientDto != null) {
			String benutzername = theClientDto.getBenutzername();
			if (benutzername != null) {
				benutzername = benutzername.trim().substring(0,
						benutzername.indexOf("|"));
				isLPAdmin = benutzername.equalsIgnoreCase("abbot");
			}
		}
		return isLPAdmin;
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

	/**
	 * Ein byte-Array (BLOB) aus einem Image erzeugen
	 * 
	 * @param o
	 *            BufferedImage
	 * @return byte[]
	 */
	public static byte[] imageToByteArray(BufferedImage o) {
		if (o != null) {
			BufferedImage image = (BufferedImage) o;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				boolean converted = ImageIO.write(image, "jpeg", baos);
				if (!converted) {

				}
				byte[] b = baos.toByteArray();
				return b;
			} catch (IOException e) {
				throw new IllegalStateException(e.toString());
			} finally {
				closeIgnoreIOEx(baos);
			}
		}
		return new byte[0];
	}

	public static BigDecimal[] teileBetragAnteiligAuf(BigDecimal[] bdWerte,
			BigDecimal[] bdGewichtung, BigDecimal bdAufzuteilenderBetrag,
			int iNachkommastellen) throws EJBExceptionLP {
		// Arrays muessen gleich gross sein
		if (bdWerte.length != bdGewichtung.length) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"bdWerte.length != bdGewichtung.length"));
		}
		if (bdWerte.length <= 0) {
			// ???
			return new BigDecimal[0];
		}

		BigDecimal bdSumme = new BigDecimal(0);
		// Gesamtwert berechnen
		for (int i = 0; i < bdWerte.length; i++) {
			bdSumme = bdSumme.add(bdWerte[i].multiply(bdGewichtung[i]));
		}
		// je mehr nachkommastellen desto besser der faktor, aber desto
		// langsamer wirds auch
		// deswegen probier ichs mahl mit 10

		int nks = 10;

		// der aufzuteilende betrag durch die summe gibt einen
		// "aufschlagsfaktor"
		// josefanmartin:
		BigDecimal bdFaktor = null;
		if (!(bdSumme.compareTo(new BigDecimal(0.0000)) == 0)) {
			bdFaktor = bdAufzuteilenderBetrag.divide(bdSumme, nks,
					BigDecimal.ROUND_HALF_EVEN);
		} else {
			// nichts aufzuteilen, da alle Werte 0 sind
			bdFaktor = new BigDecimal(0);
		}

		// der aufzuteilende betrag durch die summe gibt einen
		// "aufschlagsfaktor"
		// BigDecimal bdFaktor = bdAufzuteilenderBetrag.divide(bdSumme, nks,
		// BigDecimal.ROUND_HALF_UP);
		BigDecimal[] bdResult = new BigDecimal[bdWerte.length];
		for (int i = 0; i < bdResult.length; i++) {
			bdResult[i] = bdWerte[i].multiply(bdFaktor);
			bdResult[i] = rundeKaufmaennisch(bdResult[i], iNachkommastellen);
		}
		// durch rundungsfehler kann es zu restbetraegen kommen
		// "clone"
		BigDecimal bdRestbetrag = bdAufzuteilenderBetrag;
		for (int i = 0; i < bdResult.length; i++) {
			bdRestbetrag = bdRestbetrag.subtract(bdResult[i]
					.multiply(bdGewichtung[i]));
		}

		/**
		 * @todo den restbetrag aufteilen PJ 4318
		 */
		/*
		 * if(!(bdRestbetrag.floatValue()==0.0)) { // dann muss ich das noch wo
		 * dazurechnen // und zwar auf die wertmaessig groesste position int
		 * iMaxIndex=0; BigDecimal bdMax=new BigDecimal(0); for (int i = 0; i <
		 * bdResult.length; i++) { BigDecimal
		 * bdWert=bdWerte[i].multiply(bdGewichtung[i]);
		 * if(bdWert.compareTo(bdMax)>=1) { bdMax=bdWert; iMaxIndex=i; } } //
		 * jetzt hab ich den groessten wert an der stelle i
		 * bdResult[i]=bdResult[i].add
		 * 
		 * 
		 * }
		 */
		// BigDecimal []bd
		return bdResult;
	}

	public static String formatMandantAdresse(MandantDto mandantDto) {
		String s = "";
		s = s + mandantDto.getPartnerDto().getCName1nachnamefirmazeile1();
		s = s + ", ";

		// UW 03.03.06 Das LKZ- wird jetzt in PartnerDto.formatAdresse()
		// befuellt
		// if (mandantDto.getPartnerDto().getLandplzortDto() != null &&
		// mandantDto.getPartnerDto().getLandplzortDto().getLandDto() != null) {
		// s = s +
		// mandantDto.getPartnerDto().getLandplzortDto().getLandDto().getCLkz()
		// + "-";
		// }
		s = s + mandantDto.getPartnerDto().formatAdresse();

		// PJ 15376
		s += "\n";
		s += mandantDto.getPartnerDto().getCName1nachnamefirmazeile1();

		LandplzortDto lpo = mandantDto.getPartnerDto().getLandplzortDto();
		if (lpo != null) {
			s += ",";
			if (mandantDto.getPartnerDto().getCStrasse() != null) {
				s += " " + mandantDto.getPartnerDto().getCStrasse();
			}

			if (lpo.getLandDto() != null && lpo.getLandDto().getCLkz() != null) {
				s += ", " + lpo.getLandDto().getCLkz().trim() + " -";
			}
			if (lpo.getCPlz() != null) {
				s += " " + lpo.getCPlz().trim();
			}
			if (lpo.getOrtDto().getCName() != null) {
				s += " " + lpo.getOrtDto().getCName().trim();
			}

		}
		return s;
	}

	/**
	 * Pruefen, ob ein String im Format x,x eine bestimmte Anzahl von x
	 * entahelt.
	 * 
	 * @param stringI
	 *            das Array
	 * @param iAnzahlI
	 *            die Laenge
	 * @return boolean true, wenn das Array die gewuenschte Laenge hat
	 */
	public static boolean pruefeLaengeStringArray(String stringI, int iAnzahlI) {
		boolean iGewuenschteLaenge = false;

		int iAnzahl = 0;

		if (stringI != null && stringI.length() != 0) {
			iAnzahl = Helper.erzeugeStringArrayAusString(stringI).length;
		}

		if (iAnzahl == iAnzahlI) {
			iGewuenschteLaenge = true;
		}

		return iGewuenschteLaenge;
	}

	/**
	 * Fuegt die Seiten eines Reports zu einem vorhandenen Report hinzu
	 * 
	 * @param vorhandenerReport
	 *            JasperPrint
	 * @param hinzuzufuegenderReport
	 *            JasperPrint
	 * @return JasperPrint
	 */
	public static JasperPrint addReport2Report(JasperPrint vorhandenerReport,
			JasperPrint hinzuzufuegenderReport) {

		if (hinzuzufuegenderReport.getPages() != null) {
			Iterator<?> iterator = hinzuzufuegenderReport.getPages().iterator();
			while (iterator.hasNext()) {
				JRPrintPage zeitdaten = (JRPrintPage) iterator.next();
				vorhandenerReport.addPage(zeitdaten);
			}
		}

		return vorhandenerReport;
	}

	public static long verfuegbarkeitvonStundenEinesTagesNachLong(
			BigDecimal bdStunden) {
		return bdStunden.multiply(DAY_IN_MS).longValue();
		// return (long) (3600000 * bdStunden.doubleValue());
	}

	public static BigDecimal verfuegbarkeitvonLongNachStundenEinesTages(long l) {
		BigDecimal d = new BigDecimal(l).setScale(2);
		d = d.divide(DAY_IN_MS, BigDecimal.ROUND_HALF_EVEN);
		return d;

		// double d = ((double) l) / 1000 / 60 / 60;
		// return new BigDecimal(d);
	}

	/**
	 * Fuegt die Seiten eines Reports zu einem vorhandenen Report hinzu
	 * 
	 * @param vorhandenerReport
	 *            JasperPrint
	 * @param hinzuzufuegenderReport
	 *            JasperPrint
	 * @return JasperPrint
	 */
	public static JasperPrintLP addReport2Report(
			JasperPrintLP vorhandenerReport, JasperPrint hinzuzufuegenderReport) {
		if (hinzuzufuegenderReport.getPages() != null) {
			JasperPrint print = addReport2Report(vorhandenerReport.getPrint(),
					hinzuzufuegenderReport);
			vorhandenerReport.setPrint(print);
		}
		return vorhandenerReport;
	}

	public static java.sql.Timestamp berechneOstersonntag(int jahr) {

		int a, b, c, d, e, p, q, r, x, y, tag, monat;
		int ptag, pmonat;

		// Es geht um die Berechnung der Groeszen d und e
		// Dazu braucht man die 9 Hilfsgroeszen a, b, c, p, n, q, r, x, y !!

		p = jahr / 100;

		q = p / 3;
		r = p / 4;

		x = (15 + p - q - r) % 30;
		y = (4 + p - r) % 7;

		a = jahr % 19;
		b = jahr % 4;
		c = jahr % 7;

		d = (19 * a + x) % 30;
		e = (2 * b + 4 * c + 6 * d + y) % 7;

		if (d == 29 && e == 6) {
			// => Ostern am 19.April
			tag = 19;
			monat = 4;
		} else if (d == 28 && e == 6) {
			// => Ostern am 18.April
			tag = 18;
			monat = 4;
		} else if (22 + d + e < 32) // ansonsten gilt
		{
			// => Ostern am (22+d+e).Maerz
			tag = 22 + d + e;
			monat = 3;
		} else {
			// => Ostern am (d+e-9).April
			tag = d + e - 9;
			monat = 4;
		}

		System.out.print("Ostern ist/war am ");

		if (tag < 10)
			System.out.println("0" + tag + "." + "0" + monat + "." + jahr);
		else
			System.out.println("" + tag + "." + "0" + monat + "." + jahr);

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, jahr);
		cal.set(Calendar.MONTH, monat - 1);
		cal.set(Calendar.DAY_OF_MONTH, tag);

		return Helper.cutTimestamp(new Timestamp(cal.getTimeInMillis()));

	}

	public static int booleanArray2int(boolean[] b) {
		int result = 0;
		for (int i = 0; i < b.length; i++) {
			if (b[i]) {
				result += Math.pow(2, i);
			}
		}
		return result;
	}

	public static Double berechneRabattsatzMehrererRabatte(Double dRabatt,
			Double dZusatzrabatt, Double dNachlass) {

		double dRabattsatz = 100.0;
		if (dRabatt != null) {
			dRabattsatz = dRabattsatz - dRabatt;
		}
		if (dZusatzrabatt != null) {
			double dFaktor2 = 100.0 - dZusatzrabatt;
			dRabattsatz = dRabattsatz * dFaktor2 / 100.0;
		}
		if (dNachlass != null) {
			double dFaktor3 = 100.0 - dNachlass;
			dRabattsatz = dRabattsatz * dFaktor3 / 100.0;
		}

		return new Double(100.0 - dRabattsatz);
	}

	public static BigDecimal berechneGesamtzeitInStunden(Long lRuestzeit,
			Long lStueckzeit, BigDecimal bdLosgroesse,
			Integer iErfassungsfaktor, Integer iAufspannung) {

		if (iAufspannung != null && iAufspannung >= 1) {
			lStueckzeit = lStueckzeit / iAufspannung;
		}

		if (iErfassungsfaktor != null && iErfassungsfaktor != 0
				&& iErfassungsfaktor != 1) {
			double d = (bdLosgroesse.doubleValue() / iErfassungsfaktor)
					* lStueckzeit;
			lStueckzeit = (long) d;
		}

		double lGesamtzeit = lRuestzeit.doubleValue()
				+ lStueckzeit.doubleValue() * bdLosgroesse.doubleValue();
		BigDecimal bdGesamtzeit = new BigDecimal(lGesamtzeit);
		return bdGesamtzeit.divide(new BigDecimal(60 * 60 * 1000),
				FertigungFac.NACHKOMMASTELLEN_ARBEITSPLAN,
				BigDecimal.ROUND_HALF_EVEN);
	}

	public static String strippHTML(String sI) {
		String result = "";
		StringTokenizer st = new StringTokenizer(sI, "<>", true);
		try {
			while (true) {
				String temp = st.nextToken();
				if (temp.equals("<")) {
					st.nextToken();
					st.nextToken();
					temp = "";
				}
				result += temp;
			}
		} catch (NoSuchElementException e) {
			// nothing here ;-)
		}
		result = result.replaceAll("&amp;", "&");
		result = result.replaceAll("&quot;", "\"");
		result = result.replaceAll("&lt;", "<");
		result = result.replaceAll("&gt;", ">");

		return result;
	}

	// Wrapping
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Wraps a single line of text, identifying words by <code>' '</code>.
	 * </p>
	 * 
	 * <p>
	 * New lines will be separated by the system property line separator. Very
	 * long words, such as URLs will <i>not</i> be wrapped.
	 * </p>
	 * 
	 * <p>
	 * Leading spaces on a new line are stripped. Trailing spaces are not
	 * stripped.
	 * </p>
	 * 
	 * <pre>
	 * WordUtils.wrap(null, *) = null
	 * WordUtils.wrap(&quot;&quot;, *) = &quot;&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to be word wrapped, may be null
	 * @param wrapLength
	 *            the column to wrap the words at, less than 1 is treated as 1
	 * @return a line with newlines inserted, <code>null</code> if null input
	 */
	public static String wrap(String str, int wrapLength) {
		return wrap(str, wrapLength, null, false);
	}

	/**
	 * <p>
	 * Wraps a single line of text, identifying words by <code>' '</code>.
	 * </p>
	 * 
	 * <p>
	 * Leading spaces on a new line are stripped. Trailing spaces are not
	 * stripped.
	 * </p>
	 * 
	 * <pre>
	 * WordUtils.wrap(null, *, *, *) = null
	 * WordUtils.wrap(&quot;&quot;, *, *, *) = &quot;&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to be word wrapped, may be null
	 * @param wrapLength
	 *            the column to wrap the words at, less than 1 is treated as 1
	 * @param newLineStr
	 *            the string to insert for a new line, <code>null</code> uses
	 *            the system property line separator
	 * @param wrapLongWords
	 *            true if long words (such as URLs) should be wrapped
	 * @return a line with newlines inserted, <code>null</code> if null input
	 */
	public static String wrap(String str, int wrapLength, String newLineStr,
			boolean wrapLongWords) {
		if (str == null) {
			return null;
		}
		if (newLineStr == null) {
			newLineStr = LINE_SEPARATOR;
		}
		if (wrapLength < 1) {
			wrapLength = 1;
		}
		int inputLineLength = str.length();
		int offset = 0;
		StringBuffer wrappedLine = new StringBuffer(inputLineLength + 32);

		while ((inputLineLength - offset) > wrapLength) {
			if (str.charAt(offset) == ' ') {
				offset++;
				continue;
			}
			int spaceToWrapAt = str.lastIndexOf(' ', wrapLength + offset);

			if (spaceToWrapAt >= offset) {
				// normal case
				wrappedLine.append(str.substring(offset, spaceToWrapAt));
				wrappedLine.append(newLineStr);
				offset = spaceToWrapAt + 1;

			} else {
				// really long word or URL
				if (wrapLongWords) {
					// wrap really long word one line at a time
					wrappedLine.append(str.substring(offset, wrapLength
							+ offset));
					wrappedLine.append(newLineStr);
					offset += wrapLength;
				} else {
					// do not wrap really long word, just extend beyond limit
					spaceToWrapAt = str.indexOf(' ', wrapLength + offset);
					if (spaceToWrapAt >= 0) {
						wrappedLine
								.append(str.substring(offset, spaceToWrapAt));
						wrappedLine.append(newLineStr);
						offset = spaceToWrapAt + 1;
					} else {
						wrappedLine.append(str.substring(offset));
						offset = inputLineLength;
					}
				}
			}
		}

		// Whatever is left in line is short enough to just pass through
		wrappedLine.append(str.substring(offset));

		return wrappedLine.toString();
	}

	// Returns the contents of the file in a byte array.
	public static byte[] getBytesFromFile(File file) throws IOException {

		FileInputStream stream = new FileInputStream(file);
		int size = stream.available();
		byte[] bytes = new byte[size];
		stream.read(bytes);
		stream.close();
		return bytes;
	}

	public static String formatIBAN(String iban) {

		if (iban == null) {
			return null;
		}

		iban = iban.replaceAll(" ", "");

		String ibanFormatted = "";

		int stelle = 0;
		int iletzte4erStelle = 0;
		for (int i = 0; i < iban.length(); i++) {

			if (stelle > 0 && stelle % 4 == 0) {
				iletzte4erStelle = stelle;
				ibanFormatted += iban.substring(stelle - 4, stelle) + " ";

			}
			if (stelle == iban.length() - 1) {
				ibanFormatted += iban
						.substring(iletzte4erStelle, iban.length());
				break;
			}

			stelle++;
		}

		return ibanFormatted;
	}

	/**
	 * Berechnet Menge inklusive Verschnitt,Wichtig: ERGEBNIS IST NICHT GERUNDET
	 * 
	 * @param bdAusgangsmenge
	 *            BigDecimal
	 * @param dVerschnittfaktor
	 *            Double
	 * @param dVerschnittbasis
	 *            Double
	 * @param losgroesse
	 *            BigDecimal
	 * @return BigDecimal
	 */
	public static BigDecimal berechneMengeInklusiveVerschnitt(
			BigDecimal bdAusgangsmenge, Double dVerschnittfaktor,
			Double dVerschnittbasis, BigDecimal losgroesse,
			Double dFertigungs_vpe) {

		BigDecimal bdMengeInklusiveVerschnitt = null;
		if (bdAusgangsmenge == null) {
			return null;
		}
		if (bdAusgangsmenge.doubleValue() == 0) {
			return new BigDecimal(0);
		} else if (dVerschnittfaktor == null) {
			dVerschnittfaktor = new Double(0);
		}

		if (dVerschnittbasis == null
				|| (dVerschnittbasis != null && dVerschnittbasis.doubleValue() == 0)) {
			bdMengeInklusiveVerschnitt = bdAusgangsmenge.add(bdAusgangsmenge
					.multiply(new BigDecimal(
							dVerschnittfaktor.doubleValue() / 100)));
		} else {
			if (losgroesse == null) {
				losgroesse = new BigDecimal(1);
			}
			if (losgroesse.doubleValue() == 0) {
				losgroesse = new BigDecimal(1);
			}

			double d = dVerschnittbasis.doubleValue()
					/ bdAusgangsmenge.doubleValue();
			int i = (int) d;

			double dAnzahl = 0;

			if (i != 0) {
				dAnzahl = losgroesse.doubleValue() / i;

				if (dAnzahl % 1 > 0) {
					dAnzahl = dAnzahl + 1;
				}
				dAnzahl = (int) dAnzahl;
			}
			bdMengeInklusiveVerschnitt = new BigDecimal(dAnzahl
					/ losgroesse.doubleValue() * dVerschnittbasis.doubleValue());
		}

		// PJ18585
		if (dFertigungs_vpe != null && dFertigungs_vpe.doubleValue() != 0) {

			
			BigDecimal bdFertigungsmenge= new BigDecimal( bdMengeInklusiveVerschnitt.doubleValue()*losgroesse.doubleValue());
			
			if (bdFertigungsmenge.doubleValue() % dFertigungs_vpe > 0) {
				
				
				
				double dRest = bdFertigungsmenge.doubleValue()
						% dFertigungs_vpe;
				bdMengeInklusiveVerschnitt = (bdFertigungsmenge
						.add(new BigDecimal(dFertigungs_vpe - dRest))).divide(losgroesse, 12, BigDecimal.ROUND_HALF_EVEN);
			}

		}

		return bdMengeInklusiveVerschnitt;
	}

	public static String formatString(String sI) {
		return sI != null ? sI.trim() : "";
	}

	/**
	 * prueft ob 2 Timestamps das gleiche Datum haben
	 * 
	 * @param t1
	 *            BewegungsvorschauDto
	 * @param t2
	 *            BewegungsvorschauDto
	 * @return boolean
	 */
	public static boolean vergleicheTimestampsInBezugAufDatum(Timestamp t1,
			Timestamp t2) {
		boolean datechanged = false;
		java.sql.Date date1 = null;
		java.sql.Date date2 = null;
		try {
			date1 = Helper.extractDate(t1);
			date2 = Helper.extractDate(t2);
		} catch (Exception ex) {
		}
		if (!(date1.equals(date2))) {
			datechanged = Helper
					.datumGueltigInbezugAufUntergrenze(date1, date2);
		}
		return datechanged;
	}

	/**
	 * Den Kehrwert eines BigDecimal's berechnen. Hier wird NICHT gerundet.
	 * 
	 * @param bdI
	 *            Zu invertierende Zahl
	 * @return BigDecimal der Reziprokwert von bdI
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	@Deprecated
	public static BigDecimal getKehrwert(BigDecimal bdI) throws EJBExceptionLP {
		if (bdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("bdI == null"));
		}
		return new BigDecimal(1).divide(bdI, 100, BigDecimal.ROUND_HALF_EVEN);
	}

	public static boolean istStringNumerisch(String wert) {
		boolean istNumerisch = true;
		try {
			int zahl = Integer.parseInt(wert);
		} catch (NumberFormatException e) {
			istNumerisch = false;
		}
		return istNumerisch;
	}

	public static boolean istWertVomTyp(String wert, String typ) {
		if (typ != null && wert != null) {

			if (typ.equals("java.lang.Integer")) {
				try {
					Integer.parseInt(wert);
					return true;
				} catch (NumberFormatException ex) {
					return false;
				}
			} else if (typ.equals("java.lang.String")) {
				return true;
			} else if (typ.equals("java.lang.Boolean")) {
				try {
					int i = Integer.parseInt(wert);

					if (i == 0 || i == 1) {
						return true;
					} else {
						return false;
					}
				} catch (NumberFormatException ex) {
					return false;
				}
			} else if (typ.equals("java.lang.Double")) {
				try {
					// Damit das Komma auch geht
					if (wert.indexOf(",") >= 0) {
						wert = wert.replaceAll(",", ".");
					}
					Double.parseDouble(wert);
					return true;
				} catch (NumberFormatException ex1) {
					return false;
				}

			} else if (typ.equals("java.math.BigDecimal")) {
				try {
					// Damit das Komma auch geht
					if (wert.indexOf(",") >= 0) {
						wert = wert.replaceAll(",", ".");
					}
					Double.parseDouble(wert);
					return true;
				} catch (NumberFormatException ex1) {
					return false;
				}
			} else {
				ArrayList<String> al = new ArrayList<String>();
				al.add(typ);
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_UNBEKANNTER_DATENTYP,
						new Exception("UNBEKANNTER DATENTYP"));

			}
		}
		return false;

	}

	public static String[] zerlegeChargennummerMHD(String chargenNr) {
		String[] s = null;
		if (chargenNr != null) {
			if (chargenNr.length() > 7) {
				String datum = chargenNr.substring(0, 8);
				String zusatz = chargenNr.substring(8);

				s = new String[2];
				s[0] = zusatz;
				s[1] = datum;

			} else {
				s = new String[1];
				s[0] = chargenNr;
			}
		}
		return s;
	}

	public static boolean validateEmailadresse(String sEmail)
			throws EJBExceptionLP {
		boolean bValid = false;
		if (sEmail != null) {
			int iIndexAt = sEmail.indexOf("@");
			int iIndexLetzterPunkt = sEmail.lastIndexOf(".");
			if (iIndexAt > 0 && // ein @ muss drin sein
					iIndexLetzterPunkt > 0 && // ein punkt muss auch drin sein
					iIndexAt < iIndexLetzterPunkt) { // und das @ muss vor dem
				// letzten punkt kommen
				bValid = true;
			}
		}
		return bValid;
	}

	public static boolean validateFaxnummer(String sFaxnummer)
			throws EJBExceptionLP {
		boolean bValid = false;
		if (sFaxnummer != null) {
			bValid = true;
			char[] cArray = sFaxnummer.toCharArray();
			for (int i = 0; i < cArray.length; i++) {
				/**
				 * @todo MB->MB quick & dirty -> mit einem regex loesen! PJ 4319
				 */
				// MB 03.07.06: sind folgende zeichen zulaessig: (keine
				// sonderzeichen mehr)
				if (cArray[i] != '0' && cArray[i] != '1' && cArray[i] != '2'
						&& cArray[i] != '3' && cArray[i] != '4'
						&& cArray[i] != '5' && cArray[i] != '6'
						&& cArray[i] != '7' && cArray[i] != '8'
						&& cArray[i] != '9' && cArray[i] != ' ') {
					bValid = false;
				}
			}
		}
		return bValid;
	}

	public static String befreieFaxnummerVonSonderzeichen(String sFaxnummer)
			throws EJBExceptionLP {
		StringBuffer sKorrigierteFaxnummer = new StringBuffer();
		if (sFaxnummer != null) {
			char[] cArray = sFaxnummer.toCharArray();
			for (int i = 0; i < cArray.length; i++) {
				/**
				 * @todo MB->MB quick & dirty -> mit einem regex loesen! PJ 4319
				 */
				// MB 03.07.06: sind folgende zeichen zulaessig: (keine
				// sonderzeichen mehr)
				if (cArray[i] != '0' && cArray[i] != '1' && cArray[i] != '2'
						&& cArray[i] != '3' && cArray[i] != '4'
						&& cArray[i] != '5' && cArray[i] != '6'
						&& cArray[i] != '7' && cArray[i] != '8'
						&& cArray[i] != '9' && cArray[i] != ' ') {
					sKorrigierteFaxnummer.append(" ");
				} else {
					sKorrigierteFaxnummer.append(cArray[i]);
				}
			}
		}
		return sKorrigierteFaxnummer.toString();
	}

	public static String befreieNummerVonSonderzeichen(String sNummer)
			throws EJBExceptionLP {
		StringBuffer sKorrigierteNummer = new StringBuffer();
		if (sNummer != null) {
			char[] cArray = sNummer.toCharArray();
			for (int i = 0; i < cArray.length; i++) {
				/**
				 * @todo MB->MB quick & dirty -> mit einem regex loesen! PJ 4319
				 */
				// MB 03.07.06: sind folgende zeichen zulaessig: (keine
				// sonderzeichen mehr)
				if (cArray[i] != '0' && cArray[i] != '1' && cArray[i] != '2'
						&& cArray[i] != '3' && cArray[i] != '4'
						&& cArray[i] != '5' && cArray[i] != '6'
						&& cArray[i] != '7' && cArray[i] != '8'
						&& cArray[i] != '9' && cArray[i] != ' ') {
					// do nothing
				} else {
					sKorrigierteNummer.append(cArray[i]);
				}
			}
		}
		return sKorrigierteNummer.toString();
	}

	public static String befreieNummerVonSonderzeichenInklisiveLeerzeichen(
			String sNummer) throws EJBExceptionLP {
		StringBuffer sKorrigierteNummer = new StringBuffer();
		if (sNummer != null) {
			char[] cArray = sNummer.toCharArray();
			for (int i = 0; i < cArray.length; i++) {
				/**
				 * @todo MB->MB quick & dirty -> mit einem regex loesen! PJ 4319
				 */
				// MB 03.07.06: sind folgende zeichen zulaessig: (keine
				// sonderzeichen mehr)
				if (cArray[i] != '0' && cArray[i] != '1' && cArray[i] != '2'
						&& cArray[i] != '3' && cArray[i] != '4'
						&& cArray[i] != '5' && cArray[i] != '6'
						&& cArray[i] != '7' && cArray[i] != '8'
						&& cArray[i] != '9') {
					// do nothing
				} else {
					sKorrigierteNummer.append(cArray[i]);
				}
			}
		}
		return sKorrigierteNummer.toString();
	}

	static public String xML2String(Document docI) throws IOException {
		Writer w = new StringWriter();

		// XERCES 1 or 2 additionnal classes.
		OutputFormat of = new OutputFormat("XML", "ISO-8859-1", true);
		of.setIndent(1);
		of.setIndenting(true);

		XMLSerializer serializer = new XMLSerializer(w, of);
		// As a DOM Serializer
		serializer.asDOMSerializer();
		serializer.serialize(docI.getDocumentElement());

		return w.toString();
	}

	static final public void addHVFeature(Node nodeFeaturesI, Document docI,
			String sDescrI, Object sValueI) throws DOMException {

		if (nodeFeaturesI == null) {
			throw new IllegalArgumentException("nodeFeaturesI == null");
		}
		if (docI == null) {
			throw new IllegalArgumentException("docI == null");
		}
		if (sDescrI == null) {
			throw new IllegalArgumentException("sDescrI == null");
		}

		Node nodeFeature = null;
		if (sValueI != null && !sValueI.equals("")) {
			nodeFeature = docI.createElement(SystemFac.SCHEMA_OF_FEATURE);
			Node node = docI
					.createElement(SystemFac.SCHEMA_OF_FEATURE_DESCRIPTION);
			node.appendChild(docI.createTextNode(sDescrI));
			nodeFeature.appendChild(node);
			node = docI.createElement(SystemFac.SCHEMA_OF_FEATURE_VALUE);
			node.appendChild(docI.createTextNode(sValueI.toString()));
			nodeFeature.appendChild(node);
			nodeFeaturesI.appendChild(nodeFeature);
		}
	}

	static final public Throwable getRootCause(Exception ex) {
		Throwable eFoundException = ex;
		while (eFoundException.getCause() != null) {
			eFoundException = eFoundException.getCause();
		}
		return eFoundException;
	}

	static final public void addOFElement(Node nodeWhere2AddI, Document docI,
			Object oValue, String sElementI) throws DOMException {

		if (nodeWhere2AddI == null) {
			throw new IllegalArgumentException("nodeWhere2AddI == null");
		}
		if (docI == null) {
			throw new IllegalArgumentException("docI == null");
		}
		if (sElementI == null) {
			throw new IllegalArgumentException("sElementI == null");
		}

		if (oValue != null) {
			Element elem = docI.createElement(sElementI);
			elem.appendChild(docI.createTextNode(oValue.toString()));
			nodeWhere2AddI.appendChild(elem);
		}
	}

	/**
	 * Echtes Klonen von Original.
	 * 
	 * @param sOriginal
	 *            String
	 * @return String
	 */
	public static String cloneString(String sOriginal) {
		if (sOriginal == null) {
			// null bleibt null
			return null;
		} else {
			// neue Instanz erzeugen
			return new String(sOriginal);
		}
	}

	public static List cloneList(List sOriginal) {
		if (sOriginal == null) {
			// null bleibt null
			return null;
		} else {
			// neue Instanz erzeugen
			return new ArrayList(sOriginal);
		}
	}

	/**
	 * Echtes Klonen von BigDecimals.
	 * 
	 * @param bdOriginal
	 *            Original
	 * @return String
	 */
	public static BigDecimal cloneBigDecimal(BigDecimal bdOriginal) {
		if (bdOriginal == null) {
			// null bleibt null
			return null;
		} else {
			// neue Instanz erzeugen
			return bdOriginal.setScale(bdOriginal.scale());
		}
	}

	/**
	 * Echtes Klonen von Integer.
	 * 
	 * @param iOriginal
	 *            Original
	 * @return String
	 */
	public static Integer cloneInteger(Integer iOriginal) {
		if (iOriginal == null) {
			// null bleibt null
			return null;
		} else {
			// neue Instanz erzeugen
			return new Integer(iOriginal.intValue());
		}
	}

	/**
	 * Echtes Klonen von Short.
	 * 
	 * @param sOriginal
	 *            String
	 * @return String
	 */
	public static Short cloneShort(Short sOriginal) {
		if (sOriginal == null) {
			// null bleibt null
			return null;
		} else {
			// neue Instanz erzeugen
			return new Short(sOriginal.shortValue());
		}
	}

	/**
	 * Echtes Klonen von Double.
	 * 
	 * @param dOriginal
	 *            String
	 * @return String
	 */
	public static Double cloneDouble(Double dOriginal) {
		if (dOriginal == null) {
			// null bleibt null
			return null;
		} else {
			// neue Instanz erzeugen
			return new Double(dOriginal.doubleValue());
		}
	}

	/**
	 * Echtes Klonen von Float.
	 * 
	 * @param fOriginal
	 *            String
	 * @return String
	 */
	public static Float cloneFloat(Float fOriginal) {
		if (fOriginal == null) {
			// null bleibt null
			return null;
		} else {
			// neue Instanz erzeugen
			return new Float(fOriginal.floatValue());
		}
	}

	/**
	 * puefen, ob eine Warenverkehrsnummer das richtige Format hat. zulaessig
	 * sind 4, 6, oder 8 stellige ziffernfolgen: 0000, 0000 00, 0000 00 00
	 * 
	 * @param cWarenverkehrsnummer
	 *            String
	 * @return boolean
	 */
	public static boolean checkWarenverkehrsnummer(String cWarenverkehrsnummer) {
		boolean bMatch = false;
		if (cWarenverkehrsnummer != null) {
			String s4Ziffern = "\\d{4}";
			String s2Ziffern = "\\d{2}";
			String sWVK6Stellig = s4Ziffern + " " + s2Ziffern;
			String sWVK8Stellig = sWVK6Stellig + " " + s2Ziffern;
			Pattern p = Pattern.compile(s4Ziffern + "|" + sWVK6Stellig + "|"
					+ sWVK8Stellig);
			Matcher m = p.matcher(cWarenverkehrsnummer);
			bMatch = m.matches();
		}
		return bMatch;
	}

	public static String getPCName() throws UnknownHostException {
		return java.net.InetAddress.getLocalHost().getHostName();
	}

	public static String formatSimpleTextForJasper(Object o) {
		return o == null ? null : formatSimpleTextForJasperImpl(o.toString());
	}

	protected static String formatSimpleTextForJasperImpl(String text) {
		String parsed = text.replaceAll("&", "&amp;");
		parsed = parsed.replaceAll("\"", "&quot;");
		parsed = parsed.replaceAll("<", "&lt;");
		parsed = parsed.replaceAll(">", "&gt;");
		return parsed;
	}

	public static String removeStyles(String text) {

		if (text == null)
			return null;
		if (text.isEmpty())
			return text;

		return text.replaceAll("<style(.*?)>|</style(.*?)>", "");
	}

	public static Object formatStyledTextForJasper(Object o) {
		return o == null ? null : formatStyledTextForJasperImpl(o.toString());
	}

	protected static String formatStyledTextForJasperImpl(String text) {
		// SP 898; '&' ist ein Escape-char in html/xml darum ersetzen
		text = text.replaceAll("&(?![#a-zA-Z0-9]+;)", "&#38;");

		// wenn keine styles dann text zurueckgeben
		if (text.indexOf("<style") == -1) {
			return text;
		}

		String sText = text;
		String result = "";
		String aktstyle = "";
		String s = "";

		while (sText.length() > 0) {
			int iIndexOfFirstStyleBeginTag = sText.indexOf("<style");
			int iIndexOfFirstStyleEndTag = sText.indexOf("</style>",
					iIndexOfFirstStyleBeginTag);
			// alle Zeichen vor Style Begin tag direkt uebernehmen
			// nur Zeilenumbrueche nach Begin des <style Tags beachten
			if (iIndexOfFirstStyleBeginTag == -1) {
				result += sText;
				return result;
			} else {
				result += sText.substring(0, iIndexOfFirstStyleBeginTag);
			}
			aktstyle = "";
			s = sText.substring(iIndexOfFirstStyleBeginTag,
					iIndexOfFirstStyleEndTag + 8);
			sText = sText.substring(iIndexOfFirstStyleEndTag + 8);
			if (s.indexOf("\n") != -1) {
				// styledtext mit zeilenumbruch, hier muessen die \n ausserhalb
				// der Formatierung sein
				while (s.length() > 0) {
					int i = s.indexOf("\n");
					if (i == -1) {
						result += s;
						s = "";
					} else {
						String temp = s.substring(0, i);
						s = s.substring(i);
						result += temp;
						// endstyle nur anhaengen wenn nicht schon vorhanden
						if ((result.length() >= 8)
								&& (result.substring(result.length() - 8)
										.compareTo("</style>") == 0)) {
						} else {
							result += "</style>";
						}
						i = temp.indexOf("<style");
						if (i == -1) {
							// aktstyle = "";
						} else {
							aktstyle = temp.substring(i,
									temp.indexOf(">", i) + 1);
						}
						while (s.length() > 0
								&& s.substring(0, 1).compareTo("\n") == 0) {
							result += "\n";
							s = s.substring(1);
						}
						if (s.length() > 0) {
							if (s.substring(0, 8).compareTo("</style>") != 0) {
								result += aktstyle;
							} else {
								s = s.substring(8);
							}
						}
					}
				}
			} else {
				result += s;
			}
		}
		return result;
	}

	public static StringBuffer getMD5Hash(String in) {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
		md5.reset();
		md5.update(in.getBytes());
		byte[] result = md5.digest();

		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < result.length; i++) {
			hexString.append(Integer.toHexString(0xFF & result[i]));
		}
		return hexString;
	}

	// /**
	// * @param ohneEinheiten true wenn Prefixe ohne Einheiten geparst werden
	// sollen.
	// * @param units die Einheiten mit Beistrich getrennt (F, H, J)
	// * @param bez die Strings, welche durchsucht werden sollen
	// * @return die geparste Zahl als BigDecimalSI
	// */
	// public static BigDecimalSI berechneSiWertAusBezeichnung(boolean
	// ohneEinheiten, String units, String... bez) {
	// // PJ18155
	// StringBuffer sb = new StringBuffer();
	// for(String s : bez) {
	// sb.append(s == null ? "" : s);
	// sb.append(" ");
	// }
	//
	// String[] unitsArray = units.split(" *, *");
	// ISiPrefixParser parser = new DefaultSiPrefixParser(!ohneEinheiten,
	// unitsArray);
	// return parser.parseFirst(sb.toString());
	// }

	public static char[] getMD5Hash(char[] in) {
		return getMD5Hash(new String(in)).toString().toCharArray();
	}

	public static String getFullUsername(String in) {
		try {
			return in + LogonFac.USERNAMEDELIMITER
					+ java.net.InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			return in;
		}
	}

	/**
	 * Helper f&uuml;r KDC100 Konvertiert KDC100 Timestamp auf
	 * java.sql.Timestamp
	 * 
	 * @param in
	 *            short[4]
	 * @return Datum/Zeit als java.sql.Timestamp
	 */
	public static Timestamp getKDCTStoTimestamp(short[] in) {
		Calendar c = getKDCTStoCal(in);
		Timestamp ts = new java.sql.Timestamp(c.getTimeInMillis());
		return ts;
	}

	/**
	 * Helper f&uuml;r KDC100 Konvertiert KDC100 Timestamp auf String
	 * 
	 * @param in
	 *            short[4]
	 * @return Datum/Zeit als String
	 */
	public static String getKDCTStoString(short[] in) {
		return getKDCTStoCal(in).getTime().toString();
	}

	/**
	 * Helper f&uuml;r KDC100 Konvertiert KDC100 Timestamp auf Calendar
	 * 
	 * @param in
	 *            short[4]
	 * @return Datum/Zeit als Calendar
	 */
	public static Calendar getKDCTStoCal(short[] in) {
		Calendar c = Calendar.getInstance();
		c.set((in[0] >> 2) + 2000, ((in[0] & 3) << 2) + (in[1] >> 6) - 1,
				(in[1] & 63) >> 1, (in[1] & 1) == 1 ? 12 + (in[2] >> 4)
						: in[2] >> 4, ((in[2] & 15) << 2) + (in[3] >> 6),
				in[3] & 63);
		return c;
	}

	/**
	 * Helper f&uuml;r KDC100 Konvertiert Calendar auf KDC100 Timestamp als
	 * short[4]
	 * 
	 * @param c
	 *            Datum/Zeit als Calendar
	 * @return short[4]
	 */
	public static short[] getKDCCaltoShort(Calendar c) {
		short[] sa = new short[4];

		sa[0] = new Integer(((c.get(Calendar.YEAR) - 2000) << 2)
				+ ((c.get(Calendar.MONTH) + 1) >> 2)).shortValue();
		sa[1] = new Integer((((c.get(Calendar.MONTH) + 1) & 3) << 6)
				+ (c.get(Calendar.DAY_OF_MONTH) << 1) + c.get(Calendar.AM_PM))
				.shortValue();
		sa[2] = new Integer((c.get(Calendar.HOUR) << 4)
				+ (c.get(Calendar.MINUTE) >> 2)).shortValue();
		sa[3] = new Integer(((c.get(Calendar.MINUTE) & 3) << 6)
				+ (c.get(Calendar.SECOND))).shortValue();
		return sa;
	}

	public static java.sql.Date parseString2Date(String s) {
		return parseString2Date(s, new SimpleDateFormat("dd.MM.yyyy"));
	}

	public static java.sql.Date parseString2Date(String s,
			SimpleDateFormat format) {
		java.sql.Date date = null;
		try {
			date = new java.sql.Date(format.parse(s).getTime());
		} catch (ParseException e) {
			//
		}
		return date;
	}

	public static String[] monatsnamenBereich(String[] monate,
			Timestamp[] tVonBis) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(tVonBis[0]);
		int monatvon = cal.get(Calendar.MONTH) + 1;
		cal.setTime(tVonBis[1]);
		int monatbis = cal.get(Calendar.MONTH) + 1;
		int anzahl = 0;
		String[] gjMonate = null;
		if (monatvon > monatbis) {
			// geht uebers jahr
			anzahl = 1 + 12 - monatvon + monatbis;
			gjMonate = new String[anzahl];
			int j = 0;
			for (int i = monatvon; i <= 12; i++)
				gjMonate[j++] = monate[i - 1];
			for (int i = 1; i <= monatbis; i++)
				gjMonate[j++] = monate[i - 1];
		} else {
			anzahl = 1 + monatbis - monatvon;
			gjMonate = new String[anzahl];
			int j = 0;
			for (int i = monatvon; i <= monatbis; i++) {
				gjMonate[j++] = monate[i - 1];
			}
		}
		return gjMonate;
	}

	public static Timestamp[] getTimestampVonBisEinerKW(java.sql.Timestamp tKW) {
		Timestamp[] tVonBis = new Timestamp[2];

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(tKW.getTime());

		int kwAktuell = c.get(Calendar.WEEK_OF_YEAR);

		Timestamp tKWVon = new Timestamp(tKW.getTime());
		Timestamp tKWBis = new Timestamp(tKW.getTime());

		// ende der KW suchen
		while (1 == 1) {
			Timestamp tTemp = Helper.addiereTageZuTimestamp(tKWBis, 1);
			Calendar cTemp = Calendar.getInstance();
			cTemp.setTimeInMillis(tTemp.getTime());

			if (kwAktuell == cTemp.get(Calendar.WEEK_OF_YEAR)) {
				tKWBis = new Timestamp(tTemp.getTime());
			} else {
				break;
			}
		}

		// anfang der KW suchen
		while (1 == 1) {
			Timestamp tTemp = Helper.addiereTageZuTimestamp(tKWVon, -1);
			Calendar cTemp = Calendar.getInstance();
			cTemp.setTimeInMillis(tTemp.getTime());

			if (kwAktuell == cTemp.get(Calendar.WEEK_OF_YEAR)) {
				tKWVon = new Timestamp(tTemp.getTime());
			} else {
				break;
			}
		}

		tVonBis[0] = tKWVon;
		tVonBis[1] = tKWBis;

		return tVonBis;
	}

	public static boolean istKennwortParameter(String parameter) {
		return parameter.equals(ParameterFac.PARAMETER_SMTPSERVER_KENNWORT)
				|| parameter
						.equals(ParameterFac.PARAMETER_IMAPSERVER_ADMIN_KENNWORT)
				|| parameter
						.equals(ParameterFac.PARAMETER_SMTPSERVER_XPIRIO_KENNWORT);
	}

	public static BigDecimal getMehrwertsteuerBetrag(BigDecimal bdBrutto,
			BigDecimal bdMwstSatz) {
		if (bdBrutto == null)
			return null;

		bdMwstSatz = bdMwstSatz.movePointLeft(2);
		BigDecimal bdBetragBasis = bdBrutto.divide(
				BigDecimal.ONE.add(bdMwstSatz), FinanzFac.NACHKOMMASTELLEN,
				BigDecimal.ROUND_HALF_EVEN);
		return Helper.rundeKaufmaennisch(bdBrutto.subtract(bdBetragBasis),
				FinanzFac.NACHKOMMASTELLEN);
	}

	/**
	 * Ermittelt den Mehrwertsteuerbetrag f&uuml;r den gegebenen Nettobetrag und
	 * Satz
	 * 
	 * @param bdNetto
	 *            ist der Nettobetrag
	 * @param bdMwstSatz
	 *            der Prozentsatz
	 * @return den f&uuml;r Finanz passend gerundeten Mwstbetrag
	 */
	public static BigDecimal getMehrwertsteuerBetragFuerNetto(
			BigDecimal bdNetto, BigDecimal bdMwstSatz) {
		if (bdNetto == null)
			return null;

		bdMwstSatz = bdMwstSatz.movePointLeft(2);
		BigDecimal bdBetragBasis = bdNetto.multiply(bdMwstSatz);
		return Helper.rundeKaufmaennisch(bdBetragBasis,
				FinanzFac.NACHKOMMASTELLEN);
	}

	/**
	 * Ein neues FilterKriterium Array erzeugen, welches um additionalItems mehr
	 * Eintraege Platz hat
	 * 
	 * @param baseArray
	 *            ist das zu kopierende Array
	 * @param additionalItems
	 *            die Anzahl der zusaetzlichen (am Ende befindlichen) Eintraege
	 * @return das neue Array das die Daten von baseArray enthaelt und um
	 *         additionalItems Eintraege mehr Platz hat
	 */
	public static FilterKriterium[] copyFilterKriterium(
			FilterKriterium[] baseArray, int additionalItems) {
		if (null == baseArray)
			throw new IllegalArgumentException("baseArray");

		FilterKriterium[] newArray = new FilterKriterium[baseArray.length
				+ additionalItems];
		for (int i = 0; i < baseArray.length; i++) {
			newArray[i] = baseArray[i];
		}
		return newArray;
	}

	/**
	 * Ist source in String[] many enthalten?
	 * 
	 * @param source
	 * @param many
	 * @return true, wenn source "equals" irgendeinem Element in many
	 */
	public static boolean isOneOf(String source, String... many) {
		if (null == many)
			return false;
		for (String element : many) {
			if (source.equals(element))
				return true;
		}
		return false;
	}

	public static boolean isOneOf(int source, int[] many) {
		if (null == many)
			return false;

		for (int i : many) {
			if (i == source)
				return true;
		}
		return false;
	}

	public static void setJcrDocBinaryData(JCRDocDto jcrDocDto,
			JasperPrint jasperPrint) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(100000);
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(jasperPrint);
			oos.flush();
			baos.flush();
			jcrDocDto.setbData(baos.toByteArray());
		} catch (IOException ex) {
		} finally {
			closeIgnoreIOEx(oos);
			closeIgnoreIOEx(baos);

			// if (oos != null) {
			// try {
			// oos.close();
			// } catch (IOException ioEx) {
			// }
			// }
			//
			// if (baos != null) {
			// try {
			// baos.close();
			// } catch (IOException ioEx) {
			// }
			// }
		}
	}

	public static void closeIgnoreIOEx(ObjectOutputStream oos) {
		if (null == oos)
			return;

		try {
			oos.close();
		} catch (IOException ex) {
		}
	}

	// SP1786
	public static int berechneJahrDerKW(Calendar c) {
		int iKW = c.get(Calendar.WEEK_OF_YEAR);
		int iJahr = c.get(Calendar.YEAR);
		if (iKW == 1) {
			if (c.get(Calendar.MONTH) == Calendar.DECEMBER) {
				// lt Wikipedia http://de.wikipedia.org/wiki/Dezember
				// Ist der 29., 30. oder 31. Dezember ein Montag, werden die
				// Tage ab Montag der ersten Kalenderwoche des Folgejahres
				// zugerechnet

				boolean jahrErhoehen = false;

				Calendar cTag = Calendar.getInstance();
				cTag.set(Calendar.YEAR, c.get(Calendar.YEAR));
				cTag.set(Calendar.MONTH, Calendar.DECEMBER);
				cTag.set(Calendar.DAY_OF_MONTH, 29);

				if (cTag.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
					jahrErhoehen = true;
				} else {
					cTag.set(Calendar.DAY_OF_MONTH, 30);
					if (cTag.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
						jahrErhoehen = true;
					} else {
						cTag.set(Calendar.DAY_OF_MONTH, 31);
						if (cTag.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
							jahrErhoehen = true;
						}
					}

				}
				if (jahrErhoehen == true) {
					iJahr = iJahr + 1;
				}

			}

		}

		return iJahr;
	}

	public static void closeIgnoreIOEx(ByteArrayOutputStream baos) {
		if (null == baos)
			return;

		try {
			baos.close();
		} catch (IOException ex) {
		}
	}

	/**
	 * Ist dieser String leer/null?</br>
	 * 
	 * Ein String ist nicht leer, wenn er getrimmed eine L&auml;nge > 0 hat
	 * 
	 * @param value
	 *            der zu pruefende String
	 * @return false wenn der String null oder eine L&auml;nge von 0 hat,
	 *         ansonsten true
	 */
	public static boolean isStringEmpty(String value) {
		if (null == value)
			return true;
		return value.trim().length() == 0;
	}

	/**
	 * Erzeugt aus einem String Array einen String, welcher dem SQL Operator
	 * 'IN' &uuml;bergeben werden kann. <br>
	 * <br>
	 * 
	 * @param values
	 *            ["Angelegt"]["Offen"]["Storniert"]
	 * @return "('Angelegt','Offen','Storniert')" ohne die doppelten
	 *         Anf&uuml;hrungszeichen.
	 */
	public static String arrayToSqlInList(String... values) {
		String s = "(";
		for (String cnr : values) {
			s += "'" + cnr + "',";
		}
		return s.substring(0, s.length() - 1) + ")";

	}

	/**
	 * Erzeugt aus einer Integer Collection einen String, welcher dem SQL
	 * Operator 'IN' &uuml;bergeben werden kann. <br>
	 * <br>
	 * 
	 * @param values
	 *            [1, 2, 4]
	 * @return "(1,2,4)" ohne die doppelten Anf&uuml;hrungszeichen.
	 */
	public static String arrayToSqlInList(Collection<Integer> values) {
		String s = "(";
		for (Integer i : values) {
			s += "" + i + ",";
		}
		return s.substring(0, s.length() - 1) + ")";

	}
}
