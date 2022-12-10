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

import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikelgruppe;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelgruppespr;
import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.benutzer.fastlanereader.generated.FLRArtgrurolle;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeArtikelgruppe;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.MandantFac;
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
public class ArtikelgruEingeschraenktHandler extends UseCaseHandler {

	/**
	 * the size of the data page returned in QueryResult.
	 */

	/**
	 * The information needed for the kundes table.
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Konstruktor.
	 */
	public ArtikelgruEingeschraenktHandler() {
	}

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
			session = setFilter(session);

			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();

			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;
			int col = 0;

			String sLocUI = Helper.locale2String(theClientDto.getLocUi());

			
			while (resultListIterator.hasNext()) {
				Object o[] = (Object[]) resultListIterator.next();
				FLRArtgrurolle artgrurolle = (FLRArtgrurolle) o[0];

				FLRArtikelgruppe artikelgruppe = artgrurolle
						.getFlrartikelgruppe();

				Iterator<?> sprsetIterator = artikelgruppe
						.getArtikelgruppesprset().iterator();

				rows[row][col++] = artikelgruppe.getI_id();
				rows[row][col++] = artikelgruppe.getC_nr();
				rows[row][col++] = findSpr(sLocUI, sprsetIterator);

				rows[row][col++] = artikelgruppe.getFlrartikelgruppe() == null ? null
						: artikelgruppe.getFlrartikelgruppe().getC_nr();

				if (artikelgruppe.getFlrartikelgruppe() != null) {
					Iterator<?> sprsetIteratorV = artikelgruppe
							.getFlrartikelgruppe().getArtikelgruppesprset()
							.iterator();

					rows[row][col++] = findSpr(sLocUI, sprsetIteratorV);
				} else {
					rows[row][col++] = null;
				}

				FLRFinanzKonto flrkonto = artikelgruppe.getFlrkonto(theClientDto.getMandant());

				rows[row][col++] = flrkonto == null ? null : flrkonto.getC_nr();

				row++;
				col = 0;
			}
			result = new QueryResult(rows, this.getRowCount(), startIndex,
					endIndex, 0);
		} catch (HibernateException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			try {
				session.close();
			} catch (HibernateException he) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HIBERNATE, he);
			}
		}
		return result;
	}

	private String findSpr(String sLocaleI, Iterator<?> iterUebersetzungenI) {
		String sUebersetzung = null;
		while (iterUebersetzungenI.hasNext()) {
			FLRArtikelgruppespr bestellungartspr = (FLRArtikelgruppespr) iterUebersetzungenI
					.next();
			if (bestellungartspr.getLocale().getC_nr().compareTo(sLocaleI) == 0) {
				sUebersetzung = bestellungartspr.getC_bez();
				break;
			}
		}
		return sUebersetzung;
	}

	protected long getRowCountFromDataBase() {
		long rowCount = 0;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			session = setFilter(session);

			String queryString = "SELECT COUNT(*) FROM FLRArtgrurolle AS artgrurolle LEFT OUTER JOIN artgrurolle.flrartikelgruppe AS artikelgruppe"
					+ " LEFT JOIN artikelgruppe.artikelgruppesprset AS artikelgruppesprset "
					+ buildWhereClause();

			Query query = session.createQuery(queryString);
			List<?> rowCountResult = query.list();
			if (rowCountResult != null && rowCountResult.size() > 0) {
				rowCount = ((Long) rowCountResult.get(0)).longValue();
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
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
					if (filterKriterien[i].isBIgnoreCase()) {
						where.append(" lower(" + filterKriterien[i].kritName
								+ ")");
					} else {
						where.append(" " + filterKriterien[i].kritName);
					}
					where.append(" " + filterKriterien[i].operator);
					if (filterKriterien[i].isBIgnoreCase()) {
						where.append(" "
								+ filterKriterien[i].value.toLowerCase());
					} else {
						where.append(" " + filterKriterien[i].value);
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
			} else {
				// no sort criteria found, add default sort
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append("artikelgruppe.c_nr ASC ");

				sortAdded = true;
			}
			if (orderBy.indexOf("artikelgruppe.c_nr") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" artikelgruppe.c_nr ");
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
		return " FROM FLRArtgrurolle AS artgrurolle LEFT OUTER JOIN artgrurolle.flrartikelgruppe AS artikelgruppe"
				+ " LEFT OUTER JOIN artikelgruppe.artikelgruppesprset AS artikelgruppesprset "
				+ " LEFT OUTER JOIN artikelgruppe.flrartikelgruppe.artikelgruppesprset AS vatergruppesprset ";
	}

	public QueryResult sort(SortierKriterium[] sortierKriterien,
			Object selectedId) throws EJBExceptionLP {

		getQuery().setSortKrit(sortierKriterien);

		QueryResult result = null;
		int rowNumber = 0;

		if (selectedId != null) {
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();
				session = setFilter(session);
				String queryString = getFromClause() + buildWhereClause()
						+ buildOrderByClause();

				Query query = session.createQuery(queryString);
				ScrollableResults scrollableResult = query.scroll();
				if (scrollableResult != null) {
					scrollableResult.beforeFirst();
					while (scrollableResult.next()) {
						FLRArtgrurolle artgrurolle = (FLRArtgrurolle) scrollableResult
								.get(0);
						FLRArtikelgruppe artikelgruppe = artgrurolle
								.getFlrartikelgruppe();
						Integer iId = artikelgruppe.getI_id();
						if (selectedId.equals(iId)) {
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
			setTableInfo(new TableInfo(new Class[] { Integer.class,
					String.class, String.class, String.class, String.class,
					String.class },

					new String[] {
							"PK",
							getTextRespectUISpr("lp.kennung",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.bezeichnung",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.vatergruppe",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.bezeichnung",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.konto",
									theClientDto.getMandant(),
									theClientDto.getLocUi()) },

					new int[] {
							-1, // diese Spalte wird ausgeblendet
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST },

					new String[] { "artikelgruppe.i_id", "artikelgruppe.c_nr",
							"artikelgruppesprset.c_bez",
							"artikelgruppe.flrartikelgruppe.c_nr",
							"vatergruppesprset.c_bez",Facade.NICHT_SORTIERBAR }));
		}
		return super.getTableInfo();
	}

	@Override
	public PrintInfoDto getSDocPathAndPartner(Object key) {
		ArtgruDto gruppe = null;
		try {
			gruppe = getArtikelFac().artgruFindByPrimaryKey((Integer) key,
					theClientDto);
		} catch (Exception e) {
			// nicht gefunden
		}
		if (gruppe != null) {
			DocPath path = new DocPath(new DocNodeArtikelgruppe(gruppe));
			return new PrintInfoDto(path, null, getSTable());
		}
		return null;
	}

	@Override
	public String getSTable() {
		return "ARTIKELGRUPPE";
	}
}
