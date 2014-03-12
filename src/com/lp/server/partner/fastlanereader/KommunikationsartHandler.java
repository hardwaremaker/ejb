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
package com.lp.server.partner.fastlanereader;

import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.partner.fastlanereader.generated.FLRKommunikationsart;
import com.lp.server.partner.fastlanereader.generated.FLRKommunikationsartspr;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.UseCaseHandler;
import com.lp.server.util.fastlanereader.service.query.FilterBlock;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * Diese Klasse kuemmert sich um den Kommunikationsarten-Handler.
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellung: Josef Ornetsmueller; dd.mm.05
 * </p>
 * 
 * <p>
 * 
 * @author $Author: heidi $
 *         </p>
 * 
 * @version $Revision: 1.9 $ Date $Date: 2009/04/24 07:55:31 $
 */
public class KommunikationsartHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {

		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = PAGE_SIZE;
			int startIndex = Math.max(rowIndex.intValue() - (pageSize / 2), 0);
			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			String queryString = getFromClause() + buildWhereClause()
					+ buildOrderByClause();
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;
			int col = 0;

			String sLocUI = Helper.locale2String(theClientDto.getLocUi());

			while (resultListIterator.hasNext()) {
				Object o[] = (Object[]) resultListIterator.next();
				FLRKommunikationsart kommunikationart = (FLRKommunikationsart) o[0];

				Iterator<?> sprsetIterator = kommunikationart
						.getKommunikationsart_kommunikationsartspr_set()
						.iterator();

				Object[] rowToAddCandidate = new Object[colCount];
				rowToAddCandidate[0] = kommunikationart.getC_nr();
				rowToAddCandidate[1] = kommunikationart.getC_nr();
				rowToAddCandidate[2] = findSpr(sLocUI, sprsetIterator);

				int idx = -1;
				if ((idx = findRowidx(rows, row, kommunikationart.getC_nr(),
						findSpr(sLocUI, sprsetIterator))) > -1) {
					if (idx < row) {
						// ueberschreiben
						rows[idx] = rowToAddCandidate;
					} else {
						// hinten anfuegen
						rows[idx] = rowToAddCandidate;
						row++;
					}
				}
			}
			result = new QueryResult(rows, getRowCount(), startIndex, endIndex,
					0);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		} finally {
			try {
				session.close();
			} catch (HibernateException he) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, he);
			}
		}
		return result;
	}

	protected long getRowCountFromDataBase() {
		long rowCount = 0;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			String queryString = "SELECT COUNT(*) FROM FLRKommunikationsart AS kommunikation"
					+ buildWhereClause();
			Query query = session.createQuery(queryString);
			List<?> rowCountResult = query.list();
			if (rowCountResult != null && rowCountResult.size() > 0) {
				rowCount = ((Long) rowCountResult.get(0)).longValue();
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (HibernateException he) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, he);
				}
			}
		}
		return rowCount;
	}

	private String buildWhereClause() {
		StringBuffer where = new StringBuffer("");

		if (getQuery() != null && getQuery().getFilterBlock() != null
				&& getQuery().getFilterBlock().filterKrit != null) {

			FilterBlock filterBlock = getQuery().getFilterBlock();
			FilterKriterium[] filterKriterien = getQuery().getFilterBlock().filterKrit;
			String booleanOperator = filterBlock.boolOperator;
			boolean filterAdded = false;

			for (int i = 0; i < filterKriterien.length; i++) {
				if (filterKriterien[i].isKrit) {
					if (filterAdded) {
						where.append(" " + booleanOperator);
					}
					filterAdded = true;
					where.append(" kommunikation."
							+ filterKriterien[i].kritName);
					where.append(" " + filterKriterien[i].operator);
					where.append(" " + filterKriterien[i].value);
				}
			}
			if (filterAdded) {
				where.insert(0, " WHERE");
			}
		}

		return where.toString();
	}

	private String buildOrderByClause() {
		StringBuffer orderBy = new StringBuffer("");
		if (getQuery() != null) {
			SortierKriterium[] kriterien = getQuery().getSortKrit();
			boolean sortAdded = false;
			if (kriterien != null && kriterien.length > 0) {
				for (int i = 0; i < kriterien.length; i++) {
					if (kriterien[i].isKrit) {
						if (sortAdded) {
							orderBy.append(", ");
						}
						sortAdded = true;
						orderBy.append(kriterien[i].kritName);
						orderBy.append(" ");
						orderBy.append(kriterien[i].value);
					}
				}
			} else {
				// no sort criteria found, add default sort
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append("kommunikation.c_nr DESC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("kommunikation.c_nr") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" kommunikation.c_nr ");
				sortAdded = true;
			}
			if (sortAdded) {
				orderBy.insert(0, " ORDER BY ");
			}
		}
		return orderBy.toString();
	}

	/**
	 * get the basic from clause for the HQL statement.
	 * 
	 * @return the from clause.
	 */
	private String getFromClause() {
		return " FROM FLRKommunikationsart AS kommunikation"
				+ " LEFT JOIN kommunikation.kommunikationsart_kommunikationsartspr_set AS kommunikationsart_kommunikationsartspr_set ";
	}

	public QueryResult sort(SortierKriterium[] sortierKriterien,
			Object selectedId) throws EJBExceptionLP {

		getQuery().setSortKrit(sortierKriterien);

		QueryResult result = null;
		int rowNumber = 0;

		if (selectedId != null) {
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();
				String queryString = getFromClause() + buildWhereClause()
						+ buildOrderByClause();
				Query query = session.createQuery(queryString);
				ScrollableResults scrollableResult = query.scroll();
				if (scrollableResult != null) {
					scrollableResult.beforeFirst();
					while (scrollableResult.next()) {
						FLRKommunikationsart kommunikationsart = (FLRKommunikationsart) scrollableResult
								.get(0);
						String cNr = kommunikationsart.getC_nr();
						if (selectedId.equals(cNr)) {
							rowNumber = scrollableResult.getRowNumber();
							break;
						}
					}
				}
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
			} finally {
				try {
					session.close();
				} catch (HibernateException he) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, he);
				}
			}
		}

		if (rowNumber < 0 || rowNumber >= getRowCount()) {
			rowNumber = 0;
		}

		result = getPageAt(new Integer(rowNumber));
		result.setIndexOfSelectedRow(rowNumber);

		return result;
	}

	public TableInfo getTableInfo() {
		if (super.getTableInfo() == null) {
			setTableInfo(new TableInfo(
					new Class[] { String.class, String.class, String.class },
					new String[] {
							"c_nr",
							getTextRespectUISpr("lp.kennung", theClientDto
									.getMandant(), theClientDto.getLocUi()),
							getTextRespectUISpr("lp.bezeichnung", theClientDto
									.getMandant(), theClientDto.getLocUi()),

					},
					new int[] { QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST },
					new String[] {
							"c_nr",
							"kommunikation.c_nr",
							"kommunikationsart_kommunikationsartspr_set.locale, kommunikationsart_kommunikationsartspr_set.c_bez" }));
		}
		return super.getTableInfo();
	}

	/**
	 * Suche in sprsetIterator nach sLocUI.
	 * 
	 * @param sLocaleI
	 *            String
	 * @param iterUebersetzungenI
	 *            FLRAnsprechpartnerfunktion
	 * @return String <br/> uebersetzung, fuer sLocUI <br/> null, wenn nicht
	 *         uebersetzt oder gar nicht da
	 */
	private String findSpr(String sLocaleI, Iterator<?> iterUebersetzungenI) {

		String sUebersetzung = null;
		while (iterUebersetzungenI.hasNext()) {
			FLRKommunikationsartspr kommunikationsartspr = (FLRKommunikationsartspr) iterUebersetzungenI
					.next();
			if (kommunikationsartspr.getLocale().getC_nr().compareTo(sLocaleI) == 0) {
				sUebersetzung = kommunikationsartspr.getC_bez();
				break;
			}
		}
		return sUebersetzung;
	}
}
