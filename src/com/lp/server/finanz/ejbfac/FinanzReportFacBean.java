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
package com.lp.server.finanz.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
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
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.angebot.fastlanereader.generated.FLRAngebot;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.bestellung.fastlanereader.generated.FLRBestellposition;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.bestellung.service.WareneingangspositionDto;
import com.lp.server.eingangsrechnung.ejb.Eingangsrechnung;
import com.lp.server.eingangsrechnung.ejb.Eingangsrechnungzahlung;
import com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnungReport;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.finanz.ejb.Buchung;
import com.lp.server.finanz.ejb.Buchungdetail;
import com.lp.server.finanz.ejb.BuchungdetailText;
import com.lp.server.finanz.ejb.Finanzamt;
import com.lp.server.finanz.ejb.FinanzamtPK;
import com.lp.server.finanz.ejb.Konto;
import com.lp.server.finanz.ejb.Mahnspesen;
import com.lp.server.finanz.ejb.MahnstufePK;
import com.lp.server.finanz.ejb.Steuerkategorie;
import com.lp.server.finanz.ejb.Uvaart;
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
import com.lp.server.finanz.service.ErgebnisgruppeDto;
import com.lp.server.finanz.service.ExportlaufDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzReportFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.FinanzamtDto;
import com.lp.server.finanz.service.IntrastatDto;
import com.lp.server.finanz.service.KassenbuchDto;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.KontoDtoSmall;
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
import com.lp.server.finanz.service.SammelmahnungDto;
import com.lp.server.finanz.service.SteuerkategorieDto;
import com.lp.server.finanz.service.UvaartDto;
import com.lp.server.finanz.service.UvaverprobungDto;
import com.lp.server.finanz.service.WarenverkehrsnummerDto;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.rechnung.ejb.Rechnung;
import com.lp.server.rechnung.ejb.Rechnungzahlung;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnungReport;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.system.fastlanereader.generated.FLRKostenstelle;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeKassenbuch;
import com.lp.server.system.jcr.service.docnode.DocNodeSaldenliste;
import com.lp.server.system.jcr.service.docnode.DocNodeUVAVerprobung;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.GeschaeftsjahrMandantDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.MwstsatzbezDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.WechselkursDto;
import com.lp.server.system.service.ZahlungszielDto;
import com.lp.server.util.HelperServer;
import com.lp.server.util.LPReport;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.server.util.report.TimingInterceptor;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.report.UvaRpt;

@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
@Interceptors(TimingInterceptor.class)
public class FinanzReportFacBean extends LPReport implements FinanzReportFac,
		JRDataSource {

	@PersistenceContext
	private EntityManager em;

	private final static int UC_RA_SCHREIBEN = 0;
	private final static int UC_MAHNUNG = 1;
	private final static int UC_KONTENLISTE = 2;
	private final static int UC_BUCHUNGEN_IN_BUCHUNGSJOURNAL = 3;
	private final static int UC_BUCHUNGSJOURNAL = 4;
	private final static int UC_BUCHUNGEN_AUF_KONTO = 5;
	private final static int UC_SALDENLISTE = 6;
	private final static int UC_SAMMELMAHNUNG = 7;
	private final static int UC_MAHNLAUF = 8;
	private final static int UC_EXPORTLAUF = 9;
	private final static int UC_INTRASTAT = 10;
	private final static int UC_KONTOBLATT = 11;
	private final static int UC_UVA = 12;
	private final static int UC_OFFENEPOSTEN = 13;
	private final static int UC_LIQUIDITAET = 14;
	private final static int UC_ERFOLGSRECHNUNG = 15;
	private final static int UC_STEUERKATEGORIEN = 16;
	private final static int UC_BUCHUNGSBELEG = 17;

	private final static int REPORT_RA_SCHREIBEN_RECHNUNGSNUMMER = 0;
	private final static int REPORT_RA_SCHREIBEN_RECHNUNGSDATUM = 1;
	private final static int REPORT_RA_SCHREIBEN_ZIELDATUM = 2;
	private final static int REPORT_RA_SCHREIBEN_ZINSSATZ = 3;
	private final static int REPORT_RA_SCHREIBEN_BETRAG = 4;
	private final static int REPORT_RA_SCHREIBEN_ZINSEN = 5;
	private final static int REPORT_RA_SCHREIBEN_MAHNSPESEN = 6;
	private final static int REPORT_RA_SCHREIBEN_ANZAHL_KRITERIEN = 7;

	private final static int REPORT_KONTENLISTE_KONTONUMMER = 0;
	private final static int REPORT_KONTENLISTE_BEZEICHNUNG = 1;
	private final static int REPORT_KONTENLISTE_USTKONTONUMMER = 2;
	private final static int REPORT_KONTENLISTE_SKONTOKONTONUMMER = 3;
	private final static int REPORT_KONTENLISTE_KONTOART = 4;
	private final static int REPORT_KONTENLISTE_STEUERKATEGORIE = 5;
	private final static int REPORT_KONTENLISTE_ERGEBNISGRUPPE = 6;
	private final static int REPORT_KONTENLISTE_UVAART = 7;
	private final static int REPORT_KONTENLISTE_FINANZAMT = 8;
	private final static int REPORT_KONTENLISTE_FINANZAMT_KURZBEZ = 9;
	private final static int REPORT_KONTENLISTE_KOSTENSTELLE = 10;
	private final static int REPORT_KONTENLISTE_BILANZKONTO = 11;
	private final static int REPORT_KONTENLISTE_GUELTIG_BIS = 12;
	private final static int REPORT_KONTENLISTE_GUELTIG_VON = 13;
	private final static int REPORT_KONTENLISTE_EROEFFNUNGSBUCHUNG = 14;
	private final static int REPORT_KONTENLISTE_ALLGEMEIN_SICHTBAR = 15;
	private final static int REPORT_KONTENLISTE_MANUELL_BEBUCHBAR = 16;
	private final static int REPORT_KONTENLISTE_VERSTECKT = 17;
	private final static int REPORT_KONTENLISTE_SORTIERUNG = 18;
	private final static int REPORT_KONTENLISTE_UVAART_CNR = 19;
	private final static int REPORT_KONTENLISTE_ALLE_FELDER = 20;

	private final static int REPORT_BUCHUNGEN_IN_BUCHUNGSJOURNAL_SOLL = 0;
	private final static int REPORT_BUCHUNGEN_IN_BUCHUNGSJOURNAL_HABEN = 1;
	private final static int REPORT_BUCHUNGEN_IN_BUCHUNGSJOURNAL_BUCHUNGSDATUM = 2;
	private final static int REPORT_BUCHUNGEN_IN_BUCHUNGSJOURNAL_BUCHUNGSART = 3;
	private final static int REPORT_BUCHUNGEN_IN_BUCHUNGSJOURNAL_KONTONUMMER = 4;
	private final static int REPORT_BUCHUNGEN_IN_BUCHUNGSJOURNAL_KONTOBEZEICHNUNG = 5;
	private final static int REPORT_BUCHUNGEN_IN_BUCHUNGSJOURNAL_TEXT = 6;

	private final static int REPORT_BUCHUNGSJOURNAL_BUCHUNGSJOURNALDATUM = 0;
	private final static int REPORT_BUCHUNGSJOURNAL_BUCHUNGSDATUM = 1;
	private final static int REPORT_BUCHUNGSJOURNAL_KONTONUMMER = 2;
	private final static int REPORT_BUCHUNGSJOURNAL_GEGENKONTONUMMER = 3;
	private final static int REPORT_BUCHUNGSJOURNAL_BUCHUNGSART = 4;
	private final static int REPORT_BUCHUNGSJOURNAL_BELEGNUMMER = 5;
	private final static int REPORT_BUCHUNGSJOURNAL_TEXT = 6;
	private final static int REPORT_BUCHUNGSJOURNAL_BETRAG = 7;
	private final static int REPORT_BUCHUNGSJOURNAL_UST = 8;
	private final static int REPORT_BUCHUNGSJOURNAL_KOSTENSTELLENUMMER = 9;
	private final static int REPORT_BUCHUNGSJOURNAL_AUSZUG = 10;
	private final static int REPORT_BUCHUNGSJOURNAL_AM = 11;
	private final static int REPORT_BUCHUNGSJOURNAL_UM = 12;
	private final static int REPORT_BUCHUNGSJOURNAL_SOLL = 13;
	private final static int REPORT_BUCHUNGSJOURNAL_HABEN = 14;
	private final static int REPORT_BUCHUNGSJOURNAL_WER = 15;
	private final static int REPORT_BUCHUNGSJOURNAL_STORNIERT = 16;
	private final static int REPORT_BUCHUNGSJOURNAL_ANZAHL_SPALTEN = 17;

	private final static int REPORT_BUCHUNGEN_AUF_KONTO_BETRAG = 0;
	private final static int REPORT_BUCHUNGEN_AUF_KONTO_UST = 1;
	private final static int REPORT_BUCHUNGEN_AUF_KONTO_BUCHUNGSDATUM = 2;
	private final static int REPORT_BUCHUNGEN_AUF_KONTO_BUCHUNGSART = 3;
	private final static int REPORT_BUCHUNGEN_AUF_KONTO_TEXT = 4;
	private final static int REPORT_BUCHUNGEN_AUF_KONTO_AUSZUG = 5;

	private final static int REPORT_SALDENLISTE_KONTONUMMER = 0;
	private final static int REPORT_SALDENLISTE_KONTOBEZEICHNUNG = 1;
	private final static int REPORT_SALDENLISTE_SOLL = 2;
	private final static int REPORT_SALDENLISTE_HABEN = 3;
	private final static int REPORT_SALDENLISTE_EBWERT = 4;
	private final static int REPORT_SALDENLISTE_SOLL_BIS = 5;
	private final static int REPORT_SALDENLISTE_HABEN_BIS = 6;
	private final static int REPORT_SALDENLISTE_STEUERKATEGORIE = 7;
	private final static int REPORT_SALDENLISTE_ERGEBNISGRUPPE = 8;
	private final static int REPORT_SALDENLISTE_EBWERT_BIS = 9;
	private final static int REPORT_SALDENLISTE_UVAART = 10;
	private final static int REPORT_SALDENLISTE_FINANZAMT = 11;
	private final static int REPORT_SALDENLISTE_KONSISTENT = 12;
	private final static int REPORT_SALDENLISTE_ANZAHL_SPALTEN = 13;

	private final static int REPORT_ERFOLGSRECHNUNG_BEZEICHNUNG = 0;
	private final static int REPORT_ERFOLGSRECHNUNG_SALDO_PERIODE = 1;
	private final static int REPORT_ERFOLGSRECHNUNG_SALDO_KUMMULIERT = 2;
	private final static int REPORT_ERFOLGSRECHNUNG_SALDO_PERIODE_VORJAHR = 3;
	private final static int REPORT_ERFOLGSRECHNUNG_SALDO_KUMMULIERT_VORJAHR = 4;
	private final static int REPORT_ERFOLGSRECHNUNG_TYP = 5;
	private final static int REPORT_ERFOLGSRECHNUNG_BEZUGSBASIS = 6;
	private final static int REPORT_ERFOLGSRECHNUNG_KONTO_CNR = 7;
	private final static int REPORT_ERFOLGSRECHNUNG_KONTO_BEZEICHNUNG = 8;
	private final static int REPORT_ERFOLGSRECHNUNG_ANZAHL = 9;

	private final static int REPORT_SAMMELMAHNUNG_RECHNUNGSNUMMER = 0;
	private final static int REPORT_SAMMELMAHNUNG_BELEGDATUM = 1;
	private final static int REPORT_SAMMELMAHNUNG_FAELLIGKEITSDATUM = 2;
	private final static int REPORT_SAMMELMAHNUNG_WERT = 3;
	private final static int REPORT_SAMMELMAHNUNG_OFFEN = 4;
	private final static int REPORT_SAMMELMAHNUNG_MAHNSTUFE = 5;
	private final static int REPORT_SAMMELMAHNUNG_ZINSEN = 6;

	private final static int REPORT_MAHNLAUF_RECHNUNGSNUMMER = 0;
	private final static int REPORT_MAHNLAUF_RECHNUNGSDATUM = 1;
	private final static int REPORT_MAHNLAUF_KUNDE = 2;
	private final static int REPORT_MAHNLAUF_ZIELDATUM = 3;
	private final static int REPORT_MAHNLAUF_WERT = 4;
	private final static int REPORT_MAHNLAUF_OFFEN = 5;
	private final static int REPORT_MAHNLAUF_MAHNSTUFE = 6;
	private final static int REPORT_MAHNLAUF_LETZTEMAHNSTUFE = 7;
	private final static int REPORT_MAHNLAUF_LETZTESMAHNDATUM = 8;
	private final static int REPORT_MAHNLAUF_AUFTRAG_NUMMER = 9;
	private final static int REPORT_MAHNLAUF_AUFTRAG_PROJEKT = 10;
	private final static int REPORT_MAHNLAUF_AUFTRAG_BESTELLNUMMER = 11;
	private final static int REPORT_MAHNLAUF_POSITIONSBEZEICHNUNG1 = 12;
	private final static int REPORT_MAHNLAUF_POSITIONSBEZEICHNUNG2 = 13;
	private final static int REPORT_MAHNLAUF_KUNDE_STATISTIKADRESSE = 14;
	private final static int REPORT_MAHNLAUF_VERTRETER = 15;
	private final static int REPORT_MAHNLAUF_WERTUST = 16;
	private final static int REPORT_MAHNLAUF_OFFENUST = 17;
	private final static int REPORT_MAHNLAUF_RECHNUNGSART = 18;
	private final static int REPORT_MAHNLAUF_MAHNSPERREBIS = 19;

	private final static int REPORT_STEUERKATEGORIE_FINANZAMT = 0;
	private final static int REPORT_STEUERKATEGORIE_STEUERKATEGORIE = 1;
	private final static int REPORT_STEUERKATEGORIE_STEUERKATEGORIEBEZEICHNUNG = 2;
	private final static int REPORT_STEUERKATEGORIE_KONTO_VK = 3;
	private final static int REPORT_STEUERKATEGORIE_KONTO_VK_BEZEICHNUNG = 4;
	private final static int REPORT_STEUERKATEGORIE_KONTO_EK = 5;
	private final static int REPORT_STEUERKATEGORIE_KONTO_EK_BEZEICHNUNG = 6;
	private final static int REPORT_STEUERKATEGORIE_KONTO_SKONTO_EK = 7;
	private final static int REPORT_STEUERKATEGORIE_KONTO_SKONTO_EK_BEZEICHNUNG = 8;
	private final static int REPORT_STEUERKATEGORIE_KONTO_SKONTO_VK = 9;
	private final static int REPORT_STEUERKATEGORIE_KONTO_SKONTO_VK_BEZEICHNUNG = 10;
	private final static int REPORT_STEUERKATEGORIE_KONTO_EINFUHRUST = 11;
	private final static int REPORT_STEUERKATEGORIE_KONTO_EINFUHRUST_BEZEICHNUNG = 12;
	private final static int REPORT_STEUERKATEGORIE_PARAMETER_SACHKONTO = 13;
	private final static int REPORT_STEUERKATEGORIE_PARAMETER_SACHKONTO_BEZEICHNUNG = 14;
	private final static int REPORT_STEUERKATEGORIE_PARAMETER_DEBITOREN = 15;
	private final static int REPORT_STEUERKATEGORIE_PARAMETER_DEBITOREN_BEZEICHNUNG = 16;
	private final static int REPORT_STEUERKATEGORIE_PARAMETER_KREDITOREN = 17;
	private final static int REPORT_STEUERKATEGORIE_PARAMETER_KREDITOREN_BEZEICHNUNG = 18;
	private final static int REPORT_STEUERKATEGORIE_PARAMETER_VERR_ERHALTEN = 19;
	private final static int REPORT_STEUERKATEGORIE_PARAMETER_VERR_ERHALTEN_BEZEICHNUNG = 20;
	private final static int REPORT_STEUERKATEGORIE_PARAMETER_ERHALTEN = 21;
	private final static int REPORT_STEUERKATEGORIE_PARAMETER_ERHALTEN_BEZEICHNUNG = 22;
	private final static int REPORT_STEUERKATEGORIE_PARAMETER_VERR_GELEISTET = 23;
	private final static int REPORT_STEUERKATEGORIE_PARAMETER_VERR_GELEISTET_BEZEICHNUNG = 24;
	private final static int REPORT_STEUERKATEGORIE_PARAMETER_GELEISTET = 25;
	private final static int REPORT_STEUERKATEGORIE_PARAMETER_GELEISTET_BEZEICHNUNG = 26;
	private final static int REPORT_STEUERKATEGORIE_REVERSECHARGE = 27;
	private final static int REPORT_STEUERKATEGORIE_MWST_BEZEICHNUNG = 28;
	private final static int REPORT_STEUERKATEGORIE_KONTO_FORDERUNGEN = 29;
	private final static int REPORT_STEUERKATEGORIE_KONTO_FORDERUNGEN_BEZEICHNUNG = 30;
	private final static int REPORT_STEUERKATEGORIE_KONTO_VERBINDLICHKEITEN = 31;
	private final static int REPORT_STEUERKATEGORIE_KONTO_VERBINDLICHKEITEN_BEZEICHNUNG = 32;
	private final static int REPORT_STEUERKATEGORIE_KONTO_KURSGEWINNE = 33;
	private final static int REPORT_STEUERKATEGORIE_KONTO_KURSGEWINNE_BEZEICHNUNG = 34;
	private final static int REPORT_STEUERKATEGORIE_KONTO_KURSVERLUSTE = 35;
	private final static int REPORT_STEUERKATEGORIE_KONTO_KURSVERLUSTE_BEZEICHNUNG = 36;
	private final static int REPORT_STEUERKATEGORIE_ANZAHL_FELDER = 37;

	private final static int REPORT_INTRASTAT_BELEGNUMMER = 0;
	private final static int REPORT_INTRASTAT_IDENT = 1;
	private final static int REPORT_INTRASTAT_BEZEICHNUNG = 2;
	private final static int REPORT_INTRASTAT_MENGE = 3;
	private final static int REPORT_INTRASTAT_WARENVERKEHRSNUMMER = 4;
	private final static int REPORT_INTRASTAT_PREIS = 5;
	private final static int REPORT_INTRASTAT_WERT = 6;
	private final static int REPORT_INTRASTAT_STATISTISCHER_WERT = 7;
	private final static int REPORT_INTRASTAT_GEWICHT = 8;
	private final static int REPORT_INTRASTAT_BELEGART = 9;
	private final static int REPORT_INTRASTAT_WVK_BEZEICHNUNG = 10;
	private final static int REPORT_INTRASTAT_UID = 11;
	private final static int REPORT_INTRASTAT_ANZAHL_SPALTEN = 12;

	private final static int REPORT_KONTOBLATT_BUCHUNGSDATUM = 0;
	private final static int REPORT_KONTOBLATT_BUCHUNGSART = 1;
	private final static int REPORT_KONTOBLATT_BELEG = 2;
	private final static int REPORT_KONTOBLATT_GEGENKONTO = 3;
	private final static int REPORT_KONTOBLATT_TEXT = 4;
	private final static int REPORT_KONTOBLATT_SOLL = 5;
	private final static int REPORT_KONTOBLATT_HABEN = 6;
	private final static int REPORT_KONTOBLATT_USTWERT = 7;
	private final static int REPORT_KONTOBLATT_USTPROZENT = 8;
	private final static int REPORT_KONTOBLATT_AUSZUG = 9;
	private final static int REPORT_KONTOBLATT_BELEGARTCNR = 10;
	private final static int REPORT_KONTOBLATT_SOLL_FW = 11;
	private final static int REPORT_KONTOBLATT_HABEN_FW = 12;
	private final static int REPORT_KONTOBLATT_BELEGKURS = 13;
	private final static int REPORT_KONTOBLATT_KURSZUDATUM = 14;
	private final static int REPORT_KONTOBLATT_BELEGWAEHRUNG = 15;
	private final static int REPORT_KONTOBLATT_AUTOMATISCHE_EB = 16;
	private final static int REPORT_KONTOBLATT_ANZAHL_SPALTEN = 17;

	private final static int REPORT_OP_BUCHUNGSDATUM = 0;
	private final static int REPORT_OP_BUCHUNGSART = 1;
	private final static int REPORT_OP_BELEG = 2;
	private final static int REPORT_OP_GEGENKONTO = 3;
	private final static int REPORT_OP_TEXT = 4;
	private final static int REPORT_OP_SOLL = 5;
	private final static int REPORT_OP_HABEN = 6;
	private final static int REPORT_OP_USTWERT = 7;
	private final static int REPORT_OP_USTPROZENT = 8;
	private final static int REPORT_OP_AUSZUG = 9;
	private final static int REPORT_OP_BELEGARTCNR = 10;
	private final static int REPORT_OP_KONTOCNR = 11;
	private final static int REPORT_OFFENEPOSTEN_ANZAHL_SPALTEN = 12;

	private final static int REPORT_LV_BELEG = 0;
	private final static int REPORT_LV_PARTNER = 1;
	private final static int REPORT_LV_JAHR = 2;
	private final static int REPORT_LV_WOCHE = 3;
	private final static int REPORT_LV_NETTO = 4;
	private final static int REPORT_LV_MWST = 5;
	private final static int REPORT_LV_ZIELTAGE_ZAHLUGNSMORAL = 6;
	private final static int REPORT_LV_ZIELTAGE_VEREINBARTES_ZAHLUNGSZIEL = 7;
	private final static int REPORT_LV_IMPORTIERT = 8;
	private final static int REPORT_LV_ANZAHL_SPALTEN = 9;

	private final static int REPORT_BUCHUNGSBELEG_ARTCNR = 0;
	private final static int REPORT_BUCHUNGSBELEG_KOMMENTAR = 1;
	private final static int REPORT_BUCHUNGSBELEG_KONTOBEZ = 2;
	private final static int REPORT_BUCHUNGSBELEG_KONTONR = 3;
	private final static int REPORT_BUCHUNGSBELEG_KONTOBEM = 4;
	private final static int REPORT_BUCHUNGSBELEG_KONTODTO = 5;
	private final static int REPORT_BUCHUNGSBELEG_AUSZUG = 6;
	private final static int REPORT_BUCHUNGSBELEG_BETRAG = 7;
	private final static int REPORT_BUCHUNGSBELEG_BETRAGUST = 8;
	private final static int REPORT_BUCHUNGSBELEG_IST_HABEN = 9;
	private final static int REPORT_BUCHUNGSBELEG_ANZAHL_SPALTEN = 10;

	private int useCase;
	private Object[][] data = null;

	public class ReportFieldValues {
		private Map<String, Integer> fieldMap;

		public ReportFieldValues(Map<String, Integer> fieldMap) {
			this.fieldMap = fieldMap;
		}

		public void setFieldMap(Map<String, Integer> fieldMap) {
			if (null == fieldMap)
				throw new IllegalArgumentException("fieldMap");

			this.fieldMap = fieldMap;
		}

		public Object getFieldValue(String fieldname) {
			Integer fieldNumber = fieldMap.get(fieldname);
			if (null == fieldNumber)
				return null;

			return data[index][fieldNumber];
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
			put("F_FINANZAMT", REPORT_KONTENLISTE_FINANZAMT);
			put("F_FINANZAMT_KURZBEZ", REPORT_KONTENLISTE_FINANZAMT_KURZBEZ);
			put("F_KOSTENSTELLE", REPORT_KONTENLISTE_KOSTENSTELLE);
			put("F_WEITERFUEHRENDEBILANZ_KONTONUMMER",
					REPORT_KONTENLISTE_BILANZKONTO);
			put("F_GUELTIG_BIS", REPORT_KONTENLISTE_GUELTIG_BIS);
			put("F_GUELTIG_VON", REPORT_KONTENLISTE_GUELTIG_VON);
			put("F_AUTOMATISCHE_EROEFFNUNGSBUCHUNG",
					REPORT_KONTENLISTE_EROEFFNUNGSBUCHUNG);
			put("F_ALLGEMEIN_SICHTBAR", REPORT_KONTENLISTE_ALLGEMEIN_SICHTBAR);
			put("F_MANUELL_BEBUCHBAR", REPORT_KONTENLISTE_MANUELL_BEBUCHBAR);
			put("F_VERSTECKT", REPORT_KONTENLISTE_VERSTECKT);
			put("F_SORTIERUNG", REPORT_KONTENLISTE_SORTIERUNG);
			put("F_UVAART_CNR", REPORT_KONTENLISTE_UVAART_CNR);
		}
	};

	private ReportFieldValues reportKontenListe = new ReportFieldValues(
			reportKontenListeIndexer);

	private ReportFieldValues reportKontoblatt = new ReportFieldValues(
			new HashMap<String, Integer>() {

				private static final long serialVersionUID = 1L;

				{
					put("Auszug", REPORT_KONTOBLATT_AUSZUG);
					put("Beleg", REPORT_KONTOBLATT_BELEG);
					put("Buchungsart", REPORT_KONTOBLATT_BUCHUNGSART);
					put("Buchungsdatum", REPORT_KONTOBLATT_BUCHUNGSDATUM);
					put("Gegenkonto", REPORT_KONTOBLATT_GEGENKONTO);
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
				}
			});

	public static HashMap<String, Integer> reportErfolgsrechnungIndexer = new HashMap<String, Integer>() {
		private static final long serialVersionUID = 1L;
		{
			put("F_BEZEICHNUNG", REPORT_ERFOLGSRECHNUNG_BEZEICHNUNG);
			put("F_SALDO_MONAT", REPORT_ERFOLGSRECHNUNG_SALDO_PERIODE);
			put("F_SALDO_KUMMULIERT", REPORT_ERFOLGSRECHNUNG_SALDO_KUMMULIERT);
			put("F_SALDO_MONAT_VORJAHR",
					REPORT_ERFOLGSRECHNUNG_SALDO_PERIODE_VORJAHR);
			put("F_SALDO_KUMMULIERT_VORJAHR",
					REPORT_ERFOLGSRECHNUNG_SALDO_KUMMULIERT_VORJAHR);
			put("F_TYP", REPORT_ERFOLGSRECHNUNG_TYP);
			put("F_BEZUGSBASIS", REPORT_ERFOLGSRECHNUNG_BEZUGSBASIS);
			put("F_KONTO_CNR", REPORT_ERFOLGSRECHNUNG_KONTO_CNR);
			put("F_KONTO_BEZEICHNUNG", REPORT_ERFOLGSRECHNUNG_KONTO_BEZEICHNUNG);
		}
	};

	private ReportFieldValues reportErfolgsrechnung = new ReportFieldValues(
			reportErfolgsrechnungIndexer);

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
			put("F_FINANZAMT", REPORT_SALDENLISTE_FINANZAMT);
			put("F_KONTO_KONSISTENT", REPORT_SALDENLISTE_KONSISTENT);
		}
	};

	private ReportFieldValues reportSaldenliste = new ReportFieldValues(
			reportSaldenlisteIndexer);

	private ReportFieldValues reportBuchungsbelegListe = new ReportFieldValues(
			new HashMap<String, Integer>() {
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
		case UC_KONTOBLATT: {
			value = reportKontoblatt.getFieldValue(fieldName);
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
			if ("F_BUCHUNGSJOURNALDATUM".equals(fieldName)) {
				value = data[index][REPORT_BUCHUNGSJOURNAL_BUCHUNGSJOURNALDATUM];
			} else if ("F_BUCHUNGSDATUM".equals(fieldName)) {
				value = data[index][REPORT_BUCHUNGSJOURNAL_BUCHUNGSDATUM];
			} else if ("F_KONTONUMMER".equals(fieldName)) {
				value = data[index][REPORT_BUCHUNGSJOURNAL_KONTONUMMER];
			} else if ("F_GEGENKONTONUMMER".equals(fieldName)) {
				value = data[index][REPORT_BUCHUNGSJOURNAL_GEGENKONTONUMMER];
			} else if ("F_BUCHUNGSART".equals(fieldName)) {
				value = data[index][REPORT_BUCHUNGSJOURNAL_BUCHUNGSART];
			} else if ("F_BELEGNUMMER".equals(fieldName)) {
				value = data[index][REPORT_BUCHUNGSJOURNAL_BELEGNUMMER];
			} else if ("F_TEXT".equals(fieldName)) {
				value = data[index][REPORT_BUCHUNGSJOURNAL_TEXT];
			} else if ("F_BETRAG".equals(fieldName)) {
				value = data[index][REPORT_BUCHUNGSJOURNAL_BETRAG];
			} else if ("F_UST".equals(fieldName)) {
				value = data[index][REPORT_BUCHUNGSJOURNAL_UST];
			} else if ("F_STORNIERT".equals(fieldName)) {
				value = data[index][REPORT_BUCHUNGSJOURNAL_STORNIERT];
			} else if ("F_KOSTENSTELLENUMMER".equals(fieldName)) {
				value = data[index][REPORT_BUCHUNGSJOURNAL_KOSTENSTELLENUMMER];
			} else if ("F_AUSZUG".equals(fieldName)) {
				value = data[index][REPORT_BUCHUNGSJOURNAL_AUSZUG];
			} else if ("F_AM".equals(fieldName)) {
				value = data[index][REPORT_BUCHUNGSJOURNAL_AM];
			} else if ("F_UM".equals(fieldName)) {
				value = data[index][REPORT_BUCHUNGSJOURNAL_UM];
			} else if ("F_SOLL".equals(fieldName)) {
				value = data[index][REPORT_BUCHUNGSJOURNAL_SOLL];
			} else if ("F_HABEN".equals(fieldName)) {
				value = data[index][REPORT_BUCHUNGSJOURNAL_HABEN];
			} else if ("F_WER".equals(fieldName)) {
				value = data[index][REPORT_BUCHUNGSJOURNAL_WER];
			}
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
			} else if ("F_PARAMETER_VERR_ERHALTEN_BEZEICHNUNG"
					.equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_PARAMETER_VERR_ERHALTEN_BEZEICHNUNG];
			} else if ("F_PARAMETER_ERHALTEN".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_PARAMETER_ERHALTEN];
			} else if ("F_PARAMETER_ERHALTEN_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_PARAMETER_ERHALTEN_BEZEICHNUNG];
			} else if ("F_PARAMETER_VERR_GELEISTET".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_PARAMETER_VERR_GELEISTET];
			} else if ("F_PARAMETER_VERR_GELEISTET_BEZEICHNUNG"
					.equals(fieldName)) {
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
			}

			else if ("F_KONTO_KURSGEWINNE".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_KONTO_KURSGEWINNE];
			} else if ("F_KONTO_KURSGEWINNE_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_KONTO_KURSGEWINNE_BEZEICHNUNG];
			} else if ("F_KONTO_FORDERUNGEN".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_KONTO_FORDERUNGEN];
			} else if ("F_KONTO_FORDERUNGEN_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_KONTO_FORDERUNGEN_BEZEICHNUNG];
			} else if ("F_KONTO_VERBINDLICHKEITEN".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_KONTO_VERBINDLICHKEITEN];
			} else if ("F_KONTO_VERBINDLICHKEITEN_BEZEICHNUNG"
					.equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_KONTO_VERBINDLICHKEITEN_BEZEICHNUNG];
			} else if ("F_REVERSECHARGE".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_REVERSECHARGE];
			} else if ("F_MWST_BEZEICHNUNG".equals(fieldName)) {
				value = data[index][REPORT_STEUERKATEGORIE_MWST_BEZEICHNUNG];
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
			}
		}
			break;
		case UC_LIQUIDITAET: {
			if ("Beleg".equals(fieldName)) {
				value = data[index][REPORT_LV_BELEG];
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

		public KontoblaetterPrinter() {
			reportParameter = new ReportParameter();
		}

		public JasperPrintLP print(PrintKontoblaetterModel kbModel,
				TheClientDto theClientDto) throws EJBExceptionLP,
				RemoteException {
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
				EnumSortOrder sortOrder = getSortOrderForKonto(konto,
						kbModel.getKontoIId() == null, kbModel);

				Session session2 = FLRSessionFactory.getFactory().openSession();
				List<?> blattDetails = getKontoDetails(session2, kbModel,
						konto, sortOrder, theClientDto);
				boolean istSachkonto = konto.getKontotyp_c_nr().equals(
						FinanzServiceFac.KONTOTYP_SACHKONTO);
				boolean istBankoderKasse = getFinanzFac().isKontoMitSaldo(
						konto.getI_id(), theClientDto);
				data = getReportBuchungen(blattDetails.size(),
						blattDetails.iterator(),
						kbModel.isDruckInMandantenWaehrung(),
						konto.getWaehrung_c_nr_druck(), istSachkonto,
						istBankoderKasse, theClientDto);

				verifyKontoIsConsistentWithEB(kbModel, konto,
						blattDetails.iterator(), theClientDto);
				session2.close();

				initJRDS(reportParameter.getParameters(),
						FinanzReportFac.REPORT_MODUL, getReportName(),
						theClientDto.getMandant(), theClientDto.getLocUi(),
						theClientDto);

				if (print != null) {
					print = Helper.addReport2Report(print, getReportPrint()
							.getPrint());
				} else {
					print = getReportPrint();
				}
			}

			session.close();

			if (null != kbModel.getPeriodeImGJ()) {
				PrintInfoDto values = new PrintInfoDto();
				values.setDocPath(new DocPath(new DocNodeKassenbuch(kbModel,
						theClientDto.getMandant())));
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
			java.sql.Timestamp bisStamp = new java.sql.Timestamp(kbModel
					.gettBis().getTime() + 24 * 3600000);
			kbModel.settBis(bisStamp);
		}

		protected void recalculateDateRangeBasedOnPeriode(
				PrintKontoblaetterModel kbModel, TheClientDto theClientDto) {
			Timestamp[] tVonBis = getBuchenFac().getDatumVonBisGeschaeftsjahr(
					kbModel.getGeschaeftsjahr(), theClientDto);
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

		protected void calculateBeginnPeriode(PrintKontoblaetterModel kbModel,
				TheClientDto theClientDto) {
			if (kbModel.getPeriodeImGJ() != null) {
				vonPeriode = kbModel.getPeriodeImGJ();
				if (vonPeriode < 1)
					vonPeriode = 1;
				recalculateDateRangeBasedOnPeriode(kbModel, theClientDto);

				return;
			}

			vonPeriode = null;
			if (kbModel.gettVon() != null) {
				java.sql.Date dVon = new java.sql.Date(kbModel.gettVon()
						.getTime());
				vonPeriode = getBuchenFac().getPeriodeImGJFuerDatum(dVon,
						theClientDto.getMandant());
			}
		}

		/**
		 * Ermittelt den Saldo f&uuml;r das Konto aus der Vorperiode</br>
		 * 
		 * Ausgehend vom Beginndatum wird die Vorperiode ermittelt und f&uuml;r diese
		 * dann der Saldo des Kontos errechnet.</br> Es gibt Konten die keinen
		 * Vorsaldo kennen/k&ouml;nnen. F&uuml;r diese wird null geliefert
		 * 
		 * @param kontoIId
		 *            das zu ermittelnde Konto
		 * @param kbModel das DatenModel zum Kontoblattdruck
		 * @param vonPeriode
		 *            Ist das Beginndatum der aktuellen Periode
		 * @param waehrungCNrDruck die CNR der beim Druck zu verwendenden W&auml;hrung
		 * @param theClientDto
		 * @return Saldo der Vorperiode. F&uuml;r Konten die keinen Saldo
		 *         kennen/k&ouml;nnen oder wo die Vorperiode im vorherigen
		 *         Gesch&auml;ftsjahr liegt wird null geliefert.
		 * @throws RemoteException
		 * @throws EJBExceptionLP
		 */
		protected BigDecimal calculateSaldoVonKontoFuerVorPeriode(
				Integer kontoIId, PrintKontoblaetterModel kbModel,
				Integer vonPeriode, String waehrungCNrDruck,
				TheClientDto theClientDto) throws EJBExceptionLP,
				RemoteException {

			// Wenn es kein Vorsaldo gibt (Kreditoren/Debitorenkonto, bzw. keine
			// Bank/Kassakonten dann null liefern
			BigDecimal vorperiodenSaldo = null;

			if (kbModel.isEnableSaldo()
					&& (vonPeriode != null && vonPeriode > 1)) {
				if (kbModel.isDruckInMandantenWaehrung()
						|| waehrungCNrDruck == null
						|| waehrungCNrDruck.equals(theClientDto
								.getSMandantenwaehrung())) {
					vorperiodenSaldo = getBuchenFac().getSaldoOhneEBVonKonto(
							kontoIId, kbModel.getGeschaeftsjahr(),
							vonPeriode - 1, true, theClientDto);
					vorperiodenSaldo = vorperiodenSaldo.add(getBuchenFac()
							.getSummeEroeffnungKontoIId(kontoIId,
									kbModel.getGeschaeftsjahr(),
									vonPeriode - 1, true, theClientDto));
				} else {
					vorperiodenSaldo = getBuchenFac()
							.getSaldoVonKontoInWaehrung(kontoIId,
									kbModel.getGeschaeftsjahr(),
									vonPeriode - 1, true, waehrungCNrDruck,
									theClientDto);
					vorperiodenSaldo = vorperiodenSaldo.add(getBuchenFac()
							.getSummeEroeffnungKontoIIdInWaehrung(kontoIId,
									kbModel.getGeschaeftsjahr(),
									vonPeriode - 1, true, waehrungCNrDruck,
									theClientDto));
				}
				if (null != vorperiodenSaldo) {
					vorperiodenSaldo = vorperiodenSaldo
							.setScale(FinanzFac.NACHKOMMASTELLEN);
				}
			}

			return vorperiodenSaldo;
		}

		protected PrintKontoblaetterModel.EnumSortOrder getEnumSortOrderFromKonto(
				String kontoC_sortierung) {
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

		protected EnumSortOrder getSortOrderForKonto(FLRFinanzKonto account,
				boolean multipleAccounts, PrintKontoblaetterModel kbModel) {
			PrintKontoblaetterModel.EnumSortOrder sortOrder = kbModel
					.getSortOrder();

			if (multipleAccounts) {
				EnumSortOrder kontoSortOrder = getEnumSortOrderFromKonto(account
						.getC_sortierung());
				if (kontoSortOrder != EnumSortOrder.SORT_UNDEFINED) {
					sortOrder = kontoSortOrder;
				}
			}

			return sortOrder;
		}

		protected List<?> getKontoBlaetter(Session session,
				PrintKontoblaetterModel kbModel, TheClientDto theClientDto) {
			Criteria crit = session.createCriteria(FLRFinanzKonto.class).add(
					Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));

			addCriteriasForKontos(crit, kbModel);
			addParametersForKontos(kbModel, theClientDto);

			return crit.list();
		}

		protected List<?> getKontoDetails(Session session2,
				PrintKontoblaetterModel kbModel, FLRFinanzKonto konto,
				EnumSortOrder sortOrder, TheClientDto theClientDto)
				throws EJBExceptionLP, RemoteException {
			Criteria crit2 = session2
					.createCriteria(FLRFinanzBuchungDetail.class);

			addCriteriasForDetails(crit2, kbModel, sortOrder, konto);
			addParametersForDetails(kbModel, sortOrder, konto, theClientDto);

			return crit2.list();
		}

		protected Object[][] getReportBuchungen(int sizeBuchungen,
				Iterator<?> buchungen, boolean druckInMandantenWaehrung,
				String waehrungDruck, boolean istSachkonto,
				boolean istBankoderKasse, TheClientDto theClientDto)
				throws EJBExceptionLP, RemoteException {
			Object[][] data = new Object[sizeBuchungen][REPORT_KONTOBLATT_ANZAHL_SPALTEN];
			int i = 0;

			BuchungdetailText buchungdetailText = new BuchungdetailText(
					getBenutzerServicesFac(), theClientDto);

			while (buchungen.hasNext()) {
				BigDecimal soll = null;
				BigDecimal haben = null;
				BigDecimal ust = null;
				BigDecimal sollFW = null;
				BigDecimal habenFW = null;
				BigDecimal belegkurs = null;
				BigDecimal kurszudatum = null;
				String belegwaehrung = null;

				FLRFinanzBuchungDetail buchungDetail = (FLRFinanzBuchungDetail) buchungen
						.next();

				if (buchungDetail.getFlrbuchung() != null) {
					String s = buchungdetailText
							.getTextFuerBuchungsart(buchungDetail
									.getFlrbuchung());
					data[i][REPORT_KONTOBLATT_BUCHUNGSART] = s;

					// FLRFbbelegart flrBelegart =
					// buchungDetail.getFlrbuchung().getFlrfbbelegart() ;
					// FLRBuchungsart flrBuchungsart =
					// buchungDetail.getFlrbuchung().getFlrbuchungsart() ;
					//
					// if (buchungDetail.getFlrbuchung().getFlrfbbelegart() !=
					// null) {
					// String art = buchungDetail.getFlrbuchung()
					// .getFlrbuchungsart().getC_kbez();
					// if (art == null)
					// art = "__";
					// String belegart = buchungDetail.getFlrbuchung()
					// .getFlrfbbelegart().getC_kbez();
					//
					// data[i][REPORT_KONTOBLATT_BUCHUNGSART] = art + belegart;
					// }
				}

				data[i][REPORT_KONTOBLATT_BELEG] = buchungDetail
						.getFlrbuchung().getC_belegnummer();
				data[i][REPORT_KONTOBLATT_BELEGARTCNR] = buchungDetail
						.getFlrbuchung().getBuchungsart_c_nr();
				data[i][REPORT_KONTOBLATT_TEXT] = buchungDetail.getFlrbuchung()
						.getC_text();
				data[i][REPORT_KONTOBLATT_BUCHUNGSDATUM] = buchungDetail
						.getFlrbuchung().getD_buchungsdatum();
				data[i][REPORT_KONTOBLATT_AUSZUG] = buchungDetail.getI_auszug();
				if (buchungDetail.getFlrgegenkonto() != null) {
					data[i][REPORT_KONTOBLATT_GEGENKONTO] = buchungDetail
							.getFlrgegenkonto().getC_nr();
				}

				if (buchungDetail.getBuchungdetailart_c_nr().equals(
						BuchenFac.HabenBuchung)) {
					haben = buchungDetail.getN_betrag();
				} else {
					soll = buchungDetail.getN_betrag();
				}
				ust = buchungDetail.getN_ust();
				if (ust.compareTo(new BigDecimal(0.00)) != 0) {
					BigDecimal ustprozent = null;
					try {
						BigDecimal netto = new BigDecimal(0);
						if (istSachkonto && !istBankoderKasse) {
							// nur Sachkonten die keine Bank oder Kasse sind
							// buchen netto
							netto = buchungDetail.getN_betrag().abs();
						} else {
							netto = buchungDetail.getN_betrag().abs()
									.subtract(buchungDetail.getN_ust().abs());
						}
						ustprozent = Helper.getProzentsatzBD(netto,
								buchungDetail.getN_ust().abs(),
								FinanzFac.NACHKOMMASTELLEN);
					} catch (Exception e) {
						//
					}
					data[i][REPORT_KONTOBLATT_USTPROZENT] = ustprozent;
				} else {
					data[i][REPORT_KONTOBLATT_USTPROZENT] = null;
				}

				BelegbuchungDto bbDto = getBelegbuchungFac(
						theClientDto.getMandant())
						.belegbuchungFindByBuchungIIdOhneExc(
								buchungDetail.getFlrbuchung().getI_id());
				if (bbDto == null) {
					// keine Belegbuchung
					if (!druckInMandantenWaehrung
							&& waehrungDruck != null
							&& !waehrungDruck.equals(theClientDto
									.getSMandantenwaehrung())) {
						// es soll in Kontowaehrung gedruckt werden und
						// Kontowaehrng != Mandantenwaehrung
						// dann wird der passende Tageskurs gesucht und
						// umgerechnet
						WechselkursDto wkDto = null;
						try {
							wkDto = getLocaleFac()
									.getKursZuDatum(
											theClientDto
													.getSMandantenwaehrung(),
											waehrungDruck,
											(java.sql.Date) buchungDetail
													.getFlrbuchung()
													.getD_buchungsdatum(),
											theClientDto);
						} catch (Throwable t) {
							//
						}
						if (wkDto != null) {
							kurszudatum = wkDto.getNKurs();
							if (haben != null)
								haben = Helper.rundeKaufmaennisch(
										haben.multiply(wkDto.getNKurs()),
										FinanzFac.NACHKOMMASTELLEN);
							if (soll != null)
								soll = Helper.rundeKaufmaennisch(
										soll.multiply(wkDto.getNKurs()),
										FinanzFac.NACHKOMMASTELLEN);
						} else {
							ArrayList<Object> allInfo = new ArrayList<Object>();
							allInfo.add(waehrungDruck);
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_KEIN_WECHSELKURS_HINTERLEGT,
									allInfo,
									new Exception("Kein Wechselkurs von "
											+ theClientDto
													.getSMandantenwaehrung()
											+ " auf " + waehrungDruck));
						}
					}
				} else {
					// Buchung ist ein Beleg
					java.sql.Date belegdatum = null;
					if (bbDto.getBelegartCNr().equals(
							LocaleFac.BELEGART_RECHNUNG)
							|| bbDto.getBelegartCNr().equals(
									LocaleFac.BELEGART_GUTSCHRIFT)) {
						Rechnung rechnung = em.find(Rechnung.class,
								bbDto.getIBelegiid());
						if (rechnung != null) {
							belegkurs = rechnung.getNKurs();
							belegwaehrung = rechnung.getWaehrungCNr();
							belegdatum = new java.sql.Date(rechnung
									.getTBelegdatum().getTime());
						}
					} else if (bbDto.getBelegartCNr().equals(
							LocaleFac.BELEGART_REZAHLUNG)) {
						Rechnungzahlung zahlung = em.find(
								Rechnungzahlung.class, bbDto.getIBelegiid());
						if (zahlung != null) {
							belegkurs = zahlung.getNKurs();
							Rechnung rechnung = em.find(Rechnung.class,
									zahlung.getRechnungIId());
							if (rechnung != null)
								belegwaehrung = rechnung.getWaehrungCNr();
							belegdatum = zahlung.getTZahldatum();
						}
					} else if (bbDto.getBelegartCNr().equals(
							LocaleFac.BELEGART_EINGANGSRECHNUNG)) {
						Eingangsrechnung rechnung = em.find(
								Eingangsrechnung.class, bbDto.getIBelegiid());
						if (rechnung != null) {
							belegkurs = Helper.rundeKaufmaennisch(
									Helper.getKehrwert(rechnung.getNKurs()),
									rechnung.getNKurs().scale());
							belegwaehrung = rechnung.getWaehrungCNr();
							belegdatum = new java.sql.Date(rechnung
									.getTBelegdatum().getTime());
						}
					} else if (bbDto.getBelegartCNr().equals(
							LocaleFac.BELEGART_ERZAHLUNG)) {
						Eingangsrechnungzahlung zahlung = em.find(
								Eingangsrechnungzahlung.class,
								bbDto.getIBelegiid());
						if (zahlung != null) {
							belegkurs = zahlung.getNKurs();
							Eingangsrechnung rechnung = em.find(
									Eingangsrechnung.class,
									zahlung.getEingangsrechnungIId());
							if (rechnung != null)
								belegwaehrung = rechnung.getWaehrungCNr();
							belegdatum = zahlung.getTZahldatum();
						}
					}
					if (belegkurs == null) {
						myLogger.warn("Kurs null fuer Belegart '"
								+ bbDto.getBelegartCNr() + "'.");
					}

					if (!druckInMandantenWaehrung
							&& waehrungDruck != null
							&& !waehrungDruck.equals(theClientDto
									.getSMandantenwaehrung())) {
						// es soll in Kontowaehrung gedruckt werden und
						// Kontowaehrung != Mandantenwaehrung
						// fremwaehrungen werden nicht gesetzt lt. Werner!
						if (belegwaehrung.equals(waehrungDruck)) {
							// es soll in der Belegwaehrung gedruckt werden ->
							// kurs ist belegkurs
							if (soll != null)
								soll = Helper.rundeKaufmaennisch(
										soll.multiply(belegkurs),
										FinanzFac.NACHKOMMASTELLEN);
							if (haben != null)
								haben = Helper.rundeKaufmaennisch(
										haben.multiply(belegkurs),
										FinanzFac.NACHKOMMASTELLEN);
							if (ust != null)
								ust = Helper.rundeKaufmaennisch(
										ust.multiply(belegkurs),
										FinanzFac.NACHKOMMASTELLEN);
						} else {
							/*
							 * fremdw&auml;hrung setzen und umrechnen aus der
							 * Mandantenw&auml;hrung
							 * data[i][REPORT_KONTOBLATT_BELEGKURS] = belegkurs;
							 * data[i][REPORT_KONTOBLATT_BELEGWAEHRUNG] =
							 * belegwaehrung;
							 */
							WechselkursDto wkDto = null;
							try {
								wkDto = getLocaleFac().getKursZuDatum(
										theClientDto.getSMandantenwaehrung(),
										waehrungDruck,
										(java.sql.Date) buchungDetail
												.getFlrbuchung()
												.getD_buchungsdatum(),
										theClientDto);
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
									haben = Helper.rundeKaufmaennisch(
											haben.multiply(wkDto.getNKurs()),
											FinanzFac.NACHKOMMASTELLEN);
								}
								if (soll != null) {
									// sollFW =
									// Helper.rundeKaufmaennisch(soll.multiply(
									// belegkurs),
									// FinanzFac.NACHKOMMASTELLEN);
									soll = Helper.rundeKaufmaennisch(
											soll.multiply(wkDto.getNKurs()),
											FinanzFac.NACHKOMMASTELLEN);
								}
								if (ust != null)
									ust = Helper.rundeKaufmaennisch(
											ust.multiply(wkDto.getNKurs()),
											FinanzFac.NACHKOMMASTELLEN);
							} else {
								ArrayList<Object> allInfo = new ArrayList<Object>();
								allInfo.add(waehrungDruck);
								throw new EJBExceptionLP(
										EJBExceptionLP.FEHLER_KEIN_WECHSELKURS_HINTERLEGT,
										allInfo,
										new Exception(
												"Kein Wechselkurs von "
														+ theClientDto
																.getSMandantenwaehrung()
														+ " auf "
														+ waehrungDruck));
							}
						}
					} else {
						if (belegkurs != null
								&& belegkurs.compareTo(new BigDecimal(1)) != 0) {
							data[i][REPORT_KONTOBLATT_BELEGKURS] = belegkurs;
							data[i][REPORT_KONTOBLATT_BELEGWAEHRUNG] = belegwaehrung;
							if (haben != null)
								habenFW = Helper.rundeKaufmaennisch(
										haben.multiply(belegkurs),
										FinanzFac.NACHKOMMASTELLEN);
							if (soll != null)
								sollFW = Helper.rundeKaufmaennisch(
										soll.multiply(belegkurs),
										FinanzFac.NACHKOMMASTELLEN);
							WechselkursDto wkDto = null;
							try {
								wkDto = getLocaleFac().getKursZuDatum(
										theClientDto.getSMandantenwaehrung(),
										(waehrungDruck == null ? belegwaehrung
												: waehrungDruck),
										(java.sql.Date) buchungDetail
												.getFlrbuchung()
												.getD_buchungsdatum(),
										theClientDto);
							} catch (Throwable t) {
								//
							}

							if (wkDto != null) {
								kurszudatum = wkDto.getNKurs();
							} else {
								ArrayList<Object> allInfo = new ArrayList<Object>();
								allInfo.add(waehrungDruck);
								throw new EJBExceptionLP(
										EJBExceptionLP.FEHLER_KEIN_WECHSELKURS_HINTERLEGT,
										allInfo,
										new Exception(
												"Kein Wechselkurs von "
														+ theClientDto
																.getSMandantenwaehrung()
														+ " auf "
														+ waehrungDruck));
							}
						}
					}
				}
				data[i][REPORT_KONTOBLATT_HABEN] = haben;
				data[i][REPORT_KONTOBLATT_SOLL] = soll;
				data[i][REPORT_KONTOBLATT_HABEN_FW] = habenFW;
				data[i][REPORT_KONTOBLATT_SOLL_FW] = sollFW;
				data[i][REPORT_KONTOBLATT_KURSZUDATUM] = kurszudatum;
				data[i][REPORT_KONTOBLATT_USTWERT] = ust;

				data[i][REPORT_KONTOBLATT_AUTOMATISCHE_EB] = buchungDetail
						.getFlrbuchung().getB_autombuchungeb();
				i++;
			}

			return data;
		}

		protected void addCriteriasForKontos(Criteria crit,
				PrintKontoblaetterModel kbModel) {
			crit.addOrder(Order.asc("c_nr"));

			// if (PrintKontoblaetterModel.EnumSortOrder.SORT_NACH_BELEG ==
			// kbModel
			// .getSortOrder()) {
			// crit.addOrder(Order.asc("c_nr"));
			// }

			if (kbModel.getKontoIId() != null) {
				crit.add(Restrictions.eq("i_id", kbModel.getKontoIId()));
			} else {
				crit.add(Restrictions.eq("kontotyp_c_nr",
						kbModel.getKontotypCNr()));

				if (kbModel.getVonKontoNr() != null) {
					crit.add(Restrictions.ge("c_nr", kbModel.getVonKontoNr()));
				}
				if (kbModel.getBisKontoNr() != null) {
					String ktoNrBis_Gefuellt = Helper.fitString2Length(
							kbModel.getBisKontoNr(), 25, '_');
					crit.add(Restrictions.lt("c_nr", ktoNrBis_Gefuellt));
				}
			}
		}

		protected void addCriteriasForDetails(Criteria crit2,
				PrintKontoblaetterModel kbModel, EnumSortOrder sortOrder,
				FLRFinanzKonto detailKonto) {
			crit2.add(Restrictions.eq("konto_i_id", detailKonto.getI_id()));

			if (kbModel.gettVon() != null) {
				crit2.add(Restrictions.ge("s.d_buchungsdatum",
						Helper.cutTimestamp(kbModel.gettVon())));
			}

			if (kbModel.gettBis() != null) {
				crit2.add(Restrictions.lt("s.d_buchungsdatum",
						Helper.cutTimestamp(kbModel.gettBis())));
			}

			if (kbModel.getGeschaeftsjahr() != null) {
				crit2.add(Restrictions.eq("s.geschaeftsjahr_i_geschaeftsjahr",
						kbModel.getGeschaeftsjahr()));
			}

			crit2.createAlias("flrbuchung", "s");

			crit2.add(Restrictions.isNull("s.t_storniert"));

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

		protected void addParametersForKontos(PrintKontoblaetterModel kbModel,
				TheClientDto theClientDto) {

			reportParameter.put("P_KONTOTYP", kbModel.getKontotypCNr());
			reportParameter.putIfNotNull("P_TVON",
					kbModel.gettVonReport() != null ? kbModel.gettVonReport()
							: kbModel.gettVon());
			reportParameter.putIfNotNull("P_TBIS",
					kbModel.gettBisReport() != null ? kbModel.gettBisReport()
							: kbModel.gettBis());
			reportParameter.putIfNotNull("P_KTOVON", kbModel.getVonKontoNr());
			reportParameter.putIfNotNull("P_KTOBIS", kbModel.getBisKontoNr());
			reportParameter.put("P_DRUCK_IN_MANDANTENWAEHRUNG",
					kbModel.isDruckInMandantenWaehrung());
			reportParameter.put("P_MANDANTENWAEHRUNG",
					theClientDto.getSMandantenwaehrung());
		}

		protected void addParametersForDetails(PrintKontoblaetterModel kbModel,
				EnumSortOrder sortOrder, FLRFinanzKonto detailKonto,
				TheClientDto theClientDto) throws EJBExceptionLP,
				RemoteException {
			reportParameter.put("P_SORTIERUNG",
					kbModel.getSorderOrderParameterString(sortOrder));
			reportParameter.put("P_KONTOTYP", detailKonto.getKontotyp_c_nr());
			reportParameter.put("P_KONTONUMMER", detailKonto.getC_nr());
			reportParameter.put("P_KONTOBEZEICHNUNG", detailKonto.getC_bez());
			reportParameter.put("P_ISTBANKKONTO",
					isBankKonto(detailKonto.getI_id()));
			BigDecimal d = calculateSaldoVonKontoFuerVorPeriode(
					detailKonto.getI_id(), kbModel, vonPeriode,
					detailKonto.getWaehrung_c_nr_druck(), theClientDto);
			reportParameter.put("P_SALDOVORPERIODE", d);
			reportParameter.put("P_PERIODE", vonPeriode);
			if (kbModel.isDruckInMandantenWaehrung()
					|| detailKonto.getWaehrung_c_nr_druck() == null)
				reportParameter.put(LPReport.P_WAEHRUNG,
						theClientDto.getSMandantenwaehrung());
			else
				reportParameter.put(LPReport.P_WAEHRUNG,
						detailKonto.getWaehrung_c_nr_druck());
			reportParameter.put("P_EB_GESCHAEFTSJAHR",
					detailKonto.getI_eb_geschaeftsjahr());
			reportParameter.put("P_EB_ANGELEGT", detailKonto.getD_eb_anlegen());

			reportParameter.put("P_DRUCK_SALDOVORPERIODE",
					new Boolean(kbModel.isEnableSaldo()));
		}

		protected void verifyKontoIsConsistentWithEB(
				PrintKontoblaetterModel kbModel, FLRFinanzKonto detailKonto,
				Iterator<?> buchungen, TheClientDto theClientDto)
				throws EJBExceptionLP, RemoteException {

			reportParameter.put("P_KONTO_KONSISTENT", new Boolean(true));

			boolean konsistent = getBuchenFac().isKontoMitEBKonsistent(
					detailKonto.getI_id(), kbModel.getGeschaeftsjahr(),
					theClientDto);
			reportParameter.put("P_KONTO_KONSISTENT", new Boolean(konsistent));
		}

		protected String getReportName() {
			return FinanzReportFac.REPORT_KONTOBLATT;
		}

		protected Boolean isBankKonto(Integer kontoIId) {
			BankverbindungDto bankDto = null;
			try {
				bankDto = getFinanzFac().bankverbindungFindByKontoIIdOhneExc(
						kontoIId);
			} catch (Throwable t) {
				//
			}
			return (bankDto != null);
		}
	}

	public class KassenbuchPrinter extends KontoblaetterPrinter {

		protected void addCriteriasForDetails(Criteria crit2,
				PrintKontoblaetterModel kbModel, EnumSortOrder sortOrder,
				FLRFinanzKonto detailKonto) {
			super.addCriteriasForDetails(crit2, kbModel, sortOrder, detailKonto);

			// Es sollen zuerst die Eingaenge, dann die Ausgangs-Buchungen
			// aufgelistet werden
			// Im Kassenbuch ist Soll Eingang, und Haben Ausgang
			crit2.addOrder(Order.desc("buchungdetailart_c_nr")); // Wie
			// praktisch
			// "HABEN"
			// ist
			// alphabetisch
			// vor
			// "SOLL"
		}

		protected String getReportName() {
			return FinanzReportFac.REPORT_KASSABUCH;
		}
	}

	public JasperPrintLP printKontoblaetter(PrintKontoblaetterModel kbModel,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		this.useCase = UC_KONTOBLATT;

		KontoblaetterPrinter printer = new KontoblaetterPrinter();
		return printer.print(kbModel, theClientDto);
	}

	public JasperPrintLP printKassabuch(PrintKontoblaetterModel kbModel,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		this.useCase = UC_KONTOBLATT;

		KontoblaetterPrinter printer = new KassenbuchPrinter();
		return printer.print(kbModel, theClientDto);
	}

	/**
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 * @deprecated use
	 *             {@link #printKontoblaetter(PrintKontoblaetterModel, TheClientDto)}
	 */
	public JasperPrintLP printKontoblaetter(String kontotypCNr,
			Integer kontoIId, String ktoVon, String ktoBis,
			boolean bSortiertNachDatum, java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, TheClientDto theClientDto,
			boolean bSortiertNachBeleg, String geschaeftsjahr)
			throws EJBExceptionLP, RemoteException {

		PrintKontoblaetterModel kbModel = new PrintKontoblaetterModel(tVon,
				tBis, geschaeftsjahr);
		kbModel.setKontotypCNr(kontotypCNr);
		kbModel.setEnableSaldo(true);
		kbModel.setSortOrder(bSortiertNachDatum, bSortiertNachBeleg);
		kbModel.setDefaultSortOrder(kbModel.getSortOrder());
		kbModel.setVonKontoNr(ktoVon);
		kbModel.setBisKontoNr(ktoBis);
		kbModel.setKontoIId(kontoIId);
		return printKontoblaetter(kbModel, theClientDto);
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

	private void setReportDataFromKonto(Object[] data, FLRFinanzKonto konto,
			TheClientDto theClientDto) {
		data[REPORT_KONTENLISTE_KONTONUMMER] = getFlrkontoCnr(konto);
		data[REPORT_KONTENLISTE_BEZEICHNUNG] = getFlrkontoCbez(konto);
		data[REPORT_KONTENLISTE_USTKONTONUMMER] = getFlrkontoCnr(konto
				.getFlrkontoust());
		data[REPORT_KONTENLISTE_SKONTOKONTONUMMER] = getFlrkontoCnr(konto
				.getFlrkontoskonto());
		try {
			data[REPORT_KONTENLISTE_KONTOART] = getFinanzServiceFac()
					.uebersetzeKontoartOptimal(konto.getKontoart_c_nr(),
							theClientDto.getLocUi(),
							theClientDto.getLocMandant());
		} catch (Exception e1) {
			data[REPORT_KONTENLISTE_KONTOART] = konto.getKontoart_c_nr();
		}
		data[REPORT_KONTENLISTE_STEUERKATEGORIE] = getFlrkontoCbez(konto
				.getFlrsteuerkategorie());

		if (konto.getFlrergebnisgruppe() != null) {

			if (Helper.short2boolean(konto.getFlrergebnisgruppe()
					.getB_bilanzgruppe()) == true) {
				data[REPORT_KONTENLISTE_BILANZKONTO] = konto
						.getFlrergebnisgruppe().getC_bez();
			} else {
				data[REPORT_KONTENLISTE_ERGEBNISGRUPPE] = konto
						.getFlrergebnisgruppe().getC_bez();
			}

		}

		data[REPORT_KONTENLISTE_ERGEBNISGRUPPE] = getFlrkontoCbez(konto
				.getFlrergebnisgruppe());
		if (konto.getFlruvaart() != null) {
			try {
				data[REPORT_KONTENLISTE_UVAART] = getFinanzServiceFac()
						.uebersetzeUvaartOptimal(
								konto.getFlruvaart().getI_id(),
								theClientDto.getLocUi(),
								theClientDto.getLocMandant());
				data[REPORT_KONTENLISTE_UVAART_CNR] = konto.getFlruvaart()
						.getC_nr();
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
		} else {
			data[REPORT_KONTENLISTE_UVAART] = "";
			data[REPORT_KONTENLISTE_UVAART_CNR] = "";
		}

		if (konto.getFinanzamt_i_id() != null) {
			try {
				FinanzamtDto help = getFinanzFac().finanzamtFindByPrimaryKey(
						konto.getFinanzamt_i_id(), theClientDto.getMandant(),
						theClientDto);
				data[REPORT_KONTENLISTE_FINANZAMT] = help.getPartnerDto()
						.getCName1nachnamefirmazeile1(); // getCReferat();
				data[REPORT_KONTENLISTE_FINANZAMT_KURZBEZ] = help
						.getPartnerDto().getCKbez();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else {
			data[REPORT_KONTENLISTE_FINANZAMT] = "";
			data[REPORT_KONTENLISTE_FINANZAMT_KURZBEZ] = "";
		}
		data[REPORT_KONTENLISTE_KOSTENSTELLE] = getFlrkontoCbez(konto
				.getFlrkostenstelle());

		data[REPORT_KONTENLISTE_GUELTIG_BIS] = konto.getD_gueltigbis() == null ? ""
				: konto.getD_gueltigbis().toString();
		data[REPORT_KONTENLISTE_GUELTIG_VON] = konto.getD_gueltigvon() == null ? ""
				: konto.getD_gueltigvon().toString();

		data[REPORT_KONTENLISTE_SORTIERUNG] = konto.getC_sortierung() == null ? ""
				: konto.getC_sortierung();
		data[REPORT_KONTENLISTE_ALLGEMEIN_SICHTBAR] = konto
				.getB_allgemeinsichtbar().toString();
		data[REPORT_KONTENLISTE_MANUELL_BEBUCHBAR] = konto
				.getB_manuellbebuchbar().toString();
		data[REPORT_KONTENLISTE_VERSTECKT] = konto.getB_versteckt().toString();
		data[REPORT_KONTENLISTE_EROEFFNUNGSBUCHUNG] = konto
				.getB_automeroeffnungsbuchung().toString();

	}

	/**
	 * Liste aller Konten drucken
	 * 
	 * @param theClientDto
	 *            String
	 * @param kontotypCNr
	 *            String
	 * @throws EJBExceptionLP
	 * @return JasperPrint
	 */
	public JasperPrintLP printAlleKonten(TheClientDto theClientDto,
			String kontotypCNr, boolean bMitVersteckten) throws EJBExceptionLP {
		Session session = null;
		try {
			this.useCase = UC_KONTENLISTE;
			this.index = -1;
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRFinanzKonto.class);
			// Filter nach Kontotyp
			c.add(Restrictions.eq(FinanzFac.FLR_KONTO_KONTOTYP_C_NR,
					kontotypCNr));
			// Filter nach Mandant
			c.add(Restrictions.eq(FinanzFac.FLR_KONTO_MANDANT_C_NR,
					theClientDto.getMandant()));

			if (bMitVersteckten == false) {
				c.add(Restrictions.eq(FinanzFac.FLR_KONTO_B_VERSTECKT,
						Helper.boolean2Short(false)));
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
			initJRDS(mapParameter, FinanzReportFac.REPORT_MODUL,
					FinanzReportFac.REPORT_ALLE_KONTEN,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);
			return getReportPrint();
		} finally {
			closeSession(session);
		}
	}

	/**
	 * Alle Rechnungen eines Mandanten im Zeitraum drucken
	 * 
	 * @param theClientDto
	 *            String
	 * @param kontoIId
	 *            Integer
	 * @return JasperPrint
	 * @throws EJBExceptionLP
	 */
	public JasperPrintLP printBuchungenAufKonto(TheClientDto theClientDto,
			Integer kontoIId) throws EJBExceptionLP {
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
			KontoDtoSmall kontoDto = getFinanzFac().kontoFindByPrimaryKeySmall(
					kontoIId);
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLRFinanzBuchungDetail b = (FLRFinanzBuchungDetail) iter.next();
				data[i][REPORT_BUCHUNGEN_AUF_KONTO_AUSZUG] = b.getI_auszug();
				data[i][REPORT_BUCHUNGEN_AUF_KONTO_BUCHUNGSART] = b
						.getFlrbuchung().getBuchungsart_c_nr();
				data[i][REPORT_BUCHUNGEN_AUF_KONTO_BUCHUNGSDATUM] = b
						.getFlrbuchung().getD_buchungsdatum();
				data[i][REPORT_BUCHUNGEN_AUF_KONTO_BETRAG] = b.getN_betrag();
				data[i][REPORT_BUCHUNGEN_AUF_KONTO_TEXT] = b.getFlrbuchung()
						.getC_text();
				data[i][REPORT_BUCHUNGEN_AUF_KONTO_UST] = b.getN_ust();
				i++;
			}
			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);
			mapParameter.put(LPReport.P_WAEHRUNG, mandantDto.getWaehrungCNr());
			mapParameter.put("P_KONTONUMMER", kontoDto.getCNr());
			mapParameter.put("P_KONTOBEZEICHNUNG", kontoDto.getCBez());
			initJRDS(mapParameter, FinanzReportFac.REPORT_MODUL,
					FinanzReportFac.REPORT_BUCHUNGEN_AUF_KONTO,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);
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
	 * @param theClientDto
	 *            String
	 * @param buchungsjournalIId
	 *            Integer
	 * @return JasperPrint
	 * @throws EJBExceptionLP
	 */
	public JasperPrintLP printBuchungenInBuchungsjournal(
			TheClientDto theClientDto, Integer buchungsjournalIId)
			throws EJBExceptionLP {
		Session session = null;
		try {
			this.useCase = UC_BUCHUNGEN_IN_BUCHUNGSJOURNAL;
			this.index = -1;
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			/*
			 * Criteria c =
			 * session.createCriteria(FLRFinanzBuchungsjournal.class);
			 * 
			 * List<?> list = c.list(); data = new Object[list.size()][7]; int i
			 * = 0; for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
			 * FLRFinanzBuchungsjournal b = (FLRFinanzBuchungsjournal) iter
			 * .next();
			 * 
			 * i++; }
			 */
			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);
			mapParameter.put(LPReport.P_WAEHRUNG, mandantDto.getWaehrungCNr());
			initJRDS(mapParameter, FinanzReportFac.REPORT_MODUL,
					FinanzReportFac.REPORT_BUCHUNGEN_IN_BUCHUNGSJOURNAL,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);
			return getReportPrint();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		} finally {
			closeSession(session);
		}
	}

	public JasperPrintLP printSteuerkategorien(TheClientDto theClientDto) {
		this.useCase = UC_STEUERKATEGORIEN;
		HashMap mapParameter = new HashMap();

		Session sessionFinanzamt = FLRSessionFactory.getFactory().openSession();
		String queryStringFinanzamt = "SELECT fa FROM FLRFinanzFinanzamt as fa WHERE fa.mandant_c_nr='"
				+ theClientDto.getMandant()
				+ "' ORDER BY fa.flrpartner.c_name1nachnamefirmazeile1 ASC ";

		Query queryFinanzamt = sessionFinanzamt
				.createQuery(queryStringFinanzamt);

		List<?> resultsFinanzamt = queryFinanzamt.list();
		Iterator<?> resultListIteratorFinanzamt = resultsFinanzamt.iterator();

		ArrayList<Object> alDaten = new ArrayList<Object>();

		while (resultListIteratorFinanzamt.hasNext()) {

			FLRFinanzFinanzamt fa = (FLRFinanzFinanzamt) resultListIteratorFinanzamt
					.next();
			FinanzamtDto faDto = null;
			try {
				faDto = getFinanzFac().finanzamtFindByPrimaryKey(fa.getI_id(),
						fa.getMandant_c_nr(), theClientDto);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			Session session = FLRSessionFactory.getFactory().openSession();

			String queryString = "SELECT sk FROM FLRSteuerkategoriekonto as sk WHERE sk.flrsteuerkategorie.finanzamt_i_id="
					+ fa.getI_id()
					+ " ORDER BY sk.flrsteuerkategorie.i_sort ASC";

			Query query = session.createQuery(queryString);

			List<?> results = query.list();
			Iterator<?> resultListIterator = results.iterator();

			while (resultListIterator.hasNext()) {
				FLRSteuerkategoriekonto sk = (FLRSteuerkategoriekonto) resultListIterator
						.next();

				Object[] oZeile = new Object[REPORT_STEUERKATEGORIE_ANZAHL_FELDER];

				oZeile[REPORT_STEUERKATEGORIE_FINANZAMT] = HelperServer
						.formatNameAusFLRPartner(fa.getFlrpartner());
				oZeile[REPORT_STEUERKATEGORIE_STEUERKATEGORIE] = sk
						.getFlrsteuerkategorie().getC_nr();
				oZeile[REPORT_STEUERKATEGORIE_STEUERKATEGORIEBEZEICHNUNG] = sk
						.getFlrsteuerkategorie().getC_bez();

				oZeile[REPORT_STEUERKATEGORIE_MWST_BEZEICHNUNG] = sk
						.getFlrmwstsatzbez().getC_bezeichnung();

				if (sk.getKontoiidek() != null) {
					oZeile[REPORT_STEUERKATEGORIE_KONTO_EK] = sk
							.getKontoiidek().getC_nr();
					oZeile[REPORT_STEUERKATEGORIE_KONTO_EK_BEZEICHNUNG] = sk
							.getKontoiidek().getC_bez();
				}
				if (sk.getKontoiidvk() != null) {
					oZeile[REPORT_STEUERKATEGORIE_KONTO_VK] = sk
							.getKontoiidvk().getC_nr();
					oZeile[REPORT_STEUERKATEGORIE_KONTO_VK_BEZEICHNUNG] = sk
							.getKontoiidvk().getC_bez();
				}
				if (sk.getKontoiidskontoek() != null) {
					oZeile[REPORT_STEUERKATEGORIE_KONTO_SKONTO_EK] = sk
							.getKontoiidskontoek().getC_nr();
					oZeile[REPORT_STEUERKATEGORIE_KONTO_SKONTO_EK_BEZEICHNUNG] = sk
							.getKontoiidskontoek().getC_bez();
				}
				if (sk.getKontoiidskontovk() != null) {
					oZeile[REPORT_STEUERKATEGORIE_KONTO_SKONTO_VK] = sk
							.getKontoiidskontovk().getC_nr();
					oZeile[REPORT_STEUERKATEGORIE_KONTO_SKONTO_VK_BEZEICHNUNG] = sk
							.getKontoiidskontovk().getC_bez();
				}
				if (sk.getKontoiideinfuhrust() != null) {
					oZeile[REPORT_STEUERKATEGORIE_KONTO_EINFUHRUST] = sk
							.getKontoiideinfuhrust().getC_nr();
					oZeile[REPORT_STEUERKATEGORIE_KONTO_EINFUHRUST_BEZEICHNUNG] = sk
							.getKontoiideinfuhrust().getC_bez();
				}

				try {
					if (faDto.getKontoIIdEbdebitoren() != null) {
						KontoDto kDto = getFinanzFac().kontoFindByPrimaryKey(
								faDto.getKontoIIdEbdebitoren());
						oZeile[REPORT_STEUERKATEGORIE_PARAMETER_DEBITOREN] = kDto
								.getCNr();
						oZeile[REPORT_STEUERKATEGORIE_PARAMETER_DEBITOREN_BEZEICHNUNG] = kDto
								.getCBez();

					}
					if (faDto.getKontoIIdAnzahlungErhaltBezahlt() != null) {
						KontoDto kDto = getFinanzFac().kontoFindByPrimaryKey(
								faDto.getKontoIIdAnzahlungErhaltBezahlt());
						oZeile[REPORT_STEUERKATEGORIE_PARAMETER_ERHALTEN] = kDto
								.getCNr();
						oZeile[REPORT_STEUERKATEGORIE_PARAMETER_ERHALTEN_BEZEICHNUNG] = kDto
								.getCBez();

					}
					if (faDto.getKontoIIdAnzahlungGegebenBezahlt() != null) {
						KontoDto kDto = getFinanzFac().kontoFindByPrimaryKey(
								faDto.getKontoIIdAnzahlungGegebenBezahlt());
						oZeile[REPORT_STEUERKATEGORIE_PARAMETER_GELEISTET] = kDto
								.getCNr();
						oZeile[REPORT_STEUERKATEGORIE_PARAMETER_GELEISTET_BEZEICHNUNG] = kDto
								.getCBez();

					}
					if (faDto.getKontoIIdEbkreditoren() != null) {
						KontoDto kDto = getFinanzFac().kontoFindByPrimaryKey(
								faDto.getKontoIIdEbkreditoren());
						oZeile[REPORT_STEUERKATEGORIE_PARAMETER_KREDITOREN] = kDto
								.getCNr();
						oZeile[REPORT_STEUERKATEGORIE_PARAMETER_KREDITOREN_BEZEICHNUNG] = kDto
								.getCBez();

					}
					if (faDto.getKontoIIdEbsachkonten() != null) {
						KontoDto kDto = getFinanzFac().kontoFindByPrimaryKey(
								faDto.getKontoIIdEbsachkonten());
						oZeile[REPORT_STEUERKATEGORIE_PARAMETER_SACHKONTO] = kDto
								.getCNr();
						oZeile[REPORT_STEUERKATEGORIE_PARAMETER_SACHKONTO_BEZEICHNUNG] = kDto
								.getCBez();

					}
					if (faDto.getKontoIIdAnzahlungErhaltVerr() != null) {
						KontoDto kDto = getFinanzFac().kontoFindByPrimaryKey(
								faDto.getKontoIIdAnzahlungErhaltVerr());
						oZeile[REPORT_STEUERKATEGORIE_PARAMETER_VERR_ERHALTEN] = kDto
								.getCNr();
						oZeile[REPORT_STEUERKATEGORIE_PARAMETER_VERR_ERHALTEN_BEZEICHNUNG] = kDto
								.getCBez();

					}
					if (faDto.getKontoIIdAnzahlungGegebenVerr() != null) {
						KontoDto kDto = getFinanzFac().kontoFindByPrimaryKey(
								faDto.getKontoIIdAnzahlungGegebenVerr());
						oZeile[REPORT_STEUERKATEGORIE_PARAMETER_VERR_GELEISTET] = kDto
								.getCNr();
						oZeile[REPORT_STEUERKATEGORIE_PARAMETER_VERR_GELEISTET_BEZEICHNUNG] = kDto
								.getCBez();

					}

					SteuerkategorieDto skDto = getFinanzServiceFac()
							.steuerkategorieFindByPrimaryKey(
									sk.getFlrsteuerkategorie().getI_id(),
									theClientDto);

					oZeile[REPORT_STEUERKATEGORIE_REVERSECHARGE] = Helper
							.short2Boolean(skDto.getBReversecharge());

					if (skDto.getKontoIIdForderungen() != null) {
						KontoDto kDto = getFinanzFac().kontoFindByPrimaryKey(
								skDto.getKontoIIdForderungen());
						oZeile[REPORT_STEUERKATEGORIE_KONTO_FORDERUNGEN] = kDto
								.getCNr();
						oZeile[REPORT_STEUERKATEGORIE_KONTO_FORDERUNGEN_BEZEICHNUNG] = kDto
								.getCBez();
					}
					if (skDto.getKontoIIdKursgewinn() != null) {
						KontoDto kDto = getFinanzFac().kontoFindByPrimaryKey(
								skDto.getKontoIIdKursgewinn());
						oZeile[REPORT_STEUERKATEGORIE_KONTO_KURSGEWINNE] = kDto
								.getCNr();
						oZeile[REPORT_STEUERKATEGORIE_KONTO_KURSGEWINNE_BEZEICHNUNG] = kDto
								.getCBez();
					}
					if (skDto.getKontoIIdKursverlust() != null) {
						KontoDto kDto = getFinanzFac().kontoFindByPrimaryKey(
								skDto.getKontoIIdKursverlust());
						oZeile[REPORT_STEUERKATEGORIE_KONTO_KURSVERLUSTE] = kDto
								.getCNr();
						oZeile[REPORT_STEUERKATEGORIE_KONTO_KURSVERLUSTE_BEZEICHNUNG] = kDto
								.getCBez();
					}
					if (skDto.getKontoIIdVerbindlichkeiten() != null) {
						KontoDto kDto = getFinanzFac().kontoFindByPrimaryKey(
								skDto.getKontoIIdVerbindlichkeiten());
						oZeile[REPORT_STEUERKATEGORIE_KONTO_VERBINDLICHKEITEN] = kDto
								.getCNr();
						oZeile[REPORT_STEUERKATEGORIE_KONTO_VERBINDLICHKEITEN_BEZEICHNUNG] = kDto
								.getCBez();
					}
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

		initJRDS(mapParameter, FinanzReportFac.REPORT_MODUL,
				FinanzReportFac.REPORT_STEUERKATEGORIEN,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);
		return getReportPrint();

	}

	/**
	 * Buchungsjournal drucken.
	 * 
	 * @param theClientDto
	 *            String
	 * @param buchungsjournalIId
	 *            Integer
	 * @return JasperPrint
	 * @throws EJBExceptionLP
	 */
	public JasperPrintLP printBuchungsjournal(TheClientDto theClientDto,
			Integer buchungsjournalIId, Date dVon, Date dBis,
			boolean storniert, boolean bDatumsfilterIstBuchungsdatum,
			String text, String belegnummer, BigDecimal betrag)
			throws EJBExceptionLP {
		Session session = null;
		try {
			this.useCase = UC_BUCHUNGSJOURNAL;
			this.index = -1;

			Map<String, Object> mapParameter = new TreeMap<String, Object>();

			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRFinanzBuchungDetail.class);

			Timestamp help = new Timestamp(dVon.getTime());

			c.createAlias("flrbuchung", "s");

			String datumsfilter = "s.d_buchungsdatum";
			if (!bDatumsfilterIstBuchungsdatum) {
				datumsfilter = "t_anlegen";
			}
			c.add(Restrictions.ge(datumsfilter, Helper.cutTimestamp(help)));

			if (text != null) {
				c.add(Restrictions.ilike("s." + FinanzFac.FLR_BUCHUNG_C_TEXT,
						"%" + text + "%"));
				mapParameter.put("P_TEXT", text);

			}

			if (belegnummer != null) {

				String trennzeichen = getParameterFac().getMandantparameter(
						theClientDto.getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN,
						ParameterFac.PARAMETER_BELEGNUMMERNFORMAT_TRENNZEICHEN)
						.getCWert();
				Integer stellenBelegnummer = new Integer(
						getParameterFac()
								.getMandantparameter(
										theClientDto.getMandant(),
										ParameterFac.KATEGORIE_ALLGEMEIN,
										ParameterFac.PARAMETER_BELEGNUMMERNFORMAT_STELLEN_BELEGNUMMER)
								.getCWert());

				String sValue = belegnummer;
				sValue = sValue.replaceAll("%", "");

				sValue = Helper.fitString2LengthAlignRight(sValue,
						stellenBelegnummer, '0');

				sValue = "%" + trennzeichen + sValue;

				c.add(Restrictions.like("s."
						+ FinanzFac.FLR_BUCHUNG_C_BELEGNUMMER, sValue));
			}

			if (betrag != null) {

				Integer toleranzBetragsuche = new Integer(getParameterFac()
						.getMandantparameter(theClientDto.getMandant(),
								ParameterFac.KATEGORIE_FINANZ,
								ParameterFac.PARAMETER_TOLERANZ_BETRAGSUCHE)
						.getCWert());

				BigDecimal value1 = betrag
						.subtract(
								new BigDecimal(
										betrag.doubleValue()
												* ((double) toleranzBetragsuche / (double) 100)))
						.abs();
				BigDecimal value2 = betrag
						.add(new BigDecimal(betrag.doubleValue()
								* ((double) toleranzBetragsuche / (double) 100)))
						.abs();

				c.add(Restrictions.or(Restrictions.between(
						FinanzFac.FLR_BUCHUNGDETAIL_N_BETRAG, value1, value2),
						Restrictions.between(
								FinanzFac.FLR_BUCHUNGDETAIL_N_BETRAG,
								value2.negate(), value1.negate())));

				mapParameter
						.put("P_TOLERANZ_BETRAGSSUCHE", toleranzBetragsuche);
				mapParameter.put("P_BETRAG", betrag);

			}

			help = new Timestamp(dBis.getTime() + 24 * 3600000);

			c.add(Restrictions.lt(datumsfilter, Helper.cutTimestamp(help)));

			if (storniert != true) {
				c.add(Restrictions.isNull("s.t_storniert"));
			} else {
				// Skip
			}

			c.createAlias("s.flrkostenstelle", "k");
			c.add(Restrictions.eq("k.mandant_c_nr", theClientDto.getMandant()));

			List<?> list = c.list();
			data = new Object[list.size()][REPORT_BUCHUNGSJOURNAL_ANZAHL_SPALTEN];
			int i = 0;

			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLRFinanzBuchungDetail b = (FLRFinanzBuchungDetail) iter.next();
				data[i][REPORT_BUCHUNGSJOURNAL_AUSZUG] = b.getI_auszug();
				data[i][REPORT_BUCHUNGSJOURNAL_BELEGNUMMER] = b.getFlrbuchung()
						.getC_belegnummer();
				data[i][REPORT_BUCHUNGSJOURNAL_BUCHUNGSART] = b.getFlrbuchung()
						.getBuchungsart_c_nr();
				data[i][REPORT_BUCHUNGSJOURNAL_BUCHUNGSDATUM] = b
						.getFlrbuchung().getD_buchungsdatum();
				if (b.getFlrgegenkonto() != null) {
					data[i][REPORT_BUCHUNGSJOURNAL_GEGENKONTONUMMER] = b
							.getFlrgegenkonto().getC_nr();
				}
				data[i][REPORT_BUCHUNGSJOURNAL_BETRAG] = b.getN_betrag();
				data[i][REPORT_BUCHUNGSJOURNAL_KONTONUMMER] = b.getFlrkonto()
						.getC_nr();
				data[i][REPORT_BUCHUNGSJOURNAL_KOSTENSTELLENUMMER] = b
						.getFlrbuchung().getFlrkostenstelle().getC_nr();
				data[i][REPORT_BUCHUNGSJOURNAL_TEXT] = b.getFlrbuchung()
						.getC_text();
				data[i][REPORT_BUCHUNGSJOURNAL_UST] = b.getN_ust();

				if (b.getFlrbuchung().getT_storniert() == null) {
					data[i][REPORT_BUCHUNGSJOURNAL_STORNIERT] = new Boolean(
							false);
				} else {
					data[i][REPORT_BUCHUNGSJOURNAL_STORNIERT] = new Boolean(
							true);
				}

				if (b.getBuchungdetailart_c_nr().equals("SOLL           ")) {
					data[i][REPORT_BUCHUNGSJOURNAL_SOLL] = b.getN_betrag();
					data[i][REPORT_BUCHUNGSJOURNAL_HABEN] = null;
				} else if (b.getBuchungdetailart_c_nr().equals(
						"HABEN          ")) {
					data[i][REPORT_BUCHUNGSJOURNAL_HABEN] = b.getN_betrag();
					data[i][REPORT_BUCHUNGSJOURNAL_SOLL] = null;
				} else {
					data[i][REPORT_BUCHUNGSJOURNAL_SOLL] = null;
					data[i][REPORT_BUCHUNGSJOURNAL_HABEN] = null;
				}

				data[i][REPORT_BUCHUNGSJOURNAL_BUCHUNGSJOURNALDATUM] = b
						.getT_anlegen();

				data[i][REPORT_BUCHUNGSJOURNAL_WER] = getPersonalFac()
						.personalFindByPrimaryKeySmall(
								b.getFlrbuchung().getPersonal_i_id_anlegen())
						.getCKurzzeichen();

				i++;
			}
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);

			mapParameter.put(LPReport.P_WAEHRUNG, mandantDto.getWaehrungCNr());
			mapParameter.put("P_VON", dVon);
			mapParameter.put("P_MITSTORNIERTEN", new Boolean(storniert));
			mapParameter.put("P_DATUMSFILTER_IST_BUCHUNGSDATUM", new Boolean(
					bDatumsfilterIstBuchungsdatum));
			mapParameter.put("P_BIS", dBis);
			initJRDS(mapParameter, FinanzReportFac.REPORT_MODUL,
					FinanzReportFac.REPORT_BUCHUNGSJOURNAL,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);
			return getReportPrint();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		} finally {
			closeSession(session);
		}
	}

	public JasperPrintLP printRASchreiben(TheClientDto theClientDto,
			Integer mahnungIId) throws EJBExceptionLP {
		try {
			this.useCase = UC_RA_SCHREIBEN;
			index = -1;
			MahnungDto mahnungDto = getMahnwesenFac().mahnungFindByPrimaryKey(
					mahnungIId);
			return printRASchreibenFuerRechnung(theClientDto,
					mahnungDto.getRechnungIId());
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	public JasperPrintLP printRASchreibenFuerRechnung(
			TheClientDto theClientDto, Integer rechnungIId)
			throws EJBExceptionLP {
		try {
			this.useCase = UC_RA_SCHREIBEN;
			index = -1;
			// zzt. nur fuer eine rechnung
			data = new Object[1][REPORT_RA_SCHREIBEN_ANZAHL_KRITERIEN];
			RechnungDto rechnungDto = getRechnungFac()
					.rechnungFindByPrimaryKey(rechnungIId);
			data[0][REPORT_RA_SCHREIBEN_RECHNUNGSNUMMER] = rechnungDto.getCNr();
			data[0][REPORT_RA_SCHREIBEN_RECHNUNGSDATUM] = rechnungDto
					.getTBelegdatum();

			ZahlungszielDto zahlungszielDto = getMandantFac()
					.zahlungszielFindByPrimaryKey(
							rechnungDto.getZahlungszielIId(), theClientDto);
			data[0][REPORT_RA_SCHREIBEN_ZIELDATUM] = Helper.addiereTageZuDatum(
					rechnungDto.getTBelegdatum(), zahlungszielDto
							.getAnzahlZieltageFuerNetto().intValue());

			MahnstufeDto mahnstufeDto = getMahnwesenFac()
					.mahnstufeFindByPrimaryKey(
							new MahnstufePK(FinanzServiceFac.MAHNSTUFE_99,
									theClientDto.getMandant()));
			Double dZinssatz = new Double(0);
			if (mahnstufeDto.getFZinssatz() != null) {
				dZinssatz = mahnstufeDto.getFZinssatz().doubleValue();
			}

			if (!rechnungDto.getRechnungartCNr().equals(
					RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
				data[0][REPORT_RA_SCHREIBEN_BETRAG] = rechnungDto.getNWert();
				data[0][REPORT_RA_SCHREIBEN_ZINSSATZ] = dZinssatz;

				BigDecimal bdZinsenFuerEinJahr = rechnungDto.getNWert()
						.multiply(new BigDecimal(dZinssatz).movePointLeft(2));
				BigDecimal bdDays = new BigDecimal(Helper.getDifferenzInTagen(
						Helper.addiereTageZuDatum(rechnungDto.getTBelegdatum(),
								zahlungszielDto.getAnzahlZieltageFuerNetto()
										.intValue()),
						new java.util.Date(System.currentTimeMillis())));
				BigDecimal bdZinsenFuerEinenTag = bdZinsenFuerEinJahr.divide(
						new BigDecimal(360.0), 4, BigDecimal.ROUND_HALF_EVEN);
				BigDecimal bdZinsen = bdZinsenFuerEinenTag.multiply(bdDays);
				bdZinsen = Helper.rundeKaufmaennisch(bdZinsen,
						FinanzFac.NACHKOMMASTELLEN);
				data[0][REPORT_RA_SCHREIBEN_ZINSEN] = bdZinsen;

				data[0][REPORT_RA_SCHREIBEN_MAHNSPESEN] = new BigDecimal(0);

				BigDecimal bdSpesenFw = getMahnspesen(mahnstufeDto,
						Helper.addiereTageZuDatum(rechnungDto.getTBelegdatum(),
								zahlungszielDto.getAnzahlZieltageFuerNetto()
										.intValue()), theClientDto,
						rechnungDto.getWaehrungCNr());

				if (bdSpesenFw != null) {
					data[0][REPORT_RA_SCHREIBEN_MAHNSPESEN] = bdSpesenFw;
				}
			} else {
				data[0][REPORT_RA_SCHREIBEN_BETRAG] = rechnungDto.getNWert()
						.negate();
				data[0][REPORT_RA_SCHREIBEN_ZINSSATZ] = new Double(0);
				data[0][REPORT_RA_SCHREIBEN_MAHNSPESEN] = new BigDecimal(0);
				data[0][REPORT_RA_SCHREIBEN_ZINSEN] = new BigDecimal(0);
			}

			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			PersonalDto oPersonalBenutzer = getPersonalFac()
					.personalFindByPrimaryKey(theClientDto.getIDPersonal(),
							theClientDto);
			PersonalDto oPersonalAnleger = getPersonalFac()
					.personalFindByPrimaryKey(
							rechnungDto.getPersonalIIdAnlegen(), theClientDto);
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);
			mapParameter.put(LPReport.P_MANDANTADRESSE,
					Helper.formatMandantAdresse(mandantDto));
			mapParameter.put("P_UNSER_ZEICHEN", Helper.getKurzzeichenkombi(
					oPersonalBenutzer.getCKurzzeichen(),
					oPersonalAnleger.getCKurzzeichen()));
			mapParameter.put("P_DATUM",
					new java.util.Date(System.currentTimeMillis()));
			mapParameter.put(LPReport.P_WAEHRUNG, mandantDto.getWaehrungCNr());

			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
					rechnungDto.getKundeIId(), theClientDto);
			Locale locDruck = Helper.string2Locale(kundeDto.getPartnerDto()
					.getLocaleCNrKommunikation());

			AnsprechpartnerDto ansprechpartnerDto = null;
			if (rechnungDto.getAnsprechpartnerIId() != null) {
				ansprechpartnerDto = getAnsprechpartnerFac()
						.ansprechpartnerFindByPrimaryKey(
								rechnungDto.getAnsprechpartnerIId(),
								theClientDto);
			}
			mapParameter.put(
					"P_SCHULDNER_ANREDE",
					formatAdresseFuerAusdruck(kundeDto.getPartnerDto(),
							ansprechpartnerDto, mandantDto, locDruck));
			String sTelefon = kundeDto.getPartnerDto().getCTelefon();
			String sFax = kundeDto.getPartnerDto().getCFax();

			mapParameter.put("P_SCHULDNER_TELEFON", sTelefon);
			mapParameter.put("P_SCHULDNER_FAX", sFax);

			mapParameter.put("P_ABSENDER", mandantDto.getPartnerDto()
					.formatAnrede());
			mapParameter.put("P_BENUTZERNAME", oPersonalBenutzer
					.getPartnerDto().formatAnrede());

			MahntextDto mahntextDto = getFinanzServiceFac()
					.mahntextFindByMandantLocaleCNr(mandantDto.getCNr(),
							Helper.locale2String(locDruck),
							new Integer(FinanzServiceFac.MAHNSTUFE_99));
			if (mahntextDto != null) {
				mapParameter.put("P_MAHNTEXT",
						Helper.formatStyledTextForJasper(mahntextDto
								.getCTextinhalt()));
			} else {
				EJBExceptionLP e = new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_KEINE_MAHNTEXTE_EINGETRAGEN,
						"");
				ArrayList<Object> a = new ArrayList<Object>();
				a.add(mandantDto.getCNr() + "|"
						+ Helper.locale2String(locDruck) + "|"
						+ new Integer(FinanzServiceFac.MAHNSTUFE_99));
				e.setAlInfoForTheClient(a);
				throw e;
			}
			initJRDS(mapParameter, FinanzReportFac.REPORT_MODUL,
					FinanzReportFac.REPORT_RA_SCHREIBEN,
					theClientDto.getMandant(), locDruck, theClientDto);
			return getReportPrint();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	public JasperPrintLP printMahnungAusMahnlauf(TheClientDto theClientDto,
			Integer mahnungIId, Boolean bMitLogo) throws EJBExceptionLP {
		try {
			this.useCase = UC_MAHNUNG;
			data = new Object[1][1];
			index = -1;
			MahnungDto mahnungDto = getMahnwesenFac().mahnungFindByPrimaryKey(
					mahnungIId);
			return printMahnungFuerRechnung(theClientDto,
					mahnungDto.getRechnungIId(), mahnungDto.getMahnstufeIId(),
					bMitLogo);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	public BigDecimal getMahnspesen(MahnstufeDto mahnstufeDto,
			java.sql.Date tMahndatum, TheClientDto theClientDto,
			String waehrungCNr) {

		if (!theClientDto.getSMandantenwaehrung().equals(waehrungCNr)) {
			try {
				javax.persistence.Query query = em
						.createNamedQuery("MahnspesenFindByIMahnstufeMandantCNrWaehrungCNr");
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
				bdSpesenFw = getLocaleFac().rechneUmInAndereWaehrungZuDatum(
						mahnstufeDto.getNMahnspesen(),
						theClientDto.getSMandantenwaehrung(), waehrungCNr,
						tMahndatum, theClientDto);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			return bdSpesenFw;

		} else {
			return new BigDecimal(0);
		}

	}

	public Map<String, Object> getMahnungsParameterFuerRechnung(
			TheClientDto theClientDto, Integer rechnungIId, Integer mahnstufeIId)
			throws EJBExceptionLP, RemoteException {
		MahnungDto mahnungDto = getMahnwesenFac()
				.mahnungFindByRechnungMahnstufe(rechnungIId, mahnstufeIId);

		Map<String, Object> mapParameter = new TreeMap<String, Object>();
		RechnungDto rechnungDto = getRechnungFac().rechnungFindByPrimaryKey(
				rechnungIId);
		// stornierte Rechnungen nicht zulassen
		if (rechnungDto.getStatusCNr().equals(RechnungFac.STATUS_STORNIERT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FINANZ_EXPORT_BELEG_IST_NOCH_NICHT_AKTIVIERT,
					"");
		}
		KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
				rechnungDto.getKundeIId(), theClientDto);
		Locale locDruck = Helper.string2Locale(kundeDto.getPartnerDto()
				.getLocaleCNrKommunikation());
		AnsprechpartnerDto oAnsprechpartner = null;
		if (rechnungDto.getAnsprechpartnerIId() != null) {
			oAnsprechpartner = getAnsprechpartnerFac()
					.ansprechpartnerFindByPrimaryKey(
							rechnungDto.getAnsprechpartnerIId(), theClientDto);
		}

		Integer partnerIIdAnsprechpartner = null;
		if (oAnsprechpartner != null) {
			partnerIIdAnsprechpartner = oAnsprechpartner
					.getPartnerIIdAnsprechpartner();
		}

		String sEmail = getPartnerFac()
				.partnerkommFindRespectPartnerAsStringOhneExec(
						partnerIIdAnsprechpartner, kundeDto.getPartnerDto(),
						PartnerFac.KOMMUNIKATIONSART_EMAIL,
						theClientDto.getMandant(), theClientDto);
		String sFax = getPartnerFac()
				.partnerkommFindRespectPartnerAsStringOhneExec(
						partnerIIdAnsprechpartner, kundeDto.getPartnerDto(),
						PartnerFac.KOMMUNIKATIONSART_FAX,
						theClientDto.getMandant(), theClientDto);
		String sTelefon = getPartnerFac()
				.partnerkommFindRespectPartnerAsStringOhneExec(
						partnerIIdAnsprechpartner, kundeDto.getPartnerDto(),
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

		MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
				theClientDto.getMandant(), theClientDto);
		mapParameter.put(LPReport.P_MANDANTADRESSE,
				Helper.formatMandantAdresse(mandantDto));
		mapParameter.put(
				"P_KUNDE_ADRESSBLOCK",
				formatAdresseFuerAusdruck(kundeDto.getPartnerDto(),
						oAnsprechpartner, mandantDto, locDruck));
		mapParameter.put("P_KUNDE_UID", kundeDto.getPartnerDto().getCUid());
		mapParameter.put("P_KUNDE_HINWEISEXTERN", kundeDto.getSHinweisextern());
		PersonalDto oPersonalBenutzer = getPersonalFac()
				.personalFindByPrimaryKey(theClientDto.getIDPersonal(),
						theClientDto);
		PersonalDto oPersonalAnleger = getPersonalFac()
				.personalFindByPrimaryKey(rechnungDto.getPersonalIIdAnlegen(),
						theClientDto);
		mapParameter.put("P_UNSER_ZEICHEN", Helper.getKurzzeichenkombi(
				oPersonalBenutzer.getCKurzzeichen(),
				oPersonalAnleger.getCKurzzeichen()));
		mapParameter.put("P_DATUM", mahnungDto.getTMahndatum());
		MahntextDto mahntextDto = getFinanzServiceFac()
				.mahntextFindByMandantLocaleCNr(mandantDto.getCNr(),
						Helper.locale2String(locDruck), mahnstufeIId);

		String sPMahnstufe;
		if (mahnstufeIId.intValue() == FinanzServiceFac.MAHNSTUFE_99) {
			sPMahnstufe = getTextRespectUISpr("lp.letzte",
					theClientDto.getMandant(), locDruck);
		} else {
			sPMahnstufe = mahnstufeIId.toString() + ".";
		}

		mapParameter.put("P_MAHNSTUFE", sPMahnstufe);

		if (mahntextDto != null) {
			mapParameter.put("P_MAHNTEXT", Helper
					.formatStyledTextForJasper(mahntextDto.getCTextinhalt()));
		} else {
			EJBExceptionLP e = new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FINANZ_KEINE_MAHNTEXTE_EINGETRAGEN,
					"");
			ArrayList<Object> a = new ArrayList<Object>();
			a.add(mandantDto.getCNr() + "|" + Helper.locale2String(locDruck)
					+ "|" + mahnstufeIId);
			e.setAlInfoForTheClient(a);
			throw e;
		}
		// Vertreter
		if (rechnungDto.getPersonalIIdVertreter() != null) {
			String cVertreterAnredeShort = getPersonalFac()
					.personalFindByPrimaryKey(
							rechnungDto.getPersonalIIdVertreter(), theClientDto)
					.getPartnerDto().formatFixName2Name1();
			mapParameter.put(P_VERTRETER, cVertreterAnredeShort);
		}

		mapParameter.put("P_RECHNUNGSNUMMER", rechnungDto.getCNr());
		mapParameter.put("P_RECHNUNGSDATUM", rechnungDto.getTBelegdatum());
		mapParameter.put("P_WERT",
				rechnungDto.getNWert().add(rechnungDto.getNWertust()));

		BigDecimal bdBetrag = rechnungDto
				.getNWertfw()
				.add(rechnungDto.getNWertustfw())
				.subtract(
						getRechnungFac().getBereitsBezahltWertVonRechnungFw(
								rechnungDto.getIId(), null))
				.subtract(
						getRechnungFac().getBereitsBezahltWertVonRechnungUstFw(
								rechnungDto.getIId(), null));

		// SP839
		BigDecimal bdUstAnzahlungFW = new BigDecimal(0);
		BigDecimal bdNettoAnzahlungFW = new BigDecimal(0);
		if (rechnungDto.getAuftragIId() != null
				&& rechnungDto.getRechnungartCNr().equals(
						RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG)) {
			bdNettoAnzahlungFW = getRechnungFac()
					.getAnzahlungenZuSchlussrechnungFw(rechnungDto.getIId());
			bdUstAnzahlungFW = getRechnungFac()
					.getAnzahlungenZuSchlussrechnungUstFw(rechnungDto.getIId());

			bdBetrag = bdBetrag.subtract(bdUstAnzahlungFW).subtract(
					bdNettoAnzahlungFW);

		}

		if (rechnungDto.getRechnungartCNr().equals(
				RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
			bdBetrag = bdBetrag.negate();
		}

		mapParameter.put("P_BETRAG", bdBetrag);
		BigDecimal bdSumme = bdBetrag;

		MahnstufeDto mahnstufeDto = getMahnwesenFac()
				.mahnstufeFindByPrimaryKey(
						new MahnstufePK(mahnstufeIId, theClientDto.getMandant()));
		// Zieldatum berechnen
		Date dZieldatum = rechnungDto.getTBelegdatum();
		if (rechnungDto.getZahlungszielIId() != null) {
			ZahlungszielDto zzDto = getMandantFac()
					.zahlungszielFindByPrimaryKey(
							rechnungDto.getZahlungszielIId(), theClientDto);
			if (zzDto.getAnzahlZieltageFuerNetto() == null) {
				zzDto.setAnzahlZieltageFuerNetto(new Integer(0));
			}
			dZieldatum = Helper.addiereTageZuDatum(dZieldatum, zzDto
					.getAnzahlZieltageFuerNetto().intValue());
		}

		// Zinsen
		if (mahnstufeDto.getFZinssatz() != null) {
			BigDecimal bdZinssatz = new BigDecimal(mahnstufeDto.getFZinssatz());
			mapParameter.put("P_ZINSSATZ", bdZinssatz);
			BigDecimal bdZinsenFuerEinJahr = bdBetrag.multiply(bdZinssatz
					.movePointLeft(2));
			BigDecimal bdDays = new BigDecimal(Helper.getDifferenzInTagen(
					dZieldatum, mahnungDto.getTMahndatum()));
			BigDecimal bdZinsenFuerEinenTag = bdZinsenFuerEinJahr.divide(
					new BigDecimal(360.0), 4, BigDecimal.ROUND_HALF_EVEN);
			BigDecimal bdZinsen = bdZinsenFuerEinenTag.multiply(bdDays);
			bdZinsen = Helper.rundeKaufmaennisch(bdZinsen,
					FinanzFac.NACHKOMMASTELLEN);
			mapParameter.put("P_ZINSEN", bdZinsen);
			bdSumme = bdSumme.add(bdZinsen);
		}
		// Mahnspesen

		BigDecimal bdSpesenFw = getMahnspesen(mahnstufeDto,
				mahnungDto.getTMahndatum(), theClientDto,
				rechnungDto.getWaehrungCNr());

		if (bdSpesenFw != null) {

			mapParameter.put("P_MAHNSPESEN", bdSpesenFw);
			bdSumme = bdSumme.add(bdSpesenFw);
		}

		// Summe
		mapParameter.put("P_SUMME", bdSumme);
		mapParameter.put("P_ZIELDATUM", dZieldatum);
		// Zahlungsfrist = heute + Tage lt. Mandantenparameter
		ParametermandantDto p = getParameterFac().getMandantparameter(
				theClientDto.getMandant(), ParameterFac.KATEGORIE_FINANZ,
				ParameterFac.PARAMETER_FINANZ_MAHNUNG_ZAHLUNGSFRIST);
		Integer iTage = new Integer(p.getCWert());
		Date dZahldatum = Helper.addiereTageZuDatum(Helper.cutDate(getDate()),
				iTage);
		mapParameter.put("P_ZAHLDATUM", dZahldatum);

		mapParameter.put("P_BERUECKSICHTIGTBIS",
				new java.sql.Date(System.currentTimeMillis())); // heute

		mapParameter.put(LPReport.P_WAEHRUNG, mandantDto.getWaehrungCNr());

		mapParameter.put("P_ABSENDER", mandantDto.getPartnerDto()
				.formatAnrede());
		mapParameter.put("P_BENUTZERNAME", oPersonalBenutzer.getPartnerDto()
				.formatAnrede());
		return mapParameter;
	}

	public JasperPrintLP printMahnungFuerRechnung(TheClientDto theClientDto,
			Integer rechnungIId, Integer mahnstufeIId, Boolean bMitLogo)
			throws EJBExceptionLP {
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
				RechnungDto rechnungDto = getRechnungFac()
						.rechnungFindByPrimaryKey(rechnungIId);
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
						rechnungDto.getKundeIId(), theClientDto);
				Locale locDruck = Helper.string2Locale(kundeDto.getPartnerDto()
						.getLocaleCNrKommunikation());
				Map<String, Object> mapParameter = getMahnungsParameterFuerRechnung(
						theClientDto, rechnungIId, mahnstufeIId);
				initJRDS(mapParameter, FinanzReportFac.REPORT_MODUL,
						FinanzReportFac.REPORT_MAHNUNG,
						theClientDto.getMandant(), locDruck, theClientDto,
						bMitLogo, rechnungDto.getKostenstelleIId());
				return getReportPrint();
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	public JasperPrintLP[] printSammelmahnung(Integer mahnlaufIId,
			Boolean bMitLogo, TheClientDto theClientDto) throws EJBExceptionLP {
		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria crit = session.createCriteria(FLRFinanzMahnung.class);
			crit.add(Restrictions.eq(FinanzFac.FLR_MAHNUNG_MAHNLAUF_I_ID,
					mahnlaufIId));
			crit.add(Restrictions.isNull(FinanzFac.FLR_MAHNUNG_T_GEDRUCKT));
			List<?> list = crit.list();
			HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
			// alle kundenId's in eine hashmap -> quasi distinct
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLRFinanzMahnung item = (FLRFinanzMahnung) iter.next();
				if (!item.getFlrrechnungreport().getStatus_c_nr()
						.equals(RechnungFac.STATUS_STORNIERT)) {
					hm.put(item.getFlrrechnungreport().getFlrkunde().getI_id(),
							item.getFlrrechnungreport().getFlrkunde().getI_id());
				}
			}
			// Reports generieren
			Collection<JasperPrintLP> c = new LinkedList<JasperPrintLP>();
			for (Iterator<Integer> iter = hm.keySet().iterator(); iter
					.hasNext();) {
				Integer kundeIId = (Integer) iter.next();
				JasperPrintLP print = printSammelmahnung(mahnlaufIId, kundeIId,
						bMitLogo, theClientDto);
				// Kunden-ID fuer den Client setzen.
				print.putAdditionalInformation(JasperPrintLP.KEY_KUNDE_I_ID,
						kundeIId);
				c.add(print);
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

	/**
	 * Alle Mahnungen eines Mahnlaufes in einen Report zusammenfassen.
	 * 
	 * @param mahnlaufIId
	 *            Integer
	 * @param kundeIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @return JasperPrint
	 * @throws EJBExceptionLP
	 */
	private JasperPrintLP printSammelmahnung(Integer mahnlaufIId,
			Integer kundeIId, Boolean bMitLogo, TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {
			this.useCase = UC_SAMMELMAHNUNG;
			index = -1;
			MahnungDto[] mahnungen = getMahnwesenFac()
					.mahnungFindByMahnlaufIId(mahnlaufIId);
			String sRechnungen = "";
			Collection<SammelmahnungDto> c = new LinkedList<SammelmahnungDto>();
			Integer iMaxMahnstufe = null;

			// SP1104 Sammelmahnung -> Ansprechpartner Tel/Fax/Email aus der
			// 1.Rechnung verwenden
			Integer partnerIIdAnsprechpartnerErsteRechnung = null;

			for (int i = 0; i < mahnungen.length; i++) {
				RechnungDto rechnungDto = getRechnungFac()
						.rechnungFindByPrimaryKey(mahnungen[i].getRechnungIId());
				// Stornierte werden nicht mitgedruckt.
				if (!rechnungDto.getStatusCNr().equals(
						RechnungFac.STATUS_STORNIERT)) {
					Integer kundeIIdMahnung = rechnungDto.getKundeIId();
					if (kundeIIdMahnung.equals(kundeIId)) {
						if (iMaxMahnstufe == null) {
							iMaxMahnstufe = mahnungen[i].getMahnstufeIId();
						} else {
							if (mahnungen[i].getMahnstufeIId().intValue() > iMaxMahnstufe
									.intValue()) {
								iMaxMahnstufe = mahnungen[i].getMahnstufeIId();
							}
						}
						SammelmahnungDto smDto = new SammelmahnungDto();
						BigDecimal bdOffen = rechnungDto
								.getNWertfw()
								.add(rechnungDto.getNWertustfw())
								.subtract(
										getRechnungFac()
												.getBereitsBezahltWertVonRechnungFw(
														rechnungDto.getIId(),
														null))
								.subtract(
										getRechnungFac()
												.getBereitsBezahltWertVonRechnungUstFw(
														rechnungDto.getIId(),
														null));

						// SP839
						BigDecimal bdUstAnzahlungFW = new BigDecimal(0);
						BigDecimal bdNettoAnzahlungFW = new BigDecimal(0);
						if (rechnungDto.getAuftragIId() != null
								&& rechnungDto.getRechnungartCNr().equals(
										RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG)) {
							bdNettoAnzahlungFW = getRechnungFac()
									.getAnzahlungenZuSchlussrechnungFw(
											rechnungDto.getIId());
							bdUstAnzahlungFW = getRechnungFac()
									.getAnzahlungenZuSchlussrechnungUstFw(
											rechnungDto.getIId());

							bdOffen = bdOffen.subtract(bdUstAnzahlungFW)
									.subtract(bdNettoAnzahlungFW);

						}

						String rechnungtyp = getRechnungServiceFac()
								.rechnungartFindByPrimaryKey(
										rechnungDto.getRechnungartCNr(),
										theClientDto).getRechnungtypCNr();

						if (rechnungtyp
								.equals(RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
							smDto.setBdOffen(bdOffen.negate());
							smDto.setBdWert(rechnungDto.getNWertfw()
									.add(rechnungDto.getNWertustfw()).negate());
							smDto.setBdZinsen(new BigDecimal(0));
						} else {
							smDto.setBdOffen(bdOffen);
							smDto.setBdWert(rechnungDto.getNWertfw().add(
									rechnungDto.getNWertustfw()));
							MahnstufeDto mahnstufeDto = getMahnwesenFac()
									.mahnstufeFindByPrimaryKey(
											new MahnstufePK(mahnungen[i]
													.getMahnstufeIId(),
													theClientDto.getMandant()));

							Date dZieldatum = rechnungDto.getTBelegdatum();
							if (rechnungDto.getZahlungszielIId() != null) {
								ZahlungszielDto zzDto = getMandantFac()
										.zahlungszielFindByPrimaryKey(
												rechnungDto
														.getZahlungszielIId(),
												theClientDto);
								if (zzDto.getAnzahlZieltageFuerNetto() == null) {
									zzDto.setAnzahlZieltageFuerNetto(new Integer(
											0));
								}
								dZieldatum = Helper.addiereTageZuDatum(
										dZieldatum, zzDto
												.getAnzahlZieltageFuerNetto()
												.intValue());
							}

							if (mahnstufeDto.getFZinssatz() != null) {
								BigDecimal bdZinssatz = new BigDecimal(
										mahnstufeDto.getFZinssatz());
								BigDecimal bdZinsenFuerEinJahr = bdOffen
										.multiply(bdZinssatz.movePointLeft(2));
								BigDecimal bdDays = new BigDecimal(
										Helper.getDifferenzInTagen(dZieldatum,
												mahnungen[i].getTMahndatum()));
								BigDecimal bdZinsenFuerEinenTag = bdZinsenFuerEinJahr
										.divide(new BigDecimal(360.0), 4,
												BigDecimal.ROUND_HALF_EVEN);
								BigDecimal bdZinsen = bdZinsenFuerEinenTag
										.multiply(bdDays);
								bdZinsen = Helper.rundeKaufmaennisch(bdZinsen,
										FinanzFac.NACHKOMMASTELLEN);
								smDto.setBdZinsen(bdZinsen);

							} else {
								smDto.setBdZinsen(new BigDecimal(0));
							}
						}

						smDto.setDBelegdatum(rechnungDto.getTBelegdatum());
						Date dZieldatum = rechnungDto.getTBelegdatum();
						if (rechnungDto.getZahlungszielIId() != null) {
							ZahlungszielDto zzDto = getMandantFac()
									.zahlungszielFindByPrimaryKey(
											rechnungDto.getZahlungszielIId(),
											theClientDto);
							if (zzDto.getAnzahlZieltageFuerNetto() == null) {
								EJBExceptionLP ex = new EJBExceptionLP(
										EJBExceptionLP.FEHLER_RECHNUNG_ZAHLUNGSZIEL_KEINE_TAGE,
										"");
								ArrayList<Object> al = new ArrayList<Object>();
								al.add(zzDto.getCBez());
								al.add(rechnungDto.getCNr());
								ex.setAlInfoForTheClient(al);
								throw ex;
							}
							dZieldatum = Helper.addiereTageZuDatum(dZieldatum,
									zzDto.getAnzahlZieltageFuerNetto()
											.intValue());
						}
						smDto.setDFaelligkeitsdatum(dZieldatum);
						smDto.setIMahnstufe(mahnungen[i].getMahnstufeIId());
						smDto.setSRechnungsnummer(rechnungDto
								.getRechnungartCNr().substring(0, 1)
								+ rechnungDto.getCNr());
						c.add(smDto);

						AnsprechpartnerDto oAnsprechpartner = null;
						if (rechnungDto.getAnsprechpartnerIId() != null) {
							oAnsprechpartner = getAnsprechpartnerFac()
									.ansprechpartnerFindByPrimaryKey(
											rechnungDto.getAnsprechpartnerIId(),
											theClientDto);
							partnerIIdAnsprechpartnerErsteRechnung = oAnsprechpartner
									.getPartnerIIdAnsprechpartner();
						}
					}
				}
			}

			data = new Object[c.size()][7];
			int i = 0;
			for (Iterator<SammelmahnungDto> iter = c.iterator(); iter.hasNext();) {
				SammelmahnungDto item = iter.next();
				data[i][REPORT_SAMMELMAHNUNG_OFFEN] = item.getBdOffen();
				data[i][REPORT_SAMMELMAHNUNG_WERT] = item.getBdWert();
				data[i][REPORT_SAMMELMAHNUNG_ZINSEN] = item.getBdZinsen();
				data[i][REPORT_SAMMELMAHNUNG_BELEGDATUM] = item
						.getDBelegdatum();
				data[i][REPORT_SAMMELMAHNUNG_FAELLIGKEITSDATUM] = item
						.getDFaelligkeitsdatum();
				data[i][REPORT_SAMMELMAHNUNG_MAHNSTUFE] = item.getIMahnstufe();
				data[i][REPORT_SAMMELMAHNUNG_RECHNUNGSNUMMER] = item
						.getSRechnungsnummer();
				if (sRechnungen.length() > 0) {
					sRechnungen = sRechnungen + ", "
							+ item.getSRechnungsnummer();
				} else {
					sRechnungen = item.getSRechnungsnummer();
				}
				i++;
			}
			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeIId,
					theClientDto);
			Locale locDruck = Helper.string2Locale(kundeDto.getPartnerDto()
					.getLocaleCNrKommunikation());
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);

			mapParameter.put(LPReport.P_MANDANTADRESSE,
					Helper.formatMandantAdresse(mandantDto));
			mapParameter.put(
					"P_KUNDE_ADRESSBLOCK",
					formatAdresseFuerAusdruck(kundeDto.getPartnerDto(), null,
							mandantDto, locDruck));
			mapParameter.put("P_KUNDE_UID", kundeDto.getPartnerDto().getCUid());
			mapParameter.put("P_KUNDE_HINWEISEXTERN",
					kundeDto.getSHinweisextern());
			PersonalDto oPersonalBenutzer = getPersonalFac()
					.personalFindByPrimaryKey(theClientDto.getIDPersonal(),
							theClientDto);
			mapParameter.put("P_UNSER_ZEICHEN", Helper.getKurzzeichenkombi(
					oPersonalBenutzer.getCKurzzeichen(),
					oPersonalBenutzer.getCKurzzeichen()));
			mapParameter.put("P_DATUM", getDate());

			String sEmail = getPartnerFac()
					.partnerkommFindRespectPartnerAsStringOhneExec(
							partnerIIdAnsprechpartnerErsteRechnung,
							kundeDto.getPartnerDto(),
							PartnerFac.KOMMUNIKATIONSART_EMAIL,
							theClientDto.getMandant(), theClientDto);
			String sFax = getPartnerFac()
					.partnerkommFindRespectPartnerAsStringOhneExec(
							partnerIIdAnsprechpartnerErsteRechnung,
							kundeDto.getPartnerDto(),
							PartnerFac.KOMMUNIKATIONSART_FAX,
							theClientDto.getMandant(), theClientDto);
			String sTelefon = getPartnerFac()
					.partnerkommFindRespectPartnerAsStringOhneExec(
							partnerIIdAnsprechpartnerErsteRechnung,
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

			MahntextDto mahntextDto = getFinanzServiceFac()
					.mahntextFindByMandantLocaleCNr(mandantDto.getCNr(),
							Helper.locale2String(locDruck), iMaxMahnstufe);

			if (mahntextDto != null) {
				mapParameter.put("P_MAHNTEXT",
						Helper.formatStyledTextForJasper(mahntextDto
								.getCTextinhalt()));
			} else {
				EJBExceptionLP e = new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_KEINE_MAHNTEXTE_EINGETRAGEN,
						"");
				ArrayList<Object> a = new ArrayList<Object>();
				a.add(mandantDto.getCNr() + "|"
						+ theClientDto.getLocUiAsString() + "|" + iMaxMahnstufe);
				e.setAlInfoForTheClient(a);
				throw e;
			}

			MahnlaufDto mahnlaufDto = getMahnwesenFac()
					.mahnlaufFindByPrimaryKey(mahnlaufIId);

			MahnstufeDto mahnstufeDto = getMahnwesenFac()
					.mahnstufeFindByPrimaryKey(
							new MahnstufePK(iMaxMahnstufe, theClientDto
									.getMandant()));
			BigDecimal mahnspesen = getMahnspesen(mahnstufeDto,
					new java.sql.Date(mahnlaufDto.getTAnlegen().getTime()),
					theClientDto, mandantDto.getWaehrungCNr());

			if (mahnspesen != null) {
				mapParameter.put("P_MAHNSPESEN", mahnspesen);
			} else {
				mapParameter.put("P_MAHNSPESEN", new BigDecimal(0));
			}

			mapParameter.put("P_BERUECKSICHTIGTBIS",
					new java.sql.Date(System.currentTimeMillis()));
			mapParameter.put(LPReport.P_WAEHRUNG, mandantDto.getWaehrungCNr());
			mapParameter.put("P_ABSENDER", mandantDto.getPartnerDto()
					.formatAnrede());
			mapParameter.put("P_BENUTZERNAME", oPersonalBenutzer
					.getPartnerDto().formatAnrede());
			initJRDS(mapParameter, FinanzReportFac.REPORT_MODUL,
					FinanzReportFac.REPORT_SAMMELMAHNUNG,
					theClientDto.getMandant(), locDruck, theClientDto,
					bMitLogo, kundeDto.getKostenstelleIId());
			JasperPrintLP print = getReportPrint();
			print.putAdditionalInformation(JasperPrintLP.KEY_MAHNSTUFE,
					iMaxMahnstufe);
			print.putAdditionalInformation(JasperPrintLP.KEY_RECHNUNG_C_NR,
					sRechnungen);
			return print;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public JasperPrintLP printSaldenliste(String mandantCNr,
			ReportSaldenlisteKriterienDto krit, TheClientDto theClientDto)
			throws EJBExceptionLP {
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
			c.add(Restrictions.eq(FinanzFac.FLR_KONTO_KONTOTYP_C_NR,
					krit.getKontotypCNr()));

			if (krit.getKontotypCNr().equals(FinanzServiceFac.KONTOTYP_DEBITOR)
					|| krit.getKontotypCNr().equals(
							FinanzServiceFac.KONTOTYP_KREDITOR)) {
				c.createAlias("flrsteuerkategorie", "s");
				c.addOrder(Order.asc("s.c_nr"));
			}

			// Sortierung aufsteigend nach Kontonummer
			c.addOrder(Order.asc(FinanzFac.FLR_KONTO_C_NR));

			List<?> list = c.list();
			data = new Object[list.size()][REPORT_SALDENLISTE_ANZAHL_SPALTEN];
			int i = 0;
			for (Iterator<FLRFinanzKonto> iter = (Iterator<FLRFinanzKonto>) list
					.iterator(); iter.hasNext();) {

				FLRFinanzKonto konto = iter.next();
				data[i][REPORT_SALDENLISTE_KONTONUMMER] = konto.getC_nr();
				data[i][REPORT_SALDENLISTE_KONTOBEZEICHNUNG] = konto.getC_bez();
				if (konto.getFlrergebnisgruppe() != null) {
					if (Helper.short2boolean(konto.getFlrergebnisgruppe()
							.getB_bilanzgruppe()) == true) {
						data[i][REPORT_SALDENLISTE_ERGEBNISGRUPPE] = "B:"
								+ konto.getFlrergebnisgruppe().getC_bez();
					} else {
						data[i][REPORT_SALDENLISTE_ERGEBNISGRUPPE] = "E:"
								+ konto.getFlrergebnisgruppe().getC_bez();
					}
				}

				if (konto.getFlruvaart() != null) {
					data[i][REPORT_SALDENLISTE_UVAART] = konto.getFlruvaart()
							.getC_nr();
				}
				if (konto.getFinanzamt_i_id() != null) {
					data[i][REPORT_SALDENLISTE_FINANZAMT] = getFinanzFac()
							.finanzamtFindByPrimaryKey(
									konto.getFinanzamt_i_id(), mandantCNr,
									theClientDto).getPartnerDto()
							.formatAnrede();
				}

				if (konto.getKontoart_c_nr() == null) {
					List<FLRFinanzKonto> konten = new ArrayList<FLRFinanzKonto>();
					konten.add(konto);

					BigDecimal[] salden = getSammelSalden(krit, konten,
							theClientDto);
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

					BigDecimal[] salden = getSammelSalden(krit, konten,
							theClientDto);
					setDataFromSalden(i, salden);
				}
				if (konto.getFlrsteuerkategorie() != null) {
					data[i][REPORT_SALDENLISTE_STEUERKATEGORIE] = konto
							.getFlrsteuerkategorie().getC_bez();
				}
				data[i][REPORT_SALDENLISTE_KONSISTENT] = new Boolean(
						getBuchenFac().isKontoMitEBKonsistent(konto.getI_id(),
								krit.getIGeschaeftsjahr(), theClientDto));
				i++;
			}
			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			// Mandantenwaehrung
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);
			mapParameter.put(LPReport.P_WAEHRUNG, mandantDto.getWaehrungCNr());

			mapParameter.put("P_PERIODE", new Integer(krit.getIPeriode()));
			mapParameter.put("P_GESCHAEFTSJAHR", krit.getIGeschaeftsjahr());
			Date[] dVonBis = getBuchenFac()
					.getDatumbereichPeriodeGJ(krit.getIGeschaeftsjahr(),
							krit.getIPeriode(), theClientDto);

			mapParameter.put("P_PERIODE_KALENDERDATUM", dVonBis[0]);
			mapParameter.put("P_KONTOTYP", krit.getKontotypCNr());

			initJRDS(mapParameter, FinanzReportFac.REPORT_MODUL,
					FinanzReportFac.REPORT_SALDENLISTE,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);

			JasperPrintLP print = getReportPrint();
			PrintInfoDto values = new PrintInfoDto();
			values.setDocPath(new DocPath(new DocNodeSaldenliste(krit,
					mandantCNr)));
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

	private BigDecimal[] getSammelSalden(ReportSaldenlisteKriterienDto krit,
			List<FLRFinanzKonto> konten, TheClientDto theClientDto)
			throws RemoteException {
		BigDecimal[] salden = { new BigDecimal(0), new BigDecimal(0),
				new BigDecimal(0), new BigDecimal(0), new BigDecimal(0),
				new BigDecimal(0) };
		for (Iterator<FLRFinanzKonto> iter = (Iterator<FLRFinanzKonto>) konten
				.iterator(); iter.hasNext();) {
			FLRFinanzKonto konto = iter.next();
			salden[0] = salden[0].add(getBuchenFac().getHabenVonKonto(
					konto.getI_id(), krit.getIGeschaeftsjahr(),
					krit.getIPeriode(), theClientDto));
			salden[1] = salden[1].add(getBuchenFac().getSollVonKonto(
					konto.getI_id(), krit.getIGeschaeftsjahr(),
					krit.getIPeriode(), theClientDto));
			salden[2] = salden[2].add(getBuchenFac()
					.getSummeEroeffnungKontoIId(konto.getI_id(),
							krit.getIGeschaeftsjahr(), krit.getIPeriode(),
							false, theClientDto));
			salden[3] = salden[3].add(getBuchenFac().getSollVonKonto(
					konto.getI_id(), krit.getIGeschaeftsjahr(),
					krit.getIPeriode(), true, theClientDto));
			salden[4] = salden[4].add(getBuchenFac().getHabenVonKonto(
					konto.getI_id(), krit.getIGeschaeftsjahr(),
					krit.getIPeriode(), true, theClientDto));
			salden[5] = salden[5].add(getBuchenFac()
					.getSummeEroeffnungKontoIId(konto.getI_id(),
							krit.getIGeschaeftsjahr(), krit.getIPeriode(),
							true, theClientDto));
		}
		return salden;
	}

	@SuppressWarnings("unchecked")
	private List<FLRFinanzKonto> getKontenJeArt(String kontoartCNr,
			String mandantCNr) {
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
	private List<FLRFinanzKonto> getKontenJeUvaart(Integer uvaartIId,
			Integer finanzamtIId) {
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
	 * getKontenJeSammelkonto(Integer sammelkontoIId, String mandantCNr) {
	 * Session session = null; SessionFactory factory =
	 * FLRSessionFactory.getFactory(); session = factory.openSession(); Criteria
	 * c = session.createCriteria(FLRFinanzKonto.class); // Filter nach Mandant
	 * c.add(Restrictions.eq(FinanzFac.FLR_KONTO_MANDANT_C_NR, mandantCNr)); //
	 * Filter kontoart //CK: Auskommentiert wg. PJ 17732
	 * //c.add(Restrictions.eq(FinanzFac.FLR_KONTO_I_ID_WEITERFUEHRENDBILANZ, //
	 * sammelkontoIId)); // Sortierung aufsteigend nach Kontonummer
	 * c.addOrder(Order.asc(FinanzFac.FLR_KONTO_C_NR)); List<FLRFinanzKonto>
	 * list = c.list(); return list; }
	 */

	public JasperPrintLP printMahnlauf(ReportJournalKriterienDto krit,
			Integer mahnlaufIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Session session = null;
		try {
			this.useCase = UC_MAHNLAUF;
			this.index = -1;
			// Mahnlauf holen
			MahnlaufDto mahnlaufDto = getMahnwesenFac()
					.mahnlaufFindByPrimaryKey(mahnlaufIId);
			// Hibernate-Abfrage
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRFinanzMahnung.class);
			Criteria crit = c
					.createCriteria(FinanzFac.FLR_MAHNUNG_FLRRECHNUNGREPORT);
			// Filter nach Mandant
			c.add(Restrictions.eq(FinanzFac.FLR_MAHNUNG_MAHNLAUF_I_ID,
					mahnlaufIId));
			// Filter nach einem Kunde
			if (krit.kundeIId != null) {
				crit.add(Restrictions.eq(RechnungFac.FLR_RECHNUNG_KUNDE_I_ID,
						krit.kundeIId));
			}
			// Filter nach einem Vertrter
			if (krit.vertreterIId != null) {
				crit.add(Restrictions.eq(
						RechnungFac.FLR_RECHNUNG_VERTRETER_I_ID,
						krit.vertreterIId));
			}
			// Sortierung nach Kunde
			if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER) {
				crit.createCriteria(RechnungFac.FLR_RECHNUNG_FLRKUNDE)
						.createCriteria(KundeFac.FLR_PARTNER)
						.addOrder(
								Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));
			}
			// Sortierung nach Vertreter
			if (krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_VERTRETER) {
				crit.createCriteria(RechnungFac.FLR_RECHNUNG_FLRVERTRETER)
						.createCriteria(KundeFac.FLR_PARTNER)
						.addOrder(
								Order.asc(PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));
			}
			// Sortierung aufsteigend nach Rechnungsnummer
			crit.addOrder(Order.asc(RechnungFac.FLR_RECHNUNG_C_NR));
			List<?> list = c.list();
			data = new Object[list.size()][20];
			int i = 0;
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLRFinanzMahnung mahnung = (FLRFinanzMahnung) iter.next();
				// Dto holen, da nicht alle Daten da sind
				MahnungDto mahnungDto = getMahnwesenFac()
						.mahnungFindByPrimaryKey(mahnung.getI_id());
				// Array befuellen
				data[i][REPORT_MAHNLAUF_KUNDE] = mahnung.getFlrrechnungreport()
						.getFlrkunde().getFlrpartner()
						.getC_name1nachnamefirmazeile1();
				if (mahnung.getFlrrechnungreport().getFlrvertreter() != null) {
					data[i][REPORT_MAHNLAUF_VERTRETER] = mahnung
							.getFlrrechnungreport().getFlrvertreter()
							.getFlrpartner().getC_name1nachnamefirmazeile1();
				}
				data[i][REPORT_MAHNLAUF_LETZTEMAHNSTUFE] = mahnungDto
						.getMahnstufeIIdLetztemahnstufe();
				data[i][REPORT_MAHNLAUF_MAHNSPERREBIS] = mahnung
						.getFlrrechnungreport().getT_mahnsperrebis();
				data[i][REPORT_MAHNLAUF_LETZTESMAHNDATUM] = mahnungDto
						.getTLetztesmahndatum();
				data[i][REPORT_MAHNLAUF_MAHNSTUFE] = mahnung
						.getMahnstufe_i_id();
				// Wert kontrollieren - jemand koennte die rechnung
				// zurueckgenommen haben
				if (mahnung.getFlrrechnungreport().getN_wert() != null) {
					data[i][REPORT_MAHNLAUF_OFFEN] = mahnung
							.getFlrrechnungreport()
							.getN_wert()
							.subtract(
									getRechnungFac()
											.getBereitsBezahltWertVonRechnung(
													mahnung.getFlrrechnungreport()
															.getI_id(), null));

				}

				if (mahnung.getFlrrechnungreport().getN_wertust() != null) {
					data[i][REPORT_MAHNLAUF_OFFENUST] =

					mahnung.getFlrrechnungreport()
							.getN_wertust()
							.subtract(

									getRechnungFac()
											.getBereitsBezahltWertVonRechnungUst(
													mahnung.getFlrrechnungreport()
															.getI_id(), null));
				}

				data[i][REPORT_MAHNLAUF_RECHNUNGSDATUM] = mahnung
						.getFlrrechnungreport().getD_belegdatum();
				data[i][REPORT_MAHNLAUF_RECHNUNGSNUMMER] = mahnung
						.getFlrrechnungreport().getC_nr();
				data[i][REPORT_MAHNLAUF_RECHNUNGSART] = mahnung
						.getFlrrechnungreport().getFlrrechnungart().getC_nr();
				// Wert in Mandantenwaehrungb
				data[i][REPORT_MAHNLAUF_WERT] = mahnung.getFlrrechnungreport()
						.getN_wert();
				data[i][REPORT_MAHNLAUF_WERTUST] = mahnung
						.getFlrrechnungreport().getN_wertust();

				// Zieldatum berechnen, falls vorhanden
				if (mahnung.getFlrrechnungreport().getZahlungsziel_i_id() != null) {
					Date dZieldatum = getMandantFac()
							.berechneZielDatumFuerBelegdatum(
									mahnung.getFlrrechnungreport()
											.getD_belegdatum(),
									mahnung.getFlrrechnungreport()
											.getZahlungsziel_i_id(),
									theClientDto);
					data[i][REPORT_MAHNLAUF_ZIELDATUM] = dZieldatum;
				}
				// Auftragsdaten (falls die Rechnung auftragsbezogen ist)
				if (mahnung.getFlrrechnungreport().getFlrauftrag() != null) {
					data[i][REPORT_MAHNLAUF_AUFTRAG_BESTELLNUMMER] = mahnung
							.getFlrrechnungreport().getFlrauftrag()
							.getC_bestellnummer();
					data[i][REPORT_MAHNLAUF_AUFTRAG_NUMMER] = mahnung
							.getFlrrechnungreport().getFlrauftrag().getC_nr();
					data[i][REPORT_MAHNLAUF_AUFTRAG_PROJEKT] = mahnung
							.getFlrrechnungreport().getFlrauftrag().getC_bez();
				}
				// Statistikadresse (falls definiert)
				if (mahnung.getFlrrechnungreport().getFlrstatistikadresse() != null) {
					data[i][REPORT_MAHNLAUF_KUNDE_STATISTIKADRESSE] = mahnung
							.getFlrrechnungreport().getFlrstatistikadresse()
							.getFlrpartner().getC_name1nachnamefirmazeile1();
				}
				// die 2 bezeichnungszeilen der ersten rechnungsposition
				RechnungPositionDto[] rePos = getRechnungFac()
						.rechnungPositionFindByRechnungIId(
								mahnung.getFlrrechnungreport().getI_id());
				if (rePos != null && rePos.length > 0) {
					RechnungPositionDto pos = rePos[0];
					// Artikel holen, falls da
					ArtikelDto artikelDto = null;
					if (pos.getArtikelIId() != null) {
						artikelDto = getArtikelFac().artikelFindByPrimaryKey(
								pos.getArtikelIId(), theClientDto);
					}

					String sBezeichnung1;
					String sBezeichnung2;
					// Kein Artikel oder Bezeichnung des Artikels uebersteuert?
					if (artikelDto == null
							|| pos.getBArtikelbezeichnunguebersteuert() == null
							|| Helper.short2boolean(pos
									.getBArtikelbezeichnunguebersteuert())) {
						if (artikelDto == null || pos.getCBez() != null) {
							sBezeichnung1 = pos.getCBez();
						} else {
							sBezeichnung1 = artikelDto.getArtikelsprDto()
									.getCBez();
						}
						// Zusatzbezeichnung
						if (artikelDto == null || pos.getCZusatzbez() != null) {
							sBezeichnung2 = pos.getCZusatzbez();
						} else {
							sBezeichnung2 = artikelDto.getArtikelsprDto()
									.getCZbez();
						}
					}
					// Bezeichnung aus dem Artikel
					else {
						sBezeichnung1 = artikelDto.getArtikelsprDto().getCBez();
						sBezeichnung2 = artikelDto.getArtikelsprDto()
								.getCZbez();
					}
					data[i][REPORT_MAHNLAUF_POSITIONSBEZEICHNUNG1] = sBezeichnung1;
					data[i][REPORT_MAHNLAUF_POSITIONSBEZEICHNUNG2] = sBezeichnung2;
				}
				i++;
			}
			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			// Mandantenwaehrung
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);
			mapParameter.put(LPReport.P_WAEHRUNG, mandantDto.getWaehrungCNr());
			// Datum des Mahnlaufs
			mapParameter.put("P_MAHNDATUM", mahnlaufDto.getTAnlegen());
			mapParameter
					.put(LPReport.P_SORTIERENACHKUNDE,
							new Boolean(
									krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER));
			mapParameter
					.put(LPReport.P_SORTIERENACHVERTRETER,
							new Boolean(
									krit.iSortierung == ReportJournalKriterienDto.KRIT_SORT_NACH_VERTRETER));
			initJRDS(mapParameter, FinanzReportFac.REPORT_MODUL,
					FinanzReportFac.REPORT_MAHNLAUF, theClientDto.getMandant(),
					theClientDto.getLocUi(), theClientDto);
			return getReportPrint();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		} finally {
			closeSession(session);
		}
	}

	public JasperPrintLP printExportlauf(Integer exportlaufIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Session session = null;
		try {
			this.useCase = UC_EXPORTLAUF;
			this.index = -1;
			// Mahnlauf holen
			ExportlaufDto exportlaufDto = getFibuExportFac()
					.exportlaufFindByPrimaryKey(exportlaufIId);
			// Hibernate-Abfrage
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRFinanzExportdaten.class);
			// Filter nach Exportlauf
			c.createCriteria(FinanzFac.FLR_EXPORTDATEN_FLREXPORTLAUF).add(
					Restrictions.eq(FinanzFac.FLR_EXPORTLAUF_I_ID,
							exportlaufIId));
			List<?> list = c.list();
			data = new Object[list.size()][9];
			int i = 0;
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLRFinanzExportdaten exportDaten = (FLRFinanzExportdaten) iter
						.next();
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
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);
			mapParameter.put(LPReport.P_WAEHRUNG, mandantDto.getWaehrungCNr());
			// Datum des Mahnlaufs
			mapParameter.put("P_STICHTAG", exportlaufDto.getTStichtag());
			initJRDS(mapParameter, FinanzReportFac.REPORT_MODUL,
					FinanzReportFac.REPORT_EXPORTLAUF,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);
			return getReportPrint();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		} finally {
			closeSession(session);
		}
	}

	public JasperPrintLP printIntrastatVorschau(String sVerfahren,
			java.sql.Date dVon, java.sql.Date dBis,
			BigDecimal bdTransportkosten, TheClientDto theClientDto)
			throws EJBExceptionLP {
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
				daten = getFibuExportFac().getIntrastatDatenVersand(dVon, dBis,
						bdTransportkosten, theClientDto);
			} else if (sVerfahren
					.equals(FinanzReportFac.INTRASTAT_VERFAHREN_WARENEINGANG)) {
				daten = getFibuExportFac().getIntrastatDatenWareneingang(dVon,
						dBis, bdTransportkosten, theClientDto);
			} else {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
						"Verfahren = " + sVerfahren));
			}
			data = new Object[daten.size()][REPORT_INTRASTAT_ANZAHL_SPALTEN];
			int i = 0;
			for (IntrastatDto iDto : daten) {
				data[i][REPORT_INTRASTAT_BELEGART] = iDto.getBelegart();
				data[i][REPORT_INTRASTAT_BELEGNUMMER] = iDto.getBelegnummer();
				data[i][REPORT_INTRASTAT_BEZEICHNUNG] = iDto.getArtikelDto()
						.formatArtikelbezeichnung();
				data[i][REPORT_INTRASTAT_GEWICHT] = iDto.getGewichtInKg();
				data[i][REPORT_INTRASTAT_IDENT] = iDto.getArtikelDto().getCNr();
				data[i][REPORT_INTRASTAT_MENGE] = iDto.getMenge();
				data[i][REPORT_INTRASTAT_PREIS] = iDto.getEinzelpreis();
				data[i][REPORT_INTRASTAT_STATISTISCHER_WERT] = iDto
						.getStatistischerWert();

				String wvkNummer;
				String wvkBezeichnung;
				if (iDto.getArtikelDto().getCWarenverkehrsnummer() != null) {
					WarenverkehrsnummerDto wvk = iDto
							.getWarenverkehrsnummerDto();
					if (wvk != null) {
						wvkNummer = wvk.getCNr();
						wvkBezeichnung = wvk.getCBez();
					} else {
						wvkNummer = "???   "
								+ iDto.getArtikelDto()
										.getCWarenverkehrsnummer();
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
				i++;
			}

			String sVerfahrenBezeichnung;
			if (sVerfahren.equals(FinanzReportFac.INTRASTAT_VERFAHREN_VERSAND)) {
				sVerfahrenBezeichnung = getTextRespectUISpr(
						"finanz.intrastat.versand", theClientDto.getMandant(),
						theClientDto.getLocUi());
			} else {
				sVerfahrenBezeichnung = getTextRespectUISpr(
						"finanz.intrastat.eingang", theClientDto.getMandant(),
						theClientDto.getLocUi());
			}
			mapParameter.put("P_VERFAHREN", sVerfahrenBezeichnung);
			initJRDS(mapParameter, FinanzReportFac.REPORT_MODUL,
					FinanzReportFac.REPORT_INTRASTAT,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return getReportPrint();
	}

	public JasperPrintLP printUva(String mandantCNr,
			ReportUvaKriterienDto krit, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		try {
			this.useCase = UC_UVA;
			this.index = -1;

			Integer iQuartal = null;
			Integer[] iPerioden = null;
			if (krit.getSAbrechnungszeitraum().equals(
					FinanzFac.UVA_ABRECHNUNGSZEITRAUM_QUARTAL)) {
				// krit.IPeriode entspricht dem Quartal
				iQuartal = krit.getIPeriode();
				iPerioden = HelperServer.getMonateFuerQuartal(iQuartal);
			}

			pruefeSteuerkontenUva(krit, theClientDto);

			// Finanzamtsbuchungen wenn noch keine Verprobung vorhanden
			// und keine Geschaeftsjahrsperre!
			GeschaeftsjahrMandantDto gjm = getSystemFac()
					.geschaeftsjahrFindByPrimaryKeyOhneExc(
							krit.getIGeschaeftsjahr(),
							theClientDto.getMandant());
			if (gjm.getTSperre() == null) {
				UvaverprobungDto uvapDto;
				if (!FinanzFac.UVA_ABRECHNUNGSZEITRAUM_JAHR.equals(krit
						.getSAbrechnungszeitraum())) {
					uvapDto = getFinanzServiceFac()
							.uvaVerprobungFindbyFinanzamtIIdGeschaeftsjahrPeriodeAbrechnungszeitraumMandant(
									krit.getFinanzamtIId(),
									krit.getIGeschaeftsjahr(),
									krit.getIPeriode(),
									UvaverprobungDto
											.getAbrechnungszeitraumFromString(krit
													.getSAbrechnungszeitraum()),
									theClientDto);
					if (uvapDto == null) {
						// jetzt die Finanzamtsbuchungen fuer die entsprechenden
						// Monate
						if (iPerioden != null) {
							for (int i = 0; i < iPerioden.length; i++)
								getFinanzServiceFac()
										.createFinanzamtsbuchungen(
												krit.getIGeschaeftsjahr(),
												iPerioden[i],
												krit.getFinanzamtIId(),
												theClientDto);
						} else {
							getFinanzServiceFac().createFinanzamtsbuchungen(
									krit.getIGeschaeftsjahr(),
									krit.getIPeriode(), krit.getFinanzamtIId(),
									theClientDto);
						}
					}
				}
			}
			UvaartDto[] uvas = getFinanzServiceFac().uvaartFindAll(mandantCNr);
			Map<String, Object> mapParameter = new TreeMap<String, Object>();

			// Betraege werden in Waehrung des Finanzamts (aus Land des
			// Finanzamts) umgerechnet
			FinanzamtDto finanzamtDto = getFinanzFac()
					.finanzamtFindByPartnerIIdMandantCNrOhneExc(
							krit.getFinanzamtIId(), mandantCNr, theClientDto);
			boolean bUmrechnen = false;
			boolean bRunden = false;
			String finanzamtwaehrung = null;
			if (finanzamtDto != null) {
				bRunden = Helper.short2boolean(finanzamtDto.getBUmsatzRunden());
				finanzamtwaehrung = finanzamtDto.getPartnerDto()
						.getLandplzortDto().getLandDto().getWaehrungCNr();
				if (finanzamtwaehrung.compareTo(theClientDto
						.getSMandantenwaehrung()) != 0) {
					bUmrechnen = true;
				}
			}
			// Salden fuer alle UVA Arten
			for (int i = 0; i < uvas.length; i++) {
				if (uvas[i].getCNr().compareTo("Nicht definiert") != 0) {
					List<FLRFinanzKonto> uvaKonten = getKontenJeUvaart(
							uvas[i].getIId(), krit.getFinanzamtIId());
					BigDecimal saldo = new BigDecimal(0);
					BigDecimal saldogerundet = null;
					if (bRunden)
						// nur wenn Runden erlaubt, sonst null!
						saldogerundet = new BigDecimal(0);

					if (krit.getSAbrechnungszeitraum().equals(
							FinanzFac.UVA_ABRECHNUNGSZEITRAUM_MONAT)) {
						saldo = getSammelSaldo(krit, uvaKonten, theClientDto);
						if (bUmrechnen) {
							saldo = umrechnen(saldo, finanzamtwaehrung, krit,
									theClientDto);
						}
						if (bRunden) {
							saldogerundet = Helper.rundeKaufmaennisch(saldo, 0);
						}
					} else if (krit.getSAbrechnungszeitraum().equals(
							FinanzFac.UVA_ABRECHNUNGSZEITRAUM_QUARTAL)) {
						for (int j = 0; j < iPerioden.length; j++) {
							krit.setIPeriode(iPerioden[j]);
							BigDecimal saldoTemp = getSammelSaldo(krit,
									uvaKonten, theClientDto);
							if (bUmrechnen) {
								saldo = saldo.add(umrechnen(saldoTemp,
										finanzamtwaehrung, krit, theClientDto));
								if (bRunden) {
									saldogerundet = saldogerundet
											.add(Helper
													.rundeKaufmaennisch(
															umrechnen(
																	saldoTemp,
																	finanzamtwaehrung,
																	krit,
																	theClientDto),
															0));
								}
							} else {
								saldo = saldo.add(saldoTemp);
								if (bRunden) {
									saldogerundet = saldogerundet.add(Helper
											.rundeKaufmaennisch(saldoTemp, 0));
								}
							}
						}
						krit.setIPeriode(iQuartal); // restore Quartal
					} else if (krit.getSAbrechnungszeitraum().equals(
							FinanzFac.UVA_ABRECHNUNGSZEITRAUM_JAHR)) {
						Integer iPeriode = krit.getIPeriode();
						for (int j = 1; j <= 12; j++) {
							krit.setIPeriode(j);
							BigDecimal saldoTemp = getSammelSaldo(krit,
									uvaKonten, theClientDto);
							if (bUmrechnen) {
								saldo = saldo.add(umrechnen(saldoTemp,
										finanzamtwaehrung, krit, theClientDto));
								if (bRunden) {
									saldogerundet = saldogerundet
											.add(Helper
													.rundeKaufmaennisch(
															umrechnen(
																	saldoTemp,
																	finanzamtwaehrung,
																	krit,
																	theClientDto),
															0));
								}
							} else {
								saldo = saldo.add(saldoTemp);
								if (bRunden) {
									saldogerundet = saldogerundet.add(Helper
											.rundeKaufmaennisch(saldoTemp, 0));
								}
							}
						}
						krit.setIPeriode(iPeriode); // restore Periode
					}
					if (uvas[i].getBInvertiert() == 1) {
						saldo = saldo.negate();
						if (bRunden) {
							saldogerundet = saldogerundet.negate();
						}
					}
					UvaRpt uva = new UvaRpt(uvas[i].getCKennzeichen(), saldo,
							saldogerundet);
					String para = "P_"
							+ uvas[i].getCNr().toUpperCase().replace(" ", "_");
					mapParameter.put(para, uva);
				}
			}

			// alle zugehoerigen Mwstsaetze
			MwstsatzbezDto[] mwstbezs = getMandantFac()
					.mwstsatzbezFindAllByMandantAsDto(mandantCNr, theClientDto);
			// GeschaeftsjahrMandantDto gj = getSystemFac()
			// .geschaeftsjahrFindByPrimaryKey(krit.getIGeschaeftsjahr(),
			// theClientDto.getMandant());
			Date[] dVonBis = null;
			if (krit.getSAbrechnungszeitraum().equals(
					FinanzFac.UVA_ABRECHNUNGSZEITRAUM_QUARTAL))
				dVonBis = getBuchenFac().getDatumbereichPeriodeGJ(
						krit.getIGeschaeftsjahr(),
						1 + (krit.getIPeriode() - 1) * 3, theClientDto);

			else if (krit.getSAbrechnungszeitraum().equals(
					FinanzFac.UVA_ABRECHNUNGSZEITRAUM_JAHR))
				dVonBis = getBuchenFac().getDatumbereichPeriodeGJ(
						krit.getIGeschaeftsjahr(), 12, theClientDto);
			else
				// default ist Monat
				// (krit.getSAbrechnungszeitraum().equals(FinanzFac.
				// UVA_ABRECHNUNGSZEITRAUM_MONAT))
				dVonBis = getBuchenFac().getDatumbereichPeriodeGJ(
						krit.getIGeschaeftsjahr(), krit.getIPeriode(),
						theClientDto);

			Timestamp tDatum = new Timestamp(dVonBis[0].getTime());
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					mandantCNr, theClientDto);
			boolean bHauptfinanzamt = false;
			if (mandantDto.getPartnerIIdFinanzamt() != null)
				bHauptfinanzamt = finanzamtDto.getPartnerIId().equals(
						mandantDto.getPartnerIIdFinanzamt());
			for (int i = 0; i < mwstbezs.length; i++) {
				if ((bHauptfinanzamt ? mwstbezs[i].getFinanzamtIId() == null
						: false)
						|| (!(mwstbezs[i].getFinanzamtIId() == null) && mwstbezs[i]
								.getFinanzamtIId().equals(
										finanzamtDto.getPartnerIId()))) {
					if (!mwstbezs[i].getBHandeingabe()) {
						if (mwstbezs[i].getCDruckname() != null) {
							MwstsatzDto mwstDto = getMandantFac()
									.mwstsatzFindZuDatum(mwstbezs[i].getIId(),
											tDatum);
							if (mwstDto != null) {
								String para = "P_"
										+ mwstbezs[i].getCDruckname()
												.toUpperCase()
												.replace(" ", "_");
								mapParameter.put(para, mwstDto.getFMwstsatz());
							}
						}
					}
				}
			}

			// Mandantenwaehrung
			if (bUmrechnen)
				mapParameter.put(LPReport.P_WAEHRUNG, finanzamtwaehrung);
			else
				mapParameter.put(LPReport.P_WAEHRUNG,
						mandantDto.getWaehrungCNr());
			// Mandantadresse
			mapParameter.put(LPReport.P_MANDANT_ANREDE_UND_NAME, mandantDto
					.getPartnerDto().formatFixTitelName1Name2());
			mapParameter.put(LPReport.P_MANDANTADRESSE, mandantDto
					.getPartnerDto().formatAdresse());

			// Finanzamtadresse
			if (finanzamtDto == null) {
				mapParameter.put("P_FA_NAME", "Unbekannt");
				mapParameter.put("P_FA_ADRESSE", "Unbekannt");
				mapParameter.put("P_FA_REFERAT", "Unbekannt");
				// Steuernummer
				mapParameter.put("P_FA_STEUERNUMMER", "Unbekannt");
				mapParameter.put("P_UMSATZRUNDEN", new Boolean(false));
			} else {
				mapParameter.put("P_FA_NAME", finanzamtDto.getPartnerDto()
						.formatFixName1Name2());
				mapParameter.put("P_FA_ADRESSE", finanzamtDto.getPartnerDto()
						.formatAdresse());
				// Steuernummer
				mapParameter.put("P_FA_STEUERNUMMER",
						finanzamtDto.getCSteuernummer());
				mapParameter.put("P_FA_REFERAT", finanzamtDto.getCReferat());
				mapParameter.put("P_UMSATZRUNDEN", new Boolean(bRunden));
			}
			// Periode + Geschaeftsjahr
			mapParameter.put("P_PERIODE", krit.getIPeriode());
			mapParameter.put("P_GESCHAEFTSJAHR", krit.getIGeschaeftsjahr());
			mapParameter.put("P_PERIODE_MONAT", krit.getSPeriode());
			mapParameter.put("P_PERIODE_KALENDERDATUM", dVonBis[0]);

			mapParameter.put("P_ABRECHNUNGSZEITRAUM",
					krit.getSAbrechnungszeitraum());

			data = new Object[0][0];

			// Formularnummer anhaengen, wenn vorhanden
			String report = FinanzReportFac.REPORT_UVA;
			if (finanzamtDto != null) {
				if (finanzamtDto.getIFormularnummer() != null) {
					report = report.replace(".",
							finanzamtDto.getIFormularnummer() + ".");
				}
			}
			initJRDS(mapParameter, FinanzReportFac.REPORT_MODUL, report,
					mandantCNr, theClientDto.getLocUi(), theClientDto);

			JasperPrintLP print = getReportPrint();
			PrintInfoDto values = new PrintInfoDto();
			values.setDocPath(new DocPath(new DocNodeUVAVerprobung(krit,
					finanzamtDto)));
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

	private void pruefeSteuerkontenUva(ReportUvaKriterienDto krit,
			TheClientDto theClientDto) {
		Session session = FLRSessionFactory.getFactory().openSession();
		String queryString = "SELECT DISTINCT (o) FROM FLRSteuerkategoriekonto o"
				+ " WHERE o.flrsteuerkategorie.finanzamt_i_id = :pFinanzamt";
		org.hibernate.Query hquery = session.createQuery(queryString);
		hquery.setParameter("pFinanzamt", krit.getFinanzamtIId());
		List<FLRSteuerkategoriekonto> results = hquery.list();
		Iterator<FLRSteuerkategoriekonto> it = (Iterator<FLRSteuerkategoriekonto>) results
				.iterator();
		while (it.hasNext()) {
			FLRSteuerkategoriekonto stkk = it.next();
			Steuerkategorie stk = em.find(Steuerkategorie.class, stkk
					.getFlrsteuerkategorie().getI_id());

			ArrayList<Integer> kontenIIdAusnahmen = new ArrayList<Integer>();

			// sammelkonto ust ausnehmen
			KontoDto sammelkonto;
			try {
				sammelkonto = getFinanzServiceFac().getSammelkonto(
						FinanzServiceFac.KONTOART_UST_SAMMEL, null,
						theClientDto);
				kontenIIdAusnahmen.add(sammelkonto.getIId());
			} catch (EJBExceptionLP e) {
				//
			}

			// Anzahlungskonten ausnehmen
			Finanzamt finanzamt = em.find(
					Finanzamt.class,
					new FinanzamtPK(krit.getFinanzamtIId(), theClientDto
							.getMandant()));

			kontenIIdAusnahmen.add(stk.getKontoIIdForderungen());
			kontenIIdAusnahmen.add(stk.getKontoIIdKursgewinn());
			kontenIIdAusnahmen.add(stk.getKontoIIdKursverlust());
			kontenIIdAusnahmen
					.add(finanzamt.getKontoIIdAnzahlungErhaltenVerr());
			kontenIIdAusnahmen.add(finanzamt
					.getKontoIIdAnzahlungErhaltenBezahlt());
			kontenIIdAusnahmen.add(finanzamt
					.getKontoIIdRCAnzahlungErhaltenVerr());
			kontenIIdAusnahmen.add(finanzamt
					.getKontoIIdRCAnzahlungErhaltenBezahlt());

			// nur UST Konten pruefen
			if (stkk.getKontoiidvk() != null)
				pruefeSteuerkontoUva(
						krit,
						stkk.getKontoiidvk().getI_id(),
						kontenIIdAusnahmen
								.toArray(new Integer[kontenIIdAusnahmen.size()]),
						theClientDto);
		}
		session.close();
	}

	private void pruefeSteuerkontoUva(ReportUvaKriterienDto krit,
			Integer kontoIId, Integer[] kontoIIdIgnore,
			TheClientDto theClientDto) {
		Session session = FLRSessionFactory.getFactory().openSession();
		String queryString = "SELECT (o) FROM FLRFinanzBuchungDetail o"
				+ " WHERE o.konto_i_id = :pKonto"
				+ " AND o.flrbuchung.t_storniert IS null"
				+ " AND o.flrbuchung.d_buchungsdatum >= :pVon AND o.flrbuchung.d_buchungsdatum < :pEnd"
				+ " AND o.flrbuchung.b_autombuchung = 0";

		org.hibernate.Query hquery = session.createQuery(queryString);
		Timestamp[] tVonBis = getBuchenFac().getDatumbereichPeriodeGJ(
				krit.getIGeschaeftsjahr(), krit.getIPeriode(), theClientDto);
		hquery.setParameter("pKonto", kontoIId);
		hquery.setParameter("pVon", tVonBis[0]);
		hquery.setParameter("pEnd", tVonBis[1]);
		List<FLRFinanzBuchungDetail> results = hquery.list();
		Iterator<FLRFinanzBuchungDetail> it = (Iterator<FLRFinanzBuchungDetail>) results
				.iterator();
		while (it.hasNext()) {
			FLRFinanzBuchungDetail detail = it.next();
			javax.persistence.Query query = em
					.createNamedQuery(Buchungdetail.QueryBuchungdetailfindByBuchungIID);
			query.setParameter(1, detail.getBuchung_i_id());
			List<Buchungdetail> buchungdetails = query.getResultList();
			for (int i = 0; i < buchungdetails.size(); i++) {
				Konto konto = em.find(Konto.class, buchungdetails.get(i)
						.getKontoIId());
				if (konto.getKontotypCNr().equals(
						FinanzServiceFac.KONTOTYP_SACHKONTO)
						&& konto.getIId().compareTo(kontoIId) != 0
						&& istKeinKontoVon(kontoIIdIgnore, konto.getIId())) {
					boolean bvalid = false;
					// wenn Bank dann OK
					BankverbindungDto bankDto = null;
					try {
						bankDto = getFinanzFac()
								.bankverbindungFindByKontoIIdOhneExc(
										konto.getIId());
					} catch (Throwable t) {
						//
					}
					if (bankDto != null)
						bvalid = true;

					if (!bvalid) {
						// wenn Kassenkonto dann OK
						KassenbuchDto kassenDto = null;
						try {
							kassenDto = getFinanzFac()
									.kassenbuchFindByKontoIIdOhneExc(
											konto.getIId());
						} catch (Throwable t) {
							//
						}
						if (kassenDto != null)
							bvalid = true;
					}
					// sonst muss die uvaart gueltig sein
					if (!bvalid) {
						bvalid = true;
						if (konto.getUvaartIId() == null)
							bvalid = false;
						else {
							Uvaart uvaart = em.find(Uvaart.class,
									konto.getUvaartIId());
							if (uvaart.getCNr().equals(
									FinanzServiceFac.UVAART_NICHT_DEFINIERT)
									|| uvaart
											.getCNr()
											.equals(FinanzServiceFac.UVAART_ZAHLLASTKONTO)) {
								bvalid = false;
							}
						}
					}
					if (!bvalid) {
						// Eine Eroeffnungsbuchung darf auf ein Steuerkonto
						// gemacht werden
						// PJ 2012/0477
						FLRFinanzBuchung buchung = detail.getFlrbuchung();
						String buchungsart = buchung.getBuchungsart_c_nr();
						if (FinanzFac.BUCHUNGSART_EROEFFNUNG
								.equals(buchungsart)) {
							bvalid = true;
						}
					}

					if (!bvalid) {
						String kontoNr = detail.getFlrkonto().getC_nr();
						String gegenkontoNr = konto.getCNr();
						Buchung buchung = em.find(Buchung.class,
								detail.getBuchung_i_id());
						String info = "?\n";
						if (buchung != null) {
							info = (buchung.getBelegartCNr() == null ? " "
									: buchung.getBelegartCNr().trim() + " ")
									+ buchung.getCBelegnummer().trim()
									+ ", "
									+ buchung.getBuchungsartCNr().trim()
									+ ", "
									+ buchung.getTBuchungsdatum() + "\n";
						}
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_FINANZ_EXPORT_UST_KONTO_NICHT_DEFINIERT,
								"Buchung: " + info + "Ung\u00FCltiges Konto "
										+ gegenkontoNr
										+ " in Buchung auf Steuerkonto "
										+ kontoNr);
					}
				}
			}
		}
	}

	private boolean istKeinKontoVon(Integer[] kontoIIds, Integer id) {
		boolean found = false;
		for (int i = 0; i < kontoIIds.length; i++) {
			if (kontoIIds[i] != null) {
				if (kontoIIds[i].compareTo(id) == 0) {
					found = true;
					break;
				}
			}
		}
		return !found;
	}

	private BigDecimal umrechnen(BigDecimal betrag, String waehrung,
			ReportUvaKriterienDto krit, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Timestamp[] tVonBis = getBuchenFac().getDatumbereichPeriodeGJ(
				krit.getIGeschaeftsjahr(), krit.getIPeriode(), theClientDto);
		// Achtung: Ende ist der Beginn der naechsten Periode, daher 1 Tag
		// abziehen
		return getLocaleFac().rechneUmInAndereWaehrungGerundetZuDatum(
				betrag,
				theClientDto.getSMandantenwaehrung(),
				waehrung,
				Helper.addiereTageZuDatum(
						new java.sql.Date(tVonBis[1].getTime()), -1),
				theClientDto);

	}

	private BigDecimal getSammelSaldo(ReportUvaKriterienDto krit,
			List<FLRFinanzKonto> konten, TheClientDto theClientDto) {
		BigDecimal saldo = new BigDecimal(0);
		for (Iterator<FLRFinanzKonto> iter = (Iterator<FLRFinanzKonto>) konten
				.iterator(); iter.hasNext();) {
			FLRFinanzKonto konto = iter.next();
			saldo = saldo.add(getBuchenFac().getSaldoUVAOhneEBVonKonto(
					konto.getI_id(), krit.getIGeschaeftsjahr(),
					krit.getIPeriode(), theClientDto));
		}
		return saldo;
	}

	private BigDecimal getSammelSaldo(ReportErfolgsrechnungKriterienDto krit,
			boolean bKummuliert, List<Konto> konten, TheClientDto theClientDto) {
		BigDecimal saldo = new BigDecimal(0);
		for (Iterator<Konto> iter = (Iterator<Konto>) konten.iterator(); iter
				.hasNext();) {
			Konto konto = iter.next();
			saldo = saldo.add(getBuchenFac().getSummeEroeffnungKontoIId(
					konto.getIId(), krit.getIGeschaeftsjahr(),
					krit.getIPeriode(), bKummuliert, theClientDto));
			saldo = saldo.add(getBuchenFac().getSaldoOhneEBVonKonto(
					konto.getIId(), krit.getIGeschaeftsjahr(),
					krit.getIPeriode(), bKummuliert, theClientDto));
		}
		return saldo;
	}

	private BigDecimal getSammelSaldo(ReportErfolgsrechnungKriterienDto krit,
			boolean bKummuliert, List<Konto> konten, boolean bPositvSaldo,
			TheClientDto theClientDto) {
		BigDecimal saldo = new BigDecimal(0);
		BigDecimal saldoKonto;
		for (Iterator<Konto> iter = (Iterator<Konto>) konten.iterator(); iter
				.hasNext();) {
			Konto konto = iter.next();
			saldoKonto = getBuchenFac().getSummeEroeffnungKontoIId(
					konto.getIId(), krit.getIGeschaeftsjahr(),
					krit.getIPeriode(), bKummuliert, theClientDto);
			saldoKonto = saldoKonto.add(getBuchenFac().getSaldoOhneEBVonKonto(
					konto.getIId(), krit.getIGeschaeftsjahr(),
					krit.getIPeriode(), bKummuliert, theClientDto));
			if (bPositvSaldo && saldoKonto.signum() > 0)
				saldo = saldo.add(saldoKonto);
			if (!bPositvSaldo && saldoKonto.signum() < 0)
				saldo = saldo.add(saldoKonto);
		}
		return saldo;
	}

	public JasperPrintLP printLiquiditaetsvorschau(BigDecimal kontostand,
			BigDecimal kreditlimit, Integer kalenderwochen,
			boolean bTerminNachZahlungsmoral, boolean bMitPlankosten,
			ArrayList<LiquititaetsvorschauImportDto> alPlankosten,
			boolean bMitOffenenAngeboten, boolean bMitOffenenBestellungen,
			boolean bMitOffenenAuftraegen, TheClientDto theClientDto) {
		this.useCase = UC_LIQUIDITAET;
		this.index = -1;

		
	
		ArrayList<Object> alDaten = new ArrayList<Object>();

		Calendar c = Calendar.getInstance();
		c.add(Calendar.WEEK_OF_YEAR, kalenderwochen);

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT r FROM FLRRechnungReport r WHERE r.d_belegdatum<'"
				+ Helper.formatDateWithSlashes(new java.sql.Date(c
						.getTimeInMillis()))
				+ "' AND r.status_c_nr IN('"
				+ RechnungFac.STATUS_TEILBEZAHLT
				+ "','"
				+ RechnungFac.STATUS_OFFEN
				+ "') AND r.mandant_c_nr='"
				+ theClientDto.getMandant()
				+ "' AND r.flrrechnungart.rechnungtyp_c_nr <>'"
				+ RechnungFac.RECHNUNGTYP_PROFORMARECHNUNG
				+ "' ORDER BY r.c_nr";

		Query query = session.createQuery(sQuery);

		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {

			FLRRechnungReport flrRechnung = (FLRRechnungReport) resultListIterator
					.next();

			RechnungDto rechnungDto = null;
			try {
				rechnungDto = getRechnungFac().rechnungFindByPrimaryKey(
						flrRechnung.getI_id());
			} catch (RemoteException e1) {
				throwEJBExceptionLPRespectOld(e1);
			}

			try {

				Integer iZieltageZahlungsmoral = getRechnungFac()
						.getZahlungsmoraleinesKunden(
								flrRechnung.getKunde_i_id(), false,
								theClientDto);
				Integer iZieltageVereinbart = 0;

				Date dZieldatum = flrRechnung.getD_belegdatum();

				if (flrRechnung.getT_mahnsperrebis() != null
						&& rechnungDto.getNMtlZahlbetrag() == null) {
					dZieldatum = flrRechnung.getT_mahnsperrebis();
				} else {

					if (bTerminNachZahlungsmoral) {

						if (flrRechnung.getZahlungsziel_i_id() != null) {

							java.sql.Date dZieldatumFuerReport = getMandantFac()
									.berechneZielDatumFuerBelegdatum(
											flrRechnung.getD_belegdatum(),
											flrRechnung.getZahlungsziel_i_id(),
											theClientDto);

							iZieltageVereinbart = Helper
									.ermittleTageEinesZeitraumes(
											rechnungDto.getTBelegdatum(),
											dZieldatumFuerReport);
							if (iZieltageZahlungsmoral == 0) {
								iZieltageZahlungsmoral = iZieltageVereinbart;
							}

						}

						dZieldatum = Helper.addiereTageZuDatum(
								flrRechnung.getD_belegdatum(),
								iZieltageZahlungsmoral);

					} else {

						if (flrRechnung.getZahlungsziel_i_id() != null) {

							dZieldatum = getMandantFac()
									.berechneZielDatumFuerBelegdatum(
											flrRechnung.getD_belegdatum(),
											flrRechnung.getZahlungsziel_i_id(),
											theClientDto);
							iZieltageVereinbart = Helper
									.ermittleTageEinesZeitraumes(
											rechnungDto.getTBelegdatum(),
											new java.sql.Date(dZieldatum
													.getTime()));

						}
					}
				}
				if (dZieldatum.before(new Date())) {
					dZieldatum = new Date();
				}

				BigDecimal bdBezahltFw = getRechnungFac()
						.getBereitsBezahltWertVonRechnungFw(
								flrRechnung.getI_id(), null);
				BigDecimal bdBezahltUstFw = getRechnungFac()
						.getBereitsBezahltWertVonRechnungUstFw(
								flrRechnung.getI_id(), null);

				if (dZieldatum.before(c.getTime())) {

					BigDecimal rechnungswertFW = rechnungDto.getNWertfw();
					BigDecimal ustWertFW = rechnungDto.getNWertustfw();

					BigDecimal offen = rechnungswertFW.subtract(bdBezahltFw);

					BigDecimal offenUst = ustWertFW.subtract(bdBezahltUstFw);

					if (flrRechnung.getFlrrechnungart().getC_nr()
							.equals(RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG)
							&& flrRechnung.getFlrauftrag() != null) {
						RechnungDto[] rechnungDtos = getRechnungFac()
								.rechnungFindByAuftragIId(
										flrRechnung.getFlrauftrag().getI_id());

						for (int i = 0; i < rechnungDtos.length; i++) {
							if (!rechnungDtos[i].getStatusCNr().equals(
									RechnungFac.STATUS_STORNIERT)) {
								if (rechnungDtos[i].getRechnungartCNr().equals(
										RechnungFac.RECHNUNGART_ANZAHLUNG)
										&& rechnungDtos[i].getNWertfw() != null) {
									offen = offen.subtract(rechnungDtos[i]
											.getNWertfw());
									offenUst = offenUst
											.subtract(rechnungDtos[i]
													.getNWertustfw());
								}
							}
						}

					}

					offen = getLocaleFac().rechneUmInAndereWaehrungZuDatum(
							offen, rechnungDto.getWaehrungCNr(),
							theClientDto.getSMandantenwaehrung(),
							new java.sql.Date(System.currentTimeMillis()),
							theClientDto);
					offenUst = getLocaleFac().rechneUmInAndereWaehrungZuDatum(
							offenUst, rechnungDto.getWaehrungCNr(),
							theClientDto.getSMandantenwaehrung(),
							new java.sql.Date(System.currentTimeMillis()),
							theClientDto);

					Calendar cZieldatum = Calendar.getInstance();
					cZieldatum.setTimeInMillis(dZieldatum.getTime());

					if (rechnungDto.getNMtlZahlbetrag() != null
							&& rechnungDto.getIZahltagMtlZahlbetrag() != null) {

						if (cZieldatum.get(Calendar.DAY_OF_MONTH) > rechnungDto
								.getIZahltagMtlZahlbetrag()) {
							cZieldatum.add(Calendar.MONTH, 1);
							cZieldatum.set(Calendar.DAY_OF_MONTH,
									rechnungDto.getIZahltagMtlZahlbetrag());
						}

						while (offen.doubleValue() > 0
								&& cZieldatum.getTime().getTime() < c.getTime()
										.getTime()) {
							Object[] oZeile = new Object[REPORT_LV_ANZAHL_SPALTEN];
							oZeile[REPORT_LV_IMPORTIERT] = new Boolean(false);
							oZeile[REPORT_LV_ZIELTAGE_ZAHLUGNSMORAL] = iZieltageZahlungsmoral;
							oZeile[REPORT_LV_ZIELTAGE_VEREINBARTES_ZAHLUNGSZIEL] = iZieltageVereinbart;
							BigDecimal bdZahlbetrag = rechnungDto
									.getNMtlZahlbetrag();

							if (offen.doubleValue() < bdZahlbetrag
									.doubleValue()) {
								bdZahlbetrag = offen;
							}

							if (flrRechnung.getFlrrechnungart().getC_nr()
									.equals(RechnungFac.RECHNUNGART_GUTSCHRIFT)) {
								oZeile[REPORT_LV_BELEG] = "GS"
										+ flrRechnung.getC_nr();
								oZeile[REPORT_LV_NETTO] = bdZahlbetrag.negate();
								oZeile[REPORT_LV_MWST] = new BigDecimal(0);

							} else {
								oZeile[REPORT_LV_BELEG] = "RE"
										+ flrRechnung.getC_nr();
								oZeile[REPORT_LV_NETTO] = bdZahlbetrag;
								oZeile[REPORT_LV_MWST] = new BigDecimal(0);
							}

							oZeile[REPORT_LV_PARTNER] = flrRechnung
									.getFlrkunde().getFlrpartner()
									.getC_name1nachnamefirmazeile1();

							oZeile[REPORT_LV_JAHR] = cZieldatum
									.get(Calendar.YEAR);
							oZeile[REPORT_LV_WOCHE] = cZieldatum
									.get(Calendar.WEEK_OF_YEAR);
							// wg SP1786
							oZeile[REPORT_LV_JAHR] = Helper
									.berechneJahrDerKW(cZieldatum);
							alDaten.add(oZeile);
							offen = offen.subtract(rechnungDto
									.getNMtlZahlbetrag());
							cZieldatum.add(Calendar.MONTH, 1);

						}

					} else {
						Object[] oZeile = new Object[REPORT_LV_ANZAHL_SPALTEN];
						oZeile[REPORT_LV_IMPORTIERT] = new Boolean(false);
						oZeile[REPORT_LV_ZIELTAGE_ZAHLUGNSMORAL] = iZieltageZahlungsmoral;
						oZeile[REPORT_LV_ZIELTAGE_VEREINBARTES_ZAHLUNGSZIEL] = iZieltageVereinbart;
						if (flrRechnung.getFlrrechnungart().getC_nr()
								.equals(RechnungFac.RECHNUNGART_GUTSCHRIFT)) {
							oZeile[REPORT_LV_BELEG] = "GS"
									+ flrRechnung.getC_nr();
							oZeile[REPORT_LV_NETTO] = offen.negate();
							oZeile[REPORT_LV_MWST] = offenUst.negate();

						} else {
							oZeile[REPORT_LV_BELEG] = "RE"
									+ flrRechnung.getC_nr();
							oZeile[REPORT_LV_NETTO] = offen;
							oZeile[REPORT_LV_MWST] = offenUst;
						}

						oZeile[REPORT_LV_PARTNER] = flrRechnung.getFlrkunde()
								.getFlrpartner()
								.getC_name1nachnamefirmazeile1();

						oZeile[REPORT_LV_JAHR] = cZieldatum.get(Calendar.YEAR);
						oZeile[REPORT_LV_WOCHE] = cZieldatum
								.get(Calendar.WEEK_OF_YEAR);

						// wg SP1786
						oZeile[REPORT_LV_JAHR] = Helper
								.berechneJahrDerKW(cZieldatum);

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
				+ Helper.formatDateWithSlashes(new java.sql.Date(c
						.getTimeInMillis()))
				+ "' AND ((r.eingangsrechnungart_c_nr<>'"
				+ EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN
				+ "' AND r.status_c_nr IN('"
				+ EingangsrechnungFac.STATUS_TEILBEZAHLT
				+ "','"
				+ EingangsrechnungFac.STATUS_ANGELEGT
				+ "')) OR(r.eingangsrechnungart_c_nr='"
				+ EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN
				+ "' AND r.status_c_nr IN('"
				+ EingangsrechnungFac.STATUS_TEILBEZAHLT
				+ "','"
				+ EingangsrechnungFac.STATUS_ERLEDIGT
				+ "','"
				+ EingangsrechnungFac.STATUS_ANGELEGT
				+ "') )) AND r.mandant_c_nr='"
				+ theClientDto.getMandant()
				+ "' ORDER BY r.c_nr";

		query = session.createQuery(sQuery);

		results = query.list();
		resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {

			FLREingangsrechnungReport flrEingangsrechnungReport = (FLREingangsrechnungReport) resultListIterator
					.next();

			try {
				Date dZieldatum = flrEingangsrechnungReport.getT_belegdatum();
				if (flrEingangsrechnungReport.getZahlungsziel_i_id() != null) {

					dZieldatum = getMandantFac()
							.berechneZielDatumFuerBelegdatum(
									flrEingangsrechnungReport.getT_belegdatum(),
									flrEingangsrechnungReport
											.getZahlungsziel_i_id(),
									theClientDto);

				}

				EingangsrechnungDto eDto = getEingangsrechnungFac()
						.eingangsrechnungFindByPrimaryKey(
								flrEingangsrechnungReport.getI_id());

				BigDecimal bdBezahltFW = getEingangsrechnungFac()
						.getBezahltBetragFw(
								flrEingangsrechnungReport.getI_id(), null);
				BigDecimal bdBezahltUstFW = getEingangsrechnungFac()
						.getBezahltBetragUstFw(
								flrEingangsrechnungReport.getI_id(), null);

				bdBezahltFW = bdBezahltFW.subtract(bdBezahltUstFW);

				if (dZieldatum.before(new Date())) {
					dZieldatum = new Date();
				}

				if (dZieldatum.before(c.getTime())) {

					if (eDto.getEingangsrechnungartCNr()
							.equals(EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN)
							&& eDto.getEingangsrechnungIIdNachfolger() == null
							&& eDto.getTWiederholenderledigt() == null) {

						String intervall = eDto.getWiederholungsintervallCNr();

						Calendar cBeginn = Calendar.getInstance();
						cBeginn.setTimeInMillis(flrEingangsrechnungReport
								.getT_belegdatum().getTime());
						int iZaehler = 0;
						while (cBeginn.getTime().before(c.getTime())) {

							Date tBelegdatumNeu = new Date(
									cBeginn.getTimeInMillis());

							if (tBelegdatumNeu.before(new Date())) {
								tBelegdatumNeu = new Date();
							}
							BigDecimal bdNettoFW = flrEingangsrechnungReport
									.getN_betragfw().subtract(
											flrEingangsrechnungReport
													.getN_ustbetragfw());

							BigDecimal offen = null;
							BigDecimal offenUst = null;
							if (iZaehler == 0) {
								offen = bdNettoFW.subtract(bdBezahltFW);
								offenUst = flrEingangsrechnungReport
										.getN_ustbetragfw().subtract(
												bdBezahltUstFW);
							} else {
								// Bei allen zukuenftigen darf das bezahlt nicht
								// beruecksichtigt werden
								offen = bdNettoFW;
								offenUst = flrEingangsrechnungReport
										.getN_ustbetragfw();
							}

							offen = getLocaleFac()
									.rechneUmInAndereWaehrungZuDatum(
											offen,
											eDto.getWaehrungCNr(),
											theClientDto
													.getSMandantenwaehrung(),
											new java.sql.Date(System
													.currentTimeMillis()),
											theClientDto);

							offenUst = getLocaleFac()
									.rechneUmInAndereWaehrungZuDatum(
											offenUst,
											eDto.getWaehrungCNr(),
											theClientDto
													.getSMandantenwaehrung(),
											new java.sql.Date(System
													.currentTimeMillis()),
											theClientDto);
							Object[] oZeile = new Object[REPORT_LV_ANZAHL_SPALTEN];
							oZeile[REPORT_LV_IMPORTIERT] = new Boolean(false);
							oZeile[REPORT_LV_BELEG] = "ZK"
									+ flrEingangsrechnungReport.getC_nr();
							oZeile[REPORT_LV_NETTO] = offen.negate();
							oZeile[REPORT_LV_MWST] = offenUst.negate();

							oZeile[REPORT_LV_PARTNER] = flrEingangsrechnungReport
									.getFlrlieferant().getFlrpartner()
									.getC_name1nachnamefirmazeile1();

							Calendar cZieldatum = Calendar.getInstance();
							cZieldatum
									.setTimeInMillis(tBelegdatumNeu.getTime());

							oZeile[REPORT_LV_JAHR] = cZieldatum
									.get(Calendar.YEAR);
							oZeile[REPORT_LV_WOCHE] = cZieldatum
									.get(Calendar.WEEK_OF_YEAR);
							// wg SP1786
							oZeile[REPORT_LV_JAHR] = Helper
									.berechneJahrDerKW(cZieldatum);
							alDaten.add(oZeile);

							if (AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_WOECHENTLICH
									.equals(intervall)) {
								cBeginn.add(Calendar.DAY_OF_MONTH, 14);
							} else if (AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_WOECHENTLICH
									.equals(intervall)) {
								cBeginn.add(Calendar.DAY_OF_MONTH, 7);
							} else if (AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_JAHR
									.equals(intervall)) {
								cBeginn.add(Calendar.YEAR, 1);
							} else if (AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_2JAHR
									.equals(intervall)) {
								cBeginn.add(Calendar.YEAR, 2);
							} else if (AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_3JAHR
									.equals(intervall)) {
								cBeginn.add(Calendar.YEAR, 3);
							} else if (AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_4JAHR
									.equals(intervall)) {
								cBeginn.add(Calendar.YEAR, 4);
							} else if (AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_5JAHR
									.equals(intervall)) {
								cBeginn.add(Calendar.YEAR, 5);
							} else if (AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_MONATLICH
									.equals(intervall)) {
								cBeginn.add(Calendar.MONTH, 1);
							} else if (AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_QUARTAL
									.equals(intervall)) {
								cBeginn.add(Calendar.MONTH, 3);
							}
							iZaehler++;
						}

					} else {

						if (eDto.getStatusCNr().equals(
								EingangsrechnungFac.STATUS_ERLEDIGT)) {
							continue;
						}

						BigDecimal bdNettoFW = flrEingangsrechnungReport
								.getN_betragfw().subtract(
										flrEingangsrechnungReport
												.getN_ustbetragfw());

						BigDecimal offen = bdNettoFW.subtract(bdBezahltFW);

						BigDecimal offenUst = flrEingangsrechnungReport
								.getN_ustbetragfw().subtract(bdBezahltUstFW);

						offen = getLocaleFac().rechneUmInAndereWaehrungZuDatum(
								offen, eDto.getWaehrungCNr(),
								theClientDto.getSMandantenwaehrung(),
								new java.sql.Date(System.currentTimeMillis()),
								theClientDto);

						offenUst = getLocaleFac()
								.rechneUmInAndereWaehrungZuDatum(
										offenUst,
										eDto.getWaehrungCNr(),
										theClientDto.getSMandantenwaehrung(),
										new java.sql.Date(System
												.currentTimeMillis()),
										theClientDto);
						Object[] oZeile = new Object[REPORT_LV_ANZAHL_SPALTEN];
						oZeile[REPORT_LV_IMPORTIERT] = new Boolean(false);
						oZeile[REPORT_LV_BELEG] = "ER"
								+ flrEingangsrechnungReport.getC_nr();
						oZeile[REPORT_LV_NETTO] = offen.negate();
						oZeile[REPORT_LV_MWST] = offenUst.negate();

						oZeile[REPORT_LV_PARTNER] = flrEingangsrechnungReport
								.getFlrlieferant().getFlrpartner()
								.getC_name1nachnamefirmazeile1();

						Calendar cZieldatum = Calendar.getInstance();
						cZieldatum.setTimeInMillis(dZieldatum.getTime());

						oZeile[REPORT_LV_JAHR] = cZieldatum.get(Calendar.YEAR);
						oZeile[REPORT_LV_WOCHE] = cZieldatum
								.get(Calendar.WEEK_OF_YEAR);
						// wg SP1786
						oZeile[REPORT_LV_JAHR] = Helper
								.berechneJahrDerKW(cZieldatum);

						alDaten.add(oZeile);
					}
				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}
		session.close();

		// PJ17841 Mit Auftraegen
		session = FLRSessionFactory.getFactory().openSession();

		sQuery = "SELECT a FROM FLRAuftragReport a WHERE a.t_belegdatum<'"
				+ Helper.formatDateWithSlashes(new java.sql.Date(c
						.getTimeInMillis()))
				+ "' AND a.auftragstatus_c_nr IN('"
				+ AuftragServiceFac.AUFTRAGSTATUS_OFFEN + "','"
				+ AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT
				+ "') AND a.auftragart_c_nr<>'"
				+ AuftragServiceFac.AUFTRAGART_RAHMEN
				+ "' AND a.mandant_c_nr='" + theClientDto.getMandant()
				+ "' ORDER BY a.c_nr";

		query = session.createQuery(sQuery);

		results = query.list();
		resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {

			FLRAuftragReport flrAuftragReport = (FLRAuftragReport) resultListIterator
					.next();

			AuftragDto aDto = getAuftragFac().auftragFindByPrimaryKey(
					flrAuftragReport.getI_id());

			BigDecimal auftragsWertUST = null;
			BigDecimal auftragsWert = null;

			try {
				auftragsWert = getLocaleFac()
						.rechneUmInAndereWaehrungZuDatum(
								flrAuftragReport
										.getN_gesamtauftragswertinauftragswaehrung(),
								aDto.getCAuftragswaehrung(),
								theClientDto.getSMandantenwaehrung(),
								new java.sql.Date(System.currentTimeMillis()),
								theClientDto);
				auftragsWertUST = getBelegVerkaufFac()
						.getGesamtwertInMandantwaehrungUST(
								getAuftragpositionFac()
										.auftragpositionFindByAuftrag(
												aDto.getIId()), aDto,
								theClientDto);

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			if (flrAuftragReport.getAuftragart_c_nr().equals(
					AuftragServiceFac.AUFTRAGART_WIEDERHOLEND)) {
				String intervall = aDto.getWiederholungsintervallCNr();

				Calendar cBeginn = Calendar.getInstance();
				cBeginn.setTimeInMillis(aDto.getTLauftermin().getTime());
				int iZaehler = 0;
				while (cBeginn.getTime().before(c.getTime())) {

					if (cBeginn.getTime().after(
							new java.sql.Timestamp(System.currentTimeMillis()))) {
						// Wenn Der Lauftermin nach heute ist, dann ist der
						// Wiederholdene Auftrag enthalten

						Date tBelegdatumNeu = new Date(
								cBeginn.getTimeInMillis());

						Object[] oZeile = new Object[REPORT_LV_ANZAHL_SPALTEN];
						oZeile[REPORT_LV_IMPORTIERT] = new Boolean(false);
						oZeile[REPORT_LV_BELEG] = "AW"
								+ flrAuftragReport.getC_nr();
						oZeile[REPORT_LV_NETTO] = auftragsWert;
						oZeile[REPORT_LV_MWST] = auftragsWertUST;

						oZeile[REPORT_LV_PARTNER] = flrAuftragReport
								.getFlrkunde().getFlrpartner()
								.getC_name1nachnamefirmazeile1();

						Calendar cZieldatum = Calendar.getInstance();
						cZieldatum.setTimeInMillis(tBelegdatumNeu.getTime());

						oZeile[REPORT_LV_JAHR] = cZieldatum.get(Calendar.YEAR);
						oZeile[REPORT_LV_WOCHE] = cZieldatum
								.get(Calendar.WEEK_OF_YEAR);
						// wg SP1786
						oZeile[REPORT_LV_JAHR] = Helper
								.berechneJahrDerKW(cZieldatum);
						alDaten.add(oZeile);

					}

					if (AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_WOECHENTLICH
							.equals(intervall)) {
						cBeginn.add(Calendar.DAY_OF_MONTH, 14);
					} else if (AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_WOECHENTLICH
							.equals(intervall)) {
						cBeginn.add(Calendar.DAY_OF_MONTH, 7);
					} else if (AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_JAHR
							.equals(intervall)) {
						cBeginn.add(Calendar.YEAR, 1);
					} else if (AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_2JAHR
							.equals(intervall)) {
						cBeginn.add(Calendar.YEAR, 2);
					} else if (AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_3JAHR
							.equals(intervall)) {
						cBeginn.add(Calendar.YEAR, 3);
					} else if (AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_4JAHR
							.equals(intervall)) {
						cBeginn.add(Calendar.YEAR, 4);
					} else if (AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_5JAHR
							.equals(intervall)) {
						cBeginn.add(Calendar.YEAR, 5);
					} else if (AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_MONATLICH
							.equals(intervall)) {
						cBeginn.add(Calendar.MONTH, 1);
					} else if (AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_QUARTAL
							.equals(intervall)) {
						cBeginn.add(Calendar.MONTH, 3);
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
						Integer iZieltageZahlungsmoral = getRechnungFac()
								.getZahlungsmoraleinesKunden(
										flrAuftragReport.getFlrkunde()
												.getI_id(), false, theClientDto);
						if (bTerminNachZahlungsmoral) {

							if (flrAuftragReport.getZahlungsziel_i_id() != null) {

								java.sql.Date dZieldatumFuerReport = getMandantFac()
										.berechneZielDatumFuerBelegdatum(
												dZieldatum,
												flrAuftragReport
														.getZahlungsziel_i_id(),
												theClientDto);

								iZieltageVereinbart = Helper
										.ermittleTageEinesZeitraumes(
												new java.sql.Date(dZieldatum
														.getTime()),
												dZieldatumFuerReport);
								if (iZieltageZahlungsmoral == 0) {
									iZieltageZahlungsmoral = iZieltageVereinbart;
								}

							}

							dZieldatum = Helper.addiereTageZuDatum(dZieldatum,
									iZieltageZahlungsmoral);

						} else {
							dZieldatum = getMandantFac()
									.berechneZielDatumFuerBelegdatum(
											dZieldatum,
											flrAuftragReport
													.getZahlungsziel_i_id(),
											theClientDto);
						}

						if (dZieldatum.before(c.getTime())) {

							Object[] oZeile = new Object[REPORT_LV_ANZAHL_SPALTEN];
							oZeile[REPORT_LV_IMPORTIERT] = new Boolean(false);
							oZeile[REPORT_LV_BELEG] = "AB"
									+ flrAuftragReport.getC_nr();
							oZeile[REPORT_LV_NETTO] = auftragsWert;
							oZeile[REPORT_LV_MWST] = auftragsWertUST;
							oZeile[REPORT_LV_ZIELTAGE_ZAHLUGNSMORAL] = iZieltageZahlungsmoral;
							oZeile[REPORT_LV_ZIELTAGE_VEREINBARTES_ZAHLUNGSZIEL] = iZieltageVereinbart;
							oZeile[REPORT_LV_PARTNER] = flrAuftragReport
									.getFlrkunde().getFlrpartner()
									.getC_name1nachnamefirmazeile1();

							Calendar cZieldatum = Calendar.getInstance();
							cZieldatum.setTimeInMillis(dZieldatum.getTime());

							oZeile[REPORT_LV_JAHR] = cZieldatum
									.get(Calendar.YEAR);
							oZeile[REPORT_LV_WOCHE] = cZieldatum
									.get(Calendar.WEEK_OF_YEAR);
							// wg SP1786
							oZeile[REPORT_LV_JAHR] = Helper
									.berechneJahrDerKW(cZieldatum);
							alDaten.add(oZeile);
						}
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
				}
			}

		}
		session.close();

		if (bMitOffenenAngeboten == true) {
			session = FLRSessionFactory.getFactory().openSession();
			sQuery = "SELECT a FROM FLRAngebot a WHERE a.t_belegdatum<'"
					+ Helper.formatDateWithSlashes(new java.sql.Date(c
							.getTimeInMillis()))
					+ "' AND a.angebotstatus_c_nr ='"
					+ AngebotServiceFac.ANGEBOTSTATUS_OFFEN
					+ "' AND a.mandant_c_nr='" + theClientDto.getMandant()
					+ "' ORDER BY a.c_nr";

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
				try {
					AngebotDto angebotDto = null;

					angebotDto = getAngebotFac().angebotFindByPrimaryKey(
							flrAngebot.getI_id(), theClientDto);
					Integer iZieltageVereinbart = 0;
					Integer iZieltageZahlungsmoral = getRechnungFac()
							.getZahlungsmoraleinesKunden(
									flrAngebot.getFlrkunde().getI_id(), false,
									theClientDto);

					if (flrAngebot.getT_realisierungstermin() != null) {

						if (bTerminNachZahlungsmoral) {

							if (angebotDto.getZahlungszielIId() != null) {

								java.sql.Date dZieldatumFuerReport = getMandantFac()
										.berechneZielDatumFuerBelegdatum(
												dZieldatum,
												angebotDto.getZahlungszielIId(),
												theClientDto);

								iZieltageVereinbart = Helper
										.ermittleTageEinesZeitraumes(
												new java.sql.Date(dZieldatum
														.getTime()),
												dZieldatumFuerReport);
								if (iZieltageZahlungsmoral == 0) {
									iZieltageZahlungsmoral = iZieltageVereinbart;
								}

							}

							dZieldatum = Helper.addiereTageZuDatum(dZieldatum,
									iZieltageZahlungsmoral);

						} else {
							dZieldatum = getMandantFac()
									.berechneZielDatumFuerBelegdatum(
											dZieldatum,
											angebotDto.getZahlungszielIId(),
											theClientDto);
						}

					}

					if (dZieldatum.before(c.getTime())
							|| flrAngebot.getT_realisierungstermin() == null) {

						oZeile[REPORT_LV_IMPORTIERT] = new Boolean(false);
						oZeile[REPORT_LV_BELEG] = "AG" + flrAngebot.getC_nr();

						BigDecimal angebotsWert = null;
						BigDecimal angebotsWertUST = null;
						try {
							angebotsWert = getLocaleFac()
									.rechneUmInAndereWaehrungZuDatum(
											flrAngebot
													.getN_gesamtangebotswertinangebotswaehrung(),
											angebotDto.getWaehrungCNr(),
											theClientDto
													.getSMandantenwaehrung(),
											new java.sql.Date(System
													.currentTimeMillis()),
											theClientDto);

							angebotsWertUST = getBelegVerkaufFac()
									.getGesamtwertInMandantwaehrungUST(
											getAngebotpositionFac()
													.angebotpositionFindByAngebotIIdOhneAlternative(
															angebotDto.getIId(),
															theClientDto),
											angebotDto, theClientDto);

							if (angebotDto.getFAuftragswahrscheinlichkeit()
									.doubleValue() != 0) {
								angebotsWert = angebotsWert
										.multiply(new BigDecimal(
												angebotDto
														.getFAuftragswahrscheinlichkeit()
														.doubleValue() / 100));
								angebotsWertUST = angebotsWertUST
										.multiply(new BigDecimal(
												angebotDto
														.getFAuftragswahrscheinlichkeit()
														.doubleValue() / 100));
							}

						} catch (RemoteException e) {
							throwEJBExceptionLPRespectOld(e);
						}

						oZeile[REPORT_LV_NETTO] = angebotsWert;
						oZeile[REPORT_LV_MWST] = angebotsWertUST;

						oZeile[REPORT_LV_PARTNER] = flrAngebot.getFlrkunde()
								.getFlrpartner()
								.getC_name1nachnamefirmazeile1();

						Calendar cZieldatum = Calendar.getInstance();
						cZieldatum.setTimeInMillis(dZieldatum.getTime());

						oZeile[REPORT_LV_JAHR] = cZieldatum.get(Calendar.YEAR);
						oZeile[REPORT_LV_WOCHE] = cZieldatum
								.get(Calendar.WEEK_OF_YEAR);
						// wg SP1786
						oZeile[REPORT_LV_JAHR] = Helper
								.berechneJahrDerKW(cZieldatum);
						oZeile[REPORT_LV_ZIELTAGE_ZAHLUGNSMORAL] = iZieltageZahlungsmoral;
						oZeile[REPORT_LV_ZIELTAGE_VEREINBARTES_ZAHLUNGSZIEL] = iZieltageVereinbart;

						alDaten.add(oZeile);
					}
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}
			session.close();
		}
		if (bMitOffenenBestellungen == true) {
			session = FLRSessionFactory.getFactory().openSession();
			sQuery = "SELECT b FROM FLRBestellposition b WHERE b.flrbestellung.t_belegdatum<'"
					+ Helper.formatDateWithSlashes(new java.sql.Date(c
							.getTimeInMillis()))
					+ "' AND b.n_menge IS NOT NULL AND b.flrbestellung.bestellungstatus_c_nr IN ('"
					+ BestellungFac.BESTELLSTATUS_OFFEN
					+ "','"
					+ BestellungFac.BESTELLSTATUS_TEILERLEDIGT
					+ "')"
					+ " AND b.flrbestellung.bestellungart_c_nr ='"
					+ BestellungFac.BESTELLUNGART_FREIE_BESTELLUNG_C_NR
					+ "' AND b.flrbestellung.mandant_c_nr='"
					+ theClientDto.getMandant()
					+ "' ORDER BY b.flrbestellung.c_nr, b.i_sort";

			query = session.createQuery(sQuery);

			results = query.list();
			resultListIterator = results.iterator();
			while (resultListIterator.hasNext()) {

				FLRBestellposition flrBestellposition = (FLRBestellposition) resultListIterator
						.next();
				Object[] oZeile = new Object[REPORT_LV_ANZAHL_SPALTEN];

				Date dZieldatum = flrBestellposition
						.getT_uebersteuerterliefertermin();

				if (flrBestellposition.getT_auftragsbestaetigungstermin() != null) {
					dZieldatum = flrBestellposition
							.getT_auftragsbestaetigungstermin();
				}

				if (dZieldatum.before(new Date())) {
					dZieldatum = new Date();
				}
				try {
					LieferantDto lfDto = getLieferantFac()
							.lieferantFindByPrimaryKey(
									flrBestellposition.getFlrbestellung()
											.getFlrlieferant().getI_id(),
									theClientDto);

					Integer iZieltageVereinbart = 0;

					dZieldatum = getMandantFac()
							.berechneZielDatumFuerBelegdatum(dZieldatum,
									lfDto.getZahlungszielIId(), theClientDto);

					if (dZieldatum.before(c.getTime())) {

						String bestellnummer = flrBestellposition
								.getFlrbestellung().getC_nr()
								+ "/"
								+ getBestellpositionFac().getPositionNummer(
										flrBestellposition.getI_id(),
										theClientDto);

						oZeile[REPORT_LV_IMPORTIERT] = new Boolean(false);

						WareneingangspositionDto[] weposDtos = getWareneingangFac()
								.wareneingangspositionFindByBestellpositionIId(
										flrBestellposition.getI_id());

						BigDecimal posWert = new BigDecimal(0);

						BestellpositionDto besPos = getBestellpositionFac()
								.bestellpositionFindByPrimaryKey(
										flrBestellposition.getI_id());

						if (weposDtos.length == 0) {
							// Noch keine Wareneingang, also Bestellpreis
							// verwenden
							oZeile[REPORT_LV_BELEG] = "BS" + bestellnummer;

							posWert = besPos.getNNettogesamtPreisminusRabatte()
									.multiply(besPos.getNMenge());
						} else {
							oZeile[REPORT_LV_BELEG] = "WE" + bestellnummer;

							for (int i = 0; i < weposDtos.length; i++) {

								// Wenn der Wareneingang dazu keine ER hat

								if (getWareneingangFac()
										.wareneingangFindByPrimaryKey(
												weposDtos[i]
														.getWareneingangIId())
										.getEingangsrechnungIId() != null) {
									continue;
								}

								posWert = posWert
										.add(weposDtos[i]
												.getNGeliefertemenge()
												.multiply(
														weposDtos[i]
																.getNGelieferterpreis()));
							}

						}

						try {
							posWert = getLocaleFac()
									.rechneUmInAndereWaehrungZuDatum(
											posWert,
											flrBestellposition
													.getFlrbestellung()
													.getWaehrung_c_nr_bestellwaehrung(),
											theClientDto
													.getSMandantenwaehrung(),
											new java.sql.Date(System
													.currentTimeMillis()),
											theClientDto);

						} catch (RemoteException e) {
							throwEJBExceptionLPRespectOld(e);
						}

						oZeile[REPORT_LV_NETTO] = posWert.negate();

						MwstsatzDto mwstsatzDtoAktuell = getMandantFac()
								.mwstsatzFindByMwstsatzbezIIdAktuellster(
										lfDto.getMwstsatzbezIId(), theClientDto);
						if (mwstsatzDtoAktuell != null) {
							oZeile[REPORT_LV_MWST] = Helper
									.getMehrwertsteuerBetrag(
											posWert,
											mwstsatzDtoAktuell.getFMwstsatz()
													.doubleValue()).negate();
						} else {
							oZeile[REPORT_LV_MWST] = new BigDecimal(0);
						}

						oZeile[REPORT_LV_PARTNER] = flrBestellposition
								.getFlrbestellung().getFlrlieferant()
								.getFlrpartner()
								.getC_name1nachnamefirmazeile1();

						Calendar cZieldatum = Calendar.getInstance();
						cZieldatum.setTimeInMillis(dZieldatum.getTime());

						oZeile[REPORT_LV_JAHR] = cZieldatum.get(Calendar.YEAR);
						oZeile[REPORT_LV_WOCHE] = cZieldatum
								.get(Calendar.WEEK_OF_YEAR);
						// wg SP1786
						oZeile[REPORT_LV_JAHR] = Helper
								.berechneJahrDerKW(cZieldatum);
						oZeile[REPORT_LV_ZIELTAGE_VEREINBARTES_ZAHLUNGSZIEL] = iZieltageVereinbart;

						alDaten.add(oZeile);
					}
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}
			session.close();
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

					oZeile[REPORT_LV_PARTNER] = alPlankosten.get(i)
							.getPartner();

					if (alPlankosten.get(i).getDatum() != null) {
						Calendar cal = Calendar.getInstance(theClientDto
								.getLocUi());
						cal.setTime(alPlankosten.get(i).getDatum());

						oZeile[REPORT_LV_JAHR] = new Integer(
								cal.get(Calendar.YEAR));
						oZeile[REPORT_LV_WOCHE] = new Integer(
								cal.get(Calendar.WEEK_OF_YEAR));
						oZeile[REPORT_LV_JAHR] = Helper
								.berechneJahrDerKW(cal);
						
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

				if (((Integer) oZeile[REPORT_LV_WOCHE]).intValue() == cKW
						.get(Calendar.WEEK_OF_YEAR)
						&& ((Integer) oZeile[REPORT_LV_JAHR]).intValue() == cKW
								.get(Calendar.YEAR)) {
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

				String woche1 = Helper.fitString2LengthAlignRight(
						o1[REPORT_LV_WOCHE] + "", 2, '0');
				String woche2 = Helper.fitString2LengthAlignRight(
						o2[REPORT_LV_WOCHE] + "", 2, '0');

				String ort1 = (Integer) o1[REPORT_LV_JAHR] + woche1
						+ (String) o1[REPORT_LV_BELEG];
				String ort2 = (Integer) o2[REPORT_LV_JAHR] + woche2
						+ (String) o2[REPORT_LV_BELEG];

				if (ort1.compareTo(ort2) > 0) {
					alDaten.set(n, o2);
					alDaten.set(n + 1, o1);
				}
			}
		}

		Object[][] returnArray = new Object[alDaten.size()][REPORT_LV_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(returnArray);

		Map<String, Object> parameter = new TreeMap<String, Object>();

		parameter.put("P_VON",
				new java.sql.Timestamp(System.currentTimeMillis()));
		parameter.put("P_BIS", new java.sql.Timestamp(c.getTimeInMillis()));
		parameter.put("P_KREDITLIMIT", kreditlimit);

		parameter.put("P_ZIELTERMIN_NACH_ZAHLUNGSMORAL", new Boolean(
				bTerminNachZahlungsmoral));

		parameter.put("P_MIT_OFFENEN_ANGEBOTEN", new Boolean(
				bMitOffenenAngeboten));
		parameter.put("P_MIT_OFFENEN_AUFTRAEGEN", new Boolean(
				bMitOffenenAuftraegen));
		parameter.put("P_MIT_OFFENEN_BESTELLUNGEN", new Boolean(
				bMitOffenenBestellungen));

		parameter.put("P_MIT_PLANKOSTEN", new Boolean(bMitPlankosten));

		ParametermandantDto p;
		try {
			p = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_IMPORT_PLANKOSTEN_DATEI);
			parameter.put("P_PLANKOSTEN_DATEI", p.getCWert());

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		parameter.put("P_KONTOSTAND", kontostand);
		parameter.put("P_BETRACHTUNGSZEITRAUM", kalenderwochen);
		parameter.put("P_WAEHRUNG", theClientDto.getSMandantenwaehrung());

		initJRDS(parameter, FinanzReportFac.REPORT_MODUL,
				FinanzReportFac.REPORT_LIQUIDITAETSVORSCHAU,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);
		return getReportPrint();
	}

	// ACHTUNG: verwendet zur Zeit gleiche Spaltenanzahl und Folge wie
	// Kontoblatt
	public JasperPrintLP printOffenePosten(String kontotypCNr,
			Integer geschaeftsjahr, Integer kontoIId,
			java.sql.Timestamp tStichtag, TheClientDto theClientDto,
			boolean sortAlphabethisch) {
		this.useCase = UC_OFFENEPOSTEN;
		this.index = -1;

		JasperPrintLP print = null;
		HashMap<String, Object> parameter = new HashMap<String, Object>();

		parameter
				.put(LPReport.P_WAEHRUNG, theClientDto.getSMandantenwaehrung());
		parameter.put("P_KONTOTYP", kontotypCNr);
		parameter.put("P_TSTICHTAG", tStichtag);
		parameter.put("P_SORTIERUNG", "Beleg");

		Session session = FLRSessionFactory.getFactory().openSession();
		String q = "from FLRFinanzBuchungDetail buchungdetail LEFT OUTER JOIN buchungdetail.flrbuchung as b LEFT OUTER JOIN buchungdetail.flrkonto as k ";
		q += "where b.d_buchungsdatum<'" + Helper.formatDateWithSlashes(Helper.addiereTageZuDatum(tStichtag, 1)) + "' ";
		q += "and b.geschaeftsjahr_i_geschaeftsjahr=" + geschaeftsjahr + " ";
		q += "and b.t_storniert is null ";
		if(kontoIId != null) {
			q += "and k.i_id=" + kontoIId + " ";
		} else {
			q += "and k.mandant_c_nr='" + theClientDto.getMandant() + "' ";
			q += "and k.kontotyp_c_nr='" + kontotypCNr + "' ";
		}
		q += "and " + BuchungDetailQueryBuilder.buildNurOffeneBuchungDetails("buchungdetail");
		q += "order by ";
		if(sortAlphabethisch) {
			q += "k.c_bez,";
		}
		q+="k.c_nr, buchungdetail.i_ausziffern, b.c_belegnummer, b.d_buchungsdatum";
		
		List<?> results = session.createQuery(q).list();

		Iterator<?> resultListIterator = results.iterator();

		int i = 0;
		ArrayList<Object[]> al = new ArrayList<Object[]>();
		Object[] tempdata = new Object[REPORT_OFFENEPOSTEN_ANZAHL_SPALTEN];
		while (resultListIterator.hasNext()) {
			Object[] row = (Object[]) resultListIterator.next();
			FLRFinanzBuchungDetail buchungDetail = (FLRFinanzBuchungDetail) row[0];

			if (buchungDetail.getFlrbuchung() != null) {
				if (buchungDetail.getFlrbuchung().getFlrfbbelegart() != null) {
					String art = buchungDetail.getFlrbuchung()
							.getFlrbuchungsart().getC_kbez();
					if (art == null)
						art = "__";
					String belegart = "";
					if (buchungDetail.getFlrbuchung().getFlrfbbelegart() != null)
						belegart = buchungDetail.getFlrbuchung()
								.getFlrfbbelegart().getC_kbez();
					tempdata[REPORT_OP_BUCHUNGSART] = art + belegart;
				}
			}

			tempdata[REPORT_OP_BELEG] = buchungDetail.getFlrbuchung()
					.getC_belegnummer();
			tempdata[REPORT_OP_BELEGARTCNR] = buchungDetail.getFlrbuchung()
					.getBuchungsart_c_nr();
			// tempdata[REPORT_OP_TEXT] =
			// buchungDetail.getFlrbuchung().getC_text();
			tempdata[REPORT_OP_TEXT] = buchungDetail.getFlrkonto().getC_bez();
			tempdata[REPORT_OP_KONTOCNR] = buchungDetail.getFlrkonto()
					.getC_nr();

			if (buchungDetail.getBuchungdetailart_c_nr().equals(
					BuchenFac.HabenBuchung)) {
				tempdata[REPORT_OP_HABEN] = buchungDetail.getN_betrag();
			} else {
				tempdata[REPORT_OP_SOLL] = buchungDetail.getN_betrag();
			}

			tempdata[REPORT_OP_BUCHUNGSDATUM] = buchungDetail.getFlrbuchung()
					.getD_buchungsdatum();
			tempdata[REPORT_OP_AUSZUG] = buchungDetail.getI_auszug();

			tempdata[REPORT_OP_USTWERT] = buchungDetail.getN_ust();
			if (buchungDetail.getN_ust().compareTo(new BigDecimal(0.00)) != 0) {
				BigDecimal ustprozent = buchungDetail.getN_betrag().subtract(
						buchungDetail.getN_ust());
				ustprozent = buchungDetail.getN_ust().divide(ustprozent, 5);
				ustprozent = ustprozent.movePointLeft(2);
				tempdata[REPORT_OP_USTPROZENT] = ustprozent;
			} else {
				tempdata[REPORT_OP_USTPROZENT] = null;
			}

			if (buchungDetail.getFlrgegenkonto() != null) {
				tempdata[REPORT_OP_GEGENKONTO] = buchungDetail
						.getFlrgegenkonto().getC_nr();
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
		initJRDS(parameter, FinanzReportFac.REPORT_MODUL,
				FinanzReportFac.REPORT_OFFENEPOSTEN, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);
		print = getReportPrint();
		// }
		return print;
	}

	public JasperPrintLP printErfolgsrechnung(String mandantCNr,
			ReportErfolgsrechnungKriterienDto krit, boolean bBilanz,
			boolean bDetails, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException {
		try {
			this.useCase = UC_ERFOLGSRECHNUNG;
			this.index = -1;

			ErgebnisgruppeDto[] ergebnisgruppenDtos = getFinanzFac()
					.ergebnisgruppeFindAll(theClientDto, bBilanz);

			Map<Integer, ErgebnisgruppeSaldo> map = new TreeMap<Integer, ErgebnisgruppeSaldo>();

			for (int i = 0; i < ergebnisgruppenDtos.length; i++) {
				ErgebnisgruppeSaldo es = new ErgebnisgruppeSaldo(
						ergebnisgruppenDtos[i]);
				map.put(ergebnisgruppenDtos[i].getIId(), es);
			}

			if (bBilanz)
				pruefeErgebnisgruppen(map, theClientDto);

			for (int i = 0; i < ergebnisgruppenDtos.length; i++) {
				ErgebnisgruppeSaldo es = map.get(ergebnisgruppenDtos[i]
						.getIId());
				if (es.ergebnisgruppeDto.getITyp() == FinanzFac.ERGEBNISGRUPPE_TYP_ERGEBNISGRUPPE
						|| es.ergebnisgruppeDto.getITyp() == FinanzFac.ERGEBNISGRUPPE_TYP_BILANZGRUPPE_POSITIV
						|| es.ergebnisgruppeDto.getITyp() == FinanzFac.ERGEBNISGRUPPE_TYP_BILANZGRUPPE_NEGATIV) {
					es.addsaldo(
							map,
							ErgebnisgruppeSaldo.SALDO_PERIODE,
							getSammelSaldo(krit, es,
									ErgebnisgruppeSaldo.SALDO_PERIODE,
									theClientDto));
					es.addsaldo(
							map,
							ErgebnisgruppeSaldo.SALDO_KUMMULIERT,
							getSammelSaldo(krit, es,
									ErgebnisgruppeSaldo.SALDO_KUMMULIERT,
									theClientDto));
					es.addsaldo(
							map,
							ErgebnisgruppeSaldo.SALDO_PERIODE_VORJAHR,
							getSammelSaldo(krit, es,
									ErgebnisgruppeSaldo.SALDO_PERIODE_VORJAHR,
									theClientDto));
					es.addsaldo(
							map,
							ErgebnisgruppeSaldo.SALDO_KUMMULIERT_VORJAHR,
							getSammelSaldo(
									krit,
									es,
									ErgebnisgruppeSaldo.SALDO_KUMMULIERT_VORJAHR,
									theClientDto));
					if (bDetails) {
						ErgebnisgruppeDetailSaldo[] detailsaldos = getDetailSaldos(
								es, krit, theClientDto);
						es.adddetailsaldo(detailsaldos);
					}
				}
			}

			Map<String, Object> mapParameter = new TreeMap<String, Object>();
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
				ErgebnisgruppeSaldo es = map.get(ergebnisgruppenDtos[j]
						.getIId());
				data[i][REPORT_ERFOLGSRECHNUNG_BEZEICHNUNG] = es.ergebnisgruppeDto
						.getCBez();
				data[i][REPORT_ERFOLGSRECHNUNG_SALDO_PERIODE] = es.saldo[ErgebnisgruppeSaldo.SALDO_PERIODE];
				data[i][REPORT_ERFOLGSRECHNUNG_SALDO_KUMMULIERT] = es.saldo[ErgebnisgruppeSaldo.SALDO_KUMMULIERT];
				data[i][REPORT_ERFOLGSRECHNUNG_SALDO_PERIODE_VORJAHR] = es.saldo[ErgebnisgruppeSaldo.SALDO_PERIODE_VORJAHR];
				data[i][REPORT_ERFOLGSRECHNUNG_SALDO_KUMMULIERT_VORJAHR] = es.saldo[ErgebnisgruppeSaldo.SALDO_KUMMULIERT_VORJAHR];
				data[i][REPORT_ERFOLGSRECHNUNG_TYP] = es.ergebnisgruppeDto
						.getITyp();
				data[i][REPORT_ERFOLGSRECHNUNG_BEZUGSBASIS] = Helper
						.short2Boolean(es.ergebnisgruppeDto.getBProzentbasis());
				if (!b100Added
						&& (Boolean) data[i][REPORT_ERFOLGSRECHNUNG_BEZUGSBASIS]) {
					mapParameter.put("P_100PROZ_PERIODE",
							data[i][REPORT_ERFOLGSRECHNUNG_SALDO_PERIODE]);
					mapParameter.put("P_100PROZ_KUMMULIERT",
							data[i][REPORT_ERFOLGSRECHNUNG_SALDO_KUMMULIERT]);
					mapParameter
							.put("P_100PROZ_PERIODE_VORJAHR",
									data[i][REPORT_ERFOLGSRECHNUNG_SALDO_PERIODE_VORJAHR]);
					mapParameter
							.put("P_100PROZ_VORJAHR_KUMMULIERT",
									data[i][REPORT_ERFOLGSRECHNUNG_SALDO_KUMMULIERT_VORJAHR]);
					b100Added = true; // nur der 1. wird hinzugefuegt, falls
					// falsch definiert
				}
				i++;
				if (bDetails && es.detailsaldos != null) {
					for (int k = 0; k < es.detailsaldos.length; k++) {
						ErgebnisgruppeDetailSaldo ds = es.detailsaldos[k];
						data[i][REPORT_ERFOLGSRECHNUNG_TYP] = es.ergebnisgruppeDto
								.getITyp();
						data[i][REPORT_ERFOLGSRECHNUNG_KONTO_CNR] = ds.konto
								.getCNr();
						data[i][REPORT_ERFOLGSRECHNUNG_KONTO_BEZEICHNUNG] = ds.konto
								.getCBez();
						data[i][REPORT_ERFOLGSRECHNUNG_SALDO_PERIODE] = ds.saldo[ErgebnisgruppeSaldo.SALDO_PERIODE];
						data[i][REPORT_ERFOLGSRECHNUNG_SALDO_KUMMULIERT] = ds.saldo[ErgebnisgruppeSaldo.SALDO_KUMMULIERT];
						data[i][REPORT_ERFOLGSRECHNUNG_SALDO_PERIODE_VORJAHR] = ds.saldo[ErgebnisgruppeSaldo.SALDO_PERIODE_VORJAHR];
						data[i][REPORT_ERFOLGSRECHNUNG_SALDO_KUMMULIERT_VORJAHR] = ds.saldo[ErgebnisgruppeSaldo.SALDO_KUMMULIERT_VORJAHR];
						i++;
					}
				}
			}

			// Mandantenwaehrung
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);
			mapParameter.put(LPReport.P_WAEHRUNG, mandantDto.getWaehrungCNr());

			mapParameter.put("P_PERIODE", new Integer(krit.getIPeriode()));

			Calendar cBilanzstichtag = Calendar.getInstance();

			cBilanzstichtag.set(Calendar.YEAR, krit.getIGeschaeftsjahr());

			cBilanzstichtag.set(Calendar.MONTH, krit.getIPeriode()-1);
			cBilanzstichtag.set(Calendar.DAY_OF_MONTH,
					cBilanzstichtag.getActualMaximum(Calendar.DAY_OF_MONTH));
			cBilanzstichtag.set(Calendar.HOUR_OF_DAY, 0);
			cBilanzstichtag.set(Calendar.MINUTE, 0);
			cBilanzstichtag.set(Calendar.SECOND, 0);

			mapParameter.put("P_BILANZSTICHTAG", cBilanzstichtag.getTime());

			if (bBilanz == true) {
				mapParameter.put("P_TYP", "Bilanz");
				mapParameter.put("P_DETAIL", new Boolean(false));
			} else {
				mapParameter.put("P_TYP", "Erfolgsrechnung");
				mapParameter.put("P_DETAIL", new Boolean(bDetails));
			}

			mapParameter.put("P_GESCHAEFTSJAHR", krit.getIGeschaeftsjahr());
			Date[] dVonBis = getBuchenFac()
					.getDatumbereichPeriodeGJ(krit.getIGeschaeftsjahr(),
							krit.getIPeriode(), theClientDto);

			mapParameter.put("P_PERIODE_KALENDERDATUM", dVonBis[0]);

			String report = FinanzReportFac.REPORT_ERFOLGSRECHNUNG;
			if (bBilanz == true) {
				report = FinanzReportFac.REPORT_BILANZ;
			}

			initJRDS(mapParameter, FinanzReportFac.REPORT_MODUL, report,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);
			return getReportPrint();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	private void pruefeErgebnisgruppen(Map<Integer, ErgebnisgruppeSaldo> map,
			TheClientDto theClientDto) {
		Iterator<Integer> it = map.keySet().iterator();
		while (it.hasNext()) {
			ErgebnisgruppeSaldo es = map.get(it.next());
			if (es.ergebnisgruppeDto.getITyp() == FinanzFac.ERGEBNISGRUPPE_TYP_BILANZGRUPPE_POSITIV
					|| es.ergebnisgruppeDto.getITyp() == FinanzFac.ERGEBNISGRUPPE_TYP_BILANZGRUPPE_NEGATIV) {
				List<Konto> konten = getErgebnisgruppeKonten(es, theClientDto);
				for (int i = 0; i < konten.size(); i++) {
					if (konten.get(i).getErgebnisgruppeIId() == null
							|| konten.get(i).getErgebnisgruppeIId_negativ() == null) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_FINANZ_BILANZGRUPPENDEFINITON_POS_NEG,
								"Bilanzgruppendefinition falsch bei Konto"
										+ konten.get(i).getCNr(),
								new Object[] { konten.get(i) });
					}
				}
			}
		}
	}

	private ErgebnisgruppeDetailSaldo[] getDetailSaldos(ErgebnisgruppeSaldo es,
			ReportErfolgsrechnungKriterienDto krit, TheClientDto theClientDto) {
		List<Konto> konten = getErgebnisgruppeKonten(es, theClientDto);
		if (konten.size() == 0)
			return null;
		else {
			ErgebnisgruppeDetailSaldo[] detailsaldos = new ErgebnisgruppeDetailSaldo[konten
					.size()];
			for (int i = 0; i < konten.size(); i++) {
				ErgebnisgruppeDetailSaldo detailsaldo = new ErgebnisgruppeDetailSaldo(
						konten.get(i), es.ergebnisgruppeDto.getBInvertiert());
				detailsaldo
						.addsaldo(
								ErgebnisgruppeSaldo.SALDO_PERIODE,
								getKontoSaldo(krit, konten.get(i), es,
										ErgebnisgruppeSaldo.SALDO_PERIODE,
										theClientDto));
				detailsaldo.addsaldo(
						ErgebnisgruppeSaldo.SALDO_KUMMULIERT,
						getKontoSaldo(krit, konten.get(i), es,
								ErgebnisgruppeSaldo.SALDO_KUMMULIERT,
								theClientDto));
				detailsaldo.addsaldo(
						ErgebnisgruppeSaldo.SALDO_PERIODE_VORJAHR,
						getKontoSaldo(krit, konten.get(i), es,
								ErgebnisgruppeSaldo.SALDO_PERIODE_VORJAHR,
								theClientDto));
				detailsaldo.addsaldo(
						ErgebnisgruppeSaldo.SALDO_KUMMULIERT_VORJAHR,
						getKontoSaldo(krit, konten.get(i), es,
								ErgebnisgruppeSaldo.SALDO_KUMMULIERT_VORJAHR,
								theClientDto));
				detailsaldos[i] = detailsaldo;
			}
			return detailsaldos;
		}
	}

	private BigDecimal getKontoSaldo(ReportErfolgsrechnungKriterienDto krit,
			Konto konto, ErgebnisgruppeSaldo es, int saldoart, TheClientDto theClientDto) {
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
		if (es.ergebnisgruppeDto.getITyp() == FinanzFac.ERGEBNISGRUPPE_TYP_BILANZGRUPPE_POSITIV)
			return getSammelSaldo(krit1, bKummuliert, konten, true,	theClientDto);
		else if (es.ergebnisgruppeDto.getITyp() == FinanzFac.ERGEBNISGRUPPE_TYP_BILANZGRUPPE_NEGATIV)
			return getSammelSaldo(krit1, bKummuliert, konten, false, theClientDto);
		else
			return getSammelSaldo(krit1, bKummuliert, konten, theClientDto);
	}

	private BigDecimal getSammelSaldo(ReportErfolgsrechnungKriterienDto krit,
			ErgebnisgruppeSaldo es, int saldoart, TheClientDto theClientDto) {
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
				return getSammelSaldo(krit1, bKummuliert, konten, true,
						theClientDto);
			else if (es.ergebnisgruppeDto.getITyp() == FinanzFac.ERGEBNISGRUPPE_TYP_BILANZGRUPPE_NEGATIV)
				return getSammelSaldo(krit1, bKummuliert, konten, false,
						theClientDto);
			else
				return getSammelSaldo(krit1, bKummuliert, konten, theClientDto);
		}
	}

	private List<Konto> getErgebnisgruppeKonten(ErgebnisgruppeSaldo es,
			TheClientDto theClientDto) {
		javax.persistence.Query query;
		if (es.ergebnisgruppeDto.getITyp() == FinanzFac.ERGEBNISGRUPPE_TYP_BILANZGRUPPE_NEGATIV)
			query = em
					.createNamedQuery(Konto.QUERY_ALL_ERGEBNISGRUPPENEGATIV_MANDANT);
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

		final static int SALDO_PERIODE = 0;
		final static int SALDO_KUMMULIERT = 1;
		final static int SALDO_PERIODE_VORJAHR = 2;
		final static int SALDO_KUMMULIERT_VORJAHR = 3;

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

		public void addsaldo(Map<Integer, ErgebnisgruppeSaldo> map,
				int saldoart, BigDecimal saldo) {
			if (Helper.short2boolean(this.ergebnisgruppeDto.getBInvertiert()))
				this.saldo[saldoart] = this.saldo[saldoart].add(saldo.negate());
			else
				this.saldo[saldoart] = this.saldo[saldoart].add(saldo);
			if (this.ergebnisgruppeDto.getErgebnisgruppeIIdSumme() != null)
				if (this.iId.compareTo(this.ergebnisgruppeDto
						.getErgebnisgruppeIIdSumme()) != 0) // gruppe kann nicht
					// gleichzeitig
					// summmengruppe
					// sein!
					if (Helper.short2boolean(this.ergebnisgruppeDto
							.getBSummeNegativ()))
						map.get(this.ergebnisgruppeDto
								.getErgebnisgruppeIIdSumme()).addsaldo(map,
								saldoart, saldo.negate());
					else
						map.get(this.ergebnisgruppeDto
								.getErgebnisgruppeIIdSumme()).addsaldo(map,
								saldoart, saldo);
		}
	}

	protected class ErgebnisgruppeDetailSaldo {
		protected Konto konto;
		protected BigDecimal saldo[];
		protected boolean bInvertiert;

		final static int SALDO_PERIODE = 0;
		final static int SALDO_KUMMULIERT = 1;
		final static int SALDO_PERIODE_VORJAHR = 2;
		final static int SALDO_KUMMULIERT_VORJAHR = 3;

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

		public void addsaldo(ErgebnisgruppeDto ergebnisgruppeDto,
				Map<Integer, ErgebnisgruppeSaldo> map, int saldoart,
				BigDecimal saldo) {
		}

	}

	@Override
	public JasperPrintLP printBuchungsbeleg(Integer buchungIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		useCase = UC_BUCHUNGSBELEG;

		Map<String, Object> map = new HashMap<String, Object>();

		BuchungDto buchung = getBuchenFac().buchungFindByPrimaryKey(buchungIId);

		map.put("P_AUTOMATISCHE_BUCHUNG",
				Helper.short2Boolean(buchung.getbAutomatischeBuchung()));
		map.put("P_AUTOMATISCHE_BUCHUNG_EB",
				Helper.short2Boolean(buchung.getbAutomatischeBuchungEB()));
		map.put("P_BUCHUNGSDATUM", new Date(buchung.getDBuchungsdatum()
				.getTime()));
		map.put("P_BUCHUNGSART", buchung.getBuchungsartCNr());
		map.put("P_TEXT", buchung.getCText());
		map.put("P_BELEGNUMMER", buchung.getCBelegnummer());
		map.put("P_GESCHAEFTSJAHR", buchung.getIGeschaeftsjahr());
		map.put("P_T_ANLEGEN", buchung.getTAnlegen() == null ? null : new Date(
				buchung.getTAnlegen().getTime()));
		map.put("P_T_AENDERN", buchung.getTAendern() == null ? null : new Date(
				buchung.getTAendern().getTime()));
		map.put("P_T_STORNIERT", buchung.getTStorniert() == null ? null
				: new Date(buchung.getTStorniert().getTime()));
		map.put("P_PERSONAL_ANLEGEN",
				formatPersonalName(buchung.getPersonalIIdAnlegen()));
		map.put("P_PERSONAL_AENDERN",
				formatPersonalName(buchung.getPersonalIIdAendern()));
		map.put("P_PERSONAL_STORNIERT",
				formatPersonalName(buchung.getPersonalIIdStorniert()));

		KostenstelleDto kostenstelle = getSystemFac()
				.kostenstelleFindByPrimaryKey(buchung.getKostenstelleIId());

		map.put("P_KOSTENSTELLE_BEZ", kostenstelle.getCBez());
		map.put("P_KOSTENSTELLE_CNR", kostenstelle.getCNr());

		BuchungdetailDto[] buchungdetails = getBuchenFac()
				.buchungdetailsFindByBuchungIId(buchungIId);

		data = new Object[buchungdetails.length][REPORT_BUCHUNGSBELEG_ANZAHL_SPALTEN];

		for (int i = 0; i < buchungdetails.length; i++) {
			BuchungdetailDto detail = buchungdetails[i];
			data[i][REPORT_BUCHUNGSBELEG_ARTCNR] = detail
					.getBuchungdetailartCNr();
			data[i][REPORT_BUCHUNGSBELEG_KOMMENTAR] = detail.getKommentar();

			if (detail.getKontoIId() != null) {
				KontoDto konto = getFinanzFac().kontoFindByPrimaryKey(
						detail.getKontoIId());
				data[i][REPORT_BUCHUNGSBELEG_KONTOBEZ] = konto.getCBez();
				data[i][REPORT_BUCHUNGSBELEG_KONTONR] = konto.getCNr();
				data[i][REPORT_BUCHUNGSBELEG_KONTOBEM] = konto.getxBemerkung() == null ? ""
						: konto.getxBemerkung();
				data[i][REPORT_BUCHUNGSBELEG_KONTODTO] = konto;
			}

			data[i][REPORT_BUCHUNGSBELEG_AUSZUG] = detail.getIAuszug();
			data[i][REPORT_BUCHUNGSBELEG_BETRAG] = detail.getNBetrag();
			data[i][REPORT_BUCHUNGSBELEG_BETRAGUST] = detail.getNUst();
			data[i][REPORT_BUCHUNGSBELEG_IST_HABEN] = new Boolean(
					detail.isHabenBuchung());
		}

		initJRDS(map, FinanzReportFac.REPORT_MODUL,
				FinanzReportFac.REPORT_BUCHUNGSBELEG,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();
	}

	private String formatPersonalName(Integer iid) {
		if (iid == null)
			return null;
		PersonalDto personal = getPersonalFac().personalFindByPrimaryKey(iid,
				null);
		if (personal == null)
			return null;
		return personal.getPartnerDto().formatFixName1Name2();
	}
}
