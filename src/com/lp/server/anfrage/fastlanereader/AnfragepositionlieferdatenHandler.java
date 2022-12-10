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
package com.lp.server.anfrage.fastlanereader;

import java.awt.Color;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.anfrage.fastlanereader.generated.FLRAnfragepositionlieferdaten;
import com.lp.server.anfrage.service.AnfrageServiceFac;
import com.lp.server.anfrage.service.AnfragepositionFac;
import com.lp.server.artikel.service.ArtikellieferantDto;
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
 * FLR fuer ANF_ANFRAGEPOSITIONLIEFERDATEN
 * </p>
 * <p>
 * Copright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum 17.06.05
 * </p>
 * <p>
 * </p>
 * 
 * @author Uli Walch
 * @version 1.0
 */

public class AnfragepositionlieferdatenHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String FLR_ANFRAGEPOSITIONLIEFERDATEN = "flranfragepositionlieferdaten.";
	public static final String FLR_ANFRAGEPOSITIONLIEFERDATEN_FROM_CLAUSE = " from FLRAnfragepositionlieferdaten flranfragepositionlieferdaten ";

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = AnfrageHandler.PAGE_SIZE;
			int startIndex = Math.max(rowIndex.intValue() - (pageSize / 2), 0);
			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			String queryString = this.getFromClause() + this.buildWhereClause() + this.buildOrderByClause();
			// myLogger.info("HQL= " + queryString);
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;
			int col = 0;

			while (resultListIterator.hasNext()) {
				FLRAnfragepositionlieferdaten position = (FLRAnfragepositionlieferdaten) resultListIterator.next();
				rows[row][col++] = position.getI_id();
				rows[row][col++] = position.getN_anliefermenge();
				rows[row][col++] = position.getFlranfrageposition().getFlrartikel().getEinheit_c_nr() == null ? ""
						: position.getFlranfrageposition().getFlrartikel().getEinheit_c_nr().trim();
				String sIdentnummer = "";
				String ref = "";
				if (position.getFlranfrageposition().getFlrartikel() != null) {
					sIdentnummer = position.getFlranfrageposition().getFlrartikel().getC_nr();
					ref = position.getFlranfrageposition().getFlrartikel().getC_referenznr();
				}
				rows[row][col++] = sIdentnummer;

				if (bReferenznummerInPositionen) {
					rows[row][col++] = ref;
				}

				String cArtikelbezeichnung = null;

				Boolean bArtikellieferantVorhanden = false;

				if (position.getFlranfrageposition().getAnfragepositionart_c_nr()
						.equals(AnfrageServiceFac.ANFRAGEPOSITIONART_IDENT)) {
					// die sprachabhaengig Artikelbezeichnung anzeigen
					cArtikelbezeichnung = getArtikelFac().formatArtikelbezeichnungEinzeiligOhneExc(
							position.getFlranfrageposition().getFlrartikel().getI_id(), theClientDto.getLocUi());

					ArtikellieferantDto alDto = getArtikelFac().getArtikelEinkaufspreis(
							position.getFlranfrageposition().getFlrartikel().getI_id(),
							position.getFlranfrageposition().getFlranfrage().getFlrlieferant().getI_id(),
							BigDecimal.ONE,

							position.getFlranfrageposition().getFlranfrage().getWaehrung_c_nr_anfragewaehrung(),
							new java.sql.Date(
									position.getFlranfrageposition().getFlranfrage().getT_belegdatum().getTime()),
							theClientDto);

					if (alDto != null) {
						bArtikellieferantVorhanden = true;
					}

				} else {
					if (position.getFlranfrageposition().getC_bez() != null) {
						cArtikelbezeichnung = position.getFlranfrageposition().getC_bez();
					}
				}

				rows[row][col++] = cArtikelbezeichnung;
				rows[row][col++] = position.getN_nettogesamtpreis();

				rows[row][col++] = bArtikellieferantVorhanden;

				if (position.getFlranfrageposition().getAnfrageposition_i_id_zugehoerig() != null) {
					rows[row][col++] = new Color(89, 188, 41);
				}

				row++;
				col = 0;
			}
			result = new QueryResult(rows, this.getRowCount(), startIndex, endIndex, 0);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			try {
				session.close();
			} catch (HibernateException he) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, he);
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
			String queryString = "select count(*) " + this.getFromClause() + this.buildWhereClause();
			Query query = session.createQuery(queryString);
			List<?> rowCountResult = query.list();
			if (rowCountResult != null && rowCountResult.size() > 0) {
				rowCount = ((Long) rowCountResult.get(0)).longValue();
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (HibernateException he) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HIBERNATE, he);
				}
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
					where.append(" " + FLR_ANFRAGEPOSITIONLIEFERDATEN + filterKriterien[i].kritName);
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
							orderBy.append(FLR_ANFRAGEPOSITIONLIEFERDATEN + kriterien[i].kritName);
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
				orderBy.append(FLR_ANFRAGEPOSITIONLIEFERDATEN)
						// default Sortierung nach iSort
						.append(AnfragepositionFac.FLR_ANFRAGEPOSITIONLIEFERDATEN_FLRANFRAGEPOSITION).append(".")
						.append(AnfragepositionFac.FLR_ANFRAGEPOSITION_I_SORT).append(" ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf(FLR_ANFRAGEPOSITIONLIEFERDATEN + "i_id") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" ").append(FLR_ANFRAGEPOSITIONLIEFERDATEN).append("i_id").append(" ");
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
		return FLR_ANFRAGEPOSITIONLIEFERDATEN_FROM_CLAUSE;
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
				String queryString = "select " + FLR_ANFRAGEPOSITIONLIEFERDATEN + "i_id"
						+ FLR_ANFRAGEPOSITIONLIEFERDATEN_FROM_CLAUSE + this.buildWhereClause()
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
				try {
					session.close();
				} catch (HibernateException he) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, he);
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

	private TableColumnInformation createColumnInformation(String mandant, Locale locUi) {
		TableColumnInformation columns = new TableColumnInformation();
		try {
			String mandantCNr = theClientDto.getMandant();

			int iNachkommastellenMenge = getMandantFac().getNachkommastellenMenge(mandantCNr);
			int iNachkommastellenPreis = getMandantFac().getNachkommastellenPreisEK(mandantCNr);

			columns.add("i_id", Integer.class, "i_id", QueryParameters.FLR_BREITE_SHARE_WITH_REST, "i_id");

			columns.add("anf.anliefermenge", super.getUIClassBigDecimalNachkommastellen(iNachkommastellenMenge),
					getTextRespectUISpr("anf.anliefermenge", mandant, locUi), QueryParameters.FLR_BREITE_M,
					AnfragepositionFac.FLR_ANFRAGEPOSITIONLIEFERDATEN_N_ANLIEFERMENGE);
			columns.add("lp.einheit", String.class, getTextRespectUISpr("lp.einheit", mandant, locUi),
					QueryParameters.FLR_BREITE_XS,
					AnfragepositionFac.FLR_ANFRAGEPOSITIONLIEFERDATEN_FLRANFRAGEPOSITION + "."
							+ AnfragepositionFac.FLR_ANFRAGEPOSITION_FLRARTIKEL + "."
							+ AnfragepositionFac.FLR_ANFRAGEPOSITION_EINHEIT_C_NR);

			columns.add("lp.ident", String.class, getTextRespectUISpr("lp.ident", mandant, locUi), getUIBreiteIdent(),
					AnfragepositionFac.FLR_ANFRAGEPOSITIONLIEFERDATEN_FLRANFRAGEPOSITION + "."
							+ AnfragepositionFac.FLR_ANFRAGEPOSITION_FLRARTIKEL + ".c_nr");

			if (bReferenznummerInPositionen) {
				columns.add("lp.referenznummer", String.class, getTextRespectUISpr("lp.referenznummer", mandant, locUi),
						QueryParameters.FLR_BREITE_XM,
						AnfragepositionFac.FLR_ANFRAGEPOSITIONLIEFERDATEN_FLRANFRAGEPOSITION + "."
								+ AnfragepositionFac.FLR_ANFRAGEPOSITION_FLRARTIKEL + ".c_referenznr");
			}
			columns.add("lp.bezeichnung", String.class, getTextRespectUISpr("lp.bezeichnung", mandant, locUi),
					QueryParameters.FLR_BREITE_SHARE_WITH_REST, Facade.NICHT_SORTIERBAR);
			columns.add("anf.anlieferpreis", super.getUIClassBigDecimalNachkommastellen(iNachkommastellenPreis),
					getTextRespectUISpr("anf.anlieferpreis", mandant, locUi), QueryParameters.FLR_BREITE_M,
					AnfragepositionFac.FLR_ANFRAGEPOSITIONLIEFERDATEN_FLRANFRAGEPOSITION + "."
							+ AnfragepositionFac.FLR_ANFRAGEPOSITION_N_RICHTPREIS);
			columns.add("anfr.artikelieferant.bereitsvorhanden.short", Boolean.class,
					getTextRespectUISpr("anfr.artikelieferant.bereitsvorhanden.short", mandant, locUi),
					QueryParameters.FLR_BREITE_S, Facade.NICHT_SORTIERBAR,
					getTextRespectUISpr("anfr.artikelieferant.bereitsvorhanden.tooltip", theClientDto.getMandant(),
							theClientDto.getLocUi()));

			columns.add("Color", Color.class, "", 1, Facade.NICHT_SORTIERBAR);

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

}
