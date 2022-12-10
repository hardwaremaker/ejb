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
package com.lp.server.bestellung.fastlanereader;

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

import com.lp.server.artikel.fastlanereader.generated.FLRArtikellistespr;
import com.lp.server.bestellung.fastlanereader.generated.FLRWarezwischenmandanten;
import com.lp.server.personal.fastlanereader.generated.FLRReligionspr;
import com.lp.server.system.fastlanereader.generated.FLRKostentraeger;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.UseCaseHandler;
import com.lp.server.util.fastlanereader.service.query.FilterBlock;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

public class WarezwischenmandantenHandler extends UseCaseHandler {

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
			String sLocUI = Helper.locale2String(theClientDto.getLocUi());

			while (resultListIterator.hasNext()) {
				Object[] o = (Object[]) resultListIterator.next();

				FLRWarezwischenmandanten flr = (FLRWarezwischenmandanten) o[0];

				rows[row][col++] = flr.getC_nr();

				if (flr.getFlrbestellposition().getFlrartikel() != null) {

					rows[row][col++] = flr.getFlrbestellposition()
							.getFlrartikel().getC_nr();

					rows[row][col++] = findSpr(sLocUI, flr
							.getFlrbestellposition().getFlrartikel()
							.getArtikelsprset().iterator());
				} else {
					rows[row][col++] = null;
					rows[row][col++] = null;
				}
				try {
					rows[row][col++] = flr.getFlrbestellposition()
							.getFlrbestellung().getC_nr();
					rows[row][col++] = getBestellpositionFac()
							.berechneOffeneMenge(flr.getBestellposition_i_id());

					rows[row][col++] = flr.getFlrbestellposition()
							.getT_auftragsbestaetigungstermin();

					rows[row][col++] = flr.getFlrauftragposition()
							.getFlrauftrag().getMandant_c_nr();
					rows[row][col++] = flr.getFlrauftragposition()
							.getFlrauftrag().getC_nr();

					if (flr.getFlrlieferscheinposition() != null) {

						rows[row][col++] = flr.getFlrlieferscheinposition()
								.getFlrlieferschein().getC_nr()
								+ (flr.getFlrlieferscheinposition()
										.getWareneingangsposition_i_id_anderermandant() != null ? " (Z)"
										: "");
						rows[row][col++] = flr.getFlrlieferscheinposition()
								.getN_menge();
						rows[row][col++] = flr.getFlrlieferscheinposition()
								.getFlrlieferschein().getD_belegdatum();

						rows[row][col++] = getStatusMitUebersetzung(flr
								.getFlrlieferscheinposition()
								.getFlrlieferschein()
								.getLieferscheinstatus_status_c_nr(), flr
								.getFlrlieferscheinposition()
								.getFlrlieferschein().getT_versandzeitpunkt(),
								flr.getFlrlieferscheinposition()
										.getFlrlieferschein()
										.getC_versandtype());

					}

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
				row++;

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

	private String findSpr(String sLocaleI, Iterator<?> iterUebersetzungenI) {

		String sUebersetzung = null;
		while (iterUebersetzungenI.hasNext()) {
			FLRArtikellistespr spr = (FLRArtikellistespr) iterUebersetzungenI
					.next();
			if (spr.getLocale_c_nr().compareTo(sLocaleI) == 0) {
				sUebersetzung = spr.getC_bez();
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
			session = setFilter(session);
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

			boolean bNurUnterwegs = true;

			for (int i = 0; i < filterKriterien.length; i++) {
				if (filterKriterien[i].isKrit
						&& filterKriterien[i].kritName.equals("NUR_UNTERWEGS")) {
					bNurUnterwegs = false;
				}
			}

			if (bNurUnterwegs) {
				where.append(" WHERE (( warezwischenmandanten.flrbestellposition.flrbestellung.bestellungstatus_c_nr <>'Erledigt' AND warezwischenmandanten.flrbestellposition.bestellpositionstatus_c_nr NOT IN ('"
						+ LocaleFac.STATUS_ERLEDIGT
						+ "','"
						+ LocaleFac.STATUS_GELIEFERT
						+ "') ) AND ( warezwischenmandanten.lieferscheinposition_i_id IS NOT NULL AND warezwischenmandanten.flrlieferscheinposition.wareneingangsposition_i_id_anderermandant IS NULL) ) ");

			} else {
				where.append(" WHERE (( warezwischenmandanten.flrbestellposition.flrbestellung.bestellungstatus_c_nr <>'Erledigt' AND warezwischenmandanten.flrbestellposition.bestellpositionstatus_c_nr NOT IN ('"
						+ LocaleFac.STATUS_ERLEDIGT
						+ "','"
						+ LocaleFac.STATUS_GELIEFERT
						+ "') ) OR ( warezwischenmandanten.lieferscheinposition_i_id IS NOT NULL AND warezwischenmandanten.flrlieferscheinposition.wareneingangsposition_i_id_anderermandant IS NULL) ) ");

			}

			for (int i = 0; i < filterKriterien.length; i++) {
				if (filterKriterien[i].isKrit && !filterKriterien[i].kritName.equals("NUR_UNTERWEGS")) {

					where.append(" " + booleanOperator);

					if (filterKriterien[i].isBIgnoreCase()) {
						where.append(" upper(warezwischenmandanten."
								+ filterKriterien[i].kritName + ")");
					} else {
						where.append(" warezwischenmandanten."
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
						orderBy.append("warezwischenmandanten."
								+ kriterien[i].kritName);
						orderBy.append(" ");
						orderBy.append(kriterien[i].value);
					}
				}
			} else {
				// no sort criteria found, add default sort
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append("warezwischenmandanten.flrbestellposition.flrbestellung.c_nr ASC ");
				sortAdded = true;
			}
			if (orderBy
					.indexOf("warezwischenmandanten.flrbestellposition.flrbestellung.c_nr") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" warezwischenmandanten.flrbestellposition.flrbestellung.c_nr ");
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
		return "from FLRWarezwischenmandanten warezwischenmandanten LEFT OUTER JOIN warezwischenmandanten.flrlieferscheinposition AS flrlieferscheinposition LEFT OUTER JOIN warezwischenmandanten.flrlieferscheinposition.flrlieferschein AS flrlieferschein ";
	}

	public QueryResult sort(SortierKriterium[] sortierKriterien,
			Object selectedId) throws EJBExceptionLP {
		this.getQuery().setSortKrit(sortierKriterien);

		QueryResult result = null;
		int rowNumber = 0;

		if (selectedId != null) {
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();
				session = setFilter(session);
				String queryString = "select warezwischenmandanten.c_nr from FLRWarezwischenmandanten warezwischenmandanten LEFT OUTER JOIN warezwischenmandanten.flrlieferscheinposition AS flrlieferscheinposition LEFT OUTER JOIN warezwischenmandanten.flrlieferscheinposition.flrlieferschein AS flrlieferschein  "
						+ this.buildWhereClause() + this.buildOrderByClause();
				Query query = session.createQuery(queryString);
				ScrollableResults scrollableResult = query.scroll();
				if (scrollableResult != null) {
					scrollableResult.beforeFirst();
					while (scrollableResult.next()) {
						String c_nr = scrollableResult.getString(0); // TYPE OF
						// KEY
						// ATTRIBUTE
						// !!!
						if (selectedId.equals(c_nr)) {
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
		try {
			if (super.getTableInfo() == null) {
				int iNachkommastellenMenge = getMandantFac()
						.getNachkommastellenMenge(theClientDto.getMandant());

				String mandantCNr = theClientDto.getMandant();
				Locale locUI = theClientDto.getLocUi();
				setTableInfo(new TableInfo(
						new Class[] {
								String.class,
								String.class,
								String.class,
								String.class,
								super.getUIClassBigDecimalNachkommastellen(iNachkommastellenMenge),
								java.util.Date.class,
								String.class,
								String.class,
								String.class,
								super.getUIClassBigDecimalNachkommastellen(iNachkommastellenMenge),
								java.util.Date.class, Icon.class },
						new String[] {
								"Id",
								getTextRespectUISpr(
										"artikel.artikelnummerlang",
										mandantCNr, locUI),
								getTextRespectUISpr("lp.bezeichnung",
										mandantCNr, locUI),
								getTextRespectUISpr("lp.bestellnummer",
										mandantCNr, locUI),
								getTextRespectUISpr(
										"bes.warezwischenmandanten.offenewepmenge",
										mandantCNr, locUI),
								getTextRespectUISpr("bes.termin", mandantCNr,
										locUI),
								getTextRespectUISpr("report.mandant",
										mandantCNr, locUI),
								getTextRespectUISpr(
										"fert.ablieferstatistik.sortierung.auftragsnummer",
										mandantCNr, locUI),
								getTextRespectUISpr("bes.lieferscheinnr",
										mandantCNr, locUI),
								getTextRespectUISpr("lp.menge", mandantCNr,
										locUI),
								getTextRespectUISpr("bes.lieferscheindatum",
										mandantCNr, locUI),
								getTextRespectUISpr("lp.status", mandantCNr,
										locUI) },
						new String[] {
								"c_nr",
								"flrbestellposition.flrartikel.c_nr",
								Facade.NICHT_SORTIERBAR,
								"flrbestellposition.flrbestellung.c_nr",
								Facade.NICHT_SORTIERBAR,
								"flrbestellposition.t_auftragsbestaetigungstermin",
								"flrlieferscheinposition.flrlieferschein.mandant_c_nr",
								"flrauftragposition.flrauftrag.c_nr",
								"flrlieferscheinposition.flrlieferschein.c_nr",
								"flrlieferscheinposition.n_menge",
								"flrlieferscheinposition.flrlieferschein.d_belegdatum",
								"flrlieferscheinposition.flrlieferschein.lieferscheinstatus_status_c_nr" }));

			}

			return super.getTableInfo();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}
}
