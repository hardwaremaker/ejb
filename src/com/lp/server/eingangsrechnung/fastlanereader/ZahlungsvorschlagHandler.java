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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnungReport;
import com.lp.server.eingangsrechnung.fastlanereader.generated.FLRZahlungsvorschlag;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.partner.service.BankDto;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.partner.service.PartnerbankDto;
import com.lp.server.system.service.TheClientDto;
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
 * Diese Klasse kuemmert sich um den FLR Zahlungsvorschlaglauf
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 07.02.07
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2012/06/18 12:59:21 $
 */
public class ZahlungsvorschlagHandler extends UseCaseHandler {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String FLR_ZV = "flrzv.";
	private static final String FLR_ZV_FROM_CLAUSE = " from FLRZahlungsvorschlag flrzv ";

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		Locale locUI = theClientDto.getLocUi();
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int startIndex = Math.max(rowIndex.intValue() - (PAGE_SIZE / 2), 0);
			int endIndex = startIndex + PAGE_SIZE - 1;

			session = factory.openSession();
			String queryString = this.getFromClause() + this.buildWhereClause()
					+ this.buildOrderByClause();
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(PAGE_SIZE);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;
			int col = 0;
			while (resultListIterator.hasNext()) {
				FLRZahlungsvorschlag zv = (FLRZahlungsvorschlag) resultListIterator
						.next();
				FLREingangsrechnungReport er = zv
						.getFlreingangsrechnungreport();
				String sWaehrung = " " + er.getWaehrung_c_nr();

				rows[row][col++] = zv.getI_id();
				rows[row][col++] = er.getEingangsrechnungart_c_nr().substring(
						0, 1);
				rows[row][col++] = er.getC_nr();
				rows[row][col++] = er.getMahnstufe_i_id();
				rows[row][col++] = er.getFlrlieferant().getFlrpartner()
						.getC_name1nachnamefirmazeile1();
				rows[row][col++] = er.getN_betragfw();
				rows[row][col++] = zv.getT_faellig();
				if (zv.getN_angewandterskontosatz().doubleValue() > 0) {
					rows[row][col++] = Helper.formatZahl(
							zv.getN_angewandterskontosatz().setScale(
									FinanzFac.NACHKOMMASTELLEN,
									RoundingMode.HALF_UP),
							FinanzFac.NACHKOMMASTELLEN, locUI)
							+ " %";
				} else {
					rows[row][col++] = null;
				}

				rows[row][col++] = zv.getN_zahlbetrag();
				rows[row][col++] = sWaehrung;
				rows[row][col++] = er.getC_text();
				rows[row][col++] = new Boolean(Helper.short2Boolean(zv
						.getB_bezahlen()));
				// hat der LF eine Bankverbindung?
				Integer iPartnerIId = er.getFlrlieferant().getFlrpartner()
						.getI_id();
				PartnerbankDto[] bvLF = getBankFac()
						.partnerbankFindByPartnerIId(iPartnerIId, theClientDto);
				if (bvLF == null || bvLF.length == 0) {
					rows[row][col++] = "";
				} else {
					BankDto bankDto = getBankFac().bankFindByPrimaryKey(
							bvLF[0].getBankPartnerIId(), theClientDto);
					rows[row][col++] = bankDto.getPartnerDto()
							.getCName1nachnamefirmazeile1();

					rows[row][col++] = bvLF[0].getCIban();
					rows[row][col++] = bankDto.getCBic();

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
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (HibernateException he) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, he);
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
					where.append(" " + FLR_ZV + filterKriterien[i].kritName);
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
					if (!kriterien[i].kritName
							.endsWith(Facade.NICHT_SORTIERBAR)) {
						if (kriterien[i].isKrit) {
							if (sortAdded) {
								orderBy.append(", ");
							}
							sortAdded = true;
							orderBy.append(FLR_ZV + kriterien[i].kritName);
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
				orderBy.append(FLR_ZV)
						.append(EingangsrechnungFac.FLR_ZV_T_FAELLIG)
						.append(" ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf(FLR_ZV + EingangsrechnungFac.FLR_ZV_T_FAELLIG) < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" ").append(FLR_ZV)
						.append(EingangsrechnungFac.FLR_ZV_T_FAELLIG)
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
		return FLR_ZV_FROM_CLAUSE;
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
				String queryString = "select " + FLR_ZV
						+ EingangsrechnungFac.FLR_ZV_LAUF_I_ID
						+ FLR_ZV_FROM_CLAUSE + this.buildWhereClause()
						+ this.buildOrderByClause();
				Query query = session.createQuery(queryString);
				ScrollableResults scrollableResult = query.scroll();
				if (scrollableResult != null) {
					scrollableResult.beforeFirst();
					while (scrollableResult.next()) {
						Integer id = scrollableResult.getInteger(0);
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
			setTableInfo(new TableInfo(
					new Class[] { Integer.class, String.class, String.class,
							Integer.class, String.class, BigDecimal.class,
							java.util.Date.class, String.class,
							BigDecimal.class, String.class, String.class,
							Boolean.class, String.class, String.class,
							String.class },
					new String[] {
							"i_id",
							"",
							getTextRespectUISpr("er.eingangsrechnungsnummer",
									mandantCNr, locUI),
							getTextRespectUISpr("lp.mahnstufe", mandantCNr,
									locUI),
							getTextRespectUISpr("lp.firma", mandantCNr, locUI),
							getTextRespectUISpr("lp.wert", mandantCNr, locUI),
							getTextRespectUISpr("er.zv.faellig", mandantCNr,
									locUI),
							getTextRespectUISpr("system.skt", mandantCNr, locUI),
							getTextRespectUISpr(
									"er.zahlungsvorschlag.zahlbetrag",
									mandantCNr, locUI),
							getTextRespectUISpr("lp.waehrung", mandantCNr,
									locUI),
							getTextRespectUISpr("lp.text", mandantCNr, locUI),
							getTextRespectUISpr("lp.freigabezurueberweisung",
									mandantCNr, locUI),
							getTextRespectUISpr("lp.bankverbindung",
									mandantCNr, locUI),
							getTextRespectUISpr("lp.iban", mandantCNr, locUI),
							getTextRespectUISpr("lp.bic", mandantCNr, locUI) },
					new int[] { QueryParameters.FLR_BREITE_SHARE_WITH_REST, 1,
							QueryParameters.FLR_BREITE_M, 3,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_PREIS,
							QueryParameters.FLR_BREITE_M, 5,
							QueryParameters.FLR_BREITE_PREIS, 6,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST, 2,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST },
					new String[] {
							EingangsrechnungFac.FLR_ZV_I_ID,
							EingangsrechnungFac.FLR_ZV_FLREINGANGSRECHNUNG
									+ "."
									+ EingangsrechnungFac.FLR_ER_EINGANGSRECHNUNGART_C_NR,
							EingangsrechnungFac.FLR_ZV_FLREINGANGSRECHNUNG
									+ "." + EingangsrechnungFac.FLR_ER_C_NR,
							EingangsrechnungFac.FLR_ZV_FLREINGANGSRECHNUNG
									+ "."
									+ EingangsrechnungFac.FLR_ER_MAHNSTUFE_I_ID,
							EingangsrechnungFac.FLR_ZV_FLREINGANGSRECHNUNG
									+ "."
									+ EingangsrechnungFac.FLR_ER_FLRLIEFERANT
									+ "."
									+ LieferantFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
							Facade.NICHT_SORTIERBAR,
							EingangsrechnungFac.FLR_ZV_T_FAELLIG,
							EingangsrechnungFac.FLR_ZV_N_ANGEWANDTERSKONTOSATZ,
							EingangsrechnungFac.FLR_ZV_N_ZAHLBETRAG,
							Facade.NICHT_SORTIERBAR, Facade.NICHT_SORTIERBAR,
							EingangsrechnungFac.FLR_ZV_B_BEZAHLEN,
							Facade.NICHT_SORTIERBAR, Facade.NICHT_SORTIERBAR,
							Facade.NICHT_SORTIERBAR }));
		}
		return super.getTableInfo();
	}
}
