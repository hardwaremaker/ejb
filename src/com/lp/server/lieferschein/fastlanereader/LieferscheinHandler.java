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
package com.lp.server.lieferschein.fastlanereader;

import java.awt.Color;
import java.math.BigDecimal;
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

import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferschein;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinServiceFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.system.fastlanereader.generated.FLRLandplzort;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeLieferschein;
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
 * FLR fuer LS_LIEFERSCHEIN.
 * </p>
 *
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 *
 * <p>
 * Erstellung: Uli Walch; 21.10.04
 * </p>
 *
 * <p>
 *
 * @author $Author: robert $
 *         </p>
 *
 * @version $Revision: 1.27 $ Date $Date: 2013/01/19 11:47:31 $
 */
public class LieferscheinHandler extends UseCaseHandler {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public static final String FLR_LIEFERSCHEIN = "flrlieferschein.";
	public static final String FLR_LIEFERSCHEIN_FROM_CLAUSE = " from FLRLieferschein as flrlieferschein ";
	Integer iAnlegerStattVertreterAnzeigen = 0;

	/**
	 * gets the data page for the specified row using the current query. The row
	 * at rowIndex will be located in the middle of the page.
	 *
	 * @param rowIndex
	 *            diese Zeile soll selektiert sein
	 * @return QueryResult das Ergebnis der Abfrage
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @see UseCaseHandler#getPageAt(java.lang.Integer)
	 */
	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = LieferscheinHandler.PAGE_SIZE;
			int startIndex = Math.max(rowIndex.intValue() - (pageSize / 2), 0);
			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			String queryString = this.getFromClause() + this.buildWhereClause()
					+ this.buildOrderByClause();
			// myLogger.info("HQL: " + queryString);
//			logQuery(queryString);
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;
			int col = 0;

			// darf Preise sehen.
			final boolean bDarfPreiseSehen = getTheJudgeFac().hatRecht(
					RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF, theClientDto);

			while (resultListIterator.hasNext()) {
				Object[] o = (Object[]) resultListIterator.next();
				FLRLieferschein lieferschein = (FLRLieferschein) (o[0]);
				rows[row][col++] = lieferschein.getI_id();
				String lieferscheinart = null;
				if (lieferschein.getLieferscheinart_c_nr().equals(
						LieferscheinFac.LSART_LIEFERANT)) {
					lieferscheinart = LieferscheinFac.LSART_LIEFERANT_SHORT;
				}
				rows[row][col++] = lieferscheinart;
				rows[row][col++] = lieferschein.getC_nr();
				rows[row][col++] = lieferschein.getFlrkunde().getFlrpartner()
						.getC_name1nachnamefirmazeile1();

				// IMS 1757 die Anschrift des Kunde anzeigen
				String cAnschrift = null;

				if (lieferschein.getFlrkunde() != null) {
					FLRLandplzort flranschrift = lieferschein.getFlrkunde()
							.getFlrpartner().getFlrlandplzort();

					if (flranschrift != null) {
						cAnschrift = flranschrift.getFlrland().getC_lkz() + "-"
								+ flranschrift.getC_plz() + " "
								+ flranschrift.getFlrort().getC_name();
					}
				}

				rows[row][col++] = cAnschrift;

				String proj_bestellnummer = "";
				if (lieferschein.getC_bez_projektbezeichnung() != null) {
					proj_bestellnummer = lieferschein.getC_bez_projektbezeichnung();
				}

				if (lieferschein.getC_bestellnummer() != null) {
					proj_bestellnummer += " | " + lieferschein.getC_bestellnummer();
				}

				rows[row][col++] = proj_bestellnummer;
				rows[row][col++] = lieferschein.getD_belegdatum();

				if (iAnlegerStattVertreterAnzeigen == 1) {
					if (lieferschein.getFlrpersonalanleger() != null) {
						rows[row][col++] = lieferschein.getFlrpersonalanleger()
								.getC_kurzzeichen();
					} else {
						rows[row][col++] = null;
					}

				} else if (iAnlegerStattVertreterAnzeigen == 2) {
					if (lieferschein.getFlrpersonalaenderer() != null) {
						rows[row][col++] = lieferschein
								.getFlrpersonalaenderer().getC_kurzzeichen();
					} else {
						rows[row][col++] = null;
					}

				} else {
					if (lieferschein.getFlrvertreter() != null) {
						rows[row][col++] = lieferschein.getFlrvertreter()
								.getC_kurzzeichen();
					} else {
						rows[row][col++] = null;
					}
				}

				rows[row][col++] = getStatusMitUebersetzung(
						lieferschein.getLieferscheinstatus_status_c_nr(),
						lieferschein.getT_versandzeitpunkt(),
						lieferschein.getC_versandtype());
				rows[row][col++] = lieferschein.getFlrrechnung() == null ? null
						: lieferschein.getFlrrechnung().getC_nr();
				if (bDarfPreiseSehen) {
					rows[row][col++] = lieferschein
							.getN_gesamtwertinlieferscheinwaehrung();
					rows[row][col++] = lieferschein
							.getWaehrung_c_nr_lieferscheinwaehrung();
				}
				if (!Helper.short2boolean(lieferschein.getB_verrechenbar())) {
					rows[row][col++] = Color.LIGHT_GRAY;
				} else {

					if((lieferschein.getFlrverkettet()!=null && lieferschein.getFlrverkettet().size()>0) || lieferschein.getFlrverkettet2()!=null && lieferschein.getFlrverkettet2().size()>0){
						rows[row][col++] = Color.BLUE;
					}else {
						rows[row][col++] = null;
					}


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
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, he);
			}
		}
		return result;
	}

	/**
	 * gets the total number of rows represented by the current query.
	 *
	 * @return int die Anzehl der Zeilen im Ergebnis
	 * @see UseCaseHandler#getRowCountFromDataBase()
	 */
	protected long getRowCountFromDataBase() {
		long rowCount = 0;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			String queryString = "select count(*) " + this.getFromClause()
					+ this.buildWhereClause();
//			logQuery(queryString);
			Query query = session.createQuery(queryString);
			List<?> rowCountResult = query.list();
			if (rowCountResult != null && rowCountResult.size() > 0) {
				rowCount = ((Long) rowCountResult.get(0)).longValue();
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (HibernateException he) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HIBERNATE,
							he);
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
					if (filterKriterien[i].kritName.equals("c_nr")) {
						try {
							String sValue = super.buildWhereBelegnummer(
									filterKriterien[i], false);
							// Belegnummernsuche auch in "altem" Jahr, wenn im
							// neuen noch keines vorhanden ist
							if (!istBelegnummernInJahr("FLRLieferschein",
									sValue)) {
								sValue = super.buildWhereBelegnummer(
										filterKriterien[i], true);
							}
							where.append(" " + FLR_LIEFERSCHEIN
									+ filterKriterien[i].kritName);
							where.append(" " + filterKriterien[i].operator);
							where.append(" " + sValue);
						} catch (Exception ex) {
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR,
									ex);
						}
					} else if (filterKriterien[i].kritName
							.equals(LieferscheinServiceFac.LS_HANDLER_OHNE_VERKETTETE)) {

						where.append(" flrlieferschein.i_id not in (SELECT v.lieferschein_i_id_verkettet FROM FLRVerkettet v) AND flrlieferschein.i_id not in (SELECT v.lieferschein_i_id FROM FLRVerkettet v) ");

					} else if (filterKriterien[i].kritName
							.equals("c_bez_projektbezeichnung")) {
						where.append(" (upper(flrlieferschein.c_kommission) LIKE "
								+ filterKriterien[i].value.toUpperCase()
								+ " OR upper(flrlieferschein.c_bez_projektbezeichnung) LIKE "
								+ filterKriterien[i].value.toUpperCase()
								+ " OR upper(flrlieferschein.c_bestellnummer) LIKE "
								+ filterKriterien[i].value.toUpperCase() + ")");
					} else if (filterKriterien[i].kritName
							.equals("flrauftrag.c_nr")) {
						try {
							String sValue = super.buildWhereBelegnummer(
									filterKriterien[i], false);
							// Belegnummernsuche auch in "altem" Jahr, wenn im
							// neuen noch keines vorhanden ist
							if (!istBelegnummernInJahr("FLRLieferschein",
									sValue)) {
								sValue = super.buildWhereBelegnummer(
										filterKriterien[i], true);
							}
							where.append(" " + FLR_LIEFERSCHEIN
									+ filterKriterien[i].kritName);
							where.append(" " + filterKriterien[i].operator);
							where.append(" " + sValue);
						} catch (Exception ex) {
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR,
									ex);
						}
					} else if (filterKriterien[i].kritName
							.equals(LieferscheinFac.FLR_LIEFERSCHEIN_FLRKUNDE
									+ "."
									+ LieferantFac.FLR_PARTNER
									+ "."
									+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1)) {
						ParametermandantDto parameter = null;
						try {
							parameter = getParameterFac()
									.getMandantparameter(
											theClientDto.getMandant(),
											ParameterFac.KATEGORIE_ALLGEMEIN,
											ParameterFac.PARAMETER_SUCHEN_INKLUSIVE_KBEZ);
						} catch (RemoteException ex) {
							throwEJBExceptionLPRespectOld(ex);
						}
						Boolean bSuchenInklusiveKbez = (java.lang.Boolean) parameter
								.getCWertAsObject();
						if (bSuchenInklusiveKbez) {
							if (filterKriterien[i].isBIgnoreCase()) {
								where.append(" ( upper(" + FLR_LIEFERSCHEIN
										+ filterKriterien[i].kritName + ")");
								where.append(" " + filterKriterien[i].operator);
								where.append(" "
										+ filterKriterien[i].value
												.toUpperCase());
								where.append(" OR upper(" + FLR_LIEFERSCHEIN
										+ "flrkunde.flrpartner.c_kbez" + ")");
								where.append(" " + filterKriterien[i].operator);
								where.append(" "
										+ filterKriterien[i].value
												.toUpperCase() + ") ");
							} else {
								where.append(" " + FLR_LIEFERSCHEIN
										+ filterKriterien[i].kritName);
								where.append(" " + filterKriterien[i].operator);
								where.append(" " + filterKriterien[i].value);
								where.append("OR " + FLR_LIEFERSCHEIN
										+ "flrkunde.flrpartner.c_kbez");
								where.append(" " + filterKriterien[i].operator);
								where.append(" " + filterKriterien[i].value);
							}
						} else {
							if (filterKriterien[i].isBIgnoreCase()) {
								where.append(" upper(" + FLR_LIEFERSCHEIN
										+ filterKriterien[i].kritName + ")");
							} else {
								where.append(" " + FLR_LIEFERSCHEIN
										+ filterKriterien[i].kritName);
							}

							where.append(" " + filterKriterien[i].operator);

							if (filterKriterien[i].isBIgnoreCase()) {
								where.append(" "
										+ filterKriterien[i].value
												.toUpperCase());
							} else {
								where.append(" " + filterKriterien[i].value);
							}
						}
					} else {
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" lower(" + FLR_LIEFERSCHEIN
									+ filterKriterien[i].kritName + ")");
						} else {
							where.append(" " + FLR_LIEFERSCHEIN
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
							orderBy.append(FLR_LIEFERSCHEIN
									+ kriterien[i].kritName);
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
				orderBy.append(FLR_LIEFERSCHEIN)
						.append(LieferscheinFac.FLR_LIEFERSCHEIN_C_NR)
						.append(" DESC ");
				sortAdded = true;
			}
			if (orderBy.indexOf(FLR_LIEFERSCHEIN
					+ LieferscheinFac.FLR_LIEFERSCHEIN_I_ID) < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" ").append(FLR_LIEFERSCHEIN)
						.append(LieferscheinFac.FLR_LIEFERSCHEIN_I_ID)
						.append(" DESC ");
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
		// return FLR_LIEFERSCHEIN_FROM_CLAUSE
		return "from FLRLieferschein as flrlieferschein "
				+ " left join flrlieferschein.flrkunde.flrpartner.flrlandplzort as flrlandplzort "
				+ " left join flrlieferschein.flrkunde.flrpartner.flrlandplzort.flrort as flrort "
				+ " left join flrlieferschein.flrkunde.flrpartner.flrlandplzort.flrland as flrland "
				+ " left join flrlieferschein.flrrechnung as flrrechnung ";
	}

	/**
	 * sorts the data described by the current query using the specified sort
	 * criterias. The current query is also updated with the new sort criterias.
	 *
	 * @param sortierKriterien
	 *            nach diesen Kriterien wird das Ergebnis sortiert
	 * @param selectedId
	 *            auf diesem Datensatz soll der Cursor stehen
	 * @return QueryResult das Ergebnis der Abfrage
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @see UseCaseHandler#sort(SortierKriterium[], Object)
	 */
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
				String queryString = "select " + FLR_LIEFERSCHEIN
						+ LieferscheinFac.FLR_LIEFERSCHEIN_I_ID
						+ FLR_LIEFERSCHEIN_FROM_CLAUSE
						+ this.buildWhereClause() + this.buildOrderByClause();
//				logQuery(queryString);
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
			TableInfo tableInfo = null;
			// darf Preise sehen.
			boolean bDarfPreiseSehen = false;

			try {

				bDarfPreiseSehen = getTheJudgeFac().hatRecht(
						RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF,
						theClientDto);

				ParametermandantDto parameter = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_ALLGEMEIN,
								ParameterFac.PARAMETER_ANZEIGE_ANLEGER_STATT_VERTRETER);
				iAnlegerStattVertreterAnzeigen = (Integer) parameter
						.getCWertAsObject();

			} catch (RemoteException ex) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
			}

			if (bDarfPreiseSehen) {
				tableInfo = new TableInfo(
						new Class[] { Integer.class, String.class,
								String.class, String.class, String.class,
								String.class, java.util.Date.class,
								String.class, Icon.class, String.class,
								BigDecimal.class, String.class, Color.class },
						new String[] {
								"i_id",
								" ",
								getTextRespectUISpr("ls.lieferscheinnummer",
										mandantCNr, locUI),
								getTextRespectUISpr("lp.kunde", mandantCNr,
										locUI),
								getTextRespectUISpr("lp.ort", mandantCNr, locUI),
								getTextRespectUISpr("ls.projektbestellnummer", mandantCNr,
										locUI),
								getTextRespectUISpr("lp.datum", mandantCNr,
										locUI),
								getTextRespectUISpr("lp.vertreter", mandantCNr,
										locUI),
								getTextRespectUISpr("lp.status", mandantCNr,
										locUI),
								getTextRespectUISpr("rechnung.rechnungsnummer",
										mandantCNr, locUI),
								getTextRespectUISpr("lp.wert", mandantCNr,
										locUI), " ", "" },
						new int[] {
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, // hidden
								1,
								QueryParameters.FLR_BREITE_M,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, // variabel
								QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_M,
								QueryParameters.FLR_BREITE_XS,
								QueryParameters.FLR_BREITE_XS, 12,
								QueryParameters.FLR_BREITE_PREIS,
								QueryParameters.FLR_BREITE_WAEHRUNG, 1 },
						new String[] {
								LieferscheinFac.FLR_LIEFERSCHEIN_I_ID,
								Facade.NICHT_SORTIERBAR, // LieferscheinFac.
								// FLR_AUFTRAGART_C_NR
								LieferscheinFac.FLR_LIEFERSCHEIN_C_NR,
								LieferscheinFac.FLR_LIEFERSCHEIN_FLRKUNDE
										+ "."
										+ KundeFac.FLR_PARTNER
										+ "."
										+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
								// Sortierung fuers erste mal nach LKZ
								LieferscheinFac.FLR_LIEFERSCHEIN_FLRKUNDE
										+ "."
										+ KundeFac.FLR_PARTNER
										+ "."
										+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT
										+ "."
										+ SystemFac.FLR_LP_FLRLAND
										+ "."
										+ SystemFac.FLR_LP_LANDLKZ
										+ ", "
										+
										// und dann nach plz
										LieferscheinHandler.FLR_LIEFERSCHEIN
										+ LieferscheinFac.FLR_LIEFERSCHEIN_FLRKUNDE
										+ "." + KundeFac.FLR_PARTNER + "."
										+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT
										+ "." + SystemFac.FLR_LP_LANDPLZORTPLZ,
								LieferscheinFac.FLR_LIEFERSCHEIN_C_BEZ_PROJEKTBEZEICHNUNG,
								LieferscheinFac.FLR_LIEFERSCHEIN_D_BELEGDATUM,
								Facade.NICHT_SORTIERBAR,
								LieferscheinFac.FLR_LIEFERSCHEIN_LIEFERSCHEINSTATUS_STATUS_C_NR,
								LieferscheinFac.FLR_LIEFERSCHEIN_FLRRECHNUNG
										+ "." + RechnungFac.FLR_RECHNUNG_C_NR,
								Facade.NICHT_SORTIERBAR,
								LieferscheinFac.FLR_LIEFERSCHEIN_WAEHRUNG_C_NR_LIEFERSCHEINWAEHRUNG,
								"" });

			} else {
				tableInfo = new TableInfo(
						new Class[] { Integer.class, String.class,
								String.class, String.class, String.class,
								String.class, java.util.Date.class,
								String.class, Icon.class, String.class,
								Color.class },
						new String[] {
								"i_id",
								" ",
								getTextRespectUISpr("ls.lieferscheinnummer",
										mandantCNr, locUI),
								getTextRespectUISpr("lp.kunde", mandantCNr,
										locUI),
								getTextRespectUISpr("lp.ort", mandantCNr, locUI),
								getTextRespectUISpr("ls.projektbestellnummer", mandantCNr,
										locUI),
								getTextRespectUISpr("lp.datum", mandantCNr,
										locUI),
								getTextRespectUISpr("lp.vertreter", mandantCNr,
										locUI),
								getTextRespectUISpr("lp.status", mandantCNr,
										locUI),
								getTextRespectUISpr("rechnung.rechnungsnummer",
										mandantCNr, locUI), "" },
						new int[] {
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, // hidden
								1,
								QueryParameters.FLR_BREITE_M,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, // variabel
								QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_M,
								QueryParameters.FLR_BREITE_XS,
								QueryParameters.FLR_BREITE_XS, 12, 1 },
						new String[] {
								LieferscheinFac.FLR_LIEFERSCHEIN_I_ID,
								Facade.NICHT_SORTIERBAR, // LieferscheinFac.
								// FLR_AUFTRAGART_C_NR
								LieferscheinFac.FLR_LIEFERSCHEIN_C_NR,
								LieferscheinFac.FLR_LIEFERSCHEIN_FLRKUNDE
										+ "."
										+ KundeFac.FLR_PARTNER
										+ "."
										+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
								// Sortierung fuers erste mal nach LKZ
								LieferscheinFac.FLR_LIEFERSCHEIN_FLRKUNDE
										+ "."
										+ KundeFac.FLR_PARTNER
										+ "."
										+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT
										+ "."
										+ SystemFac.FLR_LP_FLRLAND
										+ "."
										+ SystemFac.FLR_LP_LANDLKZ
										+ ", "
										+
										// und dann nach plz
										LieferscheinHandler.FLR_LIEFERSCHEIN
										+ LieferscheinFac.FLR_LIEFERSCHEIN_FLRKUNDE
										+ "." + KundeFac.FLR_PARTNER + "."
										+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT
										+ "." + SystemFac.FLR_LP_LANDPLZORTPLZ,
								LieferscheinFac.FLR_LIEFERSCHEIN_C_BEZ_PROJEKTBEZEICHNUNG,
								LieferscheinFac.FLR_LIEFERSCHEIN_D_BELEGDATUM,
								Facade.NICHT_SORTIERBAR,
								LieferscheinFac.FLR_LIEFERSCHEIN_LIEFERSCHEINSTATUS_STATUS_C_NR,
								LieferscheinFac.FLR_LIEFERSCHEIN_FLRRECHNUNG
										+ "." + RechnungFac.FLR_RECHNUNG_C_NR,
								"" });
			}

			setTableInfo(tableInfo);
		}

		return super.getTableInfo();
	}

	public PrintInfoDto getSDocPathAndPartner(Object key) {
		LieferscheinDto lieferscheinDto = null;
		KundeDto kundeDto = null;
		try {
			lieferscheinDto = getLieferscheinFac()
					.lieferscheinFindByPrimaryKey((Integer) key, theClientDto);
			kundeDto = getKundeFac().kundeFindByPrimaryKeySmall(
					lieferscheinDto.getKundeIIdLieferadresse());
		} catch (Exception e) {
			// Nicht gefunden
		}
		if (lieferscheinDto != null) {
//			String sPath = JCRDocFac.HELIUMV_NODE + "/"
//					+ theClientDto.getMandant() + "/"
//					+ LocaleFac.BELEGART_LIEFERSCHEIN.trim() + "/"
//					+ LocaleFac.BELEGART_LIEFERSCHEIN.trim() + "/"
//					+ lieferscheinDto.getCNr().replace("/", ".");
			DocPath docPath = new DocPath(new DocNodeLieferschein(lieferscheinDto));
			return new PrintInfoDto(docPath, kundeDto.getPartnerIId(), getSTable());
		} else {
			return null;
		}
	}

	public String getSTable() {
		return "LIEFERSCHEIN";
	}
}
