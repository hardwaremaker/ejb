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
package com.lp.server.auftrag.bl;

import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.TreeMap;

import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragUebersichtTabelleDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.UmsatzUseCaseHandlerTabelle;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * Handler fuer Uebersichtstabelle im Auftrag. <br>
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
 * @author Uli Walch
 * @version 1.0
 */

public class AuftragUebersichtHandler extends UmsatzUseCaseHandlerTabelle {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int iAnzahlZeilen = 17;
	private final int iAnzahlSpalten = 9;

	/**
	 * Konstruktor.
	 */
	public AuftragUebersichtHandler() {
		super();

		try {
			setAnzahlZeilen(iAnzahlZeilen);
			setAnzahlSpalten(iAnzahlSpalten);
		} catch (Throwable t) {
			// @todo was tun ??? PJ 3818
		}
	}

	public TableInfo getTableInfo() {
		if (tableInfo == null) {
			tableInfo = new TableInfo(new Class[] { String.class,String.class,
					BigDecimal.class, BigDecimal.class, BigDecimal.class,
					String.class, BigDecimal.class, BigDecimal.class,
					BigDecimal.class }, new String[] {" ",
					" ",
					getTextRespectUISpr("auft.freieauftraege", theClientDto
							.getMandant(), theClientDto.getLocUi()),
					getTextRespectUISpr("auft.abrufautraege", theClientDto
							.getMandant(), theClientDto.getLocUi()),
					getTextRespectUISpr("auft.rahmenauftraege", theClientDto
							.getMandant(), theClientDto.getLocUi()),
					" ",
					getTextRespectUISpr("auft.freieauftraege", theClientDto
							.getMandant(), theClientDto.getLocUi()),
					getTextRespectUISpr("auft.abrufautraege", theClientDto
							.getMandant(), theClientDto.getLocUi()),
					getTextRespectUISpr("auft.rahmenauftraege", theClientDto
							.getMandant(), theClientDto.getLocUi()) },
					new String[] {"", "", "", "", "", "", "", "", "" });
		}

		return tableInfo;
	}

	/**
	 * gets the data page for the specified row using the current query. The row
	 * at rowIndex will be located in the middle of the page.
	 * 
	 * @param rowIndex
	 *            die markierte Zeile
	 * @return QueryResult Ergebnis
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;

		try {
			// ptkrit: 3 die aktuellen Filter Kriterien bestimmen
			getFilterKriterien();

			// enthaelt KRIT_BELEGDATUM | KRIT_LIEFERTERMIN | KRIT_FINALTERMIN =
			// vierstelliges Geschaeftsjahr
			FilterKriterium fkAuswertung = aFilterKriterium[AuftragFac.UMSATZUEBERSICHT_IDX_KRIT_AUSWERTUNG];
			init(aFilterKriterium[AuftragFac.UMSATZUEBERSICHT_IDX_KRIT_JAHR]);
			AuftragUebersichtTabelleDto oSummenVorjahr = null;
			// zuerste eine HashMap mit den darzustellenden Daten zusammenbauen
			TreeMap<Integer, AuftragUebersichtTabelleDto> hmSammelstelle = new TreeMap<Integer, AuftragUebersichtTabelleDto>();
			for (int i = 0; i < iAnzahlZeilen; i++) {
				String sZeilenHeader = null;
				GregorianCalendar gcBerechnungsdatumVonI = null;
				GregorianCalendar gcBerechnungsdatumBisI = null;
				BigDecimal bdOffeneFreie = new BigDecimal(0);
				BigDecimal bdOffeneAbruf = new BigDecimal(0);
				BigDecimal bdOffeneRahmen = new BigDecimal(0);
				BigDecimal bdEingangFreie = new BigDecimal(0);
				BigDecimal bdEingangAbruf = new BigDecimal(0);
				BigDecimal bdEingangRahmen = new BigDecimal(0);
				switch (i) {
				// in Zeile 0 stehen die Summen fuer das gesamte Vorjahr
				case 0:
					sZeilenHeader = getTextRespectUISpr("lp.vorjahr", theClientDto.getMandant(), theClientDto.getLocUi());
					sZeilenHeader += " ";
					sZeilenHeader += iVorjahr;
					// das Zeitintervall auf das gesamte Vorjahr festlegen
					gcBerechnungsdatumVonI = new GregorianCalendar(iVorjahr,iIndexBeginnMonat, 1);
					gcBerechnungsdatumBisI = new GregorianCalendar(iJahr,iIndexBeginnMonat, 1);
					break;
				// Zeile 1 ist eine Leerzeile zur otpischen Trennung
				case 1:
					sZeilenHeader = null;
					break;
				// Zeile 14 ist eine Leerzeile zur optischen Trennung
				case 14:
					sZeilenHeader = null;
					break;
				// Zeile 15 enthaelt die Summe ueber das gesamte laufende
				// Geschaeftsjahr
				case 15:
					sZeilenHeader = getTextRespectUISpr("lp.summe", theClientDto.getMandant(), theClientDto.getLocUi());
					sZeilenHeader += " ";
					sZeilenHeader += fkAuswertung.value;
					// das Zeitintervall auf das gesamte laufende Jahr festlegen
					gcBerechnungsdatumVonI = new GregorianCalendar(iJahr,iIndexBeginnMonat, 1);
					gcBerechnungsdatumBisI = new GregorianCalendar(iJahr + 1,iIndexBeginnMonat, 1);
					break;
				// Zeile 16 enthaelt die Summe aus gesamtem laufenden Jahr plus
				// Summe des Vorjahres
				case 16:
					sZeilenHeader = getTextRespectUISpr("lp.gesamtsumme",theClientDto.getMandant(), theClientDto.getLocUi());
					// das Zeitintervall auf alle erfassten Auftraege festlegen
					// IMS 749 Summe ueber saemtliche Auftraege
					gcBerechnungsdatumVonI = new GregorianCalendar(1900,iIndexBeginnMonat, 1);
					gcBerechnungsdatumBisI = new GregorianCalendar(iJahr + 1,iIndexBeginnMonat, 1);
					break;
				// Zeile 2 bis 13 enthaelt die Summe fuer das jeweilige Monat
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

				if (gcBerechnungsdatumVonI != null
						&& gcBerechnungsdatumBisI != null) {

					// den offenen Nettoauftragswert fuer Auftragart FREI
					// bestimmen
					bdOffeneFreie = getAuftragFac()
							.berechneSummeAuftragsnettowert(
									AuftragServiceFac.AUFTRAGART_FREI,
									fkAuswertung.kritName,
									gcBerechnungsdatumVonI,
									gcBerechnungsdatumBisI,
									AuftragFac.AUFT_UMSATZUEBERSICHT_OFFEN,
									theClientDto);

					// den offenen Nettoauftragswert fuer Auftragart ABRUF
					// bestimmen
					bdOffeneAbruf = getAuftragFac()
							.berechneSummeAuftragsnettowert(
									AuftragServiceFac.AUFTRAGART_ABRUF,
									fkAuswertung.kritName,
									gcBerechnungsdatumVonI,
									gcBerechnungsdatumBisI,
									AuftragFac.AUFT_UMSATZUEBERSICHT_OFFEN,
									theClientDto);

					// den offenen Nettoauftragswert fuer Auftragart RAHMEN
					// bestimmen
					bdOffeneRahmen = getAuftragFac()
							.berechneSummeAuftragsnettowert(
									AuftragServiceFac.AUFTRAGART_RAHMEN,
									fkAuswertung.kritName,
									gcBerechnungsdatumVonI,
									gcBerechnungsdatumBisI,
									AuftragFac.AUFT_UMSATZUEBERSICHT_OFFEN,
									theClientDto);

					// den gesamten Nettoauftragswert fuer Auftragart FREI
					// bestimmen
					bdEingangFreie = getAuftragFac()
							.berechneSummeAuftragsnettowert(
									AuftragServiceFac.AUFTRAGART_FREI,
									fkAuswertung.kritName,
									gcBerechnungsdatumVonI,
									gcBerechnungsdatumBisI,
									AuftragFac.AUFT_UMSATZUEBERSICHT_EINGANG,
									theClientDto);

					// die Summe fuer Auftragart ABRUF und Auftragstatus OFFEN
					// bestimmen
					bdEingangAbruf = getAuftragFac()
							.berechneSummeAuftragsnettowert(
									AuftragServiceFac.AUFTRAGART_ABRUF,
									fkAuswertung.kritName,
									gcBerechnungsdatumVonI,
									gcBerechnungsdatumBisI,
									AuftragFac.AUFT_UMSATZUEBERSICHT_EINGANG,
									theClientDto);

					// die Summe fuer Auftragart RAHMEN und Auftragstatus OFFEN
					// bestimmen
					bdEingangRahmen = getAuftragFac()
							.berechneSummeAuftragsnettowert(
									AuftragServiceFac.AUFTRAGART_RAHMEN,
									fkAuswertung.kritName,
									gcBerechnungsdatumVonI,
									gcBerechnungsdatumBisI,
									AuftragFac.AUFT_UMSATZUEBERSICHT_EINGANG,
									theClientDto);
				}

				// die Zeilen fuer die Anzeige zusammenbauen
				AuftragUebersichtTabelleDto oAuftragUebersichtDto = new AuftragUebersichtTabelleDto();

				if (sZeilenHeader != null) {
					oAuftragUebersichtDto.setSZeilenheader(sZeilenHeader);
					oAuftragUebersichtDto.setBdAbrufauftraege1(bdOffeneAbruf);
					oAuftragUebersichtDto.setBdFreieauftraege1(bdOffeneFreie);
					oAuftragUebersichtDto.setBdRahmenauftrage1(bdOffeneRahmen);
					// oAuftragUebersichtDto.setSEmpty(null) // ist bereits
					// ueber Dto gesetzt
					oAuftragUebersichtDto.setBdAbrufauftraege2(bdEingangAbruf);
					oAuftragUebersichtDto.setBdFreieauftraege2(bdEingangFreie);
					oAuftragUebersichtDto.setBdRahmenauftrage2(bdEingangRahmen);
				}

				// die Summen des Vorjahrs muss man sich fuer spaeter merken
				if (i == IDX_SUMMEN_VORJAHR) {
					oSummenVorjahr = oAuftragUebersichtDto;
				}

				// die Gesamtsummen bestehen aus den Summen des laufenden Jahres
				// und des Vorjahres
				if (i == IDX_SUMMEN_GESAMT) {
					// IMS 749 linke Spalte Offenen enthaelt die Summe ueber
					// alle Auftraege im System
					oAuftragUebersichtDto
							.setBdAbrufauftraege1(oAuftragUebersichtDto
									.getBdAbrufauftraege1());
					oAuftragUebersichtDto
							.setBdFreieauftraege1(oAuftragUebersichtDto
									.getBdFreieauftraege1());
					oAuftragUebersichtDto
							.setBdRahmenauftrage1(oAuftragUebersichtDto
									.getBdRahmenauftrage1());

					// IMS 749 rechte Spalte Eingang enthaelt 0.00, weil die
					// Anzeige der Gesamtsumme keinen Sinn macht
					oAuftragUebersichtDto.setBdAbrufauftraege2(Helper
							.getBigDecimalNull());
					oAuftragUebersichtDto.setBdFreieauftraege2(Helper
							.getBigDecimalNull());
					oAuftragUebersichtDto.setBdRahmenauftrage2(Helper
							.getBigDecimalNull());
				}

				hmSammelstelle.put(new Integer(i), oAuftragUebersichtDto);
			}

			// jetzt die Darstellung in der Tabelle zusammenbauen
			int startIndex = 0;
			long endIndex = startIndex + getAnzahlZeilen() - 1;

			Object[][] rows = new Object[(int) getAnzahlZeilen()][getAnzahlSpalten()];
			int row = 0;
			int col = 0;

			Collection<AuftragUebersichtTabelleDto> clSichtLieferstatus = hmSammelstelle.values();
			Iterator<AuftragUebersichtTabelleDto> it = clSichtLieferstatus.iterator();

			while (it.hasNext()) {
				AuftragUebersichtTabelleDto oAuftragUebersichtDto = (AuftragUebersichtTabelleDto) it
						.next();
				rows[row][col++] = "";
				rows[row][col++] = oAuftragUebersichtDto.getSZeilenHeader();
				rows[row][col++] = oAuftragUebersichtDto.getBdFreieauftraege1();
				rows[row][col++] = oAuftragUebersichtDto.getBdAbrufauftraege1();
				rows[row][col++] = oAuftragUebersichtDto.getBdRahmenauftrage1();
				rows[row][col++] = oAuftragUebersichtDto.getSEmpty();
				rows[row][col++] = oAuftragUebersichtDto.getBdFreieauftraege2();
				rows[row][col++] = oAuftragUebersichtDto.getBdAbrufauftraege2();
				rows[row++][col++] = oAuftragUebersichtDto
						.getBdRahmenauftrage2();

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
