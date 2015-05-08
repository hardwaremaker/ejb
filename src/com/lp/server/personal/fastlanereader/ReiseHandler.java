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

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.fastlanereader.generated.FLRReise;
import com.lp.server.personal.service.ZeiterfassungFac;
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
import com.lp.util.Helper;

/**
 * <p>
 * Hier wird die FLR Funktionalit&auml;t f&uuml;r den Personal implementiert. Pro UseCase
 * gibt es einen Handler.
 * </p>
 * <p>
 * Copright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum 2004-11-02
 * </p>
 * <p>
 * </p>
 * 
 * @author ck
 * @version 1.0
 */

public class ReiseHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String[] kurzeWochentage = null;

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = ReiseHandler.PAGE_SIZE;
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
			FLRReise letzterReiseeintrag = null;

			while (resultListIterator.hasNext()) {
				FLRReise reise = (FLRReise) resultListIterator.next();
				rows[row][col++] = reise.getI_id();

				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(reise.getT_zeit().getTime());
				String tag = kurzeWochentage[cal.get(Calendar.DAY_OF_WEEK)];

				if (row > 0
						&& Helper.short2boolean(reise.getB_beginn()) == true
						&& Helper.short2boolean(letzterReiseeintrag
								.getB_beginn()) == false) {
					rows[row - 1][col] = "/";
				}
				if (row > 0
						&& Helper.short2boolean(reise.getB_beginn()) == false
						&& Helper.short2boolean(letzterReiseeintrag
								.getB_beginn()) == true) {
					rows[row - 1][col] = "\\";
				}
				if (resultListIterator.hasNext() == false
						&& Helper.short2boolean(reise.getB_beginn()) == true) {
					rows[row][col++] = "\\";
				} else {
					rows[row][col++] = "|";
				}

				rows[row][col++] = tag;
				rows[row][col++] = reise.getT_zeit();
				rows[row][col++] = !Helper.short2boolean(reise.getB_beginn());
				if (reise.getFlrdiaeten() != null) {
					rows[row][col++] = reise.getFlrdiaeten().getC_bez();
				} else {
					rows[row][col++] = null;
				}
				rows[row][col++] = reise.getC_kommentar();
				if (reise.getFlrpartner() != null) {
					String firma = reise.getFlrpartner()
							.getC_name1nachnamefirmazeile1();
					if (reise.getFlrpartner().getC_name2vornamefirmazeile2() != null) {
						firma += " "
								+ reise.getFlrpartner()
										.getC_name2vornamefirmazeile2();
					}
					rows[row][col++] = firma;
				} else {
					rows[row][col++] = null;
				}
				if (reise.getI_kmbeginn() != null
						&& reise.getI_kmende() != null) {
					rows[row][col++] = reise.getI_kmende()
							- reise.getI_kmbeginn();
				} else {
					rows[row][col++] = null;
				}
				row++;
				letzterReiseeintrag = reise;
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
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, he);
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
					where.append(" reise." + filterKriterien[i].kritName);
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
					if (!kriterien[i].kritName
							.endsWith(Facade.NICHT_SORTIERBAR)) {
						if (kriterien[i].isKrit) {
							if (sortAdded) {
								orderBy.append(", ");
							}
							sortAdded = true;
							orderBy.append("reise." + kriterien[i].kritName);
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
				orderBy.append("reise." + ZeiterfassungFac.FLR_REISE_T_ZEIT
						+ " DESC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("reise.i_id") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" reise.i_id ");
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
		return "from FLRReise reise ";
	}

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
				String queryString = "select reise.i_id from FLRReise reise "
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
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
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
			setTableInfo(new TableInfo(
					new Class[] { Integer.class, String.class, String.class,
							java.sql.Timestamp.class, java.lang.Boolean.class,
							String.class, String.class, String.class,
							java.lang.Integer.class },
					new String[] {
							"Id",
							"",
							getTextRespectUISpr("lp.tag", theClientDto
									.getMandant(), theClientDto.getLocUi()),
							getTextRespectUISpr("lp.zeit", theClientDto
									.getMandant(), theClientDto.getLocUi()),
							getTextRespectUISpr("lp.ende", theClientDto
									.getMandant(), theClientDto.getLocUi()),
							getTextRespectUISpr("pers.reiseland", theClientDto
									.getMandant(), theClientDto.getLocUi()),
							getTextRespectUISpr("lp.kommentar", theClientDto
									.getMandant(), theClientDto.getLocUi()),
							getTextRespectUISpr("lp.kunde", theClientDto
									.getMandant(), theClientDto.getLocUi()),
							getTextRespectUISpr("lp.entfernung", theClientDto
									.getMandant(), theClientDto.getLocUi()) },
					new int[] {
							-1, // diese Spalte wird ausgeblendet
							QueryParameters.FLR_BREITE_S,
							QueryParameters.FLR_BREITE_XS,
							QueryParameters.FLR_BREITE_XM,
							QueryParameters.FLR_BREITE_XS,
							QueryParameters.FLR_BREITE_XS,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_XL,
							QueryParameters.FLR_BREITE_M },

					new String[] {
							"i_id",
							Facade.NICHT_SORTIERBAR,
							Facade.NICHT_SORTIERBAR,
							ZeiterfassungFac.FLR_REISE_T_ZEIT,
							ZeiterfassungFac.FLR_REISE_B_BEGINN,
							ZeiterfassungFac.FLR_REISE_FLRDIAETEN + ".c_bez",
							ZeiterfassungFac.FLR_REISE_C_KOMMENTAR,
							ZeiterfassungFac.FLR_REISE_FLRPARTNER
									+ "."
									+ PartnerFac.FLR_PARTNER_C_NAME1NACHNAMEFIRMAZEILE1,
							Facade.NICHT_SORTIERBAR }));

			kurzeWochentage = new DateFormatSymbols(theClientDto.getLocUi())
					.getShortWeekdays();

		}

		return super.getTableInfo();
	}
}
