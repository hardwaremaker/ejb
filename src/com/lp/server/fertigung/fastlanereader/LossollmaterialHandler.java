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
package com.lp.server.fertigung.fastlanereader;

import java.awt.Color;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.Icon;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelfehlmengeDto;
import com.lp.server.fertigung.fastlanereader.generated.FLRLosistmaterial;
import com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.system.service.LocaleFac;
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
 * Hier wird die FLR Funktionalitaet fuer das Losmaterial implementiert.
 * </p>
 * <p>
 * Copright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum 21.07.2005
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version 1.0
 */
public class LossollmaterialHandler extends UseCaseHandler {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String FLR_LOSMAT = "flrlossollmaterial.";
	private static final String FLR_LOSMAT_FROM_CLAUSE = " from FLRLossollmaterial flrlossollmaterial ";

	private boolean bFehlmengeStattPreis = false;

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int startIndex = Math.max(rowIndex.intValue() - (PAGE_SIZE / 2), 0);
			int endIndex = startIndex + PAGE_SIZE - 1;

			session = factory.openSession();
			String queryString = "SELECT flrlossollmaterial,(SELECT distinct s.artikel_i_id FROM FLRArtikelsperren as s WHERE s.artikel_i_id=flrlossollmaterial.flrartikel.i_id) as sperren from FLRLossollmaterial flrlossollmaterial "

					+ this.buildWhereClause() + this.buildOrderByClause();
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(PAGE_SIZE);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;
			int col = 0;
			while (resultListIterator.hasNext()) {
				Object[] o = (Object[]) resultListIterator.next();
				FLRLossollmaterial losmat = (FLRLossollmaterial) o[0];

				rows[row][col++] = losmat.getI_id();
				if (Helper.short2boolean(losmat.getB_nachtraeglich())) {
					rows[row][col++] = "N";
				} else {
					rows[row][col++] = "S";
				}
				rows[row][col++] = losmat.getFlrartikel().getC_nr();

				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(
						losmat.getFlrartikel().getI_id(), theClientDto);

				if (aDto.getArtikelsprDto() != null) {
					rows[row][col++] = aDto.getArtikelsprDto().getCBez();
					rows[row][col++] = aDto.getArtikelsprDto().getCZbez();
				} else {
					rows[row][col++] = null;
					rows[row][col++] = null;
				}

				rows[row][col++] = losmat.getN_menge();
				// erledigte Menge
				BigDecimal bdAusgegeben = new BigDecimal(0);
				for (Iterator<?> iter = losmat.getIstmaterialset().iterator(); iter
						.hasNext();) {
					FLRLosistmaterial item = (FLRLosistmaterial) iter.next();
					if (Helper.short2boolean(item.getB_abgang()) == true) {
						bdAusgegeben = bdAusgegeben.add(item.getN_menge());
					} else {
						bdAusgegeben = bdAusgegeben.subtract(item.getN_menge());
					}
				}
				rows[row][col++] = bdAusgegeben;

				ArtikelfehlmengeDto artikelfehlmengeDto = getFehlmengeFac()
						.artikelfehlmengeFindByBelegartCNrBelegartPositionIIdOhneExc(
								LocaleFac.BELEGART_LOS, losmat.getI_id());

				if (bFehlmengeStattPreis) {

					if (Helper.short2boolean(losmat.getB_nachtraeglich())
							&& artikelfehlmengeDto == null) {
						rows[row][col++] = null;
					} else {
						rows[row][col++] = losmat.getN_menge().subtract(
								bdAusgegeben);
					}

				} else {

					rows[row][col++] = losmat.getN_sollpreis();
				}

				if (artikelfehlmengeDto != null) {
					rows[row][col++] = "F";
				} else {
					rows[row][col++] = "";
				}
				if (o[1] != null) {
					rows[row][col++] = LocaleFac.STATUS_GESPERRT;
				} else {
					rows[row][col++] = null;
				}

				if (losmat.getLossollmaterial_i_id_original() != null) {
					rows[row][col++] = new Color(89, 188, 41);
				} else {
					rows[row][col++] = null;
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
					where.append(" " + FLR_LOSMAT + filterKriterien[i].kritName);
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
							orderBy.append(FLR_LOSMAT + kriterien[i].kritName);
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

				if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_ERSATZTYPENVERWALTUNG,
						theClientDto)) {
					orderBy.append("flrlossollmaterial.i_sort ASC, flrlossollmaterial.lossollmaterial_i_id_original ASC");
				} else {
					orderBy.append(FLR_LOSMAT)
							.append(FertigungFac.FLR_LOSSOLLMATERIAL_FLRARTIKEL
									+ ".c_nr")
							.append(" ASC , flrlossollmaterial.t_aendern ASC ");
				}

				sortAdded = true;
			}
			if (orderBy.indexOf(FLR_LOSMAT
					+ FertigungFac.FLR_LOSSOLLMATERIAL_FLRARTIKEL + ".c_nr") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" ")
						.append(FLR_LOSMAT)
						.append(FertigungFac.FLR_LOSSOLLMATERIAL_FLRARTIKEL
								+ ".c_nr").append(" ");
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
		return FLR_LOSMAT_FROM_CLAUSE;
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
				String queryString = "select " + FLR_LOSMAT
						+ FertigungFac.FLR_LOSSOLLMATERIAL_I_ID
						+ FLR_LOSMAT_FROM_CLAUSE + this.buildWhereClause()
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
								ParameterFac.PARAMETER_FEHLMENGE_STATT_PREIS_IN_LOSMATERIAL);
				bFehlmengeStattPreis = (java.lang.Boolean) parameter
						.getCWertAsObject();

			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}

			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();
			setTableInfo(new TableInfo(
					new Class[] { Integer.class, String.class, String.class,
							String.class, String.class, BigDecimal.class,
							BigDecimal.class, BigDecimal.class, String.class,
							Icon.class, Color.class },
					new String[] {
							"i_id",
							getTextRespectUISpr("lp.art", mandantCNr, locUI),
							getTextRespectUISpr("lp.artikelnummer", mandantCNr,
									locUI),
							getTextRespectUISpr("lp.bezeichnung", mandantCNr,
									locUI),
							getTextRespectUISpr("artikel.zusatzbez",
									mandantCNr, locUI),
							getTextRespectUISpr("lp.menge", mandantCNr, locUI),
							getTextRespectUISpr("fert.ausgegeben", mandantCNr,
									locUI),
							bFehlmengeStattPreis ? getTextRespectUISpr(
									"lp.fehlmenge", mandantCNr, locUI)
									: getTextRespectUISpr("lp.preis",
											mandantCNr, locUI), " ", "S", "" },
					new int[] { QueryParameters.FLR_BREITE_SHARE_WITH_REST, 3,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_PREIS,
							QueryParameters.FLR_BREITE_PREIS,
							QueryParameters.FLR_BREITE_PREIS, 1,
							QueryParameters.FLR_BREITE_S, 1 },
					new String[] {
							FertigungFac.FLR_LOSSOLLMATERIAL_I_ID,
							Facade.NICHT_SORTIERBAR,
							FertigungFac.FLR_LOSSOLLMATERIAL_FLRARTIKEL
									+ ".c_nr",
							Facade.NICHT_SORTIERBAR,
							Facade.NICHT_SORTIERBAR,
							FertigungFac.FLR_LOSSOLLMATERIAL_N_MENGE,
							Facade.NICHT_SORTIERBAR,
							bFehlmengeStattPreis ? Facade.NICHT_SORTIERBAR
									: FertigungFac.FLR_LOSSOLLMATERIAL_N_SOLLPREIS,
							Facade.NICHT_SORTIERBAR, Facade.NICHT_SORTIERBAR,
							"" }));
		}
		return super.getTableInfo();
	}
}
