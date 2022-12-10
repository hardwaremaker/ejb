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

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;

import javax.swing.Icon;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikelsperren;
import com.lp.server.artikel.service.SperrenIcon;
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

/**
 * <p>
 * Hier wird die FLR Funktionalit&auml;t f&uuml;r den Artikel implementiert. Pro
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
 * @author ck
 * @version 1.0
 */

public class KundenidentnummerHandler extends UseCaseHandler {
	
	boolean bTextsucheInklusiveArtikelnummer = false;
	boolean bTextsucheInklusiveIndexRevision = false;
	
	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {

		

		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;

		Iterator resultListIterator = null;
		List resultList = null;

		Object[][] rows = null;
		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = PAGE_SIZE;
			int startIndex = Math.max(rowIndex.intValue() - (pageSize / 2), 0);

			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			session = setFilter(session);

			String queryString = getFromClause() + buildWhereClause()
					+ buildOrderByClause();
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);

			resultList = query.list();
			resultListIterator = resultList.iterator();

			rows = new Object[resultList.size()][colCount];

			resultListIterator = resultList.iterator();

			int row = 0;

			while (resultListIterator.hasNext()) {
				Object o[] = (Object[]) resultListIterator.next();

				Object[] rowToAddCandidate = new Object[colCount];
				rowToAddCandidate[0] = o[0];
				rowToAddCandidate[1] = o[1];
				rowToAddCandidate[2] = o[2];
				rowToAddCandidate[3] = o[3];
				rowToAddCandidate[4] = o[4];
				rowToAddCandidate[5] = o[5];
				rowToAddCandidate[6] = o[6];
				
				
				String gesperrt = null;
				FLRArtikelsperren as = (FLRArtikelsperren) o[7];
				if ( as != null){
					gesperrt = as.getFlrsperren().getC_bez();
				}
				
				
				rowToAddCandidate[7] = gesperrt;

				rows[row] = rowToAddCandidate;
				row++;
			}
			result = new QueryResult(rows, getRowCount(), startIndex, endIndex,
					0);
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
			session = setFilter(session);

			String queryString = "SELECT count(kundesoko.i_id) FROM FLRKundesoko as kundesoko"
					+ " LEFT OUTER JOIN kundesoko.flrartikel AS artikelliste "
					+ " LEFT OUTER JOIN kundesoko.flrartikel.artikelsprset AS aspr "

					+ buildWhereClause();

			Query query = session.createQuery(queryString);
			List rowCountResult = query.list();
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
	 * builds the where clause of the HQL (Hibernate Query Language) statement
	 * using the current query.
	 * 
	 * @return the HQL where clause.
	 */
	private String buildWhereClause() {
		StringBuffer where = new StringBuffer(
				"WHERE artikelliste.i_id IS NOT NULL AND kundesoko.c_kundeartikelnummer IS NOT NULL ");

		if (this.getQuery() != null && this.getQuery().getFilterBlock() != null
				&& this.getQuery().getFilterBlock().filterKrit != null) {

			FilterBlock filterBlock = this.getQuery().getFilterBlock();
			FilterKriterium[] filterKriterien = this.getQuery()
					.getFilterBlock().filterKrit;
			String booleanOperator = filterBlock.boolOperator;

			for (int i = 0; i < filterKriterien.length; i++) {
				if (filterKriterien[i].isKrit) {
					where.append(" " + booleanOperator);

					if (filterKriterien[i].kritName.equals("c_volltext")) {
						String suchstring = "lower(coalesce(aspr.c_bez,'')||' '||coalesce(aspr.c_kbez,'')||' '||coalesce(aspr.c_zbez,'')||' '||coalesce(aspr.c_zbez2,''))";
						

						if (bTextsucheInklusiveArtikelnummer) {
							suchstring += "||' '||lower(artikelliste.c_nr)";
						}

						if (bTextsucheInklusiveIndexRevision) {
							suchstring += "||' '||lower(coalesce(artikelliste.c_index,''))||' '||lower(coalesce(artikelliste.c_revision,''))";
						}
						
						String[] teile = filterKriterien[i].value.toLowerCase()
								.split(" ");
						where.append("(");

						for (int p = 0; p < teile.length; p++) {

							if (teile[p].startsWith("-")) {
								where.append(" NOT ");

								teile[p] = teile[p].substring(1);

							}

							where.append("lower(" + suchstring + ") like '%"
									+ teile[p].toLowerCase() + "%'");
							if (p < teile.length - 1) {
								where.append(" AND ");
							}
						}

						where.append(")");
					} else {
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" upper("
									+ filterKriterien[i].kritName + ")");
						} else {
							where.append(" " + filterKriterien[i].kritName);
						}
						where.append(" " + filterKriterien[i].operator);
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" "
									+ filterKriterien[i].value.toUpperCase());
						} else {
							where.append(" " + filterKriterien[i].value);
						}
					}

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
		if (getQuery() != null) {
			SortierKriterium[] kriterien = getQuery().getSortKrit();
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
				orderBy.append("kundesoko.c_kundeartikelnummer ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("kundesoko.i_id") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" kundesoko.i_id ");
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
	private String getFromClause() throws Exception {

		return "SELECT kundesoko.i_id, kundesoko.c_kundeartikelnummer, artikelliste.c_nr, aspr.c_bez,aspr.c_zbez, aspr.c_zbez2, kundesoko.flrkunde.flrpartner.c_name1nachnamefirmazeile1, (SELECT s FROM FLRArtikelsperren as s WHERE s.artikel_i_id=artikelliste.i_id AND s.i_sort=1) as sperren FROM FLRKundesoko as kundesoko"
				+ " LEFT OUTER JOIN kundesoko.flrartikel AS artikelliste "
				+ " LEFT OUTER JOIN kundesoko.flrartikel.artikelsprset AS aspr ";

	}

	public Session setFilter(Session session) {
		session = super.setFilter(session);
		String sMandant = theClientDto.getMandant();
		session.enableFilter("filterMandant").setParameter("paramMandant",
				sMandant);
		return session;
	}

	public QueryResult sort(SortierKriterium[] sortierKriterien,
			Object selectedIdI) throws EJBExceptionLP {

		getQuery().setSortKrit(sortierKriterien);

		QueryResult result = null;
		int rowNumber = 0;
		ScrollableResults scrollableResult = null;
		if (selectedIdI instanceof Integer) {
			if (selectedIdI != null && ((Integer) selectedIdI).intValue() >= 0) {
				SessionFactory factory = FLRSessionFactory.getFactory();
				Session session = null;

				try {
					session = factory.openSession();
					session = setFilter(session);

					String queryString = null;
					try {
						queryString = getFromClause() + buildWhereClause()
								+ buildOrderByClause();
					} catch (Exception ex) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, ex);
					}

					Query query = session.createQuery(queryString);
					scrollableResult = query.scroll();

					if (scrollableResult != null) {
						scrollableResult.beforeFirst();
						while (scrollableResult.next()) {
							Integer id = (Integer) scrollableResult
									.getInteger(0);
							if (selectedIdI.equals(id)) {
								rowNumber = scrollableResult.getRowNumber();
								break;
							}
						}
					}
				} catch (HibernateException e) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
				} finally {
					try {
						session.close();
					} catch (HibernateException he) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, he);
					}
				}
			}
		}
		if (rowNumber < 0
		// || rowNumber >= getRowCount()
		) {
			rowNumber = 0;
		}

		result = getPageAt(new Integer(rowNumber));
		result.setIndexOfSelectedRow(rowNumber);

		return result;
	}

	public TableInfo getTableInfo() {
		if (super.getTableInfo() == null) {

			try {
				ParametermandantDto param = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_ARTIKEL,
								ParameterFac.PARAMETER_TEXTSUCHE_INKLUSIVE_ARTIKELNUMMER);

				bTextsucheInklusiveArtikelnummer=(Boolean)param.getCWertAsObject();
				
				param = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_ARTIKEL,
								ParameterFac.PARAMETER_TEXTSUCHE_INKLUSIVE_INDEX_REVISION);

				bTextsucheInklusiveIndexRevision=(Boolean)param.getCWertAsObject();
			} catch (RemoteException ex) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
			}

			setTableInfo(new TableInfo(new Class[] { Integer.class,
					String.class, String.class, String.class, String.class,
					String.class, String.class, SperrenIcon.class, },
					new String[] {
							"i_id",
							getTextRespectUISpr("lp.kundenartikelnummer",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("artikel.artikelnummerlang",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("bes.artikelbezeichnung",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("artikel.zusatzbez",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("artikel.zusatzbez2",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.firma",
									theClientDto.getMandant(),
									theClientDto.getLocUi()), 
							getTextRespectUISpr("artikel.sperre",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),  }, 
					new int[] {
							-1, // diese Spalte wird ausgeblendet
							QueryParameters.FLR_BREITE_L,
							QueryParameters.FLR_BREITE_L,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_L,
							QueryParameters.FLR_BREITE_S }, 
					new String[] {
							"i_id", "kundesoko.c_kundeartikelnummer",
							"artikelliste.c_nr", "aspr.c_bez", "aspr.c_zbez",
							"aspr.c_zbez2", Facade.NICHT_SORTIERBAR,
							Facade.NICHT_SORTIERBAR },
					new String[]{ null, null, null, null, null, null, null, 
							getTextRespectUISpr("artikel.sperre.tooltip", theClientDto.getMandant(), theClientDto.getLocUi()), 	
					}));

		}
		return super.getTableInfo();
	}

}
