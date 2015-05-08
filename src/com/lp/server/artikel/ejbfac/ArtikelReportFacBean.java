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
package com.lp.server.artikel.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
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
import javax.interceptor.Interceptors;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.anfrage.service.ReportAnfragestatistikKriterienDto;
import com.lp.server.angebotstkl.fastlanereader.generated.FLRAgstklarbeitsplan;
import com.lp.server.angebotstkl.fastlanereader.generated.FLRAgstklposition;
import com.lp.server.angebotstkl.fastlanereader.generated.FLREinkaufsangebotposition;
import com.lp.server.angebotstkl.service.AngebotstklpositionFac;
import com.lp.server.artikel.ejb.Lagerbewegung;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelalergen;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelbestellt;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikellager;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikellagerplaetze;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikellieferant;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikellieferantstaffel;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikellog;
import com.lp.server.artikel.fastlanereader.generated.FLRFehlmenge;
import com.lp.server.artikel.fastlanereader.generated.FLRInventurstand;
import com.lp.server.artikel.fastlanereader.generated.FLRLagerbewegung;
import com.lp.server.artikel.fastlanereader.generated.FLRLagercockpitumbuchung;
import com.lp.server.artikel.service.AlergenDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelReportFac;
import com.lp.server.artikel.service.ArtikelalergenDto;
import com.lp.server.artikel.service.ArtikelbestelltFac;
import com.lp.server.artikel.service.ArtikelkommentarDto;
import com.lp.server.artikel.service.ArtikellagerplaetzeDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ArtklaDto;
import com.lp.server.artikel.service.BelegInfos;
import com.lp.server.artikel.service.HerstellerDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.LagerFac;
import com.lp.server.artikel.service.LagerabgangursprungDto;
import com.lp.server.artikel.service.LagerbewegungDto;
import com.lp.server.artikel.service.LagerplatzDto;
import com.lp.server.artikel.service.MaterialDto;
import com.lp.server.artikel.service.RahmenbestelltReportDto;
import com.lp.server.artikel.service.ReportRahmenreservierungDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VkPreisfindungEinzelverkaufspreisDto;
import com.lp.server.artikel.service.VkPreisfindungPreislisteDto;
import com.lp.server.artikel.service.VkpfMengenstaffelDto;
import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragpositionReport;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragseriennrn;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.auftrag.service.AuftragpositionFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.fastlanereader.generated.FLRBestellpositionReport;
import com.lp.server.bestellung.fastlanereader.generated.FLRWareneingangspositionen;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.bestellung.service.BewegungsvorschauDto;
import com.lp.server.fertigung.fastlanereader.generated.FLRLos;
import com.lp.server.fertigung.fastlanereader.generated.FLRLosReport;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LosablieferungDto;
import com.lp.server.fertigung.service.LosistmaterialDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.partner.fastlanereader.generated.FLRKundesokomengenstaffel;
import com.lp.server.partner.fastlanereader.generated.FLRPartner;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeReportFac;
import com.lp.server.partner.service.KundesokoDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStueckliste;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklistearbeitsplan;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklisteposition;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteReportFac;
import com.lp.server.stueckliste.service.StuecklisteeigenschaftDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeArtikeletikett;
import com.lp.server.system.jcr.service.docnode.DocNodeBase;
import com.lp.server.system.jcr.service.docnode.DocNodeFile;
import com.lp.server.system.jcr.service.docnode.DocNodeFolder;
import com.lp.server.system.jcr.service.docnode.DocNodeLiteral;
import com.lp.server.system.jcr.service.docnode.DocNodeLosAblieferung;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.jcr.service.docnode.HeliumDocPath;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.PanelbeschreibungDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.ZusatzfunktionberechtigungDto;
import com.lp.server.util.HelperServer;
import com.lp.server.util.LPReport;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.server.util.report.TimingInterceptor;
import com.lp.util.AufgeloesteFehlmengenDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.LPDatenSubreport;

@Stateless
@Interceptors(TimingInterceptor.class)
public class ArtikelReportFacBean extends LPReport implements ArtikelReportFac,
		JRDataSource {

	private String sAktuellerReport = null;
	private Object[][] data = null;

	private static int REPORT_LOSSTATUS_LOSNR = 0;
	private static int REPORT_LOSSTATUS_MENGE = 1;
	private static int REPORT_LOSSTATUS_AUFTRAG = 2;
	private static int REPORT_LOSSTATUS_PROJEKT = 3;
	private static int REPORT_LOSSTATUS_BEGINN = 4;
	private static int REPORT_LOSSTATUS_ENDE = 5;
	private static int REPORT_LOSSTATUS_STATUS = 6;
	private static int REPORT_LOSSTATUS_ABGELIEFERT = 7;
	private static int REPORT_LOSSTATUS_PREIS_ABLIEFERUNG_MATERIAL = 8;
	private static int REPORT_LOSSTATUS_PREIS_ABLIEFERUNG_ZEIT = 9;
	private static int REPORT_LOSSTATUS_ANGELEGT = 10;
	private static int REPORT_LOSSTATUS_ERLEDIGT = 11;
	private static int REPORT_LOSSTATUS_AUSGEGEBEN = 12;
	private static int REPORT_LOSSTATUS_KUNDE = 13;
	private static int REPORT_LOSSTATUS_FERTIGUNGSGRUPPE = 14;
	private static int REPORT_LOSSTATUS_TECHNIKER = 15;
	private static int REPORT_LOSSTATUS_ZIELLAGER = 16;
	private static int REPORT_LOSSTATUS_AUFTRAG_PROJEKT = 17;

	private static int REPORT_ARTIKELETIKETT_ARTIKELNUMMER = 0;
	private static int REPORT_ARTIKELETIKETT_KURZBEZEICHNUNG = 1;
	private static int REPORT_ARTIKELETIKETT_BEZEICHNUNG = 2;
	private static int REPORT_ARTIKELETIKETT_ZUSATZBEZEICHNUNG = 3;
	private static int REPORT_ARTIKELETIKETT_ZUSATZBEZEICHNUNG2 = 4;
	private static int REPORT_ARTIKELETIKETT_EINHEIT = 5;
	private static int REPORT_ARTIKELETIKETT_REFERENZNUMMER = 6;
	private static int REPORT_ARTIKELETIKETT_HERSTELLER = 7;
	private static int REPORT_ARTIKELETIKETT_LIEFERANT = 8;
	private static int REPORT_ARTIKELETIKETT_LAGERSTAND = 9;
	private static int REPORT_ARTIKELETIKETT_LAGERORT = 10;
	private static int REPORT_ARTIKELETIKETT_LIEFERANT_ARTIKELNUMMER = 11;
	private static int REPORT_ARTIKELETIKETT_LIEFERANT_ARTIKELBEZEICHNUNG = 12;
	private static int REPORT_ARTIKELETIKETT_MENGE = 13;
	private static int REPORT_ARTIKELETIKETT_KOMMENTAR = 14;
	private static int REPORT_ARTIKELETIKETT_BREITE = 15;
	private static int REPORT_ARTIKELETIKETT_HOEHE = 16;
	private static int REPORT_ARTIKELETIKETT_TIEFE = 17;
	private static int REPORT_ARTIKELETIKETT_BAUFORM = 18;
	private static int REPORT_ARTIKELETIKETT_VERPACKUNGSART = 19;
	private static int REPORT_ARTIKELETIKETT_MATERIAL = 20;
	private static int REPORT_ARTIKELETIKETT_HERSTELLER_NAME1 = 21;
	private static int REPORT_ARTIKELETIKETT_HERSTELLER_NAME2 = 22;
	private static int REPORT_ARTIKELETIKETT_INDEX = 23;
	private static int REPORT_ARTIKELETIKETT_REVISION = 24;
	private static int REPORT_ARTIKELETIKETT_MANDANTADRESSE = 25;
	private static int REPORT_ARTIKELETIKETT_VERPACKUNGS_EAN = 26;
	private static int REPORT_ARTIKELETIKETT_VERKAUFS_EAN = 27;
	private static int REPORT_ARTIKELETIKETT_VERPACKUNGSMENGE = 28;
	private static int REPORT_ARTIKELETIKETT_SNRCHNR = 29;
	private static int REPORT_ARTIKELETIKETT_VERSION = 30;
	private static int REPORT_ARTIKELETIKETT_PERSON_BUCHENDER = 31;
	private static int REPORT_ARTIKELETIKETT_KURZZEICHEN_BUCHENDER = 32;
	private static int REPORT_ARTIKELETIKETT_ANZAHL_SPALTEN = 33;

	private static int REPORT_FREIINFERTIGUNG_LOSNR = 0;
	private static int REPORT_FREIINFERTIGUNG_FREIEMENGE = 1;
	private static int REPORT_FREIINFERTIGUNG_AUFTRAG = 2;
	private static int REPORT_FREIINFERTIGUNG_PROJEKT = 3;
	private static int REPORT_FREIINFERTIGUNG_BEGINN = 4;
	private static int REPORT_FREIINFERTIGUNG_ENDE = 5;

	private static int REPORT_LIEFERANTENPREIS_LIEFERANT = 0;
	private static int REPORT_LIEFERANTENPREIS_MENGE = 1;
	private static int REPORT_LIEFERANTENPREIS_NETTOPREIS = 2;
	private static int REPORT_LIEFERANTENPREIS_MINDESTBESTELLMENGE = 3;
	private static int REPORT_LIEFERANTENPREIS_WIEDERBESCHAFFUNGSZEIT = 4;
	private static int REPORT_LIEFERANTENPREIS_FIXKOSTEN = 5;
	private static int REPORT_LIEFERANTENPREIS_GUELTIGAB = 6;
	private static int REPORT_LIEFERANTENPREIS_ANZAHL_SPALTEN = 7;

	private static int REPORT_FEHLMENGENLISTE_LOS = 0;
	private static int REPORT_FEHLMENGENLISTE_PROJEKTNAME = 1;
	private static int REPORT_FEHLMENGENLISTE_LIEFERTERMIN = 2;
	private static int REPORT_FEHLMENGENLISTE_MENGE = 3;
	private static int REPORT_FEHLMENGENLISTE_KUNDE = 4;
	private static int REPORT_FEHLMENGENLISTE_STUECKLISTE_NUMMER = 5;
	private static int REPORT_FEHLMENGENLISTE_STUECKLISTE_BEZEICHNUNG = 6;
	private static int REPORT_FEHLMENGENLISTE_LOSBEGINN = 7;
	private static int REPORT_FEHLMENGENLISTE_LOSENDE = 8;
	private static int REPORT_FEHLMENGENLISTE_ABLIEFERTERMIN = 9;

	private static int REPORT_ARTIKELSTAMMBLATT_ARTIKELNUMMER = 0;
	private static int REPORT_ARTIKELSTAMMBLATT_BEZEICHNUNG = 1;
	private static int REPORT_ARTIKELSTAMMBLATT_KURZBEZEICHNUNG = 2;
	private static int REPORT_ARTIKELSTAMMBLATT_ZUSATZBEZEICHNUNG = 3;
	private static int REPORT_ARTIKELSTAMMBLATT_BILD = 4;
	private static int REPORT_ARTIKELSTAMMBLATT_LANGTEXT = 5;
	private static int REPORT_ARTIKELSTAMMBLATT_MATERIAL = 6;
	private static int REPORT_ARTIKELSTAMMBLATT_GEWICHT = 7;
	private static int REPORT_ARTIKELSTAMMBLATT_VERPACKUNGSEINHEIT = 8;
	private static int REPORT_ARTIKELSTAMMBLATT_VERKAUFSPREIS = 9;
	private static int REPORT_ARTIKELSTAMMBLATT_LIEFERANT = 10;
	private static int REPORT_ARTIKELSTAMMBLATT_EINKAUFSPREIS = 11;
	private static int REPORT_ARTIKELSTAMMBLATT_ARTIKELNUMMERLIEFERANT = 12;
	private static int REPORT_ARTIKELSTAMMBLATT_EAN = 13;
	private static int REPORT_ARTIKELSTAMMBLATT_WARENVERKEHRSNUMMER = 14;
	private static int REPORT_ARTIKELSTAMMBLATT_LAGERPLATZ = 15;
	private static int REPORT_ARTIKELSTAMMBLATT_LAGERSTAND = 16;

	private static int REPORT_ARTIKELSTAMMBLATT_RESERVIERT = 17;
	private static int REPORT_ARTIKELSTAMMBLATT_FEHLMENGE = 18;
	private static int REPORT_ARTIKELSTAMMBLATT_INFERTIGUNG = 19;
	private static int REPORT_ARTIKELSTAMMBLATT_BESTELLT = 20;
	private static int REPORT_ARTIKELSTAMMBLATT_RAHMENRESERVIERT = 21;
	private static int REPORT_ARTIKELSTAMMBLATT_RAHMENBESTELLT = 22;
	private static int REPORT_ARTIKELSTAMMBLATT_DETAILBEDARF = 23;
	private static int REPORT_ARTIKELSTAMMBLATT_ECCN = 24;
	private static int REPORT_ARTIKELSTAMMBLATT_SUBREPORT_LAGERSTAENDE = 25;

	private static int REPORT_VERWENDUNGSNACHWEIS_STUECKLISTE = 0;
	private static int REPORT_VERWENDUNGSNACHWEIS_BEZEICHNUNG = 1;
	private static int REPORT_VERWENDUNGSNACHWEIS_ZUSATZ = 2;
	private static int REPORT_VERWENDUNGSNACHWEIS_ARBEITSGANG = 3;
	private static int REPORT_VERWENDUNGSNACHWEIS_MENGE = 4;
	private static int REPORT_VERWENDUNGSNACHWEIS_EINHEIT = 5;
	private static int REPORT_VERWENDUNGSNACHWEIS_SPERREN = 6;
	private static int REPORT_VERWENDUNGSNACHWEIS_LETZTE_VERWENDUNG = 7;
	private static int REPORT_VERWENDUNGSNACHWEIS_VERBRAUCHTEMENGE = 8;
	private static int REPORT_VERWENDUNGSNACHWEIS_VERSTECKT = 9;
	private static int REPORT_VERWENDUNGSNACHWEIS_STUECKZEIT = 10;
	private static int REPORT_VERWENDUNGSNACHWEIS_RUESTZEIT = 11;
	private static int REPORT_VERWENDUNGSNACHWEIS_LAGERSTAND = 12;
	private static int REPORT_VERWENDUNGSNACHWEIS_RESERVIERT = 13;
	private static int REPORT_VERWENDUNGSNACHWEIS_RAHMENRESERVIERT = 14;
	private static int REPORT_VERWENDUNGSNACHWEIS_IN_FERTIGUNG = 15;
	private static int REPORT_VERWENDUNGSNACHWEIS_FEHLMENGE = 16;
	private static int REPORT_VERWENDUNGSNACHWEIS_ANGBEOTSTUECKLSTE = 17;
	private static int REPORT_VERWENDUNGSNACHWEIS_ANZAHL_SPALTEN = 18;

	private static int REPORT_KUNDENSOKOS_KUNDE_NAME1 = 0;
	private static int REPORT_KUNDENSOKOS_KUNDE_NAME2 = 1;
	private static int REPORT_KUNDENSOKOS_KUNDE_LKZ = 2;
	private static int REPORT_KUNDENSOKOS_KUNDE_PLZ = 3;
	private static int REPORT_KUNDENSOKOS_KUNDE_ORT = 4;
	private static int REPORT_KUNDENSOKOS_KUNDE_KDARTIKELNUMMER = 5;
	private static int REPORT_KUNDENSOKOS_KUNDE_KDARTIKELBEZ = 6;
	private static int REPORT_KUNDENSOKOS_KUNDE_KDARTIKELBEZ2 = 7;
	private static int REPORT_KUNDENSOKOS_KUNDE_GUELTIAGAB = 8;
	private static int REPORT_KUNDENSOKOS_KUNDE_GUELTIAGBIS = 9;
	private static int REPORT_KUNDENSOKOS_KUNDE_MENGE = 10;
	private static int REPORT_KUNDENSOKOS_KUNDE_FIXPREIS = 11;
	private static int REPORT_KUNDENSOKOS_KUNDE_RABATT = 12;
	private static int REPORT_KUNDENSOKOS_KUNDE_BERECHNETER_PREIS = 13;
	private static int REPORT_KUNDENSOKOS_KUNDE_NAECHSTE_VKPREISBASIS_GUELTIG_AB = 14;
	private static int REPORT_KUNDENSOKOS_KUNDE_KBEZ = 15;
	private static int REPORT_KUNDENSOKOS_KUNDE_UID = 16;
	private static int REPORT_KUNDENSOKOS_ANZAHL_FELDER = 17;

	private static int REPORT_BEWEGUNGSVORSCHAU_BELEGART = 0;
	private static int REPORT_BEWEGUNGSVORSCHAU_BELEGNR = 1;
	private static int REPORT_BEWEGUNGSVORSCHAU_MENGE = 2;
	private static int REPORT_BEWEGUNGSVORSCHAU_LIEFERTERMIN = 3;
	private static int REPORT_BEWEGUNGSVORSCHAU_PARTNER = 4;
	private static int REPORT_BEWEGUNGSVORSCHAU_FIKTIVERLAGERSTAND = 5;
	private static int REPORT_BEWEGUNGSVORSCHAU_PROJEKT = 6;
	private static int REPORT_BEWEGUNGSVORSCHAU_FINALTERMIN = 7;
	private static int REPORT_BEWEGUNGSVORSCHAU_INTERNEBESTELLUNG_BERUECKSICHTIGT_LAGER = 8;
	private static int REPORT_BEWEGUNGSVORSCHAU_BESTELLVORSCHLAG_BERUECKSICHTIGT_LAGER = 9;
	private static int REPORT_BEWEGUNGSVORSCHAU_BEST_ABTERMIN = 10;
	private static int REPORT_BEWEGUNGSVORSCHAU_BEST_ABNUMMER = 11;
	private static int REPORT_BEWEGUNGSVORSCHAU_LOS_AUFTRAGSNUMMER = 12;
	private static int REPORT_BEWEGUNGSVORSCHAU_MANDANT = 13;
	private static int REPORT_BEWEGUNGSVORSCHAU_ANZAHL_FELDER = 14;

	private static final int REPORT_AUFGELOESTEFEHLMENGEN_ARTIKEL = 0;
	private static final int REPORT_AUFGELOESTEFEHLMENGEN_MENGE = 1;
	private static final int REPORT_AUFGELOESTEFEHLMENGEN_EINHEIT = 2;
	private static final int REPORT_AUFGELOESTEFEHLMENGEN_BEZEICHNUNG = 3;
	private static final int REPORT_AUFGELOESTEFEHLMENGEN_LAGER = 4;
	private static final int REPORT_AUFGELOESTEFEHLMENGEN_SNRCHNR = 5;
	private static final int REPORT_AUFGELOESTEFEHLMENGEN_ARTIKELKLASSE = 6;
	private static final int REPORT_AUFGELOESTEFEHLMENGEN_ZUSATZBEZEICHNUNG = 7;
	private static final int REPORT_AUFGELOESTEFEHLMENGEN_ZUSATZBEZEICHNUNG2 = 8;
	private static final int REPORT_AUFGELOESTEFEHLMENGEN_FARBCODE = 9;
	private static final int REPORT_AUFGELOESTEFEHLMENGEN_MATERIAL = 10;
	private static final int REPORT_AUFGELOESTEFEHLMENGEN_HOEHE = 11;
	private static final int REPORT_AUFGELOESTEFEHLMENGEN_BREITE = 12;
	private static final int REPORT_AUFGELOESTEFEHLMENGEN_TIEFE = 13;
	private static final int REPORT_AUFGELOESTEFEHLMENGEN_BAUFORM = 14;
	private static final int REPORT_AUFGELOESTEFEHLMENGEN_VERPACKUNGSART = 15;
	private static final int REPORT_AUFGELOESTEFEHLMENGEN_GEWICHTKG = 16;
	private static final int REPORT_AUFGELOESTEFEHLMENGEN_RASTERSTEHEND = 17;
	private static final int REPORT_AUFGELOESTEFEHLMENGEN_LAGERORT = 18;
	private static final int REPORT_AUFGELOESTEFEHLMENGEN_LIEFERSCHEIN = 19;
	private static final int REPORT_AUFGELOESTEFEHLMENGEN_ANZAHL_SPALTEN = 20;

	private static int REPORT_VKPREISLISTE_ARTIKELNUMMER = 0;
	private static int REPORT_VKPREISLISTE_BEZEICHNUNG = 1;
	private static int REPORT_VKPREISLISTE_KURZBEZEICHNUNG = 2;
	private static int REPORT_VKPREISLISTE_ZUSATZBEZEICHNUNG = 3;
	private static int REPORT_VKPREISLISTE_ZUSATZBEZEICHNUNG2 = 4;
	private static int REPORT_VKPREISLISTE_VKPREIS = 5;
	private static int REPORT_VKPREISLISTE_KOMMENTAR = 6;
	private static int REPORT_VKPREISLISTE_EINHEIT = 7;
	private static int REPORT_VKPREISLISTE_ARTIKELGRUPPE = 8;
	private static int REPORT_VKPREISLISTE_ARTIKELKLASSE = 9;
	private static int REPORT_VKPREISLISTE_STAFFEL1 = 10;
	private static int REPORT_VKPREISLISTE_PREISSTAFFEL1 = 11;
	private static int REPORT_VKPREISLISTE_STAFFEL2 = 12;
	private static int REPORT_VKPREISLISTE_PREISSTAFFEL2 = 13;
	private static int REPORT_VKPREISLISTE_STAFFEL3 = 14;
	private static int REPORT_VKPREISLISTE_PREISSTAFFEL3 = 15;
	private static int REPORT_VKPREISLISTE_STAFFEL4 = 16;
	private static int REPORT_VKPREISLISTE_PREISSTAFFEL4 = 17;
	private static int REPORT_VKPREISLISTE_STAFFEL5 = 18;
	private static int REPORT_VKPREISLISTE_PREISSTAFFEL5 = 19;
	private static int REPORT_VKPREISLISTE_VERSTECKT = 20;
	private static int REPORT_VKPREISLISTE_SHOPGRUPPE = 21;
	private static int REPORT_VKPREISLISTE_MATERIALZUSCHLAG = 22;
	private static int REPORT_VKPREISLISTE_ANZAHL_SPALTEN = 23;

	private static int REPORT_AENDERUNGEN_EIGENSCHAFT = 0;
	private static int REPORT_AENDERUNGEN_VON = 1;
	private static int REPORT_AENDERUNGEN_NACH = 2;
	private static int REPORT_AENDERUNGEN_WER = 3;
	private static int REPORT_AENDERUNGEN_WANN = 4;
	private static int REPORT_AENDERUNGEN_SPRACHE = 5;
	private static int REPORT_AENDERUNGEN_ANZAHL_SPALTEN = 6;

	private static int REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_ARTIKELNUMMER = 0;
	private static int REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_BEZEICHNUNG = 1;
	private static int REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_ZUSATZBEZEICHNUNG = 2;
	private static int REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_EINHEIT = 3;
	private static int REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LOS_NUMMER = 4;
	private static int REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LOS_PROJEKT = 5;
	private static int REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LOS_BEGINN = 6;
	private static int REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LOS_ENDE = 7;
	private static int REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LOS_KOMMENTAR = 8;
	private static int REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LOS_STKLNUMMER = 9;
	private static int REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LOS_STKLBEZEICHUNG = 10;
	private static int REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LOS_STKLZUSATZBEZEICHNUNG = 11;
	private static int REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_MENGE = 12;
	private static int REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LAGER = 13;
	private static int REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LAGERPLAETZE = 14;
	private static int REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_TYP = 15;
	private static int REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_ZIEL = 16;
	private static int REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_QUELLE = 17;
	private static int REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LAGERSTAND = 18;
	private static int REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_ANZAHL_SPALTEN = 19;

	private static int REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_ARTIKELNUMMER = 0;
	private static int REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_BEZEICHNUNG = 1;
	private static int REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_ZUSATZBEZEICHNUNG = 2;
	private static int REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_EINHEIT = 3;
	private static int REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LOS_NUMMER = 4;
	private static int REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LOS_PROJEKT = 5;
	private static int REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LOS_BEGINN = 6;
	private static int REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LOS_ENDE = 7;
	private static int REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LOS_KOMMENTAR = 8;
	private static int REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LOS_STKLNUMMER = 9;
	private static int REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LOS_STKLBEZEICHUNG = 10;
	private static int REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LOS_STKLZUSATZBEZEICHNUNG = 11;
	private static int REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_UMZUBUCHENDE_MENGE = 12;
	private static int REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LAGERSTAND_KUMULIERT = 13;
	private static int REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LAGER = 14;
	private static int REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LAGERPLAETZE = 15;
	private static int REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LAGERBEWIRTSCHAFTET = 16;
	private static int REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_BESTELLNUMMER = 17;
	private static int REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_WARENEINGANG = 18;
	private static int REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_ANZAHL_SPALTEN = 19;

	private static int REPORT_RAHMENBEST_BESTELLUNGSNUMMER = 0;
	private static int REPORT_RAHMENBEST_LIEFERANT = 1;
	private static int REPORT_RAHMENBEST_PROJEKT = 2;
	private static int REPORT_RAHMENBEST_LIEFERTERMIN = 3;
	private static int REPORT_RAHMENBEST_RAHMENMENGE = 4;
	private static int REPORT_RAHMENBEST_OFFENE_MENGE = 5;
	private static int REPORT_RAHMENBEST_AB_TERMIN = 7;
	private static int REPORT_RAHMENBEST_AB_NUMMER = 8;
	private static int REPORT_RAHMENBEST_AB_KOMMENTAR = 9;
	private static int REPORT_RAHMENBEST_PREIS = 10;
	private static int REPORT_RAHMENBEST_ANZAHL_SPALTEN = 11;

	private static int REPORT_BESTELLTLISTE_BESTELLUNG = 0;
	private static int REPORT_BESTELLTLISTE_LIEFERANTENNAME = 1;
	private static int REPORT_BESTELLTLISTE_PROJEKTNAME = 2;
	private static int REPORT_BESTELLTLISTE_LIEFERTERMIN = 3;
	private static int REPORT_BESTELLTLISTE_MENGE = 4;
	private static int REPORT_BESTELLTLISTE_BESTELLMENGE = 5;
	private static int REPORT_BESTELLTLISTE_OFFENEMENGE = 6;
	private static int REPORT_BESTELLTLISTE_AB_TERMIN = 7;
	private static int REPORT_BESTELLTLISTE_AB_NUMMER = 8;
	private static int REPORT_BESTELLTLISTE_AB_KOMMENTAR = 9;

	public final static int REPORT_RAHMENRESERVIERUNG_AUFTRAGCNR = 0;
	public final static int REPORT_RAHMENRESERVIERUNG_KUNDECNAME1 = 1;
	public final static int REPORT_RAHMENRESERVIERUNG_AUFTRAGCBEZ = 2;
	public final static int REPORT_RAHMENRESERVIERUNG_AUFTRAGPOSITIONRAHMENTERMIN = 3;
	public final static int REPORT_RAHMENRESERVIERUNG_AUFTRAGPOSITIONMENGE = 4;
	public final static int REPORT_RAHMENRESERVIERUNG_AUFTRAGPOSITIONOFFENEMENGE = 5;
	public final static int REPORT_RAHMENRESERVIERUNG_AUFTRAGPOSITIONGELIEFERT = 6;

	private static int REPORT_ARTIKELSTATISTIK_BELEGART = 0;
	private static int REPORT_ARTIKELSTATISTIK_BELEGNUMMER = 1;
	private static int REPORT_ARTIKELSTATISTIK_FIRMA = 2;
	private static int REPORT_ARTIKELSTATISTIK_BELEGDATUM = 3;
	private static int REPORT_ARTIKELSTATISTIK_MENGE = 4;
	private static int REPORT_ARTIKELSTATISTIK_PREIS = 5;
	private static int REPORT_ARTIKELSTATISTIK_LIEFERTERMIN = 6;
	private static int REPORT_ARTIKELSTATISTIK_SNRCHNR = 7;
	private static int REPORT_ARTIKELSTATISTIK_LAGER = 8;
	private static int REPORT_ARTIKELSTATISTIK_BUCHUNGSZEIT = 9;
	private static int REPORT_ARTIKELSTATISTIK_BELEGARTCNR = 10;
	private static int REPORT_ARTIKELSTATISTIK_STUECKLISTENARTIKEL = 11;
	private static int REPORT_ARTIKELSTATISTIK_INVENTURMENGE = 12;
	private static int REPORT_ARTIKELSTATISTIK_WE_REFERENZ = 13;
	private static int REPORT_ARTIKELSTATISTIK_HERSTELLER = 14;
	private static int REPORT_ARTIKELSTATISTIK_URSPRUNGSLAND = 15;
	private static int REPORT_ARTIKELSTATISTIK_VERLEIHTAGE = 16;
	private static int REPORT_ARTIKELSTATISTIK_VERLEIHFAKTOR = 17;

	private static int REPORT_ARTIKELSTATISTIK_LOS_STATUS = 18;
	private static int REPORT_ARTIKELSTATISTIK_LOS_AUFTRAG = 19;
	private static int REPORT_ARTIKELSTATISTIK_LOS_PROJEKT = 20;
	private static int REPORT_ARTIKELSTATISTIK_LOS_ABGELIFERT = 21;
	private static int REPORT_ARTIKELSTATISTIK_LOS_BEGINN = 22;
	private static int REPORT_ARTIKELSTATISTIK_LOS_ENDE = 23;
	private static int REPORT_ARTIKELSTATISTIK_AUFTRAG_AUSLOESER = 24;
	private static int REPORT_ARTIKELSTATISTIK_BEWEGUNGSVORSCHAU = 25;
	private static int REPORT_ARTIKELSTATISTIK_SORTIERDATUM = 26;
	private static int REPORT_ARTIKELSTATISTIK_MATERIALZUSCHLAG = 27;
	private static int REPORT_ARTIKELSTATISTIK_VERSION = 28;
	private static int REPORT_ARTIKELSTATISTIK_I_ID_BUCHUNG = 29;
	private static int REPORT_ARTIKELSTATISTIK_ANZAHL_SPALTEN = 30;

	private static int REPORT_MONATSSTATISTIK_MONAT = 0;
	private static int REPORT_MONATSSTATISTIK_JAHR = 1;
	private static int REPORT_MONATSSTATISTIK_ZUGANG_MENGE = 2;
	private static int REPORT_MONATSSTATISTIK_ZUGANG_WERT = 3;
	private static int REPORT_MONATSSTATISTIK_ABGANG_MENGE = 4;
	private static int REPORT_MONATSSTATISTIK_ABGANG_WERT = 5;

	private static int REPORT_LAGERPLATZ_LAGERPLATZ = 0;
	private static int REPORT_LAGERPLATZ_LAGER = 1;
	private static int REPORT_LAGERPLATZ_ANZAHL_SPALTEN = 2;

	private static int REPORT_VKPREISENTWICKLUNG_ART = 0;
	private static int REPORT_VKPREISENTWICKLUNG_PREISLISTENNAME = 1;
	private static int REPORT_VKPREISENTWICKLUNG_VKPREISBASIS = 2;
	private static int REPORT_VKPREISENTWICKLUNG_FIXPREIS = 3;
	private static int REPORT_VKPREISENTWICKLUNG_RABATT = 4;
	private static int REPORT_VKPREISENTWICKLUNG_BERECHNETERPREIS = 5;
	private static int REPORT_VKPREISENTWICKLUNG_GUELTIGAB = 6;
	private static int REPORT_VKPREISENTWICKLUNG_GUELTIGBIS = 7;
	private static int REPORT_VKPREISENTWICKLUNG_T_AENDERN = 8;
	private static int REPORT_VKPREISENTWICKLUNG_PERSON_GEAENDERT = 9;
	private static int REPORT_VKPREISENTWICKLUNG_STAFFELMENGE = 10;
	private static int REPORT_VKPREISENTWICKLUNG_MANDANT = 11;
	private static int REPORT_VKPREISENTWICKLUNG_ANZAHL_SPALTEN = 12;

	private static int REPORT_AUFTRAGSERIENNR_AUFTRAG = 0;
	private static int REPORT_AUFTRAGSERIENNR_KUNDE = 1;
	private static int REPORT_AUFTRAGSERIENNR_BELEGDATUM = 2;
	private static int REPORT_AUFTRAGSERIENNR_PROJEKT = 3;
	private static int REPORT_AUFTRAGSERIENNR_SERIENNUMMER = 4;
	private static int REPORT_AUFTRAGSERIENNR_ANZAHL_SPALTEN = 5;

	private static int REPORT_MINDESTLAGERSTAENDE_ARTIKEL = 0;
	private static int REPORT_MINDESTLAGERSTAENDE_BEZEICHNUNG = 1;
	private static int REPORT_MINDESTLAGERSTAENDE_ZUSATZBEZEICHNUNG = 2;
	private static int REPORT_MINDESTLAGERSTAENDE_ZUSATZBEZEICHNUNG2 = 3;
	private static int REPORT_MINDESTLAGERSTAENDE_KURZBEZEICHNUNG = 4;
	private static int REPORT_MINDESTLAGERSTAENDE_LETZTER_ABGANG = 5;
	private static int REPORT_MINDESTLAGERSTAENDE_VK_PREISBASIS = 6;
	private static int REPORT_MINDESTLAGERSTAENDE_LAGERSTAND = 7;
	private static int REPORT_MINDESTLAGERSTAENDE_LAGERMINDESTSTAND = 8;
	private static int REPORT_MINDESTLAGERSTAENDE_LAGERSOLLSTAND = 9;
	private static int REPORT_MINDESTLAGERSTAENDE_RAHMENBESTELLT = 10;
	private static int REPORT_MINDESTLAGERSTAENDE_DETAILBEDARF = 11;
	private static int REPORT_MINDESTLAGERSTAENDE_RAHMENRESERVIERT = 12;
	private static int REPORT_MINDESTLAGERSTAENDE_FERTIGUNGSSATZGROESSE = 13;
	private static int REPORT_MINDESTLAGERSTAENDE_ANZAHL_SPALTEN = 14;

	private static int REPORT_NAECHSTE_WARTUNGEN_ARTIKEL = 0;
	private static int REPORT_NAECHSTE_WARTUNGEN_BEZEICHNUNG = 1;
	private static int REPORT_NAECHSTE_WARTUNGEN_ZUSATZBEZEICHNUNG = 2;
	private static int REPORT_NAECHSTE_WARTUNGEN_ZUSATZBEZEICHNUNG2 = 3;
	private static int REPORT_NAECHSTE_WARTUNGEN_KURZBEZEICHNUNG = 4;
	private static int REPORT_NAECHSTE_WARTUNGEN_REFERENZNUMMER = 5;
	private static int REPORT_NAECHSTE_WARTUNGEN_REVISION = 6;
	private static int REPORT_NAECHSTE_WARTUNGEN_INDEX = 7;
	private static int REPORT_NAECHSTE_WARTUNGEN_ARTIKELGRUPPE = 8;
	private static int REPORT_NAECHSTE_WARTUNGEN_ARTIKELKLASSE = 9;
	private static int REPORT_NAECHSTE_WARTUNGEN_LAGERPLATZ = 10;
	private static int REPORT_NAECHSTE_WARTUNGEN_LAGER_LAGERPLATZ = 11;
	private static int REPORT_NAECHSTE_WARTUNGEN_WARTUNGSINTERVALL = 12;
	private static int REPORT_NAECHSTE_WARTUNGEN_FAELLIG_SEIT = 13;
	private static int REPORT_NAECHSTE_WARTUNGEN_LETZTE_WARTUNG = 14;
	private static int REPORT_NAECHSTE_WARTUNGEN_ANZAHL_SPALTEN = 15;

	private static int REPORT_ALLERGENE_ARTIKELNUMMER = 0;
	private static int REPORT_ALLERGENE_BEZEICHNUNG = 1;
	private static int REPORT_ALLERGENE_SUBREPORT_ENTHALTENE_ALLERGENE = 2;

	private static int REPORT_ALLERGENE_ANZAHL_SPALTEN = 3;

	public Object getFieldValue(JRField jRField) throws JRException {
		Object value = null;
		String fieldName = jRField.getName();
		if (sAktuellerReport.equals(ArtikelReportFac.REPORT_ARTIKELSTATISTIK)) {
			if ("Belegart".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_BELEGART];
			} else if ("Belegnummer".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_BELEGNUMMER];
			} else if ("Firma".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_FIRMA];
			} else if ("Buchungszeit".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_BUCHUNGSZEIT];
			} else if ("Belegdatum".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_BELEGDATUM];
			} else if ("Sortierdatum".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_SORTIERDATUM];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_MENGE];
			} else if ("Liefertermin".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_LIEFERTERMIN];
			} else if ("Preis".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_PREIS];
			} else if ("Materialzuschlag".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_MATERIALZUSCHLAG];
			} else if ("Snrchnr".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_SNRCHNR];
			} else if ("Version".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_VERSION];
			} else if ("Lager".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_LAGER];
			} else if ("F_BELEGARTCNR".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_BELEGARTCNR];
			} else if ("F_STUECKLISTENARTIKEL".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_STUECKLISTENARTIKEL];
			} else if ("Inventurmenge".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_INVENTURMENGE];
			} else if ("F_WE_REFERENZ".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_WE_REFERENZ];
			} else if ("F_HERSTELLER".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_HERSTELLER];
			} else if ("F_URSPRUNGSLAND".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_URSPRUNGSLAND];
			} else if ("F_VERLEIHTAGE".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_VERLEIHTAGE];
			} else if ("F_VERLEIHFAKTOR".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_VERLEIHFAKTOR];
			} else if ("F_MATERIALZUSCHLAG".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_MATERIALZUSCHLAG];
			} else if ("F_LOS_STATUS".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_LOS_STATUS];
			} else if ("F_LOS_AUFTRAG".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_LOS_AUFTRAG];
			} else if ("F_LOS_PROJEKT".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_LOS_PROJEKT];
			} else if ("F_LOS_ABGELIFERT".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_LOS_ABGELIFERT];
			} else if ("F_LOS_BEGINN".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_LOS_BEGINN];
			} else if ("F_LOS_ENDE".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_LOS_ENDE];
			} else if ("F_AUFTRAG_AUSLOESER".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_AUFTRAG_AUSLOESER];
			} else if ("F_BEWEGUNGSVORSCHAU".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_BEWEGUNGSVORSCHAU];
			} else if ("F_I_ID_BUCHUNG".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_I_ID_BUCHUNG];
			}

		} else if (sAktuellerReport
				.equals(ArtikelReportFac.REPORT_MONATSSTATISTIK)) {
			if ("Jahr".equals(fieldName)) {
				value = data[index][REPORT_MONATSSTATISTIK_JAHR];
			} else if ("Monat".equals(fieldName)) {
				value = data[index][REPORT_MONATSSTATISTIK_MONAT];
			} else if ("Zugangsmenge".equals(fieldName)) {
				value = data[index][REPORT_MONATSSTATISTIK_ZUGANG_MENGE];
			} else if ("Zugangswert".equals(fieldName)) {
				value = data[index][REPORT_MONATSSTATISTIK_ZUGANG_WERT];
			} else if ("Abgangsmenge".equals(fieldName)) {
				value = data[index][REPORT_MONATSSTATISTIK_ABGANG_MENGE];
			} else if ("Abgangswert".equals(fieldName)) {
				value = data[index][REPORT_MONATSSTATISTIK_ABGANG_WERT];
			}
		} else if (sAktuellerReport.equals(ArtikelReportFac.REPORT_ALLERGENE)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_ALLERGENE_ARTIKELNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_ALLERGENE_BEZEICHNUNG];
			} else if ("SubreportEnthalteneAllergene".equals(fieldName)) {
				value = data[index][REPORT_ALLERGENE_SUBREPORT_ENTHALTENE_ALLERGENE];
			}
		} else if (sAktuellerReport.equals(ArtikelReportFac.REPORT_KUNDENSOKOS)) {
			if ("KundeName1".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSOKOS_KUNDE_NAME1];
			} else if ("KundeName2".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSOKOS_KUNDE_NAME2];
			} else if ("Lkz".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSOKOS_KUNDE_LKZ];
			} else if ("Plz".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSOKOS_KUNDE_PLZ];
			} else if ("Ort".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSOKOS_KUNDE_ORT];
			} else if ("KdArtikelnummer".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSOKOS_KUNDE_KDARTIKELNUMMER];
			} else if ("KdArtikelbez".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSOKOS_KUNDE_KDARTIKELBEZ];
			} else if ("KdArtikelbez2".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSOKOS_KUNDE_KDARTIKELBEZ2];
			} else if ("Gueltigab".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSOKOS_KUNDE_GUELTIAGAB];
			} else if ("Gueltigbis".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSOKOS_KUNDE_GUELTIAGBIS];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSOKOS_KUNDE_MENGE];
			} else if ("Fixpreis".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSOKOS_KUNDE_FIXPREIS];
			} else if ("Rabatt".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSOKOS_KUNDE_RABATT];
			} else if ("BerechneterPreis".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSOKOS_KUNDE_BERECHNETER_PREIS];
			} else if ("NaechsteVKPreisbasisGueltigab".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSOKOS_KUNDE_NAECHSTE_VKPREISBASIS_GUELTIG_AB];
			} else if ("KundeKbez".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSOKOS_KUNDE_KBEZ];
			} else if ("KundeUID".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSOKOS_KUNDE_UID];
			}
		} else if (sAktuellerReport
				.equals(ArtikelReportFac.REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_ARTIKELNUMMER];
			} else if ("Bezeichung".equals(fieldName)) {
				value = data[index][REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_BEZEICHNUNG];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_ZUSATZBEZEICHNUNG];
			} else if ("Einheit".equals(fieldName)) {
				value = data[index][REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_EINHEIT];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_MENGE];
			} else if ("Lager".equals(fieldName)) {
				value = data[index][REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LAGER];
			} else if ("Lagerstand".equals(fieldName)) {
				value = data[index][REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LAGERSTAND];
			} else if ("Lagerplaetze".equals(fieldName)) {
				value = data[index][REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LAGERPLAETZE];
			} else if ("LosBeginn".equals(fieldName)) {
				value = data[index][REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LOS_BEGINN];
			} else if ("LosEnde".equals(fieldName)) {
				value = data[index][REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LOS_ENDE];
			} else if ("LosKommentar".equals(fieldName)) {
				value = data[index][REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LOS_KOMMENTAR];
			} else if ("Losnummer".equals(fieldName)) {
				value = data[index][REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LOS_NUMMER];
			} else if ("LosProjekt".equals(fieldName)) {
				value = data[index][REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LOS_PROJEKT];
			} else if ("LosStklbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LOS_STKLBEZEICHUNG];
			} else if ("LosStklnummer".equals(fieldName)) {
				value = data[index][REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LOS_STKLNUMMER];
			} else if ("LosStklzusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LOS_STKLZUSATZBEZEICHNUNG];
			} else if ("Typ".equals(fieldName)) {
				value = data[index][REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_TYP];
			} else if ("Ziel".equals(fieldName)) {
				value = data[index][REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_ZIEL];
			} else if ("Quelle".equals(fieldName)) {
				value = data[index][REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_QUELLE];
			}
		} else if (sAktuellerReport
				.equals(ArtikelReportFac.REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_ARTIKELNUMMER];
			} else if ("Lagerbewirtschaftet".equals(fieldName)) {
				value = data[index][REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LAGERBEWIRTSCHAFTET];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_BEZEICHNUNG];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_ZUSATZBEZEICHNUNG];
			} else if ("Einheit".equals(fieldName)) {
				value = data[index][REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_EINHEIT];
			} else if ("UmzubuchendeMenge".equals(fieldName)) {
				value = data[index][REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_UMZUBUCHENDE_MENGE];
			} else if ("Lager".equals(fieldName)) {
				value = data[index][REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LAGER];
			} else if ("LagerstandKumuliert".equals(fieldName)) {
				value = data[index][REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LAGERSTAND_KUMULIERT];
			} else if ("Lagerplaetze".equals(fieldName)) {
				value = data[index][REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LAGERPLAETZE];
			} else if ("LosBeginn".equals(fieldName)) {
				value = data[index][REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LOS_BEGINN];
			} else if ("LosEnde".equals(fieldName)) {
				value = data[index][REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LOS_ENDE];
			} else if ("LosKommentar".equals(fieldName)) {
				value = data[index][REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LOS_KOMMENTAR];
			} else if ("Losnummer".equals(fieldName)) {
				value = data[index][REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LOS_NUMMER];
			} else if ("LosProjekt".equals(fieldName)) {
				value = data[index][REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LOS_PROJEKT];
			} else if ("LosStklbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LOS_STKLBEZEICHUNG];
			} else if ("LosStklnummer".equals(fieldName)) {
				value = data[index][REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LOS_STKLNUMMER];
			} else if ("LosStklzusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LOS_STKLZUSATZBEZEICHNUNG];
			} else if ("Bestellnummer".equals(fieldName)) {
				value = data[index][REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_BESTELLNUMMER];
			} else if ("Wareneingang".equals(fieldName)) {
				value = data[index][REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_WARENEINGANG];
			}
		} else if (sAktuellerReport
				.equals(ArtikelReportFac.REPORT_AUFTRAGSSERIENNUMMERN)) {
			if ("Auftrag".equals(fieldName)) {
				value = data[index][REPORT_AUFTRAGSERIENNR_AUFTRAG];
			} else if ("Belegdatum".equals(fieldName)) {
				value = data[index][REPORT_AUFTRAGSERIENNR_BELEGDATUM];
			} else if ("Projekt".equals(fieldName)) {
				value = data[index][REPORT_AUFTRAGSERIENNR_PROJEKT];
			} else if ("Kunde".equals(fieldName)) {
				value = data[index][REPORT_AUFTRAGSERIENNR_KUNDE];
			} else if ("Seriennummer".equals(fieldName)) {
				value = data[index][REPORT_AUFTRAGSERIENNR_SERIENNUMMER];
			}
		} else if (sAktuellerReport.equals(ArtikelReportFac.REPORT_AENDERUNGEN)) {
			if ("Eigenschaft".equals(fieldName)) {
				value = data[index][REPORT_AENDERUNGEN_EIGENSCHAFT];
			} else if ("Nach".equals(fieldName)) {
				value = data[index][REPORT_AENDERUNGEN_NACH];
			} else if ("Von".equals(fieldName)) {
				value = data[index][REPORT_AENDERUNGEN_VON];
			} else if ("Wer".equals(fieldName)) {
				value = data[index][REPORT_AENDERUNGEN_WER];
			} else if ("Wann".equals(fieldName)) {
				value = data[index][REPORT_AENDERUNGEN_WANN];
			} else if ("Sprache".equals(fieldName)) {
				value = data[index][REPORT_AENDERUNGEN_SPRACHE];
			}
		} else if (sAktuellerReport
				.equals(ArtikelReportFac.REPORT_LIEFERANTENPREIS)) {
			if ("Fixkosten".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENPREIS_FIXKOSTEN];
			} else if ("Gueltigab".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENPREIS_GUELTIGAB];
			} else if ("Lieferant".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENPREIS_LIEFERANT];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENPREIS_MENGE];
			} else if ("Mindestbestellmenge".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENPREIS_MINDESTBESTELLMENGE];
			} else if ("Nettopreis".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENPREIS_NETTOPREIS];
			} else if ("Wiederbeschaffungszeit".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENPREIS_WIEDERBESCHAFFUNGSZEIT];
			}
		} else if (sAktuellerReport
				.equals(ArtikelReportFac.REPORT_ARTIKELETIKETT)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_ARTIKELNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_BEZEICHNUNG];
			} else if ("Einheit".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_EINHEIT];
			} else if ("Hersteller".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_HERSTELLER];
			} else if ("HerstellerName1".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_HERSTELLER_NAME1];
			} else if ("VerkaufsEAN".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_VERKAUFS_EAN];
			} else if ("VerpackungsEAN".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_VERPACKUNGS_EAN];
			} else if ("Verpackungsmenge".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_VERPACKUNGSMENGE];
			} else if ("HerstellerName2".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_HERSTELLER_NAME2];
			} else if ("Kurzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_KURZBEZEICHNUNG];
			} else if ("Lagerort".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_LAGERORT];
			} else if ("Lieferant".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_LIEFERANT];
			} else if ("Lagerstand".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_LAGERSTAND];
			} else if ("Referenznummer".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_REFERENZNUMMER];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_ZUSATZBEZEICHNUNG];
			} else if ("Zusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_ZUSATZBEZEICHNUNG2];
			} else if (F_ARTIKELLIEFERANT_ARTIKELNR.equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_LIEFERANT_ARTIKELNUMMER];
			} else if (F_ARTIKELLIEFERANT_BEZ.equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_LIEFERANT_ARTIKELBEZEICHNUNG];
			} else if ("Kommentar".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_KOMMENTAR];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_MENGE];
			} else if ("SnrChnr".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_SNRCHNR];
			} else if ("Version".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_VERSION];
			} else if ("Bauform".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_BAUFORM];
			} else if ("Verpackungsart".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_VERPACKUNGSART];
			} else if ("Breite".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_BREITE];
			} else if ("Hoehe".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_HOEHE];
			} else if ("Tiefe".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_TIEFE];
			} else if ("Material".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_MATERIAL];
			} else if ("Index".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_INDEX];
			} else if ("Revision".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_REVISION];
			} else if ("Mandantadresse".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_MANDANTADRESSE];
			} else if ("PersonBuchender".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_PERSON_BUCHENDER];
			} else if ("KurzzeichenBuchender".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_KURZZEICHEN_BUCHENDER];
			}
		} else if (sAktuellerReport.equals(ArtikelReportFac.REPORT_LOSSTATUS)) {
			if ("Auftrag".equals(fieldName)) {
				value = data[index][REPORT_LOSSTATUS_AUFTRAG];
			} else if ("Beginn".equals(fieldName)) {
				value = data[index][REPORT_LOSSTATUS_BEGINN];
			} else if ("AuftragProjektbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_LOSSTATUS_AUFTRAG_PROJEKT];
			} else if ("Ende".equals(fieldName)) {
				value = data[index][REPORT_LOSSTATUS_ENDE];
			} else if ("Los".equals(fieldName)) {
				value = data[index][REPORT_LOSSTATUS_LOSNR];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_LOSSTATUS_MENGE];
			} else if ("Projektbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_LOSSTATUS_PROJEKT];
			} else if ("Abgeliefert".equals(fieldName)) {
				value = data[index][REPORT_LOSSTATUS_ABGELIEFERT];
			} else if ("Status".equals(fieldName)) {
				value = data[index][REPORT_LOSSTATUS_STATUS];
			} else if ("PreisMaterial".equals(fieldName)) {
				value = data[index][REPORT_LOSSTATUS_PREIS_ABLIEFERUNG_MATERIAL];
			} else if ("PreisZeit".equals(fieldName)) {
				value = data[index][REPORT_LOSSTATUS_PREIS_ABLIEFERUNG_ZEIT];
			} else if ("Angelegt".equals(fieldName)) {
				value = data[index][REPORT_LOSSTATUS_ANGELEGT];
			} else if ("Erledigt".equals(fieldName)) {
				value = data[index][REPORT_LOSSTATUS_ERLEDIGT];
			} else if ("Ausgegeben".equals(fieldName)) {
				value = data[index][REPORT_LOSSTATUS_AUSGEGEBEN];
			} else if ("Kunde".equals(fieldName)) {
				value = data[index][REPORT_LOSSTATUS_KUNDE];
			} else if ("Fertigungsgruppe".equals(fieldName)) {
				value = data[index][REPORT_LOSSTATUS_FERTIGUNGSGRUPPE];
			} else if ("Techniker".equals(fieldName)) {
				value = data[index][REPORT_LOSSTATUS_TECHNIKER];
			} else if ("Ziellager".equals(fieldName)) {
				value = data[index][REPORT_LOSSTATUS_ZIELLAGER];
			}

		} else if (sAktuellerReport
				.equals(ArtikelReportFac.REPORT_LAGERPLATZETIKETT)) {
			if ("Lagerplatz".equals(fieldName)) {
				value = data[index][REPORT_LAGERPLATZ_LAGERPLATZ];
			} else if ("Lager".equals(fieldName)) {
				value = data[index][REPORT_LAGERPLATZ_LAGER];
			}
		} else if (sAktuellerReport
				.equals(ArtikelReportFac.REPORT_VKPREISLISTE)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_VKPREISLISTE_ARTIKELNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_VKPREISLISTE_BEZEICHNUNG];
			} else if ("Vkpreis".equals(fieldName)) {
				value = data[index][REPORT_VKPREISLISTE_VKPREIS];
			} else if ("Kurzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_VKPREISLISTE_KURZBEZEICHNUNG];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_VKPREISLISTE_ZUSATZBEZEICHNUNG];
			} else if ("Zusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_VKPREISLISTE_ZUSATZBEZEICHNUNG2];
			} else if ("Einheit".equals(fieldName)) {
				value = data[index][REPORT_VKPREISLISTE_EINHEIT];
			} else if ("Artikelgruppe".equals(fieldName)) {
				value = data[index][REPORT_VKPREISLISTE_ARTIKELGRUPPE];
			} else if ("Shopgruppe".equals(fieldName)) {
				value = data[index][REPORT_VKPREISLISTE_SHOPGRUPPE];
			} else if ("Artikelklasse".equals(fieldName)) {
				value = data[index][REPORT_VKPREISLISTE_ARTIKELKLASSE];
			} else if ("Materialzuschlag".equals(fieldName)) {
				value = data[index][REPORT_VKPREISLISTE_MATERIALZUSCHLAG];
			}

			else if ("Staffel1".equals(fieldName)) {
				value = data[index][REPORT_VKPREISLISTE_STAFFEL1];
			} else if ("Staffel2".equals(fieldName)) {
				value = data[index][REPORT_VKPREISLISTE_STAFFEL2];
			} else if ("Staffel3".equals(fieldName)) {
				value = data[index][REPORT_VKPREISLISTE_STAFFEL3];
			} else if ("Staffel4".equals(fieldName)) {
				value = data[index][REPORT_VKPREISLISTE_STAFFEL4];
			} else if ("Staffel5".equals(fieldName)) {
				value = data[index][REPORT_VKPREISLISTE_STAFFEL5];
			} else if ("Staffelpreis1".equals(fieldName)) {
				value = data[index][REPORT_VKPREISLISTE_PREISSTAFFEL1];
			} else if ("Staffelpreis2".equals(fieldName)) {
				value = data[index][REPORT_VKPREISLISTE_PREISSTAFFEL2];
			} else if ("Staffelpreis3".equals(fieldName)) {
				value = data[index][REPORT_VKPREISLISTE_PREISSTAFFEL3];
			} else if ("Staffelpreis4".equals(fieldName)) {
				value = data[index][REPORT_VKPREISLISTE_PREISSTAFFEL4];
			} else if ("Staffelpreis5".equals(fieldName)) {
				value = data[index][REPORT_VKPREISLISTE_PREISSTAFFEL5];
			} else if ("Versteckt".equals(fieldName)) {
				value = data[index][REPORT_VKPREISLISTE_VERSTECKT];
			}

		} else if (sAktuellerReport
				.equals(ArtikelReportFac.REPORT_FREIINFERTIGUNG)) {
			if ("Auftrag".equals(fieldName)) {
				value = data[index][REPORT_FREIINFERTIGUNG_AUFTRAG];
			} else if ("Beginn".equals(fieldName)) {
				value = data[index][REPORT_FREIINFERTIGUNG_BEGINN];
			} else if ("Ende".equals(fieldName)) {
				value = data[index][REPORT_FREIINFERTIGUNG_ENDE];
			} else if ("Los".equals(fieldName)) {
				value = data[index][REPORT_FREIINFERTIGUNG_LOSNR];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_FREIINFERTIGUNG_FREIEMENGE];
			} else if ("Projektbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_FREIINFERTIGUNG_PROJEKT];
			}
		} else if (sAktuellerReport
				.equals(ArtikelReportFac.REPORT_NAECHSTE_WARTUNGEN)) {
			if ("Artikel".equals(fieldName)) {
				value = data[index][REPORT_NAECHSTE_WARTUNGEN_ARTIKEL];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_NAECHSTE_WARTUNGEN_BEZEICHNUNG];
			} else if ("Wartungsintervall".equals(fieldName)) {
				value = data[index][REPORT_NAECHSTE_WARTUNGEN_WARTUNGSINTERVALL];
			} else if ("FaelligSeit".equals(fieldName)) {
				value = data[index][REPORT_NAECHSTE_WARTUNGEN_FAELLIG_SEIT];
			} else if ("Lager".equals(fieldName)) {
				value = data[index][REPORT_NAECHSTE_WARTUNGEN_LAGER_LAGERPLATZ];
			} else if ("Lagerplatz".equals(fieldName)) {
				value = data[index][REPORT_NAECHSTE_WARTUNGEN_LAGERPLATZ];
			}

			else if ("Artikelgruppe".equals(fieldName)) {
				value = data[index][REPORT_NAECHSTE_WARTUNGEN_ARTIKELGRUPPE];
			} else if ("Artikelklasse".equals(fieldName)) {
				value = data[index][REPORT_NAECHSTE_WARTUNGEN_ARTIKELKLASSE];
			} else if ("LetzteWartung".equals(fieldName)) {
				value = data[index][REPORT_NAECHSTE_WARTUNGEN_LETZTE_WARTUNG];
			} else if ("Revision".equals(fieldName)) {
				value = data[index][REPORT_NAECHSTE_WARTUNGEN_REVISION];
			} else if ("Index".equals(fieldName)) {
				value = data[index][REPORT_NAECHSTE_WARTUNGEN_INDEX];
			} else if ("Kurzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_NAECHSTE_WARTUNGEN_KURZBEZEICHNUNG];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_NAECHSTE_WARTUNGEN_ZUSATZBEZEICHNUNG];
			} else if ("Zusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_NAECHSTE_WARTUNGEN_ZUSATZBEZEICHNUNG2];
			} else if ("Referenznummer".equals(fieldName)) {
				value = data[index][REPORT_NAECHSTE_WARTUNGEN_REFERENZNUMMER];
			}

		} else if (sAktuellerReport
				.equals(ArtikelReportFac.REPORT_ARTIKELFEHLMENGE)) {

			if ("Los".equals(fieldName)) {
				value = data[index][REPORT_FEHLMENGENLISTE_LOS];
			} else if ("Projektname".equals(fieldName)) {
				value = data[index][REPORT_FEHLMENGENLISTE_PROJEKTNAME];
			} else if ("Liefertermin".equals(fieldName)) {
				value = data[index][REPORT_FEHLMENGENLISTE_LIEFERTERMIN];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_FEHLMENGENLISTE_MENGE];
			} else if ("Kunde".equals(fieldName)) {
				value = data[index][REPORT_FEHLMENGENLISTE_KUNDE];
			} else if ("Stueckliste".equals(fieldName)) {
				value = data[index][REPORT_FEHLMENGENLISTE_STUECKLISTE_NUMMER];
			} else if ("StuecklisteBezeichnung".equals(fieldName)) {
				value = data[index][REPORT_FEHLMENGENLISTE_STUECKLISTE_BEZEICHNUNG];
			} else if ("LosBeginn".equals(fieldName)) {
				value = data[index][REPORT_FEHLMENGENLISTE_LOSBEGINN];
			} else if ("LosEnde".equals(fieldName)) {
				value = data[index][REPORT_FEHLMENGENLISTE_LOSENDE];
			} else if ("AB_Liefertermin".equals(fieldName)) {
				value = data[index][REPORT_FEHLMENGENLISTE_ABLIEFERTERMIN];
			}
		} else if (sAktuellerReport
				.equals(ArtikelReportFac.REPORT_ARTIKELSTAMMBLATT)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTAMMBLATT_ARTIKELNUMMER];
			} else if ("Bild".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTAMMBLATT_BILD];
			} else if ("Artikelnrlieferant".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTAMMBLATT_ARTIKELNUMMERLIEFERANT];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTAMMBLATT_BEZEICHNUNG];
			} else if ("Eannummer".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTAMMBLATT_EAN];
			} else if ("Ekpreis".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTAMMBLATT_EINKAUFSPREIS];
			} else if ("Gewicht".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTAMMBLATT_GEWICHT];
			} else if ("Kurzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTAMMBLATT_KURZBEZEICHNUNG];
			} else if ("Langtext".equals(fieldName)) {
				value = Helper
						.formatStyledTextForJasper(data[index][REPORT_ARTIKELSTAMMBLATT_LANGTEXT]);
			} else if ("Lieferant".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTAMMBLATT_LIEFERANT];
			} else if ("Material".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTAMMBLATT_MATERIAL];
			} else if ("Vkpreis".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTAMMBLATT_VERKAUFSPREIS];
			} else if ("Verpackungseinheit".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTAMMBLATT_VERPACKUNGSEINHEIT];
			} else if ("Warenverkehrsnummer".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTAMMBLATT_WARENVERKEHRSNUMMER];
			} else if ("Eccn".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTAMMBLATT_ECCN];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTAMMBLATT_ZUSATZBEZEICHNUNG];
			} else if ("Lagerstand".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTAMMBLATT_LAGERSTAND];
			} else if ("Lagerplatz".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTAMMBLATT_LAGERPLATZ];
			}

			else if ("Reserviert".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTAMMBLATT_RESERVIERT];
			} else if ("Fehlmenge".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTAMMBLATT_FEHLMENGE];
			} else if ("Infertigung".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTAMMBLATT_INFERTIGUNG];
			} else if ("Bestellt".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTAMMBLATT_BESTELLT];
			} else if ("Rahmenreserviert".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTAMMBLATT_RAHMENRESERVIERT];
			} else if ("Rahmenbestellt".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTAMMBLATT_RAHMENBESTELLT];
			} else if ("Detailbedarf".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTAMMBLATT_DETAILBEDARF];
			} else if ("DatenSubreport".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTAMMBLATT_SUBREPORT_LAGERSTAENDE];
			}

		} else if (sAktuellerReport
				.equals(ArtikelReportFac.REPORT_VERWENDUNGSNACHWEIS)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_VERWENDUNGSNACHWEIS_STUECKLISTE];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_VERWENDUNGSNACHWEIS_BEZEICHNUNG];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_VERWENDUNGSNACHWEIS_MENGE];
			} else if ("Zusatz".equals(fieldName)) {
				value = data[index][REPORT_VERWENDUNGSNACHWEIS_ZUSATZ];
			} else if ("Arbeitsgang".equals(fieldName)) {
				value = data[index][REPORT_VERWENDUNGSNACHWEIS_ARBEITSGANG];
			} else if ("Einheit".equals(fieldName)) {
				value = data[index][REPORT_VERWENDUNGSNACHWEIS_EINHEIT];
			} else if ("Letzteverwendung".equals(fieldName)) {
				value = data[index][REPORT_VERWENDUNGSNACHWEIS_LETZTE_VERWENDUNG];
			} else if ("Sperren".equals(fieldName)) {
				value = data[index][REPORT_VERWENDUNGSNACHWEIS_SPERREN];
			} else if ("Verbrauchtemenge".equals(fieldName)) {
				value = data[index][REPORT_VERWENDUNGSNACHWEIS_VERBRAUCHTEMENGE];
			} else if ("Versteckt".equals(fieldName)) {
				value = data[index][REPORT_VERWENDUNGSNACHWEIS_VERSTECKT];
			} else if ("Stueckzeit".equals(fieldName)) {
				value = data[index][REPORT_VERWENDUNGSNACHWEIS_STUECKZEIT];
			} else if ("Ruestzeit".equals(fieldName)) {
				value = data[index][REPORT_VERWENDUNGSNACHWEIS_RUESTZEIT];
			} else if ("Lagerstand".equals(fieldName)) {
				value = data[index][REPORT_VERWENDUNGSNACHWEIS_LAGERSTAND];
			} else if ("Reserviert".equals(fieldName)) {
				value = data[index][REPORT_VERWENDUNGSNACHWEIS_RESERVIERT];
			} else if ("Rahmenreserviert".equals(fieldName)) {
				value = data[index][REPORT_VERWENDUNGSNACHWEIS_RAHMENRESERVIERT];
			} else if ("InFertigung".equals(fieldName)) {
				value = data[index][REPORT_VERWENDUNGSNACHWEIS_IN_FERTIGUNG];
			} else if ("Fehlmengen".equals(fieldName)) {
				value = data[index][REPORT_VERWENDUNGSNACHWEIS_FEHLMENGE];
			}

		} else if (sAktuellerReport
				.equals(ArtikelReportFac.REPORT_BEWEGUNGSVORSCHAU)) {
			if ("Belegart".equals(fieldName)) {
				value = data[index][REPORT_BEWEGUNGSVORSCHAU_BELEGART];
			} else if ("Belegnummer".equals(fieldName)) {
				value = data[index][REPORT_BEWEGUNGSVORSCHAU_BELEGNR];
			} else if ("Mandant".equals(fieldName)) {
				value = data[index][REPORT_BEWEGUNGSVORSCHAU_MANDANT];
			} else if ("Liefertermin".equals(fieldName)) {
				value = data[index][REPORT_BEWEGUNGSVORSCHAU_LIEFERTERMIN];
			} else if ("BestellungABTermin".equals(fieldName)) {
				value = data[index][REPORT_BEWEGUNGSVORSCHAU_BEST_ABTERMIN];
			} else if ("BestellungABNummer".equals(fieldName)) {
				value = data[index][REPORT_BEWEGUNGSVORSCHAU_BEST_ABNUMMER];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_BEWEGUNGSVORSCHAU_MENGE];
			} else if ("Partner".equals(fieldName)) {
				value = data[index][REPORT_BEWEGUNGSVORSCHAU_PARTNER];
			} else if ("Fiktiverlagerstand".equals(fieldName)) {
				value = data[index][REPORT_BEWEGUNGSVORSCHAU_FIKTIVERLAGERSTAND];
			} else if ("Projekt".equals(fieldName)) {
				value = data[index][REPORT_BEWEGUNGSVORSCHAU_PROJEKT];
			} else if ("LosAuftrag".equals(fieldName)) {
				value = data[index][REPORT_BEWEGUNGSVORSCHAU_LOS_AUFTRAGSNUMMER];
			} else if ("Finaltermin".equals(fieldName)) {
				value = data[index][REPORT_BEWEGUNGSVORSCHAU_FINALTERMIN];
			} else if ("LAGERWIRDVONINTERNERBESTELLUNGBERUECKSICHTIGT"
					.equals(fieldName)) {
				value = data[index][REPORT_BEWEGUNGSVORSCHAU_INTERNEBESTELLUNG_BERUECKSICHTIGT_LAGER];
			} else if ("LAGERWIRDVONBESTELLVORSCHLAGBERUECKSICHTIGT"
					.equals(fieldName)) {
				value = data[index][REPORT_BEWEGUNGSVORSCHAU_BESTELLVORSCHLAG_BERUECKSICHTIGT_LAGER];
			}
		} else if (sAktuellerReport
				.equals(ArtikelReportFac.REPORT_AUFGELOESTEFEHLMENGEN)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_AUFGELOESTEFEHLMENGEN_ARTIKEL];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_AUFGELOESTEFEHLMENGEN_BEZEICHNUNG];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_AUFGELOESTEFEHLMENGEN_MENGE];
			} else if ("Seriennrchnr".equals(fieldName)) {
				value = data[index][REPORT_AUFGELOESTEFEHLMENGEN_SNRCHNR];
			} else if ("Lager".equals(fieldName)) {
				value = data[index][REPORT_AUFGELOESTEFEHLMENGEN_LAGER];
			} else if ("Einheit".equals(fieldName)) {
				value = data[index][REPORT_AUFGELOESTEFEHLMENGEN_EINHEIT];
			} else if ("Lagerort".equals(fieldName)) {
				value = data[index][REPORT_AUFGELOESTEFEHLMENGEN_LAGERORT];
			} else if ("Artikelklasse".equals(fieldName)) {
				value = data[index][REPORT_AUFGELOESTEFEHLMENGEN_ARTIKELKLASSE];
			} else if ("Farbcode".equals(fieldName)) {
				value = data[index][REPORT_AUFGELOESTEFEHLMENGEN_FARBCODE];
			} else if ("Material".equals(fieldName)) {
				value = data[index][REPORT_AUFGELOESTEFEHLMENGEN_MATERIAL];
			} else if ("Hoehe".equals(fieldName)) {
				value = data[index][REPORT_AUFGELOESTEFEHLMENGEN_HOEHE];
			} else if ("Breite".equals(fieldName)) {
				value = data[index][REPORT_AUFGELOESTEFEHLMENGEN_BREITE];
			} else if ("Tiefe".equals(fieldName)) {
				value = data[index][REPORT_AUFGELOESTEFEHLMENGEN_TIEFE];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_AUFGELOESTEFEHLMENGEN_ZUSATZBEZEICHNUNG];
			} else if ("Zusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_AUFGELOESTEFEHLMENGEN_ZUSATZBEZEICHNUNG2];
			} else if ("Rasterstehend".equals(fieldName)) {
				value = data[index][REPORT_AUFGELOESTEFEHLMENGEN_RASTERSTEHEND];
			} else if ("Bauform".equals(fieldName)) {
				value = data[index][REPORT_AUFGELOESTEFEHLMENGEN_BAUFORM];
			} else if ("Verpackungsart".equals(fieldName)) {
				value = data[index][REPORT_AUFGELOESTEFEHLMENGEN_VERPACKUNGSART];
			} else if ("Lieferschein".equals(fieldName)) {
				value = data[index][REPORT_AUFGELOESTEFEHLMENGEN_LIEFERSCHEIN];
			}
		} else if (sAktuellerReport
				.equals(ArtikelReportFac.REPORT_ARTIKELBESTELLT)) {

			if ("Bestellung".equals(fieldName)) {
				value = data[index][REPORT_BESTELLTLISTE_BESTELLUNG];
			} else if ("Lieferantenname".equals(fieldName)) {
				value = data[index][REPORT_BESTELLTLISTE_LIEFERANTENNAME];
			} else if ("Projektname".equals(fieldName)) {
				value = data[index][REPORT_BESTELLTLISTE_PROJEKTNAME];
			} else if ("Liefertermin".equals(fieldName)) {
				value = data[index][REPORT_BESTELLTLISTE_LIEFERTERMIN];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_BESTELLTLISTE_MENGE];
			} else if ("Bestellmenge".equals(fieldName)) {
				value = data[index][REPORT_BESTELLTLISTE_BESTELLMENGE];
			} else if ("F_AB_NUMMER".equals(fieldName)) {
				value = data[index][REPORT_BESTELLTLISTE_AB_NUMMER];
			} else if ("F_AB_TERMIN".equals(fieldName)) {
				value = data[index][REPORT_BESTELLTLISTE_AB_TERMIN];
			} else if ("F_OFFENEMENGE".equals(fieldName)) {
				value = data[index][REPORT_BESTELLTLISTE_OFFENEMENGE];
			} else if ("F_AB_KOMMENTAR".equals(fieldName)) {
				value = data[index][REPORT_BESTELLTLISTE_AB_KOMMENTAR];
			}
		} else if (sAktuellerReport
				.equals(ArtikelReportFac.REPORT_ARTIKELBESTELLTRAHMEN)) {
			if ("F_BESTELLUNGCNR".equals(fieldName)) {
				value = data[index][REPORT_RAHMENBEST_BESTELLUNGSNUMMER];
			} else if ("F_LIEFERANT".equals(fieldName)) {
				value = data[index][REPORT_RAHMENBEST_LIEFERANT];
			} else if ("F_PROJEKT".equals(fieldName)) {
				value = data[index][REPORT_RAHMENBEST_PROJEKT];
			} else if ("F_LIEFERTERMIN".equals(fieldName)) {
				value = data[index][REPORT_RAHMENBEST_LIEFERTERMIN];
			} else if ("F_MENGE".equals(fieldName)) {
				value = data[index][REPORT_RAHMENBEST_RAHMENMENGE];
			} else if ("F_OFFENE_MENGE".equals(fieldName)) {
				value = data[index][REPORT_RAHMENBEST_OFFENE_MENGE];
			} else if ("F_PREIS".equals(fieldName)) {
				value = data[index][REPORT_RAHMENBEST_PREIS];
			} else if ("F_AB_TERMIN".equals(fieldName)) {
				value = data[index][REPORT_RAHMENBEST_AB_TERMIN];
			} else if ("F_AB_NUMMER".equals(fieldName)) {
				value = data[index][REPORT_RAHMENBEST_AB_NUMMER];
			} else if ("F_AB_KOMMENTAR".equals(fieldName)) {
				value = data[index][REPORT_RAHMENBEST_AB_KOMMENTAR];
			}
		} else if (sAktuellerReport
				.equals(ArtikelReportFac.REPORT_RAHMENRESERVIERUNG)) {
			if ("F_AUFTRAGCNR".equals(fieldName)) {
				value = data[index][REPORT_RAHMENRESERVIERUNG_AUFTRAGCNR];
			} else if ("F_KUNDECNAME1".equals(fieldName)) {
				value = data[index][REPORT_RAHMENRESERVIERUNG_KUNDECNAME1];
			} else if ("F_AUFTRAGCBEZ".equals(fieldName)) {
				value = data[index][REPORT_RAHMENRESERVIERUNG_AUFTRAGCBEZ];
			} else if ("F_AUFTRAGPOSITIONRAHMENTERMIN".equals(fieldName)) {
				value = data[index][REPORT_RAHMENRESERVIERUNG_AUFTRAGPOSITIONRAHMENTERMIN];
			} else if ("F_AUFTRAGPOSITIONMENGE".equals(fieldName)) {
				value = data[index][REPORT_RAHMENRESERVIERUNG_AUFTRAGPOSITIONMENGE];
			} else if ("F_AUFTRAGPOSITIONOFFENEMENGE".equals(fieldName)) {
				value = data[index][REPORT_RAHMENRESERVIERUNG_AUFTRAGPOSITIONOFFENEMENGE];
			} else if ("F_GELIEFERT".equals(fieldName)) {
				value = data[index][REPORT_RAHMENRESERVIERUNG_AUFTRAGPOSITIONGELIEFERT];
			}
		} else if (sAktuellerReport
				.equals(ArtikelReportFac.REPORT_VKPREISENTWICKLUNG)) {
			if ("Art".equals(fieldName)) {
				value = data[index][REPORT_VKPREISENTWICKLUNG_ART];
			} else if ("Preisliste".equals(fieldName)) {
				value = data[index][REPORT_VKPREISENTWICKLUNG_PREISLISTENNAME];
			} else if ("Preisbasis".equals(fieldName)) {
				value = data[index][REPORT_VKPREISENTWICKLUNG_VKPREISBASIS];
			} else if ("Fixpreis".equals(fieldName)) {
				value = data[index][REPORT_VKPREISENTWICKLUNG_FIXPREIS];
			} else if ("Rabatt".equals(fieldName)) {
				value = data[index][REPORT_VKPREISENTWICKLUNG_RABATT];
			} else if ("Berechneterpreis".equals(fieldName)) {
				value = data[index][REPORT_VKPREISENTWICKLUNG_BERECHNETERPREIS];
			} else if ("Gueltigab".equals(fieldName)) {
				value = data[index][REPORT_VKPREISENTWICKLUNG_GUELTIGAB];
			} else if ("Gueltigbis".equals(fieldName)) {
				value = data[index][REPORT_VKPREISENTWICKLUNG_GUELTIGBIS];
			} else if ("Aenderungsdatum".equals(fieldName)) {
				value = data[index][REPORT_VKPREISENTWICKLUNG_T_AENDERN];
			} else if ("PersonGeaendert".equals(fieldName)) {
				value = data[index][REPORT_VKPREISENTWICKLUNG_PERSON_GEAENDERT];
			} else if ("Staffelmenge".equals(fieldName)) {
				value = data[index][REPORT_VKPREISENTWICKLUNG_STAFFELMENGE];
			} else if ("Mandant".equals(fieldName)) {
				value = data[index][REPORT_VKPREISENTWICKLUNG_MANDANT];
			}
		} else if (sAktuellerReport
				.equals(ArtikelReportFac.REPORT_MINDESTLAGERSTAENDE)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_MINDESTLAGERSTAENDE_ARTIKEL];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_MINDESTLAGERSTAENDE_BEZEICHNUNG];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_MINDESTLAGERSTAENDE_ZUSATZBEZEICHNUNG];
			} else if ("Zusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_MINDESTLAGERSTAENDE_ZUSATZBEZEICHNUNG2];
			} else if ("Kurzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_MINDESTLAGERSTAENDE_KURZBEZEICHNUNG];
			} else if ("Lagermindeststand".equals(fieldName)) {
				value = data[index][REPORT_MINDESTLAGERSTAENDE_LAGERMINDESTSTAND];
			} else if ("Lagersollstand".equals(fieldName)) {
				value = data[index][REPORT_MINDESTLAGERSTAENDE_LAGERSOLLSTAND];
			} else if ("Fertigungssatzgroesse".equals(fieldName)) {
				value = data[index][REPORT_MINDESTLAGERSTAENDE_FERTIGUNGSSATZGROESSE];
			} else if ("VKPreisbasis".equals(fieldName)) {
				value = data[index][REPORT_MINDESTLAGERSTAENDE_VK_PREISBASIS];
			} else if ("LetzterAbgang".equals(fieldName)) {
				value = data[index][REPORT_MINDESTLAGERSTAENDE_LETZTER_ABGANG];
			} else if ("Rahmenreserviert".equals(fieldName)) {
				value = data[index][REPORT_MINDESTLAGERSTAENDE_RAHMENRESERVIERT];
			} else if ("Detailbedarf".equals(fieldName)) {
				value = data[index][REPORT_MINDESTLAGERSTAENDE_DETAILBEDARF];
			} else if ("Rahmenbestellt".equals(fieldName)) {
				value = data[index][REPORT_MINDESTLAGERSTAENDE_RAHMENBESTELLT];
			} else if ("Lagerstand".equals(fieldName)) {
				value = data[index][REPORT_MINDESTLAGERSTAENDE_LAGERSTAND];
			}
		}

		return value;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAufgeloesteFehlmengen(
			TreeMap<?, ?> tmAufgeloesteFehlmengen, TheClientDto theClientDto) {

		Set<?> s = tmAufgeloesteFehlmengen.keySet();
		JasperPrintLP print = null;

		Iterator<?> it = s.iterator();
		while (it.hasNext()) {
			Object key = it.next();
			ArrayList<AufgeloesteFehlmengenDto> al = (ArrayList) tmAufgeloesteFehlmengen
					.get(key);

			// Nach Artikel sortieren
			for (int i = al.size() - 1; i > 0; --i) {
				for (int j = 0; j < i; ++j) {
					AufgeloesteFehlmengenDto a = (AufgeloesteFehlmengenDto) al
							.get(j);
					AufgeloesteFehlmengenDto b = (AufgeloesteFehlmengenDto) al
							.get(j + 1);
					if (a.getArtikelCNr().compareTo(b.getArtikelCNr()) > 0) {
						AufgeloesteFehlmengenDto h = a;
						al.set(j, b);
						al.set(j + 1, h);
					}
				}
			}

			data = new Object[al.size()][REPORT_AUFGELOESTEFEHLMENGEN_ANZAHL_SPALTEN];
			for (int i = 0; i < al.size(); i++) {
				AufgeloesteFehlmengenDto aufgeloesteFehlmengenDto = (AufgeloesteFehlmengenDto) al
						.get(i);

				ArtikelDto artikelDto = aufgeloesteFehlmengenDto
						.getArtikelDto();
				data[i][REPORT_AUFGELOESTEFEHLMENGEN_ARTIKEL] = aufgeloesteFehlmengenDto
						.getArtikelDto().getCNr();
				data[i][REPORT_AUFGELOESTEFEHLMENGEN_BEZEICHNUNG] = artikelDto
						.getArtikelsprDto().getCBez();
				data[i][REPORT_AUFGELOESTEFEHLMENGEN_ZUSATZBEZEICHNUNG] = artikelDto
						.getArtikelsprDto().getCZbez();
				data[i][REPORT_AUFGELOESTEFEHLMENGEN_ZUSATZBEZEICHNUNG2] = artikelDto
						.getArtikelsprDto().getCZbez2();
				data[i][REPORT_AUFGELOESTEFEHLMENGEN_EINHEIT] = artikelDto
						.getEinheitCNr();

				try {
					if (artikelDto.getFarbcodeIId() != null) {
						data[i][REPORT_AUFGELOESTEFEHLMENGEN_FARBCODE] = getArtikelFac()
								.farbcodeFindByPrimaryKey(
										artikelDto.getFarbcodeIId()).getCNr();
					}
					// Material
					if (artikelDto.getMaterialIId() != null) {
						MaterialDto materialDto = getMaterialFac()
								.materialFindByPrimaryKey(
										artikelDto.getMaterialIId(),
										theClientDto);
						data[i][REPORT_AUFGELOESTEFEHLMENGEN_MATERIAL] = materialDto
								.getBezeichnung();
					}
					// Lagerort

					data[i][REPORT_AUFGELOESTEFEHLMENGEN_LAGERORT] = getLagerFac()
							.getLagerplaezteEinesArtikels(
									artikelDto.getIId(),
									aufgeloesteFehlmengenDto.getLagerDto()
											.getIId());

					// Artkelklasse
					if (artikelDto.getArtklaIId() != null) {
						ArtklaDto aklaDto = getArtikelFac()
								.artklaFindByPrimaryKey(
										artikelDto.getArtklaIId(), theClientDto);
						data[i][REPORT_AUFGELOESTEFEHLMENGEN_ARTIKELKLASSE] = aklaDto
								.getBezeichnung();
					}

					// Staerke/Hoehe
					if (artikelDto.getGeometrieDto() != null) {
						if (artikelDto.getGeometrieDto().getFHoehe() != null) {
							data[i][REPORT_AUFGELOESTEFEHLMENGEN_HOEHE] = artikelDto
									.getGeometrieDto().getFHoehe();
						}
						if (artikelDto.getGeometrieDto().getFBreite() != null) {
							data[i][REPORT_AUFGELOESTEFEHLMENGEN_BREITE] = artikelDto
									.getGeometrieDto().getFBreite();
						}
						if (artikelDto.getGeometrieDto().getFTiefe() != null) {
							data[i][REPORT_AUFGELOESTEFEHLMENGEN_TIEFE] = artikelDto
									.getGeometrieDto().getFTiefe();
						}
					}

					// Verpackung
					if (artikelDto.getVerpackungDto() != null) {
						data[i][REPORT_AUFGELOESTEFEHLMENGEN_BAUFORM] = artikelDto
								.getVerpackungDto().getCBauform();
						data[i][REPORT_AUFGELOESTEFEHLMENGEN_VERPACKUNGSART] = artikelDto
								.getVerpackungDto().getCVerpackungsart();
					}

					// Gewicht
					data[i][REPORT_AUFGELOESTEFEHLMENGEN_GEWICHTKG] = artikelDto
							.getFGewichtkg();
					// Montage Rasterstehend
					if (artikelDto.getMontageDto() != null
							&& artikelDto.getMontageDto().getFRasterstehend() != null) {
						data[i][REPORT_AUFGELOESTEFEHLMENGEN_RASTERSTEHEND] = new Double(
								artikelDto.getMontageDto().getFRasterstehend()
										.doubleValue());
					}

				} catch (RemoteException ex1) {
					throwEJBExceptionLPRespectOld(ex1);
				}

				data[i][REPORT_AUFGELOESTEFEHLMENGEN_EINHEIT] = aufgeloesteFehlmengenDto
						.getArtikelDto().getEinheitCNr();
				data[i][REPORT_AUFGELOESTEFEHLMENGEN_LAGER] = aufgeloesteFehlmengenDto
						.getLagerDto().getCNr();
				data[i][REPORT_AUFGELOESTEFEHLMENGEN_MENGE] = aufgeloesteFehlmengenDto
						.getAufgeloesteMenge();
				data[i][REPORT_AUFGELOESTEFEHLMENGEN_SNRCHNR] = Helper
						.erzeugeStringAusStringArray(aufgeloesteFehlmengenDto
								.getSSeriennrChnr());

				if (aufgeloesteFehlmengenDto.getLieferscheinDto() != null) {
					data[i][REPORT_AUFGELOESTEFEHLMENGEN_LIEFERSCHEIN] = aufgeloesteFehlmengenDto
							.getLieferscheinDto().getCNr();
				}

			}
			HashMap<String, Object> parameter = new HashMap<String, Object>();
			index = -1;
			sAktuellerReport = ArtikelReportFac.REPORT_AUFGELOESTEFEHLMENGEN;

			if (al.size() > 0) {

				AufgeloesteFehlmengenDto aufgeloesteFehlmengenDto = (AufgeloesteFehlmengenDto) al
						.get(0);

				if (aufgeloesteFehlmengenDto.getLosDto() != null) {

					String losBez = aufgeloesteFehlmengenDto.getLosDto()
							.getCNr();
					if (aufgeloesteFehlmengenDto.getLosDto().getCProjekt() != null) {
						losBez += " "
								+ aufgeloesteFehlmengenDto.getLosDto()
										.getCProjekt();
					}
					LosDto losDto = aufgeloesteFehlmengenDto.getLosDto();
					parameter.put("P_LOSNUMMER", losDto.getCNr());

					parameter.put("P_ANGELEGT", new java.util.Date(losDto
							.getTAnlegen().getTime()));

					parameter.put("P_PROJEKT", losDto.getCProjekt());
					parameter.put("P_LOSKOMMENTAR", losDto.getCKommentar());
					parameter.put("P_LOSLANGTEXT", losDto.getXText());
					parameter.put("P_LOSGROESSE", losDto.getNLosgroesse());

					parameter.put("P_PRODUKTIONSBEGINN",
							losDto.getTProduktionsbeginn());
					parameter.put("P_PRODUKTIONSENDE",
							losDto.getTProduktionsende());

					try {
						if (losDto.getAuftragIId() != null) {
							AuftragDto auftragDto = getAuftragFac()
									.auftragFindByPrimaryKey(
											losDto.getAuftragIId());
							parameter.put("P_AUFTRAGNUMMER",
									auftragDto.getCNr());
							parameter
									.put("P_KUNDE",
											getKundeFac()
													.kundeFindByPrimaryKey(
															auftragDto
																	.getKundeIIdAuftragsadresse(),
															theClientDto)
													.getPartnerDto()
													.getCName1nachnamefirmazeile1());
						}

						KostenstelleDto kstDto = getSystemFac()
								.kostenstelleFindByPrimaryKey(
										losDto.getKostenstelleIId());
						parameter.put("P_KOSTENSTELLENUMMER", kstDto.getCNr());

						FertigungsgruppeDto fertGruppeDto = getStuecklisteFac()
								.fertigungsgruppeFindByPrimaryKey(
										losDto.getFertigungsgruppeIId());
						parameter.put("P_FERTIGUNGSGRUPPE",
								fertGruppeDto.getCBez());

						if (losDto.getStuecklisteIId() != null) {
							StuecklisteDto stkDto = getStuecklisteFac()
									.stuecklisteFindByPrimaryKey(
											losDto.getStuecklisteIId(),
											theClientDto);
							parameter.put("P_STUECKLISTEBEZEICHNUNG", stkDto
									.getArtikelDto().getArtikelsprDto()
									.getCBez());
							parameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG",
									stkDto.getArtikelDto().getArtikelsprDto()
											.getCZbez());
							parameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG2",
									stkDto.getArtikelDto().getArtikelsprDto()
											.getCZbez2());
							parameter.put("P_STUECKLISTENUMMER", stkDto
									.getArtikelDto().getCNr());

							// Zeichnungsnummer
							StuecklisteeigenschaftDto[] stuecklisteeigenschaftDtos = getStuecklisteFac()
									.stuecklisteeigenschaftFindByStuecklisteIId(
											losDto.getStuecklisteIId());
							ArrayList<Object[]> alZeichnung = new ArrayList<Object[]>();
							for (int i = 0; i < stuecklisteeigenschaftDtos.length; i++) {
								StuecklisteeigenschaftDto dto = stuecklisteeigenschaftDtos[i];

								Object[] o = new Object[2];
								String sStklEigenschaftArt = dto
										.getStuecklisteeigenschaftartDto()
										.getCBez();
								o[0] = sStklEigenschaftArt;
								o[1] = dto.getCBez();
								alZeichnung.add(o);

								// Index und Materialplatz auch einzeln an
								// Report
								// uebergeben
								if (sStklEigenschaftArt
										.equals(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_INDEX)) {
									parameter.put(
											P_STUECKLISTENEIGENSCHAFT_INDEX,
											dto.getCBez());
								}
								if (sStklEigenschaftArt
										.equals(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_MATERIALPLATZ)) {
									parameter
											.put(P_STUECKLISTENEIGENSCHAFT_MATERIALPLATZ,
													dto.getCBez());
								}
							}

							// Stuecklisteeigenschaft fuer Subreport
							if (stuecklisteeigenschaftDtos.length > 0) {
								String[] fieldnames = new String[] {
										"F_EIGENSCHAFTART", "F_BEZEICHNUNG" };
								Object[][] dataSub = new Object[alZeichnung
										.size()][fieldnames.length];
								dataSub = (Object[][]) alZeichnung
										.toArray(dataSub);

								parameter.put("DATENSUBREPORT",
										new LPDatenSubreport(dataSub,
												fieldnames));
							}

							// Stuecklisteeigenschaften als einzelne Parameter
							// fuer
							// Index und Materialplatz
							Hashtable<?, ?> htStklEigenschaften = getStuecklisteReportFac()
									.getStuecklisteEigenschaften(
											losDto.getStuecklisteIId(),
											theClientDto.getMandant(),
											theClientDto);
							if (htStklEigenschaften != null) {
								if (htStklEigenschaften
										.containsKey(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_INDEX)) {
									parameter
											.put(P_STUECKLISTENEIGENSCHAFT_INDEX,
													htStklEigenschaften
															.get(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_INDEX));
								}
								if (htStklEigenschaften
										.containsKey(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_MATERIALPLATZ)) {
									parameter
											.put(P_STUECKLISTENEIGENSCHAFT_MATERIALPLATZ,
													htStklEigenschaften
															.get(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_MATERIALPLATZ));
								}
							}

						} else {
							parameter.put("P_STUECKLISTEBEZEICHNUNG",
									losDto.getCProjekt());
							parameter.put(
									"P_STUECKLISTENUMMER",
									getTextRespectUISpr("fert.materialliste",
											theClientDto.getMandant(),
											theClientDto.getLocUi()));
						}
					} catch (RemoteException ex) {
						throwEJBExceptionLPRespectOld(ex);
					}
				} else if (aufgeloesteFehlmengenDto.getAuftagDto() != null) {
					parameter.put("P_KUNDENAUFTRAGSNUMMER",
							aufgeloesteFehlmengenDto.getAuftagDto().getCNr());

					parameter.put("P_ANGELEGT", new java.util.Date(
							aufgeloesteFehlmengenDto.getAuftagDto()
									.getTAnlegen().getTime()));

					parameter.put("P_PROJEKT", aufgeloesteFehlmengenDto
							.getAuftagDto().getCBezProjektbezeichnung());

					parameter
							.put("P_KUNDE",
									getKundeFac()
											.kundeFindByPrimaryKey(
													aufgeloesteFehlmengenDto
															.getAuftagDto()
															.getKundeIIdAuftragsadresse(),
													theClientDto)
											.getPartnerDto()
											.getCName1nachnamefirmazeile1());

					KostenstelleDto kstDto = getSystemFac()
							.kostenstelleFindByPrimaryKey(
									aufgeloesteFehlmengenDto.getAuftagDto()
											.getKostIId());
					parameter.put("P_KOSTENSTELLENUMMER", kstDto.getCNr());

				}
			}
			initJRDS(parameter, ArtikelReportFac.REPORT_MODUL,
					ArtikelReportFac.REPORT_AUFGELOESTEFEHLMENGEN,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);
			if (print != null) {

				print = Helper.addReport2Report(print, getReportPrint()
						.getPrint());
			} else {
				print = getReportPrint();
			}
		}
		PrintInfoDto values = new PrintInfoDto();
		values.setDocPath(new HeliumDocPath()
				.add(new DocNodeLiteral(theClientDto.getMandant()))
				.add(new DocNodeFolder(DocNodeBase.BELEGART_ARTIKEL))
				.add(new DocNodeFile(DocNodeBase.BELEGART_AUFGLFEHLMENGEN)));
		// JCRDocFac.HELIUMV_NODE + "/"
		// + LocaleFac.BELEGART_ARTIKEL.trim() + "/"
		// + "AufgeloesteFehlmengen";
		values.setiId(theClientDto.getIDPersonal());
		values.setTable("");

		if (print != null) {
			print.setOInfoForArchive(values);
		}
		return print;

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printNaechsteWartungen(TheClientDto theClientDto) {

		// Erstellung des Reports
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		index = -1;
		sAktuellerReport = ArtikelReportFac.REPORT_NAECHSTE_WARTUNGEN;

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session.createCriteria(FLRArtikel.class);
		crit.add(Restrictions
				.isNotNull(ArtikelFac.FLR_ARTIKEL_I_WARTUNGSINTERVALL));
		crit.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));
		crit.addOrder(Order.asc("c_nr"));
		List<?> results = crit.list();
		Iterator<?> resultListIterator = results.iterator();

		ArrayList alDaten = new ArrayList();

		while (resultListIterator.hasNext()) {
			FLRArtikel flrartikel = (FLRArtikel) resultListIterator.next();

			Object[] zeile = new Object[REPORT_NAECHSTE_WARTUNGEN_ANZAHL_SPALTEN];

			ArtikelDto artikelDto = getArtikelFac()
					.artikelFindByPrimaryKeySmall(flrartikel.getI_id(),
							theClientDto);

			Timestamp tFaelligSeit = null;
			if (artikelDto.getTLetztewartung() != null) {

				zeile[REPORT_NAECHSTE_WARTUNGEN_LETZTE_WARTUNG] = artikelDto
						.getTLetztewartung();

				Calendar cWartungFaellig = Calendar.getInstance();
				cWartungFaellig.setTimeInMillis(artikelDto.getTLetztewartung()
						.getTime());
				cWartungFaellig.add(Calendar.MONTH,
						artikelDto.getIWartungsintervall());

				tFaelligSeit = new Timestamp(cWartungFaellig.getTimeInMillis());

			}

			if (tFaelligSeit == null
					|| tFaelligSeit.getTime() < System.currentTimeMillis()) {

				zeile[REPORT_NAECHSTE_WARTUNGEN_FAELLIG_SEIT] = tFaelligSeit;

				zeile[REPORT_NAECHSTE_WARTUNGEN_ARTIKEL] = flrartikel.getC_nr();

				if (artikelDto.getArtikelsprDto() != null) {
					zeile[REPORT_NAECHSTE_WARTUNGEN_BEZEICHNUNG] = artikelDto
							.getArtikelsprDto().getCBez();
					zeile[REPORT_NAECHSTE_WARTUNGEN_KURZBEZEICHNUNG] = artikelDto
							.getArtikelsprDto().getCKbez();
					zeile[REPORT_NAECHSTE_WARTUNGEN_ZUSATZBEZEICHNUNG] = artikelDto
							.getArtikelsprDto().getCZbez();
					zeile[REPORT_NAECHSTE_WARTUNGEN_ZUSATZBEZEICHNUNG2] = artikelDto
							.getArtikelsprDto().getCZbez2();
				}

				zeile[REPORT_NAECHSTE_WARTUNGEN_REVISION] = artikelDto
						.getCRevision();
				zeile[REPORT_NAECHSTE_WARTUNGEN_INDEX] = artikelDto.getCIndex();
				zeile[REPORT_NAECHSTE_WARTUNGEN_REFERENZNUMMER] = artikelDto
						.getCReferenznr();

				if (flrartikel.getFlrartikelgruppe() != null) {
					zeile[REPORT_NAECHSTE_WARTUNGEN_ARTIKELGRUPPE] = flrartikel
							.getFlrartikelgruppe().getC_nr();
				}
				if (flrartikel.getFlrartikelklasse() != null) {
					zeile[REPORT_NAECHSTE_WARTUNGEN_ARTIKELKLASSE] = flrartikel
							.getFlrartikelklasse().getC_nr();
				}

				zeile[REPORT_NAECHSTE_WARTUNGEN_WARTUNGSINTERVALL] = artikelDto
						.getIWartungsintervall();

				ArtikellagerplaetzeDto artikellagerplaetzeDto = getLagerFac()
						.getErstenArtikellagerplatz(flrartikel.getI_id(),
								theClientDto);
				if (artikellagerplaetzeDto != null) {

					String lagerplatz = artikellagerplaetzeDto
							.getLagerplatzDto().getCLagerplatz();

					if (artikellagerplaetzeDto.isbEsGibtMehrereLagerplaetze() == true) {
						lagerplatz += " ++";
					}

					zeile[REPORT_NAECHSTE_WARTUNGEN_LAGERPLATZ] = lagerplatz;

					try {
						zeile[REPORT_NAECHSTE_WARTUNGEN_LAGER_LAGERPLATZ] = getLagerFac()
								.lagerFindByPrimaryKey(
										artikellagerplaetzeDto
												.getLagerplatzDto()
												.getLagerIId()).getCNr();
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}

				}

				alDaten.add(zeile);
			}
		}
		session.close();

		Object[][] dataTemp = new Object[alDaten.size()][REPORT_NAECHSTE_WARTUNGEN_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(dataTemp);

		initJRDS(parameter, ArtikelReportFac.REPORT_MODUL,
				ArtikelReportFac.REPORT_NAECHSTE_WARTUNGEN,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printFehlmengen(Integer artikelIId,
			TheClientDto theClientDto) {
		if (artikelIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelIId == null"));
		}
		// Erstellung des Reports
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		index = -1;
		sAktuellerReport = ArtikelReportFac.REPORT_ARTIKELFEHLMENGE;

		String eingeloggterMandant = theClientDto.getMandant();

		FLRFehlmenge flrFehlmenge = new FLRFehlmenge();
		flrFehlmenge.setArtikel_i_id(artikelIId);

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session
				.createCriteria(FLRFehlmenge.class).add(
						Example.create(flrFehlmenge));
		List<?> results = crit.list();
		Iterator<?> resultListIterator = results.iterator();

		int row = 0;

		Object[][] dataHelp = new Object[results.size()][10];

		while (resultListIterator.hasNext()) {
			FLRFehlmenge fehlmenge = (FLRFehlmenge) resultListIterator.next();

			String sBelegnummer = null;
			String sProjektbezeichnung = null;
			String sMandant = null;
			String sKunde = null;
			String sStueckliste = null;
			String sStuecklisteBezeichnung = null;
			java.sql.Date tLosbeginn = null;
			java.sql.Date tLosende = null;
			java.sql.Timestamp tGeplanterliefertermin = null;

			if (fehlmenge.getC_belegartnr().equals(LocaleFac.BELEGART_LOS)) {

				com.lp.server.fertigung.service.LosDto losDto = null;
				try {
					if (fehlmenge.getFlrlossollmaterial() != null) {
						losDto = getFertigungFac()
								.losFindByPrimaryKey(
										fehlmenge.getFlrlossollmaterial()
												.getLos_i_id());
						sBelegnummer = "L" + losDto.getCNr();

						tLosbeginn = losDto.getTProduktionsbeginn();
						tLosende = losDto.getTProduktionsende();

						if (losDto.getAuftragIId() != null) {
							AuftragDto auftragDto = null;
							auftragDto = getAuftragFac()
									.auftragFindByPrimaryKey(
											losDto.getAuftragIId());
							KundeDto kundeDto = getKundeFac()
									.kundeFindByPrimaryKey(
											auftragDto
													.getKundeIIdAuftragsadresse(),
											theClientDto);

							sKunde = kundeDto.getPartnerDto()
									.formatTitelAnrede();

							tGeplanterliefertermin = auftragDto
									.getDLiefertermin();

							if (losDto.getAuftragpositionIId() != null) {
								AuftragpositionDto auftragpostionDto = getAuftragpositionFac()
										.auftragpositionFindByPrimaryKey(
												losDto.getAuftragpositionIId());
								if (auftragpostionDto
										.getTUebersteuerbarerLiefertermin() != null) {
									tGeplanterliefertermin = auftragpostionDto
											.getTUebersteuerbarerLiefertermin();
								}
							} else {

							}

						} else {
							sKunde = null;
						}
						sMandant = losDto.getMandantCNr();
						sProjektbezeichnung = losDto.getCProjekt();

						if (fehlmenge.getFlrlossollmaterial().getFlrlos()
								.getFlrstueckliste() != null) {
							ArtikelDto artikelDto = getArtikelFac()
									.artikelFindByPrimaryKeySmall(
											fehlmenge.getFlrlossollmaterial()
													.getFlrlos()
													.getFlrstueckliste()
													.getFlrartikel().getI_id(),
											theClientDto);
							sStueckliste = artikelDto.getCNr();
							sStuecklisteBezeichnung = artikelDto
									.formatBezeichnung();
						}

					} else {
						sProjektbezeichnung = "Lossollmaterial gel\u00F6scht";
					}
				} catch (RemoteException ex3) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex3);
				}
			}
			if (sMandant == null || eingeloggterMandant.equals(sMandant)) {

				dataHelp[row][REPORT_FEHLMENGENLISTE_LOS] = sBelegnummer;
				dataHelp[row][REPORT_FEHLMENGENLISTE_PROJEKTNAME] = sProjektbezeichnung;
				dataHelp[row][REPORT_FEHLMENGENLISTE_LIEFERTERMIN] = fehlmenge
						.getT_liefertermin();
				dataHelp[row][REPORT_FEHLMENGENLISTE_MENGE] = fehlmenge
						.getN_menge();
				dataHelp[row][REPORT_FEHLMENGENLISTE_KUNDE] = sKunde;
				dataHelp[row][REPORT_FEHLMENGENLISTE_STUECKLISTE_NUMMER] = sStueckliste;
				dataHelp[row][REPORT_FEHLMENGENLISTE_STUECKLISTE_BEZEICHNUNG] = sStuecklisteBezeichnung;

				dataHelp[row][REPORT_FEHLMENGENLISTE_LOSBEGINN] = tLosbeginn;
				dataHelp[row][REPORT_FEHLMENGENLISTE_LOSENDE] = tLosende;
				dataHelp[row][REPORT_FEHLMENGENLISTE_ABLIEFERTERMIN] = tGeplanterliefertermin;

				row++;
			}

		}
		session.close();

		data = new Object[row][4];
		for (int i = 0; i < row; i++) {
			data[i] = dataHelp[i];
		}

		ArtikelDto dto = getArtikelFac().artikelFindByPrimaryKey(artikelIId,
				theClientDto);
		parameter.put("P_ARTIKEL", dto.formatArtikelbezeichnung());

		initJRDS(parameter, ArtikelReportFac.REPORT_MODUL,
				ArtikelReportFac.REPORT_ARTIKELFEHLMENGE, eingeloggterMandant,
				theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	public boolean next() throws JRException {
		index++;
		return (index < data.length);
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printLosstatus(Integer artikelIId,
			TheClientDto theClientDto) {

		Integer stuecklisteIId = null;

		com.lp.server.stueckliste.service.StuecklisteDto stuecklisteDto = getStuecklisteFac()
				.stuecklisteFindByMandantCNrArtikelIIdOhneExc(artikelIId,
						theClientDto);
		if (stuecklisteDto != null) {
			stuecklisteIId = stuecklisteDto.getIId();
		}

		if (stuecklisteIId != null) {
			Session session = FLRSessionFactory.getFactory().openSession();
			org.hibernate.Criteria atikelliste = session
					.createCriteria(FLRLosReport.class);
			atikelliste.add(Restrictions.eq(
					FertigungFac.FLR_LOS_STUECKLISTE_I_ID, stuecklisteIId));

			atikelliste.addOrder(Order.desc("c_nr"));

			List<?> resultList = atikelliste.list();

			Iterator<?> resultListIterator = resultList.iterator();
			int row = 0;
			data = new Object[resultList.size()][18];
			while (resultListIterator.hasNext()) {
				FLRLosReport los = (FLRLosReport) resultListIterator.next();

				FLRAuftragReport flrauftrag = null;
				if (los.getFlrauftragposition() != null) {
					flrauftrag = los.getFlrauftragposition().getFlrauftrag();
				} else {
					flrauftrag = los.getFlrauftrag();
				}

				if (flrauftrag != null) {
					data[row][REPORT_LOSSTATUS_AUFTRAG] = flrauftrag.getC_nr();
					data[row][REPORT_LOSSTATUS_AUFTRAG_PROJEKT] = flrauftrag
							.getC_bez();
				}

				data[row][REPORT_LOSSTATUS_LOSNR] = los.getC_nr();
				data[row][REPORT_LOSSTATUS_MENGE] = los.getN_losgroesse();
				data[row][REPORT_LOSSTATUS_PROJEKT] = los.getC_projekt();
				data[row][REPORT_LOSSTATUS_BEGINN] = new java.util.Date(los
						.getT_produktionsbeginn().getTime());
				data[row][REPORT_LOSSTATUS_ENDE] = new java.util.Date(los
						.getT_produktionsende().getTime());

				data[row][REPORT_LOSSTATUS_STATUS] = los.getStatus_c_nr()
						.trim();

				Session session2 = FLRSessionFactory.getFactory().openSession();

				String sQueryAblieferungen = "SELECT sum(losablieferung.n_menge) AS n_menge, sum(losablieferung.n_menge*losablieferung.n_materialwert) as materialwert,sum(losablieferung.n_menge*losablieferung.n_arbeitszeitwert) AS n_azwert "
						+ " FROM FLRLosablieferung AS losablieferung WHERE losablieferung.flrlos.i_id="
						+ los.getI_id();

				Query ablieferungen = session2.createQuery(sQueryAblieferungen);

				if (ablieferungen.list().iterator().hasNext()) {

					Object[] o = (Object[]) ablieferungen.list().iterator()
							.next();

					java.math.BigDecimal abgelieferteMange = (BigDecimal) o[0];

					data[row][REPORT_LOSSTATUS_ABGELIEFERT] = abgelieferteMange;

					if (abgelieferteMange != null
							&& abgelieferteMange.doubleValue() != 0) {
						data[row][REPORT_LOSSTATUS_PREIS_ABLIEFERUNG_MATERIAL] = ((BigDecimal) o[1])
								.divide(abgelieferteMange, 4);
						data[row][REPORT_LOSSTATUS_PREIS_ABLIEFERUNG_ZEIT] = ((BigDecimal) o[2])
								.divide(abgelieferteMange, 4);
					}

				}
				session2.close();

				try {
					LosDto losDto = getFertigungFac().losFindByPrimaryKey(
							los.getI_id());

					data[row][REPORT_LOSSTATUS_ANGELEGT] = losDto.getTAnlegen();
					data[row][REPORT_LOSSTATUS_ERLEDIGT] = losDto
							.getTErledigt();
					data[row][REPORT_LOSSTATUS_AUSGEGEBEN] = losDto
							.getTAusgabe();

					if (los.getFlrauftrag() != null) {

						KundeDto kundeDto = getKundeFac()
								.kundeFindByPrimaryKey(
										los.getFlrauftrag().getFlrkunde()
												.getI_id(), theClientDto);
						data[row][REPORT_LOSSTATUS_KUNDE] = kundeDto
								.getPartnerDto().formatFixName1Name2();
					}

					data[row][REPORT_LOSSTATUS_FERTIGUNGSGRUPPE] = los
							.getFlrfertigungsgruppe().getC_bez();

					if (losDto.getPersonalIIdTechniker() != null) {
						data[row][REPORT_LOSSTATUS_TECHNIKER] = getPersonalFac()
								.personalFindByPrimaryKey(
										losDto.getPersonalIIdTechniker(),
										theClientDto)
								.formatFixUFTitelName2Name1();

					}

					data[row][REPORT_LOSSTATUS_ZIELLAGER] = getLagerFac()
							.lagerFindByPrimaryKey(losDto.getLagerIIdZiel())
							.getCNr();

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

				row++;
			}
			session.close();

		} else {
			data = new Object[0][0];
		}
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		index = -1;
		sAktuellerReport = ArtikelReportFac.REPORT_LOSSTATUS;

		ArtikelDto dto = getArtikelFac().artikelFindByPrimaryKey(artikelIId,
				theClientDto);
		parameter.put("P_ARTIKEL", dto.formatArtikelbezeichnung());

		initJRDS(parameter, ArtikelReportFac.REPORT_MODUL,
				ArtikelReportFac.REPORT_LOSSTATUS, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);

		return getReportPrint();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printVerwendungsnachweis(Integer artikelIId,
			boolean bMitVerbrauchtenMengen, java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, boolean bMitVersteckten,
			TheClientDto theClientDto) {
		if (artikelIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelIId == null"));
		}

		// Erstellung des Reports

		ArrayList alDaten = new ArrayList();

		JasperPrintLP print = null;
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		String sMandant = theClientDto.getMandant();
		Locale locUi = theClientDto.getLocUi();
		index = -1;
		sAktuellerReport = ArtikelReportFac.REPORT_VERWENDUNGSNACHWEIS;

		Session session = FLRSessionFactory.getFactory().openSession();
		Session session2 = FLRSessionFactory.getFactory().openSession();
		Session session3 = FLRSessionFactory.getFactory().openSession();

		FLRStueckliste flrStueckliste = new FLRStueckliste();
		flrStueckliste.setMandant_c_nr(sMandant);

		FLRStuecklisteposition flrStuecklisteposition = new FLRStuecklisteposition();
		flrStuecklisteposition.setFlrstueckliste(flrStueckliste);

		FLRStuecklistearbeitsplan flrStuecklistearbeitsplan = new FLRStuecklistearbeitsplan();
		flrStuecklistearbeitsplan.setFlrstueckliste(flrStueckliste);

		org.hibernate.Criteria crit2 = session2
				.createCriteria(FLRStuecklistearbeitsplan.class)
				.add(Example.create(flrStuecklistearbeitsplan))
				.createAlias(
						com.lp.server.stueckliste.service.StuecklisteFac.FLR_STUECKLISTEPOSITION_FLRSTUECKLISTE,
						"s")
				.add(Restrictions.eq("s.mandant_c_nr", sMandant))
				.createAlias(
						com.lp.server.stueckliste.service.StuecklisteFac.FLR_STUECKLISTEARBEITSPLAN_FLRARTIKEL,
						"a").add(Restrictions.eq("a.i_id", artikelIId))
				.addOrder(Order.asc("a.c_nr"));
		List<?> results2 = crit2.list();
		Iterator<?> resultListIterator2 = results2.iterator();

		org.hibernate.Criteria crit = session
				.createCriteria(FLRStuecklisteposition.class)
				.createAlias(
						com.lp.server.stueckliste.service.StuecklisteFac.FLR_STUECKLISTEPOSITION_FLRSTUECKLISTE,
						"s")
				.add(Restrictions.eq("s.mandant_c_nr", sMandant))
				.createAlias(
						com.lp.server.stueckliste.service.StuecklisteFac.FLR_STUECKLISTEPOSITION_FLRARTIKEL,
						"a").add(Restrictions.eq("a.i_id", artikelIId))
				.addOrder(Order.asc("a.c_nr"));
		List<?> results = crit.list();

		org.hibernate.Criteria crit3 = session3
				.createCriteria(
						com.lp.server.angebotstkl.fastlanereader.generated.FLRAgstklposition.class)
				.createAlias(
						AngebotstklpositionFac.FLR_AGSTKLPOSITION_FLRAGSTKL,
						"as")
				.add(Restrictions.eq("as.mandant_c_nr", sMandant))
				.createAlias(
						AngebotstklpositionFac.FLR_AGSTKLPOSITION_FLRARTIKEL,
						"a").add(Restrictions.eq("a.i_id", artikelIId))
				.addOrder(Order.asc("as.c_nr"));
		List<?> results3 = crit3.list();
		Iterator<?> resultListIterator3 = results3.iterator();

		Iterator<?> resultListIterator = results.iterator();

		data = new Object[results.size() + results2.size() + results3.size()][17];

		while (resultListIterator.hasNext()) {
			FLRStuecklisteposition stuecklisteposition = (FLRStuecklisteposition) resultListIterator
					.next();

			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(
					stuecklisteposition.getFlrstueckliste().getFlrartikel()
							.getI_id(), theClientDto);

			Object[] oZeile = new Object[REPORT_VERWENDUNGSNACHWEIS_ANZAHL_SPALTEN];
			oZeile[REPORT_VERWENDUNGSNACHWEIS_STUECKLISTE] = stuecklisteposition
					.getFlrstueckliste().getFlrartikel().getC_nr();
			oZeile[REPORT_VERWENDUNGSNACHWEIS_BEZEICHNUNG] = artikelDto
					.formatBezeichnung();
			oZeile[REPORT_VERWENDUNGSNACHWEIS_VERSTECKT] = Helper
					.short2Boolean(artikelDto.getBVersteckt());
			oZeile[REPORT_VERWENDUNGSNACHWEIS_MENGE] = stuecklisteposition
					.getN_menge();
			oZeile[REPORT_VERWENDUNGSNACHWEIS_EINHEIT] = stuecklisteposition
					.getEinheit_c_nr();

			try {
				oZeile[REPORT_VERWENDUNGSNACHWEIS_LETZTE_VERWENDUNG] = getLagerFac()
						.getDatumLetzterZugangsOderAbgangsbuchung(
								artikelDto.getIId(), true);
				oZeile[REPORT_VERWENDUNGSNACHWEIS_SPERREN] = getArtikelFac()
						.getArtikelsperrenText(artikelDto.getIId());
				if (bMitVerbrauchtenMengen) {
					oZeile[REPORT_VERWENDUNGSNACHWEIS_VERBRAUCHTEMENGE] = getLagerFac()
							.getVerbrauchteMengeEinesArtikels(
									stuecklisteposition.getFlrstueckliste()
											.getFlrartikel().getI_id(), tVon,
									tBis, theClientDto);
				}

				oZeile[REPORT_VERWENDUNGSNACHWEIS_LAGERSTAND] = getLagerFac()
						.getLagerstandAllerLagerEinesMandanten(
								artikelDto.getIId(), theClientDto);

				oZeile[REPORT_VERWENDUNGSNACHWEIS_RESERVIERT] = getReservierungFac()
						.getAnzahlReservierungen(artikelDto.getIId(),
								theClientDto);

				oZeile[REPORT_VERWENDUNGSNACHWEIS_IN_FERTIGUNG] = getFertigungFac()
						.getAnzahlInFertigung(artikelDto.getIId(), theClientDto);

				oZeile[REPORT_VERWENDUNGSNACHWEIS_FEHLMENGE] = getFehlmengeFac()
						.getAnzahlFehlmengeEinesArtikels(artikelDto.getIId(),
								theClientDto);

				oZeile[REPORT_VERWENDUNGSNACHWEIS_RAHMENRESERVIERT] = getReservierungFac()
						.getAnzahlRahmenreservierungen(artikelDto.getIId(),
								theClientDto);

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			alDaten.add(oZeile);

		}
		while (resultListIterator2.hasNext()) {
			FLRStuecklistearbeitsplan stuecklisteposition = (FLRStuecklistearbeitsplan) resultListIterator2
					.next();

			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(
					stuecklisteposition.getFlrstueckliste().getFlrartikel()
							.getI_id(), theClientDto);

			Object[] oZeile = new Object[REPORT_VERWENDUNGSNACHWEIS_ANZAHL_SPALTEN];
			oZeile[REPORT_VERWENDUNGSNACHWEIS_STUECKLISTE] = stuecklisteposition
					.getFlrstueckliste().getFlrartikel().getC_nr();
			oZeile[REPORT_VERWENDUNGSNACHWEIS_BEZEICHNUNG] = artikelDto
					.formatBezeichnung();
			oZeile[REPORT_VERWENDUNGSNACHWEIS_VERSTECKT] = Helper
					.short2Boolean(artikelDto.getBVersteckt());
			oZeile[REPORT_VERWENDUNGSNACHWEIS_ZUSATZ] = "A";
			oZeile[REPORT_VERWENDUNGSNACHWEIS_ARBEITSGANG] = stuecklisteposition
					.getI_arbeitsgang();

			double lStueckzeit = stuecklisteposition.getL_stueckzeit();
			oZeile[REPORT_VERWENDUNGSNACHWEIS_STUECKZEIT] = Helper
					.rundeKaufmaennisch(new BigDecimal(lStueckzeit / 3600000),
							4);
			double lRuestzeit = stuecklisteposition.getL_ruestzeit();
			oZeile[REPORT_VERWENDUNGSNACHWEIS_RUESTZEIT] = Helper
					.rundeKaufmaennisch(new BigDecimal(lRuestzeit / 3600000), 4);

			try {
				oZeile[REPORT_VERWENDUNGSNACHWEIS_LETZTE_VERWENDUNG] = getLagerFac()
						.getDatumLetzterZugangsOderAbgangsbuchung(
								artikelDto.getIId(), true);
				oZeile[REPORT_VERWENDUNGSNACHWEIS_SPERREN] = getArtikelFac()
						.getArtikelsperrenText(artikelDto.getIId());
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			alDaten.add(oZeile);

		}
		while (resultListIterator3.hasNext()) {
			FLRAgstklposition stuecklisteposition = (FLRAgstklposition) resultListIterator3
					.next();
			Object[] oZeile = new Object[REPORT_VERWENDUNGSNACHWEIS_ANZAHL_SPALTEN];
			oZeile[REPORT_VERWENDUNGSNACHWEIS_STUECKLISTE] = "AS "
					+ stuecklisteposition.getFlragstkl().getC_nr();
			oZeile[REPORT_VERWENDUNGSNACHWEIS_BEZEICHNUNG] = stuecklisteposition
					.getFlragstkl().getC_bez();
			oZeile[REPORT_VERWENDUNGSNACHWEIS_MENGE] = stuecklisteposition
					.getN_menge();
			oZeile[REPORT_VERWENDUNGSNACHWEIS_EINHEIT] = stuecklisteposition
					.getEinheit_c_nr();

			oZeile[REPORT_VERWENDUNGSNACHWEIS_VERSTECKT] = Helper
					.short2Boolean(Helper.boolean2Short(false));

			if (stuecklisteposition.getFlrartikel() != null) {
				try {
					oZeile[REPORT_VERWENDUNGSNACHWEIS_LETZTE_VERWENDUNG] = getLagerFac()
							.getDatumLetzterZugangsOderAbgangsbuchung(
									stuecklisteposition.getFlrartikel()
											.getI_id(), true);
					oZeile[REPORT_VERWENDUNGSNACHWEIS_SPERREN] = getArtikelFac()
							.getArtikelsperrenText(
									stuecklisteposition.getFlrartikel()
											.getI_id());
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}

			alDaten.add(oZeile);

		}

		session.close();
		session2.close();
		session3.close();

		// AGSTKLARBEITSPLAN hinzufuegen
		session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Criteria critAgstklAP = session
				.createCriteria(
						com.lp.server.angebotstkl.fastlanereader.generated.FLRAgstklarbeitsplan.class)
				.createAlias(
						AngebotstklpositionFac.FLR_AGSTKLPOSITION_FLRAGSTKL,
						"as")
				.add(Restrictions.eq("as.mandant_c_nr", sMandant))
				.createAlias(
						AngebotstklpositionFac.FLR_AGSTKLPOSITION_FLRARTIKEL,
						"a").add(Restrictions.eq("a.i_id", artikelIId))
				.addOrder(Order.asc("as.c_nr"));

		List<?> resultsAgstklAP = critAgstklAP.list();
		Iterator<?> resultListIteratorAgstklAP = resultsAgstklAP.iterator();

		while (resultListIteratorAgstklAP.hasNext()) {
			FLRAgstklarbeitsplan stuecklisteposition = (FLRAgstklarbeitsplan) resultListIteratorAgstklAP
					.next();

			Object[] oZeile = new Object[REPORT_VERWENDUNGSNACHWEIS_ANZAHL_SPALTEN];
			oZeile[REPORT_VERWENDUNGSNACHWEIS_STUECKLISTE] = "AS "
					+ stuecklisteposition.getFlragstkl().getC_nr();
			oZeile[REPORT_VERWENDUNGSNACHWEIS_BEZEICHNUNG] = stuecklisteposition
					.getFlragstkl().getC_bez();

			oZeile[REPORT_VERWENDUNGSNACHWEIS_ARBEITSGANG] = stuecklisteposition
					.getI_arbeitsgang();
			oZeile[REPORT_VERWENDUNGSNACHWEIS_VERSTECKT] = Helper
					.short2Boolean(Helper.boolean2Short(false));

			double lStueckzeit = stuecklisteposition.getL_stueckzeit();
			oZeile[REPORT_VERWENDUNGSNACHWEIS_STUECKZEIT] = Helper
					.rundeKaufmaennisch(new BigDecimal(lStueckzeit / 3600000),
							4);
			double lRuestzeit = stuecklisteposition.getL_ruestzeit();
			oZeile[REPORT_VERWENDUNGSNACHWEIS_RUESTZEIT] = Helper
					.rundeKaufmaennisch(new BigDecimal(lRuestzeit / 3600000), 4);

			if (stuecklisteposition.getFlrartikel() != null) {
				try {
					oZeile[REPORT_VERWENDUNGSNACHWEIS_LETZTE_VERWENDUNG] = getLagerFac()
							.getDatumLetzterZugangsOderAbgangsbuchung(
									stuecklisteposition.getFlrartikel()
											.getI_id(), true);
					oZeile[REPORT_VERWENDUNGSNACHWEIS_SPERREN] = getArtikelFac()
							.getArtikelsperrenText(
									stuecklisteposition.getFlrartikel()
											.getI_id());
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}

			alDaten.add(oZeile);

		}

		session.close();

		// EK-Angebot
		session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Criteria critEKAngebotPos = session
				.createCriteria(
						com.lp.server.angebotstkl.fastlanereader.generated.FLREinkaufsangebotposition.class)
				.createAlias("flreinkaufsangebot", "ek")
				.add(Restrictions.eq("ek.mandant_c_nr", sMandant))
				.createAlias(
						AngebotstklpositionFac.FLR_AGSTKLPOSITION_FLRARTIKEL,
						"a").add(Restrictions.eq("a.i_id", artikelIId))
				.addOrder(Order.asc("ek.c_nr"));

		List<?> resultsEKAngebotPos = critEKAngebotPos.list();
		Iterator<?> resultListIteratorEKAngebotPos = resultsEKAngebotPos
				.iterator();

		while (resultListIteratorEKAngebotPos.hasNext()) {

			FLREinkaufsangebotposition stuecklisteposition = (FLREinkaufsangebotposition) resultListIteratorEKAngebotPos
					.next();
			Object[] oZeile = new Object[REPORT_VERWENDUNGSNACHWEIS_ANZAHL_SPALTEN];
			oZeile[REPORT_VERWENDUNGSNACHWEIS_STUECKLISTE] = "EK "
					+ stuecklisteposition.getFlreinkaufsangebot().getC_nr();
			oZeile[REPORT_VERWENDUNGSNACHWEIS_BEZEICHNUNG] = stuecklisteposition
					.getFlreinkaufsangebot().getC_projekt();
			oZeile[REPORT_VERWENDUNGSNACHWEIS_MENGE] = stuecklisteposition
					.getN_menge();
			oZeile[REPORT_VERWENDUNGSNACHWEIS_EINHEIT] = stuecklisteposition
					.getEinheit_c_nr();
			oZeile[REPORT_VERWENDUNGSNACHWEIS_VERSTECKT] = Helper
					.short2Boolean(Helper.boolean2Short(false));

			if (stuecklisteposition.getFlrartikel() != null) {
				try {
					oZeile[REPORT_VERWENDUNGSNACHWEIS_LETZTE_VERWENDUNG] = getLagerFac()
							.getDatumLetzterZugangsOderAbgangsbuchung(
									stuecklisteposition.getFlrartikel()
											.getI_id(), true);
					oZeile[REPORT_VERWENDUNGSNACHWEIS_SPERREN] = getArtikelFac()
							.getArtikelsperrenText(
									stuecklisteposition.getFlrartikel()
											.getI_id());
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}

			alDaten.add(oZeile);

		}
		session.close();
		data = new Object[alDaten.size()][REPORT_VERWENDUNGSNACHWEIS_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(data);

		// Nun noch nach Stueckliste sortieren
		for (int m = data.length - 1; m > 0; --m) {
			for (int n = 0; n < m; ++n) {
				Object[] o1 = (Object[]) data[n];
				Object[] o2 = (Object[]) data[n + 1];

				String artnr1 = (String) o1[REPORT_VERWENDUNGSNACHWEIS_STUECKLISTE];
				String artnr2 = (String) o2[REPORT_VERWENDUNGSNACHWEIS_STUECKLISTE];

				if (artnr1.compareToIgnoreCase(artnr2) > 0) {
					data[n] = o2;
					data[n + 1] = o1;
				}
			}
		}

		ArtikelDto dto = getArtikelFac().artikelFindByPrimaryKey(artikelIId,
				theClientDto);
		parameter.put("P_ARTIKEL", dto.formatArtikelbezeichnung());
		parameter.put("P_MITVERSTECKTEN", new Boolean(bMitVersteckten));
		parameter.put("P_MITVERBRAUCHTENMENGEN", new Boolean(
				bMitVerbrauchtenMengen));
		parameter.put("P_VON", tVon);
		parameter.put("P_BIS", tBis);

		initJRDS(parameter, ArtikelReportFac.REPORT_MODUL,
				ArtikelReportFac.REPORT_VERWENDUNGSNACHWEIS, sMandant, locUi,
				theClientDto);
		print = getReportPrint();
		return print;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printFreiInFertigung(Integer artikelIId,
			TheClientDto theClientDto) {
		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT lossollmaterial.los_i_id, sum(lossollmaterial.n_menge) AS n_menge "
				+ " FROM FLRLossollmaterial AS lossollmaterial WHERE lossollmaterial."
				+ FertigungFac.FLR_LOSSOLLMATERIAL_FLRARTIKEL
				+ ".i_id="
				+ artikelIId
				+ " AND lossollmaterial.n_menge > 0 AND lossollmaterial."
				+ FertigungFac.FLR_LOSSOLLMATERIAL_FLRLOS
				+ ".mandant_c_nr='"
				+ theClientDto.getMandant()
				+ "' AND lossollmaterial."
				+ FertigungFac.FLR_LOSSOLLMATERIAL_FLRLOS
				+ ".status_c_nr NOT IN ('"
				+ FertigungFac.STATUS_ERLEDIGT
				+ "','"
				+ FertigungFac.STATUS_STORNIERT
				+ "') GROUP BY lossollmaterial.los_i_id, lossollmaterial."
				+ FertigungFac.FLR_LOSSOLLMATERIAL_FLRARTIKEL
				+ ".i_id ORDER BY lossollmaterial.los_i_id DESC";

		Query inventurliste = session.createQuery(sQuery);

		List<?> resultList = inventurliste.list();

		Iterator<?> resultListIterator = resultList.iterator();

		data = new Object[resultList.size()][6];

		int row = 0;

		while (resultListIterator.hasNext()) {
			Object o[] = (Object[]) resultListIterator.next();

			Integer los_i_id = (Integer) o[0];
			java.math.BigDecimal n_menge = (java.math.BigDecimal) o[1];

			try {
				LosDto losDto = getFertigungFac().losFindByPrimaryKey(los_i_id);
				if (losDto.getAuftragpositionIId() != null) {
					Integer auftrag_i_id = getAuftragpositionFac()
							.auftragpositionFindByPrimaryKey(
									losDto.getAuftragpositionIId())
							.getBelegIId();
					data[row][REPORT_FREIINFERTIGUNG_AUFTRAG] = getAuftragFac()
							.auftragFindByPrimaryKey(auftrag_i_id).getCNr();
				}

				Session session2 = FLRSessionFactory.getFactory().openSession();

				String sQueryAblieferungen = "SELECT sum(losablieferung.n_menge) AS n_menge "
						+ " FROM FLRLosablieferung AS losablieferung WHERE losablieferung.flrlos.i_id="
						+ losDto.getIId() + " GROUP BY losablieferung.los_i_id";

				Query ablieferungen = session2.createQuery(sQueryAblieferungen);

				java.math.BigDecimal abgelieferteMange = new java.math.BigDecimal(
						0);

				if (ablieferungen.list().iterator().hasNext()) {
					abgelieferteMange = (java.math.BigDecimal) ablieferungen
							.list().iterator().next();
				}
				session2.close();

				java.math.BigDecimal teil1 = losDto.getNLosgroesse().subtract(
						abgelieferteMange);

				java.math.BigDecimal teil2 = n_menge.divide(
						losDto.getNLosgroesse(),
						java.math.BigDecimal.ROUND_HALF_EVEN);

				data[row][REPORT_FREIINFERTIGUNG_BEGINN] = new java.util.Date(
						losDto.getTProduktionsbeginn().getTime());
				data[row][REPORT_FREIINFERTIGUNG_ENDE] = new java.util.Date(
						losDto.getTProduktionsende().getTime());
				data[row][REPORT_FREIINFERTIGUNG_FREIEMENGE] = teil1
						.multiply(teil2);
				data[row][REPORT_FREIINFERTIGUNG_LOSNR] = losDto.getCNr();
				data[row][REPORT_FREIINFERTIGUNG_PROJEKT] = losDto
						.getCProjekt();

			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}

			row++;

		}
		session.close();
		HashMap<String, Object> parameter = new HashMap<String, Object>();

		ArtikelDto dto = getArtikelFac().artikelFindByPrimaryKey(artikelIId,
				theClientDto);
		parameter.put("P_ARTIKEL", dto.formatArtikelbezeichnung());

		index = -1;
		sAktuellerReport = ArtikelReportFac.REPORT_FREIINFERTIGUNG;

		initJRDS(parameter, ArtikelReportFac.REPORT_MODUL,
				ArtikelReportFac.REPORT_FREIINFERTIGUNG,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printArtikelstammblatt(Integer artikelIId,
			TheClientDto theClientDto) {
		// Erstellung des Reports
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		index = -1;
		sAktuellerReport = ArtikelReportFac.REPORT_ARTIKELSTAMMBLATT;
		data = new Object[1][26];

		try {

			boolean darfEinkaufspreisSehen = false;
			boolean darfVerkaufspreisSehen = false;

			darfEinkaufspreisSehen = getTheJudgeFac().hatRecht(
					RechteFac.RECHT_LP_DARF_PREISE_SEHEN_EINKAUF, theClientDto);
			darfVerkaufspreisSehen = getTheJudgeFac().hatRecht(
					RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF, theClientDto);

			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(
					artikelIId, theClientDto);

			data[0][REPORT_ARTIKELSTAMMBLATT_ARTIKELNUMMER] = artikelDto
					.getCNr();

			byte[] bild = getArtikelkommentarFac().getArtikeldefaultBild(
					artikelIId, theClientDto);

			if (bild != null) {
				java.awt.Image myImage = Helper.byteArrayToImage(bild);
				data[0][REPORT_ARTIKELSTAMMBLATT_BILD] = myImage;
			}

			if (artikelDto.getArtikelsprDto() != null) {
				data[0][REPORT_ARTIKELSTAMMBLATT_BEZEICHNUNG] = artikelDto
						.getArtikelsprDto().getCBez();
				data[0][REPORT_ARTIKELSTAMMBLATT_ZUSATZBEZEICHNUNG] = artikelDto
						.getArtikelsprDto().getCZbez();
				data[0][REPORT_ARTIKELSTAMMBLATT_KURZBEZEICHNUNG] = artikelDto
						.getArtikelsprDto().getCKbez();
			}

			ArtikellieferantDto[] artikellieferantDtos = getArtikelFac()
					.artikellieferantFindByArtikelIId(artikelIId, theClientDto);

			if (artikellieferantDtos != null && artikellieferantDtos.length > 0) {
				data[0][REPORT_ARTIKELSTAMMBLATT_LIEFERANT] = getLieferantFac()
						.lieferantFindByPrimaryKey(
								artikellieferantDtos[0].getLieferantIId(),
								theClientDto).getPartnerDto().formatAnrede();
				data[0][REPORT_ARTIKELSTAMMBLATT_ARTIKELNUMMERLIEFERANT] = artikellieferantDtos[0]
						.getCArtikelnrlieferant();
			}

			if (artikelDto.getMaterialIId() != null) {
				data[0][REPORT_ARTIKELSTAMMBLATT_MATERIAL] = getMaterialFac()
						.materialFindByPrimaryKey(artikelDto.getMaterialIId(),
								theClientDto).getCNr();
			}
			data[0][REPORT_ARTIKELSTAMMBLATT_GEWICHT] = artikelDto
					.getFGewichtkg();
			data[0][REPORT_ARTIKELSTAMMBLATT_WARENVERKEHRSNUMMER] = artikelDto
					.getCWarenverkehrsnummer();
			data[0][REPORT_ARTIKELSTAMMBLATT_ECCN] = artikelDto.getCEccn();
			data[0][REPORT_ARTIKELSTAMMBLATT_EAN] = artikelDto
					.getCVerkaufseannr();
			data[0][REPORT_ARTIKELSTAMMBLATT_VERPACKUNGSEINHEIT] = artikelDto
					.getFVerpackungsmenge();
			data[0][REPORT_ARTIKELSTAMMBLATT_LAGERSTAND] = getLagerFac()
					.getLagerstandAllerLagerEinesMandanten(artikelDto.getIId(),
							theClientDto);
			data[0][REPORT_ARTIKELSTAMMBLATT_LAGERPLATZ] = getLagerFac()
					.getLagerplaezteEinesArtikels(artikelDto.getIId(), null);

			data[0][REPORT_ARTIKELSTAMMBLATT_RESERVIERT] = getReservierungFac()
					.getAnzahlReservierungen(artikelDto.getIId(), theClientDto);
			data[0][REPORT_ARTIKELSTAMMBLATT_FEHLMENGE] = getFehlmengeFac()
					.getAnzahlFehlmengeEinesArtikels(artikelDto.getIId(),
							theClientDto);
			data[0][REPORT_ARTIKELSTAMMBLATT_INFERTIGUNG] = getFertigungFac()
					.getAnzahlInFertigung(artikelIId, theClientDto);
			data[0][REPORT_ARTIKELSTAMMBLATT_BESTELLT] = getArtikelbestelltFac()
					.getAnzahlBestellt(artikelDto.getIId());
			data[0][REPORT_ARTIKELSTAMMBLATT_RAHMENRESERVIERT] = getReservierungFac()
					.getAnzahlRahmenreservierungen(artikelDto.getIId(),
							theClientDto);

			BigDecimal rahmenbestellt = null;
			Hashtable htAnzahlRahmenbestellt = getArtikelbestelltFac()
					.getAnzahlRahmenbestellt(artikelDto.getIId(), theClientDto);
			if (htAnzahlRahmenbestellt
					.containsKey(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL)) {
				rahmenbestellt = (BigDecimal) htAnzahlRahmenbestellt
						.get(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL);
				data[0][REPORT_ARTIKELSTAMMBLATT_RAHMENBESTELLT] = rahmenbestellt;
			}

			data[0][REPORT_ARTIKELSTAMMBLATT_DETAILBEDARF] = getRahmenbedarfeFac()
					.getSummeAllerRahmenbedarfeEinesArtikels(
							artikelDto.getIId());

			LagerDto[] lagerDtos = getLagerFac().lagerFindByMandantCNr(
					theClientDto.getMandant());

			Object[][] dataSub = new Object[lagerDtos.length][2];
			String[] fieldnames = new String[] { "F_LAGER", "F_LAGERSTAND" };

			for (int i = 0; i < lagerDtos.length; i++) {
				dataSub[i][0] = lagerDtos[i].getCNr();
				dataSub[i][1] = getLagerFac().getLagerstand(
						artikelDto.getIId(), lagerDtos[i].getIId(),
						theClientDto);
			}

			data[0][REPORT_ARTIKELSTAMMBLATT_SUBREPORT_LAGERSTAENDE] = new LPDatenSubreport(
					dataSub, fieldnames);

			if (darfEinkaufspreisSehen) {

				ArtikellieferantDto liefrant = getArtikelFac()
						.getArtikelEinkaufspreis(artikelDto.getIId(),
								new BigDecimal(1),
								theClientDto.getSMandantenwaehrung(),
								theClientDto);

				if (liefrant != null) {
					data[0][REPORT_ARTIKELSTAMMBLATT_EINKAUFSPREIS] = liefrant
							.getLief1Preis();
				}
			}
			if (darfVerkaufspreisSehen) {

				try {
					VkPreisfindungEinzelverkaufspreisDto dto = getVkPreisfindungFac()
							.getArtikeleinzelverkaufspreis(
									artikelDto.getIId(),
									new java.sql.Date(System
											.currentTimeMillis()),
									theClientDto.getSMandantenwaehrung(),
									theClientDto);
					if (dto != null) {
						data[0][REPORT_ARTIKELSTAMMBLATT_VERKAUFSPREIS] = dto
								.getNVerkaufspreisbasis();
					}
				} catch (RemoteException ex1) {
					// NOTHING HERE
				}
			}
			ArtikelkommentarDto[] dtos = getArtikelkommentarFac()
					.artikelkommentardruckFindByArtikelIIdBelegartCNr(
							artikelDto.getIId(), LocaleFac.BELEGART_ANGEBOT,
							theClientDto.getLocUiAsString(), theClientDto);

			if (dtos != null && dtos.length > 0) {

				for (int i = 0; i < dtos.length; i++) {
					if (dtos[i].getDatenformatCNr().equals(
							MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML)
							&& dtos[i].getArtikelkommentarsprDto() != null) {
						data[0][REPORT_ARTIKELSTAMMBLATT_LANGTEXT] = dtos[0]
								.getArtikelkommentarsprDto().getXKommentar();
					}
					break;
				}
			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		parameter = new HashMap<String, Object>();
		parameter.put("P_WAEHRUNG", theClientDto.getSMandantenwaehrung());

		initJRDS(parameter, ArtikelReportFac.REPORT_MODUL,
				ArtikelReportFac.REPORT_ARTIKELSTAMMBLATT,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAuftragsseriennummern(Integer artikelIId,
			java.sql.Date dVon, java.sql.Date dBis, TheClientDto theClientDto) {

		sAktuellerReport = ArtikelReportFac.REPORT_AUFTRAGSSERIENNUMMERN;

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria bestelltliste = session
				.createCriteria(FLRAuftragseriennrn.class);
		bestelltliste.add(Restrictions.eq("artikel_i_id", artikelIId));

		bestelltliste.createAlias("flrauftragposition", "ap");
		bestelltliste.createAlias("ap.flrauftrag", "a");

		if (dVon != null) {
			bestelltliste.add(Restrictions.ge("a."
					+ AuftragFac.FLR_AUFTRAG_D_BELEGDATUM, dVon));
		}
		if (dBis != null) {
			bestelltliste.add(Restrictions.lt("a."
					+ AuftragFac.FLR_AUFTRAG_D_BELEGDATUM, dBis));
		}

		bestelltliste.addOrder(Order.desc("c_seriennr"));

		List<?> resultList = bestelltliste.list();
		Iterator<?> resultListIterator = resultList.iterator();

		data = new Object[resultList.size()][REPORT_AUFTRAGSERIENNR_ANZAHL_SPALTEN];
		int row = 0;
		while (resultListIterator.hasNext()) {
			FLRAuftragseriennrn auftser = (FLRAuftragseriennrn) resultListIterator
					.next();
			data[row][REPORT_AUFTRAGSERIENNR_SERIENNUMMER] = auftser
					.getC_seriennr();
			data[row][REPORT_AUFTRAGSERIENNR_AUFTRAG] = auftser
					.getFlrauftragposition().getFlrauftrag().getC_nr();
			data[row][REPORT_AUFTRAGSERIENNR_BELEGDATUM] = new Timestamp(
					auftser.getFlrauftragposition().getFlrauftrag()
							.getT_belegdatum().getTime());
			data[row][REPORT_AUFTRAGSERIENNR_PROJEKT] = auftser
					.getFlrauftragposition().getFlrauftrag().getC_bez();

			String kunde = auftser.getFlrauftragposition().getFlrauftrag()
					.getFlrkunde().getFlrpartner()
					.getC_name1nachnamefirmazeile1();
			if (auftser.getFlrauftragposition().getFlrauftrag().getFlrkunde()
					.getFlrpartner().getC_name2vornamefirmazeile2() != null) {
				kunde += " "
						+ auftser.getFlrauftragposition().getFlrauftrag()
								.getFlrkunde().getFlrpartner()
								.getC_name2vornamefirmazeile2();
			}

			data[row][REPORT_AUFTRAGSERIENNR_KUNDE] = kunde;
			row++;

		}

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		ArtikelDto dto = getArtikelFac().artikelFindByPrimaryKey(artikelIId,
				theClientDto);
		parameter.put("P_ARTIKEL", dto.formatArtikelbezeichnung());

		initJRDS(parameter, ArtikelReportFac.REPORT_MODUL,
				ArtikelReportFac.REPORT_AUFTRAGSSERIENNUMMERN,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printBewegungsvorschau(Integer artikelId,
			boolean bInternebestellungMiteinbeziehen, TheClientDto theClientDto) {

		HashMap<String, Object> parameter = new HashMap<String, Object>();
		boolean bAuftragStundenMinuten = false;

		try {
			ArtikelDto dto = getArtikelFac().artikelFindByPrimaryKey(artikelId,
					theClientDto);
			parameter.put("P_ARTIKEL", dto.formatArtikelbezeichnung());
			parameter.put("P_ARTIKELEINHEIT", dto.getEinheitCNr().trim());
			parameter.put("P_BESTELLEINHEIT", dto.getEinheitCNrBestellung());
			parameter.put("P_MULTIPLIKATORBESTELLMENGE",
					dto.getNUmrechnungsfaktor());
			parameter.put("P_BESTELLEINHEIT_INVERS",
					Helper.short2Boolean(dto.getbBestellmengeneinheitInvers()));
			parameter.put("P_LAGERMINDESTSTAND", dto.getFLagermindest());
			parameter.put("P_UEBERPRODUKTION", dto.getFUeberproduktion());
			parameter.put("P_LAGERSOLLSTAND", dto.getFLagersoll());
			parameter.put("P_FERTIGUNGSSATZGROESSE",
					dto.getFFertigungssatzgroesse());
			ZusatzfunktionberechtigungDto zusatzfunktionberechtigungDto = getMandantFac()
					.zusatzfunktionberechtigungFindByPrimaryKey(
							MandantFac.ZUSATZFUNKTION_AUFTRAGTERMIN_STUNDEN_MINUTEN,
							theClientDto.getMandant());
			if (zusatzfunktionberechtigungDto != null) {
				bAuftragStundenMinuten = true;
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		index = -1;
		sAktuellerReport = ArtikelReportFac.REPORT_BEWEGUNGSVORSCHAU;

		try {
			// SP 1008 OHNE Internebestellung
			ArrayList<?> list = getInternebestellungFac()
					.getBewegungsvorschauSortiert(artikelId, false,
							theClientDto);

			BewegungsvorschauDto[] returnArray = new BewegungsvorschauDto[list
					.size()];
			BewegungsvorschauDto[] dtos = (com.lp.server.bestellung.service.BewegungsvorschauDto[]) list
					.toArray(returnArray);
			LagerDto lagerDto = getLagerFac().getHauptlagerDesMandanten(
					theClientDto);
			LagerDto[] allelaegerDtos = null;

			if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
					MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
					theClientDto)) {
				allelaegerDtos = getLagerFac().lagerFindAll();
			} else {
				allelaegerDtos = getLagerFac().lagerFindByMandantCNr(
						theClientDto.getMandant());
			}

			int row = 0;
			data = new Object[list.size() + allelaegerDtos.length][REPORT_BEWEGUNGSVORSCHAU_ANZAHL_FELDER];
			BigDecimal anfangslagerstand = new BigDecimal(0);
			if (Helper.short2boolean(lagerDto.getBBestellvorschlag())
					|| Helper.short2boolean(lagerDto.getBInternebestellung())) {
				anfangslagerstand = getLagerFac().getLagerstand(artikelId,
						lagerDto.getIId(), theClientDto);
			}
			data[0][REPORT_BEWEGUNGSVORSCHAU_BELEGART] = "Lagerstand";
			data[0][REPORT_BEWEGUNGSVORSCHAU_BELEGNR] = lagerDto.getCNr();
			data[0][REPORT_BEWEGUNGSVORSCHAU_MANDANT] = lagerDto
					.getMandantCNr();
			data[0][REPORT_BEWEGUNGSVORSCHAU_PARTNER] = LagerFac.LAGERART_HAUPTLAGER
					.trim();
			data[0][REPORT_BEWEGUNGSVORSCHAU_MENGE] = getLagerFac()
					.getLagerstand(artikelId, lagerDto.getIId(), theClientDto);
			data[0][REPORT_BEWEGUNGSVORSCHAU_FIKTIVERLAGERSTAND] = anfangslagerstand;
			data[0][REPORT_BEWEGUNGSVORSCHAU_BESTELLVORSCHLAG_BERUECKSICHTIGT_LAGER] = Helper
					.short2Boolean(lagerDto.getBBestellvorschlag());
			data[0][REPORT_BEWEGUNGSVORSCHAU_INTERNEBESTELLUNG_BERUECKSICHTIGT_LAGER] = Helper
					.short2Boolean(lagerDto.getBInternebestellung());
			if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
					MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
					theClientDto)) {
				for (int i = 0; i < allelaegerDtos.length; i++) {
					LagerDto dto = allelaegerDtos[i];
					if (!dto.getIId().equals(lagerDto.getIId())) {
						row++;
						data[row][REPORT_BEWEGUNGSVORSCHAU_BELEGART] = "Lagerstand";
						data[row][REPORT_BEWEGUNGSVORSCHAU_BELEGNR] = dto
								.getCNr();
						data[row][REPORT_BEWEGUNGSVORSCHAU_MANDANT] = dto
								.getMandantCNr();
						data[row][REPORT_BEWEGUNGSVORSCHAU_PARTNER] = dto
								.getLagerartCNr();
						BigDecimal bdLagerstandActLager = getLagerFac()
								.getLagerstand(artikelId, dto.getIId(),
										theClientDto);
						data[row][REPORT_BEWEGUNGSVORSCHAU_MENGE] = bdLagerstandActLager;
						if (Helper.short2boolean(dto.getBBestellvorschlag())
								|| Helper.short2boolean(dto
										.getBInternebestellung())) {
							// Lagerstand der Laeger aendert sich nur wenn im
							// Artikel
							// beruecksichigen aktiviert wurde
							anfangslagerstand = anfangslagerstand
									.add(bdLagerstandActLager);
						}
						data[row][REPORT_BEWEGUNGSVORSCHAU_FIKTIVERLAGERSTAND] = anfangslagerstand;
						data[row][REPORT_BEWEGUNGSVORSCHAU_BESTELLVORSCHLAG_BERUECKSICHTIGT_LAGER] = Helper
								.short2Boolean(dto.getBBestellvorschlag());
						data[row][REPORT_BEWEGUNGSVORSCHAU_INTERNEBESTELLUNG_BERUECKSICHTIGT_LAGER] = Helper
								.short2Boolean(dto.getBInternebestellung());
					}
				}
			} else {
				for (int i = 0; i < allelaegerDtos.length; i++) {
					LagerDto dto = allelaegerDtos[i];
					if (!dto.getLagerartCNr().equals(
							LagerFac.LAGERART_HAUPTLAGER)) {
						row++;
						data[row][REPORT_BEWEGUNGSVORSCHAU_BELEGART] = "Lagerstand";
						data[row][REPORT_BEWEGUNGSVORSCHAU_BELEGNR] = dto
								.getCNr();
						data[row][REPORT_BEWEGUNGSVORSCHAU_MANDANT] = dto
								.getMandantCNr();
						data[row][REPORT_BEWEGUNGSVORSCHAU_PARTNER] = dto
								.getLagerartCNr();
						BigDecimal bdLagerstandActLager = getLagerFac()
								.getLagerstand(artikelId, dto.getIId(),
										theClientDto);
						data[row][REPORT_BEWEGUNGSVORSCHAU_MENGE] = bdLagerstandActLager;
						if (Helper.short2boolean(dto.getBBestellvorschlag())
								|| Helper.short2boolean(dto
										.getBInternebestellung())) {
							// Lagerstand der Laeger aendert sich nur wenn im
							// Artikel
							// beruecksichigen aktiviert wurde
							anfangslagerstand = anfangslagerstand
									.add(bdLagerstandActLager);
						}
						data[row][REPORT_BEWEGUNGSVORSCHAU_FIKTIVERLAGERSTAND] = anfangslagerstand;
						data[row][REPORT_BEWEGUNGSVORSCHAU_BESTELLVORSCHLAG_BERUECKSICHTIGT_LAGER] = Helper
								.short2Boolean(dto.getBBestellvorschlag());
						data[row][REPORT_BEWEGUNGSVORSCHAU_INTERNEBESTELLUNG_BERUECKSICHTIGT_LAGER] = Helper
								.short2Boolean(dto.getBInternebestellung());
					}
				}
			}

			for (int i = 0; i < dtos.length; i++) {
				row++;
				BewegungsvorschauDto dto = dtos[i];

				data[row][REPORT_BEWEGUNGSVORSCHAU_BELEGART] = dto
						.getCBelegartCNr();
				data[row][REPORT_BEWEGUNGSVORSCHAU_BELEGNR] = dto
						.getCBelegnummer();
				data[row][REPORT_BEWEGUNGSVORSCHAU_MANDANT] = dto
						.getMandantCNr();

				if (dto.getCBelegartCNr() != null
						&& dto.getCBelegartCNr().equals(LocaleFac.BELEGART_LOS)) {

					LosDto losDto = null;
					try {
						losDto = getFertigungFac().losFindByPrimaryKey(
								dto.getIBelegIId());
					} catch (EJBExceptionLP e) {
						// Los nicht mehr vorhanden
					}

					if (losDto != null && losDto.getAuftragIId() != null) {
						AuftragDto aDto = getAuftragFac()
								.auftragFindByPrimaryKey(losDto.getAuftragIId());

						data[row][REPORT_BEWEGUNGSVORSCHAU_LOS_AUFTRAGSNUMMER] = aDto
								.getCNr();
					}
				}

				data[row][REPORT_BEWEGUNGSVORSCHAU_PROJEKT] = dto.getCProjekt();
				data[row][REPORT_BEWEGUNGSVORSCHAU_MENGE] = dto.getNMenge();
				data[row][REPORT_BEWEGUNGSVORSCHAU_LIEFERTERMIN] = dto
						.getTLiefertermin();

				if (dto.getCBelegartCNr() != null
						&& dto.getCBelegartCNr().equals(
								LocaleFac.BELEGART_BESTELLUNG)
						&& dto.getIBelegPositionIId() != null) {
					BestellpositionDto bestPosDto = getBestellpositionFac()
							.bestellpositionFindByPrimaryKeyOhneExc(
									dto.getIBelegPositionIId());
					data[row][REPORT_BEWEGUNGSVORSCHAU_BEST_ABNUMMER] = bestPosDto
							.getCABNummer();
					data[row][REPORT_BEWEGUNGSVORSCHAU_BEST_ABTERMIN] = dto
							.getTABTerminBestellung();
				}
				if (bAuftragStundenMinuten)
					if (dto.getCBelegartCNr()
							.equals(LocaleFac.BELEGART_AUFTRAG)) {
						AuftragDto auftragDto = getAuftragFac()
								.auftragFindByPrimaryKey(dto.getIBelegIId());
						data[row][REPORT_BEWEGUNGSVORSCHAU_FINALTERMIN] = Helper
								.formatTimestamp(auftragDto.getDFinaltermin(),
										theClientDto.getLocMandant());
					}
				anfangslagerstand = anfangslagerstand.add(dto.getNMenge());

				data[row][REPORT_BEWEGUNGSVORSCHAU_FIKTIVERLAGERSTAND] = anfangslagerstand;
				if (dto.getPartnerDto() != null) {
					data[row][REPORT_BEWEGUNGSVORSCHAU_PARTNER] = dto
							.getPartnerDto().formatFixTitelName1Name2();
				}
				data[row][REPORT_BEWEGUNGSVORSCHAU_BESTELLVORSCHLAG_BERUECKSICHTIGT_LAGER] = null;
				data[row][REPORT_BEWEGUNGSVORSCHAU_INTERNEBESTELLUNG_BERUECKSICHTIGT_LAGER] = null;
			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		initJRDS(parameter, ArtikelReportFac.REPORT_MODUL,
				ArtikelReportFac.REPORT_BEWEGUNGSVORSCHAU,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);
		return getReportPrint();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printArtikelstatistik(Integer artikelIId,
			java.sql.Date dVon, java.sql.Date dBis, Integer iOption,
			boolean bMonatsstatistik, boolean bEingeschraenkt,
			boolean bMitHandlagerbewegungen, boolean bMitBewegungsvorschau,
			boolean bMitHistory, TheClientDto theClientDto)
			throws RemoteException {
		if (artikelIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelIId == null"));
		}

		boolean bWeReferenzAndrucken = false;
		try {
			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL,
							ParameterFac.PARAMETER_WE_REFERENZ_IN_STATISTIK);
			bWeReferenzAndrucken = ((Boolean) parameter.getCWertAsObject())
					.booleanValue();

		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		// Erstellung des Reports
		HashMap<String, Object> parameter = new HashMap<String, Object>();

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session
				.createCriteria(FLRLagerbewegung.class)
				.createAlias("flrartikel", "a")
				.add(Restrictions.eq("a.i_id", artikelIId))

				.createAlias("flrlager", "l");

		if (bMitHistory == false) {
			crit.add(Restrictions.eq("b_historie", Helper.boolean2Short(false)));
		}

		parameter.put("P_MITBUCHUNGSDETAILS", new Boolean(bMitHistory));

		parameter.put("P_MITBEWEGUNGSVORSCHAU", new Boolean(
				bMitBewegungsvorschau));
		parameter.put("P_EINGESCHRAENKT", new Boolean(bEingeschraenkt));

		if (!getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
				MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, theClientDto)) {
			crit.add(Restrictions.eq("l.mandant_c_nr",
					theClientDto.getMandant()));

		} else {
			if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
					MandantFac.ZUSATZFUNKTION_GETRENNTE_LAGER, theClientDto)) {
				crit.add(Restrictions.eq("l.mandant_c_nr",
						theClientDto.getMandant()));

			}
		}
		// PJ 17221
		// crit.add(
		// Restrictions.not(Restrictions.eq("l.lagerart_c_nr",
		// LagerFac.LAGERART_WERTGUTSCHRIFT)));
		crit.addOrder(Order.desc(LagerFac.FLR_LAGERBEWEGUNG_T_BELEGDATUM))
				.addOrder(Order.desc(LagerFac.FLR_LAGERBEWEGUNG_T_BUCHUNGSZEIT))
				.addOrder(Order.desc(LagerFac.FLR_LAGERBEWEGUNG_B_ABGANG));
		if (dVon != null) {
			crit.add(Restrictions.ge(LagerFac.FLR_LAGERBEWEGUNG_T_BELEGDATUM,
					dVon));
			parameter.put("P_VON", new java.sql.Timestamp(dVon.getTime()));
		}
		if (dBis != null) {
			crit.add(Restrictions.lt(LagerFac.FLR_LAGERBEWEGUNG_T_BELEGDATUM,
					new java.sql.Timestamp(dBis.getTime() + 24 * 3600000)));
			parameter.put("P_BIS", new java.sql.Timestamp(dBis.getTime()));
		}

		boolean darfEinkaufspreisSehen = getTheJudgeFac().hatRecht(
				RechteFac.RECHT_LP_DARF_PREISE_SEHEN_EINKAUF, theClientDto);
		boolean darfVerkaufspreisSehen = getTheJudgeFac().hatRecht(
				RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF, theClientDto);
		parameter.put("P_RECHT_LP_DARF_PREISE_SEHEN_EINKAUF",
				darfEinkaufspreisSehen);
		parameter.put("P_RECHT_LP_DARF_PREISE_SEHEN_VERKAUF",
				darfVerkaufspreisSehen);
		if (iOption == ArtikelFac.REPORT_ARTIKELSTATISTIK_OPTION_ALLE) {
			parameter.put("P_MITSUMMENZEILE", new Boolean(false));
			parameter.put(
					"P_BELEGARTEN",
					getTextRespectUISpr("lp.alle", theClientDto.getMandant(),
							theClientDto.getLocUi()));
			parameter.put("P_BELEGART_AUSWAHL", "lp.alle");
		} else if (iOption == ArtikelFac.REPORT_ARTIKELSTATISTIK_OPTION_EK) {
			parameter.put("P_MITSUMMENZEILE", new Boolean(true));
			parameter
					.put("P_BELEGARTEN",
							getTextRespectUISpr("lp.einkauf",
									theClientDto.getMandant(),
									theClientDto.getLocUi()));
			String[] belegarten = new String[1];
			belegarten[0] = LocaleFac.BELEGART_BESTELLUNG;
			crit.add(Restrictions.in(LagerFac.FLR_LAGERBEWEGUNG_C_BELEGARTNR,
					belegarten));
			parameter.put("P_BELEGART_AUSWAHL", "lp.einkauf");
		} else if (iOption == ArtikelFac.REPORT_ARTIKELSTATISTIK_OPTION_VK) {
			parameter.put("P_MITSUMMENZEILE", new Boolean(true));
			parameter
					.put("P_BELEGARTEN",
							getTextRespectUISpr("lp.verkauf",
									theClientDto.getMandant(),
									theClientDto.getLocUi()));
			String[] belegarten = new String[3];
			belegarten[0] = LocaleFac.BELEGART_RECHNUNG;
			belegarten[1] = LocaleFac.BELEGART_LIEFERSCHEIN;
			belegarten[2] = LocaleFac.BELEGART_GUTSCHRIFT;
			crit.add(Restrictions.in(LagerFac.FLR_LAGERBEWEGUNG_C_BELEGARTNR,
					belegarten));
			parameter.put("P_BELEGART_AUSWAHL", "lp.verkauf");
		} else if (iOption == ArtikelFac.REPORT_ARTIKELSTATISTIK_OPTION_FERTIGUNG) {
			parameter.put("P_MITSUMMENZEILE", new Boolean(true));
			parameter
					.put("P_BELEGARTEN",
							getTextRespectUISpr("lp.fertigung",
									theClientDto.getMandant(),
									theClientDto.getLocUi()));
			String[] belegarten = new String[2];
			belegarten[0] = LocaleFac.BELEGART_LOS;
			belegarten[1] = LocaleFac.BELEGART_LOSABLIEFERUNG;
			crit.add(Restrictions.in(LagerFac.FLR_LAGERBEWEGUNG_C_BELEGARTNR,
					belegarten));
			parameter.put("P_BELEGART_AUSWAHL", "lp.fertigung");
		}

		if (bMitHandlagerbewegungen == false) {
			String[] belegarten = new String[1];
			belegarten[0] = LocaleFac.BELEGART_HAND;
			crit.add(Restrictions.not(Restrictions.in(
					LagerFac.FLR_LAGERBEWEGUNG_C_BELEGARTNR, belegarten)));

		}
		parameter.put("P_MITHANDLAGERBEWEGUNGEN", new Boolean(
				bMitHandlagerbewegungen));

		if (bEingeschraenkt) {
			crit.setMaxResults(50);
		}

		List<?> results = crit.list();

		Iterator<?> resultListIterator = results.iterator();

		ArrayList<Object[]> al = new ArrayList<Object[]>();

		// PJ 14202

		Session sessionInv = FLRSessionFactory.getFactory().openSession();

		String sQuery = "FROM FLRInventurstand AS i WHERE i.flrartikel.i_id="
				+ artikelIId;
		// SP3180
		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
				MandantFac.ZUSATZFUNKTION_GETRENNTE_LAGER, theClientDto)) {

			sQuery += " AND i.flrlager.mandant_c_nr='"
					+ theClientDto.getMandant() + "'";

		}

		if (dVon != null) {

			sQuery += " AND i.flrinventur.t_inventurdatum>='"
					+ Helper.formatDateWithSlashes(dVon) + "'";

		}
		if (dBis != null) {

			sQuery += " AND i.flrinventur.t_inventurdatum<='"
					+ Helper.formatDateWithSlashes(dBis) + "'";

		}
		sQuery += " ORDER BY i.flrinventur.t_inventurdatum DESC";

		Query inventurliste = sessionInv.createQuery(sQuery);

		ArrayList alInventurliste = new ArrayList();

		List<?> resultList = inventurliste.list();

		Iterator<?> resultListInventur = resultList.iterator();

		while (resultListInventur.hasNext()) {
			FLRInventurstand item = (FLRInventurstand) resultListInventur
					.next();

			alInventurliste.add(item);

		}

		while (resultListIterator.hasNext()) {
			FLRLagerbewegung lagerbewegung = (FLRLagerbewegung) resultListIterator
					.next();

			if (bMitHistory == true
					|| lagerbewegung.getN_menge().doubleValue() > 0) {

				while (alInventurliste.size() > 0) {
					FLRInventurstand flr = (FLRInventurstand) alInventurliste
							.get(0);

					if (lagerbewegung.getT_belegdatum().getTime() <= flr
							.getFlrinventur().getT_inventurdatum().getTime()) {

						Object[] zeile = new Object[REPORT_ARTIKELSTATISTIK_ANZAHL_SPALTEN];
						zeile[REPORT_ARTIKELSTATISTIK_BEWEGUNGSVORSCHAU] = new Boolean(
								false);
						zeile[REPORT_ARTIKELSTATISTIK_BELEGART] = "Inventurstand";

						java.sql.Timestamp ts = new java.sql.Timestamp(flr
								.getFlrinventur().getT_inventurdatum()
								.getTime());
						zeile[REPORT_ARTIKELSTATISTIK_SORTIERDATUM] = ts;
						zeile[REPORT_ARTIKELSTATISTIK_BELEGDATUM] = ts;
						zeile[REPORT_ARTIKELSTATISTIK_BUCHUNGSZEIT] = ts;

						zeile[REPORT_ARTIKELSTATISTIK_INVENTURMENGE] = flr
								.getN_inventurmenge();
						zeile[REPORT_ARTIKELSTATISTIK_BELEGNUMMER] = flr
								.getFlrinventur().getC_bez();
						zeile[REPORT_ARTIKELSTATISTIK_LAGER] = flr
								.getFlrlager().getC_nr();

						al.add(zeile);
						alInventurliste.remove(0);
					} else {
						break;
					}
				}

				Object[] zeile = new Object[REPORT_ARTIKELSTATISTIK_ANZAHL_SPALTEN];
				zeile[REPORT_ARTIKELSTATISTIK_BEWEGUNGSVORSCHAU] = new Boolean(
						false);
				String sBelegartCNr = lagerbewegung.getFlrbelegart().getC_nr();
				zeile[REPORT_ARTIKELSTATISTIK_BELEGARTCNR] = sBelegartCNr;

				if (lagerbewegung.getFlrlager().getLagerart_c_nr()
						.equals(LagerFac.LAGERART_WERTGUTSCHRIFT)) {
					zeile[REPORT_ARTIKELSTATISTIK_BELEGART] = LagerFac.LAGERART_WERTGUTSCHRIFT;
				} else {
					zeile[REPORT_ARTIKELSTATISTIK_BELEGART] = lagerbewegung
							.getC_belegartnr();
				}

				if (lagerbewegung.getC_belegartnr().equals(
						LocaleFac.BELEGART_LOS)
						|| lagerbewegung.getC_belegartnr().equals(
								LocaleFac.BELEGART_LOSABLIEFERUNG)) {
					LosDto losDto = getFertigungFac().losFindByPrimaryKey(
							lagerbewegung.getI_belegartid());
					if (losDto.getStuecklisteIId() != null) {
						zeile[REPORT_ARTIKELSTATISTIK_STUECKLISTENARTIKEL] = getStuecklisteFac()
								.stuecklisteFindByPrimaryKey(
										losDto.getStuecklisteIId(),
										theClientDto).getArtikelDto()
								.formatArtikelbezeichnung();
					} else {
						zeile[REPORT_ARTIKELSTATISTIK_STUECKLISTENARTIKEL] = "Materialliste";
					}

					zeile[REPORT_ARTIKELSTATISTIK_LOS_STATUS] = losDto
							.getStatusCNr();
					zeile[REPORT_ARTIKELSTATISTIK_LOS_BEGINN] = losDto
							.getTProduktionsbeginn();
					zeile[REPORT_ARTIKELSTATISTIK_LOS_ENDE] = losDto
							.getTProduktionsende();
					zeile[REPORT_ARTIKELSTATISTIK_LOS_PROJEKT] = losDto
							.getCProjekt();

					zeile[REPORT_ARTIKELSTATISTIK_LOS_ABGELIFERT] = getFertigungFac()
							.getErledigteMenge(losDto.getIId(), theClientDto);

					if (losDto.getAuftragIId() != null) {
						zeile[REPORT_ARTIKELSTATISTIK_LOS_AUFTRAG] = getAuftragFac()
								.auftragFindByPrimaryKey(losDto.getAuftragIId())
								.getCNr();
					}

				}

				if (lagerbewegung.getC_belegartnr().equals(
						LocaleFac.BELEGART_LIEFERSCHEIN)
						|| lagerbewegung.getC_belegartnr().equals(
								LocaleFac.BELEGART_RECHNUNG)) {

					Integer aufposIId = null;
					if (lagerbewegung.getC_belegartnr().equals(
							LocaleFac.BELEGART_RECHNUNG)) {
						RechnungPositionDto rechPos = getRechnungFac()
								.rechnungPositionFindByPrimaryKeyOhneExc(
										lagerbewegung.getI_belegartpositionid());
						if (rechPos != null) {
							aufposIId = rechPos.getAuftragpositionIId();
						}
					} else if (lagerbewegung.getC_belegartnr().equals(
							LocaleFac.BELEGART_LIEFERSCHEIN)) {
						LieferscheinpositionDto lsPos = getLieferscheinpositionFac()
								.lieferscheinpositionFindByPrimaryKeyOhneExcUndOhneSnrChnrList(
										lagerbewegung.getI_belegartpositionid());
						if (lsPos != null) {
							aufposIId = lsPos.getAuftragpositionIId();
						}
					}

					if (aufposIId != null) {
						AuftragpositionDto aufposDto = getAuftragpositionFac()
								.auftragpositionFindByPrimaryKey(aufposIId);
						AuftragDto aDto = getAuftragFac()
								.auftragFindByPrimaryKey(
										aufposDto.getBelegIId());
						zeile[REPORT_ARTIKELSTATISTIK_AUFTRAG_AUSLOESER] = aDto
								.getCNr();
					}

				}

				zeile[REPORT_ARTIKELSTATISTIK_LAGER] = lagerbewegung
						.getFlrlager().getC_nr();
				zeile[REPORT_ARTIKELSTATISTIK_SNRCHNR] = lagerbewegung
						.getC_seriennrchargennr();
				zeile[REPORT_ARTIKELSTATISTIK_VERSION] = lagerbewegung
						.getC_version();
				zeile[REPORT_ARTIKELSTATISTIK_I_ID_BUCHUNG] = lagerbewegung
						.getI_id_buchung();

				BigDecimal preis = new BigDecimal(0);
				BigDecimal wert = new BigDecimal(0);
				if (lagerbewegung.getB_abgang().intValue() == 0) {
					if (darfEinkaufspreisSehen) {

						if (lagerbewegung.getC_belegartnr().equals(
								LocaleFac.BELEGART_GUTSCHRIFT)) {

							RechnungPositionDto rechposDto = getRechnungFac()
									.rechnungPositionFindByPrimaryKeyOhneExc(
											lagerbewegung
													.getI_belegartpositionid());

							if (rechposDto != null) {
								RechnungDto rechnungDto = getRechnungFac()
										.rechnungFindByPrimaryKey(
												rechposDto.getRechnungIId());

								preis = rechposDto
										.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte()
										.divide(rechnungDto.getNKurs(), 5,
												BigDecimal.ROUND_HALF_EVEN);
								zeile[REPORT_ARTIKELSTATISTIK_PREIS] = Helper
										.rundeKaufmaennisch(preis, 4);
							} else {
								preis = lagerbewegung.getN_einstandspreis();
								zeile[REPORT_ARTIKELSTATISTIK_PREIS] = Helper
										.rundeKaufmaennisch(preis, 4);
							}

						} else {
							preis = lagerbewegung.getN_einstandspreis();
							zeile[REPORT_ARTIKELSTATISTIK_PREIS] = Helper
									.rundeKaufmaennisch(preis, 4);
						}

					} else {
						zeile[REPORT_ARTIKELSTATISTIK_PREIS] = null;
					}
				} else {

					if (darfVerkaufspreisSehen) {
						preis = lagerbewegung.getN_verkaufspreis();

						if (lagerbewegung.getC_belegartnr().equals(
								LocaleFac.BELEGART_LOS)) {
							preis = getLagerFac()
									.getGemittelterGestehungspreisEinerAbgangsposition(
											LocaleFac.BELEGART_LOS,
											lagerbewegung
													.getI_belegartpositionid());
						}

						zeile[REPORT_ARTIKELSTATISTIK_PREIS] = Helper
								.rundeKaufmaennisch(preis, 4);
					} else {
						zeile[REPORT_ARTIKELSTATISTIK_PREIS] = null;
					}
				}

				if (bWeReferenzAndrucken) {

					zeile[REPORT_ARTIKELSTATISTIK_WE_REFERENZ] = getLagerFac()
							.getWareneingangsreferenzSubreport(
									lagerbewegung.getC_belegartnr(),
									lagerbewegung.getI_belegartpositionid(),
									lagerbewegung.getC_seriennrchargennr(),
									false, theClientDto);
				}

				wert = preis.multiply(lagerbewegung.getN_menge());

				BigDecimal d = lagerbewegung.getN_menge();
				// Wenn Lagerabgang, dann negative Menge
				if (Helper.short2boolean(lagerbewegung.getB_abgang())) {
					d = d.negate();
				}
				zeile[REPORT_ARTIKELSTATISTIK_MENGE] = d;

				if (lagerbewegung.getFlrhersteller() != null) {
					zeile[REPORT_ARTIKELSTATISTIK_HERSTELLER] = lagerbewegung
							.getFlrhersteller().getC_nr();
				}
				if (lagerbewegung.getFlrland() != null) {
					zeile[REPORT_ARTIKELSTATISTIK_URSPRUNGSLAND] = lagerbewegung
							.getFlrland().getC_lkz();
				}

				try {
					BelegInfos bi = getLagerFac().getBelegInfos(
							lagerbewegung.getC_belegartnr(),
							lagerbewegung.getI_belegartid(),
							lagerbewegung.getI_belegartpositionid(),
							theClientDto);
					zeile[REPORT_ARTIKELSTATISTIK_BELEGNUMMER] = bi
							.getBelegnummer();
					zeile[REPORT_ARTIKELSTATISTIK_FIRMA] = bi
							.getKundeLieferant();

					zeile[REPORT_ARTIKELSTATISTIK_VERLEIHFAKTOR] = bi
							.getVerleihfaktor();
					zeile[REPORT_ARTIKELSTATISTIK_VERLEIHTAGE] = bi
							.getVerleihtage();
					zeile[REPORT_ARTIKELSTATISTIK_BUCHUNGSZEIT] = lagerbewegung
							.getT_buchungszeit();
					if (lagerbewegung.getC_belegartnr().equals(
							LocaleFac.BELEGART_LIEFERSCHEIN)) {
						zeile[REPORT_ARTIKELSTATISTIK_LIEFERTERMIN] = bi
								.getBelegdatum();
					}
					if (bi.getBelegdatum() != null) {
						zeile[REPORT_ARTIKELSTATISTIK_BELEGDATUM] = bi
								.getBelegdatum();
					} else {
						zeile[REPORT_ARTIKELSTATISTIK_BELEGDATUM] = lagerbewegung
								.getT_belegdatum();
					}

					zeile[REPORT_ARTIKELSTATISTIK_MATERIALZUSCHLAG] = bi
							.getBdMaterialzuschlag();

					zeile[REPORT_ARTIKELSTATISTIK_SORTIERDATUM] = zeile[REPORT_ARTIKELSTATISTIK_BELEGDATUM];
					// Wenn Belegdatum und Buchungsdatum gleich, dann wird die
					// Buchungszeit + Datum als Belegdatum verwendet
					if (Helper
							.cutTimestamp(
									(Timestamp) zeile[REPORT_ARTIKELSTATISTIK_BELEGDATUM])
							.equals(Helper
									.cutTimestamp((Timestamp) zeile[REPORT_ARTIKELSTATISTIK_BUCHUNGSZEIT]))) {
						zeile[REPORT_ARTIKELSTATISTIK_SORTIERDATUM] = lagerbewegung
								.getT_buchungszeit();
					}

					// Fuer Monatsstatistik
					Calendar c = Calendar.getInstance();
					if (bi.getBelegdatum() != null) {
						c.setTimeInMillis(bi.getBelegdatum().getTime());
					} else {
						c.setTimeInMillis(lagerbewegung.getT_buchungszeit()
								.getTime());
					}
					c.set(Calendar.HOUR_OF_DAY, 0);
					c.set(Calendar.MINUTE, 0);
					c.set(Calendar.SECOND, 0);
					c.set(Calendar.MILLISECOND, 0);
					c.set(Calendar.DAY_OF_MONTH, 1);
					// Schon enthalten?

					al.add(zeile);
				} catch (RemoteException ex2) {
					throwEJBExceptionLPRespectOld(ex2);
				}

			}

		}

		//
		while (alInventurliste.size() > 0) {
			FLRInventurstand flr = (FLRInventurstand) alInventurliste.get(0);

			Object[] zeile = new Object[REPORT_ARTIKELSTATISTIK_ANZAHL_SPALTEN];

			zeile[REPORT_ARTIKELSTATISTIK_BELEGART] = "Inventurstand";
			zeile[REPORT_ARTIKELSTATISTIK_BEWEGUNGSVORSCHAU] = new Boolean(
					false);
			java.sql.Timestamp ts = new java.sql.Timestamp(flr.getFlrinventur()
					.getT_inventurdatum().getTime());

			zeile[REPORT_ARTIKELSTATISTIK_SORTIERDATUM] = ts;
			zeile[REPORT_ARTIKELSTATISTIK_BELEGDATUM] = ts;
			zeile[REPORT_ARTIKELSTATISTIK_BUCHUNGSZEIT] = ts;

			zeile[REPORT_ARTIKELSTATISTIK_INVENTURMENGE] = flr
					.getN_inventurmenge();
			zeile[REPORT_ARTIKELSTATISTIK_BELEGNUMMER] = flr.getFlrinventur()
					.getC_bez();
			zeile[REPORT_ARTIKELSTATISTIK_LAGER] = flr.getFlrlager().getC_nr();

			al.add(zeile);
			alInventurliste.remove(0);

		}

		sessionInv.close();

		session.close();

		ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKey(artikelIId,
				theClientDto);
		parameter.put("P_ARTIKEL", aDto.formatArtikelbezeichnung());

		// SP3003
		parameter.put("P_CHARGENEIGENSCHAFTEN_ANZEIGEN", Boolean.FALSE);

		if (aDto.istArtikelSnrOderchargentragend()) {
			PanelbeschreibungDto[] panelDtos = getPanelFac()
					.panelbeschreibungFindByPanelCNrMandantCNr(
							PanelFac.PANEL_CHARGENEIGENSCHAFTEN,
							theClientDto.getMandant(), aDto.getArtgruIId());
			if (panelDtos != null && panelDtos.length > 0) {
				parameter.put("P_CHARGENEIGENSCHAFTEN_ANZEIGEN", Boolean.TRUE);
			}
		}

		parameter.put("P_ARTIKELEINHEIT", aDto.getEinheitCNr().trim());
		parameter.put("P_BESTELLEINHEIT", aDto.getEinheitCNrBestellung());
		parameter.put("P_MULTIPLIKATORBESTELLMENGE",
				aDto.getNUmrechnungsfaktor());
		parameter.put("P_BESTELLEINHEIT_INVERS",
				Helper.short2Boolean(aDto.getbBestellmengeneinheitInvers()));
		if (bMonatsstatistik == true) {
			sAktuellerReport = ArtikelReportFac.REPORT_MONATSSTATISTIK;
			for (int i = 0; i < al.size() - 1; i = i + 1) {
				for (int j = al.size() - 1; j > i; j = j - 1) {
					Object[] erstes = (Object[]) al.get(j - 1);
					Object[] zweites = (Object[]) al.get(j);

					if (((java.sql.Timestamp) erstes[REPORT_ARTIKELSTATISTIK_BELEGDATUM])
							.before(((java.sql.Timestamp) zweites[REPORT_ARTIKELSTATISTIK_BELEGDATUM]))) {
						Object[] temp = erstes;
						al.set(j - 1, zweites);
						al.set(j, temp);
					}
				}
			}
			// sortiere nach Jahr/Monat
			java.text.DateFormatSymbols symbols = new java.text.DateFormatSymbols(
					theClientDto.getLocUi());
			String[] defaultMonths = symbols.getMonths();

			GregorianCalendar cAktuell = new GregorianCalendar();
			if (al.size() > 0) {
				Object[] erste = (Object[]) al.get(al.size() - 1);
				Object[] letzte = (Object[]) al.get(0);

				cAktuell.setTimeInMillis(((Timestamp) letzte[REPORT_ARTIKELSTATISTIK_BELEGDATUM])
						.getTime());
				ArrayList alMonate = new ArrayList();
				while (cAktuell.getTimeInMillis() >= ((Timestamp) erste[REPORT_ARTIKELSTATISTIK_BELEGDATUM])
						.getTime()) {
					BigDecimal mengeZugang = new BigDecimal(0);
					BigDecimal wertZugang = new BigDecimal(0);
					BigDecimal mengeAbgang = new BigDecimal(0);
					BigDecimal wertAbgang = new BigDecimal(0);
					for (int i = 0; i < al.size(); i++) {
						Object[] zeile = (Object[]) al.get(i);
						Timestamp d = (Timestamp) zeile[REPORT_ARTIKELSTATISTIK_BELEGDATUM];

						Calendar cZeile = Calendar.getInstance();
						cZeile.setTimeInMillis(d.getTime());

						if (cAktuell.get(Calendar.MONTH) == cZeile
								.get(Calendar.MONTH)
								&& cAktuell.get(Calendar.YEAR) == cZeile
										.get(Calendar.YEAR)) {
							BigDecimal mengeZeile = (BigDecimal) zeile[REPORT_ARTIKELSTATISTIK_MENGE];
							BigDecimal preisZeile = (BigDecimal) zeile[REPORT_ARTIKELSTATISTIK_PREIS];
							if (mengeZeile != null && preisZeile != null) {

								boolean bGutschrift = false;

								if (zeile[REPORT_ARTIKELSTATISTIK_BELEGART] != null
										&& zeile[REPORT_ARTIKELSTATISTIK_BELEGART]
												.equals(LocaleFac.BELEGART_GUTSCHRIFT)) {
									bGutschrift = true;
								}

								// SP18027

								if (mengeZeile.doubleValue() > 0) {
									if (bGutschrift == true) {
										mengeAbgang = mengeAbgang
												.subtract(mengeZeile.abs());
										wertAbgang = wertAbgang
												.subtract(mengeZeile.abs()
														.multiply(preisZeile));
									} else {
										mengeZugang = mengeZugang
												.add(mengeZeile);
										wertZugang = wertZugang.add(mengeZeile
												.multiply(preisZeile));
									}
								} else {
									mengeAbgang = mengeAbgang.add(mengeZeile
											.abs());
									wertAbgang = wertAbgang.add(mengeZeile
											.abs().multiply(preisZeile));
								}

							}
						}

					}

					Object[] zeileMonate = new Object[KundeReportFac.REPORT_MONATSSTATISTIK_ANZAHL_FELDER];
					zeileMonate[REPORT_MONATSSTATISTIK_MONAT] = defaultMonths[cAktuell
							.get(Calendar.MONTH)];
					zeileMonate[REPORT_MONATSSTATISTIK_JAHR] = cAktuell
							.get(Calendar.YEAR);
					zeileMonate[REPORT_MONATSSTATISTIK_ABGANG_MENGE] = mengeAbgang;
					zeileMonate[REPORT_MONATSSTATISTIK_ABGANG_WERT] = wertAbgang;
					zeileMonate[REPORT_MONATSSTATISTIK_ZUGANG_MENGE] = mengeZugang;
					zeileMonate[REPORT_MONATSSTATISTIK_ZUGANG_WERT] = wertZugang;
					alMonate.add(zeileMonate);

					cAktuell.set(Calendar.DAY_OF_MONTH, 1);
					cAktuell.getTimeInMillis();
					cAktuell.set(Calendar.MONTH,
							cAktuell.get(Calendar.MONTH) - 1);
					cAktuell.getTimeInMillis();
					cAktuell.set(Calendar.DAY_OF_MONTH,
							cAktuell.getActualMaximum(Calendar.DAY_OF_MONTH));
					cAktuell.getTimeInMillis();

				}

				Object[][] dataTemp = new Object[1][1];
				data = (Object[][]) alMonate.toArray(dataTemp);

				initJRDS(parameter, ArtikelFac.REPORT_MODUL,
						ArtikelReportFac.REPORT_MONATSSTATISTIK,
						theClientDto.getMandant(), theClientDto.getLocUi(),
						theClientDto);
				return getReportPrint();
			} else {
				return null;
			}

		} else {

			if (bMitBewegungsvorschau == true) {
				// SP 1008 OHNE Internebestellung
				ArrayList<?> list = getInternebestellungFac()
						.getBewegungsvorschauSortiert(artikelIId, false,
								theClientDto);

				BewegungsvorschauDto[] returnArray = new BewegungsvorschauDto[list
						.size()];
				BewegungsvorschauDto[] dtos = (com.lp.server.bestellung.service.BewegungsvorschauDto[]) list
						.toArray(returnArray);

				for (int i = 0; i < dtos.length; i++) {
					BewegungsvorschauDto dto = dtos[i];

					if (dto.getTLiefertermin() != null) {

						Object[] zeile = new Object[REPORT_ARTIKELSTATISTIK_ANZAHL_SPALTEN];
						zeile[REPORT_ARTIKELSTATISTIK_BEWEGUNGSVORSCHAU] = new Boolean(
								true);

						if (dto.getCBelegartCNr() != null
								&& dto.getCBelegartCNr().equals(
										LocaleFac.BELEGART_LOSABLIEFERUNG)) {
							LosDto losDto = getFertigungFac()
									.losFindByPrimaryKey(dto.getIBelegIId());

							zeile[REPORT_ARTIKELSTATISTIK_LOS_STATUS] = losDto
									.getStatusCNr();
							zeile[REPORT_ARTIKELSTATISTIK_LOS_BEGINN] = losDto
									.getTProduktionsbeginn();
							zeile[REPORT_ARTIKELSTATISTIK_LOS_ENDE] = losDto
									.getTProduktionsende();
							zeile[REPORT_ARTIKELSTATISTIK_LOS_PROJEKT] = losDto
									.getCProjekt();

							zeile[REPORT_ARTIKELSTATISTIK_LOS_ABGELIFERT] = getFertigungFac()
									.getErledigteMenge(losDto.getIId(),
											theClientDto);

							if (losDto.getAuftragIId() != null) {
								zeile[REPORT_ARTIKELSTATISTIK_LOS_AUFTRAG] = getAuftragFac()
										.auftragFindByPrimaryKey(
												losDto.getAuftragIId())
										.getCNr();
							}

						}

						zeile[REPORT_ARTIKELSTATISTIK_BELEGART] = dto
								.getCBelegartCNr();
						zeile[REPORT_ARTIKELSTATISTIK_BELEGNUMMER] = dto
								.getCBelegnummer();

						if (dto.getPartnerDto() != null) {
							zeile[REPORT_ARTIKELSTATISTIK_FIRMA] = dto
									.getPartnerDto().formatFixTitelName1Name2();
						} else {
							zeile[REPORT_ARTIKELSTATISTIK_FIRMA] = dto
									.getCProjekt();
						}

						// PJ17836
						zeile[REPORT_ARTIKELSTATISTIK_SORTIERDATUM] = dto
								.getTLiefertermin();
						if (dto.getTABTerminBestellung() != null) {
							zeile[REPORT_ARTIKELSTATISTIK_SORTIERDATUM] = new Timestamp(
									dto.getTABTerminBestellung().getTime());
						}

						zeile[REPORT_ARTIKELSTATISTIK_BELEGDATUM] = dto
								.getTLiefertermin();
						zeile[REPORT_ARTIKELSTATISTIK_BUCHUNGSZEIT] = dto
								.getTLiefertermin();
						zeile[REPORT_ARTIKELSTATISTIK_LIEFERTERMIN] = dto
								.getTLiefertermin();
						zeile[REPORT_ARTIKELSTATISTIK_MENGE] = dto.getNMenge();

						al.add(zeile);
					}

				}

				// PJ17817

				ReportAnfragestatistikKriterienDto kritDtoI = new ReportAnfragestatistikKriterienDto();
				kritDtoI.setArtikelIId(artikelIId);
				ReportRahmenreservierungDto[] aReportRahmenreservierungDto = getReportRahmenreservierung(
						kritDtoI, theClientDto);

				for (int i = 0; i < aReportRahmenreservierungDto.length; i++) {
					ReportRahmenreservierungDto reportRahmenreservierungDto = (ReportRahmenreservierungDto) aReportRahmenreservierungDto[i];
					Object[] zeile = new Object[REPORT_ARTIKELSTATISTIK_ANZAHL_SPALTEN];
					zeile[REPORT_ARTIKELSTATISTIK_BEWEGUNGSVORSCHAU] = new Boolean(
							true);
					zeile[REPORT_ARTIKELSTATISTIK_BELEGART] = "Rahmenreservierung";

					zeile[REPORT_ARTIKELSTATISTIK_BELEGNUMMER] = reportRahmenreservierungDto
							.getAuftragCNr();
					zeile[REPORT_ARTIKELSTATISTIK_FIRMA] = reportRahmenreservierungDto
							.getCKundenname();

					zeile[REPORT_ARTIKELSTATISTIK_SORTIERDATUM] = reportRahmenreservierungDto
							.getTUebersteuerterLiefertermin();

					zeile[REPORT_ARTIKELSTATISTIK_LIEFERTERMIN] = reportRahmenreservierungDto
							.getTUebersteuerterLiefertermin();
					zeile[REPORT_ARTIKELSTATISTIK_BELEGDATUM] = reportRahmenreservierungDto
							.getTUebersteuerterLiefertermin();
					zeile[REPORT_ARTIKELSTATISTIK_BUCHUNGSZEIT] = reportRahmenreservierungDto
							.getTUebersteuerterLiefertermin();
					zeile[REPORT_ARTIKELSTATISTIK_MENGE] = reportRahmenreservierungDto
							.getNOffeneMenge().negate();

					al.add(zeile);
				}

				RahmenbestelltReportDto[] rahmenbestelltDtos = getReportRahmenbestelltDto(
						artikelIId, theClientDto);

				for (int i = 0; i < rahmenbestelltDtos.length; i++) {
					RahmenbestelltReportDto dto = rahmenbestelltDtos[i];
					Object[] zeile = new Object[REPORT_ARTIKELSTATISTIK_ANZAHL_SPALTEN];
					zeile[REPORT_ARTIKELSTATISTIK_BEWEGUNGSVORSCHAU] = new Boolean(
							true);
					zeile[REPORT_ARTIKELSTATISTIK_BELEGART] = "Rahmenbestellt";
					zeile[REPORT_ARTIKELSTATISTIK_BELEGNUMMER] = dto
							.getBestellnummer();
					zeile[REPORT_ARTIKELSTATISTIK_FIRMA] = dto.getLieferant();

					zeile[REPORT_ARTIKELSTATISTIK_LIEFERTERMIN] = dto
							.getTLiefertermin();
					zeile[REPORT_ARTIKELSTATISTIK_BELEGDATUM] = dto
							.getTLiefertermin();
					zeile[REPORT_ARTIKELSTATISTIK_BUCHUNGSZEIT] = dto
							.getTLiefertermin();

					zeile[REPORT_ARTIKELSTATISTIK_SORTIERDATUM] = dto
							.getTLiefertermin();

					zeile[REPORT_ARTIKELSTATISTIK_MENGE] = dto.getOffenmenge();
					// Nettoeinzelpreis
					zeile[REPORT_ARTIKELSTATISTIK_PREIS] = dto.getPreis();
					al.add(zeile);
				}

			}

			Timestamp tHeute = new Timestamp(System.currentTimeMillis());
			Timestamp tMorgen = new Timestamp(System.currentTimeMillis()
					+ (24 * 3600000));

			for (int i = 0; i < al.size(); i++) {
				// Bei Bewegungsvorschau-Daten kann der Liefertermin fruehestens
				// morgen sein
				Object[] zeile = (Object[]) al.get(i);

				if (((Boolean) zeile[REPORT_ARTIKELSTATISTIK_BEWEGUNGSVORSCHAU]) == true) {
					Timestamp t = (Timestamp) zeile[REPORT_ARTIKELSTATISTIK_SORTIERDATUM];
					if (t.before(tHeute)) {
						zeile[REPORT_ARTIKELSTATISTIK_SORTIERDATUM] = tMorgen;
						al.set(i, zeile);
					}
				}

			}

			// PJ17836 immer nach diesem Sortierdatum sortieren
			for (int i = 0; i < al.size() - 1; i = i + 1) {
				for (int j = al.size() - 1; j > i; j = j - 1) {
					Object[] erstes = (Object[]) al.get(j - 1);
					Object[] zweites = (Object[]) al.get(j);

					if (((java.sql.Timestamp) erstes[REPORT_ARTIKELSTATISTIK_SORTIERDATUM])
							.before(((java.sql.Timestamp) zweites[REPORT_ARTIKELSTATISTIK_SORTIERDATUM]))) {
						Object[] temp = erstes;
						al.set(j - 1, zweites);
						al.set(j, temp);
					}
				}
			}
			index = -1;
			sAktuellerReport = ArtikelReportFac.REPORT_ARTIKELSTATISTIK;
			Object[][] returnArray = new Object[al.size()][16];
			data = (Object[][]) al.toArray(returnArray);

			initJRDS(parameter, ArtikelFac.REPORT_MODUL,
					ArtikelReportFac.REPORT_ARTIKELSTATISTIK,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);
			return getReportPrint();

		}
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printVkPreisentwicklung(Integer artikelIId,
			TheClientDto theClientDto) {
		if (artikelIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelIId == null"));
		}

		// Erstellung des Reports
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		index = -1;
		sAktuellerReport = ArtikelReportFac.REPORT_VKPREISENTWICKLUNG;

		ArrayList alDaten = new ArrayList();

		try {

			VkPreisfindungEinzelverkaufspreisDto[] vkPreisfindungEinzelverkaufspreisDtos = getVkPreisfindungFac()
					.vkpfartikelverkaufspreisbasisFindByArtikelIId(artikelIId,
							theClientDto);

			// Preisbasis
			for (int i = 0; i < vkPreisfindungEinzelverkaufspreisDtos.length; i++) {
				Object[] zeile = new Object[REPORT_VKPREISENTWICKLUNG_ANZAHL_SPALTEN];
				zeile[REPORT_VKPREISENTWICKLUNG_GUELTIGAB] = vkPreisfindungEinzelverkaufspreisDtos[i]
						.getTVerkaufspreisbasisgueltigab();
				zeile[REPORT_VKPREISENTWICKLUNG_ART] = "Preisbasis";
				zeile[REPORT_VKPREISENTWICKLUNG_T_AENDERN] = vkPreisfindungEinzelverkaufspreisDtos[i]
						.getTAendern();
				zeile[REPORT_VKPREISENTWICKLUNG_VKPREISBASIS] = vkPreisfindungEinzelverkaufspreisDtos[i]
						.getNVerkaufspreisbasis();
				zeile[REPORT_VKPREISENTWICKLUNG_MANDANT] = vkPreisfindungEinzelverkaufspreisDtos[i]
						.getMandantCNr();

				PersonalDto personalDto;

				personalDto = getPersonalFac()
						.personalFindByPrimaryKey(
								vkPreisfindungEinzelverkaufspreisDtos[i]
										.getPersonalIIdAendern(),
								theClientDto);
				zeile[REPORT_VKPREISENTWICKLUNG_PERSON_GEAENDERT] = personalDto
						.formatAnrede();

				alDaten.add(zeile);

			}

			// Einzelpreis
			VkPreisfindungPreislisteDto[] preislisten = getVkPreisfindungFac()
					.vkPreisfindungPreislisteFindByArtikelIId(artikelIId);
			for (int i = 0; i < preislisten.length; i++) {
				Object[] zeile = new Object[REPORT_VKPREISENTWICKLUNG_ANZAHL_SPALTEN];

				zeile[REPORT_VKPREISENTWICKLUNG_GUELTIGAB] = preislisten[i]
						.getTPreisgueltigab();
				zeile[REPORT_VKPREISENTWICKLUNG_ART] = "Einzelpreis";
				zeile[REPORT_VKPREISENTWICKLUNG_T_AENDERN] = preislisten[i]
						.getTAendern();
				zeile[REPORT_VKPREISENTWICKLUNG_FIXPREIS] = preislisten[i]
						.getNArtikelfixpreis();

				if (preislisten[i].getNArtikelstandardrabattsatz() != null) {
					zeile[REPORT_VKPREISENTWICKLUNG_RABATT] = new Double(
							preislisten[i].getNArtikelstandardrabattsatz()
									.doubleValue());

				}

				VkpfartikelpreislisteDto preisliste = getVkPreisfindungFac()
						.vkpfartikelpreislisteFindByPrimaryKey(
								preislisten[i].getVkpfartikelpreislisteIId());

				zeile[REPORT_VKPREISENTWICKLUNG_PREISLISTENNAME] = preisliste
						.getCNr();
				zeile[REPORT_VKPREISENTWICKLUNG_MANDANT] = preisliste
						.getMandantCNr();

				PersonalDto personalDto;

				personalDto = getPersonalFac().personalFindByPrimaryKey(
						preislisten[i].getPersonalIIdAendern(), theClientDto);
				zeile[REPORT_VKPREISENTWICKLUNG_PERSON_GEAENDERT] = personalDto
						.formatAnrede();

				alDaten.add(zeile);
			}

			// Mengenstaffeln

			VkpfMengenstaffelDto[] staffeln = getVkPreisfindungFac()
					.vkpfMengenstaffelFindByArtikelIIdFuerVKPreisentwicklung(
							artikelIId, theClientDto);

			for (int i = 0; i < staffeln.length; i++) {
				Object[] zeile = new Object[REPORT_VKPREISENTWICKLUNG_ANZAHL_SPALTEN];
				zeile[REPORT_VKPREISENTWICKLUNG_GUELTIGAB] = staffeln[i]
						.getTPreisgueltigab();
				zeile[REPORT_VKPREISENTWICKLUNG_GUELTIGBIS] = staffeln[i]
						.getTPreisgueltigbis();
				zeile[REPORT_VKPREISENTWICKLUNG_ART] = "Mengenstaffel";
				zeile[REPORT_VKPREISENTWICKLUNG_T_AENDERN] = staffeln[i]
						.getTAendern();
				zeile[REPORT_VKPREISENTWICKLUNG_FIXPREIS] = staffeln[i]
						.getNArtikelfixpreis();
				zeile[REPORT_VKPREISENTWICKLUNG_RABATT] = staffeln[i]
						.getFArtikelstandardrabattsatz();
				zeile[REPORT_VKPREISENTWICKLUNG_STAFFELMENGE] = staffeln[i]
						.getNMenge();

				PersonalDto personalDto;

				personalDto = getPersonalFac().personalFindByPrimaryKey(
						staffeln[i].getPersonalIIdAendern(), theClientDto);
				zeile[REPORT_VKPREISENTWICKLUNG_PERSON_GEAENDERT] = personalDto
						.formatAnrede();

				if (staffeln[i].getVkpfartikelpreislisteIId() != null) {
					VkpfartikelpreislisteDto preisliste = getVkPreisfindungFac()
							.vkpfartikelpreislisteFindByPrimaryKey(
									staffeln[i].getVkpfartikelpreislisteIId());

					zeile[REPORT_VKPREISENTWICKLUNG_PREISLISTENNAME] = preisliste
							.getCNr();
					zeile[REPORT_VKPREISENTWICKLUNG_MANDANT] = preisliste
							.getMandantCNr();
				}

				alDaten.add(zeile);

			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		ArtikelDto dto = getArtikelFac().artikelFindByPrimaryKey(artikelIId,
				theClientDto);
		parameter.put("P_ARTIKEL", dto.formatArtikelbezeichnung());

		data = new Object[alDaten.size()][10];
		data = (Object[][]) alDaten.toArray(data);

		initJRDS(parameter, ArtikelReportFac.REPORT_MODUL,
				ArtikelReportFac.REPORT_VKPREISENTWICKLUNG,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();
	}

	private LinkedHashMap getAllergeneEinesArtikels(Integer artikelIId,
			LinkedHashMap hmDaten, TheClientDto theClientDto) {

		ArtikelalergenDto[] artikelalergeneDtos = getArtikelFac()
				.artikelallergenFindByArtikelIId(artikelIId);

		for (int i = 0; i < artikelalergeneDtos.length; i++) {
			artikelalergeneDtos[i].getAlergenIId();

			hmDaten.put(artikelalergeneDtos[i].getAlergenIId(), "");

		}

		StuecklisteDto stklDto = getStuecklisteFac()
				.stuecklisteFindByArtikelIIdMandantCNrOhneExc(artikelIId,
						theClientDto.getMandant());

		if (stklDto != null) {

			StuecklistepositionDto[] stkposDto = getStuecklisteFac()
					.stuecklistepositionFindByStuecklisteIId(stklDto.getIId(),
							theClientDto);

			for (int i = 0; i < stkposDto.length; i++) {
				hmDaten = getAllergeneEinesArtikels(
						stkposDto[i].getArtikelIId(), hmDaten, theClientDto);

			}

		}

		return hmDaten;

	};

	public LPDatenSubreport getSubreportAllergene(Integer artikelIId,
			TheClientDto theClientDto) {
		String[] fieldnames = new String[] { "Allergen", "Enthalten" };

		TreeMap tmAlergeneKomplett = new TreeMap();

		LinkedHashMap hmDaten = new LinkedHashMap();

		AlergenDto[] alergene = getArtikelFac().allergenFindByMandantCNr(
				theClientDto);

		if (alergene.length > 0) {
			for (int i = 0; i < alergene.length; i++) {
				tmAlergeneKomplett.put(alergene[i].getISort(), alergene[i]);
			}
			hmDaten = getAllergeneEinesArtikels(artikelIId, hmDaten,
					theClientDto);

		}

		//

		ArrayList alDaten = new ArrayList();

		Iterator it = tmAlergeneKomplett.keySet().iterator();

		while (it.hasNext()) {

			AlergenDto aDto = (AlergenDto) tmAlergeneKomplett.get(it.next());

			Object[] oZeile = new Object[2];
			oZeile[0] = aDto.getCBez();
			oZeile[1] = new Boolean(hmDaten.containsKey(aDto.getIId()));

			alDaten.add(oZeile);
		}

		Object[][] dataSubKD = new Object[alDaten.size()][2];
		dataSubKD = (Object[][]) alDaten.toArray(dataSubKD);
		return new LPDatenSubreport(dataSubKD, fieldnames);
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printVkPreisliste(Integer preislisteIId,
			Integer artikelgruppeIId, Integer artikelklasseIId,
			Integer shopgruppeIId, boolean bMitInaktiven, String artikelNrVon,
			String artikelNrBis, boolean bMitVersteckten,
			java.sql.Date datGueltikeitsdatumI, TheClientDto theClientDto) {
		// Erstellung des Reports
		index = -1;
		sAktuellerReport = ArtikelReportFac.REPORT_VKPREISLISTE;

		Integer mwstsatzId_0 = null;

		try {
			Map<?, ?> m = getMandantFac().mwstsatzFindAll(theClientDto);
			Iterator<?> it = m.keySet().iterator();
			while (it.hasNext()) {
				MwstsatzDto dto = (MwstsatzDto) m.get(it.next());
				if (dto.getFMwstsatz() == 0) {
					mwstsatzId_0 = dto.getIId();
				}
			}

		} catch (RemoteException ex4) {
			throwEJBExceptionLPRespectOld(ex4);
		}

		String eingeloggterMandant = theClientDto.getMandant();
		String sQuery = "";
		if (preislisteIId == null) {
			sQuery = "SELECT artikelliste.i_id, artikelliste.c_nr, aspr.c_bez, aspr.c_kbez, aspr.c_zbez,aspr.c_zbez2,artikelliste.einheit_c_nr, gruppe.c_nr, klasse.c_nr, artikelliste.b_versteckt, shopgruppe.c_nr "
					+ " FROM FLRArtikelliste AS artikelliste"
					+ " LEFT OUTER JOIN artikelliste.flrartikelgruppe AS gruppe "
					+ " LEFT OUTER JOIN artikelliste.flrartikelklasse AS klasse "
					+ " LEFT OUTER JOIN artikelliste.flrshopgruppe AS shopgruppe "
					+ " LEFT OUTER JOIN artikelliste.artikelsprset AS aspr WHERE artikelliste.mandant_c_nr='"
					+ eingeloggterMandant
					+ "' AND artikelliste.artikelart_c_nr NOT IN ('"
					+ ArtikelFac.ARTIKELART_HANDARTIKEL + "') ";

			if (artikelklasseIId != null) {
				sQuery += " AND klasse.i_id=" + artikelklasseIId.intValue();
			}
			if (artikelgruppeIId != null) {
				sQuery += " AND gruppe.i_id=" + artikelgruppeIId.intValue();
			}
			if (shopgruppeIId != null) {
				sQuery += " AND shopgruppe.i_id=" + shopgruppeIId.intValue();
			}
			if (bMitVersteckten == false) {
				sQuery += " AND artikelliste.b_versteckt=0 ";
			}
			if (artikelNrVon != null) {
				sQuery += " AND artikelliste.c_nr >='" + artikelNrVon + "'";
			}
			if (artikelNrBis != null) {
				sQuery = sQuery + " AND artikelliste.c_nr <='" + artikelNrBis
						+ "'";
			}

			sQuery += "ORDER BY artikelliste.c_nr";
		} else {
			sQuery = "SELECT artikelliste.i_id, artikelliste.c_nr, aspr.c_bez, aspr.c_kbez, aspr.c_zbez,aspr.c_zbez2,artikelliste.einheit_c_nr, gruppe.c_nr, klasse.c_nr, artikelliste.b_versteckt, shopgruppe.c_nr "
					+ " FROM FLRArtikelliste AS artikelliste"
					+ " LEFT OUTER JOIN artikelliste.flrartikelgruppe AS gruppe "
					+ " LEFT OUTER JOIN artikelliste.flrshopgruppe AS shopgruppe "
					+ " LEFT OUTER JOIN artikelliste.flrartikelklasse AS klasse "
					+ " LEFT OUTER JOIN artikelliste.artikelsprset AS aspr WHERE artikelliste.mandant_c_nr='"
					+ eingeloggterMandant
					+ "' AND artikelliste.artikelart_c_nr NOT IN ('"
					+ ArtikelFac.ARTIKELART_HANDARTIKEL + "')";// AND
																// artikelliste.i_id
																// IN (SELECT
																// liste.artikel_i_id
																// FROM
																// FLRVkpfartikelpreis
																// liste WHERE
																// liste.vkpfartikelpreisliste_i_id="
			// + preislisteIId + ") "; -> lt. WH muessen immer alle Artikel in
			// der Liste

			if (artikelklasseIId != null) {
				sQuery += " AND klasse.i_id=" + artikelklasseIId.intValue();
			}
			if (artikelgruppeIId != null) {
				sQuery += " AND gruppe.i_id=" + artikelgruppeIId.intValue();
			}
			if (shopgruppeIId != null) {
				sQuery += " AND shopgruppe.i_id=" + shopgruppeIId.intValue();
			}
			if (artikelNrVon != null) {
				sQuery += " AND artikelliste.c_nr >='" + artikelNrVon + "'";
			}
			if (bMitVersteckten == false) {
				sQuery += " AND artikelliste.b_versteckt=0 ";
			}
			if (artikelNrBis != null) {
				sQuery = sQuery + " AND artikelliste.c_nr <='" + artikelNrBis
						+ "'";
			}
			sQuery += "ORDER BY artikelliste.c_nr";
		}

		Session session = FLRSessionFactory.getFactory().openSession();
		Query inventurliste = session.createQuery(sQuery);

		List<?> resultList = inventurliste.list();

		Iterator<?> resultListIterator = resultList.iterator();

		data = new Object[resultList.size()][REPORT_VKPREISLISTE_ANZAHL_SPALTEN];

		int row = 0;

		while (resultListIterator.hasNext()) {
			Object o[] = (Object[]) resultListIterator.next();

			Integer artikel_i_id = (Integer) o[0];
			String artikelnummer = (String) o[1];
			String bezeichnung = (String) o[2];
			String kurzbezeichnung = (String) o[3];
			String zusatzbezeichnung = (String) o[4];
			String zusatzbezeichnung2 = (String) o[5];
			String einheit = (String) o[6];
			String gruppe = (String) o[7];
			String klasse = (String) o[8];
			Short versteckt = (Short) o[9];
			String shopgruppe = (String) o[10];

			data[row][REPORT_VKPREISLISTE_ARTIKELNUMMER] = artikelnummer;
			data[row][REPORT_VKPREISLISTE_BEZEICHNUNG] = bezeichnung;
			data[row][REPORT_VKPREISLISTE_KURZBEZEICHNUNG] = kurzbezeichnung;
			data[row][REPORT_VKPREISLISTE_ZUSATZBEZEICHNUNG] = zusatzbezeichnung;
			data[row][REPORT_VKPREISLISTE_ZUSATZBEZEICHNUNG2] = zusatzbezeichnung2;
			data[row][REPORT_VKPREISLISTE_EINHEIT] = einheit;
			data[row][REPORT_VKPREISLISTE_ARTIKELGRUPPE] = gruppe;
			data[row][REPORT_VKPREISLISTE_SHOPGRUPPE] = shopgruppe;
			data[row][REPORT_VKPREISLISTE_ARTIKELKLASSE] = klasse;
			data[row][REPORT_VKPREISLISTE_VERSTECKT] = Helper
					.short2Boolean(versteckt);
			data[row][REPORT_VKPREISLISTE_MATERIALZUSCHLAG] = getMaterialFac()
					.getMaterialzuschlagVKInZielwaehrung(artikel_i_id,
							Helper.cutDate(datGueltikeitsdatumI),
							theClientDto.getSMandantenwaehrung(), theClientDto);

			try {
				BigDecimal preis = new BigDecimal(0);
				if (preislisteIId != null) {
					ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKeySmall(artikel_i_id,
									theClientDto);

					// @@ToDo: AD ? ist hier eine Staffelmenge bei Preisfindung
					// aus EK moeglich?
					VkpreisfindungDto dto = getVkPreisfindungFac()
							.verkaufspreisfindungStufe1(
									artikelDto,
									Helper.cutDate(datGueltikeitsdatumI),
									preislisteIId,
									new VkpreisfindungDto(theClientDto
											.getLocUi()), mwstsatzId_0,
									new BigDecimal(1),
									theClientDto.getSMandantenwaehrung(),
									theClientDto);

					if (dto.getVkpStufe1() != null
							&& dto.getVkpStufe1().nettopreis != null) {
						preis = dto.getVkpStufe1().nettopreis;
					}
				} else {
					VkPreisfindungEinzelverkaufspreisDto vkpreisDto = getVkPreisfindungFac()
							.getArtikeleinzelverkaufspreis(artikel_i_id,
									datGueltikeitsdatumI,
									theClientDto.getSMandantenwaehrung(),
									theClientDto);
					if (vkpreisDto != null
							&& vkpreisDto.getNVerkaufspreisbasis() != null) {
						preis = vkpreisDto.getNVerkaufspreisbasis();
					}

				}

				data[row][REPORT_VKPREISLISTE_VKPREIS] = preis;

				java.sql.Date jetzt = new java.sql.Date(
						System.currentTimeMillis());
				jetzt = Helper.cutDate(jetzt);

				VkpfMengenstaffelDto[] vkpfMengenstaffelDtos = getVkPreisfindungFac()
						.vkpfMengenstaffelFindByArtikelIIdGueltigkeitsdatum(
								artikel_i_id, datGueltikeitsdatumI, null, theClientDto);

				if (vkpfMengenstaffelDtos.length > 0) {
					data[row][REPORT_VKPREISLISTE_STAFFEL1] = vkpfMengenstaffelDtos[0]
							.getNMenge();
					data[row][REPORT_VKPREISLISTE_PREISSTAFFEL1] = getVkPreisfindungFac()
							.berechneEinzelVkpreisEinerMengenstaffel(
									vkpfMengenstaffelDtos[0].getIId(),
									theClientDto.getSMandantenwaehrung(),
									theClientDto);
				}
				if (vkpfMengenstaffelDtos.length > 1) {
					data[row][REPORT_VKPREISLISTE_STAFFEL2] = vkpfMengenstaffelDtos[1]
							.getNMenge();
					data[row][REPORT_VKPREISLISTE_PREISSTAFFEL2] = getVkPreisfindungFac()
							.berechneEinzelVkpreisEinerMengenstaffel(
									vkpfMengenstaffelDtos[1].getIId(),
									theClientDto.getSMandantenwaehrung(),
									theClientDto);
				}
				if (vkpfMengenstaffelDtos.length > 2) {
					data[row][REPORT_VKPREISLISTE_STAFFEL3] = vkpfMengenstaffelDtos[2]
							.getNMenge();
					data[row][REPORT_VKPREISLISTE_PREISSTAFFEL3] = getVkPreisfindungFac()
							.berechneEinzelVkpreisEinerMengenstaffel(
									vkpfMengenstaffelDtos[2].getIId(),
									theClientDto.getSMandantenwaehrung(),
									theClientDto);
				}
				if (vkpfMengenstaffelDtos.length > 3) {
					data[row][REPORT_VKPREISLISTE_STAFFEL4] = vkpfMengenstaffelDtos[3]
							.getNMenge();
					data[row][REPORT_VKPREISLISTE_PREISSTAFFEL4] = getVkPreisfindungFac()
							.berechneEinzelVkpreisEinerMengenstaffel(
									vkpfMengenstaffelDtos[3].getIId(),
									theClientDto.getSMandantenwaehrung(),
									theClientDto);
				}
				if (vkpfMengenstaffelDtos.length > 4) {
					data[row][REPORT_VKPREISLISTE_STAFFEL5] = vkpfMengenstaffelDtos[4]
							.getNMenge();
					data[row][REPORT_VKPREISLISTE_PREISSTAFFEL5] = getVkPreisfindungFac()
							.berechneEinzelVkpreisEinerMengenstaffel(
									vkpfMengenstaffelDtos[4].getIId(),
									theClientDto.getSMandantenwaehrung(),
									theClientDto);
				}

			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}
			row++;
		}

		session.close();

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		try {
			if (preislisteIId != null) {

				VkpfartikelpreislisteDto vkpfartikelpreislisteDto = getVkPreisfindungFac()
						.vkpfartikelpreislisteFindByPrimaryKey(preislisteIId);
				parameter
						.put("P_PREISLISTE", vkpfartikelpreislisteDto.getCNr());

			}
			if (artikelgruppeIId != null) {
				parameter.put("P_ARTIKELGRUPPE", getArtikelFac()
						.artgruFindByPrimaryKey(artikelgruppeIId, theClientDto)
						.getCNr());
			}
			if (shopgruppeIId != null) {
				parameter.put(
						"P_SHOPGRUPPE",
						getArtikelFac().shopgruppeFindByPrimaryKey(
								shopgruppeIId, theClientDto).getCNr());
			}
			if (artikelklasseIId != null) {
				parameter.put("P_ARTIKELKLASSE", getArtikelFac()
						.artklaFindByPrimaryKey(artikelklasseIId, theClientDto)
						.getCNr());
			}

		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}

		parameter.put("P_ARTIKELNRVON", artikelNrVon);
		parameter.put("P_ARTIKELNRBIS", artikelNrBis);
		parameter.put("P_MITVERSTECKTEN", new Boolean(bMitVersteckten));

		parameter.put("P_PREISGUELTIGKEIT", datGueltikeitsdatumI);

		initJRDS(parameter, ArtikelReportFac.REPORT_MODUL,
				ArtikelReportFac.REPORT_VKPREISLISTE,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAllergene(String artikelNrVon,
			String artikelNrBis, TheClientDto theClientDto) {

		// Erstellung des Reports

		// Es werden nur Stuecklisten angezeigt

		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("P_ARTIKELNRVON", artikelNrVon);
		parameter.put("P_ARTIKELNRBIS", artikelNrBis);
		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT distinct aa.flrartikelliste.i_id,aa.flrartikelliste.c_nr, aspr.c_bez FROM FLRArtikelalergen as aa LEFT OUTER JOIN aa.flrartikelliste.artikelsprset AS aspr"
				+ " WHERE aa.flrartikelliste.mandant_c_nr='"
				+ theClientDto.getMandant() + "' ";

		String artikelNrBis_Gefuellt = null;
		if (artikelNrBis != null) {
			artikelNrBis_Gefuellt = Helper.fitString2Length(artikelNrBis, 25,
					'_');
		}

		TreeMap<String, ArtikelDto> tmBetroffeneArtikel = new TreeMap<String, ArtikelDto>();

		queryString += " GROUP BY aa.flrartikelliste.i_id,aa.flrartikelliste.c_nr, aspr.c_bez ORDER BY aa.flrartikelliste.c_nr ASC";

		session.enableFilter("filterLocale").setParameter("paramLocale",
				Helper.locale2String(theClientDto.getLocUi()));

		Query query = session.createQuery(queryString);

		List<?> results = query.list();

		Iterator<?> resultListIterator = results.iterator();

		ArrayList al = new ArrayList();

		while (resultListIterator.hasNext()) {
			Object[] o = (Object[]) resultListIterator.next();
			Integer artikelIId = (Integer) o[0];
			tmBetroffeneArtikel = betroffeneStuecklistenHinzufuegen(artikelIId,
					tmBetroffeneArtikel, theClientDto);

		}

		session.close();

		Iterator itBetroffeneArtikel = tmBetroffeneArtikel.keySet().iterator();
		while (itBetroffeneArtikel.hasNext()) {

			ArtikelDto aDto = tmBetroffeneArtikel.get(itBetroffeneArtikel
					.next());

			if (artikelNrVon != null) {
				if (aDto.getCNr().compareTo(artikelNrVon) < 0) {
					continue;
				}
			}
			if (artikelNrBis_Gefuellt != null) {
				if (aDto.getCNr().compareTo(artikelNrBis_Gefuellt) > 0) {
					continue;
				}
			}

			Object[] oZeile = new Object[REPORT_ALLERGENE_ANZAHL_SPALTEN];
			oZeile[REPORT_ALLERGENE_ARTIKELNUMMER] = aDto.getCNr();
			oZeile[REPORT_ALLERGENE_BEZEICHNUNG] = aDto.getArtikelsprDto()
					.getCBez();
			oZeile[REPORT_ALLERGENE_SUBREPORT_ENTHALTENE_ALLERGENE] = getSubreportAllergene(
					aDto.getIId(), theClientDto);

			al.add(oZeile);

		}

		index = -1;
		sAktuellerReport = ArtikelReportFac.REPORT_ALLERGENE;
		Object[][] returnArray = new Object[al.size()][REPORT_ALLERGENE_ANZAHL_SPALTEN];
		data = (Object[][]) al.toArray(returnArray);

		initJRDS(parameter, ArtikelFac.REPORT_MODUL,
				ArtikelReportFac.REPORT_ALLERGENE, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);
		return getReportPrint();

	}

	private TreeMap<String, ArtikelDto> betroffeneStuecklistenHinzufuegen(
			Integer artikelIId,
			TreeMap<String, ArtikelDto> tmBetroffeneArtikel,
			TheClientDto theClientDto) {
		// Abhaengige Stuecklisten hinzufuegen
		Session sessionStkl = FLRSessionFactory.getFactory().openSession();
		String queryStringStkl = "SELECT stklpos FROM FLRStuecklisteposition stklpos WHERE stklpos.flrartikel.i_id="
				+ artikelIId + "";
		Query queryStkl = sessionStkl.createQuery(queryStringStkl);
		List<?> resultsStkl = queryStkl.list();
		Iterator<?> resultListIteratorStkl = resultsStkl.iterator();
		while (resultListIteratorStkl.hasNext()) {
			FLRStuecklisteposition stklPos = (FLRStuecklisteposition) resultListIteratorStkl
					.next();

			ArtikelDto aDto = getArtikelFac()
					.artikelFindByPrimaryKeySmall(
							stklPos.getFlrstueckliste().getArtikel_i_id(),
							theClientDto);

			tmBetroffeneArtikel.put(aDto.getCNr(), aDto);
			betroffeneStuecklistenHinzufuegen(stklPos.getFlrstueckliste()
					.getArtikel_i_id(), tmBetroffeneArtikel, theClientDto);

		}

		sessionStkl.close();
		return tmBetroffeneArtikel;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printRahmenbestellungsliste(Integer artikelIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (artikelIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelIId == null"));
		}
		Session session = null;
		try {
			// Erstellung des Reports
			index = -1;
			sAktuellerReport = ArtikelReportFac.REPORT_ARTIKELBESTELLTRAHMEN;

			RahmenbestelltReportDto[] dtos = getReportRahmenbestelltDto(
					artikelIId, theClientDto);

			data = new Object[dtos.length][REPORT_RAHMENBEST_ANZAHL_SPALTEN];

			for (int i = 0; i < dtos.length; i++) {
				RahmenbestelltReportDto dto = dtos[i];
				data[i][REPORT_RAHMENBEST_BESTELLUNGSNUMMER] = dto
						.getBestellnummer();
				data[i][REPORT_RAHMENBEST_LIEFERANT] = dto.getLieferant();
				data[i][REPORT_RAHMENBEST_PROJEKT] = dto.getProjekt();

				data[i][REPORT_RAHMENBEST_LIEFERTERMIN] = dto
						.getTLiefertermin();

				// Rahmenmenge ist die Menge der Rahmenposition
				data[i][REPORT_RAHMENBEST_RAHMENMENGE] = dto.getRahmenmenge();

				data[i][REPORT_RAHMENBEST_OFFENE_MENGE] = dto.getOffenmenge();
				// Nettoeinzelpreis
				data[i][REPORT_RAHMENBEST_PREIS] = dto.getPreis();

				data[i][REPORT_RAHMENBEST_AB_NUMMER] = dto.getAbNummer();
				data[i][REPORT_RAHMENBEST_AB_TERMIN] = dto.gettAbTermin();
				data[i][REPORT_RAHMENBEST_AB_KOMMENTAR] = dto.getAbKommentar();

			}

			Map<String, Object> mapParameter = new TreeMap<String, Object>();
			ArtikelDto dto = null;
			dto = getArtikelFac().artikelFindByPrimaryKey(artikelIId,
					theClientDto);
			mapParameter.put("P_ARTIKEL", dto.formatArtikelbezeichnung());

			initJRDS(mapParameter, ArtikelReportFac.REPORT_MODUL,
					ArtikelReportFac.REPORT_ARTIKELBESTELLTRAHMEN,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return getReportPrint();
	}

	private RahmenbestelltReportDto[] getReportRahmenbestelltDto(
			Integer artikelIId, TheClientDto theClientDto)
			throws RemoteException {
		Session session;
		SessionFactory factory = FLRSessionFactory.getFactory();
		session = factory.openSession();
		Criteria crit = session.createCriteria(FLRBestellpositionReport.class);
		Criteria critBestellung = crit
				.createCriteria(BestellpositionFac.FLR_BESTELLPOSITION_FLRBESTELLUNG);

		Criteria critArtikel = crit
				.createCriteria(BestellpositionFac.FLR_BESTELLPOSITION_FLRARTIKEL);
		// nur diesen Artikel
		critArtikel.add(Restrictions.eq("i_id", artikelIId));
		// Filter nach Mandant
		critBestellung.add(Restrictions.eq(
				BestellungFac.FLR_BESTELLUNG_MANDANT_C_NR,
				theClientDto.getMandant()));
		// keine stornierten oder erledigten Bestellungen.
		Collection<String> cStati = new LinkedList<String>();
		cStati.add(BestellungFac.BESTELLSTATUS_STORNIERT);
		cStati.add(BestellungFac.BESTELLSTATUS_ERLEDIGT);
		critBestellung.add(Restrictions.not(Restrictions.in(
				BestellungFac.FLR_BESTELLUNG_BESTELLUNGSTATUS_C_NR, cStati)));
		// Nur Rahmenbestellungen
		critBestellung.add(Restrictions.eq(
				BestellungFac.FLR_BESTELLUNG_BESTELLUNGART_C_NR,
				BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR));
		// Query ausfuehren
		List<?> list = crit.list();

		Iterator<?> resultListIterator = list.iterator();
		int row = 0;

		RahmenbestelltReportDto[] dtos = new RahmenbestelltReportDto[list
				.size()];

		while (resultListIterator.hasNext()) {
			FLRBestellpositionReport besPos = (FLRBestellpositionReport) resultListIterator
					.next();

			RahmenbestelltReportDto dto = new RahmenbestelltReportDto();

			dto.setBestellnummer(besPos.getFlrbestellung().getC_nr());

			dto.setLieferant(besPos.getFlrbestellung().getFlrlieferant()
					.getFlrpartner().getC_name1nachnamefirmazeile1());

			dto.setProjekt(besPos.getFlrbestellung()
					.getC_bezprojektbezeichnung());

			if (besPos.getT_uebersteuerterliefertermin() != null) {
				dto.setTLiefertermin(besPos.getT_uebersteuerterliefertermin());

			} else {
				if (besPos.getFlrbestellung().getT_liefertermin() != null) {
					dto.setTLiefertermin(new Timestamp(besPos
							.getFlrbestellung().getT_liefertermin().getTime()));
				}
			}
			// Rahmenmenge ist die Menge der Rahmenposition
			dto.setRahmenmenge(besPos.getN_menge());

			BigDecimal bdOffen = besPos.getN_offenemenge();

			dto.setOffenmenge(bdOffen);
			// Nettoeinzelpreis

			dto.setPreis(besPos.getN_nettogesamtpreis());

			BestellpositionDto bestellpositionDto = getBestellpositionFac()
					.bestellpositionFindByPrimaryKey(besPos.getI_id());

			dto.setAbNummer(bestellpositionDto.getCABNummer());

			dto.settAbTermin(bestellpositionDto
					.getTAuftragsbestaetigungstermin());

			dto.setAbKommentar(bestellpositionDto.getCABKommentar());

			dtos[row] = dto;
			row++;
		}

		session.close();
		return dtos;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printLieferantenpreis(Integer artikelIId,
			TheClientDto theClientDto) {
		HashMap<String, Object> parameter = new HashMap<String, Object>();

		ArtikelDto dto = getArtikelFac().artikelFindByPrimaryKey(artikelIId,
				theClientDto);
		parameter.put("P_ARTIKEL", dto.formatArtikelbezeichnung());

		sAktuellerReport = ArtikelReportFac.REPORT_LIEFERANTENPREIS;

		String sQuery = "SELECT al "
				+ " FROM FLRArtikellieferant AS al WHERE al.artikel_i_id="
				+ artikelIId;
		Session session = FLRSessionFactory.getFactory().openSession();
		Query inventurliste = session.createQuery(sQuery);

		List<?> resultList = inventurliste.list();

		Iterator<?> resultListIterator = resultList.iterator();

		data = new Object[resultList.size()][6];

		ArrayList alDaten = new ArrayList();

		while (resultListIterator.hasNext()) {
			FLRArtikellieferant al = (FLRArtikellieferant) resultListIterator
					.next();
			Set s = al.getStaffelset();

			if (al.getN_einzelpreis() == null) {
				continue;
			}

			ArtikellieferantDto alDto = null;
			try {
				alDto = getArtikelFac().artikellieferantFindByPrimaryKey(
						al.getI_id(), theClientDto);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Object[] zeile = new Object[REPORT_LIEFERANTENPREIS_ANZAHL_SPALTEN];

			zeile[REPORT_LIEFERANTENPREIS_FIXKOSTEN] = alDto.getNFixkosten();
			zeile[REPORT_LIEFERANTENPREIS_GUELTIGAB] = alDto
					.getTPreisgueltigab();
			zeile[REPORT_LIEFERANTENPREIS_LIEFERANT] = alDto.getLieferantDto()
					.getPartnerDto().formatFixTitelName1Name2();
			zeile[REPORT_LIEFERANTENPREIS_MENGE] = new BigDecimal(1);
			zeile[REPORT_LIEFERANTENPREIS_MINDESTBESTELLMENGE] = alDto
					.getFMindestbestelmenge();
			zeile[REPORT_LIEFERANTENPREIS_NETTOPREIS] = alDto.getNNettopreis();
			zeile[REPORT_LIEFERANTENPREIS_WIEDERBESCHAFFUNGSZEIT] = alDto
					.getIWiederbeschaffungszeit();

			alDaten.add(zeile);
			Iterator it = s.iterator();

			while (it.hasNext()) {

				FLRArtikellieferantstaffel staffel = (FLRArtikellieferantstaffel) it
						.next();

				zeile = new Object[REPORT_LIEFERANTENPREIS_ANZAHL_SPALTEN];

				zeile[REPORT_LIEFERANTENPREIS_FIXKOSTEN] = alDto
						.getNFixkosten();
				zeile[REPORT_LIEFERANTENPREIS_GUELTIGAB] = new java.sql.Timestamp(
						staffel.getT_preisgueltigab().getTime());
				zeile[REPORT_LIEFERANTENPREIS_LIEFERANT] = alDto
						.getLieferantDto().getPartnerDto()
						.formatFixTitelName1Name2();
				zeile[REPORT_LIEFERANTENPREIS_MENGE] = staffel.getN_menge();
				zeile[REPORT_LIEFERANTENPREIS_MINDESTBESTELLMENGE] = alDto
						.getFMindestbestelmenge();
				zeile[REPORT_LIEFERANTENPREIS_NETTOPREIS] = staffel
						.getN_nettopreis();
				zeile[REPORT_LIEFERANTENPREIS_WIEDERBESCHAFFUNGSZEIT] = alDto
						.getIWiederbeschaffungszeit();
				alDaten.add(zeile);
			}
		}

		// Nach Menge + Nettopreis sortieren
		for (int i = alDaten.size() - 1; i > 0; --i) {
			for (int j = 0; j < i; ++j) {

				Object[] zeilea = (Object[]) alDaten.get(j);
				Object[] zeileb = (Object[]) alDaten.get(j + 1);

				BigDecimal zahla = (BigDecimal) zeilea[REPORT_LIEFERANTENPREIS_MENGE];
				BigDecimal zahlb = (BigDecimal) zeileb[REPORT_LIEFERANTENPREIS_MENGE];

				if (zahla.doubleValue() > zahlb.doubleValue()) {
					Object[] h = zeilea;
					alDaten.set(j, zeileb);
					alDaten.set(j + 1, h);
				} else if (zahla.doubleValue() == zahlb.doubleValue()) {

					BigDecimal preisa = (BigDecimal) zeilea[REPORT_LIEFERANTENPREIS_NETTOPREIS];
					BigDecimal preisb = (BigDecimal) zeileb[REPORT_LIEFERANTENPREIS_NETTOPREIS];
					if (preisa.doubleValue() > preisb.doubleValue()) {
						Object[] h = zeilea;
						alDaten.set(j, zeileb);
						alDaten.set(j + 1, h);
					}
				}
			}
		}

		Object[][] dataTemp = new Object[alDaten.size()][REPORT_LIEFERANTENPREIS_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(dataTemp);

		initJRDS(parameter, ArtikelReportFac.REPORT_MODUL,
				ArtikelReportFac.REPORT_LIEFERANTENPREIS,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printArtikeletikett(Integer artikelIId,
			String sKommentar, BigDecimal bdMenge, Integer iExemplare,
			String[] cSnrChnr, TheClientDto theClientDto) {
		// Erstellung des Reports
		ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(
				artikelIId, theClientDto);
		JasperPrintLP print = null;
		Integer varianteIId = theClientDto.getReportvarianteIId();
		int iAnzahlSnrs = 1;
		if (cSnrChnr != null && cSnrChnr.length > 0) {
			iAnzahlSnrs = cSnrChnr.length;
		}

		for (int k = 0; k < iAnzahlSnrs; k++) {

			HashMap<String, Object> parameter = new HashMap<String, Object>();
			index = -1;
			sAktuellerReport = ArtikelReportFac.REPORT_ARTIKELETIKETT;
			data = new Object[1][REPORT_ARTIKELETIKETT_ANZAHL_SPALTEN];

			try {

				MandantDto mandantDto = getMandantFac()
						.mandantFindByPrimaryKey(theClientDto.getMandant(),
								theClientDto);

				data[0][REPORT_ARTIKELETIKETT_ARTIKELNUMMER] = artikelDto
						.getCNr();
				data[0][REPORT_ARTIKELETIKETT_EINHEIT] = artikelDto
						.getEinheitCNr();
				data[0][REPORT_ARTIKELETIKETT_REFERENZNUMMER] = artikelDto
						.getCReferenznr();
				data[0][REPORT_ARTIKELETIKETT_REVISION] = artikelDto
						.getCRevision();
				data[0][REPORT_ARTIKELETIKETT_INDEX] = artikelDto.getCIndex();
				data[0][REPORT_ARTIKELETIKETT_VERPACKUNGSMENGE] = artikelDto
						.getFVerpackungsmenge();

				data[0][REPORT_ARTIKELETIKETT_MANDANTADRESSE] = Helper
						.formatMandantAdresse(mandantDto);

				if (artikelDto.getArtikelsprDto() != null) {
					data[0][REPORT_ARTIKELETIKETT_BEZEICHNUNG] = artikelDto
							.getArtikelsprDto().getCBez();
					data[0][REPORT_ARTIKELETIKETT_ZUSATZBEZEICHNUNG] = artikelDto
							.getArtikelsprDto().getCZbez();
					data[0][REPORT_ARTIKELETIKETT_ZUSATZBEZEICHNUNG2] = artikelDto
							.getArtikelsprDto().getCZbez2();
					data[0][REPORT_ARTIKELETIKETT_KURZBEZEICHNUNG] = artikelDto
							.getArtikelsprDto().getCKbez();
				}

				if (artikelDto.getMaterialIId() != null) {
					MaterialDto materialDto = getMaterialFac()
							.materialFindByPrimaryKey(
									artikelDto.getMaterialIId(), theClientDto);
					data[0][REPORT_ARTIKELETIKETT_MATERIAL] = materialDto
							.getBezeichnung();
				}

				if (artikelDto.getGeometrieDto() != null) {
					data[0][REPORT_ARTIKELETIKETT_BREITE] = artikelDto
							.getGeometrieDto().getFBreite();
					data[0][REPORT_ARTIKELETIKETT_HOEHE] = artikelDto
							.getGeometrieDto().getFHoehe();
					data[0][REPORT_ARTIKELETIKETT_TIEFE] = artikelDto
							.getGeometrieDto().getFTiefe();
				}

				if (artikelDto.getVerpackungDto() != null) {
					data[0][REPORT_ARTIKELETIKETT_BAUFORM] = artikelDto
							.getVerpackungDto().getCBauform();
					data[0][REPORT_ARTIKELETIKETT_VERPACKUNGSART] = artikelDto
							.getVerpackungDto().getCVerpackungsart();
				}

				data[0][REPORT_ARTIKELETIKETT_VERPACKUNGS_EAN] = artikelDto
						.getCVerpackungseannr();
				data[0][REPORT_ARTIKELETIKETT_VERKAUFS_EAN] = artikelDto
						.getCVerkaufseannr();

				if (artikelDto.getHerstellerIId() != null) {
					HerstellerDto herstellerDto = getArtikelFac()
							.herstellerFindByPrimaryKey(
									artikelDto.getHerstellerIId(), theClientDto);
					data[0][REPORT_ARTIKELETIKETT_HERSTELLER] = herstellerDto
							.getCNr();
					data[0][REPORT_ARTIKELETIKETT_HERSTELLER_NAME1] = herstellerDto
							.getPartnerDto().getCName1nachnamefirmazeile1();
					data[0][REPORT_ARTIKELETIKETT_HERSTELLER_NAME2] = herstellerDto
							.getPartnerDto().getCName2vornamefirmazeile2();
				}

				ArtikellieferantDto[] artikellieferantDtos = getArtikelFac()
						.artikellieferantFindByArtikelIId(artikelIId,
								theClientDto);

				if (artikellieferantDtos != null
						&& artikellieferantDtos.length > 0) {
					data[0][REPORT_ARTIKELETIKETT_LIEFERANT] = getLieferantFac()
							.lieferantFindByPrimaryKey(
									artikellieferantDtos[0].getLieferantIId(),
									theClientDto).getPartnerDto()
							.formatAnrede();
					data[0][REPORT_ARTIKELETIKETT_LIEFERANT_ARTIKELNUMMER] = artikellieferantDtos[0]
							.getCArtikelnrlieferant();
					data[0][REPORT_ARTIKELETIKETT_LIEFERANT_ARTIKELBEZEICHNUNG] = artikellieferantDtos[0]
							.getCBezbeilieferant();
				}
				Integer iIdHauplpager = getLagerFac()
						.getHauptlagerDesMandanten(theClientDto).getIId();
				if (Helper.short2boolean(artikelDto.getBLagerbewirtschaftet())) {
					data[0][REPORT_ARTIKELETIKETT_LAGERSTAND] = getLagerFac()
							.getLagerstand(artikelIId, iIdHauplpager,
									theClientDto);
				}

				data[0][REPORT_ARTIKELETIKETT_LAGERORT] = getLagerFac()
						.getLagerplaezteEinesArtikels(artikelDto.getIId(),
								iIdHauplpager);

				String snrChr = null;
				if (cSnrChnr != null && cSnrChnr.length > k) {
					snrChr = cSnrChnr[k];
				}

				data[0][REPORT_ARTIKELETIKETT_MENGE] = bdMenge;
				data[0][REPORT_ARTIKELETIKETT_SNRCHNR] = snrChr;
				data[0][REPORT_ARTIKELETIKETT_KOMMENTAR] = sKommentar;

				// letzte lagerbewegung dazu holen

				SessionFactory factory = FLRSessionFactory.getFactory();
				Session session = factory.openSession();

				org.hibernate.Criteria crit = session
						.createCriteria(FLRLagerbewegung.class);
				crit.setMaxResults(1);

				crit.createAlias(LagerFac.FLR_LAGERPLAETZE_FLRARTIKEL, "a")
						.add(Expression.eq("a.i_id", artikelIId))
						.add(Expression.eq("b_historie",
								Helper.boolean2Short(false)));
				crit.createAlias(LagerFac.FLR_LAGERPLAETZE_FLRLAGER, "l");

				crit.add(Expression.eq(LagerFac.FLR_LAGERBEWEGUNG_B_ABGANG,
						Helper.boolean2Short(false)));

				if (snrChr != null) {
					crit.add(Expression.eq(
							LagerFac.FLR_LAGERBEWEGUNG_C_SERIENNRCHARGENNR,
							snrChr));
				} else {
					crit.add(Expression
							.isNull(LagerFac.FLR_LAGERBEWEGUNG_C_SERIENNRCHARGENNR));
				}

				crit.addOrder(
						Order.desc(LagerFac.FLR_LAGERBEWEGUNG_T_BELEGDATUM))
						.addOrder(
								Order.desc(LagerFac.FLR_LAGERBEWEGUNG_T_BUCHUNGSZEIT));
				List<?> resultList = crit.list();
				Iterator<?> resultListIterator = resultList.iterator();

				if (resultListIterator.hasNext()) {

					FLRLagerbewegung lagerbewegung = (FLRLagerbewegung) resultListIterator
							.next();
					data[0][REPORT_ARTIKELETIKETT_VERSION] = lagerbewegung
							.getC_version();

					// PJ18617
					PersonalDto personalDto = getPersonalFac()
							.personalFindByPrimaryKey(
									lagerbewegung.getFlrpersonal().getI_id(),
									theClientDto);

					data[0][REPORT_ARTIKELETIKETT_PERSON_BUCHENDER] = personalDto
							.formatFixUFTitelName2Name1();
					data[0][REPORT_ARTIKELETIKETT_KURZZEICHEN_BUCHENDER] = personalDto
							.getCKurzzeichen();

				}

			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}

			for (int i = 0; i < iExemplare; i++) {

				parameter.put("P_EXEMPLAR", new Integer(i + 1));
				parameter.put("P_EXEMPLAREGESAMT", iExemplare);
				theClientDto.setReportvarianteIId(varianteIId);
				if (print != null) {
					initJRDS(parameter, ArtikelReportFac.REPORT_MODUL,
							ArtikelReportFac.REPORT_ARTIKELETIKETT,
							theClientDto.getMandant(), theClientDto.getLocUi(),
							theClientDto);
					print = Helper.addReport2Report(print, getReportPrint()
							.getPrint());
				} else {
					initJRDS(parameter, ArtikelReportFac.REPORT_MODUL,
							ArtikelReportFac.REPORT_ARTIKELETIKETT,
							theClientDto.getMandant(), theClientDto.getLocUi(),
							theClientDto);
					print = getReportPrint();
				}

			}
		}

		PrintInfoDto values = new PrintInfoDto();
		values.setDocPath(new DocPath(new DocNodeArtikeletikett(artikelDto)));
		print.setOInfoForArchive(values);

		return print;

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printKundensokos(Integer artikelIId,
			TheClientDto theClientDto) {
		Integer iPreisbasis = null;
		try {
			ParametermandantDto param = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_KUNDEN,
					ParameterFac.PARAMETER_PREISBASIS_VERKAUF);
			iPreisbasis = (Integer) param.getCWertAsObject();
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		ArtikelDto dto = getArtikelFac().artikelFindByPrimaryKey(artikelIId,
				theClientDto);
		parameter.put("P_ARTIKEL", dto.formatArtikelbezeichnung());
		sAktuellerReport = ArtikelReportFac.REPORT_KUNDENSOKOS;

		String sQuery = "SELECT s FROM FLRKundesokomengenstaffel s WHERE s.flrkundesoko.artikel_i_id="
				+ artikelIId;

		if (dto.getArtgruIId() != null) {
			sQuery += " OR s.flrkundesoko.artgru_i_id=" + dto.getArtgruIId();
		}

		sQuery += " ORDER BY  s.flrkundesoko.flrkunde.flrpartner.c_name1nachnamefirmazeile1 ASC, s.n_menge ASC, s.flrkundesoko.t_preisgueltigab ASC";

		Session session = FLRSessionFactory.getFactory().openSession();

		Query inventurliste = session.createQuery(sQuery);

		List<?> resultList = inventurliste.list();

		Iterator<?> resultListIterator = resultList.iterator();

		data = new Object[resultList.size()][REPORT_KUNDENSOKOS_ANZAHL_FELDER];

		ArrayList alDaten = new ArrayList();
		while (resultListIterator.hasNext()) {
			FLRKundesokomengenstaffel flrKundesokomengenstaffel = (FLRKundesokomengenstaffel) resultListIterator
					.next();
			Object[] zeile = new Object[REPORT_KUNDENSOKOS_ANZAHL_FELDER];

			zeile[REPORT_KUNDENSOKOS_KUNDE_NAME1] = flrKundesokomengenstaffel
					.getFlrkundesoko().getFlrkunde().getFlrpartner()
					.getC_name1nachnamefirmazeile1();
			zeile[REPORT_KUNDENSOKOS_KUNDE_NAME2] = flrKundesokomengenstaffel
					.getFlrkundesoko().getFlrkunde().getFlrpartner()
					.getC_name2vornamefirmazeile2();
			zeile[REPORT_KUNDENSOKOS_KUNDE_KBEZ] = flrKundesokomengenstaffel
					.getFlrkundesoko().getFlrkunde().getFlrpartner()
					.getC_kbez();
			zeile[REPORT_KUNDENSOKOS_KUNDE_UID] = flrKundesokomengenstaffel
					.getFlrkundesoko().getFlrkunde().getFlrpartner().getC_uid();
			if (flrKundesokomengenstaffel.getFlrkundesoko().getFlrkunde()
					.getFlrpartner().getFlrlandplzort() != null) {
				zeile[REPORT_KUNDENSOKOS_KUNDE_LKZ] = flrKundesokomengenstaffel
						.getFlrkundesoko().getFlrkunde().getFlrpartner()
						.getFlrlandplzort().getFlrland().getC_lkz();
				zeile[REPORT_KUNDENSOKOS_KUNDE_PLZ] = flrKundesokomengenstaffel
						.getFlrkundesoko().getFlrkunde().getFlrpartner()
						.getFlrlandplzort().getC_plz();
				zeile[REPORT_KUNDENSOKOS_KUNDE_ORT] = flrKundesokomengenstaffel
						.getFlrkundesoko().getFlrkunde().getFlrpartner()
						.getFlrlandplzort().getFlrort().getC_name();
			}

			zeile[REPORT_KUNDENSOKOS_KUNDE_MENGE] = flrKundesokomengenstaffel
					.getN_menge();
			zeile[REPORT_KUNDENSOKOS_KUNDE_KDARTIKELNUMMER] = flrKundesokomengenstaffel
					.getFlrkundesoko().getC_kundeartikelnummer();

			try {
				KundesokoDto kdsokoDto = getKundesokoFac()
						.kundesokoFindByPrimaryKey(
								flrKundesokomengenstaffel.getFlrkundesoko()
										.getI_id());

				zeile[REPORT_KUNDENSOKOS_KUNDE_KDARTIKELNUMMER] = flrKundesokomengenstaffel
						.getFlrkundesoko().getC_kundeartikelnummer();
				zeile[REPORT_KUNDENSOKOS_KUNDE_KDARTIKELBEZ] = kdsokoDto
						.getCKundeartikelbez();
				zeile[REPORT_KUNDENSOKOS_KUNDE_KDARTIKELBEZ2] = kdsokoDto
						.getCKundeartikelzbez();

				// Preis berechnen
				BigDecimal nBerechneterPreis = null;

				if (kdsokoDto.getArtikelIId() != null
						|| kdsokoDto.getArtgruIId() != null) {
					// der Preis muss an dieser Stelle berechnet werden

					if (flrKundesokomengenstaffel.getN_fixpreis() != null) {
						nBerechneterPreis = flrKundesokomengenstaffel
								.getN_fixpreis();
						// Fixpreis ist in Kundenwaehrung -> nach
						// Mandantenwehrung umrechnen
						KundeDto kdDto = getKundeFac()
								.kundeFindByPrimaryKeySmall(
										flrKundesokomengenstaffel
												.getFlrkundesoko()
												.getFlrkunde().getI_id());

						try {
							if (!kdDto.getCWaehrung().equals(
									theClientDto.getSMandantenwaehrung())) {
								nBerechneterPreis = getLocaleFac()
										.rechneUmInAndereWaehrungZuDatum(
												flrKundesokomengenstaffel
														.getN_fixpreis(),
												kdDto.getCWaehrung(),
												theClientDto
														.getSMandantenwaehrung(),
												Helper.cutDate(new java.sql.Date(
														System.currentTimeMillis())),
												theClientDto);
							}
						} catch (RemoteException e) {
							throwEJBExceptionLPRespectOld(e);
						}

					} else {
						// WH 21.06.06 Es gilt die VK-Basis, die zu Beginn der
						// Mengenstaffel gueltig ist
						BigDecimal nPreisbasis = null;
						if (iPreisbasis == 0 || iPreisbasis == 2) {

							nPreisbasis = getVkPreisfindungFac()
									.ermittlePreisbasis(
											artikelIId,
											new java.sql.Date(kdsokoDto
													.getTPreisgueltigab()
													.getTime()),
											null,
											theClientDto
													.getSMandantenwaehrung(),
											theClientDto);
						} else {
							nPreisbasis = getVkPreisfindungFac()
									.ermittlePreisbasis(
											artikelIId,
											new java.sql.Date(kdsokoDto
													.getTPreisgueltigab()
													.getTime()),
											flrKundesokomengenstaffel
													.getFlrkundesoko()
													.getFlrkunde()
													.getVkpfartikelpreisliste_i_id_stdpreisliste(),
											theClientDto
													.getSMandantenwaehrung(),
											theClientDto);
						}

						VerkaufspreisDto vkpfDto = getVkPreisfindungFac()
								.berechneVerkaufspreis(
										nPreisbasis,
										flrKundesokomengenstaffel
												.getF_rabattsatz());

						if (vkpfDto != null) {
							nBerechneterPreis = vkpfDto.nettopreis;
						} else {
							// Wahrscheinlich keine VK-Preisbasis verfuegbar
							VkPreisfindungEinzelverkaufspreisDto[] dtos = getVkPreisfindungFac()
									.vkPreisfindungEinzelverkaufspreisfindByMandantCNrArtikelIIdAbGueltigab(
											artikelIId,
											new java.sql.Date(kdsokoDto
													.getTPreisgueltigab()
													.getTime()), theClientDto);
							if (dtos.length > 0) {
								zeile[REPORT_KUNDENSOKOS_KUNDE_NAECHSTE_VKPREISBASIS_GUELTIG_AB] = dtos[0]
										.getTVerkaufspreisbasisgueltigab();
							}

						}

					}
				}
				zeile[REPORT_KUNDENSOKOS_KUNDE_BERECHNETER_PREIS] = nBerechneterPreis;

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			zeile[REPORT_KUNDENSOKOS_KUNDE_GUELTIAGAB] = flrKundesokomengenstaffel
					.getFlrkundesoko().getT_preisgueltigab();
			zeile[REPORT_KUNDENSOKOS_KUNDE_GUELTIAGBIS] = flrKundesokomengenstaffel
					.getFlrkundesoko().getT_preisgueltigbis();

			zeile[REPORT_KUNDENSOKOS_KUNDE_FIXPREIS] = flrKundesokomengenstaffel
					.getN_fixpreis();
			zeile[REPORT_KUNDENSOKOS_KUNDE_RABATT] = flrKundesokomengenstaffel
					.getF_rabattsatz();

			alDaten.add(zeile);
		}

		Object[][] returnArray = new Object[alDaten.size()][REPORT_KUNDENSOKOS_ANZAHL_FELDER];
		data = (Object[][]) alDaten.toArray(returnArray);

		initJRDS(parameter, ArtikelReportFac.REPORT_MODUL,
				ArtikelReportFac.REPORT_KUNDENSOKOS, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printLagerplatzetikett(Integer lagerplatzIId,
			TheClientDto theClientDto) {
		// Erstellung des Reports
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		index = -1;
		sAktuellerReport = ArtikelReportFac.REPORT_LAGERPLATZETIKETT;
		data = new Object[1][REPORT_LAGERPLATZ_ANZAHL_SPALTEN];

		try {
			LagerplatzDto lagerplatzDto = getLagerFac()
					.lagerplatzFindByPrimaryKey(lagerplatzIId);
			data[0][REPORT_LAGERPLATZ_LAGERPLATZ] = lagerplatzDto
					.getCLagerplatz();

			LagerDto lagerDto = getLagerFac().lagerFindByPrimaryKey(
					lagerplatzDto.getLagerIId());
			data[0][REPORT_LAGERPLATZ_LAGER] = lagerDto.getCNr();

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		initJRDS(parameter, ArtikelReportFac.REPORT_MODUL,
				ArtikelReportFac.REPORT_LAGERPLATZETIKETT,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printMindestlagerstaende(TheClientDto theClientDto) {

		sAktuellerReport = ArtikelReportFac.REPORT_MINDESTLAGERSTAENDE;

		String sQuery = "SELECT a,(SELECT sum(artikellager.n_lagerstand) FROM FLRArtikellager AS artikellager WHERE artikellager.compId.artikel_i_id=a.i_id "
				+ "AND artikellager.mandant_c_nr=a.mandant_c_nr"
				+ "), "
				+ " ( SELECT sum(r.n_gesamtmenge) FROM FLRRahmenbedarfe r WHERE r.flrartikel.i_id=a.i_id )"
				+ "FROM FLRArtikel a WHERE a.b_versteckt=0 AND mandant_c_nr='"
				+ theClientDto.getMandant()
				+ "' AND (a.f_lagermindest IS NOT NULL OR a.f_fertigungssatzgroesse IS NOT NULL )"
				+ "AND (a.f_lagermindest >0 OR a.f_fertigungssatzgroesse >0) AND a.artikelart_c_nr NOT IN ('Handartikel') ORDER BY a.c_nr";

		Session session = FLRSessionFactory.getFactory().openSession();

		Query inventurliste = session.createQuery(sQuery);

		List<?> resultList = inventurliste.list();

		Iterator<?> resultListIterator = resultList.iterator();

		data = new Object[resultList.size()][REPORT_MINDESTLAGERSTAENDE_ANZAHL_SPALTEN];

		int row = 0;
		while (resultListIterator.hasNext()) {
			Object o[] = (Object[]) resultListIterator.next();
			FLRArtikel a = (FLRArtikel) o[0];

			BigDecimal lagerstand = (BigDecimal) o[1];
			BigDecimal detailbedarfe = (BigDecimal) o[2];

			data[row][REPORT_MINDESTLAGERSTAENDE_ARTIKEL] = a.getC_nr();
			data[row][REPORT_MINDESTLAGERSTAENDE_FERTIGUNGSSATZGROESSE] = a
					.getF_fertigungssatzgroesse();
			data[row][REPORT_MINDESTLAGERSTAENDE_LAGERMINDESTSTAND] = a
					.getF_lagermindest();
			data[row][REPORT_MINDESTLAGERSTAENDE_LAGERSOLLSTAND] = a
					.getF_lagersoll();

			data[row][REPORT_MINDESTLAGERSTAENDE_LAGERSTAND] = lagerstand;
			data[row][REPORT_MINDESTLAGERSTAENDE_DETAILBEDARF] = detailbedarfe;

			try {
				data[row][REPORT_MINDESTLAGERSTAENDE_LETZTER_ABGANG] = getLagerFac()
						.getDatumLetzterZugangsOderAbgangsbuchung(a.getI_id(),
								true);

				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKeySmall(a.getI_id(), theClientDto);

				if (artikelDto.getArtikelsprDto() != null) {
					data[row][REPORT_MINDESTLAGERSTAENDE_BEZEICHNUNG] = artikelDto
							.getArtikelsprDto().getCBez();
					data[row][REPORT_MINDESTLAGERSTAENDE_ZUSATZBEZEICHNUNG] = artikelDto
							.getArtikelsprDto().getCZbez();
					data[row][REPORT_MINDESTLAGERSTAENDE_ZUSATZBEZEICHNUNG2] = artikelDto
							.getArtikelsprDto().getCZbez2();
					data[row][REPORT_MINDESTLAGERSTAENDE_KURZBEZEICHNUNG] = artikelDto
							.getArtikelsprDto().getCKbez();
				}

				Hashtable<?, ?> htRahmenbestellt = getArtikelbestelltFac()
						.getAnzahlRahmenbestellt(a.getI_id(), theClientDto);
				if (htRahmenbestellt
						.containsKey(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL)) {
					data[row][REPORT_MINDESTLAGERSTAENDE_RAHMENBESTELLT] = htRahmenbestellt
							.get(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL);
				}

				data[row][REPORT_MINDESTLAGERSTAENDE_RAHMENRESERVIERT] = getReservierungFac()
						.getAnzahlRahmenreservierungen(a.getI_id(),
								theClientDto);

				// MinVK
				VkPreisfindungEinzelverkaufspreisDto vkpfDto = getVkPreisfindungFac()
						.getArtikeleinzelverkaufspreis(a.getI_id(),
								new java.sql.Date(System.currentTimeMillis()),
								theClientDto.getSMandantenwaehrung(),
								theClientDto);
				if (vkpfDto != null && vkpfDto.getNVerkaufspreisbasis() != null) {
					data[row][REPORT_MINDESTLAGERSTAENDE_VK_PREISBASIS] = vkpfDto
							.getNVerkaufspreisbasis();
				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			row++;
		}
		session.close();

		HashMap<String, Object> parameter = new HashMap<String, Object>();
		initJRDS(parameter, ArtikelbestelltFac.REPORT_MODUL,
				ArtikelReportFac.REPORT_MINDESTLAGERSTAENDE,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);
		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAenderungen(Integer artikelIId,
			TheClientDto theClientDto) {

		if (artikelIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelIId == null"));
		}
		// Erstellung des Reports
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		ArtikelDto dto = getArtikelFac().artikelFindByPrimaryKey(artikelIId,
				theClientDto);
		parameter.put("P_ARTIKEL", dto.formatArtikelbezeichnung());
		index = -1;
		sAktuellerReport = ArtikelReportFac.REPORT_AENDERUNGEN;

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session
				.createCriteria(FLRArtikellog.class);
		crit.add(Restrictions.eq("artikel_i_id", artikelIId));
		crit.addOrder(Order.desc("t_aendern"));
		List<?> results = crit.list();
		Iterator<?> resultListIterator = results.iterator();

		data = new Object[results.size()][ArtikelReportFacBean.REPORT_AENDERUNGEN_ANZAHL_SPALTEN];

		int i = 0;
		while (resultListIterator.hasNext()) {
			FLRArtikellog flrArtikellog = (FLRArtikellog) resultListIterator
					.next();
			data[i][ArtikelReportFacBean.REPORT_AENDERUNGEN_EIGENSCHAFT] = flrArtikellog
					.getC_key();
			data[i][ArtikelReportFacBean.REPORT_AENDERUNGEN_VON] = flrArtikellog
					.getC_von();
			data[i][ArtikelReportFacBean.REPORT_AENDERUNGEN_NACH] = flrArtikellog
					.getC_nach();
			data[i][ArtikelReportFacBean.REPORT_AENDERUNGEN_SPRACHE] = flrArtikellog
					.getLocale_c_nr();
			data[i][ArtikelReportFacBean.REPORT_AENDERUNGEN_WANN] = flrArtikellog
					.getT_aendern();

			data[i][ArtikelReportFacBean.REPORT_AENDERUNGEN_WER] = HelperServer
					.formatNameAusFLRPartner(flrArtikellog.getFlrpersonal()
							.getFlrpartner());

			i++;
		}

		initJRDS(parameter, ArtikelbestelltFac.REPORT_MODUL,
				ArtikelReportFac.REPORT_AENDERUNGEN, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);
		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printArtikelbestellt(Integer artikelIId,
			java.sql.Date dVon, java.sql.Date dBis, TheClientDto theClientDto) {
		if (artikelIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelIId == null"));
		}
		// Erstellung des Reports
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		index = -1;
		sAktuellerReport = ArtikelReportFac.REPORT_ARTIKELBESTELLT;
		ArtikelDto dto = getArtikelFac().artikelFindByPrimaryKeySmall(
				artikelIId, theClientDto);
		parameter.put("P_ARTIKEL", dto.formatArtikelbezeichnung());
		parameter.put("P_EINHEIT", dto.getEinheitCNr().trim());
		parameter.put("P_BESTELLEINHEIT_INVERS",
				Helper.short2Boolean(dto.getbBestellmengeneinheitInvers()));
		if (dto.getEinheitCNrBestellung() != null) {
			parameter.put("P_BESTELLEINHEIT", dto.getEinheitCNrBestellung()
					.trim());
		} else {
			parameter.put("P_BESTELLEINHEIT", "");
		}

		String eingeloggterMandant = theClientDto.getMandant();

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria bestelltliste = session
				.createCriteria(FLRArtikelbestellt.class);
		bestelltliste.createAlias(
				ArtikelbestelltFac.FLR_ARTIKELBESTELLT_FLRARTIKEL, "a").add(
				Restrictions.eq("a.i_id", artikelIId));
		if (dVon != null) {
			bestelltliste.add(Restrictions
					.ge(ArtikelbestelltFac.FLR_ARTIKELBESTELLT_D_LIEFERTERMIN,
							dVon));
		}
		if (dBis != null) {
			bestelltliste.add(Restrictions
					.lt(ArtikelbestelltFac.FLR_ARTIKELBESTELLT_D_LIEFERTERMIN,
							dBis));
		}

		bestelltliste.addOrder(Order
				.asc(ArtikelbestelltFac.FLR_ARTIKELBESTELLT_D_LIEFERTERMIN));

		List<?> resultList = bestelltliste.list();
		Iterator<?> resultListIterator = resultList.iterator();
		int row = 0;

		Object[][] dataHelp = new Object[resultList.size()][10];

		while (resultListIterator.hasNext()) {
			FLRArtikelbestellt artikelbestellt = (FLRArtikelbestellt) resultListIterator
					.next();

			String sBelegnummer = null;
			String sPartner = null;
			String sProjektbezeichnung = null;
			String sMandant = null;
			Date dAuftragbestliefertermin = null;

			BestellpositionDto bestellpositionDto = null;
			BestellungDto bestellungDto = null;
			if (artikelbestellt.getC_belegartnr().equals(
					LocaleFac.BELEGART_BESTELLUNG)) {
				try {
					bestellpositionDto = getBestellpositionFac()
							.bestellpositionFindByPrimaryKey(
									artikelbestellt.getI_belegartpositionid());
					if (!bestellpositionDto
							.getBestellpositionstatusCNr()
							.equals(BestellpositionFac.BESTELLPOSITIONSTATUS_ERLEDIGT)) {
						bestellungDto = getBestellungFac()
								.bestellungFindByPrimaryKey(
										bestellpositionDto.getBestellungIId());
						sBelegnummer = bestellungDto.getCNr();
						sMandant = bestellungDto.getMandantCNr();
						sProjektbezeichnung = bestellungDto.getCBez();
						dAuftragbestliefertermin = bestellpositionDto
								.getTAuftragsbestaetigungstermin();

						sPartner = getLieferantFac()
								.lieferantFindByPrimaryKey(
										bestellungDto
												.getLieferantIIdBestelladresse(),
										theClientDto).getPartnerDto()
								.formatTitelAnrede();
					}
				} catch (RemoteException ex3) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex3);
				}
			}

			if (eingeloggterMandant.equals(sMandant)) {

				dataHelp[row][REPORT_BESTELLTLISTE_BESTELLUNG] = sBelegnummer;
				dataHelp[row][REPORT_BESTELLTLISTE_LIEFERANTENNAME] = sPartner;
				dataHelp[row][REPORT_BESTELLTLISTE_PROJEKTNAME] = sProjektbezeichnung;
				// Wenn
				if (dAuftragbestliefertermin != null) {
					dataHelp[row][REPORT_BESTELLTLISTE_LIEFERTERMIN] = dAuftragbestliefertermin;
				} else {
					dataHelp[row][REPORT_BESTELLTLISTE_LIEFERTERMIN] = artikelbestellt
							.getT_liefertermin();

				}

				if (dto.getEinheitCNrBestellung() != null) {

					if (Helper.short2boolean(dto
							.getbBestellmengeneinheitInvers()) == true) {
						if (dto.getNUmrechnungsfaktor().doubleValue() != 0) {
							dataHelp[row][REPORT_BESTELLTLISTE_BESTELLMENGE] = artikelbestellt
									.getN_menge().divide(
											dto.getNUmrechnungsfaktor(), 4,
											BigDecimal.ROUND_UP);
						}
					} else {
						dataHelp[row][REPORT_BESTELLTLISTE_BESTELLMENGE] = artikelbestellt
								.getN_menge().multiply(
										dto.getNUmrechnungsfaktor());
					}

				}

				if (artikelbestellt.getC_belegartnr().equals(
						LocaleFac.BELEGART_BESTELLUNG)) {
					dataHelp[row][REPORT_BESTELLTLISTE_MENGE] = bestellpositionDto
							.getNMenge();
					dataHelp[row][REPORT_BESTELLTLISTE_AB_NUMMER] = bestellpositionDto
							.getCABNummer();
					dataHelp[row][REPORT_BESTELLTLISTE_LIEFERTERMIN] = bestellpositionDto
							.getTUebersteuerterLiefertermin();
					dataHelp[row][REPORT_BESTELLTLISTE_AB_TERMIN] = bestellpositionDto
							.getTAuftragsbestaetigungstermin();
					dataHelp[row][REPORT_BESTELLTLISTE_OFFENEMENGE] = artikelbestellt
							.getN_menge();
					dataHelp[row][REPORT_BESTELLTLISTE_AB_KOMMENTAR] = bestellpositionDto
							.getCABKommentar();
				}

				row++;
			}

		}
		session.close();
		data = new Object[row][10];
		for (int i = 0; i < row; i++) {
			data[i] = dataHelp[i];
		}

		initJRDS(parameter, ArtikelbestelltFac.REPORT_MODUL,
				ArtikelReportFac.REPORT_ARTIKELBESTELLT,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);
		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printLagercockpitWELagerVerteilungsvorschlag(
			TheClientDto theClientDto) {
		HashMap<String, Object> parameter = new HashMap<String, Object>();

		index = -1;
		sAktuellerReport = ArtikelReportFac.REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG;
		Session sessionArtikellager = FLRSessionFactory.getFactory()
				.openSession();
		String sQueryArtikellager = "SELECT artikellager from FLRArtikellager artikellager LEFT OUTER JOIN artikellager.flrartikelliste.artikelsprset AS aspr  WHERE artikellager.flrartikelliste.mandant_c_nr = '"
				+ theClientDto.getMandant()
				+ "' AND artikellager.flrlager.lagerart_c_nr = '"
				+ LagerFac.LAGERART_WARENEINGANG
				+ "' AND artikellager.n_lagerstand > 0 ORDER BY artikellager.flrartikel.c_nr ";

		Query queryArtikellager = sessionArtikellager
				.createQuery(sQueryArtikellager);

		List<?> resultListArtikelager = queryArtikellager.list();
		Iterator<?> resultListIteratorArtikelager = resultListArtikelager
				.iterator();

		ArrayList al = new ArrayList();

		while (resultListIteratorArtikelager.hasNext()) {
			FLRArtikellager artikellager = (FLRArtikellager) resultListIteratorArtikelager
					.next();

			BigDecimal lagerstandKumuliert = artikellager.getN_lagerstand();

			Session session = FLRSessionFactory.getFactory().openSession();
			String queryString = "SELECT flrfehlmenge from FLRFehlmenge flrfehlmenge LEFT OUTER JOIN flrfehlmenge.flrartikelliste.artikellagerplatzset as artikellagerplatzset WHERE (artikellagerplatzset.i_sort=1 OR artikellagerplatzset.i_sort is null)  AND flrfehlmenge.flrartikel.i_id="
					+ artikellager.getFlrartikel().getI_id()
					+ " ORDER BY flrfehlmenge.flrlossollmaterial.flrlos.t_produktionsbeginn, flrfehlmenge.flrlossollmaterial.flrlos.c_nr  ";

			Query query = session.createQuery(queryString);

			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();

			Object[] zeileVorlage = new Object[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_ANZAHL_SPALTEN];
			zeileVorlage[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_ARTIKELNUMMER] = artikellager
					.getFlrartikel().getC_nr();
			zeileVorlage[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LAGERBEWIRTSCHAFTET] = Helper
					.short2Boolean(artikellager.getFlrartikel()
							.getB_lagerbewirtschaftet());
			ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(
					artikellager.getFlrartikel().getI_id(), theClientDto);
			if (aDto.getArtikelsprDto() != null) {
				zeileVorlage[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_BEZEICHNUNG] = aDto
						.getArtikelsprDto().getCBez();
				zeileVorlage[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_ZUSATZBEZEICHNUNG] = aDto
						.getArtikelsprDto().getCZbez();
			}

			zeileVorlage[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_EINHEIT] = artikellager
					.getFlrartikel().getEinheit_c_nr();

			Integer lagerIId = null;
			if (artikellager.getFlrartikelliste().getArtikellagerplatzset()
					.size() > 0) {
				Iterator it = artikellager.getFlrartikelliste()
						.getArtikellagerplatzset().iterator();
				while (it.hasNext()) {
					FLRArtikellagerplaetze flrArtikellagerplaetze = (FLRArtikellagerplaetze) it
							.next();
					if (flrArtikellagerplaetze.getI_sort() == 1) {
						zeileVorlage[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LAGER] = flrArtikellagerplaetze
								.getFlrlagerplatz().getFlrlager().getC_nr();
						lagerIId = flrArtikellagerplaetze.getFlrlagerplatz()
								.getFlrlager().getI_id();
						break;
					}
				}

			}

			try {

				String sLagerplaetze = getLagerFac()
						.getLagerplaezteEinesArtikels(aDto.getIId(), null);

				boolean bMehrerLagerplaetze = false;

				if (sLagerplaetze != null
						&& sLagerplaetze.split(",").length > 2) {
					bMehrerLagerplaetze = true;
				}
				if (bMehrerLagerplaetze == true && lagerIId != null) {
					zeileVorlage[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LAGERPLAETZE] = getLagerFac()
							.getLagerplaezteEinesArtikels(
									artikellager.getFlrartikel().getI_id(),
									lagerIId)
							+ " ++";
				} else {
					zeileVorlage[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LAGERPLAETZE] = getLagerFac()
							.getLagerplaezteEinesArtikels(
									artikellager.getFlrartikel().getI_id(),
									lagerIId);
				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			if (resultList.size() > 0) {

				while (resultListIterator.hasNext()) {
					FLRFehlmenge flrFehlmenge = (FLRFehlmenge) resultListIterator
							.next();

					Object[] zeile = zeileVorlage.clone();

					LosDto losDto;
					try {
						losDto = getFertigungFac().losFindByPrimaryKey(
								flrFehlmenge.getFlrlossollmaterial()
										.getFlrlos().getI_id());
						zeile[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LOS_KOMMENTAR] = losDto
								.getCKommentar();
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}

					zeile[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_UMZUBUCHENDE_MENGE] = flrFehlmenge
							.getN_menge();

					lagerstandKumuliert = lagerstandKumuliert
							.subtract(flrFehlmenge.getN_menge());
					zeile[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LAGERSTAND_KUMULIERT] = lagerstandKumuliert;

					zeile[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LOS_BEGINN] = flrFehlmenge
							.getFlrlossollmaterial().getFlrlos()
							.getT_produktionsbeginn();
					zeile[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LOS_ENDE] = flrFehlmenge
							.getFlrlossollmaterial().getFlrlos()
							.getT_produktionsende();

					zeile[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LOS_NUMMER] = flrFehlmenge
							.getFlrlossollmaterial().getFlrlos().getC_nr();

					zeile[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LOS_PROJEKT] = flrFehlmenge
							.getFlrlossollmaterial().getFlrlos().getC_projekt();

					if (flrFehlmenge.getFlrlossollmaterial().getFlrlos()
							.getFlrstueckliste() != null) {
						zeile[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LOS_STKLNUMMER] = flrFehlmenge
								.getFlrlossollmaterial().getFlrlos()
								.getFlrstueckliste().getFlrartikel().getC_nr();

						ArtikelDto aDtoStkl = getArtikelFac()
								.artikelFindByPrimaryKeySmall(
										flrFehlmenge.getFlrlossollmaterial()
												.getFlrlos()
												.getFlrstueckliste()
												.getFlrartikel().getI_id(),
										theClientDto);

						if (aDtoStkl.getArtikelsprDto() != null) {
							zeile[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LOS_STKLBEZEICHUNG] = aDtoStkl
									.getArtikelsprDto().getCBez();
							zeile[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LOS_STKLZUSATZBEZEICHNUNG] = aDtoStkl
									.getArtikelsprDto().getCZbez();
						}

					}

					al.add(zeile);

				}
			} else {
				// Wenns keine Fehlmengen gibt

				zeileVorlage[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LAGERSTAND_KUMULIERT] = artikellager
						.getN_lagerstand();
				al.add(zeileVorlage);

			}

			session.close();
		}

		sessionArtikellager.close();
		sessionArtikellager = FLRSessionFactory.getFactory().openSession();

		// Nun noch alle nicht verrauemten Artikel hinzufuegen

		String query = "SELECT wep  from FLRWareneingangspositionen wep WHERE wep.b_verraeumt = 0 AND wep.flrbestellposition.flrartikel.artikelart_c_nr <> 'Handartikel    ' AND wep.flrbestellposition.flrartikel.b_lagerbewirtschaftet = 0 ORDER BY wep.flrbestellposition.flrartikel.c_nr ASC";

		queryArtikellager = sessionArtikellager.createQuery(query);

		resultListArtikelager = queryArtikellager.list();
		resultListIteratorArtikelager = resultListArtikelager.iterator();

		while (resultListIteratorArtikelager.hasNext()) {
			FLRWareneingangspositionen wep = (FLRWareneingangspositionen) resultListIteratorArtikelager
					.next();

			Object[] zeile = new Object[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_ANZAHL_SPALTEN];

			zeile[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_ARTIKELNUMMER] = wep
					.getFlrbestellposition().getFlrartikel().getC_nr();
			zeile[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_UMZUBUCHENDE_MENGE] = wep
					.getN_geliefertemenge();

			ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(
					wep.getFlrbestellposition().getFlrartikel().getI_id(),
					theClientDto);
			if (aDto.getArtikelsprDto() != null) {
				zeile[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_BEZEICHNUNG] = aDto
						.getArtikelsprDto().getCBez();
				zeile[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_ZUSATZBEZEICHNUNG] = aDto
						.getArtikelsprDto().getCZbez();
			}

			zeile[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LAGERBEWIRTSCHAFTET] = Boolean.FALSE;

			zeile[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_BESTELLNUMMER] = wep
					.getFlrbestellposition().getFlrbestellung().getC_nr();
			zeile[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_WARENEINGANG] = wep
					.getFlrwareneingang().getC_lieferscheinnr();

			Integer lagerIId = null;
			if (wep.getFlrbestellposition().getFlrartikel()
					.getArtikellagerplatzset().size() > 0) {
				Iterator it = wep.getFlrbestellposition().getFlrartikel()
						.getArtikellagerplatzset().iterator();
				while (it.hasNext()) {
					FLRArtikellagerplaetze flrArtikellagerplaetze = (FLRArtikellagerplaetze) it
							.next();
					if (flrArtikellagerplaetze.getI_sort() == 1) {
						zeile[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LAGER] = flrArtikellagerplaetze
								.getFlrlagerplatz().getFlrlager().getC_nr();
						lagerIId = flrArtikellagerplaetze.getFlrlagerplatz()
								.getFlrlager().getI_id();
						break;
					}
				}

			}

			try {

				String sLagerplaetze = getLagerFac()
						.getLagerplaezteEinesArtikels(aDto.getIId(), null);

				boolean bMehrerLagerplaetze = false;

				if (sLagerplaetze != null
						&& sLagerplaetze.split(",").length > 2) {
					bMehrerLagerplaetze = true;
				}
				if (bMehrerLagerplaetze == true && lagerIId != null) {
					zeile[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LAGERPLAETZE] = getLagerFac()
							.getLagerplaezteEinesArtikels(
									wep.getFlrbestellposition().getFlrartikel()
											.getI_id(), lagerIId)
							+ " ++";
				} else {
					zeile[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LAGERPLAETZE] = getLagerFac()
							.getLagerplaezteEinesArtikels(
									wep.getFlrbestellposition().getFlrartikel()
											.getI_id(), lagerIId);
				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			al.add(zeile);

		}

		sessionArtikellager.close();

		// und nach ArtikelNR-sortieren

		// Nach Artikel sortieren
		for (int i = al.size() - 1; i > 0; --i) {
			for (int j = 0; j < i; ++j) {
				Object[] a = (Object[]) al.get(j);

				String artikelnummer1 = (String) a[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_ARTIKELNUMMER];

				Object[] b = (Object[]) al.get(j + 1);

				String artikelnummer2 = (String) b[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_ARTIKELNUMMER];

				if (artikelnummer1.compareTo(artikelnummer2) > 0) {
					Object[] h = a;
					al.set(j, b);
					al.set(j + 1, h);
				}
			}
		}

		Object[][] returnArray = new Object[al.size()][REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_ANZAHL_SPALTEN];
		data = (Object[][]) al.toArray(returnArray);

		initJRDS(
				parameter,
				ArtikelReportFac.REPORT_MODUL,
				ArtikelReportFac.REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printLagercockpitMaterialVerteilungsvorschlag(
			boolean bNurArtikelMitLagerstand, TheClientDto theClientDto)
			throws EJBExceptionLP {
		HashMap<String, Object> parameter = new HashMap<String, Object>();

		parameter.put("P_NURARTIKELMITLAGERSTAND", new Boolean(
				bNurArtikelMitLagerstand));

		TreeMap tmDaten = new TreeMap();

		index = -1;
		sAktuellerReport = ArtikelReportFac.REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG;

		Session session = FLRSessionFactory.getFactory().openSession();
		String queryString = "SELECT umbuchung FROM FLRLagercockpitumbuchung umbuchung  LEFT OUTER JOIN umbuchung.flrlager_lagerplatz as flrlager_lagerplatz WHERE 1=1 ";
		if (bNurArtikelMitLagerstand == true) {
			queryString += " AND umbuchung.lagerstand >0 ";
		}

		// PJ18216
		boolean bNichtLagerbewSofortAusgeben = false;
		try {
			ParametermandantDto parameterM = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_FERTIGUNG,
							ParameterFac.PARAMETER_NICHT_LAGERBEWIRTSCHAFTETE_SOFORT_AUSGEBEN);
			bNichtLagerbewSofortAusgeben = ((Boolean) parameterM
					.getCWertAsObject()).booleanValue();

		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		if (bNichtLagerbewSofortAusgeben == true) {
			queryString += " AND umbuchung.flrartikel.b_lagerbewirtschaftet = 1 ";
		}

		queryString += " ORDER BY umbuchung.flrartikel.c_nr ASC, umbuchung.flrlos.c_nr ASC, flrlager_lagerplatz.c_nr ASC ";
		Query query = session.createQuery(queryString);

		List<?> resultList = query.list();
		Iterator<?> resultListIterator = resultList.iterator();

		int iZeile = 0;

		while (resultListIterator.hasNext()) {
			FLRLagercockpitumbuchung losmat = (FLRLagercockpitumbuchung) resultListIterator
					.next();
			Object[] zeile = new Object[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_ANZAHL_SPALTEN];
			zeile[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_ARTIKELNUMMER] = losmat
					.getFlrartikel().getC_nr();
			ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(
					losmat.getFlrartikel().getI_id(), theClientDto);
			if (aDto.getArtikelsprDto() != null) {
				zeile[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_BEZEICHNUNG] = aDto
						.getArtikelsprDto().getCBez();
				zeile[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_ZUSATZBEZEICHNUNG] = aDto
						.getArtikelsprDto().getCZbez();
			}

			zeile[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_EINHEIT] = losmat
					.getFlrartikel().getEinheit_c_nr();
			Integer lagerIId = null;
			if (losmat.getFlrlager_lagerplatz() != null) {
				zeile[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LAGER] = losmat
						.getFlrlager_lagerplatz().getC_nr();

				lagerIId = losmat.getFlrlager_lagerplatz().getI_id();

			}
			try {

				String sLagerplaetze = getLagerFac()
						.getLagerplaezteEinesArtikels(aDto.getIId(), null);

				boolean bMehrerLagerplaetze = false;

				if (sLagerplaetze != null
						&& sLagerplaetze.split(",").length > 2) {
					bMehrerLagerplaetze = true;
				}
				if (bMehrerLagerplaetze == true && lagerIId != null) {
					zeile[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LAGERPLAETZE] = getLagerFac()
							.getLagerplaezteEinesArtikels(
									losmat.getFlrartikel().getI_id(), lagerIId)
							+ " ++";
				} else {
					zeile[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LAGERPLAETZE] = getLagerFac()
							.getLagerplaezteEinesArtikels(
									losmat.getFlrartikel().getI_id(), lagerIId);
				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			LosDto losDto;
			try {
				losDto = getFertigungFac().losFindByPrimaryKey(
						losmat.getFlrlos().getI_id());
				zeile[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LOS_KOMMENTAR] = losDto
						.getCKommentar();
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			zeile[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_MENGE] = losmat
					.getDiff();
			zeile[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LAGERSTAND] = losmat
					.getLagerstand();
			zeile[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LOS_BEGINN] = losmat
					.getFlrlos().getT_produktionsbeginn();
			zeile[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LOS_ENDE] = losmat
					.getFlrlos().getT_produktionsende();

			zeile[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LOS_NUMMER] = losmat
					.getFlrlos().getC_nr();

			zeile[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LOS_PROJEKT] = losmat
					.getFlrlos().getC_projekt();

			if (losmat.getFlrlos().getFlrstueckliste() != null) {
				zeile[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LOS_STKLNUMMER] = losmat
						.getFlrlos().getFlrstueckliste().getFlrartikel()
						.getC_nr();

				ArtikelDto aDtoStkl = getArtikelFac()
						.artikelFindByPrimaryKeySmall(
								losmat.getFlrlos().getFlrstueckliste()
										.getFlrartikel().getI_id(),
								theClientDto);

				if (aDtoStkl.getArtikelsprDto() != null) {
					zeile[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LOS_STKLBEZEICHUNG] = aDtoStkl
							.getArtikelsprDto().getCBez();
					zeile[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LOS_STKLZUSATZBEZEICHNUNG] = aDtoStkl
							.getArtikelsprDto().getCZbez();
				}

			}

			// Es gibt 3 Typen
			String key = "";

			if (losmat.getDiff().doubleValue() > 0) {
				// Zuwenig -> Lager an Los
				// 3.)LAGER-LOS

				zeile[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_TYP] = "LAGER_AN_LOS";

				LagerDto lDto = getLagerFac()
						.getLagerDtoWennNurAufEinemTypLagerstandVorhandenIst(
								losmat.getFlrartikel().getI_id());

				String lager = "";

				if (lDto != null) {
					zeile[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_QUELLE] = "Lager: "
							+ lDto.getCNr();
					lager = lDto.getCNr();
				}

				key = "3"
						+ Helper.fitString2Length(lager, 30, ' ')
						+ Helper.fitString2Length(losmat.getFlrlos().getC_nr(),
								30, ' ');

			} else {
				// Zuviel

				try {
					// Los an Los
					ArrayList al = getFehlmengeFac().getFehlmengen(
							losmat.getFlrartikel().getI_id(),
							theClientDto.getMandant(), theClientDto);
					FLRLos losZu = null;
					for (int i = 0; i < al.size(); i++) {
						FLRFehlmenge flr = (FLRFehlmenge) al.get(i);
						if (flr.getN_menge().doubleValue() > 0) {

							if (!flr.getFlrlossollmaterial().getI_id()
									.equals(losmat.getI_id())) {
								losZu = flr.getFlrlossollmaterial().getFlrlos();
								break;
							}

						}
					}

					if (losZu != null) {
						// 1.)LOS-ZU:LOS-AB
						key = "1"
								+ Helper.fitString2Length(losmat.getFlrlos()
										.getC_nr(), 30, ' ')
								+ Helper.fitString2Length(losZu.getC_nr(), 30,
										' ');
						zeile[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_TYP] = "LOS_ZU_LOS";
						zeile[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_ZIEL] = losZu
								.getC_nr();
						// oder
					} else {
						// Los an Lager
						// 2.)LOS-LAGER

						// Lager definiert artikel

						String lager = "";
						if (losmat.getFlrlager_lagerplatz() != null) {
							lager = losmat.getFlrlager_lagerplatz().getC_nr();
							zeile[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_ZIEL] = lager;
						}

						key = "2"
								+ Helper.fitString2Length(lager, 30, ' ')
								+ Helper.fitString2Length(losmat.getFlrlos()
										.getC_nr(), 30, ' ');
						zeile[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_TYP] = "LOS_AN_LAGER";
					}

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}

			key += iZeile;

			tmDaten.put(key, zeile);
			iZeile++;

		}

		Iterator it = tmDaten.keySet().iterator();
		data = new Object[tmDaten.size()][REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_ANZAHL_SPALTEN];
		iZeile = 0;
		while (it.hasNext()) {
			Object key = it.next();

			data[iZeile] = (Object[]) tmDaten.get(key);

			iZeile++;
		}

		initJRDS(
				parameter,
				ArtikelReportFac.REPORT_MODUL,
				ArtikelReportFac.REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();

	}

	/**
	 * Fuer einen bestimmten Artikel die Rahmenreservierung des Auftrags
	 * drucken.
	 * 
	 * @param kritDtoI
	 *            die Auswertungskriterien des Benutzers
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return JasperPrint der Druck
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printRahmenreservierungsliste(
			ReportAnfragestatistikKriterienDto kritDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (kritDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("kritDtoI == null"));
		}

		JasperPrintLP jasperPrint = null;
		try {
			// es gilt das Locale des Benutzers

			// die Daten fuer den Report ueber Hibernate holen
			ReportRahmenreservierungDto[] aReportRahmenreservierungDto = getReportRahmenreservierung(
					kritDtoI, theClientDto);

			// Erstellung des Report
			index = -1;
			sAktuellerReport = ArtikelReportFac.REPORT_RAHMENRESERVIERUNG;

			int iAnzahlZeilen = aReportRahmenreservierungDto.length; // Anzahl
			// der
			// Zeilen
			// in
			// der
			// Gruppe
			int iAnzahlSpalten = 7; // Anzahl der Spalten in der Gruppe

			data = new Object[iAnzahlZeilen][iAnzahlSpalten];

			// die Datenmatrix befuellen
			for (int i = 0; i < aReportRahmenreservierungDto.length; i++) {
				ReportRahmenreservierungDto reportRahmenreservierungDto = (ReportRahmenreservierungDto) aReportRahmenreservierungDto[i];

				data[i][REPORT_RAHMENRESERVIERUNG_AUFTRAGCNR] = reportRahmenreservierungDto
						.getAuftragCNr();
				data[i][REPORT_RAHMENRESERVIERUNG_KUNDECNAME1] = reportRahmenreservierungDto
						.getCKundenname();
				data[i][REPORT_RAHMENRESERVIERUNG_AUFTRAGCBEZ] = reportRahmenreservierungDto
						.getCBez();
				data[i][REPORT_RAHMENRESERVIERUNG_AUFTRAGPOSITIONRAHMENTERMIN] = reportRahmenreservierungDto
						.getTUebersteuerterLiefertermin();
				data[i][REPORT_RAHMENRESERVIERUNG_AUFTRAGPOSITIONMENGE] = reportRahmenreservierungDto
						.getNMenge();
				data[i][REPORT_RAHMENRESERVIERUNG_AUFTRAGPOSITIONOFFENEMENGE] = reportRahmenreservierungDto
						.getNOffeneMenge();
				data[i][REPORT_RAHMENRESERVIERUNG_AUFTRAGPOSITIONGELIEFERT] = reportRahmenreservierungDto
						.getNGelieferteMenge();
			}

			// die Parameter dem Report uebergeben
			HashMap<String, Object> parameter = new HashMap<String, Object>();

			parameter.put(
					"P_FILTER",
					formatRahmenreservierungKriterien(kritDtoI,
							theClientDto.getLocUi(), theClientDto));
			if (kritDtoI.getArtikelIId() != null) {
				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKeySmall(kritDtoI.getArtikelIId(),
								theClientDto);

				parameter.put("P_ARTIKELNUMMER", artikelDto.getCNr());
				parameter.put("P_ARTIKELBEZEICHNUNG",
						artikelDto.formatBezeichnung());
			}

			initJRDS(parameter, ArtikelReportFac.REPORT_MODUL,
					ArtikelReportFac.REPORT_RAHMENRESERVIERUNG,
					theClientDto.getMandant(), theClientDto.getLocUi(),
					theClientDto);

			jasperPrint = getReportPrint();
		} catch (Exception t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, t);
		}

		return jasperPrint;
	}

	/**
	 * String zum Andrucken der Filterkriterien.
	 * 
	 * @param kritDtoI
	 *            die Kriterien
	 * @param localeI
	 *            das bei der Formatierung gewuenschte Locale
	 * @param cNrUserI
	 *            der aktuelle Benutzer
	 * @return String die Filterkriterien
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	private String formatRahmenreservierungKriterien(
			ReportAnfragestatistikKriterienDto kritDtoI, Locale localeI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		StringBuffer buff = new StringBuffer("");

		// Belegdatum
		if (kritDtoI.getDVon() != null || kritDtoI.getDBis() != null) {
			buff.append("\n")
					.append(getTextRespectUISpr("lp.rahmentermin",
							theClientDto.getMandant(), theClientDto.getLocUi()));
		}

		if (kritDtoI.getDVon() != null) {
			buff.append(" ").append(
					getTextRespectUISpr("lp.von", theClientDto.getMandant(),
							theClientDto.getLocUi()));
			buff.append(" ").append(
					Helper.formatDatum(kritDtoI.getDVon(), localeI));
		}

		if (kritDtoI.getDBis() != null) {
			buff.append(" ").append(
					getTextRespectUISpr("lp.bis", theClientDto.getMandant(),
							theClientDto.getLocUi()));
			buff.append(" ").append(
					Helper.formatDatum(kritDtoI.getDBis(), localeI));
		}

		String cBuffer = buff.toString().trim();

		return cBuffer;
	}

	/**
	 * Diese Methode liefert eine Liste von allen Auftraegen zu einem bestimmten
	 * Artikel, die nach den eingegebenen Kriterien des Benutzers
	 * zusammengestellt wird. <br>
	 * Achtung: Hibernate verwendet lazy initialization, d.h. der Zugriff auf
	 * Collections muss innerhalb der Session erfolgen.
	 * 
	 * @param kritDtoI
	 *            die Kriterien des Benutzers
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return ReportRahmenreservierungDto[] die Liste der Auftraege
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public ReportRahmenreservierungDto[] getReportRahmenreservierung(
			ReportAnfragestatistikKriterienDto kritDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		ReportRahmenreservierungDto[] aResult = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;

		try {

			session = factory.openSession();

			// Hiberante Criteria fuer alle Tabellen ausgehend von meiner
			// Haupttabelle anlegen,
			// nach denen ich filtern und sortieren kann
			Criteria critAuftragposition = session
					.createCriteria(FLRAuftragpositionReport.class);

			// flrauftragpositionreport > flrauftrag
			Criteria critAuftrag = critAuftragposition
					.createCriteria(AuftragpositionFac.FLR_AUFTRAGPOSITION_FLRAUFTRAG);

			// Einschraenken auf Rahmenauftraege des Mandanten
			critAuftrag.add(Restrictions.eq(
					AuftragFac.FLR_AUFTRAG_MANDANT_C_NR,
					theClientDto.getMandant()));
			critAuftrag.add(Restrictions.eq(
					AuftragFac.FLR_AUFTRAG_AUFTRAGART_C_NR,
					AuftragServiceFac.AUFTRAGART_RAHMEN));

			// keine stornierten oder erledigten Auftraege.
			Collection<String> cStati = new LinkedList<String>();
			cStati.add(AuftragServiceFac.AUFTRAGSTATUS_STORNIERT);
			cStati.add(AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT);
			critAuftrag.add(Restrictions.not(Restrictions.in(
					AuftragFac.FLR_AUFTRAG_AUFTRAGSTATUS_C_NR, cStati)));

			// Einschraenkung auf den gewaehlten Artikel
			if (kritDtoI.getArtikelIId() != null) {
				critAuftragposition.add(Restrictions.eq(
						AuftragpositionFac.FLR_AUFTRAGPOSITION_ARTIKEL_I_ID,
						kritDtoI.getArtikelIId()));
			}
			/*
			 * // die offene Menge muss != null > 0 sein
			 * critAuftragposition.add(
			 * Restrictions.isNotNull(AuftragpositionFac.
			 * FLR_AUFTRAGPOSITION_N_MENGE));
			 * critAuftragposition.add(Restrictions.gt(AuftragpositionFac.
			 * FLR_AUFTRAGPOSITION_N_OFFENEMENGE, new BigDecimal(0)));
			 */
			// Einschraenkung nach Rahmentermin von - bis
			if (kritDtoI.getDVon() != null) {
				critAuftragposition
						.add(Restrictions
								.ge(AuftragpositionFac.FLR_AUFTRAGPOSITION_T_UEBERSTEUERTERLIEFERTERMIN,
										kritDtoI.getDVon()));
			}

			if (kritDtoI.getDBis() != null) {
				critAuftragposition
						.add(Restrictions
								.le(AuftragpositionFac.FLR_AUFTRAGPOSITION_T_UEBERSTEUERTERLIEFERTERMIN,
										kritDtoI.getDBis()));
			}

			// es wird nach Belegnummer und Artikel sortiert
			critAuftrag.addOrder(Order.asc(AuftragFac.FLR_AUFTRAG_C_NR));

			List<?> list = critAuftrag.list();
			aResult = new ReportRahmenreservierungDto[list.size()];
			Iterator<?> it = list.iterator();
			int iIndex = 0;

			while (it.hasNext()) {
				FLRAuftragpositionReport flrauftragposition = (FLRAuftragpositionReport) it
						.next();
				FLRAuftragReport flrauftrag = flrauftragposition
						.getFlrauftrag();
				FLRPartner flrpartner = flrauftrag.getFlrkunde()
						.getFlrpartner();

				ReportRahmenreservierungDto reportDto = new ReportRahmenreservierungDto();

				reportDto.setAuftragCNr(flrauftrag.getC_nr());
				reportDto.setAuftragIId(flrauftrag.getI_id());
				reportDto.setArtikelIId(flrauftragposition.getArtikel_i_id());
				reportDto.setCBez(flrauftrag.getC_bez());
				reportDto.setCKundenname(flrpartner
						.getC_name1nachnamefirmazeile1());
				reportDto.setTUebersteuerterLiefertermin(new Timestamp(
						flrauftragposition.getT_uebersteuerterliefertermin()
								.getTime()));
				reportDto.setNMenge(flrauftragposition.getN_menge());
				// offene (rahmenmenge-abgerufenemenge)
				reportDto.setNOffeneMenge(flrauftragposition
						.getN_offenerahmenmenge());
				// geliefert

				if (flrauftragposition.getN_offenerahmenmenge() != null) {
					reportDto
							.setNGelieferteMenge(flrauftragposition
									.getN_menge().subtract(
											flrauftragposition
													.getN_offenerahmenmenge()));
				} else {
					reportDto.setNGelieferteMenge(flrauftragposition
							.getN_menge());
				}

				aResult[iIndex] = reportDto;
				iIndex++;
			}
		} finally {
			try {
				session.close();
			} catch (HibernateException he) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HIBERNATE, he);
			}
		}
		return aResult;
	}

}
