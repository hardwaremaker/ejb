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
package com.lp.server.rechnung.fastlanereader;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

import javax.swing.Icon;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragpositionFac;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnungPosition;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.system.service.TheClientDto;
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

/**
 * <p>
 * Diese Klasse kuemmert sich um die Auftraege, die in einer Rechnung enthalten
 * sind
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 10.04.07
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2013/02/08 08:35:52 $
 */
public class AuftraegeEinerRechnungHandler extends UseCaseHandler {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String FLR_LIEFERSCHEINPOSITION = "flrrechnungposition.";
	public static final String FLR_LIEFERSCHEINPOSITION_FROM_CLAUSE = " from FLRRechnungPosition flrrechnungposition ";

	/**
	 * gets the data page for the specified row using the current query. The row
	 * at rowIndex will be located in the middle of the page.
	 * 
	 * @param rowIndex
	 *            diese Zeile soll selektiert sein
	 * @return QueryResult das Ergebnis der Abfrage
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @see UseCaseHandler#getPageAt(java.lang.Integer)
	 */
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
			
			String queryString = "SELECT auftrag FROM FLRAuftragReport AS auftrag"
					+ " WHERE auftrag.i_id IN"
							+ " (" + getSelectFromClauseRechnungPosition()
							+ buildWhereClauseRechnungPosition() + ")"
					+ " OR auftrag.i_id IN"
							+ " (" + getSelectFromClauseRechnung()
							+ buildWhereClauseRechnung() + ")"
					+ buildOrderByClause();

			// myLogger.info("HQL: " + queryString);
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();

			HashMap<?, ?> hmStatus = getSystemMultilanguageFac()
					.getAllStatiMitUebersetzung(
							theClientDto.getLocUi(), theClientDto);
			int row = 0;
			int col = 0;
			Object[][] rows = new Object[resultList.size()][colCount];
			Iterator<?> resultListIterator = resultList.iterator();

			while(resultListIterator.hasNext()) {
				FLRAuftragReport flrauftrag = (FLRAuftragReport) resultListIterator.next();
				
				rows[row][col++] = flrauftrag.getI_id();
				rows[row][col++] = flrauftrag.getC_nr();
				rows[row][col++] = flrauftrag.getC_bez();
				rows[row++][col++] = hmStatus.get(flrauftrag.getAuftragstatus_c_nr());

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

	/**
	 * gets the total number of rows represented by the current query.
	 * 
	 * @return int die Anzehl der Zeilen im Ergebnis
	 * @see UseCaseHandler#getRowCountFromDataBase()
	 */
	protected long getRowCountFromDataBase() {
		long rowCount = 0;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			String queryString = "SELECT auftrag.i_id FROM FLRAuftragReport AS auftrag"
					+ " WHERE auftrag.i_id IN"
							+ " (" + getSelectFromClauseRechnungPosition()
							+ buildWhereClauseRechnungPosition() + ")"
					+ " OR auftrag.i_id IN"
							+ " (" + getSelectFromClauseRechnung()
							+ buildWhereClauseRechnung() + ")"
					+ buildOrderByClause();
			Query query = session.createQuery(queryString);
			List<?> rowCountResult = query.list();
			rowCount = rowCountResult.size();
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (HibernateException he) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HIBERNATE,
							he);
				}
			}
		}
		return rowCount;
	}

	/**
	 * builds the where clause for the RechnungPosition part of the HQL statement
	 * using the current query.
	 * 
	 * @return the HQL where clause.
	 */
	private String buildWhereClauseRechnungPosition() {
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
					where.append(" rechposition." + filterKriterien[i].kritName);
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
	 * builds the where clause for the Rechnung part of the HQL statement
	 * using the current query.
	 * 
	 * @return the HQL where clause.
	 */
	private String buildWhereClauseRechnung() {
		StringBuffer where = new StringBuffer("");

		if (this.getQuery() != null && this.getQuery().getFilterBlock() != null
				&& this.getQuery().getFilterBlock().filterKrit != null) {

			FilterKriterium[] filterKriterien = this.getQuery()
					.getFilterBlock().filterKrit;

			if (filterKriterien.length > 0 && filterKriterien[0].isKrit) {
				where.append(" WHERE rechnung.i_id"
								+ " " + filterKriterien[0].operator
								+ " " + filterKriterien[0].value);
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
		// if (this.getQuery() != null) {
		// SortierKriterium[] kriterien = this.getQuery().getSortKrit();
		// boolean sortAdded = false;
		// if (kriterien != null && kriterien.length > 0) {
		// for (int i = 0; i < kriterien.length; i++) {
		// if (!kriterien[i].kritName.endsWith(Facade.NICHT_SORTIERBAR)) {
		// if (kriterien[i].isKrit) {
		// if (sortAdded) {
		// orderBy.append(", ");
		// }
		// sortAdded = true;
		// orderBy.append(FLR_LIEFERSCHEINPOSITION + kriterien[i].kritName);
		// orderBy.append(" ");
		// orderBy.append(kriterien[i].value);
		// }
		// }
		// }
		// }
		// else {
		// // no sort criteria found, add default sort
		// if (sortAdded) {
		// orderBy.append(", ");
		// }
		// orderBy
		// .append(FLR_LIEFERSCHEINPOSITION)
		// .append(LieferscheinpositionFac.FLR_LIEFERSCHEINPOSITION_FLRARTIKEL)
		// .append(".")
		// .append(ArtikelFac.FLR_ARTIKEL_C_NR)
		// .append(" ASC ");
		// sortAdded = true;
		// }
		// if (orderBy.indexOf(FLR_LIEFERSCHEINPOSITION +
		// LieferscheinpositionFac.FLR_LIEFERSCHEINPOSITION_I_ID) < 0) {
		// // unique sort required because otherwise rowNumber of selectedId
		// // within sort() method may be different from the position of
		// selectedId
		// // as returned in the page of getPageAt().
		// if (sortAdded) {
		// orderBy.append(", ");
		// }
		// orderBy
		// .append(" ")
		// .append(FLR_LIEFERSCHEINPOSITION)
		// .append(LieferscheinpositionFac.FLR_LIEFERSCHEINPOSITION_I_ID)
		// .append(" ");
		// sortAdded = true;
		// }
		// if (sortAdded) {
		// orderBy.insert(0, " ORDER BY ");
		// }
		// }
		orderBy.append(" ORDER BY ");
		orderBy.append("auftrag.c_nr");
		return orderBy.toString();
	}

	/**
	 * get the basic select from clause for the RechnungPosition part
	 * of the HQL statement.
	 * 
	 * @return the select from clause.
	 */
	private String getSelectFromClauseRechnungPosition() {
		
		return "SELECT flrauftrag.i_id FROM FLRRechnungPosition rechposition"
				+ " LEFT JOIN rechposition.flrpositionensichtauftrag.flrauftrag AS flrauftrag";
	}

	/**
	 * get the basic from clause for the Rechnung part
	 * of the HQL statement.
	 * 
	 * @return the select from clause.
	 */
	private String getSelectFromClauseRechnung() {
		
		return "SELECT flrauftrag.i_id FROM FLRRechnungReport AS rechnung"
				+ " LEFT JOIN rechnung.flrauftrag AS flrauftrag";
	}

	/**
	 * sorts the data described by the current query using the specified sort
	 * criterias. The current query is also updated with the new sort criterias.
	 * 
	 * @param sortierKriterien
	 *            nach diesen Kriterien wird das Ergebnis sortiert
	 * @param selectedId
	 *            auf diesem Datensatz soll der Cursor stehen
	 * @return QueryResult das Ergebnis der Abfrage
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @see UseCaseHandler#sort(SortierKriterium[], Object)
	 */
	public QueryResult sort(SortierKriterium[] sortierKriterien,
			Object selectedId) throws EJBExceptionLP {
		this.getQuery().setSortKrit(sortierKriterien);

		QueryResult result = null;
		int rowNumber = 0;

		if (selectedId != null && ((Integer) selectedId).intValue() >= 0) {
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();
				String queryString = "SELECT auftrag.i_id FROM FLRAuftragReport AS auftrag"
						+ " WHERE auftrag.i_id IN"
								+ " (" + getSelectFromClauseRechnungPosition()
								+ buildWhereClauseRechnungPosition() + ")"
						+ " OR auftrag.i_id IN"
								+ " (" + getSelectFromClauseRechnung()
								+ buildWhereClauseRechnung() + ")"
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
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, he);
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
			setTableInfo(new TableInfo(new Class[] { Integer.class,
					String.class, String.class, Icon.class }, new String[] {
					"i_id",
					getTextRespectUISpr("auft.auftragsnummer", mandantCNr,
							locUI),
					getTextRespectUISpr("lp.projekt", mandantCNr, locUI),
					getTextRespectUISpr("lp.status", mandantCNr, locUI) },
					new int[] {
							QueryParameters.FLR_BREITE_SHARE_WITH_REST, // hidden
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST },
					new String[] { Facade.NICHT_SORTIERBAR,
							Facade.NICHT_SORTIERBAR, Facade.NICHT_SORTIERBAR,
							Facade.NICHT_SORTIERBAR }));
		}

		return super.getTableInfo();
	}
}
