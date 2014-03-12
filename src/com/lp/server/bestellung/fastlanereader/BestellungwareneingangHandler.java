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
package com.lp.server.bestellung.fastlanereader;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.fastlanereader.generated.FLRWareneingang;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.bestellung.service.WareneingangFac;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeWareneingang;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.UseCaseHandler;
import com.lp.server.util.fastlanereader.service.query.FilterBlock;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.EJBExceptionLP;

public class BestellungwareneingangHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
			int pageSize = BestellungwareneingangHandler.PAGE_SIZE;
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
			// darf Preise sehen.
			final boolean bDarfPreiseSehen = getTheJudgeFac().hatRecht(
					RechteFac.RECHT_LP_DARF_PREISE_SEHEN_EINKAUF, theClientDto);

			while (resultListIterator.hasNext()) {
				FLRWareneingang wareneingang = (FLRWareneingang) resultListIterator
						.next();
				rows[row][col++] = wareneingang.getI_id();
				rows[row][col++] = wareneingang.getC_lieferscheinnr();
				rows[row][col++] = wareneingang.getT_lieferscheindatum();
				if (bDarfPreiseSehen) {
					BigDecimal bdTransportkosten = new BigDecimal(0);
					if (wareneingang.getN_transportkosten() != null) {
						bdTransportkosten = bdTransportkosten.add(wareneingang
								.getN_transportkosten());
					}
					if (wareneingang.getN_bankspesen() != null) {
						bdTransportkosten = bdTransportkosten.add(wareneingang
								.getN_bankspesen());
					}
					if (wareneingang.getN_sonstigespesen() != null) {
						bdTransportkosten = bdTransportkosten.add(wareneingang
								.getN_sonstigespesen());
					}
					if (wareneingang.getN_zollkosten() != null) {
						bdTransportkosten = bdTransportkosten.add(wareneingang
								.getN_zollkosten());
					}

					rows[row][col++] = bdTransportkosten;
					rows[row][col++] = wareneingang.getF_gemeinkostenfaktor();
				} else {
					rows[row][col++] = null;
					rows[row][col++] = null;
				}
				rows[row][col++] = wareneingang.getT_wareneingansdatum();
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
					where.append(" wareneingang." + filterKriterien[i].kritName);
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
						orderBy.append("wareneingang." + kriterien[i].kritName);
						orderBy.append(" ");
						orderBy.append(kriterien[i].value);
					}
				}
			} else {
				// no sort criteria found, add default sort
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append("wareneingang.")
						.append(WareneingangFac.FLR_WE_I_SORT) // vorher wurde
																// nach i_id
						// geordnet
						.append(" ASC ");
				sortAdded = true;
			}
			if (orderBy
					.indexOf("wareneingang." + WareneingangFac.FLR_WE_I_SORT) < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" wareneingang.")
						.append(WareneingangFac.FLR_WE_I_SORT).append(" ");
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
		return "from FLRWareneingang wareneingang ";
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
				String queryString = "select wareneingang." + "i_id"
						+ " from FLRWareneingang wareneingang "
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
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
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
	 * gets information about the Bestellungstable.
	 * 
	 * @return TableInfo
	 */
	public TableInfo getTableInfo() {
		if (super.getTableInfo() == null) {
			setTableInfo(new TableInfo(new Class[] { Integer.class,
					String.class, java.util.Date.class, BigDecimal.class,
					Double.class, java.util.Date.class,

			},
					new String[] {
							"i_id",
							getTextRespectUISpr("bes.lieferscheinnr",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("bes.lieferscheindatum",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("bes.transportkosten",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("bes.gemeinkostenfaktor",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("bes.wedatum",
									theClientDto.getMandant(),
									theClientDto.getLocUi()) }, new int[] {
							-1, // id
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_XM,
							QueryParameters.FLR_BREITE_PREIS,
							QueryParameters.FLR_BREITE_PREIS,
							QueryParameters.FLR_BREITE_XM, }, new String[] {
							"i_id", WareneingangFac.FLR_WE_C_LIEFERSCHEIN,
							WareneingangFac.FLR_WE_T_LIEFERSCHEINDATUM,
							WareneingangFac.FLR_WE_N_TRANSPORTKOSTEN,
							WareneingangFac.FLR_WE_F_GEMEINKOSTENFAKTOR,
							WareneingangFac.FLR_WE_T_WARENEINGANGSDATUM }));

		}

		return super.getTableInfo();
	}

	public PrintInfoDto getSDocPathAndPartner(Object key) {
		WareneingangDto weDto = null;
		BestellungDto bestDto = null;
		LieferantDto lieferantDto = null;
		PartnerDto partnerDto = null;
		try {
			weDto = getWareneingangFac().wareneingangFindByPrimaryKey(
					(Integer) key);
			bestDto = getBestellungFac().bestellungFindByPrimaryKey(
					weDto.getBestellungIId());
			lieferantDto = getLieferantFac().lieferantFindByPrimaryKey(
					bestDto.getLieferantIIdBestelladresse(), theClientDto);
			if (lieferantDto != null) {
				partnerDto = getPartnerFac().partnerFindByPrimaryKey(
						lieferantDto.getPartnerIId(), theClientDto);
			}
		} catch (Exception e) {
			// Nicht gefunden
		}
		if (bestDto != null && weDto != null) {
			// String sPath = JCRDocFac.HELIUMV_NODE + "/"
			// + theClientDto.getMandant() + "/"
			// + LocaleFac.BELEGART_BESTELLUNG.trim() + "/"
			// + bestDto.getBelegartCNr().trim() + "/"
			// + bestDto.getCNr().replace("/", ".") + "/"
			// + "Wareneingaenge/" + "Wareneingang " + weDto.getIId();
			DocPath docPath = new DocPath(new DocNodeWareneingang(weDto,
					bestDto));
			Integer sPartnerIId = null;
			if (partnerDto != null) {
				sPartnerIId = partnerDto.getIId();
			}
			return new PrintInfoDto(docPath, sPartnerIId, getSTable());
		} else {
			// return new Object[] { "", "" };
			return null;
		}
	}

	public String getSTable() {
		return "WARENEINGANG";
	}
}
