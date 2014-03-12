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
package com.lp.server.partner.fastlanereader;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.Icon;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeLieferant;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
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
 * Diese Klasse kuemmert sich um den Lieferanten-FLR.
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellungsdatum 2005-01-10
 * </p>
 * 
 * <p>
 * 
 * @author $Author: robert $
 *         </p>
 * 
 * @version $Revision: 1.17 $ Date $Date: 2013/01/19 11:47:31 $
 */

public class LieferantHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean bSuchenInklusiveKbez = true;
	public static final String FLR_LIEFERANT = "flrlieferant.";
	public static final String FLR_LIEFERANT_FROM_CLAUSE = " from FLRLieferant flrlieferant ";

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {

		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = LieferantHandler.PAGE_SIZE;
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
				Object[] o = (Object[]) resultListIterator.next();

				rows[row][col++] = o[0];
				rows[row][col++] = o[1];
				rows[row][col++] = o[2];
				rows[row][col++] = o[3];
				if (o[4] != null) {
					rows[row][col++] = LocaleFac.STATUS_GESPERRT;
				} else {
					rows[row][col++] = null;
				}
				rows[row][col++] = o[5];
				rows[row][col++] = o[6];
				rows[row][col++] = o[7];
				rows[row][col++] = o[8];
				row++;
				col = 0;
			}
			result = new QueryResult(rows, this.getRowCount(), startIndex,
					endIndex, 0);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
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
			String queryString = "SELECT count(distinct lieferant.i_id) FROM FLRLieferant AS lieferant "
					+ " left join lieferant.flrpartner.flrlandplzort as flrlandplzort "
					+ " left join lieferant.flrpartner.flrlandplzort.flrort as flrort "
					+ " left join lieferant.flrkonto as flrkonto "
					+ " left join lieferant.flrpartner.flrlandplzort.flrland as flrland "

					+ " LEFT OUTER JOIN lieferant.flrpartner.ansprechpartner AS ansprechpartnerset "
					+ " LEFT OUTER JOIN ansprechpartnerset.flrpartneransprechpartner AS partneransprechpartner "
					

					+ buildWhereClause();

			Query query = session.createQuery(queryString);
			List<?> rowCountResult = query.list();
			if (rowCountResult != null && rowCountResult.size() > 0) {
				rowCount = ((Long) rowCountResult.get(0)).longValue();
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(e);
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (HibernateException he) {
					throw new EJBExceptionLP(he);
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
					if (bSuchenInklusiveKbez
							&& filterKriterien[i].kritName
									.equals(LieferantFac.FLR_PARTNER
											+ "."
											+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1)) {
						where.append(" (lower(lieferant."
								+ filterKriterien[i].kritName + ")");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase());
						where.append(" OR lower(lieferant.flrpartner.c_name2vornamefirmazeile2)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase());
						where.append(" OR lower(lieferant.flrpartner.c_kbez)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase() + ")");
					} else if (bSuchenInklusiveKbez == false
							&& filterKriterien[i].kritName
									.equals(LieferantFac.FLR_PARTNER
											+ "."
											+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1)) {
						where.append(" (lower(lieferant."
								+ filterKriterien[i].kritName + ")");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase());
						where.append(" OR lower(lieferant.flrpartner.c_name2vornamefirmazeile2)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase() + ")");
					} else if (filterKriterien[i].kritName
							.equals(PartnerFac.PARTNERQP1_ERWEITERTE_SUCHE)) {

						where.append(" (replace(lower(lieferant.flrpartner.c_name1nachnamefirmazeile1),' ','')");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase()
										.replaceAll(" ", ""));
						where.append(" OR replace(lower(lieferant.flrpartner.c_name2vornamefirmazeile2),' ','')");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase()
										.replaceAll(" ", ""));
						where.append(" OR replace(lower(lieferant.flrpartner.c_name3vorname2abteilung),' ','')");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase()
										.replaceAll(" ", ""));
						where.append(" OR replace(lower(lieferant.flrpartner.c_kbez),' ','')");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase()
										.replaceAll(" ", ""));
						where.append(" OR replace(lower(lieferant.flrpartner.c_strasse),' ','')");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase()
										.replaceAll(" ", ""));

						where.append(" OR replace(lower(partneransprechpartner.c_name1nachnamefirmazeile1),' ','')");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase()
										.replaceAll(" ", ""));
						where.append(" OR replace(lower(partneransprechpartner.c_name2vornamefirmazeile2),' ','')");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase()
										.replaceAll(" ", ""));
						where.append(" OR replace(lower(partneransprechpartner.c_name3vorname2abteilung),' ','')");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase()
										.replaceAll(" ", ""));

						where.append(" OR replace(lower(lieferant.flrpartner.c_email),' ','')");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase()
										.replaceAll(" ", ""));
						where.append(" OR replace(lower(lieferant.flrpartner.c_fax),' ','')");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase()
										.replaceAll(" ", ""));
						where.append(" OR replace(lower(lieferant.flrpartner.c_telefon),' ','')");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase()
										.replaceAll(" ", ""));
						where.append(" OR replace(lower(ansprechpartnerset.c_handy),' ','')");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase()
										.replaceAll(" ", ""));

						where.append(" OR replace(lower(ansprechpartnerset.c_email),' ','')");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase()
										.replaceAll(" ", ""));
						where.append(" OR replace(lower(ansprechpartnerset.c_fax),' ','')");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase()
										.replaceAll(" ", ""));
						where.append(" OR replace(lower(ansprechpartnerset.c_telefon),' ','')");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase()
										.replaceAll(" ", ""));

						where.append(" OR replace(lower(cast(ansprechpartnerset.x_bemerkung as string)),' ','')");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase()
										.replaceAll(" ", ""));

						where.append(" OR replace(lower(cast(lieferant.x_kommentar as string)),' ','')");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase()
										.replaceAll(" ", ""));

						where.append(" OR replace(lower(lieferant.c_hinweisintern),' ','')");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase()
										.replaceAll(" ", ""));
						where.append(" OR replace(lower(lieferant.c_hinweisextern),' ','')");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase()
										.replaceAll(" ", ""));

						where.append(" OR replace(lower(cast(lieferant.flrpartner.x_bemerkung as string)),' ','')");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase()
										.replaceAll(" ", "") + ")");

					}

					else {

						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" lower(lieferant."
									+ filterKriterien[i].kritName + ")");
						} else {
							where.append(" lieferant."
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
							orderBy.append("lieferant." + kriterien[i].kritName);
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
				orderBy.append("lieferant." + LieferantFac.FLR_PARTNER + "."
						+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1
						+ " ASC ");
			}
			if (orderBy.indexOf("lieferant.i_id") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" lieferant.i_id ");
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
		return "SELECT distinct lieferant.i_id, lieferant.flrpartner.c_name1nachnamefirmazeile1,lieferant.flrpartner.c_kbez,lieferant.flrpartner.c_telefon,lieferant.t_bestellsperream , lieferant.flrpartner.flrlandplzort.flrland.c_lkz,lieferant.flrpartner.flrlandplzort.c_plz,lieferant.flrpartner.flrlandplzort.flrort.c_name, lieferant.flrkonto.c_nr from FLRLieferant as lieferant "
				+ " left join lieferant.flrpartner.flrlandplzort as flrlandplzort "
				+ " left join lieferant.flrpartner.flrlandplzort.flrort as flrort "
				+ " left join lieferant.flrkonto as flrkonto "
				+ " left join lieferant.flrpartner.flrlandplzort.flrland as flrland "

				+ " LEFT OUTER JOIN lieferant.flrpartner.ansprechpartner AS ansprechpartnerset "
				+ " LEFT OUTER JOIN ansprechpartnerset.flrpartneransprechpartner AS partneransprechpartner ";
			
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
				String queryString = getFromClause() + buildWhereClause()
						+ buildOrderByClause();

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

	public TableInfo getTableInfo() {

		if (super.getTableInfo() == null) {
			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();
			setTableInfo(new TableInfo(
					new Class[] { Integer.class, String.class, String.class,
							String.class, Icon.class, String.class,
							String.class, String.class, String.class },
					new String[] {
							"i_id",
							getTextRespectUISpr("lp.firma", mandantCNr, locUI),
							getTextRespectUISpr("lp.kurzbezeichnung",
									mandantCNr, locUI),
							getTextRespectUISpr("lp.telefon", mandantCNr, locUI),
							"",
							getTextRespectUISpr("lp.lkz", mandantCNr, locUI),
							getTextRespectUISpr("lp.plz", mandantCNr, locUI),
							getTextRespectUISpr("lp.ort", mandantCNr, locUI),
							getTextRespectUISpr("lp.kreditoren", mandantCNr,
									locUI) }, new int[] {
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_XM,
							QueryParameters.FLR_BREITE_XM,
							QueryParameters.FLR_BREITE_S,
							QueryParameters.FLR_BREITE_S,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST },
					new String[] {
							"i_id",
							LieferantFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
							LieferantFac.FLR_PARTNER + "."
									+ PartnerFac.FLR_PARTNER_C_KBEZ,
							Facade.NICHT_SORTIERBAR,
							LieferantFac.FLR_LIEFERANT_TBESTELLSPERREAM,
							LieferantFac.FLR_PARTNER_LANDCLKZ,
							LieferantFac.FLR_PARTNER_LANDPLZORT_PLZ,
							LieferantFac.FLR_PARTNER_LANDPLZORT_ORT_NAME,
							KundeFac.FLR_KONTO + ".c_nr" }));

		}
		return super.getTableInfo();

	}

	public String getQueryHandler() {
		return getFromClause() + "" + buildWhereClause();
	}

	/**
	 * Die Liste aller Lieferanten holen. Dabei werden nicht die gesamten
	 * Datensaetze benoetigt, sondern nur die IDs.
	 * 
	 * @return String
	 */
	public static String getQueryLieferantenliste() {
		StringBuffer buff = new StringBuffer();

		buff.append("select ").append(FLR_LIEFERANT)
				.append("i_id")
				// nur die
				// IDs holen
				.append(FLR_LIEFERANT_FROM_CLAUSE).append("ORDER BY ")
				.append(FLR_LIEFERANT).append("mandant_c_nr ASC");

		return buff.toString();
	}

	/**
	 * Ueber diese Methode kann eine Facade auf eine Lieferantenliste zugreifen.
	 * 
	 * @param cQueryStringI
	 *            der Hibernate Query String
	 * @return List<?> die Liste der Lieferanten.
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public static List<?> getFLRLieferantenliste(String cQueryStringI)
			throws EJBExceptionLP {

		List<?> resultList = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;

		try {
			session = factory.openSession();
			Query query = session.createQuery(cQueryStringI);
			resultList = query.list();
		} catch (Exception t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, t);
		} finally {
			try {
				session.close();
			} catch (HibernateException he) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HIBERNATE, he);
			}
		}

		return resultList;
	}

	public PrintInfoDto getSDocPathAndPartner(Object key) {
		LieferantDto lieferantDto = null;
		PartnerDto partnerDto = null;
		try {
			lieferantDto = getLieferantFac().lieferantFindByPrimaryKey(
					(Integer) key, theClientDto);
			partnerDto = getPartnerFac().partnerFindByPrimaryKey(
					lieferantDto.getPartnerIId(), theClientDto);
			lieferantDto.setPartnerDto(partnerDto);
		} catch (Exception e) {
			// Nicht gefunden
		}
		if (partnerDto != null) {
//			String sLieferant = partnerDto.getCName1nachnamefirmazeile1()
//					.replace("/", ".").trim();
//			if (partnerDto.getCName2vornamefirmazeile2() != null) {
//				sLieferant = sLieferant
//						+ " "
//						+ partnerDto.getCName2vornamefirmazeile2()
//								.replace("/", ".").trim();
//			}
//			String sPath = JCRDocFac.HELIUMV_NODE + "/"
//					+ theClientDto.getMandant() + "/"
//					+ LocaleFac.BELEGART_LIEFERANT.trim() + "/"
//					+ LocaleFac.BELEGART_LIEFERANT.trim() + "/" + sLieferant;
			DocPath docPath = new DocPath(new DocNodeLieferant(lieferantDto, partnerDto));
			return new PrintInfoDto(docPath, null, getSTable());
		} else {
			return null;
		}
	}

	public String getSTable() {
		return "LIEFERANT";
	}

}
