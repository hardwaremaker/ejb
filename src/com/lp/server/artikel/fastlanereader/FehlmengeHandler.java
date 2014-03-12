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
package com.lp.server.artikel.fastlanereader;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.Icon;
import javax.swing.UIManager;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.artikel.fastlanereader.generated.FLRFehlmenge;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.bestellung.ejbfac.BestellpositionFacBean;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellpositionDtoAssembler;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.fertigung.service.LoslagerentnahmeDto;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklisteposition;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
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

/**
 * <p>
 * Hier wird die FLR Funktionalitaet fuer die Fehlmengen implementiert.
 * </p>
 * <p>
 * Copright Logistik Pur Software GmbH (c) 2004-2007,2005,2006
 * </p>
 * <p>
 * Erstellungsdatum 13.10.2005
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version 1.0
 */
public class FehlmengeHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String FLR_FEHLMENGE = "flrfehlmenge.";
	private static final String FLR_FEHLMENGE_FROM_CLAUSE = "SELECT flrfehlmenge,(SELECT distinct s.artikel_i_id FROM FLRArtikelsperren as s WHERE s.artikel_i_id=flrfehlmenge.flrartikel.i_id) as sperren from FLRFehlmenge flrfehlmenge LEFT OUTER JOIN flrfehlmenge.flrlossollmaterial.flrlos.flrstueckliste AS stkl ";

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {

		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = PAGE_SIZE;
			int startIndex = Math.max(rowIndex.intValue() - (pageSize / 2), 0);
			int endIndex = startIndex + pageSize - 1;

			BestellpositionDto bestellpositionDtos[] = null;

			session = factory.openSession();
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
				FLRFehlmenge fm = (FLRFehlmenge) o[0];
				rows[row][col++] = fm.getI_id();
				rows[row][col++] = fm.getFlrlossollmaterial().getFlrlos()
						.getC_nr();
				rows[row][col++] = fm.getFlrartikel().getC_nr();
				rows[row][col++] = getArtikelFac()
						.artikelFindByPrimaryKeySmall(
								fm.getFlrartikel().getI_id(), theClientDto)
						.formatBezeichnung();
				rows[row][col++] = fm.getN_menge();
				rows[row][col++] = fm.getT_liefertermin();

				java.sql.Date dFruehesterEintreffTermin = null;

				bestellpositionDtos = getBestellpositionFac()
						.bestellpositionfindByArtikelOrderByTAuftragsbestaetigungstermin(
								fm.getFlrartikel().getI_id(), theClientDto);
				if (bestellpositionDtos != null) {
					for (int i = 0; i < bestellpositionDtos.length; i++) {
						if (!bestellpositionDtos[i]
								.getBestellpositionstatusCNr()
								.equals(BestellpositionFac.BESTELLPOSITIONSTATUS_ERLEDIGT)) {

							if (bestellpositionDtos[i].getNOffeneMenge() == null
									|| bestellpositionDtos[i].getNOffeneMenge()
											.doubleValue() > 0) {

								dFruehesterEintreffTermin = bestellpositionDtos[i]
										.getTAuftragsbestaetigungstermin();
								break;

							}
						}
					}

				}

				rows[row][col++] = dFruehesterEintreffTermin;

				if (fm.getFlrlossollmaterial() != null
						&& fm.getFlrlossollmaterial().getFlrlos()
								.getFlrstueckliste() != null) {
					rows[row][col++] = Helper.short2Boolean(fm
							.getFlrlossollmaterial().getFlrlos()
							.getFlrstueckliste()
							.getB_materialbuchungbeiablieferung());
				} else {
					rows[row][col++] = null;
				}

				// Lagerstand
				BigDecimal lagerstand = new BigDecimal(0);

				LoslagerentnahmeDto[] lolaeDtos = getFertigungFac()
						.loslagerentnahmeFindByLosIId(
								fm.getFlrlossollmaterial().getFlrlos()
										.getI_id());
				for (int i = 0; i < lolaeDtos.length; i++) {

					if (Helper.short2boolean(fm.getFlrartikel()
							.getB_lagerbewirtschaftet())) {

						BigDecimal lagerstandEisneLagers = getLagerFac()
								.getLagerstand(fm.getFlrartikel().getI_id(),
										lolaeDtos[i].getLagerIId(),
										theClientDto);
						if (lagerstandEisneLagers != null) {
							lagerstand = lagerstand.add(lagerstandEisneLagers);
						}
					}
				}

				rows[row][col++] = lagerstand;

				if (o[1] != null) {
					rows[row][col++] = LocaleFac.STATUS_GESPERRT;
				} else {
					rows[row][col++] = null;
				}

				if (fm.getN_menge().doubleValue() > lagerstand.doubleValue()) {
					rows[row][col++] = Color.RED;
				} else {
					rows[row][col++] = null;
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

	protected long getRowCountFromDataBase() {
		long rowCount = 0;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			String queryString = "SELECT count(*) from FLRFehlmenge flrfehlmenge LEFT OUTER JOIN flrfehlmenge.flrlossollmaterial.flrlos.flrstueckliste AS stkl "
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
					where.append(" " + FLR_FEHLMENGE
							+ filterKriterien[i].kritName);
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
					if (!kriterien[i].kritName
							.endsWith(Facade.NICHT_SORTIERBAR)) {
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
				orderBy.append(FLR_FEHLMENGE)
						.append(ArtikelFac.FLR_FEHLMENGE_I_ID).append(" DESC ");
				sortAdded = true;
			}
			if (orderBy.indexOf(FLR_FEHLMENGE + ArtikelFac.FLR_FEHLMENGE_I_ID) < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" ").append(FLR_FEHLMENGE)
						.append(ArtikelFac.FLR_FEHLMENGE_I_ID).append(" ");
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
		return FLR_FEHLMENGE_FROM_CLAUSE;
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
				String queryString = "select "
						+ FLR_FEHLMENGE
						+ ArtikelFac.FLR_FEHLMENGE_I_ID
						+ " from FLRFehlmenge flrfehlmenge LEFT OUTER JOIN flrfehlmenge.flrlossollmaterial.flrlos.flrstueckliste AS stkl"
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

	public TableInfo getTableInfo() {
		if (super.getTableInfo() == null) {
			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();
			setTableInfo(new TableInfo(
					new Class[] { Integer.class, String.class, String.class,
							String.class, BigDecimal.class,
							java.util.Date.class, java.util.Date.class,
							Boolean.class, BigDecimal.class, Icon.class,
							Color.class },

					new String[] {
							"i_id",
							getTextRespectUISpr("lp.losnr", mandantCNr, locUI),
							getTextRespectUISpr("lp.artikel", mandantCNr, locUI),
							getTextRespectUISpr("lp.bezeichnung", mandantCNr,
									locUI),
							getTextRespectUISpr("lp.menge", mandantCNr, locUI),
							getTextRespectUISpr("lp.begintermin", mandantCNr,
									locUI),
							getTextRespectUISpr("lp.eintrefftermin",
									mandantCNr, locUI),
							getTextRespectUISpr(
									"fert.materialbuchungbeiablieferung",
									mandantCNr, locUI),
							getTextRespectUISpr("lp.lagerstand", mandantCNr,
									locUI), "S", "" }, new int[] {
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_PREIS,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_S,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_S,
							QueryParameters.FLR_BREITE_S }, new String[] {
							FLR_FEHLMENGE + ArtikelFac.FLR_FEHLMENGE_I_ID,
							FLR_FEHLMENGE
									+ ArtikelFac.FLR_FEHLMENGE_C_BELEGARTNR,
							FLR_FEHLMENGE + ArtikelFac.FLR_FEHLMENGE_FLRARTIKEL
									+ ".c_nr",
							Facade.NICHT_SORTIERBAR,
							FLR_FEHLMENGE + ArtikelFac.FLR_FEHLMENGE_N_MENGE,
							FLR_FEHLMENGE
									+ ArtikelFac.FLR_FEHLMENGE_T_LIEFERTERMIN,
							FLR_FEHLMENGE + ArtikelFac.FLR_FEHLMENGE_AB_TERMIN,
							"stkl.b_materialbuchungbeiablieferung",
							Facade.NICHT_SORTIERBAR, Facade.NICHT_SORTIERBAR,
							Facade.NICHT_SORTIERBAR }));
		}
		return super.getTableInfo();
	}
}
