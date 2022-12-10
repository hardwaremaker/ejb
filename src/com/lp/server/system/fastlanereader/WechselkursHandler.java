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
package com.lp.server.system.fastlanereader;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.system.fastlanereader.generated.FLRWechselkurs;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.UseCaseHandler;
import com.lp.server.util.fastlanereader.service.query.FilterBlock;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.BigDecimal13;
import com.lp.util.BigDecimal6;
import com.lp.util.EJBExceptionLP;

/**
 * <p>
 * Hier wird die FLR Funktionalitaet fuer die Wechselkurse implementiert. Pro
 * UseCase gibt es einen Handler.
 * </p>
 * <p>
 * Copright Logistik Pur GmbH (c) 2004
 * </p>
 * <p>
 * Erstellungsdatum 2005-06-22
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version 1.0
 */
public class WechselkursHandler extends UseCaseHandler {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The information needed for the kundes table.
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
			int pageSize = getLimit();
			int startIndex = getStartIndex(rowIndex, pageSize);
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
			// Filter pruefen
			FilterKriterium[] filterKriterien = this.getQuery()
					.getFilterBlock().filterKrit;
			boolean bFilterVonZu = false;
			String sWaehrungVonZu = null;
			for (int i = 0; i < filterKriterien.length; i++) {
				if (filterKriterien[i].kritName
						.equals(FinanzFac.FILTER_WECHSELKURS_VON_ZU)) {
					bFilterVonZu = true;
					sWaehrungVonZu = filterKriterien[i].value;
				}
			}
			while (resultListIterator.hasNext()) {
				FLRWechselkurs kurs = (FLRWechselkurs) resultListIterator
						.next();
				rows[row][col++] = kurs.getId_comp();
				if (bFilterVonZu) {
					if (kurs.getId_comp().getWaehrung_c_nr_von().equals(
							sWaehrungVonZu.replaceAll("'", ""))) {
						rows[row][col++] = kurs.getId_comp()
								.getWaehrung_c_nr_zu();
						rows[row][col++] = kurs.getId_comp().getT_datum();
//						rows[row][col++] = new BigDecimal6(kurs.getN_kurs()
//								.floatValue());
						rows[row][col++] = new BigDecimal13(kurs.getN_kurs());
					} else {
						rows[row][col++] = kurs.getId_comp()
								.getWaehrung_c_nr_von();
						rows[row][col++] = kurs.getId_comp().getT_datum();
						// hier den kurs invertieren
//						rows[row][col++] = new BigDecimal6(1.0).divide(new BigDecimal6(kurs
//								.getN_kurs().floatValue()));
						rows[row][col++] = new BigDecimal13(BigDecimal.ONE
								.divide(kurs.getN_kurs(), 13, RoundingMode.HALF_EVEN));
					}
				} else {
					rows[row][col++] = kurs.getId_comp().getWaehrung_c_nr_zu();
					rows[row][col++] = kurs.getId_comp().getT_datum();
//					rows[row][col++] = new BigDecimal6(kurs.getN_kurs()
//							.floatValue());
					rows[row][col++] = new BigDecimal13(kurs.getN_kurs());
				}
				row++;
				col = 0;
			}
			result = new QueryResult(rows, this.getRowCount(), startIndex,
					endIndex, 0);
		} catch (HibernateException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			closeSession(session);
		}
		return result;
	}

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
			closeSession(session);
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
					// der wechsel kurs muss beidseitig erscheinen, als VON und
					// ZU
					if (filterKriterien[i].kritName
							.equals(FinanzFac.FILTER_WECHSELKURS_VON_ZU)) {
						// 2. VON-Spalte
						if (filterKriterien[i].isBIgnoreCase()) {
							where
									.append(" lower(kurs."
											+ SystemFac.FLR_WECHSELKURS_WAEHRUNG_C_NR_VON
											+ ")");
						} else {
							where
									.append(" kurs."
											+ SystemFac.FLR_WECHSELKURS_WAEHRUNG_C_NR_VON);
						}
						where.append(" " + filterKriterien[i].operator);
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" "
									+ filterKriterien[i].value.toLowerCase());
						} else {
							where.append(" " + filterKriterien[i].value);
						}
						// mit OR verbinden
						/*where.append(" " + FilterKriterium.BOOLOPERATOR_OR
								+ " ");
						// 2. ZU-Spalte
						if (filterKriterien[i].isBIgnoreCase()) {
							where
									.append(" lower(kurs."
											+ SystemFac.FLR_WECHSELKURS_WAEHRUNG_C_NR_ZU
											+ ")");
						} else {
							where
									.append(" kurs."
											+ SystemFac.FLR_WECHSELKURS_WAEHRUNG_C_NR_ZU);
						}
						where.append(" " + filterKriterien[i].operator);
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" "
									+ filterKriterien[i].value.toLowerCase());
						} else {
							where.append(" " + filterKriterien[i].value);
						}*/
					} else {
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" lower(kurs."
									+ filterKriterien[i].kritName + ")");
						} else {
							where
									.append(" kurs."
											+ filterKriterien[i].kritName);
						}
						where.append(" " + filterKriterien[i].operator);
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" "
									+ filterKriterien[i].value.toLowerCase());
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
	private String buildOrderByClause() {
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
						orderBy.append("kurs." + kriterien[i].kritName);
						orderBy.append(" ");
						orderBy.append(kriterien[i].value);
					}
				}
			} else {
				// no sort criteria found, add default sort
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append("kurs."
						+ SystemFac.FLR_WECHSELKURS_WAEHRUNG_C_NR_ZU + " ASC "
						+ ", kurs." + SystemFac.FLR_WECHSELKURS_T_DATUM
						+ " DESC " + " ");
				sortAdded = true;
			}
			if (orderBy.indexOf("kurs."
					+ SystemFac.FLR_WECHSELKURS_WAEHRUNG_C_NR_ZU) < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" kurs."
						+ SystemFac.FLR_WECHSELKURS_WAEHRUNG_C_NR_ZU + " ");
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
		return "from FLRWechselkurs kurs ";
	}

	public QueryResult sort(SortierKriterium[] sortierKriterien,
			Object selectedId) throws EJBExceptionLP {
		this.getQuery().setSortKrit(sortierKriterien);

		QueryResult result = null;
		int rowNumber = 0;

		if (selectedId != null /* && ( (Integer) selectedId).intValue() >= 0 */) {
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();
				String queryString = "select kurs."
						+ SystemFac.FLR_WECHSELKURS_WAEHRUNG_C_NR_ZU
						+ " from FLRWechselkurs kurs "
						+ this.buildWhereClause() + this.buildOrderByClause();
				Query query = session.createQuery(queryString);
				ScrollableResults scrollableResult = query.scroll();
//				boolean idFound = false;
				if (scrollableResult != null) {
					scrollableResult.beforeFirst();
					while (scrollableResult.next()) {
						String id = (String) scrollableResult.getString(0); // TYPE
						// OF
						// KEY
						// ATTRIBUTE
						// !
						// !
						// !
						if (selectedId.equals(id)) {
							rowNumber = scrollableResult.getRowNumber();
							break;
						}
					}
				}
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
			} finally {
				closeSession(session);
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
					new Class[] { Object.class, String.class, Date.class,
							BigDecimal13.class },
					new String[] {
							"pk",
							getTextRespectUISpr("lp.waehrung", mandantCNr,
									locUI),
							getTextRespectUISpr("lp.datum", mandantCNr, locUI),
							getTextRespectUISpr("lp.kurs", mandantCNr, locUI) },

					new int[] {
							-1, // diese Spalte wird ausgeblendet
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST },

					new String[] { SystemFac.FLR_WECHSELKURS_ID_COMP,
							SystemFac.FLR_WECHSELKURS_WAEHRUNG_C_NR_ZU,
							SystemFac.FLR_WECHSELKURS_T_DATUM,
							SystemFac.FLR_WECHSELKURS_N_KURS }));
		}
		return super.getTableInfo();
	}
}
