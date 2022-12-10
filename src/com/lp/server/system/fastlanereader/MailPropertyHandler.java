package com.lp.server.system.fastlanereader;

import java.util.List;
import java.util.Locale;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.system.fastlanereader.generated.FLRMailProperty;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.UseCaseHandler;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

public class MailPropertyHandler extends UseCaseHandler {
	private static final long serialVersionUID = -5509950138201206209L;
	
	private static final String FLR_MAILPROPERTY = "flrmailproperty.";
	private static final String FLR_MAILPROPERTY_FROM_CLAUSE = " from FLRMailProperty flrmailproperty ";
	private static final String FLR_SORTQUERY = "select " + FLR_MAILPROPERTY + "c_nr" + FLR_MAILPROPERTY_FROM_CLAUSE;

	private String getFromClause() {
		return FLR_MAILPROPERTY_FROM_CLAUSE;
	}
	
	private String buildWhereClause() {
		return buildDefaultWhereClause(FLR_MAILPROPERTY);
	}

	private String buildOrderByClause() {
		if (getQuery() == null)
			return "";

		String orderByCnr = FLR_MAILPROPERTY + "c_nr";
		StringBuilder orderBy = new StringBuilder();
		SortierKriterium[] kriterien = getQuery().getSortKrit();
		boolean sortAdded = false;
		if (kriterien != null && kriterien.length > 0) {
			for (SortierKriterium krit : kriterien) {
				if (krit.isKrit) {
					if (sortAdded)
						orderBy.append(", ");
					
					sortAdded = true;
					orderBy.append(orderPart(FLR_MAILPROPERTY + krit.kritName, krit.value));
				}
			}
		} else {
			if (sortAdded) {
				orderBy.append(", ");
			}
			orderBy.append(orderPart(orderByCnr, "ASC"));
			sortAdded = true;
		}
		
		if (orderBy.indexOf(orderByCnr) < 0) {
			if (sortAdded) {
				orderBy.append(", ");
			}
			orderBy.append(orderPart(orderByCnr, "ASC"));
			sortAdded = true;
		}
		
		if (sortAdded) {
			orderBy.insert(0, " ORDER BY ");
		}
		return orderBy.toString();
	}
	
	private String orderPart(String column, String order) {
		return column + " " + (Helper.isStringEmpty(order) ? "" : (order + " "));
	}
	
	@Override
	protected long getRowCountFromDataBase() {
		String queryString = "select count(*) " + getFromClause() + buildWhereClause();
		return getRowCountFromDataBaseByQuery(queryString);
	}

	@Override
	public QueryResult sort(SortierKriterium[] sortierKriterien, Object selectedId) throws EJBExceptionLP {
		return defaultSort(sortierKriterien, selectedId, 
				FLR_SORTQUERY + buildWhereClause() + buildOrderByClause());
	}

	@Override
	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		
		try {
			int colCount = getTableInfo().getColumnClasses().length + 1;
			int pageSize = getLimit();
			int startIndex = getStartIndex(rowIndex, pageSize);
			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			session = setFilter(session);
			String queryString = this.getFromClause() + this.buildWhereClause()
					+ this.buildOrderByClause();
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<FLRMailProperty> resultList = query.list();

			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;
			int col = 0;
			for (FLRMailProperty mailProperty : resultList) {	
				rows[row][col++] = mailProperty.getC_nr();
				rows[row][col++] = mailProperty.getC_nr();
				rows[row++][col++] = mailProperty.getC_wert();
				
				col = 0;
			}
			result = new QueryResult(rows, getRowCount(), startIndex, endIndex, 0);
		} catch (HibernateException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			closeSession(session);
		}
		
		return result;
	}
	
	@Override
	public TableInfo getTableInfo() {
		if (super.getTableInfo() == null) {
			String mandantCnr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();
			setTableInfo(new TableInfo(
					new Class[] { String.class, String.class, String.class}, 
					new String[] {
							"c_nr",
							getTextRespectUISpr("system.mailproperty", mandantCnr, locUI),
							getTextRespectUISpr("lp.wert", mandantCnr, locUI) },
					new int[] {
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_XL },
					new String[] { "c_nr", "c_nr", "c_wert" }));
		}
		return super.getTableInfo();
	}
}
