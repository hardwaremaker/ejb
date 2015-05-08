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

import java.awt.Color;
import java.awt.Font;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.Icon;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.lp.server.finanz.ejb.BuchungdetailText;
import com.lp.server.finanz.ejbfac.BuchungDetailQueryBuilder;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzBuchung;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzBuchungDetail;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto;
import com.lp.server.finanz.service.BuchenFac;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.TheClientDto;
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
import com.lp.util.Pair;

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

public class BuchungDetailHandler extends UseCaseHandler {
	/**
	 * 
	 */

	// private static final String getSumForSelectedIIds = "SELECT "
	private static final long serialVersionUID = 1L;

	String trennzeichen = "";
	Integer stellenBelegnummer = 7;

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;

		BuchungdetailText buchungdetailText = new BuchungdetailText(
				getBenutzerServicesFac(), theClientDto);
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
			String[] tooltipData = new String[resultList.size()];
			int row = 0;
			int col = 0;
			while (resultListIterator.hasNext()) {
				Object o[] = (Object[]) resultListIterator.next();
				FLRFinanzBuchungDetail buchungDetail = (FLRFinanzBuchungDetail) o[0];
				FLRFinanzBuchung buchung = buchungDetail.getFlrbuchung();

				rows[row][col++] = buchungDetail.getI_id();
				rows[row][col++] = buchung.getD_buchungsdatum();
				rows[row][col++] = buchung.getC_belegnummer().trim();
				rows[row][col++] = buchungdetailText
						.getTextFuerAutomatischeEBBuchung(buchung);
				rows[row][col++] = buchungdetailText
						.getTextFuerBuchungsart(buchung);

				rows[row][col++] = buchung.getC_text();

				if (buchungDetail.getBuchungdetailart_c_nr().equals(
						BuchenFac.HabenBuchung)) {
					rows[row][col++] = null;
					rows[row][col++] = buchungDetail.getN_betrag();
				} else {
					rows[row][col++] = buchungDetail.getN_betrag();
					rows[row][col++] = null;
				}

				rows[row][col++] = buchungDetail.getN_ust();
				FLRFinanzKonto gegenkonto = buchungDetail.getFlrgegenkonto();
				if (gegenkonto != null) {
					rows[row][col++] = gegenkonto.getC_nr() + ", "
							+ gegenkonto.getC_bez();
				} else {
					rows[row][col++] = "";
				}
				rows[row][col++] = buchungDetail.getI_auszug();
				rows[row][col++] = buchungDetail.getI_ausziffern();
				if (buchungDetail.getFlrbuchung().getT_storniert() != null) {
					rows[row][col++] = LocaleFac.STATUS_STORNIERT;
				} else {
					rows[row][col++] = null;
				}

				if (buchungDetail.getKommentar() != null) {
					rows[row][col++] = !buchungDetail.getKommentar().isEmpty();
					tooltipData[row] = buchungDetail.getKommentar().isEmpty() ? null
							: Helper.removeStyles(buchungDetail.getKommentar());
				} else {
					rows[row][col++] = false;
				}
				if (Helper.short2boolean(buchung.getB_autombuchung())) {
					rows[row][col++] = Color.CYAN.darker();
				} else {
					rows[row][col++] = null;
				}
				row++;
				col = 0;
			}
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
		String query = "select count(*) " + getFromClause()
				+ buildWhereClause();
		return getRowCountFromDataBaseByQuery(query);
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

					if (filterKriterien[i].kritName.equals(FinanzFac.FLR_BUCHUNG_C_BELEGNUMMER)) {

						where.append(buildWhereClauseRTrim("buchungdetail.flrbuchung.", filterKriterien[i]));
//						try {
//							String sValue = filterKriterien[i].value;
//							where.append(" rtrim(buchungdetail.flrbuchung.c_belegnummer)");
//							where.append(" " + filterKriterien[i].operator);
//							where.append(" " + sValue);
//						} catch (Exception ex) {
//							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR,
//									ex);
//						}
					} else if (filterKriterien[i].kritName
							.equals(FinanzFac.FILTER_BUCHUNGDETAILS_NUR_OFFENE)) {
						where.append(BuchungDetailQueryBuilder.buildNurOffeneBuchungDetails("buchungdetail"));
					} else {
						where.append(buildWhereClausePart("buchungdetail.", filterKriterien[i]));
					}
				}
			}
			if (filterAdded) {
				where.insert(0, " WHERE");
			}
		}
		// where.append("(SELECT SUM(CASE WHEN BD.BUCHUNGDETAILART_C_NR LIKE 'HABEN%' THEN -BD.N_BETRAG ELSE BD.N_BETRAG END) "+
		// "FROM FB_BUCHUNGDETAIL AS BD, FB_BUCHUNG AS B "+
		// "WHERE BD.KONTO_I_ID = 204 "+
		// "AND B.T_STORNIERT IS NULL "+
		// "AND BD.BUCHUNG_I_ID = B.I_ID "+
		// "AND B.GESCHAEFTSJAHR_I_GESCHAEFTSJAHR = 2013 "+
		// "AND ( "+
		// "FB_BUCHUNG.C_BELEGNUMMER LIKE B.C_BELEGNUMMER "+
		// "OR (BD.I_AUSZIFFERN IS NOT NULL AND BD.I_AUSZIFFERN = FB_BUCHUNGDETAIL.I_AUSZIFFERN)"+
		// "))");

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

				String defaultFilter = "id";
				FilterKriterium[] filterKriterien = this.getQuery()
						.getFilterBlock().filterKrit;

				for (int i = 0; i < filterKriterien.length; i++) {
					if (filterKriterien[i].isKrit) {
						if (filterKriterien[i].kritName.equals("konto_i_id")
								&& filterKriterien[i].value != null) {

							Integer wert = new Integer(
									filterKriterien[i].value
											.replaceAll("'", ""));
							try {
								KontoDto ktoDto = getFinanzFac()
										.kontoFindByPrimaryKey(wert);

								if (ktoDto.getCsortierung() != null) {

									if (ktoDto.getCsortierung().equals(
											FinanzFac.KONTO_SORTIERUNG_AUSZUG)) {
										defaultFilter = "i_auszug";
									} else if (ktoDto.getCsortierung().equals(
											FinanzFac.KONTO_SORTIERUNG_BELEG)) {
										defaultFilter = "flrbuchung.c_belegnummer";
									} else if (ktoDto.getCsortierung().equals(
											FinanzFac.KONTO_SORTIERUNG_DATUM)) {
										defaultFilter = "flrbuchung.d_buchungsdatum";
									}

								}

							} catch (RemoteException e) {
								throwEJBExceptionLPRespectOld(e);
							}

						}
					}
				}
				orderBy.append("buchungdetail." + defaultFilter + " ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("buchungdetail.id") < 0) {
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
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();
			setTableInfo(new TableInfo(
					new Class[] { Integer.class, java.util.Date.class,
							String.class, String.class, String.class,
							String.class, BigDecimal.class, BigDecimal.class,
							BigDecimal.class, String.class, Integer.class,
							Integer.class, Icon.class, Boolean.class,
							Color.class },
					new String[] {
							"Id",
							getTextRespectUISpr("lp.datum", mandantCNr, locUI),
							getTextRespectUISpr("bes.belegartnummer",
									mandantCNr, locUI),
							getTextRespectUISpr("lp.buchungtypeb", mandantCNr,
									locUI),
							getTextRespectUISpr("lp.art", mandantCNr, locUI),
							getTextRespectUISpr("lp.text", mandantCNr, locUI),
							getTextRespectUISpr("lp.soll", mandantCNr, locUI),
							getTextRespectUISpr("lp.haben", mandantCNr, locUI),
							getTextRespectUISpr("lp.ust", mandantCNr, locUI),
							getTextRespectUISpr("lp.gegenkonto", mandantCNr,
									locUI),
							getTextRespectUISpr("fb.auszug", mandantCNr, locUI),
							getTextRespectUISpr("fb.ausziffern", mandantCNr,
									locUI),
							getTextRespectUISpr("fb.storno", mandantCNr, locUI),
							getTextRespectUISpr("lp.kommentar", mandantCNr,
									locUI) },

					new int[] {
							-1, // diese Spalte wird ausgeblendet
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_S,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_S,
							QueryParameters.FLR_BREITE_S,
							QueryParameters.FLR_BREITE_S },

					new String[] {
							FinanzFac.FLR_BUCHUNGDETAIL_I_ID,
							FinanzFac.FLR_BUCHUNGDETAIL_FLRBUCHUNG + "."
									+ FinanzFac.FLR_BUCHUNG_D_BUCHUNGSDATUM,
							// FinanzFac.FLR_BUCHUNG_C_BELEGNUMMER,
							FinanzFac.FLR_BUCHUNGDETAIL_FLRBUCHUNG + "."
									+ FinanzFac.FLR_BUCHUNG_C_BELEGNUMMER,
							FinanzFac.FLR_BUCHUNGDETAIL_FLRBUCHUNG + "."
									+ FinanzFac.FLR_BUCHUNG_AUTOMBUCHUNG_EB,
							FinanzFac.FLR_BUCHUNGDETAIL_FLRBUCHUNG + "."
									+ FinanzFac.FLR_BUCHUNG_BUCHUNGSART_C_NR,
							FinanzFac.FLR_BUCHUNGDETAIL_FLRBUCHUNG + "."
									+ FinanzFac.FLR_BUCHUNG_C_TEXT,
							FinanzFac.FLR_BUCHUNGDETAIL_N_BETRAG,
							FinanzFac.FLR_BUCHUNGDETAIL_N_BETRAG,
							FinanzFac.FLR_BUCHUNGDETAIL_N_UST,
							FinanzFac.FLR_BUCHUNGDETAIL_FLRGEGENKONTO + "."
									+ FinanzFac.FLR_KONTO_C_NR,
							FinanzFac.FLR_BUCHUNGDETAIL_I_AUSZUG,
							FinanzFac.FLR_BUCHUNGDETAIL_I_AUSZIFFERN,
							FinanzFac.FLR_BUCHUNGDETAIL_FLRBUCHUNG + "."
									+ FinanzFac.FLR_BUCHUNG_T_STORNIERT,
							FinanzFac.FLR_BUCHUNGDETAIL_KOMMENTAR }));
		}
		return super.getTableInfo();
	}

	@Override
	public List<Pair<?, ?>> getInfoForSelectedIIds(TheClientDto theClientDto,
			List<Object> selectedIIds) {
		if (selectedIIds == null || selectedIIds.size() < 2) // es wurde einer
																// oder kein
																// Eintrag
																// ausgewaehlt
			return null;
		Session session = FLRSessionFactory.getFactory().openSession();
		Criteria crit = session.createCriteria(FLRFinanzBuchungDetail.class);
		crit.add(Restrictions.in("i_id", selectedIIds.toArray()));

		@SuppressWarnings("unchecked")
		List<FLRFinanzBuchungDetail> list = new ArrayList<FLRFinanzBuchungDetail>(
				crit.list());

		BigDecimal soll = BigDecimal.ZERO;
		BigDecimal haben = BigDecimal.ZERO;
		for (FLRFinanzBuchungDetail bd : list) {
			if (BuchenFac.HabenBuchung.equals(bd.getBuchungdetailart_c_nr())) {
				haben = haben.add(bd.getN_betrag());
			} else {
				soll = soll.add(bd.getN_betrag());
			}
		}

		String mandantCNr = theClientDto.getMandant();
		Locale locUI = theClientDto.getLocUi();
		List<Pair<?, ?>> pairs = new ArrayList<Pair<?, ?>>();

		pairs.add(new Pair<String, Object>(getTextRespectUISpr("lp.soll",
				mandantCNr, locUI), Helper.formatZahl(soll, locUI)));
		pairs.add(new Pair<String, Object>(getTextRespectUISpr("lp.haben",
				mandantCNr, locUI), Helper.formatZahl(haben, locUI)));
		pairs.add(new Pair<String, Object>(getTextRespectUISpr("lp.saldo",
				mandantCNr, locUI), Helper.formatZahl(soll.subtract(haben),
				locUI)));

		return pairs;
	}
}
