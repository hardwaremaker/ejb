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

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.Icon;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikellistespr;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.fertigung.fastlanereader.generated.FLRProduzieren;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.system.pkgenerator.format.LpBelegnummerFormat;
import com.lp.server.system.pkgenerator.format.LpDefaultBelegnummerFormat;
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
import com.lp.util.DoppelIcon;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * Hier wird die FLR Funktionalitaet fuer den Losarbeitsplan implementiert.
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
public class ProduzierenHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String FLR_OFFENEAGS = "flrProduzieren.";
/*
 	private static final String FLR_LOSMAT_FROM_CLAUSE = " from FLRProduzieren flrProduzieren "
			+ " LEFT OUTER JOIN flrProduzieren.flrlos.flrstueckliste.flrartikel.artikelsprset AS aspr_stueckliste "
			+ " LEFT OUTER JOIN flrProduzieren.flrlossollarbeitsplan AS sollarbeitsplan "
			+ " LEFT OUTER JOIN sollarbeitsplan.flrartikel AS flrartikel_sollarbeitsplan "
			+ " LEFT OUTER JOIN flrProduzieren.flrlos AS flrmaschine "
			+ " LEFT OUTER JOIN flrProduzieren.flrlos AS flrlos "
			+ " LEFT OUTER JOIN flrProduzieren.flrlos.flrstueckliste AS flrstueckliste "
			+ " LEFT OUTER JOIN flrProduzieren.flrlos.flrauftrag AS flrauftrag "
			+ " LEFT OUTER JOIN flrProduzieren.flrkunde AS kunde " 
			+ " LEFT OUTER JOIN kunde.flrpartner AS partner "
			+ " LEFT OUTER JOIN sollarbeitsplan.flrmaschine AS maschine "
			+ " LEFT OUTER JOIN maschine.flrmaschinengruppe AS maschinengruppe "
			+ " LEFT OUTER JOIN sollarbeitsplan.flrsollmaterial AS sollmaterial "
			+ " LEFT OUTER JOIN sollmaterial.flrartikel AS artikel_sollmaterial ";
*/
	private static final String FLR_LOSMAT_FROM_CLAUSE = " from FLRProduzieren flrProduzieren "
			+ " LEFT OUTER JOIN flrProduzieren.flrlos AS flrlos "
			+ " LEFT OUTER JOIN flrProduzieren.flrlossollarbeitsplan AS sollarbeitsplan "
			+ " LEFT OUTER JOIN sollarbeitsplan.flrartikel AS flrartikel_sollarbeitsplan "
			+ " LEFT OUTER JOIN flrlos.flrstueckliste AS flrstueckliste "
			+ " LEFT OUTER JOIN flrstueckliste.flrartikel.artikelsprset AS aspr_stueckliste "
//			+ " LEFT OUTER JOIN flrProduzieren.flrlos AS flrmaschine "
			+ " LEFT OUTER JOIN flrlos.flrauftrag AS flrauftrag "
			+ " LEFT OUTER JOIN flrProduzieren.flrkunde AS kunde " 
			+ " LEFT OUTER JOIN kunde.flrpartner AS partner "
			+ " LEFT OUTER JOIN sollarbeitsplan.flrmaschine AS maschine "
			+ " LEFT OUTER JOIN maschine.flrmaschinengruppe AS maschinengruppe "
			+ " LEFT OUTER JOIN sollarbeitsplan.flrsollmaterial AS sollmaterial "
			+ " LEFT OUTER JOIN sollmaterial.flrartikel AS artikel_sollmaterial ";

	private int bLosnummerAuftragsbezogen = 0;
	private boolean bPruefplan1 = false;

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;

		int colCount = getTableInfo().getColumnClasses().length;
		int pageSize = Integer.MAX_VALUE;
		int startIndex = getStartIndex(rowIndex, pageSize);
		int endIndex = startIndex + pageSize - 1;

		session = factory.openSession();
		session = setFilter(session);
		String queryString = this.getFromClause() + this.buildWhereClause() + this.buildOrderByClause();
		Query query = session.createQuery(queryString);
		query.setFirstResult(startIndex);
		query.setMaxResults(pageSize);
		List<?> resultList = query.list();
		Iterator<?> resultListIterator = resultList.iterator();
		Object[][] rows = new Object[resultList.size()][colCount + 1];
		int row = 0;
		int col = 0;

		Map<Integer, String> m = getFertigungFac().getAllZusatzstatus(theClientDto);

		Map<Integer, ArtikelDto> artikelDtoCache = new HashMap<Integer, ArtikelDto>();
		Map<Integer, BigDecimal> lagerstaende = new HashMap<Integer, BigDecimal>();

		while (resultListIterator.hasNext()) {

			Object[] o = (Object[]) resultListIterator.next();
			FLRProduzieren loszeit = (FLRProduzieren) o[0];

			FLRArtikellistespr artikellistespr_stueckliste = (FLRArtikellistespr) o[3];

			rows[row][col++] = loszeit.getLos_i_id();

			if (loszeit.getFlrkunde() != null) {
				rows[row][col++] = loszeit.getFlrkunde().getFlrpartner().getC_kbez();
				rows[row][col++] = loszeit.getFlrkunde().getC_abc();
			} else {
				rows[row][col++] = null;
				rows[row][col++] = null;
			}

			rows[row][col++] = loszeit.getFlrlos().getC_nr();
			rows[row][col++] = loszeit.getFlrlos().getN_losgroesse();

			if (loszeit.getFlrlos().getFlrstueckliste() != null) {
				rows[row][col++] = loszeit.getFlrlos().getFlrstueckliste().getFlrartikel().getC_nr();

				if (artikellistespr_stueckliste != null) {
					rows[row][col++] = artikellistespr_stueckliste.getC_bez();
				} else {
					rows[row][col++] = null;
				}

			} else {
				rows[row][col++] = null;
				rows[row][col++] = null;
			}

			if (loszeit.getFlrlossollarbeitsplan() != null) {
				rows[row][col++] = loszeit.getFlrlossollarbeitsplan().getI_arbeitsgangsnummer();
				rows[row][col++] = loszeit.getFlrlossollarbeitsplan().getFlrartikel().getC_nr();

				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(
						loszeit.getFlrlossollarbeitsplan().getFlrartikel().getI_id(), theClientDto);
				if (aDto.getArtikelsprDto() != null) {
					rows[row][col++] = aDto.getArtikelsprDto().getCBez();
				} else {
					rows[row][col++] = null;
				}

			} else {
				rows[row][col++] = null;
				rows[row][col++] = null;
				rows[row][col++] = null;
			}

			// Hier Gesamtdauer berechnen
			rows[row][col++] = loszeit.getN_dauer_offen_personal();

			try {
				if (m.size() > 0) {

					DoppelIcon ia = new DoppelIcon();

					Object[] oIcon1 = getStatusMitUebersetzung(loszeit.getFlrlos().getStatus_c_nr());

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
							.getJuengstenZusatzstatuseinesLoses(loszeit.getFlrlos().getI_id());

					if (zusatzstatusIId != null) {
						m.get(zusatzstatusIId);

						String zusatzstatus = Helper.fitString2Length(m.get(zusatzstatusIId), 15, ' ');

						ia.setIcon2(zusatzstatus);
						ia.setTooltip2(zusatzstatus);
					}

					rows[row][col++] = ia;
				} else {

					rows[row][col++] = getStatusMitUebersetzung(loszeit.getFlrlos().getStatus_c_nr());

				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			if (loszeit.getFlrlossollarbeitsplan() != null) {

				rows[row][col++] = loszeit.getT_agbeginn();
				rows[row][col++] = loszeit.getFlrlossollarbeitsplan().getI_maschinenversatz_ms();

				if (loszeit.getT_agbeginn() != null) {
					Calendar c = Calendar.getInstance();
					c.setTimeInMillis(loszeit.getT_agbeginn().getTime());

					rows[row][col++] = c.get(Calendar.WEEK_OF_YEAR) + "/" + Helper.berechneJahrDerKW(c);
				} else {
					rows[row][col++] = null;
				}

			} else {
				rows[row][col++] = null;
				rows[row][col++] = null;
				rows[row][col++] = null;
			}

			if (loszeit.getFlrlossollarbeitsplan() != null
					&& loszeit.getFlrlossollarbeitsplan().getFlrmaschine() != null) {
				rows[row][col++] = loszeit.getFlrlossollarbeitsplan().getFlrmaschine().getFlrmaschinengruppe()
						.getC_bez();
				rows[row][col++] = loszeit.getFlrlossollarbeitsplan().getFlrmaschine().getC_identifikationsnr();
				rows[row][col++] = loszeit.getFlrlossollarbeitsplan().getFlrmaschine().getC_bez();
			} else {
				rows[row][col++] = null;
				rows[row][col++] = null;
				rows[row][col++] = null;
			}

			rows[row][col++] = loszeit.getN_dauer_offen_maschine();

			if (bPruefplan1 == false) {
				if (loszeit.getFlrlossollarbeitsplan() != null
						&& loszeit.getFlrlossollarbeitsplan().getFlrsollmaterial() != null) {
					rows[row][col++] = loszeit.getFlrlossollarbeitsplan().getFlrsollmaterial().getFlrartikel()
							.getC_nr();
					ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(
							loszeit.getFlrlossollarbeitsplan().getFlrsollmaterial().getFlrartikel().getI_id(),
							theClientDto);
					if (aDto.getArtikelsprDto() != null) {
						rows[row][col++] = aDto.getArtikelsprDto().getCBez();
					} else {
						rows[row][col++] = null;
					}
					rows[row][col++] = loszeit.getFlrlossollarbeitsplan().getFlrsollmaterial().getN_menge();
				} else {
					rows[row][col++] = null;
					rows[row][col++] = null;
					rows[row][col++] = null;
				}
			}

			// Heute produzierbar

			try {
				LossollmaterialDto[] sollDtos = getFertigungFac().lossollmaterialFindByLosIId(loszeit.getLos_i_id());

				Boolean bProduzierbar = true;

				for (int i = 0; i < sollDtos.length; i++) {

					if (sollDtos[i].getArtikelIId() == 354) {
						int t = 0;
					}

					ArtikelDto aDto = null;
					if (artikelDtoCache.containsKey(sollDtos[i].getArtikelIId())) {
						aDto = artikelDtoCache.get(sollDtos[i].getArtikelIId());
					} else {
						aDto = getArtikelFac().artikelFindByPrimaryKeySmall(sollDtos[i].getArtikelIId(), theClientDto);
						artikelDtoCache.put(sollDtos[i].getArtikelIId(), aDto);
					}

					if (aDto.isLagerbewirtschaftet()) {
						BigDecimal lagerstand = null;
						if (lagerstaende.containsKey(aDto.getIId())) {
							lagerstand = lagerstaende.get(aDto.getIId());
						} else {
							lagerstand = getLagerFac().getLagerstandAllerLagerEinesMandanten(
									sollDtos[i].getArtikelIId(), false, theClientDto);
						}

						if (lagerstand.doubleValue() < sollDtos[i].getNMenge().doubleValue()) {
							bProduzierbar = false;

						}
						lagerstand = lagerstand.subtract(sollDtos[i].getNMenge());
						if (lagerstand.doubleValue() < 0) {
							lagerstand = BigDecimal.ZERO;
						}

						lagerstaende.put(aDto.getIId(), lagerstand);

					}

				}

				rows[row][col++] = bProduzierbar;

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			row++;
			col = 0;
		}

		result = new QueryResult(rows, getRowCount(), startIndex, endIndex, 0);

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

					if(filterKriterien[i].kritName.startsWith("flrProduzieren.flrlos")) {
						filterKriterien[i].kritName = filterKriterien[i].kritName.replace("flrProduzieren.", "");
					}

					if (filterKriterien[i].kritName.equals("MASCHINE_GRUPPE")) {

						if (filterKriterien[i].value.startsWith("M")) {

							where.append(" maschine.i_id=" + filterKriterien[i].value.substring(1));

						} else if (filterKriterien[i].value.startsWith("G")) {
							where.append(" maschine.maschinengruppe_i_id=" + filterKriterien[i].value.substring(1));
						}

					} else if (filterKriterien[i].kritName.equals(FertigungFac.ZUPRODUZIEREN_TEXTSUCHE)) {

						String suchstring = "coalesce(aspr_stueckliste.c_bez,'')||' '|| coalesce(aspr_stueckliste.c_kbez,'')||' '|| coalesce(aspr_stueckliste.c_zbez,'')||' '|| coalesce(aspr_stueckliste.c_zbez2,'')||' '||coalesce(flrlos.c_projekt,'')||' '||coalesce(flrlos.x_text,'')";

						String[] teile = filterKriterien[i].value.toLowerCase().split(" ");

						for (int p = 0; p < teile.length; p++) {

							if (teile[p].startsWith("-")) {
								where.append(" NOT ");

								teile[p] = teile[p].substring(1);

							}

							where.append(" lower(" + suchstring + ") like '%" + teile[p].toLowerCase() + "%'");
							if (p < teile.length - 1) {
								where.append(" AND ");
							}
						}

					} else

					if (filterKriterien[i].kritName.equals("flrmaschine.c_identifikationsnr")) {
						where.append(" (lower(flrmaschine.c_identifikationsnr)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" " + filterKriterien[i].value.toLowerCase());
						where.append(" OR lower(flrmaschine.c_bez)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" " + filterKriterien[i].value.toLowerCase() + ")");
					} else if (filterKriterien[i].kritName.equals("flroffeneags.flrlos." + FertigungFac.FLR_LOS_C_NR)) {
						try {

							FilterKriterium fkLocal = new FilterKriterium(
									filterKriterien[i].kritName.replace("flroffeneags.", ""), filterKriterien[i].isKrit,
									filterKriterien[i].value, filterKriterien[i].operator,
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
									fkLocal.kritName = FertigungFac.FLR_LOSREPORT_FLRAUFTRAG + ".c_nr";
									whereAuftrag(where, fkLocal);
									where.append(")");
								}

							}

						} catch (Exception ex) {
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, ex);
						}
					} else {

						if (filterKriterien[i].isBIgnoreCase()) {

							where.append(" lower(" + filterKriterien[i].kritName + ")");
						} else {
							where.append(" " + filterKriterien[i].kritName);
						}

						where.append(" " + filterKriterien[i].operator);
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" " + filterKriterien[i].value.toLowerCase());
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
				orderBy.append(FLR_OFFENEAGS).append("t_agbeginn ASC, sollarbeitsplan.i_maschinenversatz_ms ASC");
				sortAdded = true;
			}
			if (orderBy.indexOf("flrProduzieren.los_i_id") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" flrProduzieren.los_i_id ");
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
				String queryString = "select " + FLR_OFFENEAGS + "los_i_id" + FLR_LOSMAT_FROM_CLAUSE
						+ this.buildWhereClause() + this.buildOrderByClause();
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

	public TableInfo getTableInfo() {
		if (super.getTableInfo() == null) {
			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();

			int iNachkommastellenLosgroesse = 2;
			int iNachkommastellenMenge = 2;

			try {

				iNachkommastellenLosgroesse = getMandantFac().getNachkommastellenLosgroesse(theClientDto.getMandant());

				iNachkommastellenMenge = getMandantFac().getNachkommastellenMenge(theClientDto.getMandant());

				ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_LOSNUMMER_AUFTRAGSBEZOGEN);
				bLosnummerAuftragsbezogen = (Integer) parameter.getCWertAsObject();

				bPruefplan1 = getMandantFac()
						.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_PRUEFPLAN1, theClientDto);

			} catch (RemoteException ex) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
			}

			if (bPruefplan1 == false) {

				setTableInfo(new TableInfo(
						new Class[] { Integer.class, String.class, String.class, String.class,
								super.getUIClassBigDecimalNachkommastellen(iNachkommastellenLosgroesse), String.class,
								String.class, Integer.class, String.class, String.class, BigDecimal.class, Icon.class,
								java.util.Date.class, Integer.class, String.class, String.class, String.class,
								String.class, BigDecimal.class, String.class, String.class,
								super.getUIClassBigDecimalNachkommastellen(iNachkommastellenMenge), Boolean.class },
						new String[] { "i_id", getTextRespectUISpr("lp.kunde", mandantCNr, locUI), "ABC",
								getTextRespectUISpr("lp.losnr", mandantCNr, locUI),
								getTextRespectUISpr("fert.losgroesse", mandantCNr, locUI),
								getTextRespectUISpr("fert.offeneags.artikelnummerstkl", mandantCNr, locUI),
								getTextRespectUISpr("fert.offeneags.bezeichnungstkl", mandantCNr, locUI),
								getTextRespectUISpr("fert.ag", mandantCNr, locUI),
								getTextRespectUISpr("fert.offeneags.artikelnummertaetigkeit", mandantCNr, locUI),
								getTextRespectUISpr("fert.offeneags.bezeichnungtaetigkeit", mandantCNr, locUI)
										+ "Taetigkeit",
								getTextRespectUISpr("fert.dauergesamtoffen", mandantCNr, locUI),
								getTextRespectUISpr("lp.status", mandantCNr, locUI),
								getTextRespectUISpr("fert.agbeginn", mandantCNr, locUI),
								getTextRespectUISpr("fert.offeneags.versatzms", mandantCNr, locUI),
								getTextRespectUISpr("lp.kw", mandantCNr, locUI),

								getTextRespectUISpr("lp.maschinengruppe", mandantCNr, locUI),
								getTextRespectUISpr("is.maschine", mandantCNr, locUI),
								getTextRespectUISpr("lp.bezeichnung", mandantCNr, locUI),
								getTextRespectUISpr("fert.dauergesamtoffen", mandantCNr, locUI),
								getTextRespectUISpr("fert.offeneags.artikelnummermaterial", mandantCNr, locUI),
								getTextRespectUISpr("fert.offeneags.bezeichnungmaterial", mandantCNr, locUI),
								getTextRespectUISpr("lp.menge", mandantCNr, locUI),
								getTextRespectUISpr("fert.heuteproduzierbar.short", mandantCNr, locUI), },

						new int[] { QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, },
						new String[] { "los_i_id", "partner.c_kbez", "kunde.c_abc", "flrProduzieren.flrlos.c_nr",
								"flrProduzieren.flrlos.n_losgroesse", "flrstueckliste.flrartikel.c_nr",
								"aspr_stueckliste.c_bez",
								"sollarbeitsplan." + FertigungFac.FLR_LOSSOLLARBEITSPLAN_I_ARBEITSGANGNUMMER,
								"flrartikel_sollarbeitsplan.c_nr", Facade.NICHT_SORTIERBAR,
								"flrProduzieren.n_dauer_offen_personal", "flrProduzieren.flrlos.status_c_nr",
								"flrProduzieren.t_agbeginn", "sollarbeitsplan.i_maschinenversatz_ms",
								Facade.NICHT_SORTIERBAR,

								"maschinengruppe.c_bez", "maschine.c_identifikationsnr", "maschine.c_bez",
								"flrProduzieren.n_dauer_offen_maschine", "artikel_sollmaterial.c_nr",
								Facade.NICHT_SORTIERBAR, "sollmaterial.n_menge", Facade.NICHT_SORTIERBAR, },
						new String[] { null, null, null, null, null, null, null, null, null, null,
								getTextRespectUISpr("fert.offene.personalzeiten.tooltip", mandantCNr, locUI), null,
								null, null, null, null, null, null,
								getTextRespectUISpr("fert.offene.maschinenzeiten.tooltip", mandantCNr, locUI), null,
								null, null,
								getTextRespectUISpr("fert.heuteproduzierbar.tooltip", mandantCNr, locUI), }));

			} else {

				setTableInfo(new TableInfo(
						new Class[] { Integer.class, String.class, String.class, String.class,
								super.getUIClassBigDecimalNachkommastellen(iNachkommastellenLosgroesse), String.class,
								String.class, Integer.class, String.class, String.class, BigDecimal.class, Icon.class,
								java.util.Date.class, Integer.class, String.class, String.class, String.class,
								String.class, BigDecimal.class, Boolean.class },
						new String[] { "i_id", getTextRespectUISpr("lp.kunde", mandantCNr, locUI), "ABC",
								getTextRespectUISpr("lp.losnr", mandantCNr, locUI),
								getTextRespectUISpr("fert.losgroesse", mandantCNr, locUI),
								getTextRespectUISpr("fert.offeneags.artikelnummerstkl", mandantCNr, locUI),
								getTextRespectUISpr("fert.offeneags.bezeichnungstkl", mandantCNr, locUI),
								getTextRespectUISpr("fert.ag", mandantCNr, locUI),
								getTextRespectUISpr("fert.offeneags.artikelnummertaetigkeit", mandantCNr, locUI),
								getTextRespectUISpr("fert.offeneags.bezeichnungtaetigkeit", mandantCNr, locUI)
										+ "Taetigkeit",
								getTextRespectUISpr("fert.dauergesamtoffen", mandantCNr, locUI),
								getTextRespectUISpr("lp.status", mandantCNr, locUI),
								getTextRespectUISpr("fert.agbeginn", mandantCNr, locUI),
								getTextRespectUISpr("fert.offeneags.versatzms", mandantCNr, locUI),
								getTextRespectUISpr("lp.kw", mandantCNr, locUI),

								getTextRespectUISpr("lp.maschinengruppe", mandantCNr, locUI),
								getTextRespectUISpr("is.maschine", mandantCNr, locUI),
								getTextRespectUISpr("lp.bezeichnung", mandantCNr, locUI),
								getTextRespectUISpr("fert.dauergesamtoffen", mandantCNr, locUI),
								getTextRespectUISpr("fert.heuteproduzierbar.short", mandantCNr, locUI), },

						new int[] { QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, },
						new String[] { "los_i_id", "partner.c_kbez", "kunde.c_abc", "flrProduzieren.flrlos.c_nr",
								"flrProduzieren.flrlos.n_losgroesse", "flrstueckliste.flrartikel.c_nr",
								"aspr_stueckliste.c_bez",
								"sollarbeitsplan." + FertigungFac.FLR_LOSSOLLARBEITSPLAN_I_ARBEITSGANGNUMMER,
								"flrartikel_sollarbeitsplan.c_nr", Facade.NICHT_SORTIERBAR,
								"flrProduzieren.n_dauer_offen_personal", "flrProduzieren.flrlos.status_c_nr",
								"flrProduzieren.t_agbeginn", "sollarbeitsplan.i_maschinenversatz_ms",
								Facade.NICHT_SORTIERBAR,

								"maschinengruppe.c_bez", "maschine.c_identifikationsnr", "maschine.c_bez",
								"flrProduzieren.n_dauer_offen_maschine", Facade.NICHT_SORTIERBAR, },
						new String[] { null, null, null, null, null, null, null, null, null, null,
								getTextRespectUISpr("fert.offene.personalzeiten.tooltip", mandantCNr, locUI), null,
								null, null, null, null, null, null,
								getTextRespectUISpr("fert.offene.maschinenzeiten.tooltip", mandantCNr, locUI),
								getTextRespectUISpr("fert.heuteproduzierbar.tooltip", mandantCNr, locUI), }));
			}

		}

		return super.getTableInfo();
	}
}
