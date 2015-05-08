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

import com.lp.server.artikel.fastlanereader.generated.FLRArtikellieferant;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikellistespr;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.bestellung.fastlanereader.generated.FLRBestellvorschlag;
import com.lp.server.bestellung.fastlanereader.generated.FLRLieferantenoptimieren;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellvorschlagFac;
import com.lp.server.fertigung.service.LosDto;
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

public class LieferantenoptimierenHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String FLR_LIEFERANTENOPTIMIEREN = "FLRLieferantenoptimieren.";
	public static final String FLR_LIEFERANTENOPTIMIEREN_FROM_CLAUSE = " from FLRLieferantenoptimieren flrLieferantenoptimieren ";

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

			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size() + 1][colCount];
			int row = 0;
			int col = 0;
			BigDecimal summe = new BigDecimal(0);

			while (resultListIterator.hasNext()) {

				Object[] o = (Object[]) resultListIterator.next();

				FLRLieferantenoptimieren flrLieferantenoptimieren = (FLRLieferantenoptimieren) o[0];
				FLRBestellvorschlag bestellvorschlag = flrLieferantenoptimieren
						.getFlrbestellvorschlag();

				rows[row][col++] = bestellvorschlag.getI_id();

				if (o[1] != null) {
					rows[row][col++] = LocaleFac.STATUS_GESPERRT;
				} else {
					rows[row][col++] = null;
				}
				rows[row][col++] = bestellvorschlag.getFlrartikelliste()
						.getC_nr();

				if (o[2] != null) {
					FLRArtikellistespr artikelspr = ((com.lp.server.artikel.fastlanereader.generated.FLRArtikellistespr) o[2]);
					rows[row][col++] = artikelspr.getC_bez();
					rows[row][col++] = artikelspr.getC_zbez();
				} else {
					rows[row][col++] = null;
					rows[row][col++] = null;
				}
				rows[row][col++] = bestellvorschlag.getN_zubestellendemenge();
				rows[row][col++] = bestellvorschlag.getFlrartikel()
						.getEinheit_c_nr();

				BigDecimal nettopreisArtikellieferant = new BigDecimal(0);
				ArtikellieferantDto alDto = getArtikelFac()
						.getArtikelEinkaufspreis(
								flrLieferantenoptimieren.getArtikel_i_id(),
								flrLieferantenoptimieren
										.getLieferant_i_id_artikellieferant(),
								bestellvorschlag.getN_zubestellendemenge(),
								theClientDto.getSMandantenwaehrung(),
								Helper.cutDate(new java.sql.Date(System
										.currentTimeMillis())), theClientDto);

				if (alDto != null) {

					if (alDto.getNNettopreis() != null) {
						nettopreisArtikellieferant = alDto.getNNettopreis();
					}

					rows[row][col++] = alDto.getNNettopreis();
					rows[row][col++] = flrLieferantenoptimieren
							.getFlrartikellieferant()
							.getI_wiederbeschaffungszeit();
					rows[row][col++] = alDto.getNFixkosten();
				} else {
					rows[row][col++] = null;
					rows[row][col++] = flrLieferantenoptimieren
							.getFlrartikellieferant()
							.getI_wiederbeschaffungszeit();
					rows[row][col++] = null;
				}

				rows[row][col++] = flrLieferantenoptimieren
						.getFlrartikellieferant().getI_sort();

				if (flrLieferantenoptimieren
						.getFlrbestellvorschlag().getFlrlieferant() == null || !flrLieferantenoptimieren
						.getFlrartikellieferant()
						.getLieferant_i_id()
						.equals(flrLieferantenoptimieren
								.getFlrbestellvorschlag().getFlrlieferant()
								.getI_id())) {
					rows[row][col++] = Color.GRAY;
				} else {
					summe = summe.add(nettopreisArtikellieferant
							.multiply(bestellvorschlag
									.getN_zubestellendemenge()));
				}

				row++;
				col = 0;
			}
			// Zeile mit Summe hinzufuegen
			rows[row][6] = "Summe:";
			rows[row][7] = summe;

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
			String queryString = "select count(distinct flrLieferantenoptimieren.bestellvorschlag_i_id) from FLRLieferantenoptimieren flrLieferantenoptimieren  LEFT OUTER JOIN flrLieferantenoptimieren.flrartikelliste.artikelsprset AS aspr  "
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
		return rowCount + 1;
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
							where.append(" lower("
									+ filterKriterien[i].kritName + ")");
						} else {
							where.append(" " + filterKriterien[i].kritName);
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
								orderBy.append("flrLieferantenoptimieren."
										+ "flrartikelliste.c_nr");
								orderBy.append(" ");
								orderBy.append(kriterien[i].value);

							}

							else if (kriterien[i].kritName.toString().equals(
									"c_bez")) {
								orderBy.append("flrLieferantenoptimieren."
										+ "flrartikelliste.c_nr");
								orderBy.append(" ");
								orderBy.append(kriterien[i].value);
							} else {
								orderBy.append("" + kriterien[i].kritName);
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
				orderBy.append("flrLieferantenoptimieren.flrartikelliste.")
						.append("c_nr").append(" ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("flrLieferantenoptimieren."
					+ "bestellvorschlag_i_id") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" flrLieferantenoptimieren.")
						.append("bestellvorschlag_i_id").append(" ");
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
		return "SELECT flrLieferantenoptimieren,  (SELECT distinct s.artikel_i_id FROM FLRArtikelsperren as s WHERE s.artikel_i_id=flrLieferantenoptimieren.artikel_i_id AND ( s.flrsperren.b_gesperrteinkauf=1 OR s.flrsperren.b_gesperrt=1) ) as sperren, aspr from FLRLieferantenoptimieren flrLieferantenoptimieren LEFT OUTER JOIN flrLieferantenoptimieren.flrartikelliste.artikelsprset AS aspr ";
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
				String queryString = "select flrLieferantenoptimieren."
						+ "bestellvorschlag_i_id"
						+ " from FLRLieferantenoptimieren flrLieferantenoptimieren  LEFT OUTER JOIN flrLieferantenoptimieren.flrartikelliste.artikelsprset AS aspr "
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
							String.class, String.class, BigDecimal.class,
							String.class, BigDecimal.class, Integer.class,
							BigDecimal.class, Integer.class, Color.class },
					new String[] {
							"i_id",
							"S",
							getTextRespectUISpr("bes.artikelcnr", mandantCNr,
									locUI),
							getTextRespectUISpr("bes.artikelbezeichnung",
									mandantCNr, locUI),
							getTextRespectUISpr("artikel.zusatzbez",
									mandantCNr, locUI),

							getTextRespectUISpr("bes.zubestellendeMenge",
									mandantCNr, locUI),
							getTextRespectUISpr("bes.einheit", mandantCNr,
									locUI),
							getTextRespectUISpr(
									"bes.nettogesamtpreisminusrabatte",
									mandantCNr, locUI),
							getTextRespectUISpr(
									"artikel.wiederbeschaffungszeit.short",
									mandantCNr, locUI),
							getTextRespectUISpr("bes.fixkosten", mandantCNr,
									locUI),
							getTextRespectUISpr("bes.sort", mandantCNr, locUI),
							"" },
					new int[] { QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_S,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_XL,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_XM,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_XS,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,

							QueryParameters.FLR_BREITE_M, 1 },
					new String[] {
							"i_id",
							Facade.NICHT_SORTIERBAR,
							"flrLieferantenoptimieren.flrartikelliste.c_nr",
							"aspr.c_bez",
							"aspr.c_zbez",
							"flrLieferantenoptimieren.flrbestellvorschlag."
									+ BestellvorschlagFac.FLR_BESTELLVORSCHLAG_N_ZUBESTELLENDEMENGE,
							"flrLieferantenoptimieren.flrartikelliste.einheit_c_nr",
							Facade.NICHT_SORTIERBAR, Facade.NICHT_SORTIERBAR,
							Facade.NICHT_SORTIERBAR, Facade.NICHT_SORTIERBAR,
							"" }));
		}

		return super.getTableInfo();
	}

}
