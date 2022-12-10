package com.lp.server.finanz.fastlanereader;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.finanz.fastlanereader.generated.FLRReversechargeart;
import com.lp.server.finanz.fastlanereader.generated.FLRReversechargeartspr;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.UseCaseHandler;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

public class ReversechargeartHandler extends UseCaseHandler {
	private static final long serialVersionUID = -1733375478764396690L;
	private static final String FLR_REVERSECHARGEART = "flrreversechargeart.";
	private static final String FLR_REVERSECHARGEART_FROM_CLAUSE = " from FLRReversechargeart flrreversechargeart LEFT JOIN flrreversechargeart.reversechargeartsprset as reversechargeartsprset ";
	private static final String FLR_SORTQUERY = "select " + FLR_REVERSECHARGEART + "i_id" + FLR_REVERSECHARGEART_FROM_CLAUSE ;

	@Override
	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = getLimit() ;
			int startIndex = getStartIndex(rowIndex, pageSize) ;
			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			String queryString = getFromClause() + buildWhereClause()
					+ buildOrderByClause();
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

				FLRReversechargeart reversechargeart = (FLRReversechargeart) o[0] ;
				Iterator<?> sprsetIterator = reversechargeart
						.getReversechargeartsprset().iterator();
				
				rows[row][col++] = reversechargeart.getI_id();
				rows[row][col++] = reversechargeart.getC_nr();
				rows[row][col++] = findSpr(sLocUI, sprsetIterator) ;  
				row++;
				col = 0;
			}
			result = new QueryResult(rows, this.getRowCount(), startIndex,
					endIndex, 0);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			sessionClose(session);  
		}
		return result;
	}

	private String findSpr(String sLocaleI, Iterator<?> iterUebersetzungenI) {
		String sUebersetzung = null;
		while (iterUebersetzungenI.hasNext()) {
			FLRReversechargeartspr reversechargeartspr = (FLRReversechargeartspr) iterUebersetzungenI
					.next();
			if (reversechargeartspr.getLocale().getC_nr().compareTo(sLocaleI) == 0) {
				sUebersetzung = reversechargeartspr.getC_bez();
				break;
			}
		}
		return sUebersetzung;
	}
	
	@Override
	protected Session setFilter(Session session) {
		session = super.setFilter(session);
		session.enableFilter("filterHidden").setParameter("paramHidden", new Short("0"));
		
		return session;
	}
	
	@Override
	protected long getRowCountFromDataBase() {
		String queryString = "select count(*) " + getFromClause() + buildWhereClause() ;
		return getRowCountFromDataBaseByQuery(queryString);
	}

	@Override
	public QueryResult sort(SortierKriterium[] sortierKriterien,
			Object selectedId) throws EJBExceptionLP {
		return defaultSort(sortierKriterien, selectedId, FLR_SORTQUERY + buildWhereClause() + buildOrderByClause()) ;
	}

	
	private String buildWhereClause() {
		return buildDefaultWhereClause(FLR_REVERSECHARGEART) ;
	}
	
	private String getFromClause() {
		return FLR_REVERSECHARGEART_FROM_CLAUSE ;
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
						orderBy.append(FLR_REVERSECHARGEART
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
				orderBy.append(FLR_REVERSECHARGEART)
						.append("i_sort").append(" ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf(FLR_REVERSECHARGEART
					+ "i_sort") < 0) {
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" ").append(FLR_REVERSECHARGEART)
						.append("i_sort").append(" ");
				sortAdded = true;
			}
			if (sortAdded) {
				orderBy.insert(0, " ORDER BY ");
			}
		}
		return orderBy.toString();		
	}
	
	public TableInfo getTableInfo() {
		if (super.getTableInfo() == null) {
			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();
			setTableInfo(new TableInfo(
					new Class[] { Integer.class, String.class, String.class},
					new String[] {
							"i_id",
							getTextRespectUISpr("lp.kennung", mandantCNr, locUI),
							getTextRespectUISpr("lp.bezeichnung", mandantCNr,
									locUI) },
					new int[] { QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST},
					new String[] { "i_id", "c_nr", "c_bez" }));
		}
		
		return super.getTableInfo();
	}
}


