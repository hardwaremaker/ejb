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

import java.math.BigDecimal;
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

import com.lp.server.partner.service.PartnerFac;
import com.lp.server.projekt.fastlanereader.generated.FLRProjektQueue;
import com.lp.server.projekt.fastlanereader.generated.FLRProjekttextsuche;
import com.lp.server.projekt.service.ProjektFac;
import com.lp.server.projekt.service.ProjektServiceFac;
import com.lp.server.system.pkgenerator.format.LpBelegnummerFormat;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.Facade;
import com.lp.server.util.HelperServer;
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
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2011/01/10 07:13:59 $
 */
public class ProjektQueueHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public static final String FLR_PROJEKT = "projekt.";
	public static final String FLR_PROJEKT_FROM_CLAUSE = " from FLRProjektQueue projekt ";
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
			int pageSize = PAGE_SIZE;
			long startIndex = Math.max(rowIndex.intValue() - (pageSize / 2), 0);
			long endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			String queryString = getFromClause() + buildWhereClause()
					+ buildOrderByClause();
			lastQuery = queryString;
			Query query = session.createQuery(queryString);
			query.setFirstResult((int) startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			String[] tooltipData = new String[resultList.size()];
			String sProjekt = getTextRespectUISpr("proj.projekt", theClientDto
					.getMandant(), theClientDto.getLocUi());

			int row = 0;
			int col = 0;
			while (resultListIterator.hasNext()) {
				
				
				
				Object[] o = (Object[]) resultListIterator.next();

				Integer projektIId = (Integer) o[0];
				String c_nr = (String) o[1];

				String typ_c_nr = (String) o[2];
				Integer i_prio = (Integer) o[3];
				Double d_dauer = (Double) o[4];
				Integer i_verrechenbar = (Integer) o[5];
				String x_freetext = (String) o[6];
				
				String status_c_nr = (String) o[8];
				java.util.Date t_zielwunschdatum = (java.util.Date) o[9];
				String titel = (String) o[10];
				String kategorie_c_nr = (String) o[11];
				String projekt_partner = (String) o[12];
				
			

				rows[row][col++] = projektIId;
				rows[row][col++] = c_nr;
				rows[row][col++] = projekt_partner;
				
				rows[row][col++] = kategorie_c_nr;
				rows[row][col++] = titel;
				rows[row][col++] = typ_c_nr;
				rows[row][col++] = i_prio;
				rows[row][col++] = status_c_nr;
				rows[row][col++] = t_zielwunschdatum;
				rows[row][col++] = d_dauer;
				
				Double ddArbeitszeitist = getZeiterfassungFac()
						.getSummeZeitenEinesBeleges(LocaleFac.BELEGART_PROJEKT,
								projektIId, null, null, null, null,
								theClientDto);
				rows[row][col++] = ddArbeitszeitist;
				
				
				StatusIcon si = new StatusIcon();

				String tooltip = getProjektServiceFac().getTextVerrechenbar(i_verrechenbar, theClientDto);
				si.setTooltip(tooltip);
				if (i_verrechenbar == ProjektServiceFac.PROJEKT_VERRECHENBAR_NICHT_DEFINIERT) {
					si.setIcon(LocaleFac.STATUS_DATEN_UNGUELTIG);
				} else if (i_verrechenbar == ProjektServiceFac.PROJEKT_VERRECHENBAR_NICHT_VERRECHENBAR) {
					si = new StatusIcon();
					si.setIcon(LocaleFac.STATUS_STORNIERT);
				} else if (i_verrechenbar == ProjektServiceFac.PROJEKT_VERRECHENBAR_VERRECHENBAR) {
					si.setIcon(LocaleFac.STATUS_VERRECHNET);
				}

				
				rows[row][col++] = si;
				if (x_freetext != null) {
					String text = "<b>" + sProjekt + " " + c_nr
							+ ":</b>\n" + x_freetext;
					text = text.replaceAll("\n", "<br>");
					text = "<html>" + text + "</html>";
					tooltipData[row] = text;
				}
				row++;
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

	/**
	 * get the basic from clause for the HQL statement.
	 * 
	 * @return the from clause.
	 */
	private String getFromClause() {
//		return "from FLRProjekt projekt ";
		return "SELECT DISTINCT projekt.i_id,"
			+ "projekt.c_nr,"
			+ "projekt.typ_c_nr,"
			+ "projekt.i_prio,"
			+ "projekt.d_dauer,"
			+ "projekt.i_verrechenbar,"
			+ "CAST(projekt.x_freetext as string),"
			+ "projekt.i_sort,"
			+ "projekt.status_c_nr,"
			+ "projekt.t_zielwunschdatum,"
			+ "projekt.c_titel,"
			+ "projekt.kategorie_c_nr,"
			+ "flrpar.c_name1nachnamefirmazeile1,"
			+ "flrpar.c_kbez,"
			+ "flrl.c_lkz,"
			+ "flrlpo.c_plz,"
			+ "flro.c_name,"
			+ "flrperszug.c_kurzzeichen,"
			+ "flrpartnerperszug.c_name1nachnamefirmazeile1,"
			+ "flrperserz.c_kurzzeichen,"
			+ "flrperserz.c_kurzzeichen,"
			+ "flrpartnerperserz.c_name1nachnamefirmazeile1"
				+ " FROM FLRProjektQueue AS projekt "
				+ " LEFT OUTER JOIN projekt.technikerset AS ts "
				+ " LEFT OUTER JOIN projekt.flrpartner AS flrpar "
				+ " LEFT OUTER JOIN flrpar.flrlandplzort AS flrlpo "
				+ " LEFT OUTER JOIN flrlpo.flrland AS flrl "
				+ " LEFT OUTER JOIN flrlpo.flrort AS flro "
				+ " LEFT OUTER JOIN projekt.flrpersonalZugewiesener AS flrperszug "
				+ " LEFT OUTER JOIN flrperszug.flrpartner AS flrpartnerperszug "
				+ " LEFT OUTER JOIN projekt.flrpersonalErzeuger AS flrperserz "
				+ " LEFT OUTER JOIN flrperserz.flrpartner AS flrpartnerperserz ";
//				+ " left outer join projekt.flrpersonalZugewiesener.flrpartner.flrlandplzort as flrlandplzort "
//				+ " left outer join projekt.flrpersonalZugewiesener.flrpartner.flrlandplzort.flrort as flrort "
//				+ " left outer join projekt.flrpersonalZugewiesener.flrpartner.flrlandplzort.flrland as flrland "
	}

	protected long getRowCountFromDataBase() {
		String queryString = "SELECT COUNT(DISTINCT projekt.i_id)"
				+ " FROM FLRProjekt AS projekt"
				+ " LEFT OUTER JOIN projekt.technikerset AS ts "
				+ buildWhereClause();
		return getRowCountFromDataBaseByQuery(queryString);
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
					if (filterKriterien[i].kritName.equals("c_nr")) {
						try {
							String sValue = super.buildWhereBelegnummer(filterKriterien[i], false);
							// Belegnummernsuche auch in "altem" Jahr, wenn im
							// neuen noch keines vorhanden ist
							if (!istBelegnummernInJahr("FLRProjekt", sValue)) {
								sValue = super.buildWhereBelegnummer(filterKriterien[i], true);
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

						where.append(buildWhereClauseExtendedSearchWithoutDuplicates(
								FLRProjekttextsuche.class.getSimpleName(), FLR_PROJEKT, filterKriterien[i]));
						
					} else if (filterKriterien[i].kritName
							.endsWith("personal_i_id_zugewiesener")) {

						where.append(" ( projekt.flrpersonalZugewiesener.i_id="
								+ filterKriterien[i].value
								+ " OR ts.personal_i_id="
								+ filterKriterien[i].value + ")");

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

		this.getQuery().setSortKrit(sortierKriterien);

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
				orderBy.append(FLR_PROJEKT).append("i_sort ASC"); // t_zielwunschdatum
				// und
				// i_prio
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
				orderBy.append(" ").append(FLR_PROJEKT).append(
						ProjektFac.FLR_PROJEKT_I_ID).append(" ASC ");
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
			String mandantCNr = theClientDto.getMandant();
			String sortierungNachPartner = Facade.NICHT_SORTIERBAR;
			Locale locUI = theClientDto.getLocUi();
			if (SORTIERUNG_UI_PARTNER_ORT) {
				sortierungNachPartner = ProjektFac.FLR_PROJEKT_FLRPARTNER + "."
						+ PartnerFac.FLR_PARTNER_C_NAME1NACHNAMEFIRMAZEILE1;
			}
			setTableInfo(new TableInfo(
					new Class[] { Integer.class, // i_id
							String.class, // c_nr
							String.class, // firma
							String.class, // kategorie
							String.class, // titel
							String.class, // typ
							Integer.class, // prio
							String.class, // status
							Date.class, // termin
							Double.class, // schaetzung
							Double.class, // dauer
							Icon.class
					},
					new String[] {
							"i_id",
							getTextRespectUISpr("proj.nr", mandantCNr, locUI),
							getTextRespectUISpr("lp.firma_nachname",
									mandantCNr, locUI),
							getTextRespectUISpr("proj.kategorie", mandantCNr,
									locUI),
							getTextRespectUISpr("proj.titel", mandantCNr, locUI),
							getTextRespectUISpr("lp.typ", mandantCNr, locUI),
							getTextRespectUISpr("proj.prioritaet.short", mandantCNr, locUI),
							getTextRespectUISpr("lp.status", mandantCNr, locUI),
							getTextRespectUISpr("lp.termin", mandantCNr, locUI),
							getTextRespectUISpr("proj.schaetzung", mandantCNr,locUI),
							getTextRespectUISpr("lp.dauer", mandantCNr, locUI),
							getTextRespectUISpr("proj.verrechenbar.short", mandantCNr, locUI)
							
					},
							
					new int[] {
							QueryParameters.FLR_BREITE_SHARE_WITH_REST, // i_id 
							QueryParameters.FLR_BREITE_M, // c_nr
							QueryParameters.FLR_BREITE_SHARE_WITH_REST, // partner_i_id
							QueryParameters.FLR_BREITE_M, // kategorieCNr
							QueryParameters.FLR_BREITE_SHARE_WITH_REST, // c_titel
							QueryParameters.FLR_BREITE_M, // typCNr
							QueryParameters.FLR_BREITE_XS, // i_prio
							QueryParameters.FLR_BREITE_SHARE_WITH_REST, // statusCNr
							QueryParameters.FLR_BREITE_M, // termin
							QueryParameters.FLR_BREITE_XS,//dauer
							QueryParameters.FLR_BREITE_XS,//dauer
							QueryParameters.FLR_BREITE_XS,//verrechenbar
							/*
							QueryParameters.FLR_BREITE_SHARE_WITH_REST, 
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_XM,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_S,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M,
					*/

					
					},
					new String[] { 
							ProjektFac.FLR_PROJEKT_I_ID,
							ProjektFac.FLR_PROJEKT_C_NR, 
							sortierungNachPartner,
							ProjektFac.FLR_PROJEKT_KATEGORIE_C_NR,
							ProjektFac.FLR_PROJEKT_C_TITEL,
							ProjektFac.FLR_PROJEKT_TYP_C_NR,
							ProjektFac.FLR_PROJEKT_I_PRIO,
							ProjektFac.FLR_PROJEKT_STATUS_C_NR,
							ProjektFac.FLR_PROJEKT_T_ZIELDATUM,
							ProjektFac.FLR_PROJEKT_D_DAUER,
							Facade.NICHT_SORTIERBAR,
							Facade.NICHT_SORTIERBAR},
					new String[]{ null, null, null, null, null, null,
							getTextRespectUISpr("proj.prioritaet.tooltip", mandantCNr, locUI), null, null, null, null,
							getTextRespectUISpr("proj.verrechenbar.tooltip", mandantCNr, locUI),
					}));
		}

		return super.getTableInfo();
	}

}
