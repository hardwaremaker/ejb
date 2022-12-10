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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

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
import com.lp.server.artikel.ejb.Geraetesnr;
import com.lp.server.artikel.ejb.Handlagerbewegung;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelbestellt;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikellager;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikellagerplaetze;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikellieferant;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikellieferantstaffel;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikellistespr;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikellog;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelreservierung;
import com.lp.server.artikel.fastlanereader.generated.FLRFehlmenge;
import com.lp.server.artikel.fastlanereader.generated.FLRGeometrie;
import com.lp.server.artikel.fastlanereader.generated.FLRHersteller;
import com.lp.server.artikel.fastlanereader.generated.FLRInventurstand;
import com.lp.server.artikel.fastlanereader.generated.FLRLager;
import com.lp.server.artikel.fastlanereader.generated.FLRLagerbewegung;
import com.lp.server.artikel.fastlanereader.generated.FLRLagercockpitumbuchung;
import com.lp.server.artikel.fastlanereader.generated.FLRLagerplatz;
import com.lp.server.artikel.fastlanereader.generated.FLRMaterial;
import com.lp.server.artikel.fastlanereader.generated.FLRMaterialspr;
import com.lp.server.artikel.fastlanereader.generated.FLRVerschleissteilwerkzeug;
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
import com.lp.server.artikel.service.FasessionDto;
import com.lp.server.artikel.service.HerstellerDto;
import com.lp.server.artikel.service.InventurlisteDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.LagerFac;
import com.lp.server.artikel.service.LagerbewegungDto;
import com.lp.server.artikel.service.LagerplatzDto;
import com.lp.server.artikel.service.MaterialDto;
import com.lp.server.artikel.service.RahmenbestelltReportDto;
import com.lp.server.artikel.service.ReportRahmenreservierungDto;
import com.lp.server.artikel.service.ReservierungFac;
import com.lp.server.artikel.service.SeriennrChargennrAufLagerDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.artikel.service.ShopgruppeDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VerpackungsmittelDto;
import com.lp.server.artikel.service.VerschleissteilDto;
import com.lp.server.artikel.service.VkPreisfindungEinzelverkaufspreisDto;
import com.lp.server.artikel.service.VkPreisfindungPreislisteDto;
import com.lp.server.artikel.service.VkpfMengenstaffelDto;
import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.artikel.service.WerkzeugDto;
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
import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.bestellung.service.WareneingangspositionDto;
import com.lp.server.fertigung.fastlanereader.generated.FLRLos;
import com.lp.server.fertigung.fastlanereader.generated.FLRLosReport;
import com.lp.server.fertigung.fastlanereader.generated.FLRLosablieferung;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.FertigungReportFac;
import com.lp.server.fertigung.service.GesamtkalkulationDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LosistmaterialDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.forecast.service.ForecastFac;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.partner.fastlanereader.generated.FLRKundesokomengenstaffel;
import com.lp.server.partner.fastlanereader.generated.FLRPartner;
import com.lp.server.partner.service.CustomerPricelistItemDto;
import com.lp.server.partner.service.CustomerPricelistPriceDto;
import com.lp.server.partner.service.CustomerPricelistReportDto;
import com.lp.server.partner.service.CustomerPricelistShopgroupDto;
import com.lp.server.partner.service.IdValueDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeReportFac;
import com.lp.server.partner.service.KundesokoDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.reklamation.fastlanereader.generated.FLRReklamation;
import com.lp.server.reklamation.service.ReklamationFac;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStueckliste;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklistearbeitsplan;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklisteposition;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteReportFac;
import com.lp.server.stueckliste.service.StuecklisteeigenschaftDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.ejbfac.EJBExcFactory;
import com.lp.server.system.ejbfac.HvCreatingCachingProvider;
import com.lp.server.system.ejbfac.HvPrinter;
import com.lp.server.system.fastlanereader.generated.FLRLandplzort;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeArtikeletikett;
import com.lp.server.system.jcr.service.docnode.DocNodeBase;
import com.lp.server.system.jcr.service.docnode.DocNodeFile;
import com.lp.server.system.jcr.service.docnode.DocNodeFolder;
import com.lp.server.system.jcr.service.docnode.DocNodeLiteral;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.jcr.service.docnode.HeliumDocPath;
import com.lp.server.system.service.GeaenderteChargennummernDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LandDto;
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
import com.lp.server.system.service.WechselkursDto;
import com.lp.server.system.service.ZusatzfunktionberechtigungDto;
import com.lp.server.util.HelperServer;
import com.lp.server.util.LPReport;
import com.lp.server.util.Validator;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.server.util.report.TimingInterceptor;
import com.lp.util.AufgeloesteFehlmengenDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.LPDatenSubreport;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

@Stateless
@Interceptors(TimingInterceptor.class)
public class ArtikelReportFacBean extends LPReport implements ArtikelReportFac, JRDataSource {

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
	private static int REPORT_ARTIKELETIKETT_SUBREPORT_LAGERZUBUCHUNGEN = 33;
	private static int REPORT_ARTIKELETIKETT_LAGERSTAND_CHARGE = 34;
	private static int REPORT_ARTIKELETIKETT_LAGER_LAGERSTAND_CHARGE = 35;
	private static int REPORT_ARTIKELETIKETT_VERPACKUNGSMITTEL_KENNUNG = 36;
	private static int REPORT_ARTIKELETIKETT_VERPACKUNGSMITTEL_BEZEICHNUNG = 37;
	private static int REPORT_ARTIKELETIKETT_VERPACKUNGSMITTEL_GEWICHT_IN_KG = 38;
	private static int REPORT_ARTIKELETIKETT_VERPACKUNGSMITTELMENGE = 39;
	private static int REPORT_ARTIKELETIKETT_URSPRUNGSLAND = 40;
	private static int REPORT_ARTIKELETIKETT_LFDNR = 41;
	private static int REPORT_ARTIKELETIKETT_EXEMPLAR = 42;
	private static int REPORT_ARTIKELETIKETT_EXEMPLAREGESAMT = 43;
	private static int REPORT_ARTIKELETIKETT_ANZAHL_SPALTEN = 44;

	private static int REPORT_FREIINFERTIGUNG_LOSNR = 0;
	private static int REPORT_FREIINFERTIGUNG_FREIEMENGE = 1;
	private static int REPORT_FREIINFERTIGUNG_AUFTRAG = 2;
	private static int REPORT_FREIINFERTIGUNG_PROJEKT = 3;
	private static int REPORT_FREIINFERTIGUNG_BEGINN = 4;
	private static int REPORT_FREIINFERTIGUNG_ENDE = 5;
	private static int REPORT_FREIINFERTIGUNG_FEHLMENGEN = 6;
	private static int REPORT_FREIINFERTIGUNG_ANZAHL_SPALTEN = 7;

	private static int REPORT_LIEFERANTENPREIS_LIEFERANT = 0;
	private static int REPORT_LIEFERANTENPREIS_MENGE = 1;
	private static int REPORT_LIEFERANTENPREIS_NETTOPREIS = 2;
	private static int REPORT_LIEFERANTENPREIS_MINDESTBESTELLMENGE = 3;
	private static int REPORT_LIEFERANTENPREIS_WIEDERBESCHAFFUNGSZEIT = 4;
	private static int REPORT_LIEFERANTENPREIS_FIXKOSTEN = 5;
	private static int REPORT_LIEFERANTENPREIS_GUELTIGAB = 6;
	private static int REPORT_LIEFERANTENPREIS_ANZAHL_SPALTEN = 7;

	private static int REPORT_LIEFERANTENPREISVERGLEICH_LIEFERANT = 0;
	private static int REPORT_LIEFERANTENPREISVERGLEICH_LIEFERANT_KBEZ = 1;
	private static int REPORT_LIEFERANTENPREISVERGLEICH_NICHT_LIEFERBAR = 2;
	private static int REPORT_LIEFERANTENPREISVERGLEICH_SUBREPORT = 3;
	private static int REPORT_LIEFERANTENPREISVERGLEICH_ANZAHL_SPALTEN = 4;

	private static int REPORT_MAKE_OR_BUY_ARTIKEL = 0;
	private static int REPORT_MAKE_OR_BUY_BEZEICHNUNG = 1;
	private static int REPORT_MAKE_OR_BUY_ZUSATZBEZEICHNUNG = 2;
	private static int REPORT_MAKE_OR_BUY_SUBREPORT_ABLIEFERUNGEN = 3;
	private static int REPORT_MAKE_OR_BUY_SUBREPORT_LIEFERANTENPREISE = 4;
	private static int REPORT_MAKE_OR_BUY_ARTIKEL_EINHEIT = 5;
	private static int REPORT_MAKE_OR_BUY_ANZAHL_SPALTEN = 6;

	private static int REPORT_FEHLMENGENLISTE_LOS = 0;
	private static int REPORT_FEHLMENGENLISTE_PROJEKTNAME = 1;
	private static int REPORT_FEHLMENGENLISTE_LIEFERTERMIN = 2;
	private static int REPORT_FEHLMENGENLISTE_MENGE = 3;
	private static int REPORT_FEHLMENGENLISTE_KUNDE = 4;
	private static int REPORT_FEHLMENGENLISTE_STUECKLISTE_NUMMER = 5;
	private static int REPORT_FEHLMENGENLISTE_STUECKLISTE_BEZEICHNUNG = 6;
	private static int REPORT_FEHLMENGENLISTE_LOSBEGINN = 7;
	private static int REPORT_FEHLMENGENLISTE_LOSENDE = 8;
	private static int REPORT_FEHLMENGENLISTE_ZUGEHOERIGER_KUNDE_IM_LOS = 9;
	private static int REPORT_FEHLMENGENLISTE_ABLIEFERTERMIN = 10;

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

	private static int REPORT_ARTIKELSTAMMBLATT_VERPACKUNGSMITTEL_KENNUNG = 26;
	private static int REPORT_ARTIKELSTAMMBLATT_VERPACKUNGSMITTEL_BEZEICHNUNG = 27;
	private static int REPORT_ARTIKELSTAMMBLATT_VERPACKUNGSMITTEL_GEWICHT_IN_KG = 28;
	private static int REPORT_ARTIKELSTAMMBLATT_VERPACKUNGSMITTELMENGE = 29;
	private static int REPORT_ARTIKELSTAMMBLATT_EINHEIT = 30;
	private static int REPORT_ARTIKELSTAMMBLATT_BESTELLMENGENEINHEIT = 31;
	private static int REPORT_ARTIKELSTAMMBLATT_UMRECHNUNGSFAKTOR = 32;
	private static int REPORT_ARTIKELSTAMMBLATT_REFERENZNUMMER = 34;

	private static int REPORT_ARTIKELSTAMMBLATT_ZUSATZBEZEICHNUNG2 = 35;
	private static int REPORT_ARTIKELSTAMMBLATT_ZUGEHOERIGER_ARTIKELNUMMER = 36;
	private static int REPORT_ARTIKELSTAMMBLATT_ZUGEHOERIGER_BEZEICHNUNG = 37;
	private static int REPORT_ARTIKELSTAMMBLATT_ZUGEHOERIGER_KURZBEZEICHNUNG = 38;
	private static int REPORT_ARTIKELSTAMMBLATT_ZUGEHOERIGER_ZUSATZBEZEICHNUNG = 39;
	private static int REPORT_ARTIKELSTAMMBLATT_ZUGEHOERIGER_ZUSATZBEZEICHNUNG2 = 40;
	private static int REPORT_ARTIKELSTAMMBLATT_LAGERBEWIRTSCHAFTET = 41;
	private static int REPORT_ARTIKELSTAMMBLATT_ANZAHL_SPALTEN = 42;

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
	private static int REPORT_VERWENDUNGSNACHWEIS_EBENE = 18;
	private static int REPORT_VERWENDUNGSNACHWEIS_MANDANT = 19;
	private static int REPORT_VERWENDUNGSNACHWEIS_ZIELMENGE = 20;
	private static int REPORT_VERWENDUNGSNACHWEIS_LAGERMINDEST = 21;
	private static int REPORT_VERWENDUNGSNACHWEIS_LAGERSOLL = 22;
	private static int REPORT_VERWENDUNGSNACHWEIS_FERTIGUNGSSATZGROESSE = 23;
	private static int REPORT_VERWENDUNGSNACHWEIS_MIT_FORMELN = 24;
	private static int REPORT_VERWENDUNGSNACHWEIS_FEHLER_EINHEITENKONVERTIERUNG = 25;
	private static int REPORT_VERWENDUNGSNACHWEIS_STUECKLISTEPOSITION_I_ID = 26;
	private static int REPORT_VERWENDUNGSNACHWEIS_ANZAHL_SPALTEN = 27;

	private static int REPORT_ARTIKEL_OHNE_STKL_VERWENDUNG_ARTIKELNUMMER = 0;
	private static int REPORT_ARTIKEL_OHNE_STKL_VERWENDUNG_BEZEICHNUNG = 1;
	private static int REPORT_ARTIKEL_OHNE_STKL_VERWENDUNG_KURZBEZEICHNUNG = 2;
	private static int REPORT_ARTIKEL_OHNE_STKL_VERWENDUNG_ZUSATZBEZEICHNUNG = 3;
	private static int REPORT_ARTIKEL_OHNE_STKL_VERWENDUNG_LAGERSTAND = 4;
	private static int REPORT_ARTIKEL_OHNE_STKL_VERWENDUNG_FEHLMENGEN = 5;
	private static int REPORT_ARTIKEL_OHNE_STKL_VERWENDUNG_RESERVIERT = 6;
	private static int REPORT_ARTIKEL_OHNE_STKL_VERWENDUNG_BESTELLT = 7;
	private static int REPORT_ARTIKEL_OHNE_STKL_VERWENDUNG_ARTIKELREFERENZNUMMER = 8;
	private static int REPORT_ARTIKEL_OHNE_STKL_VERWENDUNG_ANZAHL_SPALTEN = 9;

	public final static int REPORT_AUFTRAGSWERTE_AUFTRAG = 0;
	public final static int REPORT_AUFTRAGSWERTE_LOS = 1;
	public final static int REPORT_AUFTRAGSWERTE_ARTIKELNUMMER = 2;
	public final static int REPORT_AUFTRAGSWERTE_BEZEICHNUNG = 3;
	public final static int REPORT_AUFTRAGSWERTE_LAGERSTAND = 4;
	public final static int REPORT_AUFTRAGSWERTE_EINHEIT = 5;
	public final static int REPORT_AUFTRAGSWERTE_LIEF1PREIS = 6;
	public final static int REPORT_AUFTRAGSWERTE_GESTEHUNGSPREIS = 7;
	public final static int REPORT_AUFTRAGSWERTE_KUNDE = 8;
	public final static int REPORT_AUFTRAGSWERTE_RESERVIERT = 9;
	public final static int REPORT_AUFTRAGSWERTE_FEHLMENGE = 10;
	public final static int REPORT_AUFTRAGSWERTE_LAGERSTANDSVERBRAUCH = 11;
	public final static int REPORT_AUFTRAGSWERTE_LOS_BEGINNTERMIN = 12;
	public final static int REPORT_AUFTRAGSWERTE_ARTIKELREFERENZNUMMER = 13;
	public final static int REPORT_AUFTRAGSWERTE_ANZAHL_SPALTEN = 14;

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
	private static int REPORT_KUNDENSOKOS_KUNDE_BERECHNETER_PREIS_KDWAEHRUNG = 17;
	private static int REPORT_KUNDENSOKOS_KUNDE_KDWAEHRUNG = 18;
	private static int REPORT_KUNDENSOKOS_KUNDE_WIRKT_NICHT_IN_PREISFINDUNG = 19;
	private static int REPORT_KUNDENSOKOS_KUNDE_BERECHNETER_PREIS_ZU_HEUTE = 20;
	private static int REPORT_KUNDENSOKOS_ANZAHL_FELDER = 21;

	private static int REPORT_KUNDENSONDERKONDITIONEN_ARTIKELNUMMER = 0;
	private static int REPORT_KUNDENSONDERKONDITIONEN_ARTIKELBEZEICHNUNG = 1;
	private static int REPORT_KUNDENSONDERKONDITIONEN_KUNDE_NAME1 = 2;
	private static int REPORT_KUNDENSONDERKONDITIONEN_KUNDE_NAME2 = 3;
	private static int REPORT_KUNDENSONDERKONDITIONEN_KUNDE_LKZ = 4;
	private static int REPORT_KUNDENSONDERKONDITIONEN_KUNDE_PLZ = 5;
	private static int REPORT_KUNDENSONDERKONDITIONEN_KUNDE_ORT = 6;
	private static int REPORT_KUNDENSONDERKONDITIONEN_KUNDE_KDARTIKELNUMMER = 7;
	private static int REPORT_KUNDENSONDERKONDITIONEN_KUNDE_KDARTIKELBEZ = 8;
	private static int REPORT_KUNDENSONDERKONDITIONEN_KUNDE_KDARTIKELBEZ2 = 9;
	private static int REPORT_KUNDENSONDERKONDITIONEN_KUNDE_GUELTIAGAB = 10;
	private static int REPORT_KUNDENSONDERKONDITIONEN_KUNDE_GUELTIAGBIS = 11;
	private static int REPORT_KUNDENSONDERKONDITIONEN_KUNDE_MENGE = 12;
	private static int REPORT_KUNDENSONDERKONDITIONEN_KUNDE_FIXPREIS = 13;
	private static int REPORT_KUNDENSONDERKONDITIONEN_KUNDE_RABATT = 14;
	private static int REPORT_KUNDENSONDERKONDITIONEN_KUNDE_BERECHNETER_PREIS = 15;
	private static int REPORT_KUNDENSONDERKONDITIONEN_KUNDE_NAECHSTE_VKPREISBASIS_GUELTIG_AB = 16;
	private static int REPORT_KUNDENSONDERKONDITIONEN_KUNDE_KBEZ = 17;
	private static int REPORT_KUNDENSONDERKONDITIONEN_KUNDE_UID = 18;
	private static int REPORT_KUNDENSONDERKONDITIONEN_KUNDE_BERECHNETER_PREIS_KDWAEHRUNG = 19;
	private static int REPORT_KUNDENSONDERKONDITIONEN_KUNDE_KDWAEHRUNG = 20;
	private static int REPORT_KUNDENSONDERKONDITIONEN_KUNDE_WIRKT_NICHT_IN_PREISFINDUNG = 21;
	private static int REPORT_KUNDENSONDERKONDITIONEN_KUNDE_BERECHNETER_PREIS_ZU_HEUTE = 22;
	private static int REPORT_KUNDENSONDERKONDITIONEN_KUNDE_MANDANT = 23;
	private static int REPORT_KUNDENSONDERKONDITIONEN_ANZAHL_FELDER = 24;

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
	private static int REPORT_BEWEGUNGSVORSCHAU_STANDORT = 14;
	private static int REPORT_BEWEGUNGSVORSCHAU_LAGER_VERSTECKT = 16;
	private static int REPORT_BEWEGUNGSVORSCHAU_AUFTRAGSFREIGABE_PERSON = 17;
	private static int REPORT_BEWEGUNGSVORSCHAU_AUFTRAGSFREIGABE_ZEITPUNKT = 18;
	private static int REPORT_BEWEGUNGSVORSCHAU_ABLIEFERTERMIN = 19;
	private static int REPORT_BEWEGUNGSVORSCHAU_FORECASTART = 20;
	private static int REPORT_BEWEGUNGSVORSCHAU_MENGE_UNTERWEGS = 21;
	private static int REPORT_BEWEGUNGSVORSCHAU_ZUGEHOERIGER_KUNDE_IM_LOS = 22;
	private static int REPORT_BEWEGUNGSVORSCHAU_FORECAST_BEMERKUNG = 23;
	private static int REPORT_BEWEGUNGSVORSCHAU_FORECASTPOSITION_I_ID = 24;
	private static int REPORT_BEWEGUNGSVORSCHAU_LOS_STUECKLISTENUMMER = 25;
	private static int REPORT_BEWEGUNGSVORSCHAU_LOS_STUECKLISTE_I_ID = 26;
	private static int REPORT_BEWEGUNGSVORSCHAU_LOS_STUECKLISTEBEZEICHNUNG = 27;
	private static int REPORT_BEWEGUNGSVORSCHAU_LOS_STUECKLISTEZUSATZBEZEICHNUNG = 28;
	private static int REPORT_BEWEGUNGSVORSCHAU_LOS_STUECKLISTEZUSATZBEZEICHNUNG2 = 29;
	private static int REPORT_BEWEGUNGSVORSCHAU_ANZAHL_FELDER = 30;

	private static int REPORT_BEWEGUNGSVORSCHAU_ALLE_ARTIKELNUMMER = 0;
	private static int REPORT_BEWEGUNGSVORSCHAU_ALLE_ARTIKELBEZEICHNUNG = 1;
	private static int REPORT_BEWEGUNGSVORSCHAU_ALLE_ARTIKELZUSATZBEZEICHNUNG = 2;
	private static int REPORT_BEWEGUNGSVORSCHAU_ALLE_ARTIKELZUSATZBEZEICHNUNG2 = 3;
	private static int REPORT_BEWEGUNGSVORSCHAU_ALLE_SUBREPORT_BEWEGUNGSVORSCHAU = 4;
	private static int REPORT_BEWEGUNGSVORSCHAU_ALLE_SUBREPORT_BEWEGUNGSVORSCHAU_PARAMETER = 5;
	private static int REPORT_BEWEGUNGSVORSCHAU_ALLE_ANZAHL_FELDER = 6;

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
	private static final int REPORT_AUFGELOESTEFEHLMENGEN_ARTIKELNUMMER_OFFENER_AG = 20;
	private static final int REPORT_AUFGELOESTEFEHLMENGEN_ARTIKELBEZEICHNUNG_OFFENER_AG = 21;
	private static final int REPORT_AUFGELOESTEFEHLMENGEN_KURZBEZEICHNUNG = 22;
	private static final int REPORT_AUFGELOESTEFEHLMENGEN_REFERENZNUMMER = 23;
	private static final int REPORT_AUFGELOESTEFEHLMENGEN_LAGERSTAND = 24;

	private static final int REPORT_AUFGELOESTEFEHLMENGEN_ANLAGEZEITPUNKT = 25;
	private static final int REPORT_AUFGELOESTEFEHLMENGEN_KURZZEICHEN_PERSONAL_ANLEGEN = 26;

	private static final int REPORT_AUFGELOESTEFEHLMENGEN_ANZAHL_SPALTEN = 27;

	private static int REPORT_VKPREISLISTE_ARTIKELNUMMER = 0;
	private static int REPORT_VKPREISLISTE_BEZEICHNUNG = 1;
	private static int REPORT_VKPREISLISTE_KURZBEZEICHNUNG = 2;
	private static int REPORT_VKPREISLISTE_ZUSATZBEZEICHNUNG = 3;
	private static int REPORT_VKPREISLISTE_ZUSATZBEZEICHNUNG2 = 4;
	private static int REPORT_VKPREISLISTE_VKPREIS = 5;
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
	private static int REPORT_VKPREISLISTE_MATERIALZUSCHLAGEK = 23;
	private static int REPORT_VKPREISLISTE_MATERIAL = 24;
	private static int REPORT_VKPREISLISTE_LOCALE = 25;
	private static int REPORT_VKPREISLISTE_MWSTSATZBEZ = 26;
	private static int REPORT_VKPREISLISTE_VERKAUFSEAN = 27;
	private static int REPORT_VKPREISLISTE_MWSTSATZ = 28;
	private static int REPORT_VKPREISLISTE_ARTIKELREFERENZNUMMER = 29;
	private static int REPORT_VKPREISLISTE_ARTIKELIID = 30;
	private static int REPORT_VKPREISLISTE_MWSTSATZID = 31;
	private static int REPORT_VKPREISLISTE_SHOPGRUPPEID = 32;
	private static int REPORT_VKPREISLISTE_VERKAUFSPREISBASIS = 33;
	private static int REPORT_VKPREISLISTE_ANZAHL_SPALTEN = 34;

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
	private static int REPORT_BESTELLTLISTE_GEBINDENAME = 10;
	private static int REPORT_BESTELLTLISTE_ANZAHL_GEBINDE = 11;
	private static int REPORT_BESTELLTLISTE_ANZAHL_SPALTEN = 12;

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
	private static int REPORT_ARTIKELSTATISTIK_HISTORIE = 30;
	private static int REPORT_ARTIKELSTATISTIK_BEWEGUNGSVORSCHAU_MANDANT = 31;
	private static int REPORT_ARTIKELSTATISTIK_BEWEGUNGSVORSCHAU_AB_TERMIN_BESTELLUNG = 32;
	private static int REPORT_ARTIKELSTATISTIK_BEWEGUNGSVORSCHAU_ABNUMMER_BESTELLUNG = 33;
	private static int REPORT_ARTIKELSTATISTIK_ARTIKELNUMMER = 34;
	private static int REPORT_ARTIKELSTATISTIK_BEWEGUNGSVORSCHAU_AUFTRAGSFREIGABEZEITPUNKT = 35;
	private static int REPORT_ARTIKELSTATISTIK_ARTIKELSNRCHNR_I_ID = 36;
	private static int REPORT_ARTIKELSTATISTIK_MANDANT = 37;
	private static int REPORT_ARTIKELSTATISTIK_LS_VERRECHENBAR = 38;
	private static int REPORT_ARTIKELSTATISTIK_LS_LIEFERSCHEINART = 39;
	private static int REPORT_ARTIKELSTATISTIK_ANZAHL_SPALTEN = 40;

	private static int REPORT_MONATSSTATISTIK_MONAT = 0;
	private static int REPORT_MONATSSTATISTIK_JAHR = 1;
	private static int REPORT_MONATSSTATISTIK_ZUGANG_MENGE = 2;
	private static int REPORT_MONATSSTATISTIK_ZUGANG_WERT = 3;
	private static int REPORT_MONATSSTATISTIK_ABGANG_MENGE = 4;
	private static int REPORT_MONATSSTATISTIK_ABGANG_WERT = 5;

	private static int REPORT_LAGERPLATZ_LAGERPLATZ = 0;
	private static int REPORT_LAGERPLATZ_LAGER = 1;
	private static int REPORT_LAGERPLATZ_ANZAHL_SPALTEN = 2;

	private static int REPORT_VERSCHLEISSTEILWERKZEUG_VERSCHLEISSTEIL = 0;
	private static int REPORT_VERSCHLEISSTEILWERKZEUG_VERSCHLEISSTEIL_BEZEICHNUNG = 1;
	private static int REPORT_VERSCHLEISSTEILWERKZEUG_VERSCHLEISSTEIL_BEZEICHNUNG2 = 2;
	private static int REPORT_VERSCHLEISSTEILWERKZEUG_WERKZEUG = 3;
	private static int REPORT_VERSCHLEISSTEILWERKZEUG_WERKZEUG_BEZEICHNUNG = 4;
	private static int REPORT_VERSCHLEISSTEILWERKZEUG_WERKZEUG_MANDANT = 5;
	private static int REPORT_VERSCHLEISSTEILWERKZEUG_ANZAHL_SPALTEN = 6;

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
	private static int REPORT_VKPREISENTWICKLUNG_BEMERKUNG = 12;
	private static int REPORT_VKPREISENTWICKLUNG_ANZAHL_SPALTEN = 13;

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
	private static int REPORT_MINDESTLAGERSTAENDE_LAGER = 14;
	private static int REPORT_MINDESTLAGERSTAENDE_ARTIKELREFERENZNUMMER = 15;
	private static int REPORT_MINDESTLAGERSTAENDE_ANZAHL_SPALTEN = 16;

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

	private static int REPORT_GEAENDERTE_CHARGEN_ARTIKELNUMMER = 0;
	private static int REPORT_GEAENDERTE_CHARGEN_BEZEICHNUNG = 1;
	private static int REPORT_GEAENDERTE_CHARGEN_ZUSATZBEZEICHNUNG = 2;
	private static int REPORT_GEAENDERTE_CHARGEN_BESTELLUNG = 3;
	private static int REPORT_GEAENDERTE_CHARGEN_WARENEINGANG = 4;
	private static int REPORT_GEAENDERTE_CHARGEN_CHNR_ALT = 5;
	private static int REPORT_GEAENDERTE_CHARGEN_CHNR_NEU = 6;
	private static int REPORT_GEAENDERTE_CHARGEN_ANZAHL_SPALTEN = 7;

	private static int REPORT_ALLERGENE_ARTIKELNUMMER = 0;
	private static int REPORT_ALLERGENE_BEZEICHNUNG = 1;
	private static int REPORT_ALLERGENE_SUBREPORT_ENTHALTENE_ALLERGENE = 2;

	private static int REPORT_ALLERGENE_ANZAHL_SPALTEN = 3;

	private static int REPORT_SNRSTAMMBLATT_ARTIKELNUMMER = 0;
	private static int REPORT_SNRSTAMMBLATT_ARTIKELBEZEICHNUNG = 1;
	private static int REPORT_SNRSTAMMBLATT_SERIENNUMER = 2;
	private static int REPORT_SNRSTAMMBLATT_MENGE = 3;
	private static int REPORT_SNRSTAMMBLATT_ZUGANG = 4;
	private static int REPORT_SNRSTAMMBLATT_ABGANG = 5;
	private static int REPORT_SNRSTAMMBLATT_ZEITPUNKT = 6;
	private static int REPORT_SNRSTAMMBLATT_LAGER = 7;
	private static int REPORT_SNRSTAMMBLATT_SUBREPORT_GERAETESNR = 8;
	private static int REPORT_SNRSTAMMBLATT_SUBREPORT_LOS_GESAMTKALKULATION = 9;
	private static int REPORT_SNRSTAMMBLATT_VERSION = 10;
	private static int REPORT_SNRSTAMMBLATT_EINHEIT = 11;
	private static int REPORT_SNRSTAMMBLATT_ANZAHL_SPALTEN = 12;

	public Object getFieldValue(JRField jRField) throws JRException {
		Object value = null;
		String fieldName = jRField.getName();
		if (sAktuellerReport.equals(ArtikelReportFac.REPORT_ARTIKELSTATISTIK)) {
			if ("Belegart".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_BELEGART];
			} else if ("Belegnummer".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_BELEGNUMMER];
			} else if ("Mandant".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_MANDANT];
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
			} else if ("F_ARTIKELSNRCHNR_I_ID".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_ARTIKELSNRCHNR_I_ID];
			} else if ("F_HISTORIE".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_HISTORIE];
			} else if ("F_BEWEGUNGSVORSCHAU_MANDANT".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_BEWEGUNGSVORSCHAU_MANDANT];
			} else if ("F_BEWEGUNGSVORSCHAU_AB_TERMIN_BESTELLUNG".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_BEWEGUNGSVORSCHAU_AB_TERMIN_BESTELLUNG];
			} else if ("F_BEWEGUNGSVORSCHAU_AB_NUMMER_BESTELLUNG".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_BEWEGUNGSVORSCHAU_ABNUMMER_BESTELLUNG];
			} else if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_ARTIKELNUMMER];
			} else if ("F_BEWEGUNGSVORSCHAU_AUFTRAGSFREIGABEZEITPUNKT".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_BEWEGUNGSVORSCHAU_AUFTRAGSFREIGABEZEITPUNKT];
			} else if ("LieferscheinArt".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_LS_LIEFERSCHEINART];
			} else if ("LieferscheinVerrechenbar".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTATISTIK_LS_VERRECHENBAR];
			}

		} else if (sAktuellerReport.equals(ArtikelReportFac.REPORT_MONATSSTATISTIK)) {
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
		} else if (sAktuellerReport.equals(ArtikelReportFac.REPORT_GEAENDERTE_CHARGEN)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_GEAENDERTE_CHARGEN_ARTIKELNUMMER];
			} else if ("Bestellung".equals(fieldName)) {
				value = data[index][REPORT_GEAENDERTE_CHARGEN_BESTELLUNG];
			} else if ("ChargennummerAlt".equals(fieldName)) {
				value = data[index][REPORT_GEAENDERTE_CHARGEN_CHNR_ALT];
			} else if ("ChargennummerNeu".equals(fieldName)) {
				value = data[index][REPORT_GEAENDERTE_CHARGEN_CHNR_NEU];
			} else if ("Wareneingang".equals(fieldName)) {
				value = data[index][REPORT_GEAENDERTE_CHARGEN_WARENEINGANG];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_GEAENDERTE_CHARGEN_BEZEICHNUNG];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_GEAENDERTE_CHARGEN_ZUSATZBEZEICHNUNG];
			}
		} else if (sAktuellerReport.equals(ArtikelReportFac.REPORT_AUFTRAGSWERTE)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_AUFTRAGSWERTE_ARTIKELNUMMER];
			} else if ("Artikelreferenznummer".equals(fieldName)) {
				value = data[index][REPORT_AUFTRAGSWERTE_ARTIKELREFERENZNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_AUFTRAGSWERTE_BEZEICHNUNG];
			} else if ("Einheit".equals(fieldName)) {
				value = data[index][REPORT_AUFTRAGSWERTE_EINHEIT];
			} else if ("Lagerstand".equals(fieldName)) {
				value = data[index][REPORT_AUFTRAGSWERTE_LAGERSTAND];
			} else if ("Lief1Preis".equals(fieldName)) {
				value = data[index][REPORT_AUFTRAGSWERTE_LIEF1PREIS];
			} else if ("Gestehungspreis".equals(fieldName)) {
				value = data[index][REPORT_AUFTRAGSWERTE_GESTEHUNGSPREIS];
			} else if ("Los".equals(fieldName)) {
				value = data[index][REPORT_AUFTRAGSWERTE_LOS];
			} else if ("LosBeginntermin".equals(fieldName)) {
				value = data[index][REPORT_AUFTRAGSWERTE_LOS_BEGINNTERMIN];
			} else if ("Auftrag".equals(fieldName)) {
				value = data[index][REPORT_AUFTRAGSWERTE_AUFTRAG];
			} else if ("Kunde".equals(fieldName)) {
				value = data[index][REPORT_AUFTRAGSWERTE_KUNDE];
			} else if ("Reserviert".equals(fieldName)) {
				value = data[index][REPORT_AUFTRAGSWERTE_RESERVIERT];
			} else if ("Fehlmenge".equals(fieldName)) {
				value = data[index][REPORT_AUFTRAGSWERTE_FEHLMENGE];
			} else if ("Lagerstandsverbrauch".equals(fieldName)) {
				value = data[index][REPORT_AUFTRAGSWERTE_LAGERSTANDSVERBRAUCH];
			}

		} else if (sAktuellerReport.equals(ArtikelReportFac.REPORT_VERSCHLEISSTEILWERKZEUG)) {
			if ("Verschleissteil".equals(fieldName)) {
				value = data[index][REPORT_VERSCHLEISSTEILWERKZEUG_VERSCHLEISSTEIL];
			} else if ("VerschleissteilBezeichnung".equals(fieldName)) {
				value = data[index][REPORT_VERSCHLEISSTEILWERKZEUG_VERSCHLEISSTEIL_BEZEICHNUNG];
			} else if ("VerschleissteilBezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_VERSCHLEISSTEILWERKZEUG_VERSCHLEISSTEIL_BEZEICHNUNG2];
			} else if ("Werkzeug".equals(fieldName)) {
				value = data[index][REPORT_VERSCHLEISSTEILWERKZEUG_WERKZEUG];
			} else if ("WerkzeugBezeichnung".equals(fieldName)) {
				value = data[index][REPORT_VERSCHLEISSTEILWERKZEUG_WERKZEUG_BEZEICHNUNG];
			} else if ("WerkzeugMandant".equals(fieldName)) {
				value = data[index][REPORT_VERSCHLEISSTEILWERKZEUG_WERKZEUG_MANDANT];
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
			} else if ("BerechneterPreisZuHeute".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSOKOS_KUNDE_BERECHNETER_PREIS_ZU_HEUTE];
			} else if ("NaechsteVKPreisbasisGueltigab".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSOKOS_KUNDE_NAECHSTE_VKPREISBASIS_GUELTIG_AB];
			} else if ("KundeKbez".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSOKOS_KUNDE_KBEZ];
			} else if ("KundeUID".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSOKOS_KUNDE_UID];
			} else if ("BerechneterPreisKDWaehrung".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSOKOS_KUNDE_BERECHNETER_PREIS_KDWAEHRUNG];
			} else if ("KDWaehrung".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSOKOS_KUNDE_KDWAEHRUNG];
			} else if ("WirktNichtInPreisfindung".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSOKOS_KUNDE_WIRKT_NICHT_IN_PREISFINDUNG];
			}
		} else if (sAktuellerReport.equals(ArtikelReportFac.REPORT_KUNDENSONDERKONDITIONEN)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSONDERKONDITIONEN_ARTIKELNUMMER];
			} else if ("Artikelbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSONDERKONDITIONEN_ARTIKELBEZEICHNUNG];
			} else if ("KundeName1".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSONDERKONDITIONEN_KUNDE_NAME1];
			} else if ("KundeName2".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSONDERKONDITIONEN_KUNDE_NAME2];
			} else if ("Lkz".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSONDERKONDITIONEN_KUNDE_LKZ];
			} else if ("Plz".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSONDERKONDITIONEN_KUNDE_PLZ];
			} else if ("Ort".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSONDERKONDITIONEN_KUNDE_ORT];
			} else if ("KdArtikelnummer".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSONDERKONDITIONEN_KUNDE_KDARTIKELNUMMER];
			} else if ("KdArtikelbez".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSONDERKONDITIONEN_KUNDE_KDARTIKELBEZ];
			} else if ("KdArtikelbez2".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSONDERKONDITIONEN_KUNDE_KDARTIKELBEZ2];
			} else if ("Gueltigab".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSONDERKONDITIONEN_KUNDE_GUELTIAGAB];
			} else if ("Gueltigbis".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSONDERKONDITIONEN_KUNDE_GUELTIAGBIS];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSONDERKONDITIONEN_KUNDE_MENGE];
			} else if ("Fixpreis".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSONDERKONDITIONEN_KUNDE_FIXPREIS];
			} else if ("Rabatt".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSONDERKONDITIONEN_KUNDE_RABATT];
			} else if ("BerechneterPreis".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSONDERKONDITIONEN_KUNDE_BERECHNETER_PREIS];
			} else if ("BerechneterPreisZuHeute".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSONDERKONDITIONEN_KUNDE_BERECHNETER_PREIS_ZU_HEUTE];
			} else if ("NaechsteVKPreisbasisGueltigab".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSONDERKONDITIONEN_KUNDE_NAECHSTE_VKPREISBASIS_GUELTIG_AB];
			} else if ("KundeKbez".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSONDERKONDITIONEN_KUNDE_KBEZ];
			} else if ("KundeUID".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSONDERKONDITIONEN_KUNDE_UID];
			} else if ("BerechneterPreisKDWaehrung".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSONDERKONDITIONEN_KUNDE_BERECHNETER_PREIS_KDWAEHRUNG];
			} else if ("KDWaehrung".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSONDERKONDITIONEN_KUNDE_KDWAEHRUNG];
			} else if ("WirktNichtInPreisfindung".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSONDERKONDITIONEN_KUNDE_WIRKT_NICHT_IN_PREISFINDUNG];
			} else if ("KundeMandant".equals(fieldName)) {
				value = data[index][REPORT_KUNDENSONDERKONDITIONEN_KUNDE_MANDANT];
			} 
		} else if (sAktuellerReport.equals(ArtikelReportFac.REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG)) {
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
		} else if (sAktuellerReport.equals(ArtikelReportFac.REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG)) {
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
		} else if (sAktuellerReport.equals(ArtikelReportFac.REPORT_AUFTRAGSSERIENNUMMERN)) {
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
		} else if (sAktuellerReport.equals(ArtikelReportFac.REPORT_LIEFERANTENPREIS)) {
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
		} else if (sAktuellerReport.equals(ArtikelReportFac.REPORT_LIEFERANTENPREISVERGLEICH)) {
			if ("Lieferant".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENPREISVERGLEICH_LIEFERANT];
			} else if ("LieferantKbez".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENPREISVERGLEICH_LIEFERANT_KBEZ];
			} else if ("NichtLieferbar".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENPREISVERGLEICH_NICHT_LIEFERBAR];
			} else if ("Subreport".equals(fieldName)) {
				value = data[index][REPORT_LIEFERANTENPREISVERGLEICH_SUBREPORT];
			}
		} else if (sAktuellerReport.equals(ArtikelReportFac.REPORT_MAKE_OR_BUY)) {
			if ("Artikel".equals(fieldName)) {
				value = data[index][REPORT_MAKE_OR_BUY_ARTIKEL];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_MAKE_OR_BUY_BEZEICHNUNG];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_MAKE_OR_BUY_ZUSATZBEZEICHNUNG];
			} else if ("SubreportAblieferungen".equals(fieldName)) {
				value = data[index][REPORT_MAKE_OR_BUY_SUBREPORT_ABLIEFERUNGEN];
			} else if ("SubreportLieferantenpreise".equals(fieldName)) {
				value = data[index][REPORT_MAKE_OR_BUY_SUBREPORT_LIEFERANTENPREISE];
			} else if ("F_ARTIKEL_EINHEIT".equals(fieldName)) {
				value = data[index][REPORT_MAKE_OR_BUY_ARTIKEL_EINHEIT];
			}
		} else if (sAktuellerReport.equals(ArtikelReportFac.REPORT_ARTIKELETIKETT)) {
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
			} else if ("LagerLagerstandCharge".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_LAGER_LAGERSTAND_CHARGE];
			} else if ("LagerstandCharge".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_LAGERSTAND_CHARGE];
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
			} else if ("LfdNr".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_LFDNR];
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
			} else if ("SubreportLagerzubuchungen".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_SUBREPORT_LAGERZUBUCHUNGEN];
			}

			else if ("VerpackungmittelBezeichnung".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_VERPACKUNGSMITTEL_BEZEICHNUNG];
			} else if ("VerpackungmittelGewichtInKg".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_VERPACKUNGSMITTEL_GEWICHT_IN_KG];
			} else if ("VerpackungmittelKennung".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_VERPACKUNGSMITTEL_KENNUNG];
			} else if ("VerpackungmittelMenge".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_VERPACKUNGSMITTELMENGE];
			} else if ("Ursprungsland".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_URSPRUNGSLAND];
			} else if ("Exemplar".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_EXEMPLAR];
			} else if ("ExemplareGesamt".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELETIKETT_EXEMPLAREGESAMT];
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

		} else if (sAktuellerReport.equals(ArtikelReportFac.REPORT_LAGERPLATZETIKETT)) {
			if ("Lagerplatz".equals(fieldName)) {
				value = data[index][REPORT_LAGERPLATZ_LAGERPLATZ];
			} else if ("Lager".equals(fieldName)) {
				value = data[index][REPORT_LAGERPLATZ_LAGER];
			}
		} else if (sAktuellerReport.equals(ArtikelReportFac.REPORT_VKPREISLISTE)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_VKPREISLISTE_ARTIKELNUMMER];
			} else if ("Artikelreferenznummer".equals(fieldName)) {
				value = data[index][REPORT_VKPREISLISTE_ARTIKELREFERENZNUMMER];
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
			} else if ("Locale".equals(fieldName)) {
				value = data[index][REPORT_VKPREISLISTE_LOCALE];
			} else if ("Staffel1".equals(fieldName)) {
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
			} else if ("Mwstsatzbez".equals(fieldName)) {
				value = data[index][REPORT_VKPREISLISTE_MWSTSATZBEZ];
			} else if ("Mwstsatz".equals(fieldName)) {
				value = data[index][REPORT_VKPREISLISTE_MWSTSATZ];
			} else if ("VerkaufsEAN".equals(fieldName)) {
				value = data[index][REPORT_VKPREISLISTE_VERKAUFSEAN];
			} else if ("Material".equals(fieldName)) {
				value = data[index][REPORT_VKPREISLISTE_MATERIAL];
			} else if ("MaterialzuschlagEK".equals(fieldName)) {
				value = data[index][REPORT_VKPREISLISTE_MATERIALZUSCHLAGEK];
			} else if ("Verkaufspreisbasis".equals(fieldName)) {
				value = data[index][REPORT_VKPREISLISTE_VERKAUFSPREISBASIS];
			}

		} else if (sAktuellerReport.equals(ArtikelReportFac.REPORT_FREIINFERTIGUNG)) {
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
			} else if ("Fehlmengen".equals(fieldName)) {
				value = data[index][REPORT_FREIINFERTIGUNG_FEHLMENGEN];
			}
		} else if (sAktuellerReport.equals(ArtikelReportFac.REPORT_NAECHSTE_WARTUNGEN)) {
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

		} else if (sAktuellerReport.equals(ArtikelReportFac.REPORT_ARTIKELFEHLMENGE)) {

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
			} else if ("ZugehoerigerKundeImLos".equals(fieldName)) {
				value = data[index][REPORT_FEHLMENGENLISTE_ZUGEHOERIGER_KUNDE_IM_LOS];
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
		} else if (sAktuellerReport.equals(ArtikelReportFac.REPORT_ARTIKELSTAMMBLATT)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTAMMBLATT_ARTIKELNUMMER];
			} else if ("Referenznummer".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTAMMBLATT_REFERENZNUMMER];
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
				value = Helper.formatStyledTextForJasper(data[index][REPORT_ARTIKELSTAMMBLATT_LANGTEXT]);
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
			} else if ("Lagerbewirtschaftet".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTAMMBLATT_LAGERBEWIRTSCHAFTET];
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

			else if ("VerpackungmittelKennung".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTAMMBLATT_VERPACKUNGSMITTEL_KENNUNG];
			} else if ("VerpackungmittelBezeichnung".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTAMMBLATT_VERPACKUNGSMITTEL_BEZEICHNUNG];
			} else if ("VerpackungmittelGewichtInKg".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTAMMBLATT_VERPACKUNGSMITTEL_GEWICHT_IN_KG];
			} else if ("VerpackungmittelMenge".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTAMMBLATT_VERPACKUNGSMITTELMENGE];
			} else if ("Einheit".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTAMMBLATT_EINHEIT];
			} else if ("Bestellmengeneinheit".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTAMMBLATT_BESTELLMENGENEINHEIT];
			} else if ("Umrechnungsfaktor".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTAMMBLATT_UMRECHNUNGSFAKTOR];
			} else

			if ("ZugehoerigerArtikelArtikelnummer".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTAMMBLATT_ZUGEHOERIGER_ARTIKELNUMMER];
			} else if ("ZugehoerigerArtikelBezeichnung".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTAMMBLATT_ZUGEHOERIGER_BEZEICHNUNG];
			} else if ("ZugehoerigerArtikelKurzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTAMMBLATT_ZUGEHOERIGER_KURZBEZEICHNUNG];
			} else if ("ZugehoerigerArtikelZusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTAMMBLATT_ZUGEHOERIGER_ZUSATZBEZEICHNUNG2];
			} else if ("ZugehoerigerArtikelZusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTAMMBLATT_ZUGEHOERIGER_ZUSATZBEZEICHNUNG];
			} else if ("Zusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_ARTIKELSTAMMBLATT_ZUSATZBEZEICHNUNG];
			}

		} else if (sAktuellerReport.equals(ArtikelReportFac.REPORT_VERWENDUNGSNACHWEIS)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_VERWENDUNGSNACHWEIS_STUECKLISTE];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_VERWENDUNGSNACHWEIS_BEZEICHNUNG];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_VERWENDUNGSNACHWEIS_MENGE];
			} else if ("Zielmenge".equals(fieldName)) {
				value = data[index][REPORT_VERWENDUNGSNACHWEIS_ZIELMENGE];
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
			} else if ("Ebene".equals(fieldName)) {
				value = data[index][REPORT_VERWENDUNGSNACHWEIS_EBENE];
			} else if ("Mandant".equals(fieldName)) {
				value = data[index][REPORT_VERWENDUNGSNACHWEIS_MANDANT];
			} else if ("Lagermindest".equals(fieldName)) {
				value = data[index][REPORT_VERWENDUNGSNACHWEIS_LAGERMINDEST];
			} else if ("Lagersoll".equals(fieldName)) {
				value = data[index][REPORT_VERWENDUNGSNACHWEIS_LAGERSOLL];
			} else if ("Fertigungssatzgroesse".equals(fieldName)) {
				value = data[index][REPORT_VERWENDUNGSNACHWEIS_FERTIGUNGSSATZGROESSE];
			} else if ("MitFormeln".equals(fieldName)) {
				value = data[index][REPORT_VERWENDUNGSNACHWEIS_MIT_FORMELN];
			} else if ("STUECKLISTEPOSITION_I_ID".equals(fieldName)) {
				value = data[index][REPORT_VERWENDUNGSNACHWEIS_STUECKLISTEPOSITION_I_ID];
			} else if ("FehlerEinheitenkonvertierung".equals(fieldName)) {
				value = data[index][REPORT_VERWENDUNGSNACHWEIS_FEHLER_EINHEITENKONVERTIERUNG];
			}

		} else if (sAktuellerReport.equals(ArtikelReportFac.REPORT_ARTIKEL_OHNE_STKL_VERWENDUNG)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_ARTIKEL_OHNE_STKL_VERWENDUNG_ARTIKELNUMMER];
			} else if ("Artikelreferenznummer".equals(fieldName)) {
				value = data[index][REPORT_ARTIKEL_OHNE_STKL_VERWENDUNG_ARTIKELREFERENZNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_ARTIKEL_OHNE_STKL_VERWENDUNG_BEZEICHNUNG];
			} else if ("Kurzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_ARTIKEL_OHNE_STKL_VERWENDUNG_KURZBEZEICHNUNG];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_ARTIKEL_OHNE_STKL_VERWENDUNG_ZUSATZBEZEICHNUNG];
			} else if ("Lagerstand".equals(fieldName)) {
				value = data[index][REPORT_ARTIKEL_OHNE_STKL_VERWENDUNG_LAGERSTAND];
			} else if ("Fehlmengen".equals(fieldName)) {
				value = data[index][REPORT_ARTIKEL_OHNE_STKL_VERWENDUNG_FEHLMENGEN];
			} else if ("Reserviert".equals(fieldName)) {
				value = data[index][REPORT_ARTIKEL_OHNE_STKL_VERWENDUNG_RESERVIERT];
			} else if ("Bestellt".equals(fieldName)) {
				value = data[index][REPORT_ARTIKEL_OHNE_STKL_VERWENDUNG_BESTELLT];
			}

		} else if (sAktuellerReport.equals(ArtikelReportFac.REPORT_BEWEGUNGSVORSCHAU)) {
			if ("Belegart".equals(fieldName)) {
				value = data[index][REPORT_BEWEGUNGSVORSCHAU_BELEGART];
			} else if ("Belegnummer".equals(fieldName)) {
				value = data[index][REPORT_BEWEGUNGSVORSCHAU_BELEGNR];
			} else if ("Mandant".equals(fieldName)) {
				value = data[index][REPORT_BEWEGUNGSVORSCHAU_MANDANT];
			} else if ("Standort".equals(fieldName)) {
				value = data[index][REPORT_BEWEGUNGSVORSCHAU_STANDORT];
			} else if ("Liefertermin".equals(fieldName)) {
				value = data[index][REPORT_BEWEGUNGSVORSCHAU_LIEFERTERMIN];
			} else if ("Abliefertermin".equals(fieldName)) {
				value = data[index][REPORT_BEWEGUNGSVORSCHAU_ABLIEFERTERMIN];
			} else if ("Forecastart".equals(fieldName)) {
				value = data[index][REPORT_BEWEGUNGSVORSCHAU_FORECASTART];
			} else if ("ForecastBemerkung".equals(fieldName)) {
				value = data[index][REPORT_BEWEGUNGSVORSCHAU_FORECAST_BEMERKUNG];
			} else if ("ForecastpositionIId".equals(fieldName)) {
				value = data[index][REPORT_BEWEGUNGSVORSCHAU_FORECASTPOSITION_I_ID];
			} else if ("MengeUnterwegs".equals(fieldName)) {
				value = data[index][REPORT_BEWEGUNGSVORSCHAU_MENGE_UNTERWEGS];
			} else if ("AuftragsfreigabePerson".equals(fieldName)) {
				value = data[index][REPORT_BEWEGUNGSVORSCHAU_AUFTRAGSFREIGABE_PERSON];
			} else if ("AuftragsfreigabeZeitpunkt".equals(fieldName)) {
				value = data[index][REPORT_BEWEGUNGSVORSCHAU_AUFTRAGSFREIGABE_ZEITPUNKT];
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
			} else if ("LAGERWIRDVONINTERNERBESTELLUNGBERUECKSICHTIGT".equals(fieldName)) {
				value = data[index][REPORT_BEWEGUNGSVORSCHAU_INTERNEBESTELLUNG_BERUECKSICHTIGT_LAGER];
			} else if ("LAGERWIRDVONBESTELLVORSCHLAGBERUECKSICHTIGT".equals(fieldName)) {
				value = data[index][REPORT_BEWEGUNGSVORSCHAU_BESTELLVORSCHLAG_BERUECKSICHTIGT_LAGER];
			} else if ("LagerVersteckt".equals(fieldName)) {
				value = data[index][REPORT_BEWEGUNGSVORSCHAU_LAGER_VERSTECKT];
			} else if ("ZugehoerigerKundeImLos".equals(fieldName)) {
				value = data[index][REPORT_BEWEGUNGSVORSCHAU_ZUGEHOERIGER_KUNDE_IM_LOS];
			}

			else if ("LosStuecklisteIId".equals(fieldName)) {
				value = data[index][REPORT_BEWEGUNGSVORSCHAU_LOS_STUECKLISTE_I_ID];
			} else if ("LosStuecklisteNummer".equals(fieldName)) {
				value = data[index][REPORT_BEWEGUNGSVORSCHAU_LOS_STUECKLISTENUMMER];
			} else if ("LosStuecklisteBezeichnung".equals(fieldName)) {
				value = data[index][REPORT_BEWEGUNGSVORSCHAU_LOS_STUECKLISTEBEZEICHNUNG];
			} else if ("LosStuecklisteZusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_BEWEGUNGSVORSCHAU_LOS_STUECKLISTEZUSATZBEZEICHNUNG];
			} else if ("LosStuecklisteZusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_BEWEGUNGSVORSCHAU_LOS_STUECKLISTEZUSATZBEZEICHNUNG2];
			}

		} else if (sAktuellerReport.equals(ArtikelReportFac.REPORT_BEWEGUNGSVORSCHAU_ALLE)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_BEWEGUNGSVORSCHAU_ALLE_ARTIKELNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_BEWEGUNGSVORSCHAU_ALLE_ARTIKELBEZEICHNUNG];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_BEWEGUNGSVORSCHAU_ALLE_ARTIKELZUSATZBEZEICHNUNG];
			} else if ("Zusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_BEWEGUNGSVORSCHAU_ALLE_ARTIKELZUSATZBEZEICHNUNG2];
			} else if ("SubreportBewegungsvorschau".equals(fieldName)) {
				value = data[index][REPORT_BEWEGUNGSVORSCHAU_ALLE_SUBREPORT_BEWEGUNGSVORSCHAU];
			} else if ("SubreportBewegungsvorschauParameter".equals(fieldName)) {
				value = data[index][REPORT_BEWEGUNGSVORSCHAU_ALLE_SUBREPORT_BEWEGUNGSVORSCHAU_PARAMETER];
			}
		} else if (sAktuellerReport.equals(ArtikelReportFac.REPORT_AUFGELOESTEFEHLMENGEN)) {
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
			} else if ("Lagerstand".equals(fieldName)) {
				value = data[index][REPORT_AUFGELOESTEFEHLMENGEN_LAGERSTAND];
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
			} else if ("Kurzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_AUFGELOESTEFEHLMENGEN_KURZBEZEICHNUNG];
			} else if ("Referenznummer".equals(fieldName)) {
				value = data[index][REPORT_AUFGELOESTEFEHLMENGEN_REFERENZNUMMER];
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
			} else if ("ArtikelnummerErsterOffenerAG".equals(fieldName)) {
				value = data[index][REPORT_AUFGELOESTEFEHLMENGEN_ARTIKELNUMMER_OFFENER_AG];
			} else if ("ArtikelbezeichnungErsterOffenerAG".equals(fieldName)) {
				value = data[index][REPORT_AUFGELOESTEFEHLMENGEN_ARTIKELBEZEICHNUNG_OFFENER_AG];
			} else if ("Anlagezeitpunkt".equals(fieldName)) {
				value = data[index][REPORT_AUFGELOESTEFEHLMENGEN_ANLAGEZEITPUNKT];
			} else if ("KurzzeichenPersonalAnlegen".equals(fieldName)) {
				value = data[index][REPORT_AUFGELOESTEFEHLMENGEN_KURZZEICHEN_PERSONAL_ANLEGEN];
			}
		} else if (sAktuellerReport.equals(ArtikelReportFac.REPORT_ARTIKELBESTELLT)) {

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
			} else if ("F_GEBINDENAME".equals(fieldName)) {
				value = data[index][REPORT_BESTELLTLISTE_GEBINDENAME];
			} else if ("F_ANZAHL_GEBINDE".equals(fieldName)) {
				value = data[index][REPORT_BESTELLTLISTE_ANZAHL_GEBINDE];
			}
		} else if (sAktuellerReport.equals(ArtikelReportFac.REPORT_ARTIKELBESTELLTRAHMEN)) {
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
		} else if (sAktuellerReport.equals(ArtikelReportFac.REPORT_RAHMENRESERVIERUNG)) {
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
		} else if (sAktuellerReport.equals(ArtikelReportFac.REPORT_VKPREISENTWICKLUNG)) {
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
			} else if ("Bemerkung".equals(fieldName)) {
				value = data[index][REPORT_VKPREISENTWICKLUNG_BEMERKUNG];
			}
		} else if (sAktuellerReport.equals(ArtikelReportFac.REPORT_MINDESTLAGERSTAENDE)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_MINDESTLAGERSTAENDE_ARTIKEL];
			} else if ("Artikelreferenznummer".equals(fieldName)) {
				value = data[index][REPORT_MINDESTLAGERSTAENDE_ARTIKELREFERENZNUMMER];
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
			} else if ("Lager".equals(fieldName)) {
				value = data[index][REPORT_MINDESTLAGERSTAENDE_LAGER];
			}
		} else if (sAktuellerReport.equals(ArtikelReportFac.REPORT_SNRSTAMMBLATT)) {

			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_SNRSTAMMBLATT_ARTIKELNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_SNRSTAMMBLATT_ARTIKELBEZEICHNUNG];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_SNRSTAMMBLATT_MENGE];
			} else if ("Einheit".equals(fieldName)) {
				value = data[index][REPORT_SNRSTAMMBLATT_EINHEIT];
			} else if ("Abgang".equals(fieldName)) {
				value = data[index][REPORT_SNRSTAMMBLATT_ABGANG];
			} else if ("Zugang".equals(fieldName)) {
				value = data[index][REPORT_SNRSTAMMBLATT_ZUGANG];
			} else if ("Seriennummer".equals(fieldName)) {
				value = data[index][REPORT_SNRSTAMMBLATT_SERIENNUMER];
			} else if ("Version".equals(fieldName)) {
				value = data[index][REPORT_SNRSTAMMBLATT_VERSION];
			} else if ("Zeitpunkt".equals(fieldName)) {
				value = data[index][REPORT_SNRSTAMMBLATT_ZEITPUNKT];
			} else if ("SubreportGeraetesnr".equals(fieldName)) {
				value = data[index][REPORT_SNRSTAMMBLATT_SUBREPORT_GERAETESNR];
			} else if ("SubreportLosGesamtkalkulation".equals(fieldName)) {
				value = data[index][REPORT_SNRSTAMMBLATT_SUBREPORT_LOS_GESAMTKALKULATION];
			} else if ("Lager".equals(fieldName)) {
				value = data[index][REPORT_SNRSTAMMBLATT_LAGER];
			}

		}
		return value;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAufgeloesteFehlmengen(Integer fasessionId, boolean bNachdruck,
			TheClientDto theClientDto) {

		// Alle holen und nach Auftrag und Losnumer gruppieren

		TreeMap tmAufgeloesteFehlmengen = getFehlmengeFac().getAufgeloesteFehlmengenEinerSession(fasessionId,
				theClientDto);

		FasessionDto fasessionDto = getFehlmengeFac().fasessionFindByPrimaryKey(fasessionId);

		Set<?> s = tmAufgeloesteFehlmengen.keySet();
		JasperPrintLP print = null;

		Iterator<?> it = s.iterator();
		while (it.hasNext()) {
			Object key = it.next();
			ArrayList<AufgeloesteFehlmengenDto> al = (ArrayList) tmAufgeloesteFehlmengen.get(key);

			// Nach Artikel sortieren
			for (int i = al.size() - 1; i > 0; --i) {
				for (int j = 0; j < i; ++j) {
					AufgeloesteFehlmengenDto a = (AufgeloesteFehlmengenDto) al.get(j);
					AufgeloesteFehlmengenDto b = (AufgeloesteFehlmengenDto) al.get(j + 1);
					if (a.getArtikelCNr().compareTo(b.getArtikelCNr()) > 0) {
						AufgeloesteFehlmengenDto h = a;
						al.set(j, b);
						al.set(j + 1, h);
					}
				}
			}

			data = new Object[al.size()][REPORT_AUFGELOESTEFEHLMENGEN_ANZAHL_SPALTEN];
			for (int i = 0; i < al.size(); i++) {
				AufgeloesteFehlmengenDto aufgeloesteFehlmengenDto = (AufgeloesteFehlmengenDto) al.get(i);

				ArtikelDto artikelDto = aufgeloesteFehlmengenDto.getArtikelDto();
				data[i][REPORT_AUFGELOESTEFEHLMENGEN_ARTIKEL] = aufgeloesteFehlmengenDto.getArtikelDto().getCNr();
				data[i][REPORT_AUFGELOESTEFEHLMENGEN_REFERENZNUMMER] = aufgeloesteFehlmengenDto.getArtikelDto()
						.getCReferenznr();
				data[i][REPORT_AUFGELOESTEFEHLMENGEN_BEZEICHNUNG] = artikelDto.getArtikelsprDto().getCBez();
				data[i][REPORT_AUFGELOESTEFEHLMENGEN_ZUSATZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCZbez();
				data[i][REPORT_AUFGELOESTEFEHLMENGEN_KURZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCKbez();
				data[i][REPORT_AUFGELOESTEFEHLMENGEN_ZUSATZBEZEICHNUNG2] = artikelDto.getArtikelsprDto().getCZbez2();
				data[i][REPORT_AUFGELOESTEFEHLMENGEN_EINHEIT] = artikelDto.getEinheitCNr();

				ArtikelDto artikelDtoErsterOffenerAG = aufgeloesteFehlmengenDto.getArtikelDtoErsterOffenerAG();
				if (artikelDtoErsterOffenerAG != null) {
					data[i][REPORT_AUFGELOESTEFEHLMENGEN_ARTIKELNUMMER_OFFENER_AG] = artikelDtoErsterOffenerAG.getCNr();
					data[i][REPORT_AUFGELOESTEFEHLMENGEN_ARTIKELBEZEICHNUNG_OFFENER_AG] = artikelDtoErsterOffenerAG
							.formatBezeichnung();
				}

				try {
					if (artikelDto.getFarbcodeIId() != null) {
						data[i][REPORT_AUFGELOESTEFEHLMENGEN_FARBCODE] = getArtikelFac()
								.farbcodeFindByPrimaryKey(artikelDto.getFarbcodeIId()).getCNr();
					}
					// Material
					if (artikelDto.getMaterialIId() != null) {
						MaterialDto materialDto = getMaterialFac().materialFindByPrimaryKey(artikelDto.getMaterialIId(),
								theClientDto);
						data[i][REPORT_AUFGELOESTEFEHLMENGEN_MATERIAL] = materialDto.getBezeichnung();
					}
					// Lagerort

					data[i][REPORT_AUFGELOESTEFEHLMENGEN_LAGERORT] = getLagerFac().getLagerplaezteEinesArtikels(
							artikelDto.getIId(), aufgeloesteFehlmengenDto.getLagerDto().getIId());

					// Artkelklasse
					if (artikelDto.getArtklaIId() != null) {
						ArtklaDto aklaDto = getArtikelFac().artklaFindByPrimaryKey(artikelDto.getArtklaIId(),
								theClientDto);
						data[i][REPORT_AUFGELOESTEFEHLMENGEN_ARTIKELKLASSE] = aklaDto.getBezeichnung();
					}

					// Staerke/Hoehe
					if (artikelDto.getGeometrieDto() != null) {
						if (artikelDto.getGeometrieDto().getFHoehe() != null) {
							data[i][REPORT_AUFGELOESTEFEHLMENGEN_HOEHE] = artikelDto.getGeometrieDto().getFHoehe();
						}
						if (artikelDto.getGeometrieDto().getFBreite() != null) {
							data[i][REPORT_AUFGELOESTEFEHLMENGEN_BREITE] = artikelDto.getGeometrieDto().getFBreite();
						}
						if (artikelDto.getGeometrieDto().getFTiefe() != null) {
							data[i][REPORT_AUFGELOESTEFEHLMENGEN_TIEFE] = artikelDto.getGeometrieDto().getFTiefe();
						}
					}

					// Verpackung
					if (artikelDto.getVerpackungDto() != null) {
						data[i][REPORT_AUFGELOESTEFEHLMENGEN_BAUFORM] = artikelDto.getVerpackungDto().getCBauform();
						data[i][REPORT_AUFGELOESTEFEHLMENGEN_VERPACKUNGSART] = artikelDto.getVerpackungDto()
								.getCVerpackungsart();
					}

					// Gewicht
					data[i][REPORT_AUFGELOESTEFEHLMENGEN_GEWICHTKG] = artikelDto.getFGewichtkg();
					// Montage Rasterstehend
					if (artikelDto.getMontageDto() != null && artikelDto.getMontageDto().getFRasterstehend() != null) {
						data[i][REPORT_AUFGELOESTEFEHLMENGEN_RASTERSTEHEND] = new Double(
								artikelDto.getMontageDto().getFRasterstehend().doubleValue());
					}

				} catch (RemoteException ex1) {
					throwEJBExceptionLPRespectOld(ex1);
				}

				data[i][REPORT_AUFGELOESTEFEHLMENGEN_EINHEIT] = aufgeloesteFehlmengenDto.getArtikelDto()
						.getEinheitCNr();
				data[i][REPORT_AUFGELOESTEFEHLMENGEN_LAGER] = aufgeloesteFehlmengenDto.getLagerDto().getCNr();

				data[i][REPORT_AUFGELOESTEFEHLMENGEN_LAGERSTAND] = aufgeloesteFehlmengenDto.getLagerstand();

				data[i][REPORT_AUFGELOESTEFEHLMENGEN_MENGE] = aufgeloesteFehlmengenDto.getAufgeloesteMenge();
				data[i][REPORT_AUFGELOESTEFEHLMENGEN_SNRCHNR] = Helper
						.erzeugeStringAusStringArray(aufgeloesteFehlmengenDto.getSSeriennrChnr());

				if (aufgeloesteFehlmengenDto.getLieferscheinDto() != null) {
					data[i][REPORT_AUFGELOESTEFEHLMENGEN_LIEFERSCHEIN] = aufgeloesteFehlmengenDto.getLieferscheinDto()
							.getCNr();
				}

				data[i][REPORT_AUFGELOESTEFEHLMENGEN_ANLAGEZEITPUNKT] = aufgeloesteFehlmengenDto.getTAnlegen();
				data[i][REPORT_AUFGELOESTEFEHLMENGEN_KURZZEICHEN_PERSONAL_ANLEGEN] = aufgeloesteFehlmengenDto
						.getKurzzeichenPersonalAnlegen();

			}
			HashMap<String, Object> parameter = new HashMap<String, Object>();
			index = -1;
			sAktuellerReport = ArtikelReportFac.REPORT_AUFGELOESTEFEHLMENGEN;

			if (al.size() > 0) {

				AufgeloesteFehlmengenDto aufgeloesteFehlmengenDto = (AufgeloesteFehlmengenDto) al.get(0);

				if (aufgeloesteFehlmengenDto.getLosDto() != null) {

					String losBez = aufgeloesteFehlmengenDto.getLosDto().getCNr();
					if (aufgeloesteFehlmengenDto.getLosDto().getCProjekt() != null) {
						losBez += " " + aufgeloesteFehlmengenDto.getLosDto().getCProjekt();
					}
					LosDto losDto = aufgeloesteFehlmengenDto.getLosDto();
					parameter.put("P_LOSNUMMER", losDto.getCNr());

					parameter.put("P_ANGELEGT", new java.util.Date(losDto.getTAnlegen().getTime()));

					parameter.put("P_PROJEKT", losDto.getCProjekt());
					parameter.put("P_LOSKOMMENTAR", losDto.getCKommentar());
					parameter.put("P_LOSLANGTEXT", losDto.getXText());
					parameter.put("P_LOSGROESSE", losDto.getNLosgroesse());

					parameter.put("P_PRODUKTIONSBEGINN", losDto.getTProduktionsbeginn());
					parameter.put("P_PRODUKTIONSENDE", losDto.getTProduktionsende());

					try {
						if (losDto.getAuftragIId() != null) {
							AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(losDto.getAuftragIId());
							parameter.put("P_AUFTRAGNUMMER", auftragDto.getCNr());
							parameter.put("P_KUNDE", getKundeFac()
									.kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(), theClientDto)
									.getPartnerDto().getCName1nachnamefirmazeile1());
						}

						KostenstelleDto kstDto = getSystemFac()
								.kostenstelleFindByPrimaryKey(losDto.getKostenstelleIId());
						parameter.put("P_KOSTENSTELLENUMMER", kstDto.getCNr());

						FertigungsgruppeDto fertGruppeDto = getStuecklisteFac()
								.fertigungsgruppeFindByPrimaryKey(losDto.getFertigungsgruppeIId());
						parameter.put("P_FERTIGUNGSGRUPPE", fertGruppeDto.getCBez());

						if (losDto.getStuecklisteIId() != null) {
							StuecklisteDto stkDto = getStuecklisteFac()
									.stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(), theClientDto);
							parameter.put("P_STUECKLISTEBEZEICHNUNG",
									stkDto.getArtikelDto().getArtikelsprDto().getCBez());
							parameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG",
									stkDto.getArtikelDto().getArtikelsprDto().getCZbez());
							parameter.put("P_STUECKLISTEZUSATZBEZEICHNUNG2",
									stkDto.getArtikelDto().getArtikelsprDto().getCZbez2());

							parameter.put("P_STUECKLISTEKURZBEZEICHNUNG",
									stkDto.getArtikelDto().getArtikelsprDto().getCKbez());

							parameter.put("P_STUECKLISTENUMMER", stkDto.getArtikelDto().getCNr());
							parameter.put("P_STUECKLISTEREFERENZNUMMER", stkDto.getArtikelDto().getCReferenznr());

							// Zeichnungsnummer
							StuecklisteeigenschaftDto[] stuecklisteeigenschaftDtos = getStuecklisteFac()
									.stuecklisteeigenschaftFindByStuecklisteIId(losDto.getStuecklisteIId());
							ArrayList<Object[]> alZeichnung = new ArrayList<Object[]>();
							for (int i = 0; i < stuecklisteeigenschaftDtos.length; i++) {
								StuecklisteeigenschaftDto dto = stuecklisteeigenschaftDtos[i];

								Object[] o = new Object[2];
								String sStklEigenschaftArt = dto.getStuecklisteeigenschaftartDto().getCBez();
								o[0] = sStklEigenschaftArt;
								o[1] = dto.getCBez();
								alZeichnung.add(o);

								// Index und Materialplatz auch einzeln an
								// Report
								// uebergeben
								if (sStklEigenschaftArt
										.equals(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_INDEX)) {
									parameter.put(P_STUECKLISTENEIGENSCHAFT_INDEX, dto.getCBez());
								}
								if (sStklEigenschaftArt
										.equals(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_MATERIALPLATZ)) {
									parameter.put(P_STUECKLISTENEIGENSCHAFT_MATERIALPLATZ, dto.getCBez());
								}
							}

							// Stuecklisteeigenschaft fuer Subreport
							if (stuecklisteeigenschaftDtos.length > 0) {
								String[] fieldnames = new String[] { "F_EIGENSCHAFTART", "F_BEZEICHNUNG" };
								Object[][] dataSub = new Object[alZeichnung.size()][fieldnames.length];
								dataSub = (Object[][]) alZeichnung.toArray(dataSub);

								parameter.put("DATENSUBREPORT", new LPDatenSubreport(dataSub, fieldnames));
							}

							// Stuecklisteeigenschaften als einzelne Parameter
							// fuer
							// Index und Materialplatz
							Hashtable<?, ?> htStklEigenschaften = getStuecklisteReportFac().getStuecklisteEigenschaften(
									losDto.getStuecklisteIId(), theClientDto.getMandant(), theClientDto);
							if (htStklEigenschaften != null) {
								if (htStklEigenschaften
										.containsKey(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_INDEX)) {
									parameter.put(P_STUECKLISTENEIGENSCHAFT_INDEX, htStklEigenschaften
											.get(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_INDEX));
								}
								if (htStklEigenschaften.containsKey(
										StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_MATERIALPLATZ)) {
									parameter.put(P_STUECKLISTENEIGENSCHAFT_MATERIALPLATZ, htStklEigenschaften
											.get(StuecklisteReportFac.REPORT_STUECKLISTE_EIGENSCHAFTEN_MATERIALPLATZ));
								}
							}

						} else {
							parameter.put("P_STUECKLISTEBEZEICHNUNG", losDto.getCProjekt());
							parameter.put("P_STUECKLISTENUMMER", getTextRespectUISpr("fert.materialliste",
									theClientDto.getMandant(), theClientDto.getLocUi()));
						}
					} catch (RemoteException ex) {
						throwEJBExceptionLPRespectOld(ex);
					}
				} else if (aufgeloesteFehlmengenDto.getAuftagDto() != null) {
					parameter.put("P_KUNDENAUFTRAGSNUMMER", aufgeloesteFehlmengenDto.getAuftagDto().getCNr());

					parameter.put("P_ANGELEGT",
							new java.util.Date(aufgeloesteFehlmengenDto.getAuftagDto().getTAnlegen().getTime()));

					parameter.put("P_PROJEKT", aufgeloesteFehlmengenDto.getAuftagDto().getCBezProjektbezeichnung());

					parameter.put("P_KUNDE",
							getKundeFac().kundeFindByPrimaryKey(
									aufgeloesteFehlmengenDto.getAuftagDto().getKundeIIdAuftragsadresse(), theClientDto)
									.getPartnerDto().getCName1nachnamefirmazeile1());

					KostenstelleDto kstDto = getSystemFac()
							.kostenstelleFindByPrimaryKey(aufgeloesteFehlmengenDto.getAuftagDto().getKostIId());
					parameter.put("P_KOSTENSTELLENUMMER", kstDto.getCNr());

				}
			}

			parameter.put("P_DRUCKDATUM", fasessionDto.gettGedruckt());
			parameter.put("P_PERSON_GEDRUCKT", getPersonalFac()
					.personalFindByPrimaryKey(fasessionDto.getPersonalIId(), theClientDto).formatAnrede());

			if (bNachdruck == true) {
				parameter.put("P_PERSON_NACHDRUCK", getPersonalFac()
						.personalFindByPrimaryKey(theClientDto.getIDPersonal(), theClientDto).formatAnrede());
			}

			initJRDS(parameter, ArtikelReportFac.REPORT_MODUL, ArtikelReportFac.REPORT_AUFGELOESTEFEHLMENGEN,
					theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
			if (print != null) {

				print = Helper.addReport2Report(print, getReportPrint().getPrint());
			} else {
				print = getReportPrint();
			}
		}
		PrintInfoDto values = new PrintInfoDto();
		values.setDocPath(new HeliumDocPath().add(new DocNodeLiteral(theClientDto.getMandant()))
				.add(new DocNodeFolder(DocNodeBase.BELEGART_ARTIKEL))
				.add(new DocNodeFile(DocNodeBase.BELEGART_AUFGLFEHLMENGEN)));
		// JCRDocFac.HELIUMV_NODE + "/"
		// + LocaleFac.BELEGART_ARTIKEL.trim() + "/"
		// + "AufgeloesteFehlmengen";
		values.setiId(theClientDto.getIDPersonal());
		values.setTable("");

		if (print != null) {
			print.setOInfoForArchive(values);

			print.putAdditionalInformation(ADD_INFO_FASESSION, fasessionId);

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
		crit.add(Restrictions.isNotNull(ArtikelFac.FLR_ARTIKEL_I_WARTUNGSINTERVALL));
		crit.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));
		crit.addOrder(Order.asc("c_nr"));
		List<?> results = crit.list();
		Iterator<?> resultListIterator = results.iterator();

		ArrayList alDaten = new ArrayList();

		while (resultListIterator.hasNext()) {
			FLRArtikel flrartikel = (FLRArtikel) resultListIterator.next();

			Object[] zeile = new Object[REPORT_NAECHSTE_WARTUNGEN_ANZAHL_SPALTEN];

			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(flrartikel.getI_id(), theClientDto);

			Timestamp tFaelligSeit = null;
			if (artikelDto.getTLetztewartung() != null) {

				zeile[REPORT_NAECHSTE_WARTUNGEN_LETZTE_WARTUNG] = artikelDto.getTLetztewartung();

				Calendar cWartungFaellig = Calendar.getInstance();
				cWartungFaellig.setTimeInMillis(artikelDto.getTLetztewartung().getTime());
				cWartungFaellig.add(Calendar.MONTH, artikelDto.getIWartungsintervall());

				tFaelligSeit = new Timestamp(cWartungFaellig.getTimeInMillis());

			}

			if (tFaelligSeit == null || tFaelligSeit.getTime() < System.currentTimeMillis()) {

				zeile[REPORT_NAECHSTE_WARTUNGEN_FAELLIG_SEIT] = tFaelligSeit;

				zeile[REPORT_NAECHSTE_WARTUNGEN_ARTIKEL] = flrartikel.getC_nr();

				if (artikelDto.getArtikelsprDto() != null) {
					zeile[REPORT_NAECHSTE_WARTUNGEN_BEZEICHNUNG] = artikelDto.getArtikelsprDto().getCBez();
					zeile[REPORT_NAECHSTE_WARTUNGEN_KURZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCKbez();
					zeile[REPORT_NAECHSTE_WARTUNGEN_ZUSATZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCZbez();
					zeile[REPORT_NAECHSTE_WARTUNGEN_ZUSATZBEZEICHNUNG2] = artikelDto.getArtikelsprDto().getCZbez2();
				}

				zeile[REPORT_NAECHSTE_WARTUNGEN_REVISION] = artikelDto.getCRevision();
				zeile[REPORT_NAECHSTE_WARTUNGEN_INDEX] = artikelDto.getCIndex();
				zeile[REPORT_NAECHSTE_WARTUNGEN_REFERENZNUMMER] = artikelDto.getCReferenznr();

				if (flrartikel.getFlrartikelgruppe() != null) {
					zeile[REPORT_NAECHSTE_WARTUNGEN_ARTIKELGRUPPE] = flrartikel.getFlrartikelgruppe().getC_nr();
				}
				if (flrartikel.getFlrartikelklasse() != null) {
					zeile[REPORT_NAECHSTE_WARTUNGEN_ARTIKELKLASSE] = flrartikel.getFlrartikelklasse().getC_nr();
				}

				zeile[REPORT_NAECHSTE_WARTUNGEN_WARTUNGSINTERVALL] = artikelDto.getIWartungsintervall();

				ArtikellagerplaetzeDto artikellagerplaetzeDto = getLagerFac()
						.getErstenArtikellagerplatz(flrartikel.getI_id(), theClientDto);
				if (artikellagerplaetzeDto != null) {

					String lagerplatz = artikellagerplaetzeDto.getLagerplatzDto().getCLagerplatz();

					if (artikellagerplaetzeDto.isbEsGibtMehrereLagerplaetze() == true) {
						lagerplatz += " ++";
					}

					zeile[REPORT_NAECHSTE_WARTUNGEN_LAGERPLATZ] = lagerplatz;

					try {
						zeile[REPORT_NAECHSTE_WARTUNGEN_LAGER_LAGERPLATZ] = getLagerFac()
								.lagerFindByPrimaryKey(artikellagerplaetzeDto.getLagerplatzDto().getLagerIId())
								.getCNr();
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

		initJRDS(parameter, ArtikelReportFac.REPORT_MODUL, ArtikelReportFac.REPORT_NAECHSTE_WARTUNGEN,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printFehlmengen(Integer artikelIId, TheClientDto theClientDto) {
		if (artikelIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
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

		org.hibernate.Criteria crit = session.createCriteria(FLRFehlmenge.class).add(Example.create(flrFehlmenge));
		List<?> results = crit.list();
		Iterator<?> resultListIterator = results.iterator();

		int row = 0;

		Object[][] dataHelp = new Object[results.size()][11];

		while (resultListIterator.hasNext()) {
			FLRFehlmenge fehlmenge = (FLRFehlmenge) resultListIterator.next();

			String sBelegnummer = null;
			String sProjektbezeichnung = null;
			String sMandant = null;
			String sKunde = null;
			String sZugehoerigerKunde = null;
			String sStueckliste = null;
			String sStuecklisteBezeichnung = null;
			java.sql.Date tLosbeginn = null;
			java.sql.Date tLosende = null;
			java.sql.Timestamp tGeplanterliefertermin = null;

			if (fehlmenge.getC_belegartnr().equals(LocaleFac.BELEGART_LOS)) {

				com.lp.server.fertigung.service.LosDto losDto = null;
				try {
					if (fehlmenge.getFlrlossollmaterial() != null) {
						losDto = getFertigungFac().losFindByPrimaryKey(fehlmenge.getFlrlossollmaterial().getLos_i_id());
						sBelegnummer = "L" + losDto.getCNr();

						tLosbeginn = losDto.getTProduktionsbeginn();
						tLosende = losDto.getTProduktionsende();

						if (losDto.getAuftragIId() != null) {
							AuftragDto auftragDto = null;
							auftragDto = getAuftragFac().auftragFindByPrimaryKey(losDto.getAuftragIId());
							KundeDto kundeDto = getKundeFac()
									.kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(), theClientDto);

							sKunde = kundeDto.getPartnerDto().formatTitelAnrede();

							Integer kundeIId = getFertigungFac().getZugehoerigenKunden(losDto.getIId(), theClientDto);
							if (kundeIId != null) {
								sZugehoerigerKunde = getKundeFac().kundeFindByPrimaryKey(kundeIId, theClientDto)
										.getPartnerDto().formatFixTitelName1Name2();
							}

							tGeplanterliefertermin = auftragDto.getDLiefertermin();

							if (losDto.getAuftragpositionIId() != null) {
								AuftragpositionDto auftragpostionDto = getAuftragpositionFac()
										.auftragpositionFindByPrimaryKey(losDto.getAuftragpositionIId());
								if (auftragpostionDto.getTUebersteuerbarerLiefertermin() != null) {
									tGeplanterliefertermin = auftragpostionDto.getTUebersteuerbarerLiefertermin();
								}
							} else {

							}

						} else {
							sKunde = null;
						}
						sMandant = losDto.getMandantCNr();
						sProjektbezeichnung = losDto.getCProjekt();

						if (fehlmenge.getFlrlossollmaterial().getFlrlos().getFlrstueckliste() != null) {
							ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(fehlmenge
									.getFlrlossollmaterial().getFlrlos().getFlrstueckliste().getFlrartikel().getI_id(),
									theClientDto);
							sStueckliste = artikelDto.getCNr();
							sStuecklisteBezeichnung = artikelDto.formatBezeichnung();
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
				dataHelp[row][REPORT_FEHLMENGENLISTE_LIEFERTERMIN] = fehlmenge.getT_liefertermin();
				dataHelp[row][REPORT_FEHLMENGENLISTE_MENGE] = fehlmenge.getN_menge();
				dataHelp[row][REPORT_FEHLMENGENLISTE_KUNDE] = sKunde;
				dataHelp[row][REPORT_FEHLMENGENLISTE_ZUGEHOERIGER_KUNDE_IM_LOS] = sZugehoerigerKunde;
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

		ArtikelDto dto = getArtikelFac().artikelFindByPrimaryKey(artikelIId, theClientDto);
		parameter.put("P_ARTIKEL", dto.formatArtikelbezeichnung());
		parameter.put("P_ARTIKELREFERENZNUMMER", dto.getCReferenznr());

		initJRDS(parameter, ArtikelReportFac.REPORT_MODUL, ArtikelReportFac.REPORT_ARTIKELFEHLMENGE,
				eingeloggterMandant, theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	public boolean next() throws JRException {
		index++;
		return (index < data.length);
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printLosstatus(Integer artikelIId, TheClientDto theClientDto) {

		Integer stuecklisteIId = null;

		com.lp.server.stueckliste.service.StuecklisteDto stuecklisteDto = getStuecklisteFac()
				.stuecklisteFindByMandantCNrArtikelIIdOhneExc(artikelIId, theClientDto);
		if (stuecklisteDto != null) {
			stuecklisteIId = stuecklisteDto.getIId();
		}

		if (stuecklisteIId != null) {
			org.hibernate.Session session = getNewSession();
			org.hibernate.Criteria atikelliste = session.createCriteria(FLRLosReport.class);
			atikelliste.add(Restrictions.eq(FertigungFac.FLR_LOS_STUECKLISTE_I_ID, stuecklisteIId));

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
					data[row][REPORT_LOSSTATUS_AUFTRAG_PROJEKT] = flrauftrag.getC_bez();
				}

				data[row][REPORT_LOSSTATUS_LOSNR] = los.getC_nr();
				data[row][REPORT_LOSSTATUS_MENGE] = los.getN_losgroesse();
				data[row][REPORT_LOSSTATUS_PROJEKT] = los.getC_projekt();
				data[row][REPORT_LOSSTATUS_BEGINN] = new java.util.Date(los.getT_produktionsbeginn().getTime());
				data[row][REPORT_LOSSTATUS_ENDE] = new java.util.Date(los.getT_produktionsende().getTime());

				data[row][REPORT_LOSSTATUS_STATUS] = los.getStatus_c_nr().trim();

				String sQueryAblieferungen = "SELECT sum(losablieferung.n_menge) AS n_menge, sum(losablieferung.n_menge*losablieferung.n_materialwert) as materialwert,sum(losablieferung.n_menge*losablieferung.n_arbeitszeitwert) AS n_azwert "
						+ " FROM FLRLosablieferung AS losablieferung WHERE losablieferung.flrlos.i_id=" + los.getI_id();

				Query ablieferungen = session.createQuery(sQueryAblieferungen);

				if (ablieferungen.list().iterator().hasNext()) {

					Object[] o = (Object[]) ablieferungen.list().iterator().next();

					java.math.BigDecimal abgelieferteMange = (BigDecimal) o[0];

					data[row][REPORT_LOSSTATUS_ABGELIEFERT] = abgelieferteMange;

					if (abgelieferteMange != null && abgelieferteMange.doubleValue() != 0) {
						data[row][REPORT_LOSSTATUS_PREIS_ABLIEFERUNG_MATERIAL] = ((BigDecimal) o[1])
								.divide(abgelieferteMange, 4);
						data[row][REPORT_LOSSTATUS_PREIS_ABLIEFERUNG_ZEIT] = ((BigDecimal) o[2])
								.divide(abgelieferteMange, 4);
					}

				}

				try {
					LosDto losDto = getFertigungFac().losFindByPrimaryKey(los.getI_id());

					data[row][REPORT_LOSSTATUS_ANGELEGT] = losDto.getTAnlegen();
					data[row][REPORT_LOSSTATUS_ERLEDIGT] = losDto.getTErledigt();
					data[row][REPORT_LOSSTATUS_AUSGEGEBEN] = losDto.getTAusgabe();

					if (los.getFlrauftrag() != null) {

						KundeDto kundeDto = getKundeFac()
								.kundeFindByPrimaryKey(los.getFlrauftrag().getFlrkunde().getI_id(), theClientDto);
						data[row][REPORT_LOSSTATUS_KUNDE] = kundeDto.getPartnerDto().formatFixName1Name2();
					}

					data[row][REPORT_LOSSTATUS_FERTIGUNGSGRUPPE] = los.getFlrfertigungsgruppe().getC_bez();

					if (losDto.getPersonalIIdTechniker() != null) {
						data[row][REPORT_LOSSTATUS_TECHNIKER] = getPersonalFac()
								.personalFindByPrimaryKey(losDto.getPersonalIIdTechniker(), theClientDto)
								.formatFixUFTitelName2Name1();

					}

					data[row][REPORT_LOSSTATUS_ZIELLAGER] = getLagerFac()
							.lagerFindByPrimaryKey(losDto.getLagerIIdZiel()).getCNr();

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

		ArtikelDto dto = getArtikelFac().artikelFindByPrimaryKey(artikelIId, theClientDto);
		parameter.put("P_ARTIKEL", dto.formatArtikelbezeichnung());

		parameter.put("P_ARTIKELREFERENZNUMMER", dto.getCReferenznr());

		initJRDS(parameter, ArtikelReportFac.REPORT_MODUL, ArtikelReportFac.REPORT_LOSSTATUS, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);

		return getReportPrint();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printVerwendungsnachweis(Integer artikelIId, boolean bMitVerbrauchtenMengen,
			java.sql.Timestamp tVon, java.sql.Timestamp tBis, boolean bMitVersteckten, boolean bMitHierarchie,
			boolean bMandantenuebergreifend, boolean bVerdichtet, TheClientDto theClientDto) {
		if (artikelIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
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

		// Material
		alDaten = befuelleReportMitMaterialPositionen(artikelIId, bMitVerbrauchtenMengen, tVon, tBis, theClientDto,
				alDaten, bMitHierarchie, bMandantenuebergreifend, sMandant, bVerdichtet, 0);

		Session session2 = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit2 = session2.createCriteria(FLRStuecklistearbeitsplan.class)
				.createAlias(com.lp.server.stueckliste.service.StuecklisteFac.FLR_STUECKLISTEPOSITION_FLRSTUECKLISTE,
						"s")
				.add(Restrictions.eq("s.mandant_c_nr", sMandant))
				.createAlias(com.lp.server.stueckliste.service.StuecklisteFac.FLR_STUECKLISTEARBEITSPLAN_FLRARTIKEL,
						"a")
				.add(Restrictions.eq("a.i_id", artikelIId)).addOrder(Order.asc("a.c_nr"));
		List<?> results2 = crit2.list();
		Iterator<?> resultListIterator2 = results2.iterator();

		LinkedHashMap lokaleDatenVerdichtet = new LinkedHashMap();

		while (resultListIterator2.hasNext()) {
			FLRStuecklistearbeitsplan stuecklisteposition = (FLRStuecklistearbeitsplan) resultListIterator2.next();

			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(
					stuecklisteposition.getFlrstueckliste().getFlrartikel().getI_id(), theClientDto);

			Object[] oZeile = new Object[REPORT_VERWENDUNGSNACHWEIS_ANZAHL_SPALTEN];
			oZeile[REPORT_VERWENDUNGSNACHWEIS_STUECKLISTE] = stuecklisteposition.getFlrstueckliste().getFlrartikel()
					.getC_nr();

			oZeile[REPORT_VERWENDUNGSNACHWEIS_MIT_FORMELN] = Helper
					.short2Boolean(stuecklisteposition.getFlrstueckliste().getB_mitFormeln());

			oZeile[REPORT_VERWENDUNGSNACHWEIS_LAGERMINDEST] = artikelDto.getFLagermindest();
			oZeile[REPORT_VERWENDUNGSNACHWEIS_LAGERSOLL] = artikelDto.getFLagersoll();
			oZeile[REPORT_VERWENDUNGSNACHWEIS_FERTIGUNGSSATZGROESSE] = artikelDto.getFFertigungssatzgroesse();

			oZeile[REPORT_VERWENDUNGSNACHWEIS_BEZEICHNUNG] = artikelDto.formatBezeichnung();
			oZeile[REPORT_VERWENDUNGSNACHWEIS_VERSTECKT] = Helper.short2Boolean(artikelDto.getBVersteckt());
			oZeile[REPORT_VERWENDUNGSNACHWEIS_ZUSATZ] = "A";
			oZeile[REPORT_VERWENDUNGSNACHWEIS_ARBEITSGANG] = stuecklisteposition.getI_arbeitsgang();

			double lStueckzeit = stuecklisteposition.getL_stueckzeit();
			oZeile[REPORT_VERWENDUNGSNACHWEIS_STUECKZEIT] = Helper
					.rundeKaufmaennisch(new BigDecimal(lStueckzeit / 3600000), 4);
			double lRuestzeit = stuecklisteposition.getL_ruestzeit();
			oZeile[REPORT_VERWENDUNGSNACHWEIS_RUESTZEIT] = Helper
					.rundeKaufmaennisch(new BigDecimal(lRuestzeit / 3600000), 4);

			try {
				oZeile[REPORT_VERWENDUNGSNACHWEIS_LETZTE_VERWENDUNG] = getLagerFac()
						.getDatumLetzterZugangsOderAbgangsbuchung(artikelDto.getIId(), true);
				oZeile[REPORT_VERWENDUNGSNACHWEIS_SPERREN] = getArtikelFac().getArtikelsperrenText(artikelDto.getIId());
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			if (bVerdichtet) {
				if (lokaleDatenVerdichtet.containsKey(stuecklisteposition.getFlrstueckliste().getArtikel_i_id())) {

					Object[] oZeilevorhanden = (Object[]) lokaleDatenVerdichtet
							.get(stuecklisteposition.getFlrstueckliste().getArtikel_i_id());

					oZeilevorhanden[REPORT_VERWENDUNGSNACHWEIS_STUECKZEIT] = ((BigDecimal) oZeilevorhanden[REPORT_VERWENDUNGSNACHWEIS_STUECKZEIT])
							.add((BigDecimal) oZeile[REPORT_VERWENDUNGSNACHWEIS_STUECKZEIT]);
					oZeilevorhanden[REPORT_VERWENDUNGSNACHWEIS_RUESTZEIT] = ((BigDecimal) oZeilevorhanden[REPORT_VERWENDUNGSNACHWEIS_RUESTZEIT])
							.add((BigDecimal) oZeile[REPORT_VERWENDUNGSNACHWEIS_RUESTZEIT]);

					lokaleDatenVerdichtet.put(stuecklisteposition.getFlrstueckliste().getArtikel_i_id(),
							oZeilevorhanden);
				} else {
					lokaleDatenVerdichtet.put(stuecklisteposition.getFlrstueckliste().getArtikel_i_id(), oZeile);
				}
			}
			if (!bVerdichtet) {
				alDaten.add(oZeile);
			}

		}

		if (bVerdichtet) {

			Iterator it = lokaleDatenVerdichtet.keySet().iterator();
			while (it.hasNext()) {

				Integer artikelIIdLocal = (Integer) it.next();
				Object[] o = (Object[]) lokaleDatenVerdichtet.get(artikelIIdLocal);
				alDaten.add(o);
			}

		}

		Session session3 = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Criteria crit3 = session3
				.createCriteria(com.lp.server.angebotstkl.fastlanereader.generated.FLRAgstklposition.class)
				.createAlias(AngebotstklpositionFac.FLR_AGSTKLPOSITION_FLRAGSTKL, "as")
				.add(Restrictions.eq("as.mandant_c_nr", sMandant))
				.createAlias(AngebotstklpositionFac.FLR_AGSTKLPOSITION_FLRARTIKEL, "a")
				.add(Restrictions.eq("a.i_id", artikelIId)).addOrder(Order.asc("as.c_nr"));
		List<?> results3 = crit3.list();
		Iterator<?> resultListIterator3 = results3.iterator();
		while (resultListIterator3.hasNext()) {
			FLRAgstklposition stuecklisteposition = (FLRAgstklposition) resultListIterator3.next();
			Object[] oZeile = new Object[REPORT_VERWENDUNGSNACHWEIS_ANZAHL_SPALTEN];
			oZeile[REPORT_VERWENDUNGSNACHWEIS_STUECKLISTE] = "AS " + stuecklisteposition.getFlragstkl().getC_nr();
			oZeile[REPORT_VERWENDUNGSNACHWEIS_BEZEICHNUNG] = stuecklisteposition.getFlragstkl().getC_bez();
			oZeile[REPORT_VERWENDUNGSNACHWEIS_MENGE] = stuecklisteposition.getN_menge();
			oZeile[REPORT_VERWENDUNGSNACHWEIS_EINHEIT] = stuecklisteposition.getEinheit_c_nr();

			oZeile[REPORT_VERWENDUNGSNACHWEIS_VERSTECKT] = Helper.short2Boolean(Helper.boolean2Short(false));

			if (stuecklisteposition.getFlrartikel() != null) {
				try {
					oZeile[REPORT_VERWENDUNGSNACHWEIS_LETZTE_VERWENDUNG] = getLagerFac()
							.getDatumLetzterZugangsOderAbgangsbuchung(stuecklisteposition.getFlrartikel().getI_id(),
									true);
					oZeile[REPORT_VERWENDUNGSNACHWEIS_SPERREN] = getArtikelFac()
							.getArtikelsperrenText(stuecklisteposition.getFlrartikel().getI_id());
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}

			alDaten.add(oZeile);

		}

		session2.close();
		session3.close();

		// AGSTKLARBEITSPLAN hinzufuegen
		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Criteria critAgstklAP = session
				.createCriteria(com.lp.server.angebotstkl.fastlanereader.generated.FLRAgstklarbeitsplan.class)
				.createAlias(AngebotstklpositionFac.FLR_AGSTKLPOSITION_FLRAGSTKL, "as")
				.add(Restrictions.eq("as.mandant_c_nr", sMandant))
				.createAlias(AngebotstklpositionFac.FLR_AGSTKLPOSITION_FLRARTIKEL, "a")
				.add(Restrictions.eq("a.i_id", artikelIId)).addOrder(Order.asc("as.c_nr"));

		List<?> resultsAgstklAP = critAgstklAP.list();
		Iterator<?> resultListIteratorAgstklAP = resultsAgstklAP.iterator();

		while (resultListIteratorAgstklAP.hasNext()) {
			FLRAgstklarbeitsplan stuecklisteposition = (FLRAgstklarbeitsplan) resultListIteratorAgstklAP.next();

			Object[] oZeile = new Object[REPORT_VERWENDUNGSNACHWEIS_ANZAHL_SPALTEN];
			oZeile[REPORT_VERWENDUNGSNACHWEIS_STUECKLISTE] = "AS " + stuecklisteposition.getFlragstkl().getC_nr();
			oZeile[REPORT_VERWENDUNGSNACHWEIS_BEZEICHNUNG] = stuecklisteposition.getFlragstkl().getC_bez();

			oZeile[REPORT_VERWENDUNGSNACHWEIS_ARBEITSGANG] = stuecklisteposition.getI_arbeitsgang();
			oZeile[REPORT_VERWENDUNGSNACHWEIS_VERSTECKT] = Helper.short2Boolean(Helper.boolean2Short(false));

			double lStueckzeit = stuecklisteposition.getL_stueckzeit();
			oZeile[REPORT_VERWENDUNGSNACHWEIS_STUECKZEIT] = Helper
					.rundeKaufmaennisch(new BigDecimal(lStueckzeit / 3600000), 4);
			double lRuestzeit = stuecklisteposition.getL_ruestzeit();
			oZeile[REPORT_VERWENDUNGSNACHWEIS_RUESTZEIT] = Helper
					.rundeKaufmaennisch(new BigDecimal(lRuestzeit / 3600000), 4);

			if (stuecklisteposition.getFlrartikel() != null) {
				try {
					oZeile[REPORT_VERWENDUNGSNACHWEIS_LETZTE_VERWENDUNG] = getLagerFac()
							.getDatumLetzterZugangsOderAbgangsbuchung(stuecklisteposition.getFlrartikel().getI_id(),
									true);
					oZeile[REPORT_VERWENDUNGSNACHWEIS_SPERREN] = getArtikelFac()
							.getArtikelsperrenText(stuecklisteposition.getFlrartikel().getI_id());
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
				.createCriteria(com.lp.server.angebotstkl.fastlanereader.generated.FLREinkaufsangebotposition.class)
				.createAlias("flreinkaufsangebot", "ek").add(Restrictions.eq("ek.mandant_c_nr", sMandant))
				.createAlias(AngebotstklpositionFac.FLR_AGSTKLPOSITION_FLRARTIKEL, "a")
				.add(Restrictions.eq("a.i_id", artikelIId)).addOrder(Order.asc("ek.c_nr"));

		List<?> resultsEKAngebotPos = critEKAngebotPos.list();
		Iterator<?> resultListIteratorEKAngebotPos = resultsEKAngebotPos.iterator();

		while (resultListIteratorEKAngebotPos.hasNext()) {

			FLREinkaufsangebotposition stuecklisteposition = (FLREinkaufsangebotposition) resultListIteratorEKAngebotPos
					.next();
			Object[] oZeile = new Object[REPORT_VERWENDUNGSNACHWEIS_ANZAHL_SPALTEN];
			oZeile[REPORT_VERWENDUNGSNACHWEIS_STUECKLISTE] = "EK "
					+ stuecklisteposition.getFlreinkaufsangebot().getC_nr();
			oZeile[REPORT_VERWENDUNGSNACHWEIS_BEZEICHNUNG] = stuecklisteposition.getFlreinkaufsangebot().getC_projekt();
			oZeile[REPORT_VERWENDUNGSNACHWEIS_MENGE] = stuecklisteposition.getN_menge();
			oZeile[REPORT_VERWENDUNGSNACHWEIS_EINHEIT] = stuecklisteposition.getEinheit_c_nr();
			oZeile[REPORT_VERWENDUNGSNACHWEIS_VERSTECKT] = Helper.short2Boolean(Helper.boolean2Short(false));

			if (stuecklisteposition.getFlrartikel() != null) {
				try {
					oZeile[REPORT_VERWENDUNGSNACHWEIS_LETZTE_VERWENDUNG] = getLagerFac()
							.getDatumLetzterZugangsOderAbgangsbuchung(stuecklisteposition.getFlrartikel().getI_id(),
									true);
					oZeile[REPORT_VERWENDUNGSNACHWEIS_SPERREN] = getArtikelFac()
							.getArtikelsperrenText(stuecklisteposition.getFlrartikel().getI_id());
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}

			alDaten.add(oZeile);

		}
		session.close();
		data = new Object[alDaten.size()][REPORT_VERWENDUNGSNACHWEIS_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(data);

		if (bMitHierarchie == false) {

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
		}
		ArtikelDto dto = getArtikelFac().artikelFindByPrimaryKey(artikelIId, theClientDto);
		parameter.put("P_ARTIKEL", dto.formatArtikelbezeichnung());
		parameter.put("P_ARTIKEL_EINHEIT", dto.getEinheitCNr());
		parameter.put("P_MITVERSTECKTEN", new Boolean(bMitVersteckten));
		parameter.put("P_VERDICHTET", new Boolean(bVerdichtet));
		parameter.put("P_MITVERBRAUCHTENMENGEN", new Boolean(bMitVerbrauchtenMengen));
		parameter.put("P_MITHIERARCHIE", new Boolean(bMitHierarchie));
		parameter.put("P_MANDANTENUEBERGREIFEND", new Boolean(bMandantenuebergreifend));
		parameter.put("P_VON", tVon);
		parameter.put("P_BIS", tBis);
		parameter.put("P_ARTIKELREFERENZNUMMER", dto.getCReferenznr());

		initJRDS(parameter, ArtikelReportFac.REPORT_MODUL, ArtikelReportFac.REPORT_VERWENDUNGSNACHWEIS, sMandant, locUi,
				theClientDto);
		print = getReportPrint();
		return print;
	}

	private ArrayList befuelleReportMitMaterialPositionen(Integer artikelIId, boolean bMitVerbrauchtenMengen,
			java.sql.Timestamp tVon, java.sql.Timestamp tBis, TheClientDto theClientDto, ArrayList alDaten,
			boolean bMitHierarchie, boolean bMandantenuebergreifend, String sMandant, boolean bVerdichtet, int iEbene) {
		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Criteria crit = session.createCriteria(FLRStuecklisteposition.class)
				.createAlias(com.lp.server.stueckliste.service.StuecklisteFac.FLR_STUECKLISTEPOSITION_FLRSTUECKLISTE,
						"s")
				.createAlias(com.lp.server.stueckliste.service.StuecklisteFac.FLR_STUECKLISTEPOSITION_FLRARTIKEL, "a")
				.add(Restrictions.eq("a.i_id", artikelIId)).addOrder(Order.asc("a.c_nr"));

		if (bMandantenuebergreifend == false) {
			crit.add(Restrictions.eq("s.mandant_c_nr", sMandant));
		}

		List<?> results = crit.list();
		Iterator<?> resultListIterator = results.iterator();

		iEbene++;

		LinkedHashMap lokaleDatenVerdichtet = new LinkedHashMap();

		while (resultListIterator.hasNext()) {
			FLRStuecklisteposition stuecklisteposition = (FLRStuecklisteposition) resultListIterator.next();

			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(
					stuecklisteposition.getFlrstueckliste().getFlrartikel().getI_id(), theClientDto);

			Object[] oZeile = new Object[REPORT_VERWENDUNGSNACHWEIS_ANZAHL_SPALTEN];
			oZeile[REPORT_VERWENDUNGSNACHWEIS_STUECKLISTE] = stuecklisteposition.getFlrstueckliste().getFlrartikel()
					.getC_nr();

			oZeile[REPORT_VERWENDUNGSNACHWEIS_STUECKLISTEPOSITION_I_ID] = stuecklisteposition.getI_id();
			oZeile[REPORT_VERWENDUNGSNACHWEIS_MIT_FORMELN] = Helper
					.short2Boolean(stuecklisteposition.getFlrstueckliste().getB_mitFormeln());
			oZeile[REPORT_VERWENDUNGSNACHWEIS_BEZEICHNUNG] = artikelDto.formatBezeichnung();

			oZeile[REPORT_VERWENDUNGSNACHWEIS_LAGERMINDEST] = artikelDto.getFLagermindest();
			oZeile[REPORT_VERWENDUNGSNACHWEIS_LAGERSOLL] = artikelDto.getFLagersoll();
			oZeile[REPORT_VERWENDUNGSNACHWEIS_FERTIGUNGSSATZGROESSE] = artikelDto.getFFertigungssatzgroesse();

			oZeile[REPORT_VERWENDUNGSNACHWEIS_MANDANT] = stuecklisteposition.getFlrstueckliste().getMandant_c_nr();

			oZeile[REPORT_VERWENDUNGSNACHWEIS_EBENE] = iEbene;

			oZeile[REPORT_VERWENDUNGSNACHWEIS_VERSTECKT] = Helper.short2Boolean(artikelDto.getBVersteckt());
			oZeile[REPORT_VERWENDUNGSNACHWEIS_MENGE] = stuecklisteposition.getN_menge();

			oZeile[REPORT_VERWENDUNGSNACHWEIS_EINHEIT] = stuecklisteposition.getEinheit_c_nr();

			oZeile[REPORT_VERWENDUNGSNACHWEIS_FEHLER_EINHEITENKONVERTIERUNG] = Boolean.FALSE;

			try {

				try {
					oZeile[REPORT_VERWENDUNGSNACHWEIS_ZIELMENGE] = getStuecklisteFac()
							.berechneZielmenge(stuecklisteposition.getI_id(), theClientDto);
				} catch (EJBExceptionLP e) {
					if (e.getCode() == EJBExceptionLP.FEHLER_EINHEITKONVERTIERUNG_KEIN_DIVISOR_DEFINIERT) {
						oZeile[REPORT_VERWENDUNGSNACHWEIS_FEHLER_EINHEITENKONVERTIERUNG] = Boolean.TRUE;
					} else {
						throw e;
					}
				}

				oZeile[REPORT_VERWENDUNGSNACHWEIS_LETZTE_VERWENDUNG] = getLagerFac()
						.getDatumLetzterZugangsOderAbgangsbuchung(artikelDto.getIId(), true);
				oZeile[REPORT_VERWENDUNGSNACHWEIS_SPERREN] = getArtikelFac().getArtikelsperrenText(artikelDto.getIId());
				if (bMitVerbrauchtenMengen) {
					oZeile[REPORT_VERWENDUNGSNACHWEIS_VERBRAUCHTEMENGE] = getLagerFac()
							.getVerbrauchteMengeEinesArtikels(
									stuecklisteposition.getFlrstueckliste().getFlrartikel().getI_id(), tVon, tBis,
									theClientDto);
				}

				oZeile[REPORT_VERWENDUNGSNACHWEIS_LAGERSTAND] = getLagerFac()
						.getLagerstandAllerLagerEinesMandanten(artikelDto.getIId(), theClientDto);

				oZeile[REPORT_VERWENDUNGSNACHWEIS_RESERVIERT] = getReservierungFac()
						.getAnzahlReservierungen(artikelDto.getIId(), theClientDto);

				oZeile[REPORT_VERWENDUNGSNACHWEIS_IN_FERTIGUNG] = getFertigungFac()
						.getAnzahlInFertigung(artikelDto.getIId(), theClientDto);

				oZeile[REPORT_VERWENDUNGSNACHWEIS_FEHLMENGE] = getFehlmengeFac()
						.getAnzahlFehlmengeEinesArtikels(artikelDto.getIId(), theClientDto);

				oZeile[REPORT_VERWENDUNGSNACHWEIS_RAHMENRESERVIERT] = getReservierungFac()
						.getAnzahlRahmenreservierungen(artikelDto.getIId(), theClientDto);
				if (bVerdichtet) {
					if (lokaleDatenVerdichtet.containsKey(stuecklisteposition.getFlrstueckliste().getArtikel_i_id())) {

						Object[] oZeilevorhanden = (Object[]) lokaleDatenVerdichtet
								.get(stuecklisteposition.getFlrstueckliste().getArtikel_i_id());

						oZeilevorhanden[REPORT_VERWENDUNGSNACHWEIS_ZIELMENGE] = ((BigDecimal) oZeilevorhanden[REPORT_VERWENDUNGSNACHWEIS_ZIELMENGE])
								.add(getStuecklisteFac().berechneZielmenge(stuecklisteposition.getI_id(),
										theClientDto));

						lokaleDatenVerdichtet.put(stuecklisteposition.getFlrstueckliste().getArtikel_i_id(),
								oZeilevorhanden);
					} else {
						lokaleDatenVerdichtet.put(stuecklisteposition.getFlrstueckliste().getArtikel_i_id(), oZeile);
					}
				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			if (!bVerdichtet) {

				alDaten.add(oZeile);
			}

			if (bMitHierarchie && bVerdichtet == false) {

				String mandantCnr = sMandant;

				if (bMandantenuebergreifend) {
					StuecklisteDto[] stklDtos = getStuecklisteFac()
							.stuecklisteFindByArtikelIId(stuecklisteposition.getFlrstueckliste().getArtikel_i_id());
					if (stklDtos.length > 0) {
						mandantCnr = stklDtos[0].getMandantCNr();
					}
				}

				alDaten = befuelleReportMitMaterialPositionen(stuecklisteposition.getFlrstueckliste().getArtikel_i_id(),
						bMitVerbrauchtenMengen, tVon, tBis, theClientDto, alDaten, bMitHierarchie,
						bMandantenuebergreifend, mandantCnr, bVerdichtet, iEbene);

			}

		}
		session.close();

		if (bVerdichtet) {

			Iterator it = lokaleDatenVerdichtet.keySet().iterator();
			while (it.hasNext()) {

				Integer artikelIIdLocal = (Integer) it.next();
				Object[] o = (Object[]) lokaleDatenVerdichtet.get(artikelIIdLocal);
				alDaten.add(o);
			}

		}

		return alDaten;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printArtikelOhneStklVerwendung(boolean bMitVersteckten, TheClientDto theClientDto) {

		ArrayList alDaten = new ArrayList();

		JasperPrintLP print = null;
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		String sMandant = theClientDto.getMandant();
		Locale locUi = theClientDto.getLocUi();
		index = -1;
		sAktuellerReport = ArtikelReportFac.REPORT_ARTIKEL_OHNE_STKL_VERWENDUNG;

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;

		String mandant_c_nr = theClientDto.getMandant();
		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
				theClientDto)) {
			mandant_c_nr = getSystemFac().getHauptmandant();
		}

		session = factory.openSession();
		String queryString = "SELECT artikel,(SELECT count(*) FROM FLRStuecklisteposition AS pos WHERE pos.flrartikel.i_id=artikel.i_id ) as anzstklpos, (SELECT spr.c_bez FROM FLRArtikellistespr spr WHERE spr.Id.artikelliste=artikel.i_id AND spr.Id.locale='"
				+ theClientDto.getLocUiAsString()
				+ "') as cbez, (SELECT spr.c_kbez FROM FLRArtikellistespr spr WHERE spr.Id.artikelliste=artikel.i_id AND spr.Id.locale='"
				+ theClientDto.getLocUiAsString()
				+ "') as ckbez, (SELECT spr.c_zbez FROM FLRArtikellistespr spr WHERE spr.Id.artikelliste=artikel.i_id AND spr.Id.locale='"
				+ theClientDto.getLocUiAsString()
				+ "') as czbez  FROM FLRArtikel AS artikel WHERE artikel.artikelart_c_nr='"
				+ ArtikelFac.ARTIKELART_ARTIKEL + "' AND artikel.mandant_c_nr='" + mandant_c_nr + "'";

		if (bMitVersteckten == false) {
			queryString += " AND artikel.b_versteckt=0";
		}

		queryString += "  ORDER BY artikel.c_nr ";

		String sLocUI = Helper.locale2String(theClientDto.getLocUi());
		session.enableFilter("filterLocale").setParameter("paramLocale", sLocUI);
		org.hibernate.Query query = session.createQuery(queryString);
		List<?> resultList = query.list();
		Iterator<?> resultListIterator = resultList.iterator();
		while (resultListIterator.hasNext()) {
			Object[] o = (Object[]) resultListIterator.next();
			FLRArtikel flrArtikel = (FLRArtikel) o[0];

			String bez = (String) o[2];
			String kbez = (String) o[2];
			String zbez = (String) o[2];

			long iAzahlinVerwendung = 0;
			if (o[1] != null) {
				iAzahlinVerwendung = (Long) o[1];
			}

			if (iAzahlinVerwendung == 0) {

				StuecklisteDto stklDto = getStuecklisteFac()
						.stuecklisteFindByArtikelIIdMandantCNrOhneExc(flrArtikel.getI_id(), theClientDto.getMandant());

				// Eigengefertigte Stuecklisten zaehlen als Verwendet
				if (stklDto != null && Helper.short2boolean(stklDto.getBFremdfertigung()) == false) {
					continue;
				}

				Object[] oZeile = new Object[REPORT_ARTIKEL_OHNE_STKL_VERWENDUNG_ANZAHL_SPALTEN];
				oZeile[REPORT_ARTIKEL_OHNE_STKL_VERWENDUNG_ARTIKELNUMMER] = flrArtikel.getC_nr();
				oZeile[REPORT_ARTIKEL_OHNE_STKL_VERWENDUNG_ARTIKELREFERENZNUMMER] = flrArtikel.getC_referenznr();
				oZeile[REPORT_ARTIKEL_OHNE_STKL_VERWENDUNG_BEZEICHNUNG] = bez;
				oZeile[REPORT_ARTIKEL_OHNE_STKL_VERWENDUNG_KURZBEZEICHNUNG] = kbez;

				oZeile[REPORT_ARTIKEL_OHNE_STKL_VERWENDUNG_ZUSATZBEZEICHNUNG] = zbez;

				try {
					oZeile[REPORT_ARTIKEL_OHNE_STKL_VERWENDUNG_LAGERSTAND] = getLagerFac()
							.getLagerstandAllerLagerEinesMandanten(flrArtikel.getI_id(), theClientDto);
					oZeile[REPORT_ARTIKEL_OHNE_STKL_VERWENDUNG_FEHLMENGEN] = getFehlmengeFac()
							.getAnzahlFehlmengeEinesArtikels(flrArtikel.getI_id(), theClientDto);
					oZeile[REPORT_ARTIKEL_OHNE_STKL_VERWENDUNG_RESERVIERT] = getReservierungFac()
							.getAnzahlReservierungen(flrArtikel.getI_id(), theClientDto);

					oZeile[REPORT_ARTIKEL_OHNE_STKL_VERWENDUNG_BESTELLT] = getArtikelbestelltFac()
							.getAnzahlBestellt(flrArtikel.getI_id());
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

				alDaten.add(oZeile);
			}

		}

		parameter.put("P_MITVERSTECKTEN", new Boolean(bMitVersteckten));

		Object[][] dataTemp = new Object[alDaten.size()][REPORT_VERWENDUNGSNACHWEIS_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(dataTemp);

		initJRDS(parameter, ArtikelReportFac.REPORT_MODUL, ArtikelReportFac.REPORT_ARTIKEL_OHNE_STKL_VERWENDUNG,
				sMandant, locUi, theClientDto);
		print = getReportPrint();
		return print;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printFreiInFertigung(Integer artikelIId, TheClientDto theClientDto) {
		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT lossollmaterial.los_i_id, sum(lossollmaterial.n_menge) AS n_menge "
				+ " FROM FLRLossollmaterial AS lossollmaterial WHERE lossollmaterial."
				+ FertigungFac.FLR_LOSSOLLMATERIAL_FLRARTIKEL + ".i_id=" + artikelIId
				+ " AND lossollmaterial.n_menge > 0 AND lossollmaterial." + FertigungFac.FLR_LOSSOLLMATERIAL_FLRLOS
				+ ".mandant_c_nr='" + theClientDto.getMandant() + "' AND lossollmaterial."
				+ FertigungFac.FLR_LOSSOLLMATERIAL_FLRLOS + ".status_c_nr NOT IN ('" + FertigungFac.STATUS_ERLEDIGT
				+ "','" + FertigungFac.STATUS_STORNIERT + "') GROUP BY lossollmaterial.los_i_id, lossollmaterial."
				+ FertigungFac.FLR_LOSSOLLMATERIAL_FLRARTIKEL + ".i_id ORDER BY lossollmaterial.los_i_id DESC";

		Query inventurliste = session.createQuery(sQuery);

		List<?> resultList = inventurliste.list();

		Iterator<?> resultListIterator = resultList.iterator();

		ArrayList alDaten = new ArrayList();

		data = new Object[resultList.size()][6];

		while (resultListIterator.hasNext()) {
			Object o[] = (Object[]) resultListIterator.next();

			Integer los_i_id = (Integer) o[0];
			java.math.BigDecimal n_menge = (java.math.BigDecimal) o[1];
			Object[] oZeile = new Object[REPORT_FREIINFERTIGUNG_ANZAHL_SPALTEN];
			try {
				LosDto losDto = getFertigungFac().losFindByPrimaryKey(los_i_id);
				if (losDto.getAuftragpositionIId() != null) {
					Integer auftrag_i_id = getAuftragpositionFac()
							.auftragpositionFindByPrimaryKey(losDto.getAuftragpositionIId()).getBelegIId();
					oZeile[REPORT_FREIINFERTIGUNG_AUFTRAG] = getAuftragFac().auftragFindByPrimaryKey(auftrag_i_id)
							.getCNr();
				}

				// Ablieferungen
				Session session2 = FLRSessionFactory.getFactory().openSession();

				String sQueryAblieferungen = "SELECT sum(losablieferung.n_menge) AS n_menge "
						+ " FROM FLRLosablieferung AS losablieferung WHERE losablieferung.flrlos.i_id="
						+ losDto.getIId() + " GROUP BY losablieferung.los_i_id";

				Query ablieferungen = session2.createQuery(sQueryAblieferungen);

				java.math.BigDecimal abgelieferteMange = new java.math.BigDecimal(0);

				if (ablieferungen.list().iterator().hasNext()) {
					abgelieferteMange = (java.math.BigDecimal) ablieferungen.list().iterator().next();
				}
				session2.close();

				java.math.BigDecimal ausgegebeneMengePLUS = new java.math.BigDecimal(0);
				java.math.BigDecimal ausgegebeneMengeMINUS = new java.math.BigDecimal(0);
				// Ausgaben POSITIV
				session2 = FLRSessionFactory.getFactory().openSession();

				String sQueryAusgegebeneMengePLUS = "SELECT sum(ist.n_menge) FROM FLRLosistmaterial ist WHERE ist.flrlossollmaterial.flrlos.i_id="
						+ los_i_id + "AND ist.flrlossollmaterial.flrartikel.i_id=" + artikelIId + " AND ist.b_abgang=1";

				Query queryAusgegebeneMengPLUSe = session2.createQuery(sQueryAusgegebeneMengePLUS);

				if (queryAusgegebeneMengPLUSe.list().iterator().hasNext()) {
					ausgegebeneMengePLUS = (java.math.BigDecimal) queryAusgegebeneMengPLUSe.list().iterator().next();
				}
				session2.close();

				// Ausgaben NEGATIV
				session2 = FLRSessionFactory.getFactory().openSession();

				String sQueryAusgegebeneMengeMINUS = "SELECT sum(ist.n_menge) FROM FLRLosistmaterial ist WHERE ist.flrlossollmaterial.flrlos.i_id="
						+ los_i_id + " AND ist.flrlossollmaterial.flrartikel.i_id=" + artikelIId
						+ " AND ist.b_abgang=0";

				Query queryAusgegebeneMengMINUS = session2.createQuery(sQueryAusgegebeneMengeMINUS);

				if (queryAusgegebeneMengMINUS.list().iterator().hasNext()) {
					ausgegebeneMengeMINUS = (java.math.BigDecimal) queryAusgegebeneMengMINUS.list().iterator().next();
				}
				session2.close();

				BigDecimal bdAusgegeben = BigDecimal.ZERO;

				if (ausgegebeneMengePLUS != null) {
					bdAusgegeben = bdAusgegeben.add(ausgegebeneMengePLUS);
				}

				if (ausgegebeneMengeMINUS != null) {
					bdAusgegeben = bdAusgegeben.subtract(ausgegebeneMengeMINUS);
				}

				BigDecimal ssg = n_menge.divide(losDto.getNLosgroesse(), 4, BigDecimal.ROUND_HALF_EVEN);

				// PJ19231
				BigDecimal freiInFertigung = bdAusgegeben.subtract(ssg.multiply(abgelieferteMange));

				if (freiInFertigung.doubleValue() > 0) {

					oZeile[REPORT_FREIINFERTIGUNG_BEGINN] = new java.util.Date(
							losDto.getTProduktionsbeginn().getTime());
					oZeile[REPORT_FREIINFERTIGUNG_ENDE] = new java.util.Date(losDto.getTProduktionsende().getTime());
					oZeile[REPORT_FREIINFERTIGUNG_FREIEMENGE] = freiInFertigung;
					oZeile[REPORT_FREIINFERTIGUNG_LOSNR] = losDto.getCNr();
					oZeile[REPORT_FREIINFERTIGUNG_PROJEKT] = losDto.getCProjekt();

					// Fehlmengen
					session2 = FLRSessionFactory.getFactory().openSession();

					String sQueryFehlmengen = "SELECT sum(fm.n_menge)  FROM FLRFehlmenge fm WHERE fm.flrlossollmaterial.flrlos.i_id="
							+ los_i_id + " AND fm.artikel_i_id=" + artikelIId;

					Query queryFehlmengen = session2.createQuery(sQueryFehlmengen);

					if (queryFehlmengen.list().iterator().hasNext()) {
						oZeile[REPORT_FREIINFERTIGUNG_FEHLMENGEN] = queryFehlmengen.list().iterator().next();
					}
					session2.close();

					alDaten.add(oZeile);
				}

			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}

		}
		session.close();

		data = new Object[alDaten.size()][REPORT_FREIINFERTIGUNG_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(data);

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		ArtikelDto dto = getArtikelFac().artikelFindByPrimaryKey(artikelIId, theClientDto);
		parameter.put("P_ARTIKEL", dto.formatArtikelbezeichnung());
		parameter.put("P_ARTIKELREFERENZNUMMER", dto.getCReferenznr());

		index = -1;
		sAktuellerReport = ArtikelReportFac.REPORT_FREIINFERTIGUNG;

		initJRDS(parameter, ArtikelReportFac.REPORT_MODUL, ArtikelReportFac.REPORT_FREIINFERTIGUNG,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printArtikelstammblatt(Integer artikelIId, TheClientDto theClientDto) {
		// Erstellung des Reports
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		index = -1;
		sAktuellerReport = ArtikelReportFac.REPORT_ARTIKELSTAMMBLATT;
		data = new Object[1][REPORT_ARTIKELSTAMMBLATT_ANZAHL_SPALTEN];

		try {

			boolean darfEinkaufspreisSehen = false;
			boolean darfVerkaufspreisSehen = false;

			darfEinkaufspreisSehen = getTheJudgeFac().hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_EINKAUF,
					theClientDto);
			darfVerkaufspreisSehen = getTheJudgeFac().hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF,
					theClientDto);

			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(artikelIId, theClientDto);

			ArtikelDto artikelDtoZugehoerig = null;
			if (artikelDto.getArtikelIIdZugehoerig() != null) {
				artikelDtoZugehoerig = getArtikelFac().artikelFindByPrimaryKey(artikelDto.getArtikelIIdZugehoerig(),
						theClientDto);
			}

			data[0][REPORT_ARTIKELSTAMMBLATT_ARTIKELNUMMER] = artikelDto.getCNr();
			data[0][REPORT_ARTIKELSTAMMBLATT_REFERENZNUMMER] = artikelDto.getCReferenznr();

			byte[] bild = getArtikelkommentarFac().getArtikeldefaultBild(artikelIId, theClientDto);

			if (bild != null) {
				java.awt.Image myImage = Helper.byteArrayToImage(bild);
				data[0][REPORT_ARTIKELSTAMMBLATT_BILD] = myImage;
			}

			if (artikelDto.getArtikelsprDto() != null) {
				data[0][REPORT_ARTIKELSTAMMBLATT_BEZEICHNUNG] = artikelDto.getArtikelsprDto().getCBez();
				data[0][REPORT_ARTIKELSTAMMBLATT_ZUSATZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCZbez();
				data[0][REPORT_ARTIKELSTAMMBLATT_ZUSATZBEZEICHNUNG2] = artikelDto.getArtikelsprDto().getCZbez2();
				data[0][REPORT_ARTIKELSTAMMBLATT_KURZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCKbez();
			}

			if (artikelDtoZugehoerig != null) {
				data[0][REPORT_ARTIKELSTAMMBLATT_ZUGEHOERIGER_ARTIKELNUMMER] = artikelDtoZugehoerig.getCNr();
				if (artikelDtoZugehoerig.getArtikelsprDto() != null) {
					data[0][REPORT_ARTIKELSTAMMBLATT_ZUGEHOERIGER_BEZEICHNUNG] = artikelDtoZugehoerig.getArtikelsprDto()
							.getCBez();
					data[0][REPORT_ARTIKELSTAMMBLATT_ZUGEHOERIGER_ZUSATZBEZEICHNUNG] = artikelDtoZugehoerig
							.getArtikelsprDto().getCZbez();
					data[0][REPORT_ARTIKELSTAMMBLATT_ZUGEHOERIGER_ZUSATZBEZEICHNUNG2] = artikelDtoZugehoerig
							.getArtikelsprDto().getCZbez2();
					data[0][REPORT_ARTIKELSTAMMBLATT_ZUGEHOERIGER_KURZBEZEICHNUNG] = artikelDtoZugehoerig
							.getArtikelsprDto().getCKbez();
				}
			}

			ArtikellieferantDto[] artikellieferantDtos = getArtikelFac().artikellieferantFindByArtikelIId(artikelIId,
					theClientDto);

			if (artikellieferantDtos != null && artikellieferantDtos.length > 0) {
				data[0][REPORT_ARTIKELSTAMMBLATT_LIEFERANT] = getLieferantFac()
						.lieferantFindByPrimaryKey(artikellieferantDtos[0].getLieferantIId(), theClientDto)
						.getPartnerDto().formatAnrede();
				data[0][REPORT_ARTIKELSTAMMBLATT_ARTIKELNUMMERLIEFERANT] = artikellieferantDtos[0]
						.getCArtikelnrlieferant();
			}

			if (artikelDto.getMaterialIId() != null) {
				data[0][REPORT_ARTIKELSTAMMBLATT_MATERIAL] = getMaterialFac()
						.materialFindByPrimaryKey(artikelDto.getMaterialIId(), theClientDto).getCNr();
			}
			data[0][REPORT_ARTIKELSTAMMBLATT_GEWICHT] = artikelDto.getFGewichtkg();
			data[0][REPORT_ARTIKELSTAMMBLATT_WARENVERKEHRSNUMMER] = artikelDto.getCWarenverkehrsnummer();
			data[0][REPORT_ARTIKELSTAMMBLATT_ECCN] = artikelDto.getCEccn();
			data[0][REPORT_ARTIKELSTAMMBLATT_EAN] = artikelDto.getCVerkaufseannr();
			data[0][REPORT_ARTIKELSTAMMBLATT_VERPACKUNGSEINHEIT] = artikelDto.getFVerpackungsmenge();
			data[0][REPORT_ARTIKELSTAMMBLATT_LAGERSTAND] = getLagerFac()
					.getLagerstandAllerLagerEinesMandanten(artikelDto.getIId(), theClientDto);

			data[0][REPORT_ARTIKELSTAMMBLATT_LAGERBEWIRTSCHAFTET] = artikelDto.isLagerbewirtschaftet();
			data[0][REPORT_ARTIKELSTAMMBLATT_LAGERPLATZ] = getLagerFac()
					.getLagerplaezteEinesArtikels(artikelDto.getIId(), null);

			data[0][REPORT_ARTIKELSTAMMBLATT_RESERVIERT] = getReservierungFac()
					.getAnzahlReservierungen(artikelDto.getIId(), theClientDto);
			data[0][REPORT_ARTIKELSTAMMBLATT_FEHLMENGE] = getFehlmengeFac()
					.getAnzahlFehlmengeEinesArtikels(artikelDto.getIId(), theClientDto);
			data[0][REPORT_ARTIKELSTAMMBLATT_INFERTIGUNG] = getFertigungFac().getAnzahlInFertigung(artikelIId,
					theClientDto);
			data[0][REPORT_ARTIKELSTAMMBLATT_BESTELLT] = getArtikelbestelltFac().getAnzahlBestellt(artikelDto.getIId());
			data[0][REPORT_ARTIKELSTAMMBLATT_RAHMENRESERVIERT] = getReservierungFac()
					.getAnzahlRahmenreservierungen(artikelDto.getIId(), theClientDto);

			if (artikelDto.getVerpackungsmittelIId() != null) {
				VerpackungsmittelDto verpackungsmittelDto = getArtikelFac().verpackungsmittelFindByPrimaryKey(
						artikelDto.getVerpackungsmittelIId(),

						theClientDto);
				data[0][REPORT_ARTIKELSTAMMBLATT_VERPACKUNGSMITTEL_KENNUNG] = verpackungsmittelDto.getCNr();

				data[0][REPORT_ARTIKELSTAMMBLATT_VERPACKUNGSMITTEL_BEZEICHNUNG] = verpackungsmittelDto.getBezeichnung();

				data[0][REPORT_ARTIKELSTAMMBLATT_VERPACKUNGSMITTEL_GEWICHT_IN_KG] = verpackungsmittelDto
						.getNGewichtInKG();
			}

			data[0][REPORT_ARTIKELSTAMMBLATT_EINHEIT] = artikelDto.getEinheitCNr();

			data[0][REPORT_ARTIKELSTAMMBLATT_VERPACKUNGSMITTELMENGE] = artikelDto.getNVerpackungsmittelmenge();

			data[0][REPORT_ARTIKELSTAMMBLATT_BESTELLMENGENEINHEIT] = artikelDto.getEinheitCNrBestellung();

			data[0][REPORT_ARTIKELSTAMMBLATT_UMRECHNUNGSFAKTOR] = artikelDto.getNUmrechnungsfaktor();

			BigDecimal rahmenbestellt = null;
			Hashtable htAnzahlRahmenbestellt = getArtikelbestelltFac().getAnzahlRahmenbestellt(artikelDto.getIId(),
					theClientDto);
			if (htAnzahlRahmenbestellt.containsKey(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL)) {
				rahmenbestellt = (BigDecimal) htAnzahlRahmenbestellt.get(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL);
				data[0][REPORT_ARTIKELSTAMMBLATT_RAHMENBESTELLT] = rahmenbestellt;
			}

			data[0][REPORT_ARTIKELSTAMMBLATT_DETAILBEDARF] = getRahmenbedarfeFac()
					.getSummeAllerRahmenbedarfeEinesArtikels(artikelDto.getIId());

			LagerDto[] lagerDtos = getLagerFac().lagerFindByMandantCNr(theClientDto.getMandant());

			Object[][] dataSub = new Object[lagerDtos.length][3];
			String[] fieldnames = new String[] { "F_LAGER", "F_LAGERSTAND", "F_LAGERART" };

			for (int i = 0; i < lagerDtos.length; i++) {
				dataSub[i][0] = lagerDtos[i].getCNr();
				if (artikelDto.isLagerbewirtschaftet()) {
					dataSub[i][1] = getLagerFac().getLagerstand(artikelDto.getIId(), lagerDtos[i].getIId(),
							theClientDto);
				}
				dataSub[i][2] = lagerDtos[i].getLagerartCNr();
			}

			data[0][REPORT_ARTIKELSTAMMBLATT_SUBREPORT_LAGERSTAENDE] = new LPDatenSubreport(dataSub, fieldnames);

			if (darfEinkaufspreisSehen) {

				ArtikellieferantDto liefrant = getArtikelFac().getArtikelEinkaufspreisDesBevorzugtenLieferanten(
						artikelDto.getIId(), new BigDecimal(1), theClientDto.getSMandantenwaehrung(), theClientDto);

				if (liefrant != null) {
					data[0][REPORT_ARTIKELSTAMMBLATT_EINKAUFSPREIS] = liefrant.getLief1Preis();
				}
			}
			if (darfVerkaufspreisSehen) {

				try {
					VkPreisfindungEinzelverkaufspreisDto dto = getVkPreisfindungFac().getArtikeleinzelverkaufspreis(
							artikelDto.getIId(), new java.sql.Date(System.currentTimeMillis()),
							theClientDto.getSMandantenwaehrung(), theClientDto);
					if (dto != null) {
						data[0][REPORT_ARTIKELSTAMMBLATT_VERKAUFSPREIS] = dto.getNVerkaufspreisbasis();
					}
				} catch (RemoteException ex1) {
					// NOTHING HERE
				}
			}
			ArtikelkommentarDto[] dtos = getArtikelkommentarFac().artikelkommentardruckFindByArtikelIIdBelegartCNr(
					artikelDto.getIId(), LocaleFac.BELEGART_ANGEBOT, theClientDto.getLocUiAsString(), theClientDto);

			if (dtos != null && dtos.length > 0) {

				for (int i = 0; i < dtos.length; i++) {
					if (dtos[i].getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML)
							&& dtos[i].getArtikelkommentarsprDto() != null) {
						data[0][REPORT_ARTIKELSTAMMBLATT_LANGTEXT] = dtos[0].getArtikelkommentarsprDto()
								.getXKommentar();
					}
					break;
				}
			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		parameter = new HashMap<String, Object>();
		parameter.put("P_WAEHRUNG", theClientDto.getSMandantenwaehrung());

		initJRDS(parameter, ArtikelReportFac.REPORT_MODUL, ArtikelReportFac.REPORT_ARTIKELSTAMMBLATT,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAuftragsseriennummern(Integer artikelIId, java.sql.Date dVon, java.sql.Date dBis,
			TheClientDto theClientDto) {

		sAktuellerReport = ArtikelReportFac.REPORT_AUFTRAGSSERIENNUMMERN;

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria bestelltliste = session.createCriteria(FLRAuftragseriennrn.class);
		bestelltliste.add(Restrictions.eq("artikel_i_id", artikelIId));

		bestelltliste.createAlias("flrauftragposition", "ap");
		bestelltliste.createAlias("ap.flrauftrag", "a");

		if (dVon != null) {
			bestelltliste.add(Restrictions.ge("a." + AuftragFac.FLR_AUFTRAG_D_BELEGDATUM, dVon));
		}
		if (dBis != null) {
			bestelltliste.add(Restrictions.lt("a." + AuftragFac.FLR_AUFTRAG_D_BELEGDATUM, dBis));
		}

		bestelltliste.addOrder(Order.desc("c_seriennr"));

		List<?> resultList = bestelltliste.list();
		Iterator<?> resultListIterator = resultList.iterator();

		data = new Object[resultList.size()][REPORT_AUFTRAGSERIENNR_ANZAHL_SPALTEN];
		int row = 0;
		while (resultListIterator.hasNext()) {
			FLRAuftragseriennrn auftser = (FLRAuftragseriennrn) resultListIterator.next();
			data[row][REPORT_AUFTRAGSERIENNR_SERIENNUMMER] = auftser.getC_seriennr();
			data[row][REPORT_AUFTRAGSERIENNR_AUFTRAG] = auftser.getFlrauftragposition().getFlrauftrag().getC_nr();
			data[row][REPORT_AUFTRAGSERIENNR_BELEGDATUM] = new Timestamp(
					auftser.getFlrauftragposition().getFlrauftrag().getT_belegdatum().getTime());
			data[row][REPORT_AUFTRAGSERIENNR_PROJEKT] = auftser.getFlrauftragposition().getFlrauftrag().getC_bez();

			String kunde = auftser.getFlrauftragposition().getFlrauftrag().getFlrkunde().getFlrpartner()
					.getC_name1nachnamefirmazeile1();
			if (auftser.getFlrauftragposition().getFlrauftrag().getFlrkunde().getFlrpartner()
					.getC_name2vornamefirmazeile2() != null) {
				kunde += " " + auftser.getFlrauftragposition().getFlrauftrag().getFlrkunde().getFlrpartner()
						.getC_name2vornamefirmazeile2();
			}

			data[row][REPORT_AUFTRAGSERIENNR_KUNDE] = kunde;
			row++;

		}

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		ArtikelDto dto = getArtikelFac().artikelFindByPrimaryKey(artikelIId, theClientDto);
		parameter.put("P_ARTIKEL", dto.formatArtikelbezeichnung());

		initJRDS(parameter, ArtikelReportFac.REPORT_MODUL, ArtikelReportFac.REPORT_AUFTRAGSSERIENNUMMERN,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printMakeOrBuy(Integer artikelgruppeIId, Integer artikelklasseIId, String artiklenrVon,
			String artiklenrBis, java.sql.Date dStichtagEK, java.sql.Date dZeitraumVonAblieferung,
			java.sql.Date dZeitraumBisAblieferung, boolean bMitVersteckten, TheClientDto theClientDto) {

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		if (artikelgruppeIId != null) {
			parameter.put("P_ARTIKELGRUPPE",
					getArtikelFac().artgruFindByPrimaryKey(artikelgruppeIId, theClientDto).getCNr());
		}

		if (artikelklasseIId != null) {
			parameter.put("P_ARTIKELKLASSE",
					getArtikelFac().artklaFindByPrimaryKey(artikelklasseIId, theClientDto).getCNr());
		}

		parameter.put("P_ARTIKELNRVON", artiklenrVon);
		parameter.put("P_ARTIKELNRBIS", artiklenrBis);
		parameter.put("P_MITVERSTECKTEN", new Boolean(bMitVersteckten));

		parameter.put("P_PREISGUELTIGKEIT_EK", dStichtagEK);

		parameter.put("P_ABLIEFERZEITRAUM_VON", dZeitraumVonAblieferung);
		parameter.put("P_ABLIEFERZEITRAUM_BIS", dZeitraumBisAblieferung);

		dZeitraumBisAblieferung = Helper.addiereTageZuDatum(dZeitraumBisAblieferung, 1);

		try {
			ParametermandantDto parameterDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKELWIEDERBESCHAFFUNGSZEIT);

			parameter.put("P_EINHEIT_WIEDERBESCHAFFUNGSZEIT", parameterDto.getCWert());
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		sAktuellerReport = ArtikelReportFac.REPORT_MAKE_OR_BUY;

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "  SELECT stkl, (SELECT sum(la.i_id) FROM FLRLosablieferung la WHERE la.flrlos.stueckliste_i_id=stkl.i_id AND la.t_aendern between '"
				+ Helper.formatDateWithSlashes(dZeitraumVonAblieferung) + "' AND '"
				+ Helper.formatDateWithSlashes(dZeitraumBisAblieferung)
				+ "') FROM FLRStueckliste AS stkl WHERE stkl.flrartikel.mandant_c_nr='" + theClientDto.getMandant()
				+ "' ";

		if (artiklenrVon != null) {
			queryString += " AND stkl.flrartikel.c_nr >='" + artiklenrVon + "'";
		}
		if (artiklenrBis != null) {
			queryString += " AND stkl.flrartikel.c_nr <='" + Helper.fitString2Length(artiklenrBis, 25, '_') + "'";
		}
		if (artikelgruppeIId != null) {
			queryString += " AND stkl.flrartikel.flrartikelgruppe.i_id=" + artikelgruppeIId + " ";
		}
		if (artikelklasseIId != null) {
			queryString += " AND stkl.flrartikel.flrartikelklasse.i_id=" + artikelklasseIId + " ";
		}

		queryString += " ORDER BY stkl.flrartikel.c_nr ASC";

		Query query = session.createQuery(queryString);
		List results = query.list();
		Iterator resultListIterator = results.iterator();

		ArrayList alDaten = new ArrayList();

		while (resultListIterator.hasNext()) {

			Object[] o = (Object[]) resultListIterator.next();

			FLRStueckliste stkl = (FLRStueckliste) o[0];

			Long lAnzahlAblieferungen = (Long) o[1];

			if (lAnzahlAblieferungen != null && lAnzahlAblieferungen > 0) {

				try {
					ArtikellieferantDto alDto = getArtikelFac().getArtikelEinkaufspreisDesBevorzugtenLieferantenZuDatum(
							stkl.getArtikel_i_id(), BigDecimal.ONE, theClientDto.getSMandantenwaehrung(), dStichtagEK,
							theClientDto);

					if (alDto != null) {

						ArtikelDto artikelDto = getArtikelFac()
								.artikelFindByPrimaryKeySmall(stkl.getFlrartikel().getI_id(), theClientDto);

						Object[] oZeile = new Object[REPORT_MAKE_OR_BUY_ANZAHL_SPALTEN];

						oZeile[REPORT_MAKE_OR_BUY_ARTIKEL] = artikelDto.getCNr();
						oZeile[REPORT_MAKE_OR_BUY_BEZEICHNUNG] = artikelDto.getCBezAusSpr();
						oZeile[REPORT_MAKE_OR_BUY_ZUSATZBEZEICHNUNG] = artikelDto.getCZBezAusSpr();
						oZeile[REPORT_MAKE_OR_BUY_ARTIKEL_EINHEIT] = artikelDto.getEinheitCNr();

						// Ablieferungen im Zeitraum
						Session session2 = FLRSessionFactory.getFactory().openSession();
						String queryAblieferungen = "SELECT la FROM FLRLosablieferung la WHERE la.flrlos.stueckliste_i_id="
								+ stkl.getI_id() + " AND la.t_aendern between '"
								+ Helper.formatDateWithSlashes(dZeitraumVonAblieferung) + "' AND '"
								+ Helper.formatDateWithSlashes(dZeitraumBisAblieferung)
								+ "')  ORDER BY la.t_aendern ASC";

						org.hibernate.Query ablieferungen = session2.createQuery(queryAblieferungen);

						List<?> resultList = ablieferungen.list();

						Iterator<?> resultListIteratorAbl = resultList.iterator();

						String[] fieldnames = new String[] { "Losnummer", "Ablieferdatum", "Abliefermenge",
								"Ablieferpreis" };
						ArrayList alAblieferungen = new ArrayList();

						while (resultListIteratorAbl.hasNext()) {
							FLRLosablieferung la = (FLRLosablieferung) resultListIteratorAbl.next();

							Object[] oZeileSub = new Object[fieldnames.length];

							oZeileSub[0] = la.getFlrlos().getC_nr();
							oZeileSub[1] = la.getT_aendern();
							oZeileSub[2] = la.getN_menge();
							oZeileSub[3] = la.getN_gestehungspreis();

							alAblieferungen.add(oZeileSub);

						}

						Object[][] dataSub = new Object[alAblieferungen.size()][fieldnames.length];
						dataSub = (Object[][]) alAblieferungen.toArray(dataSub);
						oZeile[REPORT_MAKE_OR_BUY_SUBREPORT_ABLIEFERUNGEN] = new LPDatenSubreport(dataSub, fieldnames);

						// EINKAUFSPREISE

						ArrayList alDatenEK = getDataLieferantenpreisvergleich(stkl.getArtikel_i_id(), dStichtagEK,
								theClientDto);

						String[] fieldnamesEK = new String[] { "Lieferant", "LieferantKbez", "NichtLieferbar",
								"Subreport" };
						oZeile[REPORT_MAKE_OR_BUY_SUBREPORT_LIEFERANTENPREISE] = new LPDatenSubreport(alDatenEK,
								fieldnamesEK);

						alDaten.add(oZeile);
					}

				} catch (RemoteException e1) {
					throwEJBExceptionLPRespectOld(e1);
				}

			}

		}

		Object[][] dataTemp = new Object[alDaten.size()][REPORT_MAKE_OR_BUY_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(dataTemp);

		sAktuellerReport = ArtikelReportFac.REPORT_MAKE_OR_BUY;

		initJRDS(parameter, ArtikelReportFac.REPORT_MODUL, ArtikelReportFac.REPORT_MAKE_OR_BUY,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printKundensonderkonditionen(Integer artikelgruppeIId, Integer artikelklasseIId,
			String artikelnrVon, String artikelnrBis, boolean bMitVersteckten,
			ReportKundensonderkoditionenSortierung sort, TheClientDto theClientDto) {

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		if (artikelgruppeIId != null) {
			parameter.put("P_ARTIKELGRUPPE",
					getArtikelFac().artgruFindByPrimaryKey(artikelgruppeIId, theClientDto).getCNr());
		}

		if (artikelklasseIId != null) {
			parameter.put("P_ARTIKELKLASSE",
					getArtikelFac().artklaFindByPrimaryKey(artikelklasseIId, theClientDto).getCNr());
		}

		parameter.put("P_ARTIKELNRVON", artikelnrVon);
		parameter.put("P_ARTIKELNRBIS", artikelnrBis);
		parameter.put("P_MITVERSTECKTEN", new Boolean(bMitVersteckten));

		Integer iPreisbasis = null;
		try {
			ParametermandantDto param = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_PREISBASIS_VERKAUF);
			iPreisbasis = (Integer) param.getCWertAsObject();
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		boolean darfVerkaufspreisSehen = getTheJudgeFac().hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF,
				theClientDto);

		sAktuellerReport = ArtikelReportFac.REPORT_KUNDENSONDERKONDITIONEN;

		Session sessionArtikel = FLRSessionFactory.getFactory().openSession();

		String queryString = "  SELECT a FROM FLRArtikel AS a WHERE a.mandant_c_nr='" + theClientDto.getMandant()
				+ "' ";

		if (artikelnrVon != null) {
			queryString += " AND a.c_nr >='" + artikelnrVon + "'";
		}
		if (artikelnrBis != null) {
			queryString += " AND a.c_nr <='" + Helper.fitString2Length(artikelnrBis, 25, '_') + "'";
		}
		if (artikelgruppeIId != null) {
			queryString += " AND a.flrartikelgruppe.i_id=" + artikelgruppeIId + " ";
		}
		if (artikelgruppeIId != null) {
			queryString += " AND a.flrartikelklasse.i_id=" + artikelklasseIId + " ";
		}

		queryString += " ORDER BY a.c_nr ASC";

		Query query = sessionArtikel.createQuery(queryString);
		List results = query.list();
		Iterator resultListIteratorArtikel = results.iterator();

		ArrayList alDaten = new ArrayList();

		while (resultListIteratorArtikel.hasNext()) {

			FLRArtikel flrArtikel = (FLRArtikel) resultListIteratorArtikel.next();

			ArtikelDto dto = getArtikelFac().artikelFindByPrimaryKey(flrArtikel.getI_id(), theClientDto);

			Integer artikelIId = dto.getIId();

			String sQuery = "SELECT s FROM FLRKundesokomengenstaffel s WHERE s.flrkundesoko.artikel_i_id="
					+ flrArtikel.getI_id();

			if (flrArtikel.getFlrartikelgruppe() != null) {
				sQuery += " OR s.flrkundesoko.artgru_i_id=" + flrArtikel.getFlrartikelgruppe().getI_id();
			}

			sQuery += " ORDER BY  s.flrkundesoko.flrkunde.flrpartner.c_name1nachnamefirmazeile1 ASC, s.n_menge ASC, s.flrkundesoko.t_preisgueltigab ASC";

			Session session = FLRSessionFactory.getFactory().openSession();

			Query inventurliste = session.createQuery(sQuery);

			List<?> resultList = inventurliste.list();

			Iterator<?> resultListIterator = resultList.iterator();

			while (resultListIterator.hasNext()) {
				FLRKundesokomengenstaffel flrKundesokomengenstaffel = (FLRKundesokomengenstaffel) resultListIterator
						.next();

				Object[] zeile = new Object[REPORT_KUNDENSONDERKONDITIONEN_ANZAHL_FELDER];

				zeile[REPORT_KUNDENSONDERKONDITIONEN_ARTIKELNUMMER] = dto.getCNr();
				zeile[REPORT_KUNDENSONDERKONDITIONEN_ARTIKELBEZEICHNUNG] = dto.formatArtikelbezeichnung();

				zeile[REPORT_KUNDENSONDERKONDITIONEN_KUNDE_NAME1] = flrKundesokomengenstaffel.getFlrkundesoko()
						.getFlrkunde().getFlrpartner().getC_name1nachnamefirmazeile1();
				zeile[REPORT_KUNDENSONDERKONDITIONEN_KUNDE_NAME2] = flrKundesokomengenstaffel.getFlrkundesoko()
						.getFlrkunde().getFlrpartner().getC_name2vornamefirmazeile2();
				zeile[REPORT_KUNDENSONDERKONDITIONEN_KUNDE_KBEZ] = flrKundesokomengenstaffel.getFlrkundesoko()
						.getFlrkunde().getFlrpartner().getC_kbez();
				
				zeile[REPORT_KUNDENSONDERKONDITIONEN_KUNDE_MANDANT] = flrKundesokomengenstaffel.getFlrkundesoko()
						.getFlrkunde().getMandant_c_nr();

				zeile[REPORT_KUNDENSONDERKONDITIONEN_KUNDE_UID] = flrKundesokomengenstaffel.getFlrkundesoko()
						.getFlrkunde().getFlrpartner().getC_uid();
				if (flrKundesokomengenstaffel.getFlrkundesoko().getFlrkunde().getFlrpartner()
						.getFlrlandplzort() != null) {
					zeile[REPORT_KUNDENSONDERKONDITIONEN_KUNDE_LKZ] = flrKundesokomengenstaffel.getFlrkundesoko()
							.getFlrkunde().getFlrpartner().getFlrlandplzort().getFlrland().getC_lkz();
					zeile[REPORT_KUNDENSONDERKONDITIONEN_KUNDE_PLZ] = flrKundesokomengenstaffel.getFlrkundesoko()
							.getFlrkunde().getFlrpartner().getFlrlandplzort().getC_plz();
					zeile[REPORT_KUNDENSONDERKONDITIONEN_KUNDE_ORT] = flrKundesokomengenstaffel.getFlrkundesoko()
							.getFlrkunde().getFlrpartner().getFlrlandplzort().getFlrort().getC_name();
				}

				zeile[REPORT_KUNDENSONDERKONDITIONEN_KUNDE_MENGE] = flrKundesokomengenstaffel.getN_menge();
				zeile[REPORT_KUNDENSONDERKONDITIONEN_KUNDE_KDARTIKELNUMMER] = flrKundesokomengenstaffel
						.getFlrkundesoko().getC_kundeartikelnummer();

				try {
					KundesokoDto kdsokoDto = getKundesokoFac()
							.kundesokoFindByPrimaryKey(flrKundesokomengenstaffel.getFlrkundesoko().getI_id());

					zeile[REPORT_KUNDENSONDERKONDITIONEN_KUNDE_WIRKT_NICHT_IN_PREISFINDUNG] = Helper
							.short2Boolean(kdsokoDto.getBWirktNichtFuerPreisfindung());

					zeile[REPORT_KUNDENSONDERKONDITIONEN_KUNDE_KDARTIKELNUMMER] = flrKundesokomengenstaffel
							.getFlrkundesoko().getC_kundeartikelnummer();
					zeile[REPORT_KUNDENSONDERKONDITIONEN_KUNDE_KDARTIKELBEZ] = kdsokoDto.getCKundeartikelbez();
					zeile[REPORT_KUNDENSONDERKONDITIONEN_KUNDE_KDARTIKELBEZ2] = kdsokoDto.getCKundeartikelzbez();

					// Preis berechnen
					BigDecimal nBerechneterPreis = null;
					BigDecimal nBerechneterPreisKundenwaehrung = null;
					BigDecimal nBerechneterPreisHeute = null;

					if (kdsokoDto.getArtikelIId() != null || kdsokoDto.getArtgruIId() != null) {
						// der Preis muss an dieser Stelle berechnet werden

						if (flrKundesokomengenstaffel.getN_fixpreis() != null) {
							nBerechneterPreis = flrKundesokomengenstaffel.getN_fixpreis();
							nBerechneterPreisHeute = flrKundesokomengenstaffel.getN_fixpreis();
							// Fixpreis ist in Kundenwaehrung -> nach
							// Mandantenwehrung umrechnen
							KundeDto kdDto = getKundeFac().kundeFindByPrimaryKeySmall(
									flrKundesokomengenstaffel.getFlrkundesoko().getFlrkunde().getI_id());

							try {
								if (!kdDto.getCWaehrung().equals(theClientDto.getSMandantenwaehrung())) {
									nBerechneterPreis = getLocaleFac().rechneUmInAndereWaehrungZuDatum(
											flrKundesokomengenstaffel.getN_fixpreis(), kdDto.getCWaehrung(),
											theClientDto.getSMandantenwaehrung(),
											Helper.cutDate(new java.sql.Date(System.currentTimeMillis())),
											theClientDto);
									nBerechneterPreisKundenwaehrung = flrKundesokomengenstaffel.getN_fixpreis();
								}
							} catch (RemoteException e) {
								throwEJBExceptionLPRespectOld(e);
							}

						} else {
							// WH 21.06.06 Es gilt die VK-Basis, die zu Beginn der
							// Mengenstaffel gueltig ist
							BigDecimal nPreisbasis = null;
							BigDecimal nPreisbasisKundenwaehrung = null;
							BigDecimal nPreisbasisHeute = null;
							if (iPreisbasis == 0 || iPreisbasis == 2) {

								nPreisbasis = getVkPreisfindungFac().ermittlePreisbasis(artikelIId,
										new java.sql.Date(kdsokoDto.getTPreisgueltigab().getTime()), null,
										theClientDto.getSMandantenwaehrung(), theClientDto);

								nPreisbasisKundenwaehrung = getVkPreisfindungFac().ermittlePreisbasis(artikelIId,
										new java.sql.Date(kdsokoDto.getTPreisgueltigab().getTime()), null,
										flrKundesokomengenstaffel.getFlrkundesoko().getFlrkunde().getWaehrung_c_nr(),
										theClientDto);
								nPreisbasisHeute = getVkPreisfindungFac().ermittlePreisbasis(artikelIId,
										new java.sql.Date(System.currentTimeMillis()), null,
										theClientDto.getSMandantenwaehrung(), theClientDto);
							} else {
								nPreisbasis = getVkPreisfindungFac().ermittlePreisbasis(artikelIId,
										new java.sql.Date(kdsokoDto.getTPreisgueltigab().getTime()),
										flrKundesokomengenstaffel.getFlrkundesoko().getFlrkunde()
												.getVkpfartikelpreisliste_i_id_stdpreisliste(),
										theClientDto.getSMandantenwaehrung(), theClientDto);
								nPreisbasisKundenwaehrung = getVkPreisfindungFac().ermittlePreisbasis(artikelIId,
										new java.sql.Date(kdsokoDto.getTPreisgueltigab().getTime()),
										flrKundesokomengenstaffel.getFlrkundesoko().getFlrkunde()
												.getVkpfartikelpreisliste_i_id_stdpreisliste(),
										flrKundesokomengenstaffel.getFlrkundesoko().getFlrkunde().getWaehrung_c_nr(),
										theClientDto);
								nPreisbasisHeute = getVkPreisfindungFac().ermittlePreisbasis(artikelIId,
										new java.sql.Date(System.currentTimeMillis()),
										flrKundesokomengenstaffel.getFlrkundesoko().getFlrkunde()
												.getVkpfartikelpreisliste_i_id_stdpreisliste(),
										theClientDto.getSMandantenwaehrung(), theClientDto);
							}

							VerkaufspreisDto vkpfDto = getVkPreisfindungFac().berechneVerkaufspreis(nPreisbasis,
									flrKundesokomengenstaffel.getF_rabattsatz(), theClientDto);

							if (vkpfDto != null) {
								nBerechneterPreis = vkpfDto.nettopreis;
							} else {
								// Wahrscheinlich keine VK-Preisbasis verfuegbar
								VkPreisfindungEinzelverkaufspreisDto[] dtos = getVkPreisfindungFac()
										.vkPreisfindungEinzelverkaufspreisfindByMandantCNrArtikelIIdAbGueltigab(
												artikelIId, new java.sql.Date(kdsokoDto.getTPreisgueltigab().getTime()),
												theClientDto);
								if (dtos.length > 0) {
									zeile[REPORT_KUNDENSONDERKONDITIONEN_KUNDE_NAECHSTE_VKPREISBASIS_GUELTIG_AB] = dtos[0]
											.getTVerkaufspreisbasisgueltigab();
								}

							}

							VerkaufspreisDto vkpfDtoKundenwaehrung = getVkPreisfindungFac().berechneVerkaufspreis(
									nPreisbasisKundenwaehrung, flrKundesokomengenstaffel.getF_rabattsatz(),
									theClientDto);
							if (vkpfDto != null) {
								nBerechneterPreisKundenwaehrung = vkpfDtoKundenwaehrung.nettopreis;
							}

							VerkaufspreisDto vkpfDtoHeute = getVkPreisfindungFac().berechneVerkaufspreis(
									nPreisbasisHeute, flrKundesokomengenstaffel.getF_rabattsatz(), theClientDto);

							if (vkpfDtoHeute != null) {
								nBerechneterPreisHeute = vkpfDtoHeute.nettopreis;
							}

						}
					}
					if (darfVerkaufspreisSehen) {

						zeile[REPORT_KUNDENSONDERKONDITIONEN_KUNDE_BERECHNETER_PREIS] = nBerechneterPreis;

						if (flrKundesokomengenstaffel.getFlrkundesoko().getT_preisgueltigbis() == null
								|| flrKundesokomengenstaffel.getFlrkundesoko().getT_preisgueltigbis()
										.after(new java.sql.Date(System.currentTimeMillis()))) {
							zeile[REPORT_KUNDENSONDERKONDITIONEN_KUNDE_BERECHNETER_PREIS_ZU_HEUTE] = nBerechneterPreisHeute;

						}

						zeile[REPORT_KUNDENSONDERKONDITIONEN_KUNDE_BERECHNETER_PREIS_KDWAEHRUNG] = nBerechneterPreisKundenwaehrung;
					}
					zeile[REPORT_KUNDENSONDERKONDITIONEN_KUNDE_KDWAEHRUNG] = flrKundesokomengenstaffel.getFlrkundesoko()
							.getFlrkunde().getWaehrung_c_nr();

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

				zeile[REPORT_KUNDENSONDERKONDITIONEN_KUNDE_GUELTIAGAB] = flrKundesokomengenstaffel.getFlrkundesoko()
						.getT_preisgueltigab();
				zeile[REPORT_KUNDENSONDERKONDITIONEN_KUNDE_GUELTIAGBIS] = flrKundesokomengenstaffel.getFlrkundesoko()
						.getT_preisgueltigbis();
				if (darfVerkaufspreisSehen) {
					zeile[REPORT_KUNDENSONDERKONDITIONEN_KUNDE_FIXPREIS] = flrKundesokomengenstaffel.getN_fixpreis();
					zeile[REPORT_KUNDENSONDERKONDITIONEN_KUNDE_RABATT] = flrKundesokomengenstaffel.getF_rabattsatz();
				}

				alDaten.add(zeile);
			}

		}

		// Sortieren
		// Nach Kunde oder Artikelnummer
		for (int i = alDaten.size() - 1; i > 0; --i) {
			for (int j = 0; j < i; ++j) {

				Object[] zeilea = (Object[]) alDaten.get(j);
				Object[] zeileb = (Object[]) alDaten.get(j + 1);

				String artikelnummerA = (String) zeilea[REPORT_KUNDENSONDERKONDITIONEN_ARTIKELNUMMER];
				String artikelnummerB = (String) zeileb[REPORT_KUNDENSONDERKONDITIONEN_ARTIKELNUMMER];

				String kundeA = (String) zeilea[REPORT_KUNDENSONDERKONDITIONEN_KUNDE_NAME1];
				String kundeB = (String) zeileb[REPORT_KUNDENSONDERKONDITIONEN_KUNDE_NAME1];

				String a = null;
				String b = null;
				if (sort == ReportKundensonderkoditionenSortierung.SORT_ARTIKELNUMMER) {
					parameter.put("P_SORTIERT_NACH_KUNDE", new Boolean(false));

					a = Helper.fitString2Length(artikelnummerA, 25, ' ') + kundeA;
					b = Helper.fitString2Length(artikelnummerB, 25, ' ') + kundeB;
				} else if (sort == ReportKundensonderkoditionenSortierung.SORT_KUNDE) {
					parameter.put("P_SORTIERT_NACH_KUNDE", new Boolean(true));
					a = Helper.fitString2Length(kundeA, 40, ' ') + artikelnummerA;
					b = Helper.fitString2Length(kundeB, 40, ' ') + artikelnummerB;
				}

				if (a.compareTo(b) > 0) {
					Object[] h = zeilea;
					alDaten.set(j, zeileb);
					alDaten.set(j + 1, h);
				}
			}
		}

		Object[][] returnArray = new Object[alDaten.size()][REPORT_KUNDENSONDERKONDITIONEN_ANZAHL_FELDER];
		data = (Object[][]) alDaten.toArray(returnArray);

		initJRDS(parameter, ArtikelReportFac.REPORT_MODUL, ArtikelReportFac.REPORT_KUNDENSONDERKONDITIONEN,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printBewegungsvorschauAlle(Integer artikelgruppeIId, Integer artikelklasseIId, String artiklenrVon, String artiklenrBis,
			Integer partnerIIdStandort, boolean bMitRahmen, boolean bArtikelOhneBewegungsvorschauAusblenden,
			boolean bMitVersteckten, TheClientDto theClientDto) {

		Session session = FLRSessionFactory.getFactory().openSession();

		// SP6794
		String eingeloggterMandant = theClientDto.getMandant();
		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
				theClientDto)) {
			eingeloggterMandant = getSystemFac().getHauptmandant();
		}

		String queryString = "  SELECT artikelliste.i_id FROM FLRArtikelliste AS artikelliste WHERE artikelliste.b_lagerbewirtschaftet=1 AND artikelliste.mandant_c_nr='"
				+ eingeloggterMandant + "'  AND artikelliste.artikelart_c_nr NOT IN ('"
				+ ArtikelFac.ARTIKELART_HANDARTIKEL + "') ";

		if (artiklenrVon != null) {
			queryString += " AND artikelliste.c_nr >='" + artiklenrVon + "'";
		}
		if (artiklenrBis != null) {
			queryString += " AND artikelliste.c_nr <='" + Helper.fitString2Length(artiklenrBis, 25, '_') + "'";
		}
		if (artikelgruppeIId != null) {
			queryString += " AND artikelliste.flrartikelgruppe.i_id=" + artikelgruppeIId + " ";
		}
		
		if (artikelklasseIId != null) {
			queryString += " AND artikelliste.flrartikelklasse.i_id=" + artikelklasseIId + " ";
		}

		if (bMitVersteckten == false) {
			queryString += " AND artikelliste.b_versteckt=0";
		}

		queryString += " ORDER BY artikelliste.c_nr ASC";

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		if (artikelgruppeIId != null) {
			parameter.put("P_ARTIKELGRUPPE",
					getArtikelFac().artgruFindByPrimaryKey(artikelgruppeIId, theClientDto).getCNr());
		}
		if (artikelklasseIId != null) {
			parameter.put("P_ARTIKELKLASSE",
					getArtikelFac().artklaFindByPrimaryKey(artikelklasseIId, theClientDto).getBezeichnung());
		}

		parameter.put("P_ARTIKELNRVON", artiklenrVon);
		parameter.put("P_ARTIKELNRBIS", artiklenrBis);
		parameter.put("P_MITVERSTECKTEN", new Boolean(bMitVersteckten));
		parameter.put("P_ARTIKEL_OHNE_BEWEGUNGSVORSCHAU_AUSBLENDEN",
				new Boolean(bArtikelOhneBewegungsvorschauAusblenden));
		parameter.put("P_MIT_RAHMEN", new Boolean(bMitRahmen));

		Query query = session.createQuery(queryString);
		List results = query.list();
		Iterator resultListIterator = results.iterator();

		ArrayList alDaten = new ArrayList();

		Integer varianteIId = theClientDto.getReportvarianteIId();
		while (resultListIterator.hasNext()) {
			Integer artikelId = (Integer) resultListIterator.next();

			ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(artikelId, theClientDto);
			Object[] oZeile = new Object[REPORT_BEWEGUNGSVORSCHAU_ALLE_ANZAHL_FELDER];

			oZeile[REPORT_BEWEGUNGSVORSCHAU_ALLE_ARTIKELNUMMER] = aDto.getCNr();
			oZeile[REPORT_BEWEGUNGSVORSCHAU_ALLE_ARTIKELBEZEICHNUNG] = aDto.getCBezAusSpr();
			oZeile[REPORT_BEWEGUNGSVORSCHAU_ALLE_ARTIKELZUSATZBEZEICHNUNG] = aDto.getCZBezAusSpr();
			oZeile[REPORT_BEWEGUNGSVORSCHAU_ALLE_ARTIKELZUSATZBEZEICHNUNG2] = aDto.getCZBez2AusSpr();

			theClientDto.setReportvarianteIId(varianteIId);
			JasperPrintLP printBew = printBewegungsvorschau(artikelId, true, partnerIIdStandort, bMitRahmen, false,
					theClientDto);

			Map parameterSUB = printBew.getMapParameters();
			Integer iAnzahl = (Integer) parameterSUB.get("P_ANZAHL_DATENSAETZE_BEWEGUNGSVORSCHAU");

			if (bArtikelOhneBewegungsvorschauAusblenden) {
				if (iAnzahl == 0) {
					continue;
				}
			}

			oZeile[REPORT_BEWEGUNGSVORSCHAU_ALLE_SUBREPORT_BEWEGUNGSVORSCHAU] = printBew.transformToSubreport();
			oZeile[REPORT_BEWEGUNGSVORSCHAU_ALLE_SUBREPORT_BEWEGUNGSVORSCHAU_PARAMETER] = printBew.getMapParameters();

			alDaten.add(oZeile);

		}

		sAktuellerReport = ArtikelReportFac.REPORT_BEWEGUNGSVORSCHAU_ALLE;

		Object[][] returnArray = new Object[alDaten.size()][REPORT_BEWEGUNGSVORSCHAU_ALLE_ANZAHL_FELDER];
		data = (Object[][]) alDaten.toArray(returnArray);

		initJRDS(parameter, ArtikelReportFac.REPORT_MODUL, ArtikelReportFac.REPORT_BEWEGUNGSVORSCHAU_ALLE,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printBewegungsvorschau(Integer artikelId, boolean bInternebestellungMiteinbeziehen,
			Integer partnerIIdStandort, boolean bMitRahmen, boolean bUeberAlleMandanten, TheClientDto theClientDto) {

		HashMap<String, Object> parameter = new HashMap<String, Object>();
		boolean bAuftragStundenMinuten = false;
		boolean bAuftragsfreigabe = false;
		boolean bDruckMitLagerstaendenAllerMandanten = false;
		boolean bZentralerArtikelstamm = getMandantFac()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, theClientDto);

		parameter.put("P_ES_SIND_MEHRERE_MANDANTEN_BETEILIGT", Boolean.FALSE);
		parameter.put("P_UEBERALLEMANDANTEN", bUeberAlleMandanten);

		try {

			if (partnerIIdStandort != null) {
				parameter.put("P_STANDORT",
						getPartnerFac().partnerFindByPrimaryKey(partnerIIdStandort, theClientDto).getCKbez());
			}
			ArtikelDto dto = getArtikelFac().artikelFindByPrimaryKey(artikelId, theClientDto);
			parameter.put("P_ARTIKEL", dto.formatArtikelbezeichnung());
			parameter.put("P_ARTIKELREFERENZNUMMER", dto.getCReferenznr());
			parameter.put("P_ARTIKELEINHEIT", dto.getEinheitCNr().trim());
			parameter.put("P_BESTELLEINHEIT", dto.getEinheitCNrBestellung());
			parameter.put("P_MULTIPLIKATORBESTELLMENGE", dto.getNUmrechnungsfaktor());
			parameter.put("P_BESTELLEINHEIT_INVERS", Helper.short2Boolean(dto.getbBestellmengeneinheitInvers()));
			parameter.put("P_UEBERPRODUKTION", dto.getFUeberproduktion());

			// PJ20062

			ParametermandantDto parameterDtoWBZ = null;
			try {
				parameterDtoWBZ = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKELWIEDERBESCHAFFUNGSZEIT);
			} catch (RemoteException e1) {
				throwEJBExceptionLPRespectOld(e1);
			}

			parameter.put("P_EINHEIT_WIEDERBESCHAFFUNGSZEIT", parameterDtoWBZ.getCWert());

			ArtikellieferantDto alDto = getArtikelFac().getArtikelEinkaufspreisDesBevorzugtenLieferanten(artikelId,
					BigDecimal.ONE, theClientDto.getSMandantenwaehrung(), theClientDto);
			if (alDto != null) {

				parameter.put("P_WIEDERBESCHAFFUNGSZEIT_LIEF1", alDto.getIWiederbeschaffungszeit());

				LieferantDto lfDto = getLieferantFac().lieferantFindByPrimaryKey(alDto.getLieferantIId(), theClientDto);

				parameter.put("P_LIEF1", lfDto.getPartnerDto().formatFixName1Name2());
				parameter.put("P_LIEF1_ARTIKELNUMMER", alDto.getCArtikelnrlieferant());
				parameter.put("P_LIEF1_ARTIKELBEZEICHNUNG", alDto.getCBezbeilieferant());

			}

			ArtikellieferantDto[] alDtos = getArtikelFac().artikellieferantfindByArtikelIIdTPreisgueltigab(artikelId,
					new java.sql.Date(System.currentTimeMillis()), theClientDto);

			Integer iWbzSchnellste = null;
			for (int i = 0; i < alDtos.length; i++) {
				Integer iWbz = alDtos[i].getIWiederbeschaffungszeit();

				if (iWbz != null && iWbzSchnellste == null) {
					iWbzSchnellste = iWbz;
				}

				if (iWbz != null && iWbzSchnellste != null && iWbz.intValue() < iWbzSchnellste.intValue()) {
					iWbzSchnellste = iWbz;
				}

			}

			parameter.put("P_WIEDERBESCHAFFUNGSZEIT_SCHNELLSTE", iWbzSchnellste);

			if (lagerMinJeLager(theClientDto)) {

				BigDecimal[] bd = getLagerFac().getSummeLagermindesUndLagerSollstandEinesStandorts(artikelId,
						partnerIIdStandort, theClientDto);

				parameter.put("P_LAGERSOLLSTAND", new Double(bd[1].doubleValue()));
				parameter.put("P_LAGERMINDESTSTAND", new Double(bd[0].doubleValue()));

			} else {
				parameter.put("P_LAGERSOLLSTAND", dto.getFLagersoll());
				parameter.put("P_LAGERMINDESTSTAND", dto.getFLagermindest());

			}

			parameter.put("P_MIT_RAHMEN", new Boolean(bMitRahmen));

			ParametermandantDto parameterDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_AUFTRAGSFREIGABE);

			bAuftragsfreigabe = ((Boolean) parameterDto.getCWertAsObject());

			parameter.put("P_PARAMETER_AUFTRAGSFREIGABE", new Boolean(bAuftragsfreigabe));

			parameterDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_DRUCK_BEWEGUNGSVORSCHAU_LAGER_ALLER_MANDANTEN);

			bDruckMitLagerstaendenAllerMandanten = ((Boolean) parameterDto.getCWertAsObject());

			parameter.put("P_DRUCK_BEWEGUNGSVORSCHAU_LAGER_ALLER_MANDANTEN",
					new Boolean(bDruckMitLagerstaendenAllerMandanten));

			parameter.put("P_FERTIGUNGSSATZGROESSE", dto.getFFertigungssatzgroesse());
			ZusatzfunktionberechtigungDto zusatzfunktionberechtigungDto = getMandantFac()
					.zusatzfunktionberechtigungFindByPrimaryKey(MandantFac.ZUSATZFUNKTION_AUFTRAGTERMIN_STUNDEN_MINUTEN,
							theClientDto.getMandant());
			if (zusatzfunktionberechtigungDto != null) {
				bAuftragStundenMinuten = true;
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		index = -1;
		sAktuellerReport = ArtikelReportFac.REPORT_BEWEGUNGSVORSCHAU;

		ArrayList alDaten = new ArrayList();

		try {
			// SP 1008 OHNE Internebestellung
			ArrayList<?> list = getInternebestellungFac().getBewegungsvorschauSortiert(artikelId, false, false,
					theClientDto, partnerIIdStandort, bMitRahmen, bUeberAlleMandanten, true, null);

			parameter.put("P_ANZAHL_DATENSAETZE_BEWEGUNGSVORSCHAU", list.size());

			BewegungsvorschauDto[] returnArray = new BewegungsvorschauDto[list.size()];
			BewegungsvorschauDto[] dtos = (com.lp.server.bestellung.service.BewegungsvorschauDto[]) list
					.toArray(returnArray);
			LagerDto lagerDto = getLagerFac().getHauptlagerDesMandanten(theClientDto);
			LagerDto[] allelaegerDtos = null;

			if ((bZentralerArtikelstamm && !getMandantFac()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_GETRENNTE_LAGER, theClientDto))
					|| (bZentralerArtikelstamm
							&& getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
									MandantFac.ZUSATZFUNKTION_GETRENNTE_LAGER, theClientDto)
							&& bDruckMitLagerstaendenAllerMandanten)) {
				allelaegerDtos = getLagerFac().lagerFindAll();
			} else {
				allelaegerDtos = getLagerFac().lagerFindByMandantCNr(theClientDto.getMandant());
			}

			BigDecimal anfangslagerstand = new BigDecimal(0);
			if (Helper.short2boolean(lagerDto.getBBestellvorschlag())
					|| Helper.short2boolean(lagerDto.getBInternebestellung())) {

				// SP8090
				if (partnerIIdStandort == null || partnerIIdStandort.equals(lagerDto.getPartnerIIdStandort())) {
					anfangslagerstand = getLagerFac().getLagerstand(artikelId, lagerDto.getIId(), theClientDto);
				}
			}

			if (partnerIIdStandort == null || partnerIIdStandort.equals(lagerDto.getPartnerIIdStandort())) {
				Object[] zeile = new Object[REPORT_BEWEGUNGSVORSCHAU_ANZAHL_FELDER];

				zeile[REPORT_BEWEGUNGSVORSCHAU_BELEGART] = "Lagerstand";
				zeile[REPORT_BEWEGUNGSVORSCHAU_BELEGNR] = lagerDto.getCNr();
				zeile[REPORT_BEWEGUNGSVORSCHAU_LAGER_VERSTECKT] = Helper.short2Boolean(lagerDto.getBVersteckt());
				zeile[REPORT_BEWEGUNGSVORSCHAU_MANDANT] = lagerDto.getMandantCNr();
				if (lagerDto.getPartnerIIdStandort() != null) {
					zeile[REPORT_BEWEGUNGSVORSCHAU_STANDORT] = getPartnerFac()
							.partnerFindByPrimaryKey(lagerDto.getPartnerIIdStandort(), theClientDto).getCKbez();
				}

				zeile[REPORT_BEWEGUNGSVORSCHAU_PARTNER] = LagerFac.LAGERART_HAUPTLAGER.trim();
				zeile[REPORT_BEWEGUNGSVORSCHAU_MENGE] = getLagerFac().getLagerstand(artikelId, lagerDto.getIId(),
						theClientDto);
				zeile[REPORT_BEWEGUNGSVORSCHAU_FIKTIVERLAGERSTAND] = anfangslagerstand;
				zeile[REPORT_BEWEGUNGSVORSCHAU_BESTELLVORSCHLAG_BERUECKSICHTIGT_LAGER] = Helper
						.short2Boolean(lagerDto.getBBestellvorschlag());
				zeile[REPORT_BEWEGUNGSVORSCHAU_INTERNEBESTELLUNG_BERUECKSICHTIGT_LAGER] = Helper
						.short2Boolean(lagerDto.getBInternebestellung());

				alDaten.add(zeile);
			}
			if ((bZentralerArtikelstamm && !getMandantFac()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_GETRENNTE_LAGER, theClientDto))
					|| (bZentralerArtikelstamm
							&& getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
									MandantFac.ZUSATZFUNKTION_GETRENNTE_LAGER, theClientDto)
							&& bDruckMitLagerstaendenAllerMandanten == true)) {
				for (int i = 0; i < allelaegerDtos.length; i++) {
					LagerDto dto = allelaegerDtos[i];
					if (!dto.getIId().equals(lagerDto.getIId())) {
						Object[] zeile = new Object[REPORT_BEWEGUNGSVORSCHAU_ANZAHL_FELDER];
						zeile[REPORT_BEWEGUNGSVORSCHAU_BELEGART] = "Lagerstand";
						zeile[REPORT_BEWEGUNGSVORSCHAU_BELEGNR] = dto.getCNr();
						zeile[REPORT_BEWEGUNGSVORSCHAU_LAGER_VERSTECKT] = Helper.short2Boolean(dto.getBVersteckt());
						zeile[REPORT_BEWEGUNGSVORSCHAU_MANDANT] = dto.getMandantCNr();
						if (dto.getPartnerIIdStandort() != null) {
							zeile[REPORT_BEWEGUNGSVORSCHAU_STANDORT] = getPartnerFac()
									.partnerFindByPrimaryKey(dto.getPartnerIIdStandort(), theClientDto).getCKbez();
						}
						zeile[REPORT_BEWEGUNGSVORSCHAU_PARTNER] = dto.getLagerartCNr();
						BigDecimal bdLagerstandActLager = getLagerFac().getLagerstand(artikelId, dto.getIId(),
								theClientDto);
						zeile[REPORT_BEWEGUNGSVORSCHAU_MENGE] = bdLagerstandActLager;
						if (Helper.short2boolean(dto.getBBestellvorschlag())
								|| Helper.short2boolean(dto.getBInternebestellung())) {
							// Lagerstand der Laeger aendert sich nur wenn im
							// Artikel
							// beruecksichigen aktiviert wurde

							anfangslagerstand = anfangslagerstand.add(bdLagerstandActLager);

						}
						zeile[REPORT_BEWEGUNGSVORSCHAU_FIKTIVERLAGERSTAND] = anfangslagerstand;
						zeile[REPORT_BEWEGUNGSVORSCHAU_BESTELLVORSCHLAG_BERUECKSICHTIGT_LAGER] = Helper
								.short2Boolean(dto.getBBestellvorschlag());
						zeile[REPORT_BEWEGUNGSVORSCHAU_INTERNEBESTELLUNG_BERUECKSICHTIGT_LAGER] = Helper
								.short2Boolean(dto.getBInternebestellung());
						alDaten.add(zeile);
					}
				}
			} else {
				for (int i = 0; i < allelaegerDtos.length; i++) {
					LagerDto dto = allelaegerDtos[i];
					if (!dto.getLagerartCNr().equals(LagerFac.LAGERART_HAUPTLAGER)) {

						if (partnerIIdStandort == null || partnerIIdStandort.equals(dto.getPartnerIIdStandort())) {

							Object[] zeile = new Object[REPORT_BEWEGUNGSVORSCHAU_ANZAHL_FELDER];
							zeile[REPORT_BEWEGUNGSVORSCHAU_BELEGART] = "Lagerstand";
							zeile[REPORT_BEWEGUNGSVORSCHAU_BELEGNR] = dto.getCNr();
							zeile[REPORT_BEWEGUNGSVORSCHAU_LAGER_VERSTECKT] = Helper.short2Boolean(dto.getBVersteckt());
							zeile[REPORT_BEWEGUNGSVORSCHAU_MANDANT] = dto.getMandantCNr();
							if (dto.getPartnerIIdStandort() != null) {
								zeile[REPORT_BEWEGUNGSVORSCHAU_STANDORT] = getPartnerFac()
										.partnerFindByPrimaryKey(dto.getPartnerIIdStandort(), theClientDto).getCKbez();
							}

							zeile[REPORT_BEWEGUNGSVORSCHAU_PARTNER] = dto.getLagerartCNr();
							BigDecimal bdLagerstandActLager = getLagerFac().getLagerstand(artikelId, dto.getIId(),
									theClientDto);
							zeile[REPORT_BEWEGUNGSVORSCHAU_MENGE] = bdLagerstandActLager;
							if (Helper.short2boolean(dto.getBBestellvorschlag())
									|| Helper.short2boolean(dto.getBInternebestellung())) {
								// Lagerstand der Laeger aendert sich nur wenn
								// im
								// Artikel
								// beruecksichigen aktiviert wurde
								anfangslagerstand = anfangslagerstand.add(bdLagerstandActLager);
							}
							zeile[REPORT_BEWEGUNGSVORSCHAU_FIKTIVERLAGERSTAND] = anfangslagerstand;
							zeile[REPORT_BEWEGUNGSVORSCHAU_BESTELLVORSCHLAG_BERUECKSICHTIGT_LAGER] = Helper
									.short2Boolean(dto.getBBestellvorschlag());
							zeile[REPORT_BEWEGUNGSVORSCHAU_INTERNEBESTELLUNG_BERUECKSICHTIGT_LAGER] = Helper
									.short2Boolean(dto.getBInternebestellung());
							alDaten.add(zeile);
						}
					}
				}
			}

			for (int i = 0; i < dtos.length; i++) {

				BewegungsvorschauDto dto = dtos[i];

				Object[] zeile = new Object[REPORT_BEWEGUNGSVORSCHAU_ANZAHL_FELDER];

				zeile[REPORT_BEWEGUNGSVORSCHAU_BELEGART] = dto.getCBelegartCNr();
				zeile[REPORT_BEWEGUNGSVORSCHAU_BELEGNR] = dto.getCBelegnummer();

				if (dto.isbKommtAusInternerBstellung()) {
					zeile[REPORT_BEWEGUNGSVORSCHAU_BELEGNR] = "Interne Bestellung";
				} else if (dto.isBKommtAusBestellvorschlag()) {
					zeile[REPORT_BEWEGUNGSVORSCHAU_BELEGNR] = "Bestellvorschlag";
				}

				// PJ19767
				if (dto.getMandantCNr() != null) {
					zeile[REPORT_BEWEGUNGSVORSCHAU_MANDANT] = dto.getMandantCNr();
				} else {
					zeile[REPORT_BEWEGUNGSVORSCHAU_MANDANT] = theClientDto.getMandant();
				}

				if (((String) zeile[REPORT_BEWEGUNGSVORSCHAU_MANDANT]).equals(theClientDto.getMandant())) {
					parameter.put("P_ES_SIND_MEHRERE_MANDANTEN_BETEILIGT", Boolean.TRUE);
				}

				if (dto.getPartnerIIdStandort() != null) {
					zeile[REPORT_BEWEGUNGSVORSCHAU_STANDORT] = getPartnerFac()
							.partnerFindByPrimaryKey(dto.getPartnerIIdStandort(), theClientDto).getCKbez();
				}

				if (dto.getCBelegartCNr() != null && dto.getCBelegartCNr().equals(LocaleFac.BELEGART_LOS)) {

					LosDto losDto = null;
					try {
						losDto = getFertigungFac().losFindByPrimaryKey(dto.getIBelegIId());
						if (dto.getKundeDto() != null) {
							zeile[REPORT_BEWEGUNGSVORSCHAU_ZUGEHOERIGER_KUNDE_IM_LOS] = dto.getKundeDto()
									.getPartnerDto().formatFixTitelName1Name2();
						}

						if (losDto.getStuecklisteIId() != null) {
							StuecklisteDto stuecklisteDto = getStuecklisteFac()
									.stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(), theClientDto);

							zeile[REPORT_BEWEGUNGSVORSCHAU_LOS_STUECKLISTE_I_ID] = losDto.getStuecklisteIId();
							zeile[REPORT_BEWEGUNGSVORSCHAU_LOS_STUECKLISTENUMMER] = stuecklisteDto.getArtikelDto()
									.getCNr();
							zeile[REPORT_BEWEGUNGSVORSCHAU_LOS_STUECKLISTEBEZEICHNUNG] = stuecklisteDto.getArtikelDto()
									.getCBezAusSpr();
							zeile[REPORT_BEWEGUNGSVORSCHAU_LOS_STUECKLISTEZUSATZBEZEICHNUNG] = stuecklisteDto
									.getArtikelDto().getCZBezAusSpr();
							zeile[REPORT_BEWEGUNGSVORSCHAU_LOS_STUECKLISTEZUSATZBEZEICHNUNG2] = stuecklisteDto
									.getArtikelDto().getCZBez2AusSpr();

						}

					} catch (EJBExceptionLP e) {
						// Los nicht mehr vorhanden
					}

					if (losDto != null && losDto.getAuftragIId() != null) {
						AuftragDto aDto = getAuftragFac().auftragFindByPrimaryKey(losDto.getAuftragIId());

						zeile[REPORT_BEWEGUNGSVORSCHAU_LOS_AUFTRAGSNUMMER] = aDto.getCNr();
					}
				}

				zeile[REPORT_BEWEGUNGSVORSCHAU_FORECASTART] = dto.getForecastartCNr();
				zeile[REPORT_BEWEGUNGSVORSCHAU_FORECAST_BEMERKUNG] = dto.getForecastBemerkung();
				zeile[REPORT_BEWEGUNGSVORSCHAU_FORECASTPOSITION_I_ID] = dto.getForecastpositionIId();

				zeile[REPORT_BEWEGUNGSVORSCHAU_PROJEKT] = dto.getCProjekt();
				zeile[REPORT_BEWEGUNGSVORSCHAU_MENGE] = dto.getNMenge();
				zeile[REPORT_BEWEGUNGSVORSCHAU_LIEFERTERMIN] = dto.getTLiefertermin();
				zeile[REPORT_BEWEGUNGSVORSCHAU_ABLIEFERTERMIN] = dto.getTAbliefertermin();

				zeile[REPORT_BEWEGUNGSVORSCHAU_AUFTRAGSFREIGABE_ZEITPUNKT] = dto.getTAuftragsfreigabe();
				if (dto.getPersonalIIdAuftragsfreigabe() != null) {
					zeile[REPORT_BEWEGUNGSVORSCHAU_AUFTRAGSFREIGABE_PERSON] = getPersonalFac()
							.personalFindByPrimaryKey(dto.getPersonalIIdAuftragsfreigabe(), theClientDto)
							.getCKurzzeichen();
				}

				zeile[REPORT_BEWEGUNGSVORSCHAU_LIEFERTERMIN] = dto.getTLiefertermin();

				if (dto.getCBelegartCNr() != null
						&& (dto.getCBelegartCNr().equals(LocaleFac.BELEGART_BESTELLUNG) || dto.getCBelegartCNr()
								.equals(BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR.trim()))
						&& dto.getIBelegPositionIId() != null) {
					BestellpositionDto bestPosDto = getBestellpositionFac()
							.bestellpositionFindByPrimaryKeyOhneExc(dto.getIBelegPositionIId());
					zeile[REPORT_BEWEGUNGSVORSCHAU_BEST_ABNUMMER] = bestPosDto.getCABNummer();
					zeile[REPORT_BEWEGUNGSVORSCHAU_BEST_ABTERMIN] = dto.getTABTerminBestellung();

					if (bZentralerArtikelstamm) {
						zeile[REPORT_BEWEGUNGSVORSCHAU_MENGE_UNTERWEGS] = getArtikelbestelltFac()
								.getWareUnterwegsEinerBestellposition(bestPosDto.getIId(), theClientDto);
					}

				}
				if (bAuftragStundenMinuten)
					if (dto.getCBelegartCNr().equals(LocaleFac.BELEGART_AUFTRAG)) {
						AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(dto.getIBelegIId());
						zeile[REPORT_BEWEGUNGSVORSCHAU_FINALTERMIN] = Helper
								.formatTimestamp(auftragDto.getDFinaltermin(), theClientDto.getLocMandant());
					}

				if (bAuftragsfreigabe && dto.getCBelegartCNr().equals(LocaleFac.BELEGART_AUFTRAG)) {

					if (dto.getCBelegartCNr().equals(LocaleFac.BELEGART_AUFTRAG)) {
						AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(dto.getIBelegIId());

						if (auftragDto.getTAuftragsfreigabe() != null) {
							anfangslagerstand = anfangslagerstand.add(dto.getNMenge());
						}
					} else {
						anfangslagerstand = anfangslagerstand.add(dto.getNMenge());
					}

				} else {

					if (zeile[REPORT_BEWEGUNGSVORSCHAU_BELEGART] != null
							&& zeile[REPORT_BEWEGUNGSVORSCHAU_BELEGART].equals(LocaleFac.BELEGART_FORECAST)
							&& (zeile[REPORT_BEWEGUNGSVORSCHAU_FORECASTART] == null
									|| zeile[REPORT_BEWEGUNGSVORSCHAU_FORECASTART]
											.equals(ForecastFac.FORECASTART_FORECASTAUFTRAG)
									|| zeile[REPORT_BEWEGUNGSVORSCHAU_FORECASTART]
											.equals(ForecastFac.FORECASTART_NICHT_DEFINIERT))) {
						// Nur COD oder COW werden beim fiktiven Lagerstadn
						// beruecksichtigt
					} else {
						anfangslagerstand = anfangslagerstand.add(dto.getNMenge());
					}

				}

				zeile[REPORT_BEWEGUNGSVORSCHAU_FIKTIVERLAGERSTAND] = anfangslagerstand;
				if (dto.getPartnerDto() != null) {
					zeile[REPORT_BEWEGUNGSVORSCHAU_PARTNER] = dto.getPartnerDto().formatFixTitelName1Name2();
				}
				zeile[REPORT_BEWEGUNGSVORSCHAU_BESTELLVORSCHLAG_BERUECKSICHTIGT_LAGER] = null;
				zeile[REPORT_BEWEGUNGSVORSCHAU_INTERNEBESTELLUNG_BERUECKSICHTIGT_LAGER] = null;
				alDaten.add(zeile);
			}

			data = new Object[alDaten.size()][REPORT_BEWEGUNGSVORSCHAU_ANZAHL_FELDER];
			data = (Object[][]) alDaten.toArray(data);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		initJRDS(parameter, ArtikelReportFac.REPORT_MODUL, ArtikelReportFac.REPORT_BEWEGUNGSVORSCHAU,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printArtikelstatistik(Integer artikelIId, java.sql.Date dVon, java.sql.Date dBis,
			Integer iOption, boolean bMonatsstatistik, boolean bEingeschraenkt, boolean bMitHandlagerbewegungen,
			boolean bMitBewegungsvorschau, boolean bMitNichtFreigegebenenAuftraegen, boolean bMitHistory,
			boolean bMitVorgaengern, TheClientDto theClientDto) throws RemoteException {
		if (artikelIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelIId == null"));
		}

		boolean bWeReferenzAndrucken = false;
		try {
			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_WE_REFERENZ_IN_STATISTIK);
			bWeReferenzAndrucken = ((Boolean) parameter.getCWertAsObject()).booleanValue();

		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		// Erstellung des Reports
		HashMap<String, Object> parameter = new HashMap<String, Object>();

		Session session = FLRSessionFactory.getFactory().openSession();

		parameter.put("P_MIT_VORGAENGERN", new Boolean(bMitVorgaengern));

		ArrayList alArtikel = new ArrayList();
		alArtikel.add(artikelIId);

		// PJ19516
		if (bMitVorgaengern) {
			alArtikel.addAll(getArtikelFac().getAlleVorgaengerArtikel(artikelIId));
		}

		org.hibernate.Criteria crit = session.createCriteria(FLRLagerbewegung.class).createAlias("flrartikel", "a")
				.add(Restrictions.in("a.i_id", alArtikel))

				.createAlias("flrlager", "l");

		if (bMitHistory == false) {
			crit.add(Restrictions.eq("b_historie", Helper.boolean2Short(false)));
		}

		parameter.put("P_MITBUCHUNGSDETAILS", new Boolean(bMitHistory));

		parameter.put("P_MITBEWEGUNGSVORSCHAU", new Boolean(bMitBewegungsvorschau));
		parameter.put("P_EINGESCHRAENKT", new Boolean(bEingeschraenkt));

		ParametermandantDto parameterAuftragsfreigabe = getParameterFac().getMandantparameter(theClientDto.getMandant(),
				ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_AUFTRAGSFREIGABE);
		boolean bAuftragsfreigabe = ((Boolean) parameterAuftragsfreigabe.getCWertAsObject());

		parameter.put("P_MANDANTENPARAMETER_AUFTRAGSFREIGABE", new Boolean(bAuftragsfreigabe));

		parameter.put("P_MIT_NICHTFREIGEGEBENEN_AUFTRAEGEN", new Boolean(bMitNichtFreigegebenenAuftraegen));

		if (!getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
				theClientDto)) {
			crit.add(Restrictions.eq("l.mandant_c_nr", theClientDto.getMandant()));

		} else {
			if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_GETRENNTE_LAGER,
					theClientDto)) {
				crit.add(Restrictions.eq("l.mandant_c_nr", theClientDto.getMandant()));

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
			crit.add(Restrictions.ge(LagerFac.FLR_LAGERBEWEGUNG_T_BELEGDATUM, dVon));
			parameter.put("P_VON", new java.sql.Timestamp(dVon.getTime()));
		}
		if (dBis != null) {
			crit.add(Restrictions.lt(LagerFac.FLR_LAGERBEWEGUNG_T_BELEGDATUM,
					Helper.addiereTageZuTimestamp(new java.sql.Timestamp(dBis.getTime()), 1)));
			parameter.put("P_BIS", new java.sql.Timestamp(dBis.getTime()));
		}

		boolean darfEinkaufspreisSehen = getTheJudgeFac().hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_EINKAUF,
				theClientDto);
		boolean darfVerkaufspreisSehen = getTheJudgeFac().hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF,
				theClientDto);
		parameter.put("P_RECHT_LP_DARF_PREISE_SEHEN_EINKAUF", darfEinkaufspreisSehen);
		parameter.put("P_RECHT_LP_DARF_PREISE_SEHEN_VERKAUF", darfVerkaufspreisSehen);
		if (iOption == ArtikelFac.REPORT_ARTIKELSTATISTIK_OPTION_ALLE) {
			parameter.put("P_MITSUMMENZEILE", new Boolean(false));
			parameter.put("P_BELEGARTEN",
					getTextRespectUISpr("lp.alle", theClientDto.getMandant(), theClientDto.getLocUi()));
			parameter.put("P_BELEGART_AUSWAHL", "lp.alle");
		} else if (iOption == ArtikelFac.REPORT_ARTIKELSTATISTIK_OPTION_EK) {
			parameter.put("P_MITSUMMENZEILE", new Boolean(true));
			parameter.put("P_BELEGARTEN",
					getTextRespectUISpr("lp.einkauf", theClientDto.getMandant(), theClientDto.getLocUi()));
			String[] belegarten = new String[1];
			belegarten[0] = LocaleFac.BELEGART_BESTELLUNG;
			crit.add(Restrictions.in(LagerFac.FLR_LAGERBEWEGUNG_C_BELEGARTNR, belegarten));
			parameter.put("P_BELEGART_AUSWAHL", "lp.einkauf");
		} else if (iOption == ArtikelFac.REPORT_ARTIKELSTATISTIK_OPTION_VK) {
			parameter.put("P_MITSUMMENZEILE", new Boolean(true));
			parameter.put("P_BELEGARTEN",
					getTextRespectUISpr("lp.verkauf", theClientDto.getMandant(), theClientDto.getLocUi()));
			String[] belegarten = new String[3];
			belegarten[0] = LocaleFac.BELEGART_RECHNUNG;
			belegarten[1] = LocaleFac.BELEGART_LIEFERSCHEIN;
			belegarten[2] = LocaleFac.BELEGART_GUTSCHRIFT;
			crit.add(Restrictions.in(LagerFac.FLR_LAGERBEWEGUNG_C_BELEGARTNR, belegarten));
			parameter.put("P_BELEGART_AUSWAHL", "lp.verkauf");
		} else if (iOption == ArtikelFac.REPORT_ARTIKELSTATISTIK_OPTION_FERTIGUNG) {
			parameter.put("P_MITSUMMENZEILE", new Boolean(true));
			parameter.put("P_BELEGARTEN",
					getTextRespectUISpr("lp.fertigung", theClientDto.getMandant(), theClientDto.getLocUi()));
			String[] belegarten = new String[2];
			belegarten[0] = LocaleFac.BELEGART_LOS;
			belegarten[1] = LocaleFac.BELEGART_LOSABLIEFERUNG;
			crit.add(Restrictions.in(LagerFac.FLR_LAGERBEWEGUNG_C_BELEGARTNR, belegarten));
			parameter.put("P_BELEGART_AUSWAHL", "lp.fertigung");
		}

		if (bMitHandlagerbewegungen == false) {
			String[] belegarten = new String[1];
			belegarten[0] = LocaleFac.BELEGART_HAND;
			crit.add(Restrictions.not(Restrictions.in(LagerFac.FLR_LAGERBEWEGUNG_C_BELEGARTNR, belegarten)));

		}
		parameter.put("P_MITHANDLAGERBEWEGUNGEN", new Boolean(bMitHandlagerbewegungen));

		if (bEingeschraenkt) {
			crit.setMaxResults(50);
		}

		List<?> results = crit.list();

		Iterator<?> resultListIterator = results.iterator();

		ArrayList<Object[]> al = new ArrayList<Object[]>();

		// PJ 14202

		Session sessionInv = FLRSessionFactory.getFactory().openSession();

		String sQuery = "FROM FLRInventurstand AS i WHERE i.flrartikel.i_id=" + artikelIId;
		// SP3180
		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_GETRENNTE_LAGER,
				theClientDto)) {

			sQuery += " AND i.flrlager.mandant_c_nr='" + theClientDto.getMandant() + "'";

		}

		if (dVon != null) {

			sQuery += " AND i.flrinventur.t_inventurdatum>='" + Helper.formatDateWithSlashes(dVon) + "'";

		}
		if (dBis != null) {

			sQuery += " AND i.flrinventur.t_inventurdatum<='" + Helper.formatDateWithSlashes(dBis) + "'";

		}
		sQuery += " ORDER BY i.flrinventur.t_inventurdatum DESC";

		Query inventurliste = sessionInv.createQuery(sQuery);

		ArrayList alInventurliste = new ArrayList();

		List<?> resultList = inventurliste.list();

		Iterator<?> resultListInventur = resultList.iterator();

		while (resultListInventur.hasNext()) {
			FLRInventurstand item = (FLRInventurstand) resultListInventur.next();

			alInventurliste.add(item);

		}

		while (resultListIterator.hasNext()) {
			FLRLagerbewegung lagerbewegung = (FLRLagerbewegung) resultListIterator.next();

			if (bMitHistory == true || lagerbewegung.getN_menge().doubleValue() > 0) {

				while (alInventurliste.size() > 0) {
					FLRInventurstand flr = (FLRInventurstand) alInventurliste.get(0);

					if (lagerbewegung.getT_belegdatum().getTime() <= flr.getFlrinventur().getT_inventurdatum()
							.getTime()) {

						Object[] zeile = new Object[REPORT_ARTIKELSTATISTIK_ANZAHL_SPALTEN];
						zeile[REPORT_ARTIKELSTATISTIK_BEWEGUNGSVORSCHAU] = new Boolean(false);
						zeile[REPORT_ARTIKELSTATISTIK_BELEGART] = "Inventurstand";
						zeile[REPORT_ARTIKELSTATISTIK_ARTIKELNUMMER] = lagerbewegung.getFlrartikel().getC_nr();

						java.sql.Timestamp ts = new java.sql.Timestamp(
								flr.getFlrinventur().getT_inventurdatum().getTime());
						zeile[REPORT_ARTIKELSTATISTIK_SORTIERDATUM] = ts;
						zeile[REPORT_ARTIKELSTATISTIK_BELEGDATUM] = ts;
						zeile[REPORT_ARTIKELSTATISTIK_BUCHUNGSZEIT] = ts;

						zeile[REPORT_ARTIKELSTATISTIK_INVENTURMENGE] = flr.getN_inventurmenge();
						zeile[REPORT_ARTIKELSTATISTIK_BELEGNUMMER] = flr.getFlrinventur().getC_bez();
						zeile[REPORT_ARTIKELSTATISTIK_MANDANT] = flr.getFlrinventur().getMandant_c_nr();
						zeile[REPORT_ARTIKELSTATISTIK_LAGER] = flr.getFlrlager().getC_nr();

						al.add(zeile);
						alInventurliste.remove(0);
					} else {
						break;
					}
				}

				Object[] zeile = new Object[REPORT_ARTIKELSTATISTIK_ANZAHL_SPALTEN];
				zeile[REPORT_ARTIKELSTATISTIK_BEWEGUNGSVORSCHAU] = new Boolean(false);
				String sBelegartCNr = lagerbewegung.getFlrbelegart().getC_nr();
				zeile[REPORT_ARTIKELSTATISTIK_BELEGARTCNR] = sBelegartCNr;

				zeile[REPORT_ARTIKELSTATISTIK_BELEGDATUM] = lagerbewegung.getT_belegdatum();

				zeile[REPORT_ARTIKELSTATISTIK_ARTIKELNUMMER] = lagerbewegung.getFlrartikel().getC_nr();

				if (lagerbewegung.getFlrlager().getLagerart_c_nr().equals(LagerFac.LAGERART_WERTGUTSCHRIFT)) {
					zeile[REPORT_ARTIKELSTATISTIK_BELEGART] = RechnungFac.RECHNUNGART_WERTGUTSCHRIFT;
				} else {
					zeile[REPORT_ARTIKELSTATISTIK_BELEGART] = lagerbewegung.getC_belegartnr();

					if (lagerbewegung.getC_belegartnr().equals(LocaleFac.BELEGART_GUTSCHRIFT)) {
						RechnungDto rechnungDto = getRechnungFac()
								.rechnungFindByPrimaryKeyOhneExc(lagerbewegung.getI_belegartid());
						if (rechnungDto.getRechnungartCNr().equals(RechnungFac.RECHNUNGART_WERTGUTSCHRIFT))
							zeile[REPORT_ARTIKELSTATISTIK_BELEGART] = RechnungFac.RECHNUNGART_WERTGUTSCHRIFT;

					}

				}

				if (bMonatsstatistik == false) {
					if (lagerbewegung.getC_belegartnr().equals(LocaleFac.BELEGART_LOS)
							|| lagerbewegung.getC_belegartnr().equals(LocaleFac.BELEGART_LOSABLIEFERUNG)) {
						LosDto losDto = getFertigungFac().losFindByPrimaryKey(lagerbewegung.getI_belegartid());
						if (losDto.getStuecklisteIId() != null) {
							zeile[REPORT_ARTIKELSTATISTIK_STUECKLISTENARTIKEL] = getStuecklisteFac()
									.stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(), theClientDto)
									.getArtikelDto().formatArtikelbezeichnung();
						} else {
							zeile[REPORT_ARTIKELSTATISTIK_STUECKLISTENARTIKEL] = "Materialliste";
						}

						zeile[REPORT_ARTIKELSTATISTIK_LOS_STATUS] = losDto.getStatusCNr();
						zeile[REPORT_ARTIKELSTATISTIK_LOS_BEGINN] = losDto.getTProduktionsbeginn();
						zeile[REPORT_ARTIKELSTATISTIK_LOS_ENDE] = losDto.getTProduktionsende();
						zeile[REPORT_ARTIKELSTATISTIK_LOS_PROJEKT] = losDto.getCProjekt();

						zeile[REPORT_ARTIKELSTATISTIK_LOS_ABGELIFERT] = getFertigungFac()
								.getErledigteMenge(losDto.getIId(), theClientDto);

						if (losDto.getAuftragIId() != null) {
							zeile[REPORT_ARTIKELSTATISTIK_LOS_AUFTRAG] = getAuftragFac()
									.auftragFindByPrimaryKey(losDto.getAuftragIId()).getCNr();
						}

					}

					if (lagerbewegung.getC_belegartnr().equals(LocaleFac.BELEGART_LIEFERSCHEIN)
							|| lagerbewegung.getC_belegartnr().equals(LocaleFac.BELEGART_RECHNUNG)) {

						Integer aufposIId = null;
						if (lagerbewegung.getC_belegartnr().equals(LocaleFac.BELEGART_RECHNUNG)) {
							RechnungPositionDto rechPos = getRechnungFac()
									.rechnungPositionFindByPrimaryKeyOhneExc(lagerbewegung.getI_belegartpositionid());
							if (rechPos != null) {
								aufposIId = rechPos.getAuftragpositionIId();
							}
						} else if (lagerbewegung.getC_belegartnr().equals(LocaleFac.BELEGART_LIEFERSCHEIN)) {
							LieferscheinpositionDto lsPos = getLieferscheinpositionFac()
									.lieferscheinpositionFindByPrimaryKeyOhneExcUndOhneSnrChnrList(
											lagerbewegung.getI_belegartpositionid());
							if (lsPos != null) {
								aufposIId = lsPos.getAuftragpositionIId();

								LieferscheinDto lsDto = getLieferscheinFac()
										.lieferscheinFindByPrimaryKey(lsPos.getLieferscheinIId());
								zeile[REPORT_ARTIKELSTATISTIK_LS_LIEFERSCHEINART] = lsDto.getLieferscheinartCNr();
								zeile[REPORT_ARTIKELSTATISTIK_LS_VERRECHENBAR] = Helper
										.short2Boolean(lsDto.getBVerrechenbar());

							}
						}

						if (aufposIId != null) {
							AuftragpositionDto aufposDto = getAuftragpositionFac()
									.auftragpositionFindByPrimaryKey(aufposIId);
							AuftragDto aDto = getAuftragFac().auftragFindByPrimaryKey(aufposDto.getBelegIId());
							zeile[REPORT_ARTIKELSTATISTIK_AUFTRAG_AUSLOESER] = aDto.getCNr();
						}

					}

					if (lagerbewegung.getC_belegartnr().equals(LocaleFac.BELEGART_INVENTUR)) {
						InventurlisteDto inventurlisteDto = getInventurFac().inventurlisteFindByPrimaryKeyOhneExc(
								lagerbewegung.getI_belegartpositionid(), theClientDto);
						if (inventurlisteDto != null) {
							zeile[REPORT_ARTIKELSTATISTIK_INVENTURMENGE] = inventurlisteDto.getNInventurmenge();
						}
					}

				}
				zeile[REPORT_ARTIKELSTATISTIK_LAGER] = lagerbewegung.getFlrlager().getC_nr();
				zeile[REPORT_ARTIKELSTATISTIK_SNRCHNR] = lagerbewegung.getC_seriennrchargennr();
				zeile[REPORT_ARTIKELSTATISTIK_VERSION] = lagerbewegung.getC_version();
				zeile[REPORT_ARTIKELSTATISTIK_I_ID_BUCHUNG] = lagerbewegung.getI_id_buchung();

				zeile[REPORT_ARTIKELSTATISTIK_HISTORIE] = Helper.short2boolean(lagerbewegung.getB_historie());

				BigDecimal preis = new BigDecimal(0);
				BigDecimal wert = new BigDecimal(0);
				if (lagerbewegung.getB_abgang().intValue() == 0) {
					if (darfEinkaufspreisSehen) {

						if (lagerbewegung.getC_belegartnr().equals(LocaleFac.BELEGART_GUTSCHRIFT)
								|| lagerbewegung.getC_belegartnr().equals(RechnungFac.RECHNUNGART_WERTGUTSCHRIFT)) {

							RechnungPositionDto rechposDto = getRechnungFac()
									.rechnungPositionFindByPrimaryKeyOhneExc(lagerbewegung.getI_belegartpositionid());

							if (rechposDto != null) {
								RechnungDto rechnungDto = getRechnungFac()
										.rechnungFindByPrimaryKey(rechposDto.getRechnungIId());

								preis = rechposDto.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte()
										.divide(rechnungDto.getNKurs(), 5, BigDecimal.ROUND_HALF_EVEN);
								zeile[REPORT_ARTIKELSTATISTIK_PREIS] = Helper.rundeKaufmaennisch(preis, 4);
							} else {
								preis = lagerbewegung.getN_einstandspreis();
								zeile[REPORT_ARTIKELSTATISTIK_PREIS] = Helper.rundeKaufmaennisch(preis, 4);
							}

						} else {
							preis = lagerbewegung.getN_einstandspreis();
							zeile[REPORT_ARTIKELSTATISTIK_PREIS] = Helper.rundeKaufmaennisch(preis, 4);
						}

					} else {
						zeile[REPORT_ARTIKELSTATISTIK_PREIS] = null;
					}
				} else {

					if (darfVerkaufspreisSehen) {
						preis = lagerbewegung.getN_verkaufspreis();

						if (lagerbewegung.getC_belegartnr().equals(LocaleFac.BELEGART_LOS)) {
							preis = getLagerFac().getGemittelterGestehungspreisEinerAbgangsposition(
									LocaleFac.BELEGART_LOS, lagerbewegung.getI_belegartpositionid());
						}

						zeile[REPORT_ARTIKELSTATISTIK_PREIS] = Helper.rundeKaufmaennisch(preis, 4);
					} else {
						zeile[REPORT_ARTIKELSTATISTIK_PREIS] = null;
					}
				}

				if (bWeReferenzAndrucken) {

					zeile[REPORT_ARTIKELSTATISTIK_WE_REFERENZ] = getLagerFac().getWareneingangsreferenzSubreport(
							lagerbewegung.getC_belegartnr(), lagerbewegung.getI_belegartpositionid(),
							SeriennrChargennrMitMengeDto.erstelleDtoAusEinerChargennummer(
									lagerbewegung.getC_seriennrchargennr(), lagerbewegung.getN_menge()),
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
					zeile[REPORT_ARTIKELSTATISTIK_HERSTELLER] = lagerbewegung.getFlrhersteller().getC_nr();
				}
				if (lagerbewegung.getFlrland() != null) {
					zeile[REPORT_ARTIKELSTATISTIK_URSPRUNGSLAND] = lagerbewegung.getFlrland().getC_lkz();
				}

				try {

					if (bMonatsstatistik == false) {
						BelegInfos bi = getLagerFac().getBelegInfos(lagerbewegung.getC_belegartnr(),
								lagerbewegung.getI_belegartid(), lagerbewegung.getI_belegartpositionid(), theClientDto);
						zeile[REPORT_ARTIKELSTATISTIK_BELEGNUMMER] = bi.getBelegnummer();
						zeile[REPORT_ARTIKELSTATISTIK_MANDANT] = bi.getMandantCNr();
						zeile[REPORT_ARTIKELSTATISTIK_FIRMA] = bi.getKundeLieferant();

						zeile[REPORT_ARTIKELSTATISTIK_VERLEIHFAKTOR] = bi.getVerleihfaktor();
						zeile[REPORT_ARTIKELSTATISTIK_VERLEIHTAGE] = bi.getVerleihtage();
						zeile[REPORT_ARTIKELSTATISTIK_BUCHUNGSZEIT] = lagerbewegung.getT_buchungszeit();
						if (lagerbewegung.getC_belegartnr().equals(LocaleFac.BELEGART_LIEFERSCHEIN)) {
							zeile[REPORT_ARTIKELSTATISTIK_LIEFERTERMIN] = bi.getBelegdatum();
						}
						if (bi.getBelegdatum() != null) {
							zeile[REPORT_ARTIKELSTATISTIK_BELEGDATUM] = bi.getBelegdatum();
						} else {
							zeile[REPORT_ARTIKELSTATISTIK_BELEGDATUM] = lagerbewegung.getT_belegdatum();
						}

						// PJ20712
						if (Helper.short2boolean(lagerbewegung.getFlrartikel().getB_chargennrtragend())) {
							zeile[REPORT_ARTIKELSTATISTIK_ARTIKELSNRCHNR_I_ID] = getLagerFac()
									.artikelsnrchnrIIdFindByArtikelIIdCSeriennrchargennr(
											lagerbewegung.getArtikel_i_id(), lagerbewegung.getC_seriennrchargennr());

						}

						zeile[REPORT_ARTIKELSTATISTIK_MATERIALZUSCHLAG] = bi.getBdMaterialzuschlag();

						zeile[REPORT_ARTIKELSTATISTIK_SORTIERDATUM] = zeile[REPORT_ARTIKELSTATISTIK_BELEGDATUM];
						// Wenn Belegdatum und Buchungsdatum gleich, dann wird
						// die
						// Buchungszeit + Datum als Belegdatum verwendet
						if (Helper.cutTimestamp((Timestamp) zeile[REPORT_ARTIKELSTATISTIK_BELEGDATUM])
								.equals(Helper.cutTimestamp((Timestamp) zeile[REPORT_ARTIKELSTATISTIK_BUCHUNGSZEIT]))) {
							zeile[REPORT_ARTIKELSTATISTIK_SORTIERDATUM] = lagerbewegung.getT_buchungszeit();
						}

						// Fuer Monatsstatistik
						Calendar c = Calendar.getInstance();
						if (bi.getBelegdatum() != null) {
							c.setTimeInMillis(bi.getBelegdatum().getTime());
						} else {
							c.setTimeInMillis(lagerbewegung.getT_buchungszeit().getTime());
						}
						c.set(Calendar.HOUR_OF_DAY, 0);
						c.set(Calendar.MINUTE, 0);
						c.set(Calendar.SECOND, 0);
						c.set(Calendar.MILLISECOND, 0);
						c.set(Calendar.DAY_OF_MONTH, 1);
						// Schon enthalten?
					}
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
			zeile[REPORT_ARTIKELSTATISTIK_BEWEGUNGSVORSCHAU] = new Boolean(false);
			java.sql.Timestamp ts = new java.sql.Timestamp(flr.getFlrinventur().getT_inventurdatum().getTime());

			zeile[REPORT_ARTIKELSTATISTIK_SORTIERDATUM] = ts;
			zeile[REPORT_ARTIKELSTATISTIK_BELEGDATUM] = ts;
			zeile[REPORT_ARTIKELSTATISTIK_BUCHUNGSZEIT] = ts;

			zeile[REPORT_ARTIKELSTATISTIK_INVENTURMENGE] = flr.getN_inventurmenge();
			zeile[REPORT_ARTIKELSTATISTIK_BELEGNUMMER] = flr.getFlrinventur().getC_bez();
			zeile[REPORT_ARTIKELSTATISTIK_MANDANT] = flr.getFlrinventur().getMandant_c_nr();
			zeile[REPORT_ARTIKELSTATISTIK_LAGER] = flr.getFlrlager().getC_nr();

			al.add(zeile);
			alInventurliste.remove(0);

		}

		sessionInv.close();

		session.close();

		ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKey(artikelIId, theClientDto);
		parameter.put("P_ARTIKEL", aDto.formatArtikelbezeichnung());
		parameter.put("P_ARTIKELREFERENZNUMMER", aDto.getCReferenznr());

		// SP3003
		parameter.put("P_CHARGENEIGENSCHAFTEN_ANZEIGEN", Boolean.FALSE);

		if (aDto.istArtikelSnrOderchargentragend()) {
			PanelbeschreibungDto[] panelDtos = getPanelFac().panelbeschreibungFindByPanelCNrMandantCNr(
					PanelFac.PANEL_CHARGENEIGENSCHAFTEN, theClientDto.getMandant(), aDto.getArtgruIId());
			if (panelDtos != null && panelDtos.length > 0) {
				parameter.put("P_CHARGENEIGENSCHAFTEN_ANZEIGEN", Boolean.TRUE);
			}
		}

		parameter.put("P_ARTIKELEINHEIT", aDto.getEinheitCNr().trim());
		parameter.put("P_BESTELLEINHEIT", aDto.getEinheitCNrBestellung());
		parameter.put("P_MULTIPLIKATORBESTELLMENGE", aDto.getNUmrechnungsfaktor());
		parameter.put("P_BESTELLEINHEIT_INVERS", Helper.short2Boolean(aDto.getbBestellmengeneinheitInvers()));
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
			java.text.DateFormatSymbols symbols = new java.text.DateFormatSymbols(theClientDto.getLocUi());
			String[] defaultMonths = symbols.getMonths();

			GregorianCalendar cAktuell = new GregorianCalendar();
			if (al.size() > 0) {
				Object[] erste = (Object[]) al.get(al.size() - 1);
				Object[] letzte = (Object[]) al.get(0);

				cAktuell.setTimeInMillis(((Timestamp) letzte[REPORT_ARTIKELSTATISTIK_BELEGDATUM]).getTime());
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

						if (cAktuell.get(Calendar.MONTH) == cZeile.get(Calendar.MONTH)
								&& cAktuell.get(Calendar.YEAR) == cZeile.get(Calendar.YEAR)) {
							BigDecimal mengeZeile = (BigDecimal) zeile[REPORT_ARTIKELSTATISTIK_MENGE];
							BigDecimal preisZeile = (BigDecimal) zeile[REPORT_ARTIKELSTATISTIK_PREIS];
							if (mengeZeile != null && preisZeile != null) {

								boolean bGutschrift = false;
								boolean bWertGutschrift = false;

								if (zeile[REPORT_ARTIKELSTATISTIK_BELEGART] != null
										&& (zeile[REPORT_ARTIKELSTATISTIK_BELEGART]
												.equals(LocaleFac.BELEGART_GUTSCHRIFT)
												|| zeile[REPORT_ARTIKELSTATISTIK_BELEGART]
														.equals(RechnungFac.RECHNUNGART_WERTGUTSCHRIFT))) {
									bGutschrift = true;

									if (zeile[REPORT_ARTIKELSTATISTIK_BELEGART]
											.equals(RechnungFac.RECHNUNGART_WERTGUTSCHRIFT)) {
										bWertGutschrift = true;
									}

								}

								// SP18027

								if (mengeZeile.doubleValue() > 0) {
									if (bGutschrift == true) {
										if (bWertGutschrift == false) {
											mengeAbgang = mengeAbgang.subtract(mengeZeile.abs());
										}
										wertAbgang = wertAbgang.subtract(mengeZeile.abs().multiply(preisZeile));
									} else {
										mengeZugang = mengeZugang.add(mengeZeile);
										wertZugang = wertZugang.add(mengeZeile.multiply(preisZeile));
									}
								} else {
									mengeAbgang = mengeAbgang.add(mengeZeile.abs());
									wertAbgang = wertAbgang.add(mengeZeile.abs().multiply(preisZeile));
								}

							}
						}

					}

					Object[] zeileMonate = new Object[KundeReportFac.REPORT_MONATSSTATISTIK_ANZAHL_FELDER];
					zeileMonate[REPORT_MONATSSTATISTIK_MONAT] = defaultMonths[cAktuell.get(Calendar.MONTH)];
					zeileMonate[REPORT_MONATSSTATISTIK_JAHR] = cAktuell.get(Calendar.YEAR);
					zeileMonate[REPORT_MONATSSTATISTIK_ABGANG_MENGE] = mengeAbgang;
					zeileMonate[REPORT_MONATSSTATISTIK_ABGANG_WERT] = wertAbgang;
					zeileMonate[REPORT_MONATSSTATISTIK_ZUGANG_MENGE] = mengeZugang;
					zeileMonate[REPORT_MONATSSTATISTIK_ZUGANG_WERT] = wertZugang;
					alMonate.add(zeileMonate);

					cAktuell.set(Calendar.DAY_OF_MONTH, 1);
					cAktuell.getTimeInMillis();
					cAktuell.set(Calendar.MONTH, cAktuell.get(Calendar.MONTH) - 1);
					cAktuell.getTimeInMillis();
					cAktuell.set(Calendar.DAY_OF_MONTH, cAktuell.getActualMaximum(Calendar.DAY_OF_MONTH));
					cAktuell.getTimeInMillis();

				}

				Object[][] dataTemp = new Object[1][1];
				data = (Object[][]) alMonate.toArray(dataTemp);

				initJRDS(parameter, ArtikelFac.REPORT_MODUL, ArtikelReportFac.REPORT_MONATSSTATISTIK,
						theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
				return getReportPrint();
			} else {
				return null;
			}

		} else {

			if (bMitBewegungsvorschau == true) {
				// SP 1008 OHNE Internebestellung

				ArrayList<?> list = getInternebestellungFac().getBewegungsvorschauSortiert(artikelIId, false, false,
						theClientDto, null, false, true, bMitNichtFreigegebenenAuftraegen, null);

				BewegungsvorschauDto[] returnArray = new BewegungsvorschauDto[list.size()];
				BewegungsvorschauDto[] dtos = (com.lp.server.bestellung.service.BewegungsvorschauDto[]) list
						.toArray(returnArray);

				for (int i = 0; i < dtos.length; i++) {
					BewegungsvorschauDto dto = dtos[i];

					if (dto.getTLiefertermin() != null) {

						Object[] zeile = new Object[REPORT_ARTIKELSTATISTIK_ANZAHL_SPALTEN];
						zeile[REPORT_ARTIKELSTATISTIK_BEWEGUNGSVORSCHAU] = new Boolean(true);

						zeile[REPORT_ARTIKELSTATISTIK_BEWEGUNGSVORSCHAU_MANDANT] = dto.getMandantCNr();
						zeile[REPORT_ARTIKELSTATISTIK_BEWEGUNGSVORSCHAU_ABNUMMER_BESTELLUNG] = dto
								.getCABNummerBestellung();
						zeile[REPORT_ARTIKELSTATISTIK_BEWEGUNGSVORSCHAU_AUFTRAGSFREIGABEZEITPUNKT] = dto
								.getTAuftragsfreigabe();
						zeile[REPORT_ARTIKELSTATISTIK_BEWEGUNGSVORSCHAU_AB_TERMIN_BESTELLUNG] = dto
								.getTABTerminBestellung();

						if (dto.getCBelegartCNr() != null
								&& dto.getCBelegartCNr().equals(LocaleFac.BELEGART_LOSABLIEFERUNG)) {
							LosDto losDto = getFertigungFac().losFindByPrimaryKey(dto.getIBelegIId());

							zeile[REPORT_ARTIKELSTATISTIK_LOS_STATUS] = losDto.getStatusCNr();
							zeile[REPORT_ARTIKELSTATISTIK_LOS_BEGINN] = losDto.getTProduktionsbeginn();
							zeile[REPORT_ARTIKELSTATISTIK_LOS_ENDE] = losDto.getTProduktionsende();
							zeile[REPORT_ARTIKELSTATISTIK_LOS_PROJEKT] = losDto.getCProjekt();

							zeile[REPORT_ARTIKELSTATISTIK_LOS_ABGELIFERT] = getFertigungFac()
									.getErledigteMenge(losDto.getIId(), theClientDto);

							if (losDto.getAuftragIId() != null) {
								zeile[REPORT_ARTIKELSTATISTIK_LOS_AUFTRAG] = getAuftragFac()
										.auftragFindByPrimaryKey(losDto.getAuftragIId()).getCNr();
							}

						}

						zeile[REPORT_ARTIKELSTATISTIK_BELEGART] = dto.getCBelegartCNr();
						zeile[REPORT_ARTIKELSTATISTIK_BELEGNUMMER] = dto.getCBelegnummer();
						zeile[REPORT_ARTIKELSTATISTIK_MANDANT] = dto.getMandantCNr();

						if (dto.getPartnerDto() != null) {
							zeile[REPORT_ARTIKELSTATISTIK_FIRMA] = dto.getPartnerDto().formatFixTitelName1Name2();
						} else {
							zeile[REPORT_ARTIKELSTATISTIK_FIRMA] = dto.getCProjekt();
						}

						// PJ17836
						zeile[REPORT_ARTIKELSTATISTIK_SORTIERDATUM] = dto.getTLiefertermin();
						if (dto.getTABTerminBestellung() != null) {
							zeile[REPORT_ARTIKELSTATISTIK_SORTIERDATUM] = new Timestamp(
									dto.getTABTerminBestellung().getTime());
						}

						zeile[REPORT_ARTIKELSTATISTIK_BELEGDATUM] = dto.getTLiefertermin();
						zeile[REPORT_ARTIKELSTATISTIK_BUCHUNGSZEIT] = dto.getTLiefertermin();
						zeile[REPORT_ARTIKELSTATISTIK_LIEFERTERMIN] = dto.getTLiefertermin();
						zeile[REPORT_ARTIKELSTATISTIK_MENGE] = dto.getNMenge();

						// SP7552
						if (darfVerkaufspreisSehen && darfEinkaufspreisSehen) {
							zeile[REPORT_ARTIKELSTATISTIK_PREIS] = dto.getBdPreis();
						}

						al.add(zeile);
					}

				}

				// PJ17817

				ReportAnfragestatistikKriterienDto kritDtoI = new ReportAnfragestatistikKriterienDto();
				kritDtoI.setArtikelIId(artikelIId);
				ReportRahmenreservierungDto[] aReportRahmenreservierungDto = getReportRahmenreservierung(kritDtoI,
						theClientDto);

				for (int i = 0; i < aReportRahmenreservierungDto.length; i++) {
					ReportRahmenreservierungDto reportRahmenreservierungDto = (ReportRahmenreservierungDto) aReportRahmenreservierungDto[i];
					Object[] zeile = new Object[REPORT_ARTIKELSTATISTIK_ANZAHL_SPALTEN];
					zeile[REPORT_ARTIKELSTATISTIK_BEWEGUNGSVORSCHAU] = new Boolean(true);
					zeile[REPORT_ARTIKELSTATISTIK_BELEGART] = "Rahmenreservierung";

					zeile[REPORT_ARTIKELSTATISTIK_BELEGNUMMER] = reportRahmenreservierungDto.getAuftragCNr();
					zeile[REPORT_ARTIKELSTATISTIK_FIRMA] = reportRahmenreservierungDto.getCKundenname();

					zeile[REPORT_ARTIKELSTATISTIK_SORTIERDATUM] = reportRahmenreservierungDto
							.getTUebersteuerterLiefertermin();

					zeile[REPORT_ARTIKELSTATISTIK_LIEFERTERMIN] = reportRahmenreservierungDto
							.getTUebersteuerterLiefertermin();
					zeile[REPORT_ARTIKELSTATISTIK_BELEGDATUM] = reportRahmenreservierungDto
							.getTUebersteuerterLiefertermin();
					zeile[REPORT_ARTIKELSTATISTIK_BUCHUNGSZEIT] = reportRahmenreservierungDto
							.getTUebersteuerterLiefertermin();
					zeile[REPORT_ARTIKELSTATISTIK_MENGE] = reportRahmenreservierungDto.getNOffeneMenge().negate();

					al.add(zeile);
				}

				RahmenbestelltReportDto[] rahmenbestelltDtos = getReportRahmenbestelltDto(artikelIId, theClientDto);

				for (int i = 0; i < rahmenbestelltDtos.length; i++) {
					RahmenbestelltReportDto dto = rahmenbestelltDtos[i];
					Object[] zeile = new Object[REPORT_ARTIKELSTATISTIK_ANZAHL_SPALTEN];
					zeile[REPORT_ARTIKELSTATISTIK_BEWEGUNGSVORSCHAU] = new Boolean(true);
					zeile[REPORT_ARTIKELSTATISTIK_BELEGART] = "Rahmenbestellt";
					zeile[REPORT_ARTIKELSTATISTIK_BELEGNUMMER] = dto.getBestellnummer();
					zeile[REPORT_ARTIKELSTATISTIK_FIRMA] = dto.getLieferant();

					zeile[REPORT_ARTIKELSTATISTIK_LIEFERTERMIN] = dto.getTLiefertermin();
					zeile[REPORT_ARTIKELSTATISTIK_BELEGDATUM] = dto.getTLiefertermin();
					zeile[REPORT_ARTIKELSTATISTIK_BUCHUNGSZEIT] = dto.getTLiefertermin();

					zeile[REPORT_ARTIKELSTATISTIK_SORTIERDATUM] = dto.getTLiefertermin();

					zeile[REPORT_ARTIKELSTATISTIK_MENGE] = dto.getOffenmenge();
					// Nettoeinzelpreis
					zeile[REPORT_ARTIKELSTATISTIK_PREIS] = dto.getPreis();
					al.add(zeile);
				}

			}

			Timestamp tHeute = new Timestamp(System.currentTimeMillis());
			Timestamp tMorgen = Helper.addiereTageZuTimestamp(new Timestamp(System.currentTimeMillis()), 1);

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

			initJRDS(parameter, ArtikelFac.REPORT_MODUL, ArtikelReportFac.REPORT_ARTIKELSTATISTIK,
					theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
			return getReportPrint();

		}
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printVkPreisentwicklung(Integer artikelIId, TheClientDto theClientDto) {
		if (artikelIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelIId == null"));
		}

		// Erstellung des Reports
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		index = -1;
		sAktuellerReport = ArtikelReportFac.REPORT_VKPREISENTWICKLUNG;

		ArrayList alDaten = new ArrayList();

		try {

			VkPreisfindungEinzelverkaufspreisDto[] vkPreisfindungEinzelverkaufspreisDtos = getVkPreisfindungFac()
					.vkpfartikelverkaufspreisbasisFindByArtikelIId(artikelIId, theClientDto);

			// Preisbasis
			for (int i = 0; i < vkPreisfindungEinzelverkaufspreisDtos.length; i++) {
				Object[] zeile = new Object[REPORT_VKPREISENTWICKLUNG_ANZAHL_SPALTEN];
				zeile[REPORT_VKPREISENTWICKLUNG_GUELTIGAB] = vkPreisfindungEinzelverkaufspreisDtos[i]
						.getTVerkaufspreisbasisgueltigab();
				zeile[REPORT_VKPREISENTWICKLUNG_ART] = "Preisbasis";
				zeile[REPORT_VKPREISENTWICKLUNG_T_AENDERN] = vkPreisfindungEinzelverkaufspreisDtos[i].getTAendern();
				zeile[REPORT_VKPREISENTWICKLUNG_VKPREISBASIS] = vkPreisfindungEinzelverkaufspreisDtos[i]
						.getNVerkaufspreisbasis();
				zeile[REPORT_VKPREISENTWICKLUNG_MANDANT] = vkPreisfindungEinzelverkaufspreisDtos[i].getMandantCNr();
				zeile[REPORT_VKPREISENTWICKLUNG_BEMERKUNG] = vkPreisfindungEinzelverkaufspreisDtos[i].getCBemerkung();

				PersonalDto personalDto;

				personalDto = getPersonalFac().personalFindByPrimaryKey(
						vkPreisfindungEinzelverkaufspreisDtos[i].getPersonalIIdAendern(), theClientDto);
				zeile[REPORT_VKPREISENTWICKLUNG_PERSON_GEAENDERT] = personalDto.formatAnrede();

				alDaten.add(zeile);

			}

			// Einzelpreis
			VkPreisfindungPreislisteDto[] preislisten = getVkPreisfindungFac()
					.vkPreisfindungPreislisteFindByArtikelIId(artikelIId);
			for (int i = 0; i < preislisten.length; i++) {
				Object[] zeile = new Object[REPORT_VKPREISENTWICKLUNG_ANZAHL_SPALTEN];

				zeile[REPORT_VKPREISENTWICKLUNG_GUELTIGAB] = preislisten[i].getTPreisgueltigab();
				zeile[REPORT_VKPREISENTWICKLUNG_ART] = "Einzelpreis";
				zeile[REPORT_VKPREISENTWICKLUNG_T_AENDERN] = preislisten[i].getTAendern();
				zeile[REPORT_VKPREISENTWICKLUNG_FIXPREIS] = preislisten[i].getNArtikelfixpreis();

				zeile[REPORT_VKPREISENTWICKLUNG_BEMERKUNG] = preislisten[i].getCBemerkung();

				if (preislisten[i].getNArtikelstandardrabattsatz() != null) {
					zeile[REPORT_VKPREISENTWICKLUNG_RABATT] = new Double(
							preislisten[i].getNArtikelstandardrabattsatz().doubleValue());

				}

				VkpfartikelpreislisteDto preisliste = getVkPreisfindungFac()
						.vkpfartikelpreislisteFindByPrimaryKey(preislisten[i].getVkpfartikelpreislisteIId());

				zeile[REPORT_VKPREISENTWICKLUNG_PREISLISTENNAME] = preisliste.getCNr();
				zeile[REPORT_VKPREISENTWICKLUNG_MANDANT] = preisliste.getMandantCNr();

				PersonalDto personalDto;

				personalDto = getPersonalFac().personalFindByPrimaryKey(preislisten[i].getPersonalIIdAendern(),
						theClientDto);
				zeile[REPORT_VKPREISENTWICKLUNG_PERSON_GEAENDERT] = personalDto.formatAnrede();

				alDaten.add(zeile);
			}

			// Mengenstaffeln

			VkpfMengenstaffelDto[] staffeln = getVkPreisfindungFac()
					.vkpfMengenstaffelFindByArtikelIIdFuerVKPreisentwicklung(artikelIId, theClientDto);

			for (int i = 0; i < staffeln.length; i++) {
				Object[] zeile = new Object[REPORT_VKPREISENTWICKLUNG_ANZAHL_SPALTEN];
				zeile[REPORT_VKPREISENTWICKLUNG_GUELTIGAB] = staffeln[i].getTPreisgueltigab();
				zeile[REPORT_VKPREISENTWICKLUNG_GUELTIGBIS] = staffeln[i].getTPreisgueltigbis();
				zeile[REPORT_VKPREISENTWICKLUNG_ART] = "Mengenstaffel";
				zeile[REPORT_VKPREISENTWICKLUNG_T_AENDERN] = staffeln[i].getTAendern();
				zeile[REPORT_VKPREISENTWICKLUNG_FIXPREIS] = staffeln[i].getNArtikelfixpreis();
				zeile[REPORT_VKPREISENTWICKLUNG_RABATT] = staffeln[i].getFArtikelstandardrabattsatz();
				zeile[REPORT_VKPREISENTWICKLUNG_STAFFELMENGE] = staffeln[i].getNMenge();

				zeile[REPORT_VKPREISENTWICKLUNG_BEMERKUNG] = staffeln[i].getCBemerkung();

				PersonalDto personalDto;

				personalDto = getPersonalFac().personalFindByPrimaryKey(staffeln[i].getPersonalIIdAendern(),
						theClientDto);
				zeile[REPORT_VKPREISENTWICKLUNG_PERSON_GEAENDERT] = personalDto.formatAnrede();

				if (staffeln[i].getVkpfartikelpreislisteIId() != null) {
					VkpfartikelpreislisteDto preisliste = getVkPreisfindungFac()
							.vkpfartikelpreislisteFindByPrimaryKey(staffeln[i].getVkpfartikelpreislisteIId());

					zeile[REPORT_VKPREISENTWICKLUNG_PREISLISTENNAME] = preisliste.getCNr();
					zeile[REPORT_VKPREISENTWICKLUNG_MANDANT] = preisliste.getMandantCNr();
					alDaten.add(zeile);
				} else {
					Object[] vorlage = zeile.clone();
					for (int j = 0; j < vkPreisfindungEinzelverkaufspreisDtos.length; j++) {

						vorlage[REPORT_VKPREISENTWICKLUNG_MANDANT] = vkPreisfindungEinzelverkaufspreisDtos[j]
								.getMandantCNr();
						alDaten.add(vorlage);
					}
				}

			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		ArtikelDto dto = getArtikelFac().artikelFindByPrimaryKey(artikelIId, theClientDto);
		parameter.put("P_ARTIKEL", dto.formatArtikelbezeichnung());
		parameter.put("P_ARTIKELREFERENZNUMMER", dto.getCReferenznr());

		data = new Object[alDaten.size()][10];
		data = (Object[][]) alDaten.toArray(data);

		initJRDS(parameter, ArtikelReportFac.REPORT_MODUL, ArtikelReportFac.REPORT_VKPREISENTWICKLUNG,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	private LinkedHashMap getAllergeneEinesArtikels(Integer artikelIId, LinkedHashMap hmDaten,
			TheClientDto theClientDto) {

		ArtikelalergenDto[] artikelalergeneDtos = getArtikelFac().artikelallergenFindByArtikelIId(artikelIId);

		for (int i = 0; i < artikelalergeneDtos.length; i++) {
			artikelalergeneDtos[i].getAlergenIId();

			hmDaten.put(artikelalergeneDtos[i].getAlergenIId(), "");

		}

		StuecklisteDto stklDto = getStuecklisteFac().stuecklisteFindByArtikelIIdMandantCNrOhneExc(artikelIId,
				theClientDto.getMandant());

		if (stklDto != null) {

			StuecklistepositionDto[] stkposDto = getStuecklisteFac()
					.stuecklistepositionFindByStuecklisteIId(stklDto.getIId(), theClientDto);

			for (int i = 0; i < stkposDto.length; i++) {
				hmDaten = getAllergeneEinesArtikels(stkposDto[i].getArtikelIId(), hmDaten, theClientDto);

			}

		}

		return hmDaten;

	};

	public LPDatenSubreport getSubreportAllergene(Integer artikelIId, TheClientDto theClientDto) {
		String[] fieldnames = new String[] { "Allergen", "Enthalten" };

		TreeMap tmAlergeneKomplett = new TreeMap();

		LinkedHashMap hmDaten = new LinkedHashMap();

		AlergenDto[] alergene = getArtikelFac().allergenFindByMandantCNr(theClientDto);

		if (alergene.length > 0) {
			for (int i = 0; i < alergene.length; i++) {
				tmAlergeneKomplett.put(alergene[i].getISort(), alergene[i]);
			}
			hmDaten = getAllergeneEinesArtikels(artikelIId, hmDaten, theClientDto);

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

	private Map<String, Object> prepareVkPreisliste(Integer preislisteIId, Integer artikelgruppeIId,
			Integer artikelklasseIId, Integer shopgruppeIId, boolean bMitInaktiven, String artikelNrVon,
			String artikelNrBis, boolean bMitVersteckten, java.sql.Date datGueltikeitsdatumI, String waehrungCNr,
			TheClientDto theClientDto) {

		Integer mwstsatzId_0 = null;

		BigDecimal kurs = BigDecimal.ZERO;
		try {

			WechselkursDto kursDto = getLocaleFac().getKursZuDatum(waehrungCNr, theClientDto.getSMandantenwaehrung(),
					datGueltikeitsdatumI, theClientDto);
			kurs = kursDto.getNKurs();
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

		// SP6794
		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
				theClientDto)) {
			eingeloggterMandant = getSystemFac().getHauptmandant();
		}

		String sQuery = "";
		if (preislisteIId == null) {
			sQuery = "SELECT artikelliste.i_id, artikelliste.c_nr, aspr.c_bez, aspr.c_kbez, aspr.c_zbez,aspr.c_zbez2,artikelliste.einheit_c_nr, gruppe.c_nr, klasse.c_nr, artikelliste.b_versteckt, shopgruppe.c_nr, aspr.locale_c_nr, material.c_nr, artikelliste "
					+ " FROM FLRArtikelliste AS artikelliste"
					+ " LEFT OUTER JOIN artikelliste.flrartikelgruppe AS gruppe "
					+ " LEFT OUTER JOIN artikelliste.flrartikelklasse AS klasse "
					+ " LEFT OUTER JOIN artikelliste.flrshopgruppe AS shopgruppe "
					+ " LEFT OUTER JOIN artikelliste.flrmaterial AS material "
					+ " LEFT OUTER JOIN artikelliste.artikelshopgruppeset AS webshopgruppen "
					+ " LEFT OUTER JOIN artikelliste.artikelsprset AS aspr WHERE artikelliste.mandant_c_nr='"
					+ eingeloggterMandant + "' AND artikelliste.artikelart_c_nr NOT IN ('"
					+ ArtikelFac.ARTIKELART_HANDARTIKEL + "') ";

			if (artikelklasseIId != null) {
				sQuery += " AND klasse.i_id IN"
						+ getArtikelFac().holeAlleArtikelklassen(artikelklasseIId).erzeuge_IN_Fuer_Query();
			}
			if (artikelgruppeIId != null) {
				sQuery += " AND gruppe.i_id IN "
						+ getArtikelFac().holeAlleArtikelgruppen(artikelgruppeIId).erzeuge_IN_Fuer_Query();
			}
			if (shopgruppeIId != null) {

				String in = getArtikelFac().holeAlleShopgruppen(shopgruppeIId).erzeuge_IN_Fuer_Query();

				// SP6031
				sQuery += " AND( shopgruppe.i_id  IN " + in + " OR webshopgruppen.shopgruppe_i_id IN " + in + "  ) ";

			}
			if (bMitVersteckten == false) {
				sQuery += " AND artikelliste.b_versteckt=0 ";
			}
			if (artikelNrVon != null) {
				sQuery += " AND artikelliste.c_nr >='" + artikelNrVon + "'";
			}
			if (artikelNrBis != null) {
				sQuery = sQuery + " AND artikelliste.c_nr <='" + artikelNrBis + "'";
			}

			sQuery += " ORDER BY artikelliste.c_nr";
		} else {
			sQuery = "SELECT artikelliste.i_id, artikelliste.c_nr, aspr.c_bez, aspr.c_kbez, aspr.c_zbez,aspr.c_zbez2,artikelliste.einheit_c_nr, gruppe.c_nr, klasse.c_nr, artikelliste.b_versteckt, shopgruppe.c_nr, aspr.locale_c_nr, material.c_nr, artikelliste "
					+ " FROM FLRArtikelliste AS artikelliste"
					+ " LEFT OUTER JOIN artikelliste.flrartikelgruppe AS gruppe "
					+ " LEFT OUTER JOIN artikelliste.flrshopgruppe AS shopgruppe "
					+ " LEFT OUTER JOIN artikelliste.flrmaterial AS material "
					+ " LEFT OUTER JOIN artikelliste.flrartikelklasse AS klasse "
					+ " LEFT OUTER JOIN artikelliste.artikelshopgruppeset AS webshopgruppen "
					+ " LEFT OUTER JOIN artikelliste.artikelsprset AS aspr WHERE artikelliste.mandant_c_nr='"
					+ eingeloggterMandant + "' AND artikelliste.artikelart_c_nr NOT IN ('"
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
				sQuery += " AND klasse.i_id IN"
						+ getArtikelFac().holeAlleArtikelklassen(artikelklasseIId).erzeuge_IN_Fuer_Query();
			}
			if (artikelgruppeIId != null) {
				sQuery += " AND gruppe.i_id IN "
						+ getArtikelFac().holeAlleArtikelgruppen(artikelgruppeIId).erzeuge_IN_Fuer_Query();
			}
			if (shopgruppeIId != null) {
				String in = getArtikelFac().holeAlleShopgruppen(shopgruppeIId).erzeuge_IN_Fuer_Query();

				// SP6031
				sQuery += " AND( shopgruppe.i_id  IN " + in + " OR webshopgruppen.shopgruppe_i_id IN " + in + "  ) ";
			}
			if (artikelNrVon != null) {
				sQuery += " AND artikelliste.c_nr >='" + artikelNrVon + "'";
			}
			if (bMitVersteckten == false) {
				sQuery += " AND artikelliste.b_versteckt=0 ";
			}
			if (artikelNrBis != null) {
				sQuery = sQuery + " AND artikelliste.c_nr <='" + artikelNrBis + "'";
			}
			sQuery += "ORDER BY artikelliste.c_nr";
		}

		Session session = FLRSessionFactory.getFactory().openSession();

		session.enableFilter("filterLocale").setParameter("paramLocale", theClientDto.getLocUiAsString());

		Query inventurliste = session.createQuery(sQuery);

		List<?> resultList = inventurliste.list();

		Iterator<?> resultListIterator = resultList.iterator();

		HvCreatingCachingProvider<Integer, MwstsatzDto> mwstsatzCache = new HvCreatingCachingProvider<Integer, MwstsatzDto>() {
			private Timestamp now = getTimestamp();

			@Override
			protected MwstsatzDto provideValue(Integer key, Integer transformedKey) {
				MwstsatzDto mwstsatzDto = getMandantFac().mwstsatzFindZuDatum(key, now);
				return mwstsatzDto;
			}
		};

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
			String locale = (String) o[11];
			String material = (String) o[12];
			FLRArtikelliste flrArtikelliste = (FLRArtikelliste) o[13];

			data[row][REPORT_VKPREISLISTE_ARTIKELNUMMER] = artikelnummer;
			data[row][REPORT_VKPREISLISTE_ARTIKELIID] = artikel_i_id;
			data[row][REPORT_VKPREISLISTE_ARTIKELREFERENZNUMMER] = flrArtikelliste.getC_referenznr();
			data[row][REPORT_VKPREISLISTE_BEZEICHNUNG] = bezeichnung;
			data[row][REPORT_VKPREISLISTE_KURZBEZEICHNUNG] = kurzbezeichnung;
			data[row][REPORT_VKPREISLISTE_ZUSATZBEZEICHNUNG] = zusatzbezeichnung;
			data[row][REPORT_VKPREISLISTE_ZUSATZBEZEICHNUNG2] = zusatzbezeichnung2;
			data[row][REPORT_VKPREISLISTE_EINHEIT] = einheit;
			data[row][REPORT_VKPREISLISTE_MATERIAL] = material;
			data[row][REPORT_VKPREISLISTE_ARTIKELGRUPPE] = gruppe;
			data[row][REPORT_VKPREISLISTE_SHOPGRUPPE] = shopgruppe;
			data[row][REPORT_VKPREISLISTE_SHOPGRUPPEID] = flrArtikelliste.getShopgruppe_i_id();
			data[row][REPORT_VKPREISLISTE_ARTIKELKLASSE] = klasse;
			data[row][REPORT_VKPREISLISTE_LOCALE] = locale;
			data[row][REPORT_VKPREISLISTE_VERSTECKT] = Helper.short2Boolean(versteckt);

			if (flrArtikelliste.getFlrmwstatzbez() != null) {
				data[row][REPORT_VKPREISLISTE_MWSTSATZBEZ] = flrArtikelliste.getFlrmwstatzbez().getC_bezeichnung();

				MwstsatzDto mwstsatzDto = mwstsatzCache.getValueOfKey(flrArtikelliste.getMwstsatz_i_id());
				if (mwstsatzDto != null) {
					data[row][REPORT_VKPREISLISTE_MWSTSATZ] = mwstsatzDto.getFMwstsatz();
					data[row][REPORT_VKPREISLISTE_MWSTSATZID] = mwstsatzDto.getIId();
				}
			}

			data[row][REPORT_VKPREISLISTE_VERKAUFSEAN] = flrArtikelliste.getC_verkaufseannr();

			if (material != null) {
				data[row][REPORT_VKPREISLISTE_MATERIALZUSCHLAG] = getMaterialFac().getMaterialzuschlagVKInZielwaehrung(
						artikel_i_id, null, Helper.cutDate(datGueltikeitsdatumI), waehrungCNr, theClientDto);
				data[row][REPORT_VKPREISLISTE_MATERIALZUSCHLAGEK] = getMaterialFac()
						.getMaterialzuschlagEKInZielwaehrung(artikel_i_id, null, Helper.cutDate(datGueltikeitsdatumI),
								waehrungCNr, theClientDto);
			}

			try {

				data[row][REPORT_VKPREISLISTE_VERKAUFSPREISBASIS] = getVkPreisfindungFac().ermittlePreisbasis(
						artikel_i_id, Helper.cutDate(datGueltikeitsdatumI), BigDecimal.ONE, preislisteIId, waehrungCNr,
						theClientDto);

				BigDecimal preis = BigDecimal.ZERO;
				if (preislisteIId != null) {
//					ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(artikel_i_id, theClientDto);

					// @@ToDo: AD ? ist hier eine Staffelmenge bei Preisfindung
					// aus EK moeglich?
					VkpreisfindungDto dto = getVkPreisfindungFac().verkaufspreisfindungStufe1(artikel_i_id,
							Helper.cutDate(datGueltikeitsdatumI), preislisteIId,
							new VkpreisfindungDto(theClientDto.getLocUi()), mwstsatzId_0, new BigDecimal(1),
							waehrungCNr, theClientDto);

					if (dto.getVkpStufe1() != null && dto.getVkpStufe1().nettopreis != null) {
						preis = dto.getVkpStufe1().nettopreis;
					}
				} else {
					VkPreisfindungEinzelverkaufspreisDto vkpreisDto = getVkPreisfindungFac()
							.getArtikeleinzelverkaufspreis(artikel_i_id, datGueltikeitsdatumI, waehrungCNr,
									theClientDto);
					if (vkpreisDto != null && vkpreisDto.getNVerkaufspreisbasis() != null) {
						preis = vkpreisDto.getNVerkaufspreisbasis();
					}

				}

				data[row][REPORT_VKPREISLISTE_VKPREIS] = preis;

				java.sql.Date jetzt = new java.sql.Date(System.currentTimeMillis());
				jetzt = Helper.cutDate(jetzt);

				VkpfMengenstaffelDto[] vkpfMengenstaffelDtos = getVkPreisfindungFac()
						.vkpfMengenstaffelFindByArtikelIIdGueltigkeitsdatum(artikel_i_id, datGueltikeitsdatumI,
								preislisteIId, theClientDto);

				if (vkpfMengenstaffelDtos.length > 0) {
					data[row][REPORT_VKPREISLISTE_STAFFEL1] = vkpfMengenstaffelDtos[0].getNMenge();
					data[row][REPORT_VKPREISLISTE_PREISSTAFFEL1] = getVkPreisfindungFac()
							.berechneEinzelVkpreisEinerMengenstaffel(vkpfMengenstaffelDtos[0].getIId(), waehrungCNr,
									theClientDto);
				}
				if (vkpfMengenstaffelDtos.length > 1) {
					data[row][REPORT_VKPREISLISTE_STAFFEL2] = vkpfMengenstaffelDtos[1].getNMenge();
					data[row][REPORT_VKPREISLISTE_PREISSTAFFEL2] = getVkPreisfindungFac()
							.berechneEinzelVkpreisEinerMengenstaffel(vkpfMengenstaffelDtos[1].getIId(), waehrungCNr,
									theClientDto);
				}
				if (vkpfMengenstaffelDtos.length > 2) {
					data[row][REPORT_VKPREISLISTE_STAFFEL3] = vkpfMengenstaffelDtos[2].getNMenge();
					data[row][REPORT_VKPREISLISTE_PREISSTAFFEL3] = getVkPreisfindungFac()
							.berechneEinzelVkpreisEinerMengenstaffel(vkpfMengenstaffelDtos[2].getIId(), waehrungCNr,
									theClientDto);
				}
				if (vkpfMengenstaffelDtos.length > 3) {
					data[row][REPORT_VKPREISLISTE_STAFFEL4] = vkpfMengenstaffelDtos[3].getNMenge();
					data[row][REPORT_VKPREISLISTE_PREISSTAFFEL4] = getVkPreisfindungFac()
							.berechneEinzelVkpreisEinerMengenstaffel(vkpfMengenstaffelDtos[3].getIId(), waehrungCNr,
									theClientDto);
				}
				if (vkpfMengenstaffelDtos.length > 4) {
					data[row][REPORT_VKPREISLISTE_STAFFEL5] = vkpfMengenstaffelDtos[4].getNMenge();
					data[row][REPORT_VKPREISLISTE_PREISSTAFFEL5] = getVkPreisfindungFac()
							.berechneEinzelVkpreisEinerMengenstaffel(vkpfMengenstaffelDtos[4].getIId(), waehrungCNr,
									theClientDto);
				}

			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}
			row++;
		}

		session.close();

		Map<String, Object> parameter = new HashMap<String, Object>();

		try {
			if (preislisteIId != null) {

				VkpfartikelpreislisteDto vkpfartikelpreislisteDto = getVkPreisfindungFac()
						.vkpfartikelpreislisteFindByPrimaryKey(preislisteIId);
				parameter.put("P_PREISLISTE", vkpfartikelpreislisteDto.getCNr());

			}
			if (artikelgruppeIId != null) {
				parameter.put("P_ARTIKELGRUPPE",
						getArtikelFac().artgruFindByPrimaryKey(artikelgruppeIId, theClientDto).getCNr());
			}
			if (shopgruppeIId != null) {
				parameter.put("P_SHOPGRUPPE",
						getArtikelFac().shopgruppeFindByPrimaryKey(shopgruppeIId, theClientDto).getCNr());
			}
			if (artikelklasseIId != null) {
				parameter.put("P_ARTIKELKLASSE",
						getArtikelFac().artklaFindByPrimaryKey(artikelklasseIId, theClientDto).getCNr());
			}

			ParametermandantDto param = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_PREISBASIS_VERKAUF);
			Integer iPreisbasis = (Integer) param.getCWertAsObject();

			parameter.put("P_PREISBASIS_VERKAUF", iPreisbasis);

		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}

		parameter.put("P_ARTIKELNRVON", artikelNrVon);
		parameter.put("P_ARTIKELNRBIS", artikelNrBis);
		parameter.put("P_MITVERSTECKTEN", new Boolean(bMitVersteckten));

		parameter.put("P_PREISGUELTIGKEIT", datGueltikeitsdatumI);

		parameter.put("P_WAEHRUNG", waehrungCNr);

		parameter.put("P_WECHSELKURS_ZU_MANDANTENWAEHRUNG", kurs);

		return parameter;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printVkPreisliste(Integer preislisteIId, Integer artikelgruppeIId, Integer artikelklasseIId,
			Integer shopgruppeIId, boolean bMitInaktiven, String artikelNrVon, String artikelNrBis,
			boolean bMitVersteckten, java.sql.Date datGueltikeitsdatumI, String waehrungCNr,
			TheClientDto theClientDto) {
		Map<String, Object> parameter = prepareVkPreisliste(preislisteIId, artikelgruppeIId, artikelklasseIId,
				shopgruppeIId, bMitInaktiven, artikelNrVon, artikelNrBis, bMitVersteckten, datGueltikeitsdatumI,
				waehrungCNr, theClientDto);
		index = -1;
		sAktuellerReport = ArtikelReportFac.REPORT_VKPREISLISTE;

		initJRDS(parameter, ArtikelReportFac.REPORT_MODUL, ArtikelReportFac.REPORT_VKPREISLISTE,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public CustomerPricelistReportDto printVkPreislisteRaw(Integer preislisteIId, Integer artikelgruppeIId,
			Integer artikelklasseIId, Integer shopgruppeIId, boolean bMitInaktiven, String artikelNrVon,
			String artikelNrBis, boolean bMitVersteckten, java.sql.Date datGueltikeitsdatumI,
			final TheClientDto theClientDto) {
		Map<String, Object> parameter = prepareVkPreisliste(preislisteIId, artikelgruppeIId, artikelklasseIId,
				shopgruppeIId, bMitInaktiven, artikelNrVon, artikelNrBis, bMitVersteckten, datGueltikeitsdatumI,
				theClientDto.getSMandantenwaehrung(), theClientDto);
		CustomerPricelistReportDto dto = new CustomerPricelistReportDto();
		dto.setWithHidden(bMitVersteckten);

		dto.setPriceValidityMs(datGueltikeitsdatumI.getTime());
		dto.setItemRangeFrom(artikelNrVon);
		dto.setItemRangeTo(artikelNrBis);
		dto.setShoproup(new IdValueDto(shopgruppeIId, (String) parameter.get("P_SHOPGRUPPE")));
		dto.setItemgroup(new IdValueDto(artikelgruppeIId, (String) parameter.get("P_ARTIKELGRUPPE")));
		dto.setItemclass(new IdValueDto(artikelklasseIId, (String) parameter.get("P_ARTIKELKLASSE")));

		int preisNachkommastellen = 4;
		try {
			preisNachkommastellen = getMandantFac().getNachkommastellenPreisVK(theClientDto.getMandant());
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		HvCreatingCachingProvider<Integer, CustomerPricelistShopgroupDto> cacheShopgruppe = new HvCreatingCachingProvider<Integer, CustomerPricelistShopgroupDto>() {
			@Override
			protected CustomerPricelistShopgroupDto provideValue(Integer key, Integer transformedKey) {
				if (key == null)
					return null;

				ShopgruppeDto dto = getArtikelFac().shopgruppeFindByPrimaryKey(key, theClientDto);
				CustomerPricelistShopgroupDto cDto = new CustomerPricelistShopgroupDto();
				cDto.setCnr(dto.getCNr());
				cDto.setId(dto.getIId());
				cDto.setName(dto.getBezeichnung() == null ? dto.getCNr() : dto.getBezeichnung());
				return cDto;
			}
		};

		for (Object[] row : data) {
			CustomerPricelistItemDto r = new CustomerPricelistItemDto((Integer) row[REPORT_VKPREISLISTE_ARTIKELIID],
					(String) row[REPORT_VKPREISLISTE_ARTIKELNUMMER]);
			r.setItemClass((String) row[REPORT_VKPREISLISTE_ARTIKELKLASSE]);
			r.setItemGroup((String) row[REPORT_VKPREISLISTE_ARTIKELGRUPPE]);
			r.setItemMaterial((String) row[REPORT_VKPREISLISTE_MATERIAL]);
			r.setItemKursMaterialzuschlag((BigDecimal) row[REPORT_VKPREISLISTE_MATERIALZUSCHLAG]);
			r.setName((String) row[REPORT_VKPREISLISTE_BEZEICHNUNG]);
			r.setShortName((String) row[REPORT_VKPREISLISTE_KURZBEZEICHNUNG]);
			r.setAdditionalName((String) row[REPORT_VKPREISLISTE_ZUSATZBEZEICHNUNG]);
			r.setAdditionalName2((String) row[REPORT_VKPREISLISTE_ZUSATZBEZEICHNUNG2]);
			r.setHidden((Boolean) row[REPORT_VKPREISLISTE_VERSTECKT]);
			r.setUnit((String) row[REPORT_VKPREISLISTE_EINHEIT]);
			r.setVATId((Integer) row[REPORT_VKPREISLISTE_MWSTSATZID]);
			r.setShopgroupDto(cacheShopgruppe.getValueOfKey((Integer) row[REPORT_VKPREISLISTE_SHOPGRUPPEID]));
			CustomerPricelistPriceDto preisDto = new CustomerPricelistPriceDto(
					CustomerPricelistPriceDto.PREISTYP_VKPREISBASIS, preisNachkommastellen);
			preisDto.setCalculatedPrice((BigDecimal) row[REPORT_VKPREISLISTE_VKPREIS]);
			r.getPrices().add(preisDto);

			transformStaffelpreise(r, row, preisNachkommastellen);

			dto.getItems().add(r);
		}
		return dto;
	}

	private void transformStaffelpreise(CustomerPricelistItemDto itemDto, Object[] row, int nachkommastellen) {
		for (Integer baseIndexStaffel : new Integer[] { REPORT_VKPREISLISTE_STAFFEL1, REPORT_VKPREISLISTE_STAFFEL2,
				REPORT_VKPREISLISTE_STAFFEL3, REPORT_VKPREISLISTE_STAFFEL4, REPORT_VKPREISLISTE_STAFFEL5 }) {
			if (row[baseIndexStaffel] != null) {
				itemDto.getPrices().add(transformStaffelPreis(nachkommastellen, baseIndexStaffel, row));
			}
		}
	}

	private CustomerPricelistPriceDto transformStaffelPreis(int nachkommastellen, int baseIndex, Object[] row) {
		CustomerPricelistPriceDto pDto = new CustomerPricelistPriceDto(
				CustomerPricelistPriceDto.PREISTYP_VKSTAFFELPREIS, nachkommastellen);
		pDto.setCalculatedPrice((BigDecimal) row[baseIndex + 1]);
		pDto.setAmount((BigDecimal) row[baseIndex]);
		return pDto;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printVkPreisliste0(Integer preislisteIId, Integer artikelgruppeIId, Integer artikelklasseIId,
			Integer shopgruppeIId, boolean bMitInaktiven, String artikelNrVon, String artikelNrBis,
			boolean bMitVersteckten, java.sql.Date datGueltikeitsdatumI, TheClientDto theClientDto) {
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

		// SP6794
		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
				theClientDto)) {
			eingeloggterMandant = getSystemFac().getHauptmandant();
		}

		String sQuery = "";
		if (preislisteIId == null) {
			sQuery = "SELECT artikelliste.i_id, artikelliste.c_nr, aspr.c_bez, aspr.c_kbez, aspr.c_zbez,aspr.c_zbez2,artikelliste.einheit_c_nr, gruppe.c_nr, klasse.c_nr, artikelliste.b_versteckt, shopgruppe.c_nr, aspr.locale_c_nr, material.c_nr, artikelliste "
					+ " FROM FLRArtikelliste AS artikelliste"
					+ " LEFT OUTER JOIN artikelliste.flrartikelgruppe AS gruppe "
					+ " LEFT OUTER JOIN artikelliste.flrartikelklasse AS klasse "
					+ " LEFT OUTER JOIN artikelliste.flrshopgruppe AS shopgruppe "
					+ " LEFT OUTER JOIN artikelliste.flrmaterial AS material "
					+ " LEFT OUTER JOIN artikelliste.artikelshopgruppeset AS webshopgruppen "
					+ " LEFT OUTER JOIN artikelliste.artikelsprset AS aspr WHERE artikelliste.mandant_c_nr='"
					+ eingeloggterMandant + "' AND artikelliste.artikelart_c_nr NOT IN ('"
					+ ArtikelFac.ARTIKELART_HANDARTIKEL + "') ";

			if (artikelklasseIId != null) {
				sQuery += " AND klasse.i_id IN"
						+ getArtikelFac().holeAlleArtikelklassen(artikelklasseIId).erzeuge_IN_Fuer_Query();
			}
			if (artikelgruppeIId != null) {
				sQuery += " AND gruppe.i_id IN "
						+ getArtikelFac().holeAlleArtikelgruppen(artikelgruppeIId).erzeuge_IN_Fuer_Query();
			}
			if (shopgruppeIId != null) {

				String in = getArtikelFac().holeAlleShopgruppen(shopgruppeIId).erzeuge_IN_Fuer_Query();

				// SP6031
				sQuery += " AND( shopgruppe.i_id  IN " + in + " OR webshopgruppen.shopgruppe_i_id IN " + in + "  ) ";

			}
			if (bMitVersteckten == false) {
				sQuery += " AND artikelliste.b_versteckt=0 ";
			}
			if (artikelNrVon != null) {
				sQuery += " AND artikelliste.c_nr >='" + artikelNrVon + "'";
			}
			if (artikelNrBis != null) {
				sQuery = sQuery + " AND artikelliste.c_nr <='" + artikelNrBis + "'";
			}

			sQuery += " ORDER BY artikelliste.c_nr";
		} else {
			sQuery = "SELECT artikelliste.i_id, artikelliste.c_nr, aspr.c_bez, aspr.c_kbez, aspr.c_zbez,aspr.c_zbez2,artikelliste.einheit_c_nr, gruppe.c_nr, klasse.c_nr, artikelliste.b_versteckt, shopgruppe.c_nr, aspr.locale_c_nr, material.c_nr, artikelliste "
					+ " FROM FLRArtikelliste AS artikelliste"
					+ " LEFT OUTER JOIN artikelliste.flrartikelgruppe AS gruppe "
					+ " LEFT OUTER JOIN artikelliste.flrshopgruppe AS shopgruppe "
					+ " LEFT OUTER JOIN artikelliste.flrmaterial AS material "
					+ " LEFT OUTER JOIN artikelliste.flrartikelklasse AS klasse "
					+ " LEFT OUTER JOIN artikelliste.artikelshopgruppeset AS webshopgruppen "
					+ " LEFT OUTER JOIN artikelliste.artikelsprset AS aspr WHERE artikelliste.mandant_c_nr='"
					+ eingeloggterMandant + "' AND artikelliste.artikelart_c_nr NOT IN ('"
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
				sQuery += " AND klasse.i_id IN"
						+ getArtikelFac().holeAlleArtikelklassen(artikelklasseIId).erzeuge_IN_Fuer_Query();
			}
			if (artikelgruppeIId != null) {
				sQuery += " AND gruppe.i_id IN "
						+ getArtikelFac().holeAlleArtikelgruppen(artikelgruppeIId).erzeuge_IN_Fuer_Query();
			}
			if (shopgruppeIId != null) {
				String in = getArtikelFac().holeAlleShopgruppen(shopgruppeIId).erzeuge_IN_Fuer_Query();

				// SP6031
				sQuery += " AND( shopgruppe.i_id  IN " + in + " OR webshopgruppen.shopgruppe_i_id IN " + in + "  ) ";
			}
			if (artikelNrVon != null) {
				sQuery += " AND artikelliste.c_nr >='" + artikelNrVon + "'";
			}
			if (bMitVersteckten == false) {
				sQuery += " AND artikelliste.b_versteckt=0 ";
			}
			if (artikelNrBis != null) {
				sQuery = sQuery + " AND artikelliste.c_nr <='" + artikelNrBis + "'";
			}
			sQuery += "ORDER BY artikelliste.c_nr";
		}

		Session session = FLRSessionFactory.getFactory().openSession();

		session.enableFilter("filterLocale").setParameter("paramLocale", theClientDto.getLocUiAsString());

		Query inventurliste = session.createQuery(sQuery);

		List<?> resultList = inventurliste.list();

		Iterator<?> resultListIterator = resultList.iterator();

		data = new Object[resultList.size()][REPORT_VKPREISLISTE_ANZAHL_SPALTEN];

		int row = 0;

		HashMap<Integer, MwstsatzDto> hmMwstsatzCache = new HashMap<Integer, MwstsatzDto>();

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
			String locale = (String) o[11];
			String material = (String) o[12];
			FLRArtikelliste flrArtikelliste = (FLRArtikelliste) o[13];

			data[row][REPORT_VKPREISLISTE_ARTIKELNUMMER] = artikelnummer;
			data[row][REPORT_VKPREISLISTE_ARTIKELREFERENZNUMMER] = flrArtikelliste.getC_referenznr();
			data[row][REPORT_VKPREISLISTE_BEZEICHNUNG] = bezeichnung;
			data[row][REPORT_VKPREISLISTE_KURZBEZEICHNUNG] = kurzbezeichnung;
			data[row][REPORT_VKPREISLISTE_ZUSATZBEZEICHNUNG] = zusatzbezeichnung;
			data[row][REPORT_VKPREISLISTE_ZUSATZBEZEICHNUNG2] = zusatzbezeichnung2;
			data[row][REPORT_VKPREISLISTE_EINHEIT] = einheit;
			data[row][REPORT_VKPREISLISTE_MATERIAL] = material;
			data[row][REPORT_VKPREISLISTE_ARTIKELGRUPPE] = gruppe;
			data[row][REPORT_VKPREISLISTE_SHOPGRUPPE] = shopgruppe;
			data[row][REPORT_VKPREISLISTE_ARTIKELKLASSE] = klasse;
			data[row][REPORT_VKPREISLISTE_LOCALE] = locale;
			data[row][REPORT_VKPREISLISTE_VERSTECKT] = Helper.short2Boolean(versteckt);

			if (flrArtikelliste.getFlrmwstatzbez() != null) {
				data[row][REPORT_VKPREISLISTE_MWSTSATZBEZ] = flrArtikelliste.getFlrmwstatzbez().getC_bezeichnung();

				MwstsatzDto mwstsatzDto = null;

				if (hmMwstsatzCache.containsKey(flrArtikelliste.getMwstsatz_i_id())) {
					mwstsatzDto = hmMwstsatzCache.get(flrArtikelliste.getMwstsatz_i_id());
				} else {
					mwstsatzDto = getMandantFac().mwstsatzFindZuDatum(flrArtikelliste.getMwstsatz_i_id(),
							getTimestamp());
					hmMwstsatzCache.put(flrArtikelliste.getMwstsatz_i_id(), mwstsatzDto);
				}

				if (mwstsatzDto != null) {
					data[row][REPORT_VKPREISLISTE_MWSTSATZ] = mwstsatzDto.getFMwstsatz();
				}

			}
			data[row][REPORT_VKPREISLISTE_VERKAUFSEAN] = flrArtikelliste.getC_verkaufseannr();

			if (material != null) {
				data[row][REPORT_VKPREISLISTE_MATERIALZUSCHLAG] = getMaterialFac().getMaterialzuschlagVKInZielwaehrung(
						artikel_i_id, null, Helper.cutDate(datGueltikeitsdatumI), theClientDto.getSMandantenwaehrung(),
						theClientDto);
				data[row][REPORT_VKPREISLISTE_MATERIALZUSCHLAGEK] = getMaterialFac()
						.getMaterialzuschlagEKInZielwaehrung(artikel_i_id, null, Helper.cutDate(datGueltikeitsdatumI),
								theClientDto.getSMandantenwaehrung(), theClientDto);
			}

			try {
				BigDecimal preis = new BigDecimal(0);
				if (preislisteIId != null) {
					ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(artikel_i_id, theClientDto);

					// @@ToDo: AD ? ist hier eine Staffelmenge bei Preisfindung
					// aus EK moeglich?
					VkpreisfindungDto dto = getVkPreisfindungFac().verkaufspreisfindungStufe1(artikelDto.getIId(),
							Helper.cutDate(datGueltikeitsdatumI), preislisteIId,
							new VkpreisfindungDto(theClientDto.getLocUi()), mwstsatzId_0, new BigDecimal(1),
							theClientDto.getSMandantenwaehrung(), theClientDto);

					if (dto.getVkpStufe1() != null && dto.getVkpStufe1().nettopreis != null) {
						preis = dto.getVkpStufe1().nettopreis;
					}
				} else {
					VkPreisfindungEinzelverkaufspreisDto vkpreisDto = getVkPreisfindungFac()
							.getArtikeleinzelverkaufspreis(artikel_i_id, datGueltikeitsdatumI,
									theClientDto.getSMandantenwaehrung(), theClientDto);
					if (vkpreisDto != null && vkpreisDto.getNVerkaufspreisbasis() != null) {
						preis = vkpreisDto.getNVerkaufspreisbasis();
					}

				}

				data[row][REPORT_VKPREISLISTE_VKPREIS] = preis;

				java.sql.Date jetzt = new java.sql.Date(System.currentTimeMillis());
				jetzt = Helper.cutDate(jetzt);

				VkpfMengenstaffelDto[] vkpfMengenstaffelDtos = getVkPreisfindungFac()
						.vkpfMengenstaffelFindByArtikelIIdGueltigkeitsdatum(artikel_i_id, datGueltikeitsdatumI, null,
								theClientDto);

				if (vkpfMengenstaffelDtos.length > 0) {
					data[row][REPORT_VKPREISLISTE_STAFFEL1] = vkpfMengenstaffelDtos[0].getNMenge();
					data[row][REPORT_VKPREISLISTE_PREISSTAFFEL1] = getVkPreisfindungFac()
							.berechneEinzelVkpreisEinerMengenstaffel(vkpfMengenstaffelDtos[0].getIId(),
									theClientDto.getSMandantenwaehrung(), theClientDto);
				}
				if (vkpfMengenstaffelDtos.length > 1) {
					data[row][REPORT_VKPREISLISTE_STAFFEL2] = vkpfMengenstaffelDtos[1].getNMenge();
					data[row][REPORT_VKPREISLISTE_PREISSTAFFEL2] = getVkPreisfindungFac()
							.berechneEinzelVkpreisEinerMengenstaffel(vkpfMengenstaffelDtos[1].getIId(),
									theClientDto.getSMandantenwaehrung(), theClientDto);
				}
				if (vkpfMengenstaffelDtos.length > 2) {
					data[row][REPORT_VKPREISLISTE_STAFFEL3] = vkpfMengenstaffelDtos[2].getNMenge();
					data[row][REPORT_VKPREISLISTE_PREISSTAFFEL3] = getVkPreisfindungFac()
							.berechneEinzelVkpreisEinerMengenstaffel(vkpfMengenstaffelDtos[2].getIId(),
									theClientDto.getSMandantenwaehrung(), theClientDto);
				}
				if (vkpfMengenstaffelDtos.length > 3) {
					data[row][REPORT_VKPREISLISTE_STAFFEL4] = vkpfMengenstaffelDtos[3].getNMenge();
					data[row][REPORT_VKPREISLISTE_PREISSTAFFEL4] = getVkPreisfindungFac()
							.berechneEinzelVkpreisEinerMengenstaffel(vkpfMengenstaffelDtos[3].getIId(),
									theClientDto.getSMandantenwaehrung(), theClientDto);
				}
				if (vkpfMengenstaffelDtos.length > 4) {
					data[row][REPORT_VKPREISLISTE_STAFFEL5] = vkpfMengenstaffelDtos[4].getNMenge();
					data[row][REPORT_VKPREISLISTE_PREISSTAFFEL5] = getVkPreisfindungFac()
							.berechneEinzelVkpreisEinerMengenstaffel(vkpfMengenstaffelDtos[4].getIId(),
									theClientDto.getSMandantenwaehrung(), theClientDto);
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
				parameter.put("P_PREISLISTE", vkpfartikelpreislisteDto.getCNr());

			}
			if (artikelgruppeIId != null) {
				parameter.put("P_ARTIKELGRUPPE",
						getArtikelFac().artgruFindByPrimaryKey(artikelgruppeIId, theClientDto).getCNr());
			}
			if (shopgruppeIId != null) {
				parameter.put("P_SHOPGRUPPE",
						getArtikelFac().shopgruppeFindByPrimaryKey(shopgruppeIId, theClientDto).getCNr());
			}
			if (artikelklasseIId != null) {
				parameter.put("P_ARTIKELKLASSE",
						getArtikelFac().artklaFindByPrimaryKey(artikelklasseIId, theClientDto).getCNr());
			}

		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}

		parameter.put("P_ARTIKELNRVON", artikelNrVon);
		parameter.put("P_ARTIKELNRBIS", artikelNrBis);
		parameter.put("P_MITVERSTECKTEN", new Boolean(bMitVersteckten));

		parameter.put("P_PREISGUELTIGKEIT", datGueltikeitsdatumI);

		initJRDS(parameter, ArtikelReportFac.REPORT_MODUL, ArtikelReportFac.REPORT_VKPREISLISTE,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAllergene(String artikelNrVon, String artikelNrBis, TheClientDto theClientDto) {

		// Erstellung des Reports

		// Es werden nur Stuecklisten angezeigt

		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("P_ARTIKELNRVON", artikelNrVon);
		parameter.put("P_ARTIKELNRBIS", artikelNrBis);
		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT distinct aa.flrartikelliste.i_id,aa.flrartikelliste.c_nr, aspr.c_bez FROM FLRArtikelalergen as aa LEFT OUTER JOIN aa.flrartikelliste.artikelsprset AS aspr"
				+ " WHERE aa.flrartikelliste.mandant_c_nr='" + theClientDto.getMandant() + "' ";

		String artikelNrBis_Gefuellt = null;
		if (artikelNrBis != null) {
			artikelNrBis_Gefuellt = Helper.fitString2Length(artikelNrBis, 25, '_');
		}

		TreeMap<String, ArtikelDto> tmBetroffeneArtikel = new TreeMap<String, ArtikelDto>();

		queryString += " GROUP BY aa.flrartikelliste.i_id,aa.flrartikelliste.c_nr, aspr.c_bez ORDER BY aa.flrartikelliste.c_nr ASC";

		session.enableFilter("filterLocale").setParameter("paramLocale", Helper.locale2String(theClientDto.getLocUi()));

		Query query = session.createQuery(queryString);

		List<?> results = query.list();

		Iterator<?> resultListIterator = results.iterator();

		ArrayList al = new ArrayList();

		while (resultListIterator.hasNext()) {
			Object[] o = (Object[]) resultListIterator.next();
			Integer artikelIId = (Integer) o[0];
			tmBetroffeneArtikel = betroffeneStuecklistenHinzufuegen(artikelIId, tmBetroffeneArtikel, theClientDto);

		}

		session.close();

		Iterator itBetroffeneArtikel = tmBetroffeneArtikel.keySet().iterator();
		while (itBetroffeneArtikel.hasNext()) {

			ArtikelDto aDto = tmBetroffeneArtikel.get(itBetroffeneArtikel.next());

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
			oZeile[REPORT_ALLERGENE_BEZEICHNUNG] = aDto.getArtikelsprDto().getCBez();
			oZeile[REPORT_ALLERGENE_SUBREPORT_ENTHALTENE_ALLERGENE] = getSubreportAllergene(aDto.getIId(),
					theClientDto);

			al.add(oZeile);

		}

		index = -1;
		sAktuellerReport = ArtikelReportFac.REPORT_ALLERGENE;
		Object[][] returnArray = new Object[al.size()][REPORT_ALLERGENE_ANZAHL_SPALTEN];
		data = (Object[][]) al.toArray(returnArray);

		initJRDS(parameter, ArtikelFac.REPORT_MODUL, ArtikelReportFac.REPORT_ALLERGENE, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);
		return getReportPrint();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printGeaenderteChargen(String artikelNrVon, String artikelNrBis,
			ArrayList<GeaenderteChargennummernDto> alGeaenderteChargen, TheClientDto theClientDto) {

		// Erstellung des Reports

		// Es werden nur Stuecklisten angezeigt

		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("P_ARTIKELNRVON", artikelNrVon);
		parameter.put("P_ARTIKELNRBIS", artikelNrBis);

		ArrayList alDaten = new ArrayList();

		for (int i = 0; i < alGeaenderteChargen.size(); i++) {

			GeaenderteChargennummernDto dto = alGeaenderteChargen.get(i);

			try {
				WareneingangspositionDto wePosDto = getWareneingangFac()
						.wareneingangspositionFindByPrimaryKey(dto.getWepIId());
				WareneingangDto weDto = getWareneingangFac()
						.wareneingangFindByPrimaryKey(wePosDto.getWareneingangIId());

				BestellpositionDto bsposDto = getBestellpositionFac()
						.bestellpositionFindByPrimaryKey(dto.getBestellpositionIId());
				BestellungDto bsDto = getBestellungFac().bestellungFindByPrimaryKey(bsposDto.getBestellungIId());
				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKey(bsposDto.getArtikelIId(), theClientDto);

				Object[] oZeile = new Object[REPORT_GEAENDERTE_CHARGEN_ANZAHL_SPALTEN];
				oZeile[REPORT_GEAENDERTE_CHARGEN_ARTIKELNUMMER] = aDto.getCNr();
				if (aDto.getArtikelsprDto() != null) {
					oZeile[REPORT_GEAENDERTE_CHARGEN_BEZEICHNUNG] = aDto.getArtikelsprDto().getCBez();
					oZeile[REPORT_GEAENDERTE_CHARGEN_ZUSATZBEZEICHNUNG] = aDto.getArtikelsprDto().getCZbez();
				}

				oZeile[REPORT_GEAENDERTE_CHARGEN_BESTELLUNG] = bsDto.getCNr();
				oZeile[REPORT_GEAENDERTE_CHARGEN_WARENEINGANG] = weDto.getCLieferscheinnr();
				oZeile[REPORT_GEAENDERTE_CHARGEN_CHNR_ALT] = dto.getChnrAlt();
				oZeile[REPORT_GEAENDERTE_CHARGEN_CHNR_NEU] = dto.getChnrNeu();

				alDaten.add(oZeile);

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}

		index = -1;
		sAktuellerReport = ArtikelReportFac.REPORT_GEAENDERTE_CHARGEN;
		Object[][] returnArray = new Object[alDaten.size()][REPORT_GEAENDERTE_CHARGEN_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(returnArray);

		initJRDS(parameter, ArtikelFac.REPORT_MODUL, ArtikelReportFac.REPORT_GEAENDERTE_CHARGEN,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();

	}

	private TreeMap<String, ArtikelDto> betroffeneStuecklistenHinzufuegen(Integer artikelIId,
			TreeMap<String, ArtikelDto> tmBetroffeneArtikel, TheClientDto theClientDto) {
		// Abhaengige Stuecklisten hinzufuegen
		Session sessionStkl = FLRSessionFactory.getFactory().openSession();
		String queryStringStkl = "SELECT stklpos FROM FLRStuecklisteposition stklpos WHERE stklpos.flrartikel.i_id="
				+ artikelIId + "";
		Query queryStkl = sessionStkl.createQuery(queryStringStkl);
		List<?> resultsStkl = queryStkl.list();
		Iterator<?> resultListIteratorStkl = resultsStkl.iterator();
		while (resultListIteratorStkl.hasNext()) {
			FLRStuecklisteposition stklPos = (FLRStuecklisteposition) resultListIteratorStkl.next();

			ArtikelDto aDto = getArtikelFac()
					.artikelFindByPrimaryKeySmall(stklPos.getFlrstueckliste().getArtikel_i_id(), theClientDto);

			tmBetroffeneArtikel.put(aDto.getCNr(), aDto);
			betroffeneStuecklistenHinzufuegen(stklPos.getFlrstueckliste().getArtikel_i_id(), tmBetroffeneArtikel,
					theClientDto);

		}

		sessionStkl.close();
		return tmBetroffeneArtikel;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printRahmenbestellungsliste(Integer artikelIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (artikelIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelIId == null"));
		}
		Session session = null;
		try {
			// Erstellung des Reports
			index = -1;
			sAktuellerReport = ArtikelReportFac.REPORT_ARTIKELBESTELLTRAHMEN;

			RahmenbestelltReportDto[] dtos = getReportRahmenbestelltDto(artikelIId, theClientDto);

			data = new Object[dtos.length][REPORT_RAHMENBEST_ANZAHL_SPALTEN];

			for (int i = 0; i < dtos.length; i++) {
				RahmenbestelltReportDto dto = dtos[i];
				data[i][REPORT_RAHMENBEST_BESTELLUNGSNUMMER] = dto.getBestellnummer();
				data[i][REPORT_RAHMENBEST_LIEFERANT] = dto.getLieferant();
				data[i][REPORT_RAHMENBEST_PROJEKT] = dto.getProjekt();

				data[i][REPORT_RAHMENBEST_LIEFERTERMIN] = dto.getTLiefertermin();

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
			dto = getArtikelFac().artikelFindByPrimaryKey(artikelIId, theClientDto);
			mapParameter.put("P_ARTIKEL", dto.formatArtikelbezeichnung());
			mapParameter.put("P_ARTIKELREFERENZNUMMER", dto.getCReferenznr());

			initJRDS(mapParameter, ArtikelReportFac.REPORT_MODUL, ArtikelReportFac.REPORT_ARTIKELBESTELLTRAHMEN,
					theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return getReportPrint();
	}

	private RahmenbestelltReportDto[] getReportRahmenbestelltDto(Integer artikelIId, TheClientDto theClientDto)
			throws RemoteException {
		Session session;
		SessionFactory factory = FLRSessionFactory.getFactory();
		session = factory.openSession();
		Criteria crit = session.createCriteria(FLRBestellpositionReport.class);
		Criteria critBestellung = crit.createCriteria(BestellpositionFac.FLR_BESTELLPOSITION_FLRBESTELLUNG);

		Criteria critArtikel = crit.createCriteria(BestellpositionFac.FLR_BESTELLPOSITION_FLRARTIKEL);
		// nur diesen Artikel
		critArtikel.add(Restrictions.eq("i_id", artikelIId));
		// Filter nach Mandant
		critBestellung.add(Restrictions.eq(BestellungFac.FLR_BESTELLUNG_MANDANT_C_NR, theClientDto.getMandant()));
		// keine stornierten oder erledigten Bestellungen.
		Collection<String> cStati = new LinkedList<String>();
		cStati.add(BestellungFac.BESTELLSTATUS_STORNIERT);
		cStati.add(BestellungFac.BESTELLSTATUS_ERLEDIGT);
		critBestellung
				.add(Restrictions.not(Restrictions.in(BestellungFac.FLR_BESTELLUNG_BESTELLUNGSTATUS_C_NR, cStati)));
		// Nur Rahmenbestellungen
		critBestellung.add(Restrictions.eq(BestellungFac.FLR_BESTELLUNG_BESTELLUNGART_C_NR,
				BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR));
		// Query ausfuehren
		List<?> list = crit.list();

		Iterator<?> resultListIterator = list.iterator();
		int row = 0;

		RahmenbestelltReportDto[] dtos = new RahmenbestelltReportDto[list.size()];

		while (resultListIterator.hasNext()) {
			FLRBestellpositionReport besPos = (FLRBestellpositionReport) resultListIterator.next();

			RahmenbestelltReportDto dto = new RahmenbestelltReportDto();

			dto.setBestellnummer(besPos.getFlrbestellung().getC_nr());

			dto.setLieferant(
					besPos.getFlrbestellung().getFlrlieferant().getFlrpartner().getC_name1nachnamefirmazeile1());

			dto.setProjekt(besPos.getFlrbestellung().getC_bezprojektbezeichnung());

			if (besPos.getT_uebersteuerterliefertermin() != null) {
				dto.setTLiefertermin(besPos.getT_uebersteuerterliefertermin());

			} else {
				if (besPos.getFlrbestellung().getT_liefertermin() != null) {
					dto.setTLiefertermin(new Timestamp(besPos.getFlrbestellung().getT_liefertermin().getTime()));
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

			dto.settAbTermin(bestellpositionDto.getTAuftragsbestaetigungstermin());

			dto.setAbKommentar(bestellpositionDto.getCABKommentar());

			dtos[row] = dto;
			row++;
		}

		session.close();
		return dtos;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printLieferantenpreisvergleich(Integer artikelIId, java.sql.Date dStichtag,
			TheClientDto theClientDto) {

		dStichtag = Helper.cutDate(dStichtag);

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		ArtikelDto dto = getArtikelFac().artikelFindByPrimaryKey(artikelIId, theClientDto);
		parameter.put("P_ARTIKEL", dto.formatArtikelbezeichnung());

		parameter.put("P_ARTIKEL_EINHEIT", dto.getEinheitCNr());

		parameter.put("P_ARTIKEL_BESTELLMENGENEINHEIT", dto.getEinheitCNrBestellung());

		parameter.put("P_ARTIKEL_UMRECHNUNGSFAKTOR", dto.getNUmrechnungsfaktor());
		parameter.put("P_ARTIKEL_BESTELLMENGENEINHEIT_INVERS",
				Helper.short2Boolean(dto.getbBestellmengeneinheitInvers()));

		try {
			ParametermandantDto parameterDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKELWIEDERBESCHAFFUNGSZEIT);

			parameter.put("P_EINHEIT_WIEDERBESCHAFFUNGSZEIT", parameterDto.getCWert());
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		parameter.put("P_STICHTAG", dStichtag);

		ArrayList alDaten = getDataLieferantenpreisvergleich(artikelIId, dStichtag, theClientDto);

		Object[][] dataTemp = new Object[alDaten.size()][REPORT_LIEFERANTENPREISVERGLEICH_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(dataTemp);

		sAktuellerReport = ArtikelReportFac.REPORT_LIEFERANTENPREISVERGLEICH;

		initJRDS(parameter, ArtikelReportFac.REPORT_MODUL, ArtikelReportFac.REPORT_LIEFERANTENPREISVERGLEICH,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();

	}

	private ArrayList getDataLieferantenpreisvergleich(Integer artikelIId, java.sql.Date dStichtag,
			TheClientDto theClientDto) {
		String sQuery = "SELECT al " + " FROM FLRArtikellieferant AS al WHERE al.artikel_i_id=" + artikelIId
				+ " AND al.t_preisgueltigab<='" + Helper.formatDateWithSlashes(dStichtag)
				+ "' AND (al.t_preisgueltigbis IS NULL OR al.t_preisgueltigbis >='"
				+ Helper.formatDateWithSlashes(dStichtag) + "') ORDER BY al.i_sort ASC";
		Session session = FLRSessionFactory.getFactory().openSession();
		Query artikelliefernat = session.createQuery(sQuery);

		List<?> resultList = artikelliefernat.list();

		Iterator<?> resultListIterator = resultList.iterator();

		ArrayList alDaten = new ArrayList();

		TreeMap<BigDecimal, String> tmStaffeln = new TreeMap<BigDecimal, String>();

		LinkedHashMap<Integer, String> lhmLieferanten = new LinkedHashMap<Integer, String>();

		while (resultListIterator.hasNext()) {
			FLRArtikellieferant al = (FLRArtikellieferant) resultListIterator.next();
			Set s = al.getStaffelset();

			tmStaffeln.put(BigDecimal.ONE, "");

			if (al.getN_einzelpreis() == null) {
				continue;
			}

			lhmLieferanten.put(al.getLieferant_i_id(),
					HelperServer.formatNameAusFLRPartner(al.getFlrlieferant().getFlrpartner()));

			Iterator it = s.iterator();

			while (it.hasNext()) {

				FLRArtikellieferantstaffel staffel = (FLRArtikellieferantstaffel) it.next();

				if (staffel.getT_preisgueltigab().getTime() <= dStichtag.getTime()
						&& (staffel.getT_preisgueltigbis() == null
								|| staffel.getT_preisgueltigbis().after(dStichtag))) {
					tmStaffeln.put(staffel.getN_menge(), "");
				}

			}
		}

		Iterator itLieferanten = lhmLieferanten.keySet().iterator();

		LieferantenpreisvergleichDto reportZeilen = new LieferantenpreisvergleichDto();

		while (itLieferanten.hasNext()) {

			Integer lieferantIId = (Integer) itLieferanten.next();

			String lieferant = lhmLieferanten.get(lieferantIId);

			Iterator itStaffeln = tmStaffeln.keySet().iterator();
			while (itStaffeln.hasNext()) {
				BigDecimal staffel = (BigDecimal) itStaffeln.next();

				ArtikellieferantDto alDto = getArtikelFac().getArtikelEinkaufspreis(artikelIId, lieferantIId, staffel,
						theClientDto.getSMandantenwaehrung(), dStichtag, theClientDto);

				reportZeilen.addReportZeile(lieferantIId, staffel, alDto);

			}

		}

		Iterator lieferantenIds = reportZeilen.lhmReportZeile.keySet().iterator();
		while (lieferantenIds.hasNext()) {

			Integer lieferantIId = (Integer) lieferantenIds.next();

			LieferantDto lfDto = getLieferantFac().lieferantFindByPrimaryKey(lieferantIId, theClientDto);

			Object[] oZeile = new Object[REPORT_LIEFERANTENPREISVERGLEICH_ANZAHL_SPALTEN];
			oZeile[REPORT_LIEFERANTENPREISVERGLEICH_LIEFERANT] = lfDto.getPartnerDto().formatFixName1Name2();
			oZeile[REPORT_LIEFERANTENPREISVERGLEICH_LIEFERANT_KBEZ] = lfDto.getPartnerDto().getCKbez();

			LinkedHashMap<BigDecimal, LieferantenpreisvergleichDtoZeile> mengenstaffeln = reportZeilen.lhmReportZeile
					.get(lieferantIId);

			// SUBREPORT
			String[] fieldnames = new String[] { "Mengenstaffel", "Preis", "Wiederbeschaffungszeit", "Guenstigster",
					"Schnellster" };

			ArrayList alDatenSubreport = new ArrayList();

			Iterator itMengenstaffel = mengenstaffeln.keySet().iterator();
			while (itMengenstaffel.hasNext()) {
				BigDecimal bdMengenstaffel = (BigDecimal) itMengenstaffel.next();

				LieferantenpreisvergleichDtoZeile zeileDto = mengenstaffeln.get(bdMengenstaffel);

				Object[] oZeileSubreport = new Object[fieldnames.length];
				oZeileSubreport[0] = bdMengenstaffel;
				oZeileSubreport[1] = zeileDto.getPreis();
				oZeileSubreport[2] = zeileDto.getWbz();
				oZeileSubreport[3] = reportZeilen.istAmGuenstigsten(bdMengenstaffel, zeileDto.getPreis());
				oZeileSubreport[4] = reportZeilen.istAmSchnellsten(bdMengenstaffel, zeileDto.getWbz());

				alDatenSubreport.add(oZeileSubreport);

				oZeile[REPORT_LIEFERANTENPREISVERGLEICH_NICHT_LIEFERBAR] = zeileDto.isbNichtLieferbar();

			}
			Object[][] dataSubKD = new Object[alDatenSubreport.size()][5];
			dataSubKD = (Object[][]) alDatenSubreport.toArray(dataSubKD);
			oZeile[REPORT_LIEFERANTENPREISVERGLEICH_SUBREPORT] = new LPDatenSubreport(dataSubKD, fieldnames);

			alDaten.add(oZeile);

		}
		return alDaten;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printLieferantenpreis(Integer artikelIId, TheClientDto theClientDto) {
		HashMap<String, Object> parameter = new HashMap<String, Object>();

		ArtikelDto dto = getArtikelFac().artikelFindByPrimaryKey(artikelIId, theClientDto);
		parameter.put("P_ARTIKEL", dto.formatArtikelbezeichnung());

		sAktuellerReport = ArtikelReportFac.REPORT_LIEFERANTENPREIS;

		String sQuery = "SELECT al " + " FROM FLRArtikellieferant AS al WHERE al.artikel_i_id=" + artikelIId;
		Session session = FLRSessionFactory.getFactory().openSession();
		Query inventurliste = session.createQuery(sQuery);

		List<?> resultList = inventurliste.list();

		Iterator<?> resultListIterator = resultList.iterator();

		data = new Object[resultList.size()][6];

		ArrayList alDaten = new ArrayList();

		while (resultListIterator.hasNext()) {
			FLRArtikellieferant al = (FLRArtikellieferant) resultListIterator.next();
			Set s = al.getStaffelset();

			if (al.getN_einzelpreis() == null) {
				continue;
			}

			ArtikellieferantDto alDto = null;
			try {
				alDto = getArtikelFac().artikellieferantFindByPrimaryKey(al.getI_id(), theClientDto);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Object[] zeile = new Object[REPORT_LIEFERANTENPREIS_ANZAHL_SPALTEN];

			zeile[REPORT_LIEFERANTENPREIS_FIXKOSTEN] = alDto.getNFixkosten();
			zeile[REPORT_LIEFERANTENPREIS_GUELTIGAB] = alDto.getTPreisgueltigab();
			zeile[REPORT_LIEFERANTENPREIS_LIEFERANT] = alDto.getLieferantDto().getPartnerDto()
					.formatFixTitelName1Name2();
			zeile[REPORT_LIEFERANTENPREIS_MENGE] = new BigDecimal(1);
			zeile[REPORT_LIEFERANTENPREIS_MINDESTBESTELLMENGE] = alDto.getFMindestbestelmenge();
			zeile[REPORT_LIEFERANTENPREIS_NETTOPREIS] = alDto.getNNettopreis();
			zeile[REPORT_LIEFERANTENPREIS_WIEDERBESCHAFFUNGSZEIT] = alDto.getIWiederbeschaffungszeit();

			alDaten.add(zeile);
			Iterator it = s.iterator();

			while (it.hasNext()) {

				FLRArtikellieferantstaffel staffel = (FLRArtikellieferantstaffel) it.next();

				zeile = new Object[REPORT_LIEFERANTENPREIS_ANZAHL_SPALTEN];

				zeile[REPORT_LIEFERANTENPREIS_FIXKOSTEN] = alDto.getNFixkosten();
				zeile[REPORT_LIEFERANTENPREIS_GUELTIGAB] = new java.sql.Timestamp(
						staffel.getT_preisgueltigab().getTime());
				zeile[REPORT_LIEFERANTENPREIS_LIEFERANT] = alDto.getLieferantDto().getPartnerDto()
						.formatFixTitelName1Name2();
				zeile[REPORT_LIEFERANTENPREIS_MENGE] = staffel.getN_menge();
				zeile[REPORT_LIEFERANTENPREIS_MINDESTBESTELLMENGE] = alDto.getFMindestbestelmenge();
				zeile[REPORT_LIEFERANTENPREIS_NETTOPREIS] = staffel.getN_nettopreis();
				zeile[REPORT_LIEFERANTENPREIS_WIEDERBESCHAFFUNGSZEIT] = alDto.getIWiederbeschaffungszeit();
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

		initJRDS(parameter, ArtikelReportFac.REPORT_MODUL, ArtikelReportFac.REPORT_LIEFERANTENPREIS,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printMehrereArtikeletiketten(String artikelgruppenList, Integer artikelklasseIId,
			Integer artikelVonId, Integer artikelBisId, Integer lagerIId, Integer lagerplatzVonId,
			Integer lagerplatzBisId, Integer shopgruppeIId,
			ArtikelReportFac.ReportMehrereArtikeletikettenSortierung sortierung, boolean orderAscDesc,
			boolean etikettProLagerplatz, TheClientDto theClientDto) throws RemoteException {

		String locale = theClientDto.getLocMandantAsString();

		Session session = getNewSession();

		Query q = buildMehrereArtikelReportQuery(artikelgruppenList, artikelklasseIId, artikelVonId, artikelBisId,
				lagerIId, lagerplatzVonId, lagerplatzBisId, shopgruppeIId, sortierung, orderAscDesc, session);

		List<Object[]> resultList = q.list();

		index = -1;
		sAktuellerReport = ArtikelReportFac.REPORT_ARTIKELETIKETT;

		List<Object[]> alDaten = new ArrayList<Object[]>();

		MandantDto mandantDto = null;

		try {
			mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		int exemplar = 1;
		// ArtikelIId -> List of lager IId
		Map<Integer, List<Integer>> mapArtikelLager = new HashMap<>();
		for (Object[] result : resultList) {
			FLRArtikelliste artikel = (FLRArtikelliste) result[0];
			FLRLagerplatz lagerplatz = (FLRLagerplatz) result[1];
			BigDecimal artLagerstand = (BigDecimal) result[2];
			FLRMaterial material = (FLRMaterial) result[3];
			FLRGeometrie geometrie = (FLRGeometrie) result[4];

			if (!etikettProLagerplatz && lagerplatz != null && lagerplatz.getFlrlager() != null) {
				// Wenn alle Lagerplaetze auf einem Etikett sein sollten, dann hier schauen, ob
				// dieser Artikel in diesem Lager schon ein Etikett hat
				int artikelIId = artikel.getI_id();
				int currLagerIId = lagerplatz.getFlrlager().getI_id();
				List<Integer> listArtikelLager = mapArtikelLager.get(artikelIId);
				if (listArtikelLager == null) {
					listArtikelLager = new ArrayList<>(1);
					mapArtikelLager.put(artikelIId, listArtikelLager);
				}
				if (listArtikelLager.contains(currLagerIId)) {
					continue;
				}
				listArtikelLager.add(currLagerIId);
			}

			Object[] zeile = new Object[REPORT_ARTIKELETIKETT_ANZAHL_SPALTEN];
			zeile[REPORT_ARTIKELETIKETT_ARTIKELNUMMER] = artikel.getC_nr();
			zeile[REPORT_ARTIKELETIKETT_EINHEIT] = artikel.getEinheit_c_nr();
			zeile[REPORT_ARTIKELETIKETT_REFERENZNUMMER] = artikel.getC_referenznr();
			zeile[REPORT_ARTIKELETIKETT_REVISION] = artikel.getC_revision();
			zeile[REPORT_ARTIKELETIKETT_INDEX] = artikel.getC_index();
			zeile[REPORT_ARTIKELETIKETT_VERPACKUNGSMENGE] = artikel.getF_verpackungsmenge();
			zeile[REPORT_ARTIKELETIKETT_VERPACKUNGSMITTELMENGE] = artikel.getN_verpackungsmittelmenge();

			zeile[REPORT_ARTIKELETIKETT_MANDANTADRESSE] = Helper.formatMandantAdresse(mandantDto);

			for (FLRArtikellistespr spr : (Set<FLRArtikellistespr>) artikel.getArtikelsprset()) {
				if (spr.getLocale_c_nr().equals(locale)) {
					zeile[REPORT_ARTIKELETIKETT_BEZEICHNUNG] = spr.getC_bez();
					zeile[REPORT_ARTIKELETIKETT_ZUSATZBEZEICHNUNG] = spr.getC_zbez();
					zeile[REPORT_ARTIKELETIKETT_ZUSATZBEZEICHNUNG2] = spr.getC_zbez2();
					zeile[REPORT_ARTIKELETIKETT_KURZBEZEICHNUNG] = spr.getC_kbez();
					break;
				}
			}

			if (material != null) {
				Set<FLRMaterialspr> materialsprset = material.getMaterialsprset();
				if (materialsprset != null && materialsprset.size() > 0) {
					for (FLRMaterialspr materialspr : materialsprset) {
						if (materialspr.getLocale().equals(locale)) {
							zeile[REPORT_ARTIKELETIKETT_MATERIAL] = materialspr.getC_bez();
						}
					}
				}
			}

			if (geometrie != null) {
				zeile[REPORT_ARTIKELETIKETT_BREITE] = geometrie.getF_breite();
				zeile[REPORT_ARTIKELETIKETT_HOEHE] = geometrie.getF_hoehe();
				zeile[REPORT_ARTIKELETIKETT_TIEFE] = geometrie.getF_tiefe();
			}

			zeile[REPORT_ARTIKELETIKETT_VERKAUFS_EAN] = artikel.getC_verkaufseannr();

			FLRHersteller hersteller = artikel.getFlrhersteller();

			if (hersteller != null) {
				zeile[REPORT_ARTIKELETIKETT_HERSTELLER] = hersteller.getC_nr();
				zeile[REPORT_ARTIKELETIKETT_HERSTELLER_NAME1] = hersteller.getFlrpartner()
						.getC_name1nachnamefirmazeile1();
				zeile[REPORT_ARTIKELETIKETT_HERSTELLER_NAME2] = hersteller.getFlrpartner()
						.getC_name2vornamefirmazeile2();
			}

			Set<FLRArtikellieferant> artikellieferantSet = artikel.getArtikellieferantset();
			if (artikellieferantSet != null && artikellieferantSet.size() > 0) {
				FLRArtikellieferant artikellieferant = artikellieferantSet.iterator().next();
				FLRPartner partner = artikellieferant.getFlrlieferant().getFlrpartner();
				StringBuilder partnerAddr = new StringBuilder();
				FLRLandplzort lpo = partner.getFlrlandplzort();
				zeile[REPORT_ARTIKELETIKETT_LIEFERANT] = formatFLRPartnerAnrede(partner);
				zeile[REPORT_ARTIKELETIKETT_LIEFERANT_ARTIKELNUMMER] = artikellieferant.getC_artikelnrlieferant();
				zeile[REPORT_ARTIKELETIKETT_LIEFERANT_ARTIKELBEZEICHNUNG] = artikellieferant.getC_bezbeilieferant();
			}
			BigDecimal lagerstand = null;

			if (lagerplatz != null) {
				if (etikettProLagerplatz) {
					zeile[REPORT_ARTIKELETIKETT_LAGERORT] = lagerplatz.getC_lagerplatz();
				} else {
					// Alle Lagerplaetze mit , getrennt
					zeile[REPORT_ARTIKELETIKETT_LAGERORT] = formatAlleLagerplaetzeArtikel(artikel,
							lagerplatz.getFlrlager().getI_id());
				}
			}

			if (artLagerstand != null && Helper.short2boolean(artikel.getB_lagerbewirtschaftet())) {
				zeile[REPORT_ARTIKELETIKETT_LAGERSTAND] = artLagerstand;
			}
			zeile[REPORT_ARTIKELETIKETT_EXEMPLAR] = exemplar;
			exemplar++;
			alDaten.add(zeile);
		}

		session.close();

		for (Object[] zeile : alDaten) {
			zeile[REPORT_ARTIKELETIKETT_EXEMPLAREGESAMT] = alDaten.size();
		}

		Object[][] returnArray = new Object[alDaten.size()][REPORT_ARTIKELETIKETT_ANZAHL_SPALTEN];
		data = alDaten.toArray(returnArray);

		HashMap<String, Object> parameterMap = new HashMap<String, Object>();

		initJRDS(parameterMap, ArtikelReportFac.REPORT_MODUL, ArtikelReportFac.REPORT_ARTIKELETIKETT,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		JasperPrintLP print = getReportPrint();
		return print;
	}

	private String formatAlleLagerplaetzeArtikel(FLRArtikelliste artikel, int lagerIId) {
		StringBuilder sb = new StringBuilder();
		for (Object o : artikel.getArtikellagerplatzset()) {
			FLRArtikellagerplaetze lagerplatz = (FLRArtikellagerplaetze) o;
			if (lagerplatz.getFlrlagerplatz().getFlrlager().getI_id() == lagerIId) {
				if (sb.length() != 0) {
					sb.append(", ");
				}
				sb.append(lagerplatz.getFlrlagerplatz().getC_lagerplatz());
			}
		}
		return sb.toString();
	}

	/**
	 * Anpassung von PartnerDto.formatAnrede() fuer FLRPartner
	 * 
	 * @param partner
	 * @return
	 */
	private String formatFLRPartnerAnrede(FLRPartner partner) {
		String ret = "";

		String sAnredeCNr = partner.getAnrede_c_nr();
		if (sAnredeCNr != null && !sAnredeCNr.equals("") && (sAnredeCNr.equals(PartnerFac.PARTNER_ANREDE_FRAU)
				|| sAnredeCNr.equals(PartnerFac.PARTNER_ANREDE_HERR))) {
			if (partner.getAnrede_c_nr() != null) {
				ret += partner.getAnrede_c_nr().trim();
			}
			if (partner.getC_titel() != null) {
				ret += " " + partner.getC_titel().trim();
			}
			if (partner.getC_name2vornamefirmazeile2() != null) {
				ret += " " + partner.getC_name2vornamefirmazeile2().trim();
			}
			if (partner.getC_name1nachnamefirmazeile1() != null) {
				ret += " " + partner.getC_name1nachnamefirmazeile1().trim();
			}
			if (partner.getC_ntitel() != null) {
				ret += " " + partner.getC_ntitel().trim();
			}
		} else {
			// leer firma
			if (partner.getC_name1nachnamefirmazeile1() != null) {
				ret += " " + partner.getC_name1nachnamefirmazeile1().trim();
			}
			if (partner.getC_name2vornamefirmazeile2() != null) {
				ret += " " + partner.getC_name2vornamefirmazeile2().trim();
			}
		}

		return ret.trim();
	}

	private Query buildMehrereArtikelReportQuery(String artikelgruppenList, Integer artikelklasseIId,
			Integer artikelVonId, Integer artikelBisId, Integer lagerIId, Integer lagerplatzVonId,
			Integer lagerplatzBisId, Integer shopgruppeIId,
			ArtikelReportFac.ReportMehrereArtikeletikettenSortierung sortierung, boolean orderAscDesc,
			Session session) {

		// Falls artikelgruppenList ein ungueltiges format hat, nicht nach Artikelgruppe
		// filtern
		if (!validateArtikelgruppenList(artikelgruppenList)) {
			myLogger.warn("ReportMehrereArtikeletiketten: Artikelgruppenfilter " + artikelgruppenList
					+ " ist nicht korrekt formatiert. Es wird nicht nach Artikelgruppen gefiltert.");
			artikelgruppenList = null;
		}

		List<String> conditions = new ArrayList<String>();

		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("select artikelliste, lagerplatz, lager.n_lagerstand, material, geometrie from ");
		queryBuilder.append("FLRArtikelliste as artikelliste ");
		queryBuilder.append("LEFT JOIN artikelliste.artikellagerset as lager ");
		queryBuilder.append("LEFT JOIN artikelliste.artikellagerplatzset as artikellagerplatz ");
		queryBuilder.append("LEFT JOIN artikelliste.flrshopgruppe as shopgruppe ");
		queryBuilder.append("LEFT JOIN artikelliste.flrartikelgruppe as artikelgruppe ");
		queryBuilder.append("LEFT JOIN artikelliste.flrartikelklasse as artikelklasse ");
		queryBuilder.append("LEFT JOIN artikelliste.artikelsprset as artikelspr ");
		queryBuilder.append("LEFT JOIN artikellagerplatz.flrlagerplatz as lagerplatz ");
		queryBuilder.append("LEFT JOIN artikelliste.flrmaterial as material ");
		queryBuilder.append("LEFT JOIN artikelliste.flrgeometrie as geometrie ");
		queryBuilder.append("LEFT JOIN artikelliste.flrhersteller as hersteller");

		conditions.add("(lagerplatz is null OR lagerplatz.flrlager = lager.flrlager)");

		if (artikelVonId != null) {
			conditions.add("artikelliste.c_nr >= (select art.c_nr from FLRArtikelliste art where art.i_id = "
					+ artikelVonId + ")");
		}
		if (artikelBisId != null) {
			conditions.add("artikelliste.c_nr <= (select art.c_nr from FLRArtikelliste art where art.i_id = "
					+ artikelBisId + ")");
		}
		if (lagerIId != null) {
			conditions.add("lager.flrlager.i_id = " + lagerIId);
		}
		if (lagerplatzVonId != null) {
			conditions.add(
					"lagerplatz.c_lagerplatz >= (select lplatz.c_lagerplatz from FLRLagerplatz lplatz where lplatz.i_id = "
							+ lagerplatzVonId + " )");
		}
		if (lagerplatzBisId != null) {
			conditions.add(
					"lagerplatz.c_lagerplatz <= (select lplatz.c_lagerplatz from FLRLagerplatz lplatz where lplatz.i_id = "
							+ lagerplatzBisId + " )");
		}
		if (shopgruppeIId != null) {
			conditions.add("shopgruppe.i_id = " + shopgruppeIId);
		}
		if (artikelgruppenList != null) {
			conditions.add("artikelgruppe.i_id IN " + artikelgruppenList);
		}
		if (artikelklasseIId != null) {
			conditions.add("artikelklasse.i_id = " + artikelklasseIId);
		}

		if (!conditions.isEmpty()) {
			queryBuilder.append(" WHERE ");
			Iterator<String> condIter = conditions.iterator();
			while (condIter.hasNext()) {
				String cond = condIter.next();
				queryBuilder.append(cond);
				if (condIter.hasNext()) {
					queryBuilder.append(" AND ");
				} else {
					queryBuilder.append(' ');
				}
			}
		}

		String orderProperty = null;
		switch (sortierung) {
		case SORT_ARTIKELBEZEICHNUNG:
			orderProperty = "artikelspr.c_bez";
			break;
		case SORT_ARTIKELGRUPPE:
			orderProperty = "artikelgruppe.c_nr";
			break;
		case SORT_ARTIKELKLASSE:
			orderProperty = "artikelklasse.c_nr";
			break;
		case SORT_ARTIKELKURZBEZEICHNUNG:
			orderProperty = "artikelspr.c_kbez";
			break;
		case SORT_ARTIKELNUMMER:
			orderProperty = "artikelliste.c_nr";
			break;
		case SORT_LAGERORT:
			orderProperty = "lagerplatz.c_lagerplatz";
			break;
		case SORT_REFERENZNUMMER:
			orderProperty = "artikelliste.c_referenznr";
			break;
		case SORT_SHOPGRUPPE:
			orderProperty = "shopgruppe.i_sort";
			break;
		}
		queryBuilder.append("ORDER BY ");
		queryBuilder.append(orderProperty);
		if (orderAscDesc) {
			queryBuilder.append(" ASC ");
		} else {
			queryBuilder.append(" DESC ");
		}

		Query q = session.createQuery(queryBuilder.toString());
		return q;
	}

	private boolean validateArtikelgruppenList(String artikelgruppenList) {
		if (artikelgruppenList == null || artikelgruppenList.isEmpty()) {
			return true;
		}
		// artikelgruppen sollte in folgendem Format sein (nr1,nr2,nr3,...)
		Pattern artikelGruppenListPattern = Pattern.compile("^\\((\\d+,)*(\\d+)\\)$");
		Matcher matcher = artikelGruppenListPattern.matcher(artikelgruppenList);
		return matcher.matches();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printArtikeletikett(Integer artikelIId, String sKommentar, BigDecimal bdMenge,
			Integer iExemplare, String[] cSnrChnr, Integer lagerIIdfuerLagerstandDerCharge, String lfdnr,
			String trennzeichenLfdNr, TheClientDto theClientDto) {
		// Erstellung des Reports
		ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(artikelIId, theClientDto);
		JasperPrintLP print = null;
		Integer varianteIId = theClientDto.getReportvarianteIId();

		int iAnzahlEtiketten = 1;

		int iAnzahlSnrs = 1;
		if (cSnrChnr != null && cSnrChnr.length > 0) {
			iAnzahlSnrs = cSnrChnr.length;
		}

		int iAnzahlLfndnrs = 1;
		String[] lfdnrs = null;
		if (trennzeichenLfdNr != null && lfdnr != null) {
			lfdnrs = lfdnr.split(trennzeichenLfdNr);
			iAnzahlLfndnrs = lfdnrs.length;
		}

		// PJ22281
		if (iAnzahlSnrs > 1 && iAnzahlLfndnrs > 1 && iAnzahlSnrs != iAnzahlLfndnrs) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ARTIKELETIKETT_ANZAHL_SNR_UNGLEICH_LFDNR,
					new Exception("FEHLER_ARTIKELETIKETT_ANZAHL_SNR_UNGLEICH_LFDNR"));
		}

		if (iAnzahlLfndnrs > iAnzahlSnrs) {
			iAnzahlEtiketten = iAnzahlLfndnrs;
		} else {
			iAnzahlEtiketten = iAnzahlSnrs;
		}

		MandantDto mandantDto = null;
		ArtikellieferantDto[] artikellieferantDtos = null;
		String lagerorte = null;
		BigDecimal lagerstand = null;
		HerstellerDto herstellerDto = null;
		LandDto landDto = null;
		LieferantDto lieferantDto = null;
		try {

			if (artikelDto.getLandIIdUrsprungsland() != null) {
				landDto = getSystemFac().landFindByPrimaryKey(artikelDto.getLandIIdUrsprungsland());
			}

			mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);

			if (artikelDto.getHerstellerIId() != null) {
				herstellerDto = getArtikelFac().herstellerFindByPrimaryKey(artikelDto.getHerstellerIId(), theClientDto);
			}

			Integer iIdHauplpager = getLagerFac().getHauptlagerDesMandanten(theClientDto).getIId();

			if (Helper.short2boolean(artikelDto.getBLagerbewirtschaftet())) {
				lagerstand = getLagerFac().getLagerstand(artikelIId, iIdHauplpager, theClientDto);
			}

			lagerorte = getLagerFac().getLagerplaezteEinesArtikels(artikelDto.getIId(), iIdHauplpager);

			artikellieferantDtos = getArtikelFac().artikellieferantFindByArtikelIId(artikelIId, theClientDto);

			if (artikellieferantDtos != null && artikellieferantDtos.length > 0) {
				lieferantDto = getLieferantFac().lieferantFindByPrimaryKey(artikellieferantDtos[0].getLieferantIId(),
						theClientDto);
			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		ArrayList alDaten = new ArrayList();
		for (int k = 0; k < iAnzahlEtiketten; k++) {

			index = -1;
			sAktuellerReport = ArtikelReportFac.REPORT_ARTIKELETIKETT;
			Object[] oZeile = new Object[REPORT_ARTIKELETIKETT_ANZAHL_SPALTEN];

			try {

				oZeile[REPORT_ARTIKELETIKETT_ARTIKELNUMMER] = artikelDto.getCNr();
				oZeile[REPORT_ARTIKELETIKETT_EINHEIT] = artikelDto.getEinheitCNr();
				oZeile[REPORT_ARTIKELETIKETT_REFERENZNUMMER] = artikelDto.getCReferenznr();
				oZeile[REPORT_ARTIKELETIKETT_REVISION] = artikelDto.getCRevision();
				oZeile[REPORT_ARTIKELETIKETT_INDEX] = artikelDto.getCIndex();
				oZeile[REPORT_ARTIKELETIKETT_VERPACKUNGSMENGE] = artikelDto.getFVerpackungsmenge();

				oZeile[REPORT_ARTIKELETIKETT_VERPACKUNGSMITTELMENGE] = artikelDto.getNVerpackungsmittelmenge();

				if (landDto != null) {
					oZeile[REPORT_ARTIKELETIKETT_URSPRUNGSLAND] = landDto.getCName();
				}

				if (artikelDto.getVerpackungsmittelIId() != null) {
					VerpackungsmittelDto verpackungsmittelDto = getArtikelFac().verpackungsmittelFindByPrimaryKey(
							artikelDto.getVerpackungsmittelIId(),

							theClientDto);
					oZeile[REPORT_ARTIKELETIKETT_VERPACKUNGSMITTEL_KENNUNG] = verpackungsmittelDto.getCNr();

					oZeile[REPORT_ARTIKELETIKETT_VERPACKUNGSMITTEL_BEZEICHNUNG] = verpackungsmittelDto.getBezeichnung();

					oZeile[REPORT_ARTIKELETIKETT_VERPACKUNGSMITTEL_GEWICHT_IN_KG] = verpackungsmittelDto
							.getNGewichtInKG();
				}

				oZeile[REPORT_ARTIKELETIKETT_MANDANTADRESSE] = Helper.formatMandantAdresse(mandantDto);

				if (artikelDto.getArtikelsprDto() != null) {
					oZeile[REPORT_ARTIKELETIKETT_BEZEICHNUNG] = artikelDto.getArtikelsprDto().getCBez();
					oZeile[REPORT_ARTIKELETIKETT_ZUSATZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCZbez();
					oZeile[REPORT_ARTIKELETIKETT_ZUSATZBEZEICHNUNG2] = artikelDto.getArtikelsprDto().getCZbez2();
					oZeile[REPORT_ARTIKELETIKETT_KURZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCKbez();
				}

				if (artikelDto.getMaterialIId() != null) {
					MaterialDto materialDto = getMaterialFac().materialFindByPrimaryKey(artikelDto.getMaterialIId(),
							theClientDto);
					oZeile[REPORT_ARTIKELETIKETT_MATERIAL] = materialDto.getBezeichnung();
				}

				if (artikelDto.getGeometrieDto() != null) {
					oZeile[REPORT_ARTIKELETIKETT_BREITE] = artikelDto.getGeometrieDto().getFBreite();
					oZeile[REPORT_ARTIKELETIKETT_HOEHE] = artikelDto.getGeometrieDto().getFHoehe();
					oZeile[REPORT_ARTIKELETIKETT_TIEFE] = artikelDto.getGeometrieDto().getFTiefe();
				}

				if (artikelDto.getVerpackungDto() != null) {
					oZeile[REPORT_ARTIKELETIKETT_BAUFORM] = artikelDto.getVerpackungDto().getCBauform();
					oZeile[REPORT_ARTIKELETIKETT_VERPACKUNGSART] = artikelDto.getVerpackungDto().getCVerpackungsart();
				}

				oZeile[REPORT_ARTIKELETIKETT_VERPACKUNGS_EAN] = artikelDto.getCVerpackungseannr();
				oZeile[REPORT_ARTIKELETIKETT_VERKAUFS_EAN] = artikelDto.getCVerkaufseannr();

				if (herstellerDto != null) {
					oZeile[REPORT_ARTIKELETIKETT_HERSTELLER] = herstellerDto.getCNr();
					oZeile[REPORT_ARTIKELETIKETT_HERSTELLER_NAME1] = herstellerDto.getPartnerDto()
							.getCName1nachnamefirmazeile1();
					oZeile[REPORT_ARTIKELETIKETT_HERSTELLER_NAME2] = herstellerDto.getPartnerDto()
							.getCName2vornamefirmazeile2();
				}

				if (artikellieferantDtos != null && artikellieferantDtos.length > 0 && lieferantDto != null) {
					oZeile[REPORT_ARTIKELETIKETT_LIEFERANT] = lieferantDto.getPartnerDto().formatAnrede();
					oZeile[REPORT_ARTIKELETIKETT_LIEFERANT_ARTIKELNUMMER] = artikellieferantDtos[0]
							.getCArtikelnrlieferant();
					oZeile[REPORT_ARTIKELETIKETT_LIEFERANT_ARTIKELBEZEICHNUNG] = artikellieferantDtos[0]
							.getCBezbeilieferant();
				}
				oZeile[REPORT_ARTIKELETIKETT_LAGERSTAND] = lagerstand;

				oZeile[REPORT_ARTIKELETIKETT_LAGERORT] = lagerorte;

				String snrChr = null;
				if (cSnrChnr != null && cSnrChnr.length > k) {
					snrChr = cSnrChnr[k];

					SeriennrChargennrAufLagerDto[] snrchnrDtos = getLagerFac().getAllSerienChargennrAufLagerInfoDtos(
							artikelIId, lagerIIdfuerLagerstandDerCharge, snrChr, true, null, theClientDto);

					if (snrchnrDtos != null && snrchnrDtos.length > 0) {
						oZeile[REPORT_ARTIKELETIKETT_LAGERSTAND_CHARGE] = snrchnrDtos[0].getNMenge();
					}

					if (lagerIIdfuerLagerstandDerCharge != null) {
						oZeile[REPORT_ARTIKELETIKETT_LAGER_LAGERSTAND_CHARGE] = getLagerFac()
								.lagerFindByPrimaryKey(lagerIIdfuerLagerstandDerCharge).getCNr();
					}

				}

				if (iAnzahlLfndnrs > 1) {
					lfdnr = lfdnrs[k];
				}

				oZeile[REPORT_ARTIKELETIKETT_MENGE] = bdMenge;
				oZeile[REPORT_ARTIKELETIKETT_SNRCHNR] = snrChr;
				oZeile[REPORT_ARTIKELETIKETT_LFDNR] = lfdnr;
				oZeile[REPORT_ARTIKELETIKETT_KOMMENTAR] = sKommentar;

				// letzte lagerbewegung dazu holen

				SessionFactory factory = FLRSessionFactory.getFactory();
				Session session = factory.openSession();

				org.hibernate.Criteria crit = session.createCriteria(FLRLagerbewegung.class);

				crit.createAlias(LagerFac.FLR_LAGERPLAETZE_FLRARTIKEL, "a").add(Expression.eq("a.i_id", artikelIId))
						.add(Expression.eq("b_historie", Helper.boolean2Short(false)));
				crit.createAlias(LagerFac.FLR_LAGERPLAETZE_FLRLAGER, "l");

				crit.add(Expression.eq(LagerFac.FLR_LAGERBEWEGUNG_B_ABGANG, Helper.boolean2Short(false)));
				crit.add(Expression.gt(LagerFac.FLR_LAGERBEWEGUNG_N_MENGE, BigDecimal.ZERO));

				if (snrChr != null) {
					crit.add(Expression.eq(LagerFac.FLR_LAGERBEWEGUNG_C_SERIENNRCHARGENNR, snrChr));
				} else {
					crit.add(Expression.isNull(LagerFac.FLR_LAGERBEWEGUNG_C_SERIENNRCHARGENNR));
				}

				crit.addOrder(Order.desc(LagerFac.FLR_LAGERBEWEGUNG_T_BELEGDATUM))
						.addOrder(Order.desc(LagerFac.FLR_LAGERBEWEGUNG_T_BUCHUNGSZEIT));
				List<?> resultList = crit.list();
				Iterator<?> resultListIterator = resultList.iterator();
				int i = 0;

				String[] fieldnames = new String[] { "C_BELEGART", "I_BELEGARTID", "I_BELEGARTPOSITIONID",
						"I_ID_BUCHUNG", "F_GEBINDE", "F_GEBINDEMENGE" };

				ArrayList alDatenSub = new ArrayList();

				while (resultListIterator.hasNext()) {

					FLRLagerbewegung lagerbewegung = (FLRLagerbewegung) resultListIterator.next();

					if (i == 0) {

						oZeile[REPORT_ARTIKELETIKETT_VERSION] = lagerbewegung.getC_version();

						// PJ18617
						oZeile[REPORT_ARTIKELETIKETT_PERSON_BUCHENDER] = HelperServer
								.formatPersonAusFLRPartner(lagerbewegung.getFlrpersonal().getFlrpartner());
						oZeile[REPORT_ARTIKELETIKETT_KURZZEICHEN_BUCHENDER] = lagerbewegung.getFlrpersonal()
								.getC_kurzzeichen();
					}

					// SP3576 Subreport aller Zugaenge, damit man die
					// Chargeneingenschaften per SQL holen kann

					String gebinde = null;
					if (lagerbewegung.getFlrgebinde() != null) {
						gebinde = lagerbewegung.getFlrgebinde().getC_bez();
					}

					alDatenSub.add(new Object[] { lagerbewegung.getC_belegartnr(), lagerbewegung.getI_belegartid(),
							lagerbewegung.getI_belegartpositionid(), lagerbewegung.getI_id_buchung(), gebinde,
							lagerbewegung.getN_gebindemenge() });

					i++;

				}

				Object[][] dataSub = new Object[alDatenSub.size()][fieldnames.length];
				dataSub = (Object[][]) alDatenSub.toArray(dataSub);

				oZeile[REPORT_ARTIKELETIKETT_SUBREPORT_LAGERZUBUCHUNGEN] = new LPDatenSubreport(dataSub, fieldnames);

			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}
			oZeile[REPORT_ARTIKELETIKETT_EXEMPLAREGESAMT] = iExemplare;

			for (int i = 0; i < iExemplare; i++) {

				Object[] oZeileKopie = oZeile.clone();
				oZeileKopie[REPORT_ARTIKELETIKETT_EXEMPLAR] = new Integer(i + 1);
				alDaten.add(oZeileKopie);
			}

		}

		Object[][] returnArray = new Object[alDaten.size()][REPORT_ARTIKELETIKETT_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(returnArray);

		HashMap<String, Object> parameter = new HashMap<String, Object>();
		initJRDS(parameter, ArtikelReportFac.REPORT_MODUL, ArtikelReportFac.REPORT_ARTIKELETIKETT,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		print = getReportPrint();

		PrintInfoDto values = new PrintInfoDto();
		values.setDocPath(new DocPath(new DocNodeArtikeletikett(artikelDto)));
		print.setOInfoForArchive(values);

		return print;

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printKundensokos(Integer artikelIId, TheClientDto theClientDto) {
		Integer iPreisbasis = null;
		try {
			ParametermandantDto param = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_PREISBASIS_VERKAUF);
			iPreisbasis = (Integer) param.getCWertAsObject();
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		boolean darfVerkaufspreisSehen = getTheJudgeFac().hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF,
				theClientDto);

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		ArtikelDto dto = getArtikelFac().artikelFindByPrimaryKey(artikelIId, theClientDto);
		parameter.put("P_ARTIKEL", dto.formatArtikelbezeichnung());
		parameter.put("P_ARTIKELREFERENZNUMMER", dto.getCReferenznr());
		sAktuellerReport = ArtikelReportFac.REPORT_KUNDENSOKOS;

		String sQuery = "SELECT s FROM FLRKundesokomengenstaffel s WHERE s.flrkundesoko.artikel_i_id=" + artikelIId;

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
			FLRKundesokomengenstaffel flrKundesokomengenstaffel = (FLRKundesokomengenstaffel) resultListIterator.next();
			Object[] zeile = new Object[REPORT_KUNDENSOKOS_ANZAHL_FELDER];

			zeile[REPORT_KUNDENSOKOS_KUNDE_NAME1] = flrKundesokomengenstaffel.getFlrkundesoko().getFlrkunde()
					.getFlrpartner().getC_name1nachnamefirmazeile1();
			zeile[REPORT_KUNDENSOKOS_KUNDE_NAME2] = flrKundesokomengenstaffel.getFlrkundesoko().getFlrkunde()
					.getFlrpartner().getC_name2vornamefirmazeile2();
			zeile[REPORT_KUNDENSOKOS_KUNDE_KBEZ] = flrKundesokomengenstaffel.getFlrkundesoko().getFlrkunde()
					.getFlrpartner().getC_kbez();

			zeile[REPORT_KUNDENSOKOS_KUNDE_UID] = flrKundesokomengenstaffel.getFlrkundesoko().getFlrkunde()
					.getFlrpartner().getC_uid();
			if (flrKundesokomengenstaffel.getFlrkundesoko().getFlrkunde().getFlrpartner().getFlrlandplzort() != null) {
				zeile[REPORT_KUNDENSOKOS_KUNDE_LKZ] = flrKundesokomengenstaffel.getFlrkundesoko().getFlrkunde()
						.getFlrpartner().getFlrlandplzort().getFlrland().getC_lkz();
				zeile[REPORT_KUNDENSOKOS_KUNDE_PLZ] = flrKundesokomengenstaffel.getFlrkundesoko().getFlrkunde()
						.getFlrpartner().getFlrlandplzort().getC_plz();
				zeile[REPORT_KUNDENSOKOS_KUNDE_ORT] = flrKundesokomengenstaffel.getFlrkundesoko().getFlrkunde()
						.getFlrpartner().getFlrlandplzort().getFlrort().getC_name();
			}

			zeile[REPORT_KUNDENSOKOS_KUNDE_MENGE] = flrKundesokomengenstaffel.getN_menge();
			zeile[REPORT_KUNDENSOKOS_KUNDE_KDARTIKELNUMMER] = flrKundesokomengenstaffel.getFlrkundesoko()
					.getC_kundeartikelnummer();

			try {
				KundesokoDto kdsokoDto = getKundesokoFac()
						.kundesokoFindByPrimaryKey(flrKundesokomengenstaffel.getFlrkundesoko().getI_id());

				zeile[REPORT_KUNDENSOKOS_KUNDE_WIRKT_NICHT_IN_PREISFINDUNG] = Helper
						.short2Boolean(kdsokoDto.getBWirktNichtFuerPreisfindung());

				zeile[REPORT_KUNDENSOKOS_KUNDE_KDARTIKELNUMMER] = flrKundesokomengenstaffel.getFlrkundesoko()
						.getC_kundeartikelnummer();
				zeile[REPORT_KUNDENSOKOS_KUNDE_KDARTIKELBEZ] = kdsokoDto.getCKundeartikelbez();
				zeile[REPORT_KUNDENSOKOS_KUNDE_KDARTIKELBEZ2] = kdsokoDto.getCKundeartikelzbez();

				// Preis berechnen
				BigDecimal nBerechneterPreis = null;
				BigDecimal nBerechneterPreisKundenwaehrung = null;
				BigDecimal nBerechneterPreisHeute = null;

				if (kdsokoDto.getArtikelIId() != null || kdsokoDto.getArtgruIId() != null) {
					// der Preis muss an dieser Stelle berechnet werden

					if (flrKundesokomengenstaffel.getN_fixpreis() != null) {
						nBerechneterPreis = flrKundesokomengenstaffel.getN_fixpreis();
						nBerechneterPreisHeute = flrKundesokomengenstaffel.getN_fixpreis();
						// Fixpreis ist in Kundenwaehrung -> nach
						// Mandantenwehrung umrechnen
						KundeDto kdDto = getKundeFac().kundeFindByPrimaryKeySmall(
								flrKundesokomengenstaffel.getFlrkundesoko().getFlrkunde().getI_id());

						try {
							if (!kdDto.getCWaehrung().equals(theClientDto.getSMandantenwaehrung())) {
								nBerechneterPreis = getLocaleFac().rechneUmInAndereWaehrungZuDatum(
										flrKundesokomengenstaffel.getN_fixpreis(), kdDto.getCWaehrung(),
										theClientDto.getSMandantenwaehrung(),
										Helper.cutDate(new java.sql.Date(System.currentTimeMillis())), theClientDto);
								nBerechneterPreisKundenwaehrung = flrKundesokomengenstaffel.getN_fixpreis();
							}
						} catch (RemoteException e) {
							throwEJBExceptionLPRespectOld(e);
						}

					} else {
						// WH 21.06.06 Es gilt die VK-Basis, die zu Beginn der
						// Mengenstaffel gueltig ist
						BigDecimal nPreisbasis = null;
						BigDecimal nPreisbasisKundenwaehrung = null;
						BigDecimal nPreisbasisHeute = null;
						if (iPreisbasis == 0 || iPreisbasis == 2) {

							nPreisbasis = getVkPreisfindungFac().ermittlePreisbasis(artikelIId,
									new java.sql.Date(kdsokoDto.getTPreisgueltigab().getTime()), null,
									theClientDto.getSMandantenwaehrung(), theClientDto);

							nPreisbasisKundenwaehrung = getVkPreisfindungFac().ermittlePreisbasis(artikelIId,
									new java.sql.Date(kdsokoDto.getTPreisgueltigab().getTime()), null,
									flrKundesokomengenstaffel.getFlrkundesoko().getFlrkunde().getWaehrung_c_nr(),
									theClientDto);
							nPreisbasisHeute = getVkPreisfindungFac().ermittlePreisbasis(artikelIId,
									new java.sql.Date(System.currentTimeMillis()), null,
									theClientDto.getSMandantenwaehrung(), theClientDto);
						} else {
							nPreisbasis = getVkPreisfindungFac().ermittlePreisbasis(artikelIId,
									new java.sql.Date(kdsokoDto.getTPreisgueltigab().getTime()),
									flrKundesokomengenstaffel.getFlrkundesoko().getFlrkunde()
											.getVkpfartikelpreisliste_i_id_stdpreisliste(),
									theClientDto.getSMandantenwaehrung(), theClientDto);
							nPreisbasisKundenwaehrung = getVkPreisfindungFac().ermittlePreisbasis(artikelIId,
									new java.sql.Date(kdsokoDto.getTPreisgueltigab().getTime()),
									flrKundesokomengenstaffel.getFlrkundesoko().getFlrkunde()
											.getVkpfartikelpreisliste_i_id_stdpreisliste(),
									flrKundesokomengenstaffel.getFlrkundesoko().getFlrkunde().getWaehrung_c_nr(),
									theClientDto);
							nPreisbasisHeute = getVkPreisfindungFac().ermittlePreisbasis(artikelIId,
									new java.sql.Date(System.currentTimeMillis()),
									flrKundesokomengenstaffel.getFlrkundesoko().getFlrkunde()
											.getVkpfartikelpreisliste_i_id_stdpreisliste(),
									theClientDto.getSMandantenwaehrung(), theClientDto);
						}

						VerkaufspreisDto vkpfDto = getVkPreisfindungFac().berechneVerkaufspreis(nPreisbasis,
								flrKundesokomengenstaffel.getF_rabattsatz(), theClientDto);

						if (vkpfDto != null) {
							nBerechneterPreis = vkpfDto.nettopreis;
						} else {
							// Wahrscheinlich keine VK-Preisbasis verfuegbar
							VkPreisfindungEinzelverkaufspreisDto[] dtos = getVkPreisfindungFac()
									.vkPreisfindungEinzelverkaufspreisfindByMandantCNrArtikelIIdAbGueltigab(artikelIId,
											new java.sql.Date(kdsokoDto.getTPreisgueltigab().getTime()), theClientDto);
							if (dtos.length > 0) {
								zeile[REPORT_KUNDENSOKOS_KUNDE_NAECHSTE_VKPREISBASIS_GUELTIG_AB] = dtos[0]
										.getTVerkaufspreisbasisgueltigab();
							}

						}

						VerkaufspreisDto vkpfDtoKundenwaehrung = getVkPreisfindungFac().berechneVerkaufspreis(
								nPreisbasisKundenwaehrung, flrKundesokomengenstaffel.getF_rabattsatz(), theClientDto);
						if (vkpfDto != null) {
							nBerechneterPreisKundenwaehrung = vkpfDtoKundenwaehrung.nettopreis;
						}

						VerkaufspreisDto vkpfDtoHeute = getVkPreisfindungFac().berechneVerkaufspreis(nPreisbasisHeute,
								flrKundesokomengenstaffel.getF_rabattsatz(), theClientDto);

						if (vkpfDtoHeute != null) {
							nBerechneterPreisHeute = vkpfDtoHeute.nettopreis;
						}

					}
				}
				if (darfVerkaufspreisSehen) {

					zeile[REPORT_KUNDENSOKOS_KUNDE_BERECHNETER_PREIS] = nBerechneterPreis;

					if (flrKundesokomengenstaffel.getFlrkundesoko().getT_preisgueltigbis() == null
							|| flrKundesokomengenstaffel.getFlrkundesoko().getT_preisgueltigbis()
									.after(new java.sql.Date(System.currentTimeMillis()))) {
						zeile[REPORT_KUNDENSOKOS_KUNDE_BERECHNETER_PREIS_ZU_HEUTE] = nBerechneterPreisHeute;

					}

					zeile[REPORT_KUNDENSOKOS_KUNDE_BERECHNETER_PREIS_KDWAEHRUNG] = nBerechneterPreisKundenwaehrung;
				}
				zeile[REPORT_KUNDENSOKOS_KUNDE_KDWAEHRUNG] = flrKundesokomengenstaffel.getFlrkundesoko().getFlrkunde()
						.getWaehrung_c_nr();

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			zeile[REPORT_KUNDENSOKOS_KUNDE_GUELTIAGAB] = flrKundesokomengenstaffel.getFlrkundesoko()
					.getT_preisgueltigab();
			zeile[REPORT_KUNDENSOKOS_KUNDE_GUELTIAGBIS] = flrKundesokomengenstaffel.getFlrkundesoko()
					.getT_preisgueltigbis();
			if (darfVerkaufspreisSehen) {
				zeile[REPORT_KUNDENSOKOS_KUNDE_FIXPREIS] = flrKundesokomengenstaffel.getN_fixpreis();
				zeile[REPORT_KUNDENSOKOS_KUNDE_RABATT] = flrKundesokomengenstaffel.getF_rabattsatz();
			}

			alDaten.add(zeile);
		}

		Object[][] returnArray = new Object[alDaten.size()][REPORT_KUNDENSOKOS_ANZAHL_FELDER];
		data = (Object[][]) alDaten.toArray(returnArray);

		initJRDS(parameter, ArtikelReportFac.REPORT_MODUL, ArtikelReportFac.REPORT_KUNDENSOKOS,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printWerkzeugVerschleissteil(Integer werkzeugIId, Integer verschleisteilIId,
			TheClientDto theClientDto) {
		// Erstellung des Reports
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		index = -1;
		sAktuellerReport = ArtikelReportFac.REPORT_VERSCHLEISSTEILWERKZEUG;
		data = new Object[1][REPORT_VERSCHLEISSTEILWERKZEUG_ANZAHL_SPALTEN];

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT vw " + " FROM FLRVerschleissteilwerkzeug AS vw WHERE 1=1 ";

		if (werkzeugIId != null) {
			sQuery += " AND vw.werkzeug_i_id= " + werkzeugIId;

			WerkzeugDto dto = getArtikelFac().werkzeugFindByPrimaryKey(werkzeugIId);
			parameter.put("P_WERKZEUG", dto.getBezeichnung());
		}
		if (verschleisteilIId != null) {
			sQuery += " AND vw.verschleissteil_i_id= " + verschleisteilIId;

			VerschleissteilDto dto = getArtikelFac().verschleissteilFindByPrimaryKey(verschleisteilIId);
			parameter.put("P_VERSCHLEISSTEIL", dto.getBezeichnung());
		}

		sQuery += " ORDER BY vw.flrverschleissteil.c_nr ASC, vw.flrwerkzeug.c_nr ASC";

		Query inventurliste = session.createQuery(sQuery);

		List<?> resultList = inventurliste.list();

		Iterator<?> resultListIterator = resultList.iterator();

		ArrayList alDaten = new ArrayList();

		data = new Object[resultList.size()][6];

		while (resultListIterator.hasNext()) {
			FLRVerschleissteilwerkzeug vw = (FLRVerschleissteilwerkzeug) resultListIterator.next();

			Object[] oZeile = new Object[REPORT_VERSCHLEISSTEILWERKZEUG_ANZAHL_SPALTEN];

			oZeile[REPORT_VERSCHLEISSTEILWERKZEUG_VERSCHLEISSTEIL] = vw.getFlrverschleissteil().getC_nr();
			oZeile[REPORT_VERSCHLEISSTEILWERKZEUG_VERSCHLEISSTEIL_BEZEICHNUNG] = vw.getFlrverschleissteil().getC_bez();
			oZeile[REPORT_VERSCHLEISSTEILWERKZEUG_VERSCHLEISSTEIL_BEZEICHNUNG2] = vw.getFlrverschleissteil()
					.getC_bez2();

			oZeile[REPORT_VERSCHLEISSTEILWERKZEUG_WERKZEUG] = vw.getFlrwerkzeug().getC_nr();
			oZeile[REPORT_VERSCHLEISSTEILWERKZEUG_WERKZEUG_BEZEICHNUNG] = vw.getFlrwerkzeug().getC_bez();
			oZeile[REPORT_VERSCHLEISSTEILWERKZEUG_WERKZEUG_MANDANT] = vw.getFlrwerkzeug().getMandant_c_nr_standort();

			alDaten.add(oZeile);

		}
		session.close();

		data = new Object[alDaten.size()][REPORT_VERSCHLEISSTEILWERKZEUG_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(data);

		initJRDS(parameter, ArtikelReportFac.REPORT_MODUL, ArtikelReportFac.REPORT_VERSCHLEISSTEILWERKZEUG,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printLagerplatzetikett(Integer lagerplatzIId, TheClientDto theClientDto) {
		// Erstellung des Reports
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		index = -1;
		sAktuellerReport = ArtikelReportFac.REPORT_LAGERPLATZETIKETT;
		data = new Object[1][REPORT_LAGERPLATZ_ANZAHL_SPALTEN];

		try {
			LagerplatzDto lagerplatzDto = getLagerFac().lagerplatzFindByPrimaryKey(lagerplatzIId);
			data[0][REPORT_LAGERPLATZ_LAGERPLATZ] = lagerplatzDto.getCLagerplatz();

			LagerDto lagerDto = getLagerFac().lagerFindByPrimaryKey(lagerplatzDto.getLagerIId());
			data[0][REPORT_LAGERPLATZ_LAGER] = lagerDto.getCNr();

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		initJRDS(parameter, ArtikelReportFac.REPORT_MODUL, ArtikelReportFac.REPORT_LAGERPLATZETIKETT,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printMindestlagerstaende(TheClientDto theClientDto) {

		sAktuellerReport = ArtikelReportFac.REPORT_MINDESTLAGERSTAENDE;

		boolean lagerminJeLager = false;
		try {
			ParametermandantDto parameterDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_LAGERMIN_JE_LAGER);

			lagerminJeLager = (Boolean) parameterDto.getCWertAsObject();
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		String sQuery = null;

		if (lagerminJeLager == false) {

			sQuery = "SELECT a,(SELECT sum(artikellager.n_lagerstand) FROM FLRArtikellager AS artikellager WHERE artikellager.compId.artikel_i_id=a.i_id "
					+ "AND artikellager.mandant_c_nr=a.mandant_c_nr AND artikellager.flrlager.b_konsignationslager=0 AND artikellager.flrlager.lagerart_c_nr NOT IN('"
					+ LagerFac.LAGERART_WERTGUTSCHRIFT + "')), "
					+ " ( SELECT sum(r.n_gesamtmenge) FROM FLRRahmenbedarfe r WHERE r.flrartikel.i_id=a.i_id )"
					+ "FROM FLRArtikel a WHERE a.b_versteckt=0 AND mandant_c_nr='" + theClientDto.getMandant()
					+ "' AND (a.f_lagermindest IS NOT NULL OR a.f_fertigungssatzgroesse IS NOT NULL )"
					+ "AND (a.f_lagermindest >0 OR a.f_fertigungssatzgroesse >0) AND a.artikelart_c_nr NOT IN ('Handartikel') ORDER BY a.c_nr";
		} else {
			sQuery = "SELECT al, "
					+ " ( SELECT sum(r.n_gesamtmenge) FROM FLRRahmenbedarfe r WHERE r.flrartikel.i_id=al.flrartikel.i_id )"
					+ "FROM FLRArtikellager al WHERE al.flrartikel.b_versteckt=0 AND al.flrlager.mandant_c_nr='"
					+ theClientDto.getMandant()
					+ "' AND (al.f_lagermindest IS NOT NULL OR al.flrartikel.f_fertigungssatzgroesse IS NOT NULL )"
					+ "AND (al.f_lagermindest >0 OR al.flrartikel.f_fertigungssatzgroesse >0) AND al.flrartikel.artikelart_c_nr NOT IN ('Handartikel') ORDER BY al.flrartikel.c_nr, al.flrlager.c_nr";
		}

		Session session = FLRSessionFactory.getFactory().openSession();

		Query inventurliste = session.createQuery(sQuery);

		List<?> resultList = inventurliste.list();

		Iterator<?> resultListIterator = resultList.iterator();

		data = new Object[resultList.size()][REPORT_MINDESTLAGERSTAENDE_ANZAHL_SPALTEN];

		int row = 0;
		while (resultListIterator.hasNext()) {
			Object o[] = (Object[]) resultListIterator.next();

			FLRArtikel a = null;

			BigDecimal lagerstand = null;
			BigDecimal detailbedarfe = null;

			if (lagerminJeLager == false) {
				a = (FLRArtikel) o[0];

				lagerstand = (BigDecimal) o[1];
				detailbedarfe = (BigDecimal) o[2];

				data[row][REPORT_MINDESTLAGERSTAENDE_LAGERMINDESTSTAND] = a.getF_lagermindest();
				data[row][REPORT_MINDESTLAGERSTAENDE_LAGERSOLLSTAND] = a.getF_lagersoll();

			} else {

				FLRArtikellager flrartikellager = (FLRArtikellager) o[0];

				a = flrartikellager.getFlrartikel();

				lagerstand = flrartikellager.getN_lagerstand();
				detailbedarfe = (BigDecimal) o[1];

				data[row][REPORT_MINDESTLAGERSTAENDE_LAGER] = flrartikellager.getFlrlager().getC_nr();
				data[row][REPORT_MINDESTLAGERSTAENDE_LAGERMINDESTSTAND] = flrartikellager.getF_lagermindest();
				data[row][REPORT_MINDESTLAGERSTAENDE_LAGERSOLLSTAND] = flrartikellager.getF_lagersoll();

			}

			data[row][REPORT_MINDESTLAGERSTAENDE_ARTIKEL] = a.getC_nr();
			data[row][REPORT_MINDESTLAGERSTAENDE_ARTIKELREFERENZNUMMER] = a.getC_referenznr();
			data[row][REPORT_MINDESTLAGERSTAENDE_FERTIGUNGSSATZGROESSE] = a.getF_fertigungssatzgroesse();

			data[row][REPORT_MINDESTLAGERSTAENDE_LAGERSTAND] = lagerstand;
			data[row][REPORT_MINDESTLAGERSTAENDE_DETAILBEDARF] = detailbedarfe;

			try {
				data[row][REPORT_MINDESTLAGERSTAENDE_LETZTER_ABGANG] = getLagerFac()
						.getDatumLetzterZugangsOderAbgangsbuchung(a.getI_id(), true);

				ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(a.getI_id(), theClientDto);

				if (artikelDto.getArtikelsprDto() != null) {
					data[row][REPORT_MINDESTLAGERSTAENDE_BEZEICHNUNG] = artikelDto.getArtikelsprDto().getCBez();
					data[row][REPORT_MINDESTLAGERSTAENDE_ZUSATZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCZbez();
					data[row][REPORT_MINDESTLAGERSTAENDE_ZUSATZBEZEICHNUNG2] = artikelDto.getArtikelsprDto()
							.getCZbez2();
					data[row][REPORT_MINDESTLAGERSTAENDE_KURZBEZEICHNUNG] = artikelDto.getArtikelsprDto().getCKbez();
				}

				Hashtable<?, ?> htRahmenbestellt = getArtikelbestelltFac().getAnzahlRahmenbestellt(a.getI_id(),
						theClientDto);
				if (htRahmenbestellt.containsKey(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL)) {
					data[row][REPORT_MINDESTLAGERSTAENDE_RAHMENBESTELLT] = htRahmenbestellt
							.get(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL);
				}

				data[row][REPORT_MINDESTLAGERSTAENDE_RAHMENRESERVIERT] = getReservierungFac()
						.getAnzahlRahmenreservierungen(a.getI_id(), theClientDto);

				// MinVK
				VkPreisfindungEinzelverkaufspreisDto vkpfDto = getVkPreisfindungFac().getArtikeleinzelverkaufspreis(
						a.getI_id(), new java.sql.Date(System.currentTimeMillis()),
						theClientDto.getSMandantenwaehrung(), theClientDto);
				if (vkpfDto != null && vkpfDto.getNVerkaufspreisbasis() != null) {
					data[row][REPORT_MINDESTLAGERSTAENDE_VK_PREISBASIS] = vkpfDto.getNVerkaufspreisbasis();
				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			row++;
		}
		session.close();

		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("P_LAGERMIN_JE_LAGER", lagerminJeLager);

		initJRDS(parameter, ArtikelbestelltFac.REPORT_MODUL, ArtikelReportFac.REPORT_MINDESTLAGERSTAENDE,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAenderungen(Integer artikelIId, TheClientDto theClientDto) {

		if (artikelIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelIId == null"));
		}
		// Erstellung des Reports
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		ArtikelDto dto = getArtikelFac().artikelFindByPrimaryKey(artikelIId, theClientDto);
		parameter.put("P_ARTIKEL", dto.formatArtikelbezeichnung());
		parameter.put("P_ARTIKELREFERENZNUMMER", dto.getCReferenznr());

		index = -1;
		sAktuellerReport = ArtikelReportFac.REPORT_AENDERUNGEN;

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session.createCriteria(FLRArtikellog.class);
		crit.add(Restrictions.eq("artikel_i_id", artikelIId));
		crit.addOrder(Order.desc("t_aendern"));
		List<?> results = crit.list();
		Iterator<?> resultListIterator = results.iterator();

		data = new Object[results.size()][ArtikelReportFacBean.REPORT_AENDERUNGEN_ANZAHL_SPALTEN];

		int i = 0;
		while (resultListIterator.hasNext()) {
			FLRArtikellog flrArtikellog = (FLRArtikellog) resultListIterator.next();
			data[i][ArtikelReportFacBean.REPORT_AENDERUNGEN_EIGENSCHAFT] = flrArtikellog.getC_key();
			data[i][ArtikelReportFacBean.REPORT_AENDERUNGEN_VON] = flrArtikellog.getC_von();
			data[i][ArtikelReportFacBean.REPORT_AENDERUNGEN_NACH] = flrArtikellog.getC_nach();
			data[i][ArtikelReportFacBean.REPORT_AENDERUNGEN_SPRACHE] = flrArtikellog.getLocale_c_nr();
			data[i][ArtikelReportFacBean.REPORT_AENDERUNGEN_WANN] = flrArtikellog.getT_aendern();

			data[i][ArtikelReportFacBean.REPORT_AENDERUNGEN_WER] = HelperServer
					.formatNameAusFLRPartner(flrArtikellog.getFlrpersonal().getFlrpartner());

			i++;
		}

		initJRDS(parameter, ArtikelbestelltFac.REPORT_MODUL, ArtikelReportFac.REPORT_AENDERUNGEN,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printSeriennummernStammblatt(Integer lagerIId, Integer artikelIId, String[] snrs,
			String snrWildcard, Boolean bSortNachIdent, String versionWildcard, boolean bNurSeriennummern,
			boolean nurObersteEbene, TheClientDto theClientDto) {

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT l.flrartikel, l.n_menge, l.t_buchungszeit, l.c_seriennrchargennr, l.c_belegartnr, l.i_belegartid, l.i_belegartpositionid, l.b_abgang, l.c_version, (SELECT spr.c_bez FROM FLRArtikellistespr spr WHERE spr.Id.artikelliste.i_id=l.artikel_i_id AND spr.Id.locale.c_nr='"
				+ theClientDto.getLocUiAsString()
				+ "') as bez, (SELECT spr.c_zbez FROM FLRArtikellistespr spr WHERE spr.Id.artikelliste.i_id=l.artikel_i_id AND spr.Id.locale.c_nr='"
				+ theClientDto.getLocUiAsString() + "') as zbez, l.flrlager FROM FLRLagerbewegung as l "
				+ " WHERE l.flrlager.lagerart_c_nr NOT IN('" + LagerFac.LAGERART_WERTGUTSCHRIFT
				+ "') AND l.n_menge > 0 AND l.b_historie=0 AND l.n_menge>0 AND l.c_seriennrchargennr IS NOT NULL ";

		// SP18698
		Boolean bZentralerArtikelstamm;
		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
				theClientDto)) {
			// Wenn zentraler Artikelstamm, dann alle Bewegungen anzeigen
			bZentralerArtikelstamm = Boolean.TRUE;

		} else {
			bZentralerArtikelstamm = Boolean.FALSE;

			queryString += " AND l.flrlager.mandant_c_nr='" + theClientDto.getMandant() + "' ";
		}

		if (lagerIId != null) {

			queryString += " AND l.flrlager.i_id=" + lagerIId;

		}

		if (artikelIId != null) {
			queryString += " AND l.flrartikel.i_id=" + artikelIId;

		}

		if (snrs != null) {
			String snrsIn = "";

			for (int i = 0; i < snrs.length; i++) {
				snrsIn += "'" + snrs[i].toLowerCase() + "'";
				if (i != snrs.length - 1) {
					snrsIn += ",";
				}
			}
			queryString += " AND lower(l.c_seriennrchargennr) IN(" + snrsIn + ")";

		} else if (snrWildcard != null) {

			queryString += " AND lower(l.c_seriennrchargennr) LIKE '%" + snrWildcard.toLowerCase() + "%'";

		}

		if (versionWildcard != null) {

			queryString += " AND l.c_version LIKE '%" + versionWildcard + "%'";

		}

		if (bSortNachIdent.booleanValue() == true) {

			queryString += " ORDER BY l.flrartikel.c_nr, l.c_seriennrchargennr, l.t_buchungszeit";

		} else {

			queryString += " ORDER BY l.c_seriennrchargennr ASC, l.flrartikel.c_nr, l.t_buchungszeit ASC";

		}

		org.hibernate.Query query = session.createQuery(queryString);

		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();

		index = -1;
		sAktuellerReport = ArtikelReportFac.REPORT_SNRSTAMMBLATT;

		ArrayList<Object[]> alDaten = new ArrayList<Object[]>();

		while (resultListIterator.hasNext()) {

			Object[] o = (Object[]) resultListIterator.next();

			FLRArtikel flrartikel = (FLRArtikel) o[0];
			BigDecimal menge = (BigDecimal) o[1];
			java.util.Date buchungszeit = (java.util.Date) o[2];
			String cSnrChnr = (String) o[3];
			String belegartCNr = (String) o[4];
			Integer belegartIId = (Integer) o[5];
			Integer belegartpositionIId = (Integer) o[6];
			Short bAbgang = (Short) o[7];
			String cVersion = (String) o[8];

			String cBez = (String) o[9];
			String cZbez = (String) o[10];

			FLRLager flrLager = (FLRLager) o[11];

			Object[] dataHelp = new Object[REPORT_SNRSTAMMBLATT_ANZAHL_SPALTEN];
			dataHelp[REPORT_SNRSTAMMBLATT_ZEITPUNKT] = buchungszeit;
			dataHelp[REPORT_SNRSTAMMBLATT_MENGE] = menge;
			dataHelp[REPORT_SNRSTAMMBLATT_EINHEIT] = flrartikel.getEinheit_c_nr();
			dataHelp[REPORT_SNRSTAMMBLATT_ARTIKELNUMMER] = flrartikel.getC_nr();
			dataHelp[REPORT_SNRSTAMMBLATT_VERSION] = cVersion;

			if (Helper.short2boolean(flrartikel.getB_seriennrtragend()) == true
					&& belegartCNr.equals(LocaleFac.BELEGART_LOSABLIEFERUNG)) {

				javax.persistence.Query queryGsnr = em.createNamedQuery("GeraetesnrfindByArtikelIIdCSnr");
				queryGsnr.setParameter(1, flrartikel.getI_id());
				queryGsnr.setParameter(2, cSnrChnr);
				Collection c = queryGsnr.getResultList();
				Iterator<?> iterator = c.iterator();
				Object[][] dataSub = new Object[c.size()][3];
				String[] fieldnames = new String[] { "F_ARTIKELNUMMER", "F_BEZEICHNUNG", "F_SNR" };
				int i = 0;
				while (iterator.hasNext()) {
					Geraetesnr g = (Geraetesnr) iterator.next();

					Session session2 = FLRSessionFactory.getFactory().openSession();

					org.hibernate.Criteria crit2 = session.createCriteria(FLRLagerbewegung.class);
					crit2.add(Restrictions.eq(LagerFac.FLR_LAGERBEWEGUNG_B_HISTORIE, Helper.boolean2Short(false)));
					crit2.add(Restrictions.eq(LagerFac.FLR_LAGERBEWEGUNG_I_ID_BUCHUNG, g.getIIdBuchung()));

					List l = crit2.list();
					if (l.size() > 0) {
						FLRLagerbewegung flrLagerbew = (FLRLagerbewegung) l.iterator().next();
						dataSub[i][0] = flrLagerbew.getFlrartikel().getC_nr();

						ArtikelDto aDto = getArtikelFac()
								.artikelFindByPrimaryKeySmall(flrLagerbew.getFlrartikel().getI_id(), theClientDto);
						dataSub[i][1] = aDto.formatBezeichnung();

						dataSub[i][2] = flrLagerbew.getC_seriennrchargennr();
					}

					session2.close();
					i++;
				}
				if (dataSub.length > 0) {
					dataHelp[REPORT_SNRSTAMMBLATT_SUBREPORT_GERAETESNR] = new LPDatenSubreport(dataSub, fieldnames);
				}
			}

			String artikelbez = "";

			if (cBez != null) {
				artikelbez += cBez + " ";
			}
			if (cZbez != null) {
				artikelbez += cZbez;
			}

			dataHelp[REPORT_SNRSTAMMBLATT_LAGER] = flrLager.getC_nr();

			dataHelp[REPORT_SNRSTAMMBLATT_ARTIKELBEZEICHNUNG] = artikelbez;

			dataHelp[REPORT_SNRSTAMMBLATT_SERIENNUMER] = cSnrChnr;

			String cAbgang = "";
			String cZugang = "";

			if (Helper.short2boolean(bAbgang)) {

				if (belegartCNr.equals(LocaleFac.BELEGART_HAND)) {

					Handlagerbewegung handlagerbewegung = em.find(Handlagerbewegung.class, belegartpositionIId);
					if (handlagerbewegung != null) {
						cAbgang = LocaleFac.BELEGART_HAND.trim() + belegartpositionIId + " "
								+ handlagerbewegung.getCKommentar();
					} else {
						cAbgang = LocaleFac.BELEGART_HAND.trim() + belegartpositionIId
								+ " Error - Handbuchung not found";
					}

				} else {

					cAbgang = getLagerFac().getBelegUndPartner(belegartCNr, belegartIId, belegartpositionIId,
							theClientDto);
				}
			} else {

				if (belegartCNr.equals(LocaleFac.BELEGART_HAND)) {

					Handlagerbewegung handlagerbewegung = em.find(Handlagerbewegung.class, belegartpositionIId);
					if (handlagerbewegung != null) {
						cZugang = LocaleFac.BELEGART_HAND.trim() + belegartpositionIId + " "
								+ handlagerbewegung.getCKommentar();
					} else {
						cZugang = LocaleFac.BELEGART_HAND.trim() + belegartpositionIId
								+ " Error - Handbuchung not found";
					}

				} else {

					cZugang = getLagerFac().getBelegUndPartner(belegartCNr, belegartIId, belegartpositionIId,
							theClientDto);
				}
			}
			dataHelp[REPORT_SNRSTAMMBLATT_ABGANG] = cAbgang;
			dataHelp[REPORT_SNRSTAMMBLATT_ZUGANG] = cZugang;

			if (belegartCNr.equals(LocaleFac.BELEGART_LOSABLIEFERUNG)) {
				dataHelp[REPORT_SNRSTAMMBLATT_SUBREPORT_LOS_GESAMTKALKULATION] = getSubreportGesamtkalkulationFuerSnrStammblatt(
						belegartIId, belegartpositionIId, theClientDto);

			} else if (belegartCNr.equals(LocaleFac.BELEGART_LOS)) {
				try {
					LosistmaterialDto liDto = getFertigungFac().losistmaterialFindByPrimaryKey(belegartpositionIId);

					LossollmaterialDto sollDto = getFertigungFac()
							.lossollmaterialFindByPrimaryKey(liDto.getLossollmaterialIId());

					javax.persistence.Query queryGsnr = em.createNamedQuery("GeraetesnrfindByArtikelIIdCSnr");
					queryGsnr.setParameter(1, sollDto.getArtikelIId());
					queryGsnr.setParameter(2, cSnrChnr);
					Collection c = queryGsnr.getResultList();
					Iterator<?> iterator = c.iterator();

					if (c.size() == 0) {
						dataHelp[REPORT_SNRSTAMMBLATT_SUBREPORT_LOS_GESAMTKALKULATION] = getSubreportGesamtkalkulationFuerSnrStammblatt(
								sollDto.getLosIId(), null, theClientDto);
					} else {
						Geraetesnr g = (Geraetesnr) iterator.next();
						org.hibernate.Criteria crit2 = session.createCriteria(FLRLagerbewegung.class);
						crit2.add(Restrictions.eq(LagerFac.FLR_LAGERBEWEGUNG_B_HISTORIE, Helper.boolean2Short(false)));
						crit2.add(Restrictions.eq(LagerFac.FLR_LAGERBEWEGUNG_I_ID_BUCHUNG, g.getIIdBuchung()));

						List l = crit2.list();
						if (l.size() > 0) {
							FLRLagerbewegung flrLagerbew = (FLRLagerbewegung) l.iterator().next();
							if (flrLagerbew.getC_belegartnr().equals(LocaleFac.BELEGART_LOSABLIEFERUNG)
									&& sollDto.getLosIId().equals(flrLagerbew.getI_belegartid())) {
								dataHelp[REPORT_SNRSTAMMBLATT_SUBREPORT_LOS_GESAMTKALKULATION] = getSubreportGesamtkalkulationFuerSnrStammblatt(
										sollDto.getLosIId(), flrLagerbew.getI_belegartpositionid(), theClientDto);
							}

						}

					}

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}

			alDaten.add(dataHelp);
		}
		session.close();

		reklamationHinzufuegen(theClientDto, alDaten, artikelIId, bSortNachIdent, snrs, snrWildcard);
		auftragsseriennummernHinzufuegen(theClientDto, alDaten, artikelIId, bSortNachIdent, snrs, snrWildcard);
		// Auftragsseriennummern

		for (int m = alDaten.size() - 1; m > 0; --m) {
			for (int n = 0; n < m; ++n) {
				Object[] o1 = (Object[]) alDaten.get(n);
				Object[] o2 = (Object[]) alDaten.get(n + 1);

				String s1 = "";
				String s2 = "";

				if (bSortNachIdent.booleanValue() == true) {

					s1 = Helper.fitString2Length((String) o1[REPORT_SNRSTAMMBLATT_ARTIKELNUMMER],
							ArtikelFac.MAX_ARTIKEL_ARTIKELNUMMER, ' ')
							+ Helper.fitString2Length((String) o1[REPORT_SNRSTAMMBLATT_SERIENNUMER], 50, ' ');
					s2 = Helper.fitString2Length((String) o2[REPORT_SNRSTAMMBLATT_ARTIKELNUMMER],
							ArtikelFac.MAX_ARTIKEL_ARTIKELNUMMER, ' ')
							+ Helper.fitString2Length((String) o2[REPORT_SNRSTAMMBLATT_SERIENNUMER], 50, ' ');

				} else {
					s1 = Helper.fitString2Length((String) o1[REPORT_SNRSTAMMBLATT_SERIENNUMER], 50, ' ')
							+ Helper.fitString2Length((String) o1[REPORT_SNRSTAMMBLATT_ARTIKELNUMMER],
									ArtikelFac.MAX_ARTIKEL_ARTIKELNUMMER, ' ');
					s2 = Helper.fitString2Length((String) o2[REPORT_SNRSTAMMBLATT_SERIENNUMER], 50, ' ')
							+ Helper.fitString2Length((String) o2[REPORT_SNRSTAMMBLATT_ARTIKELNUMMER],
									ArtikelFac.MAX_ARTIKEL_ARTIKELNUMMER, ' ');
				}

				if (s1.compareTo(s2) > 0) {
					alDaten.set(n, o2);
					alDaten.set(n + 1, o1);
				} else if (s1.compareTo(s2) == 0) {

					if (((java.util.Date) o1[REPORT_SNRSTAMMBLATT_ZEITPUNKT])
							.before((java.util.Date) o2[REPORT_SNRSTAMMBLATT_ZEITPUNKT])) {
						alDaten.set(n, o2);
						alDaten.set(n + 1, o1);
					}

				}

			}
		}

		data = new Object[alDaten.size()][8];
		for (int i = 0; i < alDaten.size(); i++) {
			data[i] = (Object[]) alDaten.get(i);
		}

		HashMap<String, Object> parameter = new HashMap<String, Object>();
		if (lagerIId != null) {
			try {
				parameter.put("P_LAGER", getLagerFac().lagerFindByPrimaryKey(lagerIId).getCNr());
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
		} else {
			parameter.put("P_LAGER", "ALLE");

		}

		if (snrWildcard != null) {
			parameter.put("P_SERIENNUMMER", snrWildcard);
		} else {
			parameter.put("P_SERIENNUMMER", Helper.erzeugeStringAusStringArray(snrs));
		}

		parameter.put("P_ZENTRALER_ARTIKELSTAMM", bZentralerArtikelstamm);

		parameter.put("P_VERSION", versionWildcard);

		parameter.put("P_NUR_SERIENNUMMERN", new Boolean(bNurSeriennummern));
		parameter.put("P_NUR_OBERSTE_EBENE", new Boolean(nurObersteEbene));

		initJRDS(parameter, LagerFac.REPORT_MODUL, ArtikelReportFac.REPORT_SNRSTAMMBLATT, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);

		return getReportPrint();

	}

	private LPDatenSubreport getSubreportGesamtkalkulationFuerSnrStammblatt(Integer losIId, Integer losablieferungIId,
			TheClientDto theClientDto) {
		String[] fieldnames = new String[] { "Artikelnummer", "Bezeichnung", "Zusatzbezeichnung", "BelegartZugang",
				"BelegnummerZugang", "Einheit", "Sollmenge", "Istmenge", "VerbrauchteMenge", "Losgroesse",
				"Arbeitszeit", "Ebene", "Sollpreis", "Istpreis", "Mengenfaktor", "Einstandspreis", "Maschine",
				"SnrChnr", "ChnrBehaftet", "SnrBehaftet", "PosWert" };

		GesamtkalkulationDto gkDto = getFertigungReportFac().getDatenGesamtkalkulation(losIId, losablieferungIId, 99,
				theClientDto);

		ArrayList alDatenSubreport = gkDto.getAlDaten();

		Object[][] dataSubKD = new Object[alDatenSubreport.size()][fieldnames.length];

		for (int i = 0; i < alDatenSubreport.size(); i++) {

			Object[] zeileGK = (Object[]) alDatenSubreport.get(i);

			Object[] zeile = new Object[fieldnames.length];

			zeile[0] = zeileGK[FertigungReportFac.GESAMTKALKULATION_ARTIKELNUMMER];
			zeile[1] = zeileGK[FertigungReportFac.GESAMTKALKULATION_ARTIKELBEZEICHNUNG];
			zeile[2] = zeileGK[FertigungReportFac.GESAMTKALKULATION_ZUSATZBEZEICHNUNG];
			zeile[3] = zeileGK[FertigungReportFac.GESAMTKALKULATION_BELEGART_ZUGANG];
			zeile[4] = zeileGK[FertigungReportFac.GESAMTKALKULATION_BELEGNUMMER_ZUGANG];
			zeile[5] = zeileGK[FertigungReportFac.GESAMTKALKULATION_EINHEIT];
			zeile[6] = zeileGK[FertigungReportFac.GESAMTKALKULATION_SOLLMENGE];
			zeile[7] = zeileGK[FertigungReportFac.GESAMTKALKULATION_ISTMENGE];
			zeile[8] = zeileGK[FertigungReportFac.GESAMTKALKULATION_VERBRAUCHTE_MENGE];
			zeile[9] = zeileGK[FertigungReportFac.GESAMTKALKULATION_LOSGROESSE];
			zeile[10] = zeileGK[FertigungReportFac.GESAMTKALKULATION_ARBEITSZEIT];
			zeile[11] = zeileGK[FertigungReportFac.GESAMTKALKULATION_EBENE];
			zeile[12] = zeileGK[FertigungReportFac.GESAMTKALKULATION_SOLLPREIS];
			zeile[13] = zeileGK[FertigungReportFac.GESAMTKALKULATION_ISTPREIS];
			zeile[14] = zeileGK[FertigungReportFac.GESAMTKALKULATION_MENGENFAKTOR];
			zeile[15] = zeileGK[FertigungReportFac.GESAMTKALKULATION_EINSTANDSPREIS];
			zeile[16] = zeileGK[FertigungReportFac.GESAMTKALKULATION_MASCHINE];
			zeile[17] = zeileGK[FertigungReportFac.GESAMTKALKULATION_SNR_CHNR];
			zeile[18] = zeileGK[FertigungReportFac.GESAMTKALKULATION_CHNRBEHAFTET];
			zeile[19] = zeileGK[FertigungReportFac.GESAMTKALKULATION_SNRBEHAFTET];

			dataSubKD[i] = zeile;

		}

		return new LPDatenSubreport(dataSubKD, fieldnames);
	}

	private void reklamationHinzufuegen(TheClientDto theClientDto, ArrayList<Object[]> alDaten, Integer artikelIId,
			Boolean bSortNachIdent, String[] snrs, String snrWildcard) {

		// PJ 16364 Reklamationen hinzufuegen

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT r FROM FLRReklamation as r " + " WHERE r.mandant_c_nr='"
				+ theClientDto.getMandant() + "' AND r.c_seriennrchargennr IS NOT NULL ";

		if (artikelIId != null) {
			queryString += " AND r.flrartikel.i_id=" + artikelIId;

		}

		if (snrs != null) {
			String snrsIn = "";

			for (int i = 0; i < snrs.length; i++) {
				snrsIn += "'" + snrs[i] + "'";
				if (i != snrs.length - 1) {
					snrsIn += ",";
				}
			}
			queryString += " AND r.c_seriennrchargennr IN(" + snrsIn + ")";

		} else if (snrWildcard != null) {

			queryString += " AND r.c_seriennrchargennr LIKE '%" + snrWildcard + "%'";

		}

		if (bSortNachIdent.booleanValue() == true) {

			queryString += " ORDER BY r.flrartikel.c_nr, r.c_seriennrchargennr";

		} else {

			queryString += " ORDER BY r.c_seriennrchargennr ASC, r.flrartikel.c_nr ASC";

		}

		org.hibernate.Query query = session.createQuery(queryString);

		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {
			FLRReklamation r = (FLRReklamation) resultListIterator.next();

			if (r.getFlrartikel() != null) {
				Object[] dataHelp = new Object[REPORT_SNRSTAMMBLATT_ANZAHL_SPALTEN];
				dataHelp[REPORT_SNRSTAMMBLATT_ZEITPUNKT] = r.getT_belegdatum();
				dataHelp[REPORT_SNRSTAMMBLATT_MENGE] = r.getN_menge();

				ArtikelDto artikelDto;
				try {

					String kurzzeichenReklamation = getLocaleFac().belegartFindByCNr(LocaleFac.BELEGART_REKLAMATION)
							.getCKurzbezeichnung();
					artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(r.getFlrartikel().getI_id(),
							theClientDto);
					dataHelp[REPORT_SNRSTAMMBLATT_ARTIKELNUMMER] = artikelDto.getCNr();
					dataHelp[REPORT_SNRSTAMMBLATT_ARTIKELBEZEICHNUNG] = artikelDto.formatBezeichnung();
					dataHelp[REPORT_SNRSTAMMBLATT_EINHEIT] = artikelDto.getEinheitCNr();

					dataHelp[REPORT_SNRSTAMMBLATT_SERIENNUMER] = r.getC_seriennrchargennr();

					dataHelp[REPORT_SNRSTAMMBLATT_ZUGANG] = "";
					dataHelp[REPORT_SNRSTAMMBLATT_ABGANG] = "";

					if (r.getReklamationart_c_nr().equals(ReklamationFac.REKLAMATIONART_LIEFERANT)) {
						dataHelp[REPORT_SNRSTAMMBLATT_ZUGANG] = kurzzeichenReklamation + r.getC_nr();

						if (r.getFlrlieferant() != null) {

							LieferantDto lfDto = getLieferantFac()
									.lieferantFindByPrimaryKey(r.getFlrlieferant().getI_id(), theClientDto);

							dataHelp[REPORT_SNRSTAMMBLATT_ZUGANG] = dataHelp[REPORT_SNRSTAMMBLATT_ZUGANG]
									+ lfDto.getPartnerDto().formatFixTitelName1Name2();
						}

					} else {
						dataHelp[REPORT_SNRSTAMMBLATT_ABGANG] = kurzzeichenReklamation + r.getC_nr();
						if (r.getFlrkunde() != null) {

							KundeDto lfDto = getKundeFac().kundeFindByPrimaryKey(r.getFlrkunde().getI_id(),
									theClientDto);

							dataHelp[REPORT_SNRSTAMMBLATT_ZUGANG] = dataHelp[REPORT_SNRSTAMMBLATT_ZUGANG] + " "
									+ lfDto.getPartnerDto().formatFixTitelName1Name2();
						}

					}
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
				alDaten.add(dataHelp);
			}
		}
	}

	private void auftragsseriennummernHinzufuegen(TheClientDto theClientDto, ArrayList<Object[]> alDaten,
			Integer artikelIId, Boolean bSortNachIdent, String[] snrs, String snrWildcard) {

		// PJ 16364 Reklamationen hinzufuegen

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT r FROM FLRAuftragseriennrn as r "
				+ " WHERE r.flrauftragposition.flrauftrag.mandant_c_nr='" + theClientDto.getMandant()
				+ "' AND r.c_seriennr IS NOT NULL ";

		if (artikelIId != null) {
			queryString += " AND r.artikel_i_id=" + artikelIId;

		}

		if (snrs != null) {
			String snrsIn = "";

			for (int i = 0; i < snrs.length; i++) {
				snrsIn += "'" + snrs[i] + "'";
				if (i != snrs.length - 1) {
					snrsIn += ",";
				}
			}
			queryString += " AND r.c_seriennr IN(" + snrsIn + ")";

		} else if (snrWildcard != null) {

			queryString += " AND r.c_seriennr LIKE '%" + snrWildcard + "%'";

		}

		if (bSortNachIdent.booleanValue() == true) {

			queryString += " ORDER BY r.flrauftragposition.flrartikel.c_nr, r.c_seriennr";

		} else {

			queryString += " ORDER BY r.c_seriennr ASC, r.flrauftragposition.flrartikel.c_nr ASC";

		}

		org.hibernate.Query query = session.createQuery(queryString);

		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {
			FLRAuftragseriennrn r = (FLRAuftragseriennrn) resultListIterator.next();

			if (r.getArtikel_i_id() != null) {
				Object[] dataHelp = new Object[REPORT_SNRSTAMMBLATT_ANZAHL_SPALTEN];
				dataHelp[REPORT_SNRSTAMMBLATT_ZEITPUNKT] = r.getFlrauftragposition().getFlrauftrag().getT_belegdatum();
				dataHelp[REPORT_SNRSTAMMBLATT_MENGE] = BigDecimal.ONE;

				ArtikelDto artikelDto;
				try {

					String kurzzeichenReklamation = getLocaleFac().belegartFindByCNr(LocaleFac.BELEGART_REKLAMATION)
							.getCKurzbezeichnung();
					artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(r.getArtikel_i_id(), theClientDto);
					dataHelp[REPORT_SNRSTAMMBLATT_ARTIKELNUMMER] = artikelDto.getCNr();
					dataHelp[REPORT_SNRSTAMMBLATT_ARTIKELBEZEICHNUNG] = artikelDto.formatBezeichnung();
					dataHelp[REPORT_SNRSTAMMBLATT_EINHEIT] = artikelDto.getEinheitCNr();

					dataHelp[REPORT_SNRSTAMMBLATT_SERIENNUMER] = r.getC_seriennr();

					dataHelp[REPORT_SNRSTAMMBLATT_ABGANG] = "";

					dataHelp[REPORT_SNRSTAMMBLATT_ZUGANG] = "AB" + r.getFlrauftragposition().getFlrauftrag().getC_nr();
					dataHelp[REPORT_SNRSTAMMBLATT_ZUGANG] = dataHelp[REPORT_SNRSTAMMBLATT_ZUGANG] + " "
							+ HelperServer.formatNameAusFLRPartner(
									r.getFlrauftragposition().getFlrauftrag().getFlrkunde().getFlrpartner());

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
				alDaten.add(dataHelp);
			}
		}
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printArtikelbestellt(Integer artikelIId, java.sql.Date dVon, java.sql.Date dBis,
			TheClientDto theClientDto) {
		if (artikelIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelIId == null"));
		}
		// Erstellung des Reports
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		index = -1;
		sAktuellerReport = ArtikelReportFac.REPORT_ARTIKELBESTELLT;
		ArtikelDto dto = getArtikelFac().artikelFindByPrimaryKeySmall(artikelIId, theClientDto);
		parameter.put("P_ARTIKEL", dto.formatArtikelbezeichnung());
		parameter.put("P_ARTIKELREFERENZNUMMER", dto.getCReferenznr());
		parameter.put("P_EINHEIT", dto.getEinheitCNr().trim());
		parameter.put("P_BESTELLEINHEIT_INVERS", Helper.short2Boolean(dto.getbBestellmengeneinheitInvers()));
		if (dto.getEinheitCNrBestellung() != null) {
			parameter.put("P_BESTELLEINHEIT", dto.getEinheitCNrBestellung().trim());
		} else {
			parameter.put("P_BESTELLEINHEIT", "");
		}

		String eingeloggterMandant = theClientDto.getMandant();

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria bestelltliste = session.createCriteria(FLRArtikelbestellt.class);
		bestelltliste.createAlias(ArtikelbestelltFac.FLR_ARTIKELBESTELLT_FLRARTIKEL, "a")
				.add(Restrictions.eq("a.i_id", artikelIId));
		if (dVon != null) {
			bestelltliste.add(Restrictions.ge(ArtikelbestelltFac.FLR_ARTIKELBESTELLT_D_LIEFERTERMIN, dVon));
		}
		if (dBis != null) {
			bestelltliste.add(Restrictions.lt(ArtikelbestelltFac.FLR_ARTIKELBESTELLT_D_LIEFERTERMIN, dBis));
		}

		bestelltliste.addOrder(Order.asc(ArtikelbestelltFac.FLR_ARTIKELBESTELLT_D_LIEFERTERMIN));

		List<?> resultList = bestelltliste.list();
		Iterator<?> resultListIterator = resultList.iterator();
		int row = 0;

		Object[][] dataHelp = new Object[resultList.size()][REPORT_BESTELLTLISTE_ANZAHL_SPALTEN];

		while (resultListIterator.hasNext()) {
			FLRArtikelbestellt artikelbestellt = (FLRArtikelbestellt) resultListIterator.next();

			String sBelegnummer = null;
			String sPartner = null;
			String sProjektbezeichnung = null;
			String sMandant = null;
			Date dAuftragbestliefertermin = null;

			BestellpositionDto bestellpositionDto = null;
			BestellungDto bestellungDto = null;
			if (artikelbestellt.getC_belegartnr().equals(LocaleFac.BELEGART_BESTELLUNG)) {
				try {
					bestellpositionDto = getBestellpositionFac()
							.bestellpositionFindByPrimaryKey(artikelbestellt.getI_belegartpositionid());
					if (!bestellpositionDto.getBestellpositionstatusCNr()
							.equals(BestellpositionFac.BESTELLPOSITIONSTATUS_ERLEDIGT)) {
						bestellungDto = getBestellungFac()
								.bestellungFindByPrimaryKey(bestellpositionDto.getBestellungIId());
						sBelegnummer = bestellungDto.getCNr();
						sMandant = bestellungDto.getMandantCNr();
						sProjektbezeichnung = bestellungDto.getCBez();
						dAuftragbestliefertermin = bestellpositionDto.getTAuftragsbestaetigungstermin();

						sPartner = getLieferantFac()
								.lieferantFindByPrimaryKey(bestellungDto.getLieferantIIdBestelladresse(), theClientDto)
								.getPartnerDto().formatTitelAnrede();
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
					dataHelp[row][REPORT_BESTELLTLISTE_LIEFERTERMIN] = artikelbestellt.getT_liefertermin();

				}

				if (dto.getEinheitCNrBestellung() != null) {

					if (Helper.short2boolean(dto.getbBestellmengeneinheitInvers()) == true) {
						if (dto.getNUmrechnungsfaktor().doubleValue() != 0) {
							dataHelp[row][REPORT_BESTELLTLISTE_BESTELLMENGE] = artikelbestellt.getN_menge()
									.divide(dto.getNUmrechnungsfaktor(), 4, BigDecimal.ROUND_UP);
						}
					} else {
						dataHelp[row][REPORT_BESTELLTLISTE_BESTELLMENGE] = artikelbestellt.getN_menge()
								.multiply(dto.getNUmrechnungsfaktor());
					}

				}

				if (artikelbestellt.getC_belegartnr().equals(LocaleFac.BELEGART_BESTELLUNG)) {
					dataHelp[row][REPORT_BESTELLTLISTE_MENGE] = bestellpositionDto.getNMenge();
					dataHelp[row][REPORT_BESTELLTLISTE_AB_NUMMER] = bestellpositionDto.getCABNummer();
					dataHelp[row][REPORT_BESTELLTLISTE_LIEFERTERMIN] = bestellpositionDto
							.getTUebersteuerterLiefertermin();
					dataHelp[row][REPORT_BESTELLTLISTE_AB_TERMIN] = bestellpositionDto
							.getTAuftragsbestaetigungstermin();
					dataHelp[row][REPORT_BESTELLTLISTE_OFFENEMENGE] = artikelbestellt.getN_menge();
					dataHelp[row][REPORT_BESTELLTLISTE_AB_KOMMENTAR] = bestellpositionDto.getCABKommentar();
					if (bestellpositionDto.getGebindeIId() != null) {
						dataHelp[row][REPORT_BESTELLTLISTE_GEBINDENAME] = getArtikelFac()
								.gebindeFindByPrimaryKey(bestellpositionDto.getGebindeIId()).getCBez();
						dataHelp[row][REPORT_BESTELLTLISTE_ANZAHL_GEBINDE] = bestellpositionDto.getNAnzahlgebinde();
					}

				}

				row++;
			}

		}
		session.close();
		data = new Object[row][10];
		for (int i = 0; i < row; i++) {
			data[i] = dataHelp[i];
		}

		initJRDS(parameter, ArtikelbestelltFac.REPORT_MODUL, ArtikelReportFac.REPORT_ARTIKELBESTELLT,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printLagercockpitWELagerVerteilungsvorschlag(TheClientDto theClientDto) {
		HashMap<String, Object> parameter = new HashMap<String, Object>();

		index = -1;
		sAktuellerReport = ArtikelReportFac.REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG;
		Session sessionArtikellager = FLRSessionFactory.getFactory().openSession();
		String sQueryArtikellager = "SELECT artikellager from FLRArtikellager artikellager LEFT OUTER JOIN artikellager.flrartikelliste.artikelsprset AS aspr  WHERE artikellager.flrartikelliste.mandant_c_nr = '"
				+ theClientDto.getMandant() + "' AND artikellager.flrlager.lagerart_c_nr = '"
				+ LagerFac.LAGERART_WARENEINGANG
				+ "' AND artikellager.n_lagerstand > 0 ORDER BY artikellager.flrartikel.c_nr ";

		Query queryArtikellager = sessionArtikellager.createQuery(sQueryArtikellager);

		List<?> resultListArtikelager = queryArtikellager.list();
		Iterator<?> resultListIteratorArtikelager = resultListArtikelager.iterator();

		ArrayList al = new ArrayList();

		while (resultListIteratorArtikelager.hasNext()) {
			FLRArtikellager artikellager = (FLRArtikellager) resultListIteratorArtikelager.next();

			BigDecimal lagerstandKumuliert = artikellager.getN_lagerstand();

			Session session = FLRSessionFactory.getFactory().openSession();
			String queryString = "SELECT flrfehlmenge from FLRFehlmenge flrfehlmenge LEFT OUTER JOIN flrfehlmenge.flrartikelliste.artikellagerplatzset as artikellagerplatzset WHERE (artikellagerplatzset.i_sort=1 OR artikellagerplatzset.i_sort is null)  AND flrfehlmenge.flrartikel.i_id="
					+ artikellager.getFlrartikel().getI_id()
					+ " ORDER BY flrfehlmenge.flrlossollmaterial.flrlos.t_produktionsbeginn, flrfehlmenge.flrlossollmaterial.flrlos.c_nr  ";

			Query query = session.createQuery(queryString);

			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();

			Object[] zeileVorlage = new Object[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_ANZAHL_SPALTEN];
			zeileVorlage[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_ARTIKELNUMMER] = artikellager.getFlrartikel()
					.getC_nr();
			zeileVorlage[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LAGERBEWIRTSCHAFTET] = Helper
					.short2Boolean(artikellager.getFlrartikel().getB_lagerbewirtschaftet());
			ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(artikellager.getFlrartikel().getI_id(),
					theClientDto);
			if (aDto.getArtikelsprDto() != null) {
				zeileVorlage[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_BEZEICHNUNG] = aDto.getArtikelsprDto()
						.getCBez();
				zeileVorlage[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_ZUSATZBEZEICHNUNG] = aDto
						.getArtikelsprDto().getCZbez();
			}

			zeileVorlage[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_EINHEIT] = artikellager.getFlrartikel()
					.getEinheit_c_nr();

			Integer lagerIId = null;
			if (artikellager.getFlrartikelliste().getArtikellagerplatzset().size() > 0) {
				Iterator it = artikellager.getFlrartikelliste().getArtikellagerplatzset().iterator();
				while (it.hasNext()) {
					FLRArtikellagerplaetze flrArtikellagerplaetze = (FLRArtikellagerplaetze) it.next();
					if (flrArtikellagerplaetze.getI_sort() == 1) {
						zeileVorlage[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LAGER] = flrArtikellagerplaetze
								.getFlrlagerplatz().getFlrlager().getC_nr();
						lagerIId = flrArtikellagerplaetze.getFlrlagerplatz().getFlrlager().getI_id();
						break;
					}
				}

			}

			try {

				String sLagerplaetze = getLagerFac().getLagerplaezteEinesArtikels(aDto.getIId(), null);

				boolean bMehrerLagerplaetze = false;

				if (sLagerplaetze != null && sLagerplaetze.split(",").length > 2) {
					bMehrerLagerplaetze = true;
				}
				if (bMehrerLagerplaetze == true && lagerIId != null) {
					zeileVorlage[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LAGERPLAETZE] = getLagerFac()
							.getLagerplaezteEinesArtikels(artikellager.getFlrartikel().getI_id(), lagerIId) + " ++";
				} else {
					zeileVorlage[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LAGERPLAETZE] = getLagerFac()
							.getLagerplaezteEinesArtikels(artikellager.getFlrartikel().getI_id(), lagerIId);
				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			if (resultList.size() > 0) {

				while (resultListIterator.hasNext()) {
					FLRFehlmenge flrFehlmenge = (FLRFehlmenge) resultListIterator.next();

					Object[] zeile = zeileVorlage.clone();

					LosDto losDto;
					try {
						losDto = getFertigungFac()
								.losFindByPrimaryKey(flrFehlmenge.getFlrlossollmaterial().getFlrlos().getI_id());
						zeile[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LOS_KOMMENTAR] = losDto.getCKommentar();
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}

					zeile[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_UMZUBUCHENDE_MENGE] = flrFehlmenge
							.getN_menge();

					lagerstandKumuliert = lagerstandKumuliert.subtract(flrFehlmenge.getN_menge());
					zeile[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LAGERSTAND_KUMULIERT] = lagerstandKumuliert;

					zeile[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LOS_BEGINN] = flrFehlmenge
							.getFlrlossollmaterial().getFlrlos().getT_produktionsbeginn();
					zeile[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LOS_ENDE] = flrFehlmenge
							.getFlrlossollmaterial().getFlrlos().getT_produktionsende();

					zeile[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LOS_NUMMER] = flrFehlmenge
							.getFlrlossollmaterial().getFlrlos().getC_nr();

					zeile[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LOS_PROJEKT] = flrFehlmenge
							.getFlrlossollmaterial().getFlrlos().getC_projekt();

					if (flrFehlmenge.getFlrlossollmaterial().getFlrlos().getFlrstueckliste() != null) {
						zeile[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LOS_STKLNUMMER] = flrFehlmenge
								.getFlrlossollmaterial().getFlrlos().getFlrstueckliste().getFlrartikel().getC_nr();

						ArtikelDto aDtoStkl = getArtikelFac().artikelFindByPrimaryKeySmall(flrFehlmenge
								.getFlrlossollmaterial().getFlrlos().getFlrstueckliste().getFlrartikel().getI_id(),
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
			FLRWareneingangspositionen wep = (FLRWareneingangspositionen) resultListIteratorArtikelager.next();

			Object[] zeile = new Object[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_ANZAHL_SPALTEN];

			zeile[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_ARTIKELNUMMER] = wep.getFlrbestellposition()
					.getFlrartikel().getC_nr();
			zeile[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_UMZUBUCHENDE_MENGE] = wep.getN_geliefertemenge();

			ArtikelDto aDto = getArtikelFac()
					.artikelFindByPrimaryKeySmall(wep.getFlrbestellposition().getFlrartikel().getI_id(), theClientDto);
			if (aDto.getArtikelsprDto() != null) {
				zeile[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_BEZEICHNUNG] = aDto.getArtikelsprDto().getCBez();
				zeile[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_ZUSATZBEZEICHNUNG] = aDto.getArtikelsprDto()
						.getCZbez();
			}

			zeile[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LAGERBEWIRTSCHAFTET] = Boolean.FALSE;

			zeile[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_BESTELLNUMMER] = wep.getFlrbestellposition()
					.getFlrbestellung().getC_nr();
			zeile[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_WARENEINGANG] = wep.getFlrwareneingang()
					.getC_lieferscheinnr();

			Integer lagerIId = null;
			if (wep.getFlrbestellposition().getFlrartikel().getArtikellagerplatzset().size() > 0) {
				Iterator it = wep.getFlrbestellposition().getFlrartikel().getArtikellagerplatzset().iterator();
				while (it.hasNext()) {
					FLRArtikellagerplaetze flrArtikellagerplaetze = (FLRArtikellagerplaetze) it.next();
					if (flrArtikellagerplaetze.getI_sort() == 1) {
						zeile[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LAGER] = flrArtikellagerplaetze
								.getFlrlagerplatz().getFlrlager().getC_nr();
						lagerIId = flrArtikellagerplaetze.getFlrlagerplatz().getFlrlager().getI_id();
						break;
					}
				}

			}

			try {

				String sLagerplaetze = getLagerFac().getLagerplaezteEinesArtikels(aDto.getIId(), null);

				boolean bMehrerLagerplaetze = false;

				if (sLagerplaetze != null && sLagerplaetze.split(",").length > 2) {
					bMehrerLagerplaetze = true;
				}
				if (bMehrerLagerplaetze == true && lagerIId != null) {
					zeile[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LAGERPLAETZE] = getLagerFac()
							.getLagerplaezteEinesArtikels(wep.getFlrbestellposition().getFlrartikel().getI_id(),
									lagerIId)
							+ " ++";
				} else {
					zeile[REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG_LAGERPLAETZE] = getLagerFac()
							.getLagerplaezteEinesArtikels(wep.getFlrbestellposition().getFlrartikel().getI_id(),
									lagerIId);
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

		initJRDS(parameter, ArtikelReportFac.REPORT_MODUL,
				ArtikelReportFac.REPORT_LAGERCOCKPIT_WELAGER_VERTEILUNGSVORSCHLAG, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);

		return getReportPrint();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printLagercockpitMaterialVerteilungsvorschlag(Integer iOption, TheClientDto theClientDto)
			throws EJBExceptionLP {
		HashMap<String, Object> parameter = new HashMap<String, Object>();

		TreeMap tmDaten = new TreeMap();

		index = -1;
		sAktuellerReport = ArtikelReportFac.REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG;

		Session session = FLRSessionFactory.getFactory().openSession();
		String queryString = "SELECT umbuchung FROM FLRLagercockpitumbuchung umbuchung  LEFT OUTER JOIN umbuchung.flrlager_lagerplatz as flrlager_lagerplatz WHERE 1=1 ";
		String sOption = getTextRespectUISpr("ww.lagercockpit.alleartikel", theClientDto.getMandant(),
				theClientDto.getLocUi());
		if (iOption != null && iOption.equals(
				ArtikelReportFac.OPTION_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_NUR_ARTIKEL_MIT_LAGERSTAND)) {
			queryString += " AND umbuchung.lagerstand > 0 ";
			sOption = getTextRespectUISpr("ww.lagercockpit.nurartikelmitlagerstand", theClientDto.getMandant(),
					theClientDto.getLocUi());
		}

		if (iOption != null && iOption.equals(
				ArtikelReportFac.OPTION_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_NUR_RUECKNAHMEN_AUS_FERTIGUNG)) {
			queryString += " AND umbuchung.diff < 0 ";
			sOption = getTextRespectUISpr("ww.lagercockpit.nurruecknahmenausfertigung", theClientDto.getMandant(),
					theClientDto.getLocUi());
		}

		parameter.put("P_OPTION", sOption);

		// PJ18216
		boolean bNichtLagerbewSofortAusgeben = false;
		try {
			ParametermandantDto parameterM = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_NICHT_LAGERBEWIRTSCHAFTETE_SOFORT_AUSGEBEN);
			bNichtLagerbewSofortAusgeben = ((Boolean) parameterM.getCWertAsObject()).booleanValue();

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
			FLRLagercockpitumbuchung losmat = (FLRLagercockpitumbuchung) resultListIterator.next();
			Object[] zeile = new Object[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_ANZAHL_SPALTEN];
			zeile[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_ARTIKELNUMMER] = losmat.getFlrartikel().getC_nr();
			ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(losmat.getFlrartikel().getI_id(),
					theClientDto);
			if (aDto.getArtikelsprDto() != null) {
				zeile[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_BEZEICHNUNG] = aDto.getArtikelsprDto()
						.getCBez();
				zeile[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_ZUSATZBEZEICHNUNG] = aDto.getArtikelsprDto()
						.getCZbez();
			}

			zeile[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_EINHEIT] = losmat.getFlrartikel().getEinheit_c_nr();
			Integer lagerIId = null;
			if (losmat.getFlrlager_lagerplatz() != null) {
				zeile[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LAGER] = losmat.getFlrlager_lagerplatz()
						.getC_nr();

				lagerIId = losmat.getFlrlager_lagerplatz().getI_id();

			}
			try {

				String sLagerplaetze = getLagerFac().getLagerplaezteEinesArtikels(aDto.getIId(), null);

				boolean bMehrerLagerplaetze = false;

				if (sLagerplaetze != null && sLagerplaetze.split(",").length > 2) {
					bMehrerLagerplaetze = true;
				}
				if (bMehrerLagerplaetze == true && lagerIId != null) {
					zeile[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LAGERPLAETZE] = getLagerFac()
							.getLagerplaezteEinesArtikels(losmat.getFlrartikel().getI_id(), lagerIId) + " ++";
				} else {
					zeile[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LAGERPLAETZE] = getLagerFac()
							.getLagerplaezteEinesArtikels(losmat.getFlrartikel().getI_id(), lagerIId);
				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			LosDto losDto;
			try {
				losDto = getFertigungFac().losFindByPrimaryKey(losmat.getFlrlos().getI_id());
				zeile[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LOS_KOMMENTAR] = losDto.getCKommentar();
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			zeile[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_MENGE] = losmat.getDiff();
			zeile[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LAGERSTAND] = losmat.getLagerstand();
			zeile[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LOS_BEGINN] = losmat.getFlrlos()
					.getT_produktionsbeginn();
			zeile[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LOS_ENDE] = losmat.getFlrlos()
					.getT_produktionsende();

			zeile[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LOS_NUMMER] = losmat.getFlrlos().getC_nr();

			zeile[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LOS_PROJEKT] = losmat.getFlrlos().getC_projekt();

			if (losmat.getFlrlos().getFlrstueckliste() != null) {
				zeile[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_LOS_STKLNUMMER] = losmat.getFlrlos()
						.getFlrstueckliste().getFlrartikel().getC_nr();

				ArtikelDto aDtoStkl = getArtikelFac().artikelFindByPrimaryKeySmall(
						losmat.getFlrlos().getFlrstueckliste().getFlrartikel().getI_id(), theClientDto);

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
						.getLagerDtoWennNurAufEinemTypLagerstandVorhandenIst(losmat.getFlrartikel().getI_id());

				String lager = "";

				if (lDto != null) {
					zeile[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_QUELLE] = "Lager: " + lDto.getCNr();
					lager = lDto.getCNr();
				}

				key = "3" + Helper.fitString2Length(lager, 30, ' ')
						+ Helper.fitString2Length(losmat.getFlrlos().getC_nr(), 30, ' ');

			} else {
				// Zuviel

				try {
					// Los an Los
					ArrayList al = getFehlmengeFac().getFehlmengen(losmat.getFlrartikel().getI_id(),
							theClientDto.getMandant(), theClientDto);
					FLRLos losZu = null;
					for (int i = 0; i < al.size(); i++) {
						FLRFehlmenge flr = (FLRFehlmenge) al.get(i);
						if (flr.getN_menge().doubleValue() > 0) {

							if (!flr.getFlrlossollmaterial().getI_id().equals(losmat.getI_id())) {
								losZu = flr.getFlrlossollmaterial().getFlrlos();
								break;
							}

						}
					}

					if (losZu != null) {
						// 1.)LOS-ZU:LOS-AB
						key = "1" + Helper.fitString2Length(losmat.getFlrlos().getC_nr(), 30, ' ')
								+ Helper.fitString2Length(losZu.getC_nr(), 30, ' ');
						zeile[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_TYP] = "LOS_ZU_LOS";
						zeile[REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG_ZIEL] = losZu.getC_nr();
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

						key = "2" + Helper.fitString2Length(lager, 30, ' ')
								+ Helper.fitString2Length(losmat.getFlrlos().getC_nr(), 30, ' ');
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

		initJRDS(parameter, ArtikelReportFac.REPORT_MODUL,
				ArtikelReportFac.REPORT_LAGERCOCKPIT_MATERIAL_VERTEILUNGSVORSCHLAG, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);

		return getReportPrint();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printAuftragswerte(String artikelNrVon, String artikelNrBis, boolean bMitKonsignationslager,
			TheClientDto theClientDto) {

		Session session = FLRSessionFactory.getFactory().openSession();

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		parameter.put("P_ARTIKELNRVON", artikelNrVon);
		parameter.put("P_ARTIKELNRBIS", artikelNrBis);
		parameter.put("P_MITKONSIGNATIONSLAGER", new Boolean(bMitKonsignationslager));

		String filterKonsiLager = " AND artikellager.flrlager.b_konsignationslager=0 ";
		if (bMitKonsignationslager == true) {
			filterKonsiLager = " ";
		}

		String queryString = "SELECT artikelliste.i_id, artikelliste.c_nr, aspr.c_bez,(SELECT sum(artikellager.n_lagerstand) FROM FLRArtikellager AS artikellager WHERE artikellager.compId.artikel_i_id=artikelliste.i_id "
				+ filterKonsiLager + "  AND artikellager.flrlager.lagerart_c_nr NOT IN('"
				+ LagerFac.LAGERART_WERTGUTSCHRIFT
				+ "')) ,(SELECT sum(artikellager.n_gestehungspreis*artikellager.n_lagerstand) FROM FLRArtikellager AS artikellager WHERE artikellager.compId.artikel_i_id=artikelliste.i_id  AND artikellager.flrlager.lagerart_c_nr NOT IN('"
				+ LagerFac.LAGERART_WERTGUTSCHRIFT + "') " + filterKonsiLager
				+ "), artikelliste.b_lagerbewirtschaftet, artikelliste.einheit_c_nr,  artikelliste.artikelart_c_nr, "
				+ " aspr.c_kbez,aspr.c_zbez,aspr.c_zbez2, artikelliste.c_referenznr FROM FLRArtikelliste AS artikelliste"
				+ " LEFT OUTER JOIN artikelliste.artikelsprset AS aspr ";

		queryString = queryString + " WHERE artikelliste.mandant_c_nr='" + theClientDto.getMandant()
				+ "' AND artikelliste.artikelart_c_nr not in('" + ArtikelFac.ARTIKELART_HANDARTIKEL + "','"
				+ ArtikelFac.ARTIKELART_ARBEITSZEIT + "') ";

		if (artikelNrVon != null) {
			queryString += " AND artikelliste.c_nr >='" + artikelNrVon + "'";
		}
		if (artikelNrBis != null) {

			String artikelNrBis_Gefuellt = Helper.fitString2Length(artikelNrBis, 25, '_');
			queryString += " AND artikelliste.c_nr <='" + artikelNrBis_Gefuellt + "'";
		}

		queryString += " AND (SELECT sum(artikellager.n_lagerstand) FROM FLRArtikellager AS artikellager WHERE artikellager.compId.artikel_i_id=artikelliste.i_id "
				+ filterKonsiLager + "  AND artikellager.flrlager.lagerart_c_nr NOT IN('"
				+ LagerFac.LAGERART_WERTGUTSCHRIFT + "')) >0   ORDER BY artikelliste.c_nr ASC";

		Query inventurliste = session.createQuery(queryString);

		List<?> resultList = inventurliste.list();

		Iterator<?> resultListIterator = resultList.iterator();

		ArrayList alDaten = new ArrayList();

		HashMap<String, BigDecimal> hmLagerstaende = new HashMap<String, BigDecimal>();

		while (resultListIterator.hasNext()) {
			Object o[] = (Object[]) resultListIterator.next();

			Object[] oZeileVorlage = new Object[REPORT_AUFTRAGSWERTE_ANZAHL_SPALTEN];

			Integer artikelIId = (Integer) o[0];

			oZeileVorlage[REPORT_AUFTRAGSWERTE_ARTIKELNUMMER] = o[1];
			oZeileVorlage[REPORT_AUFTRAGSWERTE_ARTIKELREFERENZNUMMER] = o[11];
			oZeileVorlage[REPORT_AUFTRAGSWERTE_BEZEICHNUNG] = o[2];
			oZeileVorlage[REPORT_AUFTRAGSWERTE_LAGERSTAND] = o[3];

			hmLagerstaende.put((String) o[1], (BigDecimal) o[3]);

			// Gestpreis
			BigDecimal gestwert = (BigDecimal) o[4];
			if (gestwert != null) {
				oZeileVorlage[REPORT_AUFTRAGSWERTE_GESTEHUNGSPREIS] = gestwert.divide((BigDecimal) o[3], 4,
						BigDecimal.ROUND_HALF_EVEN);
			}

			oZeileVorlage[REPORT_AUFTRAGSWERTE_EINHEIT] = o[6];
			try {

				ArtikellieferantDto alDto = getArtikelFac().getArtikelEinkaufspreisDesBevorzugtenLieferanten(artikelIId,
						BigDecimal.ONE, theClientDto.getSMandantenwaehrung(), theClientDto);
				if (alDto != null) {
					oZeileVorlage[REPORT_AUFTRAGSWERTE_LIEF1PREIS] = alDto.getNNettopreis();
				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			Session sessionRes = FLRSessionFactory.getFactory().openSession();

			org.hibernate.Criteria reservierungen = sessionRes.createCriteria(FLRArtikelreservierung.class);
			reservierungen.createAlias(ReservierungFac.FLR_ARTIKELRESERVIERUNG_FLRARTIKEL, "a")
					.add(Restrictions.eq("a.i_id", artikelIId));
			reservierungen.addOrder(Order.asc(ReservierungFac.FLR_ARTIKELRESERVIERUNG_D_LIEFERTERMIN));

			List<?> resultListRes = reservierungen.list();
			Iterator<?> resultListIteratorRes = resultListRes.iterator();
			int row = 0;

			while (resultListIteratorRes.hasNext()) {
				FLRArtikelreservierung artikelreservierung = (FLRArtikelreservierung) resultListIteratorRes.next();
				Object[] oZeile = oZeileVorlage.clone();

				oZeile[REPORT_AUFTRAGSWERTE_RESERVIERT] = artikelreservierung.getN_menge();

				if (artikelreservierung.getC_belegartnr().equals(LocaleFac.BELEGART_AUFTRAG)) {
					AuftragpositionDto auftragpositionDto = null;
					try {
						auftragpositionDto = getAuftragpositionFac()
								.auftragpositionFindByPrimaryKey(artikelreservierung.getI_belegartpositionid());

					} catch (RemoteException ex1) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex1);
					}
					AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(auftragpositionDto.getBelegIId());
					oZeile[REPORT_AUFTRAGSWERTE_AUFTRAG] = auftragDto.getCNr();

					oZeile[REPORT_AUFTRAGSWERTE_KUNDE] = getKundeFac()
							.kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(), theClientDto)
							.getPartnerDto().formatTitelAnrede();

				} else if (artikelreservierung.getC_belegartnr().equals(LocaleFac.BELEGART_LOS)) {
					com.lp.server.fertigung.service.LossollmaterialDto auftragpositionDto = null;
					try {
						auftragpositionDto = getFertigungFac()
								.lossollmaterialFindByPrimaryKey(artikelreservierung.getI_belegartpositionid());
					} catch (RemoteException ex1) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex1);
					}
					LosDto losDto = null;
					try {
						losDto = getFertigungFac().losFindByPrimaryKey(auftragpositionDto.getLosIId());
						oZeile[REPORT_AUFTRAGSWERTE_LOS] = losDto.getCNr();
						oZeile[REPORT_AUFTRAGSWERTE_LOS_BEGINNTERMIN] = losDto.getTProduktionsbeginn();

					} catch (RemoteException ex3) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex3);
					}

					Integer kundeIId = getFertigungFac().getZugehoerigenKunden(losDto.getIId(), theClientDto);
					if (kundeIId != null) {
						oZeile[REPORT_AUFTRAGSWERTE_KUNDE] = getKundeFac().kundeFindByPrimaryKey(kundeIId, theClientDto)
								.getPartnerDto().formatFixTitelName1Name2();
					}

				}

				alDaten.add(oZeile);

			}

			sessionRes.close();

			// Fehlmengen

			Session sessionFM = FLRSessionFactory.getFactory().openSession();

			org.hibernate.Criteria fehlmengen = sessionFM.createCriteria(FLRFehlmenge.class);
			fehlmengen.add(Restrictions.eq("artikel_i_id", artikelIId));

			List<?> resultListFM = fehlmengen.list();
			Iterator<?> resultListIteratorFM = resultListFM.iterator();

			while (resultListIteratorFM.hasNext()) {
				FLRFehlmenge fehlmenge = (FLRFehlmenge) resultListIteratorFM.next();
				Object[] oZeile = oZeileVorlage.clone();

				oZeile[REPORT_AUFTRAGSWERTE_FEHLMENGE] = fehlmenge.getN_menge();

				if (fehlmenge.getC_belegartnr().equals(LocaleFac.BELEGART_LOS)) {

					com.lp.server.fertigung.service.LosDto losDto = null;
					try {
						if (fehlmenge.getFlrlossollmaterial() != null) {
							losDto = getFertigungFac()
									.losFindByPrimaryKey(fehlmenge.getFlrlossollmaterial().getLos_i_id());
							oZeile[REPORT_AUFTRAGSWERTE_LOS] = losDto.getCNr();
							oZeile[REPORT_AUFTRAGSWERTE_LOS_BEGINNTERMIN] = losDto.getTProduktionsbeginn();

							if (losDto.getAuftragIId() != null) {
								AuftragDto auftragDto = null;
								auftragDto = getAuftragFac().auftragFindByPrimaryKey(losDto.getAuftragIId());

								oZeile[REPORT_AUFTRAGSWERTE_AUFTRAG] = auftragDto.getCNr();

								KundeDto kundeDto = getKundeFac()
										.kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(), theClientDto);

								oZeile[REPORT_AUFTRAGSWERTE_KUNDE] = kundeDto.getPartnerDto().formatTitelAnrede();

							}

						}

					} catch (RemoteException ex3) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex3);
					}
				}

				alDaten.add(oZeile);

			}

			sessionFM.close();

			if (resultListRes.size() == 0 && resultListFM.size() == 0) {
				alDaten.add(oZeileVorlage);
			}

		}

		// Nach Auftrag/Los/Ident sortieren
		for (int i = alDaten.size() - 1; i > 0; --i) {
			for (int j = 0; j < i; ++j) {
				Object[] o = (Object[]) alDaten.get(j);
				Object[] o1 = (Object[]) alDaten.get(j + 1);

				// Lieferant aufsteigend

				String auftrag = "__";
				String losBeginn = "";
				if (o[REPORT_AUFTRAGSWERTE_AUFTRAG] != null) {
					auftrag = (String) o[REPORT_AUFTRAGSWERTE_AUFTRAG];
				}
				if (o[REPORT_AUFTRAGSWERTE_LOS_BEGINNTERMIN] != null) {
					Date dBeginn = (java.util.Date) o[REPORT_AUFTRAGSWERTE_LOS_BEGINNTERMIN];
					losBeginn = dBeginn.getTime() + "";
				}

				String auftrag1 = "__";
				String losBeginn1 = "";
				if (o1[REPORT_AUFTRAGSWERTE_AUFTRAG] != null) {
					auftrag1 = (String) o1[REPORT_AUFTRAGSWERTE_AUFTRAG];
				}
				if (o1[REPORT_AUFTRAGSWERTE_LOS_BEGINNTERMIN] != null) {

					Date dBeginn = (java.util.Date) o1[REPORT_AUFTRAGSWERTE_LOS_BEGINNTERMIN];

					losBeginn1 = dBeginn.getTime() + "";
				}

				String s = Helper.fitString2Length(auftrag, 50, ' ')
						+ Helper.fitString2LengthAlignRight(losBeginn, 50, '0') + o[REPORT_AUFTRAGSWERTE_ARTIKELNUMMER];
				String s1 = Helper.fitString2Length(auftrag1, 50, ' ')
						+ Helper.fitString2LengthAlignRight(losBeginn1, 50, '0')
						+ o1[REPORT_AUFTRAGSWERTE_ARTIKELNUMMER];

				if (s.compareTo(s1) > 0) {
					alDaten.set(j, o1);
					alDaten.set(j + 1, o);

				}
			}
		}

		// Lagerstaende verbrauchen
		for (int i = 0; i < alDaten.size(); i++) {

			Object[] oZeile = (Object[]) alDaten.get(i);

			BigDecimal bdLagerstand = hmLagerstaende.get(oZeile[REPORT_AUFTRAGSWERTE_ARTIKELNUMMER]);

			if (bdLagerstand.doubleValue() > 0) {

				BigDecimal fehlmenge = (BigDecimal) oZeile[REPORT_AUFTRAGSWERTE_FEHLMENGE];
				BigDecimal reservierung = (BigDecimal) oZeile[REPORT_AUFTRAGSWERTE_RESERVIERT];

				if (reservierung != null) {
					bdLagerstand = bdLagerstand.subtract(reservierung);
				}

				if (fehlmenge != null) {
					bdLagerstand = bdLagerstand.subtract(fehlmenge);
				}

				if (bdLagerstand.doubleValue() < 0) {
					bdLagerstand = BigDecimal.ZERO;
				}

			}

			oZeile[REPORT_AUFTRAGSWERTE_LAGERSTANDSVERBRAUCH] = bdLagerstand;

			hmLagerstaende.put((String) oZeile[REPORT_AUFTRAGSWERTE_ARTIKELNUMMER], bdLagerstand);

		}

		data = new Object[alDaten.size()][REPORT_AUFTRAGSWERTE_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(data);
		sAktuellerReport = ArtikelReportFac.REPORT_AUFTRAGSWERTE;
		initJRDS(parameter, ArtikelReportFac.REPORT_MODUL, ArtikelReportFac.REPORT_AUFTRAGSWERTE,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	/**
	 * Fuer einen bestimmten Artikel die Rahmenreservierung des Auftrags drucken.
	 * 
	 * @param kritDtoI     die Auswertungskriterien des Benutzers
	 * @param theClientDto der aktuelle Benutzer
	 * @return JasperPrint der Druck
	 * @throws EJBExceptionLP Ausnahme
	 */
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printRahmenreservierungsliste(ReportAnfragestatistikKriterienDto kritDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (kritDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("kritDtoI == null"));
		}

		JasperPrintLP jasperPrint = null;
		try {
			// es gilt das Locale des Benutzers

			// die Daten fuer den Report ueber Hibernate holen
			ReportRahmenreservierungDto[] aReportRahmenreservierungDto = getReportRahmenreservierung(kritDtoI,
					theClientDto);

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

				data[i][REPORT_RAHMENRESERVIERUNG_AUFTRAGCNR] = reportRahmenreservierungDto.getAuftragCNr();
				data[i][REPORT_RAHMENRESERVIERUNG_KUNDECNAME1] = reportRahmenreservierungDto.getCKundenname();
				data[i][REPORT_RAHMENRESERVIERUNG_AUFTRAGCBEZ] = reportRahmenreservierungDto.getCBez();
				data[i][REPORT_RAHMENRESERVIERUNG_AUFTRAGPOSITIONRAHMENTERMIN] = reportRahmenreservierungDto
						.getTUebersteuerterLiefertermin();
				data[i][REPORT_RAHMENRESERVIERUNG_AUFTRAGPOSITIONMENGE] = reportRahmenreservierungDto.getNMenge();
				data[i][REPORT_RAHMENRESERVIERUNG_AUFTRAGPOSITIONOFFENEMENGE] = reportRahmenreservierungDto
						.getNOffeneMenge();
				data[i][REPORT_RAHMENRESERVIERUNG_AUFTRAGPOSITIONGELIEFERT] = reportRahmenreservierungDto
						.getNGelieferteMenge();
			}

			// die Parameter dem Report uebergeben
			HashMap<String, Object> parameter = new HashMap<String, Object>();

			parameter.put("P_FILTER",
					formatRahmenreservierungKriterien(kritDtoI, theClientDto.getLocUi(), theClientDto));
			if (kritDtoI.getArtikelIId() != null) {
				ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(kritDtoI.getArtikelIId(),
						theClientDto);

				parameter.put("P_ARTIKELNUMMER", artikelDto.getCNr());
				parameter.put("P_ARTIKELBEZEICHNUNG", artikelDto.formatBezeichnung());
				parameter.put("P_ARTIKELREFERENZNUMMER", artikelDto.getCReferenznr());
			}

			initJRDS(parameter, ArtikelReportFac.REPORT_MODUL, ArtikelReportFac.REPORT_RAHMENRESERVIERUNG,
					theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

			jasperPrint = getReportPrint();
		} catch (Exception t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, t);
		}

		return jasperPrint;
	}

	/**
	 * String zum Andrucken der Filterkriterien.
	 * 
	 * @param kritDtoI die Kriterien
	 * @param localeI  das bei der Formatierung gewuenschte Locale
	 * @param cNrUserI der aktuelle Benutzer
	 * @return String die Filterkriterien
	 * @throws EJBExceptionLP Ausnahme
	 */
	private String formatRahmenreservierungKriterien(ReportAnfragestatistikKriterienDto kritDtoI, Locale localeI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		StringBuffer buff = new StringBuffer("");

		// Belegdatum
		if (kritDtoI.getDVon() != null || kritDtoI.getDBis() != null) {
			buff.append("\n")
					.append(getTextRespectUISpr("lp.rahmentermin", theClientDto.getMandant(), theClientDto.getLocUi()));
		}

		if (kritDtoI.getDVon() != null) {
			buff.append(" ").append(getTextRespectUISpr("lp.von", theClientDto.getMandant(), theClientDto.getLocUi()));
			buff.append(" ").append(Helper.formatDatum(kritDtoI.getDVon(), localeI));
		}

		if (kritDtoI.getDBis() != null) {
			buff.append(" ").append(getTextRespectUISpr("lp.bis", theClientDto.getMandant(), theClientDto.getLocUi()));
			buff.append(" ").append(Helper.formatDatum(kritDtoI.getDBis(), localeI));
		}

		String cBuffer = buff.toString().trim();

		return cBuffer;
	}

	/**
	 * Diese Methode liefert eine Liste von allen Auftraegen zu einem bestimmten
	 * Artikel, die nach den eingegebenen Kriterien des Benutzers zusammengestellt
	 * wird. <br>
	 * Achtung: Hibernate verwendet lazy initialization, d.h. der Zugriff auf
	 * Collections muss innerhalb der Session erfolgen.
	 * 
	 * @param kritDtoI     die Kriterien des Benutzers
	 * @param theClientDto der aktuelle Benutzer
	 * @return ReportRahmenreservierungDto[] die Liste der Auftraege
	 * @throws EJBExceptionLP Ausnahme
	 */
	public ReportRahmenreservierungDto[] getReportRahmenreservierung(ReportAnfragestatistikKriterienDto kritDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		ReportRahmenreservierungDto[] aResult = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;

		try {

			session = factory.openSession();

			// Hiberante Criteria fuer alle Tabellen ausgehend von meiner
			// Haupttabelle anlegen,
			// nach denen ich filtern und sortieren kann
			Criteria critAuftragposition = session.createCriteria(FLRAuftragpositionReport.class);

			// flrauftragpositionreport > flrauftrag
			Criteria critAuftrag = critAuftragposition
					.createCriteria(AuftragpositionFac.FLR_AUFTRAGPOSITION_FLRAUFTRAG);

			// Einschraenken auf Rahmenauftraege des Mandanten
			critAuftrag.add(Restrictions.eq(AuftragFac.FLR_AUFTRAG_MANDANT_C_NR, theClientDto.getMandant()));
			critAuftrag
					.add(Restrictions.eq(AuftragFac.FLR_AUFTRAG_AUFTRAGART_C_NR, AuftragServiceFac.AUFTRAGART_RAHMEN));

			// keine stornierten oder erledigten Auftraege.
			Collection<String> cStati = new LinkedList<String>();
			cStati.add(AuftragServiceFac.AUFTRAGSTATUS_STORNIERT);
			cStati.add(AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT);
			critAuftrag.add(Restrictions.not(Restrictions.in(AuftragFac.FLR_AUFTRAG_AUFTRAGSTATUS_C_NR, cStati)));

			// Einschraenkung auf den gewaehlten Artikel
			if (kritDtoI.getArtikelIId() != null) {
				critAuftragposition.add(
						Restrictions.eq(AuftragpositionFac.FLR_AUFTRAGPOSITION_ARTIKEL_I_ID, kritDtoI.getArtikelIId()));
			}
			/*
			 * // die offene Menge muss != null > 0 sein critAuftragposition.add(
			 * Restrictions.isNotNull(AuftragpositionFac. FLR_AUFTRAGPOSITION_N_MENGE));
			 * critAuftragposition.add(Restrictions.gt(AuftragpositionFac.
			 * FLR_AUFTRAGPOSITION_N_OFFENEMENGE, new BigDecimal(0)));
			 */
			// Einschraenkung nach Rahmentermin von - bis
			if (kritDtoI.getDVon() != null) {
				critAuftragposition.add(Restrictions
						.ge(AuftragpositionFac.FLR_AUFTRAGPOSITION_T_UEBERSTEUERTERLIEFERTERMIN, kritDtoI.getDVon()));
			}

			if (kritDtoI.getDBis() != null) {
				critAuftragposition.add(Restrictions
						.le(AuftragpositionFac.FLR_AUFTRAGPOSITION_T_UEBERSTEUERTERLIEFERTERMIN, kritDtoI.getDBis()));
			}

			// es wird nach Belegnummer und Artikel sortiert
			critAuftrag.addOrder(Order.asc(AuftragFac.FLR_AUFTRAG_C_NR));

			List<?> list = critAuftrag.list();
			aResult = new ReportRahmenreservierungDto[list.size()];
			Iterator<?> it = list.iterator();
			int iIndex = 0;

			while (it.hasNext()) {
				FLRAuftragpositionReport flrauftragposition = (FLRAuftragpositionReport) it.next();
				FLRAuftragReport flrauftrag = flrauftragposition.getFlrauftrag();
				FLRPartner flrpartner = flrauftrag.getFlrkunde().getFlrpartner();

				ReportRahmenreservierungDto reportDto = new ReportRahmenreservierungDto();

				reportDto.setAuftragCNr(flrauftrag.getC_nr());
				reportDto.setAuftragIId(flrauftrag.getI_id());
				reportDto.setArtikelIId(flrauftragposition.getArtikel_i_id());
				reportDto.setCBez(flrauftrag.getC_bez());
				reportDto.setCKundenname(flrpartner.getC_name1nachnamefirmazeile1());
				reportDto.setTUebersteuerterLiefertermin(
						new Timestamp(flrauftragposition.getT_uebersteuerterliefertermin().getTime()));
				reportDto.setNMenge(flrauftragposition.getN_menge());
				// offene (rahmenmenge-abgerufenemenge)
				reportDto.setNOffeneMenge(flrauftragposition.getN_offenerahmenmenge());
				// geliefert

				if (flrauftragposition.getN_offenerahmenmenge() != null) {
					reportDto.setNGelieferteMenge(
							flrauftragposition.getN_menge().subtract(flrauftragposition.getN_offenerahmenmenge()));
				} else {
					reportDto.setNGelieferteMenge(flrauftragposition.getN_menge());
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

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public JasperPrintLP printArtikeletikettOnServer(Integer artikelIId, String sKommentar, BigDecimal bdMenge,
			String[] cSnrChnr, Integer lagerIIdfuerLagerstandDerCharge, String lfdnr, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP {
		return printArtikeletikettOnServer(artikelIId, sKommentar, bdMenge, cSnrChnr, lagerIIdfuerLagerstandDerCharge,
				lfdnr, 1, theClientDto);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public JasperPrintLP printArtikeletikettOnServer(Integer artikelIId, String sKommentar, BigDecimal bdMenge,
			String[] cSnrChnr, Integer lagerIIdfuerLagerstandDerCharge, String lfdnr, Integer exemplare,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP {
		Validator.pkFieldNotNull(artikelIId, "artikelIId");

		if (cSnrChnr != null) {
			validateSerienChargennummern(artikelIId, cSnrChnr, theClientDto);
		}

		String printer = getServerDruckerFacLocal().getPrinterNameByArbeitsplatzparameter(
				ParameterFac.ARBEITSPLATZPARAMETER_DRUCKERNAME_MOBILES_ARTIKEL_ETIKETT, theClientDto);

		JasperPrintLP print = printArtikeletikett(artikelIId, sKommentar, bdMenge, exemplare, cSnrChnr,
				lagerIIdfuerLagerstandDerCharge, lfdnr, null, theClientDto);
		HvPrinter hvPrinter = getServerDruckerFacLocal().createHvPrinter(printer);
		if (getServerDruckerFacLocal().exists(hvPrinter)) {
			getServerDruckerFacLocal().print(print, hvPrinter);
		}

		return print;
	}

	private void validateSerienChargennummern(Integer artikelIId, String[] serienChargennummern,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP {
		List<String> unknown = new ArrayList<String>();
		for (String snrChnr : serienChargennummern) {
			LagerbewegungDto[] lagerbewegungen = getLagerFac()
					.lagerbewegungFindByArtikelIIdCSeriennrChargennr(artikelIId, snrChnr);
			if (lagerbewegungen == null || lagerbewegungen.length == 0) {
				unknown.add(snrChnr);
			}
		}

		if (!unknown.isEmpty()) {
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(artikelIId, theClientDto);
			throw EJBExcFactory.unbekannteSerienChargennummer(artikelDto, unknown);
		}
	}
}
