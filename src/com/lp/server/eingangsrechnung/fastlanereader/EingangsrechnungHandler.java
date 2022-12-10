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
package com.lp.server.eingangsrechnung.fastlanereader;

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

import com.lp.server.auftrag.fastlanereader.AuftragHandler;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnung;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.finanz.service.ReversechargeartDto;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.system.fastlanereader.generated.FLRLandplzort;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeEingangsrechnung;
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
 * Hier wird die FLR Funktionalitaet fuer die Eingangsrechnungen implementiert.
 * Pro UseCase gibt es einen Handler.
 * </p>
 * <p>
 * Copright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum 2005-02-13
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version 1.0
 */

public class EingangsrechnungHandler extends UseCaseHandler {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String FLR_EINGANGSRECHNUNG = "eingangsrechnung.";
	boolean bBruttoStattNetto = false;
	boolean bHatFibu = false;
	Integer iERPruefen = 0;

	private class EingangsrechnungLieferantFilterBuilder extends FlrFirmaAnsprechpartnerFilterBuilder {

		public EingangsrechnungLieferantFilterBuilder(boolean bSuchenInklusiveKBez) {
			super(bSuchenInklusiveKBez);
		}

		public String getFlrPartner() {
			return FLR_EINGANGSRECHNUNG + EingangsrechnungFac.FLR_ER_FLRLIEFERANT + "." + LieferantFac.FLR_PARTNER;
		}

		public String getFlrPropertyAnsprechpartnerIId() {
			return FLR_EINGANGSRECHNUNG;
		}

	}

	/**
	 * gets the data page for the specified row using the current query. The row at
	 * rowIndex will be located in the middle of the page.
	 * 
	 * @see UseCaseHandler#getPageAt(java.lang.Integer)
	 * @param rowIndex Integer
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
			String queryString = "SELECT eingangsrechnung,(SELECT count(*) FROM FLRFinanzBelegbuchung as bb WHERE bb.belegart_c_nr='"
					+ LocaleFac.BELEGART_EINGANGSRECHNUNG
					+ "' AND bb.i_belegiid=eingangsrechnung.i_id  ) from FLREingangsrechnung as eingangsrechnung "
					+ " left join eingangsrechnung.flrlieferant.flrpartner.flrlandplzort as flrlandplzort "
					+ " left join eingangsrechnung.flrlieferant.flrpartner.flrlandplzort.flrort as flrort "
					+ " left join eingangsrechnung.flrlieferant.flrpartner.flrlandplzort.flrland as flrland "
					+ this.buildWhereClause() + this.buildOrderByClause();
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;

			ReversechargeartDto rcartOhneDto = getFinanzServiceFac().reversechargeartFindOhne(theClientDto);

			while (resultListIterator.hasNext()) {
				// ortimhandler: 8 da kommt jetzt ein Array zurueck, das an
				// index 0 brauch ich

				Object[] o = (Object[]) resultListIterator.next();
				FLREingangsrechnung eingangsrechnung = (FLREingangsrechnung) o[0];

				long lVerbucht = (Long) o[1];

				Object[] rowToAddCandidate = new Object[colCount];

				rowToAddCandidate[getTableColumnInformation().getViewIndex("i_id")] = eingangsrechnung.getI_id();
				rowToAddCandidate[getTableColumnInformation()
						.getViewIndex("art")] = eingangsrechnung.getEingangsrechnungart_c_nr() == null ? null
								: eingangsrechnung.getEingangsrechnungart_c_nr().substring(0, 1);
				rowToAddCandidate[getTableColumnInformation()
						.getViewIndex("er.eingangsrechnungsnummer")] = eingangsrechnung.getC_nr();
				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.lieferant")] = eingangsrechnung
						.getFlrlieferant().getFlrpartner().getC_name1nachnamefirmazeile1();
				// ortimhandler: 1 wenn vorhanden, dann im Format A-5020
				// Salzburg
				FLRLandplzort anschrift = eingangsrechnung.getFlrlieferant().getFlrpartner().getFlrlandplzort();
				if (anschrift != null) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.ort")] = anschrift.getFlrland()
							.getC_lkz() + "-" + anschrift.getC_plz() + " " + anschrift.getFlrort().getC_name();
				}
				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.datum")] = eingangsrechnung
						.getT_belegdatum();
				String sStatus = eingangsrechnung.getStatus_c_nr();
				if (eingangsrechnung.getT_gedruckt() != null) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.status")] = getStatusMitUebersetzung(
							sStatus, eingangsrechnung.getT_gedruckt(), "DRUCKER");
				} else {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.status")] = getStatusMitUebersetzung(
							sStatus);
				}

				String lrnText = "";
				if (eingangsrechnung.getC_lieferantenrechnungsnummer() != null) {
					lrnText = eingangsrechnung.getC_lieferantenrechnungsnummer();
					if (eingangsrechnung.getC_text() != null) {
						lrnText = lrnText + " " + eingangsrechnung.getC_text();
					}

				} else if (eingangsrechnung.getC_text() != null) {
					lrnText = eingangsrechnung.getC_text();
				} else {
					lrnText = "";
				}
				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.text")] = lrnText;
				if (bBruttoStattNetto == false) {

					if (Helper.short2boolean(eingangsrechnung.getB_igerwerb())
							|| !rcartOhneDto.getIId().equals(eingangsrechnung.getReversechargeartId())) {
						rowToAddCandidate[getTableColumnInformation()
								.getViewIndex("lp.nettobetrag")] = eingangsrechnung.getN_betragfw();
					} else {
						if (eingangsrechnung.getN_betragfw() != null && eingangsrechnung.getN_ustbetragfw() != null) {
							rowToAddCandidate[getTableColumnInformation()
									.getViewIndex("lp.nettobetrag")] = eingangsrechnung.getN_betragfw()
											.subtract(eingangsrechnung.getN_ustbetragfw());
						}
					}

				} else {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.bruttobetrag")] = eingangsrechnung
							.getN_betragfw();
				}
				rowToAddCandidate[getTableColumnInformation().getViewIndex("er.whg")] = eingangsrechnung
						.getWaehrung_c_nr();

				if (iERPruefen>0) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("er.geprueft")] = new Boolean(
							eingangsrechnung.getT_geprueft() != null);
				}

				if (bHatFibu == true) {
					if (lVerbucht > 0) {
						rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.fb")] = new Boolean(true);
					} else {
						rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.fb")] = new Boolean(false);
					}

				} else {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.fb")] = new Boolean(
							eingangsrechnung.getT_fibuuebernahme() != null);

				}

				// PJ20436

				boolean bMehrfach = eingangsrechnung.getKonto_i_id() == null
						|| eingangsrechnung.getKostenstelle_i_id() == null;

				if (bMehrfach) {
					BigDecimal bdNochNichtKontiert = getEingangsrechnungFac()
							.getWertNochNichtKontiert(eingangsrechnung.getI_id());

					if (bdNochNichtKontiert.signum() != 0) {
						rowToAddCandidate[getTableColumnInformation().getViewIndex("Color")] = Color.RED;
					} else {
						if (eingangsrechnung.getAuftragszuordnungset().size() > 0) {
							rowToAddCandidate[getTableColumnInformation().getViewIndex("Color")] = Color.BLUE;
						}

					}
				} else {
					if (eingangsrechnung.getAuftragszuordnungset().size() > 0) {
						rowToAddCandidate[getTableColumnInformation().getViewIndex("Color")] = Color.BLUE;
					}
				}

				rows[row] = rowToAddCandidate;

				row++;

			}
			result = new QueryResult(rows, this.getRowCount(), startIndex, endIndex, 0);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} catch (Throwable t) {
			myLogger.error("Throwable: ", t);
		} finally {
			sessionClose(session);
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
			String queryString = "select count(*) from FLREingangsrechnung as eingangsrechnung "
					+ " left join eingangsrechnung.flrlieferant.flrpartner.flrlandplzort as flrlandplzort "
					+ " left join eingangsrechnung.flrlieferant.flrpartner.flrlandplzort.flrort as flrort "
					+ " left join eingangsrechnung.flrlieferant.flrpartner.flrlandplzort.flrland as flrland "
					+ this.buildWhereClause();
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
					if (filterKriterien[i].kritName.equals(EingangsrechnungFac.FLR_ER_C_NR)) {
						try {
							String sValue = super.buildWhereBelegnummer(filterKriterien[i], false,
									ParameterFac.KATEGORIE_EINGANGSRECHNUNG,
									ParameterFac.PARAMETER_EINGANGSRECHNUNG_BELEGNUMMERSTARTWERT);
							// Belegnummernsuche auch in "altem" Jahr, wenn im
							// neuen noch keines vorhanden ist
							if (!istBelegnummernInJahr("FLREingangsrechnung", sValue, "eingangsrechnungart_c_nr<>'"
									+ EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN + "'")) {
								sValue = super.buildWhereBelegnummer(filterKriterien[i], true);
							}
							where.append(" eingangsrechnung." + filterKriterien[i].kritName);
							where.append(" " + filterKriterien[i].operator);
							where.append(" " + sValue);
						} catch (Exception ex) {
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, ex);
						}
					} else if (filterKriterien[i].kritName.equals("c_suche")) {
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" lower(" + "eingangsrechnung."
									+ EingangsrechnungFac.FLR_ER_FLREINGANGSRECHNUNGTEXTSUCHE + "."
									+ filterKriterien[i].kritName + ")");
						} else {
							where.append(
									" " + "eingangsrechnung." + EingangsrechnungFac.FLR_ER_FLREINGANGSRECHNUNGTEXTSUCHE
											+ "." + filterKriterien[i].kritName);
						}
						where.append(" " + filterKriterien[i].operator);
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" " + filterKriterien[i].value.toLowerCase());
						} else {
							where.append(" " + filterKriterien[i].value);
						}
					} else if (isAdresseLieferant(filterKriterien[i])) {
						EingangsrechnungLieferantFilterBuilder erLieferantFilterBuilder = new EingangsrechnungLieferantFilterBuilder(
								getParameterFac().getSuchenInklusiveKBez(theClientDto.getMandant()));
						erLieferantFilterBuilder.buildFirmaFilter(filterKriterien[i], where);
						// buildFirmaFilterOld(filterKriterien[i], where);
					} else {
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" lower(eingangsrechnung." + filterKriterien[i].kritName + ")");
						} else {
							where.append(" eingangsrechnung." + filterKriterien[i].kritName);
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

	private void buildFirmaFilterOld(FilterKriterium filterKriterium, StringBuffer where) {
		ParametermandantDto parameter = null;
		try {
			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_SUCHEN_INKLUSIVE_KBEZ);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		Boolean bSuchenInklusiveKbez = (java.lang.Boolean) parameter.getCWertAsObject();
		if (bSuchenInklusiveKbez) {
			if (filterKriterium.isBIgnoreCase()) {
				where.append(" (lower(eingangsrechnung." + filterKriterium.kritName + ")");
				where.append(" " + filterKriterium.operator);
				where.append(" " + filterKriterium.value.toLowerCase());
				where.append("OR lower(eingangsrechnung." + "flrlieferant.flrpartner.c_kbez" + ")");
				where.append(" " + filterKriterium.operator);
				where.append(" " + filterKriterium.value.toLowerCase() + ")");
			} else {
				where.append("( eingangsrechnung." + filterKriterium.kritName);
				where.append(" " + filterKriterium.operator);
				where.append(" " + filterKriterium.value);
				where.append("OR eingangsrechnung." + "flrlieferant.flrpartner.c_kbez");
				where.append(" " + filterKriterium.operator);
				where.append(" " + filterKriterium.value + ")");
			}
		} else {
			if (filterKriterium.isBIgnoreCase()) {
				where.append(" lower(eingangsrechnung." + filterKriterium.kritName + ")");
			} else {
				where.append(" eingangsrechnung." + filterKriterium.kritName);
			}
			where.append(" " + filterKriterium.operator);
			if (filterKriterium.isBIgnoreCase()) {
				where.append(" " + filterKriterium.value.toLowerCase());
			} else {
				where.append(" " + filterKriterium.value);
			}
		}

	}

	private boolean isAdresseLieferant(FilterKriterium filterKriterium) {
		return filterKriterium.kritName.equals(EingangsrechnungFac.FLR_ER_FLRLIEFERANT + "." + LieferantFac.FLR_PARTNER
				+ "." + PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1);
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
							orderBy.append("eingangsrechnung." + kriterien[i].kritName);
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
				orderBy.append( // "eingangsrechnung." +
						// EingangsrechnungFac.FLR_ER_I_GESCHEAFTSJAHR
						// +" DESC,"+
						"eingangsrechnung." + EingangsrechnungFac.FLR_ER_C_NR + " DESC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("eingangsrechnung.c_nr") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" eingangsrechnung.c_nr ");
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
				String queryString = "select eingangsrechnung.i_id from FLREingangsrechnung eingangsrechnung "
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

	public TableInfo getTableInfo() {

		TableInfo info = super.getTableInfo();
		if (info != null)
			return info;

		try {
			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_RECHNUNG, ParameterFac.PARAMETER_BRUTTO_STATT_NETTO_IN_AUSWAHLLISTE);
			bBruttoStattNetto = (Boolean) parameter.getCWertAsObject();
			bHatFibu = getMandantFac().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_FINANZBUCHHALTUNG,
					theClientDto);

			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_EINGANGSRECHNUNG, ParameterFac.PARAMETER_EINGANGSRECHNUNG_PRUEFEN);
			iERPruefen = ((Integer) parameter.getCWertAsObject());

		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		setTableColumnInformation(createColumnInformation(theClientDto.getMandant(), theClientDto.getLocUi()));

		TableColumnInformation c = getTableColumnInformation();
		info = new TableInfo(c.getClasses(), c.getHeaderNames(), c.getWidths(), c.getDbColumNames(),
				c.getHeaderToolTips());
		setTableInfo(info);
		return info;

	}

	private TableColumnInformation createColumnInformation(String mandant, Locale locUi) {

		TableColumnInformation columns = new TableColumnInformation();

		columns.add("i_id", Integer.class, "i_id", QueryParameters.FLR_BREITE_SHARE_WITH_REST,
				EingangsrechnungFac.FLR_ER_I_ID);
		columns.add("art", String.class, " ", 1, EingangsrechnungFac.FLR_ER_EINGANGSRECHNUNGART_C_NR);
		columns.add("er.eingangsrechnungsnummer", String.class,
				getTextRespectUISpr("er.eingangsrechnungsnummer", mandant, locUi), QueryParameters.FLR_BREITE_M,
				EingangsrechnungFac.FLR_ER_C_NR);
		columns.add("lp.lieferant", String.class, getTextRespectUISpr("lp.lieferant", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST,
				EingangsrechnungFac.FLR_ER_FLRLIEFERANT + "." + LieferantFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1);
		columns.add("lp.ort", String.class, getTextRespectUISpr("lp.ort", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST,
				EingangsrechnungFac.FLR_ER_FLRLIEFERANT + "." + LieferantFac.FLR_PARTNER + "."
						+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT + "." + SystemFac.FLR_LP_FLRLAND + "."
						+ SystemFac.FLR_LP_LANDLKZ + ", " +
						// ortimhandler: 6 und dann nach plz
						"eingangsrechnung." + EingangsrechnungFac.FLR_ER_FLRLIEFERANT + "." + LieferantFac.FLR_PARTNER
						+ "." + PartnerFac.FLR_PARTNER_FLRLANDPLZORT + "." + SystemFac.FLR_LP_LANDPLZORTPLZ);

		columns.add("lp.datum", Date.class, getTextRespectUISpr("lp.datum", mandant, locUi),
				QueryParameters.FLR_BREITE_M, EingangsrechnungFac.FLR_ER_D_BELEGDATUM);
		columns.add("lp.status", Icon.class, getTextRespectUISpr("lp.status", mandant, locUi),
				QueryParameters.FLR_BREITE_XS, EingangsrechnungFac.FLR_ER_STATUS_C_NR);
		columns.add("lp.text", String.class, getTextRespectUISpr("lp.text", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, EingangsrechnungFac.FLR_ER_C_LIEFERANTENRECHNUNGSNUMMER);

		if (bBruttoStattNetto) {
			columns.add("lp.bruttobetrag", BigDecimal.class, getTextRespectUISpr("lp.bruttobetrag", mandant, locUi),
					QueryParameters.FLR_BREITE_PREIS, EingangsrechnungFac.FLR_ER_N_BETRAGFW);
		} else {
			columns.add("lp.nettobetrag", BigDecimal.class, getTextRespectUISpr("lp.nettobetrag", mandant, locUi),
					QueryParameters.FLR_BREITE_PREIS, EingangsrechnungFac.FLR_ER_N_BETRAGFW + "-eingangsrechnung."
							+ EingangsrechnungFac.FLR_ER_N_USTBETRAGFW);
		}

		columns.add("er.whg", String.class, getTextRespectUISpr("er.whg", mandant, locUi),
				QueryParameters.FLR_BREITE_WAEHRUNG, EingangsrechnungFac.FLR_ER_WAEHRUNG_C_NR);

		if (iERPruefen>0) {
			columns.add("er.geprueft", Boolean.class, getTextRespectUISpr("er.geprueft", mandant, locUi), 3,
					"t_geprueft");
		}

		columns.add("lp.fb", Boolean.class, getTextRespectUISpr("lp.fb", mandant, locUi), 3,
				EingangsrechnungFac.FLR_ER_T_FIBUUEBERNAHME);

		columns.add("Color", Color.class, "", 1, Facade.NICHT_SORTIERBAR);

		return columns;
	}

	public PrintInfoDto getSDocPathAndPartner(Object key) {
		EingangsrechnungDto eingangsrechnungDto = null;
		LieferantDto lieferantDto = null;
		PartnerDto partnerDto = null;
		try {
			eingangsrechnungDto = getEingangsrechnungFac().eingangsrechnungFindByPrimaryKey((Integer) key);
			lieferantDto = getLieferantFac().lieferantFindByPrimaryKey(eingangsrechnungDto.getLieferantIId(),
					theClientDto);
			partnerDto = getPartnerFac().partnerFindByPrimaryKey(lieferantDto.getPartnerIId(), theClientDto);
		} catch (Exception e) {
			// Nicht gefunden
		}
		if (eingangsrechnungDto != null) {
			// String sPath = JCRDocFac.HELIUMV_NODE + "/"
			// + theClientDto.getMandant() + "/"
			// + LocaleFac.BELEGART_EINGANGSRECHNUNG.trim() + "/"
			// + LocaleFac.BELEGART_EINGANGSRECHNUNG.trim() + "/"
			// + eingangsrechnungDto.getCNr().replace("/", ".");
			DocPath docPath = new DocPath(new DocNodeEingangsrechnung(eingangsrechnungDto));
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
		return "EINGANGSRECHNUNG";
	}

}
