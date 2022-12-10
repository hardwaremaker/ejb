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
package com.lp.server.rechnung.bl;

import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungUmsatzTabelleDto;
import com.lp.server.util.UmsatzUseCaseHandlerTabelle;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.EJBExceptionLP;

/**
 * Handler fuer Uebersichtstabelle in der Rechnung. <br>
 * Diese Handlerklasse geht nicht ueber Hibernate.
 * <p>
 * Copright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum 2005-01-20
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version 1.0
 */

public class RechnungUmsatzHandler extends UmsatzUseCaseHandlerTabelle {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final int iAnzahlSpalten = 10;

	private ArrayList<RechnungUmsatzTabelleDto> hmDaten = null;

	/**
	 * Konstruktor.
	 */
	public RechnungUmsatzHandler() {
		super();

		setAnzahlSpalten(iAnzahlSpalten);
	}

	public TableInfo getTableInfo() throws EJBExceptionLP {
		if (tableInfo == null) {
			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();

			tableInfo = new TableInfo(
					new Class[] { String.class, String.class, BigDecimal.class, BigDecimal.class, String.class,
							BigDecimal.class, BigDecimal.class, String.class, BigDecimal.class, BigDecimal.class },
					new String[] { "", "  ", getTextRespectUISpr("lp.brutto", mandantCNr, locUI),
							getTextRespectUISpr("lp.netto", mandantCNr, locUI), "  ",
							getTextRespectUISpr("lp.brutto", mandantCNr, locUI),
							getTextRespectUISpr("lp.netto", mandantCNr, locUI), "  ",
							getTextRespectUISpr("lp.brutto", mandantCNr, locUI),
							getTextRespectUISpr("lp.netto", mandantCNr, locUI) },
					new String[] { "", "", "", "", "", "", "", "", "", "" });
		}
		return tableInfo;
	}

	protected long getRowCountFromDataBase() {
		try {

			hmDaten = new ArrayList<RechnungUmsatzTabelleDto>();
			getFilterKriterien();

			setInhalt();
		} catch (Throwable t) {
			if (t.getCause() instanceof EJBExceptionLP) {
				throw (EJBExceptionLP) t.getCause();
			} else {

				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, new Exception(t));
			}
		}
		return getAnzahlZeilen();
	}

	private void setInhalt() throws Throwable {
		mandantCNr = theClientDto.getMandant();
		locUI = theClientDto.getLocUi();
		hmDaten = new ArrayList<RechnungUmsatzTabelleDto>();
		DateFormatSymbols symbols = new DateFormatSymbols(locUI);
		aMonatsnamen = symbols.getMonths();

		getFilterKriterien();
		FilterKriterium fkAuswertung = aFilterKriterium[RechnungFac.IDX_KRIT_AUSWERTUNG];
		FilterKriterium fkJahr = aFilterKriterium[RechnungFac.IDX_KRIT_JAHR];

		Integer iJahr = new Integer(fkJahr.value).intValue();

		boolean bGerschaeftsjahr = false;
		if (fkJahr.kritName.equals(RechnungFac.KRIT_JAHR_GESCHAEFTSJAHR)) {
			bGerschaeftsjahr = true;
		}

		// Zeile 1 Vorjahr
		RechnungUmsatzTabelleDto zeileDto = new RechnungUmsatzTabelleDto();
		zeileDto.setSZeilenheader(getTextRespectUISpr("lp.vorjahr", mandantCNr, locUI) + " " + (iJahr - 1));

		GregorianCalendar[] gcVonBis = getVorjahr(iJahr, 0,bGerschaeftsjahr);

		hmDaten.add(
				befuelleDtoAnhandDatumsgrenzen(getTextRespectUISpr("lp.vorjahr", mandantCNr, locUI) + " " + (iJahr - 1),
						fkAuswertung.kritName, gcVonBis[0], gcVonBis[1]));

		// Leerzeile
		hmDaten.add(new RechnungUmsatzTabelleDto());

		// Nun Monate des aktuellen GF durchgehen

		ArrayList<GregorianCalendar[]> alMonate = getMonateAktuellesJahr(iJahr, 0, bGerschaeftsjahr);

		for (int i = 0; i < alMonate.size(); i++) {

			GregorianCalendar[] gcVonBisMonate = alMonate.get(i);

			hmDaten.add(befuelleDtoAnhandDatumsgrenzen(aMonatsnamen[gcVonBisMonate[0].get(GregorianCalendar.MONTH)],
					fkAuswertung.kritName, gcVonBisMonate[0], gcVonBisMonate[1]));
		}

		// Leerzeile
		hmDaten.add(new RechnungUmsatzTabelleDto());

		// Summe aktuelles Jahr
		GregorianCalendar[] gcVonBisAktuell = getAktuellesJahr(iJahr, 0, bGerschaeftsjahr);

		hmDaten.add(befuelleDtoAnhandDatumsgrenzen(getTextRespectUISpr("lp.summe", mandantCNr, locUI) + " " + iJahr,
				fkAuswertung.kritName, gcVonBisAktuell[0], gcVonBisAktuell[1]));

		// Summe Gesamt
		GregorianCalendar[] gcVonBisGesamt = getGesamtJahr(iJahr, 0, bGerschaeftsjahr);

		hmDaten.add(befuelleDtoAnhandDatumsgrenzen(getTextRespectUISpr("lp.gesamtsumme", mandantCNr, locUI),
				fkAuswertung.kritName, gcVonBisGesamt[0], gcVonBisGesamt[1]));

		setAnzahlZeilen(hmDaten.size());

	}

	private RechnungUmsatzTabelleDto befuelleDtoAnhandDatumsgrenzen(String header, String kriterium,
			GregorianCalendar gcBerechnungsdatumVonI, GregorianCalendar gcBerechnungsdatumBisI) throws Throwable {

		RechnungUmsatzTabelleDto zeileDto = new RechnungUmsatzTabelleDto();

		BigDecimal bdOffeneBrutto = getRechnungFac().berechneSummeOffenBrutto(mandantCNr, kriterium,
				gcBerechnungsdatumVonI, gcBerechnungsdatumBisI, theClientDto);

		// den offenen Nettowert bestimmen
		BigDecimal bdOffeneNetto = getRechnungFac().berechneSummeOffenNetto(mandantCNr, kriterium,
				gcBerechnungsdatumVonI, gcBerechnungsdatumBisI, null, false, theClientDto);

		// den Umsatz brutto bestimmen
		BigDecimal bdUmsatzBrutto = getRechnungFac().berechneSummeUmsatzBrutto(mandantCNr, kriterium,
				gcBerechnungsdatumVonI, gcBerechnungsdatumBisI,false, theClientDto);

		// den Umsatz netto bestimmen
		BigDecimal bdUmsatzNetto = getRechnungFac().berechneSummeUmsatzNetto(mandantCNr, kriterium,
				gcBerechnungsdatumVonI, gcBerechnungsdatumBisI,false, theClientDto);

		// den Anzahlung Gesamtbrutto bestimmen
		BigDecimal bdAnzahlungBrutto = getRechnungFac().berechneSummeAnzahlungBrutto(mandantCNr, kriterium,
				gcBerechnungsdatumVonI, gcBerechnungsdatumBisI, false,false, theClientDto);

		// den Anzahlung Gesamtnetto bestimmen
		BigDecimal bdAnzahlungNetto = getRechnungFac().berechneSummeAnzahlungNetto(mandantCNr, kriterium,
				gcBerechnungsdatumVonI, gcBerechnungsdatumBisI, false,false, theClientDto);

		// den nichabgerechneten Anzahlungen brutto bestimmen
		BigDecimal bdAnzahlungBruttoNichtAbgerechnet = getRechnungFac().berechneSummeAnzahlungBrutto(mandantCNr,
				kriterium, gcBerechnungsdatumVonI, gcBerechnungsdatumBisI, true,false, theClientDto);

		// den Anzahlung netto bestimmen
		BigDecimal bdAnzahlungNettoNichtAbgerechnet = getRechnungFac().berechneSummeAnzahlungNetto(mandantCNr,
				kriterium, gcBerechnungsdatumVonI, gcBerechnungsdatumBisI, true,false, theClientDto);

		zeileDto.setSZeilenheader(header);
		zeileDto.setBdOffeneBrutto(bdOffeneBrutto);
		zeileDto.setBdOffeneNetto(bdOffeneNetto);
		zeileDto.setBdUmsatzBrutto(bdUmsatzBrutto.subtract(bdAnzahlungBrutto));
		zeileDto.setBdUmsatzNetto(bdUmsatzNetto.subtract(bdAnzahlungNetto));
		zeileDto.setBdAnzahlungBrutto(bdAnzahlungBruttoNichtAbgerechnet);
		zeileDto.setBdAnzahlungNetto(bdAnzahlungNettoNichtAbgerechnet);

		return zeileDto;
	}

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;

		try {
			// die Anzahl der Zeilen festlegen und den Inhalt befuellen

			setInhalt();

			// jetzt die Darstellung in der Tabelle zusammenbauen
			long startIndex = 0;
			long endIndex = startIndex + getAnzahlZeilen() - 1;

			Object[][] rows = new Object[(int) getAnzahlZeilen()][getAnzahlSpalten()];

			for (int row = 0; row < hmDaten.size(); row++) {

				RechnungUmsatzTabelleDto oReUebersichtDto = hmDaten.get(row);

				rows[row][RechnungFac.FLR_SPALTE_RE_UMSATZ_HEADER] = oReUebersichtDto.getSZeilenHeader();
				rows[row][RechnungFac.FLR_SPALTE_RE_UMSATZ_OFFEN_BRUTTO] = oReUebersichtDto.getBdOffeneBrutto();
				rows[row][RechnungFac.FLR_SPALTE_RE_UMSATZ_OFFEN_NETTO] = oReUebersichtDto.getBdOffeneNetto();
				rows[row][RechnungFac.FLR_SPALTE_RE_UMSATZ_LEER1] = oReUebersichtDto.getSEmpty();
				rows[row][RechnungFac.FLR_SPALTE_RE_UMSATZ_UMSATZ_BRUTTO] = oReUebersichtDto.getBdUmsatzBrutto();
				rows[row][RechnungFac.FLR_SPALTE_RE_UMSATZ_UMSATZ_NETTO] = oReUebersichtDto.getBdUmsatzNetto();
				rows[row][RechnungFac.FLR_SPALTE_RE_UMSATZ_LEER2] = oReUebersichtDto.getSEmpty();
				rows[row][RechnungFac.FLR_SPALTE_RE_UMSATZ_ANZAHLUNG_BRUTTO] = oReUebersichtDto.getBdAnzahlungBrutto();
				rows[row][RechnungFac.FLR_SPALTE_RE_UMSATZ_ANZAHLUNG_NETTO] = oReUebersichtDto.getBdAnzahlungNetto();
			}
			result = new QueryResult(rows, getRowCount(), startIndex, endIndex, 0);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return result;
	}

}
