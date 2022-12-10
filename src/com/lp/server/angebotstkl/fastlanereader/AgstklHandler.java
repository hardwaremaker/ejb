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
package com.lp.server.angebotstkl.fastlanereader;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.angebotstkl.service.AgstklDto;
import com.lp.server.angebotstkl.service.AngebotstklFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeAgStueckliste;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.FlrFirmaAnsprechpartnerFilterBuilder;
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
 * Hier wird die FLR Funktionalit&auml;t f&uuml;r die Angebotsstuecklisten
 * implementiert. Pro UseCase gibt es einen Handler.
 * </p>
 * <p>
 * Copright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum 2005-02-14
 * </p>
 * <p>
 * </p>
 * 
 * @author ck
 * @version 1.0
 */
public class AgstklHandler extends UseCaseHandler {
	/**
	 * the size of the data page returned in QueryResult.
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String FLR_AGSTKL = "agstkl.";

	private class AgstklKundeAnsprechpartnerFilterBuilder extends FlrFirmaAnsprechpartnerFilterBuilder {
		
		public AgstklKundeAnsprechpartnerFilterBuilder(boolean bSuchenInklusiveKBez) {
			super(bSuchenInklusiveKBez);
		}

		@Override
		public String getFlrPartner() {
			return FLR_AGSTKL + AngebotstklFac.FLR_AGSTKL_FLRKUNDE + "." + LieferantFac.FLR_PARTNER;
		}

		@Override
		public String getFlrPropertyAnsprechpartnerIId() {
			return FLR_AGSTKL + "ansprechpartner_i_id_kunde";
		}
	}
	
	/**
	 * The information needed for the kundes table.
	 */

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
				Object[] agstkl = (Object[]) resultListIterator.next();
				rows[row][col++] = agstkl[0];
				rows[row][col++] = agstkl[1];
				rows[row][col++] = agstkl[2];

				if (agstkl[5] != null) {
					rows[row][col++] = agstkl[5]+"-"+agstkl[6]+" "+agstkl[7];
				}else {
					rows[row][col++] = null;
				}

				rows[row][col++] = agstkl[3];
				
				rows[row][col++] = agstkl[9];
				
				rows[row][col++] = agstkl[4];
				rows[row++][col++] =new Boolean(Helper.short2boolean((Short)agstkl[8]));
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
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, he);
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
			String queryString = " SELECT  count( distinct agstkl.i_id) from FLRAgstkl agstkl "
					+ " left join agstkl.flrkunde.flrpartner.flrlandplzort as flrlandplzort "
					+ " left join agstkl.flrkunde.flrpartner.flrlandplzort.flrort as flrort "
					+ " left outer join agstkl.angebotspositionen as angebotspositionen "
					+ " left join agstkl.flrkunde.flrpartner.flrlandplzort.flrland as flrland "
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

					// wenn nach der c_nr gefilter wird, wird das kriterium
					// veraendert
					if (filterKriterien[i].kritName.equals("c_nr")) {
						try {
							String sValue = super.buildWhereBelegnummer(
									filterKriterien[i], false);
							// Belegnummernsuche auch in "altem" Jahr, wenn im
							// neuen noch keines vorhanden ist
							if (!istBelegnummernInJahr("FLRAgstkl", sValue)) {
								sValue = super.buildWhereBelegnummer(
										filterKriterien[i], true);
							}
							where.append(" " + "" + filterKriterien[i].kritName);
							where.append(" " + filterKriterien[i].operator);
							where.append(" " + sValue);
						} catch (Throwable ex) {
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR,
									new Exception(ex));
						}
					} else if (filterKriterien[i].kritName
							.equals("angebotspositionen.flrangebot.c_nr")) {

						whereAngebot(where, filterKriterien[i]);

					} else if (filterKriterien[i].kritName
							.equals("agstkl.c_bez")) {

						
						where.append(" (");
						where.append(buildWhereClauseExtendedSearch(Arrays
								.asList(filterKriterien[i].value.split(" ")),
								filterKriterien[i].kritName, filterKriterien[i]
										.isBIgnoreCase()));
						where.append(" OR ");
						where.append(
								buildWhereClauseExtendedSearch(Arrays.asList(filterKriterien[i].value.split(" ")),
										"agstkl.c_zeichnungsnummer", filterKriterien[i].isBIgnoreCase()));
						where.append(") ");
						
						
						
					} else if (isKundeFilter(filterKriterien[i])) {
						AgstklKundeAnsprechpartnerFilterBuilder filterBuilder = new AgstklKundeAnsprechpartnerFilterBuilder(
								getParameterFac().getSuchenInklusiveKBez(theClientDto.getMandant()));
						filterBuilder.buildFirmaAnsprechpartnerFilter(filterKriterien[i], where);
					} else {
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" upper(" + ""
									+ filterKriterien[i].kritName + ")");
						} else {
							where.append(" " + "" + filterKriterien[i].kritName);
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
			if (filterAdded) {
				where.insert(0, " WHERE");
			}
		}

		return where.toString();
	}

	private boolean isKundeFilter(FilterKriterium filterKriterium) {
		return filterKriterium.kritName.equals(FLR_AGSTKL + AngebotstklFac.FLR_AGSTKL_FLRKUNDE
				+ "."
				+ KundeFac.FLR_PARTNER 
				+ "."
				+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1);
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
							orderBy.append("" + kriterien[i].kritName);
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
				orderBy.append("agstkl.c_nr DESC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("agstkl.i_id") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" agstkl.i_id" + " ");
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
		return "SELECT distinct agstkl.i_id, agstkl.c_nr, agstkl.flrkunde.flrpartner.c_name1nachnamefirmazeile1, agstkl.c_bez, agstkl.t_belegdatum, agstkl.flrkunde.flrpartner.flrlandplzort.flrland.c_lkz, agstkl.flrkunde.flrpartner.flrlandplzort.c_plz, agstkl.flrkunde.flrpartner.flrlandplzort.flrort.c_name, agstkl.b_vorlage, agstkl.c_zeichnungsnummer from FLRAgstkl agstkl "
				+ " left join agstkl.flrkunde.flrpartner.flrlandplzort as flrlandplzort "
				+ " left join agstkl.flrkunde.flrpartner.flrlandplzort.flrort as flrort "
				+ " left outer join agstkl.angebotspositionen as angebotspositionen "
				+ " left join agstkl.flrkunde.flrpartner.flrlandplzort.flrland as flrland ";
	}

	private void whereAngebot(StringBuffer where,
			FilterKriterium filterKriterium) {
		try {

			String sValue = super.buildWhereBelegnummer(filterKriterium, false);

			// Belegnummernsuche auch in "altem" Jahr, wenn im
			// neuen noch keines vorhanden ist
			if (!istBelegnummernInJahr("FLRAngebot", sValue)) {
				sValue = super.buildWhereBelegnummer(filterKriterium, true);
			}
			where.append(" " + filterKriterium.kritName);
			where.append(" " + filterKriterium.operator);
			where.append(" " + sValue);
		} catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, ex);
		}
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
				String queryString = getFromClause() + buildWhereClause()
						+ buildOrderByClause();
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
			setTableInfo(new TableInfo(
					new Class[] { Integer.class, String.class, String.class,
							String.class, String.class,String.class, Date.class, Boolean.class },
					new String[] {
							"Id",
							getTextRespectUISpr("bes.belegartnummer",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.kunde",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.ort",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("agstkl.projekt.bezeichnung",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("agstkl.projekt.zeichnungsnummer",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.datum",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("as.vorlage",
									theClientDto.getMandant(),
									theClientDto.getLocUi()) },
					new int[] {
							-1, // ausgeblendet
							QueryParameters.FLR_BREITE_XM,
							QueryParameters.FLR_BREITE_L,
							QueryParameters.FLR_BREITE_L,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_S},

					new String[] {
							"i_id",
							"agstkl.c_nr",
							"agstkl."
									+ AngebotstklFac.FLR_AGSTKL_FLRKUNDE
									+ "."
									+ KundeFac.FLR_PARTNER
									+ "."
									+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,

							"agstkl." + AngebotstklFac.FLR_AGSTKL_FLRKUNDE
									+ "." + LieferantFac.FLR_PARTNER + "."
									+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT
									+ "." + SystemFac.FLR_LP_FLRLAND + "."
									+ SystemFac.FLR_LP_LANDLKZ + ", agstkl."
									+ AngebotstklFac.FLR_AGSTKL_FLRKUNDE + "."
									+ LieferantFac.FLR_PARTNER + "."
									+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT
									+ "." + SystemFac.FLR_LP_LANDPLZORTPLZ,

							"agstkl.c_bez",
							"agstkl.c_zeichnungsnummer",
							"agstkl." + AngebotstklFac.FLR_AGSTKL_T_BELEGDATUM ,
							"agstkl.b_vorlage" }));
		}

		return super.getTableInfo();
	}

	public PrintInfoDto getSDocPathAndPartner(Object key) {
		AgstklDto agstklDto = null;
		KundeDto kundeDto = null;
		PartnerDto partnerDto = null;
		try {
			agstklDto = getAngebotstklFac().agstklFindByPrimaryKey(
					(Integer) key);
			kundeDto = getKundeFac().kundeFindByPrimaryKey(
					agstklDto.getKundeIId(), theClientDto);
			partnerDto = getPartnerFac().partnerFindByPrimaryKey(
					kundeDto.getPartnerIId(), theClientDto);
		} catch (Exception e) {
			// Nicht gefunden
		}
		if (agstklDto != null) {
			// String sPath = JCRDocFac.HELIUMV_NODE + "/"
			// + theClientDto.getMandant() + "/"
			// + LocaleFac.BELEGART_AGSTUECKLISTE.trim() + "/"
			// + LocaleFac.BELEGART_AGSTUECKLISTE.trim() + "/"
			// + agstklDto.getCNr().replace("/", ".");
			DocPath docPath = new DocPath(new DocNodeAgStueckliste(agstklDto));
			Integer iPartnerIId = null;
			if (partnerDto != null) {
				iPartnerIId = partnerDto.getIId();
			}
			return new PrintInfoDto(docPath, iPartnerIId, getSTable());
		} else {
			return null;
		}
	}

	public String getSTable() {
		return "AGSTUECKLISTE";
	}
}
