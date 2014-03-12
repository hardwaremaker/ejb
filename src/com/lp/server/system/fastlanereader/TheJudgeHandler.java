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
import java.util.Locale;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.system.fastlanereader.generated.FLRTheJudge;
import com.lp.server.system.service.LockMeDto;
import com.lp.server.system.service.TheJudgeFac;
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
 * <I>Diese Klasse kuemmert sich um Rechte.</I>
 * </p>
 * 
 * 
 * <p>
 * Copright Logistik Pur GmbH (c) 2004
 * </p>
 * 
 * <p>
 * Erstellungsdatum <I>Herbst 2004</I>
 * </p>
 * 
 * <p>
 * </p>
 * 
 * @author Josef Ornetsmueller
 * @version 1.0
 */
public class TheJudgeHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * getPageAt
	 * 
	 * @param rowIndex
	 *            Integer
	 * @return QueryResult
	 */
	public QueryResult getPageAt(Integer rowIndex) {

		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			int colCount = this.getTableInfo().getColumnClasses().length;
			int pageSize = PAGE_SIZE;
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

			Object[][] rows = new Object[resultList.size()][colCount + 1];

			int row = 0;
			int col = 0;
			while (resultListIterator.hasNext()) {
				FLRTheJudge theJudge = (FLRTheJudge) resultListIterator.next();
				// flrc: 2
				// rows[row][col++] = row + ""; //wegen erste zeile ist key und
				// wird ausgeblendet.
				LockMeDto lm = new LockMeDto(theJudge.getId_comp().getC_wer(),
						theJudge.getId_comp().getC_was(), theJudge
								.getC_usernr()); // wegen erste zeile ist key
				// und wird ausgeblendet.
				lm.setPersonalIIdLocker(theJudge.getPersonal_i_id_locker());
				rows[row][col++] = lm; // wegen erste zeile ist key und wird
				// ausgeblendet.
				rows[row][col++] = theJudge.getId_comp().getC_wer();
				rows[row][col++] = theJudge.getPersonal_i_id_locker();
				rows[row][col++] = theJudge.getC_usernr().trim();
				rows[row][col++] = theJudge.getId_comp().getC_was();
				rows[row][col++] = new java.sql.Timestamp(theJudge.getT_wann()
						.getTime());
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

	/**
	 * buildOrderByClause
	 * 
	 * @return String
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
						// flrc: 2
						orderBy.append(" thejudge." + kriterien[i].kritName);
						orderBy.append(" ");
						orderBy.append(kriterien[i].value);
					}
				}
			} else {
				// flrc: 2
				// no sort criteria found, add default sort
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" thejudge.id_comp.c_wer DESC ");
				sortAdded = true;
			}
			if (orderBy.indexOf(" thejudge.id_comp.c_wer") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" thejudge.id_comp.c_wer ");
				sortAdded = true;
			}
			if (sortAdded) {
				orderBy.insert(0, " ORDER BY ");
			}
		}
		return orderBy.toString();
	}

	/**
	 * buildWhereClause
	 * 
	 * @return String
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
					// flrc: 3
					where.append(" thejudge." + filterKriterien[i].kritName);
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

	/**
	 * getFromClause
	 * 
	 * @return String
	 */
	private String getFromClause() {
		return "from FLRTheJudge thejudge ";
	}

	/**
	 * getRowCountFromDataBase
	 * 
	 * @return int
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
	 * getTableInfo
	 * 
	 * @return TableInfo
	 */
	public TableInfo getTableInfo() {
		if (super.getTableInfo() == null) {
			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();
			setTableInfo(new TableInfo(new Class[] { String.class,
					String.class, String.class, String.class, String.class,
					java.sql.Timestamp.class }, new String[] {
					"i_id",
					getTextRespectUISpr("judge.modul", mandantCNr, locUI),
					getTextRespectUISpr("judge.locker", mandantCNr, locUI),
					getTextRespectUISpr("judge.locker_userid", mandantCNr,
							locUI),
					getTextRespectUISpr("judge.was", mandantCNr, locUI),
					getTextRespectUISpr("judge.wann", mandantCNr, locUI), },
					new String[] { "i_id", TheJudgeFac.FLRSPALTE_ID_COMP_C_WER,
							TheJudgeFac.FLRSPALTE_PERSONAL_I_ID_LOCKER,
							TheJudgeFac.FLRSPALTE_C_USERNR,
							TheJudgeFac.FLRSPALTE_ID_COMP_C_WAS,
							TheJudgeFac.FLRSPALTE_T_WANN }));
		}
		return super.getTableInfo();
	}

	/**
	 * sort
	 * 
	 * @param sortierKriterien
	 *            SortierKriterium[]
	 * @param selectedId
	 *            Object
	 * @return QueryResult
	 */
	public QueryResult sort(SortierKriterium[] sortierKriterien,
			Object selectedId) {

		this.getQuery().setSortKrit(sortierKriterien);

		QueryResult result = null;
		int rowNumber = 0;

		// flrc: 5 weil selectedId ein zB. auch ein String sein kann
		if (selectedId != null /* && ( (Integer) selectedId).intValue() >= 0 */) {
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();
				// flrc: 6 hier das select formulieren
				String queryString = "select thejudge.id_comp.c_wer from FLRTheJudge thejudge "
						+ this.buildWhereClause() + this.buildOrderByClause();
				Query query = session.createQuery(queryString);
				ScrollableResults scrollableResult = query.scroll();
				boolean idFound = false;
				if (scrollableResult != null) {
					scrollableResult.beforeFirst();
					while (scrollableResult.next()) {
						// flrc: 7 String, weil thejudge.id_comp.c_wer ein
						// String ist.
						String id = (String) scrollableResult.getString(0);
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
}
