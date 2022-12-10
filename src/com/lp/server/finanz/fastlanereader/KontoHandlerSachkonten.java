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
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.finanz.fastlanereader.generated.FLRFinanzErgebnisgruppe;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzKontoDetail;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.system.ejbfac.HvCreatingCachingProvider;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

public class KontoHandlerSachkonten extends KontoHandler {
	private static final long serialVersionUID = -5296378527218306060L;

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {

		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			int colCount = this.getTableInfo().getColumnClasses().length;
			int pageSize = KontoHandler.PAGE_SIZE;
			int startIndex = Math.max(rowIndex.intValue() - (pageSize / 2), 0);
			int endIndex = startIndex + pageSize - 1;
			session = factory.openSession();

			Query query = null;
//			if (filtereKontenNachBuchungen()) {
			query = getKontoDetailQuery(session);
//			} else {
//				query = session.createQuery(
//						super.getFromClause()
//						+ buildWhereClause()
//						+ buildOrderByClause());
//			}

			HvCreatingCachingProvider<String, String> cacheKontoart = new HvCreatingCachingProvider<String, String>() {
				@Override
				protected String provideValue(String key, String transformedKey) {
					try {
						return getFinanzServiceFac().uebersetzeKontoartOptimal(key, theClientDto.getLocUi(),
								theClientDto.getLocMandant());
					} catch (RemoteException e) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
					}
				}
			};
			HvCreatingCachingProvider<Integer, String> cacheUvaart = new HvCreatingCachingProvider<Integer, String>() {
				@Override
				protected String provideValue(Integer key, Integer transformedKey) {
					try {
						return getFinanzServiceFac().uebersetzeUvaartOptimal(key, theClientDto.getLocUi(),
								theClientDto.getLocMandant());
					} catch (RemoteException e) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
					}
				}
			};
			HvCreatingCachingProvider<Integer, String> cacheFinanzamtName = new HvCreatingCachingProvider<Integer, String>() {
				@Override
				protected String provideValue(Integer key, Integer transformedKey) {
					return getPartnerFac().partnerFindByPrimaryKey(key, theClientDto).getCName1nachnamefirmazeile1();
				}
			};

			HvCreatingCachingProvider<Integer, String> cacheMwstsatz = new HvCreatingCachingProvider<Integer, String>() {
				@Override
				protected String provideValue(Integer key, Integer transformedKey) {

					return getMandantFac().mwstsatzFindByPrimaryKey(key, theClientDto).formatMwstsatz(theClientDto);

				}
			};

			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;
			int col = 0;
			while (resultListIterator.hasNext()) {
				Object[] o=(Object[])resultListIterator.next();
				FLRFinanzKontoDetail konto = (FLRFinanzKontoDetail) o[0];

				rows[row][col++] = konto.getI_id();
				rows[row][col++] = konto.getC_nr();
				rows[row][col++] = konto.getC_bez();

				if (konto.getFlrergebnisgruppe() != null) {
					FLRFinanzErgebnisgruppe ergebnisgruppe = konto.getFlrergebnisgruppe();
					rows[row][col++] = (Helper.short2boolean(ergebnisgruppe.getB_bilanzgruppe())
							? getTextRespectUISpr("fb.bilanzgruppe", theClientDto.getMandant(),
									theClientDto.getLocMandant(), ergebnisgruppe.getC_bez())
							: getTextRespectUISpr("fb.ergebnisgruppe", theClientDto.getMandant(),
									theClientDto.getLocMandant(), ergebnisgruppe.getC_bez()));
				} else {
					rows[row][col++] = "";
				}

				if (konto.getFlrkontoart() != null) {
					rows[row][col++] = cacheKontoart.getValueOfKey(konto.getFlrkontoart().getC_nr());
//					rows[row][col++] = getFinanzServiceFac().uebersetzeKontoartOptimal(konto.getFlrkontoart().getC_nr(),
//							theClientDto.getLocUi(), theClientDto.getLocMandant());
				} else {
					rows[row][col++] = "";
				}

				if (konto.getFlruvaart() != null) {
					rows[row][col++] = cacheUvaart.getValueOfKey(konto.getFlruvaart().getI_id());
//					rows[row][col++] = getFinanzServiceFac().uebersetzeUvaartOptimal(konto.getFlruvaart().getI_id(),
//							theClientDto.getLocUi(), theClientDto.getLocMandant());
				} else {
					rows[row][col++] = "";
				}

				if (konto.getFlrmwstsatz() != null) {
					rows[row][col++] = cacheMwstsatz.getValueOfKey(konto.getFlrmwstsatz().getI_id());

				} else {
					rows[row][col++] = "";
				}

				if (konto.getFinanzamt_i_id() != null) {
//					rows[row][col++] = getPartnerFac().partnerFindByPrimaryKey(
//							konto.getFinanzamt_i_id(), theClientDto)
//							.getCName1nachnamefirmazeile1();
					rows[row][col++] = cacheFinanzamtName.getValueOfKey(konto.getFinanzamt_i_id());
				} else {
					rows[row][col++] = "";
				}

				rows[row][col++] = konto.getB_versteckt() > (short) 0 ? Color.lightGray : null;

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
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, he);
			}
		}
		return result;
	}

	private Boolean filtereKontenNachBuchungen() {
		FilterKriterium[] filterKriterien = this.getQuery().getFilterBlock().filterKrit;

		for (FilterKriterium filterKriterium : filterKriterien) {
			if (filterKriterium.kritName.equals(FinanzFac.FILTER_KONTEN_MIT_BUCHUNGEN))
				return true;
		}

		return false;
	}

	private String getIdQueryString() {
		return "SELECT konto.i_id" + getFromClause() + buildWhereClause() + buildGroupByClause() + buildOrderByClause();

	}

	private Query getKontoDetailQuery(Session session) {

		Query query = session.createQuery(getIdQueryString());
		Iterator<?> resultListIterator = query.list().iterator();
		String inOp = "";

		while (resultListIterator.hasNext()) {
			if (inOp.length() != 0) {
				inOp += ",";
			}
			inOp += resultListIterator.next();
		}

		if (inOp.length() > 0) {
			inOp = " WHERE konto.i_id  IN (" + inOp + ")";
			
			//Aufgrund des Sortieren der MwstsatzBezeichnung muss hier noch der LeftOuterJoin hinzugefuegt werden
			String fromClause = " FROM FLRFinanzKontoDetail konto LEFT OUTER JOIN konto.flrmwstsatz AS flr_mwstsatz "
					+ " LEFT OUTER JOIN flr_mwstsatz.flrmwstsatzbez AS flr_mwstsatzbez ";
			
			query = session.createQuery(fromClause + inOp + buildOrderByClause());
		}

		return query;
	}

	protected String getFromClause() {
		String fromClause = " FROM FLRFinanzKontoDetail konto " + " LEFT OUTER JOIN konto.flrkontoust AS flr_kontoust "
				+ " LEFT OUTER JOIN konto.flrkontoskonto AS flr_kontoskonto "
				+ " LEFT OUTER JOIN konto.flrkostenstelle AS flr_kostenstelle "
				+ " LEFT OUTER JOIN konto.flrergebnisgruppe AS flr_ergebnisgruppe "
				+ " LEFT OUTER JOIN konto.flrkontoart AS flr_kontoart "
//				+ " LEFT OUTER JOIN konto.flrsteuerkategorie AS flr_steuerkategorie "
				+ " LEFT OUTER JOIN konto.flruvaart AS flr_uvaart "
				+ " LEFT OUTER JOIN konto.flrmwstsatz AS flr_mwstsatz "
				+ " LEFT OUTER JOIN flr_mwstsatz.flrmwstsatzbez AS flr_mwstsatzbez ";

		if (filtereKontenNachBuchungen()) {
			// Wenn nach "Konten mit Buchungen" gefiltert wird, dann muss auch das
			// Buchungsdetail
			// miteinbezogen werden. Wobei auch hier noch deutlich zu viele Saetze gelesen
			// werden, denn eigentlich muesste ein "count(*) der buchungsdetails ausreichen.
			fromClause += " LEFT OUTER JOIN konto.flrfinanzbuchungdetail AS flr_finanzbuchungdetail "
					+ " LEFT OUTER JOIN flr_finanzbuchungdetail.flrbuchung AS flr_buchung ";
		}

		return fromClause;
	}

	protected String getFromClause0() {
		return " FROM FLRFinanzKontoDetail konto " + " LEFT OUTER JOIN konto.flrkontoust AS flr_kontoust "
				+ " LEFT OUTER JOIN konto.flrkontoskonto AS flr_kontoskonto "
				+ " LEFT OUTER JOIN konto.flrkostenstelle AS flr_kostenstelle "
				+ " LEFT OUTER JOIN konto.flrergebnisgruppe AS flr_ergebnisgruppe "
				+ " LEFT OUTER JOIN konto.flrkontoart AS flr_kontoart "
//				+ " LEFT OUTER JOIN konto.flrsteuerkategorie AS flr_steuerkategorie "
				+ " LEFT OUTER JOIN konto.flruvaart AS flr_uvaart "
				+ " LEFT OUTER JOIN konto.flrmwstsatz AS flr_mwstsatz "
				+ " LEFT OUTER JOIN flr_mwstsatz.flrmwstsatzbez AS flr_mwstsatzbez "
				+ " LEFT OUTER JOIN konto.flrfinanzbuchungdetail AS flr_finanzbuchungdetail "
				+ " LEFT OUTER JOIN flr_finanzbuchungdetail.flrbuchung AS flr_buchung ";
	}

	private String buildGroupByClause() {
		return " GROUP BY konto.c_nr, konto.c_bez, konto.i_id, konto.kontoart_c_nr, konto.flruvaart.c_kennzeichen, flr_mwstsatzbez.c_bezeichnung, flr_mwstsatz.d_gueltigab, konto.finanzamt_i_id ";
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

				Query query = null;
				if (filtereKontenNachBuchungen()) {
					query = session.createQuery(getIdQueryString());
				} else {
					query = session.createQuery(
							"SELECT konto.i_id" + getFromClause() + buildWhereClause() + buildOrderByClause());
				}

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
				sessionClose(session);
			}
		}

		if (rowNumber < 0 || rowNumber >= this.getRowCount()) {
			rowNumber = 0;
		}

		result = this.getPageAt(new Integer(rowNumber));
		result.setIndexOfSelectedRow(rowNumber);

		return result;
	}

	protected TableInfo produceTableInfo() {
		String mandantCNr = theClientDto.getMandant();
		Locale locUI = theClientDto.getLocUi();
		return new TableInfo(
				new Class[] { Integer.class, String.class, String.class, String.class, String.class, String.class,
						String.class, String.class, Color.class },
				new String[] { "Id", getTextRespectUISpr("lp.nr", mandantCNr, locUI),
						getTextRespectUISpr("lp.bezeichnung", mandantCNr, locUI),
						getTextRespectUISpr("lp.hauptgruppe", mandantCNr, locUI),
						getTextRespectUISpr("fb.kontoart", mandantCNr, locUI),
						getTextRespectUISpr("fb.uvaart", mandantCNr, locUI),
						getTextRespectUISpr("fb.konto.variante", mandantCNr, locUI),
						getTextRespectUISpr("fb.finanzamt", mandantCNr, locUI), "" },
				new int[] { -1, 6, -1, 18, 6, 30, 6, 25, 0 },
				new String[] { FinanzFac.FLR_KONTO_I_ID, FinanzFac.FLR_KONTO_C_NR, FinanzFac.FLR_KONTO_C_BEZ,
//						FinanzFac.FLR_KONTO_FLRERGEBNISGRUPPE + "." + FinanzFac.FLR_ERGEBNISGRUPPE_C_BEZ, // ergbnisgruppe
						Facade.NICHT_SORTIERBAR, FinanzFac.FLR_KONTO_FLRKONTOART + "." + FinanzFac.FLR_KONTOART_C_NR,
						FinanzFac.FLR_KONTO_FLRUVAART + "." + FinanzFac.FLR_UVAART_C_KENNZEICHEN, // uvaart
						"flr_mwstsatzbez.c_bezeichnung", FinanzFac.FLR_KONTO_FINANZAMT_I_ID, "" },
				new String[] { null, null, null, null, null, null, getTextRespectUISpr("fb.konto.variante.tooltip", mandantCNr, locUI), null, null, });
	}
}
