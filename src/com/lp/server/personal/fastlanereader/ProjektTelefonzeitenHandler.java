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

import java.awt.Color;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.fastlanereader.generated.FLRTelefonzeiten;
import com.lp.server.personal.service.PersonalgehaltDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.util.Facade;
import com.lp.server.util.HelperServer;
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
 * Hier wird die FLR Funktionalit&auml;t f&uuml;r den Personal implementiert.
 * Pro UseCase gibt es einen Handler.
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

public class ProjektTelefonzeitenHandler extends UseCaseHandler {

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
			int pageSize = TelefonzeitenHandler.PAGE_SIZE;
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
				FLRTelefonzeiten reise = (FLRTelefonzeiten) resultListIterator
						.next();
				rows[row][col++] = reise.getI_id();

				rows[row][col++] = HelperServer.formatNameAusFLRPartner(reise
						.getFlrpersonal().getFlrpartner());

				rows[row][col++] = reise.getT_von();
				rows[row][col++] = reise.getT_bis();

				// Dauer

				if (reise.getT_bis() != null) {

					rows[row][col++] = new Double(((double) (reise.getT_bis()
							.getTime() - reise.getT_von().getTime()) / 3600000));
				} else {

				}
				// Kosten
				// Die Kosten kommen aus dem Stundensatz
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(reise.getT_von().getTime());

				PersonalgehaltDto pgDto = getPersonalFac()
						.personalgehaltFindLetztePersonalgehalt(
								reise.getPersonal_i_id(), c.get(Calendar.YEAR),
								c.get(Calendar.MONTH));
				if (pgDto != null && pgDto.getNStundensatz() != null) {

					Double dauer = new Double(((double) (reise.getT_bis()
							.getTime() - reise.getT_von().getTime()) / 3600000));

					rows[row][col++] = pgDto.getNStundensatz().multiply(
							new BigDecimal(dauer));
				} else {
					rows[row][col++] = null;
				}

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
				rows[row][col++] = Helper.removeStyles(reise
						.getX_kommentarext());

				boolean isTimeOK = true;
				if (Helper.cutDate(reise.getT_von()).compareTo(
						Helper.cutDate(reise.getT_bis())) != 0) {
					isTimeOK = false;
				} else if (reise.getT_von().after(reise.getT_bis())) {
					isTimeOK = false;
				}
				rows[row][col++] = isTimeOK ? null : Color.red;

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

					if (filterKriterien[i].kritName.equals("c_volltext")) {
						where.append(" (lower(cast(x_kommentarint as string))");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase());
						where.append(" OR lower(cast(x_kommentarext as string))");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase() + ")");
					} else {

						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" lower("
									+ filterKriterien[i].kritName + ")");
						} else {
							where.append(" " + filterKriterien[i].kritName);
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
					if (!kriterien[i].kritName
							.endsWith(Facade.NICHT_SORTIERBAR)) {
						if (kriterien[i].isKrit) {
							if (sortAdded) {
								orderBy.append(", ");
							}
							sortAdded = true;
							orderBy.append("telefonzeiten."
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
				orderBy.append("telefonzeiten."
						+ ZeiterfassungFac.FLR_TELEFONZEITEN_T_VON + " DESC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("telefonzeiten.i_id") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" telefonzeiten.i_id ");
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
		return "from FLRTelefonzeiten telefonzeiten ";
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
				String queryString = "select telefonzeiten.i_id from FLRTelefonzeiten telefonzeiten "
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
					new Class[] { Integer.class, String.class,
							java.sql.Timestamp.class, java.sql.Timestamp.class,
							Double.class, BigDecimal.class, String.class,
							String.class, Color.class },
					new String[] {
							"Id",
							getTextRespectUISpr("lp.person",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.von",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.bis",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.dauer",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.kosten",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.partner",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.kommentar",
									theClientDto.getMandant(),
									theClientDto.getLocUi()) },
					new int[] {
							-1, // diese Spalte wird ausgeblendet
							QueryParameters.FLR_BREITE_XM,
							QueryParameters.FLR_BREITE_XM,
							QueryParameters.FLR_BREITE_XM,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_L,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST, },

					new String[] {
							"i_id",
							ZeiterfassungFac.FLR_TELEFONZEITEN_FLRPERSONAL
									+ "."
									+ ZeiterfassungFac.FLR_TELEFONZEITEN_FLRPARTNER
									+ "."
									+ PartnerFac.FLR_PARTNER_C_NAME1NACHNAMEFIRMAZEILE1,
							ZeiterfassungFac.FLR_TELEFONZEITEN_T_VON,
							ZeiterfassungFac.FLR_TELEFONZEITEN_T_BIS,
							Facade.NICHT_SORTIERBAR,
							Facade.NICHT_SORTIERBAR,
							ZeiterfassungFac.FLR_TELEFONZEITEN_FLRPARTNER
									+ "."
									+ PartnerFac.FLR_PARTNER_C_NAME1NACHNAMEFIRMAZEILE1,
							ZeiterfassungFac.FLR_TELEFONZEITEN_X_KOMMENTAREXT }));

		}

		return super.getTableInfo();
	}
}
