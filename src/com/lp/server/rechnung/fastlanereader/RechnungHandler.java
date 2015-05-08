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
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.Icon;

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
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnung;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.system.fastlanereader.generated.FLRLandplzort;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeRechnung;
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
 * Hier wird die FLR Funktionalitaet fuer die Rechnung implementiert. Pro
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

public class RechnungHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Integer iAnlegerStattVertreterAnzeigen = 0;
	boolean bBruttoStattNetto = false;
	public static final String FLR_RECHNUNG = "rechnung.";
	boolean bHatFibu = false;

	/**
	 * gets the data page for the specified row using the current query. The row
	 * at rowIndex will be located in the middle of the page.
	 * 
	 * @see UseCaseHandler#getPageAt(java.lang.Integer)
	 * @param rowIndex
	 *            Integer
	 * @throws EJBExceptionLP
	 * @return QueryResult
	 */
	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = PAGE_SIZE;
			int startIndex = Math.max(rowIndex.intValue() - (pageSize / 2), 0);
			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			String queryString = "SELECT rechnung, (SELECT count(*) FROM FLRFinanzBelegbuchung as bb WHERE bb.belegart_c_nr='"
					+ LocaleFac.BELEGART_RECHNUNG
					+ "' AND bb.i_belegiid=rechnung.i_id  ),(SELECT  MAX(m.mahnstufe_i_id) FROM FLRFinanzMahnung as m  WHERE m.flrrechnungreport.i_id=rechnung.i_id AND m.t_gedruckt IS NOT NULL  ) from FLRRechnung as rechnung "
					+ " left join rechnung.flrkunde.flrpartner.flrlandplzort as flrlandplzort "
					+ " left join rechnung.flrkunde.flrpartner.flrlandplzort.flrort as flrort "
					+ " left join rechnung.flrkunde.flrpartner.flrlandplzort.flrland as flrland "
					+ " left join rechnung.flrvertreter as flrvertreter "
					+ this.buildWhereClause() + this.buildOrderByClause();
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
				FLRRechnung rechnung = (FLRRechnung) o[0];
				long lVerbucht = (Long) o[1];
				Integer mahnstufeIId = (Integer) o[2];
				rows[row][col++] = rechnung.getI_id();
				// Den Anfangsbuchstaben der Rechnungsart
				rows[row][col++] = rechnung.getFlrrechnungart() == null ? null
						: rechnung.getFlrrechnungart().getC_nr()
								.substring(0, 1);
				rows[row][col++] = rechnung.getC_nr();
				FLRLandplzort anschrift = null;
				String kunde = null;
				kunde = rechnung.getFlrkunde() == null ? null : rechnung
						.getFlrkunde().getFlrpartner()
						.getC_name1nachnamefirmazeile1();
				anschrift = rechnung.getFlrkunde().getFlrpartner()
						.getFlrlandplzort();

				rows[row][col++] = kunde;
				if (anschrift != null) {
					rows[row][col++] = anschrift.getFlrland().getC_lkz() + "-"
							+ anschrift.getC_plz() + " "
							+ anschrift.getFlrort().getC_name();
				} else {
					rows[row][col++] = "";
				}
				rows[row][col++] = rechnung.getD_belegdatum();

				if (iAnlegerStattVertreterAnzeigen == 1) {
					if (rechnung.getFlrpersonalanleger() != null) {
						rows[row][col++] = rechnung.getFlrpersonalanleger()
								.getC_kurzzeichen();
					} else {
						rows[row][col++] = null;
					}
				} else if (iAnlegerStattVertreterAnzeigen == 2) {
					if (rechnung.getFlrpersonalaenderer() != null) {
						rows[row][col++] = rechnung.getFlrpersonalaenderer()
								.getC_kurzzeichen();
					} else {
						rows[row][col++] = null;
					}
				} else {
					if (rechnung.getFlrvertreter() != null) {
						rows[row][col++] = rechnung.getFlrvertreter()
								.getC_kurzzeichen();
					} else {
						rows[row][col++] = null;
					}
				}

				String sStatus = rechnung.getStatus_c_nr();
				rows[row][col++] = getStatusMitUebersetzung(sStatus,
						rechnung.getT_versandzeitpunkt(),
						rechnung.getC_versandtype());
				if (bDarfPreiseSehen) {
					if (bBruttoStattNetto == false) {
						rows[row][col++] = rechnung.getN_wertfw();
					} else {

						if (rechnung.getN_wertfw() != null
								&& rechnung.getN_wertustfw() != null) {
							rows[row][col++] = rechnung.getN_wertfw().add(
									rechnung.getN_wertustfw());
						} else {
							rows[row][col++] = null;
						}

					}

				} else {
					rows[row][col++] = null;
				}
				rows[row][col++] = rechnung.getWaehrung_c_nr();
				if (rechnung.getT_mahnsperrebis() != null) {
					rows[row][col++] = getTextRespectUISpr("lp.m",
							theClientDto.getMandant(), theClientDto.getLocUi());
				} else {
					rows[row][col++] = mahnstufeIId != null ? mahnstufeIId
							.toString() : "";
				}
				if (bHatFibu == true) {
					if (lVerbucht > 0) {
						rows[row][col++] = new Boolean(true);
					} else {
						rows[row][col++] = new Boolean(false);
					}

				} else {
					rows[row][col++] = new Boolean(
							rechnung.getT_fibuuebernahme() != null);
				}
				row++;
				col = 0;
			}
			result = new QueryResult(rows, this.getRowCount(), startIndex,
					endIndex, 0);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} finally {
			closeSession(session);
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
			String queryString = "select count(*) from FLRRechnung as rechnung "
					+ " left join rechnung.flrkunde.flrpartner.flrlandplzort as flrlandplzort "
					+ " left join rechnung.flrkunde.flrpartner.flrlandplzort.flrort as flrort "
					+ " left join rechnung.flrkunde.flrpartner.flrlandplzort.flrland as flrland "
					+ " left join rechnung.flrvertreter as flrvertreter "
					+ this.buildWhereClause();
			Query query = session.createQuery(queryString);
			List<?> rowCountResult = query.list();
			if (rowCountResult != null && rowCountResult.size() > 0) {
				rowCount = ((Long) rowCountResult.get(0)).longValue();
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
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
					// belegfilter: 1 wenn nach der c_nr gefilter wird, wird das
					// kriterium veraendert
					if (filterKriterien[i].kritName
							.equals(RechnungFac.FLR_RECHNUNG_C_NR)) {
						try {
							String sValue = super.buildWhereBelegnummer(
									filterKriterien[i], false);
							// Belegnummernsuche auch in "altem" Jahr, wenn im
							// neuen noch keines vorhanden ist
							if (!istBelegnummernInJahr("FLRRechnung", sValue)) {
								sValue = super.buildWhereBelegnummer(
										filterKriterien[i], true);
							}
							where.append(" rechnung."
									+ filterKriterien[i].kritName);
							where.append(" " + filterKriterien[i].operator);
							where.append(" " + sValue);
						} catch (Exception ex) {
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR,
									ex);
						}
					} else if (filterKriterien[i].kritName
							.equals(RechnungFac.FLR_RECHNUNG_C_BEZ)) {
						
						where.append(" (upper(rechnung.c_bez) LIKE "
								+ filterKriterien[i].value.toUpperCase()
								+ " OR upper(rechnung.c_bestellnummer) LIKE "
								+ filterKriterien[i].value.toUpperCase() + ")");
						
					} else if (filterKriterien[i].kritName
							.equals(RechnungFac.FLR_RECHNUNG_FLRKUNDE
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
								where.append(" (upper(" + FLR_RECHNUNG
										+ filterKriterien[i].kritName + ")");
								where.append(" " + filterKriterien[i].operator);
								where.append(" "
										+ filterKriterien[i].value
												.toUpperCase());
								where.append("OR upper(" + FLR_RECHNUNG
										+ "flrkunde.flrpartner.c_kbez" + ")");
								where.append(" " + filterKriterien[i].operator);
								where.append(" "
										+ filterKriterien[i].value
												.toUpperCase() + ") ");
							} else {
								where.append(" (" + FLR_RECHNUNG
										+ filterKriterien[i].kritName);
								where.append(" " + filterKriterien[i].operator);
								where.append(" " + filterKriterien[i].value);
								where.append("OR " + FLR_RECHNUNG
										+ "flrkunde.flrpartner.c_kbez");
								where.append(" " + filterKriterien[i].operator);
								where.append(" " + filterKriterien[i].value
										+ ") ");
							}
						} else {
							if (filterKriterien[i].isBIgnoreCase()) {
								where.append(" upper(" + FLR_RECHNUNG
										+ filterKriterien[i].kritName + ")");
							} else {
								where.append(" " + FLR_RECHNUNG
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
						// belegfilter: 2 der rest im else-block
						where.append(buildWhereClausePart("rechnung.",
								filterKriterien[i]));
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
							orderBy.append("rechnung." + kriterien[i].kritName);
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
				orderBy.append(// "rechnung." +
				// RechnungFac.FLR_RECHNUNG_I_GESCHAEFTSJAHR
				// +" DESC,"+
				"rechnung." + RechnungFac.FLR_RECHNUNG_C_NR + " DESC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("rechnung." + RechnungFac.FLR_RECHNUNG_C_NR) < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" rechnung." + RechnungFac.FLR_RECHNUNG_C_NR
						+ " ");
				sortAdded = true;
			}
			if (sortAdded) {
				orderBy.insert(0, " ORDER BY ");
			}
		}
		return orderBy.toString();
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
				String queryString = "select rechnung."
						+ RechnungFac.FLR_RECHNUNG_I_ID
						+ " from FLRRechnung rechnung "
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

	/**
	 * gets information about the Rechnungstable.
	 * 
	 * @return TableInfo
	 */
	public TableInfo getTableInfo() {
		if (super.getTableInfo() == null) {
			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();

			try {
				ParametermandantDto parameter = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_ALLGEMEIN,
								ParameterFac.PARAMETER_ANZEIGE_ANLEGER_STATT_VERTRETER);
				iAnlegerStattVertreterAnzeigen = (Integer) parameter
						.getCWertAsObject();
				parameter = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_RECHNUNG,
								ParameterFac.PARAMETER_BRUTTO_STATT_NETTO_IN_AUSWAHLLISTE);
				bBruttoStattNetto = (Boolean) parameter.getCWertAsObject();
				bHatFibu = getMandantFac().darfAnwenderAufModulZugreifen(
						LocaleFac.BELEGART_FINANZBUCHHALTUNG, theClientDto);

			} catch (RemoteException ex) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
			}

			setTableInfo(new TableInfo(
					new Class[] { Integer.class, String.class, String.class,
							String.class, String.class, Date.class,
							String.class, Icon.class, BigDecimal.class,
							String.class, String.class, Boolean.class },
					new String[] {
							"Id",
							" ",
							getTextRespectUISpr("lp.rechnr", mandantCNr, locUI),
							getTextRespectUISpr("lp.kunde", mandantCNr, locUI),
							getTextRespectUISpr("lp.ort", mandantCNr, locUI),
							getTextRespectUISpr("lp.datum", mandantCNr, locUI),
							getTextRespectUISpr("lp.vertreter", mandantCNr,
									locUI),
							getTextRespectUISpr("lp.status", mandantCNr, locUI),
							bBruttoStattNetto ? getTextRespectUISpr(
									"lp.bruttobetrag", mandantCNr, locUI)
									: getTextRespectUISpr("lp.nettobetrag",
											mandantCNr, locUI),
							getTextRespectUISpr("lp.whg", mandantCNr, locUI),
							getTextRespectUISpr("lp.m", mandantCNr, locUI),
							getTextRespectUISpr("lp.fb", mandantCNr, locUI) },
					new int[] { -1, 1, QueryParameters.FLR_BREITE_M, -1, -1,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_XS,
							QueryParameters.FLR_BREITE_XS,
							QueryParameters.FLR_BREITE_PREIS,
							QueryParameters.FLR_BREITE_WAEHRUNG, 2, 3 },
					new String[] {
							RechnungFac.FLR_RECHNUNG_I_ID,
							RechnungFac.FLR_RECHNUNG_FLRRECHNUNGART + "."
									+ RechnungFac.FLR_RECHNUNGART_C_NR,
							RechnungFac.FLR_RECHNUNG_C_NR,
							RechnungFac.FLR_RECHNUNG_FLRKUNDE
									+ "."
									+ KundeFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
							// Sortierung fuers erste mal nach LKZ
							RechnungFac.FLR_RECHNUNG_FLRKUNDE + "."
									+ KundeFac.FLR_PARTNER + "."
									+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT
									+ "."
									+ SystemFac.FLR_LP_FLRLAND
									+ "."
									+ SystemFac.FLR_LP_LANDLKZ
									+ ", "
									+
									// und dann nach plz
									"rechnung."
									+ RechnungFac.FLR_RECHNUNG_FLRKUNDE + "."
									+ KundeFac.FLR_PARTNER + "."
									+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT
									+ "." + SystemFac.FLR_LP_LANDPLZORTPLZ,
							RechnungFac.FLR_RECHNUNG_D_BELEGDATUM,
							RechnungFac.FLR_RECHNUNG_FLRVERTRETER + "."
									+ PersonalFac.FLR_PERSONAL_C_KURZZEICHEN,
							RechnungFac.FLR_RECHNUNG_STATUS_C_NR,
							bBruttoStattNetto ? RechnungFac.FLR_RECHNUNG_N_WERTFW
									+ "+rechnung.n_wertustfw"
									: RechnungFac.FLR_RECHNUNG_N_WERTFW,
							RechnungFac.FLR_RECHNUNG_WAEHRUNG_C_NR,
							Facade.NICHT_SORTIERBAR,
							RechnungFac.FLR_RECHNUNG_T_FIBUUEBERNAHME }));
		}

		return super.getTableInfo();
	}

	public PrintInfoDto getSDocPathAndPartner(Object key) {
		RechnungDto rechnungDto = null;
		KundeDto kundeDto = null;
		PartnerDto partnerDto = null;
		try {
			rechnungDto = getRechnungFac().rechnungFindByPrimaryKey(
					(Integer) key);
			kundeDto = getKundeFac().kundeFindByPrimaryKey(
					rechnungDto.getKundeIId(), theClientDto);
			partnerDto = getPartnerFac().partnerFindByPrimaryKey(
					kundeDto.getPartnerIId(), theClientDto);
		} catch (Exception e) {
			// Nicht gefunden
		}
		if (rechnungDto != null) {
			// String sPath = JCRDocFac.HELIUMV_NODE + "/"
			// + theClientDto.getMandant() + "/"
			// + LocaleFac.BELEGART_RECHNUNG.trim() + "/"
			// + LocaleFac.BELEGART_RECHNUNG.trim() + "/"
			// + rechnungDto.getCNr().replace("/", ".");
			DocPath docPath = new DocPath(new DocNodeRechnung(rechnungDto));
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
