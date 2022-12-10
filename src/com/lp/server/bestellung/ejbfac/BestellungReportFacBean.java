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
package com.lp.server.bestellung.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
import javax.xml.bind.JAXBException;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.xml.sax.SAXException;

import com.lp.server.anfrage.service.AnfrageDto;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelbestellt;
import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelbestelltFac;
import com.lp.server.artikel.service.ArtikelkommentarDto;
import com.lp.server.artikel.service.ArtikellagerplaetzeDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ArtklaDto;
import com.lp.server.artikel.service.GebindeDto;
import com.lp.server.artikel.service.HerstellerDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.MaterialDto;
import com.lp.server.artikel.service.MaterialzuschlagDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.artikel.service.WarenabgangsreferenzDto;
import com.lp.server.artikel.service.WarenzugangsreferenzDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragReportFac;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.bl.OpenTransTransformer;
import com.lp.server.bestellung.ejb.BsmahnstufePK;
import com.lp.server.bestellung.fastlanereader.generated.FLRBSMahnung;
import com.lp.server.bestellung.fastlanereader.generated.FLRBSZahlungsplan;
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
import com.lp.server.bestellung.service.OpenTransOrder;
import com.lp.server.bestellung.service.OpenTransOrderHead;
import com.lp.server.bestellung.service.OpenTransOrderPosition;
import com.lp.server.bestellung.service.OpenTransXmlReportResult;
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
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.ejbfac.EJBExcFactory;
import com.lp.server.system.ejbfac.HvPrinter;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.pkgenerator.format.LpBelegnummerFormat;
import com.lp.server.system.service.BelegartDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.LandplzortDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.MediastandardDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.MwstsatzbezDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.system.service.SpediteurDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.UneceUnitCode;
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
import com.lp.util.report.PositionRpt;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

@Stateless
@Interceptors(TimingInterceptor.class)
public class BestellungReportFacBean extends LPReport implements BestellungReportFac, JRDataSource {

	@PersistenceContext
	private EntityManager em;

	private final static int UC_ALLE = 0;
	private final static int UC_BESTELLUNG = 1;
	private final static int UC_OFFENE = 2;
	private final static int UC_BESTELLVORSCHLAG = 4;
	private final static int UC_WEP_ETIKETT = 5;
	private final static int UC_BSSAMMELMAHNUNG = 6;
	private final static int UC_BSMAHNUNG = 7;
	private final static int UC_BESTELLUNG_WARENEINGANG = 8;
	private final static int UC_ABHOLAUFTRAG = 9;
	private final static int UC_GEAENDERTE_ARTIKEL = 10;
	private final static int UC_RAHMENUEBERSICHT = 11;
	private final static int UC_ZAHLUNGSPLAN = 12;
	private final static int UC_STANDORTLISTE = 13;
	private final static int UC_ERWARTETELIEFERUNGEN = 14;
	private final static int UC_TERMINUEBERSICHT = 15;
	private final static int UC_WE_ETIKETTEN = 16;
	private final static int UC_BESTELLETIKETT = 17;

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
	public static int REPORT_ABHOLAUFTRAG_ARTIKELGEWICHT = 36;
	public static int REPORT_ABHOLAUFTRAG_ANZAHL_SPALTEN = 37;

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
	private final static int REPORT_BSSAMMELMAHNUNG_ANGEBOTSNUMMER = 14;
	private static final int REPORT_BSSAMMELMAHNUNG_SIZE = 15;

	public final static int REPORT_ZAHLUNGSPLAN_BESTELLNUMMER = 0;
	public final static int REPORT_ZAHLUNGSPLAN_STATUS = 1;
	public final static int REPORT_ZAHLUNGSPLAN_PROJEKT = 2;
	public final static int REPORT_ZAHLUNGSPLAN_LIEFERTERMIN = 3;
	public final static int REPORT_ZAHLUNGSPLAN_BELEGDATUM = 4;
	public final static int REPORT_ZAHLUNGSPLAN_LIEFERANT = 5;
	public final static int REPORT_ZAHLUNGSPLAN_LKZ = 6;
	public final static int REPORT_ZAHLUNGSPLAN_PLZ = 7;
	public final static int REPORT_ZAHLUNGSPLAN_ORT = 8;
	public final static int REPORT_ZAHLUNGSPLAN_TERMIN = 9;
	public final static int REPORT_ZAHLUNGSPLAN_BETRAG = 10;
	public final static int REPORT_ZAHLUNGSPLAN_BETRAG_URSPRUNG = 11;
	public final static int REPORT_ZAHLUNGSPLAN_KOMMENTAR = 12;
	public final static int REPORT_ZAHLUNGSPLAN_KOMMENTAR_LANG = 13;
	public final static int REPORT_ZAHLUNGSPLAN_PERSON_ERLEDIGT = 14;
	public final static int REPORT_ZAHLUNGSPLAN_KURZZEICHEN_ERLEDIGT = 15;
	public final static int REPORT_ZAHLUNGSPLAN_ERLEDIGUNGSZEITPUNKT = 16;
	public final static int REPORT_ZAHLUNGSPLAN_ANZAHL_SPALTEN = 17;

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
	private final static int REPORT_BSWARENEINGANGSJOURNAL_STATUS = 25;
	private final static int REPORT_BSWARENEINGANGSJOURNAL_LIEFERTERMIN = 26;
	private final static int REPORT_BSWARENEINGANGSJOURNAL_AB_TERMIN = 27;
	private final static int REPORT_BSWARENEINGANGSJOURNAL_PREISE_ERFASST = 28;
	private final static int REPORT_BSWARENEINGANGSJOURNAL_SUBREPORT_SNRCHNR = 29;
	private final static int REPORT_BSWARENEINGANGSJOURNAL_LIEFERART = 30;
	private final static int REPORT_BSWARENEINGANGSJOURNAL_ARTIKELREFERENZNUMMER = 31;
	private final static int REPORT_BSWARENEINGANGSJOURNAL_ZUSATZBEZEICHNUNG = 32;
	private final static int REPORT_BSWARENEINGANGSJOURNAL_ZUSATZBEZEICHNUNG2 = 33;
	private final static int REPORT_BSWARENEINGANGSJOURNAL_KURZBEZEICHNUNG = 34;
	private final static int REPORT_BSWARENEINGANGSJOURNAL_BESTELLMENGENEINHEIT = 35;
	private final static int REPORT_BSWARENEINGANGSJOURNAL_UMRECHNUNGSFAKTOR = 36;
	private final static int REPORT_BSWARENEINGANGSJOURNAL_BESTELLMENGENEINHEIT_INVERS = 37;
	private final static int REPORT_BSWARENEINGANGSJOURNAL_LAGERPLAETZE = 38;
	private final static int REPORT_BSWARENEINGANGSJOURNAL_ANZAHL_SPALTEN = 39;

	public final static int REPORT_RAHMENUEBERSICHT_BESTELLUNGART = 0;
	public final static int REPORT_RAHMENUEBERSICHT_BESTELLNR = 1;
	public final static int REPORT_RAHMENUEBERSICHT_BELEGDATUM = 2;
	public final static int REPORT_RAHMENUEBERSICHT_BESTELLWERT = 3;
	public final static int REPORT_RAHMENUEBERSICHT_LIEFERTERMIN = 4;
	public final static int REPORT_RAHMENUEBERSICHT_ARTIKELNUMMER = 5;
	public final static int REPORT_RAHMENUEBERSICHT_ARTIKELBEZEICHNUNG = 6;
	public final static int REPORT_RAHMENUEBERSICHT_MENGE = 7;
	public final static int REPORT_RAHMENUEBERSICHT_LIEFERANTENANGEBOTNUMMER = 8;
	public final static int REPORT_RAHMENUEBERSICHT_PREIS = 9;
	public final static int REPORT_RAHMENUEBERSICHT_STORNIERT = 10;
	public final static int REPORT_RAHMENUEBERSICHT_WARENEINGANG = 11;
	public final static int REPORT_RAHMENUEBERSICHT_EINGANGSRECHNUNG = 12;
	public final static int REPORT_RAHMENUEBERSICHT_KEIN_RAHMENBEZUG = 13;
	public final static int REPORT_RAHMENUEBERSICHT_ANZAHL_SPALTEN = 14;

	public static int REPORT_STANDORTLISTE_ARTIKELNUMMER = 0;
	public static int REPORT_STANDORTLISTE_BEZEICHNUNG = 1;
	public static int REPORT_STANDORTLISTE_ZUSATZBEZEICHNUNG = 2;
	public static int REPORT_STANDORTLISTE_KURZBEZEICHNUNG = 3;
	public static int REPORT_STANDORTLISTE_SUBREPORT_STANDORTE = 4;
	public static int REPORT_STANDORTLISTE_ANZAHL_SPALTEN = 5;

	public static int REPORT_TERMINUEBERSICHT_ARTIKELNUMMER = 0;
	public static int REPORT_TERMINUEBERSICHT_BEZEICHNUNG = 1;
	public static int REPORT_TERMINUEBERSICHT_ZUSATZBEZEICHNUNG = 2;
	public static int REPORT_TERMINUEBERSICHT_SUBREPORT_BEWEGUNGSVORSCHAU = 3;
	public static int REPORT_TERMINUEBERSICHT_ANZAHL_SPALTEN = 4;

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
			} else if ("F_LIEFERART".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_LIEFERART];
			} else if ("F_STATUS".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_STATUS];
			} else if ("F_IDENT".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_IDENT];
			} else if ("F_ARTIKELREFERENZNUMMER".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_ARTIKELREFERENZNUMMER];
			} else if ("F_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_BEZEICHNUNG];
			} else if ("F_MENGE".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_MENGE];
			} else if ("F_GELIEFERTERPREIS".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_GELIEFERTERPREIS];
			} else if ("F_PREISE_ERFASST".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_PREISE_ERFASST];
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
			} else if ("F_LIEFERTERMIN".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_LIEFERTERMIN];
			} else if ("F_AB_TERMIN".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_AB_TERMIN];
			} else if ("F_SUBREPORT_SNRCHNR".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_SUBREPORT_SNRCHNR];
			} else if ("F_ZUSATZBEZEICHNUNG".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_ZUSATZBEZEICHNUNG];
			} else if ("F_ZUSATZBEZEICHNUNG2".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_ZUSATZBEZEICHNUNG2];
			} else if ("F_KURZBEZEICHNUNG".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_KURZBEZEICHNUNG];
			} else if ("F_BESTELLMENGENEINHEIT".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_BESTELLMENGENEINHEIT];
			} else if ("F_UMRECHNUNGSFAKTOR".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_UMRECHNUNGSFAKTOR];
			} else if ("F_BESTELLMENGENEINHEIT_INVERS".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_BESTELLMENGENEINHEIT_INVERS];
			} else if ("F_LAGERPLAETZE".equals(fieldName)) {
				value = data[index][REPORT_BSWARENEINGANGSJOURNAL_LAGERPLAETZE];
			}

		}
			break;
		case UC_OFFENE: {
			if ("F_BESTELLUNGCNR".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_BESTELLUNGCNR];
			} else if ("F_STATUS".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_STATUS];
			} else if ("F_BESTELLUNGARTCNR".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_BESTELLUNGARTCNR];
			} else if ("F_LIEFERTERMIN".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_BESTELLUNGLIEFERTERMIN];
			} else if ("F_BESTELLUNGLIEFERANT".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_BESTELLUNGLIEFERANT];
			} else if ("F_KOSTENSTELLE".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_KOSTENSTELLECNR];
			} else if ("F_ARTIKELCNR".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ARTIKELCNR];
			} else if ("F_ARTIKELREFERENZNUMMER".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ARTIKELREFERENZNUMMER];
			} else if ("F_POS_NR".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_POS_NR];
			} else if ("F_ARTIKELBEZEICHNUNG".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ARTIKELBEZ];
			} else if ("F_ARTIKELZUSATZBEZEICHNUNG".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ARTIKELZBEZ];
			} else if ("F_ARTIKELZUSATZBEZEICHNUNG2".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ARTIKELZBEZ2];
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
			} else if ("F_OFFENE_FIXKOSTEN".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_OFFENE_FIXKOSTEN];
			} else if ("F_ARTIKELGELIEFERTEMENGE".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ARTIKELGELIEFERTEMENGE];
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
			} else if ("F_ABURSPRUNGSTERMIN".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ABURSPRUNGSTERMIN];
			} else if ("F_OFFENELIEFERUNG".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_OFFENELIEFERUNGEN];
			} else if ("F_SETARTIKEL_TYP".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_SETARTIKEL_TYP];
			} else if ("F_GEBINDENAME".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_GEBINDENAME];
			} else if ("F_ANZAHL_GEBINDE".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ANZAHL_GEBINDE];
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
			} else if ("F_ARTIKELREFERENZNUMMER".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELREFERENZNUMMER];
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
			} else if ("Mandant".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_MANDANT];
			} else if ("F_STANDORT".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_STANDORT];
			} else if ("F_GEBINDENAME".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_GEBINDENAME];
			} else if ("F_ANZAHL_GEBINDE".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ANZAHL_GEBINDE];
			} else if ("F_GEBINDEPFLICHTIG".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_GEBINDEPFLICHTIG];
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
			} else if ("F_ARTIKELRAHMENMENGE_OFFEN_BESTELLCNR".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKEL_RAHMENBESTELLNR];
			} else if ("F_ARTIKEL_OFFEN_BESTELLCNR".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKEL_OFFENE_BESTELLNR];
			} else if ("F_LIEFERANT_MATERIALZUSCHLAG".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_LIEFERANT_MATERIALZUSCHLAG];
			} else if ("F_SUBREPORT_VERWENDUNGSNACHWEIS".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_SUBREPORT_VERWENDUNGSNACHWEIS];
			} else if ("F_LIEFERANT_LIEF1PREIS".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_LIEFERANT_LIEF1PREIS];
			} else if ("F_LIEFERANT_PREISGUELTIGAB".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_LIEFERANT_PREISGUELTIGAB];
			} else if ("ArtikelAnzahlAngeboten".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELANZAHLANGEBOTEN];
			} else if ("ArtikelAnzahlAngefragt".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELANZAHLANGEFRAGT];
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
			} else if ("F_KURZBEZEICHNUNG".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WEP_ETIKETT_KURZBEZEICHNUNG];
			} else if ("F_REFERENZNUMMER".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WEP_ETIKETT_REFERENZNUMMER];
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
			} else if ("F_WE_LIEFERSCHEINNUMMER".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WEP_ETIKETT_WE_LIEFERSCHEINNUMMER];
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
			} else if ("F_SUBREPORT_SNRCHNR".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WEP_ETIKETT_SUBREPORT_SNRCHNR];
			} else if ("F_GESTEHUNGSPREIS".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WEP_ETIKETT_GESTEHUNGSPREIS];
			} else if ("F_EINSTANDSPREIS".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WEP_ETIKETT_EINSTANDSPREIS];
			} else if ("F_LIEF1PREIS".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WEP_ETIKETT_LIEF1PREIS];
			} else if ("F_EXEMPLAR".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WEP_ETIKETT_EXEMPLAR];
			} else if ("F_EXEMPLARE_GESAMT".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WEP_ETIKETT_EXEMPLAREGESAMT];
			}

			else if ("F_LOS_NUMMER".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WEP_ETIKETT_LOS_NUMMER];
			} else if ("F_LOS_STUECKLISTE".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WEP_ETIKETT_LOS_STUECKLISTE];
			} else if ("F_LOS_MENGE".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WEP_ETIKETT_LOS_MENGE];
			} else if ("F_LOS_STUECKLISTE_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WEP_ETIKETT_LOS_STUECKLISTE_BEZEICHNUNG];
			} else if ("F_LOS_PROJEKT".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WEP_ETIKETT_LOS_PROJEKT];
			} else if ("F_LAGERMENGE".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WEP_ETIKETT_LAGERMENGE];
			}

		}
			break;
		case UC_WE_ETIKETTEN: {
			if ("F_ANLIEFERMENGE".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WE_ETIKETTEN_ANLIEFERMENGE];
			} else if ("F_BESTELLNUMMER".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WE_ETIKETTEN_BESTELLNUMMER];
			} else if ("F_CHARGENNUMMER".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WE_ETIKETTEN_CHARGENNUMMER];
			} else if ("F_SERIENNUMMER".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WE_ETIKETTEN_SERIENNUMMER];
			} else if ("F_VERPACKUNGSEINHEIT".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WE_ETIKETTEN_VERPACKUNGSEINHEIT];
			} else if ("F_WARENVERKEHRSNUMMER".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WE_ETIKETTEN_WARENVERKEHRSNUMMER];
			} else if ("F_LAGER".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WE_ETIKETTEN_LAGER];
			} else if ("F_IDENT".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WE_ETIKETTEN_IDENT];
			} else if ("F_KURZBEZEICHNUNG".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WE_ETIKETTEN_KURZBEZEICHNUNG];
			} else if ("F_REFERENZNUMMER".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WE_ETIKETTEN_REFERENZNUMMER];
			} else if ("F_BEZ".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WE_ETIKETTEN_BEZ];
			} else if ("F_ZBEZ".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WE_ETIKETTEN_ZBEZ];
			} else if ("F_ZBEZ2".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WE_ETIKETTEN_ZBEZ2];
			} else if ("F_LIEFERANTENARTIKELNUMMER".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WE_ETIKETTEN_LIEFERANTENARTIKELNUMMER];
			} else if ("F_LIEFERANTENARTIKELBEZ".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WE_ETIKETTEN_LIEFERANTENARTIKELBEZ];
			} else if ("F_WE_DATUM".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WE_ETIKETTEN_WE_DATUM];
			} else if ("F_WE_LIEFERSCHEINNUMMER".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WE_ETIKETTEN_WE_LIEFERSCHEINNUMMER];
			} else if ("F_EINHEIT".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WE_ETIKETTEN_EINHEIT];
			} else if ("F_PROJEKTBEZ".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WE_ETIKETTEN_PROJEKTBEZ];
			} else if ("F_LIEFERANTENNAME".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WE_ETIKETTEN_LIEFERANTNAME];
			} else if ("F_HERSTELLER".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WE_ETIKETTEN_HERSTELLER];
			} else if ("F_HERSTELLERNAME".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WE_ETIKETTEN_HERSTELLERNAME];
			} else if ("F_WE_REFERENZ".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WE_ETIKETTEN_WE_REFERENZ];
			} else if ("F_SUBREPORT_SNRCHNR".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WE_ETIKETTEN_SUBREPORT_SNRCHNR];
			} else if ("F_GESTEHUNGSPREIS".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WE_ETIKETTEN_GESTEHUNGSPREIS];
			} else if ("F_EINSTANDSPREIS".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WE_ETIKETTEN_EINSTANDSPREIS];
			} else if ("F_LIEF1PREIS".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WE_ETIKETTEN_LIEF1PREIS];
			} else if ("F_EXEMPLAR".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WE_ETIKETTEN_EXEMPLAR];
			} else if ("F_EXEMPLARE_GESAMT".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WE_ETIKETTEN_EXEMPLAREGESAMT];
			} else if ("F_PAKETMENGE".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WE_ETIKETTEN_PAKETMENGE];
			} else if ("F_LAGERPLATZ".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WE_ETIKETTEN_LAGERPLATZ];
			} else if ("F_URSPRUNGSLAND".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WE_ETIKETTEN_URSPRUNGSLAND];
			} else if ("F_GEWICHT".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_WE_ETIKETTEN_GEWICHT];
			}
		}
			break;
		case UC_BESTELLETIKETT: {
			if ("F_BESTELLNUMMER".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLETIKETTEN_BESTELLNUMMER];
			} else if ("F_VERPACKUNGSEINHEIT".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLETIKETTEN_VERPACKUNGSEINHEIT];
			} else if ("F_WARENVERKEHRSNUMMER".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLETIKETTEN_WARENVERKEHRSNUMMER];
			} else if ("F_LAGER".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLETIKETTEN_LAGER];
			} else if ("F_IDENT".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLETIKETTEN_IDENT];
			} else if ("F_KURZBEZEICHNUNG".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLETIKETTEN_KURZBEZEICHNUNG];
			} else if ("F_REFERENZNUMMER".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLETIKETTEN_REFERENZNUMMER];
			} else if ("F_BEZ".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLETIKETTEN_BEZ];
			} else if ("F_ZBEZ".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLETIKETTEN_ZBEZ];
			} else if ("F_ZBEZ2".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLETIKETTEN_ZBEZ2];
			} else if ("F_LIEFERANTENARTIKELNUMMER".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLETIKETTEN_LIEFERANTENARTIKELNUMMER];
			} else if ("F_LIEFERANTENARTIKELBEZ".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLETIKETTEN_LIEFERANTENARTIKELBEZ];
			} else if ("F_EINHEIT".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLETIKETTEN_EINHEIT];
			} else if ("F_PROJEKTBEZ".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLETIKETTEN_PROJEKTBEZ];
			} else if ("F_LIEFERANTENNAME".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLETIKETTEN_LIEFERANTNAME];
			} else if ("F_HERSTELLER".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLETIKETTEN_HERSTELLER];
			} else if ("F_HERSTELLERNAME".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLETIKETTEN_HERSTELLERNAME];
			} else if ("F_GESTEHUNGSPREIS".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLETIKETTEN_GESTEHUNGSPREIS];
			} else if ("F_LIEF1PREIS".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLETIKETTEN_LIEF1PREIS];
			} else if ("F_EXEMPLAR".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLETIKETTEN_EXEMPLAR];
			} else if ("F_EXEMPLARE_GESAMT".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLETIKETTEN_EXEMPLAREGESAMT];
			} else if ("F_PAKETMENGE".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLETIKETTEN_PAKETMENGE];
			} else if ("F_LAGERPLATZ".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLETIKETTEN_LAGERPLATZ];
			} else if ("F_URSPRUNGSLAND".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLETIKETTEN_URSPRUNGSLAND];
			} else if ("F_GEWICHT".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLETIKETTEN_GEWICHT];
			} else if ("F_BESTELLMENGE".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLETIKETTEN_BESTELLMENGE];
			} else if ("F_OFFENEMENGE".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLETIKETTEN_OFFENEMENGE];
			} else if ("F_GELIEFERTEMENGE".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLETIKETTEN_GELIEFERTEMENGE];
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
			} else if ("F_ANGEBOTSNUMMER".equals(fieldName)) {
				value = data[index][REPORT_BSSAMMELMAHNUNG_ANGEBOTSNUMMER];
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
		case UC_STANDORTLISTE: {
			if ("F_ARTIKELNUMMER".equals(fieldName)) {
				value = data[index][REPORT_STANDORTLISTE_ARTIKELNUMMER];
			} else if ("F_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][REPORT_STANDORTLISTE_BEZEICHNUNG];
			} else if ("F_KURZBEZEICHNUNG".equals(fieldName)) {
				value = data[index][REPORT_STANDORTLISTE_KURZBEZEICHNUNG];
			} else if ("F_SUBREPORT_STANDORTE".equals(fieldName)) {
				value = data[index][REPORT_STANDORTLISTE_SUBREPORT_STANDORTE];
			} else if ("F_ZUSATZBEZEICHNUNG".equals(fieldName)) {
				value = data[index][REPORT_STANDORTLISTE_ZUSATZBEZEICHNUNG];
			}
		}
			break;
		case UC_TERMINUEBERSICHT: {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_TERMINUEBERSICHT_ARTIKELNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_TERMINUEBERSICHT_BEZEICHNUNG];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_TERMINUEBERSICHT_ZUSATZBEZEICHNUNG];
			} else if ("SubreportBewegungsvorschau".equals(fieldName)) {
				value = data[index][REPORT_TERMINUEBERSICHT_SUBREPORT_BEWEGUNGSVORSCHAU];
			}
		}
			break;
		case UC_ERWARTETELIEFERUNGEN: {
			if ("Artikel".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_ERWARTETELIEFERUNGEN_ARTIKELNUMMER];
			} else if ("Bestellung".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_ERWARTETELIEFERUNGEN_BESTELLUNG];
			} else if ("Bestellungart".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_ERWARTETELIEFERUNGEN_BESTELLUNGSART];
			} else if ("Bestellt".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_ERWARTETELIEFERUNGEN_BESTELLT];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_ERWARTETELIEFERUNGEN_BEZEICHNUNG];
			} else if ("Rueckstand".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_ERWARTETELIEFERUNGEN_RUECKSTAND];
			} else if ("Danach".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_ERWARTETELIEFERUNGEN_DANACH];
			} else if ("Einheit".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_ERWARTETELIEFERUNGEN_EINHEIT];
			} else if ("Fehlmenge".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_ERWARTETELIEFERUNGEN_FEHLMENGE];
			} else if ("InFertigung".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_ERWARTETELIEFERUNGEN_INFERTIGUNG];
			} else if ("LieferantKbez".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_ERWARTETELIEFERUNGEN_LIEFERANT_KBEZ];
			} else if ("LieferantLkz".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_ERWARTETELIEFERUNGEN_LIEFERANT_LKZ];
			} else if ("Lieferant".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_ERWARTETELIEFERUNGEN_LIEFERANT_NAME];
			} else if ("LieferantOrt".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_ERWARTETELIEFERUNGEN_LIEFERANT_ORT];
			} else if ("LieferantPlz".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_ERWARTETELIEFERUNGEN_LIEFERANT_PLZ];
			} else if ("Kurzbezeichnung".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_ERWARTETELIEFERUNGEN_KURZBEZEICHNUNG];
			} else if ("Lagerstand".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_ERWARTETELIEFERUNGEN_LAGERSTAND];
			} else if ("OffeneMenge".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_ERWARTETELIEFERUNGEN_OFFENE_MENGE];
			} else if ("Rahmenbestellt".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_ERWARTETELIEFERUNGEN_RAHMENBESTELLT];
			} else if ("Reserviert".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_ERWARTETELIEFERUNGEN_RESERVIERT];
			} else if ("SubreportTermine".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_ERWARTETELIEFERUNGEN_SUBREPORT_TERMINE];
			} else if ("Anforderer".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_ERWARTETELIEFERUNGEN_ANFORDERER];
			} else if ("EKPreis".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_ERWARTETELIEFERUNGEN_EKPREIS];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_ERWARTETELIEFERUNGEN_ZUSATZBEZEICHNUNG];
			} else if ("Zusatzbezeichnung2".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_ERWARTETELIEFERUNGEN_ZUSATZBEZEICHNUNG2];
			}

			else if ("KommissionierungDurchgefuehrt".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_ERWARTETELIEFERUNGEN_KOMISSIONIERUNG_DURCHGEFUEHRT];
			} else if ("KommissionierungGeplant".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_ERWARTETELIEFERUNGEN_KOMISSIONIERUNG_GEPLANT];
			} else if ("UnterlagenUebergeben".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_ERWARTETELIEFERUNGEN_UNTERLAGEN_UEBERGEBEN];
			}

		}
			break;

		case UC_ZAHLUNGSPLAN: {
			if ("Betrag".equals(fieldName)) {
				value = data[index][REPORT_ZAHLUNGSPLAN_BETRAG];
			} else if ("BetragUrsprung".equals(fieldName)) {
				value = data[index][REPORT_ZAHLUNGSPLAN_BETRAG_URSPRUNG];
			} else if ("Erledigungszeitpunkt".equals(fieldName)) {
				value = data[index][REPORT_ZAHLUNGSPLAN_ERLEDIGUNGSZEITPUNKT];
			} else if ("Kommentar".equals(fieldName)) {
				value = data[index][REPORT_ZAHLUNGSPLAN_KOMMENTAR];
			} else if ("KommentarLang".equals(fieldName)) {
				value = data[index][REPORT_ZAHLUNGSPLAN_KOMMENTAR_LANG];
			} else if ("KurzzeichenErledigt".equals(fieldName)) {
				value = data[index][REPORT_ZAHLUNGSPLAN_KURZZEICHEN_ERLEDIGT];
			} else if ("PersonErledigt".equals(fieldName)) {
				value = data[index][REPORT_ZAHLUNGSPLAN_PERSON_ERLEDIGT];
			} else if ("Termin".equals(fieldName)) {
				value = data[index][REPORT_ZAHLUNGSPLAN_TERMIN];
			} else if ("Status".equals(fieldName)) {
				value = data[index][REPORT_ZAHLUNGSPLAN_STATUS];
			} else if ("Projekt".equals(fieldName)) {
				value = data[index][REPORT_ZAHLUNGSPLAN_PROJEKT];
			} else if ("Liefertermin".equals(fieldName)) {
				value = data[index][REPORT_ZAHLUNGSPLAN_LIEFERTERMIN];
			} else if ("Belegdatum".equals(fieldName)) {
				value = data[index][REPORT_ZAHLUNGSPLAN_BELEGDATUM];
			} else if ("Bestellnummer".equals(fieldName)) {
				value = data[index][REPORT_ZAHLUNGSPLAN_BESTELLNUMMER];
			} else if ("Lieferant".equals(fieldName)) {
				value = data[index][REPORT_ZAHLUNGSPLAN_LIEFERANT];
			} else if ("Lkz".equals(fieldName)) {
				value = data[index][REPORT_ZAHLUNGSPLAN_LKZ];
			} else if ("Plz".equals(fieldName)) {
				value = data[index][REPORT_ZAHLUNGSPLAN_PLZ];
			} else if ("Ort".equals(fieldName)) {
				value = data[index][REPORT_ZAHLUNGSPLAN_ORT];
			}
		}
			break;

		case UC_RAHMENUEBERSICHT: {
			if ("Bestellungart".equals(fieldName)) {
				value = data[index][REPORT_RAHMENUEBERSICHT_BESTELLUNGART];
			} else if ("Bestellnummer".equals(fieldName)) {
				value = data[index][REPORT_RAHMENUEBERSICHT_BESTELLNR];
			} else if ("Belegdatum".equals(fieldName)) {
				value = data[index][REPORT_RAHMENUEBERSICHT_BELEGDATUM];
			} else if ("Bestellwert".equals(fieldName)) {
				value = data[index][REPORT_RAHMENUEBERSICHT_BESTELLWERT];
			} else if ("Liefertermin".equals(fieldName)) {
				value = data[index][REPORT_RAHMENUEBERSICHT_LIEFERTERMIN];
			} else if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_RAHMENUEBERSICHT_ARTIKELNUMMER];
			} else if ("Artikelbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_RAHMENUEBERSICHT_ARTIKELBEZEICHNUNG];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_RAHMENUEBERSICHT_MENGE];
			} else if ("Lieferantenangebotsnummer".equals(fieldName)) {
				value = data[index][REPORT_RAHMENUEBERSICHT_LIEFERANTENANGEBOTNUMMER];
			} else if ("Preis".equals(fieldName)) {
				value = data[index][REPORT_RAHMENUEBERSICHT_PREIS];
			} else if ("Wareneingang".equals(fieldName)) {
				value = data[index][REPORT_RAHMENUEBERSICHT_WARENEINGANG];
			} else if ("Eingangsrechnung".equals(fieldName)) {
				value = data[index][REPORT_RAHMENUEBERSICHT_EINGANGSRECHNUNG];
			} else if ("Storniert".equals(fieldName)) {
				value = data[index][REPORT_RAHMENUEBERSICHT_STORNIERT];
			} else if ("KeinRahmenbezug".equals(fieldName)) {
				value = data[index][REPORT_RAHMENUEBERSICHT_KEIN_RAHMENBEZUG];
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
			} else if ("F_ARTIKELZUSATZBEZEICHNUNG_BESTELLUNG".equals(fieldName)) {
				value = data[index][REPORT_GEAENDERTEARTIKEL_ARTIKELZUSATZBEZEICHNUNG_BESTELLUNG];
			} else if ("F_ARTIKELZUSATZBEZEICHNUNG2_BESTELLUNG".equals(fieldName)) {
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
			} else if ("ArtikelGewichtInKG".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_ARTIKELGEWICHT];
			} else if ("Gewichteinheit".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_GEWICHTEINHEIT];
			} else if ("F_ARTIKELCZBEZ2".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_ARTIKELCZBEZ2];
			} else if ("F_POSITIONTERMIN".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_POSITIONTERMIN];
			} else if ("Einzelpreis".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_EINZELPREIS];
			} else if ("Nettoeinzelpreis".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_NETTOEINZELPREIS];
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
			} else if ("F_LIEFERANT_ARTIKEL_VERPACKUNGSEINHEIT".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_LIEFERANT_VERPACKUNGSEINHEIT];
			} else if ("F_LIEFERANT_ARTIKEL_IDENTNUMMER".equals(fieldName)) {
				value = Helper.formatStyledTextForJasper(
						data[index][BestellungReportFac.REPORT_BESTELLUNG_LIEFERANT_ARTIKEL_IDENTNUMMER]);
			} else if ("F_LIEFERANT_ARTIKEL_BEZEICHNUNG".equals(fieldName)) {
				value = Helper.formatStyledTextForJasper(
						data[index][BestellungReportFac.REPORT_BESTELLUNG_LIEFERANT_ARTIKEL_BEZEICHNUNG]);
			} else if ("F_ARTIKEL_HERSTELLER".equals(fieldName)) {
				value = Helper.formatStyledTextForJasper(
						data[index][BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_HERSTELLER]);
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
			} else if ("F_HERSTELLER_KURZBEZEICHNUNG".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_HERSTELLER_KURZBEZEICHNUNG];
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
			} else if ("F_SOLLMATERIAL_SUBREPORT_ARBEITSGAENGE".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_SOLLMATERIAL_SUBREPORT_ARBEITSGAENGE];
			} else if ("F_SETARTIKEL_TYP".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_SETARTIKEL_TYP];
			} else if ("F_INSERAT_DTO".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_INSERAT_DTO];
			} else if ("F_INSERAT_KUNDE".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_INSERAT_KUNDE];
			}

			else if ("F_RAHMENMENGE".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_RAHMENMENGE];
			} else if ("F_ABGERUFENE_MENGE".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_ABGERUFENE_MENGE];
			} else if ("F_ERHALTENEMENGE".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_ERHALTENE_MENGE];
			} else if ("F_LETZTER_ABRUF".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_LETZTER_ABRUF];
			} else if ("F_GEBINDENAME".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_GEBINDENAME];
			} else if ("F_ANZAHL_GEBINDE".equals(fieldName)) {
				value = data[index][BestellungReportFac.REPORT_BESTELLUNG_ANZAHL_GEBINDE];
			} else if ("Herstellernummer".equals(fieldName)) {
				value = Helper.formatStyledTextForJasper(
						data[index][BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_HERSTELLERNUMMER]);
			} else if ("Herstellerbezeichnung".equals(fieldName)) {
				value = Helper.formatStyledTextForJasper(
						data[index][BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_HERSTELLERBEZEICHNUNG]);
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
			} else if ("ArtikelGewichtInKG".equals(fieldName)) {
				value = data[index][REPORT_ABHOLAUFTRAG_ARTIKELGEWICHT];
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
				value = Helper.formatStyledTextForJasper(data[index][REPORT_ABHOLAUFTRAG_ARTIKEL_HERSTELLER]);
			} else if (F_REFERENZNUMMER.equals(fieldName)) {
				value = Helper.formatStyledTextForJasper(data[index][REPORT_ABHOLAUFTRAG_REFERENZNUMMER]);
			} else if (F_ARTIKELKOMMENTAR.equals(fieldName)) {
				value = Helper.formatStyledTextForJasper(data[index][REPORT_ABHOLAUFTRAG_ARTIKELKOMMENTAR]);
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

	public JasperPrintLP printBestellungenAlle(ReportJournalKriterienDto krit, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Session session = null;
		try {
			this.useCase = UC_ALLE;
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();

			// reportjournal: 00 Erzeugen einer Hibernate-Criteria-Query
			Criteria c = session.createCriteria(FLRBestellung.class);
			// reportjournal: 01 Filter nach Mandant
			c.add(Restrictions.eq(BestellungFac.FLR_BESTELLUNG_MANDANT_C_NR, theClientDto.getMandant()));
			// reportjournal: 02 Filter: nur eine Kostenstelle
			if (krit.kostenstelleIId != null) {
				c.add(Restrictions.eq(BestellungFac.FLR_BESTELLUNG_KOSTENSTELLE_I_ID, krit.kostenstelleIId));
			}
			// reportjournal: 03 Filter: nur ein Lieferant
			if (krit.lieferantIId != null) {
				c.add(Restrictions.eq(BestellungFac.FLR_BESTELLUNG_LIEFERANT_I_ID_BESTELLADRESSE, krit.lieferantIId));
			}
			String sVon = null;
			String sBis = null;
			// reportjournal: 04 Datum von
			if (krit.dVon != null) {
				c.add(Restrictions.ge(BestellungFac.FLR_BESTELLUNG_T_LIEFERTERMIN, Helper.cutDate(krit.dVon)));
				sVon = Helper.formatDatum(krit.dVon, theClientDto.getLocUi());
			}
			// reportjournal: 05 Datum bis
			if (krit.dBis != null) {
				c.add(Restrictions.lt(BestellungFac.FLR_BESTELLUNG_T_LIEFERTERMIN,
						Helper.cutDate(Helper.addiereTageZuDatum(krit.dBis, 1))));
				sBis = Helper.formatDatum(krit.dBis, theClientDto.getLocUi());
			}
			// reportjournalbelegnummer: 0 dazu muss ich das Belegnummernformat
			// und das
			// aktuelle Geschaeftsjahr des Mandanten kennen.
			LpBelegnummerFormat f = getBelegnummerGeneratorObj().getBelegnummernFormat(theClientDto.getMandant());
			Integer iGeschaeftsjahr = getParameterFac().getGeschaeftsjahr(theClientDto.getMandant());
			String sMandantKuerzel = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_BELEGNUMMER_MANDANTKENNUNG).getCWert();
			// reportjournal: 06 belegnummer von
			// reportjournalbelegnummer: 1 (von) hier funktionierts fast gleich
			// wie bei den Direktfiltern
			if (krit.sBelegnummerVon != null) {
				sVon = HelperServer.getBelegnummernFilterForHibernateCriterias(f, iGeschaeftsjahr, sMandantKuerzel,
						krit.sBelegnummerVon);
				c.add(Restrictions.ge(BestellungFac.FLR_BESTELLUNG_C_NR, sVon));
			}
			// reportjournal: 07 belegnummer bis
			// reportjournalbelegnummer: 2 (bis) detto
			if (krit.sBelegnummerBis != null) {
				sBis = HelperServer.getBelegnummernFilterForHibernateCriterias(f, iGeschaeftsjahr, sMandantKuerzel,
						krit.sBelegnummerBis);
				c.add(Restrictions.le(BestellungFac.FLR_BESTELLUNG_C_NR, sBis));
			}
			// reportjournal: 08 Sortierung nach Kostenstelle
			if (krit.bSortiereNachKostenstelle) {
				c.createCriteria(BestellungFac.FLR_BESTELLUNG_FLRKOSTENSTELLE).addOrder(Order.asc("c_nr"));
			}
			// reportjournal: 09 Sortierung nach Lieferant
			if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				c.createCriteria(BestellungFac.FLR_BESTELLUNG_FLRLIEFERANT).createCriteria(LieferantFac.FLR_PARTNER)
						.addOrder(Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));
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
				if (getTheJudgeFac().hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_EINKAUF, theClientDto)) {
					data[i][ALLE_BESTELLWERT] = b.getN_bestellwert();
				} else {
					data[i][ALLE_BESTELLWERT] = null;
				}
				data[i][ALLE_KOSTENSTELLENUMMER] = b.getFlrkostenstelle() != null ? b.getFlrkostenstelle().getC_nr()
						: null;
				data[i][ALLE_LIEFERANT] = b.getFlrlieferant().getFlrpartner().getC_name1nachnamefirmazeile1();
				data[i][ALLE_LIEFERTERMIN] = b.getT_liefertermin();
				i++;
			}
			Map<String, Object> map = new TreeMap<String, Object>();
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
			// Waehrung
			map.put(LPReport.P_WAEHRUNG, mandantDto.getWaehrungCNr());
			StringBuffer sSortierung = new StringBuffer();
			// Sortierung nach Kostenstelle
			// reportjournalparameter: 0 nach Kostenstelle
			map.put(LPReport.P_SORTIERENACHKOSTENSTELLE, new Boolean(krit.bSortiereNachKostenstelle));
			if (krit.bSortiereNachKostenstelle) {
				sSortierung.append(
						getTextRespectUISpr("lp.kostenstelle", theClientDto.getMandant(), theClientDto.getLocUi()));
				sSortierung.append(", ");
			}
			// Sortierung nach Lieferant
			// reportjournalparameter: 1 nach Lieferanten
			map.put(LPReport.P_SORTIERENACHLIEFERANT,
					new Boolean(krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER));
			if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				sSortierung.append(
						getTextRespectUISpr("lp.lieferant", theClientDto.getMandant(), theClientDto.getLocUi()));
			}
			// reportjournalparameter: 2 nach Belegnummer
			else if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_BELEGNUMMER) {
				sSortierung
						.append(getTextRespectUISpr("bes.bestnr", theClientDto.getMandant(), theClientDto.getLocUi()));
			}
			StringBuffer sFilter = new StringBuffer();
			if (sVon != null) {
				sFilter.append(getTextRespectUISpr("lp.von", theClientDto.getMandant(), theClientDto.getLocUi()));
				sFilter.append(" " + sVon + " ");
			}
			if (sBis != null) {
				sFilter.append(getTextRespectUISpr("lp.bis", theClientDto.getMandant(), theClientDto.getLocUi()));
				sFilter.append(" " + sBis + " ");
			}
			if (krit.kostenstelleIId != null) {
				if (sFilter.length() > 0) {
					sFilter.append(", ");
				}
				KostenstelleDto kstDto = getSystemFac().kostenstelleFindByPrimaryKey(krit.kostenstelleIId);
				sFilter.append(
						getTextRespectUISpr("lp.kostenstelle", theClientDto.getMandant(), theClientDto.getLocUi()));
				sFilter.append(" ");
				sFilter.append(kstDto.getCNr());
			}

			// reportjournalparameter: 3 Uebergabe
			map.put(LPReport.P_SORTIERUNG, sSortierung.toString());
			map.put(LPReport.P_FILTER, sFilter.toString());
			initJRDS(map, BestellungReportFac.REPORT_MODUL, BestellungReportFac.REPORT_BESTELLUNGEN_ALLE,
					theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} finally {
			closeSession(session);
		}
		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printStandortliste(TheClientDto theClientDto) {
		useCase = UC_STANDORTLISTE;
		HashMap<String, Object> parameter = new HashMap<String, Object>();

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT distinct  bv.flrartikel.i_id, bv.flrartikel.c_nr FROM FLRBestellvorschlag bv WHERE bv.mandant_c_nr='"
				+ theClientDto.getMandant() + "' " + "ORDER BY bv.flrartikel.c_nr ASC";

		org.hibernate.Query query = session.createQuery(queryString);
		List resultList = query.list();

		Iterator resultListIterator = resultList.iterator();

		ArrayList<Object[]> alDaten = new ArrayList<Object[]>();

		while (resultListIterator.hasNext()) {
			Object[] bv = (Object[]) resultListIterator.next();

			Object[] oZeile = new Object[REPORT_STANDORTLISTE_ANZAHL_SPALTEN];

			Integer artikelIId = (Integer) bv[0];

			oZeile[REPORT_STANDORTLISTE_ARTIKELNUMMER] = bv[1];

			ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(artikelIId, theClientDto);

			if (aDto.getArtikelsprDto() != null) {
				oZeile[REPORT_STANDORTLISTE_BEZEICHNUNG] = aDto.getArtikelsprDto().getCBez();
				oZeile[REPORT_STANDORTLISTE_KURZBEZEICHNUNG] = aDto.getArtikelsprDto().getCKbez();
				oZeile[REPORT_STANDORTLISTE_ZUSATZBEZEICHNUNG] = aDto.getArtikelsprDto().getCZbez();
			}

			// Verwendungsnachweis
			Session session2 = FLRSessionFactory.getFactory().openSession();

			String sQueryVerwendungsnachweis = "SELECT distinct l.parnter_i_id_standort,l.flrpartner.c_kbez FROM FLRLager l WHERE l.parnter_i_id_standort IS NOT NULL ORDER BY l.flrpartner.c_kbez ASC";

			Query q = session2.createQuery(sQueryVerwendungsnachweis);
			List<?> results = q.list();
			Iterator itVW = results.iterator();

			Object[][] dataSub = new Object[results.size()][6];
			String[] fieldnames = new String[] { "F_STANDORT", "F_BEDARF", "F_LAGERSTAND", "F_FREI_VERFUEGBAR",
					"F_BESTELLT", "F_IN_FERTIGUNG" };
			int iZeileSub = 0;
			while (itVW.hasNext()) {
				Object[] o = (Object[]) itVW.next();
				Integer partner_i_id_standort = (Integer) o[0];
				String standort = (String) o[1];

				String queryStringBedarf = "SELECT sum(bv.n_zubestellendemenge) FROM FLRBestellvorschlag bv WHERE bv.artikel_i_id="
						+ artikelIId + "  AND bv.partner_i_id_standort=" + partner_i_id_standort;
				Session session3 = FLRSessionFactory.getFactory().openSession();
				Query qBedarf = session3.createQuery(queryStringBedarf);

				List<?> resultsBedarf = qBedarf.list();

				BigDecimal bdBedarf = BigDecimal.ZERO;
				if (resultsBedarf.iterator().hasNext()) {
					bdBedarf = (BigDecimal) resultsBedarf.iterator().next();
				}

				if (bdBedarf == null) {
					bdBedarf = BigDecimal.ZERO;
				}
				session3.close();
				dataSub[iZeileSub][0] = standort;
				dataSub[iZeileSub][1] = bdBedarf;

				BigDecimal[] bd = getLagerFac().getSummeLagermindesUndLagerSollstandEinesStandorts(artikelIId,
						partner_i_id_standort, theClientDto);

				dataSub[iZeileSub][2] = bd[2];

				try {
					dataSub[iZeileSub][4] = getArtikelbestelltFac().getAnzahlBestellt(artikelIId,
							partner_i_id_standort);

					BigDecimal bdReservierungen = getReservierungFac().getAnzahlReservierungen(artikelIId, null,
							theClientDto.getMandant(), partner_i_id_standort);

					if (bdReservierungen == null) {
						bdReservierungen = BigDecimal.ZERO;
					}
					BigDecimal bdFehlmengen = getFehlmengeFac().getAnzahlFehlmengeEinesArtikels(artikelIId,
							theClientDto, partner_i_id_standort);

					if (bdFehlmengen == null) {
						bdFehlmengen = BigDecimal.ZERO;
					}
					dataSub[iZeileSub][3] = bd[2].subtract(bdFehlmengen).subtract(bdReservierungen);

					BigDecimal bdAnzahlInFertigung = getFertigungFac().getAnzahlInFertigung(artikelIId, null,
							partner_i_id_standort, theClientDto);
					dataSub[iZeileSub][5] = bdAnzahlInFertigung;

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

				iZeileSub++;
			}
			oZeile[REPORT_STANDORTLISTE_SUBREPORT_STANDORTE] = new LPDatenSubreport(dataSub, fieldnames);
			session2.close();

			alDaten.add(oZeile);

		}
		session.close();
		data = new Object[alDaten.size()][REPORT_ZAHLUNGSPLAN_ANZAHL_SPALTEN];
		data = alDaten.toArray(data);

		initJRDS(parameter, REPORT_MODUL, REPORT_STANDORTLISTE, theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printZahlungsplan(Timestamp tFaelligBis, TheClientDto theClientDto) {
		useCase = UC_ZAHLUNGSPLAN;
		HashMap<String, Object> parameter = new HashMap<String, Object>();

		parameter.put("P_STICHTAG", tFaelligBis);

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT zp FROM FLRBSZahlungsplan zp WHERE zp.flrbestellung.mandant_c_nr='"
				+ theClientDto.getMandant() + "' " + " AND zp.flrbestellung.bestellungstatus_c_nr NOT IN ('"
				+ BestellungFac.BESTELLSTATUS_STORNIERT + "','" + BestellungFac.BESTELLSTATUS_ERLEDIGT
				+ "') AND zp.t_termin<='" + Helper.formatDateWithSlashes(new Date(tFaelligBis.getTime()))
				+ "' ORDER BY zp.t_termin ASC";

		org.hibernate.Query query = session.createQuery(queryString);
		List resultList = query.list();

		Iterator resultListIterator = resultList.iterator();

		ArrayList<Object[]> alDaten = new ArrayList<Object[]>();

		while (resultListIterator.hasNext()) {
			FLRBSZahlungsplan zp = (FLRBSZahlungsplan) resultListIterator.next();

			Object[] oZeile = new Object[REPORT_ZAHLUNGSPLAN_ANZAHL_SPALTEN];

			oZeile[REPORT_ZAHLUNGSPLAN_BESTELLNUMMER] = zp.getFlrbestellung().getC_nr();

			oZeile[REPORT_ZAHLUNGSPLAN_STATUS] = zp.getFlrbestellung().getBestellungstatus_c_nr();
			oZeile[REPORT_ZAHLUNGSPLAN_PROJEKT] = zp.getFlrbestellung().getC_bezprojektbezeichnung();
			oZeile[REPORT_ZAHLUNGSPLAN_LIEFERTERMIN] = zp.getFlrbestellung().getT_liefertermin();
			oZeile[REPORT_ZAHLUNGSPLAN_BELEGDATUM] = zp.getFlrbestellung().getT_belegdatum();

			oZeile[REPORT_ZAHLUNGSPLAN_LIEFERANT] = HelperServer
					.formatNameAusFLRPartner(zp.getFlrbestellung().getFlrlieferant().getFlrpartner());
			if (zp.getFlrbestellung().getFlrlieferant().getFlrpartner().getFlrlandplzort() != null) {
				oZeile[REPORT_ZAHLUNGSPLAN_LKZ] = zp.getFlrbestellung().getFlrlieferant().getFlrpartner()
						.getFlrlandplzort().getFlrland().getC_lkz();
				oZeile[REPORT_ZAHLUNGSPLAN_PLZ] = zp.getFlrbestellung().getFlrlieferant().getFlrpartner()
						.getFlrlandplzort().getC_plz();
				oZeile[REPORT_ZAHLUNGSPLAN_ORT] = zp.getFlrbestellung().getFlrlieferant().getFlrpartner()
						.getFlrlandplzort().getFlrort().getC_name();

			}

			oZeile[REPORT_ZAHLUNGSPLAN_TERMIN] = zp.getT_termin();
			oZeile[REPORT_ZAHLUNGSPLAN_BETRAG] = zp.getN_betrag();
			oZeile[REPORT_ZAHLUNGSPLAN_BETRAG_URSPRUNG] = zp.getN_betrag_ursprung();

			oZeile[REPORT_ZAHLUNGSPLAN_KOMMENTAR] = zp.getC_kommentar();
			oZeile[REPORT_ZAHLUNGSPLAN_KOMMENTAR_LANG] = zp.getX_text();

			if (zp.getFlrpersonalerledigt() != null) {
				oZeile[REPORT_ZAHLUNGSPLAN_ERLEDIGUNGSZEITPUNKT] = zp.getT_erledigt();
				oZeile[REPORT_ZAHLUNGSPLAN_PERSON_ERLEDIGT] = HelperServer
						.formatPersonAusFLRPartner(zp.getFlrpersonalerledigt().getFlrpartner());
				oZeile[REPORT_ZAHLUNGSPLAN_KURZZEICHEN_ERLEDIGT] = zp.getFlrpersonalerledigt().getC_kurzzeichen();

			}

			alDaten.add(oZeile);

		}
		session.close();
		data = new Object[alDaten.size()][REPORT_ZAHLUNGSPLAN_ANZAHL_SPALTEN];
		data = alDaten.toArray(data);

		initJRDS(parameter, REPORT_MODUL, REPORT_ZAHLUNGSPLAN, theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();
	}

	public JasperPrintLP printBSMahnungAusMahnlauf(Integer bsmahnungIId, boolean bMitLogo, TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {
			this.useCase = UC_BSMAHNUNG;
			data = new Object[1][1];
			BSMahnungDto bsmahnungDto = getBSMahnwesenFac().bsmahnungFindByPrimaryKey(bsmahnungIId);
			return printBSMahnungFuerBestellung(bsmahnungDto.getBestellpositionIId(), bsmahnungDto.getBestellungIId(),
					bsmahnungDto.getMahnstufeIId(), new Date(bsmahnungDto.getTMahndatum().getTime()), bMitLogo,
					theClientDto);
		} catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, ex);
		}
	}

	public JasperPrintLP printBSMahnungFuerBestellung(Integer bestellpositionIId, Integer bestellungIId,
			Integer bsmahnstufeIId, Date dMahndatum, boolean bMitLogo, TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {
			this.useCase = UC_BSMAHNUNG;
			data = new Object[1][1];
			Map<String, Object> map = new TreeMap<String, Object>();
			BestellungDto bestellungDto = getBestellungFac().bestellungFindByPrimaryKey(bestellungIId);

			BestellpositionDto besposDto = getBestellpositionFac().bestellpositionFindByPrimaryKey(bestellpositionIId);

			LieferantDto lieferantDto = getLieferantFac()
					.lieferantFindByPrimaryKey(bestellungDto.getLieferantIIdBestelladresse(), theClientDto);
			Locale locDruck = Helper.string2Locale(lieferantDto.getPartnerDto().getLocaleCNrKommunikation());
			AnsprechpartnerDto oAnsprechpartner = null;
			if (bestellungDto.getAnsprechpartnerIId() != null) {
				oAnsprechpartner = getAnsprechpartnerFac()
						.ansprechpartnerFindByPrimaryKey(bestellungDto.getAnsprechpartnerIId(), theClientDto);
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

			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);

			map.put("P_LIEFERANT_ADRESSBLOCK",
					formatAdresseFuerAusdruck(lieferantDto.getPartnerDto(), oAnsprechpartner, mandantDto, locDruck));
			map.put("P_LIEFERANT_UID", lieferantDto.getPartnerDto().getCUid());
			PersonalDto oPersonalBenutzer = getPersonalFac().personalFindByPrimaryKey(theClientDto.getIDPersonal(),
					theClientDto);
			PersonalDto oPersonalAnleger = getPersonalFac()
					.personalFindByPrimaryKey(bestellungDto.getPersonalIIdAnlegen(), theClientDto);
			map.put("P_UNSER_ZEICHEN", Helper.getKurzzeichenkombi(oPersonalBenutzer.getCKurzzeichen(),
					oPersonalAnleger.getCKurzzeichen()));
			map.put("P_DATUM", new java.util.Date(getDate().getTime()));
			map.put("P_BERUECKSICHTIGTBIS", dMahndatum);

			Integer ansprechpartnerIId = null;
			if (oAnsprechpartner != null) {
				ansprechpartnerIId = oAnsprechpartner.getIId();
			}

			// belegkommunikation: 2 daten holen
			String sEmail = getPartnerFac().partnerkommFindRespectPartnerAsStringOhneExec(ansprechpartnerIId,
					lieferantDto.getPartnerDto(), PartnerFac.KOMMUNIKATIONSART_EMAIL, theClientDto.getMandant(),
					theClientDto);
			String sFax = getPartnerFac().partnerkommFindRespectPartnerAsStringOhneExec(ansprechpartnerIId,
					lieferantDto.getPartnerDto(), PartnerFac.KOMMUNIKATIONSART_FAX, theClientDto.getMandant(),
					theClientDto);
			String sTelefon = getPartnerFac().partnerkommFindRespectPartnerAsStringOhneExec(ansprechpartnerIId,
					lieferantDto.getPartnerDto(), PartnerFac.KOMMUNIKATIONSART_TELEFON, theClientDto.getMandant(),
					theClientDto);
			// belegkommunikation: 3 daten als parameter an den Report
			// weitergeben
			map.put(LPReport.P_ANSPRECHPARTNEREMAIL, sEmail != null ? sEmail : "");
			map.put(LPReport.P_ANSPRECHPARTNERFAX, sFax != null ? sFax : "");
			map.put(LPReport.P_ANSPRECHPARTNERTELEFON, sTelefon != null ? sTelefon : "");

			BSMahntextDto bsmahntextDto = getBSMahnwesenFac().bsmahntextFindByMandantLocaleCNr(mandantDto.getCNr(),
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
				map.put("P_MAHNTEXT", Helper.formatStyledTextForJasper(bsmahntextDto.getXTextinhalt()));
			} else {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_KEINE_MAHNTEXTE_EINGETRAGEN, "");
			}
			map.put("P_BESTELLUNGSNUMMER", bestellungDto.getCNr());
			map.put("P_ANGEBOTSNUMMER", bestellungDto.getCLieferantenangebot());
			map.put("P_BESTELLUNGSDATUM", bestellungDto.getDBelegdatum());
			map.put("P_BEZEICHNUNG", besposDto.getCBez());
			map.put("P_ZUSATZBEZEICHNUNG", besposDto.getCZusatzbez());
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(besposDto.getArtikelIId(), theClientDto);
			map.put("P_LIEFERANT_ARTIKEL_IDENTNUMMER", artikelDto.getCArtikelnrhersteller());
			map.put("P_IDENT", artikelDto.getCNr());
			map.put("P_ARTIKELCZBEZ2", artikelDto.formatBezeichnung());
			ArtikellieferantDto artLiefDto = getArtikelFac().getArtikelEinkaufspreis(besposDto.getArtikelIId(),
					lieferantDto.getIId(), BigDecimal.ONE, bestellungDto.getWaehrungCNr(),
					bestellungDto.getDBelegdatum(), theClientDto);
			if (artLiefDto != null) {
				map.put("P_LIEFERANT_ARTIKEL_BEZEICHNUNG", artLiefDto.getCBezbeilieferant());
			}
			BigDecimal bdOffeneMenge = getBestellpositionFac().berechneOffeneMenge(besposDto);
			map.put("P_OFFENEMENGE", bdOffeneMenge);
			map.put("P_EINHEIT", getSystemFac().formatEinheit(besposDto.getEinheitCNr(), locDruck, theClientDto));
			map.put("P_BSWERT", bdOffeneMenge.multiply(besposDto.getNNettogesamtpreis()));

			if (besposDto.getTUebersteuerterLiefertermin() != null) {
				map.put("P_LIEFERTERMIN", new Date(besposDto.getTUebersteuerterLiefertermin().getTime()));
			} else {
				map.put("P_LIEFERTERMIN", bestellungDto.getDLiefertermin());
			}

			if (besposDto.getTAuftragsbestaetigungstermin() != null) {
				map.put("P_ABTERMIN", new Date(besposDto.getTAuftragsbestaetigungstermin().getTime()));
			} else {
				map.put("P_ABTERMIN", null);
			}

			BSMahnstufeDto bsmahnstufeDto = getBSMahnwesenFac()
					.bsmahnstufeFindByPrimaryKeyOhneExc(new BsmahnstufePK(bsmahnstufeIId, mandantDto.getCNr()));
			map.put("P_MAHNSTUFE_TAGE", bsmahnstufeDto.getITage());

			map.put(LPReport.P_WAEHRUNG, mandantDto.getWaehrungCNr());

			map.put("P_ABSENDER", mandantDto.getPartnerDto().formatAnrede());
			map.put("P_BENUTZERNAME", oPersonalBenutzer.getPartnerDto().formatAnrede());
			map.put("P_MITLOGO", bMitLogo);

			initJRDS(map, BestellungReportFac.REPORT_MODUL,

					BestellungReportFac.REPORT_BSMAHNUNG, theClientDto.getMandant(), locDruck, theClientDto, bMitLogo,
					null);

			return getReportPrint();

		} catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, ex);
		}
	}

	public JasperPrintLP[] printBSSammelMahnung(Integer bsmahnlaufIId, boolean bMitLogo, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Session session = null;
		try {
			HashMap<Integer, Integer> hm = getAllLieferantenFromMahnlauf(bsmahnlaufIId);
			Collection<JasperPrintLP> c = new LinkedList<JasperPrintLP>();
			for (Iterator<Integer> iter = hm.keySet().iterator(); iter.hasNext();) {
				ArrayList<JasperPrintLP> print = printBSSammelMahnung(bsmahnlaufIId, (Integer) iter.next(),
						theClientDto, false, bMitLogo);

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

	private HashMap<Integer, Integer> getAllLieferantenFromMahnlauf(Integer bsmahnlaufIId) {
		Session session = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		session = factory.openSession();
		Criteria crit = session.createCriteria(FLRBSMahnung.class);
		crit.add(Restrictions.eq(BSMahnwesenFac.FLR_MAHNUNG_MAHNLAUF_I_ID, bsmahnlaufIId));
		List<?> list = crit.list();
		HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
		// alle lieferantenIds in eine hashmap -> quasi distinct
		for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
			FLRBSMahnung item = (FLRBSMahnung) iter.next();
			hm.put(item.getFlrbestellung().getFlrlieferant().getI_id(),
					item.getFlrbestellung().getFlrlieferant().getI_id());
		}
		return hm;
	}

	public ArrayList<JasperPrintLP> getMahnungenFuerAlleLieferanten(Integer bsmahnlaufIId, TheClientDto theClientDto,
			boolean bNurNichtGemahnte, boolean bMitLogo) throws EJBExceptionLP, RemoteException {
		HashMap<Integer, Integer> hm = getAllLieferantenFromMahnlauf(bsmahnlaufIId);
		ArrayList<JasperPrintLP> hmPrints = new ArrayList<JasperPrintLP>();
		for (Iterator<Integer> iter = hm.keySet().iterator(); iter.hasNext();) {
			Integer iLieferantIId = (Integer) iter.next();
			ArrayList<JasperPrintLP> print = null;

			try {
				print = printBSSammelMahnung(bsmahnlaufIId, iLieferantIId, theClientDto, bNurNichtGemahnte, bMitLogo);
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

	public JasperPrintLP printWepEtikett(Integer iIdWepI, Integer iIdBestellpositionI, Integer iIdLagerI,
			Integer iExemplare, Integer iVerpackungseinheit, Double dGewicht, String sWarenverkehrsnummer,
			String sLagerort, String sUrsprungsland, String sKommentar, BigDecimal bdHandmenge, String chargennummer,
			boolean bInklLosbuchungen, TheClientDto theClientDto) throws EJBExceptionLP {
		this.useCase = UC_WEP_ETIKETT;
		try {
			WareneingangspositionDto wepDto = null;
			WareneingangDto weDto = null;
			LagerDto lagerDto = null;

			BestellpositionDto bespDto = getBestellpositionFac().bestellpositionFindByPrimaryKey(iIdBestellpositionI);
			if (iIdWepI != null) {
				wepDto = getWareneingangFac().wareneingangspositionFindByPrimaryKey(iIdWepI);

				if (chargennummer != null) {
					if (wepDto.getSeriennrChargennrMitMenge() != null) {
						List<SeriennrChargennrMitMengeDto> list = wepDto.getSeriennrChargennrMitMenge();

						for (int i = 0; i < list.size(); i++) {
							if (list.get(i).getCSeriennrChargennr() != null
									&& list.get(i).getCSeriennrChargennr().equals(chargennummer)) {
								ArrayList al = new ArrayList();
								al.add(list.get(i));
								wepDto.setSeriennrChargennrMitMenge(al);
								wepDto.setNGeliefertemenge(list.get(i).getNMenge());
								break;
							}
						}
					}
				}

				weDto = getWareneingangFac().wareneingangFindByPrimaryKey(wepDto.getWareneingangIId());
			}
			BestellungDto bestellungDto = getBestellungFac().bestellungFindByPrimaryKey(bespDto.getBestellungIId());
			lagerDto = getLagerFac().lagerFindByPrimaryKey(iIdLagerI);
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(bespDto.getArtikelIId(), theClientDto);
			ArtikellieferantDto artikellieferantDto = getArtikelFac().getArtikelEinkaufspreis(artikelDto.getIId(),
					bestellungDto.getLieferantIIdBestelladresse(), bespDto.getNMenge(),

					bestellungDto.getWaehrungCNr(), new java.sql.Date(bestellungDto.getDBelegdatum().getTime()),
					theClientDto);

			LieferantDto lieferantDto = getLieferantFac()
					.lieferantFindByPrimaryKey(bestellungDto.getLieferantIIdBestelladresse(), theClientDto);
			// es gilt das Locale des Lieferanten
			Locale locDruck = Helper.string2Locale(lieferantDto.getPartnerDto().getLocaleCNrKommunikation());

			Object[] oZeile = new Object[REPORT_WEP_ETIKETT_ANZAHL_SPALTEN];

			if (wepDto != null) {
				oZeile[BestellungReportFac.REPORT_WEP_ETIKETT_ANLIEFERMENGE] = wepDto.getNGeliefertemenge();
				oZeile[BestellungReportFac.REPORT_WEP_ETIKETT_EINHEIT] = bespDto.getEinheitCNr().trim();
				if (Helper.short2boolean(artikelDto.getBChargennrtragend())) {
					oZeile[BestellungReportFac.REPORT_WEP_ETIKETT_CHARGENNUMMER] = SeriennrChargennrMitMengeDto
							.erstelleStringAusMehrerenSeriennummern(wepDto.getSeriennrChargennrMitMenge());
				}
				if (Helper.short2boolean(artikelDto.getBSeriennrtragend())) {
					oZeile[BestellungReportFac.REPORT_WEP_ETIKETT_SERIENNUMMER] = SeriennrChargennrMitMengeDto
							.erstelleStringAusMehrerenSeriennummern(wepDto.getSeriennrChargennrMitMenge());
				}

				oZeile[BestellungReportFac.REPORT_WEP_ETIKETT_EINSTANDSPREIS] = wepDto.getNEinstandspreis();

			}
			if (weDto != null) {
				oZeile[BestellungReportFac.REPORT_WEP_ETIKETT_WE_DATUM] = Helper
						.formatDatum(Helper.extractDate(weDto.getTWareneingangsdatum()), locDruck);
				oZeile[BestellungReportFac.REPORT_WEP_ETIKETT_WE_LIEFERSCHEINNUMMER] = weDto.getCLieferscheinnr();
			}
			oZeile[BestellungReportFac.REPORT_WEP_ETIKETT_BESTELLNUMMER] = bestellungDto.getCNr();
			oZeile[BestellungReportFac.REPORT_WEP_ETIKETT_PROJEKTBEZ] = bestellungDto.getCBez();
			oZeile[BestellungReportFac.REPORT_WEP_ETIKETT_WARENVERKEHRSNUMMER] = sWarenverkehrsnummer;
			oZeile[BestellungReportFac.REPORT_WEP_ETIKETT_VERPACKUNGSEINHEIT] = iVerpackungseinheit;
			oZeile[BestellungReportFac.REPORT_WEP_ETIKETT_HANDMENGE] = bdHandmenge;
			oZeile[BestellungReportFac.REPORT_WEP_ETIKETT_LAGER] = lagerDto.getCNr();

			// PJ19859

			oZeile[BestellungReportFac.REPORT_WEP_ETIKETT_GESTEHUNGSPREIS] = getLagerFac()
					.getGemittelterGestehungspreisEinesLagers(artikelDto.getIId(), lagerDto.getIId(), theClientDto);

			ArtikellieferantDto alDto = getArtikelFac().getArtikelEinkaufspreis(artikelDto.getIId(),
					bestellungDto.getLieferantIIdBestelladresse(), BigDecimal.ONE, bestellungDto.getWaehrungCNr(),
					new Date(System.currentTimeMillis()), theClientDto);
			if (alDto != null) {
				oZeile[BestellungReportFac.REPORT_WEP_ETIKETT_LIEF1PREIS] = alDto.getLief1Preis();
			}

			oZeile[BestellungReportFac.REPORT_WEP_ETIKETT_LAGERORT] = sLagerort;
			oZeile[BestellungReportFac.REPORT_WEP_ETIKETT_URSPRUNGSLAND] = sUrsprungsland;
			oZeile[BestellungReportFac.REPORT_WEP_ETIKETT_GEWICHT] = dGewicht;
			oZeile[BestellungReportFac.REPORT_WEP_ETIKETT_KOMMENTAR] = sKommentar;
			if (artikellieferantDto != null) {
				oZeile[BestellungReportFac.REPORT_WEP_ETIKETT_LIEFERANTENARTIKELNUMMER] = artikellieferantDto
						.getCArtikelnrlieferant();
				oZeile[BestellungReportFac.REPORT_WEP_ETIKETT_LIEFERANTENARTIKELBEZ] = artikellieferantDto
						.getCBezbeilieferant();
			}
			oZeile[BestellungReportFac.REPORT_WEP_ETIKETT_IDENT] = artikelDto.getCNr();
			oZeile[BestellungReportFac.REPORT_WEP_ETIKETT_REFERENZNUMMER] = artikelDto.getCReferenznr();
			if (artikelDto.getArtikelsprDto() != null) {
				oZeile[BestellungReportFac.REPORT_WEP_ETIKETT_BEZ] = artikelDto.getArtikelsprDto().getCBez();
				oZeile[BestellungReportFac.REPORT_WEP_ETIKETT_ZBEZ] = artikelDto.getArtikelsprDto().getCZbez();
				oZeile[BestellungReportFac.REPORT_WEP_ETIKETT_ZBEZ2] = artikelDto.getArtikelsprDto().getCZbez2();
				oZeile[BestellungReportFac.REPORT_WEP_ETIKETT_KURZBEZEICHNUNG] = artikelDto.getArtikelsprDto()
						.getCKbez();
			}

			oZeile[BestellungReportFac.REPORT_WEP_ETIKETT_WE_REFERENZ] = getLagerFac()
					.getWareneingangsreferenzSubreport(LocaleFac.BELEGART_BESTELLUNG, wepDto.getIId(),
							wepDto.getSeriennrChargennrMitMenge(), false, theClientDto);

			if (lieferantDto != null) {
				oZeile[BestellungReportFac.REPORT_WEP_ETIKETT_LIEFERANTNAME] = lieferantDto.getPartnerDto()
						.formatFixName1Name2();
			} else {
				oZeile[BestellungReportFac.REPORT_WEP_ETIKETT_LIEFERANTNAME] = "";
			}
			HerstellerDto hersteller = null;
			if (artikelDto.getHerstellerIId() != null) {
				hersteller = getArtikelFac().herstellerFindByPrimaryKey(artikelDto.getHerstellerIId(), theClientDto);
			}
			if (hersteller != null) {
				oZeile[BestellungReportFac.REPORT_WEP_ETIKETT_HERSTELLER] = hersteller.getCNr();
				if (hersteller.getPartnerDto() != null) {
					oZeile[BestellungReportFac.REPORT_WEP_ETIKETT_HERSTELLERNAME] = hersteller.getPartnerDto()
							.formatAnrede();
				}
			} else {
				oZeile[BestellungReportFac.REPORT_WEP_ETIKETT_HERSTELLER] = "";
				oZeile[BestellungReportFac.REPORT_WEP_ETIKETT_HERSTELLERNAME] = "";
			}

			// PJ18618
			if (wepDto.getSeriennrChargennrMitMenge() != null) {
				Object[][] oSubData = new Object[wepDto.getSeriennrChargennrMitMenge().size()][6];

				for (int j = 0; j < wepDto.getSeriennrChargennrMitMenge().size(); j++) {

					oSubData[j][0] = wepDto.getSeriennrChargennrMitMenge().get(j).getCSeriennrChargennr();
					oSubData[j][1] = wepDto.getSeriennrChargennrMitMenge().get(j).getNMenge();
					oSubData[j][2] = wepDto.getSeriennrChargennrMitMenge().get(j).getCVersion();

					if (wepDto.getSeriennrChargennrMitMenge().get(j).getGebindeIId() != null) {

						oSubData[j][3] = getArtikelFac()
								.gebindeFindByPrimaryKey(wepDto.getSeriennrChargennrMitMenge().get(j).getGebindeIId())
								.getCBez();
					}

					oSubData[j][4] = wepDto.getSeriennrChargennrMitMenge().get(j).getNGebindemenge();
					oSubData[j][5] = wepDto.getSeriennrChargennrMitMenge().get(j).getTBuchungszeit();
				}
				String[] fieldnames = new String[] { "F_SERIENCHARGENNR", "F_MENGE", "F_VERSION", "F_GEBINDE",
						"F_GEBINDEMENGE", "F_BUCHUNGSZEIT" };

				oZeile[BestellungReportFac.REPORT_WEP_ETIKETT_SUBREPORT_SNRCHNR] = new LPDatenSubreport(oSubData,
						fieldnames);

			}

			oZeile[REPORT_WEP_ETIKETT_EXEMPLAREGESAMT] = iExemplare;

			ArrayList alDaten = new ArrayList();

			for (int i = 0; i < iExemplare; i++) {

				Object[] oZeileKopie = oZeile.clone();
				oZeileKopie[REPORT_WEP_ETIKETT_EXEMPLAR] = new Integer(i + 1);

				alDaten.add(oZeileKopie);
			}

			// PJ22428
			if (bInklLosbuchungen) {

				ArrayList<WarenabgangsreferenzDto> refDtos = getLagerFac().getWarenausgangsreferenz(
						LocaleFac.BELEGART_BESTELLUNG, wepDto.getIId(), wepDto.getSeriennrChargennrMitMenge(),
						theClientDto);
				BigDecimal bdLosMenge = BigDecimal.ZERO;
				ArrayList alDatenNeu = new ArrayList();
				for (WarenabgangsreferenzDto refDto : refDtos) {

					if (refDto.getBelegart().equals(LocaleFac.BELEGART_LOS.trim())) {
						bdLosMenge = bdLosMenge.add(refDto.getMenge());
					}

				}

				for (int i = 0; i < alDaten.size(); i++) {
					Object[] oZeileZuKopieren = (Object[]) alDaten.get(i);

					oZeileZuKopieren[REPORT_WEP_ETIKETT_LAGERMENGE] = ((BigDecimal) oZeileZuKopieren[REPORT_WEP_ETIKETT_ANLIEFERMENGE])
							.subtract(bdLosMenge);

					alDatenNeu.add(oZeileZuKopieren);

					for (WarenabgangsreferenzDto refDto : refDtos) {

						Object[] oZeileLos = oZeileZuKopieren.clone();
						oZeileLos[REPORT_WEP_ETIKETT_LAGERMENGE] = null;
						if (refDto.getBelegart().equals(LocaleFac.BELEGART_LOS.trim())) {

							LosDto losDto = getFertigungFac().losFindByPrimaryKeyOhneExc(refDto.getBelegartId());

							if (losDto != null) {

								oZeileLos[REPORT_WEP_ETIKETT_LOS_MENGE] = refDto.getMenge();
								oZeileLos[REPORT_WEP_ETIKETT_LOS_NUMMER] = losDto.getCNr();
								oZeileLos[REPORT_WEP_ETIKETT_LOS_PROJEKT] = losDto.getCProjekt();

								if (losDto.getStuecklisteIId() != null) {
									StuecklisteDto stuecklisteDto = getStuecklisteFac()
											.stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(), theClientDto);
									oZeileLos[REPORT_WEP_ETIKETT_LOS_STUECKLISTE] = stuecklisteDto.getArtikelDto()
											.getCNr();
									oZeileLos[REPORT_WEP_ETIKETT_LOS_STUECKLISTE_BEZEICHNUNG] = stuecklisteDto
											.getArtikelDto().formatBezeichnung();
								}

								alDatenNeu.add(oZeileLos);
							}
						}
					}

				}
				alDaten = alDatenNeu;
			}

			Object[][] returnArray = new Object[alDaten.size()][REPORT_WEP_ETIKETT_ANZAHL_SPALTEN];
			data = (Object[][]) alDaten.toArray(returnArray);

			Map<String, Object> parameter = new TreeMap<String, Object>();

			parameter.put("P_MIT_LOSBUCHUNGEN", bInklLosbuchungen);

			initJRDS(parameter, BestellungReportFac.REPORT_MODUL, BestellungReportFac.REPORT_WEP_ETIKETT,
					theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
			return getReportPrint();
		}

		catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, ex);
		}

	}

	public JasperPrintLP printBestelletiketten(ArrayList<Integer> bestellpositionIIds, TheClientDto theClientDto) {
		this.useCase = UC_BESTELLETIKETT;
		try {

			BestellungDto bestellungDto = null;
			LagerDto lagerDto = null;
			LieferantDto lieferantDto = null;

			ArrayList alDaten = new ArrayList();

			for (int i = 0; i < bestellpositionIIds.size(); i++) {

				BestellpositionDto bespDto = getBestellpositionFac()
						.bestellpositionFindByPrimaryKey(bestellpositionIIds.get(i));
				if (bestellungDto == null) {
					bestellungDto = getBestellungFac().bestellungFindByPrimaryKey(bespDto.getBestellungIId());
					lieferantDto = getLieferantFac()
							.lieferantFindByPrimaryKey(bestellungDto.getLieferantIIdBestelladresse(), theClientDto);

					lagerDto = getLagerFac().lagerFindByPrimaryKey(lieferantDto.getLagerIIdZubuchungslager());
				}

				ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(bespDto.getArtikelIId(), theClientDto);
				ArtikellieferantDto artikellieferantDto = getArtikelFac().getArtikelEinkaufspreis(artikelDto.getIId(),
						bestellungDto.getLieferantIIdBestelladresse(), bespDto.getNMenge(),

						bestellungDto.getWaehrungCNr(), new java.sql.Date(bestellungDto.getDBelegdatum().getTime()),
						theClientDto);

				Object[] oZeile = new Object[REPORT_BESTELLETIKETTEN_ANZAHL_SPALTEN];

				oZeile[BestellungReportFac.REPORT_BESTELLETIKETTEN_BESTELLMENGE] = bespDto.getNMenge();
				oZeile[BestellungReportFac.REPORT_BESTELLETIKETTEN_OFFENEMENGE] = bespDto.getNOffeneMenge();
				oZeile[BestellungReportFac.REPORT_BESTELLETIKETTEN_GELIEFERTEMENGE] = bespDto.getNMenge()
						.subtract(bespDto.getNOffeneMenge());
				oZeile[BestellungReportFac.REPORT_BESTELLETIKETTEN_EINHEIT] = bespDto.getEinheitCNr().trim();

				oZeile[BestellungReportFac.REPORT_BESTELLETIKETTEN_BESTELLNUMMER] = bestellungDto.getCNr();
				oZeile[BestellungReportFac.REPORT_BESTELLETIKETTEN_PROJEKTBEZ] = bestellungDto.getCBez();
				oZeile[BestellungReportFac.REPORT_BESTELLETIKETTEN_WARENVERKEHRSNUMMER] = artikelDto
						.getCWarenverkehrsnummer();

				oZeile[BestellungReportFac.REPORT_BESTELLETIKETTEN_GEWICHT] = artikelDto.getFGewichtkg();

				if (artikelDto.getLandIIdUrsprungsland() != null) {
					LandDto landDto = getSystemFac().landFindByPrimaryKey(artikelDto.getLandIIdUrsprungsland());
					oZeile[BestellungReportFac.REPORT_BESTELLETIKETTEN_URSPRUNGSLAND] = landDto.getCName();
				}

				ArtikellagerplaetzeDto artikellagerplaetzeDto = getLagerFac()
						.artikellagerplaetzeFindByArtikelIIdLagerIId(artikelDto.getIId(), lagerDto.getIId());

				if (artikellagerplaetzeDto != null) {
					oZeile[BestellungReportFac.REPORT_BESTELLETIKETTEN_LAGERPLATZ] = artikellagerplaetzeDto
							.getLagerplatzDto().getCLagerplatz();
				}

				// PJ19859

				oZeile[BestellungReportFac.REPORT_BESTELLETIKETTEN_GESTEHUNGSPREIS] = getLagerFac()
						.getGemittelterGestehungspreisEinesLagers(artikelDto.getIId(), lagerDto.getIId(), theClientDto);

				ArtikellieferantDto alDto = getArtikelFac().getArtikelEinkaufspreis(artikelDto.getIId(),
						bestellungDto.getLieferantIIdBestelladresse(), BigDecimal.ONE, bestellungDto.getWaehrungCNr(),
						new Date(System.currentTimeMillis()), theClientDto);

				BigDecimal bdVerpackungseinheit = null;

				if (alDto != null) {
					oZeile[BestellungReportFac.REPORT_BESTELLETIKETTEN_LIEF1PREIS] = alDto.getLief1Preis();

					oZeile[BestellungReportFac.REPORT_BESTELLETIKETTEN_VERPACKUNGSEINHEIT] = alDto
							.getNVerpackungseinheit();
					bdVerpackungseinheit = alDto.getNVerpackungseinheit();

				}

				if (artikellieferantDto != null) {
					oZeile[BestellungReportFac.REPORT_BESTELLETIKETTEN_LIEFERANTENARTIKELNUMMER] = artikellieferantDto
							.getCArtikelnrlieferant();
					oZeile[BestellungReportFac.REPORT_BESTELLETIKETTEN_LIEFERANTENARTIKELBEZ] = artikellieferantDto
							.getCBezbeilieferant();
				}
				oZeile[BestellungReportFac.REPORT_BESTELLETIKETTEN_IDENT] = artikelDto.getCNr();
				oZeile[BestellungReportFac.REPORT_BESTELLETIKETTEN_REFERENZNUMMER] = artikelDto.getCReferenznr();
				if (artikelDto.getArtikelsprDto() != null) {
					oZeile[BestellungReportFac.REPORT_BESTELLETIKETTEN_BEZ] = artikelDto.getArtikelsprDto().getCBez();
					oZeile[BestellungReportFac.REPORT_BESTELLETIKETTEN_ZBEZ] = artikelDto.getArtikelsprDto().getCZbez();
					oZeile[BestellungReportFac.REPORT_BESTELLETIKETTEN_ZBEZ2] = artikelDto.getArtikelsprDto()
							.getCZbez2();
					oZeile[BestellungReportFac.REPORT_BESTELLETIKETTEN_KURZBEZEICHNUNG] = artikelDto.getArtikelsprDto()
							.getCKbez();
				}

				if (lieferantDto != null) {
					oZeile[BestellungReportFac.REPORT_BESTELLETIKETTEN_LIEFERANTNAME] = lieferantDto.getPartnerDto()
							.formatFixName1Name2();
				} else {
					oZeile[BestellungReportFac.REPORT_BESTELLETIKETTEN_LIEFERANTNAME] = "";
				}
				HerstellerDto hersteller = null;
				if (artikelDto.getHerstellerIId() != null) {
					hersteller = getArtikelFac().herstellerFindByPrimaryKey(artikelDto.getHerstellerIId(),
							theClientDto);
				}
				if (hersteller != null) {
					oZeile[BestellungReportFac.REPORT_BESTELLETIKETTEN_HERSTELLER] = hersteller.getCNr();
					if (hersteller.getPartnerDto() != null) {
						oZeile[BestellungReportFac.REPORT_BESTELLETIKETTEN_HERSTELLERNAME] = hersteller.getPartnerDto()
								.formatAnrede();
					}
				} else {
					oZeile[BestellungReportFac.REPORT_BESTELLETIKETTEN_HERSTELLER] = "";
					oZeile[BestellungReportFac.REPORT_BESTELLETIKETTEN_HERSTELLERNAME] = "";
				}

				if (bdVerpackungseinheit == null) {
					Object[] oZeileKopie = oZeile.clone();
					oZeileKopie[REPORT_BESTELLETIKETTEN_EXEMPLAR] = new Integer(1);
					oZeileKopie[REPORT_BESTELLETIKETTEN_EXEMPLAREGESAMT] = new Integer(1);
					oZeileKopie[REPORT_BESTELLETIKETTEN_PAKETMENGE] = bespDto.getNOffeneMenge();
					alDaten.add(oZeileKopie);
				} else {

					if (bdVerpackungseinheit.doubleValue() == 0) {
						bdVerpackungseinheit = bespDto.getNOffeneMenge();
					}

					BigDecimal bdAnliefermenge = bespDto.getNOffeneMenge();

					BigDecimal bdExemplareGesamt = bdAnliefermenge.divide(bdVerpackungseinheit, 12,
							BigDecimal.ROUND_HALF_EVEN);

					double dGesamt = Math.ceil(bdExemplareGesamt.doubleValue());
					int iExemplar = 1;
					while (bdAnliefermenge.doubleValue() > 0) {
						Object[] oZeileKopie = oZeile.clone();
						oZeileKopie[REPORT_BESTELLETIKETTEN_EXEMPLAR] = iExemplar;
						oZeileKopie[REPORT_BESTELLETIKETTEN_EXEMPLAREGESAMT] = (int) dGesamt;

						if (bdAnliefermenge.doubleValue() < bdVerpackungseinheit.doubleValue()) {

							oZeileKopie[REPORT_BESTELLETIKETTEN_PAKETMENGE] = bdAnliefermenge;
						} else {
							oZeileKopie[REPORT_BESTELLETIKETTEN_PAKETMENGE] = bdVerpackungseinheit;

						}

						bdAnliefermenge = bdAnliefermenge.subtract(bdVerpackungseinheit);
						iExemplar++;

						alDaten.add(oZeileKopie);
					}

				}

			}

			Object[][] returnArray = new Object[alDaten.size()][REPORT_BESTELLETIKETTEN_ANZAHL_SPALTEN];
			data = (Object[][]) alDaten.toArray(returnArray);

			Map<String, Object> parameter = new TreeMap<String, Object>();
			initJRDS(parameter, BestellungReportFac.REPORT_MODUL, BestellungReportFac.REPORT_BESTELLETIKETTEN,
					theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
			return getReportPrint();
		}

		catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, ex);
		}

	}

	public JasperPrintLP printWE_Etiketten(Integer wareneingangIId, TheClientDto theClientDto) {
		this.useCase = UC_WE_ETIKETTEN;
		try {

			LagerDto lagerDto = null;

			WareneingangDto weDto = getWareneingangFac().wareneingangFindByPrimaryKey(wareneingangIId);

			BestellungDto bestellungDto = getBestellungFac().bestellungFindByPrimaryKey(weDto.getBestellungIId());
			lagerDto = getLagerFac().lagerFindByPrimaryKey(weDto.getLagerIId());

			WareneingangspositionDto[] wepDtos = getWareneingangFac()
					.wareneingangspositionFindByWareneingangIId(wareneingangIId);
			ArrayList alDaten = new ArrayList();

			for (int i = 0; i < wepDtos.length; i++) {
				WareneingangspositionDto wepDto = wepDtos[i];

				BestellpositionDto bespDto = getBestellpositionFac()
						.bestellpositionFindByPrimaryKey(wepDto.getBestellpositionIId());

				ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(bespDto.getArtikelIId(), theClientDto);
				ArtikellieferantDto artikellieferantDto = getArtikelFac().getArtikelEinkaufspreis(artikelDto.getIId(),
						bestellungDto.getLieferantIIdBestelladresse(), bespDto.getNMenge(),

						bestellungDto.getWaehrungCNr(), new java.sql.Date(bestellungDto.getDBelegdatum().getTime()),
						theClientDto);

				LieferantDto lieferantDto = getLieferantFac()
						.lieferantFindByPrimaryKey(bestellungDto.getLieferantIIdBestelladresse(), theClientDto);

				Object[] oZeile = new Object[REPORT_WE_ETIKETTEN_ANZAHL_SPALTEN];

				if (wepDto != null) {
					oZeile[BestellungReportFac.REPORT_WE_ETIKETTEN_ANLIEFERMENGE] = wepDto.getNGeliefertemenge();
					oZeile[BestellungReportFac.REPORT_WE_ETIKETTEN_EINHEIT] = bespDto.getEinheitCNr().trim();
					if (Helper.short2boolean(artikelDto.getBChargennrtragend())) {
						oZeile[BestellungReportFac.REPORT_WE_ETIKETTEN_CHARGENNUMMER] = SeriennrChargennrMitMengeDto
								.erstelleStringAusMehrerenSeriennummern(wepDto.getSeriennrChargennrMitMenge());
					}
					if (Helper.short2boolean(artikelDto.getBSeriennrtragend())) {
						oZeile[BestellungReportFac.REPORT_WE_ETIKETTEN_SERIENNUMMER] = SeriennrChargennrMitMengeDto
								.erstelleStringAusMehrerenSeriennummern(wepDto.getSeriennrChargennrMitMenge());
					}

					oZeile[BestellungReportFac.REPORT_WE_ETIKETTEN_EINSTANDSPREIS] = wepDto.getNEinstandspreis();

				}
				if (weDto != null) {
					oZeile[BestellungReportFac.REPORT_WE_ETIKETTEN_WE_DATUM] = Helper
							.formatDatum(Helper.extractDate(weDto.getTWareneingangsdatum()), theClientDto.getLocUi());
					oZeile[BestellungReportFac.REPORT_WE_ETIKETTEN_WE_LIEFERSCHEINNUMMER] = weDto.getCLieferscheinnr();
				}
				oZeile[BestellungReportFac.REPORT_WE_ETIKETTEN_BESTELLNUMMER] = bestellungDto.getCNr();
				oZeile[BestellungReportFac.REPORT_WE_ETIKETTEN_PROJEKTBEZ] = bestellungDto.getCBez();
				oZeile[BestellungReportFac.REPORT_WE_ETIKETTEN_WARENVERKEHRSNUMMER] = artikelDto
						.getCWarenverkehrsnummer();

				oZeile[BestellungReportFac.REPORT_WE_ETIKETTEN_LAGER] = lagerDto.getCNr();

				oZeile[BestellungReportFac.REPORT_WE_ETIKETTEN_GEWICHT] = artikelDto.getFGewichtkg();

				if (artikelDto.getLandIIdUrsprungsland() != null) {
					LandDto landDto = getSystemFac().landFindByPrimaryKey(artikelDto.getLandIIdUrsprungsland());
					oZeile[BestellungReportFac.REPORT_WE_ETIKETTEN_URSPRUNGSLAND] = landDto.getCName();
				}

				ArtikellagerplaetzeDto artikellagerplaetzeDto = getLagerFac()
						.artikellagerplaetzeFindByArtikelIIdLagerIId(artikelDto.getIId(), weDto.getLagerIId());

				if (artikellagerplaetzeDto != null) {
					oZeile[BestellungReportFac.REPORT_WE_ETIKETTEN_LAGERPLATZ] = artikellagerplaetzeDto
							.getLagerplatzDto().getCLagerplatz();
				}

				// PJ19859

				oZeile[BestellungReportFac.REPORT_WE_ETIKETTEN_GESTEHUNGSPREIS] = getLagerFac()
						.getGemittelterGestehungspreisEinesLagers(artikelDto.getIId(), lagerDto.getIId(), theClientDto);

				ArtikellieferantDto alDto = getArtikelFac().getArtikelEinkaufspreis(artikelDto.getIId(),
						bestellungDto.getLieferantIIdBestelladresse(), BigDecimal.ONE, bestellungDto.getWaehrungCNr(),
						new Date(System.currentTimeMillis()), theClientDto);

				BigDecimal bdVerpackungseinheit = null;

				if (alDto != null) {
					oZeile[BestellungReportFac.REPORT_WE_ETIKETTEN_LIEF1PREIS] = alDto.getLief1Preis();

					oZeile[BestellungReportFac.REPORT_WE_ETIKETTEN_VERPACKUNGSEINHEIT] = alDto.getNVerpackungseinheit();
					bdVerpackungseinheit = alDto.getNVerpackungseinheit();

				}

				if (artikellieferantDto != null) {
					oZeile[BestellungReportFac.REPORT_WE_ETIKETTEN_LIEFERANTENARTIKELNUMMER] = artikellieferantDto
							.getCArtikelnrlieferant();
					oZeile[BestellungReportFac.REPORT_WE_ETIKETTEN_LIEFERANTENARTIKELBEZ] = artikellieferantDto
							.getCBezbeilieferant();
				}
				oZeile[BestellungReportFac.REPORT_WE_ETIKETTEN_IDENT] = artikelDto.getCNr();
				oZeile[BestellungReportFac.REPORT_WE_ETIKETTEN_REFERENZNUMMER] = artikelDto.getCReferenznr();
				if (artikelDto.getArtikelsprDto() != null) {
					oZeile[BestellungReportFac.REPORT_WE_ETIKETTEN_BEZ] = artikelDto.getArtikelsprDto().getCBez();
					oZeile[BestellungReportFac.REPORT_WE_ETIKETTEN_ZBEZ] = artikelDto.getArtikelsprDto().getCZbez();
					oZeile[BestellungReportFac.REPORT_WE_ETIKETTEN_ZBEZ2] = artikelDto.getArtikelsprDto().getCZbez2();
					oZeile[BestellungReportFac.REPORT_WE_ETIKETTEN_KURZBEZEICHNUNG] = artikelDto.getArtikelsprDto()
							.getCKbez();
				}

				oZeile[BestellungReportFac.REPORT_WE_ETIKETTEN_WE_REFERENZ] = getLagerFac()
						.getWareneingangsreferenzSubreport(LocaleFac.BELEGART_BESTELLUNG, wepDto.getIId(),
								wepDto.getSeriennrChargennrMitMenge(), false, theClientDto);

				if (lieferantDto != null) {
					oZeile[BestellungReportFac.REPORT_WE_ETIKETTEN_LIEFERANTNAME] = lieferantDto.getPartnerDto()
							.formatFixName1Name2();
				} else {
					oZeile[BestellungReportFac.REPORT_WE_ETIKETTEN_LIEFERANTNAME] = "";
				}
				HerstellerDto hersteller = null;
				if (artikelDto.getHerstellerIId() != null) {
					hersteller = getArtikelFac().herstellerFindByPrimaryKey(artikelDto.getHerstellerIId(),
							theClientDto);
				}
				if (hersteller != null) {
					oZeile[BestellungReportFac.REPORT_WE_ETIKETTEN_HERSTELLER] = hersteller.getCNr();
					if (hersteller.getPartnerDto() != null) {
						oZeile[BestellungReportFac.REPORT_WE_ETIKETTEN_HERSTELLERNAME] = hersteller.getPartnerDto()
								.formatAnrede();
					}
				} else {
					oZeile[BestellungReportFac.REPORT_WE_ETIKETTEN_HERSTELLER] = "";
					oZeile[BestellungReportFac.REPORT_WE_ETIKETTEN_HERSTELLERNAME] = "";
				}

				// PJ18618
				if (wepDto.getSeriennrChargennrMitMenge() != null) {
					Object[][] oSubData = new Object[wepDto.getSeriennrChargennrMitMenge().size()][6];

					for (int j = 0; j < wepDto.getSeriennrChargennrMitMenge().size(); j++) {

						oSubData[j][0] = wepDto.getSeriennrChargennrMitMenge().get(j).getCSeriennrChargennr();
						oSubData[j][1] = wepDto.getSeriennrChargennrMitMenge().get(j).getNMenge();
						oSubData[j][2] = wepDto.getSeriennrChargennrMitMenge().get(j).getCVersion();

						if (wepDto.getSeriennrChargennrMitMenge().get(j).getGebindeIId() != null) {

							oSubData[j][3] = getArtikelFac().gebindeFindByPrimaryKey(
									wepDto.getSeriennrChargennrMitMenge().get(j).getGebindeIId()).getCBez();
						}

						oSubData[j][4] = wepDto.getSeriennrChargennrMitMenge().get(j).getNGebindemenge();
						oSubData[j][5] = wepDto.getSeriennrChargennrMitMenge().get(j).getTBuchungszeit();
					}
					String[] fieldnames = new String[] { "F_SERIENCHARGENNR", "F_MENGE", "F_VERSION", "F_GEBINDE",
							"F_GEBINDEMENGE", "F_BUCHUNGSZEIT" };

					oZeile[BestellungReportFac.REPORT_WE_ETIKETTEN_SUBREPORT_SNRCHNR] = new LPDatenSubreport(oSubData,
							fieldnames);

				}

				if (bdVerpackungseinheit == null) {
					Object[] oZeileKopie = oZeile.clone();
					oZeileKopie[REPORT_WE_ETIKETTEN_EXEMPLAR] = new Integer(1);
					oZeileKopie[REPORT_WE_ETIKETTEN_EXEMPLAREGESAMT] = new Integer(1);
					oZeileKopie[REPORT_WE_ETIKETTEN_PAKETMENGE] = wepDto.getNGeliefertemenge();
					alDaten.add(oZeileKopie);
				} else {

					if (bdVerpackungseinheit.doubleValue() == 0) {
						bdVerpackungseinheit = wepDto.getNGeliefertemenge();
					}

					BigDecimal bdAnliefermenge = wepDto.getNGeliefertemenge();

					BigDecimal bdExemplareGesamt = bdAnliefermenge.divide(bdVerpackungseinheit, 12,
							BigDecimal.ROUND_HALF_EVEN);

					double dGesamt = Math.ceil(bdExemplareGesamt.doubleValue());
					int iExemplar = 1;
					while (bdAnliefermenge.doubleValue() > 0) {
						Object[] oZeileKopie = oZeile.clone();
						oZeileKopie[REPORT_WE_ETIKETTEN_EXEMPLAR] = iExemplar;
						oZeileKopie[REPORT_WE_ETIKETTEN_EXEMPLAREGESAMT] = (int) dGesamt;

						if (bdAnliefermenge.doubleValue() < bdVerpackungseinheit.doubleValue()) {

							oZeileKopie[REPORT_WE_ETIKETTEN_PAKETMENGE] = bdAnliefermenge;
						} else {
							oZeileKopie[REPORT_WE_ETIKETTEN_PAKETMENGE] = bdVerpackungseinheit;

						}

						bdAnliefermenge = bdAnliefermenge.subtract(bdVerpackungseinheit);
						iExemplar++;

						alDaten.add(oZeileKopie);
					}

				}

			}

			Object[][] returnArray = new Object[alDaten.size()][REPORT_WE_ETIKETTEN_ANZAHL_SPALTEN];
			data = (Object[][]) alDaten.toArray(returnArray);

			Map<String, Object> parameter = new TreeMap<String, Object>();
			initJRDS(parameter, BestellungReportFac.REPORT_MODUL, BestellungReportFac.REPORT_WE_ETIKETTEN,
					theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
			return getReportPrint();
		}

		catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, ex);
		}

	}

	public ArrayList<JasperPrintLP> printBSSammelMahnung(Integer bsmahnlaufIId, Integer lieferantIId,
			TheClientDto theClientDto, boolean bNurNichtGemahnte, boolean bMitLogo)
			throws EJBExceptionLP, RemoteException {

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

			BSMahnungDto[] bsmahnungen = getBSMahnwesenFac().bsmahnungFindByBSMahnlaufIIdLieferantIId(bsmahnlaufIId,
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
				ParametermandantDto parameter = (ParametermandantDto) getParameterFac().getMandantparameter(
						theClientDto.getMandant(), ParameterFac.KATEGORIE_BESTELLUNG,
						ParameterFac.PARAMETER_MAHNUNGSABSENDER);

				int iWert = ((Integer) parameter.getCWertAsObject()).intValue();

				if (iWert == 1) {
					bAbsenderIstAnforderer = true;
				}

			} catch (RemoteException ex5) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex5);
			}

			HashMap hmSammelmahnungProAnsprechpartner = new HashMap();

			Integer ansprechpartnerIIdErsterAnsprechpartner = null;

			if (bsmahnungen != null && bsmahnungen.length > 0) {
				for (int i = 0; i < bsmahnungen.length; i++) {
					BestellungDto bestellungDto = null;
					bestellungDto = getBestellungFac().bestellungFindByPrimaryKey(bsmahnungen[i].getBestellungIId());

					AnsprechpartnerDto oAnsprechpartner = null;

					if (bestellungDto.getAnsprechpartnerIId() != null) {
						oAnsprechpartner = getAnsprechpartnerFac()
								.ansprechpartnerFindByPrimaryKey(bestellungDto.getAnsprechpartnerIId(), theClientDto);
					}

					if (oAnsprechpartner != null && i == 0) {
						ansprechpartnerIIdErsterAnsprechpartner = oAnsprechpartner.getIId();
					}

					BestellpositionDto besposDto;
					Integer lieferantIIdMahnung = bestellungDto.getLieferantIIdBestelladresse();

					besposDto = getBestellpositionFac()
							.bestellpositionFindByPrimaryKey(bsmahnungen[i].getBestellpositionIId());

					String key = bestellungDto.getAnsprechpartnerIId() + "";

					if (bAbsenderIstAnforderer == true) {
						if (bestellungDto.getPersonalIIdAnforderer() != null) {
							key += " " + bestellungDto.getPersonalIIdAnforderer();
						}
					}

					if (hmSammelmahnungProAnsprechpartner.containsKey(key)) {

						Collection<BSSammelmahnungDto> c = (Collection<BSSammelmahnungDto>) hmSammelmahnungProAnsprechpartner
								.get(key);
						BSSammelmahnungDto smDto = new BSSammelmahnungDto();
						BigDecimal bdOffeneMenge = getBestellpositionFac().berechneOffeneMenge(besposDto);
						smDto.setBdOffen(bdOffeneMenge);
						smDto.setAnsprechpartnerIId(bestellungDto.getAnsprechpartnerIId());
						smDto.setPersonalIIdAnforderer(bestellungDto.getPersonalIIdAnforderer());
						smDto.setsBestellnummern(bestellungDto.getCNr());
						smDto.setSAngebotsnummer(bestellungDto.getCLieferantenangebot());
						smDto.setBdWert(besposDto.getNNettoeinzelpreis().multiply(bdOffeneMenge));
						// BSMahnstufeDto bsmahnstufeDto = getBSMahnwesenFac().
						// bsmahnstufeFindByPrimaryKeyOhneExc(new
						// BSMahnstufePK(bsmahnungen[i].
						// getMahnstufeIId(), theClientDto.getMandant()));
						smDto.setDBelegdatum(bestellungDto.getDBelegdatum());
						/*
						 * Date dZieldatum = new Date(bestellungDto.getDBelegdatum() .getTime()); if
						 * (bestellungDto.getZahlungszielIId() != null) { ZahlungszielDto zzDto =
						 * getMandantFac() .zahlungszielFindByPrimaryKey(
						 * bestellungDto.getZahlungszielIId(), theClientDto); dZieldatum =
						 * Helper.addiereTageZuDatum(dZieldatum, zzDto
						 * .getAnzahlZieltageFuerNetto().intValue()); }
						 */
						smDto.setDFaelligkeitsdatum(/* dZieldatum */besposDto.getTUebersteuerterLiefertermin());
						smDto.setDABTermin(besposDto.getTAuftragsbestaetigungstermin());
						smDto.setIMahnstufe(bsmahnungen[i].getMahnstufeIId());
						smDto.setSRechnungsnummer(bestellungDto.getCNr());
						smDto.setSBestellpositionbezeichung(besposDto.getCBez());
						ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(besposDto.getArtikelIId(),
								theClientDto);
						smDto.setSIdentnummer(artikelDto.getCNr());

						ArtikellieferantDto artLiefDto = getArtikelFac().getArtikelEinkaufspreis(
								besposDto.getArtikelIId(), lieferantIIdMahnung, BigDecimal.ONE,
								bestellungDto.getWaehrungCNr(), bestellungDto.getDBelegdatum(), theClientDto);

						smDto.setSHerstellerIdentnummer(artikelDto.getCArtikelnrhersteller());
						if (artLiefDto != null) {
							smDto.setSHerstellerbezeichnung(artLiefDto.getCBezbeilieferant());
						} else {
							smDto.setSHerstellerbezeichnung(null);
						}
						if (artikelDto.getHerstellerIId() != null) {
							smDto.setSArtikelhersteller(getArtikelFac()
									.herstellerFindByPrimaryKey(artikelDto.getHerstellerIId(), theClientDto).getCNr());
						} else {
							smDto.setSArtikelhersteller("");
						}
						smDto.setSReferenznummer(artikelDto.getCReferenznr());
						smDto.setSArtikelbez(artikelDto.formatArtikelbezeichnung());
						c.add(smDto);

						hmSammelmahnungProAnsprechpartner.put(key, c);

					} else {
						Collection<BSSammelmahnungDto> c = new LinkedList<BSSammelmahnungDto>();
						BSSammelmahnungDto smDto = new BSSammelmahnungDto();
						BigDecimal bdOffeneMenge = getBestellpositionFac().berechneOffeneMenge(besposDto);
						smDto.setBdOffen(bdOffeneMenge);
						smDto.setsBestellnummern(bestellungDto.getCNr());
						smDto.setSAngebotsnummer(bestellungDto.getCLieferantenangebot());
						smDto.setBdWert(besposDto.getNNettoeinzelpreis().multiply(bdOffeneMenge));
						// BSMahnstufeDto bsmahnstufeDto = getBSMahnwesenFac().
						// bsmahnstufeFindByPrimaryKeyOhneExc(new
						// BSMahnstufePK(bsmahnungen[i].
						// getMahnstufeIId(), theClientDto.getMandant()));
						smDto.setDBelegdatum(bestellungDto.getDBelegdatum());
						smDto.setAnsprechpartnerIId(bestellungDto.getAnsprechpartnerIId());
						smDto.setPersonalIIdAnforderer(bestellungDto.getPersonalIIdAnforderer());
						smDto.setDFaelligkeitsdatum(/* dZieldatum */besposDto.getTUebersteuerterLiefertermin());
						smDto.setDABTermin(besposDto.getTAuftragsbestaetigungstermin());
						smDto.setIMahnstufe(bsmahnungen[i].getMahnstufeIId());
						smDto.setSRechnungsnummer(bestellungDto.getCNr());
						smDto.setSBestellpositionbezeichung(besposDto.getCBez());
						ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(besposDto.getArtikelIId(),
								theClientDto);
						smDto.setSIdentnummer(artikelDto.getCNr());
						ArtikellieferantDto artLiefDto = getArtikelFac().getArtikelEinkaufspreis(
								besposDto.getArtikelIId(), lieferantIIdMahnung, besposDto.getNMenge(),
								bestellungDto.getWaehrungCNr(), bestellungDto.getDBelegdatum(), theClientDto);
						smDto.setSHerstellerIdentnummer(artikelDto.getCArtikelnrhersteller());
						if (artLiefDto != null) {
							smDto.setSHerstellerbezeichnung(artLiefDto.getCBezbeilieferant());
						} else {
							smDto.setSHerstellerbezeichnung(null);
						}
						if (artikelDto.getHerstellerIId() != null) {
							smDto.setSArtikelhersteller(getArtikelFac()
									.herstellerFindByPrimaryKey(artikelDto.getHerstellerIId(), theClientDto).getCNr());
						} else {
							smDto.setSArtikelhersteller("");
						}
						smDto.setSReferenznummer(artikelDto.getCReferenznr());
						smDto.setSArtikelbez(artikelDto.formatArtikelbezeichnung());
						c.add(smDto);

						hmSammelmahnungProAnsprechpartner.put(key, c);

					}

					// }
				}

				for (Iterator iterA = hmSammelmahnungProAnsprechpartner.keySet().iterator(); iterA.hasNext();) {

					String key = (String) iterA.next();

					HashMap hmBestellnummern = new HashMap();

					Collection<BSSammelmahnungDto> c = (Collection<BSSammelmahnungDto>) hmSammelmahnungProAnsprechpartner
							.get(key);

					// SP8177
					// Die Max Mahnstufe sollte pro Druck sein
					Integer iMaxMahnstufe = null;

					for (Iterator<BSSammelmahnungDto> iter = c.iterator(); iter.hasNext();) {
						BSSammelmahnungDto item = (BSSammelmahnungDto) iter.next();
						if (iMaxMahnstufe == null) {
							iMaxMahnstufe = item.getIMahnstufe();
						} else {
							if (item.getIMahnstufe().intValue() > iMaxMahnstufe.intValue()) {
								iMaxMahnstufe = item.getIMahnstufe();
							}
						}

					}

					Integer anspIId = null;
					Integer personalIIdAnforderer = null;
					data = new Object[c.size()][REPORT_BSSAMMELMAHNUNG_SIZE];
					int i = 0;
					for (Iterator<BSSammelmahnungDto> iter = c.iterator(); iter.hasNext();) {
						BSSammelmahnungDto item = (BSSammelmahnungDto) iter.next();
						anspIId = item.getAnsprechpartnerIId();
						if (bAbsenderIstAnforderer == true) {
							personalIIdAnforderer = item.getPersonalIIdAnforderer();
						}
						data[i][REPORT_BSSAMMELMAHNUNG_OFFENEMENGE] = item.getBdOffen();
						data[i][REPORT_BSSAMMELMAHNUNG_WERT] = item.getBdWert();
						data[i][REPORT_BSSAMMELMAHNUNG_BESTELLPOSITION_BEZEICHNUNG] = item
								.getSBestellpositionbezeichung();
						data[i][REPORT_BSSAMMELMAHNUNG_BELEGDATUM] = new java.util.Date(
								item.getDBelegdatum().getTime());

						if (!hmBestellnummern.containsKey(item.getsBestellnummern())) {
							hmBestellnummern.put(item.getsBestellnummern(), item.getsBestellnummern());
						}

						data[i][REPORT_BSSAMMELMAHNUNG_LIEFERTERMIN] = new java.util.Date(
								item.getDFaelligkeitsdatum().getTime());
						if (item.getDABTermin() != null) {
							data[i][REPORT_BSSAMMELMAHNUNG_ABTERMIN] = new java.util.Date(
									item.getDABTermin().getTime());
						} else {
							data[i][REPORT_BSSAMMELMAHNUNG_ABTERMIN] = null;
						}
						data[i][REPORT_BSSAMMELMAHNUNG_MAHNSTUFE] = item.getIMahnstufe();
						data[i][REPORT_BSSAMMELMAHNUNG_BESTELLNUMMER] = item.getSRechnungsnummer();
						data[i][REPORT_BSSAMMELMAHNUNG_ANGEBOTSNUMMER] = item.getSAngebotsnummer();
						data[i][REPORT_BSSAMMELMAHNUNG_IDENTNUMMER] = item.getSIdentnummer();
						data[i][REPORT_BSSAMMELMAHNUNG_LIEFERANT_IDENTNUMMER] = item.getSHerstellerIdentnummer();
						data[i][REPORT_BSSAMMELMAHNUNG_LIEFERANT_ARTIKEL_BEZEICHNUNG] = item
								.getSHerstellerbezeichnung();
						data[i][REPORT_BSSAMMELMAHNUNG_ARTIKEL_HERSTELLER] = item.getSArtikelhersteller();
						data[i][REPORT_BSSAMMELMAHNUNG_REFERENZNUMMER] = item.getSReferenznummer();
						data[i][REPORT_BSSAMMELMAHNUNG_ARTIKELCZBEZ2] = item.getSArtikelbez();
						i++;
					}
					Map<String, Object> map = new TreeMap<String, Object>();
					LieferantDto lieferantDto = getLieferantFac().lieferantFindByPrimaryKey(lieferantIId, theClientDto);
					Locale locDruck = Helper.string2Locale(lieferantDto.getPartnerDto().getLocaleCNrKommunikation());
					MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(),
							theClientDto);

					if (anspIId != null) {
						AnsprechpartnerDto ansprechpartnerDto = getAnsprechpartnerFac()
								.ansprechpartnerFindByPrimaryKey(anspIId, theClientDto);

						map.put("P_LIEFERANT_ADRESSBLOCK", formatAdresseFuerAusdruck(lieferantDto.getPartnerDto(),
								ansprechpartnerDto, mandantDto, locDruck));
					} else {
						map.put("P_LIEFERANT_ADRESSBLOCK",
								formatAdresseFuerAusdruck(lieferantDto.getPartnerDto(), null, mandantDto, locDruck));
					}

					// belegkommunikation: 2 daten holen
					String sEmail = getPartnerFac().partnerkommFindRespectPartnerAsStringOhneExec(
							ansprechpartnerIIdErsterAnsprechpartner, lieferantDto.getPartnerDto(),
							PartnerFac.KOMMUNIKATIONSART_EMAIL, theClientDto.getMandant(), theClientDto);
					String sFax = getPartnerFac().partnerkommFindRespectPartnerAsStringOhneExec(
							ansprechpartnerIIdErsterAnsprechpartner, lieferantDto.getPartnerDto(),
							PartnerFac.KOMMUNIKATIONSART_FAX, theClientDto.getMandant(), theClientDto);
					String sTelefon = getPartnerFac().partnerkommFindRespectPartnerAsStringOhneExec(
							ansprechpartnerIIdErsterAnsprechpartner, lieferantDto.getPartnerDto(),
							PartnerFac.KOMMUNIKATIONSART_TELEFON, theClientDto.getMandant(), theClientDto);
					// belegkommunikation: 3 daten als parameter an den Report
					// weitergeben
					map.put(LPReport.P_ANSPRECHPARTNEREMAIL, sEmail != null ? sEmail : "");
					map.put(LPReport.P_ANSPRECHPARTNERFAX, sFax != null ? sFax : "");
					map.put(LPReport.P_ANSPRECHPARTNERTELEFON, sTelefon != null ? sTelefon : "");

					map.put("P_LIEFERANT_UID", lieferantDto.getPartnerDto().getCUid());
					PersonalDto oPersonalBenutzer = getPersonalFac()
							.personalFindByPrimaryKey(theClientDto.getIDPersonal(), theClientDto);
					map.put("P_UNSER_ZEICHEN", Helper.getKurzzeichenkombi(oPersonalBenutzer.getCKurzzeichen(),
							oPersonalBenutzer.getCKurzzeichen()));
					BSMahnlaufDto mahnlaufDto = getBSMahnwesenFac().bsmahnlaufFindByPrimaryKey(bsmahnlaufIId);
					map.put("P_BERUECKSICHTIGTBIS", new java.util.Date(mahnlaufDto.getTAnlegen().getTime()));
					map.put("P_DATUM", new java.util.Date(getDate().getTime()));

					BSMahntextDto bsmahntextDto = getBSMahnwesenFac().bsmahntextFindByMandantLocaleCNr(
							mandantDto.getCNr(), Helper.locale2String(locDruck), iMaxMahnstufe);

					if (bsmahntextDto != null) {
						map.put("P_MAHNTEXT", Helper.formatStyledTextForJasper(bsmahntextDto.getXTextinhalt()));
					} else {

						ArrayList al = new ArrayList();
						al.add(lieferantDto.getPartnerDto().formatAnrede() + " ("
								+ Helper.locale2String(locDruck).trim() + ")");
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_KEINE_MAHNTEXTE_EINGETRAGEN, al,
								new Exception("FEHLER_FINANZ_KEINE_MAHNTEXTE_EINGETRAGEN"));
					}

					BSMahnstufeDto bsmahnstufeDto = getBSMahnwesenFac().bsmahnstufeFindByPrimaryKeyOhneExc(
							new BsmahnstufePK(iMaxMahnstufe, theClientDto.getMandant()));
					map.put("P_MAXMAHNSTUFE", bsmahnstufeDto.getIId());
					map.put("P_MAXMAHNSTUFE_TAGE", bsmahnstufeDto.getITage());

					map.put("P_BERUECKSICHTIGTBIS", new java.sql.Date(System.currentTimeMillis()));
					map.put(LPReport.P_WAEHRUNG, mandantDto.getWaehrungCNr());
					map.put("P_ABSENDER", mandantDto.getPartnerDto().formatAnrede());
					map.put("P_BENUTZERNAME", oPersonalBenutzer.getPartnerDto().formatAnrede());
					map.put("Mandantenadresse", Helper.formatMandantAdresse(mandantDto));
					map.put("P_MITLOGO", bMitLogo);
					initJRDS(map, BestellungReportFac.REPORT_MODUL, BestellungReportFac.REPORT_BSSAMMELMAHNUNG,
							theClientDto.getMandant(), locDruck, theClientDto, bMitLogo, null);

					JasperPrintLP print = getReportPrint();
					print.putAdditionalInformation("ansprechpartnerIId", anspIId);
					print.putAdditionalInformation("personalIIdAnforderer", personalIIdAnforderer);
					print.putAdditionalInformation("lieferantIId", lieferantIId);
					print.putAdditionalInformation("bestellnummern", hmBestellnummern);

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
	 * @param reportJournalKriterienDtoI die Kriterien
	 * @param theClientDto               der aktuelle Benutzer
	 * @return String die Sortierkriterien
	 */
	private String buildSortierungBestellungOffene(ReportJournalKriterienDto reportJournalKriterienDtoI,
			TheClientDto theClientDto) {
		StringBuffer buff = new StringBuffer(
				getTextRespectUISpr("lp.sortierungnach", theClientDto.getMandant(), theClientDto.getLocUi()));
		buff.append(" ");

		if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_BELEGNUMMER) {
			buff.append(getTextRespectUISpr("bes.bestnr", theClientDto.getMandant(), theClientDto.getLocUi()));
		}
		if (reportJournalKriterienDtoI.bSortiereNachKostenstelle) {
			buff.append(getTextRespectUISpr("lp.kostenstelle", theClientDto.getMandant(), theClientDto.getLocUi()))
					.append(", ");
		}

		if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
			buff.append(getTextRespectUISpr("lp.lieferant", theClientDto.getMandant(), theClientDto.getLocUi()))
					.append(", ");
		}
		if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_IDENT) {
			buff.append(getTextRespectUISpr("bes.offene.lieferantartikel", theClientDto.getMandant(),
					theClientDto.getLocUi())).append(", ");
		}

		if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PROJEKT) {
			buff.append(getTextRespectUISpr("lp.projekt", theClientDto.getMandant(), theClientDto.getLocUi()));
		}
		if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PROJEKT) {
			buff.append(getTextRespectUISpr("lp.projekt", theClientDto.getMandant(), theClientDto.getLocUi()));
		}
		if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_ART) {
			buff.append(getTextRespectUISpr("bes.bestellungsart", theClientDto.getMandant(), theClientDto.getLocUi()));
		}
		return buff.toString();
	}

	private String buildSortierungBestellvorschlag(ReportJournalKriterienDto reportJournalKriterienDtoI,
			TheClientDto theClientDto) {
		StringBuffer buff = new StringBuffer(
				getTextRespectUISpr("lp.sortierungnach", theClientDto.getMandant(), theClientDto.getLocUi()));
		buff.append(" ");

		if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_BELEGNUMMER) {
			buff.append(getTextRespectUISpr("bes.bestnr", theClientDto.getMandant(), theClientDto.getLocUi()));
		}
		if (reportJournalKriterienDtoI.bSortiereNachKostenstelle) {
			buff.append(getTextRespectUISpr("lp.kostenstelle", theClientDto.getMandant(), theClientDto.getLocUi()))
					.append(", ");
		}

		if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
			buff.append(getTextRespectUISpr("lp.lieferant", theClientDto.getMandant(), theClientDto.getLocUi()))
					.append(", ");
		}
		if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_IDENT) {
			buff.append(getTextRespectUISpr("lp.artikel", theClientDto.getMandant(), theClientDto.getLocUi()))
					.append(", ");
		}

		if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PROJEKT) {
			buff.append(getTextRespectUISpr("lp.projekt", theClientDto.getMandant(), theClientDto.getLocUi()));
		}
		if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PROJEKT) {
			buff.append(getTextRespectUISpr("lp.projekt", theClientDto.getMandant(), theClientDto.getLocUi()));
		}
		if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_ART) {
			buff.append(getTextRespectUISpr("bes.bestellungsart", theClientDto.getMandant(), theClientDto.getLocUi()));
		}
		return buff.toString();
	}

	/**
	 * String zum Andrucken der Filterkriterien.
	 * 
	 * @param reportJournalKriterienDtoI die Kriterien
	 * @param artikelklasseIId           Integer
	 * @param artikelgruppeIId           Integer
	 * @param artikelCNrVon              String
	 * @param artikelCNrBis              String
	 * @param projektCBezeichnung        String
	 * @param theClientDto               der aktuelle Benutzer
	 * @return String die Filterkriterien
	 * @throws EJBExceptionLP Ausnahme
	 */
	private String buildFilterBestellungOffene(ReportJournalKriterienDto reportJournalKriterienDtoI,
			Integer artikelklasseIId, Integer artikelgruppeIId, String artikelCNrVon, String artikelCNrBis,
			String projektCBezeichnung, TheClientDto theClientDto)

			throws EJBExceptionLP {
		StringBuffer buff = new StringBuffer("");

		// Kostenstelle
		if (reportJournalKriterienDtoI.kostenstelleIId != null) {
			buff.append(" ")
					.append(getTextRespectUISpr("lp.kostenstelle", theClientDto.getMandant(), theClientDto.getLocUi()));
			buff.append(" ").append(
					getSystemFac().kostenstelleFindByPrimaryKey(reportJournalKriterienDtoI.kostenstelleIId).getCNr());
		}

		// Belegdatum
		if (reportJournalKriterienDtoI.dVon != null || reportJournalKriterienDtoI.dBis != null) {
			buff.append(getTextRespectUISpr("bes.belegdatum", theClientDto.getMandant(), theClientDto.getLocUi()));
		}

		// Lieferant
		if (reportJournalKriterienDtoI.lieferantIId != null) {
			buff.append(" ")
					.append(getTextRespectUISpr("lp.lieferant", theClientDto.getMandant(), theClientDto.getLocUi()));
			buff.append(" ")
					.append(getLieferantFac()
							.lieferantFindByPrimaryKey(reportJournalKriterienDtoI.lieferantIId, theClientDto)
							.getPartnerDto().getCName1nachnamefirmazeile1());
		}

		if (projektCBezeichnung != null) {
			buff.append(" ").append(
					getTextRespectUISpr("lp.projektbezeichnung", theClientDto.getMandant(), theClientDto.getLocUi()));
			buff.append(" ").append(projektCBezeichnung);
		}

		if (artikelklasseIId != null) {
			buff.append(" ").append(
					getTextRespectUISpr("lp.artikelklasse", theClientDto.getMandant(), theClientDto.getLocUi()));
			ArtklaDto artikelklasseDto = getArtikelFac().artklaFindByPrimaryKey(artikelklasseIId, theClientDto);
			if (artikelklasseDto.getArtklasprDto() != null) {
				buff.append(" ").append(artikelklasseDto.getArtklasprDto().getCBez());
			}
		}

		if (artikelgruppeIId != null) {
			buff.append(" ").append(
					getTextRespectUISpr("lp.artikelgruppe", theClientDto.getMandant(), theClientDto.getLocUi()));
			ArtgruDto artikelgruppeDto = getArtikelFac().artgruFindByPrimaryKey(artikelgruppeIId, theClientDto);
			if (artikelgruppeDto.getArtgrusprDto() != null) {
				buff.append(" ").append(artikelgruppeDto.getArtgrusprDto().getCBez());
			}
		}

		return buff.toString().trim();
	}

	/**
	 * String zum Andrucken der Filterkriterien.
	 * 
	 * @param reportJournalKriterienDtoI die Kriterien
	 * @param theClientDto               der aktuelle Benutzer
	 * @return String die Filterkriterien
	 * @throws EJBExceptionLP Ausnahme
	 */
	private String buildFilterBestellungOffene(ReportJournalKriterienDto reportJournalKriterienDtoI,

			TheClientDto theClientDto)

			throws EJBExceptionLP {
		StringBuffer buff = new StringBuffer("");
		// Belegdatum
		if (reportJournalKriterienDtoI.dVon != null || reportJournalKriterienDtoI.dBis != null) {
			buff.append(getTextRespectUISpr("bes.belegdatum", theClientDto.getMandant(), theClientDto.getLocUi()));
		}

		// Lieferant
		if (reportJournalKriterienDtoI.lieferantIId != null) {
			buff.append(" ")
					.append(getTextRespectUISpr("lp.lieferant", theClientDto.getMandant(), theClientDto.getLocUi()));
			buff.append(" ")
					.append(getLieferantFac()
							.lieferantFindByPrimaryKey(reportJournalKriterienDtoI.lieferantIId, theClientDto)
							.getPartnerDto().getCName1nachnamefirmazeile1());
		}

		return buff.toString().trim();
	}

	/**
	 * Alle offenen Bestellungen fuer einen bestimmten Mandanten drucken.
	 * 
	 * @param krit                        die Filter- und Sortierkriterien
	 * @param dStichtag                   Date
	 * @param bSortierungNachLiefertermin Boolean
	 * @param artikelklasseIId            Integer
	 * @param artikelgruppeIId            Integer
	 * @param artikelCNrVon               String
	 * @param artikelCNrBis               String
	 * @param projektCBezeichnung         String
	 * @param auftragIId                  Integer
	 * @param theClientDto                der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 * @return JasperPrint der Druck
	 * @throws RemoteException
	 */
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printBestellungOffene(ReportJournalKriterienDto krit, Date dStichtag,
			Boolean bSortierungNachLiefertermin, Integer artikelklasseIId, Integer artikelgruppeIId,
			String artikelCNrVon, String artikelCNrBis, String projektCBezeichnung, Integer auftragIId, Integer iArt,
			boolean bNurAngelegte, boolean bNurOffeneMengenAnfuehren, Integer[] projekte, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		useCase = UC_OFFENE;
		int iAnzahlZeilen = 0;

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		// vom Stichtag die Uhrzeit abschneiden
		dStichtag = Helper.cutDate(dStichtag);

		// die dem Report uebergeben
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("P_STICHTAG", Helper.formatDatum(dStichtag, theClientDto.getLocUi()));
		dStichtag = Helper.addiereTageZuDatum(dStichtag, 1);

		session = factory.openSession();

		boolean bProjektklammer = getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
				MandantFac.ZUSATZFUNKTION_PROJEKTKLAMMER, theClientDto.getMandant());
		// Hiberante Criteria fuer alle Tabellen ausgehend von meiner
		// Haupttabelle anlegen,
		// nach denen ich filtern und sortieren kann
		Criteria crit = session.createCriteria(FLRBestellung.class);

		// Einschraenkung auf den aktuellen Mandanten
		crit.add(Restrictions.eq(BestellungFac.FLR_BESTELLUNG_MANDANT_C_NR, theClientDto.getMandant()));

		if (bNurAngelegte == true) {
			crit.add(Restrictions.eq(BestellungFac.FLR_BESTELLUNG_BESTELLUNGSTATUS_C_NR,
					BestellungFac.BESTELLSTATUS_ANGELEGT));
		}

		crit.add(Restrictions.not(Restrictions.eq(BestellungFac.FLR_BESTELLUNG_BESTELLUNGSTATUS_C_NR,
				BestellungFac.BESTELLSTATUS_STORNIERT)));

		// SP5625 Das T_MANUELLGELIEFERT <=Stichtag bzw.
		// T_VOLLSTAENDIG_GELIEFERT <=Stichtag
		crit.add(Restrictions.le(BestellungFac.FLR_BESTELLUNG_T_BELEGDATUM, dStichtag));

		crit.add(Restrictions.or(Restrictions.ge(BestellungFac.FLR_BESTELLUNG_T_VOLLSTAENDIG_GELIEFERT, dStichtag),
				Restrictions.isNull(BestellungFac.FLR_BESTELLUNG_T_VOLLSTAENDIG_GELIEFERT)));

		crit.add(Restrictions.or(Restrictions.ge(BestellungFac.FLR_BESTELLUNG_T_MANUELLGELIEFERT, dStichtag),
				Restrictions.isNull(BestellungFac.FLR_BESTELLUNG_T_MANUELLGELIEFERT)));

		// Filter nach Projektbezeichnung
		if (projektCBezeichnung != null) {
			crit.add(Restrictions.ilike(BestellungFac.FLR_BESTELLUNG_C_BEZPROJEKTBEZEICHNUNG,
					"%" + projektCBezeichnung + "%"));
		}

		// Projektklammer
		if (projekte != null && projekte.length > 0) {
			crit.add(Restrictions.in(BestellungFac.FLR_BESTELLUNG_PROJEKT_I_ID, projekte));

			String text = "";
			for (int i = 0; i < projekte.length; i++) {

				try {
					ProjektDto pDto = getProjektFac().projektFindByPrimaryKey(projekte[i]);

					text += pDto.getCNr() + ", ";
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}

			parameter.put("P_PROJEKTE", text);

		}

		// Filter nach Auftrag
		if (auftragIId != null) {
			crit.add(Restrictions.eq(BestellungFac.FLR_BESTELLUNG_AUFTRAG_I_ID, auftragIId));
		}
		// Einschraenkung nach einer bestimmten Kostenstelle
		if (krit.kostenstelleIId != null) {
			crit.add(Restrictions.eq(BestellungFac.FLR_BESTELLUNG_KOSTENSTELLE_I_ID, krit.kostenstelleIId));
		}

		// Einschraenkung nach einem bestimmten Lieferanten
		if (krit.lieferantIId != null) {
			crit.add(Restrictions.eq(BestellungFac.FLR_BESTELLUNG_LIEFERANT_I_ID_BESTELLADRESSE, krit.lieferantIId));
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
				crit.add(Restrictions.in(BestellungFac.FLR_BESTELLUNG_BESTELLUNGART_C_NR, cArt));
			} else if (iArt == 2) {
				// Nur Rahmenbestellungen
				cArt = new LinkedList<String>();
				cArt.add(BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR);
				crit.add(Restrictions.in(BestellungFac.FLR_BESTELLUNG_BESTELLUNGART_C_NR, cArt));
			}
		}

		// Sortierung nach Kostenstelle ist immer die erste Sortierung
		if (krit.bSortiereNachKostenstelle) {
			crit.createCriteria(BestellungFac.FLR_BESTELLUNG_FLRKOSTENSTELLE).addOrder(Order.asc("c_nr"));
		}
		// Sortierung nach Kunde, eventuell innerhalb der Kostenstelle
		if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
			crit.createCriteria(BestellungFac.FLR_BESTELLUNG_FLRLIEFERANT).createCriteria(LieferantFac.FLR_PARTNER)
					.addOrder(Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));

		}
		// Sortierung nach Projekt, eventuell innerhalb der Kostenstelle
		else if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PROJEKT) {

			if (bProjektklammer == true) {

				crit.createAlias(BestellungFac.FLR_BESTELLUNG_FLRPROJEKT, "p");

				crit.addOrder(Order.asc("p.c_nr"));
			} else {
				crit.addOrder(Order.asc(BestellungFac.FLR_BESTELLUNG_C_BEZPROJEKTBEZEICHNUNG));
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
		if (bSortierungNachLiefertermin != null && bSortierungNachLiefertermin.booleanValue()) {
			crit.addOrder(Order.asc(BestellungFac.FLR_BESTELLUNG_T_LIEFERTERMIN));
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
			Criteria crit1Bestellung = crit1.createCriteria(BestellpositionFac.FLR_BESTELLPOSITION_FLRBESTELLUNG);
			// nur Positionen der aktuellen Bestellung.
			crit1Bestellung.add(Restrictions.eq(BestellungFac.FLR_BESTELLUNG_I_ID, flrbestellung.getI_id()));
			// keine erledigten Positionen.
			crit1.add(Restrictions.ne(BestellpositionFac.FLR_BESTELLPOSITION_BESTELLPOSITIONSTATUS_C_NR,
					BestellpositionFac.BESTELLPOSITIONSTATUS_ERLEDIGT));
			// keine geliferten Positionen.
			crit1.add(Restrictions.ne(BestellpositionFac.FLR_BESTELLPOSITION_BESTELLPOSITIONSTATUS_C_NR,
					BestellpositionFac.BESTELLPOSITIONSTATUS_GELIEFERT));
			// Der Liefertermin muss vor dem Stichtag liegen
			crit1.add(Restrictions.or(Restrictions.and(
					// Wenn der AB-Termin
					// eingegeben ist, zieht
					// der
					Restrictions.isNotNull(BestellpositionFac.FLR_BESTELLPOSITION_T_AUFTRAGSBESTAETIGUNGSTERMIN),
					Restrictions.le(BestellpositionFac.FLR_BESTELLPOSITION_T_AUFTRAGSBESTAETIGUNGSTERMIN, dStichtag)),
					Restrictions.and(
							// sonst der
							// uebersteuerte
							// Liefertermin
							Restrictions.isNull(BestellpositionFac.FLR_BESTELLPOSITION_T_AUFTRAGSBESTAETIGUNGSTERMIN),
							Restrictions.le(BestellpositionFac.FLR_BESTELLPOSITION_T_UEBERSTEUERTERLIEFERTERMIN,
									dStichtag))));

			if (artikelklasseIId != null || artikelgruppeIId != null || artikelCNrVon != null
					|| artikelCNrBis != null) {
				// Wenn nach Artikelklassen/Gruppen gefiltert wird, dann
				// kommen nur Ident-Positionen
				crit1.add(Restrictions.eq(BestellpositionFac.FLR_BESTELLPOSITION_BESTELLPOSITIONART_C_NR,
						BestellpositionFac.BESTELLPOSITIONART_IDENT));
				Criteria critArtikel = crit1.createCriteria(BestellpositionFac.FLR_BESTELLPOSITION_FLRARTIKEL);
				if (artikelklasseIId != null) {
					critArtikel.createCriteria(ArtikelFac.FLR_ARTIKEL_FLRARTIKELKLASSE)
							.add(Restrictions.eq("i_id", artikelklasseIId));
				}
				if (artikelgruppeIId != null) {
					critArtikel.createCriteria(ArtikelFac.FLR_ARTIKEL_FLRARTIKELGRUPPE)
							.add(Restrictions.eq("i_id", artikelgruppeIId));
				}
				if (artikelCNrVon != null) {
					critArtikel.add(Restrictions.ge(ArtikelFac.FLR_ARTIKEL_C_NR, artikelCNrVon));
				}
				if (artikelCNrBis != null) {
					critArtikel.add(Restrictions.le(ArtikelFac.FLR_ARTIKEL_C_NR, artikelCNrBis));
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
				data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_BESTELLUNGCNR] = flrbestellung.getC_nr();
				data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_STATUS] = flrbestellung.getBestellungstatus_c_nr();
				data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_BESTELLUNGARTCNR] = flrbestellung
						.getBestellungart_c_nr();

				if (bProjektklammer && flrbestellung.getProjekt_i_id() != null) {
					data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_PROJEKT] = flrbestellung.getFlrprojekt()
							.getC_nr();
				} else {
					data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_PROJEKT] = flrbestellung
							.getC_bezprojektbezeichnung();
				}

				data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_BESTELLUNGLIEFERANT] = flrbestellung
						.getFlrlieferant().getFlrpartner().getC_name1nachnamefirmazeile1();

				// PJ 14752

				String sortierstring = "";
				if (krit.bSortiereNachKostenstelle == true) {
					sortierstring = Helper.fitString2Length(flrbestellung.getFlrkostenstelle().getC_nr(), 80, ' ');
				}
				if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_IDENT) {
					sortierstring += Helper.fitString2Length(
							flrbestellung.getFlrlieferant().getFlrpartner().getC_name1nachnamefirmazeile1(), 80, ' ')
							+ Helper.fitString2Length("", 80, ' ');
				}
				if (bSortierungNachLiefertermin == true) {
					sortierstring += Helper.fitString2Length(flrbestellung.getT_liefertermin() + "", 15, ' ');
				}
				data[i][REPORT_BESTELLUNG_OFFENE_SORTIERKRITERIUM] = sortierstring;

				data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_BESTELLUNGDATUM] = Helper
						.formatDatum(flrbestellung.getT_belegdatum(), theClientDto.getLocUi());
				if (flrbestellung.getFlrkostenstelle() != null) {
					data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_KOSTENSTELLECNR] = flrbestellung
							.getFlrkostenstelle().getC_nr();
				}
				i++;
			}
			session = factory.openSession();
			Criteria crit1 = session.createCriteria(FLRBestellpositionReport.class);
			// Keine erledigten Positionen.
			crit1.add(Restrictions.ne(BestellpositionFac.FLR_BESTELLPOSITION_BESTELLPOSITIONSTATUS_C_NR,
					BestellpositionFac.BESTELLPOSITIONSTATUS_ERLEDIGT));
			crit1.add(Restrictions.ne(BestellpositionFac.FLR_BESTELLPOSITION_BESTELLPOSITIONSTATUS_C_NR,
					BestellpositionFac.BESTELLPOSITIONSTATUS_GELIEFERT));

			// Der Liefertermin muss vor dem Stichtag liegen
			crit1.add(Restrictions.or(Restrictions.and(
					// Wenn der AB-Termin
					// eingegeben ist, zieht
					// der
					Restrictions.isNotNull(BestellpositionFac.FLR_BESTELLPOSITION_T_AUFTRAGSBESTAETIGUNGSTERMIN),
					Restrictions.le(BestellpositionFac.FLR_BESTELLPOSITION_T_AUFTRAGSBESTAETIGUNGSTERMIN, dStichtag)),
					Restrictions.and(
							// sonst der
							// uebersteuerte
							// Liefertermin
							Restrictions.isNull(BestellpositionFac.FLR_BESTELLPOSITION_T_AUFTRAGSBESTAETIGUNGSTERMIN),
							Restrictions.le(BestellpositionFac.FLR_BESTELLPOSITION_T_UEBERSTEUERTERLIEFERTERMIN,
									dStichtag))));

			// Nur Positionen der aktuellen Bestellung.
			Criteria crit1Bestellung = crit1.createCriteria(BestellpositionFac.FLR_BESTELLPOSITION_FLRBESTELLUNG);
			crit1Bestellung.add(Restrictions.eq(BestellungFac.FLR_BESTELLUNG_I_ID, flrbestellung.getI_id()));
			if (artikelklasseIId != null || artikelgruppeIId != null || artikelCNrVon != null
					|| artikelCNrBis != null) {
				// Wenn nach Artikelklassen/Gruppen gefiltert wird, dann
				// kommen nur Ident-Positionen
				crit1.add(Restrictions.eq(BestellpositionFac.FLR_BESTELLPOSITION_BESTELLPOSITIONART_C_NR,
						BestellpositionFac.BESTELLPOSITIONART_IDENT));
				Criteria critArtikel = crit1.createCriteria(BestellpositionFac.FLR_BESTELLPOSITION_FLRARTIKEL);
				if (artikelklasseIId != null) {
					critArtikel.createCriteria(ArtikelFac.FLR_ARTIKEL_FLRARTIKELKLASSE)
							.add(Restrictions.eq("i_id", artikelklasseIId));
				}
				if (artikelgruppeIId != null) {
					critArtikel.createCriteria(ArtikelFac.FLR_ARTIKEL_FLRARTIKELGRUPPE)
							.add(Restrictions.eq("i_id", artikelgruppeIId));
				}
				if (artikelCNrVon != null) {
					critArtikel.add(Restrictions.ge(ArtikelFac.FLR_ARTIKEL_C_NR, artikelCNrVon));
				}
				if (artikelCNrBis != null) {
					critArtikel.add(Restrictions.le(ArtikelFac.FLR_ARTIKEL_C_NR, artikelCNrBis));
				}
			}
			// PJ20670
			crit1.addOrder(Order.asc("i_sort"));

			List<?> resultList = crit1.list();
			for (Iterator<?> iter = resultList.iterator(); iter.hasNext();) {
				FLRBestellpositionReport item = (FLRBestellpositionReport) iter.next();
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
					Criteria critWep = session.createCriteria(FLRWareneingangspositionen.class);
					critWep.createCriteria("flrbestellposition").add(Restrictions.eq("i_id", item.getI_id()));
					List<?> wepResultList = critWep.list();
					Iterator<?> wepResultListIterator = wepResultList.iterator();

					data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_BESTELLUNGCNR] = flrbestellung.getC_nr();
					data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_STATUS] = flrbestellung
							.getBestellungstatus_c_nr();
					data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_BESTELLUNGARTCNR] = flrbestellung
							.getBestellungart_c_nr();
					data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_PROJEKT] = flrbestellung
							.getC_bezprojektbezeichnung();
					data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_BESTELLUNGLIEFERANT] = flrbestellung
							.getFlrlieferant().getFlrpartner().getC_name1nachnamefirmazeile1();
					data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ARTIKELCNR] = artikelCNr;
					data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ARTIKELREFERENZNUMMER] = item.getFlrartikel()
							.getC_referenznr();

					data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_POS_NR] = getBestellpositionFac()
							.getPositionNummerReadOnly(item.getI_id());

					// SP903
					if (item.getPosition_i_id_artikelset() != null) {
						data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_SETARTIKEL_TYP] = ArtikelFac.SETARTIKEL_TYP_POSITION;
					} else {

						Session sessionSet = FLRSessionFactory.getFactory().openSession();

						sessionSet = factory.openSession();
						Criteria critSet = sessionSet.createCriteria(FLRBestellpositionReport.class);
						critSet.add(Restrictions.eq("position_i_id_artikelset", item.getI_id()));

						int iZeilen = critSet.list().size();

						if (iZeilen > 0) {
							data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_SETARTIKEL_TYP] = ArtikelFac.SETARTIKEL_TYP_KOPF;
						}
						sessionSet.close();

					}

					// PJ 14752

					String sortierstring = "";
					if (krit.bSortiereNachKostenstelle == true) {
						sortierstring = Helper.fitString2Length(flrbestellung.getFlrkostenstelle().getC_nr(), 80, ' ');
					}

					if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_IDENT) {
						sortierstring += Helper.fitString2Length(
								flrbestellung.getFlrlieferant().getFlrpartner().getC_name1nachnamefirmazeile1(), 80,
								' ') + Helper.fitString2Length(artikelCNr, 80, ' ');
					}

					if (bSortierungNachLiefertermin == true) {
						sortierstring += Helper.fitString2Length(flrbestellung.getT_liefertermin() + "", 15, ' ');
					}

					data[i][REPORT_BESTELLUNG_OFFENE_SORTIERKRITERIUM] = sortierstring;

					data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ARTIKELMENGE] = item.getN_menge();
					if (item.getGebinde_i_id() != null) {
						data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_GEBINDENAME] = item.getFlrgebinde()
								.getC_bez();
						data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ANZAHL_GEBINDE] = item
								.getN_anzahlgebinde();
					}

					BigDecimal bdOffeneLiefermenge = new BigDecimal(0);
					if (BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR
							.equals(flrbestellung.getBestellungart_c_nr())) {
						try {
							BestellpositionDto[] abrufPos = getBestellpositionFac()
									.bestellpositionFindByBestellpositionIIdRahmenposition(item.getI_id(),
											theClientDto);
							for (int y = 0; y < abrufPos.length; y++) {
								bdOffeneLiefermenge = bdOffeneLiefermenge
										.add(getBestellpositionFac().berechneOffeneMenge(abrufPos[y]));
							}
						} catch (RemoteException e) {
						}
						data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_OFFENELIEFERUNGEN] = bdOffeneLiefermenge;

					} else {
						data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_OFFENELIEFERUNGEN] = null;
					}
					StringBuffer sbArtikelInfo = new StringBuffer();

					ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(item.getFlrartikel().getI_id(),
							theClientDto);

					if (item.getC_bezeichnung() != null) {
						sbArtikelInfo.append(item.getC_bezeichnung());
					} else {

						if (artikelDto.getArtikelsprDto() != null) {
							if (artikelDto.getArtikelsprDto().getCBez() != null) {
								sbArtikelInfo.append(artikelDto.getArtikelsprDto().getCBez());
							}
						}
					}
					data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ARTIKELBEZ] = sbArtikelInfo.toString();

					if (artikelDto.getArtikelsprDto() != null) {
						data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ARTIKELZBEZ] = artikelDto
								.getArtikelsprDto().getCZbez();
						data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ARTIKELZBEZ2] = artikelDto
								.getArtikelsprDto().getCZbez2();
					}
					if (item.getC_zusatzbezeichnung() != null) {
						data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ARTIKELZBEZ] = item
								.getC_zusatzbezeichnung();
					}
					// der Preis wird in Mandantenwaehrung angezeigt, es
					// gilt der hinterlegte Wechselkurs
					BigDecimal bdPreisinmandantenwaehrung = item.getN_nettogesamtpreis();
					BigDecimal wechselkursmandantwaehrungzuauftragswaehrung = null;
					if (!flrbestellung.getWaehrung_c_nr_bestellwaehrung()
							.equals(theClientDto.getSMandantenwaehrung())) {
						wechselkursmandantwaehrungzuauftragswaehrung = new BigDecimal(
								flrbestellung.getF_wechselkursmandantwaehrungbestellungswaehrung().doubleValue());
						bdPreisinmandantenwaehrung = getBetragMalWechselkurs(bdPreisinmandantenwaehrung,
								Helper.getKehrwert(wechselkursmandantwaehrungzuauftragswaehrung));
					}
					if (getTheJudgeFac().hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_EINKAUF, theClientDto)) {
						data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ARTIKELPREIS] = bdPreisinmandantenwaehrung;
					} else {
						data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ARTIKELPREIS] = null;
					}

					if (item.getEinheit_c_nr() != null) {
						data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ARTIKELEINHEIT] = item.getEinheit_c_nr()
								.trim();
					}
					if (item.getT_auftragsbestaetigungstermin() != null) {
						data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ABTERMIN] = Helper
								.formatDatum(item.getT_auftragsbestaetigungstermin(), theClientDto.getLocUi());
					}

					data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ABNUMMER] = item.getC_abnummer();
					data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ABKOMMENTAR] = item.getC_abkommentar();
					data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ABURSPRUNGSTERMIN] = item
							.getT_abursprungstermin();

					if (item.getT_uebersteuerterliefertermin() != null) {
						data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_BESTELLUNGLIEFERTERMIN] = Helper
								.formatDatum(item.getT_uebersteuerterliefertermin(), theClientDto.getLocUi());
					} else {
						data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_BESTELLUNGLIEFERTERMIN] = Helper
								.formatDatum(item.getFlrbestellung().getT_liefertermin(), theClientDto.getLocUi());
					}
					BigDecimal noffeneMenge = item.getN_menge();
					BigDecimal ngeliferteMenge = new BigDecimal(0);
					if (flrbestellung.getBestellungart_c_nr()
							.equals(BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR)) {
						noffeneMenge = item.getN_offenemenge();
						while (wepResultListIterator.hasNext()) {
							FLRWareneingangspositionen waren = (FLRWareneingangspositionen) wepResultListIterator
									.next();
							ngeliferteMenge = ngeliferteMenge.add(waren.getN_geliefertemenge());
						}

					} else {
						while (wepResultListIterator.hasNext()) {
							FLRWareneingangspositionen waren = (FLRWareneingangspositionen) wepResultListIterator
									.next();
							noffeneMenge = noffeneMenge.subtract(waren.getN_geliefertemenge());
						}
					}
					data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ARTIKELGELIEFERTEMENGE] = ngeliferteMenge;

					data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ARTIKELOFFENEMENGE] = noffeneMenge;

					if (item.getN_fixkosten() != null && noffeneMenge.doubleValue() > 0) {
						data[i][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_OFFENE_FIXKOSTEN] = item.getN_fixkosten()
								.divide(item.getN_menge(), 4, BigDecimal.ROUND_HALF_EVEN).multiply(noffeneMenge);
					}

					if (getTheJudgeFac().hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_EINKAUF, theClientDto)) {
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
			Object[][] returnArray = new Object[alTemp
					.size()][BestellungReportFac.REPORT_BESTELLUNG_OFFENE_ANZAHL_SPALTEN];
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

		parameter.put(LPReport.P_SORTIERUNG, buildSortierungBestellungOffene(krit, theClientDto));
		parameter.put(LPReport.P_FILTER, buildFilterBestellungOffene(krit, artikelklasseIId, artikelgruppeIId,
				artikelCNrVon, artikelCNrBis, projektCBezeichnung, theClientDto));

		parameter.put(LPReport.P_SORTIERENACHLIEFERANT,
				new Boolean(krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER));
		parameter.put("P_SORTIERENACHBESTELLUNGART",
				new Boolean(krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_ART));
		parameter.put("P_TITLE",
				getTextRespectUISpr("bes.print.offene", theClientDto.getMandant(), theClientDto.getLocUi()));
		parameter.put(P_MANDANTWAEHRUNG, theClientDto.getSMandantenwaehrung());

		initJRDS(parameter, BestellungReportFac.REPORT_MODUL, BestellungReportFac.REPORT_BESTELLUNG_JOURNAL_OFFENE,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printErwarteteLieferungen(TheClientDto theClientDto) {
		useCase = UC_ERWARTETELIEFERUNGEN;
		HashMap<String, Object> mapParameter = new HashMap<String, Object>();

		Timestamp tHeute = Helper.cutTimestamp(new Timestamp(System.currentTimeMillis()));

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT bp FROM FLRBestellpositionReport bp WHERE bp.flrbestellung.mandant_c_nr='"
				+ theClientDto.getMandant() + "' " + " AND bp.flrbestellung.bestellungstatus_c_nr NOT IN ('"
				+ BestellungFac.BESTELLSTATUS_STORNIERT + "','" + BestellungFac.BESTELLSTATUS_ERLEDIGT
				+ "') AND bp.bestellpositionstatus_c_nr  NOT IN ('" + BestellpositionFac.BESTELLPOSITIONSTATUS_ERLEDIGT
				+ "','" + BestellpositionFac.BESTELLPOSITIONSTATUS_GELIEFERT + "') "

				+ " ORDER BY bp.flrbestellung.flrpersonal.c_kurzzeichen, bp.flrbestellung.flrlieferant.flrpartner.c_name1nachnamefirmazeile1, bp.flrbestellung.c_nr, bp.flrartikel.c_nr ";

		org.hibernate.Query query = session.createQuery(queryString);
		List resultList = query.list();

		Iterator resultListIterator = resultList.iterator();
		int iTageInZukunft = 18;

		try {
			ParametermandantDto parametermandantDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_LIEFERPLAN_TAGE_IN_ZUKUNFT);

			iTageInZukunft = (Integer) parametermandantDto.getCWertAsObject();

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		class SubreportDaten {
			public BigDecimal bdMenge = BigDecimal.ZERO;
			public java.util.Date kommissionierungGeplant = null;
			public java.util.Date kommissionierungDurchgefuehrt = null;
			public java.util.Date unterlagenUebergeben = null;

			boolean bKommissionierungGeplantEnthaeltKeinDatum = false;
			boolean bkommissionierungDurchgefuehrtEnthaeltKeinDatum = false;
			boolean bunterlagenUebergebenEnthaeltKeinDatum = false;

			public void addMenge(BigDecimal bdMenge) {
				this.bdMenge = this.bdMenge.add(bdMenge);
			}

			SubreportDaten() {

			}
		}

		LinkedHashMap<Integer, LinkedHashMap<Integer, Object[]>> lhmBestellungen = new LinkedHashMap<Integer, LinkedHashMap<Integer, Object[]>>();

		while (resultListIterator.hasNext()) {
			FLRBestellpositionReport bp = (FLRBestellpositionReport) resultListIterator.next();

			if (bp.getN_menge() != null && bp.getFlrartikel() != null) {

				Integer bestellungIId = bp.getFlrbestellung().getI_id();
				if (bp.getFlrbestellung().getBestellungart_c_nr()
						.equals(BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR)
						&& bp.getFlrbestellung().getBestellung_i_id_rahmenbestellung() != null) {
					bestellungIId = bp.getFlrbestellung().getBestellung_i_id_rahmenbestellung();
				}

				LinkedHashMap<Integer, Object[]> lhmArtikel = null;

				if (lhmBestellungen.containsKey(bestellungIId)) {
					lhmArtikel = lhmBestellungen.get(bestellungIId);
				} else {
					lhmArtikel = new LinkedHashMap<Integer, Object[]>();
				}

				Object[] oZeile = null;
				if (lhmArtikel.containsKey(bp.getFlrartikel().getI_id())) {
					oZeile = lhmArtikel.get(bp.getFlrartikel().getI_id());
				} else {
					oZeile = new Object[REPORT_ERWARTETELIEFERUNGEN_ANZAHL_SPALTEN];
					oZeile[REPORT_ERWARTETELIEFERUNGEN_RUECKSTAND] = BigDecimal.ZERO;
					oZeile[REPORT_ERWARTETELIEFERUNGEN_DANACH] = BigDecimal.ZERO;

					// Daten befuellen
					BestellungDto bsDto = null;
					try {
						bsDto = getBestellungFac().bestellungFindByPrimaryKey(bestellungIId);
					} catch (RemoteException e1) {
						throwEJBExceptionLPRespectOld(e1);
					}

					oZeile[REPORT_ERWARTETELIEFERUNGEN_BESTELLUNG] = bsDto.getCNr();
					oZeile[REPORT_ERWARTETELIEFERUNGEN_BESTELLUNGSART] = bsDto.getBestellungartCNr();

					PersonalDto personalDto = getPersonalFac()
							.personalFindByPrimaryKey(bsDto.getPersonalIIdAnforderer(), theClientDto);
					oZeile[REPORT_ERWARTETELIEFERUNGEN_ANFORDERER] = personalDto.getCKurzzeichen();

					// Kunde
					LieferantDto lfDto = getLieferantFac()
							.lieferantFindByPrimaryKey(bsDto.getLieferantIIdBestelladresse(), theClientDto);

					oZeile[REPORT_ERWARTETELIEFERUNGEN_LIEFERANT_NAME] = lfDto.getPartnerDto().formatFixName1Name2();

					LandplzortDto lplzDto = lfDto.getPartnerDto().getLandplzortDto();

					if (lplzDto != null && lplzDto.getOrtDto() != null && lplzDto.getLandDto() != null) {
						oZeile[REPORT_ERWARTETELIEFERUNGEN_LIEFERANT_LKZ] = lplzDto.getLandDto().getCLkz();
						oZeile[REPORT_ERWARTETELIEFERUNGEN_LIEFERANT_PLZ] = lplzDto.getCPlz();
						oZeile[REPORT_ERWARTETELIEFERUNGEN_LIEFERANT_ORT] = lplzDto.getOrtDto().getCName();
					}

					oZeile[REPORT_ERWARTETELIEFERUNGEN_ARTIKELNUMMER] = bp.getFlrartikel().getC_nr();

					ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(bp.getFlrartikel().getI_id(),
							theClientDto);

					if (artikelDto.getArtikelsprDto() != null) {

						oZeile[REPORT_ERWARTETELIEFERUNGEN_BEZEICHNUNG] = artikelDto.getArtikelsprDto().getCBez();
						oZeile[REPORT_ERWARTETELIEFERUNGEN_KURZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCKbez();
						oZeile[REPORT_ERWARTETELIEFERUNGEN_ZUSATZBEZEICHNUNG] = artikelDto.getArtikelsprDto()
								.getCZbez();
						oZeile[REPORT_ERWARTETELIEFERUNGEN_ZUSATZBEZEICHNUNG2] = artikelDto.getArtikelsprDto()
								.getCZbez2();

					}
					oZeile[REPORT_ERWARTETELIEFERUNGEN_EINHEIT] = bp.getEinheit_c_nr();

					oZeile[REPORT_ERWARTETELIEFERUNGEN_EKPREIS] = bp.getN_nettogesamtpreis();
					if (bp.getFlrbestellung().getBestellungart_c_nr()
							.equals(BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR)) {
						oZeile[REPORT_ERWARTETELIEFERUNGEN_OFFENE_MENGE] = bp.getN_offenemenge();
					}

					oZeile[REPORT_ERWARTETELIEFERUNGEN_KOMISSIONIERUNG_GEPLANT] = bsDto.getTKommissionierungGeplant();
					oZeile[REPORT_ERWARTETELIEFERUNGEN_KOMISSIONIERUNG_DURCHGEFUEHRT] = bsDto
							.getTKommissionierungDurchgefuehrt();
					oZeile[REPORT_ERWARTETELIEFERUNGEN_UNTERLAGEN_UEBERGEBEN] = bsDto.getTUebergabeTechnik();

					try {
						oZeile[REPORT_ERWARTETELIEFERUNGEN_LAGERSTAND] = getLagerFac()
								.getLagerstandAllerLagerEinesMandanten(bp.getFlrartikel().getI_id(), theClientDto);

						oZeile[REPORT_ERWARTETELIEFERUNGEN_BESTELLT] = getArtikelbestelltFac()
								.getAnzahlBestellt(bp.getFlrartikel().getI_id());

						oZeile[REPORT_ERWARTETELIEFERUNGEN_INFERTIGUNG] = getFertigungFac()
								.getAnzahlInFertigung(bp.getFlrartikel().getI_id(), theClientDto);

						oZeile[REPORT_ERWARTETELIEFERUNGEN_FEHLMENGE] = getFehlmengeFac()
								.getAnzahlFehlmengeEinesArtikels(bp.getFlrartikel().getI_id(), theClientDto);
						oZeile[REPORT_ERWARTETELIEFERUNGEN_RESERVIERT] = getReservierungFac()
								.getAnzahlReservierungen(bp.getFlrartikel().getI_id(), theClientDto);

						Hashtable htAnzahlRahmenbestellt = getArtikelbestelltFac()
								.getAnzahlRahmenbestellt(bp.getFlrartikel().getI_id(), theClientDto);
						if (htAnzahlRahmenbestellt.containsKey(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL)) {
							BigDecimal rahmenbestellt = (BigDecimal) htAnzahlRahmenbestellt
									.get(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL);
							oZeile[REPORT_ERWARTETELIEFERUNGEN_RAHMENBESTELLT] = rahmenbestellt;
						}

					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}

					TreeMap<Timestamp, SubreportDaten> tmSubreportMuster = new TreeMap();

					Calendar cHeute = Calendar.getInstance();

					for (int i = 0; i < iTageInZukunft; i++) {
						tmSubreportMuster.put(Helper.cutTimestamp(new Timestamp(cHeute.getTimeInMillis())),
								new SubreportDaten());
						cHeute.add(Calendar.DATE, 1);
					}

					oZeile[REPORT_ERWARTETELIEFERUNGEN_SUBREPORT_TERMINE] = tmSubreportMuster.clone();

				}

				// Termin berechnen

				if (!bp.getFlrbestellung().getBestellungart_c_nr()
						.equals(BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR)) {

					Timestamp tLiefertermin = bp.getT_uebersteuerterliefertermin();

					if (bp.getT_auftragsbestaetigungstermin() != null) {
						tLiefertermin = bp.getT_auftragsbestaetigungstermin();
					}

					// Wenn vor heute, dann Rueckstand
					if (tLiefertermin.before(tHeute)) {
						BigDecimal bdRueckstand = (BigDecimal) oZeile[REPORT_ERWARTETELIEFERUNGEN_RUECKSTAND];
						bdRueckstand = bdRueckstand.add(bp.getN_offenemenge());

						oZeile[REPORT_ERWARTETELIEFERUNGEN_RUECKSTAND] = bdRueckstand;

					} else {

						TreeMap<Timestamp, SubreportDaten> tmSubreport = (TreeMap) oZeile[REPORT_ERWARTETELIEFERUNGEN_SUBREPORT_TERMINE];

						if (tmSubreport.containsKey(tLiefertermin)) {

							SubreportDaten daten = tmSubreport.get(tLiefertermin);

							daten.addMenge(bp.getN_offenemenge());

							if (bp.getFlrbestellung().getT_komissionierung_geplant() != null) {
								daten.kommissionierungGeplant = bp.getFlrbestellung().getT_komissionierung_geplant();
							} else {
								daten.bKommissionierungGeplantEnthaeltKeinDatum = true;
							}

							if (bp.getFlrbestellung().getT_komissionierung_durchgefuehrt() != null) {
								daten.kommissionierungDurchgefuehrt = bp.getFlrbestellung()
										.getT_komissionierung_durchgefuehrt();
							} else {
								daten.bkommissionierungDurchgefuehrtEnthaeltKeinDatum = true;
							}

							if (bp.getFlrbestellung().getT_uebergabe_technik() != null) {
								daten.unterlagenUebergeben = bp.getFlrbestellung().getT_uebergabe_technik();
							} else {
								daten.bunterlagenUebergebenEnthaeltKeinDatum = true;
							}

							tmSubreport.put(tLiefertermin, daten);

						} else {
							// Dann muss es danach sein
							BigDecimal bdDanach = (BigDecimal) oZeile[REPORT_ERWARTETELIEFERUNGEN_DANACH];
							bdDanach = bdDanach.add(bp.getN_offenemenge());

							oZeile[REPORT_ERWARTETELIEFERUNGEN_DANACH] = bdDanach;
						}

						oZeile[REPORT_ERWARTETELIEFERUNGEN_SUBREPORT_TERMINE] = tmSubreport;

					}
				}

				lhmArtikel.put(bp.getFlrartikel().getI_id(), oZeile);

				lhmBestellungen.put(bestellungIId, lhmArtikel);

			}

		}
		session.close();

		Iterator it = lhmBestellungen.keySet().iterator();

		ArrayList<Object[]> alDaten = new ArrayList<Object[]>();

		DateFormatSymbols symbols = new DateFormatSymbols(theClientDto.getLocUi());

		String[] defaultDays = symbols.getWeekdays();

		while (it.hasNext()) {
			Integer auftragIId = (Integer) it.next();

			LinkedHashMap<Integer, Object[]> lhmArtikel = lhmBestellungen.get(auftragIId);

			Iterator it2 = lhmArtikel.keySet().iterator();
			while (it2.hasNext()) {
				Object[] oZeile = lhmArtikel.get(it2.next());

				// Subreport erstellen

				TreeMap<Timestamp, SubreportDaten> tmSubreport = (TreeMap) oZeile[REPORT_ERWARTETELIEFERUNGEN_SUBREPORT_TERMINE];

				String[] fieldnames = new String[] { "Datum", "Menge", "Tagesart", "KommissionierungDurchgefuehrt",
						"KommissionierungGeplant", "UnterlagenUebergeben" };
				Object[][] oSubData = new Object[tmSubreport.size()][6];

				Iterator itSub = tmSubreport.keySet().iterator();
				int i = 0;
				while (itSub.hasNext()) {

					Timestamp datum = (Timestamp) itSub.next();

					SubreportDaten daten = tmSubreport.get(datum);

					BigDecimal bdMenge = daten.bdMenge;

					Calendar c = Calendar.getInstance();
					c.setTime(datum);

					oSubData[i][0] = datum;

					oSubData[i][1] = bdMenge;
					oSubData[i][2] = defaultDays[c.get(Calendar.DAY_OF_WEEK)];

					if (daten.bKommissionierungGeplantEnthaeltKeinDatum == false) {
						oSubData[i][3] = daten.kommissionierungGeplant;
					}
					if (daten.bkommissionierungDurchgefuehrtEnthaeltKeinDatum == false) {
						oSubData[i][4] = daten.kommissionierungDurchgefuehrt;
					}
					if (daten.bunterlagenUebergebenEnthaeltKeinDatum == false) {
						oSubData[i][5] = daten.unterlagenUebergeben;
					}

					i++;
				}

				oZeile[REPORT_ERWARTETELIEFERUNGEN_SUBREPORT_TERMINE] = new LPDatenSubreport(oSubData, fieldnames);

				if (!mapParameter.containsKey("P_SUBREPORT_TERMINE")) {
					mapParameter.put("P_SUBREPORT_TERMINE", new LPDatenSubreport(oSubData, fieldnames));
				}

				alDaten.add(oZeile);
			}

		}

		data = new Object[alDaten.size()][REPORT_ERWARTETELIEFERUNGEN_ANZAHL_SPALTEN];
		data = alDaten.toArray(data);

		initJRDS(mapParameter, BestellungReportFac.REPORT_MODUL, BestellungReportFac.REPORT_ERWARTETELIEFERUNGEN,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();
	}

	/**
	 * Alle offenen Bestellungen fuer einen bestimmten Mandanten drucken.
	 * 
	 * @param krit                        die Filter- und Sortierkriterien
	 * @param bSortierungNachLiefertermin Boolean
	 * @param bAnfragevorschlag
	 * @param theClientDto                der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 * @return JasperPrint der Druck
	 */
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printBestellVorschlag(ReportJournalKriterienDto krit, Boolean bSortierungNachLiefertermin,
			boolean bAnfragevorschlag, Integer partnerIIdStandort, TheClientDto theClientDto) throws EJBExceptionLP {
		JasperPrintLP oPrintO = null;
		useCase = UC_BESTELLVORSCHLAG;
		int iAnzahlZeilen = 0;
		Locale locDruck = theClientDto.getLocUi();

		ArrayList<FLRBestellvorschlag> bestellung = new ArrayList();

		boolean bLagerminJeLager = lagerMinJeLager(theClientDto);

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			LagerDto[] lagerDto = null;
			if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
					theClientDto)) {
				lagerDto = getLagerFac().lagerFindAll();
			} else {
				lagerDto = getLagerFac().lagerFindByMandantCNr(theClientDto.getMandant());
			}

			session = factory.openSession();
			// Hiberante Criteria fuer alle Tabellen ausgehend von meiner
			// Haupttabelle anlegen,
			// nach denen ich filtern und sortieren kann
			Criteria crit = session.createCriteria(FLRBestellvorschlag.class);

			// Einschraenkung auf den aktuellen Mandanten
			crit.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));

			if (partnerIIdStandort != null) {
				crit.add(Restrictions.eq("flrpartner_standort.i_id", partnerIIdStandort));
			}

			// Einschraenkung nach einem bestimmten Kunden
			if (krit.lieferantIId != null) {
				crit.add(Restrictions.eq(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_LIEFERANT_I_ID, krit.lieferantIId));
			}

			if (getParameterFac().getPersoenlicherBestellvorschlag(theClientDto.getMandant())) {
				crit.add(Restrictions.eq(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_PERSONAL_I_ID,
						theClientDto.getIDPersonal()));
			}

			// Sortierung nach Kunde, eventuell innerhalb der Kostenstelle
			if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				crit.createCriteria(BestellungFac.FLR_BESTELLUNG_FLRLIEFERANT).createCriteria(LieferantFac.FLR_PARTNER)
						.addOrder(Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));
			}

			if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_IDENT) {
				crit.createCriteria(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_FLRARTIKEL).addOrder(Order.asc("c_nr"));
			}

			// Sortierung nach Liefertermin (optional)
			if (bSortierungNachLiefertermin != null && bSortierungNachLiefertermin.booleanValue()) {
				crit.addOrder(Order.asc(BestellungFac.FLR_BESTELLUNG_T_LIEFERTERMIN));
			}
			crit.addOrder(Order.asc(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_BELEGART_C_NR));
			List<?> list = crit.list();
			Iterator<?> it = list.iterator();

			while (it.hasNext()) {
				FLRBestellvorschlag flrbestellvorschlag = (FLRBestellvorschlag) it.next();
				bestellung.add(flrbestellvorschlag);
				iAnzahlZeilen++;
			}
			// Danach wenn nach Lieferant sortiert auch noch die null
			// Lieferanten anfuegen
			if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				Criteria crit2 = session.createCriteria(FLRBestellvorschlag.class);
				crit2.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));
				crit2.add(Restrictions.isNull(BestellungFac.FLR_BESTELLUNG_FLRLIEFERANT));
				if (bSortierungNachLiefertermin != null && bSortierungNachLiefertermin.booleanValue()) {
					crit2.addOrder(Order.asc(BestellungFac.FLR_BESTELLUNG_T_LIEFERTERMIN));
				}

				if (getParameterFac().getPersoenlicherBestellvorschlag(theClientDto.getMandant())) {
					crit2.add(Restrictions.eq(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_PERSONAL_I_ID,
							theClientDto.getIDPersonal()));
				}

				crit2.addOrder(Order.asc(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_BELEGART_C_NR));
				if (krit.kundeIId != null) {
					crit2.add(Restrictions.eq(BestellungFac.FLR_BESTELLUNG_LIEFERANT_I_ID_BESTELLADRESSE,
							krit.lieferantIId));
				}

				list = crit2.list();
				it = list.iterator();
				while (it.hasNext()) {
					FLRBestellvorschlag flrbestellvorschlag = (FLRBestellvorschlag) it.next();
					bestellung.add(flrbestellvorschlag);
					iAnzahlZeilen++;
				}
			}

			data = new Object[iAnzahlZeilen][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ANZAHL_SPALTEN];

			HashMap<Integer, ArtikelDto> hmArtikelDtoCache = new HashMap<Integer, ArtikelDto>();

			int i = 0;
			FLRBestellvorschlag flrbestellung = null;
			while (i < iAnzahlZeilen) {
				flrbestellung = (FLRBestellvorschlag) bestellung.get(i);
				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_BELEGART] = flrbestellung.getBelegart_c_nr();
				String belegCNr = "";
				if (flrbestellung.getBelegart_c_nr() != null) { // Es gibt auch
					// Eintraege
					// ohne
					// Belegart, zb
					// fuer den
					// mindestlagerstand
					if (flrbestellung.getBelegart_c_nr().equals(LocaleFac.BELEGART_AUFTRAG)) {
						AuftragDto aDto = getAuftragFac().auftragFindByPrimaryKey(flrbestellung.getI_belegartid());
						belegCNr = aDto.getCNr();
					} else if (flrbestellung.getBelegart_c_nr().equals(LocaleFac.BELEGART_LOS)) {
						LosDto aDto = getFertigungFac().losFindByPrimaryKey(flrbestellung.getI_belegartid());
						belegCNr = aDto.getCNr();
					}
				}
				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_BELEGCNR] = belegCNr;
				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_MANDANT] = flrbestellung.getMandant_c_nr();

				if (flrbestellung.getPartner_i_id_standort() != null) {
					data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_STANDORT] = flrbestellung
							.getFlrpartner_standort().getC_kbez();
				}

				if (flrbestellung.getGebinde_i_id() != null) {
					data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_GEBINDENAME] = flrbestellung.getFlrgebinde()
							.getC_bez();
				}
				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ANZAHL_GEBINDE] = flrbestellung
						.getN_anzahlgebinde();

				Boolean bGebindepflichtig = Boolean.FALSE;

				if (flrbestellung.getFlrlieferant() != null) {
					ArrayList<GebindeDto> alGebinde = getArtikelFac().getGebindeEinesArtikelsUndEinesLieferanten(
							flrbestellung.getArtikel_i_id(), flrbestellung.getFlrlieferant().getI_id(),
							new java.sql.Date(System.currentTimeMillis()), theClientDto);
					if (alGebinde.size() > 0) {
						bGebindepflichtig = Boolean.TRUE;
					}
				}
				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_GEBINDEPFLICHTIG] = bGebindepflichtig;

				if (flrbestellung.getLieferant_i_id() != null) {
					data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_BESTELLUNGLIEFERANT] = flrbestellung
							.getFlrlieferant().getFlrpartner().getC_name1nachnamefirmazeile1();
				}
				if (flrbestellung.getProjekt_i_id() != null) {
					data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_PROJEKT] = flrbestellung.getFlrprojekt()
							.getC_nr();
				}

				/*
				 * locDruck = Helper.string2Locale(flrbestellung.getFlrlieferant(
				 * ).getFlrpartner(). getLocale_c_nr_kommunikation());
				 */
				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_BESTELLTERMIN] = Helper
						.formatDatum(flrbestellung.getT_liefertermin(), locDruck);

				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELCNR] = flrbestellung.getFlrartikel()
						.getC_nr();
				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELREFERENZNUMMER] = flrbestellung
						.getFlrartikel().getC_referenznr();
				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKEL_LAGERBEWIRTSCHAFTET] = Helper
						.short2Boolean(flrbestellung.getFlrartikel().getB_lagerbewirtschaftet());
				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELMENGE] = flrbestellung
						.getN_zubestellendemenge();
				ArtikelDto oArtikelDto = getArtikelFac().artikelFindByPrimaryKey(flrbestellung.getArtikel_i_id(),
						theClientDto);
				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELBEZ] = oArtikelDto.getArtikelsprDto()
						.getCBez();
				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELKBEZ] = oArtikelDto.getArtikelsprDto()
						.getCKbez();
				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELZBEZ] = oArtikelDto.getArtikelsprDto()
						.getCZbez();
				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELZBEZ2] = oArtikelDto.getArtikelsprDto()
						.getCZbez2();

				Long[] angebotenUndAngefragt = getAnfrageFac()
						.getAngefragteUndAngeboteneMenge(flrbestellung.getArtikel_i_id(), theClientDto);

				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELANZAHLANGEFRAGT] = angebotenUndAngefragt[0];
				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELANZAHLANGEBOTEN] = angebotenUndAngefragt[1];

				// Sperren
				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_SPERREN] = getArtikelFac()
						.getArtikelsperrenText(flrbestellung.getArtikel_i_id());

				// offene Bestellmenge
				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKEL_MENGE_OFFEN] = getArtikelbestelltFac()
						.getAnzahlBestellt(flrbestellung.getArtikel_i_id());

				// offene Rahmenbestellmenge
				Hashtable<?, ?> htRahmenbestellt = getArtikelbestelltFac()
						.getAnzahlRahmenbestellt(flrbestellung.getArtikel_i_id(), theClientDto);
				if (htRahmenbestellt.containsKey(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL)) {
					data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKEL_RAHMENMENGE_OFFEN] = htRahmenbestellt
							.get(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL);
				}
				// alle offenen Rahmenbestellnr
				if (htRahmenbestellt.containsKey(ArtikelbestelltFac.KEY_RAHMENBESTELLT_BELEGCNR)) {
					Collection<?> aRahmenbestellnr = (Collection<?>) htRahmenbestellt
							.get(ArtikelbestelltFac.KEY_RAHMENBESTELLT_BELEGCNR);
					if (aRahmenbestellnr != null && aRahmenbestellnr.size() > 0) {
						String[] aRahmenbestellnrStringArray = (String[]) aRahmenbestellnr.toArray(new String[0]);
						data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKEL_RAHMENBESTELLNR] = Helper
								.erzeugeStringAusStringArray(aRahmenbestellnrStringArray);
					}
				}
				// alle offenenen Bestellungen CNr
				ArrayList<String> alBestellungnr = new ArrayList<String>();
				Collection<?> lFLRArtikelbestellt = getArtikelbestelltFac()
						.getArtikelbestellt(flrbestellung.getArtikel_i_id(), null, null);
				Iterator<?> iteratorFLRArtikelbestellt = lFLRArtikelbestellt.iterator();
				while (iteratorFLRArtikelbestellt.hasNext()) {
					FLRArtikelbestellt fLRArtikelbestellt = (FLRArtikelbestellt) iteratorFLRArtikelbestellt.next();
					BestellpositionDto bestellpositionDto = null;
					BestellungDto bestellungDto = null;
					if (fLRArtikelbestellt.getC_belegartnr().equals(LocaleFac.BELEGART_BESTELLUNG)) {
						bestellpositionDto = getBestellpositionFac()
								.bestellpositionFindByPrimaryKey(fLRArtikelbestellt.getI_belegartpositionid());
						if (!bestellpositionDto.getBestellpositionstatusCNr()
								.equals(BestellpositionFac.BESTELLPOSITIONSTATUS_ERLEDIGT)) {
							bestellungDto = getBestellungFac()
									.bestellungFindByPrimaryKey(bestellpositionDto.getBestellungIId());
							alBestellungnr.add(bestellungDto.getCNr());
						}
					}
				}
				// offene Bestellungen CNr als String uebergeben
				if (alBestellungnr.size() > 0) {
					String[] aArtikelbestellNr = (String[]) alBestellungnr.toArray(new String[0]);
					data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKEL_OFFENE_BESTELLNR] = Helper
							.erzeugeStringAusStringArray(aArtikelbestellNr);
				}

				// ----------------
				// Artikellieferant, wenn vorhanden
				ArtikellieferantDto artikellieferantDto = getArtikelFac().getArtikelEinkaufspreis(
						flrbestellung.getArtikel_i_id(), flrbestellung.getLieferant_i_id(), BigDecimal.ONE,

						theClientDto.getSMandantenwaehrung(), new java.sql.Date(System.currentTimeMillis()),
						theClientDto);

				if (artikellieferantDto != null) {
					data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_LIEFERANT_ARTIKEL_IDENTNUMMER] = artikellieferantDto
							.getCArtikelnrlieferant();
					data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_LIEFERANT_ARTIKEL_BEZEICHNUNG] = artikellieferantDto
							.getCBezbeilieferant();
					data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_LIEFERANT_STANDARDMENGE] = artikellieferantDto
							.getFStandardmenge();

					data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_LIEFERANT_MATERIALZUSCHLAG] = artikellieferantDto
							.getNMaterialzuschlag();
					data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_LIEFERANT_LIEF1PREIS] = artikellieferantDto
							.getNNettopreis();
					data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_LIEFERANT_PREISGUELTIGAB] = artikellieferantDto
							.getTPreisgueltigab();
				}
				BigDecimal rahmenbedarf = this.getRahmenbedarfeFac()
						.getSummeAllerRahmenbedarfeEinesArtikels(flrbestellung.getArtikel_i_id());
				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKEL_RAHMENDETAILBEDARF] = rahmenbedarf;

				BigDecimal nLagersoll = new BigDecimal(0);
				BigDecimal nLagermindest = new BigDecimal(0);
				BigDecimal nArtikelPreis = new BigDecimal(0);
				BigDecimal nAnzahlArtikelRes = new BigDecimal(0);
				BigDecimal nFehlMenge = new BigDecimal(0);

				if (bLagerminJeLager) {
					BigDecimal[] bd = getLagerFac().getSummeLagermindesUndLagerSollstandEinesStandorts(
							flrbestellung.getArtikel_i_id(), partnerIIdStandort, theClientDto);

					data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELLAGERMINDESTSTAND] = bd[0];
					data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELLAGERSOLL] = bd[1];

				} else {
					if (oArtikelDto.getFLagermindest() != null) {
						nLagermindest = new BigDecimal(oArtikelDto.getFLagermindest());
					}
					data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELLAGERMINDESTSTAND] = nLagermindest;
					if (oArtikelDto.getFLagersoll() != null) {
						nLagersoll = new BigDecimal(oArtikelDto.getFLagersoll());
					}
					data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELLAGERSOLL] = nLagersoll;
				}

				// PJ 14828

				BigDecimal bdLagerstand = new BigDecimal(0);

				if (Helper.short2Boolean(flrbestellung.getFlrartikel().getB_lagerbewirtschaftet())) {
					for (int j = 0; j < lagerDto.length; j++) {

						if (Helper.short2boolean(lagerDto[j].getBBestellvorschlag())) {
							bdLagerstand = bdLagerstand.add(getLagerFac().getLagerstand(oArtikelDto.getIId(),
									lagerDto[j].getIId(), theClientDto));
						}

					}
				}

				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELLAGERSTAND] = bdLagerstand;

				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELEINHEIT] = flrbestellung.getFlrartikel()
						.getEinheit_c_nr();
				if (flrbestellung.getN_nettoeinzelpreis() != null) {
					nArtikelPreis = flrbestellung.getN_nettoeinzelpreis();
				}
				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELPREIS] = nArtikelPreis;

				nAnzahlArtikelRes = getReservierungFac().getAnzahlReservierungen(flrbestellung.getArtikel_i_id(),
						theClientDto);
				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELRESERVIERUNG] = nAnzahlArtikelRes;
				nFehlMenge = getFehlmengeFac().getAnzahlFehlmengeEinesArtikels(flrbestellung.getArtikel_i_id(),
						theClientDto);
				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_ARTIKELFEHLMENGE] = nFehlMenge;

				// Verwendungsnachweis
				Session session2 = FLRSessionFactory.getFactory().openSession();

				String sQueryVerwendungsnachweis = "SELECT sp.flrstueckliste.artikel_i_id, sp.n_menge FROM FLRStuecklisteposition sp WHERE sp.flrartikel.i_id="
						+ flrbestellung.getArtikel_i_id();

				Query q = session2.createQuery(sQueryVerwendungsnachweis);
				List<?> results = q.list();
				Iterator itVW = results.iterator();

				Object[][] dataSub = new Object[results.size()][3];
				String[] fieldnames = new String[] { "F_ARTIKEL", "F_BEZEICHNUNG", "F_MENGE" };
				int iZeileSub = 0;
				while (itVW.hasNext()) {
					Object[] o = (Object[]) itVW.next();
					Integer artikelIId = (Integer) o[0];
					BigDecimal posmenge = (BigDecimal) o[1];

					if (!hmArtikelDtoCache.containsKey(artikelIId)) {
						hmArtikelDtoCache.put(artikelIId,
								getArtikelFac().artikelFindByPrimaryKeySmall(artikelIId, theClientDto));
					}

					ArtikelDto aDto = hmArtikelDtoCache.get(artikelIId);
					dataSub[iZeileSub][0] = aDto.getCNr();
					dataSub[iZeileSub][1] = aDto.formatBezeichnung();
					dataSub[iZeileSub][2] = posmenge;
					iZeileSub++;
				}
				data[i][BestellungReportFac.REPORT_BESTELLVORSCHLAG_SUBREPORT_VERWENDUNGSNACHWEIS] = new LPDatenSubreport(
						dataSub, fieldnames);
				session2.close();

				i++;
			}

			// die Parameter dem Report uebergeben
			HashMap<String, Object> mapParameter = new HashMap<String, Object>();

			mapParameter.put("P_SUBREPORT_LETZTE_EINSTELLUNGEN", getSystemServicesFac().getSubreportLetzteEinstellungen(
					getBestellvorschlagFac().getKEYVALUE_EINSTELLUNGEN_LETZTER_BESTELLVORSCHLAG(theClientDto)));

			if (partnerIIdStandort != null) {
				mapParameter.put("P_STANDORT",
						getPartnerFac().partnerFindByPrimaryKey(partnerIIdStandort, theClientDto).getCKbez());
			}

			mapParameter.put(LPReport.P_SORTIERUNG, buildSortierungBestellvorschlag(krit, theClientDto));
			mapParameter.put(LPReport.P_FILTER, buildFilterBestellungOffene(krit, theClientDto));
			mapParameter.put("P_SORTIERENACHBELEGART",
					new Boolean(krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_BELEGNUMMER));
			mapParameter.put("P_SORTIERENACHIDENT",
					new Boolean(krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_IDENT));
			mapParameter.put(LPReport.P_SORTIERENACHLIEFERANT,
					new Boolean(krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER));

			mapParameter.put(P_MANDANTWAEHRUNG, theClientDto.getSMandantenwaehrung());

			mapParameter.put("P_ANFRAGEVORSCHLAG", new Boolean(bAnfragevorschlag));

			initJRDS(mapParameter, BestellungReportFac.REPORT_MODUL,
					BestellungReportFac.REPORT_BESTELLUNG_JOURNAL_BESTELLVORSCHLAG, theClientDto.getMandant(),
					theClientDto.getLocUi(), theClientDto);

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
	 * Mandanten, die nach den eingegebenen Kriterien des Benutzers zusammengestellt
	 * wird. <br>
	 * Achtung: Hibernate verwendet lazy initialization, d.h. der Zugriff auf
	 * Collections muss innerhalb der Session erfolgen.
	 * 
	 * @param reportJournalKriterienDtoI  die Kriterien des Benutzers
	 * @param dStichtag                   Date
	 * @param bSortierungNachLiefertermin Boolean
	 * @param artikelklasseIId            Integer
	 * @param artikelgruppeIId            Integer
	 * @param theClientDto                der aktuelle Benutzer
	 * @return ReportBestellungOffeneDto[] die Liste der Bestellungen
	 * @throws EJBExceptionLP Ausnahme
	 */
	private ReportBestellungOffeneDto[] getListeReportBestellungOffene(
			ReportJournalKriterienDto reportJournalKriterienDtoI, Date dStichtag, Boolean bSortierungNachLiefertermin,
			Integer artikelklasseIId, Integer artikelgruppeIId, TheClientDto theClientDto) throws EJBExceptionLP {
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
			crit.add(Restrictions.ne(BestellungFac.FLR_BESTELLUNG_BESTELLUNGART_C_NR,
					BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR));
			crit.add(Restrictions.ne(BestellungFac.FLR_BESTELLUNG_BESTELLUNGART_C_NR,
					BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR));
			crit.add(Restrictions.ne(BestellungFac.FLR_BESTELLUNG_BESTELLUNGART_C_NR,
					BestellungFac.BESTELLUNGART_LEIHBESTELLUNG_C_NR));

			// Einschraenkung nach Status Offen, Erledigt
			Collection<String> cStati = new LinkedList<String>();
			cStati.add(BestellungFac.BESTELLSTATUS_OFFEN);
			cStati.add(BestellungFac.BESTELLSTATUS_BESTAETIGT);
			cStati.add(BestellungFac.BESTELLSTATUS_ERLEDIGT);
			crit.add(Restrictions.in(BestellungFac.FLR_BESTELLUNG_BESTELLUNGSTATUS_C_NR, cStati));

			// Das Belegdatum muss vor dem Stichtag liegen
			crit.add(Restrictions.le(BestellungFac.FLR_BESTELLUNG_T_BELEGDATUM, dStichtag));

			// Einschraenkung nach einer bestimmten Kostenstelle
			if (reportJournalKriterienDtoI.kostenstelleIId != null) {
				crit.add(Restrictions.eq(BestellungFac.FLR_BESTELLUNG_KOSTENSTELLE_I_ID,
						reportJournalKriterienDtoI.kostenstelleIId));
			}

			// Einschraenkung nach einem bestimmten Lieferanten
			if (reportJournalKriterienDtoI.lieferantIId != null) {
				crit.add(Restrictions.eq(BestellungFac.FLR_BESTELLUNG_LIEFERANT_I_ID_BESTELLADRESSE,
						reportJournalKriterienDtoI.lieferantIId));
			}

			// Sortierung nach Kostenstelle ist immer die erste Sortierung
			if (reportJournalKriterienDtoI.bSortiereNachKostenstelle) {
				crit.createCriteria(BestellungFac.FLR_BESTELLUNG_FLRKOSTENSTELLE).addOrder(Order.asc("c_nr"));
			}

			// Sortierung nach Kunde, eventuell innerhalb der Kostenstelle
			if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				crit.createCriteria(BestellungFac.FLR_BESTELLUNG_FLRLIEFERANT).createCriteria(LieferantFac.FLR_PARTNER)
						.addOrder(Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));

			}
			// Sortierung nach Projekt, eventuell innerhalb der Kostenstelle
			else if (reportJournalKriterienDtoI.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PROJEKT) {
				crit.addOrder(Order.asc(BestellungFac.FLR_BESTELLUNG_C_BEZPROJEKTBEZEICHNUNG));
			}
			// Sortierung nach Liefertermin (optional)
			if (bSortierungNachLiefertermin != null && bSortierungNachLiefertermin.booleanValue()) {
				crit.addOrder(Order.asc(BestellungFac.FLR_BESTELLUNG_T_LIEFERTERMIN));
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
				Criteria crit1 = session1.createCriteria(FLRBestellposition.class);
				// keine erledigten Positionen.
				crit1.add(Restrictions.ne(BestellpositionFac.FLR_BESTELLPOSITION_BESTELLPOSITIONSTATUS_C_NR,
						BestellpositionFac.BESTELLPOSITIONSTATUS_ERLEDIGT));
				// und nur die, die sich auf den aktuellen Bestellung beziehen.
				Criteria crit1Bestellung = crit1.createCriteria(BestellpositionFac.FLR_BESTELLPOSITION_FLRBESTELLUNG);
				crit1Bestellung.add(Restrictions.eq(BestellungFac.FLR_BESTELLUNG_I_ID, flrbestellung.getI_id()));
				int anzahlPositionen = 0;
				List<?> resultList = crit1.list();
				for (Iterator<?> iter = resultList.iterator(); iter.hasNext();) {
					FLRBestellposition item = (FLRBestellposition) iter.next();
					if (item.getBestellpositionart_c_nr().equals(BestellpositionFac.BESTELLPOSITIONART_IDENT) || item
							.getBestellpositionart_c_nr().equals(BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE)) {
						anzahlPositionen++;
					}
				}

				reportDto = new ReportBestellungOffeneDto();
				reportDto.setIIdBestellung(flrbestellung.getI_id());
				reportDto.setCNrBestellung(flrbestellung.getC_nr());
				reportDto.setLieferantCName1(
						flrbestellung.getFlrlieferant().getFlrpartner().getC_name1nachnamefirmazeile1());
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
	 * @param iIdBestellungI PK der Bestellung
	 * @param iAnzahlKopienI Integer
	 * @param bMitLogo       Boolean
	 * @param theClientDto   der aktuelle Benutzer
	 * @return JasperPrint der Druck
	 * @throws EJBExceptionLP Ausnahme
	 */
	public JasperPrintLP[] printBestellung(Integer iIdBestellungI, Integer iAnzahlKopienI, Boolean bMitLogo,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Timestamp tStart = new Timestamp(System.currentTimeMillis());
		JasperPrintLP[] prints = null;
		if (iIdBestellungI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdBestellungI == null"));
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
			bestellungDto = getBestellungFac().bestellungFindByPrimaryKey(iIdBestellungI);

			aPositionDtos = getBestellpositionFac().bestellpositionFindByBestellung(iIdBestellungI, theClientDto);

			// den Druckzeitpunkt vermerken
			// bestellungDto.setTGedruckt(getTimestamp());
			// getBestellungFac().updateBestellung(bestellungDto, theClientDto);
			// rk: passiert jetzt in BestellungFacBean.aktiviereBestellung()

			LieferantDto lieferantDto = getLieferantFac()
					.lieferantFindByPrimaryKey(bestellungDto.getLieferantIIdBestelladresse(), theClientDto);

			AnsprechpartnerDto oAnsprechpartnerDto = null;

			Integer partnerIIdAnsprechpartner = null;
			if (bestellungDto.getAnsprechpartnerIId() != null) {
				oAnsprechpartnerDto = getAnsprechpartnerFac()
						.ansprechpartnerFindByPrimaryKey(bestellungDto.getAnsprechpartnerIId(), theClientDto);
				if (oAnsprechpartnerDto != null) {
					partnerIIdAnsprechpartner = oAnsprechpartnerDto.getPartnerIIdAnsprechpartner();
				}
			}

			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);

			// es gilt das Locale des Lieferanten
			Locale locDruck = Helper.string2Locale(lieferantDto.getPartnerDto().getLocaleCNrKommunikation());

			int iAnzahlZeilen = aPositionDtos.length; // Anzahl der Zeilen in
			// der Gruppe

			ArrayList alDaten = new ArrayList();

			// die Datenmatrix befuellen
			for (int i = 0; i < iAnzahlZeilen; i++) {

				Object[] oZeile = new Object[REPORT_BESTELLUNG_ANZAHL_SPALTEN];

				oZeile[BestellungReportFac.REPORT_BESTELLUNG_POSITIONOBKJEKT] = getSystemReportFac()
						.getPositionForReport(LocaleFac.BELEGART_BESTELLUNG, aPositionDtos[i].getIId(), theClientDto);
				oZeile[BestellungReportFac.REPORT_BESTELLUNG_POSITION] = getBestellpositionFac()
						.getPositionNummer(aPositionDtos[i].getIId(), theClientDto);

				if (aPositionDtos[i].getTUebersteuerterLiefertermin() != null) {
					Timestamp tposLieferTermin = Helper.cutTimestamp(aPositionDtos[i].getTUebersteuerterLiefertermin());
					if (!(bestellungDto.getDLiefertermin().equals(tposLieferTermin))) {
						bTermineUnterschiedlich = true;
					}
				}

				// Artikelpositionen
				if (aPositionDtos[i].getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_IDENT)
						|| aPositionDtos[i].getPositionsartCNr()
								.equals(BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE)) {

					// Typ, wenn Setartikel

					if (aPositionDtos[i].getPositioniIdArtikelset() != null) {

						oZeile[BestellungReportFac.REPORT_BESTELLUNG_SETARTIKEL_TYP] = ArtikelFac.SETARTIKEL_TYP_POSITION;

					} else {

						Session session = null;
						SessionFactory factory = FLRSessionFactory.getFactory();
						session = factory.openSession();
						Criteria crit = session.createCriteria(FLRBestellposition.class);
						crit.add(Restrictions.eq("position_i_id_artikelset", aPositionDtos[i].getIId()));

						int iZeilen = crit.list().size();

						if (iZeilen > 0) {
							oZeile[BestellungReportFac.REPORT_BESTELLUNG_SETARTIKEL_TYP] = ArtikelFac.SETARTIKEL_TYP_KOPF;
						}
						session.close();

					}

					if (!bestellungDto.getDLiefertermin().equals(aPositionDtos[i].getTUebersteuerterLiefertermin())) {
						oZeile[BestellungReportFac.REPORT_BESTELLUNG_POSITIONTERMIN] = Helper.formatDatum(
								Helper.extractDate(aPositionDtos[i].getTUebersteuerterLiefertermin()), locDruck);

					}

					ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(aPositionDtos[i].getArtikelIId(),
							theClientDto);
					BelegPositionDruckIdentDto druckDto = printIdent(aPositionDtos[i], LocaleFac.BELEGART_BESTELLUNG,
							artikelDto, locDruck, lieferantDto.getPartnerIId(), theClientDto);
					oZeile[BestellungReportFac.REPORT_BESTELLUNG_ARTIKELCZBEZ2] = druckDto
							.getSArtikelZusatzBezeichnung2();
					// Artikel Bezeichnung
					oZeile[BestellungReportFac.REPORT_BESTELLUNG_BEZEICHNUNG] = druckDto.getSBezeichnung();
					// Artikel Kurzbezeichnung
					oZeile[BestellungReportFac.REPORT_BESTELLUNG_KURZBEZEICHNUNG] = druckDto.getSKurzbezeichnung();
					oZeile[BestellungReportFac.REPORT_BESTELLUNG_ZUSATZBEZEICHNUNG] = druckDto.getSZusatzBezeichnung();
					oZeile[BestellungReportFac.REPORT_BESTELLUNG_IDENT] = druckDto.getSArtikelInfo();
					oZeile[BestellungReportFac.REPORT_BESTELLUNG_ARTIKELKOMMENTAR] = druckDto.getSArtikelkommentar();
					oZeile[BestellungReportFac.REPORT_BESTELLUNG_FREIERTEXT] = aPositionDtos[i].getXTextinhalt();

					oZeile[BestellungReportFac.REPORT_BESTELLUNG_REFERENZNUMMER] = artikelDto.getCReferenznr();

					InseratDto insDto = getInseratFac()
							.istInseratAufBestellpositionVorhanden(aPositionDtos[i].getIId());
					oZeile[BestellungReportFac.REPORT_BESTELLUNG_INSERAT_DTO] = insDto;
					if (insDto != null && insDto.getInseratrechnungDto() != null) {
						KundeDto kDto = getKundeFac()
								.kundeFindByPrimaryKey(insDto.getInseratrechnungDto().getKundeIId(), theClientDto);
						oZeile[BestellungReportFac.REPORT_BESTELLUNG_INSERAT_KUNDE] = kDto.getPartnerDto()
								.formatTitelAnrede();
					}

					// Wenn Bezug zu Lossollmaterial (PJ 16215)
					if (aPositionDtos[i].getLossollmaterialIId() != null) {
						LossollmaterialDto lossollmaterialDto = getFertigungFac()
								.lossollmaterialFindByPrimaryKey(aPositionDtos[i].getLossollmaterialIId());

						LosDto losDto = getFertigungFac().losFindByPrimaryKey(lossollmaterialDto.getLosIId());

						oZeile[BestellungReportFac.REPORT_BESTELLUNG_SOLLMATERIAL_KOMMENTAR] = lossollmaterialDto
								.getCKommentar();
						oZeile[BestellungReportFac.REPORT_BESTELLUNG_SOLLMATERIAL_LOSNUMMER] = losDto.getCNr();
						oZeile[BestellungReportFac.REPORT_BESTELLUNG_SOLLMATERIAL_LOS_KOMMENTAR] = losDto
								.getCKommentar();
						oZeile[BestellungReportFac.REPORT_BESTELLUNG_SOLLMATERIAL_LOS_PROJEKT] = losDto.getCProjekt();

						if (losDto.getStuecklisteIId() != null) {

							StuecklisteDto stklDto = getStuecklisteFac()
									.stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(), theClientDto);
							oZeile[BestellungReportFac.REPORT_BESTELLUNG_SOLLMATERIAL_LOS_STKLNUMMER] = stklDto
									.getArtikelDto().getCNr();
							if (stklDto.getArtikelDto().getArtikelsprDto() != null) {
								oZeile[BestellungReportFac.REPORT_BESTELLUNG_SOLLMATERIAL_LOS_STKLBEZ] = stklDto
										.getArtikelDto().getArtikelsprDto().getCBez();
								oZeile[BestellungReportFac.REPORT_BESTELLUNG_SOLLMATERIAL_LOS_STKLKBEZ] = stklDto
										.getArtikelDto().getArtikelsprDto().getCKbez();
							}

						}

						LossollarbeitsplanDto[] sollarbeitsplanDtos = getFertigungFac()
								.lossollarbeitsplanFindByLossollmaterialIId(lossollmaterialDto.getIId(), theClientDto);

						Object[][] oSubData = new Object[sollarbeitsplanDtos.length][13];

						for (int s = 0; s < sollarbeitsplanDtos.length; s++) {
							LossollarbeitsplanDto lossollarbeitsplanDto = sollarbeitsplanDtos[s];

							ArtikelDto artikelDtoSollarbeitsplan = getArtikelFac().artikelFindByPrimaryKeySmall(
									lossollarbeitsplanDto.getArtikelIIdTaetigkeit(), theClientDto);
							oSubData[s][0] = artikelDtoSollarbeitsplan.getCNr();
							oSubData[s][1] = artikelDtoSollarbeitsplan.formatBezeichnung();

							oSubData[s][2] = new BigDecimal(lossollarbeitsplanDto.getLStueckzeit())
									.divide(new BigDecimal(3600000), 4, BigDecimal.ROUND_HALF_EVEN);
							oSubData[s][3] = new BigDecimal(lossollarbeitsplanDto.getLRuestzeit())
									.divide(new BigDecimal(3600000), 4, BigDecimal.ROUND_HALF_EVEN);

							oSubData[s][4] = lossollarbeitsplanDto.getIArbeitsgangnummer();
							oSubData[s][5] = lossollarbeitsplanDto.getIUnterarbeitsgang();
							oSubData[s][6] = lossollarbeitsplanDto.getCKomentar();
							oSubData[s][7] = lossollarbeitsplanDto.getXText();

						}

						String[] fieldnames = new String[] { "F_ARTIKEL", "F_BEZEICHNUNG", "F_STUECKZEIT",
								"F_RUESTZEIT", "F_AGNUMMER", "F_UAGNUMMER", "F_KOMMENTAR", "F_TEXT" };
						oZeile[BestellungReportFac.REPORT_BESTELLUNG_SOLLMATERIAL_SUBREPORT_ARBEITSGAENGE] = new LPDatenSubreport(
								oSubData, fieldnames);

					}

					// Lieferantendaten: Artikelnummer, Bezeichnung
					ArtikellieferantDto artikellieferantDto = getArtikelFac().getArtikelEinkaufspreisMitOptionGebinde(
							aPositionDtos[i].getArtikelIId(), bestellungDto.getLieferantIIdBestelladresse(),
							aPositionDtos[i].getNMenge(),

							bestellungDto.getWaehrungCNr(), new java.sql.Date(bestellungDto.getDBelegdatum().getTime()),
							aPositionDtos[i].getGebindeIId(), theClientDto);

					// SP3541
					if (artikellieferantDto == null) {
						artikellieferantDto = getArtikelFac()
								.artikellieferantFindByArtikellIIdLieferantIIdTPreisgueltigabKleiner(
										aPositionDtos[i].getArtikelIId(), bestellungDto.getLieferantIIdBestelladresse(),
										new java.sql.Date(bestellungDto.getDBelegdatum().getTime()),
										aPositionDtos[i].getGebindeIId(), theClientDto);
					}

					if (artikellieferantDto != null) {

						if (Helper.short2boolean(artikellieferantDto.getBHerstellerbez()) == true) {
							// PJ18293
							oZeile[BestellungReportFac.REPORT_BESTELLUNG_LIEFERANT_ARTIKEL_IDENTNUMMER] = artikelDto
									.getCArtikelnrhersteller();
							oZeile[BestellungReportFac.REPORT_BESTELLUNG_LIEFERANT_ARTIKEL_BEZEICHNUNG] = artikelDto
									.getCArtikelbezhersteller();

						} else {
							if (artikellieferantDto.getCArtikelnrlieferant() != null) {
								oZeile[BestellungReportFac.REPORT_BESTELLUNG_LIEFERANT_ARTIKEL_IDENTNUMMER] = artikellieferantDto
										.getCArtikelnrlieferant();
							}
							if (artikellieferantDto.getCBezbeilieferant() != null) {
								oZeile[BestellungReportFac.REPORT_BESTELLUNG_LIEFERANT_ARTIKEL_BEZEICHNUNG] = artikellieferantDto
										.getCBezbeilieferant();
							}
						}

						oZeile[BestellungReportFac.REPORT_BESTELLUNG_LIEFERANT_VERPACKUNGSEINHEIT] = artikellieferantDto
								.getNVerpackungseinheit();

					}

					if (artikelDto.getHerstellerIId() != null) {
						HerstellerDto herstellerDto = getArtikelFac()
								.herstellerFindByPrimaryKey(artikelDto.getHerstellerIId(), theClientDto);
						oZeile[BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_HERSTELLER] = herstellerDto.getCNr();
						PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(herstellerDto.getPartnerIId(),
								theClientDto);
						oZeile[BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_HERSTELLER_NAME] = partnerDto
								.formatFixName1Name2();
						oZeile[BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_HERSTELLER_KURZBEZEICHNUNG] = partnerDto
								.getCKbez();
					}

					if (artikelDto.getFGewichtkg() != null) {
						oZeile[BestellungReportFac.REPORT_BESTELLUNG_ARTIKELGEWICHT] = new BigDecimal(
								artikelDto.getFGewichtkg()).multiply(aPositionDtos[i].getNMenge());
					}

					BigDecimal bdUmrechnungsfaktor = artikelDto.getNUmrechnungsfaktor();
					boolean bInvers = Helper.short2boolean(artikelDto.getbBestellmengeneinheitInvers());
					String einheit = artikelDto.getEinheitCNrBestellung();
					if (bdUmrechnungsfaktor == null && artikellieferantDto != null) {
						bdUmrechnungsfaktor = artikellieferantDto.getNVerpackungseinheit();
						einheit = artikellieferantDto.getEinheitCNrVpe();
						bInvers = true;
					}

					if (bdUmrechnungsfaktor != null && bdUmrechnungsfaktor.doubleValue() != 0 && einheit != null) {

						BigDecimal gewicht = null;

						if (bInvers) {
							gewicht = aPositionDtos[i].getNMenge().divide(bdUmrechnungsfaktor, 4,
									BigDecimal.ROUND_HALF_EVEN);
						} else {
							gewicht = aPositionDtos[i].getNMenge().multiply(bdUmrechnungsfaktor);
						}

						BigDecimal umrechnung = new BigDecimal(0);
						gewicht = Helper.rundeKaufmaennisch(gewicht, 4);
						oZeile[BestellungReportFac.REPORT_BESTELLUNG_GEWICHT] = gewicht;
						oZeile[BestellungReportFac.REPORT_BESTELLUNG_GEWICHTEINHEIT] = getSystemFac()
								.formatEinheit(einheit, locDruck, theClientDto);

						if (bInvers) {
							umrechnung = aPositionDtos[i].getNNettogesamtPreisminusRabatte()
									.multiply(bdUmrechnungsfaktor);
						} else {
							umrechnung = aPositionDtos[i].getNNettogesamtPreisminusRabatte().divide(bdUmrechnungsfaktor,
									4, BigDecimal.ROUND_HALF_EVEN);
						}

						if (getTheJudgeFac().hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_EINKAUF, theClientDto)) {
							oZeile[BestellungReportFac.REPORT_BESTELLUNG_PREISPEREINHEIT] = umrechnung;
						} else {
							oZeile[BestellungReportFac.REPORT_BESTELLUNG_PREISPEREINHEIT] = null;
						}
					}

					// PJ 14867
					if (aPositionDtos[i].getIBestellpositionIIdRahmenposition() != null && bestellungDto
							.getBestellungartCNr().equals(BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR)) {
						Session sessionRahmen = FLRSessionFactory.getFactory().openSession();

						String query = "FROM FLRBestellpositionReport bs WHERE bs.i_id="
								+ aPositionDtos[i].getIBestellpositionIIdRahmenposition()
								+ " ORDER BY bs.flrbestellung.t_belegdatum ASC";

						Query q = sessionRahmen.createQuery(query);

						List l = q.list();

						if (l.size() > 0) {
							FLRBestellpositionReport eintrag = (FLRBestellpositionReport) l.iterator().next();

							oZeile[BestellungReportFac.REPORT_BESTELLUNG_RAHMENMENGE] = eintrag.getN_menge();

							BigDecimal offeneMenge = eintrag.getN_menge();
							BigDecimal abgerufeneMenge = BigDecimal.ZERO;

							Timestamp tLetzteLieferung = null;

							for (Iterator<?> iter = eintrag.getAbrufpositionenset().iterator(); iter.hasNext();) {
								FLRBestellpositionReport item = (FLRBestellpositionReport) iter.next();
								FLRBestellung flrBestellung = item.getFlrbestellung();
								if (!BestellungFac.BESTELLSTATUS_STORNIERT
										.equals(flrBestellung.getBestellungstatus_c_nr())
										&& flrBestellung.getT_belegdatum()
												.compareTo(bestellungDto.getDBelegdatum()) <= 0
										&& flrBestellung.getI_id() < bestellungDto.getIId()) {
									offeneMenge = offeneMenge.subtract(item.getN_menge());
									abgerufeneMenge = abgerufeneMenge.add(item.getN_menge());

									if (item.getFlrbestellung().getI_id().intValue() != bestellungDto.getIId()
											.intValue()) {

										Timestamp t = new java.sql.Timestamp(
												item.getFlrbestellung().getT_belegdatum().getTime());

										if (tLetzteLieferung == null) {
											tLetzteLieferung = t;
										} else {
											if (t.after(tLetzteLieferung)) {
												tLetzteLieferung = t;
											}

										}
									}

								}
							}

							oZeile[BestellungReportFac.REPORT_BESTELLUNG_OFFENERAHMENMENGE] = offeneMenge;
							oZeile[BestellungReportFac.REPORT_BESTELLUNG_ABGERUFENE_MENGE] = abgerufeneMenge;
							oZeile[BestellungReportFac.REPORT_BESTELLUNG_LETZTER_ABRUF] = tLetzteLieferung;

						}

						sessionRahmen.close();

					}

					if (aPositionDtos[i].getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_IDENT)) {
						oZeile[BestellungReportFac.REPORT_BESTELLUNG_IDENTNUMMER] = artikelDto.getCNr();
						oZeile[BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_INDEX] = artikelDto.getCIndex();
						oZeile[BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_REVISION] = artikelDto.getCRevision();

						if (artikelDto.getVerpackungDto() != null) {
							oZeile[BestellungReportFac.REPORT_BESTELLUNG_BAUFORM] = artikelDto.getVerpackungDto()
									.getCBauform();
							oZeile[BestellungReportFac.REPORT_BESTELLUNG_VERPACKUNGSART] = artikelDto.getVerpackungDto()
									.getCVerpackungsart();
						}
						if (artikelDto.getMaterialIId() != null) {
							MaterialDto materialDto = getMaterialFac()
									.materialFindByPrimaryKey(artikelDto.getMaterialIId(), locDruck, theClientDto);
							if (materialDto.getMaterialsprDto() != null
									&& materialDto.getMaterialsprDto().getCBez() != null) {

								oZeile[BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_MATERIAL] = materialDto
										.getMaterialsprDto().getCBez();
							} else {
								oZeile[BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_MATERIAL] = materialDto.getCNr();
							}

							MaterialzuschlagDto mzDto = getMaterialFac().getKursMaterialzuschlagDtoInZielwaehrung(
									artikelDto.getMaterialIId(), bestellungDto.getDBelegdatum(),
									bestellungDto.getWaehrungCNr(), theClientDto);

							oZeile[BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_KURS_MATERIALZUSCHLAG] = mzDto
									.getNZuschlag();
							oZeile[BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_DATUM_MATERIALZUSCHLAG] = mzDto
									.getTGueltigab();

						}

						oZeile[BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_MATERIALGEWICHT] = artikelDto
								.getFMaterialgewicht();

						if (artikelDto.getGeometrieDto() != null) {
							oZeile[BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_BREITE] = artikelDto.getGeometrieDto()
									.getFBreite();
							oZeile[BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_HOEHE] = artikelDto.getGeometrieDto()
									.getFHoehe();
							oZeile[BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_TIEFE] = artikelDto.getGeometrieDto()
									.getFTiefe();
						}

						oZeile[BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_HERSTELLERNUMMER] = artikelDto
								.getCArtikelnrhersteller();
						oZeile[BestellungReportFac.REPORT_BESTELLUNG_ARTIKEL_HERSTELLERBEZEICHNUNG] = artikelDto
								.getCArtikelbezhersteller();

					}
					oZeile[BestellungReportFac.REPORT_BESTELLUNG_MENGE] = aPositionDtos[i].getNMenge();

					if (aPositionDtos[i].getNOffeneMenge() != null && aPositionDtos[i].getNMenge() != null) {
						oZeile[BestellungReportFac.REPORT_BESTELLUNG_ERHALTENE_MENGE] = aPositionDtos[i].getNMenge()
								.subtract(aPositionDtos[i].getNOffeneMenge());
					}

					oZeile[BestellungReportFac.REPORT_BESTELLUNG_ANZAHL_GEBINDE] = aPositionDtos[i].getNAnzahlgebinde();

					if (aPositionDtos[i].getGebindeIId() != null) {
						oZeile[BestellungReportFac.REPORT_BESTELLUNG_GEBINDENAME] = getArtikelFac()
								.gebindeFindByPrimaryKey(aPositionDtos[i].getGebindeIId()).getCBez();
					}

					oZeile[BestellungReportFac.REPORT_BESTELLUNG_EINHEIT] = getSystemFac()
							.formatEinheit(aPositionDtos[i].getEinheitCNr(), locDruck, theClientDto);

					oZeile[BestellungReportFac.REPORT_BESTELLUNG_ANGEBOTSNUMMER] = aPositionDtos[i].getCAngebotnummer();

					if (aPositionDtos[i].getPositioniIdArtikelset() == null) {
						if (getTheJudgeFac().hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_EINKAUF, theClientDto)) {
							oZeile[BestellungReportFac.REPORT_BESTELLUNG_EINZELPREIS] = aPositionDtos[i]
									.getNNettoeinzelpreis();
							oZeile[BestellungReportFac.REPORT_BESTELLUNG_MATERIALZUSCHLAG] = aPositionDtos[i]
									.getNMaterialzuschlag();

							oZeile[BestellungReportFac.REPORT_BESTELLUNG_RABATT] = aPositionDtos[i].getDRabattsatz();

							BigDecimal bdGesamtpreis = null;

							if (aPositionDtos[i].getNNettogesamtpreis() != null
									&& aPositionDtos[i].getNMenge() != null) {
								bdGesamtpreis = aPositionDtos[i].getNNettogesamtpreis()
										.multiply(aPositionDtos[i].getNMenge());
							}

							oZeile[BestellungReportFac.REPORT_BESTELLUNG_NETTOEINZELPREIS] = aPositionDtos[i]
									.getNNettogesamtpreis();

							oZeile[BestellungReportFac.REPORT_BESTELLUNG_GESAMTPREIS] = bdGesamtpreis;
						} else {
							oZeile[BestellungReportFac.REPORT_BESTELLUNG_EINZELPREIS] = null;
							oZeile[BestellungReportFac.REPORT_BESTELLUNG_MATERIALZUSCHLAG] = null;
							oZeile[BestellungReportFac.REPORT_BESTELLUNG_RABATT] = null;
							oZeile[BestellungReportFac.REPORT_BESTELLUNG_GESAMTPREIS] = null;
						}

					}
					/**
					 * @todo MR-> Wird nicht mehr benoetigt, da LPReport.printIdent das abbildet.
					 *       Wegen kurzer Zeit vor deploy nicht mehr geloescht.
					 */
					ArrayList<Object> images = new ArrayList<Object>();
					ArtikelkommentarDto[] artikelkommentarDto = getArtikelkommentarFac()
							.artikelkommentardruckFindByArtikelIIdBelegartCNr(aPositionDtos[i].getArtikelIId(),
									LocaleFac.BELEGART_BESTELLUNG, theClientDto.getLocUiAsString(), theClientDto);

					if (artikelkommentarDto != null && artikelkommentarDto.length > 0) {
						String cPositionKommentarText = "";

						for (int j = 0; j < artikelkommentarDto.length; j++) {
							if (artikelkommentarDto[j].getArtikelkommentarsprDto() != null) {
								String cDatenformat = artikelkommentarDto[j].getDatenformatCNr();
								if (cDatenformat.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG)
										|| artikelkommentarDto[j].getDatenformatCNr()
												.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG)
										|| artikelkommentarDto[j].getDatenformatCNr()
												.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF)) {
									byte[] bild = artikelkommentarDto[j].getArtikelkommentarsprDto().getOMedia();
									if (bild != null) {
										java.awt.Image myImage = Helper.byteArrayToImage(bild);
										oZeile[BestellungReportFac.REPORT_BESTELLUNG_IMAGE] = myImage;
										images.add(myImage);
									}
								} else if (artikelkommentarDto[j].getDatenformatCNr()
										.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF)) {

									byte[] bild = artikelkommentarDto[j].getArtikelkommentarsprDto().getOMedia();

									java.awt.Image[] tiffs = Helper.tiffToImageArray(bild);
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
				if (aPositionDtos[i].getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_BETRIFFT)) {
					oZeile[BestellungReportFac.REPORT_BESTELLUNG_FREIERTEXT] = aPositionDtos[i].getCBez();
				}

				// Texteingabe Positionen
				if (aPositionDtos[i].getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_TEXTEINGABE)) {
					// IMS 1619 leerer Text soll als Leerezeile erscheinen
					String sText = aPositionDtos[i].getXTextinhalt();

					if (sText != null && sText.trim().equals("")) {
						oZeile[BestellungReportFac.REPORT_BESTELLUNG_LEERZEILE] = " ";
					} else {
						oZeile[BestellungReportFac.REPORT_BESTELLUNG_FREIERTEXT] = sText;
					}
				}

				// Textbaustein Positionen
				if (aPositionDtos[i].getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_TEXTBAUSTEIN)) {
					// Dto holen
					MediastandardDto oMediastandardDto = getMediaFac()
							.mediastandardFindByPrimaryKey(aPositionDtos[i].getMediastandardIId());
					// zum Drucken vorbereiten
					BelegPositionDruckTextbausteinDto druckDto = printTextbaustein(oMediastandardDto, theClientDto);
					oZeile[BestellungReportFac.REPORT_BESTELLUNG_FREIERTEXT] = druckDto.getSFreierText();
					oZeile[BestellungReportFac.REPORT_BESTELLUNG_IMAGE] = druckDto.getOImage();
				}

				// Leerzeile Positionen
				if (aPositionDtos[i].getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_LEERZEILE)) {
					oZeile[BestellungReportFac.REPORT_BESTELLUNG_LEERZEILE] = " ";
				}

				// Seitenumbruch Positionen
				if (aPositionDtos[i].getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_SEITENUMBRUCH)) {
					bbSeitenumbruch = new Boolean(!bbSeitenumbruch.booleanValue()); // toggle
				}

				oZeile[BestellungReportFac.REPORT_BESTELLUNG_SEITENUMBRUCH] = bbSeitenumbruch;
				oZeile[BestellungReportFac.REPORT_BESTELLUNG_POSITIONSART] = aPositionDtos[i].getPositionsartCNr();
				alDaten.add(oZeile);

				// PJ21437

				if (aPositionDtos[i].getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_IDENT)
						&& aPositionDtos[i].getNFixkosten() != null) {

					Object[] oZeileFixkosten = new Object[REPORT_BESTELLUNG_ANZAHL_SPALTEN];

					oZeileFixkosten[BestellungReportFac.REPORT_BESTELLUNG_POSITIONSART] = AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE;

					oZeileFixkosten[BestellungReportFac.REPORT_BESTELLUNG_IDENT] = "Fixkosten";
					oZeileFixkosten[BestellungReportFac.REPORT_BESTELLUNG_BEZEICHNUNG] = "Fixkosten";
					oZeileFixkosten[BestellungReportFac.REPORT_BESTELLUNG_SEITENUMBRUCH] = bbSeitenumbruch;
					oZeileFixkosten[BestellungReportFac.REPORT_BESTELLUNG_EINZELPREIS] = aPositionDtos[i]
							.getNFixkosten();
					oZeileFixkosten[BestellungReportFac.REPORT_BESTELLUNG_RABATT] = 0D;
					oZeileFixkosten[BestellungReportFac.REPORT_BESTELLUNG_MENGE] = new BigDecimal(1);
					oZeileFixkosten[BestellungReportFac.REPORT_BESTELLUNG_GESAMTPREIS] = aPositionDtos[i]
							.getNFixkosten();

					alDaten.add(oZeileFixkosten);

				}

			}

			if (bestellungDto.getNKorrekturbetrag() != null && bestellungDto.getNKorrekturbetrag().doubleValue() != 0) {
				// PJ19376 Pauschalkorrektur hinzufuegen
				Object[] oZeilePauschalkorrektur = new Object[REPORT_BESTELLUNG_ANZAHL_SPALTEN];

				oZeilePauschalkorrektur[BestellungReportFac.REPORT_BESTELLUNG_POSITIONSART] = AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE;

				oZeilePauschalkorrektur[BestellungReportFac.REPORT_BESTELLUNG_IDENT] = "Pauschalkorrektur";
				oZeilePauschalkorrektur[BestellungReportFac.REPORT_BESTELLUNG_BEZEICHNUNG] = "Pauschalkorrektur";
				oZeilePauschalkorrektur[BestellungReportFac.REPORT_BESTELLUNG_SEITENUMBRUCH] = bbSeitenumbruch;
				oZeilePauschalkorrektur[BestellungReportFac.REPORT_BESTELLUNG_EINZELPREIS] = bestellungDto
						.getNKorrekturbetrag();
				oZeilePauschalkorrektur[BestellungReportFac.REPORT_BESTELLUNG_RABATT] = 0D;
				oZeilePauschalkorrektur[BestellungReportFac.REPORT_BESTELLUNG_MENGE] = new BigDecimal(1);
				oZeilePauschalkorrektur[BestellungReportFac.REPORT_BESTELLUNG_GESAMTPREIS] = bestellungDto
						.getNKorrekturbetrag();

				alDaten.add(oZeilePauschalkorrektur);
			}

			data = new Object[alDaten.size()][BestellungReportFac.REPORT_BESTELLUNG_ANZAHL_SPALTEN];

			data = (Object[][]) alDaten.toArray(data);

			// Kopftext
			String sKopftext = bestellungDto.getCKopftextUebersteuert();

			if (sKopftext == null || sKopftext.length() == 0) {
				BestellungtextDto oBestellungtextDto = getBestellungServiceFac().bestellungtextFindByMandantLocaleCNr(
						theClientDto.getMandant(), lieferantDto.getPartnerDto().getLocaleCNrKommunikation(),
						BestellungServiceFac.BESTELLUNGTEXT_KOPFTEXT, theClientDto);
				sKopftext = oBestellungtextDto.getXTextinhalt();
			}

			// Fusstext
			String sFusstext = bestellungDto.getCFusstextUebersteuert();

			if (sFusstext == null || sFusstext.length() == 0) {
				BestellungtextDto oBestellungtextDto = getBestellungServiceFac().bestellungtextFindByMandantLocaleCNr(
						theClientDto.getMandant(), lieferantDto.getPartnerDto().getLocaleCNrKommunikation(),
						BestellungServiceFac.BESTELLUNGTEXT_FUSSTEXT, theClientDto);

				sFusstext = oBestellungtextDto.getXTextinhalt();
			}

			// Erstellung des Report
			HashMap<String, Object> mapParameter = new HashMap<String, Object>();

			mapParameter.put("P_SUBREPORT_LIEFERGRUPPENTEXTE",
					getSubreportLiefergruppenTexte(aPositionDtos, theClientDto));
			// PJ19209
			mapParameter.put("P_KUPFERZAHL", lieferantDto.getNKupferzahl());

			// PJ20323
			mapParameter.put("P_LKZ_LIEFERANT", lieferantDto.getPartnerDto().formatLKZ());

			// PJ19814
			mapParameter.put("P_ZUSCHLAG_INKLUSIVE", Helper.short2Boolean(lieferantDto.getBZuschlagInklusive()));

			// CK: PJ 13849
			mapParameter.put("P_BEARBEITER",
					getPersonalFac().getPersonRpt(bestellungDto.getPersonalIIdAnlegen(), theClientDto));

			// PJ19360
			mapParameter.put("P_PERSON_AENDERN",
					getPersonalFac().getPersonRpt(bestellungDto.getPersonalIIdAendern(), theClientDto));
			// die Parameter uebergeben
			if (bestellungDto.getBTeillieferungMoeglich() != null) {
				mapParameter.put("P_TEILLIEFERUNGMOEGLICH",
						new Boolean(Helper.short2boolean(bestellungDto.getBTeillieferungMoeglich())));
			}

			String sSpediteur = null;
			if (bestellungDto.getSpediteurIId() != null) {
				SpediteurDto spediteurDto = getMandantFac().spediteurFindByPrimaryKey(bestellungDto.getSpediteurIId());
				sSpediteur = spediteurDto.getCNamedesspediteurs();

				if (spediteurDto.getPartnerIId() != null) {
					PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(spediteurDto.getPartnerIId(),
							theClientDto);

					AnsprechpartnerDto ansprechpartnerDtoSpediteur = null;

					if (spediteurDto.getAnsprechpartnerIId() != null) {
						ansprechpartnerDtoSpediteur = getAnsprechpartnerFac()
								.ansprechpartnerFindByPrimaryKey(spediteurDto.getAnsprechpartnerIId(), theClientDto);
					}

					mapParameter.put("P_SPEDITEUR_ADRESSBLOCK",
							formatAdresseFuerAusdruck(partnerDto, ansprechpartnerDtoSpediteur, mandantDto, locDruck));
				}

			}
			mapParameter.put("P_SPEDITEUR", sSpediteur != null ? sSpediteur : "");

			mapParameter.put("P_LIEFERANTENANGEBOT", bestellungDto.getCLieferantenangebot());

			mapParameter.put("P_LIEFERANTINTERNERKOMMENTAR", lieferantDto.getCHinweisintern());
			mapParameter.put("P_LIEFERANTEXTERNERKOMMENTAR", lieferantDto.getCHinweisextern());

			if (bestellungDto.getAuftragIId() != null) {
				AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(bestellungDto.getAuftragIId());
				mapParameter.put("P_AUFTRAGSNUMMER", auftragDto.getCNr());
			}
			if (bestellungDto.getStatusCNr().equals(BestellpositionFac.BESTELLPOSITIONSTATUS_STORNIERT)) {
				mapParameter.put("P_STORNIERT", true);
			} else {
				mapParameter.put("P_STORNIERT", false);
			}
			mapParameter.put("Mandantenadresse", Helper.formatMandantAdresse(mandantDto));
			mapParameter.put("P_LIEFERADRESSE_IST_NICHT_MANDANT", new Boolean(true));

			if (bestellungDto.getPartnerIIdLieferadresse() != null) {
				PartnerDto lieferadresseDto = getPartnerFac()
						.partnerFindByPrimaryKey(bestellungDto.getPartnerIIdLieferadresse(), theClientDto);

				// SP581

				if (lieferantDto.getPartnerDto().getLandplzortDto() != null
						&& lieferadresseDto.getLandplzortDto() != null
						&& !lieferantDto.getPartnerDto().getLandplzortDto().getLandDto().getIID()
								.equals(lieferadresseDto.getLandplzortDto().getLandDto().getIID())) {

					if (lieferadresseDto.getLandplzortDto().getLandDto().getLandIIdGemeinsamespostland() != null
							&& lieferadresseDto.getLandplzortDto().getLandDto().getLandIIdGemeinsamespostland()
									.equals(lieferantDto.getPartnerDto().getLandplzortDto().getIlandID())) {
						mapParameter.put("P_LIEFERADRESSE",
								formatAdresseFuerAusdruck(lieferadresseDto, null, mandantDto,
										Helper.string2Locale(lieferadresseDto.getLocaleCNrKommunikation()),
										LocaleFac.BELEGART_BESTELLUNG));
					} else if (lieferantDto.getPartnerDto().getLandplzortDto().getLandDto()
							.getLandIIdGemeinsamespostland() != null
							&& lieferantDto.getPartnerDto().getLandplzortDto().getLandDto()
									.getLandIIdGemeinsamespostland()
									.equals(lieferadresseDto.getLandplzortDto().getIlandID())) {
						mapParameter.put("P_LIEFERADRESSE",
								formatAdresseFuerAusdruck(lieferadresseDto, null, mandantDto,
										Helper.string2Locale(lieferadresseDto.getLocaleCNrKommunikation()),
										LocaleFac.BELEGART_BESTELLUNG));
					} else {
						mapParameter.put("P_LIEFERADRESSE",
								formatAdresseFuerAusdruck(lieferadresseDto, null, mandantDto,
										Helper.string2Locale(lieferadresseDto.getLocaleCNrKommunikation()), true,
										LocaleFac.BELEGART_BESTELLUNG, false));
					}

				} else {
					mapParameter.put("P_LIEFERADRESSE",
							formatAdresseFuerAusdruck(lieferadresseDto, null, mandantDto,
									Helper.string2Locale(lieferadresseDto.getLocaleCNrKommunikation()),
									LocaleFac.BELEGART_BESTELLUNG));
				}

				mapParameter.put("P_LIEFERADRESSENICHTNULL", new Boolean(true));

				if (mandantDto.getPartnerIId().equals(bestellungDto.getPartnerIIdLieferadresse())) {
					mapParameter.put("P_LIEFERADRESSE_IST_NICHT_MANDANT", new Boolean(false));
				}

				if (bestellungDto.getAnsprechpartnerIIdLieferadresse() != null) {
					AnsprechpartnerDto ansprechpartnerLieferadresseDto = getAnsprechpartnerFac()
							.ansprechpartnerFindByPrimaryKey(bestellungDto.getAnsprechpartnerIIdLieferadresse(),
									theClientDto);
					mapParameter.put("P_ANSPRECHPARTNER_LIEFERADRESSE",
							getPartnerFac().formatFixAnredeTitelName2Name1FuerAdresskopf(
									ansprechpartnerLieferadresseDto.getPartnerDto(),
									Helper.string2Locale(lieferadresseDto.getLocaleCNrKommunikation()), null));
				}

			}
			if (lieferantDto.getPartnerRechnungsadresseDto() != null) {
				mapParameter.put("P_RECHNUNGADRESSE",
						formatAdresseFuerAusdruck(lieferantDto.getPartnerRechnungsadresseDto(), oAnsprechpartnerDto,
								mandantDto,
								Helper.string2Locale(
										lieferantDto.getPartnerRechnungsadresseDto().getLocaleCNrKommunikation()),
								LocaleFac.BELEGART_BESTELLUNG));
				if (oAnsprechpartnerDto != null) {
					mapParameter.put("P_ANSPRECHPARTNER_RECHNUNGADRESSE",
							getPartnerFac().formatFixAnredeTitelName2Name1FuerAdresskopf(
									oAnsprechpartnerDto.getPartnerDto(),
									Helper.string2Locale(
											lieferantDto.getPartnerRechnungsadresseDto().getLocaleCNrKommunikation()),
									null));
				}
			}

			ParametermandantDto parameterDtoVorsteuer = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_VORSTEUER_BEI_WE_EINSTANDSPREIS,
					new Timestamp(bestellungDto.getDBelegdatum().getTime()));
			boolean bVorsteuer = (Boolean) parameterDtoVorsteuer.getCWertAsObject();

			mapParameter.put("P_PARAMETER_VORSTEUER_BEI_WE_EINSTANDSPREIS", bVorsteuer);
			if (bestellungDto.getLieferantIIdRechnungsadresse() != null) {

				LieferantDto lieferantDtoRechnungsadresse = getLieferantFac()
						.lieferantFindByPrimaryKey(bestellungDto.getLieferantIIdRechnungsadresse(), theClientDto);
				if (lieferantDtoRechnungsadresse.getMwstsatzbezIId() != null) {
					MwstsatzbezDto bezDto = getMandantFac().mwstsatzbezFindByPrimaryKey(
							lieferantDtoRechnungsadresse.getMwstsatzbezIId(), theClientDto);
					mapParameter.put("P_MWST_BEZEICHNUNG", bezDto.getCBezeichnung());
					MwstsatzDto mwstsatzDtoZumBelegdatum = getMandantFac().mwstsatzFindZuDatum(
							lieferantDtoRechnungsadresse.getMwstsatzbezIId(),
							new Timestamp(bestellungDto.getDBelegdatum().getTime()));
					if (mwstsatzDtoZumBelegdatum != null) {
						mapParameter.put("P_MWSTSATZ_ZUM_BELEGDATUM", mwstsatzDtoZumBelegdatum.getFMwstsatz());
					}
				}

			}

			mapParameter.put("P_SUBREPORT_PARTNERKOMMENTAR",
					getPartnerServicesFacLocal().getSubreportAllerMitzudruckendenPartnerkommentare(
							lieferantDto.getPartnerDto().getIId(), false, LocaleFac.BELEGART_BESTELLUNG, theClientDto));

			mapParameter.put("Adressefuerausdruck",
					formatAdresseFuerAusdruck(lieferantDto.getPartnerDto(), oAnsprechpartnerDto, mandantDto,
							Helper.string2Locale(lieferantDto.getPartnerDto().getLocaleCNrKommunikation()),
							LocaleFac.BELEGART_BESTELLUNG));

			if (oAnsprechpartnerDto != null) {
				mapParameter.put("P_ANSPRECHPARTNER_ADRESSBLOCK",
						getPartnerFac().formatFixAnredeTitelName2Name1FuerAdresskopf(
								oAnsprechpartnerDto.getPartnerDto(),
								Helper.string2Locale(lieferantDto.getPartnerDto().getLocaleCNrKommunikation()), null));
			}

			if (bestellungDto.getAnfrageIId() != null) {
				AnfrageDto anfrageDto = getAnfrageFac().anfrageFindByPrimaryKey(bestellungDto.getAnfrageIId(),
						theClientDto);
				mapParameter.put("P_ANFRAGENUMMER", anfrageDto.getCNr());

				mapParameter.put("P_ANFRAGE_ANGEBOTSNUMMER", anfrageDto.getCAngebotnummer());
				mapParameter.put("P_ANFRAGE_ANGEBOTSDATUM", anfrageDto.getTAngebotdatum());

			}

			/**
			 * @todo fuer diesen Block eine zentrale Methode bauen.
			 */

			// die partnerIId des ansprechpartners brauch ich
			// daten holen
			String sEmail = getPartnerFac().partnerkommFindRespectPartnerAsStringOhneExec(
					bestellungDto.getAnsprechpartnerIId(), lieferantDto.getPartnerDto(),
					PartnerFac.KOMMUNIKATIONSART_EMAIL, theClientDto.getMandant(), theClientDto);

			String sFax = getPartnerFac().partnerkommFindRespectPartnerAsStringOhneExec(
					bestellungDto.getAnsprechpartnerIId(), lieferantDto.getPartnerDto(),
					PartnerFac.KOMMUNIKATIONSART_FAX, theClientDto.getMandant(), theClientDto);

			String sTelefon = getPartnerFac().partnerkommFindRespectPartnerAsStringOhneExec(
					bestellungDto.getAnsprechpartnerIId(), lieferantDto.getPartnerDto(),
					PartnerFac.KOMMUNIKATIONSART_TELEFON, theClientDto.getMandant(), theClientDto);

			// daten als parameter an den Report weitergeben
			mapParameter.put(LPReport.P_ANSPRECHPARTNEREMAIL, sEmail != null ? sEmail : "");
			mapParameter.put(LPReport.P_ANSPRECHPARTNERFAX, sFax != null ? sFax : "");
			mapParameter.put(LPReport.P_ANSPRECHPARTNERTELEFON, sTelefon != null ? sTelefon : "");

			mapParameter = uebersteuereAnsprechpartnerKommmunikationsdaten(theClientDto, lieferantDto.getPartnerDto(),
					mapParameter);

			mapParameter.put("P_LIEFERANT_UID", lieferantDto.getPartnerDto().getCUid());
			mapParameter.put("P_LIEFERANT_EORI", lieferantDto.getPartnerDto().getCEori());
			if (lieferantDto.getKontoIIdKreditorenkonto() != null) {
				mapParameter.put("P_KREDITORENKONTO",
						getFinanzFac().kontoFindByPrimaryKey(lieferantDto.getKontoIIdKreditorenkonto()).getCNr());
			}
			mapParameter.put("P_KUNDENNUMMER", lieferantDto.getCKundennr());

			String cLabelRahmenOderLiefertermin = getTextRespectUISpr("lp.liefertermin", theClientDto.getMandant(),
					locDruck);
			String cLabelBestellungnr = null;

			if (bestellungDto.getBestellungartCNr().equals(BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR)) {
				cLabelRahmenOderLiefertermin = getTextRespectUISpr("lp.rahmentermin", theClientDto.getMandant(),
						locDruck);
				cLabelBestellungnr = getTextRespectUISpr("bes.bestellungnr.rahmen", theClientDto.getMandant(),
						locDruck);
			} else if (bestellungDto.getBestellungartCNr().equals(BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR)) {

				BestellungDto rahmenbestellungDto = getBestellungFac()
						.bestellungFindByPrimaryKey(bestellungDto.getIBestellungIIdRahmenbestellung());
				cLabelBestellungnr = getTextRespectUISpr("bes.bestellungnr.abrufrahmen", theClientDto.getMandant(),
						locDruck) + " " + rahmenbestellungDto.getCNr();
			} else if (bestellungDto.getBestellungartCNr().equals(BestellungFac.BESTELLUNGART_LEIHBESTELLUNG_C_NR)) {
				cLabelBestellungnr = getTextRespectUISpr("bes.bestellungnr.leihbestellung", theClientDto.getMandant(),
						locDruck);
			} else {
				cLabelBestellungnr = getTextRespectUISpr("bes.bestellungnr.bestellung", theClientDto.getMandant(),
						locDruck);
			}

			mapParameter.put("P_LABELBESTELLUNGNR", cLabelBestellungnr);
			mapParameter.put("P_BELEGKENNUNG", baueKennungBestellung(bestellungDto, theClientDto));

			mapParameter.put("P_LABELRAHMENODERLIEFERTERMIN", cLabelRahmenOderLiefertermin);

			mapParameter.put("P_LIEFERTERMIN", Helper.formatDatum(bestellungDto.getDLiefertermin(), locDruck));
			mapParameter.put("P_POENALE", Helper.short2Boolean(bestellungDto.getBPoenale()));

			mapParameter.put("P_LIEFERART", getLocaleFac()
					.lieferartFindByIIdLocaleOhneExc(bestellungDto.getLieferartIId(), locDruck, theClientDto));
			mapParameter.put("P_LIEFERART_ORT", bestellungDto.getCLieferartort());

			mapParameter.put("P_ZAHLUNGSKONDITION", getMandantFac()
					.zahlungszielFindByIIdLocaleOhneExc(bestellungDto.getZahlungszielIId(), locDruck, theClientDto));
			mapParameter.put("P_PROJEKTBEZ", bestellungDto.getCBez());

			ParametermandantDto parameterDtoVP = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_VERPACKUNGSMENGEN_EINGABE);
			Integer iVerpackungsmengeneingabe = (Integer) parameterDtoVP.getCWertAsObject();

			mapParameter.put("P_VERPACKUNGSMENGEN_EINGABE", iVerpackungsmengeneingabe);

			mapParameter.put("P_PROJEKT_I_ID", bestellungDto.getProjektIId());

			if (bestellungDto.getProjektIId() != null) {
				ProjektDto pjDto = getProjektFac().projektFindByPrimaryKey(bestellungDto.getProjektIId());
				mapParameter.put("P_PROJEKTNUMMER", pjDto.getCNr());
			}

			if (bestellungDto.getKostenstelleIId() != null) {
				KostenstelleDto kostenstelleDto = getSystemFac()
						.kostenstelleFindByPrimaryKey(bestellungDto.getKostenstelleIId());
				mapParameter.put(LPReport.P_KOSTENSTELLE, kostenstelleDto.getCNr());
			}

			String cBriefanrede = "";

			if (oAnsprechpartnerDto != null) {
				cBriefanrede = getPartnerServicesFac().getBriefanredeFuerBeleg(bestellungDto.getAnsprechpartnerIId(),
						lieferantDto.getPartnerIId(), locDruck, theClientDto);
			} else {
				// neutrale Anrede
				cBriefanrede = getBriefanredeNeutralOderPrivatperson(lieferantDto.getPartnerIId(), locDruck,
						theClientDto);
			}

			mapParameter.put("Briefanrede", cBriefanrede);

			PersonalDto oPersonalBenutzer = getPersonalFac().personalFindByPrimaryKey(theClientDto.getIDPersonal(),
					theClientDto);
			PersonalDto oPersonalAnleger = getPersonalFac()
					.personalFindByPrimaryKey(bestellungDto.getPersonalIIdAnlegen(), theClientDto);
			mapParameter.put("P_UNSER_ZEICHEN", Helper.getKurzzeichenkombi(oPersonalBenutzer.getCKurzzeichen(),
					oPersonalAnleger.getCKurzzeichen()));

			mapParameter.put("P_BELEGDATUM", Helper.formatDatum(bestellungDto.getDBelegdatum(), locDruck));
			mapParameter.put("P_WAEHRUNG", bestellungDto.getWaehrungCNr());
			mapParameter.put("P_ALLGEMEINER_RABATT", bestellungDto.getFAllgemeinerRabattsatz());
			mapParameter.put("P_ALLGEMEINER_RABATT_STRING",
					Helper.formatZahl(bestellungDto.getFAllgemeinerRabattsatz(), locDruck));

			mapParameter.put("P_TRANSPORTKOSTEN", bestellungDto.getNTransportkosten());

			mapParameter.put("P_TERMINEUNTERSCHIEDLICH", new Boolean(bTermineUnterschiedlich));

			mapParameter.put("Kopftext", Helper.formatStyledTextForJasper(sKopftext));

			if (getTheJudgeFac().hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_EINKAUF, theClientDto)) {
				mapParameter.put("P_BELEGWERT", bestellungDto.getNBestellwert());
			} else {
				mapParameter.put("P_BELEGWERT", null);
			}

			// die folgenden Felder werden im Report mit jeweils einer
			// trennenden Leerzeile
			// hintereinandergehaengt
			// MR 20071014 Folgende Felder muessen auch einzeln an Report
			// uebergeben werden.
			StringBuffer buff = new StringBuffer();

			// Externer Kommentar
			if (bestellungDto.getXExternerKommentar() != null && bestellungDto.getXExternerKommentar().length() > 0) {
				mapParameter.put(P_EXTERNERKOMMENTAR,
						Helper.formatStyledTextForJasper(bestellungDto.getXExternerKommentar()));
				buff.append(bestellungDto.getXExternerKommentar()).append("\n\n");
			}

			// Fusstext
			if (sFusstext != null) {
				mapParameter.put("P_FUSSTEXT", Helper.formatStyledTextForJasper(sFusstext));
				buff.append(sFusstext).append("\n\n");
			}

			StringBuffer buffVertreter = new StringBuffer();
			// Anrede des Mandanten
			String sMandantAnrede = mandantDto.getPartnerDto().formatFixName1Name2();
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
				vertreterDto = getPersonalFac().personalFindByPrimaryKey(bestellungDto.getPersonalIIdAnforderer(),
						theClientDto);
			}

			if (vertreterDto != null) {
				String sVertreterUFTitelName2Name1 = vertreterDto.formatFixUFTitelName2Name1();

				mapParameter.put(P_VERTRETER,
						getPersonalFac().formatAnrede(vertreterDto.getPartnerDto(), locDruck, theClientDto));

				mapParameter.put(P_VERTRETER_UNTERSCHRIFTSFUNKTION_UND_NAME, sVertreterUFTitelName2Name1);
				buff.append(sVertreterUFTitelName2Name1);
				buffVertreter.append(sVertreterUFTitelName2Name1);

				if (vertreterDto.getCUnterschriftstext() != null && vertreterDto.getCUnterschriftstext().length() > 0) {
					String sUnterschriftstext = vertreterDto.getCUnterschriftstext();
					mapParameter.put(P_VERTRETER_UNTERSCHRIFTSTEXT, sUnterschriftstext);
					buff.append("\n").append(sUnterschriftstext);
					buffVertreter.append("\n").append(sUnterschriftstext);
				}

				// Vertreter Kontaktdaten
				String sVertreterEmail = vertreterDto.getCEmail();

				String sVertreterFaxDirekt = vertreterDto.getCDirektfax();

				String sVertreterFax = vertreterDto.getCFax();

				String sVertreterTelefon = vertreterDto.getCTelefon();
				mapParameter.put(LPReport.P_VERTRETEREMAIL, sVertreterEmail != null ? sVertreterEmail : "");
				if (sVertreterFaxDirekt != null && sVertreterFaxDirekt != "") {
					mapParameter.put(LPReport.P_VERTRETERFAX, sVertreterFaxDirekt);
				} else {
					mapParameter.put(LPReport.P_VERTRETERFAX, sVertreterFax != null ? sVertreterFax : "");
				}
				mapParameter.put(LPReport.P_VERTRETERTELEFON, sVertreterTelefon != null ? sVertreterTelefon : "");

				mapParameter.put(LPReport.P_VERTRETER_TELEFON_FIRMA_MIT_DW,
						getPartnerFac().enrichNumber(mandantDto.getPartnerIId(), PartnerFac.KOMMUNIKATIONSART_TELEFON,
								theClientDto, vertreterDto.getCTelefon(), false));
			}
			mapParameter.put("P_SUMMARY", Helper.formatStyledTextForJasper(buff.toString()));

			mapParameter.put("P_STORNIERUNGSDATUM", bestellungDto.getTStorniert());
			mapParameter.put("P_AENDERUNGSBESTELLUNGSDATUM", bestellungDto.getTAenderungsbestellung());
			mapParameter.put("P_AENDERUNGSBESTELLUNG", new Boolean(bestellungDto.getTAenderungsbestellung() != null));
			mapParameter.put("P_AENDERUNGSBESTELLUNG_VERSION", bestellungDto.getIVersion());

			// Die Anzahl der Exemplare ist 1 + Anzahl der Kopien
			int iExemplare;
			if (iAnzahlKopienI == null || iAnzahlKopienI.intValue() <= 0) {
				iExemplare = 1;
			} else {
				iExemplare = 1 + iAnzahlKopienI.intValue();
			}

			prints = new JasperPrintLP[iExemplare];

			// PJ21318
			JasperPrintLP jasperPrintLP_AGB = getSystemReportFac().getABGReport(LocaleFac.BELEGART_BESTELLUNG, locDruck,
					theClientDto);
			if (jasperPrintLP_AGB != null) {
				mapParameter.put("P_SEITENANZAHL_AGB", jasperPrintLP_AGB.getPrint().getPages().size());
			}

			// Print-Array in der Schleife befuellen
			for (int iKopieNummer = 0; iKopieNummer < iExemplare; iKopieNummer++) {
				// jede Kopie erhaelt seine Kopiennummer
				// die "0." Kopie ist das Original und kriegt deshalb keine
				if (iKopieNummer > 0) {
					mapParameter.put(LPReport.P_KOPIE_NUMMER, new Integer(iKopieNummer));
				}
				// Index zuruecksetzen
				index = -1;

				initJRDS(mapParameter, BestellungReportFac.REPORT_MODUL, BestellungReportFac.REPORT_BESTELLUNG,
						theClientDto.getMandant(), locDruck, theClientDto, bMitLogo.booleanValue(),
						bestellungDto.getKostenstelleIId());
				prints[iKopieNummer] = Helper.addReport2Report(getReportPrint(), jasperPrintLP_AGB);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		PrintInfoDto values = getJCRDocFac().getPathAndPartnerAndTable(bestellungDto.getIId(),
				QueryParameters.UC_ID_BESTELLUNG, theClientDto);
		prints[0].setOInfoForArchive(values);
		prints[0].putAdditionalInformation(JasperPrintLP.KEY_BELEGART, LocaleFac.BELEGART_BESTELLUNG);
		prints[0].putAdditionalInformation(JasperPrintLP.KEY_BELEGIID, bestellungDto.getIId());
		Timestamp tend = new Timestamp(System.currentTimeMillis());
		System.out.println("Took: " + (tend.getTime() - tStart.getTime()) + "ms");

		return prints;
	}

	/**
	 * Eine Bestellung drucken.
	 * 
	 * @param theClientDto der aktuelle Benutzer
	 * @return JasperPrint der Druck
	 * @throws EJBExceptionLP Ausnahme
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
					&& flrartikelAktuell.getC_nr().charAt(flrartikelAktuell.getC_nr().length() - 4) == '_') {
				// Es ist eine neuere Version

				// Nun alle aelteren Versionen suchen:

				Session session3 = FLRSessionFactory.getFactory().openSession();

				String queryString = "SELECT a FROM FLRArtikel a WHERE a.mandant_c_nr='" + theClientDto.getMandant()
						+ "' AND c_nr LIKE '"
						+ flrartikelAktuell.getC_nr().substring(0, flrartikelAktuell.getC_nr().length() - 4)
						+ "%' AND  c_nr < '" + flrartikelAktuell.getC_nr() + "'";

				org.hibernate.Query query3 = session3.createQuery(queryString);
				List<?> results3 = query3.list();
				Iterator<?> resultListIterator3 = results3.iterator();

				while (resultListIterator3.hasNext()) {

					FLRArtikel flrArtikel = (FLRArtikel) resultListIterator3.next();

					Session session2 = null;

					session2 = factory.openSession();

					String sQuery2 = "SELECT b FROM FLRBestellposition b WHERE b.flrbestellung.mandant_c_nr='"
							+ theClientDto.getMandant() + "' AND b.flrartikel.i_id=" + flrArtikel.getI_id()
							+ " AND b.flrbestellung.bestellungstatus_c_nr IN ('" + BestellungFac.BESTELLSTATUS_ANGELEGT
							+ "','" + BestellungFac.BESTELLSTATUS_BESTAETIGT + "','" + BestellungFac.BESTELLSTATUS_OFFEN
							+ "','" + BestellungFac.BESTELLSTATUS_TEILERLEDIGT + "')";
					Query q2 = session2.createQuery(sQuery2);
					List<?> list2 = q2.list();
					Iterator<?> it2 = list2.iterator();

					while (it2.hasNext()) {
						FLRBestellposition flrBestellposition = (FLRBestellposition) it2.next();

						hmAeltereVersionen.put(flrartikelAktuell.getI_id(), null);

						Object[] oZeile = new Object[BestellungReportFac.REPORT_GEAENDERTEARTIKEL_ANZAHL_SPALTEN];
						oZeile[BestellungReportFac.REPORT_GEAENDERTEARTIKEL_ARTIKELNUMMER_AKTUELL] = flrartikelAktuell
								.getC_nr();

						ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKey(flrartikelAktuell.getI_id(),
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

						aDto = getArtikelFac().artikelFindByPrimaryKey(flrBestellposition.getFlrartikel().getI_id(),
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
						oZeile[BestellungReportFac.REPORT_GEAENDERTEARTIKEL_MENGE] = flrBestellposition.getN_menge();
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
								.formatNameAusFLRPartner(
										flrBestellposition.getFlrbestellung().getFlrlieferant().getFlrpartner());

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

		initJRDS(mapParameter, BestellungReportFac.REPORT_MODUL, BestellungReportFac.REPORT_GEAENDERTE_ARTIKEL,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printTerminuebersicht(Integer bestellungIId, TheClientDto theClientDto) {
		useCase = UC_TERMINUEBERSICHT;

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		try {
			BestellungDto bsDto = getBestellungFac().bestellungFindByPrimaryKey(bestellungIId);

			LieferantDto lfDto = getLieferantFac().lieferantFindByPrimaryKey(bsDto.getLieferantIIdBestelladresse(),
					theClientDto);

			parameter.put("P_BESTELLNUMMER", bsDto.getCNr());
			parameter.put("P_LIEFERANT", lfDto.getPartnerDto().formatFixName1Name2());
			parameter.put("P_BELEGDATUM", bsDto.getDBelegdatum());

		} catch (RemoteException e1) {
			throwEJBExceptionLPRespectOld(e1);
		}

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT distinct bp.flrartikel.i_id, bp.flrartikel.c_nr FROM FLRBestellpositionReport bp WHERE bp.bestellpositionart_c_nr='"
				+ BestellpositionFac.BESTELLPOSITIONART_IDENT + "' AND bp.flrbestellung.i_id=" + bestellungIId
				+ " ORDER BY bp.flrartikel.c_nr ";

		org.hibernate.Query query = session.createQuery(queryString);
		List resultList = query.list();

		Iterator resultListIterator = resultList.iterator();

		ArrayList<Object[]> alDaten = new ArrayList<Object[]>();

		while (resultListIterator.hasNext()) {

			Object[] o = (Object[]) resultListIterator.next();

			Integer artikelIId = (Integer) o[0];

			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(artikelIId, theClientDto);

			Object[] oZeile = new Object[REPORT_TERMINUEBERSICHT_ANZAHL_SPALTEN];
			oZeile[REPORT_TERMINUEBERSICHT_ARTIKELNUMMER] = artikelDto.getCNr();
			oZeile[REPORT_TERMINUEBERSICHT_BEZEICHNUNG] = artikelDto.getCBezAusSpr();
			oZeile[REPORT_TERMINUEBERSICHT_ZUSATZBEZEICHNUNG] = artikelDto.getCZBezAusSpr();

			try {
				if (artikelDto.isLagerbewirtschaftet()) {
					JasperPrintLP bewegungsvorschau = getArtikelReportFac().printBewegungsvorschau(artikelIId, true,
							null, false, false, theClientDto);

					oZeile[REPORT_TERMINUEBERSICHT_SUBREPORT_BEWEGUNGSVORSCHAU] = bewegungsvorschau
							.transformToSubreport();
				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			alDaten.add(oZeile);

		}

		data = new Object[alDaten.size()][REPORT_TERMINUEBERSICHT_ANZAHL_SPALTEN];
		data = alDaten.toArray(data);

		initJRDS(parameter, BestellungReportFac.REPORT_MODUL, BestellungReportFac.REPORT_TERMINUEBERSICHT,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();

	}

	public JasperPrintLP printAbholauftrag(Integer iIdBestellungI, TheClientDto theClientDto) {
		JasperPrintLP print = null;
		if (iIdBestellungI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdBestellungI == null"));
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
			bestellungDto = getBestellungFac().bestellungFindByPrimaryKey(iIdBestellungI);

			aPositionDtos = getBestellpositionFac().bestellpositionFindByBestellung(iIdBestellungI, theClientDto);

			// den Druckzeitpunkt vermerken
			bestellungDto.setTGedruckt(getTimestamp());

			LieferantDto lieferantDto = getLieferantFac()
					.lieferantFindByPrimaryKey(bestellungDto.getLieferantIIdBestelladresse(), theClientDto);

			AnsprechpartnerDto oAnsprechpartnerDto = null;

			Integer partnerIIdAnsprechpartner = null;
			if (bestellungDto.getAnsprechpartnerIId() != null) {
				oAnsprechpartnerDto = getAnsprechpartnerFac()
						.ansprechpartnerFindByPrimaryKey(bestellungDto.getAnsprechpartnerIId(), theClientDto);
				if (oAnsprechpartnerDto != null) {
					partnerIIdAnsprechpartner = oAnsprechpartnerDto.getPartnerIIdAnsprechpartner();
				}
			}

			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);

			// es gilt das Locale des Lieferanten
			Locale locDruck = Helper.string2Locale(lieferantDto.getPartnerDto().getLocaleCNrKommunikation());

			int iAnzahlZeilen = aPositionDtos.length; // Anzahl der Zeilen in
			// der Gruppe

			data = new Object[iAnzahlZeilen][BestellungReportFac.REPORT_BESTELLUNG_ANZAHL_SPALTEN];

			// die Datenmatrix befuellen
			for (int i = 0; i < iAnzahlZeilen; i++) {
				data[i][REPORT_ABHOLAUFTRAG_POSITIONOBKJEKT] = getSystemReportFac()
						.getPositionForReport(LocaleFac.BELEGART_BESTELLUNG, aPositionDtos[i].getIId(), theClientDto);
				data[i][REPORT_ABHOLAUFTRAG_POSITION] = getBestellpositionFac()
						.getPositionNummer(aPositionDtos[i].getIId(), theClientDto);

				if (aPositionDtos[i].getTUebersteuerterLiefertermin() != null) {
					Timestamp tposLieferTermin = Helper.cutTimestamp(aPositionDtos[i].getTUebersteuerterLiefertermin());
					if (!(bestellungDto.getDLiefertermin().equals(tposLieferTermin))) {
						bTermineUnterschiedlich = true;
					}
				}

				// Artikelpositionen
				if (aPositionDtos[i].getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_IDENT)
						|| aPositionDtos[i].getPositionsartCNr()
								.equals(BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE)) {

					if (!bestellungDto.getDLiefertermin().equals(aPositionDtos[i].getTUebersteuerterLiefertermin())) {
						data[i][REPORT_ABHOLAUFTRAG_POSITIONTERMIN] = Helper.formatDatum(
								Helper.extractDate(aPositionDtos[i].getTUebersteuerterLiefertermin()), locDruck);

					}

					ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(aPositionDtos[i].getArtikelIId(),
							theClientDto);
					BelegPositionDruckIdentDto druckDto = printIdent(aPositionDtos[i], LocaleFac.BELEGART_BESTELLUNG,
							artikelDto, locDruck, lieferantDto.getPartnerIId(), theClientDto);
					data[i][REPORT_ABHOLAUFTRAG_ARTIKELCZBEZ2] = druckDto.getSArtikelZusatzBezeichnung2();
					// Artikel Bezeichnung
					data[i][REPORT_ABHOLAUFTRAG_BEZEICHNUNG] = druckDto.getSBezeichnung();
					// Artikel Kurzbezeichnung
					data[i][REPORT_ABHOLAUFTRAG_KURZBEZEICHNUNG] = druckDto.getSKurzbezeichnung();
					data[i][REPORT_ABHOLAUFTRAG_ZUSATZBEZEICHNUNG] = druckDto.getSZusatzBezeichnung();
					data[i][REPORT_ABHOLAUFTRAG_IDENT] = druckDto.getSArtikelInfo();
					data[i][REPORT_ABHOLAUFTRAG_ARTIKELKOMMENTAR] = druckDto.getSArtikelkommentar();

					data[i][REPORT_ABHOLAUFTRAG_REFERENZNUMMER] = artikelDto.getCReferenznr();

					// Lieferantendaten: Artikelnummer, Bezeichnung
					ArtikellieferantDto artikellieferantDto = getArtikelFac().getArtikelEinkaufspreis(
							aPositionDtos[i].getArtikelIId(), bestellungDto.getLieferantIIdBestelladresse(),
							aPositionDtos[i].getNMenge(),

							bestellungDto.getWaehrungCNr(), new java.sql.Date(bestellungDto.getDBelegdatum().getTime()),
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
								.herstellerFindByPrimaryKey(artikelDto.getHerstellerIId(), theClientDto);
						data[i][REPORT_ABHOLAUFTRAG_ARTIKEL_HERSTELLER] = herstellerDto.getCNr();
						PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(herstellerDto.getPartnerIId(),
								theClientDto);
						data[i][REPORT_ABHOLAUFTRAG_ARTIKEL_HERSTELLER_NAME] = partnerDto.formatFixName1Name2();
					}

					if (artikelDto.getFGewichtkg() != null) {
						data[i][REPORT_ABHOLAUFTRAG_ARTIKELGEWICHT] = new BigDecimal(artikelDto.getFGewichtkg())
								.multiply(aPositionDtos[i].getNMenge());
					}

					if (artikelDto.getEinheitCNrBestellung() != null) {
						boolean bInvers = Helper.short2boolean(artikelDto.getbBestellmengeneinheitInvers());

						BigDecimal gewicht = null;

						if (bInvers) {
							gewicht = aPositionDtos[i].getNMenge().divide(artikelDto.getNUmrechnungsfaktor(), 4,
									BigDecimal.ROUND_HALF_EVEN);
						} else {
							gewicht = aPositionDtos[i].getNMenge().multiply(artikelDto.getNUmrechnungsfaktor());
						}
						BigDecimal umrechnung = new BigDecimal(0);
						gewicht = Helper.rundeKaufmaennisch(gewicht, 4);
						data[i][REPORT_ABHOLAUFTRAG_GEWICHT] = gewicht;
						data[i][REPORT_ABHOLAUFTRAG_GEWICHTEINHEIT] = getSystemFac()
								.formatEinheit(artikelDto.getEinheitCNrBestellung(), locDruck, theClientDto);

						if (bInvers) {
							umrechnung = aPositionDtos[i].getNNettoeinzelpreis()
									.multiply(artikelDto.getNUmrechnungsfaktor());
						} else {
							umrechnung = aPositionDtos[i].getNNettoeinzelpreis()
									.divide(artikelDto.getNUmrechnungsfaktor(), 4, BigDecimal.ROUND_HALF_EVEN);
						}

						umrechnung = aPositionDtos[i].getNNettoeinzelpreis().divide(artikelDto.getNUmrechnungsfaktor(),
								4, BigDecimal.ROUND_HALF_EVEN);

						data[i][REPORT_ABHOLAUFTRAG_PREISPEREINHEIT] = umrechnung;
					}

					// PJ 14867
					if (aPositionDtos[i].getIBestellpositionIIdRahmenposition() != null && bestellungDto
							.getBestellungartCNr().equals(BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR)) {
						Session sessionRahmen = FLRSessionFactory.getFactory().openSession();

						String query = "FROM FLRBestellpositionReport bs WHERE bs.i_id="
								+ aPositionDtos[i].getIBestellpositionIIdRahmenposition();

						Query q = sessionRahmen.createQuery(query);

						List l = q.list();

						if (l.size() > 0) {
							FLRBestellpositionReport eintrag = (FLRBestellpositionReport) l.iterator().next();

							BigDecimal offeneMenge = eintrag.getN_menge();

							for (Iterator<?> iter = eintrag.getAbrufpositionenset().iterator(); iter.hasNext();) {
								FLRBestellpositionReport item = (FLRBestellpositionReport) iter.next();
								if (item.getFlrbestellung().getT_belegdatum().getTime() <= bestellungDto
										.getDBelegdatum().getTime()) {
									offeneMenge = offeneMenge.subtract(item.getN_menge());
								}
							}

							data[i][REPORT_ABHOLAUFTRAG_OFFENERAHMENMENGE] = offeneMenge;

						}

						sessionRahmen.close();

					}

					if (aPositionDtos[i].getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_IDENT)) {
						data[i][REPORT_ABHOLAUFTRAG_IDENTNUMMER] = artikelDto.getCNr();

						if (artikelDto.getVerpackungDto() != null) {
							data[i][REPORT_ABHOLAUFTRAG_BAUFORM] = artikelDto.getVerpackungDto().getCBauform();
							data[i][REPORT_ABHOLAUFTRAG_VERPACKUNGSART] = artikelDto.getVerpackungDto()
									.getCVerpackungsart();
						}
						if (artikelDto.getMaterialIId() != null) {
							MaterialDto materialDto = getMaterialFac()
									.materialFindByPrimaryKey(artikelDto.getMaterialIId(), locDruck, theClientDto);
							if (materialDto.getMaterialsprDto() != null) {
								/**
								 * @todo MR->MR richtige Mehrsprachigkeit: Material in Drucksprache.
								 */
								data[i][REPORT_ABHOLAUFTRAG_ARTIKEL_MATERIAL] = materialDto.getMaterialsprDto()
										.getCBez();
							} else {
								data[i][REPORT_ABHOLAUFTRAG_ARTIKEL_MATERIAL] = materialDto.getCNr();
							}
						}
						if (artikelDto.getGeometrieDto() != null) {
							data[i][REPORT_ABHOLAUFTRAG_ARTIKEL_BREITE] = artikelDto.getGeometrieDto().getFBreite();
							data[i][REPORT_ABHOLAUFTRAG_ARTIKEL_HOEHE] = artikelDto.getGeometrieDto().getFHoehe();
							data[i][REPORT_ABHOLAUFTRAG_ARTIKEL_TIEFE] = artikelDto.getGeometrieDto().getFTiefe();
						}

					}
					data[i][REPORT_ABHOLAUFTRAG_MENGE] = aPositionDtos[i].getNMenge();
					data[i][REPORT_ABHOLAUFTRAG_EINHEIT] = getSystemFac()
							.formatEinheit(aPositionDtos[i].getEinheitCNr(), locDruck, theClientDto);

					data[i][REPORT_ABHOLAUFTRAG_EINZELPREIS] = aPositionDtos[i].getNNettoeinzelpreis();
					data[i][REPORT_ABHOLAUFTRAG_RABATT] = aPositionDtos[i].getDRabattsatz();
					data[i][REPORT_ABHOLAUFTRAG_ANGEBOTSNUMMER] = aPositionDtos[i].getCAngebotnummer();

					BigDecimal bdGesamtpreis = null;

					if (aPositionDtos[i].getNNettogesamtpreis() != null && aPositionDtos[i].getNMenge() != null) {
						bdGesamtpreis = aPositionDtos[i].getNNettogesamtpreis().multiply(aPositionDtos[i].getNMenge());
					}

					data[i][REPORT_ABHOLAUFTRAG_GESAMTPREIS] = bdGesamtpreis;

					/**
					 * @todo MR-> Wird nicht mehr benoetigt, da LPReport.printIdent das abbildet.
					 *       Wegen kurzer Zeit vor deploy nicht mehr geloescht.
					 */
					ArrayList<Object> images = new ArrayList<Object>();
					ArtikelkommentarDto[] artikelkommentarDto = getArtikelkommentarFac()
							.artikelkommentardruckFindByArtikelIIdBelegartCNr(aPositionDtos[i].getArtikelIId(),
									LocaleFac.BELEGART_BESTELLUNG, theClientDto.getLocUiAsString(), theClientDto);

					if (artikelkommentarDto != null && artikelkommentarDto.length > 0) {
						String cPositionKommentarText = "";

						for (int j = 0; j < artikelkommentarDto.length; j++) {
							if (artikelkommentarDto[j].getArtikelkommentarsprDto() != null) {
								String cDatenformat = artikelkommentarDto[j].getDatenformatCNr();
								if (cDatenformat.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG)
										|| artikelkommentarDto[j].getDatenformatCNr()
												.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG)
										|| artikelkommentarDto[j].getDatenformatCNr()
												.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF)) {
									byte[] bild = artikelkommentarDto[j].getArtikelkommentarsprDto().getOMedia();
									if (bild != null) {
										java.awt.Image myImage = Helper.byteArrayToImage(bild);
										data[i][REPORT_ABHOLAUFTRAG_IMAGE] = myImage;
										images.add(myImage);
									}
								} else if (artikelkommentarDto[j].getDatenformatCNr()
										.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF)) {

									byte[] bild = artikelkommentarDto[j].getArtikelkommentarsprDto().getOMedia();

									java.awt.Image[] tiffs = Helper.tiffToImageArray(bild);
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
				if (aPositionDtos[i].getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_BETRIFFT)) {
					data[i][REPORT_ABHOLAUFTRAG_FREIERTEXT] = aPositionDtos[i].getCBez();
				}

				// Texteingabe Positionen
				if (aPositionDtos[i].getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_TEXTEINGABE)) {
					// IMS 1619 leerer Text soll als Leerezeile erscheinen
					String sText = aPositionDtos[i].getXTextinhalt();

					if (sText != null && sText.trim().equals("")) {
						data[i][REPORT_ABHOLAUFTRAG_LEERZEILE] = " ";
					} else {
						data[i][REPORT_ABHOLAUFTRAG_FREIERTEXT] = sText;
					}
				}

				// Textbaustein Positionen
				if (aPositionDtos[i].getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_TEXTBAUSTEIN)) {
					// Dto holen
					MediastandardDto oMediastandardDto = getMediaFac()
							.mediastandardFindByPrimaryKey(aPositionDtos[i].getMediastandardIId());
					// zum Drucken vorbereiten
					BelegPositionDruckTextbausteinDto druckDto = printTextbaustein(oMediastandardDto, theClientDto);
					data[i][REPORT_ABHOLAUFTRAG_FREIERTEXT] = druckDto.getSFreierText();
					data[i][REPORT_ABHOLAUFTRAG_IMAGE] = druckDto.getOImage();
				}

				// Leerzeile Positionen
				if (aPositionDtos[i].getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_LEERZEILE)) {
					data[i][REPORT_ABHOLAUFTRAG_LEERZEILE] = " ";
				}

				// Seitenumbruch Positionen
				if (aPositionDtos[i].getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_SEITENUMBRUCH)) {
					bbSeitenumbruch = new Boolean(!bbSeitenumbruch.booleanValue()); // toggle
				}

				data[i][REPORT_ABHOLAUFTRAG_SEITENUMBRUCH] = bbSeitenumbruch;
				data[i][REPORT_ABHOLAUFTRAG_POSITIONSART] = aPositionDtos[i].getPositionsartCNr();

			}

			// Kopftext
			String sKopftext = bestellungDto.getCKopftextUebersteuert();

			if (sKopftext == null || sKopftext.length() == 0) {
				BestellungtextDto oBestellungtextDto = getBestellungServiceFac().bestellungtextFindByMandantLocaleCNr(
						theClientDto.getMandant(), lieferantDto.getPartnerDto().getLocaleCNrKommunikation(),
						BestellungServiceFac.BESTELLUNGTEXT_KOPFTEXT, theClientDto);
				sKopftext = oBestellungtextDto.getXTextinhalt();
			}

			// Fusstext
			String sFusstext = bestellungDto.getCFusstextUebersteuert();

			if (sFusstext == null || sFusstext.length() == 0) {
				BestellungtextDto oBestellungtextDto = getBestellungServiceFac().bestellungtextFindByMandantLocaleCNr(
						theClientDto.getMandant(), lieferantDto.getPartnerDto().getLocaleCNrKommunikation(),
						BestellungServiceFac.BESTELLUNGTEXT_FUSSTEXT, theClientDto);

				sFusstext = oBestellungtextDto.getXTextinhalt();
			}

			// Erstellung des Report
			HashMap<String, Object> mapParameter = new HashMap<String, Object>();
			// CK: PJ 13849
			mapParameter.put("P_BEARBEITER",
					getPersonalFac().getPersonRpt(bestellungDto.getPersonalIIdAnlegen(), theClientDto));
			// die Parameter uebergeben
			if (bestellungDto.getBTeillieferungMoeglich() != null) {
				mapParameter.put("P_TEILLIEFERUNGMOEGLICH",
						new Boolean(Helper.short2boolean(bestellungDto.getBTeillieferungMoeglich())));
			}

			mapParameter.put("P_PERSON_AENDERN",
					getPersonalFac().getPersonRpt(bestellungDto.getPersonalIIdAendern(), theClientDto));

			String sSpediteur = null;
			if (bestellungDto.getSpediteurIId() != null) {

				SpediteurDto spediteurDto = getMandantFac().spediteurFindByPrimaryKey(bestellungDto.getSpediteurIId());

				sSpediteur = getMandantFac().spediteurFindByPrimaryKey(bestellungDto.getSpediteurIId())
						.getCNamedesspediteurs();

				if (spediteurDto.getPartnerIId() != null) {
					PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(spediteurDto.getPartnerIId(),
							theClientDto);

					AnsprechpartnerDto ansprechpartnerDtoSpediteur = null;

					if (spediteurDto.getAnsprechpartnerIId() != null) {
						ansprechpartnerDtoSpediteur = getAnsprechpartnerFac()
								.ansprechpartnerFindByPrimaryKey(spediteurDto.getAnsprechpartnerIId(), theClientDto);

						String sTelefon = getPartnerFac().partnerkommFindRespectPartnerAsStringOhneExec(
								ansprechpartnerDtoSpediteur.getIId(), partnerDto, PartnerFac.KOMMUNIKATIONSART_TELEFON,
								theClientDto.getMandant(), theClientDto);
						mapParameter.put("P_SPEDITEUR_ANSPRECHPARTNER_TELEFON", sTelefon);

					}

					mapParameter.put("P_SPEDITEUR_ADRESSBLOCK",
							formatAdresseFuerAusdruck(partnerDto, ansprechpartnerDtoSpediteur, mandantDto, locDruck));

				}
			}

			mapParameter.put("P_SPEDITEUR", sSpediteur != null ? sSpediteur : "");

			mapParameter.put("P_SUBREPORT_LIEFERGRUPPENTEXTE",
					getSubreportLiefergruppenTexte(aPositionDtos, theClientDto));
			mapParameter.put("P_SUBREPORT_PARTNERKOMMENTAR",
					getPartnerServicesFacLocal().getSubreportAllerMitzudruckendenPartnerkommentare(
							lieferantDto.getPartnerDto().getIId(), false, LocaleFac.BELEGART_BESTELLUNG, theClientDto));

			mapParameter.put("P_LIEFERANTINTERNERKOMMENTAR", lieferantDto.getCHinweisintern());
			mapParameter.put("P_LIEFERANTEXTERNERKOMMENTAR", lieferantDto.getCHinweisextern());

			if (bestellungDto.getAuftragIId() != null) {
				AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(bestellungDto.getAuftragIId());
				mapParameter.put("P_AUFTRAGSNUMMER", auftragDto.getCNr());
			}
			if (bestellungDto.getStatusCNr().equals(BestellpositionFac.BESTELLPOSITIONSTATUS_STORNIERT)) {
				mapParameter.put("P_STORNIERT", true);
			} else {
				mapParameter.put("P_STORNIERT", false);
			}
			mapParameter.put("Mandantenadresse", Helper.formatMandantAdresse(mandantDto));

			mapParameter.put("P_LIEFERADRESSE_IST_NICHT_MANDANT", new Boolean(true));

			if (bestellungDto.getPartnerIIdLieferadresse() != null) {
				PartnerDto lieferadresseDto = getPartnerFac()
						.partnerFindByPrimaryKey(bestellungDto.getPartnerIIdLieferadresse(), theClientDto);

				if (bestellungDto.getAnsprechpartnerIIdLieferadresse() != null) {
					AnsprechpartnerDto ansprechpartnerDtoLieferadresse = getAnsprechpartnerFac()
							.ansprechpartnerFindByPrimaryKey(bestellungDto.getAnsprechpartnerIIdLieferadresse(),
									theClientDto);

					String sTelefon = getPartnerFac().partnerkommFindRespectPartnerAsStringOhneExec(
							ansprechpartnerDtoLieferadresse.getIId(), lieferadresseDto,
							PartnerFac.KOMMUNIKATIONSART_TELEFON, theClientDto.getMandant(), theClientDto);
					mapParameter.put("P_LIEFERADRESSE_ANSPRECHPARTNER_TELEFON", sTelefon);

				}

				// SP581
				if (lieferantDto.getPartnerDto().getLandplzortDto() != null
						&& lieferadresseDto.getLandplzortDto() != null
						&& !lieferantDto.getPartnerDto().getLandplzortDto().getLandDto().getIID()
								.equals(lieferadresseDto.getLandplzortDto().getLandDto().getIID())) {

					if (lieferadresseDto.getLandplzortDto().getLandDto().getLandIIdGemeinsamespostland() != null
							&& lieferadresseDto.getLandplzortDto().getLandDto().getLandIIdGemeinsamespostland()
									.equals(lieferantDto.getPartnerDto().getLandplzortDto().getIlandID())) {
						mapParameter.put("P_LIEFERADRESSE", formatAdresseFuerAusdruck(lieferadresseDto, null,
								mandantDto, Helper.string2Locale(lieferadresseDto.getLocaleCNrKommunikation())));
					} else if (lieferantDto.getPartnerDto().getLandplzortDto().getLandDto()
							.getLandIIdGemeinsamespostland() != null
							&& lieferantDto.getPartnerDto().getLandplzortDto().getLandDto()
									.getLandIIdGemeinsamespostland()
									.equals(lieferadresseDto.getLandplzortDto().getIlandID())) {
						mapParameter.put("P_LIEFERADRESSE",
								formatAdresseFuerAusdruck(lieferadresseDto, null, mandantDto,
										Helper.string2Locale(lieferadresseDto.getLocaleCNrKommunikation()),
										LocaleFac.BELEGART_BESTELLUNG));
					} else {

						mapParameter.put("P_LIEFERADRESSE",
								formatAdresseFuerAusdruck(lieferadresseDto, null, mandantDto,
										Helper.string2Locale(lieferadresseDto.getLocaleCNrKommunikation()), true,
										LocaleFac.BELEGART_BESTELLUNG, false));
					}
				} else {
					mapParameter.put("P_LIEFERADRESSE",
							formatAdresseFuerAusdruck(lieferadresseDto, null, mandantDto,
									Helper.string2Locale(lieferadresseDto.getLocaleCNrKommunikation()),
									LocaleFac.BELEGART_BESTELLUNG));
				}

				mapParameter.put("P_LIEFERADRESSENICHTNULL", new Boolean(true));

				if (mandantDto.getPartnerIId().equals(bestellungDto.getPartnerIIdLieferadresse())) {
					mapParameter.put("P_LIEFERADRESSE_IST_NICHT_MANDANT", new Boolean(false));
				}

			}

			if (bestellungDto.getAnfrageIId() != null) {
				AnfrageDto anfrageDto = getAnfrageFac().anfrageFindByPrimaryKey(bestellungDto.getAnfrageIId(),
						theClientDto);
				mapParameter.put("P_ANFRAGENUMMER", anfrageDto.getCNr());
			}

			if (bestellungDto.getPartnerIIdAbholadresse() != null) {
				PartnerDto abholadresseDto = getPartnerFac()
						.partnerFindByPrimaryKey(bestellungDto.getPartnerIIdAbholadresse(), theClientDto);

				AnsprechpartnerDto oAnsprechpartnerDtoAbholadresse = null;

				if (bestellungDto.getAnsprechpartnerIIdAbholadresse() != null) {
					oAnsprechpartnerDtoAbholadresse = getAnsprechpartnerFac().ansprechpartnerFindByPrimaryKey(
							bestellungDto.getAnsprechpartnerIIdAbholadresse(), theClientDto);

					String sTelefon = getPartnerFac().partnerkommFindRespectPartnerAsStringOhneExec(
							oAnsprechpartnerDtoAbholadresse.getIId(), abholadresseDto,
							PartnerFac.KOMMUNIKATIONSART_TELEFON, theClientDto.getMandant(), theClientDto);
					mapParameter.put("P_ABHOLADRESSE_ANSPRECHPARTNER_TELEFON", sTelefon);

				}

				mapParameter.put("P_ABHOLADRESSE",
						formatAdresseFuerAusdruck(abholadresseDto, oAnsprechpartnerDtoAbholadresse, mandantDto,
								Helper.string2Locale(abholadresseDto.getLocaleCNrKommunikation()),
								LocaleFac.BELEGART_BESTELLUNG));

				if (oAnsprechpartnerDtoAbholadresse != null) {
					mapParameter.put("P_ANSPRECHPARTNER_ABHOLADRESSE",
							getPartnerFac().formatFixAnredeTitelName2Name1FuerAdresskopf(
									oAnsprechpartnerDtoAbholadresse.getPartnerDto(),
									Helper.string2Locale(abholadresseDto.getLocaleCNrKommunikation()), null));
				}

			}

			if (lieferantDto.getPartnerRechnungsadresseDto() != null) {
				mapParameter.put("P_RECHNUNGADRESSE",
						formatAdresseFuerAusdruck(lieferantDto.getPartnerRechnungsadresseDto(), oAnsprechpartnerDto,
								mandantDto,
								Helper.string2Locale(
										lieferantDto.getPartnerRechnungsadresseDto().getLocaleCNrKommunikation()),
								LocaleFac.BELEGART_BESTELLUNG));
			}

			mapParameter.put("Adressefuerausdruck",
					formatAdresseFuerAusdruck(lieferantDto.getPartnerDto(), oAnsprechpartnerDto, mandantDto,
							Helper.string2Locale(lieferantDto.getPartnerDto().getLocaleCNrKommunikation()),
							LocaleFac.BELEGART_BESTELLUNG));
			if (oAnsprechpartnerDto != null) {
				mapParameter.put("P_ANSPRECHPARTNER_ADRESSBLOCK",
						getPartnerFac().formatFixAnredeTitelName2Name1FuerAdresskopf(
								oAnsprechpartnerDto.getPartnerDto(),
								Helper.string2Locale(lieferantDto.getPartnerDto().getLocaleCNrKommunikation()), null));
				String sTelefon = getPartnerFac().partnerkommFindRespectPartnerAsStringOhneExec(
						oAnsprechpartnerDto.getIId(), lieferantDto.getPartnerDto(),
						PartnerFac.KOMMUNIKATIONSART_TELEFON, theClientDto.getMandant(), theClientDto);
				mapParameter.put("P_BESTELLADRESSE_ANSPRECHPARTNER_TELEFON", sTelefon);
			}
			/**
			 * @todo fuer diesen Block eine zentrale Methode bauen.
			 */

			// die partnerIId des ansprechpartners brauch ich
			// daten holen
			String sEmail = getPartnerFac().partnerkommFindRespectPartnerAsStringOhneExec(
					bestellungDto.getAnsprechpartnerIId(), lieferantDto.getPartnerDto(),
					PartnerFac.KOMMUNIKATIONSART_EMAIL, theClientDto.getMandant(), theClientDto);

			String sFax = getPartnerFac().partnerkommFindRespectPartnerAsStringOhneExec(
					bestellungDto.getAnsprechpartnerIId(), lieferantDto.getPartnerDto(),
					PartnerFac.KOMMUNIKATIONSART_FAX, theClientDto.getMandant(), theClientDto);

			String sTelefon = getPartnerFac().partnerkommFindRespectPartnerAsStringOhneExec(
					bestellungDto.getAnsprechpartnerIId(), lieferantDto.getPartnerDto(),
					PartnerFac.KOMMUNIKATIONSART_TELEFON, theClientDto.getMandant(), theClientDto);

			// daten als parameter an den Report weitergeben
			mapParameter.put(LPReport.P_ANSPRECHPARTNEREMAIL, sEmail != null ? sEmail : "");
			mapParameter.put(LPReport.P_ANSPRECHPARTNERFAX, sFax != null ? sFax : "");
			mapParameter.put(LPReport.P_ANSPRECHPARTNERTELEFON, sTelefon != null ? sTelefon : "");

			mapParameter = uebersteuereAnsprechpartnerKommmunikationsdaten(theClientDto, lieferantDto.getPartnerDto(),
					mapParameter);

			mapParameter.put("P_LIEFERANT_UID", lieferantDto.getPartnerDto().getCUid());
			mapParameter.put("P_LIEFERANT_EORI", lieferantDto.getPartnerDto().getCEori());
			if (lieferantDto.getKontoIIdKreditorenkonto() != null) {
				mapParameter.put("P_KREDITORENKONTO",
						getFinanzFac().kontoFindByPrimaryKey(lieferantDto.getKontoIIdKreditorenkonto()).getCNr());
			}
			mapParameter.put("P_KUNDENNUMMER", lieferantDto.getCKundennr());

			String cLabelRahmenOderLiefertermin = getTextRespectUISpr("lp.liefertermin", theClientDto.getMandant(),
					locDruck);
			String cLabelBestellungnr = null;

			if (bestellungDto.getBestellungartCNr().equals(BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR)) {
				cLabelRahmenOderLiefertermin = getTextRespectUISpr("lp.rahmentermin", theClientDto.getMandant(),
						locDruck);
				cLabelBestellungnr = getTextRespectUISpr("bes.bestellungnr.rahmen", theClientDto.getMandant(),
						locDruck);
			} else if (bestellungDto.getBestellungartCNr().equals(BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR)) {

				BestellungDto rahmenbestellungDto = getBestellungFac()
						.bestellungFindByPrimaryKey(bestellungDto.getIBestellungIIdRahmenbestellung());
				cLabelBestellungnr = getTextRespectUISpr("bes.bestellungnr.abrufrahmen", theClientDto.getMandant(),
						locDruck) + " " + rahmenbestellungDto.getCNr();
			} else if (bestellungDto.getBestellungartCNr().equals(BestellungFac.BESTELLUNGART_LEIHBESTELLUNG_C_NR)) {
				cLabelBestellungnr = getTextRespectUISpr("bes.bestellungnr.leihbestellung", theClientDto.getMandant(),
						locDruck);
			} else {
				cLabelBestellungnr = getTextRespectUISpr("bes.bestellungnr.bestellung", theClientDto.getMandant(),
						locDruck);
			}

			mapParameter.put("P_LABELBESTELLUNGNR", cLabelBestellungnr);
			mapParameter.put("P_BELEGKENNUNG", baueKennungBestellung(bestellungDto, theClientDto));

			mapParameter.put("P_LABELRAHMENODERLIEFERTERMIN", cLabelRahmenOderLiefertermin);

			mapParameter.put("P_LIEFERTERMIN", Helper.formatDatum(bestellungDto.getDLiefertermin(), locDruck));
			mapParameter.put("P_LIEFERART", getLocaleFac()
					.lieferartFindByIIdLocaleOhneExc(bestellungDto.getLieferartIId(), locDruck, theClientDto));
			mapParameter.put("P_ZAHLUNGSKONDITION", getMandantFac()
					.zahlungszielFindByIIdLocaleOhneExc(bestellungDto.getZahlungszielIId(), locDruck, theClientDto));
			mapParameter.put("P_PROJEKTBEZ", bestellungDto.getCBez());
			if (bestellungDto.getKostenstelleIId() != null) {
				KostenstelleDto kostenstelleDto = getSystemFac()
						.kostenstelleFindByPrimaryKey(bestellungDto.getKostenstelleIId());
				mapParameter.put(LPReport.P_KOSTENSTELLE, kostenstelleDto.getCNr());
			}

			String cBriefanrede = "";

			if (oAnsprechpartnerDto != null) {
				cBriefanrede = getPartnerServicesFac().getBriefanredeFuerBeleg(bestellungDto.getAnsprechpartnerIId(),
						lieferantDto.getPartnerIId(), locDruck, theClientDto);
			} else {
				// neutrale Anrede
				cBriefanrede = getBriefanredeNeutralOderPrivatperson(lieferantDto.getPartnerIId(), locDruck,
						theClientDto);
			}

			mapParameter.put("Briefanrede", cBriefanrede);

			PersonalDto oPersonalBenutzer = getPersonalFac().personalFindByPrimaryKey(theClientDto.getIDPersonal(),
					theClientDto);
			PersonalDto oPersonalAnleger = getPersonalFac()
					.personalFindByPrimaryKey(bestellungDto.getPersonalIIdAnlegen(), theClientDto);
			mapParameter.put("P_UNSER_ZEICHEN", Helper.getKurzzeichenkombi(oPersonalBenutzer.getCKurzzeichen(),
					oPersonalAnleger.getCKurzzeichen()));

			mapParameter.put("P_BELEGDATUM", Helper.formatDatum(bestellungDto.getDBelegdatum(), locDruck));
			mapParameter.put("P_WAEHRUNG", bestellungDto.getWaehrungCNr());
			mapParameter.put("P_ALLGEMEINER_RABATT", bestellungDto.getFAllgemeinerRabattsatz());
			mapParameter.put("P_ALLGEMEINER_RABATT_STRING",
					Helper.formatZahl(bestellungDto.getFAllgemeinerRabattsatz(), locDruck));

			mapParameter.put("P_TERMINEUNTERSCHIEDLICH", new Boolean(bTermineUnterschiedlich));

			mapParameter.put("Kopftext", Helper.formatStyledTextForJasper(sKopftext));

			mapParameter.put("P_BELEGWERT", bestellungDto.getNBestellwert());

			// die folgenden Felder werden im Report mit jeweils einer
			// trennenden Leerzeile
			// hintereinandergehaengt
			// MR 20071014 Folgende Felder muessen auch einzeln an Report
			// uebergeben werden.
			StringBuffer buff = new StringBuffer();

			// Externer Kommentar
			if (bestellungDto.getXExternerKommentar() != null && bestellungDto.getXExternerKommentar().length() > 0) {
				mapParameter.put(P_EXTERNERKOMMENTAR,
						Helper.formatStyledTextForJasper(bestellungDto.getXExternerKommentar()));
				buff.append(bestellungDto.getXExternerKommentar()).append("\n\n");
			}

			// Fusstext
			if (sFusstext != null) {
				mapParameter.put("P_FUSSTEXT", Helper.formatStyledTextForJasper(sFusstext));
				buff.append(sFusstext).append("\n\n");
			}

			StringBuffer buffVertreter = new StringBuffer();
			// Anrede des Mandanten
			String sMandantAnrede = mandantDto.getPartnerDto().formatFixName1Name2();
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
				vertreterDto = getPersonalFac().personalFindByPrimaryKey(bestellungDto.getPersonalIIdAnforderer(),
						theClientDto);
			}

			if (vertreterDto != null) {
				String sVertreterUFTitelName2Name1 = vertreterDto.formatFixUFTitelName2Name1();
				mapParameter.put(P_VERTRETER_UNTERSCHRIFTSFUNKTION_UND_NAME, sVertreterUFTitelName2Name1);
				buff.append(sVertreterUFTitelName2Name1);
				buffVertreter.append(sVertreterUFTitelName2Name1);

				if (vertreterDto.getCUnterschriftstext() != null && vertreterDto.getCUnterschriftstext().length() > 0) {
					String sUnterschriftstext = vertreterDto.getCUnterschriftstext();
					mapParameter.put(P_VERTRETER_UNTERSCHRIFTSTEXT, sUnterschriftstext);
					buff.append("\n").append(sUnterschriftstext);
					buffVertreter.append("\n").append(sUnterschriftstext);
				}

				// Vertreter Kontaktdaten
				String sVertreterEmail = vertreterDto.getCEmail();

				String sVertreterFaxDirekt = vertreterDto.getCDirektfax();

				String sVertreterFax = vertreterDto.getCFax();

				String sVertreterTelefon = vertreterDto.getCTelefon();
				mapParameter.put(LPReport.P_VERTRETEREMAIL, sVertreterEmail != null ? sVertreterEmail : "");
				if (sVertreterFaxDirekt != null && sVertreterFaxDirekt != "") {
					mapParameter.put(LPReport.P_VERTRETERFAX, sVertreterFaxDirekt);
				} else {
					mapParameter.put(LPReport.P_VERTRETERFAX, sVertreterFax != null ? sVertreterFax : "");
				}
				mapParameter.put(LPReport.P_VERTRETERTELEFON, sVertreterTelefon != null ? sVertreterTelefon : "");

				mapParameter.put(LPReport.P_VERTRETER_TELEFON_FIRMA_MIT_DW,
						getPartnerFac().enrichNumber(mandantDto.getPartnerIId(), PartnerFac.KOMMUNIKATIONSART_TELEFON,
								theClientDto, vertreterDto.getCTelefon(), false));

			}

			mapParameter.put(P_VERTRETER, Helper.formatStyledTextForJasper(buffVertreter.toString()));
			mapParameter.put("P_SUMMARY", Helper.formatStyledTextForJasper(buff.toString()));

			mapParameter.put("P_STORNIERUNGSDATUM", bestellungDto.getTStorniert());
			mapParameter.put("P_AENDERUNGSBESTELLUNGSDATUM", bestellungDto.getTAenderungsbestellung());
			mapParameter.put("P_AENDERUNGSBESTELLUNG", new Boolean(bestellungDto.getTAenderungsbestellung() != null));
			mapParameter.put("P_AENDERUNGSBESTELLUNG_VERSION", bestellungDto.getIVersion());

			if (bestellungDto.getAnsprechpartnerIIdLieferadresse() != null) {
				AnsprechpartnerDto ansprechpartnerLieferadresseDto = getAnsprechpartnerFac()
						.ansprechpartnerFindByPrimaryKey(bestellungDto.getAnsprechpartnerIIdLieferadresse(),
								theClientDto);
				mapParameter.put("P_ANSPRECHPARTNER_LIEFERADRESSE",
						ansprechpartnerLieferadresseDto.getPartnerDto().formatFixTitelName1Name2());
			}

			initJRDS(mapParameter, BestellungReportFac.REPORT_MODUL, BestellungReportFac.REPORT_ABHOLAUFTRAG,
					theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

			return getReportPrint();

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return print;
	}

	/**
	 * Kennung fuer eine Bestellung zum Andrucken zusammenbauen.
	 * 
	 * @param bestellungDtoI die Bestellung
	 * @param sUserI         der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 * @return String die Kennung
	 */
	private String baueKennungBestellung(BestellungDto bestellungDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		StringBuffer kennung = null;
		try {
			BelegartDto belegartDto = getLocaleFac().belegartFindByPrimaryKey(bestellungDtoI.getBelegartCNr(),
					theClientDto);

			kennung = new StringBuffer(belegartDto.getCKurzbezeichnung());
			kennung.append(" ").append(bestellungDtoI.getCNr());
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return kennung.toString();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printRahmenuebersicht(Integer bestellungIId, TheClientDto theClientDto) {
		this.useCase = UC_RAHMENUEBERSICHT;
		HashMap<String, Object> mapParameter = new HashMap<String, Object>();
		ArrayList alDaten = new ArrayList();
		try {
			BestellungDto bestellungDto = getBestellungFac().bestellungFindByPrimaryKey(bestellungIId);

			BestellpositionDto[] bestposDtos = getBestellpositionFac().bestellpositionFindByBestellung(bestellungIId,
					theClientDto);

			for (int i = 0; i < bestposDtos.length; i++) {
				befuelleZeileRahmenUebersicht(bestellungDto, bestposDtos[i], alDaten, theClientDto);
			}

			BestellungDto[] abrufBestellungenDtos = getBestellungFac()
					.abrufBestellungenfindByRahmenbestellung(bestellungIId, theClientDto);

			for (int i = 0; i < abrufBestellungenDtos.length; i++) {
				BestellpositionDto[] abrufaufposDtos = getBestellpositionFac()
						.bestellpositionFindByBestellung(abrufBestellungenDtos[i].getIId(), theClientDto);
				for (int j = 0; j < abrufaufposDtos.length; j++) {
					befuelleZeileRahmenUebersicht(abrufBestellungenDtos[i], abrufaufposDtos[j], alDaten, theClientDto);
				}

			}

			Object[][] returnArray = new Object[alDaten
					.size()][AuftragReportFac.REPORT_RAHMENUEBERSICHT_ANZAHL_SPALTEN];
			data = (Object[][]) alDaten.toArray(returnArray);

			// die Parameter dem Report uebergeben

			mapParameter.put("P_BESTELLUNG", bestellungDto.getCNr());
			mapParameter.put("P_LIEFERTERMIN", bestellungDto.getDLiefertermin());
			mapParameter.put("P_PROJEKT", bestellungDto.getCBez());

			LieferantDto lieferantDto = getLieferantFac()
					.lieferantFindByPrimaryKey(bestellungDto.getLieferantIIdBestelladresse(), theClientDto);
			mapParameter.put("P_LIEFERANT_NAME1", lieferantDto.getPartnerDto().getCName1nachnamefirmazeile1());
			mapParameter.put("P_LIEFERANT_NAME2", lieferantDto.getPartnerDto().getCName2vornamefirmazeile2());
			mapParameter.put("P_LIEFERANT_NAME3", lieferantDto.getPartnerDto().getCName3vorname2abteilung());

			if (lieferantDto.getPartnerDto().getLandplzortDto() != null) {
				mapParameter.put("P_LIEFERANT_STRASSE", lieferantDto.getPartnerDto().getCStrasse());
				mapParameter.put("P_LIEFERANT_LAND",
						lieferantDto.getPartnerDto().getLandplzortDto().getLandDto().getCLkz());
				mapParameter.put("P_LIEFERANT_PLZ", lieferantDto.getPartnerDto().getLandplzortDto().getCPlz());
				mapParameter.put("P_LIEFERANT_ORT",
						lieferantDto.getPartnerDto().getLandplzortDto().getOrtDto().getCName());
			}

			mapParameter.put("P_ZAHLUNGSZIEL", getMandantFac().zahlungszielFindByIIdLocaleOhneExc(
					bestellungDto.getZahlungszielIId(), theClientDto.getLocUi(), theClientDto));

			mapParameter.put("P_LIEFERART", getLocaleFac().lieferartFindByIIdLocaleOhneExc(
					bestellungDto.getLieferartIId(), theClientDto.getLocUi(), theClientDto));

			if (bestellungDto.getKostenstelleIId() != null) {
				KostenstelleDto kostenstelleDto = getSystemFac()
						.kostenstelleFindByPrimaryKey(bestellungDto.getKostenstelleIId());
				mapParameter.put("P_KOSTENSTELLE", kostenstelleDto.formatKostenstellenbezeichnung());
			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		initJRDS(mapParameter, BestellungReportFac.REPORT_MODUL, BestellungReportFac.REPORT_RAHMENUEBERSICHT,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	private void befuelleZeileRahmenUebersicht(BestellungDto bestellungDto, BestellpositionDto bestellpositionDto,
			ArrayList alDaten, TheClientDto theClientDto) {

		if (bestellpositionDto.getNMenge() != null) {
			Object[] oZeile = new Object[REPORT_RAHMENUEBERSICHT_ANZAHL_SPALTEN];

			oZeile[REPORT_RAHMENUEBERSICHT_BESTELLUNGART] = bestellungDto.getBestellungartCNr();
			oZeile[REPORT_RAHMENUEBERSICHT_BESTELLWERT] = bestellungDto.getNGesamtwertinbelegwaehrung();
			oZeile[REPORT_RAHMENUEBERSICHT_BESTELLNR] = bestellungDto.getCNr();
			oZeile[REPORT_RAHMENUEBERSICHT_BELEGDATUM] = bestellungDto.getDBelegdatum();
			oZeile[REPORT_RAHMENUEBERSICHT_LIEFERANTENANGEBOTNUMMER] = bestellungDto.getCLieferantenangebot();
			oZeile[REPORT_RAHMENUEBERSICHT_LIEFERTERMIN] = bestellungDto.getDLiefertermin();
			oZeile[REPORT_RAHMENUEBERSICHT_MENGE] = bestellpositionDto.getNMenge();

			Boolean bStorniert = false;
			if (bestellungDto.getStatusCNr().equals(LocaleFac.STATUS_STORNIERT)) {
				bStorniert = true;
			}
			oZeile[REPORT_RAHMENUEBERSICHT_STORNIERT] = bStorniert;

			oZeile[REPORT_RAHMENUEBERSICHT_PREIS] = bestellpositionDto.getNNettogesamtPreisminusRabatte();

			oZeile[REPORT_RAHMENUEBERSICHT_KEIN_RAHMENBEZUG] = Boolean.FALSE;

			if (bestellungDto.getBestellungartCNr().equals(BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR)
					&& bestellpositionDto.getIBestellpositionIIdRahmenposition() == null) {
				oZeile[REPORT_RAHMENUEBERSICHT_KEIN_RAHMENBEZUG] = Boolean.TRUE;
			}

			if (bestellpositionDto.getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_IDENT)) {

				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(bestellpositionDto.getArtikelIId(),
						theClientDto);

				oZeile[REPORT_RAHMENUEBERSICHT_ARTIKELNUMMER] = aDto.getCNr();
				oZeile[REPORT_RAHMENUEBERSICHT_ARTIKELBEZEICHNUNG] = aDto.formatBezeichnung();

			} else {
				oZeile[REPORT_RAHMENUEBERSICHT_ARTIKELBEZEICHNUNG] = bestellpositionDto.getCBez();
			}

			alDaten.add(oZeile);

			try {

				WareneingangspositionDto[] weposDtos = getWareneingangFac()
						.wareneingangspositionFindByBestellpositionIId(bestellpositionDto.getIId());

				String ls = "";
				for (int u = 0; u < weposDtos.length; u++) {

					WareneingangspositionDto weposDto = weposDtos[u];
					oZeile = oZeile.clone();

					if (bestellpositionDto.getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_IDENT)) {

						ArtikelDto aDto = getArtikelFac()
								.artikelFindByPrimaryKeySmall(bestellpositionDto.getArtikelIId(), theClientDto);

						oZeile[REPORT_RAHMENUEBERSICHT_ARTIKELNUMMER] = aDto.getCNr();
						oZeile[REPORT_RAHMENUEBERSICHT_ARTIKELBEZEICHNUNG] = aDto.formatBezeichnung();

					} else {
						oZeile[AuftragReportFac.REPORT_RAHMENUEBERSICHT_ARTIKELBEZEICHNUNG] = bestellpositionDto
								.getCBez();
					}

					oZeile[REPORT_RAHMENUEBERSICHT_MENGE] = weposDto.getNGeliefertemenge();

					oZeile[REPORT_RAHMENUEBERSICHT_PREIS] = weposDto.getNGelieferterpreis();

					WareneingangDto weDto = getWareneingangFac()
							.wareneingangFindByPrimaryKey(weposDto.getWareneingangIId());

					oZeile[REPORT_RAHMENUEBERSICHT_WARENEINGANG] = weDto.getCLieferscheinnr();

					if (weDto.getEingangsrechnungIId() != null) {
						oZeile[REPORT_RAHMENUEBERSICHT_EINGANGSRECHNUNG] = getEingangsrechnungFac()
								.eingangsrechnungFindByPrimaryKey(weDto.getEingangsrechnungIId()).getCNr();
					}

					alDaten.add(oZeile);
				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printBestellungWareneingangsJournal(ReportJournalKriterienDto krit, Integer artikelklasseIId,
			Integer artikelgruppeIId, String artikelCNrVon, String artikelCNrBis, String projektCBezeichnung,
			Integer auftragIId, boolean bMitWarenverbrauch, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {

		Session session = null;
		useCase = UC_BESTELLUNG_WARENEINGANG;
		SessionFactory factory = FLRSessionFactory.getFactory();
		session = factory.openSession();

		Map<String, Object> map = new TreeMap<String, Object>();

		Criteria c = session.createCriteria(FLRWareneingangspositionen.class);
		Criteria cWe = c.createCriteria(WareneingangFac.FLR_WEPOS_FLRWARENEINGANG);
		Criteria cBesPos = c.createCriteria(WareneingangFac.FLR_WEPOS_FLRBESTELLPOSITION);
		Criteria cArt = cBesPos.createCriteria(BestellpositionFac.FLR_BESTELLPOSITION_FLRARTIKEL);
		Criteria cBestellung = cWe.createCriteria(WareneingangFac.FLR_WE_FLRBESTELLUNG);
		cBestellung.add(Restrictions.eq(BestellungFac.FLR_BESTELLUNG_MANDANT_C_NR, theClientDto.getMandant()));
		// Datum von
		if (krit.dVon != null) {
			cWe.add(Restrictions.ge(WareneingangFac.FLRSPALTE_T_WARENEINGANGSDATUM, Helper.cutDate(krit.dVon)));
		}
		// Datum bis
		if (krit.dBis != null) {

			java.sql.Date dBisTemp = Helper
					.cutDate(Helper.addiereTageZuDatum(new java.sql.Date(krit.dBis.getTime()), 1));
			cWe.add(Restrictions.lt(WareneingangFac.FLRSPALTE_T_WARENEINGANGSDATUM, dBisTemp));
		}
		// Filter nach Projektbezeichnung
		if (projektCBezeichnung != null) {
			cBestellung.add(Restrictions.ilike(BestellungFac.FLR_BESTELLUNG_C_BEZPROJEKTBEZEICHNUNG,
					"%" + projektCBezeichnung + "%"));
		}
		// Filter nach Auftrag
		if (auftragIId != null) {
			cBestellung.add(Restrictions.eq(BestellungFac.FLR_BESTELLUNG_AUFTRAG_I_ID, auftragIId));
		}

		LpBelegnummerFormat f = getBelegnummerGeneratorObj().getBelegnummernFormat(theClientDto.getMandant());
		Integer iGeschaeftsjahr = getParameterFac().getGeschaeftsjahr(theClientDto.getMandant());
		String sMandantKuerzel = getParameterFac().getMandantparameter(theClientDto.getMandant(),
				ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_BELEGNUMMER_MANDANTKENNUNG).getCWert();
		// reportjournal: 06 belegnummer von
		// reportjournalbelegnummer: 1 (von) hier funktionierts fast gleich
		// wie bei den Direktfiltern
		if (krit.sBelegnummerVon != null) {
			String sVon = HelperServer.getBelegnummernFilterForHibernateCriterias(f, iGeschaeftsjahr, sMandantKuerzel,
					krit.sBelegnummerVon);
			cBestellung.add(Restrictions.ge(BestellungFac.FLR_BESTELLUNG_C_NR, sVon));

			map.put("P_BELEGNUMMER_VON", krit.sBelegnummerVon);
		}
		// reportjournal: 07 belegnummer bis
		// reportjournalbelegnummer: 2 (bis) detto
		if (krit.sBelegnummerBis != null) {
			String sBis = HelperServer.getBelegnummernFilterForHibernateCriterias(f, iGeschaeftsjahr, sMandantKuerzel,
					krit.sBelegnummerBis);
			cBestellung.add(Restrictions.le(BestellungFac.FLR_BESTELLUNG_C_NR, sBis));

			map.put("P_BELEGNUMMER_BIS", krit.sBelegnummerBis);

		}
		// Einschraenkung nach einer bestimmten Kostenstelle
		if (krit.kostenstelleIId != null) {
			cBestellung.add(Restrictions.eq(BestellungFac.FLR_BESTELLUNG_KOSTENSTELLE_I_ID, krit.kostenstelleIId));
		}

		// Einschraenkung nach einem bestimmten Lieferanten
		if (krit.lieferantIId != null) {
			cBestellung.add(
					Restrictions.eq(BestellungFac.FLR_BESTELLUNG_LIEFERANT_I_ID_BESTELLADRESSE, krit.lieferantIId));
		}

		if (artikelklasseIId != null) {
			cArt.createCriteria(ArtikelFac.FLR_ARTIKEL_FLRARTIKELKLASSE).add(Restrictions.eq("i_id", artikelklasseIId));
		}
		if (artikelgruppeIId != null) {
			cArt.createCriteria(ArtikelFac.FLR_ARTIKEL_FLRARTIKELGRUPPE).add(Restrictions.eq("i_id", artikelgruppeIId));
		}
		if (artikelCNrVon != null) {
			cArt.add(Restrictions.ge(ArtikelFac.FLR_ARTIKEL_C_NR, artikelCNrVon));
		}
		if (artikelCNrBis != null) {
			cArt.add(Restrictions.le(ArtikelFac.FLR_ARTIKEL_C_NR, artikelCNrBis));
		}

		// Sortierung nach Kostenstelle ist immer die erste Sortierung
		if (krit.bSortiereNachKostenstelle) {
			cBestellung.createCriteria(BestellungFac.FLR_BESTELLUNG_FLRKOSTENSTELLE).addOrder(Order.asc("c_nr"));
		}

		if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_DATUM) {
			cWe.addOrder(Order.asc(WareneingangFac.FLRSPALTE_T_WARENEINGANGSDATUM));
			map.put(LPReport.P_SORTIERUNG,
					getTextRespectUISpr("bes.wedatum", theClientDto.getMandant(), theClientDto.getLocUi()));

		} else if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_IDENT) {
			cArt.addOrder(Order.asc(ArtikelFac.FLR_ARTIKEL_C_NR));
			map.put(LPReport.P_SORTIERUNG,
					getTextRespectUISpr("lp.artikel", theClientDto.getMandant(), theClientDto.getLocUi()));
			cWe.addOrder(Order.asc(WareneingangFac.FLR_WE_T_WARENEINGANGSDATUM));
		} else if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_BELEGNUMMER) {
			cBestellung.addOrder(Order.asc(BestellungFac.FLR_BESTELLUNG_C_NR));

			cWe.addOrder(Order.asc(WareneingangFac.FLR_WE_T_WARENEINGANGSDATUM));

			map.put(LPReport.P_SORTIERUNG,
					getTextRespectUISpr("bes.belegnummer", theClientDto.getMandant(), theClientDto.getLocUi()));
		} else if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PROJEKT) {
			cBestellung.addOrder(Order.asc(BestellungFac.FLR_BESTELLUNG_C_BEZPROJEKTBEZEICHNUNG));
			cWe.addOrder(Order.asc(WareneingangFac.FLR_WE_T_WARENEINGANGSDATUM));
			map.put(LPReport.P_SORTIERUNG,
					getTextRespectUISpr("lp.projekt", theClientDto.getMandant(), theClientDto.getLocUi()));
		} else if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
			cBestellung.createCriteria(BestellungFac.FLR_BESTELLUNG_FLRLIEFERANT)
					.createCriteria(LieferantFac.FLR_PARTNER)
					.addOrder(Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));
			cBestellung.addOrder(Order.asc(BestellungFac.FLR_BESTELLUNG_C_NR));

			// PJ20125
			cWe.addOrder(Order.asc(WareneingangFac.FLR_WE_C_LIEFERSCHEIN));

			map.put(LPReport.P_SORTIERUNG,
					getTextRespectUISpr("lp.partner", theClientDto.getMandant(), theClientDto.getLocUi()));
		} else {
			c.addOrder(Order.asc("i_id"));
			map.put(LPReport.P_SORTIERUNG, "i_id");
		}

		cBesPos.addOrder(Order.asc("i_sort"));

		List<?> list = c.list();
		data = new Object[list.size()][REPORT_BSWARENEINGANGSJOURNAL_ANZAHL_SPALTEN];
		int i = 0;
		for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
			FLRWareneingangspositionen w = (FLRWareneingangspositionen) iter.next();
			data[i][REPORT_BSWARENEINGANGSJOURNAL_PREISE_ERFASST] = Helper.short2Boolean(w.getB_preiseerfasst());
			if (w.getFlrwareneingang().getT_wareneingansdatum() != null) {
				data[i][REPORT_BSWARENEINGANGSJOURNAL_DATUM] = w.getFlrwareneingang().getT_wareneingansdatum();
			}
			if (w.getFlrwareneingang().getFlrbestellung().getC_nr() != null) {
				data[i][REPORT_BSWARENEINGANGSJOURNAL_BESTELLNUMMER] = w.getFlrwareneingang().getFlrbestellung()
						.getC_nr();
				data[i][REPORT_BSWARENEINGANGSJOURNAL_LIEFERART] = w.getFlrwareneingang().getFlrbestellung()
						.getFlrlieferart().getC_nr();

			} else {
				data[i][REPORT_BSWARENEINGANGSJOURNAL_BESTELLNUMMER] = "";
			}

			data[i][REPORT_BSWARENEINGANGSJOURNAL_STATUS] = w.getFlrwareneingang().getFlrbestellung()
					.getBestellungstatus_c_nr();

			if (w.getFlrbestellposition().getFlrartikel().getC_nr() != null) {

				data[i][REPORT_BSWARENEINGANGSJOURNAL_IDENT] = w.getFlrbestellposition().getFlrartikel().getC_nr();

				data[i][REPORT_BSWARENEINGANGSJOURNAL_ARTIKELREFERENZNUMMER] = w.getFlrbestellposition().getFlrartikel()
						.getC_referenznr();

				ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(
						w.getFlrbestellposition().getFlrartikel().getI_id(), theClientDto);
				data[i][REPORT_BSWARENEINGANGSJOURNAL_BEZEICHNUNG] = artikelDto.getCBezAusSpr();

				data[i][REPORT_BSWARENEINGANGSJOURNAL_ZUSATZBEZEICHNUNG] = artikelDto.getCZBezAusSpr();
				data[i][REPORT_BSWARENEINGANGSJOURNAL_ZUSATZBEZEICHNUNG2] = artikelDto.getCZBez2AusSpr();
				data[i][REPORT_BSWARENEINGANGSJOURNAL_KURZBEZEICHNUNG] = artikelDto.getCKBezAusSpr();
				data[i][REPORT_BSWARENEINGANGSJOURNAL_BESTELLMENGENEINHEIT] = artikelDto.getEinheitCNrBestellung();
				data[i][REPORT_BSWARENEINGANGSJOURNAL_UMRECHNUNGSFAKTOR] = artikelDto.getNUmrechnungsfaktor();
				data[i][REPORT_BSWARENEINGANGSJOURNAL_BESTELLMENGENEINHEIT_INVERS] = Helper
						.short2Boolean(artikelDto.getbBestellmengeneinheitInvers());
				data[i][REPORT_BSWARENEINGANGSJOURNAL_LAGERPLAETZE] = getLagerFac()
						.getLagerplaezteEinesArtikels(artikelDto.getIId(), null);

				if (Helper.short2boolean(w.getFlrbestellposition().getFlrartikel().getB_chargennrtragend()) == true
						|| Helper.short2boolean(
								w.getFlrbestellposition().getFlrartikel().getB_seriennrtragend()) == true) {
					data[i][REPORT_BSWARENEINGANGSJOURNAL_SUBREPORT_SNRCHNR] = getLagerFac()
							.getSerienChargennummernEinerBelegpositionSubreport(LocaleFac.BELEGART_BESTELLUNG,
									w.getI_id(), theClientDto);
				}

			} else {
				data[i][REPORT_BSWARENEINGANGSJOURNAL_IDENT] = "";
			}
			if (w.getFlrbestellposition().getC_bezeichnung() != null) {
				data[i][REPORT_BSWARENEINGANGSJOURNAL_BEZEICHNUNG] = w.getFlrbestellposition().getC_bezeichnung();
				data[i][REPORT_BSWARENEINGANGSJOURNAL_ZUSATZBEZEICHNUNG] = w.getFlrbestellposition()
						.getC_zusatzbezeichnung();
			}
			if (bMitWarenverbrauch) {

				List<SeriennrChargennrMitMengeDto> snrs = null;

				if (Helper.short2boolean(w.getFlrbestellposition().getFlrartikel().getB_chargennrtragend()) == true
						|| Helper.short2boolean(
								w.getFlrbestellposition().getFlrartikel().getB_seriennrtragend()) == true) {
					snrs = getLagerFac().getAllSeriennrchargennrEinerBelegartpositionUeberHibernate(
							LocaleFac.BELEGART_BESTELLUNG, w.getI_id());
				}

				data[i][REPORT_BSWARENEINGANGSJOURNAL_WA_REFERENZ] = getLagerFac().getWarenausgangsreferenzSubreport(
						LocaleFac.BELEGART_BESTELLUNG, w.getI_id(), snrs, theClientDto);
			}
			// SP903
			if (w.getFlrbestellposition().getPosition_i_id_artikelset() != null) {
				data[i][REPORT_BSWARENEINGANGSJOURNAL_SETARTIKEL_TYP] = ArtikelFac.SETARTIKEL_TYP_POSITION;
			} else {

				Session sessionSet = FLRSessionFactory.getFactory().openSession();

				sessionSet = factory.openSession();
				Criteria critSet = sessionSet.createCriteria(FLRBestellpositionReport.class);
				critSet.add(Restrictions.eq("position_i_id_artikelset", w.getFlrbestellposition().getI_id()));

				int iZeilen = critSet.list().size();

				if (iZeilen > 0) {
					data[i][REPORT_BSWARENEINGANGSJOURNAL_SETARTIKEL_TYP] = ArtikelFac.SETARTIKEL_TYP_KOPF;
				}
				sessionSet.close();

			}

			data[i][REPORT_BSWARENEINGANGSJOURNAL_PROJEKT] = w.getFlrbestellposition().getFlrbestellung()
					.getC_bezprojektbezeichnung();
			data[i][REPORT_BSWARENEINGANGSJOURNAL_LIEFERANT] = w.getFlrbestellposition().getFlrbestellung()
					.getFlrlieferant().getFlrpartner().getC_name1nachnamefirmazeile1();

			if (w.getN_geliefertemenge() != null) {
				data[i][REPORT_BSWARENEINGANGSJOURNAL_MENGE] = w.getN_geliefertemenge();
			} else {
				data[i][REPORT_BSWARENEINGANGSJOURNAL_MENGE] = new BigDecimal(0);
			}

			// /PJ20027
			data[i][REPORT_BSWARENEINGANGSJOURNAL_LIEFERTERMIN] = w.getFlrbestellposition()
					.getT_uebersteuerterliefertermin();
			data[i][REPORT_BSWARENEINGANGSJOURNAL_AB_TERMIN] = w.getFlrbestellposition()
					.getT_auftragsbestaetigungstermin();

			if (w.getN_gelieferterpreis() != null) {
				if (getTheJudgeFac().hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_EINKAUF, theClientDto)) {

					BigDecimal kurs = new BigDecimal(w.getFlrwareneingang().getFlrbestellung()
							.getF_wechselkursmandantwaehrungbestellungswaehrung());
					if (kurs.doubleValue() != 0) {
						data[i][REPORT_BSWARENEINGANGSJOURNAL_GELIEFERTERPREIS] = w.getN_gelieferterpreis().divide(kurs,
								4, BigDecimal.ROUND_HALF_EVEN);
					} else {
						data[i][REPORT_BSWARENEINGANGSJOURNAL_GELIEFERTERPREIS] = new BigDecimal(0);
					}

				} else {
					data[i][REPORT_BSWARENEINGANGSJOURNAL_GELIEFERTERPREIS] = null;
				}
			} else {
				if (getTheJudgeFac().hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_EINKAUF, theClientDto)) {
					data[i][REPORT_BSWARENEINGANGSJOURNAL_GELIEFERTERPREIS] = new BigDecimal(0);
				} else {
					data[i][REPORT_BSWARENEINGANGSJOURNAL_GELIEFERTERPREIS] = null;
				}
			}

			if (w.getN_einstandspreis() != null) {
				if (getTheJudgeFac().hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_EINKAUF, theClientDto)) {

					BigDecimal kurs = new BigDecimal(w.getFlrwareneingang().getFlrbestellung()
							.getF_wechselkursmandantwaehrungbestellungswaehrung());
					if (kurs.doubleValue() != 0) {
						data[i][REPORT_BSWARENEINGANGSJOURNAL_EINSTANDSPREIS] = w.getN_einstandspreis().divide(kurs, 4,
								BigDecimal.ROUND_HALF_EVEN);
					} else {
						data[i][REPORT_BSWARENEINGANGSJOURNAL_EINSTANDSPREIS] = new BigDecimal(0);
					}

				} else {
					data[i][REPORT_BSWARENEINGANGSJOURNAL_EINSTANDSPREIS] = null;
				}
			} else {
				if (getTheJudgeFac().hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_EINKAUF, theClientDto)) {
					data[i][REPORT_BSWARENEINGANGSJOURNAL_EINSTANDSPREIS] = new BigDecimal(0);
				} else {
					data[i][REPORT_BSWARENEINGANGSJOURNAL_EINSTANDSPREIS] = null;
				}
			}

			data[i][REPORT_BSWARENEINGANGSJOURNAL_LIEFERSCHEIN] = w.getFlrwareneingang().getC_lieferscheinnr();
			data[i][REPORT_BSWARENEINGANGSJOURNAL_LIEFERSCHEINDATUM] = w.getFlrwareneingang().getT_lieferscheindatum();

			if (w.getFlrwareneingang().getFlreingangsrechnung() != null) {
				data[i][REPORT_BSWARENEINGANGSJOURNAL_EINGANGSRECHNUNG] = w.getFlrwareneingang()
						.getFlreingangsrechnung().getC_nr();
			}

			if (w.getFlrwareneingang().getFlrbestellung().getFlrkostenstelle() != null) {

				data[i][REPORT_BSWARENEINGANGSJOURNAL_KOSTENSTELLE] = w.getFlrwareneingang().getFlrbestellung()
						.getFlrkostenstelle().getC_nr();
			}
			data[i][REPORT_BSWARENEINGANGSJOURNAL_ZUBUCHUNGSLAGER] = w.getFlrwareneingang().getFlrlager().getC_nr();
			data[i][REPORT_BSWARENEINGANGSJOURNAL_RABATTSATZ] = w.getFlrwareneingang().getF_rabattsatz();
			if (w.getFlrwareneingang().getFlrbestellung().getAuftrag_i_id() != null) {
				data[i][REPORT_BSWARENEINGANGSJOURNAL_AUFTRAG] = w.getFlrwareneingang().getFlrbestellung()
						.getFlrauftrag().getC_nr();
			}
			if (w.getFlrbestellposition().getFlrartikel().getFlrartikelklasse() != null) {
				w.getFlrbestellposition().getFlrartikel().getFlrartikelklasse().getC_nr();
				data[i][REPORT_BSWARENEINGANGSJOURNAL_ARTIKELKLASSE] = w.getFlrbestellposition().getFlrartikel()
						.getFlrartikelklasse().getC_nr();
			}
			if (w.getFlrbestellposition().getFlrartikel().getFlrartikelgruppe() != null) {
				w.getFlrbestellposition().getFlrartikel().getFlrartikelgruppe().getC_nr();
				data[i][REPORT_BSWARENEINGANGSJOURNAL_ARTIKELGRUPPE] = w.getFlrbestellposition().getFlrartikel()
						.getFlrartikelgruppe().getC_nr();
			}

			data[i][REPORT_BSWARENEINGANGSJOURNAL_TRANSPORTKOSTEN] = w.getFlrwareneingang().getN_transportkosten();
			data[i][REPORT_BSWARENEINGANGSJOURNAL_BANKSPESEN] = w.getFlrwareneingang().getN_bankspesen();
			data[i][REPORT_BSWARENEINGANGSJOURNAL_ZOLLKOSTEN] = w.getFlrwareneingang().getN_zollkosten();
			data[i][REPORT_BSWARENEINGANGSJOURNAL_SONSTIGEKOSTEN] = w.getFlrwareneingang().getN_sonstigespesen();
			data[i][REPORT_BSWARENEINGANGSJOURNAL_GK_FAKTOR] = w.getFlrwareneingang().getF_gemeinkostenfaktor();

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
		initJRDS(map, BestellungReportFac.REPORT_MODUL, BestellungReportFac.REPORT_BESTELLUNG_JOURNAL_WARENEINGANG,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		session.close();
		return getReportPrint();
	}

	public void sendMahnlauf(String cKommuniaktionsart, BSMahnlaufDto bsMahnlaufDto, Locale absenderLocale,
			TheClientDto theClientDto) throws EJBExceptionLP, Throwable {
		// BSMahnlaufDto bsMahnlaufDto = getBSMahnlaufDto();
		if (bsMahnlaufDto != null) {
			Integer mahnlaufIId = bsMahnlaufDto.getIId();
			String sAbsenderadresse = null;
			if (mahnlaufIId != null) {
				// Pruefung ob fuer Personal Mail definiert ist
				if (PartnerFac.KOMMUNIKATIONSART_EMAIL.equals(cKommuniaktionsart)) {
					PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(theClientDto.getIDPersonal(),
							theClientDto);
					if (personalDto.getCEmail() != null) {
						sAbsenderadresse = personalDto.getCEmail();
						if (sAbsenderadresse == null) {

							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_MAHNUNGSVERSAND_KEINE_ABSENDERADRESSE,
									new Exception(getTextRespectUISpr("bestellung.fehler.keinemailadressedefiniert",
											theClientDto.getMandant(), absenderLocale)));
						}
						if (!Helper.validateEmailadresse(sAbsenderadresse)) {
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_UNGUELTIGE_EMAILADRESSE,
									new Exception(
											getTextRespectUISpr("bestellung.fehler.ungueltigemailadressedefiniert",
													theClientDto.getMandant(), absenderLocale)));
						}
					} else {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_MAHNUNGSVERSAND_KEINE_ABSENDERADRESSE,
								new Exception(getTextRespectUISpr("bestellung.fehler.keinemailadressedefiniert",
										theClientDto.getMandant(), absenderLocale)));
					}

				}

				List<VersandauftragDto> delayedVersandauftrags = new ArrayList<VersandauftragDto>();
				// Holen der Sammelmahnungen
				ArrayList<JasperPrintLP> hm = getBestellungReportFac().getMahnungenFuerAlleLieferanten(mahnlaufIId,
						theClientDto, true, true);
				for (JasperPrintLP print : hm) {
					Integer iLieferant = (Integer) print.getAdditionalInformation("lieferantIId");
					Integer ansprechpartnerIId = (Integer) print.getAdditionalInformation("ansprechpartnerIId");

					HashMap bestellnummern = (HashMap) print.getAdditionalInformation("bestellnummern");

					// Lieferant holen
					Integer iPartnerIId = getLieferantFac().lieferantFindByPrimaryKey(iLieferant, theClientDto)
							.getPartnerIId();
					// Kommunikation holen
					PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(iPartnerIId, theClientDto);
					String partkommDto = null;
					String sEmpfaenger = null;
					if (PartnerFac.KOMMUNIKATIONSART_EMAIL.equals(cKommuniaktionsart)) {
						partkommDto = partnerDto.getCEmail();

						if (ansprechpartnerIId != null) {
//							AnsprechpartnerDto ansprechpartnerDto = getAnsprechpartnerFac()
//									.ansprechpartnerFindByPrimaryKey(ansprechpartnerIId, theClientDto);
//
							String komm = getPartnerFac().partnerkommFindOhneExec(partnerDto.getIId(),
									ansprechpartnerIId, PartnerFac.KOMMUNIKATIONSART_EMAIL, theClientDto.getMandant(),
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
					if (PartnerFac.KOMMUNIKATIONSART_FAX.equals(cKommuniaktionsart)) {
						partkommDto = partnerDto.getCFax();
						if (partkommDto != null) {

							if (ansprechpartnerIId != null) {
								AnsprechpartnerDto ansprechpartnerDto = getAnsprechpartnerFac()
										.ansprechpartnerFindByPrimaryKey(ansprechpartnerIId, theClientDto);

								if (ansprechpartnerDto.getCFax() != null) {

									partkommDto = ansprechpartnerDto.getCFax();

									if (!Helper.validateFaxnummer(ansprechpartnerDto.getCFax())) {
										sEmpfaenger = null;
									} else {
										sEmpfaenger = ansprechpartnerDto.getCFax();
									}
								}
							}

							sEmpfaenger = getPartnerFac().passeInlandsAuslandsVorwahlAn(partnerDto.getIId(),
									theClientDto.getMandant(), partkommDto, theClientDto);

							// = partkommDto.getCInhalt();
							if (sEmpfaenger == null && !Helper.validateFaxnummer(sEmpfaenger)) {
								sEmpfaenger = null;
							}
						}
					}
					if (sEmpfaenger != null) {
						VersandauftragDto versDto = new VersandauftragDto();
						versDto.setCEmpfaenger(sEmpfaenger);
						versDto.setCAbsenderadresse(sAbsenderadresse);
						String sLocMahnung = getTextRespectUISpr("lp.mahnung", theClientDto.getMandant(),
								absenderLocale);
						versDto.setCDateiname(sLocMahnung + ".pdf");
						long fiveMinutesInMillis = 1000 * 60 * 5;
						versDto.setTSendezeitpunktwunsch(
								new Timestamp(System.currentTimeMillis() + fiveMinutesInMillis));
						partnerDto.getLocaleCNrKommunikation();

						String betreff = sLocMahnung;

						if (bestellnummern.size() > 0) {
							Iterator it = bestellnummern.keySet().iterator();
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
						versDto.setOInhalt(exportToPDF(print.getPrint()));

						delayedVersandauftrags.add(versDto);
//						getVersandFac().createVersandauftrag(versDto, false, theClientDto);
						// Mahnungen auf gemahnt setzen
						BSMahnungDto[] bsMahnungDto = getBSMahnwesenFac().bsmahnungFindByBSMahnlaufIIdLieferantIId(
								mahnlaufIId, iLieferant, theClientDto.getMandant());
						for (int i = 0; i < bsMahnungDto.length; i++) {
							getBSMahnwesenFac().mahneBSMahnung(bsMahnungDto[i].getIId(), theClientDto);
						}
					}
				}

				getVersandFac().createVersandauftrags(delayedVersandauftrags, false, theClientDto);
			}
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void printWepEtikettOnServer(Integer wareneingangspositionId, TheClientDto theClientDto)
			throws RemoteException {
		printWepEtikettOnServer(wareneingangspositionId, null, theClientDto);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void printWepEtikettOnServer(Integer wareneingangspositionId, String chargennummer,
			TheClientDto theClientDto) throws RemoteException {

		String printer = getServerDruckerFacLocal().getPrinterNameByArbeitsplatzparameterOhneExc(
				ParameterFac.ARBEITSPLATZPARAMETER_DRUCKERNAME_MOBILES_WEP_ETIKETT, theClientDto);
		if (printer == null) {
			return;
		}

		WareneingangspositionDto weposDto = getWareneingangFac()
				.wareneingangspositionFindByPrimaryKey(wareneingangspositionId);
		WareneingangDto weDto = getWareneingangFac().wareneingangFindByPrimaryKey(weposDto.getWareneingangIId());
		BestellpositionDto besposDto = getBestellpositionFac()
				.bestellpositionFindByPrimaryKey(weposDto.getBestellpositionIId());
		ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(besposDto.getArtikelIId(), theClientDto);
		BestellungDto besDto = getBestellungFac().bestellungFindByPrimaryKey(besposDto.getBestellungIId());
		LieferantDto lieferantDto = getLieferantFac().lieferantFindByPrimaryKey(besDto.getLieferantIIdBestelladresse(),
				theClientDto);
		ArtikellieferantDto artikelLieferantDto = getArtikelFac().getArtikelEinkaufspreis(besposDto.getArtikelIId(),
				besDto.getLieferantIIdBestelladresse(), BigDecimal.ONE, besDto.getWaehrungCNr(),
				besDto.getDBelegdatum(), theClientDto);
		ArtikellagerplaetzeDto lagerplaetzeDto = getLagerFac()
				.artikellagerplaetzeFindByArtikelIIdLagerIId(artikelDto.getIId(), weDto.getLagerIId());
		String lagerplatz = lagerplaetzeDto == null ? null : lagerplaetzeDto.getLagerplatzDto().getCLagerplatz();
		String ursprungsland = artikelDto.getLandIIdUrsprungsland() == null ? null
				: getSystemFac().landFindByPrimaryKey(artikelDto.getLandIIdUrsprungsland()).getCName();
		Integer vpEinheit = (artikelLieferantDto == null || artikelLieferantDto.getNVerpackungseinheit() == null) ? null
				: artikelLieferantDto.getNVerpackungseinheit().intValue();

		JasperPrintLP print = printWepEtikett(wareneingangspositionId, besposDto.getIId(), weDto.getLagerIId(), 1,
				vpEinheit, artikelDto.getFGewichtkg(), artikelDto.getCWarenverkehrsnummer(), lagerplatz, ursprungsland,
				null, null, chargennummer, false, theClientDto);

		HvPrinter hvPrinter = getServerDruckerFacLocal().createHvPrinter(printer);
		if (getServerDruckerFacLocal().exists(hvPrinter))
			getServerDruckerFacLocal().print(print, hvPrinter);
	}

	@Override
	public OpenTransXmlReportResult transformPrintBestellung(Integer bestellungId, Integer iAnzahlKopienI,
			Boolean bMitLogo, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		JasperPrintLP[] prints = printBestellung(bestellungId, iAnzahlKopienI, bMitLogo, theClientDto);
		return transformBestellungToOpenTrans(bestellungId, prints, theClientDto);
	}

	private OpenTransXmlReportResult transformBestellungToOpenTrans(Integer bestellungId, JasperPrintLP[] prints,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		if (prints == null || prints.length < 1) {
			return new OpenTransXmlReportResult(prints);
		}

		BestellungDto bestellungDto = getBestellungFac().bestellungFindByPrimaryKey(bestellungId);
		BestellungReportDataOTTransformer transformer = new BestellungReportDataOTTransformer(bestellungDto, prints[0]);
		OpenTransOrder openTransOrder = transformer.transform(theClientDto);

		try {
			OpenTransTransformer xmlTransformer = new OpenTransTransformer();
			String xmlOtOrder = xmlTransformer.orderXmlString(openTransOrder);
			OpenTransXmlReportResult result = new OpenTransXmlReportResult(prints, xmlOtOrder);
			result.setFilename("order" + bestellungDto.getCNr() + ".xml");
			return result;
		} catch (JAXBException e) {
			throw EJBExcFactory.bestellungXmlMarshalling(bestellungDto, openTransOrder, e);
		} catch (SAXException e) {
			throw EJBExcFactory.bestellungXmlMarshalling(bestellungDto, openTransOrder, e);
		}
	}

	public class BestellungReportDataOTTransformer {
		private BestellungDto bestellungDto;
		private Map<String, Object> reportParams;
		private Object[][] reportDaten;

		public BestellungReportDataOTTransformer(BestellungDto bestellungDto, JasperPrintLP jasperPrintLP) {
			this.bestellungDto = bestellungDto;
			this.reportParams = jasperPrintLP.getMapParameters();
			this.reportDaten = data;// jasperPrintLP.getDatenMitUeberschrift();
		}

		public String paramString(String key) {
			return (String) reportParams.get(key);
		}

		public OpenTransOrder transform(TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
			OpenTransOrder otOrder = new OpenTransOrder();
			otOrder.setHead(transformHead(theClientDto));
			otOrder.setPositions(transformPositions(theClientDto));

			return otOrder;
		}

		private OpenTransOrderHead transformHead(TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
			OpenTransOrderHead orderHead = new OpenTransOrderHead();
			orderHead.setOrderNumber(bestellungDto.getCNr());
			orderHead.setOrderDate(bestellungDto.getDBelegdatum());
			orderHead.setCurrency(bestellungDto.getWaehrungCNr());
			orderHead.setTermsAndConditions(paramString("P_ZAHLUNGSKONDITION"));

			PartnerDto partnerLieferadresse = getPartnerFac()
					.partnerFindByPrimaryKey(bestellungDto.getPartnerIIdLieferadresse(), theClientDto);
			orderHead.setBuyerAddress(partnerLieferadresse);
			orderHead.setDeliveryAddress(partnerLieferadresse);

			setAnforderer(orderHead, theClientDto);

			LieferantDto lieferantDtoBestelladresse = getLieferantFac()
					.lieferantFindByPrimaryKey(bestellungDto.getLieferantIIdBestelladresse(), theClientDto);
			orderHead.setSupplier(lieferantDtoBestelladresse.getPartnerDto());
			setValidLiefKundennr(orderHead, lieferantDtoBestelladresse);
			Integer kreditorenNr = lieferantDtoBestelladresse.getIKreditorenkontoAsIntegerNotiId();
			orderHead.setSupplierId(kreditorenNr != null ? String.valueOf(kreditorenNr) : null);

//			LieferantDto lieferantDtoRechnungsadresse = getLieferantFac().lieferantFindByPrimaryKey(bestellungDto.getLieferantIIdRechnungsadresse(), theClientDto);
//			orderHead.setInvoiceAddress(lieferantDtoRechnungsadresse.getPartnerDto());

			return orderHead;
		}

		private void setAnforderer(OpenTransOrderHead orderHead, TheClientDto theClientDto) {
			Integer personalIdAnforderer = bestellungDto.getPersonalIIdAnforderer();
			if (personalIdAnforderer == null) {
				return;
			}

			PersonalDto anforderer = getPersonalFac().personalFindByPrimaryKey(personalIdAnforderer, theClientDto);
			PartnerDto anfordererPartner = anforderer.getPartnerDto();
			anfordererPartner.setCEmail(anforderer.getCEmail());
			orderHead.setBuyerContact(anfordererPartner);
		}

		private void setValidLiefKundennr(OpenTransOrderHead orderHead, LieferantDto lieferantDto) {
			String liefKundennr = lieferantDto.getCKundennr();
			if (Helper.isStringEmpty(liefKundennr)) {
				throw EJBExcFactory.bestellungXmlLieferantKundennummerFehlt(bestellungDto, lieferantDto);
			}
			orderHead.setBuyerSupplierId(liefKundennr);
		}

		private List<OpenTransOrderPosition> transformPositions(TheClientDto theClientDto) {
			List<OpenTransOrderPosition> otPositions = new ArrayList<OpenTransOrderPosition>();
			if (reportDaten == null || reportDaten.length < 2)
				return otPositions;

			for (int row = 0; row < reportDaten.length; row++) {
				addPosition(otPositions, reportDaten[row]);
			}
			return otPositions;
		}

		private void addPosition(List<OpenTransOrderPosition> otPositions, Object[] dataRow) {
			PositionRpt positionRpt = (PositionRpt) dataRow[BestellungReportFac.REPORT_BESTELLUNG_POSITIONOBKJEKT];
			if (!isArtikel(positionRpt))
				return;

			OpenTransOrderPosition pos = new OpenTransOrderPosition();
			pos.setAmount(dataBigDecimal(dataRow[REPORT_BESTELLUNG_PREISPEREINHEIT]));
			pos.setDeliveryDate(Helper.extractDate(positionRpt.getTTermin()));
			pos.setDescriptionShort(positionRpt.getSBezeichnung());
			pos.setItemNumber(positionRpt.getSIdent());
			pos.setLineAmount(dataBigDecimal(dataRow[REPORT_BESTELLUNG_GESAMTPREIS]));
			pos.setLineItemId(dataInteger(dataRow[REPORT_BESTELLUNG_POSITION]));

			setValidQuantityAndOrderUnit(pos, positionRpt, dataRow);
			setValidLiefArtikelnr(pos, dataRow);

			otPositions.add(pos);
		}

		private void setValidQuantityAndOrderUnit(OpenTransOrderPosition pos, PositionRpt positionRpt,
				Object[] dataRow) {
			BigDecimal convertedQuantity = dataBigDecimal(dataRow[REPORT_BESTELLUNG_GEWICHT]);
			pos.setQuantity(convertedQuantity != null ? convertedQuantity : positionRpt.getBdMenge());

			String unit = convertedQuantity != null ? dataString(dataRow[REPORT_BESTELLUNG_GEWICHTEINHEIT])
					: positionRpt.getSEinheit();
			setValidOrderUnit(pos, unit);
		}

		private void setValidOrderUnit(OpenTransOrderPosition pos, String sEinheit) {
			try {
				pos.setOrderUnit(UneceUnitCode.fromStringTrimmed(sEinheit));
			} catch (IllegalArgumentException ex) {
				throw EJBExcFactory.bestellungXmlFehlendesEinheitMapping(bestellungDto, pos, sEinheit);
			}
		}

		private void setValidLiefArtikelnr(OpenTransOrderPosition pos, Object[] dataRow) {
			String liefArtikelnr = dataString(dataRow[REPORT_BESTELLUNG_LIEFERANT_ARTIKEL_IDENTNUMMER]);
			if (Helper.isStringEmpty(liefArtikelnr)) {
				throw EJBExcFactory.bestellungXmlLieferantenartikelnummerFehlt(bestellungDto, pos);
			}
			pos.setSupplierItemNumber(liefArtikelnr);
		}

		private BigDecimal dataBigDecimal(Object value) {
			return value instanceof BigDecimal ? (BigDecimal) value : null;
		}

		private String dataString(Object value) {
			return value instanceof String ? (String) value : null;
		}

		private String dataInteger(Object value) {
			return value instanceof Integer ? String.valueOf(value) : null;
		}

		private boolean isArtikel(PositionRpt positionRpt) {
			if (positionRpt == null)
				return false;

			String positionsart = positionRpt.getSPositionsartCNr();
			return positionsart != null && Helper.isOneOf(positionsart, BestellpositionFac.BESTELLPOSITIONART_IDENT,
					BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE);
		}
	}
}
