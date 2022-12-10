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

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.personal.fastlanereader.generated.FLRGleitzeitsaldo;
import com.lp.server.personal.service.KollektivDto;
import com.lp.server.personal.service.PersonalFac;
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

public class GleitzeitsaldoHandler extends UseCaseHandler {

	/**
	 * the size of the data page returned in QueryResult.
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The information needed for the kundes table.
	 */

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
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
			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;
			int col = 0;

			java.text.DateFormatSymbols symbols = new java.text.DateFormatSymbols(
					theClientDto.getLocUi());
			String[] defaultMonths = symbols.getMonths();

			while (resultListIterator.hasNext()) {
				FLRGleitzeitsaldo gleitzeitsaldo = (FLRGleitzeitsaldo) resultListIterator
						.next();
				rows[row][col++] = gleitzeitsaldo.getI_id();
				rows[row][col++] = gleitzeitsaldo.getI_jahr();

				rows[row][col++] = defaultMonths[gleitzeitsaldo.getI_monat()
						.intValue()];
				rows[row][col++] = gleitzeitsaldo.getN_saldo();
				
				
				Integer kollektivIId = getPersonalFac()
						.personalFindByPrimaryKey(
								gleitzeitsaldo.getPersonal_i_id(),
								theClientDto).getKollektivIId();
				
				BigDecimal bdErrechneteNormalstunden = BigDecimal.ZERO;
				
				if (kollektivIId != null) {
					try {
						KollektivDto kDto = getPersonalFac()
								.kollektivFindByPrimaryKey(kollektivIId);

						bdErrechneteNormalstunden = bdErrechneteNormalstunden
								.add(gleitzeitsaldo.getN_saldomehrstunden()
										.multiply(kDto.getNFaktormehrstd()));
						bdErrechneteNormalstunden = bdErrechneteNormalstunden
								.add(gleitzeitsaldo.getN_saldouest200().multiply(
										kDto.getNFaktoruestd200()));
						bdErrechneteNormalstunden = bdErrechneteNormalstunden
								.add(gleitzeitsaldo.getN_saldouestfrei100()
										.multiply(kDto.getNFaktoruestd100()));
						bdErrechneteNormalstunden = bdErrechneteNormalstunden
								.add(gleitzeitsaldo.getN_saldouestpflichtig100()
										.multiply(kDto.getNFaktoruestd100()));
						bdErrechneteNormalstunden = bdErrechneteNormalstunden
								.add(gleitzeitsaldo.getN_saldouestfrei50()
										.multiply(kDto.getNFaktoruestd50()));
						bdErrechneteNormalstunden = bdErrechneteNormalstunden
								.add(gleitzeitsaldo.getN_saldouestpflichtig50()
										.multiply(kDto.getNFaktoruestd50()));

					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
				}

				rows[row][col++] = bdErrechneteNormalstunden;
				
				
				
				rows[row][col++] = gleitzeitsaldo.getN_saldomehrstunden();
				rows[row][col++] = gleitzeitsaldo.getN_saldouestfrei50();
				rows[row][col++] = gleitzeitsaldo.getN_saldouestpflichtig50();
				rows[row][col++] = gleitzeitsaldo.getN_saldouestfrei100();
				rows[row][col++] =  gleitzeitsaldo
				.getN_saldouestpflichtig100();
				rows[row][col++] = gleitzeitsaldo.getN_saldouest200();
				rows[row++][col++] = gleitzeitsaldo.getN_gz_saldo_mit_uestd_in_normalstunden();

				col = 0;
			}
			result = new QueryResult(rows, this.getRowCount(), startIndex,
					endIndex, 0);
		} catch (HibernateException e) {
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
					where.append(" gleitzeitsaldo."
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
						orderBy.append("gleitzeitsaldo."
								+ kriterien[i].kritName);
						orderBy.append(" ");
						orderBy.append(kriterien[i].value);
					}
				}
			} else {
				// no sort criteria found, add default sort
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append("gleitzeitsaldo."
						+ PersonalFac.FLR_GLEITZEITSALDO_I_JAHR
						+ " DESC ,gleitzeitsaldo."
						+ PersonalFac.FLR_GLEITZEITSALDO_I_MONAT + " DESC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("gleitzeitsaldo."
					+ PersonalFac.FLR_GLEITZEITSALDO_I_JAHR) < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" gleitzeitsaldo."
						+ PersonalFac.FLR_GLEITZEITSALDO_I_JAHR + " ");
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
		return "from FLRGleitzeitsaldo gleitzeitsaldo ";
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
				String queryString = "select gleitzeitsaldo.i_id from FLRGleitzeitsaldo gleitzeitsaldo "
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
			setTableInfo(new TableInfo(
					new Class[] { Integer.class, Integer.class, String.class,
							java.math.BigDecimal.class,
							java.math.BigDecimal.class,
							java.math.BigDecimal.class,
							java.math.BigDecimal.class,
							java.math.BigDecimal.class,
							java.math.BigDecimal.class,
							java.math.BigDecimal.class,
							java.math.BigDecimal.class,
							java.math.BigDecimal.class },
					new String[] {
							"Id",
							getTextRespectUISpr("lp.jahr",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.monat",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.saldo",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("pers.gleitzeitsaldo.uest.in.normalstunden",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.mehrstd",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr(
									"pers.gleitzeitsaldo.ueberstundenfrei50",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr(
									"pers.gleitzeitsaldo.steuerpflichtig50",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr(
									"pers.gleitzeitsaldo.ueberstundenfrei100",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr(
									"pers.gleitzeitsaldo.steuerpflichtig100",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("pers.gleitzeitsaldo.200",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),getTextRespectUISpr("pers.gleitzeitsaldo.gzsaldo.mituest.in.normalstunden",
											theClientDto.getMandant(),
											theClientDto.getLocUi()) },

					new int[] {
							-1, // diese Spalte wird ausgeblendet
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST },

					new String[] {
							"i_id",
							PersonalFac.FLR_GLEITZEITSALDO_I_JAHR,
							PersonalFac.FLR_GLEITZEITSALDO_I_MONAT,
							PersonalFac.FLR_GLEITZEITSALDO_N_SALDO,
							Facade.NICHT_SORTIERBAR,
							PersonalFac.FLR_GLEITZEITSALDO_N_SALDOMEHRST,
							PersonalFac.FLR_GLEITZEITSALDO_N_SALDOUESTFREI50,
							PersonalFac.FLR_GLEITZEITSALDO_N_SALDOUESTPFLICHTIG50,
							PersonalFac.FLR_GLEITZEITSALDO_N_SALDOUESTFREI100,
							PersonalFac.FLR_GLEITZEITSALDO_N_SALDOUESTPFLICHTIG100,
							PersonalFac.FLR_GLEITZEITSALDO_N_SALDOUEST200,
							PersonalFac.FLR_GLEITZEITSALDO_N_GZ_SALDO_MIT_UESTD_IN_NORMALSTUNDEN }));

		}
		return super.getTableInfo();
	}
}
