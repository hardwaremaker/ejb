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
package com.lp.server.artikel.fastlanereader;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.artikel.fastlanereader.generated.FLRFehlmenge;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.LagerFac;
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
public class LagercockpitFehlmengeHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String FLR_FEHLMENGE = "flrfehlmenge.";
	private static final String FLR_FEHLMENGE_FROM_CLAUSE = "SELECT flrfehlmenge,(SELECT distinct s.artikel_i_id FROM FLRArtikelsperren as s WHERE s.artikel_i_id=flrfehlmenge.flrartikel.i_id) as sperren from FLRFehlmenge flrfehlmenge LEFT OUTER JOIN flrfehlmenge.flrlossollmaterial.flrlos.flrstueckliste AS stkl ";

	private Integer artikelIId = null;

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {

		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = 9999999;
			int startIndex = Math.max(rowIndex.intValue() - (pageSize / 2), 0);
			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			String queryString = this.getFromClause() + this.buildWhereClause()
					+ this.buildOrderByClause();

			// Lagerstand
			BigDecimal lagerstand = null;
			HashMap<String, BigDecimal> hmLagerstaende = getLagerFac()
					.getLagerstaendeAllerLagerartenOhneKeinLager(artikelIId,
							theClientDto);
			lagerstand = hmLagerstaende.get(LagerFac.LAGERART_WARENEINGANG);
			if (lagerstand == null) {
				lagerstand = new BigDecimal(0);
			}

			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			ArrayList alZeilen = new ArrayList();

			int row = 0;
			int col = 0;
			while (resultListIterator.hasNext()) {
				Object[] o = (Object[]) resultListIterator.next();
				FLRFehlmenge fm = (FLRFehlmenge) o[0];

				Object[] zeile = new Object[colCount];
				zeile[0] = fm.getI_id();
				zeile[1] = fm.getFlrartikel().getC_nr();
				zeile[2] = getArtikelFac().artikelFindByPrimaryKeySmall(
						fm.getFlrartikel().getI_id(), theClientDto)
						.formatBezeichnung();
				zeile[3] = fm.getN_menge();

				lagerstand = lagerstand.subtract(fm.getN_menge());

				String losnummerProjekt = fm.getFlrlossollmaterial()
						.getFlrlos().getC_nr();
				if (fm.getFlrlossollmaterial().getFlrlos().getC_projekt() != null) {
					losnummerProjekt += " "
							+ fm.getFlrlossollmaterial().getFlrlos()
									.getC_projekt();
				}

				zeile[4] = losnummerProjekt;

				Integer lagerIId = null;
				LagerDto lDto = getLagerFac()
						.getLagerDesErstenArtikellagerplatzes(
								fm.getFlrartikel().getI_id(), theClientDto);
				if (lDto != null) {
					lagerIId = lDto.getIId();
				}

				String sLagerplaetze = getLagerFac()
						.getLagerplaezteEinesArtikels(
								fm.getFlrartikel().getI_id(), null);

				boolean bMehrerLagerplaetze = false;

				if (sLagerplaetze != null
						&& sLagerplaetze.split(",").length > 2) {
					bMehrerLagerplaetze = true;
				}
				if (bMehrerLagerplaetze == true && lagerIId != null) {
					zeile[5] = getLagerFac().getLagerplaezteEinesArtikels(
							fm.getFlrartikel().getI_id(), lagerIId)
							+ " ++";
				} else {
					zeile[5] = getLagerFac().getLagerplaezteEinesArtikels(
							fm.getFlrartikel().getI_id(), lagerIId);

				}

				alZeilen.add(zeile);
			}

			if (lagerstand.doubleValue() > 0) {

				// Noch 2 Zeilen hinzufuegen
				Object[] zeile = new Object[colCount];
				zeile[0] = -1;
				zeile[1] = "---------";
				zeile[2] = "----------------------------------------------------------------------";
				alZeilen.add(zeile);

				zeile = new Object[5];

				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(
						artikelIId, theClientDto);

				zeile[0] = -1;
				zeile[1] = aDto.getCNr();
				zeile[2] = "Verbleibende Menge:";
				zeile[3] = lagerstand;
				alZeilen.add(zeile);
			}

			Object[][] rows = new Object[alZeilen.size()][colCount];
			rows = (Object[][]) alZeilen.toArray(rows);

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
			String queryString = "SELECT flrfehlmenge from FLRFehlmenge flrfehlmenge LEFT OUTER JOIN flrfehlmenge.flrlossollmaterial.flrlos.flrstueckliste AS stkl "
					+ this.buildWhereClause();

			// Lagerstand
			BigDecimal lagerstand = null;
			HashMap<String, BigDecimal> hmLagerstaende = getLagerFac()
					.getLagerstaendeAllerLagerartenOhneKeinLager(artikelIId,
							theClientDto);
			lagerstand = hmLagerstaende.get(LagerFac.LAGERART_WARENEINGANG);
			if (lagerstand == null) {
				lagerstand = new BigDecimal(0);
			}

			Query query = session.createQuery(queryString);
			List<?> rowCountResult = query.list();

			Iterator<?> resultListIterator = rowCountResult.iterator();
			rowCount = rowCountResult.size();

			while (resultListIterator.hasNext()) {
				FLRFehlmenge fLRFehlmenge = (FLRFehlmenge) resultListIterator
						.next();
				lagerstand = lagerstand.subtract(fLRFehlmenge.getN_menge());
			}

			if (lagerstand.doubleValue() > 0) {
				rowCount = rowCount + 2;
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

				if (filterKriterien[i].kritName.equals("artikel_i_id")) {
					artikelIId = new Integer(filterKriterien[i].value);
				}

				if (filterKriterien[i].isKrit) {
					if (filterAdded) {
						where.append(" " + booleanOperator);
					}
					filterAdded = true;
					where.append(" " + FLR_FEHLMENGE
							+ filterKriterien[i].kritName);
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
				orderBy.append(FLR_FEHLMENGE)
						.append(ArtikelFac.FLR_FEHLMENGE_I_ID).append(" DESC ");
				sortAdded = true;
			}
			if (orderBy.indexOf(FLR_FEHLMENGE + ArtikelFac.FLR_FEHLMENGE_I_ID) < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" ").append(FLR_FEHLMENGE)
						.append(ArtikelFac.FLR_FEHLMENGE_I_ID).append(" ");
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

		if (selectedId != null && ((Integer) selectedId).intValue() >= 0) {
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();
				String queryString = "select "
						+ FLR_FEHLMENGE
						+ ArtikelFac.FLR_FEHLMENGE_I_ID
						+ " from FLRFehlmenge flrfehlmenge LEFT OUTER JOIN flrfehlmenge.flrlossollmaterial.flrlos.flrstueckliste AS stkl"
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

	public TableInfo getTableInfo() {
		if (super.getTableInfo() == null) {
			try {
				String mandantCNr = theClientDto.getMandant();
				int iNachkommastellenMenge = getMandantFac()
						.getNachkommastellenMenge(mandantCNr);

				Locale locUI = theClientDto.getLocUi();
				setTableInfo(new TableInfo(
						new Class[] {
								Integer.class,
								String.class,
								String.class,
								super.getUIClassBigDecimalNachkommastellen(iNachkommastellenMenge),
								String.class, String.class },

						new String[] {
								"i_id",

								getTextRespectUISpr("lp.artikel", mandantCNr,
										locUI),
								getTextRespectUISpr("lp.bezeichnung",
										mandantCNr, locUI),
								getTextRespectUISpr("lp.menge", mandantCNr,
										locUI),
								getTextRespectUISpr("lp.losnr", mandantCNr,
										locUI),
								getTextRespectUISpr("lp.lagerplatz",
										mandantCNr, locUI), },
						new int[] { QueryParameters.FLR_BREITE_SHARE_WITH_REST,

						QueryParameters.FLR_BREITE_M,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_M,
								QueryParameters.FLR_BREITE_L,
								QueryParameters.FLR_BREITE_L },
						new String[] {
								FLR_FEHLMENGE + ArtikelFac.FLR_FEHLMENGE_I_ID,

								FLR_FEHLMENGE
										+ ArtikelFac.FLR_FEHLMENGE_FLRARTIKEL
										+ ".c_nr",
								Facade.NICHT_SORTIERBAR,
								FLR_FEHLMENGE
										+ ArtikelFac.FLR_FEHLMENGE_N_MENGE,
								FLR_FEHLMENGE
										+ ArtikelFac.FLR_FEHLMENGE_C_BELEGARTNR,

								Facade.NICHT_SORTIERBAR }));
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
				return null;
			}
		}
		return super.getTableInfo();
	}
}
