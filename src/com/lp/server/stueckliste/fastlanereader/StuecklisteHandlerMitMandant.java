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
package com.lp.server.stueckliste.fastlanereader;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.Icon;

import org.apache.commons.collections.map.HashedMap;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikelsperren;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.SperrenIcon;
import com.lp.server.fertigung.fastlanereader.generated.FLRLos;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundesokoDto;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklistetextsuche;
import com.lp.server.stueckliste.service.IStuecklisteFLRData;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFLRDataDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.stueckliste.service.StuecklisteHandlerFeature;
import com.lp.server.stueckliste.service.StuecklisteQueryResult;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeArtikel;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
import com.lp.server.util.QueryFeature;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.UseCaseHandler;
import com.lp.server.util.fastlanereader.service.query.FilterBlock;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryParametersFeatures;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.EJBExceptionLP;

/**
 * <p>
 * Hier wird die FLR Funktionalit&auml;t f&uuml;r die Stueckliste implementiert.
 * Pro UseCase gibt es einen Handler.
 * </p>
 * <p>
 * Copright Logistik Pur GmbH (c) 2004
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
public class StuecklisteHandlerMitMandant extends UseCaseHandler {

	boolean bTextsucheInklusiveArtikelnummer = false;
	boolean bTextsucheInklusiveIndexRevision = false;

	private static final long serialVersionUID = 1L;
	private static final String FLR_STUECKLISTE = "stueckliste.";
	private MyFeature cachedFeature = null;

	private static Map<String, Integer> statusPriorityMap = new HashMap<String, Integer>() {
		{
			put(FertigungFac.STATUS_ANGELEGT, 0);
			put(FertigungFac.STATUS_STORNIERT, 0);
			put(FertigungFac.STATUS_AUSGEGEBEN, 1);
			put(FertigungFac.STATUS_IN_PRODUKTION, 2);
			put(FertigungFac.STATUS_GESTOPPT, 3);
			put(FertigungFac.STATUS_TEILERLEDIGT, 4);
			put(FertigungFac.STATUS_ERLEDIGT, 5);
		}
	};

	private class MyFeature extends QueryFeature<IStuecklisteFLRData> {
		private boolean featureLosStatus = false;
		private boolean featureKundenartikelnummer = false;
		private Integer kundeId;
		private Date sokoDate;

		public MyFeature() {
			if (getQuery() instanceof QueryParametersFeatures) {
				initializeFeature((QueryParametersFeatures) getQuery());
			}
			clearCacheValues();
		}

		@Override
		protected void initializeFeature(QueryParametersFeatures query) {
			featureLosStatus = query
					.hasFeature(StuecklisteHandlerFeature.LOS_STATUS);
			featureKundenartikelnummer = query
					.hasFeature(StuecklisteHandlerFeature.KUNDEN_ARTIKLENUMMER);
		}

		@Override
		protected IStuecklisteFLRData[] createFlrData(int rows) {
			return new StuecklisteFLRDataDto[rows];
		}

		private void clearCacheValues() {
			kundeId = null;
			sokoDate = null;
		}

		@Override
		public void setFlrRowCount(int rows) {
			super.setFlrRowCount(rows);
			clearCacheValues(); // Sicher ist sicher
		}

		public boolean hasFeatureLosStatus() {
			return featureLosStatus;
		}

		public boolean hasFeatureKundenartikelnummer() {
			return featureKundenartikelnummer;
		}

		private void setLosStatus(int row, int losCount) {
			setFlrDataObject(row, new StuecklisteFLRDataDto(losCount));
		}

		private void setLosStatus(int row, List<FLRLos> flrLose) {
			if (flrLose.size() == 0) {
				setFlrDataObject(row, new StuecklisteFLRDataDto(0));
				return;
			}

			Integer highestStatus = -1;
			String highestStatusCnr = null;
			for (FLRLos flrLos : flrLose) {
				Integer status = statusPriorityMap.get(flrLos.getStatus_c_nr());
				if (status == null) {
					highestStatus = Integer.MAX_VALUE;
					highestStatusCnr = FertigungFac.STATUS_ERLEDIGT;
					break;
				}
				if (status > highestStatus) {
					highestStatus = status;
					highestStatusCnr = flrLos.getStatus_c_nr();
				}
			}
			setFlrDataObject(row, new StuecklisteFLRDataDto(flrLose.size(),
					highestStatusCnr));
		}

		protected void buildLosStatus(int row, Integer stuecklisteId) {
			Session session = null;
			try {
				session = FLRSessionFactory.getFactory().openSession();
				Criteria crit = session.createCriteria(FLRLos.class);

				String[] stati = new String[7];
				stati[0] = FertigungFac.STATUS_ANGELEGT;
				stati[1] = FertigungFac.STATUS_IN_PRODUKTION;
				stati[2] = FertigungFac.STATUS_AUSGEGEBEN;
				stati[3] = FertigungFac.STATUS_TEILERLEDIGT;
				stati[4] = FertigungFac.STATUS_STORNIERT;
				stati[5] = FertigungFac.STATUS_ERLEDIGT;
				stati[6] = FertigungFac.STATUS_GESTOPPT;

				crit.add(Restrictions.in(FertigungFac.FLR_LOS_STATUS_C_NR,
						stati));
				crit.add(Restrictions.eq(
						FertigungFac.FLR_INTERNE_BESTELLUNG_STUECKLISTE_I_ID,
						stuecklisteId));
				crit.addOrder(Order.desc(FertigungFac.FLR_LOS_STATUS_C_NR));
				List<FLRLos> flrLose = (List<FLRLos>) crit.list();
				// setLosStatus(row, flrLose.size());
				setLosStatus(row, flrLose);
			} finally {
				closeSession(session);
			}
		}

		private Integer getKundeIdFromPartner(Integer partnerId)
				throws RemoteException {
			if (kundeId == null) {
				KundeDto kundeDto = getKundeFac()
						.kundeFindByiIdPartnercNrMandantOhneExc(partnerId,
								theClientDto.getMandant(), theClientDto);
				kundeId = kundeDto.getIId();
			}
			return kundeId;
		}

		private Date getSokoDate() {
			if (sokoDate == null) {
				sokoDate = new Date(getTimestamp().getTime());
			}
			return sokoDate;
		}

		private void setKundenartikelnummer(int row, String kundenartikelCnr) {
			IStuecklisteFLRData flrDataEntry = getFlrDataObject(row);
			if (flrDataEntry == null) {
				flrDataEntry = new StuecklisteFLRDataDto();
				setFlrDataObject(row, flrDataEntry);
			}

			flrDataEntry.setKundenartikelNr(kundenartikelCnr);
		}

		protected void buildKundenartikelnummer(int row, Integer itemId,
				Integer partnerId) {
			try {
				KundesokoDto dto = getKundesokoFac()
						.kundesokoFindByKundeIIdArtikelIIdGueltigkeitsdatumOhneExc(
								getKundeIdFromPartner(partnerId), itemId,
								getSokoDate());
				if (dto != null) {
					setKundenartikelnummer(row, dto.getCKundeartikelnummer());
				}
			} catch (RemoteException e) {
				setKundenartikelnummer(row, "Nicht ermittelbar");
			}
		}

		public void build(int row, Object[] o) {
			if (hasFeatureLosStatus()) {
				buildLosStatus(row, (Integer) o[0]);
			}
			if (hasFeatureKundenartikelnummer()) {
				buildKundenartikelnummer(row, (Integer) o[6], (Integer) o[8]);
			}
		}
	}

	private MyFeature getFeature() {
		if (cachedFeature == null) {
			cachedFeature = new MyFeature();
		}
		return cachedFeature;
	}

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
			session = setFilter(session);
			String queryString = this.getFromClause() + this.buildWhereClause()
					+ this.buildOrderByClause();
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount + 3];

			getFeature().setFlrRowCount(rows.length);

			int row = 0;
			while (resultListIterator.hasNext()) {
				Object o[] = (Object[]) resultListIterator.next();

				Object[] rowToAddCandidate = new Object[colCount + 3];
				rowToAddCandidate[0] = o[0];
				rowToAddCandidate[1] = o[1];
				rowToAddCandidate[2] = o[5] != null ? ((String) o[5]).trim()
						: o[5];
				rowToAddCandidate[3] = o[2];
				rowToAddCandidate[4] = o[3];

				FLRArtikelsperren as = (FLRArtikelsperren) o[4];

				if (as != null) {
					String gesperrt = null;

					if (as != null) {
						gesperrt = as.getFlrsperren().getC_bez();
					}

					rowToAddCandidate[5] = gesperrt;
				}

				rowToAddCandidate[6] = o[6];
				rowToAddCandidate[7] = o[7];
				rowToAddCandidate[8] = o[8];

				rowToAddCandidate[6] = o[9];

				rows[row] = rowToAddCandidate;

				getFeature().build(row, o);
				row++;
			}

			if (getFeature().hasFeatureLosStatus()) {
				StuecklisteQueryResult stuecklisteResult = new StuecklisteQueryResult(
						rows, getRowCount(), startIndex, endIndex, 0);
				stuecklisteResult.setFlrData(getFeature().getFlrData());
				result = stuecklisteResult;
			} else {
				result = new QueryResult(rows, this.getRowCount(), startIndex,
						endIndex, 0);
			}
		} catch (HibernateException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			try {
				closeSession(session);
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
			session = setFilter(session);
			String queryString = "SELECT COUNT(*) FROM com.lp.server.stueckliste.fastlanereader.generated.FLRStueckliste AS stueckliste LEFT OUTER JOIN stueckliste.flrartikel.artikelsprset AS aspr"
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
				closeSession(session);
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
			boolean bvolltext = false;
			for (int i = 0; i < filterKriterien.length; i++) {
				if (filterKriterien[i].isKrit) {
					if (filterAdded) {
						where.append(" " + booleanOperator);
					}
					filterAdded = true;
					if (filterKriterien[i].kritName.equals("stueckliste."
							+ StuecklisteFac.FLR_STUECKLISTE_FLRARTIKEL
							+ ".c_nr")) {
						String s = "";
						s = filterKriterien[i].value.toLowerCase();
						filterKriterien[i].value = s;
					}

					if (filterKriterien[i].kritName.equals("c_volltext")) {

						String suchstring = "lower(coalesce(aspr.c_bez,'')||' '||coalesce(aspr.c_kbez,'')||' '||coalesce(aspr.c_zbez,'')||' '||coalesce(aspr.c_zbez2,''))";

						if (bTextsucheInklusiveArtikelnummer) {
							suchstring += "||' '||lower(stueckliste.flrartikel.c_nr)";
						}

						if (bTextsucheInklusiveIndexRevision) {
							suchstring += "||' '||lower(coalesce(stueckliste.flrartikel.c_index,''))||' '||lower(coalesce(stueckliste.flrartikel.c_revision,''))";
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

						where.append(") ");

					} else if (filterKriterien[i].kritName
							.equals(LieferantFac.FLR_PARTNER
									+ "."
									+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1)) {
						ParametermandantDto parameter = null;
						try {
							parameter = getParameterFac()
									.getMandantparameter(
											theClientDto.getMandant(),
											ParameterFac.KATEGORIE_ALLGEMEIN,
											ParameterFac.PARAMETER_SUCHEN_INKLUSIVE_KBEZ);
						} catch (RemoteException ex) {
							throwEJBExceptionLPRespectOld(ex);
						}
						Boolean bSuchenInklusiveKbez = (java.lang.Boolean) parameter
								.getCWertAsObject();
						if (bSuchenInklusiveKbez) {
							if (filterKriterien[i].isBIgnoreCase()) {
								where.append(" ( lower(stueckliste."
										+ filterKriterien[i].kritName + ")");
								where.append(" " + filterKriterien[i].operator);
								where.append(" "
										+ filterKriterien[i].value
												.toLowerCase());
								where.append(" OR lower(stueckliste.flrpartner.c_kbez"
										+ ") ");
								where.append(" " + filterKriterien[i].operator);
								where.append(" "
										+ filterKriterien[i].value
												.toLowerCase() + ") ");
							} else {
								where.append(" stueckliste."
										+ filterKriterien[i].kritName);
								where.append(" " + filterKriterien[i].operator);
								where.append(" " + filterKriterien[i].value);
								where.append("OR stueckliste.flrpartner.c_kbez");
								where.append(" " + filterKriterien[i].operator);
								where.append(" " + filterKriterien[i].value);
							}
						} else {
							if (filterKriterien[i].isBIgnoreCase()) {
								where.append(" lower(stueckliste."
										+ filterKriterien[i].kritName + ")");
							} else {
								where.append(" stueckliste."
										+ filterKriterien[i].kritName);
							}

							where.append(" " + filterKriterien[i].operator);

							if (filterKriterien[i].isBIgnoreCase()) {
								where.append(" "
										+ filterKriterien[i].value
												.toLowerCase());
							} else {
								where.append(" " + filterKriterien[i].value);
							}
						}
					} else if (filterKriterien[i].kritName.equals("c_suche")) {

						where.append(buildWhereClauseExtendedSearchWithoutDuplicates(
								FLRStuecklistetextsuche.class.getSimpleName(),
								FLR_STUECKLISTE, filterKriterien[i]));

					} else {
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" lower("
									+ filterKriterien[i].kritName + ")");
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
							orderBy.append(" " + kriterien[i].kritName);
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
				orderBy.append("stueckliste.flrartikel.c_nr ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("stueckliste.flrartikel.c_nr") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" stueckliste.flrartikel.c_nr" + " ");
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
		return "SELECT stueckliste.i_id, stueckliste.flrartikel.c_nr, aspr.c_bez, aspr.c_zbez, "
				+ "(SELECT s FROM FLRArtikelsperren as s WHERE s.artikel_i_id=stueckliste.artikel_i_id AND s.i_sort=1) as sperren, "
				+ "stueckliste.stuecklisteart_c_nr, stueckliste.flrartikel.i_id, stueckliste.t_freigabe, stueckliste.partner_i_id,stueckliste.mandant_c_nr "
				+ " FROM FLRStueckliste AS stueckliste"
				+ " LEFT OUTER JOIN stueckliste.flrartikel.artikelsprset AS aspr ";
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
				session = setFilter(session);
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
					closeSession(session);
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
			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();
			try {
				ParametermandantDto param = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_ARTIKEL,
								ParameterFac.PARAMETER_TEXTSUCHE_INKLUSIVE_ARTIKELNUMMER);
				bTextsucheInklusiveArtikelnummer = (Boolean) param
						.getCWertAsObject();

				param = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_ARTIKEL,
								ParameterFac.PARAMETER_TEXTSUCHE_INKLUSIVE_INDEX_REVISION);
				bTextsucheInklusiveIndexRevision = (Boolean) param
						.getCWertAsObject();

			} catch (RemoteException ex) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
			}

			setTableInfo(new TableInfo(new Class[] { Integer.class,
					String.class, String.class, String.class, String.class,
					SperrenIcon.class, String.class },
					new String[] {
							"Id",
							getTextRespectUISpr("artikel.artikelnummerlang",
									mandantCNr, locUI),
							getTextRespectUISpr("lp.stuecklistenart",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("bes.artikelbezeichnung",
									mandantCNr, locUI),
							getTextRespectUISpr("artikel.zusatzbez",
									mandantCNr, locUI),
							"S",
							getTextRespectUISpr("report.mandant", mandantCNr,
									locUI) },

					new int[] {
							-1, // diese Spalte wird ausgeblendet
							QueryParameters.FLR_BREITE_L,
							QueryParameters.FLR_BREITE_S,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_XL,
							QueryParameters.FLR_BREITE_S,
							QueryParameters.FLR_BREITE_S }

					, new String[] { "i_id", "stueckliste.flrartikel.c_nr",
							"stueckliste.stuecklisteart_c_nr", "aspr.c_bez",
							"aspr.c_zbez", Facade.NICHT_SORTIERBAR,
							"stueckliste.mandant_c_nr" }));

		}

		return super.getTableInfo();
	}

	public PrintInfoDto getSDocPathAndPartner(Object key) {
		StuecklisteDto stuecklisteDto = null;
		ArtikelDto artikelDto = null;
		try {
			getLocaleFac().getAllSpr(theClientDto.getLocUi(),
					theClientDto.getMandant());
			stuecklisteDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(
					(Integer) key, theClientDto);
			artikelDto = getArtikelFac().artikelFindByPrimaryKey(
					stuecklisteDto.getArtikelIId(), theClientDto);
		} catch (Exception e) {
			// Nicht gefunden
		}
		if (artikelDto != null) {
			// String sPath = JCRDocFac.HELIUMV_NODE + "/"
			// + theClientDto.getMandant() + "/"
			// + LocaleFac.BELEGART_ARTIKEL.trim() + "/"
			// + artikelDto.getArtikelartCNr().trim() + "/"
			// + artikelDto.getCNr().replace("/", ".");
			DocPath docPath = new DocPath(new DocNodeArtikel(artikelDto));
			return new PrintInfoDto(docPath, null, getSTable());
		} else {
			return null;
		}
	}

	public String getSTable() {
		return "STUECKLISTE";
	}
}
