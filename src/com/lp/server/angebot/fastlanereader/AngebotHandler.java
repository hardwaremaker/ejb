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
package com.lp.server.angebot.fastlanereader;

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

import com.lp.server.angebot.fastlanereader.generated.FLRAngebot;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebot.service.AngebotFac;
import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.system.fastlanereader.generated.FLRLandplzort;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeAngebot;
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

/**
 * <p>
 * FLR fuer ANGB_ANGEBOT.
 * </p>
 * <p>
 * Copright Logistik Pur GmbH (c) 2005
 * </p>
 * <p>
 * Erstellungsdatum 07.06.05
 * </p>
 * <p>
 * </p>
 * 
 * @author Uli Walch
 * @version 1.0
 */

public class AngebotHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String FLR_ANGEBOT = "flrangebot.";
	public static final String FLR_ANGEBOT_FROM_CLAUSE = " from FLRAngebot flrangebot ";
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
			int pageSize = AngebotHandler.PAGE_SIZE;
			int startIndex = Math.max(rowIndex.intValue() - (pageSize / 2), 0);
			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			String queryString = this.getFromClause() + this.buildWhereClause()
					+ this.buildOrderByClause();
			// myLogger.info("HQL= " + queryString);
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
				FLRAngebot angebot = (FLRAngebot) ((Object[]) resultListIterator
						.next())[0];

				rows[row][col++] = angebot.getI_id();
				rows[row][col++] = angebot.getC_nr();
				rows[row][col++] = angebot.getFlrkunde() == null ? null
						: angebot.getFlrkunde().getFlrpartner()
								.getC_name1nachnamefirmazeile1();

				// IMS 1757 die Anschrift des Kunden anzeigen
				String cAnschrift = null;

				if (angebot.getFlrkunde() != null) {
					FLRLandplzort flranschrift = angebot.getFlrkunde()
							.getFlrpartner().getFlrlandplzort();

					if (flranschrift != null) {
						cAnschrift = flranschrift.getFlrland().getC_lkz() + "-"
								+ flranschrift.getC_plz() + " "
								+ flranschrift.getFlrort().getC_name();
					}
				}

				rows[row][col++] = cAnschrift;
				rows[row][col++] = angebot.getC_bez();
				rows[row][col++] = angebot.getT_belegdatum();
				rows[row][col++] = angebot.getT_nachfasstermin();

				if (iAnlegerStattVertreterAnzeigen == 1) {
					if (angebot.getFlrpersonalanleger() != null) {
						rows[row][col++] = angebot.getFlrpersonalanleger()
								.getC_kurzzeichen();
					} else {
						rows[row][col++] = null;
					}
				} else if (iAnlegerStattVertreterAnzeigen == 2) {
					if (angebot.getFlrpersonalaenderer() != null) {
						rows[row][col++] = angebot.getFlrpersonalaenderer()
								.getC_kurzzeichen();
					} else {
						rows[row][col++] = null;
					}
				} else {
					if (angebot.getFlrvertreter() != null) {
						rows[row][col++] = angebot.getFlrvertreter()
								.getC_kurzzeichen();
					} else {
						rows[row][col++] = null;
					}
				}
				String sStatus = angebot.getAngebotstatus_c_nr();
				rows[row][col++] = getStatusMitUebersetzung(sStatus,
						angebot.getT_versandzeitpunkt(),
						angebot.getC_versandtype());

				BigDecimal nGesamtwertAngebotInAngebotswaehrung = new BigDecimal(
						0);

				if (angebot.getN_gesamtangebotswertinangebotswaehrung() != null
						&& !angebot.getAngebotstatus_c_nr().equals(
								AngebotServiceFac.ANGEBOTSTATUS_STORNIERT)) {
					nGesamtwertAngebotInAngebotswaehrung = angebot
							.getN_gesamtangebotswertinangebotswaehrung();
				}

				if (bDarfPreiseSehen) {
					rows[row][col++] = nGesamtwertAngebotInAngebotswaehrung;
				} else {
					rows[row][col++] = null;
				}

				rows[row++][col++] = angebot
						.getWaehrung_c_nr_angebotswaehrung();

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
							if (!istBelegnummernInJahr("FLRAngebot", sValue)) {
								sValue = super.buildWhereBelegnummer(
										filterKriterien[i], true);
							}
							where.append(" " + FLR_ANGEBOT
									+ filterKriterien[i].kritName);
							where.append(" " + filterKriterien[i].operator);
							where.append(" " + sValue);
						} catch (Throwable ex) {
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR,
									new Exception(ex));
						}
					} else if (filterKriterien[i].kritName.equals("c_bez")) {

						where.append(" ( upper(" + FLR_ANGEBOT + "c_bez) "
								+ filterKriterien[i].operator + " "
								+ filterKriterien[i].value.toUpperCase()
								+ " OR upper(" + FLR_ANGEBOT
								+ "c_kundenanfrage) "
								+ filterKriterien[i].operator + " "
								+ filterKriterien[i].value.toUpperCase()
								+ ") ");
					} else if (filterKriterien[i].kritName
							.equals(AngebotFac.FLR_ANGEBOT_FLRKUNDE
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
								where.append(" ( upper(" + FLR_ANGEBOT
										+ filterKriterien[i].kritName + ")");
								where.append(" " + filterKriterien[i].operator);
								where.append(" "
										+ filterKriterien[i].value
												.toUpperCase());
								where.append(" OR upper(" + FLR_ANGEBOT
										+ "flrkunde.flrpartner.c_kbez" + ") ");
								where.append(" " + filterKriterien[i].operator);
								where.append(" "
										+ filterKriterien[i].value
												.toUpperCase() + ") ");
							} else {
								where.append(" " + FLR_ANGEBOT
										+ filterKriterien[i].kritName);
								where.append(" " + filterKriterien[i].operator);
								where.append(" " + filterKriterien[i].value);
								where.append("OR " + FLR_ANGEBOT
										+ "flrkunde.flrpartner.c_kbez");
								where.append(" " + filterKriterien[i].operator);
								where.append(" " + filterKriterien[i].value);
							}
						} else {
							if (filterKriterien[i].isBIgnoreCase()) {
								where.append(" upper(" + FLR_ANGEBOT
										+ filterKriterien[i].kritName + ")");
							} else {
								where.append(" " + FLR_ANGEBOT
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
					} else if (filterKriterien[i].kritName.equals("c_suche")) {

						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" lower(" + FLR_ANGEBOT
									+ "flrangebottextsuche."
									+ filterKriterien[i].kritName + ")");
						} else {
							where.append(" " + FLR_ANGEBOT
									+ "flrangebottextsuche."
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

					else {
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" lower(" + FLR_ANGEBOT
									+ filterKriterien[i].kritName + ")");
						} else {
							where.append(" " + FLR_ANGEBOT
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
							orderBy.append(FLR_ANGEBOT + kriterien[i].kritName);
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
				orderBy.append(FLR_ANGEBOT).append("c_nr DESC ");
				sortAdded = true;
			}
			if (orderBy.indexOf(FLR_ANGEBOT + "i_id") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" ").append(FLR_ANGEBOT).append("i_id")
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
		// return FLR_ANGEBOT_FROM_CLAUSE;
		return "from FLRAngebot as flrangebot "
				+ " left join flrangebot.flrkunde.flrpartner.flrlandplzort as flrlandplzort "
				+ " left join flrangebot.flrkunde.flrpartner.flrlandplzort.flrort as flrort "
				+ " left join flrangebot.flrkunde.flrpartner.flrlandplzort.flrland as flrland ";
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
		QueryResult result = null;

		try {
			int rowNumber = 0;

			getQuery().setSortKrit(sortierKriterien);

			if (selectedId != null && ((Integer) selectedId).intValue() >= 0) {
				SessionFactory factory = FLRSessionFactory.getFactory();
				Session session = null;

				try {
					session = factory.openSession();
					String queryString = "select " + FLR_ANGEBOT + "i_id"
							+ FLR_ANGEBOT_FROM_CLAUSE + this.buildWhereClause()
							+ this.buildOrderByClause();
					Query query = session.createQuery(queryString);
					ScrollableResults scrollableResult = query.scroll();
					if (scrollableResult != null) {
						scrollableResult.beforeFirst();
						while (scrollableResult.next()) {
							Integer id = (Integer) scrollableResult
									.getInteger(0);
							if (selectedId.equals(id)) {
								rowNumber = scrollableResult.getRowNumber();
								break;
							}
						}
					}
				} finally {
					try {
						session.close();
					} catch (HibernateException he) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_HIBERNATE, he);
					}
				}
			}

			if (rowNumber < 0 || rowNumber >= this.getRowCount()) {
				rowNumber = 0;
			}

			result = this.getPageAt(new Integer(rowNumber));
			result.setIndexOfSelectedRow(rowNumber);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return result;
	}

	public TableInfo getTableInfo() {
		if (super.getTableInfo() == null) {
			try {
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

			Locale locUI = theClientDto.getLocUi();
			setTableInfo(new TableInfo(
					new Class[] { Integer.class, String.class, String.class,
							String.class, String.class, Date.class, Date.class,
							String.class, Icon.class, BigDecimal.class,
							String.class },
					new String[] {
							"i_id",
							getTextRespectUISpr("angb.angebotnummer",
									theClientDto.getMandant(), locUI),
							getTextRespectUISpr("lp.kunde",
									theClientDto.getMandant(), locUI),
							getTextRespectUISpr("lp.ort",
									theClientDto.getMandant(), locUI),
							getTextRespectUISpr("lp.projekt",
									theClientDto.getMandant(), locUI),
							getTextRespectUISpr("lp.datum",
									theClientDto.getMandant(), locUI),
							getTextRespectUISpr("lp.nachfasstermin",
									theClientDto.getMandant(), locUI),
							getTextRespectUISpr("lp.vertreter",
									theClientDto.getMandant(), locUI),
							getTextRespectUISpr("lp.status",
									theClientDto.getMandant(), locUI),
							getTextRespectUISpr("lp.wert",
									theClientDto.getMandant(), locUI), "" },
					new int[] {
							QueryParameters.FLR_BREITE_SHARE_WITH_REST, // diese
							// Spalte
							// wird
							// ausgeblendet
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_XS,
							QueryParameters.FLR_BREITE_XS,
							QueryParameters.FLR_BREITE_PREIS,
							QueryParameters.FLR_BREITE_WAEHRUNG },
					new String[] {
							"i_id",
							"c_nr",
							AngebotFac.FLR_ANGEBOT_FLRKUNDE
									+ "."
									+ KundeFac.FLR_PARTNER
									+ "."
									+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
							// Sortierung fuers erste mal nach LKZ
							AngebotFac.FLR_ANGEBOT_FLRKUNDE + "."
									+ KundeFac.FLR_PARTNER + "."
									+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT
									+ "."
									+ SystemFac.FLR_LP_FLRLAND
									+ "."
									+ SystemFac.FLR_LP_LANDLKZ
									+ ", "
									+
									// und dann nach plz
									AngebotHandler.FLR_ANGEBOT
									+ AngebotFac.FLR_ANGEBOT_FLRKUNDE + "."
									+ KundeFac.FLR_PARTNER + "."
									+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT
									+ "." + SystemFac.FLR_LP_LANDPLZORTPLZ,
							"c_bez",
							AngebotFac.FLR_ANGEBOT_T_BELEGDATUM,
							AngebotFac.FLR_ANGEBOT_T_NACHFASSTERMIN,
							AngebotFac.FLR_ANGEBOT_VERTRETER_I_ID,
							AngebotFac.FLR_ANGEBOT_ANGEBOTSTATUS_C_NR,
							AngebotFac.FLR_ANGEBOT_N_GESAMTANGEBOTSWERTINANGEBOTSWAEHRUNG,
							AngebotFac.FLR_ANGEBOT_WAEHRUNG_C_NR_ANGEBOTSWAEHRUNG }));
		}

		return super.getTableInfo();
	}

	private static String buildReportWhereClause(
			FilterKriterium[] aFilterKriteriumI) {
		StringBuffer where = new StringBuffer("");

		if (aFilterKriteriumI != null && aFilterKriteriumI.length > 0) {
			for (int i = 0; i < aFilterKriteriumI.length; i++) {
				if (aFilterKriteriumI[i].value != null) {
					where.append(FLR_ANGEBOT + aFilterKriteriumI[i].kritName)
							.append(aFilterKriteriumI[i].operator)
							.append(aFilterKriteriumI[i].value).append(" AND ");
				}
			}
		}

		String whereString = "";

		if (where.length() > 0) {
			where.insert(0, " WHERE ");
			whereString = where.substring(0, where.length() - 5); // dads letzte
			// " AND "
			// abschneiden
		}

		return whereString;
	}

	private static String buildReportOrderByClause(
			SortierKriterium[] aSortierKriteriumI) {
		StringBuffer orderby = new StringBuffer("");

		if (aSortierKriteriumI != null && aSortierKriteriumI.length > 0) {
			for (int i = 0; i < aSortierKriteriumI.length; i++) {
				if (aSortierKriteriumI[i].value.equals("true")) {
					orderby.append(FLR_ANGEBOT + aSortierKriteriumI[i].kritName
							+ " ,");
				}
			}
		}

		String orderbyString = "";

		if (orderby.length() > 0) {
			orderby.insert(0, " ORDER BY ");
			orderbyString = orderby.substring(0, orderby.length() - 2); // das
			// letzte
			// " ,"
			// abschneiden
		}

		return orderbyString;
	}

	public PrintInfoDto getSDocPathAndPartner(Object key) {
		AngebotDto angebotDto = null;
		KundeDto kundeDto = null;
		PartnerDto partnerDto = null;
		try {
			angebotDto = getAngebotFac().angebotFindByPrimaryKey((Integer) key,
					theClientDto);
			kundeDto = getKundeFac().kundeFindByPrimaryKey(
					angebotDto.getKundeIIdAngebotsadresse(), theClientDto);
			partnerDto = getPartnerFac().partnerFindByPrimaryKey(
					kundeDto.getPartnerIId(), theClientDto);
		} catch (Exception e) {
			// Nicht gefunden
		}
		if (angebotDto != null) {
//			String sPath = JCRDocFac.HELIUMV_NODE + "/"
//					+ theClientDto.getMandant() + "/"
//					+ LocaleFac.BELEGART_ANGEBOT.trim() + "/"
//					+ LocaleFac.BELEGART_ANGEBOT.trim() + "/"
//					+ angebotDto.getCNr().replace("/", ".");
			DocPath docPath = new DocPath(new DocNodeAngebot(angebotDto));
			Integer iPartnerIId = null;
			if (partnerDto != null) {
				iPartnerIId = partnerDto.getIId();
			}
			return new PrintInfoDto(docPath, iPartnerIId, getSTable());
		} else {
			return null;
		}
	}

	public String getSTable() {
		return "ANGEBOT";
	}
}
