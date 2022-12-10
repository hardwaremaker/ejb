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

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.finanz.fastlanereader.generated.FLRKontolaenderart;
import com.lp.server.finanz.fastlanereader.generated.FLRReversechargeartspr;
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
import com.lp.util.Helper;

/**
 * <p>
 * Hier wird die FLR Funktionalitaet fuer die Auftragfunktionen implementiert.
 * Auftragfunktionen werden vom Anwender definiert und koennen mehrsprachig
 * sein.
 * </p>
 * <p>
 * Copright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum 07.02.2005
 * </p>
 * <p>
 * </p>
 * 
 * @author Uli Walch
 * @version 1.0
 */
public class KontolaenderartHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String FLR_KONTOLAENDERART = "flrkontolaenderart.";
	private static final String FLR_KONTOLAENDERART_FROM_CLAUSE = " from FLRKontolaenderart as flrkontolaenderart ";

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = KontolaenderartHandler.PAGE_SIZE;
			int startIndex = Math.max(rowIndex.intValue() - (pageSize / 2), 0);
			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			String queryString = this.getFromClause() + this.buildWhereClause()
					+ this.buildOrderByClause();
			Query query = session.createQuery(queryString);
			session = setFilter(session);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			String sLocUI = Helper.locale2String(theClientDto.getLocUi());

			int row = 0;
			int col = 0;
			while (resultListIterator.hasNext()) {
				FLRKontolaenderart kontolaenderart = (FLRKontolaenderart) resultListIterator.next();
/*				
				rows[row][col++] = kontolaenderart.getId_comp();
				rows[row][col++] = getFinanzFac().finanzamtFindByPrimaryKey(kontolaenderart.getId_comp().getFinanzamt_i_id(), 
						kontolaenderart.getId_comp().getMandant_c_nr(), theClientDto).getPartnerDto().formatName();
				rows[row][col++] = getFinanzServiceFac()
						.uebersetzeLaenderartOptimal(
								kontolaenderart.getId_comp().getLaenderart_c_nr(),
								theClientDto.getLocUi(),
								theClientDto.getLocUi());
*/
				rows[row][col++] = kontolaenderart.getI_id();
				rows[row][col++] = getFinanzFac().finanzamtFindByPrimaryKey(kontolaenderart.getFinanzamt_i_id(), 
						kontolaenderart.getFlrmandant().getC_nr(), theClientDto).getPartnerDto().formatName();
				rows[row][col++] = getFinanzServiceFac()
						.uebersetzeLaenderartOptimal(
								kontolaenderart.getFlrlaenderart().getC_nr(),
								theClientDto.getLocUi(),
								theClientDto.getLocUi());
				Iterator<?> sprsetIterator = kontolaenderart.getFlrreversechargeart()
						.getReversechargeartsprset().iterator();
				rows[row][col++] = findSpr(sLocUI, sprsetIterator) ;  
				rows[row][col++] = kontolaenderart.getFlrkonto_uebersetzt().getC_nr();
				rows[row][col++] = kontolaenderart.getFlrkonto_uebersetzt().getC_bez();
				rows[row][col++] = Helper.asDate(kontolaenderart.getGueltigAb());
				
				row++;
				col = 0;
			}
			result = new QueryResult(rows, this.getRowCount(), startIndex,
					endIndex, 0);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			sessionClose(session);
		}
		return result;
	}

	private String findSpr(String sLocaleI, Iterator<?> iterUebersetzungenI) {
		String sUebersetzung = null;
		while (iterUebersetzungenI.hasNext()) {
			FLRReversechargeartspr reversechargeartspr = (FLRReversechargeartspr) iterUebersetzungenI
					.next();
			if (reversechargeartspr.getLocale().getC_nr().compareTo(sLocaleI) == 0) {
				sUebersetzung = reversechargeartspr.getC_bez();
				break;
			}
		}
		return sUebersetzung;
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
					where.append(" " + FLR_KONTOLAENDERART
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

	private String transformClientKrit(String clientKritname) {
		if(FinanzFac.FLR_KONTO_C_NR.equals(clientKritname)) {
			return "flrkonto_uebersetzt.c_nr" ;
		}
		if(FinanzFac.FLR_KONTO_C_BEZ.equals(clientKritname)) {
			return "flrkonto_uebersetzt.c_bez" ;
		}
		if(FinanzFac.FLR_KONTOLAENDERART_REVERSECHARGEART_BEZ.equals(clientKritname)) {
			return "flrreversechargeart.c_nr" ;
		}
		return clientKritname ;
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
						String kritName = transformClientKrit(kriterien[i].kritName); 
//						orderBy.append(FLR_KONTOLAENDERART
//								+ kriterien[i].kritName);
						orderBy.append(kritName);
						orderBy.append(" ");
						orderBy.append(kriterien[i].value);
						sortAdded = true;
					}
				}
			} else {
				// no sort criteria found, add default sort
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(FLR_KONTOLAENDERART).append(
						FinanzFac.FLR_KONTOLAENDERART_FLRKONTOUEBERSETZT + "."
								+ FinanzFac.FLR_KONTO_C_NR).append(" ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf(FLR_KONTOLAENDERART
					+ FinanzFac.FLR_KONTOLAENDERART_FLRKONTOUEBERSETZT + "."
					+ FinanzFac.FLR_KONTO_C_NR) < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" ").append(FLR_KONTOLAENDERART).append(
						FinanzFac.FLR_KONTOLAENDERART_FLRKONTOUEBERSETZT + "."
								+ FinanzFac.FLR_KONTO_C_NR).append(" ");
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
		return FLR_KONTOLAENDERART_FROM_CLAUSE;
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
				String queryString = "select " + FLR_KONTOLAENDERART 
						+ FinanzFac.FLR_KONTOLAENDERART_I_ID
						+ " from FLRKontolaenderart flrkontolaenderart" 
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
				closeSession(session);
			}
		}
/*		
		if (selectedId != null
				&& ((FLRKontolaenderartPK) selectedId).getKonto_i_id()
						.intValue() >= 0) {
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();
				String queryString = "select " + FLR_KONTOLAENDERART
						+ FinanzFac.FLR_KONTOLAENDERART_FLRKONTOUEBERSETZT
						+ "." + FinanzFac.FLR_KONTO_C_NR
						+ FLR_KONTOLAENDERART_FROM_CLAUSE
						+ this.buildWhereClause() + this.buildOrderByClause();
				Query query = session.createQuery(queryString);
				ScrollableResults scrollableResult = query.scroll();
				if (scrollableResult != null) {
					scrollableResult.beforeFirst();
					while (scrollableResult.next()) {
						String id = (String) scrollableResult.getString(0);
						if (selectedId.equals(id)) {
							rowNumber = scrollableResult.getRowNumber();
							break;
						}
					}
				}
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
			} finally {
				sessionClose(session) ;
			}
		}
*/
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
					new Class[] { Integer.class, String.class, String.class, String.class, String.class, String.class, Date.class },
					new String[] {"i_id",
							getTextRespectUISpr("fb.finanzamt", mandantCNr, locUI),
							getTextRespectUISpr("fb.laenderart", mandantCNr, locUI),
							getTextRespectUISpr("fb.reversechargeart", mandantCNr, locUI),
							getTextRespectUISpr("lp.konto", mandantCNr, locUI),
							getTextRespectUISpr("lp.bezeichnung", mandantCNr, locUI),
							getTextRespectUISpr("lp.gueltig_ab", mandantCNr, locUI)},
					new int[] {-1,
							QueryParameters.FLR_BREITE_L,
							QueryParameters.FLR_BREITE_L,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_L,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST},
					new String[] {
							FinanzFac.FLR_KONTOLAENDERART_I_ID,
							FinanzFac.FLR_KONTOLAENDERART_FINANZAMT_I_ID,
							FinanzFac.FLR_KONTOLAENDERART_LAENDERART_C_NR,
							FinanzFac.FLR_KONTOLAENDERART_REVERSECHARGEART_BEZ,
							FinanzFac.FLR_KONTO_C_NR,
							FinanzFac.FLR_KONTO_C_BEZ,
							FinanzFac.FLR_KONTOLAENDERART_GUELTIG_AB}));
		}
		return super.getTableInfo();
	}
}
