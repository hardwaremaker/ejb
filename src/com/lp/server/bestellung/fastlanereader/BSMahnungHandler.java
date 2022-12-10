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

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.anfrage.service.AnfragepositionFac;
import com.lp.server.bestellung.fastlanereader.generated.FLRBSMahnung;
import com.lp.server.bestellung.fastlanereader.generated.FLRBestellposition;
import com.lp.server.bestellung.service.BSMahnwesenFac;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;
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
 * Hier wird die FLR Funktionalitaet fuer die Mahnungen implementiert. Pro
 * UseCase gibt es einen Handler.
 * </p>
 * <p>
 * Copright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum 2005-10-26
 * </p>
 * <p>
 * </p>
 * 
 * @author Josef Erlinger
 * @version 1.0
 */

public class BSMahnungHandler extends UseCaseHandler {
	/**
	 * the size of the data page returned in QueryResult.
	 */

	/**
	 * The information needed for the Konten table.
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String FLR_MAHNUNG = "bsmahnung.";

	/**
	 * gets the data page for the specified row using the current query. The row at
	 * rowIndex will be located in the middle of the page.
	 * 
	 * @see UseCaseHandler#getPageAt(java.lang.Integer)
	 * @param rowIndex Integer
	 * @throws EJBExceptionLP
	 * @return QueryResult
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

				FLRBSMahnung bsmahnung = (FLRBSMahnung) resultListIterator.next();
				FLRBestellposition bsposition = bsmahnung.getFlrbestellposition();

				Object[] rowToAddCandidate = new Object[colCount];
				rowToAddCandidate[getTableColumnInformation().getViewIndex("i_id")] = bsmahnung.getI_id();
				rowToAddCandidate[getTableColumnInformation().getViewIndex("bes.abmahnung")] = bsmahnung
						.getMahnstufe_i_id().equals(new Integer(BSMahnwesenFac.MAHNSTUFE_0)) ? new Boolean(true)
								: new Boolean(false);
				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.bestnr")] = bsmahnung.getFlrbestellung()
						.getC_nr();

				if (bsposition.getFlrartikel() != null) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.ident")] = bsposition.getFlrartikel()
							.getC_nr();
					if (bReferenznummerInPositionen) {
						rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.referenznummer")] = bsposition
								.getFlrartikel().getC_referenznr();

					}

				}

				String bezeichnung = bsposition.getC_bezeichnung();

				if (bezeichnung == null && bsposition.getFlrartikel() != null) {
					bezeichnung = getArtikelFac().formatArtikelbezeichnungEinzeiligOhneExc(
							bsposition.getFlrartikel().getI_id(), theClientDto.getLocUi());
				}

				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.bezeichnung")] = bezeichnung;
				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.menge")] = bsmahnung.getN_offenemenge();
				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.lieferant")] = bsmahnung
						.getFlrbestellung().getFlrlieferant().getFlrpartner().getC_name1nachnamefirmazeile1();
				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.wert")] = bsposition
						.getN_nettogesamtpreis().multiply(bsmahnung.getN_offenemenge());
				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.whg")] = bsmahnung.getFlrbestellung()
						.getWaehrung_c_nr_bestellwaehrung();
				rowToAddCandidate[getTableColumnInformation().getViewIndex("bes.liefertermin")] = bsmahnung
						.getFlrbestellposition().getT_uebersteuerterliefertermin();

				rowToAddCandidate[getTableColumnInformation().getViewIndex("bes.termin")] = bsmahnung
						.getFlrbestellposition().getT_auftragsbestaetigungstermin();

				rowToAddCandidate[getTableColumnInformation().getViewIndex("bes.mahnstufe")] = bsmahnung
						.getMahnstufe_i_id();

				rowToAddCandidate[getTableColumnInformation().getViewIndex("")] = new Boolean(
						bsmahnung.getT_gedruckt() != null);

				// in java.util.Date casten, darf kein java.sql.Date sein sonst
				// geht FLR Druck nicht.
				java.util.Date dUtilDate = null;
				if (bsmahnung.getD_mahndatum() instanceof java.sql.Date) {
					dUtilDate = new java.util.Date(bsmahnung.getD_mahndatum().getTime());
				} else {
					dUtilDate = bsmahnung.getD_mahndatum();
				}
				rows[row][col++] = dUtilDate;

				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.mahndatum")] = dUtilDate;

				rows[row] = rowToAddCandidate;

				row++;
				col = 0;
			}
			result = new QueryResult(rows, this.getRowCount(), startIndex, endIndex, 0);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			closeSession(session);
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
			String queryString = "select count(*) " + this.getFromClause() + this.buildWhereClause();
			Query query = session.createQuery(queryString);
			List<?> rowCountResult = query.list();
			if (rowCountResult != null && rowCountResult.size() > 0) {
				rowCount = ((Long) rowCountResult.get(0)).longValue();
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			closeSession(session);
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
					where.append(" bsmahnung." + filterKriterien[i].kritName);
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
					if (!kriterien[i].kritName.endsWith(Facade.NICHT_SORTIERBAR)) {
						if (kriterien[i].isKrit) {
							if (sortAdded) {
								orderBy.append(", ");
							}
							sortAdded = true;
							orderBy.append(kriterien[i].kritName);
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
				orderBy.append("bsmahnung.i_id ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("bsmahnung.i_id") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" bsmahnung.i_id ");
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
		return "FROM FLRBSMahnung bsmahnung ";
	}

	/**
	 * sorts the data described by the current query using the specified sort
	 * criterias. The current query is also updated with the new sort criterias.
	 * 
	 * @see UseCaseHandler#sort(SortierKriterium[], Object)
	 * @throws EJBExceptionLP
	 * @param sortierKriterien SortierKriterium[]
	 * @param selectedId       Object
	 * @return QueryResult
	 */
	public QueryResult sort(SortierKriterium[] sortierKriterien, Object selectedId) throws EJBExceptionLP {
		this.getQuery().setSortKrit(sortierKriterien);

		QueryResult result = null;
		int rowNumber = 0;

		if (selectedId != null /* && ( (Integer) selectedId).intValue() >= 0 */) {
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();
				String queryString = "select bsmahnung.i_id from FLRBSMahnung bsmahnung " + this.buildWhereClause()
						+ this.buildOrderByClause();
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

		if (rowNumber < 0 || rowNumber >= this.getRowCount()) {
			rowNumber = 0;
		}

		result = this.getPageAt(new Integer(rowNumber));
		result.setIndexOfSelectedRow(rowNumber);

		return result;
	}

	public TableInfo getTableInfo() {
		TableInfo info = super.getTableInfo();
		if (info != null)
			return info;

		setTableColumnInformation(createColumnInformation(theClientDto.getMandant(), theClientDto.getLocUi()));

		TableColumnInformation c = getTableColumnInformation();
		info = new TableInfo(c.getClasses(), c.getHeaderNames(), c.getWidths(), c.getDbColumNames(),
				c.getHeaderToolTips());
		setTableInfo(info);
		return info;
	}

	private TableColumnInformation createColumnInformation(String mandant, Locale locUi) {
		TableColumnInformation columns = new TableColumnInformation();
		try {

			String mandantCNr = theClientDto.getMandant();

			int iNachkommastellenMenge = getMandantFac().getNachkommastellenMenge(mandantCNr);
			int iNachkommastellenPreis = getMandantFac().getNachkommastellenPreisVK(mandantCNr);

			columns.add("i_id", Integer.class, "i_id", QueryParameters.FLR_BREITE_SHARE_WITH_REST, "i_id");

			columns.add("bes.abmahnung", Boolean.class, getTextRespectUISpr("bes.abmahnung", mandant, locUi),
					QueryParameters.FLR_BREITE_M, Facade.NICHT_SORTIERBAR,
					getTextRespectUISpr("bes.abmahnung.tooltip", theClientDto.getMandant(), theClientDto.getLocUi()));

			/*
			 * if (bReferenznummerInPositionen) { columns.add("lp.referenznummer",
			 * String.class, getTextRespectUISpr("lp.referenznummer", mandant, locUi),
			 * QueryParameters.FLR_BREITE_XM,
			 * AnfragepositionFac.FLR_ANFRAGEPOSITION_FLRARTIKEL + ".c_referenznr"); }
			 */

			columns.add("lp.bestnr", String.class, getTextRespectUISpr("lp.bestnr", mandant, locUi),
					QueryParameters.FLR_BREITE_M,
					FLR_MAHNUNG + BSMahnwesenFac.FLR_MAHNUNG_FLRBESTELLUNG + "." + BestellungFac.FLR_BESTELLUNG_C_NR);

			columns.add("lp.ident", String.class, getTextRespectUISpr("lp.ident", mandant, locUi), getUIBreiteIdent(),
					FLR_MAHNUNG + BSMahnwesenFac.FLR_MAHNUNG_FLRBESTELLPOSITION + "."
							+ BestellpositionFac.FLR_BESTELLPOSITION_FLRARTIKEL + ".c_nr");

			if (bReferenznummerInPositionen) {
				columns.add("lp.referenznummer", String.class, getTextRespectUISpr("lp.referenznummer", mandant, locUi),
						QueryParameters.FLR_BREITE_XM, FLR_MAHNUNG + BSMahnwesenFac.FLR_MAHNUNG_FLRBESTELLPOSITION + "."
								+ BestellpositionFac.FLR_BESTELLPOSITION_FLRARTIKEL + ".c_referenznr");
			}

			columns.add("lp.bezeichnung", String.class, getTextRespectUISpr("lp.bezeichnung", mandant, locUi),
					QueryParameters.FLR_BREITE_SHARE_WITH_REST,
					FLR_MAHNUNG + BSMahnwesenFac.FLR_MAHNUNG_FLRBESTELLPOSITION + "."
							+ BestellpositionFac.FLR_BESTELLPOSITION_C_BEZEICHNUNG);
			columns.add("lp.menge", super.getUIClassBigDecimalNachkommastellen(iNachkommastellenMenge),
					getTextRespectUISpr("lp.menge", mandant, locUi), QueryParameters.FLR_BREITE_M,
					FLR_MAHNUNG + BSMahnwesenFac.FLR_MAHNUNG_OFFENEMENGE);
			columns.add("lp.lieferant", String.class, getTextRespectUISpr("lp.lieferant", mandant, locUi),
					QueryParameters.FLR_BREITE_SHARE_WITH_REST,
					FLR_MAHNUNG + BSMahnwesenFac.FLR_MAHNUNG_FLRBESTELLUNG + "."
							+ BestellungFac.FLR_BESTELLUNG_FLRLIEFERANT + "."
							+ KundeFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1);
			columns.add("lp.wert", BigDecimal.class, getTextRespectUISpr("lp.wert", mandant, locUi),
					QueryParameters.FLR_BREITE_PREIS, FLR_MAHNUNG + BSMahnwesenFac.FLR_MAHNUNG_FLRBESTELLUNG + "."
							+ BestellungFac.FLR_BESTELLUNG_N_BESTELLWERT);
			columns.add("lp.whg", String.class, getTextRespectUISpr("lp.whg", mandant, locUi),
					QueryParameters.FLR_BREITE_WAEHRUNG, FLR_MAHNUNG + BSMahnwesenFac.FLR_MAHNUNG_FLRBESTELLUNG + "."
							+ BestellungFac.FLR_BESTELLUNG_WAEHRUNG_C_NR);

			columns.add("bes.liefertermin", java.util.Date.class,
					getTextRespectUISpr("bes.liefertermin", mandant, locUi), QueryParameters.FLR_BREITE_M,
					FLR_MAHNUNG + BSMahnwesenFac.FLR_MAHNUNG_FLRBESTELLPOSITION + "."
							+ BestellpositionFac.FLR_BESTELLPOSITION_T_UEBERSTEUERTERLIEFERTERMIN);
			columns.add("bes.termin", java.util.Date.class, getTextRespectUISpr("bes.termin", mandant, locUi),
					QueryParameters.FLR_BREITE_M, FLR_MAHNUNG + BSMahnwesenFac.FLR_MAHNUNG_FLRBESTELLPOSITION + "."
							+ BestellpositionFac.FLR_BESTELLPOSITION_T_AUFTRAGSBESTAETIGUNGSTERMIN);

			columns.add("bes.mahnstufe", Integer.class, getTextRespectUISpr("bes.mahnstufe", mandant, locUi), 2,
					FLR_MAHNUNG + BSMahnwesenFac.FLR_MAHNUNG_MAHNSTUFE_I_ID,
					getTextRespectUISpr("bes.mahnstufe.tooltip", theClientDto.getMandant(), theClientDto.getLocUi()));
			columns.add("lp.mahndatum", java.util.Date.class, getTextRespectUISpr("lp.mahndatum", mandant, locUi),
					QueryParameters.FLR_BREITE_M, FLR_MAHNUNG + BSMahnwesenFac.FLR_MAHNUNG_D_MAHNDATUM);
			columns.add("", Boolean.class, "", 2, FLR_MAHNUNG + BSMahnwesenFac.FLR_MAHNUNG_T_GEDRUCKT);

		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		return columns;

	}

}
