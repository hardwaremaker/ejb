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
package com.lp.server.partner.fastlanereader;

import java.awt.Color;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeAgStueckliste;
import com.lp.server.system.jcr.service.docnode.DocNodePartner;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
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

public class PartnerHandler extends UseCaseHandler {

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
			int pageSize = PAGE_SIZE;
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
				Object[] o = (Object[]) resultListIterator.next();

				rows[row][col++] = o[0];
				rows[row][col++] = o[1];
				rows[row][col++] = o[2];
				rows[row][col++] = o[3];
				rows[row][col++] = o[4];
				rows[row][col++] = o[5];
				rows[row][col++] = o[6];
				rows[row][col++] = o[7];

				if (Helper.short2boolean((Short) o[8])) {
					rows[row][col++] = Color.LIGHT_GRAY;
				}

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
			String queryString = "SELECT count(distinct partner.i_id) FROM FLRPartner AS partner "
					+ " LEFT JOIN partner.flrlandplzort AS flrlandplzort "
					+ " LEFT JOIN partner.flrlandplzort.flrort AS flrort "
					+ " LEFT JOIN partner.flrlandplzort.flrland AS flrland "
					+ " LEFT OUTER JOIN partner.ansprechpartner AS ansprechpartnerset "
					+ " LEFT OUTER JOIN ansprechpartnerset.flrpartneransprechpartner AS partneransprechpartner "

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
					filterAdded = true;

					if (bSuchenInklusiveKbez
							&& filterKriterien[i].kritName
									.equals(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1)) {
						where.append(" (lower(partner."
								+ filterKriterien[i].kritName + ")");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase());
						where.append(" OR lower(partner.c_name2vornamefirmazeile2)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase());
						where.append(" OR lower(partner.c_kbez)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase() + ")");
					} else if (bSuchenInklusiveKbez == false
							&& filterKriterien[i].kritName
									.equals(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1)) {
						where.append(" (lower(partner."
								+ filterKriterien[i].kritName + ")");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase());
						where.append(" OR lower(partner.c_name2vornamefirmazeile2)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase() + ")");
					} else if (filterKriterien[i].kritName
							.equals(PartnerFac.PARTNERQP1_TELEFONNUMMERN_SUCHE)) {
						
						
						where.append(" replace(replace(replace(replace(replace(coalesce(replace(partner.c_telefon,' ',''),'') ||' '|| coalesce(replace(partner.c_fax,' ',''),'') ||' '|| coalesce(replace(ansprechpartnerset.c_handy,' ',''),'') ||' '|| coalesce(replace(partneransprechpartner.c_fax,' ',''),'') ||' '|| coalesce(replace(partneransprechpartner.c_telefon,' ',''),'') ,'(','') ,')','') ,'+','') ,'-','') ,'/','')");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase()
										.replaceAll(" ", ""));
						
					} else if (filterKriterien[i].kritName
							.equals(PartnerFac.PARTNERQP1_ERWEITERTE_SUCHE)) {

						
					
						
						
						String suchstring = "  coalesce(partner.c_name1nachnamefirmazeile1,'')||' '||coalesce(partner.c_name2vornamefirmazeile2,'')||' '||coalesce(partner.c_name3vorname2abteilung,'')||' '||coalesce(partner.c_kbez,'')";
							suchstring += "||' '||coalesce(partner.c_strasse,'')||' '||coalesce(partneransprechpartner.c_name1nachnamefirmazeile1,'')||' '||coalesce(partneransprechpartner.c_name2vornamefirmazeile2,'')";
						
							suchstring += "||' '||coalesce(partneransprechpartner.c_name3vorname2abteilung,'')||' '||coalesce(partner.c_email,'')||' '||coalesce(partner.c_fax,'')";
						
							suchstring += "||' '||coalesce(partner.c_telefon,'')||' '||coalesce(ansprechpartnerset.c_handy,'')||' '||coalesce(ansprechpartnerset.c_email,'')";
							suchstring += "||' '||coalesce(cast(ansprechpartnerset.x_bemerkung as string),'')||' '||coalesce(cast(partner.x_bemerkung as string),'')";
							

						String[] teile = filterKriterien[i].value.toLowerCase()
								.split(" ");
						

						for (int p = 0; p < teile.length; p++) {

							if (teile[p].startsWith("-")) {
								where.append(" NOT ");

								teile[p] = teile[p].substring(1);

							}

							where.append(" lower(" + suchstring + ") like '%"
									+ teile[p].toLowerCase() + "%'");
							if (p < teile.length - 1) {
								where.append(" AND ");
							}
						}

						

					} else {

						//

						// ignorecase: 1 hier auf upper
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" lower(partner."
									+ filterKriterien[i].kritName + ")");
						} else {
							where.append(" partner."
									+ filterKriterien[i].kritName);
						}

						where.append(" " + filterKriterien[i].operator);

						// ignorecase: 2 hier auf upper
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
						orderBy.append("partner." + kriterien[i].kritName);
						orderBy.append(" ");
						orderBy.append(kriterien[i].value);
					}
				}
			} else {
				// no sort criteria found, add default sort
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append("partner."
						+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1
						+ " ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("partner.i_id") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" partner.i_id ");
				sortAdded = true;
			}
			if (sortAdded) {
				orderBy.insert(0, " ORDER BY ");
			}
		}
		return orderBy.toString();
	}

	/**
	 * flrjoin: 0 get the basic from clause for the HQL statement.
	 * 
	 * @return the from clause.
	 */
	private String getFromClause() {
		return "SELECT distinct partner.i_id, partner.partnerart_c_nr,partner.c_name1nachnamefirmazeile1,partner.c_name2vornamefirmazeile2,partner.flrlandplzort.flrland.c_lkz,partner.flrlandplzort.c_plz,partner.flrlandplzort.flrort.c_name,partner.f_gmtversatz,partner.b_versteckt FROM FLRPartner AS partner "
				+ " LEFT JOIN partner.flrlandplzort AS flrlandplzort "
				+ " LEFT JOIN partner.flrlandplzort.flrort AS flrort "
				+ " LEFT JOIN partner.flrlandplzort.flrland AS flrland "
				+ " LEFT OUTER JOIN partner.ansprechpartner AS ansprechpartnerset "
				+ " LEFT OUTER JOIN ansprechpartnerset.flrpartneransprechpartner AS partneransprechpartner ";

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
			setTableInfo(new TableInfo(new Class[] { Integer.class,
					String.class, String.class, String.class, String.class,
					String.class, String.class, Double.class, Color.class },
					new String[] {
							"i_id",
							getTextRespectUISpr("lp.art", mandantCNr, locUI),
							getTextRespectUISpr("lp.firma_nachname",
									mandantCNr, locUI),
							getTextRespectUISpr("lp.firma_vorname", mandantCNr,
									locUI),
							getTextRespectUISpr("lp.lkz", mandantCNr, locUI),
							getTextRespectUISpr("lp.plz", mandantCNr, locUI),
							getTextRespectUISpr("lp.ort", mandantCNr, locUI),
							getTextRespectUISpr("part.auswahl.gmt",
									theClientDto.getMandant(),
									theClientDto.getLocUi()), "" }, new int[] {
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_L,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_S,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_XS, 0 }, new String[] {
							"i_id",
							PartnerFac.FLR_PARTNER_PARTNERART,
							PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
							PartnerFac.FLR_PARTNER_NAME2VORNAMEFIRMAZEILE2,
							PartnerFac.FLR_PARTNER_FLRLANDPLZORT + "."
									+ SystemFac.FLR_LP_FLRLAND + "."
									+ SystemFac.FLR_LP_LANDLKZ,

							PartnerFac.FLR_PARTNER_FLRLANDPLZORT + "."
									+ SystemFac.FLR_LP_LANDPLZORTPLZ,

							PartnerFac.FLR_PARTNER_FLRLANDPLZORT + "."
									+ SystemFac.FLR_LP_FLRORT + "."
									+ SystemFac.FLR_LP_ORTNAME,
							PartnerFac.FLR_PARTNER_F_GMTVERSATZ, "" }));

		}
		return super.getTableInfo();
	}

	public PrintInfoDto getSDocPathAndPartner(Object key) {
		PartnerDto partnerDto = null;
		try {
			partnerDto = getPartnerFac().partnerFindByPrimaryKey((Integer) key,
					theClientDto);
		} catch (Exception e) {
			// Nicht gefunden
		}
		if (partnerDto != null) {
			// String sPartner = partnerDto.getCName1nachnamefirmazeile1()
			// .replace("/", ".").replace("'", " ").trim();
			// if (partnerDto.getCName2vornamefirmazeile2() != null) {
			// sPartner = sPartner
			// + " "
			// + partnerDto.getCName2vornamefirmazeile2()
			// .replace("/", ".").replace("'", " ").trim();
			// }
			// String sPath = JCRDocFac.HELIUMV_NODE + "/"
			// + theClientDto.getMandant() + "/"
			// + LocaleFac.BELEGART_PARTNER.trim() + "/"
			// + LocaleFac.BELEGART_PARTNER.trim() + "/" + sPartner;
			DocPath docPath = new DocPath(new DocNodePartner(partnerDto));
			return new PrintInfoDto(docPath, null, getSTable());
		} else {
			return null;
		}
	}

	public String getSTable() {
		return "PARTNER";
	}
}
