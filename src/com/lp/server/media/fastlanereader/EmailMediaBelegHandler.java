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
package com.lp.server.media.fastlanereader;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.media.fastlanereader.generated.FLRMediaInbox;
import com.lp.server.media.fastlanereader.generated.FLRMediaStoreBeleg;
import com.lp.server.media.service.EmailMediaFac;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
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
 * Diese Klasse kuemmert sich den FLR-Partner.
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellungsdatum 18.10.04
 * </p>
 * 
 * <p>
 * 
 * @author $Author: robert $
 *         </p>
 * 
 * @version $Revision: 1.16 $ Date $Date: 2013/01/19 11:47:31 $
 */

public class EmailMediaBelegHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean bSuchenInklusiveKbez = true;

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {

		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = getLimit() ;
			int startIndex = Math.max(rowIndex.intValue() - (pageSize / 2), 0);
			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			String queryString = getFromClause() + buildWhereClause()
					+ buildOrderByClause();

			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];

			int row = 0;
			int col = 0;
			while (resultListIterator.hasNext()) {
				// flrjoin: 1
				FLRMediaStoreBeleg storebeleg = (FLRMediaStoreBeleg) resultListIterator.next();

				rows[row][col++] = storebeleg.getI_id();
				rows[row][col++] = storebeleg.getT_anlegen() ;
				rows[row][col++] = storebeleg.getFlrmedia().getT_emaildate() ;
				rows[row][col++] = storebeleg.getFlrmedia().getC_from() ;
				rows[row][col++] = storebeleg.getFlrmedia().getC_subject() ;
				rows[row][col++] = storebeleg.getC_belegartnr() ;
				rows[row][col++] = storebeleg.getBeleg_i_id() ;
				rows[row][col++] = storebeleg.getBelegposition_i_id() ;
				
				row++;
				col = 0;
			}
			
			result = new QueryResult(rows, getRowCount(), startIndex, endIndex,
					0);
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
			String queryString = "SELECT count(*) FROM FLRMediaStoreBeleg AS flrmediastorebeleg "
					+ " LEFT JOIN flrmediastorebeleg.flrmedia AS flremailmeta "
					+ buildWhereClause();
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

	/**
	 * builds the where clause of the HQL (Hibernate Query Language) statement
	 * using the current query.
	 * 
	 * @return the HQL where clause.
	 */
	private String buildWhereClause() {
		StringBuffer where = new StringBuffer("");

		try {
			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.PARAMETER_SUCHEN_INKLUSIVE_KBEZ);
			bSuchenInklusiveKbez = (java.lang.Boolean) parameter
					.getCWertAsObject();

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		if (getQuery() != null && getQuery().getFilterBlock() != null
				&& getQuery().getFilterBlock().filterKrit != null) {

			FilterBlock filterBlock = getQuery().getFilterBlock();
			FilterKriterium[] filterKriterien = getQuery().getFilterBlock().filterKrit;
			String booleanOperator = filterBlock.boolOperator;
			boolean filterAdded = false;
			for (int i = 0; i < filterKriterien.length; i++) {
				if (filterKriterien[i].isKrit) {
					if (filterAdded) {
						where.append(" " + booleanOperator);
					}

					if (filterKriterien[i].isBIgnoreCase()) {
						where.append(" lower("
								+ filterKriterien[i].kritName + ")");
					} else {
						where.append(" " + filterKriterien[i].kritName);
					}
					where.append(" " + filterKriterien[i].operator);
					if (filterKriterien[i].isBIgnoreCase()) {
						where.append(" "
								+ filterKriterien[i].value
										.toLowerCase());
					} else {
						where.append(" " + filterKriterien[i].value);
					}
					
					filterAdded = true;
//
//					if (bSuchenInklusiveKbez
//							&& filterKriterien[i].kritName
//									.equals(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1)) {
//						where.append(" (lower(partner."
//								+ filterKriterien[i].kritName + ")");
//						where.append(" " + filterKriterien[i].operator);
//						where.append(" "
//								+ filterKriterien[i].value.toLowerCase());
//						where.append(" OR lower(partner.c_name2vornamefirmazeile2)");
//						where.append(" " + filterKriterien[i].operator);
//						where.append(" "
//								+ filterKriterien[i].value.toLowerCase());
//						where.append(" OR lower(partner.c_kbez)");
//						where.append(" " + filterKriterien[i].operator);
//						where.append(" "
//								+ filterKriterien[i].value.toLowerCase() + ")");
//					} else if (bSuchenInklusiveKbez == false
//							&& filterKriterien[i].kritName
//									.equals(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1)) {
//						where.append(" (lower(partner."
//								+ filterKriterien[i].kritName + ")");
//						where.append(" " + filterKriterien[i].operator);
//						where.append(" "
//								+ filterKriterien[i].value.toLowerCase());
//						where.append(" OR lower(partner.c_name2vornamefirmazeile2)");
//						where.append(" " + filterKriterien[i].operator);
//						where.append(" "
//								+ filterKriterien[i].value.toLowerCase() + ")");
//					} else if (filterKriterien[i].kritName
//							.equals(PartnerFac.PARTNERQP1_ERWEITERTE_SUCHE)) {
//
//						where.append(" (replace(lower(partner.c_name1nachnamefirmazeile1),' ','')");
//						where.append(" " + filterKriterien[i].operator);
//						where.append(" "
//								+ filterKriterien[i].value.toLowerCase()
//										.replaceAll(" ", ""));
//						where.append(" OR replace(lower(partner.c_name2vornamefirmazeile2),' ','')");
//						where.append(" " + filterKriterien[i].operator);
//						where.append(" "
//								+ filterKriterien[i].value.toLowerCase()
//										.replaceAll(" ", ""));
//						where.append(" OR replace(lower(partner.c_name3vorname2abteilung),' ','')");
//						where.append(" " + filterKriterien[i].operator);
//						where.append(" "
//								+ filterKriterien[i].value.toLowerCase()
//										.replaceAll(" ", ""));
//						where.append(" OR replace(lower(partner.c_kbez),' ','')");
//						where.append(" " + filterKriterien[i].operator);
//						where.append(" "
//								+ filterKriterien[i].value.toLowerCase()
//										.replaceAll(" ", ""));
//						where.append(" OR replace(lower(partner.c_strasse),' ','')");
//						where.append(" " + filterKriterien[i].operator);
//						where.append(" "
//								+ filterKriterien[i].value.toLowerCase()
//										.replaceAll(" ", ""));
//
//						where.append(" OR replace(lower(partneransprechpartner.c_name1nachnamefirmazeile1),' ','')");
//						where.append(" " + filterKriterien[i].operator);
//						where.append(" "
//								+ filterKriterien[i].value.toLowerCase()
//										.replaceAll(" ", ""));
//						where.append(" OR replace(lower(partneransprechpartner.c_name2vornamefirmazeile2),' ','')");
//						where.append(" " + filterKriterien[i].operator);
//						where.append(" "
//								+ filterKriterien[i].value.toLowerCase()
//										.replaceAll(" ", ""));
//						where.append(" OR replace(lower(partneransprechpartner.c_name3vorname2abteilung),' ','')");
//						where.append(" " + filterKriterien[i].operator);
//						where.append(" "
//								+ filterKriterien[i].value.toLowerCase()
//										.replaceAll(" ", ""));
//
//						where.append(" OR replace(lower(partner.c_email),' ','')");
//						where.append(" " + filterKriterien[i].operator);
//						where.append(" "
//								+ filterKriterien[i].value.toLowerCase()
//										.replaceAll(" ", ""));
//						where.append(" OR replace(lower(partner.c_fax),' ','')");
//						where.append(" " + filterKriterien[i].operator);
//						where.append(" "
//								+ filterKriterien[i].value.toLowerCase()
//										.replaceAll(" ", ""));
//						where.append(" OR replace(lower(partner.c_telefon),' ','')");
//						where.append(" " + filterKriterien[i].operator);
//						where.append(" "
//								+ filterKriterien[i].value.toLowerCase()
//										.replaceAll(" ", ""));
//
//						where.append(" OR replace(lower(partneransprechpartner.c_email),' ','')");
//						where.append(" " + filterKriterien[i].operator);
//						where.append(" "
//								+ filterKriterien[i].value.toLowerCase()
//										.replaceAll(" ", ""));
//						where.append(" OR replace(lower(partneransprechpartner.c_fax),' ','')");
//						where.append(" " + filterKriterien[i].operator);
//						where.append(" "
//								+ filterKriterien[i].value.toLowerCase()
//										.replaceAll(" ", ""));
//						where.append(" OR replace(lower(partneransprechpartner.c_telefon),' ','')");
//						where.append(" " + filterKriterien[i].operator);
//						where.append(" "
//								+ filterKriterien[i].value.toLowerCase()
//										.replaceAll(" ", ""));
//						where.append(" OR replace(lower(ansprechpartnerset.c_handy),' ','')");
//						where.append(" " + filterKriterien[i].operator);
//						where.append(" "
//								+ filterKriterien[i].value.toLowerCase()
//										.replaceAll(" ", ""));
//
//						where.append(" OR replace(lower(cast(ansprechpartnerset.x_bemerkung as string)),' ','')");
//						where.append(" " + filterKriterien[i].operator);
//						where.append(" "
//								+ filterKriterien[i].value.toLowerCase()
//										.replaceAll(" ", ""));
//
//						where.append(" OR replace(lower(cast(partner.x_bemerkung as string)),' ','')");
//						where.append(" " + filterKriterien[i].operator);
//						where.append(" "
//								+ filterKriterien[i].value.toLowerCase()
//										.replaceAll(" ", "") + ")");
//
//					} else {
//
//						//
//
//						// ignorecase: 1 hier auf upper
//						if (filterKriterien[i].isBIgnoreCase()) {
//							where.append(" lower(partner."
//									+ filterKriterien[i].kritName + ")");
//						} else {
//							where.append(" partner."
//									+ filterKriterien[i].kritName);
//						}
//
//						where.append(" " + filterKriterien[i].operator);
//
//						// ignorecase: 2 hier auf upper
//						if (filterKriterien[i].isBIgnoreCase()) {
//							where.append(" "
//									+ filterKriterien[i].value.toLowerCase());
//						} else {
//							where.append(" " + filterKriterien[i].value);
//						}
//					}
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
		if (getQuery() != null) {
			SortierKriterium[] kriterien = getQuery().getSortKrit();
			boolean sortAdded = false;
			if (kriterien != null && kriterien.length > 0) {
				for (int i = 0; i < kriterien.length; i++) {
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
			} else {
				// no sort criteria found, add default sort
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(EmailMediaFac.FLR_MEDIASTOREBELEG_DATENEW 
						+ " DESC ");
				sortAdded = true;
			}
			if (orderBy.indexOf(EmailMediaFac.FLR_MEDIASTOREBELEG_MEDIA_ID) < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" " + EmailMediaFac.FLR_MEDIASTOREBELEG_MEDIA_ID + " ");
				sortAdded = true;
			}
			if (sortAdded) {
				orderBy.insert(0, " ORDER BY ");
			}
		}
		return orderBy.toString();
	}

	/**
	 * @return the from clause.
	 */
	private String getFromClause() {
		return "SELECT flrmediastorebeleg from FLRMediaStoreBeleg AS flrmediastorebeleg "
				+ " LEFT JOIN flrmediastorebeleg.flrmedia AS flremailmeta " ;
	}

	public QueryResult sort(SortierKriterium[] sortierKriterien,
			Object selectedId) throws EJBExceptionLP {

		getQuery().setSortKrit(sortierKriterien);

		QueryResult result = null;
		int rowNumber = 0;

		String queryString = null;
		if (selectedId != null && ((Integer) selectedId).intValue() >= 0) {
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();
				queryString = getFromClause() + buildWhereClause()
						+ buildOrderByClause();

				Query query = session.createQuery(queryString);

				ScrollableResults scrollableResult = query.scroll();
				if (scrollableResult != null) {
					scrollableResult.beforeFirst();
					while (scrollableResult.next()) {
						FLRMediaStoreBeleg flrmediabeleg = (FLRMediaStoreBeleg) scrollableResult.get(0) ;
//						Integer id = (Integer) scrollableResult.getInteger(0);
						Integer id = flrmediabeleg.getI_id() ;
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

		if (rowNumber < 0 || rowNumber >= getRowCount()) {
			rowNumber = 0;
		}

		result = getPageAt(new Integer(rowNumber));
		result.setIndexOfSelectedRow(rowNumber);
		return result;
	}

	public TableInfo getTableInfo() {

		if (super.getTableInfo() == null) {
			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();
			
			setTableInfo(new TableInfo(
					new Class[] { 
							Integer.class, Timestamp.class, Timestamp.class, String.class, String.class, String.class, Integer.class, Integer.class},
					new String[] {
							"i_id",
							getTextRespectUISpr("mediareference.date.new", mandantCNr, locUI),
							getTextRespectUISpr("mediareference.emaildate.from", mandantCNr, locUI),
							getTextRespectUISpr("mediareference.from", mandantCNr, locUI),
							getTextRespectUISpr("mediareference.subject", mandantCNr, locUI),
							getTextRespectUISpr("mediareference.belegart", mandantCNr, locUI),
							"beleg_i_id", 
							"pos_id"},
					new int[] {
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_S,
							QueryParameters.FLR_BREITE_S},
					new String[] {
							"i_id",
							EmailMediaFac.FLR_MEDIASTOREBELEG_DATENEW,
							EmailMediaFac.FLR_MEDIASTOREBELEG_EMAILMETA_DATEFROM,
							EmailMediaFac.FLR_MEDIASTOREBELEG_EMAILMETA_FROM,
							EmailMediaFac.FLR_MEDIASTOREBELEG_EMAILMETA_SUBJECT,
							EmailMediaFac.FLR_MEDIASTOREBELEG_CBELEGARTNR,
							EmailMediaFac.FLR_MEDIASTOREBELEG_BELEGID,
							EmailMediaFac.FLR_MEDIASTOREBELEG_BELEGPOSITIONID}));
		}
		return super.getTableInfo();
	}

	public PrintInfoDto getSDocPathAndPartner(Object key) {
		return null ;
	}

	public String getSTable() {
		return "MEDIA_INBOX";
	}
}
