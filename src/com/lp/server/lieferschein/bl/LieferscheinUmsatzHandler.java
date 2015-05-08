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
package com.lp.server.lieferschein.bl;

import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import com.lp.server.auftrag.bl.UseCaseHandlerTabelle;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinUmsatzTabelleDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * Handler fuer Uebersichtstabelle im Lieferschein. <br>
 * Diese Handlerklasse geht nicht ueber Hibernate.
 * <p>
 * Copright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum 13.03.2005
 * </p>
 * <p>
 * </p>
 * 
 * @author Uli Walch
 * @version 1.0
 */

public class LieferscheinUmsatzHandler extends UseCaseHandlerTabelle {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** In dieser Map werden alle Ergebniszeilen in Forma von Dtos gesammelt. */
	private Map<Long, LieferscheinUmsatzTabelleDto> hmSammelstelle = null;

	/**
	 * Konstruktor.
	 */
	public LieferscheinUmsatzHandler() {
		super();

		try {
			setAnzahlSpalten(4);
		} catch (Throwable t) {
			// @todo was tun ??? PJ 4410
		}
	}

	public TableInfo getTableInfo() {
		if (tableInfo == null) {
			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();
			tableInfo = new TableInfo(new Class[] { String.class,String.class,
					BigDecimal.class, Integer.class },
			// die Spaltenueberschriften werden durch die Kriterien bestimmt
					new String[] {" ",
							" ",
							" ",
							getTextRespectUISpr("ls.anzahllieferscheine",
									mandantCNr, locUI) }, new String[] {"", "",
							"", "" });
		}

		return tableInfo;
	}

	/**
	 * gets the data page for the specified row using the current query. The row
	 * at rowIndex will be located in the middle of the page.
	 * 
	 * @param rowIndex
	 *            Zeilenindex
	 * @return QueryResult
	 * @throws EJBExceptionLP
	 */
	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		// hier wird das Ergebnis einer Hibernate Abfrage abgelegt
		QueryResult result = null;

		try {
			// die Anzahl der Zeilen festlegen und den Inhalt befuellen
			setTableProperties(true);

			// jetzt die Darstellung in der Tabelle zusammenbauen
			long startIndex = 0;
			long endIndex = startIndex + getAnzahlZeilen() - 1;

			Object[][] rows = new Object[(int) getAnzahlZeilen()][getAnzahlSpalten()];
			int row = 0;
			int col = 0;

			Collection<LieferscheinUmsatzTabelleDto> clUmsatz = hmSammelstelle.values();
			Iterator<LieferscheinUmsatzTabelleDto> it = clUmsatz.iterator();

			while (it.hasNext()) {
				LieferscheinUmsatzTabelleDto oLsUmsatzDto = (LieferscheinUmsatzTabelleDto) it
						.next();
				rows[row][col++] = "";
				rows[row][col++] = oLsUmsatzDto.getSZeilenHeader();
				rows[row][col++] = oLsUmsatzDto.getBdUmsatz();
				rows[row++][col++] = oLsUmsatzDto.getIiAnzahlLieferscheine();

				col = 0;
			}

			result = new QueryResult(rows, getRowCount(), startIndex, endIndex,
					0);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return result;
	}

	/**
	 * Es wird der Umsatz eines Tages angezeigt.
	 * 
	 * @param gcI
	 *            das gewaehlte Datum
	 * @return die anzuzeigenden Daten
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	private Map<Long, LieferscheinUmsatzTabelleDto> buildZeilenTag(Calendar gcI) throws Throwable {

		hmSammelstelle = new TreeMap<Long, LieferscheinUmsatzTabelleDto>();

		for (long i = getAnzahlZeilen() - 1; i >= 0; i--) {

			LieferscheinUmsatzTabelleDto oDto = new LieferscheinUmsatzTabelleDto();

			oDto
					.setSZeilenheader(Helper.formatDatum(new java.util.Date(gcI
							.getTimeInMillis()), theClientDto
							.getLocUi()));

			// die Anzahl der Lieferscheine im Zeitraum von 00:00:00 000 dieses
			// Tages
			// bis 23:59:59 999 dieses Tages

			GregorianCalendar gcVonI = (GregorianCalendar) Helper
					.cutCalendar(gcI);
			GregorianCalendar gcBisI = (GregorianCalendar) Helper
					.fillCalendar(gcI);

			Integer iiAnzahlLieferscheine = getLieferscheinFac()
					.zaehleLieferscheinFuerUmsatz(gcVonI, gcBisI, theClientDto);

			oDto.setIiAnzahlLieferscheine(iiAnzahlLieferscheine);

			oDto.setBdUmsatz(getLieferscheinFac().berechneUmsatz(gcVonI,
					gcBisI, theClientDto));

			hmSammelstelle.put(new Long(i), oDto);

			gcI.add(Calendar.DATE, -1);
		}

		return hmSammelstelle;
	}

	/**
	 * Wenn die Usaetze nach Wochen angezeigt werden.
	 * 
	 * @param gcI
	 *            das aktuelle Datum
	 * @return Map die anzuzeigenden Daten
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	private Map<Long, LieferscheinUmsatzTabelleDto> buildZeilenWoche(Calendar gcI) throws Throwable {

		hmSammelstelle = new TreeMap<Long, LieferscheinUmsatzTabelleDto>();

		// die letzte Woche in der Liste ist die aktuell gewuenschte
		Calendar calReferenz = Calendar.getInstance();
		calReferenz.setFirstDayOfWeek(Calendar.MONDAY); // unsere Woche beginnt
		// mit Montag
		calReferenz.setTime(new java.util.Date(gcI.getTimeInMillis()));

		int iKalenderwoche = calReferenz.get(Calendar.WEEK_OF_YEAR); // die
		// Zaehlung
		// beginnt
		// mit 1

		int iJahr = calReferenz.get(Calendar.YEAR);

		for (long i = getAnzahlZeilen() - 1; i >= 0; i--) {
			LieferscheinUmsatzTabelleDto oDto = new LieferscheinUmsatzTabelleDto();

			oDto.setSZeilenheader(formatKalenderwoche(iKalenderwoche, iJahr));

			// die Anzahl der Lieferscheine im Zeitraum von 00:00:00 000 des
			// ersten
			// Tages einer Kalenderwoche bis 23:59:59 999 des letzten Tages
			// einer Kalenderwoche

			GregorianCalendar gcBeginnWoche = (GregorianCalendar) calReferenz
					.clone();
			GregorianCalendar gcEndeWoche = (GregorianCalendar) calReferenz
					.clone();

			// man muss jetzt festlegen, welcher Tag der erste und welcher der
			// letzte
			// in dieser Kalenderwoche sein soll

			/*
			 * GregorianCalendar gcErsterTagDesJahres = new GregorianCalendar(
			 * calReferenz.get(Calendar.YEAR) + 1, Calendar.JANUARY, 1);
			 * 
			 * GregorianCalendar gcLetzterTagDesJahres = new GregorianCalendar(
			 * calReferenz.get(Calendar.YEAR), Calendar.DECEMBER, 31);
			 * 
			 * // pruefen, ob man auf der letzten Woche des Jahres steht if
			 * (calReferenz.get(Calendar.WEEK_OF_YEAR) ==
			 * gcLetzterTagDesJahres.get(Calendar.WEEK_OF_YEAR)) {
			 * gcBeginnWoche.set(Calendar.DAY_OF_WEEK, 2); // muss der Montag
			 * sein
			 * 
			 * int iLetzterTagDerWoche =
			 * gcLetzterTagDesJahres.get(Calendar.DAY_OF_WEEK);
			 * 
			 * // wenn der letzte Tag ein Sonntag ist, dann brauchen wir den
			 * Sonntag der NAECHSTEN Woche if (iLetzterTagDerWoche == 1) {
			 * iLetzterTagDerWoche = 8; }
			 * 
			 * gcEndeWoche.set(Calendar.DAY_OF_WEEK, iLetzterTagDerWoche); }
			 * else
			 * 
			 * // pruefen, ob man auf der ersten Woche des Jahres steht // @todo
			 * Woche 1 ist nach GregorianCalendar die erste volle Woche im neuen
			 * Jahr; PJ 4418 // wenn es einzelne Tage davor gibt, dann werden
			 * die in die letzte Kalenderwoche des vergangenen jahres gezaehlt
			 * if (calReferenz.get(Calendar.WEEK_OF_YEAR) == 1) { int
			 * iErsterTagDerWoche =
			 * gcErsterTagDesJahres.get(Calendar.DAY_OF_WEEK);
			 * 
			 * gcBeginnWoche.set(Calendar.DAY_OF_WEEK, iErsterTagDerWoche);
			 * 
			 * // wenn der erste Tag ein Sonntag ist, dann gilt dieser Sonntag
			 * auch als Ende if (iErsterTagDerWoche == 1) {
			 * gcEndeWoche.set(Calendar.DAY_OF_WEEK, iErsterTagDerWoche); } else
			 * { gcEndeWoche.set(Calendar.DAY_OF_WEEK, 8); // Sonntag der
			 * naechsten Woche } } else {
			 */
			gcBeginnWoche.set(Calendar.DAY_OF_WEEK, 2); // MONDAY
			gcEndeWoche.set(Calendar.DAY_OF_WEEK, 8); // SUNDAY der naechsten
			// Woche
			// }

			// myLogger.info(Helper.formatTimestamp(new
			// java.sql.Timestamp(gcBeginnWoche.getTimeInMillis()),
			// Locale.GERMAN));
			// myLogger.info(Helper.formatTimestamp(new
			// java.sql.Timestamp(gcEndeWoche.getTimeInMillis()),
			// Locale.GERMAN));

			Integer iiAnzahlLieferscheine = getLieferscheinFac()
					.zaehleLieferscheinFuerUmsatz(
							(GregorianCalendar) Helper
									.cutCalendar(gcBeginnWoche),
							(GregorianCalendar) Helper
									.fillCalendar(gcEndeWoche), theClientDto);

			oDto.setIiAnzahlLieferscheine(iiAnzahlLieferscheine);

			oDto.setBdUmsatz(getLieferscheinFac().berechneUmsatz(gcBeginnWoche,
					gcEndeWoche, theClientDto));

			iKalenderwoche--;

			calReferenz.set(Calendar.WEEK_OF_YEAR, iKalenderwoche);

			// wir muessen mit der Anzeige ins Vorjahr, wenn ...
			if (iKalenderwoche == 0) {

				GregorianCalendar gcVorjahr = new GregorianCalendar(iJahr - 1,
						GregorianCalendar.DECEMBER, 31);

				calReferenz.setTime(new java.util.Date(gcVorjahr
						.getTimeInMillis()));

				iKalenderwoche = calReferenz.get(Calendar.WEEK_OF_YEAR);

				iJahr = calReferenz.get(Calendar.YEAR);
			}

			hmSammelstelle.put(new Long(i), oDto);
		}

		return hmSammelstelle;
	}

	private String formatKalenderwoche(int iWoche, int iJahr) {
		String sWoche = String.valueOf(iWoche);

		if (sWoche.length() == 1) {
			sWoche = "0" + sWoche;
		}

		return sWoche + "/" + String.valueOf(iJahr).substring(2, 4);
	}

	private String formatMonat(String sMonat, int iJahr) {
		return sMonat + " " + String.valueOf(iJahr).substring(2, 4);
	}

	/**
	 * Die Umsatzzahlen werden fuer alle Monate eines bestimmten
	 * Geschaeftsjahres zurueck in die Vergangenheit berechnet.
	 * 
	 * @param gcI
	 *            das gewaehlt Datum
	 * @return Map alle darzustellenden Daten
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	private Map<Long, LieferscheinUmsatzTabelleDto> buildZeilenMonate(Calendar gcI) throws Throwable {

		// das sind alle Monatsnamen, die in dieser Tabelle in dieser
		// Reihenfolge vorkommen koennen.
		DateFormatSymbols symbols = new DateFormatSymbols(theClientDto.getLocUi());
		String[] aMonatsnamen = symbols.getMonths();

		hmSammelstelle = new TreeMap<Long, LieferscheinUmsatzTabelleDto>();

		int iCurrentMonat = gcI.get(Calendar.MONTH); // die Zaehlung beginnt mit
		// 0

		int iJahr = gcI.get(Calendar.YEAR);

		for (long i = getAnzahlZeilen() - 1; i >= 0; i--) {

			LieferscheinUmsatzTabelleDto oDto = new LieferscheinUmsatzTabelleDto();

			oDto.setSZeilenheader(formatMonat(aMonatsnamen[iCurrentMonat],
					iJahr)); // das letzte Monat in der Anzeige

			// die Anzahl der Lieferscheine im Zeitraum von 00:00:00 000 des
			// ersten
			// Tages eines Monats bis 23:59:59 999 des letzten Tages eines
			// Monats

			GregorianCalendar gcBeginnMonat = new GregorianCalendar(iJahr,
					iCurrentMonat, 1);

			int iLetzterTag = gcBeginnMonat
					.getActualMaximum(Calendar.DAY_OF_MONTH);

			GregorianCalendar gcEndeMonat = new GregorianCalendar(iJahr,
					iCurrentMonat, iLetzterTag);

			Integer iiAnzahlLieferscheine = getLieferscheinFac()
					.zaehleLieferscheinFuerUmsatz(
							(GregorianCalendar) Helper
									.cutCalendar(gcBeginnMonat),
							(GregorianCalendar) Helper
									.fillCalendar(gcEndeMonat), theClientDto);

			oDto.setIiAnzahlLieferscheine(iiAnzahlLieferscheine);

			oDto.setBdUmsatz(getLieferscheinFac().berechneUmsatz(gcBeginnMonat,
					gcEndeMonat, theClientDto));

			iCurrentMonat--;

			if (iCurrentMonat == -1) {
				iCurrentMonat = 11; // mit Dezember weitermachen

				iJahr--;
			}

			hmSammelstelle.put(new Long(i), oDto);
		}

		return hmSammelstelle;
	}

	protected long getRowCountFromDataBase() {
		try {
			setTableProperties(false);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR,
					new Exception(t));
		}
		return getAnzahlZeilen();
	}

	/**
	 * Diese Methode setzt die Anzahl der Zeilen in der Tabelle und den Inhalt.
	 * 
	 * @param bFillData
	 *            false, wenn der Inhalt nicht befuellt werden soll
	 * @throws Throwable
	 *             Ausnahme
	 */
	private void setTableProperties(boolean bFillData) throws Throwable {

		// die aktuellen Filter Kriterien bestimmen
		getFilterKriterien();

		// enthaelt KRIT_TAG = true || KRIT_WOCHE = true || KRIT_MONAT = true
		FilterKriterium fkZeiteinheit = aFilterKriterium[LieferscheinFac.LS_UMSATZ_IDX_KRIT_ZEITEINHEIT];

		Calendar calendar = Calendar.getInstance(theClientDto.getLocUi());

		if (fkZeiteinheit.kritName.equals(LieferscheinFac.KRIT_TAG)) {
			setAnzahlZeilen(20);

			if (bFillData) {
				hmSammelstelle = buildZeilenTag(calendar);
			}
		} else if (fkZeiteinheit.kritName.equals(LieferscheinFac.KRIT_WOCHE)) {
			setAnzahlZeilen(20);

			if (bFillData) {
				hmSammelstelle = buildZeilenWoche(calendar);
			}
		} else if (fkZeiteinheit.kritName.equals(LieferscheinFac.KRIT_MONAT)) {
			setAnzahlZeilen(20);

			if (bFillData) {
				hmSammelstelle = buildZeilenMonate(calendar);
			}
		}
	}
}
