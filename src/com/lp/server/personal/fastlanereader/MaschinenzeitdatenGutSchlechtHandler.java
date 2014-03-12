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
package com.lp.server.personal.fastlanereader;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.personal.fastlanereader.generated.FLRMaschinenzeitdaten;
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
 * Hier wird die FLR Funktionalit&auml;t f&uuml;r die Zeitdaten implementiert. Pro UseCase
 * gibt es einen Handler.
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
public class MaschinenzeitdatenGutSchlechtHandler extends UseCaseHandler {

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
			session = setFilter(session);
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

				String dauer = null;

				FLRMaschinenzeitdaten zeitdaten = (FLRMaschinenzeitdaten) resultListIterator
						.next();

				rows[row][col++] = zeitdaten.getI_id();
				rows[row][col++] = zeitdaten.getFlrmaschine()
						.getC_inventarnummer();

				rows[row][col++] = zeitdaten.getT_von();
				rows[row][col++] = zeitdaten.getT_bis();

				String los = zeitdaten.getFlrlossollarbeitsplan().getFlrlos()
						.getC_nr()
						+ " AG:"
						+ zeitdaten.getFlrlossollarbeitsplan()
								.getI_arbeitsgangsnummer();

				if (zeitdaten.getFlrlossollarbeitsplan()
						.getI_unterarbeitsgang() != null) {
					los += " UAG:"
							+ zeitdaten.getFlrlossollarbeitsplan()
									.getI_unterarbeitsgang();
				}

				rows[row][col++] = los;
				if (zeitdaten.getT_bis() != null) {

					long l_zeitdec = zeitdaten.getT_bis().getTime()
							- zeitdaten.getT_von().getTime();

					Double d = Helper.time2Double(new java.sql.Time(
							l_zeitdec - 3600000));

					java.text.DecimalFormat df = new java.text.DecimalFormat(
							"00.00");
					dauer = df.format(d.doubleValue()).toString();
				} else {
					dauer = null;
				}
				rows[row][col++] = dauer;

				rows[row][col++] = zeitdaten.getC_bemerkung();

				String starter = zeitdaten.getFlrpersonal_gestartet()
						.getFlrpartner().getC_name1nachnamefirmazeile1();

				if (zeitdaten.getFlrpersonal_gestartet().getFlrpartner()
						.getC_name2vornamefirmazeile2() != null) {
					starter += " "
							+ zeitdaten.getFlrpersonal_gestartet()
									.getFlrpartner()
									.getC_name2vornamefirmazeile2();
				}

				rows[row++][col++] = starter;

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
					if (filterKriterien[i].isBIgnoreCase()) {
						where.append(" upper(maschinenzeitdaten."
								+ filterKriterien[i].kritName + ")");
					} else {
						where.append(" maschinenzeitdaten."
								+ filterKriterien[i].kritName);
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
					// nodbfeld: 2: Hier alle Spaltennamen, die mit X enden beim
					// sortieren ignorieren.
					if (!kriterien[i].kritName
							.endsWith(Facade.NICHT_SORTIERBAR)) {
						if (kriterien[i].isKrit) {
							if (sortAdded) {
								orderBy.append(", ");
							}
							sortAdded = true;
							orderBy.append("maschinenzeitdaten."
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
				orderBy.append("maschinenzeitdaten.flrmaschine.c_identifikationsnr ASC, maschinenzeitdaten."
						+ ZeiterfassungFac.FLR_MASCHINENZEITDATEN_T_VON
						+ " DESC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("maschinenzeitdaten.flrmaschine.c_identifikationsnr ASC, maschinenzeitdaten."
					+ ZeiterfassungFac.FLR_MASCHINENZEITDATEN_T_VON) < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append("maschinenzeitdaten.flrmaschine.c_identifikationsnr ASC,  maschinenzeitdaten."
						+ ZeiterfassungFac.FLR_MASCHINENZEITDATEN_T_VON + " ");
				sortAdded = true;
			}
			if (sortAdded) {
				orderBy.insert(0, " ORDER BY ");
			}
		}
		return orderBy.toString();
	}

	private String getFromClause() {
		return "from FLRMaschinenzeitdaten maschinenzeitdaten ";
	}

	public QueryResult sort(SortierKriterium[] sortierKriterien,
			Object selectedId) throws EJBExceptionLP {
		this.getQuery().setSortKrit(sortierKriterien);

		QueryResult result = null;
		int rowNumber = 0;

		if (selectedId != null && selectedId instanceof Integer
				&& ((Integer) selectedId).intValue() >= 0) {
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();
				String queryString = "select maschinenzeitdaten.i_id from FLRMaschinenzeitdaten maschinenzeitdaten "
						+ this.buildWhereClause() + this.buildOrderByClause();
				Query query = session.createQuery(queryString);
				ScrollableResults scrollableResult = query.scroll();
				if (scrollableResult != null) {
					scrollableResult.beforeFirst();
					while (scrollableResult.next()) {
						Integer id = scrollableResult.getInteger(0);
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
					new Class[] { Integer.class, String.class, Timestamp.class,
							Timestamp.class, String.class, String.class,
							String.class, String.class, },
					new String[] {
							"Id",

							getTextRespectUISpr("lp.maschine", mandantCNr,
									locUI),
							getTextRespectUISpr("lp.von", mandantCNr, locUI),
							getTextRespectUISpr("lp.bis", mandantCNr, locUI),
							getTextRespectUISpr("lp.los", mandantCNr, locUI),
							getTextRespectUISpr("lp.dauer", mandantCNr, locUI),
							getTextRespectUISpr("lp.bem", mandantCNr, locUI),
							getTextRespectUISpr("lp.gestartetvon", mandantCNr,
									locUI) },
					new int[] {
							-1, // diese Spalte wird ausgeblendet

							QueryParameters.FLR_BREITE_XM,
							QueryParameters.FLR_BREITE_XM,
							QueryParameters.FLR_BREITE_XM,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_XM,
							QueryParameters.FLR_BREITE_XM,
							QueryParameters.FLR_BREITE_L },
					new String[] {
							"i_id",
							ZeiterfassungFac.FLR_MASCHINENZEITDATEN_FLRMASCHINE
									+ ".c_identifikationsnr",
							ZeiterfassungFac.FLR_MASCHINENZEITDATEN_T_VON,
							ZeiterfassungFac.FLR_MASCHINENZEITDATEN_T_BIS,
							Facade.NICHT_SORTIERBAR,
							Facade.NICHT_SORTIERBAR,
							ZeiterfassungFac.FLR_MASCHINENZEITDATEN_C_BEMERKUNG,
							Facade.NICHT_SORTIERBAR, }));

		}

		return super.getTableInfo();
	}
}
