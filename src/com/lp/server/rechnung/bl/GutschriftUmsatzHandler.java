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
import java.rmi.RemoteException;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.TreeMap;

import com.lp.server.auftrag.bl.UseCaseHandlerTabelle;
import com.lp.server.rechnung.service.GutschriftUmsatzTabelleDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungUmsatzTabelleDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.UmsatzUseCaseHandlerTabelle;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * Handler fuer Uebersichtstabelle in der Rechnung. <br>
 * Diese Handlerklasse geht nicht ueber Hibernate.
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 07.04.2005
 * </p>
 * 
 * <p>
 * 
 * @author $Author: victor $
 *         </p>
 * 
 * @version not attributable Date $Date: 2009/07/09 16:29:18 $
 */
public class GutschriftUmsatzHandler extends UmsatzUseCaseHandlerTabelle {

	private static final long serialVersionUID = 1L;

	private ArrayList<GutschriftUmsatzTabelleDto> hmDaten = null;

	private final int iAnzahlSpalten = 7;

	/**
	 * Konstruktor.
	 */
	public GutschriftUmsatzHandler() {
		super();
		setAnzahlZeilen(iAnzahlZeilen);
		setAnzahlSpalten(iAnzahlSpalten);
	}

	/**
	 * Jede ableitende Klasse muss in dieser Methode die Variable tableInfo
	 * befuellen.
	 * 
	 * @throws EJBExceptionLP
	 * @return TableInfo
	 */
	public TableInfo getTableInfo() throws EJBExceptionLP {
		if (tableInfo == null) {
			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();
			tableInfo = new TableInfo(
					new Class[] { String.class, String.class, BigDecimal.class, BigDecimal.class, String.class,
							BigDecimal.class, BigDecimal.class },
					new String[] { "", " ", getTextRespectUISpr("lp.brutto", mandantCNr, locUI),
							getTextRespectUISpr("lp.netto", mandantCNr, locUI), " ",
							getTextRespectUISpr("lp.brutto", mandantCNr, locUI),
							getTextRespectUISpr("lp.netto", mandantCNr, locUI) },
					new String[] { "", "", "", "", "", "", "" });
		}
		return this.tableInfo;
	}

	protected long getRowCountFromDataBase() {
		try {

			hmDaten = new ArrayList<GutschriftUmsatzTabelleDto>();
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

	private GutschriftUmsatzTabelleDto befuelleDtoAnhandDatumsgrenzen(String header, String kriterium,
			GregorianCalendar gcBerechnungsdatumVonI, GregorianCalendar gcBerechnungsdatumBisI) throws Throwable {

		GutschriftUmsatzTabelleDto zeileDto = new GutschriftUmsatzTabelleDto();

		BigDecimal bdOffeneBrutto = getRechnungFac().berechneSummeGutschriftOffenBrutto(theClientDto.getMandant(),
				kriterium, gcBerechnungsdatumVonI, gcBerechnungsdatumBisI, theClientDto);

		// den offenen Nettowert bestimmen
		BigDecimal bdOffeneNetto = getRechnungFac().berechneSummeGutschriftOffenNetto(theClientDto.getMandant(),
				kriterium, gcBerechnungsdatumVonI, gcBerechnungsdatumBisI, theClientDto);

		// den Umsatz brutto bestimmen
		BigDecimal bdUmsatzBrutto = getRechnungFac().berechneSummeGutschriftUmsatzBrutto(theClientDto.getMandant(),
				kriterium, gcBerechnungsdatumVonI, gcBerechnungsdatumBisI, theClientDto);

		// den Umsatz netto bestimmen
		BigDecimal bdUmsatzNetto = getRechnungFac().berechneSummeGutschriftUmsatzNetto(theClientDto.getMandant(),
				kriterium, gcBerechnungsdatumVonI, gcBerechnungsdatumBisI, theClientDto);

		zeileDto.setSZeilenheader(header);
		zeileDto.setBdOffeneBrutto(bdOffeneBrutto);
		zeileDto.setBdOffeneNetto(bdOffeneNetto);
		zeileDto.setBdUmsatzBrutto(bdUmsatzBrutto);
		zeileDto.setBdUmsatzNetto(bdUmsatzNetto);

		return zeileDto;
	}

	private void setInhalt() throws Throwable {
		mandantCNr = theClientDto.getMandant();
		locUI = theClientDto.getLocUi();
		hmDaten = new ArrayList<GutschriftUmsatzTabelleDto>();
		DateFormatSymbols symbols = new DateFormatSymbols(locUI);
		aMonatsnamen = symbols.getMonths();

		getFilterKriterien();
		FilterKriterium fkAuswertung = aFilterKriterium[RechnungFac.IDX_KRIT_JAHR];
		FilterKriterium fkJahr = aFilterKriterium[RechnungFac.IDX_KRIT_JAHR];

		Integer iJahr = new Integer(fkJahr.value).intValue();

		boolean bGerschaeftsjahr = false;
		if (fkJahr.kritName.equals(RechnungFac.KRIT_JAHR_GESCHAEFTSJAHR)) {
			bGerschaeftsjahr = true;
		}

		// Zeile 1 Vorjahr
		RechnungUmsatzTabelleDto zeileDto = new RechnungUmsatzTabelleDto();
		zeileDto.setSZeilenheader(getTextRespectUISpr("lp.vorjahr", mandantCNr, locUI) + " " + (iJahr - 1));

		GregorianCalendar[] gcVonBis = getVorjahr(iJahr,0, bGerschaeftsjahr);

		hmDaten.add(
				befuelleDtoAnhandDatumsgrenzen(getTextRespectUISpr("lp.vorjahr", mandantCNr, locUI) + " " + (iJahr - 1),
						fkAuswertung.kritName, gcVonBis[0], gcVonBis[1]));

		// Leerzeile
		hmDaten.add(new GutschriftUmsatzTabelleDto());

		// Nun Monate des aktuellen GF durchgehen

		ArrayList<GregorianCalendar[]> alMonate = getMonateAktuellesJahr(iJahr, 0, bGerschaeftsjahr);

		for (int i = 0; i < alMonate.size(); i++) {

			GregorianCalendar[] gcVonBisMonate = alMonate.get(i);

			hmDaten.add(befuelleDtoAnhandDatumsgrenzen(aMonatsnamen[gcVonBisMonate[0].get(GregorianCalendar.MONTH)],
					fkAuswertung.kritName, gcVonBisMonate[0], gcVonBisMonate[1]));
		}

		// Leerzeile
		hmDaten.add(new GutschriftUmsatzTabelleDto());

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

	/**
	 * gets the data page for the specified row using the current query. The row at
	 * rowIndex will be located in the middle of the page.
	 * 
	 * @param rowIndex Integer
	 * @throws EJBExceptionLP
	 * @return QueryResult
	 */
	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;

		try {
			setInhalt();
			// jetzt die Darstellung in der Tabelle zusammenbauen
			int startIndex = 0;
			long endIndex = startIndex + getAnzahlZeilen() - 1;

			Object[][] rows = new Object[(int) getAnzahlZeilen()][getAnzahlSpalten()];

			for (int row = 0; row < hmDaten.size(); row++) {
				GutschriftUmsatzTabelleDto oReUebersichtDto = (GutschriftUmsatzTabelleDto) hmDaten.get(row);
				rows[row][RechnungFac.FLR_SPALTE_GS_UMSATZ_I_ID] = "";
				rows[row][RechnungFac.FLR_SPALTE_GS_UMSATZ_HEADER] = oReUebersichtDto.getSZeilenHeader();
				rows[row][RechnungFac.FLR_SPALTE_GS_UMSATZ_OFFEN_BRUTTO] = oReUebersichtDto.getBdOffeneBrutto();
				rows[row][RechnungFac.FLR_SPALTE_GS_UMSATZ_OFFEN_NETTO] = oReUebersichtDto.getBdOffeneNetto();
				rows[row][RechnungFac.FLR_SPALTE_GS_UMSATZ_LEER] = oReUebersichtDto.getSEmpty();
				rows[row][RechnungFac.FLR_SPALTE_GS_UMSATZ_UMSATZ_BRUTTO] = oReUebersichtDto.getBdUmsatzBrutto();
				rows[row][RechnungFac.FLR_SPALTE_GS_UMSATZ_UMSATZ_NETTO] = oReUebersichtDto.getBdUmsatzNetto();

			}
			result = new QueryResult(rows, getRowCount(), startIndex, endIndex, 0);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return result;
	}
}
