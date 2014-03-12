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

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikellieferantstaffel;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
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
 * Hier wird die FLR Funktionalit&auml;t f&uuml;r den Artikellieferant implementiert. Pro
 * UseCase gibt es einen Handler.
 * </p>
 * <p>
 * Copright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum 2004-08-14
 * </p>
 * <p>
 * </p>
 * 
 * @author ck
 * @version 1.0
 */
public class ArtikellieferantstaffelHandler extends UseCaseHandler {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the size of the data page returned in QueryResult.
	 */

	/**
	 * The information needed for the kundes table.
	 */

	/**
	 * Konstruktor.
	 */
	public ArtikellieferantstaffelHandler() {
	}

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			int colCount = this.getTableInfo().getColumnClasses().length;
			int pageSize = this.PAGE_SIZE;
			int startIndex = Math.max(rowIndex.intValue() - (pageSize / 2), 0);
			int endIndex = startIndex + pageSize - 1;

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
				FLRArtikellieferantstaffel artikellieferantstaffel = (FLRArtikellieferantstaffel) resultListIterator
						.next();

				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKeySmall(
								artikellieferantstaffel
										.getFlrartikellieferant()
										.getArtikel_i_id(), theClientDto);

				rows[row][col++] = artikellieferantstaffel.getI_id();
				rows[row][col++] = Helper.rundeKaufmaennisch(
						artikellieferantstaffel.getN_menge(), 2);
				if (artikelDto.getEinheitCNrBestellung() != null) {

					if (Helper.short2boolean(artikelDto
							.getbBestellmengeneinheitInvers())) {
						if (artikelDto.getNUmrechnungsfaktor().doubleValue() != 0) {
							rows[row][col++] = Helper
									.rundeKaufmaennisch(
											artikellieferantstaffel
													.getN_menge()
													.divide(artikelDto
															.getNUmrechnungsfaktor(),
															2,
															BigDecimal.ROUND_HALF_EVEN),
											2);
						} else {
							rows[row][col++] = new BigDecimal(0);
						}
					} else {
						rows[row][col++] = Helper.rundeKaufmaennisch(
								artikellieferantstaffel.getN_menge().multiply(
										artikelDto.getNUmrechnungsfaktor()), 2);
					}

					// CK: Projekt 8019

				} else {
					rows[row][col++] = null;
				}

				rows[row][col++] = getLocaleFac()
						.rechneUmInAndereWaehrungZuDatum(
								artikellieferantstaffel
										.getFlrartikellieferant()
										.getN_einzelpreis(),
								artikellieferantstaffel
										.getFlrartikellieferant()
										.getFlrlieferant().getWaehrung_c_nr(),
								theClientDto.getSMandantenwaehrung(),
								new Date(System.currentTimeMillis()),
								theClientDto);

				rows[row][col++] = Helper.rundeKaufmaennisch(
						new java.math.BigDecimal(artikellieferantstaffel
								.getF_rabatt().doubleValue()), 2);
				rows[row][col++] = getLocaleFac()
						.rechneUmInAndereWaehrungZuDatum(
								artikellieferantstaffel.getN_nettopreis(),
								artikellieferantstaffel
										.getFlrartikellieferant()
										.getFlrlieferant().getWaehrung_c_nr(),
								theClientDto.getSMandantenwaehrung(),
								new Date(System.currentTimeMillis()),
								theClientDto);

				rows[row][col++] = artikelDto.getEinheitCNr().trim();
				if (artikelDto.getEinheitCNrBestellung() != null
						&& artikellieferantstaffel.getN_nettopreis() != null) {
					if (artikelDto.getNUmrechnungsfaktor().doubleValue() != 0) {

						if (Helper.short2boolean(artikelDto
								.getbBestellmengeneinheitInvers())) {

							rows[row][col++] = getLocaleFac()
									.rechneUmInAndereWaehrungZuDatum(
											artikellieferantstaffel
													.getN_nettopreis(),
											artikellieferantstaffel
													.getFlrartikellieferant()
													.getFlrlieferant()
													.getWaehrung_c_nr(),
											theClientDto
													.getSMandantenwaehrung(),
											new Date(System.currentTimeMillis()),
											theClientDto).multiply(
											artikelDto.getNUmrechnungsfaktor());
						} else {
							rows[row][col++] = getLocaleFac()
									.rechneUmInAndereWaehrungZuDatum(
											artikellieferantstaffel
													.getN_nettopreis(),
											artikellieferantstaffel
													.getFlrartikellieferant()
													.getFlrlieferant()
													.getWaehrung_c_nr(),
											theClientDto
													.getSMandantenwaehrung(),
											new Date(System.currentTimeMillis()),
											theClientDto).divide(
											artikelDto.getNUmrechnungsfaktor(),
											4, BigDecimal.ROUND_HALF_EVEN);
						}
					} else {
						rows[row][col++] = new BigDecimal(0);
					}
					rows[row][col++] = artikelDto.getEinheitCNrBestellung()
							.trim();
				} else {
					rows[row][col++] = null;
					rows[row][col++] = null;
				}

				rows[row][col++] = artikellieferantstaffel
						.getT_preisgueltigab();
				rows[row++][col++] = artikellieferantstaffel
						.getT_preisgueltigbis() == null ? null
						: artikellieferantstaffel.getT_preisgueltigbis();

				col = 0;
			}
			result = new QueryResult(rows, this.getRowCount(), startIndex,
					endIndex, 0);
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
			boolean filterAdded = false;

			for (int i = 0; i < filterKriterien.length; i++) {
				if (filterKriterien[i].isKrit) {
					if (filterAdded) {
						where.append(" " + booleanOperator);
					}
					filterAdded = true;
					where.append(" artikellieferantstaffel."
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
							orderBy.append("artikellieferantstaffel."
									+ kriterien[i].kritName);
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
				orderBy.append("artikellieferantstaffel."
						+ ArtikelFac.FLR_ARTIKELLIEFERANTSTAFFEL_N_MENGE
						+ " ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("artikellieferantstaffel."
					+ ArtikelFac.FLR_ARTIKELLIEFERANTSTAFFEL_N_MENGE) < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" artikellieferantstaffel."
						+ ArtikelFac.FLR_ARTIKELLIEFERANTSTAFFEL_N_MENGE + " ");
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
		return "from FLRArtikellieferantstaffel artikellieferantstaffel ";
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
				String queryString = "select artikellieferantstaffel.i_id from FLRArtikellieferantstaffel artikellieferantstaffel "
						+ this.buildWhereClause() + this.buildOrderByClause();
				Query query = session.createQuery(queryString);
				ScrollableResults scrollableResult = query.scroll();
				if (scrollableResult != null) {
					scrollableResult.beforeFirst();
					while (scrollableResult.next()) {
						Integer id = (Integer) scrollableResult.getInteger(0); // TYPE
						// OF
						// KEY
						// ATTRIBUTE
						// !
						// !
						// !
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
		try {
			if (super.getTableInfo() == null) {
				int iNachkommastellenPreis = getMandantFac()
						.getNachkommastellenPreisEK(theClientDto.getMandant());
				setTableInfo(new TableInfo(
						new Class[] {
								Object.class,
								java.math.BigDecimal.class,
								java.math.BigDecimal.class,
								super.getUIClassBigDecimalNachkommastellen(iNachkommastellenPreis),
								java.math.BigDecimal.class,
								super.getUIClassBigDecimalNachkommastellen(iNachkommastellenPreis),
								String.class,
								super.getUIClassBigDecimalNachkommastellen(iNachkommastellenPreis),
								String.class, java.util.Date.class,
								java.util.Date.class },
						new String[] {
								"PK",
								getTextRespectUISpr("lp.menge",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("artikel.bestmenge",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.einzelpreis",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.rabatt",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr(
										"bes.nettogesamtpreisminusrabatte",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								"",
								getTextRespectUISpr(
										"bes.nettogesamtpreisminusrabatte",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								"",
								getTextRespectUISpr("lp.gueltig_ab",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.gueltig_bis",
										theClientDto.getMandant(),
										theClientDto.getLocUi()) },
						new int[] {
								-1, // diese Spalte wird ausgeblendet
								QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_M,
								QueryParameters.FLR_BREITE_M,
								QueryParameters.FLR_BREITE_M,
								QueryParameters.FLR_BREITE_M,
								QueryParameters.FLR_BREITE_XS,
								QueryParameters.FLR_BREITE_M,
								QueryParameters.FLR_BREITE_XS,
								QueryParameters.FLR_BREITE_M,
								QueryParameters.FLR_BREITE_M }

						,
						new String[] {
								"PK",
								ArtikelFac.FLR_ARTIKELLIEFERANTSTAFFEL_N_MENGE,
								ArtikelFac.FLR_ARTIKELLIEFERANTSTAFFEL_N_MENGE,
								ArtikelFac.FLR_ARTIKELLIEFERANTSTAFFEL_FLRARTIKELLIEFERANT
										+ "."
										+ ArtikelFac.FLR_ARTIKELLIEFERANT_N_EINZELPREIS,
								ArtikelFac.FLR_ARTIKELLIEFERANTSTAFFEL_F_RABATT,
								ArtikelFac.FLR_ARTIKELLIEFERANTSTAFFEL_N_NETTOPREIS,
								Facade.NICHT_SORTIERBAR,
								ArtikelFac.FLR_ARTIKELLIEFERANTSTAFFEL_N_NETTOPREIS,
								Facade.NICHT_SORTIERBAR,
								ArtikelFac.FLR_ARTIKELLIEFERANTSTAFFEL_D_PREISGUELITGAB,
								ArtikelFac.FLR_ARTIKELLIEFERANTSTAFFEL_D_PREISGUELITGBIS }));

			}
			return super.getTableInfo();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}
}
