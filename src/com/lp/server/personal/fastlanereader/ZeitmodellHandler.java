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
package com.lp.server.personal.fastlanereader;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.personal.fastlanereader.generated.FLRZeitmodell;
import com.lp.server.personal.fastlanereader.generated.FLRZeitmodellspr;
import com.lp.server.personal.service.ZeiterfassungFac;
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
 * Hier wird die FLR Funktionalit&auml;t f&uuml;r die Zeitmodelle implementiert.
 * Pro UseCase gibt es einen Handler.
 * </p>
 * <p>
 * Copright Logistik Pur GmbH (c) 2004
 * </p>
 * <p>
 * Erstellungsdatum 2005-02-14
 * </p>
 * <p>
 * </p>
 * 
 * @author ck
 * @version 1.0
 */
public class ZeitmodellHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String FLR_ZEITMODELL_C_NR = "zeitmodell.c_nr";

	/**
	 * the size of the data page returned in QueryResult.
	 * 
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
			int pageSize = ZeitmodellHandler.PAGE_SIZE;
			int startIndex = Math.max(rowIndex.intValue() - (pageSize / 2), 0);
			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			String queryString = this.getFromClause() + this.buildWhereClause()
					+ this.buildOrderByClause();

			Query query = session.createQuery(queryString);
			session = setFilter(session);

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
				FLRZeitmodell zeitmodell = (FLRZeitmodell) o[0];

				Iterator<?> sprsetIterator = zeitmodell.getZeitmodellsprset()
						.iterator();

				rows[row][col++] = zeitmodell.getI_id();
				rows[row][col++] = zeitmodell.getC_nr();

				String spr = null;
				while (sprsetIterator.hasNext()) {
					FLRZeitmodellspr zeitmodellspr = (FLRZeitmodellspr) sprsetIterator
							.next();
					if (zeitmodellspr.getLocale().getC_nr().compareTo(sLocUI) == 0) {
						spr = zeitmodellspr.getC_bez();
						break;
					}
				}
				rows[row][col++] = spr;

				if (zeitmodell.getFlrschicht() != null) {
					rows[row][col++] = zeitmodell.getFlrschicht().getC_bez();
				} else {
					rows[row][col++] = null;

				}

				if (zeitmodell.getZeitmodelltagset() == null
						|| zeitmodell.getZeitmodelltagset().size() == 0) {
					rows[row][col++] = Color.RED;
				}
				row++;
				col = 0;
			}
			result = new QueryResult(rows, this.getRowCount(), startIndex,
					endIndex, 0);
		}

		catch (HibernateException e) {
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

	protected long getRowCountFromDataBase() {
		long rowCount = 0;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			session = setFilter(session);

			String queryString = "SELECT COUNT(*) FROM FLRZeitmodell AS zeitmodell"
					+ " LEFT JOIN zeitmodell.zeitmodellsprset AS zeitmodellsprset "
					+ buildWhereClause();
			Query query = session.createQuery(queryString);
			List<?> rowCountResult = query.list();
			if (rowCountResult != null && rowCountResult.size() > 0) {
				rowCount = ((Long) rowCountResult.get(0)).longValue();
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
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
	 * builds the where clause of the HQL (Hibernate Query Language) statement
	 * using the current query.
	 * 
	 * @return the HQL where clause.
	 */
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
					if (filterKriterien[i].isBIgnoreCase()) {
						where.append(" upper(" + filterKriterien[i].kritName
								+ ")");
					} else {
						where.append(" " + filterKriterien[i].kritName);
					}
					where.append(" " + filterKriterien[i].operator);
					if (filterKriterien[i].isBIgnoreCase()) {
						where.append(" "
								+ filterKriterien[i].value.toUpperCase());
					} else {
						where.append(" " + filterKriterien[i].value);
					}
				}
			}
			if (filterAdded) {
				where.insert(0, " WHERE");
			}
		}

		return where.toString();
	}

	// /**
	// * builds the HQL (Hibernate Query Language) order by clause using the
	// sort
	// * criterias contained in the current query.
	// *
	// * @return the HQL order by clause.
	// */
	// private String buildOrderByClause() {
	// StringBuffer orderBy = new StringBuffer("");
	// if (this.getQuery() != null) {
	// SortierKriterium[] kriterien = this.getQuery().getSortKrit();
	// boolean sortAdded = false;
	// if (kriterien != null && kriterien.length > 0) {
	// for (int i = 0; i < kriterien.length; i++) {
	// if (kriterien[i].isKrit) {
	// if (sortAdded) {
	// orderBy.append(", ");
	// }
	// sortAdded = true;
	// orderBy.append("zeitmodell." + kriterien[i].kritName);
	// orderBy.append(" ");
	// orderBy.append(kriterien[i].value);
	// }
	// }
	// } else {
	// // no sort criteria found, add default sort
	// if (sortAdded) {
	// orderBy.append(", ");
	// }
	// orderBy.append("zeitmodell.c_nr ASC ");
	//
	// sortAdded = true;
	// }
	// if (orderBy.indexOf("zeitmodell.c_nr") < 0) {
	// // unique sort required because otherwise rowNumber of
	// // selectedId
	// // within sort() method may be different from the position of
	// // selectedId
	// // as returned in the page of getPageAt().
	// if (sortAdded) {
	// orderBy.append(", ");
	// }
	// orderBy.append(" zeitmodell.c_nr ");
	// sortAdded = true;
	// }
	// if (sortAdded) {
	// orderBy.insert(0, " ORDER BY ");
	// }
	// }
	// return orderBy.toString();
	// }

	/**
	 * builds the HQL (Hibernate Query Language) order by clause using the sort
	 * criterias contained in the current query.
	 * 
	 * @return the HQL order by clause.
	 */
	private String buildOrderByClause() {

		StringBuffer orderBy = new StringBuffer("");

		if (getQuery() != null) {
			SortierKriterium[] sortKrit = getQuery().getSortKrit();

			boolean sortAdded = false;

			if (sortKrit != null && sortKrit.length > 0) {

				for (SortierKriterium s : sortKrit) {

					if (s.isKrit) {
						if (sortAdded) {
							orderBy.append(", ");
						}
						sortAdded = true;
						orderBy.append(s.kritName);
						orderBy.append(" ");
						orderBy.append(s.value);
					}
				}
			}
			// no sort criteria found, add default sort
			if (orderBy.indexOf(FLR_ZEITMODELL_C_NR) < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" " + FLR_ZEITMODELL_C_NR + " ");
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
		return "FROM FLRZeitmodell AS zeitmodell"
				+ " LEFT JOIN zeitmodell.zeitmodellsprset AS zeitmodellsprset LEFT JOIN zeitmodell.flrschicht AS schicht";
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
				session = setFilter(session);
				String queryString = getFromClause() + buildWhereClause()
						+ buildOrderByClause();

				Query query = session.createQuery(queryString);
				ScrollableResults scrollableResult = query.scroll();
				if (scrollableResult != null) {
					scrollableResult.beforeFirst();
					while (scrollableResult.next()) {
						FLRZeitmodell zeitmodell = (FLRZeitmodell) scrollableResult
								.get(0);
						Integer id = zeitmodell.getI_id();
						if (selectedId.equals(id)) {
							rowNumber = scrollableResult.getRowNumber();
							break;
						}
					}
				}
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
			} finally {
				try {
					session.close();
				} catch (HibernateException he) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, he);
				}
			}
		}

		if (rowNumber < 0 || rowNumber >= this.getRowCount()) {
			rowNumber = 0;
		}

		result = this.getPageAt(new Integer(rowNumber));
		result.setIndexOfSelectedRow(rowNumber);

		return result;
	}

	public TableInfo getTableInfo() {
		if (super.getTableInfo() == null) {
			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();
			setTableInfo(new TableInfo(
					new Class[] { Integer.class, String.class, String.class,
							String.class, Color.class },
					new String[] {
							"Id",
							getTextRespectUISpr("lp.kennung", mandantCNr, locUI),
							getTextRespectUISpr("lp.bezeichnung", mandantCNr,
									locUI),
							getTextRespectUISpr("pers.schicht", mandantCNr,
									locUI) }

					, new int[] {
							-1, // diese Spalte wird ausgeblendet
							QueryParameters.FLR_BREITE_L,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST }

					, new String[] {
							"i_id",
							FLR_ZEITMODELL_C_NR,
							ZeiterfassungFac.FLR_ZEITMODELL_ZEITMODELLSPRSET
									+ ".c_bez", "schicht.c_bez" }));

		}
		return super.getTableInfo();
	}
}
