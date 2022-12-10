
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
package com.lp.server.fertigung.fastlanereader;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;
import com.lp.server.system.pkgenerator.format.LpBelegnummerFormat;
import com.lp.server.system.pkgenerator.format.LpDefaultBelegnummerFormat;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
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
import com.lp.util.Helper;

/**
 * <p>
 * Hier wird die FLR Funktionalitaet fuer das Losmaterial implementiert.
 * </p>
 * <p>
 * Copright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum 21.07.2005
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version 1.0
 */
public class LossollmaterialTrumpfHandler extends UseCaseHandler {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String FLR_LOSMAT = "flrlossollmaterial.";
	private static final String FLR_LOSMAT_FROM_CLAUSE = " from FLRLossollmaterial flrlossollmaterial LEFT OUTER JOIN flrlossollmaterial.flrartikelliste.artikelsprset AS aspr LEFT OUTER JOIN flrlossollmaterial.flrlos.flrauftrag.flrkunde.flrpartner AS partner "
			+ " LEFT OUTER JOIN flrlossollmaterial.flrlos.flrkunde.flrpartner AS partnerK LEFT OUTER JOIN flrlossollmaterial.flrlos.flrauftrag.flrkunde.flrpartner AS partner LEFT OUTER JOIN flrlossollmaterial.flrlos AS flrlos ";

	int bLosnummerAuftragsbezogen = 0;
	
	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int startIndex = Math.max(rowIndex.intValue() - (PAGE_SIZE / 2), 0);
			int endIndex = startIndex + PAGE_SIZE - 1;

			session = factory.openSession();
			session = setFilter(session);
			String queryString = "SELECT flrlossollmaterial,aspr.c_bez, (SELECT at.c_fehlercode FROM FLRArtikeltrutops at WHERE at.artikel_i_id=flrlossollmaterial.flrartikel.i_id) as fc_artikel from FLRLossollmaterial flrlossollmaterial  LEFT OUTER JOIN flrlossollmaterial.flrartikelliste.artikelsprset AS aspr  LEFT OUTER JOIN flrlossollmaterial.flrlos.flrkunde.flrpartner AS partnerK  LEFT OUTER JOIN flrlossollmaterial.flrlos.flrauftrag.flrkunde.flrpartner AS partner "

					+ this.buildWhereClause() + this.buildOrderByClause();
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(PAGE_SIZE);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;

			while (resultListIterator.hasNext()) {

				Object o[] = (Object[]) resultListIterator.next();

				FLRLossollmaterial losmat = (FLRLossollmaterial) o[0];

				Object[] rowToAddCandidate = new Object[colCount];
				rowToAddCandidate[getTableColumnInformation().getViewIndex("i_id")] = losmat.getI_id();

				String kundeName = "";
				if (losmat.getFlrlos().getFlrkunde() != null) {
					kundeName = HelperServer.formatNameAusFLRPartner(losmat.getFlrlos().getFlrkunde().getFlrpartner());
				} else if (losmat.getFlrlos().getFlrauftrag() != null) {
					kundeName = HelperServer
							.formatNameAusFLRPartner(losmat.getFlrlos().getFlrauftrag().getFlrkunde().getFlrpartner());
				}
				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.kunde")] = kundeName;
				rowToAddCandidate[getTableColumnInformation().getViewIndex("fert.tab.unten.los.title")] = losmat
						.getFlrlos().getC_nr();
				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.artikelnummer")] = losmat.getFlrartikel()
						.getC_nr();
				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.bezeichnung")] = o[1];

				
				rowToAddCandidate[getTableColumnInformation().getViewIndex("artikel.trumpf.fehlercode.los")] = losmat.getC_fehlercode();
				
				rowToAddCandidate[getTableColumnInformation().getViewIndex("artikel.trumpf.fehlercode.artikel")] = o[2];
				
				
				rowToAddCandidate[getTableColumnInformation().getViewIndex("artikel.trumpf.exportbeginn")] = losmat.getT_export_beginn();
				rowToAddCandidate[getTableColumnInformation().getViewIndex("artikel.trumpf.exportende")] = losmat.getT_export_ende();

				
				
				
				
				
				rows[row] = rowToAddCandidate;

				row++;
			}
			result = new QueryResult(rows, this.getRowCount(), startIndex, endIndex, 0);
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
			String queryString = "select count(*) " + this.getFromClause() + this.buildWhereClause();
			Query query = session.createQuery(queryString);
			List<?> rowCountResult = query.list();
			if (rowCountResult != null && rowCountResult.size() > 0) {
				rowCount = ((Long) rowCountResult.get(0)).longValue();
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		} finally {
			closeSession(session);
		}
		return rowCount;
	}

	/**
	 * builds the where clause of the HQL (Hibernate Query Language) statement using
	 * the current query.
	 * 
	 * @return the HQL where clause.
	 */
	private String buildWhereClause() {
		StringBuffer where = new StringBuffer("");

		if (this.getQuery() != null && this.getQuery().getFilterBlock() != null
				&& this.getQuery().getFilterBlock().filterKrit != null) {

			FilterBlock filterBlock = this.getQuery().getFilterBlock();
			FilterKriterium[] filterKriterien = this.getQuery().getFilterBlock().filterKrit;
			String booleanOperator = filterBlock.boolOperator;
			boolean filterAdded = false;

			for (int i = 0; i < filterKriterien.length; i++) {
				if (filterKriterien[i].isKrit) {
					
					
					
					
					if (filterAdded) {
						where.append(" " + booleanOperator);
					}
					filterAdded = true;
					
					
					
					 if (filterKriterien[i].kritName.equals("flrlossollmaterial.flrlos." + FertigungFac.FLR_LOS_C_NR)) {
						try {

							FilterKriterium fkLocal = new FilterKriterium(filterKriterien[i].kritName,
									filterKriterien[i].isKrit, filterKriterien[i].value, filterKriterien[i].operator,
									filterKriterien[i].isBIgnoreCase());

							LpBelegnummerFormat f = getBelegnummerGeneratorObj()
									.getBelegnummernFormat(theClientDto.getMandant());
							LpDefaultBelegnummerFormat defaultFormat = (LpDefaultBelegnummerFormat) f;

							String postfix = "-___";
							boolean bPostfixVorhanden = false;
							if (fkLocal.value != null && fkLocal.value.indexOf("-") > -1) {
								postfix = fkLocal.value.substring(fkLocal.value.indexOf("-"));
								fkLocal.value = fkLocal.value.substring(0, fkLocal.value.indexOf("-")) + "'";
								bPostfixVorhanden = true;
							}

							String sValue = super.buildWhereBelegnummer(fkLocal, false);

							if (bLosnummerAuftragsbezogen >= 1) {
								if (sValue != null) {
									sValue = sValue.substring(0, defaultFormat.getStellenGeschaeftsjahr() + 2) + "_"
											+ sValue.substring(defaultFormat.getStellenGeschaeftsjahr() + 3);

									if (!sValue.endsWith("'")) {
										sValue += "'";
									}

								}
							}

							// Belegnummernsuche auch in "altem" Jahr, wenn im
							// neuen noch keines vorhanden ist
							if (!istBelegnummernInJahr("FLRLos", sValue)) {
								sValue = super.buildWhereBelegnummer(fkLocal, true);

								if (bLosnummerAuftragsbezogen >= 1) {
									if (sValue != null) {
										sValue = sValue.substring(0, defaultFormat.getStellenGeschaeftsjahr() + 2) + "_"
												+ sValue.substring(defaultFormat.getStellenGeschaeftsjahr() + 3);
										if (!sValue.endsWith("'")) {
											sValue += "'";
										}
									}
								}

							}

							if (bLosnummerAuftragsbezogen == 0) {
								where.append(" " + fkLocal.kritName);
								where.append(" " + fkLocal.operator);
								where.append(" " + sValue + "");

							} else {

								int iStellenGesamtAuftragsbezogen = defaultFormat.getStellenGeschaeftsjahr()
										+ defaultFormat.getStellenLfdNummer() + 5;

								// Losnummer kann maximal 12-stellig sein
								if (defaultFormat.getStellenGeschaeftsjahr() == 2) {
									if (iStellenGesamtAuftragsbezogen > 12) {

										iStellenGesamtAuftragsbezogen = 12;

									}
								}
								// SP2765
								if (defaultFormat.getStellenGeschaeftsjahr() == 4) {
									if (iStellenGesamtAuftragsbezogen > 13) {

										iStellenGesamtAuftragsbezogen = 13;

									}
								}

								String losnummer = super.buildWhereBelegnummer(fkLocal, false);
								String valueBelegnummernfilterAuftragsbezogen = losnummer.substring(0,
										defaultFormat.getStellenGeschaeftsjahr() + 2);
								postfix = postfix.replaceAll("'", "");
								String krit = null;
								if (fkLocal.value.indexOf(",") > 0) {
									krit = fkLocal.value.substring(0, fkLocal.value.indexOf(",")).replaceAll("%", "")
											.replaceAll("'", "") + postfix;
								} else {
									krit = fkLocal.value.replaceAll("%", "").replaceAll("'", "") + postfix;
								}

								valueBelegnummernfilterAuftragsbezogen = Helper.fitString2Length(
										valueBelegnummernfilterAuftragsbezogen,
										iStellenGesamtAuftragsbezogen - krit.length() + 1, '0') + krit + "'";

								// SP4455
								if (valueBelegnummernfilterAuftragsbezogen.length() > 14
										&& !valueBelegnummernfilterAuftragsbezogen.startsWith("'")) {
									valueBelegnummernfilterAuftragsbezogen = "'"
											+ valueBelegnummernfilterAuftragsbezogen;
								}

								if (!istBelegnummernInJahr("FLRLos", valueBelegnummernfilterAuftragsbezogen)
										&& !istBelegnummernInJahr("FLRLos",
												valueBelegnummernfilterAuftragsbezogen.replaceFirst("0", "").substring(
														0, valueBelegnummernfilterAuftragsbezogen.length() - 2)
														+ "_'")) {
									losnummer = super.buildWhereBelegnummer(fkLocal, true);
									valueBelegnummernfilterAuftragsbezogen = losnummer.substring(0,
											defaultFormat.getStellenGeschaeftsjahr() + 2);

									if (fkLocal.value.indexOf(",") > 0) {
										krit = fkLocal.value.substring(0, fkLocal.value.indexOf(","))
												.replaceAll("%", "").replaceAll("'", "") + postfix;
									} else {
										krit = fkLocal.value.replaceAll("%", "").replaceAll("'", "") + postfix;
									}

									valueBelegnummernfilterAuftragsbezogen = Helper.fitString2Length(
											valueBelegnummernfilterAuftragsbezogen,
											iStellenGesamtAuftragsbezogen - krit.length() + 1, '0') + krit + "'";

								}

								// Wenn '-' vorhanden, dann nur nach
								// Auftragsbezogenen Nummer suchen
								if (bPostfixVorhanden) {
									where.append(" " + fkLocal.kritName);
									where.append(" " + fkLocal.operator);
									where.append(" " + valueBelegnummernfilterAuftragsbezogen + "");
								} else {
									where.append("(");
									where.append(" (" + fkLocal.kritName);
									where.append(" " + fkLocal.operator);
									where.append(" " + sValue + " OR ");
									where.append(" " + fkLocal.kritName);
									where.append(" " + fkLocal.operator);
									where.append(" " + valueBelegnummernfilterAuftragsbezogen + " OR ");
									where.append(" " + fkLocal.kritName);
									where.append(" " + fkLocal.operator);
									where.append(" " + valueBelegnummernfilterAuftragsbezogen.replaceFirst("0", "")
											.substring(0, valueBelegnummernfilterAuftragsbezogen.length() - 2) + "_'"
											+ ") ");

									where.append(" OR ");
									fkLocal.kritName = "flrlossollmaterial.flrlos." + FertigungFac.FLR_LOSREPORT_FLRAUFTRAG + ".c_nr";
									whereAuftrag(where, fkLocal);
									where.append(")");
								}

							}

						} catch (Exception ex) {
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, ex);
						}
					}else if (filterKriterien[i].kritName
							.equals("flrlos.flrauftrag.flrkunde.flrpartner.c_name1nachnamefirmazeile1")) {

						where.append(" (lower(partnerK.c_name1nachnamefirmazeile1)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" " + filterKriterien[i].value.toLowerCase());

						where.append(" OR lower(partner.c_name1nachnamefirmazeile1)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" " + filterKriterien[i].value.toLowerCase());

						where.append(" OR lower(partnerK.c_name2vornamefirmazeile2)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" " + filterKriterien[i].value.toLowerCase());
						where.append(" OR lower(partner.c_name2vornamefirmazeile2)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" " + filterKriterien[i].value.toLowerCase() + ")");
						continue;
					}else {
						where.append(" " + FLR_LOSMAT + filterKriterien[i].kritName);
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

	private void whereAuftrag(StringBuffer where, FilterKriterium filterKriterium) {
		try {

			String sValue = super.buildWhereBelegnummer(filterKriterium, false);

			// Belegnummernsuche auch in "altem" Jahr, wenn im
			// neuen noch keines vorhanden ist
			if (!istBelegnummernInJahr("FLRAuftrag", sValue)) {
				sValue = super.buildWhereBelegnummer(filterKriterium, true);
			}
			where.append(" " + filterKriterium.kritName);
			where.append(" " + filterKriterium.operator);
			where.append(" " + sValue);
		} catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, ex);
		}
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
					if (!kriterien[i].kritName.endsWith(Facade.NICHT_SORTIERBAR)) {
						if (kriterien[i].isKrit) {
							if (sortAdded) {
								orderBy.append(", ");
							}
							sortAdded = true;
							if (!kriterien[i].kritName.startsWith("aspr")) {
								orderBy.append(kriterien[i].kritName);
							} else {
								orderBy.append(kriterien[i].kritName);
							}
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

				orderBy.append("flrlossollmaterial.flrlos.c_nr ASC, flrlossollmaterial.flrartikel.c_nr ASC ");

				sortAdded = true;
			}
			if (orderBy.indexOf("flrlossollmaterial.flrlos.c_nr") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" ").append("flrlossollmaterial.flrlos.c_nr").append(" ");
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
		return FLR_LOSMAT_FROM_CLAUSE;
	}

	public QueryResult sort(SortierKriterium[] sortierKriterien, Object selectedId) throws EJBExceptionLP {
		this.getQuery().setSortKrit(sortierKriterien);

		QueryResult result = null;
		int rowNumber = 0;

		if (selectedId != null && ((Integer) selectedId).intValue() >= 0) {
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();
				session = setFilter(session);
				String queryString = "select " + FLR_LOSMAT + FertigungFac.FLR_LOSSOLLMATERIAL_I_ID +",(SELECT at.c_fehlercode FROM FLRArtikeltrutops at WHERE at.artikel_i_id=flrlossollmaterial.flrartikel.i_id) as fc_artikel " 
						+ FLR_LOSMAT_FROM_CLAUSE + this.buildWhereClause() + this.buildOrderByClause();
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

	private TableColumnInformation createColumnInformation(String mandant, Locale locUi) {
		TableColumnInformation columns = new TableColumnInformation();

		try {
			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_LOSNUMMER_AUFTRAGSBEZOGEN);
			bLosnummerAuftragsbezogen = (Integer) parameter.getCWertAsObject();

			
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		columns.add("i_id", Integer.class, "i_id", QueryParameters.FLR_BREITE_SHARE_WITH_REST, "i_id");

		columns.add("lp.kunde", String.class, getTextRespectUISpr("lp.kunde", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, "partner.c_name1nachnamefirmazeile1");
		columns.add("fert.tab.unten.los.title", String.class,
				getTextRespectUISpr("fert.tab.unten.los.title", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, "flrlossollmaterial.flrlos.c_nr");

		columns.add("lp.artikelnummer", String.class, getTextRespectUISpr("lp.artikelnummer", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST,
				"flrlossollmaterial." + FertigungFac.FLR_LOSSOLLMATERIAL_FLRARTIKEL + ".c_nr");

		columns.add("lp.bezeichnung", String.class, getTextRespectUISpr("lp.bezeichnung", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, "aspr.c_bez");

		columns.add("artikel.trumpf.fehlercode.los", String.class,
				getTextRespectUISpr("artikel.trumpf.fehlercode.los", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, "flrlossollmaterial.c_fehlercode");
		
		columns.add("artikel.trumpf.fehlercode.artikel", String.class,
				getTextRespectUISpr("artikel.trumpf.fehlercode.artikel", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, "fc_artikel");
		

		columns.add("artikel.trumpf.exportbeginn", Timestamp.class,
				getTextRespectUISpr("artikel.trumpf.exportbeginn", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, "flrlossollmaterial.t_export_beginn");
		columns.add("artikel.trumpf.exportende", Timestamp.class,
				getTextRespectUISpr("artikel.trumpf.exportende", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, "flrlossollmaterial.t_export_ende");

		return columns;

	}

	public TableInfo getTableInfo() {

		TableInfo info = super.getTableInfo();
		if (info != null)
			return info;

		setTableColumnInformation(createColumnInformation(theClientDto.getMandant(), theClientDto.getLocUi()));

		TableColumnInformation c = getTableColumnInformation();
		info = new TableInfo(c.getClasses(), c.getHeaderNames(), c.getWidths(), c.getDbColumNames(),
				c.getHeaderToolTips());
		setTableInfo(info);
		return info;
	}

}
