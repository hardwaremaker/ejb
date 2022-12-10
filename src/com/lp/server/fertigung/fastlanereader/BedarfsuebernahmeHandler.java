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

import java.awt.Color;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.Icon;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelfehlmengeDto;
import com.lp.server.fertigung.fastlanereader.generated.FLRBedarfsuebernahme;
import com.lp.server.fertigung.fastlanereader.generated.FLRLosistmaterial;
import com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.system.pkgenerator.format.LpBelegnummerFormat;
import com.lp.server.system.pkgenerator.format.LpDefaultBelegnummerFormat;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
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
import com.lp.server.util.report.JasperPrintLP;
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
public class BedarfsuebernahmeHandler extends UseCaseHandler {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String FLR_BEDARFSUEBERNAHME = "flrbedarfsuebernahme.";
	private static final String FLR_BEDARFSUEBERNAHME_FROM_CLAUSE = " from FLRBedarfsuebernahme flrbedarfsuebernahme LEFT OUTER JOIN flrbedarfsuebernahme.flrartikelliste.artikelsprset AS aspr ";

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
			String queryString = "SELECT flrbedarfsuebernahme,(SELECT distinct s.artikel_i_id FROM FLRArtikelsperren as s WHERE s.artikel_i_id=flrbedarfsuebernahme.flrartikel.i_id) as sperren  from FLRBedarfsuebernahme flrbedarfsuebernahme  LEFT OUTER JOIN flrbedarfsuebernahme.flrartikelliste.artikelsprset AS aspr  "

					+ this.buildWhereClause() + this.buildOrderByClause();
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(PAGE_SIZE);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;
			int col = 0;
			while (resultListIterator.hasNext()) {
				Object[] o = (Object[]) resultListIterator.next();
				FLRBedarfsuebernahme losmat = (FLRBedarfsuebernahme) o[0];

				rows[row][col++] = losmat.getI_id();

				rows[row][col++] = losmat.getFlrpersonal_anlegen()
						.getC_kurzzeichen();

				if (losmat.getLos_i_id() != null) {
					rows[row][col++] = losmat.getFlrlos().getC_nr();
				} else {
					rows[row][col++] = losmat.getC_losnummer();
				}

				if (losmat.getArtikel_i_id() != null) {
					rows[row][col++] = losmat.getFlrartikel().getC_nr();

					ArtikelDto aDto = getArtikelFac()
							.artikelFindByPrimaryKeySmall(
									losmat.getFlrartikel().getI_id(),
									theClientDto);

					if (aDto.getArtikelsprDto() != null) {
						rows[row][col++] = aDto.getArtikelsprDto().getCBez();
					} else {
						rows[row][col++] = null;
					}

				} else {
					rows[row][col++] = losmat.getC_artikelnummer();
					rows[row][col++] = losmat.getC_artikelbezeichnung();
				}

				rows[row][col++] = losmat.getN_wunschmenge();
				rows[row][col++] = losmat.getT_wunschtermin();

				rows[row][col++] = getStatusMitUebersetzung(
						losmat.getStatus_c_nr(),
						losmat.getT_verbucht_gedruckt(),
						JasperPrintLP.DRUCKTYP_DRUCKER);

				if (o[1] != null) {
					rows[row][col++] = LocaleFac.STATUS_GESPERRT;
				} else {
					rows[row][col++] = null;
				}

				row++;
				col = 0;
			}
			result = new QueryResult(rows, this.getRowCount(), startIndex,
					endIndex, 0);
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
			String queryString = "select count(*) " + this.getFromClause()
					+ this.buildWhereClause();
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

					if (filterKriterien[i].kritName
							.equals("flrbedarfsuebernahme.flrlos."
									+ FertigungFac.FLR_LOS_C_NR)) {
						try {

							FilterKriterium fkLocal = new FilterKriterium(
									filterKriterien[i].kritName.replace(
											"flroffeneags.", ""),
									filterKriterien[i].isKrit,
									filterKriterien[i].value,
									filterKriterien[i].operator,
									filterKriterien[i].isBIgnoreCase());

							LpBelegnummerFormat f = getBelegnummerGeneratorObj()
									.getBelegnummernFormat(
											theClientDto.getMandant());
							LpDefaultBelegnummerFormat defaultFormat = (LpDefaultBelegnummerFormat) f;

							String postfix = "-___";
							boolean bPostfixVorhanden = false;
							if (fkLocal.value != null
									&& fkLocal.value.indexOf("-") > -1) {
								postfix = fkLocal.value.substring(fkLocal.value
										.indexOf("-"));
								fkLocal.value = fkLocal.value.substring(0,
										fkLocal.value.indexOf("-")) + "'";
								bPostfixVorhanden = true;
							}

							String sValue = super.buildWhereBelegnummer(
									fkLocal, false);

							if (bLosnummerAuftragsbezogen >= 1) {
								if (sValue != null) {
									sValue = sValue.substring(0, defaultFormat
											.getStellenGeschaeftsjahr() + 2)
											+ "_"
											+ sValue.substring(defaultFormat
													.getStellenGeschaeftsjahr() + 3);
								}
							}

							// Belegnummernsuche auch in "altem" Jahr, wenn im
							// neuen noch keines vorhanden ist
							if (!istBelegnummernInJahr("FLRLos", sValue)) {
								sValue = super.buildWhereBelegnummer(fkLocal,
										true);

								if (bLosnummerAuftragsbezogen >= 1) {
									if (sValue != null) {
										sValue = sValue
												.substring(
														0,
														defaultFormat
																.getStellenGeschaeftsjahr() + 2)
												+ "_"
												+ sValue.substring(defaultFormat
														.getStellenGeschaeftsjahr() + 3);
									}
								}

							}

							if (bLosnummerAuftragsbezogen == 0) {
								where.append(" " + fkLocal.kritName);
								where.append(" " + fkLocal.operator);
								where.append(" " + sValue + "");
							} else {

								int iStellenGesamtAuftragsbezogen = defaultFormat
										.getStellenGeschaeftsjahr()
										+ defaultFormat.getStellenLfdNummer()
										+ 5;

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

								String losnummer = super.buildWhereBelegnummer(
										fkLocal, false);
								String valueBelegnummernfilterAuftragsbezogen = losnummer
										.substring(0, defaultFormat
												.getStellenGeschaeftsjahr() + 2);
								postfix = postfix.replaceAll("'", "");
								String krit = null;
								if (fkLocal.value.indexOf(",") > 0) {
									krit = fkLocal.value
											.substring(0,
													fkLocal.value.indexOf(","))
											.replaceAll("%", "")
											.replaceAll("'", "")
											+ postfix;
								} else {
									krit = fkLocal.value.replaceAll("%", "")
											.replaceAll("'", "") + postfix;
								}

								valueBelegnummernfilterAuftragsbezogen = Helper
										.fitString2Length(
												valueBelegnummernfilterAuftragsbezogen,
												iStellenGesamtAuftragsbezogen
														- krit.length() + 1,
												'0')
										+ krit + "'";

								if (!istBelegnummernInJahr("FLRLos",
										valueBelegnummernfilterAuftragsbezogen)
										&& !istBelegnummernInJahr(
												"FLRLos",
												valueBelegnummernfilterAuftragsbezogen
														.replaceFirst("0", "")
														.substring(
																0,
																valueBelegnummernfilterAuftragsbezogen
																		.length() - 2)
														+ "_'")) {
									losnummer = super.buildWhereBelegnummer(
											fkLocal, true);
									valueBelegnummernfilterAuftragsbezogen = losnummer
											.substring(
													0,
													defaultFormat
															.getStellenGeschaeftsjahr() + 2);

									if (fkLocal.value.indexOf(",") > 0) {
										krit = fkLocal.value
												.substring(
														0,
														fkLocal.value
																.indexOf(","))
												.replaceAll("%", "")
												.replaceAll("'", "")
												+ postfix;
									} else {
										krit = fkLocal.value
												.replaceAll("%", "")
												.replaceAll("'", "")
												+ postfix;
									}

									valueBelegnummernfilterAuftragsbezogen = Helper
											.fitString2Length(
													valueBelegnummernfilterAuftragsbezogen,
													iStellenGesamtAuftragsbezogen
															- krit.length() + 1,
													'0')
											+ krit + "'";

								}

								// Wenn '-' vorhanden, dann nur nach
								// Auftragsbezogenen Nummer suchen
								if (bPostfixVorhanden) {
									where.append(" " + fkLocal.kritName);
									where.append(" " + fkLocal.operator);
									where.append(" "
											+ valueBelegnummernfilterAuftragsbezogen
											+ "");
								} else {
									where.append("(");
									where.append(" (" + fkLocal.kritName);
									where.append(" " + fkLocal.operator);
									where.append(" " + sValue + " OR ");
									where.append(" " + fkLocal.kritName);
									where.append(" " + fkLocal.operator);
									where.append(" "
											+ valueBelegnummernfilterAuftragsbezogen
											+ " OR ");
									where.append(" " + fkLocal.kritName);
									where.append(" " + fkLocal.operator);
									where.append(" "
											+ valueBelegnummernfilterAuftragsbezogen
													.replaceFirst("0", "")
													.substring(
															0,
															valueBelegnummernfilterAuftragsbezogen
																	.length() - 2)
											+ "_'" + ") ");

								
									where.append(")");
								}

							}

						} catch (Exception ex) {
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR,
									ex);
						}
					} else {

						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" lower(" + FLR_BEDARFSUEBERNAHME
									+ filterKriterien[i].kritName + ")");
						} else {

							where.append(" " + FLR_BEDARFSUEBERNAHME
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
	
	private int bLosnummerAuftragsbezogen = 0;
	
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
							if (!kriterien[i].kritName.startsWith("aspr")) {
								orderBy.append(FLR_BEDARFSUEBERNAHME
										+ kriterien[i].kritName);
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

				orderBy.append(" flrbedarfsuebernahme.flrpersonal_anlegen.c_kurzzeichen ASC, flrbedarfsuebernahme.t_aendern ASC ");

				sortAdded = true;
			}
			if (orderBy.indexOf("flrbedarfsuebernahme.t_aendern") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" ")
						.append("flrbedarfsuebernahme.t_aendern").append(" ");
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
		return FLR_BEDARFSUEBERNAHME_FROM_CLAUSE;
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
				String queryString = "select " + FLR_BEDARFSUEBERNAHME
						+ FertigungFac.FLR_LOSSOLLMATERIAL_I_ID
						+ FLR_BEDARFSUEBERNAHME_FROM_CLAUSE
						+ this.buildWhereClause() + this.buildOrderByClause();
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

	private void whereAuftrag(StringBuffer where,
			FilterKriterium filterKriterium) {
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

	
	public TableInfo getTableInfo() {
		if (super.getTableInfo() == null) {
			int iNachkommastellenMenge = 2;

			try {
				ParametermandantDto parameter = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_FERTIGUNG,
								ParameterFac.PARAMETER_LOSNUMMER_AUFTRAGSBEZOGEN);
				bLosnummerAuftragsbezogen = (Integer) parameter
						.getCWertAsObject();
			} catch (RemoteException ex) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
			}

			
			try {
				iNachkommastellenMenge = getMandantFac()
						.getNachkommastellenMenge(theClientDto.getMandant());

			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}

			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();
			setTableInfo(new TableInfo(
					new Class[] {
							Integer.class,
							String.class,
							String.class,
							String.class,
							String.class,
							super.getUIClassBigDecimalNachkommastellen(iNachkommastellenMenge),

							java.util.Date.class, Icon.class, Icon.class },
					new String[] {
							"i_id",
							getTextRespectUISpr(
									"fert.bedarfsuebernhame.person",
									mandantCNr, locUI),
							getTextRespectUISpr("lp.los", mandantCNr, locUI),
							getTextRespectUISpr("lp.artikelnummer", mandantCNr,
									locUI),
							getTextRespectUISpr("lp.bezeichnung", mandantCNr,
									locUI),
							getTextRespectUISpr(
									"fert.bedarfsuebernhame.wunschmenge",
									mandantCNr, locUI),
							getTextRespectUISpr(
									"fert.bedarfsuebernhame.wunschtermin",
									mandantCNr, locUI),
							getTextRespectUISpr("lp.status", mandantCNr, locUI),
							getTextRespectUISpr("fert.sperre", mandantCNr,
									locUI) }, new int[] {
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_XS,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_XS,
							QueryParameters.FLR_BREITE_S }, new String[] {
							"i_id", "flrpersonal_anlegen.c_kurzzeichen",
							Facade.NICHT_SORTIERBAR, Facade.NICHT_SORTIERBAR,
							Facade.NICHT_SORTIERBAR, "n_wunschmenge",
							"t_wunschtermin", "status_c_nr",
							Facade.NICHT_SORTIERBAR }));
		}
		return super.getTableInfo();
	}
}
