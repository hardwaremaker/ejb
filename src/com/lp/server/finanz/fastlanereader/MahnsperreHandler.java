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

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnungReport;
import com.lp.server.rechnung.service.RechnungFac;
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
 * Hier wird die FLR Funktionalitaet fuer die Mahnungen implementiert. Pro
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

public class MahnsperreHandler extends UseCaseHandler {

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
				FLRRechnungReport rechnung = (FLRRechnungReport) ((Object[]) resultListIterator
						.next())[0];
				rows[row][col++] = rechnung.getI_id();
				rows[row][col++] = rechnung.getFlrrechnungart().getC_nr()
						.substring(0, 1);
				rows[row][col++] = rechnung.getC_nr();
				rows[row][col++] = rechnung.getFlrkunde().getFlrpartner()
						.getC_name1nachnamefirmazeile1();

				if (rechnung.getN_wert() != null
						&& rechnung.getN_kurs().doubleValue() != 0) {

					if (rechnung.getN_kurs().doubleValue() != 0) {

						BigDecimal bdBruttoGesamt = rechnung.getN_wert()
								.multiply(rechnung.getN_kurs()).add(
										rechnung.getN_wertust().multiply(
												rechnung.getN_kurs()));

						if (rechnung.getFlrrechnungart().getRechnungtyp_c_nr()
								.equals(RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
							bdBruttoGesamt = bdBruttoGesamt.negate();
						}
						rows[row][col++] = bdBruttoGesamt;

						BigDecimal bdBezahltFw = getRechnungFac()
								.getBereitsBezahltWertVonRechnungFw(
										rechnung.getI_id(), null);
						BigDecimal bdBezahltUstFw = getRechnungFac()
								.getBereitsBezahltWertVonRechnungUstFw(
										rechnung.getI_id(), null);

						rows[row][col++] = bdBruttoGesamt.subtract(bdBezahltFw
								.add(bdBezahltUstFw));

					} else {
						rows[row][col++] = null;
						rows[row][col++] = null;
					}
				} else {
					rows[row][col++] = null;
					rows[row][col++] = null;
				}

				rows[row][col++] = rechnung.getWaehrung_c_nr();
				if (rechnung.getFlrvertreter() != null) {
					rows[row][col++] = rechnung.getFlrvertreter()
							.getC_kurzzeichen();
				} else {
					rows[row][col++] = "";
				}
				rows[row][col++] = getMahnwesenFac()
						.getAktuelleMahnstufeEinerRechnung(rechnung.getI_id(),
								theClientDto);
				// in java.util.Date casten, darf kein java.sql.Date sein sonst
				// geht FLR Druck nicht.
				java.util.Date d_UtilDate = null;
				if (rechnung.getT_mahnsperrebis() instanceof java.sql.Date) {
					d_UtilDate = new java.util.Date(rechnung
							.getT_mahnsperrebis().getTime());
				} else {
					d_UtilDate = rechnung.getT_mahnsperrebis();
				}
				rows[row][col++] = d_UtilDate;
				rows[row][col++] = getRechnungFac()
						.getDatumLetzterZahlungseingang(rechnung.getI_id());

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
					where.append(" rechnung." + filterKriterien[i].kritName);
					where.append(" " + filterKriterien[i].operator);
					where.append(" " + filterKriterien[i].value);

					if (filterKriterien[i].kritName.equals("t_mahnsperrebis")) {
						where
								.append(" AND (  rechnung.t_storniert IS NULL  OR  rechnung.t_storniert >= "
										+ filterKriterien[i].value
										+ " ) AND (rechnung.t_bezahltdatum IS NULL  OR rechnung.t_bezahltdatum >= "
										+ filterKriterien[i].value + ") ");

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
					if (!kriterien[i].kritName
							.endsWith(Facade.NICHT_SORTIERBAR)) {
						if (kriterien[i].isKrit) {
							if (sortAdded) {
								orderBy.append(", ");
							}
							sortAdded = true;
//							orderBy.append("rechnung." + kriterien[i].kritName);
							orderBy.append(kriterien[i].kritName);
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
				orderBy.append("rechnung.c_nr ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("rechnung.c_nr ") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" rechnung.c_nr  ");
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
		return "from FLRRechnungReport as rechnung "
				+ " left join rechnung.flrvertreter as flrvertreter ";
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
				String queryString = "select rechnung.i_id from FLRRechnungReport rechnung "
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
			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();
			setTableInfo(new TableInfo(
					new Class[] { Integer.class, String.class, String.class,
							String.class, BigDecimal.class, BigDecimal.class,
							String.class, String.class, Integer.class,
							java.util.Date.class, java.util.Date.class },
					new String[] {
							"Id",
							"",
							getTextRespectUISpr("lp.rechnr", mandantCNr, locUI),
							getTextRespectUISpr("lp.kunde", mandantCNr, locUI),
							getTextRespectUISpr("lp.bruttowert", mandantCNr,
									locUI),
							getTextRespectUISpr("lp.bruttooffen", mandantCNr,
									locUI),
							getTextRespectUISpr("lp.whg", mandantCNr, locUI),
							getTextRespectUISpr("lp.vertreter", mandantCNr,
									locUI),
							getTextRespectUISpr("lp.m", mandantCNr, locUI),
							getTextRespectUISpr("lp.mahnsperrebis", mandantCNr,
									locUI),
							getTextRespectUISpr(
									"fb.mahnsperre.letzterzahlungseingang",
									mandantCNr, locUI) },
					new int[] { -1, QueryParameters.FLR_BREITE_XXS,
							QueryParameters.FLR_BREITE_M, -1,
							QueryParameters.FLR_BREITE_PREIS,
							QueryParameters.FLR_BREITE_PREIS,
							QueryParameters.FLR_BREITE_WAEHRUNG,
							QueryParameters.FLR_BREITE_XS, 2,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_PREIS },
					new String[] {
							"i_id",
							Facade.NICHT_SORTIERBAR,
							RechnungFac.FLR_RECHNUNG_C_NR,
							"rechnung."+
							RechnungFac.FLR_RECHNUNG_FLRKUNDE
									+ "."
									+ KundeFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
							RechnungFac.FLR_RECHNUNG_N_WERTFW,
							Facade.NICHT_SORTIERBAR,
							RechnungFac.FLR_RECHNUNG_WAEHRUNG_C_NR,
							RechnungFac.FLR_RECHNUNG_FLRVERTRETER + "."
									+ PersonalFac.FLR_PERSONAL_C_KURZZEICHEN,
							Facade.NICHT_SORTIERBAR,
							RechnungFac.FLR_RECHNUNG_T_MAHNSPERREBIS,
							FinanzFac.FLR_MAHNUNG_T_GEDRUCKT }));
		}
		return super.getTableInfo();
	}
}
