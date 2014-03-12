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
package com.lp.server.bestellung.fastlanereader;

import java.awt.Color;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.swing.Icon;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebot.service.AngebotpositionDto;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikellieferant;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.bestellung.fastlanereader.generated.FLRBestellvorschlag;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellvorschlagDto;
import com.lp.server.bestellung.service.BestellvorschlagFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.system.service.LocaleFac;
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

public class BestellvorschlagHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String FLR_BESTELLVORSCHLAG = "flrbestellvorschlag.";
	public static final String FLR_BESTELLVORSCHLAG_FROM_CLAUSE = " from FLRBestellvorschlag flrbestellvorschlag ";

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
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = BestellvorschlagHandler.PAGE_SIZE;
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
			while (resultListIterator.hasNext()) {

				Object[] o = (Object[]) resultListIterator.next();
				FLRBestellvorschlag bestellvorschlag = (FLRBestellvorschlag) o[0];

				rows[row][col++] = bestellvorschlag.getI_id();

				if (o[1] != null) {
					rows[row][col++] = LocaleFac.STATUS_GESPERRT;
				} else {
					rows[row][col++] = null;
				}

				String artikelspr = null;
				if (o[2] != null) {
					artikelspr = ((com.lp.server.artikel.fastlanereader.generated.FLRArtikellistespr) o[2])
							.getC_bez();
				}

				rows[row][col++] = bestellvorschlag.getFlrartikelliste()
						.getC_nr();

				// belegartId
				if (bestellvorschlag.getI_belegartid() != null) {
					// Los
					if (bestellvorschlag.getBelegart_c_nr().equals(
							LocaleFac.BELEGART_LOS)) {

						rows[row][col++] = artikelspr;
					}
					// Auftrag
					else if (bestellvorschlag.getBelegart_c_nr().equals(
							LocaleFac.BELEGART_AUFTRAG)) {

						AuftragpositionDto auftragposDto = null;

						auftragposDto = this.getAuftragpositionFac()
								.auftragpositionFindByPrimaryKeyOhneExc(
										bestellvorschlag
												.getI_belegartpositionid());
						if (auftragposDto != null
								&& auftragposDto.getCBez() != null) {

							rows[row][col++] = auftragposDto.getCBez() == null ? null
									: auftragposDto.getCBez();
						} else if (artikelspr != null) {
							rows[row][col++] = artikelspr;
						} else {
							rows[row][col++] = null;
						}

					} else if (bestellvorschlag.getBelegart_c_nr().equals(
							LocaleFac.BELEGART_ANGEBOT)) {

						if (bestellvorschlag.getI_belegartpositionid() != null) {
							AngebotpositionDto angebotposDto = null;

							angebotposDto = this.getAngebotpositionFac()
									.angebotpositionFindByPrimaryKeyOhneExc(
											bestellvorschlag
													.getI_belegartpositionid());
							if (angebotposDto != null
									&& angebotposDto.getCBez() != null) {

								rows[row][col++] = angebotposDto.getCBez() == null ? null
										: angebotposDto.getCBez();
							} else if (artikelspr != null) {
								rows[row][col++] = artikelspr;
							} else {
								rows[row][col++] = null;
							}
						} else {
							rows[row][col++] = artikelspr;
						}

					}
					// Bestellung
					else if (bestellvorschlag.getBelegart_c_nr().equals(
							LocaleFac.BELEGART_BESTELLUNG)) {

						BestellpositionDto bestellpositionDto = null;

						bestellpositionDto = this.getBestellpositionFac()
								.bestellpositionFindByPrimaryKeyOhneExc(
										bestellvorschlag.getI_belegartid());

						if (bestellpositionDto != null
								&& bestellpositionDto.getCBez() != null) {
							rows[row][col++] = bestellpositionDto.getCBez() == null ? null
									: bestellpositionDto.getCBez();
						} else if (artikelspr != null) {
							rows[row][col++] = artikelspr;
						} else {
							rows[row][col++] = null;
						}
					} else {
						rows[row][col++] = null;
					}
				} else {
					if (artikelspr != null) {
						rows[row][col++] = artikelspr;
					} else {
						rows[row][col++] = null;
					}
				}
				rows[row][col++] = bestellvorschlag.getN_zubestellendemenge();
				rows[row][col++] = bestellvorschlag.getT_liefertermin();
				if (bestellvorschlag.getLieferant_i_id() != null) {
					rows[row][col++] = HelperServer
							.formatNameAusFLRPartner(bestellvorschlag
									.getFlrlieferant().getFlrpartner());
					;
					// Wiederbeschaffungszeit (falls vorhanden)

					Integer iWiederbeschaffungszeit = null;

					Set s = bestellvorschlag.getFlrartikelliste()
							.getArtikellieferantset();
					Iterator it = s.iterator();
					while (it.hasNext()) {
						FLRArtikellieferant al = (FLRArtikellieferant) it
								.next();

						if (al.getLieferant_i_id().equals(
								bestellvorschlag.getLieferant_i_id())) {
							iWiederbeschaffungszeit = al
									.getI_wiederbeschaffungszeit();
						}

					}

					rows[row][col++] = iWiederbeschaffungszeit;

				} else {
					rows[row][col++] = null;
					rows[row][col++] = null;
				}
				rows[row][col++] = bestellvorschlag.getN_nettogesamtpreis();

				rows[row][col++] = bestellvorschlag.getBelegart_c_nr();

				BestellungDto bestellungDto = null;
				BestellpositionDto besposDto = null;
				AuftragDto auftragDto = null;

				if (bestellvorschlag.getI_belegartid() != null) {
					if (bestellvorschlag.getBelegart_c_nr().equals(
							LocaleFac.BELEGART_BESTELLUNG)) {
						besposDto = getBestellpositionFac()
								.bestellpositionFindByPrimaryKeyOhneExc(
										bestellvorschlag.getI_belegartid());

						if (besposDto != null) {
							bestellungDto = getBestellungFac()
									.bestellungFindByPrimaryKey(
											besposDto.getBestellungIId());

							rows[row][col++] = bestellungDto.getCNr() == null ? null
									: bestellungDto.getCNr();
						} else {
							rows[row][col++] = "Position gel\u00F6scht";
						}
					} else if (bestellvorschlag.getBelegart_c_nr().equals(
							LocaleFac.BELEGART_AUFTRAG)) {
						auftragDto = getAuftragFac().auftragFindByPrimaryKey(
								bestellvorschlag.getI_belegartid());
						rows[row][col++] = auftragDto.getCNr() == null ? null
								: auftragDto.getCNr();
					} else if (bestellvorschlag.getBelegart_c_nr().equals(
							LocaleFac.BELEGART_ANGEBOT)) {
						AngebotDto angebotDto = getAngebotFac()
								.angebotFindByPrimaryKey(
										bestellvorschlag.getI_belegartid(),
										theClientDto);
						rows[row][col++] = angebotDto.getCNr();
					}
					// Los
					else if (bestellvorschlag.getBelegart_c_nr().equals(
							LocaleFac.BELEGART_LOS)) {
						LosDto losDto = getFertigungFac().losFindByPrimaryKey(
								bestellvorschlag.getI_belegartid());
						rows[row][col++] = losDto.getCNr();
					} else {
						rows[row][col++] = null;
					}

				} else {
					rows[row][col++] = null;
				}

				if (!Helper.short2boolean(bestellvorschlag.getFlrartikelliste()
						.getB_lagerbewirtschaftet())) {
					rows[row][col++] = new Color(0, 0, 255);
				}

				row++;
				col = 0;
			}
			result = new QueryResult(rows, this.getRowCount(), startIndex,
					endIndex, 0);
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
			String queryString = "select count(distinct bestellvorschlag.i_id) from FLRBestellvorschlag bestellvorschlag  LEFT OUTER JOIN bestellvorschlag.flrartikelliste.artikelsprset AS aspr  "
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

					if (filterKriterien[i].kritName.equals("c_volltext")) {
						String suchstring = "lower(coalesce(aspr.c_bez,'')||' '||coalesce(aspr.c_kbez,'')||' '||coalesce(aspr.c_zbez,'')||' '||coalesce(aspr.c_zbez2,''))";

						String[] teile = filterKriterien[i].value.toLowerCase()
								.split(" ");
						where.append("(");

						for (int p = 0; p < teile.length; p++) {

							if (teile[p].startsWith("-")) {
								where.append(" NOT ");

								teile[p] = teile[p].substring(1);

							}

							where.append("lower(" + suchstring + ") like '%"
									+ teile[p].toLowerCase() + "%'");
							if (p < teile.length - 1) {
								where.append(" AND ");
							}
						}

						where.append(")");
					} else {

						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" lower(bestellvorschlag."
									+ filterKriterien[i].kritName + ")");
						} else {
							where.append(" bestellvorschlag."
									+ filterKriterien[i].kritName);
						}
						where.append(" " + filterKriterien[i].operator);
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" "
									+ filterKriterien[i].value.toLowerCase());
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
					if (!kriterien[i].kritName
							.endsWith(Facade.NICHT_SORTIERBAR)) {
						if (kriterien[i].isKrit) {
							if (sortAdded) {
								orderBy.append(", ");
							}
							sortAdded = true;

							// spezial unterscheidung nur in diesem haendler
							if (kriterien[i].kritName.toString().equals("c_nr")) {
								orderBy.append("bestellvorschlag."
										+ "flrartikel.c_nr");
								orderBy.append(" ");
								orderBy.append(kriterien[i].value);

							}

							else if (kriterien[i].kritName.toString().equals(
									"c_bez")) {
								orderBy.append("bestellvorschlag."
										+ "flrartikel.c_nr");
								orderBy.append(" ");
								orderBy.append(kriterien[i].value);
							} else {
								orderBy.append("bestellvorschlag."
										+ kriterien[i].kritName);
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
				orderBy.append("bestellvorschlag.flrartikel.").append("c_nr")
						.append(" ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("bestellvorschlag." + "i_id") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" bestellvorschlag.").append("i_id").append(" ");
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
		return "SELECT bestellvorschlag,  (SELECT distinct s.artikel_i_id FROM FLRArtikelsperren as s WHERE s.artikel_i_id=bestellvorschlag.artikel_i_id AND ( s.flrsperren.b_gesperrteinkauf=1 OR s.flrsperren.b_gesperrt=1) ) as sperren, aspr from FLRBestellvorschlag bestellvorschlag LEFT OUTER JOIN bestellvorschlag.flrartikelliste.artikelsprset AS aspr ";
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

		// if (selectedId != null && ( (Integer) selectedId).intValue() >= 0) {
		if (selectedId != null) {
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();
				String queryString = "select bestellvorschlag."
						+ "i_id"
						+ " from FLRBestellvorschlag bestellvorschlag  LEFT OUTER JOIN bestellvorschlag.flrartikelliste.artikelsprset AS aspr "
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
	public TableInfo getTableInfo() {
		if (super.getTableInfo() == null) {
			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();
			setTableInfo(new TableInfo(
					new Class[] { Integer.class, Icon.class, String.class,
							String.class, BigDecimal.class, Date.class,
							String.class, Integer.class, BigDecimal.class,
							String.class, String.class, Color.class },
					new String[] {
							"i_id",
							"S",
							getTextRespectUISpr("bes.artikelcnr", mandantCNr,
									locUI),
							getTextRespectUISpr("bes.artikelbezeichnung",
									mandantCNr, locUI),
							getTextRespectUISpr("bes.zubestellendeMenge",
									mandantCNr, locUI),
							getTextRespectUISpr("bes.bestelltermin",
									mandantCNr, locUI),
							getTextRespectUISpr("bes.lieferantcnr", mandantCNr,
									locUI),
							getTextRespectUISpr(
									"artikel.wiederbeschaffungszeit.short",
									mandantCNr, locUI),
							getTextRespectUISpr(
									"bes.nettogesamtpreisminusrabatte",
									mandantCNr, locUI),
							getTextRespectUISpr("bes.belegart", mandantCNr,
									locUI),
							getTextRespectUISpr("bes.belegartnummer",
									mandantCNr, locUI), "" },
					new int[] { QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_S,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_XL,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_XS,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M, 1 },
					new String[] {
							"i_id",
							Facade.NICHT_SORTIERBAR,
							"c_nr",
							"c_bez",
							BestellvorschlagFac.FLR_BESTELLVORSCHLAG_N_ZUBESTELLENDEMENGE,
							BestellvorschlagFac.FLR_BESTELLVORSCHLAG_T_LIEFERTERMIN,
							BestellvorschlagFac.FLR_BESTELLVORSCHLAG_FLRLIEFERANT
									+ "." + LieferantFac.FLR_LIEFERANT_I_ID,
							Facade.NICHT_SORTIERBAR,
							BestellvorschlagFac.FLR_BESTELLVORSCHLAG_N_NETTOGESAMTPREISMINUSRABATTE,
							BestellvorschlagFac.FLR_BESTELLVORSCHLAG_BELEGART_C_NR,
							BestellvorschlagFac.FLR_BESTELLVORSCHLAG_I_BELEGARTID,
							"" }));
		}
		return super.getTableInfo();
	}

	// ##########################################################################
	// #####
	/**
	 * 
	 * @param aFilterKriteriumI
	 *            FilterKriterium[]
	 * @return String
	 */
	private static String buildBestellvorschlagWhereClause(
			FilterKriterium[] aFilterKriteriumI) {
		StringBuffer where = new StringBuffer("");

		if (aFilterKriteriumI != null && aFilterKriteriumI.length > 0) {
			for (int i = 0; i < aFilterKriteriumI.length; i++) {
				if (aFilterKriteriumI[i].value != null) {
					where.append(
							FLR_BESTELLVORSCHLAG
									+ aFilterKriteriumI[i].kritName)
							.append(" " + aFilterKriteriumI[i].operator + " ")
							.append(aFilterKriteriumI[i].value + " ")
							.append(" AND ");
				}
			}
		}

		String whereString = "";

		if (where.length() > 0) {
			where.insert(0, " WHERE ");
			whereString = where.substring(0, where.length() - 5); // das letzte
			// " AND "
			// abschneiden
		}

		return whereString;
	}

	/**
	 * 
	 * @param aSortierKriteriumI
	 *            SortierKriterium[]
	 * @return String
	 */
	private static String buildBestellvorschlagOrderByClause(
			SortierKriterium[] aSortierKriteriumI) {
		StringBuffer orderby = new StringBuffer("");

		if (aSortierKriteriumI != null && aSortierKriteriumI.length > 0) {
			for (int i = 0; i < aSortierKriteriumI.length; i++) {
				if (aSortierKriteriumI[i].value.equals("true")) {
					orderby.append(FLR_BESTELLVORSCHLAG
							+ aSortierKriteriumI[i].kritName + " ,");
				}
			}
		}

		String orderbyString = "";

		if (orderby.length() > 0) {
			orderby.insert(0, " ORDER BY ");
			orderbyString = orderby.substring(0, orderby.length() - 2); // das
			// letzte
			// " ,"
			// abschneiden
		}

		return orderbyString;
	}

	private static String buildBestellvorschlagOrderByClause() {

		StringBuffer orderby = new StringBuffer("");

		orderby.append("flrbestellvorschlag.")
				.append(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_T_LIEFERTERMIN)
				.append(" ASC ")

				.append(", ").append(" flrbestellvorschlag.").append("i_id")
				.append(" ");

		String orderbyString = "";

		if (orderby.length() > 0) {
			orderby.insert(0, " ORDER BY ");
			orderbyString = orderby.toString();
		}
		return orderbyString;
	}

	/**
	 * reportflr: 2 Diese Methode liefert eine Liste von allen
	 * Bestellvorschlaegen eines Mandanten, die nach den eingegebenen Kriterien
	 * des Benutzers zusammengestellt wird. <br>
	 * Achtung: Hibernate verwendet lazy initialization, d.h. der Zugriff auf
	 * Collections muss innerhalb der Session erfolgen.
	 * 
	 * @return ReportAngebotAlleDto[] die Liste der Angebote.
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @param aFilterKriteriumI
	 *            FilterKriterium[]
	 * @param aSortierKriteriumI
	 *            SortierKriterium[]
	 * @param whichFilter
	 *            String
	 */
	public static BestellvorschlagDto[] getListeBestellvorschlaege(
			FilterKriterium[] aFilterKriteriumI,
			SortierKriterium[] aSortierKriteriumI, String whichFilter)
			throws EJBExceptionLP {
		BestellvorschlagDto[] aResult = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;

		try {
			session = factory.openSession();
			String queryString = "";
			if (whichFilter
					.equals(BestellvorschlagFac.BES_NACH_BV_FUER_JEDEN_LF_UND_GLEICHE_TERMIN)) {
				queryString = FLR_BESTELLVORSCHLAG_FROM_CLAUSE
						+ BestellvorschlagHandler
								.buildBestellvorschlagWhereClause(aFilterKriteriumI)
						+ BestellvorschlagHandler
								.buildBestellvorschlagOrderByClause();

			}

			else if (whichFilter
					.equals(BestellvorschlagFac.BES_NACH_BV_FUER_JEDEN_LF)) {
				queryString = FLR_BESTELLVORSCHLAG_FROM_CLAUSE
						+ BestellvorschlagHandler
								.buildBestellvorschlagWhereClause(aFilterKriteriumI)
						+ BestellvorschlagHandler
								.buildBestellvorschlagOrderByClause();

			} else if (whichFilter
					.equals(BestellvorschlagFac.BES_NACH_BV_FUER_BESTIMMTEN_LF_UND_TERMIN)) {
				queryString = FLR_BESTELLVORSCHLAG_FROM_CLAUSE
						+ BestellvorschlagHandler
								.buildBestellvorschlagWhereClause(aFilterKriteriumI);
			} else if (whichFilter
					.equals(BestellvorschlagFac.BES_NACH_BV_FUER_BESTIMMTEN_LF)) {
				queryString = FLR_BESTELLVORSCHLAG_FROM_CLAUSE
						+ BestellvorschlagHandler
								.buildBestellvorschlagWhereClause(aFilterKriteriumI)
						+ BestellvorschlagHandler
								.buildBestellvorschlagOrderByClause();

			} else if (whichFilter
					.equals(BestellvorschlagFac.BES_ABRUFE_ZU_RAHMEN)) {
				queryString = FLR_BESTELLVORSCHLAG_FROM_CLAUSE
						+ BestellvorschlagHandler
								.buildBestellvorschlagWhereClause(aFilterKriteriumI)
						+ BestellvorschlagHandler
								.buildBestellvorschlagOrderByClause();
			}

			Query query = session.createQuery(queryString);
			List<?> list = query.list();

			aResult = new BestellvorschlagDto[list.size()];
			int iIndex = 0;
			Iterator<?> it = list.iterator();
			BestellvorschlagDto bestellvorschlagDto = null;

			while (it.hasNext()) {
				FLRBestellvorschlag flrbestellvorschlag = (FLRBestellvorschlag) it
						.next();

				bestellvorschlagDto = new BestellvorschlagDto();
				bestellvorschlagDto.setIId(flrbestellvorschlag.getI_id());
				bestellvorschlagDto.setIArtikelId(flrbestellvorschlag
						.getArtikel_i_id());
				bestellvorschlagDto.setNZubestellendeMenge(flrbestellvorschlag
						.getN_zubestellendemenge());
				bestellvorschlagDto.setCBelegartCNr(flrbestellvorschlag
						.getBelegart_c_nr());
				bestellvorschlagDto.setCMandantCNr(flrbestellvorschlag
						.getMandant_c_nr());
				bestellvorschlagDto.setIBelegartId(flrbestellvorschlag
						.getI_belegartid());
				bestellvorschlagDto.setIBelegartpositionid(flrbestellvorschlag
						.getI_belegartpositionid());
				bestellvorschlagDto.setILieferantId(flrbestellvorschlag
						.getLieferant_i_id());
				bestellvorschlagDto.setNNettoeinzelpreis(flrbestellvorschlag
						.getN_nettoeinzelpreis());
				bestellvorschlagDto.setNNettogesamtpreis(flrbestellvorschlag
						.getN_nettogesamtpreis());
				bestellvorschlagDto.setNRabattbetrag(flrbestellvorschlag
						.getN_rabattbetrag());
				bestellvorschlagDto
						.setTLiefertermin((Timestamp) flrbestellvorschlag
								.getT_liefertermin());
				bestellvorschlagDto
						.setBNettopreisuebersteuert(flrbestellvorschlag
								.getB_nettopreisuebersteuert());
				bestellvorschlagDto.setProjektIId(flrbestellvorschlag
						.getProjekt_i_id());

				aResult[iIndex] = bestellvorschlagDto;
				iIndex++;
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR,
					new Exception(t));
		} finally {
			closeSession(session);
		}

		return aResult;
	}

}
