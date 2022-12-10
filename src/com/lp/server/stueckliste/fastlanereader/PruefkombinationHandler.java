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
package com.lp.server.stueckliste.fastlanereader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.VerschleissteilDto;
import com.lp.server.artikel.service.WerkzeugDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.stueckliste.fastlanereader.generated.FLRPruefkombination;
import com.lp.server.stueckliste.fastlanereader.generated.FLRPruefkombinationspr;
import com.lp.server.stueckliste.service.PruefartDto;
import com.lp.server.stueckliste.service.PruefkombinationDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeLos;
import com.lp.server.system.jcr.service.docnode.DocNodePruefkombination;
import com.lp.server.system.jcr.service.docnode.DocPath;
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
import com.lp.util.Helper;

public class PruefkombinationHandler extends UseCaseHandler {

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
			while (resultListIterator.hasNext()) {
				FLRPruefkombination pk = (FLRPruefkombination) resultListIterator
						.next();
				rows[row][col++] = pk.getI_id();
				rows[row][col++] = pk.getFlrpruefart().getC_nr();
				// SPR
				Iterator it = pk.getPruefkombinationspr_set().iterator();

				String sSpr = null;
				while (it.hasNext()) {

					FLRPruefkombinationspr spr = (FLRPruefkombinationspr) it
							.next();
					if (spr.getLocale().getC_nr()
							.compareTo(theClientDto.getLocUiAsString()) == 0) {

						sSpr = spr.getC_bez();
					}
				}

				rows[row][col++] = sSpr;

				if (pk.getFlrartikel_kontakt() != null) {
					rows[row][col++] = pk.getFlrartikel_kontakt().getC_nr();

					ArtikelDto aDto = getArtikelFac()
							.artikelFindByPrimaryKeySmall(
									pk.getFlrartikel_kontakt().getI_id(),
									theClientDto);

					rows[row][col++] = aDto.formatBezeichnung();
				} else {
					rows[row][col++] = null;
					rows[row][col++] = null;
				}

				if (pk.getFlrartikel_litze() != null
						&& (pk.getFlrpruefart()
								.getC_nr()
								.equals(StuecklisteFac.PRUEFART_CRIMPEN_MIT_ISO)
								|| pk.getFlrpruefart()
										.getC_nr()
										.equals(StuecklisteFac.PRUEFART_CRIMPEN_OHNE_ISO)
								|| pk.getFlrpruefart()
										.getC_nr()
										.equals(StuecklisteFac.PRUEFART_MASSPRUEFUNG) || pk
								.getFlrpruefart().getC_nr()
								.equals(StuecklisteFac.PRUEFART_KRAFTMESSUNG))) {
					rows[row][col++] = pk.getFlrartikel_litze().getC_nr();
					ArtikelDto aDtoLitze = getArtikelFac()
							.artikelFindByPrimaryKeySmall(
									pk.getFlrartikel_litze().getI_id(),
									theClientDto);

					rows[row][col++] = aDtoLitze.formatBezeichnung();
				} else {
					rows[row][col++] = null;
					rows[row][col++] = null;
				}

				if (pk.getFlrverschleissteil() != null
						&& (pk.getFlrpruefart()
								.getC_nr()
								.equals(StuecklisteFac.PRUEFART_CRIMPEN_MIT_ISO) || pk
								.getFlrpruefart()
								.getC_nr()
								.equals(StuecklisteFac.PRUEFART_CRIMPEN_OHNE_ISO))) {
					rows[row][col++] = pk.getFlrverschleissteil().getC_nr();
				} else {
					rows[row][col++] = null;
				}
				rows[row][col++] = Helper.short2Boolean(pk.getB_standard());
				rows[row][col++] = pk.getN_crimphoehe_draht();
				rows[row][col++] = pk.getN_crimphoehe_isolation();
				rows[row][col++] = pk.getN_crimpbreite_draht();
				rows[row][col++] = pk.getN_crimpbreite_isolation();

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

	public PrintInfoDto getSDocPathAndPartner(Object key) {
		PruefkombinationDto pruefkombinationDto = null;
		try {
			pruefkombinationDto = getStuecklisteFac()
					.pruefkombinationFindByPrimaryKey((Integer) key,
							theClientDto);
		} catch (Exception e) {
			// Nicht gefunden
		}
		if (pruefkombinationDto != null) {
			PruefartDto paDto = getStuecklisteFac().pruefartFindByPrimaryKey(
					pruefkombinationDto.getPruefartIId(), theClientDto);
			String pfad = paDto.getCNr();

			if (pruefkombinationDto.getArtikelIIdKontakt() != null) {
				ArtikelDto aDtoKontakt = getArtikelFac()
						.artikelFindByPrimaryKeySmall(
								pruefkombinationDto.getArtikelIIdKontakt(),
								theClientDto);

				pfad = paDto.getCNr() + "/" + aDtoKontakt.getCNr();
			}

			if (pruefkombinationDto.getArtikelIIdLitze() != null) {
				ArtikelDto aDtoLitze = getArtikelFac()
						.artikelFindByPrimaryKeySmall(
								pruefkombinationDto.getArtikelIIdLitze(),
								theClientDto);

				pfad += "/" + aDtoLitze.getCNr();

			}

			if (pruefkombinationDto.getVerschleissteilIId() != null) {

				VerschleissteilDto vDto = getArtikelFac()
						.verschleissteilFindByPrimaryKey(
								pruefkombinationDto.getVerschleissteilIId());

				pfad += " " + vDto.getCNr();
			}

			DocPath docPath = new DocPath(new DocNodePruefkombination(
					pruefkombinationDto, theClientDto.getMandant(), pfad));

			return new PrintInfoDto(docPath, null, getSTable());
		} else {
			return null;
		}
	}

	protected long getRowCountFromDataBase() {
		long rowCount = 0;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			session = setFilter(session);
			String queryString = "SELECT count(pruefkombination.i_id) from FLRPruefkombination pruefkombination  LEFT OUTER JOIN pruefkombination.flrartikel_kontakt AS kontakt  LEFT OUTER JOIN pruefkombination.flrartikel_litze.artikelsprset AS litzespr LEFT OUTER JOIN pruefkombination.flrverschleissteil AS verschleissteil  "
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
			boolean filterAdded = false;

			for (int i = 0; i < filterKriterien.length; i++) {
				if (filterKriterien[i].isKrit) {
					if (filterAdded) {
						where.append(" " + booleanOperator);
					}
					filterAdded = true;

					if (filterKriterien[i].kritName.equals("c_bez_litze")) {

						String suchstring = "lower(coalesce(litzespr.c_bez,'')||' '||coalesce(litzespr.c_kbez,'')||' '||coalesce(litzespr.c_zbez,'')||' '||coalesce(litzespr.c_zbez2,''))";

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

						where.append(") ");

					} else if (filterKriterien[i].kritName
							.equals("BEIDE_RICHTUNGEN")) {

						String[] teile = filterKriterien[i].value.toLowerCase()
								.split(" ");
						if (teile.length == 1) {

							String suchstring = "(pruefkombination.flrpruefart.c_nr IN ('"
									+ StuecklisteFac.PRUEFART_MASSPRUEFUNG
									+ "','"
									+ StuecklisteFac.PRUEFART_ELEKTRISCHE_PRUEFUNG
									+ "','"
									+ StuecklisteFac.PRUEFART_OPTISCHE_PRUEFUNG
									+ "')) AND (pruefkombination.flrartikel_kontakt.i_id ="
									+ teile[0]
									+ " OR pruefkombination.flrartikel_litze.i_id ="
									+ teile[0] + ")";

							where.append(suchstring);
						} else if (teile.length == 2) {

							String suchstring = "(pruefkombination.flrpruefart.c_nr IN ('"
									+ StuecklisteFac.PRUEFART_CRIMPEN_MIT_ISO
									+ "','"
									+ StuecklisteFac.PRUEFART_CRIMPEN_OHNE_ISO
									+ "','"
									+ StuecklisteFac.PRUEFART_KRAFTMESSUNG
									+ "')) AND ( pruefkombination.flrartikel_kontakt.i_id IN ("
									+ teile[0]
									+ ","
									+ teile[1]
									+ ") AND pruefkombination.flrartikel_litze.i_id IN ("
									+ teile[0] + "," + teile[1] + "))";

							where.append(suchstring);
						}

					} else {

						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" upper(pruefkombination."
									+ filterKriterien[i].kritName + ")");
						} else {
							where.append(" pruefkombination."
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
						orderBy.append("pruefkombination."
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
				orderBy.append("pruefkombination.flrpruefart.c_nr ASC, pruefkombination.flrartikel_kontakt.c_nr ASC ");
				sortAdded = true;
			}
			if (orderBy
					.indexOf("pruefkombination.flrpruefart.c_nr ASC, pruefkombination.flrartikel_kontakt.c_nr") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append("pruefkombination.flrpruefart.c_nr ASC,  pruefkombination.flrartikel_kontakt.c_nr ");
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
		return "SELECT pruefkombination from FLRPruefkombination pruefkombination LEFT OUTER JOIN pruefkombination.flrverschleissteil AS verschleissteil LEFT OUTER JOIN pruefkombination.flrartikel_kontakt AS kontakt LEFT OUTER JOIN pruefkombination.flrartikel_litze.artikelsprset AS litzespr ";
	}

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
				session = setFilter(session);
				String queryString = "select pruefkombination.i_id from FLRPruefkombination pruefkombination LEFT OUTER JOIN pruefkombination.flrverschleissteil AS verschleissteil  LEFT OUTER JOIN pruefkombination.flrartikel_kontakt AS kontakt  LEFT OUTER JOIN pruefkombination.flrartikel_litze.artikelsprset AS litzespr "
						+ this.buildWhereClause() + this.buildOrderByClause();
				Query query = session.createQuery(queryString);
				ScrollableResults scrollableResult = query.scroll();
				// boolean idFound = false;
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

	public TableInfo getTableInfo() {
		if (super.getTableInfo() == null) {
			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();
			setTableInfo(new TableInfo(new Class[] { Integer.class,
					String.class, String.class, String.class, String.class,
					String.class, String.class, String.class, Boolean.class,
					BigDecimal.class, BigDecimal.class, BigDecimal.class,
					BigDecimal.class },

			new String[] {
					"Id",
					getTextRespectUISpr("stkl.pruefkombination.pruefart",
							mandantCNr, locUI),
					getTextRespectUISpr("lp.kommentar", mandantCNr, locUI),
					getTextRespectUISpr("stkl.pruefkombination.kontakt",
							mandantCNr, locUI),
					getTextRespectUISpr("lp.bezeichnung", mandantCNr, locUI),
					getTextRespectUISpr("stkl.pruefkombination.litze",
							mandantCNr, locUI),
					getTextRespectUISpr("lp.bezeichnung", mandantCNr, locUI),
					getTextRespectUISpr(
							"stkl.pruefkombination.verschleissteil",
							mandantCNr, locUI),
					getTextRespectUISpr("stkl.pruefkombination.standard",
							mandantCNr, locUI),
					getTextRespectUISpr(
							"stkl.pruefkombination.crimphoehedraht",
							mandantCNr, locUI),
					getTextRespectUISpr(
							"stkl.pruefkombination.crimphoeheisolation",
							mandantCNr, locUI),
					getTextRespectUISpr(
							"stkl.pruefkombination.crimpbreitedraht",
							mandantCNr, locUI),
					getTextRespectUISpr(
							"stkl.pruefkombination.crimpbreiteisolation",
							mandantCNr, locUI) },

			new int[] {
					-1, // diese Spalte wird ausgeblendet
					QueryParameters.FLR_BREITE_SHARE_WITH_REST,
					QueryParameters.FLR_BREITE_SHARE_WITH_REST,
					QueryParameters.FLR_BREITE_SHARE_WITH_REST,
					QueryParameters.FLR_BREITE_SHARE_WITH_REST,
					QueryParameters.FLR_BREITE_SHARE_WITH_REST,
					QueryParameters.FLR_BREITE_SHARE_WITH_REST,
					QueryParameters.FLR_BREITE_SHARE_WITH_REST,
					QueryParameters.FLR_BREITE_SHARE_WITH_REST,
					QueryParameters.FLR_BREITE_SHARE_WITH_REST,
					QueryParameters.FLR_BREITE_SHARE_WITH_REST,
					QueryParameters.FLR_BREITE_SHARE_WITH_REST,
					QueryParameters.FLR_BREITE_SHARE_WITH_REST },

			new String[] { "id", "flrpruefart.c_nr", Facade.NICHT_SORTIERBAR,
					"flrartikel_kontakt.c_nr", Facade.NICHT_SORTIERBAR,
					"flrartikel_litze.c_nr", Facade.NICHT_SORTIERBAR,
					"flrverschleissteil.c_nr", "b_standard",
					"n_crimphoehe_draht", "n_crimphoehe_isolation",
					"n_crimpbreite_draht", "n_crimpbreite_isolation" }));
		}

		return super.getTableInfo();
	}
}
