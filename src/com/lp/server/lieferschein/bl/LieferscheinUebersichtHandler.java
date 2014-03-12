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
package com.lp.server.lieferschein.bl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.TreeMap;

import com.lp.server.auftrag.bl.UseCaseHandlerTabelle;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinUebersichtTabelleDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.EJBExceptionLP;

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

public class LieferscheinUebersichtHandler extends UseCaseHandlerTabelle {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int iAnzahlZeilen = 12; // Default sind 12 Monate
	private final int iAnzahlSpalten = 11;

	/**
	 * Konstruktor.
	 */
	public LieferscheinUebersichtHandler() {
		super();

		try {
			setAnzahlZeilen(iAnzahlZeilen);
			setAnzahlSpalten(iAnzahlSpalten);
		} catch (Throwable t) {
			// @todo was tun ??? PJ 4409
		}
	}

	public TableInfo getTableInfo() {
		if (tableInfo == null) {
			tableInfo = new TableInfo(
					new Class[] { String.class, Integer.class, Integer.class,
							Integer.class, Integer.class, Integer.class,
							Integer.class, Integer.class, Integer.class,
							Integer.class, Integer.class },
					// die Spaltenueberschriften werden am Client gesetzt
					new String[] { "", "", "", "", "", "", "", "", "", "", "" },
					new String[] { "", "", "", "", "", "", "", "", "", "", "" });
		}

		return tableInfo;
	}

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		String mandantCNr = theClientDto.getMandant();
		Locale locUI = theClientDto.getLocUi();

		QueryResult result = null;

		try {
			// die aktuellen Filter Kriterien bestimmen
			getFilterKriterien();

			// enthaelt die Jahreszahl, bei der die Uebersicht beginnt
			// KRIT_JAHRESZAHL = int Value
			FilterKriterium fkJahreszahl = aFilterKriterium[LieferscheinFac.IDX_KRIT_JAHRESZAHL];

			int iJahreszahl = Integer.parseInt(fkJahreszahl.value);

			// enthaelt KRIT_TAG | KRIT_WOCHE | KRIT_MONAT = true
			FilterKriterium fkZeiteinheit = aFilterKriterium[LieferscheinFac.IDX_KRIT_ZEITEINHEIT];

			if (fkZeiteinheit.kritName.equals(LieferscheinFac.KRIT_TAG)) {
				iAnzahlZeilen = 366; // @WH ???
			} else if (fkZeiteinheit.kritName
					.equals(LieferscheinFac.KRIT_WOCHE)) {
				iAnzahlZeilen = 52; // @WH ???
			} else if (fkZeiteinheit.kritName
					.equals(LieferscheinFac.KRIT_MONAT)) {
				iAnzahlZeilen = 12;
			}

			// in dieser Map werden alle darzustellenden Daten gesammelt
			TreeMap<Integer, LieferscheinUebersichtTabelleDto> hmSammelstelle = new TreeMap<Integer, LieferscheinUebersichtTabelleDto>();

			// Monatsnamen localeabhaengig mit Calendar formatieren
			SimpleDateFormat dateformat = new SimpleDateFormat("MMMM", locUI);
			Calendar cal = GregorianCalendar.getInstance(locUI);
			cal.set(cal.YEAR, GregorianCalendar.JANUARY, 1);

			// ueber alle Zeilen iterieren
			for (int i = 0; i < iAnzahlZeilen; i++) {
				LieferscheinUebersichtTabelleDto oDto = new LieferscheinUebersichtTabelleDto();

				// die jeweiligen Spalten einer Zeile
				String sZeilenHeader = null;

				switch (i) {
				case 0:
					// in dieser Zeile stehen die Summen fuer den Januar des
					// aktuellen Jahres
					cal.set(cal.YEAR, GregorianCalendar.JANUARY, 1);
					sZeilenHeader = dateformat.format(cal.getTime());
					break;
				case 1:
					cal.set(cal.YEAR, GregorianCalendar.FEBRUARY, 1);
					sZeilenHeader = dateformat.format(cal.getTime());
					break;
				case 2:
					cal.set(cal.YEAR, GregorianCalendar.MARCH, 1);
					sZeilenHeader = dateformat.format(cal.getTime());
					break;
				case 3:
					cal.set(cal.YEAR, GregorianCalendar.APRIL, 1);
					sZeilenHeader = dateformat.format(cal.getTime());
					break;
				case 4:
					cal.set(cal.YEAR, GregorianCalendar.MAY, 1);
					sZeilenHeader = dateformat.format(cal.getTime());
					break;
				case 5:
					cal.set(cal.YEAR, GregorianCalendar.JUNE, 1);
					sZeilenHeader = dateformat.format(cal.getTime());
					break;
				case 6:
					cal.set(cal.YEAR, GregorianCalendar.JULY, 1);
					sZeilenHeader = dateformat.format(cal.getTime());
					break;
				case 7:
					cal.set(cal.YEAR, GregorianCalendar.AUGUST, 1);
					sZeilenHeader = dateformat.format(cal.getTime());
					break;
				case 8:
					cal.set(cal.YEAR, GregorianCalendar.SEPTEMBER, 1);
					sZeilenHeader = dateformat.format(cal.getTime());
					break;
				case 9:
					cal.set(cal.YEAR, GregorianCalendar.OCTOBER, 1);
					sZeilenHeader = dateformat.format(cal.getTime());
					break;
				case 10:
					cal.set(cal.YEAR, GregorianCalendar.NOVEMBER, 1);
					sZeilenHeader = dateformat.format(cal.getTime());
					break;
				case 11:
					cal.set(cal.YEAR, GregorianCalendar.DECEMBER, 1);
					sZeilenHeader = dateformat.format(cal.getTime());
					break;
				}

				if (sZeilenHeader != null) {
					oDto.setSZeilenheader(sZeilenHeader);
				}

				// ueber alle Spalten iterieren
				for (int j = 0; j < iAnzahlSpalten; j++) {
				}

				hmSammelstelle.put(new Integer(i), oDto);
			}

			// jetzt die Darstellung in der Tabelle zusammenbauen
			long startIndex = 0;
			long endIndex = startIndex + getAnzahlZeilen() - 1;

			Object[][] rows = new Object[(int) getAnzahlZeilen()][getAnzahlSpalten()];
			int row = 0;
			int col = 0;

			Collection<LieferscheinUebersichtTabelleDto> clSichtLieferstatus = hmSammelstelle.values();
			Iterator<LieferscheinUebersichtTabelleDto> it = clSichtLieferstatus.iterator();

			while (it.hasNext()) {
				LieferscheinUebersichtTabelleDto oLsUebersichtDto = (LieferscheinUebersichtTabelleDto) it
						.next();

				rows[row][col++] = oLsUebersichtDto.getSZeilenHeader();
				rows[row][col++] = oLsUebersichtDto.getBdUmsatzJahr0();
				rows[row][col++] = oLsUebersichtDto.getBdUmsatzJahr1();
				rows[row][col++] = oLsUebersichtDto.getBdUmsatzJahr2();
				rows[row][col++] = oLsUebersichtDto.getBdUmsatzJahr3();
				rows[row][col++] = oLsUebersichtDto.getBdUmsatzJahr4();
				rows[row][col++] = oLsUebersichtDto.getBdUmsatzJahr5();
				rows[row][col++] = oLsUebersichtDto.getBdUmsatzJahr6();
				rows[row][col++] = oLsUebersichtDto.getBdUmsatzJahr7();
				rows[row][col++] = oLsUebersichtDto.getBdUmsatzJahr8();
				rows[row++][col++] = oLsUebersichtDto.getBdUmsatzJahr9();

				col = 0;
			}

			result = new QueryResult(rows, getRowCount(), startIndex, endIndex,
					0);
		} catch (Exception e) {
			throw new EJBExceptionLP(1, e);
		}

		return result;
	}
}
