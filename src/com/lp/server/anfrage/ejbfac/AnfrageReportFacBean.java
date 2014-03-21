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
package com.lp.server.anfrage.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import com.lp.server.anfrage.fastlanereader.generated.FLRAnfrage;
import com.lp.server.anfrage.fastlanereader.generated.FLRAnfragepositionReport;
import com.lp.server.anfrage.fastlanereader.generated.FLRAnfragepositionlieferdatenReport;
import com.lp.server.anfrage.service.AnfrageDto;
import com.lp.server.anfrage.service.AnfrageFac;
import com.lp.server.anfrage.service.AnfrageReportFac;
import com.lp.server.anfrage.service.AnfrageServiceFac;
import com.lp.server.anfrage.service.AnfragepositionDto;
import com.lp.server.anfrage.service.AnfragepositionFac;
import com.lp.server.anfrage.service.AnfragetextDto;
import com.lp.server.anfrage.service.ReportAnfrageLieferdatenuebersichtDto;
import com.lp.server.anfrage.service.ReportAnfragelieferdatenuebersichtKriterienDto;
import com.lp.server.anfrage.service.ReportAnfragestatistikDto;
import com.lp.server.anfrage.service.ReportAnfragestatistikKriterienDto;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ArtklaDto;
import com.lp.server.artikel.service.HerstellerDto;
import com.lp.server.artikel.service.MaterialDto;
import com.lp.server.artikel.service.MaterialzuschlagDto;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MediastandardDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SpediteurDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.BelegPositionDruckIdentDto;
import com.lp.server.util.BelegPositionDruckTextbausteinDto;
import com.lp.server.util.LPReport;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.server.util.report.TimingInterceptor;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
@Interceptors(TimingInterceptor.class)
public class AnfrageReportFacBean extends LPReport implements AnfrageReportFac,
		JRDataSource {

	private String cAktuellerReport = null;
	private Object[][] data = null;

	public boolean next() throws JRException {
		index++;
		return (index < data.length);
	}

	public Object getFieldValue(JRField field) throws JRException {
		Object value = null;
		String fieldName = field.getName();
		if (cAktuellerReport.equals(AnfrageReportFac.REPORT_ANFRAGE)) {
			if ("Einheit".equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_ANFRAGE_EINHEIT];
			} else if ("Freiertext".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][AnfrageReportFac.REPORT_ANFRAGE_FREIERTEXT]);
			} else if ("Ident".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][AnfrageReportFac.REPORT_ANFRAGE_IDENT]);
			} else if ("Image".equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_ANFRAGE_IMAGE];
			} else if ("Leerzeile".equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_ANFRAGE_LEERZEILE];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_ANFRAGE_MENGE];
			} else if ("Position".equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_ANFRAGE_POSITION];
			} else if ("Positionsart".equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_ANFRAGE_POSITIONSART];
			} else if ("Richtpreis".equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_ANFRAGE_RICHTPREIS];
			} else if ("Seitenumbruch".equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_ANFRAGE_SEITENUMBRUCH];
			} else if ("Artikelnrlieferant".equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_ANFRAGE_ARTIKELNRLIEFERANT];
			} else if (F_IDENTNUMMER.equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_ANFRAGE_IDENTNUMMER];
			} else if (F_BEZEICHNUNG.equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_ANFRAGE_BEZEICHNUNG];
			} else if (F_KURZBEZEICHNUNG.equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_ANFRAGE_KURZBEZEICHNUNG];
			} else if (F_REFERENZNUMMER.equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_ANFRAGE_REFERENZNUMMER];
			} else if (F_ZUSATZBEZEICHNUNG2.equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_ANFRAGE_ARTIKELCZBEZ2];
			} else if (F_ARTIKELKOMMENTAR.equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_ANFRAGE_ARTIKELKOMMENTAR];
			} else if (F_ZUSATZBEZEICHNUNG.equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_ANFRAGE_ZUSATZBEZEICHNUNG];
			} else if (F_ARTIKEL_BAUFORM.equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_ANFRAGE_BAUFORM];
			} else if (F_ARTIKEL_VERPACKUNGSART.equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_ANFRAGE_VERPACKUNGSART];
			} else if (F_ARTIKEL_MATERIAL.equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_ANFRAGE_ARTIKEL_MATERIAL];
			} else if (F_ARTIKEL_MATERIALGEWICHT.equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_ANFRAGE_ARTIKEL_MATERIALGEWICHT];
			} else if (F_ARTIKEL_KURS_MATERIALZUSCHLAG.equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_ANFRAGE_ARTIKEL_KURS_MATERIALZUSCHLAG];
			} else if (F_ARTIKEL_DATUM_MATERIALZUSCHLAG.equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_ANFRAGE_ARTIKEL_DATUM_MATERIALZUSCHLAG];
			} else if (F_ARTIKEL_BREITE.equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_ANFRAGE_ARTIKEL_BREITE];
			} else if (F_ARTIKEL_HOEHE.equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_ANFRAGE_ARTIKEL_HOEHE];
			} else if (F_ARTIKEL_TIEFE.equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_ANFRAGE_ARTIKEL_TIEFE];
			} else if ("Artikelgruppe".equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_ANFRAGE_ARTIKELGRUPPE];
			} else if ("Artikelklasse".equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_ANFRAGE_ARTIKELKLASSE];
			} else if ("F_IDENT_TEXTEINGABE".equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_ANFRAGE_IDENT_TEXTEINGABE];
			} else if ("F_POSITIONSOBJEKT".equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_ANFRAGE_POSITIIONOBJEKT];
			} else if (F_HERSTELLER_NAME.equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_ANFRAGE_ARTIKEL_HERSTELLER_NAME];
			} else if ("F_ARTIKEL_HERSTELLER".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][AnfrageReportFac.REPORT_ANFRAGE_ARTIKEL_HERSTELLER]);
			} else if ("F_LIEFERANT_ARTIKEL_IDENTNUMMER".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][AnfrageReportFac.REPORT_ANFRAGE_LIEFERANT_ARTIKEL_IDENTNUMMER]);
			} else if ("F_LIEFERANT_ARTIKEL_BEZEICHNUNG".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][AnfrageReportFac.REPORT_ANFRAGE_LIEFERANT_ARTIKEL_BEZEICHNUNG]);
			}

		} else if (cAktuellerReport
				.equals(AnfrageReportFac.REPORT_ANFRAGESTATISTIK)) {
			if ("Angebotenemenge".equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_ANFRAGESTATISTIK_ANGEBOTENEMENGE];
			} else if ("Angebotenerpreis".equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_ANFRAGESTATISTIK_ANGEBOTENERPREIS];
			} else if ("Angefragtemenge".equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_ANFRAGESTATISTIK_ANGEFRAGTEMENGE];
			} else if ("Belegdatum".equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_ANFRAGESTATISTIK_BELEGDATUM];
			} else if ("Cnr".equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_ANFRAGESTATISTIK_CNR];
			} else if ("Kunde".equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_ANFRAGESTATISTIK_KUNDE];
			} else if ("Lieferzeit".equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_ANFRAGESTATISTIK_LIEFERZEIT];
			}
		} else if (cAktuellerReport
				.equals(AnfrageReportFac.REPORT_ANFRAGE_LIEFERDATENUEBERSICHT)) {
			if ("Artikelcnr".equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_LIEFERDATEN_ARTIKELCNR];
			} else if ("Liefermenge".equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_LIEFERDATEN_LIEFERMENGE];
			} else if ("Einheitcnr".equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_LIEFERDATEN_EINHEITCNR];
			} else if ("Artikelcbez".equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_LIEFERDATEN_ARTIKELCBEZ];
			} else if ("Anlieferpreis".equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_LIEFERDATEN_ANLIEFERPREIS];
			} else if ("Anlieferzeit".equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_LIEFERDATEN_ANLIEFERZEIT];
			} else if ("Lieferantname".equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_LIEFERDATEN_LIEFERANTNAME];
			} else if ("Anfragecnr".equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_LIEFERDATEN_ANFRAGECNR];
			} else if ("F_ANFRAGECBEZ".equals(fieldName)) {
				value = data[index][AnfrageReportFac.REPORT_LIEFERDATEN_ANFRAGECBEZ];
			}
		}
		return value;
	}

	/*
	 * 
	 * private JasperPrintLP printAnfrageAlle(ReportJournalKriterienDto krit,
	 * boolean bNurOffene, String idUser) throws EJBExceptionLP { Session
	 * session = null; try {
	 * 
	 * TheClientDto theClientDto = super.check(idUser);
	 * 
	 * this.useCase = UC_REPORT_ALLE;
	 * 
	 * this.index = -1; SessionFactory factory = FLRSessionFactory.getFactory();
	 * session = factory.openSession(); Criteria c =
	 * session.createCriteria(FLRAnfrage.class);
	 * c.add(Restrictions.eq(AnfrageFac.FLR_ANFRAGE_MANDANT_C_NR,
	 * theClientDto.getMandant())); // Filter nach Kostenstelle if
	 * (krit.kostenstelleIId != null) {
	 * c.add(Restrictions.eq(RechnungFac.FLR_RECHNUNG_KOSTENSTELLE_I_ID,
	 * krit.kostenstelleIId)); } // Filter nach einem Kunden if (krit.kundeIId
	 * != null) { c.add(Restrictions.eq(RechnungFac.FLR_RECHNUNG_KUNDE_I_ID,
	 * krit.kundeIId)); } // Filter nach einem Vertrter if (krit.vertreterIId !=
	 * null) { c.add(Restrictions.eq(RechnungFac.FLR_RECHNUNG_FLRVERTRETER +
	 * ".i_id", krit.vertreterIId)); } // Filter nach Status (keine stornierten)
	 * c.add(Restrictions.not(
	 * Restrictions.eq(RechnungFac.FLR_RECHNUNG_STATUS_C_NR,
	 * RechnungFac.STATUS_STORNIERT))); // Nur offene anzeigen? if (bNurOffene)
	 * { Collection cStati = new LinkedList();
	 * cStati.add(RechnungFac.STATUS_ANGELEGT);
	 * cStati.add(RechnungFac.STATUS_OFFEN);
	 * cStati.add(RechnungFac.STATUS_TEILBEZAHLT);
	 * cStati.add(RechnungFac.STATUS_VERBUCHT);
	 * c.add(Restrictions.in(RechnungFac.FLR_RECHNUNG_STATUS_C_NR, cStati)); }
	 * // Datum von/bis String sVon = null; String sBis = null; if (krit.dVon !=
	 * null) { c.add(Restrictions.ge(RechnungFac.FLR_RECHNUNG_D_BELEGDATUM,
	 * krit.dVon)); sVon = Helper.formatDatum(krit.dVon,
	 * theClientDto.getLocUi()); } if (krit.dBis != null) {
	 * c.add(Restrictions.le(RechnungFac.FLR_RECHNUNG_D_BELEGDATUM, krit.dBis));
	 * sBis = Helper.formatDatum(krit.dBis, theClientDto.getLocUi()); }
	 * 
	 * // Belegnummer von/bis LpBelegnummerFormat f =
	 * getBelegnummerGeneratorObj().getBelegnummernFormat(
	 * theClientDto.getMandant()); Integer iGeschaeftsjahr =
	 * getParameterFac().getGeschaeftsjahr(theClientDto. getMandant()); if
	 * (krit.sBelegnummerVon != null) { sVon =
	 * HelperServer.getBelegnummernFilterForHibernateCriterias(f,
	 * iGeschaeftsjahr, krit.sBelegnummerVon);
	 * c.add(Restrictions.ge(RechnungFac.FLR_RECHNUNG_C_NR, sVon)); } if
	 * (krit.sBelegnummerBis != null) { sBis =
	 * HelperServer.getBelegnummernFilterForHibernateCriterias(f,
	 * iGeschaeftsjahr, krit.sBelegnummerBis);
	 * c.add(Restrictions.le(RechnungFac.FLR_RECHNUNG_C_NR, sBis)); } //
	 * Sortierung nach Kostenstelle ist optional if
	 * (krit.bSortiereNachKostenstelle) {
	 * c.createCriteria(RechnungFac.FLR_RECHNUNG_FLRKOSTENSTELLE
	 * ).addOrder(Order.asc( "c_nr")); } // Sortierung nach Partner ist optional
	 * if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER)
	 * {
	 * c.createCriteria(RechnungFac.FLR_RECHNUNG_FLRKUNDE).createCriteria(KundeFac
	 * . FLR_PARTNER).addOrder(Order.asc(PartnerFac.
	 * FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1)); } else if (krit.iSortierung ==
	 * ReportJournalKriterienDto.KRIT_SORT_NACH_BELEGNUMMER) {
	 * c.addOrder(Order.asc(RechnungFac.FLR_RECHNUNG_C_NR)); } if
	 * (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_VERTRETER)
	 * { c.createCriteria(RechnungFac.FLR_RECHNUNG_FLRVERTRETER).addOrder(Order.
	 * asc(PersonalFac. FLR_PERSONAL_C_KURZZEICHEN)); } java.util.List list =
	 * c.list(); data = new Object[list.size()][REPORT_ALLE_ANZAHL_SPALTEN];
	 * 
	 * // Map mapZahlungsziele =
	 * getMandantFac().zahlungszielFindAllByMandantAsDto
	 * (theClientDto.getMandant(), // theClientDto.getIDUser());
	 * 
	 * int i = 0; for (Iterator iter = list.iterator(); iter.hasNext(); ) {
	 * FLRRechnungReport r = (FLRRechnungReport) iter.next();
	 * data[i][FELD_ALLE_BELEGDATUM] = r.getD_belegdatum();
	 * data[i][FELD_ALLE_BETRAG] = r.getN_wert(); data[i][FELD_ALLE_BETRAGUST] =
	 * r.getN_wertust(); // Bezahlte Betraege BigDecimal bdBezahlt =
	 * getRechnungFac().getBereitsBezahltWertVonRechnung(r. getI_id(), null);
	 * data[i][FELD_ALLE_BEZAHLT] = bdBezahlt; BigDecimal bdBezahltUst =
	 * getRechnungFac().getBereitsBezahltWertVonRechnungUst(r. getI_id(), null);
	 * data[i][FELD_ALLE_BEZAHLTUST] = bdBezahltUst; // Kostenstelle if
	 * (r.getFlrkostenstelle() != null) { data[i][FELD_ALLE_KOSTENSTELLENUMMER]
	 * = r.getFlrkostenstelle().getC_nr(); } else {
	 * data[i][FELD_ALLE_KOSTENSTELLENUMMER] = null; } // Kundendaten
	 * data[i][FELD_ALLE_KUNDE] = r.getFlrkunde().getFlrpartner().
	 * getC_name1nachnamefirmazeile1(); if(r.getFlrvertreter() != null){ if (
	 * r.getFlrvertreter().getFlrpartner(). getC_name2vornamefirmazeile2()!=
	 * null) { data[i][FELD_ALLE_VERTRETER] =
	 * r.getFlrvertreter().getFlrpartner(). getC_name1nachnamefirmazeile1() +
	 * " " + r.getFlrvertreter().getFlrpartner().
	 * getC_name2vornamefirmazeile2(); }else{ data[i][FELD_ALLE_VERTRETER] =
	 * r.getFlrvertreter().getFlrpartner(). getC_name1nachnamefirmazeile1(); } }
	 * if (r.getFlrkunde().getFlrpartner().getC_name2vornamefirmazeile2() !=
	 * null) { data[i][FELD_ALLE_KUNDE2] = r.getFlrkunde().getFlrpartner().
	 * getC_name2vornamefirmazeile2(); } else { data[i][FELD_ALLE_KUNDE2] = "";
	 * } PartnerDto partnerDto =
	 * getPartnerFac().partnerFindByPrimaryKey(r.getFlrkunde().
	 * getFlrpartner().getI_id(), idUser); data[i][FELD_ALLE_ADRESSE] =
	 * partnerDto.formatAdresse(); if (r.getMahnstufe_i_id() != null) {
	 * data[i][FELD_ALLE_MAHNSTUFE] = r.getMahnstufe_i_id(); MahnstufeDto
	 * mahnstufeDto = getMahnwesenFac().mahnstufeFindByPrimaryKey(new
	 * MahnstufePK(r.getMahnstufe_i_id(), r.getMandant_c_nr())); // Zinsen nur
	 * fuer noch nicht vollstaendig bezahlte if
	 * (!r.getStatus_c_nr().equals(RechnungFac.STATUS_BEZAHLT)) { if
	 * (mahnstufeDto.getFZinssatz() != null) { BigDecimal bdOffen =
	 * r.getN_wert().subtract(bdBezahlt); // Zinsen data[i][FELD_ALLE_ZINSEN] =
	 * bdOffen.multiply(new BigDecimal(mahnstufeDto.
	 * getFZinssatz().floatValue())).movePointLeft(2); } } }
	 * data[i][FELD_ALLE_ORT] = partnerDto.formatAdresse();
	 * data[i][FELD_ALLE_RECHNUNGSNUMMER] = r.getC_nr(); if
	 * (r.getZahlungsziel_i_id() != null) { data[i][FELD_ALLE_ZIELDATUM] =
	 * getMandantFac().berechneZielDatumFuerBelegdatum( r.getD_belegdatum(),
	 * r.getZahlungsziel_i_id(), idUser); } // Bezahlt if (r.getT_bezahltdatum()
	 * != null) { data[i][FELD_ALLE_BEZAHLTDATUM] = r.getT_bezahltdatum(); //
	 * Zahltage int iZahltage = Helper.getDifferenzInTagen(r.getD_belegdatum(),
	 * r.getT_bezahltdatum()); data[i][FELD_ALLE_ZAHLTAGE] = new
	 * Integer(iZahltage); } else { RechnungzahlungDto[] zahlungen =
	 * getRechnungFac().zahlungFindByRechnungIId(r. getI_id()); // Wechsel for
	 * (int j = 0; j < zahlungen.length; j++) { if
	 * (zahlungen[j].getZahlungsartCNr
	 * ().equals(RechnungFac.ZAHLUNGSART_WECHSEL)) {
	 * data[i][FELD_ALLE_WECHSELDATUM] = zahlungen[j].getDWechselFaelligAm(); }
	 * } } // Debitorenkontonummer KontoDtoSmall kontoDtoDeb = null; if
	 * (r.getFlrkunde().getKonto_i_id_debitorenkonto() != null) { kontoDtoDeb =
	 * getFinanzFac().kontoFindByPrimaryKeySmallOhneExc(r.getFlrkunde().
	 * getKonto_i_id_debitorenkonto()); } String sKontonummer; if (kontoDtoDeb
	 * != null) { sKontonummer = kontoDtoDeb.getCNr(); } else { sKontonummer =
	 * ""; } data[i][FELD_ALLE_DEBITORENKONTO] = sKontonummer; i++; }
	 * 
	 * Map<String, Object> mapParameter = new TreeMap<String, Object> ();
	 * MandantDto mandantDto =
	 * getMandantFac().mandantFindByPrimaryKey(theClientDto. getMandant(),
	 * idUser); // Waehrung mapParameter.put(LPReport.P_WAEHRUNG,
	 * mandantDto.getWaehrungCNr()); StringBuffer sSortierung = new
	 * StringBuffer(); if (krit.bSortiereNachKostenstelle) {
	 * sSortierung.append(getTextRespectUISpr("lp.kostenstelle",
	 * theClientDto.getMandant(), theClientDto.getLocUi()));
	 * sSortierung.append(", "); } if (krit.iSortierung ==
	 * krit.KRIT_SORT_NACH_PARTNER) {
	 * sSortierung.append(getTextRespectUISpr("lp.kunde",
	 * theClientDto.getMandant(), theClientDto.getLocUi())); } else if
	 * (krit.iSortierung == krit.KRIT_SORT_NACH_BELEGNUMMER) {
	 * sSortierung.append(getTextRespectUISpr("bes.bestnr",
	 * theClientDto.getMandant(), theClientDto.getLocUi())); } else if
	 * (krit.iSortierung == krit.KRIT_SORT_NACH_VERTRETER) {
	 * sSortierung.append(getTextRespectUISpr("lp.vertreter",
	 * theClientDto.getMandant(), theClientDto.getLocUi())); } StringBuffer
	 * sFilter = new StringBuffer(); if (sVon != null) {
	 * sFilter.append(getTextRespectUISpr("lp.von", theClientDto.getMandant(),
	 * theClientDto.getLocUi())); sFilter.append(" " + sVon + " "); } if (sBis
	 * != null) { sFilter.append(getTextRespectUISpr("lp.bis",
	 * theClientDto.getMandant(), theClientDto.getLocUi())); sFilter.append(" "
	 * + sBis + " "); } if (krit.kostenstelleIId != null) { if (sFilter.length()
	 * > 0) { sFilter.append(", "); } KostenstelleDto kstDto =
	 * getSystemFac().kostenstelleFindByPrimaryKey(krit. kostenstelleIId);
	 * sFilter.append(getTextRespectUISpr("lp.kostenstelle",
	 * theClientDto.getMandant(), theClientDto.getLocUi()));
	 * sFilter.append(" "); sFilter.append(kstDto.getCNr()); }
	 * mapParameter.put(LPReport.P_SORTIERUNG, sSortierung.toString());
	 * mapParameter.put(LPReport.P_SORTIERENACHKOSTENSTELLE, new
	 * Boolean(krit.bSortiereNachKostenstelle));
	 * mapParameter.put(LPReport.P_SORTIERENACHKUNDE, new
	 * Boolean(krit.iSortierung ==
	 * ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER));
	 * mapParameter.put(LPReport.P_SORTIERENACHVERTRETER, new
	 * Boolean(krit.iSortierung ==
	 * ReportJournalKriterienDto.KRIT_SORT_NACH_VERTRETER));
	 * mapParameter.put(LPReport.P_FILTER, sFilter.toString());
	 * 
	 * initJRDS(mapParameter, AnfrageReportFac.REPORT_MODUL,
	 * AnfrageReportFac.REPORT_ANFRAGE, theClientDto.getMandant(),
	 * theClientDto.getLocUi(), idUser);
	 * 
	 * return getReportPrint(); } catch (FinderException ex) { throw new
	 * EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, ex); } catch
	 * (RemoteException ex) { throwEJBExceptionLPRespectOld(ex); return null; }
	 * finally { if (session != null) { session.close(); } } }
	 */

	/**
	 * Eine Anfrage drucken.
	 * 
	 * @param iIdAnfrageI
	 *            PK der Anfrage
	 * @param iAnzahlKopienI
	 *            wieviele Kopien zusaetzlich zum Original?
	 * @param bMitLogo
	 *            Boolean
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return JasperPrint[] der Druck und seine Kopien
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public JasperPrintLP[] printAnfrage(Integer iIdAnfrageI,
			Integer iAnzahlKopienI, Boolean bMitLogo, TheClientDto theClientDto)
			throws EJBExceptionLP {
		checkAnfrageIId(iIdAnfrageI);

		JasperPrintLP[] aJasperPrint = null;

		try {
			AnfrageDto anfrageDto = getAnfrageFac().anfrageFindByPrimaryKey(
					iIdAnfrageI, theClientDto);

			AnfragepositionDto[] aAnfragepositionDto = getAnfragepositionFac()
					.anfragepositionFindByAnfrage(iIdAnfrageI, theClientDto);

			cAktuellerReport = AnfrageReportFac.REPORT_ANFRAGE;
			Boolean bbSeitenumbruch = new Boolean(false); // dieser Wert
			// wechselt mit
			// jedem
			// Seitenumbruch
			// zwischen true und
			// false

			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);

			Locale localeCNrDruck = theClientDto.getLocUi();

			LieferantDto lieferantDto = null;
			Integer partnerIId = null;
			AnsprechpartnerDto ansprechpartnerDto = null;
			String cAdresseFuerAusdruck = null;
			HashMap<String, Object> parameter = new HashMap<String, Object>();

			if (anfrageDto.getArtCNr().equals(
					AnfrageServiceFac.ANFRAGEART_LIEFERANT)) {
				lieferantDto = getLieferantFac().lieferantFindByPrimaryKey(
						anfrageDto.getLieferantIIdAnfrageadresse(),
						theClientDto);
				partnerIId = lieferantDto.getPartnerIId();
				localeCNrDruck = Helper.string2Locale(lieferantDto
						.getPartnerDto().getLocaleCNrKommunikation());

				if (anfrageDto.getAnsprechpartnerIIdLieferant() != null) {
					ansprechpartnerDto = getAnsprechpartnerFac()
							.ansprechpartnerFindByPrimaryKey(
									anfrageDto.getAnsprechpartnerIIdLieferant(),
									theClientDto);
				}

				cAdresseFuerAusdruck = formatAdresseFuerAusdruck(
						lieferantDto.getPartnerDto(), ansprechpartnerDto,
						mandantDto, localeCNrDruck, LocaleFac.BELEGART_ANFRAGE);

				if (ansprechpartnerDto != null) {
					parameter
							.put("P_ANSPRECHPARTNER_KUNDE_ADRESSBLOCK",
									getPartnerFac()
											.formatFixAnredeTitelName2Name1FuerAdresskopf(
													ansprechpartnerDto
															.getPartnerDto(),
													localeCNrDruck, null));
				}

			}

			int iAnzahlZeilen = aAnfragepositionDto.length; // Anzahl der Zeilen
			// in der Gruppe
			data = new Object[iAnzahlZeilen][AnfrageReportFac.REPORT_ANFRAGE_ANZAHL_SPALTEN];

			// die Datenmatrix befuellen
			for (int i = 0; i < iAnzahlZeilen; i++) {
				data[i][AnfrageReportFac.REPORT_ANFRAGE_POSITIIONOBJEKT] = getSystemReportFac()
						.getPositionForReport(LocaleFac.BELEGART_ANFRAGE,
								aAnfragepositionDto[i].getIId(), theClientDto);
				data[i][AnfrageReportFac.REPORT_ANFRAGE_POSITION] = getAnfragepositionFac()
						.getPositionNummer(aAnfragepositionDto[i].getIId(),
								theClientDto);
				// Artikelpositionen
				if (aAnfragepositionDto[i].getPositionsartCNr().equals(
						AnfrageServiceFac.ANFRAGEPOSITIONART_IDENT)
						|| aAnfragepositionDto[i]
								.getPositionsartCNr()
								.equals(AnfrageServiceFac.ANFRAGEPOSITIONART_HANDEINGABE)) {

					ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKey(
									aAnfragepositionDto[i].getArtikelIId(),
									theClientDto);

					// Druckdaten zusammenstellen
					BelegPositionDruckIdentDto druckDto = printIdent(
							aAnfragepositionDto[i], LocaleFac.BELEGART_ANFRAGE,
							artikelDto, localeCNrDruck, partnerIId,
							theClientDto);

					data[i][AnfrageReportFac.REPORT_ANFRAGE_ARTIKELCZBEZ2] = druckDto
							.getSArtikelZusatzBezeichnung2();
					data[i][AnfrageReportFac.REPORT_ANFRAGE_IMAGE] = druckDto
							.getOImageKommentar();
					data[i][AnfrageReportFac.REPORT_ANFRAGE_ARTIKELKOMMENTAR] = druckDto
							.getSArtikelkommentar();
					data[i][AnfrageReportFac.REPORT_ANFRAGE_IDENT] = druckDto
							.getSArtikelInfo();
					data[i][AnfrageReportFac.REPORT_ANFRAGE_IDENT_TEXTEINGABE] = druckDto
							.getSIdentTexteingabe();

					if (artikelDto.getArtgruIId() != null) {

						ArtgruDto artgruDto = getArtikelFac()
								.artgruFindByPrimaryKey(
										artikelDto.getArtgruIId(), theClientDto);

						data[i][AnfrageReportFac.REPORT_ANFRAGE_ARTIKELGRUPPE] = artgruDto
								.getBezeichnung();
					}
					if (artikelDto.getArtklaIId() != null) {

						ArtklaDto artklaDto = getArtikelFac()
								.artklaFindByPrimaryKey(
										artikelDto.getArtklaIId(), theClientDto);

						data[i][AnfrageReportFac.REPORT_ANFRAGE_ARTIKELKLASSE] = artklaDto
								.getBezeichnung();
					}
					data[i][AnfrageReportFac.REPORT_ANFRAGE_IDENTNUMMER] = druckDto
							.getSIdentnummer();
					data[i][AnfrageReportFac.REPORT_ANFRAGE_BEZEICHNUNG] = druckDto
							.getSBezeichnung();
					data[i][AnfrageReportFac.REPORT_ANFRAGE_ZUSATZBEZEICHNUNG] = druckDto
							.getSZusatzBezeichnung();
					data[i][AnfrageReportFac.REPORT_ANFRAGE_KURZBEZEICHNUNG] = druckDto
							.getSKurzbezeichnung();
					data[i][AnfrageReportFac.REPORT_ANFRAGE_REFERENZNUMMER] = druckDto
							.getSReferenznr();

					if (aAnfragepositionDto[i].getPositionsartCNr().equals(
							AnfrageServiceFac.ANFRAGEPOSITIONART_IDENT)) {

						if (artikelDto.getVerpackungDto() != null) {
							data[i][AnfrageReportFac.REPORT_ANFRAGE_BAUFORM] = artikelDto
									.getVerpackungDto().getCBauform();
							data[i][AnfrageReportFac.REPORT_ANFRAGE_VERPACKUNGSART] = artikelDto
									.getVerpackungDto().getCVerpackungsart();
						}
						if (artikelDto.getMaterialIId() != null) {
							MaterialDto materialDto = getMaterialFac()
									.materialFindByPrimaryKey(
											artikelDto.getMaterialIId(),
											theClientDto);
							if (materialDto.getMaterialsprDto() != null) {
								/**
								 * @todo MR->MR richtige Mehrsprachigkeit:
								 *       Material in Drucksprache.
								 */
								data[i][AnfrageReportFac.REPORT_ANFRAGE_ARTIKEL_MATERIAL] = materialDto
										.getMaterialsprDto().getCBez();
							} else {
								data[i][AnfrageReportFac.REPORT_ANFRAGE_ARTIKEL_MATERIAL] = materialDto
										.getCNr();
							}

							MaterialzuschlagDto mzDto = getMaterialFac()
									.getKursMaterialzuschlagDtoInZielwaehrung(
											artikelDto.getMaterialIId(),
											anfrageDto.getTBelegdatum(),
											anfrageDto.getWaehrungCNr(),
											theClientDto);

							data[i][AnfrageReportFac.REPORT_ANFRAGE_ARTIKEL_KURS_MATERIALZUSCHLAG] = mzDto
									.getNZuschlag();
							data[i][AnfrageReportFac.REPORT_ANFRAGE_ARTIKEL_DATUM_MATERIALZUSCHLAG] = mzDto
									.getTGueltigab();

						}

						data[i][AnfrageReportFac.REPORT_ANFRAGE_ARTIKEL_MATERIALGEWICHT] = artikelDto
								.getFMaterialgewicht();

						if (artikelDto.getGeometrieDto() != null) {
							data[i][AnfrageReportFac.REPORT_ANFRAGE_ARTIKEL_BREITE] = artikelDto
									.getGeometrieDto().getFBreite();
							data[i][AnfrageReportFac.REPORT_ANFRAGE_ARTIKEL_HOEHE] = artikelDto
									.getGeometrieDto().getFHoehe();
							data[i][AnfrageReportFac.REPORT_ANFRAGE_ARTIKEL_TIEFE] = artikelDto
									.getGeometrieDto().getFTiefe();
						}

						// Lieferantendaten: Artikelnummer, Bezeichnung
						ArtikellieferantDto artikellieferantDto = getArtikelFac()
								.artikellieferantFindByArtikellIIdLieferantIIdOhneExc(
										artikelDto.getIId(),
										anfrageDto
												.getLieferantIIdAnfrageadresse(),
										theClientDto);

						if (artikellieferantDto != null) {

							if (Helper.short2boolean(artikellieferantDto
									.getBHerstellerbez()) == true) {
								// PJ18293

								data[i][AnfrageReportFac.REPORT_ANFRAGE_LIEFERANT_ARTIKEL_IDENTNUMMER] = artikelDto
										.getCArtikelnrhersteller();
								data[i][AnfrageReportFac.REPORT_ANFRAGE_ARTIKELNRLIEFERANT] = artikelDto
										.getCArtikelnrhersteller();

								data[i][AnfrageReportFac.REPORT_ANFRAGE_LIEFERANT_ARTIKEL_BEZEICHNUNG] = artikelDto
										.getCArtikelbezhersteller();

							} else {
								if (artikellieferantDto
										.getCArtikelnrlieferant() != null) {
									data[i][AnfrageReportFac.REPORT_ANFRAGE_LIEFERANT_ARTIKEL_IDENTNUMMER] = artikellieferantDto
											.getCArtikelnrlieferant();
									data[i][AnfrageReportFac.REPORT_ANFRAGE_ARTIKELNRLIEFERANT] = artikellieferantDto
											.getCArtikelnrlieferant();
								}
								if (artikellieferantDto.getCBezbeilieferant() != null) {
									data[i][AnfrageReportFac.REPORT_ANFRAGE_LIEFERANT_ARTIKEL_BEZEICHNUNG] = artikellieferantDto
											.getCBezbeilieferant();
								}
							}

						}

						if (artikelDto.getHerstellerIId() != null) {
							HerstellerDto herstellerDto = getArtikelFac()
									.herstellerFindByPrimaryKey(
											artikelDto.getHerstellerIId(),
											theClientDto);
							data[i][AnfrageReportFac.REPORT_ANFRAGE_ARTIKEL_HERSTELLER] = herstellerDto
									.getCNr();
							PartnerDto partnerDto = getPartnerFac()
									.partnerFindByPrimaryKey(
											herstellerDto.getPartnerIId(),
											theClientDto);
							data[i][AnfrageReportFac.REPORT_ANFRAGE_ARTIKEL_HERSTELLER_NAME] = partnerDto
									.formatFixName1Name2();
						}

					}

					data[i][AnfrageReportFac.REPORT_ANFRAGE_MENGE] = aAnfragepositionDto[i]
							.getNMenge();
					data[i][AnfrageReportFac.REPORT_ANFRAGE_EINHEIT] = aAnfragepositionDto[i]
							.getEinheitCNr() == null ? "" : getSystemFac()
							.formatEinheit(
									aAnfragepositionDto[i].getEinheitCNr(),
									localeCNrDruck, theClientDto);

					// Richtpreis nur andrucken, wenn er nicht null ist
					BigDecimal nRichtpreis = null;
					if (aAnfragepositionDto[i].getNRichtpreis() != null
							&& aAnfragepositionDto[i].getNRichtpreis()
									.doubleValue() != 0) {
						nRichtpreis = aAnfragepositionDto[i].getNRichtpreis();
					}
					data[i][AnfrageReportFac.REPORT_ANFRAGE_RICHTPREIS] = nRichtpreis;
				}

				// Betrifft Positionen
				if (aAnfragepositionDto[i].getPositionsartCNr().equals(
						AnfrageServiceFac.ANFRAGEPOSITIONART_BETRIFFT)) {
					data[i][AnfrageReportFac.REPORT_ANFRAGE_FREIERTEXT] = aAnfragepositionDto[i]
							.getCBez();
				}

				// Texteingabe Positionen
				if (aAnfragepositionDto[i].getPositionsartCNr().equals(
						AnfrageServiceFac.ANFRAGEPOSITIONART_TEXTEINGABE)) {
					// IMS 1619 leerer Text soll als Leerezeile erscheinen
					String sText = aAnfragepositionDto[i].getXTextinhalt();

					if (sText != null && sText.trim().equals("")) {
						data[i][AnfrageReportFac.REPORT_ANFRAGE_LEERZEILE] = " ";
					} else {
						data[i][AnfrageReportFac.REPORT_ANFRAGE_FREIERTEXT] = sText;
					}
				}

				// Textbaustein Positionen
				if (aAnfragepositionDto[i].getPositionsartCNr().equals(
						AnfrageServiceFac.ANFRAGEPOSITIONART_TEXTBAUSTEIN)) {
					// Dto holen
					MediastandardDto oMediastandardDto = getMediaFac()
							.mediastandardFindByPrimaryKey(
									aAnfragepositionDto[i]
											.getMediastandardIId());
					// zum Drucken vorbereiten
					BelegPositionDruckTextbausteinDto druckDto = printTextbaustein(
							oMediastandardDto, theClientDto);
					data[i][AnfrageReportFac.REPORT_ANFRAGE_FREIERTEXT] = druckDto
							.getSFreierText();
					data[i][AnfrageReportFac.REPORT_ANFRAGE_IMAGE] = druckDto
							.getOImage();
				}

				// Leerzeile Positionen
				if (aAnfragepositionDto[i].getPositionsartCNr().equals(
						AnfrageServiceFac.ANFRAGEPOSITIONART_LEERZEILE)) {
					data[i][AnfrageReportFac.REPORT_ANFRAGE_LEERZEILE] = " ";
				}

				// Seitenumbruch Positionen
				if (aAnfragepositionDto[i].getPositionsartCNr().equals(
						AnfrageServiceFac.ANFRAGEPOSITIONART_SEITENUMBRUCH)) {
					bbSeitenumbruch = new Boolean(
							!bbSeitenumbruch.booleanValue()); // toggle
				}
				data[i][AnfrageReportFac.REPORT_ANFRAGE_SEITENUMBRUCH] = bbSeitenumbruch;

				data[i][AnfrageReportFac.REPORT_ANFRAGE_POSITIONSART] = aAnfragepositionDto[i]
						.getPositionsartCNr();
			}
			String localeCNr = null;
			if (lieferantDto != null) {
				localeCNr = lieferantDto.getPartnerDto()
						.getLocaleCNrKommunikation();
			} else {
				localeCNr = mandantDto.getPartnerDto()
						.getLocaleCNrKommunikation();
			}

			// Kopftext
			String sKopftext = anfrageDto.getXKopftextuebersteuert();

			if (sKopftext == null || sKopftext.length() == 0) {
				AnfragetextDto anfragetextDto = getAnfrageServiceFac()
						.anfragetextFindByMandantLocaleCNr(localeCNr,
								AnfrageServiceFac.ANFRAGETEXT_KOPFTEXT,
								theClientDto);

				sKopftext = anfragetextDto.getXTextinhalt();
			}

			// Fusstext
			String sFusstext = anfrageDto.getXFusstextuebersteuert();

			if (sFusstext == null || sFusstext.length() == 0) {
				AnfragetextDto anfragetextDto = getAnfrageServiceFac()
						.anfragetextFindByMandantLocaleCNr(localeCNr,
								AnfrageServiceFac.ANFRAGETEXT_FUSSTEXT,
								theClientDto);

				sFusstext = anfragetextDto.getXTextinhalt();
			}

			// Erstellung des Report

			// fuer die Liefergruppenanfrage wird die Liste der Lieferanten der
			// LG uebergeben
			if (anfrageDto.getArtCNr().equals(
					AnfrageServiceFac.ANFRAGEART_LIEFERGRUPPE)) {
				LieferantDto[] aLieferantDto = getLieferantFac()
						.lieferantFindByLiefergruppeIId(
								anfrageDto.getLiefergruppeIId(), theClientDto);

				StringBuffer buff = new StringBuffer();
				buff.append("An: ");

				for (int i = 0; i < aLieferantDto.length; i++) {
					buff.append(aLieferantDto[i].getPartnerDto()
							.formatFixName2Name1());
					if (i < aLieferantDto.length - 1) {
						buff.append(", ");
					}
				}

				parameter.put("P_LIEFERANTEN", buff.toString());
			}

			parameter.put("P_ANLIEFERTERMIN", anfrageDto.getTAnliefertermin());

			// CK: PJ 13849
			parameter.put(
					"P_BEARBEITER",
					getPersonalFac().getPersonRpt(
							anfrageDto.getPersonalIIdAnlegen(), theClientDto));

			parameter.put("P_MANDANTADRESSE",
					Helper.formatMandantAdresse(mandantDto));
			parameter.put("P_KUNDE_ADRESSBLOCK", cAdresseFuerAusdruck);
			parameter.put("P_ANFRAGEART", anfrageDto.getArtCNr());
			parameter.put("Waehrung", anfrageDto.getWaehrungCNr());
			parameter.put("Lieferantuid", lieferantDto == null ? null
					: lieferantDto.getPartnerDto().getCUid());
			parameter.put("LieferantEori", lieferantDto == null ? null
					: lieferantDto.getPartnerDto().getCEori());
			parameter.put(
					"Belegkennung",
					getAnfrageFac().getAnfragekennung(iIdAnfrageI,
							localeCNrDruck, theClientDto));
			parameter.put("Projekt", anfrageDto.getCBez());

			if (anfrageDto.getProjektIId() != null) {
				ProjektDto pjDto = getProjektFac().projektFindByPrimaryKey(
						anfrageDto.getProjektIId());
				parameter.put("P_PROJEKTNUMMER", pjDto.getCNr());
			}

			if (anfrageDto.getKostenstelleIId() != null) {
				KostenstelleDto kostenstelleDto = getSystemFac()
						.kostenstelleFindByPrimaryKey(
								anfrageDto.getKostenstelleIId());
				parameter
						.put(LPReport.P_KOSTENSTELLE, kostenstelleDto.getCNr());
			}

			String cBriefanrede = "";

			if (ansprechpartnerDto != null) {
				cBriefanrede = getPartnerServicesFac().getBriefanredeFuerBeleg(
						anfrageDto.getAnsprechpartnerIIdLieferant(),
						lieferantDto.getPartnerIId(), localeCNrDruck,
						theClientDto);
			} else {
				// neutrale Anrede

				if (lieferantDto != null) {
					cBriefanrede = getBriefanredeNeutralOderPrivatperson(
							lieferantDto.getPartnerIId(), localeCNrDruck,
							theClientDto);
				} else {
					cBriefanrede = getTextRespectUISpr(
							"lp.anrede.sehrgeehrtedamenundherren",
							theClientDto.getMandant(), localeCNrDruck);
				}

			}

			parameter.put("Briefanrede", cBriefanrede);

			PersonalDto oPersonalBenutzer = getPersonalFac()
					.personalFindByPrimaryKey(theClientDto.getIDPersonal(),
							theClientDto);
			PersonalDto oPersonalAnleger = getPersonalFac()
					.personalFindByPrimaryKey(
							anfrageDto.getPersonalIIdAnlegen(), theClientDto);
			parameter.put("Unserzeichen", Helper.getKurzzeichenkombi(
					oPersonalBenutzer.getCKurzzeichen(),
					oPersonalAnleger.getCKurzzeichen()));

			parameter.put("Belegdatum", Helper.formatDatum(
					anfrageDto.getTBelegdatum(), localeCNrDruck));
			parameter.put("P_ANLIEFERTERMIN", Helper.formatDatum(
					anfrageDto.getTAnliefertermin(), localeCNrDruck));
			parameter.put("Kopftext", sKopftext);

			if (anfrageDto.getSpediteurIId() != null) {
				SpediteurDto spediteurDto = getMandantFac()
						.spediteurFindByPrimaryKey(anfrageDto.getSpediteurIId());
				parameter.put("P_SPEDITEUR",
						spediteurDto.getCNamedesspediteurs());

				if (spediteurDto.getPartnerIId() != null) {
					PartnerDto partnerDto = getPartnerFac()
							.partnerFindByPrimaryKey(
									spediteurDto.getPartnerIId(), theClientDto);

					AnsprechpartnerDto ansprechpartnerDtoSpediteur = null;

					if (spediteurDto.getAnsprechpartnerIId() != null) {
						ansprechpartnerDtoSpediteur = getAnsprechpartnerFac()
								.ansprechpartnerFindByPrimaryKey(
										spediteurDto.getAnsprechpartnerIId(),
										theClientDto);
					}

					parameter.put(
							"P_SPEDITEUR_ADRESSBLOCK",
							formatAdresseFuerAusdruck(partnerDto,
									ansprechpartnerDtoSpediteur, mandantDto,
									localeCNrDruck));
				}
			}

			// die Kommunikationsinformation des Lieferanten, wenn es keine LG
			// Anfrage ist
			String sEmail = null;
			String sFax = null;
			String sTelefon = null;

			if (anfrageDto.getLieferantIIdAnfrageadresse() != null) {
				Integer partnerIIdAnsprechpartner = null;

				if (ansprechpartnerDto != null) {
					partnerIIdAnsprechpartner = ansprechpartnerDto
							.getPartnerIIdAnsprechpartner();
				}

				sEmail = getPartnerFac()
						.partnerkommFindRespectPartnerAsStringOhneExec(
								partnerIIdAnsprechpartner,
								lieferantDto.getPartnerDto(),
								PartnerFac.KOMMUNIKATIONSART_EMAIL,
								theClientDto.getMandant(), theClientDto);
				sFax = getPartnerFac()
						.partnerkommFindRespectPartnerAsStringOhneExec(
								partnerIIdAnsprechpartner,
								lieferantDto.getPartnerDto(),
								PartnerFac.KOMMUNIKATIONSART_FAX,
								theClientDto.getMandant(), theClientDto);
				sTelefon = getPartnerFac()
						.partnerkommFindRespectPartnerAsStringOhneExec(
								partnerIIdAnsprechpartner,
								lieferantDto.getPartnerDto(),
								PartnerFac.KOMMUNIKATIONSART_TELEFON,
								theClientDto.getMandant(), theClientDto);
			}

			parameter.put(LPReport.P_ANSPRECHPARTNEREMAIL,
					sEmail != null ? sEmail : "");
			parameter.put(LPReport.P_ANSPRECHPARTNERFAX, sFax != null ? sFax
					: "");
			parameter.put(LPReport.P_ANSPRECHPARTNERTELEFON,
					sTelefon != null ? sTelefon : "");

			// die folgenden Felder werden im Report mit jeweils einer
			// trennenden Leerzeile
			// hintereinandergehaengt
			// MR 20071014: Folgende Werte muessen auch einzeln an Report
			// uebergeben werden.
			StringBuffer buff = new StringBuffer();

			// Fusstext
			if (sFusstext != null) {
				parameter.put("P_FUSSTEXT",
						Helper.formatStyledTextForJasper(sFusstext));
				buff.append(sFusstext).append("\n\n");
			}

			StringBuffer buffVertreter = new StringBuffer();
			// Anrede des Mandanten
			String sMandantAnrede = mandantDto.getPartnerDto()
					.formatFixName1Name2();
			if (sMandantAnrede != null) {
				parameter.put(P_MANDANT_ANREDE_UND_NAME, sMandantAnrede);
				buff.append(sMandantAnrede).append("\n\n");
				buffVertreter.append(sMandantAnrede).append("\n\n");
			}

			// P_SUMMARY Die Unterschrift fuer Belege inclusive
			// Unterschriftstext und -funktion
			// Beispiel:
			// "i.A. Ing. Werner Hehenwarter" - im Falle der Anfrage der
			// aktuelle Benutzer
			// "Einkaufsleiter"
			if (oPersonalBenutzer != null) {
				String sVertreterUFTitelName2Name1 = oPersonalBenutzer
						.formatFixUFTitelName2Name1();
				parameter.put(P_VERTRETER_UNTERSCHRIFTSFUNKTION_UND_NAME,
						sVertreterUFTitelName2Name1);
				buff.append(sVertreterUFTitelName2Name1);
				buffVertreter.append(sVertreterUFTitelName2Name1);

				if (oPersonalBenutzer.getCUnterschriftstext() != null
						&& oPersonalBenutzer.getCUnterschriftstext().length() > 0) {
					String sUnterschriftstext = oPersonalBenutzer
							.getCUnterschriftstext();
					parameter.put(P_VERTRETER_UNTERSCHRIFTSTEXT,
							sUnterschriftstext);
					buff.append("\n").append(sUnterschriftstext);
					buffVertreter.append("\n").append(sUnterschriftstext);
				}
			}
			parameter.put(P_VERTRETER,
					Helper.formatStyledTextForJasper(buffVertreter.toString()));

			parameter.put("P_LIEFERART_ORT", anfrageDto.getCLieferartort());

			parameter.put("P_SUMMARY",
					Helper.formatStyledTextForJasper(buff.toString()));

			// die Anzahl der Exemplare ist 1 + Anzahl der Kopien
			int iAnzahlExemplare = 1;

			if (iAnzahlKopienI != null && iAnzahlKopienI.intValue() > 0) {
				iAnzahlExemplare += iAnzahlKopienI.intValue();
			}

			aJasperPrint = new JasperPrintLP[iAnzahlExemplare];

			for (int iKopieNummer = 0; iKopieNummer < iAnzahlExemplare; iKopieNummer++) {
				// jede Kopie bekommt eine Kopienummer, das Original bekommt
				// keine
				if (iKopieNummer > 0) {
					parameter.put(LPReport.P_KOPIE_NUMMER, new Integer(
							iKopieNummer));
				}

				// Index zuruecksetzen !!!
				index = -1;

				initJRDS(parameter, AnfrageReportFac.REPORT_MODUL,
						AnfrageReportFac.REPORT_ANFRAGE,
						theClientDto.getMandant(), localeCNrDruck,
						theClientDto, bMitLogo.booleanValue(),
						anfrageDto.getKostenstelleIId());

				aJasperPrint[iKopieNummer] = getReportPrint();
			}

			getAnfrageFac().setzeDruckzeitpunkt(iIdAnfrageI, getTimestamp(),
					theClientDto);
			PrintInfoDto values = getJCRDocFac().getPathAndPartnerAndTable(
					anfrageDto.getIId(), QueryParameters.UC_ID_ANFRAGE,
					theClientDto);
			aJasperPrint[0].setOInfoForArchive(values);
			aJasperPrint[0].putAdditionalInformation(
					JasperPrintLP.KEY_BELEGART, LocaleFac.BELEGART_ANFRAGE);
			aJasperPrint[0].putAdditionalInformation(
					JasperPrintLP.KEY_BELEGIID, anfrageDto.getIId());

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return aJasperPrint;
	}

	/**
	 * Die Anfragestatistik fuer einen bestimmten Artikel ausdrucken.
	 * 
	 * @param reportAnfragestatistikKriterienDtoI
	 *            die Kriterien
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return JasperPrint der Druck
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public JasperPrintLP printAnfragestatistik(
			ReportAnfragestatistikKriterienDto reportAnfragestatistikKriterienDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (reportAnfragestatistikKriterienDtoI == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("reportAnfragestatistikKriterienDtoI == null"));
		}

		JasperPrintLP oPrint = null;

		try {
			// es gilt das Locale des Benutzers
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);

			// die Daten fuer den Report ueber Hibernate holen
			ReportAnfragestatistikDto[] aReportAnfragestatisikDto = getReportAnfragestatistik(
					reportAnfragestatistikKriterienDtoI, theClientDto);

			cAktuellerReport = AnfrageReportFac.REPORT_ANFRAGESTATISTIK;

			int iAnzahlZeilen = aReportAnfragestatisikDto.length; // Anzahl der
			// Zeilen in
			// der
			// Gruppe
			int iAnzahlSpalten = 7; // Anzahl der Spalten in der Gruppe

			data = new Object[iAnzahlZeilen][iAnzahlSpalten];

			// die Datenmatrix befuellen
			for (int i = 0; i < aReportAnfragestatisikDto.length; i++) {
				ReportAnfragestatistikDto anfragestatistikDto = (ReportAnfragestatistikDto) aReportAnfragestatisikDto[i];

				data[i][AnfrageReportFac.REPORT_ANFRAGESTATISTIK_CNR] = anfragestatistikDto
						.getAnfrageCNr();
				data[i][AnfrageReportFac.REPORT_ANFRAGESTATISTIK_KUNDE] = anfragestatistikDto
						.getKundenname();
				data[i][AnfrageReportFac.REPORT_ANFRAGESTATISTIK_BELEGDATUM] = anfragestatistikDto
						.getBelegdatumCNr();
				data[i][AnfrageReportFac.REPORT_ANFRAGESTATISTIK_ANGEFRAGTEMENGE] = anfragestatistikDto
						.getNAngefragtemenge();
				data[i][AnfrageReportFac.REPORT_ANFRAGESTATISTIK_ANGEBOTENEMENGE] = anfragestatistikDto
						.getNAngebotenemenge();
				data[i][AnfrageReportFac.REPORT_ANFRAGESTATISTIK_ANGEBOTENERPREIS] = anfragestatistikDto
						.getNAngebotenerpreis();
				data[i][AnfrageReportFac.REPORT_ANFRAGESTATISTIK_LIEFERZEIT] = anfragestatistikDto
						.getILieferzeit();
			}

			// Erstellung des Report
			HashMap<String, Object> parameter = new HashMap<String, Object>();

			parameter.put("Mandant", mandantDto.getPartnerDto()
					.getCName1nachnamefirmazeile1());
			parameter.put(
					"Artikel",
					formatAnfragestatistikKriterien(
							reportAnfragestatistikKriterienDtoI,
							theClientDto.getLocUi(), theClientDto));

			ParametermandantDto parameterDto = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL,
							ParameterFac.PARAMETER_ARTIKELWIEDERBESCHAFFUNGSZEIT);

			parameter.put("P_EINHEIT_ANLIEFERZEIT", parameterDto.getCWert());

			initJRDS(parameter, AnfrageReportFac.REPORT_MODUL,
					AnfrageReportFac.REPORT_ANFRAGESTATISTIK,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);

			oPrint = getReportPrint();

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} catch (Exception t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, t);
		}

		return oPrint;
	}

	/**
	 * Diese Methode liefert eine Liste von allen Anfragen zu einem bestimmten
	 * Artikel, die nach den eingegebenen Kriterien des Benutzers
	 * zusammengestellt wird. <br>
	 * Achtung: Hibernate verwendet lazy initialization, d.h. der Zugriff auf
	 * Collections muss innerhalb der Session erfolgen.
	 * 
	 * @param reportAnfragestatistikKriterienDtoI
	 *            die Kriterien des Benutzers
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return ReportAnfragestatistikDto[] die Liste der Anfragen
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	private ReportAnfragestatistikDto[] getReportAnfragestatistik(
			ReportAnfragestatistikKriterienDto reportAnfragestatistikKriterienDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		ReportAnfragestatistikDto[] aResult = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;

		try {
			session = factory.openSession();

			// Hiberante Criteria fuer alle Tabellen ausgehend von meiner
			// Haupttabelle anlegen,
			// nach denen ich filtern und sortieren kann
			Criteria crit = session
					.createCriteria(FLRAnfragepositionlieferdatenReport.class);

			// flranfragepositionlieferdatenReport > flranfragepositionReport
			Criteria critAnfragepositionReport = crit
					.createCriteria(AnfragepositionFac.FLR_ANFRAGEPOSITIONLIEFERDATENREPORT_FLRANFRAGEPOSITIONREPORT);

			// flranfragepositionlieferdatenReport > flranfragepositionReport >
			// flrartikel
			Criteria critArtikel = critAnfragepositionReport
					.createCriteria(AnfragepositionFac.FLR_ANFRAGEPOSITION_FLRARTIKEL);

			// flranfragepositionReport > flranfrage
			Criteria critAnfrage = critAnfragepositionReport
					.createCriteria(AnfragepositionFac.FLR_ANFRAGEPOSITION_FLRANFRAGE);

			// Einschraenkung auf den gewaehlten Artikel
			if (reportAnfragestatistikKriterienDtoI.getArtikelIId() != null) {
				critArtikel.add(Restrictions.eq("i_id",
						reportAnfragestatistikKriterienDtoI.getArtikelIId()));
			}

			// Einschraenkung der Anfragen auf den aktuellen Mandanten
			critAnfrage.add(Restrictions.eq("mandant_c_nr",
					theClientDto.getMandant()));

			// Einschraenken nach Status
			critAnfrage.add(Restrictions.ne(
					AnfrageFac.FLR_ANFRAGE_ANFRAGESTATUS_C_NR,
					AnfrageServiceFac.ANFRAGESTATUS_ANGELEGT));
			critAnfrage.add(Restrictions.ne(
					AnfrageFac.FLR_ANFRAGE_ANFRAGESTATUS_C_NR,
					AnfrageServiceFac.ANFRAGESTATUS_STORNIERT));

			// Einschraenken nach Anfrageart
			critAnfrage.add(Restrictions.ne(
					AnfrageFac.FLR_ANFRAGE_ANFRAGEART_C_NR,
					AnfrageServiceFac.ANFRAGEART_LIEFERGRUPPE));

			// Einschraenkung nach Belegdatum von - bis
			if (reportAnfragestatistikKriterienDtoI.getDVon() != null) {
				critAnfrage.add(Restrictions.ge(
						AnfrageFac.FLR_ANFRAGE_T_BELEGDATUM,
						reportAnfragestatistikKriterienDtoI.getDVon()));
			}

			if (reportAnfragestatistikKriterienDtoI.getDBis() != null) {
				critAnfrage.add(Restrictions.le(
						AnfrageFac.FLR_ANFRAGE_T_BELEGDATUM,
						reportAnfragestatistikKriterienDtoI.getDBis()));
			}

			// es wird nach der Belegnummer sortiert
			critAnfrage.addOrder(Order.asc("c_nr"));

			List<?> list = crit.list();
			aResult = new ReportAnfragestatistikDto[list.size()];
			int iIndex = 0;
			Iterator<?> it = list.iterator();
			ReportAnfragestatistikDto reportDto = null;

			while (it.hasNext()) {
				FLRAnfragepositionlieferdatenReport flranfragepositionlieferdaten = (FLRAnfragepositionlieferdatenReport) it
						.next();
				FLRAnfragepositionReport flranfrageposition = flranfragepositionlieferdaten
						.getFlranfragepositionreport();
				FLRAnfrage flranfrage = flranfrageposition.getFlranfrage();

				reportDto = new ReportAnfragestatistikDto();

				reportDto.setAnfrageCNr(flranfrage.getC_nr());
				reportDto.setKundenname(flranfrage.getFlrlieferant()
						.getFlrpartner().getC_name1nachnamefirmazeile1());

				Date datBelegdatum = flranfrage.getT_belegdatum();
				reportDto.setBelegdatumCNr(Helper.formatDatum(datBelegdatum,
						theClientDto.getLocUi()));

				reportDto.setIIndex(new Integer(iIndex));
				reportDto.setNAngebotenemenge(flranfragepositionlieferdaten
						.getN_anliefermenge());

				reportDto.setILieferzeit(flranfragepositionlieferdaten
						.getI_anlieferzeit());

				// der Preis wird in Mandantenwaehrung angezeigt, es gilt der
				// hinterlegte Wechselkurs
				Double ddWechselkurs = flranfrage
						.getF_wechselkursmandantwaehrungzuanfragewaehrung();

				BigDecimal bdPreisinmandantenwaehrung = flranfragepositionlieferdaten
						.getN_nettogesamtpreisminusrabatt().multiply(
								new BigDecimal(ddWechselkurs.doubleValue()));
				bdPreisinmandantenwaehrung = Helper.rundeKaufmaennisch(
						bdPreisinmandantenwaehrung, 4);
				checkNumberFormat(bdPreisinmandantenwaehrung);

				reportDto.setNAngebotenerpreis(bdPreisinmandantenwaehrung);

				reportDto.setNAngefragtemenge(flranfrageposition.getN_menge());

				aResult[iIndex] = reportDto;
				iIndex++;
			}
		} finally {
			closeSession(session);
		}
		return aResult;
	}

	/**
	 * String zum Andrucken der Filterkriterien.
	 * 
	 * @param reportAnfragestatistikKriterienDtoI
	 *            die Kriterien
	 * @param localeI
	 *            das bei der Formatierung gewuenschte Locale
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return String die Filterkriterien
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	private String formatAnfragestatistikKriterien(
			ReportAnfragestatistikKriterienDto reportAnfragestatistikKriterienDtoI,
			Locale localeI, TheClientDto theClientDto) throws EJBExceptionLP {
		StringBuffer buff = new StringBuffer("");

		try {
			buff.append(getArtikelFac()
					.baueArtikelBezeichnungMehrzeiligOhneExc(
							reportAnfragestatistikKriterienDtoI.getArtikelIId(),
							LocaleFac.POSITIONSART_IDENT, null, null, true,
							null, theClientDto));

			// Belegdatum
			if (reportAnfragestatistikKriterienDtoI.getDVon() != null
					|| reportAnfragestatistikKriterienDtoI.getDBis() != null) {
				buff.append("\n").append(
						getTextRespectUISpr("bes.belegdatum",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
			}

			if (reportAnfragestatistikKriterienDtoI.getDVon() != null) {
				buff.append(" ").append(
						getTextRespectUISpr("lp.von",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
				buff.append(" ").append(
						Helper.formatDatum(
								reportAnfragestatistikKriterienDtoI.getDVon(),
								localeI));
			}

			if (reportAnfragestatistikKriterienDtoI.getDBis() != null) {
				buff.append(" ").append(
						getTextRespectUISpr("lp.bis",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
				buff.append(" ").append(
						Helper.formatDatum(
								reportAnfragestatistikKriterienDtoI.getDBis(),
								localeI));
			}
		} catch (RemoteException re) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, re);
		}

		String cBuffer = buff.toString().trim();

		return cBuffer;
	}

	/**
	 * Die Lieferdatenuebersicht fuer alle Anfragen nach bestimmten Kriterien
	 * drucken.
	 * 
	 * @param kritDtoI
	 *            die Kriterien des Benutzers
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return JasperPrint der Druck
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public JasperPrintLP printLieferdatenuebersicht(
			ReportAnfragelieferdatenuebersichtKriterienDto kritDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		JasperPrintLP jasperPrint = null;
		cAktuellerReport = AnfrageReportFac.REPORT_ANFRAGE_LIEFERDATENUEBERSICHT;

		try {
			ReportAnfrageLieferdatenuebersichtDto[] aReportDto = getListeReportAnfrageLiederdatenuebersicht(
					kritDtoI, theClientDto);

			int iAnzahlZeilen = aReportDto.length; // Anzahl der Zeilen in der
			// Gruppe
			int iAnzahlSpalten = 9; // Anzahl der Spalten in der Gruppe

			data = new Object[iAnzahlZeilen][iAnzahlSpalten];

			// die Datenmatrix befuellen
			for (int i = 0; i < iAnzahlZeilen; i++) {
				String artikelCNr = null;

				// @todo boeser Workaround ... PJ 3688
				if (!aReportDto[i].getCNrArtikelcnr().startsWith("~")) {
					artikelCNr = aReportDto[i].getCNrArtikelcnr();
				}

				data[i][AnfrageReportFac.REPORT_LIEFERDATEN_ARTIKELCNR] = artikelCNr;
				data[i][AnfrageReportFac.REPORT_LIEFERDATEN_LIEFERMENGE] = aReportDto[i]
						.getNLiefermenge();
				data[i][AnfrageReportFac.REPORT_LIEFERDATEN_EINHEITCNR] = aReportDto[i]
						.getEinheitCNr().trim();

				String cBezeichnung = getArtikelFac()
						.formatArtikelbezeichnungEinzeiligOhneExc(
								aReportDto[i].getArtikelIId(),
								theClientDto.getLocUi());

				data[i][AnfrageReportFac.REPORT_LIEFERDATEN_ARTIKELCBEZ] = cBezeichnung;

				// umrechnen in Mandantenwaehrung
				BigDecimal nAnlieferpreisinmandantenwaehrung = getBetragMalWechselkurs(
						aReportDto[i].getNLieferpreisInAnfragewaehrung(),
						Helper.getKehrwert(new BigDecimal(
								aReportDto[i]
										.getFWechselkursMandantwaehrungZuAnfragewaehrung()
										.doubleValue())));

				data[i][AnfrageReportFac.REPORT_LIEFERDATEN_ANLIEFERPREIS] = nAnlieferpreisinmandantenwaehrung;
				data[i][AnfrageReportFac.REPORT_LIEFERDATEN_ANLIEFERZEIT] = aReportDto[i]
						.getIAnlieferzeit();
				data[i][AnfrageReportFac.REPORT_LIEFERDATEN_LIEFERANTNAME] = aReportDto[i]
						.getLieferantName();
				data[i][AnfrageReportFac.REPORT_LIEFERDATEN_ANFRAGECNR] = aReportDto[i]
						.getAnfrageCNr();
				data[i][AnfrageReportFac.REPORT_LIEFERDATEN_ANFRAGECBEZ] = aReportDto[i]
						.getAnfrageCBez();
			}

			// Erstellung des Report
			HashMap<String, Object> parameter = new HashMap<String, Object>();

			parameter.put(
					"FILTERKRITERIEN",
					getAnfrageLieferdatenuebersichtFilterKriteriumString(
							kritDtoI, theClientDto.getMandant(),
							theClientDto.getLocUi()));
			parameter.put(
					"SORTIERKRITERIEN",
					getAnfrageLieferdatenuebersichtSortierKriteriumString(
							kritDtoI, theClientDto.getMandant(),
							theClientDto.getLocUi()));
			parameter.put("P_SORTIERENACHPROJEKT",
					new Boolean(kritDtoI.getBSortierungNachProjekt()));
			parameter.put(P_MANDANTWAEHRUNG,
					theClientDto.getSMandantenwaehrung());

			ParametermandantDto parameterDto = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL,
							ParameterFac.PARAMETER_ARTIKELWIEDERBESCHAFFUNGSZEIT);

			parameter.put("P_EINHEIT_ANLIEFERZEIT", parameterDto.getCWert());

			initJRDS(parameter, AnfrageReportFac.REPORT_MODUL,
					cAktuellerReport, theClientDto.getMandant(),
					theClientDto.getLocUi(), theClientDto);

			jasperPrint = getReportPrint();

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return jasperPrint;
	}

	/**
	 * Diese Methode liefert eine Liste von allen Lieferdaten der Anfragen eines
	 * Mandanten, die nach den eingegebenen Kriterien des Benutzers
	 * zusammengestellt wird. <br>
	 * Achtung: Hibernate verwendet lazy initialization, d.h. der Zugriff auf
	 * Collections muss innerhalb der Session erfolgen.
	 * 
	 * @param kritDtoI
	 *            die Kriterien des Benutzers
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return ReportAnfrageLieferdatenuebersichtDto[] alle gewuenschten
	 *         Lieferdaten
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public ReportAnfrageLieferdatenuebersichtDto[] getListeReportAnfrageLiederdatenuebersicht(
			ReportAnfragelieferdatenuebersichtKriterienDto kritDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		ReportAnfrageLieferdatenuebersichtDto[] aResult = null;
		Session session = null;

		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();

			// Criteria duerfen keine Texts oder Blobs enthalten!

			// Criteria anlegen fuer flranfragepositionlieferdatenreport >
			// flranfragepositionreport > flranfrage
			Criteria crit = session
					.createCriteria(FLRAnfragepositionlieferdatenReport.class);

			Criteria critAnfrageposition = crit
					.createCriteria(AnfragepositionFac.FLR_ANFRAGEPOSITIONLIEFERDATENREPORT_FLRANFRAGEPOSITIONREPORT);

			Criteria critAnfrage = critAnfrageposition
					.createCriteria(AnfragepositionFac.FLR_ANFRAGEPOSITION_FLRANFRAGE);

			// Einschraenken nach Mandant
			critAnfrage.add(Restrictions.eq("mandant_c_nr",
					theClientDto.getMandant()));

			// Einschraenken nach Status
			critAnfrage.add(Restrictions.ne(
					AnfrageFac.FLR_ANFRAGE_ANFRAGESTATUS_C_NR,
					AnfrageServiceFac.ANFRAGESTATUS_ANGELEGT));
			critAnfrage.add(Restrictions.ne(
					AnfrageFac.FLR_ANFRAGE_ANFRAGESTATUS_C_NR,
					AnfrageServiceFac.ANFRAGESTATUS_STORNIERT));

			// Einschraenken nach Anfrageart
			critAnfrage.add(Restrictions.ne(
					AnfrageFac.FLR_ANFRAGE_ANFRAGEART_C_NR,
					AnfrageServiceFac.ANFRAGEART_LIEFERGRUPPE));

			// Belegdatum von bis: flranfragepositionlieferdatenreport >
			// flranfragepositionreport > flranfrage.t_belegdatum
			if (kritDtoI.getDVon() != null) {
				critAnfrage
						.add(Restrictions.ge(
								AnfrageFac.FLR_ANFRAGE_T_BELEGDATUM,
								kritDtoI.getDVon()));
			}

			if (kritDtoI.getDBis() != null) {
				critAnfrage
						.add(Restrictions.le(
								AnfrageFac.FLR_ANFRAGE_T_BELEGDATUM,
								kritDtoI.getDBis()));
			}

			// SK: Sortierung nach Bezeichnung und Bezeichnung2
			critAnfrageposition.addOrder(Property.forName("c_bez").asc());
			critAnfrageposition.addOrder(Property.forName("c_zbez").asc());
			// Artikelcnr von bis: flranfragepositionlieferdaten >
			// flranfrageposition > flrartikel.c_nr
			Criteria critArtikel = critAnfrageposition
					.createCriteria(AnfragepositionFac.FLR_ANFRAGEPOSITION_FLRARTIKEL);

			if (kritDtoI.getArtikelCNrVon() != null
					|| kritDtoI.getArtikelCNrBis() != null) {

				String cNrIdentvon = kritDtoI.getArtikelCNrVon();
				String cNrIdentbis = kritDtoI.getArtikelCNrBis();

				if (cNrIdentvon != null && cNrIdentvon.length() > 0
						&& cNrIdentbis != null && cNrIdentbis.length() > 0) {
					critArtikel.add(Restrictions.between("c_nr", cNrIdentvon,
							cNrIdentbis));
				} else if (cNrIdentvon != null && cNrIdentvon.length() > 0) {
					critArtikel.add(Restrictions.ge("c_nr", cNrIdentvon));
				} else if (cNrIdentbis != null && cNrIdentbis.length() > 0) {
					critArtikel.add(Restrictions.le("c_nr", cNrIdentbis));
				}
			}

			// nur Projekt
			if (kritDtoI.getBNurProjekt()) {
				critAnfrage.add(Restrictions.eq("c_bez",
						kritDtoI.getProjektCBez()));
			}

			// inklusive Lieferdaten == null; Hibernate: per default null valued
			// properties are included
			// aber: aufgrund der Implementierung gilt: inklusive Lieferdaten ==
			// 0
			if (!kritDtoI.getBMitLiefermengenNull()) {
				crit.add(Restrictions
						.ne(AnfragepositionFac.FLR_ANFRAGEPOSITIONLIEFERDATEN_N_ANLIEFERMENGE,
								new BigDecimal(0)));
			}

			// Sortierung nach Projekt
			if (kritDtoI.getBSortierungNachProjekt()) {
				critAnfrage.addOrder(Property.forName("c_bez").asc());
			} else {
				// per default wird nach der Artikelcnr sortiert
				critArtikel.addOrder(Property.forName("c_nr").asc());
			}

			List<?> list = crit.list();

			aResult = new ReportAnfrageLieferdatenuebersichtDto[list.size()];
			int iIndex = 0;
			Iterator<?> it = list.iterator();
			ReportAnfrageLieferdatenuebersichtDto reportDto = null;

			while (it.hasNext()) {
				FLRAnfragepositionlieferdatenReport flranfragepositionlieferdaten = (FLRAnfragepositionlieferdatenReport) it
						.next();
				FLRAnfragepositionReport flranfrageposition = flranfragepositionlieferdaten
						.getFlranfragepositionreport();
				FLRArtikel flrartikel = flranfrageposition.getFlrartikel();
				FLRAnfrage flranfrage = flranfrageposition.getFlranfrage();

				reportDto = new ReportAnfrageLieferdatenuebersichtDto();

				reportDto.setCNrArtikelcnr(flranfrageposition.getFlrartikel()
						.getC_nr());
				reportDto.setNLiefermenge(flranfragepositionlieferdaten
						.getN_anliefermenge());
				reportDto.setEinheitCNr(flrartikel.getEinheit_c_nr().trim());
				reportDto.setArtikelIId(flrartikel.getI_id());
				reportDto
						.setNLieferpreisInAnfragewaehrung(flranfragepositionlieferdaten
								.getN_nettogesamtpreisminusrabatt());
				reportDto.setAnfrageIId(flranfrage.getI_id());
				reportDto.setIAnlieferzeit(flranfragepositionlieferdaten
						.getI_anlieferzeit());
				reportDto.setLieferantName(flranfrage.getFlrlieferant()
						.getFlrpartner().getC_name1nachnamefirmazeile1());
				reportDto.setAnfrageCNr(flranfrage.getC_nr());
				reportDto.setAnfrageCBez(flranfrage.getC_bez());
				reportDto.setWaehrungCNr(flranfrage
						.getWaehrung_c_nr_anfragewaehrung());
				reportDto
						.setFWechselkursMandantwaehrungZuAnfragewaehrung(flranfrage
								.getF_wechselkursmandantwaehrungzuanfragewaehrung());

				aResult[iIndex] = reportDto;
				iIndex++;
			}
		} finally {
			closeSession(session);
		}
		return aResult;
	}

	private String getAnfrageLieferdatenuebersichtSortierKriteriumString(
			ReportAnfragelieferdatenuebersichtKriterienDto kritDtoI,
			String mandantCNrI, Locale localeI) throws EJBExceptionLP {
		StringBuffer buff = new StringBuffer(getTextRespectUISpr(
				"lp.sortierungnach", mandantCNrI, localeI));
		buff.append(" ");

		if (kritDtoI.getBSortierungNachProjekt()) {
			buff.append(getTextRespectUISpr("lp.projekt", mandantCNrI, localeI));
		} else {
			buff.append(getTextRespectUISpr("lp.artikel", mandantCNrI, localeI));
		}
		return buff.toString();
	}

	private String getAnfrageLieferdatenuebersichtFilterKriteriumString(
			ReportAnfragelieferdatenuebersichtKriterienDto kritDtoI,
			String mandantCNrI, Locale localeI) throws EJBExceptionLP {
		StringBuffer buff = new StringBuffer();

		if (kritDtoI.getDVon() != null || kritDtoI.getDBis() != null) {
			buff.append(
					getTextRespectUISpr("bes.belegdatum", mandantCNrI, localeI))
					.append(" ");

			if (kritDtoI.getDVon() != null) {
				buff.append(getTextRespectUISpr("lp.von", mandantCNrI, localeI))
						.append(" ")
						.append(Helper.formatDatum(kritDtoI.getDVon(), localeI));
			}

			if (kritDtoI.getDBis() != null) {
				buff.append(" ")
						.append(getTextRespectUISpr("lp.bis", mandantCNrI,
								localeI))
						.append(" ")
						.append(Helper.formatDatum(kritDtoI.getDBis(), localeI))
						.append(" ");
			}
		}

		if (kritDtoI.getArtikelCNrVon() != null
				|| kritDtoI.getArtikelCNrBis() != null) {
			buff.append(
					getTextRespectUISpr("artikel.artikelnummer", mandantCNrI,
							localeI)).append(" ");

			if (kritDtoI.getArtikelCNrVon() != null) {
				buff.append(getTextRespectUISpr("lp.von", mandantCNrI, localeI))
						.append(" ").append(kritDtoI.getArtikelCNrVon())
						.append(" ");
			}

			if (kritDtoI.getArtikelCNrBis() != null) {
				buff.append(" ")
						.append(getTextRespectUISpr("lp.bis", mandantCNrI,
								localeI)).append(" ")
						.append(kritDtoI.getArtikelCNrBis());
			}

			buff.append(" ");
		}

		if (kritDtoI.getBNurProjekt()) {
			buff.append(getTextRespectUISpr("lp.projekt", mandantCNrI, localeI))
					.append(" ");
			buff.append(kritDtoI.getProjektCBez());
		}

		return buff.toString();
	}

	private void checkAnfrageIId(Integer iIdAnfrageI) throws EJBExceptionLP {
		if (iIdAnfrageI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdAnfrageI == null"));
		}

		myLogger.info("AnfrageIId: " + iIdAnfrageI);
	}

}
