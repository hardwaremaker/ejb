package com.lp.server.angebotstkl.fastlanereader;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.angebotstkl.fastlanereader.generated.FLRWebFindChips;
import com.lp.server.angebotstkl.service.AngebotstklFac;
import com.lp.server.util.Facade;
import com.lp.server.util.HelperServer;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.UseCaseHandler;
import com.lp.server.util.fastlanereader.service.query.FilterBlock;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.EJBExceptionLP;

public class WebFindChipsHandler extends UseCaseHandler{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String FLR_FROM_CLAUSE = 
			" FROM FLRWebFindChips flrwebfindchips "
			+ " LEFT JOIN flrwebfindchips.flrwebpartner AS flrwebpartner "
			+ " LEFT OUTER JOIN flrwebpartner.flrlieferant AS flrlieferant ";

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {

		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int startIndex = Math.max(rowIndex.intValue() - (PAGE_SIZE / 2), 0);
			int endIndex = startIndex + PAGE_SIZE - 1;

			session = factory.openSession();
			String queryString = "select flrwebfindchips "
					+ this.getFromClause() + this.buildWhereClause()
					+ this.buildOrderByClause();
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(PAGE_SIZE);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;
			int col = 0;
			
			while (resultListIterator.hasNext()) {
				FLRWebFindChips flr = (FLRWebFindChips) resultListIterator.next();
				
				rows[row][col++] = flr.getWebpartner_i_id();
				rows[row][col++] = flr.getC_distributor();
				rows[row][col++] = flr.getC_name();
				rows[row][col++] = flr.getFlrwebpartner().getFlrlieferant() != null ? HelperServer.formatNameAusFLRPartner(
						flr.getFlrwebpartner().getFlrlieferant().getFlrpartner()) : "";

				row++;
				col = 0;
			}
			result = new QueryResult(rows, this.getRowCount(), startIndex, endIndex, 0);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			try {
				if (session != null) session.close();
			} catch (HibernateException he) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, he);
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
			String queryString = "select count(*) " + this.getFromClause() + this.buildWhereClause();
			Query query = session.createQuery(queryString);
			List<?> rowCountResult = query.list();
			if (rowCountResult != null && rowCountResult.size() > 0) {
				rowCount = ((Long) rowCountResult.get(0)).longValue();
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
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
					
					if (AngebotstklFac.FLR_WEBPARTNER_KRIT_MANDANT_MIT_LIEFERANT_NULL.equals(filterKriterien[i].kritName)) {
						where.append(" (flrwebpartner.lieferant_i_id IS NULL OR");
						where.append(" flrlieferant.mandant_c_nr");
						where.append(" " + filterKriterien[i].operator);
						where.append(" '" + filterKriterien[i].value + "')");
					} else if (AngebotstklFac.FLR_WEBPARTNER_KRIT_MANDANT_OHNE_LIEFERANT_NULL.equals(filterKriterien[i].kritName)) {
						where.append(" flrwebpartner.lieferant_i_id IS NOT NULL AND");
						where.append(" flrlieferant.mandant_c_nr");
						where.append(" " + filterKriterien[i].operator);
						where.append(" '" + filterKriterien[i].value + "'");
					} else {
						where.append(" flrwebfindchips." + filterKriterien[i].kritName);
						where.append(" " + filterKriterien[i].operator);
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
					if (!kriterien[i].kritName
							.endsWith(Facade.NICHT_SORTIERBAR)) {
						if (kriterien[i].isKrit) {
							if (sortAdded) {
								orderBy.append(", ");
							}
							sortAdded = true;
							orderBy.append(" "+ kriterien[i].kritName);
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
				orderBy.append(" flrwebfindchips.c_distributor ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf(" flrwebfindchips.webpartner_i_id") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" ").append(" flrwebfindchips.webpartner_i_id").append(" ");
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
		return FLR_FROM_CLAUSE;
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
				String queryString = "select flrwebfindchips.webpartner_i_id "
						+ FLR_FROM_CLAUSE + this.buildWhereClause()
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
				try {
					if (session != null) session.close();
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
					new Class[] { Integer.class, String.class, String.class, String.class }, 
					new String[] {
						"i_id",
						getTextRespectUISpr("agstkl.webdistributor.id", mandantCNr, locUI),
						getTextRespectUISpr("agstkl.webdistributor.name", mandantCNr, locUI),
						getTextRespectUISpr("bes.lieferant", mandantCNr, locUI)}, 
					new int[] {
						QueryParameters.FLR_BREITE_SHARE_WITH_REST,
						QueryParameters.FLR_BREITE_SHARE_WITH_REST,
						QueryParameters.FLR_BREITE_SHARE_WITH_REST,
						QueryParameters.FLR_BREITE_SHARE_WITH_REST }, 
					new String[] { "i_id",
						"flrwebfindchips.c_distributor",
						"flrwebfindchips.c_name",
						"flrlieferant.flrpartner.c_name1nachnamefirmazeile1"}));
		}
		return super.getTableInfo();
	}

}
