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
package com.lp.server.finanz.fastlanereader;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.finanz.fastlanereader.generated.FLRFinanzErgebnisgruppe;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

public class KontoHandlerSachkonten extends KontoHandler {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * gets the data page for the specified row using the current query. The row
	 * at rowIndex will be located in the middle of the page.
	 * 
	 * @see KontoHandler#getPageAt(java.lang.Integer)
	 * @param rowIndex
	 *            Integer
	 * @throws EJBExceptionLP
	 * @return QueryResult
	 */
	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {

		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			int colCount = this.getTableInfo().getColumnClasses().length;
			int pageSize = KontoHandler.PAGE_SIZE;
			int startIndex = Math.max(rowIndex.intValue() - (pageSize / 2), 0);
			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			String queryString = this.getFromClause() + this.buildWhereClause()
					+ this.buildOrderByClause();
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;
			int col = 0;
			while (resultListIterator.hasNext()) {
				FLRFinanzKonto konto = (FLRFinanzKonto) resultListIterator
						.next();
				rows[row][col++] = konto.getI_id();
				rows[row][col++] = konto.getC_nr();
				rows[row][col++] = konto.getC_bez();
				if (konto.getFlrergebnisgruppe() != null) {

					FLRFinanzErgebnisgruppe ergebnisgruppe = konto.getFlrergebnisgruppe() ;
					rows[row][col++] =
//							(Helper.short2boolean(ergebnisgruppe.getB_bilanzgruppe()) ?
//									"B:" : "E:") + ergebnisgruppe.getC_bez() ;
							(Helper.short2boolean(ergebnisgruppe.getB_bilanzgruppe()) ?
									getTextRespectUISpr("fb.bilanzgruppe", theClientDto.getMandant(), theClientDto.getLocMandant(), ergebnisgruppe.getC_bez()) :
									getTextRespectUISpr("fb.ergebnisgruppe", theClientDto.getMandant(), theClientDto.getLocMandant(), ergebnisgruppe.getC_bez())) ;
					
//					if (Helper.short2boolean(konto.getFlrergebnisgruppe()
//							.getB_bilanzgruppe()) == true) {
//						rows[row][col++] = "B:"
//								+ konto.getFlrergebnisgruppe().getC_bez();
//					} else {
//						rows[row][col++] = "E:"
//								+ konto.getFlrergebnisgruppe().getC_bez();
//					}

				} else {
					rows[row][col++] = "";
				}
				if (konto.getFlrkontoart() != null) {
					rows[row][col++] = getFinanzServiceFac().uebersetzeKontoartOptimal(konto.getFlrkontoart().getC_nr(),
							theClientDto.getLocUi(), theClientDto.getLocMandant());
				} else {
					rows[row][col++] = "";
				}
				
				if (konto.getFlruvaart() != null) {
//					rows[row][col++] = konto.getFlruvaart().getC_nr();
					rows[row][col++] = getFinanzServiceFac().uebersetzeUvaartOptimal(konto.getFlruvaart().getI_id(),
							theClientDto.getLocUi(), theClientDto.getLocMandant());
				} else {
					rows[row][col++] = "";
				}
				if (konto.getFinanzamt_i_id() != null) {
					rows[row][col++] = getPartnerFac().partnerFindByPrimaryKey(
							konto.getFinanzamt_i_id(), theClientDto)
							.getCName1nachnamefirmazeile1();
				} else {
					rows[row][col++] = "";
				}

				// getFinanzFac().finanzamtFindByPrimaryKey(konto.getFinanzamt_i_id(),
				// theClientDto.getMandant(), theClientDto);

				rows[row][col++] = konto.getB_versteckt() > (short) 0 ? Color.lightGray : null ;

				row++;
				col = 0;
			}
			result = new QueryResult(rows, this.getRowCount(), startIndex,
					endIndex, 0);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			try {
				session.close();
			} catch (HibernateException he) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, he);
			}
		}
		return result;
	}

	protected TableInfo produceTableInfo() {
		String mandantCNr = theClientDto.getMandant();
		Locale locUI = theClientDto.getLocUi();
		return new TableInfo(
				new Class[] { 
						Integer.class, String.class,
						String.class, String.class, String.class, String.class,
						String.class, Color.class	 },
				new String[] { "Id",
					getTextRespectUISpr("lp.nr", mandantCNr, locUI),
					getTextRespectUISpr("lp.bezeichnung", mandantCNr, locUI),
					getTextRespectUISpr("lp.hauptgruppe", mandantCNr, locUI),
					getTextRespectUISpr("fb.kontoart", mandantCNr, locUI),
					getTextRespectUISpr("fb.uvaart", mandantCNr, locUI),
					getTextRespectUISpr("fb.finanzamt", mandantCNr, locUI), "" },
				new int[] { -1, 6, -1, 18, 6, 30, 25, 0 },
				new String[] {
						FinanzFac.FLR_KONTO_I_ID,
						FinanzFac.FLR_KONTO_C_NR,
						FinanzFac.FLR_KONTO_C_BEZ,
						FinanzFac.FLR_KONTO_FLRERGEBNISGRUPPE + "." + FinanzFac.FLR_ERGEBNISGRUPPE_C_BEZ, // ergbnisgruppe
						FinanzFac.FLR_KONTO_FLRKONTOART + "."
								+ FinanzFac.FLR_KONTOART_C_NR,
						FinanzFac.FLR_KONTO_FLRUVAART + "." + FinanzFac.FLR_UVAART_C_KENNZEICHEN, // uvaart
						NICHT_SORTIERBAR, "" });
	}
}
