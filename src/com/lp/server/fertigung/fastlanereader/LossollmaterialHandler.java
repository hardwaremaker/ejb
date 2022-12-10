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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.Icon;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.anfrage.service.AnfragepositionFac;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelsperren;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelfehlmengeDto;
import com.lp.server.artikel.service.LagerFac;
import com.lp.server.artikel.service.SperrenIcon;
import com.lp.server.fertigung.fastlanereader.generated.FLRLosistmaterial;
import com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;
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
public class LossollmaterialHandler extends UseCaseHandler {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String FLR_LOSMAT = "flrlossollmaterial.";
	private static final String FLR_LOSMAT_FROM_CLAUSE = " from FLRLossollmaterial flrlossollmaterial LEFT OUTER JOIN flrlossollmaterial.flrartikelliste.artikelsprset AS aspr ";

	private boolean bFehlmengeStattPreis = false;

	private boolean bIstpreisAnzeigen = false;

	private boolean bPositionstextAusStklAnzeigen = false;

	private boolean bLagerinfo = false;
	private int iLagerinfoLagerstandKnapp = 10;

	private String getEinrueckung(FLRLossollmaterial sm, int iEbene) {

		iEbene = iEbene + 1;

		if (iEbene < 10) {
			if (sm.getLossollmaterial_i_id_original() != null) {

				return getEinrueckung(sm.getFlrlossollmaterial_original(), iEbene) + "  ";
			} else {
				return "";
			}
		} else {
			return "";
		}

	}

	private String getEinrueckung(FLRLossollmaterial sm) {
		String s = "";

		if (sm.getLossollmaterial_i_id_original() != null) {
			return getEinrueckung(sm.getFlrlossollmaterial_original(), 0) + " ";
		}

		return s;
	}

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
			String queryString = "SELECT flrlossollmaterial,(SELECT s FROM FLRArtikelsperren as s WHERE s.artikel_i_id=flrlossollmaterial.flrartikel.i_id AND s.i_sort=1) as sperren, (SELECT sum(artikellager.n_lagerstand) FROM FLRArtikellager AS artikellager WHERE artikellager.compId.artikel_i_id=flrlossollmaterial.flrartikel.i_id AND artikellager.flrlager.mandant_c_nr='"
					+ theClientDto.getMandant()
					+ "'  AND artikellager.flrlager.b_konsignationslager=0 AND artikellager.flrlager.lagerart_c_nr NOT IN('"
					+ LagerFac.LAGERART_WERTGUTSCHRIFT
					+ "')), (SELECT sum(fm.n_menge) FROM FLRFehlmenge AS fm WHERE fm.artikel_i_id=flrlossollmaterial.flrartikel.i_id), (SELECT sum(ar.n_menge) FROM FLRArtikelreservierung AS ar WHERE ar.flrartikel.i_id=flrlossollmaterial.flrartikel.i_id), (SELECT sum(bs.n_menge) FROM FLRArtikelbestellt AS bs WHERE bs.flrartikel.i_id=flrlossollmaterial.flrartikel.i_id)   from FLRLossollmaterial flrlossollmaterial  LEFT OUTER JOIN flrlossollmaterial.flrartikelliste.artikelsprset AS aspr  "

					+ this.buildWhereClause() + this.buildOrderByClause();
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(PAGE_SIZE);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;
			int col = 0;

			HashMap<Integer, String> positionstexteAusStueckliste = null;

			while (resultListIterator.hasNext()) {
				Object[] o = (Object[]) resultListIterator.next();
				FLRLossollmaterial losmat = (FLRLossollmaterial) o[0];

				if (positionstexteAusStueckliste == null) {
					positionstexteAusStueckliste = new HashMap<Integer, String>();

					if (bPositionstextAusStklAnzeigen) {
						if (losmat.getFlrlos().getStueckliste_i_id() != null) {
							StuecklistepositionDto[] stklPosDtos = getStuecklisteFac()
									.stuecklistepositionFindByStuecklisteIId(losmat.getFlrlos().getStueckliste_i_id(),
											theClientDto);

							ArrayList<StuecklistepositionDto> alPositionen = new ArrayList<StuecklistepositionDto>();

							for (int j = 0; j < stklPosDtos.length; j++) {
								alPositionen.add(stklPosDtos[j]);

							}

							LossollmaterialDto[] sollmatDtos = getFertigungFac()
									.lossollmaterialFindByLosIIdOrderByISort(losmat.getFlrlos().getI_id());

							for (int i = 0; i < sollmatDtos.length; i++) {

								// SP5058 Wenn Artikel und Montageart genau einmal vorhanden
								// sind, dann verwenden wird den Eintrag

								LossollmaterialDto sollmatDto = sollmatDtos[i];

								StuecklistepositionDto stuecklistepositionDto_Zueghoerig = null;
								for (int u = 0; u < alPositionen.size(); u++) {

									if (sollmatDto.getArtikelIId().equals(alPositionen.get(u).getArtikelIId())
											&& sollmatDto.getMontageartIId()
													.equals(alPositionen.get(u).getMontageartIId())) {

										if (stuecklistepositionDto_Zueghoerig == null) {

											stuecklistepositionDto_Zueghoerig = alPositionen.get(u);
										} else {
											stuecklistepositionDto_Zueghoerig = null;
											break;
										}

									}

								}

								if (stuecklistepositionDto_Zueghoerig == null) {
									// SP5709
									BigDecimal sollsatzmenge = sollmatDto.getNMenge().divide(
											losmat.getFlrlos().getN_losgroesse(), 3, BigDecimal.ROUND_HALF_EVEN);

									com.lp.server.fertigung.service.StklPosDtoSearchResult resultS = getFertigungReportFac()
											.findStklPositionByLossollmaterial(alPositionen,
													new com.lp.server.fertigung.service.StklPosDtoSearchParams(
															sollmatDto.getArtikelIId(), sollmatDto.getMontageartIId(),
															sollsatzmenge, sollmatDto.getNMengeStklPos()),
													theClientDto);
									stuecklistepositionDto_Zueghoerig = resultS.getStklPosDto();
								}
								if (stuecklistepositionDto_Zueghoerig != null) {
									String positionAusStueckliste = stuecklistepositionDto_Zueghoerig.getCPosition();

									positionstexteAusStueckliste.put(sollmatDto.getIId(), positionAusStueckliste);

									alPositionen.remove(stuecklistepositionDto_Zueghoerig);
								}
							}

						}
					}
				}

				rows[row][col++] = losmat.getI_id();
				if (Helper.short2boolean(losmat.getB_nachtraeglich())) {
					rows[row][col++] = "N";
				} else {
					rows[row][col++] = "S";
				}

				rows[row][col++] = getEinrueckung(losmat) + losmat.getFlrartikel().getC_nr();

				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(losmat.getFlrartikel().getI_id(),
						theClientDto);
				if (bReferenznummerInPositionen) {
					rows[row][col++] = aDto.getCReferenznr();
				}
				if (aDto.getArtikelsprDto() != null) {
					rows[row][col++] = aDto.getArtikelsprDto().getCBez();
					rows[row][col++] = aDto.getArtikelsprDto().getCZbez();
				} else {
					rows[row][col++] = null;
					rows[row][col++] = null;
				}

				if (bPositionstextAusStklAnzeigen) {
					rows[row][col++] = positionstexteAusStueckliste.get(losmat.getI_id());
				}

				rows[row][col++] = losmat.getN_menge();
				
				
				if (losmat.getFlrartikel() != null && losmat.getFlrartikel().getEinheit_c_nr()!=null) {
					rows[row][col++] = losmat.getFlrartikel().getEinheit_c_nr().trim();
				}else {
					rows[row][col++] = null;
				}
				
				// erledigte Menge
				BigDecimal bdAusgegeben = new BigDecimal(0);
				for (Iterator<?> iter = losmat.getIstmaterialset().iterator(); iter.hasNext();) {
					FLRLosistmaterial item = (FLRLosistmaterial) iter.next();
					if (Helper.short2boolean(item.getB_abgang()) == true) {
						bdAusgegeben = bdAusgegeben.add(item.getN_menge());
					} else {
						bdAusgegeben = bdAusgegeben.subtract(item.getN_menge());
					}
				}
				rows[row][col++] = bdAusgegeben;

				ArtikelfehlmengeDto artikelfehlmengeDto = getFehlmengeFac()
						.artikelfehlmengeFindByBelegartCNrBelegartPositionIIdOhneExc(LocaleFac.BELEGART_LOS,
								losmat.getI_id());

				boolean bLagerstandKnapp = false;
				if (bLagerinfo) {

					BigDecimal lagerstand = (BigDecimal) o[2];
					if (lagerstand == null) {
						lagerstand = BigDecimal.ZERO;
					}

					BigDecimal fehlmenge = (BigDecimal) o[3];
					if (fehlmenge == null) {
						fehlmenge = BigDecimal.ZERO;
					}

					BigDecimal reservierungen = (BigDecimal) o[4];
					if (reservierungen == null) {
						reservierungen = BigDecimal.ZERO;
					}

					BigDecimal bestellt = (BigDecimal) o[5];
					if (bestellt == null) {
						bestellt = BigDecimal.ZERO;
					}

					rows[row][col++] = lagerstand;
					rows[row][col++] = lagerstand.subtract(fehlmenge.add(reservierungen));
					rows[row][col++] = bestellt;

					if (Helper.short2boolean(aDto.getBLagerbewirtschaftet()) && artikelfehlmengeDto != null
							&& lagerstand.doubleValue() < (artikelfehlmengeDto.getNMenge().doubleValue()
									+ iLagerinfoLagerstandKnapp)) {

						bLagerstandKnapp = true;
					}

				}

				if (bFehlmengeStattPreis) {

					if (Helper.short2boolean(losmat.getB_nachtraeglich()) && artikelfehlmengeDto == null) {
						rows[row][col++] = null;
					} else {
						rows[row][col++] = losmat.getN_menge().subtract(bdAusgegeben);
					}

				} else {

					rows[row][col++] = losmat.getN_sollpreis();
				}

				if (bIstpreisAnzeigen) {
					rows[row][col++] = getFertigungFac().getAusgegebeneMengePreis(losmat.getI_id(), null, theClientDto);
				}

				if (artikelfehlmengeDto != null) {
					rows[row][col++] = "F";
				} else {
					rows[row][col++] = "";
				}

				FLRArtikelsperren as = (FLRArtikelsperren) o[1];

				if (as != null) {
					rows[row][col++] = as.getFlrsperren().getC_bez();
				} else {
					rows[row][col++] = null;
				}

				if (Helper.short2boolean(losmat.getB_dringend())) {

					rows[row][col++] = Color.BLUE;

				} else {
					if (losmat.getLossollmaterial_i_id_original() != null) {
						rows[row][col++] = new Color(89, 188, 41);
					} else {

						if (bLagerstandKnapp) {
							rows[row][col++] = Color.ORANGE;
						} else {
							rows[row][col++] = null;
						}

					}
				}

				row++;
				col = 0;
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
					where.append(" " + FLR_LOSMAT + filterKriterien[i].kritName);
					where.append(" " + filterKriterien[i].operator);
					where.append(" " + filterKriterien[i].value);
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
					if (!kriterien[i].kritName.endsWith(Facade.NICHT_SORTIERBAR)) {
						if (kriterien[i].isKrit) {
							if (sortAdded) {
								orderBy.append(", ");
							}
							sortAdded = true;
							if (!kriterien[i].kritName.startsWith("aspr")) {
								orderBy.append(FLR_LOSMAT + kriterien[i].kritName);
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

				if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_ERSATZTYPENVERWALTUNG, theClientDto)) {
					orderBy.append(
							"flrlossollmaterial.i_sort ASC, flrlossollmaterial.lossollmaterial_i_id_original DESC");
				} else {
					orderBy.append(FLR_LOSMAT).append(FertigungFac.FLR_LOSSOLLMATERIAL_FLRARTIKEL + ".c_nr")
							.append(" ASC , flrlossollmaterial.t_aendern ASC ");
				}

				sortAdded = true;
			}
			if (orderBy.indexOf(FLR_LOSMAT + FertigungFac.FLR_LOSSOLLMATERIAL_FLRARTIKEL + ".c_nr") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" ").append(FLR_LOSMAT).append(FertigungFac.FLR_LOSSOLLMATERIAL_FLRARTIKEL + ".c_nr")
						.append(" ");
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
				String queryString = "select " + FLR_LOSMAT + FertigungFac.FLR_LOSSOLLMATERIAL_I_ID
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

		int iNachkommastellenMenge = 2;
		int iNachkommastellenPreis = 2;
		try {
			iNachkommastellenMenge = getMandantFac().getNachkommastellenMenge(theClientDto.getMandant());
			iNachkommastellenPreis = getMandantFac().getNachkommastellenPreisAllgemein(theClientDto.getMandant());
			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_FEHLMENGE_STATT_PREIS_IN_LOSMATERIAL);
			bFehlmengeStattPreis = (java.lang.Boolean) parameter.getCWertAsObject();

			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_POSITIONSTEXT_AUS_STKL_IN_LOSSOLLMATERIAL_ANZEIGEN);
			bPositionstextAusStklAnzeigen = (java.lang.Boolean) parameter.getCWertAsObject();

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		columns.add("i_id", Integer.class, "i_id", QueryParameters.FLR_BREITE_SHARE_WITH_REST, "i_id");
		columns.add("lp.art", String.class, getTextRespectUISpr("lp.art", mandant, locUi), 3, Facade.NICHT_SORTIERBAR);
		columns.add("lp.artikelnummer", String.class, getTextRespectUISpr("lp.artikelnummer", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, FertigungFac.FLR_LOSSOLLMATERIAL_FLRARTIKEL + ".c_nr");

		if (bReferenznummerInPositionen) {
			columns.add("lp.referenznummer", String.class, getTextRespectUISpr("lp.referenznummer", mandant, locUi),
					QueryParameters.FLR_BREITE_XM, FertigungFac.FLR_LOSSOLLMATERIAL_FLRARTIKEL + ".c_referenznr");
		}

		columns.add("lp.bezeichnung", String.class, getTextRespectUISpr("lp.bezeichnung", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, "aspr.c_bez");
		columns.add("artikel.zusatzbez", String.class, getTextRespectUISpr("artikel.zusatzbez", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, "aspr.c_zbez");

		if (bPositionstextAusStklAnzeigen) {
			columns.add("lp.position", String.class, getTextRespectUISpr("lp.position", mandant, locUi),
					QueryParameters.FLR_BREITE_M, Facade.NICHT_SORTIERBAR);
		}

		columns.add("lp.menge", super.getUIClassBigDecimalNachkommastellen(iNachkommastellenMenge),
				getTextRespectUISpr("lp.menge", mandant, locUi), QueryParameters.FLR_BREITE_PREIS,
				FertigungFac.FLR_LOSSOLLMATERIAL_N_MENGE);
		columns.add("lp.einheit", String.class, getTextRespectUISpr("lp.einheit", mandant, locUi),
				QueryParameters.FLR_BREITE_XS, FertigungFac.FLR_LOSSOLLMATERIAL_FLRARTIKEL + ".einheit_c_nr");
		columns.add("fert.ausgegeben", super.getUIClassBigDecimalNachkommastellen(iNachkommastellenMenge),
				getTextRespectUISpr("fert.ausgegeben", mandant, locUi),
				getUIBreiteAbhaengigvonNachkommastellen(QueryParameters.MENGE, iNachkommastellenMenge),
				AnfragepositionFac.FLR_ANFRAGEPOSITION_N_MENGE);

		// PJ21663
		if (bLagerinfo) {
			columns.add("lp.lagerstand", super.getUIClassBigDecimalNachkommastellen(iNachkommastellenMenge),
					getTextRespectUISpr("lp.lagerstand", mandant, locUi), QueryParameters.FLR_BREITE_M,
					Facade.NICHT_SORTIERBAR);
			columns.add("lp.verfuegbar", super.getUIClassBigDecimalNachkommastellen(iNachkommastellenMenge),
					getTextRespectUISpr("lp.verfuegbar", mandant, locUi), QueryParameters.FLR_BREITE_M,
					Facade.NICHT_SORTIERBAR);
			columns.add("lp.bestellt", super.getUIClassBigDecimalNachkommastellen(iNachkommastellenMenge),
					getTextRespectUISpr("lp.bestellt", mandant, locUi), QueryParameters.FLR_BREITE_M,
					Facade.NICHT_SORTIERBAR);
		}

		if (bFehlmengeStattPreis) {
			columns.add("lp.fehlmenge", super.getUIClassBigDecimalNachkommastellen(iNachkommastellenMenge),
					getTextRespectUISpr("lp.fehlmenge", mandant, locUi), QueryParameters.FLR_BREITE_PREIS,
					Facade.NICHT_SORTIERBAR);
		} else {
			columns.add("fert.losmaterial.sollpreis",
					super.getUIClassBigDecimalNachkommastellen(iNachkommastellenPreis),
					getTextRespectUISpr("fert.losmaterial.sollpreis", mandant, locUi), QueryParameters.FLR_BREITE_PREIS,
					FertigungFac.FLR_LOSSOLLMATERIAL_N_SOLLPREIS);
		}

		if (bIstpreisAnzeigen) {
			columns.add("fert.losmaterial.istpreis", super.getUIClassBigDecimalNachkommastellen(iNachkommastellenPreis),
					getTextRespectUISpr("fert.losmaterial.istpreis", mandant, locUi), QueryParameters.FLR_BREITE_PREIS,
					Facade.NICHT_SORTIERBAR);
		}

		columns.add("fehlmengen", String.class, " ", 1, Facade.NICHT_SORTIERBAR,
				getTextRespectUISpr("lp.fehlmenge", theClientDto.getMandant(), theClientDto.getLocUi()));

		columns.add("fert.sperre", SperrenIcon.class, getTextRespectUISpr("fert.sperre", mandant, locUi),
				QueryParameters.FLR_BREITE_S, Facade.NICHT_SORTIERBAR,
				getTextRespectUISpr("fert.sperre.tooltip", theClientDto.getMandant(), theClientDto.getLocUi()));
		columns.add("Color", Color.class, "", 1, Facade.NICHT_SORTIERBAR);

		return columns;

	}

	public TableInfo getTableInfo() {

		try {
			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_FEHLMENGE_STATT_PREIS_IN_LOSMATERIAL);
			bFehlmengeStattPreis = (java.lang.Boolean) parameter.getCWertAsObject();

			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_ISTPREIS_ANZEIGEN);
			bIstpreisAnzeigen = (java.lang.Boolean) parameter.getCWertAsObject();

			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_FERTIGUNG, ParameterFac.PARAMETER_LAGERINFO_IN_POSITIONEN);
			bLagerinfo = (Boolean) parameter.getCWertAsObject();

			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_LAGERINFO_IN_LOSPOSITIONEN_LAGERSTAND_KNAPP);
			iLagerinfoLagerstandKnapp = (Integer) parameter.getCWertAsObject();

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

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
