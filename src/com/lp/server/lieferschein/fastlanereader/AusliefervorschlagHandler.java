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
package com.lp.server.lieferschein.fastlanereader;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.Icon;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebot.service.AngebotpositionDto;
import com.lp.server.artikel.service.SperrenIcon;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.forecast.service.ForecastDto;
import com.lp.server.lieferschein.fastlanereader.generated.FLRAusliefervorschlag;
import com.lp.server.lieferschein.service.AusliefervorschlagFac;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.util.Facade;
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

public class AusliefervorschlagHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String FLR_AUSLIEFERVORSCHLAG = "flrausliefervorschlag.";
	public static final String FLR_AUSLIEFERVORSCHLAG_FROM_CLAUSE = " from FLRAusliefervorschlag flrausliefervorschlag ";

	boolean bZentralerArtikelstamm = false;

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
			int pageSize = 99999;
			int startIndex = Math.max(rowIndex.intValue() - (pageSize / 2), 0);
			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			session = setFilter(session);
			String queryString = this.getFromClause() + this.buildWhereClause() + this.buildOrderByClause();

			Query query = session.createQuery(queryString);

			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			String[] tooltipData = new String[resultList.size()];
			int row = 0;

			while (resultListIterator.hasNext()) {

				Object[] o = (Object[]) resultListIterator.next();
				FLRAusliefervorschlag ausliefervorschlag = (FLRAusliefervorschlag) o[0];

				rows[row][getTableColumnInformation().getViewIndex("i_id")] = ausliefervorschlag.getI_id();

				if (o[1] != null) {
					rows[row][getTableColumnInformation().getViewIndex("S")] = o[1];
				}
				String artikelspr = null;
				if (o[2] != null) {
					artikelspr = ((com.lp.server.artikel.fastlanereader.generated.FLRArtikellistespr) o[2]).getC_bez();
				}

				rows[row][getTableColumnInformation().getViewIndex("artikel.artikelnummerlang")] = ausliefervorschlag
						.getFlrartikelliste().getC_nr();

				if (ausliefervorschlag.getN_verfuegbar().doubleValue() <= 0 && Helper
						.short2boolean(ausliefervorschlag.getFlrartikel().getB_lagerbewirtschaftet()) == true) {
					rows[row][getTableColumnInformation().getViewIndex("Color")] = Color.RED;
				} else if (ausliefervorschlag.getN_verfuegbar().doubleValue() < ausliefervorschlag.getN_menge()
						.doubleValue()) {
					rows[row][getTableColumnInformation().getViewIndex("Color")] = Color.ORANGE;
				}

				// belegartId
				if (ausliefervorschlag.getI_belegartid() != null) {
					// Los
					if (ausliefervorschlag.getBelegart_c_nr().equals(LocaleFac.BELEGART_LOS)) {

						rows[row][getTableColumnInformation().getViewIndex("lp.bezeichnung")] = artikelspr;
					}
					// Auftrag
					else if (ausliefervorschlag.getBelegart_c_nr().equals(LocaleFac.BELEGART_AUFTRAG)) {

						AuftragpositionDto auftragposDto = null;

						auftragposDto = this.getAuftragpositionFac()
								.auftragpositionFindByPrimaryKeyOhneExc(ausliefervorschlag.getI_belegartpositionid());
						if (auftragposDto != null && auftragposDto.getCBez() != null) {

							rows[row][getTableColumnInformation()
									.getViewIndex("lp.bezeichnung")] = auftragposDto.getCBez() == null ? null
											: auftragposDto.getCBez();
						} else if (artikelspr != null) {
							rows[row][getTableColumnInformation().getViewIndex("lp.bezeichnung")] = artikelspr;
						}

					} else if (ausliefervorschlag.getBelegart_c_nr().equals(LocaleFac.BELEGART_ANGEBOT)) {

						if (ausliefervorschlag.getI_belegartpositionid() != null) {
							AngebotpositionDto angebotposDto = null;

							angebotposDto = this.getAngebotpositionFac().angebotpositionFindByPrimaryKeyOhneExc(
									ausliefervorschlag.getI_belegartpositionid());
							if (angebotposDto != null && angebotposDto.getCBez() != null) {

								rows[row][getTableColumnInformation()
										.getViewIndex("lp.bezeichnung")] = angebotposDto.getCBez() == null ? null
												: angebotposDto.getCBez();
							} else if (artikelspr != null) {
								rows[row][getTableColumnInformation().getViewIndex("lp.bezeichnung")] = artikelspr;
							}
						}

					}
					// Bestellung
					else if (ausliefervorschlag.getBelegart_c_nr().equals(LocaleFac.BELEGART_BESTELLUNG)) {

						BestellpositionDto bestellpositionDto = null;

						bestellpositionDto = this.getBestellpositionFac()
								.bestellpositionFindByPrimaryKeyOhneExc(ausliefervorschlag.getI_belegartid());

						if (bestellpositionDto != null && bestellpositionDto.getCBez() != null) {
							rows[row][getTableColumnInformation()
									.getViewIndex("lp.bezeichnung")] = bestellpositionDto.getCBez() == null ? null
											: bestellpositionDto.getCBez();
						} else if (artikelspr != null) {
							rows[row][getTableColumnInformation().getViewIndex("lp.bezeichnung")] = artikelspr;
						}
					} else if (ausliefervorschlag.getBelegart_c_nr().equals(LocaleFac.BELEGART_FORECAST)) {

						rows[row][getTableColumnInformation().getViewIndex("lp.bezeichnung")] = artikelspr;
					}
				} else {
					if (artikelspr != null) {
						rows[row][getTableColumnInformation().getViewIndex("lp.bezeichnung")] = artikelspr;
					}
				}
				if (bZentralerArtikelstamm) {
					rows[row][getTableColumnInformation().getViewIndex("report.mandant")] = ausliefervorschlag
							.getMandant_c_nr();
				}

				rows[row][getTableColumnInformation().getViewIndex("ls.ausliefermenge")] = ausliefervorschlag
						.getN_menge();
				rows[row][getTableColumnInformation().getViewIndex("ls.ausliefertermin")] = ausliefervorschlag
						.getT_ausliefertermin();

				rows[row][getTableColumnInformation().getViewIndex("lp.kunde")] = HelperServer
						.formatNameAusFLRPartner(ausliefervorschlag.getFlrkunde().getFlrpartner());
				rows[row][getTableColumnInformation().getViewIndex("lp.lieferadresse")] = HelperServer
						.formatNameAusFLRPartner(ausliefervorschlag.getFlrkunde_lieferadresse().getFlrpartner());

				rows[row][getTableColumnInformation().getViewIndex("lp.belegart")] = ausliefervorschlag
						.getBelegart_c_nr();

				BestellungDto bestellungDto = null;
				BestellpositionDto besposDto = null;
				AuftragDto auftragDto = null;

				if (ausliefervorschlag.getI_belegartid() != null) {
					if (ausliefervorschlag.getBelegart_c_nr().equals(LocaleFac.BELEGART_BESTELLUNG)) {
						besposDto = getBestellpositionFac()
								.bestellpositionFindByPrimaryKeyOhneExc(ausliefervorschlag.getI_belegartid());

						if (besposDto != null) {
							bestellungDto = getBestellungFac().bestellungFindByPrimaryKey(besposDto.getBestellungIId());

							rows[row][getTableColumnInformation().getViewIndex(
									"lp.belegnummer")] = bestellungDto.getCNr() == null ? null : bestellungDto.getCNr();
						} else {
							rows[row][getTableColumnInformation()
									.getViewIndex("lp.belegnummer")] = "Position gel\u00F6scht";
						}
					} else if (ausliefervorschlag.getBelegart_c_nr().equals(LocaleFac.BELEGART_AUFTRAG)) {
						auftragDto = getAuftragFac().auftragFindByPrimaryKey(ausliefervorschlag.getI_belegartid());
						rows[row][getTableColumnInformation().getViewIndex(
								"lp.belegnummer")] = auftragDto.getCNr() == null ? null : auftragDto.getCNr();
					} else if (ausliefervorschlag.getBelegart_c_nr().equals(LocaleFac.BELEGART_ANGEBOT)) {
						AngebotDto angebotDto = getAngebotFac()
								.angebotFindByPrimaryKey(ausliefervorschlag.getI_belegartid(), theClientDto);
						rows[row][getTableColumnInformation().getViewIndex("lp.belegnummer")] = angebotDto.getCNr();
					}
					// Los
					else if (ausliefervorschlag.getBelegart_c_nr().equals(LocaleFac.BELEGART_LOS)) {
						LosDto losDto = getFertigungFac().losFindByPrimaryKey(ausliefervorschlag.getI_belegartid());
						rows[row][getTableColumnInformation().getViewIndex("lp.belegnummer")] = losDto.getCNr();
					}
					// Forecastz
					else if (ausliefervorschlag.getBelegart_c_nr().equals(LocaleFac.BELEGART_FORECAST)) {
						ForecastDto fDto = getForecastFac()
								.forecastFindByPrimaryKeyOhneExc(ausliefervorschlag.getI_belegartid());
						rows[row][getTableColumnInformation().getViewIndex("lp.belegnummer")] = fDto.getCNr();
					}

				}

				row++;

			}
			result = new QueryResult(rows, this.getRowCount(), startIndex, endIndex, 0, tooltipData);
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
			String queryString = "select count(distinct ausliefervorschlag.i_id) from FLRAusliefervorschlag ausliefervorschlag  LEFT OUTER JOIN ausliefervorschlag.flrartikelliste.artikelsprset AS aspr  "
					+ this.buildWhereClause();

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

	// /**
	// * gets the total number of rows represented by the current query.
	// *
	// * @see UseCaseHandler#getRowCountFromDataBase()
	// * @return int
	// */
	// protected int getRowCountFromDataBase() {
	// int rowCount = 0;
	// SessionFactory factory = FLRSessionFactory.getFactory();
	// Session session = null;
	// try {
	// session = factory.openSession();
	// String queryString = "select count(*) " + this.getFromClause()
	// + this.buildWhereClause();
	// myLogger.logData("getRowCountFromDataBase(), HQL Query = " +
	// queryString);
	// Query query = session.createQuery(queryString);
	// List rowCountResult = query.list();
	// if (rowCountResult != null && rowCountResult.size() > 0) {
	// rowCount = ( (Integer) rowCountResult.get(0)).intValue();
	// }
	// }
	// catch (Exception e) {
	// e.printStackTrace();
	// }
	// finally {
	// closeSession(session);
	// }
	// return rowCount;
	// }

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

					if (filterKriterien[i].kritName.equals("c_volltext")) {
						String suchstring = "lower(coalesce(aspr.c_bez,'')||' '||coalesce(aspr.c_kbez,'')||' '||coalesce(aspr.c_zbez,'')||' '||coalesce(aspr.c_zbez2,''))";

						String[] teile = filterKriterien[i].value.toLowerCase().split(" ");
						where.append("(");

						for (int p = 0; p < teile.length; p++) {

							if (teile[p].startsWith("-")) {
								where.append(" NOT ");

								teile[p] = teile[p].substring(1);

							}

							where.append("lower(" + suchstring + ") like '%" + teile[p].toLowerCase() + "%'");
							if (p < teile.length - 1) {
								where.append(" AND ");
							}
						}

						where.append(")");
					} else if (filterKriterien[i].kritName
							.equals(AusliefervorschlagFac.AUSLIEFERVORSCHLAGHANDLER_VERFUEGBARKEITEN)) {

						if (filterKriterien[i].value != null) {

							if (filterKriterien[i].value
									.equals(AusliefervorschlagFac.AUSLIEFERVORSCHLAGHANDLER_FILTER_NICHT_VERFUEGBAR)) {

								where.append(
										" ausliefervorschlag.n_verfuegbar <=0 AND ausliefervorschlag.flrartikel.b_lagerbewirtschaftet = 1 ");
							} else if (filterKriterien[i].value.equals(
									AusliefervorschlagFac.AUSLIEFERVORSCHLAGHANDLER_FILTER_TEILMENGE_VERFUEGBAR)) {

								where.append(" ausliefervorschlag.n_verfuegbar >0 AND  ausliefervorschlag.n_verfuegbar < ausliefervorschlag.n_menge ");
							} else if (filterKriterien[i].value.equals(
									AusliefervorschlagFac.AUSLIEFERVORSCHLAGHANDLER_FILTER_AUSREICHEND_VERFUEGBAR)) {

								where.append(" ausliefervorschlag.n_verfuegbar >= ausliefervorschlag.n_menge ");
							} else {
								where.append(" 1=1 ");
							}

						}

					} else {

						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" lower(ausliefervorschlag." + filterKriterien[i].kritName + ")");
						} else {
							where.append(" ausliefervorschlag." + filterKriterien[i].kritName);
						}
						where.append(" " + filterKriterien[i].operator);
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" " + filterKriterien[i].value.toLowerCase());
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
					if (!kriterien[i].kritName.endsWith(Facade.NICHT_SORTIERBAR)) {
						if (kriterien[i].isKrit) {
							if (sortAdded) {
								orderBy.append(", ");
							}
							sortAdded = true;

							// spezial unterscheidung nur in diesem haendler
							if (kriterien[i].kritName.toString().equals("c_nr")) {
								orderBy.append("ausliefervorschlag." + "flrartikel.c_nr");
								orderBy.append(" ");
								orderBy.append(kriterien[i].value);

							}

							else if (kriterien[i].kritName.toString().equals("c_bez")) {
								orderBy.append("ausliefervorschlag." + "flrartikel.c_nr");
								orderBy.append(" ");
								orderBy.append(kriterien[i].value);
							} else {
								orderBy.append("ausliefervorschlag." + kriterien[i].kritName);
								orderBy.append(" ");
								orderBy.append(kriterien[i].value);

							}
						}
					}
				}
			} else {
				// no sort criteria found, add default sort
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(
						"ausliefervorschlag.flrartikel.c_nr ASC, ausliefervorschlag.t_ausliefertermin ASC, ausliefervorschlag.belegart_c_nr DESC, ausliefervorschlag.i_belegartid ASC");
				sortAdded = true;
			}
			if (orderBy.indexOf("ausliefervorschlag." + "i_id") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" ausliefervorschlag.").append("i_id").append(" ");
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
		return "SELECT ausliefervorschlag,  (SELECT max(s.flrsperren.c_bez) FROM FLRArtikelsperren as s WHERE s.artikel_i_id=ausliefervorschlag.artikel_i_id AND ( s.flrsperren.b_gesperrteinkauf=1 OR s.flrsperren.b_gesperrt=1) ) as sperren, aspr from FLRAusliefervorschlag ausliefervorschlag LEFT OUTER JOIN ausliefervorschlag.flrartikelliste.artikelsprset AS aspr ";
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

		// if (selectedId != null && ( (Integer) selectedId).intValue() >= 0) {
		if (selectedId != null) {
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();
				String queryString = "select ausliefervorschlag." + "i_id"
						+ " from FLRAusliefervorschlag ausliefervorschlag  LEFT OUTER JOIN ausliefervorschlag.flrartikelliste.artikelsprset AS aspr "
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
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
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

	/**
	 * gets information about the Bestellungstable.
	 * 
	 * @return TableInfo
	 */

	private TableColumnInformation createColumnInformation(String mandant, Locale locUi) {
		TableColumnInformation columns = new TableColumnInformation();

		columns.add("i_id", Integer.class, "i_id", -1, "i_id");
		columns.add("S", SperrenIcon.class, getTextRespectUISpr("ls.sperre", mandant, locUi),
				QueryParameters.FLR_BREITE_S, Facade.NICHT_SORTIERBAR,
				getTextRespectUISpr("ls.sperre.tooltip", mandant, locUi));

		columns.add("artikel.artikelnummerlang", String.class,
				getTextRespectUISpr("artikel.artikelnummerlang", mandant, locUi), QueryParameters.FLR_BREITE_XM,
				"c_nr");
		columns.add("lp.bezeichnung", String.class, getTextRespectUISpr("lp.bezeichnung", mandant, locUi),
				QueryParameters.FLR_BREITE_XM, "c_bez");

		if (bZentralerArtikelstamm) {

			columns.add("report.mandant", String.class, getTextRespectUISpr("report.mandant", mandant, locUi),
					QueryParameters.FLR_BREITE_XS, Facade.NICHT_SORTIERBAR);
		}

		columns.add("ls.ausliefermenge", BigDecimal.class, getTextRespectUISpr("ls.ausliefermenge", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, "n_menge");

		columns.add("ls.ausliefertermin", Date.class, getTextRespectUISpr("ls.ausliefertermin", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, "t_ausliefertermin");

		columns.add("lp.kunde", String.class, getTextRespectUISpr("lp.kunde", mandant, locUi),
				QueryParameters.FLR_BREITE_M,
				"flrkunde.flrpartner.c_name1nachnamefirmazeile1, ausliefervorschlag.flrkunde.flrpartner.c_name2vornamefirmazeile2");
		columns.add("lp.lieferadresse", String.class, getTextRespectUISpr("lsch.lieferadreesse", mandant, locUi),
				QueryParameters.FLR_BREITE_M,
				"flrkunde_lieferadresse.flrpartner.c_name1nachnamefirmazeile1, ausliefervorschlag.flrkunde_lieferadresse.flrpartner.c_name2vornamefirmazeile2");

		columns.add("lp.belegart", String.class, getTextRespectUISpr("lp.belegart", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, "belegart_c_nr");

		columns.add("lp.belegnummer", String.class, getTextRespectUISpr("lp.belegnummer", mandant, locUi),
				QueryParameters.FLR_BREITE_M, Facade.NICHT_SORTIERBAR);

		columns.add("Color", Color.class, "", 1, "");

		return columns;
	}

	public TableInfo getTableInfo() {

		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
				theClientDto)) {
			bZentralerArtikelstamm = true;
		}

		TableInfo info = super.getTableInfo();
		if (info != null)
			return info;

		setTableColumnInformation(createColumnInformation(theClientDto.getMandant(), theClientDto.getLocUi()));

		TableColumnInformation c = getTableColumnInformation();
		info = new TableInfo(c.getClasses(), c.getHeaderNames(), c.getWidths(), c.getDbColumNames());
		setTableInfo(info);

		return info;
	}

}
