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
package com.lp.server.eingangsrechnung.bl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.TreeMap;

import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungUebersichtTabelleDto;
import com.lp.server.util.UmsatzUseCaseHandlerTabelle;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * Handler fuer Uebersichtstabelle in der ER. <br>
 * Diese Handlerklasse geht nicht ueber Hibernate.
 * <p>
 * Copright Logistik Pur GmbH (c) 2005
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

public class EingangsrechnungUebersichtHandler extends
		UmsatzUseCaseHandlerTabelle {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int iAnzahlZeilen = 17;
	private final int iAnzahlSpalten = 7;

	/**
	 * Konstruktor.
	 */
	public EingangsrechnungUebersichtHandler() {
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
			tableInfo = new TableInfo(new Class[] { String.class, String.class,
					BigDecimal.class, BigDecimal.class, String.class,
					BigDecimal.class, BigDecimal.class },

			new String[] { "", "  ",
					getTextRespectUISpr("lp.brutto", mandantCNr, locUI),
					getTextRespectUISpr("lp.netto", mandantCNr, locUI), "  ",
					getTextRespectUISpr("lp.brutto", mandantCNr, locUI),
					getTextRespectUISpr("lp.netto", mandantCNr, locUI) },
					new String[] { "", "", "", "", "", "", "" });
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
			FilterKriterium fkDatum = aFilterKriterium[EingangsrechnungFac.IDX_KRIT_DATUM];
			FilterKriterium fkJahr = aFilterKriterium[EingangsrechnungFac.IDX_KRIT_JAHR];

			FilterKriterium fkZusatzkosten = aFilterKriterium[EingangsrechnungFac.IDX_KRIT_ZUSATZKOSTEN];
			boolean bZusatzkosten = Helper.short2boolean(new Short(
					fkZusatzkosten.value));
			init(fkJahr);

			String waehrung = theClientDto.getSMandantenwaehrung();

			GregorianCalendar gcBerechnungsdatumVonI = null;
			GregorianCalendar gcBerechnungsdatumBisI = null;

			BigDecimal bdOffeneBruttoGesamt = new BigDecimal(0);
			BigDecimal bdOffeneNettoGesamt = new BigDecimal(0);

			BigDecimal bdUmsatzBruttoGesamt = new BigDecimal(0);
			BigDecimal bdUmsatzNettoGesamt = new BigDecimal(0);

			EingangsrechnungUebersichtTabelleDto oSummenVorjahr = null;

			// zuerste eine HashMap mit den darzustellenden Daten zusammenbauen
			TreeMap<Integer, EingangsrechnungUebersichtTabelleDto> hmSammelstelle = new TreeMap<Integer, EingangsrechnungUebersichtTabelleDto>();

			for (int i = 0; i < iAnzahlZeilen; i++) {
				// die jeweiligen Spalten einer Zeile
				String sZeilenHeader = null;
				BigDecimal bdOffeneBrutto = null;
				BigDecimal bdOffeneNetto = null;
				BigDecimal bdUmsatzBrutto = null;
				BigDecimal bdUmsatzNetto = null;
				// die Zeilen fuer die Anzeige zusammenbauen
				EingangsrechnungUebersichtTabelleDto oErUebersichtDto = new EingangsrechnungUebersichtTabelleDto();
				switch (i) {
				case 0:
					// in dieser Zeile stehen die Summen fuer das gesamte
					// Vorjahr
					sZeilenHeader = getTextRespectUISpr("lp.vorjahr",
							mandantCNr, locUI);
					sZeilenHeader += " ";
					sZeilenHeader += iVorjahr;
					// das Zeitintervall auf das gesamte Vorjahr festlegen
					gcBerechnungsdatumVonI = new GregorianCalendar(iVorjahr,
							iIndexBeginnMonat, 1);
					// vorjahr: der unterschied ist exakt 1 jahr -> +1
					gcBerechnungsdatumBisI = new GregorianCalendar(
							iVorjahr + 1, iIndexBeginnMonat, 1);
					break;
				case 1:
					// das ist eine Leerzeile zur optischen Trennung
					sZeilenHeader = null;
					hmSammelstelle.put(new Integer(i), oErUebersichtDto);
					continue;
				case 14:
					// das ist eine Leerzeile zur optischen Trennung
					sZeilenHeader = null;
					hmSammelstelle.put(new Integer(i), oErUebersichtDto);
					continue;
				case 15:
					sZeilenHeader = getTextRespectUISpr("lp.summe", mandantCNr,
							locUI);
					sZeilenHeader += " ";
					sZeilenHeader += fkDatum.value;
					// das Zeitintervall auf das gesamte laufende Jahr festlegen
					gcBerechnungsdatumVonI = new GregorianCalendar(iJahr,
							iIndexBeginnMonat, 1);
					gcBerechnungsdatumBisI = new GregorianCalendar(iJahr + 1,
							iIndexBeginnMonat, 1);
					break;
				case 16:
					/*
					 * sZeilenHeader = getTextRespectUISpr("lp.gesamtsumme",
					 * theClientDto.getMandant(), theClientDto.getLocUi());
					 * 
					 * oErUebersichtDto.setSZeilenheader(sZeilenHeader);
					 * oErUebersichtDto.setBdOffenBrutto(bdOffeneBruttoGesamt);
					 * oErUebersichtDto.setBdOffenNetto(bdOffeneNettoGesamt);
					 * oErUebersichtDto.setBdUmsatzBrutto(bdUmsatzNettoGesamt);
					 * oErUebersichtDto.setBdUmsatzNetto(bdUmsatzBruttoGesamt);
					 * hmSammelstelle.put(new Integer(i), oErUebersichtDto);
					 */
					continue;
				default:
					sZeilenHeader = aMonatsnamen[iCurrentMonat];
					// das Zeitintervall auf das laufende Monat festlegen
					gcBerechnungsdatumVonI = new GregorianCalendar(
							iCurrentJahr, iCurrentMonat, 1);
					// dieser Zeitpunkt liegt 1 Monat nach dem Von Datum
					gcBerechnungsdatumBisI = berechneNaechstesZeitintervall(
							iCurrentMonat, iCurrentJahr);
					iCurrentJahr = gcBerechnungsdatumBisI
							.get(GregorianCalendar.YEAR);

					iCurrentMonat = gcBerechnungsdatumBisI
							.get(GregorianCalendar.MONTH);
					break;

				}

				// den offenen Bruttowert bestimmen
				bdOffeneBrutto = getEingangsrechnungFac()
						.berechneSummeOffenBruttoInMandantenwaehrung(
								theClientDto, fkDatum.kritName,
								fkJahr.kritName, gcBerechnungsdatumVonI,
								gcBerechnungsdatumBisI, bZusatzkosten);

				bdOffeneBruttoGesamt = bdOffeneBruttoGesamt.add(bdOffeneBrutto);

				// den offenen Nettowert bestimmen
				bdOffeneNetto = getEingangsrechnungFac()
						.berechneSummeOffenNettoInMandantenwaehrung(
								theClientDto, fkDatum.kritName,
								fkJahr.kritName, gcBerechnungsdatumVonI,
								gcBerechnungsdatumBisI, bZusatzkosten);

				bdOffeneNettoGesamt = bdOffeneNettoGesamt.add(bdOffeneNetto);

				// den Umsatz brutto bestimmen
				bdUmsatzBrutto = getEingangsrechnungFac()
						.berechneSummeUmsatzBrutto(theClientDto,
								fkDatum.kritName, fkJahr.kritName, waehrung,
								gcBerechnungsdatumVonI, gcBerechnungsdatumBisI,
								bZusatzkosten);

				bdUmsatzBruttoGesamt = bdUmsatzBruttoGesamt.add(bdUmsatzBrutto);

				// den Umsatz netto bestimmen
				bdUmsatzNetto = getEingangsrechnungFac()
						.berechneSummeUmsatzNetto(theClientDto,
								fkDatum.kritName, fkJahr.kritName, waehrung,
								gcBerechnungsdatumVonI, gcBerechnungsdatumBisI,
								bZusatzkosten);

				bdUmsatzNettoGesamt = bdUmsatzNettoGesamt.add(bdUmsatzNetto);

				if (sZeilenHeader != null) {
					oErUebersichtDto.setSZeilenheader(sZeilenHeader);
					oErUebersichtDto.setBdOffenBrutto(bdOffeneBrutto);
					oErUebersichtDto.setBdOffenNetto(bdOffeneNetto);
					// oAuftragUebersichtDto.setSEmpty(null) // ist bereits
					// ueber Dto gesetzt
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
					oErUebersichtDto.setSZeilenheader(sZeilenHeader);
					oErUebersichtDto.setBdOffenBrutto(bdOffeneBrutto);
					oErUebersichtDto.setBdOffenNetto(bdOffeneNetto);
					oErUebersichtDto.setBdUmsatzBrutto(Helper
							.getBigDecimalNull());
					oErUebersichtDto.setBdUmsatzNetto(Helper
							.getBigDecimalNull());
				}

				hmSammelstelle.put(new Integer(i), oErUebersichtDto);
			}

			// jetzt die Darstellung in der Tabelle zusammenbauen
			int startIndex = 0;
			long endIndex = startIndex + getAnzahlZeilen() - 1;

			Object[][] rows = new Object[(int) getAnzahlZeilen()][getAnzahlSpalten()];
			int row = 0;
			int col = 0;

			Collection<EingangsrechnungUebersichtTabelleDto> clSichtLieferstatus = hmSammelstelle
					.values();
			Iterator<EingangsrechnungUebersichtTabelleDto> it = clSichtLieferstatus
					.iterator();

			while (it.hasNext()) {
				EingangsrechnungUebersichtTabelleDto oErUebersichtDto = (EingangsrechnungUebersichtTabelleDto) it
						.next();
				rows[row][col++] = "";
				rows[row][col++] = oErUebersichtDto.getSZeilenHeader();
				rows[row][col++] = oErUebersichtDto.getBdOffenBrutto();
				rows[row][col++] = oErUebersichtDto.getBdOffenNetto();
				rows[row][col++] = oErUebersichtDto.getSEmpty();
				rows[row][col++] = oErUebersichtDto.getBdUmsatzBrutto();
				rows[row][col++] = oErUebersichtDto.getBdUmsatzNetto();
				row++;
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
