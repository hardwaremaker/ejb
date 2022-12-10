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
package com.lp.server.projekt.fastlanereader;

import java.awt.Color;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.Icon;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.partner.service.PartnerFac;
import com.lp.server.projekt.fastlanereader.generated.FLRProjekttextsuche;
import com.lp.server.projekt.service.BereichDto;
import com.lp.server.projekt.service.HistoryartDto;
import com.lp.server.projekt.service.IProjektFLRData;
import com.lp.server.projekt.service.ProjectQueryResult;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.projekt.service.ProjektFLRDataDto;
import com.lp.server.projekt.service.ProjektFac;
import com.lp.server.projekt.service.ProjektHandlerFeature;
import com.lp.server.projekt.service.ProjektServiceFac;
import com.lp.server.projekt.service.ProjekttypsprDto;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeProjekt;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
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
import com.lp.util.StatusIcon;

/**
 * <p>
 * Diese Klasse kuemmert sich um ...
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellung: Vorname Nachname; dd.mm.06
 * </p>
 * 
 * <p>
 * 
 * @author $Author: robert $
 *         </p>
 * 
 * @version not attributable Date $Date: 2013/01/19 11:47:31 $
 */
public class ProjektHandler extends UseCaseHandler {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	boolean bProjektMitUmsatz = false;
	boolean bKurzzeichenStattName = false;
	boolean bSuchenInklusiveKbez = true;
	boolean bKbezStattOrt = false;
	boolean bProjektMitBetreiber = false;
	boolean bProjektMitArtikel = false;
	private HistoryartDto titelAusHistoryartInAuswahllisteAnzeigen = null;
	public static final String FLR_PROJEKT = "projekt.";
	/**
	 * @todo VF->VF -> Query von client abschicken.
	 */
	public static String lastQuery = "";

	private Feature cachedFeature;

	private class Feature extends FlrFeatureBase<IProjektFLRData> {
		private boolean enabled;

		public Feature() {
			super(getQuery());
		}

		public boolean isEnabled() {
			return enabled;
		}

		@Override
		protected void initialize(QueryParametersFeatures qpf) {
			enabled = qpf.hasFeature(ProjektHandlerFeature.PROJECT_DATA);
		}

		@Override
		protected IProjektFLRData[] createFlrData(int rows) {
			return new ProjektFLRDataDto[rows];
		}

		@Override
		protected IProjektFLRData createFlrDataObject(int index) {
			return new ProjektFLRDataDto();
		}

		public void setAddress(int row, String ort) {
			getFlrDataObject(row).setAddress(ort);
		}

		public void setCategory(int row, String category) {
			getFlrDataObject(row).setCategory(category);
		}

		public void setTitle(int row, String title) {
			getFlrDataObject(row).setTitle(title);
		}

		public void setDeadline(int row, Date d) {
			getFlrDataObject(row).setDeadlineMs(d.getTime());
		}

		public void setInternalDone(int row, Boolean internalDone) {
			getFlrDataObject(row).setInternalDone(internalDone);
		}

		public void setInternalComment(int row, String comment) {
			getFlrDataObject(row).setInternalComment(comment);
		}

		public void setPartnerId(int row, Integer partnerId) {
			getFlrDataObject(row).setPartnerId(partnerId);
		}

		public void setPriority(int row, Integer priority) {
			getFlrDataObject(row).setPriority(priority);
		}

		public void setStatusCnr(int row, String statusCnr) {
			getFlrDataObject(row).setStatusCnr(statusCnr);
		}
	}

	private class ProjektPartnerAnsprechpartnerFilterBuilder extends FlrFirmaAnsprechpartnerFilterBuilder {

		public ProjektPartnerAnsprechpartnerFilterBuilder(boolean bSuchenInklusiveKBez) {
			super(bSuchenInklusiveKBez);
		}

		public String getFlrPartner() {
			return FLR_PROJEKT + ProjektFac.FLR_PROJEKT_FLRPARTNER;
		}

		public String getFlrPropertyAnsprechpartnerIId() {
			return FLR_PROJEKT + "ansprechpartner_i_id";
		}
	}

	private Feature getFeature() {
		if (cachedFeature == null) {
			cachedFeature = new Feature();
		}
		return cachedFeature;
	}

	private String getPartnerAddress(String projekt_partner, String lkz, String plz, String ort) {
		String cAnschrift = null;
		if (projekt_partner != null && lkz != null) {
			cAnschrift = lkz + "-" + plz + " " + ort;
		}
		return cAnschrift;
	}

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {

		myLogger.warn("ProjektHandler.getPageAt BEGINN "+theClientDto.getBenutzername());
		
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = getLimit();
			int startIndex = getStartIndex(rowIndex, pageSize);
			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();

			// String queryString =
			// "SELECT distinct projekt.i_id, projekt.c_nr, projekt.typ_c_nr,
			// projekt.i_prio,projekt.bereich_i_id, projekt.n_umsatzgeplant,
			// projekt.i_wahrscheinlichkeit, projekt.d_dauer, projekt.t_internerledigt, "
			// +
			// " projekt.b_verrechenbar, cast(projekt.x_freetext as string) ,projekt.i_sort,
			// projekt.status_c_nr, projekt.t_zielwunschdatum, projekt.c_titel,
			// projekt.kategorie_c_nr, projekt.flrpartner.c_name1nachnamefirmazeile1,
			// projekt.flrpartner.c_kbez, flrland.c_lkz, flrlandplzort.c_plz,"
			// +
			// " flrort.c_name, projekt.flrpersonalZugewiesener.c_kurzzeichen,
			// projekt.flrpersonalZugewiesener.flrpartner.c_name1nachnamefirmazeile1,
			// projekt.flrpersonalErzeuger.c_kurzzeichen,projekt.flrpersonalErzeuger.flrpartner.c_name1nachnamefirmazeile1,
			// projekt.flrpersonalErzeuger.c_kurzzeichen from FLRProjekt projekt "
			// + " LEFT OUTER JOIN projekt.technikerset AS ts "
			// +
			// " left outer join projekt.flrpersonalZugewiesener.flrpartner.flrlandplzort as
			// flrlandplzort "
			// +
			// " left outer join
			// projekt.flrpersonalZugewiesener.flrpartner.flrlandplzort.flrort as flrort "
			// +
			// " left outer join
			// projekt.flrpersonalZugewiesener.flrpartner.flrlandplzort.flrland as flrland "
			// + buildWhereClause() + buildOrderByClause();

			String queryString = getFromClause() + buildWhereClause() + buildOrderByClause();
			lastQuery = queryString;
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount + 1];
			String[] tooltipData = new String[resultList.size()];
			String sProjekt = getTextRespectUISpr("proj.projekt", theClientDto.getMandant(), theClientDto.getLocUi());

			getFeature().setFlrRowCount(rows.length);

			int row = 0;
			int col = 0;
			while (resultListIterator.hasNext()) {
				// flrjoin: 1
				Object[] o = (Object[]) resultListIterator.next();

				Integer projektIId = (Integer) o[0];
				String c_nr = (String) o[1];

				String typ_c_nr = (String) o[2];
				Integer i_prio = (Integer) o[3];
				Integer bereich_i_id = (Integer) o[4];
				BigDecimal n_umsatzgeplant = (BigDecimal) o[5];
				Integer i_wahrscheinlichkeit = (Integer) o[6];
				Double d_dauer = (Double) o[7];
				java.util.Date t_internerledigt = (java.util.Date) o[8];
				Integer i_verrechenbar = (Integer) o[9];
				String x_freetext = (String) o[10];
				Integer i_sort = (Integer) o[11];
				String status_c_nr = (String) o[12];
				java.util.Date t_zielwunschdatum = (java.util.Date) o[13];
				String titel = (String) o[14];
				String kategorie_c_nr = (String) o[15];
				String projekt_partner = (String) o[16];
				String projekt_partner_kbez = (String) o[17];
				String lkz = (String) o[18];
				String plz = (String) o[19];
				String ort = (String) o[20];
				String zugewiesener_c_kurzzeichen = (String) o[21];
				String zugewiesener_c_name = (String) o[22];
				String erzeuger_c_kurzzeichen = (String) o[23];
				String erzeuger_c_name = (String) o[25];

				String betreiber = (String) o[26];
				Integer partner_i_id = (Integer) o[27];

				String verkaufsfortschritt = (String) o[28];
				String leadstatus = (String) o[29];
				String artikel = (String) o[30];
				String artikelbez = (String) o[31];

				String bereich_c_bez = (String) o[32];

				if (bereich_c_bez != null && bereich_c_bez.length() > 2) {
					bereich_c_bez = bereich_c_bez.substring(0, 2);
				}

				Long iAnzahlZeitbuchungen = (Long) o[33];

				String typSPR = (String) o[34];

				String titelAusHistoryart = (String) o[35];

				getFeature().setPartnerId(row, partner_i_id);

				Object[] rowToAddCandidate = new Object[colCount];
				rowToAddCandidate[getTableColumnInformation().getViewIndex("i_id")] = projektIId;

				rowToAddCandidate[getTableColumnInformation().getViewIndex("proj.nr")] = c_nr;

				rowToAddCandidate[getTableColumnInformation().getViewIndex("proj.bereich")] = bereich_c_bez;

				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.firma_nachname")] = projekt_partner;

				if (bKbezStattOrt) {

					rowToAddCandidate[getTableColumnInformation()
							.getViewIndex("lp.kurzbezeichnung")] = projekt_partner_kbez;

					if (getFeature().isEnabled()) {
						String cAnschrift = getPartnerAddress(projekt_partner, lkz, plz, ort);
						getFeature().setAddress(row, cAnschrift);
					}
				} else {
					String cAnschrift = getPartnerAddress(projekt_partner, lkz, plz, ort);
					//
					// if (projekt_partner != null && lkz != null) {
					//
					// cAnschrift = lkz + "-" + plz + " " + ort;
					//
					// }
					//
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.ort")] = cAnschrift;
					getFeature().setAddress(row, cAnschrift);
				}

				rowToAddCandidate[getTableColumnInformation().getViewIndex("proj.kategorie")] = kategorie_c_nr.trim();
				getFeature().setCategory(row, kategorie_c_nr.trim());

				rowToAddCandidate[getTableColumnInformation().getViewIndex("proj.titel")] = titel;
				getFeature().setTitle(row, titel);

				if (bProjektMitBetreiber) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("proj.betreiber")] = betreiber;
				}

				if (bProjektMitArtikel) {
					rowToAddCandidate[getTableColumnInformation()
							.getViewIndex("artikel.artikelnummerhalblang")] = artikel;
					rowToAddCandidate[getTableColumnInformation().getViewIndex("bes.artikelbezeichnung")] = artikelbez;
				}

				if (bKurzzeichenStattName) {
					rowToAddCandidate[getTableColumnInformation()
							.getViewIndex("proj.personal.erzeuger")] = erzeuger_c_kurzzeichen;
				} else {
					rowToAddCandidate[getTableColumnInformation()
							.getViewIndex("proj.personal.erzeuger")] = erzeuger_c_name;
				}

				if (bKurzzeichenStattName) {
					rowToAddCandidate[getTableColumnInformation()
							.getViewIndex("proj.personal.fuer")] = zugewiesener_c_kurzzeichen;
				} else {
					rowToAddCandidate[getTableColumnInformation()
							.getViewIndex("proj.personal.fuer")] = zugewiesener_c_name;
				}

				if (typSPR != null) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.typ")] = typSPR;
				} else {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.typ")] = typ_c_nr;
				}

				rowToAddCandidate[getTableColumnInformation().getViewIndex("proj.prio")] = i_prio;
				getFeature().setPriority(row, i_prio);

				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.status")] = getStatusMitUebersetzung(
						status_c_nr);
				getFeature().setStatusCnr(row, status_c_nr);

				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.termin")] = t_zielwunschdatum;
				getFeature().setDeadline(row, t_zielwunschdatum);

				rowToAddCandidate[getTableColumnInformation()
						.getViewIndex("proj.label.umsatzgeplant")] = n_umsatzgeplant;

				if (titelAusHistoryartInAuswahllisteAnzeigen != null) {

					rowToAddCandidate[getTableColumnInformation().getViewIndex("titelhistoryart")] = titelAusHistoryart;
				}

				if (bProjektMitUmsatz) {

					rowToAddCandidate[getTableColumnInformation()
							.getViewIndex("proj.label.wahrscheinlichkeit")] = i_wahrscheinlichkeit;
					rowToAddCandidate[getTableColumnInformation()
							.getViewIndex("proj.verkaufsfortschritt")] = verkaufsfortschritt;
					rowToAddCandidate[getTableColumnInformation().getViewIndex("proj.leadstatus")] = leadstatus;

				} else {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("proj.schaetzung")] = d_dauer != null
							? d_dauer
							: new Double(0.0);

					StatusIcon si = new StatusIcon();

					String tooltip = getProjektServiceFac().getTextVerrechenbar(i_verrechenbar, theClientDto);
					si.setTooltip(tooltip);
					if (i_verrechenbar == ProjektServiceFac.PROJEKT_VERRECHENBAR_NICHT_DEFINIERT) {
						si.setIcon(LocaleFac.STATUS_DATEN_UNGUELTIG);
					} else if (i_verrechenbar == ProjektServiceFac.PROJEKT_VERRECHENBAR_NICHT_VERRECHENBAR) {
						si = new StatusIcon();
						si.setIcon(LocaleFac.STATUS_STORNIERT);
					} else if (i_verrechenbar == ProjektServiceFac.PROJEKT_VERRECHENBAR_VERRECHENBAR) {
						si.setIcon(LocaleFac.STATUS_VERRECHNET);
					}

					rowToAddCandidate[getTableColumnInformation().getViewIndex("proj.label.verrechenbar")] = si;
					Boolean internalDone = new Boolean(t_internerledigt != null);
					rowToAddCandidate[getTableColumnInformation().getViewIndex("proj.internerledigt")] = internalDone;
					getFeature().setInternalDone(row, internalDone);
				}

				if (i_sort != null) {

					if (iAnzahlZeitbuchungen != null && iAnzahlZeitbuchungen > 0) {
						rowToAddCandidate[getTableColumnInformation().getViewIndex("Color")] = new Color(255, 161, 50);
					} else {
						rowToAddCandidate[getTableColumnInformation().getViewIndex("Color")] = new Color(176, 0, 255);
					}

				} else {
					if (iAnzahlZeitbuchungen != null && iAnzahlZeitbuchungen > 0) {
						rowToAddCandidate[getTableColumnInformation().getViewIndex("Color")] = new Color(0, 153, 51);// GRUEN
					}
				}

				if (x_freetext != null) {
					String text = "<b>" + sProjekt + " " + c_nr + ":</b>\n" + x_freetext;
					text = text.replaceAll("\n", "<br>");
					text = "<html>" + text + "</html>";
					tooltipData[row] = text;
				}

				if (getFeature().isEnabled()) {
					Session s = factory.openSession();
					String textQuery = "SELECT f.x_freetext FROM FLRProjekt AS f where f.i_id =" + projektIId;
					Query querys = s.createQuery(textQuery);
					Iterator r = querys.list().iterator();
					while (r.hasNext()) {
						Object ro = r.next();
						getFeature().setInternalComment(row, (String) ro);
						break;
					}
					s.close();

					// getFeature().setInternalComment(row, x_freetext);
				}

				rows[row] = rowToAddCandidate;

				row++;
				col = 0;
			}

			if (getFeature().isEnabled()) {
				ProjectQueryResult projectResult = new ProjectQueryResult(rows, this.getRowCount(), startIndex,
						endIndex, 0);
				projectResult.setFlrData(getFeature().getFlrData());
				result = projectResult;
			} else {
				result = new QueryResult(rows, getRowCount(), startIndex, endIndex, 0, tooltipData);
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			closeSession(session);
		}

		myLogger.warn("ProjektHandler.getPageAt ENDE "+theClientDto.getBenutzername());
		
		return result;
	}

	/**
	 * get the basic from clause for the HQL statement.
	 * 
	 * @return the from clause.
	 */
	private String getFromClause() {
		// return "from FLRProjekt projekt ";
		return "SELECT DISTINCT projekt.i_id," + "projekt.c_nr," + "projekt.typ_c_nr," + "projekt.i_prio,"
				+ "projekt.bereich_i_id," + "projekt.n_umsatzgeplant," + "projekt.i_wahrscheinlichkeit,"
				+ "projekt.d_dauer," + "projekt.t_internerledigt, " + "projekt.i_verrechenbar,"
				+ "CAST(projekt.x_freetext as string)," + "projekt.i_sort," + "projekt.status_c_nr,"
				+ "projekt.t_zielwunschdatum," + "projekt.c_titel," + "projekt.kategorie_c_nr,"
				+ "flrpar.c_name1nachnamefirmazeile1," + "flrpar.c_kbez," + "flrl.c_lkz," + "flrlpo.c_plz,"
				+ "flro.c_name," + "flrperszug.c_kurzzeichen," + "flrpartnerperszug.c_name1nachnamefirmazeile1,"
				+ "flrperserz.c_kurzzeichen," + "flrperserz.c_kurzzeichen,"
				+ "flrpartnerperserz.c_name1nachnamefirmazeile1," + "flrparbetr.c_name1nachnamefirmazeile1,"
				+ "flrpar.i_id," + "vkf.c_nr," + "leadstatus.c_bez,"
				+ "flrartikel.c_nr, (SELECT spr.c_bez FROM FLRArtikellistespr as spr WHERE spr.Id.artikelliste=flrartikel.i_id AND spr.Id.locale='"
				+ theClientDto.getLocUiAsString()
				+ "' ), projekt.flrbereich.c_bez, (select count(z.i_id) FROM FLRZeitdaten z WHERE z.i_belegartid=projekt.i_id AND z.c_belegartnr='"
				+ LocaleFac.BELEGART_PROJEKT
				+ "'), (SELECT t.c_bez FROM FLRTypspr t WHERE t.projekttyp_c_nr=projekt.flrtyp.c_nr AND t.mandant_c_nr=projekt.mandant_c_nr AND t.locale_c_nr='"
				+ theClientDto.getLocUiAsString()
				+ "'  ), projekt.flrtitelaushistory.c_titel  FROM FLRProjekt AS projekt"
				+ " LEFT OUTER JOIN projekt.technikerset AS ts " + " LEFT OUTER JOIN projekt.historyset AS hs"
				+ " LEFT OUTER JOIN projekt.flrpartner AS flrpar "
				+ " LEFT OUTER JOIN projekt.flrpartnerbetreiber AS flrparbetr "
				+ " LEFT OUTER JOIN flrpar.flrlandplzort AS flrlpo " + " LEFT OUTER JOIN flrlpo.flrland AS flrl "
				+ " LEFT OUTER JOIN flrlpo.flrort AS flro "
				+ " LEFT OUTER JOIN projekt.flrpersonalZugewiesener AS flrperszug "
				+ " LEFT OUTER JOIN flrperszug.flrpartner AS flrpartnerperszug "
				+ " LEFT OUTER JOIN projekt.flrpersonalErzeuger AS flrperserz "
				+ " LEFT OUTER JOIN flrperserz.flrpartner AS flrpartnerperserz "
				+ " LEFT OUTER JOIN projekt.flrvkfortschritt AS vkf "
				+ " LEFT OUTER JOIN vkf.flrleadstatus AS leadstatus" + " LEFT OUTER JOIN projekt.flrtyp as flrtyp"
				+ " LEFT OUTER JOIN projekt.flrartikel as flrartikel"
				+ " LEFT OUTER JOIN flrtyp.typ_typ_set as flrtypspr"
				+ " LEFT OUTER JOIN projekt.flrbereich"
				+ " LEFT OUTER JOIN projekt.flrtitelaushistory";
		// +
		// " left outer join projekt.flrpersonalZugewiesener.flrpartner.flrlandplzort as
		// flrlandplzort "
		// +
		// " left outer join
		// projekt.flrpersonalZugewiesener.flrpartner.flrlandplzort.flrort as flrort "
		// +
		// " left outer join
		// projekt.flrpersonalZugewiesener.flrpartner.flrlandplzort.flrland as flrland "
	}

	protected long getRowCountFromDataBase() {
		
		myLogger.warn("ProjektHandler.getRowCountFromDataBase BEGINN "+theClientDto.getBenutzername());
		
		String queryString = "SELECT COUNT(DISTINCT projekt.i_id)" + " FROM FLRProjekt AS projekt"
				+ " LEFT OUTER JOIN projekt.technikerset AS ts" + " LEFT OUTER JOIN projekt.historyset AS hs"
				+ " LEFT OUTER JOIN projekt.flrtyp as flrtyp" + " LEFT OUTER JOIN flrtyp.typ_typ_set as flrtypspr"
				+ buildWhereClause();
		
		long l=getRowCountFromDataBaseByQuery(queryString);
		
		myLogger.warn("ProjektHandler.getRowCountFromDataBase ENDE "+theClientDto.getBenutzername());
		
		return l;
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

					String flrpartnerKrit = FLR_PROJEKT + ProjektFac.FLR_PROJEKT_FLRPARTNER + "."
							+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1;
					if (isFilterPartner(filterKriterien[i])) {
						ProjektPartnerAnsprechpartnerFilterBuilder filterBuilder = new ProjektPartnerAnsprechpartnerFilterBuilder(
								bSuchenInklusiveKbez);
						filterBuilder.buildFirmaAnsprechpartnerFilter(filterKriterien[i], where);
						// if (bSuchenInklusiveKbez
						// && filterKriterien[i].kritName
						// .equals(flrpartnerKrit)) {
						// getWhereFlrPartner(where, filterKriterien, i);
						// where.append(" OR lower(" + FLR_PROJEKT
						// + ProjektFac.FLR_PROJEKT_FLRPARTNER + "."
						// + PartnerFac.FLR_PARTNER_C_KBEZ + ")");
						// where.append(" " + filterKriterien[i].operator);
						// where.append(" "
						// + filterKriterien[i].value.toLowerCase() + ")");
						// } else if (bSuchenInklusiveKbez == false
						// && filterKriterien[i].kritName
						// .equals(flrpartnerKrit)) {
						// getWhereFlrPartner(where, filterKriterien, i);
						// where.append(")");
					} else if (filterKriterien[i].kritName.equals(ProjektFac.FLR_PROJEKT_C_NR)) {
						try {
							String sValue = super.buildWhereBelegnummer(filterKriterien[i], false,
									ParameterFac.KATEGORIE_PROJEKT,
									ParameterFac.PARAMETER_PROJEKT_BELEGNUMMERSTARTWERT);
							if (!istBelegnummernInJahr("FLRProjekt", sValue)) {
								sValue = super.buildWhereBelegnummer(filterKriterien[i], true);
							}
							where.append(" " + FLR_PROJEKT + filterKriterien[i].kritName);
							where.append(" " + filterKriterien[i].operator);
							where.append(" " + sValue);
						} catch (Exception ex) {
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, ex);
						}
					} else if (filterKriterien[i].kritName.equals("c_suche")) {

						where.append(buildWhereClauseExtendedSearchWithoutDuplicates(
								FLRProjekttextsuche.class.getSimpleName(), FLR_PROJEKT, filterKriterien[i]));

					} else if (filterKriterien[i].kritName.endsWith("personal_i_id_zugewiesener")) {

						where.append(" ( projekt.flrpersonalZugewiesener.i_id=" + filterKriterien[i].value
								+ " OR ts.personal_i_id=" + filterKriterien[i].value
								+ " OR (projekt.flrbereich.b_duchgefuehrt_von_in_offene=1 AND hs.flrpersonal_wirddurchgefuehrtvon.i_id="
								+ filterKriterien[i].value + "))");

					} else if (filterKriterien[i].kritName.endsWith("flrtypspr.c_bez")) {

//						where.append(" ( lower(flrtypspr.c_bez) LIKE " + filterKriterien[i].value.toLowerCase()
//								+ " OR lower(flrtyp.c_nr) LIKE " + filterKriterien[i].value.toLowerCase() + ")");
						where.append(
								filterKriterien[i].getQueryWithMultipleColumns(true, "flrtypspr.c_bez", "flrtyp.c_nr"));

					}

					else {
						String critName = filterKriterien[i].kritName;
						if ("i_id".equals(critName)) {
							critName = FLR_PROJEKT + critName;
						}
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" lower(" + critName + ")");
						} else {
							where.append(" " + critName);
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

	private boolean isFilterPartner(FilterKriterium filterKriterium) {
		return filterKriterium.kritName.equals(FLR_PROJEKT + ProjektFac.FLR_PROJEKT_FLRPARTNER + "."
				+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1);
	}

	public QueryResult sort(SortierKriterium[] sortierKriterien, Object selectedId) throws EJBExceptionLP {

		myLogger.warn("ProjektHandler.sort BEGINN "+theClientDto.getBenutzername());

		
		if (this.getQuery() != null) {
			this.getQuery().setSortKrit(sortierKriterien);
		}

		QueryResult result = null;
		int rowNumber = 0;

		if (selectedId != null && ((Integer) selectedId).intValue() >= 0) {
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();

				// String queryString =
				// "SELECT distinct projekt.i_id, projekt.c_nr, projekt.typ_c_nr,
				// projekt.i_prio,projekt.bereich_i_id, projekt.n_umsatzgeplant,
				// projekt.i_wahrscheinlichkeit, projekt.d_dauer, projekt.t_internerledigt, "
				// +
				// " projekt.b_verrechenbar, cast(projekt.x_freetext as string),projekt.i_sort,
				// projekt.status_c_nr, projekt.t_zielwunschdatum, projekt.c_titel,
				// projekt.kategorie_c_nr, projekt.flrpartner.c_name1nachnamefirmazeile1,
				// projekt.flrpartner.c_kbez, projekt.flrpartner.flrlandplzort.flrland.c_lkz,
				// projekt.flrpartner.flrlandplzort.c_plz,"
				// +
				// " projekt.flrpartner.flrlandplzort.flrort.c_name,
				// projekt.flrpersonalZugewiesener.c_kurzzeichen,
				// projekt.flrpersonalZugewiesener.flrpartner.c_name1nachnamefirmazeile1,
				// projekt.flrpersonalErzeuger.c_kurzzeichen,projekt.flrpersonalErzeuger.flrpartner.c_name1nachnamefirmazeile1,
				// projekt.flrpersonalErzeuger.c_kurzzeichen from FLRProjekt projekt LEFT OUTER
				// JOIN projekt.technikerset AS ts "
				// + this.buildWhereClause() + this.buildOrderByClause();

				String queryString = getFromClause() + buildWhereClause() + buildOrderByClause();

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

		myLogger.warn("ProjektHandler.sort ENDE "+theClientDto.getBenutzername());
		
		return result;
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
							// orderBy.append(FLR_PROJEKT +
							// kriterien[i].kritName);
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
				orderBy.append(FLR_PROJEKT).append("c_nr DESC"); // t_zielwunschdatum
				// und i_prio
				sortAdded = true;
			}
			if (orderBy.indexOf(FLR_PROJEKT + ProjektFac.FLR_PROJEKT_I_ID) < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" ").append(FLR_PROJEKT).append(ProjektFac.FLR_PROJEKT_I_ID).append(" ASC ");
				sortAdded = true;
			}
			if (sortAdded) {
				orderBy.insert(0, " ORDER BY ");
			}
		}
		return orderBy.toString();
	}

	private void setupParameters() {
		try {
			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_PROJEKT, ParameterFac.PARAMETER_PROJEKT_MIT_UMSATZ);
			bProjektMitUmsatz = (Boolean) parameter.getCWertAsObject();

			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_SUCHEN_INKLUSIVE_KBEZ);
			bSuchenInklusiveKbez = (java.lang.Boolean) parameter.getCWertAsObject();
			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(), ParameterFac.KATEGORIE_PROJEKT,
					ParameterFac.PARAMETER_KURZBEZEICHNUNG_STATT_ORT_IN_AUSWAHLLISTE);
			bKbezStattOrt = (java.lang.Boolean) parameter.getCWertAsObject();

			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(), ParameterFac.KATEGORIE_PROJEKT,
					ParameterFac.PARAMETER_KURZZEICHEN_STATT_NAME_IN_AUSWAHLLISTE);
			bKurzzeichenStattName = (java.lang.Boolean) parameter.getCWertAsObject();

			bProjektMitBetreiber = getProjektServiceFac().esGibtMindestensEinenBereichMitBetreiber(theClientDto);
			bProjektMitArtikel = getProjektServiceFac().esGibtMindestensEinenBereichMitArtikel(theClientDto);

			titelAusHistoryartInAuswahllisteAnzeigen = getProjektServiceFac()
					.getHistoryartInAuswahllisteAnzeigen(theClientDto);

		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}
	}

	private TableColumnInformation createColumnInformation(String mandant, Locale locUi) {

		TableColumnInformation columns = new TableColumnInformation();

		columns.add("i_id", Integer.class, "i_id", -1, "i_id");

		columns.add("proj.bereich", String.class, getTextRespectUISpr("proj.bereich", mandant, locUi), 3,
				FLR_PROJEKT + "flrbereich.c_bez");

		columns.add("proj.nr", String.class, getTextRespectUISpr("proj.nr", mandant, locUi), 10,
				FLR_PROJEKT + ProjektFac.FLR_PROJEKT_C_NR);

		columns.add("lp.firma_nachname", String.class, getTextRespectUISpr("lp.firma_nachname", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST,
				"flrpar." + PartnerFac.FLR_PARTNER_C_NAME1NACHNAMEFIRMAZEILE1);

		if (bKbezStattOrt) {
			columns.add("lp.kurzbezeichnung", String.class, getTextRespectUISpr("lp.kurzbezeichnung", mandant, locUi),
					QueryParameters.FLR_BREITE_SHARE_WITH_REST, "flrpar." + PartnerFac.FLR_PARTNER_C_KBEZ);
		} else {
			columns.add("lp.ort", String.class, getTextRespectUISpr("lp.ort", mandant, locUi),
					QueryParameters.FLR_BREITE_SHARE_WITH_REST, "flrl.c_lkz" + ",  flrlpo.c_plz");
		}

		columns.add("proj.kategorie", String.class, getTextRespectUISpr("proj.kategorie", mandant, locUi),
				QueryParameters.FLR_BREITE_M, FLR_PROJEKT + ProjektFac.FLR_PROJEKT_KATEGORIE_C_NR);

		columns.add("proj.titel", String.class, getTextRespectUISpr("proj.titel", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, FLR_PROJEKT + ProjektFac.FLR_PROJEKT_C_TITEL);

		if (bProjektMitBetreiber) {
			columns.add("proj.betreiber", String.class, getTextRespectUISpr("proj.betreiber", mandant, locUi),
					QueryParameters.FLR_BREITE_SHARE_WITH_REST,
					"flrartikel." + PartnerFac.FLR_PARTNER_C_NAME1NACHNAMEFIRMAZEILE1);

		}

		if (bProjektMitArtikel) {
			columns.add("artikel.artikelnummerhalblang", String.class,
					getTextRespectUISpr("artikel.artikelnummerhalblang", mandant, locUi),
					QueryParameters.FLR_BREITE_IDENT, "flrartikel.c_nr");

			columns.add("bes.artikelbezeichnung", String.class,
					getTextRespectUISpr("bes.artikelbezeichnung", mandant, locUi),
					QueryParameters.FLR_BREITE_SHARE_WITH_REST, Facade.NICHT_SORTIERBAR);

		}

		String sortierungNachPersonalErzeuger = "flrpartnerperserz."
				+ PartnerFac.FLR_PARTNER_C_NAME1NACHNAMEFIRMAZEILE1;

		if (bKurzzeichenStattName) {
			sortierungNachPersonalErzeuger = "flrperserz.c_kurzzeichen";
		}

		columns.add("proj.personal.erzeuger", String.class,
				getTextRespectUISpr("proj.personal.erzeuger", mandant, locUi), QueryParameters.FLR_BREITE_M,
				sortierungNachPersonalErzeuger);

		String sortierungNachPersonalZugewiesener = "flrpartnerperszug."
				+ PartnerFac.FLR_PARTNER_C_NAME1NACHNAMEFIRMAZEILE1;
		if (bKurzzeichenStattName) {
			sortierungNachPersonalZugewiesener = "flrperszug.c_kurzzeichen";
		}

		columns.add("proj.personal.fuer", String.class, getTextRespectUISpr("proj.personal.fuer", mandant, locUi),
				QueryParameters.FLR_BREITE_M, sortierungNachPersonalZugewiesener);

		columns.add("lp.typ", String.class, getTextRespectUISpr("lp.typ", mandant, locUi), QueryParameters.FLR_BREITE_M,
				FLR_PROJEKT + ProjektFac.FLR_PROJEKT_TYP_C_NR);
		
		if (titelAusHistoryartInAuswahllisteAnzeigen != null) {

			columns.add("titelhistoryart", String.class, titelAusHistoryartInAuswahllisteAnzeigen.getCBez(),
					QueryParameters.FLR_BREITE_M, FLR_PROJEKT + ProjektFac.FLR_PROJEKT_TYP_C_NR);
		}
		
		columns.add("proj.prio", Integer.class, getTextRespectUISpr("proj.prioritaet.short", mandant, locUi), 1,
				FLR_PROJEKT + ProjektFac.FLR_PROJEKT_I_PRIO,
				getTextRespectUISpr("proj.prioritaet.tooltip", mandant, locUi));
		columns.add("lp.status", Icon.class, getTextRespectUISpr("lp.status", mandant, locUi),
				QueryParameters.FLR_BREITE_XS, FLR_PROJEKT + ProjektFac.FLR_PROJEKT_STATUS_C_NR);
		columns.add("lp.termin", Date.class, getTextRespectUISpr("lp.termin", mandant, locUi), 10,
				FLR_PROJEKT + ProjektFac.FLR_PROJEKT_T_ZIELDATUM);

	

		columns.add("proj.label.umsatzgeplant", BigDecimal.class,
				getTextRespectUISpr("proj.label.umsatzgeplant", mandant, locUi), QueryParameters.FLR_BREITE_M,
				FLR_PROJEKT + ProjektFac.FLR_PROJEKT_N_UMSATZGEPLANT);

		if (bProjektMitUmsatz) {

			columns.add("proj.label.wahrscheinlichkeit", Integer.class,
					getTextRespectUISpr("proj.label.wahrscheinlichkeit", mandant, locUi), QueryParameters.FLR_BREITE_XS,
					FLR_PROJEKT + ProjektFac.FLR_PROJEKT_I_WAHRSCHEINLICHKEIT);
			columns.add("proj.verkaufsfortschritt", String.class,
					getTextRespectUISpr("proj.verkaufsfortschritt", mandant, locUi), QueryParameters.FLR_BREITE_XS,
					"vkf.c_nr");
			columns.add("proj.leadstatus", String.class, getTextRespectUISpr("proj.leadstatus", mandant, locUi),
					QueryParameters.FLR_BREITE_XS, "leadstatus.c_bez");
		} else {
			columns.add("proj.schaetzung", Double.class, getTextRespectUISpr("proj.schaetzung", mandant, locUi),
					QueryParameters.FLR_BREITE_XS, FLR_PROJEKT + ProjektFac.FLR_PROJEKT_D_DAUER);

			columns.add("proj.label.verrechenbar", Icon.class,
					getTextRespectUISpr("proj.verrechenbar.short", mandant, locUi), QueryParameters.FLR_BREITE_S,
					FLR_PROJEKT + ProjektFac.FLR_PROJEKT_I_VERRECHENBAR,
					getTextRespectUISpr("proj.verrechenbar.tooltip", mandant, locUi));
			columns.add("proj.internerledigt", Boolean.class,
					getTextRespectUISpr("proj.internerledigt.short", mandant, locUi), QueryParameters.FLR_BREITE_S,
					FLR_PROJEKT + ProjektFac.FLR_PROJEKT_T_INTERNERLEDIGT,
					getTextRespectUISpr("proj.internerledigt.tooltip", mandant, locUi));

		}

		columns.add("Color", Color.class, "", 1, "");

		return columns;
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

	public PrintInfoDto getSDocPathAndPartner(Object key) {
		ProjektDto projektDto = null;
		BereichDto bereichDto = null;
		try {
			projektDto = getProjektFac().projektFindByPrimaryKey((Integer) key);
			bereichDto = getProjektServiceFac().bereichFindByPrimaryKey(projektDto.getBereichIId());
		} catch (Exception e) {
			// Nicht gefunden
		}
		if (projektDto != null) {
			// String sPath = JCRDocFac.HELIUMV_NODE
			// + "/"
			// + theClientDto.getMandant()
			// + "/"
			// + LocaleFac.BELEGART_PROJEKT.trim()
			// + "/"
			// + getProjektServiceFac().bereichFindByPrimaryKey(
			// projektDto.getBereichIId()).getCBez() + "/"
			// + projektDto.getCNr().replace("/", ".");

			DocPath docPath = new DocPath(new DocNodeProjekt(projektDto, bereichDto));
			return new PrintInfoDto(docPath, projektDto.getPartnerIId(), getSTable());
		} else {
			return null;
		}
	}

	public String getSTable() {
		return "PROJEKT";
	}

}
