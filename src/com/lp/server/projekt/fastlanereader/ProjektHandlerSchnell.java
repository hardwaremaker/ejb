
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
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.Icon;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.partner.fastlanereader.generated.FLRPartner;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.projekt.fastlanereader.generated.FLRProjektSchnell;
import com.lp.server.projekt.fastlanereader.generated.FLRProjekttextsuche;
import com.lp.server.projekt.service.BereichDto;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.projekt.service.ProjektFac;
import com.lp.server.projekt.service.ProjektServiceFac;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeProjekt;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.util.Facade;
import com.lp.server.util.HelperServer;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.FlrFirmaAnsprechpartnerFilterBuilder;
import com.lp.server.util.fastlanereader.UseCaseHandler;
import com.lp.server.util.fastlanereader.service.query.FilterBlock;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.EJBExceptionLP;
import com.lp.util.StatusIcon;

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
public class ProjektHandlerSchnell extends UseCaseHandler {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public static final String FLR_PROJEKT = "projekt.";
	/**
	 * @todo VF->VF -> Query von client abschicken.
	 */
	public static String lastQuery = "";


	private String getPartnerAddress(FLRPartner flrPartner) {
		String cAnschrift = null;
		if (flrPartner.getFlrlandplzort() != null) {
			cAnschrift = flrPartner.getFlrlandplzort().getFlrland().getC_lkz() + "-"
					+ flrPartner.getFlrlandplzort().getC_plz() + " "
					+ flrPartner.getFlrlandplzort().getFlrort().getC_name();
		}
		return cAnschrift;
	}

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {

		myLogger.warn("ProjektHandlerSchnell.getPageAt BEGINN "+theClientDto.getBenutzername());
		
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = getLimit();
			int startIndex = getStartIndex(rowIndex, pageSize);
			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();

			String queryString = getFromClause() + buildWhereClause() + buildOrderByClause();
			lastQuery = queryString;
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount + 1];
			String[] tooltipData = new String[resultList.size()];
			String sProjekt = getTextRespectUISpr("proj.projekt", theClientDto.getMandant(), theClientDto.getLocUi());

			int row = 0;
			int col = 0;
			while (resultListIterator.hasNext()) {
				// flrjoin: 1

				FLRProjektSchnell flrprojekt = (FLRProjektSchnell) resultListIterator.next();

				/*
				 * Object[] o = (Object[]) resultListIterator.next();
				 * 
				 * Integer projektIId = (Integer) o[0]; String c_nr = (String) o[1];
				 * 
				 * String typ_c_nr = (String) o[2]; Integer i_prio = (Integer) o[3]; Integer
				 * bereich_i_id = (Integer) o[4]; BigDecimal n_umsatzgeplant = (BigDecimal)
				 * o[5]; Integer i_wahrscheinlichkeit = (Integer) o[6]; Double d_dauer =
				 * (Double) o[7]; java.util.Date t_internerledigt = (java.util.Date) o[8];
				 * Integer i_verrechenbar = (Integer) o[9]; String x_freetext = (String) o[10];
				 * Integer i_sort = (Integer) o[11]; String status_c_nr = (String) o[12];
				 * java.util.Date t_zielwunschdatum = (java.util.Date) o[13]; String titel =
				 * (String) o[14]; String kategorie_c_nr = (String) o[15]; String
				 * projekt_partner = (String) o[16]; String projekt_partner_kbez = (String)
				 * o[17]; String lkz = (String) o[18]; String plz = (String) o[19]; String ort =
				 * (String) o[20]; String zugewiesener_c_kurzzeichen = (String) o[21]; String
				 * zugewiesener_c_name = (String) o[22]; String erzeuger_c_kurzzeichen =
				 * (String) o[23]; String erzeuger_c_name = (String) o[25];
				 * 
				 * String betreiber = (String) o[26]; Integer partner_i_id = (Integer) o[27];
				 * 
				 * String verkaufsfortschritt = (String) o[28]; String leadstatus = (String)
				 * o[29]; String artikel = (String) o[30]; String artikelbez = (String) o[31];
				 */

				String bereich_c_bez = flrprojekt.getFlrbereich().getC_bez();

				if (bereich_c_bez != null && bereich_c_bez.length() > 2) {
					bereich_c_bez = bereich_c_bez.substring(0, 2);
				}

				Object[] rowToAddCandidate = new Object[colCount];
				rowToAddCandidate[getTableColumnInformation().getViewIndex("i_id")] = flrprojekt.getI_id();

				rowToAddCandidate[getTableColumnInformation().getViewIndex("proj.nr")] = flrprojekt.getC_nr();

				rowToAddCandidate[getTableColumnInformation().getViewIndex("proj.bereich")] = bereich_c_bez;

				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.firma_nachname")] = HelperServer
						.formatNameAusFLRPartner(flrprojekt.getFlrpartner());

				String cAnschrift = getPartnerAddress(flrprojekt.getFlrpartner());

				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.ort")] = cAnschrift;

				rowToAddCandidate[getTableColumnInformation().getViewIndex("proj.kategorie")] = flrprojekt
						.getKategorie_c_nr().trim();

				rowToAddCandidate[getTableColumnInformation().getViewIndex("proj.titel")] = flrprojekt.getC_titel();

				rowToAddCandidate[getTableColumnInformation().getViewIndex("proj.personal.erzeuger")] = flrprojekt
						.getFlrpersonalErzeuger().getC_kurzzeichen();

				rowToAddCandidate[getTableColumnInformation().getViewIndex("proj.personal.fuer")] = flrprojekt
						.getFlrpersonalZugewiesener().getC_kurzzeichen();

				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.typ")] = flrprojekt.getTyp_c_nr();

				rowToAddCandidate[getTableColumnInformation().getViewIndex("proj.prio")] = flrprojekt.getI_prio();

				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.status")] = getStatusMitUebersetzung(
						flrprojekt.getStatus_c_nr());

				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.termin")] = flrprojekt
						.getT_zielwunschdatum();

				rowToAddCandidate[getTableColumnInformation()
						.getViewIndex("proj.schaetzung")] = flrprojekt.getD_dauer() != null ? flrprojekt.getD_dauer()
								: new Double(0.0);

				StatusIcon si = new StatusIcon();

				String tooltip = getProjektServiceFac().getTextVerrechenbar(flrprojekt.getI_verrechenbar(),
						theClientDto);
				si.setTooltip(tooltip);
				if (flrprojekt.getI_verrechenbar() == ProjektServiceFac.PROJEKT_VERRECHENBAR_NICHT_DEFINIERT) {
					si.setIcon(LocaleFac.STATUS_DATEN_UNGUELTIG);
				} else if (flrprojekt
						.getI_verrechenbar() == ProjektServiceFac.PROJEKT_VERRECHENBAR_NICHT_VERRECHENBAR) {
					si = new StatusIcon();
					si.setIcon(LocaleFac.STATUS_STORNIERT);
				} else if (flrprojekt.getI_verrechenbar() == ProjektServiceFac.PROJEKT_VERRECHENBAR_VERRECHENBAR) {
					si.setIcon(LocaleFac.STATUS_VERRECHNET);
				}

				rowToAddCandidate[getTableColumnInformation().getViewIndex("proj.label.verrechenbar")] = si;
				Boolean internalDone = new Boolean(flrprojekt.getT_internerledigt() != null);
				rowToAddCandidate[getTableColumnInformation().getViewIndex("proj.internerledigt")] = internalDone;

				if (flrprojekt.getI_sort() != null) {

					rowToAddCandidate[getTableColumnInformation().getViewIndex("Color")] = new Color(176, 0, 255);

				}

				if (flrprojekt.getX_freetext() != null) {
					String text = "<b>" + sProjekt + " " + flrprojekt.getC_nr() + ":</b>\n"
							+ flrprojekt.getX_freetext();
					text = text.replaceAll("\n", "<br>");
					text = "<html>" + text + "</html>";
					tooltipData[row] = text;
				}

				rows[row] = rowToAddCandidate;

				row++;
				col = 0;
			}

			result = new QueryResult(rows, getRowCount(), startIndex, endIndex, 0, tooltipData);

		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			closeSession(session);
		}

		myLogger.warn("ProjektHandlerSchnell.getPageAt ENDE "+theClientDto.getBenutzername());
		
		return result;
	}

	/**
	 * get the basic from clause for the HQL statement.
	 * 
	 * @return the from clause.
	 */
	private String getFromClause() {
		// return "from FLRProjekt projekt ";
		return "SELECT projekt  FROM FLRProjektSchnell AS projekt ";
		// +
		// " left outer join projekt.flrpersonalZugewiesener.flrpartner.flrlandplzort as
		// flrlandplzort "
		// +
		// " left outer join
		// projekt.flrpersonalZugewiesener.flrpartner.flrlandplzort.flrort as flrort "
		// +
		// " left outer join
		// projekt.flrpersonalZugewiesener.flrpartner.flrlandplzort.flrland as flrland "
	}

	protected long getRowCountFromDataBase() {

		myLogger.warn("ProjektHandlerSchnell.getRowCountFromDataBase BEGINN "+theClientDto.getBenutzername());
		
		long rowCount = 0;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			String queryString = "SELECT count(projekt.i_id) FROM FLRProjekt AS projekt" + buildWhereClause();

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
		
		myLogger.warn("ProjektHandlerSchnell.getRowCountFromDataBase ENDE "+theClientDto.getBenutzername());
		
		return rowCount;

	}

	private class ProjektPartnerAnsprechpartnerFilterBuilder extends FlrFirmaAnsprechpartnerFilterBuilder {

		public ProjektPartnerAnsprechpartnerFilterBuilder(boolean bSuchenInklusiveKBez) {
			super(bSuchenInklusiveKBez);
		}

		public String getFlrPartner() {
			return FLR_PROJEKT + ProjektFac.FLR_PROJEKT_FLRPARTNER;
		}

		public String getFlrPropertyAnsprechpartnerIId() {
			return FLR_PROJEKT + "ansprechpartner_i_id";
		}
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

					
					if (isFilterPartner(filterKriterien[i])) {
						ProjektPartnerAnsprechpartnerFilterBuilder filterBuilder = new ProjektPartnerAnsprechpartnerFilterBuilder(
								true);
						filterBuilder.buildFirmaAnsprechpartnerFilter(filterKriterien[i], where);
						// if (bSuchenInklusiveKbez
						// && filterKriterien[i].kritName
						// .equals(flrpartnerKrit)) {
						// getWhereFlrPartner(where, filterKriterien, i);
						// where.append(" OR lower(" + FLR_PROJEKT
						// + ProjektFac.FLR_PROJEKT_FLRPARTNER + "."
						// + PartnerFac.FLR_PARTNER_C_KBEZ + ")");
						// where.append(" " + filterKriterien[i].operator);
						// where.append(" "
						// + filterKriterien[i].value.toLowerCase() + ")");
						// } else if (bSuchenInklusiveKbez == false
						// && filterKriterien[i].kritName
						// .equals(flrpartnerKrit)) {
						// getWhereFlrPartner(where, filterKriterien, i);
						// where.append(")");
					}else if (filterKriterien[i].kritName.equals(ProjektFac.FLR_PROJEKT_C_NR)) {
						try {
							String sValue = super.buildWhereBelegnummer(filterKriterien[i], false,
									ParameterFac.KATEGORIE_PROJEKT,
									ParameterFac.PARAMETER_PROJEKT_BELEGNUMMERSTARTWERT);
							if (!istBelegnummernInJahr("FLRProjekt", sValue)) {
								sValue = super.buildWhereBelegnummer(filterKriterien[i], true);
							}
							where.append(" " + FLR_PROJEKT + filterKriterien[i].kritName);
							where.append(" " + filterKriterien[i].operator);
							where.append(" " + sValue);
						} catch (Exception ex) {
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, ex);
						}
					} else if (filterKriterien[i].kritName.equals("c_suche")) {

						where.append(buildWhereClauseExtendedSearchWithoutDuplicates(
								FLRProjekttextsuche.class.getSimpleName(), FLR_PROJEKT, filterKriterien[i]));

					} else if (filterKriterien[i].kritName.endsWith("personal_i_id_zugewiesener")) {

						where.append(" ( projekt.flrpersonalZugewiesener.i_id=" + filterKriterien[i].value + ")");

					} else if (filterKriterien[i].kritName.endsWith("flrtypspr.c_bez")) {
						where.append(
								filterKriterien[i].getQueryWithMultipleColumns(true, "flrtypspr.c_bez", "flrtyp.c_nr"));

					}

					else {
						String critName = filterKriterien[i].kritName;
						if ("i_id".equals(critName)) {
							critName = FLR_PROJEKT + critName;
						}
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" lower(" + critName + ")");
						} else {
							where.append(" " + critName);
						}
						where.append(" " + filterKriterien[i].operator);
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" " + filterKriterien[i].value.toLowerCase());
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

	private boolean isFilterPartner(FilterKriterium filterKriterium) {
		return filterKriterium.kritName.equals(FLR_PROJEKT + ProjektFac.FLR_PROJEKT_FLRPARTNER + "."
				+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1);
	}

	public QueryResult sort(SortierKriterium[] sortierKriterien, Object selectedId) throws EJBExceptionLP {

		myLogger.warn("ProjektHandlerSchnell.sort BEGINN "+theClientDto.getBenutzername());
		
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

				

				String queryString = getFromClause() + buildWhereClause() + buildOrderByClause();

				Query query = session.createQuery(queryString);
				ScrollableResults scrollableResult = query.scroll();
				if (scrollableResult != null) {
					scrollableResult.beforeFirst();
					while (scrollableResult.next()) {
						FLRProjektSchnell flr = (FLRProjektSchnell) scrollableResult.get(0);
						if (selectedId.equals(flr.getI_id())) {
							rowNumber = scrollableResult.getRowNumber();
							break;
						}
					}
				}
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
			} finally {
				closeSession(session);
			}
		}

		if (rowNumber < 0 || rowNumber >= this.getRowCount()) {
			rowNumber = 0;
		}

		result = this.getPageAt(new Integer(rowNumber));
		result.setIndexOfSelectedRow(rowNumber);

		myLogger.warn("ProjektHandlerSchnell.sort ENDE "+theClientDto.getBenutzername());
		
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
					if (!kriterien[i].kritName.endsWith(Facade.NICHT_SORTIERBAR)) {
						if (kriterien[i].isKrit) {
							if (sortAdded) {
								orderBy.append(", ");
							}
							sortAdded = true;
							// orderBy.append(FLR_PROJEKT +
							// kriterien[i].kritName);
							orderBy.append(kriterien[i].kritName);
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
				orderBy.append(" ").append(FLR_PROJEKT).append(ProjektFac.FLR_PROJEKT_I_ID).append(" ASC ");
				sortAdded = true;
			}
			if (sortAdded) {
				orderBy.insert(0, " ORDER BY ");
			}
		}
		return orderBy.toString();
	}

	private void setupParameters() {

	}

	private TableColumnInformation createColumnInformation(String mandant, Locale locUi) {

		TableColumnInformation columns = new TableColumnInformation();

		columns.add("i_id", Integer.class, "i_id", -1, "i_id");

		columns.add("proj.bereich", String.class, getTextRespectUISpr("proj.bereich", mandant, locUi), 3,
				FLR_PROJEKT + "flrbereich.c_bez");

		columns.add("proj.nr", String.class, getTextRespectUISpr("proj.nr", mandant, locUi), 10,
				FLR_PROJEKT + ProjektFac.FLR_PROJEKT_C_NR);

		columns.add("lp.firma_nachname", String.class, getTextRespectUISpr("lp.firma_nachname", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST,
				"projekt.flrpartner." + PartnerFac.FLR_PARTNER_C_NAME1NACHNAMEFIRMAZEILE1);

		columns.add("lp.ort", String.class, getTextRespectUISpr("lp.ort", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, Facade.NICHT_SORTIERBAR);

		columns.add("proj.kategorie", String.class, getTextRespectUISpr("proj.kategorie", mandant, locUi),
				QueryParameters.FLR_BREITE_M, FLR_PROJEKT + ProjektFac.FLR_PROJEKT_KATEGORIE_C_NR);

		columns.add("proj.titel", String.class, getTextRespectUISpr("proj.titel", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, FLR_PROJEKT + ProjektFac.FLR_PROJEKT_C_TITEL);

		columns.add("proj.personal.erzeuger", String.class,
				getTextRespectUISpr("proj.personal.erzeuger", mandant, locUi), QueryParameters.FLR_BREITE_XS,
				"flrpersonalErzeuger.flrpartner."
						+ PartnerFac.FLR_PARTNER_C_NAME1NACHNAMEFIRMAZEILE1);

		columns.add("proj.personal.fuer", String.class, getTextRespectUISpr("proj.personal.fuer", mandant, locUi),
				QueryParameters.FLR_BREITE_XS, "flrpersonalZugewiesener.flrpartner."
						+ PartnerFac.FLR_PARTNER_C_NAME1NACHNAMEFIRMAZEILE1);

		columns.add("lp.typ", String.class, getTextRespectUISpr("lp.typ", mandant, locUi), QueryParameters.FLR_BREITE_M,
				FLR_PROJEKT + ProjektFac.FLR_PROJEKT_TYP_C_NR);

		columns.add("proj.prio", Integer.class, getTextRespectUISpr("proj.prioritaet.short", mandant, locUi), 1,
				FLR_PROJEKT + ProjektFac.FLR_PROJEKT_I_PRIO,
				getTextRespectUISpr("proj.prioritaet.tooltip", mandant, locUi));
		columns.add("lp.status", Icon.class, getTextRespectUISpr("lp.status", mandant, locUi),
				QueryParameters.FLR_BREITE_XS, FLR_PROJEKT + ProjektFac.FLR_PROJEKT_STATUS_C_NR);
		columns.add("lp.termin", Date.class, getTextRespectUISpr("lp.termin", mandant, locUi), 10,
				FLR_PROJEKT + ProjektFac.FLR_PROJEKT_T_ZIELDATUM);


		columns.add("proj.schaetzung", Double.class, getTextRespectUISpr("proj.schaetzung", mandant, locUi),
				QueryParameters.FLR_BREITE_XS, FLR_PROJEKT + ProjektFac.FLR_PROJEKT_D_DAUER);

		columns.add("proj.label.verrechenbar", Icon.class,
				getTextRespectUISpr("proj.verrechenbar.short", mandant, locUi), QueryParameters.FLR_BREITE_S,
				FLR_PROJEKT + ProjektFac.FLR_PROJEKT_I_VERRECHENBAR,
				getTextRespectUISpr("proj.verrechenbar.tooltip", mandant, locUi));
		columns.add("proj.internerledigt", Boolean.class,
				getTextRespectUISpr("proj.internerledigt.short", mandant, locUi), QueryParameters.FLR_BREITE_S,
				FLR_PROJEKT + ProjektFac.FLR_PROJEKT_T_INTERNERLEDIGT,
				getTextRespectUISpr("proj.internerledigt.tooltip", mandant, locUi));

		columns.add("Color", Color.class, "", 1, "");

		return columns;
	}

	public TableInfo getTableInfo() {
		TableInfo info = super.getTableInfo();
		if (info != null)
			return info;

		setupParameters();
		setTableColumnInformation(createColumnInformation(theClientDto.getMandant(), theClientDto.getLocUi()));

		TableColumnInformation c = getTableColumnInformation();
		info = new TableInfo(c.getClasses(), c.getHeaderNames(), c.getWidths(), c.getDbColumNames(),
				c.getHeaderToolTips());
		setTableInfo(info);
		return info;
	}

	public PrintInfoDto getSDocPathAndPartner(Object key) {
		ProjektDto projektDto = null;
		BereichDto bereichDto = null;
		try {
			projektDto = getProjektFac().projektFindByPrimaryKey((Integer) key);
			bereichDto = getProjektServiceFac().bereichFindByPrimaryKey(projektDto.getBereichIId());
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

			DocPath docPath = new DocPath(new DocNodeProjekt(projektDto, bereichDto));
			return new PrintInfoDto(docPath, projektDto.getPartnerIId(), getSTable());
		} else {
			return null;
		}
	}

	public String getSTable() {
		return "PROJEKT";
	}

}
