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
package com.lp.server.bestellung.fastlanereader;

import java.awt.Color;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.swing.Icon;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebot.service.AngebotpositionDto;
import com.lp.server.angebotstkl.service.EinkaufsangebotDto;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikellieferant;
import com.lp.server.artikel.service.ArtikelbestelltFac;
import com.lp.server.artikel.service.ArtikellieferantstaffelDto;
import com.lp.server.artikel.service.SperrenIcon;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.bestellung.fastlanereader.generated.FLRBestellvorschlag;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellvorschlagDto;
import com.lp.server.bestellung.service.BestellvorschlagFLRDataDto;
import com.lp.server.bestellung.service.BestellvorschlagFac;
import com.lp.server.bestellung.service.BestellvorschlagHandlerFeature;
import com.lp.server.bestellung.service.BestellvorschlagQueryResult;
import com.lp.server.bestellung.service.IBestellvorschlagFLRData;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.forecast.service.ForecastDto;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.partner.service.LieferantbeurteilungDto;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
import com.lp.server.util.HelperServer;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
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

public class BestellvorschlagHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String FLR_BESTELLVORSCHLAG = "flrbestellvorschlag.";
	public static final String FLR_BESTELLVORSCHLAG_FROM_CLAUSE = "SELECT flrbestellvorschlag,(SELECT count(*) from FLRBestellvorschlag bv2 WHERE bv2.mandant_c_nr=flrbestellvorschlag.mandant_c_nr AND bv2.artikel_i_id=flrbestellvorschlag.artikel_i_id AND bv2.personal_i_id<>flrbestellvorschlag.personal_i_id) as anzahlbvandererbenutzer, (SELECT count(s.i_id) FROM FLRArtikelsperren as s WHERE s.artikel_i_id=flrbestellvorschlag.artikel_i_id AND ( s.flrsperren.b_gesperrteinkauf=1 OR s.flrsperren.b_gesperrt=1) ) as sperren from FLRBestellvorschlag flrbestellvorschlag ";

	boolean bZentralerArtikelstamm = false;
	boolean bLagerminJeLager = false;
	boolean bPersoenlicherBestellvorschlag = false;

	private Feature cachedFeature;

	private class Feature extends FlrFeatureBase<IBestellvorschlagFLRData> {
		private boolean enabled;

		public Feature() {
			super(getQuery());
		}

		@Override
		protected void initialize(QueryParametersFeatures qpf) {
			enabled = qpf.hasFeature(BestellvorschlagHandlerFeature.BESTELLVORSCHLAG_DATA);
		}

		public boolean isEnabled() {
			return enabled;
		}

		@Override
		protected IBestellvorschlagFLRData[] createFlrData(int rows) {
			return new BestellvorschlagFLRDataDto[rows];
		}

		@Override
		protected IBestellvorschlagFLRData createFlrDataObject(int index) {
			return new BestellvorschlagFLRDataDto();
		}

		public void setFeatureData(int row, FLRBestellvorschlag flrBestellvorschlag) {
			getFlrDataObject(row).setNoted(Helper.short2Boolean(flrBestellvorschlag.getB_vormerkung()));
		}
	}

	private Feature getFeature() {
		if (cachedFeature == null) {
			cachedFeature = new Feature();
		}
		return cachedFeature;
	}

	/**
	 * gets the data page for the specified row using the current query. The row at
	 * rowIndex will be located in the middle of the page.
	 * 
	 * @see UseCaseHandler#getPageAt(java.lang.Integer)
	 * @param rowIndex Integer
	 * @throws EJBExceptionLP
	 * @return QueryResult
	 */
	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		cachedFeature = null;

		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = BestellvorschlagHandler.PAGE_SIZE;
			int startIndex = Math.max(rowIndex.intValue() - (pageSize / 2), 0);
			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			session = setFilter(session);
			String queryString = this.getFromClause() + this.buildWhereClause() + this.buildOrderByClause();

			Query query = session.createQuery(queryString);

			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			String[] tooltipData = new String[resultList.size()];
			int row = 0;
			getFeature().setFlrRowCount(rows.length);

			while (resultListIterator.hasNext()) {

				Object[] o = (Object[]) resultListIterator.next();
				FLRBestellvorschlag bestellvorschlag = (FLRBestellvorschlag) o[0];

				rows[row][getTableColumnInformation().getViewIndex("i_id")] = bestellvorschlag.getI_id();

				if (o[1] != null) {
					rows[row][getTableColumnInformation().getViewIndex("S")] = o[1];
				}
				String artikelspr = null;
				if (o[2] != null) {
					artikelspr = ((com.lp.server.artikel.fastlanereader.generated.FLRArtikellistespr) o[2]).getC_bez();
				}

				rows[row][getTableColumnInformation().getViewIndex("bes.artikelcnr")] = bestellvorschlag
						.getFlrartikelliste().getC_nr();

				// belegartId
				if (bestellvorschlag.getI_belegartid() != null) {
					// Los
					if (bestellvorschlag.getBelegart_c_nr().equals(LocaleFac.BELEGART_LOS)) {

						rows[row][getTableColumnInformation().getViewIndex("bes.artikelbezeichnung")] = artikelspr;
					} else if (bestellvorschlag.getBelegart_c_nr().equals(LocaleFac.BELEGART_FORECAST)) {

						rows[row][getTableColumnInformation().getViewIndex("bes.artikelbezeichnung")] = artikelspr;
					}
					// Auftrag
					else if (bestellvorschlag.getBelegart_c_nr().equals(LocaleFac.BELEGART_AUFTRAG)) {

						AuftragpositionDto auftragposDto = null;

						auftragposDto = this.getAuftragpositionFac()
								.auftragpositionFindByPrimaryKeyOhneExc(bestellvorschlag.getI_belegartpositionid());
						if (auftragposDto != null && auftragposDto.getCBez() != null) {

							rows[row][getTableColumnInformation()
									.getViewIndex("bes.artikelbezeichnung")] = auftragposDto.getCBez() == null ? null
											: auftragposDto.getCBez();
						} else if (artikelspr != null) {
							rows[row][getTableColumnInformation().getViewIndex("bes.artikelbezeichnung")] = artikelspr;
						}

					} else if (bestellvorschlag.getBelegart_c_nr().equals(LocaleFac.BELEGART_ANGEBOT)) {

						if (bestellvorschlag.getI_belegartpositionid() != null) {
							AngebotpositionDto angebotposDto = null;

							angebotposDto = this.getAngebotpositionFac()
									.angebotpositionFindByPrimaryKeyOhneExc(bestellvorschlag.getI_belegartpositionid());
							if (angebotposDto != null && angebotposDto.getCBez() != null) {

								rows[row][getTableColumnInformation().getViewIndex(
										"bes.artikelbezeichnung")] = angebotposDto.getCBez() == null ? null
												: angebotposDto.getCBez();
							} else if (artikelspr != null) {
								rows[row][getTableColumnInformation()
										.getViewIndex("bes.artikelbezeichnung")] = artikelspr;
							}
						}

					}
					// Bestellung
					else if (bestellvorschlag.getBelegart_c_nr().equals(LocaleFac.BELEGART_BESTELLUNG)) {

						BestellpositionDto bestellpositionDto = null;

						bestellpositionDto = this.getBestellpositionFac()
								.bestellpositionFindByPrimaryKeyOhneExc(bestellvorschlag.getI_belegartpositionid());

						if (bestellpositionDto != null && bestellpositionDto.getCBez() != null) {
							rows[row][getTableColumnInformation().getViewIndex(
									"bes.artikelbezeichnung")] = bestellpositionDto.getCBez() == null ? null
											: bestellpositionDto.getCBez();
						} else if (artikelspr != null) {
							rows[row][getTableColumnInformation().getViewIndex("bes.artikelbezeichnung")] = artikelspr;
						}
					}
				} else {
					if (artikelspr != null) {
						rows[row][getTableColumnInformation().getViewIndex("bes.artikelbezeichnung")] = artikelspr;
					}
				}
				if (bZentralerArtikelstamm) {
					rows[row][getTableColumnInformation().getViewIndex("report.mandant")] = bestellvorschlag
							.getMandant_c_nr();
				}

				// PJ19509
				// offene Rahmenbestellmenge
				Hashtable<?, ?> htRahmenbestellt = getArtikelbestelltFac()
						.getAnzahlRahmenbestellt(bestellvorschlag.getArtikel_i_id(), theClientDto);
				if (htRahmenbestellt.containsKey(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL)) {

					BigDecimal bdOffen = (BigDecimal) htRahmenbestellt
							.get(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL);

					if (bdOffen != null && bdOffen.doubleValue() > 0) {

						rows[row][getTableColumnInformation().getViewIndex("bes.rahmenmengevorhanden")] = "R";
					}
				}

				rows[row][getTableColumnInformation().getViewIndex("bes.zubestellendeMenge")] = bestellvorschlag
						.getN_zubestellendemenge();
				rows[row][getTableColumnInformation().getViewIndex("bes.bestelltermin")] = bestellvorschlag
						.getT_liefertermin();
				if (bestellvorschlag.getLieferant_i_id() != null) {
					rows[row][getTableColumnInformation().getViewIndex("bes.lieferantcnr")] = HelperServer
							.formatNameAusFLRPartner(bestellvorschlag.getFlrlieferant().getFlrpartner());

					if (bestellvorschlag.getFlrlieferant().getT_bestellsperream() != null) {
						rows[row][getTableColumnInformation().getViewIndex("SL")] = LocaleFac.STATUS_GESPERRT;
					}

					LieferantbeurteilungDto[] bDtos = getLieferantFac()
							.lieferantbeurteilungfindByLetzteBeurteilungByLieferantIId(
									bestellvorschlag.getLieferant_i_id(),
									new java.sql.Timestamp(System.currentTimeMillis()));

					if (bDtos != null && bDtos.length > 0) {
						rows[row][getTableColumnInformation().getViewIndex("ABC")] = bDtos[0].getCKlasse();
					}

				}

				
				if(bestellvorschlag.getFlrartikel().getFlrliefergruppe()!=null) {
					rows[row][getTableColumnInformation().getViewIndex(
							"artikel.liefergruppe")] = bestellvorschlag.getFlrartikel().getFlrliefergruppe().getC_nr();
				}
				
				
				rows[row][getTableColumnInformation().getViewIndex(
						"artikel.wiederbeschaffungszeit.short")] = bestellvorschlag.getI_wiederbeschaffungszeit();

				rows[row][getTableColumnInformation()
						.getViewIndex("bes.nettogesamtpreisminusrabatte")] = bestellvorschlag.getN_nettogesamtpreis();

				rows[row][getTableColumnInformation().getViewIndex("bes.belegart")] = bestellvorschlag
						.getBelegart_c_nr();

				BestellungDto bestellungDto = null;
				BestellpositionDto besposDto = null;
				AuftragDto auftragDto = null;

				if (bestellvorschlag.getI_belegartid() != null) {
					if (bestellvorschlag.getBelegart_c_nr().equals(LocaleFac.BELEGART_BESTELLUNG)) {
						besposDto = getBestellpositionFac()
								.bestellpositionFindByPrimaryKeyOhneExc(bestellvorschlag.getI_belegartpositionid());

						if (besposDto != null) {
							bestellungDto = getBestellungFac().bestellungFindByPrimaryKey(besposDto.getBestellungIId());

							rows[row][getTableColumnInformation()
									.getViewIndex("bes.belegartnummer")] = bestellungDto.getCNr() == null ? null
											: bestellungDto.getCNr();
						} else {
							rows[row][getTableColumnInformation()
									.getViewIndex("bes.belegartnummer")] = "Position gel\u00F6scht";
						}
					} else if (bestellvorschlag.getBelegart_c_nr().equals(LocaleFac.BELEGART_AUFTRAG)) {
						auftragDto = getAuftragFac().auftragFindByPrimaryKey(bestellvorschlag.getI_belegartid());
						rows[row][getTableColumnInformation().getViewIndex(
								"bes.belegartnummer")] = auftragDto.getCNr() == null ? null : auftragDto.getCNr();
					} else if (bestellvorschlag.getBelegart_c_nr().equals(LocaleFac.BELEGART_ANGEBOT)) {
						AngebotDto angebotDto = getAngebotFac()
								.angebotFindByPrimaryKey(bestellvorschlag.getI_belegartid(), theClientDto);
						rows[row][getTableColumnInformation().getViewIndex("bes.belegartnummer")] = angebotDto.getCNr();
					}
					// Los
					else if (bestellvorschlag.getBelegart_c_nr().equals(LocaleFac.BELEGART_LOS)) {

						LosDto losDto = getFertigungFac()
								.losFindByPrimaryKeyOhneExc(bestellvorschlag.getI_belegartid());

						if (losDto != null) {
							rows[row][getTableColumnInformation().getViewIndex("bes.belegartnummer")] = losDto.getCNr();
						}
					}
					// Forecast
					else if (bestellvorschlag.getBelegart_c_nr().equals(LocaleFac.BELEGART_FORECAST)) {
						ForecastDto fDto = getForecastFac()
								.forecastFindByPrimaryKeyOhneExc(bestellvorschlag.getI_belegartid());
						rows[row][getTableColumnInformation().getViewIndex("bes.belegartnummer")] = fDto.getCNr();
					} else if (bestellvorschlag.getBelegart_c_nr().equals(LocaleFac.BELEGART_EINKAUFSANGEBOT)) {
						EinkaufsangebotDto ekagDto = getAngebotstklFac()
								.einkaufsangebotFindByPrimaryKey(bestellvorschlag.getI_belegartid());
						rows[row][getTableColumnInformation().getViewIndex("bes.belegartnummer")] = ekagDto.getCNr();
					}

				}
				
				rows[row][getTableColumnInformation().getViewIndex("ww.lagermindest")] = bestellvorschlag
						.getF_lagermindest();
				

				if (bestellvorschlag.getX_textinhalt() != null && !bestellvorschlag.getX_textinhalt().equals("")) {
					rows[row][getTableColumnInformation().getViewIndex("lp.text")] = new Boolean(true);
					String text = bestellvorschlag.getX_textinhalt();
					text = text.replaceAll("\n", "<br>");
					text = "<html>" + text + "</html>";
					tooltipData[row] = text;
				} else {
					rows[row][getTableColumnInformation().getViewIndex("lp.text")] = new Boolean(false);
				}

				// PJ18566

				Calendar c = Calendar.getInstance();
				c.setTime(bestellvorschlag.getT_liefertermin());
				c.add(Calendar.DATE, 1);

				boolean bEsgibtPositiveArtikelbestelltNachStichtag = getArtikelbestelltFac()
						.gibtEsPositiveArtikelbestelltNachStichtag(bestellvorschlag.getArtikel_i_id(),
								Helper.cutDate(new java.sql.Date(c.getTimeInMillis())));

				if (bLagerminJeLager) {
					if (bestellvorschlag.getFlrpartner_standort() != null) {
						rows[row][getTableColumnInformation().getViewIndex("system.standort")] = bestellvorschlag
								.getFlrpartner_standort().getC_kbez();
					}
				}

				String gebinde = null;

				if (bestellvorschlag.getLieferant_i_id() != null) {
					if (getArtikelFac()
							.getGebindeEinesArtikelsUndEinesLieferanten(bestellvorschlag.getArtikel_i_id(),
									bestellvorschlag.getLieferant_i_id(),
									Helper.cutDate(new java.sql.Date(System.currentTimeMillis())), theClientDto)
							.size() > 0) {

						if (bestellvorschlag.getGebinde_i_id() == null) {
							gebinde = "?";
						} else {
							gebinde = "v";
						}

					}
				}

				rows[row][getTableColumnInformation().getViewIndex("artikel.gebinde")] = gebinde;

				if (bestellvorschlag.getT_bearbeitet() == null) {
					rows[row][getTableColumnInformation().getViewIndex("lp.bearbeitet")] = Boolean.FALSE;
				} else {
					rows[row][getTableColumnInformation().getViewIndex("lp.bearbeitet")] = Boolean.TRUE;
				}

				if (!Helper.short2boolean(bestellvorschlag.getFlrartikelliste().getB_lagerbewirtschaftet())) {
					if (bEsgibtPositiveArtikelbestelltNachStichtag == true) {
						rows[row][getTableColumnInformation().getViewIndex("Color")] = new Color(139, 139, 0);
					} else {
						rows[row][getTableColumnInformation().getViewIndex("Color")] = Color.BLUE;
					}

				} else {
					if (bEsgibtPositiveArtikelbestelltNachStichtag == true) {
						rows[row][getTableColumnInformation().getViewIndex("Color")] = new Color(34, 139, 34);
					}
				}

				// PJ19999
				if (Helper.short2boolean(bestellvorschlag.getFlrartikel().getB_keine_lagerzubuchung())) {
					// Uebersteuert alle anderen Farben
					rows[row][getTableColumnInformation().getViewIndex("Color")] = Color.LIGHT_GRAY;
				}

				// PJ20901
				rows[row][getTableColumnInformation().getViewIndex("artikel.vorzugsteil")] = o[4];

				// PJ20810

				if (bPersoenlicherBestellvorschlag && o[3] != null && ((Long) o[3]) > 0) {
					rows[row][getTableColumnInformation().getViewIndex("Color")] = Color.RED;
				}

				getFeature().setFeatureData(row, bestellvorschlag);
				row++;
			}

			if (getFeature().isEnabled()) {
				BestellvorschlagQueryResult vorschlagResult = new BestellvorschlagQueryResult(rows, this.getRowCount(),
						startIndex, endIndex, 0);
				vorschlagResult.setFlrData(getFeature().getFlrData());
				result = vorschlagResult;
			} else {
				result = new QueryResult(rows, this.getRowCount(), startIndex, endIndex, 0, tooltipData);
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
	 * @see UseCaseHandler#getRowCountFromDataBase()
	 * @return int
	 */
	protected long getRowCountFromDataBase() {
		long rowCount = 0;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			String queryString = "select count(distinct bestellvorschlag.i_id) from FLRBestellvorschlag bestellvorschlag  LEFT OUTER JOIN bestellvorschlag.flrartikelliste.artikelsprset AS aspr  LEFT OUTER JOIN bestellvorschlag.flrartikelliste.flrvorzug AS vz  LEFT OUTER JOIN bestellvorschlag.flrartikelliste.flrartikelgruppe AS ag "
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

	// /**
	// * gets the total number of rows represented by the current query.
	// *
	// * @see UseCaseHandler#getRowCountFromDataBase()
	// * @return int
	// */
	// protected int getRowCountFromDataBase() {
	// int rowCount = 0;
	// SessionFactory factory = FLRSessionFactory.getFactory();
	// Session session = null;
	// try {
	// session = factory.openSession();
	// String queryString = "select count(*) " + this.getFromClause()
	// + this.buildWhereClause();
	// myLogger.logData("getRowCountFromDataBase(), HQL Query = " +
	// queryString);
	// Query query = session.createQuery(queryString);
	// List rowCountResult = query.list();
	// if (rowCountResult != null && rowCountResult.size() > 0) {
	// rowCount = ( (Integer) rowCountResult.get(0)).intValue();
	// }
	// }
	// catch (Exception e) {
	// e.printStackTrace();
	// }
	// finally {
	// closeSession(session);
	// }
	// return rowCount;
	// }

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

					if (filterKriterien[i].kritName.equals("c_volltext")) {
						String suchstring = "lower(coalesce(bestellvorschlag.flrartikelliste.c_referenznr,'')||' '||coalesce(aspr.c_bez,'')||' '||coalesce(aspr.c_kbez,'')||' '||coalesce(aspr.c_zbez,'')||' '||coalesce(aspr.c_zbez2,''))";

						String[] teile = filterKriterien[i].value.toLowerCase().split(" ");
						where.append("(");

						for (int p = 0; p < teile.length; p++) {

							if (teile[p].startsWith("-")) {
								where.append(" NOT ");

								teile[p] = teile[p].substring(1);

							}

							where.append("lower(" + suchstring + ") like '%" + teile[p].toLowerCase() + "%'");
							if (p < teile.length - 1) {
								where.append(" AND ");
							}
						}

						where.append(")");
					} else if (filterKriterien[i].kritName.equals("ag.i_id")) {
						where.append(" " + filterKriterien[i].kritName);
						where.append(" " + filterKriterien[i].operator);
						where.append(" " + filterKriterien[i].value);
					} else if (filterKriterien[i].kritName.equals("vz.c_nr")) {

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
					} else if (filterKriterien[i].kritName
							.equals("flrlieferant.flrpartner.c_name1nachnamefirmazeile1")) {

						where.append(
								"( lower(bestellvorschlag.flrlieferant.flrpartner.c_name1nachnamefirmazeile1) LIKE "
										+ filterKriterien[i].value.toLowerCase() + " ");

						where.append(" OR lower(bestellvorschlag.flrlieferant.flrpartner.c_kbez) LIKE "
								+ filterKriterien[i].value.toLowerCase() + ") ");

					} else {

						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" lower(bestellvorschlag." + filterKriterien[i].kritName + ")");
						} else {
							where.append(" bestellvorschlag." + filterKriterien[i].kritName);
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

							// spezial unterscheidung nur in diesem haendler
							if (kriterien[i].kritName.toString().equals("c_nr")) {
								orderBy.append("bestellvorschlag." + "flrartikel.c_nr");
								orderBy.append(" ");
								orderBy.append(kriterien[i].value);

							}

							else if (kriterien[i].kritName.toString().equals("c_bez")) {
								orderBy.append("aspr.c_bez");
								orderBy.append(" ");
								orderBy.append(kriterien[i].value);
							} else if (kriterien[i].kritName.toString().equals("vz.c_nr")) {
								orderBy.append("vz.c_nr");
								orderBy.append(" ");
								orderBy.append(kriterien[i].value);
							} else {
								orderBy.append("bestellvorschlag." + kriterien[i].kritName);
								orderBy.append(" ");
								orderBy.append(kriterien[i].value);

							}
						}
					}
				}
			} else {
				// no sort criteria found, add default sort
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append("bestellvorschlag.flrartikel.").append("c_nr").append(" ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("bestellvorschlag." + "i_id") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" bestellvorschlag.").append("i_id").append(" ");
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
		return "SELECT bestellvorschlag,  (SELECT max(s.flrsperren.c_bez) FROM FLRArtikelsperren as s WHERE s.artikel_i_id=bestellvorschlag.artikel_i_id AND ( s.flrsperren.b_gesperrteinkauf=1 OR s.flrsperren.b_gesperrt=1) ) as sperren, aspr, (SELECT count(*) from FLRBestellvorschlag bv2 WHERE bv2.mandant_c_nr=bestellvorschlag.mandant_c_nr AND bv2.artikel_i_id=bestellvorschlag.artikel_i_id AND bv2.personal_i_id<>bestellvorschlag.personal_i_id) as anzahlbvandererbenutzer, vz.c_nr from FLRBestellvorschlag bestellvorschlag LEFT OUTER JOIN bestellvorschlag.flrartikelliste.artikelsprset AS aspr LEFT OUTER JOIN bestellvorschlag.flrartikelliste.flrvorzug AS vz LEFT OUTER JOIN bestellvorschlag.flrartikel.flrliefergruppe AS lg LEFT OUTER JOIN bestellvorschlag.flrartikelliste.flrartikelgruppe AS ag ";
	}

	/**
	 * sorts the data described by the current query using the specified sort
	 * criterias. The current query is also updated with the new sort criterias.
	 * 
	 * @see UseCaseHandler#sort(SortierKriterium[], Object)
	 * @throws EJBExceptionLP
	 * @param sortierKriterien SortierKriterium[]
	 * @param selectedId       Object
	 * @return QueryResult
	 */
	public QueryResult sort(SortierKriterium[] sortierKriterien, Object selectedId) throws EJBExceptionLP {
		this.getQuery().setSortKrit(sortierKriterien);

		QueryResult result = null;
		int rowNumber = 0;

		// if (selectedId != null && ( (Integer) selectedId).intValue() >= 0) {
		if (selectedId != null) {
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();
				String queryString = "select bestellvorschlag." + "i_id"
						+ " from FLRBestellvorschlag bestellvorschlag  LEFT OUTER JOIN bestellvorschlag.flrartikelliste.artikelsprset AS aspr  LEFT OUTER JOIN bestellvorschlag.flrartikelliste.flrvorzug AS vz LEFT OUTER JOIN bestellvorschlag.flrartikelliste.flrartikelgruppe AS ag "
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
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
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

	/**
	 * gets information about the Bestellungstable.
	 * 
	 * @return TableInfo
	 */

	private TableColumnInformation createColumnInformation(String mandant, Locale locUi) {
		TableColumnInformation columns = new TableColumnInformation();

		try {
			int iNachkommastellenPreis = getMandantFac().getNachkommastellenPreisEK(theClientDto.getMandant());

			columns.add("i_id", Integer.class, "i_id", -1, "i_id");
			columns.add("S", SperrenIcon.class, getTextRespectUISpr("bes.sperre", mandant, locUi),
					QueryParameters.FLR_BREITE_S, Facade.NICHT_SORTIERBAR,
					getTextRespectUISpr("bes.sperre.tooltip", mandant, locUi));

			columns.add("bes.artikelcnr", String.class, getTextRespectUISpr("bes.artikelcnr", mandant, locUi),
					QueryParameters.FLR_BREITE_XM, "c_nr");
			columns.add("bes.artikelbezeichnung", String.class,
					getTextRespectUISpr("bes.artikelbezeichnung", mandant, locUi), QueryParameters.FLR_BREITE_XM,
					"c_bez");

			if (bZentralerArtikelstamm) {

				columns.add("report.mandant", String.class, getTextRespectUISpr("report.mandant", mandant, locUi),
						QueryParameters.FLR_BREITE_XS, "mandant_c_nr");
			}

			columns.add("bes.rahmenmengevorhanden", String.class,
					getTextRespectUISpr("bes.rahmenmengevorhanden.short", mandant, locUi), QueryParameters.FLR_BREITE_S,
					Facade.NICHT_SORTIERBAR, getTextRespectUISpr("bes.rahmenmengevorhanden.tooltip", mandant, locUi));

			columns.add("bes.zubestellendeMenge", BigDecimal.class,
					getTextRespectUISpr("bes.zubestellendeMenge", mandant, locUi),
					QueryParameters.FLR_BREITE_SHARE_WITH_REST,
					BestellvorschlagFac.FLR_BESTELLVORSCHLAG_N_ZUBESTELLENDEMENGE);

			columns.add("bes.bestelltermin", Date.class, getTextRespectUISpr("bes.bestelltermin", mandant, locUi),
					QueryParameters.FLR_BREITE_SHARE_WITH_REST,
					BestellvorschlagFac.FLR_BESTELLVORSCHLAG_T_LIEFERTERMIN);

			columns.add("SL", Icon.class, getTextRespectUISpr("bes.sperre", mandant, locUi),
					QueryParameters.FLR_BREITE_S, Facade.NICHT_SORTIERBAR,
					getTextRespectUISpr("lieferant.bestellsperre", mandant, locUi));
			columns.add("ABC", String.class, getTextRespectUISpr("lp.beurteilung", mandant, locUi),
					QueryParameters.FLR_BREITE_S, Facade.NICHT_SORTIERBAR,
					getTextRespectUISpr("lp.beurteilung", mandant, locUi));

			columns.add("bes.lieferantcnr", String.class, getTextRespectUISpr("bes.lieferantcnr", mandant, locUi),
					QueryParameters.FLR_BREITE_M,
					BestellvorschlagFac.FLR_BESTELLVORSCHLAG_FLRLIEFERANT + "." + LieferantFac.FLR_LIEFERANT_I_ID);

			columns.add("artikel.wiederbeschaffungszeit.short", Integer.class,
					getTextRespectUISpr("artikel.wiederbeschaffungszeit.short", mandant, locUi),
					QueryParameters.FLR_BREITE_SHARE_WITH_REST, "i_wiederbeschaffungszeit",
					getTextRespectUISpr("artikel.wiederbeschaffungszeit.tooltip", mandant, locUi));

			//PJ21761
			columns.add("artikel.liefergruppe", String.class,
					getTextRespectUISpr("artikel.liefergruppe", mandant, locUi),
					QueryParameters.FLR_BREITE_SHARE_WITH_REST, "flrartikel.flrliefergruppe.c_nr",
					getTextRespectUISpr("artikel.liefergruppe", mandant, locUi));
			
			columns.add("bes.nettogesamtpreisminusrabatte",
					super.getUIClassBigDecimalNachkommastellen(iNachkommastellenPreis),
					getTextRespectUISpr("bes.nettogesamtpreisminusrabatte", mandant, locUi),
					getUIBreiteAbhaengigvonNachkommastellen(QueryParameters.PREIS, iNachkommastellenPreis),
					BestellvorschlagFac.FLR_BESTELLVORSCHLAG_N_NETTOGESAMTPREISMINUSRABATTE);

			columns.add("bes.belegart", String.class, getTextRespectUISpr("bes.belegart", mandant, locUi),
					QueryParameters.FLR_BREITE_SHARE_WITH_REST, BestellvorschlagFac.FLR_BESTELLVORSCHLAG_BELEGART_C_NR);

			columns.add("bes.belegartnummer", String.class, getTextRespectUISpr("bes.belegartnummer", mandant, locUi),
					QueryParameters.FLR_BREITE_M, BestellvorschlagFac.FLR_BESTELLVORSCHLAG_I_BELEGARTID);

			columns.add("lp.text", Boolean.class, getTextRespectUISpr("lp.text", mandant, locUi),
					QueryParameters.FLR_BREITE_M, Facade.NICHT_SORTIERBAR);

			if (bLagerminJeLager) {
				columns.add("system.standort", String.class, getTextRespectUISpr("system.standort", mandant, locUi),
						QueryParameters.FLR_BREITE_M, "flrpartner_standort.c_kbez");
			}

			columns.add("artikel.gebinde", String.class, getTextRespectUISpr("artikel.gebinde", mandant, locUi),
					QueryParameters.FLR_BREITE_XXS, Facade.NICHT_SORTIERBAR);

			columns.add("artikel.vorzugsteil", String.class, getTextRespectUISpr("artikel.vorzugsteil", mandant, locUi),
					QueryParameters.FLR_BREITE_S, "vz.c_nr");

			columns.add("lp.bearbeitet", Boolean.class, getTextRespectUISpr("lp.bearbeitet", mandant, locUi),
					QueryParameters.FLR_BREITE_XS, BestellvorschlagFac.FLR_BESTELLVORSCHLAG_T_BEARBEITET + ",bestellvorschlag."
							+ BestellvorschlagFac.FLR_BESTELLVORSCHLAG_FLRARTIKEL + ".c_nr");

			columns.add("ww.lagermindest", Double.class, getTextRespectUISpr("ww.lagermindest", mandant, locUi),
					QueryParameters.FLR_BREITE_MENGE, "f_lagermindest");
			
			columns.add("Color", Color.class, "", 1, "");
		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}
		return columns;
	}

	public TableInfo getTableInfo() {

		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
				theClientDto)) {
			bZentralerArtikelstamm = true;
		}
		try {
			ParametermandantDto param = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_LAGERMIN_JE_LAGER);
			bLagerminJeLager = (Boolean) param.getCWertAsObject();
		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		bPersoenlicherBestellvorschlag = getParameterFac().getPersoenlicherBestellvorschlag(theClientDto.getMandant());

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

	// ##########################################################################
	// #####
	/**
	 * 
	 * @param aFilterKriteriumI FilterKriterium[]
	 * @return String
	 */
	private static String buildBestellvorschlagWhereClause(FilterKriterium[] aFilterKriteriumI, String mandantCNr) {
		StringBuffer where = new StringBuffer("");

		if (aFilterKriteriumI != null && aFilterKriteriumI.length > 0) {
			for (int i = 0; i < aFilterKriteriumI.length; i++) {
				if (aFilterKriteriumI[i].value != null) {
					where.append(FLR_BESTELLVORSCHLAG + aFilterKriteriumI[i].kritName)
							.append(" " + aFilterKriteriumI[i].operator + " ").append(aFilterKriteriumI[i].value + " ")
							.append(" AND ");
				}
			}
		}

		String whereString = "";

		where.insert(0, " WHERE  " + FLR_BESTELLVORSCHLAG + "mandant_c_nr='" + mandantCNr + "' AND ");

		whereString = where.substring(0, where.length() - 5); // das letzte
		// " AND "
		// abschneiden

		return whereString;
	}

	private static String buildBestellvorschlagOrderByClause() {

		StringBuffer orderby = new StringBuffer("");

		orderby.append("flrbestellvorschlag.").append(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_T_LIEFERTERMIN)
				.append(" ASC ")

				.append(", ").append(" flrbestellvorschlag.").append("i_id").append(" ");

		String orderbyString = "";

		if (orderby.length() > 0) {
			orderby.insert(0, " ORDER BY ");
			orderbyString = orderby.toString();
		}
		return orderbyString;
	}

	/**
	 * reportflr: 2 Diese Methode liefert eine Liste von allen Bestellvorschlaegen
	 * eines Mandanten, die nach den eingegebenen Kriterien des Benutzers
	 * zusammengestellt wird. <br>
	 * Achtung: Hibernate verwendet lazy initialization, d.h. der Zugriff auf
	 * Collections muss innerhalb der Session erfolgen.
	 * 
	 * @return ReportAngebotAlleDto[] die Liste der Angebote.
	 * @throws EJBExceptionLP Ausnahme
	 * @param aFilterKriteriumI  FilterKriterium[]
	 * @param aSortierKriteriumI SortierKriterium[]
	 * @param whichFilter        String
	 */
	public static BestellvorschlagDto[] getListeBestellvorschlaege(FilterKriterium[] aFilterKriteriumI,
			SortierKriterium[] aSortierKriteriumI, String whichFilter, String mandantCNr,
			boolean gemeinsameArtikelBestellen, boolean bInclGesperrteArtikel) throws EJBExceptionLP {
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		ArrayList<BestellvorschlagDto> alBVDtos = new ArrayList<BestellvorschlagDto>();
		try {
			session = factory.openSession();
			String queryString = "";
			if (whichFilter.equals(BestellvorschlagFac.BES_NACH_BV_FUER_JEDEN_LF_UND_GLEICHE_TERMIN)) {
				queryString = FLR_BESTELLVORSCHLAG_FROM_CLAUSE
						+ BestellvorschlagHandler.buildBestellvorschlagWhereClause(aFilterKriteriumI, mandantCNr)
						+ BestellvorschlagHandler.buildBestellvorschlagOrderByClause();

			}

			else if (whichFilter.equals(BestellvorschlagFac.BES_NACH_BV_FUER_JEDEN_LF)) {
				queryString = FLR_BESTELLVORSCHLAG_FROM_CLAUSE
						+ BestellvorschlagHandler.buildBestellvorschlagWhereClause(aFilterKriteriumI, mandantCNr)
						+ BestellvorschlagHandler.buildBestellvorschlagOrderByClause();

			} else if (whichFilter.equals(BestellvorschlagFac.BES_NACH_BV_FUER_BESTIMMTEN_LF_UND_TERMIN)) {
				queryString = FLR_BESTELLVORSCHLAG_FROM_CLAUSE
						+ BestellvorschlagHandler.buildBestellvorschlagWhereClause(aFilterKriteriumI, mandantCNr);
			} else if (whichFilter.equals(BestellvorschlagFac.BES_NACH_BV_FUER_BESTIMMTEN_LF)) {
				queryString = FLR_BESTELLVORSCHLAG_FROM_CLAUSE
						+ BestellvorschlagHandler.buildBestellvorschlagWhereClause(aFilterKriteriumI, mandantCNr)
						+ BestellvorschlagHandler.buildBestellvorschlagOrderByClause();

			} else if (whichFilter.equals(BestellvorschlagFac.BES_ABRUFE_ZU_RAHMEN)) {
				queryString = FLR_BESTELLVORSCHLAG_FROM_CLAUSE
						+ BestellvorschlagHandler.buildBestellvorschlagWhereClause(aFilterKriteriumI, mandantCNr)
						+ BestellvorschlagHandler.buildBestellvorschlagOrderByClause();
			}

			Query query = session.createQuery(queryString);
			List<?> list = query.list();

			Iterator<?> it = list.iterator();
			BestellvorschlagDto bestellvorschlagDto = null;

			while (it.hasNext()) {

				Object[] o = (Object[]) it.next();
				FLRBestellvorschlag flrbestellvorschlag = (FLRBestellvorschlag) o[0];

				// PJ20810
				if (gemeinsameArtikelBestellen == false && o[1] != null && ((Long) o[1]) > 0) {
					// gemeinsame auslassen
					continue;
				}
				
				//SP8810
				Long anzSperen=(Long)o[2];
				if(anzSperen!=null && anzSperen>0 && !bInclGesperrteArtikel) {
					continue;
				}
				

				bestellvorschlagDto = new BestellvorschlagDto();
				bestellvorschlagDto.setIId(flrbestellvorschlag.getI_id());
				bestellvorschlagDto.setIArtikelId(flrbestellvorschlag.getArtikel_i_id());
				bestellvorschlagDto.setNZubestellendeMenge(flrbestellvorschlag.getN_zubestellendemenge());
				bestellvorschlagDto.setCBelegartCNr(flrbestellvorschlag.getBelegart_c_nr());
				bestellvorschlagDto.setCMandantCNr(flrbestellvorschlag.getMandant_c_nr());
				bestellvorschlagDto.setIBelegartId(flrbestellvorschlag.getI_belegartid());
				bestellvorschlagDto.setIBelegartpositionid(flrbestellvorschlag.getI_belegartpositionid());
				bestellvorschlagDto.setILieferantId(flrbestellvorschlag.getLieferant_i_id());
				bestellvorschlagDto.setIWiederbeschaffungszeit(flrbestellvorschlag.getI_wiederbeschaffungszeit());
				bestellvorschlagDto.setNNettoeinzelpreis(flrbestellvorschlag.getN_nettoeinzelpreis());
				bestellvorschlagDto.setNNettogesamtpreis(flrbestellvorschlag.getN_nettogesamtpreis());
				bestellvorschlagDto.setNRabattbetrag(flrbestellvorschlag.getN_rabattbetrag());
				bestellvorschlagDto.setTLiefertermin((Timestamp) flrbestellvorschlag.getT_liefertermin());
				bestellvorschlagDto.setBNettopreisuebersteuert(flrbestellvorschlag.getB_nettopreisuebersteuert());
				bestellvorschlagDto.setProjektIId(flrbestellvorschlag.getProjekt_i_id());
				bestellvorschlagDto.setPartnerIIdStandort(flrbestellvorschlag.getPartner_i_id_standort());
				bestellvorschlagDto.setGebindeIId(flrbestellvorschlag.getGebinde_i_id());
				bestellvorschlagDto.setNAnzahlgebinde(flrbestellvorschlag.getN_anzahlgebinde());
				bestellvorschlagDto.setXTextinhalt(flrbestellvorschlag.getX_textinhalt());
				bestellvorschlagDto.setPersonalIId(flrbestellvorschlag.getPersonal_i_id());

				alBVDtos.add(bestellvorschlagDto);

			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, new Exception(t));
		} finally {
			closeSession(session);
		}

		return alBVDtos.toArray(new BestellvorschlagDto[alBVDtos.size()]);
	}

}
