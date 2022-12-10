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
package com.lp.server.lieferschein.fastlanereader;

import java.awt.Color;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.Icon;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferschein;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferscheintextsuche;
import com.lp.server.lieferschein.fastlanereader.generated.FLRVerkettet;
import com.lp.server.lieferschein.service.ILieferscheinFLRData;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinFLRData;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinHandlerFeature;
import com.lp.server.lieferschein.service.LieferscheinQueryResult;
import com.lp.server.lieferschein.service.LieferscheinServiceFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.system.fastlanereader.generated.FLRLandplzort;
import com.lp.server.system.fastlanereader.generated.FLRMandant;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeLieferschein;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.FlrFirmaAnsprechpartnerFilterBuilder;
import com.lp.server.util.fastlanereader.UseCaseHandler;
import com.lp.server.util.fastlanereader.service.query.FilterBlock;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FlrFeatureBase;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryParametersFeatures;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * FLR fuer LS_LIEFERSCHEIN.
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellung: Uli Walch; 21.10.04
 * </p>
 * 
 * <p>
 * 
 * @author $Author: robert $
 *         </p>
 * 
 * @version $Revision: 1.27 $ Date $Date: 2013/01/19 11:47:31 $
 */
public class LieferscheinHandler extends UseCaseHandler {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public static final String FLR_LIEFERSCHEIN = "flrlieferschein.";
	public static final String FLR_LIEFERSCHEIN_FROM_CLAUSE = " from FLRLieferschein as flrlieferschein left join flrlieferschein.flrverkettet2 as flrverkettet2 ";
	Integer iAnlegerStattVertreterAnzeigen = 0;

	boolean bDarfPreiseSehen = false;
	boolean bVerketteteLieferscheine = false;
	boolean bAuftragAusPosition = false;
	boolean bRechnungsadresseInLsAuswahl = false;

	private LieferscheinFeature cachedFeature;

	private class LieferscheinKundeAnsprechpartnerFilterBuilder extends FlrFirmaAnsprechpartnerFilterBuilder {

		public LieferscheinKundeAnsprechpartnerFilterBuilder(boolean bSuchenInklusiveKBez) {
			super(bSuchenInklusiveKBez);
		}

		@Override
		public String getFlrPartner() {
			return FLR_LIEFERSCHEIN + LieferscheinFac.FLR_LIEFERSCHEIN_FLRKUNDE + "." + LieferantFac.FLR_PARTNER;
		}

		@Override
		public String getFlrPropertyAnsprechpartnerIId() {
			return FLR_LIEFERSCHEIN + "ansprechpartner_i_id_kunde";
		}
	}

	private class LieferscheinFeature extends FlrFeatureBase<ILieferscheinFLRData> {
		private boolean enabled;

		public LieferscheinFeature() {
			super(getQuery());
		}

		@Override
		protected void initialize(QueryParametersFeatures qpf) {
			enabled = qpf.hasFeature(LieferscheinHandlerFeature.LIEFERSCHEIN_DATA);
		}

		public boolean isEnabled() {
			return enabled;
		}

		@Override
		protected ILieferscheinFLRData[] createFlrData(int rows) {
			return new LieferscheinFLRData[rows];
		}

		@Override
		protected ILieferscheinFLRData createFlrDataObject(int index) {
			return new LieferscheinFLRData();
		}

		public void setStatusCnr(int row, String statusCnr) {
			getFlrDataObject(row).setStatusCnr(statusCnr);
		}

		public void setKundeIdLieferadresse(int row, Integer kundeId) {
			getFlrDataObject(row).setKundeIdLieferadresse(kundeId);
		}
	}

	private LieferscheinFeature getFeature() {
		if (cachedFeature == null) {
			cachedFeature = new LieferscheinFeature();
		}
		return cachedFeature;
	}

	/**
	 * gets the data page for the specified row using the current query. The row at
	 * rowIndex will be located in the middle of the page.
	 * 
	 * @param rowIndex diese Zeile soll selektiert sein
	 * @return QueryResult das Ergebnis der Abfrage
	 * @throws EJBExceptionLP Ausnahme
	 * @see UseCaseHandler#getPageAt(java.lang.Integer)
	 */
	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		cachedFeature = null;

		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = getLimit();
			int startIndex = getStartIndex(rowIndex, pageSize);// Math.max(rowIndex.intValue() - (pageSize / 2), 0);
			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			String queryString = this.getFromClause() + this.buildWhereClause() + this.buildOrderByClause();
			// myLogger.info("HQL: " + queryString);
			// logQuery(queryString);
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;
			int col = 0;
			getFeature().setFlrRowCount(rows.length);

			// darf Preise sehen.
			final boolean bDarfPreiseSehen = getTheJudgeFac().hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF,
					theClientDto);

			while (resultListIterator.hasNext()) {
				Object[] o = (Object[]) resultListIterator.next();
				FLRLieferschein lieferschein = (FLRLieferschein) (o[0]);
				FLRAuftragReport auftrag = (FLRAuftragReport) (o[1]);
				FLRMandant lieferadresseIstEinMandant = (FLRMandant) (o[2]);
				String mandantCNrBeiDerPartnerLieferadresseIst = (String) (o[3]);

				Object[] rowToAddCandidate = new Object[colCount];

				rowToAddCandidate[getTableColumnInformation().getViewIndex("i_id")] = lieferschein.getI_id();

				String lieferscheinart = null;
				if (lieferschein.getLieferscheinart_c_nr().equals(LieferscheinFac.LSART_LIEFERANT)) {
					lieferscheinart = LieferscheinFac.LSART_LIEFERANT_SHORT;
				} else if ((auftrag != null && auftrag.getBestellung_i_id_anderermandant() != null)
						|| (lieferschein.getFlrauftrag() != null
								&& lieferschein.getFlrauftrag().getBestellung_i_id_anderermandant() != null)) {
					lieferscheinart = "M";
				} else if (lieferadresseIstEinMandant != null
						&& !lieferadresseIstEinMandant.getC_nr().equals(lieferschein.getMandant_c_nr())) {
					lieferscheinart = "m";
				} else if (mandantCNrBeiDerPartnerLieferadresseIst != null
						&& !mandantCNrBeiDerPartnerLieferadresseIst.equals(lieferschein.getMandant_c_nr())) {
					lieferscheinart = "ml";
				}

				rowToAddCandidate[getTableColumnInformation().getViewIndex("art")] = lieferscheinart;

				rowToAddCandidate[getTableColumnInformation().getViewIndex("ls.lieferscheinnummer")] = lieferschein
						.getC_nr();

				if (bAuftragAusPosition == true && auftrag != null) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("auft.auftrag")] = auftrag.getC_nr();
				}

				rowToAddCandidate[getTableColumnInformation().getViewIndex("lsch.lieferadreesse")] = lieferschein
						.getFlrkunde().getFlrpartner().getC_name1nachnamefirmazeile1();

				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.ort")] = lieferscheinart;

				// IMS 1757 die Anschrift des Kunde anzeigen
				String cAnschrift = null;

				if (lieferschein.getFlrkunde() != null) {
					getFeature().setKundeIdLieferadresse(row, lieferschein.getFlrkunde().getI_id());

					FLRLandplzort flranschrift = lieferschein.getFlrkunde().getFlrpartner().getFlrlandplzort();

					if (flranschrift != null) {
						cAnschrift = flranschrift.getFlrland().getC_lkz() + "-" + flranschrift.getC_plz() + " "
								+ flranschrift.getFlrort().getC_name();
					}
				}

				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.ort")] = cAnschrift;

				if (bRechnungsadresseInLsAuswahl) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("ls.rechnungadresse")] = lieferschein
							.getFlrkunderechnungsadresse().getFlrpartner().getC_name1nachnamefirmazeile1();
				}

				String proj_bestellnummer = "";
				
				
				if (bTitelInAF_BS_LS_RE_LOS) {
					if (lieferschein.getFlrprojekt() != null) {
						proj_bestellnummer = lieferschein.getFlrprojekt().getC_titel()+" | ";
					}
				}
				
				if (lieferschein.getC_bez_projektbezeichnung() != null) {
					proj_bestellnummer += lieferschein.getC_bez_projektbezeichnung();
				}

				if (lieferschein.getC_bestellnummer() != null) {
					proj_bestellnummer += " | " + lieferschein.getC_bestellnummer();
				}

				rowToAddCandidate[getTableColumnInformation()
						.getViewIndex("ls.projektbestellnummer")] = proj_bestellnummer;
				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.datum")] = lieferschein
						.getD_belegdatum();

				if (iAnlegerStattVertreterAnzeigen == 1) {
					if (lieferschein.getFlrpersonalanleger() != null) {
						rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.vertreter")] = lieferschein
								.getFlrpersonalanleger().getC_kurzzeichen();
					}

				} else if (iAnlegerStattVertreterAnzeigen == 2) {
					if (lieferschein.getFlrpersonalaenderer() != null) {

						rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.vertreter")] = lieferschein
								.getFlrpersonalaenderer().getC_kurzzeichen();
					}
				} else {
					if (lieferschein.getFlrvertreter() != null) {
						rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.vertreter")] = lieferschein
								.getFlrvertreter().getC_kurzzeichen();

					}
				}

				String status = lieferschein.getLieferscheinstatus_status_c_nr();
				getFeature().setStatusCnr(row, status);

				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.status")] = getStatusMitUebersetzung(
						status, lieferschein.getT_versandzeitpunkt(), lieferschein.getC_versandtype());

				rowToAddCandidate[getTableColumnInformation()
						.getViewIndex("rechnung.rechnungsnummer")] = lieferschein.getFlrrechnung() == null ? null
								: lieferschein.getFlrrechnung().getC_nr();
				if (bDarfPreiseSehen) {

					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.wert")] = lieferschein
							.getN_gesamtwertinlieferscheinwaehrung();
					rowToAddCandidate[getTableColumnInformation().getViewIndex("waehrung")] = lieferschein
							.getWaehrung_c_nr_lieferscheinwaehrung();

				}

				
				
				
				if (bVerketteteLieferscheine) {
					if ((lieferschein.getFlrverkettet2() != null && lieferschein.getFlrverkettet2().size() > 0)) {
						FLRVerkettet vk = (FLRVerkettet) lieferschein.getFlrverkettet2().iterator().next();

						rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.verkettet.kopf")] = vk
								.getFlrlieferschein().getC_nr();
					}
				}

				
				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.kommentar")] = getKommentarart(
						lieferschein.getX_internerkommentar(), null);
				
				if (!Helper.short2boolean(lieferschein.getB_verrechenbar())) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("Color")] = Color.LIGHT_GRAY;
				} else {

					if ((lieferschein.getFlrverkettet() != null && lieferschein.getFlrverkettet().size() > 0)
							|| lieferschein.getFlrverkettet2() != null && lieferschein.getFlrverkettet2().size() > 0) {
						rowToAddCandidate[getTableColumnInformation().getViewIndex("Color")] = Color.BLUE;
					}

				}
				rows[row] = rowToAddCandidate;
				row++;
				col = 0;
			}

			if (getFeature().isEnabled()) {
				LieferscheinQueryResult lieferscheinResult = new LieferscheinQueryResult(rows, this.getRowCount(),
						startIndex, endIndex, 0);
				lieferscheinResult.setFlrData(getFeature().getFlrData());
				result = lieferscheinResult;
			} else {
				result = new QueryResult(rows, this.getRowCount(), startIndex, endIndex, 0);
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			closeSession(session);
		}
		return result;
	}

	/**
	 * gets the total number of rows represented by the current query.
	 * 
	 * @return int die Anzehl der Zeilen im Ergebnis
	 * @see UseCaseHandler#getRowCountFromDataBase()
	 */
	protected long getRowCountFromDataBase() {
		long rowCount = 0;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			String queryString = "SELECT count(*) from FLRLieferschein as flrlieferschein "
					+ " left join flrlieferschein.flrkunde.flrpartner.flrlandplzort as flrlandplzort "
					+ " left join flrlieferschein.flrkunde.flrpartner.flrlandplzort.flrort as flrort "
					+ " left join flrlieferschein.flrkunde.flrpartner.flrlandplzort.flrland as flrland "
					+ " left join flrlieferschein.flrrechnung as flrrechnung "
					+ " left join flrlieferschein.flrverkettet2 as flrverkettet2 " + this.buildWhereClause();
			// logQuery(queryString);
			Query query = session.createQuery(queryString);
			List<?> rowCountResult = query.list();
			if (rowCountResult != null && rowCountResult.size() > 0) {
				rowCount = ((Long) rowCountResult.get(0)).longValue();
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (HibernateException he) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HIBERNATE, he);
				}
			}
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

					// wenn nach der c_nr gefilter wird, wird das kriterium
					// veraendert
					if (filterKriterien[i].kritName.equals("c_nr")) {
						try {
							String sValue = super.buildWhereBelegnummer(filterKriterien[i], false,
									ParameterFac.KATEGORIE_LIEFERSCHEIN,
									ParameterFac.PARAMETER_LIEFERSCHEIN_BELEGNUMMERSTARTWERT);
							// Belegnummernsuche auch in "altem" Jahr, wenn im
							// neuen noch keines vorhanden ist
							if (!istBelegnummernInJahr("FLRLieferschein", sValue)) {
								sValue = super.buildWhereBelegnummer(filterKriterien[i], true);
							}
							where.append(" " + FLR_LIEFERSCHEIN + filterKriterien[i].kritName);
							where.append(" " + filterKriterien[i].operator);
							where.append(" " + sValue);
						} catch (Exception ex) {
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, ex);
						}
					} else if (filterKriterien[i].kritName.equals(LieferscheinServiceFac.LS_HANDLER_OHNE_VERKETTETE)) {

						where.append(
								" flrlieferschein.i_id not in (SELECT v.lieferschein_i_id_verkettet FROM FLRVerkettet v) AND flrlieferschein.i_id not in (SELECT v.lieferschein_i_id FROM FLRVerkettet v) ");

					} else if (filterKriterien[i].kritName.equals("c_bez_projektbezeichnung")) {

						where.append(" (");
						where.append(buildWhereClauseExtendedSearch(Arrays.asList(filterKriterien[i].value.split(" ")),
								FLR_LIEFERSCHEIN + "c_bez_projektbezeichnung", filterKriterien[i].isBIgnoreCase()));
						where.append(" OR ");
						where.append(buildWhereClauseExtendedSearch(Arrays.asList(filterKriterien[i].value.split(" ")),
								FLR_LIEFERSCHEIN + "c_bestellnummer", filterKriterien[i].isBIgnoreCase()));
						where.append(" OR ");
						where.append(buildWhereClauseExtendedSearch(Arrays.asList(filterKriterien[i].value.split(" ")),
								FLR_LIEFERSCHEIN + "c_kommission", filterKriterien[i].isBIgnoreCase()));
						where.append(") ");

					} else if (filterKriterien[i].kritName.equals("flrauftrag.c_nr")) {
						try {
							String sValue = super.buildWhereBelegnummer(filterKriterien[i], false,
									ParameterFac.KATEGORIE_AUFTRAG,
									ParameterFac.PARAMETER_AUFTRAG_BELEGNUMMERSTARTWERT);
							// Belegnummernsuche auch in "altem" Jahr, wenn im
							// neuen noch keines vorhanden ist
							if (!istBelegnummernInJahr("FLRLieferschein", sValue)) {
								sValue = super.buildWhereBelegnummer(filterKriterien[i], true,
										ParameterFac.KATEGORIE_AUFTRAG,
										ParameterFac.PARAMETER_AUFTRAG_BELEGNUMMERSTARTWERT);
							}
							where.append(" " + FLR_LIEFERSCHEIN + filterKriterien[i].kritName);
							where.append(" " + filterKriterien[i].operator);
							where.append(" " + sValue);
						} catch (Exception ex) {
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, ex);
						}
					} else if (isKundeFilter(filterKriterien[i])) {
						LieferscheinKundeAnsprechpartnerFilterBuilder filterBuilder = new LieferscheinKundeAnsprechpartnerFilterBuilder(
								getParameterFac().getSuchenInklusiveKBez(theClientDto.getMandant()));
						filterBuilder.buildFirmaAnsprechpartnerFilter(filterKriterien[i], where);
						// buildFirmaFilterOld(filterKriterien[i], where);
					} else if (filterKriterien[i].kritName.equals("c_suche")) {

						where.append(buildWhereClauseExtendedSearchWithoutDuplicates(
								FLRLieferscheintextsuche.class.getSimpleName(), FLR_LIEFERSCHEIN, filterKriterien[i]));
					} else {
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" lower(" + FLR_LIEFERSCHEIN + filterKriterien[i].kritName + ")");
						} else {
							where.append(" " + FLR_LIEFERSCHEIN + filterKriterien[i].kritName);
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

	private void buildFirmaFilterOld(FilterKriterium filterKriterium, StringBuffer where) {
		ParametermandantDto parameter = null;
		try {
			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_SUCHEN_INKLUSIVE_KBEZ);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		Boolean bSuchenInklusiveKbez = (java.lang.Boolean) parameter.getCWertAsObject();
		if (bSuchenInklusiveKbez) {
			if (filterKriterium.isBIgnoreCase()) {
				where.append(" ( upper(" + FLR_LIEFERSCHEIN + filterKriterium.kritName + ")");
				where.append(" " + filterKriterium.operator);
				where.append(" " + filterKriterium.value.toUpperCase());
				where.append(" OR upper(" + FLR_LIEFERSCHEIN + "flrkunde.flrpartner.c_kbez" + ")");
				where.append(" " + filterKriterium.operator);
				where.append(" " + filterKriterium.value.toUpperCase() + ") ");
			} else {
				where.append(" " + FLR_LIEFERSCHEIN + filterKriterium.kritName);
				where.append(" " + filterKriterium.operator);
				where.append(" " + filterKriterium.value);
				where.append("OR " + FLR_LIEFERSCHEIN + "flrkunde.flrpartner.c_kbez");
				where.append(" " + filterKriterium.operator);
				where.append(" " + filterKriterium.value);
			}
		} else {
			if (filterKriterium.isBIgnoreCase()) {
				where.append(" upper(" + FLR_LIEFERSCHEIN + filterKriterium.kritName + ")");
			} else {
				where.append(" " + FLR_LIEFERSCHEIN + filterKriterium.kritName);
			}

			where.append(" " + filterKriterium.operator);

			if (filterKriterium.isBIgnoreCase()) {
				where.append(" " + filterKriterium.value.toUpperCase());
			} else {
				where.append(" " + filterKriterium.value);
			}
		}
	}

	private boolean isKundeFilter(FilterKriterium filterKriterium) {
		return filterKriterium.kritName.equals(LieferscheinFac.FLR_LIEFERSCHEIN_FLRKUNDE + "."
				+ LieferantFac.FLR_PARTNER + "." + PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1);
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

							if (kriterien[i].kritName.startsWith("flrverkettet2")
									|| kriterien[i].kritName.startsWith("(SELECT")) {
								orderBy.append(kriterien[i].kritName);
							} else {
								orderBy.append(FLR_LIEFERSCHEIN + kriterien[i].kritName);
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
				orderBy.append(FLR_LIEFERSCHEIN).append(LieferscheinFac.FLR_LIEFERSCHEIN_C_NR).append(" DESC ");
				sortAdded = true;
			}
			if (orderBy.indexOf(FLR_LIEFERSCHEIN + LieferscheinFac.FLR_LIEFERSCHEIN_I_ID) < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" ").append(FLR_LIEFERSCHEIN).append(LieferscheinFac.FLR_LIEFERSCHEIN_I_ID)
						.append(" DESC ");
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
		// return FLR_LIEFERSCHEIN_FROM_CLAUSE
		return "SELECT flrlieferschein,(SELECT min(lp.flrpositionensichtauftrag.flrauftrag) FROM FLRLieferscheinposition lp WHERE lp.lieferschein_i_id=flrlieferschein.i_id ), (SELECT mand FROM FLRMandant mand WHERE mand.partner_i_id=flrlieferschein.flrkunde.flrpartner.i_id), (SELECT max(mand.c_nr) FROM FLRMandant mand WHERE mand.partner_i_id_lieferadresse=flrlieferschein.flrkunde.flrpartner.i_id) from FLRLieferschein as flrlieferschein "
				+ " left join flrlieferschein.flrkunde.flrpartner.flrlandplzort as flrlandplzort "
				+ " left join flrlieferschein.flrkunde.flrpartner.flrlandplzort.flrort as flrort "
				+ " left join flrlieferschein.flrkunde.flrpartner.flrlandplzort.flrland as flrland "
				+ " left join flrlieferschein.flrrechnung as flrrechnung "
				+ " left join flrlieferschein.flrverkettet2 as flrverkettet2 ";
	}

	/**
	 * sorts the data described by the current query using the specified sort
	 * criterias. The current query is also updated with the new sort criterias.
	 * 
	 * @param sortierKriterien nach diesen Kriterien wird das Ergebnis sortiert
	 * @param selectedId       auf diesem Datensatz soll der Cursor stehen
	 * @return QueryResult das Ergebnis der Abfrage
	 * @throws EJBExceptionLP Ausnahme
	 * @see UseCaseHandler#sort(SortierKriterium[], Object)
	 */
	public QueryResult sort(SortierKriterium[] sortierKriterien, Object selectedId) throws EJBExceptionLP {
		this.getQuery().setSortKrit(sortierKriterien);

		QueryResult result = null;
		int rowNumber = 0;

		if (selectedId != null && ((Integer) selectedId).intValue() >= 0) {
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();
				String queryString = "select " + FLR_LIEFERSCHEIN + LieferscheinFac.FLR_LIEFERSCHEIN_I_ID
						+ FLR_LIEFERSCHEIN_FROM_CLAUSE + this.buildWhereClause() + this.buildOrderByClause();
				// logQuery(queryString);
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
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
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

	private TableColumnInformation createColumnInformation(String mandant, Locale locUi) {
		TableColumnInformation columns = new TableColumnInformation();

		columns.add("i_id", Integer.class, "i_id", -1, LieferscheinFac.FLR_LIEFERSCHEIN_I_ID);
		columns.add("art", String.class, " ", 1, Facade.NICHT_SORTIERBAR);
		columns.add("ls.lieferscheinnummer", String.class, getTextRespectUISpr("ls.lieferscheinnummer", mandant, locUi),
				QueryParameters.FLR_BREITE_M, LieferscheinFac.FLR_LIEFERSCHEIN_C_NR);

		if (bAuftragAusPosition) {
			columns.add("auft.auftrag", String.class, getTextRespectUISpr("auft.auftrag", mandant, locUi),
					QueryParameters.FLR_BREITE_M, Facade.NICHT_SORTIERBAR);
		}

		columns.add("lsch.lieferadreesse", String.class, getTextRespectUISpr("lsch.lieferadreesse", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, LieferscheinFac.FLR_LIEFERSCHEIN_FLRKUNDE + "."
						+ KundeFac.FLR_PARTNER + "." + PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1);
		columns.add("lp.ort", String.class, getTextRespectUISpr("lp.ort", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST,
				LieferscheinFac.FLR_LIEFERSCHEIN_FLRKUNDE + "." + KundeFac.FLR_PARTNER + "."
						+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT + "." + SystemFac.FLR_LP_FLRLAND + "."
						+ SystemFac.FLR_LP_LANDLKZ + ", " +
						// und dann nach plz
						LieferscheinHandler.FLR_LIEFERSCHEIN + LieferscheinFac.FLR_LIEFERSCHEIN_FLRKUNDE + "."
						+ KundeFac.FLR_PARTNER + "." + PartnerFac.FLR_PARTNER_FLRLANDPLZORT + "."
						+ SystemFac.FLR_LP_LANDPLZORTPLZ);

		if (bRechnungsadresseInLsAuswahl) {
			columns.add("ls.rechnungadresse", String.class, getTextRespectUISpr("ls.rechnungadresse", mandant, locUi),
					QueryParameters.FLR_BREITE_M, LieferscheinFac.FLR_LIEFERSCHEIN_FLRKUNDERECHNUNGSADRESSE + "."
							+ KundeFac.FLR_PARTNER + "." + PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1);
		}

		columns.add("ls.projektbestellnummer", String.class,
				getTextRespectUISpr("ls.projektbestellnummer", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, LieferscheinFac.FLR_LIEFERSCHEIN_C_BEZ_PROJEKTBEZEICHNUNG);
		columns.add("lp.datum", java.util.Date.class, getTextRespectUISpr("lp.datum", mandant, locUi),
				QueryParameters.FLR_BREITE_M, LieferscheinFac.FLR_LIEFERSCHEIN_D_BELEGDATUM);

		String vertreterSort = "flrvertreter.c_kurzzeichen";

		if (iAnlegerStattVertreterAnzeigen == 1) {
			vertreterSort = "flrpersonalanleger.c_kurzzeichen";
		} else if (iAnlegerStattVertreterAnzeigen == 2) {
			vertreterSort = "flrpersonalaenderer.c_kurzzeichen";
		}

		columns.add("lp.vertreter", String.class, getTextRespectUISpr("lp.vertreter", mandant, locUi),
				QueryParameters.FLR_BREITE_XS, vertreterSort);
		columns.add("lp.status", Icon.class, getTextRespectUISpr("lp.status", mandant, locUi),
				QueryParameters.FLR_BREITE_XS, LieferscheinFac.FLR_LIEFERSCHEIN_LIEFERSCHEINSTATUS_STATUS_C_NR);
		columns.add("rechnung.rechnungsnummer", String.class,
				getTextRespectUISpr("rechnung.rechnungsnummer", mandant, locUi), 12,
				LieferscheinFac.FLR_LIEFERSCHEIN_FLRRECHNUNG + "." + RechnungFac.FLR_RECHNUNG_C_NR);

		if (bDarfPreiseSehen) {

			columns.add("lp.wert", BigDecimal.class, getTextRespectUISpr("lp.wert", mandant, locUi),
					QueryParameters.FLR_BREITE_PREIS, Facade.NICHT_SORTIERBAR);
			columns.add("waehrung", String.class, " ", QueryParameters.FLR_BREITE_WAEHRUNG,
					LieferscheinFac.FLR_LIEFERSCHEIN_WAEHRUNG_C_NR_LIEFERSCHEINWAEHRUNG);
		}

		if (bVerketteteLieferscheine) {
			columns.add("lp.verkettet.kopf", String.class, getTextRespectUISpr("lp.verkettet.kopf", mandant, locUi),
					QueryParameters.FLR_BREITE_M, "flrverkettet2.flrlieferschein.c_nr");
		}

		columns.add("lp.kommentar", String.class, getTextRespectUISpr("lp.kommentar", mandant, locUi), 1,
				Facade.NICHT_SORTIERBAR);
		
		columns.add("Color", Color.class, "", 1, "");

		return columns;
	}

	private void setupParameters() {
		try {
			bDarfPreiseSehen = getTheJudgeFac().hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF, theClientDto);

			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_ANZEIGE_ANLEGER_STATT_VERTRETER);
			iAnlegerStattVertreterAnzeigen = (Integer) parameter.getCWertAsObject();

			if (getMandantFac().hatZusatzfunktionberechtigung(
					com.lp.server.system.service.MandantFac.ZUSATZFUNKTION_LIEFERSCHEINE_VERKETTEN, theClientDto)) {
				bVerketteteLieferscheine = true;
			}

			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_LIEFERSCHEIN, ParameterFac.PARAMETER_AUFTRAG_AUS_POSITIONEN_IN_AUSWAHLLISTE);
			bAuftragAusPosition = (Boolean) parameter.getCWertAsObject();

			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_LIEFERSCHEIN,
					ParameterFac.PARAMETER_RECHNUNGSADRESSE_IN_LIEFERSCHEINAUSWAHL);
			bRechnungsadresseInLsAuswahl = (Boolean) parameter.getCWertAsObject();

		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}
	}

	public TableInfo getTableInfo() {
		TableInfo info = super.getTableInfo();
		if (info != null)
			return info;

		setupParameters();
		setTableColumnInformation(createColumnInformation(theClientDto.getMandant(), theClientDto.getLocUi()));

		TableColumnInformation c = getTableColumnInformation();
		info = new TableInfo(c.getClasses(), c.getHeaderNames(), c.getWidths(), c.getDbColumNames());
		setTableInfo(info);
		return info;
	}

	public PrintInfoDto getSDocPathAndPartner(Object key) {
		LieferscheinDto lieferscheinDto = null;
		KundeDto kundeDto = null;
		try {
			lieferscheinDto = getLieferscheinFac().lieferscheinFindByPrimaryKey((Integer) key, theClientDto);
			kundeDto = getKundeFac().kundeFindByPrimaryKeySmall(lieferscheinDto.getKundeIIdLieferadresse());
		} catch (Exception e) {
			// Nicht gefunden
		}
		if (lieferscheinDto != null) {
			// String sPath = JCRDocFac.HELIUMV_NODE + "/"
			// + theClientDto.getMandant() + "/"
			// + LocaleFac.BELEGART_LIEFERSCHEIN.trim() + "/"
			// + LocaleFac.BELEGART_LIEFERSCHEIN.trim() + "/"
			// + lieferscheinDto.getCNr().replace("/", ".");
			DocPath docPath = new DocPath(new DocNodeLieferschein(lieferscheinDto));
			return new PrintInfoDto(docPath, kundeDto.getPartnerIId(), getSTable());
		} else {
			return null;
		}
	}

	public String getSTable() {
		return "LIEFERSCHEIN";
	}
}
