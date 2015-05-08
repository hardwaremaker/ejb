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
package com.lp.server.rechnung.ejbfac;

import java.io.File;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.export.JRCsvExporter;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.MaterialDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VerleihDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragReportFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.finanz.bl.FibuExportManager;
import com.lp.server.finanz.bl.FibuExportManagerFactory;
import com.lp.server.finanz.ejb.Exportlauf;
import com.lp.server.finanz.ejb.MahnstufePK;
import com.lp.server.finanz.ejbfac.BuchungDetailQueryBuilder;
import com.lp.server.finanz.ejbfac.FinanzSubreportGenerator;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzBuchungDetail;
import com.lp.server.finanz.service.ExportdatenDto;
import com.lp.server.finanz.service.ExportlaufDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzReportFac;
import com.lp.server.finanz.service.FinanzamtDto;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.KontoDtoSmall;
import com.lp.server.finanz.service.MahnstufeDto;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferscheinposition;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinReportFac;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.lieferschein.service.LieferscheinpositionFac;
import com.lp.server.partner.fastlanereader.generated.FLRKunde;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.BankFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.KundeReportFac;
import com.lp.server.partner.service.KundesokoDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.rechnung.ejb.Rechnung;
import com.lp.server.rechnung.ejb.Rechnungposition;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnungPosition;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnungReport;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnungZahlung;
import com.lp.server.rechnung.service.GutschrifttextDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungDtoAssembler;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.rechnung.service.RechnungReportFac;
import com.lp.server.rechnung.service.RechnungartsprDto;
import com.lp.server.rechnung.service.RechnungkontierungDto;
import com.lp.server.rechnung.service.RechnungtextDto;
import com.lp.server.rechnung.service.RechnungzahlungDto;
import com.lp.server.rechnung.service.ReportRechnungJournalKriterienDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteMitStrukturDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.pkgenerator.format.LpBelegnummerFormat;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.MediastandardDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.system.service.SpediteurDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.ZahlungszielDto;
import com.lp.server.util.BelegPositionDruckIdentDto;
import com.lp.server.util.BelegPositionDruckTextbausteinDto;
import com.lp.server.util.HelperServer;
import com.lp.server.util.LPReport;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.server.util.report.MwstsatzReportDto;
import com.lp.service.BelegpositionVerkaufDto;
import com.lp.service.StuecklisteInfoDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.LPDatenSubreport;

@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
public class RechnungReportFacBean extends LPReport implements
		RechnungReportFac, JRDataSource {

	@PersistenceContext
	private EntityManager em;

	private final static int UC_RE_GS_PR = 0;
	public final static int UC_REPORT_RECHNUNGEN_OFFENE = 1;
	public final static int UC_REPORT_WARENAUSGANGSJOURNAL = 2;
	public final static int UC_REPORT_ZAHLUNGSJOURNAL = 3;
	public final static int UC_REPORT_UMSATZ = 4;
	public final static int UC_REPORT_ALLE = 5;
	public final static int UC_ZAHLSCHEIN = 6;
	public final static int UC_ZUSAMMENFASSENDE_MELDUNG = 7;

	private final static int RECHNUNG_FELD_POSITION = 0;
	private final static int RECHNUNG_FELD_IDENT = 1;
	private final static int RECHNUNG_FELD_MENGE = 2;
	private final static int RECHNUNG_FELD_EINHEIT = 3;
	private final static int RECHNUNG_FELD_EINZELPREIS = 4;
	private final static int RECHNUNG_FELD_RABATT = 5;
	private final static int RECHNUNG_FELD_GESAMTPREIS = 6;
	private final static int RECHNUNG_FELD_POSITIONSART = 7;
	private final static int RECHNUNG_FELD_FREIERTEXT = 8;
	private final static int RECHNUNG_FELD_LEERZEILE = 9;
	private final static int RECHNUNG_FELD_B_SEITENUMBRUCH = 10;
	private final static int RECHNUNG_FELD_IMAGE = 11;
	private final static int RECHNUNG_FELD_MWSTSATZ = 12;
	private final static int RECHNUNG_FELD_MWSTBETRAG = 13;
	private final static int RECHNUNG_FELD_STKLMENGE = 14;
	private final static int RECHNUNG_FELD_STKLEINHEIT = 15;
	private final static int RECHNUNG_FELD_STKLARTIKELCNR = 16;
	private final static int RECHNUNG_FELD_STKLARTIKELBEZ = 17;
	private final static int RECHNUNG_FELD_ZUSATZRABATT = 18;
	private final static int RECHNUNG_FELD_IDENTNUMMER = 19;
	private final static int RECHNUNG_FELD_BEZEICHNUNG = 20;
	private final static int RECHNUNG_FELD_KURZBEZEICHNUNG = 21;
	private final static int RECHNUNG_FELD_AUFTRAG_NUMMER = 22;
	private final static int RECHNUNG_FELD_AUFTRAG_PROJEKT = 23;
	private final static int RECHNUNG_FELD_AUFTRAG_BESTELLNUMMER = 24;
	private final static int RECHNUNG_FELD_AUFTRAG_BESTELLDATUM = 25;
	private final static int RECHNUNG_FELD_WARENVERKEHRSNUMMER = 26;
	private final static int RECHNUNG_FELD_SERIENCHARGENR = 27;
	private final static int RECHNUNG_FELD_SERIENCHARGENR_MENGE = 28;
	private final static int RECHNUNG_FELD_MINDESTHALTBARKEIT = 29;
	private final static int RECHNUNG_FELD_KUNDEARTIKELNR = 30;
	private final static int RECHNUNG_FELD_ARTIKEL_VERPACKUNGSMENGE = 31;
	private final static int RECHNUNG_FELD_ARTIKEL_VERPACKUNGSEANNR = 32;
	private final static int RECHNUNG_FELD_ARTIKEL_VERKAUFSEANNR = 33;
	private final static int RECHNUNG_FELD_REFERENZNUMMER = 34;
	private final static int RECHNUNG_FELD_ARTIKELCZBEZ2 = 35;
	private final static int RECHNUNG_FELD_ARTIKELKOMMENTAR = 36;
	private final static int RECHNUNG_FELD_ZUSATZBEZEICHNUNG = 37;
	public final static int RECHNUNG_FELD_BAUFORM = 38;
	public final static int RECHNUNG_FELD_VERPACKUNGSART = 39;
	public final static int RECHNUNG_FELD_ARTIKEL_MATERIAL = 40;
	public final static int RECHNUNG_FELD_ARTIKEL_BREITE = 41;
	public final static int RECHNUNG_FELD_ARTIKEL_HOEHE = 42;
	public final static int RECHNUNG_FELD_ARTIKEL_TIEFE = 43;
	public final static int RECHNUNG_FELD_ARTIKEL_URSPRUNGSLAND = 44;
	public final static int RECHNUNG_FELD_ARTIKEL_ARTIKELGRUPPE = 45;
	private final static int RECHNUNG_FELD_POSITION_NR = 46;
	private final static int RECHNUNG_FELD_TYP_CNR = 47;
	private final static int RECHNUNG_FELD_IDENT_TEXTEINGABE = 48;
	private final static int RECHNUNG_FELD_LS_FILIALNR = 49;
	private final static int RECHNUNG_FELD_POSITIONSOBJEKT = 50;
	private final static int RECHNUNG_FELD_LS_ANSPRECHPARTNER = 51;
	private final static int RECHNUNG_FELD_LS_ANSPRECHPARTNER_FREMDSYSTEMNUMMER = 52;
	private final static int RECHNUNG_FELD_LS_KOMMISIONSNUMMER = 53;
	private final static int RECHNUNG_FELD_LS_ABBUCHUNGSLAGER = 54;
	private final static int RECHNUNG_FELD_WE_REFERENZ = 55;
	private final static int RECHNUNG_FELD_LS_PROJEKT = 56;
	private final static int RECHNUNG_FELD_LS_BESTELLNUMMER = 57;
	private final static int RECHNUNG_FELD_LS_KOSTENSTELLE = 58;
	private final static int RECHNUNG_FELD_LS_RECHNUNGSADRESSEGLEICHLIEFERADRESSE = 59;
	private final static int RECHNUNG_FELD_SETARTIKEL_TYP = 60;
	private final static int RECHNUNG_FELD_FIBU_MWST_CODE = 61;
	private final static int RECHNUNG_FELD_LS_VERRECHNET_MIT = 62;
	public final static int RECHNUNG_FELD_ARTIKEL_REVISION = 63;
	public final static int RECHNUNG_FELD_ARTIKEL_INDEX = 64;
	public final static int RECHNUNG_FELD_VERLEIHTAGE = 65;
	public final static int RECHNUNG_FELD_VERLEIHFAKTOR = 66;
	public final static int RECHNUNG_FELD_GEWICHT = 67;
	public final static int RECHNUNG_FELD_VONPOSITION = 68;
	public final static int RECHNUNG_FELD_BISPOSITION = 69;
	public final static int RECHNUNG_FELD_ZWSNETTOSUMME = 70;
	public final static int RECHNUNG_FELD_ZWSTEXT = 71;
	public final static int RECHNUNG_FELD_INTERNAL_IID = 72;
	public final static int RECHNUNG_FELD_LVPOSITION = 73;
	private final static int RECHNUNG_FELD_STKLARTIKELKBEZ = 74;
	private final static int RECHNUNG_FELD_STKLARTIKEL_KDARTIKELNR = 75;
	private final static int RECHNUNG_FELD_ARTIKEL_WERBEABGABEPFLICHTIG = 76;
	private final static int RECHNUNG_FELD_INSERAT_DTO = 77;
	private final static int RECHNUNG_FELD_STKLARTIKEL_KDPREIS = 78;
	public final static int RECHNUNG_FELD_MATERIALZUSCHLAG = 79;
	public final static int RECHNUNG_FELD_ARTIKEL_MATERIALGEWICHT = 80;
	public final static int RECHNUNG_FELD_ARTIKEL_KURS_MATERIALZUSCHLAG = 81;
	public final static int RECHNUNG_FELD_ARTIKEL_DATUM_MATERIALZUSCHLAG = 82;
	private final static int RECHNUNG_FELD_VERSION = 83;
	public final static int RECHNUNG_FELD_ZWSPOSPREISDRUCKEN = 84;
	public final static int RECHNUNG_FELD_LAGER_UEBERSTEUERT_AUS_LIEFERSCHEIN = 85;
	public final static int RECHNUNG_FELD_ERLOESKONTO_DTO = 86;
	private final static int RECHNUNG_ANZAHL_SPALTEN = 87;

	private final static int OFFENE_FELD_RECHNUNGART = 0;
	private final static int OFFENE_FELD_KOSTENSTELLE = 1;
	private final static int OFFENE_FELD_RECHNUNGSNUMMER = 2;
	private final static int OFFENE_FELD_FIRMA = 3;
	private final static int OFFENE_FELD_RECHNUNGSDATUM = 4;
	private final static int OFFENE_FELD_MAHNDATUM = 5;
	private final static int OFFENE_FELD_MAHNSTUFE = 6;
	private final static int OFFENE_FELD_WERT_BRUTTO = 7;
	private final static int OFFENE_FELD_WERT_NETTO = 8;
	private final static int OFFENE_FELD_WERT_BRUTTO_OFFEN = 9;
	private final static int OFFENE_FELD_WERT_NETTO_OFFEN = 10;
	private final static int OFFENE_FELD_WECHSELDATUM = 11;
	private final static int OFFENE_FELD_FAELLIGKEITSDATUM = 12;
	private final static int OFFENE_FELD_TAGE_BIS_FAELLIGKEITSDATUM = 13;
	private final static int OFFENE_FELD_FIRMA_LKZ = 14;
	private final static int OFFENE_FELD_FIRMA_LKZ_STATISTIK = 15;
	private final static int OFFENE_FELD_FIRMA_STATISTIK = 16;
	private final static int OFFENE_FELD_VERTRETER = 17;
	private final static int OFFENE_FELD_MAHNSPERRE = 18;
	private final static int OFFENE_FELD_WERT_BRUTTO_ANZAHLUNGEN = 19;
	private final static int OFFENE_FELD_WERT_NETTO_ANZAHLUNGEN = 20;
	private final static int OFFENE_FELD_WERT_BRUTTO_FW = 21;
	private final static int OFFENE_FELD_WERT_NETTO_FW = 22;
	private final static int OFFENE_FELD_WERT_BRUTTO_OFFEN_FW = 23;
	private final static int OFFENE_FELD_WERT_NETTO_OFFEN_FW = 24;
	private final static int OFFENE_FELD_WERT_BRUTTO_ANZAHLUNGEN_FW = 25;
	private final static int OFFENE_FELD_WERT_NETTO_ANZAHLUNGEN_FW = 26;
	private final static int OFFENE_FELD_KURS = 27;
	private final static int OFFENE_FELD_WAEHRUNG = 28;
	private final static int OFFENE_FELD_DEBITORENNR = 29;
	private final static int OFFENE_SUBREPORT_OFFENE_BUCHUNGEN = 30;
	private final static int OFFENE_FELD_KREDITLIMIT = 31;
	private final static int OFFENE_ANZAHL_SPALTEN = 32;

	private final static int ZAHLUNGEN_FELD_ER_C_NR = 0;
	private final static int ZAHLUNGEN_FELD_FIRMA = 1;
	private final static int ZAHLUNGEN_FELD_ZAHLDATUM = 2;
	private final static int ZAHLUNGEN_FELD_BETRAG = 3;
	private final static int ZAHLUNGEN_FELD_BETRAG_UST = 4;
	private final static int ZAHLUNGEN_FELD_BANK = 5;
	private final static int ZAHLUNGEN_FELD_AUSZUG = 6;
	private final static int ZAHLUNGEN_FELD_WECHSEL = 7;
	private static int ZAHLUNGEN_FELD_BETRAG_MANDANTENWAEHRUNG_ZU_BELEGZEIT = 8;
	private static int ZAHLUNGEN_FELD_BETRAG_UST_MANDANTENWAEHRUNG_ZU_BELEGZEIT = 9;
	private static int ZAHLUNGEN_FELD_BELEGWAEHRUNG = 10;
	private static int ZAHLUNGEN_FELD_BETRAG_FW = 11;
	private static int ZAHLUNGEN_FELD_BETRAG_UST_FW = 12;
	private static int ZAHLUNGEN_FELD_KONTO = 13;
	private static int ZAHLUNGEN_FELD_ZAHLUNGSART = 14;
	private static int ZAHLUNGEN_FELD_KASSENBUCH = 15;
	private static int ZAHLUNGEN_FELD_DEBITORENKONTO = 16;
	private static int ZAHLUNGEN_FELD_RECHNUNGSART = 17;
	private final static int ZAHLUNGEN_FELD_LAENDERART = 18;
	private final static int ZAHLUNGEN_FELD_KOSTENSTELLE = 19;
	private final static int ZAHLUNGEN_ANZAHL_FELDER = 20;

	private final static int UMSATZ_FELD_MONAT = 0;
	private final static int UMSATZ_FELD_WERT = 1;
	private final static int UMSATZ_FELD_WERT_UST = 2;
	private final static int UMSATZ_FELD_WERT_NICHT_ABGERECHNETE_ANZAHLUNGEN = 3;
	private final static int UMSATZ_FELD_WERT_NICHT_ABGERECHNETE_ANZAHLUNGEN_UST = 4;

	private final static int FELD_ALLE_RECHNUNGSNUMMER = 0;
	private final static int FELD_ALLE_BELEGDATUM = 1;
	private final static int FELD_ALLE_KUNDE = 2;
	private final static int FELD_ALLE_KUNDE2 = 3;
	private final static int FELD_ALLE_KOSTENSTELLENUMMER = 4;
	private final static int FELD_ALLE_BETRAG = 5;
	private final static int FELD_ALLE_BETRAGUST = 6;
	private final static int FELD_ALLE_ORT = 7;
	private final static int FELD_ALLE_ZIELDATUM = 8;
	private final static int FELD_ALLE_BEZAHLTDATUM = 9;
	private final static int FELD_ALLE_BEZAHLT = 10;
	private final static int FELD_ALLE_BEZAHLTUST = 11;
	private final static int FELD_ALLE_MAHNSTUFE = 12;
	private final static int FELD_ALLE_WECHSELDATUM = 13;
	private final static int FELD_ALLE_DEBITORENKONTO = 14;
	private final static int FELD_ALLE_ADRESSE = 15;
	private final static int FELD_ALLE_ZINSEN = 16;
	private final static int FELD_ALLE_ZAHLTAGE = 17;
	private final static int FELD_ALLE_VERTRETER = 18;
	private final static int FELD_ALLE_KURS = 19;
	private final static int FELD_ALLE_WAEHRUNG = 20;
	private final static int FELD_ALLE_RECHNUNGSNUMMERZUGUTSCHRIFT = 21;
	private final static int FELD_ALLE_KUNDE_STATISTIK = 22;
	private final static int FELD_ALLE_KUNDE2_STATISTIK = 23;
	private final static int FELD_ALLE_ADRESSE_STATISTIK = 24;
	private final static int FELD_ALLE_LAENDERART = 25;
	private final static int FELD_ALLE_KUNDE_UID = 26;
	private final static int FELD_ALLE_REVERSE_CHARGE = 27;
	private final static int FELD_ALLE_WERT_ANZAHLUNGEN = 28;
	private final static int FELD_ALLE_WERT_ANZAHLUNGENUST = 29;
	private final static int FELD_ALLE_STATUS = 30;
	private final static int FELD_ALLE_BETRAG_FW = 31;
	private final static int FELD_ALLE_BETRAGUST_FW = 32;
	private final static int FELD_ALLE_BEZAHLT_FW = 33;
	private final static int FELD_ALLE_BEZAHLTUST_FW = 34;
	private final static int FELD_ALLE_WERT_ANZAHLUNGEN_FW = 35;
	private final static int FELD_ALLE_WERT_ANZAHLUNGENUST_FW = 36;
	private final static int FELD_ALLE_ZOLLBELEGNUMMER = 37;
	private final static int FELD_ALLE_KREDITLIMIT = 38;
	private final static int REPORT_ALLE_ANZAHL_SPALTEN = 39;

	private final static int FELD_ZM_RECHNUNGSNUMMER = 0;
	private final static int FELD_ZM_BELEGDATUM = 1;
	private final static int FELD_ZM_KUNDE = 2;
	private final static int FELD_ZM_KUNDE2 = 3;
	private final static int FELD_ZM_KOSTENSTELLENUMMER = 4;
	private final static int FELD_ZM_BETRAG = 5;
	private final static int FELD_ZM_BETRAGUST = 6;
	private final static int FELD_ZM_ORT = 7;
	private final static int FELD_ZM_ZIELDATUM = 8;
	private final static int FELD_ZM_BEZAHLTDATUM = 9;
	private final static int FELD_ZM_BEZAHLT = 10;
	private final static int FELD_ZM_BEZAHLTUST = 11;
	private final static int FELD_ZM_MAHNSTUFE = 12;
	private final static int FELD_ZM_WECHSELDATUM = 13;
	private final static int FELD_ZM_DEBITORENKONTO = 14;
	private final static int FELD_ZM_ADRESSE = 15;
	private final static int FELD_ZM_ZINSEN = 16;
	private final static int FELD_ZM_ZAHLTAGE = 17;
	private final static int FELD_ZM_VERTRETER = 18;
	private final static int FELD_ZM_KURS = 19;
	private final static int FELD_ZM_WAEHRUNG = 20;
	private final static int FELD_ZM_RECHNUNGSNUMMERZUGUTSCHRIFT = 21;
	private final static int FELD_ZM_KUNDE_STATISTIK = 22;
	private final static int FELD_ZM_KUNDE2_STATISTIK = 23;
	private final static int FELD_ZM_ADRESSE_STATISTIK = 24;
	private final static int FELD_ZM_LAENDERART = 25;
	private final static int FELD_ZM_KUNDE_UID = 26;
	private final static int FELD_ZM_REVERSE_CHARGE = 27;
	private final static int REPORT_ZM_ANZAHL_SPALTEN = 28;

	private final static int FELD_WARENAUSGANG_RECHNUNGSNUMMER = 0;
	private final static int FELD_WARENAUSGANG_RECHNUNGSDATUM = 1;
	private final static int FELD_WARENAUSGANG_POSITIONSPREIS = 2;
	private final static int FELD_WARENAUSGANG_KUNDENBEZEICHNUNG = 3;
	private final static int FELD_WARENAUSGANG_KUNDENNUMMER = 4;
	private final static int FELD_WARENAUSGANG_ARTIKELNUMMER = 5;
	private final static int FELD_WARENAUSGANG_ARTIKELBEZEICHNUNG = 6;
	private final static int FELD_WARENAUSGANG_ARTIKELKLASSE = 7;
	private final static int FELD_WARENAUSGANG_ARTIKELGRUPPE = 8;
	private final static int FELD_WARENAUSGANG_URSPRUNGSLAND = 9;
	private final static int FELD_WARENAUSGANG_WARENVERKEHRSNUMMER = 10;
	private final static int FELD_WARENAUSGANG_MENGE = 11;
	private final static int FELD_WARENAUSGANG_EINHEIT = 12;
	private final static int FELD_WARENAUSGANG_ARTIKELBEZEICHNUNG2 = 13;
	private final static int FELD_WARENAUSGANG_RECHNUNGSPROVISION = 14;
	private final static int FELD_WARENAUSGANG_PROVISIONSEMPFAENGER = 15;
	private final static int FELD_WARENAUSGANG_ERLOESKONTO = 16;
	private final static int FELD_WARENAUSGANG_DEBITORENKONTO = 17;
	private final static int FELD_WARENAUSGANG_PLZ = 18;
	private final static int FELD_WARENAUSGANG_LKZ = 19;
	private final static int FELD_WARENAUSGANG_ADRESSE = 20;
	private final static int FELD_WARENAUSGANG_MWST = 21;
	private final static int FELD_WARENAUSGANG_BRANCHE = 22;
	private final static int FELD_WARENAUSGANG_PARTNERKLASSE = 23;
	private final static int FELD_WARENAUSGANG_USTLAND = 24;
	private final static int FELD_WARENAUSGANG_LIEFERSCHEINNUMMER = 25;
	private final static int FELD_WARENAUSGANG_LIEFERSCHEINDATUM = 26;
	private final static int FELD_WARENAUSGANG_STATISTIKADRESSE_PARTNER_I_ID = 27;
	private final static int FELD_WARENAUSGANG_STATISTIKADRESSE_ANREDE_C_NR = 28;
	private final static int FELD_WARENAUSGANG_STATISTIKADRESSE_C_NAME1 = 29;
	private final static int FELD_WARENAUSGANG_STATISTIKADRESSE_C_NAME2 = 30;
	private final static int FELD_WARENAUSGANG_STATISTIKADRESSE_C_NAME3ABTEILUNG = 31;
	private final static int FELD_WARENAUSGANG_STATISTIKADRESSE_C_STRASSE = 32;
	private final static int FELD_WARENAUSGANG_STATISTIKADRESSE_LAND = 33;
	private final static int FELD_WARENAUSGANG_STATISTIKADRESSE_ORT = 34;
	private final static int FELD_WARENAUSGANG_STATISTIKADRESSE_PLZ = 35;
	private final static int FELD_WARENAUSGANG_PARTNER_I_ID = 36;
	private final static int FELD_WARENAUSGANG_WAEHRUNG = 37;
	private final static int FELD_WARENAUSGANG_KURS = 38;
	private final static int FELD_WARENAUSGANG_RECHNUNGSART = 39;
	private final static int FELD_WARENAUSGANG_ZURECHNUNG = 40;
	private final static int FELD_WARENAUSGANG_ARTIKELPROVISION = 41;
	private final static int FELD_WARENAUSGANG_BESTELLNUMMER = 42;
	private final static int FELD_WARENAUSGANG_KUNDENNUMMER_RECHNUNGSADRESSE_LS = 43;
	private final static int FELD_WARENAUSGANG_KUNDENBEZEICHNUNG_RECHNUNGSADRESSE_LS = 44;
	private final static int FELD_WARENAUSGANG_KUNDEKURZBEZEICHNUNG_RECHNUNGSADRESSE_LS = 45;
	private final static int FELD_WARENAUSGANG_KUNDE_HINWEISEXT = 46;
	private final static int FELD_WARENAUSGANG_KUNDE_HINWEISINT = 47;
	private final static int FELD_WARENAUSGANG_KUNDE_PROVISIONSEMPFAENGER_KURZZEICHEN = 48;
	private final static int FELD_WARENAUSGANG_ARTIKEL_GEWICHTKG = 49;
	private final static int FELD_WARENAUSGANG_LAGER_RECHNUNG = 50;
	private final static int FELD_WARENAUSGANG_LAGER_LIEFERSCHEIN = 51;
	private final static int FELD_WARENAUSGANG_ZIELLAGER_LIEFERSCHEIN = 52;
	private final static int FELD_WARENAUSGANG_KUNDENNUMMER_LIEFERADRESSE_LS = 53;
	private final static int FELD_WARENAUSGANG_TEXTEINGABE = 54; // der
	private final static int FELD_WARENAUSGANG_ILN_LIEFERADRESSE = 55; // der
	private final static int FELD_WARENAUSGANG_ILN_RECHNUNGSADRESSE = 56;
	private final static int FELD_WARENAUSGANG_LIEFERADRESSE_PARTNER_I_ID = 57;
	private final static int FELD_WARENAUSGANG_LIEFERADRESSE_ANREDE_C_NR = 58;
	private final static int FELD_WARENAUSGANG_LIEFERADRESSE_C_NAME1 = 59;
	private final static int FELD_WARENAUSGANG_LIEFERADRESSE_C_NAME2 = 60;
	private final static int FELD_WARENAUSGANG_LIEFERADRESSE_C_NAME3ABTEILUNG = 61;
	private final static int FELD_WARENAUSGANG_LIEFERADRESSE_C_STRASSE = 62;
	private final static int FELD_WARENAUSGANG_LIEFERADRESSE_LAND = 63;
	private final static int FELD_WARENAUSGANG_LIEFERADRESSE_ORT = 64;
	private final static int FELD_WARENAUSGANG_LIEFERADRESSE_PLZ = 65;
	private final static int FELD_WARENAUSGANG_GRUNDNICHTEINGEHALTEN = 66;
	private final static int FELD_WARENAUSGANG_GEPLANTER_LIEFERTERMIN = 67;
	private final static int FELD_WARENAUSGANG_KUNDE_LIEFERDAUER = 68;
	private final static int FELD_WARENAUSGANG_AUFTRAG_NUMMER = 69;
	private final static int FELD_WARENAUSGANG_AUFTRAG_BEGRUENDUNG = 70;
	private final static int FELD_WARENAUSGANG_GESTPREIS = 71;
	private final static int FELD_WARENAUSGANG_KOSTENTRAEGER = 72;
	private final static int FELD_WARENAUSGANG_SETARTIKEL_TYP = 73;
	private final static int FELD_WARENAUSGANG_PROJEKT = 74;
	private final static int REPORT_WARENAUSGANG_ANZAHL_SPALTEN = 75;

	private final static String PARAMETER_WARENAUSGANG_VON = "P_VON";
	private final static String PARAMETER_WARENAUSGANG_BIS = "P_BIS";
	private final static String PARAMETER_WARENAUSGNAG_GRUPPIERUNGNACHSTATISTIK = "P_GRUPPIERTNACHSTATISTIK";
	private final static String PARAMETER_WARENAUSGANG_MANDANTENWAEHRUNG = "P_WAEHRUNG_MANDANT";

	private int useCase;
	private Object[][] data = null;
	private Integer iArtikelpositionsnummer;

	private FibuExportManager fibuExportManager;

	public boolean next() throws JRException {
		index++;
		return (index < data.length);
	}

	public Object getFieldValue(JRField field) throws JRException {
		Object value = null;
		String fieldName = field.getName();
		switch (useCase) {
		case UC_RE_GS_PR: {
			if ("Position".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_POSITION];
			}
			if ("F_POSITION_NR".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_POSITION_NR];
			} else if ("Ident".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][RECHNUNG_FELD_IDENT]);
			} else if ("Menge".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_MENGE];
			} else if ("Einheit".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_EINHEIT];
			} else if ("Einzelpreis".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_EINZELPREIS];
			} else if ("Rabatt".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_RABATT];
			} else if ("Gesamtpreis".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_GESAMTPREIS];
			} else if ("Materialzuschlag".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_MATERIALZUSCHLAG];
			} else if ("Positionsart".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_POSITIONSART];
			} else if ("Freiertext".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][RECHNUNG_FELD_FREIERTEXT]);
			} else if ("Leerzeile".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][RECHNUNG_FELD_LEERZEILE]);
			} else if ("Seitenumbruch".equals(fieldName)) {
				// seitenumbruch: uebergeben
				value = data[index][RECHNUNG_FELD_B_SEITENUMBRUCH];
			} else if ("Image".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_IMAGE];
			} else if ("Mwstsatz".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_MWSTSATZ];
			} else if ("Mwstbetrag".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_MWSTBETRAG];
			} else if ("F_STKLMENGE".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_STKLMENGE];
			} else if ("F_STKLEINHEIT".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_STKLEINHEIT];
			} else if ("F_STKLARTIKELCNR".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_STKLARTIKELCNR];
			} else if ("F_STKLARTIKELBEZ".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_STKLARTIKELBEZ];
			} else if ("F_STKLARTIKELKBEZ".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_STKLARTIKELKBEZ];
			} else if ("F_STKLARTIKEL_KDARTIKELNR".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_STKLARTIKEL_KDARTIKELNR];
			} else if ("F_STKLARTIKEL_KDPREIS".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_STKLARTIKEL_KDPREIS];
			} else if ("F_ZUSATZRABATT".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_ZUSATZRABATT];
			} else if (F_IDENTNUMMER.equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_IDENTNUMMER];
			} else if (F_BEZEICHNUNG.equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_BEZEICHNUNG];
			} else if ("F_LV_POSITION".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_LVPOSITION];
			} else if (F_KURZBEZEICHNUNG.equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_KURZBEZEICHNUNG];
			} else if ("AUFTRAG_BESTELLDATUM".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_AUFTRAG_BESTELLDATUM];
			} else if ("AUFTRAG_BESTELLNUMMER".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][RECHNUNG_FELD_AUFTRAG_BESTELLNUMMER]);
			} else if ("AUFTRAG_NUMMER".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_AUFTRAG_NUMMER];
			} else if ("AUFTRAG_PROJEKT".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_AUFTRAG_PROJEKT];
			} else if ("F_WARENVERKEHRSNUMMER".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_WARENVERKEHRSNUMMER];
			} else if ("F_SERIENCHARGENR".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_SERIENCHARGENR];
			} else if ("F_SERIENCHARGENR_MENGE".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_SERIENCHARGENR_MENGE];
			} else if ("F_CHARGE_MINDESTHALTBARKEIT".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][RECHNUNG_FELD_MINDESTHALTBARKEIT]);
			} else if (F_KUNDEARTIKELNR.equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][RECHNUNG_FELD_KUNDEARTIKELNR]);
			} else if ("F_VERPACKUNGSMENGE".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_ARTIKEL_VERPACKUNGSMENGE];
			} else if ("F_ARTIKEL_WERBEABGABEPFLICHTIG".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_ARTIKEL_WERBEABGABEPFLICHTIG];
			} else if ("F_INSERAT_DTO".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_INSERAT_DTO];
			} else if ("F_VERPACKUNGSEANNR".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_ARTIKEL_VERPACKUNGSEANNR];
			} else if ("F_VERKAUFSEANNR".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_ARTIKEL_VERKAUFSEANNR];
			} else if (F_REFERENZNUMMER.equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_REFERENZNUMMER];
			} else if ("F_GEWICHT".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_GEWICHT];
			} else if (F_ZUSATZBEZEICHNUNG2.equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_ARTIKELCZBEZ2];
			} else if (F_ARTIKELKOMMENTAR.equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_ARTIKELKOMMENTAR];
			} else if (F_ZUSATZBEZEICHNUNG.equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_ZUSATZBEZEICHNUNG];
			} else if (F_ARTIKEL_BAUFORM.equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_BAUFORM];
			} else if (F_ARTIKEL_VERPACKUNGSART.equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_VERPACKUNGSART];
			} else if (F_ARTIKEL_MATERIAL.equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_ARTIKEL_MATERIAL];
			} else if (F_ARTIKEL_MATERIALGEWICHT.equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_ARTIKEL_MATERIALGEWICHT];
			} else if (F_ARTIKEL_KURS_MATERIALZUSCHLAG.equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_ARTIKEL_KURS_MATERIALZUSCHLAG];
			} else if (F_ARTIKEL_DATUM_MATERIALZUSCHLAG.equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_ARTIKEL_DATUM_MATERIALZUSCHLAG];
			} else if ("F_ARTIKEL_INDEX".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_ARTIKEL_INDEX];
			} else if ("F_ARTIKEL_REVISION".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_ARTIKEL_REVISION];
			} else if (F_ARTIKEL_BREITE.equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_ARTIKEL_BREITE];
			} else if (F_ARTIKEL_HOEHE.equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_ARTIKEL_HOEHE];
			} else if (F_ARTIKEL_TIEFE.equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_ARTIKEL_TIEFE];
			} else if (F_ARTIKEL_URSPRUNGSLAND.equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_ARTIKEL_URSPRUNGSLAND];
			} else if ("F_ARTIKEL_ARTIKELGRUPPE".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_ARTIKEL_ARTIKELGRUPPE];
			} else if ("F_TYP_CNR".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_TYP_CNR];
			} else if ("F_IDENT_TEXTEINGABE".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_IDENT_TEXTEINGABE];
			} else if ("F_LS_FILIALNR".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_LS_FILIALNR];
			} else if ("F_LS_ANSPRECHPARTNER".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_LS_ANSPRECHPARTNER];
			} else if ("F_LS_ANSPRECHPARTNER_FREMDSYSTEMNUMMER"
					.equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_LS_ANSPRECHPARTNER_FREMDSYSTEMNUMMER];
			} else if ("F_LS_KOMMISIONSNUMMER".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_LS_KOMMISIONSNUMMER];
			} else if ("F_LS_ABBUCHUNGSLAGER".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_LS_ABBUCHUNGSLAGER];
			} else if ("F_POSITIONSOBJEKT".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_POSITIONSOBJEKT];
			} else if ("WE_REFERENZ".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_WE_REFERENZ];
			} else if ("F_LS_PROJEKT".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_LS_PROJEKT];
			} else if ("F_LS_BESTELLNUMMER".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_LS_BESTELLNUMMER];
			} else if ("F_LS_KOSTENSTELLE".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_LS_KOSTENSTELLE];
			} else if ("F_LS_VERRECHNETMIT".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_LS_VERRECHNET_MIT];
			} else if ("F_LS_RECHNUNGSADRESSEGLEICHLIEFERADRESSE"
					.equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_LS_RECHNUNGSADRESSEGLEICHLIEFERADRESSE];
			} else if ("F_SETARTIKEL_TYP".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_SETARTIKEL_TYP];
			} else if ("F_FIBU_MWST_CODE".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_FIBU_MWST_CODE];
			} else if ("F_VERLEIHTAGE".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_VERLEIHTAGE];
			} else if ("F_VERLEIHFAKTOR".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_VERLEIHFAKTOR];
			} else if ("F_VONPOSITION".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_VONPOSITION];
			} else if ("F_BISPOSITION".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_BISPOSITION];
			} else if ("F_ZWSNETTOSUMME".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_ZWSNETTOSUMME];
			} else if ("F_ZWSTEXT".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_ZWSTEXT];
			} else if ("F_VERSION".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_VERSION];
			} else if ("F_ZWSPOSPREISDRUCKEN".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_ZWSPOSPREISDRUCKEN];
			} else if ("F_LAGER_UEBERSTEUERT_AUS_LIEFERSCHEIN"
					.equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_LAGER_UEBERSTEUERT_AUS_LIEFERSCHEIN];
			} else if ("F_ERLOESKONTO_DTO".equals(fieldName)) {
				value = data[index][RECHNUNG_FELD_ERLOESKONTO_DTO];
			}
		}
			break;

		case UC_REPORT_RECHNUNGEN_OFFENE: {
			if ("F_KUNDE".equals(fieldName)) {
				value = data[index][OFFENE_FELD_FIRMA];
			} else if ("F_KUNDE_STATISTIK".equals(fieldName)) {
				value = data[index][OFFENE_FELD_FIRMA_STATISTIK];
			} else if ("F_KOSTENSTELLE".equals(fieldName)) {
				value = data[index][OFFENE_FELD_KOSTENSTELLE];
			} else if ("F_MAHNDATUM".equals(fieldName)) {
				value = data[index][OFFENE_FELD_MAHNDATUM];
			} else if ("F_MAHNSTUFE".equals(fieldName)) {
				value = data[index][OFFENE_FELD_MAHNSTUFE];
			} else if ("F_RECHNUNGSART".equals(fieldName)) {
				value = data[index][OFFENE_FELD_RECHNUNGART];
			} else if ("F_RECHNUNGSDATUM".equals(fieldName)) {
				value = data[index][OFFENE_FELD_RECHNUNGSDATUM];
			} else if ("F_RECHNUNGSNUMMER".equals(fieldName)) {
				value = data[index][OFFENE_FELD_RECHNUNGSNUMMER];
			} else if ("F_WECHSEL".equals(fieldName)) {
				value = data[index][OFFENE_FELD_WECHSELDATUM];
			} else if ("F_WERT".equals(fieldName)) {
				value = data[index][OFFENE_FELD_WERT_BRUTTO];
			} else if ("F_OFFEN".equals(fieldName)) {
				value = data[index][OFFENE_FELD_WERT_BRUTTO_OFFEN];
			} else if ("F_WERT_NETTO".equals(fieldName)) {
				value = data[index][OFFENE_FELD_WERT_NETTO];
			} else if ("F_NETTO_ANZAHLUNGEN".equals(fieldName)) {
				value = data[index][OFFENE_FELD_WERT_NETTO_ANZAHLUNGEN];
			} else if ("F_BRUTTO_ANZAHLUNGEN".equals(fieldName)) {
				value = data[index][OFFENE_FELD_WERT_BRUTTO_ANZAHLUNGEN];
			} else if ("F_OFFEN_NETTO".equals(fieldName)) {
				value = data[index][OFFENE_FELD_WERT_NETTO_OFFEN];
			}

			else if ("F_WERT_FW".equals(fieldName)) {
				value = data[index][OFFENE_FELD_WERT_BRUTTO_FW];
			} else if ("F_KURS".equals(fieldName)) {
				value = data[index][OFFENE_FELD_KURS];
			} else if ("F_WAEHRUNG".equals(fieldName)) {
				value = data[index][OFFENE_FELD_WAEHRUNG];
			} else if ("F_DEBITORENNR".equals(fieldName)) {
				value = data[index][OFFENE_FELD_DEBITORENNR];
			} else if ("F_OFFEN_FW".equals(fieldName)) {
				value = data[index][OFFENE_FELD_WERT_BRUTTO_OFFEN_FW];
			} else if ("F_WERT_NETTO_FW".equals(fieldName)) {
				value = data[index][OFFENE_FELD_WERT_NETTO_FW];
			} else if ("F_NETTO_ANZAHLUNGEN_FW".equals(fieldName)) {
				value = data[index][OFFENE_FELD_WERT_NETTO_ANZAHLUNGEN_FW];
			} else if ("F_BRUTTO_ANZAHLUNGEN_FW".equals(fieldName)) {
				value = data[index][OFFENE_FELD_WERT_BRUTTO_ANZAHLUNGEN_FW];
			} else if ("F_OFFEN_NETTO_FW".equals(fieldName)) {
				value = data[index][OFFENE_FELD_WERT_NETTO_OFFEN_FW];
			}

			else if ("F_FAELLIGKEITSDATUM".equals(fieldName)) {
				value = data[index][OFFENE_FELD_FAELLIGKEITSDATUM];
			} else if ("F_TAGE_BIS_FAELLIGKEITSDATUM".equals(fieldName)) {
				value = data[index][OFFENE_FELD_TAGE_BIS_FAELLIGKEITSDATUM];
			} else if ("F_FIRMA_LKZ_STATISTIK".equals(fieldName)) {
				value = data[index][OFFENE_FELD_FIRMA_LKZ_STATISTIK];
			} else if ("F_FIRMA_LKZ".equals(fieldName)) {
				value = data[index][OFFENE_FELD_FIRMA_LKZ];
			} else if ("F_VERTRETER".equals(fieldName)) {
				value = data[index][OFFENE_FELD_VERTRETER];
			} else if ("F_MAHNSPERRE".equals(fieldName)) {
				value = data[index][OFFENE_FELD_MAHNSPERRE];
			} else if ("F_SUBREPORT_OFFENE_BUCHUNGEN".equals(fieldName)) {
				value = data[index][OFFENE_SUBREPORT_OFFENE_BUCHUNGEN];
			} else if ("F_KREDITLIMIT".equals(fieldName)) {
				value = data[index][OFFENE_FELD_KREDITLIMIT];
			}
		}
			break;
		case UC_REPORT_ZAHLUNGSJOURNAL: {
			if ("F_RECHNUNGSNUMMER".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_ER_C_NR];
			} else if ("F_FIRMA".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_FIRMA];
			} else if ("F_KOSTENSTELLE".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_KOSTENSTELLE];
			} else if ("F_ZAHLDATUM".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_ZAHLDATUM];
			} else if ("F_BETRAG".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_BETRAG];
			} else if ("F_BETRAG_UST".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_BETRAG_UST];
			} else if ("F_BANK".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_BANK];
			} else if ("F_AUSZUG".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_AUSZUG];
			} else if ("F_WECHSEL".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_WECHSEL];
			} else if ("F_BETRAG_BELEGZEIT".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_BETRAG_MANDANTENWAEHRUNG_ZU_BELEGZEIT];
			} else if ("F_BETRAG_UST_BELEGZEIT".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_BETRAG_UST_MANDANTENWAEHRUNG_ZU_BELEGZEIT];
			} else if ("F_WAEHRUNG".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_BELEGWAEHRUNG];
			} else if ("F_BETRAG_FW".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_BETRAG_FW];
			} else if ("F_BETRAG_UST_FW".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_BETRAG_UST_FW];
			} else if ("F_KONTO".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_KONTO];
			} else if ("F_ZAHLUNGSART".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_ZAHLUNGSART];
			} else if ("F_KASSENBUCH".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_KASSENBUCH];
			} else if ("F_DEBITORENKONTO".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_DEBITORENKONTO];
			} else if ("F_RECHNUNGSART".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_RECHNUNGSART];
			} else if ("F_LAENDERART".equals(fieldName)) {
				value = data[index][ZAHLUNGEN_FELD_LAENDERART];
			}

		}
			break;
		case UC_REPORT_UMSATZ: {
			if ("F_MONAT".equals(fieldName)) {
				value = data[index][UMSATZ_FELD_MONAT];
			} else if ("F_WERT".equals(fieldName)) {
				value = data[index][UMSATZ_FELD_WERT];
			} else if ("F_WERT_UST".equals(fieldName)) {
				value = data[index][UMSATZ_FELD_WERT_UST];
			} else if ("F_WERT_NICHT_ABGERECHNETE_ANZAHLUNGEN"
					.equals(fieldName)) {
				value = data[index][UMSATZ_FELD_WERT_NICHT_ABGERECHNETE_ANZAHLUNGEN];
			} else if ("F_WERT_NICHT_ABGERECHNETE_ANZAHLUNGEN_UST"
					.equals(fieldName)) {
				value = data[index][UMSATZ_FELD_WERT_NICHT_ABGERECHNETE_ANZAHLUNGEN_UST];
			}
		}
			break;
		case UC_REPORT_ALLE: {
			if ("F_BELEGDATUM".equals(fieldName)) {
				value = data[index][FELD_ALLE_BELEGDATUM];
			} else if ("F_BETRAG".equals(fieldName)) {
				value = data[index][FELD_ALLE_BETRAG];
			} else if ("F_BETRAGUST".equals(fieldName)) {
				value = data[index][FELD_ALLE_BETRAGUST];
			} else if ("F_BETRAG_FW".equals(fieldName)) {
				value = data[index][FELD_ALLE_BETRAG_FW];
			} else if ("F_BETRAGUST_FW".equals(fieldName)) {
				value = data[index][FELD_ALLE_BETRAGUST_FW];
			} else if ("F_WERT_ANZAHLUNGEN".equals(fieldName)) {
				value = data[index][FELD_ALLE_WERT_ANZAHLUNGEN];
			} else if ("F_WERT_ANZAHLUNGENUST".equals(fieldName)) {
				value = data[index][FELD_ALLE_WERT_ANZAHLUNGENUST];
			} else if ("F_WERT_ANZAHLUNGEN_FW".equals(fieldName)) {
				value = data[index][FELD_ALLE_WERT_ANZAHLUNGEN_FW];
			} else if ("F_WERT_ANZAHLUNGENUST_FW".equals(fieldName)) {
				value = data[index][FELD_ALLE_WERT_ANZAHLUNGENUST_FW];
			} else if ("F_BEZAHLT".equals(fieldName)) {
				value = data[index][FELD_ALLE_BEZAHLT];
			} else if ("F_BEZAHLT_FW".equals(fieldName)) {
				value = data[index][FELD_ALLE_BEZAHLT_FW];
			} else if ("F_BEZAHLTDATUM".equals(fieldName)) {
				value = data[index][FELD_ALLE_BEZAHLTDATUM];
			} else if ("F_BEZAHLTUST".equals(fieldName)) {
				value = data[index][FELD_ALLE_BEZAHLTUST];
			} else if ("F_BEZAHLTUST_FW".equals(fieldName)) {
				value = data[index][FELD_ALLE_BEZAHLTUST_FW];
			} else if ("F_KOSTENSTELLENUMMER".equals(fieldName)) {
				value = data[index][FELD_ALLE_KOSTENSTELLENUMMER];
			} else if ("F_KUNDE".equals(fieldName)) {
				value = data[index][FELD_ALLE_KUNDE];
			} else if ("F_REVERSECHARGE".equals(fieldName)) {
				value = data[index][FELD_ALLE_REVERSE_CHARGE];
			} else if ("F_STATUS".equals(fieldName)) {
				value = data[index][FELD_ALLE_STATUS];
			} else if ("F_ZOLLBELEGNUMMER".equals(fieldName)) {
				value = data[index][FELD_ALLE_ZOLLBELEGNUMMER];
			} else if ("F_KUNDE_UID".equals(fieldName)) {
				value = data[index][FELD_ALLE_KUNDE_UID];
			} else if ("F_KUNDE2".equals(fieldName)) {
				value = data[index][FELD_ALLE_KUNDE2];
			} else if ("F_KUNDE_STATISTIK".equals(fieldName)) {
				value = data[index][FELD_ALLE_KUNDE_STATISTIK];
			} else if ("F_KUNDE2_STATISTIK".equals(fieldName)) {
				value = data[index][FELD_ALLE_KUNDE2_STATISTIK];
			} else if ("F_MAHNSTUFE".equals(fieldName)) {
				value = data[index][FELD_ALLE_MAHNSTUFE];
			} else if ("F_ORT".equals(fieldName)) {
				value = data[index][FELD_ALLE_ORT];
			} else if ("F_RECHNUNGSNUMMER".equals(fieldName)) {
				value = data[index][FELD_ALLE_RECHNUNGSNUMMER];
			} else if ("F_ZIELDATUM".equals(fieldName)) {
				value = data[index][FELD_ALLE_ZIELDATUM];
			} else if ("F_WECHSEL".equals(fieldName)) {
				value = data[index][FELD_ALLE_WECHSELDATUM];
			} else if ("F_DEBITORENKONTO".equals(fieldName)) {
				value = data[index][FELD_ALLE_DEBITORENKONTO];
			} else if ("F_ADRESSE".equals(fieldName)) {
				value = data[index][FELD_ALLE_ADRESSE];
			} else if ("F_ADRESSE_STATISTIK".equals(fieldName)) {
				value = data[index][FELD_ALLE_ADRESSE_STATISTIK];
			} else if ("F_ZINSEN".equals(fieldName)) {
				value = data[index][FELD_ALLE_ZINSEN];
			} else if ("F_ZAHLTAGE".equals(fieldName)) {
				value = data[index][FELD_ALLE_ZAHLTAGE];
			} else if ("F_VERTRETER".equals(fieldName)) {
				value = data[index][FELD_ALLE_VERTRETER];
			} else if ("F_WAEHRUNG".equals(fieldName)) {
				value = data[index][FELD_ALLE_WAEHRUNG];
			} else if ("F_KURS".equals(fieldName)) {
				value = data[index][FELD_ALLE_KURS];
			} else if ("F_RECHNUNGSNUMMERZUGUTSCHRIFT".equals(fieldName)) {
				value = data[index][FELD_ALLE_RECHNUNGSNUMMERZUGUTSCHRIFT];
			} else if ("F_LAENDERART".equals(fieldName)) {
				value = data[index][FELD_ALLE_LAENDERART];
			} else if ("F_KREDITLIMIT".equals(fieldName)) {
				value = data[index][FELD_ALLE_KREDITLIMIT];
			}

		}
			break;
		case UC_ZUSAMMENFASSENDE_MELDUNG: {
			if ("F_BELEGDATUM".equals(fieldName)) {
				value = data[index][FELD_ZM_BELEGDATUM];
			} else if ("F_BETRAG".equals(fieldName)) {
				value = data[index][FELD_ZM_BETRAG];
			} else if ("F_BETRAGUST".equals(fieldName)) {
				value = data[index][FELD_ZM_BETRAGUST];
			} else if ("F_BEZAHLT".equals(fieldName)) {
				value = data[index][FELD_ZM_BEZAHLT];
			} else if ("F_BEZAHLTDATUM".equals(fieldName)) {
				value = data[index][FELD_ZM_BEZAHLTDATUM];
			} else if ("F_BEZAHLTUST".equals(fieldName)) {
				value = data[index][FELD_ZM_BEZAHLTUST];
			} else if ("F_KOSTENSTELLENUMMER".equals(fieldName)) {
				value = data[index][FELD_ZM_KOSTENSTELLENUMMER];
			} else if ("F_KUNDE".equals(fieldName)) {
				value = data[index][FELD_ZM_KUNDE];
			} else if ("F_REVERSECHARGE".equals(fieldName)) {
				value = data[index][FELD_ZM_REVERSE_CHARGE];
			} else if ("F_KUNDE_UID".equals(fieldName)) {
				value = data[index][FELD_ZM_KUNDE_UID];
			} else if ("F_KUNDE2".equals(fieldName)) {
				value = data[index][FELD_ZM_KUNDE2];
			} else if ("F_KUNDE_STATISTIK".equals(fieldName)) {
				value = data[index][FELD_ZM_KUNDE_STATISTIK];
			} else if ("F_KUNDE2_STATISTIK".equals(fieldName)) {
				value = data[index][FELD_ZM_KUNDE2_STATISTIK];
			} else if ("F_MAHNSTUFE".equals(fieldName)) {
				value = data[index][FELD_ZM_MAHNSTUFE];
			} else if ("F_ORT".equals(fieldName)) {
				value = data[index][FELD_ZM_ORT];
			} else if ("F_RECHNUNGSNUMMER".equals(fieldName)) {
				value = data[index][FELD_ZM_RECHNUNGSNUMMER];
			} else if ("F_ZIELDATUM".equals(fieldName)) {
				value = data[index][FELD_ZM_ZIELDATUM];
			} else if ("F_WECHSEL".equals(fieldName)) {
				value = data[index][FELD_ZM_WECHSELDATUM];
			} else if ("F_DEBITORENKONTO".equals(fieldName)) {
				value = data[index][FELD_ZM_DEBITORENKONTO];
			} else if ("F_ADRESSE".equals(fieldName)) {
				value = data[index][FELD_ZM_ADRESSE];
			} else if ("F_ADRESSE_STATISTIK".equals(fieldName)) {
				value = data[index][FELD_ZM_ADRESSE_STATISTIK];
			} else if ("F_ZINSEN".equals(fieldName)) {
				value = data[index][FELD_ZM_ZINSEN];
			} else if ("F_ZAHLTAGE".equals(fieldName)) {
				value = data[index][FELD_ZM_ZAHLTAGE];
			} else if ("F_VERTRETER".equals(fieldName)) {
				value = data[index][FELD_ZM_VERTRETER];
			} else if ("F_WAEHRUNG".equals(fieldName)) {
				value = data[index][FELD_ZM_WAEHRUNG];
			} else if ("F_KURS".equals(fieldName)) {
				value = data[index][FELD_ZM_KURS];
			} else if ("F_RECHNUNGSNUMMERZUGUTSCHRIFT".equals(fieldName)) {
				value = data[index][FELD_ZM_RECHNUNGSNUMMERZUGUTSCHRIFT];
			} else if ("F_LAENDERART".equals(fieldName)) {
				value = data[index][FELD_ZM_LAENDERART];
			}

		}
			break;
		case UC_REPORT_WARENAUSGANGSJOURNAL: {
			if ("F_RECHNUNGSNUMMER".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_RECHNUNGSNUMMER];
			} else if ("F_DATUM".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_RECHNUNGSDATUM];
			} else if ("F_PREIS".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_POSITIONSPREIS];
			} else if ("F_GESTPREIS".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_GESTPREIS];
			} else if ("F_KOSTENTRAEGER".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_KOSTENTRAEGER];
			} else if ("F_KUNDENBEZEICHNUNG".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_KUNDENBEZEICHNUNG];
			} else if ("F_KUNDENNUMMER".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_KUNDENNUMMER];
			} else if ("F_ARTIKELNUMMER".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_ARTIKELNUMMER];
			} else if ("F_ARTIKELBEZEICHNUNG".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_ARTIKELBEZEICHNUNG];
			} else if ("F_ARTIKELKLASSE".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_ARTIKELKLASSE];
			} else if ("F_ARTIKELGRUPPE".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_ARTIKELGRUPPE];
			} else if ("F_URSPRUNGSLAND".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_URSPRUNGSLAND];
			} else if ("F_WARENVERKEHRSNUMMER".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_WARENVERKEHRSNUMMER];
			} else if ("F_MENGE".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_MENGE];
			} else if ("F_GRUNDNICHTEINGEHALTEN".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_GRUNDNICHTEINGEHALTEN];
			} else if ("F_EINHEIT".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_EINHEIT];
			} else if ("F_ARTIKELBEZEICHNUNG2".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_ARTIKELBEZEICHNUNG2];
			} else if ("F_RECHNUNGSPROVISION".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_RECHNUNGSPROVISION];
			} else if ("F_PROVISIONSEMPFAENGER".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_PROVISIONSEMPFAENGER];
			} else if ("F_ERLOESKONTO".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_ERLOESKONTO];
			} else if ("F_DEBITORENKONTO".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_DEBITORENKONTO];
			} else if ("F_PLZ".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_PLZ];
			} else if ("F_LKZ".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_LKZ];
			} else if ("F_ADRESSE".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_ADRESSE];
			} else if ("F_MWST".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_MWST];
			} else if ("F_BRANCHE".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_BRANCHE];
			} else if ("F_PARTNERKLASSE".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_PARTNERKLASSE];
			} else if ("F_USTLAND".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_USTLAND];
			} else if ("F_LIEFERSCHEINNUMMER".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_LIEFERSCHEINNUMMER];
			} else if ("F_LIEFERSCHEINDATUM".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_LIEFERSCHEINDATUM];
			} else if ("F_STATISTIKADRESSE_PARTNERIID".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_STATISTIKADRESSE_PARTNER_I_ID];
			} else if ("F_STATISTIKADRESSE_ANREDE".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_STATISTIKADRESSE_ANREDE_C_NR];
			} else if ("F_STATISTIKADRESSE_NAME1".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_STATISTIKADRESSE_C_NAME1];
			} else if ("F_STATISTIKADRESSE_NAME2".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_STATISTIKADRESSE_C_NAME2];
			} else if ("F_STATISTIKADRESSE_NAME3ABTEILUNG".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_STATISTIKADRESSE_C_NAME3ABTEILUNG];
			} else if ("F_STATISTIKADRESSE_STRASSE".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_STATISTIKADRESSE_C_STRASSE];
			} else if ("F_STATISTIKADRESSE_ORT".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_STATISTIKADRESSE_ORT];
			} else if ("F_STATISTIKADRESSE_LAND".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_STATISTIKADRESSE_LAND];
			} else if ("F_STATISTIKADRESSE_PLZ".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_STATISTIKADRESSE_PLZ];
			} else if ("F_PARTNERIID".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_PARTNER_I_ID];
			} else if ("F_WAEHRUNG".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_WAEHRUNG];
			} else if ("F_KURS".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_KURS];
			} else if ("F_RECHNUNGSART".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_RECHNUNGSART];
			} else if ("F_ZURECHNUNG".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_ZURECHNUNG];
			} else if ("F_ARTIKELPROVISION".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_ARTIKELPROVISION];
			} else if ("F_BESTELLNUMMER".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_BESTELLNUMMER];
			} else if ("F_KUNDENNUMMER_RECHNUNGSADRESSE_LS".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_KUNDENNUMMER_RECHNUNGSADRESSE_LS];
			} else if ("F_KUNDENBEZEICHNUNG_RECHNUNGSADRESSE_LS"
					.equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_KUNDENBEZEICHNUNG_RECHNUNGSADRESSE_LS];
			} else if ("F_KUNDENKURZBEZEICHNUNG_RECHNUNGSADRESSE_LS"
					.equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_KUNDEKURZBEZEICHNUNG_RECHNUNGSADRESSE_LS];
			} else if ("F_KUNDE_HINWEISEXTERN".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_KUNDE_HINWEISEXT];
			} else if ("F_KUNDE_LIEFERDAUER".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_KUNDE_LIEFERDAUER];
			} else if ("F_GEPLANTER_LIEFERTERMIN".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_GEPLANTER_LIEFERTERMIN];
			} else if ("F_KUNDE_HINWEISINTERN".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_KUNDE_HINWEISINT];
			} else if ("F_KUNDE_PROVISIONSEMPFAENGER_KURZZEICHEN"
					.equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_KUNDE_PROVISIONSEMPFAENGER_KURZZEICHEN];
			} else if ("F_ARTIKELGEWICHTKG".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_ARTIKEL_GEWICHTKG];
			} else if ("F_LAGER_RECHNUNG".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_LAGER_RECHNUNG];
			} else if ("F_LAGER_LIEFERSCHEIN".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_LAGER_LIEFERSCHEIN];
			} else if ("F_ZIELLAGER_LIEFERSCHEIN".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_ZIELLAGER_LIEFERSCHEIN];
			} else if ("F_KUNDENNUMMER_LIEFERADRESSE_LS".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_KUNDENNUMMER_LIEFERADRESSE_LS];
			} else if ("F_TEXTEINGABE".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_TEXTEINGABE];
			} else if ("F_ILN_LIEFERADRESSE".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_ILN_LIEFERADRESSE];
			} else if ("F_ILN_RECHNUNGSADRESSE".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_ILN_RECHNUNGSADRESSE];
			} else if ("F_LIEFERADRESSE_PARTNERIID".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_LIEFERADRESSE_PARTNER_I_ID];
			} else if ("F_LIEFERADRESSE_ANREDE".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_LIEFERADRESSE_ANREDE_C_NR];
			} else if ("F_LIEFERADRESSE_NAME1".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_LIEFERADRESSE_C_NAME1];
			} else if ("F_LIEFERADRESSE_NAME2".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_LIEFERADRESSE_C_NAME2];
			} else if ("F_LIEFERADRESSE_NAME3ABTEILUNG".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_LIEFERADRESSE_C_NAME3ABTEILUNG];
			} else if ("F_LIEFERADRESSE_STRASSE".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_LIEFERADRESSE_C_STRASSE];
			} else if ("F_LIEFERADRESSE_LAND".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_LIEFERADRESSE_LAND];
			} else if ("F_LIEFERADRESSE_ORT".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_LIEFERADRESSE_ORT];
			} else if ("F_LIEFERADRESSE_PLZ".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_LIEFERADRESSE_PLZ];
			} else if ("F_AUFTRAGSNUMMER".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_AUFTRAG_NUMMER];
			} else if ("F_AUFTRAGBEGRUENDUNG".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_AUFTRAG_BEGRUENDUNG];
			} else if ("F_SETARTIKEL_TYP".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_SETARTIKEL_TYP];
			} else if ("F_PROJEKT".equals(fieldName)) {
				value = data[index][FELD_WARENAUSGANG_PROJEKT];
			}
		}
			break;
		case UC_ZAHLSCHEIN: {
			value = null;
		}
			break;

		}
		return value;
	}

	protected FibuExportManager getFibuExportManger(TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (fibuExportManager == null) {
			try {
				ParametermandantDto parameter = getParameterFac()
						.getMandantparameter(theClientDto.getMandant(),
								ParameterFac.KATEGORIE_FINANZ,
								ParameterFac.PARAMETER_FINANZ_EXPORT_VARIANTE);
				String variante = parameter.getCWert();

				fibuExportManager = FibuExportManagerFactory
						.getFibuErloeskontoManager(variante, theClientDto);
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
				return null;
			}
		}

		return fibuExportManager;
	}

	/**
	 * Den gecacheten Exportmanager auf null setzen, damit er dann beim
	 * naechsten Zugriff erneut geladen wird
	 */
	protected void resetFibuExportManager() {
		fibuExportManager = null;
	}

	/**
	 * Rechnung drucken.
	 * 
	 * @param rechnungIId
	 *            Integer
	 * @param locale
	 *            Locale
	 * @param bMitLogo
	 *            Boolean
	 * @param iAnzahlKopien
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @return JasperPrint
	 * @throws EJBExceptionLP
	 */
	public JasperPrintLP[] printRechnung(Integer rechnungIId, Locale locale,
			Boolean bMitLogo, Integer iAnzahlKopien, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// belegkopien: 0 Der Methode wird die Anzahl der Kopien uebergeben
		// belegkopien: 1 Der Rueckgabetyp der Print-Methode ist JasperPrint []
		if (rechnungIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("rechnungIId == null"));
		}
		return buildRechnungReport(rechnungIId,
				RechnungReportFac.REPORT_RECHNUNG,
				RechnungReportFac.REPORT_MODUL, locale, bMitLogo,
				iAnzahlKopien, null, false, theClientDto);
	}

	public JasperPrintLP printRechnungAlsMahnung(Integer rechnungIId,
			Locale locale, Boolean bMitLogo, Integer iMahnstufe,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		// belegkopien: 0 Der Methode wird die Anzahl der Kopien uebergeben
		// belegkopien: 1 Der Rueckgabetyp der Print-Methode ist JasperPrint []
		if (rechnungIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("rechnungIId == null"));
		}
		JasperPrintLP rechnungsprint = buildRechnungReport(rechnungIId,
				FinanzReportFac.REPORT_MAHNUNG_AUSFUEHRLICH,
				FinanzReportFac.REPORT_MODUL, locale, bMitLogo, 1, iMahnstufe,
				false, theClientDto)[0];
		return rechnungsprint;
	}

	/**
	 * Gutschrift drucken.
	 * 
	 * @param rechnungIId
	 *            Integer
	 * @param locale
	 *            Locale
	 * @param bMitLogo
	 *            Boolean
	 * @param iAnzahlKopien
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @return JasperPrint
	 * @throws EJBExceptionLP
	 */
	public JasperPrintLP[] printGutschrift(Integer rechnungIId, Locale locale,
			Boolean bMitLogo, Integer iAnzahlKopien, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (rechnungIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("rechnungIId == null"));
		}
		return buildRechnungReport(rechnungIId,
				RechnungReportFac.REPORT_GUTSCHRIFT,
				RechnungReportFac.REPORT_MODUL, locale, bMitLogo,
				iAnzahlKopien, null, false, theClientDto);
	}

	/**
	 * Proformarechnung drucken.
	 * 
	 * @param rechnungIId
	 *            Integer
	 * @param locale
	 *            Locale
	 * @param bMitLogo
	 *            Boolean
	 * @param iAnzahlKopien
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @return JasperPrint
	 * @throws EJBExceptionLP
	 */
	public JasperPrintLP[] printProformarechnung(Integer rechnungIId,
			Locale locale, Boolean bMitLogo, Integer iAnzahlKopien,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (rechnungIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("rechnungIId == null"));
		}
		return buildRechnungReport(rechnungIId,
				RechnungReportFac.REPORT_PROFORMARECHNUNG,
				RechnungReportFac.REPORT_MODUL, locale, bMitLogo,
				iAnzahlKopien, null, false, theClientDto);
	}

	/**
	 * Alle Gutschriften eines Mandanten im Zeitraum drucken
	 * 
	 * @param krit
	 *            ReportJournalKriterienDto
	 * @param theClientDto
	 *            Date
	 * @throws EJBExceptionLP
	 * @return JasperPrint
	 */
	public JasperPrintLP printGutschriftenAlle(
			ReportRechnungJournalKriterienDto krit, TheClientDto theClientDto)
			throws EJBExceptionLP {
		return buildAlleReport(krit, RechnungFac.RECHNUNGTYP_GUTSCHRIFT,
				RechnungReportFac.REPORT_GUTSCHRIFTEN_ALLE, theClientDto);
	}

	/**
	 * Alle Offenen Rechnungen eines Mandanten im Zeitraum drucken
	 * 
	 * @param theClientDto
	 *            String
	 * @param iGeschaeftsjahr
	 *            Integer
	 * @param bMitGutschriften
	 *            Boolean
	 * @throws EJBExceptionLP
	 * @return JasperPrint
	 */
	public JasperPrintLP printRechnungenUmsatz(TheClientDto theClientDto,
			Integer iGeschaeftsjahr, Boolean bMitGutschriften)
			throws EJBExceptionLP {
		Locale locUI = theClientDto.getLocUi();
		// Monatsnamen localeabhaengig mit Calendar formatieren
		SimpleDateFormat dateformat = new SimpleDateFormat("MMMM", locUI);
		Calendar cal = GregorianCalendar.getInstance(locUI);
		cal.set(Calendar.YEAR, GregorianCalendar.JANUARY, 1);

		useCase = UC_REPORT_UMSATZ;
		try {
			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			mapParameter.put("P_GESCHAEFTSJAHR", iGeschaeftsjahr);
			if (bMitGutschriften.booleanValue()) {
				mapParameter.put(LPReport.P_FILTER, "inklusive Gutschriften");
			} else {
				mapParameter.put(LPReport.P_FILTER, "exklusive Gutschriften");
			}
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);
			mapParameter.put(LPReport.P_WAEHRUNG, mandantDto.getWaehrungCNr());

			data = new Object[12][5];
			String sKriterium = null;
			if (bMitGutschriften.booleanValue()) {
				sKriterium = RechnungFac.KRIT_MIT_GUTSCHRIFTEN;
			} else {
				sKriterium = RechnungFac.KRIT_OHNE_GUTSCHRIFTEN;
			}

			for (int i = 0; i < data.length; i++) {
				String sMonat = null;
				GregorianCalendar gcBerechnungsdatumVon = null;
				GregorianCalendar gcBerechnungsdatumBis = null;

				switch (i) {
				case 0: {
					gcBerechnungsdatumVon = new GregorianCalendar(locUI);
					gcBerechnungsdatumVon.set(iGeschaeftsjahr.intValue(),
							GregorianCalendar.JANUARY, 1);
					sMonat = dateformat.format(gcBerechnungsdatumVon.getTime());

					gcBerechnungsdatumBis = new GregorianCalendar(
							iGeschaeftsjahr.intValue(),
							GregorianCalendar.FEBRUARY, 1);
				}
					break;
				case 1: {
					gcBerechnungsdatumVon = new GregorianCalendar(locUI);
					gcBerechnungsdatumVon.set(iGeschaeftsjahr.intValue(),
							GregorianCalendar.FEBRUARY, 1);
					sMonat = dateformat.format(gcBerechnungsdatumVon.getTime());

					gcBerechnungsdatumBis = new GregorianCalendar(
							iGeschaeftsjahr.intValue(),
							GregorianCalendar.MARCH, 1);
				}
					break;
				case 2: {
					gcBerechnungsdatumVon = new GregorianCalendar(locUI);
					gcBerechnungsdatumVon.set(iGeschaeftsjahr.intValue(),
							GregorianCalendar.MARCH, 1);
					sMonat = dateformat.format(gcBerechnungsdatumVon.getTime());

					gcBerechnungsdatumBis = new GregorianCalendar(
							iGeschaeftsjahr.intValue(),
							GregorianCalendar.APRIL, 1);
				}
					break;
				case 3: {
					gcBerechnungsdatumVon = new GregorianCalendar(locUI);
					gcBerechnungsdatumVon.set(iGeschaeftsjahr.intValue(),
							GregorianCalendar.APRIL, 1);
					sMonat = dateformat.format(gcBerechnungsdatumVon.getTime());

					gcBerechnungsdatumBis = new GregorianCalendar(
							iGeschaeftsjahr.intValue(), GregorianCalendar.MAY,
							1);
				}
					break;
				case 4: {
					gcBerechnungsdatumVon = new GregorianCalendar(locUI);
					gcBerechnungsdatumVon.set(iGeschaeftsjahr.intValue(),
							GregorianCalendar.MAY, 1);
					sMonat = dateformat.format(gcBerechnungsdatumVon.getTime());

					gcBerechnungsdatumBis = new GregorianCalendar(
							iGeschaeftsjahr.intValue(), GregorianCalendar.JUNE,
							1);
				}
					break;
				case 5: {
					gcBerechnungsdatumVon = new GregorianCalendar(locUI);
					gcBerechnungsdatumVon.set(iGeschaeftsjahr.intValue(),
							GregorianCalendar.JUNE, 1);
					sMonat = dateformat.format(gcBerechnungsdatumVon.getTime());

					gcBerechnungsdatumBis = new GregorianCalendar(
							iGeschaeftsjahr.intValue(), GregorianCalendar.JULY,
							1);
				}
					break;
				case 6: {
					gcBerechnungsdatumVon = new GregorianCalendar(locUI);
					gcBerechnungsdatumVon.set(iGeschaeftsjahr.intValue(),
							GregorianCalendar.JULY, 1);
					sMonat = dateformat.format(gcBerechnungsdatumVon.getTime());

					gcBerechnungsdatumBis = new GregorianCalendar(
							iGeschaeftsjahr.intValue(),
							GregorianCalendar.AUGUST, 1);
				}
					break;
				case 7: {
					gcBerechnungsdatumVon = new GregorianCalendar(locUI);
					gcBerechnungsdatumVon.set(iGeschaeftsjahr.intValue(),
							GregorianCalendar.AUGUST, 1);
					sMonat = dateformat.format(gcBerechnungsdatumVon.getTime());

					gcBerechnungsdatumBis = new GregorianCalendar(
							iGeschaeftsjahr.intValue(),
							GregorianCalendar.SEPTEMBER, 1);
				}
					break;
				case 8: {
					gcBerechnungsdatumVon = new GregorianCalendar(locUI);
					gcBerechnungsdatumVon.set(iGeschaeftsjahr.intValue(),
							GregorianCalendar.SEPTEMBER, 1);
					sMonat = dateformat.format(gcBerechnungsdatumVon.getTime());

					gcBerechnungsdatumBis = new GregorianCalendar(
							iGeschaeftsjahr.intValue(),
							GregorianCalendar.OCTOBER, 1);
				}
					break;
				case 9: {
					gcBerechnungsdatumVon = new GregorianCalendar(locUI);
					gcBerechnungsdatumVon.set(iGeschaeftsjahr.intValue(),
							GregorianCalendar.OCTOBER, 1);
					sMonat = dateformat.format(gcBerechnungsdatumVon.getTime());

					gcBerechnungsdatumBis = new GregorianCalendar(
							iGeschaeftsjahr.intValue(),
							GregorianCalendar.NOVEMBER, 1);
				}
					break;
				case 10: {
					gcBerechnungsdatumVon = new GregorianCalendar(locUI);
					gcBerechnungsdatumVon.set(iGeschaeftsjahr.intValue(),
							GregorianCalendar.NOVEMBER, 1);
					sMonat = dateformat.format(gcBerechnungsdatumVon.getTime());

					gcBerechnungsdatumBis = new GregorianCalendar(
							iGeschaeftsjahr.intValue(),
							GregorianCalendar.DECEMBER, 1);
				}
					break;
				case 11: {
					gcBerechnungsdatumVon = new GregorianCalendar(locUI);
					gcBerechnungsdatumVon.set(iGeschaeftsjahr.intValue(),
							GregorianCalendar.DECEMBER, 1);
					sMonat = dateformat.format(gcBerechnungsdatumVon.getTime());
					gcBerechnungsdatumBis = new GregorianCalendar(
							iGeschaeftsjahr.intValue() + 1,
							GregorianCalendar.JANUARY, 1);
				}
					break;
				}
				data[i][UMSATZ_FELD_MONAT] = sMonat;
				// den Umsatz brutto bestimmen
				BigDecimal bdUmsatzBrutto = getRechnungFac()
						.berechneSummeUmsatzBrutto(theClientDto.getMandant(),
								sKriterium, gcBerechnungsdatumVon,
								gcBerechnungsdatumBis, theClientDto);
				// den Umsatz netto bestimmen
				BigDecimal bdUmsatzNetto = getRechnungFac()
						.berechneSummeUmsatzNetto(theClientDto.getMandant(),
								sKriterium, gcBerechnungsdatumVon,
								gcBerechnungsdatumBis, theClientDto);

				// den Anzahlung Gesamtbrutto bestimmen
				BigDecimal bdAnzahlungBruttoGesamt = getRechnungFac()
						.berechneSummeAnzahlungBrutto(
								theClientDto.getMandant(), sKriterium,
								gcBerechnungsdatumVon, gcBerechnungsdatumBis,
								false, theClientDto);

				// den Anzahlung Gesamtnetto bestimmen
				BigDecimal bdAnzahlungNettoGesamt = getRechnungFac()
						.berechneSummeAnzahlungNetto(theClientDto.getMandant(),
								sKriterium, gcBerechnungsdatumVon,
								gcBerechnungsdatumBis, false, theClientDto);

				bdUmsatzBrutto = bdUmsatzBrutto
						.subtract(bdAnzahlungBruttoGesamt);
				bdUmsatzNetto = bdUmsatzNetto.subtract(bdAnzahlungNettoGesamt);

				// den nichabgerechneten Anzahlungen brutto bestimmen
				BigDecimal bdAnzahlungBruttoNichtAbgerechnet = getRechnungFac()
						.berechneSummeAnzahlungBrutto(
								theClientDto.getMandant(), sKriterium,
								gcBerechnungsdatumVon, gcBerechnungsdatumBis,
								true, theClientDto);

				// den Anzahlung netto bestimmen
				BigDecimal bdAnzahlungNettoGesamtNichtAbgerechnet = getRechnungFac()
						.berechneSummeAnzahlungNetto(theClientDto.getMandant(),
								sKriterium, gcBerechnungsdatumVon,
								gcBerechnungsdatumBis, true, theClientDto);

				data[i][UMSATZ_FELD_WERT] = bdUmsatzNetto;
				data[i][UMSATZ_FELD_WERT_UST] = bdUmsatzBrutto
						.subtract(bdUmsatzNetto);

				data[i][UMSATZ_FELD_WERT_NICHT_ABGERECHNETE_ANZAHLUNGEN] = bdAnzahlungBruttoNichtAbgerechnet;
				data[i][UMSATZ_FELD_WERT_NICHT_ABGERECHNETE_ANZAHLUNGEN_UST] = bdAnzahlungBruttoNichtAbgerechnet
						.subtract(bdAnzahlungNettoGesamtNichtAbgerechnet);

			}
			initJRDS(mapParameter, RechnungReportFac.REPORT_MODUL,
					RechnungReportFac.REPORT_RECHNUNGEN_UMSATZ,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);
			return getReportPrint();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	/**
	 * RE/AR/PR drucken
	 * 
	 * @param rechnungIId
	 *            Integer
	 * @param sReportName
	 *            String
	 * @param locale
	 *            Locale
	 * @param bMitLogo
	 *            Boolean
	 * @param iAnzahlKopien
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @return JasperPrint
	 * @throws EJBExceptionLP
	 */
	private JasperPrintLP[] buildRechnungReport(Integer rechnungIId,
			String sReportName, String sReportModul, Locale locale,
			Boolean bMitLogo, Integer iAnzahlKopien, Integer iMahnstufe,
			boolean bSortiertNachArtikelnummer, TheClientDto theClientDto)
			throws EJBExceptionLP {

		useCase = UC_RE_GS_PR;
		resetFibuExportManager();

		try {
			// Rechnung holen
			final RechnungDto rechnungDto = getRechnungFac()
					.rechnungFindByPrimaryKey(rechnungIId);
			// Rechnung im Reverse Charge Verfahren ?
			final boolean bReverseCharge = Helper.short2boolean(rechnungDto
					.getBReversecharge());

			// Zur detaillierten UST-Auflistung
			final Set<?> mwstSatzKeys = getMandantFac()
					.mwstsatzIIdFindAllByMandant(rechnungDto.getMandantCNr(),
							theClientDto);
			final LinkedHashMap<Integer, MwstsatzReportDto> mwstMap = new LinkedHashMap<Integer, MwstsatzReportDto>();
			// Fuer jeden UST-Satz gibt es einen Eintrag
			for (Iterator<?> iter = mwstSatzKeys.iterator(); iter.hasNext();) {
				Integer item = (Integer) iter.next();
				MwstsatzReportDto mwstsatzReportDto = new MwstsatzReportDto();
				mwstMap.put(item, mwstsatzReportDto);
			}
			// Kunde holen
			final KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
					rechnungDto.getKundeIId(), theClientDto);
			// Sollen die Rabatte auf der Rechnung angedruckt werden?
			final boolean bDruckeRabatt = Helper.short2boolean(kundeDto
					.getBRechnungsdruckmitrabatt());
			// Positionen der Rechnung holen
			final RechnungPositionDto[] rePos = getRechnungFac()
					.getRechnungPositionenByRechnungIId(rechnungIId,
							theClientDto);
			// die Lieferscheindaten werden in HashMaps gecacht, Schluessel ist
			// jeweils die LS-IId
			final HashMap<Integer, LieferscheinDto> hmLSDto = new HashMap<Integer, LieferscheinDto>();
			// Lieferscheinpositionen cachen
			final HashMap<Integer, LieferscheinpositionDto[]> hmLSPosDtos = new HashMap<Integer, LieferscheinpositionDto[]>();
			final HashMap<Integer, Object[][]> hmLSData = new HashMap<Integer, Object[][]>();
			// Auch die anzudruckenden Stuecklistenpositionen, key ist die
			// ArtikelIId
			final HashMap<Integer, StuecklisteInfoDto> hmStklPos = new HashMap<Integer, StuecklisteInfoDto>();
			// Auch die Artikel, key ist die ArtikelIId
			final HashMap<Integer, ArtikelDto> hmArtikel = new HashMap<Integer, ArtikelDto>();
			// Nach jeder mengenbehafteten Position eine Leerzeile einfuegen?
			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.PARAMETER_LEERZEILE_NACH_MENGENBEHAFTETERPOSITION);
			final boolean bLeerzeileNachMengenbehafteterPosition = ((Boolean) parameter
					.getCWertAsObject()).booleanValue();
			// Beinhaltet Chargennummer die Mindesthalbarkeit MHD?
			ParametermandantDto parameter_MhdInCharge = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL,
							ParameterFac.PARAMETER_CHARGENNUMMER_BEINHALTET_MINDESTHALTBARKEITSDATUM);
			ParametermandantDto parameterAbweichung = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_RECHNUNG,
							ParameterFac.PARAMETER_MAXIMALE_ABWEICHUNG_SUMMENAUSDRUCK);
			final boolean bChargennummerBeinhaltetMindesthaltbarkeitsdatum = ((Boolean) parameter_MhdInCharge
					.getCWertAsObject()).booleanValue();
			final Double dAbweichung = (Double) parameterAbweichung
					.getCWertAsObject();
			
			ParametermandantDto parameterGsNenntsichRechnungskorrektur = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_GUTSCHRIFT,
							ParameterFac.PARAMETER_GUTSCHRIFT_NENNT_SICH_RECHNUNGSKORREKTUR);
			boolean bGutschriftNenntSichRechnungskorrektur = ((Boolean) parameterGsNenntsichRechnungskorrektur
					.getCWertAsObject()).booleanValue();
			
			// jetzt wird die anzahl der zeilen berechnet, gleichzeitig werden
			// alle erforderlichen Daten geholt
			final int rows = getZeilenAnzahlDesRechnungsDrucksUndCacheDaten(
					rePos, hmLSDto, hmLSPosDtos, hmLSData, hmStklPos,
					hmArtikel, bLeerzeileNachMengenbehafteterPosition,
					theClientDto);
			// Datenarray kreieren
			data = new Object[rows][RECHNUNG_ANZAHL_SPALTEN];
			// Artikelpositionsnummer fuer alle mengen- und preisbehafteten
			// positionen
			iArtikelpositionsnummer = new Integer(1);
			// seitenumbruch: deklarieren
			Boolean bSeitenumbruch = new Boolean(false);
			int index = 0;
			for (int p = 0; p < rePos.length; p++) {
				data[index][RECHNUNG_FELD_POSITIONSOBJEKT] = getSystemReportFac()
						.getPositionForReport(LocaleFac.BELEGART_RECHNUNG,
								rePos[p].getIId(), theClientDto);
				data[index][RECHNUNG_FELD_INTERNAL_IID] = rePos[p].getIId();

				// 15145
				if (rePos[p].getAuftragpositionIId() != null) {
					AuftragpositionDto abPosDto = getAuftragpositionFac()
							.auftragpositionFindByPrimaryKey(
									rePos[p].getAuftragpositionIId());
					AuftragDto auftragDto = getAuftragFac()
							.auftragFindByPrimaryKey(abPosDto.getBelegIId());

					data[index][RECHNUNG_FELD_AUFTRAG_BESTELLDATUM] = auftragDto
							.getDBestelldatum();
					data[index][RECHNUNG_FELD_AUFTRAG_BESTELLNUMMER] = auftragDto
							.getCBestellnummer();
					data[index][RECHNUNG_FELD_AUFTRAG_NUMMER] = auftragDto
							.getCNr();
					data[index][RECHNUNG_FELD_AUFTRAG_PROJEKT] = auftragDto
							.getCBezProjektbezeichnung();
				}

				if (rePos[p].getTypCNr() != null) {
					if (rePos[p].getRechnungpositionartCNr().equals(
							RechnungFac.POSITIONSART_RECHNUNG_POSITION)) {
						index = addPositionToDataPosition(rechnungDto,
								rePos[p], mwstMap, index, bDruckeRabatt,
								bSeitenumbruch, theClientDto);
						index++;
					}
				} else {
					if (rePos[p].getRechnungpositionartCNr().equals(
							RechnungFac.POSITIONSART_RECHNUNG_SEITENUMBRUCH)) {
						// seitenumbruch: togglemodus
						bSeitenumbruch = new Boolean(
								!bSeitenumbruch.booleanValue());
					}
					// seitenumbruch: uebergeben
					data[index][RECHNUNG_FELD_B_SEITENUMBRUCH] = bSeitenumbruch;
					// Artikelpositionen
					if (rePos[p].getRechnungpositionartCNr().equals(
							RechnungFac.POSITIONSART_RECHNUNG_IDENT)
							|| rePos[p]
									.getRechnungpositionartCNr()
									.equals(RechnungFac.POSITIONSART_RECHNUNG_HANDEINGABE)) {
						// daten anhaengen
						index = addPositionToDataIdentHandeingabe(
								rechnungDto,
								rePos[p],
								bDruckeRabatt,
								mwstMap,
								hmArtikel,
								hmStklPos,
								bSeitenumbruch,
								index,
								locale,
								bLeerzeileNachMengenbehafteterPosition,
								bChargennummerBeinhaltetMindesthaltbarkeitsdatum,
								theClientDto);
					} else if (rePos[p].getRechnungpositionartCNr().equals(
							RechnungFac.POSITIONSART_RECHNUNG_LIEFERSCHEIN)) {
						index = addPositionToDataLieferschein(
								rechnungDto,
								rePos[p],
								bDruckeRabatt,
								mwstMap,
								hmLSDto,
								hmLSPosDtos,
								hmLSData,
								locale,
								bSeitenumbruch,
								index,
								bLeerzeileNachMengenbehafteterPosition,
								bChargennummerBeinhaltetMindesthaltbarkeitsdatum,
								theClientDto);
					} else if (rePos[p]
							.getRechnungpositionartCNr()
							.equals(RechnungFac.POSITIONSART_RECHNUNG_INTELLIGENTE_ZWISCHENSUMME)) {
						index = addPositionToDataIntelligenteZwischensumme(
								rechnungDto,
								rePos[p],
								bDruckeRabatt,
								mwstMap,
								hmArtikel,
								hmStklPos,
								bSeitenumbruch,
								index,
								locale,
								bLeerzeileNachMengenbehafteterPosition,
								bChargennummerBeinhaltetMindesthaltbarkeitsdatum,
								theClientDto);
					}
					// Betrifft Positionen
					else if (rePos[p].getRechnungpositionartCNr().equals(
							RechnungFac.POSITIONSART_RECHNUNG_BETRIFFT)) {
						data[index][RECHNUNG_FELD_POSITIONSART] = rePos[p]
								.getRechnungpositionartCNr();
						data[index][RECHNUNG_FELD_FREIERTEXT] = rePos[p]
								.getCBez();
						index++;
					}
					// Ursprung Positionen
					else if (rePos[p].getRechnungpositionartCNr().equals(
							RechnungFac.POSITIONSART_RECHNUNG_URSPRUNGSLAND)) {
						data[index][RECHNUNG_FELD_POSITIONSART] = rePos[p]
								.getRechnungpositionartCNr();
						data[index][RECHNUNG_FELD_FREIERTEXT] = rePos[p]
								.getCBez();
						index++;
					} else if (rePos[p].getRechnungpositionartCNr().equals(
							AngebotServiceFac.ANGEBOTPOSITIONART_ENDSUMME)) {
						data[index][RECHNUNG_FELD_POSITIONSART] = rePos[p]
								.getPositionsartCNr();

						index++;
					}
					// Texteingabe Positionen
					else if (rePos[p].getRechnungpositionartCNr().equals(
							RechnungFac.POSITIONSART_RECHNUNG_TEXTEINGABE)) {
						data[index][RECHNUNG_FELD_POSITIONSART] = rePos[p]
								.getRechnungpositionartCNr();
						// lt. WH soll ein leerer text als leerzeile erscheinen
						String sText = rePos[p].getXTextinhalt();
						if (sText != null && sText.trim().equals("")) {
							data[index] = getLeerzeile(bSeitenumbruch);
						} else {
							data[index][RECHNUNG_FELD_FREIERTEXT] = sText;
						}
						index++;
					}
					// Textbaustein Positionen
					else if (rePos[p].getRechnungpositionartCNr().equals(
							RechnungFac.POSITIONSART_RECHNUNG_TEXTBAUSTEIN)) {
						// Dto holen
						MediastandardDto oMediastandardDto = getMediaFac()
								.mediastandardFindByPrimaryKey(
										rePos[p].getMediastandardIId());
						// zum Drucken vorbereiten
						BelegPositionDruckTextbausteinDto druckDto = printTextbaustein(
								oMediastandardDto, theClientDto);
						data[index][RECHNUNG_FELD_FREIERTEXT] = druckDto
								.getSFreierText();
						data[index][RECHNUNG_FELD_IMAGE] = druckDto.getOImage();

						data[index][RECHNUNG_FELD_POSITIONSART] = RechnungFac.POSITIONSART_RECHNUNG_TEXTBAUSTEIN;
						index++;
					}
					// Leerzeile Positionen
					else if (rePos[p].getRechnungpositionartCNr().equals(
							RechnungFac.POSITIONSART_RECHNUNG_LEERZEILE)) {
						data[index] = getLeerzeile(bSeitenumbruch);
						index++;
					}
				}
			}

			// PJ 16369
			if (bSortiertNachArtikelnummer) {
				for (int i = data.length - 1; i > 0; --i) {
					for (int j = 0; j < i; ++j) {
						Object[] o = data[j];
						Object[] o1 = data[j + 1];

						String sArtikelnummer1 = "";

						if (o[RECHNUNG_FELD_IDENTNUMMER] != null) {
							sArtikelnummer1 = (String) o[RECHNUNG_FELD_IDENTNUMMER];
						}

						String sArtikelnummer2 = "";

						if (o1[RECHNUNG_FELD_IDENTNUMMER] != null) {
							sArtikelnummer2 = (String) o1[RECHNUNG_FELD_IDENTNUMMER];
						}

						if (sArtikelnummer1.compareTo(sArtikelnummer2) > 0) {
							data[j] = o1;
							data[j + 1] = o;
						}
					}
				}
			}

			AnsprechpartnerDto oAnsprechpartner = null;
			if (rechnungDto.getAnsprechpartnerIId() != null) {
				oAnsprechpartner = getAnsprechpartnerFac()
						.ansprechpartnerFindByPrimaryKey(
								rechnungDto.getAnsprechpartnerIId(),
								theClientDto);
			}
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);
			// Parameter
			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			if (FinanzReportFac.REPORT_MAHNUNG_AUSFUEHRLICH.equals(sReportName)) {
				mapParameter = getFinanzReportFac()
						.getMahnungsParameterFuerRechnung(theClientDto,
								rechnungIId, iMahnstufe);
			}

			// CK: PJ 13849
			mapParameter.put(
					"P_BEARBEITER",
					getPersonalFac().getPersonRpt(
							rechnungDto.getPersonalIIdAnlegen(), theClientDto));
			mapParameter.put(LPReport.P_MANDANTADRESSE,
					Helper.formatMandantAdresse(mandantDto));

			// SK Comment out Immer die Adresse des Kunden
			// if (kundeDto.getPartnerIIdRechnungsadresse() != null) {
			// PartnerDto kundeDtoRechnungsadresse =
			// getPartnerFac().partnerFindByPrimaryKey(
			// kundeDto.getPartnerIIdRechnungsadresse(), theClientDto);
			// mapParameter.put("P_KUNDE_ADRESSBLOCK",
			// formatAdresseFuerAusdruck(kundeDtoRechnungsadresse,
			// oAnsprechpartner, mandantDto, locale));
			//
			// KundeDto rechnungKunde =
			// getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(
			// kundeDtoRechnungsadresse.getIId(),
			// theClientDto.getMandant(), theClientDto);
			// if (rechnungKunde != null) {
			// mapParameter.put("P_RECHNUNG_LIEFERANTENNR",
			// rechnungKunde.getCLieferantennr());
			// }
			// }
			// else {
			mapParameter.put(
					"P_KUNDE_ADRESSBLOCK",
					formatAdresseFuerAusdruck(kundeDto.getPartnerDto(),
							oAnsprechpartner, mandantDto, locale,
							LocaleFac.BELEGART_RECHNUNG));

			
			// PJ18870
			if (rechnungDto.getRechnungartCNr().equals(
					RechnungFac.RECHNUNGART_GUTSCHRIFT)) {
				mapParameter.put(
						"P_SUBREPORT_PARTNERKOMMENTAR",
						getPartnerServicesFac()
								.getSubreportAllerMitzudruckendenPartnerkommentare(
										kundeDto.getPartnerDto().getIId(), true,
										LocaleFac.BELEGART_GUTSCHRIFT, theClientDto));
			} else {
				mapParameter.put(
						"P_SUBREPORT_PARTNERKOMMENTAR",
						getPartnerServicesFac()
								.getSubreportAllerMitzudruckendenPartnerkommentare(
										kundeDto.getPartnerDto().getIId(), true,
										LocaleFac.BELEGART_RECHNUNG, theClientDto));
			}
			
			if (oAnsprechpartner != null) {
				mapParameter.put(
						"P_ANSPRECHPARTNER_KUNDE_ADRESSBLOCK",
						getPartnerFac()
								.formatFixAnredeTitelName2Name1FuerAdresskopf(
										oAnsprechpartner.getPartnerDto(),
										locale, null));
			}

			// }
			// Debitorenkonto des Kunden
			if (kundeDto.getIidDebitorenkonto() != null) {
				KontoDto kontoDto = getFinanzFac().kontoFindByPrimaryKey(
						kundeDto.getIidDebitorenkonto());
				mapParameter.put("P_KUNDE_DEBITORENKONTO", kontoDto.getCNr());
			}

			mapParameter.put("P_GUTSCHRIFT_NENNT_SICH_RECHNUNGSKORREKTUR",new Boolean(bGutschriftNenntSichRechnungskorrektur));
			
			
			// PJ17820
			mapParameter.put("P_AUFTRAGANZAHL",
					getGesamtAnzahlAnAuftraegen(rechnungDto));

			// Fuer Voest
			mapParameter.put("P_KUNDE_NAME1", kundeDto.getPartnerDto()
					.getCName1nachnamefirmazeile1());
			mapParameter.put("P_KUNDE_NAME2", kundeDto.getPartnerDto()
					.getCName2vornamefirmazeile2());
			mapParameter.put("P_KUNDE_NAME3", kundeDto.getPartnerDto()
					.getCName3vorname2abteilung());

			mapParameter.put("P_KUNDE_STRASSE", kundeDto.getPartnerDto()
					.getCStrasse());

			if (kundeDto.getPartnerDto().getLandplzortDto() != null) {
				mapParameter.put("P_KUNDE_LKZ", kundeDto.getPartnerDto()
						.getLandplzortDto().getLandDto().getCLkz());
				mapParameter.put("P_KUNDE_PLZ", kundeDto.getPartnerDto()
						.getLandplzortDto().getCPlz());
				mapParameter.put("P_KUNDE_ORT", kundeDto.getPartnerDto()
						.getLandplzortDto().getOrtDto().getCName());
			}

			mapParameter.put("P_KUNDE_UID", kundeDto.getPartnerDto().getCUid());

			mapParameter.put(
					"P_KUNDE_LAENDERART",
					getFinanzServiceFac().getLaenderartZuPartner(
							kundeDto.getPartnerDto().getIId(), theClientDto));

			mapParameter.put("P_KUNDE_EORI", kundeDto.getPartnerDto()
					.getCEori());
			mapParameter.put("P_KUNDE_FREMDSYSTEMNR",
					kundeDto.getCFremdsystemnr());
			mapParameter.put("P_KUNDE_TOUR", kundeDto.getCTour());
			mapParameter.put("P_KUNDE_KUNDENNUMMER",
					kundeDto.getIKundennummer());
			mapParameter.put("P_ILN", kundeDto.getPartnerDto().getCIln());

			mapParameter.put("P_LIEFERANTENNR", kundeDto.getCLieferantennr());

			mapParameter.put("P_MAXIMALE_ABWEICHUNG", dAbweichung);

			ParametermandantDto parameterWerbeabgabe = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL,
							ParameterFac.PARAMETER_WERBEABGABE_PROZENT);
			mapParameter.put("P_WERBEABGABE_PROZENT",
					(Integer) parameterWerbeabgabe.getCWertAsObject());
			ParametermandantDto parameterWerbeabgabeArtikel = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL,
							ParameterFac.PARAMETER_WERBEABGABE_ARTIKEL);
			mapParameter.put("P_WERBEABGABE_ARTIKEL",
					parameterWerbeabgabeArtikel.getCWert());

			if (kundeDto.getSHinweisextern() != null) {
				mapParameter.put("P_KUNDE_HINWEISEXTERN",
						kundeDto.getSHinweisextern());
			}
			mapParameter.put("P_BELEGKENNUNG", rechnungDto.getCNr());
			mapParameter.put("P_RECHNUNGSART", rechnungDto.getRechnungartCNr());

			RechnungartsprDto srpDto = getRechnungServiceFac()
					.rechnungartsprFindByPrimaryKeyOhneExc(
							rechnungDto.getRechnungartCNr(), locale);
			if (srpDto != null) {
				mapParameter.put("P_RECHNUNGSARTSPR", srpDto.getCBez());
			} else {
				mapParameter.put("P_RECHNUNGSARTSPR",
						rechnungDto.getRechnungartCNr());
			}

			// Kopftext
			String sKopftext = rechnungDto.getCKopftextuebersteuert();
			if (sKopftext == null || sKopftext.length() == 0) {

				if (rechnungDto.getRechnungartCNr().equals(
						RechnungFac.RECHNUNGART_GUTSCHRIFT)) {
					GutschrifttextDto oRechnungtextDto = getRechnungServiceFac()
							.gutschrifttextFindByMandantLocaleCNr(
									mandantDto.getCNr(),
									kundeDto.getPartnerDto()
											.getLocaleCNrKommunikation(),
									MediaFac.MEDIAART_KOPFTEXT);
					if (oRechnungtextDto != null) {
						sKopftext = oRechnungtextDto.getCTextinhalt();
					} else {
						sKopftext = "";
					}
				} else {
					RechnungtextDto oRechnungtextDto = getRechnungServiceFac()
							.rechnungtextFindByMandantLocaleCNr(
									mandantDto.getCNr(),
									kundeDto.getPartnerDto()
											.getLocaleCNrKommunikation(),
									MediaFac.MEDIAART_KOPFTEXT);
					if (oRechnungtextDto != null) {
						sKopftext = oRechnungtextDto.getCTextinhalt();
					} else {
						sKopftext = "";
					}
				}
			}
			mapParameter.put("P_KOPFTEXT",
					Helper.formatStyledTextForJasper(sKopftext));
			// Fusstext
			String sFusstext = rechnungDto.getCFusstextuebersteuert();
			if (sFusstext == null || sFusstext.length() == 0) {
				if (rechnungDto.getRechnungartCNr().equals(
						RechnungFac.RECHNUNGART_GUTSCHRIFT)) {
					GutschrifttextDto oRechnungtextDto = getRechnungServiceFac()
							.gutschrifttextFindByMandantLocaleCNr(
									mandantDto.getCNr(),
									kundeDto.getPartnerDto()
											.getLocaleCNrKommunikation(),
									MediaFac.MEDIAART_FUSSTEXT);
					if (oRechnungtextDto != null) {
						sFusstext = oRechnungtextDto.getCTextinhalt();
					} else {
						sFusstext = "";
					}
				} else {
					RechnungtextDto oRechnungtextDto = getRechnungServiceFac()
							.rechnungtextFindByMandantLocaleCNr(
									mandantDto.getCNr(),
									kundeDto.getPartnerDto()
											.getLocaleCNrKommunikation(),
									MediaFac.MEDIAART_FUSSTEXT);
					if (oRechnungtextDto != null) {
						sFusstext = oRechnungtextDto.getCTextinhalt();
					} else {
						sFusstext = "";
					}
				}

			}
			mapParameter.put("P_FUSSTEXT",
					Helper.formatStyledTextForJasper(sFusstext));
			// Kommunikationsdaten des Ansprechpartners
			// belegkommunikation: 1 die partnerIId des ansprechpartners brauch
			// ich
			Integer partnerIIdAnsprechpartner = null;
			if (oAnsprechpartner != null) {
				partnerIIdAnsprechpartner = oAnsprechpartner
						.getPartnerIIdAnsprechpartner();
			}
			// belegkommunikation: 2 daten holen
			String sEmail = getPartnerFac()
					.partnerkommFindRespectPartnerAsStringOhneExec(
							partnerIIdAnsprechpartner,
							kundeDto.getPartnerDto(),
							PartnerFac.KOMMUNIKATIONSART_EMAIL,
							theClientDto.getMandant(), theClientDto);
			String sFax = getPartnerFac()
					.partnerkommFindRespectPartnerAsStringOhneExec(
							partnerIIdAnsprechpartner,
							kundeDto.getPartnerDto(),
							PartnerFac.KOMMUNIKATIONSART_FAX,
							theClientDto.getMandant(), theClientDto);
			String sTelefon = getPartnerFac()
					.partnerkommFindRespectPartnerAsStringOhneExec(
							partnerIIdAnsprechpartner,
							kundeDto.getPartnerDto(),
							PartnerFac.KOMMUNIKATIONSART_TELEFON,
							theClientDto.getMandant(), theClientDto);
			// belegkommunikation: 3 daten als parameter an den Report
			// weitergeben
			mapParameter.put(LPReport.P_ANSPRECHPARTNEREMAIL,
					sEmail != null ? sEmail : "");
			mapParameter.put(LPReport.P_ANSPRECHPARTNERFAX, sFax != null ? sFax
					: "");
			mapParameter.put(LPReport.P_ANSPRECHPARTNERTELEFON,
					sTelefon != null ? sTelefon : "");
			// IG Lieferung / Drittland

			// SP3184
			// lt. WH kommt die Liefer-Laenderart aus der Lieferadresse der
			// ersten Lieferscheines und nicht aus der Auftrags-Lieferadresse

			Integer partnerIIdLieferadresse = kundeDto.getPartnerIId();
			if (rechnungDto.getLieferscheinIId() != null) {
				LieferscheinDto lsDto = getLieferscheinFac()
						.lieferscheinFindByPrimaryKey(
								rechnungDto.getLieferscheinIId());

				if (lsDto.getKundeIIdLieferadresse() != null) {
					partnerIIdLieferadresse = getKundeFac()
							.kundeFindByPrimaryKeySmall(
									lsDto.getKundeIIdLieferadresse())
							.getPartnerIId();
				}
			} else {
				if (hmLSDto.size() > 0) {
					Integer lieferscheinIId = hmLSDto.keySet().iterator()
							.next();
					LieferscheinDto lsDto = hmLSDto.get(lieferscheinIId);
					if (lsDto.getKundeIIdLieferadresse() != null) {
						partnerIIdLieferadresse = getKundeFac()
								.kundeFindByPrimaryKeySmall(
										lsDto.getKundeIIdLieferadresse())
								.getPartnerIId();
					}
				}
			}

			String laenderartCNr_Lieferadresse = getFinanzServiceFac()
					.getLaenderartZuPartner(partnerIIdLieferadresse,
							theClientDto);

			boolean bIGLieferung = false;
			boolean bWarenerklaerung = false;
			if (laenderartCNr_Lieferadresse != null) {
				if (laenderartCNr_Lieferadresse
						.equals(FinanzFac.LAENDERART_EU_AUSLAND_MIT_UID)) {
					// nur mit UID
					bIGLieferung = true;
				}
				if (laenderartCNr_Lieferadresse
						.equals(FinanzFac.LAENDERART_DRITTLAND)) {
					bWarenerklaerung = true;
				}
			}
			mapParameter.put("P_IGWARENVERKEHR", new Boolean(bIGLieferung));
			mapParameter
					.put("P_WARENERKLAERUNG", new Boolean(bWarenerklaerung));
			// Reverse Charge
			Boolean bReverseChargeInland = Boolean.FALSE;
			Boolean bReverseChargeAusland = Boolean.FALSE;
			if (bReverseCharge) {
				if (laenderartCNr_Lieferadresse != null) {
					if (laenderartCNr_Lieferadresse
							.equals(FinanzFac.LAENDERART_INLAND)) {
						bReverseChargeInland = Boolean.TRUE;
					} else {
						bReverseChargeAusland = Boolean.TRUE;
					}
				}
			}
			mapParameter.put("P_REVERSECHARGE_INLAND", bReverseChargeInland);
			mapParameter.put("P_REVERSECHARGE_AUSLAND", bReverseChargeAusland);
			// Kostenstelle der Rechnung
			if (rechnungDto.getKostenstelleIId() != null) {
				KostenstelleDto kostenstelleDto = getSystemFac()
						.kostenstelleFindByPrimaryKey(
								rechnungDto.getKostenstelleIId());
				mapParameter.put(LPReport.P_KOSTENSTELLE,
						kostenstelleDto.getCNr());
			}
			mapParameter.put(LPReport.P_LIEFERBEDINGUNGEN, Helper
					.formatStyledTextForJasper(getMediaFac()
							.mediastandardFindByCNrDatenformatCNrMandantCNr(
									MediaFac.MEDIASTANDARD_LIEFERBEDINGUNGEN,
									MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML,
									theClientDto.getMandant(),
									kundeDto.getPartnerDto()
											.getLocaleCNrKommunikation(),
									theClientDto).getOMediaText()));
			mapParameter.put(LPReport.P_EIGENTUMSVORBEHALT, Helper
					.formatStyledTextForJasper(getMediaFac()
							.mediastandardFindByCNrDatenformatCNrMandantCNr(
									MediaFac.MEDIASTANDARD_EIGENTUMSVORBEHALT,
									MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML,
									theClientDto.getMandant(),
									kundeDto.getPartnerDto()
											.getLocaleCNrKommunikation(),
									theClientDto).getOMediaText()));

			mapParameter.put("P_RABATTSPALTE_DRUCKEN", new Boolean(
					bDruckeRabatt));

			String sIhreBestellNummer = null;
			String sAuftragsnummer = null;
			if (rechnungDto.getAuftragIId() != null) {
				// wenn die AR einem Auftrag zugeordnet ist, kommen Auftrags-
				// und Kundenbestellnummer aus der AB
				AuftragDto auftragDto = getAuftragFac()
						.auftragFindByPrimaryKey(rechnungDto.getAuftragIId());
				sAuftragsnummer = auftragDto.getCNr();
				sIhreBestellNummer = auftragDto.getCBestellnummer();
				befuelleParameterLieferDaten(
						auftragDto.getKundeIIdLieferadresse(), mapParameter,
						mandantDto, locale, theClientDto);

				// AUFTRAGSEIGENSCHAFTEN
				Hashtable<?, ?> hAE = getAuftragReportFac()
						.getAuftragEigenschaften(rechnungDto.getAuftragIId(),
								theClientDto);
				if (hAE != null) {
					mapParameter.put(P_AUFTRAGEIGENSCHAFT_FA,
							hAE.get(AuftragReportFac.AUFTRAG_EIGENSCHAFTEN_FA));
					mapParameter
							.put(P_AUFTRAGEIGENSCHAFT_CLUSTER,
									hAE.get(AuftragReportFac.AUFTRAG_EIGENSCHAFTEN_CLUSTER));
					mapParameter.put(P_AUFTRAGEIGENSCHAFT_EQNR, hAE
							.get(AuftragReportFac.AUFTRAG_EIGENSCHAFTEN_EQNR));
				}
			} else if (rechnungDto.getLieferscheinIId() != null) {
				// AR ist einem LS zugeordnet
				LieferscheinDto lsDto = getLieferscheinFac()
						.lieferscheinFindByPrimaryKey(
								rechnungDto.getLieferscheinIId(), theClientDto);
				// dann kommt die Bestellnummer aus dem LS
				sIhreBestellNummer = lsDto.getCBestellnummer();
				mapParameter.put("P_AUSLIEFERDATUM", Helper.formatDatum(
						Helper.cutTimestamp(lsDto.getTLiefertermin()), locale));

				// SK 14572
				befuelleParameterLieferDaten(lsDto.getKundeIIdLieferadresse(),
						mapParameter, mandantDto, locale, theClientDto);

				if (lsDto.getLieferscheinartCNr().equals(
						LieferscheinFac.LSART_AUFTRAG)) {
					// Die Nummer des Hauptauftrags des LS wird gedruckt.
					if (lsDto.getAuftragIId() != null) {
						AuftragDto auftragDto = getAuftragFac()
								.auftragFindByPrimaryKey(lsDto.getAuftragIId());
						sAuftragsnummer = auftragDto.getCNr();
						// SK 14572
						// befuelleParameterLieferDaten(auftragDto.
						// getKundeIIdLieferadresse(),
						// mapParameter, mandantDto, locale, theClientDto);
						// befuelleParameterLieferDatenRechnungsadresse(auftragDto
						// .
						// getKundeIIdRechnungsadresse(),
						// mapParameter, mandantDto, locale, theClientDto);
						// AUFTRAGSEIGENSCHAFTEN
						Hashtable<?, ?> hAE = getAuftragReportFac()
								.getAuftragEigenschaften(lsDto.getAuftragIId(),
										theClientDto);
						if (hAE != null) {
							mapParameter
									.put(P_AUFTRAGEIGENSCHAFT_FA,
											hAE.get(AuftragReportFac.AUFTRAG_EIGENSCHAFTEN_FA));
							mapParameter
									.put(P_AUFTRAGEIGENSCHAFT_CLUSTER,
											hAE.get(AuftragReportFac.AUFTRAG_EIGENSCHAFTEN_CLUSTER));
							mapParameter
									.put(P_AUFTRAGEIGENSCHAFT_EQNR,
											hAE.get(AuftragReportFac.AUFTRAG_EIGENSCHAFTEN_EQNR));
						}
					}
				}
				// SK 14572
				// else {
				// // Freier Lieferschein, oder Lieferant Lieferschein
				// befuelleParameterLieferDaten(lsDto.getKundeIIdLieferadresse(),
				// mapParameter,
				// mandantDto, locale, theClientDto);
				// befuelleParameterLieferDatenRechnungsadresse(lsDto.
				// getKundeIIdRechnungsadresse(),
				// mapParameter,
				// mandantDto, locale, theClientDto);
				// }

			}
			befuelleParameterLieferDatenRechnungsadresse(
					rechnungDto.getKundeIId(), mapParameter, mandantDto,
					locale, theClientDto);

			if (rechnungDto.getKundeIIdStatistikadresse() != null) {
				if (!rechnungDto.getKundeIIdStatistikadresse().equals(
						kundeDto.getPartnerIIdRechnungsadresse())) {
					KundeDto statistikKunde = getKundeFac()
							.kundeFindByPrimaryKey(
									rechnungDto.getKundeIIdStatistikadresse(),
									theClientDto);
					AnsprechpartnerDto akunde = null;
					if (statistikKunde.getAnsprechpartnerDto() != null) {
						AnsprechpartnerDto[] anspkunde = statistikKunde
								.getAnsprechpartnerDto();
						akunde = anspkunde[0];
					}
					mapParameter.put(
							"P_STATISTIKADRESS",
							formatAdresseFuerAusdruck(
									statistikKunde.getPartnerDto(), akunde,
									mandantDto, locale));
					mapParameter.put("P_STATISTIKKUNDE_FREMDSYSTEMNR",
							statistikKunde.getCFremdsystemnr());
					mapParameter.put("P_STATISTIKKUNDE_LIEFERANTENNR",
							statistikKunde.getCLieferantennr());
					if (statistikKunde.getIidDebitorenkonto() != null) {
						KontoDto kontoDto = getFinanzFac()
								.kontoFindByPrimaryKey(
										statistikKunde.getIidDebitorenkonto());
						mapParameter.put("P_STATISTIKKUNDE_DEBITORENKONTO",
								kontoDto.getCNr());
					}
					mapParameter.put("P_STATISTIKKUNDE_TOUR",
							statistikKunde.getCTour());

					mapParameter.put("P_STATISTIKKUNDE_ILN", statistikKunde
							.getPartnerDto().getCIln());
				}
			}
			// falls die RE eine eigene bestellnummer hat, nehm ich diese
			if (rechnungDto.getCBestellnummer() != null) {
				sIhreBestellNummer = rechnungDto.getCBestellnummer();
			}
			mapParameter.put("P_IHRE_BESTELLNUMMER", sIhreBestellNummer);
			mapParameter.put("P_AUFTRAGSNUMMER", sAuftragsnummer);
			mapParameter.put("P_PROJEKT", rechnungDto.getCBez());

			if (rechnungDto.getProjektIId() != null) {
				ProjektDto pjDto = getProjektFac().projektFindByPrimaryKey(
						rechnungDto.getProjektIId());
				mapParameter.put("P_PROJEKTNUMMER", pjDto.getCNr());

				if (pjDto.getAnsprechpartnerIId() != null) {
					AnsprechpartnerDto oAnsprechpartnerProjekt = getAnsprechpartnerFac()
							.ansprechpartnerFindByPrimaryKey(
									pjDto.getAnsprechpartnerIId(), theClientDto);
					mapParameter
							.put("P_ANSPRECHPARTNER_PROJEKT_ADRESSBLOCK",
									getPartnerFac()
											.formatFixAnredeTitelName2Name1FuerAdresskopf(
													oAnsprechpartnerProjekt
															.getPartnerDto(),
													locale, null));
				}

			}

			// fuer Gutschrift
			if (rechnungDto.getRechnungIIdZurechnung() != null) {
				RechnungDto rechnungDtoZuRechnung = getRechnungFac()
						.rechnungFindByPrimaryKey(
								rechnungDto.getRechnungIIdZurechnung());
				mapParameter.put("P_RECHNUNGSNUMMER",
						rechnungDtoZuRechnung.getCNr());
			}
			// Zahlungsziel alt
			String sZahlungsziel = getMandantFac()
					.zahlungszielFindByIIdLocaleOhneExc(
							rechnungDto.getZahlungszielIId(), locale,
							theClientDto);
			mapParameter.put("P_ZAHLUNGSKONDITION", sZahlungsziel);
			// Lieferart
			String sLieferart = getLocaleFac().lieferartFindByIIdLocaleOhneExc(
					rechnungDto.getLieferartIId(), locale, theClientDto);
			mapParameter.put("P_LIEFERART", sLieferart);

			mapParameter.put("P_LIEFERART_ORT", rechnungDto.getCLieferartort());

			// Unser Zeichen
			PersonalDto oPersonalBenutzer = getPersonalFac()
					.personalFindByPrimaryKey(theClientDto.getIDPersonal(),
							theClientDto);
			PersonalDto oPersonalAnleger;
			// wenn Anleger und Ausdrucker identisch sind, erspar ich mir einen
			// DB-Zugriff.
			if (theClientDto.getIDPersonal().equals(
					rechnungDto.getPersonalIIdAnlegen())) {
				oPersonalAnleger = oPersonalBenutzer;
			} else {
				oPersonalAnleger = getPersonalFac().personalFindByPrimaryKey(
						rechnungDto.getPersonalIIdAnlegen(), theClientDto);
			}
			mapParameter.put("P_UNSER_ZEICHEN", Helper.getKurzzeichenkombi(
					oPersonalBenutzer.getCKurzzeichen(),
					oPersonalAnleger.getCKurzzeichen()));
			// Name des Anlegers
			mapParameter.put("P_ANLEGER_ANREDE", oPersonalAnleger
					.getPartnerDto().formatFixName2Name1());

			LagerDto lDto = getLagerFac().lagerFindByPrimaryKey(
					rechnungDto.getLagerIId());
			mapParameter.put("P_ABBUCHUNGSLAGER", lDto.getCNr());

			// Belegdatum
			mapParameter.put("P_BELEGDATUM",
					Helper.formatDatum(rechnungDto.getTBelegdatum(), locale));
			mapParameter.put("P_BELEGDATUM_TIMESTAMP",
					rechnungDto.getTBelegdatum());
			// Waehrung
			mapParameter.put(LPReport.P_WAEHRUNG, rechnungDto.getWaehrungCNr());
			BigDecimal dAllgemeinerRabatt;
			if (rechnungDto.getFAllgemeinerRabattsatz() != null) {
				dAllgemeinerRabatt = new BigDecimal(rechnungDto
						.getFAllgemeinerRabattsatz().doubleValue());
			} else {
				dAllgemeinerRabatt = new BigDecimal(0);
			}

			// aufschlag auf 2 Stellen runden (wegen float in der DB)
			BigDecimal bdAllgRabattProzent = Helper.rundeKaufmaennisch(
					dAllgemeinerRabatt, 2);
			dAllgemeinerRabatt = bdAllgRabattProzent;
			// und an den report weitergeben
			mapParameter.put("P_ALLGEMEINER_RABATT",
					dAllgemeinerRabatt.doubleValue());
			if (rechnungDto.getMwstsatzIId() != null) {
				MwstsatzDto mwstsatzDto = getMandantFac()
						.mwstsatzFindByPrimaryKey(rechnungDto.getMwstsatzIId(),
								theClientDto);
				mapParameter
						.put("P_MEHRWERTSTEUER", mwstsatzDto.getFMwstsatz());
			}
			mapParameter.put("P_MEHRWERTSTEUERBETRAG",
					rechnungDto.getNWertustfw());
			// Endbetrag
			BigDecimal bdEndbetrag;
			if (rechnungDto.getNWertfw() != null
					&& rechnungDto.getNWertustfw() != null) {
				bdEndbetrag = rechnungDto.getNWertfw().add(
						rechnungDto.getNWertustfw());
			}
			// bei stornierten muss ich das aus den positionen ausrechnen
			else {
				bdEndbetrag = new BigDecimal(0);
				for (int i = 0; i < data.length; i++) {
					if (data[i][RECHNUNG_FELD_GESAMTPREIS] != null) {
						bdEndbetrag = bdEndbetrag
								.add((BigDecimal) data[i][RECHNUNG_FELD_GESAMTPREIS]);
					}
					if (data[i][RECHNUNG_FELD_MWSTBETRAG] != null) {
						bdEndbetrag = bdEndbetrag
								.add((BigDecimal) data[i][RECHNUNG_FELD_MWSTBETRAG]);
					}
				}
			}

			Object sMwstsatz[] = null;
			sMwstsatz = getBelegVerkaufFac().getMwstTabelle(mwstMap,
					rechnungDto, locale, theClientDto);

			mapParameter.put("P_MWST_TABELLE_LINKS",
					sMwstsatz[LPReport.MWST_TABELLE_LINKS]);
			mapParameter.put("P_MWST_TABELLE_RECHTS",
					sMwstsatz[LPReport.MWST_TABELLE_RECHTS]);
			mapParameter.put("P_MWST_TABELLE_BETRAG",
					sMwstsatz[LPReport.MWST_TABELLE_SUMME_POSITIONEN]);
			mapParameter.put("P_MWST_TABELLE_WAEHRUNG",
					sMwstsatz[LPReport.MWST_MWST_TABELLE_WAEHRUNG]);

			if (bReverseCharge)
				bdEndbetrag = rechnungDto.getNWertfw();
			else
				bdEndbetrag = (BigDecimal) sMwstsatz[LPReport.MWST_ENDBETRAGMITMWST];
			mapParameter.put("P_RECHNUNGSENDBETRAG", bdEndbetrag);
			// Zahlungsziel Detail andrucken
			ZahlungszielDto zahlungszielDto = null;
			if (rechnungDto.getZahlungszielIId() != null) {
				zahlungszielDto = getMandantFac().zahlungszielFindByPrimaryKey(
						rechnungDto.getZahlungszielIId(), theClientDto);
			}
			// Zahlungsziel ist bei Gutschrift nicht vorhanden
			if (zahlungszielDto != null) {
				java.sql.Date dBelegdatum = new java.sql.Date(rechnungDto
						.getTBelegdatum().getTime());
				Integer iSkontoAnzahlTage1 = zahlungszielDto
						.getSkontoAnzahlTage1();
				Integer iSkontoAnzahlTage2 = zahlungszielDto
						.getSkontoAnzahlTage2();

				java.sql.Date dSkonto1Datum = null;
				java.sql.Date dSkonto2Datum = null;
				if (iSkontoAnzahlTage1 != null && iSkontoAnzahlTage1 != 0) {
					dSkonto1Datum = getMandantFac()
							.berechneSkontoTage1FuerBelegdatum(dBelegdatum,
									rechnungDto.getZahlungszielIId(),
									theClientDto);
					mapParameter.put("P_SKONTO1ANZAHLTAGE", Helper
							.formatStyledTextForJasper(iSkontoAnzahlTage1
									.toString()));
					mapParameter.put("P_SKONTO1DATUM", new java.util.Date(
							dSkonto1Datum.getTime()));
				}
				if (iSkontoAnzahlTage2 != null && iSkontoAnzahlTage2 != 0) {
					dSkonto2Datum = getMandantFac()
							.berechneSkontoTage2Belegdatum(dBelegdatum,
									rechnungDto.getZahlungszielIId(),
									theClientDto);
					mapParameter.put("P_SKONTO2ANZAHLTAGE", Helper
							.formatStyledTextForJasper(iSkontoAnzahlTage2
									.toString()));
					mapParameter.put("P_SKONTO2DATUM", new java.util.Date(
							dSkonto2Datum.getTime()));
				}
				BigDecimal bdSkontoProzentsatz1 = zahlungszielDto
						.getSkontoProzentsatz1();
				BigDecimal bdSkontoProzentsatz2 = zahlungszielDto
						.getSkontoProzentsatz2();

				BigDecimal bdBetragNurSkonto1 = null;
				BigDecimal bdBetragNurSkonto2 = null;
				if (bdEndbetrag != null && bdSkontoProzentsatz1 != null
						&& bdSkontoProzentsatz1.intValue() != 0) {
					mapParameter.put("P_SKONTO1PROZENTSATZ",
							bdSkontoProzentsatz1);
					bdBetragNurSkonto1 = Helper.rundeKaufmaennisch(bdEndbetrag
							.multiply(bdSkontoProzentsatz1
									.divide(new BigDecimal(100))), 2);

				}
				if (bdEndbetrag != null && bdSkontoProzentsatz2 != null
						&& bdSkontoProzentsatz2.intValue() != 0) {
					mapParameter.put("P_SKONTO2PROZENTSATZ",
							bdSkontoProzentsatz2);
					bdBetragNurSkonto2 = Helper.rundeKaufmaennisch(bdEndbetrag
							.multiply(bdSkontoProzentsatz2
									.divide(new BigDecimal(100))), 2);
				}

				mapParameter.put("P_BETRAGNURSKONTO1", bdBetragNurSkonto1);
				mapParameter.put("P_BETRAGNURSKONTO2", bdBetragNurSkonto2);

				java.sql.Date dZielDatumNetto = getMandantFac()
						.berechneZielDatumFuerBelegdatum(dBelegdatum,
								zahlungszielDto, theClientDto);

				if (dZielDatumNetto != null) {
					mapParameter.put("P_ZIELDATUMNETTO", new java.util.Date(
							dZielDatumNetto.getTime()));
				}
			}
			// Zahlungsziel Detail andrucken ENDE

			StringBuffer buff = new StringBuffer();
			// Anrede des Mandanten
			String sMandantAnrede = mandantDto.getPartnerDto()
					.formatFixName1Name2();
			if (sMandantAnrede != null) {
				mapParameter.put(P_MANDANT_ANREDE_UND_NAME, sMandantAnrede);
				buff.append(sMandantAnrede).append("\n\n");
			}
			// Vertreter darf nicht null sein.
			PersonalDto vertreterDto = null;
			if (rechnungDto.getPersonalIIdVertreter() != null) {
				vertreterDto = getPersonalFac().personalFindByPrimaryKey(
						rechnungDto.getPersonalIIdVertreter(), theClientDto);
			}
			if (vertreterDto != null) {
				mapParameter.put(P_VERTRETER, vertreterDto.formatAnrede());

				mapParameter.put(P_VERTRETER_UNTERSCHRIFTSFUNKTION_UND_NAME,
						vertreterDto.formatFixUFTitelName2Name1());
				buff.append(vertreterDto.formatFixUFTitelName2Name1());
				if (vertreterDto.getCUnterschriftstext() != null
						&& vertreterDto.getCUnterschriftstext().length() > 0) {
					String sUnterschriftstext = vertreterDto
							.getCUnterschriftstext();
					mapParameter.put(P_VERTRETER_UNTERSCHRIFTSTEXT,
							sUnterschriftstext);
					buff.append("\n").append(sUnterschriftstext);
				}
				mapParameter.put("P_VERTRETER_ANREDE", vertreterDto
						.getPartnerDto().formatFixName2Name1());

				// Vertreter Kontaktdaten
				String sVertreterEmail = vertreterDto.getCEmail();

				String sVertreterFaxDirekt = vertreterDto.getCDirektfax();

				String sVertreterFax = vertreterDto.getCFax();

				String sVertreterTelefon = vertreterDto.getCTelefon();
				mapParameter.put(LPReport.P_VERTRETEREMAIL,
						sVertreterEmail != null ? sVertreterEmail : "");
				if (sVertreterFaxDirekt != null && sVertreterFaxDirekt != "") {
					mapParameter.put(LPReport.P_VERTRETERFAX,
							sVertreterFaxDirekt);
				} else {
					mapParameter.put(LPReport.P_VERTRETERFAX,
							sVertreterFax != null ? sVertreterFax : "");
				}
				mapParameter.put(LPReport.P_VERTRETEERTELEFON,
						sVertreterTelefon != null ? sVertreterTelefon : "");

			}

			String sSpediteur = null;
			if (rechnungDto.getSpediteurIId() != null) {

				SpediteurDto spediteurDto = getMandantFac()
						.spediteurFindByPrimaryKey(
								rechnungDto.getSpediteurIId());
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
									locale));
				}

				sSpediteur = spediteurDto.getCNamedesspediteurs();

			}
			mapParameter.put("P_SPEDITEUR", sSpediteur != null ? sSpediteur
					: "");

			String cBriefanrede = "";

			if (oAnsprechpartner != null) {
				cBriefanrede = getPartnerServicesFac().getBriefanredeFuerBeleg(
						rechnungDto.getAnsprechpartnerIId(),
						kundeDto.getPartnerIId(), locale, theClientDto);
			} else {
				// neutrale Anrede
				cBriefanrede = getBriefanredeNeutralOderPrivatperson(
						kundeDto.getPartnerIId(), locale, theClientDto);
			}

			mapParameter.put("P_BRIEFANREDE", cBriefanrede);

			// Storno ?
			if (rechnungDto.getStatusCNr().equals(RechnungFac.STATUS_STORNIERT)) {
				mapParameter.put("P_STORNIERT", new Boolean(true));
			} else {
				mapParameter.put("P_STORNIERT", new Boolean(false));
			}
			// belegkopien: 2 Die Anzahl der Exemplare ist 1 + Anzahl der Kopien
			int iExemplare;
			if (iAnzahlKopien == null || iAnzahlKopien.intValue() <= 0) {
				iExemplare = 1;
			} else {
				iExemplare = 1 + iAnzahlKopien.intValue();
			}
			// Wenns eine Schlussrechnung ist, dann muessen die Anzahlungen noch
			// mitgedruckt werden
			if (rechnungDto.getRechnungartCNr().equals(
					RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG)) {
				// Anzahlungsrechnungen suchen
				RechnungDto[] anzRechnungen = getRechnungFac()
						.rechnungFindByAuftragIId(rechnungDto.getAuftragIId());

				BigDecimal bdEndbetrag2 = bdEndbetrag;
				StringBuffer sAnz = new StringBuffer();
				StringBuffer sAnzDetails = new StringBuffer();

				ArrayList<Object[]> alSuData = new ArrayList<Object[]>();

				String[] fieldnames = new String[] { "F_RECHNUNGSNUMMER",
						"F_WERTBRUTTO", "F_WERT_UST", "F_BEZAHLTBRUTTO",
						"F_BEZAHLT_UST" };

				for (int i = 0; i < anzRechnungen.length; i++) {
					if (anzRechnungen[i].getRechnungartCNr().equals(
							RechnungFac.RECHNUNGART_ANZAHLUNG)) {
						if (anzRechnungen[i].getNWertfw() != null
								&& anzRechnungen[i].getNWertustfw() != null) {

							bdEndbetrag2 = befuelleAnzahlungsdetail(
									rechnungDto, bdEndbetrag2,
									mandantDto.getCNr(), sAnz, false,
									sAnzDetails, anzRechnungen[i], locale,
									fieldnames, alSuData);

							// PJ18843 wenn eine Anzahlung gutgeschrieben worden
							// ist, dann dies wieder gegenrechnen

							RechnungDto[] aGutschriftenDtos = null;
							javax.persistence.Query query = em
									.createNamedQuery("RechnungfindByRechnungIIdZuRechnung");
							query.setParameter(1, anzRechnungen[i].getIId());

							Collection<?> cl = query.getResultList();
							aGutschriftenDtos = RechnungDtoAssembler
									.createDtos(cl);

							for (int k = 0; k < aGutschriftenDtos.length; k++) {

								bdEndbetrag2 = befuelleAnzahlungsdetail(
										rechnungDto, bdEndbetrag2,
										mandantDto.getCNr(), sAnz, true,
										sAnzDetails, aGutschriftenDtos[k],
										locale, fieldnames, alSuData);

							}

						}
					}
				}
				mapParameter.put("P_RECHNUNGSENDBETRAG2", bdEndbetrag2);
				mapParameter.put("P_ANZAHLUNGEN", sAnz.toString());
				mapParameter
						.put("P_ANZAHLUNGENDETAILS", sAnzDetails.toString());

				Object[][] oSubData = new Object[alSuData.size()][fieldnames.length];
				oSubData = alSuData.toArray(oSubData);

				mapParameter.put("P_SUBREPORT_ANZAHLUNGEN",
						new LPDatenSubreport(oSubData, fieldnames));

			}
			JasperPrintLP[] prints = new JasperPrintLP[iExemplare];
			// belegkopien: 3 Print-Array in der Schleife befuellen
			for (int iKopieNummer = 0; iKopieNummer < iExemplare; iKopieNummer++) {
				// belegkopien: 4 jede Kopie erhaelt seine Kopiennummer
				// die "0." Kopie ist das Original und kriegt deshalb keine
				if (iKopieNummer > 0) {
					mapParameter.put(LPReport.P_KOPIE_NUMMER, new Integer(
							iKopieNummer));
				}
				if (FinanzReportFac.REPORT_MAHNUNG_AUSFUEHRLICH
						.equals(sReportName)) {
					mapParameter.put("P_MAHNSTUFE", iMahnstufe);
				}
				// Index zuruecksetzen
				index = -1;
				initJRDS(mapParameter, sReportModul, sReportName,
						theClientDto.getMandant(), locale, theClientDto,
						bMitLogo.booleanValue(),
						rechnungDto.getKostenstelleIId());
				prints[iKopieNummer] = getReportPrint();
			}
			int useCaseId = 0;
			int iId = rechnungDto.getIId();
			if (sReportName.equals(RechnungReportFac.REPORT_GUTSCHRIFT))
				useCaseId = QueryParameters.UC_ID_GUTSCHRIFT;
			else if (sReportName.equals(RechnungReportFac.REPORT_RECHNUNG))
				useCaseId = QueryParameters.UC_ID_RECHNUNG;
			else if (sReportName
					.equals(RechnungReportFac.REPORT_PROFORMARECHNUNG))
				useCaseId = QueryParameters.UC_ID_PROFORMARECHNUNG;
			else if (sReportName
					.equals(FinanzReportFac.REPORT_MAHNUNG_AUSFUEHRLICH)) {
				useCaseId = QueryParameters.UC_ID_RECHNUNG;
			}

			PrintInfoDto values = getJCRDocFac().getPathAndPartnerAndTable(iId,
					useCaseId, theClientDto);
			prints[0].setOInfoForArchive(values);
			prints[0].putAdditionalInformation(JasperPrintLP.KEY_BELEGART,
					LocaleFac.BELEGART_RECHNUNG);
			prints[0].putAdditionalInformation(JasperPrintLP.KEY_BELEGIID,
					rechnungDto.getIId());
			return prints;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	private BigDecimal befuelleAnzahlungsdetail(RechnungDto rechnungDto,
			BigDecimal bdEndbetrag, String mandantCNr, StringBuffer sAnz,
			boolean bGutschrift, StringBuffer sAnzDetails,
			RechnungDto reDtoAnzahlung, Locale locale, String[] fieldnames,
			ArrayList<Object[]> alSuData) {

		if (reDtoAnzahlung.getNWertfw() != null
				&& reDtoAnzahlung.getNWertustfw() != null) {

			Object[] oSubZeile = new Object[fieldnames.length];

			BigDecimal bdBrutto = reDtoAnzahlung.getNWertfw().add(
					reDtoAnzahlung.getNWertustfw());

			if (bGutschrift == true) {
				bdBrutto = bdBrutto.negate();
			}

			oSubZeile[0] = reDtoAnzahlung.getCNr();
			oSubZeile[1] = bdBrutto;
			oSubZeile[2] = reDtoAnzahlung.getNWertustfw();

			BigDecimal bezahltUst = getRechnungFac()
					.getBereitsBezahltWertVonRechnungUstFw(
							reDtoAnzahlung.getIId(), null,
							rechnungDto.getTBelegdatum());

			oSubZeile[3] = getRechnungFac()
					.getBereitsBezahltWertVonRechnungFw(
							reDtoAnzahlung.getIId(), null,
							rechnungDto.getTBelegdatum()).add(bezahltUst);
			oSubZeile[4] = bezahltUst;

			bdEndbetrag = bdEndbetrag.subtract(bdBrutto);
			// umbrechen, wenn schon was drinsteht
			if (sAnz.length() > 0) {
				sAnz.append("\n");
				sAnzDetails.append("\n");
			}
			sAnz.append(Helper.formatZahl(bdBrutto, FinanzFac.NACHKOMMASTELLEN,
					locale));

			if (bGutschrift == true) {
				sAnzDetails.append(getTextRespectUISpr("lp.kuerzel.gutschrift",
						mandantCNr, locale));
			} else {
				sAnzDetails.append(getTextRespectUISpr("lp.kuerzel.rechnung",
						mandantCNr, locale));
			}

			sAnzDetails.append(" " + reDtoAnzahlung.getCNr() + ", ");
			sAnzDetails.append(reDtoAnzahlung.getWaehrungCNr()
					+ " "
					+ Helper.formatZahl(bdBrutto, FinanzFac.NACHKOMMASTELLEN,
							locale));
			sAnzDetails.append(", "
					+ getTextRespectUISpr("lp.enthaltenemwst", mandantCNr,
							locale));
			sAnzDetails.append(" "
					+ Helper.formatZahl(reDtoAnzahlung.getNWertustfw(),
							FinanzFac.NACHKOMMASTELLEN, locale));
			sAnzDetails.append("   " + reDtoAnzahlung.getWaehrungCNr());

			alSuData.add(oSubZeile);

		}

		return bdEndbetrag;

	}

	/**
	 * Befuelle Mit Kundendaten folgende Lieferparameter P_LIEFERADRESSE,
	 * P_FREMDSYSTEMNR,P_LIEFERANTENNR,P_DEBITORENKONTO,P_TOUR,P_KUNDE_ILN,
	 * P_LIEFERKUNDE_FIRMENBUCHNUMMER,P_LIEFERKUNDE_KUNDENNUMMER
	 * 
	 * @param kundeIID
	 *            Integer
	 * @param locale
	 *            Locale
	 * @param theClientDto
	 *            String
	 * @param mandantDto
	 *            MandantDto
	 * @param mapParameter
	 *            Map
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 */
	private void befuelleParameterLieferDaten(Integer kundeIID,
			Map<String, Object> mapParameter, MandantDto mandantDto,
			Locale locale, TheClientDto theClientDto) throws RemoteException,
			EJBExceptionLP {
		KundeDto lieferKunde = getKundeFac().kundeFindByPrimaryKey(kundeIID,
				theClientDto);
		AnsprechpartnerDto akunde = null;
		if (lieferKunde.getAnsprechpartnerDto() != null) {
			AnsprechpartnerDto[] anspkunde = lieferKunde
					.getAnsprechpartnerDto();
			akunde = anspkunde[0];
		}
		mapParameter.put(
				"P_LIEFERADRESSE",
				formatAdresseFuerAusdruck(lieferKunde.getPartnerDto(), akunde,
						mandantDto, locale));

		mapParameter.put("P_FREMDSYSTEMNR", lieferKunde.getCFremdsystemnr());
		// Achtung falsch benannt eigentlich P_LIEFER_ILN
		mapParameter.put("P_KUNDE_ILN", lieferKunde.getPartnerDto().getCIln());

		if (lieferKunde.getIidDebitorenkonto() != null) {
			KontoDto kontoDto = getFinanzFac().kontoFindByPrimaryKey(
					lieferKunde.getIidDebitorenkonto());
			mapParameter.put("P_DEBITORENKONTO", kontoDto.getCNr());
		}
		mapParameter.put("P_TOUR", lieferKunde.getCTour());
		mapParameter.put("P_LIEFERKUNDE_FIRMENBUCHNUMMER", lieferKunde
				.getPartnerDto().getCFirmenbuchnr());
		mapParameter.put("P_LIEFERKUNDE_KUNDENNUMMER",
				lieferKunde.getIKundennummer());
	}

	private Integer getGesamtAnzahlAnAuftraegen(RechnungDto rechnungDto) {

		HashMap hmAuftraege = new HashMap();

		if (rechnungDto.getAuftragIId() != null) {
			hmAuftraege.put(rechnungDto.getAuftragIId(), "");
		}

		// Auftrage aus RE-Positionen
		Session session = FLRSessionFactory.getFactory().openSession();
		String sQuery = "SELECT distinct auftragpos.auftrag_i_id FROM FLRRechnungPosition rp LEFT OUTER JOIN rp.flrpositionensichtauftrag AS auftragpos WHERE rp.flrrechnung.i_id="
				+ rechnungDto.getIId() + " ";

		Query querylagerbewegungen = session.createQuery(sQuery);
		List<?> results = querylagerbewegungen.list();
		Iterator<?> resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {
			Integer auftragIId = (Integer) resultListIterator.next();
			if (auftragIId != null && !hmAuftraege.containsKey(auftragIId)) {
				hmAuftraege.put(auftragIId, "");

			}
		}
		session.close();

		// Auftrage aus LS-Positionen
		session = FLRSessionFactory.getFactory().openSession();
		sQuery = "SELECT distinct aufpos.auftrag_i_id FROM FLRRechnungPosition rp LEFT OUTER JOIN rp.flrlieferschein AS ls LEFT OUTER JOIN ls.flrlieferscheinpositionen AS lspos LEFT OUTER JOIN  lspos.flrpositionensichtauftrag AS aufpos WHERE rp.flrrechnung.i_id="
				+ rechnungDto.getIId() + " ";

		querylagerbewegungen = session.createQuery(sQuery);
		results = querylagerbewegungen.list();
		resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {
			Integer auftragIId = (Integer) resultListIterator.next();
			if (auftragIId != null && !hmAuftraege.containsKey(auftragIId)) {
				hmAuftraege.put(auftragIId, "");
			}
		}

		session.close();

		return hmAuftraege.size();
	}

	/**
	 * Befuelle Mit Kundendaten folgende Lieferparameter
	 * P_RECHNUNG_KUNDE_LIEFERADRESSE,
	 * P_RECHNUNG_KUNDE_FREMDSYSTEMNR,P_RECHNUNG_KUNDE_LIEFERANTENNR,
	 * P_RECHNUNG_KUNDE_DEBITORENKONTO
	 * ,P_RECHNUNG_KUNDE_TOUR,P_RECHNUNG_KUNDE_ILN,
	 * P_RECHNUNG_KUNDE_FIRMENBUCHNUMMER,P_RECHNUNG_KUNDE_KUNDENNUMMER
	 * 
	 * @param kundeIID
	 *            Integer
	 * @param locale
	 *            Locale
	 * @param theClientDto
	 *            String
	 * @param mandantDto
	 *            MandantDto
	 * @param mapParameter
	 *            Map
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 */
	private void befuelleParameterLieferDatenRechnungsadresse(Integer kundeIID,
			Map<String, Object> mapParameter, MandantDto mandantDto,
			Locale locale, TheClientDto theClientDto) throws RemoteException,
			EJBExceptionLP {
		KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeIID,
				theClientDto);
		AnsprechpartnerDto akunde = null;
		if (kundeDto.getAnsprechpartnerDto() != null) {
			AnsprechpartnerDto[] anspkunde = kundeDto.getAnsprechpartnerDto();
			akunde = anspkunde[0];
		}
		mapParameter.put(
				"P_RECHNUNG_KUNDE_LIEFERADRESSE",
				formatAdresseFuerAusdruck(kundeDto.getPartnerDto(), akunde,
						mandantDto, locale));

		mapParameter.put("P_RECHNUNG_KUNDE_FREMDSYSTEMNR",
				kundeDto.getCFremdsystemnr());
		mapParameter.put("P_RECHNUNG_KUNDE_ILN", kundeDto.getPartnerDto()
				.getCIln());
		mapParameter.put("P_RECHNUNG_KUNDE_LIEFERANTENNR",
				kundeDto.getCLieferantennr());
		if (kundeDto.getIidDebitorenkonto() != null) {
			KontoDto kontoDto = getFinanzFac().kontoFindByPrimaryKey(
					kundeDto.getIidDebitorenkonto());
			mapParameter.put("P_RECHNUNG_KUNDE_DEBITORENKONTO",
					kontoDto.getCNr());
		}
		mapParameter.put("P_RECHNUNG_KUNDE_TOUR", kundeDto.getCTour());
		mapParameter.put("P_RECHNUNG_KUNDE_FIRMENBUCHNUMMER", kundeDto
				.getPartnerDto().getCFirmenbuchnr());
		mapParameter.put("P_RECHNUNG_KUNDE_KUNDENNUMMER",
				kundeDto.getIKundennummer());
	}

	public JasperPrintLP printRechnungenOffene(
			ReportRechnungJournalKriterienDto krit, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Session session = null;
		try {
			Map<String, Object> mapParameter = new TreeMap<String, Object>();

			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);

			this.useCase = UC_REPORT_RECHNUNGEN_OFFENE;
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			// Kunde oder Statistikadresse verwenden
			final String critKundeIId;
			final String critFLRKunde;
			if (krit.getBVerwendeStatistikAdresse()) {
				critKundeIId = RechnungFac.FLR_RECHNUNG_KUNDE_I_ID_STATISTIKADRESSE;
				critFLRKunde = RechnungFac.FLR_RECHNUNG_FLRSTATISTIKADRESSE;
			} else {
				critKundeIId = RechnungFac.FLR_RECHNUNG_KUNDE_I_ID;
				critFLRKunde = RechnungFac.FLR_RECHNUNG_FLRKUNDE;
			}

			List<Integer> kundenIIds = new ArrayList<Integer>();

			if (krit.kundeIId != null) {
				kundenIIds.add(krit.kundeIId);
			} else if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				Iterator<?> iter = session
						.createCriteria(FLRKunde.class)
						.createAlias("flrpartner", "p")
						.addOrder(Order.asc("p.c_name1nachnamefirmazeile1"))
						.add(Restrictions.eq("mandant_c_nr",
								theClientDto.getMandant())).list().iterator();
				while (iter.hasNext())
					kundenIIds.add(((FLRKunde) iter.next()).getI_id());
			} else {
				kundenIIds.add(null);
			}

			List<Object[]> dataList = new ArrayList<Object[]>();

			Set<Integer> gedruckteKonten = new HashSet<Integer>();

			for (Integer kndIId : kundenIIds) {

				Criteria c = session.createCriteria(FLRRechnungReport.class);
				// Filter nach Mandant
				c.add(Restrictions.eq(RechnungFac.FLR_RECHNUNG_MANDANT_C_NR,
						theClientDto.getMandant()));
				// Rechnungstypen
				Collection<String> cRechnungstyp = new LinkedList<String>();
				cRechnungstyp.add(RechnungFac.RECHNUNGTYP_RECHNUNG);
				if (krit.getBGutschriftenBeruecksichtigen()) {
					cRechnungstyp.add(RechnungFac.RECHNUNGTYP_GUTSCHRIFT);
				}
				c.createCriteria(RechnungFac.FLR_RECHNUNG_FLRRECHNUNGART).add(
						Restrictions.in(
								RechnungFac.FLR_RECHNUNGART_RECHNUNGTYP_C_NR,
								cRechnungstyp));
				// Kostenstelle
				if (krit.kostenstelleIId != null) {
					c.add(Restrictions.eq(
							RechnungFac.FLR_RECHNUNG_KOSTENSTELLE_I_ID,
							krit.kostenstelleIId));
				}
				if (kndIId != null) {
					c.add(Restrictions.eq(critKundeIId, kndIId));
					KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
							kndIId, theClientDto);
					mapParameter.put("P_KUNDE", kundeDto.getPartnerDto()
							.formatTitelAnrede());
				}
				// Filter nach einem Vertrter
				if (krit.vertreterIId != null) {
					c.add(Restrictions.eq(RechnungFac.FLR_RECHNUNG_FLRVERTRETER
							+ ".i_id", krit.vertreterIId));
				}
				String sVon = null;
				String sBis = null;
				if (krit.dVon != null) {
					c.add(Restrictions.ge(
							RechnungFac.FLR_RECHNUNG_D_BELEGDATUM, krit.dVon));
					sVon = Helper.formatDatum(krit.dVon,
							theClientDto.getLocUi());
				}
				if (krit.dBis != null) {
					c.add(Restrictions.le(
							RechnungFac.FLR_RECHNUNG_D_BELEGDATUM, krit.dBis));
					sBis = Helper.formatDatum(krit.dBis,
							theClientDto.getLocUi());
				}
				LpBelegnummerFormat f = getBelegnummerGeneratorObj()
						.getBelegnummernFormat(theClientDto.getMandant());
				Integer iGeschaeftsjahr = getParameterFac().getGeschaeftsjahr(
						theClientDto.getMandant());
				String sMandantKuerzel = getParameterFac().getMandantparameter(
						theClientDto.getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN,
						ParameterFac.PARAMETER_BELEGNUMMER_MANDANTKENNUNG)
						.getCWert();
				if (krit.sBelegnummerVon != null) {
					sVon = HelperServer
							.getBelegnummernFilterForHibernateCriterias(f,
									iGeschaeftsjahr, sMandantKuerzel,
									krit.sBelegnummerVon);
					c.add(Restrictions.ge(RechnungFac.FLR_RECHNUNG_C_NR, sVon));
				}
				if (krit.sBelegnummerBis != null) {
					sBis = HelperServer
							.getBelegnummernFilterForHibernateCriterias(f,
									iGeschaeftsjahr, sMandantKuerzel,
									krit.sBelegnummerBis);
					c.add(Restrictions.le(RechnungFac.FLR_RECHNUNG_C_NR, sBis));
				}
				// Alle Stati ausser Angelegt, Bezahlt, Storniert
				Collection<String> cStati = new LinkedList<String>();
				cStati.add(RechnungFac.STATUS_ANGELEGT);
				cStati.add(RechnungFac.STATUS_STORNIERT);

				c.add(Restrictions.not(Restrictions.in(
						RechnungFac.FLR_RECHNUNG_STATUS_C_NR, cStati)));

				c.add(Restrictions.or(
						Restrictions.gt("t_bezahltdatum", krit.getTStichtag()),
						Restrictions.isNull("t_bezahltdatum")));

				boolean mitNichtZugeordnetenBelegen = false;
				// Sortierung nach Kostenstelle
				if (krit.bSortiereNachKostenstelle) {
					c.createCriteria(RechnungFac.FLR_RECHNUNG_FLRKOSTENSTELLE)
							.addOrder(Order.asc("c_nr"));
				}
				// Sortierung nach Kunde
				if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
					c.createCriteria(critFLRKunde)
							.createCriteria(KundeFac.FLR_PARTNER)
							.addOrder(
									Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));
					c.addOrder(Order.asc(RechnungFac.FLR_RECHNUNG_C_NR));
					mitNichtZugeordnetenBelegen = krit
							.getBMitNichtZugeordnetendBelegen();
				}
				// Sortierung nach Belegnummer
				else if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_BELEGNUMMER) {
					c.addOrder(Order.asc(RechnungFac.FLR_RECHNUNG_C_NR));
				} else if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_VERTRETER) {
					c.createCriteria(RechnungFac.FLR_RECHNUNG_FLRVERTRETER)
							.addOrder(
									Order.asc(PersonalFac.FLR_PERSONAL_C_KURZZEICHEN));
				} else if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_FAELLIGKEIT) {
					c.addOrder(
							Order.asc(RechnungFac.FLR_RECHNUNG_T_FAELLIGKEIT))
							.addOrder(Order.asc(RechnungFac.FLR_RECHNUNG_C_NR));
				}
				// stichtag ab belegdatum
				c.add(Restrictions.le(RechnungFac.FLR_RECHNUNG_D_BELEGDATUM,
						krit.getTStichtag()));
				List<?> firstResultList = c.list();
				Iterator<?> firstResultListIterator = firstResultList
						.iterator();
				List<FLRRechnungReport> resultList = new LinkedList<FLRRechnungReport>();
				while (firstResultListIterator.hasNext()) {
					FLRRechnungReport re = (FLRRechnungReport) firstResultListIterator
							.next();
					// stichtag bereits bezahlt?
					if (re.getT_bezahltdatum() == null
							|| !krit.getTStichtag().after(
									re.getT_bezahltdatum())) {
						resultList.add(re);
					}
				}
				Iterator<?> resultListIterator = resultList.iterator();
				int row = 0;
				Object[][] tempData = new Object[resultList.size()][OFFENE_ANZAHL_SPALTEN];

				KundeDto kundeDto = null;
				if (kndIId != null)
					kundeDto = getKundeFac().kundeFindByPrimaryKey(kndIId,
							theClientDto);

				if (mitNichtZugeordnetenBelegen
						&& kundeDto.getIidDebitorenkonto() != null
						&& !gedruckteKonten.contains(kundeDto
								.getIidDebitorenkonto())) {

					gedruckteKonten.add(kundeDto.getIidDebitorenkonto());
					// TODO: nur FLRFinanzBuchungDetail holen
					Query query = session
							.createQuery("SELECT buchungdetail from FLRFinanzBuchungDetail buchungdetail LEFT OUTER JOIN buchungdetail.flrbuchung AS buchung"
									+ " WHERE"
									+ BuchungDetailQueryBuilder
											.buildNurOffeneBuchungDetails(
													"buchungdetail",
													new Timestamp(krit
															.getTStichtag()
															.getTime()))
									+ "AND"
									+ BuchungDetailQueryBuilder
											.buildNichtZuordenbareVonKonto(
													"buchungdetail",
													"buchung",
													kundeDto.getIidDebitorenkonto())
									+ (krit.getTStichtag() == null ? ""
											: (" AND buchung.d_buchungsdatum<='"
													+ Helper.formatDateWithSlashes(krit
															.getTStichtag()) + "' "))
									+ "AND buchung.geschaeftsjahr_i_geschaeftsjahr="
									+ getBuchenFac()
											.findGeschaeftsjahrFuerDatum(
													krit.getTStichtag(),
													theClientDto.getMandant()));

					@SuppressWarnings("unchecked")
					List<FLRFinanzBuchungDetail> bdList = query.list();
					if (bdList.size() > 0) {
						if (tempData.length < 1) {
							tempData = new Object[1][OFFENE_ANZAHL_SPALTEN];
							String sFirma = kundeDto.getPartnerDto()
									.getCName1nachnamefirmazeile1();
							tempData[row][OFFENE_FELD_FIRMA] = sFirma;
							tempData[row][OFFENE_FELD_DEBITORENNR] = kundeDto
									.getIidDebitorenkonto() != null ? getFinanzFac()
									.kontoFindByPrimaryKey(
											kundeDto.getIidDebitorenkonto())
									.getCNr() : null;
						}
						tempData[0][OFFENE_SUBREPORT_OFFENE_BUCHUNGEN] = FinanzSubreportGenerator
								.createBuchungsdetailSubreport(bdList, true);
					}
				}

				while (resultListIterator.hasNext()) {
					FLRRechnungReport re = (FLRRechnungReport) resultListIterator
							.next();
					RechnungDto reDto = getRechnungFac()
							.rechnungFindByPrimaryKey(re.getI_id());
					// Kunde oder Statistikadresse?
					final Integer kundeIId, kundeIIdStatistik;
					if (krit.getBVerwendeStatistikAdresse()) {
						kundeIId = reDto.getKundeIIdStatistikadresse();
					} else {
						kundeIId = reDto.getKundeIId();
						// Statistikdaten wenn nicht Kriterium Statistikadresse
						kundeIIdStatistik = reDto.getKundeIIdStatistikadresse();

						KundeDto kundeDtoStatistik = getKundeFac()
								.kundeFindByPrimaryKey(kundeIIdStatistik,
										theClientDto);
						String sFirmaStatistik = kundeDtoStatistik
								.getPartnerDto().getCName1nachnamefirmazeile1();

						String sKundeLKZStatistik = null;
						if (kundeDtoStatistik.getPartnerDto()
								.getLandplzortDto() != null) {
							sKundeLKZStatistik = kundeDtoStatistik
									.getPartnerDto().getLandplzortDto()
									.getLandDto().getCLkz();
						}
						tempData[row][OFFENE_FELD_FIRMA_STATISTIK] = sFirmaStatistik;
						tempData[row][OFFENE_FELD_FIRMA_LKZ_STATISTIK] = sKundeLKZStatistik;

					}

					if (kundeDto == null || kundeDto.getIId() != kundeIId) {
						kundeDto = getKundeFac().kundeFindByPrimaryKey(
								kundeIId, theClientDto);
					}

					String sFirma = kundeDto.getPartnerDto()
							.getCName1nachnamefirmazeile1();

					String sKundeLKZ = null;
					if (kundeDto.getPartnerDto().getLandplzortDto() != null) {
						sKundeLKZ = kundeDto.getPartnerDto().getLandplzortDto()
								.getLandDto().getCLkz();
					}
					tempData[row][OFFENE_FELD_RECHNUNGART] = re
							.getFlrrechnungart().getRechnungtyp_c_nr();
					tempData[row][OFFENE_FELD_RECHNUNGSNUMMER] = reDto.getCNr();
					tempData[row][OFFENE_FELD_FIRMA] = sFirma;
					tempData[row][OFFENE_FELD_KREDITLIMIT] = kundeDto
							.getNKreditlimit();
					tempData[row][OFFENE_FELD_FIRMA_LKZ] = sKundeLKZ;
					tempData[row][OFFENE_FELD_RECHNUNGSDATUM] = re
							.getD_belegdatum();

					tempData[row][OFFENE_FELD_KURS] = re.getN_kurs();
					tempData[row][OFFENE_FELD_WAEHRUNG] = re.getWaehrung_c_nr();
					tempData[row][OFFENE_FELD_DEBITORENNR] = kundeDto
							.getIidDebitorenkonto() != null ? getFinanzFac()
							.kontoFindByPrimaryKey(
									kundeDto.getIidDebitorenkonto()).getCNr()
							: null;

					// Vertreter
					if (re.getFlrvertreter() != null) {
						if (re.getFlrvertreter().getFlrpartner()
								.getC_name2vornamefirmazeile2() != null) {
							tempData[row][OFFENE_FELD_VERTRETER] = re
									.getFlrvertreter().getFlrpartner()
									.getC_name1nachnamefirmazeile1()
									+ " "
									+ re.getFlrvertreter().getFlrpartner()
											.getC_name2vornamefirmazeile2();
						} else {
							tempData[row][OFFENE_FELD_VERTRETER] = re
									.getFlrvertreter().getFlrpartner()
									.getC_name1nachnamefirmazeile1();
						}
					}
					// datum der letzten zahlung bis zum stichtag ermitteln
					RechnungzahlungDto[] zahlungen = getRechnungFac()
							.zahlungFindByRechnungIId(re.getI_id());
					java.sql.Date dZahldatum = null;
					for (int i = 0; i < zahlungen.length; i++) {
						if ((dZahldatum == null || zahlungen[i].getDZahldatum()
								.after(dZahldatum))
								&& !zahlungen[i].getDZahldatum().after(
										krit.getTStichtag())) {
							dZahldatum = new Date(zahlungen[i].getDZahldatum()
									.getTime());
						}
					}
					// Zahlungsbetrag bis zum Stichtag ermitteln
					BigDecimal bdBezahlt = new BigDecimal(0);
					for (int i = 0; i < zahlungen.length; i++) {
						if (!zahlungen[i].getDZahldatum().after(
								krit.getTStichtag())) {
							bdBezahlt = bdBezahlt
									.add(zahlungen[i].getNBetrag());
						}
					}
					if (reDto.getKostenstelleIId() != null) {
						KostenstelleDto kstDto = getSystemFac()
								.kostenstelleFindByPrimaryKey(
										reDto.getKostenstelleIId());
						tempData[row][OFFENE_FELD_KOSTENSTELLE] = kstDto
								.getCNr();
					}

					if (reDto.getNWert() == null) {
						EJBExceptionLP ex = new EJBExceptionLP(
								EJBExceptionLP.FEHLER_RECHNUNG_HAT_KEINEN_WERT,
								"");
						ArrayList<Object> alInfo = new ArrayList<Object>();
						alInfo.add("ID: " + reDto.getIId());
						alInfo.add("Nr: " + reDto.getCNr());
						ex.setAlInfoForTheClient(alInfo);
						throw ex;
					}
					BigDecimal bdWertBrutto = reDto.getNWert().add(
							reDto.getNWertust());
					BigDecimal bdWertNetto = reDto.getNWert();

					BigDecimal bdWertBruttoFW = reDto.getNWertfw().add(
							reDto.getNWertustfw());
					BigDecimal bdWertNettoFW = reDto.getNWertfw();

					BigDecimal bdBezahltUst = getRechnungFac()
							.getBereitsBezahltWertVonRechnungUst(
									reDto.getIId(), null, krit.getTStichtag());
					BigDecimal bdBezahltNetto = getRechnungFac()
							.getBereitsBezahltWertVonRechnung(reDto.getIId(),
									null, krit.getTStichtag());

					BigDecimal bdBezahltUstFw = getRechnungFac()
							.getBereitsBezahltWertVonRechnungUstFw(
									reDto.getIId(), null, krit.getTStichtag());
					BigDecimal bdBezahltNettoFw = getRechnungFac()
							.getBereitsBezahltWertVonRechnungFw(reDto.getIId(),
									null, krit.getTStichtag());

					BigDecimal bdNettoOffenFw = reDto.getNWertfw().subtract(
							bdBezahltNettoFw);

					BigDecimal bdUstOffenFw = reDto.getNWertustfw().subtract(
							bdBezahltUstFw);

					BigDecimal bdNettoOffenMandWhg = getLocaleFac()
							.rechneUmInAndereWaehrungZuDatum(bdNettoOffenFw,
									reDto.getWaehrungCNr(),
									theClientDto.getSMandantenwaehrung(),
									krit.getTStichtag(), theClientDto);
					BigDecimal bdUstOffenMandWhg = getLocaleFac()
							.rechneUmInAndereWaehrungZuDatum(bdUstOffenFw,
									reDto.getWaehrungCNr(),
									theClientDto.getSMandantenwaehrung(),
									krit.getTStichtag(), theClientDto);

					BigDecimal bdUstAnzahlungMandWhg = new BigDecimal(0);
					BigDecimal bdNettoAnzahlungMandWhg = new BigDecimal(0);
					BigDecimal bdUstAnzahlungFW = new BigDecimal(0);
					BigDecimal bdNettoAnzahlungFW = new BigDecimal(0);
					if (reDto.getAuftragIId() != null
							&& reDto.getRechnungartCNr().equals(
									RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG)) {
						bdNettoAnzahlungMandWhg = getLocaleFac()
								.rechneUmInAndereWaehrungZuDatum(
										getRechnungFac()
												.getAnzahlungenZuSchlussrechnungFw(
														reDto.getIId()),
										reDto.getWaehrungCNr(),
										theClientDto.getSMandantenwaehrung(),
										krit.getTStichtag(), theClientDto);
						bdUstAnzahlungMandWhg = getLocaleFac()
								.rechneUmInAndereWaehrungZuDatum(
										getRechnungFac()
												.getAnzahlungenZuSchlussrechnungUstFw(
														reDto.getIId()),
										reDto.getWaehrungCNr(),
										theClientDto.getSMandantenwaehrung(),
										krit.getTStichtag(), theClientDto);

					}

					tempData[row][OFFENE_FELD_WERT_BRUTTO_ANZAHLUNGEN] = bdNettoAnzahlungMandWhg
							.add(bdUstAnzahlungMandWhg);
					tempData[row][OFFENE_FELD_WERT_NETTO_ANZAHLUNGEN] = bdNettoAnzahlungMandWhg;
					tempData[row][OFFENE_FELD_WERT_BRUTTO_ANZAHLUNGEN_FW] = bdNettoAnzahlungFW
							.add(bdUstAnzahlungFW);
					tempData[row][OFFENE_FELD_WERT_NETTO_ANZAHLUNGEN_FW] = bdNettoAnzahlungFW;

					// SP554
					bdNettoOffenMandWhg = bdNettoOffenMandWhg
							.subtract(bdNettoAnzahlungMandWhg);
					bdUstOffenMandWhg = bdUstOffenMandWhg
							.subtract(bdUstAnzahlungMandWhg);

					if (re.getFlrrechnungart().getRechnungtyp_c_nr()
							.equals(RechnungFac.RECHNUNGTYP_RECHNUNG)) {
						tempData[row][OFFENE_FELD_WERT_BRUTTO] = bdWertBrutto;
						tempData[row][OFFENE_FELD_WERT_NETTO] = bdWertNetto;
						tempData[row][OFFENE_FELD_WERT_BRUTTO_OFFEN] = bdNettoOffenMandWhg
								.add(bdUstOffenMandWhg);
						tempData[row][OFFENE_FELD_WERT_NETTO_OFFEN] = bdNettoOffenMandWhg;

						// FW
						tempData[row][OFFENE_FELD_WERT_BRUTTO_FW] = bdWertBruttoFW;
						tempData[row][OFFENE_FELD_WERT_NETTO_FW] = bdWertNettoFW;
						tempData[row][OFFENE_FELD_WERT_BRUTTO_OFFEN_FW] = bdNettoOffenFw
								.add(bdUstOffenFw);
						tempData[row][OFFENE_FELD_WERT_NETTO_OFFEN_FW] = bdNettoOffenFw;

					} else if (re.getFlrrechnungart().getRechnungtyp_c_nr()
							.equals(RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
						tempData[row][OFFENE_FELD_WERT_BRUTTO] = bdWertBrutto
								.negate();
						tempData[row][OFFENE_FELD_WERT_NETTO] = bdWertNetto
								.negate();
						tempData[row][OFFENE_FELD_WERT_BRUTTO_OFFEN] = bdNettoOffenMandWhg
								.add(bdUstOffenMandWhg).negate();
						tempData[row][OFFENE_FELD_WERT_NETTO_OFFEN] = bdNettoOffenMandWhg
								.negate();

						// FW
						tempData[row][OFFENE_FELD_WERT_BRUTTO_FW] = bdWertBruttoFW
								.negate();
						tempData[row][OFFENE_FELD_WERT_NETTO_FW] = bdWertNettoFW
								.negate();
						tempData[row][OFFENE_FELD_WERT_BRUTTO_OFFEN_FW] = bdNettoOffenFw
								.add(bdUstOffenFw).negate();
						tempData[row][OFFENE_FELD_WERT_NETTO_OFFEN_FW] = bdNettoOffenFw
								.negate();

					}

					tempData[row][OFFENE_FELD_MAHNDATUM] = getMahnwesenFac()
							.getAktuellesMahndatumEinerRechnung(reDto.getIId(),
									theClientDto);
					tempData[row][OFFENE_FELD_MAHNSTUFE] = getMahnwesenFac()
							.getAktuelleMahnstufeEinerRechnung(reDto.getIId(),
									theClientDto);
					tempData[row][OFFENE_FELD_MAHNSPERRE] = reDto
							.getTMahnsperrebis();
					// Faelligkeitsdatum
					// java.sql.Date dFaellig;
					// if (reDto.getZahlungszielIId() != null) {
					// dFaellig =
					// getMandantFac().berechneZielDatumFuerBelegdatum(reDto
					// .getTBelegdatum(),
					// reDto.getZahlungszielIId(), theClientDto);
					// }
					// else {
					// dFaellig = new
					// java.sql.Date(reDto.getTBelegdatum().getTime());
					// }
					if (re.getT_faelligkeit() != null) {
						tempData[row][OFFENE_FELD_FAELLIGKEITSDATUM] = new java.util.Date(
								re.getT_faelligkeit().getTime());
					} else {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_RECHNUNG_FAELLIGKEIT_NICHT_BERECHENBAR,
								new Exception("Faelligkeit nicht berechenbar"));
					}
					// Differenz in Tagen: Stichtag - Faelligkeitsdatum
					tempData[row][OFFENE_FELD_TAGE_BIS_FAELLIGKEITSDATUM] = Helper
							.getDifferenzInTagen(krit.getTStichtag(),
									re.getT_faelligkeit());
					row++;
				}
				dataList.addAll(Arrays.asList(tempData));
			}

			data = dataList.toArray(new Object[0][]);

			// Waehrung
			mapParameter.put(LPReport.P_WAEHRUNG, mandantDto.getWaehrungCNr());
			mapParameter.put(
					"P_STICHTAG",
					Helper.formatDatum(krit.getTStichtag(),
							theClientDto.getLocUi()));
			StringBuffer sSortierung = new StringBuffer();
			if (krit.bSortiereNachKostenstelle) {
				sSortierung.append(getTextRespectUISpr("lp.kostenstelle",
						theClientDto.getMandant(), theClientDto.getLocUi()));
				sSortierung.append(", ");
			}
			if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				if (krit.getBVerwendeStatistikAdresse()) {
					sSortierung.append(getTextRespectUISpr(
							"rech.statistikadresse", theClientDto.getMandant(),
							theClientDto.getLocUi()));
				} else {
					sSortierung
							.append(getTextRespectUISpr("lp.kunde",
									theClientDto.getMandant(),
									theClientDto.getLocUi()));
				}
			} else if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_BELEGNUMMER) {
				sSortierung.append(getTextRespectUISpr(
						"rechnung.rechnungsnummer", theClientDto.getMandant(),
						theClientDto.getLocUi()));
			} else if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_VERTRETER) {
				sSortierung.append(getTextRespectUISpr("lp.vertreter",
						theClientDto.getMandant(), theClientDto.getLocUi()));
			} else if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_FAELLIGKEIT) {
				sSortierung.append(getTextRespectUISpr("lp.faelligkeit",
						theClientDto.getMandant(), theClientDto.getLocUi()));
			}
			mapParameter.put(LPReport.P_SORTIERUNG, sSortierung.toString());
			mapParameter.put(LPReport.P_SORTIERENACHKOSTENSTELLE, new Boolean(
					krit.bSortiereNachKostenstelle));
			mapParameter
					.put(LPReport.P_SORTIERENACHKUNDE,
							new Boolean(
									krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER));
			mapParameter
					.put(LPReport.P_SORTIERENACHVERTRETER,
							new Boolean(
									krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_VERTRETER));

			String sZessionstext = null;
			sZessionstext = getParameterFac()
					.parametermandantFindByPrimaryKey(
							ParameterFac.PARAMETER_ZESSIONSTEXT,
							ParameterFac.KATEGORIE_ALLGEMEIN,
							theClientDto.getMandant()).getCWert();
			if (sZessionstext != null) {
				mapParameter.put("P_ZESSIONSTEXT", sZessionstext);
			}
			/**
			 * @todo zinsen PJ 4538
			 */

			initJRDS(mapParameter, RechnungReportFac.REPORT_MODUL,
					RechnungReportFac.REPORT_RECHNUNGEN_OFFENE,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);
			return getReportPrint();
			// }
			// catch (FinderException ex) {
			// { // @ToDo FinderException
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN,
			// null);
			// }
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, ex);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		} finally {
			try {
				session.close();
			} catch (HibernateException he) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, he);
			}
		}
	}

	public JasperPrintLP printZahlungsjournal(TheClientDto theClientDto,
			int iSortierung, Date dVon, Date dBis, Integer bankverbindungIId,
			boolean bSortierungNachKostenstelle) throws EJBExceptionLP {
		this.useCase = UC_REPORT_ZAHLUNGSJOURNAL;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRRechnungZahlung.class);
			// Filter von/bis
			if (dVon != null) {
				c.add(Restrictions.ge(
						RechnungFac.FLR_RECHNUNG_ZAHLUNG_D_ZAHLDATUM, dVon));
			}
			if (dBis != null) {
				c.add(Restrictions.le(
						RechnungFac.FLR_RECHNUNG_ZAHLUNG_D_ZAHLDATUM, dBis));
			}

			if (bankverbindungIId != null) {
				c.add(Restrictions.eq(
						RechnungFac.FLR_RECHNUNG_ZAHLUNG_BANKVERBINDUNG_I_ID,
						bankverbindungIId));
			}

			Criteria cRechnung = c
					.createCriteria(RechnungFac.FLR_RECHNUNG_ZAHLUNG_FLRRECHNUNG);
			// Filter nach Mandant
			cRechnung.add(Restrictions.eq(
					RechnungFac.FLR_RECHNUNG_MANDANT_C_NR,
					theClientDto.getMandant()));
			// Sortierung

			if (bSortierungNachKostenstelle) {
				cRechnung.createCriteria(
						RechnungFac.FLR_RECHNUNG_FLRKOSTENSTELLE).addOrder(
						Order.asc("c_nr"));
			}

			if (iSortierung == RechnungReportFac.REPORT_ZAHLUNGEN_SORT_BANK_AUSZUG) {
				// SK full_join wird hier benoetigt damit auch Gutschriften die
				// keine Bankverbindung haben angedruckt werden
				c.createCriteria(
						RechnungFac.FLR_RECHNUNG_ZAHLUNG_FLRBANKVERBINDUNG,
						CriteriaSpecification.FULL_JOIN)
						.createCriteria(FinanzFac.FLR_BANKKONTO_FLRBANK,
								CriteriaSpecification.FULL_JOIN)
						.createCriteria(BankFac.FLR_PARTNERBANK_FLRPARTNER,
								CriteriaSpecification.FULL_JOIN)
						.addOrder(
								Order.asc(PartnerFac.FLR_PARTNER_C_NAME1NACHNAMEFIRMAZEILE1));

				c.addOrder(Order.asc(RechnungFac.FLR_RECHNUNG_ZAHLUNG_I_AUSZUG));
			} else if (iSortierung == RechnungReportFac.REPORT_ZAHLUNGEN_SORT_RECHNUNGSNUMMER) {
				cRechnung.addOrder(Order.asc(RechnungFac.FLR_RECHNUNG_C_NR));
			} else if (iSortierung == RechnungReportFac.REPORT_ZAHLUNGEN_SORT_ZAHLUNGSAUSGANG) {
				c.addOrder(Order
						.asc(RechnungFac.FLR_RECHNUNG_ZAHLUNG_D_ZAHLDATUM));
			}
			List<?> resultList = c.list();
			Iterator<?> resultListIterator = resultList.iterator();
			int row = 0;
			data = new Object[resultList.size()][ZAHLUNGEN_ANZAHL_FELDER];

			while (resultListIterator.hasNext()) {
				FLRRechnungZahlung zahlung = (FLRRechnungZahlung) resultListIterator
						.next();
				RechnungDto erDto = getRechnungFac().rechnungFindByPrimaryKey(
						zahlung.getRechnung_i_id());
				String sErCNr = erDto.getCNr();
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
						erDto.getKundeIId(), theClientDto);

				String sFirma = kundeDto.getPartnerDto().formatAnrede();
				data[row][ZAHLUNGEN_FELD_DEBITORENKONTO] = kundeDto
						.getIDebitorenkontoAsIntegerNotiId();
				data[row][ZAHLUNGEN_FELD_ER_C_NR] = sErCNr;
				data[row][ZAHLUNGEN_FELD_KOSTENSTELLE] = zahlung
						.getFlrrechnung().getFlrkostenstelle().getC_nr();
				data[row][ZAHLUNGEN_FELD_RECHNUNGSART] = erDto
						.getRechnungartCNr();
				data[row][ZAHLUNGEN_FELD_FIRMA] = sFirma;
				data[row][ZAHLUNGEN_FELD_ZAHLDATUM] = zahlung.getD_zahldatum();

				String sLaenderart = getFinanzServiceFac()
						.getLaenderartZuPartner(kundeDto.getPartnerIId(),
								theClientDto);
				data[row][ZAHLUNGEN_FELD_LAENDERART] = sLaenderart;
				if (zahlung.getFlrbankverbindung() != null) {
					data[row][ZAHLUNGEN_FELD_BANK] = zahlung
							.getFlrbankverbindung().getFlrbank()
							.getFlrpartner().getC_name1nachnamefirmazeile1();
				} else {
				}
				data[row][ZAHLUNGEN_FELD_AUSZUG] = zahlung.getI_auszug();
				data[row][ZAHLUNGEN_FELD_WECHSEL] = zahlung
						.getD_wechsel_faellig_am();
				data[row][ZAHLUNGEN_FELD_BELEGWAEHRUNG] = erDto
						.getWaehrungCNr();
				if (zahlung.getFlrbankverbindung() != null) {
					data[row][ZAHLUNGEN_FELD_KONTO] = zahlung
							.getFlrbankverbindung().getC_kontonummer();
				}
				if (RechnungFac.RECHNUNGART_GUTSCHRIFT.equals(erDto
						.getRechnungartCNr())) {
					data[row][ZAHLUNGEN_FELD_BETRAG] = zahlung.getN_betrag()
							.negate();
					data[row][ZAHLUNGEN_FELD_BETRAG_UST] = zahlung
							.getN_betrag_ust().negate();
					data[row][ZAHLUNGEN_FELD_BETRAG_MANDANTENWAEHRUNG_ZU_BELEGZEIT] = zahlung
							.getN_betragfw().multiply(erDto.getNKurs())
							.negate();
					data[row][ZAHLUNGEN_FELD_BETRAG_UST_MANDANTENWAEHRUNG_ZU_BELEGZEIT] = zahlung
							.getN_betrag_ustfw().multiply(erDto.getNKurs())
							.negate();
					data[row][ZAHLUNGEN_FELD_BETRAG_FW] = zahlung
							.getN_betragfw().negate();
					data[row][ZAHLUNGEN_FELD_BETRAG_UST_FW] = zahlung
							.getN_betrag_ustfw().negate();
				} else {
					data[row][ZAHLUNGEN_FELD_BETRAG] = zahlung.getN_betrag();
					data[row][ZAHLUNGEN_FELD_BETRAG_UST] = zahlung
							.getN_betrag_ust();
					data[row][ZAHLUNGEN_FELD_BETRAG_MANDANTENWAEHRUNG_ZU_BELEGZEIT] = zahlung
							.getN_betragfw().multiply(erDto.getNKurs());
					data[row][ZAHLUNGEN_FELD_BETRAG_UST_MANDANTENWAEHRUNG_ZU_BELEGZEIT] = zahlung
							.getN_betrag_ustfw().multiply(erDto.getNKurs());
					data[row][ZAHLUNGEN_FELD_BETRAG_FW] = zahlung
							.getN_betragfw();
					data[row][ZAHLUNGEN_FELD_BETRAG_UST_FW] = zahlung
							.getN_betrag_ustfw();
				}
				data[row][ZAHLUNGEN_FELD_ZAHLUNGSART] = zahlung
						.getZahlungsart_c_nr();
				if (zahlung.getFlrkassenbuch() != null) {
					data[row][ZAHLUNGEN_FELD_KASSENBUCH] = zahlung
							.getFlrkassenbuch().getC_bez();
				} else {
					data[row][ZAHLUNGEN_FELD_KASSENBUCH] = "";
				}
				row++;
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		} finally {
			try {
				session.close();
			} catch (HibernateException he) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, he);
			}
		}
		Map<String, Object> mapParameter = new TreeMap<String, Object>();
		mapParameter.put("VON", dVon);
		mapParameter.put("BIS", dBis);

		mapParameter.put("WAEHRUNG_C_NR", theClientDto.getSMandantenwaehrung());

		String sSortierung = null;
		if (iSortierung == RechnungReportFac.REPORT_ZAHLUNGEN_SORT_BANK_AUSZUG) {
			sSortierung = "Bank, Auszug";
		} else if (iSortierung == RechnungReportFac.REPORT_ZAHLUNGEN_SORT_RECHNUNGSNUMMER) {
			sSortierung = "Rechnungsnummer";
		} else if (iSortierung == RechnungReportFac.REPORT_ZAHLUNGEN_SORT_ZAHLUNGSAUSGANG) {
			sSortierung = "Zahlungseingangsdatum";
		}
		mapParameter.put("SORTIERUNG", sSortierung);

		mapParameter.put("P_SORTIERTNACHKOSTENSTELLE", new Boolean(
				bSortierungNachKostenstelle));

		initJRDS(mapParameter, RechnungReportFac.REPORT_MODUL,
				RechnungReportFac.REPORT_ZAHLUNGSJOURNAL,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);
		return getReportPrint();
	}

	public String exportWAJournal(TheClientDto theClientDto, Integer kundeIId,
			Date dVon, Date dBis, String sLineSeparator, Integer iSortierung)
			throws EJBExceptionLP {

		Object[][] data = null;
		StringBuffer sb = new StringBuffer();
		data = getKundeReportFac().getDataLieferstatistik(theClientDto,
				kundeIId, null, null, dVon, dBis, iSortierung,
				theClientDto.getMandant(), false, false, false,
				KundeReportFac.REPORT_LIEFERSTATISTIK_OPTION_LIEFERADRESSE,
				false);
		if (data != null) {
			String trennzeichen = ";";
			// Ueberschriften
			sb.append("Rechnungsnummer" + trennzeichen);
			sb.append("Lieferscheinnummer" + trennzeichen);
			sb.append(getTextRespectUISpr("lp.datum",
					theClientDto.getMandant(), theClientDto.getLocUi())
					+ trennzeichen);
			sb.append("Ident" + trennzeichen);
			sb.append("Bezeichnung" + trennzeichen);
			sb.append("Menge" + trennzeichen);
			sb.append("Preis" + trennzeichen);

			sb.append("Artikelgruppe" + trennzeichen);
			sb.append("Artikelklasse" + trennzeichen);
			sb.append("Konto" + trennzeichen);
			sb.append("Mwstsatz" + trennzeichen);
			sb.append("Vertreter" + trennzeichen);
			sb.append("Provisionsempfaenger" + trennzeichen);
			sb.append("Kunde" + trennzeichen);
			sb.append("Land" + trennzeichen);
			sb.append("Plz" + trennzeichen);
			sb.append("Ort" + trennzeichen);
			sb.append("Statistikadresse" + trennzeichen);
			sb.append("StatistikadresseLand" + trennzeichen);
			sb.append("StatistikadressePlz" + trennzeichen);
			sb.append("StatistikadresseOrt");

			sb.append(sLineSeparator);

			for (int i = 0; i < data.length; i++) {
				if (data[i][KundeReportFac.REPORT_STATISTIK_RECHNUNGSNUMMER] != null) {
					sb.append(data[i][KundeReportFac.REPORT_STATISTIK_RECHNUNGSNUMMER]);
				}
				sb.append(trennzeichen);
				if (data[i][KundeReportFac.REPORT_STATISTIK_LIEFERSCHEINNUMMER] != null) {
					sb.append(data[i][KundeReportFac.REPORT_STATISTIK_LIEFERSCHEINNUMMER]);
				}
				sb.append(trennzeichen);
				if (data[i][KundeReportFac.REPORT_STATISTIK_DATUM] != null) {
					sb.append(data[i][KundeReportFac.REPORT_STATISTIK_DATUM]);
				}
				sb.append(trennzeichen);
				if (data[i][KundeReportFac.REPORT_STATISTIK_IDENT] != null) {
					sb.append(data[i][KundeReportFac.REPORT_STATISTIK_IDENT]);
				}
				sb.append(trennzeichen);
				if (data[i][KundeReportFac.REPORT_STATISTIK_BEZEICHNUNG] != null) {
					sb.append(data[i][KundeReportFac.REPORT_STATISTIK_BEZEICHNUNG]);
				}
				sb.append(trennzeichen);
				if (data[i][KundeReportFac.REPORT_STATISTIK_MENGE] != null) {
					sb.append(data[i][KundeReportFac.REPORT_STATISTIK_MENGE]);
				}
				sb.append(trennzeichen);
				if (data[i][KundeReportFac.REPORT_STATISTIK_PREIS] != null) {
					sb.append(data[i][KundeReportFac.REPORT_STATISTIK_PREIS]);
				}

				sb.append(trennzeichen);
				if (data[i][KundeReportFac.REPORT_STATISTIK_ARTIKELGRUPPE] != null) {
					sb.append(data[i][KundeReportFac.REPORT_STATISTIK_ARTIKELGRUPPE]);
				}
				sb.append(trennzeichen);
				if (data[i][KundeReportFac.REPORT_STATISTIK_ARTIKELKLASSE] != null) {
					sb.append(data[i][KundeReportFac.REPORT_STATISTIK_ARTIKELKLASSE]);
				}
				sb.append(trennzeichen);
				if (data[i][KundeReportFac.REPORT_STATISTIK_KONTO] != null) {
					sb.append(data[i][KundeReportFac.REPORT_STATISTIK_KONTO]);
				}
				sb.append(trennzeichen);
				if (data[i][KundeReportFac.REPORT_STATISTIK_MWSTSATZ] != null) {
					sb.append(data[i][KundeReportFac.REPORT_STATISTIK_MWSTSATZ]);
				}
				sb.append(trennzeichen);
				if (data[i][KundeReportFac.REPORT_STATISTIK_VERTRETER] != null) {
					sb.append(data[i][KundeReportFac.REPORT_STATISTIK_VERTRETER]);
				}
				sb.append(trennzeichen);
				if (data[i][KundeReportFac.REPORT_STATISTIK_PROVISIONSEMPFAENGER] != null) {
					sb.append(data[i][KundeReportFac.REPORT_STATISTIK_PROVISIONSEMPFAENGER]);
				}
				sb.append(trennzeichen);
				if (data[i][KundeReportFac.REPORT_STATISTIK_KUNDENNAME] != null) {
					sb.append(data[i][KundeReportFac.REPORT_STATISTIK_KUNDENNAME]);
				}
				sb.append(trennzeichen);
				if (data[i][KundeReportFac.REPORT_STATISTIK_LAND] != null) {
					sb.append(data[i][KundeReportFac.REPORT_STATISTIK_LAND]);
				}
				sb.append(trennzeichen);
				if (data[i][KundeReportFac.REPORT_STATISTIK_PLZ] != null) {
					sb.append(data[i][KundeReportFac.REPORT_STATISTIK_PLZ]);
				}
				sb.append(trennzeichen);
				if (data[i][KundeReportFac.REPORT_STATISTIK_ORT] != null) {
					sb.append(data[i][KundeReportFac.REPORT_STATISTIK_ORT]);
				}
				sb.append(trennzeichen);
				if (data[i][KundeReportFac.REPORT_STATISTIK_STATISTIKADRESSE] != null) {
					sb.append(data[i][KundeReportFac.REPORT_STATISTIK_STATISTIKADRESSE]);
				}
				sb.append(trennzeichen);
				if (data[i][KundeReportFac.REPORT_STATISTIK_LAND_STATISTIKADRESSE] != null) {
					sb.append(data[i][KundeReportFac.REPORT_STATISTIK_LAND_STATISTIKADRESSE]);
				}
				sb.append(trennzeichen);
				if (data[i][KundeReportFac.REPORT_STATISTIK_PLZ_STATISTIKADRESSE] != null) {
					sb.append(data[i][KundeReportFac.REPORT_STATISTIK_PLZ_STATISTIKADRESSE]);
				}
				sb.append(trennzeichen);
				if (data[i][KundeReportFac.REPORT_STATISTIK_ORT_STATISTIKADRESSE] != null) {
					sb.append(data[i][KundeReportFac.REPORT_STATISTIK_ORT_STATISTIKADRESSE]);
				}
				sb.append(sLineSeparator);
			}
		}
		return sb.toString();
	}

	public JasperPrintLP printRechnungenAlle(
			ReportRechnungJournalKriterienDto krit, TheClientDto theClientDto)
			throws EJBExceptionLP {
		return buildAlleReport(krit, RechnungFac.RECHNUNGTYP_RECHNUNG,
				RechnungReportFac.REPORT_RECHNUNGEN_ALLE, theClientDto);
	}

	public JasperPrintLP printWarenausgangsjournal(
			ReportRechnungJournalKriterienDto krit, TheClientDto theClientDto)
			throws EJBExceptionLP {
		return buildWarenausgangsjournal(krit, theClientDto);
	}

	public JasperPrintLP printProformarechnungenAlle(
			ReportRechnungJournalKriterienDto krit, TheClientDto theClientDto)
			throws EJBExceptionLP {
		return buildAlleReport(krit, RechnungFac.RECHNUNGTYP_PROFORMARECHNUNG,
				RechnungReportFac.REPORT_PROFORMARECHNUNGEN_ALLE, theClientDto);
	}

	private JasperPrintLP buildWarenausgangsjournal(
			ReportRechnungJournalKriterienDto krit, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Session session = null;
		try {

			String querySelect = "SELECT R.C_NR AS R_C_NR,"
					+ "R.T_BELEGDATUM AS R_T_BELEGDATUM,"
					+ "COALESCE "
					+ "(LP.N_NETTOGESAMTPREISPLUSVERSTECKTERAUFSCHLAGMINUSRABATT, P.N_NETTOEINZELPREISPLUSAUFSCHLAGMINUSRABATT, P.N_NETTOEINZELPREIS ) "
					+ "AS P_N_NETTOEINZELPREISPLUSAUFSCHLAGMINUSRABATT,"
					+ "PN.C_NAME1NACHNAMEFIRMAZEILE1 AS PN_C_NAME1NACHNAMEFIRMAZEILE1,"
					+ "KN.I_KUNDENNUMMER AS KN_I_KUNDENNUMMER,"
					+ "AR.C_NR AS AR_C_NR,"
					+ "ARS.C_BEZ AS ARS_C_BEZ,"
					+ "ARK.C_NR AS ARK_C_NR,"
					+ "ARG.C_NR AS ARG_C_NR,"
					+ "LA.C_LKZ AS LA_C_LKZ,"
					+ "AR.C_WARENVERKEHRSNUMMER AS AR_C_WARENVERKEHRSNUMMER,"
					+ "COALESCE (LP.N_MENGE, P.N_MENGE) AS P_N_MENGE,"
					+ "COALESCE (cast (LP.EINHEIT_C_NR AS VARCHAR), cast (P.EINHEIT_C_NR AS VARCHAR)) AS P_EINHEIT_C_NR,"
					+ "ARS.C_ZBEZ AS ARS_C_ZBEZ,"
					+ "R.N_PROVISION AS R_N_PROVISION,"
					+ "PE.C_KURZZEICHEN AS PE_C_KURZZEICHEN,"
					+ "KNTE.C_NR AS KNTE_C_NR,"
					+ "KNTD.C_NR AS KNTD_C_NR,"
					+ "LPO.C_PLZ AS LPO_C_PLZ,"
					+ "LA2.C_LKZ AS LA2_C_LKZ,"
					+ "PN.C_STRASSE AS PN_C_STRASSE,"
					+ "COALESCE (LSPOSMWSTSATZ.F_MWSTSATZ, POSMWSTSATZ.F_MWSTSATZ, MWSTSATZ.F_MWSTSATZ) AS MWSTSATZ_F_MWSTSATZ,"
					+ "BRANCH.C_NR AS BRANCH_C_NR,"
					+ "cast (PARTKL.C_NR AS VARCHAR) AS PARTKL_C_NR,"
					+ "LA2.C_LKZ AS LA2_C_LKZ,"
					+ "L.C_NR AS L_C_NR,"
					+ "L.T_BELEGDATUM AS L_T_BELEGDATUM,"
					+ "PARTNER_STATISTIK.I_ID AS PART_STATISTIK_I_ID,"
					+ "cast (PARTNER_STATISTIK.ANREDE_C_NR AS VARCHAR) AS PART_STATISTIK_ANREDE_C_NR,"
					+ "PARTNER_STATISTIK.C_NAME1NACHNAMEFIRMAZEILE1 AS PARTNER_STATISTIK_C_NAME1,"
					+ "PARTNER_STATISTIK.C_NAME2VORNAMEFIRMAZEILE2 AS PARTNER_STATISTIK_C_NAME2,"
					+ "PARTNER_STATISTIK.C_NAME3VORNAME2ABTEILUNG AS PARTNER_STATISTIK_C_NAME3_ABTEILUNG,"
					+ "PARTNER_STATISTIK.C_STRASSE AS PARTNER_STATISTIK_C_STRASSE,"
					+ "PARTNER_STATISTIK_LAND.C_LKZ AS PARTNER_STATISTIK_C_LKZ,"
					+ "PARTNER_STATISTIK_ORT.C_NAME AS PARTNER_STATISTIK_C_ORT,"
					+ "PARTNER_STATISTIK_LPO.C_PLZ AS PARTNER_STATISTIK_C_PLZ,"
					+ "PN.I_ID AS PARTNER_I_ID,"
					+ "cast (R.WAEHRUNG_C_NR AS VARCHAR)AS R_WAEHRUNG_C_NR,"
					+ "R.N_KURS AS R_N_KURS,"
					+ "R.RECHNUNGART_C_NR AS R_RECHNUNGART_C_NR,"
					+ "R.RECHNUNG_I_ID_ZURECHNUNG AS R_RECHNUNG_I_ID_ZURECHNUNG,"
					+ "AR.F_VERTRETERPROVISIONMAX AS AR_F_VERTRETERPROVISIONMAX,"
					+ "COALESCE (L.C_BESTELLNUMMER,R.C_BESTELLNUMMER) AS R_BESTELLNUMMER,"
					+ "KNRECHNUNGSADRESSELS.I_KUNDENNUMMER AS KN_RECHNUNGSADRESSELS_I_KUNDENNUMMER,"
					+ "PARTNERRECHNUNGSADRESSELS.C_NAME1NACHNAMEFIRMAZEILE1 AS PARTNERRECHNUNGSADRESSELSBEZEICHNUNG,"
					+ "PARTNERRECHNUNGSADRESSELS.C_KBEZ AS PARTNERRECHNUNGSADRESSELSKBEZ,"
					+ "KN.C_HINWEISEXTERN AS KUNDEHINWEISEXTERN,"
					+ "KN.C_HINWEISINTERN AS KUNDEHINWEISINTERN,"
					+ "PEPROVISION.C_KURZZEICHEN AS PEPROVISION_C_KURZZEICHEN,"
					+ "AR.F_GEWICHTKG AS ARTIKELGEWICHTKG,"
					+ "RELAGER.C_NR AS RELAGERCNR,"
					+ "LSLAGER.C_NR AS LSLAGERCNR,"
					+ "LSZIELLAGER.C_NR AS LSZIELLAGERCNR,"
					+ "KNLIEFERADRESSELS.I_KUNDENNUMMER AS KN_KNLIEFERADRESSELS_I_KUNDENNUMMER,"
					+ "cast(COALESCE (P.X_TEXTINHALT,LP.X_TEXTINHALT) AS VARCHAR (3000)) AS R_TEXTINHALT, "
					+ "PLIEFERADRESSELS.C_ILN AS C_ILNLIEFERADRESSELS, "
					+ "PN.C_ILN AS C_ILNPARTNER, "
					+ "PLIEFERADRESSELS.I_ID AS PART_STATISTIK_I_ID,"
					+ "cast (PLIEFERADRESSELS.ANREDE_C_NR AS VARCHAR) AS PLIEFERADRESSELS_ANREDE_C_NR,"
					+ "PLIEFERADRESSELS.C_NAME1NACHNAMEFIRMAZEILE1 AS PLIEFERADRESSELS_C_NAME1,"
					+ "PLIEFERADRESSELS.C_NAME2VORNAMEFIRMAZEILE2 AS PLIEFERADRESSELS_C_NAME2,"
					+ "PLIEFERADRESSELS.C_NAME3VORNAME2ABTEILUNG AS PLIEFERADRESSELS_C_NAME3_ABTEILUNG,"
					+ "PLIEFERADRESSELS.C_STRASSE AS PLIEFERADRESSELS_C_STRASSE,"
					+ "PLIEFERADRESSELS_LAND.C_LKZ AS PLIEFERADRESSELS_C_LKZ,"
					+ "PLIEFERADRESSELS_ORT.C_NAME AS PLIEFERADRESSELS_C_ORT,"
					+ "PLIEFERADRESSELS_LPO.C_PLZ AS PLIEFERADRESSELS_C_PLZ, "
					+ "BEGR.C_NR AS BEGR_C_NR, "
					+ "ABP.T_UEBERSTEUERTERLIEFERTERMIN AS UEBERSTEUERTERLIEFERTERMIN, "
					+ "KN.I_LIEFERDAUER AS LIEFERDAUER, "
					+ "AB.C_NR AS AUFTRAGSNUMMER, "
					+ "ABG.C_NR AS AUFTRGABEGRUENDUNG, "
					+ " COALESCE ( (SELECT AVG(N_GESTEHUNGSPREIS) FROM WW_LAGERBEWEGUNG WHERE I_BELEGARTPOSITIONID=P.I_ID AND C_BELEGARTNR=R.RECHNUNGART_C_NR AND B_HISTORIE=0),(SELECT AVG(N_GESTEHUNGSPREIS) FROM WW_LAGERBEWEGUNG WHERE I_BELEGARTPOSITIONID=LP.I_ID AND C_BELEGARTNR='Lieferschein' AND B_HISTORIE=0)) AS GESTPREIS,"
					+ " KT.C_BEZ, "
					+ "CASE WHEN LP.POSITION_I_ID_ARTIKELSET IS NULL THEN "
					+ " CASE WHEN (SELECT COUNT(*) FROM LS_LIEFERSCHEINPOSITION WHERE POSITION_I_ID_ARTIKELSET=LP.I_ID) > 0 THEN 'Kopf' "
					+ " ELSE NULL END "
					+ "ELSE 'Position' END AS ARTIKELSETART, COALESCE (R.C_BEZ,L.C_BEZ) AS R_BEZ ";

			String queryFrom = "FROM RECH_RECHNUNG R "
					+ "INNER JOIN RECH_RECHNUNGPOSITION P ON P.RECHNUNG_I_ID=R.I_ID "
					+ "LEFT OUTER JOIN LS_LIEFERSCHEIN L ON L.I_ID=P.LIEFERSCHEIN_I_ID "
					+ "LEFT OUTER JOIN LS_LIEFERSCHEINPOSITION LP ON LP.LIEFERSCHEIN_I_ID=P.LIEFERSCHEIN_I_ID "
					+ "LEFT OUTER JOIN PART_KUNDE KN ON KN.I_ID=R.KUNDE_I_ID "
					+ "LEFT OUTER JOIN PART_PARTNER PN ON PN.I_ID=KN.PARTNER_I_ID "
					+ "LEFT OUTER JOIN WW_ARTIKEL AR ON AR.I_ID=COALESCE(LP.ARTIKEL_I_ID, P.ARTIKEL_I_ID) "
					+ "LEFT OUTER JOIN WW_ARTKLA ARK ON ARK.I_ID=AR.ARTKLA_I_ID "
					+ "LEFT OUTER JOIN WW_ARTGRU ARG ON ARG.I_ID=AR.ARTGRU_I_ID "
					+ "LEFT OUTER JOIN LP_LAND LA ON LA.I_ID=AR.LAND_I_ID_URSPRUNGSLAND "
					+ "LEFT OUTER JOIN PERS_PERSONAL PE ON PE.I_ID=R.PERSONAL_I_ID_VERTRETER "
					+ "LEFT OUTER JOIN FB_KONTO KNTE ON KNTE.I_ID=KN.KONTO_I_ID_ERLOESEKONTO "
					+ "LEFT OUTER JOIN FB_KONTO KNTD ON KNTD.I_ID=KN.KONTO_I_ID_DEBITORENKONTO "
					+ "LEFT OUTER JOIN LP_LANDPLZORT LPO ON LPO.I_ID=PN.LANDPLZORT_I_ID "
					+ "LEFT OUTER JOIN LP_LAND LA2 ON LA2.I_ID=LPO.LAND_I_ID "
					+ "LEFT OUTER JOIN LP_MWSTSATZ MWSTSATZ ON MWSTSATZ.I_ID=R.MWSTSATZ_I_ID "
					+ "LEFT OUTER JOIN LP_MWSTSATZ POSMWSTSATZ ON POSMWSTSATZ.I_ID=P.MWSTSATZ_I_ID "
					+ "LEFT OUTER JOIN LP_MWSTSATZ LSPOSMWSTSATZ ON LSPOSMWSTSATZ.I_ID=LP.MWSTSATZ_I_ID "
					+ "LEFT OUTER JOIN PART_BRANCHE BRANCH ON BRANCH.I_ID=PN.BRANCHE_I_ID "
					+ "LEFT OUTER JOIN PART_PARTNERKLASSE PARTKL ON PARTKL.I_ID=PN.PARTNERKLASSE_I_ID "
					+ "LEFT OUTER JOIN PART_KUNDE KNSTATISTIK ON KNSTATISTIK.I_ID=R.KUNDE_I_ID_STATISTIKADRESSE "
					+ "LEFT OUTER JOIN PART_PARTNER PARTNER_STATISTIK ON PARTNER_STATISTIK.I_ID = KNSTATISTIK.PARTNER_I_ID "
					+ "LEFT OUTER JOIN LP_LANDPLZORT PARTNER_STATISTIK_LPO ON PARTNER_STATISTIK_LPO.I_ID=PARTNER_STATISTIK.LANDPLZORT_I_ID "
					+ "LEFT OUTER JOIN LP_LAND PARTNER_STATISTIK_LAND ON PARTNER_STATISTIK_LAND.I_ID=PARTNER_STATISTIK_LPO.LAND_I_ID "
					+ "LEFT OUTER JOIN LP_ORT PARTNER_STATISTIK_ORT ON PARTNER_STATISTIK_ORT.I_ID=PARTNER_STATISTIK_LPO.ORT_I_ID "
					+ "LEFT OUTER JOIN PART_KUNDE KNRECHNUNGSADRESSELS ON KNRECHNUNGSADRESSELS.I_ID=L.KUNDE_I_ID_RECHNUNGSADRESSE "
					+ "LEFT OUTER JOIN PART_PARTNER PARTNERRECHNUNGSADRESSELS ON PARTNERRECHNUNGSADRESSELS.I_ID=KNRECHNUNGSADRESSELS.PARTNER_I_ID "
					+ "LEFT OUTER JOIN PERS_PERSONAL PEPROVISION ON PEPROVISION.I_ID=KN.PERSONAL_I_ID_BEKOMMEPROVISION "
					+ "LEFT OUTER JOIN WW_LAGER RELAGER ON RELAGER.I_ID=R.LAGER_I_ID "
					+ "LEFT OUTER JOIN WW_LAGER LSLAGER ON LSLAGER.I_ID=L.LAGER_I_ID "
					+ "LEFT OUTER JOIN WW_LAGER LSZIELLAGER ON LSZIELLAGER.I_ID=L.ZIELLAGER_I_ID "
					+ "LEFT OUTER JOIN PART_KUNDE KNLIEFERADRESSELS ON KNLIEFERADRESSELS.I_ID=L.KUNDE_I_ID_LIEFERADRESSE "
					+ "LEFT OUTER JOIN PART_PARTNER PLIEFERADRESSELS ON PLIEFERADRESSELS.I_ID=KNLIEFERADRESSELS.PARTNER_I_ID "
					+ "LEFT OUTER JOIN LP_LANDPLZORT PLIEFERADRESSELS_LPO ON PLIEFERADRESSELS_LPO.I_ID=PLIEFERADRESSELS.LANDPLZORT_I_ID "
					+ "LEFT OUTER JOIN LP_LAND PLIEFERADRESSELS_LAND ON PLIEFERADRESSELS_LAND.I_ID=PLIEFERADRESSELS_LPO.LAND_I_ID "
					+ "LEFT OUTER JOIN LP_ORT PLIEFERADRESSELS_ORT ON PLIEFERADRESSELS_ORT.I_ID=PLIEFERADRESSELS_LPO.ORT_I_ID "
					+ "LEFT OUTER JOIN LS_BEGRUENDUNG BEGR ON BEGR.I_ID=L.BEGRUENDUNG_I_ID "
					+ "LEFT OUTER JOIN AUFT_AUFTRAGPOSITION ABP ON ABP.I_ID=LP.AUFTRAGPOSITION_I_ID "
					+ "LEFT OUTER JOIN AUFT_AUFTRAG AB ON AB.I_ID=ABP.AUFTRAG_I_ID "
					+ "LEFT OUTER JOIN AUFT_AUFTRAGBEGRUENDUNG ABG ON ABG.I_ID=AB.AUFTRAGBEGRUENDUNG_I_ID "
					+ "LEFT OUTER JOIN LP_KOSTENTRAEGER KT ON KT.I_ID=COALESCE(LP.KOSTENTRAEGER_I_ID, P.KOSTENTRAEGER_I_ID) "
					+ "LEFT OUTER JOIN WW_ARTIKELSPR ARS ON ARS.ARTIKEL_I_ID=AR.I_ID AND ARS.LOCALE_C_NR='"
					+ theClientDto.getLocUiAsString() + "' ";

			String queryWhere = "";
			if (krit.dVon != null && krit.dBis != null) {
				queryWhere = "WHERE R.T_BELEGDATUM BETWEEN '"
						+ Helper.formatDateWithSlashes(krit.dVon) + "' AND '"
						+ Helper.formatDateWithSlashes(krit.dBis) + "' ";
			} else {
				if (krit.sBelegnummerVon != null
						&& krit.sBelegnummerBis != null) {
					queryWhere = "WHERE R.C_NR BETWEEN '"
							+ krit.sBelegnummerVon + "' AND '"
							+ krit.sBelegnummerBis + "' ";
				}
			}

			queryWhere = queryWhere + "AND (POSITIONSART_C_NR='"
					+ RechnungFac.POSITIONSART_RECHNUNG_HANDEINGABE.trim()
					+ "' " + "OR P.POSITIONSART_C_NR='"
					+ RechnungFac.POSITIONSART_RECHNUNG_IDENT.trim() + "' "
					+ "OR P.POSITIONSART_C_NR='"
					+ RechnungFac.POSITIONSART_RECHNUNG_LIEFERSCHEIN.trim()
					+ "' " + "OR P.POSITIONSART_C_NR='"
					+ RechnungFac.POSITIONSART_RECHNUNG_TEXTEINGABE.trim()
					+ "') ";
			String[] sRechnungsarten = null;
			if (krit.getBGutschriftenBeruecksichtigen()) {
				sRechnungsarten = new String[5];
				sRechnungsarten[0] = LocaleFac.BELEGART_RECHNUNG;
				sRechnungsarten[1] = LocaleFac.BELEGART_GUTSCHRIFT;
				sRechnungsarten[2] = RechnungFac.RECHNUNGART_WERTGUTSCHRIFT;
				sRechnungsarten[3] = RechnungFac.RECHNUNGART_ANZAHLUNG;
				sRechnungsarten[4] = RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG;
			} else {
				sRechnungsarten = new String[3];
				sRechnungsarten[0] = LocaleFac.BELEGART_RECHNUNG;
				sRechnungsarten[1] = RechnungFac.RECHNUNGART_ANZAHLUNG;
				sRechnungsarten[2] = RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG;
			}
			queryWhere = queryWhere + "AND R.MANDANT_C_NR = '"
					+ theClientDto.getMandant() + "' ";
			boolean bFirstRechnungArt = true;
			for (int i = 0; i < sRechnungsarten.length; i++) {
				if (bFirstRechnungArt) {
					queryWhere = queryWhere + "AND RECHNUNGART_C_NR IN (";
					bFirstRechnungArt = false;
				} else {
					queryWhere = queryWhere + ", ";
				}
				queryWhere = queryWhere + "'" + sRechnungsarten[i].trim() + "'";
			}
			queryWhere = queryWhere + ")";
			queryWhere = queryWhere + "AND STATUS_C_NR NOT LIKE '"
					+ RechnungFac.STATUS_STORNIERT + "' ";
			if (krit.kundeIId != null) {
				queryWhere = queryWhere + "AND R.KUNDE_I_ID = " + krit.kundeIId
						+ " ";
			}
			if (krit.vertreterIId != null) {
				queryWhere = queryWhere + "AND R.PERSONAL_I_ID_VERTRETER = "
						+ krit.vertreterIId + " ";
			}
			if (!krit.getBMitTexteingaben()) {
				// das OR muss wegen Gutschriften rein
				// queryWhere = queryWhere
				// +
				// "AND (LP.LIEFERSCHEINPOSITIONART_C_NR != 'Texteingabe' OR LP.LIEFERSCHEINPOSITIONART_C_NR IS NULL) ";
				queryWhere = queryWhere
						+ "AND ((LP.LIEFERSCHEINPOSITIONART_C_NR != 'Texteingabe' AND LP.LIEFERSCHEINPOSITIONART_C_NR != 'IZwischensumme') "
						+ " OR LP.LIEFERSCHEINPOSITIONART_C_NR IS NULL) ";
			}
			if (krit.kostenstelleIId != null) {
				queryWhere = queryWhere + "AND R.KOSTENSTELLE_I_ID = "
						+ krit.kostenstelleIId + " ";
			}
			String queryOrder = "ORDER BY ";
			if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				queryOrder = queryOrder + "PN.C_NAME1NACHNAMEFIRMAZEILE1";
			} else if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_BELEGNUMMER) {
				queryOrder = queryOrder + "R.C_NR";
			} else if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_VERTRETER) {
				queryOrder = queryOrder + "R.PERSONAL_I_ID_VERTRETER";
			} else if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_IDENT) {
				queryOrder = queryOrder + "AR.C_NR";
			}
			if (krit.getBVerwendeStatistikAdresse()) {
				queryOrder = queryOrder + ", PARTNER_STATISTIK.I_ID";
			}
			String queryString = querySelect + queryFrom + queryWhere
					+ queryOrder;
			this.useCase = UC_REPORT_WARENAUSGANGSJOURNAL;
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Query query = session.createSQLQuery(queryString);
			List<?> list = query.list();
			Map<Integer, Object> tmData = new TreeMap<Integer, Object>();
			int i = 0;
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				tmData.put(i, iter.next());
				i++;
			}
			// Lieferscheine ohne Rechnung hinten anhaengen
			// Alle Lieferscheine ohne Rechnung Kriterien
			List<?> LsList = null;
			if (krit.dVon != null || krit.dBis != null) {
				Criteria cLsPosCrit = session
						.createCriteria(FLRLieferscheinposition.class);
				Criteria cLs = cLsPosCrit
						.createCriteria(LieferscheinpositionFac.FLR_LIEFERSCHEINPOSITION_FLRLIEFERSCHEIN);
				// Filter nach Mandant
				cLs.add(Restrictions.eq(
						LieferscheinFac.FLR_LIEFERSCHEIN_MANDANT_C_NR,
						theClientDto.getMandant()));
				// Filter nach Status (keine stornierten)
				cLs.add(Restrictions.not(Restrictions
						.eq(LieferscheinFac.FLR_LIEFERSCHEIN_LIEFERSCHEINSTATUS_STATUS_C_NR,
								LieferscheinFac.LSSTATUS_STORNIERT)));
				// Filter nach Datum
				if (krit.dVon != null) {
					cLs.add(Restrictions.ge(
							LieferscheinFac.FLR_LIEFERSCHEIN_D_BELEGDATUM,
							krit.dVon));
				}
				if (krit.dBis != null) {
					cLs.add(Restrictions.le(
							LieferscheinFac.FLR_LIEFERSCHEIN_D_BELEGDATUM,
							krit.dBis));
				}

				cLsPosCrit
						.add(Restrictions
								.gt(LieferscheinpositionFac.FLR_LIEFERSCHEINPOSITION_N_MENGE,
										new BigDecimal(0)));

				// Filter nach Kunde
				if (krit.kundeIId != null) {
					cLs.add(Restrictions
							.eq(LieferscheinFac.FLR_LIEFERSCHEIN_KUNDE_I_ID_RECHNUNGSADRESSE,
									krit.kundeIId));
				}
				// Filter nach Vertreter
				if (krit.vertreterIId != null) {
					cLs.add(Restrictions
							.eq(LieferscheinFac.FLR_LIEFERSCHEIN_PERSONAL_I_ID_VERTRETER,
									krit.vertreterIId));
				}
				// Filter nach Kostenstelle
				if (krit.kostenstelleIId != null) {
					cLs.add(Restrictions.eq(
							LieferscheinFac.FLR_LIEFERSCHEIN_KOSTENSTELLE_I_ID,
							krit.kostenstelleIId));
				}
				if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_BELEGNUMMER) {
					cLs.addOrder(Order
							.asc(LieferscheinFac.FLR_LIEFERSCHEIN_C_NR));
				}

				if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
					cLs.createAlias("flrkunde", "k")
							.createAlias("k.flrpartner", "p")
							.addOrder(Order.asc("p.c_name1nachnamefirmazeile1"));

				}
				if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_VERTRETER) {
					cLs.addOrder(Order
							.asc(LieferscheinFac.FLR_LIEFERSCHEIN_PERSONAL_I_ID_VERTRETER));
				}
				if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_IDENT) {
					cLsPosCrit.createAlias("flrartikel", "a").addOrder(
							Order.asc("a.c_nr"));
				}

				if (krit.getBNurOffene()) {
					Collection<String> cStati = new LinkedList<String>();
					cStati.add(LieferscheinFac.LSSTATUS_ANGELEGT);
					cStati.add(LieferscheinFac.LSSTATUS_OFFEN);
					cStati.add(LieferscheinFac.LSSTATUS_ERLEDIGT);
					cStati.add(LieferscheinFac.LSSTATUS_GELIEFERT);
				}
				cLs.add(Restrictions
						.isNull(LieferscheinFac.FLR_LIEFERSCHEIN_FLRRECHNUNG));
				LsList = cLsPosCrit.list();
			}
			// befuellen
			if (LsList != null) {
				for (Iterator<?> iter = LsList.iterator(); iter.hasNext();) {
					FLRLieferscheinposition lspos = (FLRLieferscheinposition) iter
							.next();
					Object[] dataRow = new Object[REPORT_WARENAUSGANG_ANZAHL_SPALTEN];
					dataRow[FELD_WARENAUSGANG_LIEFERSCHEINNUMMER] = lspos
							.getFlrlieferschein().getC_nr();
					dataRow[FELD_WARENAUSGANG_LIEFERSCHEINDATUM] = lspos
							.getFlrlieferschein().getD_belegdatum();

					if (lspos.getFlrlieferschein().getFlrbegruendung() != null) {
						dataRow[FELD_WARENAUSGANG_GRUNDNICHTEINGEHALTEN] = lspos
								.getFlrlieferschein().getFlrbegruendung()
								.getC_nr();

						if (lspos.getFlrlieferschein().getFlrbegruendung()
								.getC_bez() != null) {
							dataRow[FELD_WARENAUSGANG_GRUNDNICHTEINGEHALTEN] = lspos
									.getFlrlieferschein().getFlrbegruendung()
									.getC_bez();
						}

					}

					dataRow[FELD_WARENAUSGANG_KUNDENBEZEICHNUNG] = lspos
							.getFlrlieferschein().getFlrkunde().getFlrpartner()
							.getC_name1nachnamefirmazeile1();
					dataRow[FELD_WARENAUSGANG_TEXTEINGABE] = lspos
							.getC_textinhalt();
					dataRow[FELD_WARENAUSGANG_ILN_LIEFERADRESSE] = lspos
							.getFlrlieferschein().getFlrkunde().getFlrpartner()
							.getC_iln();
					dataRow[FELD_WARENAUSGANG_KUNDENNUMMER_LIEFERADRESSE_LS] = lspos
							.getFlrlieferschein().getFlrkunde()
							.getI_kundennummer();
					dataRow[FELD_WARENAUSGANG_KUNDENNUMMER] = lspos
							.getFlrlieferschein().getFlrkunde()
							.getI_kundennummer();
					dataRow[FELD_WARENAUSGANG_PARTNER_I_ID] = lspos
							.getFlrlieferschein().getFlrkunde().getFlrpartner()
							.getI_id();
					if (lspos.getFlrartikel() != null) {
						dataRow[FELD_WARENAUSGANG_ARTIKELNUMMER] = lspos
								.getFlrartikel().getC_nr();
						dataRow[FELD_WARENAUSGANG_ARTIKELPROVISION] = lspos
								.getFlrartikel().getF_vertreterprovisionmax();

						if (lspos.getFlrartikel().getI_id() != null) {
							ArtikelDto artikelDto = getArtikelFac()
									.artikelFindByPrimaryKeySmall(
											lspos.getFlrartikel().getI_id(),
											theClientDto);
							dataRow[FELD_WARENAUSGANG_ARTIKELBEZEICHNUNG] = artikelDto
									.formatArtikelbezeichnung();
							dataRow[FELD_WARENAUSGANG_ARTIKEL_GEWICHTKG] = artikelDto
									.getFGewichtkg();
							if (artikelDto.formatArtikelbezeichnung().length() > 1) {
								if (artikelDto.formatBezeichnung() != null) {
									// Comment out SK.. funktioniert so nicht
									// ???
									// data[i][
									// FELD_WARENAUSGANG_ARTIKELBEZEICHNUNG2] =
									// artikelDto.
									// formatBezeichnung().substring(artikelDto.
									// formatArtikelbezeichnung().
									// length() - 1);
								} else {
									// data[i][
									// FELD_WARENAUSGANG_ARTIKELBEZEICHNUNG2] =
									// artikelDto.formatBezeichnung();
								}
							} else {
								dataRow[FELD_WARENAUSGANG_ARTIKELBEZEICHNUNG2] = "";
							}
						} else {
							dataRow[FELD_WARENAUSGANG_ARTIKELBEZEICHNUNG] = "";
							dataRow[FELD_WARENAUSGANG_ARTIKELBEZEICHNUNG2] = "";
						}
						if (lspos.getFlrartikel().getFlrartikelklasse() != null) {
							dataRow[FELD_WARENAUSGANG_ARTIKELKLASSE] = lspos
									.getFlrartikel().getFlrartikelklasse()
									.getC_nr();
						} else {
							dataRow[FELD_WARENAUSGANG_ARTIKELKLASSE] = "";
						}
						if (lspos.getFlrartikel().getFlrartikelgruppe() != null) {
							dataRow[FELD_WARENAUSGANG_ARTIKELGRUPPE] = lspos
									.getFlrartikel().getFlrartikelgruppe()
									.getC_nr();
						} else {
							dataRow[FELD_WARENAUSGANG_ARTIKELGRUPPE] = "";
						}
						dataRow[FELD_WARENAUSGANG_EINHEIT] = lspos
								.getFlrartikel().getEinheit_c_nr();
					}

					dataRow[FELD_WARENAUSGANG_URSPRUNGSLAND] = "";
					dataRow[FELD_WARENAUSGANG_WARENVERKEHRSNUMMER] = "";
					dataRow[FELD_WARENAUSGANG_MENGE] = lspos.getN_menge();
					dataRow[FELD_WARENAUSGANG_POSITIONSPREIS] = lspos
							.getN_nettogesamtpreisplusversteckteraufschlagminusrabatt();
					dataRow[FELD_WARENAUSGANG_GESTPREIS] = getLagerFac()
							.getGemittelterGestehungspreisEinerAbgangsposition(
									LocaleFac.BELEGART_LIEFERSCHEIN,
									lspos.getI_id());

					if (lspos.getFlrkostentraeger() != null) {
						dataRow[FELD_WARENAUSGANG_KOSTENTRAEGER] = lspos
								.getFlrkostentraeger().getC_bez();
					}

					dataRow[FELD_WARENAUSGANG_TEXTEINGABE] = lspos
							.getC_textinhalt();
					dataRow[FELD_WARENAUSGANG_RECHNUNGSPROVISION] = null;
					if (lspos.getFlrlieferschein().getPersonal_i_id_vertreter() != null) {
						PersonalDto persDto = getPersonalFac()
								.personalFindByPrimaryKey(
										lspos.getFlrlieferschein()
												.getPersonal_i_id_vertreter(),
										theClientDto);
						dataRow[FELD_WARENAUSGANG_PROVISIONSEMPFAENGER] = persDto
								.getCKurzzeichen();
					}
					if (lspos.getFlrmwstsatz() != null) {
						dataRow[FELD_WARENAUSGANG_MWST] = lspos
								.getFlrmwstsatz().getF_mwstsatz();
					}
					dataRow[FELD_WARENAUSGANG_ERLOESKONTO] = null;
					dataRow[FELD_WARENAUSGANG_DEBITORENKONTO] = null;
					if (lspos.getFlrlieferschein().getFlrkunde()
							.getFlrpartner().getFlrlandplzort() == null) {
						dataRow[FELD_WARENAUSGANG_PLZ] = "";
						dataRow[FELD_WARENAUSGANG_LKZ] = "";
						dataRow[FELD_WARENAUSGANG_STATISTIKADRESSE_ORT] = "";
					} else {
						dataRow[FELD_WARENAUSGANG_PLZ] = lspos
								.getFlrlieferschein().getFlrkunde()
								.getFlrpartner().getFlrlandplzort().getC_plz();
						dataRow[FELD_WARENAUSGANG_LKZ] = lspos
								.getFlrlieferschein().getFlrkunde()
								.getFlrpartner().getFlrlandplzort()
								.getFlrland().getC_lkz();
						dataRow[FELD_WARENAUSGANG_STATISTIKADRESSE_ORT] = lspos
								.getFlrlieferschein().getFlrkunde()
								.getFlrpartner().getFlrlandplzort().getFlrort()
								.getC_name();
						dataRow[FELD_WARENAUSGANG_LIEFERADRESSE_ORT] = lspos
								.getFlrlieferschein().getFlrkunde()
								.getFlrpartner().getFlrlandplzort().getFlrort()
								.getC_name();
					}
					dataRow[FELD_WARENAUSGANG_ADRESSE] = lspos
							.getFlrlieferschein().getFlrkunde().getFlrpartner()
							.getC_strasse();
					dataRow[FELD_WARENAUSGANG_STATISTIKADRESSE_PARTNER_I_ID] = dataRow[FELD_WARENAUSGANG_PARTNER_I_ID];
					dataRow[FELD_WARENAUSGANG_STATISTIKADRESSE_ANREDE_C_NR] = lspos
							.getFlrlieferschein().getFlrkunde().getFlrpartner()
							.getAnrede_c_nr();
					dataRow[FELD_WARENAUSGANG_STATISTIKADRESSE_C_NAME1] = lspos
							.getFlrlieferschein().getFlrkunde().getFlrpartner()
							.getC_name1nachnamefirmazeile1();
					dataRow[FELD_WARENAUSGANG_STATISTIKADRESSE_C_NAME2] = lspos
							.getFlrlieferschein().getFlrkunde().getFlrpartner()
							.getC_name2vornamefirmazeile2();
					dataRow[FELD_WARENAUSGANG_STATISTIKADRESSE_C_NAME3ABTEILUNG] = lspos
							.getFlrlieferschein().getFlrkunde().getFlrpartner()
							.getC_name3vorname2abteilung();
					dataRow[FELD_WARENAUSGANG_STATISTIKADRESSE_C_STRASSE] = lspos
							.getFlrlieferschein().getFlrkunde().getFlrpartner()
							.getC_strasse();
					dataRow[FELD_WARENAUSGANG_STATISTIKADRESSE_LAND] = dataRow[FELD_WARENAUSGANG_LKZ];
					dataRow[FELD_WARENAUSGANG_STATISTIKADRESSE_PLZ] = dataRow[FELD_WARENAUSGANG_PLZ];
					dataRow[FELD_WARENAUSGANG_LIEFERADRESSE_PARTNER_I_ID] = dataRow[FELD_WARENAUSGANG_PARTNER_I_ID];
					dataRow[FELD_WARENAUSGANG_LIEFERADRESSE_ANREDE_C_NR] = lspos
							.getFlrlieferschein().getFlrkunde().getFlrpartner()
							.getAnrede_c_nr();
					dataRow[FELD_WARENAUSGANG_LIEFERADRESSE_C_NAME1] = lspos
							.getFlrlieferschein().getFlrkunde().getFlrpartner()
							.getC_name1nachnamefirmazeile1();
					dataRow[FELD_WARENAUSGANG_LIEFERADRESSE_C_NAME2] = lspos
							.getFlrlieferschein().getFlrkunde().getFlrpartner()
							.getC_name2vornamefirmazeile2();
					dataRow[FELD_WARENAUSGANG_LIEFERADRESSE_C_NAME3ABTEILUNG] = lspos
							.getFlrlieferschein().getFlrkunde().getFlrpartner()
							.getC_name3vorname2abteilung();
					dataRow[FELD_WARENAUSGANG_LIEFERADRESSE_C_STRASSE] = lspos
							.getFlrlieferschein().getFlrkunde().getFlrpartner()
							.getC_strasse();
					dataRow[FELD_WARENAUSGANG_LIEFERADRESSE_LAND] = dataRow[FELD_WARENAUSGANG_LKZ];
					dataRow[FELD_WARENAUSGANG_LIEFERADRESSE_PLZ] = dataRow[FELD_WARENAUSGANG_PLZ];

					Integer brancheIId = lspos.getFlrlieferschein()
							.getFlrkunde().getFlrpartner().getBranche_i_id();
					if (brancheIId != null) {
						dataRow[FELD_WARENAUSGANG_BRANCHE] = getPartnerServicesFac()
								.brancheFindByPrimaryKey(brancheIId,
										theClientDto).getCNr();
					}
					Integer partnerklIId = lspos.getFlrlieferschein()
							.getFlrkunde().getFlrpartner()
							.getPartnerklasse_i_id();
					if (partnerklIId != null) {
						dataRow[FELD_WARENAUSGANG_PARTNERKLASSE] = getPartnerFac()
								.partnerklasseFindByPrimaryKey(partnerklIId,
										theClientDto).getCNr();
					}
					dataRow[FELD_WARENAUSGANG_USTLAND] = null;
					dataRow[FELD_WARENAUSGANG_WAEHRUNG] = lspos
							.getFlrlieferschein()
							.getWaehrung_c_nr_lieferscheinwaehrung();
					dataRow[FELD_WARENAUSGANG_KURS] = new BigDecimal(
							lspos.getFlrlieferschein()
									.getF_wechselkursmandantwaehrungzulieferscheinwaehrung());
					dataRow[FELD_WARENAUSGANG_BESTELLNUMMER] = lspos
							.getFlrlieferschein().getC_bestellnummer();
					dataRow[FELD_WARENAUSGANG_PROJEKT] = lspos
							.getFlrlieferschein().getC_bez_projektbezeichnung();
					if (lspos.getFlrlieferschein()
							.getFlrkunderechnungsadresse() != null) {
						dataRow[FELD_WARENAUSGANG_KUNDENNUMMER_RECHNUNGSADRESSE_LS] = lspos
								.getFlrlieferschein()
								.getFlrkunderechnungsadresse()
								.getI_kundennummer();
						dataRow[FELD_WARENAUSGANG_KUNDENBEZEICHNUNG_RECHNUNGSADRESSE_LS] = lspos
								.getFlrlieferschein()
								.getFlrkunderechnungsadresse().getFlrpartner()
								.getC_name1nachnamefirmazeile1();
						dataRow[FELD_WARENAUSGANG_KUNDEKURZBEZEICHNUNG_RECHNUNGSADRESSE_LS] = lspos
								.getFlrlieferschein()
								.getFlrkunderechnungsadresse().getFlrpartner()
								.getC_kbez();
					} else {
						dataRow[FELD_WARENAUSGANG_KUNDENNUMMER_RECHNUNGSADRESSE_LS] = "";
						dataRow[FELD_WARENAUSGANG_KUNDENBEZEICHNUNG_RECHNUNGSADRESSE_LS] = "";
						dataRow[FELD_WARENAUSGANG_KUNDEKURZBEZEICHNUNG_RECHNUNGSADRESSE_LS] = "";
					}
					dataRow[FELD_WARENAUSGANG_KUNDE_HINWEISEXT] = lspos
							.getFlrlieferschein().getFlrkunde()
							.getC_hinweisextern();
					dataRow[FELD_WARENAUSGANG_KUNDE_HINWEISINT] = lspos
							.getFlrlieferschein().getFlrkunde()
							.getC_hinweisintern();

					if (lspos.getPosition_i_id_artikelset() != null) {

						dataRow[FELD_WARENAUSGANG_SETARTIKEL_TYP] = ArtikelFac.SETARTIKEL_TYP_POSITION;

					} else {
						if (lspos.getSetartikel_set().size() > 0) {
							dataRow[FELD_WARENAUSGANG_SETARTIKEL_TYP] = ArtikelFac.SETARTIKEL_TYP_KOPF;
						}
					}

					if (lspos.getFlrlieferschein().getFlrauftrag() != null) {
						dataRow[FELD_WARENAUSGANG_KUNDE_LIEFERDAUER] = lspos
								.getFlrlieferschein().getFlrauftrag()
								.getFlrkunde().getI_lieferdauer();

						dataRow[FELD_WARENAUSGANG_AUFTRAG_NUMMER] = lspos
								.getFlrlieferschein().getFlrauftrag().getC_nr();

						if (lspos.getFlrlieferschein().getFlrauftrag()
								.getFlrbegruendung() != null) {
							String bez = lspos.getFlrlieferschein()
									.getFlrauftrag().getFlrbegruendung()
									.getC_nr();

							if (lspos.getFlrlieferschein().getFlrauftrag()
									.getFlrbegruendung().getC_bez() != null) {
								bez += " "
										+ lspos.getFlrlieferschein()
												.getFlrauftrag()
												.getFlrbegruendung().getC_bez();
							}

							dataRow[FELD_WARENAUSGANG_AUFTRAG_BEGRUENDUNG] = bez;
						}
					}
					if (lspos.getFlrpositionensichtauftrag() != null) {
						dataRow[FELD_WARENAUSGANG_GEPLANTER_LIEFERTERMIN] = lspos
								.getFlrpositionensichtauftrag()
								.getT_uebersteuerterliefertermin();
					}

					if (lspos.getFlrlieferschein().getFlrkunde()
							.getFlrpersonal() != null) {
						dataRow[FELD_WARENAUSGANG_KUNDE_PROVISIONSEMPFAENGER_KURZZEICHEN] = lspos
								.getFlrlieferschein().getFlrkunde()
								.getFlrpersonal().getC_kurzzeichen();
					} else {
						dataRow[FELD_WARENAUSGANG_KUNDE_PROVISIONSEMPFAENGER_KURZZEICHEN] = "";
					}
					dataRow[FELD_WARENAUSGANG_LAGER_RECHNUNG] = "";
					if (lspos.getFlrlieferschein().getFlrlager() != null) {
						dataRow[FELD_WARENAUSGANG_LAGER_LIEFERSCHEIN] = lspos
								.getFlrlieferschein().getFlrlager().getC_nr();
					} else {
						dataRow[FELD_WARENAUSGANG_LAGER_LIEFERSCHEIN] = "";
					}
					if (lspos.getFlrlieferschein().getFlrziellager() != null) {
						dataRow[FELD_WARENAUSGANG_ZIELLAGER_LIEFERSCHEIN] = lspos
								.getFlrlieferschein().getFlrziellager()
								.getC_nr();
					} else {
						dataRow[FELD_WARENAUSGANG_ZIELLAGER_LIEFERSCHEIN] = "";
					}
					if (lspos.getFlrlieferschein()
							.getFlrkunderechnungsadresse() != null) {
						dataRow[FELD_WARENAUSGANG_ILN_RECHNUNGSADRESSE] = lspos
								.getFlrlieferschein()
								.getFlrkunderechnungsadresse().getFlrpartner()
								.getC_iln();
					}

					tmData.put(i, dataRow);
					i++;
				}
			}

			// tmData in Object[][] umwandeln
			data = new Object[tmData.size()][REPORT_WARENAUSGANG_ANZAHL_SPALTEN];
			for (int y = 0; y < data.length; y++) {
				data[y] = (Object[]) tmData.get(y);
				if (RechnungFac.RECHNUNGART_GUTSCHRIFT
						.equals(data[y][FELD_WARENAUSGANG_RECHNUNGSART])
						|| RechnungFac.RECHNUNGART_WERTGUTSCHRIFT
								.equals(data[y][FELD_WARENAUSGANG_RECHNUNGSART])) {
					BigDecimal bdMenge = (BigDecimal) data[y][FELD_WARENAUSGANG_MENGE];
					if (bdMenge != null) {
						data[y][FELD_WARENAUSGANG_MENGE] = bdMenge
								.multiply(new BigDecimal(-1));
					} else {
						data[y][FELD_WARENAUSGANG_MENGE] = new BigDecimal(0);
					}
					// SK: Achtung bremst
					if (data[y][FELD_WARENAUSGANG_ZURECHNUNG] != null) {
						RechnungDto rechDto = getRechnungFac()
								.rechnungFindByPrimaryKey(
										(Integer) data[y][FELD_WARENAUSGANG_ZURECHNUNG]);
						data[y][FELD_WARENAUSGANG_ZURECHNUNG] = rechDto
								.getCNr();
						data[y][FELD_WARENAUSGANG_LIEFERSCHEINDATUM] = rechDto
								.getTBelegdatum();
					}
				}

				// Dauert so zu lange.. Felder werden einzeln uebergeben
				/*
				 * Integer partnerIId = (Integer)
				 * data[y][FELD_WARENAUSGANG_STATISTIKADRESSE];
				 * if(partnerIId!=null){ PartnerDto helper =
				 * getPartnerFac().partnerFindByPrimaryKey(partnerIId ,
				 * theClientDto); data[y][FELD_WARENAUSGANG_STATISTIKADRESSE]=
				 * helper.formatFixTitelName1Name2(); }
				 */
			}
			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			StringBuffer sSortierung = new StringBuffer();

			mapParameter.put("P_MITTEXTEINGABEN", krit.getBMitTexteingaben());
			if (krit.bSortiereNachKostenstelle) {
				sSortierung.append(getTextRespectUISpr("lp.kostenstelle",
						theClientDto.getMandant(), theClientDto.getLocUi()));
				sSortierung.append(", ");
			}
			if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				if (krit.getBVerwendeStatistikAdresse()) {
					sSortierung.append(getTextRespectUISpr(
							"rech.statistikadresse", theClientDto.getMandant(),
							theClientDto.getLocUi()));
				} else {
					sSortierung
							.append(getTextRespectUISpr("lp.kunde",
									theClientDto.getMandant(),
									theClientDto.getLocUi()));
				}
			} else if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_BELEGNUMMER) {
				sSortierung.append(getTextRespectUISpr("bes.belegnummer",
						theClientDto.getMandant(), theClientDto.getLocUi()));
			} else if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_VERTRETER) {
				sSortierung.append(getTextRespectUISpr("lp.vertreter",
						theClientDto.getMandant(), theClientDto.getLocUi()));
			} else if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_IDENT) {
				sSortierung.append(getTextRespectUISpr("lp.artikel",
						theClientDto.getMandant(), theClientDto.getLocUi()));
			}
			mapParameter.put(LPReport.P_SORTIERUNG, sSortierung.toString());
			if (krit.dBis != null && krit.dVon != null) {
				mapParameter.put(PARAMETER_WARENAUSGANG_BIS,
						krit.dBis.toString());
				mapParameter.put(PARAMETER_WARENAUSGANG_VON,
						krit.dVon.toString());
			}
			if (krit.sBelegnummerBis != null && krit.sBelegnummerVon != null) {
				mapParameter.put(PARAMETER_WARENAUSGANG_BIS,
						krit.sBelegnummerBis);
				mapParameter.put(PARAMETER_WARENAUSGANG_VON,
						krit.sBelegnummerVon);
			}
			mapParameter.put(PARAMETER_WARENAUSGNAG_GRUPPIERUNGNACHSTATISTIK,
					krit.getBVerwendeStatistikAdresse());
			mapParameter.put(PARAMETER_WARENAUSGANG_MANDANTENWAEHRUNG,
					theClientDto.getSMandantenwaehrung());
			initJRDS(mapParameter, RechnungReportFac.REPORT_MODUL,
					RechnungReportFac.REPORT_WARENAUSGANGSJOURNAL,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);
			return getReportPrint();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		} finally {
			if (session != null) {
				session.close();
			}
		}

	}

	public JasperPrintLP printZusammenfassendeMeldung(java.sql.Date dVon,
			java.sql.Date dBis, Integer partnerIIdFinanzamt,
			TheClientDto theClientDto) {
		Session session = null;
		try {

			PartnerDto partnerDtoFinazamt = getPartnerFac()
					.partnerFindByPrimaryKey(partnerIIdFinanzamt, theClientDto);

			this.useCase = UC_REPORT_ALLE;
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			// Kunde oder Statistikadresse verwenden

			Criteria c = session.createCriteria(FLRRechnungReport.class);
			// Filter nach Mandant
			c.add(Restrictions.eq(RechnungFac.FLR_RECHNUNG_MANDANT_C_NR,
					theClientDto.getMandant()));

			// Rechnungen und Gutschriften
			Collection<String> cArten = new LinkedList<String>();
			cArten.add(RechnungFac.RECHNUNGTYP_RECHNUNG);
			cArten.add(RechnungFac.RECHNUNGART_GUTSCHRIFT);
			c.createCriteria(RechnungFac.FLR_RECHNUNG_FLRRECHNUNGART).add(
					Restrictions.in(
							RechnungFac.FLR_RECHNUNGART_RECHNUNGTYP_C_NR,
							cArten));

			// Filter nach Status (keine stornierten)
			c.add(Restrictions.not(Restrictions.eq(
					RechnungFac.FLR_RECHNUNG_STATUS_C_NR,
					RechnungFac.STATUS_STORNIERT)));

			// Datum von/bis
			String sVon = null;
			String sBis = null;
			if (dVon != null) {
				c.add(Restrictions.ge(RechnungFac.FLR_RECHNUNG_D_BELEGDATUM,
						dVon));
				sVon = Helper.formatDatum(dVon, theClientDto.getLocUi());
			}
			if (dBis != null) {
				c.add(Restrictions.le(RechnungFac.FLR_RECHNUNG_D_BELEGDATUM,
						dBis));
				sBis = Helper.formatDatum(dBis, theClientDto.getLocUi());
			}

			// Belegnummer von/bis
			LpBelegnummerFormat f = getBelegnummerGeneratorObj()
					.getBelegnummernFormat(theClientDto.getMandant());
			Integer iGeschaeftsjahr = getParameterFac().getGeschaeftsjahr(
					theClientDto.getMandant());
			String sMandantKuerzel = getParameterFac().getMandantparameter(
					theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_BELEGNUMMER_MANDANTKENNUNG)
					.getCWert();

			// Immer nach Kunde sortiert

			c.createCriteria(RechnungFac.FLR_RECHNUNG_FLRKUNDE)
					.createCriteria(KundeFac.FLR_PARTNER)
					.addOrder(
							Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));
			c.addOrder(Order.asc(RechnungFac.FLR_RECHNUNG_C_NR));

			List<?> list = c.list();

			ArrayList<Object[]> alZeilen = new ArrayList<Object[]>();

			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLRRechnungReport r = (FLRRechnungReport) iter.next();

				Object[] zeile = new Object[REPORT_ZM_ANZAHL_SPALTEN];

				zeile[FELD_ZM_BELEGDATUM] = r.getD_belegdatum();
				// Bezahlte Betraege
				BigDecimal bdBezahlt = getRechnungFac()
						.getBereitsBezahltWertVonRechnung(r.getI_id(), null);
				BigDecimal bdBezahltUst = getRechnungFac()
						.getBereitsBezahltWertVonRechnungUst(r.getI_id(), null);

				BigDecimal bdWertmw = null;
				BigDecimal bdWertUstmw = null;
				BigDecimal bdBezahltmw = null;
				BigDecimal bdBezahltUstmw = null;
				bdBezahltmw = bdBezahlt;
				bdBezahltUstmw = bdBezahltUst;

				bdWertmw = r.getN_wert();
				bdWertUstmw = r.getN_wertust();
				if (r.getFlrrechnungart().getC_nr()
						.equals(RechnungFac.RECHNUNGART_GUTSCHRIFT)
						|| r.getFlrrechnungart().getC_nr()
								.equals(RechnungFac.RECHNUNGART_WERTGUTSCHRIFT)) {
					if (bdWertmw != null)
						bdWertmw = bdWertmw.negate();
					if (bdWertUstmw != null)
						bdWertUstmw = bdWertUstmw.negate();
					if (bdBezahltmw != null)
						bdBezahltmw = bdBezahltmw.negate();
					if (bdBezahltUstmw != null)
						bdBezahltUstmw = bdBezahltUstmw.negate();
				}
				zeile[FELD_ZM_BETRAG] = bdWertmw;
				zeile[FELD_ZM_BETRAGUST] = bdWertUstmw;
				zeile[FELD_ZM_BEZAHLT] = bdBezahltmw;
				zeile[FELD_ZM_BEZAHLTUST] = bdBezahltUstmw;
				// Kostenstelle
				if (r.getFlrkostenstelle() != null) {
					String sKostenstellen = "";
					KostenstelleDto k = null;
					RechnungkontierungDto[] rechnungkontierungsDto = getRechnungFac()
							.rechnungkontierungFindByRechnungIId(r.getI_id());
					if (rechnungkontierungsDto.length != 0) {
						for (int e = 0; e < rechnungkontierungsDto.length; e++) {
							k = getSystemFac().kostenstelleFindByPrimaryKey(
									rechnungkontierungsDto[e]
											.getKostenstelleIId());
							sKostenstellen = sKostenstellen + k.getCNr() + " ";
						}
					} else {
						sKostenstellen = r.getFlrkostenstelle().getC_nr();
					}
					zeile[FELD_ZM_KOSTENSTELLENUMMER] = sKostenstellen;
				} else {
					zeile[FELD_ZM_KOSTENSTELLENUMMER] = null;
				}
				// Kundendaten
				FLRKunde flrKunde, flrKundeStatistik;
				flrKunde = r.getFlrkunde();

				// Statistikdaten wenn nicht Kriterium Statistikadresse
				flrKundeStatistik = r.getFlrstatistikadresse();
				zeile[FELD_ZM_KUNDE_STATISTIK] = flrKundeStatistik
						.getFlrpartner().getC_name1nachnamefirmazeile1();
				if (flrKundeStatistik.getFlrpartner()
						.getC_name2vornamefirmazeile2() != null) {
					zeile[FELD_ZM_KUNDE2_STATISTIK] = flrKundeStatistik
							.getFlrpartner().getC_name2vornamefirmazeile2();
				} else {
					zeile[FELD_ZM_KUNDE2_STATISTIK] = "";
				}
				PartnerDto partnerDtoStatistik = getPartnerFac()
						.partnerFindByPrimaryKey(
								flrKundeStatistik.getFlrpartner().getI_id(),
								theClientDto);
				zeile[FELD_ZM_ADRESSE_STATISTIK] = partnerDtoStatistik
						.formatAdresse();

				zeile[FELD_ZM_KUNDE] = flrKunde.getFlrpartner()
						.getC_name1nachnamefirmazeile1();
				zeile[FELD_ZM_REVERSE_CHARGE] = Helper.short2Boolean(r
						.getB_reversecharge());
				zeile[FELD_ZM_KUNDE_UID] = flrKunde.getFlrpartner().getC_uid();
				if (r.getFlrvertreter() != null) {
					if (r.getFlrvertreter().getFlrpartner()
							.getC_name2vornamefirmazeile2() != null) {
						zeile[FELD_ZM_VERTRETER] = r.getFlrvertreter()
								.getFlrpartner()
								.getC_name1nachnamefirmazeile1()
								+ " "
								+ r.getFlrvertreter().getFlrpartner()
										.getC_name2vornamefirmazeile2();
					} else {
						zeile[FELD_ZM_VERTRETER] = r.getFlrvertreter()
								.getFlrpartner()
								.getC_name1nachnamefirmazeile1();
					}
				}
				if (flrKunde.getFlrpartner().getC_name2vornamefirmazeile2() != null) {
					zeile[FELD_ZM_KUNDE2] = flrKunde.getFlrpartner()
							.getC_name2vornamefirmazeile2();
				} else {
					zeile[FELD_ZM_KUNDE2] = "";
				}
				PartnerDto partnerDto = getPartnerFac()
						.partnerFindByPrimaryKey(
								flrKunde.getFlrpartner().getI_id(),
								theClientDto);
				zeile[FELD_ZM_ADRESSE] = partnerDto.formatAdresse();

				Integer mahnstufeIId = getMahnwesenFac()
						.getAktuelleMahnstufeEinerRechnung(r.getI_id(),
								theClientDto);

				if (mahnstufeIId != null) {
					zeile[FELD_ZM_MAHNSTUFE] = mahnstufeIId;
					MahnstufeDto mahnstufeDto = getMahnwesenFac()
							.mahnstufeFindByPrimaryKey(
									new MahnstufePK(mahnstufeIId, r
											.getMandant_c_nr()));
					// Zinsen nur fuer noch nicht vollstaendig bezahlte
					if (!r.getStatus_c_nr().equals(RechnungFac.STATUS_BEZAHLT)) {
						if (mahnstufeDto.getFZinssatz() != null) {
							BigDecimal bdOffen = r.getN_wert().subtract(
									bdBezahlt);
							// Zinsen
							zeile[FELD_ZM_ZINSEN] = bdOffen.multiply(
									new BigDecimal(mahnstufeDto.getFZinssatz()
											.floatValue())).movePointLeft(2);
						}
					}
				}
				zeile[FELD_ZM_ORT] = partnerDto.formatAdresse();
				if (r.getFlrrechnungart().getC_nr()
						.equals(RechnungFac.RECHNUNGART_RECHNUNG)) {
					zeile[FELD_ZM_RECHNUNGSNUMMER] = "RE " + r.getC_nr();
					zeile[FELD_ZM_RECHNUNGSNUMMERZUGUTSCHRIFT] = "";
				} else if (r.getFlrrechnungart().getC_nr()
						.equals(RechnungFac.RECHNUNGART_ANZAHLUNG)) {
					zeile[FELD_ZM_RECHNUNGSNUMMER] = "AZ " + r.getC_nr();
					zeile[FELD_ZM_RECHNUNGSNUMMERZUGUTSCHRIFT] = "";
				} else if (r.getFlrrechnungart().getC_nr()
						.equals(RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG)) {
					zeile[FELD_ZM_RECHNUNGSNUMMER] = "SZ " + r.getC_nr();
					zeile[FELD_ZM_RECHNUNGSNUMMERZUGUTSCHRIFT] = "";
				} else {
					if (r.getFlrrechnungart().getC_nr()
							.equals(RechnungFac.RECHNUNGART_GUTSCHRIFT)
							|| r.getFlrrechnungart()
									.getC_nr()
									.equals(RechnungFac.RECHNUNGART_WERTGUTSCHRIFT)) {
						zeile[FELD_ZM_RECHNUNGSNUMMER] = "GS " + r.getC_nr();
						// wenn es eine Gutschrift ist die C_NR der Rechnung
						// uebergeben
						if (r.getFlrrechnung() != null) {
							zeile[FELD_ZM_RECHNUNGSNUMMERZUGUTSCHRIFT] = "RE "
									+ r.getFlrrechnung().getC_nr();
						}
					} else {
						zeile[FELD_ZM_RECHNUNGSNUMMER] = r.getC_nr();
						zeile[FELD_ZM_RECHNUNGSNUMMERZUGUTSCHRIFT] = "";
					}
				}
				if (r.getZahlungsziel_i_id() != null) {
					zeile[FELD_ZM_ZIELDATUM] = getMandantFac()
							.berechneZielDatumFuerBelegdatum(
									r.getD_belegdatum(),
									r.getZahlungsziel_i_id(), theClientDto);
				}
				// Bezahlt
				if (r.getT_bezahltdatum() != null) {
					zeile[FELD_ZM_BEZAHLTDATUM] = r.getT_bezahltdatum();
					// Zahltage
					int iZahltage = Helper.getDifferenzInTagen(
							r.getD_belegdatum(), r.getT_bezahltdatum());
					zeile[FELD_ZM_ZAHLTAGE] = new Integer(iZahltage);
				}
				// Debitorenkontonummer
				KontoDtoSmall kontoDtoDeb = null;
				if (flrKunde.getKonto_i_id_debitorenkonto() != null) {
					kontoDtoDeb = getFinanzFac()
							.kontoFindByPrimaryKeySmallOhneExc(
									flrKunde.getKonto_i_id_debitorenkonto());
				}
				String sKontonummer;
				if (kontoDtoDeb != null) {
					sKontonummer = kontoDtoDeb.getCNr();
				} else {
					sKontonummer = "";
				}
				zeile[FELD_ZM_DEBITORENKONTO] = sKontonummer;
				zeile[FELD_ZM_WAEHRUNG] = r.getWaehrung_c_nr();
				zeile[FELD_ZM_KURS] = r.getN_kurs();

				// 14217
				String sLaenderart = getFinanzServiceFac()
						.getLaenderartZuPartner(partnerDtoFinazamt, partnerDto,
								theClientDto);
				zeile[FELD_ZM_LAENDERART] = sLaenderart;

				if (sLaenderart != null
						&& sLaenderart
								.equals(FinanzFac.LAENDERART_EU_AUSLAND_MIT_UID)) {
					alZeilen.add(zeile);
				}

			}

			data = new Object[alZeilen.size()][REPORT_ZM_ANZAHL_SPALTEN];
			data = alZeilen.toArray(data);

			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);
			// Waehrung
			HashMap mapParameter = new HashMap();
			mapParameter.put(LPReport.P_WAEHRUNG, mandantDto.getWaehrungCNr());
			StringBuffer sSortierung = new StringBuffer();

			sSortierung.append(getTextRespectUISpr("lp.kunde",
					theClientDto.getMandant(), theClientDto.getLocUi()));

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

			mapParameter.put(LPReport.P_SORTIERUNG, sSortierung.toString());
			mapParameter.put(LPReport.P_FILTER, sFilter.toString());

			FinanzamtDto finanzamtDto = getFinanzFac()
					.finanzamtFindByPrimaryKey(partnerIIdFinanzamt,
							theClientDto.getMandant(), theClientDto);

			boolean bRunden = false;

			bRunden = Helper.short2boolean(finanzamtDto.getBUmsatzRunden());

			mapParameter.put("P_FA_NAME", finanzamtDto.getPartnerDto()
					.formatFixName1Name2());
			mapParameter.put("P_FA_ADRESSE", finanzamtDto.getPartnerDto()
					.formatAdresse());
			// Steuernummer
			mapParameter.put("P_FA_STEUERNUMMER",
					finanzamtDto.getCSteuernummer());
			mapParameter.put("P_FA_REFERAT", finanzamtDto.getCReferat());
			mapParameter.put("P_UMSATZRUNDEN", new Boolean(bRunden));

			initJRDS(mapParameter, RechnungReportFac.REPORT_MODUL,
					RechnungReportFac.REPORT_RECHNUNGEN_ZM,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);
			return getReportPrint();

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		} finally {
			if (session != null) {
				session.close();
			}
		}

	}

	private JasperPrintLP buildAlleReport(
			ReportRechnungJournalKriterienDto krit, String rechnungtypCNr,
			String sReportname, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Session session = null;
		try {
			this.useCase = UC_REPORT_ALLE;
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			// Kunde oder Statistikadresse verwenden
			final String critKundeIId;
			final String critFLRKunde;
			if (krit.getBVerwendeStatistikAdresse()) {
				critKundeIId = RechnungFac.FLR_RECHNUNG_KUNDE_I_ID_STATISTIKADRESSE;
				critFLRKunde = RechnungFac.FLR_RECHNUNG_FLRSTATISTIKADRESSE;
			} else {
				critKundeIId = RechnungFac.FLR_RECHNUNG_KUNDE_I_ID;
				critFLRKunde = RechnungFac.FLR_RECHNUNG_FLRKUNDE;
			}

			Criteria c = session.createCriteria(FLRRechnungReport.class);
			// Filter nach Mandant
			c.add(Restrictions.eq(RechnungFac.FLR_RECHNUNG_MANDANT_C_NR,
					theClientDto.getMandant()));
			if (krit.getBGutschriftenBeruecksichtigen()) {
				// Rechnungen und Gutschriften
				Collection<String> cArten = new LinkedList<String>();
				cArten.add(rechnungtypCNr);
				cArten.add(RechnungFac.RECHNUNGART_GUTSCHRIFT);
				c.createCriteria(RechnungFac.FLR_RECHNUNG_FLRRECHNUNGART).add(
						Restrictions.in(
								RechnungFac.FLR_RECHNUNGART_RECHNUNGTYP_C_NR,
								cArten));
			} else {
				// nur Rechnungen
				c.createCriteria(RechnungFac.FLR_RECHNUNG_FLRRECHNUNGART).add(
						Restrictions.eq(
								RechnungFac.FLR_RECHNUNGART_RECHNUNGTYP_C_NR,
								rechnungtypCNr));
			}
			// Filter nach Kostenstelle
			if (krit.kostenstelleIId != null) {
				c.add(Restrictions.eq(
						RechnungFac.FLR_RECHNUNG_KOSTENSTELLE_I_ID,
						krit.kostenstelleIId));
			}
			// Filter nach einem Kunden
			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			if (krit.kundeIId != null) {
				c.add(Restrictions.eq(critKundeIId, krit.kundeIId));
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
						krit.kundeIId, theClientDto);
				mapParameter.put("P_KUNDE", kundeDto.getPartnerDto()
						.formatTitelAnrede());
			}
			// Filter nach einem Vertrter
			if (krit.vertreterIId != null) {
				c.add(Restrictions.eq(RechnungFac.FLR_RECHNUNG_FLRVERTRETER
						+ ".i_id", krit.vertreterIId));
			}
			// Filter nach Status (keine stornierten)
			c.add(Restrictions.not(Restrictions.eq(
					RechnungFac.FLR_RECHNUNG_STATUS_C_NR,
					RechnungFac.STATUS_STORNIERT)));
			// Nur offene anzeigen?
			if (krit.getBNurOffene()) {
				Collection<String> cStati = new LinkedList<String>();
				cStati.add(RechnungFac.STATUS_ANGELEGT);
				cStati.add(RechnungFac.STATUS_OFFEN);
				cStati.add(RechnungFac.STATUS_TEILBEZAHLT);
				cStati.add(RechnungFac.STATUS_VERBUCHT);
				c.add(Restrictions.in(RechnungFac.FLR_RECHNUNG_STATUS_C_NR,
						cStati));
			}
			// Datum von/bis
			String sVon = null;
			String sBis = null;
			if (krit.dVon != null) {
				c.add(Restrictions.ge(RechnungFac.FLR_RECHNUNG_D_BELEGDATUM,
						krit.dVon));
				sVon = Helper.formatDatum(krit.dVon, theClientDto.getLocUi());
			}
			if (krit.dBis != null) {
				c.add(Restrictions.le(RechnungFac.FLR_RECHNUNG_D_BELEGDATUM,
						krit.dBis));
				sBis = Helper.formatDatum(krit.dBis, theClientDto.getLocUi());
			}

			// Belegnummer von/bis
			LpBelegnummerFormat f = getBelegnummerGeneratorObj()
					.getBelegnummernFormat(theClientDto.getMandant());
			Integer iGeschaeftsjahr = getParameterFac().getGeschaeftsjahr(
					theClientDto.getMandant());
			String sMandantKuerzel = getParameterFac().getMandantparameter(
					theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_BELEGNUMMER_MANDANTKENNUNG)
					.getCWert();
			if (krit.sBelegnummerVon != null) {
				sVon = HelperServer.getBelegnummernFilterForHibernateCriterias(
						f, iGeschaeftsjahr, sMandantKuerzel,
						krit.sBelegnummerVon);
				c.add(Restrictions.ge(RechnungFac.FLR_RECHNUNG_C_NR, sVon));
			}
			if (krit.sBelegnummerBis != null) {
				sBis = HelperServer.getBelegnummernFilterForHibernateCriterias(
						f, iGeschaeftsjahr, sMandantKuerzel,
						krit.sBelegnummerBis);
				c.add(Restrictions.le(RechnungFac.FLR_RECHNUNG_C_NR, sBis));
			}
			// Sortierung nach Kostenstelle ist optional
			if (krit.bSortiereNachKostenstelle) {
				c.createCriteria(RechnungFac.FLR_RECHNUNG_FLRKOSTENSTELLE)
						.addOrder(Order.asc("c_nr"));
			}
			// Sortierung nach Partner ist optional
			if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				c.createCriteria(critFLRKunde)
						.createCriteria(KundeFac.FLR_PARTNER)
						.addOrder(
								Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));
			} else if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_BELEGNUMMER) {
				c.addOrder(Order.asc(RechnungFac.FLR_RECHNUNG_C_NR));
			}
			if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_VERTRETER) {
				c.createCriteria(RechnungFac.FLR_RECHNUNG_FLRVERTRETER)
						.addOrder(
								Order.asc(PersonalFac.FLR_PERSONAL_C_KURZZEICHEN));
			}
			List<?> list = c.list();
			data = new Object[list.size()][REPORT_ALLE_ANZAHL_SPALTEN];

			// Map mapZahlungsziele =
			// getMandantFac().zahlungszielFindAllByMandantAsDto
			// (theClientDto.getMandant(),
			// theClientDto.getIDUser());

			int i = 0;
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLRRechnungReport r = (FLRRechnungReport) iter.next();
				data[i][FELD_ALLE_BELEGDATUM] = r.getD_belegdatum();
				// Bezahlte Betraege
				BigDecimal bdBezahlt = getRechnungFac()
						.getBereitsBezahltWertVonRechnung(r.getI_id(), null);
				BigDecimal bdBezahltUst = getRechnungFac()
						.getBereitsBezahltWertVonRechnungUst(r.getI_id(), null);

				BigDecimal bdWertmw = null;
				BigDecimal bdWertUstmw = null;
				BigDecimal bdBezahltmw = null;
				BigDecimal bdBezahltUstmw = null;
				bdBezahltmw = bdBezahlt;
				bdBezahltUstmw = bdBezahltUst;

				BigDecimal bdWertFW = null;
				BigDecimal bdWertUstFW = null;
				BigDecimal bdBezahltFW = getRechnungFac()
						.getBereitsBezahltWertVonRechnungFw(r.getI_id(), null);
				BigDecimal bdBezahltUstFW = getRechnungFac()
						.getBereitsBezahltWertVonRechnungUstFw(r.getI_id(),
								null);

				bdWertmw = r.getN_wert();
				bdWertUstmw = r.getN_wertust();
				bdWertFW = r.getN_wertfw();
				bdWertUstFW = r.getN_wertustfw();
				if (r.getFlrrechnungart().getC_nr()
						.equals(RechnungFac.RECHNUNGART_GUTSCHRIFT)
						|| r.getFlrrechnungart().getC_nr()
								.equals(RechnungFac.RECHNUNGART_WERTGUTSCHRIFT)) {
					if (bdWertmw != null)
						bdWertmw = bdWertmw.negate();
					if (bdWertUstmw != null)
						bdWertUstmw = bdWertUstmw.negate();
					if (bdBezahltmw != null)
						bdBezahltmw = bdBezahltmw.negate();
					if (bdBezahltUstmw != null)
						bdBezahltUstmw = bdBezahltUstmw.negate();
					// FW
					if (bdWertFW != null)
						bdWertFW = bdWertFW.negate();
					if (bdWertUstFW != null)
						bdWertUstFW = bdWertUstFW.negate();
					if (bdBezahltFW != null)
						bdBezahltFW = bdBezahltFW.negate();
					if (bdBezahltUstFW != null)
						bdBezahltUstFW = bdBezahltUstFW.negate();

				}
				data[i][FELD_ALLE_BETRAG] = bdWertmw;
				data[i][FELD_ALLE_BETRAGUST] = bdWertUstmw;
				data[i][FELD_ALLE_BEZAHLT] = bdBezahltmw;
				data[i][FELD_ALLE_BEZAHLTUST] = bdBezahltUstmw;

				data[i][FELD_ALLE_BETRAG_FW] = bdWertFW;
				data[i][FELD_ALLE_BETRAGUST_FW] = bdWertUstFW;
				data[i][FELD_ALLE_BEZAHLT_FW] = bdBezahltFW;
				data[i][FELD_ALLE_BEZAHLTUST_FW] = bdBezahltUstFW;

				BigDecimal bdUstAnzahlungMandWhg = new BigDecimal(0);
				BigDecimal bdNettoAnzahlungMandWhg = new BigDecimal(0);
				BigDecimal bdUstAnzahlungFW = new BigDecimal(0);
				BigDecimal bdNettoAnzahlungFW = new BigDecimal(0);
				if (r.getFlrauftrag() != null
						&& r.getFlrrechnungart().getC_nr()
								.equals(RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG)) {

					bdUstAnzahlungFW = getRechnungFac()
							.getAnzahlungenZuSchlussrechnungUstFw(r.getI_id());
					bdNettoAnzahlungFW = getRechnungFac()
							.getAnzahlungenZuSchlussrechnungFw(r.getI_id());

					bdNettoAnzahlungMandWhg = getLocaleFac()
							.rechneUmInAndereWaehrungZuDatum(
									bdNettoAnzahlungFW, r.getWaehrung_c_nr(),
									theClientDto.getSMandantenwaehrung(),
									new Date(System.currentTimeMillis()),
									theClientDto);
					bdUstAnzahlungMandWhg = getLocaleFac()
							.rechneUmInAndereWaehrungZuDatum(bdUstAnzahlungFW,
									r.getWaehrung_c_nr(),
									theClientDto.getSMandantenwaehrung(),
									new Date(System.currentTimeMillis()),
									theClientDto);

				}

				data[i][FELD_ALLE_WERT_ANZAHLUNGEN] = bdNettoAnzahlungMandWhg;
				data[i][FELD_ALLE_WERT_ANZAHLUNGENUST] = bdUstAnzahlungMandWhg;
				data[i][FELD_ALLE_WERT_ANZAHLUNGEN_FW] = bdNettoAnzahlungFW;
				data[i][FELD_ALLE_WERT_ANZAHLUNGENUST_FW] = bdUstAnzahlungFW;

				// Kostenstelle
				if (r.getFlrkostenstelle() != null) {
					String sKostenstellen = "";
					KostenstelleDto k = null;
					RechnungkontierungDto[] rechnungkontierungsDto = getRechnungFac()
							.rechnungkontierungFindByRechnungIId(r.getI_id());
					if (rechnungkontierungsDto.length != 0) {
						for (int e = 0; e < rechnungkontierungsDto.length; e++) {
							k = getSystemFac().kostenstelleFindByPrimaryKey(
									rechnungkontierungsDto[e]
											.getKostenstelleIId());
							sKostenstellen = sKostenstellen + k.getCNr() + " ";
						}
					} else {
						sKostenstellen = r.getFlrkostenstelle().getC_nr();
					}
					data[i][FELD_ALLE_KOSTENSTELLENUMMER] = sKostenstellen;
				} else {
					data[i][FELD_ALLE_KOSTENSTELLENUMMER] = null;
				}
				// Kundendaten
				FLRKunde flrKunde, flrKundeStatistik;
				if (krit.getBVerwendeStatistikAdresse()) {
					flrKunde = r.getFlrstatistikadresse();
				} else {
					flrKunde = r.getFlrkunde();

					// Statistikdaten wenn nicht Kriterium Statistikadresse
					flrKundeStatistik = r.getFlrstatistikadresse();
					data[i][FELD_ALLE_KUNDE_STATISTIK] = flrKundeStatistik
							.getFlrpartner().getC_name1nachnamefirmazeile1();
					if (flrKundeStatistik.getFlrpartner()
							.getC_name2vornamefirmazeile2() != null) {
						data[i][FELD_ALLE_KUNDE2_STATISTIK] = flrKundeStatistik
								.getFlrpartner().getC_name2vornamefirmazeile2();
					} else {
						data[i][FELD_ALLE_KUNDE2_STATISTIK] = "";
					}
					PartnerDto partnerDtoStatistik = getPartnerFac()
							.partnerFindByPrimaryKey(
									flrKundeStatistik.getFlrpartner().getI_id(),
									theClientDto);
					data[i][FELD_ALLE_ADRESSE_STATISTIK] = partnerDtoStatistik
							.formatAdresse();

				}
				data[i][FELD_ALLE_KUNDE] = flrKunde.getFlrpartner()
						.getC_name1nachnamefirmazeile1();
				data[i][FELD_ALLE_KREDITLIMIT] = flrKunde.getN_kreditlimit();
				data[i][FELD_ALLE_REVERSE_CHARGE] = Helper.short2Boolean(r
						.getB_reversecharge());
				data[i][FELD_ALLE_STATUS] = r.getStatus_c_nr();
				data[i][FELD_ALLE_ZOLLBELEGNUMMER] = r.getC_zollpapier();
				data[i][FELD_ALLE_KUNDE_UID] = flrKunde.getFlrpartner()
						.getC_uid();
				if (r.getFlrvertreter() != null) {
					if (r.getFlrvertreter().getFlrpartner()
							.getC_name2vornamefirmazeile2() != null) {
						data[i][FELD_ALLE_VERTRETER] = r.getFlrvertreter()
								.getFlrpartner()
								.getC_name1nachnamefirmazeile1()
								+ " "
								+ r.getFlrvertreter().getFlrpartner()
										.getC_name2vornamefirmazeile2();
					} else {
						data[i][FELD_ALLE_VERTRETER] = r.getFlrvertreter()
								.getFlrpartner()
								.getC_name1nachnamefirmazeile1();
					}
				}
				if (flrKunde.getFlrpartner().getC_name2vornamefirmazeile2() != null) {
					data[i][FELD_ALLE_KUNDE2] = flrKunde.getFlrpartner()
							.getC_name2vornamefirmazeile2();
				} else {
					data[i][FELD_ALLE_KUNDE2] = "";
				}
				PartnerDto partnerDto = getPartnerFac()
						.partnerFindByPrimaryKey(
								flrKunde.getFlrpartner().getI_id(),
								theClientDto);
				data[i][FELD_ALLE_ADRESSE] = partnerDto.formatAdresse();
				Integer mahnstufeIId = getMahnwesenFac()
						.getAktuelleMahnstufeEinerRechnung(r.getI_id(),
								theClientDto);

				if (mahnstufeIId != null) {
					data[i][FELD_ALLE_MAHNSTUFE] = mahnstufeIId;
					MahnstufeDto mahnstufeDto = getMahnwesenFac()
							.mahnstufeFindByPrimaryKey(
									new MahnstufePK(mahnstufeIId, r
											.getMandant_c_nr()));
					// Zinsen nur fuer noch nicht vollstaendig bezahlte
					if (!r.getStatus_c_nr().equals(RechnungFac.STATUS_BEZAHLT)) {
						if (mahnstufeDto.getFZinssatz() != null) {
							BigDecimal bdOffen = r.getN_wert().subtract(
									bdBezahlt);
							// Zinsen
							data[i][FELD_ALLE_ZINSEN] = bdOffen.multiply(
									new BigDecimal(mahnstufeDto.getFZinssatz()
											.floatValue())).movePointLeft(2);
						}
					}
				}
				data[i][FELD_ALLE_ORT] = partnerDto.formatAdresse();
				if (r.getFlrrechnungart().getC_nr()
						.equals(RechnungFac.RECHNUNGART_RECHNUNG)) {
					data[i][FELD_ALLE_RECHNUNGSNUMMER] = "RE " + r.getC_nr();
					data[i][FELD_ALLE_RECHNUNGSNUMMERZUGUTSCHRIFT] = "";
				} else {
					if (r.getFlrrechnungart().getC_nr()
							.equals(RechnungFac.RECHNUNGART_GUTSCHRIFT)
							|| r.getFlrrechnungart()
									.getC_nr()
									.equals(RechnungFac.RECHNUNGART_WERTGUTSCHRIFT)) {
						data[i][FELD_ALLE_RECHNUNGSNUMMER] = "GS "
								+ r.getC_nr();
						// wenn es eine Gutschrift ist die C_NR der Rechnung
						// uebergeben
						if (r.getFlrrechnung() != null) {
							data[i][FELD_ALLE_RECHNUNGSNUMMERZUGUTSCHRIFT] = "RE "
									+ r.getFlrrechnung().getC_nr();
						}
					} else if (r.getFlrrechnungart().getC_nr()
							.equals(RechnungFac.RECHNUNGART_ANZAHLUNG)) {
						data[i][FELD_ALLE_RECHNUNGSNUMMER] = "AZ "
								+ r.getC_nr();
					} else if (r.getFlrrechnungart().getC_nr()
							.equals(RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG)) {
						data[i][FELD_ALLE_RECHNUNGSNUMMER] = "SR "
								+ r.getC_nr();
					} else if (r.getFlrrechnungart().getC_nr()
							.equals(RechnungFac.RECHNUNGART_PROFORMARECHNUNG)) {
						data[i][FELD_ALLE_RECHNUNGSNUMMER] = "PF "
								+ r.getC_nr();
					} else {
						data[i][FELD_ALLE_RECHNUNGSNUMMER] = r.getC_nr();
						data[i][FELD_ALLE_RECHNUNGSNUMMERZUGUTSCHRIFT] = "";
					}
				}
				if (r.getZahlungsziel_i_id() != null) {
					data[i][FELD_ALLE_ZIELDATUM] = getMandantFac()
							.berechneZielDatumFuerBelegdatum(
									r.getD_belegdatum(),
									r.getZahlungsziel_i_id(), theClientDto);
				}
				// Bezahlt
				if (r.getT_bezahltdatum() != null) {
					data[i][FELD_ALLE_BEZAHLTDATUM] = r.getT_bezahltdatum();
					// Zahltage
					int iZahltage = Helper.getDifferenzInTagen(
							r.getD_belegdatum(), r.getT_bezahltdatum());
					data[i][FELD_ALLE_ZAHLTAGE] = new Integer(iZahltage);
				} else {
					// SP2297
					int iZahltage = Helper.getDifferenzInTagen(r
							.getD_belegdatum(), Helper.cutDate(new Date(System
							.currentTimeMillis())));
					data[i][FELD_ALLE_ZAHLTAGE] = new Integer(iZahltage);
				}
				// Debitorenkontonummer
				KontoDtoSmall kontoDtoDeb = null;
				if (flrKunde.getKonto_i_id_debitorenkonto() != null) {
					kontoDtoDeb = getFinanzFac()
							.kontoFindByPrimaryKeySmallOhneExc(
									flrKunde.getKonto_i_id_debitorenkonto());
				}
				String sKontonummer;
				if (kontoDtoDeb != null) {
					sKontonummer = kontoDtoDeb.getCNr();
				} else {
					sKontonummer = "";
				}
				data[i][FELD_ALLE_DEBITORENKONTO] = sKontonummer;
				data[i][FELD_ALLE_WAEHRUNG] = r.getWaehrung_c_nr();
				data[i][FELD_ALLE_KURS] = r.getN_kurs();

				// 14217
				String sLaenderart = getFinanzServiceFac()
						.getLaenderartZuPartner(partnerDto.getIId(),
								theClientDto);
				data[i][FELD_ALLE_LAENDERART] = sLaenderart;
				i++;
			}

			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);
			// Waehrung
			mapParameter.put(LPReport.P_WAEHRUNG, mandantDto.getWaehrungCNr());
			StringBuffer sSortierung = new StringBuffer();
			if (krit.bSortiereNachKostenstelle) {
				sSortierung.append(getTextRespectUISpr("lp.kostenstelle",
						theClientDto.getMandant(), theClientDto.getLocUi()));
				sSortierung.append(", ");
			}
			if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				if (krit.getBVerwendeStatistikAdresse()) {
					sSortierung.append(getTextRespectUISpr(
							"rech.statistikadresse", theClientDto.getMandant(),
							theClientDto.getLocUi()));
				} else {
					sSortierung
							.append(getTextRespectUISpr("lp.kunde",
									theClientDto.getMandant(),
									theClientDto.getLocUi()));
				}
			} else if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_BELEGNUMMER) {
				sSortierung.append(getTextRespectUISpr("bes.belegnummer",
						theClientDto.getMandant(), theClientDto.getLocUi()));
			} else if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_VERTRETER) {
				sSortierung.append(getTextRespectUISpr("lp.vertreter",
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
			// Statistikadresse
			if (krit.getBVerwendeStatistikAdresse()) {
				if (sFilter.length() > 0) {
					sFilter.append(", ");
				}
				sFilter.append(getTextRespectUISpr("rech.nachstatistikadresse",
						theClientDto.getMandant(), theClientDto.getLocUi()));
			}

			mapParameter.put(LPReport.P_SORTIERUNG, sSortierung.toString());
			mapParameter.put(LPReport.P_SORTIERENACHKOSTENSTELLE, new Boolean(
					krit.bSortiereNachKostenstelle));
			mapParameter
					.put(LPReport.P_SORTIERENACHKUNDE,
							new Boolean(
									krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER));
			mapParameter
					.put(LPReport.P_SORTIERENACHVERTRETER,
							new Boolean(
									krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_VERTRETER));
			mapParameter.put(LPReport.P_FILTER, sFilter.toString());
			initJRDS(mapParameter, RechnungReportFac.REPORT_MODUL, sReportname,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);
			return getReportPrint();
			// }
			// catch (FinderException ex) {
			// { // @ToDo FinderException
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN,
			// null);
			// }
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, ex);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	private int getZeilenAnzahlDesRechnungsDrucksUndCacheDaten(
			RechnungPositionDto[] rePos,
			HashMap<Integer, LieferscheinDto> hmLSDto,
			HashMap<Integer, LieferscheinpositionDto[]> hmLSPosDtos,
			HashMap<Integer, Object[][]> hmLSData,
			HashMap<Integer, StuecklisteInfoDto> hmStklPos,
			HashMap<Integer, ArtikelDto> hmArtikel,
			boolean bLeerzeileNachMengenbehafteterPosition,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// laufzeiten werden mitgeloggt
		long lStartzeit = System.currentTimeMillis();
		int rows = 0;
		try {
			// da aber auch saemtliche lieferscheinpositionen gedruckt werden,
			// muss ich mir das vorher anschaun
			for (int i = 0; i < rePos.length; i++) {
				if (rePos[i].getRechnungpositionartCNr().equals(
						RechnungFac.POSITIONSART_RECHNUNG_LIEFERSCHEIN)) {
					Integer lieferscheinIId = rePos[i].getLieferscheinIId();
					LieferscheinDto lieferscheinDto = getLieferscheinFac()
							.lieferscheinFindByPrimaryKey(lieferscheinIId,
									theClientDto);
					LieferscheinpositionDto[] lsPos = getLieferscheinpositionFac()
							.getLieferscheinPositionenByLieferschein(
									lieferscheinDto.getIId(), theClientDto);
					// Optional: Leerzeilen nach jeder mengenbehafteten Position
					// auch fuer
					// die angedruckten Lieferscheinpositionen
					if (bLeerzeileNachMengenbehafteterPosition) {
						for (int j = 0; j < lsPos.length; j++) {
							if (lsPos[j].getNMenge() != null) {
								rows++;
							}
						}
					}
					Object[][] lsData = getLieferscheinReportFac()
							.getLieferscheinReportData(lieferscheinIId,
									theClientDto);
					hmLSDto.put(lieferscheinIId, lieferscheinDto);
					hmLSPosDtos.put(lieferscheinIId, lsPos);
					hmLSData.put(lieferscheinIId, lsData);
					// Der Lieferschein hat ausserdem eine kopf- und eine
					// fusszeile -> +2
					rows = rows + lsData.length + 2;
				} else if (rePos[i].getRechnungpositionartCNr().equals(
						RechnungFac.POSITIONSART_RECHNUNG_IDENT)) {
					// eine zeile fuer die position selbst
					rows++;
					// nur wenn der Artikel noch nicht gecacht wurde
					if (hmArtikel.get(rePos[i].getArtikelIId()) == null) {
						// die eventuell dem Artikel hinterlegte Stueckliste
						// holen
						StuecklisteDto stuecklisteDto = getStuecklisteFac()
								.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
										rePos[i].getArtikelIId(), theClientDto);
						// hier kann ich auch gleich den Artikel cachen
						ArtikelDto artikelDto = null;
						if (stuecklisteDto != null) {
							// Stuecklisteninfo holen, enthaelt anzahl der
							// positionen und hierarchie
							StuecklisteInfoDto stuecklisteInfoDto = getStuecklisteFac()
									.getStrukturdatenEinesArtikels(
											rePos[i].getArtikelIId(), true,
											null, // in die Rekursion mit einer
											// leeren Listen einsteigen
											0, // in die Rekursion mit Ebene 0
												// einsteigen
											-1, // alle Stuecklisten komplett
											// aufloesen
											false, // Menge pro Einheit der
											// uebergeorndeten Position
											new BigDecimal(1), // fuer 1 Einheit
											// der STKL
											true, // Fremdfertigung aufloesen
											theClientDto);
							rows += stuecklisteInfoDto.getIiAnzahlPositionen()
									.intValue();
							hmStklPos.put(rePos[i].getArtikelIId(),
									stuecklisteInfoDto);
							// jetzt noch den artikel holen
							artikelDto = stuecklisteDto.getArtikelDto();
							// der Artikel kommt nicht immer mit :-(
							if (artikelDto == null) {
								artikelDto = getArtikelFac()
										.artikelFindByPrimaryKey(
												stuecklisteDto.getArtikelIId(),
												theClientDto);
							}
						} else {
							artikelDto = getArtikelFac()
									.artikelFindByPrimaryKey(
											rePos[i].getArtikelIId(),
											theClientDto);
						}
						// Artikel behalten
						hmArtikel.put(rePos[i].getArtikelIId(), artikelDto);
					} else {
						// wenn der artikel schon gecacht wurde, muss ich auch
						// schaun, ob der zu druckende Stkl-Positionen hat
						if (hmStklPos.get(rePos[i].getArtikelIId()) != null) {
							StuecklisteInfoDto stklInfoDto = hmStklPos
									.get(rePos[i].getArtikelIId());
							rows += stklInfoDto.getIiAnzahlPositionen()
									.intValue();
						}
					}
					// fuer Serien/Chargenr. wird jeweils eine zeile reserviert.
					int iAnzahlSerienChargeNummern = berechneAnzahlSerienChargeNummern(
							hmArtikel.get(rePos[i].getArtikelIId()),
							rePos[i].getSeriennrChargennrMitMenge(),
							theClientDto);
					rows += iAnzahlSerienChargeNummern;
					// Optional: Leerzeilen nach jeder mengenbehafteten Position
					if (bLeerzeileNachMengenbehafteterPosition) {
						rows++;
					}
				} else if (rePos[i].getRechnungpositionartCNr().equals(
						RechnungFac.POSITIONSART_RECHNUNG_HANDEINGABE)) {
					// jede handeingabe hat ihren eigenen Handartikel -> 1 Zeile
					rows++;
					ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKey(rePos[i].getArtikelIId(),
									theClientDto);
					hmArtikel.put(rePos[i].getArtikelIId(), artikelDto);
					// Optional: Leerzeilen nach jeder mengenbehafteten Position
					if (bLeerzeileNachMengenbehafteterPosition) {
						rows++;
					}
				} else if (rePos[i].getRechnungpositionartCNr().equals(
						RechnungFac.POSITIONSART_RECHNUNG_SEITENUMBRUCH)) {
					// Der Seitenumbruch wird ueber ein Toggle-Flag gesteuert ->
					// braucht keine zeile
				} else {
					// jede andere position braucht nur eine zeile
					rows++;
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		// laufzeiten werden mitgeloggt
		myLogger.logData("daten holen dauerte "
				+ (System.currentTimeMillis() - lStartzeit) + " ms.",
				theClientDto.getIDUser());
		return rows;
	}

	private int addPositionToDataPosition(RechnungDto rechnungDto,
			RechnungPositionDto rePos, Map<?, MwstsatzReportDto> mwstMap,
			int index, boolean bDruckeRabatt, Boolean bSeitenumbruch,
			TheClientDto theClientDto) throws EJBExceptionLP {
		data[index][RECHNUNG_FELD_POSITION_NR] = iArtikelpositionsnummer;
		data[index][RECHNUNG_FELD_B_SEITENUMBRUCH] = bSeitenumbruch;
		// weiterzaehlen
		iArtikelpositionsnummer = new Integer(
				iArtikelpositionsnummer.intValue() + 1);
		try {
			// Positionsnummer
			data[index][RECHNUNG_FELD_POSITION] = getRechnungFac()
					.getPositionNummer(rePos.getIId());
			data[index][RECHNUNG_FELD_MENGE] = new BigDecimal(1);
			data[index][RECHNUNG_FELD_EINHEIT] = SystemFac.EINHEIT_STUECK
					.trim();
			data[index][RECHNUNG_FELD_ZUSATZRABATT] = new Float(0);
			data[index][RECHNUNG_FELD_RABATT] = new Float(0);
			data[index][RECHNUNG_FELD_POSITIONSART] = LocaleFac.POSITIONSART_IDENT;
			String sIdent = null;
			// Druckdaten zusammenstellen
			if (rePos.getCBez() != null)
				sIdent = rePos.getCBez();
			else
				sIdent = "";
			data[index][RECHNUNG_FELD_IDENTNUMMER] = sIdent;
			data[index][RECHNUNG_FELD_IDENT] = sIdent;

			BigDecimal bdNettogesamtpreisplusversteckteraufschlagminusrabatte = getRechnungFac()
					.getGesamtpreisPosition(rePos.getIId(), theClientDto);
			data[index][RECHNUNG_FELD_EINZELPREIS] = bdNettogesamtpreisplusversteckteraufschlagminusrabatte;
			data[index][RECHNUNG_FELD_GESAMTPREIS] = bdNettogesamtpreisplusversteckteraufschlagminusrabatte;
			RechnungPositionDto first = getRechnungFac()
					.rechnungPositionFindPositionIIdISort(rePos.getIId(),
							rePos.getISort() + 1);
			Integer mwstsatzIId = first.getMwstsatzIId();
			if (mwstsatzIId != null) {
				MwstsatzDto mwstREPos = getMandantFac()
						.mwstsatzFindByPrimaryKey(mwstsatzIId, theClientDto);
				bdNettogesamtpreisplusversteckteraufschlagminusrabatte = Helper
						.rundeKaufmaennisch(
								bdNettogesamtpreisplusversteckteraufschlagminusrabatte,
								2);
				BigDecimal bdLSUst = bdNettogesamtpreisplusversteckteraufschlagminusrabatte
						.multiply(new BigDecimal(mwstREPos.getFMwstsatz()
								.doubleValue() / 100.0));
				data[index][RECHNUNG_FELD_MWSTSATZ] = mwstREPos.getFMwstsatz();
				data[index][RECHNUNG_FELD_MWSTBETRAG] = bdLSUst;
				MwstsatzReportDto m = mwstMap.get(mwstsatzIId);
				m.setNSummeMwstbetrag(m.getNSummeMwstbetrag().add(bdLSUst));
				m.setNSummePositionsbetrag(m.getNSummePositionsbetrag().add(
						bdNettogesamtpreisplusversteckteraufschlagminusrabatte));
			}
			Session session = null;
			try {
				if (rePos.getTypCNr().equals(LocaleFac.POSITIONTYP_MITPREISE)) {
					List<?> l = null;
					String sArtikelInfo = new String();
					SessionFactory factory = FLRSessionFactory.getFactory();
					session = factory.openSession();
					Criteria crit = session
							.createCriteria(FLRRechnungPosition.class);
					crit.add(Restrictions.eq("position_i_id", rePos.getIId()));
					crit.addOrder(Order
							.asc(RechnungFac.FLR_RECHNUNGPOSITION_I_SORT));
					l = crit.list();
					Iterator<?> iter = l.iterator();
					while (iter.hasNext()) {
						index++;
						FLRRechnungPosition pos = (FLRRechnungPosition) iter
								.next();
						if (pos.getPositionsart_c_nr().equals(
								LocaleFac.POSITIONSART_IDENT))
							sArtikelInfo = pos.getFlrartikel().getC_nr();
						else if (pos.getPositionsart_c_nr().equals(
								LocaleFac.POSITIONSART_TEXTEINGABE))
							sArtikelInfo = pos.getX_textinhalt();
						else if (pos.getPositionsart_c_nr().equals(
								LocaleFac.POSITIONSART_HANDEINGABE))
							sArtikelInfo = pos.getC_bez();
						// Druckdaten zusammenstellen
						data[index][RECHNUNG_FELD_IDENTNUMMER] = sArtikelInfo;
						data[index][RECHNUNG_FELD_IDENT] = sArtikelInfo;
						// weitere Daten
						data[index][RECHNUNG_FELD_MENGE] = pos.getN_menge();
						data[index][RECHNUNG_FELD_EINHEIT] = pos
								.getEinheit_c_nr();
						data[index][RECHNUNG_FELD_ZUSATZRABATT] = new Float(0);
						data[index][RECHNUNG_FELD_RABATT] = new Float(0);
						data[index][RECHNUNG_FELD_POSITIONSART] = LocaleFac.POSITIONSART_IDENT;
						data[index][RECHNUNG_FELD_EINZELPREIS] = pos
								.getN_einzelpreis();
						data[index][RECHNUNG_FELD_GESAMTPREIS] = pos
								.getN_menge().multiply(pos.getN_einzelpreis());
						data[index][RECHNUNG_FELD_TYP_CNR] = pos.getTyp_c_nr();
						data[index][RECHNUNG_FELD_B_SEITENUMBRUCH] = bSeitenumbruch;
					}
				} else {
					List<?> l = null;
					StringBuffer sbArtikelInfo = new StringBuffer();
					SessionFactory factory = FLRSessionFactory.getFactory();
					session = factory.openSession();
					Criteria crit = session
							.createCriteria(FLRRechnungPosition.class);
					crit.add(Restrictions.eq("position_i_id", rePos.getIId()));
					crit.addOrder(Order
							.asc(RechnungFac.FLR_RECHNUNGPOSITION_I_SORT));
					l = crit.list();
					Iterator<?> iter = l.iterator();
					while (iter.hasNext()) {
						FLRRechnungPosition pos = (FLRRechnungPosition) iter
								.next();
						if (pos.getPositionsart_c_nr().equals(
								LocaleFac.POSITIONSART_IDENT)) {
							sbArtikelInfo.append(pos.getFlrartikel().getC_nr());
							sbArtikelInfo.append("  ");
							if (pos.getFlrartikel().getFlrartikelgruppe() != null)
								sbArtikelInfo.append(pos.getFlrartikel()
										.getFlrartikelgruppe().getC_nr());
							if (iter.hasNext()) {
								sbArtikelInfo.append("\n");
							}
						} else if (pos.getPositionsart_c_nr().equals(
								LocaleFac.POSITIONSART_TEXTEINGABE)) {
							sbArtikelInfo.append(pos.getX_textinhalt());
							if (iter.hasNext()) {
								sbArtikelInfo.append("\n");
							}
						}
						if (pos.getPositionsart_c_nr().equals(
								LocaleFac.POSITIONSART_HANDEINGABE)) {
							if (pos.getC_bez() != null) {
								sbArtikelInfo.append(pos.getC_bez());
								if (iter.hasNext()) {
									sbArtikelInfo.append("\n");
								}
							}
						}
					}
					// Druckdaten zusammenstellen
					data[index][RECHNUNG_FELD_IDENTNUMMER] = sbArtikelInfo
							.toString();
					data[index][RECHNUNG_FELD_IDENT] = sbArtikelInfo.toString();

				}

			} finally {
				if (session != null) {
					session.close();
				}
			}
		} catch (RemoteException ex) {
		} catch (EJBExceptionLP ex) {
		}

		return index;
	}

	private int addPositionToDataIdentHandeingabe(RechnungDto rechnungDto,
			RechnungPositionDto rePos, boolean bDruckeRabatt,
			Map<?, MwstsatzReportDto> mwstMap,
			HashMap<?, ArtikelDto> hmArtikel,
			HashMap<?, StuecklisteInfoDto> hmStklPos, Boolean bSeitenumbruch,
			int index, Locale locale,
			boolean bLeerzeileNachMengenbehafteterPosition,
			boolean bChargennummerBeinhaltetMindesthaltbarkeitsdatumI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			// Flag Seitenumbruch
			data[index][RECHNUNG_FELD_B_SEITENUMBRUCH] = bSeitenumbruch;
			// Positionsnummer
			data[index][RECHNUNG_FELD_POSITION] = getRechnungFac()
					.getPositionNummer(rePos.getIId());
			data[index][RECHNUNG_FELD_POSITION_NR] = iArtikelpositionsnummer;
			// weiterzaehlen
			iArtikelpositionsnummer = new Integer(
					iArtikelpositionsnummer.intValue() + 1);
			ArtikelDto oArtikelDto = hmArtikel.get(rePos.getArtikelIId());
			// Druckdaten zusammenstellen
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
					rechnungDto.getKundeIId(), theClientDto);

			data[index][RECHNUNG_FELD_LVPOSITION] = rePos.getCLvposition();

			BelegPositionDruckIdentDto druckDto = printIdent(rePos,
					LocaleFac.BELEGART_RECHNUNG, oArtikelDto, locale,
					kundeDto.getPartnerIId(), theClientDto);
			data[index][RECHNUNG_FELD_IDENTNUMMER] = druckDto.getSIdentnummer();
			data[index][RECHNUNG_FELD_WARENVERKEHRSNUMMER] = druckDto
					.getSWarenverkehrsnummer();
			data[index][RECHNUNG_FELD_POSITIONSOBJEKT] = getSystemReportFac()
					.getPositionForReport(LocaleFac.BELEGART_RECHNUNG,
							rePos.getIId(), theClientDto);
			data[index][RechnungReportFacBean.RECHNUNG_FELD_IMAGE] = druckDto
					.getOImageKommentar();

			data[index][RechnungReportFacBean.RECHNUNG_FELD_WE_REFERENZ] = getLagerFac()
					.getWareneingangsreferenzSubreport(
							LocaleFac.BELEGART_RECHNUNG, rePos.getIId(),
							rePos.getSeriennrChargennrMitMenge(), false,
							theClientDto);

			if (oArtikelDto.getMaterialIId() != null) {
				MaterialDto materialDto = getMaterialFac()
						.materialFindByPrimaryKey(oArtikelDto.getMaterialIId(),
								locale, theClientDto);
				if (materialDto.getMaterialsprDto() != null) {
					/**
					 * @todo MR->MR richtige Mehrsprachigkeit: Material in
					 *       Drucksprache.
					 */
					data[index][RECHNUNG_FELD_ARTIKEL_MATERIAL] = materialDto
							.getMaterialsprDto().getCBez();
				} else {
					data[index][RECHNUNG_FELD_ARTIKEL_MATERIAL] = materialDto
							.getCNr();
				}
				data[index][RECHNUNG_FELD_ARTIKEL_KURS_MATERIALZUSCHLAG] = rePos
						.getNMaterialzuschlagKurs();
				data[index][RECHNUNG_FELD_ARTIKEL_DATUM_MATERIALZUSCHLAG] = rePos
						.getTMaterialzuschlagDatum();

			}

			data[index][RECHNUNG_FELD_ARTIKEL_MATERIALGEWICHT] = oArtikelDto
					.getFMaterialgewicht();

			// KundeArtikelnr gueltig zu Belegdatum
			KundesokoDto kundeSokoDto_gueltig = this.getKundesokoFac()
					.kundesokoFindByKundeIIdArtikelIIdGueltigkeitsdatumOhneExc(
							rechnungDto.getKundeIId(),
							oArtikelDto.getIId(),
							new java.sql.Date(rechnungDto.getTBelegdatum()
									.getTime()));
			if (kundeSokoDto_gueltig != null) {
				data[index][RechnungReportFacBean.RECHNUNG_FELD_KUNDEARTIKELNR] = kundeSokoDto_gueltig
						.getCKundeartikelnummer();
			}

			data[index][RECHNUNG_FELD_BEZEICHNUNG] = druckDto.getSBezeichnung();
			data[index][RECHNUNG_FELD_KURZBEZEICHNUNG] = druckDto
					.getSKurzbezeichnung();
			data[index][RECHNUNG_FELD_IDENT] = druckDto.getSArtikelInfo();
			data[index][RECHNUNG_FELD_IDENT_TEXTEINGABE] = druckDto
					.getSIdentTexteingabe();
			data[index][RECHNUNG_FELD_ZUSATZBEZEICHNUNG] = druckDto
					.getSZusatzBezeichnung();
			data[index][RECHNUNG_FELD_ARTIKELKOMMENTAR] = druckDto
					.getSArtikelkommentar();
			data[index][RECHNUNG_FELD_FREIERTEXT] = rePos.getXTextinhalt();
			// weitere Daten
			data[index][RECHNUNG_FELD_MENGE] = Helper.rundeKaufmaennisch(
					rePos.getNMenge(),
					getMandantFac().getNachkommastellenMenge(
							theClientDto.getMandant()));
			data[index][RECHNUNG_FELD_EINHEIT] = getSystemFac().formatEinheit(
					rePos.getEinheitCNr(), locale, theClientDto);
			data[index][RECHNUNG_FELD_ARTIKEL_VERPACKUNGSMENGE] = oArtikelDto
					.getFVerpackungsmenge();
			data[index][RECHNUNG_FELD_ARTIKEL_WERBEABGABEPFLICHTIG] = Helper
					.short2Boolean(oArtikelDto.getBWerbeabgabepflichtig());
			data[index][RECHNUNG_FELD_POSITIONSART] = rePos
					.getRechnungpositionartCNr();
			data[index][RECHNUNG_FELD_ARTIKEL_VERKAUFSEANNR] = druckDto
					.getSVerkaufseannr();
			data[index][RECHNUNG_FELD_ARTIKEL_VERPACKUNGSEANNR] = druckDto
					.getSVerpackungseannr();
			data[index][RECHNUNG_FELD_REFERENZNUMMER] = druckDto
					.getSReferenznr();

			data[index][RECHNUNG_FELD_INSERAT_DTO] = getInseratFac()
					.istIseratAufRechnungspositionVorhanden(rePos.getIId());

			// PJ 15348
			if (rePos.getMwstsatzIId() != null) {

				MwstsatzDto mwstSatzDto = getMandantFac()
						.mwstsatzFindByPrimaryKey(rePos.getMwstsatzIId(),
								theClientDto);
				data[index][RECHNUNG_FELD_FIBU_MWST_CODE] = mwstSatzDto
						.getIFibumwstcode();

			}

			if (rePos.getPositioniIdArtikelset() != null) {

				data[index][RECHNUNG_FELD_SETARTIKEL_TYP] = ArtikelFac.SETARTIKEL_TYP_POSITION;

			} else {

				Session session = null;
				SessionFactory factory = FLRSessionFactory.getFactory();
				session = factory.openSession();
				Criteria crit = session
						.createCriteria(FLRRechnungPosition.class);
				crit.add(Restrictions.eq("position_i_id_artikelset",
						rePos.getIId()));

				int iZeilen = crit.list().size();

				if (iZeilen > 0) {
					data[index][RECHNUNG_FELD_SETARTIKEL_TYP] = ArtikelFac.SETARTIKEL_TYP_KOPF;
				}

			}

			if (oArtikelDto.getArtikelsprDto() != null) {
				if (oArtikelDto.getArtikelsprDto().getCZbez2() != null) {
					data[index][RECHNUNG_FELD_ARTIKELCZBEZ2] = oArtikelDto
							.getArtikelsprDto().getCZbez2();
				}
			}

			int iNachkommastellenPreis = getUINachkommastellenPreisVK(rechnungDto
					.getMandantCNr());
			RechnungPositionDto rechnungPositionDto = (RechnungPositionDto) getBelegVerkaufFac()
					.getBelegpositionVerkaufReport(rePos, rechnungDto,
							iNachkommastellenPreis);
			// Bei Setartikelpositionen Preise auslassen
			if (rechnungPositionDto.getPositioniIdArtikelset() == null) {

				if (bDruckeRabatt) {
					data[index][RECHNUNG_FELD_EINZELPREIS] = rechnungPositionDto
							.getNReportEinzelpreisplusversteckteraufschlag();
					data[index][RECHNUNG_FELD_RABATT] = new Float(
							rechnungPositionDto.getDReportRabattsatz());
					data[index][RECHNUNG_FELD_ZUSATZRABATT] = new Float(
							rechnungPositionDto.getDReportZusatzrabattsatz());
				} else {
					data[index][RECHNUNG_FELD_EINZELPREIS] = rechnungPositionDto
							.getNReportNettoeinzelpreisplusversteckteraufschlag();
					data[index][RECHNUNG_FELD_RABATT] = new Float(0);
					data[index][RECHNUNG_FELD_ZUSATZRABATT] = new Float(0);
				}

				data[index][RECHNUNG_FELD_GESAMTPREIS] = rechnungPositionDto
						.getNReportGesamtpreis();
				data[index][RECHNUNG_FELD_MATERIALZUSCHLAG] = rechnungPositionDto
						.getNMaterialzuschlag();
				data[index][RECHNUNG_FELD_MWSTSATZ] = rechnungPositionDto
						.getDReportMwstsatz();
				data[index][RECHNUNG_FELD_MWSTBETRAG] = Helper
						.rundeGeldbetrag(rechnungPositionDto
								.getNReportMwstsatzbetrag());
				MwstsatzReportDto m = mwstMap.get(rechnungPositionDto
						.getMwstsatzIId());
				m.setNSummeMwstbetrag(m.getNSummeMwstbetrag().add(
						rechnungPositionDto.getNReportMwstsatzbetrag()));
				m.setNSummePositionsbetrag(m.getNSummePositionsbetrag().add(
						rechnungPositionDto.getNReportGesamtpreis()));
			}

			/*
			 * // ohne Rabatt wird der einzelpreis vor rabattabzug gedruckt if
			 * (bDruckeRabatt) { data[index][RECHNUNG_FELD_EINZELPREIS] = rePos.
			 * getNEinzelpreisplusversteckteraufschlag(); } // mit Rabatt sofort
			 * der Nettopreis else { data[index][RECHNUNG_FELD_EINZELPREIS] =
			 * rePos. getNNettoeinzelpreisplusversteckteraufschlag(); } // der
			 * Rabattsatz wird immer uebergeben
			 * data[index][RECHNUNG_FELD_RABATT] = new
			 * Float(rePos.getFRabattsatz()); if (rePos.getFZusatzrabattsatz()
			 * != null) { data[index][RECHNUNG_FELD_ZUSATZRABATT] = new
			 * Float(rePos.getFZusatzrabattsatz()); } else {
			 * data[index][RECHNUNG_FELD_ZUSATZRABATT] = new Float(0); } //
			 * Gesamtpreis: Nettopreis X Menge BigDecimal bdGesamtpreis = null;
			 * if (rePos.getNNettoeinzelpreisplusversteckteraufschlag() != null)
			 * { if (rePos.getNMenge() != null) { bdGesamtpreis =
			 * rePos.getNNettoeinzelpreisplusversteckteraufschlag().multiply(
			 * rePos. getNMenge()); } } data[index][RECHNUNG_FELD_GESAMTPREIS] =
			 * bdGesamtpreis;
			 * 
			 * if (rePos.getMwstsatzIId() != null &&
			 * !Helper.short2boolean(rechnungDto.getBReversecharge())) { //
			 * Umsatzsteuerbetrag berechnen MwstsatzDto mwst =
			 * getMandantFac().mwstsatzFindByPrimaryKey(rePos.getMwstsatzIId(),
			 * theClientDto); data[index][RECHNUNG_FELD_MWSTSATZ] =
			 * mwst.getFMwstsatz(); // vom Gesamtpreis muss noch der allgemeine
			 * Rabatt abgezogen werden double dAllgRabatt; if
			 * (rechnungDto.getFAllgemeinerRabattsatz() != null) { dAllgRabatt =
			 * rechnungDto.getFAllgemeinerRabattsatz().doubleValue(); } else {
			 * dAllgRabatt = 0.0; } BigDecimal bdGesamtpreisMinusAufschlag =
			 * bdGesamtpreis.multiply(new BigDecimal( 1.0 - (dAllgRabatt /
			 * 100.0))); BigDecimal ust =
			 * Helper.getProzentWert(bdGesamtpreisMinusAufschlag, new
			 * Float(mwst.getFMwstsatz().floatValue()), 2);
			 * data[index][RECHNUNG_FELD_MWSTBETRAG] = ust; MwstsatzReportDto m
			 * = ( (MwstsatzReportDto) mwstMap.get(rePos.getMwstsatzIId()));
			 * m.setNSummeMwstbetrag(m.getNSummeMwstbetrag().add(ust));
			 * m.setNSummePositionsbetrag(m.getNSummePositionsbetrag().add(
			 * bdGesamtpreisMinusAufschlag)); }
			 */
			// eventuell zu druckende stuecklistenpositionen drucken
			if (rePos.getRechnungpositionartCNr().equals(
					RechnungFac.POSITIONSART_RECHNUNG_IDENT)) {
				StuecklisteInfoDto stklPos = hmStklPos.get(rePos
						.getArtikelIId());
				if (stklPos != null) {
					ArrayList<?> aListe = stklPos.getAlStuecklisteAufgeloest();
					for (Iterator<?> iter = aListe.iterator(); iter.hasNext();) {
						index++;
						StuecklisteMitStrukturDto stuecklisteMitStrukturDto = (StuecklisteMitStrukturDto) iter
								.next();
						data[index][RECHNUNG_FELD_POSITIONSART] = LocaleFac.POSITIONSART_STUECKLISTENPOSITION;
						data[index][RECHNUNG_FELD_B_SEITENUMBRUCH] = bSeitenumbruch;

						StuecklistepositionDto stuecklistepositionDto = stuecklisteMitStrukturDto
								.getStuecklistepositionDto();

						data[index][RECHNUNG_FELD_STKLMENGE] = stuecklistepositionDto
								.getNMenge();
						data[index][RECHNUNG_FELD_STKLEINHEIT] = getSystemFac()
								.formatEinheit(
										stuecklistepositionDto.getEinheitCNr(),
										locale, theClientDto);

						// Einrueckung fuer Unterstuecklisten
						String einrueckung = "";
						for (int j = 0; j < stuecklisteMitStrukturDto
								.getIEbene(); j++) {
							einrueckung = einrueckung + "    ";
						}
						data[index][RECHNUNG_FELD_STKLARTIKELCNR] = einrueckung
								+ stuecklistepositionDto.getArtikelDto()
										.getCNr();
						data[index][RECHNUNG_FELD_STKLARTIKELBEZ] = getArtikelFac()
								.baueArtikelBezeichnungMehrzeilig(
										stuecklistepositionDto.getArtikelIId(),
										LocaleFac.POSITIONSART_IDENT, null,
										null, false, locale, theClientDto);

						if (stuecklistepositionDto.getArtikelIId() != null) {

							ArtikelDto artikelDtoStkl = getArtikelFac()
									.artikelFindByPrimaryKeySmall(
											stuecklistepositionDto
													.getArtikelIId(),
											theClientDto);

							data[index][RECHNUNG_FELD_STKLARTIKELKBEZ] = artikelDtoStkl
									.getKbezAusSpr();
						}

						// KundeArtikelnr gueltig zu Belegdatum
						KundesokoDto kundeSokoDto_gueltigStkl = this
								.getKundesokoFac()
								.kundesokoFindByKundeIIdArtikelIIdGueltigkeitsdatumOhneExc(
										rechnungDto.getKundeIId(),
										stuecklistepositionDto.getArtikelIId(),
										new java.sql.Date(rechnungDto
												.getTBelegdatum().getTime()));
						if (kundeSokoDto_gueltigStkl != null) {
							data[index][RECHNUNG_FELD_STKLARTIKEL_KDARTIKELNR] = kundeSokoDto_gueltigStkl
									.getCKundeartikelnummer();
						}

						// PJ18038
						VkpreisfindungDto vkpreisfindungDto = getVkPreisfindungFac()
								.verkaufspreisfindung(
										stuecklistepositionDto.getArtikelIId(),
										rechnungDto.getKundeIId(),
										stuecklistepositionDto.getNMenge(),
										new Date(rechnungDto.getTBelegdatum()
												.getTime()),
										kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
										rechnungPositionDto.getMwstsatzIId(),
										theClientDto.getSMandantenwaehrung(),
										theClientDto);

						VerkaufspreisDto kundenVKPreisDto = Helper
								.getVkpreisBerechnet(vkpreisfindungDto);
						if (kundenVKPreisDto != null) {
							data[index][RECHNUNG_FELD_STKLARTIKEL_KDPREIS] = kundenVKPreisDto.nettopreis;
						}

					}
				}
				/**
				 * SerienChargenummer fuer Artikel als eigene Pos drucken
				 */
				if (Helper.short2Boolean(oArtikelDto.getBSeriennrtragend())) {
					index++;
					data[index][RECHNUNG_FELD_SERIENCHARGENR] = SeriennrChargennrMitMengeDto
							.erstelleStringAusMehrerenSeriennummern(rePos
									.getSeriennrChargennrMitMenge());
					data[index][RECHNUNG_FELD_VERSION] = SeriennrChargennrMitMengeDto
							.erstelleStringAusMehrerenVersionen(rePos
									.getSeriennrChargennrMitMenge());
					data[index][RECHNUNG_FELD_POSITIONSART] = RechnungFac.POSITIONSART_RECHNUNG_SERIENNR;
					data[index][RECHNUNG_FELD_B_SEITENUMBRUCH] = bSeitenumbruch;
				} else if (Helper.short2Boolean(oArtikelDto
						.getBChargennrtragend())) {
					// Chargennummer splitten in eigene Positionen
					String[] aChargeNummer = SeriennrChargennrMitMengeDto
							.erstelleStringArrayAusMehrerenSeriennummern(rePos
									.getSeriennrChargennrMitMenge());
					for (int k = 0; k < aChargeNummer.length; k++) {
						String sChargeNummer = aChargeNummer[k];
						index++;
						if (bChargennummerBeinhaltetMindesthaltbarkeitsdatumI) {
							// MHD als eigenes Feld falls vorhanden
							String[] aChargenrMHD = Helper
									.zerlegeChargennummerMHD(sChargeNummer);
							if (aChargenrMHD != null) {
								data[index][RECHNUNG_FELD_SERIENCHARGENR] = aChargenrMHD[0];
								if (aChargenrMHD.length > 1) {
									data[index][RECHNUNG_FELD_MINDESTHALTBARKEIT] = aChargenrMHD[1];
								}
							}
						} else {
							data[index][RECHNUNG_FELD_SERIENCHARGENR] = sChargeNummer;
						}

						data[index][RECHNUNG_FELD_POSITIONSART] = RechnungFac.POSITIONSART_RECHNUNG_CHARGENR;
						data[index][RECHNUNG_FELD_B_SEITENUMBRUCH] = bSeitenumbruch;
						// Menge dieser Charge holen,nur wenn mehrere
						// Chargennummer vorhanden sind
						if (aChargeNummer.length > 1) {
							// alle Chargennummern der Position
							List<SeriennrChargennrMitMengeDto> aChargenMitMengeDto = getLagerFac()
									.getAllSeriennrchargennrEinerBelegartpositionOhneChargeneigenschaften(
											LocaleFac.BELEGART_RECHNUNG,
											rePos.getIId());

							if (aChargenMitMengeDto != null) {
								for (int u = 0; u < aChargenMitMengeDto.size(); u++) {
									SeriennrChargennrMitMengeDto chargenMitMengeDto = aChargenMitMengeDto
											.get(u);
									// Menge der aktuellen Chargenr holen
									if (sChargeNummer.equals(chargenMitMengeDto
											.getCSeriennrChargennr())) {
										data[index][RECHNUNG_FELD_SERIENCHARGENR_MENGE] = chargenMitMengeDto
												.getNMenge();
									}
								}
							}

						}
					}

				}

				if (oArtikelDto.getVerpackungDto() != null) {
					data[index][RECHNUNG_FELD_BAUFORM] = oArtikelDto
							.getVerpackungDto().getCBauform();
					data[index][RECHNUNG_FELD_VERPACKUNGSART] = oArtikelDto
							.getVerpackungDto().getCVerpackungsart();
				}

				// PJ 15926
				if (rePos.getVerleihIId() != null) {
					VerleihDto verleihDto = getArtikelFac()
							.verleihFindByPrimaryKey(rePos.getVerleihIId());
					data[index][RECHNUNG_FELD_VERLEIHFAKTOR] = verleihDto
							.getFFaktor();
					data[index][RECHNUNG_FELD_VERLEIHTAGE] = verleihDto
							.getITage();
				}

				data[index][RECHNUNG_FELD_ARTIKEL_INDEX] = oArtikelDto
						.getCIndex();

				data[index][RECHNUNG_FELD_ARTIKEL_REVISION] = oArtikelDto
						.getCRevision();
				data[index][RECHNUNG_FELD_GEWICHT] = oArtikelDto
						.getFGewichtkg();

				if (oArtikelDto.getGeometrieDto() != null) {
					data[index][RECHNUNG_FELD_ARTIKEL_BREITE] = oArtikelDto
							.getGeometrieDto().getFBreite();
					data[index][RECHNUNG_FELD_ARTIKEL_HOEHE] = oArtikelDto
							.getGeometrieDto().getFHoehe();
					data[index][RECHNUNG_FELD_ARTIKEL_TIEFE] = oArtikelDto
							.getGeometrieDto().getFTiefe();
				}

				// Ursprungsland
				if (oArtikelDto.getLandIIdUrsprungsland() != null) {
					LandDto landDto = getSystemFac().landFindByPrimaryKey(
							oArtikelDto.getLandIIdUrsprungsland());
					data[index][RECHNUNG_FELD_ARTIKEL_URSPRUNGSLAND] = landDto
							.getCName().toUpperCase();
				}
				// Artikelgruppe
				if ((Boolean) getParameterFac().getMandantparameter(
						theClientDto.getMandant(),
						ParameterFac.KATEGORIE_RECHNUNG,
						ParameterFac.PARAMETER_RECHNUNG_ARTIKELGRUPPE_DRUCKEN)
						.getCWertAsObject()) {
					if (oArtikelDto.getArtgruIId() != null) {
						ArtgruDto artgruDto = getArtikelFac()
								.artgruFindByPrimaryKey(
										oArtikelDto.getArtgruIId(),
										theClientDto);
						// String sHelper = (String)
						// data[index][RECHNUNG_FELD_IDENT];
						// String sBegin = sHelper.substring(0,
						// sHelper.indexOf("\n"));
						// String sEnd =
						// sHelper.substring(sHelper.indexOf("\n"));
						data[index][RECHNUNG_FELD_ARTIKEL_ARTIKELGRUPPE] = data[index][RECHNUNG_FELD_ARTIKEL_ARTIKELGRUPPE] == null ? artgruDto
								.getCNr() + "\n"
								: data[index][RECHNUNG_FELD_ARTIKEL_ARTIKELGRUPPE]
										+ artgruDto.getCNr() + "\n";
						// data[index][RECHNUNG_FELD_IDENT] =sBegin + "  " + +
						// sEnd;
					}
				}
			}

			data[index][RECHNUNG_FELD_ERLOESKONTO_DTO] = getFibuExportManger(
					theClientDto).getErloeskonto(rechnungDto.getIId(), rePos);
			index++;
			// Optional: Leerzeile nach mengenbehafteter Position
			if (bLeerzeileNachMengenbehafteterPosition
					&& rePos.getNMenge() != null) {
				data[index] = getLeerzeile(bSeitenumbruch);
				// damit trotz eingefuegter Leerzeile keine neue
				// Auftragsueberschrift kommt.
				data[index][RECHNUNG_FELD_AUFTRAG_NUMMER] = data[index - 1][RECHNUNG_FELD_AUFTRAG_NUMMER];
				index++;
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return index;
	}

	private int addPositionToDataLieferschein(RechnungDto rechnungDto,
			RechnungPositionDto rePos, boolean bDruckeRabatt,
			Map<?, MwstsatzReportDto> mwstMap,
			HashMap<?, LieferscheinDto> hmLSDto,
			HashMap<?, LieferscheinpositionDto[]> hmLSPosDto,
			HashMap<?, Object[][]> hmLSData, Locale locale,
			Boolean bSeitenumbruch, int index,
			boolean bLeerzeileNachMengenbehafteterPosition,
			boolean bChargennummerBeinhaltetMindesthaltbarkeitsdatumI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			// Den Lieferschein und seine Positionen aus dem Cache holen
			LieferscheinDto lieferscheinDto = hmLSDto.get(rePos
					.getLieferscheinIId());
			LieferscheinpositionDto[] lsPos = hmLSPosDto.get(rePos
					.getLieferscheinIId());

			String ansprechpartner = null;
			String ansp_fremdsystem = null;
			if (lieferscheinDto.getAnsprechpartnerIId() != null) {
				AnsprechpartnerDto anspDto = getAnsprechpartnerFac()
						.ansprechpartnerFindByPrimaryKey(
								lieferscheinDto.getAnsprechpartnerIId(),
								theClientDto);

				ansprechpartner = anspDto.getPartnerDto().formatAnrede();
				ansp_fremdsystem = anspDto.getCFremdsystemnr();

			}

			Object[][] lsData = hmLSData.get(lieferscheinDto.getIId());
			// am Druck gibt es eine "Ueberschrift"
			StringBuffer sbArtikelInfo = new StringBuffer();

			sbArtikelInfo.append(getTextRespectUISpr(
					"rechnung.unserlieferschein", theClientDto.getMandant(),
					locale)
					+ " ");
			sbArtikelInfo.append(lieferscheinDto.getCNr());
			sbArtikelInfo.append(" "
					+ getTextRespectUISpr("lp.vom", theClientDto.getMandant(),
							locale) + " ");
			sbArtikelInfo.append(Helper.formatDatum(
					lieferscheinDto.getTBelegdatum(), locale));
			data[index][RECHNUNG_FELD_POSITIONSART] = RechnungFac.POSITIONSART_RECHNUNG_LS_BEGINN;
			data[index][RECHNUNG_FELD_FREIERTEXT] = sbArtikelInfo.toString();
			data[index][RECHNUNG_FELD_B_SEITENUMBRUCH] = bSeitenumbruch;
			data[index][RECHNUNG_FELD_LS_ANSPRECHPARTNER] = ansprechpartner;
			data[index][RECHNUNG_FELD_LS_ANSPRECHPARTNER_FREMDSYSTEMNUMMER] = ansp_fremdsystem;
			data[index][RECHNUNG_FELD_LS_KOMMISIONSNUMMER] = lieferscheinDto
					.getCKommission();

			String rechnungMitDerVerrechnet = null;
			if (rechnungDto.getRechnungartCNr().equals(
					RechnungFac.RECHNUNGART_PROFORMARECHNUNG)) {

				javax.persistence.Query query = em
						.createNamedQuery("RechnungPositionfindByLieferscheinIId");
				query.setParameter(1, lieferscheinDto.getIId());
				Collection<?> cl = query.getResultList();
				if (cl.size() != 0) {

					Iterator<?> iterator = cl.iterator();
					while (iterator.hasNext()) {
						Rechnungposition rechnungPosition = (Rechnungposition) iterator
								.next();

						Rechnung rechnungMitDerBereitsVerrechnet = em.find(
								Rechnung.class,
								rechnungPosition.getRechnungIId());

						if (rechnungMitDerBereitsVerrechnet.getRechnungartCNr()
								.equals(RechnungFac.RECHNUNGART_RECHNUNG)) {
							rechnungMitDerVerrechnet = rechnungMitDerBereitsVerrechnet
									.getCNr();
							data[index][RECHNUNG_FELD_LS_VERRECHNET_MIT] = rechnungMitDerVerrechnet;
						}

					}

				}
			}

			// Pj 15202
			data[index][RECHNUNG_FELD_LS_BESTELLNUMMER] = lieferscheinDto
					.getCBestellnummer();

			Boolean bRechnungsadresseGleichLieferadresse = Boolean.FALSE;
			if (lieferscheinDto.getKundeIIdRechnungsadresse().equals(
					lieferscheinDto.getKundeIIdLieferadresse())) {
				bRechnungsadresseGleichLieferadresse = Boolean.TRUE;
			}
			data[index][RECHNUNG_FELD_LS_RECHNUNGSADRESSEGLEICHLIEFERADRESSE] = bRechnungsadresseGleichLieferadresse;

			data[index][RECHNUNG_FELD_LS_PROJEKT] = lieferscheinDto
					.getCBezProjektbezeichnung();
			// Waehrungen pruefen
			String sRechnungswaehrung = rechnungDto.getWaehrungCNr();
			String sLieferscheinwaehrung = lieferscheinDto.getWaehrungCNr();
			boolean bUmrechnen = false;
			if (!sRechnungswaehrung.equals(sLieferscheinwaehrung)) {
				bUmrechnen = true;
			}
			int lsIndex = 0;
			index++;
			for (int i = 0; i < lsData.length; i++) {
				// die einfachen Felder koennen direkt uebernommen werden.
				data[index][RECHNUNG_FELD_LEERZEILE] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_LEERZEILE];
				data[index][RECHNUNG_FELD_EINHEIT] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_EINHEIT];
				data[index][RECHNUNG_FELD_POSITIONSOBJEKT] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_POSITIONSOBJEKT];
				data[index][RECHNUNG_FELD_FREIERTEXT] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_FREIERTEXT];
				data[index][RECHNUNG_FELD_IDENT] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_IDENT];

				data[index][RECHNUNG_FELD_IDENT_TEXTEINGABE] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_IDENT_TEXTEINGABE];
				data[index][RECHNUNG_FELD_ARTIKEL_ARTIKELGRUPPE] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_ARTIKEL_ARTIKELGRUPPE];
				data[index][RECHNUNG_FELD_IDENTNUMMER] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_IDENTNUMMER];
				data[index][RECHNUNG_FELD_BEZEICHNUNG] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_BEZEICHNUNG];
				data[index][RECHNUNG_FELD_LVPOSITION] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_LV_POSITION];
				data[index][RECHNUNG_FELD_ZUSATZBEZEICHNUNG] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_ZUSATZBEZEICHNUNG];
				data[index][RECHNUNG_FELD_KURZBEZEICHNUNG] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_KURZBEZEICHNUNG];
				data[index][RECHNUNG_FELD_IMAGE] = Helper
						.byteArrayToImage((byte[]) lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_IMAGE]);
				data[index][RECHNUNG_FELD_LAGER_UEBERSTEUERT_AUS_LIEFERSCHEIN] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_LAGER_UEBERSTEUERT];
				data[index][RECHNUNG_FELD_ARTIKELKOMMENTAR] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_ARTIKELKOMMENTAR];
				data[index][RECHNUNG_FELD_POSITIONSART] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_POSITIONSART];
				data[index][RECHNUNG_FELD_FIBU_MWST_CODE] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_FIBU_MWST_CODE];
				data[index][RECHNUNG_FELD_MENGE] = Helper
						.rundeKaufmaennisch(
								(BigDecimal) (lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_MENGE]),
								getMandantFac().getNachkommastellenMenge(
										theClientDto.getMandant()));
				data[index][RECHNUNG_FELD_STKLARTIKELBEZ] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_STKLARTIKELBEZ];
				data[index][RECHNUNG_FELD_STKLARTIKELKBEZ] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_STKLARTIKELKBEZ];
				data[index][RECHNUNG_FELD_STKLARTIKEL_KDARTIKELNR] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_STKLARTIKEL_KDARTIKELNR];
				data[index][RECHNUNG_FELD_STKLARTIKEL_KDPREIS] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_STKLARTIKEL_KDPREIS];
				data[index][RECHNUNG_FELD_STKLARTIKELCNR] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_STKLARTIKELCNR];
				data[index][RECHNUNG_FELD_STKLEINHEIT] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_STKLEINHEIT];
				data[index][RECHNUNG_FELD_STKLMENGE] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_STKLMENGE];
				// die Auftragsdaten der LS-Position
				data[index][RECHNUNG_FELD_AUFTRAG_BESTELLDATUM] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_AUFTRAG_BESTELLDATUM];
				data[index][RECHNUNG_FELD_AUFTRAG_BESTELLNUMMER] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_AUFTRAG_BESTELLNUMMER];
				data[index][RECHNUNG_FELD_AUFTRAG_NUMMER] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_AUFTRAG_NUMMER];
				data[index][RECHNUNG_FELD_AUFTRAG_PROJEKT] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_AUFTRAG_PROJEKT];
				// Seitenumbrueche im Lieferschein werden ignoriert
				data[index][RECHNUNG_FELD_B_SEITENUMBRUCH] = bSeitenumbruch;
				data[index][RECHNUNG_FELD_ARTIKEL_VERPACKUNGSMENGE] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_ARTIKEL_VERPACKUNGSMENGE];
				data[index][RECHNUNG_FELD_ARTIKEL_WERBEABGABEPFLICHTIG] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_ARTIKEL_WERBEABGABEPFLICHTIG];
				data[index][RECHNUNG_FELD_ARTIKEL_VERPACKUNGSEANNR] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_ARTIKEL_VERPACKUNGSEANNR];
				data[index][RECHNUNG_FELD_ARTIKEL_VERKAUFSEANNR] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_ARTIKEL_VERKAUFSEANNR];
				data[index][RECHNUNG_FELD_REFERENZNUMMER] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_REFERENZNUMMER];
				data[index][RECHNUNG_FELD_ARTIKELCZBEZ2] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_ARTIKELCZBEZ2];
				data[index][RECHNUNG_FELD_WARENVERKEHRSNUMMER] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_WARENVERKEHRSNUMMER];
				data[index][RECHNUNG_FELD_ARTIKEL_MATERIAL] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_ARTIKEL_MATERIAL];
				data[index][RECHNUNG_FELD_ARTIKEL_MATERIALGEWICHT] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_ARTIKEL_MATERIALGEWICHT];
				data[index][RECHNUNG_FELD_ARTIKEL_KURS_MATERIALZUSCHLAG] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_ARTIKEL_KURS_MATERIALZUSCHLAG];
				data[index][RECHNUNG_FELD_ARTIKEL_DATUM_MATERIALZUSCHLAG] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_ARTIKEL_DATUM_MATERIALZUSCHLAG];
				data[index][RECHNUNG_FELD_ARTIKEL_BREITE] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_ARTIKEL_BREITE];
				data[index][RECHNUNG_FELD_ARTIKEL_HOEHE] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_ARTIKEL_HOEHE];
				data[index][RECHNUNG_FELD_ARTIKEL_TIEFE] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_ARTIKEL_TIEFE];
				data[index][RECHNUNG_FELD_BAUFORM] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_BAUFORM];
				data[index][RECHNUNG_FELD_TYP_CNR] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_TYP_CNR];
				data[index][RECHNUNG_FELD_VERPACKUNGSART] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_VERPACKUNGSART];
				data[index][RECHNUNG_FELD_ARTIKEL_URSPRUNGSLAND] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_ARTIKEL_URSPRUNGSLAND];
				data[index][RECHNUNG_FELD_SETARTIKEL_TYP] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_SETARTIKEL_TYP];
				data[index][RECHNUNG_FELD_ARTIKEL_INDEX] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_ARTIKEL_INDEX];
				data[index][RECHNUNG_FELD_ARTIKEL_REVISION] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_ARTIKEL_REVISION];
				data[index][RECHNUNG_FELD_GEWICHT] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_ARTIKEL_GEWICHT];
				data[index][RECHNUNG_FELD_VERLEIHFAKTOR] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_VERLEIHFAKTOR];
				data[index][RECHNUNG_FELD_VERLEIHTAGE] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_VERLEIHTAGE];
				data[index][RECHNUNG_FELD_VERSION] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_VERSION];

				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
						lieferscheinDto.getKundeIIdLieferadresse(),
						theClientDto);
				data[index][RECHNUNG_FELD_LS_FILIALNR] = kundeDto
						.getPartnerDto().getCFilialnummer();
				data[index][RECHNUNG_FELD_LS_KOMMISIONSNUMMER] = lieferscheinDto
						.getCKommission();
				data[index][RECHNUNG_FELD_LS_VERRECHNET_MIT] = rechnungMitDerVerrechnet;

				// Pj 15202
				data[index][RECHNUNG_FELD_LS_BESTELLNUMMER] = lieferscheinDto
						.getCBestellnummer();

				data[index][RECHNUNG_FELD_LS_RECHNUNGSADRESSEGLEICHLIEFERADRESSE] = bRechnungsadresseGleichLieferadresse;

				data[index][RECHNUNG_FELD_LS_PROJEKT] = lieferscheinDto
						.getCBezProjektbezeichnung();
				KostenstelleDto kstDto = getSystemFac()
						.kostenstelleFindByPrimaryKey(
								lieferscheinDto.getKostenstelleIId());
				data[index][RECHNUNG_FELD_LS_KOSTENSTELLE] = kstDto.getCNr();

				LagerDto lDto = getLagerFac().lagerFindByPrimaryKey(
						lieferscheinDto.getLagerIId());

				data[index][RECHNUNG_FELD_LS_ABBUCHUNGSLAGER] = lDto.getCNr();

				data[index][RECHNUNG_FELD_LS_ANSPRECHPARTNER] = ansprechpartner;
				data[index][RECHNUNG_FELD_LS_ANSPRECHPARTNER_FREMDSYSTEMNUMMER] = ansp_fremdsystem;
				/**
				 * Seriennr Position
				 */
				if (lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_POSITIONSART]
						.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_SERIENNR)) {
					// Seriennr
					data[index][RECHNUNG_FELD_SERIENCHARGENR] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_SERIENCHARGENR];
					data[index][RECHNUNG_FELD_POSITIONSART] = RechnungFac.POSITIONSART_RECHNUNG_SERIENNR;
				}

				// Chargennr Position
				if (lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_POSITIONSART]
						.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_CHARGENR)) {
					// Chargennr
					data[index][RECHNUNG_FELD_SERIENCHARGENR] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_SERIENCHARGENR];
					data[index][RECHNUNG_FELD_MINDESTHALTBARKEIT] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_MINDESTHALTBARKEIT];
					data[index][RECHNUNG_FELD_POSITIONSART] = RechnungFac.POSITIONSART_RECHNUNG_CHARGENR;
					// Menge nur bei Chargennummer
					data[index][RECHNUNG_FELD_SERIENCHARGENR_MENGE] = lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_SERIENCHARGENR_MENGE];
				}
				// hier beginnt die preisberechnung,
				// stuecklistenpositionen,Auftragsdaten und
				// SerienChargepositionen sind davon ausgeschlossen
				if (!lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_POSITIONSART]
						.equals(LocaleFac.POSITIONSART_STUECKLISTENPOSITION)
						&& !lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_POSITIONSART]
								.equals(LocaleFac.POSITIONSART_AUFTRAGSDATEN)
						&& !lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_POSITIONSART]
								.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_SERIENNR)
						&& !lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_POSITIONSART]
								.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_CHARGENR)) {

					data[index][RECHNUNG_FELD_INTERNAL_IID] = lsPos[lsIndex]
							.getIId();

					// auch beim Lieferschein gelten die Positionsnummern nur
					// fuer Ident und Handeingabe
					if (lsPos[lsIndex]
							.getLieferscheinpositionartCNr()
							.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)
							|| lsPos[lsIndex]
									.getLieferscheinpositionartCNr()
									.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_HANDEINGABE)
							|| lsPos[lsIndex].getLieferscheinpositionartCNr()
									.equals(LocaleFac.POSITIONSART_POSITION)) {
						Integer nr = null;
						if (lsPos[lsIndex].getAuftragpositionIId() != null) {
							nr = getAuftragpositionFac().getPositionNummer(
									lsPos[lsIndex].getAuftragpositionIId());
						} else {
							if (lieferscheinDto.getAuftragIId() == null)
								nr = getLieferscheinpositionFac()
										.getPositionNummer(
												lsPos[lsIndex].getIId());
						}
						data[index][RECHNUNG_FELD_POSITION] = nr;
						data[index][RECHNUNG_FELD_POSITION_NR] = iArtikelpositionsnummer;
						iArtikelpositionsnummer = new Integer(
								iArtikelpositionsnummer.intValue() + 1);

						data[index][RechnungReportFacBean.RECHNUNG_FELD_WE_REFERENZ] = getLagerFac()
								.getWareneingangsreferenzSubreport(
										LocaleFac.BELEGART_LIEFERSCHEIN,
										lsPos[lsIndex].getIId(),
										lsPos[lsIndex]
												.getSeriennrChargennrMitMenge(),
										false, theClientDto);

					}

					// nur preisbehaftete Positionen
					if (lsPos[lsIndex].getNEinzelpreis() != null) {
						// Einzelpreis berechnen
						// PJ 09/0014325
						/*
						 * BigDecimal bdEinzelpreis =getRechnungFac().
						 * berechneEinzelpreisLSPositionOhneLSRabatt(
						 * rechnungDto, lieferscheinDto, lsPos[lsIndex],
						 * !bDruckeRabatt, theClientDto);
						 * if(Helper.short2boolean
						 * (lsPos[lsIndex].getBNettopreisuebersteuert()))
						 * bdEinzelpreis =
						 * lsPos[lsIndex].getNNettoeinzelpreis(); BigDecimal
						 * bdNettopreis =
						 * getRechnungFac().berechneNettopreisLSPositionOhneLSRabatt
						 * ( rechnungDto, lieferscheinDto, lsPos[lsIndex],
						 * theClientDto);
						 * if(Helper.short2boolean(lsPos[lsIndex].
						 * getBNettopreisuebersteuert())) bdNettopreis =
						 * lsPos[lsIndex].getNNettoeinzelpreis(); BigDecimal
						 * bdGesamtpreis =
						 * bdNettopreis.multiply(lsPos[lsIndex].getNMenge()); if
						 * (lsPos[lsIndex].getMwstsatzIId() != null &&
						 * !Helper.short2boolean
						 * (rechnungDto.getBReversecharge())) { // allgemeinen
						 * rabatt abziehen double dAllgRabatt; if
						 * (rechnungDto.getFAllgemeinerRabattsatz() != null) {
						 * dAllgRabatt =
						 * rechnungDto.getFAllgemeinerRabattsatz().
						 * doubleValue(); } else { dAllgRabatt = 0.0; }
						 * BigDecimal bdAllgRabatt =
						 * Helper.getProzentWert(bdGesamtpreis, new
						 * Float(dAllgRabatt), 2); BigDecimal
						 * bdGesamtNettoEndpreis =
						 * bdGesamtpreis;//.subtract(bdAllgRabatt);
						 * 
						 * MwstsatzDto mwstLSPos =
						 * getMandantFac().mwstsatzFindByPrimaryKey
						 * (lsPos[lsIndex].getMwstsatzIId(), theClientDto);
						 * BigDecimal bdLSUst =
						 * bdGesamtNettoEndpreis.multiply(new BigDecimal(
						 * mwstLSPos.getFMwstsatz().doubleValue() / 100.0));
						 * data[index][RECHNUNG_FELD_MWSTSATZ] =
						 * mwstLSPos.getFMwstsatz();
						 * data[index][RECHNUNG_FELD_MWSTBETRAG] = bdLSUst;
						 * MwstsatzReportDto m = ( (MwstsatzReportDto)
						 * mwstMap.get(lsPos[lsIndex]. getMwstsatzIId()));
						 * m.setNSummeMwstbetrag
						 * (m.getNSummeMwstbetrag().add(bdLSUst));
						 * m.setNSummePositionsbetrag
						 * (m.getNSummePositionsbetrag().add(
						 * bdGesamtNettoEndpreis)); }
						 */
						int iNachkommastellenPreis = getUINachkommastellenPreisVK(rechnungDto
								.getMandantCNr());
						LieferscheinpositionDto lieferscheinpositionDto = (LieferscheinpositionDto) getBelegVerkaufFac()
								.getBelegpositionVerkaufReport(lsPos[lsIndex],
										lieferscheinDto, iNachkommastellenPreis);

						// Bei Setartikelpositionen Preise auslassen
						if (lieferscheinpositionDto.getPositioniIdArtikelset() == null) {

							if (bDruckeRabatt) {
								data[index][RECHNUNG_FELD_EINZELPREIS] = lieferscheinpositionDto
										.getNReportEinzelpreisplusversteckteraufschlag();
								data[index][RECHNUNG_FELD_RABATT] = new Float(
										lieferscheinpositionDto
												.getDReportRabattsatz());
								data[index][RECHNUNG_FELD_ZUSATZRABATT] = new Float(
										lieferscheinpositionDto
												.getDReportZusatzrabattsatz());
							} else {
								data[index][RECHNUNG_FELD_EINZELPREIS] = lieferscheinpositionDto
										.getNReportNettoeinzelpreisplusversteckteraufschlag();
								data[index][RECHNUNG_FELD_RABATT] = new Float(0);
								data[index][RECHNUNG_FELD_ZUSATZRABATT] = new Float(
										0);
							}
							data[index][RECHNUNG_FELD_GESAMTPREIS] = lieferscheinpositionDto
									.getNReportGesamtpreis();
							data[index][RECHNUNG_FELD_MATERIALZUSCHLAG] = lieferscheinpositionDto
									.getNMaterialzuschlag();

							data[index][RECHNUNG_FELD_MWSTSATZ] = lieferscheinpositionDto
									.getDReportMwstsatz();
							data[index][RECHNUNG_FELD_MWSTBETRAG] = lieferscheinpositionDto
									.getNMwstbetrag();
							MwstsatzReportDto m = mwstMap
									.get(lieferscheinpositionDto
											.getMwstsatzIId());
							m.setNSummeMwstbetrag(m.getNSummeMwstbetrag().add(
									lieferscheinpositionDto
											.getNReportMwstsatzbetrag()));
							m.setNSummePositionsbetrag(m
									.getNSummePositionsbetrag().add(
											lieferscheinpositionDto
													.getNReportGesamtpreis()));
						}
					} else {
						if (lsPos[lsIndex].getPositionsartCNr().equals(
								LocaleFac.POSITIONSART_POSITION)) {
							if (lsPos[lsIndex].getTypCNr().equals(
									LocaleFac.POSITIONTYP_MITPREISE)) {
								BigDecimal bdGesamtNettoEndpreis = getLieferscheinpositionFac()
										.getGesamtpreisPosition(
												lsPos[lsIndex].getIId(),
												theClientDto);
								data[index][RECHNUNG_FELD_EINZELPREIS] = bdGesamtNettoEndpreis;
								data[index][RECHNUNG_FELD_GESAMTPREIS] = bdGesamtNettoEndpreis;
								data[index][RECHNUNG_FELD_RABATT] = new Float(0);
								data[index][RECHNUNG_FELD_ZUSATZRABATT] = new Float(
										0);
								Integer mwstsatzIId = null;
								if (lsPos[lsIndex].getAuftragpositionIId() != null) {
									LieferscheinpositionDto lsp = getLieferscheinpositionFac()
											.lieferscheinpositionFindPositionIIdISort(
													lsPos[lsIndex].getIId(),
													lsPos[lsIndex].getISort() + 1);
									mwstsatzIId = lsp.getMwstsatzIId();
									if (mwstsatzIId != null) {
										MwstsatzDto mwstLSPos = getMandantFac()
												.mwstsatzFindByPrimaryKey(
														mwstsatzIId,
														theClientDto);
										BigDecimal bdLSUst = bdGesamtNettoEndpreis
												.multiply(new BigDecimal(
														mwstLSPos
																.getFMwstsatz()
																.doubleValue() / 100.0));
										data[index][RECHNUNG_FELD_MWSTSATZ] = mwstLSPos
												.getFMwstsatz();
										data[index][RECHNUNG_FELD_MWSTBETRAG] = bdLSUst;
										MwstsatzReportDto m = mwstMap
												.get(mwstsatzIId);
										m.setNSummeMwstbetrag(m
												.getNSummeMwstbetrag().add(
														bdLSUst));
										m.setNSummePositionsbetrag(m
												.getNSummePositionsbetrag()
												.add(bdGesamtNettoEndpreis));
									}
								}

								Session session = null;
								List<?> l = null;
								String sArtikelInfo = new String();
								SessionFactory factory = FLRSessionFactory
										.getFactory();
								session = factory.openSession();
								Criteria crit = session
										.createCriteria(FLRLieferscheinposition.class);
								crit.add(Restrictions.eq("position_i_id",
										lsPos[lsIndex].getIId()));
								crit.addOrder(Order
										.asc(RechnungFac.FLR_RECHNUNGPOSITION_I_SORT));
								l = crit.list();
								Iterator<?> iter = l.iterator();
								while (iter.hasNext()) {
									index++;
									i++;
									FLRLieferscheinposition pos = (FLRLieferscheinposition) iter
											.next();
									if (pos.getPositionsart_c_nr().equals(
											LocaleFac.POSITIONSART_IDENT))
										sArtikelInfo = pos.getFlrartikel()
												.getC_nr();
									else if (pos.getPositionsart_c_nr().equals(
											LocaleFac.POSITIONSART_TEXTEINGABE))
										sArtikelInfo = pos.getC_textinhalt();
									else if (pos.getPositionsart_c_nr().equals(
											LocaleFac.POSITIONSART_HANDEINGABE))
										sArtikelInfo = pos.getC_bez();
									// Druckdaten zusammenstellen
									data[index][RECHNUNG_FELD_IDENTNUMMER] = sArtikelInfo;
									data[index][RECHNUNG_FELD_IDENT] = sArtikelInfo;
									// weitere Daten
									data[index][RECHNUNG_FELD_MENGE] = pos
											.getN_menge();
									data[index][RECHNUNG_FELD_EINHEIT] = pos
											.getEinheit_c_nr();
									data[index][RECHNUNG_FELD_ZUSATZRABATT] = new Float(
											0);
									data[index][RECHNUNG_FELD_RABATT] = new Float(
											0);
									data[index][RECHNUNG_FELD_B_SEITENUMBRUCH] = new Boolean(
											false);
									data[index][RECHNUNG_FELD_POSITIONSART] = LocaleFac.POSITIONSART_IDENT;
									data[index][RECHNUNG_FELD_EINZELPREIS] = pos
											.getN_nettogesamtpreis();
									if (pos.getN_menge() != null
											&& pos.getN_nettogesamtpreis() != null)
										data[index][RECHNUNG_FELD_GESAMTPREIS] = pos
												.getN_menge()
												.multiply(
														pos.getN_nettogesamtpreis());
									data[index][RECHNUNG_FELD_TYP_CNR] = pos
											.getTyp_c_nr();

								}

							} else {
								BigDecimal bdGesamtNettoEndpreis = getLieferscheinpositionFac()
										.getGesamtpreisPosition(
												lsPos[lsIndex].getIId(),
												theClientDto);
								data[index][RECHNUNG_FELD_EINZELPREIS] = bdGesamtNettoEndpreis;
								data[index][RECHNUNG_FELD_GESAMTPREIS] = bdGesamtNettoEndpreis;
								data[index][RECHNUNG_FELD_RABATT] = new Float(0);
								data[index][RECHNUNG_FELD_ZUSATZRABATT] = new Float(
										0);
								Integer mwstsatzIId = null;
								if (lsPos[lsIndex].getAuftragpositionIId() != null) {
									LieferscheinpositionDto lsp = getLieferscheinpositionFac()
											.lieferscheinpositionFindPositionIIdISort(
													lsPos[lsIndex].getIId(),
													lsPos[lsIndex].getISort() + 1);
									mwstsatzIId = lsp.getMwstsatzIId();
									if (mwstsatzIId != null) {
										MwstsatzDto mwstLSPos = getMandantFac()
												.mwstsatzFindByPrimaryKey(
														mwstsatzIId,
														theClientDto);
										BigDecimal bdLSUst = bdGesamtNettoEndpreis
												.multiply(new BigDecimal(
														mwstLSPos
																.getFMwstsatz()
																.doubleValue() / 100.0));
										data[index][RECHNUNG_FELD_MWSTSATZ] = mwstLSPos
												.getFMwstsatz();
										data[index][RECHNUNG_FELD_MWSTBETRAG] = bdLSUst;
										MwstsatzReportDto m = mwstMap
												.get(mwstsatzIId);
										m.setNSummeMwstbetrag(m
												.getNSummeMwstbetrag().add(
														bdLSUst));
										m.setNSummePositionsbetrag(m
												.getNSummePositionsbetrag()
												.add(bdGesamtNettoEndpreis));
									}
								}

							}
						}
					}
					if (lsPos[lsIndex]
							.getLieferscheinpositionartCNr()
							.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)) {
						// KundeArtikelnr gueltig zu Belegdatum
						KundesokoDto kundeSokoDto_gueltig = this
								.getKundesokoFac()
								.kundesokoFindByKundeIIdArtikelIIdGueltigkeitsdatumOhneExc(
										rechnungDto.getKundeIId(),
										lsPos[lsIndex].getArtikelIId(),
										new java.sql.Date(rechnungDto
												.getTBelegdatum().getTime()));
						if (kundeSokoDto_gueltig != null) {
							data[index][RechnungReportFacBean.RECHNUNG_FELD_KUNDEARTIKELNR] = kundeSokoDto_gueltig
									.getCKundeartikelnummer();
						}
					}

					if (lsPos[lsIndex]
							.getLieferscheinpositionartCNr()
							.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_INTELLIGENTE_ZWISCHENSUMME)) {
						data[index][RECHNUNG_FELD_VONPOSITION] = getLieferscheinpositionFac()
								.getLSPositionNummer(
										lsPos[lsIndex].getZwsVonPosition());
						data[index][RECHNUNG_FELD_BISPOSITION] = getLieferscheinpositionFac()
								.getLSPositionNummer(
										lsPos[lsIndex].getZwsBisPosition());
						data[index][RECHNUNG_FELD_ZWSNETTOSUMME] = lsPos[lsIndex]
								.getZwsNettoSumme();
						data[index][RECHNUNG_FELD_ZWSPOSPREISDRUCKEN] = lsPos[lsIndex]
								.getBZwsPositionspreisZeigen();
						if (data[index][RECHNUNG_FELD_VONPOSITION] != null) {
							// updateZwischensummenData(index,
							// lsPos[lsIndex].getZwsVonPosition(),
							// lsPos[lsIndex].getCBez());
							updateZwischensummenData(index, lsPos[lsIndex]);
						}
						data[index][RECHNUNG_FELD_POSITION] = getLieferscheinpositionFac()
								.getLSPositionNummer(lsPos[lsIndex].getIId());
					}

					data[index][RECHNUNG_FELD_ERLOESKONTO_DTO] = getFibuExportManger(
							theClientDto).getErloeskonto(rechnungDto.getIId(),
							lsPos[lsIndex]);

					// fuer alle nicht-stuecklistenpositionen auch bei den
					// LS-Pos. eins weiter
					lsIndex++;
				}
				index++;
				// Optional: Leerzeile nach mengenbehafteter Position
				if (bLeerzeileNachMengenbehafteterPosition
						&& lsData[i][LieferscheinReportFac.REPORT_LIEFERSCHEIN_MENGE] != null) {
					data[index] = getLeerzeile(bSeitenumbruch);
					// damit trotz eingefuegter Leerzeile keine neue
					// Auftragsueberschrift kommt.
					data[index][RECHNUNG_FELD_AUFTRAG_NUMMER] = data[index - 1][RECHNUNG_FELD_AUFTRAG_NUMMER];
					index++;
				}
			}
			// Fusszeile des Lieferscheins
			sbArtikelInfo = new StringBuffer();
			sbArtikelInfo.append(getTextRespectUISpr(
					"rechnung.endelieferschein", theClientDto.getMandant(),
					locale)
					+ " ");
			sbArtikelInfo.append(lieferscheinDto.getCNr());
			data[index][RECHNUNG_FELD_POSITIONSART] = RechnungFac.POSITIONSART_RECHNUNG_LS_ENDE;
			data[index][RECHNUNG_FELD_FREIERTEXT] = sbArtikelInfo.toString();
			data[index][RECHNUNG_FELD_B_SEITENUMBRUCH] = bSeitenumbruch;
			data[index][RECHNUNG_FELD_LS_ANSPRECHPARTNER] = ansprechpartner;
			index++;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return index;
	}

	private int addPositionToDataIntelligenteZwischensumme(
			RechnungDto rechnungDto, RechnungPositionDto rePos,
			boolean bDruckeRabatt, Map<?, MwstsatzReportDto> mwstMap,
			HashMap<?, ArtikelDto> hmArtikel,
			HashMap<?, StuecklisteInfoDto> hmStklPos, Boolean bSeitenumbruch,
			int index, Locale locale,
			boolean bLeerzeileNachMengenbehafteterPosition,
			boolean bChargennummerBeinhaltetMindesthaltbarkeitsdatumI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			data[index][RECHNUNG_FELD_INTERNAL_IID] = rePos.getIId();

			// Flag Seitenumbruch
			data[index][RECHNUNG_FELD_B_SEITENUMBRUCH] = bSeitenumbruch;
			// Positionsnummer
			data[index][RECHNUNG_FELD_POSITION] = getRechnungFac()
					.getPositionNummer(rePos.getIId());
			data[index][RECHNUNG_FELD_POSITION_NR] = iArtikelpositionsnummer;
			// weiterzaehlen
			iArtikelpositionsnummer = new Integer(
					iArtikelpositionsnummer.intValue() + 1);

			// Druckdaten zusammenstellen
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
					rechnungDto.getKundeIId(), theClientDto);

			data[index][RECHNUNG_FELD_BEZEICHNUNG] = rePos.getCBez();
			data[index][RECHNUNG_FELD_KURZBEZEICHNUNG] = "";
			data[index][RECHNUNG_FELD_IDENT] = rePos.getCBez();
			data[index][RECHNUNG_FELD_IDENT_TEXTEINGABE] = "";
			data[index][RECHNUNG_FELD_ZUSATZBEZEICHNUNG] = "";
			data[index][RECHNUNG_FELD_ARTIKELKOMMENTAR] = "";

			// weitere Daten
			// data[index][RECHNUNG_FELD_MENGE] = Helper.rundeKaufmaennisch(
			// rePos.getNMenge(),
			// getMandantFac().getNachkommastellenMenge(
			// theClientDto.getMandant()));
			data[index][RECHNUNG_FELD_EINHEIT] = getSystemFac().formatEinheit(
					rePos.getEinheitCNr(), locale, theClientDto);
			data[index][RECHNUNG_FELD_ARTIKEL_VERPACKUNGSMENGE] = 0.0;
			data[index][RECHNUNG_FELD_POSITIONSART] = rePos
					.getRechnungpositionartCNr();
			data[index][RECHNUNG_FELD_ARTIKEL_VERKAUFSEANNR] = "";
			data[index][RECHNUNG_FELD_ARTIKEL_VERPACKUNGSEANNR] = "";
			data[index][RECHNUNG_FELD_REFERENZNUMMER] = "";

			// PJ 15348
			if (rePos.getMwstsatzIId() != null) {
				MwstsatzDto mwstSatzDto = getMandantFac()
						.mwstsatzFindByPrimaryKey(rePos.getMwstsatzIId(),
								theClientDto);
				data[index][RECHNUNG_FELD_FIBU_MWST_CODE] = mwstSatzDto
						.getIFibumwstcode();
			}

			int iNachkommastellenPreis = getUINachkommastellenPreisVK(rechnungDto
					.getMandantCNr());
			RechnungPositionDto rechnungPositionDto = (RechnungPositionDto) getBelegVerkaufFac()
					.getBelegpositionVerkaufReport(rePos, rechnungDto,
							iNachkommastellenPreis);

			if (bDruckeRabatt) {
				data[index][RECHNUNG_FELD_EINZELPREIS] = rechnungPositionDto
						.getNReportEinzelpreisplusversteckteraufschlag();
			} else {
				data[index][RECHNUNG_FELD_EINZELPREIS] = rechnungPositionDto
						.getNReportNettoeinzelpreisplusversteckteraufschlag();
			}
			data[index][RECHNUNG_FELD_RABATT] = new Float(
					rechnungPositionDto.getDReportRabattsatz());
			data[index][RECHNUNG_FELD_ZUSATZRABATT] = new Float(
					rechnungPositionDto.getDReportZusatzrabattsatz());
			data[index][RECHNUNG_FELD_GESAMTPREIS] = rechnungPositionDto
					.getNReportGesamtpreis();
			data[index][RECHNUNG_FELD_MWSTSATZ] = rechnungPositionDto
					.getDReportMwstsatz();
			data[index][RECHNUNG_FELD_MWSTBETRAG] = Helper
					.rundeGeldbetrag(rechnungPositionDto
							.getNReportMwstsatzbetrag());
			data[index][RECHNUNG_FELD_VONPOSITION] = getRechnungFac()
					.getPositionNummer(rePos.getZwsVonPosition());
			data[index][RECHNUNG_FELD_BISPOSITION] = getRechnungFac()
					.getPositionNummer(rePos.getZwsBisPosition());
			data[index][RECHNUNG_FELD_ZWSNETTOSUMME] = rechnungPositionDto
					.getZwsNettoSumme();
			data[index][RECHNUNG_FELD_ZWSPOSPREISDRUCKEN] = rechnungPositionDto
					.getBZwsPositionspreisZeigen();
			if (data[index][RECHNUNG_FELD_VONPOSITION] != null) {
				// updateZwischensummenData(index, rePos.getZwsVonPosition(),
				// rePos.getCBez());
				updateZwischensummenData(index, rechnungPositionDto);
			}

			MwstsatzReportDto m = mwstMap.get(rechnungPositionDto
					.getMwstsatzIId());
			m.setNSummeMwstbetrag(m.getNSummeMwstbetrag().add(
					rechnungPositionDto.getNReportMwstsatzbetrag()));
			m.setNSummePositionsbetrag(m.getNSummePositionsbetrag().add(
					rechnungPositionDto.getNReportGesamtpreis()));

			index++;
			// Optional: Leerzeile nach mengenbehafteter Position
			if (bLeerzeileNachMengenbehafteterPosition
					&& rePos.getNMenge() != null) {
				data[index] = getLeerzeile(bSeitenumbruch);
				// damit trotz eingefuegter Leerzeile keine neue
				// Auftragsueberschrift kommt.
				data[index][RECHNUNG_FELD_AUFTRAG_NUMMER] = data[index - 1][RECHNUNG_FELD_AUFTRAG_NUMMER];
				index++;
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return index;
	}

	/**
	 * Update des Data-Arrays um die Zwischensummenbezeichnung in allen
	 * Positionen verf&uuml;gbar zu haben.
	 * 
	 * @param i
	 *            ist der Index an dem die Zwischensummenposition definiert ist
	 * @param zwsVonPosition
	 *            ist die IId der Von-Position
	 * @param zwsBisPosition
	 *            ist die IId der Bis-Position
	 * @param cBez
	 */
	private void updateZwischensummenDataOld(int lastIndex,
			Integer zwsVonPosition, Integer zwsBisPosition, String cBez) {
		boolean foundFrom = false;

		for (int i = 0; i < lastIndex; i++) {
			if (zwsVonPosition.equals(data[i][RECHNUNG_FELD_INTERNAL_IID])) {
				foundFrom = true;
			}

			if (foundFrom) {
				if (null == data[i][RECHNUNG_FELD_ZWSTEXT]) {
					data[i][RECHNUNG_FELD_ZWSTEXT] = cBez;
				} else {
					String s = (String) data[i][RECHNUNG_FELD_ZWSTEXT] + "\n"
							+ cBez;
					data[i][RECHNUNG_FELD_ZWSTEXT] = s;
				}
				if (zwsBisPosition.equals(data[i][RECHNUNG_FELD_INTERNAL_IID])) {
					return;
				}
			}
		}
	}

	private void updateZwischensummenData(int lastIndex,
			Integer zwsVonPosition, String cBez) {
		for (int i = 0; i < lastIndex; i++) {
			if (zwsVonPosition.equals(data[i][RECHNUNG_FELD_INTERNAL_IID])) {
				if (null == data[i][RECHNUNG_FELD_ZWSTEXT]) {
					data[i][RECHNUNG_FELD_ZWSTEXT] = cBez;
				} else {
					String s = (String) data[i][RECHNUNG_FELD_ZWSTEXT] + "\n"
							+ cBez;
					data[i][RECHNUNG_FELD_ZWSTEXT] = s;
				}
				return;
			}
		}
	}

	private void updateZwischensummenData(int lastIndex,
			BelegpositionVerkaufDto zwsPos) {
		Integer zwsVonPosition = zwsPos.getZwsVonPosition();
		if (zwsVonPosition == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_POSITION_ZWISCHENSUMME_UNVOLLSTAENDIG,
					"Position '" + zwsPos.getCBez() + "' unvollst\u00E4ndig",
					new Object[] { zwsPos.getCBez(), zwsPos.getIId() });
		}

		int foundIndex = -1;
		for (int i = 0; i < lastIndex; i++) {
			Object[] o = (Object[]) data[i];
			if (zwsVonPosition.equals(o[RECHNUNG_FELD_INTERNAL_IID])) {
				if (null == o[RECHNUNG_FELD_ZWSTEXT]) {
					o[RECHNUNG_FELD_ZWSTEXT] = zwsPos.getCBez();
				} else {
					String s = (String) o[RECHNUNG_FELD_ZWSTEXT] + "\n"
							+ zwsPos.getCBez();
					o[RECHNUNG_FELD_ZWSTEXT] = s;
				}

				o[RECHNUNG_FELD_ZWSNETTOSUMME] = zwsPos.getZwsNettoSumme();
				foundIndex = i;
				break;
			}
		}

		if (foundIndex == -1) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_POSITION_ZWISCHENSUMME_UNVOLLSTAENDIG,
					"Position '" + zwsPos.getCBez() + "' unvollst\u00E4ndig",
					new Object[] { zwsPos.getCBez(), zwsPos.getIId() });
		}

		for (int i = foundIndex; i < lastIndex; i++) {
			Object[] o = (Object[]) data[i];
			o[RECHNUNG_FELD_ZWSPOSPREISDRUCKEN] = zwsPos
					.getBZwsPositionspreisZeigen();
		}
	}

	private Object[] getLeerzeile(Boolean bSeitenumbruch) {
		Object[] oZeile = new Object[RechnungReportFacBean.RECHNUNG_ANZAHL_SPALTEN];
		oZeile[RECHNUNG_FELD_POSITIONSART] = RechnungFac.POSITIONSART_RECHNUNG_LEERZEILE;
		oZeile[RECHNUNG_FELD_LEERZEILE] = " ";
		oZeile[RECHNUNG_FELD_B_SEITENUMBRUCH] = bSeitenumbruch;
		return oZeile;
	}

	/**
	 * Fuer Serien- oder Chargennummerntragenden Artikel, die Anzahl der Serien
	 * oder Chargennummern berechnen.
	 * 
	 * @param artikelDto
	 *            ArtikelDto
	 * @param cSerienchargennummerI
	 *            String
	 * @param cNrUserI
	 *            String
	 * @return int
	 * @throws EJBExceptionLP
	 */
	private int berechneAnzahlSerienChargeNummern(ArtikelDto artikelDto,
			List<SeriennrChargennrMitMengeDto> list, TheClientDto theClientDto)
			throws EJBExceptionLP {
		int iAnzahl = 0;
		if (Helper.short2Boolean(artikelDto.getBChargennrtragend())) {
			if (list != null) {

				iAnzahl = list.size();
			}
		}
		if (Helper.short2Boolean(artikelDto.getBSeriennrtragend())) {
			if (list != null) {

				iAnzahl = 1;
			}
		}
		return iAnzahl;
	}

	public void erstelleEinzelexport(Integer rechnungIId, String pfad,
			boolean bSortiertNachArtikelnummer, TheClientDto theClientDto) {

		try {
			RechnungDto rechnungDto = getRechnungFac()
					.rechnungFindByPrimaryKey(rechnungIId);

			if (rechnungDto.getStatusCNr().equals(LocaleFac.STATUS_ANGELEGT)
					|| rechnungDto.getStatusCNr().equals(
							LocaleFac.STATUS_STORNIERT)) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
						new Exception(
								"Rechnung kann nicht exportiert werden, Status : "
										+ rechnungDto.getStatusCNr()));
			}

			if (rechnungDto.getTFibuuebernahme() == null) {

				JasperPrintLP[] report = null;

				if (bSortiertNachArtikelnummer) {
					report = buildRechnungReport(rechnungIId,
							"rech_rechnung_expo2.jasper",
							RechnungReportFac.REPORT_MODUL,
							theClientDto.getLocUi(), false, 0, null, true,
							theClientDto);
				} else {
					report = buildRechnungReport(rechnungIId,
							"rech_rechnung_expo.jasper",
							RechnungReportFac.REPORT_MODUL,
							theClientDto.getLocUi(), false, 0, null, false,
							theClientDto);
				}

				// JRCsvExporter exp=new JRCsvExporter();
				File reportFile = new File(pfad + "RE"
						+ rechnungDto.getCNr().replaceAll("/", "_") + ".csv");
				JRCsvExporter exporterCSV = new JRCsvExporter();
				exporterCSV.setParameter(JRExporterParameter.JASPER_PRINT,
						report[0].getPrint());

				String characterEncoding = "ISO-8859-1";
				exporterCSV.setParameter(
						JRExporterParameter.CHARACTER_ENCODING,
						characterEncoding);

				exporterCSV.setParameter(JRExporterParameter.OUTPUT_FILE,
						reportFile);

				javax.persistence.Query query = em
						.createNamedQuery("ExportlaufFindByTStichtagMandantCNr");
				query.setParameter(1, new Date(System.currentTimeMillis()));
				query.setParameter(2, theClientDto.getMandant());
				Collection<?> cl = query.getResultList();

				Integer exportlaufId = null;

				if (cl.size() > 0) {
					exportlaufId = ((Exportlauf) cl.iterator().next()).getIId();
				} else {
					ExportlaufDto exportlaufDto = new ExportlaufDto();
					exportlaufDto.setMandantCNr(theClientDto.getMandant());
					exportlaufDto.setPersonalIIdAendern(theClientDto
							.getIDPersonal());
					exportlaufDto.setTStichtag(Helper.cutDate(new Date(System
							.currentTimeMillis())));
					exportlaufDto = getFibuExportFac().createExportlauf(
							exportlaufDto);
					exportlaufId = exportlaufDto.getIId();
				}

				// Exportprotokoll
				ExportdatenDto exportdatenDto = new ExportdatenDto();
				exportdatenDto.setBelegartCNr(LocaleFac.BELEGART_RECHNUNG);
				exportdatenDto.setExportlaufIId(exportlaufId);
				exportdatenDto.setIBelegiid(rechnungDto.getIId());
				getFibuExportFac().createExportdaten(exportdatenDto);
				// AER-Status setzen
				getRechnungFac().setzeRechnungFibuUebernahme(
						rechnungDto.getIId(), theClientDto);

				try {
					exporterCSV.exportReport();
				} catch (JRException e) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
				}

			} else {

				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_BELEG_BEREITS_VERBUCHT,
						new Exception("FEHLER_FINANZ_BELEG_BEREITS_VERBUCHT"));

			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

	}

	public JasperPrintLP[] printRechnungZahlschein(Integer iRechnungIId,
			String sReportname, Integer iKopien, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Map<String, Object> mapParameter = new TreeMap<String, Object>();

		RechnungDto rechnungDto = getRechnungFac().rechnungFindByPrimaryKey(
				iRechnungIId);

		if (LocaleFac.STATUS_ANGELEGT.equals(rechnungDto.getStatusCNr())) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_RECHNUNG_NOCH_NICHT_AKTIVIERT,
					new Exception(
							"Status == Angelegt. Rechnung wurde noch nicht aktiviert"));
		}

		KostenstelleDto kostenstelleDto = getSystemFac()
				.kostenstelleFindByPrimaryKey(rechnungDto.getKostenstelleIId());
		KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
				rechnungDto.getKundeIId(), theClientDto);

		mapParameter.put(
				"P_BRUTTOBETRAG",
				rechnungDto.getNGesamtwertinbelegwaehrung().add(
						rechnungDto.getNWertustfw()));
		mapParameter.put("P_RECHNUNGSNUMMER", rechnungDto.getCNr());
		mapParameter.put("P_BESTELLNUMMER", rechnungDto.getCBestellnummer());
		mapParameter.put("P_RECHNUNGSDATUM", rechnungDto.getTBelegdatum());
		mapParameter.put("P_KOSTENSTELLENUMMER", kostenstelleDto.getCNr());
		mapParameter
				.put("P_KOSTENSTELLEBEZEICHNUNG", kostenstelleDto.getCBez());
		mapParameter.put("P_KUNDENNUMMER", kundeDto.getIKundennummer());
		mapParameter.put("P_DEBITORENKONTO",
				kundeDto.getIDebitorenkontoAsIntegerNotiId());
		if (rechnungDto.getZahlungszielIId() != null) {
			ZahlungszielDto zahlungszielDto = getMandantFac()
					.zahlungszielFindByPrimaryKey(
							rechnungDto.getZahlungszielIId(), theClientDto);
			mapParameter.put("P_SKONTO_BEZEICHNUNG", zahlungszielDto.getCBez());
			mapParameter.put("P_SKONTO_ZIELTAGE_NETTO",
					zahlungszielDto.getAnzahlZieltageFuerNetto());
			mapParameter.put("P_SKONTO1_ANZAHL_TAGE",
					zahlungszielDto.getSkontoAnzahlTage1());
			mapParameter.put("P_SKONTO2_ANZAHL_TAGE",
					zahlungszielDto.getSkontoAnzahlTage2());
			mapParameter.put("P_SKONTO1_PROZENT",
					zahlungszielDto.getSkontoProzentsatz1());
			mapParameter.put("P_SKONTO2_PROZENT",
					zahlungszielDto.getSkontoProzentsatz2());
			Timestamp tFaelligkeit = null;
			Timestamp tFaelligkeitSkonto1 = null;
			Timestamp tFaelligkeitSkonto2 = null;
			if (zahlungszielDto.getAnzahlZieltageFuerNetto() != null) {
				tFaelligkeit = new Timestamp(
						rechnungDto.getTBelegdatum().getTime()
								+ Helper.calculateDaysBackIntoMilliseconds(zahlungszielDto
										.getAnzahlZieltageFuerNetto()));
			}
			if (zahlungszielDto.getSkontoAnzahlTage1() != null) {
				tFaelligkeit = new Timestamp(
						rechnungDto.getTBelegdatum().getTime()
								+ Helper.calculateDaysBackIntoMilliseconds(zahlungszielDto
										.getSkontoAnzahlTage1()));
			}
			if (zahlungszielDto.getSkontoAnzahlTage2() != null) {
				tFaelligkeit = new Timestamp(
						rechnungDto.getTBelegdatum().getTime()
								+ Helper.calculateDaysBackIntoMilliseconds(zahlungszielDto
										.getSkontoAnzahlTage2()));
			}
			mapParameter.put("P_FAELLIGKEIT", tFaelligkeit);
			mapParameter.put("P_FAELLIGKEIT_SKONTO1", tFaelligkeitSkonto1);
			mapParameter.put("P_FAELLIGKEIT_SKONTO2", tFaelligkeitSkonto2);

		}

		this.useCase = UC_ZAHLSCHEIN;
		data = new Object[1][1];

		int iAnzahlExemplare = 1;

		if (iKopien != null && iKopien.intValue() > 0) {
			iAnzahlExemplare += iKopien.intValue();
		}
		JasperPrintLP[] aJasperPrint = null;
		aJasperPrint = new JasperPrintLP[iAnzahlExemplare];
		for (int iKopieNummer = 0; iKopieNummer < iAnzahlExemplare; iKopieNummer++) {
			if (iKopieNummer > 0) {
				mapParameter.put(LPReport.P_KOPIE_NUMMER, new Integer(
						iKopieNummer));
			}
			index = -1;
			initJRDS(mapParameter, RechnungReportFac.REPORT_MODUL, sReportname,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto, true, rechnungDto.getKostenstelleIId());
			aJasperPrint[iKopieNummer] = getReportPrint();

		}
		return aJasperPrint;
	}
}
