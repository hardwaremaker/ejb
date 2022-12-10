
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
package com.lp.server.angebotstkl.fastlanereader;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.angebotstkl.fastlanereader.generated.FLREinkaufsangebotposition;
import com.lp.server.angebotstkl.fastlanereader.generated.FLREkaglieferant;
import com.lp.server.angebotstkl.fastlanereader.generated.FLREkgruppelieferant;
import com.lp.server.angebotstkl.fastlanereader.generated.FLRPositionlieferant;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.projekt.fastlanereader.generated.FLRBereich;
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

public class PositionlieferantHandler extends UseCaseHandler {

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
			String queryString = "SELECT positionlieferant, aspr from FLRPositionlieferant positionlieferant LEFT OUTER JOIN positionlieferant.flreinkaufsangebotposition.flrartikel.artikelsprset AS aspr "
					+ this.buildWhereClause() + this.buildOrderByClause();
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			int iNachkommastellenMenge = getMandantFac().getNachkommastellenMenge(theClientDto.getMandant());
			int iNachkommastellenPreis = getMandantFac().getNachkommastellenPreisEK(theClientDto.getMandant());

			String[] tooltipData = new String[resultList.size()];

			int row = 0;
			int col = 0;
			while (resultListIterator.hasNext()) {
				Object[] o = (Object[]) resultListIterator.next();
				FLRPositionlieferant positionlieferant = (FLRPositionlieferant) o[0];

				FLREinkaufsangebotposition agstklposition = positionlieferant.getFlreinkaufsangebotposition();

				rows[row][col++] = positionlieferant.getI_id();

				rows[row][col++] = getUIObjectBigDecimalNachkommastellen(agstklposition.getN_menge(),
						iNachkommastellenMenge);
				rows[row][col++] = agstklposition.getEinheit_c_nr() == null ? ""
						: agstklposition.getEinheit_c_nr().trim();

				if (agstklposition.getFlrartikel() != null) {
					rows[row][col++] = agstklposition.getFlrartikel().getC_nr();
				} else {
					rows[row][col++] = null;
				}

				if (agstklposition.getAgstklpositionsart_c_nr().equals(LocaleFac.POSITIONSART_IDENT)) {

					ArtikelDto aDto = getArtikelFac()
							.artikelFindByPrimaryKeySmall(agstklposition.getFlrartikel().getI_id(), theClientDto);
					rows[row][col++] = aDto.getCBezAusSpr();
					rows[row][col++] = aDto.getCZBezAusSpr();
				} else if (agstklposition.getAgstklpositionsart_c_nr().equals(LocaleFac.POSITIONSART_HANDEINGABE)) {
					rows[row][col++] = agstklposition.getC_bez();
					rows[row][col++] = null;
				}

				rows[row][col++] = positionlieferant.getC_artikelnrlieferant();
				rows[row][col++] = positionlieferant.getI_lieferzeitinkw();

				rows[row][col++] = getUIObjectBigDecimalNachkommastellen(positionlieferant.getN_preis_menge1(),
						iNachkommastellenPreis);
				rows[row][col++] = getUIObjectBigDecimalNachkommastellen(positionlieferant.getN_preis_menge2(),
						iNachkommastellenPreis);
				rows[row][col++] = getUIObjectBigDecimalNachkommastellen(positionlieferant.getN_preis_menge3(),
						iNachkommastellenPreis);
				rows[row][col++] = getUIObjectBigDecimalNachkommastellen(positionlieferant.getN_preis_menge4(),
						iNachkommastellenPreis);
				rows[row][col++] = getUIObjectBigDecimalNachkommastellen(positionlieferant.getN_preis_menge5(),
						iNachkommastellenPreis);

				BigDecimal bdPreisMenge1 = getLocaleFac().rechneUmInAndereWaehrungZuDatumOhneExc(
						positionlieferant.getN_preis_menge1(),
						positionlieferant.getFlrekaglieferant().getWaehrung_c_nr(),
						theClientDto.getSMandantenwaehrung(), new java.sql.Date(System.currentTimeMillis()),
						theClientDto);

				if (bdPreisMenge1 != null) {
					BigDecimal aufschlag1 = Helper.getProzentWert(bdPreisMenge1,
							positionlieferant.getFlrekaglieferant().getN_aufschlag(), iNachkommastellenPreis);
					bdPreisMenge1 = bdPreisMenge1.add(aufschlag1);
				}

				rows[row][col++] = getUIObjectBigDecimalNachkommastellen(bdPreisMenge1, iNachkommastellenPreis);
				BigDecimal bdPreisMenge2 = getLocaleFac().rechneUmInAndereWaehrungZuDatumOhneExc(
						positionlieferant.getN_preis_menge2(),
						positionlieferant.getFlrekaglieferant().getWaehrung_c_nr(),
						theClientDto.getSMandantenwaehrung(), new java.sql.Date(System.currentTimeMillis()),
						theClientDto);
				
				if (bdPreisMenge2 != null) {
					BigDecimal aufschlag = Helper.getProzentWert(bdPreisMenge2,
							positionlieferant.getFlrekaglieferant().getN_aufschlag(), iNachkommastellenPreis);
					bdPreisMenge2 = bdPreisMenge2.add(aufschlag);
				}
				

				rows[row][col++] = getUIObjectBigDecimalNachkommastellen(bdPreisMenge2, iNachkommastellenPreis);
				BigDecimal bdPreisMenge3 = getLocaleFac().rechneUmInAndereWaehrungZuDatumOhneExc(
						positionlieferant.getN_preis_menge3(),
						positionlieferant.getFlrekaglieferant().getWaehrung_c_nr(),
						theClientDto.getSMandantenwaehrung(), new java.sql.Date(System.currentTimeMillis()),
						theClientDto);

				if (bdPreisMenge3 != null) {
					BigDecimal aufschlag = Helper.getProzentWert(bdPreisMenge3,
							positionlieferant.getFlrekaglieferant().getN_aufschlag(), iNachkommastellenPreis);
					bdPreisMenge3 = bdPreisMenge3.add(aufschlag);
				}
				
				rows[row][col++] = getUIObjectBigDecimalNachkommastellen(bdPreisMenge3, iNachkommastellenPreis);
				BigDecimal bdPreisMenge4 = getLocaleFac().rechneUmInAndereWaehrungZuDatumOhneExc(
						positionlieferant.getN_preis_menge4(),
						positionlieferant.getFlrekaglieferant().getWaehrung_c_nr(),
						theClientDto.getSMandantenwaehrung(), new java.sql.Date(System.currentTimeMillis()),
						theClientDto);

				if (bdPreisMenge4 != null) {
					BigDecimal aufschlag = Helper.getProzentWert(bdPreisMenge4,
							positionlieferant.getFlrekaglieferant().getN_aufschlag(), iNachkommastellenPreis);
					bdPreisMenge4 = bdPreisMenge4.add(aufschlag);
				}
				
				rows[row][col++] = getUIObjectBigDecimalNachkommastellen(bdPreisMenge4, iNachkommastellenPreis);
				BigDecimal bdPreisMenge5 = getLocaleFac().rechneUmInAndereWaehrungZuDatumOhneExc(
						positionlieferant.getN_preis_menge5(),
						positionlieferant.getFlrekaglieferant().getWaehrung_c_nr(),
						theClientDto.getSMandantenwaehrung(), new java.sql.Date(System.currentTimeMillis()),
						theClientDto);

				if (bdPreisMenge5 != null) {
					BigDecimal aufschlag = Helper.getProzentWert(bdPreisMenge5,
							positionlieferant.getFlrekaglieferant().getN_aufschlag(), iNachkommastellenPreis);
					bdPreisMenge5 = bdPreisMenge5.add(aufschlag);
				}
				
				rows[row][col++] = getUIObjectBigDecimalNachkommastellen(bdPreisMenge5, iNachkommastellenPreis);

				tooltipData[row] = positionlieferant.getC_bemerkung();

				row++;

				col = 0;
			}
			result = new QueryResult(rows, this.getRowCount(), startIndex, endIndex, 0, tooltipData);
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

	protected long getRowCountFromDataBase() {
		long rowCount = 0;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			session = setFilter(session);
			String queryString = "select count(*) from FLRPositionlieferant positionlieferant LEFT OUTER JOIN positionlieferant.flreinkaufsangebotposition.flrartikel.artikelsprset AS aspr "
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

					if (filterKriterien[i].kritName.equals("aspr.c_bez")) {

						where.append("");
						where.append(buildWhereClauseExtendedSearch(Arrays.asList(filterKriterien[i].value.split(" ")),
								filterKriterien[i].kritName, filterKriterien[i].isBIgnoreCase()));
						where.append(" OR ");
						where.append(buildWhereClauseExtendedSearch(Arrays.asList(filterKriterien[i].value.split(" ")),
								"positionlieferant.flreinkaufsangebotposition.c_bez",
								filterKriterien[i].isBIgnoreCase()));
						where.append(" OR ");
						where.append(buildWhereClauseExtendedSearch(Arrays.asList(filterKriterien[i].value.split(" ")),
								"positionlieferant.flreinkaufsangebotposition.c_bez",
								filterKriterien[i].isBIgnoreCase()));
					} else {

						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" upper(positionlieferant." + filterKriterien[i].kritName + ")");
						} else {
							where.append(" positionlieferant." + filterKriterien[i].kritName);
						}
						where.append(" " + filterKriterien[i].operator);
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" " + filterKriterien[i].value.toUpperCase());
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
						orderBy.append("positionlieferant." + kriterien[i].kritName);
						orderBy.append(" ");
						orderBy.append(kriterien[i].value);
					}
				}
			} else {
				// no sort criteria found, add default sort
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append("positionlieferant.flreinkaufsangebotposition.i_sort ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("positionlieferant.flreinkaufsangebotposition.i_sort") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" positionlieferant.flreinkaufsangebotposition.i_sort ");
				sortAdded = true;
			}
			if (sortAdded) {
				orderBy.insert(0, " ORDER BY ");
			}
		}
		return orderBy.toString();
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
				session = setFilter(session);
				String queryString = "select positionlieferant.i_id from FLRPositionlieferant positionlieferant LEFT OUTER JOIN positionlieferant.flreinkaufsangebotposition.flrartikel.artikelsprset AS aspr "
						+ this.buildWhereClause() + this.buildOrderByClause();
				Query query = session.createQuery(queryString);
				ScrollableResults scrollableResult = query.scroll();
				if (scrollableResult != null) {
					scrollableResult.beforeFirst();
					while (scrollableResult.next()) {
						Integer id = scrollableResult.getInteger(0); // TYPE OF
						// KEY
						// ATTRIBUTE
						// !!!
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

			try {

				String mandantCNr = theClientDto.getMandant();
				Locale locUI = theClientDto.getLocUi();

				int iNachkommastellenMenge = getMandantFac().getNachkommastellenMenge(theClientDto.getMandant());
				int iNachkommastellenPreis = getMandantFac().getNachkommastellenPreisEK(theClientDto.getMandant());

				setTableInfo(new TableInfo(
						new Class[] { Integer.class, getUIClassBigDecimalNachkommastellen(iNachkommastellenMenge),
								String.class, String.class, String.class, String.class, String.class, Integer.class,
								getUIClassBigDecimalNachkommastellen(iNachkommastellenPreis),
								getUIClassBigDecimalNachkommastellen(iNachkommastellenPreis),
								getUIClassBigDecimalNachkommastellen(iNachkommastellenPreis),
								getUIClassBigDecimalNachkommastellen(iNachkommastellenPreis),
								getUIClassBigDecimalNachkommastellen(iNachkommastellenPreis),
								getUIClassBigDecimalNachkommastellen(iNachkommastellenPreis),
								getUIClassBigDecimalNachkommastellen(iNachkommastellenPreis),
								getUIClassBigDecimalNachkommastellen(iNachkommastellenPreis),
								getUIClassBigDecimalNachkommastellen(iNachkommastellenPreis),
								getUIClassBigDecimalNachkommastellen(iNachkommastellenPreis), },

						new String[] { "Id",
								getTextRespectUISpr("lp.menge", theClientDto.getMandant(), theClientDto.getLocUi()),
								getTextRespectUISpr("lp.einheit", theClientDto.getMandant(), theClientDto.getLocUi()),
								getTextRespectUISpr("artikel.artikelnummerlang", theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.bezeichnung", theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("artikel.zusatzbez", theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("agstkl.positionlieferant.artikelnrlieferant", mandantCNr, locUI),
								getTextRespectUISpr("agstkl.ekaglieferant.positionlieferant.lieferzeit", mandantCNr,
										locUI),
								getTextRespectUISpr("agstkl.ekaglieferant.positionlieferant.preis",
										theClientDto.getMandant(), theClientDto.getLocUi()) + " 1",
								getTextRespectUISpr("agstkl.ekaglieferant.positionlieferant.preis",
										theClientDto.getMandant(), theClientDto.getLocUi()) + " 2",
								getTextRespectUISpr("agstkl.ekaglieferant.positionlieferant.preis",
										theClientDto.getMandant(), theClientDto.getLocUi()) + " 3",
								getTextRespectUISpr("agstkl.ekaglieferant.positionlieferant.preis",
										theClientDto.getMandant(), theClientDto.getLocUi()) + " 4",
								getTextRespectUISpr("agstkl.ekaglieferant.positionlieferant.preis",
										theClientDto.getMandant(), theClientDto.getLocUi()) + " 5",
								getTextRespectUISpr("agstkl.ekaglieferant.positionlieferant.preis",
										theClientDto.getMandant(), theClientDto.getLocUi()) + " 1" + "/"
										+ theClientDto.getSMandantenwaehrung(),
								getTextRespectUISpr("agstkl.ekaglieferant.positionlieferant.preis",
										theClientDto.getMandant(), theClientDto.getLocUi()) + " 2" + "/"
										+ theClientDto.getSMandantenwaehrung(),
								getTextRespectUISpr("agstkl.ekaglieferant.positionlieferant.preis",
										theClientDto.getMandant(), theClientDto.getLocUi()) + " 3" + "/"
										+ theClientDto.getSMandantenwaehrung(),
								getTextRespectUISpr("agstkl.ekaglieferant.positionlieferant.preis",
										theClientDto.getMandant(), theClientDto.getLocUi()) + " 4" + "/"
										+ theClientDto.getSMandantenwaehrung(),
								getTextRespectUISpr("agstkl.ekaglieferant.positionlieferant.preis",
										theClientDto.getMandant(), theClientDto.getLocUi()) + " 5" + "/"
										+ theClientDto.getSMandantenwaehrung()

						},

						new int[] { -1, // diese Spalte wird ausgeblendet
								QueryParameters.FLR_BREITE_M, // Format 1234.123
								QueryParameters.FLR_BREITE_XS, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST },

						new String[] { "i_id", Facade.NICHT_SORTIERBAR, Facade.NICHT_SORTIERBAR,
								"flreinkaufsangebotposition.flrartikel.c_nr", Facade.NICHT_SORTIERBAR,
								Facade.NICHT_SORTIERBAR, "c_artikelnrlieferant", "i_lieferzeitinkw", "n_preis_menge1",
								"n_preis_menge2", "n_preis_menge3", "n_preis_menge4", "n_preis_menge5",
								"n_preis_menge1", "n_preis_menge2", "n_preis_menge3", "n_preis_menge4", "n_preis_menge5"

						}));
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
				return null;
			}
		}

		return super.getTableInfo();
	}
}
