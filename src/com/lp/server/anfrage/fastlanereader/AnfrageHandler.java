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
package com.lp.server.anfrage.fastlanereader;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.Icon;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.anfrage.fastlanereader.generated.FLRAnfrage;
import com.lp.server.anfrage.service.AnfrageDto;
import com.lp.server.anfrage.service.AnfrageFac;
import com.lp.server.anfrage.service.AnfrageServiceFac;
import com.lp.server.partner.fastlanereader.generated.FLRLiefergruppespr;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.system.fastlanereader.generated.FLRLandplzort;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeAnfrage;
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
 * FLR fuer ANF_ANFRAGE.
 * </p>
 * 
 * <p>
 * Copright Logistik Pur GmbH (c) 2005
 * </p>
 * 
 * <p>
 * Erstellungsdatum 07.06.05
 * </p>
 * <p>
 * </p>
 * 
 * @author Uli Walch
 * @version 1.0
 */

public class AnfrageHandler extends UseCaseHandler {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String FLR_ANFRAGE = "flranfrage.";
	public static final String FLR_ANFRAGE_FROM_CLAUSE = " from FLRAnfrage flranfrage ";

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
			int pageSize = AnfrageHandler.PAGE_SIZE;
			int startIndex = Math.max(rowIndex.intValue() - (pageSize / 2), 0);
			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			String queryString = this.getFromClause() + this.buildWhereClause()
					+ this.buildOrderByClause();
			// myLogger.info("HQL: " + queryString);
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();

			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;
			int col = 0;

			while (resultListIterator.hasNext()) {
				// FLRAnfrage anfrage = (FLRAnfrage) resultListIterator.next();
				FLRAnfrage anfrage = (FLRAnfrage) ((Object[]) resultListIterator
						.next())[0];

				rows[row][col++] = anfrage.getI_id();

				// Kuerzel fuer die Auftragart
				String anfrageart = null;

				if (anfrage.getAnfrageart_c_nr().equals(
						AnfrageServiceFac.ANFRAGEART_LIEFERGRUPPE)) {
					anfrageart = AnfrageServiceFac.ANFRAGEART_LIEFERGRUPPE_SHORT;
				}

				rows[row][col++] = anfrageart;
				rows[row][col++] = anfrage.getC_nr();

				if (anfrage.getAnfrageart_c_nr().equals(
						AnfrageServiceFac.ANFRAGEART_LIEFERGRUPPE)) {
					if (anfrage.getFlrliefergruppe() != null) {
						String sUebersetzung = anfrage.getFlrliefergruppe()
								.getC_nr();
						Iterator iterUebersetzungenI = anfrage
								.getFlrliefergruppe()
								.getLiefergruppe_liefergruppespr_set()
								.iterator();

						while (iterUebersetzungenI.hasNext()) {
							FLRLiefergruppespr bestellungartspr = (FLRLiefergruppespr) iterUebersetzungenI
									.next();
							if (bestellungartspr.getLocale().getC_nr()
									.compareTo(theClientDto.getLocUiAsString()) == 0) {
								if (bestellungartspr.getC_bez() != null
										&& bestellungartspr.getC_bez().length() > 0) {
									sUebersetzung = bestellungartspr.getC_bez();
									break;
								}
							}
						}

						rows[row][col++] = sUebersetzung;
					} else {
						rows[row][col++] = null;
					}

				} else {

					rows[row][col++] = anfrage.getFlrlieferant() == null ? null
							: anfrage.getFlrlieferant().getFlrpartner()
									.getC_name1nachnamefirmazeile1();
				}

				// IMS 1757 die Anschrift des Lieferanten anzeigen im Format
				// A-5020 Salzburg, Bahnhofstrasse 1
				String cAnschrift = null; // eine Liefergruppenanfrage hat
				// keinen Lieferanten

				if (anfrage.getFlrlieferant() != null) {
					FLRLandplzort flranschrift = anfrage.getFlrlieferant()
							.getFlrpartner().getFlrlandplzort();

					if (flranschrift != null) {
						cAnschrift = flranschrift.getFlrland().getC_lkz() + "-"
								+ flranschrift.getC_plz() + " "
								+ flranschrift.getFlrort().getC_name();
					}
				}
				rows[row][col++] = cAnschrift;

				rows[row][col++] = anfrage.getC_bez();
				rows[row][col++] = anfrage.getT_belegdatum();
				String sStatus = anfrage.getAnfragestatus_c_nr();
				rows[row][col++] = getStatusMitUebersetzung(sStatus,
						anfrage.getT_versandzeitpunkt(),
						anfrage.getC_versandtype());

				BigDecimal nGesamtwertAnfrageInAnfragewaehrung = new BigDecimal(
						0);

				if (anfrage.getN_gesamtanfragewertinanfragewaehrung() != null
						&& !anfrage.getAnfragestatus_c_nr().equals(
								AnfrageServiceFac.ANFRAGESTATUS_STORNIERT)) {
					nGesamtwertAnfrageInAnfragewaehrung = anfrage
							.getN_gesamtanfragewertinanfragewaehrung();
				}

				rows[row][col++] = nGesamtwertAnfrageInAnfragewaehrung;
				rows[row++][col++] = anfrage.getWaehrung_c_nr_anfragewaehrung();

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
	 * @return int die Anzahl der Zeilen im Ergebnis
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
							if (!istBelegnummernInJahr("FLRAnfrage", sValue)) {
								sValue = super.buildWhereBelegnummer(
										filterKriterien[i], true);
							}
							where.append(" " + FLR_ANFRAGE
									+ filterKriterien[i].kritName);
							where.append(" " + filterKriterien[i].operator);
							where.append(" " + sValue);
						} catch (Throwable ex) {
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR,
									new Exception(ex));
						}
					} else if (filterKriterien[i].kritName
							.equals(AnfrageFac.FLR_ANFRAGE_FLRLIEFERANT
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
								where.append(" upper(" + FLR_ANFRAGE
										+ filterKriterien[i].kritName + ")");
								where.append(" " + filterKriterien[i].operator);
								where.append(" "
										+ filterKriterien[i].value
												.toUpperCase());
								where.append("OR upper(" + FLR_ANFRAGE
										+ "flrlieferant.flrpartner.c_kbez"
										+ ")");
								where.append(" " + filterKriterien[i].operator);
								where.append(" "
										+ filterKriterien[i].value
												.toUpperCase());
							} else {
								where.append(" " + FLR_ANFRAGE
										+ filterKriterien[i].kritName);
								where.append(" " + filterKriterien[i].operator);
								where.append(" " + filterKriterien[i].value);
								where.append("OR " + FLR_ANFRAGE
										+ "flrlieferant.flrpartner.c_kbez");
								where.append(" " + filterKriterien[i].operator);
								where.append(" " + filterKriterien[i].value);
							}
						} else {
							if (filterKriterien[i].isBIgnoreCase()) {
								where.append(" upper(" + FLR_ANFRAGE
										+ filterKriterien[i].kritName + ")");
							} else {
								where.append(" " + FLR_ANFRAGE
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
							where.append(" upper(" + FLR_ANFRAGE
									+ filterKriterien[i].kritName + ")");
						} else {
							where.append(" " + FLR_ANFRAGE
									+ filterKriterien[i].kritName);
						}

						where.append(" " + filterKriterien[i].operator);

						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" "
									+ filterKriterien[i].value.toUpperCase());
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
							orderBy.append(FLR_ANFRAGE + kriterien[i].kritName);
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
				orderBy.append(FLR_ANFRAGE).append("c_nr DESC ");
				sortAdded = true;
			}
			if (orderBy.indexOf(FLR_ANFRAGE + "i_id") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" ").append(FLR_ANFRAGE).append("i_id")
						.append(" ");
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
		// return FLR_ANFRAGE_FROM_CLAUSE;
		return "from FLRAnfrage as flranfrage "
				+ " left join flranfrage.flrlieferant.flrpartner.flrlandplzort as flrlandplzort "
				+ " left join flranfrage.flrlieferant.flrpartner.flrlandplzort.flrort as flrort "
				+ " left join flranfrage.flrlieferant.flrpartner.flrlandplzort.flrland as flrland ";
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
					String queryString = "select " + FLR_ANFRAGE + "i_id"
							+ FLR_ANFRAGE_FROM_CLAUSE + this.buildWhereClause()
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
			setTableInfo(new TableInfo(
					new Class[] { Integer.class, String.class, String.class,
							String.class, String.class, String.class,
							Date.class, Icon.class, BigDecimal.class,
							String.class },
					new String[] {
							"i_id",
							"",
							getTextRespectUISpr("anf.anfragenummer",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.lieferant",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.ort",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.projekt",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.datum",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.status",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.wert",
									theClientDto.getMandant(),
									theClientDto.getLocUi()), "" },
					new int[] {
							QueryParameters.FLR_BREITE_SHARE_WITH_REST, // diese
							// Spalte
							// wird
							// ausgeblendet
							QueryParameters.FLR_BREITE_XXS, // Kuerzel
							// Anfrageart
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_XS,
							QueryParameters.FLR_BREITE_PREIS,
							QueryParameters.FLR_BREITE_WAEHRUNG },
					new String[] {
							"i_id",
							Facade.NICHT_SORTIERBAR, // AnfrageFac.
							// FLR_ANFRAGEART_C_NR
							"c_nr",
							AnfrageFac.FLR_ANFRAGE_FLRLIEFERANT
									+ "."
									+ KundeFac.FLR_PARTNER
									+ "."
									+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
							// Sortierung fuers erste mal nach LKZ
							AnfrageFac.FLR_ANFRAGE_FLRLIEFERANT + "."
									+ KundeFac.FLR_PARTNER + "."
									+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT
									+ "."
									+ SystemFac.FLR_LP_FLRLAND
									+ "."
									+ SystemFac.FLR_LP_LANDLKZ
									+ ", "
									+
									// und dann nach plz
									AnfrageHandler.FLR_ANFRAGE
									+ AnfrageFac.FLR_ANFRAGE_FLRLIEFERANT + "."
									+ KundeFac.FLR_PARTNER + "."
									+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT
									+ "." + SystemFac.FLR_LP_LANDPLZORTPLZ,
							"c_bez",
							AnfrageFac.FLR_ANFRAGE_T_BELEGDATUM,
							AnfrageFac.FLR_ANFRAGE_ANFRAGESTATUS_C_NR,
							AnfrageFac.FLR_ANFRAGE_N_GESAMTANFRAGEWERTINANFRAGEWAEHRUNG,
							AnfrageFac.FLR_ANFRAGE_WAEHRUNG_C_NR_ANFRAGEWAEHRUNG }));
		}

		return super.getTableInfo();
	}

	public PrintInfoDto getSDocPathAndPartner(Object key) {
		AnfrageDto anfrageDto = null;
		LieferantDto lieferantDto = null;
		try {
			anfrageDto = getAnfrageFac().anfrageFindByPrimaryKey((Integer) key,
					theClientDto);
			lieferantDto = getLieferantFac().lieferantFindByPrimaryKeySmall(
					anfrageDto.getLieferantIIdAnfrageadresse());

		} catch (Exception e) {
			// Nicht gefunden
		}
		if (anfrageDto != null) {
//			String sPath = JCRDocFac.HELIUMV_NODE + "/"
//					+ theClientDto.getMandant() + "/"
//					+ LocaleFac.BELEGART_ANFRAGE.trim() + "/"
//					+ LocaleFac.BELEGART_ANFRAGE.trim() + "/"
//					+ anfrageDto.getCNr().replace("/", ".");
			DocPath docPath = new DocPath(new DocNodeAnfrage(anfrageDto));

			Integer lieferantIId = null;
			if (lieferantDto != null) {
				lieferantIId = lieferantDto.getIId();
			}

			return new PrintInfoDto(docPath, lieferantIId, getSTable());
		} else {
			return null;
		}
	}

	public String getSTable() {
		return "ANFRAGE";
	}
}
