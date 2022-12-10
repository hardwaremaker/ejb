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
package com.lp.server.rechnung.fastlanereader;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Arrays;
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

import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnung;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnungtextsuche;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.system.fastlanereader.generated.FLRLandplzort;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeGutschrift;
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

/**
 * <p>
 * Hier wird die FLR Funktionalitaet fuer die Gutschrift implementiert. Pro
 * UseCase gibt es einen Handler.
 * </p>
 * <p>
 * Copright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum 2004-08-14
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version 1.0
 */

public class GutschriftHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Integer iAnlegerStattVertreterAnzeigen = 0;
	boolean bHatFibu = false;
	boolean bBruttoStattNetto = false;
	boolean bAuswahllisteMitDebitorennummer = false;
	private String FLR_GUTSCHRIFT = "gutschrift.";

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = GutschriftHandler.PAGE_SIZE;
			int startIndex = Math.max(rowIndex.intValue() - (pageSize / 2), 0);
			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			String queryString = "SELECT gutschrift, (SELECT count(*) FROM FLRFinanzBelegbuchung as bb WHERE bb.belegart_c_nr='"
					+ LocaleFac.BELEGART_GUTSCHRIFT
					+ "' AND bb.i_belegiid=gutschrift.i_id  ) from FLRRechnung as gutschrift "
					+ " left join gutschrift.flrkunde.flrpartner.flrlandplzort as flrlandplzort "
					+ " left join gutschrift.flrkunde.flrpartner.flrlandplzort.flrort as flrort "
					+ " left join gutschrift.flrkunde.flrpartner.flrlandplzort.flrland as flrland "
					+ " left join gutschrift.flrkunde.flrkonto as flrkonto " + this.buildWhereClause()
					+ this.buildOrderByClause();
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;
			int col = 0;
			final boolean bDarfPreiseSehen = getTheJudgeFac().hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF,
					theClientDto);

			while (resultListIterator.hasNext()) {
				Object[] o = (Object[]) resultListIterator.next();
				FLRRechnung gutschrift = (FLRRechnung) o[0];
				long lVerbucht = (Long) o[1];

				rows[row][col++] = gutschrift.getI_id();
				rows[row][col++] = gutschrift.getFlrrechnungart() == null ? null
						: gutschrift.getFlrrechnungart().getC_nr().substring(0, 1);
				rows[row][col++] = gutschrift.getC_nr();
				rows[row][col++] = gutschrift.getFlrkunde() == null ? null
						: gutschrift.getFlrkunde().getFlrpartner().getC_name1nachnamefirmazeile1();
				FLRLandplzort anschrift = gutschrift.getFlrkunde().getFlrpartner().getFlrlandplzort();
				if (anschrift != null) {
					rows[row][col++] = anschrift.getFlrland().getC_lkz() + "-" + anschrift.getC_plz() + " "
							+ anschrift.getFlrort().getC_name();
				} else {
					rows[row][col++] = "";
				}

				if (bAuswahllisteMitDebitorennummer) {
					if (gutschrift.getFlrkunde().getFlrkonto() != null) {
						rows[row][col++] = gutschrift.getFlrkunde().getFlrkonto().getC_nr();
					} else {
						rows[row][col++] = null;
					}
				}

				String proj_bestellnummer = "";

				if (bTitelInAF_BS_LS_RE_LOS) {
					if (gutschrift.getFlrprojekt() != null) {
						proj_bestellnummer = gutschrift.getFlrprojekt().getC_titel() + " | ";
					}
				}

				if (gutschrift.getC_bez() != null) {
					proj_bestellnummer += gutschrift.getC_bez();
				}

				if (gutschrift.getC_bestellnummer() != null) {
					proj_bestellnummer += " | " + gutschrift.getC_bestellnummer();
				}

				rows[row][col++] = proj_bestellnummer;

				rows[row][col++] = gutschrift.getD_belegdatum();

				if (iAnlegerStattVertreterAnzeigen == 1) {
					if (gutschrift.getFlrpersonalanleger() != null) {
						rows[row][col++] = gutschrift.getFlrpersonalanleger().getC_kurzzeichen();
					} else {
						rows[row][col++] = null;
					}

				} else if (iAnlegerStattVertreterAnzeigen == 2) {
					if (gutschrift.getFlrpersonalaenderer() != null) {
						rows[row][col++] = gutschrift.getFlrpersonalaenderer().getC_kurzzeichen();
					} else {
						rows[row][col++] = null;
					}
				} else {
					if (gutschrift.getFlrvertreter() != null) {
						rows[row][col++] = gutschrift.getFlrvertreter().getC_kurzzeichen();
					} else {
						rows[row][col++] = null;
					}
				}

				String sStatus = gutschrift.getStatus_c_nr();
				rows[row][col++] = getStatusMitUebersetzung(sStatus, gutschrift.getT_versandzeitpunkt(),
						gutschrift.getC_versandtype());
				if (bDarfPreiseSehen) {
					if (bBruttoStattNetto == false) {
						rows[row][col++] = gutschrift.getN_wertfw();
					} else {

						if (gutschrift.getN_wertfw() != null && gutschrift.getN_wertustfw() != null) {
							rows[row][col++] = gutschrift.getN_wertfw().add(gutschrift.getN_wertustfw());
						} else {
							rows[row][col++] = null;
						}

					}

				} else {
					rows[row][col++] = null;
				}
				rows[row][col++] = gutschrift.getWaehrung_c_nr();
				String reCNr = null;
				if (gutschrift.getRechnung_i_id_zurechnung() != null) {
					RechnungDto reDto = getRechnungFac()
							.rechnungFindByPrimaryKey(gutschrift.getRechnung_i_id_zurechnung());
					reCNr = reDto.getCNr();
				} else {
					reCNr = "";
				}
				rows[row][col++] = reCNr;
				if (bHatFibu == true) {
					if (lVerbucht > 0) {
						rows[row][col++] = new Boolean(true);
					} else {
						rows[row][col++] = new Boolean(false);
					}

				} else {
					rows[row][col++] = new Boolean(gutschrift.getT_fibuuebernahme() != null);
				}
				row++;
				col = 0;
			}
			result = new QueryResult(rows, this.getRowCount(), startIndex, endIndex, 0);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} finally {
			try {
				session.close();
			} catch (HibernateException he) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, he);
			}
		}
		return result;
	}

	/**
	 * gets the total number of rows represented by the current query.
	 * 
	 * @see UseCaseHandler#getRowCountFromDataBase()
	 * @return int
	 */
	protected long getRowCountFromDataBase() {
		long rowCount = 0;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			String queryString = "select count(*) from FLRRechnung as gutschrift "
					+ " left join gutschrift.flrkunde.flrpartner.flrlandplzort as flrlandplzort "
					+ " left join gutschrift.flrkunde.flrpartner.flrlandplzort.flrort as flrort "
					+ " left join gutschrift.flrkunde.flrpartner.flrlandplzort.flrland as flrland "
					+ " left join gutschrift.flrkunde.flrkonto as flrkonto " + this.buildWhereClause();
			Query query = session.createQuery(queryString);
			List<?> rowCountResult = query.list();
			if (rowCountResult != null && rowCountResult.size() > 0) {
				rowCount = ((Long) rowCountResult.get(0)).longValue();
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
					if (filterKriterien[i].kritName.equals(RechnungFac.FLR_RECHNUNG_C_NR)) {
						try {
							String sValue = super.buildWhereBelegnummer(filterKriterien[i], false,
									ParameterFac.KATEGORIE_GUTSCHRIFT,
									ParameterFac.PARAMETER_GUTSCHRIFT_BELEGNUMMERSTARTWERT);
							if (!istBelegnummernInJahr("FLRRechnung", sValue,
									"flrrechnungart.rechnungtyp_c_nr='" + RechnungFac.RECHNUNGTYP_GUTSCHRIFT + "'")) {
								sValue = super.buildWhereBelegnummer(filterKriterien[i], true);
							}
							where.append(" gutschrift." + filterKriterien[i].kritName);
							where.append(" " + filterKriterien[i].operator);
							where.append(" " + sValue);
						} catch (Exception ex) {
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, ex);
						}
					} else if (filterKriterien[i].kritName.equals(RechnungFac.FLR_RECHNUNG_FLRKUNDE + "."
							+ LieferantFac.FLR_PARTNER + "." + PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1)) {
						ParametermandantDto parameter = null;
						try {
							parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
									ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_SUCHEN_INKLUSIVE_KBEZ);
						} catch (RemoteException ex) {
							throwEJBExceptionLPRespectOld(ex);
						}
						Boolean bSuchenInklusiveKbez = (java.lang.Boolean) parameter.getCWertAsObject();
						if (bSuchenInklusiveKbez) {
							if (filterKriterien[i].isBIgnoreCase()) {
								where.append(" (upper(" + FLR_GUTSCHRIFT + filterKriterien[i].kritName + ")");
								where.append(" " + filterKriterien[i].operator);
								where.append(" " + filterKriterien[i].value.toUpperCase());
								where.append("OR upper(" + FLR_GUTSCHRIFT + "flrkunde.flrpartner.c_kbez" + ")");
								where.append(" " + filterKriterien[i].operator);
								where.append(" " + filterKriterien[i].value.toUpperCase() + ") ");
							} else {
								where.append(" (" + FLR_GUTSCHRIFT + filterKriterien[i].kritName);
								where.append(" " + filterKriterien[i].operator);
								where.append(" " + filterKriterien[i].value);
								where.append("OR " + FLR_GUTSCHRIFT + "flrkunde.flrpartner.c_kbez");
								where.append(" " + filterKriterien[i].operator);
								where.append(" " + filterKriterien[i].value + ") ");
							}
						} else {
							if (filterKriterien[i].isBIgnoreCase()) {
								where.append(" upper(" + FLR_GUTSCHRIFT + filterKriterien[i].kritName + ")");
							} else {
								where.append(" " + FLR_GUTSCHRIFT + filterKriterien[i].kritName);
							}

							where.append(" " + filterKriterien[i].operator);

							if (filterKriterien[i].isBIgnoreCase()) {
								where.append(" " + filterKriterien[i].value.toUpperCase());
							} else {
								where.append(" " + filterKriterien[i].value);
							}
						}
					} else if (filterKriterien[i].kritName.equals("c_suche")) {

						where.append(buildWhereClauseExtendedSearchWithoutDuplicates(
								FLRRechnungtextsuche.class.getSimpleName(), FLR_GUTSCHRIFT, filterKriterien[i]));

					} else if (filterKriterien[i].kritName.equals(RechnungFac.FLR_RECHNUNG_C_BEZ)) {

						where.append(" (");
						where.append(buildWhereClauseExtendedSearch(Arrays.asList(filterKriterien[i].value.split(" ")),
								"gutschrift." + filterKriterien[i].kritName, filterKriterien[i].isBIgnoreCase()));
						where.append(" OR ");
						where.append(buildWhereClauseExtendedSearch(Arrays.asList(filterKriterien[i].value.split(" ")),
								"gutschrift." + "c_bestellnummer", filterKriterien[i].isBIgnoreCase()));
						where.append(") ");

					} else {
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" lower(gutschrift." + filterKriterien[i].kritName + ")");
						} else {
							where.append(" gutschrift." + filterKriterien[i].kritName);
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
							orderBy.append("gutschrift." + kriterien[i].kritName);
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
				orderBy.append("gutschrift." + RechnungFac.FLR_RECHNUNG_I_GESCHAEFTSJAHR + " DESC," + "gutschrift."
						+ RechnungFac.FLR_RECHNUNG_C_NR + " DESC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("gutschrift." + RechnungFac.FLR_RECHNUNG_C_NR) < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" gutschrift." + RechnungFac.FLR_RECHNUNG_C_NR + " ");
				sortAdded = true;
			}
			if (sortAdded) {
				orderBy.insert(0, " ORDER BY ");
			}
		}
		return orderBy.toString();
	}

	/**
	 * sorts the data described by the current query using the specified sort
	 * criterias. The current query is also updated with the new sort criterias.
	 * 
	 * @see UseCaseHandler#sort(SortierKriterium[], Object)
	 * @throws EJBExceptionLP
	 * @param sortierKriterien SortierKriterium[]
	 * @param selectedId       Object
	 * @return QueryResult
	 */
	public QueryResult sort(SortierKriterium[] sortierKriterien, Object selectedId) throws EJBExceptionLP {
		this.getQuery().setSortKrit(sortierKriterien);

		QueryResult result = null;
		int rowNumber = 0;

		if (selectedId != null && ((Integer) selectedId).intValue() >= 0) {
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();
				String queryString = "select gutschrift." + RechnungFac.FLR_RECHNUNG_I_ID
						+ " from FLRRechnung gutschrift " + this.buildWhereClause() + this.buildOrderByClause();
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

	/**
	 * gets information about the Gutschriftstable.
	 * 
	 * @return TableInfo
	 */
	public TableInfo getTableInfo() {
		if (super.getTableInfo() == null) {
			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();

			try {
				ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_ANZEIGE_ANLEGER_STATT_VERTRETER);
				iAnlegerStattVertreterAnzeigen = (Integer) parameter.getCWertAsObject();
				bHatFibu = getMandantFac().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_FINANZBUCHHALTUNG,
						theClientDto);

				parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_RECHNUNG, ParameterFac.PARAMETER_BRUTTO_STATT_NETTO_IN_AUSWAHLLISTE);
				bBruttoStattNetto = (Boolean) parameter.getCWertAsObject();

				parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_RECHNUNG, ParameterFac.PARAMETER_DEBITORENNUMMER_IN_AUSWAHLLISTE);
				bAuswahllisteMitDebitorennummer = (Boolean) parameter.getCWertAsObject();

			} catch (RemoteException ex) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
			}

			TableColumnInformation columns = new TableColumnInformation();

			columns.add("i_id", Integer.class, "i_id", -1, RechnungFac.FLR_RECHNUNG_I_ID);

			columns.add("art", String.class, " ", 2,
					RechnungFac.FLR_RECHNUNG_FLRRECHNUNGART + "." + RechnungFac.FLR_RECHNUNGART_C_NR);

			columns.add("lp.gutnr", String.class, getTextRespectUISpr("lp.gutnr", mandantCNr, locUI),
					QueryParameters.FLR_BREITE_M, RechnungFac.FLR_RECHNUNG_C_NR);
			columns.add("lp.kunde", String.class, getTextRespectUISpr("lp.kunde", mandantCNr, locUI),
					QueryParameters.FLR_BREITE_SHARE_WITH_REST,
					RechnungFac.FLR_RECHNUNG_FLRKUNDE + "." + KundeFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1);
			columns.add("lp.ort", String.class, getTextRespectUISpr("lp.ort", mandantCNr, locUI),
					QueryParameters.FLR_BREITE_SHARE_WITH_REST,
					RechnungFac.FLR_RECHNUNG_FLRKUNDE + "." + KundeFac.FLR_PARTNER + "."
							+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT + "." + SystemFac.FLR_LP_FLRLAND + "."
							+ SystemFac.FLR_LP_LANDLKZ + ", " +
							// und dann nach plz
							"gutschrift." + RechnungFac.FLR_RECHNUNG_FLRKUNDE + "." + KundeFac.FLR_PARTNER + "."
							+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT + "." + SystemFac.FLR_LP_LANDPLZORTPLZ);

			if (bAuswahllisteMitDebitorennummer) {
				columns.add("lp.debitoren", String.class, getTextRespectUISpr("lp.debitoren", mandantCNr, locUI),
						QueryParameters.FLR_BREITE_SHARE_WITH_REST,
						RechnungFac.FLR_RECHNUNG_FLRKUNDE + "." + KundeFac.FLR_KONTO + ".c_nr");
			}

			columns.add("re.projektbestellnummer", String.class,
					getTextRespectUISpr("re.projektbestellnummer", mandantCNr, locUI),
					QueryParameters.FLR_BREITE_SHARE_WITH_REST, RechnungFac.FLR_RECHNUNG_C_BEZ);

			columns.add("lp.datum", Date.class, getTextRespectUISpr("lp.datum", mandantCNr, locUI),
					QueryParameters.FLR_BREITE_M, RechnungFac.FLR_RECHNUNG_D_BELEGDATUM);

			columns.add("lp.vertreter", String.class, getTextRespectUISpr("lp.vertreter", mandantCNr, locUI),
					QueryParameters.FLR_BREITE_XS, Facade.NICHT_SORTIERBAR);
			columns.add("lp.status", Icon.class, getTextRespectUISpr("lp.status", mandantCNr, locUI),
					QueryParameters.FLR_BREITE_XS, RechnungFac.FLR_RECHNUNG_STATUS_C_NR);

			columns.add("lp.wert", BigDecimal.class, getTextRespectUISpr("lp.wert", mandantCNr, locUI),
					QueryParameters.FLR_BREITE_PREIS, RechnungFac.FLR_RECHNUNG_N_WERTFW);

			columns.add("lp.whg", String.class, getTextRespectUISpr("lp.whg", mandantCNr, locUI),
					QueryParameters.FLR_BREITE_WAEHRUNG, RechnungFac.FLR_RECHNUNG_WAEHRUNG_C_NR);

			columns.add("lp.rechnr", String.class, getTextRespectUISpr("lp.rechnr", mandantCNr, locUI),
					QueryParameters.FLR_BREITE_M, Facade.NICHT_SORTIERBAR);
			columns.add("lp.fb", Boolean.class, getTextRespectUISpr("lp.fb", mandantCNr, locUI), 3,
					RechnungFac.FLR_RECHNUNG_T_FIBUUEBERNAHME,
					getTextRespectUISpr("rechnung.fb.tooltip", mandantCNr, locUI));

			setTableInfo(new TableInfo(columns.getClasses(), columns.getHeaderNames(), columns.getWidths(),
					columns.getDbColumNames(), columns.getHeaderToolTips()));

		}
		return super.getTableInfo();
	}

	public PrintInfoDto getSDocPathAndPartner(Object key) {
		RechnungDto rechnungDto = null;
		KundeDto kundeDto = null;
		PartnerDto partnerDto = null;
		try {
			rechnungDto = getRechnungFac().rechnungFindByPrimaryKey((Integer) key);
			kundeDto = getKundeFac().kundeFindByPrimaryKey(rechnungDto.getKundeIId(), theClientDto);
			partnerDto = getPartnerFac().partnerFindByPrimaryKey(kundeDto.getPartnerIId(), theClientDto);
		} catch (Exception e) {
			// Nicht gefunden
		}
		if (rechnungDto != null) {
			// String sPath = JCRDocFac.HELIUMV_NODE + "/"
			// + theClientDto.getMandant() + "/"
			// + LocaleFac.BELEGART_RECHNUNG.trim() + "/"
			// + LocaleFac.BELEGART_GUTSCHRIFT.trim() + "/"
			// + rechnungDto.getCNr().replace("/", ".");
			DocPath docPath = new DocPath(new DocNodeGutschrift(rechnungDto));
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
		return "RECHNUNG";
	}
}
