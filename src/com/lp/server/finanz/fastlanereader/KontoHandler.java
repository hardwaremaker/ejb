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

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.finanz.ejbfac.KontoQueryBuilder;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.UseCaseHandler;
import com.lp.server.util.fastlanereader.service.query.FilterBlock;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.EJBExceptionLP;

/**
 * <p>
 * Hier wird die FLR Funktionalitaet fuer die Fibu-Konten implementiert. Pro
 * UseCase gibt es einen Handler.
 * </p>
 * <p>
 * Copright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum 2004-09-02
 * </p>
 * <p>
 * </p>
 *
 * @author MB
 * @version 1.0
 */

public abstract class KontoHandler extends UseCaseHandler {

	private static final long serialVersionUID = 8100668485401670018L;

	abstract public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP;

	/**
	 * gets the total number of rows represented by the current query.
	 *
	 * @see UseCaseHandler#getRowCountFromDataBase()
	 * @return int
	 */
	protected long getRowCountFromDataBase() {
		String queryString = "SELECT COUNT(distinct konto.i_id) " + getFromClause() + buildWhereClause();
		return getRowCountFromDataBaseByQuery(queryString);
	}

	/**
	 * builds the where clause of the HQL (Hibernate Query Language) statement using
	 * the current query.
	 *
	 * @return the HQL where clause.
	 */
	protected String buildWhereClause() {
		StringBuffer where = new StringBuffer("");

		if (this.getQuery() != null && this.getQuery().getFilterBlock() != null
				&& this.getQuery().getFilterBlock().filterKrit != null) {

			FilterBlock filterBlock = this.getQuery().getFilterBlock();
			FilterKriterium[] filterKriterien = this.getQuery().getFilterBlock().filterKrit;
			String booleanOperator = filterBlock.boolOperator;

			boolean filterAdded = false;

			for (int i = 0; i < filterKriterien.length; i++) {
				if (filterKriterien[i].isKrit) {
					if (filterAdded) {
						where.append(" " + booleanOperator);
					}
					filterAdded = true;

					if (filterKriterien[i].kritName.equals(FinanzFac.FILTER_KONTO_OHNE_MITLAUFENDE)) {
						where.append(KontoQueryBuilder.buildOhneMitlaufendeKonten("konto"));
					} else if (filterKriterien[i].kritName
							.equals(FinanzFac.FILTER_KONTO_OHNE_UST_VST_KONTEN_AUSSER_MWSTSATZBEZ)) {
						where.append(KontoQueryBuilder.buildOhneUstVstKontenAusserMwstsatzBez("konto",
								new Integer(filterKriterien[i].value)));
					} else if (filterKriterien[i].kritName.equals(FinanzFac.FILTER_KONTEN_MIT_BUCHUNGEN)) {
						where.append(KontoQueryBuilder.showKontenMitBuchungen(filterKriterien[i].value));
					} else if (filterKriterien[i].kritName.equals("c_suche")) {
						where.append(KontoQueryBuilder.findKonto(filterKriterien[i].value.toLowerCase()));
					} else {
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" lower(konto." + filterKriterien[i].kritName + ")");
						} else {
							where.append(" konto." + filterKriterien[i].kritName);
						}
						where.append(" " + filterKriterien[i].operator);
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" " + filterKriterien[i].value.toLowerCase());
						} else {
							where.append(" " + filterKriterien[i].value);
						}
					}
				}
			}
			if (filterAdded) {
				where.insert(0, " WHERE");
			}
		}
		return where.toString();
	}

	/**
	 * builds the HQL (Hibernate Query Language) order by clause using the sort
	 * criterias contained in the current query.
	 *
	 * @return the HQL order by clause.
	 */
	protected String buildOrderByClause() {
		StringBuffer orderBy = new StringBuffer("");
		if (this.getQuery() != null) {
			SortierKriterium[] kriterien = this.getQuery().getSortKrit();
			boolean sortAdded = false;
			if (kriterien != null && kriterien.length > 0) {
				for (int i = 0; i < kriterien.length; i++) {
					if (kriterien[i].isKrit) {
						if (sortAdded) {
							orderBy.append(", ");
						}
						sortAdded = true;

						if (kriterien[i].kritName.startsWith("flr_mwstsatz")) {
							orderBy.append(kriterien[i].kritName);
						} else {
							orderBy.append("konto." + kriterien[i].kritName);
						}

						orderBy.append(" ");
						orderBy.append(kriterien[i].value);
					}
				}
			} else {
				// no sort criteria found, add default sort
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append("konto.c_nr ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("konto.c_nr") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" konto.c_nr ");
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
	protected String getFromClause() {
		return " from FLRFinanzKonto konto ";
	}

	/**
	 * sorts the data described by the current query using the specified sort
	 * criterias. The current query is also updated with the new sort criterias.
	 *
	 * @see UseCaseHandler#sort(SortierKriterium[], Object)
	 * @throws EJBExceptionLP
	 * @param sortierKriterien SortierKriterium[]
	 * @param selectedId       Object
	 * @return QueryResult
	 */
	public QueryResult sort(SortierKriterium[] sortierKriterien, Object selectedId) throws EJBExceptionLP {
		this.getQuery().setSortKrit(sortierKriterien);

		QueryResult result = null;
		int rowNumber = 0;

		if (selectedId != null && ((Integer) selectedId).intValue() >= 0) {
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();
				String queryString = "SELECT konto." + FinanzFac.FLR_KONTO_I_ID + getFromClause() + buildWhereClause()
						+ buildOrderByClause();
				Query query = session.createQuery(queryString);
				ScrollableResults scrollableResult = query.scroll();
				if (scrollableResult != null) {
					scrollableResult.beforeFirst();
					while (scrollableResult.next()) {
						Integer id = (Integer) scrollableResult.getInteger(0);
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

	/**
	 * gets information about the Kontentable.
	 *
	 * @return TableInfo
	 */
	public TableInfo getTableInfo() {
		if (super.getTableInfo() == null) {
			setTableInfo(produceTableInfo());
		}
		return super.getTableInfo();
	}

	protected abstract TableInfo produceTableInfo();
}
