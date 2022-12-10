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
package com.lp.server.angebot.fastlanereader;

import java.awt.Color;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.Icon;

import jnr.ffi.Struct.id_t;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.angebot.fastlanereader.generated.FLRAngebot;
import com.lp.server.angebot.fastlanereader.generated.FLRAngebottextsuche;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebot.service.AngebotFac;
import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.auftrag.fastlanereader.AuftragHandler;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.system.fastlanereader.generated.FLRLandplzort;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeAngebot;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.Facade;
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
import com.lp.util.Helper;

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
	boolean bProjekttitelInAG_AB = false;
	boolean bZweiterVertreter = false;

	private class AngebotKundeAnsprechpartnerFilterBuilder extends FlrFirmaAnsprechpartnerFilterBuilder {

		public AngebotKundeAnsprechpartnerFilterBuilder(boolean bSuchenInklusiveKBez) {
			super(bSuchenInklusiveKBez);
		}

		@Override
		public String getFlrPartner() {
			return FLR_ANGEBOT + AngebotFac.FLR_ANGEBOT_FLRKUNDE + "." + LieferantFac.FLR_PARTNER;
		}

		@Override
		public String getFlrPropertyAnsprechpartnerIId() {
			return FLR_ANGEBOT + "ansprechpartner_i_id_kunde";
		}
	}

	/**
	 * gets the data page for the specified row using the current query. The row at
	 * rowIndex will be located in the middle of the page.
	 * 
	 * @param rowIndex diese Zeile soll selektiert sein
	 * @return QueryResult das Ergebnis der Abfrage
	 * @throws EJBExceptionLP Ausnahme
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
			String queryString = this.getFromClause() + this.buildWhereClause() + this.buildOrderByClause();
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
			final boolean bDarfPreiseSehen = getTheJudgeFac().hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF,
					theClientDto);

			while (resultListIterator.hasNext()) {
				FLRAngebot angebot = (FLRAngebot) ((Object[]) resultListIterator.next())[0];

				Object[] rowToAddCandidate = new Object[colCount];

				rowToAddCandidate[getTableColumnInformation().getViewIndex("i_id")] = angebot.getI_id();
				rowToAddCandidate[getTableColumnInformation().getViewIndex("angb.angebotnummer")] = angebot.getC_nr();
				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.kunde")] = angebot.getFlrkunde() == null
						? null
						: angebot.getFlrkunde().getFlrpartner().getC_name1nachnamefirmazeile1();

				// IMS 1757 die Anschrift des Kunden anzeigen
				String cAnschrift = null;

				if (angebot.getFlrkunde() != null) {
					FLRLandplzort flranschrift = angebot.getFlrkunde().getFlrpartner().getFlrlandplzort();

					if (flranschrift != null) {
						cAnschrift = flranschrift.getFlrland().getC_lkz() + "-" + flranschrift.getC_plz() + " "
								+ flranschrift.getFlrort().getC_name();
					}
				}

				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.ort")] = cAnschrift;

				String proj_anfragenummer = "";

				if (bProjekttitelInAG_AB) {

					if (angebot.getFlrprojekt() != null) {
						proj_anfragenummer = angebot.getFlrprojekt().getC_titel() + " | ";
					}

				}

				if (angebot.getC_bez() != null) {
					proj_anfragenummer += angebot.getC_bez();
				}

				if (angebot.getC_kundenanfrage() != null) {
					proj_anfragenummer += " | " + angebot.getC_kundenanfrage();
				}

				rowToAddCandidate[getTableColumnInformation()
						.getViewIndex("angb.projektanfragenummer")] = proj_anfragenummer;
				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.datum")] = angebot.getT_belegdatum();
				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.nachfasstermin")] = angebot
						.getT_nachfasstermin();

				if (iAnlegerStattVertreterAnzeigen == 1) {
					if (angebot.getFlrpersonalanleger() != null) {
						rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.vertreter")] = angebot
								.getFlrpersonalanleger().getC_kurzzeichen();
					}
				} else if (iAnlegerStattVertreterAnzeigen == 2) {
					if (angebot.getFlrpersonalaenderer() != null) {
						rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.vertreter")] = angebot
								.getFlrpersonalaenderer().getC_kurzzeichen();
					}
				} else {
					if (angebot.getFlrvertreter() != null) {
						rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.vertreter")] = angebot
								.getFlrvertreter().getC_kurzzeichen();
					}
				}

				if (bZweiterVertreter && angebot.getFlrvertreter2()!=null) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.vertreter2")] = angebot.getFlrvertreter2().getC_kurzzeichen();
				}

				String sStatus = angebot.getAngebotstatus_c_nr();
				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.status")] = getStatusMitUebersetzung(
						sStatus, angebot.getT_versandzeitpunkt(), angebot.getC_versandtype());

				if (angebot.getFlrakquisestatus() != null) {

					String zusatzstatus = Helper.fitString2Length(angebot.getFlrakquisestatus().getC_bez(), 15, ' ');

					rowToAddCandidate[getTableColumnInformation().getViewIndex("angb.akquisestatus")] = zusatzstatus;

				}

				BigDecimal nGesamtwertAngebotInAngebotswaehrung = new BigDecimal(0);

				if (angebot.getN_gesamtangebotswertinangebotswaehrung() != null
						&& !angebot.getAngebotstatus_c_nr().equals(AngebotServiceFac.ANGEBOTSTATUS_STORNIERT)) {
					nGesamtwertAngebotInAngebotswaehrung = angebot.getN_gesamtangebotswertinangebotswaehrung();
				}

				if (bDarfPreiseSehen) {
					rowToAddCandidate[getTableColumnInformation()
							.getViewIndex("lp.wert")] = nGesamtwertAngebotInAngebotswaehrung;
				}

				rowToAddCandidate[getTableColumnInformation().getViewIndex("waehrung")] = angebot
						.getWaehrung_c_nr_angebotswaehrung();

				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.kommentar")] = getKommentarart(
						angebot.getX_internerkommentar(), angebot.getX_externerkommentar());

				rows[row] = rowToAddCandidate;
				
				row++;

				col = 0;
			}
			result = new QueryResult(rows, this.getRowCount(), startIndex, endIndex, 0);
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
			String queryString = "select count(*) " + this.getFromClause() + this.buildWhereClause();
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
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HIBERNATE, he);
				}
			}
		}
		return rowCount;
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

					// wenn nach der c_nr gefilter wird, wird das kriterium
					// veraendert
					if (filterKriterien[i].kritName.equals("c_nr")) {
						try {
							String sValue = super.buildWhereBelegnummer(filterKriterien[i], false,
									ParameterFac.KATEGORIE_ANGEBOT,
									ParameterFac.PARAMETER_ANGEBOT_BELEGNUMMERSTARTWERT);
							// Belegnummernsuche auch in "altem" Jahr, wenn im
							// neuen noch keines vorhanden ist
							if (!istBelegnummernInJahr("FLRAngebot", sValue)) {
								sValue = super.buildWhereBelegnummer(filterKriterien[i], true);
							}
							where.append(" " + FLR_ANGEBOT + filterKriterien[i].kritName);
							where.append(" " + filterKriterien[i].operator);
							where.append(" " + sValue);
						} catch (Throwable ex) {
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, new Exception(ex));
						}
					} else if (filterKriterien[i].kritName.equals("c_bez")) {

						where.append(" (");
						where.append(buildWhereClauseExtendedSearch(Arrays.asList(filterKriterien[i].value.split(" ")),
								FLR_ANGEBOT + filterKriterien[i].kritName, filterKriterien[i].isBIgnoreCase()));
						where.append(" OR ");
						where.append(buildWhereClauseExtendedSearch(Arrays.asList(filterKriterien[i].value.split(" ")),
								FLR_ANGEBOT + "c_kundenanfrage", filterKriterien[i].isBIgnoreCase()));
						// 19915
						if (bProjekttitelInAG_AB) {
							where.append(" OR ");
							where.append(
									buildWhereClauseExtendedSearch(Arrays.asList(filterKriterien[i].value.split(" ")),
											"flrprojekt.c_titel", filterKriterien[i].isBIgnoreCase()));
						}

						where.append(") ");

					} else if (isFilterKunde(filterKriterien[i])) {
						AngebotKundeAnsprechpartnerFilterBuilder filterBuilder = new AngebotKundeAnsprechpartnerFilterBuilder(
								getParameterFac().getSuchenInklusiveKBez(theClientDto.getMandant()));
						filterBuilder.buildFirmaAnsprechpartnerFilter(filterKriterien[i], where);
						// buildFirmaFilterOld(filterKriterien[i], where);
					} else if (filterKriterien[i].kritName.equals("c_suche")) {

						where.append(buildWhereClauseExtendedSearchWithoutDuplicates(
								FLRAngebottextsuche.class.getSimpleName(), FLR_ANGEBOT, filterKriterien[i]));

					} else {
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" lower(" + FLR_ANGEBOT + filterKriterien[i].kritName + ")");
						} else {
							where.append(" " + FLR_ANGEBOT + filterKriterien[i].kritName);
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

	private boolean isFilterKunde(FilterKriterium filterKriterium) {
		return filterKriterium.kritName.equals(AngebotFac.FLR_ANGEBOT_FLRKUNDE + "." + LieferantFac.FLR_PARTNER + "."
				+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1);
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
				orderBy.append(" ").append(FLR_ANGEBOT).append("i_id").append(" DESC ");
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
		String s = "from FLRAngebot as flrangebot "
				+ " left join flrangebot.flrkunde.flrpartner.flrlandplzort as flrlandplzort "
				+ " left join flrangebot.flrkunde.flrpartner.flrlandplzort.flrort as flrort "
				+ " left join flrangebot.flrkunde.flrpartner.flrlandplzort.flrland as flrland "
				+ " left join flrangebot.flrprojekt as flrprojekt "
				+ " left join flrangebot.flrvertreter2 as flrvertreter2 "
				+ " left join flrangebot.flrakquisestatus as flrakquisestatus ";

		if (iAnlegerStattVertreterAnzeigen == 1) {
			s += " left join flrangebot.flrpersonalanleger as vertreter ";
		} else if (iAnlegerStattVertreterAnzeigen == 2) {
			s += " left join flrangebot.flrpersonalaenderer as vertreter ";
		} else {
			s += " left join flrangebot.flrvertreter as vertreter ";
		}

		return s;

	}

	/**
	 * sorts the data described by the current query using the specified sort
	 * criterias. The current query is also updated with the new sort criterias.
	 * 
	 * @param sortierKriterien nach diesen Kriterien wird das Ergebnis sortiert
	 * @param selectedId       auf diesem Datensatz soll der Cursor stehen
	 * @return QueryResult das Ergebnis der Abfrage
	 * @throws EJBExceptionLP Ausnahme
	 * @see UseCaseHandler#sort(SortierKriterium[], Object)
	 */
	public QueryResult sort(SortierKriterium[] sortierKriterien, Object selectedId) throws EJBExceptionLP {
		QueryResult result = null;

		try {
			int rowNumber = 0;

			getQuery().setSortKrit(sortierKriterien);

			if (selectedId != null && ((Integer) selectedId).intValue() >= 0) {
				SessionFactory factory = FLRSessionFactory.getFactory();
				Session session = null;

				try {
					session = factory.openSession();
					String queryString = "select " + FLR_ANGEBOT + "i_id" + FLR_ANGEBOT_FROM_CLAUSE
							+ this.buildWhereClause() + this.buildOrderByClause();
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
				} finally {
					try {
						session.close();
					} catch (HibernateException he) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HIBERNATE, he);
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
		TableInfo info = super.getTableInfo();
		if (info != null)
			return info;

		setTableColumnInformation(createColumnInformation(theClientDto.getMandant(), theClientDto.getLocUi()));

		TableColumnInformation c = getTableColumnInformation();
		info = new TableInfo(c.getClasses(), c.getHeaderNames(), c.getWidths(), c.getDbColumNames(),
				c.getHeaderToolTips());
		setTableInfo(info);
		return info;
	}

	private TableColumnInformation createColumnInformation(String mandant, Locale locUi) {

		String orderVertreter = "flrvertreter.c_kurzzeichen";

		try {
			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_ANZEIGE_ANLEGER_STATT_VERTRETER);
			iAnlegerStattVertreterAnzeigen = (Integer) parameter.getCWertAsObject();

			if (iAnlegerStattVertreterAnzeigen == 1) {
				orderVertreter = "flrpersonalanleger.c_kurzzeichen";
			} else if (iAnlegerStattVertreterAnzeigen == 2) {
				orderVertreter = "flrpersonalaenderer.c_kurzzeichen";

			}

			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_PROJEKT_TITEL_IN_AG_AB_PROJEKT);
			bProjekttitelInAG_AB = (Boolean) parameter.getCWertAsObject();

			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(), ParameterFac.KATEGORIE_ANGEBOT,
					ParameterFac.PARAMETER_ZWEITER_VERTRETER);
			bZweiterVertreter = (Boolean) parameter.getCWertAsObject();

		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		TableColumnInformation columns = new TableColumnInformation();

		columns.add("i_id", Integer.class, "i_id", QueryParameters.FLR_BREITE_SHARE_WITH_REST,
				AuftragFac.FLR_AUFTRAG_I_ID);
		columns.add("angb.angebotnummer", String.class, getTextRespectUISpr("angb.angebotnummer", mandant, locUi),
				QueryParameters.FLR_BREITE_M, "c_nr");
		columns.add("lp.kunde", String.class, getTextRespectUISpr("lp.kunde", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, AngebotFac.FLR_ANGEBOT_FLRKUNDE + "." + KundeFac.FLR_PARTNER
						+ "." + PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1);
		columns.add("lp.ort", String.class, getTextRespectUISpr("lp.ort", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST,
				AngebotFac.FLR_ANGEBOT_FLRKUNDE + "." + KundeFac.FLR_PARTNER + "."
						+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT + "." + SystemFac.FLR_LP_FLRLAND + "."
						+ SystemFac.FLR_LP_LANDLKZ + ", " +
						// und dann nach plz
						AngebotHandler.FLR_ANGEBOT + AngebotFac.FLR_ANGEBOT_FLRKUNDE + "." + KundeFac.FLR_PARTNER + "."
						+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT + "." + SystemFac.FLR_LP_LANDPLZORTPLZ);
		columns.add("angb.projektanfragenummer", String.class,
				getTextRespectUISpr("angb.projektanfragenummer", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, "c_bez");
		columns.add("lp.datum", Date.class, getTextRespectUISpr("lp.datum", mandant, locUi),
				QueryParameters.FLR_BREITE_M, AngebotFac.FLR_ANGEBOT_T_BELEGDATUM);
		columns.add("lp.nachfasstermin", Date.class, getTextRespectUISpr("lp.nachfasstermin", mandant, locUi),
				QueryParameters.FLR_BREITE_M, AngebotFac.FLR_ANGEBOT_T_NACHFASSTERMIN);
		columns.add("lp.vertreter", String.class, getTextRespectUISpr("lp.vertreter", mandant, locUi),
				QueryParameters.FLR_BREITE_XS, orderVertreter);

		if (bZweiterVertreter) {
			columns.add("lp.vertreter2", String.class, getTextRespectUISpr("lp.vertreter", mandant, locUi) + " 2",
					QueryParameters.FLR_BREITE_XS, "flrvertreter2.c_kurzzeichen");
		}

		columns.add("lp.status", Icon.class, getTextRespectUISpr("lp.status", mandant, locUi),
				QueryParameters.FLR_BREITE_XS, AngebotFac.FLR_ANGEBOT_ANGEBOTSTATUS_C_NR);
		columns.add("angb.akquisestatus", Icon.class, getTextRespectUISpr("angb.akquisestatus", mandant, locUi),
				QueryParameters.FLR_BREITE_XS, "flrakquisestatus.c_bez");
		columns.add("lp.wert", BigDecimal.class, getTextRespectUISpr("lp.wert", mandant, locUi),
				QueryParameters.FLR_BREITE_PREIS, AngebotFac.FLR_ANGEBOT_N_GESAMTANGEBOTSWERTINANGEBOTSWAEHRUNG);
		columns.add("waehrung", String.class, "", QueryParameters.FLR_BREITE_WAEHRUNG,
				AngebotFac.FLR_ANGEBOT_WAEHRUNG_C_NR_ANGEBOTSWAEHRUNG);
		columns.add("lp.kommentar", String.class, getTextRespectUISpr("lp.kommentar", mandant, locUi),
				QueryParameters.FLR_BREITE_XXS, Facade.NICHT_SORTIERBAR);

		return columns;
	}

	private static String buildReportWhereClause(FilterKriterium[] aFilterKriteriumI) {
		StringBuffer where = new StringBuffer("");

		if (aFilterKriteriumI != null && aFilterKriteriumI.length > 0) {
			for (int i = 0; i < aFilterKriteriumI.length; i++) {
				if (aFilterKriteriumI[i].value != null) {
					where.append(FLR_ANGEBOT + aFilterKriteriumI[i].kritName).append(aFilterKriteriumI[i].operator)
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

	private static String buildReportOrderByClause(SortierKriterium[] aSortierKriteriumI) {
		StringBuffer orderby = new StringBuffer("");

		if (aSortierKriteriumI != null && aSortierKriteriumI.length > 0) {
			for (int i = 0; i < aSortierKriteriumI.length; i++) {
				if (aSortierKriteriumI[i].value.equals("true")) {
					orderby.append(FLR_ANGEBOT + aSortierKriteriumI[i].kritName + " ,");
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
			angebotDto = getAngebotFac().angebotFindByPrimaryKey((Integer) key, theClientDto);
			kundeDto = getKundeFac().kundeFindByPrimaryKey(angebotDto.getKundeIIdAngebotsadresse(), theClientDto);
			partnerDto = getPartnerFac().partnerFindByPrimaryKey(kundeDto.getPartnerIId(), theClientDto);
		} catch (Exception e) {
			// Nicht gefunden
		}
		if (angebotDto != null) {
			// String sPath = JCRDocFac.HELIUMV_NODE + "/"
			// + theClientDto.getMandant() + "/"
			// + LocaleFac.BELEGART_ANGEBOT.trim() + "/"
			// + LocaleFac.BELEGART_ANGEBOT.trim() + "/"
			// + angebotDto.getCNr().replace("/", ".");
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
