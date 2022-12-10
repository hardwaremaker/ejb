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
package com.lp.server.personal.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.layer.hibernate.HvTypedCriteria;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebot.service.AngebotpositionDto;
import com.lp.server.angebotstkl.fastlanereader.generated.FLRAgstklarbeitsplan;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikellistespr;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.artikel.service.BelegInfos;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.auftrag.service.AuftragzeitenDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungzahlungDto;
import com.lp.server.fertigung.fastlanereader.generated.FLRLossollarbeitsplan;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.BankDto;
import com.lp.server.partner.service.IdValueDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.fastlanereader.ZeitdatenHandler;
import com.lp.server.personal.fastlanereader.generated.FLRMaschine;
import com.lp.server.personal.fastlanereader.generated.FLRMaschinenzeitdaten;
import com.lp.server.personal.fastlanereader.generated.FLRReise;
import com.lp.server.personal.fastlanereader.generated.FLRReisespesen;
import com.lp.server.personal.fastlanereader.generated.FLRSonderzeiten;
import com.lp.server.personal.fastlanereader.generated.FLRTaetigkeitspr;
import com.lp.server.personal.fastlanereader.generated.FLRTelefonzeiten;
import com.lp.server.personal.fastlanereader.generated.FLRZeitabschluss;
import com.lp.server.personal.fastlanereader.generated.FLRZeitdaten;
import com.lp.server.personal.fastlanereader.generated.FLRZeitdatenLos;
import com.lp.server.personal.service.AbschnittEinerReiseDto;
import com.lp.server.personal.service.ArbeitszeitstatistikDto;
import com.lp.server.personal.service.ArbeitszeitstatistikJournalKriterienDto;
import com.lp.server.personal.service.BetriebskalenderDto;
import com.lp.server.personal.service.DiaetentagessatzDto;
import com.lp.server.personal.service.FahrzeugDto;
import com.lp.server.personal.service.MaschineDto;
import com.lp.server.personal.service.MaschinenStundensatzDto;
import com.lp.server.personal.service.MaschinenerfolgReportDto;
import com.lp.server.personal.service.MaschinengruppeDto;
import com.lp.server.personal.service.MaschinenkostenDto;
import com.lp.server.personal.service.MaschinenzeitdatenDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.personal.service.PersonalgehaltDto;
import com.lp.server.personal.service.ReiseDto;
import com.lp.server.personal.service.ReiseKomplettDto;
import com.lp.server.personal.service.ReisekostenDto;
import com.lp.server.personal.service.ReisezeitenEinerPersonDto;
import com.lp.server.personal.service.SonderzeitenDto;
import com.lp.server.personal.service.TaetigkeitDto;
import com.lp.server.personal.service.TelefonzeitenDto;
import com.lp.server.personal.service.VonBisErfassungTagesdatenDto;
import com.lp.server.personal.service.ZeitdatenDto;
import com.lp.server.personal.service.ZeiterfassungAenderungenItemDto;
import com.lp.server.personal.service.ZeiterfassungAenderungenReportDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.personal.service.ZeiterfassungFacAll;
import com.lp.server.personal.service.ZeiterfassungReportFac;
import com.lp.server.personal.service.ZeitmodelltagDto;
import com.lp.server.personal.service.ZeitstiftDto;
import com.lp.server.projekt.fastlanereader.generated.FLRProjekt;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.projekt.service.ProjektFac;
import com.lp.server.projekt.service.ProjektReportFac;
import com.lp.server.projekt.service.ProjektServiceFac;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklistearbeitsplan;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.system.ejb.Belegart;
import com.lp.server.system.fastlanereader.generated.FLREntitylog;
import com.lp.server.system.service.HvDtoLogClass;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.DatumsfilterVonBis;
import com.lp.server.util.HelperServer;
import com.lp.server.util.LPReport;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.report.IJasperPrintTransformer;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.server.util.report.TimingInterceptor;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.LPDatenSubreport;
import com.lp.util.Pair;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

@Stateless
@Interceptors(TimingInterceptor.class)
@TransactionAttribute(TransactionAttributeType.NEVER)
public class ZeiterfassungReportFacBean extends LPReport implements ZeiterfassungReportFac, JRDataSource {

	@PersistenceContext
	private EntityManager em;

	private String sAktuellerReport = null;
	private Object[][] data = null;

	private IJasperPrintTransformer jasperPrintTransformer;
	private static int AENDERUNGEN_TAENDERN_TOLERANZ_IN_MSEC = 500;

	private static int REPORT_ZESTIFTE_KENNUNG = 0;
	private static int REPORT_ZESTIFTE_MEHRFACHSTIFT = 1;
	private static int REPORT_ZESTIFTE_PERSONAL = 2;

	private static int REPORT_MASCHINENPRODUKTIVITAET_MASCHINE = 0;
	private static int REPORT_MASCHINENPRODUKTIVITAET_MASCHINE_KAUFDATUM = 1;
	private static int REPORT_MASCHINENPRODUKTIVITAET_MACHINE_KALK_STUNDENSATZ = 2;
	private static int REPORT_MASCHINENPRODUKTIVITAET_MACHINE_VK_STUNDENSATZ = 3;
	private static int REPORT_MASCHINENPRODUKTIVITAET_MASCHINE_VERFUEGBARKEIT = 4;
	private static int REPORT_MASCHINENPRODUKTIVITAET_BELEG = 5;
	private static int REPORT_MASCHINENPRODUKTIVITAET_VON = 6;
	private static int REPORT_MASCHINENPRODUKTIVITAET_AUFTRAG = 7;
	private static int REPORT_MASCHINENPRODUKTIVITAET_PROJEKT = 8;
	private static int REPORT_MASCHINENPRODUKTIVITAET_STUECKLISTE = 9;
	private static int REPORT_MASCHINENPRODUKTIVITAET_STKLBEZEICHNUNG = 10;
	private static int REPORT_MASCHINENPRODUKTIVITAET_KUNDE = 11;
	private static int REPORT_MASCHINENPRODUKTIVITAET_KOSTEN_KALK = 12;
	private static int REPORT_MASCHINENPRODUKTIVITAET_KOSTEN_VK = 13;
	private static int REPORT_MASCHINENPRODUKTIVITAET_BIS = 14;
	private static int REPORT_MASCHINENPRODUKTIVITAET_DAUER = 15;
	private static int REPORT_MASCHINENPRODUKTIVITAET_FERTIGUNGSGRUPPE = 16;
	private static int REPORT_MASCHINENPRODUKTIVITAET_MASCHINE_ANSCHAFFUNGSKOSTEN = 17;
	private static int REPORT_MASCHINENPRODUKTIVITAET_MASCHINE_ABSCHREIBUNGSDAUER = 18;
	private static int REPORT_MASCHINENPRODUKTIVITAET_MASCHINE_VERZINSUNG = 19;
	private static int REPORT_MASCHINENPRODUKTIVITAET_MASCHINE_ENERGIERKOSTEN = 20;
	private static int REPORT_MASCHINENPRODUKTIVITAET_MASCHINE_RAUMKOSTEN = 21;
	private static int REPORT_MASCHINENPRODUKTIVITAET_MASCHINE_SONSTIGE_KOSTEN = 22;
	private static int REPORT_MASCHINENPRODUKTIVITAET_MASCHINE_PLANSTUNDEN = 23;
	private static int REPORT_MASCHINENPRODUKTIVITAET_MASCHINE_MANUELLE_BEDIENUNG = 24;
	private static int REPORT_MASCHINENPRODUKTIVITAET_ANZAHL_SPALTEN = 25;

	private static int REPORT_MASCHINENLISTE_INVENTARNUMMER = 0;
	private static int REPORT_MASCHINENLISTE_IDENTIFIKATIONSNUMMER = 1;
	private static int REPORT_MASCHINENLISTE_BEZEICHNUNG = 2;
	private static int REPORT_MASCHINENLISTE_KAUFDATUM = 3;
	private static int REPORT_MASCHINENLISTE_VERFUEGBARKEIT = 4;
	private static int REPORT_MASCHINENLISTE_AUTOENDE = 5;
	private static int REPORT_MASCHINENLISTE_MASCHINENGRUPPE = 6;
	private static int REPORT_MASCHINENLISTE_ZULETZT_GESTARTET_VON = 7;
	private static int REPORT_MASCHINENLISTE_ZULETZT_GESTARTET_UM = 8;
	private static int REPORT_MASCHINENLISTE_KOSTEN_ZUM_ZEITPUNKT = 9;
	private static int REPORT_MASCHINENLISTE_ANSCHAFFUNGSKOSTEN = 10;
	private static int REPORT_MASCHINENLISTE_ABSCHREIBUNGSDAUER = 11;
	private static int REPORT_MASCHINENLISTE_VERZINSUNG = 12;
	private static int REPORT_MASCHINENLISTE_ENERGIERKOSTEN = 13;
	private static int REPORT_MASCHINENLISTE_RAUMKOSTEN = 14;
	private static int REPORT_MASCHINENLISTE_SONSTIGE_KOSTEN = 15;
	private static int REPORT_MASCHINENLISTE_PLANSTUNDEN = 16;
	private static int REPORT_MASCHINENLISTE_KOSTEN_VK_ZUM_ZEITPUNKT = 17;
	private static int REPORT_MASCHINENLISTE_MANUELLE_BEDIENUNG = 18;
	private static int REPORT_MASCHINENLISTE_VERRECHNUNGSARTIKEL = 19;
	private static int REPORT_MASCHINENLISTE_VERRECHNUNGSARTIKEL_BEZEICHNUNG = 20;
	private static int REPORT_MASCHINENLISTE_SERIENNUMMER = 21;
	private static int REPORT_MASCHINENLISTE_ANZAHL_SPALTEN = 22;

	private static int REPORT_MASCHINENVERWENDUNG_BELEGART = 0;
	private static int REPORT_MASCHINENVERWENDUNG_STUECKLISTE = 1;
	private static int REPORT_MASCHINENVERWENDUNG_BEZEICHNUNG = 2;
	private static int REPORT_MASCHINENVERWENDUNG_ZUSATZBEZEICHNUNG = 3;
	private static int REPORT_MASCHINENVERWENDUNG_STUECKZEIT = 4;
	private static int REPORT_MASCHINENVERWENDUNG_RUESTZEIT = 5;
	private static int REPORT_MASCHINENVERWENDUNG_ARBEITSGANG = 6;
	private static int REPORT_MASCHINENVERWENDUNG_UNTERARBEITSGANG = 7;
	private static int REPORT_MASCHINENVERWENDUNG_AZ_ARTIKEL = 8;
	private static int REPORT_MASCHINENVERWENDUNG_AZ_ARTIKELBEZEICHNUNG = 9;
	private static int REPORT_MASCHINENVERWENDUNG_ANZAHL_SPALTEN = 11;

	private static int REPORT_MASCHINENZEITDATEN_VON = 0;
	private static int REPORT_MASCHINENZEITDATEN_BIS = 1;
	private static int REPORT_MASCHINENZEITDATEN_DAUER = 2;
	private static int REPORT_MASCHINENZEITDATEN_LOS = 3;
	private static int REPORT_MASCHINENZEITDATEN_PROJEKTBEZEICHNUNG = 4;
	private static int REPORT_MASCHINENZEITDATEN_KUNDE = 5;
	private static int REPORT_MASCHINENZEITDATEN_LOSARTIKELNR = 6;
	private static int REPORT_MASCHINENZEITDATEN_LOSARTIKELBEZ = 7;
	private static int REPORT_MASCHINENZEITDATEN_LOS_AG = 8;
	private static int REPORT_MASCHINENZEITDATEN_LOS_UAG = 9;
	private static int REPORT_MASCHINENZEITDATEN_MASCHINE = 10;
	private static int REPORT_MASCHINENZEITDATEN_MASCHINE_MANUELLE_BEDIENUNG = 11;
	private static int REPORT_MASCHINENZEITDATEN_PERSON = 12;
	private static int REPORT_MASCHINENZEITDATEN_LOSARTIKELREFERENZNUMMER = 13;
	private static int REPORT_MASCHINENZEITDATEN_PARALLELBEDIENUNG = 14;
	private static int REPORT_MASCHINENZEITDATEN_ANZAHL_SPALTEN = 15;

	private static int REPORT_FAHRTENBUCH_TAG = 0;
	private static int REPORT_FAHRTENBUCH_DATUM = 1;
	private static int REPORT_FAHRTENBUCH_KMBEGINN = 2;
	private static int REPORT_FAHRTENBUCH_KMENDE = 3;
	private static int REPORT_FAHRTENBUCH_STRECKE = 4;
	private static int REPORT_FAHRTENBUCH_KUNDE = 5;
	private static int REPORT_FAHRTENBUCH_FAHRZEUG_PRIVAT = 6;
	private static int REPORT_FAHRTENBUCH_FAHRZEUG_FIRMA = 7;
	private static int REPORT_FAHRTENBUCH_FAHRZEUG_FIRMA_KENNZEICHEN = 8;
	private static int REPORT_FAHRTENBUCH_FAHRZEUG_FIRMA_KMKOSTEN = 9;
	private static int REPORT_FAHRTENBUCH_FEHLER_IN_KM = 10;
	private static int REPORT_FAHRTENBUCH_DATUM_ENDE = 11;
	private static int REPORT_FAHRTENBUCH_MITFAHRER = 12;
	private static int REPORT_FAHRTENBUCH_KOMMENTAR = 13;
	private static int REPORT_FAHRTENBUCH_FAHRZEUG_VERWENDUNGSART = 14;
	private static int REPORT_FAHRTENBUCH_REISE_WIRD_FORTGESETZT = 15;
	private static int REPORT_FAHRTENBUCH_REISE_IST_FORTGESETZT = 16;
	private static int REPORT_FAHRTENBUCH_ANZAHL_SPALTEN = 17;

	private static int REPORT_FAHRZEUGE_FAHRZEUG = 0;
	private static int REPORT_FAHRZEUGE_KENNZEICHEN = 1;
	private static int REPORT_FAHRZEUGE_KOSTEN = 2;
	private static int REPORT_FAHRZEUGE_BEGINN = 3;
	private static int REPORT_FAHRZEUGE_ENDE = 4;
	private static int REPORT_FAHRZEUGE_PARTNER = 5;
	private static int REPORT_FAHRZEUGE_LAND = 6;
	private static int REPORT_FAHRZEUGE_STRECKE = 7;
	private static int REPORT_FAHRZEUGE_PERSON = 8;
	private static int REPORT_FAHRZEUGE_ANZAHL_SPALTEN = 9;

	private static int REPORT_ARBEITSZEITSTATISTIK_KUNDE = 0;
	private static int REPORT_ARBEITSZEITSTATISTIK_ARTIKELNUMMER = 1;
	private static int REPORT_ARBEITSZEITSTATISTIK_BEZEICHNUNG = 2;
	private static int REPORT_ARBEITSZEITSTATISTIK_PERSON = 3;
	private static int REPORT_ARBEITSZEITSTATISTIK_BELEG = 4;
	private static int REPORT_ARBEITSZEITSTATISTIK_DAUER = 5;
	private static int REPORT_ARBEITSZEITSTATISTIK_KOSTEN = 6;
	private static int REPORT_ARBEITSZEITSTATISTIK_ARTIKELGRUPPE = 7;
	private static int REPORT_ARBEITSZEITSTATISTIK_ARTIKELKLASSE = 8;
	private static int REPORT_ARBEITSZEITSTATISTIK_GRUPPIERUNG = 9;
	private static int REPORT_ARBEITSZEITSTATISTIK_PROJEKT = 10;
	private static int REPORT_ARBEITSZEITSTATISTIK_PERSON_KOSTENSTELLE = 11;
	private static int REPORT_ARBEITSZEITSTATISTIK_BEMERKUNG = 12;
	private static int REPORT_ARBEITSZEITSTATISTIK_KOMMENTAR = 13;
	private static int REPORT_ARBEITSZEITSTATISTIK_FERTIGUNGSGRUPPE = 14;
	private static int REPORT_ARBEITSZEITSTATISTIK_VERTRETER = 15;
	private static int REPORT_ARBEITSZEITSTATISTIK_VON = 16;
	private static int REPORT_ARBEITSZEITSTATISTIK_BIS = 17;
	private static int REPORT_ARBEITSZEITSTATISTIK_KUNDENBESTELLNUMMER = 18;
	private static int REPORT_ARBEITSZEITSTATISTIK_PROJEKT_STATUS = 19;
	private static int REPORT_ARBEITSZEITSTATISTIK_PROJEKT_KATEGORIE = 20;
	private static int REPORT_ARBEITSZEITSTATISTIK_PROJEKT_GESAMTDAUER = 21;
	private static int REPORT_ARBEITSZEITSTATISTIK_PROJEKT_TYP = 22;
	private static int REPORT_ARBEITSZEITSTATISTIK_PROJEKT_VERRECHENBAR = 23;
	private static int REPORT_ARBEITSZEITSTATISTIK_PROJEKT_INTERN_ERLEDIGT = 24;
	private static int REPORT_ARBEITSZEITSTATISTIK_PERSONALNUMMER = 25;
	private static int REPORT_ARBEITSZEITSTATISTIK_KURZZEICHEN = 26;
	private static int REPORT_ARBEITSZEITSTATISTIK_PROJEKT_I_ID = 27;
	private static int REPORT_ARBEITSZEITSTATISTIK_PROJEKT_BEREICH = 28;
	private static int REPORT_ARBEITSZEITSTATISTIK_VERRECHENBAR = 29;
	private static int REPORT_ARBEITSZEITSTATISTIK_PERSON_ERLEDIGT = 30;
	private static int REPORT_ARBEITSZEITSTATISTIK_ZEITPUNKT_ERLEDIGT = 31;
	private static int REPORT_ARBEITSZEITSTATISTIK_BEREITS_VERRECHNET = 32;
	private static int REPORT_ARBEITSZEITSTATISTIK_KOMMENTAR_INTERN = 33;
	private static int REPORT_ARBEITSZEITSTATISTIK_ANZAHL_SPALTEN = 34;

	private static int REPORT_AUFTRAGSZEITSTATISTIK_PERSON = 0;
	private static int REPORT_AUFTRAGSZEITSTATISTIK_SUBREPORT_AUFTRAEGE = 1;
	private static int REPORT_AUFTRAGSZEITSTATISTIK_NICHT_ZUORDENBAR = 2;
	private static int REPORT_AUFTRAGSZEITSTATISTIK_SONDERZEITEN = 3;
	private static int REPORT_AUFTRAGSZEITSTATISTIK_ANZAHL_SPALTEN = 4;

	private static int REPORT_MITARBEITERUEBERSICHT_NAME = 0;
	private static int REPORT_MITARBEITERUEBERSICHT_TAG01 = 1;
	private static int REPORT_MITARBEITERUEBERSICHT_TAG02 = 2;
	private static int REPORT_MITARBEITERUEBERSICHT_TAG03 = 3;
	private static int REPORT_MITARBEITERUEBERSICHT_TAG04 = 4;
	private static int REPORT_MITARBEITERUEBERSICHT_TAG05 = 5;
	private static int REPORT_MITARBEITERUEBERSICHT_TAG06 = 6;
	private static int REPORT_MITARBEITERUEBERSICHT_TAG07 = 7;
	private static int REPORT_MITARBEITERUEBERSICHT_TAG08 = 8;
	private static int REPORT_MITARBEITERUEBERSICHT_TAG09 = 9;
	private static int REPORT_MITARBEITERUEBERSICHT_TAG10 = 10;
	private static int REPORT_MITARBEITERUEBERSICHT_TAG11 = 11;
	private static int REPORT_MITARBEITERUEBERSICHT_TAG12 = 12;
	private static int REPORT_MITARBEITERUEBERSICHT_TAG13 = 13;
	private static int REPORT_MITARBEITERUEBERSICHT_TAG14 = 14;
	private static int REPORT_MITARBEITERUEBERSICHT_TAG15 = 15;
	private static int REPORT_MITARBEITERUEBERSICHT_TAG16 = 16;
	private static int REPORT_MITARBEITERUEBERSICHT_TAG17 = 17;
	private static int REPORT_MITARBEITERUEBERSICHT_TAG18 = 18;
	private static int REPORT_MITARBEITERUEBERSICHT_TAG19 = 19;
	private static int REPORT_MITARBEITERUEBERSICHT_TAG20 = 20;
	private static int REPORT_MITARBEITERUEBERSICHT_TAG21 = 21;
	private static int REPORT_MITARBEITERUEBERSICHT_TAG22 = 22;
	private static int REPORT_MITARBEITERUEBERSICHT_TAG23 = 23;
	private static int REPORT_MITARBEITERUEBERSICHT_TAG24 = 24;
	private static int REPORT_MITARBEITERUEBERSICHT_TAG25 = 25;
	private static int REPORT_MITARBEITERUEBERSICHT_TAG26 = 26;
	private static int REPORT_MITARBEITERUEBERSICHT_TAG27 = 27;
	private static int REPORT_MITARBEITERUEBERSICHT_TAG28 = 28;
	private static int REPORT_MITARBEITERUEBERSICHT_TAG29 = 29;
	private static int REPORT_MITARBEITERUEBERSICHT_TAG30 = 30;
	private static int REPORT_MITARBEITERUEBERSICHT_TAG31 = 31;
	private static int REPORT_MITARBEITERUEBERSICHT_ANWESENHEITSUMME = 32;
	private static int REPORT_MITARBEITERUEBERSICHT_KALK_JAHRESISTSTUNDEN = 33;

	private static int REPORT_MITARBEITERUEBERSICHT_BEZ01 = 34;
	private static int REPORT_MITARBEITERUEBERSICHT_BEZ02 = 35;
	private static int REPORT_MITARBEITERUEBERSICHT_BEZ03 = 36;
	private static int REPORT_MITARBEITERUEBERSICHT_BEZ04 = 37;
	private static int REPORT_MITARBEITERUEBERSICHT_BEZ05 = 38;
	private static int REPORT_MITARBEITERUEBERSICHT_BEZ06 = 39;
	private static int REPORT_MITARBEITERUEBERSICHT_BEZ07 = 40;
	private static int REPORT_MITARBEITERUEBERSICHT_BEZ08 = 41;
	private static int REPORT_MITARBEITERUEBERSICHT_BEZ09 = 42;
	private static int REPORT_MITARBEITERUEBERSICHT_BEZ10 = 43;
	private static int REPORT_MITARBEITERUEBERSICHT_BEZ11 = 44;
	private static int REPORT_MITARBEITERUEBERSICHT_BEZ12 = 45;
	private static int REPORT_MITARBEITERUEBERSICHT_BEZ13 = 46;
	private static int REPORT_MITARBEITERUEBERSICHT_BEZ14 = 47;
	private static int REPORT_MITARBEITERUEBERSICHT_BEZ15 = 48;
	private static int REPORT_MITARBEITERUEBERSICHT_BEZ16 = 49;
	private static int REPORT_MITARBEITERUEBERSICHT_BEZ17 = 50;
	private static int REPORT_MITARBEITERUEBERSICHT_BEZ18 = 51;
	private static int REPORT_MITARBEITERUEBERSICHT_BEZ19 = 52;
	private static int REPORT_MITARBEITERUEBERSICHT_BEZ20 = 53;
	private static int REPORT_MITARBEITERUEBERSICHT_BEZ21 = 54;
	private static int REPORT_MITARBEITERUEBERSICHT_BEZ22 = 55;
	private static int REPORT_MITARBEITERUEBERSICHT_BEZ23 = 56;
	private static int REPORT_MITARBEITERUEBERSICHT_BEZ24 = 57;
	private static int REPORT_MITARBEITERUEBERSICHT_BEZ25 = 58;
	private static int REPORT_MITARBEITERUEBERSICHT_BEZ26 = 59;
	private static int REPORT_MITARBEITERUEBERSICHT_BEZ27 = 60;
	private static int REPORT_MITARBEITERUEBERSICHT_BEZ28 = 61;
	private static int REPORT_MITARBEITERUEBERSICHT_BEZ29 = 62;
	private static int REPORT_MITARBEITERUEBERSICHT_BEZ30 = 63;
	private static int REPORT_MITARBEITERUEBERSICHT_BEZ31 = 64;

	private static int REPORT_MITARBEITERUEBERSICHT_KOSTENSTELLE = 65;
	private static int REPORT_MITARBEITERUEBERSICHT_ABTEILUNG = 66;

	private static int REPORT_MITARBEITERUEBERSICHT_ANZAHL_SPALTEN = 67;

	private static int REPORT_TELEFONLISTE_PERSON = 0;
	private static int REPORT_TELEFONLISTE_VON = 1;
	private static int REPORT_TELEFONLISTE_BIS = 2;
	private static int REPORT_TELEFONLISTE_PARTNER = 3;
	private static int REPORT_TELEFONLISTE_ANSPRECHPARTNER = 4;
	private static int REPORT_TELEFONLISTE_KOMMENTAREXTERN = 5;
	private static int REPORT_TELEFONLISTE_KOMMENTARINTERN = 6;
	private static int REPORT_TELEFONLISTE_DAUER = 7;
	private static int REPORT_TELEFONLISTE_LKZ_PARTNER = 8;
	private static int REPORT_TELEFONLISTE_PROJEKT = 9;
	private static int REPORT_TELEFONLISTE_GRUPPIERUNG = 10;
	private static int REPORT_TELEFONLISTE_ANZAHL_SPALTEN = 11;

	private static int REPORT_MASCHINENBELEGUNG_INVENTARNUMMER = 0;
	private static int REPORT_MASCHINENBELEGUNG_IDENTIFIKATIONSNUMMER = 1;
	private static int REPORT_MASCHINENBELEGUNG_BEZEICHNUNG = 2;
	private static int REPORT_MASCHINENBELEGUNG_DATUM = 3;
	private static int REPORT_MASCHINENBELEGUNG_LOSNUMMER = 4;
	private static int REPORT_MASCHINENBELEGUNG_LOSGROESSE = 5;
	private static int REPORT_MASCHINENBELEGUNG_STUECKLISTE = 6;
	private static int REPORT_MASCHINENBELEGUNG_STUECKLISTEBEZEICHNUNG = 7;
	private static int REPORT_MASCHINENBELEGUNG_STUECKLISTEKURZBEZEICHNUNG = 8;
	private static int REPORT_MASCHINENBELEGUNG_STUECKLISTEZUSATZBEZEICHNUNG = 9;
	private static int REPORT_MASCHINENBELEGUNG_STUECKLISTEZUSATZBEZEICHNUNG2 = 10;
	private static int REPORT_MASCHINENBELEGUNG_PROJEKT = 11;
	private static int REPORT_MASCHINENBELEGUNG_KOMMENTAR = 12;
	private static int REPORT_MASCHINENBELEGUNG_ARBEITSGANG = 13;
	private static int REPORT_MASCHINENBELEGUNG_ARTIKEL = 14;
	private static int REPORT_MASCHINENBELEGUNG_ARTIKELBEZEICHNUNG = 15;
	private static int REPORT_MASCHINENBELEGUNG_SOLL = 16;
	private static int REPORT_MASCHINENBELEGUNG_FERTIG = 17;
	private static int REPORT_MASCHINENBELEGUNG_VERFUEGBARKEIT = 18;
	private static int REPORT_MASCHINENBELEGUNG_UNTERARBEITSGANG = 19;
	private static int REPORT_MASCHINENBELEGUNG_MASCHINENGRUPPE = 20;
	private static int REPORT_MASCHINENBELEGUNG_POSITIONSKOMMENTAR = 21;
	private static int REPORT_MASCHINENBELEGUNG_LOSENDE = 22;
	private static int REPORT_MASCHINENBELEGUNG_RUESTZEIT = 23;
	private static int REPORT_MASCHINENBELEGUNG_STUECKZEIT = 24;
	private static int REPORT_MASCHINENBELEGUNG_KUNDE_NAME = 25;
	private static int REPORT_MASCHINENBELEGUNG_KUNDE_LKZ = 26;
	private static int REPORT_MASCHINENBELEGUNG_KUNDE_PLZ = 27;
	private static int REPORT_MASCHINENBELEGUNG_KUNDE_ORT = 28;
	private static int REPORT_MASCHINENBELEGUNG_LOSKLASSEN = 29;
	private static int REPORT_MASCHINENBELEGUNG_UAG_NAECHSTER_AG_MASCHINE_BEZEICHNUNG = 30;
	private static int REPORT_MASCHINENBELEGUNG_UAG_NAECHSTER_AG_MASCHINE_IDENTIFIKATIONSNUMMER = 31;
	private static int REPORT_MASCHINENBELEGUNG_UAG_NAECHSTER_AG_MASCHINE_INVENTARNUMMER = 32;
	private static int REPORT_MASCHINENBELEGUNG_PRODUKTIONSSTOP = 33;
	private static int REPORT_MASCHINENBELEGUNG_VERSTECKT = 34;
	private static int REPORT_MASCHINENBELEGUNG_FERTIGUNGSGRUPPE = 35;
	private static int REPORT_MASCHINENBELEGUNG_STUECKLISTEREFERENZNUMMER = 36;
	private static int REPORT_MASCHINENBELEGUNG_SERIENNUMMER = 37;
	private static int REPORT_MASCHINENBELEGUNG_ANZAHL_SPALTEN = 38;

	private static int REPORT_MASCHINENERFOLG_PERSONALNUMMER = 0;
	private static int REPORT_MASCHINENERFOLG_VORNAME = 1;
	private static int REPORT_MASCHINENERFOLG_NACHNAME = 2;
	private static int REPORT_MASCHINENERFOLG_DATUM = 3;
	private static int REPORT_MASCHINENERFOLG_ANWESENHEITSZEIT = 4;
	private static int REPORT_MASCHINENERFOLG_INVENTARNUMMER = 5;
	private static int REPORT_MASCHINENERFOLG_BEZEICHNUNG = 6;
	private static int REPORT_MASCHINENERFOLG_MASCHINENGRUPPE = 7;
	private static int REPORT_MASCHINENERFOLG_IDENTIFIKATIONSNUMMER = 8;
	private static int REPORT_MASCHINENERFOLG_LAUFZEIT = 9;
	private static int REPORT_MASCHINENERFOLG_JAHRMONAT = 10;
	private static int REPORT_MASCHINENERFOLG_ABTEILUNG = 11;
	private static int REPORT_MASCHINENERFOLG_KOSTENSTELLE = 12;
	private static int REPORT_MASCHINENERFOLG_SORT_PERSONAL = 13;
	private static int REPORT_MASCHINENERFOLG_SERIENNUMMER = 14;
	private static int REPORT_MASCHINENERFOLG_ANZAHL_SPALTEN = 15;

	private static int REPORT_MITARBEITEREINTEILUNG_PERSONAL = 0;
	private static int REPORT_MITARBEITEREINTEILUNG_PERSONALNUMMER = 1;
	private static int REPORT_MITARBEITEREINTEILUNG_DATUM = 2;
	private static int REPORT_MITARBEITEREINTEILUNG_LOSNUMMER = 3;
	private static int REPORT_MITARBEITEREINTEILUNG_ARBEITSGANG = 4;
	private static int REPORT_MITARBEITEREINTEILUNG_ARTIKEL = 5;
	private static int REPORT_MITARBEITEREINTEILUNG_ARTIKELBEZEICHNUNG = 6;
	private static int REPORT_MITARBEITEREINTEILUNG_FERTIG = 7;
	private static int REPORT_MITARBEITEREINTEILUNG_LAUFZEIT = 8;
	private static int REPORT_MITARBEITEREINTEILUNG_UMSPANNZEIT = 9;
	private static int REPORT_MITARBEITEREINTEILUNG_MASCHINE = 10;
	private static int REPORT_MITARBEITEREINTEILUNG_UNTERARBEITSGANG = 11;
	private static int REPORT_MITARBEITEREINTEILUNG_TAGESSOLL = 12;
	private static int REPORT_MITARBEITEREINTEILUNG_SOLL = 13;
	private static int REPORT_MITARBEITEREINTEILUNG_KOSTENSTELLE = 14;
	private static int REPORT_MITARBEITEREINTEILUNG_ABTEILUNG = 15;
	private static int REPORT_MITARBEITEREINTEILUNG_ANZAHL_SPALTEN = 16;

	private static int REPORT_PRODUKTIVITAETSTAGESSTATISTIK_PERSON = 0;
	private static int REPORT_PRODUKTIVITAETSTAGESSTATISTIK_LOS = 1;
	private static int REPORT_PRODUKTIVITAETSTAGESSTATISTIK_KUNDE = 2;
	private static int REPORT_PRODUKTIVITAETSTAGESSTATISTIK_DAUER = 3;
	private static int REPORT_PRODUKTIVITAETSTAGESSTATISTIK_STKLNR = 4;
	private static int REPORT_PRODUKTIVITAETSTAGESSTATISTIK_STKLBEZ = 5;
	private static int REPORT_PRODUKTIVITAETSTAGESSTATISTIK_LOSKLASSEN = 6;
	private static int REPORT_PRODUKTIVITAETSTAGESSTATISTIK_TAETIGKEIT = 7;
	private static int REPORT_PRODUKTIVITAETSTAGESSTATISTIK_TAETIGKEIT_BEZEICHNUNG = 8;
	private static int REPORT_PRODUKTIVITAETSTAGESSTATISTIK_AGART = 9;
	private static int REPORT_PRODUKTIVITAETSTAGESSTATISTIK_RUESTZEIT = 10;
	private static int REPORT_PRODUKTIVITAETSTAGESSTATISTIK_STUECKZEIT = 11;
	private static int REPORT_PRODUKTIVITAETSTAGESSTATISTIK_GUTSTUECK = 12;
	private static int REPORT_PRODUKTIVITAETSTAGESSTATISTIK_SCHLECHTSTUECK = 13;
	private static int REPORT_PRODUKTIVITAETSTAGESSTATISTIK_LOSGROESSE = 14;
	private static int REPORT_PRODUKTIVITAETSTAGESSTATISTIK_FERTIG = 15;
	private static int REPORT_PRODUKTIVITAETSTAGESSTATISTIK_FERTIGUNGSGRUPPE = 16;
	private static int REPORT_PRODUKTIVITAETSTAGESSTATISTIK_MASCHINE_BEZ = 17;
	private static int REPORT_PRODUKTIVITAETSTAGESSTATISTIK_MASCHINE_IDENTIFIKATION = 18;
	private static int REPORT_PRODUKTIVITAETSTAGESSTATISTIK_MASCHINE_INVENTATNUMMER = 19;
	private static int REPORT_PRODUKTIVITAETSTAGESSTATISTIK_DATUM = 20;
	private static int REPORT_PRODUKTIVITAETSTAGESSTATISTIK_ARBEITSGANG = 21;
	private static int REPORT_PRODUKTIVITAETSTAGESSTATISTIK_UNTERARBEITSGANG = 22;
	private static int REPORT_PRODUKTIVITAETSTAGESSTATISTIK_RUESTEN_MITRECHNEN = 23;
	private static int REPORT_PRODUKTIVITAETSTAGESSTATISTIK_LEISTUNGSFAKTOR = 24;
	private static int REPORT_PRODUKTIVITAETSTAGESSTATISTIK_ANWESENHEITSZEIT_MONATSBETRACHTUNG = 25;
	private static int REPORT_PRODUKTIVITAETSTAGESSTATISTIK_ANZAHL_SPALTEN = 26;

	private static int REPORT_ZEITABSCHLUSS_NAME = 0;
	private static int REPORT_ZEITABSCHLUSS_VORNAME = 1;
	private static int REPORT_ZEITABSCHLUSS_TITEL = 2;
	private static int REPORT_ZEITABSCHLUSS_ZEITEN_ABGESCHLOSSEN_BIS = 3;
	private static int REPORT_ZEITABSCHLUSS_PERSONALNR = 4;
	private static int REPORT_ZEITABSCHLUSS_ANZAHL_SPALTEN = 5;

	private static int REPORT_URLAUBSANTRAG_VON = 0;
	private static int REPORT_URLAUBSANTRAG_BIS = 1;
	private static int REPORT_URLAUBSANTRAG_ZUSATZ = 2;
	private static int REPORT_URLAUBSANTRAG_URLAUB = 3;
	private static int REPORT_URLAUBSANTRAG_ANZAHL_TAGE = 4;
	private static int REPORT_URLAUBSANTRAG_ANZAHL_HALBE_TAGE = 5;
	private static int REPORT_URLAUBSANTRAG_ANZAHL_STUNDEN = 6;
	private static int REPORT_URLAUBSANTRAG_KRANK = 7;
	private static int REPORT_URLAUBSANTRAG_ANZAHL_SPALTEN = 8;

	private static int REPORT_ZEITERFASSUNG_AENDERUNGEN_FELDNAME = 0;
	private static int REPORT_ZEITERFASSUNG_AENDERUNGEN_AENDERUNGSZEITPUNKT = 1;
	private static int REPORT_ZEITERFASSUNG_AENDERUNGEN_GEAENDERT_VON = 2;
	private static int REPORT_ZEITERFASSUNG_AENDERUNGEN_GEAENDERT_NACH = 3;
	private static int REPORT_ZEITERFASSUNG_AENDERUNGEN_OPERATION = 4;
	private static int REPORT_ZEITERFASSUNG_AENDERUNGEN_WER = 5;
	private static int REPORT_ZEITERFASSUNG_AENDERUNGEN_ANZAHL_SPALTEN = 6;

	protected void setData(Object[][] newData) {
		data = newData;
		jasperPrintTransformer = null;
	}

	protected void setDataTransformer(IJasperPrintTransformer transformer) {
		data = null;
		jasperPrintTransformer = transformer;
	}

	public JasperPrintLP printMaschinenproduktivitaet(Integer maschineIId, java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, TheClientDto theClientDto) {

		sAktuellerReport = ZeiterfassungReportFac.REPORT_MASCHINENPRODUKTIVITAET;
		JasperPrintLP print = null;

		ArrayList<Object[]> alDaten = new ArrayList<Object[]>();

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();

		org.hibernate.Criteria crit = session
				.createCriteria(com.lp.server.personal.fastlanereader.generated.FLRMaschine.class)
				.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));

		if (maschineIId != null) {
			crit.add(Restrictions.eq("i_id", maschineIId));
		}

		crit.addOrder(Order.asc(ZeiterfassungFac.FLR_MASCHINE_C_IDENTIFIKATIONSNR));

		List<?> resultList = crit.list();

		Iterator<?> resultListIterator = resultList.iterator();

		while (resultListIterator.hasNext()) {

			FLRMaschine flrmaschine = (FLRMaschine) resultListIterator.next();

			try {
				ArrayList<MaschinenzeitdatenDto> zeitdatenEinerMaschine = getZeiterfassungFac()
						.getZeitdatenEinerMaschine(flrmaschine.getI_id(), tVon, tBis, theClientDto);

				MaschineDto maschineDto = getZeiterfassungFac().maschineFindByPrimaryKey(flrmaschine.getI_id());

				BigDecimal bdVerfuegbarkeitInStunden = BigDecimal.ZERO;

				int iAnzahlTage = Helper.getDifferenzInTagen(tVon, tBis);

				java.sql.Timestamp tTemp = tVon;

				for (int j = 0; j < iAnzahlTage; j++) {

					BigDecimal bdVerf = getMaschineFac().getVerfuegbarkeitInStundenZuDatum(flrmaschine.getI_id(),
							new java.sql.Date(tTemp.getTime()), theClientDto);

					if (bdVerf != null) {

						bdVerfuegbarkeitInStunden = bdVerfuegbarkeitInStunden.add(bdVerf);

					}

					tTemp = new java.sql.Timestamp(tTemp.getTime() + 24 * 3600000);

				}

				for (int i = 0; i < zeitdatenEinerMaschine.size(); i++) {

					MaschinenzeitdatenDto maschinenzeitdatenDto = zeitdatenEinerMaschine.get(i);

					Object[] oZeile = new Object[REPORT_MASCHINENPRODUKTIVITAET_ANZAHL_SPALTEN];

					oZeile[REPORT_MASCHINENPRODUKTIVITAET_MASCHINE] = maschineDto.getBezeichnung();

					oZeile[REPORT_MASCHINENPRODUKTIVITAET_MASCHINE_MANUELLE_BEDIENUNG] = Helper
							.short2Boolean(maschineDto.getBManuelleBedienung());

					oZeile[REPORT_MASCHINENPRODUKTIVITAET_MASCHINE_KAUFDATUM] = maschineDto.getTKaufdatum();

					// PJ20714
					if (getTheJudgeFac().hatRecht(RechteFac.RECHT_LP_FINANCIAL_INFO_TYP_1, theClientDto)) {
						oZeile[REPORT_MASCHINENPRODUKTIVITAET_MASCHINE_ANSCHAFFUNGSKOSTEN] = maschineDto
								.getNAnschaffungskosten();
						oZeile[REPORT_MASCHINENPRODUKTIVITAET_MASCHINE_ABSCHREIBUNGSDAUER] = maschineDto
								.getIAbschreibungInMonaten();
						oZeile[REPORT_MASCHINENPRODUKTIVITAET_MASCHINE_VERZINSUNG] = maschineDto.getNVerzinsung();
						oZeile[REPORT_MASCHINENPRODUKTIVITAET_MASCHINE_ENERGIERKOSTEN] = maschineDto
								.getNEnergiekosten();
						oZeile[REPORT_MASCHINENPRODUKTIVITAET_MASCHINE_RAUMKOSTEN] = maschineDto.getNRaumkosten();
						oZeile[REPORT_MASCHINENPRODUKTIVITAET_MASCHINE_SONSTIGE_KOSTEN] = maschineDto
								.getNSonstigekosten();
						oZeile[REPORT_MASCHINENPRODUKTIVITAET_MASCHINE_PLANSTUNDEN] = maschineDto.getIPlanstunden();
					}

					oZeile[REPORT_MASCHINENPRODUKTIVITAET_MASCHINE_VERFUEGBARKEIT] = bdVerfuegbarkeitInStunden;

					try {

						LosDto losDto = null;

						if (maschinenzeitdatenDto.getLossollarbeitsplanIId() != null) {
							LossollarbeitsplanDto sollaDto;
							try {
								sollaDto = getFertigungFac().lossollarbeitsplanFindByPrimaryKey(
										maschinenzeitdatenDto.getLossollarbeitsplanIId());
								losDto = getFertigungFac().losFindByPrimaryKey(sollaDto.getLosIId());
							} catch (EJBExceptionLP e) {
								losDto = getFertigungFac().losFindByPrimaryKey(maschinenzeitdatenDto
										.getLosIId_WennBeiManuellerBedinungAufkeinenAGGebuchtwurde());
							}

						} else if (maschinenzeitdatenDto
								.getLosIId_WennBeiManuellerBedinungAufkeinenAGGebuchtwurde() != null) {
							losDto = getFertigungFac().losFindByPrimaryKey(
									maschinenzeitdatenDto.getLosIId_WennBeiManuellerBedinungAufkeinenAGGebuchtwurde());
						}

						if (losDto.getStuecklisteIId() != null) {
							com.lp.server.stueckliste.service.StuecklisteDto stuecklisteDto = getStuecklisteFac()
									.stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(), theClientDto);
							oZeile[REPORT_MASCHINENPRODUKTIVITAET_STUECKLISTE] = stuecklisteDto.getArtikelDto()
									.getCNr();
							oZeile[REPORT_MASCHINENPRODUKTIVITAET_STKLBEZEICHNUNG] = stuecklisteDto.getArtikelDto()
									.formatBezeichnung();

						} else {
							oZeile[REPORT_MASCHINENPRODUKTIVITAET_STUECKLISTE] = "Materialliste";
						}
						oZeile[REPORT_MASCHINENPRODUKTIVITAET_BELEG] = losDto.getCNr();

						oZeile[REPORT_MASCHINENPRODUKTIVITAET_FERTIGUNGSGRUPPE] = getStuecklisteFac()
								.fertigungsgruppeFindByPrimaryKey(losDto.getFertigungsgruppeIId()).getCBez();

						oZeile[REPORT_MASCHINENPRODUKTIVITAET_PROJEKT] = losDto.getCProjekt();
						if (losDto.getAuftragpositionIId() != null) {
							Integer auftragIId = getAuftragpositionFac()
									.auftragpositionFindByPrimaryKey(losDto.getAuftragpositionIId()).getBelegIId();
							com.lp.server.auftrag.service.AuftragDto auftragDto = getAuftragFac()
									.auftragFindByPrimaryKey(auftragIId);
							oZeile[REPORT_MASCHINENPRODUKTIVITAET_AUFTRAG] = auftragDto.getCNr();
							oZeile[REPORT_MASCHINENPRODUKTIVITAET_KUNDE] = auftragDto.getCNr();
							com.lp.server.partner.service.KundeDto kundeDto = getKundeFac()
									.kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(), theClientDto);

							oZeile[REPORT_MASCHINENPRODUKTIVITAET_KUNDE] = kundeDto.getPartnerDto().formatTitelAnrede();
						}

					} catch (RemoteException ex) {
						throwEJBExceptionLPRespectOld(ex);
					}

					MaschinenkostenDto mkDto = getZeiterfassungFac()
							.getMaschinenKostenDtoZumZeitpunkt(maschineDto.getIId(), maschinenzeitdatenDto.getTVon());

					BigDecimal bdKostenVK = BigDecimal.ZERO;
					BigDecimal bdKostenKalk = BigDecimal.ZERO;

					if (mkDto != null) {
						bdKostenVK = mkDto.getNVkstundensatz();
						bdKostenKalk = mkDto.getNStundensatz();
					}

					oZeile[REPORT_MASCHINENPRODUKTIVITAET_MACHINE_KALK_STUNDENSATZ] = bdKostenKalk;
					oZeile[REPORT_MASCHINENPRODUKTIVITAET_MACHINE_VK_STUNDENSATZ] = bdKostenVK;

					if (maschinenzeitdatenDto.getTBis() != null) {
						long l_zeitdec = maschinenzeitdatenDto.getTBis().getTime()
								- maschinenzeitdatenDto.getTVon().getTime();

						Double d = Helper.time2Double(new java.sql.Time(l_zeitdec - 3600000));

						oZeile[REPORT_MASCHINENPRODUKTIVITAET_DAUER] = d;
						oZeile[REPORT_MASCHINENPRODUKTIVITAET_KOSTEN_KALK] = bdKostenKalk
								.multiply(new BigDecimal(d.doubleValue()));
						oZeile[REPORT_MASCHINENPRODUKTIVITAET_KOSTEN_VK] = bdKostenVK
								.multiply(new BigDecimal(d.doubleValue()));
					}

					alDaten.add(oZeile);
				}

			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}

		}

		index = -1;

		data = new Object[alDaten.size()][REPORT_MASCHINENPRODUKTIVITAET_ANZAHL_SPALTEN];
		setData((Object[][]) alDaten.toArray(data));

		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("P_WAEHRUNG", theClientDto.getSMandantenwaehrung());
		parameter.put("T_VON", tVon);
		tBis.setTime(tBis.getTime() - 1000);
		parameter.put("T_BIS", tBis);

		initJRDS(parameter, ZeiterfassungReportFac.REPORT_MODUL, ZeiterfassungReportFac.REPORT_MASCHINENPRODUKTIVITAET,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();

	}

	public JasperPrintLP printMaschinenverwendung(Integer maschineIId, TheClientDto theClientDto) {
		sAktuellerReport = ZeiterfassungReportFac.REPORT_MASCHINENVERWENDUNG;

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();

		String sLocUI = Helper.locale2String(theClientDto.getLocUi());
		session.enableFilter("filterLocale").setParameter("paramLocale", sLocUI);

		String sQuery = "SELECT sap,aspr_stkl FROM FLRStuecklistearbeitsplan sap  LEFT OUTER JOIN sap.flrstueckliste.flrartikel.artikelsprset AS aspr_stkl WHERE sap.maschine_i_id="
				+ maschineIId;

		sQuery += " ORDER BY sap.flrstueckliste.flrartikel.c_nr,sap.i_arbeitsgang,sap.i_unterarbeitsgang ";

		Query query = session.createQuery(sQuery);

		List<?> resultList = query.list();

		Iterator<?> resultListIterator = resultList.iterator();
		int row = 0;

		ArrayList alDaten = new ArrayList();

		while (resultListIterator.hasNext()) {

			Object[] oQuery = (Object[]) resultListIterator.next();

			FLRStuecklistearbeitsplan ag = (FLRStuecklistearbeitsplan) oQuery[0];

			FLRArtikellistespr spr = (FLRArtikellistespr) oQuery[1];

			Object[] oZeile = new Object[REPORT_MASCHINENVERWENDUNG_ANZAHL_SPALTEN];

			oZeile[REPORT_MASCHINENVERWENDUNG_BELEGART] = LocaleFac.BELEGART_STUECKLISTE;

			oZeile[REPORT_MASCHINENVERWENDUNG_STUECKLISTE] = ag.getFlrstueckliste().getFlrartikel().getC_nr();
			if (spr != null) {

				oZeile[REPORT_MASCHINENVERWENDUNG_BEZEICHNUNG] = spr.getC_bez();
				oZeile[REPORT_MASCHINENVERWENDUNG_ZUSATZBEZEICHNUNG] = spr.getC_zbez();
			}

			oZeile[REPORT_MASCHINENVERWENDUNG_ARBEITSGANG] = ag.getI_arbeitsgang();
			oZeile[REPORT_MASCHINENVERWENDUNG_UNTERARBEITSGANG] = ag.getI_unterarbeitsgang();

			oZeile[REPORT_MASCHINENVERWENDUNG_AZ_ARTIKEL] = ag.getFlrartikel().getC_nr();

			ArtikelDto aDtoAZ = getArtikelFac().artikelFindByPrimaryKeySmall(ag.getFlrartikel().getI_id(),
					theClientDto);
			oZeile[REPORT_MASCHINENVERWENDUNG_AZ_ARTIKELBEZEICHNUNG] = aDtoAZ.getCBezAusSpr();

			double lStueckzeit = ag.getL_stueckzeit();
			oZeile[REPORT_MASCHINENVERWENDUNG_STUECKZEIT] = Helper
					.rundeKaufmaennisch(new BigDecimal(lStueckzeit / 3600000), 4);
			double lRuestzeit = ag.getL_ruestzeit();
			oZeile[REPORT_MASCHINENVERWENDUNG_RUESTZEIT] = Helper
					.rundeKaufmaennisch(new BigDecimal(lRuestzeit / 3600000), 4);

			alDaten.add(oZeile);
		}

		session.close();
		session = factory.openSession();
		sQuery = "SELECT ap FROM FLRAgstklarbeitsplan ap WHERE ap.maschine_i_id=" + maschineIId;

		sQuery += " ORDER BY ap.flragstkl.c_nr,ap.i_arbeitsgang,ap.i_unterarbeitsgang ";

		query = session.createQuery(sQuery);

		resultList = query.list();

		resultListIterator = resultList.iterator();

		while (resultListIterator.hasNext()) {

			FLRAgstklarbeitsplan ag = (FLRAgstklarbeitsplan) resultListIterator.next();

			Object[] oZeile = new Object[REPORT_MASCHINENVERWENDUNG_ANZAHL_SPALTEN];

			oZeile[REPORT_MASCHINENVERWENDUNG_BELEGART] = LocaleFac.BELEGART_AGSTUECKLISTE;

			oZeile[REPORT_MASCHINENVERWENDUNG_STUECKLISTE] = ag.getFlragstkl().getC_nr();

			oZeile[REPORT_MASCHINENVERWENDUNG_BEZEICHNUNG] = ag.getFlragstkl().getC_bez();

			oZeile[REPORT_MASCHINENVERWENDUNG_ARBEITSGANG] = ag.getI_arbeitsgang();
			oZeile[REPORT_MASCHINENVERWENDUNG_UNTERARBEITSGANG] = ag.getI_unterarbeitsgang();

			oZeile[REPORT_MASCHINENVERWENDUNG_AZ_ARTIKEL] = ag.getFlrartikel().getC_nr();

			ArtikelDto aDtoAZ = getArtikelFac().artikelFindByPrimaryKeySmall(ag.getFlrartikel().getI_id(),
					theClientDto);
			oZeile[REPORT_MASCHINENVERWENDUNG_AZ_ARTIKELBEZEICHNUNG] = aDtoAZ.getCBezAusSpr();

			double lStueckzeit = ag.getL_stueckzeit();
			oZeile[REPORT_MASCHINENVERWENDUNG_STUECKZEIT] = Helper
					.rundeKaufmaennisch(new BigDecimal(lStueckzeit / 3600000), 4);
			double lRuestzeit = ag.getL_ruestzeit();
			oZeile[REPORT_MASCHINENVERWENDUNG_RUESTZEIT] = Helper
					.rundeKaufmaennisch(new BigDecimal(lRuestzeit / 3600000), 4);

			alDaten.add(oZeile);
		}
		data = new Object[alDaten.size()][REPORT_MASCHINENVERWENDUNG_ANZAHL_SPALTEN];
		setData((Object[][]) alDaten.toArray(data));

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		MaschineDto maschineDto = getZeiterfassungFac().maschineFindByPrimaryKey(maschineIId);

		parameter.put("P_MASCHINE_BEZEICHNUNG", maschineDto.getCBez());
		parameter.put("P_MASCHINE_INVENTARNUMMER", maschineDto.getCInventarnummer());
		parameter.put("P_MASCHINE_IDENTIFIKATIONSNUMMER", maschineDto.getCIdentifikationsnr());
		parameter.put("P_MASCHINE_SERIENNUMMER", maschineDto.getCSeriennummer());
		parameter.put("P_MASCHINE_KAUFDATUM", maschineDto.getTKaufdatum());

		try {
			if (maschineDto.getMaschinengruppeIId() != null) {
				MaschinengruppeDto maschinengruppeDto = getZeiterfassungFac()
						.maschinengruppeFindByPrimaryKey(maschineDto.getMaschinengruppeIId());
				parameter.put("P_MASCHINE_MASCHINENGRUPPE", maschinengruppeDto.getCBez());
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		MaschinenStundensatzDto mstdDto=getZeiterfassungFac().getMaschinenKostenZumZeitpunkt(maschineIId,
				new Timestamp(System.currentTimeMillis()));;
		
		parameter.put("P_MASCHINE_KOSTEN", mstdDto.getBdStundensatz());

		parameter.put("P_MASCHINE_KOSTENVK",  mstdDto.getBdStundensatzVK());

		initJRDS(parameter, ZeiterfassungReportFac.REPORT_MODUL, ZeiterfassungReportFac.REPORT_MASCHINENVERWENDUNG,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();

	}

	public JasperPrintLP printMaschinenliste(java.sql.Timestamp tStichtag, boolean bMitVersteckten,
			boolean bMitBarcodes, TheClientDto theClientDto) {
		sAktuellerReport = ZeiterfassungReportFac.REPORT_MASCHINENLISTE;

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();

		org.hibernate.Criteria crit = session
				.createCriteria(com.lp.server.personal.fastlanereader.generated.FLRMaschine.class)
				.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));
		if (bMitVersteckten == false) {
			crit.add(Restrictions.eq(ZeiterfassungFac.FLR_MASCHINE_B_VERSTECKT, Helper.boolean2Short(false)));
		}
		if (tStichtag != null) {
			crit.add(Restrictions.or(Restrictions.le(ZeiterfassungFac.FLR_MASCHINE_T_KAUFDATUM, tStichtag),
					Restrictions.isNull(ZeiterfassungFac.FLR_MASCHINE_T_KAUFDATUM)));
		}

		crit.createAlias(ZeiterfassungFac.FLR_MASCHINE_FLR_MASCHINENGRUPPE, "m").addOrder(Order.asc("m.c_bez"));
		crit.addOrder(Order.asc(ZeiterfassungFac.FLR_MASCHINE_C_INVENTARNUMMER));

		List<?> resultList = crit.list();

		setData(new Object[resultList.size()][REPORT_MASCHINENLISTE_ANZAHL_SPALTEN]);

		Iterator<?> resultListIterator = resultList.iterator();
		int row = 0;
		while (resultListIterator.hasNext()) {
			FLRMaschine flrmaschine = (FLRMaschine) resultListIterator.next();

			data[row][REPORT_MASCHINENLISTE_INVENTARNUMMER] = flrmaschine.getC_inventarnummer();
			data[row][REPORT_MASCHINENLISTE_BEZEICHNUNG] = flrmaschine.getC_bez();
			data[row][REPORT_MASCHINENLISTE_IDENTIFIKATIONSNUMMER] = flrmaschine.getC_identifikationsnr();
			data[row][REPORT_MASCHINENLISTE_SERIENNUMMER] = flrmaschine.getC_seriennummer();
			data[row][REPORT_MASCHINENLISTE_KAUFDATUM] = flrmaschine.getT_kaufdatum();

			if (tStichtag != null) {
				data[row][REPORT_MASCHINENLISTE_VERFUEGBARKEIT] = getMaschineFac().getVerfuegbarkeitInStundenZuDatum(
						flrmaschine.getI_id(), new java.sql.Date(tStichtag.getTime()), theClientDto).doubleValue();
			} else {
				data[row][REPORT_MASCHINENLISTE_VERFUEGBARKEIT] = getMaschineFac()
						.getVerfuegbarkeitInStundenZuDatum(flrmaschine.getI_id(),
								new java.sql.Date(System.currentTimeMillis()), theClientDto)
						.doubleValue();
			}

			data[row][REPORT_MASCHINENLISTE_AUTOENDE] = new Boolean(
					Helper.short2Boolean(flrmaschine.getB_autoendebeigeht()));
			data[row][REPORT_MASCHINENLISTE_MANUELLE_BEDIENUNG] = new Boolean(
					Helper.short2Boolean(flrmaschine.getB_manuelle_bedienung()));

			MaschineDto mDto = getZeiterfassungFac().maschineFindByPrimaryKey(flrmaschine.getI_id());
			if (mDto.getArtikelIIdVerrechnen() != null) {

				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKey(mDto.getArtikelIIdVerrechnen(), theClientDto);
				data[row][REPORT_MASCHINENLISTE_VERRECHNUNGSARTIKEL] = aDto.getCNr();
				data[row][REPORT_MASCHINENLISTE_VERRECHNUNGSARTIKEL_BEZEICHNUNG] = aDto.getCBezAusSpr();
			}

			
			MaschinenStundensatzDto maschinenStundensatzDto=getZeiterfassungFac()
			.getMaschinenKostenZumZeitpunkt(flrmaschine.getI_id(), new Timestamp(System.currentTimeMillis()));
			
			data[row][REPORT_MASCHINENLISTE_KOSTEN_ZUM_ZEITPUNKT] = maschinenStundensatzDto.getBdStundensatz();

			data[row][REPORT_MASCHINENLISTE_KOSTEN_VK_ZUM_ZEITPUNKT] =maschinenStundensatzDto.getBdStundensatzVK();

			data[row][REPORT_MASCHINENLISTE_MASCHINENGRUPPE] = flrmaschine.getFlrmaschinengruppe().getC_bez();

			// PJ20714
			if (getTheJudgeFac().hatRecht(RechteFac.RECHT_LP_FINANCIAL_INFO_TYP_1, theClientDto)) {
				data[row][REPORT_MASCHINENLISTE_ANSCHAFFUNGSKOSTEN] = mDto.getNAnschaffungskosten();
				data[row][REPORT_MASCHINENLISTE_ABSCHREIBUNGSDAUER] = mDto.getIAbschreibungInMonaten();
				data[row][REPORT_MASCHINENLISTE_VERZINSUNG] = mDto.getNVerzinsung();
				data[row][REPORT_MASCHINENLISTE_ENERGIERKOSTEN] = mDto.getNEnergiekosten();
				data[row][REPORT_MASCHINENLISTE_RAUMKOSTEN] = mDto.getNRaumkosten();
				data[row][REPORT_MASCHINENLISTE_SONSTIGE_KOSTEN] = mDto.getNSonstigekosten();
				data[row][REPORT_MASCHINENLISTE_PLANSTUNDEN] = mDto.getIPlanstunden();
			}

			Session s2 = FLRSessionFactory.getFactory().openSession();
			Query query2 = s2.createQuery("FROM FLRMaschinenzeitdaten m WHERE m.maschine_i_id=" + flrmaschine.getI_id()
					+ " ORDER BY m.t_von DESC");
			query2.setMaxResults(1);
			List<?> resultListSub = query2.list();

			String starter = "";
			Timestamp tUm = null;

			if (resultListSub.size() > 0) {
				FLRMaschinenzeitdaten m = (FLRMaschinenzeitdaten) resultListSub.iterator().next();

				starter = m.getFlrpersonal_gestartet().getFlrpartner().getC_name1nachnamefirmazeile1();

				if (m.getFlrpersonal_gestartet().getFlrpartner().getC_name2vornamefirmazeile2() != null) {
					starter += " " + m.getFlrpersonal_gestartet().getFlrpartner().getC_name2vornamefirmazeile2();
				}
				tUm = new Timestamp(m.getT_von().getTime());
			}
			data[row][REPORT_MASCHINENLISTE_ZULETZT_GESTARTET_VON] = starter;
			data[row][REPORT_MASCHINENLISTE_ZULETZT_GESTARTET_UM] = tUm;

			row++;
		}

		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("T_STICHTAG", tStichtag);
		if (bMitBarcodes == true) {
			initJRDS(parameter, ZeiterfassungReportFac.REPORT_MODUL,
					ZeiterfassungReportFac.REPORT_MASCHINENLISTE_BARCODE, theClientDto.getMandant(),
					theClientDto.getLocUi(), theClientDto);
		} else {
			initJRDS(parameter, ZeiterfassungReportFac.REPORT_MODUL, ZeiterfassungReportFac.REPORT_MASCHINENLISTE,
					theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		}

		return getReportPrint();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printMaschinenbelegung(Integer maschineIId, java.sql.Timestamp tStichtag,
			boolean bMitErstemUagDesNaechstenAg, TheClientDto theClientDto) {

		sAktuellerReport = ZeiterfassungReportFac.REPORT_MASCHINENBELEGUNG;
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("P_STICHTAG", new Timestamp(tStichtag.getTime() - 3600000 * 24));

		Session session = FLRSessionFactory.getFactory().openSession();
		String sQuery = "SELECT l FROM FLRLossollarbeitsplan l WHERE l.flrlos.t_produktionsbeginn <='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tStichtag.getTime()))
				+ "' AND l.flrlos.status_c_nr IN ('" + LocaleFac.STATUS_ANGELEGT + "','" + LocaleFac.STATUS_AUSGEGEBEN
				+ "','" + LocaleFac.STATUS_IN_PRODUKTION + "','" + LocaleFac.STATUS_GESTOPPT + "','"
				+ LocaleFac.STATUS_TEILERLEDIGT + "') AND l.flrlos.mandant_c_nr='" + theClientDto.getMandant()
				+ "' AND l.maschine_i_id IS NOT NULL ";

		if (maschineIId != null) {
			sQuery += " AND l.maschine_i_id=" + maschineIId;
		}

		sQuery += " ORDER BY l.flrmaschine.flrmaschinengruppe.c_bez, l.flrmaschine.c_inventarnummer,l.flrlos.t_produktionsbeginn,l.flrlos.c_nr,l.i_arbeitsgangsnummer,l.i_unterarbeitsgang ";

		Query query = session.createQuery(sQuery);
		List<?> resultList = query.list();

		Iterator<?> resultListIterator = resultList.iterator();
		int row = 0;

		ArrayList maschinen = new ArrayList();

		while (resultListIterator.hasNext()) {
			FLRLossollarbeitsplan flrlossollarbeitsplan = (FLRLossollarbeitsplan) resultListIterator.next();

			Timestamp tDatum = new Timestamp(flrlossollarbeitsplan.getFlrlos().getT_produktionsbeginn().getTime());

			if (flrlossollarbeitsplan.getI_maschinenversatztage() != null) {
				tDatum = Helper.addiereTageZuTimestamp(tDatum, flrlossollarbeitsplan.getI_maschinenversatztage());
			}

			if (tDatum.after(tStichtag)) {

				continue;
			}

			if (!maschinen.contains(flrlossollarbeitsplan.getFlrmaschine().getI_id())) {
				maschinen.add(flrlossollarbeitsplan.getFlrmaschine().getI_id());
			}

			row++;
		}

		ArrayList alDaten = new ArrayList();
		for (int j = 0; j < maschinen.size(); j++) {

			MaschineDto maschineDto = null;
			MaschinengruppeDto maschinengruppeDto = null;
			try {
				maschineDto = getZeiterfassungFac().maschineFindByPrimaryKey((Integer) maschinen.get(j));

				if (maschineDto.getMaschinengruppeIId() != null) {
					maschinengruppeDto = getZeiterfassungFac()
							.maschinengruppeFindByPrimaryKey(maschineDto.getMaschinengruppeIId());
				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			int iAnzahlTage = Helper.getDifferenzInTagen(Helper.cutTimestamp(new Timestamp(System.currentTimeMillis())),
					tStichtag);
			if (iAnzahlTage <= 0) {
				iAnzahlTage = 1;
			}
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(System.currentTimeMillis());

			for (int i = 0; i < iAnzahlTage; i++) {

				Object[] oZeileTag = new Object[REPORT_MASCHINENBELEGUNG_ANZAHL_SPALTEN];
				oZeileTag[REPORT_MASCHINENBELEGUNG_DATUM] = Helper.cutTimestamp(new Timestamp(c.getTimeInMillis()));
				oZeileTag[REPORT_MASCHINENBELEGUNG_BEZEICHNUNG] = maschineDto.getCBez();
				oZeileTag[REPORT_MASCHINENBELEGUNG_IDENTIFIKATIONSNUMMER] = maschineDto.getCIdentifikationsnr();
				oZeileTag[REPORT_MASCHINENBELEGUNG_SERIENNUMMER] = maschineDto.getCSeriennummer();
				oZeileTag[REPORT_MASCHINENBELEGUNG_INVENTARNUMMER] = maschineDto.getCInventarnummer();
				oZeileTag[REPORT_MASCHINENBELEGUNG_VERSTECKT] = Helper.short2Boolean(maschineDto.getBVersteckt());

				double dVerfuegbarkeit = getMaschineFac().getVerfuegbarkeitInStundenZuDatum(maschineDto.getIId(),
						new java.sql.Date(c.getTimeInMillis()), theClientDto).doubleValue();

				oZeileTag[REPORT_MASCHINENBELEGUNG_VERFUEGBARKEIT] = dVerfuegbarkeit;
				if (maschinengruppeDto != null) {
					oZeileTag[REPORT_MASCHINENBELEGUNG_MASCHINENGRUPPE] = maschinengruppeDto.getCBez();
				}

				resultListIterator = resultList.iterator();

				boolean bEsGibtEinenEintrag = false;

				while (resultListIterator.hasNext()) {

					oZeileTag = new Object[REPORT_MASCHINENBELEGUNG_ANZAHL_SPALTEN];
					oZeileTag[REPORT_MASCHINENBELEGUNG_DATUM] = Helper.cutTimestamp(new Timestamp(c.getTimeInMillis()));
					oZeileTag[REPORT_MASCHINENBELEGUNG_BEZEICHNUNG] = maschineDto.getBezeichnung();
					oZeileTag[REPORT_MASCHINENBELEGUNG_IDENTIFIKATIONSNUMMER] = maschineDto.getCIdentifikationsnr();
					oZeileTag[REPORT_MASCHINENBELEGUNG_SERIENNUMMER] = maschineDto.getCSeriennummer();
					oZeileTag[REPORT_MASCHINENBELEGUNG_VERSTECKT] = Helper.short2Boolean(maschineDto.getBVersteckt());
					oZeileTag[REPORT_MASCHINENBELEGUNG_INVENTARNUMMER] = maschineDto.getCInventarnummer();

					FLRLossollarbeitsplan flrlossollarbeitsplan = (FLRLossollarbeitsplan) resultListIterator.next();

					oZeileTag[REPORT_MASCHINENBELEGUNG_VERFUEGBARKEIT] = dVerfuegbarkeit;

					if (maschinengruppeDto != null) {
						oZeileTag[REPORT_MASCHINENBELEGUNG_MASCHINENGRUPPE] = maschinengruppeDto.getCBez();
					}

					Timestamp tDatum = new Timestamp(
							flrlossollarbeitsplan.getFlrlos().getT_produktionsbeginn().getTime());

					if (flrlossollarbeitsplan.getI_maschinenversatztage() != null) {
						tDatum = Helper.addiereTageZuTimestamp(tDatum,
								flrlossollarbeitsplan.getI_maschinenversatztage());
					}

					if (Helper.cutTimestamp(tDatum).getTime() < Helper
							.cutTimestamp(new Timestamp(System.currentTimeMillis())).getTime()) {
						tDatum = Helper.cutTimestamp(new Timestamp(System.currentTimeMillis()));
					}

					if (Helper.cutTimestamp(tDatum).getTime() == Helper.cutTimestamp(new Timestamp(c.getTimeInMillis()))
							.getTime()) {
						if (maschinen.get(j).equals(flrlossollarbeitsplan.getFlrmaschine().getI_id())) {

							oZeileTag[REPORT_MASCHINENBELEGUNG_LOSNUMMER] = flrlossollarbeitsplan.getFlrlos().getC_nr();

							oZeileTag[REPORT_MASCHINENBELEGUNG_FERTIGUNGSGRUPPE] = flrlossollarbeitsplan.getFlrlos()
									.getFlrfertigungsgruppe().getC_bez();

							oZeileTag[REPORT_MASCHINENBELEGUNG_LOSENDE] = new Timestamp(
									flrlossollarbeitsplan.getFlrlos().getT_produktionsende().getTime());
							if (flrlossollarbeitsplan.getFlrlos().getT_produktionsstop() != null) {
								oZeileTag[REPORT_MASCHINENBELEGUNG_PRODUKTIONSSTOP] = new Timestamp(
										flrlossollarbeitsplan.getFlrlos().getT_produktionsstop().getTime());
							}
							oZeileTag[REPORT_MASCHINENBELEGUNG_SOLL] = flrlossollarbeitsplan.getN_gesamtzeit();

							BigDecimal ruestzeit = flrlossollarbeitsplan.getL_ruestzeit()
									.divide(new BigDecimal(3600000), 4, BigDecimal.ROUND_HALF_EVEN);

							oZeileTag[REPORT_MASCHINENBELEGUNG_RUESTZEIT] = ruestzeit;

							BigDecimal stueckzeit = flrlossollarbeitsplan.getL_stueckzeit()
									.divide(new BigDecimal(3600000), 4, BigDecimal.ROUND_HALF_EVEN);
							oZeileTag[REPORT_MASCHINENBELEGUNG_STUECKZEIT] = stueckzeit;

							try {

								if (Helper.short2boolean(flrlossollarbeitsplan.getB_fertig())) {
									oZeileTag[REPORT_MASCHINENBELEGUNG_SOLL] = new BigDecimal(0);
								} else {
									BigDecimal[] bdGutSchlechtInarbeit = getFertigungFac()
											.getGutSchlechtInarbeit(flrlossollarbeitsplan.getI_id(), theClientDto);

									BigDecimal bdOffen = bdGutSchlechtInarbeit[3];
									if (bdOffen != null) {
										oZeileTag[REPORT_MASCHINENBELEGUNG_SOLL] = ruestzeit
												.add(stueckzeit.multiply(bdOffen));
									}

								}

							} catch (RemoteException e) {
								throwEJBExceptionLPRespectOld(e);
							}

							oZeileTag[REPORT_MASCHINENBELEGUNG_ARBEITSGANG] = flrlossollarbeitsplan
									.getI_arbeitsgangsnummer();
							oZeileTag[REPORT_MASCHINENBELEGUNG_UNTERARBEITSGANG] = flrlossollarbeitsplan
									.getI_unterarbeitsgang();

							oZeileTag[REPORT_MASCHINENBELEGUNG_POSITIONSKOMMENTAR] = flrlossollarbeitsplan
									.getC_kommentar();
							oZeileTag[REPORT_MASCHINENBELEGUNG_FERTIG] = Helper
									.short2boolean(flrlossollarbeitsplan.getB_fertig());
							oZeileTag[REPORT_MASCHINENBELEGUNG_ARTIKEL] = flrlossollarbeitsplan.getFlrartikel()
									.getC_nr();

							oZeileTag[REPORT_MASCHINENBELEGUNG_PROJEKT] = flrlossollarbeitsplan.getFlrlos()
									.getC_projekt();

							if (bMitErstemUagDesNaechstenAg) {

								// PJ 16122
								LossollarbeitsplanDto lossollarbeitsplanDtoNaechsterHauptsarbeitsgang = getFertigungFac()
										.lossollarbeitsplanfindByLosIIdIArbeitsgangnummerNaechsterHauptarbeitsgang(
												flrlossollarbeitsplan.getLos_i_id(),
												flrlossollarbeitsplan.getI_arbeitsgangsnummer());

								if (lossollarbeitsplanDtoNaechsterHauptsarbeitsgang != null
										&& lossollarbeitsplanDtoNaechsterHauptsarbeitsgang.getMaschineIId() != null) {
									MaschineDto naechschterAGDto = getZeiterfassungFac().maschineFindByPrimaryKey(
											lossollarbeitsplanDtoNaechsterHauptsarbeitsgang.getMaschineIId());

									oZeileTag[REPORT_MASCHINENBELEGUNG_UAG_NAECHSTER_AG_MASCHINE_BEZEICHNUNG] = naechschterAGDto
											.getCBez();
									oZeileTag[REPORT_MASCHINENBELEGUNG_UAG_NAECHSTER_AG_MASCHINE_IDENTIFIKATIONSNUMMER] = naechschterAGDto
											.getCIdentifikationsnr();
									oZeileTag[REPORT_MASCHINENBELEGUNG_UAG_NAECHSTER_AG_MASCHINE_INVENTARNUMMER] = naechschterAGDto
											.getCInventarnummer();
								}
							}

							Session sessionLosklasse = FLRSessionFactory.getFactory().openSession();
							String queryLosklasse = "FROM FLRLoslosklasse l where l.los_i_id="
									+ flrlossollarbeitsplan.getFlrlos().getI_id();

							org.hibernate.Query loslosklasse = sessionLosklasse.createQuery(queryLosklasse);

							List resultListLosklasse = loslosklasse.list();

							Iterator resultListIteratorLosklasse = resultListLosklasse.iterator();

							String losklassen = "";
							while (resultListIteratorLosklasse.hasNext()) {
								com.lp.server.fertigung.fastlanereader.generated.FLRLoslosklasse lk = (com.lp.server.fertigung.fastlanereader.generated.FLRLoslosklasse) resultListIteratorLosklasse
										.next();

								losklassen += lk.getFlrlosklasse().getC_nr() + ",";

							}

							oZeileTag[REPORT_MASCHINENBELEGUNG_LOSKLASSEN] = losklassen;

							sessionLosklasse.close();

							try {
								ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(
										flrlossollarbeitsplan.getFlrartikel().getI_id(), theClientDto);
								if (artikelDto.getArtikelsprDto() != null) {
									oZeileTag[REPORT_MASCHINENBELEGUNG_ARTIKELBEZEICHNUNG] = artikelDto
											.getArtikelsprDto().getCBez();
								}
								LosDto losDto = getFertigungFac()
										.losFindByPrimaryKey(flrlossollarbeitsplan.getFlrlos().getI_id());
								oZeileTag[REPORT_MASCHINENBELEGUNG_KOMMENTAR] = losDto.getCKommentar();

								if (flrlossollarbeitsplan.getFlrlos().getFlrauftrag() != null) {
									oZeileTag[REPORT_MASCHINENBELEGUNG_KUNDE_NAME] = flrlossollarbeitsplan.getFlrlos()
											.getFlrauftrag().getFlrkunde().getFlrpartner()
											.getC_name1nachnamefirmazeile1();

									if (flrlossollarbeitsplan.getFlrlos().getFlrauftrag().getFlrkunde().getFlrpartner()
											.getFlrlandplzort() != null) {
										oZeileTag[REPORT_MASCHINENBELEGUNG_KUNDE_LKZ] = flrlossollarbeitsplan
												.getFlrlos().getFlrauftrag().getFlrkunde().getFlrpartner()
												.getFlrlandplzort().getFlrland().getC_lkz();
										oZeileTag[REPORT_MASCHINENBELEGUNG_KUNDE_PLZ] = flrlossollarbeitsplan
												.getFlrlos().getFlrauftrag().getFlrkunde().getFlrpartner()
												.getFlrlandplzort().getC_plz();
										oZeileTag[REPORT_MASCHINENBELEGUNG_KUNDE_ORT] = flrlossollarbeitsplan
												.getFlrlos().getFlrauftrag().getFlrkunde().getFlrpartner()
												.getFlrlandplzort().getFlrort().getC_name();
									}
								} else {
									if (flrlossollarbeitsplan.getFlrlos().getFlrkunde() != null) {
										oZeileTag[REPORT_MASCHINENBELEGUNG_KUNDE_NAME] = flrlossollarbeitsplan
												.getFlrlos().getFlrkunde().getFlrpartner()
												.getC_name1nachnamefirmazeile1();

										if (flrlossollarbeitsplan.getFlrlos().getFlrkunde().getFlrpartner()
												.getFlrlandplzort() != null) {
											oZeileTag[REPORT_MASCHINENBELEGUNG_KUNDE_LKZ] = flrlossollarbeitsplan
													.getFlrlos().getFlrkunde().getFlrpartner().getFlrlandplzort()
													.getFlrland().getC_lkz();
											oZeileTag[REPORT_MASCHINENBELEGUNG_KUNDE_PLZ] = flrlossollarbeitsplan
													.getFlrlos().getFlrkunde().getFlrpartner().getFlrlandplzort()
													.getC_plz();
											oZeileTag[REPORT_MASCHINENBELEGUNG_KUNDE_ORT] = flrlossollarbeitsplan
													.getFlrlos().getFlrkunde().getFlrpartner().getFlrlandplzort()
													.getFlrort().getC_name();
										}
									}
								}

								if (flrlossollarbeitsplan.getFlrlos().getFlrstueckliste() != null) {
									ArtikelDto artikelDtoStkl = getArtikelFac().artikelFindByPrimaryKeySmall(
											flrlossollarbeitsplan.getFlrlos().getFlrstueckliste().getArtikel_i_id(),
											theClientDto);

									oZeileTag[REPORT_MASCHINENBELEGUNG_STUECKLISTE] = artikelDtoStkl.getCNr();
									oZeileTag[REPORT_MASCHINENBELEGUNG_STUECKLISTEREFERENZNUMMER] = artikelDtoStkl
											.getCReferenznr();

									if (artikelDto.getArtikelsprDto() != null) {
										oZeileTag[REPORT_MASCHINENBELEGUNG_STUECKLISTEBEZEICHNUNG] = artikelDtoStkl
												.getArtikelsprDto().getCBez();
										oZeileTag[REPORT_MASCHINENBELEGUNG_STUECKLISTEKURZBEZEICHNUNG] = artikelDtoStkl
												.getArtikelsprDto().getCKbez();
										oZeileTag[REPORT_MASCHINENBELEGUNG_STUECKLISTEZUSATZBEZEICHNUNG] = artikelDtoStkl
												.getArtikelsprDto().getCZbez();
										oZeileTag[REPORT_MASCHINENBELEGUNG_STUECKLISTEZUSATZBEZEICHNUNG2] = artikelDtoStkl
												.getArtikelsprDto().getCZbez2();
									}
								}

							} catch (RemoteException e) {
								throwEJBExceptionLPRespectOld(e);
							}

							oZeileTag[REPORT_MASCHINENBELEGUNG_LOSGROESSE] = flrlossollarbeitsplan.getFlrlos()
									.getN_losgroesse();

							alDaten.add(oZeileTag);
							bEsGibtEinenEintrag = true;
						}

					}
				}

				if (bEsGibtEinenEintrag == false) {
					alDaten.add(oZeileTag);
				}

				c.set(Calendar.DATE, c.get(Calendar.DATE) + 1);

			}

		}
		session.close();

		data = new Object[alDaten.size()][REPORT_MASCHINENBELEGUNG_ANZAHL_SPALTEN];
		// data = (Object[][]) alDaten.toArray(data);
		setData((Object[][]) alDaten.toArray(data));

		initJRDS(parameter, ZeiterfassungReportFac.REPORT_MODUL, ZeiterfassungReportFac.REPORT_MASCHINENBELEGUNG,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();
	}

	public JasperPrintLP printAbgeschlosseneZeitbuchungen(boolean bMitVersteckten, TheClientDto theClientDto) {

		sAktuellerReport = ZeiterfassungReportFac.REPORT_ABGESCHLOSSENE_ZEITBUCHUNGEN;
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("P_MITVERSTECKTEN", new Boolean(bMitVersteckten));
		try {
			PersonalDto[] pDtos = getPersonalFac().personalFindByMandantCNr(theClientDto.getMandant(), bMitVersteckten);

			// data = new
			// Object[pDtos.length][REPORT_ZEITABSCHLUSS_ANZAHL_SPALTEN];
			setData(new Object[pDtos.length][REPORT_ZEITABSCHLUSS_ANZAHL_SPALTEN]);

			for (int i = 0; i < pDtos.length; i++) {
				PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(pDtos[i].getPartnerIId(), theClientDto);

				data[i][REPORT_ZEITABSCHLUSS_NAME] = partnerDto.getCName1nachnamefirmazeile1();
				data[i][REPORT_ZEITABSCHLUSS_VORNAME] = partnerDto.getCName2vornamefirmazeile2();
				data[i][REPORT_ZEITABSCHLUSS_TITEL] = partnerDto.getCTitel();

				data[i][REPORT_ZEITABSCHLUSS_PERSONALNR] = pDtos[i].getCPersonalnr();

				Session session = FLRSessionFactory.getFactory().openSession();
				String sQuery = "SELECT za FROM FLRZeitabschluss za WHERE za.personal_i_id=" + pDtos[i].getIId()
						+ " ORDER BY t_abgeschlossen_bis DESC";

				Query query = session.createQuery(sQuery);
				query.setMaxResults(1);
				List<?> resultList = query.list();

				Iterator<?> resultListIterator = resultList.iterator();

				if (resultListIterator.hasNext()) {
					FLRZeitabschluss flrZeitabschluss = (FLRZeitabschluss) resultListIterator.next();

					data[i][REPORT_ZEITABSCHLUSS_ZEITEN_ABGESCHLOSSEN_BIS] = flrZeitabschluss.getT_abgeschlossen_bis();

				}

				session.close();

			}
		} catch (RemoteException e2) {
			throwEJBExceptionLPRespectOld(e2);
		}

		initJRDS(parameter, ZeiterfassungReportFac.REPORT_MODUL,
				ZeiterfassungReportFac.REPORT_ABGESCHLOSSENE_ZEITBUCHUNGEN, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);
		return getReportPrint();
	}

	public JasperPrintLP printMitarbeitereinteilung(Integer personalIId, Integer personalgruppeIId,
			java.sql.Timestamp tStichtag, Integer iOptionSortierung, String sortierung, TheClientDto theClientDto) {

		sAktuellerReport = ZeiterfassungReportFac.REPORT_MITARBEITEREINTEILUNG;
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("P_STICHTAG", new Timestamp(tStichtag.getTime() - 3600000 * 24));

		Session session = FLRSessionFactory.getFactory().openSession();
		String sQuery = "SELECT l FROM FLRLossollarbeitsplan l WHERE l.flrlos.t_produktionsbeginn <='"
				+ Helper.formatDateWithSlashes(new java.sql.Date(tStichtag.getTime()))
				+ "' AND l.flrlos.status_c_nr IN ('" + LocaleFac.STATUS_ANGELEGT + "','"
				+ LocaleFac.STATUS_IN_PRODUKTION + "','" + LocaleFac.STATUS_TEILERLEDIGT + "') ";
		if (personalIId != null) {
			sQuery += " AND l.personal_i_id_zugeordneter=" + personalIId;
		} else {
			if (personalgruppeIId != null) {
				sQuery += " AND l.flrpersonal_zugeordneter.personalgruppe_i_id=" + personalgruppeIId;
			}
		}

		PersonalDto[] personalDtos = null;

		try {

			if (personalIId != null) {
				personalDtos = new PersonalDto[1];
				personalDtos[0] = getPersonalFac().personalFindByPrimaryKey(personalIId, theClientDto);
			} else {

				if (personalgruppeIId != null) {
					personalDtos = getPersonalFac().personalFindByPersonalgruppeIdMandantCNr(personalgruppeIId,
							theClientDto.getMandant(), false);
				} else {
					personalDtos = getPersonalFac().personalFindByMandantCNr(theClientDto.getMandant(), false);
				}

			}
		} catch (RemoteException e2) {
			throwEJBExceptionLPRespectOld(e2);
		}

		// PJ 17333

		parameter.put("P_SORTIERUNG", sortierung);

		if (iOptionSortierung != ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_PERSONALNUMMER) {
			for (int i = personalDtos.length - 1; i > 0; --i) {
				for (int j = 0; j < i; ++j) {
					String vergleich1 = "";
					String vergleich2 = "";

					String kostenstelle1 = "               ";
					String kostenstelle2 = "               ";

					if (iOptionSortierung == ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_KOSTENSTELLE_NAME_VORNAME
							|| iOptionSortierung == ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_ABTEILUNG_KOSTENSTELLE_NAME_VORNAME) {
						if (personalDtos[j].getKostenstelleIIdStamm() != null) {

							KostenstelleDto kstDto = getSystemFac()
									.kostenstelleFindByPrimaryKey(personalDtos[j].getKostenstelleIIdStamm());
							kostenstelle1 = Helper.fitString2Length(kstDto.getCNr(), 15, ' ');

						}
						if (personalDtos[j + 1].getKostenstelleIIdStamm() != null) {
							KostenstelleDto kstDto = getSystemFac()
									.kostenstelleFindByPrimaryKey(personalDtos[j + 1].getKostenstelleIIdStamm());
							kostenstelle2 = Helper.fitString2Length(kstDto.getCNr(), 15, ' ');

						}

						if (iOptionSortierung == ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_KOSTENSTELLE_NAME_VORNAME) {
							vergleich1 += kostenstelle1;
							vergleich2 += kostenstelle2;
						}

					}

					if (iOptionSortierung == ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_ABTEILUNG_NAME_VORNAME
							|| iOptionSortierung == ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_ABTEILUNG_KOSTENSTELLE_NAME_VORNAME) {

						String abteilung1 = "               ";
						String abteilung2 = "               ";

						if (personalDtos[j].getKostenstelleIIdAbteilung() != null) {

							KostenstelleDto kstDto = getSystemFac()
									.kostenstelleFindByPrimaryKey(personalDtos[j].getKostenstelleIIdAbteilung());
							abteilung1 = Helper.fitString2Length(kstDto.getCNr(), 15, ' ');

						} else {
							abteilung1 = "               ";
						}
						if (personalDtos[j + 1].getKostenstelleIIdAbteilung() != null) {
							KostenstelleDto kstDto = getSystemFac()
									.kostenstelleFindByPrimaryKey(personalDtos[j + 1].getKostenstelleIIdAbteilung());
							abteilung2 = Helper.fitString2Length(kstDto.getCNr(), 15, ' ');

						} else {
							abteilung2 = "               ";
						}
						if (iOptionSortierung == ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_ABTEILUNG_KOSTENSTELLE_NAME_VORNAME) {
							vergleich1 = kostenstelle1 + vergleich1;
							vergleich2 = kostenstelle2 + vergleich2;
						}
						vergleich1 = abteilung1 + vergleich1;
						vergleich2 = abteilung2 + vergleich2;

					}

					PartnerDto p1Dto = getPartnerFac().partnerFindByPrimaryKey(personalDtos[j].getPartnerIId(),
							theClientDto);
					personalDtos[j].setPartnerDto(p1Dto);
					PartnerDto p2Dto = getPartnerFac().partnerFindByPrimaryKey(personalDtos[j + 1].getPartnerIId(),
							theClientDto);
					vergleich1 += Helper.fitString2Length(p1Dto.getCName1nachnamefirmazeile1(), 80, ' ');
					vergleich2 += Helper.fitString2Length(p2Dto.getCName1nachnamefirmazeile1(), 80, ' ');

					if (p1Dto.getCName2vornamefirmazeile2() != null) {
						vergleich1 += p1Dto.getCName2vornamefirmazeile2();
					}
					if (p2Dto.getCName2vornamefirmazeile2() != null) {
						vergleich2 += p2Dto.getCName2vornamefirmazeile2();
					}

					if (vergleich1.compareTo(vergleich2) > 0) {
						PersonalDto tauschDto = personalDtos[j];
						personalDtos[j] = personalDtos[j + 1];
						personalDtos[j + 1] = tauschDto;
					}

				}
			}
		}

		sQuery += " AND l.flrlos.mandant_c_nr='" + theClientDto.getMandant()
				+ "' AND l.personal_i_id_zugeordneter IS NOT NULL ORDER BY l.flrmaschine.c_inventarnummer,l.flrlos.t_produktionsbeginn";

		Query query = session.createQuery(sQuery);
		List<?> resultList = query.list();

		Iterator<?> resultListIterator = resultList.iterator();
		int row = 0;

		java.sql.Timestamp tSpaetestesDatum = tStichtag;

		while (resultListIterator.hasNext()) {
			FLRLossollarbeitsplan flrlossollarbeitsplan = (FLRLossollarbeitsplan) resultListIterator.next();

			Timestamp tDatum = new Timestamp(flrlossollarbeitsplan.getFlrlos().getT_produktionsbeginn().getTime());

			if (flrlossollarbeitsplan.getI_maschinenversatztage() != null) {
				tDatum = Helper.addiereTageZuTimestamp(tDatum, flrlossollarbeitsplan.getI_maschinenversatztage());
			}

			if (tDatum.after(tSpaetestesDatum)) {

				tSpaetestesDatum = tDatum;
			}

			row++;
		}
		Integer tagesartIId_Feiertag = null;
		Integer tagesartIId_Halbtag = null;

		tagesartIId_Feiertag = getZeiterfassungFac().tagesartFindByCNr(ZeiterfassungFac.TAGESART_FEIERTAG, theClientDto)
				.getIId();
		tagesartIId_Halbtag = getZeiterfassungFac().tagesartFindByCNr(ZeiterfassungFac.TAGESART_HALBTAG, theClientDto)
				.getIId();

		ArrayList alDaten = new ArrayList();
		// data = new Object[personalDtos.length][5];
		setData(new Object[personalDtos.length][5]);

		for (int j = 0; j < personalDtos.length; j++) {

			PersonalDto personalDto = personalDtos[j];

			personalDto
					.setPartnerDto(getPartnerFac().partnerFindByPrimaryKey(personalDto.getPartnerIId(), theClientDto));

			int iAnzahlTage = Helper.getDifferenzInTagen(Helper.cutTimestamp(new Timestamp(System.currentTimeMillis())),
					tSpaetestesDatum);

			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(System.currentTimeMillis());

			String abteilung = "";
			if (personalDto.getKostenstelleIIdAbteilung() != null) {
				abteilung = getSystemFac().kostenstelleFindByPrimaryKey(personalDto.getKostenstelleIIdAbteilung())
						.formatKostenstellenbezeichnung();
			}

			String kostenstelle = "";
			if (personalDto.getKostenstelleIIdStamm() != null) {
				kostenstelle = getSystemFac().kostenstelleFindByPrimaryKey(personalDto.getKostenstelleIIdStamm())
						.formatKostenstellenbezeichnung();
			}

			for (int i = 0; i < iAnzahlTage; i++) {
				Object[] oZeileTag = new Object[REPORT_MITARBEITEREINTEILUNG_ANZAHL_SPALTEN];
				oZeileTag[REPORT_MITARBEITEREINTEILUNG_PERSONAL] = personalDto.formatAnrede();
				oZeileTag[REPORT_MITARBEITEREINTEILUNG_PERSONALNUMMER] = personalDto.getCPersonalnr();

				oZeileTag[REPORT_MITARBEITEREINTEILUNG_KOSTENSTELLE] = kostenstelle;
				oZeileTag[REPORT_MITARBEITEREINTEILUNG_ABTEILUNG] = abteilung;

				oZeileTag[REPORT_MITARBEITEREINTEILUNG_DATUM] = Helper.cutTimestamp(new Timestamp(c.getTimeInMillis()));

				BigDecimal tagessoll = getZeiterfassungFac().getSollzeitEinerPersonUndEinesTages(personalDto,
						tagesartIId_Feiertag, tagesartIId_Halbtag, new Timestamp(c.getTimeInMillis()), theClientDto);

				oZeileTag[REPORT_MITARBEITEREINTEILUNG_TAGESSOLL] = tagessoll;

				resultListIterator = resultList.iterator();

				boolean bEsGibtEinenEintrag = false;
				while (resultListIterator.hasNext()) {
					FLRLossollarbeitsplan flrlossollarbeitsplan = (FLRLossollarbeitsplan) resultListIterator.next();

					Timestamp tDatum = new Timestamp(
							flrlossollarbeitsplan.getFlrlos().getT_produktionsbeginn().getTime());

					if (flrlossollarbeitsplan.getI_maschinenversatztage() != null) {
						tDatum = Helper.addiereTageZuTimestamp(tDatum,
								flrlossollarbeitsplan.getI_maschinenversatztage());
					}

					if (Helper.cutTimestamp(tDatum).getTime() < Helper
							.cutTimestamp(new Timestamp(System.currentTimeMillis())).getTime()) {
						tDatum = Helper.cutTimestamp(new Timestamp(System.currentTimeMillis()));
					}

					if (Helper.cutTimestamp(tDatum).getTime() == Helper.cutTimestamp(new Timestamp(c.getTimeInMillis()))
							.getTime()) {
						if (personalDto.getIId().equals(flrlossollarbeitsplan.getPersonal_i_id_zugeordneter())) {
							oZeileTag = new Object[REPORT_MITARBEITEREINTEILUNG_ANZAHL_SPALTEN];

							oZeileTag[REPORT_MITARBEITEREINTEILUNG_PERSONAL] = personalDto.formatAnrede();
							oZeileTag[REPORT_MITARBEITEREINTEILUNG_KOSTENSTELLE] = kostenstelle;
							oZeileTag[REPORT_MITARBEITEREINTEILUNG_ABTEILUNG] = abteilung;
							oZeileTag[REPORT_MITARBEITEREINTEILUNG_PERSONALNUMMER] = personalDto.getCPersonalnr();
							oZeileTag[REPORT_MITARBEITEREINTEILUNG_LOSNUMMER] = flrlossollarbeitsplan.getFlrlos()
									.getC_nr();
							oZeileTag[REPORT_MITARBEITEREINTEILUNG_ARBEITSGANG] = flrlossollarbeitsplan
									.getI_arbeitsgangsnummer();
							oZeileTag[REPORT_MITARBEITEREINTEILUNG_UNTERARBEITSGANG] = flrlossollarbeitsplan
									.getI_unterarbeitsgang();
							oZeileTag[REPORT_MITARBEITEREINTEILUNG_DATUM] = Helper
									.cutTimestamp(new Timestamp(c.getTimeInMillis()));
							oZeileTag[REPORT_MITARBEITEREINTEILUNG_TAGESSOLL] = tagessoll;
							BigDecimal laufzeit = new BigDecimal(0);
							BigDecimal umspannzeit = new BigDecimal(0);
							if (flrlossollarbeitsplan.getAgart_c_nr() != null) {

								if (flrlossollarbeitsplan.getAgart_c_nr().equals(StuecklisteFac.AGART_LAUFZEIT)) {
									laufzeit = flrlossollarbeitsplan.getN_gesamtzeit();
								}

								if (flrlossollarbeitsplan.getAgart_c_nr().equals(StuecklisteFac.AGART_UMSPANNZEIT)) {
									umspannzeit = flrlossollarbeitsplan.getN_gesamtzeit();
								}
							} else {
								oZeileTag[REPORT_MITARBEITEREINTEILUNG_SOLL] = flrlossollarbeitsplan.getN_gesamtzeit();
							}
							oZeileTag[REPORT_MITARBEITEREINTEILUNG_FERTIG] = Helper
									.short2boolean(flrlossollarbeitsplan.getB_fertig());

							oZeileTag[REPORT_MITARBEITEREINTEILUNG_ARTIKEL] = flrlossollarbeitsplan.getFlrartikel()
									.getC_nr();
							if (flrlossollarbeitsplan.getFlrmaschine() != null) {
								oZeileTag[REPORT_MITARBEITEREINTEILUNG_MASCHINE] = flrlossollarbeitsplan
										.getFlrmaschine().getC_inventarnummer();
							}
							ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(
									flrlossollarbeitsplan.getFlrartikel().getI_id(), theClientDto);

							if (artikelDto.getArtikelsprDto() != null) {
								oZeileTag[REPORT_MITARBEITEREINTEILUNG_ARTIKELBEZEICHNUNG] = artikelDto
										.getArtikelsprDto().getCBez();
							}

							oZeileTag[REPORT_MITARBEITEREINTEILUNG_UMSPANNZEIT] = umspannzeit;
							oZeileTag[REPORT_MITARBEITEREINTEILUNG_LAUFZEIT] = laufzeit;

							alDaten.add(oZeileTag);
							bEsGibtEinenEintrag = true;
						}
					}

				}

				if (bEsGibtEinenEintrag == false) {
					alDaten.add(oZeileTag);
				}

				c.set(Calendar.DATE, c.get(Calendar.DATE) + 1);
			}

		}

		session.close();
		data = new Object[alDaten.size()][REPORT_MITARBEITEREINTEILUNG_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(data);
		initJRDS(parameter, ZeiterfassungReportFac.REPORT_MODUL, ZeiterfassungReportFac.REPORT_MITARBEITEREINTEILUNG,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();
	}

	public JasperPrintLP printMaschinenzeitdaten(Integer maschineIId, java.sql.Timestamp tVon, java.sql.Timestamp tBis,
			boolean bAlleMaschinen, TheClientDto theClientDto) {

		sAktuellerReport = ZeiterfassungReportFac.REPORT_MASCHINENZEITDATEN;
		HashMap<String, Object> parameter = new HashMap<String, Object>();

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();

		org.hibernate.Criteria crit = session.createCriteria(FLRMaschine.class)
				.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));

		if (bAlleMaschinen == false) {
			crit.add(Restrictions.eq("i_id", maschineIId));
		}

		crit.addOrder(Order.asc("c_identifikationsnr"));
		List<?> resultList = crit.list();

		Iterator<?> resultListIterator = resultList.iterator();

		ArrayList alDaten = new ArrayList();
		while (resultListIterator.hasNext()) {

			FLRMaschine flrMaschine = (FLRMaschine) resultListIterator.next();

			MaschineDto maschineDto = null;
			ArrayList<MaschinenzeitdatenDto> zeitdatenEinerMaschine = null;
			try {
				maschineDto = getZeiterfassungFac().maschineFindByPrimaryKey(flrMaschine.getI_id());

				zeitdatenEinerMaschine = getZeiterfassungFac().getZeitdatenEinerMaschine(flrMaschine.getI_id(), tVon,
						tBis, theClientDto);
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}

			for (int i = 0; i < zeitdatenEinerMaschine.size(); i++) {
				MaschinenzeitdatenDto zeitdatenDto = zeitdatenEinerMaschine.get(i);

				Object[] oZeile = new Object[REPORT_MASCHINENZEITDATEN_ANZAHL_SPALTEN];

				oZeile[REPORT_MASCHINENZEITDATEN_MASCHINE] = maschineDto.getBezeichnung();

				oZeile[REPORT_MASCHINENZEITDATEN_MASCHINE_MANUELLE_BEDIENUNG] = Helper
						.short2Boolean(maschineDto.getBManuelleBedienung());
				
				oZeile[REPORT_MASCHINENZEITDATEN_PARALLELBEDIENUNG] = Helper
						.short2Boolean(zeitdatenDto.getBParallel());

				oZeile[REPORT_MASCHINENZEITDATEN_VON] = zeitdatenDto.getTVon();

				oZeile[REPORT_MASCHINENZEITDATEN_BIS] = zeitdatenDto.getTBis();

				if (zeitdatenDto.getPersonalIIdGestartet() != null) {
					PersonalDto pDtoGestartet = getPersonalFac()
							.personalFindByPrimaryKey(zeitdatenDto.getPersonalIIdGestartet(), theClientDto);
					oZeile[REPORT_MASCHINENZEITDATEN_PERSON] = pDtoGestartet.formatFixName1Name2();
				}

				if (zeitdatenDto.getTBis() != null) {
					long l_zeitdec = zeitdatenDto.getTBis().getTime() - zeitdatenDto.getTVon().getTime();
					oZeile[REPORT_MASCHINENZEITDATEN_DAUER] = ((double) l_zeitdec) / (3600000);
				}

				try {

					LosDto losDto = null;

					if (zeitdatenDto.getLossollarbeitsplanIId() != null) {
						LossollarbeitsplanDto sollaDto = getFertigungFac()
								.lossollarbeitsplanFindByPrimaryKeyOhneExc(zeitdatenDto.getLossollarbeitsplanIId());

						if (sollaDto != null) {
							oZeile[REPORT_MASCHINENZEITDATEN_LOS_AG] = sollaDto.getIArbeitsgangnummer();
							oZeile[REPORT_MASCHINENZEITDATEN_LOS_UAG] = sollaDto.getIUnterarbeitsgang();

							losDto = getFertigungFac().losFindByPrimaryKey(sollaDto.getLosIId());

						}
					} else if (zeitdatenDto.getLosIId_WennBeiManuellerBedinungAufkeinenAGGebuchtwurde() != null) {
						losDto = getFertigungFac().losFindByPrimaryKey(
								zeitdatenDto.getLosIId_WennBeiManuellerBedinungAufkeinenAGGebuchtwurde());
					}

					if (losDto != null) {

						oZeile[REPORT_MASCHINENZEITDATEN_LOS] = losDto.getCNr();

						oZeile[REPORT_MASCHINENZEITDATEN_PROJEKTBEZEICHNUNG] = losDto.getCProjekt();

						if (losDto.getAuftragIId() != null) {
							AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(losDto.getAuftragIId());
							if (auftragDto.getKundeIIdAuftragsadresse() != null) {
								KundeDto kundeDto = getKundeFac()
										.kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(), theClientDto);
								oZeile[REPORT_MASCHINENZEITDATEN_KUNDE] = kundeDto.getPartnerDto().formatAnrede();
							}

						}

						if (losDto.getStuecklisteIId() != null) {
							StuecklisteDto stuecklisteDto = getStuecklisteFac()
									.stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(), theClientDto);
							oZeile[REPORT_MASCHINENZEITDATEN_LOSARTIKELNR] = stuecklisteDto.getArtikelDto().getCNr();
							oZeile[REPORT_MASCHINENZEITDATEN_LOSARTIKELREFERENZNUMMER] = stuecklisteDto.getArtikelDto()
									.getCReferenznr();
							oZeile[REPORT_MASCHINENZEITDATEN_LOSARTIKELBEZ] = stuecklisteDto.getArtikelDto()
									.formatBezeichnung();
						}
					}

				} catch (RemoteException ex1) {
					throwEJBExceptionLPRespectOld(ex1);
				}

				alDaten.add(oZeile);
			}

		}

		Object[][] returnArray = new Object[alDaten.size()][REPORT_MASCHINENZEITDATEN_ANZAHL_SPALTEN];
		setData((Object[][]) alDaten.toArray(returnArray));

		parameter.put("P_VON", tVon);
		parameter.put("P_BIS", tBis);

		initJRDS(parameter, ZeiterfassungReportFac.REPORT_MODUL, ZeiterfassungReportFac.REPORT_MASCHINENZEITDATEN,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();

	}

	private HashMap befuelleFilterkriterienZeitjournale(Integer iOption, boolean bPlusVersteckte, Integer personalIId,
			HashMap m, TheClientDto theClientDto) {
		if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_SELEKTIERTE_PERSON) {

			m.put("P_OPTION_PERSON", getTextRespectUISpr("pers.reports.zeiterfassung.option.selektierteperson",
					theClientDto.getMandant(), theClientDto.getLocUi()));

			if (personalIId != null) {
				PersonalDto pDto = getPersonalFac().personalFindByPrimaryKey(personalIId, theClientDto);

				m.put("P_SELEKTIERTE_PERSON", pDto.formatFixName1Name2());

			}

		} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_PERSONEN) {
			m.put("P_OPTION_PERSON", getTextRespectUISpr("pers.reports.zeiterfassung.option.allepersonen",
					theClientDto.getMandant(), theClientDto.getLocUi()));
		} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_EINE_ABTEILUNG) {
			m.put("P_OPTION_PERSON", getTextRespectUISpr("pers.reports.zeiterfassung.option.meineabteilung",
					theClientDto.getMandant(), theClientDto.getLocUi()));
		} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_ARBEITER) {
			m.put("P_OPTION_PERSON", getTextRespectUISpr("pers.reports.zeiterfassung.option.allearbeiter",
					theClientDto.getMandant(), theClientDto.getLocUi()));
		} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_ANGESTELLTE) {
			m.put("P_OPTION_PERSON", getTextRespectUISpr("pers.reports.zeiterfassung.option.alleangestellte",
					theClientDto.getMandant(), theClientDto.getLocUi()));
		}

		m.put("P_MIT_VERSTECKTEN", new Boolean(bPlusVersteckte));

		return m;
	}

	public Object[][] erstelleZeitdatenjournal(Integer personalIId, java.sql.Timestamp tVon, java.sql.Timestamp tBis,
			Integer iOption, Integer kostenstelleIIdAbteilung, Boolean bPlusVersteckte, TheClientDto theClientDto) {
		ArrayList<Object[]> daten = new ArrayList<Object[]>();
		try {

			ParametermandantDto parameterVonBis = (ParametermandantDto) getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_VON_BIS_ERFASSUNG);
			boolean bVonBisErfassung = (Boolean) parameterVonBis.getCWertAsObject();

			Integer taetiegkeitIId_Ende = getZeiterfassungFac()
					.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_ENDE, theClientDto).getIId();

			boolean bVonBisZeiterfassungOhneKommtGeht = false;

			boolean bKommtGeht = true;

			ParametermandantDto parameter = (ParametermandantDto) getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_VON_BIS_ERFASSUNG_KOMMT_GEHT_BUCHEN);

			bKommtGeht = (Boolean) parameter.getCWertAsObject();

			if (bVonBisErfassung == true && bKommtGeht == false) {
				bVonBisZeiterfassungOhneKommtGeht = true;
			}

			Integer tagesartIId_Feiertag = getZeiterfassungFac()
					.tagesartFindByCNr(ZeiterfassungFac.TAGESART_FEIERTAG, theClientDto).getIId();
			Integer tagesartIId_Halbtag = getZeiterfassungFac()
					.tagesartFindByCNr(ZeiterfassungFac.TAGESART_HALBTAG, theClientDto).getIId();
			Integer taetigkeitIId_Kommt = getZeiterfassungFac()
					.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_KOMMT, theClientDto).getIId();
			Integer taetigkeitIId_Geht = getZeiterfassungFac()
					.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_GEHT, theClientDto).getIId();
			Integer taetigkeitIId_Telefon = getZeiterfassungFac()
					.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_TELEFON, theClientDto).getIId();

			tVon = Helper.cutTimestamp(tVon);
			tBis = Helper.cutTimestamp(tBis);

			PersonalDto[] personalDtos = null;

			if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_SELEKTIERTE_PERSON) {

				if (personalIId == null) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
							new Exception("personalIId == null"));
				}

				personalDtos = new PersonalDto[1];

				personalDtos[0] = getPersonalFac().personalFindByPrimaryKey(personalIId, theClientDto);

			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_PERSONEN) {
				personalDtos = getPersonalFac().personalFindByMandantCNr(theClientDto.getMandant(), bPlusVersteckte);
			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_EINE_ABTEILUNG) {
				PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(personalIId, theClientDto);
				personalDtos = getPersonalFac().personalFindAllPersonenEinerAbteilung(kostenstelleIIdAbteilung,
						theClientDto.getMandant(), bPlusVersteckte);
			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_ARBEITER) {
				personalDtos = getPersonalFac().personalFindAllArbeiterEinesMandanten(theClientDto.getMandant(),
						bPlusVersteckte);
			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_ANGESTELLTE) {
				personalDtos = getPersonalFac().personalFindAllAngestellteEinesMandanten(theClientDto.getMandant(),
						bPlusVersteckte);
			} else {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("OPTION NICHT VERFUEGBAR"));
			}

			for (int i = 0; i < personalDtos.length; i++) {
				int iSchichttag = 0;
				PersonalDto personalDto = personalDtos[i];

				personalDto.setPartnerDto(
						getPartnerFac().partnerFindByPrimaryKey(personalDto.getPartnerIId(), theClientDto));

				Calendar cVon = Calendar.getInstance();
				cVon.setTime(tVon);

				Calendar cBis = Calendar.getInstance();
				cBis.setTime(tBis);
				cBis.set(Calendar.HOUR, 23);
				cBis.set(Calendar.MINUTE, 59);
				cBis.set(Calendar.SECOND, 59);
				cBis.set(Calendar.MILLISECOND, 999);

				while (cVon.before(cBis)) {
					Calendar cNaechsterTag = Calendar.getInstance();
					cNaechsterTag.setTimeInMillis(cVon.getTimeInMillis());
					cNaechsterTag.set(Calendar.DAY_OF_MONTH, cNaechsterTag.get(Calendar.DAY_OF_MONTH) + 1);

					SessionFactory factory = FLRSessionFactory.getFactory();
					Session session = factory.openSession();

					org.hibernate.Criteria crit = session.createCriteria(FLRZeitdaten.class)
							.createAlias(ZeiterfassungFac.FLR_ZEITDATEN_FLRPERSONAL, "p")
							.add(Restrictions.eq("p.mandant_c_nr", theClientDto.getMandant()));
					crit.add(Restrictions.eq("p.i_id", personalDto.getIId()));
					crit.add(Restrictions.ge(ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT,
							new Timestamp(cVon.getTimeInMillis())));
					crit.add(Restrictions.lt(ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT,
							new Timestamp(cNaechsterTag.getTimeInMillis())));
					crit.addOrder(Order.asc(ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT));
					List<?> resultList = crit.list();

					Iterator<?> resultListIterator = resultList.iterator();

					boolean bEnde = false;
					String sLetzteTaetigkeit = null;

					String kommentarBelegBeginn = null;
					String kommentarInternBelegBeginn = null;
					String bemerkungBelegBeginn = null;

					while (resultListIterator.hasNext()) {
						Object[] zeile = new Object[REPORT_ZEITDATEN_ANZAHL_SPALTEN];
						FLRZeitdaten flrzeitdaten = (FLRZeitdaten) resultListIterator.next();

						zeile[REPORT_ZEITDATEN_PERSONALNR] = flrzeitdaten.getFlrpersonal().getC_personalnummer();
						zeile[REPORT_ZEITDATEN_ZEIT] = new Timestamp(flrzeitdaten.getT_zeit().getTime());

						// PJ18440
						boolean bEndeBeiVonBis = false;

						if (bVonBisErfassung == true) {
							ZeitdatenDto zDto = getZeiterfassungFac().zeitdatenFindByPrimaryKey(flrzeitdaten.getI_id(),
									theClientDto);

							zeile[REPORT_ZEITDATEN_ZEIT_BIS] = zDto.gettZeit_Bis();

							if (flrzeitdaten.getTaetigkeit_i_id() != null
									&& !flrzeitdaten.getTaetigkeit_i_id().equals(taetiegkeitIId_Ende)
									&& zDto.gettZeit_Bis() == null) {

								bEndeBeiVonBis = true;

							}

						}

						if (flrzeitdaten.getTaetigkeit_i_id() != null) {

							if (flrzeitdaten.getFlrtaetigkeit().getC_nr().equals(ZeiterfassungFac.TAETIGKEIT_KOMMT)
									|| flrzeitdaten.getFlrtaetigkeit().getC_nr()
											.equals(ZeiterfassungFac.TAETIGKEIT_GEHT)
									|| flrzeitdaten.getFlrtaetigkeit().getC_nr()
											.equals(ZeiterfassungFac.TAETIGKEIT_ENDE)) {
								kommentarBelegBeginn = null;
								kommentarInternBelegBeginn = null;
								bemerkungBelegBeginn = null;
							}

						}

						zeile[REPORT_ZEITDATEN_VERRECHENBAR_INPROZENT] = flrzeitdaten.getF_verrechenbar();
						if (flrzeitdaten.getFlrpersonal_erledigt() != null) {
							zeile[REPORT_ZEITDATEN_PERSON_ERLEDIGT] = flrzeitdaten.getFlrpersonal_erledigt()
									.getC_kurzzeichen();
							zeile[REPORT_ZEITDATEN_ZEITPUNKT_ERLEDIGT] = flrzeitdaten.getT_erledigt();

						}

						zeile[REPORT_ZEITDATEN_BEMERKUNG] = flrzeitdaten.getC_bemerkungzubelegart();
						zeile[REPORT_ZEITDATEN_KOMMENTAR] = flrzeitdaten.getX_kommentar();
						zeile[REPORT_ZEITDATEN_KOMMENTAR_INTERN] = flrzeitdaten.getX_kommentar_intern();

						// PJ18562

						String sZusatz = null;

						if (bEnde == false) {

							if (sLetzteTaetigkeit != null && flrzeitdaten.getFlrtaetigkeit() != null) {
								if (sLetzteTaetigkeit.equals(flrzeitdaten.getFlrtaetigkeit().getC_nr())
										&& flrzeitdaten.getFlrtaetigkeit().getTaetigkeitart_c_nr()
												.equals(ZeiterfassungFac.TAETIGKEITART_SONDERTAETIGKEIT)) {
									if (flrzeitdaten.getFlrtaetigkeit().getC_nr()
											.equals(ZeiterfassungFac.TAETIGKEIT_KOMMT)
											|| flrzeitdaten.getFlrtaetigkeit().getC_nr()
													.equals(ZeiterfassungFac.TAETIGKEIT_GEHT)
											|| flrzeitdaten.getFlrtaetigkeit().getC_nr()
													.equals(ZeiterfassungFac.TAETIGKEIT_ENDE)) {
									} else {
										sZusatz = "Ende";

										zeile[REPORT_ZEITDATEN_BEZAHLT] = 100D;

										((Object[]) daten.get(daten.size() - 1))[REPORT_ZEITDATEN_ZUSATZ] = "Beginn";

										((Object[]) daten
												.get(daten.size() - 1))[REPORT_ZEITDATEN_BEZAHLT] = flrzeitdaten
														.getFlrtaetigkeit().getF_bezahlt();
										bEnde = true;

										if (kommentarBelegBeginn != null && flrzeitdaten.getX_kommentar() == null) {
											zeile[REPORT_ZEITDATEN_KOMMENTAR] = kommentarBelegBeginn;

										}

										if (kommentarInternBelegBeginn != null
												&& flrzeitdaten.getX_kommentar_intern() == null) {
											zeile[REPORT_ZEITDATEN_KOMMENTAR_INTERN] = kommentarInternBelegBeginn;

										}

										if (bemerkungBelegBeginn != null
												&& flrzeitdaten.getC_bemerkungzubelegart() == null) {
											zeile[REPORT_ZEITDATEN_BEMERKUNG] = bemerkungBelegBeginn;

										}

									}
								}

							}
						} else {
							bEnde = false;
						}
						zeile[REPORT_ZEITDATEN_ZUSATZ] = sZusatz;

						zeile[REPORT_ZEITDATEN_SOLLZEIT] = getZeiterfassungFac().getSollzeitEinerPersonUndEinesTages(
								personalDto, tagesartIId_Feiertag, tagesartIId_Halbtag,
								new Timestamp(flrzeitdaten.getT_zeit().getTime()), theClientDto);

						zeile[REPORT_ZEITDATEN_QUELLE] = flrzeitdaten.getC_wowurdegebucht();

						String sNurTaetigkeitGeaendert = "";
						if (Helper.short2boolean(flrzeitdaten.getB_taetigkeitgeaendert()) == true) {
							sNurTaetigkeitGeaendert = " B";
						}
						String sAutomatikbuchung = "";
						if (Helper.short2boolean(flrzeitdaten.getB_automatikbuchung()) == true) {
							sAutomatikbuchung = " A";
						}

						zeile[REPORT_ZEITDATEN_BUCHUNGSART] = ZeitdatenHandler
								.istBuchungManipuliert(flrzeitdaten.getT_zeit(), flrzeitdaten.getT_aendern())
								+ sNurTaetigkeitGeaendert + sAutomatikbuchung;

						String sVorname = flrzeitdaten.getFlrpersonal().getFlrpartner().getC_name2vornamefirmazeile2();
						String sNachname = flrzeitdaten.getFlrpersonal().getFlrpartner()
								.getC_name1nachnamefirmazeile1();
						if (sVorname != null) {
							sNachname = sVorname + " " + sNachname;
						}
						zeile[REPORT_ZEITDATEN_NAME] = sNachname;

						try {
							if (flrzeitdaten.getFlrtaetigkeit() != null) {

								zeile[REPORT_ZEITDATEN_TAETIGKEIT_KENNUNG] = flrzeitdaten.getFlrtaetigkeit().getC_nr();

								if (zeile[REPORT_ZEITDATEN_BEZAHLT] == null) {
									zeile[REPORT_ZEITDATEN_BEZAHLT] = flrzeitdaten.getFlrtaetigkeit().getF_bezahlt();
								}

								zeile[REPORT_ZEITDATEN_TAETIGKEIT] = getZeiterfassungFac().taetigkeitFindByPrimaryKey(
										flrzeitdaten.getFlrtaetigkeit().getI_id(), theClientDto).getBezeichnung();

								if (flrzeitdaten.getFlrtaetigkeit().getI_id().equals(taetigkeitIId_Kommt)) {
									iSchichttag++;
								}

								if (flrzeitdaten.getFlrtaetigkeit().getI_id().equals(taetigkeitIId_Kommt)
										|| flrzeitdaten.getFlrtaetigkeit().getI_id().equals(taetigkeitIId_Geht)) {

									if (flrzeitdaten.getFlrtaetigkeit().getI_id().equals(taetigkeitIId_Kommt)) {
										zeile[REPORT_ZEITDATEN_ZEITGUTSCHRIFT] = getZeiterfassungFac()
												.zeitgutschriftEinesDatums(flrzeitdaten.getPersonal_i_id(),
														new Timestamp(flrzeitdaten.getT_zeit().getTime()), true,
														theClientDto);
									} else {
										zeile[REPORT_ZEITDATEN_ZEITGUTSCHRIFT] = getZeiterfassungFac()
												.zeitgutschriftEinesDatums(flrzeitdaten.getPersonal_i_id(),
														new Timestamp(flrzeitdaten.getT_zeit().getTime()), false,
														theClientDto);
									}

								}

								// PJ 08/12678
								if (flrzeitdaten.getFlrtaetigkeit().getI_id().equals(taetigkeitIId_Telefon)) {

									try {
										TelefonzeitenDto tzDto = getZeiterfassungFac()
												.telefonzeitenFindByPersonalIIdTVon(personalDto.getIId(),
														new Timestamp(flrzeitdaten.getT_zeit().getTime()));

										zeile[REPORT_ZEITDATEN_TELEFONZEIT_ENDE] = tzDto.getTBis();
										
										if (tzDto.getPartnerIId() != null) {

											PartnerDto partnerDto = getPartnerFac()
													.partnerFindByPrimaryKey(tzDto.getPartnerIId(), theClientDto);
											zeile[REPORT_ZEITDATEN_KUNDE] = partnerDto.formatFixTitelName1Name2();
										}

										String kommentare = "";

										if (tzDto.getXKommentarext() != null) {
											kommentare = tzDto.getXKommentarext();
										}
										if (tzDto.getXKommentarint() != null) {
											kommentare += "/ " + tzDto.getXKommentarint();
										}

										zeile[REPORT_ZEITDATEN_KOMMENTAR] = kommentare;

										zeile[REPORT_ZEITDATEN_VERRECHENBAR_INPROZENT] = tzDto.getFVerrechenbar();
										if (tzDto.getPersonalIIdErledigt() != null) {
											zeile[REPORT_ZEITDATEN_PERSON_ERLEDIGT] = getPersonalFac()
													.personalFindByPrimaryKeySmall(tzDto.getPersonalIIdErledigt())
													.getCKurzzeichen();
											zeile[REPORT_ZEITDATEN_ZEITPUNKT_ERLEDIGT] = tzDto.getTErledigt();

										}

									} catch (javax.ejb.EJBException e) {
										// Keine Zuordnung zu Telefonzeiten
										// gefunden
									}

								}

							} else {
								ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(
										flrzeitdaten.getFlrartikel().getI_id(), theClientDto);

								zeile[REPORT_ZEITDATEN_TAETIGKEIT] = artikelDto.getCNr();
								zeile[REPORT_ZEITDATEN_TAETIGKEIT_KENNUNG] = artikelDto.getCNr();
								zeile[REPORT_ZEITDATEN_ARTIKELBEZEICHNUNG] = artikelDto.formatBezeichnung();

								zeile[REPORT_ZEITDATEN_BEZAHLT] = 100D;

								bemerkungBelegBeginn = flrzeitdaten.getC_bemerkungzubelegart();
								kommentarBelegBeginn = flrzeitdaten.getX_kommentar();
								kommentarInternBelegBeginn = flrzeitdaten.getX_kommentar_intern();

							}

							zeile[REPORT_ZEITDATEN_SCHICHTTAG] = iSchichttag;

							if (flrzeitdaten.getC_belegartnr() != null) {

								BelegInfos belegInfos = getLagerFac().getBelegInfos(flrzeitdaten.getC_belegartnr(),
										flrzeitdaten.getI_belegartid(), flrzeitdaten.getI_belegartpositionid(),
										theClientDto);

								if (flrzeitdaten.getC_belegartnr().equals(LocaleFac.BELEGART_AUFTRAG)) {
									zeile[REPORT_ZEITDATEN_AUFTRAG] = "AB" + belegInfos.getBelegnummer();

								} else if (flrzeitdaten.getC_belegartnr().equals(LocaleFac.BELEGART_LOS)) {
									zeile[REPORT_ZEITDATEN_AUFTRAG] = "LO" + belegInfos.getBelegnummer();
								} else if (flrzeitdaten.getC_belegartnr().equals(LocaleFac.BELEGART_ANGEBOT)) {
									zeile[REPORT_ZEITDATEN_AUFTRAG] = "AG" + belegInfos.getBelegnummer();
								} else if (flrzeitdaten.getC_belegartnr().equals(LocaleFac.BELEGART_PROJEKT)) {
									zeile[REPORT_ZEITDATEN_AUFTRAG] = "PJ" + belegInfos.getBelegnummer();

									ProjektDto pjDto = getProjektFac()
											.projektFindByPrimaryKeyOhneExc(flrzeitdaten.getI_belegartid());
									if (pjDto != null) {
										zeile[REPORT_ZEITDATEN_PROJEKT_I_ID] = pjDto.getIId();
										zeile[REPORT_ZEITDATEN_PROJEKT_BEREICH] = getProjektServiceFac()
												.bereichFindByPrimaryKey(pjDto.getBereichIId()).getCBez();
									}

								} else {
									zeile[REPORT_ZEITDATEN_AUFTRAG] = belegInfos.getBelegnummer();
								}

								zeile[REPORT_ZEITDATEN_PROJEKTBEZEICHNUNG] = belegInfos.getBelegbezeichnung();
								zeile[REPORT_ZEITDATEN_KUNDE] = belegInfos.getKundeLieferant();

								if (flrzeitdaten.getI_belegartpositionid() != null) {
									if (flrzeitdaten.getC_belegartnr().equals(LocaleFac.BELEGART_AUFTRAG)) {
										AuftragpositionDto posDto = getAuftragpositionFac()
												.auftragpositionFindByPrimaryKeyOhneExc(
														flrzeitdaten.getI_belegartpositionid());
										if (posDto != null) {

											if (posDto.getArtikelIId() != null) {
												zeile[REPORT_ZEITDATEN_POSITION] = getArtikelFac()
														.artikelFindByPrimaryKey(posDto.getArtikelIId(), theClientDto)
														.formatArtikelbezeichnung();
											} else {
												zeile[REPORT_ZEITDATEN_POSITION] = posDto.getCBez();
											}
										} else {
											zeile[REPORT_ZEITDATEN_POSITION] = "Auftragposition nicht mehr vorhanden.";
										}
									} else if (flrzeitdaten.getC_belegartnr().equals(LocaleFac.BELEGART_LOS)) {
										try {
											LossollarbeitsplanDto posDto = getFertigungFac()
													.lossollarbeitsplanFindByPrimaryKey(
															flrzeitdaten.getI_belegartpositionid());
											zeile[REPORT_ZEITDATEN_POSITION] = getArtikelFac()
													.artikelFindByPrimaryKey(posDto.getArtikelIIdTaetigkeit(),
															theClientDto)
													.formatArtikelbezeichnung();

											if (flrzeitdaten.getFlrmaschine() != null) {

												MaschineDto maschineDto = getZeiterfassungFac()
														.maschineFindByPrimaryKey(
																flrzeitdaten.getFlrmaschine().getI_id());

												zeile[REPORT_ZEITDATEN_MASCHINE] = maschineDto.getBezeichnung();
											}

										} catch (EJBExceptionLP ex4) {
											zeile[REPORT_ZEITDATEN_POSITION] = "Losposition nicht mehr vorhanden.";
										}

									} else if (flrzeitdaten.getC_belegartnr().equals(LocaleFac.BELEGART_ANGEBOT)) {
										try {
											com.lp.server.angebot.service.AngebotpositionDto posDto = getAngebotpositionFac()
													.angebotpositionFindByPrimaryKey(
															flrzeitdaten.getI_belegartpositionid(), theClientDto);
											zeile[REPORT_ZEITDATEN_POSITION] = getArtikelFac()
													.artikelFindByPrimaryKey(posDto.getArtikelIId(), theClientDto)
													.formatArtikelbezeichnung();
										} catch (EJBExceptionLP ex4) {
											zeile[REPORT_ZEITDATEN_POSITION] = "Angebotsposition nicht mehr vorhanden.";
										}

									}
								}

							}

							// Dauer
							if (daten.size() > 0) {
								Object[] letzteZeile = (Object[]) daten.get(daten.size() - 1);
								if (letzteZeile[REPORT_ZEITDATEN_ZEIT] != null
										&& !taetigkeitIId_Kommt.equals(flrzeitdaten.getTaetigkeit_i_id())) {
									long l_zeitdec = flrzeitdaten.getT_zeit().getTime()
											- ((Timestamp) letzteZeile[REPORT_ZEITDATEN_ZEIT]).getTime();

									Double dauer = Helper.time2Double(new java.sql.Time(l_zeitdec - 3600000));
									if (daten.size() > 0) {
										Object[] letzte = (Object[]) daten.get(daten.size() - 1);
										letzte[REPORT_ZEITDATEN_DAUER] = dauer;
										daten.set(daten.size() - 1, letzte);
									}
								}
							}

						} catch (RemoteException ex1) {
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex1);
						}

						if (flrzeitdaten.getFlrtaetigkeit() != null) {
							sLetzteTaetigkeit = flrzeitdaten.getFlrtaetigkeit().getC_nr();
						} else {
							sLetzteTaetigkeit = null;
						}

						if (bEndeBeiVonBis == true) {
							zeile[REPORT_ZEITDATEN_TAETIGKEIT_KENNUNG] = ZeiterfassungFac.TAETIGKEIT_ENDE;
							zeile[REPORT_ZEITDATEN_TAETIGKEIT] = ZeiterfassungFac.TAETIGKEIT_ENDE;
						}

						// PJ19944
						ZeitmodelltagDto dto = getZeiterfassungFac().getZeitmodelltagZuDatum(personalDto.getIId(),
								new Timestamp(flrzeitdaten.getT_zeit().getTime()), tagesartIId_Feiertag,
								tagesartIId_Halbtag, false, theClientDto);
						if (dto != null) {

							zeile[REPORT_ZEITDATEN_FRUEHESTES_KOMMT] = dto.getUBeginn();
							zeile[REPORT_ZEITDATEN_FRUEHESTES_KOMMT_VORTAG] = Helper
									.short2Boolean(dto.getBBeginnVortag());
							zeile[REPORT_ZEITDATEN_KOMMT_AKZEPTIERT_BIS] = dto.getUBeginnAkzeptiertBis();
							zeile[REPORT_ZEITDATEN_SPAETESTES_GEHT] = dto.getUEnde();
							zeile[REPORT_ZEITDATEN_GEHT_AKZEPTIERT_AB] = dto.getUEndeAkzeptiertAb();

						}

						zeile[REPORT_ZEITDATEN_ZEITDATEN_I_ID] = flrzeitdaten.getI_id();
						daten.add(zeile);
					}
					SonderzeitenDto[] dtos = null;
					try {
						// Hier nun Sonderzeiten holen
						dtos = getZeiterfassungFac().sonderzeitenFindByPersonalIIdDDatum(personalDto.getIId(),
								Helper.cutTimestamp(new Timestamp(cVon.getTimeInMillis())));
						for (int j = 0; j < dtos.length; j++) {
							TaetigkeitDto taeitgkeitDto = getZeiterfassungFac()
									.taetigkeitFindByPrimaryKey(dtos[j].getTaetigkeitIId(), theClientDto);
							Object[] neueZeile = new Object[REPORT_ZEITDATEN_ANZAHL_SPALTEN];
							neueZeile[REPORT_ZEITDATEN_TAETIGKEIT_SONDERZEIT_KENNUNG] = taeitgkeitDto.getCNr();
							neueZeile[REPORT_ZEITDATEN_TAETIGKEIT_SONDERZEIT] = taeitgkeitDto.getBezeichnung();
							neueZeile[REPORT_ZEITDATEN_PERSONALNR] = personalDto.getCPersonalnr();

							String sVorname = personalDto.getPartnerDto().getCName2vornamefirmazeile2();
							String sNachname = personalDto.getPartnerDto().getCName1nachnamefirmazeile1();
							if (sVorname != null) {
								sNachname = sVorname + " " + sNachname;
							}
							neueZeile[REPORT_ZEITDATEN_NAME] = sNachname;
							neueZeile[REPORT_ZEITDATEN_DATUM_SONDERZEIT] = new Timestamp(cVon.getTimeInMillis());

							// Dauer
							double dauer = 0;

							if (Helper.short2boolean(dtos[j].getBTag()) == true
									|| Helper.short2boolean(dtos[j].getBHalbtag()) == true) {
								ZeitmodelltagDto dto = getZeiterfassungFac().getZeitmodelltagZuDatum(
										personalDto.getIId(), new Timestamp(cVon.getTimeInMillis()),
										tagesartIId_Feiertag, tagesartIId_Halbtag, false, theClientDto);
								if (dto != null && dto.getUSollzeit() != null) {

									dauer = Helper.time2Double(dto.getUSollzeit());
									if (Helper.short2boolean(dtos[j].getBHalbtag()) == true
											&& !dto.getTagesartIId().equals(tagesartIId_Halbtag)) {
										dauer = dauer / 2;
									}

								}
							} else {
								dauer = Helper.time2Double(dtos[j].getUStunden());
							}
							neueZeile[REPORT_ZEITDATEN_DAUER_SONDERZEIT] = new Double(dauer);
							daten.add(neueZeile);
						}

					} catch (RemoteException ex1) {
						throwEJBExceptionLPRespectOld(ex1);
					}

					if (resultList.size() > 0 || dtos.length > 0) {
						// Letzte Zeile holen und Tagessumme eintragen
						Object[] letzteZeile = (Object[]) daten.get(daten.size() - 1);

						try {

							if (bVonBisZeiterfassungOhneKommtGeht) {

								VonBisErfassungTagesdatenDto vbDto = getZeiterfassungFac()
										.berechneTagesArbeitszeitVonBisZeiterfassungOhneKommtGeht(personalDto.getIId(),
												new java.sql.Date(cVon.getTimeInMillis()), theClientDto);

								letzteZeile[REPORT_ZEITDATEN_TAGESSUMME] = vbDto.getdIst();

							} else {
								Double d = getZeiterfassungFac().berechneTagesArbeitszeit(personalDto.getIId(),
										new java.sql.Date(cVon.getTimeInMillis()), theClientDto);

								letzteZeile[REPORT_ZEITDATEN_TAGESSUMME] = d;

							}

						} catch (javax.ejb.EJBException ex3) {
							letzteZeile[REPORT_ZEITDATEN_TAGESSUMME] = new Double(0);
							letzteZeile[REPORT_ZEITDATEN_PROJEKTBEZEICHNUNG] = "Fehler in Zeitdaten";
						} catch (EJBExceptionLP ex4) {
							letzteZeile[REPORT_ZEITDATEN_TAGESSUMME] = new Double(0);
							letzteZeile[REPORT_ZEITDATEN_PROJEKTBEZEICHNUNG] = "Fehler in Zeitdaten";
						} catch (RemoteException e) {
							throwEJBExceptionLPRespectOld(e);

						}
						daten.set(daten.size() - 1, letzteZeile);
					}

					session.close();

					cVon.set(Calendar.DAY_OF_MONTH, cVon.get(Calendar.DAY_OF_MONTH) + 1);

				}

			}
		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		Object[] x = daten.toArray();
		Object[][] dataLocal = new Object[x.length][22];
		for (int i = 0; i < x.length; i++) {
			dataLocal[i] = (Object[]) x[i];
		}

		return dataLocal;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printZeitdatenjournal(Integer personalIId, java.sql.Timestamp tVon, java.sql.Timestamp tBis,
			Integer iOption, Integer kostenstelleIIdAbteilung, Boolean bPlusVersteckte, TheClientDto theClientDto) {
		sAktuellerReport = ZeiterfassungReportFac.REPORT_ZEITDATEN;
		HashMap<String, Object> parameter = new HashMap<String, Object>();

		parameter = befuelleFilterkriterienZeitjournale(iOption, bPlusVersteckte, personalIId, parameter, theClientDto);

		setData(erstelleZeitdatenjournal(personalIId, tVon, tBis, iOption, kostenstelleIIdAbteilung, bPlusVersteckte,
				theClientDto));

		if (tVon != null) {
			parameter.put("P_VON", tVon);
		}
		if (tBis != null) {
			parameter.put("P_BIS", tBis);
		}

		try {
			ParametermandantDto parameterVonBis = (ParametermandantDto) getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_VON_BIS_ERFASSUNG);
			parameter.put("P_VON_BIS_ERFASSUNG", ((Boolean) parameterVonBis.getCWertAsObject()));

		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		initJRDS(parameter, ZeiterfassungFac.REPORT_MODUL, ZeiterfassungReportFac.REPORT_ZEITDATEN,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();

	}

	public JasperPrintLP printZestiftliste(TheClientDto theClientDto) {
		sAktuellerReport = ZeiterfassungReportFac.REPORT_ZESTIFTE;
		HashMap<String, Object> parameter = new HashMap<String, Object>();

		ZeitstiftDto[] dtos = null;
		try {
			dtos = getZeiterfassungFac().zeitstiftFindByMandantCNr(theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		// data = new Object[dtos.length][3];
		setData(new Object[dtos.length][3]);

		for (int i = 0; i < dtos.length; i++) {
			data[i][REPORT_ZESTIFTE_KENNUNG] = dtos[i].getCNr().trim();
			data[i][REPORT_ZESTIFTE_MEHRFACHSTIFT] = new Boolean(Helper.short2Boolean(dtos[i].getBMehrfachstift()));
			if (dtos[i].getPersonalIId() != null) {

				PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(dtos[i].getPersonalIId(),
						theClientDto);
				data[i][REPORT_ZESTIFTE_PERSONAL] = personalDto.getPartnerDto().formatFixTitelName1Name2();

			}
		}

		initJRDS(parameter, ZeiterfassungReportFac.REPORT_MODUL, ZeiterfassungReportFac.REPORT_ZESTIFTE,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();
	}

	public JasperPrintLP printTelefonzeit(Integer telefonzeitIId, TheClientDto theClientDto) {

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		try {
			TelefonzeitenDto telefonzeitenDto = getZeiterfassungFac().telefonzeitenFindByPrimaryKey(telefonzeitIId);

			if (telefonzeitenDto.getPartnerIId() != null) {
				com.lp.server.partner.service.PartnerDto partnerDto = getPartnerFac()
						.partnerFindByPrimaryKey(telefonzeitenDto.getPartnerIId(), theClientDto);
				parameter.put("P_PARTNER", partnerDto.formatFixTitelName1Name2());
			}

			if (telefonzeitenDto.getAnsprechpartnerIId() != null) {
				AnsprechpartnerDto ansprechpartnerDto = getAnsprechpartnerFac()
						.ansprechpartnerFindByPrimaryKey(telefonzeitenDto.getAnsprechpartnerIId(), theClientDto);
				parameter.put("P_ANSPRECHPARTNER",
						ansprechpartnerDto.getPartnerDto().formatFixTitelVornameNachnameNTitel());
			}

			parameter.put("P_PERSONAL",
					getPersonalFac().personalFindByPrimaryKey(telefonzeitenDto.getPersonalIId(), theClientDto)
							.getPartnerDto().formatFixTitelName1Name2());
			if (telefonzeitenDto.getProjektIId() != null) {
				parameter.put("P_PROJEKT",
						getProjektFac().projektFindByPrimaryKey(telefonzeitenDto.getProjektIId()).getCNr());
			}

			parameter.put("P_VON", telefonzeitenDto.getTVon());
			parameter.put("P_BIS", telefonzeitenDto.getTBis());

			if (telefonzeitenDto.getTBis() != null) {
				java.sql.Time tTemp = new java.sql.Time(
						telefonzeitenDto.getTBis().getTime() - telefonzeitenDto.getTVon().getTime() - 3600000);
				parameter.put("P_DAUER", Helper.time2Double(tTemp));

			}

			parameter.put("P_KOMMENTAREXTERN", telefonzeitenDto.getXKommentarext());
			parameter.put("P_KOMMENTARINTERN", telefonzeitenDto.getXKommentarint());
			parameter.put("P_TITEL", telefonzeitenDto.getCTitel());
			parameter.put("P_WIEDERVORLAGE", telefonzeitenDto.getTWiedervorlage());
			parameter.put("P_WIEDERVORLAGE_ERLEDIGT", telefonzeitenDto.getTWiedervorlageErledigt());

			if (telefonzeitenDto.getPersonalIIdZugewiesener() != null) {
				parameter.put("P_PERSONAL_ZUGEWIESEN",
						getPersonalFac()
								.personalFindByPrimaryKey(telefonzeitenDto.getPersonalIIdZugewiesener(), theClientDto)
								.getPartnerDto().formatFixTitelName1Name2());
			}

			if (telefonzeitenDto.getAuftragIId() != null) {
				parameter.put("P_AUFTRAG",
						getAuftragFac().auftragFindByPrimaryKey(telefonzeitenDto.getAuftragIId()).getCNr());
			}

			if (telefonzeitenDto.getAngebotIId() != null) {
				parameter.put("P_ANGEBOT", getAngebotFac()
						.angebotFindByPrimaryKey(telefonzeitenDto.getAngebotIId(), theClientDto).getCNr());
			}

			//PJ22112
			parameter.put("P_SUBREPORT_BELEGARTMEDIA", getSubreportBelegartmedia(QueryParameters.UC_ID_TELEFONZEITEN, telefonzeitIId, theClientDto));

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		data = new Object[0][0];

		initJRDS(parameter, ZeiterfassungReportFac.REPORT_MODUL, ZeiterfassungReportFac.REPORT_TELEFONZEIT,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();

	}

	public JasperPrintLP printTelefonzeiten(Integer personalIId, Integer partnerIId, java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, boolean bSortiertNachPersonal, TheClientDto theClientDto) {

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(tBis.getTime());
		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1);
		tBis = Helper.cutTimestamp(new Timestamp(c.getTimeInMillis()));
		tVon = Helper.cutTimestamp(tVon);

		return erstelleTelefonzeiten(personalIId, partnerIId, tVon, tBis, bSortiertNachPersonal, theClientDto);

	}

	private JasperPrintLP erstelleTelefonzeiten(Integer personalIId, Integer partnerIId, java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, boolean bSortiertNachPersonal, TheClientDto theClientDto) {
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		org.hibernate.Criteria crit = session.createCriteria(FLRTelefonzeiten.class)
				.createAlias(ZeiterfassungFac.FLR_TELEFONZEITEN_FLRPERSONAL, "p")
				.add(Restrictions.eq("p.mandant_c_nr", theClientDto.getMandant()));

		if (personalIId != null) {
			crit.add(Restrictions.eq(ZeiterfassungFac.FLR_TELEFONZEITEN_PERSONAL_I_ID, personalIId));
		}
		if (partnerIId != null) {
			crit.add(Restrictions.eq(ZeiterfassungFac.FLR_TELEFONZEITEN_PARTNER_I_ID, partnerIId));

			com.lp.server.partner.service.PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(partnerIId,
					theClientDto);

			parameter.put("P_PARTNER", partnerDto.formatFixTitelName1Name2());

		}

		if (bSortiertNachPersonal == true) {
			parameter.put("P_SORTIERUNG",
					getTextRespectUISpr("lp.personal", theClientDto.getMandant(), theClientDto.getLocUi()));

			crit.addOrder(Order.asc("p." + PersonalFac.FLR_PERSONAL_C_PERSONALNUMMER));

		} else {
			parameter.put("P_SORTIERUNG",
					getTextRespectUISpr("lp.partner", theClientDto.getMandant(), theClientDto.getLocUi()));

			crit.createAlias(ZeiterfassungFac.FLR_TELEFONZEITEN_FLRPARTNER, "pa")
					.addOrder(Order.asc("pa." + PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1));

		}

		crit.add(Restrictions.ge(ZeiterfassungFac.FLR_TELEFONZEITEN_T_VON, tVon));
		crit.add(Restrictions.lt(ZeiterfassungFac.FLR_TELEFONZEITEN_T_VON, tBis));

		crit.addOrder(Order.asc(ZeiterfassungFac.FLR_TELEFONZEITEN_T_VON));
		List<?> list = crit.list();
		Iterator<?> iterator = list.iterator();
		// data = new Object[list.size()][REPORT_TELEFONLISTE_ANZAHL_SPALTEN];
		setData(new Object[list.size()][REPORT_TELEFONLISTE_ANZAHL_SPALTEN]);

		int row = 0;
		while (iterator.hasNext()) {
			FLRTelefonzeiten flrTelefonzeiten = (FLRTelefonzeiten) iterator.next();

			String sVorname = flrTelefonzeiten.getFlrpersonal().getFlrpartner().getC_name2vornamefirmazeile2();
			String sNachname = flrTelefonzeiten.getFlrpersonal().getFlrpartner().getC_name1nachnamefirmazeile1();
			if (sVorname != null) {
				sNachname = sVorname + " " + sNachname;
			}
			data[row][REPORT_TELEFONLISTE_PERSON] = sNachname;

			if (flrTelefonzeiten.getProjekt_i_id() != null) {
				try {
					data[row][REPORT_TELEFONLISTE_PROJEKT] = getProjektFac()
							.projektFindByPrimaryKey(flrTelefonzeiten.getProjekt_i_id()).getCNr();
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}

			if (flrTelefonzeiten.getFlransprechpartner() != null) {
				sVorname = flrTelefonzeiten.getFlransprechpartner().getFlrpartneransprechpartner()
						.getC_name2vornamefirmazeile2();
				sNachname = flrTelefonzeiten.getFlransprechpartner().getFlrpartneransprechpartner()
						.getC_name1nachnamefirmazeile1();
				if (sVorname != null) {
					sNachname = sVorname + " " + sNachname;
				}
				data[row][REPORT_TELEFONLISTE_ANSPRECHPARTNER] = sNachname;
			}

			if (flrTelefonzeiten.getT_bis() != null) {
				java.sql.Time tTemp = new java.sql.Time(
						flrTelefonzeiten.getT_bis().getTime() - flrTelefonzeiten.getT_von().getTime() - 3600000);
				data[row][REPORT_TELEFONLISTE_DAUER] = Helper.time2Double(tTemp);

			}

			if (flrTelefonzeiten.getFlrpartner() != null) {

				com.lp.server.partner.service.PartnerDto partnerDto = getPartnerFac()
						.partnerFindByPrimaryKey(flrTelefonzeiten.getFlrpartner().getI_id(), theClientDto);
				data[row][REPORT_TELEFONLISTE_PARTNER] = partnerDto.formatFixTitelName1Name2();

				if (flrTelefonzeiten.getFlrpartner().getFlrlandplzort() != null) {
					data[row][REPORT_TELEFONLISTE_LKZ_PARTNER] = flrTelefonzeiten.getFlrpartner().getFlrlandplzort()
							.getFlrland().getC_lkz();
				}

			}

			if (bSortiertNachPersonal == true) {
				data[row][REPORT_TELEFONLISTE_GRUPPIERUNG] = flrTelefonzeiten.getPersonal_i_id();
			} else {
				if (flrTelefonzeiten.getFlrpartner() != null) {
					data[row][REPORT_TELEFONLISTE_GRUPPIERUNG] = flrTelefonzeiten.getFlrpartner().getI_id();
				}
			}

			data[row][REPORT_TELEFONLISTE_VON] = new Timestamp(flrTelefonzeiten.getT_von().getTime());
			if (flrTelefonzeiten.getT_bis() != null) {
				data[row][REPORT_TELEFONLISTE_BIS] = new Timestamp(flrTelefonzeiten.getT_bis().getTime());
			}
			data[row][REPORT_TELEFONLISTE_KOMMENTAREXTERN] = flrTelefonzeiten.getX_kommentarext();
			data[row][REPORT_TELEFONLISTE_KOMMENTARINTERN] = flrTelefonzeiten.getX_kommentarint();

			row++;
		}

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(tBis.getTime());
		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) - 1);
		tBis = new Timestamp(c.getTimeInMillis());

		parameter.put("P_VON", tVon);
		parameter.put("P_BIS", tBis);

		sAktuellerReport = ZeiterfassungReportFac.REPORT_TELEFONZEITEN;
		index = -1;
		initJRDS(parameter, ZeiterfassungReportFac.REPORT_MODUL, ZeiterfassungReportFac.REPORT_TELEFONZEITEN,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	public JasperPrintLP printFahrzeuge(Integer fahrzeugIId, java.sql.Timestamp tVon, java.sql.Timestamp tBis,
			TheClientDto theClientDto) {

		tVon = Helper.cutTimestamp(tVon);
		tBis = Helper.cutTimestamp(tBis);

		if (tVon.after(tBis)) {
			java.sql.Timestamp h = tVon;
			tVon = tBis;
			tBis = h;
		}

		ArrayList<ReiseKomplettDto> alReisen = getZeiterfassungFac().holeReisenKomplett(fahrzeugIId, tVon, tBis,
				theClientDto);

		ArrayList alDaten = new ArrayList();
		for (int k = 0; k < alReisen.size(); k++) {

			ReiseKomplettDto rkDto = alReisen.get(k);

			// Kosten

			BigDecimal kmKostenKomplett = getZeiterfassungFac().getKmKostenEinerReise(rkDto, theClientDto);

			FahrzeugDto fzDto = getPersonalFac().fahrzeugFindByPrimaryKey(rkDto.getReiseEnde().getFahrzeugIId());

			TreeMap tmBeginn = rkDto.getTmReiseBeginn();
			Iterator it = tmBeginn.keySet().iterator();
			while (it.hasNext()) {
				ReiseDto rDto = (ReiseDto) tmBeginn.get(it.next());
				Object[] oZeile = new Object[REPORT_FAHRZEUGE_ANZAHL_SPALTEN];
				oZeile[REPORT_FAHRZEUGE_FAHRZEUG] = fzDto.getCBez();
				oZeile[REPORT_FAHRZEUGE_KENNZEICHEN] = fzDto.getCKennzeichen();
				oZeile[REPORT_FAHRZEUGE_BEGINN] = rDto.getTZeit();
				oZeile[REPORT_FAHRZEUGE_KOSTEN] = rkDto.getAnteiligeKostenEinesAbschnitts(rDto.getIId(),
						kmKostenKomplett);

				try {

					oZeile[REPORT_FAHRZEUGE_PERSON] = getPersonalFac()
							.personalFindByPrimaryKey(rDto.getPersonalIId(), theClientDto).formatFixName1Name2();

					oZeile[REPORT_FAHRZEUGE_LAND] = getZeiterfassungFac().diaetenFindByPrimaryKey(rDto.getDiaetenIId())
							.getCBez();

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
				if (rDto.getPartnerIId() != null) {
					oZeile[REPORT_FAHRZEUGE_PARTNER] = getPartnerFac()
							.partnerFindByPrimaryKey(rDto.getPartnerIId(), theClientDto).formatFixName1Name2();
				}
				if (it.hasNext() == false) {
					oZeile[REPORT_FAHRZEUGE_ENDE] = rkDto.getReiseEnde().getTZeit();
					if (rkDto.getReiseEnde().getIKmbeginn() != null && rkDto.getReiseEnde().getIKmende() != null) {

						oZeile[REPORT_FAHRZEUGE_STRECKE] = rkDto.getReiseEnde().getIKmende()
								- rkDto.getReiseEnde().getIKmbeginn();
					}
				}

				alDaten.add(oZeile);

			}

		}

		Object[][] returnArray = new Object[alDaten.size()][REPORT_FAHRZEUGE_ANZAHL_SPALTEN];
		// data = (Object[][]) alDaten.toArray(returnArray);
		setData((Object[][]) alDaten.toArray(returnArray));

		/*
		 * try {
		 * 
		 * //Nun alle Reisen durchgehen
		 * 
		 * Integer landIIdHeimat = null; Integer partnerMandant =
		 * getMandantFac().mandantFindByPrimaryKey( theClientDto.getMandant(),
		 * theClientDto).getPartnerIId(); PartnerDto partnerDto =
		 * getPartnerFac().partnerFindByPrimaryKey( partnerMandant, theClientDto); if
		 * (partnerDto.getLandplzortDto() != null) { landIIdHeimat =
		 * partnerDto.getLandplzortDto().getIlandID(); } ArrayList<Object>
		 * alReiseeintraege = erstelleEinzelneReiseeintraege( tVon, tBis, theClientDto,
		 * landIIdHeimat, null); data = new
		 * Object[alReiseeintraege.size()][REPORT_FAHRZEUGE_ANZAHL_SPALTEN]; for (int k
		 * = 0; k < alReiseeintraege.size(); k++) { Object[] o = (Object[])
		 * alReiseeintraege.get(k); data[k][RE PORT_FAHRZEUGE_BEGINN] =
		 * o[REPORT_REISEZEITEN_BEGINN]; data[k][REPORT_FAHRZEUGE_ENDE] =
		 * o[REPORT_REISEZEITEN_ENDE]; data[k][REPORT_FAHRZEUGE_FAHRZEUG] =
		 * o[REPORT_REISEZEITEN_FAHRZEUG]; data[k][REPORT_FAHRZEUGE_KENNZEICHEN] =
		 * o[REPORT_REISEZEITEN_KENNZEICHEN]; data[k][REPORT_FAHRZEUGE_LAND] =
		 * o[REPORT_REISEZEITEN_LAND]; data[k][REPORT_FAHRZEUGE_PARTNER] =
		 * o[REPORT_REISEZEITEN_PARTNER]; // data[k][REPORT_FAHRZEUGE_KOSTEN] = //
		 * o[REPORT_REISEZEITEN_SPESEN]; data[k][REPORT_FAHRZEUGE_STRECKE] =
		 * o[REPORT_REISEZEITEN_ENTFERNUNG];
		 * 
		 * }
		 * 
		 * } catch (RemoteException e) { throwEJBExceptionLPRespectOld(e); }
		 */

		sAktuellerReport = ZeiterfassungReportFac.REPORT_FAHRZEUGE;
		index = -1;
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(tBis.getTime());
		c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
		parameter.put("P_VON", tVon);
		parameter.put("P_BIS", new java.sql.Timestamp(c.getTimeInMillis()));
		initJRDS(parameter, ZeiterfassungReportFac.REPORT_MODUL, ZeiterfassungReportFac.REPORT_FAHRZEUGE,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	public JasperPrintLP printUrlaubsantrag(Integer personalIId, Integer[] integerIIds, boolean bGenehmigt,
			String cVorraussetzung, TheClientDto theClientDto) {

		sAktuellerReport = ZeiterfassungReportFac.REPORT_URLAUBSANTRAG;
		HashMap<String, Object> parameter = new HashMap<String, Object>();

		PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(personalIId, theClientDto);

		parameter.put("P_PERSON", personalDto.getPartnerDto().formatFixTitelName1Name2());

		if (personalDto.getKostenstelleIIdAbteilung() != null) {

			KostenstelleDto abtDto = getSystemFac()
					.kostenstelleFindByPrimaryKey(personalDto.getKostenstelleIIdAbteilung());
			parameter.put("P_ABTEILUNG", abtDto.formatKostenstellenbezeichnung());

		}

		PersonalDto pDto_Vorgsestzter = getPersonalFac().getPersonalDto_Vorgesetzter(personalIId, theClientDto);

		if (pDto_Vorgsestzter != null) {
			parameter.put("P_PERSON_VORGESETZTER", pDto_Vorgsestzter.getPartnerDto().formatFixTitelName1Name2());
		}

		parameter.put("P_GENEHMIGT", new Boolean(bGenehmigt));
		parameter.put("P_VORAUSSETZUNG", cVorraussetzung);

		TreeMap<Timestamp, SonderzeitenDto> tmZeitraumUrlaub = new TreeMap<Timestamp, SonderzeitenDto>();
		TreeMap<Timestamp, SonderzeitenDto> tmZeitraumZeitausgleich = new TreeMap<Timestamp, SonderzeitenDto>();
		TreeMap<Timestamp, SonderzeitenDto> tmZeitraumKrank = new TreeMap<Timestamp, SonderzeitenDto>();

		// Zuerst alle Urlaubsantraege holen

		TaetigkeitDto taetigkeitDto_Urlaubsantrag = getZeiterfassungFac()
				.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_URLAUBSANTRAG, theClientDto);

		TaetigkeitDto taetigkeitDto_Urlaub = getZeiterfassungFac()
				.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_URLAUB, theClientDto);

		TaetigkeitDto taetigkeitDto_Zaantrag = getZeiterfassungFac()
				.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_ZAANTRAG, theClientDto);

		TaetigkeitDto taetigkeitDto_Zeitausgleich = getZeiterfassungFac()
				.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_ZEITAUSGLEICH, theClientDto);

		TaetigkeitDto taetigkeitDto_Krankantrag = getZeiterfassungFac()
				.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_KRANKANTRAG, theClientDto);

		TaetigkeitDto taetigkeitDto_Krank = getZeiterfassungFac().taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_KRANK,
				theClientDto);

		for (int i = 0; i < integerIIds.length; i++) {

			try {
				SonderzeitenDto szDto = getZeiterfassungFac().sonderzeitenFindByPrimaryKey(integerIIds[i],
						theClientDto);
				if (szDto.getTaetigkeitIId().equals(taetigkeitDto_Urlaubsantrag.getIId())
						|| szDto.getTaetigkeitIId().equals(taetigkeitDto_Urlaub.getIId())) {
					tmZeitraumUrlaub.put(Helper.cutTimestamp(szDto.getTDatum()), szDto);

				}
				if (szDto.getTaetigkeitIId().equals(taetigkeitDto_Zaantrag.getIId())
						|| szDto.getTaetigkeitIId().equals(taetigkeitDto_Zeitausgleich.getIId())) {
					tmZeitraumZeitausgleich.put(Helper.cutTimestamp(szDto.getTDatum()), szDto);

				}
				if (szDto.getTaetigkeitIId().equals(taetigkeitDto_Krankantrag.getIId())
						|| szDto.getTaetigkeitIId().equals(taetigkeitDto_Krank.getIId())) {
					tmZeitraumKrank.put(Helper.cutTimestamp(szDto.getTDatum()), szDto);

				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}
		ArrayList alZeitraeume = new ArrayList();

		if (tmZeitraumUrlaub.size() > 0) {

			alZeitraeume.addAll(
					erzeugeDatenFuerUrlaubsantragsdruck(theClientDto, personalDto, tmZeitraumUrlaub, true, false));
		}
		if (tmZeitraumZeitausgleich.size() > 0) {
			alZeitraeume.addAll(erzeugeDatenFuerUrlaubsantragsdruck(theClientDto, personalDto, tmZeitraumZeitausgleich,
					false, false));
		}

		if (tmZeitraumKrank.size() > 0) {
			alZeitraeume.addAll(
					erzeugeDatenFuerUrlaubsantragsdruck(theClientDto, personalDto, tmZeitraumKrank, false, true));
		}

		data = new Object[alZeitraeume.size()][REPORT_URLAUBSANTRAG_ANZAHL_SPALTEN];
		// data = (Object[][]) alZeitraeume.toArray(data);
		setData((Object[][]) alZeitraeume.toArray(data));

		initJRDS(parameter, ZeiterfassungReportFac.REPORT_MODUL, ZeiterfassungReportFac.REPORT_URLAUBSANTRAG,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();

	}

	private Object[] befuelleMitAnzahlTageStunden(Object[] oZeile, TreeMap<Timestamp, SonderzeitenDto> tmZeitraum,
			Integer personalIId, TheClientDto theClientDto) {

		Integer tagesartIId_Feiertag = getZeiterfassungFac()
				.tagesartFindByCNr(ZeiterfassungFac.TAGESART_FEIERTAG, theClientDto).getIId();
		Integer tagesartIId_Halbtag = getZeiterfassungFac()
				.tagesartFindByCNr(ZeiterfassungFac.TAGESART_HALBTAG, theClientDto).getIId();

		Double dStundenImZeitraum = 0D;
		Double dAnzahlTage = 0D;
		Double dAnzahlHalbeTage = 0D;

		Timestamp tVon = (Timestamp) oZeile[REPORT_URLAUBSANTRAG_VON];
		Timestamp tBis = (Timestamp) oZeile[REPORT_URLAUBSANTRAG_BIS];

		Calendar cAktuell = Calendar.getInstance();
		cAktuell.setTime(tVon);

		while (cAktuell.getTimeInMillis() <= tBis.getTime()) {

			Timestamp tAktuell = new Timestamp(cAktuell.getTimeInMillis());

			if (tmZeitraum.containsKey(tAktuell)) {

				SonderzeitenDto szDto = tmZeitraum.get(tAktuell);

				if (Helper.short2boolean(szDto.getBTag())) {
					dAnzahlTage += 1;
				} else if (Helper.short2boolean(szDto.getBHalbtag())) {
					dAnzahlHalbeTage += 1;
				}

				try {
					ZeitmodelltagDto zmtagDto = getZeiterfassungFac().getZeitmodelltagZuDatum(personalIId, tAktuell,
							tagesartIId_Feiertag, tagesartIId_Halbtag, true, theClientDto);

					Double d = new Double(0);
					if (zmtagDto != null) {
						d = Helper.time2Double(zmtagDto.getUSollzeit());
					}

					if (d != null && d.doubleValue() > 0) {
						if (Helper.short2boolean(szDto.getBHalbtag())) {
							dStundenImZeitraum += d / 2;
						} else {
							dStundenImZeitraum += d;
						}
					}
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}

			cAktuell.add(Calendar.DATE, 1);

		}

		oZeile[REPORT_URLAUBSANTRAG_ANZAHL_TAGE] = dAnzahlTage;
		oZeile[REPORT_URLAUBSANTRAG_ANZAHL_HALBE_TAGE] = dAnzahlHalbeTage;
		oZeile[REPORT_URLAUBSANTRAG_ANZAHL_STUNDEN] = dStundenImZeitraum;

		return oZeile;
	}

	public ArrayList erzeugeDatenFuerUrlaubsantragsdruck(TheClientDto theClientDto, PersonalDto personalDto,
			TreeMap<Timestamp, SonderzeitenDto> tmZeitraum, boolean bUrlaub, boolean bKrank) {

		ArrayList alZeitraeume = new ArrayList();
		Iterator<Timestamp> it = tmZeitraum.keySet().iterator();

		Timestamp tVon = it.next();
		Calendar cVon = Calendar.getInstance();
		cVon.setTimeInMillis(tVon.getTime());

		SonderzeitenDto szDto = tmZeitraum.get(tVon);

		if (it.hasNext()) {

			while (it.hasNext()) {
				Timestamp tZeile = it.next();

				szDto = tmZeitraum.get(tZeile);

				Calendar cZeile = Calendar.getInstance();
				cZeile.setTimeInMillis(tZeile.getTime());

				cVon.add(Calendar.DAY_OF_MONTH, 1);

				if (cVon.get(Calendar.DAY_OF_YEAR) == cZeile.get(Calendar.DAY_OF_YEAR)
						&& cVon.get(Calendar.YEAR) == cZeile.get(Calendar.YEAR)) {
					// Dann ist das get naechste Tag, also eins weiter

					if (it.hasNext()) {
						continue;
					} else {
						Object[] oZeile = new Object[REPORT_URLAUBSANTRAG_ANZAHL_SPALTEN];
						oZeile[REPORT_URLAUBSANTRAG_VON] = tVon;
						oZeile[REPORT_URLAUBSANTRAG_BIS] = tZeile;
						oZeile[REPORT_URLAUBSANTRAG_URLAUB] = new Boolean(bUrlaub);
						oZeile[REPORT_URLAUBSANTRAG_KRANK] = new Boolean(bKrank);

						oZeile = befuelleMitAnzahlTageStunden(oZeile, tmZeitraum, personalDto.getIId(), theClientDto);

						alZeitraeume.add(oZeile);

					}

				} else {
					Object[] oZeile = new Object[REPORT_URLAUBSANTRAG_ANZAHL_SPALTEN];
					oZeile[REPORT_URLAUBSANTRAG_VON] = tVon;
					oZeile[REPORT_URLAUBSANTRAG_BIS] = tmZeitraum.lowerKey(tZeile);
					oZeile[REPORT_URLAUBSANTRAG_URLAUB] = new Boolean(bUrlaub);
					oZeile[REPORT_URLAUBSANTRAG_KRANK] = new Boolean(bKrank);
					tVon = tZeile;
					cVon.setTimeInMillis(tZeile.getTime());

					oZeile = befuelleMitAnzahlTageStunden(oZeile, tmZeitraum, personalDto.getIId(), theClientDto);
					alZeitraeume.add(oZeile);

					if (it.hasNext() == false && tmZeitraum.size() >= 2) {
						Object[] oZeile2 = new Object[REPORT_URLAUBSANTRAG_ANZAHL_SPALTEN];
						oZeile2[REPORT_URLAUBSANTRAG_VON] = tVon;
						oZeile2[REPORT_URLAUBSANTRAG_BIS] = tVon;
						oZeile2[REPORT_URLAUBSANTRAG_URLAUB] = new Boolean(bUrlaub);
						oZeile2[REPORT_URLAUBSANTRAG_KRANK] = new Boolean(bKrank);
						oZeile = befuelleMitAnzahlTageStunden(oZeile2, tmZeitraum, personalDto.getIId(), theClientDto);
						alZeitraeume.add(oZeile2);
					}
				}

			}

		} else {
			// Wenn nur ein einziger Eintrag
			Object[] oZeile = new Object[REPORT_URLAUBSANTRAG_ANZAHL_SPALTEN];
			oZeile[REPORT_URLAUBSANTRAG_VON] = tVon;
			oZeile[REPORT_URLAUBSANTRAG_BIS] = tVon;

			oZeile[REPORT_URLAUBSANTRAG_URLAUB] = new Boolean(bUrlaub);
			oZeile[REPORT_URLAUBSANTRAG_KRANK] = new Boolean(bKrank);

			oZeile = befuelleMitAnzahlTageStunden(oZeile, tmZeitraum, personalDto.getIId(), theClientDto);

			alZeitraeume.add(oZeile);
		}

		Timestamp tErster = tmZeitraum.firstKey();
		Timestamp tLetzter = tmZeitraum.lastKey();

		Timestamp tAktuell = tErster;

		while (tAktuell.before(tLetzter)) {

			// Nun noch die Feiertage einsortieren

			String sBezeichnung = null;

			Integer religionIId = personalDto.getReligionIId();

			if (religionIId != null) {
				try {
					BetriebskalenderDto dtoTemp = getPersonalFac().betriebskalenderFindByMandantCNrDDatumReligionIId(
							tAktuell, personalDto.getMandantCNr(), religionIId);
					if (dtoTemp != null) {
						sBezeichnung = dtoTemp.getCBez();
					}
				} catch (RemoteException ex) {
					// keiner da
				}
			} else {

				BetriebskalenderDto dtoTemp = getPersonalFac().betriebskalenderFindByMandantCNrDDatum(tAktuell,
						personalDto.getMandantCNr(), theClientDto);
				if (dtoTemp != null) {
					sBezeichnung = dtoTemp.getCBez();
				}

			}

			// Wenns einen gibt, dann in TreeMap hinzufuegen
			if (sBezeichnung != null) {

				// Wenn nur ein einziger Eintrag
				Object[] oZeile = new Object[REPORT_URLAUBSANTRAG_ANZAHL_SPALTEN];
				oZeile[REPORT_URLAUBSANTRAG_VON] = new Timestamp(tAktuell.getTime() + 10);
				oZeile[REPORT_URLAUBSANTRAG_BIS] = new Timestamp(tAktuell.getTime() + 10);
				oZeile[REPORT_URLAUBSANTRAG_ZUSATZ] = sBezeichnung;

				oZeile[REPORT_URLAUBSANTRAG_URLAUB] = new Boolean(bUrlaub);
				oZeile[REPORT_URLAUBSANTRAG_KRANK] = new Boolean(bKrank);

				alZeitraeume.add(oZeile);

			}

			tAktuell = new Timestamp(tAktuell.getTime() + 24 * 3600000);

		}

		// Nun noch nach Datum sortieren damit die Feiertage richtig einsortiert
		// sind

		for (int m = alZeitraeume.size() - 1; m > 0; --m) {
			for (int n = 0; n < m; ++n) {
				Object[] o1 = (Object[]) alZeitraeume.get(n);
				Object[] o2 = (Object[]) alZeitraeume.get(n + 1);

				Timestamp tVon1 = (Timestamp) o1[REPORT_URLAUBSANTRAG_VON];
				Timestamp tVon2 = (Timestamp) o2[REPORT_URLAUBSANTRAG_VON];

				if (tVon1.after(tVon2)) {
					alZeitraeume.set(n, o2);
					alZeitraeume.set(n + 1, o1);

				}
			}
		}

		return alZeitraeume;

	}

	private int getYear(Timestamp ts) {
		long time = ts.getTime();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		int year = cal.get(Calendar.YEAR);
		return year;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public ZeiterfassungAenderungenReportDto printAenderungenNew(Integer personalIId,
			DatumsfilterVonBis dfZeitbuchungen, DatumsfilterVonBis dfAenderungen, boolean mitInserts,
			boolean mitUpdates, boolean mitDeletes, Integer sortierungsart, TheClientDto theClientDto) {

		sAktuellerReport = ZeiterfassungReportFac.REPORT_ZEITERFASSUNG_AENDERUNGEN;

		ZeiterfassungAenderungenReportDto returnDto = new ZeiterfassungAenderungenReportDto();
		PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(personalIId, theClientDto);

		returnDto.setPerson(
				new IdValueDto(personalDto.getPartnerIId(), personalDto.getPartnerDto().formatFixTitelName1Name2()));
		returnDto.setZeitbuchungenVon(dfZeitbuchungen.getTimestampVon());
		returnDto.setZeitbuchungenBis(dfZeitbuchungen.getTimestampBisUnveraendert());
		returnDto.setAenderungenVon(dfAenderungen != null ? dfAenderungen.getTimestampVon() : null);
		returnDto.setAenderungenBis(dfAenderungen != null ? dfAenderungen.getTimestampBisUnveraendert() : null);
		returnDto.setMitInserts(new Boolean(mitInserts));
		returnDto.setMitUpdates(new Boolean(mitUpdates));
		returnDto.setMitDeletes(new Boolean(mitDeletes));
		returnDto.setSortierungsart(sortierungsart);

		Session session = FLRSessionFactory.getFactory().openSession();
		HvTypedCriteria<FLREntitylog> crit = new HvTypedCriteria<FLREntitylog>(
				session.createCriteria(FLREntitylog.class, "elog"));

		crit.createAlias("elog.flrpersonal", "personal");
		crit.createAlias("personal.flrpartner", "partner");

		crit.add(Restrictions.eq("c_filter_key", HvDtoLogClass.ZEITDATEN));
		crit.add(Restrictions.eq("filter_i_id", personalIId + ""));
		crit.add(Restrictions.in("c_operation", returnDto.getOperationen()));

		crit.addOrder(Order.desc("i_id"));

		if (mitInserts == true || mitUpdates == true || mitDeletes == true) {
			if (mitInserts == false) {
				returnDto.removeOperation(REPORT_AENDERUNGEN_OP_INSERT);
			}
			if (mitUpdates == false) {
				returnDto.removeOperation(REPORT_AENDERUNGEN_OP_UPDATE);
			}
			if (mitDeletes == false) {
				returnDto.removeOperation(REPORT_AENDERUNGEN_OP_DELETE);
			}

			List<FLREntitylog> results = crit.list();
			returnDto.setItems(getListOfAenderungen(results, returnDto, dfZeitbuchungen, dfAenderungen, theClientDto));
		}

		returnDto.sortItems();

		return returnDto;
	}

	/**
	 * Erstellt die &Auml;nderungsliste nach den gegebenen Parametern. Die Elemente
	 * der Liste sind dabei Pairs aus {@link ZeiterfassungAenderungenItemDto}
	 * bestehend aus einem Von und Nach. "Von" (Key des Pairs) repr&auml;sentiert
	 * die Basisdaten bei einer UPDATE und DELETE Operation. "Nach" (Value des
	 * Pairs) wird bei einem INSERT mit den angelegten Daten bef&uuml;llt und bei
	 * UPDATE mit den aktualisierten Daten.
	 * 
	 * F&uuml;r UPDATE und DELETE Operationen wird in der Historie des Entitylogs
	 * nach den zu setzenden Pflichtfeldern gesucht und im "Von" gesetzt.
	 * 
	 * @param elogList        Liste der EntityLog Eintr&auml;ge
	 * @param reportDto       h&auml;lt die Basisdaten des Reports
	 * @param dfZeitbuchungen Datumsfilter der Zeitbuchungen
	 * @param dfAenderungen   Datumsfilter des &Auml;nderungszeitpunkte, optional
	 *                        gesetzt
	 * @param theClientDto    der aktuelle Benutzer
	 * @return Liste mit den gefundenen Pairs
	 */
	private List<Pair<ZeiterfassungAenderungenItemDto, ZeiterfassungAenderungenItemDto>> getListOfAenderungen(
			List<FLREntitylog> elogList, ZeiterfassungAenderungenReportDto reportDto,
			DatumsfilterVonBis dfZeitbuchungen, DatumsfilterVonBis dfAenderungen, TheClientDto theClientDto) {

		List<Pair<ZeiterfassungAenderungenItemDto, ZeiterfassungAenderungenItemDto>> list = new ArrayList<Pair<ZeiterfassungAenderungenItemDto, ZeiterfassungAenderungenItemDto>>();
		List<String> keys = reportDto.getKeys();
		List<String> operations = reportDto.getOperationen();

		ZeiterfassungAenderungenItemDto vonDto = null;
		ZeiterfassungAenderungenItemDto nachDto = null;
		Integer elogListSize = elogList.size();
		Integer idxHead = 0;

		while (idxHead < elogListSize) {
			FLREntitylog elog = elogList.get(idxHead);
			String op = elog.getC_operation().trim();

			if (operations.contains(op)
					&& (keys.contains(elog.getC_key()) || REPORT_AENDERUNGEN_OP_DELETE.equals(op))) {
				vonDto = new ZeiterfassungAenderungenItemDto();
				vonDto.setPersonAendern(new IdValueDto(elog.getFlrpersonal().getI_id(),
						elog.getFlrpersonal().getFlrpartner().getC_kbez()));
				vonDto.setOperation(elog.getC_operation().trim());
				vonDto.setEntityId(Integer.parseInt(elog.getEntity_i_id()));
				vonDto.setZeitAendern(new Timestamp(elog.getT_aendern().getTime()));
				nachDto = new ZeiterfassungAenderungenItemDto(vonDto);

				// Suche Gruppe zur&uuml;ck
				for (Integer idx = idxHead; idx >= 0; idx--) {
					FLREntitylog elogTemp = elogList.get(idx);

					if (meetAenderungenGroupCondition(elogTemp, vonDto)) {
						setPropertyOfAenderungenItem(elogTemp.getC_key(), elogTemp.getC_von(), vonDto, theClientDto);
						setPropertyOfAenderungenItem(elogTemp.getC_key(), elogTemp.getC_nach(), nachDto, theClientDto);
					} else {
						break;
					}
				}

				// Suche Gruppe nach vorne
				Integer idxForward = idxHead + 1;
				for (; idxForward < elogListSize; idxForward++) {
					FLREntitylog elogTemp = elogList.get(idxForward);

					if (meetAenderungenGroupCondition(elogTemp, vonDto)) {
						setPropertyOfAenderungenItem(elogTemp.getC_key(), elogTemp.getC_von(), vonDto, theClientDto);
						setPropertyOfAenderungenItem(elogTemp.getC_key(), elogTemp.getC_nach(), nachDto, theClientDto);
					} else {
						break;
					}
				}

				// Sammeln der Zusatzinformation bei UPDATE oder DELETE
				if (REPORT_AENDERUNGEN_OP_UPDATE.equals(vonDto.getOperation())
						|| REPORT_AENDERUNGEN_OP_DELETE.equals(vonDto.getOperation())) {
					Boolean insertDone = false;

					for (Integer idxInfo = idxForward; idxInfo < elogListSize; idxInfo++) {
						FLREntitylog elogTemp = elogList.get(idxInfo);

						if (vonDto.getEntityId().equals(Integer.parseInt(elogTemp.getEntity_i_id()))) {
							String opTemp = elogTemp.getC_operation().trim();
							String sValue = null;

							if (REPORT_AENDERUNGEN_OP_INSERT.equals(opTemp)) {
								sValue = elogTemp.getC_von();
								insertDone = true;
							} else if (REPORT_AENDERUNGEN_OP_UPDATE.equals(opTemp)) {
								sValue = elogTemp.getC_nach();
							} else {
								continue;
							}

							setPropertyOfAenderungenItem(elogTemp.getC_key(), sValue, vonDto, theClientDto);

							if ("CWowurdegebucht".equals(elogTemp.getC_key())) {
								setPropertyOfAenderungenItem(elogTemp.getC_key(), sValue, nachDto, theClientDto);
							}

							if (vonDto.sindPflichtfelderGesetzt())
								break;
						} else {
							if (insertDone)
								break;
						}
					}

					if (nachDto.istBeleg() == null) {
						nachDto.setIstBeleg(vonDto.istBeleg());
					}
				}

				if (elog.getT_aendern().before(dfZeitbuchungen.getTimestampVon())
						|| dfAenderungen.getTimestampVon() != null
								&& elog.getT_aendern().before(dfAenderungen.getTimestampVon())) {
					// Abbruchbedingung
					break;
				} else {
					if ((dateIsInRange(vonDto.getZeit(), dfZeitbuchungen.getTimestampVon(),
							dfZeitbuchungen.getTimestampBis())
							|| dateIsInRange(nachDto.getZeit(), dfZeitbuchungen.getTimestampVon(),
									dfZeitbuchungen.getTimestampBis()))
							&& (dfAenderungen.getTimestampBis() == null ? true
									: !elog.getT_aendern().after(dfAenderungen.getTimestampBis()))) {
						// Eintrag innerhalb der Datumsfilter?

						fetchBelegdatenBezeichnungen(vonDto.getBelegart(), vonDto, theClientDto);
						fetchBelegdatenBezeichnungen(
								nachDto.getBelegart() != null ? nachDto.getBelegart() : vonDto.getBelegart(), nachDto,
								theClientDto);

						if (REPORT_AENDERUNGEN_OP_INSERT.equals(vonDto.getOperation())) {
							list.add(new Pair<ZeiterfassungAenderungenItemDto, ZeiterfassungAenderungenItemDto>(nachDto,
									vonDto));
						} else {
							list.add(new Pair<ZeiterfassungAenderungenItemDto, ZeiterfassungAenderungenItemDto>(vonDto,
									nachDto));
						}
					}
					idxHead = idxForward;
				}
			} else {
				idxHead++;
			}
		}

		return list;
	}

	/**
	 * Pr&uuml;ft, ob ein Datum innerhalb eines Zeitraums liegt.
	 * 
	 * @param dPruefen das zu &uuml;berpr&uuml;fende Datum
	 * @param dVon     Beginn des Zeitraums
	 * @param dBis     Ende des Zeitraums
	 * @return true, wenn das Pr&uuml;fdatum innerhalb des Zeitbereichs liegt
	 */
	private Boolean dateIsInRange(Date dPruefen, Date dVon, Date dBis) {
		if (dPruefen != null) {
			if (dVon != null) {
				if (dPruefen.before(dVon))
					return false;
			}
			if (dBis != null) {
				if (dPruefen.after(dBis))
					return false;
			}
			return true;
		}

		return false;
	}

	/**
	 * Holt die Daten der Belege aus der Datenbank und speichert sie im
	 * {@link ZeiterfassungAenderungenItemDto} ab.
	 * 
	 * @param belegart     Art des Belegs
	 * @param itemDto      Datenobjekt des &Auml;nderungenreports
	 * @param theClientDto der aktuelle Benutzer
	 */
	private void fetchBelegdatenBezeichnungen(String belegart, ZeiterfassungAenderungenItemDto itemDto,
			TheClientDto theClientDto) {
		if (itemDto.getBeleg() != null) {
			Integer iId = itemDto.getBeleg().getId();

			if (LocaleFac.BELEGART_AUFTRAG.equals(belegart)) {
				AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(iId);
				if (auftragDto != null) {
					itemDto.setBeleg(new IdValueDto(iId, auftragDto.getCNr())); // auftragDto.getCBezProjektbezeichnung()));
				}
			} else if (LocaleFac.BELEGART_ANGEBOT.equals(belegart)) {
				AngebotDto angebotDto = getAngebotFac().angebotFindByPrimaryKeyOhneExec(iId);
				if (angebotDto != null) {
					itemDto.setBeleg(new IdValueDto(iId, angebotDto.getCNr()));// angebotDto.getCBez()));
				}
			} else if (LocaleFac.BELEGART_PROJEKT.equals(belegart)) {
				ProjektDto projektDto = getProjektFac().projektFindByPrimaryKeyOhneExc(iId);
				if (projektDto != null) {
					itemDto.setBeleg(new IdValueDto(iId, projektDto.getCNr()));// projektDto.getCTitel()));
				}
			} else if (LocaleFac.BELEGART_LOS.equals(belegart)) {
				LosDto losDto = getFertigungFac().losFindByPrimaryKeyOhneExc(iId);
				if (losDto != null) {
					itemDto.setBeleg(new IdValueDto(iId, losDto.getCNr()));
				}
			}
		}

		if (itemDto.getBelegPosition() != null) {
			Integer iId = itemDto.getBelegPosition().getId();
			String bez = iId.toString();

			if (LocaleFac.BELEGART_AUFTRAG.equals(belegart)) {
				AuftragpositionDto aposDto = getAuftragpositionFac().auftragpositionFindByPrimaryKeyOhneExc(iId);

				if (aposDto != null) {
					if (aposDto.getCBez() == null) {
						if (aposDto.getArtikelIId() != null) {
							ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(aposDto.getArtikelIId(),
									theClientDto);
							bez = artikelDto == null ? bez : artikelDto.getCNr();
						} else {
							bez = aposDto.getPositionsartCNr();
						}
					} else {
						bez = aposDto.getCBez();
					}
				}
			} else if (LocaleFac.BELEGART_ANGEBOT.equals(belegart)) {
				AngebotpositionDto aposDto = getAngebotpositionFac().angebotpositionFindByPrimaryKeyOhneExc(iId);

				if (aposDto != null) {
					if (aposDto.getCBez() == null) {
						if (aposDto.getArtikelIId() != null) {
							ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(aposDto.getArtikelIId(),
									theClientDto);
							bez = artikelDto == null ? bez : artikelDto.getCNr();
						} else {
							bez = aposDto.getPositionsartCNr();
						}
					} else {
						bez = aposDto.getCBez();
					}
				}
			} else if (LocaleFac.BELEGART_LOS.equals(belegart)) {
				LossollarbeitsplanDto lsaDto = getFertigungFac().lossollarbeitsplanFindByPrimaryKeyOhneExc(iId);

				if (lsaDto != null) {
					if (lsaDto.getArtikelIIdTaetigkeit() != null) {
						ArtikelDto artikelDto = getArtikelFac()
								.artikelFindByPrimaryKey(lsaDto.getArtikelIIdTaetigkeit(), theClientDto);

						if (artikelDto != null) {
							bez = artikelDto.getCNr();
						}
					}
				}
			}

			itemDto.setBelegPosition(new IdValueDto(iId, bez));
		}

	}

	/**
	 * &Uuml;berpr&uuml;ft, ob der Entitylog-Eintrag zum aktuellen
	 * &Auml;nderungsitem dazugeh&ouml;rt. &Uuml;bereinstimmen m&uuml;ssen die
	 * Operation, die Entity-Id und die Person, die die Zeitbuchung
	 * durchgef&uuml;hrt hat. Des weiteren muss der &Auml;nderungszeitpunkt
	 * zusammenpassen und in einem Toleranzbereich liegen.
	 * 
	 * @param elogTemp Entitylog-Eintrag
	 * @param itemDto  aktuelles &Auml;nderungsitem
	 * @return true, wenn der Entitylog-Eintrag zum aktuellen Item geh&ouml;rt
	 */
	private Boolean meetAenderungenGroupCondition(FLREntitylog elogTemp, ZeiterfassungAenderungenItemDto itemDto) {

		if (!itemDto.getOperation().equals(elogTemp.getC_operation().trim())) {
			return false;
		}
		if (!itemDto.getEntityId().equals(Integer.parseInt(elogTemp.getEntity_i_id()))) {
			return false;
		}
		if (!itemDto.getPersonAendern().getId().equals(elogTemp.getFlrpersonal().getI_id())) {
			return false;
		}
		if (!dateIsInRange(elogTemp.getT_aendern(),
				new Timestamp(itemDto.getZeitAendern().getTime() - AENDERUNGEN_TAENDERN_TOLERANZ_IN_MSEC),
				new Timestamp(itemDto.getZeitAendern().getTime() + AENDERUNGEN_TAENDERN_TOLERANZ_IN_MSEC))) {
			return false;
		}

		return true;
	}

	/**
	 * Setzt eine Eigenschaft ({@code key}) eines
	 * {@link ZeiterfassungAenderungenItemDto} mit der {@code value}
	 * 
	 * @param key          Schl&uuml;ssel zur Bestimmung welcher Variable gesetzt
	 *                     werden soll
	 * @param value        der zu setzende Wert
	 * @param itemDto      Datenobjekt des Reports
	 * @param theClientDto der aktuelle Benutzer
	 */
	private void setPropertyOfAenderungenItem(String key, String value, ZeiterfassungAenderungenItemDto itemDto,
			TheClientDto theClientDto) {
		if (value == null)
			value = "";

		try {
			if ("TZeit".equals(key) && itemDto.getZeit() == null) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				Timestamp parsedTimeStamp;
				try {
					parsedTimeStamp = new Timestamp(dateFormat.parse(value).getTime());
				} catch (ParseException e) {
					parsedTimeStamp = null;
				}

				itemDto.setZeit(parsedTimeStamp);
			} else if ("TaetigkeitIId".equals(key) && itemDto.getTaetigkeit() == null) {
				itemDto.setIstBeleg(false);
				Integer iValue = Integer.parseInt(value);
				TaetigkeitDto taDto = null;
				try {
					taDto = getZeiterfassungFac().taetigkeitFindByPrimaryKey(iValue, theClientDto);
				} catch (RemoteException e) {
				}
				itemDto.setTaetigkeit(new IdValueDto(iValue, taDto != null ? taDto.getCNr() : value));
			} else if ("IBelegartid".equals(key) && itemDto.getBeleg() == null) {
				itemDto.setIstBeleg(true);
				itemDto.setBeleg(new IdValueDto(Integer.parseInt(value), value));
			} else if ("IBelegartpositionid".equals(key) && itemDto.getBelegPosition() == null) {
				itemDto.setIstBeleg(true);
				itemDto.setBelegPosition(new IdValueDto(Integer.parseInt(value), value));
			} else if ("CBelegartnr".equals(key) && itemDto.getBelegart() == null) {
				itemDto.setIstBeleg(true);
				itemDto.setBelegart(value);
			} else if ("CWowurdegebucht".equals(key) && itemDto.getWoWurdeGebucht() == null) {
				itemDto.setWoWurdeGebucht(value);
			} else if ("ArtikelIId".equals(key)) {
				itemDto.setIstBeleg(true);
				itemDto.setArtikelCnr(value);
				ArtikelDto artikelDto = null;
				try {
					artikelDto = getArtikelFac().artikelFindByCNrOhneExc(value, theClientDto);
					if (artikelDto != null) {
						ArtikelsprDto artikelsprDto = getArtikelFac().artikelsprFindByArtikelIIdLocaleCNrOhneExc(
								artikelDto.getIId(), Helper.locale2String(theClientDto.getLocUi()), theClientDto);
						itemDto.setArtikelBez(artikelsprDto != null
								? artikelsprDto.getCKbez() != null ? artikelsprDto.getCKbez() : artikelsprDto.getCBez()
								: null);
					}
				} catch (RemoteException e) {
				}
			}
		} catch (NumberFormatException e) {
			// String ist leer, do nothing
		}
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAenderungen(Integer personalIId, DatumsfilterVonBis dfZeitbuchungen,
			DatumsfilterVonBis dfAenderungen, boolean mitInserts, boolean mitUpdates, boolean mitDeletes,
			Integer sortierungsart, TheClientDto theClientDto) {
		boolean useOld = false;
		if (useOld) {
			return printAenderungenOld(personalIId, dfZeitbuchungen, mitInserts, mitUpdates, mitDeletes, theClientDto);
		}

		ZeiterfassungAenderungenReportDto reportDto = printAenderungenNew(personalIId, dfZeitbuchungen, dfAenderungen,
				mitInserts, mitUpdates, mitDeletes, sortierungsart, theClientDto);
		setDataTransformer(new AenderungenTransformer(reportDto, theClientDto));

		initJRDS(jasperPrintTransformer.transformParameter(), ZeiterfassungReportFac.REPORT_MODUL,
				ZeiterfassungReportFac.REPORT_ZEITERFASSUNG_AENDERUNGEN, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	public JasperPrintLP printAenderungenOld(Integer personalIId, DatumsfilterVonBis datumsfilter, boolean mitInserts,
			boolean mitUpdates, boolean mitDeletes, TheClientDto theClientDto) {
		sAktuellerReport = ZeiterfassungReportFac.REPORT_ZEITERFASSUNG_AENDERUNGEN;

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(personalIId, theClientDto);
		parameter.put("P_PERSON", personalDto.getPartnerDto().formatFixTitelName1Name2());

		parameter.put("P_VON", datumsfilter.getTimestampVon());
		parameter.put("P_BIS", datumsfilter.getTimestampBisUnveraendert());

		parameter.put("P_MIT_INSERTS", new Boolean(mitInserts));
		parameter.put("P_MIT_UPDATES", new Boolean(mitUpdates));
		parameter.put("P_MIT_DELETES", new Boolean(mitDeletes));

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session.createCriteria(FLREntitylog.class);

		crit.add(Restrictions.eq("c_filter_key", HvDtoLogClass.ZEITDATEN));
		crit.add(Restrictions.eq("filter_i_id", personalIId + ""));

		crit.add(Restrictions.eq("c_key", "TZeit"));

		ArrayList<String> operationen = new ArrayList<String>();

		if (mitInserts == true || mitUpdates == true || mitDeletes == true) {
			if (mitInserts == true) {
				operationen.add("INSERT");
			}
			if (mitUpdates == true) {
				operationen.add("UPDATE");
			}
			if (mitDeletes == true) {
				operationen.add("DELETE");
			}

			crit.add(Restrictions.in("c_operation", operationen));
		} else {

			crit.add(Restrictions.eq("c_operation", "NIX"));

		}

		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		crit.add(Restrictions.ge("c_von", formatter.format(datumsfilter.getTimestampVon())));
		crit.add(Restrictions.lt("c_von", formatter.format(datumsfilter.getTimestampBis())));

		crit.addOrder(Order.asc("c_von"));
		List<?> results = crit.list();
		Iterator<?> resultListIterator = results.iterator();

		ArrayList alDaten = new ArrayList();

		while (resultListIterator.hasNext()) {
			FLREntitylog flrEntitylog = (FLREntitylog) resultListIterator.next();

			Object[] zeile = new Object[REPORT_ZEITERFASSUNG_AENDERUNGEN_ANZAHL_SPALTEN];

			zeile[REPORT_ZEITERFASSUNG_AENDERUNGEN_FELDNAME] = flrEntitylog.getC_key();

			zeile[REPORT_ZEITERFASSUNG_AENDERUNGEN_GEAENDERT_NACH] = flrEntitylog.getC_nach();
			zeile[REPORT_ZEITERFASSUNG_AENDERUNGEN_OPERATION] = flrEntitylog.getC_operation();

			zeile[REPORT_ZEITERFASSUNG_AENDERUNGEN_GEAENDERT_VON] = flrEntitylog.getC_von();
			zeile[REPORT_ZEITERFASSUNG_AENDERUNGEN_AENDERUNGSZEITPUNKT] = flrEntitylog.getT_aendern();

			alDaten.add(zeile);
		}

		Object[][] dataTemp = new Object[alDaten.size()][REPORT_ZEITERFASSUNG_AENDERUNGEN_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(dataTemp);

		initJRDS(parameter, ZeiterfassungReportFac.REPORT_MODUL,
				ZeiterfassungReportFac.REPORT_ZEITERFASSUNG_AENDERUNGEN, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);
		return getReportPrint();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAuftragszeitstatistik(java.sql.Timestamp tVon, java.sql.Timestamp tBis,
			TheClientDto theClientDto) {
		sAktuellerReport = ZeiterfassungReportFac.REPORT_AUFTRAGSZEITSTATISTIK;

		Integer tagesartIId_Feiertag = getZeiterfassungFac()
				.tagesartFindByCNr(ZeiterfassungFac.TAGESART_FEIERTAG, theClientDto).getIId();
		Integer tagesartIId_Halbtag = getZeiterfassungFac()
				.tagesartFindByCNr(ZeiterfassungFac.TAGESART_HALBTAG, theClientDto).getIId();

		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("P_VON", tVon);

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(tBis.getTime());
		c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);

		parameter.put("P_BIS", new java.sql.Timestamp(c.getTimeInMillis()));

		String sQueryAuftragzeiten = "SELECT distinct z.c_belegartnr, z.i_belegartid FROM FLRZeitdatenLos z WHERE z.flrpersonal.mandant_c_nr='"
				+ theClientDto.getMandant() + "' AND (z.c_belegartnr='" + LocaleFac.BELEGART_AUFTRAG
				+ "' OR z.c_belegartnr='" + LocaleFac.BELEGART_LOS + "'  OR z.c_belegartnr='"
				+ LocaleFac.BELEGART_ANGEBOT + "')";

		// WG MITTERNACHTSSPRUNG
		// Daten einen Tag vorher holen wg. Mitternachtssprung

		sQueryAuftragzeiten += " AND z.t_zeit>='"
				+ Helper.formatTimestampWithSlashes(Helper.cutTimestamp(new Timestamp(tVon.getTime() - (3600000 * 24))))
				+ "'";

		sQueryAuftragzeiten += " AND z.t_zeit<'" + Helper.formatTimestampWithSlashes(tBis) + "'";

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Query zeiten = session.createQuery(sQueryAuftragzeiten);

		List<?> resultList = zeiten.list();

		Iterator<?> resultListAuftraege = resultList.iterator();

		TreeMap hmPersonen = new TreeMap();
		HashMap hmBereitsGezaehlteAuftraege = new HashMap();
		HashMap hmBereitsGezaehlteAngebote = new HashMap();

		HashMap hmAnwesenheitzeitenImZeitraum = new HashMap();
		HashMap hmSondezeitenImZeitraum = new HashMap();

		TreeMap tmAlleAuftraege = new TreeMap();

		Double dAnwesenheitGesamt = new Double(0);
		BigDecimal dSonderzeitGesamt = new BigDecimal(0);

		while (resultListAuftraege.hasNext()) {
			Object[] o = (Object[]) resultListAuftraege.next();
			String belegartCNr = (String) o[0];
			Integer iBelegartIId = (Integer) o[1];

			if (belegartCNr.equals(LocaleFac.BELEGART_AUFTRAG)) {
				AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKeyOhneExc(iBelegartIId);
				if (auftragDto != null) {
					// Auftrag nur einmal zaehlen
					if (!hmBereitsGezaehlteAuftraege.containsKey(iBelegartIId)) {

						AuftragzeitenDto[] auftragszeitenEinesAuftragsDtos = getZeiterfassungFac()
								.getAllZeitenEinesBeleges(LocaleFac.BELEGART_AUFTRAG, iBelegartIId, null, null, tVon,
										tBis, ZeiterfassungFac.SORTIERUNG_ZEITDATEN_ARTIKEL, theClientDto);
						for (int i = 0; i < auftragszeitenEinesAuftragsDtos.length; i++) {
							AuftragzeitenDto auftragszeitDto = auftragszeitenEinesAuftragsDtos[i];
							add2PersonalHashMap(hmPersonen, auftragszeitDto.getsPersonNachnameVorname(),
									"AB" + auftragDto.getCNr(), auftragszeitDto.getDdDauer());

							add2PersonalHashMap(hmPersonen, "~GESAMTSUMME", "AB" + auftragDto.getCNr(),
									auftragszeitDto.getDdDauer());

							tmAlleAuftraege.put("AB" + auftragDto.getCNr(), auftragDto);

							if (!hmAnwesenheitzeitenImZeitraum
									.containsKey(auftragszeitDto.getsPersonNachnameVorname())) {

								Double dIstGesamt = new Double(0);
								Double dGesamt = getZeiterfassungFac().berechneArbeitszeitImZeitraum(
										auftragszeitDto.getIPersonalMaschinenId(), new java.sql.Date(tVon.getTime()),
										new java.sql.Date(tBis.getTime()), true, theClientDto);

								if (dGesamt != null) {
									dIstGesamt = dGesamt.doubleValue();
								}

								hmAnwesenheitzeitenImZeitraum.put(auftragszeitDto.getsPersonNachnameVorname(),
										dIstGesamt);
								dAnwesenheitGesamt += dIstGesamt;

							}

							if (!hmSondezeitenImZeitraum.containsKey(auftragszeitDto.getsPersonNachnameVorname())) {
								BigDecimal bdSonder = getZeiterfassungFac()
										.getStundenAllerBezahltenSondertaetigkeitenImZeitraum(
												auftragszeitDto.getIPersonalMaschinenId(), tagesartIId_Feiertag,
												tagesartIId_Halbtag, tVon, tBis, theClientDto);
								hmSondezeitenImZeitraum.put(auftragszeitDto.getsPersonNachnameVorname(), bdSonder);
								dSonderzeitGesamt = dSonderzeitGesamt.add(bdSonder);
							}

						}
						hmBereitsGezaehlteAuftraege.put(iBelegartIId, "");
					}
				}

			} else if (belegartCNr.equals(LocaleFac.BELEGART_ANGEBOT)) {
				AngebotDto angebotDto = null;
				try {
					angebotDto = getAngebotFac().angebotFindByPrimaryKey(iBelegartIId, theClientDto);
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
				if (angebotDto != null) {
					// Auftrag nur einmal zaehlen
					if (!hmBereitsGezaehlteAngebote.containsKey(iBelegartIId)) {

						AuftragzeitenDto[] auftragszeitenEinesAuftragsDtos = getZeiterfassungFac()
								.getAllZeitenEinesBeleges(LocaleFac.BELEGART_ANGEBOT, iBelegartIId, null, null, tVon,
										tBis, ZeiterfassungFac.SORTIERUNG_ZEITDATEN_ARTIKEL, theClientDto);
						for (int i = 0; i < auftragszeitenEinesAuftragsDtos.length; i++) {
							AuftragzeitenDto auftragszeitDto = auftragszeitenEinesAuftragsDtos[i];
							add2PersonalHashMap(hmPersonen, auftragszeitDto.getsPersonNachnameVorname(),
									"AG" + angebotDto.getCNr(), auftragszeitDto.getDdDauer());

							add2PersonalHashMap(hmPersonen, "~GESAMTSUMME", "AG" + angebotDto.getCNr(),
									auftragszeitDto.getDdDauer());

							tmAlleAuftraege.put("AG" + angebotDto.getCNr(), angebotDto);

							if (!hmAnwesenheitzeitenImZeitraum
									.containsKey(auftragszeitDto.getsPersonNachnameVorname())) {

								Double dIstGesamt = new Double(0);
								Double dGesamt = getZeiterfassungFac().berechneArbeitszeitImZeitraum(
										auftragszeitDto.getIPersonalMaschinenId(), new java.sql.Date(tVon.getTime()),
										new java.sql.Date(tBis.getTime()), true, theClientDto);

								if (dGesamt != null) {
									dIstGesamt = dGesamt.doubleValue();
								}

								hmAnwesenheitzeitenImZeitraum.put(auftragszeitDto.getsPersonNachnameVorname(),
										dIstGesamt);
								dAnwesenheitGesamt += dIstGesamt;

							}

							if (!hmSondezeitenImZeitraum.containsKey(auftragszeitDto.getsPersonNachnameVorname())) {
								BigDecimal bdSonder = getZeiterfassungFac()
										.getStundenAllerBezahltenSondertaetigkeitenImZeitraum(
												auftragszeitDto.getIPersonalMaschinenId(), tagesartIId_Feiertag,
												tagesartIId_Halbtag, tVon, tBis, theClientDto);
								hmSondezeitenImZeitraum.put(auftragszeitDto.getsPersonNachnameVorname(), bdSonder);
								dSonderzeitGesamt = dSonderzeitGesamt.add(bdSonder);
							}

						}
						hmBereitsGezaehlteAngebote.put(iBelegartIId, "");
					}
				}

			} else {
				// Lose

				try {
					LosDto losDto = getFertigungFac().losFindByPrimaryKey(iBelegartIId);
					if (losDto.getAuftragIId() != null) {
						AuftragDto auftragDtoLos = getAuftragFac().auftragFindByPrimaryKey(losDto.getAuftragIId());

						AuftragzeitenDto[] loszeitenDtos = getZeiterfassungFac().getAllZeitenEinesBeleges(
								LocaleFac.BELEGART_LOS, losDto.getIId(), null, null, tVon, tBis,
								ZeiterfassungFac.SORTIERUNG_ZEITDATEN_ARTIKEL, theClientDto);

						for (int j = 0; j < loszeitenDtos.length; j++) {
							AuftragzeitenDto loszeitDto = loszeitenDtos[j];

							add2PersonalHashMap(hmPersonen, loszeitDto.getsPersonNachnameVorname(),
									"AB" + auftragDtoLos.getCNr(), loszeitDto.getDdDauer());

							add2PersonalHashMap(hmPersonen, "~GESAMTSUMME", "AB" + auftragDtoLos.getCNr(),
									loszeitDto.getDdDauer());
							tmAlleAuftraege.put("AB" + auftragDtoLos.getCNr(), auftragDtoLos);

							if (!hmAnwesenheitzeitenImZeitraum.containsKey(loszeitDto.getsPersonNachnameVorname())) {

								Double dIstGesamt = new Double(0);
								Double dGesamt = getZeiterfassungFac().berechneArbeitszeitImZeitraum(
										loszeitDto.getIPersonalMaschinenId(), new java.sql.Date(tVon.getTime()),
										new java.sql.Date(tBis.getTime()), true, theClientDto);

								if (dGesamt != null) {
									dIstGesamt = dGesamt.doubleValue();
								}

								hmAnwesenheitzeitenImZeitraum.put(loszeitDto.getsPersonNachnameVorname(), dIstGesamt);
								dAnwesenheitGesamt += dIstGesamt;

							}

							if (!hmSondezeitenImZeitraum.containsKey(loszeitDto.getsPersonNachnameVorname())) {
								BigDecimal bdSonder = getZeiterfassungFac()
										.getStundenAllerBezahltenSondertaetigkeitenImZeitraum(
												loszeitDto.getIPersonalMaschinenId(), tagesartIId_Feiertag,
												tagesartIId_Halbtag, tVon, tBis, theClientDto);
								hmSondezeitenImZeitraum.put(loszeitDto.getsPersonNachnameVorname(), bdSonder);
								dSonderzeitGesamt = dSonderzeitGesamt.add(bdSonder);
							}

						}

					}
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}

		}

		// Subreport fuer Titel

		String[] fieldnames = new String[] { "Beleg", "Kunde", "Projekt" };
		Object[][] dataSub = new Object[tmAlleAuftraege.size()][fieldnames.length];

		// data = new
		// Object[hmPersonen.size()][REPORT_AUFTRAGSZEITSTATISTIK_ANZAHL_SPALTEN];
		setData(new Object[hmPersonen.size()][REPORT_AUFTRAGSZEITSTATISTIK_ANZAHL_SPALTEN]);

		Iterator it = hmPersonen.keySet().iterator();
		int i = 0;
		int iVorher = 0;
		while (it.hasNext()) {
			Object person = it.next();

			data[i][REPORT_AUFTRAGSZEITSTATISTIK_PERSON] = person;

			TreeMap tm = (TreeMap) hmPersonen.get(person);

			String[] fieldnamesAuft = new String[] { "Beleg", "Dauer" };
			Object[][] dataSubAuft = new Object[tmAlleAuftraege.size()][fieldnamesAuft.length];

			Iterator itAlleAufttmAlleAuftraege = tmAlleAuftraege.keySet().iterator();
			int j = 0;

			Double dGesamteAuftragszeit = new Double(0);

			boolean bSubreportKopfInitialisiert = false;

			while (itAlleAufttmAlleAuftraege.hasNext()) {
				String auftrag = (String) itAlleAufttmAlleAuftraege.next();
				dataSubAuft[j][0] = auftrag;
				if (bSubreportKopfInitialisiert == false) {

					String projekt = null;
					KundeDto kdDto = null;
					if (auftrag.startsWith("AB")) {

						AuftragDto auftragDto = (AuftragDto) tmAlleAuftraege.get(auftrag);

						projekt = auftragDto.getCBezProjektbezeichnung();

						kdDto = getKundeFac().kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(),
								theClientDto);
					} else if (auftrag.startsWith("AG")) {
						AngebotDto angebotDto = (AngebotDto) tmAlleAuftraege.get(auftrag);
						kdDto = getKundeFac().kundeFindByPrimaryKey(angebotDto.getKundeIIdAngebotsadresse(),
								theClientDto);
						projekt = angebotDto.getCBez();
					}

					dataSub[j][0] = auftrag;
					dataSub[j][1] = kdDto.getPartnerDto().getCName1nachnamefirmazeile1();
					dataSub[j][2] = projekt;
				}
				if (tm.containsKey(auftrag)) {

					Double dDauer = (Double) tm.get(auftrag);
					dataSubAuft[j][1] = dDauer;
					dGesamteAuftragszeit = dGesamteAuftragszeit + dDauer;
				}

				j++;
			}
			bSubreportKopfInitialisiert = true;

			data[i][REPORT_AUFTRAGSZEITSTATISTIK_SUBREPORT_AUFTRAEGE] = ((net.sf.jasperreports.engine.JRDataSource) new LPDatenSubreport(
					dataSubAuft, fieldnamesAuft));

			Double dAnwesenheitszeit = (Double) hmAnwesenheitzeitenImZeitraum.get(person);

			BigDecimal bdSonderzeit = (BigDecimal) hmSondezeitenImZeitraum.get(person);

			if (person.equals("~GESAMTSUMME")) {
				dAnwesenheitszeit = dAnwesenheitGesamt;
				bdSonderzeit = dSonderzeitGesamt;
			}

			if (dAnwesenheitszeit == null) {
				dAnwesenheitszeit = new Double(0);
			}

			data[i][REPORT_AUFTRAGSZEITSTATISTIK_NICHT_ZUORDENBAR] = new BigDecimal(
					dAnwesenheitszeit.doubleValue() - dGesamteAuftragszeit.doubleValue());
			data[i][REPORT_AUFTRAGSZEITSTATISTIK_SONDERZEITEN] = bdSonderzeit;

			i++;
		}

		parameter.put("SUBREPORT_AUFTRAEGE",
				((net.sf.jasperreports.engine.JRDataSource) new LPDatenSubreport(dataSub, fieldnames)));

		// Subreport fuer Zeilen

		initJRDS(parameter, ZeiterfassungReportFac.REPORT_MODUL, ZeiterfassungReportFac.REPORT_AUFTRAGSZEITSTATISTIK,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();
	}

	private void add2PersonalHashMap(TreeMap hmPersonen, String person, String auftragCNr, Double dDauer) {
		TreeMap hmAuftraege = null;
		if (hmPersonen.containsKey(person)) {
			hmAuftraege = (TreeMap) hmPersonen.get(person);
		} else {
			hmAuftraege = new TreeMap();
		}

		Double dDauerVorhanden = new Double(0);
		if (hmAuftraege.containsKey(auftragCNr)) {
			dDauerVorhanden = (Double) hmAuftraege.get(auftragCNr);
		}

		dDauerVorhanden = new Double(dDauerVorhanden + dDauer);

		hmAuftraege.put(auftragCNr, dDauerVorhanden);

		hmPersonen.put(person, hmAuftraege);

	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printArbeitszeitstatistik(ArbeitszeitstatistikJournalKriterienDto krit,
			TheClientDto theClientDto) throws RemoteException {

		sAktuellerReport = ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK;

		if (krit.dVon == null || krit.dBis == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("tVon == null || tBis == null"));
		}
		Timestamp tVon = Helper.cutTimestamp(new Timestamp(krit.dVon.getTime()));
		Timestamp tBis = Helper.cutTimestamp(new Timestamp(krit.dBis.getTime()));

		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("P_VON", tVon);

		parameter.put("P_BELEGART", krit.belegartCnr);
		parameter.put("P_BELEGNUMMER", krit.belegnummerFuerReport);

		parameter.put("P_MIT_ERLEDIGTEN_PROJEKTEN", krit.mitErledigtenProjekten);
		parameter.put("P_INTERN_ERLEDIGT_BERUECKSICHTIGEN", krit.projektInterneErledigungBeruecksichtigen);

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(krit.dBis.getTime());
		c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);

		parameter.put("P_BIS", new java.sql.Timestamp(c.getTimeInMillis()));

		//SP9643 Achtung: Hier muss dBis um einen Tag spaeter angegeben werden, ueblicherweise
		//machen wir das in den Server-Routinen selbst. dBis passt dann fuer die DB-Query, aber
		// nicht fuer die Anzeige, deshalb wird P_BIS um einen Tag vermindert.
		// Bei Aenderung auch an ArbeitszeitstatistikReporter.java denken
		Session session = FLRSessionFactory.getFactory().openSession();
		String queryString = "SELECT distinct z.c_belegartnr, z.i_belegartid FROM FLRZeitdaten as z"
				+ " WHERE z.t_zeit>='" + Helper.formatDateWithSlashes(krit.dVon) + "'" + " AND z.t_zeit<'"
				+ Helper.formatDateWithSlashes(krit.dBis) + "'" + " AND z.flrpersonal.mandant_c_nr='"
				+ theClientDto.getMandant() + "'";

		// PJ17944
		if (LocaleFac.BELEGART_AUFTRAG.equals(krit.belegartCnr)) {
			if (krit.belegartIId != null) {

				LosDto[] losDtos = getFertigungFac().losFindByAuftragIId(krit.belegartIId);

				if (losDtos.length > 0) {

					String in = "(";
					for (int i = 0; i < losDtos.length; i++) {
						in += losDtos[i].getIId();

						if (i != losDtos.length - 1) {
							in += ",";
						}
					}
					in += ")";

					queryString += " AND ( (z.c_belegartnr='" + krit.belegartCnr + "' AND z.i_belegartid="
							+ krit.belegartIId + ") OR (z.c_belegartnr='" + LocaleFac.BELEGART_LOS
							+ "' AND z.i_belegartid IN " + in + "))";

				} else {
					queryString += " AND z.c_belegartnr='" + krit.belegartCnr + "'";
					queryString += " AND z.i_belegartid=" + krit.belegartIId;
				}

			} else {
				queryString += " AND z.c_belegartnr='" + krit.belegartCnr + "'";
			}

		} else {
			if (krit.belegartCnr != null) {
				queryString += " AND z.c_belegartnr='" + krit.belegartCnr + "'";
			}
			if (krit.belegartIId != null) {
				queryString += " AND z.i_belegartid=" + krit.belegartIId;
			}
		}

		if (krit.personalIId != null) {
			queryString += " AND z.personal_i_id=" + krit.personalIId;
			parameter.put("P_PERSON",
					getPersonalFac().personalFindByPrimaryKey(krit.personalIId, theClientDto).formatFixName1Name2());

		}
		if (krit.artikelIId != null) {
			queryString += " AND z.artikel_i_id=" + krit.artikelIId;
			parameter.put("P_TAETIGKEIT", getArtikelFac().artikelFindByPrimaryKeySmall(krit.artikelIId, theClientDto)
					.formatArtikelbezeichnung());
		}
		if (krit.artikelgruppeIId != null) {
			queryString += " AND z.flrartikel.flrartikelgruppe.i_id=" + krit.artikelgruppeIId;
			parameter.put("P_ARTIKELGRUPPE",
					getArtikelFac().artgruFindByPrimaryKey(krit.artikelgruppeIId, theClientDto).getBezeichnung());
		}
		if (krit.artikelklasseIId != null) {
			queryString += " AND z.flrartikel.flrartikelklasse.i_id=" + krit.artikelklasseIId;
			parameter.put("P_ARTIKELKLASSE",
					getArtikelFac().artklaFindByPrimaryKey(krit.artikelklasseIId, theClientDto).getBezeichnung());
		}

		// Sortieren
		if (krit.sortierung == ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_ARTIKELGRUPPE) {
			parameter.put("P_SORTIERUNG",
					getTextRespectUISpr("lp.artikelgruppe", theClientDto.getMandant(), theClientDto.getLocUi()));
		} else if (krit.sortierung == ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_ARTIKELKLASSE) {
			parameter.put("P_SORTIERUNG",
					getTextRespectUISpr("lp.artikelklasse", theClientDto.getMandant(), theClientDto.getLocUi()));
		} else if (krit.sortierung == ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_AUFTRAG) {
			parameter.put("P_SORTIERUNG", getTextRespectUISpr("pers.arbeitszeitstatistik.sortierung.belegpersonal",
					theClientDto.getMandant(), theClientDto.getLocUi()));
		} else if (krit.sortierung == ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_KUNDE_BELEG_PERSONAL) {
			parameter.put("P_SORTIERUNG", getTextRespectUISpr("pers.arbeitszeitstatistik.sortierung.kundebelegpersonal",
					theClientDto.getMandant(), theClientDto.getLocUi()));
		} else if (krit.sortierung == ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_PERSONAL) {
			parameter.put("P_SORTIERUNG",
					getTextRespectUISpr("lp.personal", theClientDto.getMandant(), theClientDto.getLocUi()));
		} else if (krit.sortierung == ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_KOSTENSTELLE) {
			parameter.put("P_SORTIERUNG",
					getTextRespectUISpr("lp.kostenstelle", theClientDto.getMandant(), theClientDto.getLocUi()));
		} else if (krit.sortierung == ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_ADRESSE) {
			parameter.put("P_SORTIERUNG", getTextRespectUISpr("pers.arbeitszeitstatistik.sortierung.kundepersonal",
					theClientDto.getMandant(), theClientDto.getLocUi()));
		} else if (krit.sortierung == ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_ARTIKEL) {
			parameter.put("P_SORTIERUNG",
					getTextRespectUISpr("lp.artikel", theClientDto.getMandant(), theClientDto.getLocUi()));
		} else if (krit.sortierung == ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_KUNDE_DATUM_PERSONAL_ARTIKEL) {
			parameter.put("P_SORTIERUNG",
					getTextRespectUISpr("pers.arbeitszeitstatistik.sortierung.kundedatumpersonalartikel",
							theClientDto.getMandant(), theClientDto.getLocUi()));
		}

		javax.persistence.Query queryBelegarten = em.createNamedQuery("BelegartfindAll");

		Collection belegarten = queryBelegarten.getResultList();

		HashMap hmBelegarten = new HashMap();

		Iterator itBel = belegarten.iterator();
		while (itBel.hasNext()) {
			Belegart b = (Belegart) itBel.next();
			hmBelegarten.put(b.getCNr(), b.getCKbez());
		}

		TreeMap<Object, ArbeitszeitstatistikDto> alleDaten = new TreeMap<Object, ArbeitszeitstatistikDto>();
		int sort = 0;

		HashMap kostenstellen = new HashMap();

		HashSet<Integer> hsBereitsBeruecksichtigteProjekte = new HashSet();

		Query query = session.createQuery(queryString);
		List<?> resultList = query.list();
		Iterator<?> resultListIterator = resultList.iterator();
		while (resultListIterator.hasNext()) {
			Object[] o = (Object[]) resultListIterator.next();
			if (o[0] != null && o[1] != null) {
				try {
					AuftragzeitenDto[] dtos = getZeiterfassungFac().getAllZeitenEinesBeleges((String) o[0],
							(Integer) o[1], null, krit.personalIId, tVon, tBis,
							ZeiterfassungFac.SORTIERUNG_ZEITDATEN_ARTIKEL, theClientDto);

					String fertigungsgruppe = "";
					String vertreter = "";
					String kundenbestellnummer = null;

					String projektStatus = null;
					String projektKategorie = null;

					Integer projektIId = null;
					String projektBereich = null;

					Integer sProjektVerrechenbar = null;
					Timestamp tProjektInternErledigt = null;
					String sProjektTyp = null;

					BigDecimal bdProjektGesamtdauer = null;

					if (((String) o[0]).equals(LocaleFac.BELEGART_LOS)) {
						LosDto losDto = getFertigungFac().losFindByPrimaryKey((Integer) o[1]);
						if (losDto.getFertigungsgruppeIId() != null) {
							fertigungsgruppe = getStuecklisteFac()
									.fertigungsgruppeFindByPrimaryKey(losDto.getFertigungsgruppeIId()).getCBez();
						}
					} else if (((String) o[0]).equals(LocaleFac.BELEGART_AUFTRAG)) {
						AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey((Integer) o[1]);

						if (auftragDto.getPersonalIIdVertreter() != null) {
							vertreter = getPersonalFac()
									.personalFindByPrimaryKey(auftragDto.getPersonalIIdVertreter(), theClientDto)
									.formatAnrede();
						}

						kundenbestellnummer = auftragDto.getCBestellnummer();

					} else if (((String) o[0]).equals(LocaleFac.BELEGART_PROJEKT)) {
						ProjektDto projektDto = getProjektFac().projektFindByPrimaryKey((Integer) o[1]);

						projektStatus = projektDto.getStatusCNr();
						projektKategorie = projektDto.getKategorieCNr();

						projektIId = projektDto.getIId();
						projektBereich = getProjektServiceFac().bereichFindByPrimaryKey(projektDto.getBereichIId())
								.getCBez();

						sProjektVerrechenbar = projektDto.getIVerrechenbar();
						tProjektInternErledigt = projektDto.getTInternerledigt();
						sProjektTyp = projektDto.getProjekttypCNr();

						if (krit.mitErledigtenProjekten) {
							bdProjektGesamtdauer = new BigDecimal(
									getZeiterfassungFac().getSummeZeitenEinesBeleges(LocaleFac.BELEGART_PROJEKT,
											projektDto.getIId(), null, null, null, null, theClientDto));
						}

						hsBereitsBeruecksichtigteProjekte.add(projektDto.getIId());

					}

					BelegInfos belegInfo = getLagerFac().getBelegInfos((String) o[0], (Integer) o[1], null,
							theClientDto);

					for (int i = 0; i < dtos.length; i++) {

						ArbeitszeitstatistikDto arbeitszeitstatistikDto = new ArbeitszeitstatistikDto();
						arbeitszeitstatistikDto.setAuftragzeitenDto(dtos[i]);
						arbeitszeitstatistikDto.setPartnerIId(belegInfo.getPartnerIId());
						arbeitszeitstatistikDto.setsFertigungsgruppe(fertigungsgruppe);
						arbeitszeitstatistikDto.setSProjektKategorie(projektKategorie);
						arbeitszeitstatistikDto.setSProjektStatus(projektStatus);
						arbeitszeitstatistikDto.setSProjektBereich(projektBereich);
						arbeitszeitstatistikDto.setIProjektIId(projektIId);

						// SP5155
						arbeitszeitstatistikDto.setsProjektVerrechenbar(sProjektVerrechenbar);
						arbeitszeitstatistikDto.setTProjektInternErledigt(tProjektInternErledigt);
						arbeitszeitstatistikDto.setSProjektTyp(sProjektTyp);

						arbeitszeitstatistikDto.setBdProjektGesamtdauer(bdProjektGesamtdauer);
						arbeitszeitstatistikDto.setSKundenbestellnummer(kundenbestellnummer);
						arbeitszeitstatistikDto.setSVertreter(vertreter);
						arbeitszeitstatistikDto.setSbeleg(hmBelegarten.get(o[0]) + belegInfo.getBelegnummer());
						arbeitszeitstatistikDto.setSBelegbezeichnung(belegInfo.getBelegbezeichnung());
						arbeitszeitstatistikDto.setSKunde(belegInfo.getKundeLieferant());

						// PJ21602
						if (dtos[i].getZeitdatenIIdBelegbuchung() != null) {
							BigDecimal bdBereitsVerrechnet = getAbrechnungsvorschlagFac()
									.getSummeZeitdatenVerrechnet(dtos[i].getZeitdatenIIdBelegbuchung());
							arbeitszeitstatistikDto.setBdBereitsverrechnet(bdBereitsVerrechnet);
						}

						if (!kostenstellen.containsKey(dtos[i].getIPersonalMaschinenId())) {

							PersonalDto personalDto = getPersonalFac()
									.personalFindByPrimaryKey(dtos[i].getIPersonalMaschinenId(), theClientDto);

							if (personalDto.getKostenstelleDto_Stamm() != null) {
								kostenstellen.put(dtos[i].getIPersonalMaschinenId(),
										personalDto.getKostenstelleDto_Stamm().getCNr());
							} else {
								kostenstellen.put(dtos[i].getIPersonalMaschinenId(), "");
							}

						}

						arbeitszeitstatistikDto
								.setSKostenstelle((String) kostenstellen.get(dtos[i].getIPersonalMaschinenId()));

						// Wenn artikelIId NOT NULL dann herausfiltern
						if (krit.artikelIId != null) {
							if (!krit.artikelIId
									.equals(arbeitszeitstatistikDto.getAuftragzeitenDto().getArtikelIId())) {
								continue;
							}
						}
						// Wenn partnerIId NOT NULL dann herausfiltern
						if (krit.partnerIId != null) {
							if (!krit.partnerIId.equals(arbeitszeitstatistikDto.getPartnerIId())) {
								continue;
							}
						}
						// Wenn artikelgruppeIId NOT NULL dann herausfiltern
						if (krit.artikelgruppeIId != null) {
							if (!krit.artikelgruppeIId
									.equals(arbeitszeitstatistikDto.getAuftragzeitenDto().getArtikelgruppeIId())) {
								continue;
							}
						}

						// Wenn artikelgruppeIId NOT NULL dann herausfiltern
						if (krit.artikelklasseIId != null) {
							if (!krit.artikelklasseIId
									.equals(arbeitszeitstatistikDto.getAuftragzeitenDto().getArtikelklasseIId())) {
								continue;
							}
						}

						if (arbeitszeitstatistikDto.getAuftragzeitenDto().getArtikelgruppeIId() != null) {
							arbeitszeitstatistikDto
									.setSArtikelgruppe(
											getArtikelFac()
													.artgruFindByPrimaryKey(arbeitszeitstatistikDto
															.getAuftragzeitenDto().getArtikelgruppeIId(), theClientDto)
													.getBezeichnung());
						}
						if (arbeitszeitstatistikDto.getAuftragzeitenDto().getArtikelklasseIId() != null) {
							arbeitszeitstatistikDto
									.setSArtikelklasse(
											getArtikelFac()
													.artklaFindByPrimaryKey(arbeitszeitstatistikDto
															.getAuftragzeitenDto().getArtikelklasseIId(), theClientDto)
													.getBezeichnung());
						}

						if (krit.sortierung == ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_ARTIKELGRUPPE) {
							arbeitszeitstatistikDto.setSGruppierung(arbeitszeitstatistikDto.getSArtikelgruppe());
							alleDaten.put(arbeitszeitstatistikDto.getSArtikelgruppe() + dtos[i].getTsBeginn() + sort,
									arbeitszeitstatistikDto);
						} else if (krit.sortierung == ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_ARTIKELKLASSE) {
							arbeitszeitstatistikDto.setSGruppierung(arbeitszeitstatistikDto.getSArtikelklasse());
							alleDaten.put(arbeitszeitstatistikDto.getSArtikelklasse() + dtos[i].getTsBeginn() + sort,
									arbeitszeitstatistikDto);
						} else if (krit.sortierung == ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_ADRESSE) {
							arbeitszeitstatistikDto.setSGruppierung(arbeitszeitstatistikDto.getSKunde() + "");
							alleDaten
									.put(Helper.fitString2Length(arbeitszeitstatistikDto.getSKunde(), 80, ' ')
											+ Helper.fitString2Length(arbeitszeitstatistikDto.getAuftragzeitenDto()
													.getSPersonalMaschinenname(), 80, ' ')
											+ dtos[i].getTsBeginn() + sort, arbeitszeitstatistikDto);
						} else if (krit.sortierung == ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_ARTIKEL) {
							arbeitszeitstatistikDto
									.setSGruppierung(arbeitszeitstatistikDto.getAuftragzeitenDto().getSArtikelcnr());
							alleDaten.put(arbeitszeitstatistikDto.getAuftragzeitenDto().getSArtikelcnr()
									+ dtos[i].getTsBeginn() + sort, arbeitszeitstatistikDto);
						} else if (krit.sortierung == ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_KOSTENSTELLE) {
							arbeitszeitstatistikDto.setSGruppierung(arbeitszeitstatistikDto.getSKostenstelle());
							alleDaten.put(arbeitszeitstatistikDto.getSKostenstelle()
									+ arbeitszeitstatistikDto.getAuftragzeitenDto().getSPersonalMaschinenname()
									+ dtos[i].getTsBeginn() + sort, arbeitszeitstatistikDto);
						} else if (krit.sortierung == ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_PERSONAL) {
							arbeitszeitstatistikDto.setSGruppierung(
									arbeitszeitstatistikDto.getAuftragzeitenDto().getSPersonalnummer());
							alleDaten.put(arbeitszeitstatistikDto.getAuftragzeitenDto().getSPersonalMaschinenname()
									+ dtos[i].getTsBeginn() + sort, arbeitszeitstatistikDto);
						} else if (krit.sortierung == ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_AUFTRAG) {
							arbeitszeitstatistikDto.setSGruppierung(Helper
									.fitString2Length(arbeitszeitstatistikDto.getSbeleg(), 15, ' ')
									+ arbeitszeitstatistikDto.getAuftragzeitenDto().getSPersonalMaschinenname());

							alleDaten.put(arbeitszeitstatistikDto.getSbeleg()
									+ arbeitszeitstatistikDto.getAuftragzeitenDto().getSPersonalMaschinenname()
									+ dtos[i].getTsBeginn() + sort, arbeitszeitstatistikDto);
						} else if (krit.sortierung == ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_KUNDE_BELEG_PERSONAL) {
							arbeitszeitstatistikDto.setSGruppierung(Helper
									.fitString2Length(arbeitszeitstatistikDto.getSKunde(), 60, ' ')
									+ Helper.fitString2Length(arbeitszeitstatistikDto.getSbeleg(), 15, ' ')
									+ arbeitszeitstatistikDto.getAuftragzeitenDto().getSPersonalMaschinenname());

							alleDaten
									.put(Helper.fitString2Length(arbeitszeitstatistikDto.getSKunde(), 80, ' ')
											+ arbeitszeitstatistikDto.getSbeleg()
											+ Helper.fitString2Length(arbeitszeitstatistikDto.getAuftragzeitenDto()
													.getSPersonalMaschinenname(), 80, ' ')
											+ dtos[i].getTsBeginn() + sort, arbeitszeitstatistikDto);
						} else if (krit.sortierung == ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_KUNDE_DATUM_PERSONAL_ARTIKEL) {
							arbeitszeitstatistikDto.setSGruppierung(
									Helper.fitString2Length(arbeitszeitstatistikDto.getSKunde(), 60, ' ')
											+ Helper.cutTimestamp(
													arbeitszeitstatistikDto.getAuftragzeitenDto().getTsBeginn())
													.getTime()
											+ arbeitszeitstatistikDto.getAuftragzeitenDto().getSPersonalMaschinenname()
											+ Helper.fitString2Length(
													arbeitszeitstatistikDto.getAuftragzeitenDto().getSArtikelcnr(), 50,
													' '));

							alleDaten.put(Helper.fitString2Length(arbeitszeitstatistikDto.getSKunde(), 80, ' ')
									+ Helper.cutTimestamp(arbeitszeitstatistikDto.getAuftragzeitenDto().getTsBeginn())
											.getTime()
									+ Helper.fitString2Length(
											arbeitszeitstatistikDto.getAuftragzeitenDto().getSPersonalMaschinenname(),
											80, ' ')
									+ Helper.fitString2Length(
											arbeitszeitstatistikDto.getAuftragzeitenDto().getSArtikelcnr(), 50, ' ')
									+ dtos[i].getTsBeginn() + sort, arbeitszeitstatistikDto);
						}
						sort++;
					}
				} catch (RemoteException ex) {
					throwEJBExceptionLPRespectOld(ex);
				}
			}
		}

		// Telefonzeiten hinzufuegen

		int iOption = 0;

		try {
			ParametermandantDto parameterOption = (ParametermandantDto) getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_PERSONALKOSTEN_QUELLE);

			iOption = ((Integer) parameterOption.getCWertAsObject()).intValue();

		} catch (RemoteException ex5) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex5);
		}

		if (krit.belegartCnr == null || krit.belegartCnr.equals(LocaleFac.BELEGART_PROJEKT)) {
			ArtikelDto artikelDto_DefaultAZ = getZeiterfassungFac().getDefaultArbeitszeitartikel(theClientDto);

			if (krit.artikelIId == null || krit.artikelIId.equals(artikelDto_DefaultAZ.getIId())) {

				if (krit.artikelgruppeIId == null
						|| krit.artikelgruppeIId.equals(artikelDto_DefaultAZ.getArtgruIId())) {

					if (krit.artikelklasseIId == null
							|| krit.artikelklasseIId.equals(artikelDto_DefaultAZ.getArtklaIId())) {

						Session sessionTel = FLRSessionFactory.getFactory().openSession();

						String sQuery = "SELECT t FROM FLRTelefonzeiten as t "

								+ " WHERE t.t_von>='" + Helper.formatDateWithSlashes(krit.dVon) + "' AND t.t_bis<'"
								+ Helper.formatDateWithSlashes(krit.dBis) + "' ";

						sQuery += " AND t.flrpersonal.mandant_c_nr='" + theClientDto.getMandant() + "' ";

						if (krit.partnerIId != null) {
							sQuery += " AND t.partner_i_id=" + krit.partnerIId + " ";
						}

						if (krit.personalIId != null) {
							sQuery += " AND t.personal_i_id=" + krit.personalIId + " ";
						}

						if (LocaleFac.BELEGART_PROJEKT.equals(krit.belegartCnr)) {
							if (krit.belegartIId != null) {
								sQuery += " AND t.projekt_i_id=" + krit.belegartIId + " ";
							}
						}

						Query queryTelefon = sessionTel.createQuery(sQuery);

						List<?> resultListTelefon = queryTelefon.list();

						Iterator<?> resultListIteratorTelefon = resultListTelefon.iterator();

						while (resultListIteratorTelefon.hasNext()) {
							FLRTelefonzeiten tel = (FLRTelefonzeiten) resultListIteratorTelefon.next();
							sort++;

							ArbeitszeitstatistikDto arbeitszeitstatistikDto = new ArbeitszeitstatistikDto();

							AuftragzeitenDto auftragzeitenDto = new AuftragzeitenDto();

							auftragzeitenDto.setTsBeginn(new Timestamp(tel.getT_von().getTime()));
							auftragzeitenDto.setTsEnde(new Timestamp(tel.getT_bis().getTime()));

							// PJ21602
							if (tel.getI_id() != null) {
								BigDecimal bdBereitsVerrechnet = getAbrechnungsvorschlagFac()
										.getSummeTelefonzeitenVerrechnet(tel.getI_id());
								arbeitszeitstatistikDto.setBdBereitsverrechnet(bdBereitsVerrechnet);
							}

							// PJ20739
							auftragzeitenDto.setFVerrechenbarInProzent(tel.getF_verrechenbar());
							if (tel.getT_erledigt() != null) {
								auftragzeitenDto.setTsErledigt(new Timestamp(tel.getT_erledigt().getTime()));
							}
							if (tel.getFlrpersonal_erledigt() != null) {
								auftragzeitenDto.setSPersonalKurzzeichenErledigt(
										tel.getFlrpersonal_erledigt().getC_kurzzeichen());
							}

							java.sql.Time tDauer = new java.sql.Time(
									tel.getT_bis().getTime() - tel.getT_von().getTime());
							tDauer.setTime(tDauer.getTime() - 3600000);
							auftragzeitenDto.setTDauer(tDauer);
							Double dDauer = Helper.time2Double(tDauer);
							auftragzeitenDto.setDdDauer(dDauer);

							auftragzeitenDto.setSPersonalKurzzeichen(tel.getFlrpersonal().getC_kurzzeichen());

							String sName = tel.getFlrpersonal().getFlrpartner().getC_name1nachnamefirmazeile1();
							if (tel.getFlrpersonal().getFlrpartner().getC_name2vornamefirmazeile2() != null) {
								sName = tel.getFlrpersonal().getFlrpartner().getC_name2vornamefirmazeile2() + " "
										+ sName;
							}

							auftragzeitenDto.setSPersonalnummer(tel.getFlrpersonal().getC_personalnummer());

							auftragzeitenDto.setSPersonalMaschinenname(sName);
							auftragzeitenDto.setSArtikelcnr(artikelDto_DefaultAZ.getCNr());

							if (artikelDto_DefaultAZ.getArtikelsprDto() != null) {
								auftragzeitenDto
										.setSArtikelbezeichnung(artikelDto_DefaultAZ.getArtikelsprDto().getCBez());
							}

							auftragzeitenDto.setSZeitbuchungtext(tel.getX_kommentarext());

							BigDecimal bdKosten = getZeiterfassungFac().getPersonalKostenProStunde(theClientDto, null,
									iOption, artikelDto_DefaultAZ.getIId(), tel.getPersonal_i_id(),
									new Timestamp(tel.getT_von().getTime()));

							auftragzeitenDto.setBdKosten(bdKosten.multiply(new BigDecimal(dDauer)));

							arbeitszeitstatistikDto.setAuftragzeitenDto(auftragzeitenDto);
							arbeitszeitstatistikDto.setPartnerIId(tel.getPartner_i_id());
							arbeitszeitstatistikDto.setsFertigungsgruppe("");
							arbeitszeitstatistikDto.setSVertreter("");

							// Je nachdem ob ein Projekt hinterlegt ist oder
							// nicht, sieht die Projektnummer anders aus
							Integer partnerIIdTelefon = null;

							if (tel.getProjekt_i_id() != null) {
								ProjektDto pjDto = getProjektFac().projektFindByPrimaryKey(tel.getProjekt_i_id());
								partnerIIdTelefon = pjDto.getPartnerIId();
								arbeitszeitstatistikDto.setSbeleg(
										hmBelegarten.get(LocaleFac.BELEGART_PROJEKT) + pjDto.getCNr() + "TZ");

								arbeitszeitstatistikDto.setSBelegbezeichnung(pjDto.getCTitel());
								arbeitszeitstatistikDto.setSProjektStatus(pjDto.getStatusCNr());
								arbeitszeitstatistikDto.setSProjektKategorie(pjDto.getKategorieCNr());

								arbeitszeitstatistikDto.setSProjektTyp(pjDto.getProjekttypCNr());
								arbeitszeitstatistikDto.setTProjektInternErledigt(pjDto.getTInternerledigt());
								arbeitszeitstatistikDto.setsProjektVerrechenbar(pjDto.getIVerrechenbar());

								arbeitszeitstatistikDto.setIProjektIId(pjDto.getIId());
								arbeitszeitstatistikDto.setSProjektBereich(getProjektServiceFac()
										.bereichFindByPrimaryKey(pjDto.getBereichIId()).getCBez());

								hsBereitsBeruecksichtigteProjekte.add(tel.getProjekt_i_id());

							} else {
								arbeitszeitstatistikDto
										.setSbeleg("TZ" + Helper.formatDatum(tel.getT_von(), theClientDto.getLocUi()));
								partnerIIdTelefon = tel.getPartner_i_id();
							}

							if (partnerIIdTelefon != null) {

								PartnerDto partnerDto_Telefon = getPartnerFac()
										.partnerFindByPrimaryKey(partnerIIdTelefon, theClientDto);

								arbeitszeitstatistikDto.setSKunde(partnerDto_Telefon.formatFixName1Name2());
							} else {
								arbeitszeitstatistikDto.setSKunde("");
							}

							if (!kostenstellen.containsKey(tel.getPersonal_i_id())) {

								PersonalDto personalDto = getPersonalFac()
										.personalFindByPrimaryKey(tel.getPersonal_i_id(), theClientDto);

								if (personalDto.getKostenstelleDto_Stamm() != null) {
									kostenstellen.put(tel.getPersonal_i_id(),
											personalDto.getKostenstelleDto_Stamm().getCNr());
								} else {
									kostenstellen.put(tel.getPersonal_i_id(), "");
								}

							}

							arbeitszeitstatistikDto
									.setSKostenstelle((String) kostenstellen.get(tel.getPersonal_i_id()));

							if (artikelDto_DefaultAZ.getArtgruIId() != null) {
								arbeitszeitstatistikDto.setSArtikelgruppe(getArtikelFac()
										.artgruFindByPrimaryKey(artikelDto_DefaultAZ.getArtgruIId(), theClientDto)
										.getBezeichnung());
							}
							if (artikelDto_DefaultAZ.getArtklaIId() != null) {
								arbeitszeitstatistikDto.setSArtikelklasse(getArtikelFac()
										.artklaFindByPrimaryKey(artikelDto_DefaultAZ.getArtklaIId(), theClientDto)
										.getBezeichnung());
							}

							if (krit.sortierung == ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_ARTIKELGRUPPE) {
								arbeitszeitstatistikDto.setSGruppierung(arbeitszeitstatistikDto.getSArtikelgruppe());
								alleDaten.put(
										arbeitszeitstatistikDto.getSArtikelgruppe()
												+ new Timestamp(tel.getT_von().getTime()) + sort,
										arbeitszeitstatistikDto);
							} else if (krit.sortierung == ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_ARTIKELKLASSE) {
								arbeitszeitstatistikDto.setSGruppierung(arbeitszeitstatistikDto.getSArtikelklasse());
								alleDaten.put(
										arbeitszeitstatistikDto.getSArtikelklasse()
												+ new Timestamp(tel.getT_von().getTime()) + sort,
										arbeitszeitstatistikDto);
							} else if (krit.sortierung == ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_ADRESSE) {
								arbeitszeitstatistikDto.setSGruppierung(arbeitszeitstatistikDto.getSKunde() + "");
								alleDaten.put(
										Helper.fitString2Length(arbeitszeitstatistikDto.getSKunde(), 80, ' ')
												+ Helper.fitString2Length(arbeitszeitstatistikDto.getAuftragzeitenDto()
														.getSPersonalMaschinenname(), 80, ' ')
												+ new Timestamp(tel.getT_von().getTime()) + sort,
										arbeitszeitstatistikDto);
							} else if (krit.sortierung == ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_ARTIKEL) {
								arbeitszeitstatistikDto.setSGruppierung(
										arbeitszeitstatistikDto.getAuftragzeitenDto().getSArtikelcnr());
								alleDaten.put(
										arbeitszeitstatistikDto.getAuftragzeitenDto().getSArtikelcnr()
												+ new Timestamp(tel.getT_von().getTime()) + sort,
										arbeitszeitstatistikDto);
							} else if (krit.sortierung == ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_KOSTENSTELLE) {
								arbeitszeitstatistikDto.setSGruppierung(arbeitszeitstatistikDto.getSKostenstelle());
								alleDaten.put(
										arbeitszeitstatistikDto.getSKostenstelle()
												+ arbeitszeitstatistikDto.getAuftragzeitenDto()
														.getSPersonalMaschinenname()
												+ new Timestamp(tel.getT_von().getTime()) + sort,
										arbeitszeitstatistikDto);
							} else if (krit.sortierung == ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_PERSONAL) {
								arbeitszeitstatistikDto.setSGruppierung(
										arbeitszeitstatistikDto.getAuftragzeitenDto().getSPersonalnummer());
								alleDaten.put(
										arbeitszeitstatistikDto.getAuftragzeitenDto().getSPersonalMaschinenname()
												+ new Timestamp(tel.getT_von().getTime()) + sort,
										arbeitszeitstatistikDto);
							} else if (krit.sortierung == ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_AUFTRAG) {

								String sBeleg = arbeitszeitstatistikDto.getSbeleg();

								if (sBeleg.endsWith("TZ")) {
									sBeleg = sBeleg.substring(0, sBeleg.length() - 2);
								}
								arbeitszeitstatistikDto.setSGruppierung(Helper.fitString2Length(sBeleg, 15, ' ')
										+ arbeitszeitstatistikDto.getAuftragzeitenDto().getSPersonalMaschinenname());
								alleDaten.put(sBeleg
										+ arbeitszeitstatistikDto.getAuftragzeitenDto().getSPersonalMaschinenname()
										+ new Timestamp(tel.getT_von().getTime()) + sort, arbeitszeitstatistikDto);
							} else if (krit.sortierung == ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_KUNDE_BELEG_PERSONAL) {

								String sBeleg = arbeitszeitstatistikDto.getSbeleg();

								if (sBeleg.endsWith("TZ")) {
									sBeleg = sBeleg.substring(0, sBeleg.length() - 2);
								}

								arbeitszeitstatistikDto.setSGruppierung(
										Helper.fitString2Length(arbeitszeitstatistikDto.getSKunde(), 60, ' ')
												+ Helper.fitString2Length(sBeleg, 15, ' ') + arbeitszeitstatistikDto
														.getAuftragzeitenDto().getSPersonalMaschinenname());
								alleDaten.put(
										Helper.fitString2Length(arbeitszeitstatistikDto.getSKunde(), 80, ' ')
												+ arbeitszeitstatistikDto.getSbeleg()
												+ Helper.fitString2Length(arbeitszeitstatistikDto.getAuftragzeitenDto()
														.getSPersonalMaschinenname(), 80, ' ')
												+ new Timestamp(tel.getT_von().getTime()) + sort,
										arbeitszeitstatistikDto);
							} else if (krit.sortierung == ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_KUNDE_DATUM_PERSONAL_ARTIKEL) {

								arbeitszeitstatistikDto.setSGruppierung(
										Helper.fitString2Length(arbeitszeitstatistikDto.getSKunde(), 60, ' ')
												+ Helper.cutTimestamp(
														arbeitszeitstatistikDto.getAuftragzeitenDto().getTsBeginn())
														.getTime()
												+ arbeitszeitstatistikDto.getAuftragzeitenDto()
														.getSPersonalMaschinenname()
												+ Helper.fitString2Length(
														arbeitszeitstatistikDto.getAuftragzeitenDto().getSArtikelcnr(),
														50, ' '));
								alleDaten.put(
										Helper.fitString2Length(arbeitszeitstatistikDto.getSKunde(), 80, ' ')
												+ Helper.cutTimestamp(
														arbeitszeitstatistikDto.getAuftragzeitenDto().getTsBeginn())
														.getTime()
												+ Helper.fitString2Length(arbeitszeitstatistikDto.getAuftragzeitenDto()
														.getSPersonalMaschinenname(), 80, ' ')
												+ Helper.fitString2Length(
														arbeitszeitstatistikDto.getAuftragzeitenDto().getSArtikelcnr(),
														50, ' ')
												+ new Timestamp(tel.getT_von().getTime()) + sort,
										arbeitszeitstatistikDto);
							}

						}

						sessionTel.close();
					}
				}
			}

			// SP5059

			if (krit.mitErledigtenProjekten) {
				Session sessionZusPJ = FLRSessionFactory.getFactory().openSession();
				// Hiberante Criteria fuer alle Tabellen ausgehend von meiner
				// Haupttabelle anlegen,
				// nach denen ich filtern und sortieren kann
				Criteria crit = sessionZusPJ.createCriteria(FLRProjekt.class);
				// Einschraenkung auf den aktuellen Mandanten
				crit.add(Restrictions.eq(ProjektFac.FLR_PROJEKT_MANDANT_C_NR, theClientDto.getMandant()));
				crit.add(Restrictions.not(Restrictions.eq(ProjektFac.FLR_PROJEKT_STATUS_C_NR,
						ProjektServiceFac.PROJEKT_STATUS_STORNIERT)));

				// Einschraenkung nach Status Offen, Erledigt

				if (krit.projektInterneErledigungBeruecksichtigen == false) {
					// PJ18471
					crit.createAlias("flrprojektstatus", "s");
					crit.add(Restrictions.eq("s.b_erledigt", Helper.boolean2Short(true)));
				}

				if (krit.projektInterneErledigungBeruecksichtigen == true) {
					crit.add(Restrictions.or(
							Restrictions.and(Restrictions.isNotNull(ProjektFac.FLR_PROJEKT_T_INTERNERLEDIGT),
									Restrictions.ge(ProjektFac.FLR_PROJEKT_T_INTERNERLEDIGT, tVon)),
							Restrictions.ge(ProjektFac.FLR_PROJEKT_T_ERLEDIGUNGSDATUM, tVon)));

				} else {
					crit.add(Restrictions.ge(ProjektFac.FLR_PROJEKT_T_ERLEDIGUNGSDATUM, tVon));
				}

				if (krit.projektInterneErledigungBeruecksichtigen == true) {
					crit.add(Restrictions.or(
							Restrictions.and(Restrictions.isNotNull(ProjektFac.FLR_PROJEKT_T_INTERNERLEDIGT),
									Restrictions.lt(ProjektFac.FLR_PROJEKT_T_INTERNERLEDIGT, tBis)),
							Restrictions.lt(ProjektFac.FLR_PROJEKT_T_ERLEDIGUNGSDATUM, tBis)));
				} else {
					crit.add(Restrictions.lt(ProjektFac.FLR_PROJEKT_T_ERLEDIGUNGSDATUM, tBis));
				}

				// Einschraenkung nach einer bestimmten Perosn
				if (krit.personalIId != null) {
					crit.add(Restrictions.eq(ProjektFac.FLR_PROJEKT_PERSONAL_I_ID_ERLEDIGER, krit.personalIId));
				}
				if (krit.partnerIId != null) {
					crit.add(Restrictions.eq("partner_i_id", krit.partnerIId));
				}

				crit.addOrder(Order.asc(ProjektFac.FLR_PROJEKT_C_NR));

				crit.addOrder(Order.asc(ProjektFac.FLR_PROJEKT_KATEGORIE_C_NR));
				List<?> list = crit.list();
				ArrayList<Object[]> alDaten = new ArrayList<Object[]>();
				Iterator<?> it = list.iterator();

				while (it.hasNext()) {
					FLRProjekt projekt = (FLRProjekt) it.next();
					Object[] oZeile = new Object[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_ANZAHL_SPALTEN];

					if (!hsBereitsBeruecksichtigteProjekte.isEmpty()) {

						hsBereitsBeruecksichtigteProjekte.contains(projekt.getI_id());
						continue;
					}

					if (krit.projektInterneErledigungBeruecksichtigen == true && projekt.getT_internerledigt() != null
							&& projekt.getT_erledigungsdatum() != null) {

						// Wenn intern-Erledigt und normal erledigt, dann gilt
						// jenes
						// Datum, das frueher war

						if (projekt.getT_internerledigt().getTime() <= projekt.getT_erledigungsdatum().getTime()) {
							if (tVon != null && projekt.getT_internerledigt().getTime() < tVon.getTime()) {
								continue;
							}

							if (tBis != null && projekt.getT_internerledigt().getTime() > tBis.getTime()) {
								continue;
							}

						}
					}
					// Zeile hinzufuegen
					ArbeitszeitstatistikDto arbeitszeitstatistikDto = new ArbeitszeitstatistikDto();

					AuftragzeitenDto auftragzeitenDto = new AuftragzeitenDto();

					auftragzeitenDto.setTsBeginn(new Timestamp(projekt.getT_anlegen().getTime()));

					if (projekt.getT_erledigungsdatum() != null) {
						auftragzeitenDto.setTsEnde(new Timestamp(projekt.getT_erledigungsdatum().getTime()));
					}

					auftragzeitenDto.setSPersonalKurzzeichen(projekt.getFlrpersonalZugewiesener().getC_kurzzeichen());

					String sName = projekt.getFlrpersonalZugewiesener().getFlrpartner().getC_name1nachnamefirmazeile1();
					if (projekt.getFlrpersonalZugewiesener().getFlrpartner().getC_name2vornamefirmazeile2() != null) {
						sName = projekt.getFlrpersonalZugewiesener().getFlrpartner().getC_name2vornamefirmazeile2()
								+ " " + sName;
					}

					auftragzeitenDto.setSPersonalnummer(projekt.getFlrpersonalZugewiesener().getC_personalnummer());

					auftragzeitenDto.setSPersonalMaschinenname(sName);
					auftragzeitenDto.setSArtikelcnr(artikelDto_DefaultAZ.getCNr());

					if (artikelDto_DefaultAZ.getArtikelsprDto() != null) {
						auftragzeitenDto.setSArtikelbezeichnung(artikelDto_DefaultAZ.getArtikelsprDto().getCBez());
					}

					auftragzeitenDto.setSZeitbuchungtext(projekt.getX_freetext());

					arbeitszeitstatistikDto.setAuftragzeitenDto(auftragzeitenDto);
					arbeitszeitstatistikDto.setPartnerIId(projekt.getPartner_i_id());
					arbeitszeitstatistikDto.setsFertigungsgruppe("");
					arbeitszeitstatistikDto.setSVertreter("");

					arbeitszeitstatistikDto.setSbeleg(hmBelegarten.get(LocaleFac.BELEGART_PROJEKT) + projekt.getC_nr());

					arbeitszeitstatistikDto.setSBelegbezeichnung(projekt.getC_titel());
					arbeitszeitstatistikDto.setSProjektStatus(projekt.getStatus_c_nr());
					arbeitszeitstatistikDto.setSProjektKategorie(projekt.getKategorie_c_nr());

					arbeitszeitstatistikDto.setsProjektVerrechenbar(projekt.getI_verrechenbar());
					if (projekt.getT_internerledigt() != null) {
						arbeitszeitstatistikDto
								.setTProjektInternErledigt(new Timestamp(projekt.getT_internerledigt().getTime()));
					}
					arbeitszeitstatistikDto.setSProjektTyp(projekt.getTyp_c_nr());

					arbeitszeitstatistikDto.setBdProjektGesamtdauer(
							new BigDecimal(getZeiterfassungFac().getSummeZeitenEinesBeleges(LocaleFac.BELEGART_PROJEKT,
									projekt.getI_id(), null, null, null, null, theClientDto)));

					// SP6065
					arbeitszeitstatistikDto.setIProjektIId(projekt.getI_id());
					arbeitszeitstatistikDto.setSProjektBereich(projekt.getFlrbereich().getC_bez());

					PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(projekt.getPartner_i_id(),
							theClientDto);

					arbeitszeitstatistikDto.setSKunde(partnerDto.formatFixName1Name2());

					if (!kostenstellen.containsKey(projekt.getFlrpersonalZugewiesener().getI_id())) {

						PersonalDto personalDto = getPersonalFac()
								.personalFindByPrimaryKey(projekt.getFlrpersonalZugewiesener().getI_id(), theClientDto);

						if (personalDto.getKostenstelleDto_Stamm() != null) {
							kostenstellen.put(projekt.getFlrpersonalZugewiesener().getI_id(),
									personalDto.getKostenstelleDto_Stamm().getCNr());
						} else {
							kostenstellen.put(projekt.getFlrpersonalZugewiesener().getI_id(), "");
						}

					}

					arbeitszeitstatistikDto.setSKostenstelle(
							(String) kostenstellen.get(projekt.getFlrpersonalZugewiesener().getI_id()));

					if (artikelDto_DefaultAZ.getArtgruIId() != null) {
						arbeitszeitstatistikDto.setSArtikelgruppe(getArtikelFac()
								.artgruFindByPrimaryKey(artikelDto_DefaultAZ.getArtgruIId(), theClientDto)
								.getBezeichnung());
					}
					if (artikelDto_DefaultAZ.getArtklaIId() != null) {
						arbeitszeitstatistikDto.setSArtikelklasse(getArtikelFac()
								.artklaFindByPrimaryKey(artikelDto_DefaultAZ.getArtklaIId(), theClientDto)
								.getBezeichnung());
					}

					if (krit.sortierung == ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_ARTIKELGRUPPE) {
						arbeitszeitstatistikDto.setSGruppierung(arbeitszeitstatistikDto.getSArtikelgruppe());
						alleDaten.put(
								arbeitszeitstatistikDto.getSArtikelgruppe()
										+ new Timestamp(projekt.getT_anlegen().getTime()) + sort,
								arbeitszeitstatistikDto);
					} else if (krit.sortierung == ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_ARTIKELKLASSE) {
						arbeitszeitstatistikDto.setSGruppierung(arbeitszeitstatistikDto.getSArtikelklasse());
						alleDaten.put(
								arbeitszeitstatistikDto.getSArtikelklasse()
										+ new Timestamp(projekt.getT_anlegen().getTime()) + sort,
								arbeitszeitstatistikDto);
					} else if (krit.sortierung == ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_ADRESSE) {
						arbeitszeitstatistikDto.setSGruppierung(arbeitszeitstatistikDto.getSKunde() + "");
						alleDaten.put(
								Helper.fitString2Length(arbeitszeitstatistikDto.getSKunde(), 80, ' ')
										+ Helper.fitString2Length(arbeitszeitstatistikDto.getAuftragzeitenDto()
												.getSPersonalMaschinenname(), 80, ' ')
										+ new Timestamp(projekt.getT_anlegen().getTime()) + sort,
								arbeitszeitstatistikDto);
					} else if (krit.sortierung == ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_ARTIKEL) {
						arbeitszeitstatistikDto
								.setSGruppierung(arbeitszeitstatistikDto.getAuftragzeitenDto().getSArtikelcnr());
						alleDaten.put(
								arbeitszeitstatistikDto.getAuftragzeitenDto().getSArtikelcnr()
										+ new Timestamp(projekt.getT_anlegen().getTime()) + sort,
								arbeitszeitstatistikDto);
					} else if (krit.sortierung == ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_KOSTENSTELLE) {
						arbeitszeitstatistikDto.setSGruppierung(arbeitszeitstatistikDto.getSKostenstelle());
						alleDaten.put(
								arbeitszeitstatistikDto.getSKostenstelle()
										+ arbeitszeitstatistikDto.getAuftragzeitenDto().getSPersonalMaschinenname()
										+ new Timestamp(projekt.getT_anlegen().getTime()) + sort,
								arbeitszeitstatistikDto);
					} else if (krit.sortierung == ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_PERSONAL) {
						arbeitszeitstatistikDto
								.setSGruppierung(arbeitszeitstatistikDto.getAuftragzeitenDto().getSPersonalnummer());
						alleDaten.put(
								arbeitszeitstatistikDto.getAuftragzeitenDto().getSPersonalMaschinenname()
										+ new Timestamp(projekt.getT_anlegen().getTime()) + sort,
								arbeitszeitstatistikDto);
					} else if (krit.sortierung == ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_AUFTRAG) {

						String sBeleg = arbeitszeitstatistikDto.getSbeleg();

						arbeitszeitstatistikDto.setSGruppierung(Helper.fitString2Length(sBeleg, 15, ' ')
								+ arbeitszeitstatistikDto.getAuftragzeitenDto().getSPersonalMaschinenname());
						alleDaten.put(
								sBeleg + arbeitszeitstatistikDto.getAuftragzeitenDto().getSPersonalMaschinenname()
										+ new Timestamp(projekt.getT_anlegen().getTime()) + sort,
								arbeitszeitstatistikDto);
					} else if (krit.sortierung == ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_KUNDE_BELEG_PERSONAL) {

						String sBeleg = arbeitszeitstatistikDto.getSbeleg();

						arbeitszeitstatistikDto
								.setSGruppierung(Helper.fitString2Length(arbeitszeitstatistikDto.getSKunde(), 60, ' ')
										+ Helper.fitString2Length(sBeleg, 15, ' ')
										+ arbeitszeitstatistikDto.getAuftragzeitenDto().getSPersonalMaschinenname());
						alleDaten.put(
								Helper.fitString2Length(arbeitszeitstatistikDto.getSKunde(), 80, ' ')
										+ arbeitszeitstatistikDto.getSbeleg()
										+ Helper.fitString2Length(arbeitszeitstatistikDto.getAuftragzeitenDto()
												.getSPersonalMaschinenname(), 80, ' ')
										+ new Timestamp(projekt.getT_anlegen().getTime()) + sort,
								arbeitszeitstatistikDto);
					} else if (krit.sortierung == ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_KUNDE_DATUM_PERSONAL_ARTIKEL) {

						arbeitszeitstatistikDto
								.setSGruppierung(Helper.fitString2Length(arbeitszeitstatistikDto.getSKunde(), 60, ' ')
										+ Helper.cutTimestamp(
												arbeitszeitstatistikDto.getAuftragzeitenDto().getTsBeginn()).getTime()
										+ arbeitszeitstatistikDto.getAuftragzeitenDto().getSPersonalMaschinenname()
										+ Helper.fitString2Length(
												arbeitszeitstatistikDto.getAuftragzeitenDto().getSArtikelcnr(), 50,
												' '));
						alleDaten.put(
								Helper.fitString2Length(arbeitszeitstatistikDto.getSKunde(), 80, ' ')
										+ Helper.cutTimestamp(
												arbeitszeitstatistikDto.getAuftragzeitenDto().getTsBeginn()).getTime()
										+ Helper.fitString2Length(arbeitszeitstatistikDto.getAuftragzeitenDto()
												.getSPersonalMaschinenname(), 80, ' ')
										+ Helper.fitString2Length(
												arbeitszeitstatistikDto.getAuftragzeitenDto().getSArtikelcnr(), 50, ' ')
										+ new Timestamp(projekt.getT_anlegen().getTime()) + sort,
								arbeitszeitstatistikDto);
					}

				}

				sessionZusPJ.close();
			}

		}

		// data = new
		// Object[alleDaten.size()][REPORT_ARBEITSZEITSTATISTIK_ANZAHL_SPALTEN];
		setData(new Object[alleDaten.size()][REPORT_ARBEITSZEITSTATISTIK_ANZAHL_SPALTEN]);

		List<ArbeitszeitstatistikDto> tempAlleDaten = new ArrayList<ArbeitszeitstatistikDto>(alleDaten.size());
		Iterator<?> itTemp = alleDaten.keySet().iterator();
		while (itTemp.hasNext()) {
			Object k = itTemp.next();
			tempAlleDaten.add(alleDaten.get(k));
		}

		Collections.sort(tempAlleDaten, getSortGruppierungBeginnzeitComparator());
		Iterator<?> it = tempAlleDaten.iterator();
		int iRow = 0;
		while (it.hasNext()) {
			Object k = it.next();
			// ArbeitszeitstatistikDto dto = (ArbeitszeitstatistikDto) alleDaten
			// .get(k);
			ArbeitszeitstatistikDto dto = (ArbeitszeitstatistikDto) k;
			data[iRow][REPORT_ARBEITSZEITSTATISTIK_ARTIKELNUMMER] = dto.getAuftragzeitenDto().getSArtikelcnr();
			data[iRow][REPORT_ARBEITSZEITSTATISTIK_BEZEICHNUNG] = dto.getAuftragzeitenDto().getSArtikelbezeichnung();
			if (dto.getAuftragzeitenDto().getDdDauer() != null) {
				data[iRow][REPORT_ARBEITSZEITSTATISTIK_DAUER] = new BigDecimal(dto.getAuftragzeitenDto().getDdDauer());
			}

			data[iRow][REPORT_ARBEITSZEITSTATISTIK_PROJEKT_GESAMTDAUER] = dto.getBdProjektGesamtdauer();
			data[iRow][REPORT_ARBEITSZEITSTATISTIK_PROJEKT_STATUS] = dto.getSProjektStatus();
			data[iRow][REPORT_ARBEITSZEITSTATISTIK_PROJEKT_I_ID] = dto.getIProjektIId();
			data[iRow][REPORT_ARBEITSZEITSTATISTIK_PROJEKT_BEREICH] = dto.getSProjektBereich();
			data[iRow][REPORT_ARBEITSZEITSTATISTIK_PROJEKT_KATEGORIE] = dto.getSProjektKategorie();
			data[iRow][REPORT_ARBEITSZEITSTATISTIK_PROJEKT_TYP] = dto.getSProjektTyp();
			data[iRow][REPORT_ARBEITSZEITSTATISTIK_PROJEKT_INTERN_ERLEDIGT] = dto.getTProjektInternErledigt();
			data[iRow][REPORT_ARBEITSZEITSTATISTIK_PROJEKT_VERRECHENBAR] = getProjektServiceFac()
					.getTextVerrechenbar(dto.getSProjektVerrechenbar(), theClientDto);

			// PJ20739
			data[iRow][REPORT_ARBEITSZEITSTATISTIK_VERRECHENBAR] = dto.getAuftragzeitenDto()
					.getFVerrechenbarInProzent();
			data[iRow][REPORT_ARBEITSZEITSTATISTIK_PERSON_ERLEDIGT] = dto.getAuftragzeitenDto()
					.getSPersonalKurzzeichenErledigt();
			data[iRow][REPORT_ARBEITSZEITSTATISTIK_ZEITPUNKT_ERLEDIGT] = dto.getAuftragzeitenDto().getTsErledigt();
			data[iRow][REPORT_ARBEITSZEITSTATISTIK_BEREITS_VERRECHNET] = dto.getBdBereitsverrechnet();

			data[iRow][REPORT_ARBEITSZEITSTATISTIK_VON] = dto.getAuftragzeitenDto().getTsBeginn();
			data[iRow][REPORT_ARBEITSZEITSTATISTIK_BIS] = dto.getAuftragzeitenDto().getTsEnde();
			data[iRow][REPORT_ARBEITSZEITSTATISTIK_KOSTEN] = dto.getAuftragzeitenDto().getBdKosten();

			data[iRow][REPORT_ARBEITSZEITSTATISTIK_BEMERKUNG] = dto.getAuftragzeitenDto().getSZeitbuchungtext();
			data[iRow][REPORT_ARBEITSZEITSTATISTIK_KOMMENTAR] = dto.getAuftragzeitenDto().getSKommentar();
			data[iRow][REPORT_ARBEITSZEITSTATISTIK_KOMMENTAR_INTERN] = dto.getAuftragzeitenDto().getSKommentarIntern();

			data[iRow][REPORT_ARBEITSZEITSTATISTIK_PERSON_KOSTENSTELLE] = dto.getSKostenstelle();
			data[iRow][REPORT_ARBEITSZEITSTATISTIK_FERTIGUNGSGRUPPE] = dto.getsFertigungsgruppe();

			if (dto.getAuftragzeitenDto().getSPersonalMaschinenname().startsWith("P:")) {
				data[iRow][REPORT_ARBEITSZEITSTATISTIK_PERSON] = dto.getAuftragzeitenDto().getSPersonalMaschinenname()
						.substring(2);

				data[iRow][REPORT_ARBEITSZEITSTATISTIK_PERSONALNUMMER] = dto.getAuftragzeitenDto().getSPersonalnummer();

				data[iRow][REPORT_ARBEITSZEITSTATISTIK_KURZZEICHEN] = dto.getAuftragzeitenDto()
						.getSPersonalKurzzeichen();

			} else {
				data[iRow][REPORT_ARBEITSZEITSTATISTIK_PERSON] = dto.getAuftragzeitenDto().getSPersonalMaschinenname();
				data[iRow][REPORT_ARBEITSZEITSTATISTIK_PERSONALNUMMER] = dto.getAuftragzeitenDto().getSPersonalnummer();

				data[iRow][REPORT_ARBEITSZEITSTATISTIK_KURZZEICHEN] = dto.getAuftragzeitenDto()
						.getSPersonalKurzzeichen();
			}

			if (data[iRow][REPORT_ARBEITSZEITSTATISTIK_KURZZEICHEN] == null) {
				data[iRow][REPORT_ARBEITSZEITSTATISTIK_KURZZEICHEN] = dto.getSPersonalKurzzeichen();
			}

			data[iRow][REPORT_ARBEITSZEITSTATISTIK_BELEG] = dto.getSbeleg();
			data[iRow][REPORT_ARBEITSZEITSTATISTIK_VERTRETER] = dto.getSVertreter();
			if (dto.getSBelegbezeichnung() != null)
				data[iRow][REPORT_ARBEITSZEITSTATISTIK_PROJEKT] = dto.getSBelegbezeichnung();

			data[iRow][REPORT_ARBEITSZEITSTATISTIK_KUNDENBESTELLNUMMER] = dto.getSKundenbestellnummer();

			data[iRow][REPORT_ARBEITSZEITSTATISTIK_KUNDE] = dto.getSKunde();
			data[iRow][REPORT_ARBEITSZEITSTATISTIK_ARTIKELGRUPPE] = dto.getSArtikelgruppe();
			data[iRow][REPORT_ARBEITSZEITSTATISTIK_ARTIKELKLASSE] = dto.getSArtikelklasse();
			data[iRow][REPORT_ARBEITSZEITSTATISTIK_GRUPPIERUNG] = dto.getSGruppierung();

			if (dto.getSGruppierung() == null) {
				int u = 0;
			}

			iRow++;

		}

		parameter.put(P_KPI_VARIABLEN, krit.getKpiReportStorage());
		initJRDS(parameter, ZeiterfassungReportFac.REPORT_MODUL, ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();
	}
	/*
	 * @TransactionAttribute(TransactionAttributeType.NEVER) public JasperPrintLP
	 * printArbeitszeitstatistik(java.sql.Timestamp tVon, java.sql.Timestamp tBis,
	 * int iOptionSortierung, String belegartCNr, Integer belegartIId, String
	 * belegnummerFuerReport, Integer personalIId, Integer artikelIId, Integer
	 * partnerIId, Integer artikelgruppeIId, Integer artikelklasseIId, boolean
	 * bVerdichtet, boolean mitErledigtenProjektenImZeitraum, boolean
	 * projektInterneErledigungBeruecksichtigen, TheClientDto theClientDto) throws
	 * EJBExceptionLP, RemoteException {
	 * 
	 * sAktuellerReport = ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK;
	 * 
	 * if (tVon == null || tBis == null) { throw new
	 * EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new
	 * Exception("tVon == null || tBis == null")); } tVon =
	 * Helper.cutTimestamp(tVon); tBis = Helper.cutTimestamp(tBis);
	 * 
	 * HashMap<String, Object> parameter = new HashMap<String, Object>();
	 * parameter.put("P_VON", tVon);
	 * 
	 * parameter.put("P_BELEGART", belegartCNr); parameter.put("P_BELEGNUMMER",
	 * belegnummerFuerReport);
	 * 
	 * parameter.put("P_MIT_ERLEDIGTEN_PROJEKTEN",
	 * mitErledigtenProjektenImZeitraum);
	 * parameter.put("P_INTERN_ERLEDIGT_BERUECKSICHTIGEN",
	 * projektInterneErledigungBeruecksichtigen);
	 * 
	 * Calendar c = Calendar.getInstance(); c.setTimeInMillis(tBis.getTime());
	 * c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
	 * 
	 * parameter.put("P_BIS", new java.sql.Timestamp(c.getTimeInMillis()));
	 * 
	 * Session session = FLRSessionFactory.getFactory().openSession(); String
	 * queryString =
	 * "SELECT distinct z.c_belegartnr, z.i_belegartid FROM FLRZeitdaten as z" +
	 * " WHERE z.t_zeit>='" + Helper.formatDateWithSlashes(new
	 * java.sql.Date(tVon.getTime())) + "'" + " AND z.t_zeit<'" +
	 * Helper.formatDateWithSlashes(new java.sql.Date(tBis.getTime())) + "'" +
	 * " AND z.flrpersonal.mandant_c_nr='" + theClientDto.getMandant() + "'";
	 * 
	 * // PJ17944 if (belegartCNr != null &&
	 * belegartCNr.equals(LocaleFac.BELEGART_AUFTRAG)) { if (belegartIId != null) {
	 * 
	 * LosDto[] losDtos = getFertigungFac().losFindByAuftragIId(belegartIId);
	 * 
	 * if (losDtos.length > 0) {
	 * 
	 * String in = "("; for (int i = 0; i < losDtos.length; i++) { in +=
	 * losDtos[i].getIId();
	 * 
	 * if (i != losDtos.length - 1) { in += ","; } } in += ")";
	 * 
	 * queryString += " AND ( (z.c_belegartnr='" + belegartCNr +
	 * "' AND z.i_belegartid=" + belegartIId + ") OR (z.c_belegartnr='" +
	 * LocaleFac.BELEGART_LOS + "' AND z.i_belegartid IN " + in + "))";
	 * 
	 * } else { queryString += " AND z.c_belegartnr='" + belegartCNr + "'";
	 * queryString += " AND z.i_belegartid=" + belegartIId; }
	 * 
	 * } else { queryString += " AND z.c_belegartnr='" + belegartCNr + "'"; }
	 * 
	 * } else { if (belegartCNr != null) { queryString += " AND z.c_belegartnr='" +
	 * belegartCNr + "'"; } if (belegartIId != null) { queryString +=
	 * " AND z.i_belegartid=" + belegartIId; } }
	 * 
	 * if (personalIId != null) { queryString += " AND z.personal_i_id=" +
	 * personalIId; parameter.put("P_PERSON",
	 * getPersonalFac().personalFindByPrimaryKey(personalIId,
	 * theClientDto).formatFixName1Name2());
	 * 
	 * } if (artikelIId != null) { queryString += " AND z.artikel_i_id=" +
	 * artikelIId; parameter.put("P_TAETIGKEIT",
	 * getArtikelFac().artikelFindByPrimaryKeySmall(artikelIId,
	 * theClientDto).formatArtikelbezeichnung()); } if (artikelgruppeIId != null) {
	 * queryString += " AND z.flrartikel.flrartikelgruppe.i_id=" + artikelgruppeIId;
	 * parameter.put("P_ARTIKELGRUPPE",
	 * getArtikelFac().artgruFindByPrimaryKey(artikelgruppeIId,
	 * theClientDto).getBezeichnung()); } if (artikelklasseIId != null) {
	 * queryString += " AND z.flrartikel.flrartikelklasse.i_id=" + artikelklasseIId;
	 * parameter.put("P_ARTIKELKLASSE",
	 * getArtikelFac().artklaFindByPrimaryKey(artikelklasseIId,
	 * theClientDto).getBezeichnung()); }
	 * 
	 * // Sortieren if (iOptionSortierung == ZeiterfassungReportFac.
	 * REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_ARTIKELGRUPPE) {
	 * parameter.put("P_SORTIERUNG", getTextRespectUISpr("lp.artikelgruppe",
	 * theClientDto.getMandant(), theClientDto.getLocUi())); } else if
	 * (iOptionSortierung == ZeiterfassungReportFac.
	 * REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_ARTIKELKLASSE) {
	 * parameter.put("P_SORTIERUNG", getTextRespectUISpr("lp.artikelklasse",
	 * theClientDto.getMandant(), theClientDto.getLocUi())); } else if
	 * (iOptionSortierung ==
	 * ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_AUFTRAG)
	 * { parameter.put("P_SORTIERUNG",
	 * getTextRespectUISpr("pers.arbeitszeitstatistik.sortierung.belegpersonal",
	 * theClientDto.getMandant(), theClientDto.getLocUi())); } else if
	 * (iOptionSortierung == ZeiterfassungReportFac.
	 * REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_KUNDE_BELEG_PERSONAL) {
	 * parameter.put("P_SORTIERUNG", getTextRespectUISpr(
	 * "pers.arbeitszeitstatistik.sortierung.kundebelegpersonal",
	 * theClientDto.getMandant(), theClientDto.getLocUi())); } else if
	 * (iOptionSortierung == ZeiterfassungReportFac.
	 * REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_PERSONAL) {
	 * parameter.put("P_SORTIERUNG", getTextRespectUISpr("lp.personal",
	 * theClientDto.getMandant(), theClientDto.getLocUi())); } else if
	 * (iOptionSortierung == ZeiterfassungReportFac.
	 * REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_KOSTENSTELLE) {
	 * parameter.put("P_SORTIERUNG", getTextRespectUISpr("lp.kostenstelle",
	 * theClientDto.getMandant(), theClientDto.getLocUi())); } else if
	 * (iOptionSortierung ==
	 * ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_ADRESSE)
	 * { parameter.put("P_SORTIERUNG",
	 * getTextRespectUISpr("pers.arbeitszeitstatistik.sortierung.kundepersonal",
	 * theClientDto.getMandant(), theClientDto.getLocUi())); } else if
	 * (iOptionSortierung ==
	 * ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_ARTIKEL)
	 * { parameter.put("P_SORTIERUNG", getTextRespectUISpr("lp.artikel",
	 * theClientDto.getMandant(), theClientDto.getLocUi())); } else if
	 * (iOptionSortierung == ZeiterfassungReportFac.
	 * REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_KUNDE_DATUM_PERSONAL_ARTIKEL) {
	 * parameter.put("P_SORTIERUNG", getTextRespectUISpr(
	 * "pers.arbeitszeitstatistik.sortierung.kundedatumpersonalartikel",
	 * theClientDto.getMandant(), theClientDto.getLocUi())); }
	 * 
	 * javax.persistence.Query queryBelegarten =
	 * em.createNamedQuery("BelegartfindAll");
	 * 
	 * Collection belegarten = queryBelegarten.getResultList();
	 * 
	 * HashMap hmBelegarten = new HashMap();
	 * 
	 * Iterator itBel = belegarten.iterator(); while (itBel.hasNext()) { Belegart b
	 * = (Belegart) itBel.next(); hmBelegarten.put(b.getCNr(), b.getCKbez()); }
	 * 
	 * TreeMap<Object, ArbeitszeitstatistikDto> alleDaten = new TreeMap<Object,
	 * ArbeitszeitstatistikDto>(); int sort = 0;
	 * 
	 * HashMap kostenstellen = new HashMap();
	 * 
	 * HashSet<Integer> hsBereitsBeruecksichtigteProjekte = new HashSet();
	 * 
	 * Query query = session.createQuery(queryString); List<?> resultList =
	 * query.list(); Iterator<?> resultListIterator = resultList.iterator(); while
	 * (resultListIterator.hasNext()) { Object[] o = (Object[])
	 * resultListIterator.next(); if (o[0] != null && o[1] != null) { try {
	 * AuftragzeitenDto[] dtos =
	 * getZeiterfassungFac().getAllZeitenEinesBeleges((String) o[0], (Integer) o[1],
	 * null, personalIId, tVon, tBis, ZeiterfassungFac.SORTIERUNG_ZEITDATEN_ARTIKEL,
	 * theClientDto);
	 * 
	 * String fertigungsgruppe = ""; String vertreter = ""; String
	 * kundenbestellnummer = null;
	 * 
	 * String projektStatus = null; String projektKategorie = null;
	 * 
	 * Integer projektIId = null; String projektBereich = null;
	 * 
	 * Short sProjektVerrechenbar = null; Timestamp tProjektInternErledigt = null;
	 * String sProjektTyp = null;
	 * 
	 * BigDecimal bdProjektGesamtdauer = null;
	 * 
	 * if (((String) o[0]).equals(LocaleFac.BELEGART_LOS)) { LosDto losDto =
	 * getFertigungFac().losFindByPrimaryKey((Integer) o[1]); if
	 * (losDto.getFertigungsgruppeIId() != null) { fertigungsgruppe =
	 * getStuecklisteFac()
	 * .fertigungsgruppeFindByPrimaryKey(losDto.getFertigungsgruppeIId()).getCBez();
	 * } } else if (((String) o[0]).equals(LocaleFac.BELEGART_AUFTRAG)) { AuftragDto
	 * auftragDto = getAuftragFac().auftragFindByPrimaryKey((Integer) o[1]);
	 * 
	 * if (auftragDto.getPersonalIIdVertreter() != null) { vertreter =
	 * getPersonalFac()
	 * .personalFindByPrimaryKey(auftragDto.getPersonalIIdVertreter(), theClientDto)
	 * .formatAnrede(); }
	 * 
	 * kundenbestellnummer = auftragDto.getCBestellnummer();
	 * 
	 * } else if (((String) o[0]).equals(LocaleFac.BELEGART_PROJEKT)) { ProjektDto
	 * projektDto = getProjektFac().projektFindByPrimaryKey((Integer) o[1]);
	 * 
	 * projektStatus = projektDto.getStatusCNr(); projektKategorie =
	 * projektDto.getKategorieCNr();
	 * 
	 * projektIId = projektDto.getIId(); projektBereich =
	 * getProjektServiceFac().bereichFindByPrimaryKey(projektDto.getBereichIId())
	 * .getCBez();
	 * 
	 * sProjektVerrechenbar = projektDto.getBVerrechenbar(); tProjektInternErledigt
	 * = projektDto.getTInternerledigt(); sProjektTyp =
	 * projektDto.getProjekttypCNr();
	 * 
	 * if (mitErledigtenProjektenImZeitraum) { bdProjektGesamtdauer = new
	 * BigDecimal(
	 * getZeiterfassungFac().getSummeZeitenEinesBeleges(LocaleFac.BELEGART_PROJEKT,
	 * projektDto.getIId(), null, null, null, null, theClientDto)); }
	 * 
	 * hsBereitsBeruecksichtigteProjekte.add(projektDto.getIId());
	 * 
	 * }
	 * 
	 * BelegInfos belegInfo = getLagerFac().getBelegInfos((String) o[0], (Integer)
	 * o[1], null, theClientDto);
	 * 
	 * for (int i = 0; i < dtos.length; i++) {
	 * 
	 * ArbeitszeitstatistikDto arbeitszeitstatistikDto = new
	 * ArbeitszeitstatistikDto();
	 * arbeitszeitstatistikDto.setAuftragzeitenDto(dtos[i]);
	 * arbeitszeitstatistikDto.setPartnerIId(belegInfo.getPartnerIId());
	 * arbeitszeitstatistikDto.setsFertigungsgruppe(fertigungsgruppe);
	 * arbeitszeitstatistikDto.setSProjektKategorie(projektKategorie);
	 * arbeitszeitstatistikDto.setSProjektStatus(projektStatus);
	 * arbeitszeitstatistikDto.setSProjektBereich(projektBereich);
	 * arbeitszeitstatistikDto.setIProjektIId(projektIId);
	 * 
	 * // SP5155
	 * arbeitszeitstatistikDto.setsProjektVerrechenbar(sProjektVerrechenbar);
	 * arbeitszeitstatistikDto.setTProjektInternErledigt(tProjektInternErledigt);
	 * arbeitszeitstatistikDto.setSProjektTyp(sProjektTyp);
	 * 
	 * arbeitszeitstatistikDto.setBdProjektGesamtdauer(bdProjektGesamtdauer);
	 * arbeitszeitstatistikDto.setSKundenbestellnummer(kundenbestellnummer);
	 * arbeitszeitstatistikDto.setSVertreter(vertreter);
	 * arbeitszeitstatistikDto.setSbeleg(hmBelegarten.get(o[0]) +
	 * belegInfo.getBelegnummer());
	 * arbeitszeitstatistikDto.setSBelegbezeichnung(belegInfo.getBelegbezeichnung())
	 * ; arbeitszeitstatistikDto.setSKunde(belegInfo.getKundeLieferant());
	 * 
	 * if (!kostenstellen.containsKey(dtos[i].getIPersonalMaschinenId())) {
	 * 
	 * PersonalDto personalDto = getPersonalFac()
	 * .personalFindByPrimaryKey(dtos[i].getIPersonalMaschinenId(), theClientDto);
	 * 
	 * if (personalDto.getKostenstelleDto_Stamm() != null) {
	 * kostenstellen.put(dtos[i].getIPersonalMaschinenId(),
	 * personalDto.getKostenstelleDto_Stamm().getCNr()); } else {
	 * kostenstellen.put(dtos[i].getIPersonalMaschinenId(), ""); }
	 * 
	 * }
	 * 
	 * arbeitszeitstatistikDto .setSKostenstelle((String)
	 * kostenstellen.get(dtos[i].getIPersonalMaschinenId()));
	 * 
	 * // Wenn artikelIId NOT NULL dann herausfiltern if (artikelIId != null) { if
	 * (!artikelIId.equals(arbeitszeitstatistikDto.getAuftragzeitenDto().
	 * getArtikelIId())) { continue; } } // Wenn partnerIId NOT NULL dann
	 * herausfiltern if (partnerIId != null) { if
	 * (!partnerIId.equals(arbeitszeitstatistikDto.getPartnerIId())) { continue; } }
	 * // Wenn artikelgruppeIId NOT NULL dann herausfiltern if (artikelgruppeIId !=
	 * null) { if (!artikelgruppeIId
	 * .equals(arbeitszeitstatistikDto.getAuftragzeitenDto().getArtikelgruppeIId()))
	 * { continue; } }
	 * 
	 * // Wenn artikelgruppeIId NOT NULL dann herausfiltern if (artikelklasseIId !=
	 * null) { if (!artikelklasseIId
	 * .equals(arbeitszeitstatistikDto.getAuftragzeitenDto().getArtikelklasseIId()))
	 * { continue; } }
	 * 
	 * if (arbeitszeitstatistikDto.getAuftragzeitenDto().getArtikelgruppeIId() !=
	 * null) { arbeitszeitstatistikDto .setSArtikelgruppe( getArtikelFac()
	 * .artgruFindByPrimaryKey(arbeitszeitstatistikDto
	 * .getAuftragzeitenDto().getArtikelgruppeIId(), theClientDto)
	 * .getBezeichnung()); } if
	 * (arbeitszeitstatistikDto.getAuftragzeitenDto().getArtikelklasseIId() != null)
	 * { arbeitszeitstatistikDto .setSArtikelklasse( getArtikelFac()
	 * .artklaFindByPrimaryKey(arbeitszeitstatistikDto
	 * .getAuftragzeitenDto().getArtikelklasseIId(), theClientDto)
	 * .getBezeichnung()); }
	 * 
	 * if (iOptionSortierung == ZeiterfassungReportFac.
	 * REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_ARTIKELGRUPPE) {
	 * arbeitszeitstatistikDto.setSGruppierung(arbeitszeitstatistikDto.
	 * getSArtikelgruppe());
	 * alleDaten.put(arbeitszeitstatistikDto.getSArtikelgruppe() +
	 * dtos[i].getTsBeginn() + sort, arbeitszeitstatistikDto); } else if
	 * (iOptionSortierung == ZeiterfassungReportFac.
	 * REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_ARTIKELKLASSE) {
	 * arbeitszeitstatistikDto.setSGruppierung(arbeitszeitstatistikDto.
	 * getSArtikelklasse());
	 * alleDaten.put(arbeitszeitstatistikDto.getSArtikelklasse() +
	 * dtos[i].getTsBeginn() + sort, arbeitszeitstatistikDto); } else if
	 * (iOptionSortierung ==
	 * ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_ADRESSE)
	 * { arbeitszeitstatistikDto.setSGruppierung(arbeitszeitstatistikDto.getSKunde()
	 * + ""); alleDaten
	 * .put(Helper.fitString2Length(arbeitszeitstatistikDto.getSKunde(), 80, ' ') +
	 * Helper.fitString2Length(arbeitszeitstatistikDto.getAuftragzeitenDto()
	 * .getSPersonalMaschinenname(), 80, ' ') + dtos[i].getTsBeginn() + sort,
	 * arbeitszeitstatistikDto); } else if (iOptionSortierung ==
	 * ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_ARTIKEL)
	 * { arbeitszeitstatistikDto
	 * .setSGruppierung(arbeitszeitstatistikDto.getAuftragzeitenDto().getSArtikelcnr
	 * ());
	 * alleDaten.put(arbeitszeitstatistikDto.getAuftragzeitenDto().getSArtikelcnr()
	 * + dtos[i].getTsBeginn() + sort, arbeitszeitstatistikDto); } else if
	 * (iOptionSortierung == ZeiterfassungReportFac.
	 * REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_KOSTENSTELLE) {
	 * arbeitszeitstatistikDto.setSGruppierung(arbeitszeitstatistikDto.
	 * getSKostenstelle()); alleDaten.put(arbeitszeitstatistikDto.getSKostenstelle()
	 * + arbeitszeitstatistikDto.getAuftragzeitenDto().getSPersonalMaschinenname() +
	 * dtos[i].getTsBeginn() + sort, arbeitszeitstatistikDto); } else if
	 * (iOptionSortierung == ZeiterfassungReportFac.
	 * REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_PERSONAL) {
	 * arbeitszeitstatistikDto.setSGruppierung(
	 * arbeitszeitstatistikDto.getAuftragzeitenDto().getSPersonalnummer());
	 * alleDaten.put(arbeitszeitstatistikDto.getAuftragzeitenDto().
	 * getSPersonalMaschinenname() + dtos[i].getTsBeginn() + sort,
	 * arbeitszeitstatistikDto); } else if (iOptionSortierung ==
	 * ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_AUFTRAG)
	 * { arbeitszeitstatistikDto.setSGruppierung(Helper
	 * .fitString2Length(arbeitszeitstatistikDto.getSbeleg(), 15, ' ') +
	 * arbeitszeitstatistikDto.getAuftragzeitenDto().getSPersonalMaschinenname());
	 * 
	 * alleDaten.put(arbeitszeitstatistikDto.getSbeleg() +
	 * arbeitszeitstatistikDto.getAuftragzeitenDto().getSPersonalMaschinenname() +
	 * dtos[i].getTsBeginn() + sort, arbeitszeitstatistikDto); } else if
	 * (iOptionSortierung == ZeiterfassungReportFac.
	 * REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_KUNDE_BELEG_PERSONAL) {
	 * 
	 * arbeitszeitstatistikDto.setSGruppierung(Helper
	 * .fitString2Length(arbeitszeitstatistikDto.getSKunde(), 60, ' ') +
	 * Helper.fitString2Length(arbeitszeitstatistikDto.getSbeleg(), 15, ' ') +
	 * arbeitszeitstatistikDto.getAuftragzeitenDto().getSPersonalMaschinenname());
	 * 
	 * alleDaten .put(Helper.fitString2Length(arbeitszeitstatistikDto.getSKunde(),
	 * 80, ' ') + arbeitszeitstatistikDto.getSbeleg() +
	 * Helper.fitString2Length(arbeitszeitstatistikDto.getAuftragzeitenDto()
	 * .getSPersonalMaschinenname(), 80, ' ') + dtos[i].getTsBeginn() + sort,
	 * arbeitszeitstatistikDto); } else if (iOptionSortierung ==
	 * ZeiterfassungReportFac.
	 * REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_KUNDE_DATUM_PERSONAL_ARTIKEL) {
	 * 
	 * arbeitszeitstatistikDto.setSGruppierung(
	 * Helper.fitString2Length(arbeitszeitstatistikDto.getSKunde(), 60, ' ') +
	 * Helper.cutTimestamp(
	 * arbeitszeitstatistikDto.getAuftragzeitenDto().getTsBeginn()) .getTime() +
	 * arbeitszeitstatistikDto.getAuftragzeitenDto().getSPersonalMaschinenname() +
	 * Helper.fitString2Length(
	 * arbeitszeitstatistikDto.getAuftragzeitenDto().getSArtikelcnr(), 50, ' '));
	 * 
	 * alleDaten.put(Helper.fitString2Length(arbeitszeitstatistikDto.getSKunde(),
	 * 80, ' ') +
	 * Helper.cutTimestamp(arbeitszeitstatistikDto.getAuftragzeitenDto().getTsBeginn
	 * ()) .getTime() + Helper.fitString2Length(
	 * arbeitszeitstatistikDto.getAuftragzeitenDto().getSPersonalMaschinenname(),
	 * 80, ' ') + Helper.fitString2Length(
	 * arbeitszeitstatistikDto.getAuftragzeitenDto().getSArtikelcnr(), 50, ' ') +
	 * dtos[i].getTsBeginn() + sort, arbeitszeitstatistikDto); } sort++; } } catch
	 * (RemoteException ex) { throwEJBExceptionLPRespectOld(ex); } } }
	 * 
	 * // Telefonzeiten hinzufuegen
	 * 
	 * int iOption = 0;
	 * 
	 * try { ParametermandantDto parameterOption = (ParametermandantDto)
	 * getParameterFac().getMandantparameter( theClientDto.getMandant(),
	 * ParameterFac.KATEGORIE_PERSONAL,
	 * ParameterFac.PARAMETER_PERSONALKOSTEN_QUELLE);
	 * 
	 * iOption = ((Integer) parameterOption.getCWertAsObject()).intValue();
	 * 
	 * } catch (RemoteException ex5) { throw new
	 * EJBExceptionLP(EJBExceptionLP.FEHLER, ex5); }
	 * 
	 * if (belegartCNr == null || belegartCNr.equals(LocaleFac.BELEGART_PROJEKT)) {
	 * ArtikelDto artikelDto_DefaultAZ =
	 * getZeiterfassungFac().getDefaultArbeitszeitartikel(theClientDto);
	 * 
	 * if (artikelIId == null || artikelIId.equals(artikelDto_DefaultAZ.getIId())) {
	 * 
	 * if (artikelgruppeIId == null ||
	 * artikelgruppeIId.equals(artikelDto_DefaultAZ.getArtgruIId())) {
	 * 
	 * if (artikelklasseIId == null ||
	 * artikelklasseIId.equals(artikelDto_DefaultAZ.getArtklaIId())) {
	 * 
	 * Session sessionTel = FLRSessionFactory.getFactory().openSession();
	 * 
	 * String sQuery = "SELECT t FROM FLRTelefonzeiten as t "
	 * 
	 * + " WHERE t.t_von>='" + Helper.formatDateWithSlashes(new
	 * java.sql.Date(tVon.getTime())) + "' AND t.t_bis<'" +
	 * Helper.formatDateWithSlashes(new java.sql.Date(tBis.getTime())) + "' ";
	 * 
	 * sQuery += " AND t.flrpersonal.mandant_c_nr='" + theClientDto.getMandant() +
	 * "' ";
	 * 
	 * if (partnerIId != null) { sQuery += " AND t.partner_i_id=" + partnerIId +
	 * " "; }
	 * 
	 * if (personalIId != null) { sQuery += " AND t.personal_i_id=" + personalIId +
	 * " "; }
	 * 
	 * if (belegartCNr != null && belegartCNr.equals(LocaleFac.BELEGART_PROJEKT)) {
	 * if (belegartIId != null) { sQuery += " AND t.projekt_i_id=" + belegartIId +
	 * " "; } }
	 * 
	 * Query queryTelefon = sessionTel.createQuery(sQuery);
	 * 
	 * List<?> resultListTelefon = queryTelefon.list();
	 * 
	 * Iterator<?> resultListIteratorTelefon = resultListTelefon.iterator();
	 * 
	 * while (resultListIteratorTelefon.hasNext()) { FLRTelefonzeiten tel =
	 * (FLRTelefonzeiten) resultListIteratorTelefon.next(); sort++;
	 * 
	 * ArbeitszeitstatistikDto arbeitszeitstatistikDto = new
	 * ArbeitszeitstatistikDto();
	 * 
	 * AuftragzeitenDto auftragzeitenDto = new AuftragzeitenDto();
	 * 
	 * auftragzeitenDto.setTsBeginn(new Timestamp(tel.getT_von().getTime()));
	 * auftragzeitenDto.setTsEnde(new Timestamp(tel.getT_bis().getTime()));
	 * 
	 * // PJ20739
	 * auftragzeitenDto.setFVerrechenbarInProzent(tel.getF_verrechenbar()); if
	 * (tel.getT_erledigt() != null) { auftragzeitenDto.setTsErledigt(new
	 * Timestamp(tel.getT_erledigt().getTime())); } if
	 * (tel.getFlrpersonal_erledigt() != null) {
	 * auftragzeitenDto.setSPersonalKurzzeichenErledigt(
	 * tel.getFlrpersonal_erledigt().getC_kurzzeichen()); }
	 * 
	 * java.sql.Time tDauer = new java.sql.Time( tel.getT_bis().getTime() -
	 * tel.getT_von().getTime()); tDauer.setTime(tDauer.getTime() - 3600000);
	 * auftragzeitenDto.setTDauer(tDauer); Double dDauer =
	 * Helper.time2Double(tDauer); auftragzeitenDto.setDdDauer(dDauer);
	 * 
	 * auftragzeitenDto.setSPersonalKurzzeichen(tel.getFlrpersonal().
	 * getC_kurzzeichen());
	 * 
	 * String sName =
	 * tel.getFlrpersonal().getFlrpartner().getC_name1nachnamefirmazeile1(); if
	 * (tel.getFlrpersonal().getFlrpartner().getC_name2vornamefirmazeile2() != null)
	 * { sName = tel.getFlrpersonal().getFlrpartner().getC_name2vornamefirmazeile2()
	 * + " " + sName; }
	 * 
	 * auftragzeitenDto.setSPersonalnummer(tel.getFlrpersonal().getC_personalnummer(
	 * ));
	 * 
	 * auftragzeitenDto.setSPersonalMaschinenname(sName);
	 * auftragzeitenDto.setSArtikelcnr(artikelDto_DefaultAZ.getCNr());
	 * 
	 * if (artikelDto_DefaultAZ.getArtikelsprDto() != null) { auftragzeitenDto
	 * .setSArtikelbezeichnung(artikelDto_DefaultAZ.getArtikelsprDto().getCBez()); }
	 * 
	 * auftragzeitenDto.setSZeitbuchungtext(tel.getX_kommentarext());
	 * 
	 * BigDecimal bdKosten =
	 * getZeiterfassungFac().getPersonalKostenProStunde(theClientDto, null, iOption,
	 * artikelDto_DefaultAZ.getIId(), tel.getPersonal_i_id(), new
	 * Timestamp(tel.getT_von().getTime()));
	 * 
	 * auftragzeitenDto.setBdKosten(bdKosten.multiply(new BigDecimal(dDauer)));
	 * 
	 * arbeitszeitstatistikDto.setAuftragzeitenDto(auftragzeitenDto);
	 * arbeitszeitstatistikDto.setPartnerIId(tel.getPartner_i_id());
	 * arbeitszeitstatistikDto.setsFertigungsgruppe("");
	 * arbeitszeitstatistikDto.setSVertreter("");
	 * 
	 * // Je nachdem ob ein Projekt hinterlegt ist oder // nicht, sieht die
	 * Projektnummer anders aus Integer partnerIIdTelefon = null;
	 * 
	 * if (tel.getProjekt_i_id() != null) { ProjektDto pjDto =
	 * getProjektFac().projektFindByPrimaryKey(tel.getProjekt_i_id());
	 * partnerIIdTelefon = pjDto.getPartnerIId(); arbeitszeitstatistikDto.setSbeleg(
	 * hmBelegarten.get(LocaleFac.BELEGART_PROJEKT) + pjDto.getCNr() + "TZ");
	 * 
	 * arbeitszeitstatistikDto.setSBelegbezeichnung(pjDto.getCTitel());
	 * arbeitszeitstatistikDto.setSProjektStatus(pjDto.getStatusCNr());
	 * arbeitszeitstatistikDto.setSProjektKategorie(pjDto.getKategorieCNr());
	 * 
	 * arbeitszeitstatistikDto.setSProjektTyp(pjDto.getProjekttypCNr());
	 * arbeitszeitstatistikDto.setTProjektInternErledigt(pjDto.getTInternerledigt())
	 * ; arbeitszeitstatistikDto.setsProjektVerrechenbar(pjDto.getBVerrechenbar());
	 * 
	 * arbeitszeitstatistikDto.setIProjektIId(pjDto.getIId());
	 * arbeitszeitstatistikDto.setSProjektBereich(getProjektServiceFac()
	 * .bereichFindByPrimaryKey(pjDto.getBereichIId()).getCBez());
	 * 
	 * hsBereitsBeruecksichtigteProjekte.add(tel.getProjekt_i_id());
	 * 
	 * } else { arbeitszeitstatistikDto .setSbeleg("TZ" +
	 * Helper.formatDatum(tel.getT_von(), theClientDto.getLocUi()));
	 * partnerIIdTelefon = tel.getPartner_i_id(); }
	 * 
	 * if (partnerIIdTelefon != null) {
	 * 
	 * PartnerDto partnerDto_Telefon = getPartnerFac()
	 * .partnerFindByPrimaryKey(partnerIIdTelefon, theClientDto);
	 * 
	 * arbeitszeitstatistikDto.setSKunde(partnerDto_Telefon.formatFixName1Name2());
	 * } else { arbeitszeitstatistikDto.setSKunde(""); }
	 * 
	 * if (!kostenstellen.containsKey(tel.getPersonal_i_id())) {
	 * 
	 * PersonalDto personalDto = getPersonalFac()
	 * .personalFindByPrimaryKey(tel.getPersonal_i_id(), theClientDto);
	 * 
	 * if (personalDto.getKostenstelleDto_Stamm() != null) {
	 * kostenstellen.put(tel.getPersonal_i_id(),
	 * personalDto.getKostenstelleDto_Stamm().getCNr()); } else {
	 * kostenstellen.put(tel.getPersonal_i_id(), ""); }
	 * 
	 * }
	 * 
	 * arbeitszeitstatistikDto .setSKostenstelle((String)
	 * kostenstellen.get(tel.getPersonal_i_id()));
	 * 
	 * if (artikelDto_DefaultAZ.getArtgruIId() != null) {
	 * arbeitszeitstatistikDto.setSArtikelgruppe(getArtikelFac()
	 * .artgruFindByPrimaryKey(artikelDto_DefaultAZ.getArtgruIId(), theClientDto)
	 * .getBezeichnung()); } if (artikelDto_DefaultAZ.getArtklaIId() != null) {
	 * arbeitszeitstatistikDto.setSArtikelklasse(getArtikelFac()
	 * .artklaFindByPrimaryKey(artikelDto_DefaultAZ.getArtklaIId(), theClientDto)
	 * .getBezeichnung()); }
	 * 
	 * if (iOptionSortierung == ZeiterfassungReportFac.
	 * REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_ARTIKELGRUPPE) {
	 * arbeitszeitstatistikDto.setSGruppierung(arbeitszeitstatistikDto.
	 * getSArtikelgruppe()); alleDaten.put(
	 * arbeitszeitstatistikDto.getSArtikelgruppe() + new
	 * Timestamp(tel.getT_von().getTime()) + sort, arbeitszeitstatistikDto); } else
	 * if (iOptionSortierung == ZeiterfassungReportFac.
	 * REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_ARTIKELKLASSE) {
	 * arbeitszeitstatistikDto.setSGruppierung(arbeitszeitstatistikDto.
	 * getSArtikelklasse()); alleDaten.put(
	 * arbeitszeitstatistikDto.getSArtikelklasse() + new
	 * Timestamp(tel.getT_von().getTime()) + sort, arbeitszeitstatistikDto); } else
	 * if (iOptionSortierung ==
	 * ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_ADRESSE)
	 * { arbeitszeitstatistikDto.setSGruppierung(arbeitszeitstatistikDto.getSKunde()
	 * + ""); alleDaten.put(
	 * Helper.fitString2Length(arbeitszeitstatistikDto.getSKunde(), 80, ' ') +
	 * Helper.fitString2Length(arbeitszeitstatistikDto.getAuftragzeitenDto()
	 * .getSPersonalMaschinenname(), 80, ' ') + new
	 * Timestamp(tel.getT_von().getTime()) + sort, arbeitszeitstatistikDto); } else
	 * if (iOptionSortierung ==
	 * ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_ARTIKEL)
	 * { arbeitszeitstatistikDto.setSGruppierung(
	 * arbeitszeitstatistikDto.getAuftragzeitenDto().getSArtikelcnr());
	 * alleDaten.put( arbeitszeitstatistikDto.getAuftragzeitenDto().getSArtikelcnr()
	 * + new Timestamp(tel.getT_von().getTime()) + sort, arbeitszeitstatistikDto); }
	 * else if (iOptionSortierung == ZeiterfassungReportFac.
	 * REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_KOSTENSTELLE) {
	 * arbeitszeitstatistikDto.setSGruppierung(arbeitszeitstatistikDto.
	 * getSKostenstelle()); alleDaten.put(
	 * arbeitszeitstatistikDto.getSKostenstelle() +
	 * arbeitszeitstatistikDto.getAuftragzeitenDto() .getSPersonalMaschinenname() +
	 * new Timestamp(tel.getT_von().getTime()) + sort, arbeitszeitstatistikDto); }
	 * else if (iOptionSortierung == ZeiterfassungReportFac.
	 * REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_PERSONAL) {
	 * arbeitszeitstatistikDto.setSGruppierung(
	 * arbeitszeitstatistikDto.getAuftragzeitenDto().getSPersonalnummer());
	 * alleDaten.put(
	 * arbeitszeitstatistikDto.getAuftragzeitenDto().getSPersonalMaschinenname() +
	 * new Timestamp(tel.getT_von().getTime()) + sort, arbeitszeitstatistikDto); }
	 * else if (iOptionSortierung ==
	 * ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_AUFTRAG)
	 * {
	 * 
	 * String sBeleg = arbeitszeitstatistikDto.getSbeleg();
	 * 
	 * if (sBeleg.endsWith("TZ")) { sBeleg = sBeleg.substring(0, sBeleg.length() -
	 * 2); } arbeitszeitstatistikDto.setSGruppierung(Helper.fitString2Length(sBeleg,
	 * 15, ' ') +
	 * arbeitszeitstatistikDto.getAuftragzeitenDto().getSPersonalMaschinenname());
	 * alleDaten.put(sBeleg +
	 * arbeitszeitstatistikDto.getAuftragzeitenDto().getSPersonalMaschinenname() +
	 * new Timestamp(tel.getT_von().getTime()) + sort, arbeitszeitstatistikDto); }
	 * else if (iOptionSortierung == ZeiterfassungReportFac.
	 * REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_KUNDE_BELEG_PERSONAL) {
	 * 
	 * String sBeleg = arbeitszeitstatistikDto.getSbeleg();
	 * 
	 * if (sBeleg.endsWith("TZ")) { sBeleg = sBeleg.substring(0, sBeleg.length() -
	 * 2); }
	 * 
	 * arbeitszeitstatistikDto.setSGruppierung(
	 * Helper.fitString2Length(arbeitszeitstatistikDto.getSKunde(), 60, ' ') +
	 * Helper.fitString2Length(sBeleg, 15, ' ') + arbeitszeitstatistikDto
	 * .getAuftragzeitenDto().getSPersonalMaschinenname()); alleDaten.put(
	 * Helper.fitString2Length(arbeitszeitstatistikDto.getSKunde(), 80, ' ') +
	 * arbeitszeitstatistikDto.getSbeleg() +
	 * Helper.fitString2Length(arbeitszeitstatistikDto.getAuftragzeitenDto()
	 * .getSPersonalMaschinenname(), 80, ' ') + new
	 * Timestamp(tel.getT_von().getTime()) + sort, arbeitszeitstatistikDto); } else
	 * if (iOptionSortierung == ZeiterfassungReportFac.
	 * REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_KUNDE_DATUM_PERSONAL_ARTIKEL) {
	 * 
	 * arbeitszeitstatistikDto.setSGruppierung(
	 * Helper.fitString2Length(arbeitszeitstatistikDto.getSKunde(), 60, ' ') +
	 * Helper.cutTimestamp(
	 * arbeitszeitstatistikDto.getAuftragzeitenDto().getTsBeginn()) .getTime() +
	 * arbeitszeitstatistikDto.getAuftragzeitenDto() .getSPersonalMaschinenname() +
	 * Helper.fitString2Length(
	 * arbeitszeitstatistikDto.getAuftragzeitenDto().getSArtikelcnr(), 50, ' '));
	 * alleDaten.put( Helper.fitString2Length(arbeitszeitstatistikDto.getSKunde(),
	 * 80, ' ') + Helper.cutTimestamp(
	 * arbeitszeitstatistikDto.getAuftragzeitenDto().getTsBeginn()) .getTime() +
	 * Helper.fitString2Length(arbeitszeitstatistikDto.getAuftragzeitenDto()
	 * .getSPersonalMaschinenname(), 80, ' ') + Helper.fitString2Length(
	 * arbeitszeitstatistikDto.getAuftragzeitenDto().getSArtikelcnr(), 50, ' ') +
	 * new Timestamp(tel.getT_von().getTime()) + sort, arbeitszeitstatistikDto); }
	 * 
	 * }
	 * 
	 * sessionTel.close(); } } }
	 * 
	 * // SP5059
	 * 
	 * if (mitErledigtenProjektenImZeitraum) {
	 * 
	 * Session sessionZusPJ = FLRSessionFactory.getFactory().openSession(); //
	 * Hiberante Criteria fuer alle Tabellen ausgehend von meiner // Haupttabelle
	 * anlegen, // nach denen ich filtern und sortieren kann Criteria crit =
	 * sessionZusPJ.createCriteria(FLRProjekt.class); // Einschraenkung auf den
	 * aktuellen Mandanten
	 * crit.add(Restrictions.eq(ProjektFac.FLR_PROJEKT_MANDANT_C_NR,
	 * theClientDto.getMandant()));
	 * crit.add(Restrictions.not(Restrictions.eq(ProjektFac.FLR_PROJEKT_STATUS_C_NR,
	 * ProjektServiceFac.PROJEKT_STATUS_STORNIERT)));
	 * 
	 * if (!hsBereitsBeruecksichtigteProjekte.isEmpty()) {
	 * crit.add(Restrictions.not(Restrictions.in("i_id",
	 * hsBereitsBeruecksichtigteProjekte))); }
	 * 
	 * // Einschraenkung nach Status Offen, Erledigt
	 * 
	 * if (projektInterneErledigungBeruecksichtigen == false) { // PJ18471
	 * crit.createAlias("flrprojektstatus", "s");
	 * crit.add(Restrictions.eq("s.b_erledigt", Helper.boolean2Short(true))); }
	 * 
	 * if (projektInterneErledigungBeruecksichtigen == true) {
	 * 
	 * crit.add(Restrictions.or( Restrictions.and(Restrictions.isNotNull(ProjektFac.
	 * FLR_PROJEKT_T_INTERNERLEDIGT),
	 * Restrictions.ge(ProjektFac.FLR_PROJEKT_T_INTERNERLEDIGT, tVon)),
	 * Restrictions.ge(ProjektFac.FLR_PROJEKT_T_ERLEDIGUNGSDATUM, tVon)));
	 * 
	 * } else { crit.add(Restrictions.ge(ProjektFac.FLR_PROJEKT_T_ERLEDIGUNGSDATUM,
	 * tVon)); }
	 * 
	 * if (projektInterneErledigungBeruecksichtigen == true) {
	 * crit.add(Restrictions.or( Restrictions.and(Restrictions.isNotNull(ProjektFac.
	 * FLR_PROJEKT_T_INTERNERLEDIGT),
	 * Restrictions.lt(ProjektFac.FLR_PROJEKT_T_INTERNERLEDIGT, tBis)),
	 * Restrictions.lt(ProjektFac.FLR_PROJEKT_T_ERLEDIGUNGSDATUM, tBis))); } else {
	 * crit.add(Restrictions.lt(ProjektFac.FLR_PROJEKT_T_ERLEDIGUNGSDATUM, tBis)); }
	 * 
	 * // Einschraenkung nach einer bestimmten Perosn if (personalIId != null) {
	 * crit.add(Restrictions.eq(ProjektFac.FLR_PROJEKT_PERSONAL_I_ID_ERLEDIGER,
	 * personalIId)); } if (partnerIId != null) {
	 * crit.add(Restrictions.eq("partner_i_id", partnerIId)); }
	 * 
	 * crit.addOrder(Order.asc(ProjektFac.FLR_PROJEKT_C_NR));
	 * 
	 * crit.addOrder(Order.asc(ProjektFac.FLR_PROJEKT_KATEGORIE_C_NR)); List<?> list
	 * = crit.list(); ArrayList<Object[]> alDaten = new ArrayList<Object[]>();
	 * Iterator<?> it = list.iterator();
	 * 
	 * while (it.hasNext()) { FLRProjekt projekt = (FLRProjekt) it.next(); Object[]
	 * oZeile = new
	 * Object[ProjektReportFac.REPORT_PROJEKT_JOURNAL_ERLEDIGT_ANZAHL_SPALTEN];
	 * 
	 * if (projektInterneErledigungBeruecksichtigen == true &&
	 * projekt.getT_internerledigt() != null && projekt.getT_erledigungsdatum() !=
	 * null) {
	 * 
	 * // Wenn intern-Erledigt und normal erledigt, dann gilt // jenes // Datum, das
	 * frueher war
	 * 
	 * if (projekt.getT_internerledigt().getTime() <=
	 * projekt.getT_erledigungsdatum().getTime()) { if (tVon != null &&
	 * projekt.getT_internerledigt().getTime() < tVon.getTime()) { continue; }
	 * 
	 * if (tBis != null && projekt.getT_internerledigt().getTime() > tBis.getTime())
	 * { continue; }
	 * 
	 * } } // Zeile hinzufuegen ArbeitszeitstatistikDto arbeitszeitstatistikDto =
	 * new ArbeitszeitstatistikDto();
	 * 
	 * AuftragzeitenDto auftragzeitenDto = new AuftragzeitenDto();
	 * 
	 * auftragzeitenDto.setTsBeginn(new
	 * Timestamp(projekt.getT_anlegen().getTime()));
	 * 
	 * if (projekt.getT_erledigungsdatum() != null) { auftragzeitenDto.setTsEnde(new
	 * Timestamp(projekt.getT_erledigungsdatum().getTime())); }
	 * 
	 * auftragzeitenDto.setSPersonalKurzzeichen(projekt.getFlrpersonalZugewiesener()
	 * .getC_kurzzeichen());
	 * 
	 * String sName = projekt.getFlrpersonalZugewiesener().getFlrpartner().
	 * getC_name1nachnamefirmazeile1(); if
	 * (projekt.getFlrpersonalZugewiesener().getFlrpartner().
	 * getC_name2vornamefirmazeile2() != null) { sName =
	 * projekt.getFlrpersonalZugewiesener().getFlrpartner().
	 * getC_name2vornamefirmazeile2() + " " + sName; }
	 * 
	 * auftragzeitenDto.setSPersonalnummer(projekt.getFlrpersonalZugewiesener().
	 * getC_personalnummer());
	 * 
	 * auftragzeitenDto.setSPersonalMaschinenname(sName);
	 * auftragzeitenDto.setSArtikelcnr(artikelDto_DefaultAZ.getCNr());
	 * 
	 * if (artikelDto_DefaultAZ.getArtikelsprDto() != null) {
	 * auftragzeitenDto.setSArtikelbezeichnung(artikelDto_DefaultAZ.getArtikelsprDto
	 * ().getCBez()); }
	 * 
	 * auftragzeitenDto.setSZeitbuchungtext(projekt.getX_freetext());
	 * 
	 * arbeitszeitstatistikDto.setAuftragzeitenDto(auftragzeitenDto);
	 * arbeitszeitstatistikDto.setPartnerIId(projekt.getPartner_i_id());
	 * arbeitszeitstatistikDto.setsFertigungsgruppe("");
	 * arbeitszeitstatistikDto.setSVertreter("");
	 * 
	 * arbeitszeitstatistikDto.setSbeleg(hmBelegarten.get(LocaleFac.
	 * BELEGART_PROJEKT) + projekt.getC_nr());
	 * 
	 * arbeitszeitstatistikDto.setSBelegbezeichnung(projekt.getC_titel());
	 * arbeitszeitstatistikDto.setSProjektStatus(projekt.getStatus_c_nr());
	 * arbeitszeitstatistikDto.setSProjektKategorie(projekt.getKategorie_c_nr());
	 * 
	 * arbeitszeitstatistikDto.setsProjektVerrechenbar(projekt.getB_verrechenbar());
	 * if (projekt.getT_internerledigt() != null) { arbeitszeitstatistikDto
	 * .setTProjektInternErledigt(new
	 * Timestamp(projekt.getT_internerledigt().getTime())); }
	 * arbeitszeitstatistikDto.setSProjektTyp(projekt.getTyp_c_nr());
	 * 
	 * arbeitszeitstatistikDto.setBdProjektGesamtdauer( new
	 * BigDecimal(getZeiterfassungFac().getSummeZeitenEinesBeleges(LocaleFac.
	 * BELEGART_PROJEKT, projekt.getI_id(), null, null, null, null, theClientDto)));
	 * 
	 * // SP6065 arbeitszeitstatistikDto.setIProjektIId(projekt.getI_id());
	 * arbeitszeitstatistikDto.setSProjektBereich(projekt.getFlrbereich().getC_bez()
	 * );
	 * 
	 * PartnerDto partnerDto =
	 * getPartnerFac().partnerFindByPrimaryKey(projekt.getPartner_i_id(),
	 * theClientDto);
	 * 
	 * arbeitszeitstatistikDto.setSKunde(partnerDto.formatFixName1Name2());
	 * 
	 * if
	 * (!kostenstellen.containsKey(projekt.getFlrpersonalZugewiesener().getI_id()))
	 * {
	 * 
	 * PersonalDto personalDto = getPersonalFac()
	 * .personalFindByPrimaryKey(projekt.getFlrpersonalZugewiesener().getI_id(),
	 * theClientDto);
	 * 
	 * if (personalDto.getKostenstelleDto_Stamm() != null) {
	 * kostenstellen.put(projekt.getFlrpersonalZugewiesener().getI_id(),
	 * personalDto.getKostenstelleDto_Stamm().getCNr()); } else {
	 * kostenstellen.put(projekt.getFlrpersonalZugewiesener().getI_id(), ""); }
	 * 
	 * }
	 * 
	 * arbeitszeitstatistikDto.setSKostenstelle( (String)
	 * kostenstellen.get(projekt.getFlrpersonalZugewiesener().getI_id()));
	 * 
	 * if (artikelDto_DefaultAZ.getArtgruIId() != null) {
	 * arbeitszeitstatistikDto.setSArtikelgruppe(getArtikelFac()
	 * .artgruFindByPrimaryKey(artikelDto_DefaultAZ.getArtgruIId(), theClientDto)
	 * .getBezeichnung()); } if (artikelDto_DefaultAZ.getArtklaIId() != null) {
	 * arbeitszeitstatistikDto.setSArtikelklasse(getArtikelFac()
	 * .artklaFindByPrimaryKey(artikelDto_DefaultAZ.getArtklaIId(), theClientDto)
	 * .getBezeichnung()); }
	 * 
	 * if (iOptionSortierung == ZeiterfassungReportFac.
	 * REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_ARTIKELGRUPPE) {
	 * arbeitszeitstatistikDto.setSGruppierung(arbeitszeitstatistikDto.
	 * getSArtikelgruppe()); alleDaten.put(
	 * arbeitszeitstatistikDto.getSArtikelgruppe() + new
	 * Timestamp(projekt.getT_anlegen().getTime()) + sort, arbeitszeitstatistikDto);
	 * } else if (iOptionSortierung == ZeiterfassungReportFac.
	 * REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_ARTIKELKLASSE) {
	 * arbeitszeitstatistikDto.setSGruppierung(arbeitszeitstatistikDto.
	 * getSArtikelklasse()); alleDaten.put(
	 * arbeitszeitstatistikDto.getSArtikelklasse() + new
	 * Timestamp(projekt.getT_anlegen().getTime()) + sort, arbeitszeitstatistikDto);
	 * } else if (iOptionSortierung ==
	 * ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_ADRESSE)
	 * { arbeitszeitstatistikDto.setSGruppierung(arbeitszeitstatistikDto.getSKunde()
	 * + ""); alleDaten.put(
	 * Helper.fitString2Length(arbeitszeitstatistikDto.getSKunde(), 80, ' ') +
	 * Helper.fitString2Length(arbeitszeitstatistikDto.getAuftragzeitenDto()
	 * .getSPersonalMaschinenname(), 80, ' ') + new
	 * Timestamp(projekt.getT_anlegen().getTime()) + sort, arbeitszeitstatistikDto);
	 * } else if (iOptionSortierung ==
	 * ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_ARTIKEL)
	 * { arbeitszeitstatistikDto
	 * .setSGruppierung(arbeitszeitstatistikDto.getAuftragzeitenDto().getSArtikelcnr
	 * ()); alleDaten.put(
	 * arbeitszeitstatistikDto.getAuftragzeitenDto().getSArtikelcnr() + new
	 * Timestamp(projekt.getT_anlegen().getTime()) + sort, arbeitszeitstatistikDto);
	 * } else if (iOptionSortierung == ZeiterfassungReportFac.
	 * REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_KOSTENSTELLE) {
	 * arbeitszeitstatistikDto.setSGruppierung(arbeitszeitstatistikDto.
	 * getSKostenstelle()); alleDaten.put(
	 * arbeitszeitstatistikDto.getSKostenstelle() +
	 * arbeitszeitstatistikDto.getAuftragzeitenDto().getSPersonalMaschinenname() +
	 * new Timestamp(projekt.getT_anlegen().getTime()) + sort,
	 * arbeitszeitstatistikDto); } else if (iOptionSortierung ==
	 * ZeiterfassungReportFac.
	 * REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_PERSONAL) {
	 * arbeitszeitstatistikDto
	 * .setSGruppierung(arbeitszeitstatistikDto.getAuftragzeitenDto().
	 * getSPersonalnummer()); alleDaten.put(
	 * arbeitszeitstatistikDto.getAuftragzeitenDto().getSPersonalMaschinenname() +
	 * new Timestamp(projekt.getT_anlegen().getTime()) + sort,
	 * arbeitszeitstatistikDto); } else if (iOptionSortierung ==
	 * ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_AUFTRAG)
	 * {
	 * 
	 * String sBeleg = arbeitszeitstatistikDto.getSbeleg();
	 * 
	 * arbeitszeitstatistikDto.setSGruppierung(Helper.fitString2Length(sBeleg, 15, '
	 * ') +
	 * arbeitszeitstatistikDto.getAuftragzeitenDto().getSPersonalMaschinenname());
	 * alleDaten.put( sBeleg +
	 * arbeitszeitstatistikDto.getAuftragzeitenDto().getSPersonalMaschinenname() +
	 * new Timestamp(projekt.getT_anlegen().getTime()) + sort,
	 * arbeitszeitstatistikDto); } else if (iOptionSortierung ==
	 * ZeiterfassungReportFac.
	 * REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_KUNDE_BELEG_PERSONAL) {
	 * 
	 * String sBeleg = arbeitszeitstatistikDto.getSbeleg();
	 * 
	 * arbeitszeitstatistikDto
	 * .setSGruppierung(Helper.fitString2Length(arbeitszeitstatistikDto.getSKunde(),
	 * 60, ' ') + Helper.fitString2Length(sBeleg, 15, ' ') +
	 * arbeitszeitstatistikDto.getAuftragzeitenDto().getSPersonalMaschinenname());
	 * alleDaten.put( Helper.fitString2Length(arbeitszeitstatistikDto.getSKunde(),
	 * 80, ' ') + arbeitszeitstatistikDto.getSbeleg() +
	 * Helper.fitString2Length(arbeitszeitstatistikDto.getAuftragzeitenDto()
	 * .getSPersonalMaschinenname(), 80, ' ') + new
	 * Timestamp(projekt.getT_anlegen().getTime()) + sort, arbeitszeitstatistikDto);
	 * } else if (iOptionSortierung == ZeiterfassungReportFac.
	 * REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_KUNDE_DATUM_PERSONAL_ARTIKEL) {
	 * 
	 * arbeitszeitstatistikDto
	 * .setSGruppierung(Helper.fitString2Length(arbeitszeitstatistikDto.getSKunde(),
	 * 60, ' ') + Helper.cutTimestamp(
	 * arbeitszeitstatistikDto.getAuftragzeitenDto().getTsBeginn()).getTime() +
	 * arbeitszeitstatistikDto.getAuftragzeitenDto().getSPersonalMaschinenname() +
	 * Helper.fitString2Length(
	 * arbeitszeitstatistikDto.getAuftragzeitenDto().getSArtikelcnr(), 50, ' '));
	 * alleDaten.put( Helper.fitString2Length(arbeitszeitstatistikDto.getSKunde(),
	 * 80, ' ') + Helper.cutTimestamp(
	 * arbeitszeitstatistikDto.getAuftragzeitenDto().getTsBeginn()).getTime() +
	 * Helper.fitString2Length(arbeitszeitstatistikDto.getAuftragzeitenDto()
	 * .getSPersonalMaschinenname(), 80, ' ') + Helper.fitString2Length(
	 * arbeitszeitstatistikDto.getAuftragzeitenDto().getSArtikelcnr(), 50, ' ') +
	 * new Timestamp(projekt.getT_anlegen().getTime()) + sort,
	 * arbeitszeitstatistikDto); }
	 * 
	 * }
	 * 
	 * sessionZusPJ.close(); }
	 * 
	 * }
	 * 
	 * // data = new //
	 * Object[alleDaten.size()][REPORT_ARBEITSZEITSTATISTIK_ANZAHL_SPALTEN];
	 * setData(new
	 * Object[alleDaten.size()][REPORT_ARBEITSZEITSTATISTIK_ANZAHL_SPALTEN]);
	 * 
	 * List<ArbeitszeitstatistikDto> tempAlleDaten = new
	 * ArrayList<ArbeitszeitstatistikDto>(alleDaten.size()); Iterator<?> itTemp =
	 * alleDaten.keySet().iterator(); while (itTemp.hasNext()) { Object k =
	 * itTemp.next(); tempAlleDaten.add(alleDaten.get(k)); }
	 * 
	 * Collections.sort(tempAlleDaten, getSortGruppierungBeginnzeitComparator());
	 * Iterator<?> it = tempAlleDaten.iterator(); int iRow = 0; while (it.hasNext())
	 * { Object k = it.next(); // ArbeitszeitstatistikDto dto =
	 * (ArbeitszeitstatistikDto) alleDaten // .get(k); ArbeitszeitstatistikDto dto =
	 * (ArbeitszeitstatistikDto) k;
	 * data[iRow][REPORT_ARBEITSZEITSTATISTIK_ARTIKELNUMMER] =
	 * dto.getAuftragzeitenDto().getSArtikelcnr();
	 * data[iRow][REPORT_ARBEITSZEITSTATISTIK_BEZEICHNUNG] =
	 * dto.getAuftragzeitenDto().getSArtikelbezeichnung(); if
	 * (dto.getAuftragzeitenDto().getDdDauer() != null) {
	 * data[iRow][REPORT_ARBEITSZEITSTATISTIK_DAUER] = new
	 * BigDecimal(dto.getAuftragzeitenDto().getDdDauer()); }
	 * 
	 * data[iRow][REPORT_ARBEITSZEITSTATISTIK_PROJEKT_GESAMTDAUER] =
	 * dto.getBdProjektGesamtdauer();
	 * data[iRow][REPORT_ARBEITSZEITSTATISTIK_PROJEKT_STATUS] =
	 * dto.getSProjektStatus(); data[iRow][REPORT_ARBEITSZEITSTATISTIK_PROJEKT_I_ID]
	 * = dto.getIProjektIId();
	 * data[iRow][REPORT_ARBEITSZEITSTATISTIK_PROJEKT_BEREICH] =
	 * dto.getSProjektBereich();
	 * data[iRow][REPORT_ARBEITSZEITSTATISTIK_PROJEKT_KATEGORIE] =
	 * dto.getSProjektKategorie();
	 * data[iRow][REPORT_ARBEITSZEITSTATISTIK_PROJEKT_TYP] = dto.getSProjektTyp();
	 * data[iRow][REPORT_ARBEITSZEITSTATISTIK_PROJEKT_INTERN_ERLEDIGT] =
	 * dto.getTProjektInternErledigt();
	 * data[iRow][REPORT_ARBEITSZEITSTATISTIK_PROJEKT_VERRECHENBAR] =
	 * dto.getSProjektVerrechenbar();
	 * 
	 * // PJ20739 data[iRow][REPORT_ARBEITSZEITSTATISTIK_VERRECHENBAR] =
	 * dto.getAuftragzeitenDto() .getFVerrechenbarInProzent();
	 * data[iRow][REPORT_ARBEITSZEITSTATISTIK_PERSON_ERLEDIGT] =
	 * dto.getAuftragzeitenDto() .getSPersonalKurzzeichenErledigt();
	 * data[iRow][REPORT_ARBEITSZEITSTATISTIK_ZEITPUNKT_ERLEDIGT] =
	 * dto.getAuftragzeitenDto().getTsErledigt();
	 * 
	 * data[iRow][REPORT_ARBEITSZEITSTATISTIK_VON] =
	 * dto.getAuftragzeitenDto().getTsBeginn();
	 * data[iRow][REPORT_ARBEITSZEITSTATISTIK_BIS] =
	 * dto.getAuftragzeitenDto().getTsEnde();
	 * data[iRow][REPORT_ARBEITSZEITSTATISTIK_KOSTEN] =
	 * dto.getAuftragzeitenDto().getBdKosten();
	 * 
	 * data[iRow][REPORT_ARBEITSZEITSTATISTIK_BEMERKUNG] =
	 * dto.getAuftragzeitenDto().getSZeitbuchungtext();
	 * data[iRow][REPORT_ARBEITSZEITSTATISTIK_KOMMENTAR] =
	 * dto.getAuftragzeitenDto().getSKommentar();
	 * 
	 * data[iRow][REPORT_ARBEITSZEITSTATISTIK_PERSON_KOSTENSTELLE] =
	 * dto.getSKostenstelle();
	 * data[iRow][REPORT_ARBEITSZEITSTATISTIK_FERTIGUNGSGRUPPE] =
	 * dto.getsFertigungsgruppe();
	 * 
	 * if (dto.getAuftragzeitenDto().getSPersonalMaschinenname().startsWith("P:")) {
	 * data[iRow][REPORT_ARBEITSZEITSTATISTIK_PERSON] =
	 * dto.getAuftragzeitenDto().getSPersonalMaschinenname() .substring(2);
	 * 
	 * data[iRow][REPORT_ARBEITSZEITSTATISTIK_PERSONALNUMMER] =
	 * dto.getAuftragzeitenDto().getSPersonalnummer();
	 * 
	 * data[iRow][REPORT_ARBEITSZEITSTATISTIK_KURZZEICHEN] =
	 * dto.getAuftragzeitenDto() .getSPersonalKurzzeichen();
	 * 
	 * } else { data[iRow][REPORT_ARBEITSZEITSTATISTIK_PERSON] =
	 * dto.getAuftragzeitenDto().getSPersonalMaschinenname();
	 * data[iRow][REPORT_ARBEITSZEITSTATISTIK_PERSONALNUMMER] =
	 * dto.getAuftragzeitenDto().getSPersonalnummer();
	 * 
	 * data[iRow][REPORT_ARBEITSZEITSTATISTIK_KURZZEICHEN] =
	 * dto.getAuftragzeitenDto() .getSPersonalKurzzeichen(); }
	 * 
	 * if (data[iRow][REPORT_ARBEITSZEITSTATISTIK_KURZZEICHEN] == null) {
	 * data[iRow][REPORT_ARBEITSZEITSTATISTIK_KURZZEICHEN] =
	 * dto.getSPersonalKurzzeichen(); }
	 * 
	 * data[iRow][REPORT_ARBEITSZEITSTATISTIK_BELEG] = dto.getSbeleg();
	 * data[iRow][REPORT_ARBEITSZEITSTATISTIK_VERTRETER] = dto.getSVertreter(); if
	 * (dto.getSBelegbezeichnung() != null)
	 * data[iRow][REPORT_ARBEITSZEITSTATISTIK_PROJEKT] = dto.getSBelegbezeichnung();
	 * 
	 * data[iRow][REPORT_ARBEITSZEITSTATISTIK_KUNDENBESTELLNUMMER] =
	 * dto.getSKundenbestellnummer();
	 * 
	 * data[iRow][REPORT_ARBEITSZEITSTATISTIK_KUNDE] = dto.getSKunde();
	 * data[iRow][REPORT_ARBEITSZEITSTATISTIK_ARTIKELGRUPPE] =
	 * dto.getSArtikelgruppe();
	 * data[iRow][REPORT_ARBEITSZEITSTATISTIK_ARTIKELKLASSE] =
	 * dto.getSArtikelklasse(); data[iRow][REPORT_ARBEITSZEITSTATISTIK_GRUPPIERUNG]
	 * = dto.getSGruppierung();
	 * 
	 * if (dto.getSGruppierung() == null) { int u = 0; }
	 * 
	 * iRow++;
	 * 
	 * }
	 * 
	 * initJRDS(parameter, ZeiterfassungReportFac.REPORT_MODUL,
	 * ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK,
	 * theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto); return
	 * getReportPrint();
	 * 
	 * }
	 */

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printProduktivitaetstagesstatistik(Integer personalIId, java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, Integer iOption, Integer kostenstelleIIdAbteilung, boolean bMitVersteckten,
			boolean bNurAnwesende, boolean bMonatsbetrachtung, Integer personalgruppeIId, TheClientDto theClientDto) {

		sAktuellerReport = ZeiterfassungReportFac.REPORT_PRODUKTIVITAETSTAGESSTATISTIK;

		ParametermandantDto parameterIstZeit;

		boolean bTheoretischeIstZeit = false;

		try {
			parameterIstZeit = (ParametermandantDto) getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_PERSONAL, ParameterFac.PARAMETER_THEORETISCHE_IST_ZEIT_RECHNUNG);

			bTheoretischeIstZeit = (Boolean) parameterIstZeit.getCWertAsObject();

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		if (tVon == null || tBis == null || personalIId == iOption) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("tVon == null || tBis == null || personalIId == iOption"));
		}
		tVon = Helper.cutTimestamp(tVon);
		tBis = Helper.cutTimestamp(tBis);

		if (tVon.after(tBis)) {
			java.sql.Timestamp h = tVon;
			tVon = tBis;
			tBis = h;
		}
		String sParameter_Personen = "";
		TreeMap<String, Object[]> tmDaten = new TreeMap<String, Object[]>();

		PersonalDto[] personalDtos = null;
		MandantDto mandantDto = null;
		try {
			mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
		} catch (RemoteException ex2) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex2);
		}
		try {
			if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_SELEKTIERTE_PERSON) {

				if (personalIId == null) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
							new Exception("personalIId == null"));
				}

				personalDtos = new PersonalDto[1];

				personalDtos[0] = getPersonalFac().personalFindByPrimaryKey(personalIId, theClientDto);
				sParameter_Personen = personalDtos[0].getPartnerDto().formatAnrede();
			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_PERSONEN) {
				personalDtos = getPersonalFac().personalFindByMandantCNr(theClientDto.getMandant(), bMitVersteckten);
				sParameter_Personen = "Alle Personen";
			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_EINE_ABTEILUNG) {
				PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(personalIId, theClientDto);
				personalDtos = getPersonalFac().personalFindAllPersonenEinerAbteilung(kostenstelleIIdAbteilung,
						theClientDto.getMandant(), bMitVersteckten);
				sParameter_Personen = "Meine Abteilung";
			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_ARBEITER) {
				personalDtos = getPersonalFac().personalFindAllArbeiterEinesMandanten(theClientDto.getMandant(),
						bMitVersteckten);
				sParameter_Personen = "Alle Arbeiter";
			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_ANGESTELLTE) {
				personalDtos = getPersonalFac().personalFindAllAngestellteEinesMandanten(theClientDto.getMandant(),
						bMitVersteckten);
				sParameter_Personen = "Alle Angestellten";
			} else {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("OPTION NICHT VERFUEGBAR"));
			}

			if (bNurAnwesende) {
				personalDtos = getZeiterfassungFac().entferneNichtAnwesendePersonen(tVon, tBis, personalDtos,
						theClientDto);
			}

		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		HashMap<String, LPDatenSubreport> hmSubreportFuerAllePersonen = new HashMap<String, LPDatenSubreport>();

		for (int i = 0; i < personalDtos.length; i++) {
			PersonalDto personalDto = personalDtos[i];
			personalDto
					.setPartnerDto(getPartnerFac().partnerFindByPrimaryKey(personalDto.getPartnerIId(), theClientDto));
			String person = personalDto.getCPersonalnr() + " " + personalDto.getPartnerDto().formatAnrede();
			if (personalgruppeIId != null) {
				if (!personalgruppeIId.equals(personalDto.getPersonalgruppeIId())) {
					continue;
				}
			}

			LinkedHashMap<String, Object[]> hmSubreportMaschinenerfolg = new LinkedHashMap<String, Object[]>();
			LinkedHashMap<String, BigDecimal> hmAnwesenheitszeitMonat = new LinkedHashMap<String, BigDecimal>();

			Timestamp tBeginn = new Timestamp(tVon.getTime());

			while (tBeginn.getTime() < tBis.getTime()) {

				String sQuery = "select distinct zeitdaten.i_belegartid,zeitdaten.flrlossollarbeitsplan";

				// Hole Zeitdaten eines Tages
				javax.persistence.Query query = em.createNamedQuery("ZeitdatenfindZeitdatenEinesTagesUndEinerPerson");
				query.setParameter(1, personalIId);
				query.setParameter(2, new java.sql.Timestamp(tBeginn.getTime()));
				query.setParameter(3, Helper.addiereTageZuTimestamp(new java.sql.Timestamp(tBeginn.getTime()), 1));
				Collection<?> cl = query.getResultList();
				ZeitdatenDto[] zeitdatenDtos = getZeiterfassungFac().assembleZeitdatenDtosOhneBelegzeiten(cl);

				Integer taetigkeitIId_Kommt = getZeiterfassungFac()
						.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_KOMMT, theClientDto).getIId();

				// Daten einen Tag vorher wg. Mitternachtssprung holen
				java.sql.Date dVon = new java.sql.Date(tBeginn.getTime() - (3600000 * 24));
				if (zeitdatenDtos.length > 0 && zeitdatenDtos[0].getTaetigkeitIId() != null
						&& zeitdatenDtos[0].getTaetigkeitIId().equals(taetigkeitIId_Kommt)) {
					// Ausser die erste Taetigkeit ist ein Kommt, dann nicht
					dVon = new java.sql.Date(tBeginn.getTime());
				}

				sQuery += " from FLRZeitdatenLos zeitdaten WHERE zeitdaten.flrpersonal.i_id=" + personalDto.getIId()
						+ "AND zeitdaten.c_belegartnr ='" + LocaleFac.BELEGART_LOS + "' AND zeitdaten.t_zeit>='"
						+ Helper.formatDateWithSlashes(new java.sql.Date(tBeginn.getTime()))
						+ "' AND zeitdaten.t_zeit<'"
						+ Helper.formatDateWithSlashes(new java.sql.Date(tBeginn.getTime() + (3600000 * 24))) + "'";

				SessionFactory factory = FLRSessionFactory.getFactory();
				Session session = factory.openSession();

				org.hibernate.Query inventurliste = session.createQuery(sQuery);

				List<?> resultList = inventurliste.list();

				Iterator<?> resultListIterator = resultList.iterator();

				while (resultListIterator.hasNext()) {
					Object[] o = (Object[]) resultListIterator.next();
					Integer losIId = (Integer) o[0];

					FLRLossollarbeitsplan flrlossollarbeitsplan = (FLRLossollarbeitsplan) o[1];

					if (flrlossollarbeitsplan != null) {

						Integer lossollarbeitsplanIId = flrlossollarbeitsplan.getI_id();

						Object[] oZeile = new Object[REPORT_PRODUKTIVITAETSTAGESSTATISTIK_ANZAHL_SPALTEN];
						// von heute bis morgen

						if (bTheoretischeIstZeit == false) {
							AuftragzeitenDto[] azDtos = getZeiterfassungFac().getAllZeitenEinesBeleges(
									LocaleFac.BELEGART_LOS, losIId, lossollarbeitsplanIId, personalDto.getIId(),
									tBeginn, Helper.addiereTageZuTimestamp(tBeginn, 1),
									ZeiterfassungFac.SORTIERUNG_ZEITDATEN_ARTIKEL, false, theClientDto);

							Double dDauer = 0D;
							for (int k = 0; k < azDtos.length; k++) {
								if (azDtos[k] != null && azDtos[k].getDdDauer() != null) {
									dDauer = dDauer + azDtos[k].getDdDauer().doubleValue();
								}
							}

							oZeile[REPORT_PRODUKTIVITAETSTAGESSTATISTIK_DAUER] = dDauer;
						}

						oZeile[REPORT_PRODUKTIVITAETSTAGESSTATISTIK_PERSON] = person;

						oZeile[REPORT_PRODUKTIVITAETSTAGESSTATISTIK_DATUM] = tBeginn;

						oZeile[REPORT_PRODUKTIVITAETSTAGESSTATISTIK_LEISTUNGSFAKTOR] = 100D;
						try {
							Calendar c = Calendar.getInstance();
							c.setTimeInMillis(tBeginn.getTime());
							PersonalgehaltDto pgDto = getPersonalFac().personalgehaltFindLetztePersonalgehalt(
									personalDto.getIId(), c.get(Calendar.YEAR), c.get(Calendar.MONTH));
							if (pgDto != null && pgDto.getFLeistungswert() != null) {
								oZeile[REPORT_PRODUKTIVITAETSTAGESSTATISTIK_LEISTUNGSFAKTOR] = pgDto
										.getFLeistungswert();
							}

							// SP1450
							if (bMonatsbetrachtung) {
								String keySubreport = new SimpleDateFormat("MMMM", Locale.GERMAN).format(tBeginn) + " "
										+ c.get(Calendar.YEAR);
								if (!hmSubreportMaschinenerfolg.containsKey(keySubreport)) {

									Calendar cVonSubreport = Calendar.getInstance();
									cVonSubreport.setTimeInMillis(tBeginn.getTime());
									cVonSubreport.set(Calendar.DAY_OF_MONTH, 1);

									if (cVonSubreport.getTime().before(tVon)) {
										cVonSubreport.setTime(new Date(tVon.getTime()));
									}

									Calendar cBisSubreport = Calendar.getInstance();
									cBisSubreport.setTimeInMillis(tBeginn.getTime());
									cBisSubreport.set(Calendar.DAY_OF_MONTH,
											cBisSubreport.getActualMaximum(Calendar.DAY_OF_MONTH));

									if (cBisSubreport.getTime().after(tBis)) {
										cBisSubreport.setTime(new Date(tBis.getTime()));
									}

									// SP1723
									cBisSubreport.add(Calendar.DATE, 1);

									MaschinenerfolgReportDto reportDto = printMaschinenerfolg(personalDto.getIId(),
											null, new Timestamp(cVonSubreport.getTimeInMillis()),
											new Timestamp(cBisSubreport.getTimeInMillis()),
											ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_PERSONALNUMMER,
											"", true, theClientDto);

									Object[] oDaten = reportDto.getData();

									Object[] zeileSub = new Object[2];

									BigDecimal anwesenheit = new BigDecimal(0);
									BigDecimal laufzeit = new BigDecimal(0);
									for (int t = 0; t < oDaten.length; t++) {
										Object[] zeile = (Object[]) oDaten[t];

										BigDecimal laufzeitZeile = (BigDecimal) zeile[REPORT_MASCHINENERFOLG_LAUFZEIT];
										if (laufzeitZeile != null) {
											laufzeit = laufzeit.add(laufzeitZeile);
										}

										BigDecimal anwsenheitZeile = (BigDecimal) zeile[REPORT_MASCHINENERFOLG_ANWESENHEITSZEIT];
										if (anwsenheitZeile != null) {
											anwesenheit = anwesenheit.add(anwsenheitZeile);
										}

									}

									zeileSub[0] = anwesenheit;
									zeileSub[1] = laufzeit;

									hmSubreportMaschinenerfolg.put(keySubreport, zeileSub);

									hmAnwesenheitszeitMonat.put(keySubreport, anwesenheit);

								}

								oZeile[REPORT_PRODUKTIVITAETSTAGESSTATISTIK_ANWESENHEITSZEIT_MONATSBETRACHTUNG] = hmAnwesenheitszeitMonat
										.get(keySubreport);

							}

						} catch (RemoteException ex1) {
							throwEJBExceptionLPRespectOld(ex1);
						}

						oZeile[REPORT_PRODUKTIVITAETSTAGESSTATISTIK_LOS] = flrlossollarbeitsplan.getFlrlos().getC_nr();
						oZeile[REPORT_PRODUKTIVITAETSTAGESSTATISTIK_LOSGROESSE] = flrlossollarbeitsplan.getFlrlos()
								.getN_losgroesse();

						BigDecimal[] gutschlecht = getFertigungFac().getGutSchlechtInarbeit(
								flrlossollarbeitsplan.getI_id(), personalDto.getIId(), tBeginn,
								Helper.addiereTageZuTimestamp(new Timestamp(tBeginn.getTime()), 1), theClientDto);

						oZeile[REPORT_PRODUKTIVITAETSTAGESSTATISTIK_GUTSTUECK] = gutschlecht[0];
						oZeile[REPORT_PRODUKTIVITAETSTAGESSTATISTIK_SCHLECHTSTUECK] = gutschlecht[1];

						if (flrlossollarbeitsplan.getFlrlos().getFlrstueckliste() != null) {
							ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(
									flrlossollarbeitsplan.getFlrlos().getFlrstueckliste().getFlrartikel().getI_id(),
									theClientDto);

							oZeile[REPORT_PRODUKTIVITAETSTAGESSTATISTIK_STKLNR] = artikelDto.getCNr();
							oZeile[REPORT_PRODUKTIVITAETSTAGESSTATISTIK_STKLBEZ] = artikelDto.formatBezeichnung();
						}

						Session sessionLosklasse = factory.openSession();
						String queryLosklasse = "FROM FLRLoslosklasse l where l.los_i_id="
								+ flrlossollarbeitsplan.getFlrlos().getI_id();

						org.hibernate.Query loslosklasse = sessionLosklasse.createQuery(queryLosklasse);

						List resultListLosklasse = loslosklasse.list();

						Iterator resultListIteratorLosklasse = resultListLosklasse.iterator();

						String losklassen = "";
						while (resultListIteratorLosklasse.hasNext()) {
							com.lp.server.fertigung.fastlanereader.generated.FLRLoslosklasse lk = (com.lp.server.fertigung.fastlanereader.generated.FLRLoslosklasse) resultListIteratorLosklasse
									.next();

							losklassen += lk.getFlrlosklasse().getC_nr() + ",";

						}

						oZeile[REPORT_PRODUKTIVITAETSTAGESSTATISTIK_LOSKLASSEN] = losklassen;

						sessionLosklasse.close();

						if (flrlossollarbeitsplan.getFlrlos().getFlrauftrag() != null) {
							oZeile[REPORT_PRODUKTIVITAETSTAGESSTATISTIK_KUNDE] = HelperServer.formatNameAusFLRPartner(
									flrlossollarbeitsplan.getFlrlos().getFlrauftrag().getFlrkunde().getFlrpartner());
						} else if (flrlossollarbeitsplan.getFlrlos().getFlrkunde() != null) {
							oZeile[REPORT_PRODUKTIVITAETSTAGESSTATISTIK_KUNDE] = HelperServer.formatNameAusFLRPartner(
									flrlossollarbeitsplan.getFlrlos().getFlrkunde().getFlrpartner());
						}

						oZeile[REPORT_PRODUKTIVITAETSTAGESSTATISTIK_RUESTZEIT] = flrlossollarbeitsplan.getL_ruestzeit()
								.divide(new BigDecimal(1000 * 60), 4, BigDecimal.ROUND_HALF_EVEN);

						oZeile[REPORT_PRODUKTIVITAETSTAGESSTATISTIK_STUECKZEIT] = flrlossollarbeitsplan
								.getL_stueckzeit().divide(new BigDecimal(1000 * 60), 4, BigDecimal.ROUND_HALF_EVEN);

						ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(
								flrlossollarbeitsplan.getFlrartikel().getI_id(), theClientDto);

						oZeile[REPORT_PRODUKTIVITAETSTAGESSTATISTIK_TAETIGKEIT] = artikelDto.getCNr();
						oZeile[REPORT_PRODUKTIVITAETSTAGESSTATISTIK_TAETIGKEIT_BEZEICHNUNG] = artikelDto
								.formatBezeichnung();

						oZeile[REPORT_PRODUKTIVITAETSTAGESSTATISTIK_AGART] = flrlossollarbeitsplan.getAgart_c_nr();
						oZeile[REPORT_PRODUKTIVITAETSTAGESSTATISTIK_ARBEITSGANG] = flrlossollarbeitsplan
								.getI_arbeitsgangsnummer();
						oZeile[REPORT_PRODUKTIVITAETSTAGESSTATISTIK_UNTERARBEITSGANG] = flrlossollarbeitsplan
								.getI_unterarbeitsgang();
						oZeile[REPORT_PRODUKTIVITAETSTAGESSTATISTIK_FERTIG] = Helper
								.short2Boolean(flrlossollarbeitsplan.getB_fertig());
						oZeile[REPORT_PRODUKTIVITAETSTAGESSTATISTIK_FERTIGUNGSGRUPPE] = flrlossollarbeitsplan
								.getFlrlos().getFlrfertigungsgruppe().getC_bez();

						if (flrlossollarbeitsplan.getFlrmaschine() != null) {
							oZeile[REPORT_PRODUKTIVITAETSTAGESSTATISTIK_MASCHINE_BEZ] = flrlossollarbeitsplan
									.getFlrmaschine().getC_bez();
							oZeile[REPORT_PRODUKTIVITAETSTAGESSTATISTIK_MASCHINE_IDENTIFIKATION] = flrlossollarbeitsplan
									.getFlrmaschine().getC_identifikationsnr();
							oZeile[REPORT_PRODUKTIVITAETSTAGESSTATISTIK_MASCHINE_INVENTATNUMMER] = flrlossollarbeitsplan
									.getFlrmaschine().getC_inventarnummer();
						}

						// PJ17973 Ruesten mitrechnen

						Boolean bRuestenMitrechnen = false;

						String sQueryRusten = "SELECT zeitdaten from FLRZeitdatenLos zeitdaten WHERE zeitdaten.c_belegartnr ='"
								+ LocaleFac.BELEGART_LOS + "' AND zeitdaten.i_belegartpositionid="
								+ flrlossollarbeitsplan.getI_id() + " ORDER BY zeitdaten.t_zeit ASC";

						Session sessionQuery = factory.openSession();
						org.hibernate.Query queryRusten = sessionQuery.createQuery(sQueryRusten);
						queryRusten.setMaxResults(1);
						List resultListRuesten = queryRusten.list();

						Iterator resultListIteratorRuesten = resultListRuesten.iterator();
						if (resultListIteratorRuesten.hasNext()) {
							FLRZeitdatenLos zl = (FLRZeitdatenLos) resultListIteratorRuesten.next();

							if (zl.getPersonal_i_id().equals(personalDto.getIId())) {
								bRuestenMitrechnen = true;
							}

						}
						oZeile[REPORT_PRODUKTIVITAETSTAGESSTATISTIK_RUESTEN_MITRECHNEN] = bRuestenMitrechnen;

						sessionQuery.close();

						Calendar c = Calendar.getInstance();
						c.setTimeInMillis(tBeginn.getTime());

						String sortString = Helper.fitString2Length(person, 100, ' ') + c.get(Calendar.YEAR) + "-"
								+ Helper.fitString2LengthAlignRight(c.get(Calendar.MONTH) + "", 2, '0') + "-"
								+ Helper.fitString2LengthAlignRight(c.get(Calendar.DAY_OF_MONTH) + "", 2, '0') + " "
								+ Helper.fitString2Length(flrlossollarbeitsplan.getFlrlos().getC_nr(), 20, ' ')
								+ Helper.fitString2Length(flrlossollarbeitsplan.getI_arbeitsgangsnummer() + "", 10, ' ')
								+ Helper.fitString2Length(flrlossollarbeitsplan.getI_unterarbeitsgang() + "", 10, ' ');
						tmDaten.put(sortString, oZeile);

					}

				}
				tBeginn = Helper.addiereTageZuTimestamp(tBeginn, 1);
			}

			// Subreport
			if (bMonatsbetrachtung) {
				String[] fieldnames = new String[] { "F_MONAT", "F_ANWESENHEIT", "F_LAUFZEIT" };

				Iterator it = hmSubreportMaschinenerfolg.keySet().iterator();
				Object[][] dataSub = new Object[hmSubreportMaschinenerfolg.size()][fieldnames.length];
				int z = 0;
				while (it.hasNext()) {
					String monat = (String) it.next();

					Object[] temp = hmSubreportMaschinenerfolg.get(monat);

					dataSub[z][0] = monat;
					dataSub[z][1] = temp[0];
					dataSub[z][2] = temp[1];
					z++;
				}

				hmSubreportFuerAllePersonen.put(person, new LPDatenSubreport(dataSub, fieldnames));
			}

		}
		HashMap parameter = new HashMap<Object, Object>();

		parameter.put("P_SUBREPORTS_MASCHINENERFOLG", hmSubreportFuerAllePersonen);

		parameter.put("P_VON", tVon);
		parameter.put("P_BIS", Helper.addiereTageZuTimestamp(tBis, -1));
		parameter.put("P_NUR_ANWESENDE", new Boolean(bNurAnwesende));
		parameter.put("P_MONATSBETRACHTUNG", new Boolean(bMonatsbetrachtung));

		parameter.put("P_PERSONEN", sParameter_Personen);

		parameter.put("P_THEORETISCHE_ISTZEIT", new Boolean(bTheoretischeIstZeit));
		if (personalgruppeIId != null) {
			parameter.put("P_PERSONALGRUPPE",
					getPersonalFac().personalgruppeFindByPrimaryKey(personalgruppeIId).getCBez());
		}

		Object[][] returnArray = new Object[tmDaten.size()][REPORT_PRODUKTIVITAETSTAGESSTATISTIK_ANZAHL_SPALTEN];
		// data = (Object[][]) tmDaten.values().toArray(returnArray);
		setData((Object[][]) tmDaten.values().toArray(returnArray));

		sAktuellerReport = ZeiterfassungReportFac.REPORT_PRODUKTIVITAETSTAGESSTATISTIK;

		initJRDS(parameter, ZeiterfassungReportFac.REPORT_MODUL,
				ZeiterfassungReportFac.REPORT_PRODUKTIVITAETSTAGESSTATISTIK, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);
		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printMitarbeiteruebersicht(Integer personalIId, Integer iJahrVon, Integer iMonatVon,
			Integer iJahrBis, Integer iMonatBis, Integer iOption, Integer kostenstelleIIdAbteilung,
			Integer iOptionSortierung, boolean bPlusVersteckte, boolean bNurAnwesende, boolean bDetailinfos,
			boolean bAnerkannteZeiten, TheClientDto theClientDto) {

		sAktuellerReport = ZeiterfassungReportFac.REPORT_MITARBEITERUEBERSICHT;
		JasperPrintLP print = null;

		PersonalDto[] personalDtos = null;
		boolean bVonBisZeiterfassungOhneKommtGeht = false;
		Integer tagesartIId_Feiertag = null;
		Integer tagesartIId_Halbtag = null;
		try {

			tagesartIId_Feiertag = getZeiterfassungFac()
					.tagesartFindByCNr(ZeiterfassungFac.TAGESART_FEIERTAG, theClientDto).getIId();
			tagesartIId_Halbtag = getZeiterfassungFac()
					.tagesartFindByCNr(ZeiterfassungFac.TAGESART_HALBTAG, theClientDto).getIId();

			ParametermandantDto parameterVonBis = (ParametermandantDto) getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_VON_BIS_ERFASSUNG);
			boolean bVonBisErfassung = (Boolean) parameterVonBis.getCWertAsObject();

			boolean bKommtGeht = true;

			ParametermandantDto parameter = (ParametermandantDto) getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_VON_BIS_ERFASSUNG_KOMMT_GEHT_BUCHEN);

			bKommtGeht = (Boolean) parameter.getCWertAsObject();

			if (bVonBisErfassung == true && bKommtGeht == false) {
				bVonBisZeiterfassungOhneKommtGeht = true;
			}

			if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_SELEKTIERTE_PERSON) {

				if (personalIId == null) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
							new Exception("personalIId == null"));
				}

				personalDtos = new PersonalDto[1];

				personalDtos[0] = getPersonalFac().personalFindByPrimaryKey(personalIId, theClientDto);

			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_PERSONEN) {
				personalDtos = getPersonalFac().personalFindByMandantCNr(theClientDto.getMandant(), bPlusVersteckte);
			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_EINE_ABTEILUNG) {
				PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(personalIId, theClientDto);
				personalDtos = getPersonalFac().personalFindAllPersonenEinerAbteilung(kostenstelleIIdAbteilung,
						theClientDto.getMandant(), bPlusVersteckte);
			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_ARBEITER) {
				personalDtos = getPersonalFac().personalFindAllArbeiterEinesMandanten(theClientDto.getMandant(),
						bPlusVersteckte);
			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_ANGESTELLTE) {
				personalDtos = getPersonalFac().personalFindAllAngestellteEinesMandanten(theClientDto.getMandant(),
						bPlusVersteckte);
			} else {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("OPTION NICHT VERFUEGBAR"));
			}

			// PJ 16763
			if (iOptionSortierung != ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_PERSONALNUMMER) {
				for (int i = personalDtos.length - 1; i > 0; --i) {
					for (int j = 0; j < i; ++j) {
						String vergleich1 = "";
						String vergleich2 = "";

						String kostenstelle1 = "               ";
						String kostenstelle2 = "               ";

						if (iOptionSortierung == ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_KOSTENSTELLE_NAME_VORNAME
								|| iOptionSortierung == ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_ABTEILUNG_KOSTENSTELLE_NAME_VORNAME) {
							if (personalDtos[j].getKostenstelleIIdStamm() != null) {

								KostenstelleDto kstDto = getSystemFac()
										.kostenstelleFindByPrimaryKey(personalDtos[j].getKostenstelleIIdStamm());
								kostenstelle1 = Helper.fitString2Length(kstDto.getCNr(), 15, ' ');

							}
							if (personalDtos[j + 1].getKostenstelleIIdStamm() != null) {
								KostenstelleDto kstDto = getSystemFac()
										.kostenstelleFindByPrimaryKey(personalDtos[j + 1].getKostenstelleIIdStamm());
								kostenstelle2 = Helper.fitString2Length(kstDto.getCNr(), 15, ' ');

							}

							if (iOptionSortierung == ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_KOSTENSTELLE_NAME_VORNAME) {
								vergleich1 += kostenstelle1;
								vergleich2 += kostenstelle2;
							}

						}

						if (iOptionSortierung == ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_ABTEILUNG_NAME_VORNAME
								|| iOptionSortierung == ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_ABTEILUNG_KOSTENSTELLE_NAME_VORNAME) {

							String abteilung1 = "               ";
							String abteilung2 = "               ";

							if (personalDtos[j].getKostenstelleIIdAbteilung() != null) {

								KostenstelleDto kstDto = getSystemFac()
										.kostenstelleFindByPrimaryKey(personalDtos[j].getKostenstelleIIdAbteilung());
								abteilung1 = Helper.fitString2Length(kstDto.getCNr(), 15, ' ');

							} else {
								abteilung1 = "               ";
							}
							if (personalDtos[j + 1].getKostenstelleIIdAbteilung() != null) {
								KostenstelleDto kstDto = getSystemFac().kostenstelleFindByPrimaryKey(
										personalDtos[j + 1].getKostenstelleIIdAbteilung());
								abteilung2 = Helper.fitString2Length(kstDto.getCNr(), 15, ' ');

							} else {
								abteilung2 = "               ";
							}
							if (iOptionSortierung == ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_ABTEILUNG_KOSTENSTELLE_NAME_VORNAME) {
								vergleich1 = kostenstelle1 + vergleich1;
								vergleich2 = kostenstelle2 + vergleich2;
							}
							vergleich1 = abteilung1 + vergleich1;
							vergleich2 = abteilung2 + vergleich2;

						}

						PartnerDto p1Dto = getPartnerFac().partnerFindByPrimaryKey(personalDtos[j].getPartnerIId(),
								theClientDto);
						personalDtos[j].setPartnerDto(p1Dto);
						PartnerDto p2Dto = getPartnerFac().partnerFindByPrimaryKey(personalDtos[j + 1].getPartnerIId(),
								theClientDto);
						vergleich1 += Helper.fitString2Length(p1Dto.getCName1nachnamefirmazeile1(), 80, ' ');
						vergleich2 += Helper.fitString2Length(p2Dto.getCName1nachnamefirmazeile1(), 80, ' ');

						if (p1Dto.getCName2vornamefirmazeile2() != null) {
							vergleich1 += p1Dto.getCName2vornamefirmazeile2();
						}
						if (p2Dto.getCName2vornamefirmazeile2() != null) {
							vergleich2 += p2Dto.getCName2vornamefirmazeile2();
						}

						if (vergleich1.compareTo(vergleich2) > 0) {
							PersonalDto tauschDto = personalDtos[j];
							personalDtos[j] = personalDtos[j + 1];
							personalDtos[j + 1] = tauschDto;
						}

					}
				}
			}

		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		String[] kurzeWochentage = new DateFormatSymbols(theClientDto.getLocUi()).getShortWeekdays();

		Calendar cAktuell = Helper.getCalendarFirstDayOfMonth(iMonatVon, iJahrVon);
		Calendar cEnde = Helper.getCalendarLastDayOfMonth(iMonatBis, iJahrBis);

		while (cAktuell.before(cEnde)) {
			HashMap<String, Object> parameter = new HashMap<String, Object>();
			index = -1;

			ArrayList<Object[]> alDaten = new ArrayList<Object[]>();

			// data = new
			// Object[personalDtos.length][REPORT_MITARBEITERUEBERSICHT_ANZAHL_SPALTEN];
			setData(new Object[personalDtos.length][REPORT_MITARBEITERUEBERSICHT_ANZAHL_SPALTEN]);

			parameter.put("P_MONAT",
					new DateFormatSymbols(theClientDto.getLocUi()).getMonths()[cAktuell.get(Calendar.MONTH)] + " "
							+ cAktuell.get(Calendar.YEAR));

			Calendar cWochentag = Calendar.getInstance();
			cWochentag.set(Calendar.DAY_OF_MONTH, 1);
			cWochentag.set(Calendar.MONTH, cAktuell.get(Calendar.MONTH));
			cWochentag.set(Calendar.YEAR, cAktuell.get(Calendar.YEAR));

			parameter.put("P_DATE", cAktuell.getTime());

			// Ueberschriften + Tagesarten
			for (int iTag = 1; iTag < Helper.ermittleAnzahlTageEinesMonats(cAktuell.get(Calendar.YEAR),
					cAktuell.get(Calendar.MONTH)) + 1; iTag++) {
				cWochentag.set(Calendar.DAY_OF_MONTH, iTag);

				String sTag = iTag + "";
				if (iTag < 10) {
					sTag = 0 + sTag;
				}
				parameter.put("P_TAG" + sTag, kurzeWochentage[cWochentag.get(Calendar.DAY_OF_WEEK)] + "\n" + iTag);

				parameter.put("P_TAGESART" + sTag, Helper.holeTagbezeichnungLang(cWochentag.get(Calendar.DAY_OF_WEEK)));

				try {

					BetriebskalenderDto betriebskalenderDto = getPersonalFac().betriebskalenderFindByMandantCNrDDatum(
							Helper.cutTimestamp(new java.sql.Timestamp(cWochentag.getTimeInMillis())),
							theClientDto.getMandant(), theClientDto);

					if (betriebskalenderDto != null) {

						String tagesart = getZeiterfassungFac()
								.tagesartFindByPrimaryKey(betriebskalenderDto.getTagesartIId(), theClientDto).getCNr()
								.trim();

						parameter.put("P_TA" + sTag, tagesart);

						parameter.put("P_TAGESART" + sTag, tagesart);

					}
				} catch (RemoteException ex3) {
					throwEJBExceptionLPRespectOld(ex3);
				}

			}
			// Personen
			for (int i = 0; i < personalDtos.length; i++) {
				PersonalDto personalDto = personalDtos[i];

				personalDto.setPartnerDto(
						getPartnerFac().partnerFindByPrimaryKey(personalDto.getPartnerIId(), theClientDto));

				if (bNurAnwesende) {
					ArrayList<Integer> personalIIds_Eingetreten = getFinanzReportFac()
							.getAlleEingetretenenPersonenEinesMonats(cAktuell.getTime(), theClientDto);

					if (!personalIIds_Eingetreten.contains(personalDto.getIId())) {
						continue;
					}
				}

				Object[] oZeile = new Object[REPORT_MITARBEITERUEBERSICHT_ANZAHL_SPALTEN];

				oZeile[REPORT_MITARBEITERUEBERSICHT_NAME] = personalDto.getPartnerDto().formatFixTitelName1Name2();

				if (personalDto.getKostenstelleIIdStamm() != null) {

					KostenstelleDto kstDto = getSystemFac()
							.kostenstelleFindByPrimaryKey(personalDto.getKostenstelleIIdStamm());

					oZeile[REPORT_MITARBEITERUEBERSICHT_KOSTENSTELLE] = kstDto.getCNr();
				}
				if (personalDto.getKostenstelleIIdAbteilung() != null) {

					KostenstelleDto kstDto = getSystemFac()
							.kostenstelleFindByPrimaryKey(personalDto.getKostenstelleIIdAbteilung());

					oZeile[REPORT_MITARBEITERUEBERSICHT_ABTEILUNG] = kstDto.getCNr();
				}

				// PJ18287
				oZeile[REPORT_MITARBEITERUEBERSICHT_KALK_JAHRESISTSTUNDEN] = getZeiterfassungFac()
						.berechneKalkJahresIstStunden(personalDto.getIId(), cAktuell.get(Calendar.MONTH),
								cAktuell.get(Calendar.YEAR), theClientDto);

				double iSummeIst = 0;
				// Je Tag eines Monats
				for (int iTag = 1; iTag < Helper.ermittleAnzahlTageEinesMonats(cAktuell.get(Calendar.YEAR),
						cAktuell.get(Calendar.MONTH)) + 1; iTag++) {

					Session session = FLRSessionFactory.getFactory().openSession();

					org.hibernate.Criteria crit = session.createCriteria(FLRSonderzeiten.class);
					crit.add(Restrictions.eq(ZeiterfassungFac.FLR_SONDERZEITEN_PERSONAL_I_ID, personalDto.getIId()));
					// Heute
					Calendar cHeuteVon = Calendar.getInstance();
					cHeuteVon.set(Calendar.DAY_OF_MONTH, iTag);
					cHeuteVon.set(Calendar.MONTH, cAktuell.get(Calendar.MONTH));
					cHeuteVon.set(Calendar.YEAR, cAktuell.get(Calendar.YEAR));
					cHeuteVon.set(Calendar.DAY_OF_MONTH, iTag);

					cHeuteVon.set(Calendar.HOUR_OF_DAY, 0);
					cHeuteVon.set(Calendar.MINUTE, 0);
					cHeuteVon.set(Calendar.SECOND, 0);
					cHeuteVon.set(Calendar.MILLISECOND, 0);

					crit.add(Restrictions.eq(ZeiterfassungFac.FLR_SONDERZEITEN_D_DATUM,
							new Date(cHeuteVon.getTimeInMillis())));
					List<?> resultList = crit.list();
					Iterator<?> resultListIterator = resultList.iterator();
					String taetigkeiten = "";
					String bezTaetigkeit = "";

					String infoDsgvoKonform = null;

					if (resultListIterator.hasNext()) {
						FLRSonderzeiten flrzeitdaten = (FLRSonderzeiten) resultListIterator.next();
						taetigkeiten = flrzeitdaten.getFlrtaetigkeit().getC_nr().substring(0, 2);

						infoDsgvoKonform = "ABW";

						Iterator it = flrzeitdaten.getFlrtaetigkeit().getTaetigkeitsprset().iterator();

						while (it.hasNext()) {
							FLRTaetigkeitspr spr = (FLRTaetigkeitspr) it.next();
							if (spr.getLocale().getC_nr().equals(theClientDto.getLocUiAsString())) {
								bezTaetigkeit = spr.getC_bez();
								break;
							}

						}

						// lt. WH: Wenn Urlaubsantrag, dann UA
						if (flrzeitdaten.getFlrtaetigkeit().getC_nr()
								.equals(ZeiterfassungFac.TAETIGKEIT_URLAUBSANTRAG)) {
							taetigkeiten = "UA";
						}

					}
					try {
						Double d = null;
						if (bVonBisZeiterfassungOhneKommtGeht == true) {
							VonBisErfassungTagesdatenDto vbDto = getZeiterfassungFac()
									.berechneTagesArbeitszeitVonBisZeiterfassungOhneKommtGeht(personalDto.getIId(),
											new java.sql.Date(cHeuteVon.getTimeInMillis()), theClientDto);
							d = vbDto.getdIst();
						} else {
							d = getZeiterfassungFac().berechneTagesArbeitszeit(personalDto.getIId(),
									new java.sql.Date(cHeuteVon.getTimeInMillis()), !bAnerkannteZeiten, theClientDto);
						}

						if (d.doubleValue() > 0) {

							// PJ 18278 Nur anzeigen, wenn keine Sonderzeiten
							if (resultList.size() == 0) {

								taetigkeiten = Helper.formatZahl(d, 1, theClientDto.getLocUi());
							}
							iSummeIst += d.doubleValue();

							infoDsgvoKonform = "ANW";

						}
					} catch (javax.ejb.EJBException ex1) {
						taetigkeiten = "ERR";
					} catch (EJBExceptionLP ex1) {
						taetigkeiten = "ERR";
					} catch (RemoteException ex2) {
						throwEJBExceptionLPRespectOld(ex2);
					}

					session.close();
					if (bDetailinfos == true) {

						oZeile[iTag] = taetigkeiten;

						// 33 = Diff zwischen TAG01 und BEZ01
						oZeile[iTag + 33] = bezTaetigkeit;
					} else {

						if (infoDsgvoKonform == null) {

							Integer tagesartIId = getZeiterfassungFac().istFeiertagOderSamstagSonntag(
									new java.sql.Timestamp(cHeuteVon.getTimeInMillis()), personalDto.getIId(),
									theClientDto);

							if (tagesartIId == null) {

								BigDecimal bdSoll = getZeiterfassungFac().getSollzeitEinerPersonUndEinesTages(
										personalDto, tagesartIId_Feiertag, tagesartIId_Halbtag,
										new java.sql.Timestamp(cHeuteVon.getTimeInMillis()), theClientDto);

								if (bdSoll != null && bdSoll.doubleValue() > 0) {

								} else {
									infoDsgvoKonform = "KSZ";
								}
							}
						}

						oZeile[iTag] = infoDsgvoKonform;
					}

				}
				oZeile[REPORT_MITARBEITERUEBERSICHT_ANWESENHEITSUMME] = new BigDecimal(iSummeIst);

				alDaten.add(oZeile);

			}
			cAktuell.set(Calendar.DAY_OF_MONTH, 1);
			cAktuell.set(Calendar.MONTH, cAktuell.get(Calendar.MONTH) + 1);

			parameter.put("P_SORTIERUNG",
					getZeiterfassungFac().getParameterSortierungZeitauswertungen(iOptionSortierung, theClientDto));

			parameter.put("P_NUR_ANWESENDE", new Boolean(bNurAnwesende));

			//SP9626
			if (kostenstelleIIdAbteilung != null) {
				parameter.put("P_KOSTENSTELLE_ABTEILUNG", getSystemFac()
						.kostenstelleFindByPrimaryKey(kostenstelleIIdAbteilung).formatKostenstellenbezeichnung());
			}
			
			parameter.put("P_DETAILINFOS", new Boolean(bDetailinfos));

			parameter.put("P_ANERKANNTE_ZEITEN", new Boolean(bAnerkannteZeiten));

			Object[][] temp = new Object[alDaten.size()][REPORT_MITARBEITERUEBERSICHT_ANZAHL_SPALTEN];
			setData((Object[][]) alDaten.toArray(temp));

			initJRDS(parameter, ZeiterfassungReportFac.REPORT_MODUL,
					ZeiterfassungReportFac.REPORT_MITARBEITERUEBERSICHT, theClientDto.getMandant(),
					theClientDto.getLocUi(), theClientDto);

			if (print != null) {
				print = Helper.addReport2Report(print, getReportPrint().getPrint());
			} else {
				print = getReportPrint();
			}
		}
		return print;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public MaschinenerfolgReportDto printMaschinenerfolg(Integer personalIId, Integer personalgruppeIId,
			java.sql.Timestamp tVon, java.sql.Timestamp tBis, Integer iOptionSortierung, String sortierung,
			boolean bMonatsbetrachtung, TheClientDto theClientDto) {

		sAktuellerReport = ZeiterfassungReportFac.REPORT_MASCHINENERFOLG;

		PersonalDto[] personalDtos = null;

		try {

			if (personalIId != null) {
				personalDtos = new PersonalDto[1];
				personalDtos[0] = getPersonalFac().personalFindByPrimaryKey(personalIId, theClientDto);
			} else {

				if (personalgruppeIId != null) {
					personalDtos = getPersonalFac().personalFindByPersonalgruppeIdMandantCNr(personalgruppeIId,
							theClientDto.getMandant(), false);
				} else {
					personalDtos = getPersonalFac().personalFindByMandantCNr(theClientDto.getMandant(), false);
				}

			}
		} catch (RemoteException e2) {
			throwEJBExceptionLPRespectOld(e2);
		}

		if (iOptionSortierung != ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_PERSONALNUMMER) {
			for (int i = personalDtos.length - 1; i > 0; --i) {

				if (personalDtos[i].getKostenstelleIIdAbteilung() != null) {
					personalDtos[i].setKostenstelleDto_Abteilung(
							getSystemFac().kostenstelleFindByPrimaryKey(personalDtos[i].getKostenstelleIIdAbteilung()));
				}
				if (personalDtos[i].getKostenstelleIIdStamm() != null) {
					personalDtos[i].setKostenstelleDto_Stamm(
							getSystemFac().kostenstelleFindByPrimaryKey(personalDtos[i].getKostenstelleIIdStamm()));
				}

				for (int j = 0; j < i; ++j) {
					String vergleich1 = "";
					String vergleich2 = "";

					String kostenstelle1 = "               ";
					String kostenstelle2 = "               ";

					if (iOptionSortierung == ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_KOSTENSTELLE_NAME_VORNAME
							|| iOptionSortierung == ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_ABTEILUNG_KOSTENSTELLE_NAME_VORNAME) {
						if (personalDtos[j].getKostenstelleIIdStamm() != null) {

							kostenstelle1 = Helper.fitString2Length(personalDtos[j].getKostenstelleDto_Stamm().getCNr(),
									15, ' ');

						}
						if (personalDtos[j + 1].getKostenstelleIIdStamm() != null) {
							kostenstelle2 = Helper
									.fitString2Length(personalDtos[j + 1].getKostenstelleDto_Stamm().getCNr(), 15, ' ');

						}

						if (iOptionSortierung == ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_KOSTENSTELLE_NAME_VORNAME) {
							vergleich1 += kostenstelle1;
							vergleich2 += kostenstelle2;
						}

					}

					if (iOptionSortierung == ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_ABTEILUNG_NAME_VORNAME
							|| iOptionSortierung == ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_ABTEILUNG_KOSTENSTELLE_NAME_VORNAME) {

						String abteilung1 = "               ";
						String abteilung2 = "               ";

						if (personalDtos[j].getKostenstelleIIdAbteilung() != null) {

							KostenstelleDto kstDto = getSystemFac()
									.kostenstelleFindByPrimaryKey(personalDtos[j].getKostenstelleIIdAbteilung());
							abteilung1 = Helper.fitString2Length(kstDto.getCNr(), 15, ' ');

						} else {
							abteilung1 = "               ";
						}
						if (personalDtos[j + 1].getKostenstelleIIdAbteilung() != null) {
							KostenstelleDto kstDto = getSystemFac()
									.kostenstelleFindByPrimaryKey(personalDtos[j + 1].getKostenstelleIIdAbteilung());
							abteilung2 = Helper.fitString2Length(kstDto.getCNr(), 15, ' ');

						} else {
							abteilung2 = "               ";
						}
						if (iOptionSortierung == ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_ABTEILUNG_KOSTENSTELLE_NAME_VORNAME) {
							vergleich1 = kostenstelle1 + vergleich1;
							vergleich2 = kostenstelle2 + vergleich2;
						}
						vergleich1 = abteilung1 + vergleich1;
						vergleich2 = abteilung2 + vergleich2;

					}

					PartnerDto p1Dto = getPartnerFac().partnerFindByPrimaryKey(personalDtos[j].getPartnerIId(),
							theClientDto);
					personalDtos[j].setPartnerDto(p1Dto);
					PartnerDto p2Dto = getPartnerFac().partnerFindByPrimaryKey(personalDtos[j + 1].getPartnerIId(),
							theClientDto);
					vergleich1 += Helper.fitString2Length(p1Dto.getCName1nachnamefirmazeile1(), 80, ' ');
					vergleich2 += Helper.fitString2Length(p2Dto.getCName1nachnamefirmazeile1(), 80, ' ');

					if (p1Dto.getCName2vornamefirmazeile2() != null) {
						vergleich1 += p1Dto.getCName2vornamefirmazeile2();
					}
					if (p2Dto.getCName2vornamefirmazeile2() != null) {
						vergleich2 += p2Dto.getCName2vornamefirmazeile2();
					}

					if (vergleich1.compareTo(vergleich2) > 0) {
						PersonalDto tauschDto = personalDtos[j];
						personalDtos[j] = personalDtos[j + 1];
						personalDtos[j + 1] = tauschDto;
					}

				}
			}
		}

		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("P_VON", tVon);
		parameter.put("P_BIS", Helper.addiereTageZuTimestamp(tBis, -1));
		parameter.put("P_MONATSBETRACHTUNG", new Boolean(bMonatsbetrachtung));
		parameter.put("P_SORTIERUNG", sortierung);
		if (personalgruppeIId != null) {
			parameter.put("P_PERSONALGRUPPE",
					getPersonalFac().personalgruppeFindByPrimaryKey(personalgruppeIId).getCBez());
		}

		ArrayList alDaten = new ArrayList();

		for (int j = 0; j < personalDtos.length; j++) {

			// Daten aufloesen
			PartnerDto p1Dto = getPartnerFac().partnerFindByPrimaryKey(personalDtos[j].getPartnerIId(), theClientDto);

			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = factory.openSession();

			if (personalDtos[j].getKostenstelleIIdAbteilung() != null) {
				personalDtos[j].setKostenstelleDto_Abteilung(
						getSystemFac().kostenstelleFindByPrimaryKey(personalDtos[j].getKostenstelleIIdAbteilung()));
			}
			if (personalDtos[j].getKostenstelleIIdStamm() != null) {
				personalDtos[j].setKostenstelleDto_Stamm(
						getSystemFac().kostenstelleFindByPrimaryKey(personalDtos[j].getKostenstelleIIdStamm()));
			}

			String sQuery = "SELECT m FROM FLRMaschinenzeitdaten m WHERE m.t_von>='"
					+ Helper.formatDateWithSlashes(new java.sql.Date(tVon.getTime())) + "' AND m.t_von<'"
					+ Helper.formatDateWithSlashes(new java.sql.Date(tBis.getTime())) + "'";

			sQuery += " AND m.flrpersonal_gestartet.i_id=" + personalDtos[j].getIId();

			sQuery += " AND m.t_bis IS NOT NULL ORDER BY m.t_von ASC AND m.flrmaschine.c_inventarnummer ASC";

			Query query = session.createQuery(sQuery);
			List<?> resultList = query.list();

			Iterator<?> resultListIterator = resultList.iterator();

			TreeMap<Timestamp, TreeMap<String, Object[]>> tmDatum = new TreeMap<Timestamp, TreeMap<String, Object[]>>();

			while (resultListIterator.hasNext()) {
				FLRMaschinenzeitdaten flrMaschinenzeitdaten = (FLRMaschinenzeitdaten) resultListIterator.next();

				Timestamp tDatum = Helper.cutTimestamp(new Timestamp(flrMaschinenzeitdaten.getT_von().getTime()));

				TreeMap<String, Object[]> tmMaschinen = null;

				if (tmDatum.containsKey(tDatum)) {
					tmMaschinen = tmDatum.get(tDatum);
				} else {
					tmMaschinen = new TreeMap<String, Object[]>();
				}

				Object[] oZeile = new Object[REPORT_MASCHINENERFOLG_ANZAHL_SPALTEN];

				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(tDatum.getTime());

				long laufzeit = flrMaschinenzeitdaten.getT_bis().getTime() - flrMaschinenzeitdaten.getT_von().getTime();
				BigDecimal bdLaufzeit = new BigDecimal(laufzeit / 3600000D);
				if (tmMaschinen.containsKey(flrMaschinenzeitdaten.getFlrmaschine().getC_inventarnummer())) {
					oZeile = tmMaschinen.get(flrMaschinenzeitdaten.getFlrmaschine().getC_inventarnummer());
					oZeile[REPORT_MASCHINENERFOLG_LAUFZEIT] = ((BigDecimal) oZeile[REPORT_MASCHINENERFOLG_LAUFZEIT])
							.add(bdLaufzeit);
				} else {
					oZeile = new Object[REPORT_MASCHINENERFOLG_ANZAHL_SPALTEN];

					try {
						Double dGesamt = getZeiterfassungFac().berechneTagesArbeitszeit(personalDtos[j].getIId(),
								new java.sql.Date(tDatum.getTime()), theClientDto);
						oZeile[REPORT_MASCHINENERFOLG_ANWESENHEITSZEIT] = new BigDecimal(dGesamt);
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
					oZeile[REPORT_MASCHINENERFOLG_JAHRMONAT] = c.get(Calendar.YEAR) + " " + (c.get(Calendar.MONTH) + 1);
					oZeile[REPORT_MASCHINENERFOLG_SORT_PERSONAL] = new Integer(j);
					oZeile[REPORT_MASCHINENERFOLG_PERSONALNUMMER] = personalDtos[j].getCPersonalnr();
					oZeile[REPORT_MASCHINENERFOLG_NACHNAME] = p1Dto.getCName1nachnamefirmazeile1();
					oZeile[REPORT_MASCHINENERFOLG_VORNAME] = p1Dto.getCName2vornamefirmazeile2();

					if (personalDtos[j].getKostenstelleIIdAbteilung() != null) {
						oZeile[REPORT_MASCHINENERFOLG_ABTEILUNG] = personalDtos[j].getKostenstelleDto_Abteilung()
								.getCNr();
					}
					if (personalDtos[j].getKostenstelleIIdStamm() != null) {
						oZeile[REPORT_MASCHINENERFOLG_KOSTENSTELLE] = personalDtos[j].getKostenstelleDto_Stamm()
								.getCNr();
					}

					oZeile[REPORT_MASCHINENERFOLG_DATUM] = tDatum;

					oZeile[REPORT_MASCHINENERFOLG_INVENTARNUMMER] = flrMaschinenzeitdaten.getFlrmaschine()
							.getC_inventarnummer();

					oZeile[REPORT_MASCHINENERFOLG_BEZEICHNUNG] = flrMaschinenzeitdaten.getFlrmaschine().getC_bez();
					oZeile[REPORT_MASCHINENERFOLG_IDENTIFIKATIONSNUMMER] = flrMaschinenzeitdaten.getFlrmaschine()
							.getC_identifikationsnr();
					oZeile[REPORT_MASCHINENERFOLG_SERIENNUMMER] = flrMaschinenzeitdaten.getFlrmaschine()
							.getC_seriennummer();

					oZeile[REPORT_MASCHINENERFOLG_MASCHINENGRUPPE] = flrMaschinenzeitdaten.getFlrmaschine()
							.getFlrmaschinengruppe().getC_bez();

					oZeile[REPORT_MASCHINENERFOLG_LAUFZEIT] = bdLaufzeit;

				}
				tmMaschinen.put(flrMaschinenzeitdaten.getFlrmaschine().getC_inventarnummer(), oZeile);

				tmDatum.put(tDatum, tmMaschinen);

			}
			session.close();

			Calendar c = Calendar.getInstance();
			c.setTime(tVon);

			while (c.getTime().before(tBis)) {

				Timestamp tAktuell = Helper.cutTimestamp(new Timestamp(c.getTimeInMillis()));

				if (tmDatum.containsKey(tAktuell)) {

					TreeMap<String, Object[]> tmMaschinen = tmDatum.get(tAktuell);

					Iterator<String> it = tmMaschinen.keySet().iterator();
					boolean bNurBeimErsten = true;
					while (it.hasNext()) {

						String key = it.next();

						Object[] zeile = tmMaschinen.get(key);

						if (bNurBeimErsten == true) {
							bNurBeimErsten = false;

						} else {
							zeile[REPORT_MASCHINENERFOLG_ANWESENHEITSZEIT] = null;
						}

						alDaten.add(zeile);

					}

				} else {
					Object[] oZeile = null;
					oZeile = new Object[REPORT_MASCHINENERFOLG_ANZAHL_SPALTEN];

					oZeile[REPORT_MASCHINENERFOLG_PERSONALNUMMER] = personalDtos[j].getCPersonalnr();
					oZeile[REPORT_MASCHINENERFOLG_NACHNAME] = p1Dto.getCName1nachnamefirmazeile1();
					oZeile[REPORT_MASCHINENERFOLG_VORNAME] = p1Dto.getCName2vornamefirmazeile2();
					oZeile[REPORT_MASCHINENERFOLG_DATUM] = tAktuell;

					try {
						Double dGesamt = getZeiterfassungFac().berechneTagesArbeitszeit(personalDtos[j].getIId(),
								new java.sql.Date(tAktuell.getTime()), theClientDto);
						oZeile[REPORT_MASCHINENERFOLG_ANWESENHEITSZEIT] = new BigDecimal(dGesamt);
					} catch (EJBExceptionLP e) {
						ArrayList<Object> al = new ArrayList<Object>();
						al.add(personalDtos[j].getIId());
						al.add(tAktuell);
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_IN_ZEITDATEN, al, new Exception());
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
					oZeile[REPORT_MASCHINENERFOLG_JAHRMONAT] = c.get(Calendar.YEAR) + " " + (c.get(Calendar.MONTH) + 1);
					oZeile[REPORT_MASCHINENERFOLG_SORT_PERSONAL] = new Integer(j);
					alDaten.add(oZeile);

				}

				c.add(Calendar.DAY_OF_MONTH, 1);

			}

		}

		// Verdichten
		if (bMonatsbetrachtung == true) {
			TreeMap tm = new TreeMap();
			for (int i = 0; i < alDaten.size(); i++) {
				Object[] zeileAktuell = (Object[]) alDaten.get(i);
				Integer iSort = (Integer) zeileAktuell[REPORT_MASCHINENERFOLG_SORT_PERSONAL];

				Timestamp tAktuell = (Timestamp) zeileAktuell[REPORT_MASCHINENERFOLG_DATUM];

				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(tAktuell.getTime());
				c.set(Calendar.DAY_OF_MONTH, 1);

				zeileAktuell[REPORT_MASCHINENERFOLG_DATUM] = new Timestamp(c.getTimeInMillis());

				String dSort = c.get(Calendar.YEAR)
						+ Helper.fitString2LengthAlignRight(c.get(Calendar.MONTH) + "", 2, '0');

				String key = Helper.fitString2LengthAlignRight(iSort + "", 15, ' ') + dSort
						+ zeileAktuell[REPORT_MASCHINENERFOLG_INVENTARNUMMER];

				if (tm.containsKey(key)) {
					Object[] zeileVorhanden = (Object[]) tm.get(key);

					BigDecimal anwesenheitszeitVorhanden = (BigDecimal) zeileVorhanden[REPORT_MASCHINENERFOLG_ANWESENHEITSZEIT];
					BigDecimal laufzeitVorhanden = (BigDecimal) zeileVorhanden[REPORT_MASCHINENERFOLG_LAUFZEIT];
					if (anwesenheitszeitVorhanden == null) {
						anwesenheitszeitVorhanden = new BigDecimal(0);
					}
					if (laufzeitVorhanden == null) {
						laufzeitVorhanden = new BigDecimal(0);
					}
					BigDecimal anwesenheitszeitAktuell = (BigDecimal) zeileAktuell[REPORT_MASCHINENERFOLG_ANWESENHEITSZEIT];
					BigDecimal laufzeitAktuell = (BigDecimal) zeileAktuell[REPORT_MASCHINENERFOLG_LAUFZEIT];
					if (anwesenheitszeitAktuell == null) {
						anwesenheitszeitAktuell = new BigDecimal(0);
					}
					if (laufzeitAktuell == null) {
						laufzeitAktuell = new BigDecimal(0);
					}

					zeileVorhanden[REPORT_MASCHINENERFOLG_ANWESENHEITSZEIT] = anwesenheitszeitVorhanden
							.add(anwesenheitszeitAktuell);
					zeileVorhanden[REPORT_MASCHINENERFOLG_LAUFZEIT] = laufzeitVorhanden.add(laufzeitAktuell);

					tm.put(key, zeileVorhanden);

				} else {
					tm.put(key, zeileAktuell);
				}

			}

			alDaten = new ArrayList();
			Iterator it = tm.keySet().iterator();

			while (it.hasNext()) {
				alDaten.add(tm.get(it.next()));
			}
		}

		data = new Object[alDaten.size()][REPORT_MASCHINENERFOLG_ANZAHL_SPALTEN];
		// data = (Object[][]) alDaten.toArray(data);
		setData((Object[][]) alDaten.toArray(data));

		MaschinenerfolgReportDto mDto = new MaschinenerfolgReportDto();
		mDto.setData(data);
		initJRDS(parameter, ZeiterfassungReportFac.REPORT_MODUL, ZeiterfassungReportFac.REPORT_MASCHINENERFOLG,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		mDto.setJasperPrintLP(getReportPrint());
		return mDto;
	}

	public JasperPrintLP printFahrtenbuch(Integer personalIId, java.sql.Timestamp tVon, java.sql.Timestamp tBis,
			Integer iOption, Integer kostenstelleIIdAbteilung, boolean bPlusVersteckte, boolean bNurAnwesende,
			TheClientDto theClientDto) {
		if (tVon == null || tBis == null || personalIId.equals(iOption)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("tVon == null || tBis == null || personalIId == iOption"));
		}
		tVon = Helper.cutTimestamp(tVon);
		tBis = Helper.cutTimestamp(tBis);

		// Kurze Tagebezeichnungen holen
		String[] kurzeWochentage = new DateFormatSymbols(theClientDto.getLocUi()).getShortWeekdays();

		if (tVon.after(tBis)) {
			java.sql.Timestamp h = tVon;
			tVon = tBis;
			tBis = h;
		}
		PersonalDto[] personalDtos = null;
		JasperPrintLP print = null;
		try {
			if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_SELEKTIERTE_PERSON) {

				if (personalIId == null) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
							new Exception("personalIId == null"));
				}

				personalDtos = new PersonalDto[1];

				personalDtos[0] = getPersonalFac().personalFindByPrimaryKey(personalIId, theClientDto);

			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_EINE_ABTEILUNG) {
				PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(personalIId, theClientDto);
				personalDtos = getPersonalFac().personalFindAllPersonenEinerAbteilung(kostenstelleIIdAbteilung,
						theClientDto.getMandant(), bPlusVersteckte);
			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_PERSONEN) {
				personalDtos = getPersonalFac().personalFindByMandantCNr(theClientDto.getMandant(), bPlusVersteckte);
			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_ARBEITER) {
				personalDtos = getPersonalFac().personalFindAllArbeiterEinesMandanten(theClientDto.getMandant(),
						bPlusVersteckte);
			} else if (iOption.intValue() == ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_ANGESTELLTE) {
				personalDtos = getPersonalFac().personalFindAllAngestellteEinesMandanten(theClientDto.getMandant(),
						bPlusVersteckte);
			} else {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("OPTION NICHT VERFUEGBAR"));
			}

			if (bNurAnwesende) {
				personalDtos = getZeiterfassungFac().entferneNichtAnwesendePersonen(tVon, tBis, personalDtos,
						theClientDto);
			}

		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		for (int i = 0; i < personalDtos.length; i++) {
			PersonalDto personalDto = personalDtos[i];

			personalDto
					.setPartnerDto(getPartnerFac().partnerFindByPrimaryKey(personalDto.getPartnerIId(), theClientDto));

			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = factory.openSession();

			org.hibernate.Criteria crit = session.createCriteria(FLRReise.class);
			crit.add(Restrictions.eq(ZeiterfassungFac.FLR_REISE_PERSONAL_I_ID, personalDto.getIId()));
			crit.add(Restrictions.ge(ZeiterfassungFac.FLR_REISE_T_ZEIT, tVon));
			crit.add(Restrictions.lt(ZeiterfassungFac.FLR_REISE_T_ZEIT, tBis));
			crit.addOrder(Order.asc(ZeiterfassungFac.FLR_REISE_T_ZEIT));

			List<?> resultList = crit.list();

			Iterator<?> resultListIterator = resultList.iterator();
			// Erster Eintrag ist beginn
			FLRReise reiseBeginn = null;
			while (resultListIterator.hasNext()) {
				reiseBeginn = (FLRReise) resultListIterator.next();

				if (Helper.short2boolean(reiseBeginn.getB_beginn()) == true) {
					break;
				}

			}

			// Ist vor dem Beginn auch ein Beginn, dann ist der Beginn das T_VON

			Session sessionVorher = factory.openSession();

			org.hibernate.Criteria critVorher = sessionVorher.createCriteria(FLRReise.class);
			critVorher.add(Restrictions.eq(ZeiterfassungFac.FLR_REISE_PERSONAL_I_ID, personalDto.getIId()));

			critVorher.add(Restrictions.lt(ZeiterfassungFac.FLR_REISE_T_ZEIT, tVon));
			critVorher.addOrder(Order.desc(ZeiterfassungFac.FLR_REISE_T_ZEIT));
			critVorher.setMaxResults(1);

			List<?> resultListVorher = critVorher.list();

			boolean bFortsetzungAusVormonat = false;
			Integer kmBeginnAusVormonat = null;
			if (resultListVorher.iterator().hasNext()) {
				FLRReise flrReise = (FLRReise) resultListVorher.iterator().next();
				if (Helper.short2boolean(flrReise.getB_beginn()) == true) {
					bFortsetzungAusVormonat = true;
					kmBeginnAusVormonat = flrReise.getI_kmbeginn();
				}
			}

			ArrayList<Object[]> alDaten = new ArrayList<Object[]>();

			while (resultListIterator.hasNext()) {

				FLRReise flrReise = (FLRReise) resultListIterator.next();

				if (Helper.short2boolean(flrReise.getB_beginn()) == false || resultListIterator.hasNext() == false) {
					if (Helper.short2boolean(reiseBeginn.getB_beginn()) == true) {
						Calendar cal = Calendar.getInstance();
						cal.setTimeInMillis(reiseBeginn.getT_zeit().getTime());

						Object[] oZeile = new Object[REPORT_FAHRTENBUCH_ANZAHL_SPALTEN];
						oZeile[REPORT_FAHRTENBUCH_REISE_WIRD_FORTGESETZT] = Boolean.FALSE;
						oZeile[REPORT_FAHRTENBUCH_REISE_IST_FORTGESETZT] = Boolean.FALSE;

						oZeile[REPORT_FAHRTENBUCH_FEHLER_IN_KM] = Boolean.FALSE;

						oZeile[REPORT_FAHRTENBUCH_TAG] = kurzeWochentage[cal.get(Calendar.DAY_OF_WEEK)];
						oZeile[REPORT_FAHRTENBUCH_KMBEGINN] = reiseBeginn.getI_kmbeginn();
						oZeile[REPORT_FAHRTENBUCH_KMENDE] = flrReise.getI_kmende();
						oZeile[REPORT_FAHRTENBUCH_MITFAHRER] = reiseBeginn.getI_mitfahrer();
						oZeile[REPORT_FAHRTENBUCH_KOMMENTAR] = reiseBeginn.getC_kommentar();

						// SP7315 Wenn es nur Beginn- oder Ende-Kilometer gibt, dann Fehler

						if (reiseBeginn.getI_kmbeginn() != null || flrReise.getI_kmende() != null) {
							if (!(reiseBeginn.getI_kmbeginn() != null && flrReise.getI_kmende() != null)) {
								oZeile[REPORT_FAHRTENBUCH_FEHLER_IN_KM] = Boolean.TRUE;
							}
						}
						oZeile[REPORT_FAHRTENBUCH_DATUM] = reiseBeginn.getT_zeit();

						oZeile[REPORT_FAHRTENBUCH_DATUM_ENDE] = flrReise.getT_zeit();

						if (reiseBeginn.getI_kmbeginn() != null && flrReise.getI_kmende() != null) {
							oZeile[REPORT_FAHRTENBUCH_STRECKE] = flrReise.getI_kmende() - reiseBeginn.getI_kmbeginn();
						}

						// SP8521
						if (Helper.short2boolean(flrReise.getB_beginn()) == true
								&& resultListIterator.hasNext() == false) {
							oZeile[REPORT_FAHRTENBUCH_DATUM_ENDE] = new Timestamp(tBis.getTime() - 1000);
							oZeile[REPORT_FAHRTENBUCH_REISE_WIRD_FORTGESETZT] = Boolean.TRUE;
							oZeile[REPORT_FAHRTENBUCH_KMENDE] = flrReise.getI_kmbeginn();
							if (flrReise.getI_kmbeginn() != null && reiseBeginn.getI_kmbeginn() != null)
								oZeile[REPORT_FAHRTENBUCH_STRECKE] = flrReise.getI_kmbeginn()
										- reiseBeginn.getI_kmbeginn();

						}

						if (bFortsetzungAusVormonat == true) {
							oZeile[REPORT_FAHRTENBUCH_REISE_IST_FORTGESETZT] = Boolean.TRUE;
							oZeile[REPORT_FAHRTENBUCH_DATUM] = tVon;
							oZeile[REPORT_FAHRTENBUCH_KMBEGINN] = kmBeginnAusVormonat;
							if (kmBeginnAusVormonat != null && flrReise.getI_kmende() != null)
								oZeile[REPORT_FAHRTENBUCH_STRECKE] = flrReise.getI_kmende() - kmBeginnAusVormonat;
							bFortsetzungAusVormonat = false;
						}

						if (reiseBeginn.getFlrpartner() != null) {
							com.lp.server.partner.service.PartnerDto partnerDto = getPartnerFac()
									.partnerFindByPrimaryKey(reiseBeginn.getFlrpartner().getI_id(), theClientDto);
							oZeile[REPORT_FAHRTENBUCH_KUNDE] = partnerDto.formatFixTitelName1Name2();
						}

						oZeile[REPORT_FAHRTENBUCH_FAHRZEUG_PRIVAT] = reiseBeginn.getC_fahrzeug();

						if (reiseBeginn.getFlrfahrzeug() != null) {
							oZeile[REPORT_FAHRTENBUCH_FAHRZEUG_FIRMA] = reiseBeginn.getFlrfahrzeug().getC_bez();
							oZeile[REPORT_FAHRTENBUCH_FAHRZEUG_FIRMA_KENNZEICHEN] = reiseBeginn.getFlrfahrzeug()
									.getC_kennzeichen();
							oZeile[REPORT_FAHRTENBUCH_FAHRZEUG_VERWENDUNGSART] = reiseBeginn.getFlrfahrzeug()
									.getFahrzeugverwendungsart_c_nr();

							oZeile[REPORT_FAHRTENBUCH_FAHRZEUG_FIRMA_KMKOSTEN] = getPersonalFac()
									.getKMKostenInZielwaehrung(reiseBeginn.getFlrfahrzeug().getI_id(),
											flrReise.getT_zeit(), theClientDto.getSMandantenwaehrung(), theClientDto);

						}

						alDaten.add(oZeile);

					}

					if (resultListIterator.hasNext()) {
						reiseBeginn = (FLRReise) resultListIterator.next();
					}

				}

			}

			session.close();

			index = -1;
			sAktuellerReport = ZeiterfassungReportFac.REPORT_FAHRTENBUCH;
			HashMap<String, Object> parameter = new HashMap<String, Object>();

			// KM-Geld
			try {
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(tVon.getTime());
				PersonalgehaltDto personalgehaltDto = getPersonalFac().personalgehaltFindLetztePersonalgehalt(
						personalIId, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));

				if (personalgehaltDto != null) {
					parameter.put("P_KMGELD1", personalgehaltDto.getNKmgeld1());
					parameter.put("P_KMGELD2", personalgehaltDto.getNKmgeld2());
					parameter.put("P_KMGELDMITFAHRER", personalgehaltDto.getNKmgeldMitfahrer());
					parameter.put("P_KMGELD1BISKILOMETER", personalgehaltDto.getFBiskilometer().doubleValue());

				}
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}

			parameter.put("P_PERSONAL", personalDto.getPartnerDto().formatFixTitelName1Name2());

			parameter.put("P_VON", tVon);

			parameter.put("P_NUR_ANWESENDE", new Boolean(bNurAnwesende));

			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(tBis.getTime());
			c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);

			parameter.put("P_BIS", new java.sql.Timestamp(c.getTimeInMillis()));

			Object[][] returnArray = new Object[alDaten.size()][REPORT_FAHRTENBUCH_ANZAHL_SPALTEN];
			setData((Object[][]) alDaten.toArray(returnArray));

			initJRDS(parameter, ZeiterfassungReportFac.REPORT_MODUL, ZeiterfassungReportFac.REPORT_FAHRTENBUCH,
					theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

			if (print != null) {

				print = Helper.addReport2Report(print, getReportPrint().getPrint());
			} else {
				print = getReportPrint();
			}
		}
		return print;

	}

	public boolean next() throws JRException {
		index++;
		if (data != null) {
			return (index < data.length);
		} else {
			return jasperPrintTransformer.next(index);
		}
	}

	public Object getFieldValue(JRField jRField) throws JRException {
		Object value = null;
		String fieldName = jRField.getName();
		if (sAktuellerReport.equals(ZeiterfassungReportFac.REPORT_ZESTIFTE)) {
			if ("Kennung".equals(fieldName)) {
				value = data[index][REPORT_ZESTIFTE_KENNUNG];
			} else if ("Mehrfachstift".equals(fieldName)) {
				value = data[index][REPORT_ZESTIFTE_MEHRFACHSTIFT];
			} else if ("Personal".equals(fieldName)) {
				value = data[index][REPORT_ZESTIFTE_PERSONAL];
			}
		} else if (sAktuellerReport.equals(ZeiterfassungReportFac.REPORT_URLAUBSANTRAG)) {
			if ("Von".equals(fieldName)) {
				value = data[index][REPORT_URLAUBSANTRAG_VON];
			} else if ("Bis".equals(fieldName)) {
				value = data[index][REPORT_URLAUBSANTRAG_BIS];
			} else if ("Zusatz".equals(fieldName)) {
				value = data[index][REPORT_URLAUBSANTRAG_ZUSATZ];
			} else if ("Urlaub".equals(fieldName)) {
				value = data[index][REPORT_URLAUBSANTRAG_URLAUB];
			} else if ("Krank".equals(fieldName)) {
				value = data[index][REPORT_URLAUBSANTRAG_KRANK];
			} else if ("AnzahlTage".equals(fieldName)) {
				value = data[index][REPORT_URLAUBSANTRAG_ANZAHL_TAGE];
			} else if ("AnzahlHalbeTage".equals(fieldName)) {
				value = data[index][REPORT_URLAUBSANTRAG_ANZAHL_HALBE_TAGE];
			} else if ("AnzahlStunden".equals(fieldName)) {
				value = data[index][REPORT_URLAUBSANTRAG_ANZAHL_STUNDEN];
			}
		} else if (sAktuellerReport.equals(ZeiterfassungReportFac.REPORT_ZEITERFASSUNG_AENDERUNGEN)) {
			if (jasperPrintTransformer != null)
				return jasperPrintTransformer.transformData(index, fieldName);

			if ("Feldname".equals(fieldName)) {
				value = data[index][REPORT_ZEITERFASSUNG_AENDERUNGEN_FELDNAME];
			} else if ("Aenderungszeitpunkt".equals(fieldName)) {
				value = data[index][REPORT_ZEITERFASSUNG_AENDERUNGEN_AENDERUNGSZEITPUNKT];
			} else if ("GeaendertVon".equals(fieldName)) {
				value = data[index][REPORT_ZEITERFASSUNG_AENDERUNGEN_GEAENDERT_VON];
			} else if ("GeaendertNach".equals(fieldName)) {
				value = data[index][REPORT_ZEITERFASSUNG_AENDERUNGEN_GEAENDERT_NACH];
			} else if ("Operation".equals(fieldName)) {
				value = data[index][REPORT_ZEITERFASSUNG_AENDERUNGEN_OPERATION];
			}
		} else if (sAktuellerReport.equals(ZeiterfassungReportFac.REPORT_ABGESCHLOSSENE_ZEITBUCHUNGEN)) {
			if ("Personalnummer".equals(fieldName)) {
				value = data[index][REPORT_ZEITABSCHLUSS_PERSONALNR];
			} else if ("Name".equals(fieldName)) {
				value = data[index][REPORT_ZEITABSCHLUSS_NAME];
			} else if ("Vorname".equals(fieldName)) {
				value = data[index][REPORT_ZEITABSCHLUSS_VORNAME];
			} else if ("Titel".equals(fieldName)) {
				value = data[index][REPORT_ZEITABSCHLUSS_TITEL];
			} else if ("ZeitenAbgeschlossenBis".equals(fieldName)) {
				value = data[index][REPORT_ZEITABSCHLUSS_ZEITEN_ABGESCHLOSSEN_BIS];
			}
		} else if (sAktuellerReport.equals(ZeiterfassungReportFac.REPORT_MASCHINENVERWENDUNG)) {
			if ("Belegart".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENVERWENDUNG_BELEGART];
			} else if ("Stueckliste".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENVERWENDUNG_STUECKLISTE];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENVERWENDUNG_BEZEICHNUNG];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENVERWENDUNG_ZUSATZBEZEICHNUNG];
			} else if ("Stueckzeit".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENVERWENDUNG_STUECKZEIT];
			} else if ("Ruestzeit".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENVERWENDUNG_RUESTZEIT];
			} else if ("Arbeitsgang".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENVERWENDUNG_ARBEITSGANG];
			} else if ("Unterarbeitsgang".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENVERWENDUNG_UNTERARBEITSGANG];
			} else if ("AZArtikel".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENVERWENDUNG_AZ_ARTIKEL];
			} else if ("AZArtikelbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENVERWENDUNG_AZ_ARTIKELBEZEICHNUNG];
			}

		} else if (sAktuellerReport.equals(ZeiterfassungReportFac.REPORT_PRODUKTIVITAETSTAGESSTATISTIK)) {

			if ("Person".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETSTAGESSTATISTIK_PERSON];
			} else if ("Datum".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETSTAGESSTATISTIK_DATUM];
			} else if ("Leistungsfaktor".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETSTAGESSTATISTIK_LEISTUNGSFAKTOR];
			} else if ("Los".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETSTAGESSTATISTIK_LOS];
			} else if ("Dauer".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETSTAGESSTATISTIK_DAUER];
			} else if ("Artikel".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETSTAGESSTATISTIK_TAETIGKEIT];
			} else if ("Artikelbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETSTAGESSTATISTIK_TAETIGKEIT_BEZEICHNUNG];
			} else if ("Kunde".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETSTAGESSTATISTIK_KUNDE];
			} else if ("Stklnr".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETSTAGESSTATISTIK_STKLNR];
			} else if ("Stklbez".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETSTAGESSTATISTIK_STKLBEZ];
			} else if ("Losklassen".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETSTAGESSTATISTIK_LOSKLASSEN];
			} else if ("Agart".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETSTAGESSTATISTIK_AGART];
			} else if ("Arbeitsgang".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETSTAGESSTATISTIK_ARBEITSGANG];
			} else if ("Unterarbeitsgang".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETSTAGESSTATISTIK_UNTERARBEITSGANG];
			} else if ("Ruestzeit".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETSTAGESSTATISTIK_RUESTZEIT];
			} else if ("Stueckzeit".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETSTAGESSTATISTIK_STUECKZEIT];
			} else if ("Losgroesse".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETSTAGESSTATISTIK_LOSGROESSE];
			} else if ("Gutstueck".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETSTAGESSTATISTIK_GUTSTUECK];
			} else if ("Schlechtstueck".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETSTAGESSTATISTIK_SCHLECHTSTUECK];
			} else if ("Fertig".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETSTAGESSTATISTIK_FERTIG];
			} else if ("Fertigungsgruppe".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETSTAGESSTATISTIK_FERTIGUNGSGRUPPE];
			} else if ("MaschineBez".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETSTAGESSTATISTIK_MASCHINE_BEZ];
			} else if ("MaschineIdentifikation".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETSTAGESSTATISTIK_MASCHINE_IDENTIFIKATION];
			} else if ("MaschineInventar".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETSTAGESSTATISTIK_MASCHINE_INVENTATNUMMER];
			} else if ("LosRuestenMitrechnen".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETSTAGESSTATISTIK_RUESTEN_MITRECHNEN];
			} else if ("AnwesenheitszeitMonatsbetrachtung".equals(fieldName)) {
				value = data[index][REPORT_PRODUKTIVITAETSTAGESSTATISTIK_ANWESENHEITSZEIT_MONATSBETRACHTUNG];
			}

		} else if (sAktuellerReport.equals(ZeiterfassungReportFac.REPORT_ZEITDATEN)) {
			if ("Personalnummer".equals(fieldName)) {
				value = data[index][REPORT_ZEITDATEN_PERSONALNR];
			} else if ("Name".equals(fieldName)) {
				value = data[index][REPORT_ZEITDATEN_NAME];
			} else if ("Zeit".equals(fieldName)) {
				value = data[index][REPORT_ZEITDATEN_ZEIT];
			} else if ("Zeit_Bis".equals(fieldName)) {
				value = data[index][REPORT_ZEITDATEN_ZEIT_BIS];
			} else if ("Taetigkeit".equals(fieldName)) {
				value = data[index][REPORT_ZEITDATEN_TAETIGKEIT];
			} else if ("Bezahlt".equals(fieldName)) {
				value = data[index][REPORT_ZEITDATEN_BEZAHLT];
			} else if ("TaetigkeitKennung".equals(fieldName)) {
				value = data[index][REPORT_ZEITDATEN_TAETIGKEIT_KENNUNG];
			} else if ("Artikelbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_ZEITDATEN_ARTIKELBEZEICHNUNG];
			} else if ("Auftrag".equals(fieldName)) {
				value = data[index][REPORT_ZEITDATEN_AUFTRAG];
			} else if ("Projektbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_ZEITDATEN_PROJEKTBEZEICHNUNG];
			} else if ("Tagessumme".equals(fieldName)) {
				value = data[index][REPORT_ZEITDATEN_TAGESSUMME];
			} else if ("Maschine".equals(fieldName)) {
				value = data[index][REPORT_ZEITDATEN_MASCHINE];
			} else if ("Position".equals(fieldName)) {
				value = data[index][REPORT_ZEITDATEN_POSITION];
			} else if ("Buchungsart".equals(fieldName)) {
				value = data[index][REPORT_ZEITDATEN_BUCHUNGSART];
			} else if ("Bemerkung".equals(fieldName)) {
				value = data[index][REPORT_ZEITDATEN_BEMERKUNG];
			} else if ("Sollzeit".equals(fieldName)) {
				value = data[index][REPORT_ZEITDATEN_SOLLZEIT];
			} else if ("Gutstueck".equals(fieldName)) {
				value = data[index][REPORT_ZEITDATEN_GUTSTK];
			} else if ("Schlechtstueck".equals(fieldName)) {
				value = data[index][REPORT_ZEITDATEN_SCHLECHTSTK];
			} else if ("Quelle".equals(fieldName)) {
				value = data[index][REPORT_ZEITDATEN_QUELLE];
			} else if ("Kunde".equals(fieldName)) {
				value = data[index][REPORT_ZEITDATEN_KUNDE];
			} else if ("Dauer".equals(fieldName)) {
				value = data[index][REPORT_ZEITDATEN_DAUER];
			} else if ("Kommentar".equals(fieldName)) {
				value = Helper.formatStyledTextForJasper(data[index][REPORT_ZEITDATEN_KOMMENTAR]);
			} else if ("KommentarIntern".equals(fieldName)) {
				value = Helper.formatStyledTextForJasper(data[index][REPORT_ZEITDATEN_KOMMENTAR_INTERN]);
			} else if ("Jahrmonat".equals(fieldName)) {
				value = data[index][REPORT_ZEITDATEN_KOMMENTAR];
			} else if ("TaetigkeitSonderzeit".equals(fieldName)) {
				value = data[index][REPORT_ZEITDATEN_TAETIGKEIT_SONDERZEIT];
			} else if ("TaetigkeitSonderzeitKennung".equals(fieldName)) {
				value = data[index][REPORT_ZEITDATEN_TAETIGKEIT_SONDERZEIT_KENNUNG];
			} else if ("DauerSonderzeit".equals(fieldName)) {
				value = data[index][REPORT_ZEITDATEN_DAUER_SONDERZEIT];
			} else if ("DatumSonderzeit".equals(fieldName)) {
				value = data[index][REPORT_ZEITDATEN_DATUM_SONDERZEIT];
			} else if ("Zusatz".equals(fieldName)) {
				value = data[index][REPORT_ZEITDATEN_ZUSATZ];
			} else if ("Zeitgutschrift".equals(fieldName)) {
				value = data[index][REPORT_ZEITDATEN_ZEITGUTSCHRIFT];
			}

			else if ("FruehestesKommt".equals(fieldName)) {
				value = data[index][REPORT_ZEITDATEN_FRUEHESTES_KOMMT];
			} else if ("FruehestesKommtVortag".equals(fieldName)) {
				value = data[index][REPORT_ZEITDATEN_FRUEHESTES_KOMMT_VORTAG];
			} else if ("KommtAkzeptiertBis".equals(fieldName)) {
				value = data[index][REPORT_ZEITDATEN_KOMMT_AKZEPTIERT_BIS];
			} else if ("SpaetestesGeht".equals(fieldName)) {
				value = data[index][REPORT_ZEITDATEN_SPAETESTES_GEHT];
			} else if ("GehtAkzeptiertAb".equals(fieldName)) {
				value = data[index][REPORT_ZEITDATEN_GEHT_AKZEPTIERT_AB];
			} else if ("ZEITDATEN_I_ID".equals(fieldName)) {
				value = data[index][REPORT_ZEITDATEN_ZEITDATEN_I_ID];
			} else if ("ProjektIId".equals(fieldName)) {
				value = data[index][REPORT_ZEITDATEN_PROJEKT_I_ID];
			} else if ("ProjektBereich".equals(fieldName)) {
				value = data[index][REPORT_ZEITDATEN_PROJEKT_BEREICH];
			} else if ("Schichttag".equals(fieldName)) {
				value = data[index][REPORT_ZEITDATEN_SCHICHTTAG];
			}

			else if ("VerrechenbarInProzent".equals(fieldName)) {
				value = data[index][REPORT_ZEITDATEN_VERRECHENBAR_INPROZENT];
			} else if ("PersonErledigt".equals(fieldName)) {
				value = data[index][REPORT_ZEITDATEN_PERSON_ERLEDIGT];
			} else if ("ZeitpunktErledigt".equals(fieldName)) {
				value = data[index][REPORT_ZEITDATEN_ZEITPUNKT_ERLEDIGT];
			} else if ("TelefonzeitEnde".equals(fieldName)) {
				value = data[index][REPORT_ZEITDATEN_TELEFONZEIT_ENDE];
			} 

		} else if (sAktuellerReport.equals(ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK)
				|| sAktuellerReport.equals(ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIKVERDICHTET)) {
			if ("Artikelgruppe".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSZEITSTATISTIK_ARTIKELGRUPPE];
			} else if ("Artikelklasse".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSZEITSTATISTIK_ARTIKELKLASSE];
			} else if ("Kunde".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSZEITSTATISTIK_KUNDE];
			} else if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSZEITSTATISTIK_ARTIKELNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSZEITSTATISTIK_BEZEICHNUNG];
			} else if ("Person".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSZEITSTATISTIK_PERSON];
			} else if ("Personalnummer".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSZEITSTATISTIK_PERSONALNUMMER];
			} else if ("Person".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSZEITSTATISTIK_PERSON];
			} else if ("PersonKurzzeichen".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSZEITSTATISTIK_KURZZEICHEN];
			} else if ("Beleg".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSZEITSTATISTIK_BELEG];
			} else if ("Vertreter".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSZEITSTATISTIK_VERTRETER];
			} else if ("Dauer".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSZEITSTATISTIK_DAUER];
			} else if ("Von".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSZEITSTATISTIK_VON];
			} else if ("Bis".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSZEITSTATISTIK_BIS];
			} else if ("Kosten".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSZEITSTATISTIK_KOSTEN];
			} else if ("Gruppierung".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSZEITSTATISTIK_GRUPPIERUNG];
			} else if ("Projekt".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSZEITSTATISTIK_PROJEKT];
			} else if ("Kundenbestellnummer".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSZEITSTATISTIK_KUNDENBESTELLNUMMER];
			} else if ("Bemerkung".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSZEITSTATISTIK_BEMERKUNG];
			} else if ("Kommentar".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSZEITSTATISTIK_KOMMENTAR];
			} else if ("Fertigungsgruppe".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSZEITSTATISTIK_FERTIGUNGSGRUPPE];
			} else if ("ProjektStatus".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSZEITSTATISTIK_PROJEKT_STATUS];
			} else if ("ProjektKategorie".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSZEITSTATISTIK_PROJEKT_KATEGORIE];
			} else if ("ProjektGesamtdauer".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSZEITSTATISTIK_PROJEKT_GESAMTDAUER];
			} else if ("ProjektIId".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSZEITSTATISTIK_PROJEKT_I_ID];
			} else if ("ProjektBereich".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSZEITSTATISTIK_PROJEKT_BEREICH];
			} else if ("ProjektTyp".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSZEITSTATISTIK_PROJEKT_TYP];
			} else if ("ProjektVerrechenbar".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSZEITSTATISTIK_PROJEKT_VERRECHENBAR];
			} else if ("ProjektInternErledigt".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSZEITSTATISTIK_PROJEKT_INTERN_ERLEDIGT];
			} else if ("VerrechenbarInProzent".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSZEITSTATISTIK_VERRECHENBAR];
			} else if ("PersonErledigt".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSZEITSTATISTIK_PERSON_ERLEDIGT];
			} else if ("ZeitpunktErledigt".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSZEITSTATISTIK_ZEITPUNKT_ERLEDIGT];
			} else if ("BereitsVerrechnet".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSZEITSTATISTIK_BEREITS_VERRECHNET];
			} else if ("KommentarIntern".equals(fieldName)) {
				value = data[index][REPORT_ARBEITSZEITSTATISTIK_KOMMENTAR_INTERN];
			}

		} else if (sAktuellerReport.equals(ZeiterfassungReportFac.REPORT_AUFTRAGSZEITSTATISTIK)) {
			if ("Person".equals(fieldName)) {
				value = data[index][REPORT_AUFTRAGSZEITSTATISTIK_PERSON];
			} else if ("SubreportAuftraege".equals(fieldName)) {
				value = data[index][REPORT_AUFTRAGSZEITSTATISTIK_SUBREPORT_AUFTRAEGE];
			} else if ("Nichtzuordenbar".equals(fieldName)) {
				value = data[index][REPORT_AUFTRAGSZEITSTATISTIK_NICHT_ZUORDENBAR];
			} else if ("Sonderzeiten".equals(fieldName)) {
				value = data[index][REPORT_AUFTRAGSZEITSTATISTIK_SONDERZEITEN];
			}
		} else if (sAktuellerReport.equals(ZeiterfassungReportFac.REPORT_FAHRZEUGE)) {
			if ("Beginn".equals(fieldName)) {
				value = data[index][REPORT_FAHRZEUGE_BEGINN];
			} else if ("Ende".equals(fieldName)) {
				value = data[index][REPORT_FAHRZEUGE_ENDE];
			} else if ("Fahrzeug".equals(fieldName)) {
				value = data[index][REPORT_FAHRZEUGE_FAHRZEUG];
			} else if ("Kennzeichen".equals(fieldName)) {
				value = data[index][REPORT_FAHRZEUGE_KENNZEICHEN];
			} else if ("Kosten".equals(fieldName)) {
				value = data[index][REPORT_FAHRZEUGE_KOSTEN];
			} else if ("Land".equals(fieldName)) {
				value = data[index][REPORT_FAHRZEUGE_LAND];
			} else if ("Partner".equals(fieldName)) {
				value = data[index][REPORT_FAHRZEUGE_PARTNER];
			} else if ("Strecke".equals(fieldName)) {
				value = data[index][REPORT_FAHRZEUGE_STRECKE];
			} else if ("Person".equals(fieldName)) {
				value = data[index][REPORT_FAHRZEUGE_PERSON];
			}
		} else if (sAktuellerReport.equals(ZeiterfassungReportFac.REPORT_MASCHINENERFOLG)) {
			if ("Vorname".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENERFOLG_VORNAME];
			} else if ("Nachname".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENERFOLG_NACHNAME];
			} else if ("Anwesenheitszeit".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENERFOLG_ANWESENHEITSZEIT];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENERFOLG_BEZEICHNUNG];
			} else if ("Datum".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENERFOLG_DATUM];
			} else if ("Identifikationsnummer".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENERFOLG_IDENTIFIKATIONSNUMMER];
			}else if ("Seriennummer".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENERFOLG_SERIENNUMMER];
			} else if ("Laufzeit".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENERFOLG_LAUFZEIT];
			} else if ("Maschinengruppe".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENERFOLG_MASCHINENGRUPPE];
			} else if ("Personalnummer".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENERFOLG_PERSONALNUMMER];
			} else if ("Inventarnummer".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENERFOLG_INVENTARNUMMER];
			} else if ("JahrMonat".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENERFOLG_JAHRMONAT];
			} else if ("Kostenstelle".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENERFOLG_KOSTENSTELLE];
			} else if ("Abteilung".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENERFOLG_ABTEILUNG];
			}

		} else if (sAktuellerReport.equals(ZeiterfassungReportFac.REPORT_TELEFONZEITEN)) {
			if ("Person".equals(fieldName)) {
				value = data[index][REPORT_TELEFONLISTE_PERSON];
			} else if ("Von".equals(fieldName)) {
				value = data[index][REPORT_TELEFONLISTE_VON];
			} else if ("Bis".equals(fieldName)) {
				value = data[index][REPORT_TELEFONLISTE_BIS];
			} else if ("Projekt".equals(fieldName)) {
				value = data[index][REPORT_TELEFONLISTE_PROJEKT];
			} else if ("Partner".equals(fieldName)) {
				value = data[index][REPORT_TELEFONLISTE_PARTNER];
			} else if ("LKZ_Partner".equals(fieldName)) {
				value = data[index][REPORT_TELEFONLISTE_LKZ_PARTNER];
			} else if ("Ansprechpartner".equals(fieldName)) {
				value = data[index][REPORT_TELEFONLISTE_ANSPRECHPARTNER];
			} else if ("Kommentarextern".equals(fieldName)) {
				value = data[index][REPORT_TELEFONLISTE_KOMMENTAREXTERN];
			} else if ("Kommentarintern".equals(fieldName)) {
				value = data[index][REPORT_TELEFONLISTE_KOMMENTARINTERN];
			} else if ("Dauer".equals(fieldName)) {
				value = data[index][REPORT_TELEFONLISTE_DAUER];
			} else if ("Gruppierung".equals(fieldName)) {
				value = data[index][REPORT_TELEFONLISTE_GRUPPIERUNG];
			}
		} else if (sAktuellerReport.equals(ZeiterfassungReportFac.REPORT_MASCHINENBELEGUNG)) {
			if ("Identifikationsnummer".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENBELEGUNG_IDENTIFIKATIONSNUMMER];
			}else if ("Seriennummer".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENBELEGUNG_SERIENNUMMER];
			} else if ("Inventarnummer".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENBELEGUNG_INVENTARNUMMER];
			} else if ("Versteckt".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENBELEGUNG_VERSTECKT];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENBELEGUNG_BEZEICHNUNG];
			} else if ("Datum".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENBELEGUNG_DATUM];
			} else if ("Artikel".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENBELEGUNG_ARTIKEL];
			} else if ("Artikelbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENBELEGUNG_ARTIKELBEZEICHNUNG];
			} else if ("Fertig".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENBELEGUNG_FERTIG];
			} else if ("Losgroesse".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENBELEGUNG_LOSGROESSE];
			} else if ("Losnummer".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENBELEGUNG_LOSNUMMER];
			} else if ("Fertigungsgruppe".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENBELEGUNG_FERTIGUNGSGRUPPE];
			} else if ("Sollzeit".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENBELEGUNG_SOLL];
			} else if ("Arbeitsgang".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENBELEGUNG_ARBEITSGANG];
			} else if ("Unterarbeitsgang".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENBELEGUNG_UNTERARBEITSGANG];
			} else if ("Verfuegbarkeit".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENBELEGUNG_VERFUEGBARKEIT];
			} else if ("Stueckliste".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENBELEGUNG_STUECKLISTE];
			} else if ("StuecklisteBezeichnung".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENBELEGUNG_STUECKLISTEBEZEICHNUNG];
			} else if ("StuecklisteKurzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENBELEGUNG_STUECKLISTEKURZBEZEICHNUNG];
			} else if ("StuecklisteReferenznummer".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENBELEGUNG_STUECKLISTEREFERENZNUMMER];
			} else if ("StuecklisteZusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENBELEGUNG_STUECKLISTEZUSATZBEZEICHNUNG];
			} else if ("StuecklisteZusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENBELEGUNG_STUECKLISTEZUSATZBEZEICHNUNG2];
			} else if ("Projekt".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENBELEGUNG_PROJEKT];
			} else if ("Kommentar".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENBELEGUNG_KOMMENTAR];
			} else if ("Maschinengruppe".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENBELEGUNG_MASCHINENGRUPPE];
			} else if ("PositionsKommentar".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENBELEGUNG_POSITIONSKOMMENTAR];
			} else if ("Losende".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENBELEGUNG_LOSENDE];
			} else if ("Produktionsstop".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENBELEGUNG_PRODUKTIONSSTOP];
			} else if ("Stueckzeit".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENBELEGUNG_STUECKZEIT];
			} else if ("Ruestzeit".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENBELEGUNG_RUESTZEIT];
			} else if ("Kunde".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENBELEGUNG_KUNDE_NAME];
			} else if ("Lkz".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENBELEGUNG_KUNDE_LKZ];
			} else if ("Plz".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENBELEGUNG_KUNDE_PLZ];
			} else if ("Ort".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENBELEGUNG_KUNDE_ORT];
			} else if ("Losklassen".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENBELEGUNG_LOSKLASSEN];
			} else if ("MaschineUagNaechsterAgIdentifikationsnummer".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENBELEGUNG_UAG_NAECHSTER_AG_MASCHINE_IDENTIFIKATIONSNUMMER];
			} else if ("MaschineUagNaechsterAgInventarnummer".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENBELEGUNG_UAG_NAECHSTER_AG_MASCHINE_INVENTARNUMMER];
			} else if ("MaschineUagNaechsterAgBezeichnung".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENBELEGUNG_UAG_NAECHSTER_AG_MASCHINE_BEZEICHNUNG];
			}

		} else if (sAktuellerReport.equals(ZeiterfassungReportFac.REPORT_MITARBEITEREINTEILUNG)) {
			if ("Personal".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITEREINTEILUNG_PERSONAL];
			} else if ("Personalnummer".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITEREINTEILUNG_PERSONALNUMMER];
			} else if ("Kostenstelle".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITEREINTEILUNG_KOSTENSTELLE];
			} else if ("Abteilung".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITEREINTEILUNG_ABTEILUNG];
			} else if ("Unterarbeitsgang".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITEREINTEILUNG_UNTERARBEITSGANG];
			} else if ("Arbeitsgang".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITEREINTEILUNG_ARBEITSGANG];
			} else if ("Artikel".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITEREINTEILUNG_ARTIKEL];
			} else if ("Artikelbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITEREINTEILUNG_ARTIKELBEZEICHNUNG];
			} else if ("Datum".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITEREINTEILUNG_DATUM];
			} else if ("Fertig".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITEREINTEILUNG_FERTIG];
			} else if ("Laufzeit".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITEREINTEILUNG_LAUFZEIT];
			} else if ("Losnummer".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITEREINTEILUNG_LOSNUMMER];
			} else if ("Maschine".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITEREINTEILUNG_MASCHINE];
			} else if ("Umspannzeit".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITEREINTEILUNG_UMSPANNZEIT];
			} else if ("Soll".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITEREINTEILUNG_SOLL];
			} else if ("Tagessoll".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITEREINTEILUNG_TAGESSOLL];
			}

		} else if (sAktuellerReport.equals(ZeiterfassungReportFac.REPORT_MASCHINENPRODUKTIVITAET)) {
			if ("Auftrag".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENPRODUKTIVITAET_AUFTRAG];
			} else if ("Kunde".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENPRODUKTIVITAET_KUNDE];
			} else if ("Projekt".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENPRODUKTIVITAET_PROJEKT];
			} else if ("Stueckliste".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENPRODUKTIVITAET_STUECKLISTE];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENPRODUKTIVITAET_STKLBEZEICHNUNG];
			} else if ("Beleg".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENPRODUKTIVITAET_BELEG];
			} else if ("Dauer".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENPRODUKTIVITAET_DAUER];
			} else if ("Von".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENPRODUKTIVITAET_VON];
			} else if ("Bis".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENPRODUKTIVITAET_BIS];
			} else if ("KostenKalk".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENPRODUKTIVITAET_KOSTEN_KALK];
			} else if ("KostenVK".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENPRODUKTIVITAET_KOSTEN_VK];
			} else if ("Maschine".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENPRODUKTIVITAET_MASCHINE];
			} else if ("MaschineManuelleBedienung".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENPRODUKTIVITAET_MASCHINE_MANUELLE_BEDIENUNG];
			} else if ("MaschineKaufdatum".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENPRODUKTIVITAET_MASCHINE_KAUFDATUM];
			} else if ("MaschineKalkStundensatz".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENPRODUKTIVITAET_MACHINE_KALK_STUNDENSATZ];
			} else if ("MaschineVKStundensatz".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENPRODUKTIVITAET_MACHINE_VK_STUNDENSATZ];
			} else if ("MaschineVerfuegbarkeit".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENPRODUKTIVITAET_MASCHINE_VERFUEGBARKEIT];
			} else if ("Fertigungsgruppe".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENPRODUKTIVITAET_FERTIGUNGSGRUPPE];
			} else if ("MaschineAnschaffungskosten".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENPRODUKTIVITAET_MASCHINE_ANSCHAFFUNGSKOSTEN];
			} else if ("MaschineAbschreibungsdauer".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENPRODUKTIVITAET_MASCHINE_ABSCHREIBUNGSDAUER];
			} else if ("MaschineVerzinsung".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENPRODUKTIVITAET_MASCHINE_VERZINSUNG];
			} else if ("MaschineEnergierkosten".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENPRODUKTIVITAET_MASCHINE_ENERGIERKOSTEN];
			} else if ("MaschineRaumkosten".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENPRODUKTIVITAET_MASCHINE_RAUMKOSTEN];
			} else if ("MaschineSonstigeKosten".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENPRODUKTIVITAET_MASCHINE_SONSTIGE_KOSTEN];
			} else if ("MaschinePlanstunden".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENPRODUKTIVITAET_MASCHINE_PLANSTUNDEN];
			}

		} else if (sAktuellerReport.equals(ZeiterfassungReportFac.REPORT_MASCHINENLISTE)) {
			if ("Inventarnummer".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENLISTE_INVENTARNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENLISTE_BEZEICHNUNG];
			} else if ("Identifikationsnummer".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENLISTE_IDENTIFIKATIONSNUMMER];
			} else if ("Seriennummer".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENLISTE_SERIENNUMMER];
			} else if ("Verfuegbarkeit".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENLISTE_VERFUEGBARKEIT];
			} else if ("Kaufdatum".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENLISTE_KAUFDATUM];
			} else if ("Autoende".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENLISTE_AUTOENDE];
			} else if ("ManuelleBedienung".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENLISTE_MANUELLE_BEDIENUNG];
			} else if ("Maschinengruppe".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENLISTE_MASCHINENGRUPPE];
			} else if ("ZuletztGestartetVon".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENLISTE_ZULETZT_GESTARTET_VON];
			} else if ("ZuletztGestartetUm".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENLISTE_ZULETZT_GESTARTET_UM];
			} else if ("MaschinenkostenZumZeitpunkt".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENLISTE_KOSTEN_ZUM_ZEITPUNKT];
			} else if ("VKStundensatz".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENLISTE_KOSTEN_VK_ZUM_ZEITPUNKT];
			} else if ("Anschaffungskosten".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENLISTE_ANSCHAFFUNGSKOSTEN];
			} else if ("Abschreibungsdauer".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENLISTE_ABSCHREIBUNGSDAUER];
			} else if ("Verzinsung".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENLISTE_VERZINSUNG];
			} else if ("Energierkosten".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENLISTE_ENERGIERKOSTEN];
			} else if ("Raumkosten".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENLISTE_RAUMKOSTEN];
			} else if ("SonstigeKosten".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENLISTE_SONSTIGE_KOSTEN];
			} else if ("Planstunden".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENLISTE_PLANSTUNDEN];
			} else if ("Verrechnungsartikel".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENLISTE_VERRECHNUNGSARTIKEL];
			} else if ("VerrechnungsartikelBezeichnung".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENLISTE_VERRECHNUNGSARTIKEL_BEZEICHNUNG];
			}

		} else if (sAktuellerReport.equals(ZeiterfassungReportFac.REPORT_MASCHINENZEITDATEN)) {
			if ("Von".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENZEITDATEN_VON];
			} else if ("Bis".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENZEITDATEN_BIS];
			} else if ("PersonGestartet".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENZEITDATEN_PERSON];
			} else if ("Los".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENZEITDATEN_LOS];
			} else if ("Projektbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENZEITDATEN_PROJEKTBEZEICHNUNG];
			} else if ("Losartikelnr".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENZEITDATEN_LOSARTIKELNR];
			} else if ("Losartikelbez".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENZEITDATEN_LOSARTIKELBEZ];
			} else if ("Losartikelreferenznummer".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENZEITDATEN_LOSARTIKELREFERENZNUMMER];
			} else if ("Dauer".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENZEITDATEN_DAUER];
			} else if ("Kunde".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENZEITDATEN_KUNDE];
			} else if ("LosArbeitsgang".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENZEITDATEN_LOS_AG];
			} else if ("LosUnterarbeitsgang".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENZEITDATEN_LOS_UAG];
			} else if ("Maschine".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENZEITDATEN_MASCHINE];
			} else if ("MaschineManuelleBedienung".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENZEITDATEN_MASCHINE_MANUELLE_BEDIENUNG];
			}else if ("Parallelbedienung".equals(fieldName)) {
				value = data[index][REPORT_MASCHINENZEITDATEN_PARALLELBEDIENUNG];
			}
		} else if (sAktuellerReport.equals(ZeiterfassungReportFac.REPORT_FAHRTENBUCH)) {
			if ("Datum".equals(fieldName)) {
				value = data[index][REPORT_FAHRTENBUCH_DATUM];
			} else if ("DatumEnde".equals(fieldName)) {
				value = data[index][REPORT_FAHRTENBUCH_DATUM_ENDE];
			} else if ("Beginn".equals(fieldName)) {
				value = data[index][REPORT_FAHRTENBUCH_KMBEGINN];
			} else if ("Mitfahrer".equals(fieldName)) {
				value = data[index][REPORT_FAHRTENBUCH_MITFAHRER];
			} else if ("Kommentar".equals(fieldName)) {
				value = data[index][REPORT_FAHRTENBUCH_KOMMENTAR];
			} else if ("Ende".equals(fieldName)) {
				value = data[index][REPORT_FAHRTENBUCH_KMENDE];
			} else if ("Partner".equals(fieldName)) {
				value = data[index][REPORT_FAHRTENBUCH_KUNDE];
			} else if ("Strecke".equals(fieldName)) {
				value = data[index][REPORT_FAHRTENBUCH_STRECKE];
			} else if ("Tag".equals(fieldName)) {
				value = data[index][REPORT_FAHRTENBUCH_TAG];
			} else if ("FahrzeugPrivat".equals(fieldName)) {
				value = data[index][REPORT_FAHRTENBUCH_FAHRZEUG_PRIVAT];
			} else if ("FahrzeugFirma".equals(fieldName)) {
				value = data[index][REPORT_FAHRTENBUCH_FAHRZEUG_FIRMA];
			} else if ("FahrzeugFirmaKennzeichen".equals(fieldName)) {
				value = data[index][REPORT_FAHRTENBUCH_FAHRZEUG_FIRMA_KENNZEICHEN];
			} else if ("FahrzeugFirmaVerwendungsart".equals(fieldName)) {
				value = data[index][REPORT_FAHRTENBUCH_FAHRZEUG_VERWENDUNGSART];
			} else if ("FahrzeugFirmaKMKosten".equals(fieldName)) {
				value = data[index][REPORT_FAHRTENBUCH_FAHRZEUG_FIRMA_KMKOSTEN];
			} else if ("FehlerInKM".equals(fieldName)) {
				value = data[index][REPORT_FAHRTENBUCH_FEHLER_IN_KM];
			} else if ("ReiseWirdFortgesetzt".equals(fieldName)) {
				value = data[index][REPORT_FAHRTENBUCH_REISE_WIRD_FORTGESETZT];
			} else if ("ReiseIstFortgesetzt".equals(fieldName)) {
				value = data[index][REPORT_FAHRTENBUCH_REISE_IST_FORTGESETZT];
			}
		} else if (sAktuellerReport.equals(ZeiterfassungReportFac.REPORT_MITARBEITERUEBERSICHT)) {
			if ("Name".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_NAME];
			} else if ("Tag01".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_TAG01];
			} else if ("Tag02".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_TAG02];
			} else if ("Tag03".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_TAG03];
			} else if ("Tag04".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_TAG04];
			} else if ("Tag05".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_TAG05];
			} else if ("Tag06".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_TAG06];
			} else if ("Tag07".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_TAG07];
			} else if ("Tag08".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_TAG08];
			} else if ("Tag09".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_TAG09];
			} else if ("Tag10".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_TAG10];
			} else if ("Tag11".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_TAG11];
			} else if ("Tag12".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_TAG12];
			} else if ("Tag13".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_TAG13];
			} else if ("Tag14".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_TAG14];
			} else if ("Tag15".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_TAG15];
			} else if ("Tag16".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_TAG16];
			} else if ("Tag17".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_TAG17];
			} else if ("Tag18".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_TAG18];
			} else if ("Tag19".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_TAG19];
			} else if ("Tag20".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_TAG20];
			} else if ("Tag21".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_TAG21];
			} else if ("Tag22".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_TAG22];
			} else if ("Tag23".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_TAG23];
			} else if ("Tag24".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_TAG24];
			} else if ("Tag25".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_TAG25];
			} else if ("Tag26".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_TAG26];
			} else if ("Tag27".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_TAG27];
			} else if ("Tag28".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_TAG28];
			} else if ("Tag29".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_TAG29];
			} else if ("Tag30".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_TAG30];
			} else if ("Tag31".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_TAG31];
			} else if ("Summeistzeit".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_ANWESENHEITSUMME];
			} else if ("KalkJahresIstStunden".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_KALK_JAHRESISTSTUNDEN];
			} else if ("Bez01".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_BEZ01];
			} else if ("Bez02".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_BEZ02];
			} else if ("Bez03".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_BEZ03];
			} else if ("Bez04".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_BEZ04];
			} else if ("Bez05".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_BEZ05];
			} else if ("Bez06".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_BEZ06];
			} else if ("Bez07".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_BEZ07];
			} else if ("Bez08".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_BEZ08];
			} else if ("Bez09".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_BEZ09];
			} else if ("Bez10".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_BEZ10];
			} else if ("Bez11".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_BEZ11];
			} else if ("Bez12".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_BEZ12];
			} else if ("Bez13".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_BEZ13];
			} else if ("Bez14".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_BEZ14];
			} else if ("Bez15".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_BEZ15];
			} else if ("Bez16".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_BEZ16];
			} else if ("Bez17".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_BEZ17];
			} else if ("Bez18".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_BEZ18];
			} else if ("Bez19".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_BEZ19];
			} else if ("Bez20".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_BEZ20];
			} else if ("Bez21".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_BEZ21];
			} else if ("Bez22".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_BEZ22];
			} else if ("Bez23".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_BEZ23];
			} else if ("Bez24".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_BEZ24];
			} else if ("Bez25".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_BEZ25];
			} else if ("Bez26".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_BEZ26];
			} else if ("Bez27".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_BEZ27];
			} else if ("Bez28".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_BEZ28];
			} else if ("Bez29".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_BEZ29];
			} else if ("Bez30".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_BEZ30];
			} else if ("Bez31".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_BEZ31];
			} else if ("Kostenstelle".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_KOSTENSTELLE];
			} else if ("Abteilung".equals(fieldName)) {
				value = data[index][REPORT_MITARBEITERUEBERSICHT_ABTEILUNG];
			}

		}
		return value;
	}

	protected static Comparator<ArbeitszeitstatistikDto> getSortGruppierungBeginnzeitComparator() {
		return new Comparator<ArbeitszeitstatistikDto>() {
			@Override
			public int compare(com.lp.server.personal.service.ArbeitszeitstatistikDto o1,
					com.lp.server.personal.service.ArbeitszeitstatistikDto o2) {

				String s1 = o1.getSGruppierung();
				if (s1 == null) {
					s1 = "";
				}
				String s2 = o2.getSGruppierung();
				if (s2 == null) {
					s2 = "";
				}

				int value = s1.compareTo(s2);
				if (value == 0) {
					value = o1.getAuftragzeitenDto().getTsBeginn().compareTo(o2.getAuftragzeitenDto().getTsBeginn());
				}
				return value;
			}
		};
	}

	public class AenderungenTransformer implements IJasperPrintTransformer {

		private ZeiterfassungAenderungenReportDto reportDto;
		private int totalSize;
		private TheClientDto theClientDto;

		public AenderungenTransformer(ZeiterfassungAenderungenReportDto reportDto, TheClientDto theClientDto) {
			this.reportDto = reportDto;
			totalSize = this.reportDto.getItems().size();
			this.theClientDto = theClientDto;
		}

		public boolean next(int index) {
			return index < totalSize;
		}

		@Override
		public Object transformData(int rowIndex, String keyName) {
			Object value = null;
			ZeiterfassungAenderungenItemDto vonDto = reportDto.getItems().get(rowIndex).getKey();
			ZeiterfassungAenderungenItemDto nachDto = reportDto.getItems().get(rowIndex).getValue();

			try {
				if ("Aenderungszeitpunkt".equals(keyName)) {
					value = vonDto.getZeitAendern();
				} else if ("Wer".equals(keyName)) {
					value = vonDto.getPersonAendern().getValue();
				} else if ("Operation".equals(keyName)) {
					value = vonDto.getOperation();
				} else if ("ZeitVon".equals(keyName)) {
					value = vonDto.getZeit();
				} else if ("WoVon".equals(keyName)) {
					value = vonDto.getWoWurdeGebucht();
				} else if ("TaetigkeitVon".equals(keyName)) {
					if (vonDto.istBeleg() != null ? vonDto.istBeleg()
							: nachDto.istBeleg() != null ? nachDto.istBeleg() : true) {
						value = vonDto.getArtikelCnr();
					} else {
						value = vonDto.getTaetigkeit() == null ? null : vonDto.getTaetigkeit().getValue();
					}
				} else if ("TaetigkeitBezVon".equals(keyName)) {
					if (vonDto.istBeleg() != null ? vonDto.istBeleg()
							: nachDto.istBeleg() != null ? nachDto.istBeleg() : true) {
						value = vonDto.getArtikelBez();
					} else {
						value = vonDto.getTaetigkeit() == null ? null : vonDto.getTaetigkeit().getValue();
					}
				} else if ("BelegartVon".equals(keyName)) {
					value = vonDto.getBelegart();
				} else if ("BelegVon".equals(keyName)) {
					value = vonDto.getBeleg() == null ? null : vonDto.getBeleg().getValue();
				} else if ("BelegpositionVon".equals(keyName)) {
					value = vonDto.getBelegPosition() == null ? null : vonDto.getBelegPosition().getValue();
				} else if ("ZeitNach".equals(keyName)) {
					value = nachDto.getZeit();
				} else if ("WoNach".equals(keyName)) {
					value = nachDto.getWoWurdeGebucht();
				} else if ("TaetigkeitNach".equals(keyName)) {
					if (nachDto.istBeleg() != null ? nachDto.istBeleg()
							: vonDto.istBeleg() != null ? vonDto.istBeleg() : true) {
						value = nachDto.getArtikelCnr();
					} else {
						value = nachDto.getTaetigkeit() == null ? null : nachDto.getTaetigkeit().getValue();
					}
				} else if ("TaetigkeitBezNach".equals(keyName)) {
					if (nachDto.istBeleg() != null ? nachDto.istBeleg()
							: vonDto.istBeleg() != null ? vonDto.istBeleg() : true) {
						value = nachDto.getArtikelBez();
					} else {
						value = nachDto.getTaetigkeit() == null ? null : nachDto.getTaetigkeit().getValue();
					}
				} else if ("BelegartNach".equals(keyName)) {
					value = nachDto.getBelegart();
				} else if ("BelegNach".equals(keyName)) {
					value = nachDto.getBeleg() == null ? null : nachDto.getBeleg().getValue();
				} else if ("BelegpositionNach".equals(keyName)) {
					value = nachDto.getBelegPosition() == null ? null : nachDto.getBelegPosition().getValue();
				} else if ("EntityId".equals(keyName)) {
					value = vonDto.getEntityId();
				} else if ("IstBelegVon".equals(keyName)) {
					value = vonDto.istBeleg();
				} else if ("IstBelegNach".equals(keyName)) {
					value = nachDto.istBeleg();
				}
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
			return value;
		}

		@Override
		public HashMap<String, Object> transformParameter() {
			HashMap<String, Object> parameter = new HashMap<String, Object>();

			parameter.put("P_PERSON", reportDto.getPerson().getValue());
			parameter.put("P_ZEIT_VON", reportDto.getZeitbuchungenVon());
			parameter.put("P_ZEIT_BIS", reportDto.getZeitbuchungenBis());
			parameter.put("P_AENDERUNG_VON", reportDto.getAenderungenVon());
			parameter.put("P_AENDERUNG_BIS", reportDto.getAenderungenBis());
			parameter.put("P_MIT_INSERTS", reportDto.getMitInserts());
			parameter.put("P_MIT_UPDATES", reportDto.getMitUpdates());
			parameter.put("P_MIT_DELETES", reportDto.getMitDeletes());

			if (reportDto.getSortierungsart()
					.equals(ZeiterfassungAenderungenReportDto.REPORT_AENDERUNGEN_SORTIERUNG_ZEITBUCHUNGEN)) {
				parameter.put("P_SORTIERUNG",
						getTextRespectUISpr("pers.zeiterfassung.aenderungenreport.sort.zeitbuchungen",
								theClientDto.getMandant(), theClientDto.getLocUi()));
			} else if (reportDto.getSortierungsart()
					.equals(ZeiterfassungAenderungenReportDto.REPORT_AENDERUNGEN_SORTIERUNG_AENDERUNGSZEIT)) {
				parameter.put("P_SORTIERUNG",
						getTextRespectUISpr("pers.zeiterfassung.aenderungenreport.sort.aenderungszeitpunkt",
								theClientDto.getMandant(), theClientDto.getLocUi()));
			}

			return parameter;
		}

	}
}
