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
package com.lp.server.system.fastlanereader;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.system.fastlanereader.generated.FLRTheClient;
import com.lp.server.system.service.TheClientFac;
import com.lp.server.system.service.TheClientLoggedInDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.UseCaseHandler;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

public class TheConcurrentUserHandler extends UseCaseHandler {
	private static final long serialVersionUID = -1758529995795777295L;
	private static final boolean modeHibernate = false;
	
	private QueryResult getPageAt(Integer rowIndex, List<TheClientLoggedInDto> users) {
		QueryResult result = null;
		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = 1000;
			int startIndex = getStartIndex(rowIndex, pageSize);
			int endIndex = startIndex + pageSize - 1;
			
			Object[][] rows = new Object[users.size()][colCount];
			int row = 0;
			int col = 0;
			for (TheClientLoggedInDto user : users) {
				rows[row][col++] = user.getIDUser();
				rows[row][col++] = user.getBenutzername().trim();
				rows[row][col++] = user.getMandant();
				rows[row][col++] = user.getSystemrolleCBez();
				rows[row][col++] = user.getDLoggedin();
				rows[row][col++] = user.getConcurrentUserCount();
				row++;
				col = 0;				
			}

			result = new QueryResult(rows, this.getRowCount(), startIndex,
					endIndex, 0);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		}
		
		return result;
	}
	
	public QueryResult getPageAt(Integer rowIndex) {
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

			Query query = null;
			if(modeHibernate) {
				String queryString = "FROM FLRTheClient ccliste " +
						buildFlrJoinClause() + 
						buildFlrWhereClause() + 
						buildFlrGroupByClause() + 
						buildFlrOrderByClause();
				query = session.createQuery(queryString);
			} else {
				String queryString = "SELECT CAST(ccliste.C_NR AS VARCHAR(120)) AS C_NR, " +
						"CAST(ccliste.C_BENUTZERNAME AS VARCHAR(120)) AS C_BENUTZERNAME, " + 
						"CAST(ccliste.C_MANDANT AS VARCHAR(3)) AS C_MANDANT, " +
						"rolle.C_BEZ, ccliste.T_LOGGEDIN, " + 
						"(dense_rank() over (order by ccliste.C_BENUTZERNAME)) AS rank " +
						"FROM LP_THECLIENT ccliste " + 
						buildSqlJoinClause() + 
						buildSqlWhereClause() + 
						buildSqlGroupByClause() +
						buildSqlOrderByClause();
				query = session.createSQLQuery(queryString);
			}

			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);

			if(modeHibernate) {
				List<FLRTheClient> resultList = query.list();
				Object[][] rows = new Object[resultList.size()][colCount];
				
				int row = 0;
				int col = 0;
		
				for (FLRTheClient flrTheClient : resultList) {
					Object o[] = new Object[colCount];
					col = 0;		
					o[col++] = flrTheClient.getCnr();
					o[col++] = flrTheClient.getC_benutzername().trim();
					o[col++] = flrTheClient.getC_mandant();
					o[col++] = flrTheClient.getFlrsystemrolle().getC_bez();
					o[col++] = flrTheClient.getT_loggedin();
					o[col++] = new Integer(1);
					
					rows[row++] = o;
				}
				result = new QueryResult(rows, this.getRowCount(), startIndex, endIndex, 0);				
			} else {
				List<Object[]> resultList = query.list();
				Object[][] rows = new Object[resultList.size()][colCount];
				
				int row = 0;
				int col = 0;
		
				for (Object[] flrObject : resultList) {
					Object o[] = new Object[colCount];
					col = 0;		
					o[col++] = ((String)flrObject[0]).trim();
					o[col++] = ((String)flrObject[1]).trim();
					o[col++] = ((String)flrObject[2]).trim();
					o[col++] = ((String)flrObject[3]).trim();
					o[col++] = flrObject[4];
					o[col++] = flrObject[5];
					
					rows[row++] = o;
				}
				result = new QueryResult(rows, this.getRowCount(), startIndex, endIndex, 0);				
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			closeSession(session);
		}
		return result;
	}
	
	public QueryResult getPageAt0(Integer rowIndex) {
		List<TheClientLoggedInDto> users = new ArrayList<TheClientLoggedInDto>();
		
		try {
			users = getLogonFac()
					.getUsersLoggedInMandant(theClientDto);
		} catch(RemoteException e) {
		}

		return getPageAt(rowIndex, users);
	}

	/**
	 * getRowCountFromDataBase
	 * 
	 * @return int
	 * @throws EJBExceptionLP
	 */
	protected long getRowCountFromDataBase() throws EJBExceptionLP {
		String queryString = "SELECT  COUNT(distinct ccliste.cnr) FROM com.lp.server.system.fastlanereader.generated.FLRTheClient AS ccliste "
				+ buildFlrJoinClause()
				+ buildFlrWhereClause();
		return getRowCountFromDataBaseByQuery(queryString);
	}

	protected long getRowCountFromDataBase0() throws EJBExceptionLP {	
		try {
			List<TheClientLoggedInDto> users = getLogonFac()
					.getUsersLoggedInMandant(theClientDto);
			return users.size();
		} catch(Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);			
		}
	}

	private String buildFlrOrderByClause() {
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
							orderBy.append("ccliste." + kriterien[i].kritName);
							orderBy.append(" ");
							orderBy.append(kriterien[i].value);
						}
					}
				}
			}
			if (orderBy.indexOf("ccliste." + "cnr") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" ").append("ccliste.").append("cnr").append(" ");
				sortAdded = true;
			}
			if (sortAdded) {
				orderBy.insert(0, " ORDER BY ");
			}
		}
		return orderBy.toString();	
	}
	
	private String buildSqlOrderByClause() {
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
							if("cnr".equals(kriterien[i].kritName)) {
								orderBy.append("ccliste.C_NR");
							} else if (TheClientFac.FLRSPALTE_SYSTEMROLLE_C_BEZ.equals(kriterien[i].kritName)) {
								orderBy.append("rolle.C_BEZ");
							} else {
								orderBy.append("ccliste.").append(kriterien[i].kritName.toUpperCase());
							}
							orderBy.append(" ");
							orderBy.append(kriterien[i].value);
						}
					}
				}
			}

			if (orderBy.indexOf("ccliste.C_NR") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" ").append("ccliste.C_NR").append(" ");
				sortAdded = true;
			}
			if (sortAdded) {
				orderBy.insert(0, " ORDER BY ");
			}
		}
		return orderBy.toString();	
	}
	
	private String buildFlrGroupByClause() {
		return "";
	}

	private String buildSqlGroupByClause() {
		return "";
	}

	private String buildFlrJoinClause() {
		return "";
	}

	private String buildSqlJoinClause() {
		return " INNER JOIN PERS_SYSTEMROLLE rolle ON rolle.I_ID = ccliste.SYSTEMROLLE_I_ID ";
	}

	private Date today = null;

	private Date getToday() {
		if(today == null) {
			today = Helper.cutDate(getDate()); 
		}
		return today;
	}
	
	private String buildFlrWhereClause() {		
		return 
				" WHERE ccliste.t_loggedout IS NULL" +
				" AND ccliste.flrsystemrolle.i_max_users IS NULL" + 
				" AND ccliste.hvmalizenz_IId IS NULL" + 
				" AND ccliste.t_loggedin >= '" + Helper.formatDateWithSlashes(getToday()) + "'";
	}
	
	private String buildSqlWhereClause() {		
		return 
				" WHERE ccliste.T_LOGGEDOUT IS NULL" +
				" AND rolle.I_MAX_USERS IS NULL" + 
				" AND ccliste.HVMALIZENZ_I_ID IS NULL" + 
				" AND ccliste.T_LOGGEDIN >= '" + Helper.formatDateWithSlashes(getToday()) + "'";
	}

	/**
	 * getTableInfo
	 * 
	 * @return TableInfo
	 */
	public TableInfo getTableInfo() {
		if (super.getTableInfo() == null) {
			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();
			setTableInfo(new TableInfo(
					new Class[] { String.class, String.class, Locale.class,
							Locale.class, java.sql.Timestamp.class, Integer.class},
					new String[] {
							"User-ID",
							getTextRespectUISpr("client.benutzername",
									mandantCNr, locUI),
							getTextRespectUISpr("report.mandant", mandantCNr,
									locUI),
							getTextRespectUISpr("lp.systemrolle", mandantCNr,
									locUI),
							getTextRespectUISpr("client.logondatum",
									mandantCNr, locUI), 
							getTextRespectUISpr("client.licencecounter", 
									mandantCNr, locUI)},
					new int[] {
							-1, // diese Spalte wird ausgeblendet
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_M},
					new String[] { 
							"cnr",
							TheClientFac.FLRSPALTE_C_BENUTZERNAME, 
							"c_mandant",
							TheClientFac.FLRSPALTE_SYSTEMROLLE_C_BEZ,
							TheClientFac.FLRSPALTE_T_LOGGEDIN,
							Facade.NICHT_SORTIERBAR
							}));
		}
		return super.getTableInfo();
	}

	public QueryResult sort(SortierKriterium[] sortierKriterien, Object selectedId) {
		getQuery().setSortKrit(sortierKriterien);

		QueryResult result = null;
		int rowNumber = 0;

		if (selectedId != null) {
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();
				String queryString = "select ccliste.cnr from FLRTheClient ccliste "
						+ buildFlrWhereClause() + buildFlrOrderByClause();
				Query query = session.createQuery(queryString);
				ScrollableResults scrollableResult = query.scroll();
				if (scrollableResult != null) {
					scrollableResult.beforeFirst();
					while (scrollableResult.next()) {
						String id = (String) scrollableResult.getString(0);
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
	
	public QueryResult sort0(SortierKriterium[] sortierKriterien,
			Object selectedId) {
		MultiColumnSortComparator comp = new MultiColumnSortComparator();
		
		if(sortierKriterien == null || sortierKriterien.length == 0) {
			comp.add(new LogonDateAscComparator());
		} else {		
			for (SortierKriterium sortierKriterium : sortierKriterien) {
				if(!sortierKriterium.isKrit) continue;
				
				if(sortierKriterium.kritName.equals(TheClientFac.FLRSPALTE_C_BENUTZERNAME)) {
					if(SortierKriterium.SORT_ASC.equals(sortierKriterium.value)) {
						comp.add(new BenutzernameAscComparator());
					} else {
						comp.add(new BenutzernameDescComparator());
					}
				} else if(sortierKriterium.kritName.equals(TheClientFac.FLRSPALTE_T_LOGGEDIN)) {
					if(SortierKriterium.SORT_ASC.equals(sortierKriterium.value)) {
						comp.add(new LogonDateAscComparator());
					} else {
						comp.add(new LogonDateDescComparator());
					}				
				} else if(sortierKriterium.kritName.equals(TheClientFac.FLRSPALTE_SYSTEMROLLE_C_BEZ)) {
					if(SortierKriterium.SORT_ASC.equals(sortierKriterium.value)) {
						comp.add(new SystemrolleAscComparator());
					} else {
						comp.add(new SystemrolleDescComparator());
					}
				} else if(sortierKriterium.kritName.equals("c_mandant")) {
					if(SortierKriterium.SORT_ASC.equals(sortierKriterium.value)) {
						comp.add(new MandantAscComparator());
					} else {
						comp.add(new MandantDescComparator());
					}
				}
			}
		}
		
		List<TheClientLoggedInDto> users = new ArrayList<TheClientLoggedInDto>();
		try {
			users = getLogonFac().getUsersLoggedInMandant(theClientDto);
			Collections.sort(users, comp);
		} catch(RemoteException e) {
		}
	
		return getPageAt(new Integer(0), users);
	}
	
	
	private class MultiColumnSortComparator implements Comparator<TheClientLoggedInDto> {
		private final List<Comparator<TheClientLoggedInDto>> comps;
		
		public MultiColumnSortComparator() {
			this(new ArrayList<Comparator<TheClientLoggedInDto>>());
		}

		public MultiColumnSortComparator(List<Comparator<TheClientLoggedInDto>> comps) {
			this.comps = comps;
		}
	
		public void add(Comparator<TheClientLoggedInDto> comp) {
			this.comps.add(comp);
		}
		
		@Override
		public int compare(TheClientLoggedInDto o1, TheClientLoggedInDto o2) {
			for (Comparator<TheClientLoggedInDto> comp : comps) {
				int diff = comp.compare(o1, o2);
				if(diff != 0) return diff;
			}
			return 0;
		}
	}
	
	private class BenutzernameAscComparator implements Comparator<TheClientLoggedInDto> {		
		@Override
		public int compare(TheClientLoggedInDto o1, TheClientLoggedInDto o2) {
			return o1.getBenutzername().compareTo(o2.getBenutzername());
		}
	}
	
	private class BenutzernameDescComparator implements Comparator<TheClientLoggedInDto> {		
		@Override
		public int compare(TheClientLoggedInDto o1, TheClientLoggedInDto o2) {
			return o2.getBenutzername().compareTo(o1.getBenutzername());
		}
	}
	
	private class SystemrolleAscComparator implements Comparator<TheClientLoggedInDto> {		
		@Override
		public int compare(TheClientLoggedInDto o1, TheClientLoggedInDto o2) {
			return o1.getSystemrolleCBez().compareTo(o2.getSystemrolleCBez());
		}
	}
	
	private class SystemrolleDescComparator implements Comparator<TheClientLoggedInDto> {		
		@Override
		public int compare(TheClientLoggedInDto o1, TheClientLoggedInDto o2) {
			return o2.getSystemrolleCBez().compareTo(o1.getSystemrolleCBez());
		}
	}
	
	private class LogonDateAscComparator implements Comparator<TheClientLoggedInDto> {		
		@Override
		public int compare(TheClientLoggedInDto o1, TheClientLoggedInDto o2) {
			return o1.getDLoggedin().compareTo(o2.getDLoggedin());
		}
	}
	
	private class LogonDateDescComparator implements Comparator<TheClientLoggedInDto> {		
		@Override
		public int compare(TheClientLoggedInDto o1, TheClientLoggedInDto o2) {
			return o2.getDLoggedin().compareTo(o1.getDLoggedin());
		}
	}
	
	private class MandantAscComparator implements Comparator<TheClientLoggedInDto> {		
		@Override
		public int compare(TheClientLoggedInDto o1, TheClientLoggedInDto o2) {
			return o1.getMandant().compareTo(o2.getMandant());
		}
	}
	
	private class MandantDescComparator implements Comparator<TheClientLoggedInDto> {		
		@Override
		public int compare(TheClientLoggedInDto o1, TheClientLoggedInDto o2) {
			return o2.getMandant().compareTo(o1.getMandant());
		}
	}
}
