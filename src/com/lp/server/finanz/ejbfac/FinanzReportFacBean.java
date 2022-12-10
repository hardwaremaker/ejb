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
package com.lp.server.finanz.ejbfac;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.lp.layer.hibernate.HvLowerLikeExpression;
import com.lp.layer.hibernate.HvRTrimExpression;
import com.lp.layer.hibernate.HvRTrimLikeExpression;
import com.lp.layer.hibernate.HvTypedCriteria;
import com.lp.server.angebot.fastlanereader.generated.FLRAngebot;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.auftrag.ejb.Auftrag;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport;
import com.lp.server.auftrag.fastlanereader.generated.FLRZahlungsplan;
import com.lp.server.auftrag.fastlanereader.generated.FLRZahlungsplanmeilenstein;
import com.lp.server.auftrag.fastlanereader.generated.FLRZeitplan;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.bestellung.ejb.Bestellung;
import com.lp.server.bestellung.fastlanereader.generated.FLRBSZahlungsplan;
import com.lp.server.bestellung.fastlanereader.generated.FLRBestellposition;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.bestellung.service.WareneingangspositionDto;
import com.lp.server.eingangsrechnung.ejb.Eingangsrechnung;
import com.lp.server.eingangsrechnung.ejb.Eingangsrechnungzahlung;
import com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnung;
import com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnungReport;
import com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnungzahlung;
import com.lp.server.eingangsrechnung.fastlanereader.generated.FLRZahlungsvorschlag;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungzahlungDto;
import com.lp.server.eingangsrechnung.service.ErZahlungsempfaenger;
import com.lp.server.eingangsrechnung.service.ZahlungsvorschlaglaufDto;
import com.lp.server.fertigung.service.FertigungReportFac;
import com.lp.server.finanz.ejb.BuchungdetailText;
import com.lp.server.finanz.ejb.Finanzamt;
import com.lp.server.finanz.ejb.FinanzamtPK;
import com.lp.server.finanz.ejb.Konto;
import com.lp.server.finanz.ejb.Kontolaenderart;
import com.lp.server.finanz.ejb.KontolaenderartQuery;
import com.lp.server.finanz.ejb.Mahnspesen;
import com.lp.server.finanz.ejb.MahnstufePK;
import com.lp.server.finanz.ejb.Steuerkategorie;
import com.lp.server.finanz.ejb.Uvaart;
import com.lp.server.finanz.fastlanereader.generated.FLRFbbelegart;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzBuchung;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzBuchungDetail;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzErgebnisgruppe;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzExportdaten;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzFinanzamt;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzMahnung;
import com.lp.server.finanz.fastlanereader.generated.FLRSteuerkategorie;
import com.lp.server.finanz.fastlanereader.generated.FLRSteuerkategoriekonto;
import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.finanz.service.BelegbuchungDto;
import com.lp.server.finanz.service.BuchenFac;
import com.lp.server.finanz.service.BuchungDto;
import com.lp.server.finanz.service.BuchungdetailDto;
import com.lp.server.finanz.service.BuchungsjournalReportParameter;
import com.lp.server.finanz.service.ErgebnisgruppeDto;
import com.lp.server.finanz.service.ExportlaufDto;
import com.lp.server.finanz.service.FibuFehlerDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzReportFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.FinanzamtDto;
import com.lp.server.finanz.service.IntrastatDto;
import com.lp.server.finanz.service.KassenbuchDto;
import com.lp.server.finanz.service.KassenbuchungsteuerartDto;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.KontoDtoSmall;
import com.lp.server.finanz.service.KontoUvaVariante;
import com.lp.server.finanz.service.LiquititaetsvorschauImportDto;
import com.lp.server.finanz.service.MahnlaufDto;
import com.lp.server.finanz.service.MahnstufeDto;
import com.lp.server.finanz.service.MahntextDto;
import com.lp.server.finanz.service.MahnungDto;
import com.lp.server.finanz.service.PrintKontoblaetterModel;
import com.lp.server.finanz.service.PrintKontoblaetterModel.EnumSortOrder;
import com.lp.server.finanz.service.ReportErfolgsrechnungKriterienDto;
import com.lp.server.finanz.service.ReportSaldenlisteKriterienDto;
import com.lp.server.finanz.service.ReportUvaKriterienDto;
import com.lp.server.finanz.service.ReversechargeartDto;
import com.lp.server.finanz.service.SaldoInfoDto;
import com.lp.server.finanz.service.SammelSaldoInfoDto;
import com.lp.server.finanz.service.SammelmahnungDto;
import com.lp.server.finanz.service.SteuerkategorieDto;
import com.lp.server.finanz.service.SteuerkategoriekontoDto;
import com.lp.server.finanz.service.UvaFormularDto;
import com.lp.server.finanz.service.UvaartDto;
import com.lp.server.finanz.service.UvaverprobungDto;
import com.lp.server.finanz.service.WarenverkehrsnummerDto;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.service.BetriebskalenderDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.PersonalgehaltDto;
import com.lp.server.personal.service.ZahltagDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.personal.service.ZeiterfassungFacAll;
import com.lp.server.rechnung.ejb.Rechnung;
import com.lp.server.rechnung.ejb.Rechnungzahlung;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnungReport;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnungZahlung;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.rechnung.service.RechnungReportFac;
import com.lp.server.rechnung.service.RechnungzahlungDto;
import com.lp.server.system.ejbfac.EJBExcFactory;
import com.lp.server.system.ejbfac.HvCreatingCachingProvider;
import com.lp.server.system.fastlanereader.generated.FLREntitylog;
import com.lp.server.system.fastlanereader.generated.FLRKostenstelle;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeBilanz;
import com.lp.server.system.jcr.service.docnode.DocNodeErfolgsrechnung;
import com.lp.server.system.jcr.service.docnode.DocNodeKassenbuch;
import com.lp.server.system.jcr.service.docnode.DocNodeSaldenliste;
import com.lp.server.system.jcr.service.docnode.DocNodeUVAVerprobung;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.BelegartDto;
import com.lp.server.system.service.GeschaeftsjahrMandantDto;
import com.lp.server.system.service.HvDtoLogClass;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.MwstsatzbezDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.WechselkursDto;
import com.lp.server.system.service.ZahlungszielDto;
import com.lp.server.util.EingangsrechnungId;
import com.lp.server.util.HelperServer;
import com.lp.server.util.HvOptional;
import com.lp.server.util.LPReport;
import com.lp.server.util.UvaartId;
import com.lp.server.util.Validator;
import com.lp.server.util.collection.CollectionTools;
import com.lp.server.util.collection.IReject;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.logger.EventLogger;
import com.lp.server.util.logger.LogEventProducer;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.server.util.report.TimingInterceptor;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.LPDatenSubreport;
import com.lp.util.LPFibuFehlerSubreport;
import com.lp.util.report.UstVerprobungCompacted;
import com.lp.util.report.UstVerprobungRow;
import com.lp.util.report.UvaRpt;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
@Interceptors(TimingInterceptor.class)
public class FinanzReportFacBean extends LPReport implements FinanzReportFac, JRDataSource {

	@PersistenceContext
	private EntityManager em;

	private EventLogger evLog = new EventLogger(getEventLoggerFac(), FinanzReportFacBean.class);

	private static final int UC_RA_SCHREIBEN = 0;
	private static final int UC_MAHNUNG = 1;
	private static final int UC_KONTENLISTE = 2;
	private static final int UC_BUCHUNGEN_IN_BUCHUNGSJOURNAL = 3;
	private static final int UC_BUCHUNGSJOURNAL = 4;
	private static final int UC_BUCHUNGEN_AUF_KONTO = 5;
	private static final int UC_SALDENLISTE = 6;
	private static final int UC_SAMMELMAHNUNG = 7;
	private static final int UC_MAHNLAUF = 8;
	private static final int UC_EXPORTLAUF = 9;
	private static final int UC_INTRASTAT = 10;
	private static final int UC_KONTOBLATT = 11;
	private static final int UC_UVA = 12;
	private static final int UC_OFFENEPOSTEN = 13;
	private static final int UC_LIQUIDITAET = 14;
	private static final int UC_ERFOLGSRECHNUNG = 15;
	private static final int UC_STEUERKATEGORIEN = 16;
	private static final int UC_BUCHUNGSBELEG = 17;
	private static final int UC_USTVERPROBUNG = 18;
	private static final int UC_AENDERUNGEN_KONTO = 19;
	private static final int UC_KONTOBLATT_KASSENBUCH = 20;
	private static final int UC_EINFACHE_ERFOLGSRECHNUNG = 21;
	private static final int UC_KASSENJOUNRAL = 22;
	private static final int UC_UEBERWEISUNGSLISTE = 23;

	private static final int REPORT_USTVERPROBUNG_BETRAG = 0;
	private static final int REPORT_USTVERPROBUNG_STEUER = 1;
	private static final int REPORT_USTVERPROBUNG_STEUERART = 2;
	private static final int REPORT_USTVERPROBUNG_KONTO_NR = 3;
	private static final int REPORT_USTVERPROBUNG_KONTO_BEZ = 4;
	private static final int REPORT_USTVERPROBUNG_GEGENKONTO_NR = 5;
	private static final int REPORT_USTVERPROBUNG_GEGENKONTO_BEZ = 6;
	private static final int REPORT_USTVERPROBUNG_MWST_SATZ = 7;
	private static final int REPORT_USTVERPROBUNG_MWST_ID = 8;
	private static final int REPORT_USTVERPROBUNG_MWST_BEZ = 9;
	private static final int REPORT_USTVERPROBUNG_STEUERKATEGORIE_BEZ = 10;
	private static final int REPORT_USTVERPROBUNG_MWST_SATZ_BERECHNET = 11;
	private static final int REPORT_USTVERPROBUNG_RCART_ID = 12;
	private static final int REPORT_USTVERPROBUNG_RCART_CNR = 13;
	private static final int REPORT_USTVERPROBUNG_RCART_BEZ = 14;
	private static final int REPORT_USTVERPROBUNG_UVAART_CNR = 15;
	private static final int REPORT_USTVERPROBUNG_GEGENKONTO_UVAART_CNR = 16;
	private static final int REPORT_USTVERPROBUNG_DETAIL = 17;
	private static final int REPORT_USTVERPROBUNG_DATUM = 18;
	private static final int REPORT_USTVERPROBUNG_BUCHUNGTEXT = 19;
	private static final int REPORT_USTVERPROBUNG_BELEGNUMMER = 20;
	private static final int REPORT_USTVERPROBUNG_BELEGART = 21;
	private static final int REPORT_USTVERPROBUNG_KONTOMWSTSATZID = 22;
	private static final int REPORT_USTVERPROBUNG_KONTOSTEUERSATZ = 23;
	private static final int REPORT_USTVERPROBUNG_GEGENKONTOMWSTSATZID = 24;
	private static final int REPORT_USTVERPROBUNG_GEGENKONTOSTEUERSATZ = 25;
	private static final int REPORT_USTVERPROBUNG_ANZAHL_FELDER = 26;

	private static final int REPORT_RA_SCHREIBEN_RECHNUNGSNUMMER = 0;
	private static final int REPORT_RA_SCHREIBEN_RECHNUNGSDATUM = 1;
	private static final int REPORT_RA_SCHREIBEN_ZIELDATUM = 2;
	private static final int REPORT_RA_SCHREIBEN_ZINSSATZ = 3;
	private static final int REPORT_RA_SCHREIBEN_BETRAG = 4;
	private static final int REPORT_RA_SCHREIBEN_ZINSEN = 5;
	private static final int REPORT_RA_SCHREIBEN_MAHNSPESEN = 6;
	private static final int REPORT_RA_SCHREIBEN_ANZAHL_KRITERIEN = 7;

	private static final int REPORT_KONTENLISTE_KONTONUMMER = 0;
	private static final int REPORT_KONTENLISTE_BEZEICHNUNG = 1;
	private static final int REPORT_KONTENLISTE_USTKONTONUMMER = 2;
	private static final int REPORT_KONTENLISTE_SKONTOKONTONUMMER = 3;
	private static final int REPORT_KONTENLISTE_KONTOART = 4;
	private static final int REPORT_KONTENLISTE_STEUERKATEGORIE = 5;
	private static final int REPORT_KONTENLISTE_ERGEBNISGRUPPE = 6;
	private static final int REPORT_KONTENLISTE_UVAART = 7;
	private static final int REPORT_KONTENLISTE_FINANZAMT = 8;
	private static final int REPORT_KONTENLISTE_FINANZAMT_KURZBEZ = 9;
	private static final int REPORT_KONTENLISTE_KOSTENSTELLE = 10;
	private static final int REPORT_KONTENLISTE_BILANZKONTO = 11;
	private static final int REPORT_KONTENLISTE_GUELTIG_BIS = 12;
	private static final int REPORT_KONTENLISTE_GUELTIG_VON = 13;
	private static final int REPORT_KONTENLISTE_EROEFFNUNGSBUCHUNG = 14;
	private static final int REPORT_KONTENLISTE_ALLGEMEIN_SICHTBAR = 15;
	private static final int REPORT_KONTENLISTE_MANUELL_BEBUCHBAR = 16;
	private static final int REPORT_KONTENLISTE_VERSTECKT = 17;
	private static final int REPORT_KONTENLISTE_SORTIERUNG = 18;
	private static final int REPORT_KONTENLISTE_UVAART_CNR = 19;
	private static final int REPORT_KONTENLISTE_STEUERART = 20;
	private static final int REPORT_KONTENLISTE_GRUPPENTYP = 21;
	private static final int REPORT_KONTENLISTE_GRUPPENBEZEICHNUNG = 22;
	private static final int REPORT_KONTENLISTE_BOHNEUST = 23;
	private static final int REPORT_KONTENLISTE_ERGEBNISGRUPPETYP = 24;
	private static final int REPORT_KONTENLISTE_GRUPPENBEZEICHNUNG_NEG = 25;
	private static final int REPORT_KONTENLISTE_UVAART_VARIANTE = 26;
	private static final int REPORT_KONTENLISTE_ALLE_FELDER = 27;

	private static final int REPORT_BUCHUNGEN_IN_BUCHUNGSJOURNAL_SOLL = 0;
	private static final int REPORT_BUCHUNGEN_IN_BUCHUNGSJOURNAL_HABEN = 1;
	private static final int REPORT_BUCHUNGEN_IN_BUCHUNGSJOURNAL_BUCHUNGSDATUM = 2;
	private static final int REPORT_BUCHUNGEN_IN_BUCHUNGSJOURNAL_BUCHUNGSART = 3;
	private static final int REPORT_BUCHUNGEN_IN_BUCHUNGSJOURNAL_KONTONUMMER = 4;
	private static final int REPORT_BUCHUNGEN_IN_BUCHUNGSJOURNAL_KONTOBEZEICHNUNG = 5;
	private static final int REPORT_BUCHUNGEN_IN_BUCHUNGSJOURNAL_TEXT = 6;

	private static final int REPORT_BUCHUNGSJOURNAL_BUCHUNGSJOURNALDATUM = 0;
	private static final int REPORT_BUCHUNGSJOURNAL_BUCHUNGSDATUM = 1;
	private static final int REPORT_BUCHUNGSJOURNAL_KONTONUMMER = 2;
	private static final int REPORT_BUCHUNGSJOURNAL_GEGENKONTONUMMER = 3;
	private static final int REPORT_BUCHUNGSJOURNAL_BUCHUNGSART = 4;
	private static final int REPORT_BUCHUNGSJOURNAL_BELEGNUMMER = 5;
	private static final int REPORT_BUCHUNGSJOURNAL_TEXT = 6;
	private static final int REPORT_BUCHUNGSJOURNAL_BETRAG = 7;
	private static final int REPORT_BUCHUNGSJOURNAL_UST = 8;
	private static final int REPORT_BUCHUNGSJOURNAL_KOSTENSTELLENUMMER = 9;
	private static final int REPORT_BUCHUNGSJOURNAL_AUSZUG = 10;
	private static final int REPORT_BUCHUNGSJOURNAL_AM = 11;
	private static final int REPORT_BUCHUNGSJOURNAL_UM = 12;
	private static final int REPORT_BUCHUNGSJOURNAL_SOLL = 13;
	private static final int REPORT_BUCHUNGSJOURNAL_HABEN = 14;
	private static final int REPORT_BUCHUNGSJOURNAL_WER = 15;
	private static final int REPORT_BUCHUNGSJOURNAL_STORNIERT = 16;
	private static final int REPORT_BUCHUNGSJOURNAL_BUCHUNGSART_KBEZ = 17;
	private static final int REPORT_BUCHUNGSJOURNAL_BELEGART_KBEZ = 18;
	private static final int REPORT_BUCHUNGSJOURNAL_AUTOMATISCHE_BUCHUNG = 19;
	private static final int REPORT_BUCHUNGSJOURNAL_AUTOMATISCHE_EB = 20;
	private static final int REPORT_BUCHUNGSJOURNAL_UVAVERPROBUNG_IID = 21;
	private static final int REPORT_BUCHUNGSJOURNAL_ANZAHL_SPALTEN = 22;

	private static final int REPORT_BUCHUNGEN_AUF_KONTO_BETRAG = 0;
	private static final int REPORT_BUCHUNGEN_AUF_KONTO_UST = 1;
	private static final int REPORT_BUCHUNGEN_AUF_KONTO_BUCHUNGSDATUM = 2;
	private static final int REPORT_BUCHUNGEN_AUF_KONTO_BUCHUNGSART = 3;
	private static final int REPORT_BUCHUNGEN_AUF_KONTO_TEXT = 4;
	private static final int REPORT_BUCHUNGEN_AUF_KONTO_AUSZUG = 5;

	private static final int REPORT_SALDENLISTE_KONTONUMMER = 0;
	private static final int REPORT_SALDENLISTE_KONTOBEZEICHNUNG = 1;
	private static final int REPORT_SALDENLISTE_SOLL = 2;
	private static final int REPORT_SALDENLISTE_HABEN = 3;
	private static final int REPORT_SALDENLISTE_EBWERT = 4;
	private static final int REPORT_SALDENLISTE_SOLL_BIS = 5;
	private static final int REPORT_SALDENLISTE_HABEN_BIS = 6;
	private static final int REPORT_SALDENLISTE_STEUERKATEGORIE = 7;
	private static final int REPORT_SALDENLISTE_ERGEBNISGRUPPE = 8;
	private static final int REPORT_SALDENLISTE_EBWERT_BIS = 9;
	private static final int REPORT_SALDENLISTE_UVAART = 10;
	private static final int REPORT_SALDENLISTE_FINANZAMT = 11;
	private static final int REPORT_SALDENLISTE_KONSISTENT = 12;
	private static final int REPORT_SALDENLISTE_UVAART_VARIANTE = 13;
	private static final int REPORT_SALDENLISTE_KONTOART = 14;
	private static final int REPORT_SALDENLISTE_ANZAHL_SPALTEN = 15;

	private static final int REPORT_AENDERUNGEN_KONTO_FELDNAME = 0;
	private static final int REPORT_AENDERUNGEN_KONTO_AENDERUNGSZEITPUNKT = 1;
	private static final int REPORT_AENDERUNGEN_KONTO_GEAENDERT_VON = 2;
	private static final int REPORT_AENDERUNGEN_KONTO_GEAENDERT_NACH = 3;
	private static final int REPORT_AENDERUNGEN_KONTO_OPERATION = 4;
	private static final int REPORT_AENDERUNGEN_KONTO_GEAENDERT_DURCH = 5;
	private static final int REPORT_AENDERUNGEN_KONTO_ANZAHL_SPALTEN = 6;

	private static final int REPORT_ERFOLGSRECHNUNG_BEZEICHNUNG = 0;
	private static final int REPORT_ERFOLGSRECHNUNG_SALDO_PERIODE = 1;
	private static final int REPORT_ERFOLGSRECHNUNG_SALDO_KUMMULIERT = 2;
	private static final int REPORT_ERFOLGSRECHNUNG_SALDO_PERIODE_VORJAHR = 3;
	private static final int REPORT_ERFOLGSRECHNUNG_SALDO_KUMMULIERT_VORJAHR = 4;
	private static final int REPORT_ERFOLGSRECHNUNG_TYP = 5;
	private static final int REPORT_ERFOLGSRECHNUNG_BEZUGSBASIS = 6;
	private static final int REPORT_ERFOLGSRECHNUNG_KONTO_CNR = 7;
	private static final int REPORT_ERFOLGSRECHNUNG_KONTO_BEZEICHNUNG = 8;
	private static final int REPORT_ERFOLGSRECHNUNG_JAHRESGEWINN = 9;
	private static final int REPORT_ERFOLGSRECHNUNG_ANZAHL = 10;

	private static final int REPORT_SAMMELMAHNUNG_RECHNUNGSNUMMER = 0;
	private static final int REPORT_SAMMELMAHNUNG_BELEGDATUM = 1;
	private static final int REPORT_SAMMELMAHNUNG_FAELLIGKEITSDATUM = 2;
	private static final int REPORT_SAMMELMAHNUNG_WERT = 3;
	private static final int REPORT_SAMMELMAHNUNG_OFFEN = 4;
	private static final int REPORT_SAMMELMAHNUNG_MAHNSTUFE = 5;
	private static final int REPORT_SAMMELMAHNUNG_ZINSEN = 6;
	private static final int REPORT_SAMMELMAHNUNG_WERT_BELEGWAEHRUNG = 7;
	private static final int REPORT_SAMMELMAHNUNG_OFFEN_BELEGWAEHRUNG = 8;
	private static final int REPORT_SAMMELMAHNUNG_BELEGWAEHRUNG = 9;

	private static final int REPORT_MAHNLAUF_RECHNUNGSNUMMER = 0;
	private static final int REPORT_MAHNLAUF_RECHNUNGSDATUM = 1;
	private static final int REPORT_MAHNLAUF_KUNDE = 2;
	private static final int REPORT_MAHNLAUF_ZIELDATUM = 3;
	private static final int REPORT_MAHNLAUF_WERT = 4;
	private static final int REPORT_MAHNLAUF_OFFEN = 5;
	private static final int REPORT_MAHNLAUF_MAHNSTUFE = 6;
	private static final int REPORT_MAHNLAUF_LETZTEMAHNSTUFE = 7;
	private static final int REPORT_MAHNLAUF_LETZTESMAHNDATUM = 8;
	private static final int REPORT_MAHNLAUF_AUFTRAG_NUMMER = 9;
	private static final int REPORT_MAHNLAUF_AUFTRAG_PROJEKT = 10;
	private static final int REPORT_MAHNLAUF_AUFTRAG_BESTELLNUMMER = 11;
	private static final int REPORT_MAHNLAUF_POSITIONSBEZEICHNUNG1 = 12;
	private static final int REPORT_MAHNLAUF_POSITIONSBEZEICHNUNG2 = 13;
	private static final int REPORT_MAHNLAUF_KUNDE_STATISTIKADRESSE = 14;
	private static final int REPORT_MAHNLAUF_VERTRETER = 15;
	private static final int REPORT_MAHNLAUF_WERTUST = 16;
	private static final int REPORT_MAHNLAUF_OFFENUST = 17;
	private static final int REPORT_MAHNLAUF_RECHNUNGSART = 18;
	private static final int REPORT_MAHNLAUF_MAHNSPERREBIS = 19;
	private static final int REPORT_MAHNLAUF_RECHNUNG_STATUS = 20;
	private static final int REPORT_MAHNLAUF_ZAHLUNGSZIEL = 21;

	private static final int REPORT_STEUERKATEGORIE_FINANZAMT = 0;
	private static final int REPORT_STEUERKATEGORIE_STEUERKATEGORIE = 1;
	private static final int REPORT_STEUERKATEGORIE_STEUERKATEGORIEBEZEICHNUNG = 2;
	private static final int REPORT_STEUERKATEGORIE_KONTO_VK = 3;
	private static final int REPORT_STEUERKATEGORIE_KONTO_VK_BEZEICHNUNG = 4;
	private static final int REPORT_STEUERKATEGORIE_KONTO_EK = 5;
	private static final int REPORT_STEUERKATEGORIE_KONTO_EK_BEZEICHNUNG = 6;
	private static final int REPORT_STEUERKATEGORIE_KONTO_SKONTO_EK = 7;
	private static final int REPORT_STEUERKATEGORIE_KONTO_SKONTO_EK_BEZEICHNUNG = 8;
	private static final int REPORT_STEUERKATEGORIE_KONTO_SKONTO_VK = 9;
	private static final int REPORT_STEUERKATEGORIE_KONTO_SKONTO_VK_BEZEICHNUNG = 10;
	private static final int REPORT_STEUERKATEGORIE_KONTO_EINFUHRUST = 11;
	private static final int REPORT_STEUERKATEGORIE_KONTO_EINFUHRUST_BEZEICHNUNG = 12;
	private static final int REPORT_STEUERKATEGORIE_PARAMETER_SACHKONTO = 13;
	private static final int REPORT_STEUERKATEGORIE_PARAMETER_SACHKONTO_BEZEICHNUNG = 14;
	private static final int REPORT_STEUERKATEGORIE_PARAMETER_DEBITOREN = 15;
	private static final int REPORT_STEUERKATEGORIE_PARAMETER_DEBITOREN_BEZEICHNUNG = 16;
	private static final int REPORT_STEUERKATEGORIE_PARAMETER_KREDITOREN = 17;
	private static final int REPORT_STEUERKATEGORIE_PARAMETER_KREDITOREN_BEZEICHNUNG = 18;
	private static final int REPORT_STEUERKATEGORIE_PARAMETER_VERR_ERHALTEN = 19;
	private static final int REPORT_STEUERKATEGORIE_PARAMETER_VERR_ERHALTEN_BEZEICHNUNG = 20;
	private static final int REPORT_STEUERKATEGORIE_PARAMETER_ERHALTEN = 21;
	private static final int REPORT_STEUERKATEGORIE_PARAMETER_ERHALTEN_BEZEICHNUNG = 22;
	private static final int REPORT_STEUERKATEGORIE_PARAMETER_VERR_GELEISTET = 23;
	private static final int REPORT_STEUERKATEGORIE_PARAMETER_VERR_GELEISTET_BEZEICHNUNG = 24;
	private static final int REPORT_STEUERKATEGORIE_PARAMETER_GELEISTET = 25;
	private static final int REPORT_STEUERKATEGORIE_PARAMETER_GELEISTET_BEZEICHNUNG = 26;
	private static final int REPORT_STEUERKATEGORIE_REVERSECHARGE = 27;
	private static final int REPORT_STEUERKATEGORIE_MWST_BEZEICHNUNG = 28;
	private static final int REPORT_STEUERKATEGORIE_KONTO_FORDERUNGEN = 29;
	private static final int REPORT_STEUERKATEGORIE_KONTO_FORDERUNGEN_BEZEICHNUNG = 30;
	private static final int REPORT_STEUERKATEGORIE_KONTO_VERBINDLICHKEITEN = 31;
	private static final int REPORT_STEUERKATEGORIE_KONTO_VERBINDLICHKEITEN_BEZEICHNUNG = 32;
	private static final int REPORT_STEUERKATEGORIE_KONTO_KURSGEWINNE = 33;
	private static final int REPORT_STEUERKATEGORIE_KONTO_KURSGEWINNE_BEZEICHNUNG = 34;
	private static final int REPORT_STEUERKATEGORIE_KONTO_KURSVERLUSTE = 35;
	private static final int REPORT_STEUERKATEGORIE_KONTO_KURSVERLUSTE_BEZEICHNUNG = 36;
	private static final int REPORT_STEUERKATEGORIE_SAMMELKONTO_MAP = 37;
	private static final int REPORT_STEUERKATEGORIE_FINANZAMT_I_ID = 38;
	private static final int REPORT_STEUERKATEGORIE_REVERSECHARGEART_I_ID = 39;
	private static final int REPORT_STEUERKATEGORIE_REVERSECHARGEART_C_NR = 40;
	private static final int REPORT_STEUERKATEGORIE_REVERSECHARGEART_BEZ = 41;
	private static final int REPORT_STEUERKATEGORIE_GUELTIG_AB = 42;
	private static final int REPORT_STEUERKATEGORIE_ANZAHL_FELDER = 43;

	private static final int REPORT_INTRASTAT_BELEGNUMMER = 0;
	private static final int REPORT_INTRASTAT_IDENT = 1;
	private static final int REPORT_INTRASTAT_BEZEICHNUNG = 2;
	private static final int REPORT_INTRASTAT_MENGE = 3;
	private static final int REPORT_INTRASTAT_WARENVERKEHRSNUMMER = 4;
	private static final int REPORT_INTRASTAT_PREIS = 5;
	private static final int REPORT_INTRASTAT_WERT = 6;
	private static final int REPORT_INTRASTAT_STATISTISCHER_WERT = 7;
	private static final int REPORT_INTRASTAT_GEWICHT = 8;
	private static final int REPORT_INTRASTAT_BELEGART = 9;
	private static final int REPORT_INTRASTAT_WVK_BEZEICHNUNG = 10;
	private static final int REPORT_INTRASTAT_UID = 11;
	private static final int REPORT_INTRASTAT_URSPRUNGSLAND = 12;
	private static final int REPORT_INTRASTAT_ANZAHL_SPALTEN = 13;

	private static final int REPORT_KONTOBLATT_BUCHUNGSDATUM = 0;
	private static final int REPORT_KONTOBLATT_BUCHUNGSART = 1;
	private static final int REPORT_KONTOBLATT_BELEG = 2;
	private static final int REPORT_KONTOBLATT_GEGENKONTO = 3;
	private static final int REPORT_KONTOBLATT_TEXT = 4;
	private static final int REPORT_KONTOBLATT_SOLL = 5;
	private static final int REPORT_KONTOBLATT_HABEN = 6;
	private static final int REPORT_KONTOBLATT_USTWERT = 7;
	private static final int REPORT_KONTOBLATT_USTPROZENT = 8;
	private static final int REPORT_KONTOBLATT_AUSZUG = 9;
	private static final int REPORT_KONTOBLATT_BELEGARTCNR = 10;
	private static final int REPORT_KONTOBLATT_SOLL_FW = 11;
	private static final int REPORT_KONTOBLATT_HABEN_FW = 12;
	private static final int REPORT_KONTOBLATT_BELEGKURS = 13;
	private static final int REPORT_KONTOBLATT_KURSZUDATUM = 14;
	private static final int REPORT_KONTOBLATT_BELEGWAEHRUNG = 15;
	private static final int REPORT_KONTOBLATT_AUTOMATISCHE_EB = 16;
	private static final int REPORT_KONTOBLATT_INKONSISTENT = 17;
	private static final int REPORT_KONTOBLATT_AZK = 18;
	private static final int REPORT_KONTOBLATT_AZRSR = 19;
	private static final int REPORT_KONTOBLATT_AZRSR_BELEG = 20;
	private static final int REPORT_KONTOBLATT_AZRSR_BELEG_ID = 21;
	private static final int REPORT_KONTOBLATT_AZRSR_BELEGART = 22;
	private static final int REPORT_KONTOBLATT_AZRSR_BELEGART_CNR = 23;
	private static final int REPORT_KONTOBLATT_GEGENKONTO_BEZEICHNUNG = 24;
	private static final int REPORT_KONTOBLATT_ANZAHL_SPALTEN = 25;

	// Kassenbuch hat die gleichen Spalten wie Kontoblatt + USTVST
	private static final int REPORT_KASSENBUCH_USTVST = REPORT_KONTOBLATT_ANZAHL_SPALTEN;
	private static final int REPORT_KASSENBUCH_ANZAHL_SPALTEN = REPORT_KONTOBLATT_ANZAHL_SPALTEN + 1;

	private static final int REPORT_OP_BUCHUNGSDATUM = 0;
	private static final int REPORT_OP_BUCHUNGSART = 1;
	private static final int REPORT_OP_BELEG = 2;
	private static final int REPORT_OP_GEGENKONTO = 3;
	private static final int REPORT_OP_TEXT = 4;
	private static final int REPORT_OP_SOLL = 5;
	private static final int REPORT_OP_HABEN = 6;
	private static final int REPORT_OP_USTWERT = 7;
	private static final int REPORT_OP_USTPROZENT = 8;
	private static final int REPORT_OP_AUSZUG = 9;
	private static final int REPORT_OP_BELEGARTCNR = 10;
	private static final int REPORT_OP_KONTOCNR = 11;
	private static final int REPORT_OP_AZK = 12;
	private static final int REPORT_OP_AZK_SUMME_FEHLERHAFT = 13;
	private static final int REPORT_OFFENEPOSTEN_ANZAHL_SPALTEN = 14;

	private static final int REPORT_LV_BELEG = 0;
	private static final int REPORT_LV_PARTNER = 1;
	private static final int REPORT_LV_JAHR = 2;
	private static final int REPORT_LV_WOCHE = 3;
	private static final int REPORT_LV_NETTO = 4;
	private static final int REPORT_LV_MWST = 5;
	private static final int REPORT_LV_ZIELTAGE_ZAHLUGNSMORAL = 6;
	private static final int REPORT_LV_ZIELTAGE_VEREINBARTES_ZAHLUNGSZIEL = 7;
	private static final int REPORT_LV_IMPORTIERT = 8;
	private static final int REPORT_LV_ARTIKELNUMMER = 9;
	private static final int REPORT_LV_BEZEICHNUNG = 10;
	private static final int REPORT_LV_ZUSATZBEZEICHNUNG = 11;
	private static final int REPORT_LV_BELEGUNTERART = 12;
	private static final int REPORT_LV_DATUM = 13;
	private static final int REPORT_LV_ANZAHL_SPALTEN = 14;

	private static final int REPORT_BUCHUNGSBELEG_ARTCNR = 0;
	private static final int REPORT_BUCHUNGSBELEG_KOMMENTAR = 1;
	private static final int REPORT_BUCHUNGSBELEG_KONTOBEZ = 2;
	private static final int REPORT_BUCHUNGSBELEG_KONTONR = 3;
	private static final int REPORT_BUCHUNGSBELEG_KONTOBEM = 4;
	private static final int REPORT_BUCHUNGSBELEG_KONTODTO = 5;
	private static final int REPORT_BUCHUNGSBELEG_AUSZUG = 6;
	private static final int REPORT_BUCHUNGSBELEG_BETRAG = 7;
	private static final int REPORT_BUCHUNGSBELEG_BETRAGUST = 8;
	private static final int REPORT_BUCHUNGSBELEG_IST_HABEN = 9;
	private static final int REPORT_BUCHUNGSBELEG_IST_MITLAUFEND = 10;
	private static final int REPORT_BUCHUNGSBELEG_ANZAHL_SPALTEN = 11;

	private static final String REPORT_GRUPPENTYP_BILANZ = "B";
	private static final String REPORT_GRUPPENTYP_ERGEBNIS = "E";

	private static final int REPORT_EINFACHE_ERFOLGSRECHNUNG_MONAT = 0;
	private static final int REPORT_EINFACHE_ERFOLGSRECHNUNG_NETTOUMSATZ_RE = 1;
	private static final int REPORT_EINFACHE_ERFOLGSRECHNUNG_NETTOUMSATZ_ER = 2;
	private static final int REPORT_EINFACHE_ERFOLGSRECHNUNG_NETTOUMSATZ_ZK = 3;
	private static final int REPORT_EINFACHE_ERFOLGSRECHNUNG_PERSONALKOSTEN = 4;
	private static final int REPORT_EINFACHE_ERFOLGSRECHNUNG_LAGER_GESTEHUNGSWERT_ZUM_MONATSLETZTEN = 5;
	private static final int REPORT_EINFACHE_ERFOLGSRECHNUNG_HF_GESTEHUNGSWERT_MATERIAL_ZUM_MONATSLETZTEN = 6;
	private static final int REPORT_EINFACHE_ERFOLGSRECHNUNG_HF_GESTEHUNGSWERT_ARBEIT_ZUM_MONATSLETZTEN = 7;
	private static final int REPORT_EINFACHE_ERFOLGSRECHNUNG_PERSONALSTUNDEN = 8;
	private static final int REPORT_EINFACHE_ERFOLGSRECHNUNG_MASCHINENSTUNDEN = 9;
	private static final int REPORT_EINFACHE_ERFOLGSRECHNUNG_ANZAHL_SPALTEN = 10;

	private static final int REPORT_UEBERWEISUNGSLISTE_ART = 0;
	private static final int REPORT_UEBERWEISUNGSLISTE_EINGANGSRECHNUNG = 1;
	private static final int REPORT_UEBERWEISUNGSLISTE_MAHNSTUFE = 2;
	private static final int REPORT_UEBERWEISUNGSLISTE_WERT = 3;
	private static final int REPORT_UEBERWEISUNGSLISTE_PARTNER = 4;
	private static final int REPORT_UEBERWEISUNGSLISTE_FAELLIG = 5;
	private static final int REPORT_UEBERWEISUNGSLISTE_SKONTO = 6;
	private static final int REPORT_UEBERWEISUNGSLISTE_ZAHLBETRAG = 7;
	private static final int REPORT_UEBERWEISUNGSLISTE_WAEHRUNG = 8;
	private static final int REPORT_UEBERWEISUNGSLISTE_TEXT = 9;
	private static final int REPORT_UEBERWEISUNGSLISTE_FREIGABE_ZUR_UEBERWEISUNG = 10;
	private static final int REPORT_UEBERWEISUNGSLISTE_BANK = 11;
	private static final int REPORT_UEBERWEISUNGSLISTE_IBAN = 12;
	private static final int REPORT_UEBERWEISUNGSLISTE_BIC = 13;
	private static final int REPORT_UEBERWEISUNGSLISTE_GEPRUEFT_VON = 14;
	private static final int REPORT_UEBERWEISUNGSLISTE_GEPRUEFT_AM = 15;
	private static final int REPORT_UEBERWEISUNGSLISTE_ANZAHL_SPALTEN = 16;

	private static final int REPORT_KASSENJOURNAL_BELEGART = 0;
	private static final int REPORT_KASSENJOURNAL_BELEGNUMMER = 1;
	private static final int REPORT_KASSENJOURNAL_KUNDELIEFERANT = 2;
	private static final int REPORT_KASSENJOURNAL_TEXT = 3;
	private static final int REPORT_KASSENJOURNAL_BETRAG_EIN = 4;
	private static final int REPORT_KASSENJOURNAL_BETRAG_AUS = 5;
	private static final int REPORT_KASSENJOURNAL_USTBETRAG_EIN = 6;
	private static final int REPORT_KASSENJOURNAL_USTBETRAG_AUS = 7;
	private static final int REPORT_KASSENJOURNAL_ZAHLDATUM = 8;
	private static final int REPORT_KASSENJOURNAL_KOMMENTAR = 9;
	private static final int REPORT_KASSENJOURNAL_ANZAHL_SPALTEN = 10;

	private static final int REPORT_UVA_UVARPT = 0;
	private static final int REPORT_UVA_UVAART = 1;
	private static final int REPORT_UVA_KENNZAHL = 2;
	private static final int REPORT_UVA_UMSATZ = 3;
	private static final int REPORT_UVA_UMSATZGERUNDET = 4;
	private static final int REPORT_UVA_STEUER = 5;
	private static final int REPORT_UVA_SATZ = 6;
	private static final int REPORT_UVA_KONTOVARIANTE = 7;
	private static final int REPORT_UVA_GRUPPE = 8;
	private static final int REPORT_UVA_SORT = 9;
	private static final int REPORT_UVA_MWSTSATZ = 10;
	private static final int REPORT_UVA_ALTERNATIVE = 11;
	private static final int REPORT_UVA_ANZAHLUNG_UMSATZ = 12;
	private static final int REPORT_UVA_ANZAHLUNG_UMSATZGERUNDET = 13;
	private static final int REPORT_UVA_ANZAHL_SPALTEN = 14;

	private int useCase;
	private Object[][] data = null;

	public class ReportFieldValues {
		private Map<String, Integer> fieldMap;

		public ReportFieldValues(Map<String, Integer> fieldMap) {
			this.fieldMap = fieldMap;
		}

		public ReportFieldValues(ReportFieldValues other) {
			this.fieldMap = new HashMap<String, Integer>();
			this.fieldMap.putAll(other.getFieldMap());
		}

		public void setFieldMap(Map<String, Integer> fieldMap) {
			if (null == fieldMap)
				throw new IllegalArgumentException("fieldMap");

			this.fieldMap = fieldMap;
		}

		protected Map<String, Integer> getFieldMap() {
			return fieldMap;
		}

		public Object getFieldValue(String fieldname) {
			Integer fieldNumber = fieldMap.get(fieldname);
			if (null == fieldNumber)
				return null;

			return data[index][fieldNumber];
		}

		public void addField(String reportFieldName, Integer dataIndex) {
			fieldMap.put(reportFieldName, dataIndex);
		}
	}

	public static HashMap<String, Integer> reportKontenListeIndexer = new HashMap<String, Integer>() {
		private static final long serialVersionUID = 1L;
		{
			put("F_KONTONUMMER", REPORT_KONTENLISTE_KONTONUMMER);
			put(F_BEZEICHNUNG, REPORT_KONTENLISTE_BEZEICHNUNG);
			put("F_USTKONTONUMMER", REPORT_KONTENLISTE_USTKONTONUMMER);
			put("F_SKONTOKONTONUMMER", REPORT_KONTENLISTE_SKONTOKONTONUMMER);
			put("F_KONTOART", REPORT_KONTENLISTE_KONTOART);
			put("F_STEUERKATEGORIE", REPORT_KONTENLISTE_STEUERKATEGORIE);
			put("F_ERGEBNISSGRUPPE", REPORT_KONTENLISTE_ERGEBNISGRUPPE);
			put("F_UVAART", REPORT_KONTENLISTE_UVAART);
			put("F_UVAART_VARIANTE", REPORT_KONTENLISTE_UVAART_VARIANTE);
			put("F_FINANZAMT", REPORT_KONTENLISTE_FINANZAMT);
			put("F_FINANZAMT_KURZBEZ", REPORT_KONTENLISTE_FINANZAMT_KURZBEZ);
			put("F_KOSTENSTELLE", REPORT_KONTENLISTE_KOSTENSTELLE);
			put("F_WEITERFUEHRENDEBILANZ_KONTONUMMER", REPORT_KONTENLISTE_BILANZKONTO);
			put("F_GUELTIG_BIS", REPORT_KONTENLISTE_GUELTIG_BIS);
			put("F_GUELTIG_VON", REPORT_KONTENLISTE_GUELTIG_VON);
			put("F_AUTOMATISCHE_EROEFFNUNGSBUCHUNG", REPORT_KONTENLISTE_EROEFFNUNGSBUCHUNG);
			put("F_ALLGEMEIN_SICHTBAR", REPORT_KONTENLISTE_ALLGEMEIN_SICHTBAR);
			put("F_MANUELL_BEBUCHBAR", REPORT_KONTENLISTE_MANUELL_BEBUCHBAR);
			put("F_VERSTECKT", REPORT_KONTENLISTE_VERSTECKT);
			put("F_SORTIERUNG", REPORT_KONTENLISTE_SORTIERUNG);
			put("F_UVAART_CNR", REPORT_KONTENLISTE_UVAART_CNR);
			put("F_STEUERART", REPORT_KONTENLISTE_STEUERART);
			put("F_GRUPPENTYP", REPORT_KONTENLISTE_GRUPPENTYP);
			put("F_GRUPPENBEZEICHNUNG", REPORT_KONTENLISTE_GRUPPENBEZEICHNUNG);
			put("F_OHNEUST", REPORT_KONTENLISTE_BOHNEUST);
			put("F_ERGEBNISGRUPPETYP", REPORT_KONTENLISTE_ERGEBNISGRUPPETYP);
			put("F_GRUPPENBEZEICHNUNG_NEG", REPORT_KONTENLISTE_GRUPPENBEZEICHNUNG_NEG);
		}
	};

	private ReportFieldValues reportKontenListe = new ReportFieldValues(reportKontenListeIndexer);

	public static HashMap<String, Integer> reportKontoblattIndexer = new HashMap<String, Integer>() {
		private static final long serialVersionUID = 1L;
		{
			put("Auszug", REPORT_KONTOBLATT_AUSZUG);
			put("Beleg", REPORT_KONTOBLATT_BELEG);
			put("Buchungsart", REPORT_KONTOBLATT_BUCHUNGSART);
			put("Buchungsdatum", REPORT_KONTOBLATT_BUCHUNGSDATUM);
			put("Gegenkonto", REPORT_KONTOBLATT_GEGENKONTO);
			put("GegenkontoBezeichnung", REPORT_KONTOBLATT_GEGENKONTO_BEZEICHNUNG);
			put("Haben", REPORT_KONTOBLATT_HABEN);
			put("Soll", REPORT_KONTOBLATT_SOLL);
			put("Text", REPORT_KONTOBLATT_TEXT);
			put("UstProzent", REPORT_KONTOBLATT_USTPROZENT);
			put("UstWert", REPORT_KONTOBLATT_USTWERT);
			put("BelegartCNr", REPORT_KONTOBLATT_BELEGARTCNR);
			put("HabenFW", REPORT_KONTOBLATT_HABEN_FW);
			put("SollFW", REPORT_KONTOBLATT_SOLL_FW);
			put("Waehrung", REPORT_KONTOBLATT_BELEGWAEHRUNG);
			put("Kurs", REPORT_KONTOBLATT_BELEGKURS);
			put("KursZuDatum", REPORT_KONTOBLATT_KURSZUDATUM);
			put("AutomatischeEB", REPORT_KONTOBLATT_AUTOMATISCHE_EB);
			put("IstInkonsistent", REPORT_KONTOBLATT_INKONSISTENT);
			put("AZK", REPORT_KONTOBLATT_AZK);
			put("AzrSrKennzeichen", REPORT_KONTOBLATT_AZRSR);
			put("AzrSrBeleg", REPORT_KONTOBLATT_AZRSR_BELEG);
			put("AzrSrBelegId", REPORT_KONTOBLATT_AZRSR_BELEG_ID);
			put("AzrSrBelegart", REPORT_KONTOBLATT_AZRSR_BELEGART);
			put("AzrSrBelegartCnr", REPORT_KONTOBLATT_AZRSR_BELEGART_CNR);
		}
	};

	private ReportFieldValues reportKontoblatt = new ReportFieldValues(reportKontoblattIndexer);

	public static HashMap<String, Integer> reportKassenbuchIndexer = new HashMap<String, Integer>(
			reportKontoblattIndexer) {
		private static final long serialVersionUID = 1L;
		{
			put("USTVST", REPORT_KASSENBUCH_USTVST);
		}
	};

	private ReportFieldValues reportKassenbuch = new ReportFieldValues(reportKassenbuchIndexer);

	// private ReportFieldValues reportKontoblatt = new ReportFieldValues(
	// new HashMap<String, Integer>() {
	//
	// private static final long serialVersionUID = 1L;
	//
	// {
	// put("Auszug", REPORT_KONTOBLATT_AUSZUG);
	// put("Beleg", REPORT_KONTOBLATT_BELEG);
	// put("Buchungsart", REPORT_KONTOBLATT_BUCHUNGSART);
	// put("Buchungsdatum", REPORT_KONTOBLATT_BUCHUNGSDATUM);
	// put("Gegenkonto", REPORT_KONTOBLATT_GEGENKONTO);
	// put("Haben", REPORT_KONTOBLATT_HABEN);
	// put("Soll", REPORT_KONTOBLATT_SOLL);
	// put("Text", REPORT_KONTOBLATT_TEXT);
	// put("UstProzent", REPORT_KONTOBLATT_USTPROZENT);
	// put("UstWert", REPORT_KONTOBLATT_USTWERT);
	// put("BelegartCNr", REPORT_KONTOBLATT_BELEGARTCNR);
	// put("HabenFW", REPORT_KONTOBLATT_HABEN_FW);
	// put("SollFW", REPORT_KONTOBLATT_SOLL_FW);
	// put("Waehrung", REPORT_KONTOBLATT_BELEGWAEHRUNG);
	// put("Kurs", REPORT_KONTOBLATT_BELEGKURS);
	// put("KursZuDatum", REPORT_KONTOBLATT_KURSZUDATUM);
	// put("AutomatischeEB", REPORT_KONTOBLATT_AUTOMATISCHE_EB);
	// put("IstInkonsistent", REPORT_KONTOBLATT_INKONSISTENT);
	// put("AZK", REPORT_KONTOBLATT_AZK);
	// }
	// });

	// private ReportFieldValues reportKassenbuch = new
	// ReportFieldValues(reportKontoblatt) {
	// {
	// addField("USTVST", REPORT_KASSENBUCH_USTVST);
	// }
	// };

	public static HashMap<String, Integer> reportErfolgsrechnungIndexer = new HashMap<String, Integer>() {
		private static final long serialVersionUID = 1L;
		{
			put("F_BEZEICHNUNG", REPORT_ERFOLGSRECHNUNG_BEZEICHNUNG);
			put("F_SALDO_MONAT", REPORT_ERFOLGSRECHNUNG_SALDO_PERIODE);
			put("F_SALDO_KUMMULIERT", REPORT_ERFOLGSRECHNUNG_SALDO_KUMMULIERT);
			put("F_SALDO_MONAT_VORJAHR", REPORT_ERFOLGSRECHNUNG_SALDO_PERIODE_VORJAHR);
			put("F_SALDO_KUMMULIERT_VORJAHR", REPORT_ERFOLGSRECHNUNG_SALDO_KUMMULIERT_VORJAHR);
			put("F_TYP", REPORT_ERFOLGSRECHNUNG_TYP);
			put("F_BEZUGSBASIS", REPORT_ERFOLGSRECHNUNG_BEZUGSBASIS);
			put("F_KONTO_CNR", REPORT_ERFOLGSRECHNUNG_KONTO_CNR);
			put("F_KONTO_BEZEICHNUNG", REPORT_ERFOLGSRECHNUNG_KONTO_BEZEICHNUNG);
			put("F_JAHRESGEWINN", REPORT_ERFOLGSRECHNUNG_JAHRESGEWINN);
		}
	};

	private ReportFieldValues reportErfolgsrechnung = new ReportFieldValues(reportErfolgsrechnungIndexer);

	public static HashMap<String, Integer> reportSaldenlisteIndexer = new HashMap<String, Integer>() {
		private static final long serialVersionUID = 1L;
		{
			put("F_KONTONUMMER", REPORT_SALDENLISTE_KONTONUMMER);
			put("F_KONTOBEZEICHNUNG", REPORT_SALDENLISTE_KONTOBEZEICHNUNG);
			put("F_SOLL", REPORT_SALDENLISTE_SOLL);
			put("F_HABEN", REPORT_SALDENLISTE_HABEN);
			put("F_EBWERT", REPORT_SALDENLISTE_EBWERT);
			put("F_SOLL_BIS", REPORT_SALDENLISTE_SOLL_BIS);
			put("F_HABEN_BIS", REPORT_SALDENLISTE_HABEN_BIS);
			put("F_STEUERKATEGORIE_C_BEZ", REPORT_SALDENLISTE_STEUERKATEGORIE);
			put("F_ERGEBNISGRUPPE", REPORT_SALDENLISTE_ERGEBNISGRUPPE);
			put("F_EBWERT_BIS", REPORT_SALDENLISTE_EBWERT_BIS);
			put("F_UVAART", REPORT_SALDENLISTE_UVAART);
			put("F_UVAART_VARIANTE", REPORT_SALDENLISTE_UVAART_VARIANTE);
			put("F_KONTOART", REPORT_SALDENLISTE_KONTOART);
			put("F_FINANZAMT", REPORT_SALDENLISTE_FINANZAMT);
			put("F_KONTO_KONSISTENT", REPORT_SALDENLISTE_KONSISTENT);
		}
	};

	private ReportFieldValues reportSaldenliste = new ReportFieldValues(reportSaldenlisteIndexer);

	private ReportFieldValues reportBuchungsbelegListe = new ReportFieldValues(new HashMap<String, Integer>() {
		private static final long serialVersionUID = 1L;
		{
			put("ArtCNr", REPORT_BUCHUNGSBELEG_ARTCNR);
			put("Kommentar", REPORT_BUCHUNGSBELEG_KOMMENTAR);
			put("KontoBez", REPORT_BUCHUNGSBELEG_KONTOBEZ);
			put("KontoNr", REPORT_BUCHUNGSBELEG_KONTONR);
			put("KontoBem", REPORT_BUCHUNGSBELEG_KONTOBEM);
			put("KontoDto", REPORT_BUCHUNGSBELEG_KONTODTO);
			put("Auszug", REPORT_BUCHUNGSBELEG_AUSZUG);
			put("Betrag", REPORT_BUCHUNGSBELEG_BETRAG);
			put("Ust", REPORT_BUCHUNGSBELEG_BETRAGUST);
			put("IstHaben", REPORT_BUCHUNGSBELEG_IST_HABEN);
			put("IstMitlaufend", REPORT_BUCHUNGSBELEG_IST_MITLAUFEND);
		}
	});

	private ReportFieldValues reportBuchungsjournal = new ReportFieldValues(new HashMap<String, Integer>() {
		private static final long serialVersionUID = -7477136424185048969L;
		{
			put("F_BUCHUNGSJOURNALDATUM", REPORT_BUCHUNGSJOURNAL_BUCHUNGSJOURNALDATUM);
			put("F_BUCHUNGSDATUM", REPORT_BUCHUNGSJOURNAL_BUCHUNGSDATUM);
			put("F_KONTONUMMER", REPORT_BUCHUNGSJOURNAL_KONTONUMMER);
			put("F_GEGENKONTONUMMER", REPORT_BUCHUNGSJOURNAL_GEGENKONTONUMMER);
			put("F_BUCHUNGSART", REPORT_BUCHUNGSJOURNAL_BUCHUNGSART);
			put("F_BELEGNUMMER", REPORT_BUCHUNGSJOURNAL_BELEGNUMMER);
			put("F_TEXT", REPORT_BUCHUNGSJOURNAL_TEXT);
			put("F_BETRAG", REPORT_BUCHUNGSJOURNAL_BETRAG);
			put("F_UST", REPORT_BUCHUNGSJOURNAL_UST);
			put("F_STORNIERT", REPORT_BUCHUNGSJOURNAL_STORNIERT);
			put("F_KOSTENSTELLENUMMER", REPORT_BUCHUNGSJOURNAL_KOSTENSTELLENUMMER);
			put("F_AUSZUG", REPORT_BUCHUNGSJOURNAL_AUSZUG);
			put("F_AM", REPORT_BUCHUNGSJOURNAL_AM);
			put("F_UM", REPORT_BUCHUNGSJOURNAL_UM);
			put("F_SOLL", REPORT_BUCHUNGSJOURNAL_SOLL);
			put("F_HABEN", REPORT_BUCHUNGSJOURNAL_HABEN);
			put("F_WER", REPORT_BUCHUNGSJOURNAL_WER);
			put("F_BELEGART_KBEZ", REPORT_BUCHUNGSJOURNAL_BELEGART_KBEZ);
			put("F_BUCHUNGSART_KBEZ", REPORT_BUCHUNGSJOURNAL_BUCHUNGSART_KBEZ);
			put("F_AUTOMATISCHE_BUCHUNG", REPORT_BUCHUNGSJOURNAL_AUTOMATISCHE_BUCHUNG);
			put("F_AUTOMATISCHE_EB", REPORT_BUCHUNGSJOURNAL_AUTOMATISCHE_EB);
			put("F_UVAVERPROBUNG_I_ID", REPORT_BUCHUNGSJOURNAL_UVAVERPROBUNG_IID);
		}
	});

	public boolean next() throws JRException {
		index++;
		return (index < data.length);
	}

	public Object getFieldValue(JRField field) throws JRException {
		Object value = null;
		String fieldName = field.getName();
		switch (useCase) {
		case UC_MAHNUNG: {

		}
			break;
		case UC_RA_SCHREIBEN: {
			if ("F_BETRAG".equals(fieldName)) {
				value = data[index][REPORT_RA_SCHREIBEN_BETRAG];
			} else if ("F_MAHNSPESEN".equals(fieldName)) {
				value = data[index][REPORT_RA_SCHREIBEN_MAHNSPESEN];
			} else if ("F_RECHNUNGSDATUM".equals(fieldName)) {
				value = data[index][REPORT_RA_SCHREIBEN_RECHNUNGSDATUM];
			} else if ("F_RECHNUNGSNUMMER".equals(fieldName)) {
				value = data[index][REPORT_RA_SCHREIBEN_RECHNUNGSNUMMER];
			} else if ("F_ZIELDATUM".equals(fieldName)) {
				value = data[index][REPORT_RA_SCHREIBEN_ZIELDATUM];
			} else if ("F_ZINSEN".equals(fieldName)) {
				value = data[index][REPORT_RA_SCHREIBEN_ZINSEN];
			} else if ("F_ZINSSATZ".equals(fieldName)) {
				value = data[index][REPORT_RA_SCHREIBEN_ZINSSATZ];
			}
		}
			break;
		case UC_EINFACHE_ERFOLGSRECHNUNG: {
			if ("F_MONAT".equals(fieldName)) {
				value = data[index][REPORT_EINFACHE_ERFOLGSRECHNUNG_MONAT];
			} else if ("F_NETTOUMSATZ_RE".equals(fieldName)) {
				value = data[index][REPORT_EINFACHE_ERFOLGSRECHNUNG_NETTOUMSATZ_RE];
			} else if ("F_NETTOUMSATZ_ER".equals(fieldName)) {
				value = data[index][REPORT_EINFACHE_ERFOLGSRECHNUNG_NETTOUMSATZ_ER];
			} else if ("F_NETTOUMSATZ_ZK".equals(fieldName)) {
				value = data[index][REPORT_EINFACHE_ERFOLGSRECHNUNG_NETTOUMSATZ_ZK];
			} else if ("F_PERSONALKOSTEN".equals(fieldName)) {
				value = data[index][REPORT_EINFACHE_ERFOLGSRECHNUNG_PERSONALKOSTEN];
			} else if ("F_LAGER_GESTEHUNGSWERT_ZUM_MONATSLETZTEN".equals(fieldName)) {
				value = data[index][REPORT_EINFACHE_ERFOLGSRECHNUNG_LAGER_GESTEHUNGSWERT_ZUM_MONATSLETZTEN];
			} else if ("F_HF_GESTEHUNGSWERT_ARBEIT_ZUM_MONATSLETZTEN".equals(fieldName)) {
				value = data[index][REPORT_EINFACHE_ERFOLGSRECHNUNG_HF_GESTEHUNGSWERT_ARBEIT_ZUM_MONATSLETZTEN];
			} else if ("F_HF_GESTEHUNGSWERT_MATERIAL_ZUM_MONATSLETZTEN".equals(fieldName)) {
				value = data[index][REPORT_EINFACHE_ERFOLGSRECHNUNG_HF_GESTEHUNGSWERT_MATERIAL_ZUM_MONATSLETZTEN];
			} else if ("F_PERSONALSTUNDEN".equals(fieldName)) {
				value = data[index][REPORT_EINFACHE_ERFOLGSRECHNUNG_PERSONALSTUNDEN];
			} else if ("F_MASCHINENSTUNDEN".equals(fieldName)) {
				value = data[index][REPORT_EINFACHE_ERFOLGSRECHNUNG_MASCHINENSTUNDEN];
			}
		}
			break;
		case UC_KASSENJOUNRAL: {
			if ("F_BELEGART".equals(fieldName)) {
				value = data[index][REPORT_KASSENJOURNAL_BELEGART];
			} else if ("F_BELEGNUMMER".equals(fieldName)) {
				value = data[index][REPORT_KASSENJOURNAL_BELEGNUMMER];
			} else if ("F_KUNDELIEFERANT".equals(fieldName)) {
				value = data[index][REPORT_KASSENJOURNAL_KUNDELIEFERANT];
			} else if ("F_BETRAG_AUS".equals(fieldName)) {
				value = data[index][REPORT_KASSENJOURNAL_BETRAG_AUS];
			} else if ("F_BETRAG_EIN".equals(fieldName)) {
				value = data[index][REPORT_KASSENJOURNAL_BETRAG_EIN];
			} else if ("F_USTBETRAG_AUS".equals(fieldName)) {
				value = data[index][REPORT_KASSENJOURNAL_USTBETRAG_AUS];
			} else if ("F_USTBETRAG_EIN".equals(fieldName)) {
				value = data[index][REPORT_KASSENJOURNAL_USTBETRAG_EIN];
			} else if ("F_TEXT".equals(fieldName)) {
				value = data[index][REPORT_KASSENJOURNAL_TEXT];
			} else if ("F_ZAHLDATUM".equals(fieldName)) {
				value = data[index][REPORT_KASSENJOURNAL_ZAHLDATUM];
			} else if ("F_KOMMENTAR".equals(fieldName)) {
				value = data[index][REPORT_KASSENJOURNAL_KOMMENTAR];
			}
		}
			break;
		case UC_UEBERWEISUNGSLISTE: {
			if ("F_ART".equals(fieldName)) {
				value = data[index][REPORT_UEBERWEISUNGSLISTE_ART];
			} else if ("F_EINGANGSRECHNUNG".equals(fieldName)) {
				value = data[index][REPORT_UEBERWEISUNGSLISTE_EINGANGSRECHNUNG];
			} else if ("F_MAHNSTUFE".equals(fieldName)) {
				value = data[index][REPORT_UEBERWEISUNGSLISTE_MAHNSTUFE];
			} else if ("F_WERT".equals(fieldName)) {
				value = data[index][REPORT_UEBERWEISUNGSLISTE_WERT];
			} else if ("F_PARTNER".equals(fieldName)) {
				value = data[index][REPORT_UEBERWEISUNGSLISTE_PARTNER];
			} else if ("F_FAELLIG".equals(fieldName)) {
				value = data[index][REPORT_UEBERWEISUNGSLISTE_FAELLIG];
			} else if ("F_SKONTO".equals(fieldName)) {
				value = data[index][REPORT_UEBERWEISUNGSLISTE_SKONTO];
			} else if ("F_ZAHLBETRAG".equals(fieldName)) {
				value = data[index][REPORT_UEBERWEISUNGSLISTE_ZAHLBETRAG];
			} else if ("F_WAEHRUNG".equals(fieldName)) {
				value = data[index][REPORT_UEBERWEISUNGSLISTE_WAEHRUNG];
			} else if ("F_TEXT".equals(fieldName)) {
				value = data[index][REPORT_UEBERWEISUNGSLISTE_TEXT];
			} else if ("F_FREIGABE_ZUR_UEBERWEISUNG".equals(fieldName)) {
				value = data[index][REPORT_UEBERWEISUNGSLISTE_FREIGABE_ZUR_UEBERWEISUNG];
			} else if ("F_BANK".equals(fieldName)) {
				value = data[index][REPORT_UEBERWEISUNGSLISTE_BANK];
			} else if ("F_IBAN".equals(fieldName)) {
				value = data[index][REPORT_UEBERWEISUNGSLISTE_IBAN];
			} else if ("F_BIC".equals(fieldName)) {
				value = data[index][REPORT_UEBERWEISUNGSLISTE_BIC];
			} else if ("F_GEPRUEFT_AM".equals(fieldName)) {
				value = data[index][REPORT_UEBERWEISUNGSLISTE_GEPRUEFT_AM];
			} else if ("F_GEPRUEFT_VON".equals(fieldName)) {
				value = data[index][REPORT_UEBERWEISUNGSLISTE_GEPRUEFT_VON];
			}

		}
			break;
		case UC_AENDERUNGEN_KONTO: {
			if ("Feldname".equals(fieldName)) {
				value = data[index][REPORT_AENDERUNGEN_KONTO_FELDNAME];
			} else if ("GeaendertVon".equals(fieldName)) {
				value = data[index][REPORT_AENDERUNGEN_KONTO_GEAENDERT_VON];
			} else if ("GeaendertNach".equals(fieldName)) {
				value = data[index][REPORT_AENDERUNGEN_KONTO_GEAENDERT_NACH];
			} else if ("Operation".equals(fieldName)) {
				value = data[index][REPORT_AENDERUNGEN_KONTO_OPERATION];
			} else if ("GeaendertDurch".equals(fieldName)) {
				value = data[index][REPORT_AENDERUNGEN_KONTO_GEAENDERT_DURCH];
			} else if ("Aenderungszeitpunkt".equals(fieldName)) {
				value = data[index][REPORT_AENDERUNGEN_KONTO_AENDERUNGSZEITPUNKT];
			}
		}
			break;
		case UC_KONTOBLATT: {
			value = reportKontoblatt.getFieldValue(fieldName);
			break;
		}
		case UC_KONTOBLATT_KASSENBUCH: {
			value = reportKassenbuch.getFieldValue(fieldName);
			break;
		}
		case UC_KONTENLISTE: {
			value = reportKontenListe.getFieldValue(fieldName);
			break;
		}
		case UC_ERFOLGSRECHNUNG: {
			value = reportErfolgsrechnung.getFieldValue(fieldName);
			break;
		}
		case UC_SALDENLISTE: {
			value = reportSaldenliste.getFieldValue(fieldName);
			break;
		}
		case UC_BUCHUNGSBELEG: {
			value = reportBuchungsbelegListe.getFieldValue(fieldName);
			break;
		}
		case UC_USTVERPROBUNG: {
			value = reportSteuernachweisListe.getFieldValue(fieldName);
			break;
		}
		// if ("F_KONTONUMMER".equals(fieldName)) {
		// value = data[index][REPORT_SALDENLISTE_KONTONUMMER];
		// } else if ("F_KONTOBEZEICHNUNG".equals(fieldName)) {
		// value = data[index][REPORT_SALDENLISTE_KONTOBEZEICHNUNG];
		// } else if ("F_SOLL".equals(fieldName)) {
		// value = data[index][REPORT_SALDENLISTE_SOLL];
		// } else if ("F_HABEN".equals(fieldName)) {
		// value = data[index][REPORT_SALDENLISTE_HABEN];
		// } else if ("F_EBWERT".equals(fieldName)) {
		// value = data[index][REPORT_SALDENLISTE_EBWERT];
		// } else if ("F_SOLL_BIS".equals(fieldName)) {
		// value = data[index][REPORT_SALDENLISTE_SOLL_BIS];
		// } else if ("F_HABEN_BIS".equals(fieldName)) {
		// value = data[index][REPORT_SALDENLISTE_HABEN_BIS];
		// } else if ("F_STEUERKATEGORIE_C_BEZ".equals(fieldName)) {
		// value = data[index][REPORT_SALDENLISTE_STEUERKATEGORIE];
		// } else if ("F_ERGEBNISGRUPPE".equals(fieldName)) {
		// value = data[index][REPORT_SALDENLISTE_ERGEBNISGRUPPE];
		// } else if ("F_EBWERT_BIS".equals(fieldName)) {
		// value = data[index][REPORT_SALDENLISTE_EBWERT_BIS];
		// } else if ("F_UVAART".equals(fieldName)) {
		// value = data[index][REPORT_SALDENLISTE_UVAART];
		// } else if ("F_FINANZAMT".equals(fieldName)) {
		// value = data[index][REPORT_SALDENLISTE_FINANZAMT];
		// }
		case UC_BUCHUNGEN_IN_BUCHUNGSJOURNAL: {
			if ("F_SOLL".equals(fieldName)) {
				value = data[index][REPORT_BUCHUNGEN_IN_BUCHUNGSJOURNAL_SOLL];
			} else if ("F_HABEN".equals(fieldName)) {
				value = data[index][REPORT_BUCHUNGEN_IN_BUCHUNGSJOURNAL_HABEN];
			} else if ("F_BUCHUNGSDATUM".equals(fieldName)) {
				value = data[index][REPORT_BUCHUNGEN_IN_BUCHUNGSJOURNAL_BUCHUNGSDATUM];
			} else if ("F_BUCHUNGSART".equals(fieldName)) {
				value = data[index][REPORT_BUCHUNGEN_IN_BUCHUNGSJOURNAL_BUCHUNGSART];
			} else if ("F_KONTONUMMER".equals(fieldName)) {
				value = data[index][REPORT_BUCHUNGEN_IN_BUCHUNGSJOURNAL_KONTONUMMER];
			} else if ("F_KONTOBEZEICHNUNG".equals(fieldName)) {
				value = data[index][REPORT_BUCHUNGEN_IN_BUCHUNGSJOURNAL_KONTOBEZEICHNUNG];
			} else if ("F_TEXT".equals(fieldName)) {
				value = data[index][REPORT_BUCHUNGEN_IN_BUCHUNGSJOURNAL_TEXT];
			}
		}
			break;
		case UC_BUCHUNGSJOURNAL: {
			value = reportBuchungsjournal.getFieldValue(fieldName);
			// if ("F_BUCHUNGSJOURNALDATUM".equals(fieldName)) {
			// value = data[index][REPORT_BUCHUNGSJOURNAL_BUCHUNGSJOURNALDATUM];
			// } else if ("F_BUCHUNGSDATUM".equals(fieldName)) {
			// value = data[index][REPORT_BUCHUNGSJOURNAL_BUCHUNGSDATUM];
			// } else if ("F_KONTONUMMER".equals(fieldName)) {
			// value = data[index][REPORT_BUCHUNGSJOURNAL_KONTONUMMER];
			// } else if ("F_GEGENKONTONUMMER".equals(fieldName)) {
			// value = data[index][REPORT_BUCHUNGSJOURNAL_GEGENKONTONUMMER];
			// } else if ("F_BUCHUNGSART".equals(fieldName)) {
			// value = data[index][REPORT_BUCHUNGSJOURNAL_BUCHUNGSART];
			// } else if ("F_BELEGNUMMER".equals(fieldName)) {
			// value = data[index][REPORT_BUCHUNGSJOURNAL_BELEGNUMMER];
			// } else if ("F_TEXT".equals(fieldName)) {
			// value = data[index][REPORT_BUCHUNGSJOURNAL_TEXT];
			// } else if ("F_BETRAG".equals(fieldName)) {
			// value = data[index][REPORT_BUCHUNGSJOURNAL_BETRAG];
			// } else if ("F_UST".equals(fieldName)) {
			// value = data[index][REPORT_BUCHUNGSJOURNAL_UST];
			// } else if ("F_STORNIERT".equals(fieldName)) {
			// value = data[index][REPORT_BUCHUNGSJOURNAL_STORNIERT];
			// } else if ("F_KOSTENSTELLENUMMER".equals(fieldName)) {
			// value = data[index][REPORT_BUCHUNGSJOURNAL_KOSTENSTELLENUMMER];
			// } else if ("F_AUSZUG".equals(fieldName)) {
			// value = data[index][REPORT_BUCHUNGSJOURNAL_AUSZUG];
			// } else if ("F_AM".equals(fieldName)) {
			// value = data[index][REPORT_BUCHUNGSJOURNAL_AM];
			// } else if ("F_UM".equals(fieldName)) {
			// value = data[index][REPORT_BUCHUNGSJOURNAL_UM];
			// } else if ("F_SOLL".equals(fieldName)) {
			// value = data[index][REPORT_BUCHUNGSJOURNAL_SOLL];
			// } else if ("F_HABEN".equals(fieldName)) {
			// value = data[index][REPORT_BUCHUNGSJOURNAL_HABEN];
			// } else if ("F_WER".equals(fieldName)) {
			// value = data[index][REPORT_BUCHUNGSJOURNAL_WER];
			// } else if ("F_BELEGART_KBEZ".equals(fieldName)) {
			// value = data[index][REPORT_BUCHUNGSJOURNAL_BELEGART_KBEZ] ;
			// } else if ("F_BUCHUNGSART_KBEZ".equals(fieldName)) {
			// value = data[index][REPORT_BUCHUNGSJOURNAL_BUCHUNGSART_KBEZ] ;
			// }
		}
			break;
		case UC_BUCHUNGEN_AUF_KONTO: {
			if ("F_BUCHUNGSDATUM".equals(fieldName)) {
				value = data[index][REPORT_BUCHUNGEN_AUF_KONTO_BUCHUNGSDATUM];
			} else if ("F_BUCHUNGSART".equals(fieldName)) {
				value = data[index][REPORT_BUCHUNGEN_AUF_KONTO_BUCHUNGSART];
			} else if ("F_TEXT".equals(fieldName)) {
				value = data[index][REPORT_BUCHUNGEN_AUF_KONTO_TEXT];
			} else if ("F_BETRAG".equals(fieldName)) {
				value = data[index][REPORT_BUCHUNGEN_AUF_KONTO_BETRAG];
			} else if ("F_UST".equals(fieldName)) {
				value = data[index][REPORT_BUCHUNGEN_AUF_KONTO_UST];
			} else if ("F_AUSZUG".equals(fieldName)) {
				value = data[index][REPORT_BUCHUNGEN_AUF_KONTO_AUSZUG];
			}
		}
			break;
		case UC_STEUERKATEGORIEN: {
			if ("F_FINANZAMT".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_FINANZAMT];
			} else if ("F_FINANZAMT_I_ID".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_FINANZAMT_I_ID];
			} else if ("F_STEUERKATEGORIE".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_STEUERKATEGORIE];
			} else if ("F_STEUERKATEGORIEBEZEICHNUNG".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_STEUERKATEGORIEBEZEICHNUNG];
			} else if ("F_KONTO_VK".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_KONTO_VK];
			} else if ("F_KONTO_EK".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_KONTO_EK];
			} else if ("F_KONTO_SKONTO_EK".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_KONTO_SKONTO_EK];
			} else if ("F_KONTO_SKONTO_VK".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_KONTO_SKONTO_VK];
			} else if ("F_KONTO_EINFUHRUST".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_KONTO_EINFUHRUST];
			} else if ("F_KONTO_VK_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_KONTO_VK_BEZEICHNUNG];
			} else if ("F_KONTO_EK_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_KONTO_EK_BEZEICHNUNG];
			} else if ("F_KONTO_SKONTO_EK_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_KONTO_SKONTO_EK_BEZEICHNUNG];
			} else if ("F_KONTO_SKONTO_VK_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_KONTO_SKONTO_VK_BEZEICHNUNG];
			} else if ("F_KONTO_EINFUHRUST_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_KONTO_EINFUHRUST_BEZEICHNUNG];
			} else if ("F_SUBREPORT_BUCHUNGSPARAMETER".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_KONTO_EINFUHRUST_BEZEICHNUNG];
			}

			else if ("F_PARAMETER_SACHKONTO".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_PARAMETER_SACHKONTO];
			} else if ("F_PARAMETER_SACHKONTO_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_PARAMETER_SACHKONTO_BEZEICHNUNG];
			}

			else if ("F_PARAMETER_DEBITOREN".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_PARAMETER_DEBITOREN];
			} else if ("F_PARAMETER_DEBITOREN_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_PARAMETER_DEBITOREN_BEZEICHNUNG];
			} else if ("F_PARAMETER_KREDITOREN".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_PARAMETER_KREDITOREN];
			} else if ("F_PARAMETER_KREDITOREN_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_PARAMETER_KREDITOREN_BEZEICHNUNG];
			} else if ("F_PARAMETER_VERR_ERHALTEN".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_PARAMETER_VERR_ERHALTEN];
			} else if ("F_PARAMETER_VERR_ERHALTEN_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_PARAMETER_VERR_ERHALTEN_BEZEICHNUNG];
			} else if ("F_PARAMETER_ERHALTEN".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_PARAMETER_ERHALTEN];
			} else if ("F_PARAMETER_ERHALTEN_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_PARAMETER_ERHALTEN_BEZEICHNUNG];
			} else if ("F_PARAMETER_VERR_GELEISTET".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_PARAMETER_VERR_GELEISTET];
			} else if ("F_PARAMETER_VERR_GELEISTET_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_PARAMETER_VERR_GELEISTET_BEZEICHNUNG];
			} else if ("F_PARAMETER_GELEISTET".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_PARAMETER_GELEISTET];
			} else if ("F_PARAMETER_GELEISTET_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_PARAMETER_GELEISTET_BEZEICHNUNG];
			}

			else if ("F_KONTO_KURSVERLUSTE".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_KONTO_KURSVERLUSTE];
			} else if ("F_KONTO_KURSVERLUSTE_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_KONTO_KURSVERLUSTE_BEZEICHNUNG];
			} else if ("F_SAMMELKONTO_MAP".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_SAMMELKONTO_MAP];
			} else if ("F_KONTO_KURSGEWINNE".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_KONTO_KURSGEWINNE];
			} else if ("F_KONTO_KURSGEWINNE_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_KONTO_KURSGEWINNE_BEZEICHNUNG];
			} else if ("F_KONTO_FORDERUNGEN".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_KONTO_FORDERUNGEN];
			} else if ("F_KONTO_FORDERUNGEN_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_KONTO_FORDERUNGEN_BEZEICHNUNG];
			} else if ("F_KONTO_VERBINDLICHKEITEN".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_KONTO_VERBINDLICHKEITEN];
			} else if ("F_KONTO_VERBINDLICHKEITEN_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_KONTO_VERBINDLICHKEITEN_BEZEICHNUNG];
			} else if ("F_REVERSECHARGE".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_REVERSECHARGE];
			} else if ("F_MWST_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_MWST_BEZEICHNUNG];
			} else if ("F_REVERSECHARGEART_I_ID".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_REVERSECHARGEART_I_ID];
			} else if ("F_REVERSECHARGEART_C_NR".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_REVERSECHARGEART_C_NR];
			} else if ("F_REVERSECHARGEART_BEZ".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_REVERSECHARGEART_BEZ];
			} else if ("F_GUELTIG_AB".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_GUELTIG_AB];
			}

		}
			break;
		case UC_SAMMELMAHNUNG: {
			if ("F_BELEGDATUM".equals(fieldName)) {
				value = data[index][REPORT_SAMMELMAHNUNG_BELEGDATUM];
			} else if ("F_FAELLIGKEITSDATUM".equals(fieldName)) {
				value = data[index][REPORT_SAMMELMAHNUNG_FAELLIGKEITSDATUM];
			} else if ("F_MAHNSTUFE".equals(fieldName)) {
				value = data[index][REPORT_SAMMELMAHNUNG_MAHNSTUFE];
			} else if ("F_OFFEN".equals(fieldName)) {
				value = data[index][REPORT_SAMMELMAHNUNG_OFFEN];
			} else if ("F_RECHNUNGSNUMMER".equals(fieldName)) {
				value = data[index][REPORT_SAMMELMAHNUNG_RECHNUNGSNUMMER];
			} else if ("F_WERT".equals(fieldName)) {
				value = data[index][REPORT_SAMMELMAHNUNG_WERT];
			} else if ("F_ZINSEN".equals(fieldName)) {
				value = data[index][REPORT_SAMMELMAHNUNG_ZINSEN];
			} else if ("F_BELEGWAEHRUNG".equals(fieldName)) {
				value = data[index][REPORT_SAMMELMAHNUNG_BELEGWAEHRUNG];
			} else if ("F_OFFEN_BELEGWAEHRUNG".equals(fieldName)) {
				value = data[index][REPORT_SAMMELMAHNUNG_OFFEN_BELEGWAEHRUNG];
			} else if ("F_WERT_BELEGWAEHRUNG".equals(fieldName)) {
				value = data[index][REPORT_SAMMELMAHNUNG_WERT_BELEGWAEHRUNG];
			}

		}
			break;
		case UC_MAHNLAUF: {
			if ("F_FIRMA".equals(fieldName)) {
				value = data[index][REPORT_MAHNLAUF_KUNDE];
			} else if ("F_LETZTEMAHNSTUFE".equals(fieldName)) {
				value = data[index][REPORT_MAHNLAUF_LETZTEMAHNSTUFE];
			} else if ("F_LETZTESMAHNDATUM".equals(fieldName)) {
				value = data[index][REPORT_MAHNLAUF_LETZTESMAHNDATUM];
			} else if ("F_MAHNSTUFE".equals(fieldName)) {
				value = data[index][REPORT_MAHNLAUF_MAHNSTUFE];
			} else if ("F_OFFEN".equals(fieldName)) {
				value = data[index][REPORT_MAHNLAUF_OFFEN];
			} else if ("F_RECHNUNGSDATUM".equals(fieldName)) {
				value = data[index][REPORT_MAHNLAUF_RECHNUNGSDATUM];
			} else if ("F_RECHNUNGSNUMMER".equals(fieldName)) {
				value = data[index][REPORT_MAHNLAUF_RECHNUNGSNUMMER];
			} else if ("F_RECHNUNGSART".equals(fieldName)) {
				value = data[index][REPORT_MAHNLAUF_RECHNUNGSART];
			} else if ("F_MAHNSPERREBIS".equals(fieldName)) {
				value = data[index][REPORT_MAHNLAUF_MAHNSPERREBIS];
			} else if ("F_WERT".equals(fieldName)) {
				value = data[index][REPORT_MAHNLAUF_WERT];
			} else if ("F_ZIELDATUM".equals(fieldName)) {
				value = data[index][REPORT_MAHNLAUF_ZIELDATUM];
			} else if ("F_AUFTRAG_NUMMER".equals(fieldName)) {
				value = data[index][REPORT_MAHNLAUF_AUFTRAG_NUMMER];
			} else if ("F_AUFTRAG_PROJEKT".equals(fieldName)) {
				value = data[index][REPORT_MAHNLAUF_AUFTRAG_PROJEKT];
			} else if ("F_AUFTRAG_BESTELLNUMMER".equals(fieldName)) {
				value = data[index][REPORT_MAHNLAUF_AUFTRAG_BESTELLNUMMER];
			} else if ("F_POSITIONSBEZEICHNUNG1".equals(fieldName)) {
				value = data[index][REPORT_MAHNLAUF_POSITIONSBEZEICHNUNG1];
			} else if ("F_POSITIONSBEZEICHNUNG2".equals(fieldName)) {
				value = data[index][REPORT_MAHNLAUF_POSITIONSBEZEICHNUNG2];
			} else if ("F_KUNDE_STATISTIKADRESSE".equals(fieldName)) {
				value = data[index][REPORT_MAHNLAUF_KUNDE_STATISTIKADRESSE];
			} else if ("F_VERTRETER".equals(fieldName)) {
				value = data[index][REPORT_MAHNLAUF_VERTRETER];
			} else if ("F_WERTUST".equals(fieldName)) {
				value = data[index][REPORT_MAHNLAUF_WERTUST];
			} else if ("F_OFFENUST".equals(fieldName)) {
				value = data[index][REPORT_MAHNLAUF_OFFENUST];
			} else if ("F_RECHNUNG_STATUS".equals(fieldName)) {
				value = data[index][REPORT_MAHNLAUF_RECHNUNG_STATUS];
			} else if ("F_ZAHLUNGSZIEL".equals(fieldName)) {
				value = data[index][REPORT_MAHNLAUF_ZAHLUNGSZIEL];
			}
		}
			break;
		case UC_INTRASTAT: {
			if ("F_BELEGNUMMER".equals(fieldName)) {
				value = data[index][REPORT_INTRASTAT_BELEGNUMMER];
			} else if ("F_IDENT".equals(fieldName)) {
				value = data[index][REPORT_INTRASTAT_IDENT];
			} else if ("F_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][REPORT_INTRASTAT_BEZEICHNUNG];
			} else if ("F_MENGE".equals(fieldName)) {
				value = data[index][REPORT_INTRASTAT_MENGE];
			} else if ("F_WARENVERKEHRSNUMMER".equals(fieldName)) {
				value = data[index][REPORT_INTRASTAT_WARENVERKEHRSNUMMER];
			} else if ("F_PREIS".equals(fieldName)) {
				value = data[index][REPORT_INTRASTAT_PREIS];
			} else if ("F_WERT".equals(fieldName)) {
				value = data[index][REPORT_INTRASTAT_WERT];
			} else if ("F_STATISTISCHERWERT".equals(fieldName)) {
				value = data[index][REPORT_INTRASTAT_STATISTISCHER_WERT];
			} else if ("F_GEWICHT".equals(fieldName)) {
				value = data[index][REPORT_INTRASTAT_GEWICHT];
			} else if ("F_BELEGART".equals(fieldName)) {
				value = data[index][REPORT_INTRASTAT_BELEGART];
			} else if ("F_WVK_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][REPORT_INTRASTAT_WVK_BEZEICHNUNG];
			} else if ("F_UID".equals(fieldName)) {
				value = data[index][REPORT_INTRASTAT_UID];
			} else if ("F_URSPRUNGSLAND".equals(fieldName)) {
				value = data[index][REPORT_INTRASTAT_URSPRUNGSLAND];
			}
		}
			break;
		case UC_OFFENEPOSTEN: {
			if ("Auszug".equals(fieldName)) {
				value = data[index][REPORT_OP_AUSZUG];
			} else if ("Beleg".equals(fieldName)) {
				value = data[index][REPORT_OP_BELEG];
			} else if ("Buchungsart".equals(fieldName)) {
				value = data[index][REPORT_OP_BUCHUNGSART];
			} else if ("Buchungsdatum".equals(fieldName)) {
				value = data[index][REPORT_OP_BUCHUNGSDATUM];
			} else if ("Gegenkonto".equals(fieldName)) {
				value = data[index][REPORT_OP_GEGENKONTO];
			} else if ("Haben".equals(fieldName)) {
				value = data[index][REPORT_OP_HABEN];
			} else if ("Soll".equals(fieldName)) {
				value = data[index][REPORT_OP_SOLL];
			} else if ("Text".equals(fieldName)) {
				value = data[index][REPORT_OP_TEXT];
			} else if ("UstProzent".equals(fieldName)) {
				value = data[index][REPORT_OP_USTPROZENT];
			} else if ("UstWert".equals(fieldName)) {
				value = data[index][REPORT_OP_USTWERT];
			} else if ("BelegartCNr".equals(fieldName)) {
				value = data[index][REPORT_OP_BELEGARTCNR];
			} else if ("KontoCNr".equals(fieldName)) {
				value = data[index][REPORT_OP_KONTOCNR];
			} else if ("AZK".equals(fieldName)) {
				value = data[index][REPORT_OP_AZK];
			} else if ("AzkSummeFehlerhaft".equals(fieldName)) {
				value = data[index][REPORT_OP_AZK_SUMME_FEHLERHAFT];
			}
		}
			break;
		case UC_LIQUIDITAET: {
			if ("Beleg".equals(fieldName)) {
				value = data[index][REPORT_LV_BELEG];
			} else if ("Belegunterart".equals(fieldName)) {
				value = data[index][REPORT_LV_BELEGUNTERART];
			} else if ("Jahr".equals(fieldName)) {
				value = data[index][REPORT_LV_JAHR];
			} else if ("Mwst".equals(fieldName)) {
				value = data[index][REPORT_LV_MWST];
			} else if ("Netto".equals(fieldName)) {
				value = data[index][REPORT_LV_NETTO];
			} else if ("Partner".equals(fieldName)) {
				value = data[index][REPORT_LV_PARTNER];
			} else if ("Woche".equals(fieldName)) {
				value = data[index][REPORT_LV_WOCHE];
			} else if ("ZieltageZahlungsmoral".equals(fieldName)) {
				value = data[index][REPORT_LV_ZIELTAGE_ZAHLUGNSMORAL];
			} else if ("ZieltageVereinbartesZahlungsziel".equals(fieldName)) {
				value = data[index][REPORT_LV_ZIELTAGE_VEREINBARTES_ZAHLUNGSZIEL];
			} else if ("Importiert".equals(fieldName)) {
				value = data[index][REPORT_LV_IMPORTIERT];
			} else if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_LV_ARTIKELNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_LV_BEZEICHNUNG];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_LV_ZUSATZBEZEICHNUNG];
			}
		}
			break;

		case UC_UVA: {
			if ("UvaRpt".equals(fieldName)) {
				value = data[index][REPORT_UVA_UVARPT];
			} else if ("UvaartCnr".equals(fieldName)) {
				value = data[index][REPORT_UVA_UVAART];
			} else if ("Kennzahl".equals(fieldName)) {
				value = data[index][REPORT_UVA_KENNZAHL];
			} else if ("Umsatz".equals(fieldName)) {
				value = data[index][REPORT_UVA_UMSATZ];
			} else if ("Umsatzgerundet".equals(fieldName)) {
				value = data[index][REPORT_UVA_UMSATZGERUNDET];
			} else if ("Satz".equals(fieldName)) {
				value = data[index][REPORT_UVA_SATZ];
			} else if ("Gruppe".equals(fieldName)) {
				value = data[index][REPORT_UVA_GRUPPE];
			} else if ("Sortierung".equals(fieldName)) {
				value = data[index][REPORT_UVA_SORT];
			} else if ("Alternative".equals(fieldName)) {
				value = data[index][REPORT_UVA_ALTERNATIVE];
			} else if ("Kontouvavariante".equals(fieldName)) {
				value = data[index][REPORT_UVA_KONTOVARIANTE];
			} else if ("MwstsatzDto".equals(fieldName)) {
				value = data[index][REPORT_UVA_MWSTSATZ];
			} else if ("AnzahlungUmsatz".equals(fieldName)) {
				value = data[index][REPORT_UVA_ANZAHLUNG_UMSATZ];
			} else if ("AnzahlungUmsatzgerundet".equals(fieldName)) {
				value = data[index][REPORT_UVA_ANZAHLUNG_UMSATZGERUNDET];
			}
		}
			break;

		}
		return value;
	}

	public class ReportParameter {
		private HashMap<String, Object> parameter;

		public ReportParameter() {
			parameter = new HashMap<String, Object>();
		}

		public void put(String key, Object value) {
			parameter.put(key, value);
		}

		public void putIfNotNull(String key, Object value) {
			if (value != null) {
				put(key, value);
			}
		}

		public HashMap<String, Object> getParameters() {
			return parameter;
		}
	}

	public class KontoblaetterPrinter {
		private ReportParameter reportParameter;
		private Integer vonPeriode;
		private Map<String, String> mapAnzahlungsCnr = new HashMap<String, String>() {
			private static final long serialVersionUID = -7477136424185048969L;
			{
				put(RechnungFac.RECHNUNGART_ANZAHLUNG, "Anzahlung");
				put(RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG, "Schlussrechnung");
				put(EingangsrechnungFac.EINGANGSRECHNUNGART_ANZAHLUNG, "Anzahlung");
				put(EingangsrechnungFac.EINGANGSRECHNUNGART_SCHLUSSZAHLUNG, "Schlussrechnung");
			}
		};

		public class BuchungParameter {

			boolean druckInMandantenWaehrung;
			BuchungdetailText buchungdetailText;
			boolean doConsistentCheck = true;
			Timestamp[] tVonbis;
			boolean istSachkonto;
			boolean istBankOderKasse;
			String waehrungDruck;

			public BuchungParameter(PrintKontoblaetterModel kbModel, FLRFinanzKonto konto, TheClientDto theClientDto) {

				setBuchungdetailText(new BuchungdetailText(getBenutzerServicesFac(), theClientDto));

				setDruckInMandantenWaehrung(kbModel.isDruckInMandantenWaehrung());

				if (konto.getI_eb_geschaeftsjahr() == null) {
					setDoConsistentCheck(false);
				}
				if (this.doConsistentCheck && !konto.getI_eb_geschaeftsjahr().equals(kbModel.getGeschaeftsjahr())) {
					setDoConsistentCheck(false);
				}

				settVonbis(getBuchenFac().getDatumbereichPeriodeGJ(kbModel.getGeschaeftsjahr() - 1, -1, theClientDto));

				setIstSachkonto(konto.getKontotyp_c_nr().equals(FinanzServiceFac.KONTOTYP_SACHKONTO));

				setIstBankOderKasse(getFinanzFac().isKontoMitSaldo(konto.getI_id(), theClientDto));

				setWaehrungDruck(konto.getWaehrung_c_nr_druck());

			}

			public boolean isDruckInMandantenWaehrung() {
				return druckInMandantenWaehrung;
			}

			private void setDruckInMandantenWaehrung(boolean druckInMandantenWaehrung) {
				this.druckInMandantenWaehrung = druckInMandantenWaehrung;
			}

			public BuchungdetailText getBuchungdetailText() {
				return buchungdetailText;
			}

			private void setBuchungdetailText(BuchungdetailText buchungdetailText) {
				this.buchungdetailText = buchungdetailText;
			}

			public boolean isDoConsistentCheck() {
				return doConsistentCheck;
			}

			private void setDoConsistentCheck(boolean doConsistentCheck) {
				this.doConsistentCheck = doConsistentCheck;
			}

			public Timestamp[] gettVonbis() {
				return tVonbis;
			}

			private void settVonbis(Timestamp[] tVonbis) {
				this.tVonbis = tVonbis;
			}

			public boolean isIstSachkonto() {
				return istSachkonto;
			}

			private void setIstSachkonto(boolean istSachkonto) {
				this.istSachkonto = istSachkonto;
			}

			public boolean isIstBankOderKasse() {
				return istBankOderKasse;
			}

			private void setIstBankOderKasse(boolean istBankOderKasse) {
				this.istBankOderKasse = istBankOderKasse;
			}

			public String getWaehrungDruck() {
				return waehrungDruck;
			}

			private void setWaehrungDruck(String waehrungDruck) {
				this.waehrungDruck = waehrungDruck;
			}

		}

		public KontoblaetterPrinter() {
			reportParameter = new ReportParameter();
		}

		public JasperPrintLP print(PrintKontoblaetterModel kbModel, TheClientDto theClientDto)
				throws EJBExceptionLP, RemoteException {
			if (null == kbModel)
				throw new IllegalArgumentException("kbModel");

			JasperPrintLP print = null;

			calculateBeginnPeriode(kbModel, theClientDto);

			Session session = FLRSessionFactory.getFactory().openSession();
			List<?> blaetter = getKontoBlaetter(session, kbModel, theClientDto);

			settBisToNextDay(kbModel);

			Iterator<?> blaetterIterator = blaetter.iterator();
			while (blaetterIterator.hasNext()) {
				FLRFinanzKonto konto = (FLRFinanzKonto) blaetterIterator.next();
				EnumSortOrder sortOrder = getSortOrderForKonto(konto, kbModel.getKontoIId() == null, kbModel);

				Session session2 = FLRSessionFactory.getFactory().openSession();
				List<?> blattDetails = getKontoDetails(session2, kbModel, konto, sortOrder, theClientDto);
				// boolean istSachkonto = konto.getKontotyp_c_nr().equals(
				// FinanzServiceFac.KONTOTYP_SACHKONTO);
				// boolean istBankoderKasse = getFinanzFac().isKontoMitSaldo(
				// konto.getI_id(), theClientDto);

				data = getReportBuchungen(blattDetails.size(), blattDetails.iterator(), kbModel, konto, theClientDto);

				verifyKontoIsConsistentWithEB(kbModel, konto, blattDetails.iterator(), theClientDto);
				session2.close();

				initJRDS(reportParameter.getParameters(), FinanzReportFac.REPORT_MODUL, getReportName(),
						theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

				if (print != null) {
					print = Helper.addReport2Report(print, getReportPrint().getPrint());
				} else {
					print = getReportPrint();
				}
			}

			session.close();

			if (null != kbModel.getPeriodeImGJ()) {
				PrintInfoDto values = new PrintInfoDto();
				values.setDocPath(new DocPath(new DocNodeKassenbuch(kbModel, theClientDto.getMandant())));
				// values[0] = JCRDocFac.HELIUMV_NODE + "/"
				// + LocaleFac.BELEGART_FINANZBUCHHALTUNG.trim() + "/"
				// + kbModel.getGeschaeftsjahrString() + "/Kassenbuch/"
				// + kbModel.getPeriodeImGJ();
				values.setiId(theClientDto.getIDPersonal());
				values.setTable("");

				print.setOInfoForArchive(values);
			}

			return print;
		}

		protected void settBisToNextDay(PrintKontoblaetterModel kbModel) {
			java.sql.Timestamp bisStamp = Helper
					.addiereTageZuTimestamp(new java.sql.Timestamp(kbModel.gettBis().getTime()), 1);
			kbModel.settBis(bisStamp);
		}

		protected void recalculateDateRangeBasedOnPeriode(PrintKontoblaetterModel kbModel, TheClientDto theClientDto) {
			Timestamp[] tVonBis = getBuchenFac().getDatumVonBisGeschaeftsjahr(kbModel.getGeschaeftsjahr(),
					theClientDto);
			if (tVonBis != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(tVonBis[0].getTime());
				cal.add(Calendar.MONTH, vonPeriode - 1);

				kbModel.settVon(new Timestamp(cal.getTimeInMillis()));
				kbModel.settVonReport(kbModel.gettVon());

				// Das Ende-Datum wird auf den letzten Tag der Periode gesetzt
				cal.setTimeInMillis(tVonBis[0].getTime());
				cal.add(Calendar.MONTH, vonPeriode);
				cal.add(Calendar.DAY_OF_MONTH, -1);
				kbModel.settBis(new Timestamp(cal.getTimeInMillis()));

				kbModel.settBisReport(new Timestamp(cal.getTimeInMillis()));

				// kbModel.settBis(tVonBis[1]);
			} else {
				kbModel.settVon(new Timestamp(0));
				kbModel.settBis(new Timestamp(0));
			}
		}

		protected void calculateBeginnPeriode(PrintKontoblaetterModel kbModel, TheClientDto theClientDto) {
			if (kbModel.getPeriodeImGJ() != null) {
				vonPeriode = kbModel.getPeriodeImGJ();
				if (vonPeriode < 1)
					vonPeriode = 1;
				recalculateDateRangeBasedOnPeriode(kbModel, theClientDto);

				return;
			}

			vonPeriode = null;
			if (kbModel.gettVon() != null) {
				java.sql.Date dVon = new java.sql.Date(kbModel.gettVon().getTime());
				vonPeriode = getBuchenFac().getPeriodeImGJFuerDatum(dVon, theClientDto.getMandant());
			}
		}

		/**
		 * Ermittelt den Saldo f&uuml;r das Konto aus der Vorperiode</br>
		 * 
		 * Ausgehend vom Beginndatum wird die Vorperiode ermittelt und f&uuml;r diese
		 * dann der Saldo des Kontos errechnet.</br>
		 * Es gibt Konten die keinen Vorsaldo kennen/k&ouml;nnen. F&uuml;r diese wird
		 * null geliefert
		 * 
		 * @param kontoIId         das zu ermittelnde Konto
		 * @param kbModel          das DatenModel zum Kontoblattdruck
		 * @param vonPeriode       Ist das Beginndatum der aktuellen Periode
		 * @param waehrungCNrDruck die CNR der beim Druck zu verwendenden W&auml;hrung
		 * @param theClientDto
		 * @return Saldo der Vorperiode. F&uuml;r Konten die keinen Saldo
		 *         kennen/k&ouml;nnen oder wo die Vorperiode im vorherigen
		 *         Gesch&auml;ftsjahr liegt wird null geliefert.
		 * @throws RemoteException
		 * @throws EJBExceptionLP
		 */
		protected BigDecimal calculateSaldoVonKontoFuerVorPeriode(Integer kontoIId, PrintKontoblaetterModel kbModel,
				Integer vonPeriode, String waehrungCNrDruck, TheClientDto theClientDto)
				throws EJBExceptionLP, RemoteException {

			// Wenn es kein Vorsaldo gibt (Kreditoren/Debitorenkonto, bzw. keine
			// Bank/Kassakonten dann null liefern
			BigDecimal vorperiodenSaldo = null;

			if (kbModel.isEnableSaldo() && (vonPeriode != null && vonPeriode > 1)) {
				if (kbModel.isDruckInMandantenWaehrung() || waehrungCNrDruck == null
						|| waehrungCNrDruck.equals(theClientDto.getSMandantenwaehrung())) {
					Timestamp ts[] = getBuchenFac().getDatumbereichPeriodeGJ(kbModel.getGeschaeftsjahr(), -1,
							theClientDto);
					vorperiodenSaldo = getBuchenFac().getSaldoOhneEBVonKonto(kontoIId, ts[0], kbModel.gettVon(), true,
							theClientDto);
					vorperiodenSaldo = vorperiodenSaldo
							.add(getBuchenFac().getSummeEroeffnungKontoIId(kontoIId, ts[0], kbModel.gettVon()));

					// vorperiodenSaldo = getBuchenFac().getSaldoOhneEBVonKonto(
					// kontoIId, kbModel.getGeschaeftsjahr(),
					// vonPeriode - 1, true, theClientDto);
					// vorperiodenSaldo = vorperiodenSaldo.add(getBuchenFac()
					// .getSummeEroeffnungKontoIId(kontoIId,
					// kbModel.getGeschaeftsjahr(),
					// vonPeriode - 1, true, theClientDto));
				} else {
					Timestamp ts[] = getBuchenFac().getDatumbereichPeriodeGJ(kbModel.getGeschaeftsjahr(), -1,
							theClientDto);
					vorperiodenSaldo = getBuchenFac().getSaldoVonKontoInWaehrung(kontoIId, ts[0], kbModel.gettVon(),
							true, waehrungCNrDruck, theClientDto);
					vorperiodenSaldo = vorperiodenSaldo.add(getBuchenFac().getSummeEroeffnungKontoIIdInWaehrung(
							kontoIId, ts[0], kbModel.gettVon(), waehrungCNrDruck, theClientDto));
					// vorperiodenSaldo = getBuchenFac()
					// .getSaldoVonKontoInWaehrung(kontoIId,
					// kbModel.getGeschaeftsjahr(),
					// vonPeriode - 1, true, waehrungCNrDruck,
					// theClientDto);
					// vorperiodenSaldo = vorperiodenSaldo.add(getBuchenFac()
					// .getSummeEroeffnungKontoIIdInWaehrung(kontoIId,
					// kbModel.getGeschaeftsjahr(),
					// vonPeriode - 1, true, waehrungCNrDruck,
					// theClientDto));
				}
				if (null != vorperiodenSaldo) {
					vorperiodenSaldo = vorperiodenSaldo.setScale(FinanzFac.NACHKOMMASTELLEN);
				}
			}

			return vorperiodenSaldo;
		}

		protected PrintKontoblaetterModel.EnumSortOrder getEnumSortOrderFromKonto(String kontoC_sortierung) {
			if ("Beleg".equals(kontoC_sortierung)) {
				return PrintKontoblaetterModel.EnumSortOrder.SORT_NACH_BELEG;
			}

			if ("Datum".equals(kontoC_sortierung)) {
				return PrintKontoblaetterModel.EnumSortOrder.SORT_NACH_DATUM;
			}

			if ("Auszug".equals(kontoC_sortierung)) {
				return PrintKontoblaetterModel.EnumSortOrder.SORT_NACH_AUSZUG;
			}

			return EnumSortOrder.SORT_UNDEFINED;
		}

		protected EnumSortOrder getSortOrderForKonto(FLRFinanzKonto account, boolean multipleAccounts,
				PrintKontoblaetterModel kbModel) {
			PrintKontoblaetterModel.EnumSortOrder sortOrder = kbModel.getSortOrder();

			if (multipleAccounts) {
				EnumSortOrder kontoSortOrder = getEnumSortOrderFromKonto(account.getC_sortierung());
				if (kontoSortOrder != EnumSortOrder.SORT_UNDEFINED) {
					sortOrder = kontoSortOrder;
				}
			}

			return sortOrder;
		}

		protected List<?> getKontoBlaetter(Session session, PrintKontoblaetterModel kbModel,
				TheClientDto theClientDto) {
			Criteria crit = session.createCriteria(FLRFinanzKonto.class)
					.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));

			addCriteriasForKontos(crit, kbModel);
			addParametersForKontos(kbModel, theClientDto);

			return crit.list();
		}

		protected List<FLRFinanzBuchungDetail> getKontoDetails(Session session2, PrintKontoblaetterModel kbModel,
				FLRFinanzKonto konto, EnumSortOrder sortOrder, TheClientDto theClientDto)
				throws EJBExceptionLP, RemoteException {

			addParametersForDetails(kbModel, sortOrder, konto, theClientDto);

			HvTypedCriteria<FLRFinanzBuchungDetail> crit2 = new HvTypedCriteria<FLRFinanzBuchungDetail>(
					session2.createCriteria(FLRFinanzBuchungDetail.class));

			List<FLRFinanzBuchungDetail> list = new ArrayList<FLRFinanzBuchungDetail>();
			if (kbModel.isSortiereNachAZK()) {
				addCriteriasForDetails(crit2, kbModel, sortOrder, konto, false);
				addOrderForDetails(crit2, kbModel, sortOrder, konto, false);
				list.addAll(crit2.list());
				crit2 = new HvTypedCriteria<FLRFinanzBuchungDetail>(
						session2.createCriteria(FLRFinanzBuchungDetail.class));
				addCriteriasForDetails(crit2, kbModel, sortOrder, konto, true);
				addOrderForDetails(crit2, kbModel, sortOrder, konto, true);
				list.addAll(crit2.list());
			} else {
				addCriteriasForDetails(crit2, kbModel, sortOrder, konto, null);
				addOrderForDetails(crit2, kbModel, sortOrder, konto, null);
				list.addAll(crit2.list());
			}

			return list;
		}

		protected int getReportSpaltenanzahl() {
			return REPORT_KONTOBLATT_ANZAHL_SPALTEN;
		}

		private String getBelegkennung(String belegartCnr, TheClientDto theClientDto) throws RemoteException {
			BelegartDto belegartDto = getLocaleFac().belegartFindByPrimaryKey(belegartCnr, theClientDto);
			return belegartDto.getCKurzbezeichnung();
		}

		private void buildAnzahlungsSchlussrechnungInfo(Rechnung rechnung, Object[] reportData,
				TheClientDto theClientDto) throws RemoteException {
			if (RechnungFac.RECHNUNGART_ANZAHLUNG.equals(rechnung.getRechnungartCNr())
					|| RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG.equals(rechnung.getRechnungartCNr())) {
				reportData[REPORT_KONTOBLATT_AZRSR] = mapAnzahlungsCnr.get(rechnung.getRechnungartCNr());
				if (rechnung.getAuftragIId() != null) {
					Auftrag auftrag = em.find(Auftrag.class, rechnung.getAuftragIId());
					reportData[REPORT_KONTOBLATT_AZRSR_BELEG] = auftrag.getCNr();
					reportData[REPORT_KONTOBLATT_AZRSR_BELEG_ID] = auftrag.getIId();
					reportData[REPORT_KONTOBLATT_AZRSR_BELEGART_CNR] = auftrag.getBelegartCNr();
					reportData[REPORT_KONTOBLATT_AZRSR_BELEGART] = getBelegkennung(auftrag.getBelegartCNr(),
							theClientDto);
				}
			}
		}

		private void buildAnzahlungsSchlussrechnungInfo(Eingangsrechnung rechnung, Object[] reportData,
				TheClientDto theClientDto) throws RemoteException {
			if (EingangsrechnungFac.EINGANGSRECHNUNGART_ANZAHLUNG.equals(rechnung.getEingangsrechnungartCNr())
					|| EingangsrechnungFac.EINGANGSRECHNUNGART_SCHLUSSZAHLUNG
							.equals(rechnung.getEingangsrechnungartCNr())) {
				reportData[REPORT_KONTOBLATT_AZRSR] = mapAnzahlungsCnr.get(rechnung.getEingangsrechnungartCNr());
				if (rechnung.getBestellungIId() != null) {
					Bestellung bestellung = em.find(Bestellung.class, rechnung.getBestellungIId());
					reportData[REPORT_KONTOBLATT_AZRSR_BELEG] = bestellung.getCNr();
					reportData[REPORT_KONTOBLATT_AZRSR_BELEG_ID] = bestellung.getIId();
					reportData[REPORT_KONTOBLATT_AZRSR_BELEGART_CNR] = bestellung.getBelegartCNr();
					reportData[REPORT_KONTOBLATT_AZRSR_BELEGART] = getBelegkennung(bestellung.getBelegartCNr(),
							theClientDto);
				}
			}
		}

		protected Object[] buildReportBuchungDetail(BuchungParameter buchungParameter, PrintKontoblaetterModel kbModel,
				FLRFinanzKonto konto, FLRFinanzBuchungDetail buchungDetail, TheClientDto theClientDto)
				throws RemoteException {
			BigDecimal soll = null;
			BigDecimal haben = null;
			BigDecimal ust = null;
			BigDecimal sollFW = null;
			BigDecimal habenFW = null;
			BigDecimal belegkurs = null;
			BigDecimal kurszudatum = null;
			String belegwaehrung = null;

			Object[] data = new Object[getReportSpaltenanzahl()];
			data[REPORT_KONTOBLATT_INKONSISTENT] = Boolean.FALSE;

			if (buchungDetail.getFlrbuchung() != null) {
				String s = buchungParameter.getBuchungdetailText()
						.getTextFuerBuchungsart(buchungDetail.getFlrbuchung());
				data[REPORT_KONTOBLATT_BUCHUNGSART] = s;

				if (buchungParameter.isDoConsistentCheck()) {
					Date buchungsDatum = buchungDetail.getFlrbuchung().getD_buchungsdatum();
					Date tanlegen = buchungDetail.getFlrbuchung().getT_anlegen();
					if (buchungsDatum.after(buchungParameter.gettVonbis()[0])
							&& buchungsDatum.before(buchungParameter.gettVonbis()[1])
							&& tanlegen.after(konto.getD_eb_anlegen())) {
						data[REPORT_KONTOBLATT_INKONSISTENT] = Boolean.TRUE;
					}
				}
			}

			data[REPORT_KONTOBLATT_BELEG] = buchungDetail.getFlrbuchung().getC_belegnummer();
			data[REPORT_KONTOBLATT_BELEGARTCNR] = buchungDetail.getFlrbuchung().getBuchungsart_c_nr();
			data[REPORT_KONTOBLATT_TEXT] = buchungDetail.getFlrbuchung().getC_text();
			data[REPORT_KONTOBLATT_BUCHUNGSDATUM] = buchungDetail.getFlrbuchung().getD_buchungsdatum();
			data[REPORT_KONTOBLATT_AUSZUG] = buchungDetail.getI_auszug();
			if (buchungDetail.getFlrgegenkonto() != null) {
				data[REPORT_KONTOBLATT_GEGENKONTO] = buchungDetail.getFlrgegenkonto().getC_nr();
				data[REPORT_KONTOBLATT_GEGENKONTO_BEZEICHNUNG] = buchungDetail.getFlrgegenkonto().getC_bez();
			}

			if (buchungDetail.getBuchungdetailart_c_nr().equals(BuchenFac.HabenBuchung)) {
				haben = buchungDetail.getN_betrag();
			} else {
				soll = buchungDetail.getN_betrag();
			}
			ust = buchungDetail.getN_ust();
			// if (ust.compareTo(new BigDecimal(0.00)) != 0) {

			Timestamp belegDatum = null;
			BelegbuchungDto bbDto = getBelegbuchungFac(theClientDto.getMandant())
					.belegbuchungFindByBuchungIIdOhneExc(buchungDetail.getFlrbuchung().getI_id());
			if (bbDto == null) {
				// keine Belegbuchung
				if (!buchungParameter.isDruckInMandantenWaehrung() && buchungParameter.getWaehrungDruck() != null
						&& !buchungParameter.getWaehrungDruck().equals(theClientDto.getSMandantenwaehrung())) {
					// es soll in Kontowaehrung gedruckt werden und
					// Kontowaehrng != Mandantenwaehrung
					// dann wird der passende Tageskurs gesucht und
					// umgerechnet
					WechselkursDto wkDto = null;
					try {
						wkDto = getLocaleFac().getKursZuDatum(theClientDto.getSMandantenwaehrung(),
								buchungParameter.getWaehrungDruck(),
								(java.sql.Date) buchungDetail.getFlrbuchung().getD_buchungsdatum(), theClientDto);
					} catch (Throwable t) {
						//
					}
					if (wkDto != null) {
						kurszudatum = wkDto.getNKurs();
						if (haben != null)
							haben = Helper.rundeKaufmaennisch(haben.multiply(wkDto.getNKurs()),
									FinanzFac.NACHKOMMASTELLEN);
						if (soll != null)
							soll = Helper.rundeKaufmaennisch(soll.multiply(wkDto.getNKurs()),
									FinanzFac.NACHKOMMASTELLEN);
					} else {
						ArrayList<Object> allInfo = new ArrayList<Object>();
						allInfo.add(buchungParameter.getWaehrungDruck());
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_KEIN_WECHSELKURS_HINTERLEGT, allInfo,
								new Exception("Kein Wechselkurs von " + theClientDto.getSMandantenwaehrung() + " auf "
										+ buchungParameter.getWaehrungDruck()));
					}
				}
			} else {
				// Buchung ist ein Beleg
				if (bbDto.getBelegartCNr().equals(LocaleFac.BELEGART_RECHNUNG)
						|| bbDto.getBelegartCNr().equals(LocaleFac.BELEGART_GUTSCHRIFT)) {
					Rechnung rechnung = em.find(Rechnung.class, bbDto.getIBelegiid());
					if (rechnung != null) {
						belegkurs = rechnung.getNKurs();
						belegwaehrung = rechnung.getWaehrungCNr();
						belegDatum = rechnung.getTBelegdatum();

						buildAnzahlungsSchlussrechnungInfo(rechnung, data, theClientDto);
					}
				} else if (bbDto.getBelegartCNr().equals(LocaleFac.BELEGART_REZAHLUNG)) {
					Rechnungzahlung zahlung = em.find(Rechnungzahlung.class, bbDto.getIBelegiid());
					if (zahlung != null) {
						belegkurs = zahlung.getNKurs();
						Rechnung rechnung = em.find(Rechnung.class, zahlung.getRechnungIId());
						if (rechnung != null) {
							belegwaehrung = rechnung.getWaehrungCNr();
							buildAnzahlungsSchlussrechnungInfo(rechnung, data, theClientDto);
						}
						belegDatum = rechnung.getTBelegdatum();
					}
				} else if (bbDto.getBelegartCNr().equals(LocaleFac.BELEGART_EINGANGSRECHNUNG)) {
					Eingangsrechnung rechnung = em.find(Eingangsrechnung.class, bbDto.getIBelegiid());
					if (rechnung != null) {
						belegkurs = Helper.rundeKaufmaennisch(Helper.getKehrwert(rechnung.getNKurs()),
								rechnung.getNKurs().scale());
						belegwaehrung = rechnung.getWaehrungCNr();
						belegDatum = new Timestamp(rechnung.getTBelegdatum().getTime());
						buildAnzahlungsSchlussrechnungInfo(rechnung, data, theClientDto);
					}
				} else if (bbDto.getBelegartCNr().equals(LocaleFac.BELEGART_ERZAHLUNG)) {
					Eingangsrechnungzahlung zahlung = em.find(Eingangsrechnungzahlung.class, bbDto.getIBelegiid());
					if (zahlung != null) {
						belegkurs = zahlung.getNKurs();
						Eingangsrechnung rechnung = em.find(Eingangsrechnung.class, zahlung.getEingangsrechnungIId());
						if (rechnung != null) {
							belegwaehrung = rechnung.getWaehrungCNr();
							buildAnzahlungsSchlussrechnungInfo(rechnung, data, theClientDto);
						}
						belegDatum = new Timestamp(rechnung.getTBelegdatum().getTime());
					}
				}
				if (belegkurs == null) {
					myLogger.warn("Kurs null fuer Belegart '" + bbDto.getBelegartCNr() + "'.");
				}

				if (!buchungParameter.isDruckInMandantenWaehrung() && buchungParameter.getWaehrungDruck() != null
						&& !buchungParameter.getWaehrungDruck().equals(theClientDto.getSMandantenwaehrung())) {
					// es soll in Kontowaehrung gedruckt werden und
					// Kontowaehrung != Mandantenwaehrung
					// fremwaehrungen werden nicht gesetzt lt. Werner!
					if (belegwaehrung.equals(buchungParameter.getWaehrungDruck())) {
						// es soll in der Belegwaehrung gedruckt werden ->
						// kurs ist belegkurs
						if (soll != null)
							soll = Helper.rundeKaufmaennisch(soll.multiply(belegkurs), FinanzFac.NACHKOMMASTELLEN);
						if (haben != null)
							haben = Helper.rundeKaufmaennisch(haben.multiply(belegkurs), FinanzFac.NACHKOMMASTELLEN);
						if (ust != null)
							ust = Helper.rundeKaufmaennisch(ust.multiply(belegkurs), FinanzFac.NACHKOMMASTELLEN);
					} else {
						/*
						 * fremdw&auml;hrung setzen und umrechnen aus der Mandantenw&auml;hrung
						 * data[REPORT_KONTOBLATT_BELEGKURS] = belegkurs;
						 * data[REPORT_KONTOBLATT_BELEGWAEHRUNG] = belegwaehrung;
						 */
						WechselkursDto wkDto = null;
						try {
							wkDto = getLocaleFac().getKursZuDatum(theClientDto.getSMandantenwaehrung(),
									buchungParameter.getWaehrungDruck(),
									(java.sql.Date) buchungDetail.getFlrbuchung().getD_buchungsdatum(), theClientDto);
						} catch (Throwable t) {
							//
						}
						if (wkDto != null) {
							kurszudatum = wkDto.getNKurs();
							if (haben != null) {
								// habenFW =
								// Helper.rundeKaufmaennisch(haben.multiply(
								// belegkurs),
								// FinanzFac.NACHKOMMASTELLEN);
								haben = Helper.rundeKaufmaennisch(haben.multiply(wkDto.getNKurs()),
										FinanzFac.NACHKOMMASTELLEN);
							}
							if (soll != null) {
								// sollFW =
								// Helper.rundeKaufmaennisch(soll.multiply(
								// belegkurs),
								// FinanzFac.NACHKOMMASTELLEN);
								soll = Helper.rundeKaufmaennisch(soll.multiply(wkDto.getNKurs()),
										FinanzFac.NACHKOMMASTELLEN);
							}
							if (ust != null)
								ust = Helper.rundeKaufmaennisch(ust.multiply(wkDto.getNKurs()),
										FinanzFac.NACHKOMMASTELLEN);
						} else {
							ArrayList<Object> allInfo = new ArrayList<Object>();
							allInfo.add(buchungParameter.getWaehrungDruck());
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_KEIN_WECHSELKURS_HINTERLEGT, allInfo,
									new Exception("Kein Wechselkurs von " + theClientDto.getSMandantenwaehrung()
											+ " auf " + buchungParameter.getWaehrungDruck()));
						}
					}
				} else {
					if (belegkurs != null && belegkurs.compareTo(new BigDecimal(1)) != 0) {
						data[REPORT_KONTOBLATT_BELEGKURS] = belegkurs;
						data[REPORT_KONTOBLATT_BELEGWAEHRUNG] = belegwaehrung;
						if (haben != null)
							habenFW = Helper.rundeKaufmaennisch(haben.multiply(belegkurs), FinanzFac.NACHKOMMASTELLEN);
						if (soll != null)
							sollFW = Helper.rundeKaufmaennisch(soll.multiply(belegkurs), FinanzFac.NACHKOMMASTELLEN);
						WechselkursDto wkDto = null;
						try {
							wkDto = getLocaleFac().getKursZuDatum(theClientDto.getSMandantenwaehrung(),
									(buchungParameter.getWaehrungDruck() == null ? belegwaehrung
											: buchungParameter.getWaehrungDruck()),
									(java.sql.Date) buchungDetail.getFlrbuchung().getD_buchungsdatum(), theClientDto);
						} catch (Throwable t) {
							//
						}

						if (wkDto != null) {
							kurszudatum = wkDto.getNKurs();
						} else {
							ArrayList<Object> allInfo = new ArrayList<Object>();
							allInfo.add(buchungParameter.getWaehrungDruck());
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_KEIN_WECHSELKURS_HINTERLEGT, allInfo,
									new Exception("Kein Wechselkurs von " + theClientDto.getSMandantenwaehrung()
											+ " auf " + buchungParameter.getWaehrungDruck()));
						}
					}
				}
			}
			if (ust.signum() != 0) {
				BigDecimal ustprozent = null;
				try {
					Timestamp t = belegDatum != null ? belegDatum
							: new Timestamp(buchungDetail.getFlrbuchung().getD_buchungsdatum().getTime());
					if (buchungParameter.isIstSachkonto() && !buchungParameter.isIstBankOderKasse()) {
						// nur Sachkonten die keine Bank oder Kasse sind
						// buchen netto
						// netto = buchungDetail.getN_betrag().abs();
						// ustprozent =
						// Helper.getProzentsatzBD(buchungDetail.getN_betrag().abs(),
						// buchungDetail.getN_ust().abs(),
						// FinanzFac.NACHKOMMASTELLEN);
						MwstsatzDto mwstSatzDto = getMandantFac().getMwstSatzVonNettoBetragUndUst(
								theClientDto.getMandant(), t, buchungDetail.getN_betrag().abs(),
								buchungDetail.getN_ust().abs());
						ustprozent = new BigDecimal(mwstSatzDto.getFMwstsatz());
					} else {
						// netto = buchungDetail.getN_betrag().abs()
						// .subtract(buchungDetail.getN_ust().abs());
						//
						MwstsatzDto mwstSatzDto = getMandantFac().getMwstSatzVonBruttoBetragUndUst(
								theClientDto.getMandant(), t, buchungDetail.getN_betrag().abs(),
								buchungDetail.getN_ust().abs());
						ustprozent = new BigDecimal(mwstSatzDto.getFMwstsatz());
					}
				} catch (Exception e) {
					//
				}
				data[REPORT_KONTOBLATT_USTPROZENT] = ustprozent;
			} else {
				data[REPORT_KONTOBLATT_USTPROZENT] = null;
			}

			data[REPORT_KONTOBLATT_HABEN] = haben;
			data[REPORT_KONTOBLATT_SOLL] = soll;
			data[REPORT_KONTOBLATT_HABEN_FW] = habenFW;
			data[REPORT_KONTOBLATT_SOLL_FW] = sollFW;
			data[REPORT_KONTOBLATT_KURSZUDATUM] = kurszudatum;
			data[REPORT_KONTOBLATT_USTWERT] = ust;
			data[REPORT_KONTOBLATT_AZK] = buchungDetail.getI_ausziffern();

			data[REPORT_KONTOBLATT_AUTOMATISCHE_EB] = buchungDetail.getFlrbuchung().getB_autombuchungeb();

			return data;
		}

		protected Object[][] getReportBuchungen(int sizeBuchungen, Iterator<?> buchungen,
				PrintKontoblaetterModel kbModel, FLRFinanzKonto konto, TheClientDto theClientDto)
				throws EJBExceptionLP, RemoteException {

			Object[][] data = new Object[sizeBuchungen][getReportSpaltenanzahl()];
			BuchungParameter buchungParameter = new BuchungParameter(kbModel, konto, theClientDto);

			int i = 0;
			while (buchungen.hasNext()) {
				FLRFinanzBuchungDetail buchungDetail = (FLRFinanzBuchungDetail) buchungen.next();
				data[i] = buildReportBuchungDetail(buchungParameter, kbModel, konto, buchungDetail, theClientDto);
				i++;
			}

			if (kbModel.isSortBeleg()) {
				boolean hasAzrSr = false;
				for (Object[] objects : data) {
					if (objects[REPORT_KONTOBLATT_AZRSR_BELEG] != null) {
						hasAzrSr = true;
						break;
					}
				}

				/*
				 * Collections.sort(al, new Comparator<ArtikelkommentarDto>() { public int
				 * compare(ArtikelkommentarDto o1, ArtikelkommentarDto o2) { return
				 * o1.getISort().compareTo(o2.getISort()); }; });
				 */
				if (hasAzrSr) {
					List<Object[]> lines = Arrays.asList(data);
					Collections.sort(lines, new Comparator<Object[]>() {
						@Override
						public int compare(Object[] o1, Object[] o2) {
							String b1 = (String) o1[REPORT_KONTOBLATT_AZRSR_BELEG];
							String b2 = (String) o2[REPORT_KONTOBLATT_AZRSR_BELEG];

							if (b1 == null && b2 == null) {
								return ((String) o1[REPORT_KONTOBLATT_BELEG])
										.compareTo((String) o2[REPORT_KONTOBLATT_BELEG]);
							}
							if (b1 == null) {
								return 1;
							}
							if (b2 == null) {
								return -1;
							}
							int diff = b1.compareTo(b2);
							if (diff == 0) {
								diff = ((String) o1[REPORT_KONTOBLATT_BELEG])
										.compareTo((String) o2[REPORT_KONTOBLATT_BELEG]);
							}
							if (diff == 0) {
								diff = ((Date) o1[REPORT_KONTOBLATT_BUCHUNGSDATUM])
										.compareTo((Date) o2[REPORT_KONTOBLATT_BUCHUNGSDATUM]);
							}
							return diff;
						}
					});
					Object[][] reportData = new Object[data.length][REPORT_KONTOBLATT_ANZAHL_SPALTEN];
					data = lines.toArray(reportData);
				}
			}

			return data;
		}

		// boolean druckInMandantenWaehrung = kbModel
		// .isDruckInMandantenWaehrung();
		// String waehrungDruck = konto.getWaehrung_c_nr_druck();
		// boolean istSachkonto = konto.getKontotyp_c_nr().equals(
		// FinanzServiceFac.KONTOTYP_SACHKONTO);
		// boolean istBankoderKasse = getFinanzFac().isKontoMitSaldo(
		// konto.getI_id(), theClientDto);
		//
		// boolean doConsistentCheck = true;
		// if (konto.getI_eb_geschaeftsjahr() == null) {
		// doConsistentCheck = false;
		// }
		// if (doConsistentCheck
		// && !konto.getI_eb_geschaeftsjahr().equals(
		// kbModel.getGeschaeftsjahr())) {
		// doConsistentCheck = false;
		// }
		//
		// Timestamp[] tVonbis = getBuchenFac().getDatumbereichPeriodeGJ(
		// kbModel.getGeschaeftsjahr() - 1, -1, theClientDto);
		//
		// BuchungdetailText buchungdetailText = new BuchungdetailText(
		// getBenutzerServicesFac(), theClientDto);

		// while (buchungen.hasNext()) {
		// BigDecimal soll = null;
		// BigDecimal haben = null;
		// BigDecimal ust = null;
		// BigDecimal sollFW = null;
		// BigDecimal habenFW = null;
		// BigDecimal belegkurs = null;
		// BigDecimal kurszudatum = null;
		// String belegwaehrung = null;
		//
		// FLRFinanzBuchungDetail buchungDetail = (FLRFinanzBuchungDetail)
		// buchungen
		// .next();
		//
		// data[i][REPORT_KONTOBLATT_INKONSISTENT] = Boolean.FALSE;
		//
		// if (buchungDetail.getFlrbuchung() != null) {
		// String s = buchungdetailText
		// .getTextFuerBuchungsart(buchungDetail
		// .getFlrbuchung());
		// data[i][REPORT_KONTOBLATT_BUCHUNGSART] = s;
		//
		// if (doConsistentCheck) {
		// Date buchungsDatum = buchungDetail.getFlrbuchung()
		// .getD_buchungsdatum();
		// Date tanlegen = buchungDetail.getFlrbuchung()
		// .getT_anlegen();
		// if (buchungsDatum.after(tVonbis[0])
		// && buchungsDatum.before(tVonbis[1])
		// && tanlegen.after(konto.getD_eb_anlegen())) {
		// data[i][REPORT_KONTOBLATT_INKONSISTENT] = Boolean.TRUE;
		// }
		// }
		// }
		//
		// data[i][REPORT_KONTOBLATT_BELEG] = buchungDetail
		// .getFlrbuchung().getC_belegnummer();
		// data[i][REPORT_KONTOBLATT_BELEGARTCNR] = buchungDetail
		// .getFlrbuchung().getBuchungsart_c_nr();
		// data[i][REPORT_KONTOBLATT_TEXT] = buchungDetail.getFlrbuchung()
		// .getC_text();
		// data[i][REPORT_KONTOBLATT_BUCHUNGSDATUM] = buchungDetail
		// .getFlrbuchung().getD_buchungsdatum();
		// data[i][REPORT_KONTOBLATT_AUSZUG] = buchungDetail.getI_auszug();
		// if (buchungDetail.getFlrgegenkonto() != null) {
		// data[i][REPORT_KONTOBLATT_GEGENKONTO] = buchungDetail
		// .getFlrgegenkonto().getC_nr();
		// }
		//
		// if (buchungDetail.getBuchungdetailart_c_nr().equals(
		// BuchenFac.HabenBuchung)) {
		// haben = buchungDetail.getN_betrag();
		// } else {
		// soll = buchungDetail.getN_betrag();
		// }
		// ust = buchungDetail.getN_ust();
		// // if (ust.compareTo(new BigDecimal(0.00)) != 0) {
		// if (ust.signum() != 0) {
		// BigDecimal ustprozent = null;
		// try {
		// Timestamp tsBuchungsDatum = new Timestamp(buchungDetail
		// .getFlrbuchung().getD_buchungsdatum().getTime());
		// if (istSachkonto && !istBankoderKasse) {
		// // nur Sachkonten die keine Bank oder Kasse sind
		// // buchen netto
		// // netto = buchungDetail.getN_betrag().abs();
		// // ustprozent =
		// // Helper.getProzentsatzBD(buchungDetail.getN_betrag().abs(),
		// // buchungDetail.getN_ust().abs(),
		// // FinanzFac.NACHKOMMASTELLEN);
		// MwstsatzDto mwstSatzDto = getMandantFac()
		// .getMwstSatzVonNettoBetragUndUst(
		// theClientDto.getMandant(),
		// tsBuchungsDatum,
		// buchungDetail.getN_betrag().abs(),
		// buchungDetail.getN_ust().abs());
		// ustprozent = new BigDecimal(
		// mwstSatzDto.getFMwstsatz());
		// } else {
		// // netto = buchungDetail.getN_betrag().abs()
		// // .subtract(buchungDetail.getN_ust().abs());
		// //
		// MwstsatzDto mwstSatzDto = getMandantFac()
		// .getMwstSatzVonBruttoBetragUndUst(
		// theClientDto.getMandant(),
		// tsBuchungsDatum,
		// buchungDetail.getN_betrag().abs(),
		// buchungDetail.getN_ust().abs());
		// ustprozent = new BigDecimal(
		// mwstSatzDto.getFMwstsatz());
		// }
		// } catch (Exception e) {
		// //
		// }
		// data[i][REPORT_KONTOBLATT_USTPROZENT] = ustprozent;
		// } else {
		// data[i][REPORT_KONTOBLATT_USTPROZENT] = null;
		// }
		//
		// BelegbuchungDto bbDto = getBelegbuchungFac(
		// theClientDto.getMandant())
		// .belegbuchungFindByBuchungIIdOhneExc(
		// buchungDetail.getFlrbuchung().getI_id());
		// if (bbDto == null) {
		// // keine Belegbuchung
		// if (!druckInMandantenWaehrung
		// && waehrungDruck != null
		// && !waehrungDruck.equals(theClientDto
		// .getSMandantenwaehrung())) {
		// // es soll in Kontowaehrung gedruckt werden und
		// // Kontowaehrng != Mandantenwaehrung
		// // dann wird der passende Tageskurs gesucht und
		// // umgerechnet
		// WechselkursDto wkDto = null;
		// try {
		// wkDto = getLocaleFac()
		// .getKursZuDatum(
		// theClientDto
		// .getSMandantenwaehrung(),
		// waehrungDruck,
		// (java.sql.Date) buchungDetail
		// .getFlrbuchung()
		// .getD_buchungsdatum(),
		// theClientDto);
		// } catch (Throwable t) {
		// //
		// }
		// if (wkDto != null) {
		// kurszudatum = wkDto.getNKurs();
		// if (haben != null)
		// haben = Helper.rundeKaufmaennisch(
		// haben.multiply(wkDto.getNKurs()),
		// FinanzFac.NACHKOMMASTELLEN);
		// if (soll != null)
		// soll = Helper.rundeKaufmaennisch(
		// soll.multiply(wkDto.getNKurs()),
		// FinanzFac.NACHKOMMASTELLEN);
		// } else {
		// ArrayList<Object> allInfo = new ArrayList<Object>();
		// allInfo.add(waehrungDruck);
		// throw new EJBExceptionLP(
		// EJBExceptionLP.FEHLER_KEIN_WECHSELKURS_HINTERLEGT,
		// allInfo,
		// new Exception("Kein Wechselkurs von "
		// + theClientDto
		// .getSMandantenwaehrung()
		// + " auf " + waehrungDruck));
		// }
		// }
		// } else {
		// // Buchung ist ein Beleg
		// java.sql.Date belegdatum = null;
		// if (bbDto.getBelegartCNr().equals(
		// LocaleFac.BELEGART_RECHNUNG)
		// || bbDto.getBelegartCNr().equals(
		// LocaleFac.BELEGART_GUTSCHRIFT)) {
		// Rechnung rechnung = em.find(Rechnung.class,
		// bbDto.getIBelegiid());
		// if (rechnung != null) {
		// belegkurs = rechnung.getNKurs();
		// belegwaehrung = rechnung.getWaehrungCNr();
		// belegdatum = new java.sql.Date(rechnung
		// .getTBelegdatum().getTime());
		// }
		// } else if (bbDto.getBelegartCNr().equals(
		// LocaleFac.BELEGART_REZAHLUNG)) {
		// Rechnungzahlung zahlung = em.find(
		// Rechnungzahlung.class, bbDto.getIBelegiid());
		// if (zahlung != null) {
		// belegkurs = zahlung.getNKurs();
		// Rechnung rechnung = em.find(Rechnung.class,
		// zahlung.getRechnungIId());
		// if (rechnung != null)
		// belegwaehrung = rechnung.getWaehrungCNr();
		// belegdatum = zahlung.getTZahldatum();
		// }
		// } else if (bbDto.getBelegartCNr().equals(
		// LocaleFac.BELEGART_EINGANGSRECHNUNG)) {
		// Eingangsrechnung rechnung = em.find(
		// Eingangsrechnung.class, bbDto.getIBelegiid());
		// if (rechnung != null) {
		// belegkurs = Helper.rundeKaufmaennisch(
		// Helper.getKehrwert(rechnung.getNKurs()),
		// rechnung.getNKurs().scale());
		// belegwaehrung = rechnung.getWaehrungCNr();
		// belegdatum = new java.sql.Date(rechnung
		// .getTBelegdatum().getTime());
		// }
		// } else if (bbDto.getBelegartCNr().equals(
		// LocaleFac.BELEGART_ERZAHLUNG)) {
		// Eingangsrechnungzahlung zahlung = em.find(
		// Eingangsrechnungzahlung.class,
		// bbDto.getIBelegiid());
		// if (zahlung != null) {
		// belegkurs = zahlung.getNKurs();
		// Eingangsrechnung rechnung = em.find(
		// Eingangsrechnung.class,
		// zahlung.getEingangsrechnungIId());
		// if (rechnung != null)
		// belegwaehrung = rechnung.getWaehrungCNr();
		// belegdatum = zahlung.getTZahldatum();
		// }
		// }
		// if (belegkurs == null) {
		// myLogger.warn("Kurs null fuer Belegart '"
		// + bbDto.getBelegartCNr() + "'.");
		// }
		//
		// if (!druckInMandantenWaehrung
		// && waehrungDruck != null
		// && !waehrungDruck.equals(theClientDto
		// .getSMandantenwaehrung())) {
		// // es soll in Kontowaehrung gedruckt werden und
		// // Kontowaehrung != Mandantenwaehrung
		// // fremwaehrungen werden nicht gesetzt lt. Werner!
		// if (belegwaehrung.equals(waehrungDruck)) {
		// // es soll in der Belegwaehrung gedruckt werden ->
		// // kurs ist belegkurs
		// if (soll != null)
		// soll = Helper.rundeKaufmaennisch(
		// soll.multiply(belegkurs),
		// FinanzFac.NACHKOMMASTELLEN);
		// if (haben != null)
		// haben = Helper.rundeKaufmaennisch(
		// haben.multiply(belegkurs),
		// FinanzFac.NACHKOMMASTELLEN);
		// if (ust != null)
		// ust = Helper.rundeKaufmaennisch(
		// ust.multiply(belegkurs),
		// FinanzFac.NACHKOMMASTELLEN);
		// } else {
		// /*
		// * fremdw&auml;hrung setzen und umrechnen aus der
		// * Mandantenw&auml;hrung
		// * data[i][REPORT_KONTOBLATT_BELEGKURS] = belegkurs;
		// * data[i][REPORT_KONTOBLATT_BELEGWAEHRUNG] =
		// * belegwaehrung;
		// */
		// WechselkursDto wkDto = null;
		// try {
		// wkDto = getLocaleFac().getKursZuDatum(
		// theClientDto.getSMandantenwaehrung(),
		// waehrungDruck,
		// (java.sql.Date) buchungDetail
		// .getFlrbuchung()
		// .getD_buchungsdatum(),
		// theClientDto);
		// } catch (Throwable t) {
		// //
		// }
		// if (wkDto != null) {
		// kurszudatum = wkDto.getNKurs();
		// if (haben != null) {
		// // habenFW =
		// // Helper.rundeKaufmaennisch(haben.multiply(
		// // belegkurs),
		// // FinanzFac.NACHKOMMASTELLEN);
		// haben = Helper.rundeKaufmaennisch(
		// haben.multiply(wkDto.getNKurs()),
		// FinanzFac.NACHKOMMASTELLEN);
		// }
		// if (soll != null) {
		// // sollFW =
		// // Helper.rundeKaufmaennisch(soll.multiply(
		// // belegkurs),
		// // FinanzFac.NACHKOMMASTELLEN);
		// soll = Helper.rundeKaufmaennisch(
		// soll.multiply(wkDto.getNKurs()),
		// FinanzFac.NACHKOMMASTELLEN);
		// }
		// if (ust != null)
		// ust = Helper.rundeKaufmaennisch(
		// ust.multiply(wkDto.getNKurs()),
		// FinanzFac.NACHKOMMASTELLEN);
		// } else {
		// ArrayList<Object> allInfo = new ArrayList<Object>();
		// allInfo.add(waehrungDruck);
		// throw new EJBExceptionLP(
		// EJBExceptionLP.FEHLER_KEIN_WECHSELKURS_HINTERLEGT,
		// allInfo,
		// new Exception(
		// "Kein Wechselkurs von "
		// + theClientDto
		// .getSMandantenwaehrung()
		// + " auf "
		// + waehrungDruck));
		// }
		// }
		// } else {
		// if (belegkurs != null
		// && belegkurs.compareTo(new BigDecimal(1)) != 0) {
		// data[i][REPORT_KONTOBLATT_BELEGKURS] = belegkurs;
		// data[i][REPORT_KONTOBLATT_BELEGWAEHRUNG] = belegwaehrung;
		// if (haben != null)
		// habenFW = Helper.rundeKaufmaennisch(
		// haben.multiply(belegkurs),
		// FinanzFac.NACHKOMMASTELLEN);
		// if (soll != null)
		// sollFW = Helper.rundeKaufmaennisch(
		// soll.multiply(belegkurs),
		// FinanzFac.NACHKOMMASTELLEN);
		// WechselkursDto wkDto = null;
		// try {
		// wkDto = getLocaleFac().getKursZuDatum(
		// theClientDto.getSMandantenwaehrung(),
		// (waehrungDruck == null ? belegwaehrung
		// : waehrungDruck),
		// (java.sql.Date) buchungDetail
		// .getFlrbuchung()
		// .getD_buchungsdatum(),
		// theClientDto);
		// } catch (Throwable t) {
		// //
		// }
		//
		// if (wkDto != null) {
		// kurszudatum = wkDto.getNKurs();
		// } else {
		// ArrayList<Object> allInfo = new ArrayList<Object>();
		// allInfo.add(waehrungDruck);
		// throw new EJBExceptionLP(
		// EJBExceptionLP.FEHLER_KEIN_WECHSELKURS_HINTERLEGT,
		// allInfo,
		// new Exception(
		// "Kein Wechselkurs von "
		// + theClientDto
		// .getSMandantenwaehrung()
		// + " auf "
		// + waehrungDruck));
		// }
		// }
		// }
		// }
		// data[i][REPORT_KONTOBLATT_HABEN] = haben;
		// data[i][REPORT_KONTOBLATT_SOLL] = soll;
		// data[i][REPORT_KONTOBLATT_HABEN_FW] = habenFW;
		// data[i][REPORT_KONTOBLATT_SOLL_FW] = sollFW;
		// data[i][REPORT_KONTOBLATT_KURSZUDATUM] = kurszudatum;
		// data[i][REPORT_KONTOBLATT_USTWERT] = ust;
		// data[i][REPORT_KONTOBLATT_AZK] = buchungDetail
		// .getI_ausziffern();
		//
		// data[i][REPORT_KONTOBLATT_AUTOMATISCHE_EB] = buchungDetail
		// .getFlrbuchung().getB_autombuchungeb();
		// i++;
		// }
		//
		// return data;
		// }

		protected void addCriteriasForKontos(Criteria crit, PrintKontoblaetterModel kbModel) {
			crit.addOrder(Order.asc("c_nr"));

			// if (PrintKontoblaetterModel.EnumSortOrder.SORT_NACH_BELEG ==
			// kbModel
			// .getSortOrder()) {
			// crit.addOrder(Order.asc("c_nr"));
			// }

			if (kbModel.getKontoIId() != null) {
				crit.add(Restrictions.eq("i_id", kbModel.getKontoIId()));
			} else {
				crit.add(Restrictions.eq("kontotyp_c_nr", kbModel.getKontotypCNr()));

				if (kbModel.getVonKontoNr() != null) {
					crit.add(Restrictions.ge("c_nr", kbModel.getVonKontoNr()));
				}
				if (kbModel.getBisKontoNr() != null) {
					String ktoNrBis_Gefuellt = Helper.fitString2Length(kbModel.getBisKontoNr(), 25, '_');
					crit.add(Restrictions.lt("c_nr", ktoNrBis_Gefuellt));
				}
			}
		}

		protected void addCriteriasForDetails(Criteria crit2, PrintKontoblaetterModel kbModel, EnumSortOrder sortOrder,
				FLRFinanzKonto detailKonto, Boolean whereAzkIsNull) {
			crit2.add(Restrictions.eq("konto_i_id", detailKonto.getI_id()));

			if (kbModel.gettVon() != null) {
				crit2.add(Restrictions.ge("s.d_buchungsdatum", Helper.cutTimestamp(kbModel.gettVon())));
			}

			if (kbModel.gettBis() != null) {
				crit2.add(Restrictions.lt("s.d_buchungsdatum", Helper.cutTimestamp(kbModel.gettBis())));
			}

			if (kbModel.getGeschaeftsjahr() != null) {
				crit2.add(Restrictions.eq("s.geschaeftsjahr_i_geschaeftsjahr", kbModel.getGeschaeftsjahr()));
			}

			crit2.createAlias("flrbuchung", "s");

			crit2.add(Restrictions.isNull("s.t_storniert"));

			if (whereAzkIsNull != null) {
				if (whereAzkIsNull)
					crit2.add(Restrictions.isNull("i_ausziffern"));
				else
					crit2.add(Restrictions.isNotNull("i_ausziffern"));
			}

			// if (kbModel.isSortiereNachAZK()) {
			// crit2.addOrder(Order.asc("i_ausziffern"));
			// }
			// if (sortOrder ==
			// PrintKontoblaetterModel.EnumSortOrder.SORT_NACH_AUSZUG) {
			// crit2.addOrder(Order.asc("i_auszug"));
			// crit2.addOrder(Order.asc("s.d_buchungsdatum"));
			// }
			//
			// if (sortOrder ==
			// PrintKontoblaetterModel.EnumSortOrder.SORT_NACH_DATUM) {
			// crit2.addOrder(Order.asc("s.d_buchungsdatum"));
			// }
			// if (sortOrder ==
			// PrintKontoblaetterModel.EnumSortOrder.SORT_NACH_BELEG) {
			// crit2.addOrder(Order.asc("s.c_belegnummer"));
			// }
		}

		protected void addOrderForDetails(Criteria crit2, PrintKontoblaetterModel kbModel, EnumSortOrder sortOrder,
				FLRFinanzKonto detailKonto, Boolean whereAzkIsNull) {
			if (kbModel.isSortiereNachAZK()) {
				crit2.addOrder(Order.asc("i_ausziffern"));
			}
			if (sortOrder == PrintKontoblaetterModel.EnumSortOrder.SORT_NACH_AUSZUG) {
				crit2.addOrder(Order.asc("i_auszug"));
				crit2.addOrder(Order.asc("s.d_buchungsdatum"));
			}

			if (sortOrder == PrintKontoblaetterModel.EnumSortOrder.SORT_NACH_DATUM) {
				crit2.addOrder(Order.asc("s.d_buchungsdatum"));
			}
			if (sortOrder == PrintKontoblaetterModel.EnumSortOrder.SORT_NACH_BELEG) {
				crit2.addOrder(Order.asc("s.c_belegnummer"));
			}
		}

		protected void addParametersForKontos(PrintKontoblaetterModel kbModel, TheClientDto theClientDto) {

			reportParameter.put("P_KONTOTYP", kbModel.getKontotypCNr());
			reportParameter.putIfNotNull("P_TVON",
					kbModel.gettVonReport() != null ? kbModel.gettVonReport() : kbModel.gettVon());
			reportParameter.putIfNotNull("P_TBIS",
					kbModel.gettBisReport() != null ? kbModel.gettBisReport() : kbModel.gettBis());
			reportParameter.putIfNotNull("P_KTOVON", kbModel.getVonKontoNr());
			reportParameter.putIfNotNull("P_KTOBIS", kbModel.getBisKontoNr());
			reportParameter.put("P_DRUCK_IN_MANDANTENWAEHRUNG", kbModel.isDruckInMandantenWaehrung());
			reportParameter.put("P_MANDANTENWAEHRUNG", theClientDto.getSMandantenwaehrung());
		}

		protected void addParametersForDetails(PrintKontoblaetterModel kbModel, EnumSortOrder sortOrder,
				FLRFinanzKonto detailKonto, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
			reportParameter.put("P_SORTIERUNG", kbModel.getSorderOrderParameterString(sortOrder));
			reportParameter.put("P_KONTOTYP", detailKonto.getKontotyp_c_nr());
			reportParameter.put("P_KONTONUMMER", detailKonto.getC_nr());
			reportParameter.put("P_KONTOBEZEICHNUNG", detailKonto.getC_bez());
			reportParameter.put("P_GEGENKONTOBEZEICHNUNG_ANDRUCKEN", kbModel.isGegenkontogezeichnungAndrucken());
			
			reportParameter.put("P_ISTBANKKONTO", isBankKonto(detailKonto.getI_id()));
			BigDecimal d = calculateSaldoVonKontoFuerVorPeriode(detailKonto.getI_id(), kbModel, vonPeriode,
					detailKonto.getWaehrung_c_nr_druck(), theClientDto);
			reportParameter.put("P_SALDOVORPERIODE", d);
			reportParameter.put("P_PERIODE", vonPeriode);
			if (kbModel.isDruckInMandantenWaehrung() || detailKonto.getWaehrung_c_nr_druck() == null)
				reportParameter.put(LPReport.P_WAEHRUNG, theClientDto.getSMandantenwaehrung());
			else
				reportParameter.put(LPReport.P_WAEHRUNG, detailKonto.getWaehrung_c_nr_druck());
			reportParameter.put("P_EB_GESCHAEFTSJAHR", detailKonto.getI_eb_geschaeftsjahr());
			reportParameter.put("P_EB_ANGELEGT", detailKonto.getD_eb_anlegen());

			reportParameter.put("P_DRUCK_SALDOVORPERIODE", new Boolean(kbModel.isEnableSaldo()));
			reportParameter.put("P_SORTIERE_NACH_AZK", new Boolean(kbModel.isSortiereNachAZK()));

			// PJ21787
			if (detailKonto.getFlruvaart() != null) {
				reportParameter.put("P_UVAART", detailKonto.getFlruvaart().getC_nr());
			}
			if (detailKonto.getFlrmwstsatz() != null) {
				reportParameter.put("P_UVAVARIANTE",
						detailKonto.getFlrmwstsatz().getFlrmwstsatzbez().getC_bezeichnung());
				reportParameter.put("P_UVAVARIANTE_PROZENTSATZ", detailKonto.getFlrmwstsatz().getF_mwstsatz());
			}

		}

		protected void verifyKontoIsConsistentWithEB(PrintKontoblaetterModel kbModel, FLRFinanzKonto detailKonto,
				Iterator<?> buchungen, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

			reportParameter.put("P_KONTO_KONSISTENT", new Boolean(true));

			boolean konsistent = getBuchenFac().isKontoMitEBKonsistent(detailKonto.getI_id(),
					kbModel.getGeschaeftsjahr(), theClientDto);
			reportParameter.put("P_KONTO_KONSISTENT", new Boolean(konsistent));
		}

		protected String getReportName() {
			return FinanzReportFac.REPORT_KONTOBLATT;
		}

		protected Boolean isBankKonto(Integer kontoIId) {
			BankverbindungDto bankDto = null;
			try {
				bankDto = getFinanzFac().bankverbindungFindByKontoIIdOhneExc(kontoIId);
			} catch (Throwable t) {
				//
			}
			return (bankDto != null);
		}
	}

	public class KassenbuchPrinter extends KontoblaetterPrinter {
		// @Override
		// protected void addCriteriasForDetails(Criteria crit2,
		// PrintKontoblaetterModel kbModel, EnumSortOrder sortOrder,
		// FLRFinanzKonto detailKonto, Boolean whereAzkIsNull) {
		// crit2.addOrder(Order.asc("s.d_buchungsdatum"));
		// crit2.addOrder(Order.asc("s.c_belegnummer"));
		// super.addCriteriasForDetails(crit2, kbModel, sortOrder,
		// detailKonto, whereAzkIsNull);
		// }

		@Override
		protected void addOrderForDetails(Criteria crit2, PrintKontoblaetterModel kbModel, EnumSortOrder sortOrder,
				FLRFinanzKonto detailKonto, Boolean whereAzkIsNull) {
			crit2.addOrder(Order.asc("s.d_buchungsdatum"));
			crit2.addOrder(Order.asc("s.c_belegnummer"));
		}

		protected String getReportName() {
			return FinanzReportFac.REPORT_KASSABUCH;
		}

		@Override
		protected int getReportSpaltenanzahl() {
			return REPORT_KASSENBUCH_ANZAHL_SPALTEN;
		}

		@Override
		protected Object[] buildReportBuchungDetail(BuchungParameter buchungParameter, PrintKontoblaetterModel kbModel,
				FLRFinanzKonto konto, FLRFinanzBuchungDetail buchungDetail, TheClientDto theClientDto)
				throws RemoteException {
			Object data[] = super.buildReportBuchungDetail(buchungParameter, kbModel, konto, buchungDetail,
					theClientDto);
			KassenbuchungsteuerartDto kbstDto = getBuchenFac()
					.getKassenbuchungSteuerart(buchungDetail.getBuchung_i_id(), theClientDto);
			if (kbstDto != null) {
				data[REPORT_KASSENBUCH_USTVST] = kbstDto.isUstBuchung();
			}
			return data;
		}
	}

	public JasperPrintLP printKontoblaetter(PrintKontoblaetterModel kbModel, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		this.useCase = UC_KONTOBLATT;

		KontoblaetterPrinter printer = new KontoblaetterPrinter();
		return printer.print(kbModel, theClientDto);
	}

	public JasperPrintLP printKassabuch(PrintKontoblaetterModel kbModel, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		this.useCase = UC_KONTOBLATT_KASSENBUCH;

		KontoblaetterPrinter printer = new KassenbuchPrinter();
		return printer.print(kbModel, theClientDto);
	}

	public JasperPrintLP printKassenjournal(Integer kassabuchIId, Integer iGeschaeftsjahr, TheClientDto theClientDto) {
		this.useCase = UC_KASSENJOUNRAL;

		ArrayList alDaten = new ArrayList();

		java.sql.Timestamp[] tVonBis = getBuchenFac().getDatumVonBisGeschaeftsjahr(iGeschaeftsjahr, theClientDto);

		Map<String, Object> mapParameter = new TreeMap<String, Object>();

		try {
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
			mapParameter.put(LPReport.P_WAEHRUNG, mandantDto.getWaehrungCNr());
			mapParameter.put("P_GESCHAEFTSJAHR", iGeschaeftsjahr);

			KassenbuchDto kbDto = getFinanzFac().kassenbuchFindByPrimaryKey(kassabuchIId, theClientDto);

			mapParameter.put("P_KASSENBUCH", kbDto.getCBez());

			KontoDto kontoDto = getFinanzFac().kontoFindByPrimaryKey(kbDto.getKontoIId());

			mapParameter.put("P_KONTONUMMER", kontoDto.getCNr());
			mapParameter.put("P_KONTONAME", kontoDto.getCBez());

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		// ER/ZK-Zahlungen
		Session session = FLRSessionFactory.getFactory().openSession();
		Criteria crit = session.createCriteria(FLREingangsrechnungzahlung.class);
		crit.createAlias("flreingangsrechnung", "r");
		crit.createAlias("flrkassenbuch", "k");
		crit.add(Restrictions.eq("r.mandant_c_nr", theClientDto.getMandant()));
		crit.add(Restrictions.eq("k.i_id", kassabuchIId));
		crit.add(Restrictions.between("t_zahldatum", tVonBis[0], tVonBis[1]));

		List<FLREingangsrechnungzahlung> list = crit.list();

		Iterator<FLREingangsrechnungzahlung> it = list.iterator();
		while (it.hasNext()) {

			FLREingangsrechnungzahlung erz = it.next();

			Object[] oZeile = new Object[REPORT_KASSENJOURNAL_ANZAHL_SPALTEN];
			oZeile[REPORT_KASSENJOURNAL_ZAHLDATUM] = erz.getT_zahldatum();

			if (erz.getFlreingangsrechnung().getEingangsrechnungart_c_nr()
					.equals(EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN)) {
				oZeile[REPORT_KASSENJOURNAL_BELEGART] = LocaleFac.BELEGART_ZUSATZKOSTEN;

			} else {
				oZeile[REPORT_KASSENJOURNAL_BELEGART] = LocaleFac.BELEGART_EINGANGSRECHNUNG;
			}

			oZeile[REPORT_KASSENJOURNAL_KOMMENTAR] = erz.getC_kommentar();

			oZeile[REPORT_KASSENJOURNAL_BELEGNUMMER] = erz.getFlreingangsrechnung().getC_nr();
			oZeile[REPORT_KASSENJOURNAL_TEXT] = erz.getFlreingangsrechnung().getC_text();
			oZeile[REPORT_KASSENJOURNAL_KUNDELIEFERANT] = HelperServer
					.formatNameAusFLRPartner(erz.getFlreingangsrechnung().getFlrlieferant().getFlrpartner());
			oZeile[REPORT_KASSENJOURNAL_BETRAG_AUS] = erz.getN_betrag();
			oZeile[REPORT_KASSENJOURNAL_USTBETRAG_AUS] = erz.getN_betrag_ust();

			alDaten.add(oZeile);
		}

		session.close();
		session = FLRSessionFactory.getFactory().openSession();
		crit = session.createCriteria(FLRRechnungZahlung.class);
		crit.createAlias("flrrechnung", "r");
		crit.createAlias("flrkassenbuch", "k");
		crit.add(Restrictions.eq("r.mandant_c_nr", theClientDto.getMandant()));
		crit.add(Restrictions.eq("k.i_id", kassabuchIId));
		crit.add(Restrictions.between("d_zahldatum", tVonBis[0], tVonBis[1]));

		List<FLRRechnungZahlung> listRZ = crit.list();

		Iterator<FLRRechnungZahlung> itRZ = listRZ.iterator();
		while (itRZ.hasNext()) {

			FLRRechnungZahlung erz = itRZ.next();

			Object[] oZeile = new Object[REPORT_KASSENJOURNAL_ANZAHL_SPALTEN];

			oZeile[REPORT_KASSENJOURNAL_ZAHLDATUM] = erz.getD_zahldatum();

			if (erz.getFlrrechnung().getFlrrechnungart().getC_nr().equals(RechnungFac.RECHNUNGART_GUTSCHRIFT)) {
				oZeile[REPORT_KASSENJOURNAL_BELEGART] = LocaleFac.BELEGART_GUTSCHRIFT;
				oZeile[REPORT_KASSENJOURNAL_BETRAG_AUS] = erz.getN_betrag();
				oZeile[REPORT_KASSENJOURNAL_USTBETRAG_AUS] = erz.getN_betrag_ust();

			} else {
				oZeile[REPORT_KASSENJOURNAL_BELEGART] = LocaleFac.BELEGART_RECHNUNG;
				oZeile[REPORT_KASSENJOURNAL_BETRAG_EIN] = erz.getN_betrag();
				oZeile[REPORT_KASSENJOURNAL_USTBETRAG_EIN] = erz.getN_betrag_ust();
			}

			oZeile[REPORT_KASSENJOURNAL_BELEGNUMMER] = erz.getFlrrechnung().getC_nr();

			oZeile[REPORT_KASSENJOURNAL_KOMMENTAR] = erz.getC_kommentar();

			oZeile[REPORT_KASSENJOURNAL_KUNDELIEFERANT] = HelperServer
					.formatNameAusFLRPartner(erz.getFlrrechnung().getFlrkunde().getFlrpartner());

			alDaten.add(oZeile);
		}

		session.close();

		// Nach Zahldatum + Beleg (zuerst RE/GS dann ER/ZK) sortieren
		for (int m = alDaten.size() - 1; m > 0; --m) {
			for (int n = 0; n < m; ++n) {
				Object[] o1 = (Object[]) alDaten.get(n);
				Object[] o2 = (Object[]) alDaten.get(n + 1);

				java.util.Date d1 = (java.util.Date) o1[REPORT_KASSENJOURNAL_ZAHLDATUM];
				java.util.Date d2 = (java.util.Date) o2[REPORT_KASSENJOURNAL_ZAHLDATUM];

				String belegart1 = (String) o1[REPORT_KASSENJOURNAL_BELEGART];

				if (belegart1.equals(LocaleFac.BELEGART_RECHNUNG)) {
					belegart1 = "1";
				} else if (belegart1.equals(LocaleFac.BELEGART_GUTSCHRIFT)) {
					belegart1 = "2";
				} else if (belegart1.equals(LocaleFac.BELEGART_EINGANGSRECHNUNG)) {
					belegart1 = "3";
				}
				if (belegart1.equals(LocaleFac.BELEGART_ZUSATZKOSTEN)) {
					belegart1 = "4";
				}

				String belegart2 = (String) o2[REPORT_KASSENJOURNAL_BELEGART];
				if (belegart2.equals(LocaleFac.BELEGART_RECHNUNG)) {
					belegart2 = "1";
				} else if (belegart2.equals(LocaleFac.BELEGART_GUTSCHRIFT)) {
					belegart2 = "2";
				} else if (belegart2.equals(LocaleFac.BELEGART_EINGANGSRECHNUNG)) {
					belegart2 = "3";
				}
				if (belegart2.equals(LocaleFac.BELEGART_ZUSATZKOSTEN)) {
					belegart2 = "4";
				}

				String s1 = d1 + "_" + belegart1;
				String s2 = d2 + "_" + belegart2;

				if (s1.compareTo(s2) > 0) {
					alDaten.set(n, o2);
					alDaten.set(n + 1, o1);
				}
			}
		}

		Object[][] returnArray = new Object[alDaten.size()][REPORT_KASSENJOURNAL_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(returnArray);

		initJRDS(mapParameter, FinanzReportFac.REPORT_MODUL, FinanzReportFac.REPORT_KASSENJOURNAL,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();
	}

	private String getFlrkontoCnr(FLRFinanzKonto konto) {
		return konto == null ? "" : konto.getC_nr();
	}

	private String getFlrkontoCbez(FLRFinanzKonto konto) {
		return konto == null ? "" : konto.getC_bez();
	}

	private String getFlrkontoCbez(FLRSteuerkategorie kategorie) {
		return kategorie == null ? "" : kategorie.getC_bez();
	}

	private String getFlrkontoCbez(FLRFinanzErgebnisgruppe gruppe) {
		return gruppe == null ? "" : gruppe.getC_bez();
	}

	private String getFlrkontoCbez(FLRKostenstelle kostenstelle) {
		return kostenstelle == null ? "" : kostenstelle.getC_bez();
	}

	private void setReportDataFromKonto(Object[] data, FLRFinanzKonto konto, TheClientDto theClientDto) {
		data[REPORT_KONTENLISTE_KONTONUMMER] = getFlrkontoCnr(konto);
		data[REPORT_KONTENLISTE_BEZEICHNUNG] = getFlrkontoCbez(konto);
		data[REPORT_KONTENLISTE_USTKONTONUMMER] = getFlrkontoCnr(konto.getFlrkontoust());
		data[REPORT_KONTENLISTE_SKONTOKONTONUMMER] = getFlrkontoCnr(konto.getFlrkontoskonto());
		try {
			data[REPORT_KONTENLISTE_KONTOART] = getFinanzServiceFac().uebersetzeKontoartOptimal(
					konto.getKontoart_c_nr(), theClientDto.getLocUi(), theClientDto.getLocMandant());
		} catch (Exception e1) {
			data[REPORT_KONTENLISTE_KONTOART] = konto.getKontoart_c_nr();
		}
		data[REPORT_KONTENLISTE_STEUERKATEGORIE] = konto.getSteuerkategorie_c_nr();
		if (konto.getSteuerkategorie_c_nr() != null)
			data[REPORT_KONTENLISTE_STEUERART] = konto.getC_steuerart();

		data[REPORT_KONTENLISTE_GRUPPENTYP] = null;
		if (konto.getFlrergebnisgruppe() != null) {
			data[REPORT_KONTENLISTE_ERGEBNISGRUPPETYP] = konto.getFlrergebnisgruppe().getI_typ();
			if (Helper.short2boolean(konto.getFlrergebnisgruppe().getB_bilanzgruppe()) == true) {
				data[REPORT_KONTENLISTE_GRUPPENTYP] = REPORT_GRUPPENTYP_BILANZ;
				data[REPORT_KONTENLISTE_GRUPPENBEZEICHNUNG] = getFlrkontoCbez(konto.getFlrergebnisgruppe());
				data[REPORT_KONTENLISTE_BILANZKONTO] = konto.getFlrergebnisgruppe().getC_bez();
				data[REPORT_KONTENLISTE_GRUPPENBEZEICHNUNG_NEG] = getFlrkontoCbez(konto.getFlrergebnisgruppenegativ());
			} else {
				data[REPORT_KONTENLISTE_GRUPPENTYP] = REPORT_GRUPPENTYP_ERGEBNIS;
				data[REPORT_KONTENLISTE_GRUPPENBEZEICHNUNG] = getFlrkontoCbez(konto.getFlrergebnisgruppe());
				data[REPORT_KONTENLISTE_ERGEBNISGRUPPE] = konto.getFlrergebnisgruppe().getC_bez();
				data[REPORT_KONTENLISTE_GRUPPENBEZEICHNUNG_NEG] = getFlrkontoCbez(konto.getFlrergebnisgruppenegativ());
			}
		}

		if (konto.getFlruvaart() != null) {
			try {
				data[REPORT_KONTENLISTE_UVAART] = getFinanzServiceFac().uebersetzeUvaartOptimal(
						konto.getFlruvaart().getI_id(), theClientDto.getLocUi(), theClientDto.getLocMandant());
				data[REPORT_KONTENLISTE_UVAART_CNR] = konto.getFlruvaart().getC_nr();
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
		} else {
			data[REPORT_KONTENLISTE_UVAART] = "";
			data[REPORT_KONTENLISTE_UVAART_CNR] = "";
		}

		if (konto.getFlrmwstsatz() != null) {
			data[REPORT_KONTENLISTE_UVAART_VARIANTE] = getMandantFac()
					.mwstsatzFindByPrimaryKey(konto.getFlrmwstsatz().getI_id(), theClientDto)
					.formatMwstsatz(theClientDto);
		} else {
			data[REPORT_KONTENLISTE_UVAART_VARIANTE] = "";

		}

		if (konto.getFinanzamt_i_id() != null) {
			try {
				FinanzamtDto help = getFinanzFac().finanzamtFindByPrimaryKey(konto.getFinanzamt_i_id(),
						theClientDto.getMandant(), theClientDto);
				data[REPORT_KONTENLISTE_FINANZAMT] = help.getPartnerDto().getCName1nachnamefirmazeile1(); // getCReferat();
				data[REPORT_KONTENLISTE_FINANZAMT_KURZBEZ] = help.getPartnerDto().getCKbez();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else {
			data[REPORT_KONTENLISTE_FINANZAMT] = "";
			data[REPORT_KONTENLISTE_FINANZAMT_KURZBEZ] = "";
		}
		data[REPORT_KONTENLISTE_KOSTENSTELLE] = getFlrkontoCbez(konto.getFlrkostenstelle());

		data[REPORT_KONTENLISTE_GUELTIG_BIS] = konto.getD_gueltigbis() == null ? ""
				: konto.getD_gueltigbis().toString();
		data[REPORT_KONTENLISTE_GUELTIG_VON] = konto.getD_gueltigvon() == null ? ""
				: konto.getD_gueltigvon().toString();

		data[REPORT_KONTENLISTE_SORTIERUNG] = konto.getC_sortierung() == null ? "" : konto.getC_sortierung();
		data[REPORT_KONTENLISTE_ALLGEMEIN_SICHTBAR] = konto.getB_allgemeinsichtbar().toString();
		data[REPORT_KONTENLISTE_MANUELL_BEBUCHBAR] = konto.getB_manuellbebuchbar().toString();
		data[REPORT_KONTENLISTE_VERSTECKT] = konto.getB_versteckt().toString();
		data[REPORT_KONTENLISTE_EROEFFNUNGSBUCHUNG] = konto.getB_automeroeffnungsbuchung().toString();
		data[REPORT_KONTENLISTE_BOHNEUST] = konto.getB_ohneust().toString();

	}

	/**
	 * Liste aller Konten drucken
	 * 
	 * @param theClientDto String
	 * @param kontotypCNr  String
	 * @throws EJBExceptionLP
	 * @return JasperPrint
	 */
	public JasperPrintLP printAlleKonten(TheClientDto theClientDto, String kontotypCNr, boolean bMitVersteckten)
			throws EJBExceptionLP {
		Session session = null;
		try {
			this.useCase = UC_KONTENLISTE;
			this.index = -1;
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRFinanzKonto.class);
			// Filter nach Kontotyp
			c.add(Restrictions.eq(FinanzFac.FLR_KONTO_KONTOTYP_C_NR, kontotypCNr));
			// Filter nach Mandant
			c.add(Restrictions.eq(FinanzFac.FLR_KONTO_MANDANT_C_NR, theClientDto.getMandant()));

			if (bMitVersteckten == false) {
				c.add(Restrictions.eq(FinanzFac.FLR_KONTO_B_VERSTECKT, Helper.boolean2Short(false)));
			}

			// Sortierung aufsteigend nach Kontonummer
			c.addOrder(Order.asc(FinanzFac.FLR_KONTO_C_NR));
			List<?> list = c.list();
			data = new Object[list.size()][REPORT_KONTENLISTE_ALLE_FELDER];
			int i = 0;
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLRFinanzKonto konto = (FLRFinanzKonto) iter.next();
				setReportDataFromKonto(data[i], konto, theClientDto);

				// data[i][REPORT_KONTENLISTE_KONTONUMMER] = konto.getC_nr();
				// data[i][REPORT_KONTENLISTE_BEZEICHNUNG] = konto.getC_bez();
				// if (konto.getFlrkontoust() != null) {
				// data[i][REPORT_KONTENLISTE_USTKONTONUMMER] = konto
				// .getFlrkontoust().getC_nr();
				// }
				// if (konto.getFlrkontoskonto() != null) {
				// data[i][REPORT_KONTENLISTE_SKONTOKONTONUMMER] = konto
				// .getFlrkontoskonto().getC_nr();
				// }
				// data[i][REPORT_KONTENLISTE_KONTOART] =
				// konto.getKontoart_c_nr();
				// if (konto.getFlrsteuerkategorie() != null) {
				// data[i][REPORT_KONTENLISTE_STEUERKATEGORIE] = konto
				// .getFlrsteuerkategorie().getC_bez();
				// } else {
				// data[i][REPORT_KONTENLISTE_STEUERKATEGORIE] = "";
				// }
				// if (konto.getFlrergebnisgruppe() != null) {
				// data[i][REPORT_KONTENLISTE_ERGEBNISGRUPPE] = konto
				// .getFlrergebnisgruppe().getC_bez();
				// } else {
				// data[i][REPORT_KONTENLISTE_ERGEBNISGRUPPE] = "";
				// }
				// if (konto.getFlruvaart() != null) {
				//
				// try {
				// data[i][REPORT_KONTENLISTE_UVAART] = getFinanzServiceFac()
				// .uebersetzeUvaartOptimal(
				// konto.getFlruvaart().getI_id(),
				// theClientDto.getLocUi(),
				// theClientDto.getLocMandant());
				// } catch (RemoteException e) {
				// throwEJBExceptionLPRespectOld(e);
				// }
				// } else {
				// data[i][REPORT_KONTENLISTE_UVAART] = "";
				// }
				//
				// try {
				// if (konto.getFinanzamt_i_id() != null) {
				// FinanzamtDto help = getFinanzFac()
				// .finanzamtFindByPrimaryKey(
				// konto.getFinanzamt_i_id(),
				// theClientDto.getMandant(), theClientDto);
				// data[i][REPORT_KONTENLISTE_FINANZAMT] = help
				// .getPartnerDto().getCName1nachnamefirmazeile1(); //
				// getCReferat();
				// data[i][REPORT_KONTENLISTE_FINANZAMT_KURZBEZ] = help
				// .getPartnerDto().getCKbez();
				// }
				// } catch (RemoteException e) {
				// e.printStackTrace();
				// }
				//
				i++;
			}
			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			mapParameter.put("P_KONTOTYP", kontotypCNr);
			mapParameter.put("P_MITVERSTECKTEN", new Boolean(bMitVersteckten));
			initJRDS(mapParameter, FinanzReportFac.REPORT_MODUL, FinanzReportFac.REPORT_ALLE_KONTEN,
					theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
			return getReportPrint();
		} finally {
			closeSession(session);
		}
	}

	/**
	 * Alle Rechnungen eines Mandanten im Zeitraum drucken
	 * 
	 * @param theClientDto String
	 * @param kontoIId     Integer
	 * @return JasperPrint
	 * @throws EJBExceptionLP
	 */
	public JasperPrintLP printBuchungenAufKonto(TheClientDto theClientDto, Integer kontoIId) throws EJBExceptionLP {
		Session session = null;
		try {
			this.useCase = UC_BUCHUNGEN_AUF_KONTO;
			this.index = -1;

			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRFinanzBuchungDetail.class);
			// Filter nach Konto
			c.add(Restrictions.eq(FinanzFac.FLR_KONTO_I_ID, kontoIId));
			List<?> list = c.list();
			data = new Object[list.size()][7];
			int i = 0;
			KontoDtoSmall kontoDto = getFinanzFac().kontoFindByPrimaryKeySmall(kontoIId);
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLRFinanzBuchungDetail b = (FLRFinanzBuchungDetail) iter.next();
				data[i][REPORT_BUCHUNGEN_AUF_KONTO_AUSZUG] = b.getI_auszug();
				data[i][REPORT_BUCHUNGEN_AUF_KONTO_BUCHUNGSART] = b.getFlrbuchung().getBuchungsart_c_nr();
				data[i][REPORT_BUCHUNGEN_AUF_KONTO_BUCHUNGSDATUM] = b.getFlrbuchung().getD_buchungsdatum();
				data[i][REPORT_BUCHUNGEN_AUF_KONTO_BETRAG] = b.getN_betrag();
				data[i][REPORT_BUCHUNGEN_AUF_KONTO_TEXT] = b.getFlrbuchung().getC_text();
				data[i][REPORT_BUCHUNGEN_AUF_KONTO_UST] = b.getN_ust();
				i++;
			}
			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
			mapParameter.put(LPReport.P_WAEHRUNG, mandantDto.getWaehrungCNr());
			mapParameter.put("P_KONTONUMMER", kontoDto.getCNr());
			mapParameter.put("P_KONTOBEZEICHNUNG", kontoDto.getCBez());
			initJRDS(mapParameter, FinanzReportFac.REPORT_MODUL, FinanzReportFac.REPORT_BUCHUNGEN_AUF_KONTO,
					theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
			return getReportPrint();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		} finally {
			closeSession(session);
		}
	}

	/**
	 * Buchungen in einem Buchungsjournal drucken.
	 * 
	 * @param theClientDto       String
	 * @param buchungsjournalIId Integer
	 * @return JasperPrint
	 * @throws EJBExceptionLP
	 */
	public JasperPrintLP printBuchungenInBuchungsjournal(TheClientDto theClientDto, Integer buchungsjournalIId)
			throws EJBExceptionLP {
		Session session = null;
		try {
			this.useCase = UC_BUCHUNGEN_IN_BUCHUNGSJOURNAL;
			this.index = -1;
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			/*
			 * Criteria c = session.createCriteria(FLRFinanzBuchungsjournal.class);
			 * 
			 * List<?> list = c.list(); data = new Object[list.size()][7]; int i = 0; for
			 * (Iterator<?> iter = list.iterator(); iter.hasNext();) {
			 * FLRFinanzBuchungsjournal b = (FLRFinanzBuchungsjournal) iter .next();
			 * 
			 * i++; }
			 */
			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
			mapParameter.put(LPReport.P_WAEHRUNG, mandantDto.getWaehrungCNr());
			initJRDS(mapParameter, FinanzReportFac.REPORT_MODUL, FinanzReportFac.REPORT_BUCHUNGEN_IN_BUCHUNGSJOURNAL,
					theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
			return getReportPrint();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		} finally {
			closeSession(session);
		}
	}

	public Map<String, Map<String, String>> getSammelKontoMap(Integer finanzamtIId) {
		HvTypedCriteria<FLRFinanzKonto> crit = new HvTypedCriteria<FLRFinanzKonto>(
				FLRSessionFactory.getFactory().openSession().createCriteria(FLRFinanzKonto.class));
		crit.add(Restrictions.ne("kontoart_c_nr", FinanzServiceFac.KONTOART_NICHT_ZUTREFFEND))
				.add(Restrictions.eq("kontotyp_c_nr", FinanzServiceFac.KONTOTYP_SACHKONTO))
				.add(Restrictions.eq("finanzamt_i_id", finanzamtIId)).addOrder(Order.asc("kontoart_c_nr"))
				.addOrder(Order.asc("c_nr"));
		List<FLRFinanzKonto> list = crit.list();
		Map<String, Map<String, String>> kontenByArt = new HashMap<String, Map<String, String>>();
		for (FLRFinanzKonto konto : list) {
			if (kontenByArt.get(konto.getKontoart_c_nr()) == null) {
				kontenByArt.put(konto.getKontoart_c_nr(), new TreeMap<String, String>());
			}
			Map<String, String> kontoBezByCnr = kontenByArt.get(konto.getKontoart_c_nr());
			kontoBezByCnr.put(konto.getC_nr(), konto.getC_bez());
		}
		return kontenByArt;
	}

	public JasperPrintLP printSteuerkategorien(TheClientDto theClientDto) {
		this.useCase = UC_STEUERKATEGORIEN;
		HashMap mapParameter = new HashMap();

		Session sessionFinanzamt = FLRSessionFactory.getFactory().openSession();
		String queryStringFinanzamt = "SELECT fa FROM FLRFinanzFinanzamt as fa WHERE fa.mandant_c_nr='"
				+ theClientDto.getMandant() + "' ORDER BY fa.flrpartner.c_name1nachnamefirmazeile1 ASC ";

		Query queryFinanzamt = sessionFinanzamt.createQuery(queryStringFinanzamt);

		List<?> resultsFinanzamt = queryFinanzamt.list();
		Iterator<?> resultListIteratorFinanzamt = resultsFinanzamt.iterator();

		ArrayList<Object> alDaten = new ArrayList<Object>();

		while (resultListIteratorFinanzamt.hasNext()) {

			FLRFinanzFinanzamt fa = (FLRFinanzFinanzamt) resultListIteratorFinanzamt.next();
			FinanzamtDto faDto = null;
			ReversechargeartDto rcartOhneDto = null;

			try {
				faDto = getFinanzFac().finanzamtFindByPrimaryKey(fa.getI_id(), fa.getMandant_c_nr(), theClientDto);
				rcartOhneDto = getFinanzServiceFac().reversechargeartFindOhne(theClientDto);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			Session session = FLRSessionFactory.getFactory().openSession();

			String queryString = "SELECT sk FROM FLRSteuerkategoriekonto as sk WHERE sk.flrsteuerkategorie.finanzamt_i_id="
					+ fa.getI_id()
					+ " ORDER BY sk.flrsteuerkategorie.i_sort ASC, sk.flrmwstsatzbez.c_bezeichnung ASC, sk.gueltigAb ASC ";

			Query query = session.createQuery(queryString);

			List<?> results = query.list();
			Iterator<?> resultListIterator = results.iterator();

			Integer prevFinanzAmt = null;
			Map<String, Map<String, String>> prevSammelkontenMap = null;
			ReversechargeartDto rcartDto = new ReversechargeartDto();

			while (resultListIterator.hasNext()) {
				FLRSteuerkategoriekonto sk = (FLRSteuerkategoriekonto) resultListIterator.next();

				Object[] oZeile = new Object[REPORT_STEUERKATEGORIE_ANZAHL_FELDER];

				oZeile[REPORT_STEUERKATEGORIE_GUELTIG_AB] = sk.getGueltigAb();

				if (!fa.getI_id().equals(prevFinanzAmt)) {
					// damit nicht jedesmal die Sammelkonten geholt werden,
					// sondern nur, wenn das naechste Finanzamt kommt
					prevFinanzAmt = fa.getI_id();
					prevSammelkontenMap = getSammelKontoMap(fa.getI_id());
				}

				oZeile[REPORT_STEUERKATEGORIE_SAMMELKONTO_MAP] = prevSammelkontenMap;

				oZeile[REPORT_STEUERKATEGORIE_FINANZAMT] = HelperServer.formatNameAusFLRPartner(fa.getFlrpartner());
				oZeile[REPORT_STEUERKATEGORIE_FINANZAMT_I_ID] = fa.getI_id();
				oZeile[REPORT_STEUERKATEGORIE_STEUERKATEGORIE] = sk.getFlrsteuerkategorie().getC_nr();
				oZeile[REPORT_STEUERKATEGORIE_STEUERKATEGORIEBEZEICHNUNG] = sk.getFlrsteuerkategorie().getC_bez();

				oZeile[REPORT_STEUERKATEGORIE_MWST_BEZEICHNUNG] = sk.getFlrmwstsatzbez().getC_bezeichnung();

				if (sk.getKontoiidek() != null) {
					oZeile[REPORT_STEUERKATEGORIE_KONTO_EK] = sk.getKontoiidek().getC_nr();
					oZeile[REPORT_STEUERKATEGORIE_KONTO_EK_BEZEICHNUNG] = sk.getKontoiidek().getC_bez();
				}
				if (sk.getKontoiidvk() != null) {
					oZeile[REPORT_STEUERKATEGORIE_KONTO_VK] = sk.getKontoiidvk().getC_nr();
					oZeile[REPORT_STEUERKATEGORIE_KONTO_VK_BEZEICHNUNG] = sk.getKontoiidvk().getC_bez();
				}
				if (sk.getKontoiidskontoek() != null) {
					oZeile[REPORT_STEUERKATEGORIE_KONTO_SKONTO_EK] = sk.getKontoiidskontoek().getC_nr();
					oZeile[REPORT_STEUERKATEGORIE_KONTO_SKONTO_EK_BEZEICHNUNG] = sk.getKontoiidskontoek().getC_bez();
				}
				if (sk.getKontoiidskontovk() != null) {
					oZeile[REPORT_STEUERKATEGORIE_KONTO_SKONTO_VK] = sk.getKontoiidskontovk().getC_nr();
					oZeile[REPORT_STEUERKATEGORIE_KONTO_SKONTO_VK_BEZEICHNUNG] = sk.getKontoiidskontovk().getC_bez();
				}
				if (sk.getKontoiideinfuhrust() != null) {
					oZeile[REPORT_STEUERKATEGORIE_KONTO_EINFUHRUST] = sk.getKontoiideinfuhrust().getC_nr();
					oZeile[REPORT_STEUERKATEGORIE_KONTO_EINFUHRUST_BEZEICHNUNG] = sk.getKontoiideinfuhrust().getC_bez();
				}

				try {
					if (faDto.getKontoIIdEbdebitoren() != null) {
						KontoDto kDto = getFinanzFac().kontoFindByPrimaryKey(faDto.getKontoIIdEbdebitoren());
						oZeile[REPORT_STEUERKATEGORIE_PARAMETER_DEBITOREN] = kDto.getCNr();
						oZeile[REPORT_STEUERKATEGORIE_PARAMETER_DEBITOREN_BEZEICHNUNG] = kDto.getCBez();

					}
					if (faDto.getKontoIIdAnzahlungErhaltBezahlt() != null) {
						KontoDto kDto = getFinanzFac().kontoFindByPrimaryKey(faDto.getKontoIIdAnzahlungErhaltBezahlt());
						oZeile[REPORT_STEUERKATEGORIE_PARAMETER_ERHALTEN] = kDto.getCNr();
						oZeile[REPORT_STEUERKATEGORIE_PARAMETER_ERHALTEN_BEZEICHNUNG] = kDto.getCBez();

					}
					if (faDto.getKontoIIdAnzahlungGegebenBezahlt() != null) {
						KontoDto kDto = getFinanzFac()
								.kontoFindByPrimaryKey(faDto.getKontoIIdAnzahlungGegebenBezahlt());
						oZeile[REPORT_STEUERKATEGORIE_PARAMETER_GELEISTET] = kDto.getCNr();
						oZeile[REPORT_STEUERKATEGORIE_PARAMETER_GELEISTET_BEZEICHNUNG] = kDto.getCBez();

					}
					if (faDto.getKontoIIdEbkreditoren() != null) {
						KontoDto kDto = getFinanzFac().kontoFindByPrimaryKey(faDto.getKontoIIdEbkreditoren());
						oZeile[REPORT_STEUERKATEGORIE_PARAMETER_KREDITOREN] = kDto.getCNr();
						oZeile[REPORT_STEUERKATEGORIE_PARAMETER_KREDITOREN_BEZEICHNUNG] = kDto.getCBez();

					}
					if (faDto.getKontoIIdEbsachkonten() != null) {
						KontoDto kDto = getFinanzFac().kontoFindByPrimaryKey(faDto.getKontoIIdEbsachkonten());
						oZeile[REPORT_STEUERKATEGORIE_PARAMETER_SACHKONTO] = kDto.getCNr();
						oZeile[REPORT_STEUERKATEGORIE_PARAMETER_SACHKONTO_BEZEICHNUNG] = kDto.getCBez();

					}
					if (faDto.getKontoIIdAnzahlungErhaltVerr() != null) {
						KontoDto kDto = getFinanzFac().kontoFindByPrimaryKey(faDto.getKontoIIdAnzahlungErhaltVerr());
						oZeile[REPORT_STEUERKATEGORIE_PARAMETER_VERR_ERHALTEN] = kDto.getCNr();
						oZeile[REPORT_STEUERKATEGORIE_PARAMETER_VERR_ERHALTEN_BEZEICHNUNG] = kDto.getCBez();

					}
					if (faDto.getKontoIIdAnzahlungGegebenVerr() != null) {
						KontoDto kDto = getFinanzFac().kontoFindByPrimaryKey(faDto.getKontoIIdAnzahlungGegebenVerr());
						oZeile[REPORT_STEUERKATEGORIE_PARAMETER_VERR_GELEISTET] = kDto.getCNr();
						oZeile[REPORT_STEUERKATEGORIE_PARAMETER_VERR_GELEISTET_BEZEICHNUNG] = kDto.getCBez();

					}

					SteuerkategorieDto skDto = getFinanzServiceFac()
							.steuerkategorieFindByPrimaryKey(sk.getFlrsteuerkategorie().getI_id(), theClientDto);

					oZeile[REPORT_STEUERKATEGORIE_REVERSECHARGE] = new Boolean(
							!rcartOhneDto.getIId().equals(skDto.getReversechargeartId()));

					if (skDto.getKontoIIdForderungen() != null) {
						KontoDto kDto = getFinanzFac().kontoFindByPrimaryKey(skDto.getKontoIIdForderungen());
						oZeile[REPORT_STEUERKATEGORIE_KONTO_FORDERUNGEN] = kDto.getCNr();
						oZeile[REPORT_STEUERKATEGORIE_KONTO_FORDERUNGEN_BEZEICHNUNG] = kDto.getCBez();
					}
					if (skDto.getKontoIIdKursgewinn() != null) {
						KontoDto kDto = getFinanzFac().kontoFindByPrimaryKey(skDto.getKontoIIdKursgewinn());
						oZeile[REPORT_STEUERKATEGORIE_KONTO_KURSGEWINNE] = kDto.getCNr();
						oZeile[REPORT_STEUERKATEGORIE_KONTO_KURSGEWINNE_BEZEICHNUNG] = kDto.getCBez();
					}
					if (skDto.getKontoIIdKursverlust() != null) {
						KontoDto kDto = getFinanzFac().kontoFindByPrimaryKey(skDto.getKontoIIdKursverlust());
						oZeile[REPORT_STEUERKATEGORIE_KONTO_KURSVERLUSTE] = kDto.getCNr();
						oZeile[REPORT_STEUERKATEGORIE_KONTO_KURSVERLUSTE_BEZEICHNUNG] = kDto.getCBez();
					}
					if (skDto.getKontoIIdVerbindlichkeiten() != null) {
						KontoDto kDto = getFinanzFac().kontoFindByPrimaryKey(skDto.getKontoIIdVerbindlichkeiten());
						oZeile[REPORT_STEUERKATEGORIE_KONTO_VERBINDLICHKEITEN] = kDto.getCNr();
						oZeile[REPORT_STEUERKATEGORIE_KONTO_VERBINDLICHKEITEN_BEZEICHNUNG] = kDto.getCBez();
					}

					oZeile[REPORT_STEUERKATEGORIE_REVERSECHARGEART_I_ID] = skDto.getReversechargeartId();
					if (!skDto.getReversechargeartId().equals(rcartDto.getIId())) {
						rcartDto = getFinanzServiceFac().reversechargeartFindByPrimaryKey(skDto.getReversechargeartId(),
								theClientDto);
					}
					oZeile[REPORT_STEUERKATEGORIE_REVERSECHARGEART_C_NR] = rcartDto.getCNr();
					oZeile[REPORT_STEUERKATEGORIE_REVERSECHARGEART_BEZ] = rcartDto.getSprDto().getcBez();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				alDaten.add(oZeile);

			}
			session.close();
		}

		sessionFinanzamt.close();

		Object[][] returnArray = new Object[alDaten.size()][REPORT_STEUERKATEGORIE_ANZAHL_FELDER];
		data = (Object[][]) alDaten.toArray(returnArray);

		initJRDS(mapParameter, FinanzReportFac.REPORT_MODUL, FinanzReportFac.REPORT_STEUERKATEGORIEN,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();

	}

	private static class HvRestrictions {
		public static HvRTrimExpression rtrim(String propertyName) {
			return new HvRTrimExpression(propertyName);
		}

		public static HvRTrimLikeExpression rtrimLike(String propertyName, String value) {
			return new HvRTrimLikeExpression(propertyName, value);
		}

		public static HvLowerLikeExpression lowerLike(String propertyName, String value) {
			return new HvLowerLikeExpression(propertyName, value);
		}

		public static HvRTrimLikeExpression rtrimLike(String propertyName, String value, MatchMode matchMode) {
			return new HvRTrimLikeExpression(propertyName, value, matchMode);
		}

		public static HvLowerLikeExpression lowerLike(String propertyName, String value, MatchMode matchMode) {
			return new HvLowerLikeExpression(propertyName, value, matchMode);
		}
	}

	private void addCriteriaBelegNummer(Criteria c, String belegnummer) {
		if (belegnummer == null)
			return;
		String s = "%" + belegnummer;
		c.add(HvRestrictions.rtrimLike("s." + FinanzFac.FLR_BUCHUNG_C_BELEGNUMMER, s));
		// c.add(Restrictions.like("s." + FinanzFac.FLR_BUCHUNG_C_BELEGNUMMER,
		// s, MatchMode.EXACT)) ;
		// s = "%" + s ;
		// c.add(Restrictions.like("s."
		// + FinanzFac.FLR_BUCHUNG_C_BELEGNUMMER, s, MatchMode.START));
	}

	private void addCriteriaKontoNummer(Criteria c, String kontonummer) {
		if (kontonummer == null)
			return;
		String s = kontonummer + "%";
		c.add(Restrictions.like("konto.c_nr", s));
	}

	private void addCriteriaBuchungsart(Criteria c, String buchungsart, String belegart) {
		if (buchungsart != null) {
			c.add(HvRestrictions.lowerLike("buchart.c_kbez", buchungsart.toLowerCase(), MatchMode.ANYWHERE));
		}
		if (belegart != null) {
			c.add(HvRestrictions.lowerLike("belegart.c_kbez", belegart.toLowerCase(), MatchMode.ANYWHERE));
		}
	}

	/**
	 * Buchungsjournal drucken.
	 * 
	 * @param theClientDto String
	 * @param params       die Parameter des Reports
	 * @return JasperPrint
	 * @throws EJBExceptionLP
	 */
	public JasperPrintLP printBuchungsjournal(TheClientDto theClientDto, BuchungsjournalReportParameter params)
			// Integer buchungsjournalIId, Date dVon, Date dBis,
			// boolean storniert, boolean bDatumsfilterIstBuchungsdatum,
			// String text, String belegnummer, BigDecimal betrag, String kontonummer)
			throws EJBExceptionLP {
		Session session = null;
		try {
			this.useCase = UC_BUCHUNGSJOURNAL;
			this.index = -1;

			Map<String, Object> mapParameter = new TreeMap<String, Object>();

			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRFinanzBuchungDetail.class);

			Timestamp help = new Timestamp(params.getdVon().getTime());

			c.createAlias("flrbuchung", "s");
			String datumsfilter = "s.d_buchungsdatum";
			if (!params.isbDatumsfilterIstBuchungsdatum()) {
				datumsfilter = "t_anlegen";
			}
			c.add(Restrictions.ge(datumsfilter, Helper.cutTimestamp(help)));

			if (params.getBuchungsText() != null) {
				c.add(Restrictions.ilike("s." + FinanzFac.FLR_BUCHUNG_C_TEXT, "%" + params.getBuchungsText() + "%"));
				mapParameter.put("P_TEXT", params.getBuchungsText());

			}

			addCriteriaBelegNummer(c, params.getBelegnummer());

			c.createAlias("flrkonto", "konto");
			addCriteriaKontoNummer(c, params.getKontonummer());

			c.createAlias("s.flrbuchungsart", "buchart");
			c.createAlias("s.flrfbbelegart", "belegart", Criteria.LEFT_JOIN);
			addCriteriaBuchungsart(c, params.getBuchungsart(), params.getBelegart());

			if (params.getBetrag() != null) {
				Integer toleranzBetragsuche = new Integer(
						getParameterFac().getMandantparameter(theClientDto.getMandant(), ParameterFac.KATEGORIE_FINANZ,
								ParameterFac.PARAMETER_TOLERANZ_BETRAGSUCHE).getCWert());

				BigDecimal value1 = params.getBetrag()
						.subtract(new BigDecimal(
								params.getBetrag().doubleValue() * ((double) toleranzBetragsuche / (double) 100)))
						.abs();
				BigDecimal value2 = params.getBetrag()
						.add(new BigDecimal(
								params.getBetrag().doubleValue() * ((double) toleranzBetragsuche / (double) 100)))
						.abs();

				c.add(Restrictions.or(Restrictions.between(FinanzFac.FLR_BUCHUNGDETAIL_N_BETRAG, value1, value2),
						Restrictions.between(FinanzFac.FLR_BUCHUNGDETAIL_N_BETRAG, value2.negate(), value1.negate())));

				mapParameter.put("P_TOLERANZ_BETRAGSSUCHE", toleranzBetragsuche);
				mapParameter.put("P_BETRAG", params.getBetrag());

			}

			help = Helper.addiereTageZuTimestamp(new Timestamp(params.getdBis().getTime()), 1);

			c.add(Restrictions.lt(datumsfilter, Helper.cutTimestamp(help)));

			if (params.isStorniert() != true) {
				c.add(Restrictions.isNull("s.t_storniert"));
			}

			c.createAlias("s.flrkostenstelle", "k");
			c.add(Restrictions.eq("k.mandant_c_nr", theClientDto.getMandant()));

			c.addOrder(Order.asc(datumsfilter));
			c.addOrder(Order.asc("buchung_i_id"));

			List<?> list = c.list();
			data = new Object[list.size()][REPORT_BUCHUNGSJOURNAL_ANZAHL_SPALTEN];
			int i = 0;

			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLRFinanzBuchungDetail b = (FLRFinanzBuchungDetail) iter.next();
				data[i][REPORT_BUCHUNGSJOURNAL_AUSZUG] = b.getI_auszug();
				data[i][REPORT_BUCHUNGSJOURNAL_BELEGNUMMER] = b.getFlrbuchung().getC_belegnummer();
				data[i][REPORT_BUCHUNGSJOURNAL_BUCHUNGSART] = b.getFlrbuchung().getBuchungsart_c_nr();
				data[i][REPORT_BUCHUNGSJOURNAL_BUCHUNGSDATUM] = b.getFlrbuchung().getD_buchungsdatum();
				if (b.getFlrgegenkonto() != null) {
					data[i][REPORT_BUCHUNGSJOURNAL_GEGENKONTONUMMER] = b.getFlrgegenkonto().getC_nr();
				}
				data[i][REPORT_BUCHUNGSJOURNAL_BETRAG] = b.getN_betrag();
				data[i][REPORT_BUCHUNGSJOURNAL_KONTONUMMER] = b.getFlrkonto().getC_nr();
				data[i][REPORT_BUCHUNGSJOURNAL_KOSTENSTELLENUMMER] = b.getFlrbuchung().getFlrkostenstelle().getC_nr();
				data[i][REPORT_BUCHUNGSJOURNAL_TEXT] = b.getFlrbuchung().getC_text();
				data[i][REPORT_BUCHUNGSJOURNAL_UST] = b.getN_ust();

				if (b.getFlrbuchung().getT_storniert() == null) {
					data[i][REPORT_BUCHUNGSJOURNAL_STORNIERT] = new Boolean(false);
				} else {
					data[i][REPORT_BUCHUNGSJOURNAL_STORNIERT] = new Boolean(true);
				}

				if (b.getBuchungdetailart_c_nr().equals("SOLL           ")) {
					data[i][REPORT_BUCHUNGSJOURNAL_SOLL] = b.getN_betrag();
					data[i][REPORT_BUCHUNGSJOURNAL_HABEN] = null;
				} else if (b.getBuchungdetailart_c_nr().equals("HABEN          ")) {
					data[i][REPORT_BUCHUNGSJOURNAL_HABEN] = b.getN_betrag();
					data[i][REPORT_BUCHUNGSJOURNAL_SOLL] = null;
				} else {
					data[i][REPORT_BUCHUNGSJOURNAL_SOLL] = null;
					data[i][REPORT_BUCHUNGSJOURNAL_HABEN] = null;
				}

				data[i][REPORT_BUCHUNGSJOURNAL_BUCHUNGSJOURNALDATUM] = b.getT_anlegen();

				data[i][REPORT_BUCHUNGSJOURNAL_WER] = getPersonalFac()
						.personalFindByPrimaryKeySmall(b.getFlrbuchung().getPersonal_i_id_anlegen()).getCKurzzeichen();
				data[i][REPORT_BUCHUNGSJOURNAL_BUCHUNGSART_KBEZ] = b.getFlrbuchung().getFlrbuchungsart().getC_kbez();
				FLRFbbelegart flrBelegart = b.getFlrbuchung().getFlrfbbelegart();
				data[i][REPORT_BUCHUNGSJOURNAL_BELEGART_KBEZ] = flrBelegart != null ? flrBelegart.getC_kbez() : null;
				data[i][REPORT_BUCHUNGSJOURNAL_AUTOMATISCHE_BUCHUNG] = Helper
						.short2Boolean(b.getFlrbuchung().getB_autombuchung());
				data[i][REPORT_BUCHUNGSJOURNAL_AUTOMATISCHE_EB] = Helper
						.short2boolean(b.getFlrbuchung().getB_autombuchungeb());
				data[i][REPORT_BUCHUNGSJOURNAL_UVAVERPROBUNG_IID] = b.getFlrbuchung().getUvaverprobung_i_id();
				i++;
			}
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);

			mapParameter.put(LPReport.P_WAEHRUNG, mandantDto.getWaehrungCNr());
			mapParameter.put("P_VON", params.getdVon());
			mapParameter.put("P_MITSTORNIERTEN", new Boolean(params.isStorniert()));
			mapParameter.put("P_DATUMSFILTER_IST_BUCHUNGSDATUM", new Boolean(params.isbDatumsfilterIstBuchungsdatum()));
			mapParameter.put("P_BIS", params.getdBis());
			mapParameter.put("P_BELEGART_KBEZ", params.getBelegart());
			mapParameter.put("P_BUCHUNGSART_KBEZ", params.getBuchungsart());
			mapParameter.put("P_KONTONUMMER", params.getKontonummer());

			initJRDS(mapParameter, FinanzReportFac.REPORT_MODUL, FinanzReportFac.REPORT_BUCHUNGSJOURNAL,
					theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
			return getReportPrint();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		} finally {
			closeSession(session);
		}
	}

	public JasperPrintLP printRASchreiben(TheClientDto theClientDto, Integer mahnungIId) throws EJBExceptionLP {
		try {
			this.useCase = UC_RA_SCHREIBEN;
			index = -1;
			MahnungDto mahnungDto = getMahnwesenFac().mahnungFindByPrimaryKey(mahnungIId);
			return printRASchreibenFuerRechnung(theClientDto, mahnungDto.getRechnungIId());
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	public JasperPrintLP printRASchreibenFuerRechnung(TheClientDto theClientDto, Integer rechnungIId)
			throws EJBExceptionLP {
		try {
			this.useCase = UC_RA_SCHREIBEN;
			index = -1;
			// zzt. nur fuer eine rechnung
			data = new Object[1][REPORT_RA_SCHREIBEN_ANZAHL_KRITERIEN];
			RechnungDto rechnungDto = getRechnungFac().rechnungFindByPrimaryKey(rechnungIId);
			data[0][REPORT_RA_SCHREIBEN_RECHNUNGSNUMMER] = rechnungDto.getCNr();
			data[0][REPORT_RA_SCHREIBEN_RECHNUNGSDATUM] = rechnungDto.getTBelegdatum();

			ZahlungszielDto zahlungszielDto = getMandantFac()
					.zahlungszielFindByPrimaryKey(rechnungDto.getZahlungszielIId(), theClientDto);
			data[0][REPORT_RA_SCHREIBEN_ZIELDATUM] = Helper.addiereTageZuDatum(rechnungDto.getTBelegdatum(),
					zahlungszielDto.getAnzahlZieltageFuerNetto().intValue());

			MahnstufeDto mahnstufeDto = getMahnwesenFac().mahnstufeFindByPrimaryKey(
					new MahnstufePK(FinanzServiceFac.MAHNSTUFE_99, theClientDto.getMandant()));
			Double dZinssatz = new Double(0);
			if (mahnstufeDto.getFZinssatz() != null) {
				dZinssatz = mahnstufeDto.getFZinssatz().doubleValue();
			}

			if (!rechnungDto.getRechnungartCNr().equals(RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
				data[0][REPORT_RA_SCHREIBEN_BETRAG] = rechnungDto.getNWert();
				data[0][REPORT_RA_SCHREIBEN_ZINSSATZ] = dZinssatz;

				BigDecimal bdZinsenFuerEinJahr = rechnungDto.getNWert()
						.multiply(new BigDecimal(dZinssatz).movePointLeft(2));
				BigDecimal bdDays = new BigDecimal(Helper.getDifferenzInTagen(
						Helper.addiereTageZuDatum(rechnungDto.getTBelegdatum(),
								zahlungszielDto.getAnzahlZieltageFuerNetto().intValue()),
						new java.util.Date(System.currentTimeMillis())));
				BigDecimal bdZinsenFuerEinenTag = bdZinsenFuerEinJahr.divide(new BigDecimal(360.0), 4,
						BigDecimal.ROUND_HALF_EVEN);
				BigDecimal bdZinsen = bdZinsenFuerEinenTag.multiply(bdDays);
				bdZinsen = Helper.rundeKaufmaennisch(bdZinsen, FinanzFac.NACHKOMMASTELLEN);
				data[0][REPORT_RA_SCHREIBEN_ZINSEN] = bdZinsen;

				data[0][REPORT_RA_SCHREIBEN_MAHNSPESEN] = new BigDecimal(0);

				BigDecimal bdSpesenFw = getMahnspesen(mahnstufeDto,
						Helper.addiereTageZuDatum(rechnungDto.getTBelegdatum(),
								zahlungszielDto.getAnzahlZieltageFuerNetto().intValue()),
						theClientDto, rechnungDto.getWaehrungCNr());

				if (bdSpesenFw != null) {
					data[0][REPORT_RA_SCHREIBEN_MAHNSPESEN] = bdSpesenFw;
				}
			} else {
				data[0][REPORT_RA_SCHREIBEN_BETRAG] = rechnungDto.getNWert().negate();
				data[0][REPORT_RA_SCHREIBEN_ZINSSATZ] = new Double(0);
				data[0][REPORT_RA_SCHREIBEN_MAHNSPESEN] = new BigDecimal(0);
				data[0][REPORT_RA_SCHREIBEN_ZINSEN] = new BigDecimal(0);
			}

			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			PersonalDto oPersonalBenutzer = getPersonalFac().personalFindByPrimaryKey(theClientDto.getIDPersonal(),
					theClientDto);
			PersonalDto oPersonalAnleger = getPersonalFac()
					.personalFindByPrimaryKey(rechnungDto.getPersonalIIdAnlegen(), theClientDto);
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
			mapParameter.put(LPReport.P_MANDANTADRESSE, Helper.formatMandantAdresse(mandantDto));
			mapParameter.put("P_UNSER_ZEICHEN", Helper.getKurzzeichenkombi(oPersonalBenutzer.getCKurzzeichen(),
					oPersonalAnleger.getCKurzzeichen()));
			mapParameter.put("P_DATUM", new java.util.Date(System.currentTimeMillis()));
			mapParameter.put(LPReport.P_WAEHRUNG, mandantDto.getWaehrungCNr());

			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(rechnungDto.getKundeIId(), theClientDto);
			Locale locDruck = Helper.string2Locale(kundeDto.getPartnerDto().getLocaleCNrKommunikation());

			AnsprechpartnerDto ansprechpartnerDto = null;
			if (rechnungDto.getAnsprechpartnerIId() != null) {
				ansprechpartnerDto = getAnsprechpartnerFac()
						.ansprechpartnerFindByPrimaryKey(rechnungDto.getAnsprechpartnerIId(), theClientDto);
			}
			mapParameter.put("P_SCHULDNER_ANREDE",
					formatAdresseFuerAusdruck(kundeDto.getPartnerDto(), ansprechpartnerDto, mandantDto, locDruck));
			String sTelefon = kundeDto.getPartnerDto().getCTelefon();
			String sFax = kundeDto.getPartnerDto().getCFax();

			mapParameter.put("P_SCHULDNER_TELEFON", sTelefon);
			mapParameter.put("P_SCHULDNER_FAX", sFax);

			mapParameter.put("P_ABSENDER", mandantDto.getPartnerDto().formatAnrede());
			mapParameter.put("P_BENUTZERNAME", oPersonalBenutzer.getPartnerDto().formatAnrede());

			MahntextDto mahntextDto = getFinanzServiceFac().mahntextFindByMandantLocaleCNr(mandantDto.getCNr(),
					Helper.locale2String(locDruck), new Integer(FinanzServiceFac.MAHNSTUFE_99));
			if (mahntextDto != null) {
				mapParameter.put("P_MAHNTEXT", Helper.formatStyledTextForJasper(mahntextDto.getCTextinhalt()));
			} else {
				EJBExceptionLP e = new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_KEINE_MAHNTEXTE_EINGETRAGEN, "");
				ArrayList<Object> a = new ArrayList<Object>();
				a.add(mandantDto.getCNr() + "|" + Helper.locale2String(locDruck) + "|"
						+ new Integer(FinanzServiceFac.MAHNSTUFE_99));
				e.setAlInfoForTheClient(a);
				throw e;
			}
			initJRDS(mapParameter, FinanzReportFac.REPORT_MODUL, FinanzReportFac.REPORT_RA_SCHREIBEN,
					theClientDto.getMandant(), locDruck, theClientDto);
			return getReportPrint();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	public JasperPrintLP printMahnungAusMahnlauf(TheClientDto theClientDto, Integer mahnungIId, Boolean bMitLogo)
			throws EJBExceptionLP {
		try {
			this.useCase = UC_MAHNUNG;
			data = new Object[1][1];
			index = -1;
			MahnungDto mahnungDto = getMahnwesenFac().mahnungFindByPrimaryKey(mahnungIId);
			return printMahnungFuerRechnung(theClientDto, mahnungDto.getRechnungIId(), mahnungDto.getMahnstufeIId(),
					bMitLogo);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	public BigDecimal getMahnspesen(MahnstufeDto mahnstufeDto, java.sql.Date tMahndatum, TheClientDto theClientDto,
			String waehrungCNr) {

		if (!theClientDto.getSMandantenwaehrung().equals(waehrungCNr)) {
			try {
				javax.persistence.Query query = em.createNamedQuery("MahnspesenFindByIMahnstufeMandantCNrWaehrungCNr");
				query.setParameter(1, mahnstufeDto.getIId());
				query.setParameter(2, mahnstufeDto.getMandantCNr());
				query.setParameter(3, waehrungCNr);
				Mahnspesen spesen = (Mahnspesen) query.getSingleResult();

				return spesen.getNMahnspesen();

			} catch (NoResultException ex1) {
				// Nothing
			}
		}

		if (mahnstufeDto.getNMahnspesen() != null) {

			BigDecimal bdSpesenFw = null;
			try {
				bdSpesenFw = getLocaleFac().rechneUmInAndereWaehrungZuDatum(mahnstufeDto.getNMahnspesen(),
						theClientDto.getSMandantenwaehrung(), waehrungCNr, tMahndatum, theClientDto);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			return bdSpesenFw;

		} else {
			return new BigDecimal(0);
		}

	}

	public HashMap<String, Object> getMahnungsParameterFuerRechnung(TheClientDto theClientDto, Integer rechnungIId,
			Integer mahnstufeIId) throws EJBExceptionLP, RemoteException {
		MahnungDto mahnungDto = getMahnwesenFac().mahnungFindByRechnungMahnstufe(rechnungIId, mahnstufeIId);

		HashMap<String, Object> mapParameter = new HashMap<String, Object>();
		RechnungDto rechnungDto = getRechnungFac().rechnungFindByPrimaryKey(rechnungIId);
		// stornierte Rechnungen nicht zulassen
		if (rechnungDto.getStatusCNr().equals(RechnungFac.STATUS_STORNIERT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_EXPORT_BELEG_IST_NOCH_NICHT_AKTIVIERT, "");
		}
		KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(rechnungDto.getKundeIId(), theClientDto);
		Locale locDruck = Helper.string2Locale(kundeDto.getPartnerDto().getLocaleCNrKommunikation());
		AnsprechpartnerDto oAnsprechpartner = null;
		if (rechnungDto.getAnsprechpartnerIId() != null) {
			oAnsprechpartner = getAnsprechpartnerFac()
					.ansprechpartnerFindByPrimaryKey(rechnungDto.getAnsprechpartnerIId(), theClientDto);
		}

		Integer ansprechpartnerIId = null;
		if (oAnsprechpartner != null) {
			ansprechpartnerIId = oAnsprechpartner.getIId();
		}

		String sEmail = getPartnerFac().partnerkommFindRespectPartnerAsStringOhneExec(ansprechpartnerIId,
				kundeDto.getPartnerDto(), PartnerFac.KOMMUNIKATIONSART_EMAIL, theClientDto.getMandant(), theClientDto);
		String sFax = getPartnerFac().partnerkommFindRespectPartnerAsStringOhneExec(ansprechpartnerIId,
				kundeDto.getPartnerDto(), PartnerFac.KOMMUNIKATIONSART_FAX, theClientDto.getMandant(), theClientDto);
		String sTelefon = getPartnerFac().partnerkommFindRespectPartnerAsStringOhneExec(ansprechpartnerIId,
				kundeDto.getPartnerDto(), PartnerFac.KOMMUNIKATIONSART_TELEFON, theClientDto.getMandant(),
				theClientDto);
		// belegkommunikation: 3 daten als parameter an den Report
		// weitergeben
		mapParameter.put(LPReport.P_ANSPRECHPARTNEREMAIL, sEmail != null ? sEmail : "");
		mapParameter.put(LPReport.P_ANSPRECHPARTNERFAX, sFax != null ? sFax : "");
		mapParameter.put(LPReport.P_ANSPRECHPARTNERTELEFON, sTelefon != null ? sTelefon : "");

		MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
		mapParameter.put(LPReport.P_MANDANTADRESSE, Helper.formatMandantAdresse(mandantDto));
		// SP5517 Ansprechpartner anhand des Parameters
		// RECHNUNG_ANSPRECHPARTNER_ANDRUCKEN andrucken
		mapParameter.put("P_KUNDE_ADRESSBLOCK", formatAdresseFuerAusdruck(kundeDto.getPartnerDto(), oAnsprechpartner,
				mandantDto, locDruck, LocaleFac.BELEGART_RECHNUNG));
		mapParameter.put("P_KUNDE_UID", kundeDto.getPartnerDto().getCUid());

		KontoDto k = null;
		if (kundeDto.getIidDebitorenkonto() != null) {
			k = getFinanzFac().kontoFindByPrimaryKey(kundeDto.getIidDebitorenkonto());
			mapParameter.put("P_KUNDE_DEBITORENNUMMER", k.getCNr());
		}

		mapParameter.put("P_KUNDE_NUMMER", kundeDto.getIKundennummer());

		mapParameter.put("P_KUNDE_HINWEISEXTERN", kundeDto.getSHinweisextern());
		mapParameter.put("P_KUNDE_LIEFERANTENNR", kundeDto.getCLieferantennr());
		mapParameter.put("P_KUNDE_EORI", kundeDto.getPartnerDto().getCEori());

		PersonalDto oPersonalBenutzer = getPersonalFac().personalFindByPrimaryKey(theClientDto.getIDPersonal(),
				theClientDto);
		PersonalDto oPersonalAnleger = getPersonalFac().personalFindByPrimaryKey(rechnungDto.getPersonalIIdAnlegen(),
				theClientDto);
		mapParameter.put("P_UNSER_ZEICHEN",
				Helper.getKurzzeichenkombi(oPersonalBenutzer.getCKurzzeichen(), oPersonalAnleger.getCKurzzeichen()));
		mapParameter.put("P_DATUM", mahnungDto.getTMahndatum());
		MahntextDto mahntextDto = getFinanzServiceFac().mahntextFindByMandantLocaleCNr(mandantDto.getCNr(),
				Helper.locale2String(locDruck), mahnstufeIId);

		String sPMahnstufe;
		if (mahnstufeIId.intValue() == FinanzServiceFac.MAHNSTUFE_99) {
			sPMahnstufe = getTextRespectUISpr("lp.letzte", theClientDto.getMandant(), locDruck);
		} else {
			sPMahnstufe = mahnstufeIId.toString() + ".";
		}

		mapParameter.put("P_MAHNSTUFE", sPMahnstufe);

		if (mahntextDto != null) {
			mapParameter.put("P_MAHNTEXT", Helper.formatStyledTextForJasper(mahntextDto.getCTextinhalt()));
		} else {
			EJBExceptionLP e = new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_KEINE_MAHNTEXTE_EINGETRAGEN, "");
			ArrayList<Object> a = new ArrayList<Object>();
			a.add(mandantDto.getCNr() + "|" + Helper.locale2String(locDruck) + "|" + mahnstufeIId);
			e.setAlInfoForTheClient(a);
			throw e;
		}
		// Vertreter
		if (rechnungDto.getPersonalIIdVertreter() != null) {
			String cVertreterAnredeShort = getPersonalFac()
					.personalFindByPrimaryKey(rechnungDto.getPersonalIIdVertreter(), theClientDto).getPartnerDto()
					.formatFixName2Name1();
			mapParameter.put(P_VERTRETER, cVertreterAnredeShort);
		}

		mapParameter.put("P_RECHNUNGSNUMMER", rechnungDto.getCNr());
		mapParameter.put("P_RECHNUNGSDATUM", rechnungDto.getTBelegdatum());
		mapParameter.put("P_WERT", rechnungDto.getNWert().add(rechnungDto.getNWertust()));

		BigDecimal bdBetrag = rechnungDto.getNWertfw().add(rechnungDto.getNWertustfw())
				.subtract(getRechnungFac().getBereitsBezahltWertVonRechnungFw(rechnungDto.getIId(), null))
				.subtract(getRechnungFac().getBereitsBezahltWertVonRechnungUstFw(rechnungDto.getIId(), null));

		// SP839
		BigDecimal bdUstAnzahlungFW = new BigDecimal(0);
		BigDecimal bdNettoAnzahlungFW = new BigDecimal(0);
		if (rechnungDto.getAuftragIId() != null
				&& rechnungDto.getRechnungartCNr().equals(RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG)) {
			bdNettoAnzahlungFW = getRechnungFac().getAnzahlungenZuSchlussrechnungFw(rechnungDto.getIId());
			bdUstAnzahlungFW = getRechnungFac().getAnzahlungenZuSchlussrechnungUstFw(rechnungDto.getIId());

			bdBetrag = bdBetrag.subtract(bdUstAnzahlungFW).subtract(bdNettoAnzahlungFW);

		}

		if (rechnungDto.getRechnungartCNr().equals(RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
			bdBetrag = bdBetrag.negate();
		}

		mapParameter.put("P_BETRAG", bdBetrag);
		BigDecimal bdSumme = bdBetrag;

		MahnstufeDto mahnstufeDto = getMahnwesenFac()
				.mahnstufeFindByPrimaryKey(new MahnstufePK(mahnstufeIId, theClientDto.getMandant()));
		// Zieldatum berechnen
		Date dZieldatum = rechnungDto.getTBelegdatum();
		if (rechnungDto.getZahlungszielIId() != null) {
			ZahlungszielDto zzDto = getMandantFac().zahlungszielFindByPrimaryKey(rechnungDto.getZahlungszielIId(),
					theClientDto);
			if (zzDto.getAnzahlZieltageFuerNetto() == null) {
				zzDto.setAnzahlZieltageFuerNetto(new Integer(0));
			}
			dZieldatum = Helper.addiereTageZuDatum(dZieldatum, zzDto.getAnzahlZieltageFuerNetto().intValue());
		}

		// Zinsen
		if (mahnstufeDto.getFZinssatz() != null) {
			BigDecimal bdZinssatz = new BigDecimal(mahnstufeDto.getFZinssatz());
			mapParameter.put("P_ZINSSATZ", bdZinssatz);
			BigDecimal bdZinsenFuerEinJahr = bdBetrag.multiply(bdZinssatz.movePointLeft(2));
			BigDecimal bdDays = new BigDecimal(Helper.getDifferenzInTagen(dZieldatum, mahnungDto.getTMahndatum()));
			BigDecimal bdZinsenFuerEinenTag = bdZinsenFuerEinJahr.divide(new BigDecimal(360.0), 4,
					BigDecimal.ROUND_HALF_EVEN);
			BigDecimal bdZinsen = bdZinsenFuerEinenTag.multiply(bdDays);
			bdZinsen = Helper.rundeKaufmaennisch(bdZinsen, FinanzFac.NACHKOMMASTELLEN);
			mapParameter.put("P_ZINSEN", bdZinsen);
			bdSumme = bdSumme.add(bdZinsen);
		}
		// Mahnspesen

		BigDecimal bdSpesenFw = getMahnspesen(mahnstufeDto, mahnungDto.getTMahndatum(), theClientDto,
				rechnungDto.getWaehrungCNr());

		if (bdSpesenFw != null) {

			mapParameter.put("P_MAHNSPESEN", bdSpesenFw);
			bdSumme = bdSumme.add(bdSpesenFw);
		}

		// Summe
		mapParameter.put("P_SUMME", bdSumme);
		mapParameter.put("P_ZIELDATUM", dZieldatum);
		// Zahlungsfrist = heute + Tage lt. Mandantenparameter
		ParametermandantDto p = getParameterFac().getMandantparameter(theClientDto.getMandant(),
				ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_FINANZ_MAHNUNG_ZAHLUNGSFRIST);
		Integer iTage = new Integer(p.getCWert());
		Date dZahldatum = Helper.addiereTageZuDatum(Helper.cutDate(getDate()), iTage);
		mapParameter.put("P_ZAHLDATUM", dZahldatum);

		mapParameter.put("P_BERUECKSICHTIGTBIS", new java.sql.Date(System.currentTimeMillis())); // heute

		// mapParameter.put(LPReport.P_WAEHRUNG, mandantDto.getWaehrungCNr());
		mapParameter.put(LPReport.P_WAEHRUNG, rechnungDto.getWaehrungCNr());

		mapParameter.put("P_ABSENDER", mandantDto.getPartnerDto().formatAnrede());
		mapParameter.put("P_BENUTZERNAME", oPersonalBenutzer.getPartnerDto().formatAnrede());
		return mapParameter;
	}

	public JasperPrintLP printMahnungFuerRechnung(TheClientDto theClientDto, Integer rechnungIId, Integer mahnstufeIId,
			Boolean bMitLogo) throws EJBExceptionLP {
		try {
			// fuer Mahnstufe 99 ist das ein RA-Schreiben!
			if (mahnstufeIId.intValue() == FinanzServiceFac.MAHNSTUFE_99) {
				return printRASchreibenFuerRechnung(theClientDto, rechnungIId);
			}
			// sonst ists eine "normale" Mahnung
			else {
				this.useCase = UC_MAHNUNG;
				data = new Object[1][1]; // brauch ich hier nicht
				index = -1;
				RechnungDto rechnungDto = getRechnungFac().rechnungFindByPrimaryKey(rechnungIId);
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(rechnungDto.getKundeIId(), theClientDto);
				Locale locDruck = Helper.string2Locale(kundeDto.getPartnerDto().getLocaleCNrKommunikation());
				Map<String, Object> mapParameter = getMahnungsParameterFuerRechnung(theClientDto, rechnungIId,
						mahnstufeIId);
				initJRDS(mapParameter, FinanzReportFac.REPORT_MODUL, FinanzReportFac.REPORT_MAHNUNG,
						theClientDto.getMandant(), locDruck, theClientDto, bMitLogo, rechnungDto.getKostenstelleIId());
				return getReportPrint();
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	public JasperPrintLP printEinfacheErfolgsrechnung(TheClientDto theClientDto, Integer iGeschaeftsjahr,
			boolean bMitLagerwert, boolean bMitHalbfertiglager, boolean bMitPersonalstunden,
			boolean bMitMaschinenstunden) {
		Locale locUI = theClientDto.getLocUi();
		// Monatsnamen localeabhaengig mit Calendar formatieren
		SimpleDateFormat dateformat = new SimpleDateFormat("MMMM", locUI);
		Calendar cal = GregorianCalendar.getInstance(locUI);
		cal.set(Calendar.YEAR, GregorianCalendar.JANUARY, 1);

		this.useCase = UC_EINFACHE_ERFOLGSRECHNUNG;
		this.index = -1;

		try {

			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			mapParameter.put("P_GESCHAEFTSJAHR", iGeschaeftsjahr);
			mapParameter.put("P_MIT_LAGERWERT", bMitLagerwert);
			mapParameter.put("P_MIT_HFLAGER", bMitHalbfertiglager);
			mapParameter.put("P_MIT_PERSONALSTUNDEN", bMitPersonalstunden);
			mapParameter.put("P_MIT_MASCHINENSTUNDEN", bMitMaschinenstunden);

			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
			mapParameter.put(LPReport.P_WAEHRUNG, mandantDto.getWaehrungCNr());

			String sKriterium = RechnungFac.KRIT_MIT_GUTSCHRIFTEN;

			ArrayList alDaten = new ArrayList();

			java.sql.Timestamp[] tVonBis = getBuchenFac().getDatumVonBisGeschaeftsjahr(iGeschaeftsjahr, theClientDto);

			int iTage = Helper.ermittleTageEinesZeitraumes(tVonBis[0], new java.sql.Date(tVonBis[1].getTime()));

			// Wiederholende Zusatzkosten in der Zukunft

			ArrayList<EingangsrechnungDto> nochNichtAngelegteWiederholendeEingangsrechnungen = new ArrayList();

			Session session = FLRSessionFactory.getFactory().openSession();

			String sQuery = "SELECT r FROM FLREingangsrechnung r WHERE r.eingangsrechnungart_c_nr='"
					+ EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN + "' AND r.status_c_nr NOT IN('"
					+ EingangsrechnungFac.STATUS_STORNIERT + "')  AND r.mandant_c_nr='" + theClientDto.getMandant()
					+ "' AND r.auftragwiederholungsintervall_c_nr is not null AND r.eingangsrechnung_i_id_nachfolger is null AND r.t_wiederholenderledigt is null ORDER BY r.c_nr";

			Query query = session.createQuery(sQuery);

			List results = query.list();
			Iterator resultListIterator = results.iterator();
			while (resultListIterator.hasNext()) {

				FLREingangsrechnung flrEingangsrechnungReport = (FLREingangsrechnung) resultListIterator.next();

				Calendar cBeginn = Calendar.getInstance();
				cBeginn.setTimeInMillis(flrEingangsrechnungReport.getT_belegdatum().getTime());

				while (cBeginn.getTime().before(tVonBis[1])) {

					cBeginn.setTimeInMillis(getRechnungFac()
							.berechneinterval(flrEingangsrechnungReport.getAuftragwiederholungsintervall_c_nr(),
									new java.sql.Date(cBeginn.getTimeInMillis()))
							.getTime());

					EingangsrechnungDto erDtoToUse = getEingangsrechnungFac()
							.eingangsrechnungFindByPrimaryKey(flrEingangsrechnungReport.getI_id());
					erDtoToUse.setDBelegdatum(new java.sql.Date(cBeginn.getTimeInMillis()));

					nochNichtAngelegteWiederholendeEingangsrechnungen.add(erDtoToUse);

				}

			}

			session.close();

			ArrayList<Object> alPersonalkosten = getPersonaldkostenFuerEinfacheErfolgsrechnung(theClientDto, tVonBis[0],
					tVonBis[1]);

			GregorianCalendar cAktuell = new GregorianCalendar();
			cAktuell.setTime(tVonBis[0]);
			while (cAktuell.getTime().before(tVonBis[1])) {

				GregorianCalendar gcEnde = new GregorianCalendar();
				gcEnde.setTime(cAktuell.getTime());
				gcEnde.add(GregorianCalendar.MONTH, 1);

				GregorianCalendar gcTemp = new GregorianCalendar();
				gcTemp.setTimeInMillis(cAktuell.getTimeInMillis());

				Object[] oZeileREumsatz = getRechnungReportFac().getZeileAnhandJahrMonat(theClientDto,
						gcTemp.get(Calendar.YEAR), locUI, dateformat, sKriterium, false, false, false,
						gcTemp.get(Calendar.MONTH));

				Object[] oZeile = new Object[REPORT_EINFACHE_ERFOLGSRECHNUNG_ANZAHL_SPALTEN];
				oZeile[REPORT_EINFACHE_ERFOLGSRECHNUNG_MONAT] = oZeileREumsatz[RechnungReportFac.UMSATZ_FELD_MONAT];
				oZeile[REPORT_EINFACHE_ERFOLGSRECHNUNG_NETTOUMSATZ_RE] = oZeileREumsatz[RechnungReportFac.UMSATZ_FELD_WERT];

				BigDecimal bdER = getEingangsrechnungFac().berechneSummeUmsatzNetto(theClientDto, null,
						EingangsrechnungFac.KRIT_DATUM_BELEGDATUM, theClientDto.getSMandantenwaehrung(), gcTemp, gcEnde,
						false);

				oZeile[REPORT_EINFACHE_ERFOLGSRECHNUNG_NETTOUMSATZ_ER] = bdER;

				BigDecimal bdZK = getEingangsrechnungFac().berechneSummeUmsatzNetto(theClientDto, null,
						EingangsrechnungFac.KRIT_DATUM_BELEGDATUM, theClientDto.getSMandantenwaehrung(), gcTemp, gcEnde,
						true);

				for (int i = 0; i < nochNichtAngelegteWiederholendeEingangsrechnungen.size(); i++) {
					EingangsrechnungDto erDtoWiederholendUndNochNichtAngelegt = nochNichtAngelegteWiederholendeEingangsrechnungen
							.get(i);
					Calendar cTemp = Calendar.getInstance();
					cTemp.setTimeInMillis(erDtoWiederholendUndNochNichtAngelegt.getDBelegdatum().getTime());

					if (cTemp.get(Calendar.YEAR) == gcTemp.get(Calendar.YEAR)
							&& cTemp.get(Calendar.MONTH) == gcTemp.get(Calendar.MONTH)) {
						bdZK = bdZK.add(erDtoWiederholendUndNochNichtAngelegt.getNBetrag()
								.subtract(erDtoWiederholendUndNochNichtAngelegt.getNUstBetrag()));
					}

				}

				oZeile[REPORT_EINFACHE_ERFOLGSRECHNUNG_NETTOUMSATZ_ZK] = bdZK;

				// Personakosten
				BigDecimal bdPersonalkosten = BigDecimal.ZERO;

				for (int i = 0; i < alPersonalkosten.size(); i++) {

					Object[] oZeilePK = (Object[]) alPersonalkosten.get(i);
					java.sql.Date dDatum = (java.sql.Date) oZeilePK[REPORT_LV_DATUM];

					Calendar cTemp = Calendar.getInstance();
					cTemp.setTimeInMillis(dDatum.getTime());

					if (cTemp.get(Calendar.YEAR) == gcTemp.get(Calendar.YEAR)
							&& cTemp.get(Calendar.MONTH) == gcTemp.get(Calendar.MONTH)) {
						if (oZeilePK[REPORT_LV_NETTO] != null) {
							bdPersonalkosten = bdPersonalkosten.add((BigDecimal) oZeilePK[REPORT_LV_NETTO]);
						}
					}

				}

				oZeile[REPORT_EINFACHE_ERFOLGSRECHNUNG_PERSONALKOSTEN] = bdPersonalkosten;

				// Gestehungswert des Lagers zum Zeitpunkt

				GregorianCalendar gcErsterDesMonats = new GregorianCalendar();
				gcErsterDesMonats.setTimeInMillis(gcTemp.getTimeInMillis());
				gcErsterDesMonats.set(Calendar.DAY_OF_MONTH, 1);

				GregorianCalendar gcLetzterDesMonats = new GregorianCalendar();
				gcLetzterDesMonats.setTimeInMillis(gcTemp.getTimeInMillis());
				gcLetzterDesMonats.set(Calendar.DAY_OF_MONTH, gcTemp.getActualMaximum(Calendar.DAY_OF_MONTH));

				if (new Date().after(cAktuell.getTime())) {

					if (bMitLagerwert) {

						BigDecimal gestehungswert = BigDecimal.ZERO;

						session = FLRSessionFactory.getFactory().openSession();

						sQuery = "SELECT a FROM FLRArtikel a WHERE a.b_lagerbewirtschaftet=1 ORDER BY a.c_nr";

						query = session.createQuery(sQuery);

						results = query.list();
						resultListIterator = results.iterator();
						while (resultListIterator.hasNext()) {
							FLRArtikel flrArtikel = (FLRArtikel) resultListIterator.next();

							BigDecimal bdLagerstand = getLagerFac().getLagerstandZumZeitpunkt(flrArtikel.getI_id(),
									null, new Timestamp(gcLetzterDesMonats.getTimeInMillis()), theClientDto);

							BigDecimal bdGestpreis = getLagerFac().getGestehungspreisZumZeitpunkt(flrArtikel.getI_id(),
									null, new Timestamp(gcLetzterDesMonats.getTimeInMillis()), theClientDto);
							if (bdGestpreis != null && bdLagerstand != null) {
								gestehungswert = gestehungswert.add(bdLagerstand.multiply(bdGestpreis));
							}

						}

						oZeile[REPORT_EINFACHE_ERFOLGSRECHNUNG_LAGER_GESTEHUNGSWERT_ZUM_MONATSLETZTEN] = gestehungswert;
						session.close();
					}

					// HF-Invenur
					if (bMitHalbfertiglager) {

						BigDecimal gestehungswertMaterial = BigDecimal.ZERO;
						BigDecimal gestehungswertArbeitszeit = BigDecimal.ZERO;

						Object[][] dataHFInventur = getFertigungReportFac().getDataHalbfertigfabrikatsinventur(
								new Timestamp(gcLetzterDesMonats.getTimeInMillis()),
								FertigungReportFac.HF_OPTION_SORTIERUNG_ARTIKELNR, false, null, false, false, false,
								theClientDto);

						for (int i = 0; i < dataHFInventur.length; i++) {
							Object[] oZeileHF = dataHFInventur[i];

							BigDecimal bdPreis = (BigDecimal) oZeileHF[FertigungReportFac.HF_POSITION_PREIS];
							BigDecimal bdOffen = (BigDecimal) oZeileHF[FertigungReportFac.HF_POSITION_OFFEN];

							if (bdPreis != null && bdOffen != null) {
								BigDecimal bdWert = bdPreis.multiply(bdOffen);

								if (oZeileHF[FertigungReportFac.HF_POSITION_ARTIKELVERWENDUNG] != null
										&& oZeileHF[FertigungReportFac.HF_POSITION_ARTIKELVERWENDUNG]
												.equals("Arbeitszeit")) {
									gestehungswertArbeitszeit = gestehungswertArbeitszeit.add(bdWert);
								} else {
									gestehungswertMaterial = gestehungswertMaterial.add(bdWert);
								}

							}

						}

						oZeile[REPORT_EINFACHE_ERFOLGSRECHNUNG_HF_GESTEHUNGSWERT_ARBEIT_ZUM_MONATSLETZTEN] = gestehungswertArbeitszeit;
						oZeile[REPORT_EINFACHE_ERFOLGSRECHNUNG_HF_GESTEHUNGSWERT_MATERIAL_ZUM_MONATSLETZTEN] = gestehungswertMaterial;
					}
				}

				if (bMitPersonalstunden) {
					// Personalzeiten
					JasperPrintLP print = getZeiterfassungReportFac().printMitarbeiteruebersicht(null,
							cAktuell.get(Calendar.YEAR), cAktuell.get(Calendar.MONTH), cAktuell.get(Calendar.YEAR),
							cAktuell.get(Calendar.MONTH),
							ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_PERSONEN, null,
							ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_PERSONALNUMMER, true, true,
							true, false, theClientDto);

					Object[][] daten = print.getDatenMitUeberschrift();

					if (daten.length > 0) {

						int spalteSummeIstZeit = 0;

						for (int i = 0; i < daten[0].length; i++) {

							String ueberschrift = (String) daten[0][i];

							if (ueberschrift.equals("Summeistzeit")) {
								spalteSummeIstZeit = i;
							}

						}

						BigDecimal bdsummeISTGesamt = BigDecimal.ZERO;

						for (int i = 1; i < daten.length; i++) {

							BigDecimal bdsummeIST = (BigDecimal) daten[i][spalteSummeIstZeit];

							if (bdsummeIST != null) {
								bdsummeISTGesamt = bdsummeISTGesamt.add(bdsummeIST);
							}

						}

						oZeile[REPORT_EINFACHE_ERFOLGSRECHNUNG_PERSONALSTUNDEN] = bdsummeISTGesamt;

					}
				}

				if (bMitMaschinenstunden) {
					oZeile[REPORT_EINFACHE_ERFOLGSRECHNUNG_MASCHINENSTUNDEN] = getZeiterfassungFac()
							.getSummeMaschinenZeitenEinesBeleges(null, null,
									new Timestamp(gcErsterDesMonats.getTimeInMillis()),
									new Timestamp(gcLetzterDesMonats.getTimeInMillis()), theClientDto);
				}

				alDaten.add(oZeile);

				cAktuell.add(Calendar.MONTH, 1);
			}

			Object[][] returnArray = new Object[alDaten.size()][REPORT_EINFACHE_ERFOLGSRECHNUNG_ANZAHL_SPALTEN];
			data = (Object[][]) alDaten.toArray(returnArray);

			initJRDS(mapParameter, FinanzReportFac.REPORT_MODUL, FinanzReportFac.REPORT_EINFACHE_ERFOLGSRECHNUNG,
					theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
			return getReportPrint();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	public JasperPrintLP[] printSammelmahnung(Integer mahnlaufIId, Boolean bMitLogo, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria crit = session.createCriteria(FLRFinanzMahnung.class);
			crit.add(Restrictions.eq(FinanzFac.FLR_MAHNUNG_MAHNLAUF_I_ID, mahnlaufIId));
			crit.add(Restrictions.isNull(FinanzFac.FLR_MAHNUNG_T_GEDRUCKT));
			List<?> list = crit.list();
			HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
			// alle kundenId's in eine hashmap -> quasi distinct
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLRFinanzMahnung item = (FLRFinanzMahnung) iter.next();
				if (!item.getFlrrechnungreport().getStatus_c_nr().equals(RechnungFac.STATUS_STORNIERT)) {
					hm.put(item.getFlrrechnungreport().getFlrkunde().getI_id(),
							item.getFlrrechnungreport().getFlrkunde().getI_id());
				}
			}
			// Reports generieren
			Collection<JasperPrintLP> c = new LinkedList<JasperPrintLP>();
			for (Iterator<Integer> iter = hm.keySet().iterator(); iter.hasNext();) {
				Integer kundeIId = (Integer) iter.next();
				JasperPrintLP print = printSammelmahnung(mahnlaufIId, kundeIId, bMitLogo, theClientDto);

				if (print != null) {
					// Kunden-ID fuer den Client setzen.
					print.putAdditionalInformation(JasperPrintLP.KEY_KUNDE_I_ID, kundeIId);
					c.add(print);
				}
			}
			JasperPrintLP[] prints = new JasperPrintLP[c.size()];
			int i = 0;
			for (Iterator<JasperPrintLP> iter = c.iterator(); iter.hasNext(); i++) {
				JasperPrintLP item = iter.next();
				prints[i] = item;
			}
			return prints;
		} finally {
			closeSession(session);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAenderungenKonto(Integer kontoIId, TheClientDto theClientDto) {

		this.useCase = UC_AENDERUNGEN_KONTO;

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		KontoDto kontoDto = null;
		try {
			kontoDto = getFinanzFac().kontoFindByPrimaryKey(kontoIId);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		parameter.put("P_KONTO", kontoDto.getKontonrBezeichnung());

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session.createCriteria(FLREntitylog.class);

		crit.add(Restrictions.eq("c_filter_key", HvDtoLogClass.KONTO));
		crit.add(Restrictions.eq("filter_i_id", kontoIId + ""));

		crit.addOrder(Order.desc("t_aendern"));
		List<?> results = crit.list();
		Iterator<?> resultListIterator = results.iterator();

		ArrayList alDaten = new ArrayList();

		while (resultListIterator.hasNext()) {
			FLREntitylog flrEntitylog = (FLREntitylog) resultListIterator.next();

			Object[] zeile = new Object[REPORT_AENDERUNGEN_KONTO_ANZAHL_SPALTEN];

			zeile[REPORT_AENDERUNGEN_KONTO_FELDNAME] = flrEntitylog.getC_key();

			zeile[REPORT_AENDERUNGEN_KONTO_GEAENDERT_NACH] = flrEntitylog.getC_nach();
			zeile[REPORT_AENDERUNGEN_KONTO_OPERATION] = flrEntitylog.getC_operation();
			zeile[REPORT_AENDERUNGEN_KONTO_GEAENDERT_DURCH] = HelperServer
					.formatPersonAusFLRPartner(flrEntitylog.getFlrpersonal().getFlrpartner());

			zeile[REPORT_AENDERUNGEN_KONTO_GEAENDERT_VON] = flrEntitylog.getC_von();
			zeile[REPORT_AENDERUNGEN_KONTO_AENDERUNGSZEITPUNKT] = flrEntitylog.getT_aendern();

			alDaten.add(zeile);
		}

		Object[][] dataTemp = new Object[alDaten.size()][REPORT_AENDERUNGEN_KONTO_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(dataTemp);

		initJRDS(parameter, FinanzReportFac.REPORT_MODUL, FinanzReportFac.REPORT_AENDERUNGEN_KONTO,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();

	}

	/**
	 * Alle Mahnungen eines Mahnlaufes in einen Report zusammenfassen.
	 * 
	 * @param mahnlaufIId  Integer
	 * @param kundeIId     Integer
	 * @param theClientDto String
	 * @return JasperPrint
	 * @throws EJBExceptionLP
	 */
	private JasperPrintLP printSammelmahnung(Integer mahnlaufIId, Integer kundeIId, Boolean bMitLogo,
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			this.useCase = UC_SAMMELMAHNUNG;
			index = -1;
			MahnungDto[] mahnungen = getMahnwesenFac().mahnungFindByMahnlaufIId(mahnlaufIId);
			String sRechnungen = "";
			Collection<SammelmahnungDto> c = new LinkedList<SammelmahnungDto>();
			Integer iMaxMahnstufe = null;

			// PJ18939
			ParametermandantDto p = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_RECHNUNG, ParameterFac.PARAMETER_MINDEST_MAHNBETRAG);
			Double dMindestmahnbetrag = (Double) p.getCWertAsObject();

			// SP1104 Sammelmahnung -> Ansprechpartner Tel/Fax/Email aus der
			// 1.Rechnung verwenden
			Integer ansprechpartnerIIdErsteRechnung = null;

			for (int i = 0; i < mahnungen.length; i++) {
				RechnungDto rechnungDto = getRechnungFac().rechnungFindByPrimaryKey(mahnungen[i].getRechnungIId());
				// Stornierte werden nicht mitgedruckt.
				if (!rechnungDto.getStatusCNr().equals(RechnungFac.STATUS_STORNIERT)) {
					Integer kundeIIdMahnung = rechnungDto.getKundeIId();
					if (kundeIIdMahnung.equals(kundeIId)) {
						if (iMaxMahnstufe == null) {
							iMaxMahnstufe = mahnungen[i].getMahnstufeIId();
						} else {
							if (mahnungen[i].getMahnstufeIId().intValue() > iMaxMahnstufe.intValue()) {
								iMaxMahnstufe = mahnungen[i].getMahnstufeIId();
							}
						}
						SammelmahnungDto smDto = new SammelmahnungDto();

						smDto.setBelegwaehrung(rechnungDto.getWaehrungCNr());
						BigDecimal bdOffen = rechnungDto.getNWertfw().add(rechnungDto.getNWertustfw())
								.subtract(
										getRechnungFac().getBereitsBezahltWertVonRechnungFw(rechnungDto.getIId(), null))
								.subtract(getRechnungFac().getBereitsBezahltWertVonRechnungUstFw(rechnungDto.getIId(),
										null));

						BigDecimal bdOffenBelegwaehrung = rechnungDto.getNWert().add(rechnungDto.getNWertust())
								.subtract(getRechnungFac().getBereitsBezahltWertVonRechnung(rechnungDto.getIId(), null))
								.subtract(getRechnungFac().getBereitsBezahltWertVonRechnungUst(rechnungDto.getIId(),
										null));

						// SP839
						BigDecimal bdUstAnzahlungFW = new BigDecimal(0);
						BigDecimal bdNettoAnzahlungFW = new BigDecimal(0);

						BigDecimal bdUstAnzahlungBelegwaehrung = new BigDecimal(0);
						BigDecimal bdNettoAnzahlungBelegwaehrung = new BigDecimal(0);

						if (rechnungDto.getAuftragIId() != null
								&& rechnungDto.getRechnungartCNr().equals(RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG)) {
							bdNettoAnzahlungFW = getRechnungFac()
									.getAnzahlungenZuSchlussrechnungFw(rechnungDto.getIId());
							bdUstAnzahlungFW = getRechnungFac()
									.getAnzahlungenZuSchlussrechnungUstFw(rechnungDto.getIId());

							bdOffen = bdOffen.subtract(bdUstAnzahlungFW).subtract(bdNettoAnzahlungFW);

							// Belegwaehrung

							bdNettoAnzahlungBelegwaehrung = getRechnungFac()
									.getAnzahlungenZuSchlussrechnung(rechnungDto.getIId());
							bdUstAnzahlungBelegwaehrung = getRechnungFac()
									.getAnzahlungenZuSchlussrechnungUst(rechnungDto.getIId());

							bdOffenBelegwaehrung = bdOffenBelegwaehrung.subtract(bdUstAnzahlungBelegwaehrung)
									.subtract(bdNettoAnzahlungBelegwaehrung);

						}

						String rechnungtyp = getRechnungServiceFac()
								.rechnungartFindByPrimaryKey(rechnungDto.getRechnungartCNr(), theClientDto)
								.getRechnungtypCNr();

						if (rechnungtyp.equals(RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
							smDto.setBdOffen(bdOffen.negate());
							smDto.setBdWert(rechnungDto.getNWertfw().add(rechnungDto.getNWertustfw()).negate());
							smDto.setBdZinsen(new BigDecimal(0));

							smDto.setBdOffenBelegWaehrung(bdOffenBelegwaehrung.negate());
							smDto.setBdWertBelegwaehrung(
									rechnungDto.getNWert().add(rechnungDto.getNWertust()).negate());

						} else {
							smDto.setBdOffen(bdOffen);
							smDto.setBdWert(rechnungDto.getNWertfw().add(rechnungDto.getNWertustfw()));

							smDto.setBdOffenBelegWaehrung(bdOffenBelegwaehrung);
							smDto.setBdWertBelegwaehrung(rechnungDto.getNWert().add(rechnungDto.getNWertust()));

							MahnstufeDto mahnstufeDto = getMahnwesenFac().mahnstufeFindByPrimaryKey(
									new MahnstufePK(mahnungen[i].getMahnstufeIId(), theClientDto.getMandant()));

							Date dZieldatum = rechnungDto.getTBelegdatum();
							if (rechnungDto.getZahlungszielIId() != null) {
								ZahlungszielDto zzDto = getMandantFac()
										.zahlungszielFindByPrimaryKey(rechnungDto.getZahlungszielIId(), theClientDto);
								if (zzDto.getAnzahlZieltageFuerNetto() == null) {
									zzDto.setAnzahlZieltageFuerNetto(new Integer(0));
								}
								dZieldatum = Helper.addiereTageZuDatum(dZieldatum,
										zzDto.getAnzahlZieltageFuerNetto().intValue());
							}

							if (mahnstufeDto.getFZinssatz() != null) {
								BigDecimal bdZinssatz = new BigDecimal(mahnstufeDto.getFZinssatz());
								BigDecimal bdZinsenFuerEinJahr = bdOffen.multiply(bdZinssatz.movePointLeft(2));
								BigDecimal bdDays = new BigDecimal(
										Helper.getDifferenzInTagen(dZieldatum, mahnungen[i].getTMahndatum()));
								BigDecimal bdZinsenFuerEinenTag = bdZinsenFuerEinJahr.divide(new BigDecimal(360.0), 4,
										BigDecimal.ROUND_HALF_EVEN);
								BigDecimal bdZinsen = bdZinsenFuerEinenTag.multiply(bdDays);
								bdZinsen = Helper.rundeKaufmaennisch(bdZinsen, FinanzFac.NACHKOMMASTELLEN);
								smDto.setBdZinsen(bdZinsen);

							} else {
								smDto.setBdZinsen(new BigDecimal(0));
							}
						}

						smDto.setDBelegdatum(rechnungDto.getTBelegdatum());
						Date dZieldatum = rechnungDto.getTBelegdatum();
						if (rechnungDto.getZahlungszielIId() != null) {
							ZahlungszielDto zzDto = getMandantFac()
									.zahlungszielFindByPrimaryKey(rechnungDto.getZahlungszielIId(), theClientDto);
							if (zzDto.getAnzahlZieltageFuerNetto() == null) {
								EJBExceptionLP ex = new EJBExceptionLP(
										EJBExceptionLP.FEHLER_RECHNUNG_ZAHLUNGSZIEL_KEINE_TAGE, "");
								ArrayList<Object> al = new ArrayList<Object>();
								al.add(zzDto.getCBez());
								al.add(rechnungDto.getCNr());
								ex.setAlInfoForTheClient(al);
								throw ex;
							}
							dZieldatum = Helper.addiereTageZuDatum(dZieldatum,
									zzDto.getAnzahlZieltageFuerNetto().intValue());
						}
						smDto.setDFaelligkeitsdatum(dZieldatum);
						smDto.setIMahnstufe(mahnungen[i].getMahnstufeIId());
						smDto.setSRechnungsnummer(
								rechnungDto.getRechnungartCNr().substring(0, 1) + rechnungDto.getCNr());
						c.add(smDto);

						AnsprechpartnerDto oAnsprechpartner = null;
						if (rechnungDto.getAnsprechpartnerIId() != null) {
							oAnsprechpartner = getAnsprechpartnerFac()
									.ansprechpartnerFindByPrimaryKey(rechnungDto.getAnsprechpartnerIId(), theClientDto);
							ansprechpartnerIIdErsteRechnung = oAnsprechpartner.getIId();
						}
					}
				}
			}

			data = new Object[c.size()][10];
			int i = 0;

			BigDecimal bdOffenGesamt = BigDecimal.ZERO;

			String letzteBelegwaehrung = null;

			for (Iterator<SammelmahnungDto> iter = c.iterator(); iter.hasNext();) {
				SammelmahnungDto item = iter.next();

				data[i][REPORT_SAMMELMAHNUNG_BELEGWAEHRUNG] = item.getBelegwaehrung();

				letzteBelegwaehrung = item.getBelegwaehrung();

				data[i][REPORT_SAMMELMAHNUNG_OFFEN] = item.getBdOffen();
				data[i][REPORT_SAMMELMAHNUNG_OFFEN_BELEGWAEHRUNG] = item.getBdOffenBelegWaehrung();

				if (item.getBdWert() != null) {
					bdOffenGesamt = bdOffenGesamt.add(item.getBdOffen());
				}

				data[i][REPORT_SAMMELMAHNUNG_WERT] = item.getBdWert();
				data[i][REPORT_SAMMELMAHNUNG_WERT_BELEGWAEHRUNG] = item.getBdWertBelegwaehrung();
				data[i][REPORT_SAMMELMAHNUNG_ZINSEN] = item.getBdZinsen();
				data[i][REPORT_SAMMELMAHNUNG_BELEGDATUM] = item.getDBelegdatum();
				data[i][REPORT_SAMMELMAHNUNG_FAELLIGKEITSDATUM] = item.getDFaelligkeitsdatum();
				data[i][REPORT_SAMMELMAHNUNG_MAHNSTUFE] = item.getIMahnstufe();
				data[i][REPORT_SAMMELMAHNUNG_RECHNUNGSNUMMER] = item.getSRechnungsnummer();
				if (sRechnungen.length() > 0) {
					sRechnungen = sRechnungen + ", " + item.getSRechnungsnummer();
				} else {
					sRechnungen = item.getSRechnungsnummer();
				}
				i++;
			}

			// PJ18939
			if (bdOffenGesamt.doubleValue() < dMindestmahnbetrag) {
				return null;
			}

			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeIId, theClientDto);
			Locale locDruck = Helper.string2Locale(kundeDto.getPartnerDto().getLocaleCNrKommunikation());
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);

			mapParameter.put(LPReport.P_MANDANTADRESSE, Helper.formatMandantAdresse(mandantDto));
			mapParameter.put("P_KUNDE_ADRESSBLOCK",
					formatAdresseFuerAusdruck(kundeDto.getPartnerDto(), null, mandantDto, locDruck));
			mapParameter.put("P_KUNDE_UID", kundeDto.getPartnerDto().getCUid());

			KontoDto k = null;
			if (kundeDto.getIidDebitorenkonto() != null) {
				k = getFinanzFac().kontoFindByPrimaryKey(kundeDto.getIidDebitorenkonto());
				mapParameter.put("P_KUNDE_DEBITORENNUMMER", k.getCNr());
			}

			// SP8331
			mapParameter.put("P_KUNDE_EORI", kundeDto.getPartnerDto().getCEori());
			mapParameter.put("P_KUNDE_LIEFERANTENNR", kundeDto.getCLieferantennr());

			MahnlaufDto mahnlaufDto = getMahnwesenFac().mahnlaufFindByPrimaryKey(mahnlaufIId);

			PersonalDto personalDto_sachbearbeiter = getPersonalFac()
					.personalFindByPrimaryKey(mahnlaufDto.getPersonalIIdAnlegen(), theClientDto);

			mapParameter.put("P_SACHBEARBEITER", personalDto_sachbearbeiter.formatAnrede());
			mapParameter.put("P_SACHBEARBEITER_EMAIL", personalDto_sachbearbeiter.getCEmail());
			mapParameter.put("P_SACHBEARBEITER_TELDW", personalDto_sachbearbeiter.getCTelefon());

			mapParameter.put("P_KUNDE_NUMMER", kundeDto.getIKundennummer());
			mapParameter.put("P_KUNDE_HINWEISEXTERN", kundeDto.getSHinweisextern());
			PersonalDto oPersonalBenutzer = getPersonalFac().personalFindByPrimaryKey(theClientDto.getIDPersonal(),
					theClientDto);
			mapParameter.put("P_UNSER_ZEICHEN", Helper.getKurzzeichenkombi(oPersonalBenutzer.getCKurzzeichen(),
					oPersonalBenutzer.getCKurzzeichen()));
			mapParameter.put("P_DATUM", getDate());

			String sEmail = getPartnerFac().partnerkommFindRespectPartnerAsStringOhneExec(
					ansprechpartnerIIdErsteRechnung, kundeDto.getPartnerDto(), PartnerFac.KOMMUNIKATIONSART_EMAIL,
					theClientDto.getMandant(), theClientDto);
			String sFax = getPartnerFac().partnerkommFindRespectPartnerAsStringOhneExec(ansprechpartnerIIdErsteRechnung,
					kundeDto.getPartnerDto(), PartnerFac.KOMMUNIKATIONSART_FAX, theClientDto.getMandant(),
					theClientDto);
			String sTelefon = getPartnerFac().partnerkommFindRespectPartnerAsStringOhneExec(
					ansprechpartnerIIdErsteRechnung, kundeDto.getPartnerDto(), PartnerFac.KOMMUNIKATIONSART_TELEFON,
					theClientDto.getMandant(), theClientDto);
			// belegkommunikation: 3 daten als parameter an den Report
			// weitergeben
			mapParameter.put(LPReport.P_ANSPRECHPARTNEREMAIL, sEmail != null ? sEmail : "");
			mapParameter.put(LPReport.P_ANSPRECHPARTNERFAX, sFax != null ? sFax : "");
			mapParameter.put(LPReport.P_ANSPRECHPARTNERTELEFON, sTelefon != null ? sTelefon : "");

			MahntextDto mahntextDto = getFinanzServiceFac().mahntextFindByMandantLocaleCNr(mandantDto.getCNr(),
					Helper.locale2String(locDruck), iMaxMahnstufe);

			if (mahntextDto != null) {
				mapParameter.put("P_MAHNTEXT", Helper.formatStyledTextForJasper(mahntextDto.getCTextinhalt()));
			} else {
				EJBExceptionLP e = new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_KEINE_MAHNTEXTE_EINGETRAGEN, "");
				ArrayList<Object> a = new ArrayList<Object>();
				a.add(mandantDto.getCNr() + "|" + theClientDto.getLocUiAsString() + "|" + iMaxMahnstufe);
				e.setAlInfoForTheClient(a);
				throw e;
			}

			MahnstufeDto mahnstufeDto = getMahnwesenFac()
					.mahnstufeFindByPrimaryKey(new MahnstufePK(iMaxMahnstufe, theClientDto.getMandant()));
			BigDecimal mahnspesen = getMahnspesen(mahnstufeDto, new java.sql.Date(mahnlaufDto.getTAnlegen().getTime()),
					theClientDto, mandantDto.getWaehrungCNr());

			if (mahnspesen != null) {
				mapParameter.put("P_MAHNSPESEN", mahnspesen);
			} else {
				mapParameter.put("P_MAHNSPESEN", new BigDecimal(0));
			}

			if (letzteBelegwaehrung != null) {
				BigDecimal mahnspesenBelegwaehrung = getMahnspesen(mahnstufeDto,
						new java.sql.Date(mahnlaufDto.getTAnlegen().getTime()), theClientDto, letzteBelegwaehrung);

				if (mahnspesenBelegwaehrung != null) {
					mapParameter.put("P_MAHNSPESEN_BELEGWAEHRUNG", mahnspesenBelegwaehrung);
				} else {
					mapParameter.put("P_MAHNSPESEN_BELEGWAEHRUNG", new BigDecimal(0));
				}
			}

			mapParameter.put("P_BELEGWAEHRUNG", letzteBelegwaehrung);

			mapParameter.put("P_BERUECKSICHTIGTBIS", new java.sql.Date(System.currentTimeMillis()));
			mapParameter.put(LPReport.P_WAEHRUNG, mandantDto.getWaehrungCNr());
			mapParameter.put("P_ABSENDER", mandantDto.getPartnerDto().formatAnrede());
			mapParameter.put("P_BENUTZERNAME", oPersonalBenutzer.getPartnerDto().formatAnrede());
			initJRDS(mapParameter, FinanzReportFac.REPORT_MODUL, FinanzReportFac.REPORT_SAMMELMAHNUNG,
					theClientDto.getMandant(), locDruck, theClientDto, bMitLogo, kundeDto.getKostenstelleIId());
			JasperPrintLP print = getReportPrint();
			print.putAdditionalInformation(JasperPrintLP.KEY_MAHNSTUFE, iMaxMahnstufe);
			print.putAdditionalInformation(JasperPrintLP.KEY_RECHNUNG_C_NR, sRechnungen);
			return print;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public JasperPrintLP printSaldenliste(final String mandantCNr, ReportSaldenlisteKriterienDto krit,
			final TheClientDto theClientDto) throws EJBExceptionLP {
		Session session = null;
		try {
			this.useCase = UC_SALDENLISTE;
			this.index = -1;
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRFinanzKonto.class);
			// Filter nach Mandant
			c.add(Restrictions.eq(FinanzFac.FLR_KONTO_MANDANT_C_NR, mandantCNr));
			// Filter kontotyp
			c.add(Restrictions.eq(FinanzFac.FLR_KONTO_KONTOTYP_C_NR, krit.getKontotypCNr()));

			if (krit.getKontotypCNr().equals(FinanzServiceFac.KONTOTYP_DEBITOR)
					|| krit.getKontotypCNr().equals(FinanzServiceFac.KONTOTYP_KREDITOR)) {
				c.addOrder(Order.asc("steuerkategorie_c_nr")).addOrder(Order.asc(FinanzFac.FLR_KONTO_FINANZAMT_I_ID));
				// c.createAlias("flrsteuerkategorie", "s");
				// c.addOrder(Order.asc("s.c_nr"));
			}

			// Sortierung aufsteigend nach Kontonummer
			c.addOrder(Order.asc(FinanzFac.FLR_KONTO_C_NR));

			List<?> list = c.list();
			data = new Object[list.size()][REPORT_SALDENLISTE_ANZAHL_SPALTEN];
			int i = 0;

			final ReversechargeartDto rcartOhneDto = getFinanzServiceFac().reversechargeartFindOhne(theClientDto);

			HvCreatingCachingProvider<String, SteuerkategorieDto> stkDtoMap = new HvCreatingCachingProvider<String, SteuerkategorieDto>() {
				@Override
				protected SteuerkategorieDto provideValue(String key, String transformedKey) {
					String[] keys = key.split(",");
					SteuerkategorieDto stkDto = getFinanzServiceFac().steuerkategorieFindByCNrFinanzamtIId(keys[0],
							rcartOhneDto.getIId(), Integer.valueOf(keys[1]), theClientDto);
					return stkDto;
				}
			};

			HvCreatingCachingProvider<Integer, String> finanzamtMap = new HvCreatingCachingProvider<Integer, String>() {
				@Override
				protected String provideValue(Integer key, Integer transformedKey) {
					try {
						return getFinanzFac().finanzamtFindByPrimaryKey(key, mandantCNr, theClientDto).getPartnerDto()
								.formatAnrede();
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
					return null;
				}
			};
			for (Iterator<FLRFinanzKonto> iter = (Iterator<FLRFinanzKonto>) list.iterator(); iter.hasNext();) {

				FLRFinanzKonto konto = iter.next();
				data[i][REPORT_SALDENLISTE_KONTONUMMER] = konto.getC_nr();
				data[i][REPORT_SALDENLISTE_KONTOBEZEICHNUNG] = konto.getC_bez();
				if (konto.getFlrergebnisgruppe() != null) {
					if (Helper.short2boolean(konto.getFlrergebnisgruppe().getB_bilanzgruppe()) == true) {
						data[i][REPORT_SALDENLISTE_ERGEBNISGRUPPE] = "B:" + konto.getFlrergebnisgruppe().getC_bez();
					} else {
						data[i][REPORT_SALDENLISTE_ERGEBNISGRUPPE] = "E:" + konto.getFlrergebnisgruppe().getC_bez();
					}
				}

				if (konto.getFlrkontoart() != null) {
					data[i][REPORT_SALDENLISTE_KONTOART] = konto.getFlrkontoart().getC_nr();
				}

				if (konto.getFlruvaart() != null) {
					data[i][REPORT_SALDENLISTE_UVAART] = konto.getFlruvaart().getC_nr();
				}

				if (konto.getFlrmwstsatz() != null) {
					data[i][REPORT_SALDENLISTE_UVAART_VARIANTE] = getMandantFac()
							.mwstsatzFindByPrimaryKey(konto.getFlrmwstsatz().getI_id(), theClientDto)
							.formatMwstsatz(theClientDto);
				}

				if (konto.getFinanzamt_i_id() != null) {
					data[i][REPORT_SALDENLISTE_FINANZAMT] = finanzamtMap.getValueOfKey(konto.getFinanzamt_i_id());
					// data[i][REPORT_SALDENLISTE_FINANZAMT] = getFinanzFac()
					// .finanzamtFindByPrimaryKey(
					// konto.getFinanzamt_i_id(), mandantCNr,
					// theClientDto).getPartnerDto()
					// .formatAnrede();
				}

				if (konto.getKontoart_c_nr() == null) {
					List<FLRFinanzKonto> konten = new ArrayList<FLRFinanzKonto>();
					konten.add(konto);

					BigDecimal[] salden = getSammelSalden(krit, konten, theClientDto);
					setDataFromSalden(i, salden);
					// AD: Sammelbuchungsfunktionalitaet wird durch
					// Finanzamtsbuchungen ersetzt
					// } else if (konto.getKontoart_c_nr().equals(
					// FinanzServiceFac.KONTOART_UST_SAMMEL)) {
					// List<FLRFinanzKonto> ustKonten = getKontenJeArt(
					// FinanzServiceFac.KONTOART_UST, mandantCNr);
					// if (Helper.short2boolean(konto.getB_manuellbebuchbar()))
					// ustKonten.add(konto);
					// BigDecimal[] salden = getSammelSalden(krit, ustKonten,
					// theClientDto);
					// setDataFromSalden(i, salden);
					// } else if (konto.getKontoart_c_nr().equals(
					// FinanzServiceFac.KONTOART_VST_SAMMEL)) {
					// List<FLRFinanzKonto> vstKonten = getKontenJeArt(
					// FinanzServiceFac.KONTOART_VST, mandantCNr);
					// if (Helper.short2boolean(konto.getB_manuellbebuchbar()))
					// vstKonten.add(konto);
					// BigDecimal[] salden = getSammelSalden(krit, vstKonten,
					// theClientDto);
					// setDataFromSalden(i, salden);
					// } else if (konto.getKontoart_c_nr().equals(
					// FinanzServiceFac.KONTOART_SAMMEL)) {
					// List<FLRFinanzKonto> konten = getKontenJeSammelkonto(
					// konto.getI_id(), mandantCNr);
					// if (Helper.short2boolean(konto.getB_manuellbebuchbar()))
					// konten.add(konto);
					// BigDecimal[] salden = getSammelSalden(krit, konten,
					// theClientDto);
					// setDataFromSalden(i, salden);
				} else {
					List<FLRFinanzKonto> konten = new ArrayList<FLRFinanzKonto>();
					konten.add(konto);

					BigDecimal[] salden = getSammelSalden(krit, konten, theClientDto);
					setDataFromSalden(i, salden);
				}

				if (konto.getSteuerkategorie_c_nr() != null) {
					SteuerkategorieDto stkDto = stkDtoMap
							.getValueOfKey(konto.getSteuerkategorie_c_nr() + "," + konto.getFinanzamt_i_id());
					if (stkDto == null) {
						throw EJBExcFactory.steuerkategorieFehlt(konto.getFinanzamt_i_id(), rcartOhneDto.getIId(),
								konto.getSteuerkategorie_c_nr());
					}
					data[i][REPORT_SALDENLISTE_STEUERKATEGORIE] = stkDto.getCBez();
				}

				// if (konto.getFlrsteuerkategorie() != null) {
				// data[i][REPORT_SALDENLISTE_STEUERKATEGORIE] = konto
				// .getFlrsteuerkategorie().getC_bez();
				// }
				data[i][REPORT_SALDENLISTE_KONSISTENT] = new Boolean(getBuchenFac()
						.isKontoMitEBKonsistent(konto.getI_id(), krit.getIGeschaeftsjahr(), theClientDto));
				i++;
			}
			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			// Mandantenwaehrung
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
			mapParameter.put(LPReport.P_WAEHRUNG, mandantDto.getWaehrungCNr());

			mapParameter.put("P_PERIODE", new Integer(krit.getIPeriode()));
			mapParameter.put("P_GESCHAEFTSJAHR", krit.getIGeschaeftsjahr());
			Date[] dVonBis = getBuchenFac().getDatumbereichPeriodeGJ(krit.getIGeschaeftsjahr(), krit.getIPeriode(),
					theClientDto);

			mapParameter.put("P_PERIODE_KALENDERDATUM", dVonBis[0]);
			mapParameter.put("P_KONTOTYP", krit.getKontotypCNr());

			initJRDS(mapParameter, FinanzReportFac.REPORT_MODUL, FinanzReportFac.REPORT_SALDENLISTE,
					theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

			JasperPrintLP print = getReportPrint();
			PrintInfoDto values = new PrintInfoDto();
			values.setDocPath(new DocPath(new DocNodeSaldenliste(krit, mandantCNr)));
			// JCRDocFac.HELIUMV_NODE + "/"
			// + LocaleFac.BELEGART_FINANZBUCHHALTUNG.trim() + "/"
			// + krit.getIGeschaeftsjahr() + "/Saldenliste/"
			// + krit.getKontotypCNr() + "/" + krit.getIPeriode();
			values.setiId(theClientDto.getIDPersonal());
			values.setTable("");

			print.setOInfoForArchive(values);

			return print;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
			// } catch(Exception e) {
			// myLogger.error("Exc", e) ;
			// return null ;
		} finally {
			closeSession(session);
		}
	}

	private void setDataFromSalden(int i, BigDecimal[] salden) {
		data[i][REPORT_SALDENLISTE_HABEN] = salden[0];
		data[i][REPORT_SALDENLISTE_SOLL] = salden[1];
		data[i][REPORT_SALDENLISTE_EBWERT] = salden[2];
		data[i][REPORT_SALDENLISTE_SOLL_BIS] = salden[3];
		data[i][REPORT_SALDENLISTE_HABEN_BIS] = salden[4];
		data[i][REPORT_SALDENLISTE_EBWERT_BIS] = salden[5];
	}

	private BigDecimal[] getSammelSalden(ReportSaldenlisteKriterienDto krit, List<FLRFinanzKonto> konten,
			TheClientDto theClientDto) throws RemoteException {
		BigDecimal[] salden = { new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0),
				new BigDecimal(0), new BigDecimal(0) };
		for (Iterator<FLRFinanzKonto> iter = (Iterator<FLRFinanzKonto>) konten.iterator(); iter.hasNext();) {
			FLRFinanzKonto konto = iter.next();
			salden[0] = salden[0].add(getBuchenFac().getHabenVonKonto(konto.getI_id(), krit.getIGeschaeftsjahr(),
					krit.getIPeriode(), theClientDto));
			salden[1] = salden[1].add(getBuchenFac().getSollVonKonto(konto.getI_id(), krit.getIGeschaeftsjahr(),
					krit.getIPeriode(), theClientDto));
			salden[2] = salden[2].add(getBuchenFac().getSummeEroeffnungKontoIId(konto.getI_id(),
					krit.getIGeschaeftsjahr(), krit.getIPeriode(), false, theClientDto));
			salden[3] = salden[3].add(getBuchenFac().getSollVonKonto(konto.getI_id(), krit.getIGeschaeftsjahr(),
					krit.getIPeriode(), true, theClientDto));
			salden[4] = salden[4].add(getBuchenFac().getHabenVonKonto(konto.getI_id(), krit.getIGeschaeftsjahr(),
					krit.getIPeriode(), true, theClientDto));
			salden[5] = salden[5].add(getBuchenFac().getSummeEroeffnungKontoIId(konto.getI_id(),
					krit.getIGeschaeftsjahr(), krit.getIPeriode(), true, theClientDto));
		}
		return salden;
	}

	@SuppressWarnings("unchecked")
	private List<FLRFinanzKonto> getKontenJeArt(String kontoartCNr, String mandantCNr) {
		Session session = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		session = factory.openSession();
		Criteria c = session.createCriteria(FLRFinanzKonto.class);
		// Filter nach Mandant
		c.add(Restrictions.eq(FinanzFac.FLR_KONTO_MANDANT_C_NR, mandantCNr));
		// Filter kontoart
		c.add(Restrictions.eq(FinanzFac.FLR_KONTO_KONTOART_C_NR, kontoartCNr));
		// Sortierung aufsteigend nach Kontonummer
		c.addOrder(Order.asc(FinanzFac.FLR_KONTO_C_NR));
		List<FLRFinanzKonto> list = c.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	private List<FLRFinanzKonto> getKontenJeUvaart(Integer uvaartIId, Integer finanzamtIId) {
		Session session = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		session = factory.openSession();
		Criteria c = session.createCriteria(FLRFinanzKonto.class);
		// Filter Uvaart
		c.add(Restrictions.eq(FinanzFac.FLR_KONTO_UVAART_I_ID, uvaartIId));
		// Filter Finanzamt
		c.add(Restrictions.eq(FinanzFac.FLR_KONTO_FINANZAMT_I_ID, finanzamtIId));
		// Sortierung aufsteigend nach Kontonummer
		c.addOrder(Order.asc(FinanzFac.FLR_KONTO_C_NR));
		List<FLRFinanzKonto> list = c.list();
		return list;
	}

	/*
	 * AD: Methode wird so nicht mehr verwendet
	 * 
	 * @SuppressWarnings("unchecked") private List<FLRFinanzKonto>
	 * getKontenJeSammelkonto(Integer sammelkontoIId, String mandantCNr) { Session
	 * session = null; SessionFactory factory = FLRSessionFactory.getFactory();
	 * session = factory.openSession(); Criteria c =
	 * session.createCriteria(FLRFinanzKonto.class); // Filter nach Mandant
	 * c.add(Restrictions.eq(FinanzFac.FLR_KONTO_MANDANT_C_NR, mandantCNr)); //
	 * Filter kontoart //CK: Auskommentiert wg. PJ 17732
	 * //c.add(Restrictions.eq(FinanzFac.FLR_KONTO_I_ID_WEITERFUEHRENDBILANZ, //
	 * sammelkontoIId)); // Sortierung aufsteigend nach Kontonummer
	 * c.addOrder(Order.asc(FinanzFac.FLR_KONTO_C_NR)); List<FLRFinanzKonto> list =
	 * c.list(); return list; }
	 */

	public JasperPrintLP printMahnlauf(ReportJournalKriterienDto krit, Integer mahnlaufIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Session session = null;
		try {
			this.useCase = UC_MAHNLAUF;
			this.index = -1;
			// Mahnlauf holen
			MahnlaufDto mahnlaufDto = getMahnwesenFac().mahnlaufFindByPrimaryKey(mahnlaufIId);
			// Hibernate-Abfrage
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRFinanzMahnung.class);
			Criteria crit = c.createCriteria(FinanzFac.FLR_MAHNUNG_FLRRECHNUNGREPORT);
			// Filter nach Mandant
			c.add(Restrictions.eq(FinanzFac.FLR_MAHNUNG_MAHNLAUF_I_ID, mahnlaufIId));
			// Filter nach einem Kunde
			if (krit.kundeIId != null) {
				crit.add(Restrictions.eq(RechnungFac.FLR_RECHNUNG_KUNDE_I_ID, krit.kundeIId));
			}
			// Filter nach einem Vertrter
			if (krit.vertreterIId != null) {
				crit.add(Restrictions.eq(RechnungFac.FLR_RECHNUNG_VERTRETER_I_ID, krit.vertreterIId));
			}
			// Sortierung nach Kunde
			if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				crit.createCriteria(RechnungFac.FLR_RECHNUNG_FLRKUNDE).createCriteria(KundeFac.FLR_PARTNER)
						.addOrder(Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));
			}
			// Sortierung nach Vertreter
			if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_VERTRETER) {
				crit.createCriteria(RechnungFac.FLR_RECHNUNG_FLRVERTRETER).createCriteria(KundeFac.FLR_PARTNER)
						.addOrder(Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));
			}
			// Sortierung aufsteigend nach Rechnungsnummer
			crit.addOrder(Order.asc(RechnungFac.FLR_RECHNUNG_C_NR));
			List<?> list = c.list();
			data = new Object[list.size()][22];
			int i = 0;
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLRFinanzMahnung mahnung = (FLRFinanzMahnung) iter.next();
				// Dto holen, da nicht alle Daten da sind
				MahnungDto mahnungDto = getMahnwesenFac().mahnungFindByPrimaryKey(mahnung.getI_id());
				// Array befuellen
				data[i][REPORT_MAHNLAUF_KUNDE] = mahnung.getFlrrechnungreport().getFlrkunde().getFlrpartner()
						.getC_name1nachnamefirmazeile1();
				if (mahnung.getFlrrechnungreport().getFlrvertreter() != null) {
					data[i][REPORT_MAHNLAUF_VERTRETER] = mahnung.getFlrrechnungreport().getFlrvertreter()
							.getFlrpartner().getC_name1nachnamefirmazeile1();
				}
				data[i][REPORT_MAHNLAUF_LETZTEMAHNSTUFE] = mahnungDto.getMahnstufeIIdLetztemahnstufe();
				data[i][REPORT_MAHNLAUF_MAHNSPERREBIS] = mahnung.getFlrrechnungreport().getT_mahnsperrebis();
				data[i][REPORT_MAHNLAUF_LETZTESMAHNDATUM] = mahnungDto.getTLetztesmahndatum();
				data[i][REPORT_MAHNLAUF_MAHNSTUFE] = mahnung.getMahnstufe_i_id();
				// Wert kontrollieren - jemand koennte die rechnung
				// zurueckgenommen haben

				// SP3194
				BigDecimal bdUstAnzahlungFW = new BigDecimal(0);
				BigDecimal bdNettoAnzahlungFW = new BigDecimal(0);
				if (mahnung.getFlrrechnungreport().getFlrauftrag() != null && mahnung.getFlrrechnungreport()
						.getFlrrechnungart().getC_nr().equals(RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG)) {
					bdNettoAnzahlungFW = getRechnungFac()
							.getAnzahlungenZuSchlussrechnungFw(mahnung.getFlrrechnungreport().getI_id());
					bdUstAnzahlungFW = getRechnungFac()
							.getAnzahlungenZuSchlussrechnungUstFw(mahnung.getFlrrechnungreport().getI_id());

				}

				if (mahnung.getFlrrechnungreport().getN_wert() != null) {
					data[i][REPORT_MAHNLAUF_OFFEN] = mahnung.getFlrrechnungreport().getN_wert()
							.subtract(getRechnungFac()
									.getBereitsBezahltWertVonRechnung(mahnung.getFlrrechnungreport().getI_id(), null))
							.subtract(bdNettoAnzahlungFW);

				}

				if (mahnung.getFlrrechnungreport().getN_wertust() != null) {
					data[i][REPORT_MAHNLAUF_OFFENUST] =

							mahnung.getFlrrechnungreport().getN_wertust().subtract(

									getRechnungFac().getBereitsBezahltWertVonRechnungUst(
											mahnung.getFlrrechnungreport().getI_id(), null))
									.subtract(bdUstAnzahlungFW);
				}

				data[i][REPORT_MAHNLAUF_RECHNUNGSDATUM] = mahnung.getFlrrechnungreport().getD_belegdatum();
				data[i][REPORT_MAHNLAUF_RECHNUNGSNUMMER] = mahnung.getFlrrechnungreport().getC_nr();
				data[i][REPORT_MAHNLAUF_RECHNUNGSART] = mahnung.getFlrrechnungreport().getFlrrechnungart().getC_nr();
				// Wert in Mandantenwaehrungb
				data[i][REPORT_MAHNLAUF_WERT] = mahnung.getFlrrechnungreport().getN_wert();
				data[i][REPORT_MAHNLAUF_WERTUST] = mahnung.getFlrrechnungreport().getN_wertust();

				// Zieldatum berechnen, falls vorhanden
				if (mahnung.getFlrrechnungreport().getZahlungsziel_i_id() != null) {
					Date dZieldatum = getMandantFac().berechneZielDatumFuerBelegdatum(
							mahnung.getFlrrechnungreport().getD_belegdatum(),
							mahnung.getFlrrechnungreport().getZahlungsziel_i_id(), theClientDto);
					data[i][REPORT_MAHNLAUF_ZIELDATUM] = dZieldatum;
				}
				// Auftragsdaten (falls die Rechnung auftragsbezogen ist)
				if (mahnung.getFlrrechnungreport().getFlrauftrag() != null) {
					data[i][REPORT_MAHNLAUF_AUFTRAG_BESTELLNUMMER] = mahnung.getFlrrechnungreport().getFlrauftrag()
							.getC_bestellnummer();
					data[i][REPORT_MAHNLAUF_AUFTRAG_NUMMER] = mahnung.getFlrrechnungreport().getFlrauftrag().getC_nr();
					data[i][REPORT_MAHNLAUF_AUFTRAG_PROJEKT] = mahnung.getFlrrechnungreport().getFlrauftrag()
							.getC_bez();
				}
				// Statistikadresse (falls definiert)
				if (mahnung.getFlrrechnungreport().getFlrstatistikadresse() != null) {
					data[i][REPORT_MAHNLAUF_KUNDE_STATISTIKADRESSE] = mahnung.getFlrrechnungreport()
							.getFlrstatistikadresse().getFlrpartner().getC_name1nachnamefirmazeile1();
				}
				// die 2 bezeichnungszeilen der ersten rechnungsposition
				RechnungPositionDto[] rePos = getRechnungFac()
						.rechnungPositionFindByRechnungIId(mahnung.getFlrrechnungreport().getI_id());
				if (rePos != null && rePos.length > 0) {
					RechnungPositionDto pos = rePos[0];
					// Artikel holen, falls da
					ArtikelDto artikelDto = null;
					if (pos.getArtikelIId() != null) {
						artikelDto = getArtikelFac().artikelFindByPrimaryKey(pos.getArtikelIId(), theClientDto);
					}

					String sBezeichnung1;
					String sBezeichnung2;
					// Kein Artikel oder Bezeichnung des Artikels uebersteuert?
					if (artikelDto == null || pos.getBArtikelbezeichnunguebersteuert() == null
							|| Helper.short2boolean(pos.getBArtikelbezeichnunguebersteuert())) {
						if (artikelDto == null || pos.getCBez() != null) {
							sBezeichnung1 = pos.getCBez();
						} else {
							sBezeichnung1 = artikelDto.getArtikelsprDto().getCBez();
						}
						// Zusatzbezeichnung
						if (artikelDto == null || pos.getCZusatzbez() != null) {
							sBezeichnung2 = pos.getCZusatzbez();
						} else {
							sBezeichnung2 = artikelDto.getArtikelsprDto().getCZbez();
						}
					}
					// Bezeichnung aus dem Artikel
					else {
						sBezeichnung1 = artikelDto.getArtikelsprDto().getCBez();
						sBezeichnung2 = artikelDto.getArtikelsprDto().getCZbez();
					}
					data[i][REPORT_MAHNLAUF_POSITIONSBEZEICHNUNG1] = sBezeichnung1;
					data[i][REPORT_MAHNLAUF_POSITIONSBEZEICHNUNG2] = sBezeichnung2;

					data[i][REPORT_MAHNLAUF_RECHNUNG_STATUS] = mahnung.getFlrrechnungreport().getStatus_c_nr();
					data[i][REPORT_MAHNLAUF_ZAHLUNGSZIEL] = getMandantFac().zahlungszielFindByIIdLocaleOhneExc(
							mahnung.getFlrrechnungreport().getZahlungsziel_i_id(), theClientDto.getLocUi(),
							theClientDto);
				}
				i++;
			}
			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			// Mandantenwaehrung
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
			mapParameter.put(LPReport.P_WAEHRUNG, mandantDto.getWaehrungCNr());
			// Datum des Mahnlaufs
			mapParameter.put("P_MAHNDATUM", mahnlaufDto.getTAnlegen());
			mapParameter.put(LPReport.P_SORTIERENACHKUNDE,
					new Boolean(krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER));
			mapParameter.put(LPReport.P_SORTIERENACHVERTRETER,
					new Boolean(krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_VERTRETER));
			initJRDS(mapParameter, FinanzReportFac.REPORT_MODUL, FinanzReportFac.REPORT_MAHNLAUF,
					theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
			return getReportPrint();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		} finally {
			closeSession(session);
		}
	}

	public JasperPrintLP printExportlauf(Integer exportlaufIId, TheClientDto theClientDto) throws EJBExceptionLP {
		Session session = null;
		try {
			this.useCase = UC_EXPORTLAUF;
			this.index = -1;
			// Mahnlauf holen
			ExportlaufDto exportlaufDto = getFibuExportFac().exportlaufFindByPrimaryKey(exportlaufIId);
			// Hibernate-Abfrage
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRFinanzExportdaten.class);
			// Filter nach Exportlauf
			c.createCriteria(FinanzFac.FLR_EXPORTDATEN_FLREXPORTLAUF)
					.add(Restrictions.eq(FinanzFac.FLR_EXPORTLAUF_I_ID, exportlaufIId));
			List<?> list = c.list();
			data = new Object[list.size()][9];
			int i = 0;
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLRFinanzExportdaten exportDaten = (FLRFinanzExportdaten) iter.next();
				// Dto holen, da nicht alle Daten da sind
				// MahnungDto mahnungDto =
				// getMahnwesenFac().mahnungFindByPrimaryKey
				// (exportDaten.getI_id());
				// // Array befuellen
				// data[i][REPORT_MAHNLAUF_KUNDE] =
				// exportDaten.getFlrrechnungreport().getFlrkunde().
				// getFlrpartner().getC_name1nachnamefirmazeile1();
				// data[i][REPORT_MAHNLAUF_LETZTEMAHNSTUFE] = mahnungDto.
				// getMahnstufeIIdLetztemahnstufe();
				// data[i][REPORT_MAHNLAUF_LETZTESMAHNDATUM] =
				// mahnungDto.getTLetztesmahndatum();
				// data[i][REPORT_MAHNLAUF_MAHNSTUFE] =
				// exportDaten.getMahnstufe_i_id();
				// // Wert kontrollieren - jemand koennte die rechnung
				// zurueckgenommen haben
				// if(exportDaten.getFlrrechnungreport().getN_wert()!=null) {
				// data[i][REPORT_MAHNLAUF_OFFEN] =
				// exportDaten.getFlrrechnungreport().getN_wert().
				// subtract(getRechnungFac().getBereitsBezahltWertVonRechnung(
				// exportDaten.
				// getFlrrechnungreport().getI_id(), null));
				// }
				// data[i][REPORT_MAHNLAUF_RECHNUNGSDATUM] =
				// exportDaten.getFlrrechnungreport().
				// getD_belegdatum();
				// data[i][REPORT_MAHNLAUF_RECHNUNGSNUMMER] =
				// exportDaten.getFlrrechnungreport().getC_nr();
				// // Wert in Mandantenwaehrungb
				// data[i][REPORT_MAHNLAUF_WERT] =
				// exportDaten.getFlrrechnungreport().getN_wert();
				// // Zieldatum berechnen, falls vorhanden
				// if(exportDaten.getFlrrechnungreport().getZahlungsziel_i_id()!=
				// null) {
				// Date dZieldatum =
				// getMandantFac().berechneZielDatumFuerBelegdatum(exportDaten.
				// getFlrrechnungreport().getD_belegdatum(),
				// exportDaten.getFlrrechnungreport().getZahlungsziel_i_id(),
				// theClientDto);
				// data[i][REPORT_MAHNLAUF_ZIELDATUM] = dZieldatum;
				// }
				// i++;
			}
			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			// Mandantenwaehrung
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
			mapParameter.put(LPReport.P_WAEHRUNG, mandantDto.getWaehrungCNr());
			// Datum des Mahnlaufs
			mapParameter.put("P_STICHTAG", exportlaufDto.getTStichtag());
			initJRDS(mapParameter, FinanzReportFac.REPORT_MODUL, FinanzReportFac.REPORT_EXPORTLAUF,
					theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
			return getReportPrint();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		} finally {
			closeSession(session);
		}
	}

	public JasperPrintLP printIntrastatVorschau(String sVerfahren, java.sql.Date dVon, java.sql.Date dBis,
			BigDecimal bdTransportkosten, TheClientDto theClientDto) throws EJBExceptionLP {
		this.useCase = UC_INTRASTAT;
		this.index = -1;

		Map<String, Object> mapParameter = new TreeMap<String, Object>();
		mapParameter.put("P_VON", dVon);
		mapParameter.put("P_BIS", dBis);

		dBis = Helper.addiereTageZuDatum(Helper.cutDate(dBis), 1); // auf 24 Uhr
		// setzen,
		// damit
		// alle
		// Daten
		// dieses
		// Tages
		// dabei
		// sind

		try {
			ArrayList<IntrastatDto> daten = null;
			if (sVerfahren.equals(FinanzReportFac.INTRASTAT_VERFAHREN_VERSAND)) {
				daten = getFibuExportFac().getIntrastatDatenVersand(dVon, dBis, bdTransportkosten, theClientDto);
			} else if (sVerfahren.equals(FinanzReportFac.INTRASTAT_VERFAHREN_WARENEINGANG)) {
				daten = getFibuExportFac().getIntrastatDatenWareneingang(dVon, dBis, bdTransportkosten, theClientDto);
			} else {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("Verfahren = " + sVerfahren));
			}
			data = new Object[daten.size()][REPORT_INTRASTAT_ANZAHL_SPALTEN];
			int i = 0;
			for (IntrastatDto iDto : daten) {
				data[i][REPORT_INTRASTAT_BELEGART] = iDto.getBelegart();
				data[i][REPORT_INTRASTAT_BELEGNUMMER] = iDto.getBelegnummer();
				data[i][REPORT_INTRASTAT_BEZEICHNUNG] = iDto.getArtikelDto().formatArtikelbezeichnung();
				data[i][REPORT_INTRASTAT_GEWICHT] = iDto.getGewichtInKg();
				data[i][REPORT_INTRASTAT_IDENT] = iDto.getArtikelDto().getCNr();
				data[i][REPORT_INTRASTAT_MENGE] = iDto.getMenge();
				data[i][REPORT_INTRASTAT_PREIS] = iDto.getEinzelpreis();
				data[i][REPORT_INTRASTAT_STATISTISCHER_WERT] = iDto.getStatistischerWert();

				String wvkNummer;
				String wvkBezeichnung;
				if (iDto.getArtikelDto().getCWarenverkehrsnummer() != null) {
					WarenverkehrsnummerDto wvk = iDto.getWarenverkehrsnummerDto();
					if (wvk != null) {
						wvkNummer = wvk.getCNr();
						wvkBezeichnung = wvk.getCBez();
					} else {
						wvkNummer = "???   " + iDto.getArtikelDto().getCWarenverkehrsnummer();
						wvkBezeichnung = "???";
					}
				} else {
					wvkNummer = "???";
					wvkBezeichnung = "???";
				}
				data[i][REPORT_INTRASTAT_WARENVERKEHRSNUMMER] = wvkNummer;
				data[i][REPORT_INTRASTAT_WVK_BEZEICHNUNG] = wvkBezeichnung;
				data[i][REPORT_INTRASTAT_WERT] = iDto.getWert();
				data[i][REPORT_INTRASTAT_UID] = iDto.getUid();

				if (sVerfahren.equals(FinanzReportFac.INTRASTAT_VERFAHREN_VERSAND)) {
					if (iDto.getArtikelDto().getLandIIdUrsprungsland() != null) {
						data[i][REPORT_INTRASTAT_URSPRUNGSLAND] = getSystemFac()
								.landFindByPrimaryKey(iDto.getArtikelDto().getLandIIdUrsprungsland()).getCLkz();
					}
				} else {
					data[i][REPORT_INTRASTAT_URSPRUNGSLAND] = iDto.getUrsprung();
				}

				i++;
			}

			String sVerfahrenBezeichnung;
			if (sVerfahren.equals(FinanzReportFac.INTRASTAT_VERFAHREN_VERSAND)) {
				sVerfahrenBezeichnung = getTextRespectUISpr("finanz.intrastat.versand", theClientDto.getMandant(),
						theClientDto.getLocUi());
			} else {
				sVerfahrenBezeichnung = getTextRespectUISpr("finanz.intrastat.eingang", theClientDto.getMandant(),
						theClientDto.getLocUi());
			}
			mapParameter.put("P_VERFAHREN", sVerfahrenBezeichnung);
			initJRDS(mapParameter, FinanzReportFac.REPORT_MODUL, FinanzReportFac.REPORT_INTRASTAT,
					theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return getReportPrint();
	}

	private ReportFieldValues reportSteuernachweisListe = new ReportFieldValues(new HashMap<String, Integer>() {
		private static final long serialVersionUID = -7477136424185048969L;
		{
			put("Betrag", REPORT_USTVERPROBUNG_BETRAG);
			put("Steuer", REPORT_USTVERPROBUNG_STEUER);
			put("Steuerart", REPORT_USTVERPROBUNG_STEUERART);
			put("KontoNr", REPORT_USTVERPROBUNG_KONTO_NR);
			put("KontoBez", REPORT_USTVERPROBUNG_KONTO_BEZ);
			put("GegenkontoNr", REPORT_USTVERPROBUNG_GEGENKONTO_NR);
			put("GegenkontoBez", REPORT_USTVERPROBUNG_GEGENKONTO_BEZ);
			put("MwstSatz", REPORT_USTVERPROBUNG_MWST_SATZ);
			put("MwstBez", REPORT_USTVERPROBUNG_MWST_BEZ);
			put("MwstIId", REPORT_USTVERPROBUNG_MWST_ID);
			put("SteuerkategorieBez", REPORT_USTVERPROBUNG_STEUERKATEGORIE_BEZ);
			put("MwstSatzBerechnet", REPORT_USTVERPROBUNG_MWST_SATZ_BERECHNET);
			put("ReversechargeartIId", REPORT_USTVERPROBUNG_RCART_ID);
			put("ReversechargeartCnr", REPORT_USTVERPROBUNG_RCART_CNR);
			put("ReversechargeartBez", REPORT_USTVERPROBUNG_RCART_BEZ);
			put("UvaartCnr", REPORT_USTVERPROBUNG_UVAART_CNR);
			put("GegenkontoUvaartCnr", REPORT_USTVERPROBUNG_GEGENKONTO_UVAART_CNR);
			put("Detail", REPORT_USTVERPROBUNG_DETAIL);
			put("Datum", REPORT_USTVERPROBUNG_DATUM);
			put("Buchungtext", REPORT_USTVERPROBUNG_BUCHUNGTEXT);
			put("Belegnummer", REPORT_USTVERPROBUNG_BELEGNUMMER);
			put("BelegartCnr", REPORT_USTVERPROBUNG_BELEGART);
			put("KontoMwstsatzId", REPORT_USTVERPROBUNG_KONTOMWSTSATZID);
			put("KontoSteuersatz", REPORT_USTVERPROBUNG_KONTOSTEUERSATZ);
			put("GegenkontoMwstsatzId", REPORT_USTVERPROBUNG_GEGENKONTOMWSTSATZID);
			put("GegenkontoSteuersatz", REPORT_USTVERPROBUNG_GEGENKONTOSTEUERSATZ);
		}
	});

	private List<UstVerprobungCompacted> createCompacted(List<UstVerprobungRow> steuerDetails) {
		List<UstVerprobungCompacted> c = new ArrayList<UstVerprobungCompacted>();
		UstVerprobungCompacted last = null;
		for (UstVerprobungRow detail : steuerDetails) {
			if (last == null || !last.equalsAusgenommenBetraege(detail)) {
				last = new UstVerprobungCompacted(detail);
				c.add(last);
			} else {
				last.addDetail(detail);
			}
		}
		return c;
	}

	private List<UstVerprobungRow> getRawUstVerprobung(ReportUvaKriterienDto krit, TheClientDto theClientDto,
			String[] steuerartCnrs) throws EJBExceptionLP, RemoteException {
		Validator.notNull(steuerartCnrs, "steuerartCnrs");
		Set<Integer> allMitlaufenden = getFinanzServiceFac().getAllMitlaufendeKonten(krit.getFinanzamtIId(),
				theClientDto);
		final List<String> steuerartSort = new ArrayList<String>();
		List<UstVerprobungRow> steuernachweisList = new ArrayList<UstVerprobungRow>();
		for (String steuerartCnr : steuerartCnrs) {
			Map<Integer, SteuerkontoInfo> kontoIds = null;

			if (FinanzServiceFac.STEUERART_UST.equals(steuerartCnr)) {
				kontoIds = getFinanzServiceFac().getAllIIdsUstkontoMitIIdMwstBez(krit.getFinanzamtIId(), theClientDto);
			}
			if (FinanzServiceFac.STEUERART_VST.equals(steuerartCnr)) {
				kontoIds = getFinanzServiceFac().getAllIIdsVstkontoMitIIdMwstBez(krit.getFinanzamtIId(), theClientDto);
			}
			if (FinanzServiceFac.STEUERART_EUST.equals(steuerartCnr)) {
				kontoIds = getFinanzServiceFac().getAllIIdsEUstkontoMitIIdMwstBez(krit.getFinanzamtIId(), theClientDto);
			}
			Validator.notNull(kontoIds, "kontoIds");
			steuernachweisList.addAll(getVerprobungList(krit, kontoIds, allMitlaufenden, steuerartCnr, theClientDto));
			steuerartSort.add(steuerartCnr);
		}

		Comparator<UstVerprobungRow> ustCompare = new UstVerprobungRowCompare(steuerartSort);
		Collections.sort(steuernachweisList, ustCompare);

		/*
		 * Collections.sort(steuernachweisList, new Comparator<UstVerprobungRow>() {
		 * 
		 * @Override public int compare(UstVerprobungRow o1, UstVerprobungRow o2) { if
		 * (o1.getSteuerkategorieISort() != o2.getSteuerkategorieISort()) { return new
		 * Integer(o1.getSteuerkategorieISort()).compareTo(o2.getSteuerkategorieISort())
		 * ; } if (!o1.getSteuerart().equals(o2.getSteuerart())) { Integer i1 =
		 * steuerartSort.indexOf(o1.getSteuerart()); Integer i2 =
		 * steuerartSort.indexOf(o2.getSteuerart()); return i1.compareTo(i2); }
		 * 
		 * int diff = diffSteuersatz(o1.getSteuersatz(), o2.getSteuersatz()); if (diff
		 * != 0) { return diff; }
		 * 
		 * diff = diffKontoCnr(o1.getKontoCNr(), o2.getKontoCNr()); if (diff != 0) {
		 * return diff; }
		 * 
		 * diff = diffKontoCnr(o1.getGegenkontoCNr(), o2.getGegenkontoCNr()); if (diff
		 * != 0) { return diff; }
		 * 
		 * return o1.getDatum().compareTo(o2.getDatum()); }
		 * 
		 * private int diffSteuersatz(BigDecimal satz1, BigDecimal satz2) { if (satz1 ==
		 * null && satz2 == null) { return 0; } if (satz1 == null && satz2 != null) {
		 * return -1; } if (satz1 != null && satz2 == null) { return 1; } return
		 * satz1.compareTo(satz2); }
		 * 
		 * private int diffKontoCnr(String kontocnr1, String kontocnr2) { if (kontocnr1
		 * == null && kontocnr2 == null) { return 0; } if (kontocnr1 == null &&
		 * kontocnr2 != null) { return -1; } if (kontocnr1 != null && kontocnr2 == null)
		 * { return 1; } return kontocnr1.compareTo(kontocnr2); } });
		 */

		return steuernachweisList;
	}

	private class UstVerprobungRowCompare implements Comparator<UstVerprobungRow> {
		private final List<String> steuerartSort;

		public UstVerprobungRowCompare(List<String> steuerartSort) {
			this.steuerartSort = steuerartSort;
		}

		public int compare(UstVerprobungRow o1, UstVerprobungRow o2) {
			if (o1.getSteuerkategorieISort() != o2.getSteuerkategorieISort()) {
				return o1.getSteuerkategorieISort() < o2.getSteuerkategorieISort() ? -1 : 1;
			}

			int diff = diffSteuerart(o1.getSteuerart(), o2.getSteuerart());
			if (diff != 0) {
				return diff;
			}

			diff = diffSteuersatz(o1.getSteuersatz(), o2.getSteuersatz());
			if (diff != 0) {
				return diff;
			}

			diff = diffKontoCnr(o1.getKontoCNr(), o2.getKontoCNr());
			if (diff != 0) {
				return diff;
			}

			diff = diffKontoCnr(o1.getGegenkontoCNr(), o2.getGegenkontoCNr());
			if (diff != 0) {
				return diff;
			}

			diff = diffSteuersatz(o1.getKontoSteuersatz(), o2.getKontoSteuersatz());
			if (diff != 0) {
				return diff;
			}

			return o1.getDatum().compareTo(o2.getDatum());
		}

		private int diffSteuerart(String s1, String s2) {
			if (s1.equals(s2))
				return 0;
			return steuerartSort.indexOf(s1) < steuerartSort.indexOf(s2) ? -1 : 1;
		}

		private int diffSteuersatz(BigDecimal satz1, BigDecimal satz2) {
			if (satz1 == null && satz2 == null) {
				return 0;
			}
			if (satz1 == null) {
				satz1 = new BigDecimal("1000");
			}
			if (satz2 == null) {
				satz2 = new BigDecimal("1000");
			}
			return satz1.compareTo(satz2);
		}

		private int diffKontoCnr(String kontocnr1, String kontocnr2) {
			if (kontocnr1 == null && kontocnr2 == null) {
				return 0;
			}
			// "null/nicht vorhanden" nach hinten sortieren
			if (kontocnr1 == null) {
				kontocnr1 = "ZZZZZZZZZZ";
			}
			if (kontocnr2 == null) {
				kontocnr2 = "ZZZZZZZZZZ";
			}
			return kontocnr1.compareTo(kontocnr2);
		}

	}

	private void fillReportData(Object[] reportRow, UstVerprobungRow stnw, boolean isDetail, Date[] dVonBis,
			TheClientDto theClientDto) throws RemoteException {

		reportRow[REPORT_USTVERPROBUNG_BETRAG] = stnw.getBetrag();
		reportRow[REPORT_USTVERPROBUNG_STEUER] = stnw.getSteuer();
		reportRow[REPORT_USTVERPROBUNG_STEUERART] = stnw.getSteuerart();
		reportRow[REPORT_USTVERPROBUNG_KONTO_BEZ] = stnw.getKontoCBez();
		reportRow[REPORT_USTVERPROBUNG_KONTO_NR] = stnw.getKontoCNr();
		reportRow[REPORT_USTVERPROBUNG_GEGENKONTO_BEZ] = stnw.getGegenkontoCBez();
		reportRow[REPORT_USTVERPROBUNG_GEGENKONTO_NR] = stnw.getGegenkontoCNr();
		reportRow[REPORT_USTVERPROBUNG_MWST_BEZ] = stnw.getMwstSatzBezCBez();
		reportRow[REPORT_USTVERPROBUNG_MWST_SATZ] = stnw.getSteuersatz();
		reportRow[REPORT_USTVERPROBUNG_MWST_ID] = stnw.getMwstSatzBezIId();
		reportRow[REPORT_USTVERPROBUNG_STEUERKATEGORIE_BEZ] = stnw.getSteuerkategorieCBez();
		if (stnw.getBetrag() != null) {
			BigDecimal d = new BigDecimal(
					getMandantFac().getMwstSatzVonBruttoBetragUndUst(theClientDto.getMandant(), stnw.getBelegDatum(),
							stnw.getBetrag().add(stnw.getSteuer()), stnw.getSteuer()).getFMwstsatz().toString())
									.setScale(FinanzFac.NACHKOMMASTELLEN);
			reportRow[REPORT_USTVERPROBUNG_MWST_SATZ_BERECHNET] = d;
			if (stnw.getSteuersatz() == null) {
				BuchungDto buchungDto = getBuchenFac().buchungFindByPrimaryKey(stnw.getBuchungId());
				throw EJBExcFactory.buchungSteuersaetzeInKontenUnterschiedlich(dVonBis[0], dVonBis[1], stnw,
						buchungDto);
			}
			if (stnw.getSteuersatz().compareTo(d) != 0) {
				myLogger.warn("Steuersaetze nicht ident! " + stnw.getSteuersatz().toPlainString() + ", "
						+ d.toPlainString() + ".");
			}
		}
		reportRow[REPORT_USTVERPROBUNG_RCART_ID] = stnw.getReversechargeartId();
		reportRow[REPORT_USTVERPROBUNG_RCART_CNR] = stnw.getReversechargeartCnr();
		reportRow[REPORT_USTVERPROBUNG_RCART_BEZ] = stnw.getReversechargeartCBez();
		reportRow[REPORT_USTVERPROBUNG_UVAART_CNR] = stnw.getUvaartCnr();
		reportRow[REPORT_USTVERPROBUNG_GEGENKONTO_UVAART_CNR] = stnw.getGegenkontoUvaartCnr();
		reportRow[REPORT_USTVERPROBUNG_DETAIL] = new Boolean(isDetail);
		reportRow[REPORT_USTVERPROBUNG_DATUM] = stnw.getDatum();
		reportRow[REPORT_USTVERPROBUNG_BUCHUNGTEXT] = stnw.getBuchungText();
		reportRow[REPORT_USTVERPROBUNG_BELEGNUMMER] = stnw.getBuchungBelegnummer();
		reportRow[REPORT_USTVERPROBUNG_BELEGART] = stnw.getBelegartCnr();
		reportRow[REPORT_USTVERPROBUNG_KONTOMWSTSATZID] = stnw.getKontoMwstsatzId();
		reportRow[REPORT_USTVERPROBUNG_KONTOSTEUERSATZ] = stnw.getKontoSteuersatz();
		reportRow[REPORT_USTVERPROBUNG_GEGENKONTOMWSTSATZID] = stnw.getGegenkontoMwstsatzId();
		reportRow[REPORT_USTVERPROBUNG_GEGENKONTOSTEUERSATZ] = stnw.getGegenkontoSteuersatz();

	}

	public JasperPrintLP printUstVerprobung(ReportUvaKriterienDto krit, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		useCase = UC_USTVERPROBUNG;
		krit.setSAbrechnungszeitraum(getParameterFac().getUvaAbrechnungszeitraum(theClientDto.getMandant()));
		Date[] dVonBis = getBuchenFac().getDatumbereichPeriodeGJUva(krit.getIGeschaeftsjahr(), krit.getIPeriode(),
				theClientDto);
		List<UstVerprobungRow> steuerDetails = getRawUstVerprobung(krit, theClientDto, new String[] {
				FinanzServiceFac.STEUERART_UST, FinanzServiceFac.STEUERART_VST, FinanzServiceFac.STEUERART_EUST });

		List<UstVerprobungCompacted> compacted = createCompacted(steuerDetails);
		int totalSize = compacted.size();
		if (krit.isDetails()) {
			totalSize += steuerDetails.size();
		}

		data = new Object[totalSize][REPORT_USTVERPROBUNG_ANZAHL_FELDER];
		int i = 0;
		for (UstVerprobungCompacted compact : compacted) {
			UstVerprobungRow stnw = compact.getCompacted();
			fillReportData(data[i], stnw, false, dVonBis, theClientDto);
			if (krit.isDetails()) {
				for (UstVerprobungRow detail : compact.getDetails()) {
					++i;
					fillReportData(data[i], detail, true, dVonBis, theClientDto);
				}
			}
			i++;
		}

		Map<String, Object> mapParameter = new HashMap<String, Object>();
		mapParameter.put(LPReport.P_WAEHRUNG, theClientDto.getSMandantenwaehrung());
		FinanzamtDto finanzamtDto = getFinanzFac().finanzamtFindByPartnerIIdMandantCNr(krit.getFinanzamtIId(),
				theClientDto.getMandant(), theClientDto);
		fillFinanzamtParameter(mapParameter, krit, finanzamtDto, dVonBis, theClientDto);

		ArrayList<FibuFehlerDto> errors = getFinanzServiceFac().pruefeBelegePeriode(krit.getIGeschaeftsjahr(),
				krit.getIPeriode(), true, theClientDto);
		mapParameter.put("P_BELEGFEHLER", new Boolean(errors.size() > 0));
		mapParameter.put("P_BELEGFEHLER_SUBREPORT", new LPFibuFehlerSubreport(errors));

		String report = FinanzReportFac.REPORT_USTVERPROBUNG;
		initJRDS(mapParameter, FinanzReportFac.REPORT_MODUL, report, theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);
		return getReportPrint();
	}

	public JasperPrintLP printUstVerprobung1(ReportUvaKriterienDto krit, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		useCase = UC_USTVERPROBUNG;
		krit.setSAbrechnungszeitraum(getParameterFac().getUvaAbrechnungszeitraum(theClientDto.getMandant()));
		Date[] dVonBis = getBuchenFac().getDatumbereichPeriodeGJUva(krit.getIGeschaeftsjahr(), krit.getIPeriode(),
				theClientDto);
		List<UstVerprobungRow> kommuliert = getRawUstVerprobung(krit, theClientDto, new String[] {
				FinanzServiceFac.STEUERART_UST, FinanzServiceFac.STEUERART_VST, FinanzServiceFac.STEUERART_EUST });
		data = new Object[kommuliert.size()][REPORT_USTVERPROBUNG_ANZAHL_FELDER];
		int i = 0;
		for (UstVerprobungRow stnw : kommuliert) {
			data[i][REPORT_USTVERPROBUNG_BETRAG] = stnw.getBetrag();
			data[i][REPORT_USTVERPROBUNG_STEUER] = stnw.getSteuer();
			data[i][REPORT_USTVERPROBUNG_STEUERART] = stnw.getSteuerart();
			data[i][REPORT_USTVERPROBUNG_KONTO_BEZ] = stnw.getKontoCBez();
			data[i][REPORT_USTVERPROBUNG_KONTO_NR] = stnw.getKontoCNr();
			data[i][REPORT_USTVERPROBUNG_GEGENKONTO_BEZ] = stnw.getGegenkontoCBez();
			data[i][REPORT_USTVERPROBUNG_GEGENKONTO_NR] = stnw.getGegenkontoCNr();
			data[i][REPORT_USTVERPROBUNG_MWST_BEZ] = stnw.getMwstSatzBezCBez();
			data[i][REPORT_USTVERPROBUNG_MWST_SATZ] = stnw.getSteuersatz();
			data[i][REPORT_USTVERPROBUNG_MWST_ID] = stnw.getMwstSatzBezIId();
			data[i][REPORT_USTVERPROBUNG_STEUERKATEGORIE_BEZ] = stnw.getSteuerkategorieCBez();
			if (stnw.getBetrag() != null) {
				BigDecimal d = new BigDecimal(
						getMandantFac().getMwstSatzVonBruttoBetragUndUst(theClientDto.getMandant(), stnw.getDatum(),
								stnw.getBetrag().add(stnw.getSteuer()), stnw.getSteuer()).getFMwstsatz());
				data[i][REPORT_USTVERPROBUNG_MWST_SATZ_BERECHNET] = d;
				if (stnw.getSteuersatz() == null) {
					BuchungDto buchungDto = getBuchenFac().buchungFindByPrimaryKey(stnw.getBuchungId());
					throw EJBExcFactory.buchungSteuersaetzeInKontenUnterschiedlich(dVonBis[0], dVonBis[1], stnw,
							buchungDto);
				}
				if (stnw.getSteuersatz().compareTo(d) != 0) {
					myLogger.warn("Steuersaetze nicht ident! " + stnw.getSteuersatz().toPlainString() + ", "
							+ d.toPlainString() + ".");
				}
			}
			data[i][REPORT_USTVERPROBUNG_RCART_ID] = stnw.getReversechargeartId();
			data[i][REPORT_USTVERPROBUNG_RCART_CNR] = stnw.getReversechargeartCnr();
			data[i][REPORT_USTVERPROBUNG_RCART_BEZ] = stnw.getReversechargeartCBez();
			data[i][REPORT_USTVERPROBUNG_UVAART_CNR] = stnw.getUvaartCnr();
			data[i][REPORT_USTVERPROBUNG_GEGENKONTO_UVAART_CNR] = stnw.getGegenkontoUvaartCnr();
			i++;
		}

		Map<String, Object> mapParameter = new HashMap<String, Object>();
		mapParameter.put(LPReport.P_WAEHRUNG, theClientDto.getSMandantenwaehrung());
		FinanzamtDto finanzamtDto = getFinanzFac().finanzamtFindByPartnerIIdMandantCNr(krit.getFinanzamtIId(),
				theClientDto.getMandant(), theClientDto);
		fillFinanzamtParameter(mapParameter, krit, finanzamtDto, dVonBis, theClientDto);

		String report = FinanzReportFac.REPORT_USTVERPROBUNG;
		initJRDS(mapParameter, FinanzReportFac.REPORT_MODUL, report, theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);
		return getReportPrint();
	}

	public JasperPrintLP printUstVerprobung0(ReportUvaKriterienDto krit, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {

		useCase = UC_USTVERPROBUNG;

		Map<Integer, SteuerkontoInfo> ustKontoIIds = getFinanzServiceFac()
				.getAllIIdsUstkontoMitIIdMwstBez(krit.getFinanzamtIId(), theClientDto);
		Map<Integer, SteuerkontoInfo> vstKontoIIds = getFinanzServiceFac()
				.getAllIIdsVstkontoMitIIdMwstBez(krit.getFinanzamtIId(), theClientDto);
		Map<Integer, SteuerkontoInfo> eustKontoIIds = getFinanzServiceFac()
				.getAllIIdsEUstkontoMitIIdMwstBez(krit.getFinanzamtIId(), theClientDto);

		Set<Integer> allMitlaufenden = getFinanzServiceFac().getAllMitlaufendeKonten(krit.getFinanzamtIId(),
				theClientDto);

		FinanzamtDto finanzamtDto = getFinanzFac().finanzamtFindByPartnerIIdMandantCNr(krit.getFinanzamtIId(),
				theClientDto.getMandant(), theClientDto);

		// TODO: runden implementieren: muss hier jede Zeile gerundet werden und
		// dann summiert,
		// oder erst summiert und dann die Summe gerundet?
		// gilt gleiches fuer's Kursumrechnen?
		// boolean bRunden =
		// Helper.short2boolean(finanzamtDto.getBUmsatzRunden());
		// String finanzamtwaehrung = finanzamtDto.getPartnerDto()
		// .getLandplzortDto().getLandDto().getWaehrungCNr();
		// boolean bUmrechnen = !finanzamtwaehrung.equals(theClientDto
		// .getSMandantenwaehrung());
		List<UstVerprobungRow> steuernachweisList = new ArrayList<UstVerprobungRow>();
		steuernachweisList.addAll(
				getVerprobungList(krit, ustKontoIIds, allMitlaufenden, FinanzServiceFac.STEUERART_UST, theClientDto));
		steuernachweisList.addAll(
				getVerprobungList(krit, vstKontoIIds, allMitlaufenden, FinanzServiceFac.STEUERART_VST, theClientDto));
		steuernachweisList.addAll(
				getVerprobungList(krit, eustKontoIIds, allMitlaufenden, FinanzServiceFac.STEUERART_EUST, theClientDto));

		final List<String> steuerartSort = new ArrayList<String>();
		steuerartSort.add(FinanzServiceFac.STEUERART_UST);
		steuerartSort.add(FinanzServiceFac.STEUERART_VST);
		steuerartSort.add(FinanzServiceFac.STEUERART_EUST);

		Collections.sort(steuernachweisList, new Comparator<UstVerprobungRow>() {
			@Override
			public int compare(UstVerprobungRow o1, UstVerprobungRow o2) {
				if (o1.getSteuerkategorieISort() != o2.getSteuerkategorieISort()) {
					return new Integer(o1.getSteuerkategorieISort()).compareTo(o2.getSteuerkategorieISort());
				}
				if (!o1.getSteuerart().equals(o2.getSteuerart())) {
					Integer i1 = steuerartSort.indexOf(o1.getSteuerart());
					Integer i2 = steuerartSort.indexOf(o2.getSteuerart());
					return i1.compareTo(i2);
				}
				if (o1.getSteuersatz() == null && o2.getSteuersatz() != null)
					return -1;
				if (o1.getSteuersatz() != null && o2.getSteuersatz() == null)
					return 1;
				if (o1.getSteuersatz() != null && !o1.getSteuersatz().equals(o2.getSteuersatz())) {
					return o1.getSteuersatz().compareTo(o2.getSteuersatz());
				}
				return o1.getGegenkontoCNr().compareTo(o2.getGegenkontoCNr());
			}
		});

		// Map<String, UstVerprobungRow> k = new HashMap<String,
		// UstVerprobungRow>();
		// for (UstVerprobungRow stnw : steuernachweisList) {
		// String key = "" + stnw.getSteuerkategorieISort() + "|" +
		// stnw.getSteuerart() + "|" + stnw.getSteuersatz().toPlainString() +
		// "|" + stnw.getGegenkontoCNr();
		// UstVerprobungRow f = k.get(key);
		// if(f == null) {
		// f = new UstVerprobungRow();
		// f.setSteuerkategorieISort(stnw.getSteuerkategorieISort());
		// f.setSteuerart(stnw.getSteuerart());
		// f.setSteuersatz(stnw.getSteuersatz());
		// f.setGegenkontoCNr(stnw.getGegenkontoCNr());
		// f.setBetrag(BigDecimal.ZERO);
		// f.setSteuer(BigDecimal.ZERO);
		// k.put(key, f);
		// }
		// f.setBetrag(f.getBetrag().add(stnw.getBetrag()));
		// f.setSteuer(f.getSteuer().add(stnw.getSteuer()));
		// }
		//
		// for (Map.Entry<String, UstVerprobungRow> r : k.entrySet()) {
		// myLogger.info("map " + r.getKey() + "| Betrag: " +
		// r.getValue().getBetrag().toPlainString() + ", Steuer: " +
		// r.getValue().getSteuer().toPlainString());
		// }

		List<UstVerprobungRow> kommuliert = new ArrayList<UstVerprobungRow>();
		for (UstVerprobungRow stnw : steuernachweisList) {
			if (kommuliert.size() != 0) {
				UstVerprobungRow last = kommuliert.get(kommuliert.size() - 1);
				if (stnw.equalsAusgenommenBetraege(last)) {
					if (stnw.getBetrag() != null) {
						last.setBetrag(last.getBetrag().add(stnw.getBetrag()));
					}
					last.setSteuer(last.getSteuer().add(stnw.getSteuer()));
					continue;
				}
			}

			kommuliert.add(stnw);
		}

		data = new Object[kommuliert.size()][REPORT_USTVERPROBUNG_ANZAHL_FELDER];
		int i = 0;
		for (UstVerprobungRow stnw : kommuliert) {
			data[i][REPORT_USTVERPROBUNG_BETRAG] = stnw.getBetrag();
			data[i][REPORT_USTVERPROBUNG_STEUER] = stnw.getSteuer();
			data[i][REPORT_USTVERPROBUNG_STEUERART] = stnw.getSteuerart();
			data[i][REPORT_USTVERPROBUNG_KONTO_BEZ] = stnw.getKontoCBez();
			data[i][REPORT_USTVERPROBUNG_KONTO_NR] = stnw.getKontoCNr();
			data[i][REPORT_USTVERPROBUNG_GEGENKONTO_BEZ] = stnw.getGegenkontoCBez();
			data[i][REPORT_USTVERPROBUNG_GEGENKONTO_NR] = stnw.getGegenkontoCNr();
			data[i][REPORT_USTVERPROBUNG_MWST_BEZ] = stnw.getMwstSatzBezCBez();
			data[i][REPORT_USTVERPROBUNG_MWST_SATZ] = stnw.getSteuersatz();
			data[i][REPORT_USTVERPROBUNG_MWST_ID] = stnw.getMwstSatzBezIId();
			data[i][REPORT_USTVERPROBUNG_STEUERKATEGORIE_BEZ] = stnw.getSteuerkategorieCBez();
			if (stnw.getBetrag() != null) {
				BigDecimal d = new BigDecimal(
						getMandantFac().getMwstSatzVonBruttoBetragUndUst(theClientDto.getMandant(), stnw.getDatum(),
								stnw.getBetrag().add(stnw.getSteuer()), stnw.getSteuer()).getFMwstsatz());
				data[i][REPORT_USTVERPROBUNG_MWST_SATZ_BERECHNET] = d;
				if (stnw.getSteuersatz().compareTo(d) != 0) {
					myLogger.warn("Steuersaetze nicht ident! " + stnw.getSteuersatz().toPlainString() + ", "
							+ d.toPlainString() + ".");
				}
			}
			data[i][REPORT_USTVERPROBUNG_RCART_ID] = stnw.getReversechargeartId();
			data[i][REPORT_USTVERPROBUNG_RCART_CNR] = stnw.getReversechargeartCnr();
			data[i][REPORT_USTVERPROBUNG_RCART_BEZ] = stnw.getReversechargeartCBez();
			i++;
		}

		Date[] dVonBis = getBuchenFac().getDatumbereichPeriodeGJ(krit.getIGeschaeftsjahr(), krit.getIPeriode(),
				theClientDto);

		Map<String, Object> mapParameter = new HashMap<String, Object>();
		mapParameter.put(LPReport.P_WAEHRUNG, theClientDto.getSMandantenwaehrung());
		fillFinanzamtParameter(mapParameter, krit, finanzamtDto, dVonBis, theClientDto);

		String report = FinanzReportFac.REPORT_USTVERPROBUNG;
		initJRDS(mapParameter, FinanzReportFac.REPORT_MODUL, report, theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);
		return getReportPrint();
	}

	private List<UstVerprobungRow> getVerprobungList(ReportUvaKriterienDto krit,
			Map<Integer, SteuerkontoInfo> steuerkontoIIds, Set<Integer> allMitlaufende, String steuerart,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		Timestamp[] tVonBis = getBuchenFac().getDatumbereichPeriodeGJUva(krit.getIGeschaeftsjahr(), krit.getIPeriode(),
				theClientDto);
		java.sql.Date tBeginn = new java.sql.Date(tVonBis[0].getTime());
		java.sql.Date tEnd = new java.sql.Date(tVonBis[1].getTime());

		Session session = FLRSessionFactory.getFactory().openSession();

		HvTypedCriteria<FLRFinanzBuchung> crit = new HvTypedCriteria<FLRFinanzBuchung>(
				session.createCriteria(FLRFinanzBuchungDetail.class));
		crit.createCriteria("flrbuchung").add(Restrictions.ge("d_buchungsdatum", tBeginn))
				.add(Restrictions.lt("d_buchungsdatum", tEnd))
				.add(Restrictions.eq("b_autombuchungeb", Helper.boolean2Short(false)))
				.add(Restrictions.eq("b_autombuchung", Helper.boolean2Short(false)))
				.add(Restrictions.isNull("t_storniert"));
		Set<Integer> allSteuerkontoIds = steuerkontoIIds.keySet();
		if (allSteuerkontoIds.size() == 0) {
			return new ArrayList<UstVerprobungRow>();
		}
		crit.add(Restrictions.in("flrkonto.i_id", allSteuerkontoIds))
				.setProjection(Projections.distinct(Projections.property("flrbuchung")));

		List<FLRFinanzBuchung> list = crit.list();
		List<UstVerprobungRow> ustVerprobungZeilen = new ArrayList<UstVerprobungRow>();
		for (FLRFinanzBuchung buchung : list) {
			ustVerprobungZeilen
					.addAll(zerlegeSteuerbuchungen(buchung, steuerkontoIIds, allMitlaufende, steuerart, theClientDto));
		}

		return ustVerprobungZeilen;
	}

	private Timestamp calculateBelegdatum(FLRFinanzBuchung buchung, String belegartCnr, TheClientDto theClientDto)
			throws RemoteException {
		if (LocaleFac.BELEGART_RECHNUNG.equals(belegartCnr) || LocaleFac.BELEGART_REZAHLUNG.equals(belegartCnr)) {
			BelegbuchungDto bbDto = getBelegbuchungFac(theClientDto.getMandant())
					.belegbuchungFindByBuchungIId(buchung.getI_id());
			if (bbDto == null) {
				return new Timestamp(buchung.getD_buchungsdatum().getTime());
			}

			Integer rechnungId = bbDto.getIBelegiid();
			if (LocaleFac.BELEGART_REZAHLUNG.equals(bbDto.getBelegartCNr())) {
				RechnungzahlungDto reZDto = getRechnungFac().rechnungzahlungFindByPrimaryKey(bbDto.getIBelegiid());
				rechnungId = reZDto.getRechnungIId();
			}
			RechnungDto reDto = getRechnungFac().rechnungFindByPrimaryKey(rechnungId);
			return reDto.getTBelegdatum();
		}
		if (LocaleFac.BELEGART_EINGANGSRECHNUNG.equals(belegartCnr)
				|| LocaleFac.BELEGART_ERZAHLUNG.equals(belegartCnr)) {
			BelegbuchungDto bbDto = getBelegbuchungFac(theClientDto.getMandant())
					.belegbuchungFindByBuchungIId(buchung.getI_id());
			if (bbDto == null) {
				return new Timestamp(buchung.getD_buchungsdatum().getTime());
			}

			Integer erId = bbDto.getIBelegiid();
			if (LocaleFac.BELEGART_ERZAHLUNG.equals(bbDto.getBelegartCNr())) {
				EingangsrechnungzahlungDto erZDto = getEingangsrechnungFac()
						.eingangsrechnungzahlungFindByPrimaryKey(bbDto.getIBelegiid());
				erId = erZDto.getEingangsrechnungIId();
			}
			EingangsrechnungDto erDto = getEingangsrechnungFac().eingangsrechnungFindByPrimaryKey(erId);
			return new Timestamp(erDto.getDBelegdatum().getTime());
		}
		return new Timestamp(buchung.getD_buchungsdatum().getTime());
	}

	private List<UstVerprobungRow> zerlegeUmbuchungSteuerbuchung(FLRFinanzBuchung buchung, BuchungdetailDto[] details,
			Map<Integer, SteuerkontoInfo> steuerkontoIIds, Set<Integer> allMitlaufende, String steuerartCnr,
			TheClientDto theClientDto) throws RemoteException {
		List<UstVerprobungRow> rows = new ArrayList<UstVerprobungRow>();

		for (int i = 0; i < details.length; i++) {
			SteuerkontoInfo skInfo = steuerkontoIIds.get(details[i].getKontoIId());
			if (skInfo == null)
				continue;
			if (details[i].getNBetrag().signum() == 0)
				continue;

			// vorherige Buchung ist die Betragsbuchung
			BuchungdetailDto steuerDetail = details[i];

			String debugPrefix = "Zerlegesteuerbuchung: Umbuchung index: " + i + "\n";

			myLogger.warn(debugPrefix + HelperServer.printBuchungssatz(details, getFinanzFac()));

			KontoDto konto = getFinanzFac().kontoFindByPrimaryKey(steuerDetail.getKontoIId());
			UvaartDto uvaartDto = getFinanzServiceFac().uvaartFindByPrimaryKey(konto.getUvaartIId(), theClientDto);
			UstVerprobungRow ustVp = new UstVerprobungRow();
			ustVp.setKontoDto(konto);
			ustVp.setGegenKontoDto(null);
			ustVp.setBuchungDetailId(steuerDetail.getIId());
			ustVp.setBuchungId(steuerDetail.getBuchungIId());
			ustVp.setBetrag(
					steuerDetail.isHabenBuchung() ? steuerDetail.getNBetrag() : steuerDetail.getNBetrag().negate());
			ustVp.setDatum(new Timestamp(buchung.getD_buchungsdatum().getTime()));
			ustVp.setBelegDatum(ustVp.getDatum());
			ustVp.setBuchungText(buchung.getC_text());
			ustVp.setBuchungBelegnummer(buchung.getC_belegnummer());
			ustVp.setBelegartCnr(buchung.getFlrfbbelegart() != null ? buchung.getFlrfbbelegart().getC_kbez() : "UB");

			ustVp.setGegenkontoCBez("");
			ustVp.setGegenkontoCNr("");
			ustVp.setSteuer(steuerDetail.getNBetrag());
			ustVp.setSteuerart(steuerartCnr);
			ustVp.setKontoCBez(konto.getCBez());
			ustVp.setKontoCNr(konto.getCNr());
			ustVp.setReversechargeartId(skInfo.getReversechargeartId());
			if (skInfo.getReversechargeartId() != null) {
				ReversechargeartDto rcartDto = getFinanzServiceFac()
						.reversechargeartFindByPrimaryKey(ustVp.getReversechargeartId(), theClientDto);
				ustVp.setReversechargeartCnr(rcartDto.getCNr());
				ustVp.setReversechargeartCBez(rcartDto.getSprDto().getcBez());
			}
			ustVp.setUvaartCnr(uvaartDto.getCNr());
			ustVp.setGegenkontoUvaartCnr(null);

			ustVp.setKontoMwstsatzId(konto.getMwstsatzIId());
			if (ustVp.getKontoMwstsatzId() != null) {
				MwstsatzDto mwstsatzDto = getMandantFac().mwstsatzFindByPrimaryKey(ustVp.getKontoMwstsatzId(),
						theClientDto);
				ustVp.setKontoSteuersatz(BigDecimal.valueOf(mwstsatzDto.getFMwstsatz()));
			}

			rows.add(ustVp);

			if (skInfo.getMwstsatzbezId() == null) {
				evLog.warn("Keine MwstsatzbezId", LogEventProducer.create(ustVp));

				List<MwstsatzbezDto> mwstsatzbezs = new ArrayList<MwstsatzbezDto>();
				List<Integer> mwstsatzbezIds = skInfo.getOtherMwstsatzbezId();
				for (Integer bezId : mwstsatzbezIds) {
					MwstsatzbezDto bezDto = getMandantFac().mwstsatzbezFindByPrimaryKey(bezId, theClientDto);
					mwstsatzbezs.add(bezDto);
				}

				Konto stKonto = em.find(Konto.class, skInfo.getKontoId());
				throw EJBExcFactory.steuerkontoMehrfachInMwstsatz(stKonto, mwstsatzbezs);
			}

			MwstsatzbezDto mwstSatzBez = getMandantFac()
					.mwstsatzbezFindByPrimaryKey(steuerkontoIIds.get(konto.getIId()).getMwstsatzbezId(), theClientDto);
			MwstsatzDto mwstSatz = getMandantFac().mwstsatzFindZuDatum(mwstSatzBez.getIId(), ustVp.getBelegDatum());
			ustVp.setMwstSatzBezCBez(mwstSatzBez.getCBezeichnung());
			ustVp.setMwstSatzBezIId(mwstSatzBez.getIId());
			ustVp.setSteuersatz(mwstSatz.getFMwstsatz());
			List<SteuerkategoriekontoDto> stkks = getFinanzServiceFac().steuerkategorieFindByKontoIIdMwstSatzBezIId(
					konto.getIId(), mwstSatzBez.getIId(), ustVp.getBelegDatum());
			if (stkks.size() != 1) {
				evLog.warn("steuerkategoriekonto.size() != 1(" + stkks.size() + ")", LogEventProducer.create(ustVp));
				continue;
			}
			SteuerkategorieDto stk = getFinanzServiceFac()
					.steuerkategorieFindByPrimaryKey(stkks.get(0).getSteuerkategorieIId(), theClientDto);
			ustVp.setSteuerkategorieCBez(stk.getCBez());
			ustVp.setSteuerkategorieISort(stk.getISort());

			evLog.warn(LogEventProducer.create(ustVp));
		}

		return rows;
	}

	private List<UstVerprobungRow> zerlegeSteuerbuchungen(FLRFinanzBuchung buchung,
			Map<Integer, SteuerkontoInfo> steuerkontoIIds, Set<Integer> allMitlaufende, String steuerart,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		BuchungdetailDto[] details = getBuchenFac().buchungdetailsFindByBuchungIId(buchung.getI_id());
		// HelperServer.printBuchungssatz(details, getFinanzFac(), System.out);
		String belegart = buchung.getFlrfbbelegart() == null ? null : buchung.getFlrfbbelegart().getC_nr();
		if (belegart == null) {
			if (FinanzFac.BUCHUNGSART_UMBUCHUNG.equals(buchung.getBuchungsart_c_nr())) {
				return zerlegeUmbuchungSteuerbuchung(buchung, details, steuerkontoIIds, allMitlaufende, steuerart,
						theClientDto);
			}
			if (FinanzFac.BUCHUNGSART_KASSENBUCHUNG.equals(buchung.getBuchungsart_c_nr())) {
				if (details.length == 2) {
					return zerlegeUmbuchungSteuerbuchung(buchung, details, steuerkontoIIds, allMitlaufende, steuerart,
							theClientDto);
				}
			}
			belegart = "Umbuchung?";
		}

		Timestamp belegDatum = calculateBelegdatum(buchung, belegart, theClientDto);
		List<UstVerprobungRow> ustVerprobungZeilen = new ArrayList<UstVerprobungRow>();
		for (int i = 1; i < details.length; i++) {
			SteuerkontoInfo skInfo = steuerkontoIIds.get(details[i].getKontoIId());
			if (skInfo == null)
				continue;
			if (details[i].getNBetrag().signum() == 0)
				continue;

			// vorherige Buchung ist die Betragsbuchung
			BuchungdetailDto betragDetail = details[i - 1];
			BuchungdetailDto steuerDetail = details[i];

			String belegnummer = buchung.getC_belegnummer().trim();
			/*
			 * if ("21/M00024".equals(belegnummer) ||
			 * "21/M00022".contentEquals(belegnummer)) { System.out.println("halt, " +
			 * belegnummer + ", buchungId:" + buchung.getI_id());
			 * System.out.println("betrag " + betragDetail.getNBetrag().toPlainString() +
			 * ", " + betragDetail.getBuchungdetailartCNr() + ", id: " +
			 * betragDetail.getIId()); System.out.println("steuer " +
			 * steuerDetail.getNBetrag().toPlainString() + ", " +
			 * steuerDetail.getBuchungdetailartCNr() + ", id: " + steuerDetail.getIId());
			 * buchung.getFlrbuchungsart().getC_nr(); buchung.getFlrfbbelegart().getC_nr();
			 * 
			 * if (betragDetail.isSollBuchung()) {
			 * betragDetail.setNBetrag(betragDetail.getNBetrag().negate());
			 * steuerDetail.setNBetrag(steuerDetail.getNBetrag().negate()); } }
			 */
			if (betragDetail.getNBetrag().signum() != steuerDetail.getNBetrag().signum()
					|| !betragDetail.getBuchungdetailartCNr().equals(steuerDetail.getBuchungdetailartCNr())) {
				// Bei einer Eingangsrechnung (ohne Mehrfachkontierung) haben
				// wir bereits alles
				// in allen anderen Faellen
				String debugPrefix = "Zerlegesteuerbuchung: '" + belegart + "', " + buchung.getC_belegnummer()
						+ ", index: " + i + "\n";

				myLogger.info(debugPrefix + HelperServer.printBuchungssatz(details, getFinanzFac()));

				if (!LocaleFac.BELEGART_EINGANGSRECHNUNG.equals(belegart)) {
					if (i < 2) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_UNGUELTIGE_STEUERBUCHUNG,
								createBuchungsInfoForException(buchung)
										+ HelperServer.printBuchungssatz(details, getFinanzFac()));
					}
					if (LocaleFac.BELEGART_RECHNUNG.equals(belegart)) {
						if (betragDetail.getNUst().signum() != 0) {
							myLogger.info("  Als Skontobuchung interpretiert.");
							// Betrag ist Brutto zu interpretieren, es war wohl
							// eine Debitorenbuchung
							// Die skontobuchung ist noch eine vorher
							betragDetail = details[i - 2];
						} else {
							// War es eventuell eine Anzahlungsbuchung?
							// Dann steht der Betrag in der naechsten(!) Zeile
							if (i + 1 < details.length) {
								BuchungdetailDto anzDetail = details[i + 1];
								if (anzDetail.getNUst().compareTo(steuerDetail.getNBetrag().abs()) == 0) {
									betragDetail = anzDetail;

									BigDecimal betrag = betragDetail.getNBetrag();
									BuchungDto buchungDto = getBuchenFac()
											.buchungFindByPrimaryKey(betragDetail.getBuchungIId());
									if (!buchungDto.isSchlussrechnungGegenbuchung()) {
										betrag = betrag.negate();
									}
									betragDetail.setNBetrag(betrag);

									myLogger.info("  DetailId: " + betragDetail.getIId() + ", betrag: "
											+ betrag.toPlainString() + ", " + betragDetail.getBuchungdetailartCNr());
								} else {
									myLogger.error(debugPrefix + "Konnte keine passende Betragsbuchung zu index: " + i
											+ " finden!\n" + HelperServer.printBuchungssatz(details, getFinanzFac()));
								}
							} else {
								myLogger.error(debugPrefix + "Vorzeitiges Detailende bei index: " + i + "!\n"
										+ HelperServer.printBuchungssatz(details, getFinanzFac()));
							}
						}
					} else {
						betragDetail = details[i - 2];
					}
				} else {
					Konto kreditor = em.find(Konto.class, betragDetail.getKontoIId());
					if (FinanzServiceFac.KONTOTYP_KREDITOR.equals(kreditor.getKontotypCNr())) {
						// war das moeglicherweise die Rueckverrechnung der AZ?
						if (i + 1 < details.length) {
							BuchungdetailDto anzDto = details[i + 1];
							if (allMitlaufende.contains(anzDto.getKontoIId())) {
								// Direkte Steuerbuchung (Kreditor an Steuer)
								myLogger.warn("  Direkte Steuerbuchung Kreditor an Steuerkonto erkannt");
								betragDetail = anzDto;
								betragDetail.setKontoIId(null);
								betragDetail.setNBetrag(null);
							} else {
								if (steuerDetail.getNBetrag().signum() < 0 && anzDto.getNBetrag().signum() > 0) {
									betragDetail = anzDto;
									betragDetail.setNBetrag(betragDetail.getNBetrag().negate());
									myLogger.info("  Eingangsrechnung, Anzahlungsbetrag erkannt");
								} else {
									if (betragDetail.getNUst().signum() == 0) {
										myLogger.info("  Eingangsrechnung, reines Steuerdetail erkannt");
										betragDetail.setKontoIId(null);
										betragDetail.setNBetrag(null);
									} else {
										myLogger.warn(
												"  Eingangsrechnung, und keine Ruecknahme der AZ? Betrag moeglicherweise falsch");
									}
								}
							}
						} else {
							myLogger.warn("  Vorzeitiges Ende der Buchungsdetails bei ER");
						}
					}
				}
			} else {
				if (LocaleFac.BELEGART_SCHLUSSRECHNUNG.equals(belegart)
						&& FinanzFac.BUCHUNGSART_BANKBUCHUNG.equals(buchung.getFlrbuchungsart().getC_nr())) {
					myLogger.info("Beleg " + belegnummer + ", buchungId:" + buchung.getI_id() + ", buchungsbelegart: "
							+ buchung.getFlrfbbelegart().getC_nr() + ", buchungsart: "
							+ buchung.getFlrbuchungsart().getC_nr());

					myLogger.info("Betrag " + betragDetail.getNBetrag().toPlainString() + ", "
							+ betragDetail.getBuchungdetailartCNr() + ", id: " + betragDetail.getIId());
					myLogger.info("Steuer " + steuerDetail.getNBetrag().toPlainString() + ", "
							+ steuerDetail.getBuchungdetailartCNr() + ", id: " + steuerDetail.getIId());

					betragDetail.setNBetrag(betragDetail.getNBetrag().negate());
					steuerDetail.setNBetrag(steuerDetail.getNBetrag().negate());

				}
			}

			KontoDto konto = getFinanzFac().kontoFindByPrimaryKey(steuerDetail.getKontoIId());
			UvaartDto uvaartDto = getFinanzServiceFac().uvaartFindByPrimaryKey(konto.getUvaartIId(), theClientDto);
			KontoDto gegenkonto = betragDetail.getKontoIId() != null
					? getFinanzFac().kontoFindByPrimaryKey(betragDetail.getKontoIId())
					: null;
			UvaartDto gegenUvaartDto = gegenkonto != null && gegenkonto.getUvaartIId() != null
					? getFinanzServiceFac().uvaartFindByPrimaryKey(gegenkonto.getUvaartIId(), theClientDto)
					: null;
			UstVerprobungRow ustVp = new UstVerprobungRow();
			ustVp.setKontoDto(konto);
			ustVp.setGegenKontoDto(gegenkonto);
			ustVp.setBuchungDetailId(betragDetail.getIId());
			ustVp.setBuchungId(betragDetail.getBuchungIId());
			ustVp.setBetrag(betragDetail.getNBetrag());
			ustVp.setDatum(new Timestamp(buchung.getD_buchungsdatum().getTime()));
			ustVp.setBelegDatum(belegDatum);
			ustVp.setBuchungText(buchung.getC_text());
			ustVp.setBuchungBelegnummer(buchung.getC_belegnummer());
			ustVp.setBelegartCnr(buchung.getFlrfbbelegart() != null ? buchung.getFlrfbbelegart().getC_kbez() : "UB");

			ustVp.setGegenkontoCBez(gegenkonto != null ? gegenkonto.getCBez() : "");
			ustVp.setGegenkontoCNr(gegenkonto != null ? gegenkonto.getCNr() : "");
			ustVp.setSteuer(steuerDetail.getNBetrag());
			ustVp.setSteuerart(steuerart);
			ustVp.setKontoCBez(konto.getCBez());
			ustVp.setKontoCNr(konto.getCNr());
			ustVp.setReversechargeartId(skInfo.getReversechargeartId());
			if (skInfo.getReversechargeartId() != null) {
				ReversechargeartDto rcartDto = getFinanzServiceFac()
						.reversechargeartFindByPrimaryKey(ustVp.getReversechargeartId(), theClientDto);
				ustVp.setReversechargeartCnr(rcartDto.getCNr());
				ustVp.setReversechargeartCBez(rcartDto.getSprDto().getcBez());
			}
			ustVp.setUvaartCnr(uvaartDto.getCNr());
			ustVp.setGegenkontoUvaartCnr(gegenUvaartDto != null ? gegenUvaartDto.getCNr() : null);

			ustVp.setKontoMwstsatzId(konto.getMwstsatzIId());
			if (ustVp.getKontoMwstsatzId() != null) {
				MwstsatzDto mwstsatzDto = getMandantFac().mwstsatzFindByPrimaryKey(ustVp.getKontoMwstsatzId(),
						theClientDto);
				ustVp.setKontoSteuersatz(BigDecimal.valueOf(mwstsatzDto.getFMwstsatz()));
			}
			ustVp.setGegenkontoMwstsatzId(gegenkonto != null ? gegenkonto.getMwstsatzIId() : null);
			if (ustVp.getGegenkontoMwstsatzId() != null) {
				if (ustVp.getGegenkontoMwstsatzId().equals(ustVp.getKontoMwstsatzId())) {
					ustVp.setGegenkontoSteuersatz(ustVp.getKontoSteuersatz());
				} else {
					MwstsatzDto mwstsatzDto = getMandantFac().mwstsatzFindByPrimaryKey(ustVp.getGegenkontoMwstsatzId(),
							theClientDto);
					ustVp.setGegenkontoSteuersatz(BigDecimal.valueOf(mwstsatzDto.getFMwstsatz()));
				}
			}
			ustVerprobungZeilen.add(ustVp);
			// evLog.warn(LogEventProducer.create(ustVp));

			if (skInfo.getMwstsatzbezId() == null) {
				evLog.warn("Keine MwstsatzbezId", LogEventProducer.create(ustVp));

				List<MwstsatzbezDto> mwstsatzbezs = new ArrayList<MwstsatzbezDto>();
				List<Integer> mwstsatzbezIds = skInfo.getOtherMwstsatzbezId();
				for (Integer bezId : mwstsatzbezIds) {
					MwstsatzbezDto bezDto = getMandantFac().mwstsatzbezFindByPrimaryKey(bezId, theClientDto);
					mwstsatzbezs.add(bezDto);
				}

				Konto stKonto = em.find(Konto.class, skInfo.getKontoId());
				throw EJBExcFactory.steuerkontoMehrfachInMwstsatz(stKonto, mwstsatzbezs);
			}

			MwstsatzbezDto mwstSatzBez = getMandantFac()
					.mwstsatzbezFindByPrimaryKey(steuerkontoIIds.get(konto.getIId()).getMwstsatzbezId(), theClientDto);
			MwstsatzDto mwstSatz = getMandantFac().mwstsatzFindZuDatum(mwstSatzBez.getIId(), ustVp.getBelegDatum());
			ustVp.setMwstSatzBezCBez(mwstSatzBez.getCBezeichnung());
			ustVp.setMwstSatzBezIId(mwstSatzBez.getIId());
			ustVp.setSteuersatz(mwstSatz.getFMwstsatz());
			List<SteuerkategoriekontoDto> stkks = getFinanzServiceFac().steuerkategorieFindByKontoIIdMwstSatzBezIId(
					konto.getIId(), mwstSatzBez.getIId(), ustVp.getBelegDatum());
			if (stkks.size() != 1) {
				evLog.warn("steuerkategoriekonto.size() != 1(" + stkks.size() + ")", LogEventProducer.create(ustVp));
				continue;
			}
			SteuerkategorieDto stk = getFinanzServiceFac()
					.steuerkategorieFindByPrimaryKey(stkks.get(0).getSteuerkategorieIId(), theClientDto);
			ustVp.setSteuerkategorieCBez(stk.getCBez());
			ustVp.setSteuerkategorieISort(stk.getISort());

			evLog.warn(LogEventProducer.create(ustVp));
		}

		return ustVerprobungZeilen;
	}

	private Collection<UstVerprobungRow> getUstVerprobungOhneAnzahlung(ReportUvaKriterienDto krit,
			FinanzamtDto finanzamtDto, TheClientDto theClientDto) throws RemoteException {
		final List<String> excludeCnrs = new ArrayList<String>();
		if (finanzamtDto.getKontoIIdAnzahlungErhaltBezahlt() != null) {
			excludeCnrs.add(getKontoCnr(finanzamtDto.getKontoIIdAnzahlungErhaltBezahlt()));
		}
		if (finanzamtDto.getKontoIIdAnzahlungErhaltVerr() != null) {
			excludeCnrs.add(getKontoCnr(finanzamtDto.getKontoIIdAnzahlungErhaltVerr()));
		}
		List<UstVerprobungRow> ustVerprobungsAll = getRawUstVerprobung(krit, theClientDto,
				new String[] { FinanzServiceFac.STEUERART_UST });
		List<UstVerprobungCompacted> compacted = createCompacted(ustVerprobungsAll);

		Collection<UstVerprobungCompacted> ustVerprobungs = CollectionTools.reject(compacted,
				new IReject<UstVerprobungCompacted>() {
					@Override
					public boolean reject(UstVerprobungCompacted element) {
						for (String excludeCnr : excludeCnrs) {
							if (excludeCnr.equals(element.getCompacted().getGegenkontoCNr())) {
								return true;
							}
						}
						return false;
					}
				});
		List<UstVerprobungRow> rows = new ArrayList<UstVerprobungRow>();
		for (UstVerprobungCompacted row : ustVerprobungs) {
			rows.add(row.getCompacted());
		}
		return rows;
	}

	private Collection<UstVerprobungRow> getUstVerprobungOhneAnzahlung0(ReportUvaKriterienDto krit,
			FinanzamtDto finanzamtDto, TheClientDto theClientDto) throws RemoteException {
		final List<String> excludeCnrs = new ArrayList<String>();
		if (finanzamtDto.getKontoIIdAnzahlungErhaltBezahlt() != null) {
			excludeCnrs.add(getKontoCnr(finanzamtDto.getKontoIIdAnzahlungErhaltBezahlt()));
		}
		if (finanzamtDto.getKontoIIdAnzahlungErhaltVerr() != null) {
			excludeCnrs.add(getKontoCnr(finanzamtDto.getKontoIIdAnzahlungErhaltVerr()));
		}
		List<UstVerprobungRow> ustVerprobungsAll = getRawUstVerprobung(krit, theClientDto,
				new String[] { FinanzServiceFac.STEUERART_UST });

		Collection<UstVerprobungRow> ustVerprobungs = CollectionTools.reject(ustVerprobungsAll,
				new IReject<UstVerprobungRow>() {
					@Override
					public boolean reject(UstVerprobungRow element) {
						for (String excludeCnr : excludeCnrs) {
							if (excludeCnr.equals(element.getGegenkontoCNr())) {
								return true;
							}
						}
						return false;
					}
				});
		return ustVerprobungs;
	}

	private String getKontoCnr(Integer kontoId) throws RemoteException {
		if (kontoId == null)
			return null;
		KontoDto kontoDto = getFinanzFac().kontoFindByPrimaryKey(kontoId);
		return kontoDto.getCNr();
	}

	private Integer[] getPeriodenFuerAbrechnungszeitraum(ReportUvaKriterienDto krit) {
		if (FinanzFac.UVA_ABRECHNUNGSZEITRAUM_MONAT.equals(krit.getSAbrechnungszeitraum())) {
			return new Integer[] { krit.getIPeriode() };
		}
		if (FinanzFac.UVA_ABRECHNUNGSZEITRAUM_QUARTAL.equals(krit.getSAbrechnungszeitraum())) {
			// krit.getIPeriode entspricht dem Quartal
			return HelperServer.getMonateFuerQuartal(krit.getIPeriode());
		}

		return new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
	}

	private void createFinanzamtsbuchungen(ReportUvaKriterienDto krit, TheClientDto theClientDto)
			throws RemoteException {
		// Finanzamtsbuchungen wenn noch keine Verprobung vorhanden
		// und keine Geschaeftsjahrsperre!
		GeschaeftsjahrMandantDto gjm = getSystemFac().geschaeftsjahrFindByPrimaryKeyOhneExc(krit.getIGeschaeftsjahr(),
				theClientDto.getMandant());
		if (gjm.getTSperre() != null)
			return;

		if (!FinanzFac.UVA_ABRECHNUNGSZEITRAUM_JAHR.equals(krit.getSAbrechnungszeitraum())) {
			UvaverprobungDto uvapDto = getFinanzServiceFac()
					.uvaVerprobungFindbyFinanzamtIIdGeschaeftsjahrPeriodeAbrechnungszeitraumMandant(
							krit.getFinanzamtIId(), krit.getIGeschaeftsjahr(), krit.getIPeriode(),
							UvaverprobungDto.getAbrechnungszeitraumFromString(krit.getSAbrechnungszeitraum()),
							theClientDto);
			if (uvapDto == null) {
				// Finanzamtsbuchungen fuer die entsprechenden Monate
				for (Integer periode : getPeriodenFuerAbrechnungszeitraum(krit)) {
					getFinanzServiceFac().createFinanzamtsbuchungen(krit.getIGeschaeftsjahr(), periode,
							krit.getFinanzamtIId(), theClientDto);
				}
			}
		}
	}

	private List<UvaRpt> buildUvaRptFromSaldo(Map<String, Object> mapParameter, FinanzamtDto finanzamtDto,
			UvaartDto uvaartDto, SammelSaldoInfoDto sammelSaldo, boolean runden, TheClientDto theClientDto) {
		List<UvaRpt> rows = new ArrayList<UvaRpt>();

		if (sammelSaldo.entrySet().size() == 0) {
			SaldoInfoDto emptySaldo = new SaldoInfoDto();
			emptySaldo.setUva(new KontoUvaVariante());
			HvOptional<UvaRpt> emptyRpt = buildUvaRptFromSaldo(mapParameter, finanzamtDto, emptySaldo, uvaartDto,
					runden, false, theClientDto);
			rows.add(emptyRpt.get());
			return rows;
		}

		for (Map.Entry<KontoUvaVariante, SaldoInfoDto[]> entry : sammelSaldo.entrySet()) {
			SaldoInfoDto[] saldo = entry.getValue();
			HvOptional<UvaRpt> baseRpt = buildUvaRptFromSaldo(mapParameter, finanzamtDto, saldo[0], uvaartDto, runden,
					false, theClientDto);
			rows.add(baseRpt.get());

			HvOptional<UvaRpt> altRpt = buildUvaRptFromSaldo(mapParameter, finanzamtDto, saldo[1], uvaartDto, runden,
					true, theClientDto);
			if (altRpt.isPresent()) {
				rows.add(altRpt.get());
			}
		}

		return rows;
	}

	private void createUvaRptFromSaldo(Map<String, Object> mapParameter, UvaartDto uvaartDto,
			SammelSaldoInfoDto sammelSaldo, boolean runden) {
		/*
		 * for (Map.Entry<KontoUvaVariante, SaldoInfoDto[]> entry :
		 * sammelSaldo.entrySet()) { SaldoInfoDto[] saldo = entry.getValue();
		 * HvOptional<UvaRpt> baseRpt = buildUvaRptFromSaldo( saldo[0], uvaartDto,
		 * runden, false); mapParameter.put("P_" + saldo[0].getUva().getVariante() + "_"
		 * + uvaartDto.getCnrUppercase(), baseRpt.get());
		 * 
		 * HvOptional<UvaRpt> altRpt = buildUvaRptFromSaldo( saldo[1], uvaartDto,
		 * runden, true); if(altRpt.isPresent()) { mapParameter.put("P_" +
		 * saldo[1].getUva().getVariante() + "_" + uvaartDto.getCnrUppercase() + "_ALT",
		 * altRpt.get()); } }
		 */

		/*
		 * 0 UvaRpt uva = new UvaRpt(uvaartDto.getCKennzeichen(), salden[0].getSaldo(),
		 * runden ? salden[0].getGerundetenSaldo() : null);
		 * uva.setMwstsatzDto(salden[0].getSatzDto()); mapParameter.put("P_" +
		 * uvaartDto.getCnrUppercase(), uva);
		 * 
		 * if (salden[1].getSaldo().signum() != 0) { uva = new
		 * UvaRpt(uvaartDto.getCKennzeichen(), salden[1].getSaldo(), runden ?
		 * salden[1].getGerundetenSaldo() : null);
		 * uva.setMwstsatzDto(salden[1].getSatzDto()); mapParameter.put("P_" +
		 * uvaartDto.getCnrUppercase() + "_ALT", uva); }
		 */
	}

	private HvOptional<UvaRpt> buildUvaRptFromSaldo(Map<String, Object> mapParameter, FinanzamtDto finanzamtDto,
			SaldoInfoDto saldo, UvaartDto uvaartDto, boolean runden, boolean alternate, TheClientDto theClientDto) {
		if (alternate && saldo.getSaldo().signum() == 0) {
			return HvOptional.empty();
		}

		UvaRpt uva = new UvaRpt(uvaartDto.getCKennzeichen(), saldo.getSaldo(),
				runden ? saldo.getGerundetenSaldo() : null);
		uva.setMwstsatzDto(saldo.getSatzDto());
		uva.setKontoUvavariante(saldo.getUva().getVariante());
		uva.setUvaartCnr(uvaartDto.getCNr());

		if (uva.getMwstsatzDto() == null && uva.getKontoUvavariante() != 0) {
			MwstsatzDto satzDto = getMandantFac().mwstsatzFindByPrimaryKey(uva.getKontoUvavariante(), theClientDto);
			uva.setMwstsatzDto(satzDto);
		}

		HvOptional<UvaFormularDto> uvaformDto = getFinanzServiceFac().uvaFormularFindByFinanzamt(
				finanzamtDto.getPartnerIId(), finanzamtDto.getMandantCNr(), new UvaartId(uvaartDto.getIId()));
		if (uvaformDto.isPresent()) {
			UvaFormularDto formDto = uvaformDto.get();
			if (!Helper.isStringEmpty(formDto.getCKennzeichen())) {
				uva.setKennzeichen(formDto.getCKennzeichen());
			}
			uva.setGruppe(formDto.getIGruppe());
			uva.setSort(formDto.getISort());
		}
		uva.setAlternativ(alternate);

		String suffix = alternate ? "_ALT" : "";
		// kompatibel zur alten Variante
		mapParameter.put("P_" + uvaartDto.getCnrUppercase() + suffix, uva);

		// mit Kontouvavariante
		mapParameter.put("P_" + uva.getKontoUvavariante() + "_" + uvaartDto.getCnrUppercase() + suffix, uva);

		return HvOptional.of(uva);
	}

	private void buildAnzahlungsSaldo(List<UvaRpt> uvaRows, String uvaartCnr, String anzahlungsUvaartCnr) {
		for (UvaRpt uvaRpt : uvaRows) {
			if (!uvaartCnr.equals(uvaRpt.getUvaartCnr())) {
				continue;
			}

			for (UvaRpt anzUvaRpt : uvaRows) {
				if (anzahlungsUvaartCnr.equals(anzUvaRpt.getUvaartCnr())
						&& uvaRpt.getKontoUvavariante() == anzUvaRpt.getKontoUvavariante()) {
					uvaRpt.setAnzahlungsSaldo(anzUvaRpt.getSaldo());
					uvaRpt.setAnzahlungsSaldoGerundet(anzUvaRpt.getSaldogerundet());
				}
			}
		}
	}

	public JasperPrintLP printUva(String mandantCNr, ReportUvaKriterienDto krit, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		try {
			this.useCase = UC_UVA;
			this.index = -1;

			pruefeSteuerkontenUva(krit, theClientDto);

			createFinanzamtsbuchungen(krit, theClientDto);

			UvaartDto[] uvas = getFinanzServiceFac().uvaartFindAll(mandantCNr);
			Map<String, Object> mapParameter = new TreeMap<String, Object>();

			// Betraege werden in Waehrung des Finanzamts (aus Land des
			// Finanzamts) umgerechnet
			FinanzamtDto finanzamtDto = getFinanzFac()
					.finanzamtFindByPartnerIIdMandantCNrOhneExc(krit.getFinanzamtIId(), mandantCNr, theClientDto);
			boolean bUmrechnen = false;
			boolean bRunden = false;
			String finanzamtwaehrung = null;
			if (finanzamtDto != null) {
				bRunden = Helper.short2boolean(finanzamtDto.getBUmsatzRunden());
				finanzamtwaehrung = finanzamtDto.getPartnerDto().getLandplzortDto().getLandDto().getWaehrungCNr();
				bUmrechnen = !finanzamtwaehrung.equals(theClientDto.getSMandantenwaehrung());
			}

			Integer[] iPerioden = getPeriodenFuerAbrechnungszeitraum(krit);

			ArrayList<FibuFehlerDto> errors = new ArrayList<FibuFehlerDto>();
			for (Integer periode : iPerioden) {
				errors.addAll(getFinanzServiceFac().pruefeBelegePeriode(krit.getIGeschaeftsjahr(), periode, true,
						theClientDto));
			}
			mapParameter.put("P_BELEGFEHLER", new Boolean(errors.size() > 0));
			mapParameter.put("P_BELEGFEHLER_SUBREPORT", new LPFibuFehlerSubreport(errors));

			List<UvaRpt> rows = new ArrayList<UvaRpt>();
			// Salden fuer alle UVA Arten
			for (int i = 0; i < uvas.length; i++) {
				if (uvas[i].getCNr().equals(FinanzServiceFac.UVAART_NICHT_ZUTREFFEND))
					continue;
				List<FLRFinanzKonto> uvaKonten = getKontenJeUvaart(uvas[i].getIId(), krit.getFinanzamtIId());

				myLogger.warn("Berechne UvaSaldo/Sammelsaldo fuer '" + uvas[i].getCNr() + "'...");
				SammelSaldoInfoDto salden = calculateUvaSaldo(uvaKonten, iPerioden, krit, theClientDto);
				if (bUmrechnen) {
					umrechnen(salden, finanzamtwaehrung, krit, theClientDto);
				}

				if (Helper.isTrue(uvas[i].getBInvertiert())) {
					salden.negateSaldo();
				}
				rows.addAll(buildUvaRptFromSaldo(mapParameter, finanzamtDto, uvas[i], salden, bRunden, theClientDto));
//				createUvaRptFromSaldo(mapParameter, uvas[i], salden, bRunden);
			}

			buildAnzahlungsSaldo(rows, FinanzServiceFac.UVAART_INLAND_10, FinanzServiceFac.UVAART_ZAHLUNG_10);
			buildAnzahlungsSaldo(rows, FinanzServiceFac.UVAART_INLAND_20, FinanzServiceFac.UVAART_ZAHLUNG_20);

			Collection<UstVerprobungRow> ustVerprobungs = getUstVerprobungOhneAnzahlung(krit, finanzamtDto,
					theClientDto);
			for (UstVerprobungRow r : ustVerprobungs) {
				evLog.warn("UstV-Saldo", LogEventProducer.create(r));
			}

			Date[] dVonBis = null;
			int fibuPeriode = 0;
			if (krit.getSAbrechnungszeitraum().equals(FinanzFac.UVA_ABRECHNUNGSZEITRAUM_QUARTAL)) {
				fibuPeriode = 1 + (krit.getIPeriode() - 1) * 3;
				// dVonBis = getBuchenFac().getDatumbereichPeriodeGJ(
				// krit.getIGeschaeftsjahr(),
				// 1 + (krit.getIPeriode() - 1) * 3, theClientDto);
			} else if (krit.getSAbrechnungszeitraum().equals(FinanzFac.UVA_ABRECHNUNGSZEITRAUM_JAHR)) {
				fibuPeriode = 12;
				// dVonBis = getBuchenFac().getDatumbereichPeriodeGJ(
				// krit.getIGeschaeftsjahr(), 12, theClientDto);
			} else {
				fibuPeriode = krit.getIPeriode();
				// dVonBis = getBuchenFac().getDatumbereichPeriodeGJ(
				// krit.getIGeschaeftsjahr(), krit.getIPeriode(),
				// theClientDto);
			}
			dVonBis = getBuchenFac().getDatumbereichPeriodeGJ(krit.getIGeschaeftsjahr(), fibuPeriode, theClientDto);

			Timestamp tDatum = new Timestamp(dVonBis[0].getTime());

			// Mandantenwaehrung
			if (bUmrechnen)
				mapParameter.put(LPReport.P_WAEHRUNG, finanzamtwaehrung);
			else
				mapParameter.put(LPReport.P_WAEHRUNG, theClientDto.getSMandantenwaehrung());

			boolean bHauptfinanzamt = false;

			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(mandantCNr, theClientDto);
			MwstsatzbezDto[] mwstbezs = getMandantFac().mwstsatzbezFindAllByMandantAsDto(mandantCNr, theClientDto);
			if (mandantDto.getPartnerIIdFinanzamt() != null)
				bHauptfinanzamt = finanzamtDto.getPartnerIId().equals(mandantDto.getPartnerIIdFinanzamt());
			for (int i = 0; i < mwstbezs.length; i++) {
				if ((bHauptfinanzamt ? mwstbezs[i].getFinanzamtIId() == null : false)
						|| (!(mwstbezs[i].getFinanzamtIId() == null)
								&& mwstbezs[i].getFinanzamtIId().equals(finanzamtDto.getPartnerIId()))) {
					if (!mwstbezs[i].getBHandeingabe()) {
						if (mwstbezs[i].getCDruckname() != null) {
							MwstsatzDto mwstDto = getMandantFac().mwstsatzFindZuDatum(mwstbezs[i].getIId(), tDatum);
							if (mwstDto != null) {
								String druckname = mwstbezs[i].getCDruckname().toUpperCase().replace(" ", "_");
								mapParameter.put("P_" + druckname, mwstDto.getFMwstsatz());

								BigDecimal st = new BigDecimal(mwstDto.getFMwstsatz());
								BigDecimal total = BigDecimal.ZERO;
								for (UstVerprobungRow ustrow : ustVerprobungs) {
									BigDecimal steuersatz = ustrow.getSteuersatz();
									if (steuersatz != null && st.compareTo(ustrow.getSteuersatz()) == 0) {
										total = total.add(ustrow.getBetrag());
									}
								}

								mapParameter.put("P_USTVBetrag_" + druckname, total);
							}
						}
					}
				}
			}

			fillFinanzamtParameter(mapParameter, krit, finanzamtDto, dVonBis, theClientDto);

			BigDecimal saldo = getBuchenFac().getSaldoUstOhneZahllast(krit.getIGeschaeftsjahr(), fibuPeriode,
					krit.getFinanzamtIId(), theClientDto);
			BigDecimal saldogerundet = null;
			if (saldo != null) {
				saldo = saldo.negate();
				saldogerundet = bRunden ? Helper.rundeKaufmaennisch(saldo, 0) : null;
			}

			UvaRpt fakedUvaZahllastkonto = new UvaRpt("", saldo, saldogerundet);
			mapParameter.put("P_ZAHLLASTKONTO", fakedUvaZahllastkonto);

//			data = new Object[0][0];
			buildReportData(rows);

			// Formularnummer anhaengen, wenn vorhanden
			String report = FinanzReportFac.REPORT_UVA;
			if (finanzamtDto != null) {
				if (finanzamtDto.getIFormularnummer() != null) {
					report = report.replace(".", finanzamtDto.getIFormularnummer() + ".");
				}
			}
			initJRDS(mapParameter, FinanzReportFac.REPORT_MODUL, report, mandantCNr, theClientDto.getLocUi(),
					theClientDto);

			JasperPrintLP print = getReportPrint();
			PrintInfoDto values = new PrintInfoDto();
			values.setDocPath(new DocPath(new DocNodeUVAVerprobung(krit, finanzamtDto)));
			// values[0] = JCRDocFac.HELIUMV_NODE + "/"
			// + LocaleFac.BELEGART_FINANZBUCHHALTUNG.trim() + "/"
			// + krit.getIGeschaeftsjahr() + "/UVA/"
			// + finanzamtDto.getPartnerDto().formatName() + "/"
			// + krit.getIPeriode();
			values.setiId(theClientDto.getIDPersonal());
			values.setTable("");

			print.setOInfoForArchive(values);

			return print;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	private void buildReportData(List<UvaRpt> rows) {
		Collections.sort(rows, new Comparator<UvaRpt>() {
			@Override
			public int compare(UvaRpt o1, UvaRpt o2) {
				if (o1 == null && o2 == null)
					return 0;
				if (o1 == null && o2 != null)
					return 1;
				if (o1 != null && o2 == null)
					return -1;

				int g1 = o1.getGruppe() == null ? 0 : o1.getGruppe();
				int g2 = o2.getGruppe() == null ? 0 : o2.getGruppe();
				if (g1 < g2)
					return -1;
				if (g1 > g2)
					return 1;

				int s1 = o1.getSort() == null ? 0 : o1.getSort();
				int s2 = o2.getSort() == null ? 0 : o2.getSort();
				if (s1 < s2)
					return -1;
				if (s1 > s2)
					return 1;

				if (o1.getKontoUvavariante() < o2.getKontoUvavariante())
					return -1;
				if (o1.getKontoUvavariante() > o2.getKontoUvavariante())
					return 1;

				if (!o1.isAlternativ() && o2.isAlternativ())
					return -1;
				if (o1.isAlternativ() && !o2.isAlternativ())
					return 1;
				return 0;
			}
		});

		data = new Object[rows.size()][REPORT_UVA_ANZAHL_SPALTEN];
		int i = 0;
		for (UvaRpt uvaRpt : rows) {
			data[i][REPORT_UVA_UVARPT] = uvaRpt;
			data[i][REPORT_UVA_UVAART] = uvaRpt.getUvaartCnr();
			data[i][REPORT_UVA_KENNZAHL] = uvaRpt.getKennzeichen();
			data[i][REPORT_UVA_KONTOVARIANTE] = new Integer(uvaRpt.getKontoUvavariante());
			data[i][REPORT_UVA_UMSATZ] = uvaRpt.getSaldo();
			data[i][REPORT_UVA_UMSATZGERUNDET] = uvaRpt.getSaldogerundet();
			data[i][REPORT_UVA_SATZ] = uvaRpt.getSteuersatz();
			data[i][REPORT_UVA_GRUPPE] = uvaRpt.getGruppe();
			data[i][REPORT_UVA_MWSTSATZ] = uvaRpt.getMwstsatzDto();
			data[i][REPORT_UVA_SORT] = uvaRpt.getSort();
			data[i][REPORT_UVA_ALTERNATIVE] = new Boolean(uvaRpt.isAlternativ());
			data[i][REPORT_UVA_ANZAHLUNG_UMSATZ] = uvaRpt.getAnzahlungsSaldo();
			data[i][REPORT_UVA_ANZAHLUNG_UMSATZGERUNDET] = uvaRpt.getAnzahlungsSaldoGerundet();

//			myLogger.warn("uvarpt: " + data[i][REPORT_UVA_GRUPPE] + "." +
//					data[i][REPORT_UVA_SORT] + ", '" + data[i][REPORT_UVA_UVAART] + "': " +
//					(data[i][REPORT_UVA_SATZ]) + ", " + data[i][REPORT_UVA_MWSTSATZ]);
			i++;
		}
	}

	private SammelSaldoInfoDto calculateUvaSaldo(List<FLRFinanzKonto> uvaKonten, Integer[] perioden,
			ReportUvaKriterienDto krit, TheClientDto theClientDto) {
		SammelSaldoInfoDto sammelSaldo = new SammelSaldoInfoDto();
		Integer originalPeriode = krit.getIPeriode();
		for (Integer periode : perioden) {
			krit.setIPeriode(periode);
			SammelSaldoInfoDto periodeSaldo = getSammelSaldo(krit, uvaKonten, theClientDto);
			sammelSaldo.addSammelSaldo(periodeSaldo);
		}

		krit.setIPeriode(originalPeriode);
		return sammelSaldo;
	}

	private void fillFinanzamtParameter(Map<String, Object> mapParameter, ReportUvaKriterienDto krit,
			FinanzamtDto finanzamtDto, Date[] dVonBis, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {

		MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);

		// Mandantadresse
		mapParameter.put(LPReport.P_MANDANT_ANREDE_UND_NAME, mandantDto.getPartnerDto().formatFixTitelName1Name2());
		mapParameter.put(LPReport.P_MANDANTADRESSE, mandantDto.getPartnerDto().formatAdresse());

		// Finanzamtadresse
		if (finanzamtDto == null) {
			mapParameter.put("P_FA_NAME", "Unbekannt");
			mapParameter.put("P_FA_ADRESSE", "Unbekannt");
			mapParameter.put("P_FA_REFERAT", "Unbekannt");
			// Steuernummer
			mapParameter.put("P_FA_STEUERNUMMER", "Unbekannt");
			mapParameter.put("P_UMSATZRUNDEN", new Boolean(false));
		} else {
			mapParameter.put("P_FA_NAME", finanzamtDto.getPartnerDto().formatFixName1Name2());
			mapParameter.put("P_FA_ADRESSE", finanzamtDto.getPartnerDto().formatAdresse());
			// Steuernummer
			mapParameter.put("P_FA_STEUERNUMMER", finanzamtDto.getCSteuernummer());
			mapParameter.put("P_FA_REFERAT", finanzamtDto.getCReferat());
			mapParameter.put("P_UMSATZRUNDEN", new Boolean(Helper.short2boolean(finanzamtDto.getBUmsatzRunden())));
		}
		// Periode + Geschaeftsjahr
		mapParameter.put("P_PERIODE", krit.getIPeriode());
		mapParameter.put("P_GESCHAEFTSJAHR", krit.getIGeschaeftsjahr());
		mapParameter.put("P_PERIODE_MONAT", krit.getSPeriode());
		mapParameter.put("P_PERIODE_KALENDERDATUM", dVonBis[0]);

		mapParameter.put("P_ABRECHNUNGSZEITRAUM", krit.getSAbrechnungszeitraum());
		mapParameter.put("P_DETAILS", new Boolean(krit.isDetails()));

	}

	private void pruefeSteuerkontenUva(ReportUvaKriterienDto krit, TheClientDto theClientDto) throws RemoteException {
		Session session = FLRSessionFactory.getFactory().openSession();
		// String queryString =
		// "SELECT DISTINCT (o) FROM FLRSteuerkategoriekonto o"
		// +
		// " WHERE o.flrsteuerkategorie.finanzamt_i_id = :pFinanzamt and
		// o.flrsteuerkategorie.b_reversecharge = 0";
		ReversechargeartDto rcartOhneDto = getFinanzServiceFac().reversechargeartFindOhne(theClientDto);
		String queryString = "SELECT DISTINCT (o) FROM FLRSteuerkategoriekonto o"
				+ " WHERE o.flrsteuerkategorie.finanzamt_i_id = :pFinanzamt "
				+ " AND o.flrsteuerkategorie.reversechargeart_i_id = :pRcArtId ";
		// SP 2652 Reverse Charge Steuerkonten fliessen nicht in die UVA mitein!
		org.hibernate.Query hquery = session.createQuery(queryString);
		hquery.setParameter("pFinanzamt", krit.getFinanzamtIId());
		hquery.setParameter("pRcArtId", rcartOhneDto.getIId());
		List<FLRSteuerkategoriekonto> results = hquery.list();
		Iterator<FLRSteuerkategoriekonto> it = (Iterator<FLRSteuerkategoriekonto>) results.iterator();
		while (it.hasNext()) {
			FLRSteuerkategoriekonto stkk = it.next();
			Steuerkategorie stk = em.find(Steuerkategorie.class, stkk.getFlrsteuerkategorie().getI_id());

			ArrayList<Integer> kontenIIdAusnahmen = new ArrayList<Integer>();

			// sammelkonto ust ausnehmen
			KontoDto sammelkonto;
			try {
				sammelkonto = getFinanzServiceFac().getSammelkonto(FinanzServiceFac.KONTOART_UST_SAMMEL, null,
						theClientDto);
				if (sammelkonto != null)
					kontenIIdAusnahmen.add(sammelkonto.getIId());
			} catch (EJBExceptionLP e) {
				//
			}

			kontenIIdAusnahmen.add(stk.getKontoIIdForderungen());
			kontenIIdAusnahmen.add(stk.getKontoIIdKursgewinn());
			kontenIIdAusnahmen.add(stk.getKontoIIdKursverlust());
			kontenIIdAusnahmen.add(stk.getKontoIIdVerbindlichkeiten());

			// Anzahlungskonten ausnehmen
			Finanzamt finanzamt = em.find(Finanzamt.class,
					new FinanzamtPK(krit.getFinanzamtIId(), theClientDto.getMandant()));
			kontenIIdAusnahmen.add(finanzamt.getKontoIIdAnzahlungErhaltenVerr());
//			kontenIIdAusnahmen.add(finanzamt.getKontoIIdAnzahlungErhaltenBezahlt());

			KontolaenderartQuery q = new KontolaenderartQuery(em);
			List<Kontolaenderart> uebersetzungen = q.listAll(theClientDto.getMandant(),
					finanzamt.getKontoIIdAnzahlungErhaltenVerr(), krit.getFinanzamtIId());

			for (Kontolaenderart kontolaenderart : uebersetzungen) {
				kontenIIdAusnahmen.add(kontolaenderart.getKontoIIdUebersetzt());
			}

			// nur UST Konten pruefen
			if (stkk.getKontoiidvk() != null) {
				pruefeSteuerkontoUva(krit, stk, stkk.getKontoiidvk().getI_id(), kontenIIdAusnahmen, theClientDto);
			}
		}
		session.close();
	}

	private void pruefeSteuerkontoUva(ReportUvaKriterienDto krit, Steuerkategorie stk, Integer kontoIId,
			List<Integer> kontoIIdIgnore, TheClientDto theClientDto) {
		Session session = FLRSessionFactory.getFactory().openSession();
		Timestamp[] tVonBis = getBuchenFac().getDatumbereichPeriodeGJUva(krit.getIGeschaeftsjahr(), krit.getIPeriode(),
				theClientDto);

		HvTypedCriteria<FLRFinanzBuchungDetail> crit = new HvTypedCriteria<FLRFinanzBuchungDetail>(
				session.createCriteria(FLRFinanzBuchungDetail.class));
		crit.add(Restrictions.eq("konto_i_id", kontoIId)).createCriteria("flrbuchung")
				.add(Restrictions.ge("d_buchungsdatum", tVonBis[0])).add(Restrictions.lt("d_buchungsdatum", tVonBis[1]))
				.add(Restrictions.isNull("t_storniert")).add(Restrictions.eq("b_autombuchung", Helper.getShortFalse()));

		List<SteuerkategoriekontoDto> allSkkDtos = Arrays
				.asList(getFinanzServiceFac().steuerkategoriekontoFindAll(stk.getIId()));

		List<FLRFinanzBuchungDetail> results = crit.list();
		for (FLRFinanzBuchungDetail detail : results) {
			BuchungdetailDto[] buchungdetails = getBuchenFac().buchungdetailsFindByBuchungIId(detail.getBuchung_i_id());
			// HelperServer.printBuchungssatz(buchungdetails, getFinanzFac(),
			// System.out);
			for (BuchungdetailDto detail1 : buchungdetails) {
				Konto konto = em.find(Konto.class, detail1.getKontoIId());
				if (!konto.getKontotypCNr().equals(FinanzServiceFac.KONTOTYP_SACHKONTO))
					continue;
				if (konto.getIId().equals(kontoIId))
					continue;
				if (kontoIIdIgnore.contains(konto.getIId()))
					continue;

				// wenn Bank dann OK
				if (getFinanzFac().bankverbindungFindByKontoIIdOhneExc(konto.getIId()) != null)
					continue;

				// wenn Kassenkonto dann OK
				if (getFinanzFac().kassenbuchFindByKontoIIdOhneExc(konto.getIId()) != null)
					continue;

				// sonst muss die uvaart gueltig sein
				if (konto.getUvaartIId() != null) {
					Uvaart uvaart = em.find(Uvaart.class, konto.getUvaartIId());
					if (!Helper.isOneOf(uvaart.getCNr(), FinanzServiceFac.UVAART_NICHT_ZUTREFFEND,
							FinanzServiceFac.UVAART_ZAHLLASTKONTO)) {
						continue;
					}
				}

				// Eine Eroeffnungsbuchung darf auf ein Steuerkonto
				// gemacht werden
				// PJ 2012/0477
				FLRFinanzBuchung buchung = detail.getFlrbuchung();
				if (FinanzFac.BUCHUNGSART_EROEFFNUNG.equals(buchung.getBuchungsart_c_nr()))
					continue;

				// Zahllastkonto darf auf USt/VSt-Konto buchen
				if (isZahllastBuchung(detail1))
					continue;

				// "konto" des Buchungsdetails1 ist also ein Steuerkonto
				// Es muss in einer der Steuerkategorikontodefinitionen auftauchen
				boolean found = allSkkDtos.stream().anyMatch(skk -> konto.getIId().equals(skk.getKontoIIdVk()));
				if (found)
					continue;

				String kontoNr = detail.getFlrkonto().getC_nr();
				String gegenkontoNr = konto.getCNr();
				String info = createBuchungsInfoForException(buchung);
				String detailInfo = createBuchungsDetailInforForException(detail1);
				Konto stkKonto = em.find(Konto.class, kontoIId);
				String stkInfo = "Steuerkategorie: " + stk.getCBez() + "(cnr:" + stk.getCNr() + "), "
						+ "Pr\u00fcfkonto: " + stkKonto.getCBez() + "(cnr:" + stkKonto.getCNr() + ").";
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_EXPORT_UNGUELTIGE_BUCHUNG_AUF_STEUERKONTO,
						stkInfo + "\nBuchung: " + info + "Ung\u00FCltiges Konto " + gegenkontoNr
								+ " in Buchung auf Steuerkonto " + kontoNr + "\n"
								+ HelperServer.printBuchungssatz(buchungdetails, getFinanzFac(), null) + "\n"
								+ detailInfo,
						new Object[] { kontoNr, gegenkontoNr, info });
			}
		}
		session.close();
	}

	private boolean isZahllastBuchung(BuchungdetailDto buchungDetailDto) {
		Konto konto1 = em.find(Konto.class, buchungDetailDto.getKontoIId());
		if (buchungDetailDto.getKontoIIdGegenkonto() == null)
			return false;

		Konto konto2 = em.find(Konto.class, buchungDetailDto.getKontoIIdGegenkonto());

		if (isZahllastBuchungImpl(konto1, konto2) || isZahllastBuchungImpl(konto2, konto1))
			return true;

		return false;
	}

	private boolean isZahllastBuchungImpl(Konto konto1, Konto konto2) {
		return FinanzServiceFac.KONTOART_FA_ZAHLLAST.equals(konto1.getKontoartCNr()) && Helper
				.isOneOf(konto2.getKontoartCNr(), FinanzServiceFac.KONTOART_UST, FinanzServiceFac.KONTOART_VST);
	}

	private String createBuchungsInfoForException(FLRFinanzBuchung buchung) {
		String info = String.format("%s %s, %s, %s%n",
				buchung.getFlrfbbelegart() == null ? "" : buchung.getFlrfbbelegart().getC_nr(),
				buchung.getC_belegnummer().trim(), buchung.getBuchungsart_c_nr().trim(), buchung.getD_buchungsdatum());
		return info;
	}

	private String createBuchungsDetailInforForException(BuchungdetailDto detail) {
		String info = "Detail: id:" + detail.getIId() + ", Betrag: " + detail.getNBetrag() + ", KontoId:"
				+ detail.getKontoIId() + ", GegenkontoId:" + detail.getKontoIIdGegenkonto() + ", "
				+ (detail.isSollBuchung() ? "SOLL" : "HABEN") + ", AZK:" + detail.getIAusziffern();
		return info;
	}

	private BigDecimal umrechnen(BigDecimal betrag, String waehrung, ReportUvaKriterienDto krit,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		Timestamp[] tVonBis = getBuchenFac().getDatumbereichPeriodeGJ(krit.getIGeschaeftsjahr(), krit.getIPeriode(),
				theClientDto);
		// Achtung: Ende ist der Beginn der naechsten Periode, daher 1 Tag
		// abziehen
		return getLocaleFac().rechneUmInAndereWaehrungGerundetZuDatum(betrag, theClientDto.getSMandantenwaehrung(),
				waehrung, Helper.addiereTageZuDatum(new java.sql.Date(tVonBis[1].getTime()), -1), theClientDto);

	}

	private void umrechnen(SammelSaldoInfoDto salden, String waehrung, ReportUvaKriterienDto krit,
			TheClientDto theClientDto) throws RemoteException {
		Timestamp[] tVonBis = getBuchenFac().getDatumbereichPeriodeGJ(krit.getIGeschaeftsjahr(), krit.getIPeriode(),
				theClientDto);
		// Achtung: Ende ist der Beginn der naechsten Periode, daher 1 Tag
		// abziehen
		java.sql.Date d = Helper.addiereTageZuDatum(new java.sql.Date(tVonBis[1].getTime()), -1);

		for (Map.Entry<KontoUvaVariante, SaldoInfoDto[]> entry : salden.total().entrySet()) {
			SaldoInfoDto[] saldo = entry.getValue();
			saldo[0].setSaldo(getLocaleFac().rechneUmInAndereWaehrungGerundetZuDatum(saldo[0].getSaldo(),
					theClientDto.getSMandantenwaehrung(), waehrung, d, theClientDto));
			if (saldo[1].getSaldo().signum() != 0) {
				saldo[1].setSaldo(getLocaleFac().rechneUmInAndereWaehrungGerundetZuDatum(saldo[1].getSaldo(),
						theClientDto.getSMandantenwaehrung(), waehrung, d, theClientDto));
			}
		}
	}

	private SammelSaldoInfoDto getSammelSaldo(ReportUvaKriterienDto krit, List<FLRFinanzKonto> konten,
			TheClientDto theClientDto) {
		SammelSaldoInfoDto sammelSaldo = new SammelSaldoInfoDto();
		for (FLRFinanzKonto konto : konten) {
			SaldoInfoDto[] salden = getBuchenFac().getSaldoUVAOhneEBVonKonto(konto.getI_id(), krit.getIGeschaeftsjahr(),
					krit.getIPeriode(), theClientDto);

			sammelSaldo.add(salden);
			myLogger.warn("Einzelsaldo " + krit.getIGeschaeftsjahr() + "." + krit.getIPeriode() + " fuer Konto "
					+ konto.getC_nr() + " = [" + salden[0].getSaldo().toPlainString() + "|"
					+ salden[1].getSaldo().toPlainString() + "].");
		}

		myLogger.warn("Sammelsaldo Monat " + krit.getIGeschaeftsjahr() + "." + krit.getIPeriode() + " fuer Konten ("
				+ logKontenInfo(konten) + "), " + logSaldoInfo(sammelSaldo));
//		myLogger.warn("Sammelsaldo Monat " + krit.getIGeschaeftsjahr() + "." + krit.getIPeriode() + " fuer Konten ("
//				+ logKontenInfo(konten) + ") = [" + total[0].getSaldo().toPlainString() + "|"
//				+ total[1].getSaldo().toPlainString() + "].");
		return sammelSaldo;
	}

	private SaldoInfoDto[] getSammelSaldo0(ReportUvaKriterienDto krit, List<FLRFinanzKonto> konten,
			TheClientDto theClientDto) {
		SaldoInfoDto[] total = null;
		for (FLRFinanzKonto konto : konten) {
			SaldoInfoDto[] salden = getBuchenFac().getSaldoUVAOhneEBVonKonto(konto.getI_id(), krit.getIGeschaeftsjahr(),
					krit.getIPeriode(), theClientDto);

			if (total == null) {
				total = salden;
			} else {
				total[0].addSaldo(salden[0].getSaldo());
				if (total[0].getSatzDto() == null) {
					total[0].setSatzDto(salden[0].getSatzDto());
				}
				total[1].addSaldo(salden[1].getSaldo());
				if (total[1].getSatzDto() == null) {
					total[1].setSatzDto(salden[1].getSatzDto());
				}
			}
			myLogger.warn("Einzelsaldo " + krit.getIGeschaeftsjahr() + "." + krit.getIPeriode() + " fuer Konto "
					+ konto.getC_nr() + " = [" + salden[0].getSaldo().toPlainString() + "|"
					+ salden[1].getSaldo().toPlainString() + "].");
		}

		if (total == null) {
			total = new SaldoInfoDto[] { new SaldoInfoDto(), new SaldoInfoDto() };
		}

		myLogger.warn("Sammelsaldo Monat " + krit.getIGeschaeftsjahr() + "." + krit.getIPeriode() + " fuer Konten ("
				+ logKontenInfo(konten) + ") = [" + total[0].getSaldo().toPlainString() + "|"
				+ total[1].getSaldo().toPlainString() + "].");
		return total;
	}

	private String logKontenInfo(List<FLRFinanzKonto> konten) {
		StringBuffer sb = new StringBuffer();
		for (FLRFinanzKonto flrFinanzKonto : konten) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append(flrFinanzKonto.getC_nr());
		}
		return sb.toString();
	}

	private String logSaldoInfo(SammelSaldoInfoDto sammelSaldo) {
		StringBuffer sb = new StringBuffer("Sammelsaldo: {");
		for (Map.Entry<KontoUvaVariante, SaldoInfoDto[]> entry : sammelSaldo.entrySet()) {
			SaldoInfoDto[] salden = entry.getValue();
			sb.append("{variante:");
			sb.append(entry.getKey().getVariante());
			sb.append("[");
			sb.append(salden[0].getSaldo());
			sb.append("|");
			sb.append(salden[1].getSaldo());
			sb.append("]}");
		}

//		TimeZone.getDefault();
		sb.append("}");
		return sb.toString();
	}

	private BigDecimal getSammelSaldo(ReportErfolgsrechnungKriterienDto krit, boolean bKummuliert, List<Konto> konten,
			boolean bNurEB, TheClientDto theClientDto) {
		BigDecimal saldo = new BigDecimal(0);
		for (Iterator<Konto> iter = (Iterator<Konto>) konten.iterator(); iter.hasNext();) {
			Konto konto = iter.next();
			saldo = saldo.add(getBuchenFac().getSummeEroeffnungKontoIId(konto.getIId(), krit.getIGeschaeftsjahr(),
					krit.getIPeriode(), bKummuliert, theClientDto));
			if (!bNurEB) {
				saldo = saldo.add(getBuchenFac().getSaldoOhneEBVonKonto(konto.getIId(), krit.getIGeschaeftsjahr(),
						krit.getIPeriode(), bKummuliert, theClientDto));
			}
		}
		return saldo;
	}

	private BigDecimal getSammelSaldo(ReportErfolgsrechnungKriterienDto krit, boolean bKummuliert, List<Konto> konten,
			boolean bPositvSaldo, boolean bNurEB, TheClientDto theClientDto) {
		BigDecimal saldo = new BigDecimal(0);
		BigDecimal saldoKonto;
		for (Iterator<Konto> iter = (Iterator<Konto>) konten.iterator(); iter.hasNext();) {
			Konto konto = iter.next();
			saldoKonto = getBuchenFac().getSummeEroeffnungKontoIId(konto.getIId(), krit.getIGeschaeftsjahr(),
					krit.getIPeriode(), bKummuliert, theClientDto);
			if (!bNurEB) {
				saldoKonto = saldoKonto.add(getBuchenFac().getSaldoOhneEBVonKonto(konto.getIId(),
						krit.getIGeschaeftsjahr(), krit.getIPeriode(), bKummuliert, theClientDto));
			}
			if (bPositvSaldo && saldoKonto.signum() > 0)
				saldo = saldo.add(saldoKonto);
			if (!bPositvSaldo && saldoKonto.signum() < 0)
				saldo = saldo.add(saldoKonto);
		}
		return saldo;
	}

	private ArrayList<Object> getPersonaldkostenFuerLiquiditaetsvorschau(TheClientDto theClientDto,
			Integer kalenderwochen, java.sql.Timestamp tDatumAb) {
		ArrayList<Object> alDaten = new ArrayList<Object>();

		Integer tagesartIId_Feiertag = getZeiterfassungFac()
				.tagesartFindByCNr(ZeiterfassungFac.TAGESART_FEIERTAG, theClientDto).getIId();
		Integer tagesartIId_Halbtag = getZeiterfassungFac()
				.tagesartFindByCNr(ZeiterfassungFac.TAGESART_HALBTAG, theClientDto).getIId();

		// ein Jahr vorher
		Calendar c = Calendar.getInstance();

		c.setTimeInMillis(tDatumAb.getTime());

		c.add(Calendar.YEAR, -1);
		c.set(Calendar.DAY_OF_MONTH, 1);

		java.sql.Timestamp tVon = Helper.cutTimestamp(new java.sql.Timestamp(c.getTimeInMillis()));

		c = Calendar.getInstance();
		c.add(Calendar.WEEK_OF_YEAR, kalenderwochen);
		c.add(Calendar.YEAR, 1);

		java.sql.Timestamp tBis = new java.sql.Timestamp(c.getTimeInMillis());

		c = Calendar.getInstance();
		c.add(Calendar.WEEK_OF_YEAR, kalenderwochen);
		java.sql.Timestamp tEndeLiqiditaetsvorschau = new java.sql.Timestamp(c.getTimeInMillis());

		// Alle Zahltage holen
		ZahltagDto[] dtos = getPersonalFac().zahltagFindByMandantCNr(theClientDto);

		HashMap<Integer, ZahltagDto> hm = new HashMap<Integer, ZahltagDto>();
		for (int i = 0; i < dtos.length; i++) {

			hm.put(dtos[i].getIMonat(), dtos[i]);

		}

		DateFormatSymbols symbols = new DateFormatSymbols(theClientDto.getLocUi());
		String[] defaultMonths = symbols.getMonths();

		while (tVon.before(tBis)) {
			Calendar cTemp = Calendar.getInstance();
			cTemp.setTimeInMillis(tVon.getTime());
			// Termine berechnen
			if (hm.containsKey(cTemp.get(Calendar.MONTH))) {
				ZahltagDto zDto = hm.get(cTemp.get(Calendar.MONTH));

				// Termin Netto
				Calendar cNetto = Calendar.getInstance();
				cNetto.setTimeInMillis(cTemp.getTimeInMillis());

				int iVersatzBanktage = 0;

				if (Helper.short2boolean(zDto.getBMonatsletzterNetto())) {
					cNetto.set(Calendar.DAY_OF_MONTH, cNetto.getActualMaximum(Calendar.DAY_OF_MONTH));

					iVersatzBanktage = zDto.getIStichtagNetto();

				} else {
					cNetto.set(Calendar.DAY_OF_MONTH, zDto.getIStichtagNetto());
				}

				// Wenn Halbtag/Feiertag/Samtag/sonntag, dann einen Tag vorher

				int iAzahlBanktageSchonVersetzt = 0;
				while (true) {

					boolean bBanktag = true;

					if (cNetto.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
							|| cNetto.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
						bBanktag = false;
					} else {

						BetriebskalenderDto dto = getPersonalFac().betriebskalenderFindByMandantCNrDDatum(
								new Timestamp(cNetto.getTimeInMillis()), theClientDto.getMandant(), theClientDto);
						if (dto != null) {
							if (dto.getTagesartIId().equals(tagesartIId_Feiertag)
									|| dto.getTagesartIId().equals(tagesartIId_Halbtag)) {
								bBanktag = false;
							}
						}
					}

					if (bBanktag == false) {

						if (iVersatzBanktage > 0) {
							cNetto.add(Calendar.DAY_OF_MONTH, 1);
						} else {
							cNetto.add(Calendar.DAY_OF_MONTH, -1);
						}

					} else {

						if (iAzahlBanktageSchonVersetzt >= java.lang.Math.abs(iVersatzBanktage)) {

							break;
						} else {
							if (iVersatzBanktage > 0) {
								cNetto.add(Calendar.DAY_OF_MONTH, 1);
							} else {
								cNetto.add(Calendar.DAY_OF_MONTH, -1);
							}
							iAzahlBanktageSchonVersetzt++;
						}

					}

				}

				// Wenn vor Ende Liquiditaetsvorschau und >= abDatum
				if (cNetto.getTime().before(tEndeLiqiditaetsvorschau)
						&& cNetto.getTime().getTime() >= Helper.cutTimestamp(tDatumAb).getTime()) {

					// Hole alle Personen, welche in diesem Monat eingetreten
					// sind
					ArrayList<Integer> personalIIds = getAlleEingetretenenPersonenEinesMonats(cNetto.getTime(),
							theClientDto);

					ArrayList<PersonalgehaltDto> listPG = getAllePersonalgehaltEinesMonats(personalIIds,
							cNetto.getTime(), theClientDto);
					BigDecimal bdSummeNetto = BigDecimal.ZERO;
					for (int i = 0; i < listPG.size(); i++) {
						PersonalgehaltDto pgDto = listPG.get(i);
						if (pgDto.getNGehaltNetto() != null) {
							bdSummeNetto = bdSummeNetto.add(pgDto.getNGehaltNetto());
						}
					}

					// In LV hinzufuegen

					Object[] oZeile = new Object[REPORT_LV_ANZAHL_SPALTEN];
					oZeile[REPORT_LV_BELEG] = "Nettogehaelter";
					oZeile[REPORT_LV_IMPORTIERT] = new Boolean(false);
					oZeile[REPORT_LV_PARTNER] = defaultMonths[zDto.getIMonat()];
					oZeile[REPORT_LV_JAHR] = Helper.berechneJahrDerKW(cNetto);
					oZeile[REPORT_LV_DATUM] = new java.sql.Date(cNetto.getTimeInMillis());
					oZeile[REPORT_LV_WOCHE] = cNetto.get(Calendar.WEEK_OF_YEAR);
					oZeile[REPORT_LV_NETTO] = bdSummeNetto.negate()
							.multiply(new BigDecimal(zDto.getFFaktor().doubleValue()));
					oZeile[REPORT_LV_MWST] = BigDecimal.ZERO;
					alDaten.add(oZeile);
				}

				// TERMIN LOHNSTEUER
				Calendar cLohnsteuer = Calendar.getInstance();
				cLohnsteuer.setTimeInMillis(cTemp.getTimeInMillis());
				cLohnsteuer.set(Calendar.DAY_OF_MONTH, cLohnsteuer.getActualMaximum(Calendar.DAY_OF_MONTH));

				cLohnsteuer.add(Calendar.DAY_OF_MONTH, zDto.getIStichtagLohnsteuerRelativZumLetzten());

				// Wenn Halbtag/Feiertag/Samtag/sonntag, dann einen Tag vorher

				while (true) {

					boolean bBanktag = true;

					if (cLohnsteuer.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
							|| cLohnsteuer.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
						bBanktag = false;
					} else {

						BetriebskalenderDto dto = getPersonalFac().betriebskalenderFindByMandantCNrDDatum(
								new Timestamp(cLohnsteuer.getTimeInMillis()), theClientDto.getMandant(), theClientDto);
						if (dto != null) {
							if (dto.getTagesartIId().equals(tagesartIId_Feiertag)
									|| dto.getTagesartIId().equals(tagesartIId_Halbtag)) {
								bBanktag = false;
							}
						}
					}

					if (bBanktag == false) {
						cLohnsteuer.add(Calendar.DAY_OF_MONTH, -1);
					} else {
						break;
					}
				}

				// Wenn vor Ende Liquiditaetsvorschau und >= abDatum
				if (cLohnsteuer.getTime().before(tEndeLiqiditaetsvorschau)
						&& cLohnsteuer.getTime().getTime() >= Helper.cutTimestamp(tDatumAb).getTime()) {

					// Hole alle Personen, welche in diesem Monat eingetreten
					// sind
					ArrayList<Integer> personalIIds = getAlleEingetretenenPersonenEinesMonats(cLohnsteuer.getTime(),
							theClientDto);

					ArrayList<PersonalgehaltDto> listPG = getAllePersonalgehaltEinesMonats(personalIIds,
							cLohnsteuer.getTime(), theClientDto);
					BigDecimal bdSummeLohnsteuer = BigDecimal.ZERO;
					for (int i = 0; i < listPG.size(); i++) {
						PersonalgehaltDto pgDto = listPG.get(i);
						if (pgDto.getNLohnsteuer() != null) {
							bdSummeLohnsteuer = bdSummeLohnsteuer.add(pgDto.getNLohnsteuer());
						}
					}

					// In LV hinzufuegen

					Object[] oZeile = new Object[REPORT_LV_ANZAHL_SPALTEN];
					oZeile[REPORT_LV_BELEG] = "Lohnsteuer";
					oZeile[REPORT_LV_IMPORTIERT] = new Boolean(false);
					oZeile[REPORT_LV_PARTNER] = defaultMonths[zDto.getIMonat()];
					oZeile[REPORT_LV_JAHR] = Helper.berechneJahrDerKW(cLohnsteuer);

					oZeile[REPORT_LV_DATUM] = new java.sql.Date(cLohnsteuer.getTimeInMillis());

					oZeile[REPORT_LV_WOCHE] = cLohnsteuer.get(Calendar.WEEK_OF_YEAR);
					oZeile[REPORT_LV_NETTO] = bdSummeLohnsteuer.negate()
							.multiply(new BigDecimal(zDto.getFFaktor().doubleValue()));
					oZeile[REPORT_LV_MWST] = BigDecimal.ZERO;
					alDaten.add(oZeile);
				}
				// TERMIN ABGABEN

				Calendar cAbgaben = Calendar.getInstance();
				cAbgaben.setTimeInMillis(cTemp.getTimeInMillis());
				cAbgaben.set(Calendar.DAY_OF_MONTH, cAbgaben.getActualMaximum(Calendar.DAY_OF_MONTH));

				cAbgaben.add(Calendar.DAY_OF_MONTH, zDto.getIStichtagSozialabgabenRelativZumLetzten());

				// Wenn Halbtag/Feiertag/Samtag/sonntag, dann einen Tag vorher

				while (true) {

					boolean bBanktag = true;

					if (cAbgaben.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
							|| cAbgaben.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
						bBanktag = false;
					} else {

						BetriebskalenderDto dto = getPersonalFac().betriebskalenderFindByMandantCNrDDatum(
								new Timestamp(cAbgaben.getTimeInMillis()), theClientDto.getMandant(), theClientDto);
						if (dto != null) {
							if (dto.getTagesartIId().equals(tagesartIId_Feiertag)
									|| dto.getTagesartIId().equals(tagesartIId_Halbtag)) {
								bBanktag = false;
							}
						}
					}

					if (bBanktag == false) {
						cAbgaben.add(Calendar.DAY_OF_MONTH, -1);
					} else {
						break;
					}
				}

				// Wenn vor Ende Liquiditaetsvorschau und >= abDatum
				if (cAbgaben.getTime().before(tEndeLiqiditaetsvorschau)
						&& cAbgaben.getTime().getTime() >= Helper.cutTimestamp(tDatumAb).getTime()) {

					// Hole alle Personen, welche in diesem Monat eingetreten
					// sind
					ArrayList<Integer> personalIIds = getAlleEingetretenenPersonenEinesMonats(cAbgaben.getTime(),
							theClientDto);

					ArrayList<PersonalgehaltDto> listPG = getAllePersonalgehaltEinesMonats(personalIIds,
							cAbgaben.getTime(), theClientDto);
					BigDecimal bdSummeSozialabgaben = BigDecimal.ZERO;
					for (int i = 0; i < listPG.size(); i++) {
						PersonalgehaltDto pgDto = listPG.get(i);
						if (pgDto.getNGehaltBruttobrutto() != null && pgDto.getNGehaltNetto() != null) {

							BigDecimal bd = pgDto.getNGehaltBruttobrutto().subtract(pgDto.getNGehaltNetto());

							if (pgDto.getNLohnsteuer() != null) {
								bd = bd.subtract(pgDto.getNLohnsteuer());
							}

							bdSummeSozialabgaben = bdSummeSozialabgaben.add(bd);
						}
					}

					// In LV hinzufuegen

					Object[] oZeile = new Object[REPORT_LV_ANZAHL_SPALTEN];
					oZeile[REPORT_LV_BELEG] = "Sozialabgaben";
					oZeile[REPORT_LV_IMPORTIERT] = new Boolean(false);
					oZeile[REPORT_LV_PARTNER] = defaultMonths[zDto.getIMonat()];
					oZeile[REPORT_LV_JAHR] = Helper.berechneJahrDerKW(cAbgaben);
					oZeile[REPORT_LV_DATUM] = new java.sql.Date(cAbgaben.getTimeInMillis());
					oZeile[REPORT_LV_WOCHE] = cAbgaben.get(Calendar.WEEK_OF_YEAR);
					oZeile[REPORT_LV_NETTO] = bdSummeSozialabgaben.negate()
							.multiply(new BigDecimal(zDto.getFFaktor().doubleValue()));
					oZeile[REPORT_LV_MWST] = BigDecimal.ZERO;
					alDaten.add(oZeile);
				}
			}

			// Monat erhoehen

			cTemp.add(Calendar.MONTH, 1);

			tVon = Helper.cutTimestamp(new java.sql.Timestamp(cTemp.getTimeInMillis()));

		}

		return alDaten;
	}

	private ArrayList<Object> getPersonaldkostenFuerEinfacheErfolgsrechnung(TheClientDto theClientDto,
			java.sql.Timestamp tDatumAb, java.sql.Timestamp tDatumBis) {
		ArrayList<Object> alDaten = new ArrayList<Object>();

		Integer tagesartIId_Feiertag = getZeiterfassungFac()
				.tagesartFindByCNr(ZeiterfassungFac.TAGESART_FEIERTAG, theClientDto).getIId();
		Integer tagesartIId_Halbtag = getZeiterfassungFac()
				.tagesartFindByCNr(ZeiterfassungFac.TAGESART_HALBTAG, theClientDto).getIId();

		// ein Jahr vorher
		Calendar c = Calendar.getInstance();

		c.setTimeInMillis(tDatumAb.getTime());

		java.sql.Timestamp tVon = Helper.cutTimestamp(new java.sql.Timestamp(c.getTimeInMillis()));

		c = Calendar.getInstance();
		c.setTimeInMillis(tDatumBis.getTime());

		java.sql.Timestamp tBis = new java.sql.Timestamp(c.getTimeInMillis());

		// Alle Zahltage holen
		ZahltagDto[] dtos = getPersonalFac().zahltagFindByMandantCNr(theClientDto);

		HashMap<Integer, ZahltagDto> hm = new HashMap<Integer, ZahltagDto>();
		for (int i = 0; i < dtos.length; i++) {

			hm.put(dtos[i].getIMonat(), dtos[i]);

		}

		DateFormatSymbols symbols = new DateFormatSymbols(theClientDto.getLocUi());
		String[] defaultMonths = symbols.getMonths();

		while (tVon.before(tBis)) {
			Calendar cTemp = Calendar.getInstance();
			cTemp.setTimeInMillis(tVon.getTime());
			// Termine berechnen
			if (hm.containsKey(cTemp.get(Calendar.MONTH))) {
				ZahltagDto zDto = hm.get(cTemp.get(Calendar.MONTH));

				// Termin Netto
				Calendar cNetto = Calendar.getInstance();
				cNetto.setTimeInMillis(cTemp.getTimeInMillis());

				// Wenn Halbtag/Feiertag/Samtag/sonntag, dann einen Tag vorher

				// Wenn vor Ende Liquiditaetsvorschau und >= abDatum
				if (cNetto.getTime().before(tBis)
						&& cNetto.getTime().getTime() >= Helper.cutTimestamp(tDatumAb).getTime()) {

					// Hole alle Personen, welche in diesem Monat eingetreten
					// sind
					ArrayList<Integer> personalIIds = getAlleEingetretenenPersonenEinesMonats(cNetto.getTime(),
							theClientDto);

					ArrayList<PersonalgehaltDto> listPG = getAllePersonalgehaltEinesMonats(personalIIds,
							cNetto.getTime(), theClientDto);
					BigDecimal bdSummeBruttoBrutto = BigDecimal.ZERO;
					for (int i = 0; i < listPG.size(); i++) {
						PersonalgehaltDto pgDto = listPG.get(i);
						if (pgDto.getNGehaltBruttobrutto() != null) {
							bdSummeBruttoBrutto = bdSummeBruttoBrutto.add(pgDto.getNGehaltBruttobrutto());
						}
					}

					// In LV hinzufuegen

					Object[] oZeile = new Object[REPORT_LV_ANZAHL_SPALTEN];
					oZeile[REPORT_LV_BELEG] = "Nettogehaelter";
					oZeile[REPORT_LV_IMPORTIERT] = new Boolean(false);
					oZeile[REPORT_LV_PARTNER] = defaultMonths[zDto.getIMonat()];
					oZeile[REPORT_LV_JAHR] = Helper.berechneJahrDerKW(cNetto);
					oZeile[REPORT_LV_DATUM] = new java.sql.Date(cNetto.getTimeInMillis());
					oZeile[REPORT_LV_WOCHE] = cNetto.get(Calendar.WEEK_OF_YEAR);
					oZeile[REPORT_LV_NETTO] = bdSummeBruttoBrutto
							.multiply(new BigDecimal(zDto.getFFaktor().doubleValue()));
					oZeile[REPORT_LV_MWST] = BigDecimal.ZERO;
					alDaten.add(oZeile);
				}

			}

			// Monat erhoehen

			cTemp.add(Calendar.MONTH, 1);

			tVon = Helper.cutTimestamp(new java.sql.Timestamp(cTemp.getTimeInMillis()));

		}

		return alDaten;
	}

	private ArrayList<PersonalgehaltDto> getAllePersonalgehaltEinesMonats(ArrayList<Integer> personalIIds,
			java.util.Date tStichtag, TheClientDto theClientDto) {
		ArrayList<PersonalgehaltDto> list = new ArrayList<PersonalgehaltDto>();

		Calendar c = Calendar.getInstance();
		c.setTime(tStichtag);

		for (int i = 0; i < personalIIds.size(); i++) {
			try {
				PersonalgehaltDto pgDto = getPersonalFac().personalgehaltFindLetztePersonalgehalt(personalIIds.get(i),
						c.get(Calendar.YEAR), c.get(Calendar.MONTH));
				if (pgDto != null) {
					list.add(pgDto);
				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
		}

		return list;

	}

	public ArrayList<Integer> getAlleEingetretenenPersonenEinesMonats(java.util.Date tStichtag,
			TheClientDto theClientDto) {

		Session session = FLRSessionFactory.getFactory().openSession();

		Calendar cVon = Calendar.getInstance();
		cVon.setTime(tStichtag);
		cVon.set(Calendar.DAY_OF_MONTH, 1);
		java.util.Date tAustritt = cVon.getTime();

		cVon.set(Calendar.DAY_OF_MONTH, cVon.getActualMaximum(Calendar.DAY_OF_MONTH));

		java.util.Date tEintritt = cVon.getTime();

		String sQuery = "SELECT distinct e.personal_i_id FROM FLREintrittaustritt e WHERE e.flrpersonal.mandant_c_nr='"
				+ theClientDto.getMandant() + "' AND e.t_eintritt<='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tEintritt.getTime()))
				+ "' AND (e.t_austritt IS NULL OR e.t_austritt > '"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tAustritt.getTime())) + "' )";

		Query query = session.createQuery(sQuery);

		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();
		ArrayList<Integer> personalIds = new ArrayList<Integer>();

		while (resultListIterator.hasNext()) {

			Integer personalIId = (Integer) resultListIterator.next();
			personalIds.add(personalIId);

		}

		return personalIds;
	}

	private ArrayList<Object> getMWSTDatenFuerLiquiditaetsvorschau(Integer kalenderwochen,
			Map<String, Object> parameter, boolean bOhneAndereMandanten, String inVerbundeneUnternehmenKunden,
			String inVerbundeneUnternehmenLieferanten, TheClientDto theClientDto) {
		ArrayList<Object> alDaten = new ArrayList<Object>();

		DateFormatSymbols symbols = new DateFormatSymbols(theClientDto.getLocUi());
		String[] defaultMonths = symbols.getMonths();

		int iUstStichtag = 15;
		int iUstMonat = 2;

		ArrayList<String> alAngelegteRechnungen = new ArrayList<String>();

		ParametermandantDto p;
		ReversechargeartDto rcartOhneDto = null;

		try {
			p = getParameterFac().getMandantparameter(theClientDto.getMandant(), ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_UST_STICHTAG);

			iUstStichtag = (Integer) p.getCWertAsObject();
			p = getParameterFac().getMandantparameter(theClientDto.getMandant(), ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_UST_MONAT);

			iUstMonat = (Integer) p.getCWertAsObject();

			rcartOhneDto = getFinanzServiceFac().reversechargeartFindOhne(theClientDto);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		Calendar c = Calendar.getInstance();

		c.add(Calendar.WEEK_OF_YEAR, kalenderwochen);
		java.sql.Timestamp tEndeLiqiditaetsvorschau = new java.sql.Timestamp(c.getTimeInMillis());
		c = Calendar.getInstance();

		if (c.get(Calendar.DAY_OF_MONTH) > iUstStichtag) {
			c.set(Calendar.DATE, iUstStichtag);
			c.add(Calendar.MONTH, 1);
		} else {
			c.set(Calendar.DATE, iUstStichtag);
		}

		java.sql.Timestamp tVon = Helper.cutTimestamp(new java.sql.Timestamp(c.getTimeInMillis()));

		while (tVon.before(tEndeLiqiditaetsvorschau)) {
			Calendar cTemp = Calendar.getInstance();
			cTemp.setTimeInMillis(tVon.getTime());

			// Zeitraum von-bis
			Calendar cZeitraum = Calendar.getInstance();
			cZeitraum.setTimeInMillis(cTemp.getTimeInMillis());
			cZeitraum.add(Calendar.MONTH, -iUstMonat);
			cZeitraum.set(Calendar.DAY_OF_MONTH, 1);
			java.sql.Date tVonBelegdatum = Helper.cutDate(new java.sql.Date(cZeitraum.getTimeInMillis()));

			cZeitraum.set(Calendar.DAY_OF_MONTH, cZeitraum.getActualMaximum(Calendar.DAY_OF_MONTH));
			java.sql.Date tBisBelegdatum = Helper.cutDate(new java.sql.Date(cZeitraum.getTimeInMillis()));
			// Rechungen holen

			Session session = FLRSessionFactory.getFactory().openSession();

			String sQuery = "SELECT r FROM FLRRechnungReport r WHERE r.d_belegdatum>='"
					+ Helper.formatDateWithSlashes(tVonBelegdatum) + "' AND r.d_belegdatum<='"
					+ Helper.formatDateWithSlashes(tBisBelegdatum) + "' AND r.status_c_nr <> '"
					+ RechnungFac.STATUS_STORNIERT + "' AND r.mandant_c_nr='" + theClientDto.getMandant()
					+ "' AND r.flrrechnungart.rechnungtyp_c_nr <>'" + RechnungFac.RECHNUNGTYP_PROFORMARECHNUNG + "'";

			if (bOhneAndereMandanten && inVerbundeneUnternehmenKunden != null) {
				sQuery += " AND r.kunde_i_id NOT IN " + inVerbundeneUnternehmenKunden;
			}

			sQuery += " ORDER BY r.c_nr";

			Query query = session.createQuery(sQuery);

			List<?> results = query.list();
			Iterator<?> resultListIterator = results.iterator();

			BigDecimal bdSummeMwst = BigDecimal.ZERO;

			while (resultListIterator.hasNext()) {

				FLRRechnungReport flrRechnung = (FLRRechnungReport) resultListIterator.next();

				if (rcartOhneDto.getIId().equals(flrRechnung.getReversechargeartId())) {
					if (flrRechnung.getStatus_c_nr().equals(RechnungFac.STATUS_ANGELEGT)) {
						// FEHLER

						alAngelegteRechnungen.add(flrRechnung.getC_nr());
						continue;
					}
					if (flrRechnung.getN_wertust() != null) {
						if (flrRechnung.getFlrrechnungart().getRechnungtyp_c_nr()
								.equals(RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
							bdSummeMwst = bdSummeMwst.subtract(flrRechnung.getN_wertust());
						} else {

							bdSummeMwst = bdSummeMwst.add(flrRechnung.getN_wertust());

							// Wenn Schlussrechung, dann Anzahlungen abziehen
							if (flrRechnung.getFlrrechnungart().getC_nr()
									.equals(RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG)) {
								BigDecimal anzUst = getRechnungFac()
										.getAnzahlungenZuSchlussrechnungUst(flrRechnung.getI_id());

								if (anzUst != null) {
									bdSummeMwst = bdSummeMwst.subtract(anzUst);
								}

							}

						}
					}

				}
			}

			session.close();

			Object[] oZeile = new Object[REPORT_LV_ANZAHL_SPALTEN];
			oZeile[REPORT_LV_BELEG] = "UST";
			oZeile[REPORT_LV_IMPORTIERT] = new Boolean(false);
			oZeile[REPORT_LV_PARTNER] = defaultMonths[cZeitraum.get(Calendar.MONTH)];
			oZeile[REPORT_LV_JAHR] = Helper.berechneJahrDerKW(cTemp);
			oZeile[REPORT_LV_WOCHE] = cTemp.get(Calendar.WEEK_OF_YEAR);
			oZeile[REPORT_LV_NETTO] = bdSummeMwst.negate();
			oZeile[REPORT_LV_MWST] = BigDecimal.ZERO;
			alDaten.add(oZeile);

			// Eingangsrechnungen holen

			session = FLRSessionFactory.getFactory().openSession();

			sQuery = "SELECT er FROM FLREingangsrechnung er WHERE er.t_belegdatum>='"
					+ Helper.formatDateWithSlashes(tVonBelegdatum) + "' AND er.t_belegdatum<='"
					+ Helper.formatDateWithSlashes(tBisBelegdatum) + "' AND er.status_c_nr <> '"
					+ RechnungFac.STATUS_STORNIERT + "' AND er.mandant_c_nr='" + theClientDto.getMandant() + "'";

			if (bOhneAndereMandanten && inVerbundeneUnternehmenLieferanten != null) {
				sQuery += " AND er.flrlieferant.i_id NOT IN " + inVerbundeneUnternehmenLieferanten;
			}

			query = session.createQuery(sQuery);

			results = query.list();
			resultListIterator = results.iterator();

			BigDecimal bdSummeVST = BigDecimal.ZERO;

			while (resultListIterator.hasNext()) {

				FLREingangsrechnung flrER = (FLREingangsrechnung) resultListIterator.next();
				if (rcartOhneDto.getIId().equals(flrER.getReversechargeartId())
						&& Helper.short2boolean(flrER.getB_igerwerb()) == false) {
					if (flrER.getN_ustbetrag() != null) {

						if (flrER.getEingangsrechnungart_c_nr()
								.equals(EingangsrechnungFac.EINGANGSRECHNUNGART_GUTSCHRIFT)) {
							bdSummeVST = bdSummeVST.subtract(flrER.getN_ustbetrag());
						} else {

							bdSummeVST = bdSummeVST.add(flrER.getN_ustbetrag());

							if (flrER.getEingangsrechnungart_c_nr()
									.equals(EingangsrechnungFac.EINGANGSRECHNUNGART_SCHLUSSZAHLUNG)) {
								bdSummeVST = bdSummeVST.subtract(getEingangsrechnungFac()
										.getAnzahlungenGestelltZuSchlussrechnungUst(flrER.getI_id()));
							}

						}
					}
				}

			}

			oZeile = new Object[REPORT_LV_ANZAHL_SPALTEN];
			oZeile[REPORT_LV_BELEG] = "VST";
			oZeile[REPORT_LV_IMPORTIERT] = new Boolean(false);
			oZeile[REPORT_LV_PARTNER] = defaultMonths[cZeitraum.get(Calendar.MONTH)];
			oZeile[REPORT_LV_JAHR] = Helper.berechneJahrDerKW(cTemp);
			oZeile[REPORT_LV_WOCHE] = cTemp.get(Calendar.WEEK_OF_YEAR);
			oZeile[REPORT_LV_NETTO] = bdSummeVST;
			oZeile[REPORT_LV_MWST] = BigDecimal.ZERO;
			alDaten.add(oZeile);

			session.close();
			cTemp.add(Calendar.MONTH, 1);

			tVon = Helper.cutTimestamp(new java.sql.Timestamp(cTemp.getTimeInMillis()));

		}
		String[] fieldnames = new String[] { "F_RECHNUNGSNUMMER" };
		Object[][] dataSub = new Object[alAngelegteRechnungen.size()][fieldnames.length];

		for (int k = 0; k < alAngelegteRechnungen.size(); k++) {
			dataSub[k][0] = alAngelegteRechnungen.get(k);
		}

		parameter.put("P_SUBREPORT_ANGELEGTE_RECHNUNGEN", new LPDatenSubreport(dataSub, fieldnames));
		return alDaten;
	}

	public JasperPrintLP printLiquiditaetsvorschau(BigDecimal kontostand, BigDecimal kreditlimit,
			Integer kalenderwochen, boolean bTerminNachZahlungsmoral, boolean bMitPlankosten,
			ArrayList<LiquititaetsvorschauImportDto> alPlankosten, boolean bMitOffenenAngeboten,
			boolean bMitOffenenBestellungen, boolean bMitOffenenAuftraegen, boolean bMitAbgaben,
			boolean bERTerminNachFreigabedatum, boolean bMitABZahlungsUndZeitplan, boolean bOhneAndereMandanten,
			TheClientDto theClientDto) {
		this.useCase = UC_LIQUIDITAET;
		this.index = -1;

		ArrayList<Object> alDaten = new ArrayList<Object>();

		Map<String, Object> parameter = new TreeMap<String, Object>();

		// PJ18812

		String inVerbundeneUnternehmenKunden = null;

		String inVerbundeneUnternehmenLieferanten = null;

		if (bOhneAndereMandanten) {
			ArrayList alKundeIId = getKundeIIdsVerbundeUnternehmen(theClientDto);

			if (alKundeIId.size() > 0) {
				inVerbundeneUnternehmenKunden = " (";
				Iterator it = alKundeIId.iterator();
				while (it.hasNext()) {
					inVerbundeneUnternehmenKunden += it.next();

					if (it.hasNext()) {
						inVerbundeneUnternehmenKunden += ",";
					}
				}

				inVerbundeneUnternehmenKunden += ")";
			}

			ArrayList alLieferantIId = getLieferantIIdsVerbundeUnternehmen(theClientDto);

			if (alLieferantIId.size() > 0) {
				inVerbundeneUnternehmenLieferanten = " (";
				Iterator it = alLieferantIId.iterator();
				while (it.hasNext()) {
					inVerbundeneUnternehmenLieferanten += it.next();

					if (it.hasNext()) {
						inVerbundeneUnternehmenLieferanten += ",";
					}
				}

				inVerbundeneUnternehmenLieferanten += ")";
			}

		}

		if (bMitAbgaben == true) {

			alDaten.addAll(getPersonaldkostenFuerLiquiditaetsvorschau(theClientDto, kalenderwochen,
					new Timestamp(System.currentTimeMillis())));

			boolean istVersteurer = getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
					MandantFac.ZUSATZFUNKTION_ISTVERSTEURER, theClientDto.getMandant());
			if (istVersteurer == true) {
				// Fehlermeldung
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_ABGABEN_UND_IST_VERSTEUERER_NICHT_UNTERSTUETZT,
						new Exception("FINANZ_ABGABEN_UND_IST_VERSTEUERER_NICHT_UNTERSTUETZT"));
			}

			alDaten.addAll(getMWSTDatenFuerLiquiditaetsvorschau(kalenderwochen, parameter, bOhneAndereMandanten,
					inVerbundeneUnternehmenKunden, inVerbundeneUnternehmenLieferanten, theClientDto));

		}

		Calendar c = Calendar.getInstance();
		c.add(Calendar.WEEK_OF_YEAR, kalenderwochen);

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT r FROM FLRRechnungReport r WHERE r.d_belegdatum<'"
				+ Helper.formatDateWithSlashes(new java.sql.Date(c.getTimeInMillis())) + "' AND r.status_c_nr IN('"
				+ RechnungFac.STATUS_TEILBEZAHLT + "','" + RechnungFac.STATUS_OFFEN + "') AND r.mandant_c_nr='"
				+ theClientDto.getMandant() + "' AND r.flrrechnungart.rechnungtyp_c_nr <>'"
				+ RechnungFac.RECHNUNGTYP_PROFORMARECHNUNG + "' ";

		if (bOhneAndereMandanten && inVerbundeneUnternehmenKunden != null) {
			sQuery += " AND r.kunde_i_id NOT IN " + inVerbundeneUnternehmenKunden;
		}

		sQuery += " ORDER BY r.c_nr";

		Query query = session.createQuery(sQuery);

		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {

			FLRRechnungReport flrRechnung = (FLRRechnungReport) resultListIterator.next();

			RechnungDto rechnungDto = null;
			try {
				rechnungDto = getRechnungFac().rechnungFindByPrimaryKey(flrRechnung.getI_id());
			} catch (RemoteException e1) {
				throwEJBExceptionLPRespectOld(e1);
			}

			try {

				Integer iZieltageZahlungsmoral = getRechnungFac()
						.getZahlungsmoraleinesKunden(flrRechnung.getKunde_i_id(), false, theClientDto);
				Integer iZieltageVereinbart = 0;

				Date dZieldatum = flrRechnung.getD_belegdatum();

				if (flrRechnung.getT_mahnsperrebis() != null && rechnungDto.getNMtlZahlbetrag() == null) {
					dZieldatum = flrRechnung.getT_mahnsperrebis();
				} else {

					if (bTerminNachZahlungsmoral) {

						if (flrRechnung.getZahlungsziel_i_id() != null) {

							java.sql.Date dZieldatumFuerReport = getMandantFac().berechneZielDatumFuerBelegdatum(
									flrRechnung.getD_belegdatum(), flrRechnung.getZahlungsziel_i_id(), theClientDto);

							iZieltageVereinbart = Helper.ermittleTageEinesZeitraumes(rechnungDto.getTBelegdatum(),
									dZieldatumFuerReport);
							if (iZieltageZahlungsmoral == 0) {
								iZieltageZahlungsmoral = iZieltageVereinbart;
							}

						}

						dZieldatum = Helper.addiereTageZuDatum(flrRechnung.getD_belegdatum(), iZieltageZahlungsmoral);

					} else {

						if (flrRechnung.getZahlungsziel_i_id() != null) {

							dZieldatum = getMandantFac().berechneZielDatumFuerBelegdatum(flrRechnung.getD_belegdatum(),
									flrRechnung.getZahlungsziel_i_id(), theClientDto);
							iZieltageVereinbart = Helper.ermittleTageEinesZeitraumes(rechnungDto.getTBelegdatum(),
									new java.sql.Date(dZieldatum.getTime()));

						}
					}
				}
				if (dZieldatum.before(new Date())) {
					dZieldatum = new Date();
				}

				BigDecimal bdBezahltFw = getRechnungFac().getBereitsBezahltWertVonRechnungFw(flrRechnung.getI_id(),
						null);
				BigDecimal bdBezahltUstFw = getRechnungFac()
						.getBereitsBezahltWertVonRechnungUstFw(flrRechnung.getI_id(), null);

				if (dZieldatum.before(c.getTime())) {

					BigDecimal rechnungswertFW = rechnungDto.getNWertfw();
					BigDecimal ustWertFW = rechnungDto.getNWertustfw();

					BigDecimal offen = rechnungswertFW.subtract(bdBezahltFw);

					BigDecimal offenUst = ustWertFW.subtract(bdBezahltUstFw);

					if (flrRechnung.getFlrrechnungart().getC_nr().equals(RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG)
							&& flrRechnung.getFlrauftrag() != null) {
						RechnungDto[] rechnungDtos = getRechnungFac()
								.rechnungFindByAuftragIId(flrRechnung.getFlrauftrag().getI_id());

						for (RechnungDto anzRe : rechnungDtos) {
							if (anzRe.getStatusCNr().equals(RechnungFac.STATUS_STORNIERT))
								continue;
							if (!anzRe.getRechnungartCNr().equals(RechnungFac.RECHNUNGART_ANZAHLUNG))
								continue;
							if (anzRe.getNWertfw() == null)
								continue;

							offen = offen.subtract(anzRe.getNWertfw());
							offenUst = offenUst.subtract(anzRe.getNWertustfw());

						}

					}

					offen = getLocaleFac().rechneUmInAndereWaehrungZuDatum(offen, rechnungDto.getWaehrungCNr(),
							theClientDto.getSMandantenwaehrung(), new java.sql.Date(System.currentTimeMillis()),
							theClientDto);
					offenUst = getLocaleFac().rechneUmInAndereWaehrungZuDatum(offenUst, rechnungDto.getWaehrungCNr(),
							theClientDto.getSMandantenwaehrung(), new java.sql.Date(System.currentTimeMillis()),
							theClientDto);

					Calendar cZieldatum = Calendar.getInstance();
					cZieldatum.setTimeInMillis(dZieldatum.getTime());

					if (rechnungDto.getNMtlZahlbetrag() != null && rechnungDto.getIZahltagMtlZahlbetrag() != null) {

						if (cZieldatum.get(Calendar.DAY_OF_MONTH) > rechnungDto.getIZahltagMtlZahlbetrag()) {
							cZieldatum.add(Calendar.MONTH, 1);
							cZieldatum.set(Calendar.DAY_OF_MONTH, rechnungDto.getIZahltagMtlZahlbetrag());
						}

						while (offen.doubleValue() > 0 && cZieldatum.getTime().getTime() < c.getTime().getTime()) {
							Object[] oZeile = new Object[REPORT_LV_ANZAHL_SPALTEN];
							oZeile[REPORT_LV_IMPORTIERT] = new Boolean(false);
							oZeile[REPORT_LV_ZIELTAGE_ZAHLUGNSMORAL] = iZieltageZahlungsmoral;
							oZeile[REPORT_LV_ZIELTAGE_VEREINBARTES_ZAHLUNGSZIEL] = iZieltageVereinbart;
							BigDecimal bdZahlbetrag = rechnungDto.getNMtlZahlbetrag();

							if (offen.doubleValue() < bdZahlbetrag.doubleValue()) {
								bdZahlbetrag = offen;
							}

							if (flrRechnung.getFlrrechnungart().getC_nr().equals(RechnungFac.RECHNUNGART_GUTSCHRIFT)) {
								oZeile[REPORT_LV_BELEG] = "GS" + flrRechnung.getC_nr();
								oZeile[REPORT_LV_NETTO] = bdZahlbetrag.negate();
								oZeile[REPORT_LV_MWST] = new BigDecimal(0);

							} else {
								oZeile[REPORT_LV_BELEG] = "RE" + flrRechnung.getC_nr();
								oZeile[REPORT_LV_NETTO] = bdZahlbetrag;
								oZeile[REPORT_LV_MWST] = new BigDecimal(0);
							}

							oZeile[REPORT_LV_PARTNER] = flrRechnung.getFlrkunde().getFlrpartner()
									.getC_name1nachnamefirmazeile1();

							oZeile[REPORT_LV_JAHR] = cZieldatum.get(Calendar.YEAR);
							oZeile[REPORT_LV_WOCHE] = cZieldatum.get(Calendar.WEEK_OF_YEAR);
							// wg SP1786
							oZeile[REPORT_LV_JAHR] = Helper.berechneJahrDerKW(cZieldatum);
							alDaten.add(oZeile);
							offen = offen.subtract(rechnungDto.getNMtlZahlbetrag());
							cZieldatum.add(Calendar.MONTH, 1);

						}

					} else {
						Object[] oZeile = new Object[REPORT_LV_ANZAHL_SPALTEN];
						oZeile[REPORT_LV_IMPORTIERT] = new Boolean(false);
						oZeile[REPORT_LV_ZIELTAGE_ZAHLUGNSMORAL] = iZieltageZahlungsmoral;
						oZeile[REPORT_LV_ZIELTAGE_VEREINBARTES_ZAHLUNGSZIEL] = iZieltageVereinbart;
						if (flrRechnung.getFlrrechnungart().getC_nr().equals(RechnungFac.RECHNUNGART_GUTSCHRIFT)) {
							oZeile[REPORT_LV_BELEG] = "GS" + flrRechnung.getC_nr();
							oZeile[REPORT_LV_NETTO] = offen.negate();
							oZeile[REPORT_LV_MWST] = offenUst.negate();

						} else {
							oZeile[REPORT_LV_BELEG] = "RE" + flrRechnung.getC_nr();
							oZeile[REPORT_LV_NETTO] = offen;
							oZeile[REPORT_LV_MWST] = offenUst;
						}

						oZeile[REPORT_LV_BELEGUNTERART] = flrRechnung.getFlrrechnungart().getC_nr().trim();

						oZeile[REPORT_LV_PARTNER] = flrRechnung.getFlrkunde().getFlrpartner()
								.getC_name1nachnamefirmazeile1();

						oZeile[REPORT_LV_JAHR] = cZieldatum.get(Calendar.YEAR);
						oZeile[REPORT_LV_WOCHE] = cZieldatum.get(Calendar.WEEK_OF_YEAR);

						// wg SP1786
						oZeile[REPORT_LV_JAHR] = Helper.berechneJahrDerKW(cZieldatum);

						alDaten.add(oZeile);
					}

				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}
		session.close();

		session = FLRSessionFactory.getFactory().openSession();

		sQuery = "SELECT r FROM FLREingangsrechnungReport r WHERE r.t_belegdatum<'"
				+ Helper.formatDateWithSlashes(new java.sql.Date(c.getTimeInMillis()))
				+ "' AND ((r.eingangsrechnungart_c_nr<>'" + EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN
				+ "' AND r.status_c_nr IN('" + EingangsrechnungFac.STATUS_TEILBEZAHLT + "','"
				+ EingangsrechnungFac.STATUS_ANGELEGT + "')) OR(r.eingangsrechnungart_c_nr='"
				+ EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN + "' AND r.status_c_nr IN('"
				+ EingangsrechnungFac.STATUS_TEILBEZAHLT + "','" + EingangsrechnungFac.STATUS_ERLEDIGT + "','"
				+ EingangsrechnungFac.STATUS_ANGELEGT + "') )) AND r.mandant_c_nr='" + theClientDto.getMandant() + "'";

		if (bOhneAndereMandanten && inVerbundeneUnternehmenLieferanten != null) {
			sQuery += " AND r.lieferant_i_id NOT IN " + inVerbundeneUnternehmenLieferanten;
		}

		sQuery += "  ORDER BY r.c_nr";

		query = session.createQuery(sQuery);

		results = query.list();
		resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {

			FLREingangsrechnungReport flrEingangsrechnungReport = (FLREingangsrechnungReport) resultListIterator.next();

			try {
				Date dZieldatum = flrEingangsrechnungReport.getT_belegdatum();
				if (bERTerminNachFreigabedatum) {
					dZieldatum = flrEingangsrechnungReport.getT_freigabedatum();
				}

				if (flrEingangsrechnungReport.getZahlungsziel_i_id() != null) {

					dZieldatum = getMandantFac().berechneZielDatumFuerBelegdatum(dZieldatum,
							flrEingangsrechnungReport.getZahlungsziel_i_id(), theClientDto);

				}

				EingangsrechnungDto eDto = getEingangsrechnungFac()
						.eingangsrechnungFindByPrimaryKey(flrEingangsrechnungReport.getI_id());

				BigDecimal bdBezahltFW = getEingangsrechnungFac()
						.getBezahltBetragFw(flrEingangsrechnungReport.getI_id(), null);
				BigDecimal bdBezahltUstFW = getEingangsrechnungFac()
						.getBezahltBetragUstFw(flrEingangsrechnungReport.getI_id(), null);

				bdBezahltFW = bdBezahltFW.subtract(bdBezahltUstFW);

				if (dZieldatum.before(new Date())) {
					dZieldatum = new Date();
				}

				if (dZieldatum.before(c.getTime())) {

					if (eDto.getEingangsrechnungartCNr().equals(EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN)
							&& eDto.getEingangsrechnungIIdNachfolger() == null
							&& eDto.getTWiederholenderledigt() == null && eDto.getWiederholungsintervallCNr() != null) {

						String intervall = eDto.getWiederholungsintervallCNr();

						// SP3583
						if (eDto.getStatusCNr().equals(EingangsrechnungFac.STATUS_ERLEDIGT) && intervall == null) {
							continue;
						}

						Calendar cBeginn = Calendar.getInstance();
						cBeginn.setTimeInMillis(flrEingangsrechnungReport.getT_belegdatum().getTime());
						int iZaehler = 0;
						while (cBeginn.getTime().before(c.getTime())) {

							Date tBelegdatumNeu = new Date(cBeginn.getTimeInMillis());

							// SP4168
							if (flrEingangsrechnungReport.getZahlungsziel_i_id() != null) {

								tBelegdatumNeu = getMandantFac().berechneZielDatumFuerBelegdatum(tBelegdatumNeu,
										flrEingangsrechnungReport.getZahlungsziel_i_id(), theClientDto);

							}

							if (tBelegdatumNeu.before(new Date())) {
								tBelegdatumNeu = new Date();
							}
							BigDecimal bdNettoFW = flrEingangsrechnungReport.getN_betragfw()
									.subtract(flrEingangsrechnungReport.getN_ustbetragfw());

							BigDecimal offen = null;
							BigDecimal offenUst = null;
							if (iZaehler == 0) {
								offen = bdNettoFW.subtract(bdBezahltFW);
								offenUst = flrEingangsrechnungReport.getN_ustbetragfw().subtract(bdBezahltUstFW);
							} else {
								// Bei allen zukuenftigen darf das bezahlt nicht
								// beruecksichtigt werden
								offen = bdNettoFW;
								offenUst = flrEingangsrechnungReport.getN_ustbetragfw();
							}

							offen = getLocaleFac().rechneUmInAndereWaehrungZuDatum(offen, eDto.getWaehrungCNr(),
									theClientDto.getSMandantenwaehrung(), new java.sql.Date(System.currentTimeMillis()),
									theClientDto);

							offenUst = getLocaleFac().rechneUmInAndereWaehrungZuDatum(offenUst, eDto.getWaehrungCNr(),
									theClientDto.getSMandantenwaehrung(), new java.sql.Date(System.currentTimeMillis()),
									theClientDto);
							Object[] oZeile = new Object[REPORT_LV_ANZAHL_SPALTEN];
							oZeile[REPORT_LV_IMPORTIERT] = new Boolean(false);
							oZeile[REPORT_LV_BELEG] = "ZK" + flrEingangsrechnungReport.getC_nr();
							oZeile[REPORT_LV_BEZEICHNUNG] = flrEingangsrechnungReport.getC_text();
							oZeile[REPORT_LV_BELEGUNTERART] = flrEingangsrechnungReport.getEingangsrechnungart_c_nr()
									.trim();

							oZeile[REPORT_LV_NETTO] = offen.negate();
							oZeile[REPORT_LV_MWST] = offenUst.negate();

							oZeile[REPORT_LV_PARTNER] = flrEingangsrechnungReport.getFlrlieferant().getFlrpartner()
									.getC_name1nachnamefirmazeile1();

							Calendar cZieldatum = Calendar.getInstance();
							cZieldatum.setTimeInMillis(tBelegdatumNeu.getTime());

							oZeile[REPORT_LV_JAHR] = cZieldatum.get(Calendar.YEAR);
							oZeile[REPORT_LV_WOCHE] = cZieldatum.get(Calendar.WEEK_OF_YEAR);
							// wg SP1786
							oZeile[REPORT_LV_JAHR] = Helper.berechneJahrDerKW(cZieldatum);
							alDaten.add(oZeile);

							cBeginn.setTimeInMillis(getRechnungFac()
									.berechneinterval(intervall, new java.sql.Date(cBeginn.getTimeInMillis()))
									.getTime());

							iZaehler++;
						}

					} else {

						if (eDto.getStatusCNr().equals(EingangsrechnungFac.STATUS_ERLEDIGT)) {
							continue;
						}

						BigDecimal bdNettoFW = flrEingangsrechnungReport.getN_betragfw()
								.subtract(flrEingangsrechnungReport.getN_ustbetragfw());

						BigDecimal offen = bdNettoFW.subtract(bdBezahltFW);

						BigDecimal offenUst = flrEingangsrechnungReport.getN_ustbetragfw().subtract(bdBezahltUstFW);

						if (eDto.getEingangsrechnungartCNr()
								.equals(EingangsrechnungFac.EINGANGSRECHNUNGART_SCHLUSSZAHLUNG)) {
							for (EingangsrechnungDto anzRe : getEingangsrechnungFac()
									.eingangsrechnungFindByBestellungIId(eDto.getBestellungIId())) {
								if (!anzRe.getEingangsrechnungartCNr()
										.equals(EingangsrechnungFac.EINGANGSRECHNUNGART_ANZAHLUNG))
									continue;
								if (anzRe.getStatusCNr().equals(EingangsrechnungFac.STATUS_STORNIERT))
									continue;

								offen = offen.subtract(anzRe.getNBetragfw().subtract(anzRe.getNUstBetragfw()));
								offenUst = offenUst.subtract(anzRe.getNUstBetragfw());
							}
						}

						offen = getLocaleFac().rechneUmInAndereWaehrungZuDatum(offen, eDto.getWaehrungCNr(),
								theClientDto.getSMandantenwaehrung(), new java.sql.Date(System.currentTimeMillis()),
								theClientDto);

						offenUst = getLocaleFac().rechneUmInAndereWaehrungZuDatum(offenUst, eDto.getWaehrungCNr(),
								theClientDto.getSMandantenwaehrung(), new java.sql.Date(System.currentTimeMillis()),
								theClientDto);
						Object[] oZeile = new Object[REPORT_LV_ANZAHL_SPALTEN];
						oZeile[REPORT_LV_IMPORTIERT] = new Boolean(false);
						oZeile[REPORT_LV_BELEG] = "ER" + flrEingangsrechnungReport.getC_nr();
						oZeile[REPORT_LV_BELEGUNTERART] = flrEingangsrechnungReport.getEingangsrechnungart_c_nr()
								.trim();
						oZeile[REPORT_LV_NETTO] = offen.negate();
						oZeile[REPORT_LV_MWST] = offenUst.negate();

						oZeile[REPORT_LV_PARTNER] = flrEingangsrechnungReport.getFlrlieferant().getFlrpartner()
								.getC_name1nachnamefirmazeile1();

						Calendar cZieldatum = Calendar.getInstance();
						cZieldatum.setTimeInMillis(dZieldatum.getTime());

						oZeile[REPORT_LV_JAHR] = cZieldatum.get(Calendar.YEAR);
						oZeile[REPORT_LV_WOCHE] = cZieldatum.get(Calendar.WEEK_OF_YEAR);
						// wg SP1786
						oZeile[REPORT_LV_JAHR] = Helper.berechneJahrDerKW(cZieldatum);

						alDaten.add(oZeile);

					}
				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}
		session.close();

		// PJ17841 Mit Auftraegen

		HashMap<Timestamp, BigDecimal> hmUstOffeneAuftraege = new HashMap();
		HashMap<Timestamp, BigDecimal> hmUstZeitplan = new HashMap();
		HashMap<Timestamp, BigDecimal> hmUstZahlungsplan = new HashMap();

		session = FLRSessionFactory.getFactory().openSession();

		sQuery = "SELECT a FROM FLRAuftragReport a WHERE a.t_belegdatum<'"
				+ Helper.formatDateWithSlashes(new java.sql.Date(c.getTimeInMillis()))
				+ "' AND a.auftragstatus_c_nr IN('" + AuftragServiceFac.AUFTRAGSTATUS_OFFEN + "','"
				+ AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT + "') AND a.auftragart_c_nr<>'"
				+ AuftragServiceFac.AUFTRAGART_RAHMEN + "' AND a.mandant_c_nr='" + theClientDto.getMandant() + "' ";

		if (bOhneAndereMandanten && inVerbundeneUnternehmenKunden != null) {
			sQuery += " AND a.kunde_i_id_rechnungsadresse NOT IN " + inVerbundeneUnternehmenKunden;
		}

		sQuery += " ORDER BY a.c_nr";

		query = session.createQuery(sQuery);

		results = query.list();
		resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {

			FLRAuftragReport flrAuftragReport = (FLRAuftragReport) resultListIterator.next();

			AuftragDto aDto = getAuftragFac().auftragFindByPrimaryKey(flrAuftragReport.getI_id());

			KundeDto kdDto = getKundeFac().kundeFindByPrimaryKeySmall(aDto.getKundeIIdAuftragsadresse());

			BigDecimal auftragsWertUST = null;
			BigDecimal auftragsWert = null;
			MwstsatzDto mwstsatzDtoAktuell = null;
			try {

//				mwstsatzDtoAktuell = getMandantFac().mwstsatzFindByMwstsatzbezIIdAktuellster(kdDto.getMwstsatzbezIId(),
//						theClientDto);
				mwstsatzDtoAktuell = getMandantFac().mwstsatzZuDatumValidate(kdDto.getMwstsatzbezIId(),
						aDto.getTBelegdatum(), theClientDto);

				if (flrAuftragReport.getN_gesamtauftragswertinauftragswaehrung() == null) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_AUFTRAG_AKTIVIERT_ABER_KEIN_WERT,
							"Auftrag " + flrAuftragReport.getC_nr() + " mit Status "
									+ flrAuftragReport.getAuftragstatus_c_nr() + " hat keinen Wert (null)",
							flrAuftragReport.getC_nr(), flrAuftragReport.getAuftragstatus_c_nr().trim());
				}

				auftragsWert = getLocaleFac().rechneUmInAndereWaehrungZuDatum(
						flrAuftragReport.getN_gesamtauftragswertinauftragswaehrung(), aDto.getCAuftragswaehrung(),
						theClientDto.getSMandantenwaehrung(), new java.sql.Date(System.currentTimeMillis()),
						theClientDto);
				auftragsWertUST = getBelegVerkaufFac().getGesamtwertInMandantwaehrungUST(
						getAuftragpositionFac().auftragpositionFindByAuftrag(aDto.getIId()), aDto, theClientDto);

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			if (flrAuftragReport.getAuftragart_c_nr().equals(AuftragServiceFac.AUFTRAGART_WIEDERHOLEND)) {
				String intervall = aDto.getWiederholungsintervallCNr();

				Calendar cBeginn = Calendar.getInstance();
				cBeginn.setTimeInMillis(aDto.getTLauftermin().getTime());
				int iZaehler = 0;
				while (cBeginn.getTime().before(c.getTime())) {

					if (cBeginn.getTime().after(new java.sql.Timestamp(System.currentTimeMillis()))) {
						// Wenn Der Lauftermin nach heute ist, dann ist der
						// Wiederholdene Auftrag enthalten

						Date tBelegdatumNeu = new Date(cBeginn.getTimeInMillis());

						Object[] oZeile = new Object[REPORT_LV_ANZAHL_SPALTEN];
						oZeile[REPORT_LV_IMPORTIERT] = new Boolean(false);
						oZeile[REPORT_LV_BELEG] = "AW" + flrAuftragReport.getC_nr();

						// SP7797
						if (Helper.short2boolean(flrAuftragReport.getFlrverrechenbar().getB_verrechenbar())) {
							oZeile[REPORT_LV_NETTO] = auftragsWert;
							oZeile[REPORT_LV_MWST] = auftragsWertUST;
						} else {
							oZeile[REPORT_LV_NETTO] = BigDecimal.ZERO;
							oZeile[REPORT_LV_MWST] = BigDecimal.ZERO;
						}

						oZeile[REPORT_LV_PARTNER] = flrAuftragReport.getFlrkunde().getFlrpartner()
								.getC_name1nachnamefirmazeile1();

						Calendar cZieldatum = Calendar.getInstance();
						cZieldatum.setTimeInMillis(tBelegdatumNeu.getTime());

						oZeile[REPORT_LV_JAHR] = cZieldatum.get(Calendar.YEAR);
						oZeile[REPORT_LV_WOCHE] = cZieldatum.get(Calendar.WEEK_OF_YEAR);
						// wg SP1786
						oZeile[REPORT_LV_JAHR] = Helper.berechneJahrDerKW(cZieldatum);
						alDaten.add(oZeile);

					}

					if (AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_WOECHENTLICH.equals(intervall)) {
						cBeginn.add(Calendar.DAY_OF_MONTH, 14);
					} else if (AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_WOECHENTLICH.equals(intervall)) {
						cBeginn.add(Calendar.DAY_OF_MONTH, 7);
					} else if (AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_JAHR.equals(intervall)) {
						cBeginn.add(Calendar.YEAR, 1);
					} else if (AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_2JAHR.equals(intervall)) {
						cBeginn.add(Calendar.YEAR, 2);
					} else if (AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_3JAHR.equals(intervall)) {
						cBeginn.add(Calendar.YEAR, 3);
					} else if (AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_4JAHR.equals(intervall)) {
						cBeginn.add(Calendar.YEAR, 4);
					} else if (AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_5JAHR.equals(intervall)) {
						cBeginn.add(Calendar.YEAR, 5);
					} else if (AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_MONATLICH.equals(intervall)) {
						cBeginn.add(Calendar.MONTH, 1);
					} else if (AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_QUARTAL.equals(intervall)) {
						cBeginn.add(Calendar.MONTH, 3);
					} else if (AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_HALBJAHR.equals(intervall)) {
						cBeginn.add(Calendar.MONTH, 6);
					} else {
						// nicht definiert: abbruch, dan ansonsten
						// Endlosschleife
						break;
					}
					iZaehler++;
				}

			} else {

				if (bMitOffenenAuftraegen == true) {

					Date dZieldatum = flrAuftragReport.getT_finaltermin();

					if (dZieldatum.before(new Date())) {
						dZieldatum = new Date();
					}

					try {
						Integer iZieltageVereinbart = 0;
						Integer iZieltageZahlungsmoral = getRechnungFac().getZahlungsmoraleinesKunden(
								flrAuftragReport.getFlrkunde().getI_id(), false, theClientDto);
						if (bTerminNachZahlungsmoral) {

							if (flrAuftragReport.getZahlungsziel_i_id() != null) {

								java.sql.Date dZieldatumFuerReport = getMandantFac().berechneZielDatumFuerBelegdatum(
										dZieldatum, flrAuftragReport.getZahlungsziel_i_id(), theClientDto);

								iZieltageVereinbart = Helper.ermittleTageEinesZeitraumes(
										new java.sql.Date(dZieldatum.getTime()), dZieldatumFuerReport);
								if (iZieltageZahlungsmoral == 0) {
									iZieltageZahlungsmoral = iZieltageVereinbart;
								}

							}

							dZieldatum = Helper.addiereTageZuDatum(dZieldatum, iZieltageZahlungsmoral);

						} else {
							dZieldatum = getMandantFac().berechneZielDatumFuerBelegdatum(dZieldatum,
									flrAuftragReport.getZahlungsziel_i_id(), theClientDto);
						}

						Object[] oZeile = new Object[REPORT_LV_ANZAHL_SPALTEN];
						oZeile[REPORT_LV_IMPORTIERT] = new Boolean(false);
						oZeile[REPORT_LV_BELEG] = "AB" + flrAuftragReport.getC_nr();

						BigDecimal offenerAuftragsWert = flrAuftragReport.getN_gesamtauftragswertinauftragswaehrung();
						BigDecimal offenerAuftragsWertUst = getBelegVerkaufFac().getGesamtwertInBelegswaehrungUST(
								getAuftragpositionFac().auftragpositionFindByAuftrag(aDto.getIId()), aDto,
								theClientDto);

						// 2015-06-02 tel mit WH: Der offene Auftragswert
						// ist der volle Auftragswert abzgl. aller
						// vorhandenen Rechnungen (Achtung: Gutschriften
						// muessen beruecksichtigt werden). Wenn bereits
						// eine Schlussrechnung vorhanden ist, dann zaehlt
						// nur diese.
						// von diesem Ergebnis werden dann noch alle offenen
						// Punkte des Zahlungsplans abgezogen (wenn
						// angehakt)

						offenerAuftragsWert = getLocaleFac().rechneUmInAndereWaehrungZuDatum(offenerAuftragsWert,
								aDto.getCAuftragswaehrung(), theClientDto.getSMandantenwaehrung(),
								new java.sql.Date(System.currentTimeMillis()), theClientDto);

						offenerAuftragsWertUst = getLocaleFac().rechneUmInAndereWaehrungZuDatum(offenerAuftragsWertUst,
								aDto.getCAuftragswaehrung(), theClientDto.getSMandantenwaehrung(),
								new java.sql.Date(System.currentTimeMillis()), theClientDto);

						// Zahlungsplan und Zeitplan hinzufuegen
						if (bMitABZahlungsUndZeitplan) {

							Session sessionZahlungsplan = FLRSessionFactory.getFactory().openSession();
							String queryStringZahlungsplan = "SELECT zp FROM FLRZahlungsplan zp WHERE zp.flrauftrag.i_id= "
									+ flrAuftragReport.getI_id() + " ORDER BY zp.t_termin ASC";

							org.hibernate.Query queryZahlungsplan = sessionZahlungsplan
									.createQuery(queryStringZahlungsplan);
							List<?> resultListZahlungsplan = queryZahlungsplan.list();

							Iterator<?> resultListIteratorZahlungsplan = resultListZahlungsplan.iterator();
							BigDecimal bdSummeZahlungsplan = BigDecimal.ZERO;
							BigDecimal bdSummeZahlungsplanMWST = BigDecimal.ZERO;

							int i = 0;

							while (resultListIteratorZahlungsplan.hasNext()) {
								i++;
								FLRZahlungsplan zp = (FLRZahlungsplan) resultListIteratorZahlungsplan.next();

								boolean alleErledigt = true;
								Iterator it = zp.getFlrzahlungsplanmeilenstein().iterator();
								while (it.hasNext()) {
									FLRZahlungsplanmeilenstein zpm = (FLRZahlungsplanmeilenstein) it.next();
									if (zpm.getFlrpersonalerledigt() == null) {
										alleErledigt = false;
									}

								}

								if (alleErledigt == true) {
									continue;
								}

								Object[] oZeileZeitplan = new Object[REPORT_LV_ANZAHL_SPALTEN];
								oZeileZeitplan[REPORT_LV_IMPORTIERT] = new Boolean(false);
								oZeileZeitplan[REPORT_LV_BELEG] = "ZP MS " + i + " " + flrAuftragReport.getC_nr();
								oZeileZeitplan[REPORT_LV_PARTNER] = flrAuftragReport.getFlrkunde().getFlrpartner()
										.getC_name1nachnamefirmazeile1();
								oZeileZeitplan[REPORT_LV_NETTO] = zp.getN_betrag();

								oZeileZeitplan[REPORT_LV_MWST] = BigDecimal.ZERO;
								// fuer MWST-Berechnung Satz den Kudnen
								// verwenden
								BigDecimal bdMwstbetrag = BigDecimal.ZERO;
								if (mwstsatzDtoAktuell != null) {

									bdMwstbetrag = Helper.getProzentWert(zp.getN_betrag(),
											new BigDecimal(mwstsatzDtoAktuell.getFMwstsatz()), 2);

									oZeileZeitplan[REPORT_LV_MWST] = bdMwstbetrag;

									bdSummeZahlungsplanMWST = bdSummeZahlungsplanMWST.add(bdMwstbetrag);

								}

								add2VST_USTMap(hmUstZahlungsplan, zp.getT_termin(), bdMwstbetrag, kalenderwochen,
										theClientDto);

								Calendar cZieldatum = Calendar.getInstance();
								cZieldatum
										.setTimeInMillis(getMandantFac()
												.berechneZielDatumFuerBelegdatum(zp.getT_termin(),
														zp.getFlrauftrag().getZahlungsziel_i_id(), theClientDto)
												.getTime());

								// SP3952
								if (cZieldatum.getTime().before(new Date())) {
									cZieldatum.setTime(new Date());
								}

								oZeileZeitplan[REPORT_LV_JAHR] = cZieldatum.get(Calendar.YEAR);
								oZeileZeitplan[REPORT_LV_WOCHE] = cZieldatum.get(Calendar.WEEK_OF_YEAR);
								// wg SP1786
								oZeileZeitplan[REPORT_LV_JAHR] = Helper.berechneJahrDerKW(cZieldatum);

								bdSummeZahlungsplan = bdSummeZahlungsplan.add(zp.getN_betrag());

								if (cZieldatum.getTime().before(c.getTime())) {
									alDaten.add(oZeileZeitplan);
								}

							}

							sessionZahlungsplan.close();

							// Auftragswert reduzieren
							offenerAuftragsWert = offenerAuftragsWert.subtract(bdSummeZahlungsplan);
							offenerAuftragsWertUst = offenerAuftragsWertUst.subtract(bdSummeZahlungsplanMWST);

							Session sessionZeitplan = FLRSessionFactory.getFactory().openSession();
							String queryString = "SELECT zp FROM FLRZeitplan zp WHERE zp.flrauftrag.i_id= "
									+ flrAuftragReport.getI_id()
									+ " AND zp.t_material_erledigt IS NULL ORDER BY zp.t_termin ASC";

							org.hibernate.Query queryZeitplan = sessionZeitplan.createQuery(queryString);
							List<?> resultListZeitplan = queryZeitplan.list();

							MandantDto mandantDto = null;
							try {
								mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(),
										theClientDto);
							} catch (RemoteException e) {
								throwEJBExceptionLPRespectOld(e);
							}

							Iterator<?> resultListIteratorZeitplan = resultListZeitplan.iterator();

							while (resultListIteratorZeitplan.hasNext()) {
								FLRZeitplan zp = (FLRZeitplan) resultListIteratorZeitplan.next();

								Object[] oZeileZeitplan = new Object[REPORT_LV_ANZAHL_SPALTEN];
								oZeileZeitplan[REPORT_LV_IMPORTIERT] = new Boolean(false);
								oZeileZeitplan[REPORT_LV_BELEG] = "ZT" + flrAuftragReport.getC_nr();
								oZeileZeitplan[REPORT_LV_BEZEICHNUNG] = zp.getC_kommentar();
								oZeileZeitplan[REPORT_LV_PARTNER] = flrAuftragReport.getFlrkunde().getFlrpartner()
										.getC_name1nachnamefirmazeile1();
								oZeileZeitplan[REPORT_LV_NETTO] = zp.getN_material().negate();

								oZeileZeitplan[REPORT_LV_MWST] = BigDecimal.ZERO;
								// fuer MWST-Berechnung Satz den Kudnen
								// verwenden
								BigDecimal bdMwstbetrag = BigDecimal.ZERO;
								if (mwstsatzDtoAktuell != null) {

									bdMwstbetrag = Helper.getProzentWert(zp.getN_material(),
											new BigDecimal(mwstsatzDtoAktuell.getFMwstsatz()), 2);

									oZeileZeitplan[REPORT_LV_MWST] = bdMwstbetrag.negate();

								}

								Calendar cZieldatum = Calendar.getInstance();
								cZieldatum
										.setTimeInMillis(
												getMandantFac()
														.berechneZielDatumFuerBelegdatum(zp.getT_termin(),
																mandantDto.getZahlungszielIIdLF(), theClientDto)
														.getTime());

								// SP3952
								if (cZieldatum.getTime().before(new Date())) {
									cZieldatum.setTime(new Date());
								}

								oZeileZeitplan[REPORT_LV_JAHR] = cZieldatum.get(Calendar.YEAR);
								oZeileZeitplan[REPORT_LV_WOCHE] = cZieldatum.get(Calendar.WEEK_OF_YEAR);
								// wg SP1786
								oZeileZeitplan[REPORT_LV_JAHR] = Helper.berechneJahrDerKW(cZieldatum);

								add2VST_USTMap(hmUstZeitplan, zp.getT_termin(), bdMwstbetrag, kalenderwochen,
										theClientDto);
								if (cZieldatum.getTime().before(c.getTime())) {
									alDaten.add(oZeileZeitplan);
								}

							}

							sessionZeitplan.close();

						}

						RechnungDto[] alleREsDesAuftrags = getRechnungFac()
								.rechnungFindByAuftragIId(flrAuftragReport.getI_id());

						boolean bSchlussrechnungVorhanden = false;

						for (int i = 0; i < alleREsDesAuftrags.length; i++) {
							if (!alleREsDesAuftrags[i].getStatusCNr().equals(RechnungFac.STATUS_STORNIERT)) {

								if (alleREsDesAuftrags[i].getRechnungartCNr()
										.equals(RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG)) {
									bSchlussrechnungVorhanden = true;
									break;
								}

							}
						}

						for (int i = 0; i < alleREsDesAuftrags.length; i++) {
							if (!alleREsDesAuftrags[i].getStatusCNr().equals(RechnungFac.STATUS_STORNIERT)) {

								if (bSchlussrechnungVorhanden == false
										|| (bSchlussrechnungVorhanden == true && !alleREsDesAuftrags[i]
												.getRechnungartCNr().equals(RechnungFac.RECHNUNGART_ANZAHLUNG))) {

									if (alleREsDesAuftrags[i].getNWert() != null) {
										offenerAuftragsWert = offenerAuftragsWert
												.subtract(alleREsDesAuftrags[i].getNWert());
									}

									if (alleREsDesAuftrags[i].getNWertust() != null) {
										offenerAuftragsWertUst = offenerAuftragsWertUst
												.subtract(alleREsDesAuftrags[i].getNWertust());
									}

									// GS

									RechnungDto[] gsDtos = getRechnungFac()
											.rechnungFindByRechnungIIdZuRechnung(alleREsDesAuftrags[i].getIId());

									for (int j = 0; j < gsDtos.length; j++) {
										if (!gsDtos[j].getStatusCNr().equals(RechnungFac.STATUS_STORNIERT)
												&& gsDtos[j].getNWert() != null) {

											offenerAuftragsWert = offenerAuftragsWert.add(gsDtos[j].getNWert());
											offenerAuftragsWertUst = offenerAuftragsWertUst
													.add(gsDtos[j].getNWertust());

										}
									}

								}

							}
						}

						// SP7797
						if (Helper.short2boolean(flrAuftragReport.getFlrverrechenbar().getB_verrechenbar())) {
							oZeile[REPORT_LV_NETTO] = offenerAuftragsWert;
							oZeile[REPORT_LV_MWST] = offenerAuftragsWertUst;
						} else {
							oZeile[REPORT_LV_NETTO] = BigDecimal.ZERO;
							oZeile[REPORT_LV_MWST] = BigDecimal.ZERO;
						}

						oZeile[REPORT_LV_ZIELTAGE_ZAHLUGNSMORAL] = iZieltageZahlungsmoral;
						oZeile[REPORT_LV_ZIELTAGE_VEREINBARTES_ZAHLUNGSZIEL] = iZieltageVereinbart;
						oZeile[REPORT_LV_PARTNER] = flrAuftragReport.getFlrkunde().getFlrpartner()
								.getC_name1nachnamefirmazeile1();

						Calendar cZieldatum = Calendar.getInstance();
						cZieldatum.setTimeInMillis(dZieldatum.getTime());

						oZeile[REPORT_LV_JAHR] = cZieldatum.get(Calendar.YEAR);
						oZeile[REPORT_LV_WOCHE] = cZieldatum.get(Calendar.WEEK_OF_YEAR);
						// wg SP1786
						oZeile[REPORT_LV_JAHR] = Helper.berechneJahrDerKW(cZieldatum);

						if (cZieldatum.getTime().before(c.getTime())) {

							alDaten.add(oZeile);
						}

						add2VST_USTMap(hmUstOffeneAuftraege, flrAuftragReport.getT_finaltermin(),
								offenerAuftragsWertUst, kalenderwochen, theClientDto);

					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}

				}
			}

		}
		session.close();
		if (bMitAbgaben) {

			alDaten = alAddUST_VST2Liquiditaetsvorschau(alDaten, hmUstOffeneAuftraege, "UST OFFENE AB", true,
					theClientDto);
			alDaten = alAddUST_VST2Liquiditaetsvorschau(alDaten, hmUstZahlungsplan, "UST ZP", true, theClientDto);
			alDaten = alAddUST_VST2Liquiditaetsvorschau(alDaten, hmUstZeitplan, "VST ZT", false, theClientDto);
		}

		if (bMitOffenenAngeboten == true) {

			HashMap<Timestamp, BigDecimal> hmUst = new HashMap();

			session = FLRSessionFactory.getFactory().openSession();
			sQuery = "SELECT a FROM FLRAngebot a WHERE a.t_belegdatum<'"
					+ Helper.formatDateWithSlashes(new java.sql.Date(c.getTimeInMillis()))
					+ "' AND a.angebotstatus_c_nr ='" + AngebotServiceFac.ANGEBOTSTATUS_OFFEN + "' AND a.mandant_c_nr='"
					+ theClientDto.getMandant() + "' ";

			if (bOhneAndereMandanten && inVerbundeneUnternehmenKunden != null) {
				sQuery += " AND a.kunde_i_id_angebotsadresse NOT IN " + inVerbundeneUnternehmenKunden;

			}

			sQuery += " ORDER BY a.c_nr";

			query = session.createQuery(sQuery);

			results = query.list();
			resultListIterator = results.iterator();
			while (resultListIterator.hasNext()) {

				FLRAngebot flrAngebot = (FLRAngebot) resultListIterator.next();
				Object[] oZeile = new Object[REPORT_LV_ANZAHL_SPALTEN];

				Date dZieldatum = flrAngebot.getT_realisierungstermin();

				if (dZieldatum == null) {
					Calendar cReal = Calendar.getInstance();
					cReal.set(Calendar.YEAR, 9999);
					cReal.set(Calendar.DAY_OF_YEAR, 1);

					dZieldatum = cReal.getTime();

				}

				if (dZieldatum.before(new Date())) {
					dZieldatum = new Date();
				}

				Date dZieldatumFuerUST = new Date(dZieldatum.getTime());

				try {
					AngebotDto angebotDto = null;

					angebotDto = getAngebotFac().angebotFindByPrimaryKey(flrAngebot.getI_id(), theClientDto);
					Integer iZieltageVereinbart = 0;
					Integer iZieltageZahlungsmoral = getRechnungFac()
							.getZahlungsmoraleinesKunden(flrAngebot.getFlrkunde().getI_id(), false, theClientDto);

					if (flrAngebot.getT_realisierungstermin() != null) {

						if (bTerminNachZahlungsmoral) {

							if (angebotDto.getZahlungszielIId() != null) {

								java.sql.Date dZieldatumFuerReport = getMandantFac().berechneZielDatumFuerBelegdatum(
										dZieldatum, angebotDto.getZahlungszielIId(), theClientDto);

								iZieltageVereinbart = Helper.ermittleTageEinesZeitraumes(
										new java.sql.Date(dZieldatum.getTime()), dZieldatumFuerReport);
								if (iZieltageZahlungsmoral == 0) {
									iZieltageZahlungsmoral = iZieltageVereinbart;
								}

							}

							dZieldatum = Helper.addiereTageZuDatum(dZieldatum, iZieltageZahlungsmoral);

						} else {
							dZieldatum = getMandantFac().berechneZielDatumFuerBelegdatum(dZieldatum,
									angebotDto.getZahlungszielIId(), theClientDto);
						}

					}

					if (dZieldatum.before(c.getTime()) || flrAngebot.getT_realisierungstermin() == null) {

						oZeile[REPORT_LV_IMPORTIERT] = new Boolean(false);
						oZeile[REPORT_LV_BELEG] = "AG" + flrAngebot.getC_nr();

						BigDecimal angebotsWert = null;
						BigDecimal angebotsWertUST = null;
						try {
							angebotsWert = getLocaleFac().rechneUmInAndereWaehrungZuDatum(
									flrAngebot.getN_gesamtangebotswertinangebotswaehrung(), angebotDto.getWaehrungCNr(),
									theClientDto.getSMandantenwaehrung(), new java.sql.Date(System.currentTimeMillis()),
									theClientDto);

							angebotsWertUST = getBelegVerkaufFac().getGesamtwertInMandantwaehrungUST(
									getAngebotpositionFac()
											// .angebotpositionFindByAngebotIIdOhneAlternative(
											.angebotpositionFindByAngebotIId(angebotDto.getIId(), theClientDto),
									angebotDto, theClientDto);

							if (angebotDto.getFAuftragswahrscheinlichkeit().doubleValue() != 0) {
								angebotsWert = angebotsWert.multiply(new BigDecimal(
										angebotDto.getFAuftragswahrscheinlichkeit().doubleValue() / 100));
								angebotsWertUST = angebotsWertUST.multiply(new BigDecimal(
										angebotDto.getFAuftragswahrscheinlichkeit().doubleValue() / 100));
							}

						} catch (RemoteException e) {
							throwEJBExceptionLPRespectOld(e);
						}

						hmUst = add2VST_USTMap(hmUst, dZieldatumFuerUST, angebotsWertUST, kalenderwochen, theClientDto);

						oZeile[REPORT_LV_NETTO] = angebotsWert;
						oZeile[REPORT_LV_MWST] = angebotsWertUST;

						oZeile[REPORT_LV_PARTNER] = flrAngebot.getFlrkunde().getFlrpartner()
								.getC_name1nachnamefirmazeile1();

						Calendar cZieldatum = Calendar.getInstance();
						cZieldatum.setTimeInMillis(dZieldatum.getTime());

						oZeile[REPORT_LV_JAHR] = cZieldatum.get(Calendar.YEAR);
						oZeile[REPORT_LV_WOCHE] = cZieldatum.get(Calendar.WEEK_OF_YEAR);
						// wg SP1786
						oZeile[REPORT_LV_JAHR] = Helper.berechneJahrDerKW(cZieldatum);
						oZeile[REPORT_LV_ZIELTAGE_ZAHLUGNSMORAL] = iZieltageZahlungsmoral;
						oZeile[REPORT_LV_ZIELTAGE_VEREINBARTES_ZAHLUNGSZIEL] = iZieltageVereinbart;

						alDaten.add(oZeile);
					}
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}
			session.close();

			if (bMitAbgaben) {

				alDaten = alAddUST_VST2Liquiditaetsvorschau(alDaten, hmUst, "UST OFFENE AG", true, theClientDto);
			}

		}
		if (bMitOffenenBestellungen == true) {

			HashSet hsBereitsVerwendeteBestellungen = new HashSet();
			HashMap<Integer, ArrayList<Object[]>> hmAnzahlungen = new HashMap();
			HashSet hsBereitsVerwendeteBestellungenFuerAnzahlung = new HashSet();

			HashMap<Timestamp, BigDecimal> hmUstBS = new HashMap();

			session = FLRSessionFactory.getFactory().openSession();
			sQuery = "SELECT b FROM FLRBestellposition b WHERE b.flrbestellung.t_belegdatum<'"
					+ Helper.formatDateWithSlashes(new java.sql.Date(c.getTimeInMillis()))
					+ "' AND b.n_menge IS NOT NULL AND  b.flrbestellung.bestellungstatus_c_nr NOT IN ('"
					+ BestellungFac.BESTELLSTATUS_ERLEDIGT + "','" + BestellungFac.BESTELLSTATUS_STORNIERT
					+ "')  AND b.bestellpositionstatus_c_nr IN('" + BestellpositionFac.BESTELLPOSITIONSTATUS_OFFEN
					+ "','" + BestellpositionFac.BESTELLPOSITIONSTATUS_BESTAETIGT + "','"
					+ BestellpositionFac.BESTELLPOSITIONSTATUS_GELIEFERT + "','"
					+ BestellpositionFac.BESTELLPOSITIONSTATUS_TEILGELIEFERT
					+ "') AND b.flrbestellung.bestellungart_c_nr IN ('"
					+ BestellungFac.BESTELLUNGART_FREIE_BESTELLUNG_C_NR + "','"
					+ BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR // SP5729
																		// mit
																		// Abrufbestellungen
					+ "') AND b.flrbestellung.mandant_c_nr='" + theClientDto.getMandant() + "'";

			if (bOhneAndereMandanten && inVerbundeneUnternehmenLieferanten != null) {
				sQuery += " AND b.flrbestellung.lieferant_i_id_bestelladresse NOT IN "
						+ inVerbundeneUnternehmenLieferanten;
			}

			sQuery += " ORDER BY b.flrbestellung.c_nr, b.i_sort";

			query = session.createQuery(sQuery);

			results = query.list();
			resultListIterator = results.iterator();
			while (resultListIterator.hasNext()) {

				FLRBestellposition flrBestellposition = (FLRBestellposition) resultListIterator.next();

				Object[] oZeile = new Object[REPORT_LV_ANZAHL_SPALTEN];

				Date dZieldatum = flrBestellposition.getT_uebersteuerterliefertermin();

				System.out.println(flrBestellposition.getFlrbestellung().getC_nr());

				if (flrBestellposition.getT_auftragsbestaetigungstermin() != null) {
					dZieldatum = flrBestellposition.getT_auftragsbestaetigungstermin();
				}

				if (dZieldatum.before(new Date())) {
					dZieldatum = new Date();
				}
				try {
					LieferantDto lfDto = getLieferantFac().lieferantFindByPrimaryKey(
							flrBestellposition.getFlrbestellung().getFlrlieferant().getI_id(), theClientDto);

					Date dZieldatumFuerVorsteuer = new Date(dZieldatum.getTime());

					Integer iZieltageVereinbart = 0;

					dZieldatum = getMandantFac().berechneZielDatumFuerBelegdatum(dZieldatum,
							flrBestellposition.getFlrbestellung().getFlrzahlungsziel().getId(), theClientDto);

					if (dZieldatum.before(c.getTime())) {

						String bestellnummer = flrBestellposition.getFlrbestellung().getC_nr() + "/"
								+ getBestellpositionFac().getPositionNummer(flrBestellposition.getI_id(), theClientDto);

						oZeile[REPORT_LV_IMPORTIERT] = new Boolean(false);

						WareneingangspositionDto[] weposDtos = getWareneingangFac()
								.wareneingangspositionFindByBestellpositionIId(flrBestellposition.getI_id());

						BigDecimal posWert = new BigDecimal(0);

						BestellpositionDto besPos = getBestellpositionFac()
								.bestellpositionFindByPrimaryKey(flrBestellposition.getI_id());

						MwstsatzDto mwstsatzDtoAktuell = getMandantFac().mwstsatzFindZuDatum(lfDto.getMwstsatzbezIId(),
								new Timestamp(flrBestellposition.getFlrbestellung().getT_belegdatum().getTime()));

						oZeile[REPORT_LV_PARTNER] = flrBestellposition.getFlrbestellung().getFlrlieferant()
								.getFlrpartner().getC_name1nachnamefirmazeile1();

						Calendar cZieldatum = Calendar.getInstance();
						cZieldatum.setTimeInMillis(dZieldatum.getTime());

						oZeile[REPORT_LV_JAHR] = cZieldatum.get(Calendar.YEAR);
						oZeile[REPORT_LV_WOCHE] = cZieldatum.get(Calendar.WEEK_OF_YEAR);
						// wg SP1786
						oZeile[REPORT_LV_JAHR] = Helper.berechneJahrDerKW(cZieldatum);
						oZeile[REPORT_LV_ZIELTAGE_VEREINBARTES_ZAHLUNGSZIEL] = iZieltageVereinbart;

						if (flrBestellposition.getFlrartikel() != null) {
							ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(
									flrBestellposition.getFlrartikel().getI_id(), theClientDto);
							oZeile[REPORT_LV_ARTIKELNUMMER] = aDto.getCNr();

							if (aDto.getArtikelsprDto() != null) {

								oZeile[REPORT_LV_BEZEICHNUNG] = aDto.getArtikelsprDto().getCBez();
								oZeile[REPORT_LV_ZUSATZBEZEICHNUNG] = aDto.getArtikelsprDto().getCZbez();
							}

						} else {

							oZeile[REPORT_LV_BEZEICHNUNG] = flrBestellposition.getC_bezeichnung();
							oZeile[REPORT_LV_ZUSATZBEZEICHNUNG] = flrBestellposition.getC_zusatzbezeichnung();
						}

						// lt. WH: Wenn Zahlungsplan vorhanden, dann darf nur
						// dieser angedruckt werden
						boolean bZahlungsplanvorhanden = false;
						Session sessionTemp = FLRSessionFactory.getFactory().openSession();
						String sQueryTemp = "SELECT zp FROM FLRBSZahlungsplan zp WHERE zp.flrbestellung.i_id= "
								+ flrBestellposition.getFlrbestellung().getI_id() + " ORDER BY zp.t_termin ASC";

						Query queryTemp = sessionTemp.createQuery(sQueryTemp);
						List resultListTemp = queryTemp.list();

						if (resultListTemp.size() > 0 && bMitABZahlungsUndZeitplan == true) {
							bZahlungsplanvorhanden = true;
						}

						sessionTemp.close();

						// Anzahlungen zur Bestellung hinzufuegen

						// SP3604

						if (!hmAnzahlungen.containsKey(flrBestellposition.getFlrbestellung().getI_id())) {

							ArrayList alAnzahlungen = new ArrayList();

							EingangsrechnungDto[] erDtos = getEingangsrechnungFac().eingangsrechnungFindByBestellungIId(
									flrBestellposition.getFlrbestellung().getI_id());

							for (int i = 0; i < erDtos.length; i++) {
								EingangsrechnungDto erDto = erDtos[i];
								if (erDto.getEingangsrechnungartCNr()
										.equals(EingangsrechnungFac.EINGANGSRECHNUNGART_ANZAHLUNG)) {

									if (!erDto.getStatusCNr().equals(EingangsrechnungFac.STATUS_STORNIERT)) {

										BigDecimal bdNettoFW = erDto.getNBetragfw().subtract(erDto.getNUstBetragfw());

										Object[] oZeileAnz = new Object[REPORT_LV_ANZAHL_SPALTEN];

										oZeileAnz[REPORT_LV_BELEG] = "BS"
												+ flrBestellposition.getFlrbestellung().getC_nr() + "/AZ";
										oZeileAnz[REPORT_LV_NETTO] = bdNettoFW;
										oZeileAnz[REPORT_LV_MWST] = erDto.getNUstBetragfw();

										oZeileAnz[REPORT_LV_IMPORTIERT] = new Boolean(false);

										// SP3992 Nur hinzufuegen, wenn kein
										// Zahlungsplan auf der Bestellung
										// vorhanden
										Session sessionBSZP = FLRSessionFactory.getFactory().openSession();

										String queryStringBSZP = "SELECT zp FROM FLRBSZahlungsplan zp WHERE zp.flrbestellung.i_id= "
												+ flrBestellposition.getFlrbestellung().getI_id()
												+ " ORDER BY zp.t_termin ASC";

										Query queryBSZP = sessionBSZP.createQuery(queryStringBSZP);
										List resultListBSZP = queryBSZP.list();
										if (resultListBSZP.size() == 0) {
											alAnzahlungen.add(oZeileAnz);
										}

										sessionBSZP.close();

									}
								}

							}

							hmAnzahlungen.put(flrBestellposition.getFlrbestellung().getI_id(), alAnzahlungen);

						}

						// ---SP3604 Anzahlungen muessen immer abgezogen werden

						if (!hsBereitsVerwendeteBestellungenFuerAnzahlung
								.contains(flrBestellposition.getFlrbestellung().getI_id())) {

							hsBereitsVerwendeteBestellungenFuerAnzahlung
									.add(flrBestellposition.getFlrbestellung().getI_id());

							if (hmAnzahlungen.containsKey(flrBestellposition.getFlrbestellung().getI_id())) {

								ArrayList<Object[]> al = hmAnzahlungen
										.get(flrBestellposition.getFlrbestellung().getI_id());
								for (int i = 0; i < al.size(); i++) {

									Object[] zeileAnz = al.get(i);

									zeileAnz[REPORT_LV_JAHR] = oZeile[REPORT_LV_JAHR];
									zeileAnz[REPORT_LV_WOCHE] = oZeile[REPORT_LV_WOCHE];
									zeileAnz[REPORT_LV_PARTNER] = oZeile[REPORT_LV_PARTNER];

									add2VST_USTMap(hmUstBS, dZieldatumFuerVorsteuer,
											((BigDecimal) zeileAnz[REPORT_LV_MWST]).negate(), kalenderwochen,
											theClientDto);

									alDaten.add(zeileAnz);
								}

								hmAnzahlungen.remove(flrBestellposition.getFlrbestellung().getI_id());
							}

						}

						if (weposDtos.length == 0 || bZahlungsplanvorhanden == true) {
							// Noch keine Wareneingang, also Bestellpreis
							// verwenden
							Object[] zeileTemp = oZeile.clone();

							zeileTemp[REPORT_LV_BELEG] = "BS" + bestellnummer;

							posWert = besPos.getNNettogesamtPreisminusRabatte().multiply(besPos.getNMenge());

							// Zahlungsplan hinzufuegen
							if (bMitABZahlungsUndZeitplan) {
								if (!hsBereitsVerwendeteBestellungen
										.contains(flrBestellposition.getFlrbestellung().getI_id())) {
									hsBereitsVerwendeteBestellungen
											.add(flrBestellposition.getFlrbestellung().getI_id());
									Session sessionBSZP = FLRSessionFactory.getFactory().openSession();
									String queryStringBSZP = "SELECT zp FROM FLRBSZahlungsplan zp WHERE zp.flrbestellung.i_id= "
											+ flrBestellposition.getFlrbestellung().getI_id()
											+ " ORDER BY zp.t_termin ASC";

									Query queryBSZP = sessionBSZP.createQuery(queryStringBSZP);
									List resultListBSZP = queryBSZP.list();

									Iterator resultListIteratorBSZP = resultListBSZP.iterator();

									BigDecimal bdSummeZahlungsplan = BigDecimal.ZERO;

									while (resultListIteratorBSZP.hasNext()) {
										FLRBSZahlungsplan zp = (FLRBSZahlungsplan) resultListIteratorBSZP.next();

										bdSummeZahlungsplan = bdSummeZahlungsplan.add(zp.getN_betrag());

										if (zp.getT_erledigt() == null) {
											Object[] oZeileBSZP = new Object[REPORT_LV_ANZAHL_SPALTEN];
											oZeileBSZP[REPORT_LV_IMPORTIERT] = new Boolean(false);
											oZeileBSZP[REPORT_LV_BELEG] = "ZP BS" + zp.getFlrbestellung().getC_nr();
											oZeileBSZP[REPORT_LV_NETTO] = zp.getN_betrag().negate();

											oZeileBSZP[REPORT_LV_PARTNER] = zp.getFlrbestellung().getFlrlieferant()
													.getFlrpartner().getC_name1nachnamefirmazeile1();

											BigDecimal bdMwstZP = Helper.getMehrwertsteuerBetragFuerNetto(
													zp.getN_betrag(),
													new BigDecimal(mwstsatzDtoAktuell.getFMwstsatz().doubleValue()));

											oZeileBSZP[REPORT_LV_MWST] = bdMwstZP.negate();

											Date dFaellig = getMandantFac().berechneZielDatumFuerBelegdatum(
													zp.getT_termin(),
													zp.getFlrbestellung().getFlrzahlungsziel().getId(), theClientDto);

											// SP3952
											if (dFaellig.before(new Date())) {
												dFaellig = new Date();
											}

											Calendar cal = Calendar.getInstance(theClientDto.getLocUi());
											cal.setTime(dFaellig);

											oZeileBSZP[REPORT_LV_JAHR] = new Integer(cal.get(Calendar.YEAR));
											oZeileBSZP[REPORT_LV_WOCHE] = new Integer(cal.get(Calendar.WEEK_OF_YEAR));
											oZeileBSZP[REPORT_LV_JAHR] = Helper.berechneJahrDerKW(cal);

											add2VST_USTMap(hmUstBS, dFaellig, bdMwstZP, kalenderwochen, theClientDto);

											if (cal.getTime().before(c.getTime())) {
												alDaten.add(oZeileBSZP);
											}

										}

									}

									posWert = posWert.subtract(bdSummeZahlungsplan);

									sessionBSZP.close();

								}
							}

							if (bZahlungsplanvorhanden == false) {

								try {
									posWert = getLocaleFac().rechneUmInAndereWaehrungZuDatum(posWert,
											flrBestellposition.getFlrbestellung().getWaehrung_c_nr_bestellwaehrung(),
											theClientDto.getSMandantenwaehrung(),
											new java.sql.Date(System.currentTimeMillis()), theClientDto);

								} catch (RemoteException e) {
									throwEJBExceptionLPRespectOld(e);
								}

								zeileTemp[REPORT_LV_NETTO] = posWert.negate();
								BigDecimal bdMwst = new BigDecimal(0);
								if (mwstsatzDtoAktuell != null) {
									bdMwst = Helper.getMehrwertsteuerBetragFuerNetto(posWert,
											new BigDecimal(mwstsatzDtoAktuell.getFMwstsatz().doubleValue()));
								} else {

								}

								zeileTemp[REPORT_LV_MWST] = bdMwst.negate();

								add2VST_USTMap(hmUstBS, dZieldatumFuerVorsteuer, bdMwst, kalenderwochen, theClientDto);

								alDaten.add(zeileTemp);
							}

						} else {

							BigDecimal bdOffeneMenge = flrBestellposition.getN_menge();

							for (int i = 0; i < weposDtos.length; i++) {
								posWert = new BigDecimal(0);
								bdOffeneMenge = bdOffeneMenge.subtract(weposDtos[i].getNGeliefertemenge());

								// Wenn der Wareneingang dazu eine ER hat
								Object[] zeileTemp = oZeile.clone();

								if (bestellnummer.startsWith("17/0000131/8")) {
									int z = 0;
								}

								zeileTemp[REPORT_LV_BELEG] = "WE" + bestellnummer + "/" + (i + 1);

								WareneingangDto weDto = getWareneingangFac()
										.wareneingangFindByPrimaryKey(weposDtos[i].getWareneingangIId());

								if (weDto.getEingangsrechnungIId() != null) {
									continue;
								}

								Date dLiefertermin = flrBestellposition.getT_uebersteuerterliefertermin();

								if (flrBestellposition.getT_auftragsbestaetigungstermin() != null) {
									dLiefertermin = flrBestellposition.getT_auftragsbestaetigungstermin();
								}

								Date dZieldatumAnhandWE = weDto.getTWareneingangsdatum();
								// Wenn Ware zu frueh geliefert wird, dann
								// zaehlt das WE-Datum
								if (weDto.getTWareneingangsdatum().before(dLiefertermin)) {
									dZieldatumAnhandWE = dLiefertermin;
								}

								if (dZieldatumAnhandWE.before(new Date())) {
									dZieldatumAnhandWE = new Date();
								}

								// Zieldatum neu berechnen
								Date dZieldatumAnhandWEFuerMWST = new Date(dZieldatumAnhandWE.getTime());

								dZieldatumAnhandWE = getMandantFac().berechneZielDatumFuerBelegdatum(dZieldatumAnhandWE,
										flrBestellposition.getFlrbestellung().getFlrzahlungsziel().getId(),
										theClientDto);

								if (dZieldatumAnhandWE.after(c.getTime())) {
									continue;
								}

								Calendar cZieldatumAnhandWE = Calendar.getInstance();
								cZieldatumAnhandWE.setTimeInMillis(dZieldatumAnhandWE.getTime());

								zeileTemp[REPORT_LV_JAHR] = cZieldatumAnhandWE.get(Calendar.YEAR);
								zeileTemp[REPORT_LV_WOCHE] = cZieldatumAnhandWE.get(Calendar.WEEK_OF_YEAR);
								// wg SP1786
								zeileTemp[REPORT_LV_JAHR] = Helper.berechneJahrDerKW(cZieldatumAnhandWE);

								// SP3176
								if (weposDtos[i].getBPreiseErfasst() == true
										&& weposDtos[i].getNGelieferterpreis() != null) {

									posWert = posWert.add(weposDtos[i].getNGeliefertemenge()
											.multiply(weposDtos[i].getNGelieferterpreis()));

								} else {
									posWert = posWert.add(besPos.getNNettogesamtPreisminusRabatte()
											.multiply(weposDtos[i].getNGeliefertemenge()));
								}

								try {
									posWert = getLocaleFac().rechneUmInAndereWaehrungZuDatum(posWert,
											flrBestellposition.getFlrbestellung().getWaehrung_c_nr_bestellwaehrung(),
											theClientDto.getSMandantenwaehrung(),
											new java.sql.Date(System.currentTimeMillis()), theClientDto);

								} catch (RemoteException e) {
									throwEJBExceptionLPRespectOld(e);
								}

								zeileTemp[REPORT_LV_NETTO] = posWert.negate();

								BigDecimal bdMwst = BigDecimal.ZERO;
								if (mwstsatzDtoAktuell != null) {
									zeileTemp[REPORT_LV_MWST] = bdMwst = Helper.getMehrwertsteuerBetragFuerNetto(
											posWert, new BigDecimal(mwstsatzDtoAktuell.getFMwstsatz().doubleValue()));
								}

								zeileTemp[REPORT_LV_MWST] = bdMwst.negate();

								add2VST_USTMap(hmUstBS, dZieldatumAnhandWEFuerMWST, bdMwst, kalenderwochen,
										theClientDto);

								alDaten.add(zeileTemp);

							}
							if (bdOffeneMenge.doubleValue() > 0) {

								// Wenn nun noch was offen ist, dann eine
								// zusaetzliche Zeile
								Object[] zeileTemp = oZeile.clone();

								BigDecimal bdOffenerWert = bdOffeneMenge
										.multiply(besPos.getNNettogesamtPreisminusRabatte());

								try {
									bdOffenerWert = getLocaleFac().rechneUmInAndereWaehrungZuDatum(bdOffenerWert,
											flrBestellposition.getFlrbestellung().getWaehrung_c_nr_bestellwaehrung(),
											theClientDto.getSMandantenwaehrung(),
											new java.sql.Date(System.currentTimeMillis()), theClientDto);

								} catch (RemoteException e) {
									throwEJBExceptionLPRespectOld(e);
								}

								zeileTemp[REPORT_LV_NETTO] = bdOffenerWert.negate();

								BigDecimal bdMwst = BigDecimal.ZERO;
								if (mwstsatzDtoAktuell != null) {
									bdMwst = Helper.getMehrwertsteuerBetragFuerNetto(bdOffenerWert,
											new BigDecimal(mwstsatzDtoAktuell.getFMwstsatz().doubleValue()));
								}

								zeileTemp[REPORT_LV_MWST] = bdMwst.negate();

								zeileTemp[REPORT_LV_BELEG] = "BS" + bestellnummer;

								add2VST_USTMap(hmUstBS, dZieldatumFuerVorsteuer, bdMwst, kalenderwochen, theClientDto);

								alDaten.add(zeileTemp);

							}

						}

					}
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}
			session.close();

			if (bMitAbgaben) {
				alDaten = alAddUST_VST2Liquiditaetsvorschau(alDaten, hmUstBS, "VST BS", false, theClientDto);
			}

		}
		// Plankosten hinzufuegen
		if (bMitPlankosten == true && alPlankosten != null) {

			for (int i = 0; i < alPlankosten.size(); i++) {

				if (alPlankosten.get(i).getDatum().before(c.getTime())) {

					if (alPlankosten.get(i).getDatum().before(new Date())) {
						alPlankosten.get(i).setDatum(new Date());
					}

					Object[] oZeile = new Object[REPORT_LV_ANZAHL_SPALTEN];
					oZeile[REPORT_LV_IMPORTIERT] = new Boolean(true);
					oZeile[REPORT_LV_BELEG] = alPlankosten.get(i).getBeleg();
					oZeile[REPORT_LV_NETTO] = alPlankosten.get(i).getNetto();
					oZeile[REPORT_LV_MWST] = alPlankosten.get(i).getMwst();

					oZeile[REPORT_LV_PARTNER] = alPlankosten.get(i).getPartner();

					if (alPlankosten.get(i).getDatum() != null) {
						Calendar cal = Calendar.getInstance(theClientDto.getLocUi());
						cal.setTime(alPlankosten.get(i).getDatum());

						oZeile[REPORT_LV_JAHR] = new Integer(cal.get(Calendar.YEAR));
						oZeile[REPORT_LV_WOCHE] = new Integer(cal.get(Calendar.WEEK_OF_YEAR));
						oZeile[REPORT_LV_JAHR] = Helper.berechneJahrDerKW(cal);

						alDaten.add(oZeile);
					}
				}

			}

		}

		// Nicht vorhandene KWS auffuellen

		for (int i = 0; i < kalenderwochen; i++) {

			Calendar cKW = Calendar.getInstance();
			cKW.add(Calendar.WEEK_OF_YEAR, i);

			boolean bGefunden = false;
			for (int m = 0; m < alDaten.size(); m++) {
				Object[] oZeile = (Object[]) alDaten.get(m);

				if (((Integer) oZeile[REPORT_LV_WOCHE]).intValue() == cKW.get(Calendar.WEEK_OF_YEAR)
						&& ((Integer) oZeile[REPORT_LV_JAHR]).intValue() == cKW.get(Calendar.YEAR)) {
					bGefunden = true;
					break;
				}

			}
			if (bGefunden == false) {

				Object[] oZeile = new Object[REPORT_LV_ANZAHL_SPALTEN];
				oZeile[REPORT_LV_WOCHE] = cKW.get(Calendar.WEEK_OF_YEAR);
				oZeile[REPORT_LV_JAHR] = cKW.get(Calendar.YEAR);
				// wg SP1786
				oZeile[REPORT_LV_JAHR] = Helper.berechneJahrDerKW(cKW);

				oZeile[REPORT_LV_NETTO] = new BigDecimal(0);
				oZeile[REPORT_LV_MWST] = new BigDecimal(0);
				oZeile[REPORT_LV_BELEG] = "";
				oZeile[REPORT_LV_PARTNER] = "";
				alDaten.add(oZeile);
			}

		}

		// Sortieren
		for (int m = alDaten.size() - 1; m > 0; --m) {
			for (int n = 0; n < m; ++n) {
				Object[] o1 = (Object[]) alDaten.get(n);
				Object[] o2 = (Object[]) alDaten.get(n + 1);

				String woche1 = Helper.fitString2LengthAlignRight(o1[REPORT_LV_WOCHE] + "", 2, '0');
				String woche2 = Helper.fitString2LengthAlignRight(o2[REPORT_LV_WOCHE] + "", 2, '0');

				String ort1 = (Integer) o1[REPORT_LV_JAHR] + woche1 + (String) o1[REPORT_LV_BELEG];
				String ort2 = (Integer) o2[REPORT_LV_JAHR] + woche2 + (String) o2[REPORT_LV_BELEG];

				if (ort1.compareTo(ort2) > 0) {
					alDaten.set(n, o2);
					alDaten.set(n + 1, o1);
				}
			}
		}

		Object[][] returnArray = new Object[alDaten.size()][REPORT_LV_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(returnArray);

		parameter.put("P_VON", new java.sql.Timestamp(System.currentTimeMillis()));
		parameter.put("P_BIS", new java.sql.Timestamp(c.getTimeInMillis()));
		parameter.put("P_KREDITLIMIT", kreditlimit);

		parameter.put("P_ZIELTERMIN_NACH_ZAHLUNGSMORAL", new Boolean(bTerminNachZahlungsmoral));

		parameter.put("P_ERZIELTERMIN_NACH_FREIGABEDATUM", new Boolean(bERTerminNachFreigabedatum));

		parameter.put("P_MIT_OFFENEN_ANGEBOTEN", new Boolean(bMitOffenenAngeboten));
		parameter.put("P_MIT_OFFENEN_AUFTRAEGEN", new Boolean(bMitOffenenAuftraegen));

		parameter.put("P_MIT_AB_ZAHL_ZEITPLAN", new Boolean(bMitABZahlungsUndZeitplan));

		parameter.put("P_MIT_OFFENEN_BESTELLUNGEN", new Boolean(bMitOffenenBestellungen));

		parameter.put("P_MIT_ABGABEN", new Boolean(bMitAbgaben));

		parameter.put("P_MIT_PLANKOSTEN", new Boolean(bMitPlankosten));

		parameter.put("P_OHNE_VERBUNDENE_UNTERNEHMEN", bOhneAndereMandanten);

		ParametermandantDto p;
		try {
			p = getParameterFac().getMandantparameter(theClientDto.getMandant(), ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_IMPORT_PLANKOSTEN_DATEI);
			parameter.put("P_PLANKOSTEN_DATEI", p.getCWert());

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		parameter.put("P_KONTOSTAND", kontostand);
		parameter.put("P_BETRACHTUNGSZEITRAUM", kalenderwochen);
		parameter.put("P_WAEHRUNG", theClientDto.getSMandantenwaehrung());

		initJRDS(parameter, FinanzReportFac.REPORT_MODUL, FinanzReportFac.REPORT_LIQUIDITAETSVORSCHAU,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();
	}

	private HashMap<Timestamp, BigDecimal> add2VST_USTMap(HashMap<Timestamp, BigDecimal> hmUst, Date tDatum,
			BigDecimal mwst, Integer kalenderwochen, TheClientDto theClientDto) {

		int iUstStichtag = 15;
		int iUstMonat = 2;

		ParametermandantDto p;
		try {
			p = getParameterFac().getMandantparameter(theClientDto.getMandant(), ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_UST_STICHTAG);

			iUstStichtag = (Integer) p.getCWertAsObject();
			p = getParameterFac().getMandantparameter(theClientDto.getMandant(), ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_UST_MONAT);

			iUstMonat = (Integer) p.getCWertAsObject();

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		Calendar c = Calendar.getInstance();

		c.add(Calendar.WEEK_OF_YEAR, kalenderwochen);
		java.sql.Timestamp tEndeLiqiditaetsvorschau = new java.sql.Timestamp(c.getTimeInMillis());
		c = Calendar.getInstance();

		// Wenn Datum vor heute
		if (tDatum.before(new Date())) {
			tDatum = new Date();
		}

		// UST-Stichtag
		c.setTimeInMillis(tDatum.getTime());
		c.set(Calendar.DATE, 1);
		c.add(Calendar.MONTH, iUstMonat);
		c.set(Calendar.DATE, iUstStichtag);

		java.sql.Timestamp tUstStichtag = Helper.cutTimestamp(new java.sql.Timestamp(c.getTimeInMillis()));

		if (tUstStichtag.before(tEndeLiqiditaetsvorschau)) {

			BigDecimal bdSteuer = BigDecimal.ZERO;
			if (hmUst.containsKey(tUstStichtag)) {
				bdSteuer = hmUst.get(tUstStichtag);
			}

			bdSteuer = bdSteuer.add(mwst);

			hmUst.put(tUstStichtag, bdSteuer);

		}

		return hmUst;
	}

	private ArrayList alAddUST_VST2Liquiditaetsvorschau(ArrayList alDaten, HashMap<Timestamp, BigDecimal> hmUst,
			String belegart, boolean bMwst, TheClientDto theClientDto) {

		DateFormatSymbols symbols = new DateFormatSymbols(theClientDto.getLocUi());
		String[] defaultMonths = symbols.getMonths();

		int iUstMonat = 2;

		ParametermandantDto p;
		try {

			p = getParameterFac().getMandantparameter(theClientDto.getMandant(), ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_UST_MONAT);

			iUstMonat = (Integer) p.getCWertAsObject();

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		Iterator it = hmUst.keySet().iterator();
		while (it.hasNext()) {
			Timestamp tKey = (Timestamp) it.next();
			BigDecimal mwst = hmUst.get(tKey);
			Calendar cKey = Calendar.getInstance();

			cKey.setTime(tKey);

			Object[] oZeile = new Object[REPORT_LV_ANZAHL_SPALTEN];

			oZeile[REPORT_LV_IMPORTIERT] = new Boolean(false);

			oZeile[REPORT_LV_JAHR] = Helper.berechneJahrDerKW(cKey);
			oZeile[REPORT_LV_WOCHE] = cKey.get(Calendar.WEEK_OF_YEAR);

			cKey.add(Calendar.MONTH, -iUstMonat);
			oZeile[REPORT_LV_PARTNER] = defaultMonths[cKey.get(Calendar.MONTH)];

			oZeile[REPORT_LV_BELEG] = belegart;

			if (bMwst) {
				oZeile[REPORT_LV_NETTO] = mwst.negate();
			} else {
				oZeile[REPORT_LV_NETTO] = mwst;
			}

			oZeile[REPORT_LV_MWST] = BigDecimal.ZERO;
			alDaten.add(oZeile);
		}

		return alDaten;
	}

	public JasperPrintLP printUeberweisungsliste(Integer zahlungsvorschlaglaufIId, TheClientDto theClientDto) {
		this.useCase = UC_UEBERWEISUNGSLISTE;
		this.index = -1;

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = " from FLRZahlungsvorschlag flrzv  WHERE flrzv.zahlungsvorschlaglauf_i_id = "
				+ zahlungsvorschlaglaufIId + " ORDER BY flrzv.t_faellig ASC";

		Query query = session.createQuery(sQuery);

		List results = query.list();
		Iterator resultListIterator = results.iterator();

		ArrayList alDaten = new ArrayList();

		while (resultListIterator.hasNext()) {

			FLRZahlungsvorschlag zv = (FLRZahlungsvorschlag) resultListIterator.next();
			FLREingangsrechnungReport er = zv.getFlreingangsrechnungreport();

			Object[] oZeile = new Object[REPORT_UEBERWEISUNGSLISTE_ANZAHL_SPALTEN];
			oZeile[REPORT_UEBERWEISUNGSLISTE_ART] = er.getEingangsrechnungart_c_nr().substring(0, 1);
			oZeile[REPORT_UEBERWEISUNGSLISTE_EINGANGSRECHNUNG] = er.getC_nr();
			oZeile[REPORT_UEBERWEISUNGSLISTE_MAHNSTUFE] = er.getMahnstufe_i_id();
			oZeile[REPORT_UEBERWEISUNGSLISTE_WERT] = er.getN_betragfw();
			oZeile[REPORT_UEBERWEISUNGSLISTE_PARTNER] = er.getFlrlieferant().getFlrpartner()
					.getC_name1nachnamefirmazeile1();
			oZeile[REPORT_UEBERWEISUNGSLISTE_FAELLIG] = zv.getT_faellig();
			if (zv.getN_angewandterskontosatz().doubleValue() > 0) {

				oZeile[REPORT_UEBERWEISUNGSLISTE_SKONTO] = Helper.formatZahl(
						zv.getN_angewandterskontosatz().setScale(FinanzFac.NACHKOMMASTELLEN, RoundingMode.HALF_UP),
						FinanzFac.NACHKOMMASTELLEN, theClientDto.getLocUi()) + " %";
			}
			oZeile[REPORT_UEBERWEISUNGSLISTE_ZAHLBETRAG] = zv.getN_zahlbetrag();
			oZeile[REPORT_UEBERWEISUNGSLISTE_WAEHRUNG] = er.getWaehrung_c_nr();
			oZeile[REPORT_UEBERWEISUNGSLISTE_TEXT] = er.getC_text();
			oZeile[REPORT_UEBERWEISUNGSLISTE_FREIGABE_ZUR_UEBERWEISUNG] = new Boolean(
					Helper.short2Boolean(zv.getB_bezahlen()));
			oZeile[REPORT_UEBERWEISUNGSLISTE_BANK] = er.getC_nr();
			oZeile[REPORT_UEBERWEISUNGSLISTE_IBAN] = er.getC_nr();
			oZeile[REPORT_UEBERWEISUNGSLISTE_BIC] = er.getC_nr();

			try {
				// hat der LF eine Bankverbindung?
				ErZahlungsempfaenger empfaenger = getEingangsrechnungFac()
						.getErZahlungsempfaenger(new EingangsrechnungId(er.getI_id()), theClientDto);
				if (empfaenger.exists()) {
					oZeile[REPORT_UEBERWEISUNGSLISTE_BANK] = empfaenger.getBankDto().getPartnerDto()
							.getCName1nachnamefirmazeile1();
					oZeile[REPORT_UEBERWEISUNGSLISTE_IBAN] = empfaenger.getPartnerbankDto().getCIban();
					oZeile[REPORT_UEBERWEISUNGSLISTE_BIC] = empfaenger.getBankDto().getCBic();
				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			if (er.getFlrpersonal_geprueft() != null) {
				oZeile[REPORT_UEBERWEISUNGSLISTE_GEPRUEFT_AM] = er.getT_geprueft();
				oZeile[REPORT_UEBERWEISUNGSLISTE_GEPRUEFT_VON] = er.getFlrpersonal_geprueft().getC_kurzzeichen();
			}

			alDaten.add(oZeile);

		}

		data = new Object[alDaten.size()][REPORT_UEBERWEISUNGSLISTE_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(data);
		try {

			ZahlungsvorschlaglaufDto zahlungsvorschlaglaufDto = getZahlungsvorschlagFac()
					.zahlungsvorschlaglaufFindByPrimaryKey(zahlungsvorschlaglaufIId);

			PersonalDto personalDto = getPersonalFac()
					.personalFindByPrimaryKey(zahlungsvorschlaglaufDto.getPersonalIIdAnlegen(), theClientDto);

			parameter.put("P_ANLAGEZEITPUNKT", zahlungsvorschlaglaufDto.getTAnlegen());
			parameter.put("P_PERSONAL_ANGELEGT", personalDto.formatFixUFTitelName2Name1());

			if (zahlungsvorschlaglaufDto.getPersonalIIdGespeichert() != null) {
				PersonalDto personalDtoGespeichert = getPersonalFac()
						.personalFindByPrimaryKey(zahlungsvorschlaglaufDto.getPersonalIIdGespeichert(), theClientDto);
				parameter.put("P_PERSONAL_EXPORTIERT", personalDtoGespeichert.formatFixUFTitelName2Name1());
			}
			parameter.put("P_EXPORTZEITPUNKT", zahlungsvorschlaglaufDto.getTGespeichert());

			ParametermandantDto parameterERPRuefen = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_EINGANGSRECHNUNG, ParameterFac.PARAMETER_EINGANGSRECHNUNG_PRUEFEN);

			Integer iERPruefen = (Integer) parameterERPRuefen.getCWertAsObject();

			parameter.put("P_EINGANGSRECHNUNG_PRUEFEN", iERPruefen);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		initJRDS(parameter, FinanzReportFac.REPORT_MODUL, FinanzReportFac.REPORT_UEBERWEISUNGSLISTE,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		JasperPrintLP print = getReportPrint();
		// }
		return print;

	}

	// ACHTUNG: verwendet zur Zeit gleiche Spaltenanzahl und Folge wie
	// Kontoblatt
	public JasperPrintLP printOffenePosten(String kontotypCNr, Integer geschaeftsjahr, Integer kontoIId,
			java.sql.Timestamp tStichtag, TheClientDto theClientDto, boolean sortAlphabethisch) {
		this.useCase = UC_OFFENEPOSTEN;
		this.index = -1;

		JasperPrintLP print = null;
		HashMap<String, Object> parameter = new HashMap<String, Object>();

		List<?> resultsOhneOp = getBuchungDetailMitOP(kontotypCNr, geschaeftsjahr, kontoIId, tStichtag, theClientDto,
				sortAlphabethisch, parameter, false);

		Iterator<?> resultListIterator = resultsOhneOp.iterator();
		// FLRFinanzBuchungDetail buchungDetail = (FLRFinanzBuchungDetail)
		// resultsOhneOp;
		// FLRFinanzBuchung buchung = (FLRFinanzBuchung) resultsOhneOp;

		List<String> azkList = new ArrayList<String>();
		Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
		BigDecimal azkSaldo = BigDecimal.ZERO.setScale(4);
		while (resultListIterator.hasNext()) {
			Object[] row = (Object[]) resultListIterator.next();
			FLRFinanzBuchungDetail buchungDetail = (FLRFinanzBuchungDetail) row[0];
			FLRFinanzBuchung buchung = (FLRFinanzBuchung) row[1];
			if (buchungDetail.getI_ausziffern() != null) {
				Integer azk = buchungDetail.getI_ausziffern();
				String s = buchungDetail.getKonto_i_id() + "." + azk;
				if (!azkList.contains(s)) {
					azkSaldo = getBuchenFac().getSaldoVonKontoByAusziffern(buchungDetail.getKonto_i_id(),
							buchung.getGeschaeftsjahr_i_geschaeftsjahr(), azk, theClientDto);
					if (azkSaldo.signum() != 0) {
						map.put(s, azkSaldo);
					}
					azkList.add(s);
				}
			}
		}

		List<?> resultsMitOp = getBuchungDetailMitOP(kontotypCNr, geschaeftsjahr, kontoIId, tStichtag, theClientDto,
				sortAlphabethisch, parameter, true);

		// Iterator<?> resultListIterator = resultsMitOp.iterator();
		resultListIterator = resultsMitOp.iterator();

		int i = 0;
		ArrayList<Object[]> al = new ArrayList<Object[]>();
		Object[] tempdata = new Object[REPORT_OFFENEPOSTEN_ANZAHL_SPALTEN];
		while (resultListIterator.hasNext()) {
			Object[] row = (Object[]) resultListIterator.next();
			FLRFinanzBuchungDetail buchungDetail = (FLRFinanzBuchungDetail) row[0];

			if (buchungDetail.getFlrbuchung() != null) {
				if (buchungDetail.getFlrbuchung().getFlrfbbelegart() != null) {
					String art = buchungDetail.getFlrbuchung().getFlrbuchungsart().getC_kbez();
					if (art == null)
						art = "__";
					String belegart = "";
					if (buchungDetail.getFlrbuchung().getFlrfbbelegart() != null)
						belegart = buchungDetail.getFlrbuchung().getFlrfbbelegart().getC_kbez();
					tempdata[REPORT_OP_BUCHUNGSART] = art + belegart;
				}
			}

			tempdata[REPORT_OP_BELEG] = buchungDetail.getFlrbuchung().getC_belegnummer();
			tempdata[REPORT_OP_BELEGARTCNR] = buchungDetail.getFlrbuchung().getBuchungsart_c_nr();
			// tempdata[REPORT_OP_TEXT] =
			// buchungDetail.getFlrbuchung().getC_text();
			tempdata[REPORT_OP_TEXT] = buchungDetail.getFlrkonto().getC_bez();
			tempdata[REPORT_OP_KONTOCNR] = buchungDetail.getFlrkonto().getC_nr();

			if (buchungDetail.getBuchungdetailart_c_nr().equals(BuchenFac.HabenBuchung)) {
				tempdata[REPORT_OP_HABEN] = buchungDetail.getN_betrag();
			} else {
				tempdata[REPORT_OP_SOLL] = buchungDetail.getN_betrag();
			}

			tempdata[REPORT_OP_BUCHUNGSDATUM] = buchungDetail.getFlrbuchung().getD_buchungsdatum();
			tempdata[REPORT_OP_AUSZUG] = buchungDetail.getI_auszug();

			tempdata[REPORT_OP_USTWERT] = buchungDetail.getN_ust();
			if (buchungDetail.getN_ust().compareTo(new BigDecimal(0.00)) != 0) {
				BigDecimal ustprozent = buchungDetail.getN_betrag().subtract(buchungDetail.getN_ust());
				ustprozent = buchungDetail.getN_ust().divide(ustprozent, 5);
				ustprozent = ustprozent.movePointLeft(2);
				tempdata[REPORT_OP_USTPROZENT] = ustprozent;
			} else {
				tempdata[REPORT_OP_USTPROZENT] = null;
			}

			if (buchungDetail.getFlrgegenkonto() != null) {
				tempdata[REPORT_OP_GEGENKONTO] = buchungDetail.getFlrgegenkonto().getC_nr();
			}
			tempdata[REPORT_OP_AZK] = buchungDetail.getI_ausziffern();

			String key = buchungDetail.getKonto_i_id() + "." + buchungDetail.getI_ausziffern();
			if (map.get(key) != null) {
				tempdata[REPORT_OP_AZK_SUMME_FEHLERHAFT] = map.get(key);
			}

			al.add(tempdata.clone());
			tempdata = new Object[REPORT_OFFENEPOSTEN_ANZAHL_SPALTEN];
		}

		data = al.toArray(new Object[0][]);

		// if (print != null) {
		// initJRDS(parameter, FinanzReportFac.REPORT_MODUL,
		// FinanzReportFac.REPORT_OFFENEPOSTEN,
		// theClientDto.getMandant(), theClientDto.getLocUi(),
		// theClientDto);
		// print = Helper.addReport2Report(print, getReportPrint().getPrint());
		// } else {
		initJRDS(parameter, FinanzReportFac.REPORT_MODUL, FinanzReportFac.REPORT_OFFENEPOSTEN,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		print = getReportPrint();
		// }
		return print;
	}

	private List<?> getBuchungDetailMitOP(String kontotypCNr, Integer geschaeftsjahr, Integer kontoIId,
			java.sql.Timestamp tStichtag, TheClientDto theClientDto, boolean sortAlphabethisch,
			HashMap<String, Object> parameter, Boolean op) {
		parameter.put(LPReport.P_WAEHRUNG, theClientDto.getSMandantenwaehrung());
		parameter.put("P_KONTOTYP", kontotypCNr);
		parameter.put("P_TSTICHTAG", tStichtag);
		parameter.put("P_SORTIERUNG", "Beleg");

		Session session = FLRSessionFactory.getFactory().openSession();
		String q = "from FLRFinanzBuchungDetail buchungdetail LEFT OUTER JOIN buchungdetail.flrbuchung as b LEFT OUTER JOIN buchungdetail.flrkonto as k ";
		q += "where b.d_buchungsdatum<'" + Helper.formatDateWithSlashes(Helper.addiereTageZuDatum(tStichtag, 1)) + "' ";
		q += "and b.geschaeftsjahr_i_geschaeftsjahr=" + geschaeftsjahr + " ";
		q += "and b.t_storniert is null ";
		if (kontoIId != null) {
			q += "and k.i_id=" + kontoIId + " ";
		} else {
			q += "and k.mandant_c_nr='" + theClientDto.getMandant() + "' ";
			q += "and k.kontotyp_c_nr='" + kontotypCNr + "' ";
		}

		if (op) {
			q += "and " + BuchungDetailQueryBuilder.buildNurOffeneBuchungDetails(
					"buchungdetail", tStichtag);
		}

		q += "order by ";
		if (sortAlphabethisch) {
			q += "k.c_bez,";
		}
		q += "k.c_nr, buchungdetail.i_ausziffern, b.c_belegnummer, b.d_buchungsdatum";

		List<?> results = session.createQuery(q).list();
		return results;
	}

	public JasperPrintLP printErfolgsrechnung(String mandantCNr, ReportErfolgsrechnungKriterienDto krit,
			boolean bBilanz, boolean bDetails, boolean bEroeffnungsbilanz, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		try {
			this.useCase = UC_ERFOLGSRECHNUNG;
			this.index = -1;

			ErgebnisgruppeDto[] ergebnisgruppenDtos = getFinanzFac().ergebnisgruppeFindAll(theClientDto, bBilanz);

			Map<Integer, ErgebnisgruppeSaldo> map = new TreeMap<Integer, ErgebnisgruppeSaldo>();

			for (int i = 0; i < ergebnisgruppenDtos.length; i++) {
				ErgebnisgruppeSaldo es = new ErgebnisgruppeSaldo(ergebnisgruppenDtos[i]);
				map.put(ergebnisgruppenDtos[i].getIId(), es);
			}

			if (bBilanz)
				pruefeErgebnisgruppen(map, theClientDto);

			for (int i = 0; i < ergebnisgruppenDtos.length; i++) {
				ErgebnisgruppeSaldo es = map.get(ergebnisgruppenDtos[i].getIId());
				if (es.ergebnisgruppeDto.getITyp() == FinanzFac.ERGEBNISGRUPPE_TYP_ERGEBNISGRUPPE
						|| es.ergebnisgruppeDto.getITyp() == FinanzFac.ERGEBNISGRUPPE_TYP_BILANZGRUPPE_POSITIV
						|| es.ergebnisgruppeDto.getITyp() == FinanzFac.ERGEBNISGRUPPE_TYP_BILANZGRUPPE_NEGATIV) {
					es.addsaldo(map, ErgebnisgruppeSaldo.SALDO_PERIODE, getSammelSaldo(krit, es,
							ErgebnisgruppeSaldo.SALDO_PERIODE, bEroeffnungsbilanz, theClientDto));
					es.addsaldo(map, ErgebnisgruppeSaldo.SALDO_KUMMULIERT, getSammelSaldo(krit, es,
							ErgebnisgruppeSaldo.SALDO_KUMMULIERT, bEroeffnungsbilanz, theClientDto));
					es.addsaldo(map, ErgebnisgruppeSaldo.SALDO_PERIODE_VORJAHR,
							getSammelSaldo(krit, es, ErgebnisgruppeSaldo.SALDO_PERIODE_VORJAHR, false, theClientDto));
					es.addsaldo(map, ErgebnisgruppeSaldo.SALDO_KUMMULIERT_VORJAHR, getSammelSaldo(krit, es,
							ErgebnisgruppeSaldo.SALDO_KUMMULIERT_VORJAHR, false, theClientDto));
					if (bDetails) {
						ErgebnisgruppeDetailSaldo[] detailsaldos = getDetailSaldos(es, krit, bEroeffnungsbilanz,
								theClientDto);
						es.adddetailsaldo(detailsaldos);
					}
				}
			}

			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			BigDecimal jahresgewinnPeriode = null;
			boolean b100Added = false;
			if (bDetails) {
				int size = map.size();
				for (int i = 0; i < ergebnisgruppenDtos.length; i++) {
					if (map.get(ergebnisgruppenDtos[i].getIId()).detailsaldos != null)
						size += map.get(ergebnisgruppenDtos[i].getIId()).detailsaldos.length;
				}
				data = new Object[size][REPORT_ERFOLGSRECHNUNG_ANZAHL];
			} else
				data = new Object[map.size()][REPORT_ERFOLGSRECHNUNG_ANZAHL];
			int i = 0;
			for (int j = 0; j < ergebnisgruppenDtos.length; j++) {
				ErgebnisgruppeSaldo es = map.get(ergebnisgruppenDtos[j].getIId());
				data[i][REPORT_ERFOLGSRECHNUNG_BEZEICHNUNG] = es.ergebnisgruppeDto.getCBez();
				data[i][REPORT_ERFOLGSRECHNUNG_SALDO_PERIODE] = es.saldo[ErgebnisgruppeSaldo.SALDO_PERIODE];
				data[i][REPORT_ERFOLGSRECHNUNG_SALDO_KUMMULIERT] = es.saldo[ErgebnisgruppeSaldo.SALDO_KUMMULIERT];
				data[i][REPORT_ERFOLGSRECHNUNG_SALDO_PERIODE_VORJAHR] = es.saldo[ErgebnisgruppeSaldo.SALDO_PERIODE_VORJAHR];
				data[i][REPORT_ERFOLGSRECHNUNG_SALDO_KUMMULIERT_VORJAHR] = es.saldo[ErgebnisgruppeSaldo.SALDO_KUMMULIERT_VORJAHR];
				data[i][REPORT_ERFOLGSRECHNUNG_TYP] = es.ergebnisgruppeDto.getITyp();
				data[i][REPORT_ERFOLGSRECHNUNG_BEZUGSBASIS] = Helper
						.short2Boolean(es.ergebnisgruppeDto.getBProzentbasis());
				if (!b100Added && (Boolean) data[i][REPORT_ERFOLGSRECHNUNG_BEZUGSBASIS]) {
					mapParameter.put("P_100PROZ_PERIODE", data[i][REPORT_ERFOLGSRECHNUNG_SALDO_PERIODE]);
					mapParameter.put("P_100PROZ_KUMMULIERT", data[i][REPORT_ERFOLGSRECHNUNG_SALDO_KUMMULIERT]);
					mapParameter.put("P_100PROZ_PERIODE_VORJAHR",
							data[i][REPORT_ERFOLGSRECHNUNG_SALDO_PERIODE_VORJAHR]);
					mapParameter.put("P_100PROZ_VORJAHR_KUMMULIERT",
							data[i][REPORT_ERFOLGSRECHNUNG_SALDO_KUMMULIERT_VORJAHR]);
					b100Added = true; // nur der 1. wird hinzugefuegt, falls
					// falsch definiert
				}

				if (Helper.short2boolean(ergebnisgruppenDtos[j].getBJahresgewinn())) {
					jahresgewinnPeriode = es.saldo[ErgebnisgruppeSaldo.SALDO_PERIODE];
					mapParameter.put("P_JAHRESGEWINN_PERIODE", jahresgewinnPeriode);
					data[i][REPORT_ERFOLGSRECHNUNG_JAHRESGEWINN] = true;
				} else {
					data[i][REPORT_ERFOLGSRECHNUNG_JAHRESGEWINN] = false;
				}

				i++;
				if (bDetails && es.detailsaldos != null) {
					for (int k = 0; k < es.detailsaldos.length; k++) {
						ErgebnisgruppeDetailSaldo ds = es.detailsaldos[k];
						data[i][REPORT_ERFOLGSRECHNUNG_TYP] = es.ergebnisgruppeDto.getITyp();
						data[i][REPORT_ERFOLGSRECHNUNG_KONTO_CNR] = ds.konto.getCNr();
						data[i][REPORT_ERFOLGSRECHNUNG_KONTO_BEZEICHNUNG] = ds.konto.getCBez();
						data[i][REPORT_ERFOLGSRECHNUNG_SALDO_PERIODE] = ds.saldo[ErgebnisgruppeSaldo.SALDO_PERIODE];
						data[i][REPORT_ERFOLGSRECHNUNG_SALDO_KUMMULIERT] = ds.saldo[ErgebnisgruppeSaldo.SALDO_KUMMULIERT];
						data[i][REPORT_ERFOLGSRECHNUNG_SALDO_PERIODE_VORJAHR] = ds.saldo[ErgebnisgruppeSaldo.SALDO_PERIODE_VORJAHR];
						data[i][REPORT_ERFOLGSRECHNUNG_SALDO_KUMMULIERT_VORJAHR] = ds.saldo[ErgebnisgruppeSaldo.SALDO_KUMMULIERT_VORJAHR];
						i++;
					}
				}
			}

			// Mandantenwaehrung
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
			mapParameter.put(LPReport.P_WAEHRUNG, mandantDto.getWaehrungCNr());

			mapParameter.put("P_PERIODE", new Integer(krit.getIPeriode()));

			Calendar cBilanzstichtag = Calendar.getInstance();

			cBilanzstichtag.set(Calendar.YEAR, krit.getIGeschaeftsjahr());

			cBilanzstichtag.set(Calendar.MONTH, krit.getIPeriode() - 1);
			cBilanzstichtag.set(Calendar.DAY_OF_MONTH, cBilanzstichtag.getActualMaximum(Calendar.DAY_OF_MONTH));
			cBilanzstichtag.set(Calendar.HOUR_OF_DAY, 0);
			cBilanzstichtag.set(Calendar.MINUTE, 0);
			cBilanzstichtag.set(Calendar.SECOND, 0);

			mapParameter.put("P_BILANZSTICHTAG", cBilanzstichtag.getTime());

			if (bBilanz == true) {
				mapParameter.put("P_TYP", "Bilanz");
				mapParameter.put("P_DETAIL", new Boolean(false));
				mapParameter.put("P_EB", new Boolean(bEroeffnungsbilanz));
			} else {
				mapParameter.put("P_TYP", "Erfolgsrechnung");
				mapParameter.put("P_DETAIL", new Boolean(bDetails));
				mapParameter.put("P_EB", new Boolean(false));
			}

			mapParameter.put("P_GESCHAEFTSJAHR", krit.getIGeschaeftsjahr());
			Date[] dVonBis = getBuchenFac().getDatumbereichPeriodeGJ(krit.getIGeschaeftsjahr(), krit.getIPeriode(),
					theClientDto);

			mapParameter.put("P_PERIODE_KALENDERDATUM", dVonBis[0]);

			// PJ18424
			Map<Integer, KontoDto> mapKonten = getAlleSachKontenOhneZuordnung(theClientDto.getMandant());
			entferneOhneSaldo(mapKonten, krit.getIGeschaeftsjahr(), theClientDto);
			if (mapKonten.size() == 0) {
				mapParameter.put("P_NICHT_ZUGEORDNET", "");
				mapParameter.put("P_NICHT_ZUGEORDNETDETAIL", "");
			} else {
				StringBuilder s1 = new StringBuilder();
				StringBuilder s2 = new StringBuilder();
				Collection<KontoDto> kontos = mapKonten.values();
				Iterator<KontoDto> it = kontos.iterator();
				while (it.hasNext()) {
					KontoDto ktDto = it.next();
					s1.append(ktDto.getCNr());
					s2.append(ktDto.getCNr());
					s2.append(" ");
					s2.append(ktDto.getCBez());
					if (it.hasNext()) {
						s1.append(", ");
						s2.append(", ");
					}
				}
				mapParameter.put("P_NICHT_ZUGEORDNET", s1.toString());
				mapParameter.put("P_NICHT_ZUGEORDNETDETAIL", s2.toString());
			}

			FinanzamtDto famt = null;
			if (mandantDto.getPartnerIIdFinanzamt() != null) {
				famt = getFinanzFac().finanzamtFindByPrimaryKey(mandantDto.getPartnerIIdFinanzamt(), mandantCNr,
						theClientDto);
				mapParameter.put("P_KONTO_I_ID_GEWINNVORTRAG", famt.getKontoIIdGewinnvortrag());
				if (famt.getKontoIIdGewinnvortrag() != null) {
					KontoDtoSmall kDto = getFinanzFac()
							.kontoFindByPrimaryKeySmallOhneExc(famt.getKontoIIdGewinnvortrag());
					if (kDto != null) {
						mapParameter.put("P_KONTO_C_NR_GEWINNVORTRAG", kDto.getCNr());
						mapParameter.put("P_KONTO_C_BEZ_GEWINNVORTRAG", kDto.getCBez());
					}
				}
				mapParameter.put("P_KONTO_I_ID_JAHRESGEWINN", famt.getKontoIIdJahresgewinn());
				if (famt.getKontoIIdJahresgewinn() != null) {
					KontoDtoSmall kDto = getFinanzFac()
							.kontoFindByPrimaryKeySmallOhneExc(famt.getKontoIIdJahresgewinn());
					if (kDto != null) {
						mapParameter.put("P_KONTO_C_NR_JAHRESGEWINN", kDto.getCNr());
						mapParameter.put("P_KONTO_C_BEZ_JAHRESGEWINN", kDto.getCBez());
					}
				}
			}

			String report = FinanzReportFac.REPORT_ERFOLGSRECHNUNG;
			if (bBilanz == true) {
				report = FinanzReportFac.REPORT_BILANZ;
			}

			initJRDS(mapParameter, FinanzReportFac.REPORT_MODUL, report, theClientDto.getMandant(),
					theClientDto.getLocUi(), theClientDto);

			JasperPrintLP print = getReportPrint();
			PrintInfoDto values = new PrintInfoDto();
			if (bBilanz == true) {
				values.setDocPath(new DocPath(new DocNodeBilanz(krit, theClientDto.getMandant())));
			} else {
				values.setDocPath(new DocPath(new DocNodeErfolgsrechnung(krit, theClientDto.getMandant())));
			}
			values.setiId(theClientDto.getIDPersonal());

			values.setTable("");

			print.setOInfoForArchive(values);

			return print;

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	private void entferneOhneSaldo(Map<Integer, KontoDto> mapKonten, int iGeschaeftsjahr, TheClientDto theClientDto) {
		Set<Integer> s = mapKonten.keySet();
		Iterator<Integer> it = s.iterator();
		ArrayList<Integer> al = new ArrayList<Integer>();
		while (it.hasNext()) {
			Integer id = it.next();
			BigDecimal saldo = getBuchenFac().getSaldoVonKontoMitEB(id, iGeschaeftsjahr, -1, theClientDto);
			if (saldo.signum() == 0)
				al.add(id);
		}
		if (al.size() > 0) {
			for (int i = 0; i < al.size(); i++)
				mapKonten.remove(al.get(i));
		}
	}

	private Map<Integer, KontoDto> getAlleSachKontenOhneZuordnung(String mandantCNr) {
		Map<Integer, KontoDto> map = new TreeMap<Integer, KontoDto>();
		KontoDto[] kontoDtos = getFinanzFac().kontoFindAllByKontotypMandant(FinanzServiceFac.KONTOTYP_SACHKONTO,
				mandantCNr);
		for (int i = 0; i < kontoDtos.length; i++)
			if (kontoDtos[i].getErgebnisgruppeIId() == null && kontoDtos[i].getErgebnisgruppeIId_negativ() == null)
				map.put(kontoDtos[i].getIId(), kontoDtos[i]);
		return map;
	}

	private void pruefeErgebnisgruppen(Map<Integer, ErgebnisgruppeSaldo> map, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Iterator<Integer> it = map.keySet().iterator();
		while (it.hasNext()) {
			ErgebnisgruppeSaldo es = map.get(it.next());
			if (es.ergebnisgruppeDto.getITyp() == FinanzFac.ERGEBNISGRUPPE_TYP_BILANZGRUPPE_POSITIV
					|| es.ergebnisgruppeDto.getITyp() == FinanzFac.ERGEBNISGRUPPE_TYP_BILANZGRUPPE_NEGATIV) {
				List<Konto> konten = getErgebnisgruppeKonten(es, theClientDto);
				for (int i = 0; i < konten.size(); i++) {
					Konto konto = konten.get(i);
					if (konto.getErgebnisgruppeIId() == null || konto.getErgebnisgruppeIId_negativ() == null) {
						throw EJBExcFactory.bilanzgruppendefinitionPositivNegativFalsch(konto);
					}
				}
			}
		}
	}

	private ErgebnisgruppeDetailSaldo[] getDetailSaldos(ErgebnisgruppeSaldo es, ReportErfolgsrechnungKriterienDto krit,
			boolean bNurEB, TheClientDto theClientDto) {
		List<Konto> konten = getErgebnisgruppeKonten(es, theClientDto);
		if (konten.size() == 0)
			return null;
		else {
			ErgebnisgruppeDetailSaldo[] detailsaldos = new ErgebnisgruppeDetailSaldo[konten.size()];
			for (int i = 0; i < konten.size(); i++) {
				ErgebnisgruppeDetailSaldo detailsaldo = new ErgebnisgruppeDetailSaldo(konten.get(i),
						es.ergebnisgruppeDto.getBInvertiert());
				detailsaldo.addsaldo(ErgebnisgruppeSaldo.SALDO_PERIODE, getKontoSaldo(krit, konten.get(i), es,
						ErgebnisgruppeSaldo.SALDO_PERIODE, bNurEB, theClientDto));
				detailsaldo.addsaldo(ErgebnisgruppeSaldo.SALDO_KUMMULIERT, getKontoSaldo(krit, konten.get(i), es,
						ErgebnisgruppeSaldo.SALDO_KUMMULIERT, bNurEB, theClientDto));
				detailsaldo.addsaldo(ErgebnisgruppeSaldo.SALDO_PERIODE_VORJAHR, getKontoSaldo(krit, konten.get(i), es,
						ErgebnisgruppeSaldo.SALDO_PERIODE_VORJAHR, false, theClientDto));
				detailsaldo.addsaldo(ErgebnisgruppeSaldo.SALDO_KUMMULIERT_VORJAHR, getKontoSaldo(krit, konten.get(i),
						es, ErgebnisgruppeSaldo.SALDO_KUMMULIERT_VORJAHR, false, theClientDto));
				detailsaldos[i] = detailsaldo;
			}
			return detailsaldos;
		}
	}

	private BigDecimal getKontoSaldo(ReportErfolgsrechnungKriterienDto krit, Konto konto, ErgebnisgruppeSaldo es,
			int saldoart, boolean bNurEB, TheClientDto theClientDto) {
		List<Konto> konten = new ArrayList<Konto>();
		konten.add(konto);
		ReportErfolgsrechnungKriterienDto krit1 = new ReportErfolgsrechnungKriterienDto();
		krit1.setIGeschaeftsjahr(krit.getIGeschaeftsjahr());
		krit1.setIPeriode(krit.getIPeriode());
		boolean bKummuliert = false;
		switch (saldoart) {
		case ErgebnisgruppeSaldo.SALDO_PERIODE:
			break;
		case ErgebnisgruppeSaldo.SALDO_PERIODE_VORJAHR:
			krit1.setIGeschaeftsjahr(krit.getIGeschaeftsjahr() - 1);
			break;
		case ErgebnisgruppeSaldo.SALDO_KUMMULIERT:
			bKummuliert = true;
			break;
		case ErgebnisgruppeSaldo.SALDO_KUMMULIERT_VORJAHR:
			krit1.setIGeschaeftsjahr(krit.getIGeschaeftsjahr() - 1);
			bKummuliert = true;
			break;
		}
		// TODO: bEroeffnungsbilanz
		if (es.ergebnisgruppeDto.getITyp() == FinanzFac.ERGEBNISGRUPPE_TYP_BILANZGRUPPE_POSITIV)
			return getSammelSaldo(krit1, bKummuliert, konten, true, bNurEB, theClientDto);
		else if (es.ergebnisgruppeDto.getITyp() == FinanzFac.ERGEBNISGRUPPE_TYP_BILANZGRUPPE_NEGATIV)
			return getSammelSaldo(krit1, bKummuliert, konten, false, bNurEB, theClientDto);
		else
			return getSammelSaldo(krit1, bKummuliert, konten, bNurEB, theClientDto);
	}

	private BigDecimal getSammelSaldo(ReportErfolgsrechnungKriterienDto krit, ErgebnisgruppeSaldo es, int saldoart,
			boolean bNurEB, TheClientDto theClientDto) {
		List<Konto> konten = getErgebnisgruppeKonten(es, theClientDto);
		if (konten.size() == 0)
			return new BigDecimal(0);
		else {
			ReportErfolgsrechnungKriterienDto krit1 = new ReportErfolgsrechnungKriterienDto();
			krit1.setIGeschaeftsjahr(krit.getIGeschaeftsjahr());
			krit1.setIPeriode(krit.getIPeriode());
			boolean bKummuliert = false;
			switch (saldoart) {
			case ErgebnisgruppeSaldo.SALDO_PERIODE:
				break;
			case ErgebnisgruppeSaldo.SALDO_PERIODE_VORJAHR:
				krit1.setIGeschaeftsjahr(krit.getIGeschaeftsjahr() - 1);
				break;
			case ErgebnisgruppeSaldo.SALDO_KUMMULIERT:
				bKummuliert = true;
				break;
			case ErgebnisgruppeSaldo.SALDO_KUMMULIERT_VORJAHR:
				krit1.setIGeschaeftsjahr(krit.getIGeschaeftsjahr() - 1);
				bKummuliert = true;
				break;
			}
			if (es.ergebnisgruppeDto.getITyp() == FinanzFac.ERGEBNISGRUPPE_TYP_BILANZGRUPPE_POSITIV)
				return getSammelSaldo(krit1, bKummuliert, konten, true, bNurEB, theClientDto);
			else if (es.ergebnisgruppeDto.getITyp() == FinanzFac.ERGEBNISGRUPPE_TYP_BILANZGRUPPE_NEGATIV)
				return getSammelSaldo(krit1, bKummuliert, konten, false, bNurEB, theClientDto);
			else
				return getSammelSaldo(krit1, bKummuliert, konten, bNurEB, theClientDto);
		}
	}

	private List<Konto> getErgebnisgruppeKonten(ErgebnisgruppeSaldo es, TheClientDto theClientDto) {
		javax.persistence.Query query;
		if (es.ergebnisgruppeDto.getITyp() == FinanzFac.ERGEBNISGRUPPE_TYP_BILANZGRUPPE_NEGATIV)
			query = em.createNamedQuery(Konto.QUERY_ALL_ERGEBNISGRUPPENEGATIV_MANDANT);
		else
			query = em.createNamedQuery(Konto.QUERY_ALL_ERGEBNISGRUPPE_MANDANT);
		query.setParameter(1, es.iId);
		query.setParameter(2, theClientDto.getMandant());
		List<Konto> konten = query.getResultList();
		return konten;
	}

	protected class ErgebnisgruppeSaldo {
		protected Integer iId;
		protected ErgebnisgruppeDto ergebnisgruppeDto;
		protected BigDecimal[] saldo;
		protected ErgebnisgruppeDetailSaldo[] detailsaldos;

		static final int SALDO_PERIODE = 0;
		static final int SALDO_KUMMULIERT = 1;
		static final int SALDO_PERIODE_VORJAHR = 2;
		static final int SALDO_KUMMULIERT_VORJAHR = 3;

		protected ErgebnisgruppeSaldo(ErgebnisgruppeDto ergebnisgruppeDto) {
			this.ergebnisgruppeDto = ergebnisgruppeDto;
			this.saldo = new BigDecimal[4];
			for (int i = 0; i < 4; i++)
				this.saldo[i] = new BigDecimal(0);
			this.iId = ergebnisgruppeDto.getIId();
		}

		public void adddetailsaldo(ErgebnisgruppeDetailSaldo[] detailsaldos) {
			this.detailsaldos = detailsaldos;
		}

		public void addsaldo(Map<Integer, ErgebnisgruppeSaldo> map, int saldoart, BigDecimal saldo) {
			if (Helper.short2boolean(this.ergebnisgruppeDto.getBInvertiert()))
				this.saldo[saldoart] = this.saldo[saldoart].add(saldo.negate());
			else
				this.saldo[saldoart] = this.saldo[saldoart].add(saldo);
			if (this.ergebnisgruppeDto.getErgebnisgruppeIIdSumme() != null)
				if (this.iId.compareTo(this.ergebnisgruppeDto.getErgebnisgruppeIIdSumme()) != 0) // gruppe kann nicht
					// gleichzeitig
					// summmengruppe
					// sein!
					if (Helper.short2boolean(this.ergebnisgruppeDto.getBSummeNegativ()))
						map.get(this.ergebnisgruppeDto.getErgebnisgruppeIIdSumme()).addsaldo(map, saldoart,
								saldo.negate());
					else
						map.get(this.ergebnisgruppeDto.getErgebnisgruppeIIdSumme()).addsaldo(map, saldoart, saldo);
		}
	}

	protected class ErgebnisgruppeDetailSaldo {
		protected Konto konto;
		protected BigDecimal saldo[];
		protected boolean bInvertiert;

		static final int SALDO_PERIODE = 0;
		static final int SALDO_KUMMULIERT = 1;
		static final int SALDO_PERIODE_VORJAHR = 2;
		static final int SALDO_KUMMULIERT_VORJAHR = 3;

		protected ErgebnisgruppeDetailSaldo(Konto konto, Short invertiert) {
			this.konto = konto;
			this.saldo = new BigDecimal[4];
			this.bInvertiert = Helper.short2boolean(invertiert);
			for (int i = 0; i < 4; i++)
				this.saldo[i] = new BigDecimal(0);
		}

		public void addsaldo(int saldoart, BigDecimal saldo) {
			if (bInvertiert)
				this.saldo[saldoart] = this.saldo[saldoart].add(saldo.negate());
			else
				this.saldo[saldoart] = this.saldo[saldoart].add(saldo);

		}

		public void addsaldo(ErgebnisgruppeDto ergebnisgruppeDto, Map<Integer, ErgebnisgruppeSaldo> map, int saldoart,
				BigDecimal saldo) {
		}

	}

	@Override
	public JasperPrintLP printBuchungsbeleg(Integer buchungIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		useCase = UC_BUCHUNGSBELEG;

		Map<String, Object> map = new HashMap<String, Object>();

		BuchungDto buchung = getBuchenFac().buchungFindByPrimaryKey(buchungIId);

		map.put("P_AUTOMATISCHE_BUCHUNG", Helper.short2Boolean(buchung.getbAutomatischeBuchung()));
		map.put("P_AUTOMATISCHE_BUCHUNG_EB", Helper.short2Boolean(buchung.getbAutomatischeBuchungEB()));
		map.put("P_BUCHUNGSDATUM", new Date(buchung.getDBuchungsdatum().getTime()));
		map.put("P_BUCHUNGSART", buchung.getBuchungsartCNr());
		map.put("P_TEXT", buchung.getCText());
		map.put("P_BELEGNUMMER", buchung.getCBelegnummer());
		map.put("P_GESCHAEFTSJAHR", buchung.getIGeschaeftsjahr());
		map.put("P_T_ANLEGEN", buchung.getTAnlegen() == null ? null : new Date(buchung.getTAnlegen().getTime()));
		map.put("P_T_AENDERN", buchung.getTAendern() == null ? null : new Date(buchung.getTAendern().getTime()));
		map.put("P_T_STORNIERT", buchung.getTStorniert() == null ? null : new Date(buchung.getTStorniert().getTime()));
		map.put("P_PERSONAL_ANLEGEN", formatPersonalName(buchung.getPersonalIIdAnlegen()));
		map.put("P_PERSONAL_AENDERN", formatPersonalName(buchung.getPersonalIIdAendern()));
		map.put("P_PERSONAL_STORNIERT", formatPersonalName(buchung.getPersonalIIdStorniert()));

		KostenstelleDto kostenstelle = getSystemFac().kostenstelleFindByPrimaryKey(buchung.getKostenstelleIId());

		map.put("P_KOSTENSTELLE_BEZ", kostenstelle.getCBez());
		map.put("P_KOSTENSTELLE_CNR", kostenstelle.getCNr());

		BuchungdetailDto[] buchungdetails = getBuchenFac().buchungdetailsFindByBuchungIId(buchungIId);

		data = new Object[buchungdetails.length][REPORT_BUCHUNGSBELEG_ANZAHL_SPALTEN];

		for (int i = 0; i < buchungdetails.length; i++) {
			BuchungdetailDto detail = buchungdetails[i];
			data[i][REPORT_BUCHUNGSBELEG_ARTCNR] = detail.getBuchungdetailartCNr();
			data[i][REPORT_BUCHUNGSBELEG_KOMMENTAR] = detail.getCKommentar();

			if (detail.getKontoIId() != null) {
				KontoDto konto = getFinanzFac().kontoFindByPrimaryKey(detail.getKontoIId());
				data[i][REPORT_BUCHUNGSBELEG_KONTOBEZ] = konto.getCBez();
				data[i][REPORT_BUCHUNGSBELEG_KONTONR] = konto.getCNr();
				data[i][REPORT_BUCHUNGSBELEG_KONTOBEM] = konto.getxBemerkung() == null ? "" : konto.getxBemerkung();
				data[i][REPORT_BUCHUNGSBELEG_KONTODTO] = konto;
				Set<Integer> mitlaufendeKonten = getFinanzServiceFac().getAllMitlaufendeKonten(konto.getFinanzamtIId(),
						theClientDto);
				data[i][REPORT_BUCHUNGSBELEG_IST_MITLAUFEND] = mitlaufendeKonten.contains(konto.getIId());
			}

			data[i][REPORT_BUCHUNGSBELEG_AUSZUG] = detail.getIAuszug();
			data[i][REPORT_BUCHUNGSBELEG_BETRAG] = detail.getNBetrag();
			data[i][REPORT_BUCHUNGSBELEG_BETRAGUST] = detail.getNUst();
			data[i][REPORT_BUCHUNGSBELEG_IST_HABEN] = new Boolean(detail.isHabenBuchung());
		}

		initJRDS(map, FinanzReportFac.REPORT_MODUL, FinanzReportFac.REPORT_BUCHUNGSBELEG, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	private String formatPersonalName(Integer iid) {
		if (iid == null)
			return null;
		PersonalDto personal = getPersonalFac().personalFindByPrimaryKey(iid, null);
		if (personal == null)
			return null;
		return personal.getPartnerDto().formatFixName1Name2();
	}
}
