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
package com.lp.server.inserat.fastlanereader;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.inserat.fastlanereader.generated.FLRInserater;
import com.lp.server.inserat.fastlanereader.generated.FLRInseratrechnung;
import com.lp.server.inserat.service.InseratFac;
import com.lp.server.partner.service.KundeFac;
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
 * Hier wird die FLR Funktionalitaet fuer den Auftrag implementiert. Pro UseCase
 * gibt es einen Handler.
 * </p>
 * <p>
 * Copright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum 2004-08-14
 * </p>
 * <p>
 * </p>
 * 
 * @author martin werner, uli walch
 * @version 1.0
 */

public class InseraterHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String FLR_INSERATER = "flrinserater.";
	public static final String FLR_INSERATER_FROM_CLAUSE = " from FLRInserater flrinserater ";

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
				FLRInserater inserat = (FLRInserater) resultListIterator.next();
				rows[row][col++] = inserat.getI_id();
				rows[row][col++] = inserat.getFlreingangsrechnung().getC_nr();
				rows[row][col++] = inserat.getFlrinserat().getC_nr();

				FLRInseratrechnung inseratrechnung = null;
				Set s = inserat.getFlrinserat().getRechnungset();
				if (s.size() > 0) {
					inseratrechnung = (FLRInseratrechnung) s.iterator().next();
				}
				if (inseratrechnung != null) {
					rows[row][col++] = inseratrechnung.getFlrkunde()
							.getFlrpartner().getC_kbez();
				} else {
					rows[row][col++] = null;
				}
				rows[row][col++] = inserat.getN_betrag();
				rows[row][col++] = getInseratFac()
						.berechneWerbeabgabeLFEinesInserates(
								inserat.getFlrinserat().getI_id(),inserat.getN_betrag(), theClientDto);

				if (inserat.getFlrinserat().getArtikelset().size() > 0) {

					rows[row][col++] = "A";
				} else {
					rows[row][col++] = null;
				}
				rows[row][col++] = inserat.getC_text();

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
			String queryString = "select count(*) from FLRInserater  as flrinserater "
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

					if (filterKriterien[i].isBIgnoreCase()) {
						where.append(" lower(" + FLR_INSERATER
								+ filterKriterien[i].kritName + ")");
					} else {
						where.append(" " + FLR_INSERATER
								+ filterKriterien[i].kritName);
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
							orderBy.append(FLR_INSERATER
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
				orderBy.append(FLR_INSERATER)
						.append("flreingangsrechnung.c_nr ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf(FLR_INSERATER + "i_id") < 0) {
				// Martin Werner original: "unique sort required because
				// otherwise rowNumber of selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt()."
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" ").append(FLR_INSERATER).append("i_id")
						.append(" ASC ");
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
		return "SELECT flrinserater from FLRInserater  as flrinserater ";

	}

	public QueryResult sort(SortierKriterium[] sortierKriterien,
			Object selectedId) throws EJBExceptionLP {
		QueryResult result = null;

		try {
			int rowNumber = 0;

			getQuery().setSortKrit(sortierKriterien);

			if (selectedId != null && ((Integer) selectedId).intValue() >= 0) {
				SessionFactory factory = FLRSessionFactory.getFactory();
				Session session = null;

				try {
					session = factory.openSession();
					String queryString = "select " + FLR_INSERATER + "i_id"
							+ FLR_INSERATER_FROM_CLAUSE
							+ this.buildWhereClause()
							+ this.buildOrderByClause();
					Query query = session.createQuery(queryString);
					ScrollableResults scrollableResult = query.scroll();
					if (scrollableResult != null) {
						scrollableResult.beforeFirst();
						while (scrollableResult.next()) {
							Integer id = (Integer) scrollableResult
									.getInteger(0);
							if (selectedId.equals(id)) {
								rowNumber = scrollableResult.getRowNumber();
								break;
							}
						}
					}
				} finally {
					try {
						session.close();
					} catch (HibernateException he) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_HIBERNATE, he);
					}
				}
			}

			if (rowNumber < 0 || rowNumber >= this.getRowCount()) {
				rowNumber = 0;
			}

			result = this.getPageAt(new Integer(rowNumber));
			result.setIndexOfSelectedRow(rowNumber);
		} catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		return result;
	}

	public TableInfo getTableInfo() {

		if (super.getTableInfo() == null) {
			int iNachkommastellenPreis = 0;
			try {
				iNachkommastellenPreis = getMandantFac()
						.getNachkommastellenPreisEK(theClientDto.getMandant());

			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
				return null;
			}

			setTableInfo(new TableInfo(
					new Class[] {
							Integer.class,
							String.class,
							String.class,
							String.class,
							super.getUIClassBigDecimalNachkommastellen(iNachkommastellenPreis),
							super.getUIClassBigDecimalNachkommastellen(iNachkommastellenPreis),
							String.class, String.class }, new String[] {
							"i_id",

							getTextRespectUISpr("er.eingangsrechnung",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("iv.inserat",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.firma",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.wert",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.werbeabgabe",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							"",
							getTextRespectUISpr("lp.kommentar",
									theClientDto.getMandant(),
									theClientDto.getLocUi())

					}, new int[] {
							QueryParameters.FLR_BREITE_SHARE_WITH_REST, // diese
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_XXS,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST },
					new String[] {
							"i_id",
							InseratFac.FLR_INSERATER_FLREINGANGSRECHNUNG
									+ ".c_nr",
							InseratFac.FLR_INSERATER_FLRINSERAT + ".c_nr",
							Facade.NICHT_SORTIERBAR,
							InseratFac.FLR_INSERATER_N_BETRAG,
							Facade.NICHT_SORTIERBAR, Facade.NICHT_SORTIERBAR,
							InseratFac.FLR_INSERATER_C_TEXT }));
		}

		return super.getTableInfo();
	}

}
