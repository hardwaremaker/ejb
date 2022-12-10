/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2016 HELIUM V IT-Solutions GmbH
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
package com.lp.server.finanz.fastlanereader;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.Icon;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.finanz.fastlanereader.generated.FLRSepakontoauszug;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.UseCaseHandler;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.EJBExceptionLP;

public class SepakontoauszugHandler extends UseCaseHandler {
	private static final long serialVersionUID = 365520663826783068L;

	private static final String FLR_SEPAKONTOAUSZUG = "flrsepakontoauszug.";
	private static final String FLR_SEPAKONTOAUSZUG_FROM_CLAUSE = " from FLRSepakontoauszug flrsepakontoauszug ";
	private static final String FLR_SORTQUERY = "select " + FLR_SEPAKONTOAUSZUG + "i_id" + FLR_SEPAKONTOAUSZUG_FROM_CLAUSE;
	
	@Override
	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		
		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = getLimit();
			int startIndex = getStartIndex(rowIndex, pageSize);
			int endIndex = startIndex + pageSize - 1;
			
			session = factory.openSession();
			String queryString = getFromClause() + buildWhereClause() + buildOrderByClause();
			Query query = session.createQuery(queryString);
			session = setFilter(session);
			
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;
			int col = 0;
			
			while (resultListIterator.hasNext()) {
				FLRSepakontoauszug sepakontoauszug = (FLRSepakontoauszug) resultListIterator.next();
				
				rows[row][col++] = sepakontoauszug.getI_id();
				rows[row][col++] = sepakontoauszug.getI_auszug();
				rows[row][col++] = sepakontoauszug.getT_auszug();
				rows[row][col++] = sepakontoauszug.getC_camt_format();
				rows[row][col++] = sepakontoauszug.getN_anfangssaldo();
				rows[row][col++] = sepakontoauszug.getN_endsaldo();
				rows[row][col++] = sepakontoauszug.getT_anlegen();
				rows[row][col++] = sepakontoauszug.getT_verbuchen();
				rows[row][col++] = getStatusMitUebersetzung(sepakontoauszug.getStatus_c_nr());
				row++;
				col = 0;
			}
			
			result = new QueryResult(rows, this.getRowCount(), startIndex, endIndex, 0);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			sessionClose(session);  
		}
		return result;
	}

	private String buildOrderByClause() {
		StringBuffer orderBy = new StringBuffer("");
		if (getQuery() != null) {
			SortierKriterium[] kriterien = getQuery().getSortKrit();
			boolean sortAdded = false;
			if (kriterien != null && kriterien.length > 0) {
				for (int i = 0; i < kriterien.length; i++) {
					if (kriterien[i].isKrit) {
						if (sortAdded) {
							orderBy.append(", ");
						}
						sortAdded = true;
						orderBy.append(FLR_SEPAKONTOAUSZUG
								+ kriterien[i].kritName);
						orderBy.append(" ");
						orderBy.append(kriterien[i].value);
					}
				}
			} else {
				// no sort criteria found, add default sort
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(FLR_SEPAKONTOAUSZUG)
						.append("i_auszug").append(" ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf(FLR_SEPAKONTOAUSZUG
					+ "i_auszug") < 0) {
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" ").append(FLR_SEPAKONTOAUSZUG)
						.append("i_auszug").append(" ");
				sortAdded = true;
			}
			if (sortAdded) {
				orderBy.insert(0, " ORDER BY ");
			}
		}
		return orderBy.toString();		
	}

	private String getFromClause() {
		return FLR_SEPAKONTOAUSZUG_FROM_CLAUSE;
	}

	private String buildWhereClause() {
		return buildDefaultWhereClause(FLR_SEPAKONTOAUSZUG);
	}

	@Override
	protected long getRowCountFromDataBase() {
		String queryString = "select count(*) " + getFromClause() + buildWhereClause();
		return getRowCountFromDataBaseByQuery(queryString);
	}

	@Override
	public QueryResult sort(SortierKriterium[] sortierKriterien,
			Object selectedId) throws EJBExceptionLP {
		return defaultSort(sortierKriterien, selectedId, FLR_SORTQUERY + buildWhereClause() + buildOrderByClause());
	}

	public TableInfo getTableInfo() {
		if (super.getTableInfo() == null) {
			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();
			setTableInfo(new TableInfo(
					new Class[] { Integer.class, Integer.class, Date.class, String.class, BigDecimal.class, 
							BigDecimal.class, Date.class, Date.class, Icon.class},
					new String[] {
							"i_id",
							getTextRespectUISpr("fb.auszug", mandantCNr, locUI),
							getTextRespectUISpr("lp.datum", mandantCNr, locUI),
							getTextRespectUISpr("fb.camtformat", mandantCNr, locUI),
							getTextRespectUISpr("fb.anfangssaldo", mandantCNr, locUI),
							getTextRespectUISpr("fb.endsaldo", mandantCNr, locUI),
							getTextRespectUISpr("fb.importdatum", mandantCNr, locUI),
							getTextRespectUISpr("fb.verbuchungsdatum", mandantCNr, locUI),
							getTextRespectUISpr("lp.status", mandantCNr, locUI) },
					new int[] { QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_L,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_PREIS,
							QueryParameters.FLR_BREITE_PREIS,
							QueryParameters.FLR_BREITE_L,
							QueryParameters.FLR_BREITE_L,
							QueryParameters.FLR_BREITE_XS},
					new String[] { "i_id", "i_auszug", "t_auszug", "c_camt_format", Facade.NICHT_SORTIERBAR, Facade.NICHT_SORTIERBAR, 
							"t_anlegen", "t_verbuchen", "status_c_nr" }));
		}
		
		return super.getTableInfo();
	}
}
