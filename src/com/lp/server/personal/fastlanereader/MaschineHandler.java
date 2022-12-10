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
package com.lp.server.personal.fastlanereader;

import java.awt.Color;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.personal.fastlanereader.generated.FLRMaschine;
import com.lp.server.personal.fastlanereader.generated.FLRMaschinenzeitdaten;
import com.lp.server.personal.fastlanereader.generated.FLRZeitdaten;
import com.lp.server.personal.service.IMaschineFLRData;
import com.lp.server.personal.service.MaschineFLRDataDto;
import com.lp.server.personal.service.MaschineHandlerFeature;
import com.lp.server.personal.service.MaschineQueryResult;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.util.Facade;
import com.lp.server.util.QueryFeature;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.UseCaseHandler;
import com.lp.server.util.fastlanereader.service.query.FilterBlock;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryParametersFeatures;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * Hier wird die FLR Funktionalit&auml;t f&uuml;r die Zeitmodelle implementiert.
 * Pro UseCase gibt es einen Handler.
 * </p>
 * <p>
 * Copright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum 2005-02-14
 * </p>
 * <p>
 * </p>
 * 
 * @author ck
 * @version 1.0
 */
public class MaschineHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Feature cachedFeature = null;

	private class Feature extends QueryFeature<IMaschineFLRData> {

		private boolean featurePersonalId;
		private MaschineFLRDataDto flrDataEntry;

		public Feature() {
			if (getQuery() instanceof QueryParametersFeatures) {
				initializeFeature((QueryParametersFeatures) getQuery());
			}
		}

		@Override
		protected void initializeFeature(QueryParametersFeatures query) {
			featurePersonalId = query.hasFeature(MaschineHandlerFeature.PERSONAL_ID);
		}

		@Override
		protected IMaschineFLRData[] createFlrData(int rows) {
			return new MaschineFLRDataDto[rows];
		}

		public boolean hasFeaturePersonalId() {
			return featurePersonalId;
		}

		protected void clearFlrDataEntry() {
			flrDataEntry = null;
		}

//		protected MaschineFLRDataDto getFlrDataEntry(int row) {
//			if(flrDataEntry == null) {
//				flrDataEntry = new MaschineFLRDataDto() ;
//				setFlrDataObject(row, flrDataEntry);
//			}
//			return flrDataEntry ;
//		}

		public void buildFeatureData(int row, Object[] o) {
			clearFlrDataEntry();
			if (hasFeaturePersonalId()) {
				FLRMaschinenzeitdaten flrMaschinenzeitdaten = (FLRMaschinenzeitdaten) o[1];
				flrDataEntry = flrMaschinenzeitdaten != null && flrMaschinenzeitdaten.getFlrpersonal_gestartet() != null
						? new MaschineFLRDataDto(flrMaschinenzeitdaten.getFlrpersonal_gestartet().getI_id(),
								flrMaschinenzeitdaten.getT_von().getTime(),
								flrMaschinenzeitdaten.getLossollarbeitsplan_i_id())
						: new MaschineFLRDataDto();
				setFlrDataObject(row, flrDataEntry);
			}
		}

	}

	private Feature getFeature() {
		if (cachedFeature == null) {
			cachedFeature = new Feature();
		}
		return cachedFeature;
	}

	/**
	 * the size of the data page returned in QueryResult.
	 * 
	 * @param rowIndex Integer
	 * @throws EJBExceptionLP
	 * @return QueryResult
	 */
	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {

		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			int colCount = this.getTableInfo().getColumnClasses().length;
			int pageSize = getLimit();
			int startIndex = getStartIndex(rowIndex, pageSize);
			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			String queryString = this.getFromClause() + this.buildWhereClause() + this.buildOrderByClause();

			Query query = session.createQuery(queryString);
			session = setFilter(session);

			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();

			Object[][] rows = new Object[resultList.size()][colCount + 4];
			int row = 0;
			int col = 0;

			getFeature().setFlrRowCount(rows.length);

			while (resultListIterator.hasNext()) {
				FLRMaschine flrmaschine;
				if (getFeature().hasFeaturePersonalId()) {
					Object[] o = (Object[]) resultListIterator.next();
					flrmaschine = (FLRMaschine) o[0];
					getFeature().buildFeatureData(row, o);
				} else {
					Object o = (Object) resultListIterator.next();
					flrmaschine = (FLRMaschine) o;
				}

				rows[row][col++] = flrmaschine.getI_id();
				if (Helper.short2boolean(flrmaschine.getB_manuelle_bedienung())) {
					rows[row][col++] = "M";
				} else {
					rows[row][col++] = null;
				}

				rows[row][col++] = flrmaschine.getC_inventarnummer();
				rows[row][col++] = flrmaschine.getC_bez();
				rows[row][col++] = flrmaschine.getFlrmaschinengruppe().getC_bez();
				rows[row][col++] = flrmaschine.getC_identifikationsnr();

				String starter = "";
				Timestamp tUm = null;

				if (Helper.short2boolean(flrmaschine.getB_manuelle_bedienung())) {
					Session s2 = FLRSessionFactory.getFactory().openSession();
					Query query2 = session.createQuery("FROM FLRZeitdaten m WHERE m.flrmaschine.i_id="
							+ flrmaschine.getI_id() + " ORDER BY m.t_zeit DESC");
					query2.setMaxResults(1);
					List<?> resultListSub = query2.list();
					if (resultListSub.size() > 0) {
						FLRZeitdaten m = (FLRZeitdaten) resultListSub.iterator().next();

						starter = m.getFlrpersonal().getFlrpartner().getC_name1nachnamefirmazeile1();

						if (m.getFlrpersonal().getFlrpartner().getC_name2vornamefirmazeile2() != null) {
							starter += " "
									+ m.getFlrpersonal().getFlrpartner().getC_name2vornamefirmazeile2();
						}
						tUm = new Timestamp(m.getT_zeit().getTime());
					}
					s2.close();
				} else {
					Session s2 = FLRSessionFactory.getFactory().openSession();
					Query query2 = session.createQuery("FROM FLRMaschinenzeitdaten m WHERE m.maschine_i_id="
							+ flrmaschine.getI_id() + " ORDER BY m.t_von DESC");
					query2.setMaxResults(1);
					List<?> resultListSub = query2.list();
					if (resultListSub.size() > 0) {
						FLRMaschinenzeitdaten m = (FLRMaschinenzeitdaten) resultListSub.iterator().next();

						starter = m.getFlrpersonal_gestartet().getFlrpartner().getC_name1nachnamefirmazeile1();

						if (m.getFlrpersonal_gestartet().getFlrpartner().getC_name2vornamefirmazeile2() != null) {
							starter += " "
									+ m.getFlrpersonal_gestartet().getFlrpartner().getC_name2vornamefirmazeile2();
						}
						tUm = new Timestamp(m.getT_von().getTime());
					}
					s2.close();
				}

				rows[row][col++] = starter;
				rows[row][col++] = tUm;

				if (Helper.short2boolean(flrmaschine.getB_versteckt())) {
					rows[row][col++] = Color.LIGHT_GRAY;
				} else {
					rows[row][col++] = null;
				}

				rows[row][col++] = flrmaschine.getMaschinengruppe_i_id();
				rows[row][col++] = flrmaschine.getFlrmaschinengruppe().getC_bez();
				rows[row][col++] = flrmaschine.getFlrmaschinengruppe().getC_kbez();
				rows[row][col++] = flrmaschine.getFlrmaschinengruppe().getI_sort();

				row++;
				col = 0;
			}

			if (getFeature().hasFeaturePersonalId()) {
				MaschineQueryResult maschineResult = new MaschineQueryResult(rows, getRowCount(), startIndex, endIndex,
						0);
				maschineResult.setFlrData(getFeature().getFlrData());
				result = maschineResult;
			} else {
				result = new QueryResult(rows, this.getRowCount(), startIndex, endIndex, 0);
			}
		} catch (HibernateException e) {
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
			session = setFilter(session);

			String queryString = "SELECT COUNT(*) " + getFromClause() + buildWhereClause();
			Query query = session.createQuery(queryString);
			List<?> rowCountResult = query.list();
			if (rowCountResult != null && rowCountResult.size() > 0) {
				rowCount = ((Long) rowCountResult.get(0)).longValue();
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			closeSession(session);
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
					if ("b_auslastungsanzeige".equals(filterKriterien[i].kritName)) {
						where.append(" maschine.flrmaschinengruppe.b_auslastungsanzeige = 1");
						continue;
					}
					if ("personal_i_id_gestartet".equals(filterKriterien[i].kritName)) {
						where.append(" maschinenzeitdaten.").append(filterKriterien[i].kritName).append(" = ")
								.append(filterKriterien[i].value);
						continue;
					}

					if (filterKriterien[i].isBIgnoreCase()) {
						where.append(" upper(maschine." + filterKriterien[i].kritName + ")");
					} else {
						String prefix = " maschine.";

						if ("fertigungsgruppe_i_id".equals(filterKriterien[i].kritName)) {
							prefix += "flrmaschinengruppe.";
						}
						where.append(prefix + filterKriterien[i].kritName);
					}
					where.append(" " + filterKriterien[i].operator);
					if (filterKriterien[i].isBIgnoreCase()) {
						where.append(" " + filterKriterien[i].value.toUpperCase());
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
					if (kriterien[i].isKrit) {
						if (sortAdded) {
							orderBy.append(", ");
						}
						sortAdded = true;
						orderBy.append("maschine." + kriterien[i].kritName);
						orderBy.append(" ");
						orderBy.append(kriterien[i].value);
					}
				}
			} else {
				// no sort criteria found, add default sort
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append("maschine.c_inventarnummer ASC ");

				sortAdded = true;
			}
			if (orderBy.indexOf("maschine.c_inventarnummer") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" maschine.c_inventarnummer ");
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
		return " FROM FLRMaschine AS maschine " + (getFeature().hasFeaturePersonalId()
				? " LEFT OUTER JOIN maschine.maschinenzeitdatenset AS maschinenzeitdaten "
				: "");
	}

	public QueryResult sort(SortierKriterium[] sortierKriterien, Object selectedId) throws EJBExceptionLP {

		getQuery().setSortKrit(sortierKriterien);

		QueryResult result = null;
		int rowNumber = 0;

		if (selectedId != null) {
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();
				session = setFilter(session);
				String queryString = getFromClause() + buildWhereClause() + buildOrderByClause();

				Query query = session.createQuery(queryString);
				ScrollableResults scrollableResult = query.scroll();
				if (scrollableResult != null) {
					scrollableResult.beforeFirst();
					while (scrollableResult.next()) {
						FLRMaschine maschine = (FLRMaschine) scrollableResult.get(0);
						Integer id = maschine.getI_id();
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
					new Class[] { Integer.class, String.class, String.class, String.class, String.class, String.class,
							String.class, Timestamp.class, Color.class },
					new String[] { "Id",
							getTextRespectUISpr("pers.maschinenliste.manuellebedienung", mandantCNr, locUI),
							getTextRespectUISpr("pers.inventarnummer", mandantCNr, locUI),
							getTextRespectUISpr("lp.bezeichnung", mandantCNr, locUI),
							getTextRespectUISpr("lp.maschinengruppe", mandantCNr, locUI),
							getTextRespectUISpr("pers.zeiterfassung.identifikationsnr", mandantCNr, locUI),

							getTextRespectUISpr("lp.zuletztgestartetvon", mandantCNr, locUI),
							getTextRespectUISpr("lp.um", mandantCNr, locUI), "" }

					, new int[] { -1, // diese Spalte wird ausgeblendet
							QueryParameters.FLR_BREITE_XXS, 15, QueryParameters.FLR_BREITE_SHARE_WITH_REST, 15, 15,

							QueryParameters.FLR_BREITE_L, QueryParameters.FLR_BREITE_L, 1 }

					,
					new String[] { "i_id", "b_manuelle_bedienung", ZeiterfassungFac.FLR_MASCHINE_C_INVENTARNUMMER,
							"c_bez", ZeiterfassungFac.FLR_MASCHINE_FLR_MASCHINENGRUPPE + ".c_bez",
							ZeiterfassungFac.FLR_MASCHINE_C_IDENTIFIKATIONSNR,

							Facade.NICHT_SORTIERBAR, Facade.NICHT_SORTIERBAR, "" }));

		}
		return super.getTableInfo();
	}

	public Session setFilter(Session session) {
		session = super.setFilter(session);

		if (getFeature().hasFeaturePersonalId()) {
			session.enableFilter("filterTBis");
		}

		return session;
	}
}
