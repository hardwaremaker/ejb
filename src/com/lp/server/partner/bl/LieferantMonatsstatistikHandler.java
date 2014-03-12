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
package com.lp.server.partner.bl;

import java.rmi.RemoteException;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.lp.server.auftrag.bl.UseCaseHandlerTabelle;
import com.lp.server.partner.ejbfac.WareneingangspositionenDto;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.partner.service.StatistikParamDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.EJBExceptionLP;

/**
 * <p>
 * Diese Klasse kuemmert sich um ...
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellung: Vorname Nachname; dd.mm.05
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2009/12/01 12:23:12 $
 */
public class LieferantMonatsstatistikHandler extends UseCaseHandlerTabelle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int iAnzahlZeilen = 17;
	private final int iAnzahlSpalten = 3;
	private double dMenge = 0;
	private double dNetto = 0;

	/**
	 * Konstruktor.
	 */
	public LieferantMonatsstatistikHandler() {
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
			tableInfo = new TableInfo(new Class[] { String.class, Double.class,
					Double.class }, new String[] { " ",
					getTextRespectUISpr("bes.menge", mandantCNr, locUI),
					getTextRespectUISpr("lp.wert", mandantCNr, locUI) },
					new String[] { "", "", "" });
		}
		return tableInfo;
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
		GregorianCalendar gcBerechnungsdatumVon = null;
		GregorianCalendar gcBerechnungsdatumBis = null;

		try {
			String sMandantWaehrung = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto)
					.getWaehrungCNr();

			getFilterKriterien();

			FilterKriterium fkAuswertung = aFilterKriterium[LieferantFac.IDX_KRIT_GESCHAEFTSJAHR];
			if (fkAuswertung.value == null) {
				throw new Exception("fkAuswertung.value ==null");
			}
			int iGJ = Integer.parseInt(fkAuswertung.value);
			FilterKriterium fkLF = aFilterKriterium[LieferantFac.IDX_KRIT_LIEFERANT_ODER_KUNDE_I_ID];
			if (fkLF.value == null) {
				throw new Exception("fkLF.value ==null");
			}
			int i_idLF = Integer.parseInt(fkLF.value);

			// *** Vorjahr
			gcBerechnungsdatumVon = new GregorianCalendar(iGJ - 1, 0, 1);
			gcBerechnungsdatumBis = new GregorianCalendar(iGJ - 1, 11, 31);
			StatistikParamDto statistikParamDto = new StatistikParamDto();
			statistikParamDto.setDDatumVon(new java.sql.Date(
					gcBerechnungsdatumVon.getTimeInMillis()));
			statistikParamDto.setDDatumBis(new java.sql.Date(
					gcBerechnungsdatumBis.getTimeInMillis()));
			statistikParamDto.setId(new Integer(i_idLF));
			berechneMengeUndNetto(statistikParamDto, sMandantWaehrung);

			int iRows = 0;
			Object[][] rows = new Object[(int) getAnzahlZeilen()][getAnzahlSpalten()];
			rows[iRows][0] = getTextRespectUISpr("lp.vorjahr", theClientDto
					.getMandant(), theClientDto.getLocUi())
					+ (iGJ - 1);
			rows[iRows][1] = new Double(dMenge);
			rows[iRows][2] = new Double(dNetto);
			iRows++;

			// *** Leerzeile
			rows[iRows][0] = "";
			rows[iRows][1] = null;
			rows[iRows][2] = null;
			iRows++;

			int startIndex = 0;
			int col = 0;

			double dSummeMenge = 0;
			double dSummePreisnetto = 0;

			// *** Monate
			// das sind alle Monatsnamen, die in dieser Tabelle in dieser
			// Reihenfolge vorkommen koennen.
			DateFormatSymbols symbols = new DateFormatSymbols(theClientDto.getLocUi());
			String[] aMonatsnamen = symbols.getMonths();

			int iRowsOffset = iRows;
			long idxMonateEnd = startIndex + getAnzahlZeilen() - iRowsOffset - 1;

			for (int row = iRowsOffset; row < idxMonateEnd; row++) {
				gcBerechnungsdatumVon = new GregorianCalendar(iGJ, (row
						- iRowsOffset), 1);
				gcBerechnungsdatumBis = berechneNaechstesZeitintervall(row
						- iRowsOffset, iGJ);
				statistikParamDto.setDDatumVon(new java.sql.Date(
						gcBerechnungsdatumVon.getTimeInMillis()));
				statistikParamDto.setDDatumBis(new java.sql.Date(
						gcBerechnungsdatumBis.getTimeInMillis()));
				berechneMengeUndNetto(statistikParamDto, sMandantWaehrung);

				rows[row][col++] = aMonatsnamen[row - iRowsOffset];
				rows[row][col++] = new Double(dMenge);
				rows[row][col++] = new Double(dNetto);

				dSummeMenge += dMenge;
				dSummePreisnetto += dNetto;

				col = 0;
				iRows++;
			}

			// *** Leerzeile
			rows[iRows][0] = "";
			rows[iRows][1] = null;
			rows[iRows][2] = null;
			iRows++;

			// *** Gesamtsumme
			rows[iRows][0] = getTextRespectUISpr("lp.gesamtsumme", theClientDto
					.getMandant(), theClientDto.getLocUi());
			rows[iRows][1] = new Double(dSummeMenge);
			rows[iRows][2] = new Double(dSummePreisnetto);
			result = new QueryResult(rows, getRowCount(), startIndex,
					iAnzahlZeilen - 1, 0);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}

		return result;
	}

	/**
	 * Wegen des Geschaeftsjahres ist es moeglich, dass die Anzeige in das
	 * naechste Kalenderjahr fuehrt.
	 * 
	 * @param iLetztesMonatI
	 *            Monat der letzten Anzeige
	 * @param iJahrI
	 *            Jahr der letzten Anzeige
	 * @return GregorianCalendar das Datum fuer die naechste Anzeige
	 */
	private GregorianCalendar berechneNaechstesZeitintervall(
			int iLetztesMonatI, int iJahrI) {
		GregorianCalendar gcNextO = null;

		if (iLetztesMonatI == GregorianCalendar.DECEMBER) {
			gcNextO = new GregorianCalendar(iJahrI + 1,
					GregorianCalendar.JANUARY, 1);
		} else {
			GregorianCalendar temp = new GregorianCalendar();
			temp.set(iJahrI, iLetztesMonatI, 1);
			gcNextO = new GregorianCalendar(iJahrI, iLetztesMonatI, temp
					.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
		}

		return gcNextO;
	}

	private void berechneMengeUndNetto(StatistikParamDto statistikParamDto,
			String sWaehrung) throws EJBExceptionLP, RemoteException {

		ArrayList<?> alWEPOS = getLieferantFac().getWareneingangspositionen(
				statistikParamDto, sWaehrung, false,false, theClientDto);

		dNetto = 0;
		dMenge = 0;
		for (int i = 0; i < alWEPOS.size(); i++) {
			WareneingangspositionenDto wEPOS = ((WareneingangspositionenDto) alWEPOS
					.get(i));

			double d = wEPOS.getBdWert() != null ? wEPOS.getBdWert()
					.doubleValue() : 0;
			dNetto += d;

			d = wEPOS.getBdMenge() != null ? wEPOS.getBdMenge().doubleValue()
					: 0;
			dMenge += d;
		}
	}
}
