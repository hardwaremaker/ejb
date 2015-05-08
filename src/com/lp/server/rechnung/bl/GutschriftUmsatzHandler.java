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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.TreeMap;

import com.lp.server.auftrag.bl.UseCaseHandlerTabelle;
import com.lp.server.rechnung.service.GutschriftUmsatzTabelleDto;
import com.lp.server.rechnung.service.RechnungFac;
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
	private final int iAnzahlZeilen = 17;
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
			tableInfo = new TableInfo(new Class[] { String.class,String.class,
					BigDecimal.class, BigDecimal.class, String.class,
					BigDecimal.class, BigDecimal.class }, 
					new String[] { ""," ",
					getTextRespectUISpr("lp.brutto", mandantCNr, locUI),
					getTextRespectUISpr("lp.netto", mandantCNr, locUI), " ",
					getTextRespectUISpr("lp.brutto", mandantCNr, locUI),
					getTextRespectUISpr("lp.netto", mandantCNr, locUI) },
					new String[] {"", "", "", "", "", "", "" });
		}
		return this.tableInfo;
	}

	/**
	 * gets the data page for the specified row using the current query. The row
	 * at rowIndex will be located in the middle of the page.
	 * 
	 * @param rowIndex
	 *            Integer
	 * @throws EJBExceptionLP
	 * @return QueryResult
	 */
	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;

		try {
			getFilterKriterien();

			// enthaelt KRIT_BELEGDATUM | KRIT_LIEFERTERMIN | KRIT_FINALTERMIN =
			// vierstellige Jahreszahl
			FilterKriterium fkAuswertung = aFilterKriterium[RechnungFac.IDX_KRIT_JAHR];
			init(fkAuswertung);
			GregorianCalendar gcBerechnungsdatumVonI = null;
			GregorianCalendar gcBerechnungsdatumBisI = null;

			GutschriftUmsatzTabelleDto oSummenVorjahr = null;
			// zuerste eine HashMap mit den darzustellenden Daten zusammenbauen
			TreeMap<Integer, GutschriftUmsatzTabelleDto> hmSammelstelle = new TreeMap<Integer, GutschriftUmsatzTabelleDto>();

			for (int i = 0; i < iAnzahlZeilen; i++) {
				// die jeweiligen Spalten einer Zeile
				String sZeilenHeader = null;

				BigDecimal bdOffeneBrutto = null;
				BigDecimal bdOffeneNetto = null;

				BigDecimal bdUmsatzBrutto = null;
				BigDecimal bdUmsatzNetto = null;

				switch (i) {
				case 0:
					// in dieser Zeile stehen die Summen fuer das gesamte
					// Vorjahr
					sZeilenHeader = getTextRespectUISpr("lp.vorjahr",mandantCNr, locUI);
					sZeilenHeader += " ";
					sZeilenHeader += iVorjahr;
					// das Zeitintervall auf das gesamte Vorjahr festlegen
					gcBerechnungsdatumVonI = new GregorianCalendar(iVorjahr,iIndexBeginnMonat, 1);
					// vorjahr: der unterschied ist exakt 1 jahr -> +1
					gcBerechnungsdatumBisI = new GregorianCalendar(iVorjahr + 1,iIndexBeginnMonat, 1);
					break;
				case 1:
					// das ist eine Leerzeile zur optischen Trennung
					sZeilenHeader = null;
					break;
				case 14:
					// das ist eine Leerzeile zur optischen Trennung
					sZeilenHeader = null;
					break;
				case 15:
					sZeilenHeader = getTextRespectUISpr("lp.summe",mandantCNr, locUI);
					sZeilenHeader += " ";
					sZeilenHeader += fkAuswertung.value;
					// das Zeitintervall auf das gesamte laufende Jahr festlegen
					gcBerechnungsdatumVonI = new GregorianCalendar(iJahr, iIndexBeginnMonat, 1);
					gcBerechnungsdatumBisI = new GregorianCalendar(iJahr + 1, iIndexBeginnMonat, 1);
					break;
				case 16:
					// Zeile 16 enthaelt die Summe aus gesamtem laufenden Jahr plus
					// Summe des Vorjahres
					sZeilenHeader = getTextRespectUISpr("lp.gesamtsumme",mandantCNr, locUI);
					// das Zeitintervall auf alle erfassten Auftraege festlegen
					// IMS 749 Summe ueber saemtliche Auftraege
					gcBerechnungsdatumVonI = new GregorianCalendar(1900,iIndexBeginnMonat, 1);
					gcBerechnungsdatumBisI = new GregorianCalendar(iJahr + 1,iIndexBeginnMonat, 1);
					break;
				default:
					sZeilenHeader = aMonatsnamen[iCurrentMonat];
					// das Zeitintervall auf das laufende Monat festlegen
					gcBerechnungsdatumVonI = new GregorianCalendar(iCurrentJahr, iCurrentMonat, 1);
					// dieser Zeitpunkt liegt 1 Monat nach dem Von Datum
					gcBerechnungsdatumBisI = berechneNaechstesZeitintervall(iCurrentMonat, iCurrentJahr);
					iCurrentJahr = gcBerechnungsdatumBisI.get(GregorianCalendar.YEAR);
					iCurrentMonat = gcBerechnungsdatumBisI.get(GregorianCalendar.MONTH);
					break;
				}

				// den offenen Bruttowert bestimmen
				bdOffeneBrutto = getRechnungFac()
						.berechneSummeGutschriftOffenBrutto(
								theClientDto.getMandant(),
								fkAuswertung.kritName, gcBerechnungsdatumVonI,
								gcBerechnungsdatumBisI, theClientDto);

				// den offenen Nettowert bestimmen
				bdOffeneNetto = getRechnungFac()
						.berechneSummeGutschriftOffenNetto(
								theClientDto.getMandant(),
								fkAuswertung.kritName, gcBerechnungsdatumVonI,
								gcBerechnungsdatumBisI, theClientDto);

				// den Umsatz brutto bestimmen
				bdUmsatzBrutto = getRechnungFac()
						.berechneSummeGutschriftUmsatzBrutto(
								theClientDto.getMandant(),
								fkAuswertung.kritName, gcBerechnungsdatumVonI,
								gcBerechnungsdatumBisI, theClientDto);

				// den Umsatz netto bestimmen
				bdUmsatzNetto = getRechnungFac()
						.berechneSummeGutschriftUmsatzNetto(
								theClientDto.getMandant(),
								fkAuswertung.kritName, gcBerechnungsdatumVonI,
								gcBerechnungsdatumBisI, theClientDto);

				// die Zeilen fuer die Anzeige zusammenbauen
				GutschriftUmsatzTabelleDto oErUebersichtDto = new GutschriftUmsatzTabelleDto();
				if (sZeilenHeader != null) {
					oErUebersichtDto.setSZeilenheader(sZeilenHeader);
					oErUebersichtDto.setBdOffeneBrutto(bdOffeneBrutto);
					oErUebersichtDto.setBdOffeneNetto(bdOffeneNetto);
					oErUebersichtDto.setBdUmsatzBrutto(bdUmsatzBrutto);
					oErUebersichtDto.setBdUmsatzNetto(bdUmsatzNetto);
				}

				// die Summen des Vorjahrs muss man sich fuer spaeter merken
				if (i == IDX_SUMMEN_VORJAHR) {
					oSummenVorjahr = oErUebersichtDto;
				}
				// die Gesamtsummen bestehen aus den Summen des laufenden Jahres
				// und des Vorjahres
				if (i == IDX_SUMMEN_GESAMT) {
					oErUebersichtDto.setBdOffeneBrutto(bdOffeneBrutto);
					oErUebersichtDto.setBdOffeneNetto(bdOffeneNetto);
					oErUebersichtDto.setBdUmsatzBrutto(Helper.getBigDecimalNull());
					oErUebersichtDto.setBdUmsatzNetto(Helper.getBigDecimalNull());
				}

				hmSammelstelle.put(new Integer(i), oErUebersichtDto);
			}

			// jetzt die Darstellung in der Tabelle zusammenbauen
			int startIndex = 0;
			long endIndex = startIndex + getAnzahlZeilen() - 1;

			Object[][] rows = new Object[(int) getAnzahlZeilen()][getAnzahlSpalten()];
			int row = 0;

			Collection<GutschriftUmsatzTabelleDto> clSichtLieferstatus = hmSammelstelle.values();
			Iterator<GutschriftUmsatzTabelleDto> it = clSichtLieferstatus.iterator();

			while (it.hasNext()) {
				GutschriftUmsatzTabelleDto oReUebersichtDto = (GutschriftUmsatzTabelleDto) it
						.next();
				rows[row][RechnungFac.FLR_SPALTE_GS_UMSATZ_I_ID] = "";
				rows[row][RechnungFac.FLR_SPALTE_GS_UMSATZ_HEADER] = oReUebersichtDto
						.getSZeilenHeader();
				rows[row][RechnungFac.FLR_SPALTE_GS_UMSATZ_OFFEN_BRUTTO] = oReUebersichtDto
						.getBdOffeneBrutto();
				rows[row][RechnungFac.FLR_SPALTE_GS_UMSATZ_OFFEN_NETTO] = oReUebersichtDto
						.getBdOffeneNetto();
				rows[row][RechnungFac.FLR_SPALTE_GS_UMSATZ_LEER] = oReUebersichtDto
						.getSEmpty();
				rows[row][RechnungFac.FLR_SPALTE_GS_UMSATZ_UMSATZ_BRUTTO] = oReUebersichtDto
						.getBdUmsatzBrutto();
				rows[row][RechnungFac.FLR_SPALTE_GS_UMSATZ_UMSATZ_NETTO] = oReUebersichtDto
						.getBdUmsatzNetto();
				row++;
			}
			result = new QueryResult(rows, getRowCount(), startIndex, endIndex,
					0);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return result;
	}
}
