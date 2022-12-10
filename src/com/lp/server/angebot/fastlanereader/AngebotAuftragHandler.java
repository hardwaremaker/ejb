
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
package com.lp.server.angebot.fastlanereader;

import java.awt.Color;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.Icon;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.angebot.fastlanereader.generated.FLRAngebotauftrag;
import com.lp.server.angebot.service.AngebotFac;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftrag;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragtextsuche;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragFLRDataDto;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.auftrag.service.AuftragHandlerFeature;
import com.lp.server.auftrag.service.AuftragQueryResult;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.IAuftragFLRData;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.fastlanereader.generated.FLRPartner;
import com.lp.server.partner.service.AddressContactDto;
import com.lp.server.partner.service.AdresseDto;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.IAddressContact;
import com.lp.server.partner.service.IAdresse;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.system.fastlanereader.generated.FLRLandplzort;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeAuftrag;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.FlrFirmaAnsprechpartnerFilterBuilder;
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

/**
 * <p>
 * Hier wird die FLR Funktionalitaet fuer den Auftrag implementiert. Pro UseCase
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
 * @author martin werner, uli walch
 * @version 1.0
 */

public class AngebotAuftragHandler extends UseCaseHandler {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public static final String FLR_ANGEBOTAUFTRAG = "flrangebotauftrag.";
	public static final String FLR_ANGEBOTAUFTRAG_FROM_CLAUSE = " from FLRAngebotauftrag flrangebotauftrag ";
	public static final String FLR_AUFTRAG_TEXTSUCHE_CLASSNAME = FLRAuftragtextsuche.class.getSimpleName();

	boolean verrechenbarStattRohsAnzeigen = false;
	boolean bAuftragsfreigabe = false;

	boolean bAuftragspositionstermin_in_auswahl_anzeigen = false;

	private class AuftragKundeAnsprechpartnerFilterBuilder extends FlrFirmaAnsprechpartnerFilterBuilder {

		public AuftragKundeAnsprechpartnerFilterBuilder(boolean bSuchenInklusiveKBez) {
			super(bSuchenInklusiveKBez);
		}

		@Override
		public String getFlrPartner() {
			return FLR_ANGEBOTAUFTRAG + AuftragFac.FLR_AUFTRAG_FLRKUNDE + "." + LieferantFac.FLR_PARTNER;
		}

		@Override
		public String getFlrPropertyAnsprechpartnerIId() {
			return FLR_ANGEBOTAUFTRAG + "ansprechpartner_i_id_kunde";
		}

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
		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = getLimit();
			int startIndex = getStartIndex(rowIndex, pageSize);
			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			String queryString = getFromClause() + buildWhereClause() + buildOrderByClause();

			// logQuery(queryString);
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;
			int col = 0;

			// darf Preise sehen.
			final boolean bDarfPreiseSehen = getTheJudgeFac().hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF,
					theClientDto);

			while (resultListIterator.hasNext()) {

				FLRAngebotauftrag flrangebotauftrag = (FLRAngebotauftrag) resultListIterator.next();

				java.util.Date dNaechsterTerminAusPosition = flrangebotauftrag.getT_positionsliefertermin();

				Object[] rowToAddCandidate = new Object[colCount];

				rowToAddCandidate[getTableColumnInformation().getViewIndex("i_id")] = flrangebotauftrag.getI_id();

				// Kuerzel fuer die Auftragart
				String auftragart = null;

				if (flrangebotauftrag.getFlrauftrag() != null) {
					if (flrangebotauftrag.getFlrauftrag().getAuftragart_c_nr()
							.equals(AuftragServiceFac.AUFTRAGART_ABRUF)) {
						auftragart = AuftragServiceFac.AUFTRAGART_ABRUF_SHORT;
					} else if (flrangebotauftrag.getFlrauftrag().getAuftragart_c_nr()
							.equals(AuftragServiceFac.AUFTRAGART_RAHMEN)) {
						auftragart = AuftragServiceFac.AUFTRAGART_RAHMEN_SHORT;
					} else if (flrangebotauftrag.getFlrauftrag().getAuftragart_c_nr()
							.equals(AuftragServiceFac.AUFTRAGART_WIEDERHOLEND)) {
						auftragart = AuftragServiceFac.AUFTRAGART_WIEDERHOLEND_SHORT;
					} else if (flrangebotauftrag.getFlrauftrag().getBestellung_i_id_anderermandant() != null) {
						auftragart = "M";
					}
				}

				rowToAddCandidate[getTableColumnInformation().getViewIndex("art")] = auftragart;

				if(flrangebotauftrag.getAuftrag_i_id()!=null) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.belegart")] =LocaleFac.BELEGART_AUFTRAG;
				}else {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.belegart")] =LocaleFac.BELEGART_ANGEBOT;
					
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.nachfasstermin")] =flrangebotauftrag.getT_angebot_nachfasstermin();
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.kommentar")] = getKommentarart(
							flrangebotauftrag.getFlrangebot().getX_internerkommentar(),
							flrangebotauftrag.getFlrangebot().getX_externerkommentar());
					
					if (flrangebotauftrag.getFlrangebot().getFlrakquisestatus() != null) {

						String zusatzstatus = Helper.fitString2Length(flrangebotauftrag.getFlrangebot().getFlrakquisestatus().getC_bez(), 15, ' ');

						rowToAddCandidate[getTableColumnInformation().getViewIndex("angb.akquisestatus")] = zusatzstatus;

					}
					
				}
				
				
				
				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.belegnummer")] = flrangebotauftrag
						.getC_nr();

				rowToAddCandidate[getTableColumnInformation()
						.getViewIndex("lp.kunde")] = flrangebotauftrag.getFlrkunde() == null ? null
								: flrangebotauftrag.getFlrkunde().getFlrpartner().getC_name1nachnamefirmazeile1();

				// IMS 1757 die Anschrift des Kunden anzeigen
				String cAnschrift = null; // eine Liefergruppenanfrage hat
				// keinen Lieferanten

				if (flrangebotauftrag.getFlrkunde() != null) {
					FLRLandplzort flranschrift = flrangebotauftrag.getFlrkunde().getFlrpartner().getFlrlandplzort();

					if (flranschrift != null) {
						cAnschrift = flranschrift.getFlrland().getC_lkz() + "-" + flranschrift.getC_plz() + " "
								+ flranschrift.getFlrort().getC_name();
					}

				}

				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.ort")] = cAnschrift;
				String proj_bestellnummer = "";

				if (flrangebotauftrag.getC_bez() != null) {
					proj_bestellnummer += flrangebotauftrag.getC_bez();
				}

				if (flrangebotauftrag.getC_bestellnummer() != null) {
					proj_bestellnummer += " | " + flrangebotauftrag.getC_bestellnummer();
				}

				rowToAddCandidate[getTableColumnInformation()
						.getViewIndex("auft.projektbestellnummer")] = proj_bestellnummer;

				if (bAuftragspositionstermin_in_auswahl_anzeigen) {
					rowToAddCandidate[getTableColumnInformation()
							.getViewIndex("lp.termin")] = dNaechsterTerminAusPosition;
				} else {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.termin")] = flrangebotauftrag
							.getT_auftrag_liefertermin();
				}

				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.datum")] = flrangebotauftrag
						.getT_belegdatum();

				if (flrangebotauftrag.getFlrvertreter() != null) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.vertreter")] = flrangebotauftrag
							.getFlrvertreter().getC_kurzzeichen();
				}

				String sStatus = flrangebotauftrag.getStatus_c_nr();

				if (flrangebotauftrag.getFlrauftrag() != null) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.status")] = getStatusMitUebersetzung(
							sStatus, flrangebotauftrag.getFlrauftrag().getT_versandzeitpunkt(),
							flrangebotauftrag.getFlrauftrag().getC_versandtype());
				} else if (flrangebotauftrag.getFlrangebot() != null) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.status")] = getStatusMitUebersetzung(
							sStatus, flrangebotauftrag.getFlrangebot().getT_versandzeitpunkt(),
							flrangebotauftrag.getFlrangebot().getC_versandtype());
				}

				BigDecimal nWert = new BigDecimal(0);

				if (flrangebotauftrag.getN_wert() != null
						&& !flrangebotauftrag.getStatus_c_nr().equals(AuftragServiceFac.AUFTRAGSTATUS_STORNIERT)) {
					nWert = flrangebotauftrag.getN_wert();
				}

				if (bDarfPreiseSehen) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.wert")] = nWert;
				}
				rowToAddCandidate[getTableColumnInformation().getViewIndex("waehrung")] = flrangebotauftrag
						.getWaehrung_c_nr();

				if (flrangebotauftrag.getFlrauftrag() != null) {
					if (verrechenbarStattRohsAnzeigen) {
						if (flrangebotauftrag.getFlrauftrag().getT_verrechenbar() != null) {
							rowToAddCandidate[getTableColumnInformation()
									.getViewIndex("auft.verrechenbar")] = Boolean.TRUE;
						} else {
							rowToAddCandidate[getTableColumnInformation()
									.getViewIndex("auft.verrechenbar")] = Boolean.FALSE;
						}

					} else {
						rowToAddCandidate[getTableColumnInformation().getViewIndex("detail.label.rohs")] = Helper
								.short2Boolean(flrangebotauftrag.getFlrauftrag().getB_rohs());
					}

					rowToAddCandidate[getTableColumnInformation().getViewIndex("detail.label.unverbindlich")] = Helper
							.short2Boolean(flrangebotauftrag.getFlrauftrag().getB_lieferterminunverbindlich());

					if (bAuftragsfreigabe) {
						if (flrangebotauftrag.getFlrauftrag().getT_auftragsfreigabe() != null) {
							rowToAddCandidate[getTableColumnInformation().getViewIndex("auft.freigabe")] = Boolean.TRUE;
						}
					}

					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.kommentar")] = getKommentarart(
							flrangebotauftrag.getFlrauftrag().getX_internerkommentar(),
							flrangebotauftrag.getFlrauftrag().getX_externerkommentar());

					if (flrangebotauftrag.getFlrauftrag().getB_poenale().equals(new Short((short) 1))) {
						// rows[row][col++] = iAnzahlER == 0 ? Color.RED :
						// Color.MAGENTA ;
						rowToAddCandidate[getTableColumnInformation().getViewIndex("Color")] = Color.RED;
					}
					// PJ19958
					if (rowToAddCandidate[getTableColumnInformation().getViewIndex("Color")] == null) {
						if (Helper.short2boolean(
								flrangebotauftrag.getFlrauftrag().getFlrverrechenbar().getB_verrechenbar()) == false) {
							rowToAddCandidate[getTableColumnInformation().getViewIndex("Color")] = Color.LIGHT_GRAY;
						}
					}
				}

				rows[row] = rowToAddCandidate;

				++row;
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

	/**
	 * gets the total number of rows represented by the current query.
	 * 
	 * @return int die Anzehl der Zeilen im Ergebnis
	 * @see UseCaseHandler#getRowCountFromDataBase()
	 */
	protected long getRowCountFromDataBase() {
		String queryString = "SELECT  COUNT(*) from FLRAngebotauftrag  as flrangebotauftrag  " + buildWhereClause();

		long rowCount = 0;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			session = setFilter(session);

			Query query = session.createQuery(queryString);
			List<?> rowCountResult = query.list();
			if (rowCountResult != null && rowCountResult.size() > 0) {
				rowCount = ((Long) rowCountResult.get(0)).longValue();
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			sessionClose(session);
		}

		return rowCount;

	}

	private boolean isAdresseKunde(FilterKriterium filterKriterium) {
		return filterKriterium.kritName.equals(AuftragFac.FLR_AUFTRAG_FLRKUNDE + "." + LieferantFac.FLR_PARTNER + "."
				+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1);
	}

	private boolean isAdresseLieferadresse(FilterKriterium filterKriterium) {
		return filterKriterium.kritName.equals(AuftragFac.FLR_AUFTRAG_FLRKUNDELIEFERADRESSE + "."
				+ LieferantFac.FLR_PARTNER + "." + PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1);
	}

	private String buildAdresseFilterImpl(String adresseTyp, FilterKriterium filterKriterium) {
		StringBuffer where = new StringBuffer();
		ParametermandantDto parameter = null;
		try {
			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_SUCHEN_INKLUSIVE_KBEZ);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		if (parameter.asBoolean()) {
			if (filterKriterium.isBIgnoreCase()) {
				where.append(" ( upper(" + FLR_ANGEBOTAUFTRAG + filterKriterium.kritName + ")");
				where.append(" " + filterKriterium.operator);
				where.append(" " + filterKriterium.value.toUpperCase());
				where.append(" OR upper(" + FLR_ANGEBOTAUFTRAG + adresseTyp + ".flrpartner.c_kbez" + ")");
				where.append(" " + filterKriterium.operator);
				where.append(" " + filterKriterium.value.toUpperCase() + ") ");
			} else {
				where.append(" " + FLR_ANGEBOTAUFTRAG + filterKriterium.kritName);
				where.append(" " + filterKriterium.operator);
				where.append(" " + filterKriterium.value);
				where.append("OR " + FLR_ANGEBOTAUFTRAG + adresseTyp + ".flrpartner.c_kbez");
				where.append(" " + filterKriterium.operator);
				where.append(" " + filterKriterium.value);
			}
		} else {
			if (filterKriterium.isBIgnoreCase()) {
				where.append(" upper(" + FLR_ANGEBOTAUFTRAG + filterKriterium.kritName + ")");
			} else {
				where.append(" " + FLR_ANGEBOTAUFTRAG + filterKriterium.kritName);
			}

			where.append(" " + filterKriterium.operator);

			if (filterKriterium.isBIgnoreCase()) {
				where.append(" " + filterKriterium.value.toUpperCase());
			} else {
				where.append(" " + filterKriterium.value);
			}
		}
		return where.toString();
	}

	private String buildAdresseFilterKunde(FilterKriterium filterKriterium) {
		AuftragKundeAnsprechpartnerFilterBuilder filterBuilder = new AuftragKundeAnsprechpartnerFilterBuilder(
				getParameterFac().getSuchenInklusiveKBez(theClientDto.getMandant()));
		return filterBuilder.buildFirmaAnsprechpartnerFilter(filterKriterium);
		// return buildAdresseFilterImpl(AuftragFac.FLR_AUFTRAG_FLRKUNDE,
		// filterKriterium) ;
	}

	private String buildAdresseFilterLieferadresse(FilterKriterium filterKriterium) {
		return buildAdresseFilterImpl(AuftragFac.FLR_AUFTRAG_FLRKUNDELIEFERADRESSE, filterKriterium);
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
									ParameterFac.KATEGORIE_AUFTRAG,
									ParameterFac.PARAMETER_AUFTRAG_BELEGNUMMERSTARTWERT);
							// Belegnummernsuche auch in "altem" Jahr, wenn im
							// neuen noch keines vorhanden ist
							if (!istBelegnummernInJahr("FLRAuftrag", sValue)) {
								sValue = super.buildWhereBelegnummer(filterKriterien[i], true);
							}
							where.append(" " + FLR_ANGEBOTAUFTRAG + filterKriterien[i].kritName);
							where.append(" " + filterKriterien[i].operator);
							where.append(" " + sValue);
						} catch (Exception ex) {
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, ex);
						}
					} else if (isAdresseKunde(filterKriterien[i])) {
						where.append(buildAdresseFilterKunde(filterKriterien[i]));
					} else if (isAdresseLieferadresse(filterKriterien[i])) {
						where.append(buildAdresseFilterLieferadresse(filterKriterien[i]));
					} else if (filterKriterien[i].kritName.equals(AuftragFac.FLR_AUFTRAG_FLRKUNDE + "."
							+ LieferantFac.FLR_PARTNER + "." + PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1)) {
						ParametermandantDto parameter = null;
						try {
							parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
									ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_SUCHEN_INKLUSIVE_KBEZ);
						} catch (RemoteException ex) {
							throwEJBExceptionLPRespectOld(ex);
						}
						Boolean bSuchenInklusiveKbez = (java.lang.Boolean) parameter.getCWertAsObject();
						if (bSuchenInklusiveKbez) {
							if (filterKriterien[i].isBIgnoreCase()) {
								where.append(" ( upper(" + FLR_ANGEBOTAUFTRAG + filterKriterien[i].kritName + ")");
								where.append(" " + filterKriterien[i].operator);
								where.append(" " + filterKriterien[i].value.toUpperCase());
								where.append(" OR upper(" + FLR_ANGEBOTAUFTRAG + "flrkunde.flrpartner.c_kbez" + ")");
								where.append(" " + filterKriterien[i].operator);
								where.append(" " + filterKriterien[i].value.toUpperCase() + ") ");
							} else {
								where.append(" " + FLR_ANGEBOTAUFTRAG + filterKriterien[i].kritName);
								where.append(" " + filterKriterien[i].operator);
								where.append(" " + filterKriterien[i].value);
								where.append("OR " + FLR_ANGEBOTAUFTRAG + "flrkunde.flrpartner.c_kbez");
								where.append(" " + filterKriterien[i].operator);
								where.append(" " + filterKriterien[i].value);
							}
						} else {
							if (filterKriterien[i].isBIgnoreCase()) {
								where.append(" upper(" + FLR_ANGEBOTAUFTRAG + filterKriterien[i].kritName + ")");
							} else {
								where.append(" " + FLR_ANGEBOTAUFTRAG + filterKriterien[i].kritName);
							}

							where.append(" " + filterKriterien[i].operator);

							if (filterKriterien[i].isBIgnoreCase()) {
								where.append(" " + filterKriterien[i].value.toUpperCase());
							} else {
								where.append(" " + filterKriterien[i].value);
							}
						}
					} else if (filterKriterien[i].kritName.equals("c_suche")) {

						where.append(buildWhereClauseExtendedSearchWithoutDuplicates(FLR_AUFTRAG_TEXTSUCHE_CLASSNAME,
								FLR_ANGEBOTAUFTRAG, filterKriterien[i]));
					}

					else if (filterKriterien[i].kritName.equals("c_bez")) {
						where.append(" (");
						where.append(buildWhereClauseExtendedSearch(Arrays.asList(filterKriterien[i].value.split(" ")),
								FLR_ANGEBOTAUFTRAG + filterKriterien[i].kritName, filterKriterien[i].isBIgnoreCase()));
						where.append(" OR ");
						where.append(buildWhereClauseExtendedSearch(Arrays.asList(filterKriterien[i].value.split(" ")),
								FLR_ANGEBOTAUFTRAG + "c_bestellnummer", filterKriterien[i].isBIgnoreCase()));

						where.append(") ");
					} else {
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" lower(" + FLR_ANGEBOTAUFTRAG + filterKriterien[i].kritName + ")");
						} else {
							where.append(" " + FLR_ANGEBOTAUFTRAG + filterKriterien[i].kritName);
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
				orderBy.append(FLR_ANGEBOTAUFTRAG).append(AuftragFac.FLR_AUFTRAG_C_NR).append(" DESC ");
				sortAdded = true;
			}
			if (orderBy.indexOf(FLR_ANGEBOTAUFTRAG + AuftragFac.FLR_AUFTRAG_I_ID) < 0) {
				// Martin Werner original: "unique sort required because
				// otherwise rowNumber of selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt()."
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" ").append(FLR_ANGEBOTAUFTRAG).append(AuftragFac.FLR_AUFTRAG_I_ID).append(" DESC ");
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
		return "SELECT flrangebotauftrag from FLRAngebotauftrag  as flrangebotauftrag ";
	}

	public QueryResult sort(SortierKriterium[] sortierKriterien, Object selectedId) throws EJBExceptionLP {
		QueryResult result = null;

		try {
			int rowNumber = 0;

			getQuery().setSortKrit(sortierKriterien);

			if (selectedId != null && ((Integer) selectedId).intValue() >= 0) {
				SessionFactory factory = FLRSessionFactory.getFactory();
				Session session = null;

				try {

					session = factory.openSession();
					String queryString = "select " + FLR_ANGEBOTAUFTRAG + AuftragFac.FLR_AUFTRAG_I_ID
							+ FLR_ANGEBOTAUFTRAG_FROM_CLAUSE

							+ this.buildWhereClause() + this.buildOrderByClause();
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
				} finally {
					try {
						session.close();
					} catch (HibernateException he) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HIBERNATE, he);
					}
				}
			}

			if (rowNumber < 0 || rowNumber >= this.getRowCount()) {
				rowNumber = 0;
			}

			result = this.getPageAt(new Integer(rowNumber));
			result.setIndexOfSelectedRow(rowNumber);
		} catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		return result;
	}

	private void setupParameters() {
		try {
			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_ZUSATZSTATUS_VERRECHENBAR);
			verrechenbarStattRohsAnzeigen = (Boolean) parameter.getCWertAsObject();

			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(), ParameterFac.KATEGORIE_AUFTRAG,
					ParameterFac.PARAMETER_AUFTRAGSFREIGABE);
			bAuftragsfreigabe = ((Boolean) parameter.getCWertAsObject());

			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(), ParameterFac.KATEGORIE_AUFTRAG,
					ParameterFac.PARAMETER_AUFTRAGSPOSITIONSTERMIN_IN_AUSWAHL_ANZEIGEN);
			bAuftragspositionstermin_in_auswahl_anzeigen = ((Boolean) parameter.getCWertAsObject());

		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}
	}

	private TableColumnInformation createColumnInformation(String mandant, Locale locUi) {

		String sortierungNachKunde = Facade.NICHT_SORTIERBAR;
		String sortierungNachOrt = Facade.NICHT_SORTIERBAR;

		if (SORTIERUNG_UI_PARTNER_ORT) {
			sortierungNachKunde = FLR_ANGEBOTAUFTRAG + AuftragFac.FLR_AUFTRAG_FLRKUNDE + "." + KundeFac.FLR_PARTNER
					+ "." + PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1;

			sortierungNachOrt = // Sortierung fuers erste mal nach LKZ
					FLR_ANGEBOTAUFTRAG + AuftragFac.FLR_AUFTRAG_FLRKUNDE + "." + KundeFac.FLR_PARTNER + "."
							+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT + "." + SystemFac.FLR_LP_FLRLAND + "."
							+ SystemFac.FLR_LP_LANDLKZ + ", " +
							// und dann nach plz
							AngebotAuftragHandler.FLR_ANGEBOTAUFTRAG + AuftragFac.FLR_AUFTRAG_FLRKUNDE + "."
							+ KundeFac.FLR_PARTNER + "." + PartnerFac.FLR_PARTNER_FLRLANDPLZORT + "."
							+ SystemFac.FLR_LP_LANDPLZORTPLZ;
		}

		TableColumnInformation columns = new TableColumnInformation();

		columns.add("i_id", Integer.class, "i_id", QueryParameters.FLR_BREITE_SHARE_WITH_REST,
				FLR_ANGEBOTAUFTRAG + AuftragFac.FLR_AUFTRAG_I_ID);
		columns.add("art", String.class, " ", QueryParameters.FLR_BREITE_XXS, Facade.NICHT_SORTIERBAR);
		
		columns.add("lp.belegart", String.class, getTextRespectUISpr("lp.belegart", mandant, locUi),
				8, FLR_ANGEBOTAUFTRAG + "auftrag_i_id");
		columns.add("lp.belegnummer", String.class, getTextRespectUISpr("lp.belegnummer", mandant, locUi),
				QueryParameters.FLR_BREITE_M, FLR_ANGEBOTAUFTRAG + AuftragFac.FLR_AUFTRAG_C_NR);
		
		
		columns.add("lp.kunde", String.class, getTextRespectUISpr("lp.kunde", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, sortierungNachKunde);
		columns.add("lp.ort", String.class, getTextRespectUISpr("lp.ort", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, sortierungNachOrt);
		columns.add("auft.projektbestellnummer", String.class,
				getTextRespectUISpr("auft.projektbestellnummer", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST,
				FLR_ANGEBOTAUFTRAG + AuftragFac.FLR_AUFTRAG_C_BEZ_PROJEKTBEZEICHNUNG);

		String sortTermin = FLR_ANGEBOTAUFTRAG + "t_auftrag_liefertermin";

		if (bAuftragspositionstermin_in_auswahl_anzeigen) {
			sortTermin = "t_positionsliefertermin";

		}

		columns.add("lp.termin", java.util.Date.class, getTextRespectUISpr("lp.termin", mandant, locUi),
				QueryParameters.FLR_BREITE_M, sortTermin);
		columns.add("lp.datum", java.util.Date.class, getTextRespectUISpr("lp.datum", mandant, locUi),
				QueryParameters.FLR_BREITE_M, FLR_ANGEBOTAUFTRAG + AuftragFac.FLR_AUFTRAG_D_BELEGDATUM);

		
		columns.add("lp.nachfasstermin", Date.class, getTextRespectUISpr("lp.nachfasstermin", mandant, locUi),
				QueryParameters.FLR_BREITE_M,  FLR_ANGEBOTAUFTRAG+"t_angebot_nachfasstermin");
		
		String orderVertreter = "flrvertreter.c_kurzzeichen";

		columns.add("lp.vertreter", String.class, getTextRespectUISpr("lp.vertreter", mandant, locUi),
				QueryParameters.FLR_BREITE_XS, FLR_ANGEBOTAUFTRAG + orderVertreter);

		columns.add("lp.status", Icon.class, getTextRespectUISpr("lp.status", mandant, locUi),
				QueryParameters.FLR_BREITE_XS, FLR_ANGEBOTAUFTRAG + "status_c_nr");
		
		columns.add("angb.akquisestatus", Icon.class, getTextRespectUISpr("angb.akquisestatus", mandant, locUi),
				QueryParameters.FLR_BREITE_XS, FLR_ANGEBOTAUFTRAG+"flrangebot.flrakquisestatus.c_bez");
		
		columns.add("lp.wert", BigDecimal.class, getTextRespectUISpr("lp.wert", mandant, locUi),
				QueryParameters.FLR_BREITE_PREIS,
				FLR_ANGEBOTAUFTRAG + "n_wert");
		columns.add("waehrung", String.class, " ", QueryParameters.FLR_BREITE_WAEHRUNG,
				FLR_ANGEBOTAUFTRAG + "waehrung_c_nr");
		if (verrechenbarStattRohsAnzeigen) {
			columns.add("auft.verrechenbar", Boolean.class, getTextRespectUISpr("auft.verrechnen", mandant, locUi), 3,
					FLR_ANGEBOTAUFTRAG + "flrauftrag."+AuftragFac.FLR_AUFTRAG_T_VERRECHENBAR,
					getTextRespectUISpr("auft.kannabgerechnetwerden", mandant, locUi));
		} else {
			columns.add("detail.label.rohs", Boolean.class, getTextRespectUISpr("detail.label.rohs", mandant, locUi), 3,
					FLR_ANGEBOTAUFTRAG + "flrauftrag."+AuftragFac.FLR_AUFTRAG_B_ROHS);
		}

		columns.add("detail.label.unverbindlich", Boolean.class,
				getTextRespectUISpr("auftrag.unverbindlich.short", mandant, locUi), 1,
				FLR_ANGEBOTAUFTRAG + "flrauftrag."+AuftragFac.FLR_AUFTRAG_B_LIEFERTERMINUNVERBINDLICH,
				getTextRespectUISpr("auftrag.unverbindlich.tooltip", mandant, locUi));

		if (bAuftragsfreigabe) {
			columns.add("auft.freigabe", Boolean.class, getTextRespectUISpr("auft.freigabe", mandant, locUi), 3,
					FLR_ANGEBOTAUFTRAG + "flrauftrag."+AuftragFac.FLR_AUFTRAG_T_AUFTRAGSFREIGABE);
		}

		// SP5836 Kommentar
		columns.add("lp.kommentar", String.class, getTextRespectUISpr("lp.kommentar", mandant, locUi), 1,
				Facade.NICHT_SORTIERBAR);

		columns.add("Color", Color.class, "", 1, Facade.NICHT_SORTIERBAR);

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
		AuftragDto auftragDto = null;
		// KundeDto kundeDto = null;
		Integer partnerIId = null;
		try {
			auftragDto = getAuftragFac().auftragFindByPrimaryKey((Integer) key);
			partnerIId = getKundeFac().kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(), theClientDto)
					.getPartnerIId();
		} catch (EJBExceptionLP e) {

		}

		if (auftragDto != null) {
			// String sPath = new JcrPathResolver(theClientDto.getMandant())
			// .add(LocaleFac.BELEGART_AUFTRAG)
			// .add(LocaleFac.BELEGART_AUFTRAG)
			// .add(auftragDto.getCNr())
			// .asPath();
			DocPath docPath = new DocPath(new DocNodeAuftrag(auftragDto));

			// String sPath = JCRDocFac.HELIUMV_NODE + "/"
			// + theClientDto.getMandant() + "/"
			// + LocaleFac.BELEGART_AUFTRAG.trim() + "/"
			// + LocaleFac.BELEGART_AUFTRAG.trim() + "/"
			// + auftragDto.getCNr().replace("/", ".");

			return new PrintInfoDto(docPath, partnerIId, getSTable());
		} else {
			return null;
		}
	}

	public String getSTable() {
		return "AUFTRAG";
	}
}
