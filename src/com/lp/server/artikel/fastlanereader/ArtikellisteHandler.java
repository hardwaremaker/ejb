/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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
package com.lp.server.artikel.fastlanereader;

import java.awt.Color;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.Icon;

import org.apache.batik.svggen.font.table.HmtxTable;
import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikelsperren;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.LagerFac;
import com.lp.server.artikel.service.SperrenIcon;
import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeArtikel;
import com.lp.server.system.jcr.service.docnode.DocPath;
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

/**
 * <p>
 * Hier wird die FLR Funktionalit&auml;t f&uuml;r den Artikel implementiert. Pro UseCase
 * gibt es einen Handler.
 * </p>
 * <p>
 * Copright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum 2004-08-14
 * </p>
 * <p>
 * </p>
 * 
 * @author ck
 * @version 1.0
 */

public class ArtikellisteHandler extends UseCaseHandler {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean bAbmessungenStattZusatzbezeichnung = false;
	boolean bArtikelgruppeStattZusatzbezeichnung = false;
	boolean bVkPreisStattGestpreis = false;
	boolean bVkPreisLief1preis = false;
	boolean bTextsucheInklusiveArtikelnummer = false;
	private boolean bArtikelgruppeAnzeigen = false;
	private boolean bLagerplaetzeAnzeigen = false;
	private boolean bArtikelklasseAnzeigen = false;
	private boolean bKurzbezeichnungAnzeigen = false;
	private boolean bDarfPreiseSehen = false;

	int vkPreisliste = -1;

	// private TableColumnInformation columns;

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {

		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;

		Iterator resultListIterator = null;
		List resultList = null;

		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = getLimit();
			int startIndex = Math.max(rowIndex.intValue() - (pageSize / 2), 0);

			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			session = setFilter(session);

			String queryString = "SELECT artikelliste.i_id FROM FLRArtikelliste AS artikelliste "
					+ " LEFT OUTER JOIN artikelliste.artikellagerset AS alager "
					+ " LEFT OUTER JOIN artikelliste.flrgeometrie AS geo "
					+ " LEFT OUTER JOIN artikelliste.artikellieferantset AS artikellieferantset "
					+ " LEFT OUTER JOIN artikelliste.stuecklisten AS stuecklisten "
					+ " LEFT OUTER JOIN artikelliste.artikelsprset AS aspr "
					+ " LEFT OUTER JOIN artikelliste.flrartikelgruppe AS ag "
					+ " LEFT OUTER JOIN artikelliste.flrartikelklasse AS ak "
					+ this.buildWhereClause()
					+ this.buildGroupByClause()
					+ this.buildOrderByClause();

			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);

			resultList = query.list();
			Object[][] rows = new Object[resultList.size()][colCount];

			Integer[] iIds = new Integer[resultList.size()];

			iIds = (Integer[]) resultList.toArray(iIds);

			String in = "";
			String inArtikel_i_id = "";

			HashMap hmRabattsatzFixpreis = new HashMap();
			HashMap hmPreisbasis = new HashMap();
			HashMap<Integer, String> hmKommentarTooltip = new HashMap<Integer, String>();

			if (resultList.size() > 0) {
				in = " AND artikelliste.i_id IN(";
				inArtikel_i_id = " pl.artikel_i_id IN(";
				String inKommentar = " ko.artikelkommentar.artikel_i_id IN(";

				for (int i = 0; i < iIds.length; i++) {
					if (i == iIds.length - 1) {
						in += iIds[i];
						inArtikel_i_id += iIds[i];
						inKommentar += iIds[i];
					} else {
						in += iIds[i] + ",";
						inArtikel_i_id += iIds[i] + ",";
						inKommentar += iIds[i] + ",";
					}
				}
				in += ") ";
				inArtikel_i_id += ") ";
				inKommentar += ") ";

				// String ins = StringUtils.join(iIds, ",") ;

				session.close();
				session = factory.openSession();
				session = setFilter(session);
				queryString = this.getFromClause() + this.buildWhereClause()
						+ in + this.buildGroupByClause()
						+ this.buildOrderByClause();
				query = session.createQuery(queryString);
				resultList = query.list();

				Session sessionVkPreisBasis = factory.openSession();
				String rabattsatzFixpreis = "SELECT pl.artikel_i_id, pl.n_artikelstandardrabattsatz ,pl.n_artikelfixpreis FROM FLRVkpfartikelpreis AS pl  WHERE pl.vkpfartikelpreisliste_i_id="
						+ vkPreisliste
						+ " AND "
						+ inArtikel_i_id
						+ "  ORDER BY t_preisgueltigab DESC ";
				Query queryRabattsatzFixpreis = sessionVkPreisBasis
						.createQuery(rabattsatzFixpreis);

				List resultListRabattsatzFixpreis = queryRabattsatzFixpreis
						.list();

				Iterator resultListIteratorRabattsatzFixpreis = resultListRabattsatzFixpreis
						.iterator();
				while (resultListIteratorRabattsatzFixpreis.hasNext()) {
					Object[] o = (Object[]) resultListIteratorRabattsatzFixpreis
							.next();
					if (!hmRabattsatzFixpreis.containsKey(o[0])) {
						hmRabattsatzFixpreis.put(o[0], o);
					}

				}
				sessionVkPreisBasis.close();

				sessionVkPreisBasis = factory.openSession();
				String preisbasis;
				if (bVkPreisLief1preis) {
					preisbasis = "SELECT pl.artikel_i_id, pl.n_nettopreis FROM FLRArtikellieferant AS pl WHERE "
							+ inArtikel_i_id + " ORDER BY i_sort ASC ";
				} else {
					preisbasis = "SELECT pl.artikel_i_id, pl.n_verkaufspreisbasis FROM FLRVkpfartikelverkaufspreisbasis AS pl  WHERE "
							+ inArtikel_i_id
							+ " AND t_verkaufspreisbasisgueltigab <='"
							+ Helper.formatDateWithSlashes(new java.sql.Date(
									System.currentTimeMillis()))
							+ "'"
							+ "  ORDER BY t_verkaufspreisbasisgueltigab DESC ";
				}
				Query queryPreisbasis = sessionVkPreisBasis
						.createQuery(preisbasis);

				List resultListPreisbasis = queryPreisbasis.list();

				Iterator resultListIteratorPreisbasis = resultListPreisbasis
						.iterator();
				while (resultListIteratorPreisbasis.hasNext()) {
					Object[] o = (Object[]) resultListIteratorPreisbasis.next();
					if (!hmPreisbasis.containsKey(o[0])) {
						hmPreisbasis.put(o[0], o[1]);
					}

				}
				sessionVkPreisBasis.close();

				// PJ18025
				sessionVkPreisBasis = factory.openSession();
				String kommentare = "SELECT ko.artikelkommentar.artikel_i_id, ko.x_kommentar, ko.artikelkommentar.flrartikelkommentarart.c_nr FROM FLRArtikelkommentarspr AS ko WHERE "
						+ inKommentar
						+ " AND ko.locale='"
						+ theClientDto.getLocUiAsString()
						+ "' AND ko.artikelkommentar.flrartikelkommentarart.b_tooltip=1 ";

				Query queryKommentare = sessionVkPreisBasis
						.createQuery(kommentare);

				List resultListKommentare = queryKommentare.list();

				Iterator resultListIteratorKommentare = resultListKommentare
						.iterator();
				while (resultListIteratorKommentare.hasNext()) {
					Object[] o = (Object[]) resultListIteratorKommentare.next();

					if (o[2] != null) {
						String kommentar = "<b>" + (String) o[2] + ":</b>\n"
								+ (String) o[1];

						String kommentarVorhanden = "";
						if (hmKommentarTooltip.containsKey(o[0])) {
							kommentarVorhanden = hmKommentarTooltip.get(o[0])
									+ "<br><br>" + kommentar;
						} else {
							kommentarVorhanden = kommentar;
						}

						hmKommentarTooltip.put((Integer) o[0],
								kommentarVorhanden);
					}

				}
				sessionVkPreisBasis.close();

			}

			rows = new Object[resultList.size()][colCount];

			resultListIterator = resultList.iterator();

			int row = 0;

			String[] tooltipData = new String[resultList.size()];

			while (resultListIterator.hasNext()) {
				Object o[] = (Object[]) resultListIterator.next();

				Object[] rowToAddCandidate = new Object[colCount];

				rowToAddCandidate[getTableColumnInformation().getViewIndex(
						"i_id")] = o[0];
				rowToAddCandidate[getTableColumnInformation().getViewIndex(
						"artikel.artikelnummerlang")] = o[1];
				rowToAddCandidate[getTableColumnInformation().getViewIndex(
						"lp.stuecklistenart")] = o[13] != null ? ((String) o[13])
						.trim() : o[13];

				if (bKurzbezeichnungAnzeigen) {
					prepareKurzbezeichnung(o, rowToAddCandidate);
				}

				rowToAddCandidate[getTableColumnInformation().getViewIndex(
						"bes.artikelbezeichnung")] = o[2];

				if (bAbmessungenStattZusatzbezeichnung
						|| bArtikelgruppeStattZusatzbezeichnung) {
					if (bAbmessungenStattZusatzbezeichnung) {
						prepareAbmessungen(o, rowToAddCandidate);
					} else {
						prepareArtikelGruppeInAbmessung(o, rowToAddCandidate);
					}
				} else {
					rowToAddCandidate[getTableColumnInformation().getViewIndex(
							"artikel.zusatzbez")] = o[6];
				}

				if (bArtikelgruppeAnzeigen) {
					prepareArtikelGruppe(o, rowToAddCandidate);
				} else {
					if (bArtikelklasseAnzeigen) {
						prepareArtikelKlasse(o, rowToAddCandidate);
					}
				}

				if (bVkPreisStattGestpreis == true) {
					prepareVkPreis(hmRabattsatzFixpreis, hmPreisbasis, o,
							rowToAddCandidate);
				}

				if (o[4] != null && Helper.short2boolean((Short) o[5])) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex(
							"lp.lagerstand")] = o[3];
				} else {
					rowToAddCandidate[getTableColumnInformation().getViewIndex(
							"lp.lagerstand")] = new BigDecimal(0);
				}

				if (bLagerplaetzeAnzeigen) {
					prepareLagerplaetze((Integer) o[0], rowToAddCandidate);
				}

				if (bDarfPreiseSehen) {
					// Gestehungspreis holen
					BigDecimal gestehungspreis = (BigDecimal) o[4];
					if (gestehungspreis != null
							&& ((BigDecimal) rowToAddCandidate[getTableColumnInformation()
									.getViewIndex("lp.lagerstand")])
									.doubleValue() > 0) {
						gestehungspreis = gestehungspreis
								.divide(new BigDecimal(
										((BigDecimal) rowToAddCandidate[getTableColumnInformation()
												.getViewIndex("lp.lagerstand")])
												.doubleValue()), 4,
										BigDecimal.ROUND_HALF_EVEN);
					} else {
						// Projekt 10870: WH: Wenn kein Gestpreis zustandekommt,
						// dann Gestpreis des Hauptlagers anzeigen
						if (Helper.short2boolean((Short) o[5]) && o[8] != null) {
							gestehungspreis = (BigDecimal) o[8];
						} else {
							gestehungspreis = new BigDecimal(0);
						}
					}
					if (bVkPreisStattGestpreis == false) {
						rowToAddCandidate[getTableColumnInformation()
								.getViewIndex("lp.preis")] = gestehungspreis;
					}
				} else {
					rowToAddCandidate[getTableColumnInformation().getViewIndex(
							"lp.preis")] = new BigDecimal(0);
				}

				Long lAnzahlReklamationen = (Long) o[15];
				Boolean hatOffeneReklamationen = (lAnzahlReklamationen != null)
						&& lAnzahlReklamationen.intValue() > 0;

				FLRArtikelsperren as = (FLRArtikelsperren) o[7];

				if (as != null || hatOffeneReklamationen) {
					String gesperrt = null;

					if (as != null) {
						gesperrt = as.getFlrsperren().getC_bez();
					}

					if (hatOffeneReklamationen) {
						rowToAddCandidate[getTableColumnInformation()
								.getViewIndex("Icon")] = getStatusMitUebersetzung(
								gesperrt,
								new java.sql.Timestamp(System
										.currentTimeMillis()), "R");
					} else {
						rowToAddCandidate[getTableColumnInformation()
								.getViewIndex("Icon")] = gesperrt;
					}

				}

				if (!Helper.short2boolean((Short) o[18])) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex(
							"Color")] = new Color(0, 0, 255);
				}

				rows[row] = rowToAddCandidate;

				// PJ18025
				String tooltip = (String) hmKommentarTooltip.get(o[0]);
				if (tooltip != null) {
					String text = tooltip;
					text = text.replaceAll("\n", "<br>");
					text = "<html>" + text + "</html>";
					tooltipData[row] = text;
				}

				row++;
			}
			result = new QueryResult(rows, getRowCount(), startIndex, endIndex,
					0, tooltipData);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			closeSession(session);
		}
		return result;
	}

	protected void prepareArtikelGruppeInAbmessung(Object[] source,
			Object[] target) {
		target[getTableColumnInformation().getViewIndex(
				"lp.artikelgruppeInAbmessung")] = source[14];
	}

	protected void prepareArtikelGruppe(Object[] source, Object[] target) {
		target[getTableColumnInformation().getViewIndex("lp.artikelgruppe")] = source[14];
	}

	protected void prepareArtikelKlasse(Object[] source, Object[] target) {
		target[getTableColumnInformation().getViewIndex("lp.artikelklasse")] = source[17];
	}

	protected void prepareKurzbezeichnung(Object[] source, Object[] target) {
		target[getTableColumnInformation().getViewIndex("lp.kurzbezeichnung")] = source[16];
	}

	protected void prepareLagerplaetze(Integer artikelIId, Object[] target)
			throws RemoteException {
		// PJ 17762 Lagerplaetze manuell anzeigen
		target[getTableColumnInformation().getViewIndex("lp.lagerplatz")] = getLagerFac()
				.getLagerplaezteEinesArtikels(artikelIId, null);
	}

	protected void prepareVkPreis(HashMap hmRabattsatzFixpreis,
			HashMap hmPreisbasis, Object[] source, Object[] target) {
		// VK-Preis Berechnen
		BigDecimal vkPreis = null;

		BigDecimal vkPreisbasis = new BigDecimal(0);
		BigDecimal vkRabattsatz = new BigDecimal(0);
		BigDecimal vkFixpreis = new BigDecimal(0);

		if (hmRabattsatzFixpreis.containsKey(source[0])) {
			Object[] oTemp = (Object[]) hmRabattsatzFixpreis.get(source[0]);
			vkRabattsatz = (BigDecimal) oTemp[1];
			vkFixpreis = (BigDecimal) oTemp[2];
		}
		if (hmPreisbasis.containsKey(source[0])) {
			vkPreisbasis = (BigDecimal) hmPreisbasis.get(source[0]);
		}

		if (vkFixpreis != null) {
			vkPreis = vkFixpreis;
		} else {
			if (vkRabattsatz != null && vkPreisbasis != null) {
				BigDecimal bdRabattsumme = vkPreisbasis.multiply(vkRabattsatz
						.movePointLeft(2));
				vkPreis = vkPreisbasis.subtract(bdRabattsumme);
			} else {
				vkPreis = vkPreisbasis;
			}
		}
		target[getTableColumnInformation().getViewIndex("lp.preis")] = vkPreis;
	}

	protected void prepareAbmessungen(Object[] source, Object[] target) {

		// PJ18155
		// String abmessungen = (String) source[19];

		String abmessungen = "";

		// Breitetext
		if (source[9] != null) {
			abmessungen += source[9];
		}

		// Breite
		if (source[10] != null) {
			abmessungen += source[10] + "x";
		}
		if (source[11] != null) {
			abmessungen += source[11] + "x";
		}
		if (source[12] != null) {
			abmessungen += source[12];
		}

		// TODO: Eigentlich falsch wenn es keine Abmessungen gibt, aber ein
		// Breitetext vom Anwender erfasst wurde der mit "x" endet (ghp)
		target[getTableColumnInformation().getViewIndex("lp.abmessungen")] = StringUtils
				.stripEnd(abmessungen, "x");

		// if (abmessungen.endsWith("x")) {
		// abmessungen = abmessungen.substring(0, abmessungen.length() - 1);
		// }

		// target[4] = abmessungen;
	}

	protected long getRowCountFromDataBase() {
		String queryString = "SELECT  COUNT(distinct artikelliste.i_id) FROM com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste AS artikelliste LEFT OUTER JOIN artikelliste.artikelsprset AS aspr  LEFT OUTER JOIN artikelliste.artikellieferantset AS artikellieferantset LEFT OUTER JOIN artikelliste.flrgeometrie AS geo  LEFT OUTER JOIN artikelliste.flrartikelgruppe AS ag   LEFT OUTER JOIN artikelliste.flrartikelklasse AS ak "
				+ buildWhereClause();
		return getRowCountFromDataBaseByQuery(queryString);
	}

	/**
	 * builds the where clause of the HQL (Hibernate Query Language) statement
	 * using the current query.
	 * 
	 * @return the HQL where clause.
	 */

	private String buildWhereClause() {
		StringBuffer where = new StringBuffer("");

		where = new StringBuffer("");
		if (getQuery() != null && getQuery().getFilterBlock() != null
				&& getQuery().getFilterBlock().filterKrit != null) {

			FilterBlock filterBlock = getQuery().getFilterBlock();
			FilterKriterium[] filterKriterien = getQuery().getFilterBlock().filterKrit;
			String booleanOperator = filterBlock.boolOperator;
			boolean filterAdded = false;

			boolean bvolltext = false;

			for (int i = 0; i < filterKriterien.length; i++) {
				if (filterKriterien[i].isKrit) {
					if (filterAdded) {
						where.append(" " + booleanOperator);
					}
					filterAdded = true;
					if (filterKriterien[i].kritName.equals("artikelliste.c_nr")) {
						String s = "";
						s = filterKriterien[i].value.toLowerCase();
						filterKriterien[i].value = s;
					}
					if (filterKriterien[i].kritName.equals("c_volltext")) {
						filterKriterien[i].kritName = "aspr.c_bez";
					}
					if (filterKriterien[i].kritName.equals("aspr.c_bez")) {
						filterKriterien[i].kritName = "aspr.c_bez";
						String s = "";
						s = filterKriterien[i].value.toLowerCase();
						filterKriterien[i].value = s;
						bvolltext = true;
					} else {
						bvolltext = false;
					}
					if (bvolltext
							&& !filterKriterien[i].kritName
									.equals(ArtikelFac.FLR_ARTIKELLIEFERANT_C_ARTIKELNRLIEFERANT)) {

						String suchstring = "lower(coalesce(aspr.c_bez,'')||' '||coalesce(aspr.c_kbez,'')||' '||coalesce(aspr.c_zbez,'')||' '||coalesce(aspr.c_zbez2,''))";
						if (bAbmessungenStattZusatzbezeichnung) {
							suchstring += "||' '||lower(coalesce(geo.c_breitetext,'')||coalesce(cast(geo.f_breite as string),'')||case  WHEN geo.f_hoehe IS NULL THEN '' ELSE 'x' END||coalesce(cast(geo.f_hoehe as string),'')||case  WHEN geo.f_tiefe IS NULL THEN '' ELSE 'x' END||coalesce(cast(geo.f_tiefe as string),''))";
						}

						if (bTextsucheInklusiveArtikelnummer) {
							suchstring += "||' '||lower(artikelliste.c_nr)";
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

						where.append(")");

					} else if (filterKriterien[i].kritName
							.equals(ArtikelFac.FLR_ARTIKELLIEFERANT_C_ARTIKELNRLIEFERANT)) {
						where.append(" (lower(artikellieferantset."
								+ filterKriterien[i].kritName + ")");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase());
						where.append(" OR lower(artikellieferantset."
								+ ArtikelFac.FLR_ARTIKELLIEFERANT_C_BEZBEILIEFERANT
								+ ")");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase() + ")");
					} else if (filterKriterien[i].kritName.equals("akag")) {
						where.append(" (lower(artikelliste.flrartikelgruppe.c_nr)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase());
						where.append(" OR lower(artikelliste.flrartikelklasse.c_nr)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" "
								+ filterKriterien[i].value.toLowerCase() + ")");

					} else {
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" LOWER("
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
				where.insert(0, " WHERE ");
			}
		}

		return where.toString();
	}

	private String buildGroupByClause() {
		return " GROUP BY artikelliste.i_id,artikelliste.c_nr, aspr.c_bez, artikelliste.b_lagerbewirtschaftet, aspr.c_zbez, geo.c_breitetext, geo.f_breite , geo.f_hoehe , geo.f_tiefe, stuecklisten.stuecklisteart_c_nr, ag.c_nr, aspr.c_kbez,ak.c_nr,aspr.c_siwert ";
	}

	/**
	 * builds the HQL (Hibernate Query Language) order by clause using the sort
	 * criterias contained in the current query.
	 * 
	 * @return the HQL order by clause.
	 */
	private String buildOrderByClause() {
		StringBuffer orderBy = new StringBuffer("");
		if (getQuery() != null) {
			SortierKriterium[] kriterien = getQuery().getSortKrit();
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
				orderBy.append("artikelliste.c_nr ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("artikelliste.i_id") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" artikelliste.i_id ");
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
	private String getFromClause() throws Exception {
		return "SELECT artikelliste.i_id, artikelliste.c_nr, aspr.c_bez, (SELECT sum(artikellager.n_lagerstand) FROM FLRArtikellager AS artikellager WHERE artikellager.compId.artikel_i_id=artikelliste.i_id  AND artikellager.flrlager.b_konsignationslager=0 AND artikellager.flrlager.lagerart_c_nr NOT IN('"
				+ LagerFac.LAGERART_WERTGUTSCHRIFT
				+ "')),"
				+ " (SELECT sum(artikellager.n_lagerstand*artikellager.n_gestehungspreis) FROM FLRArtikellager AS artikellager WHERE artikellager.compId.artikel_i_id=artikelliste.i_id AND artikellager.flrlager.b_konsignationslager=0 AND artikellager.flrlager.lagerart_c_nr NOT IN('"
				+ LagerFac.LAGERART_WERTGUTSCHRIFT
				+ "')), artikelliste.b_lagerbewirtschaftet, aspr.c_zbez, (SELECT s FROM FLRArtikelsperren as s WHERE s.artikel_i_id=artikelliste.i_id AND s.i_sort=1) as sperren,(SELECT al.n_gestehungspreis FROM FLRArtikellager as al WHERE al.compId.lager_i_id="
				+ getLagerFac().getHauptlagerDesMandanten(theClientDto)
						.getIId()
				+ " AND al.compId.artikel_i_id=artikelliste.i_id) as gestpreishauptlager, geo.c_breitetext, geo.f_breite , geo.f_hoehe , geo.f_tiefe, stuecklisten.stuecklisteart_c_nr, ag.c_nr,(SELECT COUNT(*) FROM FLRReklamation r WHERE r.flrartikel.i_id=artikelliste.i_id AND r.status_c_nr='"
				+ LocaleFac.STATUS_ANGELEGT
				+ "'), aspr.c_kbez, ak.c_nr,artikelliste.b_lagerbewirtschaftet,lower(coalesce(geo.c_breitetext,'')||coalesce(cast(geo.f_breite as string),'')||case  WHEN geo.f_hoehe IS NULL THEN '' ELSE 'x' END||coalesce(cast(geo.f_hoehe as string),'')||case  WHEN geo.f_tiefe IS NULL THEN '' ELSE 'x' END||coalesce(cast(geo.f_tiefe as string),'')) "
				+ " FROM FLRArtikelliste AS artikelliste"
				+ " LEFT OUTER JOIN artikelliste.artikellieferantset AS artikellieferantset "
				+ " LEFT OUTER JOIN artikelliste.stuecklisten AS stuecklisten "
				+ " LEFT OUTER JOIN artikelliste.artikellagerset AS alager "
				+ " LEFT OUTER JOIN artikelliste.flrgeometrie AS geo "
				+ " LEFT OUTER JOIN artikelliste.artikelsprset AS aspr "
				+ " LEFT OUTER JOIN artikelliste.flrartikelgruppe AS ag "
				+ " LEFT OUTER JOIN artikelliste.flrartikelklasse AS ak ";

	}

	public Session setFilter(Session session) {
		session = super.setFilter(session);
		String sMandant = theClientDto.getMandant();
		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
				MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, theClientDto)) {
			session.enableFilter("filterMandant").setParameter("paramMandant",
					getSystemFac().getHauptmandant());
		} else {
			session.enableFilter("filterMandant").setParameter("paramMandant",
					sMandant);
		}
		return session;
	}

	public QueryResult sort(SortierKriterium[] sortierKriterien,
			Object selectedIdI) throws EJBExceptionLP {

		getQuery().setSortKrit(sortierKriterien);

		QueryResult result = null;
		int rowNumber = 0;
		ScrollableResults scrollableResult = null;
		if (selectedIdI instanceof Integer) {
			if (selectedIdI != null && ((Integer) selectedIdI).intValue() >= 0) {
				SessionFactory factory = FLRSessionFactory.getFactory();
				Session session = null;

				try {
					session = factory.openSession();
					session = setFilter(session);

					String queryString = null;
					try {

						queryString = "SELECT artikelliste.i_id FROM FLRArtikelliste AS artikelliste LEFT OUTER JOIN artikelliste.artikellagerset AS alager "
								+ " LEFT OUTER JOIN artikelliste.flrgeometrie AS geo "
								+ " LEFT OUTER JOIN artikelliste.stuecklisten AS stuecklisten "
								+ " LEFT OUTER JOIN artikelliste.artikellieferantset AS artikellieferantset "
								+ " LEFT OUTER JOIN artikelliste.artikelsprset AS aspr "
								+ " LEFT OUTER JOIN artikelliste.flrartikelgruppe AS ag "
								+ " LEFT OUTER JOIN artikelliste.flrartikelklasse AS ak "
								+ buildWhereClause()
								+ buildGroupByClause()
								+ buildOrderByClause();
					} catch (Exception ex) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, ex);
					}

					Query query = session.createQuery(queryString);
					scrollableResult = query.scroll();

					if (scrollableResult != null) {
						scrollableResult.beforeFirst();
						while (scrollableResult.next()) {
							Integer id = (Integer) scrollableResult
									.getInteger(0);
							if (selectedIdI.equals(id)) {
								rowNumber = scrollableResult.getRowNumber();
								break;
							}
						}
					}
				} catch (HibernateException e) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
				} finally {
					try {
						if (session != null)
							session.close();
					} catch (HibernateException he) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, he);
					}
				}
			}
		}
		if (rowNumber < 0
		// || rowNumber >= getRowCount()
		) {
			rowNumber = 0;
		}

		result = getPageAt(new Integer(rowNumber));
		result.setIndexOfSelectedRow(rowNumber);

		return result;
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

	private TableColumnInformation createColumnInformation(String mandant,
			Locale locUi) {
		TableColumnInformation columns = new TableColumnInformation();

		columns.add("i_id", Integer.class, "i_id", -1, "i_id");
		columns.add(
				"artikel.artikelnummerlang",
				String.class,
				getTextRespectUISpr("artikel.artikelnummerlang", mandant, locUi),
				QueryParameters.FLR_BREITE_L, "artikelliste.c_nr");
		columns.add("lp.stuecklistenart", String.class,
				getTextRespectUISpr("lp.stuecklistenart", mandant, locUi),
				QueryParameters.FLR_BREITE_S,
				"stuecklisten.stuecklisteart_c_nr");
		if (bKurzbezeichnungAnzeigen) {
			columns.add("lp.kurzbezeichnung", String.class,
					getTextRespectUISpr("lp.kurzbezeichnung", mandant, locUi),
					QueryParameters.FLR_BREITE_XM, "aspr.c_kbez");
		}
		columns.add("bes.artikelbezeichnung", String.class,
				getTextRespectUISpr("bes.artikelbezeichnung", mandant, locUi),
				QueryParameters.FLR_BREITE_XL, "aspr.c_bez");

		if (bAbmessungenStattZusatzbezeichnung
				|| bArtikelgruppeStattZusatzbezeichnung) {
			/*
			 * Unsauber Definition. Mit den Parametern ist es m&ouml;glich beide
			 * Variante zu setzen. Es soll aber nur eine von beiden
			 * funktionieren. So programmiert, dass bei beiden immer die
			 * Abmessung gewinnt.
			 */
			if (bAbmessungenStattZusatzbezeichnung) {
				columns.add("lp.abmessungen", String.class,
						getTextRespectUISpr("lp.abmessungen", mandant, locUi),
						QueryParameters.FLR_BREITE_SHARE_WITH_REST,
						"aspr.c_zbez");
			} else {
				columns.add(
						"lp.artikelgruppeInAbmessung",
						String.class,
						getTextRespectUISpr("lp.artikelgruppe", mandant, locUi),
						QueryParameters.FLR_BREITE_SHARE_WITH_REST, "ag.c_nr");
			}
		} else {
			columns.add("artikel.zusatzbez", String.class,
					getTextRespectUISpr("artikel.zusatzbez", mandant, locUi),
					QueryParameters.FLR_BREITE_SHARE_WITH_REST, "aspr.c_zbez");
		}

		if (bArtikelgruppeAnzeigen) {
			columns.add("lp.artikelgruppe", String.class,
					getTextRespectUISpr("lp.artikelgruppe", mandant, locUi),
					QueryParameters.FLR_BREITE_L, "ag.c_nr");
		} else {
			if (bArtikelklasseAnzeigen) {
				columns.add(
						"lp.artikelklasse",
						String.class,
						getTextRespectUISpr("lp.artikelklasse", mandant, locUi),
						QueryParameters.FLR_BREITE_L, "ak.c_nr");
			}
		}

		if (bLagerplaetzeAnzeigen) {
			columns.add("lp.lagerplatz", String.class,
					getTextRespectUISpr("lp.lagerplatz", mandant, locUi),
					QueryParameters.FLR_BREITE_SHARE_WITH_REST,
					Facade.NICHT_SORTIERBAR);
		}

		columns.add("lp.lagerstand", BigDecimal.class,
				getTextRespectUISpr("lp.lagerstand", mandant, locUi),
				QueryParameters.FLR_BREITE_M, Facade.NICHT_SORTIERBAR);

		columns.add(
				"lp.preis",
				BigDecimal.class,
				getTextRespectUISpr(bVkPreisStattGestpreis ? "lp.vkpreis"
						: "lp.gestpreis", mandant, locUi),
				QueryParameters.FLR_BREITE_M, Facade.NICHT_SORTIERBAR);

		columns.add("Icon", SperrenIcon.class, "S",
				QueryParameters.FLR_BREITE_XS, Facade.NICHT_SORTIERBAR);
		columns.add("Color", Color.class, "", 1, "");
		return columns;
	}

	private Boolean getBooleanParameter(String category, String parameter)
			throws RemoteException {
		ParametermandantDto param = getParameterFac().getMandantparameter(
				theClientDto.getMandant(), category, parameter);
		return (Boolean) param.getCWertAsObject();
	}

	private void setupParameters() {
		try {
			bAbmessungenStattZusatzbezeichnung = getBooleanParameter(
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ABMESSUNGEN_STATT_ZUSATZBEZ_IN_AUSWAHLLISTE);
			bTextsucheInklusiveArtikelnummer = getBooleanParameter(
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_TEXTSUCHE_INKLUSIVE_ARTIKELNUMMER);

			bArtikelgruppeStattZusatzbezeichnung = getBooleanParameter(
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ARTIKELGRUPPE_STATT_ZUSATZBEZ_IN_AUSWAHLLISTE);

			bVkPreisStattGestpreis = getBooleanParameter(
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_VKPREIS_STATT_GESTPREIS_IN_ARTIKELAUSWAHL);

			bVkPreisLief1preis = getBooleanParameter(
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_VKPREISBASIS_IST_LIEF1PREIS);

			bArtikelgruppeAnzeigen = getBooleanParameter(
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ANZEIGEN_ARTIKELGRUPPE_IN_AUSWAHLLISTE);
			bArtikelklasseAnzeigen = getBooleanParameter(
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ANZEIGEN_ARTIKELKLASSE_IN_AUSWAHLLISTE);
			bKurzbezeichnungAnzeigen = getBooleanParameter(
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ANZEIGEN_KURZBEZEICHNUNG_IN_AUSWAHLLISTE);
			bLagerplaetzeAnzeigen = getBooleanParameter(
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ANZEIGEN_LAGERPLATZ_IN_AUSWAHLLISTE);

			VkpfartikelpreislisteDto[] vkpfartikelpreislisteDtos = getVkPreisfindungFac()
					.vkpfartikelpreislisteFindByMandantCNr(
							theClientDto.getMandant());

			if (vkpfartikelpreislisteDtos != null
					&& vkpfartikelpreislisteDtos.length > 0) {
				vkPreisliste = vkpfartikelpreislisteDtos[0].getIId();
			}

			bDarfPreiseSehen = getBenutzerServicesFac().hatRecht(
					RechteFac.RECHT_LP_DARF_PREISE_SEHEN_EINKAUF, theClientDto);

		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}
	}

	public TableInfo old_getTableInfo() {
		if (super.getTableInfo() == null) {

			setupParameters();

			if (bAbmessungenStattZusatzbezeichnung == true) {

				setTableInfo(new TableInfo(
						new Class[] { Integer.class, String.class,
								String.class, String.class, String.class,
								BigDecimal.class, BigDecimal.class,
								SperrenIcon.class, }, new String[] {
								"i_id",
								getTextRespectUISpr(
										"artikel.artikelnummerlang",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.stuecklistenart",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("bes.artikelbezeichnung",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.abmessungen",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.lagerstand",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.gestpreis",
										theClientDto.getMandant(),
										theClientDto.getLocUi()), "S" },
						new int[] {
								-1, // diese Spalte wird ausgeblendet
								QueryParameters.FLR_BREITE_L,
								QueryParameters.FLR_BREITE_S,
								QueryParameters.FLR_BREITE_XL,
								QueryParameters.FLR_BREITE_SHARE_WITH_REST,
								QueryParameters.FLR_BREITE_M,
								QueryParameters.FLR_BREITE_M,
								QueryParameters.FLR_BREITE_XS }, new String[] {
								"i_id", "artikelliste.c_nr",
								"stuecklisten.stuecklisteart_c_nr",
								"aspr.c_bez", "aspr.c_zbez",
								Facade.NICHT_SORTIERBAR,
								Facade.NICHT_SORTIERBAR,
								Facade.NICHT_SORTIERBAR }));
			} else {

				if (bVkPreisStattGestpreis == true) {

					setTableInfo(new TableInfo(
							new Class[] { Integer.class, String.class,
									String.class, String.class, String.class,
									BigDecimal.class, BigDecimal.class,
									Icon.class, },
							new String[] {
									"i_id",
									getTextRespectUISpr(
											"artikel.artikelnummerlang",
											theClientDto.getMandant(),
											theClientDto.getLocUi()),
									getTextRespectUISpr("lp.stuecklistenart",
											theClientDto.getMandant(),
											theClientDto.getLocUi()),
									getTextRespectUISpr(
											"bes.artikelbezeichnung",
											theClientDto.getMandant(),
											theClientDto.getLocUi()),

									bArtikelgruppeStattZusatzbezeichnung ? getTextRespectUISpr(
											"lp.artikelgruppe",
											theClientDto.getMandant(),
											theClientDto.getLocUi())
											: getTextRespectUISpr(
													"artikel.zusatzbez",
													theClientDto.getMandant(),
													theClientDto.getLocUi()),

									getTextRespectUISpr("lp.lagerstand",
											theClientDto.getMandant(),
											theClientDto.getLocUi()),
									getTextRespectUISpr("lp.vkpreis",
											theClientDto.getMandant(),
											theClientDto.getLocUi()), "S" },
							new int[] {
									-1, // diese Spalte wird ausgeblendet
									QueryParameters.FLR_BREITE_L,
									QueryParameters.FLR_BREITE_S,
									QueryParameters.FLR_BREITE_XL,
									QueryParameters.FLR_BREITE_SHARE_WITH_REST,
									QueryParameters.FLR_BREITE_M,
									QueryParameters.FLR_BREITE_M,
									QueryParameters.FLR_BREITE_XS },
							new String[] {
									"i_id",
									"artikelliste.c_nr",
									"stuecklisten.stuecklisteart_c_nr",
									"aspr.c_bez",
									bArtikelgruppeStattZusatzbezeichnung ? "ag.c_nr"
											: "aspr.c_zbez",
									Facade.NICHT_SORTIERBAR,
									Facade.NICHT_SORTIERBAR,
									Facade.NICHT_SORTIERBAR }));

				} else {
					setTableInfo(new TableInfo(
							new Class[] { Integer.class, String.class,
									String.class, String.class, String.class,
									BigDecimal.class, BigDecimal.class,
									Icon.class, },
							new String[] {
									"i_id",
									getTextRespectUISpr(
											"artikel.artikelnummerlang",
											theClientDto.getMandant(),
											theClientDto.getLocUi()),
									getTextRespectUISpr("lp.stuecklistenart",
											theClientDto.getMandant(),
											theClientDto.getLocUi()),
									getTextRespectUISpr(
											"bes.artikelbezeichnung",
											theClientDto.getMandant(),
											theClientDto.getLocUi()),
									bArtikelgruppeStattZusatzbezeichnung ? getTextRespectUISpr(
											"lp.artikelgruppe",
											theClientDto.getMandant(),
											theClientDto.getLocUi())
											: getTextRespectUISpr(
													"artikel.zusatzbez",
													theClientDto.getMandant(),
													theClientDto.getLocUi()),
									getTextRespectUISpr("lp.lagerstand",
											theClientDto.getMandant(),
											theClientDto.getLocUi()),
									getTextRespectUISpr("lp.gestpreis",
											theClientDto.getMandant(),
											theClientDto.getLocUi()), "S" },
							new int[] {
									-1, // diese Spalte wird ausgeblendet
									QueryParameters.FLR_BREITE_L,
									QueryParameters.FLR_BREITE_S,
									QueryParameters.FLR_BREITE_XL,
									QueryParameters.FLR_BREITE_SHARE_WITH_REST,
									QueryParameters.FLR_BREITE_M,
									QueryParameters.FLR_BREITE_M,
									QueryParameters.FLR_BREITE_XS },
							new String[] {
									"i_id",
									"artikelliste.c_nr",
									"stuecklisten.stuecklisteart_c_nr",
									"aspr.c_bez",
									bArtikelgruppeStattZusatzbezeichnung ? "ag.c_nr"
											: "aspr.c_zbez",
									Facade.NICHT_SORTIERBAR,
									Facade.NICHT_SORTIERBAR,
									Facade.NICHT_SORTIERBAR }));

				}
			}
		}
		return super.getTableInfo();
	}

	public PrintInfoDto getSDocPathAndPartner(Object key) {
		ArtikelDto artikelDto = null;
		try {
			artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(
					(Integer) key, theClientDto);
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
		return "ARTIKEL";
	}

}
