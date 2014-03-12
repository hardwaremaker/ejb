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
package com.lp.server.finanz.fastlanereader;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.Icon;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.finanz.ejb.BuchungdetailText;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzBuchungDetail;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto;
import com.lp.server.finanz.service.BuchenFac;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
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
 * Hier wird die FLR Funktionalitaet fuer die Buchungsdetails implementiert. Pro
 * UseCase gibt es einen Handler.
 * </p>
 * <p>
 * Copright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum 28.09.2004
 * </p>
 * <p>
 * </p>
 * 
 * @author MB
 * @version 1.0
 */

public class FinanzBuchungenDetailliertHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String trennzeichen = "";
	Integer stellenBelegnummer = 7;
	Integer toleranzBetragsuche = 0;

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;

		BuchungdetailText buchungdetailText = 
			new BuchungdetailText(getBenutzerServicesFac(), theClientDto) ;

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
				Object o[] = (Object[]) resultListIterator.next();
				FLRFinanzBuchungDetail buchungDetail = (FLRFinanzBuchungDetail) o[0];

				rows[row][col++] = buchungDetail.getI_id();
				rows[row][col++] = buchungDetail.getFlrbuchung().getT_anlegen();
				rows[row][col++] = buchungDetail.getFlrbuchung()
						.getD_buchungsdatum();

				rows[row][col++] = buchungdetailText
						.getTextFuerBuchungsart(buchungDetail.getFlrbuchung());
				rows[row][col++] = buchungDetail.getFlrbuchung()
						.getC_belegnummer();
				rows[row][col++] = buchungDetail.getFlrkonto().getC_nr() + ", "
						+ buchungDetail.getFlrkonto().getC_bez();

				rows[row][col++] = buchungDetail.getFlrbuchung().getC_text();

				if (buchungDetail.getBuchungdetailart_c_nr().equals(
						BuchenFac.HabenBuchung)) {
					rows[row][col++] = null;
					rows[row][col++] = buchungDetail.getN_betrag();
				} else {
					rows[row][col++] = buchungDetail.getN_betrag();
					rows[row][col++] = null;
				}

				FLRFinanzKonto gegenkonto = buchungDetail.getFlrgegenkonto();
				if (gegenkonto != null) {
					rows[row][col++] = gegenkonto.getC_nr() + ", "
							+ gegenkonto.getC_bez();
				} else {
					rows[row][col++] = "";
				}
				rows[row][col++] = buchungDetail.getI_auszug();

				if (buchungDetail.getFlrbuchung().getT_storniert() != null) {
					rows[row][col++] = LocaleFac.STATUS_STORNIERT;
				} else {
					rows[row][col++] = null;
				}
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
		String query = "select count(*) " + getFromClause()
				+ buildWhereClause();
		return getRowCountFromDataBaseByQuery(query);

		// long rowCount = 0;
		// SessionFactory factory = FLRSessionFactory.getFactory();
		// Session session = null;
		// try {
		// session = factory.openSession();
		// String queryString = "select count(*) " + this.getFromClause()
		// + this.buildWhereClause();
		// Query query = session.createQuery(queryString);
		// List<?> rowCountResult = query.list();
		// if (rowCountResult != null && rowCountResult.size() > 0) {
		// rowCount = ((Long) rowCountResult.get(0)).longValue();
		// }
		// } catch (Exception e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		// } finally {
		// try {
		// session.close();
		// } catch (HibernateException he) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, he);
		// }
		// }
		// return rowCount;
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

					if (filterKriterien[i].kritName.equals("c_belegnummer")) {

						// MANDANTENKENNUNG WIRD DERZEIT NICHT UNTERSTUETZT
						try {
							String sValue = filterKriterien[i].value;
							sValue = sValue.replaceAll("%", "");

							sValue = Helper.fitString2LengthAlignRight(sValue,
									stellenBelegnummer, '0');

							sValue = "'%" + trennzeichen + sValue + "'";
							where.append(" buchungdetail.flrbuchung.c_belegnummer");
							where.append(" " + filterKriterien[i].operator);
							where.append(" " + sValue);
						} catch (Exception ex) {
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR,
									ex);
						}
					} else if (filterKriterien[i].kritName.equals("n_betrag")) {

						if (filterKriterien[i].bdValue != null) {

							String bdValue1 = HelperServer.formatNumberForSQL(filterKriterien[i].bdValue.subtract(new BigDecimal(
											filterKriterien[i].bdValue
													.doubleValue()
													* ((double)toleranzBetragsuche / (double)100))));
							String bdValue2 = HelperServer.formatNumberForSQL(filterKriterien[i].bdValue.add(new BigDecimal(
											filterKriterien[i].bdValue
													.doubleValue()
													* ((double)toleranzBetragsuche / (double)100))));

							where.append(" ABS(buchungdetail."
									+ filterKriterien[i].kritName + ") BETWEEN "
									+ bdValue1 + " AND " + bdValue2 + " ");
						}
					} else {

						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" upper(buchungdetail."
									+ filterKriterien[i].kritName + ")");
						} else {
							where.append(" buchungdetail."
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
						orderBy.append("buchungdetail." + kriterien[i].kritName);
						orderBy.append(" ");
						orderBy.append(kriterien[i].value);
					}
				}
			} else {
				// no sort criteria found, add default sort
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append("buchungdetail.id ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("buchungdetail.id") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" buchungdetail.id ");
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
		// return "from FLRFinanzBuchungDetail buchungdetail ";
		return "from FLRFinanzBuchungDetail buchungdetail "
				+ "LEFT OUTER JOIN buchungdetail.flrgegenkonto AS gk ";
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
				String queryString = "select buchungdetail.id from FLRFinanzBuchungDetail buchungdetail "
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

			try {
				trennzeichen = getParameterFac().getMandantparameter(
						theClientDto.getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN,
						ParameterFac.PARAMETER_BELEGNUMMERNFORMAT_TRENNZEICHEN)
						.getCWert();
				stellenBelegnummer = new Integer(
						getParameterFac()
								.getMandantparameter(
										theClientDto.getMandant(),
										ParameterFac.KATEGORIE_ALLGEMEIN,
										ParameterFac.PARAMETER_BELEGNUMMERNFORMAT_STELLEN_BELEGNUMMER)
								.getCWert());
				toleranzBetragsuche = new Integer(getParameterFac()
						.getMandantparameter(theClientDto.getMandant(),
								ParameterFac.KATEGORIE_FINANZ,
								ParameterFac.PARAMETER_TOLERANZ_BETRAGSUCHE)
						.getCWert());
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();
			setTableInfo(new TableInfo(
					new Class[] { Integer.class, /* java.util.Date.class */ java.sql.Timestamp.class,
							java.util.Date.class, String.class, String.class,
							String.class, String.class, BigDecimal.class,
							BigDecimal.class, String.class, Integer.class,
							Icon.class },
					new String[] {
							"Id",
							getTextRespectUISpr("lp.gebucht", mandantCNr, locUI),
							getTextRespectUISpr("lp.buchungsdatum", mandantCNr,
									locUI),
							getTextRespectUISpr("lp.art", mandantCNr, locUI),
							getTextRespectUISpr("bes.belegnummer", mandantCNr,
									locUI),
							getTextRespectUISpr("lp.konto", mandantCNr, locUI),
							getTextRespectUISpr("lp.text", mandantCNr, locUI),
							getTextRespectUISpr("lp.soll", mandantCNr, locUI),
							getTextRespectUISpr("lp.haben", mandantCNr, locUI),
							getTextRespectUISpr("lp.gegenkonto", mandantCNr,
									locUI),
							getTextRespectUISpr("fb.auszug", mandantCNr, locUI),
							getTextRespectUISpr("fb.storno", mandantCNr, locUI) },

					new int[] {
							-1, // diese Spalte wird ausgeblendet
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_XS,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_S },

					new String[] {
							FinanzFac.FLR_BUCHUNGDETAIL_I_ID,
							FinanzFac.FLR_BUCHUNGDETAIL_FLRBUCHUNG + "."
									+ FinanzFac.FLR_BUCHUNG_T_ANLEGEN,
							FinanzFac.FLR_BUCHUNGDETAIL_FLRBUCHUNG + "."
									+ FinanzFac.FLR_BUCHUNG_D_BUCHUNGSDATUM,
							FinanzFac.FLR_BUCHUNGDETAIL_FLRBUCHUNG + "."
									+ FinanzFac.FLR_BUCHUNG_BUCHUNGSART_C_NR,
							FinanzFac.FLR_BUCHUNG_C_BELEGNUMMER,
							FinanzFac.FLR_BUCHUNGDETAIL_FLRKONTO + "."
									+ FinanzFac.FLR_KONTO_C_NR,

							FinanzFac.FLR_BUCHUNGDETAIL_FLRBUCHUNG + "."
									+ FinanzFac.FLR_BUCHUNG_C_TEXT,
							FinanzFac.FLR_BUCHUNGDETAIL_N_BETRAG,
							FinanzFac.FLR_BUCHUNGDETAIL_N_BETRAG,
							FinanzFac.FLR_BUCHUNGDETAIL_FLRGEGENKONTO + "."
									+ FinanzFac.FLR_KONTO_C_NR,
							FinanzFac.FLR_BUCHUNGDETAIL_I_AUSZUG,
							FinanzFac.FLR_BUCHUNGDETAIL_FLRBUCHUNG + "."
									+ FinanzFac.FLR_BUCHUNG_T_STORNIERT }));
		}
		return super.getTableInfo();
	}
}
