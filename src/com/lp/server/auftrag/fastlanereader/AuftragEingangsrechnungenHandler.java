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
package com.lp.server.auftrag.fastlanereader;

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

import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnung;
import com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnungAuftragszuordnung;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.system.fastlanereader.generated.FLRLandplzort;
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
import com.lp.util.Helper;

/**
 * <p>
 * Hier wird die FLR Funktionalitaet fuer die ER-Auftragszuordnungen
 * implementiert. Pro UseCase gibt es einen Handler.
 * </p>
 * <p>
 * Copright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum 2005-02-18
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version 1.0
 */

public class AuftragEingangsrechnungenHandler extends UseCaseHandler {

	boolean bBruttoStattNetto = false;

	private static final long serialVersionUID = 1L;

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
			String queryString = "SELECT er from FLREingangsrechnungAuftragszuordnung as er "
					+ " left join er.flreingangsrechnung.flrlieferant.flrpartner.flrlandplzort as flrlandplzort "
					+ " left join er.flreingangsrechnung.flrlieferant.flrpartner.flrlandplzort.flrort as flrort "
					+ " left join er.flreingangsrechnung.flrlieferant.flrpartner.flrlandplzort.flrland as flrland "
					+ this.buildWhereClause() + this.buildOrderByClause();
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;
			int col = 0;

			while (resultListIterator.hasNext()) {
				// ortimhandler: 8 da kommt jetzt ein Array zurueck, das an
				// index 0 brauch ich

				FLREingangsrechnungAuftragszuordnung flrEingangsrechnungAuftragszuordnung = (FLREingangsrechnungAuftragszuordnung) resultListIterator
						.next();
				FLREingangsrechnung eingangsrechnung = flrEingangsrechnungAuftragszuordnung
						.getFlreingangsrechnung();

				rows[row][col++] = eingangsrechnung.getI_id();
				rows[row][col++] = eingangsrechnung
						.getEingangsrechnungart_c_nr() == null ? null
						: eingangsrechnung.getEingangsrechnungart_c_nr()
								.substring(0, 1);
				rows[row][col++] = eingangsrechnung.getC_nr();
				rows[row][col++] = eingangsrechnung.getFlrlieferant()
						.getFlrpartner().getC_name1nachnamefirmazeile1();

				if (eingangsrechnung.getFlrbestellung() != null) {
					rows[row][col++] = eingangsrechnung.getFlrbestellung()
							.getC_nr();
				} else {
					rows[row][col++] = null;
				}

				rows[row][col++] = eingangsrechnung.getT_belegdatum();
				String sStatus = eingangsrechnung.getStatus_c_nr();
				if (eingangsrechnung.getT_gedruckt() != null) {
					rows[row][col++] = getStatusMitUebersetzung(sStatus,
							eingangsrechnung.getT_gedruckt(), "DRUCKER");
				} else {
					rows[row][col++] = getStatusMitUebersetzung(sStatus);
				}

				String lrnText = "";
				if (eingangsrechnung.getC_lieferantenrechnungsnummer() != null) {
					lrnText = eingangsrechnung
							.getC_lieferantenrechnungsnummer();
					if (eingangsrechnung.getC_text() != null) {
						lrnText = lrnText + " " + eingangsrechnung.getC_text();
					}

				} else if (eingangsrechnung.getC_text() != null) {
					lrnText = eingangsrechnung.getC_text();
				} else {
					lrnText = "";
				}
				rows[row][col++] = lrnText;

				rows[row][col++] = flrEingangsrechnungAuftragszuordnung
						.getC_text();
				rows[row][col++] = flrEingangsrechnungAuftragszuordnung
						.getN_betrag();

				if (bBruttoStattNetto == false) {
					if (eingangsrechnung.getN_betragfw() != null
							&& eingangsrechnung.getN_ustbetragfw() != null) {
						rows[row][col++] = eingangsrechnung.getN_betragfw()
								.subtract(eingangsrechnung.getN_ustbetragfw());
					} else {
						rows[row][col++] = null;
					}

				} else {
					rows[row][col++] = eingangsrechnung.getN_betragfw();
				}
				rows[row][col++] = eingangsrechnung.getWaehrung_c_nr();

				rows[row][col++] = Helper
						.short2Boolean(flrEingangsrechnungAuftragszuordnung
								.getB_keine_auftragswertung());

				row++;
				col = 0;
			}
			result = new QueryResult(rows, this.getRowCount(), startIndex,
					endIndex, 0);
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
			try {
				session.close();
			} catch (HibernateException he) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, he);
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
					where.append(" er." + filterKriterien[i].kritName);
					where.append(" " + filterKriterien[i].operator);
					where.append(" " + filterKriterien[i].value);
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
					if (kriterien[i].isKrit) {
						if (sortAdded) {
							orderBy.append(", ");
						}
						sortAdded = true;
						orderBy.append("er." + kriterien[i].kritName);
						orderBy.append(" ");
						orderBy.append(kriterien[i].value);
					}
				}
			} else {
				// no sort criteria found, add default sort
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append("er.flreingangsrechnung.c_nr ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("er.flreingangsrechnung.c_nr") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" er.flreingangsrechnung.c_nr ");
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
		return "from FLREingangsrechnungAuftragszuordnung er ";
	}

	/**
	 * sorts the data described by the current query using the specified sort
	 * criterias. The current query is also updated with the new sort criterias.
	 * 
	 * @see UseCaseHandler#sort(SortierKriterium[], Object)
	 * @throws EJBExceptionLP
	 * @param sortierKriterien
	 *            SortierKriterium[]
	 * @param selectedId
	 *            Object
	 * @return QueryResult
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
				String queryString = "select er.i_id from FLREingangsrechnungAuftragszuordnung er "
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

	/**
	 * gets information about the Kontentable.
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
								ParameterFac.KATEGORIE_RECHNUNG,
								ParameterFac.PARAMETER_BRUTTO_STATT_NETTO_IN_AUSWAHLLISTE);
				bBruttoStattNetto = (Boolean) parameter.getCWertAsObject();

			} catch (RemoteException ex) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
			}

			setTableInfo(new TableInfo(
					new Class[] { Integer.class, String.class, String.class,
							String.class, String.class, Date.class, Icon.class,
							String.class, String.class, BigDecimal.class,
							BigDecimal.class, String.class, Boolean.class },
					new String[] {
							"i_id",
							" ",
							getTextRespectUISpr("er.eingangsrechnungsnummer",
									mandantCNr, locUI),
							getTextRespectUISpr("lp.lieferant", mandantCNr,
									locUI),
							getTextRespectUISpr("bes.bestellung", mandantCNr,
									locUI),

							// ortimhandler: 3 Ueberschrift ist "Ort"
							getTextRespectUISpr("lp.datum", mandantCNr, locUI),
							getTextRespectUISpr("lp.status", mandantCNr, locUI),
							getTextRespectUISpr("lp.text", mandantCNr, locUI),
							getTextRespectUISpr(
									"auft.erzuordnung.splittkommentar",
									mandantCNr, locUI),
							getTextRespectUISpr(
									"auft.erzuordnung.splittbetrag",
									mandantCNr, locUI),
							bBruttoStattNetto ? getTextRespectUISpr(
									"lp.bruttobetrag", mandantCNr, locUI)
									: getTextRespectUISpr("lp.nettobetrag",
											mandantCNr, locUI),
							getTextRespectUISpr("er.whg", mandantCNr, locUI),
							getTextRespectUISpr("auft.keineauftragswertung.short",
									mandantCNr, locUI) },
					new int[] {
							-1, // ausgeblendet
							1,
							QueryParameters.FLR_BREITE_M,
							-1,
							-1,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_XS, // status
							QueryParameters.FLR_BREITE_M, -1,
							QueryParameters.FLR_BREITE_PREIS,
							QueryParameters.FLR_BREITE_PREIS,
							QueryParameters.FLR_BREITE_WAEHRUNG,
							QueryParameters.FLR_BREITE_S },
					new String[] {
							EingangsrechnungFac.FLR_ER_I_ID,
							"flreingangsrechnung."
									+ EingangsrechnungFac.FLR_ER_EINGANGSRECHNUNGART_C_NR,
							"flreingangsrechnung."
									+ EingangsrechnungFac.FLR_ER_C_NR,
							"flreingangsrechnung."
									+ EingangsrechnungFac.FLR_ER_FLRLIEFERANT
									+ "."
									+ LieferantFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
							Facade.NICHT_SORTIERBAR,
							"flreingangsrechnung."
									+ EingangsrechnungFac.FLR_ER_D_BELEGDATUM,
							"flreingangsrechnung."
									+ EingangsrechnungFac.FLR_ER_STATUS_C_NR,
							"flreingangsrechnung."
									+ EingangsrechnungFac.FLR_ER_C_LIEFERANTENRECHNUNGSNUMMER,
							"c_text",
							"n_betrag",
							"flreingangsrechnung."
									+ EingangsrechnungFac.FLR_ER_N_BETRAGFW,
							"flreingangsrechnung."
									+ EingangsrechnungFac.FLR_ER_WAEHRUNG_C_NR,
							"b_keine_auftragswertung" },
					new String[]{ null, null, null, null, null, null, null, null, null, null, null, null, 
							getTextRespectUISpr("auft.keineauftragswertung.tooltip", mandantCNr, locUI),
					}));
		}
		return super.getTableInfo();
	}
}
