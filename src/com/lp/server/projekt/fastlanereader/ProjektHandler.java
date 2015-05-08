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
package com.lp.server.projekt.fastlanereader;

import java.awt.Color;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Date;
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
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.projekt.fastlanereader.generated.FLRProjekt;
import com.lp.server.projekt.service.BereichDto;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.projekt.service.ProjektFac;
import com.lp.server.projekt.service.ProjekttypsprDto;
import com.lp.server.system.fastlanereader.generated.FLRLandplzort;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeProjekt;
import com.lp.server.system.jcr.service.docnode.DocPath;
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
 * Diese Klasse kuemmert sich um ...
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellung: Vorname Nachname; dd.mm.06
 * </p>
 * 
 * <p>
 * 
 * @author $Author: robert $
 *         </p>
 * 
 * @version not attributable Date $Date: 2013/01/19 11:47:31 $
 */
public class ProjektHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean bProjektMitUmsatz = false;
	boolean bKurzzeichenStattName = false;
	boolean bSuchenInklusiveKbez = true;
	boolean bKbezStattOrt = false;
	public static final String FLR_PROJEKT = "projekt.";
	public static final String FLR_PROJEKT_FROM_CLAUSE = " from FLRProjekt projekt ";
	/**
	 * @todo VF->VF -> Query von client abschicken.
	 */
	public static String lastQuery = "";

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {

		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = getLimit();
			int startIndex = getStartIndex(rowIndex, pageSize);
			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			String queryString = getFromClause() + buildWhereClause()
					+ buildOrderByClause();
			lastQuery = queryString;
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount + 1];
			String[] tooltipData = new String[resultList.size()];
			String sProjekt = getTextRespectUISpr("proj.projekt",
					theClientDto.getMandant(), theClientDto.getLocUi());

			int row = 0;
			int col = 0;
			while (resultListIterator.hasNext()) {
				// flrjoin: 1
				FLRProjekt projekt = (FLRProjekt) resultListIterator.next();
				rows[row][col++] = projekt.getI_id();
				rows[row][col++] = projekt.getC_nr();
				rows[row][col++] = projekt.getFlrpartner()
						.getC_name1nachnamefirmazeile1();

				if (bKbezStattOrt) {
					rows[row][col++] = projekt.getFlrpartner().getC_kbez();
				} else {

					String cAnschrift = null;

					if (projekt.getFlrpartner() != null) {
						FLRLandplzort flranschrift = projekt.getFlrpartner()
								.getFlrlandplzort();

						if (flranschrift != null) {
							cAnschrift = flranschrift.getFlrland().getC_lkz()
									+ "-" + flranschrift.getC_plz() + " "
									+ flranschrift.getFlrort().getC_name();
						}
					}
					rows[row][col++] = cAnschrift;
				}

				rows[row][col++] = projekt.getKategorie_c_nr().trim();
				rows[row][col++] = projekt.getC_titel();

				if (bKurzzeichenStattName) {
					rows[row][col++] = projekt.getFlrpersonalErzeuger()
							.getC_kurzzeichen();
				} else {
					rows[row][col++] = projekt.getFlrpersonalErzeuger()
							.getFlrpartner().getC_name1nachnamefirmazeile1();
				}

				if (projekt.getFlrpersonalZugewiesener() != null) {
					if (bKurzzeichenStattName) {
						rows[row][col++] = projekt.getFlrpersonalZugewiesener()
								.getC_kurzzeichen();
					} else {
						rows[row][col++] = projekt.getFlrpersonalZugewiesener()
								.getFlrpartner()
								.getC_name1nachnamefirmazeile1();
					}

				} else {
					rows[row][col++] = "";
				}

				rows[row][col++] = getProjektTypBezeichnung(
						projekt.getTyp_c_nr(), theClientDto.getLocUi(),
						theClientDto.getMandant());
				rows[row][col++] = projekt.getI_prio();
				String sStatus = projekt.getStatus_c_nr();
				rows[row][col++] = getStatusMitUebersetzung(sStatus);
				rows[row][col++] = projekt.getT_zielwunschdatum();

				if (bProjektMitUmsatz) {

					rows[row][col++] = projekt.getN_umsatzgeplant();

					rows[row][col++] = projekt.getI_wahrscheinlichkeit();
				} else {
					if (projekt.getD_dauer() != null) {
						rows[row][col++] = projekt.getD_dauer();
					} else {
						rows[row][col++] = new Double(0.0);
					}

					rows[row][col++] = Helper.short2Boolean(projekt
							.getB_verrechenbar());

					if (projekt.getT_internerledigt() == null) {
						rows[row][col++] = new Boolean(false);
					} else {
						rows[row][col++] = new Boolean(true);
					}
				}

				if (projekt.getX_freetext() != null) {
					String text = "<b>" + sProjekt + " " + projekt.getC_nr()
							+ ":</b>\n" + projekt.getX_freetext();
					text = text.replaceAll("\n", "<br>");
					text = "<html>" + text + "</html>";
					tooltipData[row] = text;
				}

				if (projekt.getI_sort() != null) {
					rows[row][col++] = new Color(176, 0, 255);
				} else {
					rows[row][col++] = null;
				}
				
				rows[row][col++] = projekt.getBereich_i_id() ;
				row++ ;
				col = 0;
			}
			result = new QueryResult(rows, getRowCount(), startIndex, endIndex,
					0, tooltipData);
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

	private String getProjektTypBezeichnung(String projektTypCNr,
			Locale locale, String mandantCNr) throws RemoteException {
		ProjekttypsprDto ptDto = getProjektServiceFac()
				.projekttypsprFindByPrimaryKeyohneEx(projektTypCNr,
						Helper.locale2String(locale), mandantCNr);
		if (ptDto == null || ptDto.getCBez() == null)
			return projektTypCNr;
		else
			return ptDto.getCBez();
	}

	/**
	 * get the basic from clause for the HQL statement.
	 * 
	 * @return the from clause.
	 */
	private String getFromClause() {
		return "from FLRProjekt projekt ";
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

					// wenn nach der c_nr gefilter wird, wird das kriterium
					// veraendert
					if (bSuchenInklusiveKbez
							&& filterKriterien[i].kritName
									.equals("flrpartner."
											+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1)) {
						where.append(" (lower(" + filterKriterien[i].kritName
								+ ")");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase());
						where.append(" OR lower(flrpartner.c_name2vornamefirmazeile2)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase());
						where.append(" OR lower(flrpartner.c_kbez)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase() + ")");
					} else if (bSuchenInklusiveKbez == false
							&& filterKriterien[i].kritName
									.equals("flrpartner."
											+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1)) {
						where.append(" (lower(" + filterKriterien[i].kritName
								+ ")");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase());
						where.append(" OR lower(flrpartner.c_name2vornamefirmazeile2)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase() + ")");
					} else if (filterKriterien[i].kritName.equals("c_nr")) {
						try {
							String sValue = super.buildWhereBelegnummer(
									filterKriterien[i], false);
							// Belegnummernsuche auch in "altem" Jahr, wenn im
							// neuen noch keines vorhanden ist
							if (!istBelegnummernInJahr("FLRProjekt", sValue)) {
								sValue = super.buildWhereBelegnummer(
										filterKriterien[i], true);
							}
							where.append(" " + FLR_PROJEKT
									+ filterKriterien[i].kritName);
							where.append(" " + filterKriterien[i].operator);
							where.append(" " + sValue);
						} catch (Exception ex) {
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR,
									ex);
						}
					} else if (filterKriterien[i].kritName.equals("c_suche")) {
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" lower("
									+ FLR_PROJEKT
									+ ProjektFac.FLR_PROJEKT_FLRPROJEKTTEXTSUCHE
									+ "." + filterKriterien[i].kritName + ")");
						} else {
							where.append(" "
									+ FLR_PROJEKT
									+ ProjektFac.FLR_PROJEKT_FLRPROJEKTTEXTSUCHE
									+ "." + filterKriterien[i].kritName);
						}
						where.append(" " + filterKriterien[i].operator);
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" "
									+ filterKriterien[i].value.toLowerCase());
						} else {
							where.append(" " + filterKriterien[i].value);
						}
					} else {
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" lower(" + FLR_PROJEKT
									+ filterKriterien[i].kritName + ")");
						} else {
							where.append(" " + FLR_PROJEKT
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

	public QueryResult sort(SortierKriterium[] sortierKriterien,
			Object selectedId) throws EJBExceptionLP {

		if (this.getQuery() != null) {
			this.getQuery().setSortKrit(sortierKriterien);
		}

		QueryResult result = null;
		int rowNumber = 0;

		if (selectedId != null && ((Integer) selectedId).intValue() >= 0) {
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();
				String queryString = "select projekt.i_id "
						+ this.getFromClause() + this.buildWhereClause()
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
							orderBy.append(FLR_PROJEKT + kriterien[i].kritName);
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
				orderBy.append(FLR_PROJEKT).append("c_nr DESC"); // t_zielwunschdatum
				// und i_prio
				sortAdded = true;
			}
			if (orderBy.indexOf(FLR_PROJEKT + ProjektFac.FLR_PROJEKT_I_ID) < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" ").append(FLR_PROJEKT)
						.append(ProjektFac.FLR_PROJEKT_I_ID).append(" ASC ");
				sortAdded = true;
			}
			if (sortAdded) {
				orderBy.insert(0, " ORDER BY ");
			}
		}
		return orderBy.toString();
	}

	public TableInfo getTableInfo() {
		if (super.getTableInfo() == null) {

			try {
				ParametermandantDto parameter = getParameterFac()
						.getMandantparameter(theClientDto.getMandant(),
								ParameterFac.KATEGORIE_PROJEKT,
								ParameterFac.PARAMETER_PROJEKT_MIT_UMSATZ);
				bProjektMitUmsatz = (Boolean) parameter.getCWertAsObject();

				parameter = getParameterFac().getMandantparameter(
						theClientDto.getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN,
						ParameterFac.PARAMETER_SUCHEN_INKLUSIVE_KBEZ);
				bSuchenInklusiveKbez = (java.lang.Boolean) parameter
						.getCWertAsObject();
				parameter = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_PROJEKT,
								ParameterFac.PARAMETER_KURZBEZEICHNUNG_STATT_ORT_IN_AUSWAHLLISTE);
				bKbezStattOrt = (java.lang.Boolean) parameter
						.getCWertAsObject();

				parameter = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_PROJEKT,
								ParameterFac.PARAMETER_KURZZEICHEN_STATT_NAME_IN_AUSWAHLLISTE);
				bKurzzeichenStattName = (java.lang.Boolean) parameter
						.getCWertAsObject();

			} catch (RemoteException ex) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
			}

			String mandantCNr = theClientDto.getMandant();
			String sortierungNachPartner = Facade.NICHT_SORTIERBAR;
			String sortierungNachPersonalErzeuger = Facade.NICHT_SORTIERBAR;
			String sortierungNachPersonalZugewiesener = Facade.NICHT_SORTIERBAR;
			Locale locUI = theClientDto.getLocUi();
			if (SORTIERUNG_UI_PARTNER_ORT) {
				sortierungNachPartner = ProjektFac.FLR_PROJEKT_FLRPARTNER + "."
						+ PartnerFac.FLR_PARTNER_C_NAME1NACHNAMEFIRMAZEILE1;
				sortierungNachPersonalErzeuger = ProjektFac.FLR_PROJEKT_FLRPERSONALERZEUGER
						+ "."
						+ PersonalFac.FLR_PERSONAL_FLRPARTNER
						+ "."
						+ PartnerFac.FLR_PARTNER_C_NAME1NACHNAMEFIRMAZEILE1;
				sortierungNachPersonalZugewiesener = ProjektFac.FLR_PROJEKT_FLRPERSONALZUGEWIESENER
						+ "."
						+ PersonalFac.FLR_PERSONAL_FLRPARTNER
						+ "."
						+ PartnerFac.FLR_PARTNER_C_NAME1NACHNAMEFIRMAZEILE1;
			}

			if (bProjektMitUmsatz) {

				setTableInfo(new TableInfo(
						new Class[] { Integer.class, String.class,
								String.class, String.class, String.class,
								String.class, String.class, String.class,
								String.class, Integer.class, Icon.class,
								Date.class, BigDecimal.class, Integer.class,
								Color.class

						},
						new String[] {
								"i_id",
								getTextRespectUISpr("proj.nr", mandantCNr,
										locUI),
								getTextRespectUISpr("lp.firma_nachname",
										mandantCNr, locUI),
								bKbezStattOrt ? getTextRespectUISpr(
										"lp.kurzbezeichnung", mandantCNr, locUI)
										: getTextRespectUISpr("lp.ort",
												mandantCNr, locUI),
								getTextRespectUISpr("proj.kategorie",
										mandantCNr, locUI),
								getTextRespectUISpr("proj.titel", mandantCNr,
										locUI),
								getTextRespectUISpr("proj.personal.erzeuger",
										mandantCNr, locUI),
								getTextRespectUISpr("proj.personal.fuer",
										mandantCNr, locUI),
								getTextRespectUISpr("lp.typ", mandantCNr, locUI),
								getTextRespectUISpr("proj.prio", mandantCNr,
										locUI),
								getTextRespectUISpr("lp.status", mandantCNr,
										locUI),
								getTextRespectUISpr("lp.termin", mandantCNr,
										locUI),
								getTextRespectUISpr("proj.label.umsatzgeplant",
										mandantCNr, locUI),
								getTextRespectUISpr(
										"proj.label.wahrscheinlichkeit",
										mandantCNr, locUI), "" },// color
						new int[] {
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, // i_id
								10, // c_nr
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, // partner_i_id
								QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_M, // kategorieCNr
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, // c_titel
								QueryParameters.FLR_BREITE_M,// personal_i_id_erzeuger
								QueryParameters.FLR_BREITE_M,// personal_i_id_zugewiesener
								QueryParameters.FLR_BREITE_M, // typCNr
								1, // i_prio
								QueryParameters.FLR_BREITE_XS, // statusCNr
								10, // termin
								QueryParameters.FLR_BREITE_M,// dauer
								QueryParameters.FLR_BREITE_XS,// verrechenbar
								1 }, // color
						new String[] {
								ProjektFac.FLR_PROJEKT_I_ID,
								ProjektFac.FLR_PROJEKT_C_NR,
								sortierungNachPartner,
								// Sortierung fuers erste mal nach LKZ
								bKbezStattOrt ? KundeFac.FLR_PARTNER + "."
										+ PartnerFac.FLR_PARTNER_C_KBEZ
										: KundeFac.FLR_PARTNER
												+ "."
												+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT
												+ "."
												+ SystemFac.FLR_LP_FLRLAND
												+ "."
												+ SystemFac.FLR_LP_LANDLKZ
												+ ", "
												+
												// und dann nach plz
												KundeFac.FLR_PARTNER
												+ "."
												+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT
												+ "."
												+ SystemFac.FLR_LP_LANDPLZORTPLZ,
								ProjektFac.FLR_PROJEKT_KATEGORIE_C_NR,
								ProjektFac.FLR_PROJEKT_C_TITEL,
								sortierungNachPersonalErzeuger,
								sortierungNachPersonalZugewiesener,
								ProjektFac.FLR_PROJEKT_TYP_C_NR,
								ProjektFac.FLR_PROJEKT_I_PRIO,
								ProjektFac.FLR_PROJEKT_STATUS_C_NR,
								ProjektFac.FLR_PROJEKT_T_ZIELDATUM,
								ProjektFac.FLR_PROJEKT_N_UMSATZGEPLANT,
								ProjektFac.FLR_PROJEKT_I_WAHRSCHEINLICHKEIT,
								"", // color
						}));
			} else {

				setTableInfo(new TableInfo(
						new Class[] { Integer.class, String.class,
								String.class, String.class, String.class,
								String.class, String.class, String.class,
								String.class, Integer.class, Icon.class,
								Date.class, Double.class, Boolean.class,
								Boolean.class, Color.class

						},
						new String[] {
								"i_id",
								getTextRespectUISpr("proj.nr", mandantCNr,
										locUI),
								getTextRespectUISpr("lp.firma_nachname",
										mandantCNr, locUI),
								bKbezStattOrt ? getTextRespectUISpr(
										"lp.kurzbezeichnung", mandantCNr, locUI)
										: getTextRespectUISpr("lp.ort",
												mandantCNr, locUI),
								getTextRespectUISpr("proj.kategorie",
										mandantCNr, locUI),
								getTextRespectUISpr("proj.titel", mandantCNr,
										locUI),
								getTextRespectUISpr("proj.personal.erzeuger",
										mandantCNr, locUI),
								getTextRespectUISpr("proj.personal.fuer",
										mandantCNr, locUI),
								getTextRespectUISpr("lp.typ", mandantCNr, locUI),
								getTextRespectUISpr("proj.prio", mandantCNr,
										locUI),
								getTextRespectUISpr("lp.status", mandantCNr,
										locUI),
								getTextRespectUISpr("lp.termin", mandantCNr,
										locUI),
								getTextRespectUISpr("proj.schaetzung",
										mandantCNr, locUI),
								getTextRespectUISpr("proj.label.verrechenbar",
										mandantCNr, locUI),
								getTextRespectUISpr("proj.internerledigt",
										mandantCNr, locUI), "" },// color
						new int[] {
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, // i_id
								10, // c_nr
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, // partner_i_id
								QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_M, // kategorieCNr
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, // c_titel
								QueryParameters.FLR_BREITE_M,// personal_i_id_erzeuger
								QueryParameters.FLR_BREITE_M,// personal_i_id_zugewiesener
								QueryParameters.FLR_BREITE_M, // typCNr
								1, // i_prio
								QueryParameters.FLR_BREITE_XS, // statusCNr
								10, // termin
								QueryParameters.FLR_BREITE_XS,// dauer
								QueryParameters.FLR_BREITE_S,// verrechenbar
								QueryParameters.FLR_BREITE_S,// intern erledigt
								1 }, // color
						new String[] {
								ProjektFac.FLR_PROJEKT_I_ID,
								ProjektFac.FLR_PROJEKT_C_NR,
								sortierungNachPartner,
								// Sortierung fuers erste mal nach LKZ
								bKbezStattOrt ? KundeFac.FLR_PARTNER + "."
										+ PartnerFac.FLR_PARTNER_C_KBEZ
										: KundeFac.FLR_PARTNER
												+ "."
												+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT
												+ "."
												+ SystemFac.FLR_LP_FLRLAND
												+ "."
												+ SystemFac.FLR_LP_LANDLKZ
												+ ", "
												+
												// und dann nach plz
												KundeFac.FLR_PARTNER
												+ "."
												+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT
												+ "."
												+ SystemFac.FLR_LP_LANDPLZORTPLZ,
								ProjektFac.FLR_PROJEKT_KATEGORIE_C_NR,
								ProjektFac.FLR_PROJEKT_C_TITEL,
								sortierungNachPersonalErzeuger,
								sortierungNachPersonalZugewiesener,
								ProjektFac.FLR_PROJEKT_TYP_C_NR,
								ProjektFac.FLR_PROJEKT_I_PRIO,
								ProjektFac.FLR_PROJEKT_STATUS_C_NR,
								ProjektFac.FLR_PROJEKT_T_ZIELDATUM,
								ProjektFac.FLR_PROJEKT_D_DAUER,
								ProjektFac.FLR_PROJEKT_B_VERRECHENBAR,
								ProjektFac.FLR_PROJEKT_T_INTERNERLEDIGT, "", // color
						}));
			}
		}

		return super.getTableInfo();
	}

	public PrintInfoDto getSDocPathAndPartner(Object key) {
		ProjektDto projektDto = null;
		BereichDto bereichDto = null;
		try {
			projektDto = getProjektFac().projektFindByPrimaryKey((Integer) key);
			bereichDto = getProjektServiceFac().bereichFindByPrimaryKey(
					projektDto.getBereichIId());
		} catch (Exception e) {
			// Nicht gefunden
		}
		if (projektDto != null) {
			// String sPath = JCRDocFac.HELIUMV_NODE
			// + "/"
			// + theClientDto.getMandant()
			// + "/"
			// + LocaleFac.BELEGART_PROJEKT.trim()
			// + "/"
			// + getProjektServiceFac().bereichFindByPrimaryKey(
			// projektDto.getBereichIId()).getCBez() + "/"
			// + projektDto.getCNr().replace("/", ".");

			DocPath docPath = new DocPath(new DocNodeProjekt(projektDto,
					bereichDto));
			return new PrintInfoDto(docPath, projektDto.getPartnerIId(),
					getSTable());
		} else {
			return null;
		}
	}

	public String getSTable() {
		return "PROJEKT";
	}

}
