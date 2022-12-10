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
package com.lp.server.artikel.fastlanereader;

import java.awt.Color;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.Icon;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikelsperren;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ArtikellisteFLRDataDto;
import com.lp.server.artikel.service.ArtikellisteHandlerFeature;
import com.lp.server.artikel.service.ArtikellisteQueryResult;
import com.lp.server.artikel.service.IArtikellisteFLRData;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.LagerFac;
import com.lp.server.artikel.service.SperrenIcon;
import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeArtikel;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
import com.lp.server.util.HelperServer;
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
import com.lp.util.Helper;
import com.lp.util.siprefixparser.DefaultSiPrefixParser;

/**
 * <p>
 * Hier wird die FLR Funktionalit&auml;t f&uuml;r den Artikel implementiert. Pro
 * UseCase gibt es einen Handler.
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
	int bVkPreisStattGestpreis = 0;
	boolean bVkPreisLief1preis = false;
	boolean bLief1Infos = false;
	boolean bTextsucheInklusiveArtikelnummer = false;
	boolean bTextsucheInklusiveIndexRevision = false;
	boolean bTextsucheInklusiveHersteller = false;
	private boolean bArtikelgruppeAnzeigen = false;
	private boolean bZusatzbezeichnung2Anzeigen = false;
	private boolean bLagerplaetzeAnzeigen = false;
	private boolean bArtikelklasseAnzeigen = false;
	private boolean bKurzbezeichnungAnzeigen = false;
	private boolean bDarfPreiseSehen = false;
	private boolean bReferenznummerAnzeigen = false;
	private boolean bHerstellernummerAnzeigen = false;
	private boolean bMaterialAnzeigen = false;
	private boolean bLagerstandDesAnderenMandantenAnzeigen = false;
	private boolean bVerfuegbarkeitAnzeigen = false;
	private int lagerIIdZusaetzlichesLager = -1;
	private LagerDto lagerDtoZusaetzlichesLager = null;
	private boolean bArtikelsucheInclReferenznummer = false;
	private String lagerCNrZusaetzlichesLager = null;
	private boolean bDualUse = false;

	boolean bArtikelfreigabe = false;

	private String artikelnummerOhne = null;

	private int vkPreisliste = -1;
	private Feature cachedFeature = null;
	private String cachedWhereClause = null;

	private class Feature extends QueryFeature<IArtikellisteFLRData> {
		private boolean featureEinheitCnr = false;
		private boolean featureKundenartikelnummer = false;
		private boolean featureSoko = false;

		private Integer kundeId;
		private Date sokoDate;
		private Integer partnerId;

		public Feature() {
			if (getQuery() instanceof QueryParametersFeatures) {
				initializeFeature((QueryParametersFeatures) getQuery());
			}
		}

		@Override
		protected void initializeFeature(QueryParametersFeatures query) {
			featureEinheitCnr = query.hasFeature(ArtikellisteHandlerFeature.EINHEIT_CNR);
			featureKundenartikelnummer = query.hasFeatureValue(ArtikellisteHandlerFeature.KUNDENARTIKELNUMMER_CNR);
			featureSoko = query.hasFeature(ArtikellisteHandlerFeature.SOKO);

			if (featureKundenartikelnummer) {
				try {
					partnerId = Integer
							.parseInt(query.getFeatureValue(ArtikellisteHandlerFeature.KUNDENARTIKELNUMMER_CNR));
				} catch (NumberFormatException e) {
					myLogger.error("Feature '" + ArtikellisteHandlerFeature.KUNDENARTIKELNUMMER_CNR
							+ "' hat keine auswertbare PartnerId! (Deaktiviere Feature)");
					featureKundenartikelnummer = false;
				}
			}
		}

		@Override
		protected IArtikellisteFLRData[] createFlrData(int rows) {
			return new ArtikellisteFLRDataDto[rows];
		}

		public boolean hasFeatureEinheitCnr() {
			return featureEinheitCnr;
		}

		public boolean hasFeatureKundenartikelCnr() {
			return featureKundenartikelnummer;
		}

		public boolean hasFeatureSoko() {
			return featureSoko;
		}

		private Integer getKundeIdFromPartner(Integer partnerId) throws RemoteException {
			if (kundeId == null) {
				KundeDto kundeDto = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(partnerId,
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

		public String getSokoDateAsString() {
			return Helper.formatDateWithSlashes(getSokoDate());
		}

		//
		// private void setKundenartikelnummer(int row, String kundenartikelCnr)
		// {
		// IArtikellisteFLRData flrDataEntry = getFlrDataObject(row) ;
		// if(flrDataEntry == null) {
		// flrDataEntry = new ArtikellisteFLRDataDto() ;
		// setFlrDataObject(row, flrDataEntry);
		// }
		//
		// flrDataEntry.setKundenartikelCnr(kundenartikelCnr);
		// }

		// protected void buildKundenartikelnummer(int row, Integer itemId,
		// Integer partnerId) {
		// try {
		// KundesokoDto dto =
		// getKundesokoFac().kundesokoFindByKundeIIdArtikelIIdGueltigkeitsdatumOhneExc(
		// getKundeIdFromPartner(partnerId), itemId, getSokoDate()) ;
		// if(dto != null) {
		// setKundenartikelnummer(row, dto.getCKundeartikelnummer()) ;
		// }
		// } catch(RemoteException e) {
		// setKundenartikelnummer(row, "Nicht ermittelbar") ;
		// }
		// }

		public void buildFeatureData(int row, Object o[]) {
			if (hasFeatureEinheitCnr()) {
				setFlrDataObject(row, new ArtikellisteFLRDataDto((String) o[22]));
			}

			if (hasFeatureKundenartikelCnr()) {
				getFlrDataObject(row).setKundenartikelCnr((String) o[27]);
			}
			// if(hasFeatureKundenartikelCnr()) {
			// buildKundenartikelnummer(row, (Integer) o[0], partnerId) ;
			// }
		}

		public Integer getKundeId() {
			try {
				return hasFeatureKundenartikelCnr() ? getKundeIdFromPartner(partnerId) : null;
			} catch (RemoteException e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
			}
		}
	}

	private Feature getFeature() {
		if (cachedFeature == null) {
			cachedFeature = new Feature();
		}
		return cachedFeature;
	}

	@Override
	public QueryResult setQuery(QueryParameters queryParameters) throws EJBExceptionLP {
		cachedWhereClause = null;
		return super.setQuery(queryParameters);
	}

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;

		Iterator resultListIterator = null;
		List resultList = null;

		try {
			int colCount = getTableInfo().getColumnClasses().length + 1;
			int pageSize = getLimit();
			int startIndex = getStartIndex(rowIndex, pageSize);
			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			session = setFilter(session);

			String queryString = "SELECT artikelliste.i_id FROM FLRArtikelliste AS artikelliste " + buildJoinClause()
			// +
			// " LEFT OUTER JOIN artikelliste.artikellagerset AS alager "
			// + " LEFT OUTER JOIN artikelliste.flrgeometrie AS geo "
			// +
			// " LEFT OUTER JOIN artikelliste.artikellieferantset AS artikellieferantset "
			// +
			// " LEFT OUTER JOIN artikelliste.stuecklisten AS stuecklisten "
			// + " LEFT OUTER JOIN artikelliste.artikelsprset AS aspr "
			// + " LEFT OUTER JOIN artikelliste.flrartikelgruppe AS ag "
			// + " LEFT OUTER JOIN artikelliste.flrartikelklasse AS ak "
			// + " LEFT OUTER JOIN artikelliste.flrvorzug AS vz "
			// + " LEFT OUTER JOIN artikelliste.kundesokoset AS soko "
					+ this.buildWhereClause() + this.buildGroupByClause() + this.buildOrderByClause();

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
				queryString = this.getFromClause() + this.buildWhereClause() + in + this.buildGroupByClause()
						+ this.buildOrderByClause();
				query = session.createQuery(queryString);
				resultList = query.list();

				Session sessionVkPreisBasis = factory.openSession();
				String rabattsatzFixpreis = "SELECT pl.artikel_i_id, pl.n_artikelstandardrabattsatz ,pl.n_artikelfixpreis FROM FLRVkpfartikelpreis AS pl  WHERE pl.vkpfartikelpreisliste_i_id="
						+ vkPreisliste + " AND " + inArtikel_i_id + "  ORDER BY t_preisgueltigab DESC ";
				Query queryRabattsatzFixpreis = sessionVkPreisBasis.createQuery(rabattsatzFixpreis);

				List resultListRabattsatzFixpreis = queryRabattsatzFixpreis.list();

				Iterator resultListIteratorRabattsatzFixpreis = resultListRabattsatzFixpreis.iterator();
				while (resultListIteratorRabattsatzFixpreis.hasNext()) {
					Object[] o = (Object[]) resultListIteratorRabattsatzFixpreis.next();
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
							+ inArtikel_i_id + " AND t_verkaufspreisbasisgueltigab <='"
							+ Helper.formatDateWithSlashes(new java.sql.Date(System.currentTimeMillis())) + "'"
							+ "  ORDER BY t_verkaufspreisbasisgueltigab DESC ";
				}
				Query queryPreisbasis = sessionVkPreisBasis.createQuery(preisbasis);

				List resultListPreisbasis = queryPreisbasis.list();

				Iterator resultListIteratorPreisbasis = resultListPreisbasis.iterator();
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
						+ inKommentar + " AND ko.locale='" + theClientDto.getLocUiAsString()
						+ "' AND ko.artikelkommentar.flrartikelkommentarart.b_tooltip=1 ";

				Query queryKommentare = sessionVkPreisBasis.createQuery(kommentare);

				List resultListKommentare = queryKommentare.list();

				Iterator resultListIteratorKommentare = resultListKommentare.iterator();
				while (resultListIteratorKommentare.hasNext()) {
					Object[] o = (Object[]) resultListIteratorKommentare.next();

					if (o[2] != null) {
						String kommentar = "<b>" + (String) o[2] + ":</b>\n" + (String) o[1];

						String kommentarVorhanden = "";
						if (hmKommentarTooltip.containsKey(o[0])) {
							kommentarVorhanden = hmKommentarTooltip.get(o[0]) + "<br><br>" + kommentar;
						} else {
							kommentarVorhanden = kommentar;
						}

						hmKommentarTooltip.put((Integer) o[0], kommentarVorhanden);
					}

				}
				sessionVkPreisBasis.close();

			}

			rows = new Object[resultList.size()][colCount];

			resultListIterator = resultList.iterator();

			int row = 0;

			String[] tooltipData = new String[resultList.size()];
			getFeature().setFlrRowCount(rows.length);

			while (resultListIterator.hasNext()) {
				Object o[] = (Object[]) resultListIterator.next();

				Object[] rowToAddCandidate = new Object[colCount];

				rowToAddCandidate[getTableColumnInformation().getViewIndex("i_id")] = o[0];
				rowToAddCandidate[getTableColumnInformation().getViewIndex("artikel.artikelnummerlang")] = o[1];
				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.stuecklistenart")] = o[13] != null
						? ((String) o[13]).trim()
						: o[13];

				if (bKurzbezeichnungAnzeigen) {
					prepareKurzbezeichnung(o, rowToAddCandidate);
				}

				rowToAddCandidate[getTableColumnInformation().getViewIndex("bes.artikelbezeichnung")] = o[2];

				if (bMaterialAnzeigen) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.material")] = o[31];
				}

				if (bAbmessungenStattZusatzbezeichnung || bArtikelgruppeStattZusatzbezeichnung) {
					if (bAbmessungenStattZusatzbezeichnung) {
						prepareAbmessungen(o, rowToAddCandidate);
					} else {
						prepareArtikelGruppeInAbmessung(o, rowToAddCandidate);
					}
				} else {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("artikel.zusatzbez")] = o[6];
				}

				if (bReferenznummerAnzeigen) {
					prepareReferenznummer(o, rowToAddCandidate);
				}
				if (bZusatzbezeichnung2Anzeigen) {
					prepareZusatzbezeichnung2(o, rowToAddCandidate);
				}
				if (bKurzbezeichnungAnzeigen) {
					prepareKurzbezeichnung(o, rowToAddCandidate);
				}

				if (bArtikelgruppeAnzeigen) {
					prepareArtikelGruppe(o, rowToAddCandidate);
				}
				if (bArtikelklasseAnzeigen) {
					prepareArtikelKlasse(o, rowToAddCandidate);
				}

				// Verfuegbarkeit
				if (bVerfuegbarkeitAnzeigen) {

					BigDecimal lagerstand = (BigDecimal) o[3];
					if (lagerstand == null) {
						lagerstand = BigDecimal.ZERO;
					}

					BigDecimal fehlmenge = (BigDecimal) o[27];
					if (fehlmenge == null) {
						fehlmenge = BigDecimal.ZERO;
					}

					BigDecimal reservierungen = (BigDecimal) o[28];
					if (reservierungen == null) {
						reservierungen = BigDecimal.ZERO;
					}

					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.verfuegbar")] = lagerstand
							.subtract(fehlmenge.add(reservierungen));

				}

				if (bArtikelklasseAnzeigen) {
					prepareArtikelKlasse(o, rowToAddCandidate);
				}

				if (bVkPreisStattGestpreis == 1 || bVkPreisStattGestpreis == 3) {
					prepareVkPreis(hmRabattsatzFixpreis, hmPreisbasis, o, rowToAddCandidate);
				}

				if (bVkPreisStattGestpreis == 2 || bVkPreisStattGestpreis == 3) {
					ArtikellieferantDto alDto = getArtikelFac().getArtikelEinkaufspreisDesBevorzugtenLieferanten(
							(Integer) o[0], BigDecimal.ONE, theClientDto.getSMandantenwaehrung(), theClientDto);
					if (alDto != null) {
						rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.lief1preis")] = alDto
								.getNNettopreis();
					}
				}

				if (o[4] != null && Helper.short2boolean((Short) o[5])) {

					BigDecimal bdLagerstandZusaetzlichesLager = BigDecimal.ZERO;
					BigDecimal lagerstand = (BigDecimal) o[3];

					if (lagerIIdZusaetzlichesLager != -1) {
						if (o[30] != null) {
							bdLagerstandZusaetzlichesLager = ((BigDecimal) o[30]);
							rowToAddCandidate[getTableColumnInformation()
									.getViewIndex("zus_lager")] = bdLagerstandZusaetzlichesLager;
						}

					}

					if (lagerstand != null) {

						/// SP8041
						if (lagerDtoZusaetzlichesLager != null
								&& Helper.short2boolean(lagerDtoZusaetzlichesLager.getBKonsignationslager())) {
							rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.lagerstand")] = lagerstand;
						} else {
							rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.lagerstand")] = lagerstand
									.subtract(bdLagerstandZusaetzlichesLager);
						}

					}
				} else {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.lagerstand")] = new BigDecimal(0);
					if (lagerIIdZusaetzlichesLager != -1) {
						rowToAddCandidate[getTableColumnInformation().getViewIndex("zus_lager")] = new BigDecimal(0);
					}
				}

				if (bLagerplaetzeAnzeigen) {
					prepareLagerplaetze((Integer) o[0], rowToAddCandidate);
				}

				if (bLagerstandDesAnderenMandantenAnzeigen == true) {

					rowToAddCandidate[getTableColumnInformation()
							.getViewIndex("artikel.lagerstand.anderermandant")] = (BigDecimal) o[26];
				} else {

					if (bDarfPreiseSehen) {
						// Gestehungspreis holen
						BigDecimal gestehungspreis = (BigDecimal) o[4];
						if (gestehungspreis != null
								&& rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.lagerstand")] != null
								&& ((BigDecimal) rowToAddCandidate[getTableColumnInformation()
										.getViewIndex("lp.lagerstand")]).doubleValue() > 0) {
							gestehungspreis = gestehungspreis.divide(
									new BigDecimal(((BigDecimal) rowToAddCandidate[getTableColumnInformation()
											.getViewIndex("lp.lagerstand")]).doubleValue()),
									4, BigDecimal.ROUND_HALF_EVEN);
						} else {
							// Projekt 10870: WH: Wenn kein Gestpreis
							// zustandekommt,
							// dann Gestpreis des Hauptlagers anzeigen
							if (Helper.short2boolean((Short) o[5]) && o[8] != null) {
								gestehungspreis = (BigDecimal) o[8];
							} else {
								gestehungspreis = new BigDecimal(0);
							}
						}
						if (bVkPreisStattGestpreis == 0 || bVkPreisStattGestpreis == 3) {
							rowToAddCandidate[getTableColumnInformation()
									.getViewIndex("lp.gestpreis")] = gestehungspreis;
						}
					}
				}

				// PJ20048
				if (bLief1Infos && bDarfPreiseSehen) {
					ArtikellieferantDto alDto = getArtikelFac().getArtikelEinkaufspreisDesBevorzugtenLieferanten(
							(Integer) o[0], BigDecimal.ONE, theClientDto.getSMandantenwaehrung(), theClientDto);
					if (alDto != null) {
						rowToAddCandidate[getTableColumnInformation().getViewIndex("artikel.auswahl.lieferant")] = alDto
								.getLieferantDto().getPartnerDto().getCKbez();
						rowToAddCandidate[getTableColumnInformation()
								.getViewIndex("artikel.auswahl.lief1preis")] = alDto.getNNettopreis();
						rowToAddCandidate[getTableColumnInformation().getViewIndex("artikel.auswahl.wbz")] = alDto
								.getIWiederbeschaffungszeit();
					}
				}

				Long lAnzahlReklamationen = (Long) o[15];
				Boolean hatOffeneReklamationen = (lAnzahlReklamationen != null) && lAnzahlReklamationen.intValue() > 0;

				Long lAnzahlProjekteMitSperrstatus = (Long) o[32];
				Boolean hatProjekteMitSperren = (lAnzahlProjekteMitSperrstatus != null)
						&& lAnzahlProjekteMitSperrstatus.intValue() > 0;

				FLRArtikelsperren as = (FLRArtikelsperren) o[7];

				if (as != null || hatOffeneReklamationen || hatProjekteMitSperren) {
					String gesperrt = null;

					if (as != null) {
						gesperrt = as.getFlrsperren().getC_bez();
					}

					if (hatOffeneReklamationen) {
						rowToAddCandidate[getTableColumnInformation().getViewIndex("Icon")] = getStatusMitUebersetzung(
								gesperrt, new java.sql.Timestamp(System.currentTimeMillis()), "R");
					} else if (hatProjekteMitSperren) {
						rowToAddCandidate[getTableColumnInformation().getViewIndex("Icon")] = getStatusMitUebersetzung(
								gesperrt, new java.sql.Timestamp(System.currentTimeMillis()), "O");
					} else {
						rowToAddCandidate[getTableColumnInformation().getViewIndex("Icon")] = gesperrt;
					}

				}
				// PJ18548
				rowToAddCandidate[getTableColumnInformation().getViewIndex("artikel.vorzugsteil")] = o[23];

				boolean bVersteckt = Helper.short2boolean((Short) o[29]);

				if (bArtikelfreigabe) {
					if (o[33] != null) {
						rowToAddCandidate[getTableColumnInformation()
								.getViewIndex("IconFreigabe")] = FertigungFac.STATUS_ERLEDIGT;
					}
				}

				if (bDualUse == true) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("artikel.meldepflichtig")] = Helper
							.short2Boolean((Short) o[34]);
					rowToAddCandidate[getTableColumnInformation()
							.getViewIndex("artikel.bewilligungspflichtig")] = Helper.short2Boolean((Short) o[35]);
				}

				if (o[36] != null) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.einheit")] = ((String) o[36]).trim();
				}

				if (bHerstellernummerAnzeigen) {

					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.herstellernummer")] = o[24];
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.herstellerbezeichnung")] = o[25];

				}

				if (!Helper.short2boolean((Short) o[18])) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("Color")] = Color.BLUE;

					if (bVersteckt == true) {
						rowToAddCandidate[getTableColumnInformation().getViewIndex("Color")] = new Color(0, 150, 255);
					}

				} else {

					if (o[37] != null && ((Long) o[37]) > 0) {
						// PJ21975 FORECAST
						rowToAddCandidate[getTableColumnInformation().getViewIndex("Color")] = new Color(255, 161, 50);
					} else {
						if (bVersteckt) {
							rowToAddCandidate[getTableColumnInformation().getViewIndex("Color")] = Color.LIGHT_GRAY;
						}
					}

				}

				rows[row] = rowToAddCandidate;

				// PJ18205
				String tooltip = (String) hmKommentarTooltip.get(o[0]);
				if (tooltip != null) {
					String text = tooltip;
					text = text.replaceAll("\n", "<br>");
					text = "<html>" + text + "</html>";
					tooltipData[row] = text;
				}

				getFeature().buildFeatureData(row, o);
				row++;
			}

			if (getFeature().hasFeatureEinheitCnr()) {
				ArtikellisteQueryResult artikellisteResult = new ArtikellisteQueryResult(rows, getRowCount(),
						startIndex, endIndex, 0);
				artikellisteResult.setFlrData(getFeature().getFlrData());
				result = artikellisteResult;
			} else {
				result = new QueryResult(rows, this.getRowCount(), startIndex, endIndex, 0, tooltipData);
			}

			// result = new QueryResult(rows, getRowCount(), startIndex,
			// endIndex,
			// 0, tooltipData);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			closeSession(session);
		}
		return result;
	}

	protected void prepareArtikelGruppeInAbmessung(Object[] source, Object[] target) {
		target[getTableColumnInformation().getViewIndex("lp.artikelgruppeInAbmessung")] = source[14];
	}

	protected void prepareArtikelGruppe(Object[] source, Object[] target) {
		target[getTableColumnInformation().getViewIndex("lp.artikelgruppe")] = source[14];
	}

	protected void prepareArtikelKlasse(Object[] source, Object[] target) {
		target[getTableColumnInformation().getViewIndex("lp.artikelklasse")] = source[17];
	}

	protected void prepareKurzbezeichnung(Object[] source, Object[] target) {
		target[getTableColumnInformation().getViewIndex("artikel.kurzbez")] = source[16];
	}

	protected void prepareReferenznummer(Object[] source, Object[] target) {
		target[getTableColumnInformation().getViewIndex("lp.referenznummer")] = source[21];
	}

	protected void prepareZusatzbezeichnung2(Object[] source, Object[] target) {
		target[getTableColumnInformation().getViewIndex("artikel.zusatzbez2")] = source[20];
	}

	protected void prepareLagerplaetze(Integer artikelIId, Object[] target) throws RemoteException {
		// PJ 17762 Lagerplaetze manuell anzeigen
		target[getTableColumnInformation().getViewIndex("lp.lagerplatz")] = getLagerFac()
				.getLagerplaezteEinesArtikels(artikelIId, null);
	}

	protected void prepareVkPreis(HashMap hmRabattsatzFixpreis, HashMap hmPreisbasis, Object[] source,
			Object[] target) {
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
				BigDecimal bdRabattsumme = vkPreisbasis.multiply(vkRabattsatz.movePointLeft(2));
				vkPreis = vkPreisbasis.subtract(bdRabattsumme);
			} else {
				vkPreis = vkPreisbasis;
			}
		}
		target[getTableColumnInformation().getViewIndex("lp.vkpreis")] = vkPreis;
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
		target[getTableColumnInformation().getViewIndex("lp.abmessungen")] = StringUtils.stripEnd(abmessungen, "x");

		// if (abmessungen.endsWith("x")) {
		// abmessungen = abmessungen.substring(0, abmessungen.length() - 1);
		// }

		// target[4] = abmessungen;
	}

	protected long getRowCountFromDataBase() {
		String queryString = "SELECT  COUNT(distinct artikelliste.i_id) FROM com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste AS artikelliste "
				+ buildJoinClause()
				// + " LEFT OUTER JOIN artikelliste.artikelsprset AS aspr "
				// +
				// " LEFT OUTER JOIN artikelliste.artikellieferantset AS artikellieferantset "
				// + " LEFT OUTER JOIN artikelliste.flrgeometrie AS geo "
				// + " LEFT OUTER JOIN artikelliste.flrartikelgruppe AS ag "
				// + " LEFT OUTER JOIN artikelliste.flrartikelklasse AS ak "
				// +
				// " LEFT OUTER JOIN artikelliste.stuecklisten AS stuecklisten "
				// + " LEFT OUTER JOIN artikelliste.flrvorzug AS vz "
				// + " LEFT OUTER JOIN artikelliste.kundesokoset AS soko "
				+ buildWhereClause();
		return getRowCountFromDataBaseByQuery(queryString);
	}

	protected String buildJoinClause() {
		String joinClause = " LEFT OUTER JOIN artikelliste.artikellagerset AS alager "
				+ " LEFT OUTER JOIN artikelliste.flrgeometrie AS geo "
				+ " LEFT OUTER JOIN artikelliste.artikellieferantset AS artikellieferantset "
				+ " LEFT OUTER JOIN artikelliste.artikelsprset AS aspr "
				+ " LEFT OUTER JOIN artikelliste.flrartikelgruppe AS ag "
				+ " LEFT OUTER JOIN artikelliste.flrartikelklasse AS ak "
				+ " LEFT OUTER JOIN artikelliste.flrvorzug AS vz " + " LEFT OUTER JOIN artikelliste.flrmaterial AS mat "
				+ " LEFT OUTER JOIN artikelliste.artikellagerplatzset AS lagerplaetze "
				+ " LEFT OUTER JOIN artikelliste.ersatztypenset AS et ";

		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
				theClientDto)) {
			joinClause += " LEFT OUTER JOIN artikelliste.stuecklisten_mandantenunabhaengig AS stuecklisten ";
		} else {
			joinClause += " LEFT OUTER JOIN artikelliste.stuecklisten AS stuecklisten ";
		}

		if (getFeature().hasFeatureKundenartikelCnr() || getFeature().hasFeatureSoko()) {
			joinClause += " LEFT OUTER JOIN artikelliste.kundesokoset2 AS soko ";
		}

		return joinClause;
	}

	/**
	 * builds the where clause of the HQL (Hibernate Query Language) statement using
	 * the current query.
	 * 
	 * @return the HQL where clause.
	 */

	private String buildWhereClause() {
		if (cachedWhereClause != null)
			return cachedWhereClause;

		StringBuffer where = new StringBuffer("");

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

						// PJ21406
						if (artikelnummerOhne != null) {
							filterKriterien[i].kritName = "REPLACE(" + filterKriterien[i].kritName + ",'"
									+ artikelnummerOhne + "','')";
							filterKriterien[i].value = filterKriterien[i].value.replace(artikelnummerOhne, "");
						}

						if (bArtikelsucheInclReferenznummer) {
							where.append(" (lower(artikelliste.c_nr)");
							where.append(" " + filterKriterien[i].operator);
							where.append(" " + filterKriterien[i].value.toLowerCase());
							where.append(" OR lower(artikelliste.c_referenznr)");
							where.append(" " + filterKriterien[i].operator);
							where.append(" " + filterKriterien[i].value.toLowerCase() + ")");
							continue;
						}

					}
					if (filterKriterien[i].kritName
							.equals("artikelliste." + ArtikelFac.FLR_ARTIKELLISTE_STUECKLISTE_PARTNER_ID)) {
						where.append("(stuecklisten.partner_i_id IS NULL OR stuecklisten.partner_i_id="
								+ filterKriterien[i].value + ")");
						bTextsucheInklusiveArtikelnummer = true;
						continue;
					}
					if (filterKriterien[i].kritName.equals(ArtikelFac.FLR_ARTIKELLISTE_SHOPGRUPPE_ID)) {
						buildFilterShopGruppe(filterKriterien[i], where);
						continue;
					}
					if (filterKriterien[i].kritName.equals("NUR_ARTIKEL_MIT_ERSATZTYPEN")) {
						where.append(" et.i_id IS NOT NULL ");
						continue;
					}
					if (filterKriterien[i].kritName.equals("c_volltext")) {
						filterKriterien[i].kritName = "aspr.c_bez";
					}
					if (filterKriterien[i].kritName.equals("aspr.c_bez")) {

						filterKriterien[i].kritName = "aspr.c_bez";

						bvolltext = true;

					} else {
						bvolltext = false;
					}
					if (bvolltext && !filterKriterien[i].kritName
							.equals(ArtikelFac.FLR_ARTIKELLIEFERANT_C_ARTIKELNRLIEFERANT)) {

						String suchstring = "lower(coalesce(aspr.c_bez,'')||' '||coalesce(aspr.c_kbez,'')||' '||coalesce(aspr.c_zbez,'')||' '||coalesce(aspr.c_zbez2,''))";
						if (bAbmessungenStattZusatzbezeichnung) {
							suchstring += "||' '||lower(coalesce(geo.c_breitetext,'')||coalesce(cast(geo.f_breite as string),'')||case  WHEN geo.f_hoehe IS NULL THEN '' ELSE 'x' END||coalesce(cast(geo.f_hoehe as string),'')||case  WHEN geo.f_tiefe IS NULL THEN '' ELSE 'x' END||coalesce(cast(geo.f_tiefe as string),''))";
						}

						if (bTextsucheInklusiveArtikelnummer) {
							suchstring += "||' '||lower(artikelliste.c_nr)";
						}

						if (bTextsucheInklusiveHersteller) {
							suchstring += "||' '||lower(coalesce(artikelliste.c_artikelnrhersteller,''))||' '||lower(coalesce(artikelliste.c_artikelbezhersteller,''))";
						}
						if (bTextsucheInklusiveIndexRevision) {
							suchstring += "||' '||lower(coalesce(artikelliste.c_index,''))||' '||lower(coalesce(artikelliste.c_revision,''))";
						}

						String[] teile = filterKriterien[i].value.trim().split(" ");

						if (getSiSortKrit()) {
							List<String> siList = new ArrayList<String>();
							List<String> nonSiList = new ArrayList<String>();

							for (String teil : teile) {
								if (getSiValue(teil) != null) {
									siList.add(teil);
								} else {
									nonSiList.add(teil);
								}
							}

							int counter = 0;
							for (String nonSiEntry : nonSiList) {

								if (nonSiEntry.startsWith("-")) {
									where.append(" NOT ");
									nonSiEntry = nonSiEntry.substring(1);
								}

								where.append("(");
								where.append(suchstring + " like '%" + nonSiEntry.toLowerCase() + "%'");
								where.append(")");

								if (counter < nonSiList.size() - 1)
									where.append(" AND ");

								counter++;
							}

							if (siList.size() > 0) {
								if (nonSiList.size() > 0)
									where.append(" AND coalesce(aspr.c_siwert,'')");
								else
									where.append(" coalesce(aspr.c_siwert,'')");

								where.append(" like ");
								where.append("(");
								where.append("'" + getSiValue(siList.get(0)) + "'");
								where.append(")");
							}
						}

						else {

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

						}

					} else if (filterKriterien[i].kritName
							.equals(ArtikelFac.FLR_ARTIKELLIEFERANT_C_ARTIKELNRLIEFERANT)) {

						String suchstring = "lower(coalesce(artikellieferantset.c_artikelnrlieferant,'')||' '||coalesce(artikelliste.c_artikelnrhersteller,'')||' '||coalesce(artikelliste.c_artikelbezhersteller,'')||' '||coalesce(artikellieferantset."
								+ ArtikelFac.FLR_ARTIKELLIEFERANT_C_BEZBEILIEFERANT + ",''))";

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

						where.append(") ");

						/*
						 * where.append(" (lower(artikellieferantset." + filterKriterien[i].kritName +
						 * ")"); where.append(" " + filterKriterien[i].operator); where.append(" " +
						 * filterKriterien[i].value.toLowerCase());
						 * 
						 * where.append(" OR lower(artikelliste.c_artikelnrhersteller" + ")");
						 * where.append(" " + filterKriterien[i].operator); where.append(" " +
						 * filterKriterien[i].value.toLowerCase());
						 * 
						 * where.append(" OR lower(artikelliste.c_artikelbezhersteller" + ")");
						 * where.append(" " + filterKriterien[i].operator); where.append(" " +
						 * filterKriterien[i].value.toLowerCase());
						 * 
						 * where.append(" OR lower(artikellieferantset." +
						 * ArtikelFac.FLR_ARTIKELLIEFERANT_C_BEZBEILIEFERANT + ")"); where.append(" " +
						 * filterKriterien[i].operator); where.append(" " +
						 * filterKriterien[i].value.toLowerCase() + ")");
						 */
					}

					else if (filterKriterien[i].kritName.equals("akag")) {
						where.append(" (lower(artikelliste.flrartikelgruppe.c_nr)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" " + filterKriterien[i].value.toLowerCase());
						where.append(" OR lower(artikelliste.flrartikelklasse.c_nr)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" " + filterKriterien[i].value.toLowerCase() + ")");

					} else if (filterKriterien[i].kritName.equals(ArtikelFac.FLR_ARTIKELLISTE_FLRHERSTELLER)) {
						where.append(" (lower(artikelliste.flrhersteller.flrpartner.c_name1nachnamefirmazeile1)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" " + filterKriterien[i].value.toLowerCase());
						where.append(" OR lower(artikelliste.flrhersteller.flrpartner.c_name2vornamefirmazeile2)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" " + filterKriterien[i].value.toLowerCase() + ")");
					} else if (filterKriterien[i].kritName.equals("ag.i_id")) {
						where.append(" ag.i_id ");
						where.append(filterKriterien[i].operator);
						String value = filterKriterien[i].value.toLowerCase().trim();
						if (FilterKriterium.OPERATOR_IN.equals(filterKriterien[i].operator)) {
							if (!(value == null || value.length() == 0)) {
								if (value.startsWith("(") && value.endsWith(")")) {
									where.append(" " + value);
								} else {
									where.append(" (" + value + ")");
								}
							}
						} else {
							where.append(value);
						}
					}

					else {
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" LOWER(" + filterKriterien[i].kritName + ")");
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

			// if(getFeature().hasFeatureKundenartikelCnr()) {
			// where.append(" AND soko.kunde_i_id = " +
			// getFeature().getKundeId()) ;
			// }

			if (filterAdded) {
				where.insert(0, " WHERE ");
			}
		}

		cachedWhereClause = where.toString();
		return cachedWhereClause;
		// return where.toString();
	}

	private void buildFilterShopGruppe(FilterKriterium filter, StringBuffer where) {
		if ("[0]".equals(filter.value)) {
			where.append(" (artikelliste.shopgruppe_i_id IS NOT NULL OR soko.kunde_i_id = " + getFeature().getKundeId()
					+ ")");
			bTextsucheInklusiveArtikelnummer = true;
			return;
		}
		if ("[1]".equals(filter.value)) {
			where.append(
					" (artikelliste.shopgruppe_i_id IS NULL AND soko.kunde_i_id = " + getFeature().getKundeId() + ")");
			bTextsucheInklusiveArtikelnummer = true;
			return;
		}
		if (FilterKriterium.OPERATOR_IN.equals(filter.operator)) {
			where.append(" artikelliste.shopgruppe_i_id IN " + filter.value);
			bTextsucheInklusiveArtikelnummer = true;
			return;
		}

		where.append(" artikelliste.shopgruppe_i_id = " + filter.value);
	}

	private String buildGroupByClause() {
		return " GROUP BY artikelliste.i_id,artikelliste.c_nr, aspr.c_bez, artikelliste.b_lagerbewirtschaftet, aspr.c_zbez, geo.c_breitetext"
				+ ", geo.f_breite , geo.f_hoehe , geo.f_tiefe, stuecklisten.stuecklisteart_c_nr, ag.c_nr, aspr.c_kbez,ak.c_nr,aspr.c_siwert"
				+ ", aspr.c_zbez2, artikelliste.c_referenznr, artikelliste.einheit_c_nr, vz.c_nr, artikelliste.c_artikelnrhersteller"
				+ " , artikelliste.c_artikelbezhersteller, artikelliste.b_versteckt, mat.c_nr, artikelliste.t_freigabe,artikelliste.b_meldepflichtig,artikelliste.b_bewilligungspflichtig "
				+ (getFeature().hasFeatureKundenartikelCnr() ? ", soko.c_kundeartikelnummer" : "");
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
		return "SELECT artikelliste.i_id, artikelliste.c_nr, aspr.c_bez, (SELECT sum(artikellager.n_lagerstand) FROM FLRArtikellager AS artikellager WHERE artikellager.compId.artikel_i_id=artikelliste.i_id AND artikellager.flrlager.mandant_c_nr='"
				+ theClientDto.getMandant()
				+ "'  AND artikellager.flrlager.b_konsignationslager=0 AND artikellager.flrlager.lagerart_c_nr NOT IN('"
				+ LagerFac.LAGERART_WERTGUTSCHRIFT + "')),"
				+ " (SELECT sum(artikellager.n_lagerstand*artikellager.n_gestehungspreis) FROM FLRArtikellager AS artikellager WHERE artikellager.compId.artikel_i_id=artikelliste.i_id AND artikellager.flrlager.b_konsignationslager=0 AND artikellager.flrlager.lagerart_c_nr NOT IN('"
				+ LagerFac.LAGERART_WERTGUTSCHRIFT
				+ "')), artikelliste.b_lagerbewirtschaftet, aspr.c_zbez, (SELECT s FROM FLRArtikelsperren as s WHERE s.artikel_i_id=artikelliste.i_id AND s.i_sort=1) as sperren,(SELECT al.n_gestehungspreis FROM FLRArtikellager as al WHERE al.compId.lager_i_id="
				+ getLagerFac().getHauptlagerDesMandanten(theClientDto).getIId()
				+ " AND al.compId.artikel_i_id=artikelliste.i_id) as gestpreishauptlager, geo.c_breitetext, geo.f_breite , geo.f_hoehe , geo.f_tiefe, stuecklisten.stuecklisteart_c_nr, ag.c_nr,(SELECT COUNT(*) FROM FLRReklamation r WHERE r.flrartikel.i_id=artikelliste.i_id AND r.status_c_nr='"
				+ LocaleFac.STATUS_ANGELEGT
				+ "'), aspr.c_kbez, ak.c_nr,artikelliste.b_lagerbewirtschaftet,lower(coalesce(geo.c_breitetext,'')||coalesce(cast(geo.f_breite as string),'')||case  WHEN geo.f_hoehe IS NULL THEN '' ELSE 'x' END||coalesce(cast(geo.f_hoehe as string),'')||case  WHEN geo.f_tiefe IS NULL THEN '' ELSE 'x' END||coalesce(cast(geo.f_tiefe as string),'')), aspr.c_zbez2, "
				+ " artikelliste.c_referenznr, artikelliste.einheit_c_nr, vz.c_nr, artikelliste.c_artikelnrhersteller, artikelliste.c_artikelbezhersteller, (SELECT sum(artikellager.n_lagerstand) FROM FLRArtikellager AS artikellager WHERE artikellager.compId.artikel_i_id=artikelliste.i_id  AND artikellager.flrlager.b_konsignationslager=0 AND artikellager.flrlager.mandant_c_nr<>'"
				+ theClientDto.getMandant() + "' AND artikellager.flrlager.lagerart_c_nr NOT IN('"
				+ LagerFac.LAGERART_WERTGUTSCHRIFT + "'))"
				+ (getFeature().hasFeatureKundenartikelCnr() ? ", soko.c_kundeartikelnummer" : "")
				+ " , (SELECT sum(fm.n_menge) FROM FLRFehlmenge AS fm WHERE fm.artikel_i_id=artikelliste.i_id), (SELECT sum(ar.n_menge) FROM FLRArtikelreservierung AS ar WHERE ar.flrartikel.i_id=artikelliste.i_id), artikelliste.b_versteckt, (SELECT artikellager.n_lagerstand FROM FLRArtikellager AS artikellager WHERE artikellager.compId.artikel_i_id=artikelliste.i_id AND artikellager.flrlager.i_id="
				+ lagerIIdZusaetzlichesLager
				+ ") , mat.c_nr,(SELECT COUNT(*) FROM FLRProjekt r WHERE r.flrartikel.i_id=artikelliste.i_id AND r.flrprojektstatus.status_c_nr <>'"
				+ LocaleFac.STATUS_STORNIERT
				+ "' AND r.flrprojektstatus.b_gesperrt=1 ), artikelliste.t_freigabe, artikelliste.b_meldepflichtig, artikelliste.b_bewilligungspflichtig, artikelliste.einheit_c_nr,(SELECT count(*) FROM FLRForecastposition AS fp WHERE fp.flrartikel.i_id=artikelliste.i_id AND fp.flrforecastauftrag.status_c_nr='"
				+ LocaleFac.STATUS_FREIGEGEBEN + "') FROM FLRArtikelliste AS artikelliste" + buildJoinClause();
		// +
		// " LEFT OUTER JOIN artikelliste.artikellieferantset AS artikellieferantset "
		// + " LEFT OUTER JOIN artikelliste.stuecklisten AS stuecklisten "
		// + " LEFT OUTER JOIN artikelliste.artikellagerset AS alager "
		// + " LEFT OUTER JOIN artikelliste.flrgeometrie AS geo "
		// + " LEFT OUTER JOIN artikelliste.artikelsprset AS aspr "
		// + " LEFT OUTER JOIN artikelliste.flrartikelgruppe AS ag "
		// + " LEFT OUTER JOIN artikelliste.flrartikelklasse AS ak "
		// + " LEFT OUTER JOIN artikelliste.flrvorzug AS vz "
		// + " LEFT OUTER JOIN artikelliste.kundesokoset AS soko ";
	}

	public Session setFilter(Session session) {
		session = super.setFilter(session);
		String sMandant = theClientDto.getMandant();
		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
				theClientDto)) {
			session.enableFilter("filterMandant").setParameter("paramMandant", getSystemFac().getHauptmandant());
		} else {
			session.enableFilter("filterMandant").setParameter("paramMandant", sMandant);
		}

		if (getFeature().hasFeatureKundenartikelCnr() || getFeature().hasFeatureSoko()) {
			session.enableFilter("filterKundeId").setParameter("paramKundeId", getFeature().getKundeId());
			// session.enableFilter("filterGueltig")
			// .setParameter("paramGueltig", "'" +
			// getFeature().getSokoDateAsString() + "'");

			session.enableFilter("filterGueltig").setParameter("paramGueltig",
					Helper.cutDate(getFeature().getSokoDate()));
		}

		return session;
	}

	// public QueryResult sort(SortierKriterium[] sortierKriterien,
	// Object selectedIdI) throws EJBExceptionLP {
	//
	// getQuery().setSortKrit(sortierKriterien);

	public QueryResult sort(SortierKriterium[] sortierKriterien, Object selectedIdI) throws EJBExceptionLP {

		getQuery().setSortKrit(sortierKriterien);

		QueryResult result = null;
		int rowNumber = 0;
		ScrollableResults scrollableResult = null;
		if (selectedIdI instanceof Integer) {
			if (((Integer) selectedIdI).intValue() >= 0) {
				if (getQuery().getIsApi()) {
					rowNumber = (Integer) selectedIdI;
				} else {
					SessionFactory factory = FLRSessionFactory.getFactory();
					Session session = null;

					try {
						session = factory.openSession();
						session = setFilter(session);

						String queryString = null;
						try {

							queryString = "SELECT artikelliste.i_id FROM FLRArtikelliste AS artikelliste "
									+ buildJoinClause()
									// +
									// " LEFT OUTER JOIN artikelliste.artikellagerset AS alager "
									// +
									// " LEFT OUTER JOIN artikelliste.flrgeometrie AS geo "
									// +
									// " LEFT OUTER JOIN artikelliste.stuecklisten AS stuecklisten "
									// +
									// " LEFT OUTER JOIN artikelliste.artikellieferantset AS artikellieferantset "
									// +
									// " LEFT OUTER JOIN artikelliste.artikelsprset AS aspr "
									// +
									// " LEFT OUTER JOIN artikelliste.flrartikelgruppe AS ag "
									// +
									// " LEFT OUTER JOIN artikelliste.flrartikelklasse AS ak "
									// +
									// " LEFT OUTER JOIN artikelliste.flrvorzug AS vz "
									+ buildWhereClause() + buildGroupByClause() + buildOrderByClause();
						} catch (Exception ex) {
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, ex);
						}

						Query query = session.createQuery(queryString);
						scrollableResult = query.scroll();

						if (scrollableResult != null) {
							scrollableResult.beforeFirst();
							while (scrollableResult.next()) {
								Integer id = (Integer) scrollableResult.getInteger(0);
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
		setTableColumnInformation(createColumnInformation(theClientDto.getMandant(), theClientDto.getLocUi()));

		TableColumnInformation c = getTableColumnInformation();
		info = new TableInfo(c.getClasses(), c.getHeaderNames(), c.getWidths(), c.getDbColumNames(),
				c.getHeaderToolTips());
		setTableInfo(info);
		return info;
	}

	private TableColumnInformation createColumnInformation(String mandant, Locale locUi) {
		TableColumnInformation columns = new TableColumnInformation();

		columns.add("i_id", Integer.class, "i_id", -1, "i_id");
		columns.add("artikel.artikelnummerlang", String.class,
				getTextRespectUISpr("artikel.artikelnummerlang", mandant, locUi), QueryParameters.FLR_BREITE_L,
				"artikelliste.c_nr");

		columns.add("lp.stuecklistenart", String.class, getTextRespectUISpr("artikel.stuecklistenart", mandant, locUi),
				QueryParameters.FLR_BREITE_S, "stuecklisten.stuecklisteart_c_nr",
				getTextRespectUISpr("artikel.stuecklistenart.tooltip", mandant, locUi));
		if (bKurzbezeichnungAnzeigen) {
			columns.add("artikel.kurzbez", String.class, getTextRespectUISpr("artikel.kurzbez", mandant, locUi),
					QueryParameters.FLR_BREITE_XM, "aspr.c_kbez");
		}

		columns.add("bes.artikelbezeichnung", String.class,
				getTextRespectUISpr("bes.artikelbezeichnung", mandant, locUi), QueryParameters.FLR_BREITE_XL,
				"aspr.c_bez");

		if (bMaterialAnzeigen) {
			columns.add("lp.material", String.class, getTextRespectUISpr("lp.material", mandant, locUi),
					QueryParameters.FLR_BREITE_XM, "mat.c_nr");
		}

		if (bAbmessungenStattZusatzbezeichnung || bArtikelgruppeStattZusatzbezeichnung) {
			/*
			 * Unsauber Definition. Mit den Parametern ist es m&ouml;glich beide Variante zu
			 * setzen. Es soll aber nur eine von beiden funktionieren. So programmiert, dass
			 * bei beiden immer die Abmessung gewinnt.
			 */
			if (bAbmessungenStattZusatzbezeichnung) {
				columns.add("lp.abmessungen", String.class, getTextRespectUISpr("lp.abmessungen", mandant, locUi),
						QueryParameters.FLR_BREITE_SHARE_WITH_REST, "aspr.c_zbez");
			} else {
				columns.add("lp.artikelgruppeInAbmessung", String.class,
						getTextRespectUISpr("lp.artikelgruppe", mandant, locUi),
						QueryParameters.FLR_BREITE_SHARE_WITH_REST, "ag.c_nr");
			}
		} else {
			columns.add("artikel.zusatzbez", String.class, getTextRespectUISpr("artikel.zusatzbez", mandant, locUi),
					QueryParameters.FLR_BREITE_SHARE_WITH_REST, "aspr.c_zbez");
		}

		if (bReferenznummerAnzeigen) {
			columns.add("lp.referenznummer", String.class, getTextRespectUISpr("lp.referenznummer", mandant, locUi),
					QueryParameters.FLR_BREITE_XM, "artikelliste.c_referenznr");
		}

		if (bZusatzbezeichnung2Anzeigen) {
			columns.add("artikel.zusatzbez2", String.class, getTextRespectUISpr("artikel.zusatzbez2", mandant, locUi),
					QueryParameters.FLR_BREITE_L, "aspr.c_zbez2");
		}

		if (bArtikelgruppeAnzeigen) {
			columns.add("lp.artikelgruppe", String.class, getTextRespectUISpr("lp.artikelgruppe", mandant, locUi),
					QueryParameters.FLR_BREITE_L, "ag.c_nr");
		}
		if (bArtikelklasseAnzeigen) {
			columns.add("lp.artikelklasse", String.class, getTextRespectUISpr("lp.artikelklasse", mandant, locUi),
					QueryParameters.FLR_BREITE_L, "ak.c_nr");
		}
		if (bLagerplaetzeAnzeigen) {
			columns.add("lp.lagerplatz", String.class, getTextRespectUISpr("lp.lagerplatz", mandant, locUi),
					QueryParameters.FLR_BREITE_SHARE_WITH_REST, Facade.NICHT_SORTIERBAR);
		}

		columns.add("lp.lagerstand", BigDecimal.class,
				getTextRespectUISpr(
						bLagerstandDesAnderenMandantenAnzeigen ? "artikel.lagerstand.eigenermandant" : "lp.lagerstand",
						mandant, locUi),
				QueryParameters.FLR_BREITE_M, Facade.NICHT_SORTIERBAR);

		columns.add("lp.einheit", String.class, getTextRespectUISpr("lp.einheit", mandant, locUi),
				QueryParameters.FLR_BREITE_XS, "artikelliste.einheit_c_nr");

		if (lagerIIdZusaetzlichesLager != -1 && lagerCNrZusaetzlichesLager != null) {
			columns.add("zus_lager", BigDecimal.class, lagerCNrZusaetzlichesLager, QueryParameters.FLR_BREITE_M,
					Facade.NICHT_SORTIERBAR);
		}

		if (bVerfuegbarkeitAnzeigen) {
			columns.add("lp.verfuegbar", BigDecimal.class, getTextRespectUISpr("lp.verfuegbar", mandant, locUi),
					QueryParameters.FLR_BREITE_M, Facade.NICHT_SORTIERBAR);
		}

		if (bLagerstandDesAnderenMandantenAnzeigen == true) {
			columns.add("artikel.lagerstand.anderermandant", BigDecimal.class,
					getTextRespectUISpr("artikel.lagerstand.anderermandant", mandant, locUi),
					QueryParameters.FLR_BREITE_M, Facade.NICHT_SORTIERBAR);
		} else {

			if (bVkPreisStattGestpreis == 0) {
				columns.add("lp.gestpreis", BigDecimal.class, getTextRespectUISpr("lp.gestpreis", mandant, locUi),
						QueryParameters.FLR_BREITE_M, Facade.NICHT_SORTIERBAR);
			} else if (bVkPreisStattGestpreis == 1) {
				columns.add("lp.vkpreis", BigDecimal.class, getTextRespectUISpr("lp.vkpreis", mandant, locUi),
						QueryParameters.FLR_BREITE_M, Facade.NICHT_SORTIERBAR);
			} else if (bVkPreisStattGestpreis == 2) {
				columns.add("lp.lief1preis", BigDecimal.class,
						getTextRespectUISpr("artikel.label.lief1Preis", mandant, locUi), QueryParameters.FLR_BREITE_M,
						Facade.NICHT_SORTIERBAR);
			} else if (bVkPreisStattGestpreis == 3) {
				columns.add("lp.gestpreis", BigDecimal.class, getTextRespectUISpr("lp.gestpreis", mandant, locUi),
						QueryParameters.FLR_BREITE_M, Facade.NICHT_SORTIERBAR);
				columns.add("lp.vkpreis", BigDecimal.class, getTextRespectUISpr("lp.vkpreis", mandant, locUi),
						QueryParameters.FLR_BREITE_M, Facade.NICHT_SORTIERBAR);
				columns.add("lp.lief1preis", BigDecimal.class,
						getTextRespectUISpr("artikel.label.lief1Preis", mandant, locUi), QueryParameters.FLR_BREITE_M,
						Facade.NICHT_SORTIERBAR);
			}

		}

		if (bLief1Infos) {
			columns.add("artikel.auswahl.lieferant", String.class,
					getTextRespectUISpr("artikel.auswahl.lieferant", mandant, locUi), QueryParameters.FLR_BREITE_M,
					Facade.NICHT_SORTIERBAR);
			columns.add("artikel.auswahl.lief1preis", BigDecimal.class,
					getTextRespectUISpr("artikel.auswahl.lief1preis", mandant, locUi), QueryParameters.FLR_BREITE_M,
					Facade.NICHT_SORTIERBAR);
			columns.add("artikel.auswahl.wbz", Integer.class,
					getTextRespectUISpr("artikel.auswahl.wbz", mandant, locUi), QueryParameters.FLR_BREITE_S,
					Facade.NICHT_SORTIERBAR);
		}

		columns.add("Icon", SperrenIcon.class, getTextRespectUISpr("artikel.sperre", mandant, locUi),
				QueryParameters.FLR_BREITE_XS, Facade.NICHT_SORTIERBAR,
				getTextRespectUISpr("artikel.sperre.tooltip", mandant, locUi));

		columns.add("artikel.vorzugsteil", String.class, getTextRespectUISpr("artikel.vorzugsteil", mandant, locUi),
				QueryParameters.FLR_BREITE_S, "vz.c_nr");

		if (bArtikelfreigabe == true) {
			columns.add("IconFreigabe", Icon.class, getTextRespectUISpr("ww.freigabe", mandant, locUi),
					QueryParameters.FLR_BREITE_S, "artikelliste.t_freigabe");
		}

		if (bDualUse == true) {
			columns.add("artikel.meldepflichtig", Boolean.class,
					getTextRespectUISpr("artikel.meldepflichtig", mandant, locUi), QueryParameters.FLR_BREITE_XXS,
					"artikelliste.b_meldepflichtig");

			columns.add("artikel.bewilligungspflichtig", Boolean.class,
					getTextRespectUISpr("artikel.bewilligungspflichtig", mandant, locUi),
					QueryParameters.FLR_BREITE_XXS, "artikelliste.b_bewilligungspflichtig");
		}

		if (bHerstellernummerAnzeigen) {
			columns.add("lp.herstellernummer", String.class, getTextRespectUISpr("lp.herstellernummer", mandant, locUi),
					QueryParameters.FLR_BREITE_XM, "artikelliste.c_artikelnrhersteller");
			columns.add("lp.herstellerbezeichnung", String.class,
					getTextRespectUISpr("lp.herstellerbezeichnung", mandant, locUi), QueryParameters.FLR_BREITE_XM,
					"artikelliste.c_artikelbezhersteller");
		}

		columns.add("Color", Color.class, "", 1, "");
		return columns;
	}

	private Integer getIntegerParameter(String category, String parameter) throws RemoteException {
		ParametermandantDto param = getParameterFac().getMandantparameter(theClientDto.getMandant(), category,
				parameter);
		return (Integer) param.getCWertAsObject();
	}

	private Boolean getBooleanParameter(String category, String parameter) throws RemoteException {
		ParametermandantDto param = getParameterFac().getMandantparameter(theClientDto.getMandant(), category,
				parameter);
		return (Boolean) param.getCWertAsObject();
	}

	private void setupParameters() {
		try {
			bAbmessungenStattZusatzbezeichnung = getBooleanParameter(ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ABMESSUNGEN_STATT_ZUSATZBEZ_IN_AUSWAHLLISTE);
			bTextsucheInklusiveArtikelnummer = getBooleanParameter(ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_TEXTSUCHE_INKLUSIVE_ARTIKELNUMMER);
			bTextsucheInklusiveIndexRevision = getBooleanParameter(ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_TEXTSUCHE_INKLUSIVE_INDEX_REVISION);
			bTextsucheInklusiveHersteller = getBooleanParameter(ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ARTIKELSUCHE_MIT_HERSTELLER);

			bArtikelgruppeStattZusatzbezeichnung = getBooleanParameter(ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ARTIKELGRUPPE_STATT_ZUSATZBEZ_IN_AUSWAHLLISTE);

			bArtikelsucheInclReferenznummer = getBooleanParameter(ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ARTIKELSUCHE_MIT_REFERENZNUMMER);

			bVkPreisStattGestpreis = getIntegerParameter(ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_VKPREIS_STATT_GESTPREIS_IN_ARTIKELAUSWAHL);
			bLagerstandDesAnderenMandantenAnzeigen = getBooleanParameter(ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_LAGERSTAND_DES_ANDEREN_MANDANTEN_ANZEIGEN);

			bVkPreisLief1preis = getBooleanParameter(ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_VKPREISBASIS_IST_LIEF1PREIS);
			bLief1Infos = getBooleanParameter(ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_LIEF1INFO_IN_ARTIKELAUSWAHLLISTE);

			bArtikelgruppeAnzeigen = getBooleanParameter(ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ANZEIGEN_ARTIKELGRUPPE_IN_AUSWAHLLISTE);

			bZusatzbezeichnung2Anzeigen = getBooleanParameter(ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ANZEIGEN_ZUSATZBEZ2_IN_AUSWAHLLISTE);
			bArtikelklasseAnzeigen = getBooleanParameter(ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ANZEIGEN_ARTIKELKLASSE_IN_AUSWAHLLISTE);
			bVerfuegbarkeitAnzeigen = getBooleanParameter(ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_VERFUEGBARKEIT_IN_AUSWAHLLISTE);
			bKurzbezeichnungAnzeigen = getBooleanParameter(ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ANZEIGEN_KURZBEZEICHNUNG_IN_AUSWAHLLISTE);
			bReferenznummerAnzeigen = getBooleanParameter(ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ANZEIGEN_REFERENZNUMMER_IN_AUSWAHLLISTE);
			bHerstellernummerAnzeigen = getBooleanParameter(ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ANZEIGEN_HERSTELLER_IN_AUSWAHLLISTE);
			bMaterialAnzeigen = getBooleanParameter(ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_MATERIAL_IN_AUSWAHLLISTE);
			bLagerplaetzeAnzeigen = getBooleanParameter(ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ANZEIGEN_LAGERPLATZ_IN_AUSWAHLLISTE);

			VkpfartikelpreislisteDto[] vkpfartikelpreislisteDtos = getVkPreisfindungFac()
					.vkpfartikelpreislisteFindByMandantCNr(theClientDto.getMandant());

			if (vkpfartikelpreislisteDtos != null && vkpfartikelpreislisteDtos.length > 0) {
				vkPreisliste = vkpfartikelpreislisteDtos[0].getIId();
			}

			bDarfPreiseSehen = getBenutzerServicesFac().hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_EINKAUF,
					theClientDto);

			ParametermandantDto param = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ZUSAETZLICHES_LAGER_IN_AUSWAHLLISTE);
			String lagerCNr = (String) param.getCWertAsObject();

			if (lagerCNr != null && lagerCNr.length() > 0) {
				try {
					LagerDto lDto = getLagerFac().lagerFindByCNrByMandantCNrOhneExc(lagerCNr,
							theClientDto.getMandant());
					if (lDto != null) {
						lagerIIdZusaetzlichesLager = lDto.getIId();
						lagerCNrZusaetzlichesLager = lDto.getCNr();
						lagerDtoZusaetzlichesLager = lDto;
					}
				} catch (Throwable e) {
					// Nicht gefunden
				}
			}

			ParametermandantDto paramOhne = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKELSUCHE_OHNE);
			artikelnummerOhne = (String) paramOhne.getCWertAsObject();

			if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ARTIKELFREIGABE,
					theClientDto)) {
				bArtikelfreigabe = true;
			}

			if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_DUAL_USE,
					theClientDto)) {
				bDualUse = true;
			}

		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}
	}

	public PrintInfoDto getSDocPathAndPartner(Object key) {
		ArtikelDto artikelDto = null;
		try {
			artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall((Integer) key, theClientDto);
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

	private String getSiValue(String searchString) {
		DefaultSiPrefixParser spa = new DefaultSiPrefixParser();

		// first located si value
		return HelperServer.getDBValueFromBigDecimal(spa.parseFirst(searchString), 60);
	}

	private boolean getSiSortKrit() {
		SortierKriterium[] sortierKriterien = getQuery().getSortKrit();
		if (sortierKriterien == null)
			return false;

		for (SortierKriterium sortierKriterium : sortierKriterien) {
			if (sortierKriterium.kritName.equals("aspr.c_siwert")) {
				return true;
			}
		}
		return false;
	}
}
