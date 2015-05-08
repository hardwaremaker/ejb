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
package com.lp.server.artikel.fastlanereader;

import java.awt.Color;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.artikel.fastlanereader.generated.FLRFehlmenge;
import com.lp.server.artikel.fastlanereader.generated.FLRFehlmengeReservierung;
import com.lp.server.artikel.fastlanereader.generated.service.FLRFehlmengeReservierungPK;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
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
 * Hier wird die FLR Funktionalitaet fuer die Fehlmengen implementiert.
 * </p>
 * <p>
 * Copright Logistik Pur Software GmbH (c) 2004-2007,2005,2006
 * </p>
 * <p>
 * Erstellungsdatum 13.10.2005
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version 1.0
 */
public class FehlmengeReservierungAufloesenHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String FLR_FEHLMENGE = "flrfehlmengereservierung.";
	private static final String FLR_FEHLMENGE_FROM_CLAUSE = "SELECT flrfehlmengereservierung from FLRFehlmengeReservierung flrfehlmengereservierung  LEFT OUTER JOIN flrfehlmengereservierung.flrlossollmaterial.flrlos.flrstueckliste AS stkl LEFT OUTER JOIN flrfehlmengereservierung.flrlossollmaterial.flrlos AS los   LEFT OUTER JOIN flrfehlmengereservierung.flrauftragposition.flrauftrag AS auftrag";

	boolean bEndeStattBeginnTermin = false;

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
			String queryString = this.getFromClause() + this.buildWhereClause()
					+ this.buildOrderByClause();
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;
			int col = 0;
			while (resultListIterator.hasNext()) {
				FLRFehlmengeReservierung fm = (FLRFehlmengeReservierung) resultListIterator
						.next();
				rows[row][col++] = fm.getCompId();
				rows[row][col++] = fm.getCompId().getTyp();
				if (fm.getFlrauftragposition() != null) {
					rows[row][col++] = fm.getFlrauftragposition()
							.getFlrauftrag().getC_nr();
				} else {
					rows[row][col++] = null;
				}

				if (fm.getFlrlossollmaterial() != null) {
					rows[row][col++] = fm.getFlrlossollmaterial().getFlrlos()
							.getC_nr();
				} else {
					rows[row][col++] = null;
				}

				rows[row][col++] = fm.getFlrartikelliste().getC_nr();

				rows[row][col++] = getArtikelFac()
						.artikelFindByPrimaryKeySmall(
								fm.getFlrartikelliste().getI_id(), theClientDto)
						.formatBezeichnung();
				rows[row][col++] = fm.getN_menge();

				if (bEndeStattBeginnTermin) {
					rows[row][col++] = fm.getT_produktionsende();
				} else {
					rows[row][col++] = fm.getT_liefertermin();
				}

				if (fm.getFlrlossollmaterial() != null
						&& fm.getFlrlossollmaterial().getFlrlos()
								.getFlrstueckliste() != null) {
					rows[row][col++] = Helper.short2Boolean(fm
							.getFlrlossollmaterial().getFlrlos()
							.getFlrstueckliste()
							.getB_materialbuchungbeiablieferung());
				} else {
					rows[row][col++] = null;
				}

				if (fm.getCompId().getTyp().equals("R")
						&& fm.getFlrlossollmaterial() != null) {
					rows[row][col++] = Color.GRAY;
				}

				row++;
				col = 0;
			}
			result = new QueryResult(rows, this.getRowCount(), startIndex,
					endIndex, 0);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			closeSession(session);
		}
		return result;
	}

	protected long getRowCountFromDataBase() {
		long rowCount = 0;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			String queryString = "SELECT count(*) from FLRFehlmengeReservierung flrfehlmengereservierung  LEFT OUTER JOIN flrfehlmengereservierung.flrlossollmaterial.flrlos.flrstueckliste AS stkl LEFT OUTER JOIN flrfehlmengereservierung.flrlossollmaterial.flrlos AS los   LEFT OUTER JOIN flrfehlmengereservierung.flrauftragposition.flrauftrag AS auftrag "
					+ this.buildWhereClause();
			Query query = session.createQuery(queryString);
			List<?> rowCountResult = query.list();
			if (rowCountResult != null && rowCountResult.size() > 0) {
				rowCount = ((Long) rowCountResult.get(0)).longValue();
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
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

					if (filterKriterien[i].kritName.equals("flrlos.c_nr")) {

						try {

							String sValue = super.buildWhereBelegnummer(
									filterKriterien[i], false);

							// Belegnummernsuche auch in "altem" Jahr, wenn im
							// neuen noch keines vorhanden ist
							if (!istBelegnummernInJahr("FLRLos", sValue)) {
								sValue = super.buildWhereBelegnummer(
										filterKriterien[i], true);
							}
							where.append(" " + FLR_FEHLMENGE
									+ "flrlossollmaterial."
									+ filterKriterien[i].kritName);
							where.append(" " + filterKriterien[i].operator);
							where.append(" " + sValue);
						} catch (Exception ex) {
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR,
									ex);
						}

					} else {

						where.append(" " + FLR_FEHLMENGE
								+ filterKriterien[i].kritName);
						where.append(" " + filterKriterien[i].operator);
						where.append(" " + filterKriterien[i].value);
					}
				}
			}
			if (filterAdded) {
				where.insert(0, " WHERE");
			}

			if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
					MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
					theClientDto)
					&& getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_GETRENNTE_LAGER,
							theClientDto)) {

				if (filterAdded) {
					where.append(" AND (los IS NULL OR los.mandant_c_nr='"
							+ theClientDto.getMandant()
							+ "') AND (auftrag IS NULL OR auftrag.mandant_c_nr='"
							+ theClientDto.getMandant() + "')");
				} else {
					where.append(" WHERE (los IS NULL OR los.mandant_c_nr='"
							+ theClientDto.getMandant()
							+ "') AND (auftrag IS NULL OR auftrag.mandant_c_nr='"
							+ theClientDto.getMandant() + "')");
				}

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

				if (bEndeStattBeginnTermin) {
					orderBy.append(FLR_FEHLMENGE)
							.append("flrlossollmaterial.flrlos.t_produktionsende ")
							.append(" ASC ");
				} else {

					orderBy.append(FLR_FEHLMENGE).append("t_liefertermin ")
							.append(" DESC ");

				}

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
		return FLR_FEHLMENGE_FROM_CLAUSE;
	}

	public QueryResult sort(SortierKriterium[] sortierKriterien,
			Object selectedId) throws EJBExceptionLP {
		this.getQuery().setSortKrit(sortierKriterien);

		QueryResult result = null;
		int rowNumber = 0;

		if (selectedId != null) {
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();
				String queryString = "select "
						+ FLR_FEHLMENGE
						+ "compId from FLRFehlmengeReservierung flrfehlmengereservierung LEFT OUTER JOIN flrfehlmengereservierung.flrlossollmaterial.flrlos.flrstueckliste AS stkl  LEFT OUTER JOIN flrfehlmengereservierung.flrlossollmaterial.flrlos AS los   LEFT OUTER JOIN flrfehlmengereservierung.flrauftragposition.flrauftrag AS auftrag "
						+ this.buildWhereClause() + this.buildOrderByClause();
				Query query = session.createQuery(queryString);
				ScrollableResults scrollableResult = query.scroll();
				if (scrollableResult != null) {
					scrollableResult.beforeFirst();
					while (scrollableResult.next()) {
						FLRFehlmengeReservierungPK pk = (FLRFehlmengeReservierungPK) scrollableResult
								.get(0);
						if (selectedId.equals(pk)) {
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

	public TableInfo getTableInfo() {
		if (super.getTableInfo() == null) {

			try {
				ParametermandantDto parameter = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_FERTIGUNG,
								ParameterFac.PARAMETER_ENDE_TERMIN_ANSTATT_BEGINN_TERMIN_ANZEIGEN);
				bEndeStattBeginnTermin = (Boolean) parameter.getCWertAsObject();

			} catch (RemoteException ex) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
			}

			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();
			setTableInfo(new TableInfo(new Class[] { Integer.class,
					String.class, String.class, String.class, String.class,
					String.class, BigDecimal.class, java.util.Date.class,
					Boolean.class, Color.class },

			new String[] {
					"i_id",
					getTextRespectUISpr("lp.typ", mandantCNr, locUI),
					getTextRespectUISpr("lp.auftragnummer", mandantCNr, locUI),
					getTextRespectUISpr("lp.losnr", mandantCNr, locUI),
					getTextRespectUISpr("lp.artikel", mandantCNr, locUI),
					getTextRespectUISpr("lp.bezeichnung", mandantCNr, locUI),
					getTextRespectUISpr("lp.menge", mandantCNr, locUI),
					bEndeStattBeginnTermin ? getTextRespectUISpr(
							"artikel.fehlmengen.endetermin", mandantCNr, locUI)
							: getTextRespectUISpr(
									"artikel.fehlmengen.beginntermin",
									mandantCNr, locUI),
					getTextRespectUISpr("fert.materialbuchungbeiablieferung",
							mandantCNr, locUI), "" }, new int[] {
					QueryParameters.FLR_BREITE_SHARE_WITH_REST,
					QueryParameters.FLR_BREITE_S, QueryParameters.FLR_BREITE_M,
					QueryParameters.FLR_BREITE_M, QueryParameters.FLR_BREITE_M,
					QueryParameters.FLR_BREITE_SHARE_WITH_REST,
					QueryParameters.FLR_BREITE_PREIS,
					QueryParameters.FLR_BREITE_XM,
					QueryParameters.FLR_BREITE_S, 1 }, new String[] {
					FLR_FEHLMENGE + ArtikelFac.FLR_FEHLMENGE_I_ID,
					FLR_FEHLMENGE + "compId.typ",
					"auftrag.c_nr",
					"los.c_nr",
					FLR_FEHLMENGE + "flrartikelliste.c_nr",
					Facade.NICHT_SORTIERBAR,
					FLR_FEHLMENGE + ArtikelFac.FLR_FEHLMENGE_N_MENGE,

					bEndeStattBeginnTermin ? FLR_FEHLMENGE
							+ "t_produktionsende" : FLR_FEHLMENGE
							+ ArtikelFac.FLR_FEHLMENGE_T_LIEFERTERMIN

					, "stkl.b_materialbuchungbeiablieferung",
					Facade.NICHT_SORTIERBAR }));
		}
		return super.getTableInfo();
	}
}
