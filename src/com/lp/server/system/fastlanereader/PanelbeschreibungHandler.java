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
package com.lp.server.system.fastlanereader;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.system.fastlanereader.generated.FLRPanelbeschreibung;
import com.lp.server.system.service.PanelFac;
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
 * Diese Klasse kuemmert sich Mandant-FLR.
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellungsdatum 08.03.05
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christoph $
 *         </p>
 * 
 * @version $Revision: 1.11 $ Date $Date: 2011/08/24 09:56:14 $
 */
public class PanelbeschreibungHandler extends UseCaseHandler {

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
			int pageSize = PanelbeschreibungHandler.PAGE_SIZE;
			int startIndex = Math.max(rowIndex.intValue() - (pageSize / 2), 0);
			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			String queryString = this.getFromClause() + this.buildWhereClause() + this.buildOrderByClause();

			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;
			int col = 0;

			String eigenschaftskennung = this.defaultFiltersForTableInfo[0].value.replace("'", "");

			while (resultListIterator.hasNext()) {
				FLRPanelbeschreibung mandant = (FLRPanelbeschreibung) resultListIterator.next();
				rows[row][col++] = mandant.getI_id();
				rows[row][col++] = mandant.getC_name();
				rows[row][col++] = mandant.getC_typ();
				rows[row][col++] = mandant.getI_gridx();
				rows[row][col++] = mandant.getI_gridy();

				if (PanelFac.PANEL_ARTIKELEIGENSCHAFTEN.equals(eigenschaftskennung)
						|| PanelFac.PANEL_CHARGENEIGENSCHAFTEN.equals(eigenschaftskennung)
						|| PanelFac.PANEL_ARTIKELTECHNIK.equals(eigenschaftskennung)) {

					if (mandant.getArtgru_i_id() != null) {
						rows[row][col++] = getArtikelFac()
								.artgruFindByPrimaryKey(mandant.getArtgru_i_id(), theClientDto).getBezeichnung(); // mandant.getArtgru_i_id();
					}

				} else if (PanelFac.PANEL_KUNDENEIGENSCHAFTEN.equals(eigenschaftskennung)) {

					if (mandant.getPartnerklasse_i_id() != null) {
						rows[row][col++] = getPartnerFac()
								.partnerklasseFindByPrimaryKey(mandant.getPartnerklasse_i_id(), theClientDto)
								.getBezeichnung();
					}

				} else if (PanelFac.PANEL_AUFTRAGSEIGENSCHAFTEN.equals(eigenschaftskennung)) {
					if (mandant.getKostenstelle_i_id() != null) {
						rows[row][col++] = getSystemFac().kostenstelleFindByPrimaryKey(mandant.getKostenstelle_i_id())
								.getCBez();
					} else {
						rows[row][col++] = null;
					}
					rows[row][col++] = mandant.getProjekttyp_c_nr();
				} else if (PanelFac.PANEL_PROJEKTEIGENSCHAFTEN.equals(eigenschaftskennung)) {
					if (mandant.getBereich_i_id() != null) {
						rows[row][col++] = getProjektServiceFac().bereichFindByPrimaryKey(mandant.getBereich_i_id())
								.getCBez();
					} else {
						rows[row][col++] = null;
					}
					rows[row][col++] = mandant.getProjekttyp_c_nr();
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
					if (filterKriterien[i].isBIgnoreCase()) {
						where.append(" lower( panelbeschreibung." + filterKriterien[i].kritName + ")");
					} else {
						where.append(" panelbeschreibung." + filterKriterien[i].kritName);
					}
					where.append(" " + filterKriterien[i].operator);
					if (filterKriterien[i].isBIgnoreCase()) {
						where.append(" " + filterKriterien[i].value.toLowerCase());
					} else {
						where.append(" " + filterKriterien[i].value);
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
							orderBy.append("panelbeschreibung." + kriterien[i].kritName);
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
				orderBy.append("panelbeschreibung.i_gridx ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("panelbeschreibung.i_gridx") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" panelbeschreibung.i_gridx ");
				sortAdded = true;
			}
			if (orderBy.indexOf("panelbeschreibung.i_gridy ") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" panelbeschreibung.i_gridy ");
				sortAdded = true;
			}
			if (sortAdded) {
				orderBy.insert(0, " ORDER BY ");
			}
		}
		return orderBy.toString();
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
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (HibernateException he) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, he);
				}
			}
		}
		return rowCount;
	}

	public TableInfo getTableInfo() {
		if (super.getTableInfo() == null) {
			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();

			TableInfo tableinfo = new TableInfo(
					new Class[] { Integer.class, String.class, String.class, Integer.class, Integer.class },
					new String[] { "i_id", getTextRespectUISpr("lp.name", mandantCNr, locUI),
							getTextRespectUISpr("lp.typ", mandantCNr, locUI),
							getTextRespectUISpr("lp.spalte", mandantCNr, locUI),
							getTextRespectUISpr("lp.zeile", mandantCNr, locUI) },
					new int[] { QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_L, QueryParameters.FLR_BREITE_M, QueryParameters.FLR_BREITE_M },
					new String[] { "i_id", PanelFac.FLR_PANELBESCHREIBUNG_C_NAME, PanelFac.FLR_PANELBESCHREIBUNG_C_TYP,
							PanelFac.FLR_PANELBESCHREIBUNG_I_GRIDX, PanelFac.FLR_PANELBESCHREIBUNG_I_GRIDY });

			String eigenschaftskennung = this.defaultFiltersForTableInfo[0].value.replace("'", "");

			if (PanelFac.PANEL_ARTIKELEIGENSCHAFTEN.equals(eigenschaftskennung)
					|| PanelFac.PANEL_CHARGENEIGENSCHAFTEN.equals(eigenschaftskennung)|| PanelFac.PANEL_ARTIKELTECHNIK.equals(eigenschaftskennung)) {
				tableinfo.spalteHinzufuegen(String.class, getTextRespectUISpr("lp.artikelgruppe", mandantCNr, locUI),
						QueryParameters.FLR_BREITE_L, PanelFac.FLR_PANELBESCHREIBUNG_ARTGRU_I_ID);
			} else if (PanelFac.PANEL_KUNDENEIGENSCHAFTEN.equals(eigenschaftskennung)) {
				tableinfo.spalteHinzufuegen(String.class, getTextRespectUISpr("lp.partnerklasse", mandantCNr, locUI),
						QueryParameters.FLR_BREITE_L, PanelFac.FLR_PANELBESCHREIBUNG_PARTNERKLASSE_I_ID);
			} else if (PanelFac.PANEL_AUFTRAGSEIGENSCHAFTEN.equals(eigenschaftskennung)) {
				tableinfo.spalteHinzufuegen(String.class, getTextRespectUISpr("lp.kostenstelle", mandantCNr, locUI),
						QueryParameters.FLR_BREITE_L, PanelFac.FLR_PANELBESCHREIBUNG_KOSTENSTELLE_I_ID);
				tableinfo.spalteHinzufuegen(String.class, getTextRespectUISpr("lp.panel.projekttyp", mandantCNr, locUI),
						QueryParameters.FLR_BREITE_L, PanelFac.FLR_PANELBESCHREIBUNG_PROJEKTTYP_C_NR);
			} else if (PanelFac.PANEL_PROJEKTEIGENSCHAFTEN.equals(eigenschaftskennung)) {
				tableinfo.spalteHinzufuegen(String.class, getTextRespectUISpr("lp.bereich", mandantCNr, locUI),
						QueryParameters.FLR_BREITE_L, PanelFac.FLR_PANELBESCHREIBUNG_BEREICH_I_ID);
				tableinfo.spalteHinzufuegen(String.class, getTextRespectUISpr("lp.panel.projekttyp", mandantCNr, locUI),
						QueryParameters.FLR_BREITE_L, PanelFac.FLR_PANELBESCHREIBUNG_PROJEKTTYP_C_NR);
			}

			setTableInfo(tableinfo);
		}

		return super.getTableInfo();
	}

	/**
	 * get the basic from clause for the HQL statement.
	 * 
	 * @return the from clause.
	 */
	private String getFromClause() {
		return "from FLRPanelbeschreibung panelbeschreibung ";
	}

	public QueryResult sort(SortierKriterium[] sortierKriterien, Object selectedId) throws EJBExceptionLP {

		this.getQuery().setSortKrit(sortierKriterien);

		QueryResult result = null;
		int rowNumber = 0;

		if (selectedId != null) {
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();
				String queryString = "select panelbeschreibung." + "i_id"
						+ " from FLRPanelbeschreibung panelbeschreibung " + this.buildWhereClause()
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
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
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

}
