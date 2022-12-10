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
package com.lp.server.eingangsrechnung.bl;

import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.TreeMap;

import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungUmsatzTabelleDto;
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

public class EingangsrechnungUebersichtHandler extends UmsatzUseCaseHandlerTabelle {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final int iAnzahlSpalten = 10;
	private ArrayList<RechnungUmsatzTabelleDto> hmDaten = null;

	/**
	 * Konstruktor.
	 */
	public EingangsrechnungUebersichtHandler() {
		super();

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
							BigDecimal.class, BigDecimal.class, String.class, BigDecimal.class, BigDecimal.class },

					new String[] { "", "  ", getTextRespectUISpr("lp.brutto", mandantCNr, locUI),
							getTextRespectUISpr("lp.netto", mandantCNr, locUI), "  ",
							getTextRespectUISpr("lp.brutto", mandantCNr, locUI),
							getTextRespectUISpr("lp.netto", mandantCNr, locUI), "  ",
							getTextRespectUISpr("lp.brutto", mandantCNr, locUI),
							getTextRespectUISpr("lp.netto", mandantCNr, locUI) },
					new String[] { "", "", "", "", "", "", "", "", "", "" });
		}
		return this.tableInfo;
	}

	private RechnungUmsatzTabelleDto befuelleDtoAnhandDatumsgrenzen(String header, String kriterium,
			boolean bZusatzkosten, GregorianCalendar gcBerechnungsdatumVonI, GregorianCalendar gcBerechnungsdatumBisI)
			throws Throwable {

		RechnungUmsatzTabelleDto zeileDto = new RechnungUmsatzTabelleDto();

		// den offenen Bruttowert bestimmen
		BigDecimal bdOffeneBrutto = getEingangsrechnungFac().berechneSummeOffenBruttoInMandantenwaehrung(theClientDto,
				kriterium, gcBerechnungsdatumVonI, gcBerechnungsdatumBisI, bZusatzkosten);

		// den offenen Nettowert bestimmen
		BigDecimal bdOffeneNetto = getEingangsrechnungFac().berechneSummeOffenNettoInMandantenwaehrung(theClientDto,
				kriterium, gcBerechnungsdatumVonI, gcBerechnungsdatumBisI, bZusatzkosten);

		// den Umsatz brutto bestimmen
		BigDecimal bdUmsatzBrutto = getEingangsrechnungFac().berechneSummeUmsatzBrutto(theClientDto, null, kriterium,
				theClientDto.getSMandantenwaehrung(), gcBerechnungsdatumVonI, gcBerechnungsdatumBisI, bZusatzkosten);

		// den Umsatz netto bestimmen
		BigDecimal bdUmsatzNetto = getEingangsrechnungFac().berechneSummeUmsatzNetto(theClientDto, null, kriterium,
				theClientDto.getSMandantenwaehrung(), gcBerechnungsdatumVonI, gcBerechnungsdatumBisI, bZusatzkosten);

		BigDecimal bdAnzahlungNichtVerrechnetBrutto = getEingangsrechnungFac()
				.berechneSummeAnzahlungenNichtVerrechnetBrutto(theClientDto, kriterium, gcBerechnungsdatumVonI,
						gcBerechnungsdatumBisI);

		BigDecimal bdAnzahlungNichtVerrechnetNetto = getEingangsrechnungFac()
				.berechneSummeAnzahlungenNichtVerrechnetNetto(theClientDto, kriterium, gcBerechnungsdatumVonI,
						gcBerechnungsdatumBisI);

		zeileDto.setSZeilenheader(header);
		zeileDto.setBdOffeneBrutto(bdOffeneBrutto);
		zeileDto.setBdOffeneNetto(bdOffeneNetto);
		// oAuftragUebersichtDto.setSEmpty(null) // ist bereits
		// ueber Dto gesetzt
		zeileDto.setBdUmsatzBrutto(bdUmsatzBrutto);
		zeileDto.setBdUmsatzNetto(bdUmsatzNetto);
		zeileDto.setBdAnzahlungBrutto(bdAnzahlungNichtVerrechnetBrutto);
		zeileDto.setBdAnzahlungNetto(bdAnzahlungNichtVerrechnetNetto);

		return zeileDto;
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
		FilterKriterium fkDatum = aFilterKriterium[EingangsrechnungFac.IDX_KRIT_DATUM];

		FilterKriterium fkZusatzkosten = aFilterKriterium[EingangsrechnungFac.IDX_KRIT_ZUSATZKOSTEN];
		FilterKriterium fkJahr = aFilterKriterium[EingangsrechnungFac.IDX_KRIT_JAHR];

		boolean bZusatzkosten = Helper.short2boolean(new Short(fkZusatzkosten.value));
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
						fkDatum.kritName, bZusatzkosten, gcVonBis[0], gcVonBis[1]));

		// Leerzeile
		hmDaten.add(new RechnungUmsatzTabelleDto());

		// Nun Monate des aktuellen GF durchgehen

		ArrayList<GregorianCalendar[]> alMonate = getMonateAktuellesJahr(iJahr, 0, bGerschaeftsjahr);

		for (int i = 0; i < alMonate.size(); i++) {

			GregorianCalendar[] gcVonBisMonate = alMonate.get(i);

			hmDaten.add(befuelleDtoAnhandDatumsgrenzen(aMonatsnamen[gcVonBisMonate[0].get(GregorianCalendar.MONTH)],
					fkDatum.kritName, bZusatzkosten, gcVonBisMonate[0], gcVonBisMonate[1]));
		}

		// Leerzeile
		hmDaten.add(new RechnungUmsatzTabelleDto());

		// Summe aktuelles Jahr
		GregorianCalendar[] gcVonBisAktuell = getAktuellesJahr(iJahr, 0, bGerschaeftsjahr);

		hmDaten.add(befuelleDtoAnhandDatumsgrenzen(getTextRespectUISpr("lp.summe", mandantCNr, locUI) + " " + iJahr,
				fkDatum.kritName, bZusatzkosten, gcVonBisAktuell[0], gcVonBisAktuell[1]));

		// Summe Gesamt
		GregorianCalendar[] gcVonBisGesamt = getGesamtJahr(iJahr, 0, bGerschaeftsjahr);

		hmDaten.add(befuelleDtoAnhandDatumsgrenzen(getTextRespectUISpr("lp.gesamtsumme", mandantCNr, locUI),
				fkDatum.kritName, bZusatzkosten, gcVonBisGesamt[0], gcVonBisGesamt[1]));

		setAnzahlZeilen(hmDaten.size());

	}

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;

		try {
			setInhalt();
			int startIndex = 0;
			long endIndex = startIndex + getAnzahlZeilen() - 1;

			Object[][] rows = new Object[(int) getAnzahlZeilen()][getAnzahlSpalten()];

			int col = 0;

			for (int row = 0; row < hmDaten.size(); row++) {
				RechnungUmsatzTabelleDto oErUebersichtDto = hmDaten.get(row);
				rows[row][col++] = "";
				rows[row][col++] = oErUebersichtDto.getSZeilenHeader();
				rows[row][col++] = oErUebersichtDto.getBdOffeneBrutto();
				rows[row][col++] = oErUebersichtDto.getBdOffeneNetto();
				rows[row][col++] = oErUebersichtDto.getSEmpty();
				rows[row][col++] = oErUebersichtDto.getBdUmsatzBrutto();
				rows[row][col++] = oErUebersichtDto.getBdUmsatzNetto();
				rows[row][col++] = oErUebersichtDto.getSEmpty();
				rows[row][col++] = oErUebersichtDto.getBdAnzahlungBrutto();
				rows[row][col++] = oErUebersichtDto.getBdAnzahlungNetto();

				col = 0;
			}

			result = new QueryResult(rows, getRowCount(), startIndex, endIndex, 0);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return result;
	}
}
