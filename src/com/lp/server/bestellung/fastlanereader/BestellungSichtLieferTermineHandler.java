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

import java.awt.Color;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.anfrage.service.AnfragepositionFac;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.fastlanereader.generated.FLRBestellungAuftragsbestaetigung;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
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
import com.lp.util.HelperReport;

/**
 * <p>
 * <I>FLR-Handler fuer Bestellpositionen</I>
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellungsdatum <I>10.02.05</I>
 * </p>
 * 
 * <p>
 * </p>
 * 
 * @author GH
 * @version 1.0
 */
public class BestellungSichtLieferTermineHandler extends UseCaseHandler {

	/**
	 * the size of the data page returned in QueryResult.
	 */

	/**
	 * The information needed for the Bestellung table.
	 */

	/**
	 * Konstruktor.
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean bZentralerArtikelstamm = false;

	/**
	 * gets the page of data for the specified row using the current
	 * queryParameters.
	 * 
	 * @param rowIndex the index of the row that should be contained in the page.
	 * @return the data page for the specified row.
	 * @throws EJBExceptionLP
	 */
	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = BestellungHandler.PAGE_SIZE;
			int startIndex = Math.max(rowIndex.intValue() - (pageSize / 2), 0);
			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			String queryString = this.getFromClause() + this.buildWhereClause() + this.buildOrderByClause();

			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			// darf Preise sehen.
			final boolean bDarfPreiseSehen = getTheJudgeFac().hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_EINKAUF,
					theClientDto);
			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;
			int col = 0;
			while (resultListIterator.hasNext()) {
				FLRBestellungAuftragsbestaetigung position = (FLRBestellungAuftragsbestaetigung) resultListIterator
						.next();
				// wenn Handeingabe oder Ident
				if (position.getBestellpositionart_c_nr().equals(BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE)
						|| position.getBestellpositionart_c_nr().equals(BestellpositionFac.BESTELLPOSITIONART_IDENT)) {
					rows[row][col++] = position.getI_id() == null ? null : position.getI_id();

					rows[row][col++] = position.getI_id() == null ? null
							: getBestellpositionFac().getPositionNummerReadOnly(position.getI_id());

					if (position.getFlrartikel() != null && position.getN_menge() != null) {
						ArtikellieferantDto artikellieferantDto = getArtikelFac().getArtikelEinkaufspreis(
								position.getFlrartikel().getI_id(),
								position.getFlrbestellung().getLieferant_i_id_bestelladresse(), position.getN_menge(),

								theClientDto.getSMandantenwaehrung(),
								new java.sql.Date(position.getFlrbestellung().getT_belegdatum().getTime()),
								theClientDto);

						if (artikellieferantDto != null) {
							rows[row][col++] = artikellieferantDto.getCArtikelnrlieferant();
						} else {
							rows[row][col++] = null;
						}

					} else {
						rows[row][col++] = null;
					}

					rows[row][col++] = position.getFlrartikel() == null ? null : position.getFlrartikel().getC_nr();
					if (bReferenznummerInPositionen) {
						rows[row][col++] = position.getFlrartikel() == null ? null
								: position.getFlrartikel().getC_referenznr();
					}

					rows[row][col++] = position.getN_menge() == null ? null : position.getN_menge();

					rows[row][col++] = position.getEinheit_c_nr();

					// in der Spalte Bezeichnung koennen verschiedene Dinge
					// stehen
					String sBezeichnung = null;

					if (position.getBestellpositionart_c_nr().equals(LocaleFac.POSITIONSART_IDENT)) {
						// die sprachabhaengig Artikelbezeichnung anzeigen
						sBezeichnung = getArtikelFac().formatArtikelbezeichnungEinzeiligOhneExc(
								position.getFlrartikel().getI_id(), theClientDto.getLocUi());
					} else {
						// die uebersteuerte bezeichnung
						if (position.getC_bezeichnung() != null) {
							sBezeichnung = position.getC_bezeichnung();
						}
					}
					rows[row][col++] = sBezeichnung == null ? null : sBezeichnung;
					if (bDarfPreiseSehen) {
						rows[row][col++] = position.getN_nettogesamtpreis() == null ? null
								: position.getN_nettogesamtpreis();
					} else {
						rows[row][col++] = null;
					}

					rows[row][col++] = position.getFlrbestellung().getWaehrung_c_nr_bestellwaehrung().trim();
					rows[row][col++] = position.getT_uebersteuerterliefertermin();
					rows[row][col++] = HelperReport.getCalendarWeekOfDate(position.getT_uebersteuerterliefertermin())
							+ "";
					rows[row][col++] = position.getT_auftragsbestaetigungstermin() == null ? null
							: position.getT_auftragsbestaetigungstermin();

					rows[row][col++] = position.getT_auftragsbestaetigungstermin() == null ? null
							: HelperReport.getCalendarWeekOfDate(position.getT_auftragsbestaetigungstermin()) + "";

					rows[row][col++] = position.getFlrbestellung().getMahnstufe_i_id();
					rows[row][col++] = null;

					Boolean b = Boolean.FALSE;
					if (position.getT_lieferterminbestaetigt() != null) {
						b = Boolean.TRUE;
					}
					rows[row][col++] = b;

					if (bZentralerArtikelstamm == true) {

						rows[row][col++] = getArtikelbestelltFac()
								.getWareUnterwegsEinerBestellposition(position.getI_id(), theClientDto);
					}
				}
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
	 * gets the total number of rows available using the current query.
	 * 
	 * @return the number of rows in the query.
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
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
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
					where.append(" position." + filterKriterien[i].kritName);
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
						orderBy.append("position." + kriterien[i].kritName);
						orderBy.append(" ");
						orderBy.append(kriterien[i].value);
					}
				}
			} else {
				// no sort criteria found, add default sort
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append("position.").append(BestellpositionFac.FLR_BESTELLPOSITION_I_SORT).append(" ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("position." + BestellpositionFac.FLR_BESTELLPOSITION_I_SORT) < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" position.").append(BestellpositionFac.FLR_BESTELLPOSITION_I_SORT).append(" ");
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
		return "from FLRBestellungAuftragsbestaetigung position ";
	}

	private TableColumnInformation createColumnInformation(String mandant, Locale locUi) {
		TableColumnInformation columns = new TableColumnInformation();
		try {
			String mandantCNr = theClientDto.getMandant();

			int iNachkommastellenMenge = getMandantFac().getNachkommastellenMenge(mandantCNr);
			int iNachkommastellenPreis = getMandantFac().getNachkommastellenPreisEK(mandantCNr);

			bZentralerArtikelstamm = getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
					MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, theClientDto);

			columns.add("i_id", Integer.class, "i_id", QueryParameters.FLR_BREITE_SHARE_WITH_REST, "i_id");
			columns.add("lp.nr", String.class, getTextRespectUISpr("lp.nr", mandant, locUi),
					QueryParameters.FLR_BREITE_S, Facade.NICHT_SORTIERBAR);
			columns.add("bes.lfartikelnr", String.class, getTextRespectUISpr("bes.lfartikelnr", mandant, locUi),
					QueryParameters.FLR_BREITE_SHARE_WITH_REST, Facade.NICHT_SORTIERBAR);
			columns.add("lp.ident", String.class, getTextRespectUISpr("lp.ident", mandant, locUi), getUIBreiteIdent(),
					AnfragepositionFac.FLR_ANFRAGEPOSITION_FLRARTIKEL + ".c_nr");

			if (bReferenznummerInPositionen) {
				columns.add("lp.referenznummer", String.class, getTextRespectUISpr("lp.referenznummer", mandant, locUi),
						QueryParameters.FLR_BREITE_XM,
						AnfragepositionFac.FLR_ANFRAGEPOSITION_FLRARTIKEL + ".c_referenznr");
			}

			columns.add("bes.menge", super.getUIClassBigDecimalNachkommastellen(iNachkommastellenMenge),
					getTextRespectUISpr("bes.menge", mandant, locUi), QueryParameters.FLR_BREITE_M,
					BestellpositionFac.FLR_BESTELLPOSITION_N_MENGE);
			columns.add("lp.einheit", String.class, getTextRespectUISpr("lp.einheit", mandant, locUi),
					QueryParameters.FLR_BREITE_M, Facade.NICHT_SORTIERBAR);

			columns.add("bes.bezeichnung", String.class, getTextRespectUISpr("bes.bezeichnung", mandant, locUi),
					QueryParameters.FLR_BREITE_SHARE_WITH_REST, BestellpositionFac.FLR_BESTELLPOSITION_C_BEZEICHNUNG);
			columns.add("lp.preis", super.getUIClassBigDecimalNachkommastellen(iNachkommastellenPreis),
					getTextRespectUISpr("lp.preis", mandant, locUi), QueryParameters.FLR_BREITE_M,
					BestellpositionFac.FLR_BESTELLPOSITION_N_NETTOGESAMTPREIS);

			columns.add("er.whg", String.class, getTextRespectUISpr("er.whg", mandant, locUi),
					QueryParameters.FLR_BREITE_WAEHRUNG, Facade.NICHT_SORTIERBAR);

			columns.add("bes.postermin", Date.class, getTextRespectUISpr("bes.postermin", mandant, locUi),
					QueryParameters.FLR_BREITE_M, BestellpositionFac.FLR_BESTELLPOSITION_T_UEBERSTEUERTERLIEFERTERMIN);

			columns.add("lp.kw", String.class, getTextRespectUISpr("lp.kw", mandant, locUi),
					QueryParameters.FLR_BREITE_XS, Facade.NICHT_SORTIERBAR);

			columns.add("bes.termin", Date.class, getTextRespectUISpr("bes.termin", mandant, locUi),
					QueryParameters.FLR_BREITE_M, BestellpositionFac.FLR_BESTELLPOSITION_T_AUFTRAGSBESTAETIGUNGSTERMIN);

			columns.add("lp.kw2", String.class, getTextRespectUISpr("lp.kw", mandant, locUi),
					QueryParameters.FLR_BREITE_XS, Facade.NICHT_SORTIERBAR);

			columns.add("bes.mahnstufe", Integer.class, getTextRespectUISpr("bes.mahnstufe", mandant, locUi),
					QueryParameters.FLR_BREITE_WAEHRUNG, "flrbestellung.mahnstufe_i_id");

			columns.add("lp.mahndatum", Date.class, getTextRespectUISpr("lp.mahndatum", mandant, locUi),
					QueryParameters.FLR_BREITE_M, "bsmahnlauf_i_id");

			columns.add("bes.lieferterminbest", Boolean.class,
					getTextRespectUISpr("bes.lieferterminbest", mandant, locUi), QueryParameters.FLR_BREITE_XS,
					"t_lieferterminbestaetigt", getTextRespectUISpr("bes.lieferterminbestaetigt.tooltip",
							theClientDto.getMandant(), theClientDto.getLocUi()));

			if (bZentralerArtikelstamm == true) {
				columns.add("bes.unterwegs", super.getUIClassBigDecimalNachkommastellen(iNachkommastellenMenge),
						getTextRespectUISpr("bes.unterwegs", mandant, locUi), QueryParameters.FLR_BREITE_M,
						Facade.NICHT_SORTIERBAR);
			}

		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		return columns;

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

	/**
	 * sorts the data of the current query using the specified criterias and returns
	 * the page of data where the row of selectedId is contained.
	 * 
	 * @param sortierKriterien the new sort criterias.
	 * @param selectedId       the id of the entity that should be included in the
	 *                         result page.
	 * @return the sorted data containing the page where the entity with the
	 *         specified id is located.
	 * @throws EJBExceptionLP
	 */
	public QueryResult sort(SortierKriterium[] sortierKriterien, Object selectedId) throws EJBExceptionLP {

		this.getQuery().setSortKrit(sortierKriterien);

		QueryResult result = null;
		int rowNumber = 0;

		if (selectedId != null && ((Integer) selectedId).intValue() >= 0) {
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();
				String queryString = "select position." + "i_id" + " from FLRBestellungAuftragsbestaetigung position "
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

		if (rowNumber < 0 || rowNumber >= this.getRowCount()) {
			rowNumber = 0;
		}

		result = this.getPageAt(new Integer(rowNumber));
		result.setIndexOfSelectedRow(rowNumber);

		return result;
	}
}
