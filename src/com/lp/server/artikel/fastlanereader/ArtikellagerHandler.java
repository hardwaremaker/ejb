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

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikellager;
import com.lp.server.artikel.fastlanereader.generated.service.WwArtikellagerPK;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.LagerFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.PartnerFac;
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
import com.lp.util.BigDecimal4;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * Hier wird die FLR Funktionalit&auml;t f&uuml;r den Artikelgruppen
 * implementiert. Pro UseCase gibt es einen Handler.
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
 * @author ck
 * @version 1.0
 */
public class ArtikellagerHandler extends UseCaseHandler {

	boolean bLagerminJeLager = false;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the size of the data page returned in QueryResult.
	 */

	/**
	 * The information needed for the kundes table.
	 */

	/**
	 * Konstruktor.
	 */
	public ArtikellagerHandler() {
	}

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			int colCount = this.getTableInfo().getColumnClasses().length;
			int pageSize = PAGE_SIZE;
			int startIndex = Math.max(rowIndex.intValue() - (pageSize / 2), 0);
			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			String queryString = getFromClause() + this.buildWhereClause() + this.buildOrderByClause();
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;
			int col = 0;

			// darf Preise sehen.
			final boolean bDarfPreiseSehen = getTheJudgeFac().hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_EINKAUF,
					theClientDto);

			while (resultListIterator.hasNext()) {
				FLRArtikellager artikellager = (FLRArtikellager) resultListIterator.next();

				ArtikelDto artikleDto = getArtikelFac()
						.artikelFindByPrimaryKey(artikellager.getCompId().getArtikel_i_id(), theClientDto);
				LagerDto lagerDto = getLagerFac().lagerFindByPrimaryKey(artikellager.getCompId().getLager_i_id());

				rows[row][col++] = artikellager.getCompId();
				rows[row][col++] = lagerDto.getCNr();
				rows[row][col++] = artikellager.getN_lagerstand();

				if (bDarfPreiseSehen) {
					rows[row][col++] = Helper.rundeKaufmaennisch(artikellager.getN_gestehungspreis(), 4);
				} else {
					rows[row][col++] = new BigDecimal(0);
				}

				double lagerwert = 0;
				lagerwert = artikellager.getN_lagerstand().doubleValue()
						* artikellager.getN_gestehungspreis().doubleValue();

				if (Helper.short2boolean(artikleDto.getBLagerbewertet()) == true && bDarfPreiseSehen) {
					rows[row][col++] = Helper.rundeKaufmaennisch(new java.math.BigDecimal(lagerwert), 4);
				} else {
					rows[row][col++] = Helper.rundeKaufmaennisch(new java.math.BigDecimal(0), 4);
				}

				if (bLagerminJeLager) {

					rows[row][col++] = artikellager.getF_lagermindest();
					rows[row][col++] = artikellager.getF_lagersoll();

					if (artikellager.getFlrlager().getParnter_i_id_standort() != null) {
						rows[row][col++] = artikellager.getFlrlager().getFlrpartner().getC_kbez();
					}

				}

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
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, he);
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
			String queryString = "select count(*) " + getFromClause() + this.buildWhereClause();
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
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, he);
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

			for (int i = 0; i < filterKriterien.length; i++) {
				if (filterKriterien[i].isKrit) {

					where.append(" " + booleanOperator);

					where.append(" artikellager." + filterKriterien[i].kritName);
					where.append(" " + filterKriterien[i].operator);
					where.append(" " + filterKriterien[i].value);
				}
			}

			where.insert(0, " WHERE (artikellager.n_lagerstand > 0 OR artikellager.flrlager.b_lagerstand_bei_0_anzeigen = 1) ");

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
							orderBy.append("artikellager." + kriterien[i].kritName);
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
				orderBy.append("artikellager.compId.lager_i_id DESC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("artikellager.compId.lager_i_id") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" artikellager.compId.lager_i_id ");
				sortAdded = true;
			}
			if (sortAdded) {
				orderBy.insert(0, " ORDER BY ");
			}
		}
		return orderBy.toString();
	}

	private static final String fromClause = "from FLRArtikellager artikellager ";

	/**
	 * get the basic from clause for the HQL statement.
	 * 
	 * @return the from clause.
	 */
	private static String getFromClause() {
		return fromClause;
	}

	public QueryResult sort(SortierKriterium[] sortierKriterien, Object selectedId) throws EJBExceptionLP {

		this.getQuery().setSortKrit(sortierKriterien);

		QueryResult result = null;
		int rowNumber = 0;

		if (selectedId != null /* && ((String) selectedId).length() > 0 */) {
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();
				String queryString = "select compId from FLRArtikellager artikellager " + this.buildWhereClause()
						+ this.buildOrderByClause();
				Query query = session.createQuery(queryString);
				ScrollableResults scrollableResult = query.scroll();
				if (scrollableResult != null) {
					scrollableResult.beforeFirst();
					while (scrollableResult.next()) {
						WwArtikellagerPK id = (WwArtikellagerPK) scrollableResult.get(0);

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
		if (super.getTableInfo() == null) {
			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();

			int iNachkommastellenPreis = 2;

			try {

				// SP797
				iNachkommastellenPreis = getMandantFac().getNachkommastellenPreisEK(theClientDto.getMandant());

				ParametermandantDto param = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_LAGERMIN_JE_LAGER);
				bLagerminJeLager = (Boolean) param.getCWertAsObject();
			} catch (RemoteException ex) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
			}

			if (bLagerminJeLager == false) {

				setTableInfo(new TableInfo(
						new Class[] { Object.class, String.class, BigDecimal.class,
								super.getUIClassBigDecimalNachkommastellen(iNachkommastellenPreis), BigDecimal.class },

						new String[] { "PK", getTextRespectUISpr("lp.lager", mandantCNr, locUI),
								getTextRespectUISpr("lp.lagerstand", mandantCNr, locUI),
								getTextRespectUISpr("lp.gestehungspreis", mandantCNr, locUI),
								getTextRespectUISpr("lp.lagerwert", mandantCNr, locUI) },

						new int[] { -1, // diese Spalte wird ausgeblendet
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST },

						new String[] { "compId", Facade.NICHT_SORTIERBAR, LagerFac.FLR_ARTIKELLAGER_N_LAGERSTAND,
								LagerFac.FLR_ARTIKELLAGER_N_GESTEHUNGSPREIS, Facade.NICHT_SORTIERBAR }));
			} else {
				setTableInfo(new TableInfo(
						new Class[] { Object.class, String.class, BigDecimal.class,
								super.getUIClassBigDecimalNachkommastellen(iNachkommastellenPreis), BigDecimal.class,
								Double.class, Double.class, String.class },

						new String[] { "PK", getTextRespectUISpr("lp.lager", mandantCNr, locUI),
								getTextRespectUISpr("lp.lagerstand", mandantCNr, locUI),
								getTextRespectUISpr("lp.gestehungspreis", mandantCNr, locUI),
								getTextRespectUISpr("lp.lagerwert", mandantCNr, locUI),
								getTextRespectUISpr("ww.lagermindest", mandantCNr, locUI),
								getTextRespectUISpr("ww.lagersoll", mandantCNr, locUI),
								getTextRespectUISpr("system.standort", mandantCNr, locUI) },

						new int[] { -1, // diese Spalte wird ausgeblendet
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST },

						new String[] { "compId", Facade.NICHT_SORTIERBAR, LagerFac.FLR_ARTIKELLAGER_N_LAGERSTAND,
								LagerFac.FLR_ARTIKELLAGER_N_GESTEHUNGSPREIS, Facade.NICHT_SORTIERBAR,
								LagerFac.FLR_ARTIKELLAGER_F_LAGERMINDEST, LagerFac.FLR_ARTIKELLAGER_F_LAGERSOLL,
								LagerFac.FLR_ARTIKELLAGER_FLRLAGER + ".flrpartner." + PartnerFac.FLR_PARTNER_C_KBEZ }));
			}
		}
		return super.getTableInfo();
	}

	public String getQueryZaehlliste(Integer lagerIId) {
		return getFromClause() + "WHERE artikellager." + LagerFac.FLR_ARTIKELLAGER_LAGER_I_ID + "=" + lagerIId;
	}
}
