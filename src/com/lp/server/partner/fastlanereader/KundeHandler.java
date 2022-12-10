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

import javax.swing.Icon;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;
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
	int bMitKundennummer = 0;
	boolean bMitVornameStrasse = false;

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

				Object[] rowToAddCandidate = new Object[colCount];

				rowToAddCandidate[getTableColumnInformation().getViewIndex(
						"i_id")] = o[0];

				rowToAddCandidate[getTableColumnInformation().getViewIndex("")] = o[12];
				rowToAddCandidate[getTableColumnInformation().getViewIndex(
						"lp.firma")] = o[1];
				rowToAddCandidate[getTableColumnInformation().getViewIndex(
						"lp.kurzbezeichnung")] = o[2];
				rowToAddCandidate[getTableColumnInformation().getViewIndex(
						"lp.telefon")] = o[3];
				rowToAddCandidate[getTableColumnInformation().getViewIndex(
						"part.auswahl.abc")] = o[4];

				if (o[5] != null) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex(
							"part.auswahl.liefersperre")] = LocaleFac.STATUS_GESPERRT;

				}

				rowToAddCandidate[getTableColumnInformation().getViewIndex(
						"lp.lkz")] = o[6];
				rowToAddCandidate[getTableColumnInformation().getViewIndex(
						"lp.plz")] = o[7];
				rowToAddCandidate[getTableColumnInformation().getViewIndex(
						"lp.ort")] = o[8];
				rowToAddCandidate[getTableColumnInformation().getViewIndex(
						"lp.vertreter")] = o[9];

				if (bMitKundennummer == 0) {

					rowToAddCandidate[getTableColumnInformation().getViewIndex(
							"lp.debitoren")] = o[10];
				} else {

					rowToAddCandidate[getTableColumnInformation().getViewIndex(
							"lp.kundennummer")] = o[11];
				}

				rowToAddCandidate[getTableColumnInformation().getViewIndex(
						"part.auswahl.gmt")] = o[13];

				if (Helper.short2boolean((Short) o[14])) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex(
							"Color")] = Color.LIGHT_GRAY;
				}

				if (bMitVornameStrasse) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex(
							"lp.firma_vorname")] = o[15];
					rowToAddCandidate[getTableColumnInformation().getViewIndex(
							"lp.strasse")] = o[16];
				}

				rows[row] = rowToAddCandidate;
				row++;
				col = 0;
			}
			result = new QueryResult(rows, this.getRowCount(), startIndex,
					endIndex, 0);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
			
		} finally {
			closeSession(session);
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
						suchstring += "||' '||coalesce(cast(ansprechpartnerset.x_bemerkung as text),'')||' '||coalesce(cast(kunde.x_kommentar as text),'')||' '||coalesce(cast(kunde.flrpartner.x_bemerkung as text),'')";
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

					else if (filterKriterien[i].kritName
							.equals("debitorennummer")) {
						where.append(" (lower(kunde.flrkonto.c_nr)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase() + ")");
					}

					else if (filterKriterien[i].kritName.equals("PLZOrt")) {
						where.append(" (lower(kunde.flrpartner.flrlandplzort.flrort.c_name)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase());
						where.append(" OR lower(kunde.flrpartner.flrlandplzort.c_plz)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase() + ")");
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
		return "SELECT distinct kunde.i_id, kunde.flrpartner.c_name1nachnamefirmazeile1, kunde.flrpartner.c_kbez,kunde.flrpartner.c_telefon, kunde.c_abc, kunde.t_liefersperream, kunde.flrpartner.flrlandplzort.flrland.c_lkz, kunde.flrpartner.flrlandplzort.c_plz,kunde.flrpartner.flrlandplzort.flrort.c_name,kunde.flrpersonal.c_kurzzeichen, kunde.flrkonto.c_nr,kunde.i_kundennummer, kunde.flrpartner.c_adressart, kunde.flrpartner.f_gmtversatz, kunde.flrpartner.b_versteckt,kunde.flrpartner.c_name2vornamefirmazeile2,kunde.flrpartner.c_strasse from FLRKunde as kunde "
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

	private TableColumnInformation createColumnInformation(String mandant,
			Locale locUi) {
		TableColumnInformation columns = new TableColumnInformation();

		columns.add("i_id", Integer.class, "i_id", -1, "i_id");

		columns.add("", String.class, "",
				QueryParameters.FLR_BREITE_XXS, KundeFac.FLR_PARTNER + "."
						+ PartnerFac.FLR_PARTNER_C_ADRESSART);
		columns.add("lp.firma", String.class,
				getTextRespectUISpr("lp.firma", mandant, locUi),
				QueryParameters.FLR_BREITE_L + QueryParameters.FLR_BREITE_M,
				KundeFac.FLR_PARTNER + "."
						+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1);

		if (bMitVornameStrasse) {
			columns.add("lp.firma_vorname", String.class,
					getTextRespectUISpr("lp.firma_vorname", mandant, locUi),
					QueryParameters.FLR_BREITE_XM, KundeFac.FLR_PARTNER + "."
							+ PartnerFac.FLR_PARTNER_NAME2VORNAMEFIRMAZEILE2);
		}

		columns.add(
				"lp.kurzbezeichnung",
				String.class,
				getTextRespectUISpr("lp.kurzbezeichnung",
						theClientDto.getMandant(), theClientDto.getLocUi()),
				QueryParameters.FLR_BREITE_XM, KundeFac.FLR_PARTNER + "."
						+ PartnerFac.FLR_PARTNER_C_KBEZ);

		columns.add("lp.telefon", String.class,
				getTextRespectUISpr("lp.telefon", mandant, locUi),
				QueryParameters.FLR_BREITE_XM, Facade.NICHT_SORTIERBAR);

		columns.add("part.auswahl.abc", String.class,
				getTextRespectUISpr("part.auswahl.abc", mandant, locUi),
				QueryParameters.FLR_BREITE_S, KundeFac.FLR_KUNDE_C_ABC);
		columns.add(
				"part.auswahl.liefersperre",
				Icon.class,
				getTextRespectUISpr("part.auswahl.liefersperre.short", mandant, locUi),
				QueryParameters.FLR_BREITE_S,
				KundeFac.FLR_KUNDE_T_LIEFERSPERREAM,getTextRespectUISpr("part.auswahl.liefersperre.tooltip", mandant, locUi) );

		columns.add("lp.lkz", String.class,
				getTextRespectUISpr("lp.lkz", mandant, locUi),
				QueryParameters.FLR_BREITE_XS, KundeFac.FLR_PARTNER + "."
						+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT + "."
						+ SystemFac.FLR_LP_FLRLAND + "."
						+ SystemFac.FLR_LP_LANDLKZ);
		columns.add("lp.plz", String.class,
				getTextRespectUISpr("lp.plz", mandant, locUi),
				QueryParameters.FLR_BREITE_S + QueryParameters.FLR_BREITE_XS,
				KundeFac.FLR_PARTNER + "."
						+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT + "."
						+ SystemFac.FLR_LP_LANDPLZORTPLZ);
		columns.add("lp.ort", String.class,
				getTextRespectUISpr("lp.ort", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST,
				KundeFac.FLR_PARTNER + "."
						+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT + "."
						+ SystemFac.FLR_LP_FLRORT + "."
						+ SystemFac.FLR_LP_ORTNAME);

		if (bMitVornameStrasse) {
			columns.add("lp.strasse", String.class,
					getTextRespectUISpr("lp.strasse", mandant, locUi),
					QueryParameters.FLR_BREITE_XM, KundeFac.FLR_PARTNER + "."
							+ PartnerFac.FLR_PARTNER_C_STRASSE);
		}

		columns.add("lp.vertreter", String.class,
				getTextRespectUISpr("lp.vertreter", mandant, locUi),
				QueryParameters.FLR_BREITE_XS, KundeFac.FLR_PERSONAL + "."
						+ PersonalFac.FLR_PERSONAL_C_KURZZEICHEN);

		if (bMitKundennummer>0) {

			columns.add("lp.kundennummer", Integer.class,
					getTextRespectUISpr("lp.kundennummer", mandant, locUi),
					QueryParameters.FLR_BREITE_SHARE_WITH_REST,
					KundeFac.FLR_KUNDE_I_KUNDENNUMMER);

		} else {
			columns.add("lp.debitoren", String.class,
					getTextRespectUISpr("lp.debitoren", mandant, locUi),
					QueryParameters.FLR_BREITE_SHARE_WITH_REST,
					KundeFac.FLR_KONTO + ".c_nr");

		}

		columns.add("part.auswahl.gmt", Double.class,
				getTextRespectUISpr("part.auswahl.gmt", mandant, locUi),
				QueryParameters.FLR_BREITE_XS, KundeFac.FLR_PARTNER + "."
						+ PartnerFac.FLR_PARTNER_F_GMTVERSATZ);

		columns.add("Color", Color.class, "", 1, "");

		return columns;
	}

	private void setupParameters() {
		try {
			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_KUNDEN,
							ParameterFac.PARAMETER_KUNDE_MIT_NUMMER);
			bMitKundennummer = (java.lang.Integer) parameter.getCWertAsObject();
			parameter = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_KUNDEN,
					ParameterFac.PARAMETER_VORNAME_UND_STRASSE_IN_AUSWAHLLISTE);
			bMitVornameStrasse = (Boolean) parameter.getCWertAsObject();

			parameter = getParameterFac().getMandantparameter(
					theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_SUCHEN_INKLUSIVE_KBEZ);
			bSuchenInklusiveKbez = (java.lang.Boolean) parameter
					.getCWertAsObject();

		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}
	}

	public TableInfo getTableInfo() {
		TableInfo info = super.getTableInfo();
		if (info != null)
			return info;

		setupParameters();
		setTableColumnInformation(createColumnInformation(
				theClientDto.getMandant(), theClientDto.getLocUi()));

		TableColumnInformation c = getTableColumnInformation();
		info = new TableInfo(c.getClasses(), c.getHeaderNames(), c.getWidths(),
				c.getDbColumNames(), c.getHeaderToolTips());
		setTableInfo(info);
		return info;
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
