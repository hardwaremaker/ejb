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
package com.lp.server.eingangsrechnung.fastlanereader;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnungKontierung;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.finanz.service.FinanzFac;
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
 * Diese Klasse kuemmert sich um den FLR ER-Kontierung
 * </p>
 *
 * <p>
 * Copyright Logistik Pur GmbH (c) 2005
 * </p>
 *
 * <p>
 * Erstellungsdatum 14.03.05
 * </p>
 *
 * <p>
 *
 * @author Martin Bluehweis
 *         </p>
 *
 * @version not attributable
 */
public class EingangsrechnungKontierungHandler extends UseCaseHandler {

	private static final long serialVersionUID = 1L;

	/**
	 * the size of the data page returned in QueryResult.
	 */

	/**
	 * The information needed for the Konten table.
	 */

	/**
	 * gets the data page for the specified row using the current query. The row
	 * at rowIndex will be located in the middle of the page.
	 *
	 * @see UseCaseHandler#getPageAt(java.lang.Integer)
	 * @param rowIndex
	 *            Integer
	 * @throws EJBExceptionLP
	 * @return QueryResult
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

			String[] tooltipData = new String[resultList.size()];

			while (resultListIterator.hasNext()) {
				FLREingangsrechnungKontierung erKontierung = (FLREingangsrechnungKontierung) resultListIterator
						.next();
				rows[row][col++] = erKontierung.getI_id();
				
				if(erKontierung.getFlrartikel()!=null){
					ArtikelDto artikelDto=getArtikelFac().artikelFindByPrimaryKey(erKontierung.getFlrartikel().getI_id(), theClientDto);
					rows[row][col++] = artikelDto.formatBezeichnung();
				} else {
					rows[row][col++] =null;
				}
				
				rows[row][col++] = erKontierung.getN_betrag();
				rows[row][col++] = erKontierung.getN_betrag_ust();
				rows[row][col++] = erKontierung.getFlrkostenstelle().getC_nr();
				rows[row][col++] = erKontierung.getFlrkonto().getC_nr();

				if (erKontierung.getC_kommentar() != null && !erKontierung.getC_kommentar().isEmpty()) {
					rows[row][col++] = true ;
					tooltipData[row] = erKontierung.getC_kommentar().isEmpty() ? null
							: Helper.removeStyles(erKontierung.getC_kommentar());
				} else {
					rows[row][col++] = false;
				}

				row++;
				col = 0;
			}

//			result = new QueryResult(rows, this.getRowCount(), startIndex,
//					endIndex, 0);

			result = new QueryResult(rows, this.getRowCount(), startIndex,
					endIndex, 0, tooltipData);

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
					where
							.append(" erKontierung."
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
						orderBy.append("erKontierung." + kriterien[i].kritName);
						orderBy.append(" ");
						orderBy.append(kriterien[i].value);
					}
				}
			} else {
				// no sort criteria found, add default sort
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append("erKontierung.i_id ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("erKontierung.i_id") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" erKontierung.i_id ");
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
		return "from FLREingangsrechnungKontierung erKontierung ";
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

		if (selectedId != null && ((Integer) selectedId).intValue() >= 0) {
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();
				String queryString = "select erKontierung.i_id from FLREingangsrechnungKontierung erKontierung "
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

	/**
	 * gets information about the Kontentable.
	 *
	 * @return TableInfo
	 */
	public TableInfo getTableInfo() {
		if (super.getTableInfo() == null) {
			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();

			setTableInfo(new TableInfo(

				new Class[] {
					Integer.class,
					String.class,
					BigDecimal.class,
					BigDecimal.class,
					String.class,
					String.class,
					Boolean.class
				},

				new String[] {
					"i_id",
					getTextRespectUISpr("lp.artikel", mandantCNr, locUI),
					getTextRespectUISpr("lp.betrag", mandantCNr, locUI),
					getTextRespectUISpr("lp.ustbetrag", mandantCNr, locUI),
					getTextRespectUISpr("lp.kostenstelle", mandantCNr, locUI),
					getTextRespectUISpr("lp.sachkonto", mandantCNr, locUI),
					getTextRespectUISpr("er.kommentar", mandantCNr, locUI)
				},

				new int[] {
						-1, // diese Spalte wird ausgeblendet
						QueryParameters.FLR_BREITE_SHARE_WITH_REST,
						QueryParameters.FLR_BREITE_SHARE_WITH_REST,
						QueryParameters.FLR_BREITE_SHARE_WITH_REST,
						QueryParameters.FLR_BREITE_SHARE_WITH_REST,
						QueryParameters.FLR_BREITE_SHARE_WITH_REST,
						QueryParameters.FLR_BREITE_SHARE_WITH_REST
				},

				new String[] {
					EingangsrechnungFac.FLR_KONTIERUNG_I_ID,
					Facade.NICHT_SORTIERBAR,
					EingangsrechnungFac.FLR_KONTIERUNG_N_BETRAG,
					EingangsrechnungFac.FLR_KONTIERUNG_N_BETRAG_UST,
					EingangsrechnungFac.FLR_KONTIERUNG_FLRKOSTENSTELLE + ".c_nr",
					/**
					 * @todo nicht hard codiert PJ 4208
					 */
					EingangsrechnungFac.FLR_KONTIERUNG_FLRKONTO + "." + FinanzFac.FLR_KONTO_C_NR,
					Facade.NICHT_SORTIERBAR
				},
				new String[]{null,null,null,null,null,null,
						getTextRespectUISpr("er.kommentar.tooltip", mandantCNr, locUI) 
				}
			));
		}
		return super.getTableInfo();
	}
}
