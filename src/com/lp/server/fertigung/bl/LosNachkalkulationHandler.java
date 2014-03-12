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
package com.lp.server.fertigung.bl;

import java.awt.Color;
import java.rmi.RemoteException;
import java.util.Locale;

import com.lp.server.auftrag.bl.UseCaseHandlerTabelle;
import com.lp.server.fertigung.service.ReportLosnachkalkulationDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.BigDecimal3;
import com.lp.util.BigDecimal4;
import com.lp.util.EJBExceptionLP;

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

public class LosNachkalkulationHandler extends UseCaseHandlerTabelle {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int iAnzahlSpalten = 11;
	private int iAnzahlZeilen = 50;

	/**
	 * Konstruktor.
	 */
	public LosNachkalkulationHandler() {
		super();
		setAnzahlSpalten(iAnzahlSpalten);
		setAnzahlZeilen(iAnzahlZeilen);
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
			tableInfo = new TableInfo(new Class[] { String.class,
					BigDecimal3.class, BigDecimal3.class, BigDecimal3.class,
					BigDecimal3.class, BigDecimal3.class, BigDecimal3.class,
					BigDecimal4.class, BigDecimal4.class, BigDecimal4.class,
					Color.class },

					new String[] {
							"  ",
							getTextRespectUISpr("lp.soll", mandantCNr, locUI),
							getTextRespectUISpr("lp.ist", mandantCNr, locUI),
							getTextRespectUISpr("lp.differenz", mandantCNr,
									locUI),
							getTextRespectUISpr("lp.soll", mandantCNr, locUI),
							getTextRespectUISpr("lp.ist", mandantCNr, locUI),
							getTextRespectUISpr("lp.differenz", mandantCNr,
									locUI),
							getTextRespectUISpr("lp.soll", mandantCNr, locUI),
							getTextRespectUISpr("lp.ist", mandantCNr, locUI),
							getTextRespectUISpr("lp.differenz", mandantCNr,
									locUI), "" }, new String[] { "", "", "",
							"", "", "", "", "", "", "", "" });
			tableInfo.setNegativeWerteRoteinfaerben(true);
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
		getFilterKriterien();
		FilterKriterium fkDatum = aFilterKriterium[0];
		Integer losIId = new Integer(fkDatum.value);
		try {

			ReportLosnachkalkulationDto mat = getFertigungReportFac()
					.getDataNachkalkulationMaterial(losIId, theClientDto);

			ReportLosnachkalkulationDto[] zeit = getFertigungReportFac()
					.getDataNachkalkulationZeitdaten(losIId, theClientDto);
			Object[][] rows = new Object[iAnzahlZeilen][iAnzahlSpalten];
			result = new QueryResult(rows, getRowCount(), 0, 49, 0);
			int row = 0;
			ReportLosnachkalkulationDto nkZeitSumme = new ReportLosnachkalkulationDto();
			nkZeitSumme.setSBezeichnung(getTextRespectUISpr("lp.summe",
					theClientDto.getMandant(), theClientDto.getLocUi()));
			for (int i = 0; i < zeit.length; i++) {
				rows[row][0] = zeit[i].getSBezeichnung();
				rows[row][1] = zeit[i].getBdSollmenge();
				rows[row][2] = zeit[i].getBdIstmenge();
				rows[row][3] = zeit[i].getBdSollmenge().subtract(
						zeit[i].getBdIstmenge());
				rows[row][4] = zeit[i].getBdSollmengeMaschine();
				rows[row][5] = zeit[i].getBdIstmengeMaschine();
				rows[row][6] = zeit[i].getBdSollmengeMaschine().subtract(
						zeit[i].getBdIstmengeMaschine());
				rows[row][7] = zeit[i].getBdSollpreis();
				rows[row][8] = zeit[i].getBdIstpreis();
				rows[row][9] = zeit[i].getBdSollpreis().subtract(
						zeit[i].getBdIstpreis());
				// Summe
				nkZeitSumme.addiereZuSollmenge(zeit[i].getBdSollmenge());
				nkZeitSumme.addiereZuIstmenge(zeit[i].getBdIstmenge());
				nkZeitSumme.addiereZuIstmengeMaschine(zeit[i]
						.getBdIstmengeMaschine());
				nkZeitSumme.addiereZuSollpreis(zeit[i].getBdSollpreis());
				nkZeitSumme.addiereZuIstpreis(zeit[i].getBdIstpreis());
				nkZeitSumme.addiereZuSollmengeMaschine(zeit[i]
						.getBdSollmengeMaschine());
				row++;
			}
			rows[row][0] = nkZeitSumme.getSBezeichnung();
			rows[row][1] = nkZeitSumme.getBdSollmenge();
			rows[row][2] = nkZeitSumme.getBdIstmenge();
			rows[row][3] = nkZeitSumme.getBdSollmenge().subtract(
					nkZeitSumme.getBdIstmenge());
			rows[row][4] = nkZeitSumme.getBdSollmengeMaschine();
			rows[row][5] = nkZeitSumme.getBdIstmengeMaschine();
			rows[row][6] = nkZeitSumme.getBdSollmengeMaschine().subtract(
					nkZeitSumme.getBdIstmengeMaschine());
			rows[row][7] = nkZeitSumme.getBdSollpreis();
			rows[row][8] = nkZeitSumme.getBdIstpreis();
			rows[row][9] = nkZeitSumme.getBdSollpreis().subtract(
					nkZeitSumme.getBdIstpreis());
			row++;
			row++;
			ReportLosnachkalkulationDto nkGesamtSumme = new ReportLosnachkalkulationDto();
			nkGesamtSumme.setSBezeichnung(getTextRespectUISpr("lp.gesamtsumme",
					theClientDto.getMandant(), theClientDto.getLocUi()));
			nkGesamtSumme.addiereZuSollpreis(nkZeitSumme.getBdSollpreis());
			nkGesamtSumme.addiereZuIstpreis(nkZeitSumme.getBdIstpreis());
			nkGesamtSumme.addiereZuSollpreis(mat.getBdSollpreis());
			nkGesamtSumme.addiereZuIstpreis(mat.getBdIstpreis());
			rows[row][0] = mat.getSBezeichnung();
			rows[row][1] = mat.getBdSollmenge();
			rows[row][2] = mat.getBdIstmenge();
			rows[row][3] = mat.getBdSollmenge().subtract(mat.getBdIstmenge());
			rows[row][4] = mat.getBdSollmengeMaschine();
			rows[row][5] = mat.getBdIstmengeMaschine();
			rows[row][6] = mat.getBdSollmengeMaschine().subtract(
					mat.getBdIstmengeMaschine());
			rows[row][7] = mat.getBdSollpreis();
			rows[row][8] = mat.getBdIstpreis();
			rows[row][9] = mat.getBdSollpreis().subtract(mat.getBdIstpreis());
			row++;
			row++;
			rows[row][0] = nkGesamtSumme.getSBezeichnung();
			rows[row][1] = nkGesamtSumme.getBdSollmenge();
			rows[row][2] = nkGesamtSumme.getBdIstmenge();
			rows[row][3] = nkGesamtSumme.getBdSollmengeMaschine().subtract(
					nkGesamtSumme.getBdIstmengeMaschine());
			rows[row][4] = nkGesamtSumme.getBdSollmengeMaschine();
			rows[row][5] = nkGesamtSumme.getBdIstmengeMaschine();
			rows[row][6] = nkGesamtSumme.getBdSollmenge().subtract(
					nkGesamtSumme.getBdIstmenge());
			rows[row][7] = nkGesamtSumme.getBdSollpreis();
			rows[row][8] = nkGesamtSumme.getBdIstpreis();
			rows[row][9] = nkGesamtSumme.getBdSollpreis().subtract(
					nkGesamtSumme.getBdIstpreis());
			
			if (getFertigungFac().losgutschlechtFindAllFehler(losIId).length > 0) {
				row++;
				row++;
				rows[row][0] = getTextRespectUISpr(
						"fert.nachkalkulation.fehlervorhanden",
						theClientDto.getMandant(), theClientDto.getLocUi());

				rows[row][10] = Color.RED;
			}
			row++;

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return result;
	}
}
