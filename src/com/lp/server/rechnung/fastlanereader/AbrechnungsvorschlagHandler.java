package com.lp.server.rechnung.fastlanereader;

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

import java.awt.Color;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.Icon;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.rechnung.fastlanereader.generated.FLRAbrechnungsvorschlag;
import com.lp.server.rechnung.service.AbrechnungsvorschlagFac;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.VersandFac;
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
import com.lp.util.BigDecimal3;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.Pair;

public class AbrechnungsvorschlagHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
			int pageSize = AbrechnungsvorschlagHandler.PAGE_SIZE;
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

				FLRAbrechnungsvorschlag av = (FLRAbrechnungsvorschlag) o[0];

				java.util.Date zuletztUnterschrienen = (java.util.Date) o[1];

				rows[row][getTableColumnInformation().getViewIndex("i_id")] = av.getI_id();

				Boolean bInkonsistet = false;

				String art = AbrechnungsvorschlagFac.ART_ZEITDATEN;

				if (av.getZeitdaten_i_id() != null) {
					art = AbrechnungsvorschlagFac.ART_ZEITDATEN;

					if (!av.getF_verrechenbar().equals(av.getFlrzeitdaten().getF_verrechenbar())) {
						bInkonsistet = true;
					}

				}

				if (av.getAuftragszuordnung_i_id() != null) {
					art = AbrechnungsvorschlagFac.ART_ER;

					if (!av.getF_verrechenbar().equals(av.getFlrauftragszuordnung().getF_verrechenbar())) {
						bInkonsistet = true;
					}

				} else if (av.getTelefonzeiten_i_id() != null) {
					art = AbrechnungsvorschlagFac.ART_TELEFON;

					if (!av.getF_verrechenbar().equals(av.getFlrtelefonzeiten().getF_verrechenbar())) {
						bInkonsistet = true;
					}

				}
				if (av.getReise_i_id() != null) {
					art = AbrechnungsvorschlagFac.ART_REISE;

					if (!av.getF_verrechenbar().equals(av.getFlrreise().getF_verrechenbar())) {
						bInkonsistet = true;
					}
				}
				if (av.getMaschinenzeitdaten_i_id() != null) {
					art = AbrechnungsvorschlagFac.ART_MASCHINE;

					if (!av.getF_verrechenbar().equals(av.getFlrmaschinenzeitdaten().getF_verrechenbar())) {
						bInkonsistet = true;
					}
				}

				rows[row][getTableColumnInformation().getViewIndex("lp.art")] = art;
				if (av.getFlrpersonal() != null) {
					rows[row][getTableColumnInformation().getViewIndex("lp.person")] = av.getFlrpersonal()
							.getC_kurzzeichen();
				}
				if (av.getFlrkunde() != null) {
					rows[row][getTableColumnInformation().getViewIndex("lp.kunde")] = HelperServer
							.formatNameAusFLRPartner(av.getFlrkunde().getFlrpartner());
				}
				if (av.getFlrauftrag() != null) {
					rows[row][getTableColumnInformation().getViewIndex("auft.auftrag")] = av.getFlrauftrag().getC_nr();
					rows[row][getTableColumnInformation().getViewIndex("auft.status")] = getStatusMitUebersetzung(
							av.getFlrauftrag().getAuftragstatus_c_nr());
				}

				rows[row][getTableColumnInformation().getViewIndex("rech.abrechnungsvorschlag.rechnungswaehrung")] = av
						.getWaehrung_c_nr_rechnung();

				if (av.getFlrlos() != null) {
					rows[row][getTableColumnInformation().getViewIndex("fert.tab.unten.los.title")] = av.getFlrlos()
							.getC_nr();
					rows[row][getTableColumnInformation().getViewIndex("los.status")] = getStatusMitUebersetzung(
							av.getFlrlos().getStatus_c_nr());

				}

				// SP8452
				if (av.getFlrzeitdaten() != null) {

					if (av.getFlrzeitdaten().getI_belegartpositionid() != null
							&& av.getFlrzeitdaten().getC_belegartnr() != null) {

						if (av.getFlrzeitdaten().getC_belegartnr().equals(LocaleFac.BELEGART_AUFTRAG)) {
							AuftragpositionDto apDto = getAuftragpositionFac().auftragpositionFindByPrimaryKeyOhneExc(
									av.getFlrzeitdaten().getI_belegartpositionid());

							if (apDto != null && apDto.getArtikelIId() != null) {
								ArtikelDto artikelDtoABPos = getArtikelFac()
										.artikelFindByPrimaryKeySmall(apDto.getArtikelIId(), theClientDto);
								if (apDto.getCBez() != null && !artikelDtoABPos.getArtikelartCNr()
										.equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {

									// Uebst
									String s = artikelDtoABPos.getCNr() + " " + apDto.getCBez();

									if (apDto.getCZusatzbez() != null) {
										s += " " + apDto.getCZusatzbez();
									}

									rows[row][getTableColumnInformation().getViewIndex("lp.position")] = s;

								} else if (apDto.getCBez() == null && !artikelDtoABPos.getArtikelartCNr()
										.equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {

									rows[row][getTableColumnInformation().getViewIndex("lp.position")] = artikelDtoABPos
											.formatArtikelbezeichnung();
								}
							}

						}

					}

					rows[row][getTableColumnInformation().getViewIndex("lp.bemerkung")] = av.getFlrzeitdaten()
							.getC_bemerkungzubelegart();

				}

				if (av.getFlrprojekt() != null) {
					rows[row][getTableColumnInformation().getViewIndex("proj.projekt")] = av.getFlrprojekt().getC_nr();
					rows[row][getTableColumnInformation().getViewIndex("proj.status")] = getStatusMitUebersetzung(
							av.getFlrprojekt().getStatus_c_nr());

				}
				if (av.getFlrzeitdaten() != null) {
					if (av.getFlrzeitdaten().getFlrartikel() != null) {
						rows[row][getTableColumnInformation().getViewIndex("artikel.artikelnummerlang")] = av
								.getFlrzeitdaten().getFlrartikel().getC_nr();
					}

				}

				rows[row][getTableColumnInformation().getViewIndex("lp.von")] = av.getT_von();
				rows[row][getTableColumnInformation().getViewIndex("lp.bis")] = av.getT_bis();

				rows[row][getTableColumnInformation().getViewIndex("lp.verrechenbar")] = av.getF_verrechenbar();

				rows[row][getTableColumnInformation().getViewIndex("lp.stunden.offen")] = av.getN_stunden_offen();

				rows[row][getTableColumnInformation().getViewIndex("lp.betrag.offen")] = av.getN_betrag_offen();

				rows[row][getTableColumnInformation()
						.getViewIndex("pers.anwesenheitsbestaetigung.unterschrieben")] = zuletztUnterschrienen;

				rows[row][getTableColumnInformation().getViewIndex("lp.stunden.gesamt")] = av.getN_stunden_gesamt();

				rows[row][getTableColumnInformation().getViewIndex("lp.betrag.gesamt")] = av.getN_betrag_gesamt();
				rows[row][getTableColumnInformation().getViewIndex("lp.betrag.verrechenbar")] = av
						.getN_betrag_verrechenbar();
				rows[row][getTableColumnInformation().getViewIndex("lp.stunden.verrechenbar")] = av
						.getN_stunden_verrechenbar();

				rows[row][getTableColumnInformation().getViewIndex("lp.km.offen")] = av.getN_kilometer_offen();

				rows[row][getTableColumnInformation().getViewIndex("lp.km.gesamt")] = av.getN_kilometer_gesamt();
				rows[row][getTableColumnInformation().getViewIndex("lp.km.verrechenbar")] = av
						.getN_kilometer_verrechenbar();

				rows[row][getTableColumnInformation().getViewIndex("lp.spesen.offen")] = av.getN_spesen_offen();

				rows[row][getTableColumnInformation().getViewIndex("lp.spesen.gesamt")] = av.getN_spesen_gesamt();
				rows[row][getTableColumnInformation().getViewIndex("lp.spesen.verrechenbar")] = av
						.getN_spesen_verrechenbar();

				if (bInkonsistet) {
					rows[row][getTableColumnInformation().getViewIndex(
							"rech.abrechnungsvorschlag.verrechenbar.inkonsistet")] = VersandFac.STATUS_DATEN_UNGUELTIG;
				}
				if (Helper.short2boolean(av.getB_verrechnet())) {
					rows[row][getTableColumnInformation().getViewIndex("Color")] = Color.LIGHT_GRAY;
				}

				row++;

			}
			result = new QueryResult(rows, this.getRowCount(), startIndex, endIndex, 0, tooltipData);
		} catch (Exception e) {
			e.printStackTrace();
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
			String queryString = "select count(distinct av.i_id) from FLRAbrechnungsvorschlag av  LEFT OUTER JOIN av.flrkunde as flrkunde   LEFT JOIN av.flrkunde.flrpartner as flrpartner   LEFT JOIN av.flrauftrag as flrauftrag   LEFT JOIN av.flrlos as flrlos   LEFT JOIN av.flrlos as flrlos LEFT JOIN av.flrprojekt as flrprojekt LEFT JOIN av.flrzeitdaten as flrzeitdaten "
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

					if (filterKriterien[i].kritName.equals("ART")) {

						if (filterKriterien[i].value.equals(AbrechnungsvorschlagFac.ART_ZEITDATEN)) {
							where.append(" av.zeitdaten_i_id IS NOT NULL");
						} else if (filterKriterien[i].value.equals(AbrechnungsvorschlagFac.ART_ER)) {
							where.append(" av.auftragszuordnung_i_id IS NOT NULL");
						} else if (filterKriterien[i].value.equals(AbrechnungsvorschlagFac.ART_MASCHINE)) {
							where.append(" av.maschinenzeitdaten_i_id IS NOT NULL");
						} else if (filterKriterien[i].value.equals(AbrechnungsvorschlagFac.ART_TELEFON)) {
							where.append(" av.telefonzeiten_i_id IS NOT NULL");
						} else if (filterKriterien[i].value.equals(AbrechnungsvorschlagFac.ART_REISE)) {
							where.append(" av.reise_i_id IS NOT NULL");
						}

					} else if (filterKriterien[i].kritName.equals("flrauftrag.c_nr")) {
						try {
							String sValue = super.buildWhereBelegnummer(filterKriterien[i], false,
									ParameterFac.KATEGORIE_AUFTRAG,
									ParameterFac.PARAMETER_AUFTRAG_BELEGNUMMERSTARTWERT);
							// Belegnummernsuche auch in "altem" Jahr, wenn im
							// neuen noch keines vorhanden ist
							if (!istBelegnummernInJahr("FLRAuftrag", sValue)) {
								sValue = super.buildWhereBelegnummer(filterKriterien[i], true,
										ParameterFac.KATEGORIE_AUFTRAG,
										ParameterFac.PARAMETER_AUFTRAG_BELEGNUMMERSTARTWERT);
							}
							where.append(" " + "av." + filterKriterien[i].kritName);
							where.append(" " + filterKriterien[i].operator);
							where.append(" " + sValue);
						} catch (Exception ex) {
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, ex);
						}
					} else {

						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" lower(av." + filterKriterien[i].kritName + ")");
						} else {
							where.append(" av." + filterKriterien[i].kritName);
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

							orderBy.append("" + kriterien[i].kritName);
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
				orderBy.append("av.t_anlegen").append(" ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("av." + "i_id") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" av.").append("i_id").append(" ");
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
		return "SELECT av, (SELECT MAX(ab.t_unterschrift) FROM FLRAnwesenheitsbestaetigung ab WHERE ab.t_unterschrift <=flrzeitdaten.t_aendern AND ab.personal_i_id=av.personal_i_id AND ab.auftrag_i_id=av.auftrag_i_id GROUP BY ab.auftrag_i_id) FROM FLRAbrechnungsvorschlag av LEFT OUTER JOIN av.flrkunde as flrkunde   LEFT JOIN av.flrkunde.flrpartner as flrpartner  LEFT JOIN av.flrauftrag as flrauftrag   LEFT JOIN av.flrlos as flrlos LEFT JOIN av.flrprojekt as flrprojekt LEFT JOIN av.flrzeitdaten as flrzeitdaten  ";
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
				String queryString = "select av." + "i_id"
						+ " from FLRAbrechnungsvorschlag av  LEFT JOIN av.flrkunde as flrkunde  LEFT JOIN av.flrkunde.flrpartner as flrpartner  LEFT JOIN av.flrauftrag as flrauftrag   LEFT JOIN av.flrlos as flrlos   LEFT JOIN av.flrlos as flrlos LEFT JOIN av.flrprojekt as flrprojekt LEFT JOIN av.flrzeitdaten as flrzeitdaten  "
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

		// ART
		columns.add("lp.art", String.class, getTextRespectUISpr("lp.art", mandant, locUi),
				QueryParameters.FLR_BREITE_XS, Facade.NICHT_SORTIERBAR);

		// Person
		columns.add("lp.person", String.class, getTextRespectUISpr("lp.person", mandant, locUi),
				QueryParameters.FLR_BREITE_XM, "av.flrpersonal.c_kurzzeichen");
		// Kunde
		columns.add("lp.kunde", String.class, getTextRespectUISpr("lp.kunde", mandant, locUi),
				QueryParameters.FLR_BREITE_M, "flrpartner.c_name1nachnamefirmazeile1");
		// Projekt
		columns.add("proj.projekt", String.class, getTextRespectUISpr("proj.projekt", mandant, locUi),
				QueryParameters.FLR_BREITE_XM, "flrprojekt.c_nr");
		// Status
		columns.add("proj.status", Icon.class, getTextRespectUISpr("lp.status", mandant, locUi),
				QueryParameters.FLR_BREITE_XS, "flrprojekt.status_c_nr");
		// Auftrag
		columns.add("auft.auftrag", String.class, getTextRespectUISpr("auft.auftrag", mandant, locUi),
				QueryParameters.FLR_BREITE_M, "flrauftrag.c_nr");
		columns.add("rech.abrechnungsvorschlag.rechnungswaehrung", String.class,
				getTextRespectUISpr("rech.abrechnungsvorschlag.rechnungswaehrung", mandant, locUi),
				QueryParameters.FLR_BREITE_M, "av.waehrung_c_nr_rechnung");
		// Status
		columns.add("auft.status", Icon.class, getTextRespectUISpr("lp.status", mandant, locUi),
				QueryParameters.FLR_BREITE_XS, "flrauftrag.auftragstatus_c_nr");
		// Los
		columns.add("fert.tab.unten.los.title", String.class,
				getTextRespectUISpr("fert.tab.unten.los.title", mandant, locUi), QueryParameters.FLR_BREITE_XM,
				"flrlos.c_nr");
		// Status
		columns.add("los.status", Icon.class, getTextRespectUISpr("lp.status", mandant, locUi),
				QueryParameters.FLR_BREITE_XS, "flrlos.status_c_nr");

		// Position
		columns.add("lp.position", String.class, getTextRespectUISpr("lp.position", mandant, locUi),
				QueryParameters.FLR_BREITE_XS, Facade.NICHT_SORTIERBAR);
		// Bemerkung
		columns.add("lp.bemerkung", String.class, getTextRespectUISpr("lp.bemerkung", mandant, locUi),
				QueryParameters.FLR_BREITE_XS, Facade.NICHT_SORTIERBAR);

		// Zeitpunkt / Belegdatum
		columns.add("lp.von", Timestamp.class, getTextRespectUISpr("lp.von", mandant, locUi),
				QueryParameters.FLR_BREITE_XM, "av.t_von");
		// Zeitpunkt bis
		columns.add("lp.bis", Timestamp.class, getTextRespectUISpr("lp.bis", mandant, locUi),
				QueryParameters.FLR_BREITE_XM, "av.t_bis");

		// Arikel
		columns.add("artikel.artikelnummerlang", String.class,
				getTextRespectUISpr("artikel.artikelnummerlang", mandant, locUi), QueryParameters.FLR_BREITE_XM,
				"flrzeitdaten.flrartikel.c_nr");

		// Verrechenbar
		columns.add("lp.verrechenbar", Double.class,
				getTextRespectUISpr("rech.abrechnungsvorschlag.verrechenbar", mandant, locUi),
				QueryParameters.FLR_BREITE_XM, "av.f_verrechenbar");

		// Verrechenbar inkonsistent
		columns.add("rech.abrechnungsvorschlag.verrechenbar.inkonsistet", Icon.class,
				getTextRespectUISpr("rech.abrechnungsvorschlag.verrechenbar.inkonsistet", mandant, locUi),
				QueryParameters.FLR_BREITE_S, Facade.NICHT_SORTIERBAR);

		// Stunden gesamt
		columns.add("lp.stunden.gesamt", BigDecimal3.class, getTextRespectUISpr("lp.stunden", mandant, locUi),
				QueryParameters.FLR_BREITE_XM, "av.n_stunden_gesamt");
		// Stunden verrechenbar
		columns.add("lp.stunden.verrechenbar", BigDecimal3.class,
				getTextRespectUISpr("rech.abrechnungsvorschlag.stundenverrechenbar", mandant, locUi),
				QueryParameters.FLR_BREITE_XM, "av.n_stunden_verrechenbar");

		// Stunden
		columns.add("lp.stunden.offen", BigDecimal3.class,
				getTextRespectUISpr("rech.abrechnungsvorschlag.offen", mandant, locUi), QueryParameters.FLR_BREITE_XM,
				"av.n_stunden_offen");
		// Betrag Gesamt
		columns.add("lp.betrag.gesamt", BigDecimal.class, getTextRespectUISpr("lp.betrag", mandant, locUi),
				QueryParameters.FLR_BREITE_XM, "av.n_betrag_gesamt");
		// Betrag verrechenbar
		columns.add("lp.betrag.verrechenbar", BigDecimal.class,
				getTextRespectUISpr("rech.abrechnungsvorschlag.betragverrechenbar", mandant, locUi),
				QueryParameters.FLR_BREITE_XM, "av.n_betrag_verrechenbar");

		// untwerschrieben
		columns.add("pers.anwesenheitsbestaetigung.unterschrieben", java.sql.Timestamp.class,
				getTextRespectUISpr("pers.anwesenheitsbestaetigung.unterschrieben", mandant, locUi),
				QueryParameters.FLR_BREITE_XM, Facade.NICHT_SORTIERBAR);

		// Betrag
		columns.add("lp.betrag.offen", BigDecimal.class,
				getTextRespectUISpr("rech.abrechnungsvorschlag.offen", mandant, locUi), QueryParameters.FLR_BREITE_XM,
				"av.n_betrag_offen");

		// Kilometer Gesamt
		columns.add("lp.km.gesamt", BigDecimal.class,
				getTextRespectUISpr("rech.abrechnungsvorschlag.km", mandant, locUi), QueryParameters.FLR_BREITE_XM,
				"av.n_kilometer_gesamt");
		// Kilometer verrechenbar
		columns.add("lp.km.verrechenbar", BigDecimal.class,
				getTextRespectUISpr("rech.abrechnungsvorschlag.betragverrechenbar", mandant, locUi),
				QueryParameters.FLR_BREITE_XM, "av.n_kilometer_verrechenbar");

		// Kilometer Offen
		columns.add("lp.km.offen", BigDecimal.class,
				getTextRespectUISpr("rech.abrechnungsvorschlag.offen", mandant, locUi), QueryParameters.FLR_BREITE_XM,
				"av.n_kilometer_offen");

		// Spesen Gesamt
		columns.add("lp.spesen.gesamt", BigDecimal.class,
				getTextRespectUISpr("rech.abrechnungsvorschlag.spesen", mandant, locUi), QueryParameters.FLR_BREITE_XM,
				"av.n_spesen_gesamt");
		// Spesen verrechenbar
		columns.add("lp.spesen.verrechenbar", BigDecimal.class,
				getTextRespectUISpr("rech.abrechnungsvorschlag.betragverrechenbar", mandant, locUi),
				QueryParameters.FLR_BREITE_XM, "av.n_spesen_verrechenbar");

		// Spesen Offen
		columns.add("lp.spesen.offen", BigDecimal.class,
				getTextRespectUISpr("rech.abrechnungsvorschlag.offen", mandant, locUi), QueryParameters.FLR_BREITE_XM,
				"av.n_spesen_offen");

		columns.add("Color", Color.class, "", 1, "");

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

	@Override
	public List<Pair<?, ?>> getInfoForSelectedIIds(TheClientDto theClientDto, List<Object> selectedIIds) {
		if (selectedIIds == null || selectedIIds.size() < 2) // es wurde einer
																// oder kein
																// Eintrag
																// ausgewaehlt
			return null;

		if (selectedIIds.size() > 1000) {
			// SP6906
			return null;
		}

		Session session = FLRSessionFactory.getFactory().openSession();
		Criteria crit = session.createCriteria(FLRAbrechnungsvorschlag.class);
		crit.add(Restrictions.in("i_id", selectedIIds.toArray()));
		crit.add(Restrictions.eq("b_verrechnet", (short) 0));

		@SuppressWarnings("unchecked")
		List<FLRAbrechnungsvorschlag> list = new ArrayList<FLRAbrechnungsvorschlag>(crit.list());

		BigDecimal betragVerrechenbar = BigDecimal.ZERO;
		BigDecimal stundenVerrechenbar = BigDecimal.ZERO;
		BigDecimal kilometerVerrechenbar = BigDecimal.ZERO;
		BigDecimal spesenVerrechenbar = BigDecimal.ZERO;
		for (FLRAbrechnungsvorschlag bd : list) {

			if (bd.getN_betrag_verrechenbar() != null) {
				betragVerrechenbar = betragVerrechenbar.add(bd.getN_betrag_offen());
			}

			if (bd.getN_stunden_verrechenbar() != null) {
				stundenVerrechenbar = stundenVerrechenbar.add(bd.getN_stunden_offen());
			}

			if (bd.getN_kilometer_verrechenbar() != null) {
				kilometerVerrechenbar = kilometerVerrechenbar.add(bd.getN_kilometer_verrechenbar());
			}

			if (bd.getN_spesen_verrechenbar() != null) {
				spesenVerrechenbar = spesenVerrechenbar.add(bd.getN_spesen_verrechenbar());
			}
		}

		String mandantCNr = theClientDto.getMandant();
		Locale locUI = theClientDto.getLocUi();
		List<Pair<?, ?>> pairs = new ArrayList<Pair<?, ?>>();

		if (betragVerrechenbar.doubleValue() > 0 && stundenVerrechenbar.doubleValue() > 0) {

			pairs.add(new Pair<String, Object>(
					getTextRespectUISpr("rech.abrechnungsvorschlag.ungueltigeselektion", mandantCNr, locUI),
					"XXXXXXXXXXXXXXXXXXXXXXX"));
		} else {

			if (betragVerrechenbar.doubleValue() > 0) {
				pairs.add(new Pair<String, Object>(
						getTextRespectUISpr("rech.abrechnungsvorschlag.betrag", mandantCNr, locUI),
						Helper.formatZahl(betragVerrechenbar, 2, locUI)));
			} else {
				pairs.add(new Pair<String, Object>(
						getTextRespectUISpr("rech.abrechnungsvorschlag.stunden", mandantCNr, locUI),
						Helper.formatZahl(stundenVerrechenbar, 3, locUI)));
			}

			if (kilometerVerrechenbar.doubleValue() > 0) {
				pairs.add(new Pair<String, Object>(
						getTextRespectUISpr("rech.abrechnungsvorschlag.kilometer", mandantCNr, locUI),
						Helper.formatZahl(kilometerVerrechenbar, 2, locUI)));
			}

			if (spesenVerrechenbar.doubleValue() > 0) {
				pairs.add(new Pair<String, Object>(
						getTextRespectUISpr("rech.abrechnungsvorschlag.verrspesen", mandantCNr, locUI),
						Helper.formatZahl(spesenVerrechenbar, 2, locUI)));
			}

		}

		return pairs;
	}

}
