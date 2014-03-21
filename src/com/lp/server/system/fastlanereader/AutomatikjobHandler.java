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
package com.lp.server.system.fastlanereader;

import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.system.fastlanereader.generated.FLRAutomatikjob;
import com.lp.server.system.service.AutomatikjobFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.UseCaseHandler;
import com.lp.server.util.fastlanereader.service.query.FilterBlock;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.EJBExceptionLP;

public class AutomatikjobHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String FLR_AUTOMATIKJOB = "FLRAutomatikjob.";
	public static final String FLR_AUTOMATIKJOB_FROM_CLAUSE = " from FLRAutomatikjob as automatikjob ";

	public AutomatikjobHandler() {
		super();
	}

	/**
	 * gets the page of data for the specified row using the current
	 * queryParameters.
	 * 
	 * @param rowIndex
	 *            the index of the row that should be contained in the page.
	 * @return the data page for the specified row.
	 * @throws EJBExceptionLP
	 * @todo Diese com.lp.server.util.fastlanereader.UseCaseHandler-Methode
	 *       implementieren
	 */
	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = AutomatikjobHandler.PAGE_SIZE;
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
				FLRAutomatikjob automatikjob = (FLRAutomatikjob) resultListIterator
						.next();
				rows[row][col++] = automatikjob.getI_id();
				rows[row][col++] = automatikjob.getI_sort();
				rows[row][col++] = automatikjob.getC_name();
				rows[row][col++] = automatikjob.getC_beschreibung();
				rows[row][col++] = automatikjob.getI_intervall();
				if (automatikjob.getB_active().equals("1")) {
					rows[row][col++] = new Boolean(true);
				} else {
					rows[row][col++] = new Boolean(false);
				}
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
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, he);
			}
		}
		return result;

	}

	private String buildWhereClause() {
		StringBuffer where = new StringBuffer("");

		if (this.getQuery() != null && this.getQuery().getFilterBlock() != null
				&& this.getQuery().getFilterBlock().filterKrit != null) {

			FilterBlock filterBlock = this.getQuery().getFilterBlock();
			FilterKriterium[] filterKriterien = this.getQuery()
					.getFilterBlock().filterKrit;
			String booleanOperator = filterBlock.boolOperator;
			boolean filterAdded = false;

			for (int i = 0; i < filterKriterien.length; i++) {
				if (filterKriterien[i].isKrit) {
					if (filterAdded) {
						where.append(" " + booleanOperator);
					}
					filterAdded = true;
					where
							.append(" automatikjob."
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
		if (this.getQuery() != null) {
			SortierKriterium[] kriterien = this.getQuery().getSortKrit();
			kriterien = null;
			boolean sortAdded = false;
			if (kriterien != null && kriterien.length > 0) {
				for (int i = 0; i < kriterien.length; i++) {
					if (!kriterien[i].kritName
							.endsWith(Facade.NICHT_SORTIERBAR)) {
						if (kriterien[i].isKrit) {
							if (sortAdded) {
								orderBy.append(", ");
							}
							sortAdded = true;
							orderBy.append("automatikjob."
									+ kriterien[i].kritName);
							orderBy.append(" ");
							orderBy.append(kriterien[i].value);
						}
					}
				}
			} else {
				// no sort criteria found, add default sort
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append("automatikjob.i_sort ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("automatikjob.i_sort") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" automatikjob.i_sort ");
				sortAdded = true;
			}
			if (sortAdded) {
				orderBy.insert(0, " ORDER BY ");
			}
		}
		return " ORDER BY automatikjob.i_sort ASC ";
	}

	private String getFromClause() {
		return "from FLRAutomatikjob as automatikjob ";
	}

	/**
	 * gets the total number of rows available using the current query.
	 * 
	 * @return the number of rows in the query.
	 * @todo Diese com.lp.server.util.fastlanereader.UseCaseHandler-Methode
	 *       implementieren
	 */
	protected long getRowCountFromDataBase() {
		long rowCount = 0;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			String queryString = "select count(*) " + this.getFromClause()
					+ this.buildWhereClause();

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

	/**
	 * sorts the data of the current query using the specified criterias and
	 * returns the page of data where the row of selectedId is contained.
	 * 
	 * @param sortierKriterien
	 *            the new sort criterias.
	 * @param selectedId
	 *            the id of the entity that should be included in the result
	 *            page.
	 * @return the sorted data containing the page where the entity with the
	 *         specified id is located.
	 * @throws EJBExceptionLP
	 * @todo Diese com.lp.server.util.fastlanereader.UseCaseHandler-Methode
	 *       implementieren
	 */
	public QueryResult sort(SortierKriterium[] sortierKriterien,
			Object selectedId) throws EJBExceptionLP {
		QueryResult result = null;

		try {
			int rowNumber = 0;
			// Damit immer nach iSort sortiert wird.
			selectedId = null;
			getQuery().setSortKrit(sortierKriterien);

			if (selectedId != null && ((Integer) selectedId).intValue() >= 0) {
				SessionFactory factory = FLRSessionFactory.getFactory();
				Session session = null;

				try {
					session = factory.openSession();
					String queryString = "select " + FLR_AUTOMATIKJOB
							+ "i_sort" + FLR_AUTOMATIKJOB_FROM_CLAUSE
							+ this.buildWhereClause()
							+ this.buildOrderByClause();
					Query query = session.createQuery(queryString);
					ScrollableResults scrollableResult = query.scroll();
					boolean idFound = false;
					if (scrollableResult != null) {
						scrollableResult.beforeFirst();
						while (scrollableResult.next()) {
							Integer id = (Integer) scrollableResult
									.getInteger(0);
							if (selectedId.equals(id)) {
								rowNumber = scrollableResult.getRowNumber();
								break;
							}
						}
					}
				} finally {
					try {
						session.close();
					} catch (HibernateException he) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_HIBERNATE, he);
					}
				}
			}

			if (rowNumber < 0 || rowNumber >= this.getRowCount()) {
				rowNumber = 0;
			}

			result = this.getPageAt(new Integer(rowNumber));
			result.setIndexOfSelectedRow(rowNumber);
		} catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}
		return result;
	}

	public TableInfo getTableInfo() {

		if (super.getTableInfo() == null) {
			setTableInfo(new TableInfo(new Class[] { Integer.class,
					Integer.class, String.class, String.class, Integer.class,
					Boolean.class, },
					new String[] {
							"i_id",
							getTextRespectUISpr("lp.automatik.ablauf",
									theClientDto.getMandant(), theClientDto
											.getLocUi()),
							getTextRespectUISpr("lp.automatik.name",
									theClientDto.getMandant(), theClientDto
											.getLocUi()),
							getTextRespectUISpr("lp.automatik.beschreibung",
									theClientDto.getMandant(), theClientDto
											.getLocUi()),
							getTextRespectUISpr("lp.automatik.intervall",
									theClientDto.getMandant(), theClientDto
											.getLocUi()),
							getTextRespectUISpr("lp.automatik.aktiv",
									theClientDto.getMandant(), theClientDto
											.getLocUi()), }, new int[] {
							QueryParameters.FLR_BREITE_SHARE_WITH_REST, // diese
							// Spalte
							// wird
							// ausgeblendet
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M, }, new String[] {
							AutomatikjobFac.FLR_AUTOMATIKJOB_I_ID,
							AutomatikjobFac.FLR_AUTOMATIKJOB_I_SORT,
							AutomatikjobFac.FLR_AUTOMATIKJOB_C_NAME,
							AutomatikjobFac.FLR_AUTOMATIKJOB_C_BESCHREIBUNG,
							AutomatikjobFac.FLR_AUTOMATIKJOB_I_INTERVALL,
							AutomatikjobFac.FLR_AUTOMATIKJOB_B_ACTIVE, }));
		}

		return super.getTableInfo();
	}
}
