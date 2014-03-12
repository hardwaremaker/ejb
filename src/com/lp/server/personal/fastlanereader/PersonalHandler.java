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
package com.lp.server.personal.fastlanereader;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodePersonal;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.SystemFac;
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
 * Hier wird die FLR Funktionalit&auml;t f&uuml;r den Personal implementiert. Pro UseCase
 * gibt es einen Handler.
 * </p>
 * <p>
 * Copright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum 2004-11-02
 * </p>
 * <p>
 * </p>
 * 
 * @author ck
 * @version 1.0
 */

public class PersonalHandler extends UseCaseHandler {

	/**
	 * the size of the data page returned in QueryResult.
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The information needed for the Personal table.
	 */

	private static final String fromClause = "from FLRPersonal as personal "
			+ " left join personal.flrpartner.flrlandplzort as flrlandplzort "
			+ " left join personal.flrpartner.flrlandplzort.flrort as flrort "
			+ " left join personal.flrpartner.flrlandplzort.flrland as flrland ";

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
				Object[] o = (Object[]) resultListIterator.next();
				FLRPersonal personal = (FLRPersonal) (o[0]);
				rows[row][col++] = personal.getI_id();
				rows[row][col++] = personal.getC_personalnummer();
				rows[row][col++] = personal.getC_ausweis();
				rows[row][col++] = personal.getFlrpartner()
						.getC_name1nachnamefirmazeile1();
				rows[row][col++] = personal.getFlrpartner()
						.getC_name2vornamefirmazeile2();
				rows[row][col++] = personal.getC_kurzzeichen();
				rows[row][col++] = personal.getFlrpartner().getFlrlandplzort() == null ? null
						: personal.getFlrpartner().getFlrlandplzort()
								.getFlrland().getC_lkz();
				rows[row][col++] = personal.getFlrpartner().getFlrlandplzort() == null ? null
						: personal.getFlrpartner().getFlrlandplzort()
								.getC_plz();
				rows[row++][col++] = personal.getFlrpartner()
						.getFlrlandplzort() == null ? null : personal
						.getFlrpartner().getFlrlandplzort().getFlrort()
						.getC_name();

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

	public long getRowCountFromDataBase() {
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
						where.append(" lower(personal."
								+ filterKriterien[i].kritName + ")");
					} else {
						where
								.append(" personal."
										+ filterKriterien[i].kritName);
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
						orderBy.append("personal." + kriterien[i].kritName);
						orderBy.append(" ");
						orderBy.append(kriterien[i].value);
					}
				}
			} else {
				// no sort criteria found, add default sort
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append("personal."
						+ PersonalFac.FLR_PERSONAL_FLRPARTNER + "."
						+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1
						+ " ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("personal.i_id") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" personal.i_id ");
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
		return fromClause;
	}

	public QueryResult sort(SortierKriterium[] sortierKriterien,
			Object selectedId) throws EJBExceptionLP {
		this.getQuery().setSortKrit(sortierKriterien);

		QueryResult result = null;
		int rowNumber = 0;

		if (selectedId != null && selectedId instanceof Integer
				&& ((Integer) selectedId).intValue() >= 0) {
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();

				String queryString = "select personal.i_id from FLRPersonal personal "
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
							String.class, String.class, String.class, String.class,
							String.class, String.class },
					new String[] {
							"Id",
							getTextRespectUISpr("lp.personalnr", mandantCNr,
									locUI),
							getTextRespectUISpr("lp.ausweis", mandantCNr, locUI),
							getTextRespectUISpr("lp.nachname", mandantCNr,
									locUI),
							getTextRespectUISpr("lp.vorname", mandantCNr, locUI),
							getTextRespectUISpr("pers.kurzzeichen", mandantCNr,
									locUI),
							getTextRespectUISpr("lp.lkz", mandantCNr, locUI),
							getTextRespectUISpr("lp.plz", mandantCNr, locUI),
							getTextRespectUISpr("lp.ort", mandantCNr, locUI) },
					new int[] {
							-1, // diese Spalte wird ausgeblendet
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M, -1,
							QueryParameters.FLR_BREITE_L,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_S,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M },
					new String[] {
							"i_id",
							PersonalFac.FLR_PERSONAL_C_PERSONALNUMMER,
							PersonalFac.FLR_PERSONAL_C_AUSWEIS,
							PersonalFac.FLR_PERSONAL_FLRPARTNER
									+ "."
									+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
							PersonalFac.FLR_PERSONAL_FLRPARTNER
									+ "."
									+ PartnerFac.FLR_PARTNER_NAME2VORNAMEFIRMAZEILE2,
							PersonalFac.FLR_PERSONAL_C_KURZZEICHEN,
							PersonalFac.FLR_PERSONAL_FLRPARTNER + "."
									+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT
									+ "." + SystemFac.FLR_LP_FLRLAND + "."
									+ SystemFac.FLR_LP_LANDLKZ,

							PersonalFac.FLR_PERSONAL_FLRPARTNER + "."
									+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT
									+ "." + SystemFac.FLR_LP_LANDPLZORTPLZ,
							PersonalFac.FLR_PERSONAL_FLRPARTNER + "."
									+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT
									+ "." + SystemFac.FLR_LP_FLRORT + "."
									+ SystemFac.FLR_LP_ORTNAME }));
		}

		return super.getTableInfo();
	}

	public static String getQueryPersonalliste(String mandant_c_nr) {
		return fromClause
				+ " WHERE personal.mandant_c_nr='"
				+ mandant_c_nr
				+ "' AND personal.b_versteckt=0 ORDER BY personal.flrpartner.c_name1nachnamefirmazeile1 ASC";
	}

	public PrintInfoDto getSDocPathAndPartner(Object key) {
		PersonalDto personalDto = null;
		PartnerDto partnerDto = null;
		try {
			personalDto = getPersonalFac().personalFindByPrimaryKey(
					(Integer) key, theClientDto);
			partnerDto = getPartnerFac().partnerFindByPrimaryKey(
					personalDto.getPartnerIId(), theClientDto);
			personalDto.setPartnerDto(partnerDto);
		} catch (Exception e) {
			// Nicht gefunden
		}
		if (partnerDto != null) {
//			String sPersonal = partnerDto.getCName1nachnamefirmazeile1()
//					.replace("/", ".");
//			if (partnerDto.getCName2vornamefirmazeile2() != null) {
//				sPersonal = sPersonal
//						+ " "
//						+ partnerDto.getCName2vornamefirmazeile2().replace("/",
//								".");
//			}
//			String sPath = JCRDocFac.HELIUMV_NODE + "/"
//					+ theClientDto.getMandant() + "/"
//					+ LocaleFac.BELEGART_PERSONAL.trim() + "/"
//					+ LocaleFac.BELEGART_PERSONAL.trim() + "/" + sPersonal;
			DocPath docPath = new DocPath(new DocNodePersonal(personalDto, partnerDto));
			return new PrintInfoDto(docPath, null, getSTable());
		} else {
			return null;
		}
	}

	public String getSTable() {
		return "PERSONAL";
	}
}
