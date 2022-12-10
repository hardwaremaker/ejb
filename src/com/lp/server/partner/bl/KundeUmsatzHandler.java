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
package com.lp.server.partner.bl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.TreeMap;

import com.lp.server.auftrag.bl.UseCaseHandlerTabelle;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.KundeUmsatzTabelleDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.EJBExceptionLP;

/**
 * Handler fuer Umsatztabelle im Kunden. <br>
 * Diese Handlerklasse geht nicht ueber Hibernate.
 * <p>
 * Copright Logistik Pur GmbH (c) 2005
 * </p>
 * <p>
 * Erstellungsdatum 2005-05-11
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version 1.0
 */

public class KundeUmsatzHandler extends UseCaseHandlerTabelle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int iAnzahlZeilen = 14;
	private final int iAnzahlSpalten = 4;

	/**
	 * Konstruktor.
	 */
	public KundeUmsatzHandler() {
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
			tableInfo = new TableInfo(new Class[] { Object.class, String.class, BigDecimal.class, Integer.class },
					new String[] { "  ",getTextRespectUISpr("lp.jahr", mandantCNr, locUI),
							getTextRespectUISpr("rechnung.umsatz", mandantCNr, locUI),
							getTextRespectUISpr("kunde.gelegterechnungen", mandantCNr, locUI) },
					new int[] { QueryParameters.FLR_BREITE_SHARE_WITH_REST, // diese Spalte wird
							// ausgeblendet
							QueryParameters.FLR_BREITE_M, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST },
					new String[] { "", "", "", "" });
		}
		return tableInfo;
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
		String mandantCNr = theClientDto.getMandant();
		Locale locUI = theClientDto.getLocUi();

		try {
			getFilterKriterien();

			FilterKriterium fkAuswertung = aFilterKriterium[KundeFac.IDX_KRIT_KUNDE_I_ID];

			FilterKriterium fkJahr = aFilterKriterium[KundeFac.IDX_KRIT_JAHR];

			boolean bGerschaeftsjahr = false;
			if (fkJahr.kritName.equals(RechnungFac.KRIT_JAHR_GESCHAEFTSJAHR)) {
				bGerschaeftsjahr = true;
			}

			Integer kundeIId = new Integer(fkAuswertung.value);
			int iLaufendesJahr = new GregorianCalendar().get(Calendar.YEAR);

			if (bGerschaeftsjahr) {
				iLaufendesJahr = getBuchenFac().findGeschaeftsjahrFuerDatum(
						new java.sql.Date(System.currentTimeMillis()), theClientDto.getMandant());

			}

			GregorianCalendar gcBerechnungsdatumVonI = null;
			GregorianCalendar gcBerechnungsdatumBisI = null;

			/*
			 * KundeDto kundeDto=getKundeFac().kundeFindByPrimaryKey(kundeIId,
			 * theClientDto);
			 * 
			 * //Kunde in Header einbauen:
			 * tableInfo.getColumnHeaderValues()[0]=kundeDto.getPartnerDto().formatAnrede();
			 */

			// zuerste eine HashMap mit den darzustellenden Daten zusammenbauen
			TreeMap<Integer, KundeUmsatzTabelleDto> hmSammelstelle = new TreeMap<Integer, KundeUmsatzTabelleDto>();

			for (int i = 0; i < iAnzahlZeilen; i++) {
				// die jeweiligen Spalten einer Zeile
				String sZeilenHeader = null;

				BigDecimal bdUmsatz = null;
				Integer iAnzahlRechnungen = null;

				if (bGerschaeftsjahr) {

					switch (i) {

					case 0:

						GregorianCalendar[] gcVonBis = getVorjahr(iLaufendesJahr, -9, true);
						// in dieser Zeile stehen die Summen fuer das gesamte
						// Vorjahr
						sZeilenHeader = getTextRespectUISpr("lp.davor", mandantCNr, locUI);

						// das Zeitintervall auf 1900 bis vor 10 jahren
						gcBerechnungsdatumVonI = new GregorianCalendar(1900, GregorianCalendar.JANUARY, 1);
						gcBerechnungsdatumBisI = gcVonBis[1];
						break;

					case 1:

						// das ist eine Leerzeile zur optischen Trennung
						sZeilenHeader = null;
						break;

					case 2:

						sZeilenHeader = (iLaufendesJahr - 9) + "";

						gcVonBis = getVorjahr(iLaufendesJahr-8, 0, true);

						gcBerechnungsdatumVonI = gcVonBis[0];

						// vorjahr: der erste des naechten monats (fuer alle
						// monate!!!)
						gcBerechnungsdatumBisI = gcVonBis[1];

						break;

					case 3:
						sZeilenHeader = (iLaufendesJahr - 8) + "";

						gcVonBis = getVorjahr(iLaufendesJahr-7, 0, true);

						gcBerechnungsdatumVonI = gcVonBis[0];
						gcBerechnungsdatumBisI = gcVonBis[1];

						break;

					case 4:
						sZeilenHeader = (iLaufendesJahr - 7) + "";

						gcVonBis = getVorjahr(iLaufendesJahr-6, 0, true);

						gcBerechnungsdatumVonI = gcVonBis[0];
						gcBerechnungsdatumBisI = gcVonBis[1];

						break;

					case 5:
						sZeilenHeader = (iLaufendesJahr - 6) + "";

						gcVonBis = getVorjahr(iLaufendesJahr-5, 0, true);

						gcBerechnungsdatumVonI = gcVonBis[0];
						gcBerechnungsdatumBisI = gcVonBis[1];

						break;

					case 6:
						sZeilenHeader = (iLaufendesJahr - 5) + "";
						gcVonBis = getVorjahr(iLaufendesJahr-4, 0, true);

						gcBerechnungsdatumVonI = gcVonBis[0];
						gcBerechnungsdatumBisI = gcVonBis[1];

						break;

					case 7:
						sZeilenHeader = (iLaufendesJahr - 4) + "";

						gcVonBis = getVorjahr(iLaufendesJahr-3, 0, true);

						gcBerechnungsdatumVonI = gcVonBis[0];
						gcBerechnungsdatumBisI = gcVonBis[1];

						break;

					case 8:
						sZeilenHeader = (iLaufendesJahr - 3) + "";

						gcVonBis = getVorjahr(iLaufendesJahr-2, 0, true);

						gcBerechnungsdatumVonI = gcVonBis[0];
						gcBerechnungsdatumBisI = gcVonBis[1];

						break;

					case 9:
						sZeilenHeader = (iLaufendesJahr - 2) + "";

						gcVonBis = getVorjahr(iLaufendesJahr-1, 0, true);

						gcBerechnungsdatumVonI = gcVonBis[0];
						gcBerechnungsdatumBisI = gcVonBis[1];

						break;

					case 10:
						sZeilenHeader = (iLaufendesJahr - 1) + "";

						gcVonBis = getVorjahr(iLaufendesJahr, 0, true);

						gcBerechnungsdatumVonI = gcVonBis[0];
						gcBerechnungsdatumBisI = gcVonBis[1];

						break;

					case 11:
						sZeilenHeader = iLaufendesJahr + "";

						gcVonBis = getAktuellesJahr(iLaufendesJahr, 0, true);

						gcBerechnungsdatumVonI = gcVonBis[0];
						gcBerechnungsdatumBisI = gcVonBis[1];

						break;

					case 12:

						// das ist eine Leerzeile zur optischen Trennung
						sZeilenHeader = null;
						break;

					case 13:
						sZeilenHeader = getTextRespectUISpr("lp.summe", mandantCNr, locUI);

						// das Zeitintervall auf das gesamte laufende Jahr festlegen
						gcBerechnungsdatumVonI = new GregorianCalendar(iLaufendesJahr, GregorianCalendar.JANUARY, 1);
						gcBerechnungsdatumBisI = new GregorianCalendar(iLaufendesJahr + 1, GregorianCalendar.JANUARY,
								1);

						// Alle vorherigen Zeilen summieren
						KundeUmsatzTabelleDto kundeUmsatzDto = new KundeUmsatzTabelleDto();

						BigDecimal umsatzGesamt = new BigDecimal(0);
						int rechnungenGesamt = 0;
						for (int j = 0; j < hmSammelstelle.size(); j++) {
							KundeUmsatzTabelleDto temp = hmSammelstelle.get(j);

							if (temp.getBdUmsatz() != null) {
								umsatzGesamt = umsatzGesamt.add(temp.getBdUmsatz());
							}

							if (temp.getIAnzahlRechnungen() != null) {
								rechnungenGesamt += temp.getIAnzahlRechnungen();
							}
						}

						kundeUmsatzDto.setSZeilenheader(sZeilenHeader);
						kundeUmsatzDto.setBdUmsatz(umsatzGesamt);
						kundeUmsatzDto.setIAnzahlRechnungen(rechnungenGesamt);
						hmSammelstelle.put(new Integer(i), kundeUmsatzDto);

						break;

					}

					if (i != 13) {

						java.sql.Date dVon = new java.sql.Date(gcBerechnungsdatumVonI.getTime().getTime());
						java.sql.Date dBis = new java.sql.Date(gcBerechnungsdatumBisI.getTime().getTime());

						// den Umsatz errechnen
						bdUmsatz = getRechnungFac().getUmsatzVomKundenImZeitraum(theClientDto, kundeIId, dVon, dBis,
								false);

						// die Anzahl der rechnungen errechnen
						iAnzahlRechnungen = getRechnungFac().getAnzahlDerRechnungenVomKundenImZeitraum(theClientDto,
								kundeIId, dVon, dBis, false);

						// die Zeilen fuer die Anzeige zusammenbauen
						KundeUmsatzTabelleDto kundeUmsatzDto = new KundeUmsatzTabelleDto();
						if (sZeilenHeader != null) {
							kundeUmsatzDto.setSZeilenheader(sZeilenHeader);
							kundeUmsatzDto.setBdUmsatz(bdUmsatz);
							kundeUmsatzDto.setIAnzahlRechnungen(iAnzahlRechnungen);
						}

						hmSammelstelle.put(new Integer(i), kundeUmsatzDto);
					}
				} else {
					switch (i) {

					case 0:

						// in dieser Zeile stehen die Summen fuer das gesamte
						// Vorjahr
						sZeilenHeader = getTextRespectUISpr("lp.davor", mandantCNr, locUI);

						// das Zeitintervall auf 1900 bis vor 10 jahren
						gcBerechnungsdatumVonI = new GregorianCalendar(1900, GregorianCalendar.JANUARY, 1);
						gcBerechnungsdatumBisI = new GregorianCalendar(iLaufendesJahr - 9, GregorianCalendar.JANUARY,
								1);
						break;

					case 1:

						// das ist eine Leerzeile zur optischen Trennung
						sZeilenHeader = null;
						break;

					case 2:

						sZeilenHeader = (iLaufendesJahr - 9) + "";

						// das Zeitintervall auf den gesamten Januar des laufenden
						// Jahres festlegen
						gcBerechnungsdatumVonI = new GregorianCalendar(iLaufendesJahr - 9, GregorianCalendar.JANUARY,
								1);

						// vorjahr: der erste des naechten monats (fuer alle
						// monate!!!)
						gcBerechnungsdatumBisI = new GregorianCalendar(iLaufendesJahr - 8, GregorianCalendar.JANUARY,
								1);

						break;

					case 3:
						sZeilenHeader = (iLaufendesJahr - 8) + "";

						gcBerechnungsdatumVonI = new GregorianCalendar(iLaufendesJahr - 8, GregorianCalendar.JANUARY,
								1);
						gcBerechnungsdatumBisI = new GregorianCalendar(iLaufendesJahr - 7, GregorianCalendar.JANUARY,
								1);

						break;

					case 4:
						sZeilenHeader = (iLaufendesJahr - 7) + "";

						gcBerechnungsdatumVonI = new GregorianCalendar(iLaufendesJahr - 7, GregorianCalendar.JANUARY,
								1);
						gcBerechnungsdatumBisI = new GregorianCalendar(iLaufendesJahr - 6, GregorianCalendar.JANUARY,
								1);

						break;

					case 5:
						sZeilenHeader = (iLaufendesJahr - 6) + "";

						gcBerechnungsdatumVonI = new GregorianCalendar(iLaufendesJahr - 6, GregorianCalendar.JANUARY,
								1);
						gcBerechnungsdatumBisI = new GregorianCalendar(iLaufendesJahr - 5, GregorianCalendar.JANUARY,
								1);

						break;

					case 6:
						sZeilenHeader = (iLaufendesJahr - 5) + "";

						gcBerechnungsdatumVonI = new GregorianCalendar(iLaufendesJahr - 5, GregorianCalendar.JANUARY,
								1);
						gcBerechnungsdatumBisI = new GregorianCalendar(iLaufendesJahr - 4, GregorianCalendar.JANUARY,
								1);

						break;

					case 7:
						sZeilenHeader = (iLaufendesJahr - 4) + "";

						gcBerechnungsdatumVonI = new GregorianCalendar(iLaufendesJahr - 4, GregorianCalendar.JANUARY,
								1);
						gcBerechnungsdatumBisI = new GregorianCalendar(iLaufendesJahr - 3, GregorianCalendar.JANUARY,
								1);

						break;

					case 8:
						sZeilenHeader = (iLaufendesJahr - 3) + "";

						gcBerechnungsdatumVonI = new GregorianCalendar(iLaufendesJahr - 3, GregorianCalendar.JANUARY,
								1);
						gcBerechnungsdatumBisI = new GregorianCalendar(iLaufendesJahr - 2, GregorianCalendar.JANUARY,
								1);

						break;

					case 9:
						sZeilenHeader = (iLaufendesJahr - 2) + "";

						gcBerechnungsdatumVonI = new GregorianCalendar(iLaufendesJahr - 2, GregorianCalendar.JANUARY,
								1);
						gcBerechnungsdatumBisI = new GregorianCalendar(iLaufendesJahr - 1, GregorianCalendar.JANUARY,
								1);

						break;

					case 10:
						sZeilenHeader = (iLaufendesJahr - 1) + "";

						gcBerechnungsdatumVonI = new GregorianCalendar(iLaufendesJahr - 1, GregorianCalendar.JANUARY,
								1);
						gcBerechnungsdatumBisI = new GregorianCalendar(iLaufendesJahr, GregorianCalendar.JANUARY, 1);

						break;

					case 11:
						sZeilenHeader = iLaufendesJahr + "";

						gcBerechnungsdatumVonI = new GregorianCalendar(iLaufendesJahr, GregorianCalendar.JANUARY, 1);
						gcBerechnungsdatumBisI = new GregorianCalendar(iLaufendesJahr + 1, GregorianCalendar.JANUARY,
								1);

						break;

					case 12:

						// das ist eine Leerzeile zur optischen Trennung
						sZeilenHeader = null;
						break;

					case 13:
						sZeilenHeader = getTextRespectUISpr("lp.summe", mandantCNr, locUI);

						// das Zeitintervall auf das gesamte laufende Jahr festlegen
						gcBerechnungsdatumVonI = new GregorianCalendar(iLaufendesJahr, GregorianCalendar.JANUARY, 1);
						gcBerechnungsdatumBisI = new GregorianCalendar(iLaufendesJahr + 1, GregorianCalendar.JANUARY,
								1);

						// Alle vorherigen Zeilen summieren
						KundeUmsatzTabelleDto kundeUmsatzDto = new KundeUmsatzTabelleDto();

						BigDecimal umsatzGesamt = new BigDecimal(0);
						int rechnungenGesamt = 0;
						for (int j = 0; j < hmSammelstelle.size(); j++) {
							KundeUmsatzTabelleDto temp = hmSammelstelle.get(j);

							if (temp.getBdUmsatz() != null) {
								umsatzGesamt = umsatzGesamt.add(temp.getBdUmsatz());
							}

							if (temp.getIAnzahlRechnungen() != null) {
								rechnungenGesamt += temp.getIAnzahlRechnungen();
							}
						}

						kundeUmsatzDto.setSZeilenheader(sZeilenHeader);
						kundeUmsatzDto.setBdUmsatz(umsatzGesamt);
						kundeUmsatzDto.setIAnzahlRechnungen(rechnungenGesamt);
						hmSammelstelle.put(new Integer(i), kundeUmsatzDto);

						break;

					}

					if (i != 13) {

						java.sql.Date dVon = new java.sql.Date(gcBerechnungsdatumVonI.getTime().getTime());
						java.sql.Date dBis = new java.sql.Date(gcBerechnungsdatumBisI.getTime().getTime());

						// den Umsatz errechnen
						bdUmsatz = getRechnungFac().getUmsatzVomKundenImZeitraum(theClientDto, kundeIId, dVon, dBis,
								false);

						// die Anzahl der rechnungen errechnen
						iAnzahlRechnungen = getRechnungFac().getAnzahlDerRechnungenVomKundenImZeitraum(theClientDto,
								kundeIId, dVon, dBis, false);

						// die Zeilen fuer die Anzeige zusammenbauen
						KundeUmsatzTabelleDto kundeUmsatzDto = new KundeUmsatzTabelleDto();
						if (sZeilenHeader != null) {
							kundeUmsatzDto.setSZeilenheader(sZeilenHeader);
							kundeUmsatzDto.setBdUmsatz(bdUmsatz);
							kundeUmsatzDto.setIAnzahlRechnungen(iAnzahlRechnungen);
						}

						hmSammelstelle.put(new Integer(i), kundeUmsatzDto);
					}
				}
			}

			// jetzt die Darstellung in der Tabelle zusammenbauen
			int startIndex = 0;
			long endIndex = startIndex + getAnzahlZeilen() - 1;

			Object[][] rows = new Object[(int) getAnzahlZeilen()][getAnzahlSpalten()];
			int row = 0;
			int col = 0;

			Collection<KundeUmsatzTabelleDto> clKundeUmsatz = hmSammelstelle.values();
			Iterator<KundeUmsatzTabelleDto> it = clKundeUmsatz.iterator();

			while (it.hasNext()) {
				KundeUmsatzTabelleDto kundeUmsatzDto = (KundeUmsatzTabelleDto) it.next();

				rows[row][col++] = null;
				rows[row][col++] = kundeUmsatzDto.getSZeilenHeader();
				rows[row][col++] = kundeUmsatzDto.getBdUmsatz();
				rows[row][col++] = kundeUmsatzDto.getIAnzahlRechnungen();
				row++;
				col = 0;
			}

			result = new QueryResult(rows, getRowCount(), startIndex, endIndex, 0);
		} catch (Exception e) {
			throw new EJBExceptionLP(1, e);
		}

		return result;
	}
}
