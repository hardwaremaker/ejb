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
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.Icon;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.SperrenIcon;
import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.fertigung.fastlanereader.generated.FLRLos;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeLos;
import com.lp.server.system.jcr.service.docnode.DocPath;
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
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.DoppelIcon;

/**
 * <p>
 * Hier wird die FLR Funktionalitaet fuer die Lose implementiert.
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
public class LosHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String FLR_LOS = " flrlos.";
	private String sMaterialliste = "";
	boolean bKundeStattBezeichnung = false;
	boolean bBewertungAnzeigen = false;
	int bLosnummerAuftragsbezogen = 0;
	boolean bEndeStattBeginnTermin = false;
	boolean bAuftragStattArtikel = false;
	boolean bSuchenInklusiveKbez = true;
	boolean bZusatzbezeichnungInAuwahlliste = false;
	boolean bTextsucheInklusiveArtikelnummer = false;
	boolean bTextsucheInklusiveIndexRevision = false;

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = getLimit();
			// int startIndex = Math.max(rowIndex.intValue() - (pageSize / 2),
			// 0);
			int startIndex = getStartIndex(rowIndex, pageSize);
			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			session = setFilter(session);

			String queryString = "SELECT distinct flrlos, flrlos.flrauftrag.c_nr,flrlos.flrauftrag.c_bez, flrlos.flrstueckliste.flrartikel, aspr, partner.c_name1nachnamefirmazeile1 FROM FLRLos AS flrlos LEFT OUTER JOIN flrlos.flrstueckliste.flrartikel.artikelsprset AS aspr LEFT OUTER JOIN flrlos.flrauftrag.flrkunde.flrpartner AS partner LEFT OUTER JOIN flrlos.flrkunde.flrpartner AS partnerK  LEFT OUTER JOIN flrlos.flrauftrag AS auftrag LEFT OUTER JOIN flrlos.technikerset AS technikerset  LEFT OUTER JOIN flrlos.flrprojekt AS projekt  LEFT OUTER JOIN flrlos.flrauftrag.flrprojekt AS projekt_auftrag  "
					+ this.buildWhereClause() + this.buildOrderByClause();

			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();

			Object[][] rows = new Object[resultList.size()][colCount];

			String in = "";

			Map<Integer, String> m = getFertigungFac().getAllZusatzstatus(
					theClientDto);

			if (resultList.size() > 0) {
				in = " AND flrlos.i_id IN(";

				Iterator it = resultList.iterator();

				while (it.hasNext()) {

					Object[] lose = (Object[]) it.next();
					FLRLos flrLos = (FLRLos) lose[0];
					if (it.hasNext() == false) {
						in += flrLos.getI_id();
					} else {
						in += flrLos.getI_id() + ",";
					}
				}
				in += ") ";

				session.close();
				session = factory.openSession();
				session = setFilter(session);
				queryString = this.getFromClause() + this.buildWhereClause()
						+ in + this.buildOrderByClause();
				query = session.createQuery(queryString);
				resultList = query.list();

				if (resultList.size() > 50) {
					int i = 0;
				}
			} else {
				int k = 0;
			}
			Iterator<?> resultListIterator = resultList.iterator();
			int row = 0;
			int col = 0;

			while (resultListIterator.hasNext()) {
				Object o[] = (Object[]) resultListIterator.next();
				rows[row][col++] = o[0];

				rows[row][col++] = o[1];
				rows[row][col++] = new Integer(((BigDecimal) o[2]).intValue());

				if (bAuftragStattArtikel == true) {
					rows[row][col++] = o[14];
				} else {

					if (o[5] != null) {
						rows[row][col++] = o[5];
					} else {
						rows[row][col++] = sMaterialliste;
					}
				}

				if (bAuftragStattArtikel == true) {
					rows[row][col++] = o[15];
				} else {
					if (bKundeStattBezeichnung == true) {
						rows[row][col++] = o[11];
					} else {
						rows[row][col++] = o[6];

					}

					if (bZusatzbezeichnungInAuwahlliste) {
						rows[row][col++] = o[18];
					}

				}

				rows[row][col++] = o[4];
				String sStatus = (String) o[3];

				if (m.size() > 0) {

					DoppelIcon ia = new DoppelIcon();

					Object[] oIcon1 = getStatusMitUebersetzung(sStatus);

					if (oIcon1 != null) {

						if (oIcon1.length > 0) {
							ia.setIcon1((String) oIcon1[0]);
						}
						if (oIcon1.length > 1) {
							ia.setTooltip1((String) oIcon1[1]);
						}

					}
					// Juengsten Zusatzstatus holen

					Integer zusatzstatusIId = getFertigungFac()
							.getJuengstenZusatzstatuseinesLoses((Integer) o[0]);

					if (zusatzstatusIId != null) {
						m.get(zusatzstatusIId);

						String zusatzstatus = Helper.fitString2Length(
								m.get(zusatzstatusIId), 15, ' ');

						ia.setIcon2(zusatzstatus);
						ia.setTooltip2(zusatzstatus);
					}

					rows[row][col++] = ia;
				} else {

					rows[row][col++] = getStatusMitUebersetzung(sStatus);

				}

				if (bEndeStattBeginnTermin) {
					rows[row][col++] = o[13];
				} else {
					rows[row][col++] = o[8];
				}

				// erledigte Menge
				rows[row][col++] = o[9];

				

				if (bBewertungAnzeigen) {
					rows[row][col++] =o[19];
				}
				
				// Fehlmenge vorhanden
				if (o[10] != null && ((BigDecimal) o[10]).doubleValue() != 0) {
					rows[row][col++] = "F";
				} else {
					rows[row][col++] = "";
				}

				long lAnzahlZeitbuchungen = 0;
				if (o[16] != null) {
					lAnzahlZeitbuchungen = (Long) o[16];
				}

				if (o[12] != null && o[12].equals(new Short((short) 1))) {
					if (lAnzahlZeitbuchungen > 0) {
						rows[row][col++] = Color.MAGENTA;
					} else {
						rows[row][col++] = Color.RED;
					}

				} else {
					if (lAnzahlZeitbuchungen > 0) {
						rows[row][col++] = new Color(0, 153, 51);// GRUEN
					} else {
						rows[row][col++] = null;
					}
				}
				row++;
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

	protected long getRowCountFromDataBase() {
		long rowCount = 0;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			session = setFilter(session);
			String queryString = "SELECT COUNT(distinct flrlos) FROM com.lp.server.fertigung.fastlanereader.generated.FLRLos AS flrlos LEFT OUTER JOIN flrlos.flrstueckliste.flrartikel.artikelsprset AS aspr LEFT OUTER JOIN flrlos.flrauftrag AS auftrag LEFT OUTER JOIN flrlos.flrauftrag.flrkunde.flrpartner AS partner LEFT OUTER JOIN flrlos.flrkunde.flrpartner AS partnerK LEFT OUTER JOIN flrlos.flrauftrag AS auftrag  LEFT OUTER JOIN flrlos.technikerset AS technikerset  LEFT OUTER JOIN flrlos.flrprojekt AS projekt LEFT OUTER JOIN flrlos.flrauftrag.flrprojekt AS projekt_auftrag "
					+ this.buildWhereClause();
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
			// das brauch ich oefters
			final String sMaterialliste = getTextRespectUISpr(
					"fert.materialliste", theClientDto.getMandant(),
					theClientDto.getLocUi());

			for (int i = 0; i < filterKriterien.length; i++) {
				if (filterKriterien[i].isKrit) {
					if (filterAdded) {
						where.append(" " + booleanOperator);
					}
					filterAdded = true;
					// damit man auch nach Materiallisten suchen kann
					if (filterKriterien[i].kritName.equals("flrlos."
							+ FertigungFac.FLR_LOS_FLRSTUECKLISTE + "."
							+ StuecklisteFac.FLR_STUECKLISTE_FLRARTIKEL
							+ ".c_nr")) {
						if (filterKriterien[i].value.replaceAll("%", "")
								.replaceAll("'", "").equals(sMaterialliste)) {
							filterKriterien[i].kritName = "flrlos."
									+ FertigungFac.FLR_LOS_FLRSTUECKLISTE;
							filterKriterien[i].operator = FilterKriterium.OPERATOR_IS;
							filterKriterien[i].value = FilterKriterium.OPERATOR_NULL;
							filterKriterien[i].setBIgnoreCase(false);
						}
					}

					if (bSuchenInklusiveKbez
							&& filterKriterien[i].kritName
									.equals("flrlos.flrauftrag.flrkunde.flrpartner.c_name1nachnamefirmazeile1")) {

						where.append(" (lower(partnerK.c_name1nachnamefirmazeile1)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase());

						where.append(" OR lower(partner.c_name1nachnamefirmazeile1)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase());
						where.append(" OR lower(partnerK.c_name2vornamefirmazeile2)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase());
						where.append(" OR lower(partner.c_name2vornamefirmazeile2)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase());
						where.append(" OR lower(partnerK.c_kbez)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase());
						where.append(" OR lower(partner.c_kbez)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase() + ")");
						continue;
					} else if (bSuchenInklusiveKbez == false
							&& filterKriterien[i].kritName
									.equals("flrlos.flrauftrag.flrkunde.flrpartner.c_name1nachnamefirmazeile1")) {

						where.append(" (lower(partnerK.c_name1nachnamefirmazeile1)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase());

						where.append(" OR lower(partner.c_name1nachnamefirmazeile1)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase());

						where.append(" OR lower(partnerK.c_name2vornamefirmazeile2)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase());
						where.append(" OR lower(partner.c_name2vornamefirmazeile2)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase() + ")");
						continue;
					}

					if (filterKriterien[i].kritName
							.equals(ArtikelFac.FLR_ARTIKELLISTE_C_VOLLTEXT)) {

						String suchstring = "lower(coalesce(aspr.c_bez,'')||' '||coalesce(aspr.c_kbez,'')||' '||coalesce(aspr.c_zbez,'')||' '||coalesce(aspr.c_zbez2,''))";

						if (bTextsucheInklusiveArtikelnummer) {
							suchstring += "||' '||lower(flrlos.flrstueckliste.flrartikel.c_nr)";
						}

						if (bTextsucheInklusiveIndexRevision) {
							suchstring += "||' '||lower(coalesce(flrlos.flrstueckliste.flrartikel.c_index,''))||' '||lower(coalesce(flrlos.flrstueckliste.flrartikel.c_revision,''))";
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
						continue;
					}

					// Belegnummern Filter
					if (filterKriterien[i].kritName.equals("VJ")) {
						where.append(FLR_LOS + "c_nr");
						where.append(" " + filterKriterien[i].operator);
						where.append(" " + filterKriterien[i].value);

					} else if (filterKriterien[i].kritName.equals("flrlos."
							+ FertigungFac.FLR_LOS_C_NR)) {
						try {

							FilterKriterium fkLocal = new FilterKriterium(
									filterKriterien[i].kritName,
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

									where.append(" OR ");
									fkLocal.kritName = "flrlos."
											+ FertigungFac.FLR_LOSREPORT_FLRAUFTRAG
											+ ".c_nr";
									whereAuftrag(where, fkLocal);
									where.append(")");
								}

							}

						} catch (Exception ex) {
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR,
									ex);
						}
					} else if (filterKriterien[i].kritName.equals("flrlos."
							+ FertigungFac.FLR_LOSREPORT_FLRAUFTRAG + ".c_nr")) {

						whereAuftrag(where, filterKriterien[i]);

					} else if (filterKriterien[i].kritName.equals("flrlos."
							+ FertigungFac.FLR_LOSREPORT_FLRAUFTRAG
							+ ".flrprojekt.c_nr")) {

						try {

							String sValue = super.buildWhereBelegnummer(
									filterKriterien[i], false);

							// Belegnummernsuche auch in "altem" Jahr, wenn im
							// neuen noch keines vorhanden ist
							if (!istBelegnummernInJahr("FLRProjekt", sValue)) {
								sValue = super.buildWhereBelegnummer(
										filterKriterien[i], true);
							}
							where.append(" (" + "projekt_auftrag.c_nr");
							where.append(" " + filterKriterien[i].operator);
							where.append(" " + sValue);

							where.append(" OR projekt.c_nr");
							where.append(" " + filterKriterien[i].operator);
							where.append(" " + sValue + "");

							where.append(")");

						} catch (Exception ex) {
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR,
									ex);
						}

					} else {
						if (filterKriterien[i].kritName.equals("locale_c_nr")) {
							where.append(" (" + FLR_LOS + "locale_c_nr"
									+ filterKriterien[i].operator
									+ filterKriterien[i].value
									+ " OR locale_c_nr IS NULL)");
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
										+ filterKriterien[i].value
												.toLowerCase());
							} else {
								where.append(" " + filterKriterien[i].value);
							}
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
							orderBy.append(kriterien[i].kritName);
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
				orderBy.append(FLR_LOS).append(FertigungFac.FLR_LOS_C_NR)
						.append(" DESC ");
				sortAdded = true;
			}
			if (orderBy.indexOf(FLR_LOS + FertigungFac.FLR_LOS_C_NR) < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" ").append(FLR_LOS)
						.append(FertigungFac.FLR_LOS_C_NR).append(" DESC");
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
		return "SELECT distinct flrlos.i_id , flrlos.c_nr, flrlos.n_losgroesse,flrlos.status_c_nr,flrlos.c_projekt, flrlos.flrstueckliste.flrartikel.c_nr, aspr.c_bez, aspr.c_zbez, "
				+ " flrlos.t_produktionsbeginn, "
				+ " (select sum(flrlosablieferung.n_menge) from FLRLosablieferung flrlosablieferung where flrlosablieferung.los_i_id = flrlos.i_id ),"
				+ " (select sum(flrfehlmenge.n_menge) FROM FLRFehlmenge flrfehlmenge WHERE flrfehlmenge.flrlossollmaterial.flrlos = flrlos.i_id AND flrfehlmenge.n_menge>0),"
				+ " partner.c_name1nachnamefirmazeile1,"
				+ " flrlos.flrauftrag.b_poenale,"
				+ " flrlos.t_produktionsende,auftrag.c_nr,auftrag.c_bez, (select count(z.i_id) FROM FLRZeitdaten z WHERE z.i_belegartid=flrlos.i_id AND z.c_belegartnr='"
				+ LocaleFac.BELEGART_LOS
				+ "') , partner.c_name1nachnamefirmazeile1, aspr.c_zbez, flrlos.f_bewertung "
				+ " FROM FLRLos AS flrlos "
				+ " LEFT OUTER JOIN flrlos.flrstueckliste.flrartikel.artikelsprset AS aspr "
				+ " LEFT OUTER JOIN flrlos.flrauftrag.flrkunde.flrpartner AS partner "
				+ " LEFT OUTER JOIN flrlos.flrkunde.flrpartner AS partnerK "
				+ " LEFT OUTER JOIN flrlos.flrauftrag AS auftrag "
				+ " LEFT OUTER JOIN flrlos.technikerset AS technikerset "
				+ " LEFT OUTER JOIN flrlos.flrprojekt AS projekt "
				+ " LEFT OUTER JOIN flrlos.flrauftrag.flrprojekt AS projekt_auftrag ";

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
				String queryString = "SELECT distinct flrlos, flrlos.flrauftrag.c_nr,flrlos.flrauftrag.c_bez, flrlos.flrstueckliste.flrartikel, aspr, partner.c_name1nachnamefirmazeile1 FROM FLRLos AS flrlos LEFT OUTER JOIN flrlos.flrstueckliste.flrartikel.artikelsprset AS aspr LEFT OUTER JOIN flrlos.flrauftrag.flrkunde.flrpartner AS partner LEFT OUTER JOIN flrlos.flrauftrag AS auftrag LEFT OUTER JOIN flrlos.flrkunde.flrpartner AS partnerK  LEFT OUTER JOIN flrlos.technikerset AS technikerset  LEFT OUTER JOIN flrlos.flrprojekt AS projekt  LEFT OUTER JOIN flrlos.flrauftrag.flrprojekt AS projekt_auftrag  "
						+ this.buildWhereClause() + this.buildOrderByClause();
				Query query = session.createQuery(queryString);
				ScrollableResults scrollableResult = query.scroll();
				if (scrollableResult != null) {
					scrollableResult.beforeFirst();
					while (scrollableResult.next()) {
						FLRLos id = (FLRLos) scrollableResult.get(0);
						if (selectedId.equals(id.getI_id())) {
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

	private TableColumnInformation createColumnInformation(String mandant,
			Locale locUi) {
		TableColumnInformation columns = new TableColumnInformation();

		columns.add("i_id", Integer.class, "i_id", -1, FLR_LOS + "i_id");

		columns.add("lp.losnr", String.class,
				getTextRespectUISpr("lp.losnr", mandant, locUi),
				QueryParameters.FLR_BREITE_M, FLR_LOS + "c_nr");
		columns.add("lp.menge", Integer.class,
				getTextRespectUISpr("lp.menge", mandant, locUi), 8, FLR_LOS
						+ FertigungFac.FLR_LOS_N_LOSGROESSE);

		if (bAuftragStattArtikel) {

			columns.add("lp.auftrag", String.class,
					getTextRespectUISpr("lp.auftrag", mandant, locUi),
					QueryParameters.FLR_BREITE_M, FLR_LOS
							+ FertigungFac.FLR_LOSREPORT_FLRAUFTRAG + ".c_nr");

			columns.add(
					"lp.projektausauftrag",
					String.class,
					getTextRespectUISpr("lp.projektausauftrag", mandant, locUi),
					QueryParameters.FLR_BREITE_M, FLR_LOS
							+ FertigungFac.FLR_LOSREPORT_FLRAUFTRAG + ".c_bez");

		} else {

			columns.add("lp.artikelnummer", String.class,
					getTextRespectUISpr("lp.artikelnummer", mandant, locUi),
					QueryParameters.FLR_BREITE_SHARE_WITH_REST, FLR_LOS
							+ "flrstueckliste.flrartikel.c_nr");

			if (bKundeStattBezeichnung) {
				columns.add("lp.kunde", String.class,
						getTextRespectUISpr("lp.kunde", mandant, locUi),
						QueryParameters.FLR_BREITE_SHARE_WITH_REST,
						"partner.c_name1nachnamefirmazeile1");
			} else {
				columns.add("lp.bezeichnung", String.class,
						getTextRespectUISpr("lp.bezeichnung", mandant, locUi),
						QueryParameters.FLR_BREITE_SHARE_WITH_REST,
						"aspr.c_bez");
			}

			if (bZusatzbezeichnungInAuwahlliste) {
				columns.add(
						"artikel.zusatzbez",
						String.class,
						getTextRespectUISpr("artikel.zusatzbez", mandant, locUi),
						QueryParameters.FLR_BREITE_SHARE_WITH_REST,
						"aspr.c_zbez");
			}

		}

		columns.add("fert.los.projekt", String.class,
				getTextRespectUISpr("fert.los.projekt", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, FLR_LOS
						+ FertigungFac.FLR_LOS_C_PROJEKT);

		columns.add("lp.status", Icon.class,
				getTextRespectUISpr("lp.status", mandant, locUi),
				QueryParameters.FLR_BREITE_XS, FLR_LOS
						+ FertigungFac.FLR_LOS_STATUS_C_NR);

		if (bEndeStattBeginnTermin) {
			columns.add("lp.ende", Date.class,
					getTextRespectUISpr("lp.ende", mandant, locUi),
					QueryParameters.FLR_BREITE_M, FLR_LOS
							+ FertigungFac.FLR_LOS_T_PRODUKTIONSENDE);

		} else {
			columns.add("lp.beginn", Date.class,
					getTextRespectUISpr("lp.beginn", mandant, locUi),
					QueryParameters.FLR_BREITE_M, FLR_LOS
							+ FertigungFac.FLR_LOS_T_PRODUKTIONSBEGINN);
		}

		columns.add("lp.fertig", BigDecimal.class,
				getTextRespectUISpr("lp.fertig", mandant, locUi), 8,
				Facade.NICHT_SORTIERBAR);

		if (bBewertungAnzeigen) {
			columns.add("fert.bewertung", Double.class,
					getTextRespectUISpr("fert.bewertung", mandant, locUi),
					QueryParameters.FLR_BREITE_XS, FLR_LOS
							+ FertigungFac.FLR_LOS_F_BEWERTUNG);
		}

		columns.add("", String.class, "", 1, Facade.NICHT_SORTIERBAR);
		columns.add("Color", Color.class, "", 1, "");

		return columns;
	}

	private void setupParameters() {
		try {
			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_FERTIGUNG,
							ParameterFac.PARAMETER_KUNDE_STATT_BEZEICHNUNG_IN_AUSWAHLLISTE);
			bKundeStattBezeichnung = (Boolean) parameter.getCWertAsObject();
			parameter = getParameterFac().getMandantparameter(
					theClientDto.getMandant(),
					ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_LOSNUMMER_AUFTRAGSBEZOGEN);
			bLosnummerAuftragsbezogen = (Integer) parameter.getCWertAsObject();

			parameter = getParameterFac().getMandantparameter(
					theClientDto.getMandant(),
					ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_ZUSATZBEZEICHNUNG_IN_AUSWAHLLISTE);
			bZusatzbezeichnungInAuwahlliste = (Boolean) parameter
					.getCWertAsObject();

			parameter = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_FERTIGUNG,
							ParameterFac.PARAMETER_ENDE_TERMIN_ANSTATT_BEGINN_TERMIN_ANZEIGEN);
			bEndeStattBeginnTermin = (Boolean) parameter.getCWertAsObject();
			parameter = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_FERTIGUNG,
							ParameterFac.PARAMETER_AUFTRAG_STATT_ARTIKEL_IN_AUSWAHLLISTE);
			bAuftragStattArtikel = (Boolean) parameter.getCWertAsObject();

			parameter = getParameterFac().getMandantparameter(
					theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_SUCHEN_INKLUSIVE_KBEZ);
			bSuchenInklusiveKbez = (java.lang.Boolean) parameter
					.getCWertAsObject();

			parameter = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_TEXTSUCHE_INKLUSIVE_ARTIKELNUMMER);
			bTextsucheInklusiveArtikelnummer = (java.lang.Boolean) parameter
					.getCWertAsObject();

			parameter = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_TEXTSUCHE_INKLUSIVE_INDEX_REVISION);
			bTextsucheInklusiveIndexRevision = (java.lang.Boolean) parameter
					.getCWertAsObject();
			parameter = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_BEWERTUNG_IN_AUSWAHLLISTE);
			bBewertungAnzeigen = (java.lang.Boolean) parameter
					.getCWertAsObject();

		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}
	}

	public TableInfo getTableInfo() {
		TableInfo info = super.getTableInfo();
		if (info != null)
			return info;

		setupParameters();
		setTableColumnInformation(createColumnInformation(
				theClientDto.getMandant(), theClientDto.getLocUi()));

		TableColumnInformation c = getTableColumnInformation();
		info = new TableInfo(c.getClasses(), c.getHeaderNames(), c.getWidths(),
				c.getDbColumNames());
		setTableInfo(info);
		return info;
	}

	public PrintInfoDto getSDocPathAndPartner(Object key) {
		LosDto losDto = null;
		try {
			losDto = getFertigungFac().losFindByPrimaryKey((Integer) key);
		} catch (Exception e) {
			// Nicht gefunden
		}
		if (losDto != null) {
			// String sPath = JCRDocFac.HELIUMV_NODE + "/"
			// + theClientDto.getMandant() + "/"
			// + LocaleFac.BELEGART_LOS.trim() + "/"
			// + LocaleFac.BELEGART_LOS.trim() + "/"
			// + losDto.getCNr().replace("/", ".");
			DocPath docPath = new DocPath(new DocNodeLos(losDto));
			Integer iPartnerIId = null;
			if (losDto != null) {
				iPartnerIId = losDto.getPartnerIIdFertigungsort();
			}
			return new PrintInfoDto(docPath, iPartnerIId, getSTable());
		} else {
			return null;
		}
	}

	public String getSTable() {
		return "LOS";
	}
}
