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
package com.lp.server.partner.fastlanereader;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.Icon;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.partner.fastlanereader.generated.FLRAnsprechpartner;
import com.lp.server.partner.fastlanereader.generated.FLRAnsprechpartnerfunktionspr;
import com.lp.server.partner.service.AnsprechpartnerFac;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.UseCaseHandler;
import com.lp.server.util.fastlanereader.service.query.FilterBlock;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * Diese Klasse kuemmert sich Ansprechpartner-FLR.
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellungsdatum 28.10.04
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version $Revision: 1.12 $ Date $Date: 2012/08/29 14:30:27 $
 */

public class AnsprechpartnerHandler extends UseCaseHandler {

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {

		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = AnsprechpartnerHandler.PAGE_SIZE;
			int startIndex = Math.max(rowIndex.intValue() - (pageSize / 2), 0);
			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			String queryString = this.getFromClause() + this.buildWhereClause() + this.buildOrderByClause();
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;
			int col = 0;
			while (resultListIterator.hasNext()) {
				FLRAnsprechpartner ansprechpartner = (FLRAnsprechpartner) resultListIterator.next();
				rows[row][col++] = ansprechpartner.getI_id();

				String fkt = ansprechpartner.getFlransprechpartnerfunktion().getC_nr();

				Set s = ansprechpartner.getFlransprechpartnerfunktion()
						.getAnsprechpartnerfunktion_ansprechpartnerfunktionspr_set();
				Iterator it = s.iterator();
				while (it.hasNext()) {
					FLRAnsprechpartnerfunktionspr spr = (FLRAnsprechpartnerfunktionspr) it.next();
					if (spr.getLocale().getC_nr().equals(theClientDto.getLocUiAsString()) && spr.getC_bez() != null) {
						fkt = spr.getC_bez();
					}
				}
				rows[row][col++] = fkt;
				rows[row][col++] = ansprechpartner.getFlrpartneransprechpartner().getAnrede_c_nr();
				rows[row][col++] = ansprechpartner.getFlrpartneransprechpartner().getC_titel();
				rows[row][col++] = ansprechpartner.getFlrpartneransprechpartner().getC_name2vornamefirmazeile2();
				rows[row][col++] = ansprechpartner.getFlrpartneransprechpartner().getC_name1nachnamefirmazeile1();
				rows[row][col++] = ansprechpartner.getFlrpartneransprechpartner().getC_ntitel();

				rows[row][col++] = ansprechpartner.getC_abteilung();
				if (ansprechpartner.getC_email() != null) {
					rows[row][col++] = LocaleFac.STATUS_EMAIL;
				} else {
					rows[row][col++] = null;
				}

				rows[row][col++] = ansprechpartner.getI_sort();

				if (Helper.short2boolean(ansprechpartner.getB_versteckt())) {
					rows[row][col++] = Color.LIGHT_GRAY;
				}

				row++;

				col = 0;
			}
			result = new QueryResult(rows, this.getRowCount(), startIndex, endIndex, 0);
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
			String queryString = "select count(*) " + this.getFromClause() + this.buildWhereClause();
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
	 * builds the where clause of the HQL (Hibernate Query Language) statement using
	 * the current query.
	 * 
	 * @return the HQL where clause.
	 */
	private String buildWhereClause() {
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

					if (filterKriterien[i].kritName.equals("flrpartneransprechpartner."+PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1)) {
						where.append(" (lower(ansprechpartner." + filterKriterien[i].kritName + ")");
						where.append(" " + filterKriterien[i].operator);
						where.append(" " + filterKriterien[i].value.toLowerCase());
						where.append(" OR lower(ansprechpartner.flrpartneransprechpartner.c_name2vornamefirmazeile2)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" " + filterKriterien[i].value.toLowerCase());
						where.append(" OR lower(ansprechpartner.flrpartneransprechpartner.c_kbez)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" " + filterKriterien[i].value.toLowerCase() + ")");
					} else {

						// ignorecase: 1 hier auf upper
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" lower(ansprechpartner." + filterKriterien[i].kritName + ")");
						} else {
							where.append(" ansprechpartner." + filterKriterien[i].kritName);
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
	private String buildOrderByClause() {
		StringBuffer orderBy = new StringBuffer("");
		if (this.getQuery() != null) {
			SortierKriterium[] kriterien = this.getQuery().getSortKrit();
			boolean sortAdded = false;
			if (kriterien != null && kriterien.length > 0) {
				for (int i = 0; i < kriterien.length; i++) {
					if (!kriterien[i].kritName.endsWith(Facade.NICHT_SORTIERBAR)) {
						if (kriterien[i].isKrit) {
							if (sortAdded) {
								orderBy.append(", ");
							}
							sortAdded = true;
							orderBy.append("ansprechpartner." + kriterien[i].kritName);
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
				orderBy.append(" ansprechpartner.i_sort ASC ");
				// orderBy.append("ansprechpartner." +
				// AnsprechpartnerFac.FLR_ANSPRECHPARTNER_PARTNERANSPRECHPARTNER
				// +
				// "." +
				// PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1 + " DESC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("ansprechpartner.i_id") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" ansprechpartner.i_id ");
				sortAdded = true;
			}
			if (orderBy.indexOf("ansprechpartner.i_sort") < 0) {
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" ansprechpartner.i_sort ");
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
		return "from FLRAnsprechpartner ansprechpartner ";
	}

	public QueryResult sort(SortierKriterium[] sortierKriterien, Object selectedId) throws EJBExceptionLP {

		this.getQuery().setSortKrit(sortierKriterien);

		QueryResult result = null;
		int rowNumber = 0;

		if (selectedId != null && ((Integer) selectedId).intValue() >= 0) {
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();
				String queryString = "select ansprechpartner.i_id from FLRAnsprechpartner ansprechpartner "
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
					new Class[] { Integer.class, String.class, String.class, String.class, String.class, String.class,
							String.class, String.class, Icon.class, Integer.class, Color.class },
					new String[] { "i_id",
							getTextRespectUISpr("lp.funktion", theClientDto.getMandant(), theClientDto.getLocUi()),
							getTextRespectUISpr("lp.anrede", theClientDto.getMandant(), theClientDto.getLocUi()),
							getTextRespectUISpr("lp.titel", theClientDto.getMandant(), theClientDto.getLocUi()),
							getTextRespectUISpr("lp.vorname", theClientDto.getMandant(), theClientDto.getLocUi()),
							getTextRespectUISpr("lp.nachname", theClientDto.getMandant(), theClientDto.getLocUi()),
							getTextRespectUISpr("lp.nachgestellt", theClientDto.getMandant(), theClientDto.getLocUi()),
							getTextRespectUISpr("ansp.abteilung", theClientDto.getMandant(), theClientDto.getLocUi()),

							getTextRespectUISpr("lp.email", theClientDto.getMandant(), theClientDto.getLocUi()),
							getTextRespectUISpr("part.sortierung", theClientDto.getMandant(), theClientDto.getLocUi()),
							"" },
					new int[] { QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M, QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_M, QueryParameters.FLR_BREITE_M, QueryParameters.FLR_BREITE_XS,
							QueryParameters.FLR_BREITE_S, 0 },
					new String[] { "i_id", "flransprechpartnerfunktion.c_nr",
							AnsprechpartnerFac.FLR_ANSPRECHPARTNER_PARTNERANSPRECHPARTNER + "."
									+ PartnerFac.FLR_PARTNER_ANREDE,
							AnsprechpartnerFac.FLR_ANSPRECHPARTNER_PARTNERANSPRECHPARTNER + "."
									+ PartnerFac.FLR_PARTNER_TITEL,
							AnsprechpartnerFac.FLR_ANSPRECHPARTNER_PARTNERANSPRECHPARTNER + "."
									+ PartnerFac.FLR_PARTNER_NAME2VORNAMEFIRMAZEILE2,
							AnsprechpartnerFac.FLR_ANSPRECHPARTNER_PARTNERANSPRECHPARTNER + "."
									+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
							AnsprechpartnerFac.FLR_ANSPRECHPARTNER_PARTNERANSPRECHPARTNER + "."
									+ PartnerFac.FLR_PARTNER_NTITEL,
							AnsprechpartnerFac.FLR_ANSPRECHPARTNER_C_ABTEILUNG,
							AnsprechpartnerFac.FLR_ANSPRECHPARTNER_C_EMAIL,
							AnsprechpartnerFac.FLR_ANSPRECHPARTNER_I_SORT, "" },
					new String[] { null, null, null, null, null, null, null, null, null, getTextRespectUISpr(
							"part.sortierung.tooltip", theClientDto.getMandant(), theClientDto.getLocUi()), null }));
		}
		return super.getTableInfo();
	}
}
