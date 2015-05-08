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
package com.lp.server.artikel.fastlanereader;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.artikel.fastlanereader.generated.FLRVkpfStaffelmenge;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VkPreisfindungFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
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
 * <I>FLR fuer WW_VKPFSTAFFELMENGE.</I>
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellungsdatum <I>12.06.2006</I>
 * </p>
 * 
 * <p>
 * </p>
 * 
 * @author Uli Walch
 * @version 1.0
 */
public class VkpfStaffelmengeHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String FLR_VKPFSTAFFELMENGE = "flrvkpfstaffelmenge.";
	public static final String FLR_VKPFSTAFFELMENGE_FROM_CLAUSE = " from FLRVkpfStaffelmenge flrvkpfstaffelmenge LEFT OUTER JOIN flrvkpfstaffelmenge.flrvkpfartikelpreisliste AS pl ";
	int iPreisbasis = 0;

	/**
	 * gets the page of data for the specified row using the current
	 * queryParameters.
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
			int pageSize = VkpfStaffelmengeHandler.PAGE_SIZE;
			int startIndex = Math.max(rowIndex.intValue() - (pageSize / 2), 0);
			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			String queryString = this.getFromClause() + this.buildWhereClause()
					+ this.buildOrderByClause();
			// myLogger.info("HQL: " + queryString);
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;
			int col = 0;
			while (resultListIterator.hasNext()) {

				Object[] o = (Object[]) resultListIterator.next();
				FLRVkpfStaffelmenge staffelmenge = (FLRVkpfStaffelmenge) o[0];

				rows[row][col++] = staffelmenge.getI_id();
				rows[row][col++] = staffelmenge.getN_menge();
				rows[row][col++] = staffelmenge.getN_fixpreis() == null ? null
						: staffelmenge.getN_fixpreis();
				rows[row][col++] = staffelmenge.getF_rabattsatz();

				// der Preis muss an dieser Stelle berechnet werden
				BigDecimal nBerechneterPreis = new BigDecimal(0);

				if (staffelmenge.getN_fixpreis() != null) {
					nBerechneterPreis = staffelmenge.getN_fixpreis();
				} else {
					// WH 21.06.06 Es gilt die VK-Basis, die zu Beginn der
					// Mengenstaffel gueltig ist

					if (iPreisbasis != 2) {

						if (Helper.short2boolean(staffelmenge
								.getB_allepreislisten()) == false) {
							String waehrung = theClientDto
									.getSMandantenwaehrung();
							if (staffelmenge.getFlrvkpfartikelpreisliste() != null) {
								waehrung = staffelmenge
										.getFlrvkpfartikelpreisliste()
										.getWaehrung_c_nr();
							}

							// SP3398 -> Preis muss zum Gueltigkeitsdatum =
							// Heute sein

							BigDecimal nPreisbasis = getVkPreisfindungFac()
									.ermittlePreisbasis(
											staffelmenge.getArtikel_i_id(),
											new java.sql.Date(System
													.currentTimeMillis()),
											staffelmenge
													.getVkpfartikelpreisliste_i_id(),
											waehrung, theClientDto);

							VerkaufspreisDto vkpfDto = getVkPreisfindungFac()
									.berechneVerkaufspreis(nPreisbasis,
											staffelmenge.getF_rabattsatz());
							if (vkpfDto != null) {
								nBerechneterPreis = vkpfDto.nettopreis;
							}
						}
					}
				}

				rows[row][col++] = nBerechneterPreis;
				if (staffelmenge.getFlrvkpfartikelpreisliste() != null) {
					rows[row][col++] = staffelmenge
							.getFlrvkpfartikelpreisliste().getWaehrung_c_nr();
					rows[row][col++] = staffelmenge
							.getFlrvkpfartikelpreisliste().getC_nr();
				} else {

					if (Helper.short2boolean(staffelmenge
							.getB_allepreislisten()) == false) {
						rows[row][col++] = theClientDto.getSMandantenwaehrung();
						rows[row][col++] = null;
					} else {
						rows[row][col++] = null;
						rows[row][col++] = getTextRespectUISpr(
								"artikel.allepreislisten",
								theClientDto.getMandant(),
								theClientDto.getLocUi());
					}

				}
				rows[row][col++] = staffelmenge.getT_preisgueltigab();
				rows[row++][col++] = staffelmenge.getT_preisgueltigbis() == null ? null
						: staffelmenge.getT_preisgueltigbis();

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
					where.append(" " + FLR_VKPFSTAFFELMENGE
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
					if (!kriterien[i].kritName
							.endsWith(Facade.NICHT_SORTIERBAR)) {
						if (kriterien[i].isKrit) {
							if (sortAdded) {
								orderBy.append(", ");
							}
							sortAdded = true;
							orderBy.append(FLR_VKPFSTAFFELMENGE
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
				orderBy.append(FLR_VKPFSTAFFELMENGE
						+ VkPreisfindungFac.FLR_VKPFSTAFFELMENGE_N_MENGE
						+ ","
						+ FLR_VKPFSTAFFELMENGE
						+ VkPreisfindungFac.FLR_VKPFSTAFFELMENGE_T_PREISGUELTIGAB);

				sortAdded = true;
			}

			if (orderBy.indexOf(FLR_VKPFSTAFFELMENGE
					+ VkPreisfindungFac.FLR_VKPFSTAFFELMENGE_N_MENGE) < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(FLR_VKPFSTAFFELMENGE
						+ VkPreisfindungFac.FLR_VKPFSTAFFELMENGE_N_MENGE);

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
		return FLR_VKPFSTAFFELMENGE_FROM_CLAUSE;
	}

	public TableInfo getTableInfo() {
		if (super.getTableInfo() == null) {

			try {
				ParametermandantDto param = getParameterFac()
						.getMandantparameter(theClientDto.getMandant(),
								ParameterFac.KATEGORIE_KUNDEN,
								ParameterFac.PARAMETER_PREISBASIS_VERKAUF);

				iPreisbasis = (Integer) param.getCWertAsObject();

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();
			setTableInfo(new TableInfo(
					new Class[] { Integer.class, BigDecimal.class,
							BigDecimal.class, Double.class, BigDecimal.class,
							String.class, String.class, java.util.Date.class,
							java.util.Date.class },
					new String[] {
							"i_id",
							getTextRespectUISpr("lp.menge", mandantCNr, locUI),
							getTextRespectUISpr("lp.fixpreis", mandantCNr,
									locUI),
							getTextRespectUISpr("lp.rabatt", mandantCNr, locUI),
							getTextRespectUISpr(
									"artikel.vkstaffel.nettopreisheute",
									mandantCNr, locUI),
							getTextRespectUISpr("lp.waehrung", mandantCNr,
									locUI),
							getTextRespectUISpr("lp.preisliste", mandantCNr,
									locUI),
							getTextRespectUISpr("lp.gueltig_ab", mandantCNr,
									locUI),
							getTextRespectUISpr("lp.gueltig_bis", mandantCNr,
									locUI) },
					new int[] {
							QueryParameters.FLR_BREITE_SHARE_WITH_REST, // diese
							// Spalte
							// wird
							// ausgeblendet
							QueryParameters.FLR_BREITE_SHARE_WITH_REST, // Format
							// 1234.123
							QueryParameters.FLR_BREITE_XM,
							QueryParameters.FLR_BREITE_XM,
							QueryParameters.FLR_BREITE_XM,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_XM,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST, // Breite
							// variabel
							QueryParameters.FLR_BREITE_SHARE_WITH_REST },
					new String[] {
							"i_id",
							VkPreisfindungFac.FLR_VKPFSTAFFELMENGE_N_MENGE,
							VkPreisfindungFac.FLR_VKPFSTAFFELMENGE_N_FIXPREIS,
							VkPreisfindungFac.FLR_VKPFSTAFFELMENGE_F_RABATTSATZ,
							Facade.NICHT_SORTIERBAR,
							VkPreisfindungFac.FLR_VKPFSTAFFELMENGE_FLRVKPFARTIKELPREISLISTE
									+ ".c_nr",
							VkPreisfindungFac.FLR_VKPFSTAFFELMENGE_FLRVKPFARTIKELPREISLISTE
									+ ".waehrung_c_nr",
							VkPreisfindungFac.FLR_VKPFSTAFFELMENGE_T_PREISGUELTIGAB,
							VkPreisfindungFac.FLR_VKPFSTAFFELMENGE_T_PREISGUELTIGBIS }));
		}
		return super.getTableInfo();
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
				String queryString = "select " + FLR_VKPFSTAFFELMENGE + "i_id"
						+ FLR_VKPFSTAFFELMENGE_FROM_CLAUSE
						+ this.buildWhereClause() + this.buildOrderByClause();
				Query query = session.createQuery(queryString);
				ScrollableResults scrollableResult = query.scroll();
				boolean idFound = false;
				if (scrollableResult != null) {
					scrollableResult.beforeFirst();
					while (scrollableResult.next()) {
						Integer id = (Integer) scrollableResult.getInteger(0); // TYPE
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
