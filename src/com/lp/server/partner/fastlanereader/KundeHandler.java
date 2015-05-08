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

import javax.swing.Icon;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeKunde;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
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
 * Diese Klasse kuemmert sich um den Kunden-FLR.
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellungsdatum 2004-11-17
 * </p>
 * 
 * <p>
 * 
 * @author $Author: robert $
 *         </p>
 * 
 * @version $Revision: 1.23 $ Date $Date: 2013/01/19 11:47:31 $
 */

public class KundeHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean bSuchenInklusiveKbez = true;
	boolean bMitKundennummer = false;

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {

		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			// spalteres: 1 auch hier via lazy loading.
			int colCount = getTableInfo().getColumnClasses().length;
			// int pageSize = KundeHandler.PAGE_SIZE;
			int pageSize = getLimit();
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
				// FLRKunde kunde = (FLRKunde) (o[0]);

				rows[row][col++] = o[0];
				rows[row][col++] = o[12];
				rows[row][col++] = o[1];
				rows[row][col++] = o[2];
				rows[row][col++] = o[3];
				rows[row][col++] = o[4];
				if (o[5] != null) {
					rows[row][col++] = LocaleFac.STATUS_GESPERRT;
				} else {
					rows[row][col++] = null;
				}
				rows[row][col++] = o[6];
				rows[row][col++] = o[7];
				rows[row][col++] = o[8];
				rows[row][col++] = o[9];
				if (bMitKundennummer == false) {
					rows[row][col++] = o[10];
				} else {
					rows[row][col++] = o[11];
				}

				rows[row][col++] = o[13];

				if (Helper.short2boolean((Short) o[14])) {
					rows[row][col++] = Color.LIGHT_GRAY;
				}

				row++;
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

	protected long getRowCountFromDataBase() throws EJBExceptionLP {

		long rowCount = 0;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			String queryString = "SELECT count(distinct kunde.i_id) FROM FLRKunde AS kunde "
					+ " left join kunde.flrpartner.flrlandplzort as flrlandplzort "
					+ " left join kunde.flrpartner.flrlandplzort.flrort as flrort "
					+ " left join kunde.flrkonto as flrkonto "
					+ " left join kunde.flrpartner.flrlandplzort.flrland as flrland "

					+ " LEFT OUTER JOIN kunde.flrpartner.ansprechpartner AS ansprechpartnerset "
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
									.equals("flrpartner."
											+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1)) {
						where.append(" (lower(kunde."
								+ filterKriterien[i].kritName + ")");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase());
						where.append(" OR lower(kunde.flrpartner.c_name2vornamefirmazeile2)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase());
						where.append(" OR lower(kunde.flrpartner.c_kbez)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase() + ")");
					} else if (bSuchenInklusiveKbez == false
							&& filterKriterien[i].kritName
									.equals("flrpartner."
											+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1)) {
						where.append(" (lower(kunde."
								+ filterKriterien[i].kritName + ")");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase());
						where.append(" OR lower(kunde.flrpartner.c_name2vornamefirmazeile2)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase() + ")");
					} else if (filterKriterien[i].kritName
							.equals(PartnerFac.PARTNERQP1_ERWEITERTE_SUCHE)) {

						String suchstring = "  coalesce(kunde.flrpartner.c_name1nachnamefirmazeile1,'')||' '||coalesce(kunde.flrpartner.c_name2vornamefirmazeile2,'')||' '||coalesce(kunde.flrpartner.c_name3vorname2abteilung,'')||' '||coalesce(kunde.flrpartner.c_kbez,'')";
						suchstring += "||' '||coalesce(kunde.flrpartner.c_strasse,'')||' '||coalesce(partneransprechpartner.c_name1nachnamefirmazeile1,'')||' '||coalesce(partneransprechpartner.c_name2vornamefirmazeile2,'')";

						suchstring += "||' '||coalesce(partneransprechpartner.c_name3vorname2abteilung,'')||' '||coalesce(kunde.flrpartner.c_email,'')||' '||coalesce(kunde.flrpartner.c_fax,'')";

						suchstring += "||' '||coalesce(kunde.flrpartner.c_telefon,'')||' '||coalesce(ansprechpartnerset.c_handy,'')||' '||coalesce(ansprechpartnerset.c_email,'')||' '||coalesce(ansprechpartnerset.c_fax,'')||' '||coalesce(ansprechpartnerset.c_telefon,'')";
						suchstring += "||' '||coalesce(cast(ansprechpartnerset.x_bemerkung as string),'')||' '||coalesce(cast(kunde.x_kommentar as string),'')||' '||coalesce(cast(kunde.flrpartner.x_bemerkung as string),'')";
						suchstring += "||' '||coalesce(kunde.c_hinweisintern,'')||' '||coalesce(kunde.c_hinweisextern,'')";

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

					} else if (filterKriterien[i].kritName
							.equals("KUNDE_INTERESSENT")) {

						if (filterKriterien[i].value.equals("1")) {
							where.append(" (kunde.b_istinteressent=0 OR kunde.b_istinteressent=1)");
						} else if (filterKriterien[i].value.equals("2")) {
							where.append(" kunde.b_istinteressent=0");
						} else if (filterKriterien[i].value.equals("3")) {
							where.append(" kunde.b_istinteressent=1");
						}

					}

					else {

						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" lower(kunde."
									+ filterKriterien[i].kritName + ")");
						} else {
							where.append(" kunde."
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
							orderBy.append("kunde." + kriterien[i].kritName);
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
				orderBy.append("kunde." + KundeFac.FLR_PARTNER + "."
						+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1
						+ " ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("kunde.i_id") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" kunde.i_id ");
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
		return "SELECT distinct kunde.i_id, kunde.flrpartner.c_name1nachnamefirmazeile1, kunde.flrpartner.c_kbez,kunde.flrpartner.c_telefon, kunde.c_abc, kunde.t_liefersperream, kunde.flrpartner.flrlandplzort.flrland.c_lkz, kunde.flrpartner.flrlandplzort.c_plz,kunde.flrpartner.flrlandplzort.flrort.c_name,kunde.flrpersonal.c_kurzzeichen, kunde.flrkonto.c_nr,kunde.i_kundennummer, kunde.flrpartner.c_adressart, kunde.flrpartner.f_gmtversatz, kunde.flrpartner.b_versteckt from FLRKunde as kunde "
				+ " left join kunde.flrpartner.flrlandplzort as flrlandplzort "
				+ " left join kunde.flrpartner.flrlandplzort.flrort as flrort "
				+ " left join kunde.flrkonto as flrkonto "
				+ " left join kunde.flrpartner.flrlandplzort.flrland as flrland "

				+ " LEFT OUTER JOIN kunde.flrpartner.ansprechpartner AS ansprechpartnerset "
				+ " LEFT OUTER JOIN ansprechpartnerset.flrpartneransprechpartner AS partneransprechpartner ";
	}

	public QueryResult sort(SortierKriterium[] sortierKriterien,
			Object selectedId) throws EJBExceptionLP {

		getQuery().setSortKrit(sortierKriterien);

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

		// spalteres: 0 Hier werden die Spaltentexte geladen.
		if (super.getTableInfo() == null) {

			try {
				ParametermandantDto parameter = getParameterFac()
						.getMandantparameter(theClientDto.getMandant(),
								ParameterFac.KATEGORIE_KUNDEN,
								ParameterFac.PARAMETER_KUNDE_MIT_NUMMER);
				bMitKundennummer = (java.lang.Boolean) parameter
						.getCWertAsObject();

			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}

			if (bMitKundennummer == false) {

				setTableInfo(new TableInfo(
						new Class[] { Integer.class, String.class,
								String.class, String.class, String.class,
								String.class, Icon.class, String.class,
								String.class, String.class, String.class,
								String.class, Double.class, Color.class },
						new String[] {
								"i_id", // hier steht immer i_id oder c_nr
								"",
								getTextRespectUISpr("lp.firma",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.kurzbezeichnung",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.telefon",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("part.auswahl.abc",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr(
										"part.auswahl.liefersperre",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.lkz",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.plz",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.ort",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.vertreter",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.debitoren",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("part.auswahl.gmt",
										theClientDto.getMandant(),
										theClientDto.getLocUi()), "" },
						new int[] {
								QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_XXS,
								QueryParameters.FLR_BREITE_L
										+ QueryParameters.FLR_BREITE_M,
								QueryParameters.FLR_BREITE_XM,
								QueryParameters.FLR_BREITE_XM,
								QueryParameters.FLR_BREITE_S,
								QueryParameters.FLR_BREITE_S,
								QueryParameters.FLR_BREITE_XS,
								QueryParameters.FLR_BREITE_S
										+ QueryParameters.FLR_BREITE_XS,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_XS,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_XS, 0 },
						new String[] {
								"i_id",
								KundeFac.FLR_PARTNER + "."
										+ PartnerFac.FLR_PARTNER_C_ADRESSART,
								KundeFac.FLR_PARTNER
										+ "."
										+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,

								KundeFac.FLR_PARTNER + "."
										+ PartnerFac.FLR_PARTNER_C_KBEZ,
								Facade.NICHT_SORTIERBAR,

								KundeFac.FLR_KUNDE_C_ABC,

								KundeFac.FLR_KUNDE_T_LIEFERSPERREAM,

								KundeFac.FLR_PARTNER + "."
										+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT
										+ "." + SystemFac.FLR_LP_FLRLAND + "."
										+ SystemFac.FLR_LP_LANDLKZ,

								KundeFac.FLR_PARTNER + "."
										+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT
										+ "." + SystemFac.FLR_LP_LANDPLZORTPLZ,

								KundeFac.FLR_PARTNER + "."
										+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT
										+ "." + SystemFac.FLR_LP_FLRORT + "."
										+ SystemFac.FLR_LP_ORTNAME,

								KundeFac.FLR_PERSONAL
										+ "."
										+ PersonalFac.FLR_PERSONAL_C_KURZZEICHEN,

								KundeFac.FLR_KONTO + ".c_nr",
								KundeFac.FLR_PARTNER + "."
										+ PartnerFac.FLR_PARTNER_F_GMTVERSATZ,
								"" })

				);
			} else {
				setTableInfo(new TableInfo(
						new Class[] { Integer.class, String.class,
								String.class, String.class, String.class,
								String.class, Icon.class, String.class,
								String.class, String.class, String.class,
								Integer.class, Double.class, Color.class },
						new String[] {
								"i_id", // hier steht immer i_id oder c_nr
								"",
								getTextRespectUISpr("lp.firma",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.kurzbezeichnung",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.telefon",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("part.auswahl.abc",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr(
										"part.auswahl.liefersperre",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.lkz",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.plz",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.ort",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.vertreter",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.kundennummer",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),

								getTextRespectUISpr("part.auswahl.gmt",
										theClientDto.getMandant(),
										theClientDto.getLocUi()), "" },
						new int[] {
								QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_XXS,
								QueryParameters.FLR_BREITE_L
										+ QueryParameters.FLR_BREITE_M,
								QueryParameters.FLR_BREITE_XM,
								QueryParameters.FLR_BREITE_XM,
								QueryParameters.FLR_BREITE_S,
								QueryParameters.FLR_BREITE_S,
								QueryParameters.FLR_BREITE_XS,
								QueryParameters.FLR_BREITE_S
										+ QueryParameters.FLR_BREITE_XS,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_XS,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_XS, 0 },
						new String[] {
								"i_id",
								KundeFac.FLR_PARTNER + "."
										+ PartnerFac.FLR_PARTNER_C_ADRESSART,
								KundeFac.FLR_PARTNER
										+ "."
										+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,

								KundeFac.FLR_PARTNER + "."
										+ PartnerFac.FLR_PARTNER_C_KBEZ,
								Facade.NICHT_SORTIERBAR,

								KundeFac.FLR_KUNDE_C_ABC,

								KundeFac.FLR_KUNDE_T_LIEFERSPERREAM,

								KundeFac.FLR_PARTNER + "."
										+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT
										+ "." + SystemFac.FLR_LP_FLRLAND + "."
										+ SystemFac.FLR_LP_LANDLKZ,

								KundeFac.FLR_PARTNER + "."
										+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT
										+ "." + SystemFac.FLR_LP_LANDPLZORTPLZ,

								KundeFac.FLR_PARTNER + "."
										+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT
										+ "." + SystemFac.FLR_LP_FLRORT + "."
										+ SystemFac.FLR_LP_ORTNAME,

								KundeFac.FLR_PERSONAL
										+ "."
										+ PersonalFac.FLR_PERSONAL_C_KURZZEICHEN,

								KundeFac.FLR_KUNDE_I_KUNDENNUMMER,
								KundeFac.FLR_PARTNER + "."
										+ PartnerFac.FLR_PARTNER_F_GMTVERSATZ,
								"" })

				);

			}

		}
		return super.getTableInfo();
	}

	public String getQueryHandler() {
		return getFromClause() + "" + buildWhereClause();
	}

	public PrintInfoDto getSDocPathAndPartner(Object key) {
		KundeDto kundeDto = null;
		PartnerDto partnerDto = null;
		try {
			kundeDto = getKundeFac().kundeFindByPrimaryKey((Integer) key,
					theClientDto);
			partnerDto = getPartnerFac().partnerFindByPrimaryKey(
					kundeDto.getPartnerIId(), theClientDto);
			kundeDto.setPartnerDto(partnerDto);
		} catch (Exception e) {
			// Nicht gefunden
		}
		if (partnerDto != null) {
			// String sKunde =
			// partnerDto.getCName1nachnamefirmazeile1().replace(
			// "/", ".");
			// if (partnerDto.getCName2vornamefirmazeile2() != null) {
			// sKunde = sKunde
			// + " "
			// + partnerDto.getCName2vornamefirmazeile2().replace("/",
			// ".");
			// }
			// String sPath = JCRDocFac.HELIUMV_NODE + "/"
			// + theClientDto.getMandant() + "/"
			// + LocaleFac.BELEGART_KUNDE.trim() + "/"
			// + LocaleFac.BELEGART_KUNDE.trim() + "/" + sKunde.trim();
			DocPath docPath = new DocPath(
					new DocNodeKunde(kundeDto, partnerDto));
			return new PrintInfoDto(docPath, null, getSTable());
		} else {
			return null;
		}
	}

	public String getSTable() {
		return "KUNDE";
	}
}
