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
package com.lp.server.bestellung.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperExportManager;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.anfrage.service.AnfrageDto;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelbestellt;
import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelbestelltFac;
import com.lp.server.artikel.service.ArtikelkommentarDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ArtklaDto;
import com.lp.server.artikel.service.HerstellerDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.MaterialDto;
import com.lp.server.artikel.service.MaterialzuschlagDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.ejb.BsmahnstufePK;
import com.lp.server.bestellung.fastlanereader.generated.FLRBSMahnung;
import com.lp.server.bestellung.fastlanereader.generated.FLRBestellposition;
import com.lp.server.bestellung.fastlanereader.generated.FLRBestellpositionReport;
import com.lp.server.bestellung.fastlanereader.generated.FLRBestellung;
import com.lp.server.bestellung.fastlanereader.generated.FLRBestellvorschlag;
import com.lp.server.bestellung.fastlanereader.generated.FLRWareneingangspositionen;
import com.lp.server.bestellung.service.BSMahnlaufDto;
import com.lp.server.bestellung.service.BSMahnstufeDto;
import com.lp.server.bestellung.service.BSMahntextDto;
import com.lp.server.bestellung.service.BSMahnungDto;
import com.lp.server.bestellung.service.BSMahnwesenFac;
import com.lp.server.bestellung.service.BSSammelmahnungDto;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.bestellung.service.BestellungReportFac;
import com.lp.server.bestellung.service.BestellungServiceFac;
import com.lp.server.bestellung.service.BestellungtextDto;
import com.lp.server.bestellung.service.BestellvorschlagFac;
import com.lp.server.bestellung.service.ReportBestellungOffeneDto;
import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.bestellung.service.WareneingangFac;
import com.lp.server.bestellung.service.WareneingangspositionDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.inserat.service.InseratDto;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklisteposition;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.pkgenerator.format.LpBelegnummerFormat;
import com.lp.server.system.service.BelegartDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.MediastandardDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.system.service.SpediteurDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.VersandauftragDto;
import com.lp.server.util.BelegPositionDruckIdentDto;
import com.lp.server.util.BelegPositionDruckTextbausteinDto;
import com.lp.server.util.HelperServer;
import com.lp.server.util.LPReport;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.server.util.report.TimingInterceptor;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.LPDatenSubreport;

@Stateless
@Interceptors(TimingInterceptor.class)
public class BestellungReportFacBean extends LPReport implements
		BestellungReportFac, JRDataSource {

	@PersistenceContext
	private EntityManager em;

	private final static int UC_ALLE = 0;
	private final static int UC_BESTELLUNG = 1;
	private final static int UC_OFFENE = 2;
	private final static int UC_OFFENE_OD = 3;
	private final static int UC_BESTELLVORSCHLAG = 4;
	private final static int UC_WEP_ETIKETT = 5;
	private final static int UC_BSSAMMELMAHNUNG = 6;
	private final static int UC_BSMAHNUNG = 7;
	private final static int UC_BESTELLUNG_WARENEINGANG = 8;
	private final static int UC_ABHOLAUFTRAG = 9;
	private final static int UC_GEAENDERTE_ARTIKEL = 10;

	private final static int ALLE_BESTELLNUMMER = 0;
	private final static int ALLE_BELEGDATUM = 1;
	private final static int ALLE_BESTELLWERT = 2;
	private final static int ALLE_LIEFERTERMIN = 3;
	private final static int ALLE_LIEFERANT = 4;
	private final static int ALLE_KOSTENSTELLENUMMER = 5;
	private final static int ALLE_STATUS = 6;

	public static int REPORT_ABHOLAUFTRAG_POSITION = 0;
	public static int REPORT_ABHOLAUFTRAG_IDENT = 1;
	public static int REPORT_ABHOLAUFTRAG_MENGE = 2;
	public static int REPORT_ABHOLAUFTRAG_EINHEIT = 3;
	public static int REPORT_ABHOLAUFTRAG_EINZELPREIS = 4;
	public static int REPORT_ABHOLAUFTRAG_RABATT = 5;
	public static int REPORT_ABHOLAUFTRAG_GESAMTPREIS = 6;
	public static int REPORT_ABHOLAUFTRAG_POSITIONSART = 7;
	public static int REPORT_ABHOLAUFTRAG_FREIERTEXT = 8;
	public static int REPORT_ABHOLAUFTRAG_LEERZEILE = 9;
	public static int REPORT_ABHOLAUFTRAG_IMAGE = 10;
	public static int REPORT_ABHOLAUFTRAG_GEWICHT = 11;
	public static int REPORT_ABHOLAUFTRAG_SEITENUMBRUCH = 12;
	public static int REPORT_ABHOLAUFTRAG_GEWICHTEINHEIT = 13;
	public static int REPORT_ABHOLAUFTRAG_POSITIONTERMIN = 14;
	public static int REPORT_ABHOLAUFTRAG_IDENTNUMMER = 15;
	public static int REPORT_ABHOLAUFTRAG_BEZEICHNUNG = 16;
	public static int REPORT_ABHOLAUFTRAG_KURZBEZEICHNUNG = 17;
	public static int REPORT_ABHOLAUFTRAG_PREISPEREINHEIT = 18;
	public static int REPORT_ABHOLAUFTRAG_ARTIKELCZBEZ2 = 19;
	public static int REPORT_ABHOLAUFTRAG_LIEFERANT_ARTIKEL_IDENTNUMMER = 20;
	public static int REPORT_ABHOLAUFTRAG_LIEFERANT_ARTIKEL_BEZEICHNUNG = 21;
	public static int REPORT_ABHOLAUFTRAG_ARTIKEL_HERSTELLER = 22;
	public static int REPORT_ABHOLAUFTRAG_REFERENZNUMMER = 23;
	public static int REPORT_ABHOLAUFTRAG_ARTIKELKOMMENTAR = 24;
	public static int REPORT_ABHOLAUFTRAG_ZUSATZBEZEICHNUNG = 25;
	public static int REPORT_ABHOLAUFTRAG_BAUFORM = 26;
	public static int REPORT_ABHOLAUFTRAG_VERPACKUNGSART = 27;
	public static int REPORT_ABHOLAUFTRAG_ARTIKEL_MATERIAL = 28;
	public static int REPORT_ABHOLAUFTRAG_ARTIKEL_BREITE = 29;
	public static int REPORT_ABHOLAUFTRAG_ARTIKEL_HOEHE = 30;
	public static int REPORT_ABHOLAUFTRAG_ARTIKEL_TIEFE = 31;
	public static int REPORT_ABHOLAUFTRAG_ARTIKEL_HERSTELLER_NAME = 32;
	public static int REPORT_ABHOLAUFTRAG_POSITIONOBKJEKT = 33;
	public static int REPORT_ABHOLAUFTRAG_OFFENERAHMENMENGE = 34;
	public static int REPORT_ABHOLAUFTRAG_ANGEBOTSNUMMER = 35;
	public static int REPORT_ABHOLAUFTRAG_ANZAHL_SPALTEN = 36;

	private final static int REPORT_BSSAMMELMAHNUNG_BESTELLNUMMER = 0;
	private final static int REPORT_BSSAMMELMAHNUNG_BELEGDATUM = 1;
	private final static int REPORT_BSSAMMELMAHNUNG_LIEFERTERMIN = 2;
	private final static int REPORT_BSSAMMELMAHNUNG_WERT = 3;
	private final static int REPORT_BSSAMMELMAHNUNG_OFFENEMENGE = 4;
	private final static int REPORT_BSSAMMELMAHNUNG_MAHNSTUFE = 5;
	private final static int REPORT_BSSAMMELMAHNUNG_BESTELLPOSITION_BEZEICHNUNG = 6;
	private static final int REPORT_BSSAMMELMAHNUNG_IDENTNUMMER = 7;
	private static final int REPORT_BSSAMMELMAHNUNG_LIEFERANT_IDENTNUMMER = 8;
	private static final int REPORT_BSSAMMELMAHNUNG_LIEFERANT_ARTIKEL_BEZEICHNUNG = 9;
	private static final int REPORT_BSSAMMELMAHNUNG_ARTIKEL_HERSTELLER = 10;
	private static final int REPORT_BSSAMMELMAHNUNG_REFERENZNUMMER = 11;
	private static final int REPORT_BSSAMMELMAHNUNG_ARTIKELCZBEZ2 = 12;
	private static final int REPORT_BSSAMMELMAHNUNG_ABTERMIN = 13;
	private static final int REPORT_BSSAMMELMAHNUNG_SIZE = 14;

	private final static int REPORT_BSWARENEINGANGSJOURNAL_DATUM = 0;
	private final static int REPORT_BSWARENEINGANGSJOURNAL_BESTELLNUMMER = 1;
	private final static int REPORT_BSWARENEINGANGSJOURNAL_IDENT = 2;
	private final static int REPORT_BSWARENEINGANGSJOURNAL_BEZEICHNUNG = 3;
	private final static int REPORT_BSWARENEINGANGSJOURNAL_MENGE = 4;
	private final static int REPORT_BSWARENEINGANGSJOURNAL_GELIEFERTERPREIS = 5;
	private final static int REPORT_BSWARENEINGANGSJOURNAL_EINSTANDSPREIS = 6;
	private final static int REPORT_BSWARENEINGANGSJOURNAL_PROJEKT = 7;
	private final static int REPORT_BSWARENEINGANGSJOURNAL_LIEFERANT = 8;

	private final static int REPORT_BSWARENEINGANGSJOURNAL_LIEFERSCHEIN = 9;
	private final static int REPORT_BSWARENEINGANGSJOURNAL_LIEFERSCHEINDATUM = 10;
	private final static int REPORT_BSWARENEINGANGSJOURNAL_KOSTENSTELLE = 11;
	private final static int REPORT_BSWARENEINGANGSJOURNAL_ZUBUCHUNGSLAGER = 12;
	private final static int REPORT_BSWARENEINGANGSJOURNAL_EINGANGSRECHNUNG = 13;
	private final static int REPORT_BSWARENEINGANGSJOURNAL_RABATTSATZ = 14;
	private final static int REPORT_BSWARENEINGANGSJOURNAL_AUFTRAG = 15;
	private final static int REPORT_BSWARENEINGANGSJOURNAL_ARTIKELKLASSE = 16;
	private final static int REPORT_BSWARENEINGANGSJOURNAL_ARTIKELGRUPPE = 17;
	private final static int REPORT_BSWARENEINGANGSJOURNAL_TRANSPORTKOSTEN = 18;
	private final static int REPORT_BSWARENEINGANGSJOURNAL_BANKSPESEN = 19;
	private final static int REPORT_BSWARENEINGANGSJOURNAL_ZOLLKOSTEN = 20;
	private final static int REPORT_BSWARENEINGANGSJOURNAL_SONSTIGEKOSTEN = 21;
	private final static int REPORT_BSWARENEINGANGSJOURNAL_GK_FAKTOR = 22;
	private final static int REPORT_BSWARENEINGANGSJOURNAL_SETARTIKEL_TYP = 23;
	private final static int REPORT_BSWARENEINGANGSJOURNAL_WA_REFERENZ = 24;
	private final static int REPORT_BSWARENEINGANGSJOURNAL_ANZAHL_SPALTEN = 25;

	private int useCase;
	private Object[][] data = null;

	public boolean next() throws JRException {
		index++;
		return (index < data.length);
	}

	public Object getFieldValue(JRField field) throws JRException {
		Object value = null;
		String fieldName = field.getName();
		switch (useCase) {
		case UC_ALLE: {
			if ("F_BESTELLNUMMER".equals(fieldName)) {
				value = data[index][ALLE_BESTELLNUMMER];
			} else if ("F_BELEGDATUM".equals(fieldName)) {
				value = data[index][ALLE_BELEGDATUM];
			} else if ("F_BESTELLWERT".equals(fieldName)) {
				value = data[index][ALLE_BESTELLWERT];
			} else if ("F_LIEFERTERMIN".equals(fieldName)) {
				value = data[index][ALLE_LIEFERTERMIN];
			} else if ("F_LIEFERANT".equals(fieldName)) {
				value = data[index][ALLE_LIEFERANT];
			} else if ("F_KOSTENSTELLENUMMER".equals(fieldName)) {
				value = data[index][ALLE_KOSTENSTELLENUMMER];
			} else if ("F_STATUS".equals(fieldName)) {
				value = data[index][ALLE_STATUS];
			}
		}
			break;
		case UC_BESTELLUNG_WARENEINGANG: {
			if ("F_DATUM".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_DATUM];
			} else if ("F_BESTELLNUMMER".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_BESTELLNUMMER];
			} else if ("F_IDENT".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_IDENT];
			} else if ("F_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_BEZEICHNUNG];
			} else if ("F_MENGE".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_MENGE];
			} else if ("F_GELIEFERTERPREIS".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_GELIEFERTERPREIS];
			} else if ("F_EINSTANDSPREIS".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_EINSTANDSPREIS];
			} else if ("F_PROJEKT".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_PROJEKT];
			} else if ("F_LIEFERANT".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_LIEFERANT];
			} else if ("F_SETARTIKEL_TYP".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_SETARTIKEL_TYP];
			} else if ("F_WA_REFERENZ".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_WA_REFERENZ];
			}

			else if ("F_LIEFERSCHEIN".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_LIEFERSCHEIN];
			} else if ("F_LIEFERSCHEINDATUM".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_LIEFERSCHEINDATUM];
			} else if ("F_KOSTENSTELLE".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_KOSTENSTELLE];
			} else if ("F_ZUBUCHUNGSLAGER".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_ZUBUCHUNGSLAGER];
			} else if ("F_EINGANGSRECHNUNG".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_EINGANGSRECHNUNG];
			} else if ("F_RABATTSATZ".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_RABATTSATZ];
			} else if ("F_AUFTRAG".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_AUFTRAG];
			} else if ("F_ARTIKELKLASSE".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_ARTIKELKLASSE];
			} else if ("F_ARTIKELGRUPPE".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_ARTIKELGRUPPE];
			} else if ("F_TRANSPORTKOSTEN".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_TRANSPORTKOSTEN];
			} else if ("F_BANKSPESEN".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_BANKSPESEN];
			} else if ("F_ZOLLKOSTEN".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_ZOLLKOSTEN];
			} else if ("F_SONSTIGEKOSTEN".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_SONSTIGEKOSTEN];
			} else if ("F_GK_FAKTOR".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_GK_FAKTOR];
			}

		}
			break;
		case UC_OFFENE: {
			if ("F_BESTELLUNGCNR".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_BESTELLUNGCNR];
			} else if ("F_BESTELLUNGARTCNR".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_BESTELLUNGARTCNR];
			} else if ("F_LIEFERTERMIN".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_BESTELLUNGLIEFERTERMIN];
			} else if ("F_WERT".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_WERT];
			} else if ("F_OFFEN".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_WERT_OFFEN];
			} else if ("F_BESTELLUNGLIEFERANT".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_BESTELLUNGLIEFERANT];
			} else if ("F_KOSTENSTELLE".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_KOSTENSTELLECNR];
			} else if ("F_ARTIKELCNR".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ARTIKELCNR];
			} else if ("F_ARTIKELBEZEICHNUNG".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ARTIKELBEZ];
			} else if ("F_ARTIKELPREIS".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ARTIKELPREIS];
			} else if ("F_ARTIKELMENGE".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ARTIKELMENGE];
			} else if ("F_ARTIKELLAGERSTAND".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ARTIKELLAGERSTAND];
			} else if ("F_ARTIKELEINHEIT".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ARTIKELEINHEIT];
			} else if ("F_ARTIKELOFENEMENGE".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ARTIKELOFFENEMENGE];
			} else if ("F_ARTIKELGELIFERTEMENGE".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ARTIKELGELIFERTEMENGE];
			} else if ("F_ARTIKELOFFENEWERT".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ARTIKELOFFENEWERT];
			} else if ("F_PROJEKT".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_PROJEKT];
			} else if ("F_ABTERMIN".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ABTERMIN];
			} else if ("F_ABNUMMER".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ABNUMMER];
			} else if ("F_ABKOMMENTAR".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ABKOMMENTAR];
			} else if ("F_OFFENELIEFERUNG".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_OFFENELIEFERUNGEN];
			} else if ("F_SETARTIKEL_TYP".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_SETARTIKEL_TYP];
			}
		}
			break;
		case UC_BESTELLVORSCHLAG: {
			if ("F_BELEGART".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_BELEGART];
			} else if ("F_BELEGCNR".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_BELEGCNR];
			} else if ("F_BESTELLTERMIN".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_BESTELLTERMIN];
			} else if ("F_WERT".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_WERT];
			} else if ("F_OFFEN".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_WERT_OFFEN];
			} else if ("F_BESTELLUNGLIEFERANT".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_BESTELLUNGLIEFERANT];
			} else if ("F_ARTIKELRESERVIERUNG".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELRESERVIERUNG];
			} else if ("F_ARTIKELFEHLMENGE".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELFEHLMENGE];
			} else if ("F_ARTIKELCNR".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELCNR];
			} else if ("F_ARTIKELLAGERBEWIRTSCHAFTET".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKEL_LAGERBEWIRTSCHAFTET];
			} else if ("F_ARTIKELBEZEICHNUNG".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELBEZ];
			} else if ("F_ARTIKELZUSATZBEZEICHNUNG".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELZBEZ];
			} else if ("F_ARTIKELZUSATZBEZEICHNUNG2".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELZBEZ2];
			} else if ("F_PROJEKT".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_PROJEKT];
			} else if ("F_ARTIKELKURZBEZEICHNUNG".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELKBEZ];
			} else if ("F_ARTIKELSPERREN".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_SPERREN];
			} else if ("F_ARTIKELPREIS".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELPREIS];
			} else if ("F_ARTIKELMENGE".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELMENGE];
			} else if ("F_ARTIKELLAGERSTAND".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELLAGERSTAND];
			} else if ("F_ARTIKELEINHEIT".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELEINHEIT];
			} else if ("F_ARTIKELLAGERMINDESTSTAND".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELLAGERMINDESTSTAND];
			} else if ("F_ARTIKELLAGERSOLL".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELLAGERSOLL];
			} else if (F_ARTIKEL_RAHMENDETAILBEDARF.equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKEL_RAHMENDETAILBEDARF];
			} else if (F_ARTIKELLIEFERANT_ARTIKELNR.equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_LIEFERANT_ARTIKEL_IDENTNUMMER];
			} else if (F_ARTIKELLIEFERANT_STDMENGE.equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_LIEFERANT_STANDARDMENGE];
			} else if (F_ARTIKELLIEFERANT_BEZ.equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_LIEFERANT_ARTIKEL_BEZEICHNUNG];
			} else if (F_ARTIKELMENGE_OFFEN.equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKEL_MENGE_OFFEN];
			} else if (F_ARTIKELRAHMENMENGE_OFFEN.equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKEL_RAHMENMENGE_OFFEN];
			} else if ("F_ARTIKELRAHMENMENGE_OFFEN_BESTELLCNR"
					.equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKEL_RAHMENBESTELLNR];
			} else if ("F_ARTIKEL_OFFEN_BESTELLCNR".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKEL_OFFENE_BESTELLNR];
			} else if ("F_LIEFERANT_MATERIALZUSCHLAG".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_LIEFERANT_MATERIALZUSCHLAG];
			} else if ("F_SUBREPORT_VERWENDUNGSNACHWEIS".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_SUBREPORT_VERWENDUNGSNACHWEIS];
			}
		}
			break;
		case UC_WEP_ETIKETT: {
			if ("F_ANLIEFERMENGE".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WEP_ETIKETT_ANLIEFERMENGE];
			} else if ("F_BESTELLNUMMER".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WEP_ETIKETT_BESTELLNUMMER];
			} else if ("F_CHARGENNUMMER".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WEP_ETIKETT_CHARGENNUMMER];
			} else if ("F_SERIENNUMMER".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WEP_ETIKETT_SERIENNUMMER];
			} else if ("F_VERPACKUNGSEINHEIT".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WEP_ETIKETT_VERPACKUNGSEINHEIT];
			} else if ("F_WARENVERKEHRSNUMMER".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WEP_ETIKETT_WARENVERKEHRSNUMMER];
			} else if ("F_GEWICHT".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WEP_ETIKETT_GEWICHT];
			} else if ("F_LAGER".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WEP_ETIKETT_LAGER];
			} else if ("F_LAGERORT".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WEP_ETIKETT_LAGERORT];
			} else if ("F_URSPRUNGSLAND".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WEP_ETIKETT_URSPRUNGSLAND];
			} else if ("F_KOMMENTAR".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WEP_ETIKETT_KOMMENTAR];
			} else if ("F_IDENT".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WEP_ETIKETT_IDENT];
			} else if ("F_BEZ".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WEP_ETIKETT_BEZ];
			} else if ("F_ZBEZ".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WEP_ETIKETT_ZBEZ];
			} else if ("F_ZBEZ2".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WEP_ETIKETT_ZBEZ2];
			} else if ("F_LIEFERANTENARTIKELNUMMER".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WEP_ETIKETT_LIEFERANTENARTIKELNUMMER];
			} else if ("F_LIEFERANTENARTIKELBEZ".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WEP_ETIKETT_LIEFERANTENARTIKELBEZ];
			} else if ("F_WE_DATUM".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WEP_ETIKETT_WE_DATUM];
			} else if ("F_EINHEIT".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WEP_ETIKETT_EINHEIT];
			} else if ("F_PROJEKTBEZ".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WEP_ETIKETT_PROJEKTBEZ];
			} else if ("F_LIEFERANTENNAME".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WEP_ETIKETT_LIEFERANTNAME];
			} else if ("F_HERSTELLER".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WEP_ETIKETT_HERSTELLER];
			} else if ("F_HANDMENGE".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WEP_ETIKETT_HANDMENGE];
			} else if ("F_HERSTELLERNAME".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WEP_ETIKETT_HERSTELLERNAME];
			} else if ("F_WE_REFERENZ".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WEP_ETIKETT_WE_REFERENZ];
			}
		}
			break;
		case UC_BSMAHNUNG: {
		}
			break;
		case UC_BSSAMMELMAHNUNG: {
			if ("F_BELEGDATUM".equals(fieldName)) {
				value = data[index][REPORT_BSSAMMELMAHNUNG_BELEGDATUM];
			} else if ("F_LIEFERTERMIN".equals(fieldName)) {
				value = data[index][REPORT_BSSAMMELMAHNUNG_LIEFERTERMIN];
			} else if ("F_MAHNSTUFE".equals(fieldName)) {
				value = data[index][REPORT_BSSAMMELMAHNUNG_MAHNSTUFE];
			} else if ("F_OFFENEMENGE".equals(fieldName)) {
				value = data[index][REPORT_BSSAMMELMAHNUNG_OFFENEMENGE];
			} else if ("F_BESTELLUNGSNUMMER".equals(fieldName)) {
				value = data[index][REPORT_BSSAMMELMAHNUNG_BESTELLNUMMER];
			} else if ("F_WERT".equals(fieldName)) {
				value = data[index][REPORT_BSSAMMELMAHNUNG_WERT];
			} else if ("F_BESPOSBEZEICHNUNG".equals(fieldName)) {
				value = data[index][REPORT_BSSAMMELMAHNUNG_BESTELLPOSITION_BEZEICHNUNG];
			} else if ("F_IDENTNUMMER".equals(fieldName)) {
				value = data[index][REPORT_BSSAMMELMAHNUNG_IDENTNUMMER];
			} else if ("F_LIEFERANT_ARTIKEL_IDENTNUMMER".equals(fieldName)) {
				value = data[index][REPORT_BSSAMMELMAHNUNG_LIEFERANT_IDENTNUMMER];
			} else if ("F_LIEFERANT_ARTIKEL_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][REPORT_BSSAMMELMAHNUNG_LIEFERANT_ARTIKEL_BEZEICHNUNG];
			} else if ("F_ARTIKEL_HERSTELLER".equals(fieldName)) {
				value = data[index][REPORT_BSSAMMELMAHNUNG_ARTIKEL_HERSTELLER];
			} else if ("F_REFERENZNUMMER".equals(fieldName)) {
				value = data[index][REPORT_BSSAMMELMAHNUNG_REFERENZNUMMER];
			} else if ("F_ARTIKELCZBEZ2".equals(fieldName)) {
				value = data[index][REPORT_BSSAMMELMAHNUNG_ARTIKELCZBEZ2];
			} else if ("F_ABTERMIN".equals(fieldName)) {
				value = data[index][REPORT_BSSAMMELMAHNUNG_ABTERMIN];
			}
		}
			break;

		case UC_GEAENDERTE_ARTIKEL: {
			if ("F_ARTIKELNUMMER_AKTUELL".equals(fieldName)) {
				value = data[index][REPORT_GEAENDERTEARTIKEL_ARTIKELNUMMER_AKTUELL];
			} else if ("F_ARTIKELBEZEICHNUNG_AKTUELL".equals(fieldName)) {
				value = data[index][REPORT_GEAENDERTEARTIKEL_ARTIKELBEZEICHNUNG_AKTUELL];
			} else if ("F_ARTIKELZUSATZBEZEICHNUNG_AKTUELL".equals(fieldName)) {
				value = data[index][REPORT_GEAENDERTEARTIKEL_ARTIKELZUSATZBEZEICHNUNG_AKTUELL];
			} else if ("F_ARTIKELZUSATZBEZEICHNUNG2_AKTUELL".equals(fieldName)) {
				value = data[index][REPORT_GEAENDERTEARTIKEL_ARTIKELZUSATZBEZEICHNUNG2_AKTUELL];
			} else if ("F_ARTIKELNUMMER_BESTELLUNG".equals(fieldName)) {
				value = data[index][REPORT_GEAENDERTEARTIKEL_ARTIKELNUMMER_BESTELLUNG];
			} else if ("F_ARTIKELBEZEICHNUNG_BESTELLUNG".equals(fieldName)) {
				value = data[index][REPORT_GEAENDERTEARTIKEL_ARTIKELBEZEICHNUNG_BESTELLUNG];
			} else if ("F_ARTIKELZUSATZBEZEICHNUNG_BESTELLUNG"
					.equals(fieldName)) {
				value = data[index][REPORT_GEAENDERTEARTIKEL_ARTIKELZUSATZBEZEICHNUNG_BESTELLUNG];
			} else if ("F_ARTIKELZUSATZBEZEICHNUNG2_BESTELLUNG"
					.equals(fieldName)) {
				value = data[index][REPORT_GEAENDERTEARTIKEL_ARTIKELZUSATZBEZEICHNUNG2_BESTELLUNG];
			} else if ("F_LIEFERANT".equals(fieldName)) {
				value = data[index][REPORT_GEAENDERTEARTIKEL_LIEFERANT];
			} else if ("F_BESTELLDATUM".equals(fieldName)) {
				value = data[index][REPORT_GEAENDERTEARTIKEL_BESTELLDATUM];
			} else if ("F_BESTELLNUMMER".equals(fieldName)) {
				value = data[index][REPORT_GEAENDERTEARTIKEL_BESTELLNUMMER];
			} else if ("F_POSITIONSTERMIN".equals(fieldName)) {
				value = data[index][REPORT_GEAENDERTEARTIKEL_POSITIONSTERMIN];
			} else if ("F_ABTERMIN".equals(fieldName)) {
				value = data[index][REPORT_GEAENDERTEARTIKEL_ABTERMIN];
			} else if ("F_ABNUMMER".equals(fieldName)) {
				value = data[index][REPORT_GEAENDERTEARTIKEL_ABNUMMER];
			} else if ("F_MENGE".equals(fieldName)) {
				value = data[index][REPORT_GEAENDERTEARTIKEL_MENGE];
			} else if ("F_PREIS".equals(fieldName)) {
				value = data[index][REPORT_GEAENDERTEARTIKEL_PREIS];
			}

		}
			break;
		case UC_BESTELLUNG:
			if ("Position".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_POSITION];
			} else if ("Ident".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_IDENT];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_MENGE];
			} else if ("Einheit".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_EINHEIT];
			} else if ("Gewicht".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_GEWICHT];
			} else if ("Gewichteinheit".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_GEWICHTEINHEIT];
			} else if ("F_ARTIKELCZBEZ2".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_ARTIKELCZBEZ2];
			} else if ("F_POSITIONTERMIN".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_POSITIONTERMIN];
			} else if ("Einzelpreis".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_EINZELPREIS];
			} else if ("Rabatt".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_RABATT];
			} else if ("Gesamtpreis".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_GESAMTPREIS];
			} else if ("Positionsart".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_POSITIONSART];
			} else if ("Freiertext".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_FREIERTEXT];
			} else if ("Leerzeile".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_LEERZEILE];
			} else if ("Image".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_IMAGE];
			} else if ("Seitenumbruch".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_SEITENUMBRUCH];
			} else if ("F_PREISPEREINHEIT".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_PREISPEREINHEIT];
			} else if (F_IDENTNUMMER.equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_IDENTNUMMER];
			} else if (F_BEZEICHNUNG.equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_BEZEICHNUNG];
			} else if (F_KURZBEZEICHNUNG.equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_KURZBEZEICHNUNG];
			} else if ("F_LIEFERANT_ARTIKEL_VERPACKUNGSEINHEIT"
					.equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_LIEFERANT_VERPACKUNGSEINHEIT];
			} else if ("F_LIEFERANT_ARTIKEL_IDENTNUMMER".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][BestellungReportFac.REPORT_BESTELLUNG_LIEFERANT_ARTIKEL_IDENTNUMMER]);
			} else if ("F_LIEFERANT_ARTIKEL_BEZEICHNUNG".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][BestellungReportFac.REPORT_BESTELLUNG_LIEFERANT_ARTIKEL_BEZEICHNUNG]);
			} else if ("F_ARTIKEL_HERSTELLER".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_HERSTELLER]);
			} else if (F_REFERENZNUMMER.equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][BestellungReportFac.REPORT_BESTELLUNG_REFERENZNUMMER]);
			} else if (F_ARTIKELKOMMENTAR.equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][BestellungReportFac.REPORT_BESTELLUNG_ARTIKELKOMMENTAR]);
			} else if (F_ZUSATZBEZEICHNUNG.equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_ZUSATZBEZEICHNUNG];
			} else if (F_ARTIKEL_BAUFORM.equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_BAUFORM];
			} else if (F_ARTIKEL_VERPACKUNGSART.equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_VERPACKUNGSART];
			} else if (F_ARTIKEL_MATERIAL.equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_MATERIAL];
			} else if (F_ARTIKEL_MATERIALGEWICHT.equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_MATERIALGEWICHT];
			} else if (F_ARTIKEL_KURS_MATERIALZUSCHLAG.equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_KURS_MATERIALZUSCHLAG];
			} else if (F_ARTIKEL_DATUM_MATERIALZUSCHLAG.equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_DATUM_MATERIALZUSCHLAG];
			} else if (F_ARTIKEL_BREITE.equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_BREITE];
			} else if (F_ARTIKEL_HOEHE.equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_HOEHE];
			} else if (F_ARTIKEL_TIEFE.equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_TIEFE];
			} else if (F_HERSTELLER_NAME.equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_HERSTELLER_NAME];
			} else if ("F_POSITIONSOBJEKT".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_POSITIONOBKJEKT];
			} else if ("F_OFFENERAHMENMENGE".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENERAHMENMENGE];
			} else if ("F_ANGEBOTSNUMMER".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_ANGEBOTSNUMMER];
			} else if ("F_MATERIALZUSCHLAG".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_MATERIALZUSCHLAG];
			} else if ("F_ARTIKEL_INDEX".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_INDEX];
			} else if ("F_ARTIKEL_REVISION".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_REVISION];
			}

			else if ("F_SOLLMATERIAL_KOMMENTAR".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_SOLLMATERIAL_KOMMENTAR];
			} else if ("F_SOLLMATERIAL_LOS_STKLKBEZ".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_SOLLMATERIAL_LOS_STKLKBEZ];
			} else if ("F_SOLLMATERIAL_LOS_STKLBEZ".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_SOLLMATERIAL_LOS_STKLBEZ];
			} else if ("F_SOLLMATERIAL_LOSNUMMER".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_SOLLMATERIAL_LOSNUMMER];
			} else if ("F_SOLLMATERIAL_LOS_STKLNUMMER".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_SOLLMATERIAL_LOS_STKLNUMMER];
			} else if ("F_SOLLMATERIAL_LOS_KOMMENTAR".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_SOLLMATERIAL_LOS_KOMMENTAR];
			} else if ("F_SOLLMATERIAL_LOS_PROJEKT".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_SOLLMATERIAL_LOS_PROJEKT];
			} else if ("F_SOLLMATERIAL_SUBREPORT_ARBEITSGAENGE"
					.equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_SOLLMATERIAL_SUBREPORT_ARBEITSGAENGE];
			} else if ("F_SETARTIKEL_TYP".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_SETARTIKEL_TYP];
			} else if ("F_INSERAT_DTO".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_INSERAT_DTO];
			} else if ("F_INSERAT_KUNDE".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_INSERAT_KUNDE];
			}

			break;
		case UC_ABHOLAUFTRAG:
			if ("Position".equals(fieldName)) {
				value = data[index][REPORT_ABHOLAUFTRAG_POSITION];
			} else if ("Ident".equals(fieldName)) {
				value = data[index][REPORT_ABHOLAUFTRAG_IDENT];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_ABHOLAUFTRAG_MENGE];
			} else if ("Einheit".equals(fieldName)) {
				value = data[index][REPORT_ABHOLAUFTRAG_EINHEIT];
			} else if ("Gewicht".equals(fieldName)) {
				value = data[index][REPORT_ABHOLAUFTRAG_GEWICHT];
			} else if ("Gewichteinheit".equals(fieldName)) {
				value = data[index][REPORT_ABHOLAUFTRAG_GEWICHTEINHEIT];
			} else if ("F_ARTIKELCZBEZ2".equals(fieldName)) {
				value = data[index][REPORT_ABHOLAUFTRAG_ARTIKELCZBEZ2];
			} else if ("F_POSITIONTERMIN".equals(fieldName)) {
				value = data[index][REPORT_ABHOLAUFTRAG_POSITIONTERMIN];
			} else if ("Einzelpreis".equals(fieldName)) {
				value = data[index][REPORT_ABHOLAUFTRAG_EINZELPREIS];
			} else if ("Rabatt".equals(fieldName)) {
				value = data[index][REPORT_ABHOLAUFTRAG_RABATT];
			} else if ("Gesamtpreis".equals(fieldName)) {
				value = data[index][REPORT_ABHOLAUFTRAG_GESAMTPREIS];
			} else if ("Positionsart".equals(fieldName)) {
				value = data[index][REPORT_ABHOLAUFTRAG_POSITIONSART];
			} else if ("Freiertext".equals(fieldName)) {
				value = data[index][REPORT_ABHOLAUFTRAG_FREIERTEXT];
			} else if ("Leerzeile".equals(fieldName)) {
				value = data[index][REPORT_ABHOLAUFTRAG_LEERZEILE];
			} else if ("Image".equals(fieldName)) {
				value = data[index][REPORT_ABHOLAUFTRAG_IMAGE];
			} else if ("Seitenumbruch".equals(fieldName)) {
				value = data[index][REPORT_ABHOLAUFTRAG_SEITENUMBRUCH];
			} else if ("F_PREISPEREINHEIT".equals(fieldName)) {
				value = data[index][REPORT_ABHOLAUFTRAG_PREISPEREINHEIT];
			} else if (F_IDENTNUMMER.equals(fieldName)) {
				value = data[index][REPORT_ABHOLAUFTRAG_IDENTNUMMER];
			} else if (F_BEZEICHNUNG.equals(fieldName)) {
				value = data[index][REPORT_ABHOLAUFTRAG_BEZEICHNUNG];
			} else if (F_KURZBEZEICHNUNG.equals(fieldName)) {
				value = data[index][REPORT_ABHOLAUFTRAG_KURZBEZEICHNUNG];
			} else if ("F_LIEFERANT_ARTIKEL_IDENTNUMMER".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][REPORT_ABHOLAUFTRAG_LIEFERANT_ARTIKEL_IDENTNUMMER]);
			} else if ("F_LIEFERANT_ARTIKEL_BEZEICHNUNG".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][REPORT_ABHOLAUFTRAG_LIEFERANT_ARTIKEL_BEZEICHNUNG]);
			} else if ("F_ARTIKEL_HERSTELLER".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][REPORT_ABHOLAUFTRAG_ARTIKEL_HERSTELLER]);
			} else if (F_REFERENZNUMMER.equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][REPORT_ABHOLAUFTRAG_REFERENZNUMMER]);
			} else if (F_ARTIKELKOMMENTAR.equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][REPORT_ABHOLAUFTRAG_ARTIKELKOMMENTAR]);
			} else if (F_ZUSATZBEZEICHNUNG.equals(fieldName)) {
				value = data[index][REPORT_ABHOLAUFTRAG_ZUSATZBEZEICHNUNG];
			} else if (F_ARTIKEL_BAUFORM.equals(fieldName)) {
				value = data[index][REPORT_ABHOLAUFTRAG_BAUFORM];
			} else if (F_ARTIKEL_VERPACKUNGSART.equals(fieldName)) {
				value = data[index][REPORT_ABHOLAUFTRAG_VERPACKUNGSART];
			} else if (F_ARTIKEL_MATERIAL.equals(fieldName)) {
				value = data[index][REPORT_ABHOLAUFTRAG_ARTIKEL_MATERIAL];
			} else if (F_ARTIKEL_BREITE.equals(fieldName)) {
				value = data[index][REPORT_ABHOLAUFTRAG_ARTIKEL_BREITE];
			} else if (F_ARTIKEL_HOEHE.equals(fieldName)) {
				value = data[index][REPORT_ABHOLAUFTRAG_ARTIKEL_HOEHE];
			} else if (F_ARTIKEL_TIEFE.equals(fieldName)) {
				value = data[index][REPORT_ABHOLAUFTRAG_ARTIKEL_TIEFE];
			} else if (F_HERSTELLER_NAME.equals(fieldName)) {
				value = data[index][REPORT_ABHOLAUFTRAG_ARTIKEL_HERSTELLER_NAME];
			} else if ("F_POSITIONSOBJEKT".equals(fieldName)) {
				value = data[index][REPORT_ABHOLAUFTRAG_POSITIONOBKJEKT];
			} else if ("F_OFFENERAHMENMENGE".equals(fieldName)) {
				value = data[index][REPORT_ABHOLAUFTRAG_OFFENERAHMENMENGE];
			} else if ("F_ANGEBOTSNUMMER".equals(fieldName)) {
				value = data[index][REPORT_ABHOLAUFTRAG_ANGEBOTSNUMMER];
			}
			break;
		}

		return value;
	}

	public JasperPrintLP printBestellungenAlle(ReportJournalKriterienDto krit,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Session session = null;
		try {
			this.useCase = UC_ALLE;
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();

			boolean darfEinkaufspreisSehen = getTheJudgeFac().hatRecht(
					RechteFac.RECHT_LP_DARF_PREISE_SEHEN_EINKAUF, theClientDto);

			// reportjournal: 00 Erzeugen einer Hibernate-Criteria-Query
			Criteria c = session.createCriteria(FLRBestellung.class);
			// reportjournal: 01 Filter nach Mandant
			c.add(Restrictions.eq(BestellungFac.FLR_BESTELLUNG_MANDANT_C_NR,
					theClientDto.getMandant()));
			// reportjournal: 02 Filter: nur eine Kostenstelle
			if (krit.kostenstelleIId != null) {
				c.add(Restrictions.eq(
						BestellungFac.FLR_BESTELLUNG_KOSTENSTELLE_I_ID,
						krit.kostenstelleIId));
			}
			// reportjournal: 03 Filter: nur ein Lieferant
			if (krit.lieferantIId != null) {
				c.add(Restrictions
						.eq(BestellungFac.FLR_BESTELLUNG_LIEFERANT_I_ID_BESTELLADRESSE,
								krit.lieferantIId));
			}
			String sVon = null;
			String sBis = null;
			// reportjournal: 04 Datum von
			if (krit.dVon != null) {
				c.add(Restrictions.ge(
						BestellungFac.FLR_BESTELLUNG_T_LIEFERTERMIN,
						Helper.cutDate(krit.dVon)));
				sVon = Helper.formatDatum(krit.dVon, theClientDto.getLocUi());
			}
			// reportjournal: 05 Datum bis
			if (krit.dBis != null) {
				c.add(Restrictions.lt(
						BestellungFac.FLR_BESTELLUNG_T_LIEFERTERMIN,
						Helper.cutDate(Helper.addiereTageZuDatum(krit.dBis, 1))));
				sBis = Helper.formatDatum(krit.dBis, theClientDto.getLocUi());
			}
			// reportjournalbelegnummer: 0 dazu muss ich das Belegnummernformat
			// und das
			// aktuelle Geschaeftsjahr des Mandanten kennen.
			LpBelegnummerFormat f = getBelegnummerGeneratorObj()
					.getBelegnummernFormat(theClientDto.getMandant());
			Integer iGeschaeftsjahr = getParameterFac().getGeschaeftsjahr(
					theClientDto.getMandant());
			String sMandantKuerzel = getParameterFac().getMandantparameter(
					theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_BELEGNUMMER_MANDANTKENNUNG)
					.getCWert();
			// reportjournal: 06 belegnummer von
			// reportjournalbelegnummer: 1 (von) hier funktionierts fast gleich
			// wie bei den Direktfiltern
			if (krit.sBelegnummerVon != null) {
				sVon = HelperServer.getBelegnummernFilterForHibernateCriterias(
						f, iGeschaeftsjahr, sMandantKuerzel,
						krit.sBelegnummerVon);
				c.add(Restrictions.ge(BestellungFac.FLR_BESTELLUNG_C_NR, sVon));
			}
			// reportjournal: 07 belegnummer bis
			// reportjournalbelegnummer: 2 (bis) detto
			if (krit.sBelegnummerBis != null) {
				sBis = HelperServer.getBelegnummernFilterForHibernateCriterias(
						f, iGeschaeftsjahr, sMandantKuerzel,
						krit.sBelegnummerBis);
				c.add(Restrictions.le(BestellungFac.FLR_BESTELLUNG_C_NR, sBis));
			}
			// reportjournal: 08 Sortierung nach Kostenstelle
			if (krit.bSortiereNachKostenstelle) {
				c.createCriteria(BestellungFac.FLR_BESTELLUNG_FLRKOSTENSTELLE)
						.addOrder(Order.asc("c_nr"));
			}
			// reportjournal: 09 Sortierung nach Lieferant
			if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				c.createCriteria(BestellungFac.FLR_BESTELLUNG_FLRLIEFERANT)
						.createCriteria(LieferantFac.FLR_PARTNER)
						.addOrder(
								Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));
			}
			// reportjournal: 10 Sortierung nach Belegnummer
			else if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_BELEGNUMMER) {
				c.addOrder(Order.asc(BestellungFac.FLR_BESTELLUNG_C_NR));
			}

			// reportjournal: 11 Und nun zusammenbauen der Daten
			List<?> list = c.list();
			data = new Object[list.size()][7];
			int i = 0;
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLRBestellung b = (FLRBestellung) iter.next();
				// reportjournal: 12 Fuer die Performance: so wenige
				// ejb-Methoden wie moeglich aufrufen!
				data[i][ALLE_BELEGDATUM] = b.getT_belegdatum();
				data[i][ALLE_BESTELLNUMMER] = b.getC_nr();
				data[i][ALLE_STATUS] = b.getBestellungstatus_c_nr();
				if (darfEinkaufspreisSehen) {
					data[i][ALLE_BESTELLWERT] = b.getN_bestellwert();
				} else {
					data[i][ALLE_BESTELLWERT] = null;
				}
				data[i][ALLE_KOSTENSTELLENUMMER] = b.getFlrkostenstelle() != null ? b
						.getFlrkostenstelle().getC_nr() : null;
				data[i][ALLE_LIEFERANT] = b.getFlrlieferant().getFlrpartner()
						.getC_name1nachnamefirmazeile1();
				data[i][ALLE_LIEFERTERMIN] = b.getT_liefertermin();
				i++;
			}
			Map<String, Object> map = new TreeMap<String, Object>();
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);
			// Waehrung
			map.put(LPReport.P_WAEHRUNG, mandantDto.getWaehrungCNr());
			StringBuffer sSortierung = new StringBuffer();
			// Sortierung nach Kostenstelle
			// reportjournalparameter: 0 nach Kostenstelle
			map.put(LPReport.P_SORTIERENACHKOSTENSTELLE, new Boolean(
					krit.bSortiereNachKostenstelle));
			if (krit.bSortiereNachKostenstelle) {
				sSortierung.append(getTextRespectUISpr("lp.kostenstelle",
						theClientDto.getMandant(), theClientDto.getLocUi()));
				sSortierung.append(", ");
			}
			// Sortierung nach Lieferant
			// reportjournalparameter: 1 nach Lieferanten
			map.put(LPReport.P_SORTIERENACHLIEFERANT,
					new Boolean(
							krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER));
			if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				sSortierung.append(getTextRespectUISpr("lp.lieferant",
						theClientDto.getMandant(), theClientDto.getLocUi()));
			}
			// reportjournalparameter: 2 nach Belegnummer
			else if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_BELEGNUMMER) {
				sSortierung.append(getTextRespectUISpr("bes.bestnr",
						theClientDto.getMandant(), theClientDto.getLocUi()));
			}
			StringBuffer sFilter = new StringBuffer();
			if (sVon != null) {
				sFilter.append(getTextRespectUISpr("lp.von",
						theClientDto.getMandant(), theClientDto.getLocUi()));
				sFilter.append(" " + sVon + " ");
			}
			if (sBis != null) {
				sFilter.append(getTextRespectUISpr("lp.bis",
						theClientDto.getMandant(), theClientDto.getLocUi()));
				sFilter.append(" " + sBis + " ");
			}
			if (krit.kostenstelleIId != null) {
				if (sFilter.length() > 0) {
					sFilter.append(", ");
				}
				KostenstelleDto kstDto = getSystemFac()
						.kostenstelleFindByPrimaryKey(krit.kostenstelleIId);
				sFilter.append(getTextRespectUISpr("lp.kostenstelle",
						theClientDto.getMandant(), theClientDto.getLocUi()));
				sFilter.append(" ");
				sFilter.append(kstDto.getCNr());
			}

			// reportjournalparameter: 3 Uebergabe
			map.put(LPReport.P_SORTIERUNG, sSortierung.toString());
			map.put(LPReport.P_FILTER, sFilter.toString());
			initJRDS(map, BestellungReportFac.REPORT_MODUL,
					BestellungReportFac.REPORT_BESTELLUNGEN_ALLE,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} finally {
			closeSession(session);
		}
		return getReportPrint();
	}

	public JasperPrintLP printBSMahnungAusMahnlauf(Integer bsmahnungIId,
			boolean bMitLogo, TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			this.useCase = UC_BSMAHNUNG;
			data = new Object[1][1];
			BSMahnungDto bsmahnungDto = getBSMahnwesenFac()
					.bsmahnungFindByPrimaryKey(bsmahnungIId);
			return printBSMahnungFuerBestellung(
					bsmahnungDto.getBestellpositionIId(),
					bsmahnungDto.getBestellungIId(),
					bsmahnungDto.getMahnstufeIId(), new Date(bsmahnungDto
							.getTMahndatum().getTime()), bMitLogo, theClientDto);
		} catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, ex);
		}
	}

	public JasperPrintLP printBSMahnungFuerBestellung(
			Integer bestellpositionIId, Integer bestellungIId,
			Integer bsmahnstufeIId, Date dMahndatum, boolean bMitLogo,
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			this.useCase = UC_BSMAHNUNG;
			data = new Object[1][1];
			Map<String, Object> map = new TreeMap<String, Object>();
			BestellungDto bestellungDto = getBestellungFac()
					.bestellungFindByPrimaryKey(bestellungIId);

			BestellpositionDto besposDto = getBestellpositionFac()
					.bestellpositionFindByPrimaryKey(bestellpositionIId);

			LieferantDto lieferantDto = getLieferantFac()
					.lieferantFindByPrimaryKey(
							bestellungDto.getLieferantIIdBestelladresse(),
							theClientDto);
			Locale locDruck = Helper.string2Locale(lieferantDto.getPartnerDto()
					.getLocaleCNrKommunikation());
			AnsprechpartnerDto oAnsprechpartner = null;
			if (bestellungDto.getAnsprechpartnerIId() != null) {
				oAnsprechpartner = getAnsprechpartnerFac()
						.ansprechpartnerFindByPrimaryKey(
								bestellungDto.getAnsprechpartnerIId(),
								theClientDto);
			}

			//
			// data = new Object[1][1];
			//
			//
			// data[0][REPORT_BSSAMMELMAHNUNG_OFFENEMENGE] = item.getBdOffen();
			// data[1][REPORT_BSSAMMELMAHNUNG_WERT] = item.getBdWert();
			// data[2][REPORT_BSSAMMELMAHNUNG_BESTELLPOSITION_BEZEICHNUNG] =
			// item.
			// getSBestellpositionbezeichung();
			// data[3][REPORT_BSSAMMELMAHNUNG_BELEGDATUM] =
			// item.getDBelegdatum();
			// data[4][REPORT_BSSAMMELMAHNUNG_LIEFERTERMIN] =
			// item.getDFaelligkeitsdatum();
			// data[5][REPORT_BSSAMMELMAHNUNG_MAHNSTUFE] = item.getIMahnstufe();
			// data[6][REPORT_BSSAMMELMAHNUNG_BESTELLNUMMER] =
			// item.getSRechnungsnummer();

			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);

			map.put("P_LIEFERANT_ADRESSBLOCK",
					formatAdresseFuerAusdruck(lieferantDto.getPartnerDto(),
							oAnsprechpartner, mandantDto, locDruck));
			map.put("P_LIEFERANT_UID", lieferantDto.getPartnerDto().getCUid());
			PersonalDto oPersonalBenutzer = getPersonalFac()
					.personalFindByPrimaryKey(theClientDto.getIDPersonal(),
							theClientDto);
			PersonalDto oPersonalAnleger = getPersonalFac()
					.personalFindByPrimaryKey(
							bestellungDto.getPersonalIIdAnlegen(), theClientDto);
			map.put("P_UNSER_ZEICHEN", Helper.getKurzzeichenkombi(
					oPersonalBenutzer.getCKurzzeichen(),
					oPersonalAnleger.getCKurzzeichen()));
			map.put("P_DATUM", new java.util.Date(getDate().getTime()));
			map.put("P_BERUECKSICHTIGTBIS", dMahndatum);

			Integer partnerIIdAnsprechpartner = null;
			if (oAnsprechpartner != null) {
				partnerIIdAnsprechpartner = oAnsprechpartner
						.getPartnerIIdAnsprechpartner();
			}

			// belegkommunikation: 2 daten holen
			String sEmail = getPartnerFac()
					.partnerkommFindRespectPartnerAsStringOhneExec(
							partnerIIdAnsprechpartner,
							lieferantDto.getPartnerDto(),
							PartnerFac.KOMMUNIKATIONSART_EMAIL,
							theClientDto.getMandant(), theClientDto);
			String sFax = getPartnerFac()
					.partnerkommFindRespectPartnerAsStringOhneExec(
							partnerIIdAnsprechpartner,
							lieferantDto.getPartnerDto(),
							PartnerFac.KOMMUNIKATIONSART_FAX,
							theClientDto.getMandant(), theClientDto);
			String sTelefon = getPartnerFac()
					.partnerkommFindRespectPartnerAsStringOhneExec(
							partnerIIdAnsprechpartner,
							lieferantDto.getPartnerDto(),
							PartnerFac.KOMMUNIKATIONSART_TELEFON,
							theClientDto.getMandant(), theClientDto);
			// belegkommunikation: 3 daten als parameter an den Report
			// weitergeben
			map.put(LPReport.P_ANSPRECHPARTNEREMAIL, sEmail != null ? sEmail
					: "");
			map.put(LPReport.P_ANSPRECHPARTNERFAX, sFax != null ? sFax : "");
			map.put(LPReport.P_ANSPRECHPARTNERTELEFON,
					sTelefon != null ? sTelefon : "");

			BSMahntextDto bsmahntextDto = getBSMahnwesenFac()
					.bsmahntextFindByMandantLocaleCNr(mandantDto.getCNr(),
							Helper.locale2String(locDruck), bsmahnstufeIId);

			String sPMahnstufe;
			if (bsmahnstufeIId.intValue() == BSMahnwesenFac.MAHNSTUFE_99) {
				sPMahnstufe = "Letzte";
			} else {
				sPMahnstufe = bsmahnstufeIId.toString() + ".";
			}

			map.put("P_MAHNSTUFE", sPMahnstufe);
			map.put("Mandantenadresse", Helper.formatMandantAdresse(mandantDto));

			if (bsmahntextDto != null) {
				map.put("P_MAHNTEXT", Helper
						.formatStyledTextForJasper(bsmahntextDto
								.getXTextinhalt()));
			} else {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_KEINE_MAHNTEXTE_EINGETRAGEN,
						"");
			}
			map.put("P_BESTELLUNGSNUMMER", bestellungDto.getCNr());
			map.put("P_BESTELLUNGSDATUM", bestellungDto.getDBelegdatum());
			map.put("P_BEZEICHNUNG", besposDto.getCBez());
			map.put("P_ZUSATZBEZEICHNUNG", besposDto.getCZusatzbez());
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(
					besposDto.getArtikelIId(), theClientDto);
			map.put("P_LIEFERANT_ARTIKEL_IDENTNUMMER",
					artikelDto.getCArtikelnrhersteller());
			map.put("P_IDENT", artikelDto.getCNr());
			map.put("P_ARTIKELCZBEZ2", artikelDto.formatBezeichnung());
			ArtikellieferantDto artLiefDto = getArtikelFac()
					.artikellieferantFindByArtikellIIdLieferantIIdOhneExc(
							besposDto.getArtikelIId(), lieferantDto.getIId(),
							theClientDto);
			if (artLiefDto != null) {
				map.put("P_LIEFERANT_ARTIKEL_BEZEICHNUNG",
						artLiefDto.getCBezbeilieferant());
			}
			BigDecimal bdOffeneMenge = getBestellpositionFac()
					.berechneOffeneMenge(besposDto);
			map.put("P_OFFENEMENGE", bdOffeneMenge);
			map.put("P_EINHEIT",
					getSystemFac().formatEinheit(besposDto.getEinheitCNr(),
							locDruck, theClientDto));
			map.put("P_BSWERT",
					bdOffeneMenge.multiply(besposDto.getNNettogesamtpreis()));

			if (besposDto.getTUebersteuerterLiefertermin() != null) {
				map.put("P_LIEFERTERMIN", new Date(besposDto
						.getTUebersteuerterLiefertermin().getTime()));
			} else {
				map.put("P_LIEFERTERMIN", bestellungDto.getDLiefertermin());
			}

			if (besposDto.getTAuftragsbestaetigungstermin() != null) {
				map.put("P_ABTERMIN", new Date(besposDto
						.getTAuftragsbestaetigungstermin().getTime()));
			} else {
				map.put("P_ABTERMIN", null);
			}

			BSMahnstufeDto bsmahnstufeDto = getBSMahnwesenFac()
					.bsmahnstufeFindByPrimaryKeyOhneExc(
							new BsmahnstufePK(bsmahnstufeIId, mandantDto
									.getCNr()));
			map.put("P_MAHNSTUFE_TAGE", bsmahnstufeDto.getITage());

			map.put(LPReport.P_WAEHRUNG, mandantDto.getWaehrungCNr());

			map.put("P_ABSENDER", mandantDto.getPartnerDto().formatAnrede());
			map.put("P_BENUTZERNAME", oPersonalBenutzer.getPartnerDto()
					.formatAnrede());
			map.put("P_MITLOGO", bMitLogo);

			initJRDS(map, BestellungReportFac.REPORT_MODUL,

			BestellungReportFac.REPORT_BSMAHNUNG, theClientDto.getMandant(),
					locDruck, theClientDto, bMitLogo, null);

			return getReportPrint();

		} catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, ex);
		}
	}

	public JasperPrintLP[] printBSSammelMahnung(Integer bsmahnlaufIId,
			boolean bMitLogo, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException {
		Session session = null;
		try {
			HashMap<Integer, Integer> hm = getAllLieferantenFromMahnlauf(bsmahnlaufIId);
			Collection<JasperPrintLP> c = new LinkedList<JasperPrintLP>();
			for (Iterator<Integer> iter = hm.keySet().iterator(); iter
					.hasNext();) {
				ArrayList<JasperPrintLP> print = printBSSammelMahnung(
						bsmahnlaufIId, (Integer) iter.next(), theClientDto,
						false, bMitLogo);

				if (print != null) {
					for (int i = 0; i < print.size(); i++) {
						c.add(print.get(i));
					}
				}

			}
			JasperPrintLP[] prints = new JasperPrintLP[c.size()];
			int i = 0;
			for (Iterator<JasperPrintLP> iter = c.iterator(); iter.hasNext(); i++) {
				JasperPrintLP item = (JasperPrintLP) iter.next();
				prints[i] = item;
			}
			return prints;
		} finally {
			closeSession(session);
		}
	}

	private HashMap<Integer, Integer> getAllLieferantenFromMahnlauf(
			Integer bsmahnlaufIId) {
		Session session = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		session = factory.openSession();
		Criteria crit = session.createCriteria(FLRBSMahnung.class);
		crit.add(Restrictions.eq(BSMahnwesenFac.FLR_MAHNUNG_MAHNLAUF_I_ID,
				bsmahnlaufIId));
		List<?> list = crit.list();
		HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
		// alle lieferantenIds in eine hashmap -> quasi distinct
		for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
			FLRBSMahnung item = (FLRBSMahnung) iter.next();
			hm.put(item.getFlrbestellung().getFlrlieferant().getI_id(), item
					.getFlrbestellung().getFlrlieferant().getI_id());
		}
		return hm;
	}

	public ArrayList<JasperPrintLP> getMahnungenFuerAlleLieferanten(
			Integer bsmahnlaufIId, TheClientDto theClientDto,
			boolean bNurNichtGemahnte, boolean bMitLogo) throws EJBExceptionLP,
			RemoteException {
		HashMap<Integer, Integer> hm = getAllLieferantenFromMahnlauf(bsmahnlaufIId);
		ArrayList<JasperPrintLP> hmPrints = new ArrayList<JasperPrintLP>();
		for (Iterator<Integer> iter = hm.keySet().iterator(); iter.hasNext();) {
			Integer iLieferantIId = (Integer) iter.next();
			ArrayList<JasperPrintLP> print = null;

			try {
				print = printBSSammelMahnung(bsmahnlaufIId, iLieferantIId,
						theClientDto, bNurNichtGemahnte, bMitLogo);
			} catch (EJBExceptionLP e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, e);
			} catch (RemoteException e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, e);
			}
			if (print != null) {

				for (int i = 0; i < print.size(); i++)

					hmPrints.add(print.get(i));
			}
		}

		return hmPrints;
	}

	public JasperPrintLP printWepEtikett(Integer iIdWepI,
			Integer iIdBestellpositionI, Integer iIdLagerI, Integer iExemplare,
			Integer iVerpackungseinheit, Double dGewicht,
			String sWarenverkehrsnummer, String sLagerort,
			String sUrsprungsland, String sKommentar, BigDecimal bdHandmenge,
			Integer wePosIId, TheClientDto theClientDto) throws EJBExceptionLP {
		this.useCase = UC_WEP_ETIKETT;
		try {
			WareneingangspositionDto wepDto = null;
			WareneingangDto weDto = null;
			LagerDto lagerDto = null;

			BestellpositionDto bespDto = getBestellpositionFac()
					.bestellpositionFindByPrimaryKey(iIdBestellpositionI);
			if (iIdWepI != null) {
				wepDto = getWareneingangFac()
						.wareneingangspositionFindByPrimaryKey(iIdWepI);
				weDto = getWareneingangFac().wareneingangFindByPrimaryKey(
						wepDto.getWareneingangIId());
			}
			BestellungDto bestellungDto = getBestellungFac()
					.bestellungFindByPrimaryKey(bespDto.getBestellungIId());
			lagerDto = getLagerFac().lagerFindByPrimaryKey(iIdLagerI);
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(
					bespDto.getArtikelIId(), theClientDto);
			ArtikellieferantDto artikellieferantDto = null;
			try {
				artikellieferantDto = getArtikelFac()
						.artikellieferantFindByArtikellIIdLieferantIIdOhneExc(
								artikelDto.getIId(),
								bestellungDto.getLieferantIIdBestelladresse(),
								theClientDto);
			} catch (RemoteException ex) {
				// throwEJBExceptionLPRespectOld(ex);
			}
			LieferantDto lieferantDto = getLieferantFac()
					.lieferantFindByPrimaryKey(
							bestellungDto.getLieferantIIdBestelladresse(),
							theClientDto);
			// es gilt das Locale des Lieferanten
			Locale locDruck = Helper.string2Locale(lieferantDto.getPartnerDto()
					.getLocaleCNrKommunikation());
			int iAnzahlZeilen = 1; // Anzahl der Zeilen in der Gruppe

			data = new Object[iAnzahlZeilen][BestellungReportFac.REPORT_WEP_ETIKETT_ANZAHL_SPALTEN];

			// die Datenmatrix befuellen
			for (int i = 0; i < iAnzahlZeilen; i++) {
				if (wepDto != null) {
					data[i][BestellungReportFac.REPORT_WEP_ETIKETT_ANLIEFERMENGE] = wepDto
							.getNGeliefertemenge();
					data[i][BestellungReportFac.REPORT_WEP_ETIKETT_EINHEIT] = bespDto
							.getEinheitCNr().trim();
					if (Helper.short2boolean(artikelDto.getBChargennrtragend())) {
						data[i][BestellungReportFac.REPORT_WEP_ETIKETT_CHARGENNUMMER] = SeriennrChargennrMitMengeDto
								.erstelleStringAusMehrerenSeriennummern(wepDto
										.getSeriennrChargennrMitMenge());
					}
					if (Helper.short2boolean(artikelDto.getBSeriennrtragend())) {
						data[i][BestellungReportFac.REPORT_WEP_ETIKETT_SERIENNUMMER] = SeriennrChargennrMitMengeDto
								.erstelleStringAusMehrerenSeriennummern(wepDto
										.getSeriennrChargennrMitMenge());
					}
				}
				if (weDto != null) {
					data[i][BestellungReportFac.REPORT_WEP_ETIKETT_WE_DATUM] = Helper
							.formatDatum(Helper.extractDate(weDto
									.getTWareneingangsdatum()), locDruck);
				}
				data[i][BestellungReportFac.REPORT_WEP_ETIKETT_BESTELLNUMMER] = bestellungDto
						.getCNr();
				data[i][BestellungReportFac.REPORT_WEP_ETIKETT_PROJEKTBEZ] = bestellungDto
						.getCBez();
				data[i][BestellungReportFac.REPORT_WEP_ETIKETT_WARENVERKEHRSNUMMER] = sWarenverkehrsnummer;
				data[i][BestellungReportFac.REPORT_WEP_ETIKETT_VERPACKUNGSEINHEIT] = iVerpackungseinheit;
				data[i][BestellungReportFac.REPORT_WEP_ETIKETT_HANDMENGE] = bdHandmenge;
				data[i][BestellungReportFac.REPORT_WEP_ETIKETT_LAGER] = lagerDto
						.getCNr();
				data[i][BestellungReportFac.REPORT_WEP_ETIKETT_LAGERORT] = sLagerort;
				data[i][BestellungReportFac.REPORT_WEP_ETIKETT_URSPRUNGSLAND] = sUrsprungsland;
				data[i][BestellungReportFac.REPORT_WEP_ETIKETT_GEWICHT] = dGewicht;
				data[i][BestellungReportFac.REPORT_WEP_ETIKETT_KOMMENTAR] = sKommentar;
				if (artikellieferantDto != null) {
					data[i][BestellungReportFac.REPORT_WEP_ETIKETT_LIEFERANTENARTIKELNUMMER] = artikellieferantDto
							.getCArtikelnrlieferant();
					data[i][BestellungReportFac.REPORT_WEP_ETIKETT_LIEFERANTENARTIKELBEZ] = artikellieferantDto
							.getCBezbeilieferant();
				}
				data[i][BestellungReportFac.REPORT_WEP_ETIKETT_IDENT] = artikelDto
						.getCNr();
				if (artikelDto.getArtikelsprDto() != null) {
					data[i][BestellungReportFac.REPORT_WEP_ETIKETT_BEZ] = artikelDto
							.getArtikelsprDto().getCBez();
					data[i][BestellungReportFac.REPORT_WEP_ETIKETT_ZBEZ] = artikelDto
							.getArtikelsprDto().getCZbez();
					data[i][BestellungReportFac.REPORT_WEP_ETIKETT_ZBEZ2] = artikelDto
							.getArtikelsprDto().getCZbez2();
				}

				data[i][BestellungReportFac.REPORT_WEP_ETIKETT_WE_REFERENZ] = getLagerFac()
						.getWareneingangsreferenzSubreport(
								LocaleFac.BELEGART_BESTELLUNG, wepDto.getIId(),
								wepDto.getSeriennrChargennrMitMenge(), false,
								theClientDto);

				if (lieferantDto != null) {
					data[i][BestellungReportFac.REPORT_WEP_ETIKETT_LIEFERANTNAME] = lieferantDto
							.getPartnerDto().formatFixName1Name2();
				} else {
					data[i][BestellungReportFac.REPORT_WEP_ETIKETT_LIEFERANTNAME] = "";
				}
				HerstellerDto hersteller = null;
				if (artikelDto.getHerstellerIId() != null) {
					hersteller = getArtikelFac().herstellerFindByPrimaryKey(
							artikelDto.getHerstellerIId(), theClientDto);
				}
				if (hersteller != null) {
					data[i][BestellungReportFac.REPORT_WEP_ETIKETT_HERSTELLER] = hersteller
							.getCNr();
					if (hersteller.getPartnerDto() != null) {
						data[i][BestellungReportFac.REPORT_WEP_ETIKETT_HERSTELLERNAME] = hersteller
								.getPartnerDto().formatAnrede();
					}
				} else {
					data[i][BestellungReportFac.REPORT_WEP_ETIKETT_HERSTELLER] = "";
					data[i][BestellungReportFac.REPORT_WEP_ETIKETT_HERSTELLERNAME] = "";
				}
			}
			Map<String, Object> parameter = new TreeMap<String, Object>();
			initJRDS(parameter, BestellungReportFac.REPORT_MODUL,
					BestellungReportFac.REPORT_WEP_ETIKETT,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);
			return getReportPrint();
		}

		catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, ex);
		}

	}

	public ArrayList<JasperPrintLP> printBSSammelMahnung(Integer bsmahnlaufIId,
			Integer lieferantIId, TheClientDto theClientDto,
			boolean bNurNichtGemahnte, boolean bMitLogo) throws EJBExceptionLP,
			RemoteException {

		ArrayList alBestellungen = new ArrayList<JasperPrintLP>();

		try {
			this.useCase = UC_BSSAMMELMAHNUNG;
			// TODO create following query to get bsmahnungen:
			// SELECT * FROM BES_BSMAHNUNG INNER JOIN BES_BESTELLUNG ON
			// BES_BSMAHNUNG.BESTELLUNG_I_ID = BES_BESTELLUNG.I_ID WHERE
			// LIEFERANT_I_ID_BESTELLADRESSE = lieferantIId AND BSMAHNLAUF_I_ID
			// = bsmahnlaufIId
			// AND BES_BSMAHNUNG.MANDANT_C_NR = theClientDto.getMandant()
			// BSMahnungDto[] bsmahnungen =
			// getBSMahnwesenFac().bsmahnungFindByBSMahnlaufIId(
			// bsmahnlaufIId, theClientDto);

			BSMahnungDto[] bsmahnungen = getBSMahnwesenFac()
					.bsmahnungFindByBSMahnlaufIIdLieferantIId(bsmahnlaufIId,
							lieferantIId, theClientDto.getMandant());
			if (bNurNichtGemahnte) {
				Collection<BSMahnungDto> cMahnungen = new LinkedList<BSMahnungDto>();
				for (int y = 0; y < bsmahnungen.length; y++) {
					if (bsmahnungen[y].getTGedruckt() == null) {
						cMahnungen.add(bsmahnungen[y]);
					}
				}
				bsmahnungen = new BSMahnungDto[cMahnungen.size()];
				bsmahnungen = cMahnungen.toArray(bsmahnungen);
			}

			boolean bAbsenderIstAnforderer = false;
			try {
				ParametermandantDto parameter = (ParametermandantDto) getParameterFac()
						.getMandantparameter(theClientDto.getMandant(),
								ParameterFac.KATEGORIE_BESTELLUNG,
								ParameterFac.PARAMETER_MAHNUNGSABSENDER);

				int iWert = ((Integer) parameter.getCWertAsObject()).intValue();

				if (iWert == 1) {
					bAbsenderIstAnforderer = true;
				}

			} catch (RemoteException ex5) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex5);
			}

			HashMap hmSammelmahnungProAnsprechpartner = new HashMap();

			Integer partnerIIdErsterAnsprechpartner = null;

			Integer iMaxMahnstufe = null;
			if (bsmahnungen != null && bsmahnungen.length > 0) {
				for (int i = 0; i < bsmahnungen.length; i++) {
					BestellungDto bestellungDto = null;
					bestellungDto = getBestellungFac()
							.bestellungFindByPrimaryKey(
									bsmahnungen[i].getBestellungIId());

					AnsprechpartnerDto oAnsprechpartner = null;

					if (bestellungDto.getAnsprechpartnerIId() != null) {
						oAnsprechpartner = getAnsprechpartnerFac()
								.ansprechpartnerFindByPrimaryKey(
										bestellungDto.getAnsprechpartnerIId(),
										theClientDto);
					}

					if (oAnsprechpartner != null && i == 0) {
						partnerIIdErsterAnsprechpartner = oAnsprechpartner
								.getPartnerIIdAnsprechpartner();
					}

					BestellpositionDto besposDto;
					Integer lieferantIIdMahnung = bestellungDto
							.getLieferantIIdBestelladresse();
					// if (lieferantIIdMahnung.equals(lieferantIId)) {
					if (iMaxMahnstufe == null) {
						iMaxMahnstufe = bsmahnungen[i].getMahnstufeIId();
					} else {
						if (bsmahnungen[i].getMahnstufeIId().intValue() > iMaxMahnstufe
								.intValue()) {
							iMaxMahnstufe = bsmahnungen[i].getMahnstufeIId();
						}
					}
					besposDto = getBestellpositionFac()
							.bestellpositionFindByPrimaryKey(
									bsmahnungen[i].getBestellpositionIId());

					String key = bestellungDto.getAnsprechpartnerIId() + "";

					if (bAbsenderIstAnforderer == true) {
						if (bestellungDto.getPersonalIIdAnforderer() != null) {
							key += " "
									+ bestellungDto.getPersonalIIdAnforderer();
						}
					}

					if (hmSammelmahnungProAnsprechpartner.containsKey(key)) {

						Collection<BSSammelmahnungDto> c = (Collection<BSSammelmahnungDto>) hmSammelmahnungProAnsprechpartner
								.get(key);
						BSSammelmahnungDto smDto = new BSSammelmahnungDto();
						BigDecimal bdOffeneMenge = getBestellpositionFac()
								.berechneOffeneMenge(besposDto);
						smDto.setBdOffen(bdOffeneMenge);
						smDto.setAnsprechpartnerIId(bestellungDto
								.getAnsprechpartnerIId());
						smDto.setPersonalIIdAnforderer(bestellungDto
								.getPersonalIIdAnforderer());
						smDto.setsBestellnummern(bestellungDto.getCNr());
						smDto.setBdWert(besposDto.getNNettoeinzelpreis()
								.multiply(bdOffeneMenge));
						// BSMahnstufeDto bsmahnstufeDto = getBSMahnwesenFac().
						// bsmahnstufeFindByPrimaryKeyOhneExc(new
						// BSMahnstufePK(bsmahnungen[i].
						// getMahnstufeIId(), theClientDto.getMandant()));
						smDto.setDBelegdatum(bestellungDto.getDBelegdatum());
						/*
						 * Date dZieldatum = new
						 * Date(bestellungDto.getDBelegdatum() .getTime()); if
						 * (bestellungDto.getZahlungszielIId() != null) {
						 * ZahlungszielDto zzDto = getMandantFac()
						 * .zahlungszielFindByPrimaryKey(
						 * bestellungDto.getZahlungszielIId(), theClientDto);
						 * dZieldatum = Helper.addiereTageZuDatum(dZieldatum,
						 * zzDto .getAnzahlZieltageFuerNetto().intValue()); }
						 */
						smDto.setDFaelligkeitsdatum(/* dZieldatum */besposDto
								.getTUebersteuerterLiefertermin());
						smDto.setDABTermin(besposDto
								.getTAuftragsbestaetigungstermin());
						smDto.setIMahnstufe(bsmahnungen[i].getMahnstufeIId());
						smDto.setSRechnungsnummer(bestellungDto.getCNr());
						smDto.setSBestellpositionbezeichung(besposDto.getCBez());
						ArtikelDto artikelDto = getArtikelFac()
								.artikelFindByPrimaryKey(
										besposDto.getArtikelIId(), theClientDto);
						smDto.setSIdentnummer(artikelDto.getCNr());
						ArtikellieferantDto artLiefDto = getArtikelFac()
								.artikellieferantFindByArtikellIIdLieferantIIdOhneExc(
										besposDto.getArtikelIId(),
										lieferantIIdMahnung, theClientDto);
						smDto.setSHerstellerIdentnummer(artikelDto
								.getCArtikelnrhersteller());
						if (artLiefDto != null) {
							smDto.setSHerstellerbezeichnung(artLiefDto
									.getCBezbeilieferant());
						} else {
							smDto.setSHerstellerbezeichnung(null);
						}
						if (artikelDto.getHerstellerIId() != null) {
							smDto.setSArtikelhersteller(getArtikelFac()
									.herstellerFindByPrimaryKey(
											artikelDto.getHerstellerIId(),
											theClientDto).getCNr());
						} else {
							smDto.setSArtikelhersteller("");
						}
						smDto.setSReferenznummer(artikelDto.getCReferenznr());
						smDto.setSArtikelbez(artikelDto
								.formatArtikelbezeichnung());
						c.add(smDto);

						hmSammelmahnungProAnsprechpartner.put(key, c);

					} else {
						Collection<BSSammelmahnungDto> c = new LinkedList<BSSammelmahnungDto>();
						BSSammelmahnungDto smDto = new BSSammelmahnungDto();
						BigDecimal bdOffeneMenge = getBestellpositionFac()
								.berechneOffeneMenge(besposDto);
						smDto.setBdOffen(bdOffeneMenge);
						smDto.setsBestellnummern(bestellungDto.getCNr());
						smDto.setBdWert(besposDto.getNNettoeinzelpreis()
								.multiply(bdOffeneMenge));
						// BSMahnstufeDto bsmahnstufeDto = getBSMahnwesenFac().
						// bsmahnstufeFindByPrimaryKeyOhneExc(new
						// BSMahnstufePK(bsmahnungen[i].
						// getMahnstufeIId(), theClientDto.getMandant()));
						smDto.setDBelegdatum(bestellungDto.getDBelegdatum());
						smDto.setAnsprechpartnerIId(bestellungDto
								.getAnsprechpartnerIId());
						smDto.setPersonalIIdAnforderer(bestellungDto
								.getPersonalIIdAnforderer());
						smDto.setDFaelligkeitsdatum(/* dZieldatum */besposDto
								.getTUebersteuerterLiefertermin());
						smDto.setDABTermin(besposDto
								.getTAuftragsbestaetigungstermin());
						smDto.setIMahnstufe(bsmahnungen[i].getMahnstufeIId());
						smDto.setSRechnungsnummer(bestellungDto.getCNr());
						smDto.setSBestellpositionbezeichung(besposDto.getCBez());
						ArtikelDto artikelDto = getArtikelFac()
								.artikelFindByPrimaryKey(
										besposDto.getArtikelIId(), theClientDto);
						smDto.setSIdentnummer(artikelDto.getCNr());
						ArtikellieferantDto artLiefDto = getArtikelFac()
								.artikellieferantFindByArtikellIIdLieferantIIdOhneExc(
										besposDto.getArtikelIId(),
										lieferantIIdMahnung, theClientDto);
						smDto.setSHerstellerIdentnummer(artikelDto
								.getCArtikelnrhersteller());
						if (artLiefDto != null) {
							smDto.setSHerstellerbezeichnung(artLiefDto
									.getCBezbeilieferant());
						} else {
							smDto.setSHerstellerbezeichnung(null);
						}
						if (artikelDto.getHerstellerIId() != null) {
							smDto.setSArtikelhersteller(getArtikelFac()
									.herstellerFindByPrimaryKey(
											artikelDto.getHerstellerIId(),
											theClientDto).getCNr());
						} else {
							smDto.setSArtikelhersteller("");
						}
						smDto.setSReferenznummer(artikelDto.getCReferenznr());
						smDto.setSArtikelbez(artikelDto
								.formatArtikelbezeichnung());
						c.add(smDto);

						hmSammelmahnungProAnsprechpartner.put(key, c);

					}

					// }
				}

				for (Iterator iterA = hmSammelmahnungProAnsprechpartner
						.keySet().iterator(); iterA.hasNext();) {

					String key = (String) iterA.next();

					HashMap hmBestellnummern = new HashMap();

					Collection<BSSammelmahnungDto> c = (Collection<BSSammelmahnungDto>) hmSammelmahnungProAnsprechpartner
							.get(key);

					Integer anspIId = null;
					Integer personalIIdAnforderer = null;
					data = new Object[c.size()][REPORT_BSSAMMELMAHNUNG_SIZE];
					int i = 0;
					for (Iterator<BSSammelmahnungDto> iter = c.iterator(); iter
							.hasNext();) {
						BSSammelmahnungDto item = (BSSammelmahnungDto) iter
								.next();
						anspIId = item.getAnsprechpartnerIId();
						if (bAbsenderIstAnforderer == true) {
							personalIIdAnforderer = item
									.getPersonalIIdAnforderer();
						}
						data[i][REPORT_BSSAMMELMAHNUNG_OFFENEMENGE] = item
								.getBdOffen();
						data[i][REPORT_BSSAMMELMAHNUNG_WERT] = item.getBdWert();
						data[i][REPORT_BSSAMMELMAHNUNG_BESTELLPOSITION_BEZEICHNUNG] = item
								.getSBestellpositionbezeichung();
						data[i][REPORT_BSSAMMELMAHNUNG_BELEGDATUM] = new java.util.Date(
								item.getDBelegdatum().getTime());

						if (!hmBestellnummern.containsKey(item
								.getsBestellnummern())) {
							hmBestellnummern.put(item.getsBestellnummern(),
									item.getsBestellnummern());
						}

						data[i][REPORT_BSSAMMELMAHNUNG_LIEFERTERMIN] = new java.util.Date(
								item.getDFaelligkeitsdatum().getTime());
						if (item.getDABTermin() != null) {
							data[i][REPORT_BSSAMMELMAHNUNG_ABTERMIN] = new java.util.Date(
									item.getDABTermin().getTime());
						} else {
							data[i][REPORT_BSSAMMELMAHNUNG_ABTERMIN] = null;
						}
						data[i][REPORT_BSSAMMELMAHNUNG_MAHNSTUFE] = item
								.getIMahnstufe();
						data[i][REPORT_BSSAMMELMAHNUNG_BESTELLNUMMER] = item
								.getSRechnungsnummer();
						data[i][REPORT_BSSAMMELMAHNUNG_IDENTNUMMER] = item
								.getSIdentnummer();
						data[i][REPORT_BSSAMMELMAHNUNG_LIEFERANT_IDENTNUMMER] = item
								.getSHerstellerIdentnummer();
						data[i][REPORT_BSSAMMELMAHNUNG_LIEFERANT_ARTIKEL_BEZEICHNUNG] = item
								.getSHerstellerbezeichnung();
						data[i][REPORT_BSSAMMELMAHNUNG_ARTIKEL_HERSTELLER] = item
								.getSArtikelhersteller();
						data[i][REPORT_BSSAMMELMAHNUNG_REFERENZNUMMER] = item
								.getSReferenznummer();
						data[i][REPORT_BSSAMMELMAHNUNG_ARTIKELCZBEZ2] = item
								.getSArtikelbez();
						i++;
					}
					Map<String, Object> map = new TreeMap<String, Object>();
					LieferantDto lieferantDto = getLieferantFac()
							.lieferantFindByPrimaryKey(lieferantIId,
									theClientDto);
					Locale locDruck = Helper.string2Locale(lieferantDto
							.getPartnerDto().getLocaleCNrKommunikation());
					MandantDto mandantDto = getMandantFac()
							.mandantFindByPrimaryKey(theClientDto.getMandant(),
									theClientDto);

					if (anspIId != null) {
						AnsprechpartnerDto ansprechpartnerDto = getAnsprechpartnerFac()
								.ansprechpartnerFindByPrimaryKey(anspIId,
										theClientDto);

						map.put("P_LIEFERANT_ADRESSBLOCK",
								formatAdresseFuerAusdruck(
										lieferantDto.getPartnerDto(),
										ansprechpartnerDto, mandantDto,
										locDruck));
					} else {
						map.put("P_LIEFERANT_ADRESSBLOCK",
								formatAdresseFuerAusdruck(
										lieferantDto.getPartnerDto(), null,
										mandantDto, locDruck));
					}

					// belegkommunikation: 2 daten holen
					String sEmail = getPartnerFac()
							.partnerkommFindRespectPartnerAsStringOhneExec(
									partnerIIdErsterAnsprechpartner,
									lieferantDto.getPartnerDto(),
									PartnerFac.KOMMUNIKATIONSART_EMAIL,
									theClientDto.getMandant(), theClientDto);
					String sFax = getPartnerFac()
							.partnerkommFindRespectPartnerAsStringOhneExec(
									partnerIIdErsterAnsprechpartner,
									lieferantDto.getPartnerDto(),
									PartnerFac.KOMMUNIKATIONSART_FAX,
									theClientDto.getMandant(), theClientDto);
					String sTelefon = getPartnerFac()
							.partnerkommFindRespectPartnerAsStringOhneExec(
									partnerIIdErsterAnsprechpartner,
									lieferantDto.getPartnerDto(),
									PartnerFac.KOMMUNIKATIONSART_TELEFON,
									theClientDto.getMandant(), theClientDto);
					// belegkommunikation: 3 daten als parameter an den Report
					// weitergeben
					map.put(LPReport.P_ANSPRECHPARTNEREMAIL,
							sEmail != null ? sEmail : "");
					map.put(LPReport.P_ANSPRECHPARTNERFAX, sFax != null ? sFax
							: "");
					map.put(LPReport.P_ANSPRECHPARTNERTELEFON,
							sTelefon != null ? sTelefon : "");

					map.put("P_LIEFERANT_UID", lieferantDto.getPartnerDto()
							.getCUid());
					PersonalDto oPersonalBenutzer = getPersonalFac()
							.personalFindByPrimaryKey(
									theClientDto.getIDPersonal(), theClientDto);
					map.put("P_UNSER_ZEICHEN", Helper.getKurzzeichenkombi(
							oPersonalBenutzer.getCKurzzeichen(),
							oPersonalBenutzer.getCKurzzeichen()));
					BSMahnlaufDto mahnlaufDto = getBSMahnwesenFac()
							.bsmahnlaufFindByPrimaryKey(bsmahnlaufIId);
					map.put("P_BERUECKSICHTIGTBIS", new java.util.Date(
							mahnlaufDto.getTAnlegen().getTime()));
					map.put("P_DATUM", new java.util.Date(getDate().getTime()));

					BSMahntextDto bsmahntextDto = getBSMahnwesenFac()
							.bsmahntextFindByMandantLocaleCNr(
									mandantDto.getCNr(),
									Helper.locale2String(locDruck),
									iMaxMahnstufe);

					if (bsmahntextDto != null) {
						map.put("P_MAHNTEXT", Helper
								.formatStyledTextForJasper(bsmahntextDto
										.getXTextinhalt()));
					} else {

						ArrayList al = new ArrayList();
						al.add(lieferantDto.getPartnerDto().formatAnrede()
								+ " (" + Helper.locale2String(locDruck).trim()
								+ ")");
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_FINANZ_KEINE_MAHNTEXTE_EINGETRAGEN,
								al,
								new Exception(
										"FEHLER_FINANZ_KEINE_MAHNTEXTE_EINGETRAGEN"));
					}

					BSMahnstufeDto bsmahnstufeDto = getBSMahnwesenFac()
							.bsmahnstufeFindByPrimaryKeyOhneExc(
									new BsmahnstufePK(iMaxMahnstufe,
											theClientDto.getMandant()));
					map.put("P_MAXMAHNSTUFE", bsmahnstufeDto.getIId());
					map.put("P_MAXMAHNSTUFE_TAGE", bsmahnstufeDto.getITage());

					map.put("P_BERUECKSICHTIGTBIS",
							new java.sql.Date(System.currentTimeMillis()));
					map.put(LPReport.P_WAEHRUNG, mandantDto.getWaehrungCNr());
					map.put("P_ABSENDER", mandantDto.getPartnerDto()
							.formatAnrede());
					map.put("P_BENUTZERNAME", oPersonalBenutzer.getPartnerDto()
							.formatAnrede());
					map.put("Mandantenadresse",
							Helper.formatMandantAdresse(mandantDto));
					map.put("P_MITLOGO", bMitLogo);
					initJRDS(map, BestellungReportFac.REPORT_MODUL,
							BestellungReportFac.REPORT_BSSAMMELMAHNUNG,
							theClientDto.getMandant(), locDruck, theClientDto,
							bMitLogo, null);

					JasperPrintLP print = getReportPrint();
					print.putAdditionalInformation("ansprechpartnerIId",
							anspIId);
					print.putAdditionalInformation("personalIIdAnforderer",
							personalIIdAnforderer);
					print.putAdditionalInformation("lieferantIId", lieferantIId);
					print.putAdditionalInformation("bestellnummern",
							hmBestellnummern);

					alBestellungen.add(print);
				}
				return alBestellungen;
			}
			return null;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}

	}

	/**
	 * String zum Andrucken der Sortierkriterien.
	 * 
	 * @param reportJournalKriterienDtoI
	 *            die Kriterien
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return String die Sortierkriterien
	 */
	private String buildSortierungBestellungOffene(
			ReportJournalKriterienDto reportJournalKriterienDtoI,
			TheClientDto theClientDto) {
		StringBuffer buff = new StringBuffer(getTextRespectUISpr(
				"lp.sortierungnach", theClientDto.getMandant(),
				theClientDto.getLocUi()));
		buff.append(" ");

		if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_BELEGNUMMER) {
			buff.append(getTextRespectUISpr("bes.bestnr",
					theClientDto.getMandant(), theClientDto.getLocUi()));
		}
		if (reportJournalKriterienDtoI.bSortiereNachKostenstelle) {
			buff.append(
					getTextRespectUISpr("lp.kostenstelle",
							theClientDto.getMandant(), theClientDto.getLocUi()))
					.append(", ");
		}

		if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
			buff.append(
					getTextRespectUISpr("lp.lieferant",
							theClientDto.getMandant(), theClientDto.getLocUi()))
					.append(", ");
		}
		if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_IDENT) {
			buff.append(
					getTextRespectUISpr("bes.offene.lieferantartikel",
							theClientDto.getMandant(), theClientDto.getLocUi()))
					.append(", ");
		}

		if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PROJEKT) {
			buff.append(getTextRespectUISpr("lp.projekt",
					theClientDto.getMandant(), theClientDto.getLocUi()));
		}
		if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PROJEKT) {
			buff.append(getTextRespectUISpr("lp.projekt",
					theClientDto.getMandant(), theClientDto.getLocUi()));
		}
		if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_ART) {
			buff.append(getTextRespectUISpr("bes.bestellungsart",
					theClientDto.getMandant(), theClientDto.getLocUi()));
		}
		return buff.toString();
	}

	private String buildSortierungBestellvorschlag(
			ReportJournalKriterienDto reportJournalKriterienDtoI,
			TheClientDto theClientDto) {
		StringBuffer buff = new StringBuffer(getTextRespectUISpr(
				"lp.sortierungnach", theClientDto.getMandant(),
				theClientDto.getLocUi()));
		buff.append(" ");

		if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_BELEGNUMMER) {
			buff.append(getTextRespectUISpr("bes.bestnr",
					theClientDto.getMandant(), theClientDto.getLocUi()));
		}
		if (reportJournalKriterienDtoI.bSortiereNachKostenstelle) {
			buff.append(
					getTextRespectUISpr("lp.kostenstelle",
							theClientDto.getMandant(), theClientDto.getLocUi()))
					.append(", ");
		}

		if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
			buff.append(
					getTextRespectUISpr("lp.lieferant",
							theClientDto.getMandant(), theClientDto.getLocUi()))
					.append(", ");
		}
		if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_IDENT) {
			buff.append(
					getTextRespectUISpr("lp.artikel",
							theClientDto.getMandant(), theClientDto.getLocUi()))
					.append(", ");
		}

		if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PROJEKT) {
			buff.append(getTextRespectUISpr("lp.projekt",
					theClientDto.getMandant(), theClientDto.getLocUi()));
		}
		if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PROJEKT) {
			buff.append(getTextRespectUISpr("lp.projekt",
					theClientDto.getMandant(), theClientDto.getLocUi()));
		}
		if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_ART) {
			buff.append(getTextRespectUISpr("bes.bestellungsart",
					theClientDto.getMandant(), theClientDto.getLocUi()));
		}
		return buff.toString();
	}

	/**
	 * String zum Andrucken der Filterkriterien.
	 * 
	 * @param reportJournalKriterienDtoI
	 *            die Kriterien
	 * @param artikelklasseIId
	 *            Integer
	 * @param artikelgruppeIId
	 *            Integer
	 * @param artikelCNrVon
	 *            String
	 * @param artikelCNrBis
	 *            String
	 * @param projektCBezeichnung
	 *            String
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return String die Filterkriterien
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	private String buildFilterBestellungOffene(
			ReportJournalKriterienDto reportJournalKriterienDtoI,
			Integer artikelklasseIId, Integer artikelgruppeIId,
			String artikelCNrVon, String artikelCNrBis,
			String projektCBezeichnung, TheClientDto theClientDto)

	throws EJBExceptionLP {
		StringBuffer buff = new StringBuffer("");

		try {

			// Kostenstelle
			if (reportJournalKriterienDtoI.kostenstelleIId != null) {
				buff.append(" ").append(
						getTextRespectUISpr("lp.kostenstelle",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
				buff.append(" ").append(
						getSystemFac().kostenstelleFindByPrimaryKey(
								reportJournalKriterienDtoI.kostenstelleIId)
								.getCNr());
			}

			// Belegdatum
			if (reportJournalKriterienDtoI.dVon != null
					|| reportJournalKriterienDtoI.dBis != null) {
				buff.append(getTextRespectUISpr("bes.belegdatum",
						theClientDto.getMandant(), theClientDto.getLocUi()));
			}

			// Lieferant
			if (reportJournalKriterienDtoI.lieferantIId != null) {
				buff.append(" ").append(
						getTextRespectUISpr("lp.lieferant",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
				buff.append(" ")
						.append(getLieferantFac()
								.lieferantFindByPrimaryKey(
										reportJournalKriterienDtoI.lieferantIId,
										theClientDto).getPartnerDto()
								.getCName1nachnamefirmazeile1());
			}

			if (projektCBezeichnung != null) {
				buff.append(" ").append(
						getTextRespectUISpr("lp.projektbezeichnung",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
				buff.append(" ").append(projektCBezeichnung);
			}

			if (artikelklasseIId != null) {
				buff.append(" ").append(
						getTextRespectUISpr("lp.artikelklasse",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
				ArtklaDto artikelklasseDto = getArtikelFac()
						.artklaFindByPrimaryKey(artikelklasseIId, theClientDto);
				if (artikelklasseDto.getArtklasprDto() != null) {
					buff.append(" ").append(
							artikelklasseDto.getArtklasprDto().getCBez());
				}
			}

			if (artikelgruppeIId != null) {
				buff.append(" ").append(
						getTextRespectUISpr("lp.artikelgruppe",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
				ArtgruDto artikelgruppeDto = getArtikelFac()
						.artgruFindByPrimaryKey(artikelgruppeIId, theClientDto);
				if (artikelgruppeDto.getArtgrusprDto() != null) {
					buff.append(" ").append(
							artikelgruppeDto.getArtgrusprDto().getCBez());
				}
			}
		} catch (RemoteException re) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, re);
		}
		return buff.toString().trim();
	}

	/**
	 * String zum Andrucken der Filterkriterien.
	 * 
	 * @param reportJournalKriterienDtoI
	 *            die Kriterien
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return String die Filterkriterien
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	private String buildFilterBestellungOffene(
			ReportJournalKriterienDto reportJournalKriterienDtoI,

			TheClientDto theClientDto)

	throws EJBExceptionLP {
		StringBuffer buff = new StringBuffer("");
		// Belegdatum
		if (reportJournalKriterienDtoI.dVon != null
				|| reportJournalKriterienDtoI.dBis != null) {
			buff.append(getTextRespectUISpr("bes.belegdatum",
					theClientDto.getMandant(), theClientDto.getLocUi()));
		}

		// Lieferant
		if (reportJournalKriterienDtoI.kundeIId != null) {
			buff.append(" ")
					.append(getTextRespectUISpr("lp.lieferant",
							theClientDto.getMandant(), theClientDto.getLocUi()));
			buff.append(" ").append(
					getLieferantFac()
							.lieferantFindByPrimaryKey(
									reportJournalKriterienDtoI.lieferantIId,
									theClientDto).getPartnerDto()
							.getCName1nachnamefirmazeile1());
		}

		return buff.toString().trim();
	}

	/**
	 * Alle offenen Bestellungen fuer einen bestimmten Mandanten drucken.
	 * 
	 * @param krit
	 *            die Filter- und Sortierkriterien
	 * @param dStichtag
	 *            Date
	 * @param bSortierungNachLiefertermin
	 *            Boolean
	 * @param artikelklasseIId
	 *            Integer
	 * @param artikelgruppeIId
	 *            Integer
	 * @param artikelCNrVon
	 *            String
	 * @param artikelCNrBis
	 *            String
	 * @param projektCBezeichnung
	 *            String
	 * @param auftragIId
	 *            Integer
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @return JasperPrint der Druck
	 */
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printBestellungOffene(ReportJournalKriterienDto krit,
			Date dStichtag, Boolean bSortierungNachLiefertermin,
			Integer artikelklasseIId, Integer artikelgruppeIId,
			String artikelCNrVon, String artikelCNrBis,
			String projektCBezeichnung, Integer auftragIId, Integer iArt,
			boolean bNurAngelegte, boolean bNurOffeneMengenAnfuehren,
			Integer[] projekte, TheClientDto theClientDto)
			throws EJBExceptionLP {
		useCase = UC_OFFENE;
		int iAnzahlZeilen = 0;

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		// vom Stichtag die Uhrzeit abschneiden
		dStichtag = Helper.cutDate(dStichtag);

		// die dem Report uebergeben
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("P_STICHTAG",
				Helper.formatDatum(dStichtag, theClientDto.getLocUi()));
		dStichtag = Helper.addiereTageZuDatum(dStichtag, 1);

		session = factory.openSession();

		boolean darfEinkaufspreisSehen = getTheJudgeFac().hatRecht(
				RechteFac.RECHT_LP_DARF_PREISE_SEHEN_EINKAUF, theClientDto);
		boolean bProjektklammer = getMandantFac()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_PROJEKTKLAMMER,
						theClientDto.getMandant());
		// Hiberante Criteria fuer alle Tabellen ausgehend von meiner
		// Haupttabelle anlegen,
		// nach denen ich filtern und sortieren kann
		Criteria crit = session.createCriteria(FLRBestellung.class);

		// Einschraenkung auf den aktuellen Mandanten
		crit.add(Restrictions.eq(BestellungFac.FLR_BESTELLUNG_MANDANT_C_NR,
				theClientDto.getMandant()));

		// Einschraenkung nach Status Offen, Erledigt
		Collection<String> cStati = new LinkedList<String>();
		cStati.add(BestellungFac.BESTELLSTATUS_ANGELEGT);
		if (bNurAngelegte == false) {
			cStati.add(BestellungFac.BESTELLSTATUS_OFFEN);
			cStati.add(BestellungFac.BESTELLSTATUS_BESTAETIGT);
			cStati.add(BestellungFac.BESTELLSTATUS_ERLEDIGT);
		}
		crit.add(Restrictions.in(
				BestellungFac.FLR_BESTELLUNG_BESTELLUNGSTATUS_C_NR, cStati));

		// Das Belegdatum muss vor dem Stichtag liegen
		crit.add(Restrictions.le(BestellungFac.FLR_BESTELLUNG_T_BELEGDATUM,
				dStichtag));

		/**
		 * @todo MB->MB hier sollte auch das erledigt-datum ziehen, das gibts
		 *       aber in der BS nicht :-(
		 */

		crit.add(Restrictions.or(
				Restrictions.ge(
						BestellungFac.FLR_BESTELLUNG_T_MANUELLGELIEFERT,
						dStichtag),
				Restrictions
						.isNull(BestellungFac.FLR_BESTELLUNG_T_MANUELLGELIEFERT)));

		// Das Belegdatum muss vor dem Stichtag liegen
		crit.add(Restrictions.le(BestellungFac.FLR_BESTELLUNG_T_BELEGDATUM,
				dStichtag));
		// Filter nach Projektbezeichnung
		if (projektCBezeichnung != null) {
			crit.add(Restrictions.ilike(
					BestellungFac.FLR_BESTELLUNG_C_BEZPROJEKTBEZEICHNUNG, "%"
							+ projektCBezeichnung + "%"));
		}

		// Projektklammer
		if (projekte != null && projekte.length > 0) {
			crit.add(Restrictions.in(BestellungFac.FLR_BESTELLUNG_PROJEKT_I_ID,
					projekte));

			String text = "";
			for (int i = 0; i < projekte.length; i++) {

				try {
					ProjektDto pDto = getProjektFac().projektFindByPrimaryKey(
							projekte[i]);

					text += pDto.getCNr() + ", ";
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}

			parameter.put("P_PROJEKTE", text);

		}

		// Filter nach Auftrag
		if (auftragIId != null) {
			crit.add(Restrictions.like(
					BestellungFac.FLR_BESTELLUNG_AUFTRAG_I_ID, auftragIId));
		}
		// Einschraenkung nach einer bestimmten Kostenstelle
		if (krit.kostenstelleIId != null) {
			crit.add(Restrictions.eq(
					BestellungFac.FLR_BESTELLUNG_KOSTENSTELLE_I_ID,
					krit.kostenstelleIId));
		}

		// Einschraenkung nach einem bestimmten Lieferanten
		if (krit.lieferantIId != null) {
			crit.add(Restrictions.eq(
					BestellungFac.FLR_BESTELLUNG_LIEFERANT_I_ID_BESTELLADRESSE,
					krit.lieferantIId));
		}
		// Filter nach Bestellungsart
		Collection<String> cArt = null;
		if (iArt != null) {
			if (iArt == 1) {
				// Ohne Rahmenbestellungen
				cArt = new LinkedList<String>();
				cArt.add(BestellungFac.BESTELLUNGART_FREIE_BESTELLUNG_C_NR);
				cArt.add(BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR);
				cArt.add(BestellungFac.BESTELLUNGART_LEIHBESTELLUNG_C_NR);
				crit.add(Restrictions.in(
						BestellungFac.FLR_BESTELLUNG_BESTELLUNGART_C_NR, cArt));
			} else if (iArt == 2) {
				// Nur Rahmenbestellungen
				cArt = new LinkedList<String>();
				cArt.add(BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR);
				crit.add(Restrictions.in(
						BestellungFac.FLR_BESTELLUNG_BESTELLUNGART_C_NR, cArt));
			}
		}

		// Sortierung nach Kostenstelle ist immer die erste Sortierung
		if (krit.bSortiereNachKostenstelle) {
			crit.createCriteria(BestellungFac.FLR_BESTELLUNG_FLRKOSTENSTELLE)
					.addOrder(Order.asc("c_nr"));
		}
		// Sortierung nach Kunde, eventuell innerhalb der Kostenstelle
		if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
			crit.createCriteria(BestellungFac.FLR_BESTELLUNG_FLRLIEFERANT)
					.createCriteria(LieferantFac.FLR_PARTNER)
					.addOrder(
							Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));

		}
		// Sortierung nach Projekt, eventuell innerhalb der Kostenstelle
		else if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PROJEKT) {

			if (bProjektklammer == true) {

				crit.createAlias(BestellungFac.FLR_BESTELLUNG_FLRPROJEKT, "p");

				crit.addOrder(Order.asc("p.c_nr"));
			} else {
				crit.addOrder(Order
						.asc(BestellungFac.FLR_BESTELLUNG_C_BEZPROJEKTBEZEICHNUNG));
			}

		}
		// Sortierung nach Bestellungart, eventuell innerhalb der
		// Kostenstelle
		// else if (krit.iSortierung ==
		// ReportJournalKriterienDto.KRIT_SORT_NACH_ART) {
		// crit.addOrder(Order
		// .asc(BestellungFac.FLR_BESTELLUNG_BESTELLUNGART_C_NR));
		// }

		// Sortierung nach Liefertermin (optional)
		if (bSortierungNachLiefertermin != null
				&& bSortierungNachLiefertermin.booleanValue()) {
			crit.addOrder(Order
					.asc(BestellungFac.FLR_BESTELLUNG_T_LIEFERTERMIN));
		}

		// es wird in jedem Fall nach der Belegnummer sortiert
		crit.addOrder(Order.asc("c_nr"));

		List<?> list = crit.list();
		Iterator<?> it = list.iterator();

		ArrayList<FLRBestellung> bestellung = new ArrayList<FLRBestellung>();
		while (it.hasNext()) {
			FLRBestellung flrbestellung = (FLRBestellung) it.next();
			session = factory.openSession();
			Criteria crit1 = session.createCriteria(FLRBestellposition.class);
			Criteria crit1Bestellung = crit1
					.createCriteria(BestellpositionFac.FLR_BESTELLPOSITION_FLRBESTELLUNG);
			// nur Positionen der aktuellen Bestellung.
			crit1Bestellung
					.add(Restrictions.eq(BestellungFac.FLR_BESTELLUNG_I_ID,
							flrbestellung.getI_id()));
			// keine erledigten Positionen.
			crit1.add(Restrictions
					.ne(BestellpositionFac.FLR_BESTELLPOSITION_BESTELLPOSITIONSTATUS_C_NR,
							BestellpositionFac.BESTELLPOSITIONSTATUS_ERLEDIGT));
			// keine geliferten Positionen.
			crit1.add(Restrictions
					.ne(BestellpositionFac.FLR_BESTELLPOSITION_BESTELLPOSITIONSTATUS_C_NR,
							BestellpositionFac.BESTELLPOSITIONSTATUS_GELIEFERT));
			// Der Liefertermin muss vor dem Stichtag liegen
			crit1.add(Restrictions.or(
					Restrictions.and(
							// Wenn der AB-Termin
							// eingegeben ist, zieht
							// der
							Restrictions
									.isNotNull(BestellpositionFac.FLR_BESTELLPOSITION_T_AUFTRAGSBESTAETIGUNGSTERMIN),
							Restrictions
									.le(BestellpositionFac.FLR_BESTELLPOSITION_T_AUFTRAGSBESTAETIGUNGSTERMIN,
											dStichtag)),
					Restrictions.and(
							// sonst der
							// uebersteuerte
							// Liefertermin
							Restrictions
									.isNull(BestellpositionFac.FLR_BESTELLPOSITION_T_AUFTRAGSBESTAETIGUNGSTERMIN),
							Restrictions
									.le(BestellpositionFac.FLR_BESTELLPOSITION_T_UEBERSTEUERTERLIEFERTERMIN,
											dStichtag))));

			if (artikelklasseIId != null || artikelgruppeIId != null
					|| artikelCNrVon != null || artikelCNrBis != null) {
				// Wenn nach Artikelklassen/Gruppen gefiltert wird, dann
				// kommen nur Ident-Positionen
				crit1.add(Restrictions
						.eq(BestellpositionFac.FLR_BESTELLPOSITION_BESTELLPOSITIONART_C_NR,
								BestellpositionFac.BESTELLPOSITIONART_IDENT));
				Criteria critArtikel = crit1
						.createCriteria(BestellpositionFac.FLR_BESTELLPOSITION_FLRARTIKEL);
				if (artikelklasseIId != null) {
					critArtikel.createCriteria(
							ArtikelFac.FLR_ARTIKEL_FLRARTIKELKLASSE).add(
							Restrictions.eq("i_id", artikelklasseIId));
				}
				if (artikelgruppeIId != null) {
					critArtikel.createCriteria(
							ArtikelFac.FLR_ARTIKEL_FLRARTIKELGRUPPE).add(
							Restrictions.eq("i_id", artikelgruppeIId));
				}
				if (artikelCNrVon != null) {
					critArtikel.add(Restrictions.ge(
							ArtikelFac.FLR_ARTIKEL_C_NR, artikelCNrVon));
				}
				if (artikelCNrBis != null) {
					critArtikel.add(Restrictions.le(
							ArtikelFac.FLR_ARTIKEL_C_NR, artikelCNrBis));
				}
			}
			List<?> resultList = crit1.list();
			// Wenn die Bestellung anzuzeigende Positionen enthaelt, dann in
			// die Liste aufnehmen.
			if (resultList.size() > 0) {
				bestellung.add(flrbestellung);
				iAnzahlZeilen++;
			}
			for (Iterator<?> iter = resultList.iterator(); iter.hasNext();) {
				FLRBestellposition item = (FLRBestellposition) iter.next();
				if (item.getN_menge() != null) {
					bestellung.add(null);
					iAnzahlZeilen++;
				}
			}
		}

		data = new Object[iAnzahlZeilen][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ANZAHL_SPALTEN];

		int i = 0;
		while (i < iAnzahlZeilen) {
			FLRBestellung flrbestellung = null;
			if (bestellung.get(i) != null) {
				flrbestellung = (FLRBestellung) bestellung.get(i);
				data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_BESTELLUNGCNR] = flrbestellung
						.getC_nr();
				data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_BESTELLUNGARTCNR] = flrbestellung
						.getBestellungart_c_nr();

				if (bProjektklammer && flrbestellung.getProjekt_i_id() != null) {
					data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_PROJEKT] = flrbestellung
							.getFlrprojekt().getC_nr();
				} else {
					data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_PROJEKT] = flrbestellung
							.getC_bezprojektbezeichnung();
				}

				data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_BESTELLUNGLIEFERANT] = flrbestellung
						.getFlrlieferant().getFlrpartner()
						.getC_name1nachnamefirmazeile1();

				// PJ 14752

				String sortierstring = "";
				if (krit.bSortiereNachKostenstelle == true) {
					sortierstring = Helper.fitString2Length(flrbestellung
							.getFlrkostenstelle().getC_nr(), 80, ' ');
				}
				if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_IDENT) {
					sortierstring += Helper.fitString2Length(flrbestellung
							.getFlrlieferant().getFlrpartner()
							.getC_name1nachnamefirmazeile1(), 80, ' ')
							+ Helper.fitString2Length("", 80, ' ');
				}
				if (bSortierungNachLiefertermin == true) {
					sortierstring += Helper.fitString2Length(
							flrbestellung.getT_liefertermin() + "", 15, ' ');
				}
				data[i][REPORT_BESTELLUNG_OFFENE_SORTIERKRITERIUM] = sortierstring;

				data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_BESTELLUNGDATUM] = Helper
						.formatDatum(flrbestellung.getT_belegdatum(),
								theClientDto.getLocUi());
				if (flrbestellung.getFlrkostenstelle() != null) {
					data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_KOSTENSTELLECNR] = flrbestellung
							.getFlrkostenstelle().getC_nr();
				}
				i++;
			}
			session = factory.openSession();
			Criteria crit1 = session
					.createCriteria(FLRBestellpositionReport.class);
			// Keine erledigten Positionen.
			crit1.add(Restrictions
					.ne(BestellpositionFac.FLR_BESTELLPOSITION_BESTELLPOSITIONSTATUS_C_NR,
							BestellpositionFac.BESTELLPOSITIONSTATUS_ERLEDIGT));
			crit1.add(Restrictions
					.ne(BestellpositionFac.FLR_BESTELLPOSITION_BESTELLPOSITIONSTATUS_C_NR,
							BestellpositionFac.BESTELLPOSITIONSTATUS_GELIEFERT));

			// Der Liefertermin muss vor dem Stichtag liegen
			crit1.add(Restrictions.or(
					Restrictions.and(
							// Wenn der AB-Termin
							// eingegeben ist, zieht
							// der
							Restrictions
									.isNotNull(BestellpositionFac.FLR_BESTELLPOSITION_T_AUFTRAGSBESTAETIGUNGSTERMIN),
							Restrictions
									.le(BestellpositionFac.FLR_BESTELLPOSITION_T_AUFTRAGSBESTAETIGUNGSTERMIN,
											dStichtag)),
					Restrictions.and(
							// sonst der
							// uebersteuerte
							// Liefertermin
							Restrictions
									.isNull(BestellpositionFac.FLR_BESTELLPOSITION_T_AUFTRAGSBESTAETIGUNGSTERMIN),
							Restrictions
									.le(BestellpositionFac.FLR_BESTELLPOSITION_T_UEBERSTEUERTERLIEFERTERMIN,
											dStichtag))));

			// Nur Positionen der aktuellen Bestellung.
			Criteria crit1Bestellung = crit1
					.createCriteria(BestellpositionFac.FLR_BESTELLPOSITION_FLRBESTELLUNG);
			crit1Bestellung
					.add(Restrictions.eq(BestellungFac.FLR_BESTELLUNG_I_ID,
							flrbestellung.getI_id()));
			if (artikelklasseIId != null || artikelgruppeIId != null
					|| artikelCNrVon != null || artikelCNrBis != null) {
				// Wenn nach Artikelklassen/Gruppen gefiltert wird, dann
				// kommen nur Ident-Positionen
				crit1.add(Restrictions
						.eq(BestellpositionFac.FLR_BESTELLPOSITION_BESTELLPOSITIONART_C_NR,
								BestellpositionFac.BESTELLPOSITIONART_IDENT));
				Criteria critArtikel = crit1
						.createCriteria(BestellpositionFac.FLR_BESTELLPOSITION_FLRARTIKEL);
				if (artikelklasseIId != null) {
					critArtikel.createCriteria(
							ArtikelFac.FLR_ARTIKEL_FLRARTIKELKLASSE).add(
							Restrictions.eq("i_id", artikelklasseIId));
				}
				if (artikelgruppeIId != null) {
					critArtikel.createCriteria(
							ArtikelFac.FLR_ARTIKEL_FLRARTIKELGRUPPE).add(
							Restrictions.eq("i_id", artikelgruppeIId));
				}
				if (artikelCNrVon != null) {
					critArtikel.add(Restrictions.ge(
							ArtikelFac.FLR_ARTIKEL_C_NR, artikelCNrVon));
				}
				if (artikelCNrBis != null) {
					critArtikel.add(Restrictions.le(
							ArtikelFac.FLR_ARTIKEL_C_NR, artikelCNrBis));
				}
			}

			List<?> resultList = crit1.list();
			for (Iterator<?> iter = resultList.iterator(); iter.hasNext();) {
				FLRBestellpositionReport item = (FLRBestellpositionReport) iter
						.next();
				if (item.getN_menge() != null) {
					String artikelCNr = null;
					/**
					 * @todo das ist nicht sehr sauber ...
					 */
					if (item.getFlrartikel().getC_nr().startsWith("~")) {
						artikelCNr = BestellungReportFac.REPORT_VORKALKULATION_ZEICHEN_FUER_HANDEINGABE;
					} else {
						artikelCNr = item.getFlrartikel().getC_nr();
					}
					Criteria critWep = session
							.createCriteria(FLRWareneingangspositionen.class);
					critWep.createCriteria("flrbestellposition").add(
							Restrictions.eq("i_id", item.getI_id()));
					List<?> wepResultList = critWep.list();
					Iterator<?> wepResultListIterator = wepResultList
							.iterator();

					data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_BESTELLUNGCNR] = flrbestellung
							.getC_nr();
					data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_BESTELLUNGARTCNR] = flrbestellung
							.getBestellungart_c_nr();
					data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_PROJEKT] = flrbestellung
							.getC_bezprojektbezeichnung();
					data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_BESTELLUNGLIEFERANT] = flrbestellung
							.getFlrlieferant().getFlrpartner()
							.getC_name1nachnamefirmazeile1();
					data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ARTIKELCNR] = artikelCNr;

					// SP903
					if (item.getPosition_i_id_artikelset() != null) {
						data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_SETARTIKEL_TYP] = ArtikelFac.SETARTIKEL_TYP_POSITION;
					} else {

						Session sessionSet = FLRSessionFactory.getFactory()
								.openSession();

						sessionSet = factory.openSession();
						Criteria critSet = sessionSet
								.createCriteria(FLRBestellpositionReport.class);
						critSet.add(Restrictions.eq("position_i_id_artikelset",
								item.getI_id()));

						int iZeilen = critSet.list().size();

						if (iZeilen > 0) {
							data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_SETARTIKEL_TYP] = ArtikelFac.SETARTIKEL_TYP_KOPF;
						}
						sessionSet.close();

					}

					// PJ 14752

					String sortierstring = "";
					if (krit.bSortiereNachKostenstelle == true) {
						sortierstring = Helper.fitString2Length(flrbestellung
								.getFlrkostenstelle().getC_nr(), 80, ' ');
					}

					if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_IDENT) {
						sortierstring += Helper.fitString2Length(flrbestellung
								.getFlrlieferant().getFlrpartner()
								.getC_name1nachnamefirmazeile1(), 80, ' ')
								+ Helper.fitString2Length(artikelCNr, 80, ' ');
					}

					if (bSortierungNachLiefertermin == true) {
						sortierstring += Helper
								.fitString2Length(
										flrbestellung.getT_liefertermin() + "",
										15, ' ');
					}

					data[i][REPORT_BESTELLUNG_OFFENE_SORTIERKRITERIUM] = sortierstring;

					data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ARTIKELMENGE] = item
							.getN_menge();
					BigDecimal bdOffeneLiefermenge = new BigDecimal(0);
					if (BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR
							.equals(flrbestellung.getBestellungart_c_nr())) {
						try {
							BestellpositionDto[] abrufPos = getBestellpositionFac()
									.bestellpositionFindByBestellpositionIIdRahmenposition(
											item.getI_id(), theClientDto);
							for (int y = 0; y < abrufPos.length; y++) {
								bdOffeneLiefermenge = bdOffeneLiefermenge
										.add(getBestellpositionFac()
												.berechneOffeneMenge(
														abrufPos[y]));
							}
						} catch (RemoteException e) {
						}
						data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_OFFENELIEFERUNGEN] = bdOffeneLiefermenge;

					} else {
						data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_OFFENELIEFERUNGEN] = null;
					}
					StringBuffer sbArtikelInfo = new StringBuffer();
					if (item.getC_bezeichnung() != null) {
						sbArtikelInfo.append(item.getC_bezeichnung());
					} else {
						ArtikelDto artikelDto = getArtikelFac()
								.artikelFindByPrimaryKey(
										item.getFlrartikel().getI_id(),
										theClientDto);

						if (artikelDto.getArtikelsprDto() != null) {
							if (artikelDto.getArtikelsprDto().getCBez() != null) {
								sbArtikelInfo.append(artikelDto
										.getArtikelsprDto().getCBez());
							}
						}
					}
					data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ARTIKELBEZ] = sbArtikelInfo
							.toString();

					// der Preis wird in Mandantenwaehrung angezeigt, es
					// gilt der hinterlegte Wechselkurs
					BigDecimal bdPreisinmandantenwaehrung = item
							.getN_nettogesamtpreis();
					BigDecimal wechselkursmandantwaehrungzuauftragswaehrung = null;
					if (!flrbestellung.getWaehrung_c_nr_bestellwaehrung()
							.equals(theClientDto.getSMandantenwaehrung())) {
						wechselkursmandantwaehrungzuauftragswaehrung = new BigDecimal(
								flrbestellung
										.getF_wechselkursmandantwaehrungbestellungswaehrung()
										.doubleValue());
						bdPreisinmandantenwaehrung = getBetragMalWechselkurs(
								bdPreisinmandantenwaehrung,
								Helper.getKehrwert(wechselkursmandantwaehrungzuauftragswaehrung));
					}
					if (darfEinkaufspreisSehen) {
						data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ARTIKELPREIS] = bdPreisinmandantenwaehrung;
					} else {
						data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ARTIKELPREIS] = null;
					}

					if (item.getEinheit_c_nr() != null) {
						data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ARTIKELEINHEIT] = item
								.getEinheit_c_nr().trim();
					}
					if (item.getT_auftragsbestaetigungstermin() != null) {
						data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ABTERMIN] = Helper
								.formatDatum(
										item.getT_auftragsbestaetigungstermin(),
										theClientDto.getLocUi());
					}

					data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ABNUMMER] = item
							.getC_abnummer();
					data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ABKOMMENTAR] = item
							.getC_abkommentar();

					if (item.getT_uebersteuerterliefertermin() != null) {
						data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_BESTELLUNGLIEFERTERMIN] = Helper
								.formatDatum(
										item.getT_uebersteuerterliefertermin(),
										theClientDto.getLocUi());
					} else {
						data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_BESTELLUNGLIEFERTERMIN] = Helper
								.formatDatum(item.getFlrbestellung()
										.getT_liefertermin(), theClientDto
										.getLocUi());
					}
					BigDecimal noffeneMenge = item.getN_menge();
					BigDecimal ngeliferteMenge = new BigDecimal(0);
					if (flrbestellung.getBestellungart_c_nr().equals(
							BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR)) {
						noffeneMenge = item.getN_offenemenge();
						while (wepResultListIterator.hasNext()) {
							FLRWareneingangspositionen waren = (FLRWareneingangspositionen) wepResultListIterator
									.next();
							ngeliferteMenge = ngeliferteMenge.add(waren
									.getN_geliefertemenge());
						}

					} else {
						while (wepResultListIterator.hasNext()) {
							FLRWareneingangspositionen waren = (FLRWareneingangspositionen) wepResultListIterator
									.next();
							noffeneMenge = noffeneMenge.subtract(waren
									.getN_geliefertemenge());
						}
					}
					data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ARTIKELGELIFERTEMENGE] = ngeliferteMenge;

					data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ARTIKELOFFENEMENGE] = noffeneMenge;
					if (darfEinkaufspreisSehen) {
						data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ARTIKELOFFENEWERT] = noffeneMenge
								.multiply(bdPreisinmandantenwaehrung);
					} else {
						data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ARTIKELOFFENEWERT] = null;
					}
					i++;
				}
			}
		}

		closeSession(session);

		// PJ 15254
		if (bNurOffeneMengenAnfuehren) {
			ArrayList alTemp = new ArrayList();

			for (int k = 0; k < data.length; k++) {
				BigDecimal bdOffeneMenge = (BigDecimal) data[k][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ARTIKELOFFENEMENGE];
				if (bdOffeneMenge != null && bdOffeneMenge.doubleValue() > 0) {
					alTemp.add(data[k]);
				}
			}
			Object[][] returnArray = new Object[alTemp.size()][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ANZAHL_SPALTEN];
			data = (Object[][]) alTemp.toArray(returnArray);
		}

		// PJ 14752 Manuell nachsortieren (in Besprechung mit AD+WH besprochen)

		if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_IDENT) {

			for (int k = data.length - 1; k > 0; --k) {
				for (int j = 0; j < k; ++j) {
					Object[] o = data[j];
					Object[] o1 = data[j + 1];

					String s = (String) o[REPORT_BESTELLUNG_OFFENE_SORTIERKRITERIUM];
					String s1 = (String) o1[REPORT_BESTELLUNG_OFFENE_SORTIERKRITERIUM];

					if (s.toUpperCase().compareTo(s1.toUpperCase()) > 0) {
						data[j] = o1;
						data[j + 1] = o;
					}
				}
			}

		}

		parameter.put(LPReport.P_SORTIERUNG,
				buildSortierungBestellungOffene(krit, theClientDto));
		parameter.put(
				LPReport.P_FILTER,
				buildFilterBestellungOffene(krit, artikelklasseIId,
						artikelgruppeIId, artikelCNrVon, artikelCNrBis,
						projektCBezeichnung, theClientDto));

		parameter
				.put(LPReport.P_SORTIERENACHLIEFERANT,
						new Boolean(
								krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER));
		parameter
				.put("P_SORTIERENACHBESTELLUNGART",
						new Boolean(
								krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_ART));
		parameter.put(
				"P_TITLE",
				getTextRespectUISpr("bes.print.offene",
						theClientDto.getMandant(), theClientDto.getLocUi()));
		parameter.put(P_MANDANTWAEHRUNG, theClientDto.getSMandantenwaehrung());

		initJRDS(parameter, BestellungReportFac.REPORT_MODUL,
				BestellungReportFac.REPORT_BESTELLUNG_JOURNAL_OFFENE,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);
		return getReportPrint();
	}

	/**
	 * Alle offenen Bestellungen fuer einen bestimmten Mandanten drucken.
	 * 
	 * @param krit
	 *            die Filter- und Sortierkriterien
	 * @param bSortierungNachLiefertermin
	 *            Boolean
	 * @param bAnfragevorschlag
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @return JasperPrint der Druck
	 */
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printBestellVorschlag(ReportJournalKriterienDto krit,
			Boolean bSortierungNachLiefertermin, boolean bAnfragevorschlag,
			TheClientDto theClientDto) throws EJBExceptionLP {
		JasperPrintLP oPrintO = null;
		useCase = UC_BESTELLVORSCHLAG;
		int iAnzahlZeilen = 0;
		Locale locDruck = theClientDto.getLocUi();

		ArrayList<FLRBestellvorschlag> bestellung = new ArrayList();

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {

			LagerDto[] lagerDto = getLagerFac().lagerFindByMandantCNr(
					theClientDto.getMandant());

			session = factory.openSession();
			// Hiberante Criteria fuer alle Tabellen ausgehend von meiner
			// Haupttabelle anlegen,
			// nach denen ich filtern und sortieren kann
			Criteria crit = session.createCriteria(FLRBestellvorschlag.class);

			// Einschraenkung auf den aktuellen Mandanten
			crit.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));

			// Einschraenkung nach einem bestimmten Kunden
			if (krit.lieferantIId != null) {
				crit.add(Restrictions
						.eq(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_LIEFERANT_I_ID,
								krit.lieferantIId));
			}

			// Sortierung nach Kunde, eventuell innerhalb der Kostenstelle
			if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				crit.createCriteria(BestellungFac.FLR_BESTELLUNG_FLRLIEFERANT)
						.createCriteria(LieferantFac.FLR_PARTNER)
						.addOrder(
								Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));
			}

			if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_IDENT) {
				crit.createCriteria(
						BestellvorschlagFac.FLR_BESTELLVORSCHLAG_FLRARTIKEL)
						.addOrder(Order.asc("c_nr"));
			}

			// Sortierung nach Liefertermin (optional)
			if (bSortierungNachLiefertermin != null
					&& bSortierungNachLiefertermin.booleanValue()) {
				crit.addOrder(Order
						.asc(BestellungFac.FLR_BESTELLUNG_T_LIEFERTERMIN));
			}
			crit.addOrder(Order
					.asc(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_BELEGART_C_NR));
			List<?> list = crit.list();
			Iterator<?> it = list.iterator();

			while (it.hasNext()) {
				FLRBestellvorschlag flrbestellvorschlag = (FLRBestellvorschlag) it
						.next();
				bestellung.add(flrbestellvorschlag);
				iAnzahlZeilen++;
			}
			// Danach wenn nach Lieferant sortiert auch noch die null
			// Lieferanten anfuegen
			if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				Criteria crit2 = session
						.createCriteria(FLRBestellvorschlag.class);
				crit2.add(Restrictions.eq("mandant_c_nr",
						theClientDto.getMandant()));
				crit2.add(Restrictions
						.isNull(BestellungFac.FLR_BESTELLUNG_FLRLIEFERANT));
				if (bSortierungNachLiefertermin != null
						&& bSortierungNachLiefertermin.booleanValue()) {
					crit2.addOrder(Order
							.asc(BestellungFac.FLR_BESTELLUNG_T_LIEFERTERMIN));
				}
				crit2.addOrder(Order
						.asc(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_BELEGART_C_NR));
				if (krit.kundeIId != null) {
					crit2.add(Restrictions
							.eq(BestellungFac.FLR_BESTELLUNG_LIEFERANT_I_ID_BESTELLADRESSE,
									krit.lieferantIId));
				}

				list = crit2.list();
				it = list.iterator();
				while (it.hasNext()) {
					FLRBestellvorschlag flrbestellvorschlag = (FLRBestellvorschlag) it
							.next();
					bestellung.add(flrbestellvorschlag);
					iAnzahlZeilen++;
				}
			}

			data = new Object[iAnzahlZeilen][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ANZAHL_SPALTEN];

			int i = 0;
			FLRBestellvorschlag flrbestellung = null;
			while (i < iAnzahlZeilen) {
				flrbestellung = (FLRBestellvorschlag) bestellung.get(i);
				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_BELEGART] = flrbestellung
						.getBelegart_c_nr();
				String belegCNr = "";
				if (flrbestellung.getBelegart_c_nr() != null) { // Es gibt auch
					// Eintraege
					// ohne
					// Belegart, zb
					// fuer den
					// mindestlagerstand
					if (flrbestellung.getBelegart_c_nr().equals(
							LocaleFac.BELEGART_AUFTRAG)) {
						AuftragDto aDto = getAuftragFac()
								.auftragFindByPrimaryKey(
										flrbestellung.getI_belegartid());
						belegCNr = aDto.getCNr();
					} else if (flrbestellung.getBelegart_c_nr().equals(
							LocaleFac.BELEGART_LOS)) {
						LosDto aDto = getFertigungFac().losFindByPrimaryKey(
								flrbestellung.getI_belegartid());
						belegCNr = aDto.getCNr();
					}
				}
				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_BELEGCNR] = belegCNr;

				if (flrbestellung.getLieferant_i_id() != null) {
					data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_BESTELLUNGLIEFERANT] = flrbestellung
							.getFlrlieferant().getFlrpartner()
							.getC_name1nachnamefirmazeile1();
				}
				if (flrbestellung.getProjekt_i_id() != null) {
					data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_PROJEKT] = flrbestellung
							.getFlrprojekt().getC_nr();
				}

				/*
				 * locDruck =
				 * Helper.string2Locale(flrbestellung.getFlrlieferant(
				 * ).getFlrpartner(). getLocale_c_nr_kommunikation());
				 */
				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_BESTELLTERMIN] = Helper
						.formatDatum(flrbestellung.getT_liefertermin(),
								locDruck);

				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELCNR] = flrbestellung
						.getFlrartikel().getC_nr();
				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKEL_LAGERBEWIRTSCHAFTET] = Helper
						.short2Boolean(flrbestellung.getFlrartikel()
								.getB_lagerbewirtschaftet());
				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELMENGE] = flrbestellung
						.getN_zubestellendemenge();
				ArtikelDto oArtikelDto = getArtikelFac()
						.artikelFindByPrimaryKey(
								flrbestellung.getArtikel_i_id(), theClientDto);
				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELBEZ] = oArtikelDto
						.getArtikelsprDto().getCBez();
				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELKBEZ] = oArtikelDto
						.getArtikelsprDto().getCKbez();
				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELZBEZ] = oArtikelDto
						.getArtikelsprDto().getCZbez();
				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELZBEZ2] = oArtikelDto
						.getArtikelsprDto().getCZbez2();

				// Sperren
				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_SPERREN] = getArtikelFac()
						.getArtikelsperrenText(flrbestellung.getArtikel_i_id());

				// offene Bestellmenge
				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKEL_MENGE_OFFEN] = getArtikelbestelltFac()
						.getAnzahlBestellt(flrbestellung.getArtikel_i_id());

				// offene Rahmenbestellmenge
				Hashtable<?, ?> htRahmenbestellt = getArtikelbestelltFac()
						.getAnzahlRahmenbestellt(
								flrbestellung.getArtikel_i_id(), theClientDto);
				if (htRahmenbestellt
						.containsKey(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL)) {
					data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKEL_RAHMENMENGE_OFFEN] = htRahmenbestellt
							.get(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL);
				}
				// alle offenen Rahmenbestellnr
				if (htRahmenbestellt
						.containsKey(ArtikelbestelltFac.KEY_RAHMENBESTELLT_BELEGCNR)) {
					Collection<?> aRahmenbestellnr = (Collection<?>) htRahmenbestellt
							.get(ArtikelbestelltFac.KEY_RAHMENBESTELLT_BELEGCNR);
					if (aRahmenbestellnr != null && aRahmenbestellnr.size() > 0) {
						String[] aRahmenbestellnrStringArray = (String[]) aRahmenbestellnr
								.toArray(new String[0]);
						data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKEL_RAHMENBESTELLNR] = Helper
								.erzeugeStringAusStringArray(aRahmenbestellnrStringArray);
					}
				}
				// alle offenenen Bestellungen CNr
				ArrayList<String> alBestellungnr = new ArrayList<String>();
				Collection<?> lFLRArtikelbestellt = getArtikelbestelltFac()
						.getArtikelbestellt(flrbestellung.getArtikel_i_id(),
								null, null);
				Iterator<?> iteratorFLRArtikelbestellt = lFLRArtikelbestellt
						.iterator();
				while (iteratorFLRArtikelbestellt.hasNext()) {
					FLRArtikelbestellt fLRArtikelbestellt = (FLRArtikelbestellt) iteratorFLRArtikelbestellt
							.next();
					BestellpositionDto bestellpositionDto = null;
					BestellungDto bestellungDto = null;
					if (fLRArtikelbestellt.getC_belegartnr().equals(
							LocaleFac.BELEGART_BESTELLUNG)) {
						bestellpositionDto = getBestellpositionFac()
								.bestellpositionFindByPrimaryKey(
										fLRArtikelbestellt
												.getI_belegartpositionid());
						if (!bestellpositionDto
								.getBestellpositionstatusCNr()
								.equals(BestellpositionFac.BESTELLPOSITIONSTATUS_ERLEDIGT)) {
							bestellungDto = getBestellungFac()
									.bestellungFindByPrimaryKey(
											bestellpositionDto
													.getBestellungIId());
							alBestellungnr.add(bestellungDto.getCNr());
						}
					}
				}
				// offene Bestellungen CNr als String uebergeben
				if (alBestellungnr.size() > 0) {
					String[] aArtikelbestellNr = (String[]) alBestellungnr
							.toArray(new String[0]);
					data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKEL_OFFENE_BESTELLNR] = Helper
							.erzeugeStringAusStringArray(aArtikelbestellNr);
				}

				// ----------------
				// Artikellieferant, wenn vorhanden
				ArtikellieferantDto artikellieferantDto = getArtikelFac()
						.artikellieferantFindByArtikellIIdLieferantIIdOhneExc(
								flrbestellung.getArtikel_i_id(),
								flrbestellung.getLieferant_i_id(), theClientDto);
				if (artikellieferantDto != null) {
					data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_LIEFERANT_ARTIKEL_IDENTNUMMER] = artikellieferantDto
							.getCArtikelnrlieferant();
					data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_LIEFERANT_ARTIKEL_BEZEICHNUNG] = artikellieferantDto
							.getCBezbeilieferant();
					data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_LIEFERANT_STANDARDMENGE] = artikellieferantDto
							.getFStandardmenge();

					data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_LIEFERANT_MATERIALZUSCHLAG] = artikellieferantDto
							.getNMaterialzuschlag();
				}
				BigDecimal rahmenbedarf = this.getRahmenbedarfeFac()
						.getSummeAllerRahmenbedarfeEinesArtikels(
								flrbestellung.getArtikel_i_id());
				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKEL_RAHMENDETAILBEDARF] = rahmenbedarf;

				BigDecimal nLagersoll = new BigDecimal(0);
				BigDecimal nLagermindest = new BigDecimal(0);
				BigDecimal nArtikelPreis = new BigDecimal(0);
				BigDecimal nAnzahlArtikelRes = new BigDecimal(0);
				BigDecimal nFehlMenge = new BigDecimal(0);
				if (oArtikelDto.getFLagermindest() != null) {
					nLagermindest = new BigDecimal(
							oArtikelDto.getFLagermindest());
				}
				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELLAGERMINDESTSTAND] = nLagermindest;
				if (oArtikelDto.getFLagersoll() != null) {
					nLagersoll = new BigDecimal(oArtikelDto.getFLagersoll());
				}
				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELLAGERSOLL] = nLagersoll;

				// PJ 14828

				BigDecimal bdLagerstand = new BigDecimal(0);

				if (Helper.short2Boolean(flrbestellung.getFlrartikel()
						.getB_lagerbewirtschaftet())) {
					for (int j = 0; j < lagerDto.length; j++) {

						if (Helper.short2boolean(lagerDto[j]
								.getBBestellvorschlag())) {
							bdLagerstand = bdLagerstand
									.add(getLagerFac().getLagerstand(
											oArtikelDto.getIId(),
											lagerDto[j].getIId(), theClientDto));
						}

					}
				}

				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELLAGERSTAND] = bdLagerstand;

				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELEINHEIT] = flrbestellung
						.getFlrartikel().getEinheit_c_nr();
				if (flrbestellung.getN_nettoeinzelpreis() != null) {
					nArtikelPreis = flrbestellung.getN_nettoeinzelpreis();
				}
				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELPREIS] = nArtikelPreis;

				nAnzahlArtikelRes = getReservierungFac()
						.getAnzahlReservierungen(
								flrbestellung.getArtikel_i_id(), theClientDto);
				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELRESERVIERUNG] = nAnzahlArtikelRes;
				nFehlMenge = getFehlmengeFac().getAnzahlFehlmengeEinesArtikels(
						flrbestellung.getArtikel_i_id(), theClientDto);
				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELFEHLMENGE] = nFehlMenge;

				// Verwendungsnachweis
				Session session2 = FLRSessionFactory.getFactory().openSession();
				org.hibernate.Criteria critVW = session2
						.createCriteria(FLRStuecklisteposition.class)
						.createAlias(
								com.lp.server.stueckliste.service.StuecklisteFac.FLR_STUECKLISTEPOSITION_FLRARTIKEL,
								"a")
						.add(Restrictions.eq("a.i_id",
								flrbestellung.getArtikel_i_id()))
						.addOrder(Order.asc("a.c_nr"));
				List<?> results = critVW.list();
				Iterator itVW = results.iterator();

				Object[][] dataSub = new Object[results.size()][3];
				String[] fieldnames = new String[] { "F_ARTIKEL",
						"F_BEZEICHNUNG", "F_MENGE" };

				int iZeileSub = 0;
				while (itVW.hasNext()) {
					FLRStuecklisteposition stkpos = (FLRStuecklisteposition) itVW
							.next();

					dataSub[iZeileSub][0] = stkpos.getFlrstueckliste()
							.getFlrartikel().getC_nr();
					ArtikelDto aDto = getArtikelFac()
							.artikelFindByPrimaryKeySmall(
									stkpos.getFlrstueckliste().getFlrartikel()
											.getI_id(), theClientDto);
					dataSub[iZeileSub][1] = aDto.formatBezeichnung();
					dataSub[iZeileSub][2] = stkpos.getN_menge();
					iZeileSub++;
				}
				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_SUBREPORT_VERWENDUNGSNACHWEIS] = new LPDatenSubreport(
						dataSub, fieldnames);
				session2.close();

				i++;
			}

			// die Parameter dem Report uebergeben
			HashMap<String, Object> mapParameter = new HashMap<String, Object>();
			mapParameter.put(LPReport.P_SORTIERUNG,
					buildSortierungBestellvorschlag(krit, theClientDto));
			mapParameter.put(LPReport.P_FILTER,
					buildFilterBestellungOffene(krit, theClientDto));
			mapParameter
					.put("P_SORTIERENACHBELEGART",
							new Boolean(
									krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_BELEGNUMMER));
			mapParameter
					.put("P_SORTIERENACHIDENT",
							new Boolean(
									krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_IDENT));
			mapParameter
					.put(LPReport.P_SORTIERENACHLIEFERANT,
							new Boolean(
									krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER));

			mapParameter.put(P_MANDANTWAEHRUNG,
					theClientDto.getSMandantenwaehrung());

			mapParameter.put("P_ANFRAGEVORSCHLAG", new Boolean(
					bAnfragevorschlag));

			initJRDS(
					mapParameter,
					BestellungReportFac.REPORT_MODUL,
					BestellungReportFac.REPORT_BESTELLUNG_JOURNAL_BESTELLVORSCHLAG,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);

			oPrintO = getReportPrint();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} finally {
			closeSession(session);
		}
		return oPrintO;
	}

	/**
	 * Diese Methode liefert eine Liste von allen offenen Bestellungen eines
	 * Mandanten, die nach den eingegebenen Kriterien des Benutzers
	 * zusammengestellt wird. <br>
	 * Achtung: Hibernate verwendet lazy initialization, d.h. der Zugriff auf
	 * Collections muss innerhalb der Session erfolgen.
	 * 
	 * @param reportJournalKriterienDtoI
	 *            die Kriterien des Benutzers
	 * @param dStichtag
	 *            Date
	 * @param bSortierungNachLiefertermin
	 *            Boolean
	 * @param artikelklasseIId
	 *            Integer
	 * @param artikelgruppeIId
	 *            Integer
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return ReportBestellungOffeneDto[] die Liste der Bestellungen
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	private ReportBestellungOffeneDto[] getListeReportBestellungOffene(
			ReportJournalKriterienDto reportJournalKriterienDtoI,
			Date dStichtag, Boolean bSortierungNachLiefertermin,
			Integer artikelklasseIId, Integer artikelgruppeIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		ReportBestellungOffeneDto[] aResult = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		// vom Stichtag die Uhrzeit abschneiden
		dStichtag = Helper.cutDate(dStichtag);

		try {
			session = factory.openSession();

			// Hiberante Criteria fuer alle Tabellen ausgehend von meiner
			// Haupttabelle anlegen,
			// nach denen ich filtern und sortieren kann
			Criteria crit = session.createCriteria(FLRBestellung.class);

			// Einschraenkung auf den aktuellen Mandanten
			crit.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));

			// Einschraenkung nach Bestelllungart
			crit.add(Restrictions.ne(
					BestellungFac.FLR_BESTELLUNG_BESTELLUNGART_C_NR,
					BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR));
			crit.add(Restrictions.ne(
					BestellungFac.FLR_BESTELLUNG_BESTELLUNGART_C_NR,
					BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR));
			crit.add(Restrictions.ne(
					BestellungFac.FLR_BESTELLUNG_BESTELLUNGART_C_NR,
					BestellungFac.BESTELLUNGART_LEIHBESTELLUNG_C_NR));

			// Einschraenkung nach Status Offen, Erledigt
			Collection<String> cStati = new LinkedList<String>();
			cStati.add(BestellungFac.BESTELLSTATUS_OFFEN);
			cStati.add(BestellungFac.BESTELLSTATUS_BESTAETIGT);
			cStati.add(BestellungFac.BESTELLSTATUS_ERLEDIGT);
			crit.add(Restrictions.in(
					BestellungFac.FLR_BESTELLUNG_BESTELLUNGSTATUS_C_NR, cStati));

			// Das Belegdatum muss vor dem Stichtag liegen
			crit.add(Restrictions.le(BestellungFac.FLR_BESTELLUNG_T_BELEGDATUM,
					dStichtag));

			// Einschraenkung nach einer bestimmten Kostenstelle
			if (reportJournalKriterienDtoI.kostenstelleIId != null) {
				crit.add(Restrictions.eq(
						BestellungFac.FLR_BESTELLUNG_KOSTENSTELLE_I_ID,
						reportJournalKriterienDtoI.kostenstelleIId));
			}

			// Einschraenkung nach einem bestimmten Lieferanten
			if (reportJournalKriterienDtoI.lieferantIId != null) {
				crit.add(Restrictions
						.eq(BestellungFac.FLR_BESTELLUNG_LIEFERANT_I_ID_BESTELLADRESSE,
								reportJournalKriterienDtoI.lieferantIId));
			}

			// Sortierung nach Kostenstelle ist immer die erste Sortierung
			if (reportJournalKriterienDtoI.bSortiereNachKostenstelle) {
				crit.createCriteria(
						BestellungFac.FLR_BESTELLUNG_FLRKOSTENSTELLE).addOrder(
						Order.asc("c_nr"));
			}

			// Sortierung nach Kunde, eventuell innerhalb der Kostenstelle
			if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				crit.createCriteria(BestellungFac.FLR_BESTELLUNG_FLRLIEFERANT)
						.createCriteria(LieferantFac.FLR_PARTNER)
						.addOrder(
								Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));

			}
			// Sortierung nach Projekt, eventuell innerhalb der Kostenstelle
			else if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PROJEKT) {
				crit.addOrder(Order
						.asc(BestellungFac.FLR_BESTELLUNG_C_BEZPROJEKTBEZEICHNUNG));
			}
			// Sortierung nach Liefertermin (optional)
			if (bSortierungNachLiefertermin != null
					&& bSortierungNachLiefertermin.booleanValue()) {
				crit.addOrder(Order
						.asc(BestellungFac.FLR_BESTELLUNG_T_LIEFERTERMIN));
			}

			// es wird in jedem Fall nach der Belegnummer sortiert
			crit.addOrder(Order.asc("c_nr"));

			List<?> list = crit.list();
			aResult = new ReportBestellungOffeneDto[list.size()];
			int iIndex = 0;
			Iterator<?> it = list.iterator();
			ReportBestellungOffeneDto reportDto = null;

			while (it.hasNext()) {
				FLRBestellung flrbestellung = (FLRBestellung) it.next();
				Session session1 = null;
				session1 = factory.openSession();
				Criteria crit1 = session1
						.createCriteria(FLRBestellposition.class);
				// keine erledigten Positionen.
				crit1.add(Restrictions
						.ne(BestellpositionFac.FLR_BESTELLPOSITION_BESTELLPOSITIONSTATUS_C_NR,
								BestellpositionFac.BESTELLPOSITIONSTATUS_ERLEDIGT));
				// und nur die, die sich auf den aktuellen Bestellung beziehen.
				Criteria crit1Bestellung = crit1
						.createCriteria(BestellpositionFac.FLR_BESTELLPOSITION_FLRBESTELLUNG);
				crit1Bestellung.add(Restrictions.eq(
						BestellungFac.FLR_BESTELLUNG_I_ID,
						flrbestellung.getI_id()));
				int anzahlPositionen = 0;
				List<?> resultList = crit1.list();
				for (Iterator<?> iter = resultList.iterator(); iter.hasNext();) {
					FLRBestellposition item = (FLRBestellposition) iter.next();
					if (item.getBestellpositionart_c_nr().equals(
							BestellpositionFac.BESTELLPOSITIONART_IDENT)
							|| item.getBestellpositionart_c_nr()
									.equals(BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE)) {
						anzahlPositionen++;
					}
				}

				reportDto = new ReportBestellungOffeneDto();
				reportDto.setIIdBestellung(flrbestellung.getI_id());
				reportDto.setCNrBestellung(flrbestellung.getC_nr());
				reportDto.setLieferantCName1(flrbestellung.getFlrlieferant()
						.getFlrpartner().getC_name1nachnamefirmazeile1());
				reportDto.setIAnzahlPositionen(anzahlPositionen);

				aResult[iIndex] = reportDto;
				iIndex++;

			}
		} finally {
			closeSession(session);
		}
		return aResult;
	}

	/**
	 * Eine Bestellung drucken.
	 * 
	 * @param iIdBestellungI
	 *            PK der Bestellung
	 * @param iAnzahlKopienI
	 *            Integer
	 * @param bMitLogo
	 *            Boolean
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return JasperPrint der Druck
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public JasperPrintLP[] printBestellung(Integer iIdBestellungI,
			Integer iAnzahlKopienI, Boolean bMitLogo, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Timestamp tStart = new Timestamp(System.currentTimeMillis());
		JasperPrintLP[] prints = null;
		if (iIdBestellungI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdBestellungI == null"));
		}
		BestellungDto bestellungDto = null;
		BestellpositionDto[] aPositionDtos = null;
		// Erstellung des Report
		Boolean bbSeitenumbruch = new Boolean(false); // dieser Wert wechselt
		// mit jedem
		// Seitenumbruch
		// zwischen true und
		// false
		boolean bTermineUnterschiedlich = false;

		useCase = UC_BESTELLUNG;

		try {
			bestellungDto = getBestellungFac().bestellungFindByPrimaryKey(
					iIdBestellungI);

			aPositionDtos = getBestellpositionFac()
					.bestellpositionFindByBestellung(iIdBestellungI,
							theClientDto);

			// den Druckzeitpunkt vermerken
//			bestellungDto.setTGedruckt(getTimestamp());
//			getBestellungFac().updateBestellung(bestellungDto, theClientDto);
			// rk: passiert jetzt in BestellungFacBean.aktiviereBestellung() 

			LieferantDto lieferantDto = getLieferantFac()
					.lieferantFindByPrimaryKey(
							bestellungDto.getLieferantIIdBestelladresse(),
							theClientDto);

			AnsprechpartnerDto oAnsprechpartnerDto = null;

			Integer partnerIIdAnsprechpartner = null;
			if (bestellungDto.getAnsprechpartnerIId() != null) {
				oAnsprechpartnerDto = getAnsprechpartnerFac()
						.ansprechpartnerFindByPrimaryKey(
								bestellungDto.getAnsprechpartnerIId(),
								theClientDto);
				if (oAnsprechpartnerDto != null) {
					partnerIIdAnsprechpartner = oAnsprechpartnerDto
							.getPartnerIIdAnsprechpartner();
				}
			}

			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);

			// es gilt das Locale des Lieferanten
			Locale locDruck = Helper.string2Locale(lieferantDto.getPartnerDto()
					.getLocaleCNrKommunikation());

			int iAnzahlZeilen = aPositionDtos.length; // Anzahl der Zeilen in
			// der Gruppe

			data = new Object[iAnzahlZeilen][BestellungReportFac.REPORT_BESTELLUNG_ANZAHL_SPALTEN];

			// die Datenmatrix befuellen
			for (int i = 0; i < iAnzahlZeilen; i++) {
				data[i][BestellungReportFac.REPORT_BESTELLUNG_POSITIONOBKJEKT] = getSystemReportFac()
						.getPositionForReport(LocaleFac.BELEGART_BESTELLUNG,
								aPositionDtos[i].getIId(), theClientDto);
				data[i][BestellungReportFac.REPORT_BESTELLUNG_POSITION] = getBestellpositionFac()
						.getPositionNummer(aPositionDtos[i].getIId(),
								theClientDto);

				if (aPositionDtos[i].getTUebersteuerterLiefertermin() != null) {
					Timestamp tposLieferTermin = Helper
							.cutTimestamp(aPositionDtos[i]
									.getTUebersteuerterLiefertermin());
					if (!(bestellungDto.getDLiefertermin()
							.equals(tposLieferTermin))) {
						bTermineUnterschiedlich = true;
					}
				}

				// Artikelpositionen
				if (aPositionDtos[i].getPositionsartCNr().equals(
						BestellpositionFac.BESTELLPOSITIONART_IDENT)
						|| aPositionDtos[i]
								.getPositionsartCNr()
								.equals(BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE)) {

					// Typ, wenn Setartikel

					if (aPositionDtos[i].getPositioniIdArtikelset() != null) {

						data[i][BestellungReportFac.REPORT_BESTELLUNG_SETARTIKEL_TYP] = ArtikelFac.SETARTIKEL_TYP_POSITION;

					} else {

						Session session = null;
						SessionFactory factory = FLRSessionFactory.getFactory();
						session = factory.openSession();
						Criteria crit = session
								.createCriteria(FLRBestellposition.class);
						crit.add(Restrictions.eq("position_i_id_artikelset",
								aPositionDtos[i].getIId()));

						int iZeilen = crit.list().size();

						if (iZeilen > 0) {
							data[i][BestellungReportFac.REPORT_BESTELLUNG_SETARTIKEL_TYP] = ArtikelFac.SETARTIKEL_TYP_KOPF;
						}
						session.close();

					}

					if (!bestellungDto.getDLiefertermin().equals(
							aPositionDtos[i].getTUebersteuerterLiefertermin())) {
						data[i][BestellungReportFac.REPORT_BESTELLUNG_POSITIONTERMIN] = Helper
								.formatDatum(
										Helper.extractDate(aPositionDtos[i]
												.getTUebersteuerterLiefertermin()),
										locDruck);

					}

					ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKey(
									aPositionDtos[i].getArtikelIId(),
									theClientDto);
					BelegPositionDruckIdentDto druckDto = printIdent(
							aPositionDtos[i], LocaleFac.BELEGART_BESTELLUNG,
							artikelDto, locDruck, lieferantDto.getPartnerIId(),
							theClientDto);
					data[i][BestellungReportFac.REPORT_BESTELLUNG_ARTIKELCZBEZ2] = druckDto
							.getSArtikelZusatzBezeichnung2();
					// Artikel Bezeichnung
					data[i][BestellungReportFac.REPORT_BESTELLUNG_BEZEICHNUNG] = druckDto
							.getSBezeichnung();
					// Artikel Kurzbezeichnung
					data[i][BestellungReportFac.REPORT_BESTELLUNG_KURZBEZEICHNUNG] = druckDto
							.getSKurzbezeichnung();
					data[i][BestellungReportFac.REPORT_BESTELLUNG_ZUSATZBEZEICHNUNG] = druckDto
							.getSZusatzBezeichnung();
					data[i][BestellungReportFac.REPORT_BESTELLUNG_IDENT] = druckDto
							.getSArtikelInfo();
					data[i][BestellungReportFac.REPORT_BESTELLUNG_ARTIKELKOMMENTAR] = druckDto
							.getSArtikelkommentar();

					data[i][BestellungReportFac.REPORT_BESTELLUNG_REFERENZNUMMER] = artikelDto
							.getCReferenznr();

					InseratDto insDto = getInseratFac()
							.istInseratAufBestellpositionVorhanden(
									aPositionDtos[i].getIId());
					data[i][BestellungReportFac.REPORT_BESTELLUNG_INSERAT_DTO] = insDto;
					if (insDto != null
							&& insDto.getInseratrechnungDto() != null) {
						KundeDto kDto = getKundeFac().kundeFindByPrimaryKey(
								insDto.getInseratrechnungDto().getKundeIId(),
								theClientDto);
						data[i][BestellungReportFac.REPORT_BESTELLUNG_INSERAT_KUNDE] = kDto
								.getPartnerDto().formatTitelAnrede();
					}

					// Wenn Bezug zu Lossollmaterial (PJ 16215)
					if (aPositionDtos[i].getLossollmaterialIId() != null) {
						LossollmaterialDto lossollmaterialDto = getFertigungFac()
								.lossollmaterialFindByPrimaryKey(
										aPositionDtos[i]
												.getLossollmaterialIId());

						LosDto losDto = getFertigungFac().losFindByPrimaryKey(
								lossollmaterialDto.getLosIId());

						data[i][BestellungReportFac.REPORT_BESTELLUNG_SOLLMATERIAL_KOMMENTAR] = lossollmaterialDto
								.getCKommentar();
						data[i][BestellungReportFac.REPORT_BESTELLUNG_SOLLMATERIAL_LOSNUMMER] = losDto
								.getCNr();
						data[i][BestellungReportFac.REPORT_BESTELLUNG_SOLLMATERIAL_LOS_KOMMENTAR] = losDto
								.getCKommentar();
						data[i][BestellungReportFac.REPORT_BESTELLUNG_SOLLMATERIAL_LOS_PROJEKT] = losDto
								.getCProjekt();

						if (losDto.getStuecklisteIId() != null) {

							StuecklisteDto stklDto = getStuecklisteFac()
									.stuecklisteFindByPrimaryKey(
											losDto.getStuecklisteIId(),
											theClientDto);
							data[i][BestellungReportFac.REPORT_BESTELLUNG_SOLLMATERIAL_LOS_STKLNUMMER] = stklDto
									.getArtikelDto().getCNr();
							if (stklDto.getArtikelDto().getArtikelsprDto() != null) {
								data[i][BestellungReportFac.REPORT_BESTELLUNG_SOLLMATERIAL_LOS_STKLBEZ] = stklDto
										.getArtikelDto().getArtikelsprDto()
										.getCBez();
								data[i][BestellungReportFac.REPORT_BESTELLUNG_SOLLMATERIAL_LOS_STKLKBEZ] = stklDto
										.getArtikelDto().getArtikelsprDto()
										.getCKbez();
							}

						}

						LossollarbeitsplanDto[] sollarbeitsplanDtos = getFertigungFac()
								.lossollarbeitsplanFindByLossollmaterialIId(
										lossollmaterialDto.getIId(),
										theClientDto);

						Object[][] oSubData = new Object[sollarbeitsplanDtos.length][13];

						for (int s = 0; s < sollarbeitsplanDtos.length; s++) {
							LossollarbeitsplanDto lossollarbeitsplanDto = sollarbeitsplanDtos[s];

							ArtikelDto artikelDtoSollarbeitsplan = getArtikelFac()
									.artikelFindByPrimaryKeySmall(
											lossollarbeitsplanDto
													.getArtikelIIdTaetigkeit(),
											theClientDto);
							oSubData[s][0] = artikelDtoSollarbeitsplan.getCNr();
							oSubData[s][1] = artikelDtoSollarbeitsplan
									.formatBezeichnung();

							oSubData[s][2] = new BigDecimal(
									lossollarbeitsplanDto.getLStueckzeit())
									.divide(new BigDecimal(3600000), 4,
											BigDecimal.ROUND_HALF_EVEN);
							oSubData[s][3] = new BigDecimal(
									lossollarbeitsplanDto.getLRuestzeit())
									.divide(new BigDecimal(3600000), 4,
											BigDecimal.ROUND_HALF_EVEN);

							oSubData[s][4] = lossollarbeitsplanDto
									.getIArbeitsgangnummer();
							oSubData[s][5] = lossollarbeitsplanDto
									.getIUnterarbeitsgang();
							oSubData[s][6] = lossollarbeitsplanDto
									.getCKomentar();
							oSubData[s][7] = lossollarbeitsplanDto.getXText();

						}

						String[] fieldnames = new String[] { "F_ARTIKEL",
								"F_BEZEICHNUNG", "F_STUECKZEIT", "F_RUESTZEIT",
								"F_AGNUMMER", "F_UAGNUMMER", "F_KOMMENTAR",
								"F_TEXT" };
						data[i][BestellungReportFac.REPORT_BESTELLUNG_SOLLMATERIAL_SUBREPORT_ARBEITSGAENGE] = new LPDatenSubreport(
								oSubData, fieldnames);

					}

					// Lieferantendaten: Artikelnummer, Bezeichnung
					ArtikellieferantDto artikellieferantDto = getArtikelFac()
							.artikellieferantFindByArtikellIIdLieferantIIdOhneExc(
									aPositionDtos[i].getArtikelIId(),
									bestellungDto
											.getLieferantIIdBestelladresse(),
									theClientDto);
					if (artikellieferantDto != null) {

						if (Helper.short2boolean(artikellieferantDto
								.getBHerstellerbez()) == true) {
							// PJ18293
								data[i][BestellungReportFac.REPORT_BESTELLUNG_LIEFERANT_ARTIKEL_IDENTNUMMER] = artikelDto
										.getCArtikelnrhersteller();
								data[i][BestellungReportFac.REPORT_BESTELLUNG_LIEFERANT_ARTIKEL_BEZEICHNUNG] = artikelDto
										.getCArtikelbezhersteller();
							
						} else {
							if (artikellieferantDto.getCArtikelnrlieferant() != null) {
								data[i][BestellungReportFac.REPORT_BESTELLUNG_LIEFERANT_ARTIKEL_IDENTNUMMER] = artikellieferantDto
										.getCArtikelnrlieferant();
							}
							if (artikellieferantDto.getCBezbeilieferant() != null) {
								data[i][BestellungReportFac.REPORT_BESTELLUNG_LIEFERANT_ARTIKEL_BEZEICHNUNG] = artikellieferantDto
										.getCBezbeilieferant();
							}
						}

						data[i][BestellungReportFac.REPORT_BESTELLUNG_LIEFERANT_VERPACKUNGSEINHEIT] = artikellieferantDto
								.getNVerpackungseinheit();

					}

					if (artikelDto.getHerstellerIId() != null) {
						HerstellerDto herstellerDto = getArtikelFac()
								.herstellerFindByPrimaryKey(
										artikelDto.getHerstellerIId(),
										theClientDto);
						data[i][BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_HERSTELLER] = herstellerDto
								.getCNr();
						PartnerDto partnerDto = getPartnerFac()
								.partnerFindByPrimaryKey(
										herstellerDto.getPartnerIId(),
										theClientDto);
						data[i][BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_HERSTELLER_NAME] = partnerDto
								.formatFixName1Name2();
					}

					if (artikelDto.getEinheitCNrBestellung() != null
							&& artikelDto.getNUmrechnungsfaktor().doubleValue() != 0) {

						boolean bInvers = Helper.short2boolean(artikelDto
								.getbBestellmengeneinheitInvers());

						BigDecimal gewicht = null;

						if (bInvers) {
							gewicht = aPositionDtos[i].getNMenge().divide(
									artikelDto.getNUmrechnungsfaktor(), 4,
									BigDecimal.ROUND_HALF_EVEN);
						} else {
							gewicht = aPositionDtos[i].getNMenge().multiply(
									artikelDto.getNUmrechnungsfaktor());
						}

						BigDecimal umrechnung = new BigDecimal(0);
						gewicht = Helper.rundeKaufmaennisch(gewicht, 4);
						data[i][BestellungReportFac.REPORT_BESTELLUNG_GEWICHT] = gewicht;
						data[i][BestellungReportFac.REPORT_BESTELLUNG_GEWICHTEINHEIT] = getSystemFac()
								.formatEinheit(
										artikelDto.getEinheitCNrBestellung(),
										locDruck, theClientDto);

						if (bInvers) {
							umrechnung = aPositionDtos[i]
									.getNNettoeinzelpreis().multiply(
											artikelDto.getNUmrechnungsfaktor());
						} else {
							umrechnung = aPositionDtos[i]
									.getNNettoeinzelpreis().divide(
											artikelDto.getNUmrechnungsfaktor(),
											4, BigDecimal.ROUND_HALF_EVEN);
						}

						data[i][BestellungReportFac.REPORT_BESTELLUNG_PREISPEREINHEIT] = umrechnung;
					}

					// PJ 14867
					if (aPositionDtos[i].getIBestellpositionIIdRahmenposition() != null
							&& bestellungDto
									.getBestellungartCNr()
									.equals(BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR)) {
						Session sessionRahmen = FLRSessionFactory.getFactory()
								.openSession();

						String query = "FROM FLRBestellpositionReport bs WHERE bs.i_id="
								+ aPositionDtos[i]
										.getIBestellpositionIIdRahmenposition();

						Query q = sessionRahmen.createQuery(query);

						List l = q.list();

						if (l.size() > 0) {
							FLRBestellpositionReport eintrag = (FLRBestellpositionReport) l
									.iterator().next();

							BigDecimal offeneMenge = eintrag.getN_menge();

							for (Iterator<?> iter = eintrag
									.getAbrufpositionenset().iterator(); iter
									.hasNext();) {
								FLRBestellpositionReport item = (FLRBestellpositionReport) iter
										.next();
								if (item.getFlrbestellung().getT_belegdatum()
										.getTime() <= bestellungDto
										.getDBelegdatum().getTime()) {
									offeneMenge = offeneMenge.subtract(item
											.getN_menge());
								}
							}

							data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENERAHMENMENGE] = offeneMenge;

						}

						sessionRahmen.close();

					}

					if (aPositionDtos[i].getPositionsartCNr().equals(
							BestellpositionFac.BESTELLPOSITIONART_IDENT)) {
						data[i][BestellungReportFac.REPORT_BESTELLUNG_IDENTNUMMER] = artikelDto
								.getCNr();
						data[i][BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_INDEX] = artikelDto
								.getCIndex();
						data[i][BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_REVISION] = artikelDto
								.getCRevision();

						if (artikelDto.getVerpackungDto() != null) {
							data[i][BestellungReportFac.REPORT_BESTELLUNG_BAUFORM] = artikelDto
									.getVerpackungDto().getCBauform();
							data[i][BestellungReportFac.REPORT_BESTELLUNG_VERPACKUNGSART] = artikelDto
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
								data[i][BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_MATERIAL] = materialDto
										.getMaterialsprDto().getCBez();
							} else {
								data[i][BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_MATERIAL] = materialDto
										.getCNr();
							}

							MaterialzuschlagDto mzDto = getMaterialFac()
									.getKursMaterialzuschlagDtoInZielwaehrung(
											artikelDto.getMaterialIId(),
											bestellungDto.getDBelegdatum(),
											bestellungDto.getWaehrungCNr(),
											theClientDto);

							data[i][BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_KURS_MATERIALZUSCHLAG] = mzDto
									.getNZuschlag();
							data[i][BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_DATUM_MATERIALZUSCHLAG] = mzDto
									.getTGueltigab();

						}

						data[i][BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_MATERIALGEWICHT] = artikelDto
								.getFMaterialgewicht();

						if (artikelDto.getGeometrieDto() != null) {
							data[i][BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_BREITE] = artikelDto
									.getGeometrieDto().getFBreite();
							data[i][BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_HOEHE] = artikelDto
									.getGeometrieDto().getFHoehe();
							data[i][BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_TIEFE] = artikelDto
									.getGeometrieDto().getFTiefe();
						}

					}
					data[i][BestellungReportFac.REPORT_BESTELLUNG_MENGE] = aPositionDtos[i]
							.getNMenge();
					data[i][BestellungReportFac.REPORT_BESTELLUNG_EINHEIT] = getSystemFac()
							.formatEinheit(aPositionDtos[i].getEinheitCNr(),
									locDruck, theClientDto);

					data[i][BestellungReportFac.REPORT_BESTELLUNG_ANGEBOTSNUMMER] = aPositionDtos[i]
							.getCAngebotnummer();

					if (aPositionDtos[i].getPositioniIdArtikelset() == null) {
						data[i][BestellungReportFac.REPORT_BESTELLUNG_EINZELPREIS] = aPositionDtos[i]
								.getNNettoeinzelpreis();
						data[i][BestellungReportFac.REPORT_BESTELLUNG_MATERIALZUSCHLAG] = aPositionDtos[i]
								.getNMaterialzuschlag();

						data[i][BestellungReportFac.REPORT_BESTELLUNG_RABATT] = aPositionDtos[i]
								.getDRabattsatz();

						BigDecimal bdGesamtpreis = null;

						if (aPositionDtos[i].getNNettogesamtpreis() != null
								&& aPositionDtos[i].getNMenge() != null) {
							bdGesamtpreis = aPositionDtos[i]
									.getNNettogesamtpreis().multiply(
											aPositionDtos[i].getNMenge());
						}

						data[i][BestellungReportFac.REPORT_BESTELLUNG_GESAMTPREIS] = bdGesamtpreis;
					}
					/**
					 * @todo MR-> Wird nicht mehr benoetigt, da
					 *       LPReport.printIdent das abbildet. Wegen kurzer Zeit
					 *       vor deploy nicht mehr geloescht.
					 */
					ArrayList<Object> images = new ArrayList<Object>();
					ArtikelkommentarDto[] artikelkommentarDto = getArtikelkommentarFac()
							.artikelkommentardruckFindByArtikelIIdBelegartCNr(
									aPositionDtos[i].getArtikelIId(),
									LocaleFac.BELEGART_BESTELLUNG,
									theClientDto.getLocUiAsString(),
									theClientDto);

					if (artikelkommentarDto != null
							&& artikelkommentarDto.length > 0) {
						String cPositionKommentarText = "";

						for (int j = 0; j < artikelkommentarDto.length; j++) {
							if (artikelkommentarDto[j]
									.getArtikelkommentarsprDto() != null) {
								String cDatenformat = artikelkommentarDto[j]
										.getDatenformatCNr();
								if (cDatenformat
										.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG)
										|| artikelkommentarDto[j]
												.getDatenformatCNr()
												.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG)
										|| artikelkommentarDto[j]
												.getDatenformatCNr()
												.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF)) {
									byte[] bild = artikelkommentarDto[j]
											.getArtikelkommentarsprDto()
											.getOMedia();
									if (bild != null) {
										java.awt.Image myImage = Helper
												.byteArrayToImage(bild);
										data[i][BestellungReportFac.REPORT_BESTELLUNG_IMAGE] = myImage;
										images.add(myImage);
									}
								} else if (artikelkommentarDto[j]
										.getDatenformatCNr()
										.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF)) {

									byte[] bild = artikelkommentarDto[j]
											.getArtikelkommentarsprDto()
											.getOMedia();

									java.awt.Image[] tiffs = Helper
											.tiffToImageArray(bild);
									if (tiffs != null) {
										for (int k = 0; k < tiffs.length; k++) {
											images.add(tiffs[k]);
										}
									}

								}
							}

						}
					}
				}

				// Betrifft Positionen
				if (aPositionDtos[i].getPositionsartCNr().equals(
						BestellpositionFac.BESTELLPOSITIONART_BETRIFFT)) {
					data[i][BestellungReportFac.REPORT_BESTELLUNG_FREIERTEXT] = aPositionDtos[i]
							.getCBez();
				}

				// Texteingabe Positionen
				if (aPositionDtos[i].getPositionsartCNr().equals(
						BestellpositionFac.BESTELLPOSITIONART_TEXTEINGABE)) {
					// IMS 1619 leerer Text soll als Leerezeile erscheinen
					String sText = aPositionDtos[i].getXTextinhalt();

					if (sText != null && sText.trim().equals("")) {
						data[i][BestellungReportFac.REPORT_BESTELLUNG_LEERZEILE] = " ";
					} else {
						data[i][BestellungReportFac.REPORT_BESTELLUNG_FREIERTEXT] = sText;
					}
				}

				// Textbaustein Positionen
				if (aPositionDtos[i].getPositionsartCNr().equals(
						BestellpositionFac.BESTELLPOSITIONART_TEXTBAUSTEIN)) {
					// Dto holen
					MediastandardDto oMediastandardDto = getMediaFac()
							.mediastandardFindByPrimaryKey(
									aPositionDtos[i].getMediastandardIId());
					// zum Drucken vorbereiten
					BelegPositionDruckTextbausteinDto druckDto = printTextbaustein(
							oMediastandardDto, theClientDto);
					data[i][BestellungReportFac.REPORT_BESTELLUNG_FREIERTEXT] = druckDto
							.getSFreierText();
					data[i][BestellungReportFac.REPORT_BESTELLUNG_IMAGE] = druckDto
							.getOImage();
				}

				// Leerzeile Positionen
				if (aPositionDtos[i].getPositionsartCNr().equals(
						BestellpositionFac.BESTELLPOSITIONART_LEERZEILE)) {
					data[i][BestellungReportFac.REPORT_BESTELLUNG_LEERZEILE] = " ";
				}

				// Seitenumbruch Positionen
				if (aPositionDtos[i].getPositionsartCNr().equals(
						BestellpositionFac.BESTELLPOSITIONART_SEITENUMBRUCH)) {
					bbSeitenumbruch = new Boolean(
							!bbSeitenumbruch.booleanValue()); // toggle
				}

				data[i][BestellungReportFac.REPORT_BESTELLUNG_SEITENUMBRUCH] = bbSeitenumbruch;
				data[i][BestellungReportFac.REPORT_BESTELLUNG_POSITIONSART] = aPositionDtos[i]
						.getPositionsartCNr();

			}

			// Kopftext
			String sKopftext = bestellungDto.getCKopftextUebersteuert();

			if (sKopftext == null || sKopftext.length() == 0) {
				BestellungtextDto oBestellungtextDto = getBestellungServiceFac()
						.bestellungtextFindByMandantLocaleCNr(
								theClientDto.getMandant(),
								lieferantDto.getPartnerDto()
										.getLocaleCNrKommunikation(),
								BestellungServiceFac.BESTELLUNGTEXT_KOPFTEXT,
								theClientDto);
				sKopftext = oBestellungtextDto.getXTextinhalt();
			}

			// Fusstext
			String sFusstext = bestellungDto.getCFusstextUebersteuert();

			if (sFusstext == null || sFusstext.length() == 0) {
				BestellungtextDto oBestellungtextDto = getBestellungServiceFac()
						.bestellungtextFindByMandantLocaleCNr(
								theClientDto.getMandant(),
								lieferantDto.getPartnerDto()
										.getLocaleCNrKommunikation(),
								BestellungServiceFac.BESTELLUNGTEXT_FUSSTEXT,
								theClientDto);

				sFusstext = oBestellungtextDto.getXTextinhalt();
			}

			// Erstellung des Report
			HashMap<String, Object> mapParameter = new HashMap<String, Object>();
			// CK: PJ 13849
			mapParameter.put(
					"P_BEARBEITER",
					getPersonalFac()
							.getPersonRpt(
									bestellungDto.getPersonalIIdAnlegen(),
									theClientDto));
			// die Parameter uebergeben
			if (bestellungDto.getBTeillieferungMoeglich() != null) {
				mapParameter.put(
						"P_TEILLIEFERUNGMOEGLICH",
						new Boolean(Helper.short2boolean(bestellungDto
								.getBTeillieferungMoeglich())));
			}

			String sSpediteur = null;
			if (bestellungDto.getSpediteurIId() != null) {
				SpediteurDto spediteurDto = getMandantFac()
						.spediteurFindByPrimaryKey(
								bestellungDto.getSpediteurIId());
				sSpediteur = spediteurDto.getCNamedesspediteurs();

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

					mapParameter.put(
							"P_SPEDITEUR_ADRESSBLOCK",
							formatAdresseFuerAusdruck(partnerDto,
									ansprechpartnerDtoSpediteur, mandantDto,
									locDruck));
				}

			}
			mapParameter.put("P_SPEDITEUR", sSpediteur != null ? sSpediteur
					: "");

			mapParameter.put("P_LIEFERANTENANGEBOT",
					bestellungDto.getCLieferantenangebot());

			mapParameter.put("P_LIEFERANTINTERNERKOMMENTAR",
					lieferantDto.getCHinweisintern());
			mapParameter.put("P_LIEFERANTEXTERNERKOMMENTAR",
					lieferantDto.getCHinweisextern());

			if (bestellungDto.getAuftragIId() != null) {
				AuftragDto auftragDto = getAuftragFac()
						.auftragFindByPrimaryKey(bestellungDto.getAuftragIId());
				mapParameter.put("P_AUFTRAGSNUMMER", auftragDto.getCNr());
			}
			if (bestellungDto.getStatusCNr().equals(
					BestellpositionFac.BESTELLPOSITIONSTATUS_STORNIERT)) {
				mapParameter.put("P_STORNIERT", true);
			} else {
				mapParameter.put("P_STORNIERT", false);
			}
			mapParameter.put("Mandantenadresse",
					Helper.formatMandantAdresse(mandantDto));
			mapParameter.put("P_LIEFERADRESSE_IST_NICHT_MANDANT", new Boolean(
					true));

			if (bestellungDto.getPartnerIIdLieferadresse() != null) {
				PartnerDto lieferadresseDto = getPartnerFac()
						.partnerFindByPrimaryKey(
								bestellungDto.getPartnerIIdLieferadresse(),
								theClientDto);

				// SP581

				if (lieferantDto.getPartnerDto().getLandplzortDto() != null
						&& lieferadresseDto.getLandplzortDto() != null
						&& !lieferantDto
								.getPartnerDto()
								.getLandplzortDto()
								.getLandDto()
								.getIID()
								.equals(lieferadresseDto.getLandplzortDto()
										.getLandDto().getIID())) {

					if (lieferadresseDto.getLandplzortDto().getLandDto()
							.getLandIIdGemeinsamespostland() != null
							&& lieferadresseDto
									.getLandplzortDto()
									.getLandDto()
									.getLandIIdGemeinsamespostland()
									.equals(lieferantDto.getPartnerDto()
											.getLandplzortDto().getIlandID())) {
						mapParameter.put(
								"P_LIEFERADRESSE",
								formatAdresseFuerAusdruck(lieferadresseDto,
										null, mandantDto,
										Helper.string2Locale(lieferadresseDto
												.getLocaleCNrKommunikation()),
										LocaleFac.BELEGART_BESTELLUNG));
					} else if (lieferantDto.getPartnerDto().getLandplzortDto()
							.getLandDto().getLandIIdGemeinsamespostland() != null
							&& lieferantDto
									.getPartnerDto()
									.getLandplzortDto()
									.getLandDto()
									.getLandIIdGemeinsamespostland()
									.equals(lieferadresseDto.getLandplzortDto()
											.getIlandID())) {
						mapParameter.put(
								"P_LIEFERADRESSE",
								formatAdresseFuerAusdruck(lieferadresseDto,
										null, mandantDto,
										Helper.string2Locale(lieferadresseDto
												.getLocaleCNrKommunikation()),
										LocaleFac.BELEGART_BESTELLUNG));
					} else {
						mapParameter.put(
								"P_LIEFERADRESSE",
								formatAdresseFuerAusdruck(lieferadresseDto,
										null, mandantDto,
										Helper.string2Locale(lieferadresseDto
												.getLocaleCNrKommunikation()),
										true, LocaleFac.BELEGART_BESTELLUNG));
					}

				} else {
					mapParameter.put(
							"P_LIEFERADRESSE",
							formatAdresseFuerAusdruck(lieferadresseDto, null,
									mandantDto,
									Helper.string2Locale(lieferadresseDto
											.getLocaleCNrKommunikation()),
									LocaleFac.BELEGART_BESTELLUNG));
				}

				mapParameter.put("P_LIEFERADRESSENICHTNULL", new Boolean(true));

				if (mandantDto.getPartnerIId().equals(
						bestellungDto.getPartnerIIdLieferadresse())) {
					mapParameter.put("P_LIEFERADRESSE_IST_NICHT_MANDANT",
							new Boolean(false));
				}

			}
			if (lieferantDto.getPartnerRechnungsadresseDto() != null) {
				mapParameter.put(
						"P_RECHNUNGADRESSE",
						formatAdresseFuerAusdruck(lieferantDto
								.getPartnerRechnungsadresseDto(),
								oAnsprechpartnerDto, mandantDto,
								Helper.string2Locale(lieferantDto
										.getPartnerRechnungsadresseDto()
										.getLocaleCNrKommunikation()),
								LocaleFac.BELEGART_BESTELLUNG));
				if (oAnsprechpartnerDto != null) {
					mapParameter
							.put("P_ANSPRECHPARTNER_RECHNUNGADRESSE",
									getPartnerFac()
											.formatFixAnredeTitelName2Name1FuerAdresskopf(
													oAnsprechpartnerDto
															.getPartnerDto(),
													Helper.string2Locale(lieferantDto
															.getPartnerRechnungsadresseDto()
															.getLocaleCNrKommunikation()),
													null));
				}
			}

			mapParameter.put(
					"Adressefuerausdruck",
					formatAdresseFuerAusdruck(lieferantDto.getPartnerDto(),
							oAnsprechpartnerDto, mandantDto, Helper
									.string2Locale(lieferantDto.getPartnerDto()
											.getLocaleCNrKommunikation()),
							LocaleFac.BELEGART_BESTELLUNG));

			if (oAnsprechpartnerDto != null) {
				mapParameter.put(
						"P_ANSPRECHPARTNER_ADRESSBLOCK",
						getPartnerFac()
								.formatFixAnredeTitelName2Name1FuerAdresskopf(
										oAnsprechpartnerDto.getPartnerDto(),
										Helper.string2Locale(lieferantDto
												.getPartnerDto()
												.getLocaleCNrKommunikation()),
										null));
			}

			if (bestellungDto.getAnfrageIId() != null) {
				AnfrageDto anfrageDto = getAnfrageFac()
						.anfrageFindByPrimaryKey(bestellungDto.getAnfrageIId(),
								theClientDto);
				mapParameter.put("P_ANFRAGENUMMER", anfrageDto.getCNr());
			}

			/**
			 * @todo fuer diesen Block eine zentrale Methode bauen.
			 */

			// die partnerIId des ansprechpartners brauch ich
			// daten holen
			String sEmail = getPartnerFac()
					.partnerkommFindRespectPartnerAsStringOhneExec(
							partnerIIdAnsprechpartner,
							lieferantDto.getPartnerDto(),
							PartnerFac.KOMMUNIKATIONSART_EMAIL,
							theClientDto.getMandant(), theClientDto);

			String sFax = getPartnerFac()
					.partnerkommFindRespectPartnerAsStringOhneExec(
							partnerIIdAnsprechpartner,
							lieferantDto.getPartnerDto(),
							PartnerFac.KOMMUNIKATIONSART_FAX,
							theClientDto.getMandant(), theClientDto);

			String sTelefon = getPartnerFac()
					.partnerkommFindRespectPartnerAsStringOhneExec(
							partnerIIdAnsprechpartner,
							lieferantDto.getPartnerDto(),
							PartnerFac.KOMMUNIKATIONSART_TELEFON,
							theClientDto.getMandant(), theClientDto);

			// daten als parameter an den Report weitergeben
			mapParameter.put(LPReport.P_ANSPRECHPARTNEREMAIL,
					sEmail != null ? sEmail : "");
			mapParameter.put(LPReport.P_ANSPRECHPARTNERFAX, sFax != null ? sFax
					: "");
			mapParameter.put(LPReport.P_ANSPRECHPARTNERTELEFON,
					sTelefon != null ? sTelefon : "");

			mapParameter.put("P_LIEFERANT_UID", lieferantDto.getPartnerDto()
					.getCUid());
			mapParameter.put("P_LIEFERANT_EORI", lieferantDto.getPartnerDto()
					.getCEori());
			if (lieferantDto.getKontoIIdKreditorenkonto() != null) {
				mapParameter.put(
						"P_KREDITORENKONTO",
						getFinanzFac().kontoFindByPrimaryKey(
								lieferantDto.getKontoIIdKreditorenkonto())
								.getCNr());
			}
			mapParameter.put("P_KUNDENNUMMER", lieferantDto.getCKundennr());

			String cLabelRahmenOderLiefertermin = getTextRespectUISpr(
					"lp.liefertermin", theClientDto.getMandant(), locDruck);
			String cLabelBestellungnr = null;

			if (bestellungDto.getBestellungartCNr().equals(
					BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR)) {
				cLabelRahmenOderLiefertermin = getTextRespectUISpr(
						"lp.rahmentermin", theClientDto.getMandant(), locDruck);
				cLabelBestellungnr = getTextRespectUISpr(
						"bes.bestellungnr.rahmen", theClientDto.getMandant(),
						locDruck);
			} else if (bestellungDto.getBestellungartCNr().equals(
					BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR)) {

				BestellungDto rahmenbestellungDto = getBestellungFac()
						.bestellungFindByPrimaryKey(
								bestellungDto
										.getIBestellungIIdRahmenbestellung());
				cLabelBestellungnr = getTextRespectUISpr(
						"bes.bestellungnr.abrufrahmen",
						theClientDto.getMandant(), locDruck)
						+ " " + rahmenbestellungDto.getCNr();
			} else if (bestellungDto.getBestellungartCNr().equals(
					BestellungFac.BESTELLUNGART_LEIHBESTELLUNG_C_NR)) {
				cLabelBestellungnr = getTextRespectUISpr(
						"bes.bestellungnr.leihbestellung",
						theClientDto.getMandant(), locDruck);
			} else {
				cLabelBestellungnr = getTextRespectUISpr(
						"bes.bestellungnr.bestellung",
						theClientDto.getMandant(), locDruck);
			}

			mapParameter.put("P_LABELBESTELLUNGNR", cLabelBestellungnr);
			mapParameter.put("P_BELEGKENNUNG",
					baueKennungBestellung(bestellungDto, theClientDto));

			mapParameter.put("P_LABELRAHMENODERLIEFERTERMIN",
					cLabelRahmenOderLiefertermin);

			mapParameter.put("P_LIEFERTERMIN", Helper.formatDatum(
					bestellungDto.getDLiefertermin(), locDruck));
			mapParameter.put("P_POENALE",
					Helper.short2Boolean(bestellungDto.getBPoenale()));

			mapParameter.put(
					"P_LIEFERART",
					getLocaleFac().lieferartFindByIIdLocaleOhneExc(
							bestellungDto.getLieferartIId(), locDruck,
							theClientDto));
			mapParameter.put("P_LIEFERART_ORT",
					bestellungDto.getCLieferartort());

			mapParameter.put(
					"P_ZAHLUNGSKONDITION",
					getMandantFac().zahlungszielFindByIIdLocaleOhneExc(
							bestellungDto.getZahlungszielIId(), locDruck,
							theClientDto));
			mapParameter.put("P_PROJEKTBEZ", bestellungDto.getCBez());

			if (bestellungDto.getProjektIId() != null) {
				ProjektDto pjDto = getProjektFac().projektFindByPrimaryKey(
						bestellungDto.getProjektIId());
				mapParameter.put("P_PROJEKTNUMMER", pjDto.getCNr());
			}

			if (bestellungDto.getKostenstelleIId() != null) {
				KostenstelleDto kostenstelleDto = getSystemFac()
						.kostenstelleFindByPrimaryKey(
								bestellungDto.getKostenstelleIId());
				mapParameter.put(LPReport.P_KOSTENSTELLE,
						kostenstelleDto.getCNr());
			}

			String cBriefanrede = "";

			if (oAnsprechpartnerDto != null) {
				cBriefanrede = getPartnerServicesFac().getBriefanredeFuerBeleg(
						bestellungDto.getAnsprechpartnerIId(),
						lieferantDto.getPartnerIId(), locDruck, theClientDto);
			} else {
				// neutrale Anrede
				cBriefanrede = getBriefanredeNeutralOderPrivatperson(
						lieferantDto.getPartnerIId(), locDruck, theClientDto);
			}

			mapParameter.put("Briefanrede", cBriefanrede);

			PersonalDto oPersonalBenutzer = getPersonalFac()
					.personalFindByPrimaryKey(theClientDto.getIDPersonal(),
							theClientDto);
			PersonalDto oPersonalAnleger = getPersonalFac()
					.personalFindByPrimaryKey(
							bestellungDto.getPersonalIIdAnlegen(), theClientDto);
			mapParameter.put("P_UNSER_ZEICHEN", Helper.getKurzzeichenkombi(
					oPersonalBenutzer.getCKurzzeichen(),
					oPersonalAnleger.getCKurzzeichen()));

			mapParameter.put("P_BELEGDATUM", Helper.formatDatum(
					bestellungDto.getDBelegdatum(), locDruck));
			mapParameter.put("P_WAEHRUNG", bestellungDto.getWaehrungCNr());
			mapParameter.put("P_ALLGEMEINER_RABATT",
					bestellungDto.getFAllgemeinerRabattsatz());
			mapParameter.put("P_ALLGEMEINER_RABATT_STRING", Helper.formatZahl(
					bestellungDto.getFAllgemeinerRabattsatz(), locDruck));

			mapParameter.put("P_TERMINEUNTERSCHIEDLICH", new Boolean(
					bTermineUnterschiedlich));

			mapParameter.put("Kopftext",
					Helper.formatStyledTextForJasper(sKopftext));
			mapParameter.put("P_BELEGWERT", bestellungDto.getNBestellwert());

			// die folgenden Felder werden im Report mit jeweils einer
			// trennenden Leerzeile
			// hintereinandergehaengt
			// MR 20071014 Folgende Felder muessen auch einzeln an Report
			// uebergeben werden.
			StringBuffer buff = new StringBuffer();

			// Externer Kommentar
			if (bestellungDto.getXExternerKommentar() != null
					&& bestellungDto.getXExternerKommentar().length() > 0) {
				mapParameter.put(P_EXTERNERKOMMENTAR, Helper
						.formatStyledTextForJasper(bestellungDto
								.getXExternerKommentar()));
				buff.append(bestellungDto.getXExternerKommentar()).append(
						"\n\n");
			}

			// Fusstext
			if (sFusstext != null) {
				mapParameter.put("P_FUSSTEXT",
						Helper.formatStyledTextForJasper(sFusstext));
				buff.append(sFusstext).append("\n\n");
			}

			StringBuffer buffVertreter = new StringBuffer();
			// Anrede des Mandanten
			String sMandantAnrede = mandantDto.getPartnerDto()
					.formatFixName1Name2();
			if (sMandantAnrede != null) {
				mapParameter.put(P_MANDANT_ANREDE_UND_NAME, sMandantAnrede);
				buff.append(sMandantAnrede).append("\n\n");
				buffVertreter.append(sMandantAnrede).append("\n\n");
			}

			// P_SUMMARY: Die Unterschrift fuer Belege inclusive
			// Unterschriftstext und -funktion
			// Beispiel:
			// "i.A. Ing. Werner Hehenwarter" - im Falle der Bestellung der
			// Vertreter aus den Kopfdaten
			// "Einkaufsleiter"
			PersonalDto vertreterDto = null;

			if (bestellungDto.getPersonalIIdAnforderer() != null) {
				vertreterDto = getPersonalFac().personalFindByPrimaryKey(
						bestellungDto.getPersonalIIdAnforderer(), theClientDto);
			}

			if (vertreterDto != null) {
				String sVertreterUFTitelName2Name1 = vertreterDto
						.formatFixUFTitelName2Name1();

				mapParameter.put(P_VERTRETER, vertreterDto.formatAnrede());

				mapParameter.put(P_VERTRETER_UNTERSCHRIFTSFUNKTION_UND_NAME,
						sVertreterUFTitelName2Name1);
				buff.append(sVertreterUFTitelName2Name1);
				buffVertreter.append(sVertreterUFTitelName2Name1);

				if (vertreterDto.getCUnterschriftstext() != null
						&& vertreterDto.getCUnterschriftstext().length() > 0) {
					String sUnterschriftstext = vertreterDto
							.getCUnterschriftstext();
					mapParameter.put(P_VERTRETER_UNTERSCHRIFTSTEXT,
							sUnterschriftstext);
					buff.append("\n").append(sUnterschriftstext);
					buffVertreter.append("\n").append(sUnterschriftstext);
				}
			}
			mapParameter.put("P_SUMMARY",
					Helper.formatStyledTextForJasper(buff.toString()));

			mapParameter.put("P_STORNIERUNGSDATUM",
					bestellungDto.getTStorniert());
			mapParameter.put("P_AENDERUNGSBESTELLUNGSDATUM",
					bestellungDto.getTAenderungsbestellung());
			if (bestellungDto.getTAenderungsbestellung() != null) {
				mapParameter.put("P_AENDERUNGSBESTELLUNG", new Boolean(true));
			} else {
				mapParameter.put("P_AENDERUNGSBESTELLUNG", new Boolean(false));
			}

			if (bestellungDto.getAnsprechpartnerIIdLieferadresse() != null) {
				AnsprechpartnerDto ansprechpartnerLieferadresseDto = getAnsprechpartnerFac()
						.ansprechpartnerFindByPrimaryKey(
								bestellungDto
										.getAnsprechpartnerIIdLieferadresse(),
								theClientDto);
				mapParameter.put("P_ANSPRECHPARTNER_LIEFERADRESSE",
						ansprechpartnerLieferadresseDto.getPartnerDto()
								.formatFixTitelName1Name2());
			}
			// Die Anzahl der Exemplare ist 1 + Anzahl der Kopien
			int iExemplare;
			if (iAnzahlKopienI == null || iAnzahlKopienI.intValue() <= 0) {
				iExemplare = 1;
			} else {
				iExemplare = 1 + iAnzahlKopienI.intValue();
			}

			prints = new JasperPrintLP[iExemplare];

			// Print-Array in der Schleife befuellen
			for (int iKopieNummer = 0; iKopieNummer < iExemplare; iKopieNummer++) {
				// jede Kopie erhaelt seine Kopiennummer
				// die "0." Kopie ist das Original und kriegt deshalb keine
				if (iKopieNummer > 0) {
					mapParameter.put(LPReport.P_KOPIE_NUMMER, new Integer(
							iKopieNummer));
				}
				// Index zuruecksetzen
				index = -1;

				initJRDS(mapParameter, BestellungReportFac.REPORT_MODUL,
						BestellungReportFac.REPORT_BESTELLUNG,
						theClientDto.getMandant(), locDruck, theClientDto,
						bMitLogo.booleanValue(),
						bestellungDto.getKostenstelleIId());
				prints[iKopieNummer] = getReportPrint();
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		PrintInfoDto values = getJCRDocFac().getPathAndPartnerAndTable(
				bestellungDto.getIId(), QueryParameters.UC_ID_BESTELLUNG,
				theClientDto);
		prints[0].setOInfoForArchive(values);
		prints[0].putAdditionalInformation(JasperPrintLP.KEY_BELEGART,
				LocaleFac.BELEGART_BESTELLUNG);
		prints[0].putAdditionalInformation(JasperPrintLP.KEY_BELEGIID,
				bestellungDto.getIId());
		Timestamp tend = new Timestamp(System.currentTimeMillis());
		System.out.println("Took: " + (tend.getTime() - tStart.getTime())
				+ "ms");
		return prints;
	}

	/**
	 * Eine Bestellung drucken.
	 *
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return JasperPrint der Druck
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */

	public Object[] getGeaenderteArtikelDaten(TheClientDto theClientDto) {

		ArrayList<Object[]> alDaten = new ArrayList<Object[]>();

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;

		session = factory.openSession();

		String sQuery = "SELECT distinct(b.flrartikel) FROM FLRBestellvorschlag b WHERE b.mandant_c_nr='"
				+ theClientDto.getMandant() + "'";

		Query querylagerbewegungen = session.createQuery(sQuery);

		List<?> list = querylagerbewegungen.list();
		Iterator<?> it = list.iterator();

		HashMap hmAeltereVersionen = new HashMap();

		while (it.hasNext()) {
			FLRArtikel flrartikelAktuell = (FLRArtikel) it.next();

			// Gibt es aeltere Versionen des Artikels

			if (flrartikelAktuell.getC_nr().length() > 4
					&& flrartikelAktuell.getC_nr().charAt(
							flrartikelAktuell.getC_nr().length() - 4) == '_') {
				// Es ist eine neuere Version

				// Nun alle aelteren Versionen suchen:

				Session session3 = FLRSessionFactory.getFactory().openSession();

				String queryString = "SELECT a FROM FLRArtikel a WHERE a.mandant_c_nr='"
						+ theClientDto.getMandant()
						+ "' AND c_nr LIKE '"
						+ flrartikelAktuell.getC_nr().substring(0,
								flrartikelAktuell.getC_nr().length() - 4)
						+ "%' AND  c_nr < '"
						+ flrartikelAktuell.getC_nr()
						+ "'";

				org.hibernate.Query query3 = session3.createQuery(queryString);
				List<?> results3 = query3.list();
				Iterator<?> resultListIterator3 = results3.iterator();

				while (resultListIterator3.hasNext()) {

					FLRArtikel flrArtikel = (FLRArtikel) resultListIterator3
							.next();

					Session session2 = null;

					session2 = factory.openSession();

					String sQuery2 = "SELECT b FROM FLRBestellposition b WHERE b.flrbestellung.mandant_c_nr='"
							+ theClientDto.getMandant()
							+ "' AND b.flrartikel.i_id="
							+ flrArtikel.getI_id()
							+ " AND b.flrbestellung.bestellungstatus_c_nr IN ('"
							+ BestellungFac.BESTELLSTATUS_ANGELEGT
							+ "','"
							+ BestellungFac.BESTELLSTATUS_BESTAETIGT
							+ "','"
							+ BestellungFac.BESTELLSTATUS_OFFEN
							+ "','"
							+ BestellungFac.BESTELLSTATUS_TEILERLEDIGT + "')";
					Query q2 = session2.createQuery(sQuery2);
					List<?> list2 = q2.list();
					Iterator<?> it2 = list2.iterator();

					while (it2.hasNext()) {
						FLRBestellposition flrBestellposition = (FLRBestellposition) it2
								.next();

						hmAeltereVersionen.put(flrartikelAktuell.getI_id(),
								null);

						Object[] oZeile = new Object[BestellungReportFac.REPORT_GEAENDERTEARTIKEL_ANZAHL_SPALTEN];
						oZeile[BestellungReportFac.REPORT_GEAENDERTEARTIKEL_ARTIKELNUMMER_AKTUELL] = flrartikelAktuell
								.getC_nr();

						ArtikelDto aDto = getArtikelFac()
								.artikelFindByPrimaryKey(
										flrartikelAktuell.getI_id(),
										theClientDto);
						if (aDto.getArtikelsprDto() != null) {
							oZeile[BestellungReportFac.REPORT_GEAENDERTEARTIKEL_ARTIKELBEZEICHNUNG_AKTUELL] = aDto
									.getArtikelsprDto().getCBez();
							oZeile[BestellungReportFac.REPORT_GEAENDERTEARTIKEL_ARTIKELZUSATZBEZEICHNUNG_AKTUELL] = aDto
									.getArtikelsprDto().getCZbez();
							oZeile[BestellungReportFac.REPORT_GEAENDERTEARTIKEL_ARTIKELZUSATZBEZEICHNUNG2_AKTUELL] = aDto
									.getArtikelsprDto().getCZbez2();
						}

						oZeile[BestellungReportFac.REPORT_GEAENDERTEARTIKEL_ARTIKELNUMMER_BESTELLUNG] = flrBestellposition
								.getFlrartikel().getC_nr();

						aDto = getArtikelFac().artikelFindByPrimaryKey(
								flrBestellposition.getFlrartikel().getI_id(),
								theClientDto);
						if (aDto.getArtikelsprDto() != null) {
							oZeile[BestellungReportFac.REPORT_GEAENDERTEARTIKEL_ARTIKELBEZEICHNUNG_BESTELLUNG] = aDto
									.getArtikelsprDto().getCBez();
							oZeile[BestellungReportFac.REPORT_GEAENDERTEARTIKEL_ARTIKELZUSATZBEZEICHNUNG_BESTELLUNG] = aDto
									.getArtikelsprDto().getCZbez();
							oZeile[BestellungReportFac.REPORT_GEAENDERTEARTIKEL_ARTIKELZUSATZBEZEICHNUNG2_BESTELLUNG] = aDto
									.getArtikelsprDto().getCZbez2();
						}

						oZeile[BestellungReportFac.REPORT_GEAENDERTEARTIKEL_BESTELLNUMMER] = flrBestellposition
								.getFlrbestellung().getC_nr();
						oZeile[BestellungReportFac.REPORT_GEAENDERTEARTIKEL_MENGE] = flrBestellposition
								.getN_menge();
						oZeile[BestellungReportFac.REPORT_GEAENDERTEARTIKEL_PREIS] = flrBestellposition
								.getN_nettogesamtpreis();

						oZeile[BestellungReportFac.REPORT_GEAENDERTEARTIKEL_ABNUMMER] = flrBestellposition
								.getC_abnummer();
						oZeile[BestellungReportFac.REPORT_GEAENDERTEARTIKEL_ABTERMIN] = flrBestellposition
								.getT_auftragsbestaetigungstermin();
						oZeile[BestellungReportFac.REPORT_GEAENDERTEARTIKEL_POSITIONSTERMIN] = flrBestellposition
								.getT_uebersteuerterliefertermin();
						oZeile[BestellungReportFac.REPORT_GEAENDERTEARTIKEL_BESTELLDATUM] = flrBestellposition
								.getFlrbestellung().getT_belegdatum();

						oZeile[BestellungReportFac.REPORT_GEAENDERTEARTIKEL_LIEFERANT] = HelperServer
								.formatNameAusFLRPartner(flrBestellposition
										.getFlrbestellung().getFlrlieferant()
										.getFlrpartner());

						alDaten.add(oZeile);

					}

					session2.close();

				}

			}

		}

		session.close();

		// Sortieren nach Artikel aktuell und dann nach Artikel Bestellung

		for (int k = alDaten.size() - 1; k > 0; --k) {
			for (int j = 0; j < k; ++j) {
				Object[] o = alDaten.get(j);
				Object[] o1 = alDaten.get(j + 1);

				String s = (String) o[REPORT_GEAENDERTEARTIKEL_ARTIKELNUMMER_AKTUELL];
				String s1 = (String) o1[REPORT_GEAENDERTEARTIKEL_ARTIKELNUMMER_AKTUELL];

				if (s.toUpperCase().compareTo(s1.toUpperCase()) > 0) {

					alDaten.set(j, o1);
					alDaten.set(j + 1, o);
				} else if (s.toUpperCase().compareTo(s1.toUpperCase()) == 0) {
					s = (String) o[REPORT_GEAENDERTEARTIKEL_ARTIKELNUMMER_BESTELLUNG];
					s1 = (String) o1[REPORT_GEAENDERTEARTIKEL_ARTIKELNUMMER_BESTELLUNG];

					if (s.toUpperCase().compareTo(s1.toUpperCase()) > 0) {

						alDaten.set(j, o1);
						alDaten.set(j + 1, o);
					}
				}
			}
		}

		Object[] oReturn = new Object[2];

		oReturn[0] = hmAeltereVersionen;
		oReturn[1] = alDaten;
		return oReturn;
	}

	public JasperPrintLP printGeaenderteArtikel(TheClientDto theClientDto) {
		useCase = UC_GEAENDERTE_ARTIKEL;

		HashMap<String, Object> mapParameter = new HashMap<String, Object>();

		Object[] o = getGeaenderteArtikelDaten(theClientDto);

		ArrayList<Object[]> alDaten = (ArrayList<Object[]>) o[1];

		data = new Object[alDaten.size()][BestellungReportFac.REPORT_GEAENDERTEARTIKEL_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(data);

		initJRDS(mapParameter, BestellungReportFac.REPORT_MODUL,
				BestellungReportFac.REPORT_GEAENDERTE_ARTIKEL,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();
	}

	public JasperPrintLP printAbholauftrag(Integer iIdBestellungI,
			TheClientDto theClientDto) {
		JasperPrintLP print = null;
		if (iIdBestellungI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdBestellungI == null"));
		}
		BestellungDto bestellungDto = null;
		BestellpositionDto[] aPositionDtos = null;
		// Erstellung des Report
		Boolean bbSeitenumbruch = new Boolean(false); // dieser Wert wechselt
		// mit jedem
		// Seitenumbruch
		// zwischen true und
		// false
		boolean bTermineUnterschiedlich = false;

		useCase = UC_ABHOLAUFTRAG;

		try {
			bestellungDto = getBestellungFac().bestellungFindByPrimaryKey(
					iIdBestellungI);

			aPositionDtos = getBestellpositionFac()
					.bestellpositionFindByBestellung(iIdBestellungI,
							theClientDto);

			// den Druckzeitpunkt vermerken
			bestellungDto.setTGedruckt(getTimestamp());

			LieferantDto lieferantDto = getLieferantFac()
					.lieferantFindByPrimaryKey(
							bestellungDto.getLieferantIIdBestelladresse(),
							theClientDto);

			AnsprechpartnerDto oAnsprechpartnerDto = null;

			Integer partnerIIdAnsprechpartner = null;
			if (bestellungDto.getAnsprechpartnerIId() != null) {
				oAnsprechpartnerDto = getAnsprechpartnerFac()
						.ansprechpartnerFindByPrimaryKey(
								bestellungDto.getAnsprechpartnerIId(),
								theClientDto);
				if (oAnsprechpartnerDto != null) {
					partnerIIdAnsprechpartner = oAnsprechpartnerDto
							.getPartnerIIdAnsprechpartner();
				}
			}

			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);

			// es gilt das Locale des Lieferanten
			Locale locDruck = Helper.string2Locale(lieferantDto.getPartnerDto()
					.getLocaleCNrKommunikation());

			int iAnzahlZeilen = aPositionDtos.length; // Anzahl der Zeilen in
			// der Gruppe

			data = new Object[iAnzahlZeilen][BestellungReportFac.REPORT_BESTELLUNG_ANZAHL_SPALTEN];

			// die Datenmatrix befuellen
			for (int i = 0; i < iAnzahlZeilen; i++) {
				data[i][REPORT_ABHOLAUFTRAG_POSITIONOBKJEKT] = getSystemReportFac()
						.getPositionForReport(LocaleFac.BELEGART_BESTELLUNG,
								aPositionDtos[i].getIId(), theClientDto);
				data[i][REPORT_ABHOLAUFTRAG_POSITION] = getBestellpositionFac()
						.getPositionNummer(aPositionDtos[i].getIId(),
								theClientDto);

				if (aPositionDtos[i].getTUebersteuerterLiefertermin() != null) {
					Timestamp tposLieferTermin = Helper
							.cutTimestamp(aPositionDtos[i]
									.getTUebersteuerterLiefertermin());
					if (!(bestellungDto.getDLiefertermin()
							.equals(tposLieferTermin))) {
						bTermineUnterschiedlich = true;
					}
				}

				// Artikelpositionen
				if (aPositionDtos[i].getPositionsartCNr().equals(
						BestellpositionFac.BESTELLPOSITIONART_IDENT)
						|| aPositionDtos[i]
								.getPositionsartCNr()
								.equals(BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE)) {

					if (!bestellungDto.getDLiefertermin().equals(
							aPositionDtos[i].getTUebersteuerterLiefertermin())) {
						data[i][REPORT_ABHOLAUFTRAG_POSITIONTERMIN] = Helper
								.formatDatum(
										Helper.extractDate(aPositionDtos[i]
												.getTUebersteuerterLiefertermin()),
										locDruck);

					}

					ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKey(
									aPositionDtos[i].getArtikelIId(),
									theClientDto);
					BelegPositionDruckIdentDto druckDto = printIdent(
							aPositionDtos[i], LocaleFac.BELEGART_BESTELLUNG,
							artikelDto, locDruck, lieferantDto.getPartnerIId(),
							theClientDto);
					data[i][REPORT_ABHOLAUFTRAG_ARTIKELCZBEZ2] = druckDto
							.getSArtikelZusatzBezeichnung2();
					// Artikel Bezeichnung
					data[i][REPORT_ABHOLAUFTRAG_BEZEICHNUNG] = druckDto
							.getSBezeichnung();
					// Artikel Kurzbezeichnung
					data[i][REPORT_ABHOLAUFTRAG_KURZBEZEICHNUNG] = druckDto
							.getSKurzbezeichnung();
					data[i][REPORT_ABHOLAUFTRAG_ZUSATZBEZEICHNUNG] = druckDto
							.getSZusatzBezeichnung();
					data[i][REPORT_ABHOLAUFTRAG_IDENT] = druckDto
							.getSArtikelInfo();
					data[i][REPORT_ABHOLAUFTRAG_ARTIKELKOMMENTAR] = druckDto
							.getSArtikelkommentar();

					data[i][REPORT_ABHOLAUFTRAG_REFERENZNUMMER] = artikelDto
							.getCReferenznr();

					// Lieferantendaten: Artikelnummer, Bezeichnung
					ArtikellieferantDto artikellieferantDto = getArtikelFac()
							.artikellieferantFindByArtikellIIdLieferantIIdOhneExc(
									aPositionDtos[i].getArtikelIId(),
									bestellungDto
											.getLieferantIIdBestelladresse(),
									theClientDto);
					if (artikellieferantDto != null) {
						if (artikellieferantDto.getCArtikelnrlieferant() != null) {
							data[i][REPORT_ABHOLAUFTRAG_LIEFERANT_ARTIKEL_IDENTNUMMER] = artikellieferantDto
									.getCArtikelnrlieferant();
						}
						if (artikellieferantDto.getCBezbeilieferant() != null) {
							data[i][REPORT_ABHOLAUFTRAG_LIEFERANT_ARTIKEL_BEZEICHNUNG] = artikellieferantDto
									.getCBezbeilieferant();
						}

					}

					if (artikelDto.getHerstellerIId() != null) {
						HerstellerDto herstellerDto = getArtikelFac()
								.herstellerFindByPrimaryKey(
										artikelDto.getHerstellerIId(),
										theClientDto);
						data[i][REPORT_ABHOLAUFTRAG_ARTIKEL_HERSTELLER] = herstellerDto
								.getCNr();
						PartnerDto partnerDto = getPartnerFac()
								.partnerFindByPrimaryKey(
										herstellerDto.getPartnerIId(),
										theClientDto);
						data[i][REPORT_ABHOLAUFTRAG_ARTIKEL_HERSTELLER_NAME] = partnerDto
								.formatFixName1Name2();
					}

					if (artikelDto.getEinheitCNrBestellung() != null) {
						boolean bInvers = Helper.short2boolean(artikelDto
								.getbBestellmengeneinheitInvers());

						BigDecimal gewicht = null;

						if (bInvers) {
							gewicht = aPositionDtos[i].getNMenge().divide(
									artikelDto.getNUmrechnungsfaktor(), 4,
									BigDecimal.ROUND_HALF_EVEN);
						} else {
							gewicht = aPositionDtos[i].getNMenge().multiply(
									artikelDto.getNUmrechnungsfaktor());
						}
						BigDecimal umrechnung = new BigDecimal(0);
						gewicht = Helper.rundeKaufmaennisch(gewicht, 4);
						data[i][REPORT_ABHOLAUFTRAG_GEWICHT] = gewicht;
						data[i][REPORT_ABHOLAUFTRAG_GEWICHTEINHEIT] = getSystemFac()
								.formatEinheit(
										artikelDto.getEinheitCNrBestellung(),
										locDruck, theClientDto);

						if (bInvers) {
							umrechnung = aPositionDtos[i]
									.getNNettoeinzelpreis().multiply(
											artikelDto.getNUmrechnungsfaktor());
						} else {
							umrechnung = aPositionDtos[i]
									.getNNettoeinzelpreis().divide(
											artikelDto.getNUmrechnungsfaktor(),
											4, BigDecimal.ROUND_HALF_EVEN);
						}

						umrechnung = aPositionDtos[i].getNNettoeinzelpreis()
								.divide(artikelDto.getNUmrechnungsfaktor(), 4,
										BigDecimal.ROUND_HALF_EVEN);

						data[i][REPORT_ABHOLAUFTRAG_PREISPEREINHEIT] = umrechnung;
					}

					// PJ 14867
					if (aPositionDtos[i].getIBestellpositionIIdRahmenposition() != null
							&& bestellungDto
									.getBestellungartCNr()
									.equals(BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR)) {
						Session sessionRahmen = FLRSessionFactory.getFactory()
								.openSession();

						String query = "FROM FLRBestellpositionReport bs WHERE bs.i_id="
								+ aPositionDtos[i]
										.getIBestellpositionIIdRahmenposition();

						Query q = sessionRahmen.createQuery(query);

						List l = q.list();

						if (l.size() > 0) {
							FLRBestellpositionReport eintrag = (FLRBestellpositionReport) l
									.iterator().next();

							BigDecimal offeneMenge = eintrag.getN_menge();

							for (Iterator<?> iter = eintrag
									.getAbrufpositionenset().iterator(); iter
									.hasNext();) {
								FLRBestellpositionReport item = (FLRBestellpositionReport) iter
										.next();
								if (item.getFlrbestellung().getT_belegdatum()
										.getTime() <= bestellungDto
										.getDBelegdatum().getTime()) {
									offeneMenge = offeneMenge.subtract(item
											.getN_menge());
								}
							}

							data[i][REPORT_ABHOLAUFTRAG_OFFENERAHMENMENGE] = offeneMenge;

						}

						sessionRahmen.close();

					}

					if (aPositionDtos[i].getPositionsartCNr().equals(
							BestellpositionFac.BESTELLPOSITIONART_IDENT)) {
						data[i][REPORT_ABHOLAUFTRAG_IDENTNUMMER] = artikelDto
								.getCNr();

						if (artikelDto.getVerpackungDto() != null) {
							data[i][REPORT_ABHOLAUFTRAG_BAUFORM] = artikelDto
									.getVerpackungDto().getCBauform();
							data[i][REPORT_ABHOLAUFTRAG_VERPACKUNGSART] = artikelDto
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
								data[i][REPORT_ABHOLAUFTRAG_ARTIKEL_MATERIAL] = materialDto
										.getMaterialsprDto().getCBez();
							} else {
								data[i][REPORT_ABHOLAUFTRAG_ARTIKEL_MATERIAL] = materialDto
										.getCNr();
							}
						}
						if (artikelDto.getGeometrieDto() != null) {
							data[i][REPORT_ABHOLAUFTRAG_ARTIKEL_BREITE] = artikelDto
									.getGeometrieDto().getFBreite();
							data[i][REPORT_ABHOLAUFTRAG_ARTIKEL_HOEHE] = artikelDto
									.getGeometrieDto().getFHoehe();
							data[i][REPORT_ABHOLAUFTRAG_ARTIKEL_TIEFE] = artikelDto
									.getGeometrieDto().getFTiefe();
						}

					}
					data[i][REPORT_ABHOLAUFTRAG_MENGE] = aPositionDtos[i]
							.getNMenge();
					data[i][REPORT_ABHOLAUFTRAG_EINHEIT] = getSystemFac()
							.formatEinheit(aPositionDtos[i].getEinheitCNr(),
									locDruck, theClientDto);

					data[i][REPORT_ABHOLAUFTRAG_EINZELPREIS] = aPositionDtos[i]
							.getNNettoeinzelpreis();
					data[i][REPORT_ABHOLAUFTRAG_RABATT] = aPositionDtos[i]
							.getDRabattsatz();
					data[i][REPORT_ABHOLAUFTRAG_ANGEBOTSNUMMER] = aPositionDtos[i]
							.getCAngebotnummer();

					BigDecimal bdGesamtpreis = null;

					if (aPositionDtos[i].getNNettogesamtpreis() != null
							&& aPositionDtos[i].getNMenge() != null) {
						bdGesamtpreis = aPositionDtos[i].getNNettogesamtpreis()
								.multiply(aPositionDtos[i].getNMenge());
					}

					data[i][REPORT_ABHOLAUFTRAG_GESAMTPREIS] = bdGesamtpreis;

					/**
					 * @todo MR-> Wird nicht mehr benoetigt, da
					 *       LPReport.printIdent das abbildet. Wegen kurzer Zeit
					 *       vor deploy nicht mehr geloescht.
					 */
					ArrayList<Object> images = new ArrayList<Object>();
					ArtikelkommentarDto[] artikelkommentarDto = getArtikelkommentarFac()
							.artikelkommentardruckFindByArtikelIIdBelegartCNr(
									aPositionDtos[i].getArtikelIId(),
									LocaleFac.BELEGART_BESTELLUNG,
									theClientDto.getLocUiAsString(),
									theClientDto);

					if (artikelkommentarDto != null
							&& artikelkommentarDto.length > 0) {
						String cPositionKommentarText = "";

						for (int j = 0; j < artikelkommentarDto.length; j++) {
							if (artikelkommentarDto[j]
									.getArtikelkommentarsprDto() != null) {
								String cDatenformat = artikelkommentarDto[j]
										.getDatenformatCNr();
								if (cDatenformat
										.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG)
										|| artikelkommentarDto[j]
												.getDatenformatCNr()
												.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG)
										|| artikelkommentarDto[j]
												.getDatenformatCNr()
												.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF)) {
									byte[] bild = artikelkommentarDto[j]
											.getArtikelkommentarsprDto()
											.getOMedia();
									if (bild != null) {
										java.awt.Image myImage = Helper
												.byteArrayToImage(bild);
										data[i][REPORT_ABHOLAUFTRAG_IMAGE] = myImage;
										images.add(myImage);
									}
								} else if (artikelkommentarDto[j]
										.getDatenformatCNr()
										.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF)) {

									byte[] bild = artikelkommentarDto[j]
											.getArtikelkommentarsprDto()
											.getOMedia();

									java.awt.Image[] tiffs = Helper
											.tiffToImageArray(bild);
									if (tiffs != null) {
										for (int k = 0; k < tiffs.length; k++) {
											images.add(tiffs[k]);
										}
									}

								}
							}

						}
					}
				}

				// Betrifft Positionen
				if (aPositionDtos[i].getPositionsartCNr().equals(
						BestellpositionFac.BESTELLPOSITIONART_BETRIFFT)) {
					data[i][REPORT_ABHOLAUFTRAG_FREIERTEXT] = aPositionDtos[i]
							.getCBez();
				}

				// Texteingabe Positionen
				if (aPositionDtos[i].getPositionsartCNr().equals(
						BestellpositionFac.BESTELLPOSITIONART_TEXTEINGABE)) {
					// IMS 1619 leerer Text soll als Leerezeile erscheinen
					String sText = aPositionDtos[i].getXTextinhalt();

					if (sText != null && sText.trim().equals("")) {
						data[i][REPORT_ABHOLAUFTRAG_LEERZEILE] = " ";
					} else {
						data[i][REPORT_ABHOLAUFTRAG_FREIERTEXT] = sText;
					}
				}

				// Textbaustein Positionen
				if (aPositionDtos[i].getPositionsartCNr().equals(
						BestellpositionFac.BESTELLPOSITIONART_TEXTBAUSTEIN)) {
					// Dto holen
					MediastandardDto oMediastandardDto = getMediaFac()
							.mediastandardFindByPrimaryKey(
									aPositionDtos[i].getMediastandardIId());
					// zum Drucken vorbereiten
					BelegPositionDruckTextbausteinDto druckDto = printTextbaustein(
							oMediastandardDto, theClientDto);
					data[i][REPORT_ABHOLAUFTRAG_FREIERTEXT] = druckDto
							.getSFreierText();
					data[i][REPORT_ABHOLAUFTRAG_IMAGE] = druckDto.getOImage();
				}

				// Leerzeile Positionen
				if (aPositionDtos[i].getPositionsartCNr().equals(
						BestellpositionFac.BESTELLPOSITIONART_LEERZEILE)) {
					data[i][REPORT_ABHOLAUFTRAG_LEERZEILE] = " ";
				}

				// Seitenumbruch Positionen
				if (aPositionDtos[i].getPositionsartCNr().equals(
						BestellpositionFac.BESTELLPOSITIONART_SEITENUMBRUCH)) {
					bbSeitenumbruch = new Boolean(
							!bbSeitenumbruch.booleanValue()); // toggle
				}

				data[i][REPORT_ABHOLAUFTRAG_SEITENUMBRUCH] = bbSeitenumbruch;
				data[i][REPORT_ABHOLAUFTRAG_POSITIONSART] = aPositionDtos[i]
						.getPositionsartCNr();

			}

			// Kopftext
			String sKopftext = bestellungDto.getCKopftextUebersteuert();

			if (sKopftext == null || sKopftext.length() == 0) {
				BestellungtextDto oBestellungtextDto = getBestellungServiceFac()
						.bestellungtextFindByMandantLocaleCNr(
								theClientDto.getMandant(),
								lieferantDto.getPartnerDto()
										.getLocaleCNrKommunikation(),
								BestellungServiceFac.BESTELLUNGTEXT_KOPFTEXT,
								theClientDto);
				sKopftext = oBestellungtextDto.getXTextinhalt();
			}

			// Fusstext
			String sFusstext = bestellungDto.getCFusstextUebersteuert();

			if (sFusstext == null || sFusstext.length() == 0) {
				BestellungtextDto oBestellungtextDto = getBestellungServiceFac()
						.bestellungtextFindByMandantLocaleCNr(
								theClientDto.getMandant(),
								lieferantDto.getPartnerDto()
										.getLocaleCNrKommunikation(),
								BestellungServiceFac.BESTELLUNGTEXT_FUSSTEXT,
								theClientDto);

				sFusstext = oBestellungtextDto.getXTextinhalt();
			}

			// Erstellung des Report
			HashMap<String, Object> mapParameter = new HashMap<String, Object>();
			// CK: PJ 13849
			mapParameter.put(
					"P_BEARBEITER",
					getPersonalFac()
							.getPersonRpt(
									bestellungDto.getPersonalIIdAnlegen(),
									theClientDto));
			// die Parameter uebergeben
			if (bestellungDto.getBTeillieferungMoeglich() != null) {
				mapParameter.put(
						"P_TEILLIEFERUNGMOEGLICH",
						new Boolean(Helper.short2boolean(bestellungDto
								.getBTeillieferungMoeglich())));
			}

			String sSpediteur = null;
			if (bestellungDto.getSpediteurIId() != null) {
				if (getMandantFac().spediteurFindByPrimaryKey(
						bestellungDto.getSpediteurIId()) != null) {
					sSpediteur = getMandantFac().spediteurFindByPrimaryKey(
							bestellungDto.getSpediteurIId())
							.getCNamedesspediteurs();
				}
			}
			mapParameter.put("P_SPEDITEUR", sSpediteur != null ? sSpediteur
					: "");

			mapParameter.put("P_LIEFERANTINTERNERKOMMENTAR",
					lieferantDto.getCHinweisintern());
			mapParameter.put("P_LIEFERANTEXTERNERKOMMENTAR",
					lieferantDto.getCHinweisextern());

			if (bestellungDto.getAuftragIId() != null) {
				AuftragDto auftragDto = getAuftragFac()
						.auftragFindByPrimaryKey(bestellungDto.getAuftragIId());
				mapParameter.put("P_AUFTRAGSNUMMER", auftragDto.getCNr());
			}
			if (bestellungDto.getStatusCNr().equals(
					BestellpositionFac.BESTELLPOSITIONSTATUS_STORNIERT)) {
				mapParameter.put("P_STORNIERT", true);
			} else {
				mapParameter.put("P_STORNIERT", false);
			}
			mapParameter.put("Mandantenadresse",
					Helper.formatMandantAdresse(mandantDto));

			mapParameter.put("P_LIEFERADRESSE_IST_NICHT_MANDANT", new Boolean(
					true));

			if (bestellungDto.getPartnerIIdLieferadresse() != null) {
				PartnerDto lieferadresseDto = getPartnerFac()
						.partnerFindByPrimaryKey(
								bestellungDto.getPartnerIIdLieferadresse(),
								theClientDto);
				/**
				 * @todo MR: Wenn ueber Client Ansprechpartner ausgewaehlt
				 *       werden kann, hier den richtigen Ansprechpartner
				 *       uebergeben.
				 */

				// SP581
				if (lieferantDto.getPartnerDto().getLandplzortDto() != null
						&& lieferadresseDto.getLandplzortDto() != null
						&& !lieferantDto
								.getPartnerDto()
								.getLandplzortDto()
								.getLandDto()
								.getIID()
								.equals(lieferadresseDto.getLandplzortDto()
										.getLandDto().getIID())) {

					if (lieferadresseDto.getLandplzortDto().getLandDto()
							.getLandIIdGemeinsamespostland() != null
							&& lieferadresseDto
									.getLandplzortDto()
									.getLandDto()
									.getLandIIdGemeinsamespostland()
									.equals(lieferantDto.getPartnerDto()
											.getLandplzortDto().getIlandID())) {
						mapParameter.put(
								"P_LIEFERADRESSE",
								formatAdresseFuerAusdruck(lieferadresseDto,
										null, mandantDto,
										Helper.string2Locale(lieferadresseDto
												.getLocaleCNrKommunikation())));
					} else if (lieferantDto.getPartnerDto().getLandplzortDto()
							.getLandDto().getLandIIdGemeinsamespostland() != null
							&& lieferantDto
									.getPartnerDto()
									.getLandplzortDto()
									.getLandDto()
									.getLandIIdGemeinsamespostland()
									.equals(lieferadresseDto.getLandplzortDto()
											.getIlandID())) {
						mapParameter.put(
								"P_LIEFERADRESSE",
								formatAdresseFuerAusdruck(lieferadresseDto,
										null, mandantDto,
										Helper.string2Locale(lieferadresseDto
												.getLocaleCNrKommunikation()),
										LocaleFac.BELEGART_BESTELLUNG));
					} else {

						mapParameter.put(
								"P_LIEFERADRESSE",
								formatAdresseFuerAusdruck(lieferadresseDto,
										null, mandantDto,
										Helper.string2Locale(lieferadresseDto
												.getLocaleCNrKommunikation()),
										true, LocaleFac.BELEGART_BESTELLUNG));
					}
				} else {
					mapParameter.put(
							"P_LIEFERADRESSE",
							formatAdresseFuerAusdruck(lieferadresseDto, null,
									mandantDto,
									Helper.string2Locale(lieferadresseDto
											.getLocaleCNrKommunikation()),
									LocaleFac.BELEGART_BESTELLUNG));
				}

				mapParameter.put("P_LIEFERADRESSENICHTNULL", new Boolean(true));

				if (mandantDto.getPartnerIId().equals(
						bestellungDto.getPartnerIIdLieferadresse())) {
					mapParameter.put("P_LIEFERADRESSE_IST_NICHT_MANDANT",
							new Boolean(false));
				}

			}

			if (bestellungDto.getAnfrageIId() != null) {
				AnfrageDto anfrageDto = getAnfrageFac()
						.anfrageFindByPrimaryKey(bestellungDto.getAnfrageIId(),
								theClientDto);
				mapParameter.put("P_ANFRAGENUMMER", anfrageDto.getCNr());
			}

			if (bestellungDto.getPartnerIIdAbholadresse() != null) {
				PartnerDto abholadresseDto = getPartnerFac()
						.partnerFindByPrimaryKey(
								bestellungDto.getPartnerIIdAbholadresse(),
								theClientDto);

				AnsprechpartnerDto oAnsprechpartnerDtoAbholadresse = null;

				if (bestellungDto.getAnsprechpartnerIIdAbholadresse() != null) {
					oAnsprechpartnerDtoAbholadresse = getAnsprechpartnerFac()
							.ansprechpartnerFindByPrimaryKey(
									bestellungDto
											.getAnsprechpartnerIIdAbholadresse(),
									theClientDto);
				}

				mapParameter.put(
						"P_ABHOLADRESSE",
						formatAdresseFuerAusdruck(abholadresseDto,
								oAnsprechpartnerDtoAbholadresse, mandantDto,
								Helper.string2Locale(abholadresseDto
										.getLocaleCNrKommunikation()),
								LocaleFac.BELEGART_BESTELLUNG));

				if (oAnsprechpartnerDtoAbholadresse != null) {
					mapParameter
							.put("P_ANSPRECHPARTNER_ABHOLADRESSE",
									getPartnerFac()
											.formatFixAnredeTitelName2Name1FuerAdresskopf(
													oAnsprechpartnerDtoAbholadresse
															.getPartnerDto(),
													Helper.string2Locale(abholadresseDto
															.getLocaleCNrKommunikation()),
													null));
				}

			}

			if (lieferantDto.getPartnerRechnungsadresseDto() != null) {
				mapParameter.put(
						"P_RECHNUNGADRESSE",
						formatAdresseFuerAusdruck(lieferantDto
								.getPartnerRechnungsadresseDto(),
								oAnsprechpartnerDto, mandantDto,
								Helper.string2Locale(lieferantDto
										.getPartnerRechnungsadresseDto()
										.getLocaleCNrKommunikation()),
								LocaleFac.BELEGART_BESTELLUNG));
			}

			mapParameter.put(
					"Adressefuerausdruck",
					formatAdresseFuerAusdruck(lieferantDto.getPartnerDto(),
							oAnsprechpartnerDto, mandantDto, Helper
									.string2Locale(lieferantDto.getPartnerDto()
											.getLocaleCNrKommunikation()),
							LocaleFac.BELEGART_BESTELLUNG));
			if (oAnsprechpartnerDto != null) {
				mapParameter.put(
						"P_ANSPRECHPARTNER_ADRESSBLOCK",
						getPartnerFac()
								.formatFixAnredeTitelName2Name1FuerAdresskopf(
										oAnsprechpartnerDto.getPartnerDto(),
										Helper.string2Locale(lieferantDto
												.getPartnerDto()
												.getLocaleCNrKommunikation()),
										null));
			}
			/**
			 * @todo fuer diesen Block eine zentrale Methode bauen.
			 */

			// die partnerIId des ansprechpartners brauch ich
			// daten holen
			String sEmail = getPartnerFac()
					.partnerkommFindRespectPartnerAsStringOhneExec(
							partnerIIdAnsprechpartner,
							lieferantDto.getPartnerDto(),
							PartnerFac.KOMMUNIKATIONSART_EMAIL,
							theClientDto.getMandant(), theClientDto);

			String sFax = getPartnerFac()
					.partnerkommFindRespectPartnerAsStringOhneExec(
							partnerIIdAnsprechpartner,
							lieferantDto.getPartnerDto(),
							PartnerFac.KOMMUNIKATIONSART_FAX,
							theClientDto.getMandant(), theClientDto);

			String sTelefon = getPartnerFac()
					.partnerkommFindRespectPartnerAsStringOhneExec(
							partnerIIdAnsprechpartner,
							lieferantDto.getPartnerDto(),
							PartnerFac.KOMMUNIKATIONSART_TELEFON,
							theClientDto.getMandant(), theClientDto);

			// daten als parameter an den Report weitergeben
			mapParameter.put(LPReport.P_ANSPRECHPARTNEREMAIL,
					sEmail != null ? sEmail : "");
			mapParameter.put(LPReport.P_ANSPRECHPARTNERFAX, sFax != null ? sFax
					: "");
			mapParameter.put(LPReport.P_ANSPRECHPARTNERTELEFON,
					sTelefon != null ? sTelefon : "");

			mapParameter.put("P_LIEFERANT_UID", lieferantDto.getPartnerDto()
					.getCUid());
			mapParameter.put("P_LIEFERANT_EORI", lieferantDto.getPartnerDto()
					.getCEori());
			if (lieferantDto.getKontoIIdKreditorenkonto() != null) {
				mapParameter.put(
						"P_KREDITORENKONTO",
						getFinanzFac().kontoFindByPrimaryKey(
								lieferantDto.getKontoIIdKreditorenkonto())
								.getCNr());
			}
			mapParameter.put("P_KUNDENNUMMER", lieferantDto.getCKundennr());

			String cLabelRahmenOderLiefertermin = getTextRespectUISpr(
					"lp.liefertermin", theClientDto.getMandant(), locDruck);
			String cLabelBestellungnr = null;

			if (bestellungDto.getBestellungartCNr().equals(
					BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR)) {
				cLabelRahmenOderLiefertermin = getTextRespectUISpr(
						"lp.rahmentermin", theClientDto.getMandant(), locDruck);
				cLabelBestellungnr = getTextRespectUISpr(
						"bes.bestellungnr.rahmen", theClientDto.getMandant(),
						locDruck);
			} else if (bestellungDto.getBestellungartCNr().equals(
					BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR)) {

				BestellungDto rahmenbestellungDto = getBestellungFac()
						.bestellungFindByPrimaryKey(
								bestellungDto
										.getIBestellungIIdRahmenbestellung());
				cLabelBestellungnr = getTextRespectUISpr(
						"bes.bestellungnr.abrufrahmen",
						theClientDto.getMandant(), locDruck)
						+ " " + rahmenbestellungDto.getCNr();
			} else if (bestellungDto.getBestellungartCNr().equals(
					BestellungFac.BESTELLUNGART_LEIHBESTELLUNG_C_NR)) {
				cLabelBestellungnr = getTextRespectUISpr(
						"bes.bestellungnr.leihbestellung",
						theClientDto.getMandant(), locDruck);
			} else {
				cLabelBestellungnr = getTextRespectUISpr(
						"bes.bestellungnr.bestellung",
						theClientDto.getMandant(), locDruck);
			}

			mapParameter.put("P_LABELBESTELLUNGNR", cLabelBestellungnr);
			mapParameter.put("P_BELEGKENNUNG",
					baueKennungBestellung(bestellungDto, theClientDto));

			mapParameter.put("P_LABELRAHMENODERLIEFERTERMIN",
					cLabelRahmenOderLiefertermin);

			mapParameter.put("P_LIEFERTERMIN", Helper.formatDatum(
					bestellungDto.getDLiefertermin(), locDruck));
			mapParameter.put(
					"P_LIEFERART",
					getLocaleFac().lieferartFindByIIdLocaleOhneExc(
							bestellungDto.getLieferartIId(), locDruck,
							theClientDto));
			mapParameter.put(
					"P_ZAHLUNGSKONDITION",
					getMandantFac().zahlungszielFindByIIdLocaleOhneExc(
							bestellungDto.getZahlungszielIId(), locDruck,
							theClientDto));
			mapParameter.put("P_PROJEKTBEZ", bestellungDto.getCBez());
			if (bestellungDto.getKostenstelleIId() != null) {
				KostenstelleDto kostenstelleDto = getSystemFac()
						.kostenstelleFindByPrimaryKey(
								bestellungDto.getKostenstelleIId());
				mapParameter.put(LPReport.P_KOSTENSTELLE,
						kostenstelleDto.getCNr());
			}

			String cBriefanrede = "";

			if (oAnsprechpartnerDto != null) {
				cBriefanrede = getPartnerServicesFac().getBriefanredeFuerBeleg(
						bestellungDto.getAnsprechpartnerIId(),
						lieferantDto.getPartnerIId(), locDruck, theClientDto);
			} else {
				// neutrale Anrede
				cBriefanrede = getBriefanredeNeutralOderPrivatperson(
						lieferantDto.getPartnerIId(), locDruck, theClientDto);
			}

			mapParameter.put("Briefanrede", cBriefanrede);

			PersonalDto oPersonalBenutzer = getPersonalFac()
					.personalFindByPrimaryKey(theClientDto.getIDPersonal(),
							theClientDto);
			PersonalDto oPersonalAnleger = getPersonalFac()
					.personalFindByPrimaryKey(
							bestellungDto.getPersonalIIdAnlegen(), theClientDto);
			mapParameter.put("P_UNSER_ZEICHEN", Helper.getKurzzeichenkombi(
					oPersonalBenutzer.getCKurzzeichen(),
					oPersonalAnleger.getCKurzzeichen()));

			mapParameter.put("P_BELEGDATUM", Helper.formatDatum(
					bestellungDto.getDBelegdatum(), locDruck));
			mapParameter.put("P_WAEHRUNG", bestellungDto.getWaehrungCNr());
			mapParameter.put("P_ALLGEMEINER_RABATT",
					bestellungDto.getFAllgemeinerRabattsatz());
			mapParameter.put("P_ALLGEMEINER_RABATT_STRING", Helper.formatZahl(
					bestellungDto.getFAllgemeinerRabattsatz(), locDruck));

			mapParameter.put("P_TERMINEUNTERSCHIEDLICH", new Boolean(
					bTermineUnterschiedlich));

			mapParameter.put("Kopftext",
					Helper.formatStyledTextForJasper(sKopftext));
			mapParameter.put("P_BELEGWERT", bestellungDto.getNBestellwert());

			// die folgenden Felder werden im Report mit jeweils einer
			// trennenden Leerzeile
			// hintereinandergehaengt
			// MR 20071014 Folgende Felder muessen auch einzeln an Report
			// uebergeben werden.
			StringBuffer buff = new StringBuffer();

			// Externer Kommentar
			if (bestellungDto.getXExternerKommentar() != null
					&& bestellungDto.getXExternerKommentar().length() > 0) {
				mapParameter.put(P_EXTERNERKOMMENTAR, Helper
						.formatStyledTextForJasper(bestellungDto
								.getXExternerKommentar()));
				buff.append(bestellungDto.getXExternerKommentar()).append(
						"\n\n");
			}

			// Fusstext
			if (sFusstext != null) {
				mapParameter.put("P_FUSSTEXT",
						Helper.formatStyledTextForJasper(sFusstext));
				buff.append(sFusstext).append("\n\n");
			}

			StringBuffer buffVertreter = new StringBuffer();
			// Anrede des Mandanten
			String sMandantAnrede = mandantDto.getPartnerDto()
					.formatFixName1Name2();
			if (sMandantAnrede != null) {
				mapParameter.put(P_MANDANT_ANREDE_UND_NAME, sMandantAnrede);
				buff.append(sMandantAnrede).append("\n\n");
				buffVertreter.append(sMandantAnrede).append("\n\n");
			}

			// P_SUMMARY: Die Unterschrift fuer Belege inclusive
			// Unterschriftstext und -funktion
			// Beispiel:
			// "i.A. Ing. Werner Hehenwarter" - im Falle der Bestellung der
			// Vertreter aus den Kopfdaten
			// "Einkaufsleiter"
			PersonalDto vertreterDto = null;

			if (bestellungDto.getPersonalIIdAnforderer() != null) {
				vertreterDto = getPersonalFac().personalFindByPrimaryKey(
						bestellungDto.getPersonalIIdAnforderer(), theClientDto);
			}

			if (vertreterDto != null) {
				String sVertreterUFTitelName2Name1 = vertreterDto
						.formatFixUFTitelName2Name1();
				mapParameter.put(P_VERTRETER_UNTERSCHRIFTSFUNKTION_UND_NAME,
						sVertreterUFTitelName2Name1);
				buff.append(sVertreterUFTitelName2Name1);
				buffVertreter.append(sVertreterUFTitelName2Name1);

				if (vertreterDto.getCUnterschriftstext() != null
						&& vertreterDto.getCUnterschriftstext().length() > 0) {
					String sUnterschriftstext = vertreterDto
							.getCUnterschriftstext();
					mapParameter.put(P_VERTRETER_UNTERSCHRIFTSTEXT,
							sUnterschriftstext);
					buff.append("\n").append(sUnterschriftstext);
					buffVertreter.append("\n").append(sUnterschriftstext);
				}
			}

			mapParameter.put(P_VERTRETER,
					Helper.formatStyledTextForJasper(buffVertreter.toString()));
			mapParameter.put("P_SUMMARY",
					Helper.formatStyledTextForJasper(buff.toString()));

			mapParameter.put("P_STORNIERUNGSDATUM",
					bestellungDto.getTStorniert());
			mapParameter.put("P_AENDERUNGSBESTELLUNGSDATUM",
					bestellungDto.getTAenderungsbestellung());
			if (bestellungDto.getTAenderungsbestellung() != null) {
				mapParameter.put("P_AENDERUNGSBESTELLUNG", new Boolean(true));
			} else {
				mapParameter.put("P_AENDERUNGSBESTELLUNG", new Boolean(false));
			}

			if (bestellungDto.getAnsprechpartnerIIdLieferadresse() != null) {
				AnsprechpartnerDto ansprechpartnerLieferadresseDto = getAnsprechpartnerFac()
						.ansprechpartnerFindByPrimaryKey(
								bestellungDto
										.getAnsprechpartnerIIdLieferadresse(),
								theClientDto);
				mapParameter.put("P_ANSPRECHPARTNER_LIEFERADRESSE",
						ansprechpartnerLieferadresseDto.getPartnerDto()
								.formatFixTitelName1Name2());
			}

			initJRDS(mapParameter, BestellungReportFac.REPORT_MODUL,
					BestellungReportFac.REPORT_ABHOLAUFTRAG,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);

			return getReportPrint();

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return print;
	}

	/**
	 * Kennung fuer eine Bestellung zum Andrucken zusammenbauen.
	 * 
	 * @param bestellungDtoI
	 *            die Bestellung
	 * @param sUserI
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @return String die Kennung
	 */
	private String baueKennungBestellung(BestellungDto bestellungDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		StringBuffer kennung = null;
		try {
			BelegartDto belegartDto = getLocaleFac().belegartFindByPrimaryKey(
					bestellungDtoI.getBelegartCNr(), theClientDto);

			kennung = new StringBuffer(belegartDto.getCKurzbezeichnung());
			kennung.append(" ").append(bestellungDtoI.getCNr());
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return kennung.toString();
	}

	public JasperPrintLP printBestellungWareneingangsJournal(
			ReportJournalKriterienDto krit, Integer artikelklasseIId,
			Integer artikelgruppeIId, String artikelCNrVon,
			String artikelCNrBis, String projektCBezeichnung,
			Integer auftragIId, boolean bMitWarenverbrauch,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		Session session = null;
		useCase = UC_BESTELLUNG_WARENEINGANG;
		SessionFactory factory = FLRSessionFactory.getFactory();
		session = factory.openSession();

		boolean darfEinkaufspreisSehen = getTheJudgeFac().hatRecht(
				RechteFac.RECHT_LP_DARF_PREISE_SEHEN_EINKAUF, theClientDto);

		Criteria c = session.createCriteria(FLRWareneingangspositionen.class);
		Criteria cWe = c
				.createCriteria(WareneingangFac.FLR_WEPOS_FLRWARENEINGANG);
		Criteria cBesPos = c
				.createCriteria(WareneingangFac.FLR_WEPOS_FLRBESTELLPOSITION);
		Criteria cArt = cBesPos
				.createCriteria(BestellpositionFac.FLR_BESTELLPOSITION_FLRARTIKEL);
		Criteria cBestellung = cWe
				.createCriteria(WareneingangFac.FLR_WE_FLRBESTELLUNG);
		cBestellung.add(Restrictions.eq(
				BestellungFac.FLR_BESTELLUNG_MANDANT_C_NR,
				theClientDto.getMandant()));
		// Datum von
		if (krit.dVon != null) {
			cWe.add(Restrictions.ge(
					WareneingangFac.FLRSPALTE_T_WARENEINGANGSDATUM,
					Helper.cutDate(krit.dVon)));
		}
		// Datum bis
		if (krit.dBis != null) {

			java.sql.Date dBisTemp = Helper.cutDate(new java.sql.Date(krit.dBis
					.getTime() + 24 * 3600000));
			cWe.add(Restrictions.lt(
					WareneingangFac.FLRSPALTE_T_WARENEINGANGSDATUM, dBisTemp));
		}
		// Filter nach Projektbezeichnung
		if (projektCBezeichnung != null) {
			cBestellung.add(Restrictions.ilike(
					BestellungFac.FLR_BESTELLUNG_C_BEZPROJEKTBEZEICHNUNG, "%"
							+ projektCBezeichnung + "%"));
		}
		// Filter nach Auftrag
		if (auftragIId != null) {
			cBestellung.add(Restrictions.like(
					BestellungFac.FLR_BESTELLUNG_AUFTRAG_I_ID, auftragIId));
		}

		LpBelegnummerFormat f = getBelegnummerGeneratorObj()
				.getBelegnummernFormat(theClientDto.getMandant());
		Integer iGeschaeftsjahr = getParameterFac().getGeschaeftsjahr(
				theClientDto.getMandant());
		String sMandantKuerzel = getParameterFac().getMandantparameter(
				theClientDto.getMandant(), ParameterFac.KATEGORIE_ALLGEMEIN,
				ParameterFac.PARAMETER_BELEGNUMMER_MANDANTKENNUNG).getCWert();
		// reportjournal: 06 belegnummer von
		// reportjournalbelegnummer: 1 (von) hier funktionierts fast gleich
		// wie bei den Direktfiltern
		if (krit.sBelegnummerVon != null) {
			String sVon = HelperServer
					.getBelegnummernFilterForHibernateCriterias(f,
							iGeschaeftsjahr, sMandantKuerzel,
							krit.sBelegnummerVon);
			cBestellung.add(Restrictions.ge(BestellungFac.FLR_BESTELLUNG_C_NR,
					sVon));
		}
		// reportjournal: 07 belegnummer bis
		// reportjournalbelegnummer: 2 (bis) detto
		if (krit.sBelegnummerBis != null) {
			String sBis = HelperServer
					.getBelegnummernFilterForHibernateCriterias(f,
							iGeschaeftsjahr, sMandantKuerzel,
							krit.sBelegnummerBis);
			cBestellung.add(Restrictions.le(BestellungFac.FLR_BESTELLUNG_C_NR,
					sBis));
		}
		// Einschraenkung nach einer bestimmten Kostenstelle
		if (krit.kostenstelleIId != null) {
			cBestellung.add(Restrictions.eq(
					BestellungFac.FLR_BESTELLUNG_KOSTENSTELLE_I_ID,
					krit.kostenstelleIId));
		}

		// Einschraenkung nach einem bestimmten Lieferanten
		if (krit.lieferantIId != null) {
			cBestellung.add(Restrictions.eq(
					BestellungFac.FLR_BESTELLUNG_LIEFERANT_I_ID_BESTELLADRESSE,
					krit.lieferantIId));
		}

		if (artikelklasseIId != null) {
			cArt.createCriteria(ArtikelFac.FLR_ARTIKEL_FLRARTIKELKLASSE).add(
					Restrictions.eq("i_id", artikelklasseIId));
		}
		if (artikelgruppeIId != null) {
			cArt.createCriteria(ArtikelFac.FLR_ARTIKEL_FLRARTIKELGRUPPE).add(
					Restrictions.eq("i_id", artikelgruppeIId));
		}
		if (artikelCNrVon != null) {
			cArt.add(Restrictions
					.ge(ArtikelFac.FLR_ARTIKEL_C_NR, artikelCNrVon));
		}
		if (artikelCNrBis != null) {
			cArt.add(Restrictions
					.le(ArtikelFac.FLR_ARTIKEL_C_NR, artikelCNrBis));
		}

		// Sortierung nach Kostenstelle ist immer die erste Sortierung
		if (krit.bSortiereNachKostenstelle) {
			cBestellung.createCriteria(
					BestellungFac.FLR_BESTELLUNG_FLRKOSTENSTELLE).addOrder(
					Order.asc("c_nr"));
		}

		Map<String, Object> map = new TreeMap<String, Object>();

		if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_DATUM) {
			cWe.addOrder(Order
					.asc(WareneingangFac.FLRSPALTE_T_WARENEINGANGSDATUM));
			map.put(LPReport.P_SORTIERUNG,
					getTextRespectUISpr("bes.wedatum",
							theClientDto.getMandant(), theClientDto.getLocUi()));

		} else if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_IDENT) {
			cArt.addOrder(Order.asc(ArtikelFac.FLR_ARTIKEL_C_NR));
			map.put(LPReport.P_SORTIERUNG,
					getTextRespectUISpr("lp.artikel",
							theClientDto.getMandant(), theClientDto.getLocUi()));
		} else if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_BELEGNUMMER) {
			cBestellung.addOrder(Order.asc(BestellungFac.FLR_BESTELLUNG_C_NR));
			map.put(LPReport.P_SORTIERUNG,
					getTextRespectUISpr("bes.belegnummer",
							theClientDto.getMandant(), theClientDto.getLocUi()));
		} else if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PROJEKT) {
			cBestellung.addOrder(Order
					.asc(BestellungFac.FLR_BESTELLUNG_C_BEZPROJEKTBEZEICHNUNG));
			map.put(LPReport.P_SORTIERUNG,
					getTextRespectUISpr("lp.projekt",
							theClientDto.getMandant(), theClientDto.getLocUi()));
		} else if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
			cBestellung
					.createCriteria(BestellungFac.FLR_BESTELLUNG_FLRLIEFERANT)
					.createCriteria(LieferantFac.FLR_PARTNER)
					.addOrder(
							Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));
			map.put(LPReport.P_SORTIERUNG,
					getTextRespectUISpr("lp.partner",
							theClientDto.getMandant(), theClientDto.getLocUi()));
		} else {
			c.addOrder(Order.asc("i_id"));
			map.put(LPReport.P_SORTIERUNG, "i_id");
		}
		List<?> list = c.list();
		data = new Object[list.size()][REPORT_BSWARENEINGANGSJOURNAL_ANZAHL_SPALTEN];
		int i = 0;
		for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
			FLRWareneingangspositionen w = (FLRWareneingangspositionen) iter
					.next();
			if (w.getFlrwareneingang().getT_wareneingansdatum() != null) {
				data[i][REPORT_BSWARENEINGANGSJOURNAL_DATUM] = w
						.getFlrwareneingang().getT_wareneingansdatum();
			}
			if (w.getFlrwareneingang().getFlrbestellung().getC_nr() != null) {
				data[i][REPORT_BSWARENEINGANGSJOURNAL_BESTELLNUMMER] = w
						.getFlrwareneingang().getFlrbestellung().getC_nr();
			} else {
				data[i][REPORT_BSWARENEINGANGSJOURNAL_BESTELLNUMMER] = "";
			}
			if (w.getFlrbestellposition().getFlrartikel().getC_nr() != null) {
				data[i][REPORT_BSWARENEINGANGSJOURNAL_IDENT] = w
						.getFlrbestellposition().getFlrartikel().getC_nr();
			} else {
				data[i][REPORT_BSWARENEINGANGSJOURNAL_IDENT] = "";
			}
			if (w.getFlrbestellposition().getC_bezeichnung() != null) {
				data[i][REPORT_BSWARENEINGANGSJOURNAL_BEZEICHNUNG] = w
						.getFlrbestellposition().getC_bezeichnung();
			} else {
				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKeySmall(
								w.getFlrbestellposition().getFlrartikel()
										.getI_id(), theClientDto);
				data[i][REPORT_BSWARENEINGANGSJOURNAL_BEZEICHNUNG] = artikelDto
						.formatBezeichnung();
			}
			if (bMitWarenverbrauch) {
				data[i][REPORT_BSWARENEINGANGSJOURNAL_WA_REFERENZ] = getLagerFac()
						.getWarenausgangsreferenzSubreport(
								LocaleFac.BELEGART_BESTELLUNG, w.getI_id(),
								null, theClientDto);
			}
			// SP903
			if (w.getFlrbestellposition().getPosition_i_id_artikelset() != null) {
				data[i][REPORT_BSWARENEINGANGSJOURNAL_SETARTIKEL_TYP] = ArtikelFac.SETARTIKEL_TYP_POSITION;
			} else {

				Session sessionSet = FLRSessionFactory.getFactory()
						.openSession();

				sessionSet = factory.openSession();
				Criteria critSet = sessionSet
						.createCriteria(FLRBestellpositionReport.class);
				critSet.add(Restrictions.eq("position_i_id_artikelset", w
						.getFlrbestellposition().getI_id()));

				int iZeilen = critSet.list().size();

				if (iZeilen > 0) {
					data[i][REPORT_BSWARENEINGANGSJOURNAL_SETARTIKEL_TYP] = ArtikelFac.SETARTIKEL_TYP_KOPF;
				}
				sessionSet.close();

			}

			data[i][REPORT_BSWARENEINGANGSJOURNAL_PROJEKT] = w
					.getFlrbestellposition().getFlrbestellung()
					.getC_bezprojektbezeichnung();
			data[i][REPORT_BSWARENEINGANGSJOURNAL_LIEFERANT] = w
					.getFlrbestellposition().getFlrbestellung()
					.getFlrlieferant().getFlrpartner()
					.getC_name1nachnamefirmazeile1();

			if (w.getN_geliefertemenge() != null) {
				data[i][REPORT_BSWARENEINGANGSJOURNAL_MENGE] = w
						.getN_geliefertemenge();
			} else {
				data[i][REPORT_BSWARENEINGANGSJOURNAL_MENGE] = new BigDecimal(0);
			}
			if (w.getN_gelieferterpreis() != null) {
				if (darfEinkaufspreisSehen) {

					BigDecimal kurs = new BigDecimal(
							w.getFlrwareneingang()
									.getFlrbestellung()
									.getF_wechselkursmandantwaehrungbestellungswaehrung());
					if (kurs.doubleValue() != 0) {
						data[i][REPORT_BSWARENEINGANGSJOURNAL_GELIEFERTERPREIS] = w
								.getN_gelieferterpreis().divide(kurs, 4,
										BigDecimal.ROUND_HALF_EVEN);
					} else {
						data[i][REPORT_BSWARENEINGANGSJOURNAL_GELIEFERTERPREIS] = new BigDecimal(
								0);
					}

				} else {
					data[i][REPORT_BSWARENEINGANGSJOURNAL_GELIEFERTERPREIS] = null;
				}
			} else {
				if (darfEinkaufspreisSehen) {
					data[i][REPORT_BSWARENEINGANGSJOURNAL_GELIEFERTERPREIS] = new BigDecimal(
							0);
				} else {
					data[i][REPORT_BSWARENEINGANGSJOURNAL_GELIEFERTERPREIS] = null;
				}
			}

			if (w.getN_einstandspreis() != null) {
				if (darfEinkaufspreisSehen) {

					BigDecimal kurs = new BigDecimal(
							w.getFlrwareneingang()
									.getFlrbestellung()
									.getF_wechselkursmandantwaehrungbestellungswaehrung());
					if (kurs.doubleValue() != 0) {
						data[i][REPORT_BSWARENEINGANGSJOURNAL_EINSTANDSPREIS] = w
								.getN_einstandspreis().divide(kurs, 4,
										BigDecimal.ROUND_HALF_EVEN);
					} else {
						data[i][REPORT_BSWARENEINGANGSJOURNAL_EINSTANDSPREIS] = new BigDecimal(
								0);
					}

				} else {
					data[i][REPORT_BSWARENEINGANGSJOURNAL_EINSTANDSPREIS] = null;
				}
			} else {
				if (darfEinkaufspreisSehen) {
					data[i][REPORT_BSWARENEINGANGSJOURNAL_EINSTANDSPREIS] = new BigDecimal(
							0);
				} else {
					data[i][REPORT_BSWARENEINGANGSJOURNAL_EINSTANDSPREIS] = null;
				}
			}

			data[i][REPORT_BSWARENEINGANGSJOURNAL_LIEFERSCHEIN] = w
					.getFlrwareneingang().getC_lieferscheinnr();
			data[i][REPORT_BSWARENEINGANGSJOURNAL_LIEFERSCHEINDATUM] = w
					.getFlrwareneingang().getT_lieferscheindatum();

			if (w.getFlrwareneingang().getFlreingangsrechnung() != null) {
				data[i][REPORT_BSWARENEINGANGSJOURNAL_EINGANGSRECHNUNG] = w
						.getFlrwareneingang().getFlreingangsrechnung()
						.getC_nr();
			}

			if (w.getFlrwareneingang().getFlrbestellung().getFlrkostenstelle() != null) {

				data[i][REPORT_BSWARENEINGANGSJOURNAL_KOSTENSTELLE] = w
						.getFlrwareneingang().getFlrbestellung()
						.getFlrkostenstelle().getC_nr();
			}
			data[i][REPORT_BSWARENEINGANGSJOURNAL_ZUBUCHUNGSLAGER] = w
					.getFlrwareneingang().getFlrlager().getC_nr();
			data[i][REPORT_BSWARENEINGANGSJOURNAL_RABATTSATZ] = w
					.getFlrwareneingang().getF_rabattsatz();
			if (w.getFlrwareneingang().getFlrbestellung().getAuftrag_i_id() != null) {
				data[i][REPORT_BSWARENEINGANGSJOURNAL_AUFTRAG] = w
						.getFlrwareneingang().getFlrbestellung()
						.getFlrauftrag().getC_nr();
			}
			if (w.getFlrbestellposition().getFlrartikel().getFlrartikelklasse() != null) {
				w.getFlrbestellposition().getFlrartikel().getFlrartikelklasse()
						.getC_nr();
				data[i][REPORT_BSWARENEINGANGSJOURNAL_ARTIKELKLASSE] = w
						.getFlrbestellposition().getFlrartikel()
						.getFlrartikelklasse().getC_nr();
			}
			if (w.getFlrbestellposition().getFlrartikel().getFlrartikelgruppe() != null) {
				w.getFlrbestellposition().getFlrartikel().getFlrartikelgruppe()
						.getC_nr();
				data[i][REPORT_BSWARENEINGANGSJOURNAL_ARTIKELGRUPPE] = w
						.getFlrbestellposition().getFlrartikel()
						.getFlrartikelgruppe().getC_nr();
			}

			data[i][REPORT_BSWARENEINGANGSJOURNAL_TRANSPORTKOSTEN] = w
					.getFlrwareneingang().getN_transportkosten();
			data[i][REPORT_BSWARENEINGANGSJOURNAL_BANKSPESEN] = w
					.getFlrwareneingang().getN_bankspesen();
			data[i][REPORT_BSWARENEINGANGSJOURNAL_ZOLLKOSTEN] = w
					.getFlrwareneingang().getN_zollkosten();
			data[i][REPORT_BSWARENEINGANGSJOURNAL_SONSTIGEKOSTEN] = w
					.getFlrwareneingang().getN_sonstigespesen();
			data[i][REPORT_BSWARENEINGANGSJOURNAL_GK_FAKTOR] = w
					.getFlrwareneingang().getF_gemeinkostenfaktor();

			i++;
		}

		if (krit.dBis != null) {
			map.put("P_BIS", new Timestamp(krit.dBis.getTime()));
		}
		if (krit.dVon != null) {
			map.put("P_VON", new Timestamp(krit.dVon.getTime()));
		}

		map.put("P_MIT_WARENVERBRAUCH", new Boolean(bMitWarenverbrauch));

		map.put(LPReport.P_REPORT_INFORMATION, "");
		initJRDS(map, BestellungReportFac.REPORT_MODUL,
				BestellungReportFac.REPORT_BESTELLUNG_JOURNAL_WARENEINGANG,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		session.close();
		return getReportPrint();
	}

	public void sendMahnlauf(String cKommuniaktionsart,
			BSMahnlaufDto bsMahnlaufDto, Locale absenderLocale,
			TheClientDto theClientDto) throws EJBExceptionLP, Throwable {
		// BSMahnlaufDto bsMahnlaufDto = getBSMahnlaufDto();
		if (bsMahnlaufDto != null) {
			Integer mahnlaufIId = bsMahnlaufDto.getIId();
			String sAbsenderadresse = null;
			if (mahnlaufIId != null) {
				// Pruefung ob fuer Personal Mail definiert ist
				if (PartnerFac.KOMMUNIKATIONSART_EMAIL
						.equals(cKommuniaktionsart)) {
					PersonalDto personalDto = getPersonalFac()
							.personalFindByPrimaryKey(
									theClientDto.getIDPersonal(), theClientDto);
					if (personalDto.getCEmail() != null) {
						sAbsenderadresse = personalDto.getCEmail();
						if (sAbsenderadresse == null) {

							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_MAHNUNGSVERSAND_KEINE_ABSENDERADRESSE,
									new Exception(
											getTextRespectUISpr(
													"bestellung.fehler.keinemailadressedefiniert",
													theClientDto.getMandant(),
													absenderLocale)));
						}
						if (!Helper.validateEmailadresse(sAbsenderadresse)) {
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_UNGUELTIGE_EMAILADRESSE,
									new Exception(
											getTextRespectUISpr(
													"bestellung.fehler.ungueltigemailadressedefiniert",
													theClientDto.getMandant(),
													absenderLocale)));
						}
					} else {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_MAHNUNGSVERSAND_KEINE_ABSENDERADRESSE,
								new Exception(
										getTextRespectUISpr(
												"bestellung.fehler.keinemailadressedefiniert",
												theClientDto.getMandant(),
												absenderLocale)));
					}

				}
				// Holen der Sammelmahnungen
				ArrayList<JasperPrintLP> hm = getBestellungReportFac()
						.getMahnungenFuerAlleLieferanten(mahnlaufIId,
								theClientDto, true, true);
				if (!hm.isEmpty()) {
					for (int j = 0; j < hm.size(); j++) {

						JasperPrintLP print = hm.get(j);

						Integer iLieferant = (Integer) print
								.getAdditionalInformation("lieferantIId");
						Integer ansprechpartnerIId = (Integer) print
								.getAdditionalInformation("ansprechpartnerIId");

						HashMap bestellnummern = (HashMap) print
								.getAdditionalInformation("bestellnummern");

						// Lieferant holen
						Integer iPartnerIId = getLieferantFac()
								.lieferantFindByPrimaryKey(iLieferant,
										theClientDto).getPartnerIId();
						// Kommunikation holen
						PartnerDto partnerDto = getPartnerFac()
								.partnerFindByPrimaryKey(iPartnerIId,
										theClientDto);
						String partkommDto = null;
						String sEmpfaenger = null;
						if (PartnerFac.KOMMUNIKATIONSART_EMAIL
								.equals(cKommuniaktionsart)) {
							partkommDto = partnerDto.getCEmail();

							if (ansprechpartnerIId != null) {
								AnsprechpartnerDto ansprechpartnerDto = getAnsprechpartnerFac()
										.ansprechpartnerFindByPrimaryKey(
												ansprechpartnerIId,
												theClientDto);

								String komm = getPartnerFac()
										.partnerkommFindOhneExec(

												partnerDto.getIId(),
												ansprechpartnerDto
														.getPartnerIIdAnsprechpartner(),
												PartnerFac.KOMMUNIKATIONSART_EMAIL,
												theClientDto.getMandant(),
												theClientDto);
								if (komm != null) {

									if (!Helper.validateEmailadresse(komm)) {
										sEmpfaenger = null;
									} else {
										sEmpfaenger = komm;
									}
								}

							}

							if (sEmpfaenger == null && partkommDto != null) {
								sEmpfaenger = partkommDto;
								if (!Helper.validateEmailadresse(sEmpfaenger)) {
									sEmpfaenger = null;
								}
							}

						}
						if (PartnerFac.KOMMUNIKATIONSART_FAX
								.equals(cKommuniaktionsart)) {
							partkommDto = partnerDto.getCFax();
							if (partkommDto != null) {

								if (ansprechpartnerIId != null) {
									AnsprechpartnerDto ansprechpartnerDto = getAnsprechpartnerFac()
											.ansprechpartnerFindByPrimaryKey(
													ansprechpartnerIId,
													theClientDto);

									if (ansprechpartnerDto.getCFax() != null) {

										partkommDto = ansprechpartnerDto
												.getCFax();

										if (!Helper
												.validateFaxnummer(ansprechpartnerDto
														.getCFax())) {
											sEmpfaenger = null;
										} else {
											sEmpfaenger = ansprechpartnerDto
													.getCFax();
										}
									}
								}

								sEmpfaenger = getPartnerFac()
										.passeInlandsAuslandsVorwahlAn(
												partnerDto.getIId(),
												theClientDto.getMandant(),
												partkommDto, theClientDto);

								// = partkommDto.getCInhalt();
								if (sEmpfaenger == null
										&& !Helper
												.validateFaxnummer(sEmpfaenger)) {
									sEmpfaenger = null;
								}
							}
						}
						if (sEmpfaenger != null) {
							VersandauftragDto versDto = new VersandauftragDto();
							versDto.setCEmpfaenger(sEmpfaenger);
							versDto.setCAbsenderadresse(sAbsenderadresse);
							String sLocMahnung = getTextRespectUISpr(
									"lp.mahnung", theClientDto.getMandant(),
									absenderLocale);
							versDto.setCDateiname(sLocMahnung + ".pdf");
							long fiveMinutesInMillis = 1000 * 60 * 5;
							versDto.setTSendezeitpunktwunsch(new Timestamp(
									System.currentTimeMillis()
											+ fiveMinutesInMillis));
							partnerDto.getLocaleCNrKommunikation();

							String betreff = sLocMahnung;

							if (bestellnummern.size() > 0) {
								Iterator it = bestellnummern.keySet()
										.iterator();
								while (it.hasNext()) {
									betreff += " " + it.next();
									if (it.hasNext()) {
										betreff += ",";
									}
								}
							}

							if (betreff.length() > 95) {
								betreff = betreff.substring(0, 94) + "...";
							}

							versDto.setCBetreff(betreff);
							versDto.setOInhalt(JasperExportManager
									.exportReportToPdf(print.getPrint()));
							getVersandFac().createVersandauftrag(versDto,
									false, theClientDto);
							// Mahnungen auf gemahnt setzen
							BSMahnungDto[] bsMahnungDto = getBSMahnwesenFac()
									.bsmahnungFindByBSMahnlaufIIdLieferantIId(
											mahnlaufIId, iLieferant,
											theClientDto.getMandant());
							for (int i = 0; i < bsMahnungDto.length; i++) {
								getBSMahnwesenFac().mahneBSMahnung(
										bsMahnungDto[i].getIId(), theClientDto);
							}
						}
					}
				}
			}

		}
	}

}
