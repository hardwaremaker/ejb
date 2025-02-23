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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.finanz.fastlanereader.generated.FLRFinanzMahnlauf;
import com.lp.server.finanz.service.FinanzFac;
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
 * Hier wird die FLR Funktionalitaet fuer die Mahnlaeufeimplementiert. Pro
 * UseCase gibt es einen Handler.
 * </p>
 * <p>
 * Copright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum 2005-01-09
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version 1.0
 */

public class MahnlaufHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Boolean hasZusatzfunktionSepaLastschrift;

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
				FLRFinanzMahnlauf mahnlauf = (FLRFinanzMahnlauf) resultListIterator
						.next();
				rows[row][col++] = mahnlauf.getI_id();
				rows[row][col++] = mahnlauf.getT_anlegen();
				rows[row][col++] = mahnlauf.getFlrpersonalanleger()
						.getC_kurzzeichen();
				rows[row][col++] = getMahnungsCount(mahnlauf.getI_id());
				rows[row][col++] = getErledigtCount(mahnlauf.getI_id());
				
				if (hasSepaLastschriftZusatzfunktion()) {
					rows[row][col++] = getLastschriftCount(mahnlauf.getI_id());
					rows[row][col++] = getErledigtCountLastschrift(mahnlauf.getI_id());
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
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, he);
			}
		}
		return result;
	}

	/**
	 * gets the total number of rows represented by the current query.
	 * 
	 * @see UseCaseHandler#getRowCountFromDataBase()
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
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			try {
				session.close();
			} catch (HibernateException he) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, he);
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
					where.append(" mahnlauf." + filterKriterien[i].kritName);
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
						orderBy.append("mahnlauf." + kriterien[i].kritName);
						orderBy.append(" ");
						orderBy.append(kriterien[i].value);
					}
				}
			} else {
				// no sort criteria found, add default sort
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append("mahnlauf.t_anlegen DESC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("mahnlauf.t_anlegen") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" mahnlauf.t_anlegen ");
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
		return "from FLRFinanzMahnlauf mahnlauf ";
	}

	/**
	 * sorts the data described by the current query using the specified sort
	 * criterias. The current query is also updated with the new sort criterias.
	 * 
	 * @see UseCaseHandler#sort(SortierKriterium[], Object)
	 * @throws EJBExceptionLP
	 * @param sortierKriterien
	 *            SortierKriterium[]
	 * @param selectedId
	 *            Object
	 * @return QueryResult
	 */
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
				String queryString = "select mahnlauf.i_id from FLRFinanzMahnlauf mahnlauf "
						+ this.buildWhereClause() + this.buildOrderByClause();
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

	public TableInfo getTableInfo() {
		if (super.getTableInfo() == null) {
			TableInfo tableInfo = new TableInfo(getTableInfoClasses(), 
					getTableInfoHeaderValues(), getColumnHeaderWidths(), getDBColumnNames());
			setTableInfo(tableInfo);
		}
		return super.getTableInfo();
	}
	
	private String[] getDBColumnNames() {
		List<String> dbColumnNames = new ArrayList<String>();
		dbColumnNames.add(FinanzFac.FLR_MAHNLAUF_I_ID);
		dbColumnNames.add(FinanzFac.FLR_MAHNLAUF_T_ANLEGEN);
		dbColumnNames.add(FinanzFac.FLR_MAHNUNG_FLRPERSONALANLEGER);
		dbColumnNames.add(FinanzFac.FLR_MAHNLAUF_I_ID);
		dbColumnNames.add(FinanzFac.FLR_MAHNLAUF_I_ID);
		
		if (hasSepaLastschriftZusatzfunktion()) {
			dbColumnNames.add(FinanzFac.FLR_MAHNLAUF_I_ID);
			dbColumnNames.add(FinanzFac.FLR_MAHNLAUF_I_ID);
		}
		
		return dbColumnNames.toArray(new String[dbColumnNames.size()]);
	}
	
	private Class[] getTableInfoClasses() {
		List<Class> classes = new ArrayList<Class>();
		classes.add(Integer.class);
		classes.add(Timestamp.class);
		classes.add(String.class);
		classes.add(Integer.class);
		classes.add(Integer.class);
		if (hasSepaLastschriftZusatzfunktion()) {
			classes.add(Integer.class);
			classes.add(Integer.class);
		}
		return classes.toArray(new Class[classes.size()]);
	}

	private String[] getTableInfoHeaderValues() {
		String mandantCNr = theClientDto.getMandant();
		Locale locUI = theClientDto.getLocUi();
		List<String> headerValues = new ArrayList<String>();
		headerValues.add("Id");
		headerValues.add(getTextRespectUISpr("lp.datum", mandantCNr, locUI));
		headerValues.add(getTextRespectUISpr("lp.erzeuger",
				theClientDto.getMandant(), theClientDto.getLocUi()));
		headerValues.add(getTextRespectUISpr("lp.mahnungen", mandantCNr, locUI));
		headerValues.add(getTextRespectUISpr("auft.offen", mandantCNr, locUI));
		if (hasSepaLastschriftZusatzfunktion()) {
			headerValues.add(getTextRespectUISpr("rechnung.lastschriftvorschlag", mandantCNr, locUI));
			headerValues.add(getTextRespectUISpr("auft.offen", mandantCNr, locUI));
		}
		return headerValues.toArray(new String[headerValues.size()]);
	}

	private int[] getColumnHeaderWidths() {
		int arraySize = 5 + (hasSepaLastschriftZusatzfunktion() ? 2 : 0);
		int[] headerValues = new int[arraySize];
		headerValues[0] = -1;
		headerValues[1] = QueryParameters.FLR_BREITE_SHARE_WITH_REST;
		headerValues[2] = QueryParameters.FLR_BREITE_SHARE_WITH_REST;
		headerValues[3] = QueryParameters.FLR_BREITE_SHARE_WITH_REST;
		headerValues[4] = QueryParameters.FLR_BREITE_SHARE_WITH_REST;
		if (hasSepaLastschriftZusatzfunktion()) {
			headerValues[5] = QueryParameters.FLR_BREITE_SHARE_WITH_REST;
			headerValues[6] = QueryParameters.FLR_BREITE_SHARE_WITH_REST;
		}
		return headerValues;
	}

	private int getMahnungsCount(Integer MahnlaufIId) {
		int rowCount = 0;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			String queryString = "select count(*) from FLRFinanzMahnung mahnung WHERE mahnung.mahnlauf_i_id = "
					+ MahnlaufIId;
			Query query = session.createQuery(queryString);
			List<?> rowCountResult = query.list();
			if (rowCountResult != null && rowCountResult.size() > 0) {
				rowCount = ((Long) rowCountResult.get(0)).intValue();
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			closeSession(session);
		}
		return rowCount;
	}

	private int getErledigtCount(Integer MahnlaufIId) {
		int rowCount = 0;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			String queryString = "select count(*) from FLRFinanzMahnung mahnung WHERE mahnung.mahnlauf_i_id = "
					+ MahnlaufIId + "and mahnung.t_gedruckt is null";
			Query query = session.createQuery(queryString);
			List<?> rowCountResult = query.list();
			if (rowCountResult != null && rowCountResult.size() > 0) {
				rowCount = ((Long) rowCountResult.get(0)).intValue();
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			closeSession(session);
		}
		return rowCount;

	}

	private int getLastschriftCount(Integer mahnlaufIId) {
		int count = 0;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			String queryString = "select count(*) from FLRLastschriftvorschlag lastschriftvorschlag "
					+ " WHERE lastschriftvorschlag.mahnlauf_i_id = " + mahnlaufIId;
			Query query = session.createQuery(queryString);
			List<?> rowCountResult = query.list();
			if (rowCountResult != null && rowCountResult.size() > 0) {
				count = ((Long) rowCountResult.get(0)).intValue();
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			closeSession(session);
		}
		return count;
	}
	
	private int getErledigtCountLastschrift(Integer mahnlaufIId) {
		int count = 0;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			String queryString = "select count(*) from FLRLastschriftvorschlag lastschriftvorschlag WHERE lastschriftvorschlag.mahnlauf_i_id = "
					+ mahnlaufIId + "and lastschriftvorschlag.t_gespeichert is null";
			Query query = session.createQuery(queryString);
			List<?> rowCountResult = query.list();
			if (rowCountResult != null && rowCountResult.size() > 0) {
				count = ((Long) rowCountResult.get(0)).intValue();
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			closeSession(session);
		}
		return count;
	}

	private boolean hasSepaLastschriftZusatzfunktion() {
		if (hasZusatzfunktionSepaLastschrift == null) {
			hasZusatzfunktionSepaLastschrift = 
					getMandantFac().hatZusatzfunktionSepaLastschrift(theClientDto);
		}
		return hasZusatzfunktionSepaLastschrift;
	}
}
